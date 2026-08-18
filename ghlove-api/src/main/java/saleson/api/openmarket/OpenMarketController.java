package saleson.api.openmarket;

import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.PropertiesUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.model.ConfigPg;
import saleson.shop.openmarket.domain.ItemStack;
import saleson.shop.openmarket.domain.NaverItem;
import saleson.shop.openmarket.domain.NaverOrder;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.pg.config.ConfigPgService;


@Slf4j
@RequiredArgsConstructor
@RestController("ApiOpenMarketController")
@RequestMapping("/api/open-market")
public class OpenMarketController {
    private final Environment environment;

    private final ConfigPgService configPgService;

    @PostMapping("/checkOutReturn")
    public ResponseEntity checkOutReturn(HttpServletRequest request, @RequestBody String paramData) {
        ResponseEntity result = null;
        try {
            paramData = URLDecoder.decode(paramData, "utf-8");
            paramData = paramData.substring(0, paramData.lastIndexOf("}")+1);
        } catch (UnsupportedEncodingException e) {
            log.error("ERROR: {}", e);
            return ApiResponseEntity.error(ApiError.FAIL_CALL_NPAY);
        }

        ConfigPg configPg = configPgService.getConfigPg();

        if (configPg == null) {
            log.error("PG 정보가 없습니다.");
            return ApiResponseEntity.error(ApiError.FAIL_CALL_NPAY);
        }

        if (!configPg.isUseNpayOrder()) {
            log.error("네이버 페이(주문형)을 사용안합니다.");
            return ApiResponseEntity.error(ApiError.FAIL_CALL_NPAY);
        }

        // json string로 naveItem 생성
        NaverItem naverItem = new NaverItem(paramData);

        int totalPrice = 0;
        int totalShipping = 0;
        // 주문 상품 내역으로 items 데이터를 생성
        List<ItemStack> items = new ArrayList<>();


        String domain = PropertiesUtils.getProperty("saleson.url.shoppingmall");
        String imageDomain = PropertiesUtils.getProperty("resource.location");
        String backURL = "";

        for (OrderItem orderItem : naverItem.getOrderItem()) {
            int itemPrice = (orderItem.getPrice() + orderItem.getOptionPrice())*orderItem.getQuantity();
            String products = "/items/details.html?code=" + orderItem.getItemUserCode();

            if ("cart".equals(naverItem.getType())) {
                backURL = domain + "/cart/index.html";
            } else {
                backURL = domain + "/items/details.html?code=" + orderItem.getItemUserCode();
            }

            items.add(new ItemStack(Integer.toString(orderItem.getItemId()), orderItem.getItemName(), itemPrice, naverItem.getShipping(), orderItem.getOptions(), orderItem.getQuantity(), imageDomain + orderItem.getImageSrc() , domain + products, orderItem.getItemUserCode()));
        }

        totalShipping = naverItem.getShipping();

        // 주문가격에 배송비 추가
        totalPrice += totalShipping;

        String shippingType = totalShipping==0 ? "FREE" : "PAYED";
        String url = "";
		/*String url = environment.getProperty("naver.checkout.order.api.url") + "?SHOP_ID=" + environment.getProperty("naver.checkout.merchantId")
				+ "&CERTI_KEY=" + environment.getProperty("naver.checkout.certi.key")
				+ "&ITEM_ID="+naverItem.getItemId()+"&ITEM_NAME="+naverItem.getItemName()+"&ITEM_TPRICE="+naverItem.getPrice()+""
				+ "&ITEM_OPTION=&ITEM_UPRICE=1000&ITEM_COUNT="+naverItem.getQuantity()
				+ "&BACK_URL=http://www.naver.com&TOTAL_PRICE=" + totalPrice + "&SHIPPING_TYPE="+ shippingType + "&SHIPPING_PRICE=" + naverItem.getShipping();*/

        int shippingPrice = totalShipping;


        String shopId = configPg.getNpayMid();
        String orderKey = "";

        try {
            NaverOrder sample = new NaverOrder(environment.getProperty("naver.checkout.order.api.url"));
            // servlet인 경우 쿠키값을 넣어야 함
            if (sample != null) {
                String nvadId = sample.getCookieValue(request, "NVADID");

                orderKey = sample.sendOrderInfoToNC(shopId, configPg.getNpayKey(),
                        items.toArray(new ItemStack[0]), shippingPrice, shippingType, backURL, nvadId, url);
            }
        } catch (IOException e) {
            //result = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
        	result = ApiResponseEntity.error(ApiError.BAD_REQUEST, ApiError.BAD_REQUEST.getDescription());
        }

        // 성공시 데이터 response로 보냄
        // flag1 = 성공여부, orderKey = 생성된 네이버페이 주문번호, price = 총주문액수, flag2 = 팝업여부, shopId = 네이버페이 아이디
        Boolean isMobile = CommonUtils.isMobile(request);
        String payUrl = environment.getProperty("naver.checkout.pc.payUrl");

        if (isMobile) {
            payUrl = environment.getProperty("naver.checkout.mobile.payUrl");
        }

        JSONObject json = new JSONObject();

        json.put("flag1", true);
        json.put("orderKey", orderKey);
        json.put("price", totalPrice);
        json.put("isMobile", isMobile);
        json.put("shopId", shopId);
        json.put("payUrl", payUrl);

        result = ApiResponseEntity.data().put("json", json).ok();
        return result;
    }

    @PostMapping("/checkOutWishReturn")
    public ResponseEntity checkOutWishReturn(HttpServletRequest request, @RequestBody String paramData) {
        ResponseEntity result = null;
        try {
            paramData = URLDecoder.decode(paramData, "utf-8");
            paramData = paramData.substring(0, paramData.lastIndexOf("}")+1);
        } catch (UnsupportedEncodingException e) {
            log.error("ERROR: {}", e);
            return ApiResponseEntity.error(ApiError.FAIL_CALL_NPAY);
        }

        ConfigPg configPg = configPgService.getConfigPg();

        if (configPg == null) {
            log.error("PG 정보가 없습니다.");
            return ApiResponseEntity.error(ApiError.FAIL_CALL_NPAY);
        }

        if (!configPg.isUseNpayOrder()) {
            log.error("네이버 페이(주문형)을 사용안합니다.");
            return ApiResponseEntity.error(ApiError.FAIL_CALL_NPAY);
        }

        // json string로 naveItem 생성
        NaverItem naverItem = new NaverItem(paramData);

        String shopId = configPg.getNpayMid();

        List<ItemStack> items = new ArrayList<>();

        String domain = PropertiesUtils.getProperty("saleson.url.shoppingmall");
        String imageDomain = PropertiesUtils.getProperty("resource.location");

        for (OrderItem orderItem : naverItem.getOrderItem()) {

            String products = "/items/details.html?code=" + orderItem.getItemUserCode();


            items.add(new ItemStack(Integer.toString(orderItem.getItemId()), orderItem.getItemName(), orderItem.getPrice(), imageDomain + orderItem.getImageSrc(), imageDomain + orderItem.getImageSrc(), domain + products, orderItem.getItemUserCode()));
        }
        String[] itemId = null;
        try {
            NaverOrder sample = new NaverOrder(environment.getProperty("naver.checkout.wishlist.api.url"));
            itemId = sample.sendZzimToNC(shopId, configPg.getNpayKey(), items.toArray(new ItemStack[0]));
            //여기서 얻은prodSeqs로 zzim popup을 띄운다.
//            System.out.println("zzim::" + Arrays.toString(itemId));
        } catch (IOException e) {
            //result = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
        	result = ApiResponseEntity.error(ApiError.BAD_REQUEST, ApiError.BAD_REQUEST.getDescription());
            log.error("ERROR: {}", e);
        }

        // 성공시 데이터 response로 보냄
        // flag1 = 성공여부, orderKey = 생성된 네이버페이 주문번호, price = 총주문액수, flag2 = 팝업여부, shopId = 네이버페이 아이디
        Boolean isMobile = CommonUtils.isMobile(request);
        String zzimUrl = environment.getProperty("naver.checkout.pc.wishlist");

        if (isMobile) {
            zzimUrl = environment.getProperty("naver.checkout.mobile.wishlist");
        }

        JSONObject json = new JSONObject();

        json.put("flag1", true);
        json.put("shopId", shopId);
        json.put("itemId", itemId);
        json.put("isMobile", isMobile);
        json.put("zzimUrl", zzimUrl);

        result = ApiResponseEntity.data().put("json", json).ok();
        return result;
    }
}
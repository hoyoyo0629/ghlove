package saleson.shop.openmarket;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.PropertiesUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import saleson.model.ConfigPg;
import saleson.shop.openmarket.domain.ItemStack;
import saleson.shop.openmarket.domain.NaverItem;
import saleson.shop.openmarket.domain.NaverOrder;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.pg.config.ConfigPgService;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/open-market/**")
@RequestProperty(layout="default")
public class OpenMarketController {

	private final Environment environment;

    private final ConfigPgService configPgService;

	//
	@PostMapping("/checkOutReturn")
	public JsonView checkOutReturn(RequestContext requestContext, HttpServletRequest request, @RequestBody String paramData) {
		
		try {
			paramData = URLDecoder.decode(paramData, "utf-8");
			paramData = paramData.substring(0, paramData.lastIndexOf("}")+1);
		} catch (UnsupportedEncodingException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: checkOutReturn UnsupportedEncodingException ==========", e);
			return JsonViewUtils.failure("구매중 오류가 발생하였습니다. (Parameter 변환 오류)");
		}

        ConfigPg configPg = configPgService.getConfigPg();

        if (configPg == null) {
            log.error("PG 정보가 없습니다.");
            return JsonViewUtils.failure("구매중 오류가 발생하였습니다. (PG 정보가 없음)");
        }

        if (!configPg.isUseNpayOrder()) {
            log.error("네이버 페이(주문형)을 사용안합니다.");
            return JsonViewUtils.failure("구매중 오류가 발생하였습니다. (네이버 페이 사용안함)");
        }

        // json string로 naveItem 생성
        NaverItem naverItem = new NaverItem(paramData);

        int totalPrice = 0;
        int totalShipping = 0;

        // 주문 상품 내역으로 items 데이터를 생성
        List<ItemStack> items = new ArrayList<>();

        String imageDomain = PropertiesUtils.getProperty("resource.location");
        String backURL = "";

        for (OrderItem orderItem : naverItem.getOrderItem()) {
            int itemPrice = (orderItem.getPrice() + orderItem.getOptionPrice())*orderItem.getQuantity();

            if ("cart".equals(naverItem.getOrderType())) {
                backURL = getCartUrl(naverItem.getDeviceType());
            } else {
                backURL = getItemViewUrl(naverItem.getDeviceType(), orderItem.getItemUserCode());
            }

            items.add(new ItemStack(Integer.toString(orderItem.getItemId()), orderItem.getItemName(), itemPrice, naverItem.getShipping(), orderItem.getOptions(), orderItem.getQuantity(), imageDomain + orderItem.getImageSrc() , getItemViewUrl(naverItem.getDeviceType(), orderItem.getItemUserCode()), orderItem.getItemUserCode()));
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
                String nvadId = sample.getCookieValue((HttpServletRequest) request, "NVADID");

                orderKey = sample.sendOrderInfoToNC(shopId, configPg.getNpayKey(),
                        items.toArray(new ItemStack[0]), shippingPrice, shippingType, backURL, nvadId, url);
            }
        } catch (IOException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
//            return JsonViewUtils.failure(e.getMessage());
            log.error("ERROR: {}", getClass().getName() + " :: checkOutReturn IOException ==========", e);
//            return JsonViewUtils.failure(MessageUtils.getMessage("실패했습니다."));				// 실패했습니다.
            return JsonViewUtils.failure("실패했습니다.");				// 실패했습니다.
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
		return JsonViewUtils.success(json);
	}
	
	//
	@PostMapping("/checkOutWishReturn")
	public JsonView checkOutWishReturn(RequestContext requestContext, HttpServletRequest request, @RequestBody String paramData) {
		
		try {
			paramData = URLDecoder.decode(paramData, "utf-8");
			paramData = paramData.substring(0, paramData.lastIndexOf("}")+1);
		} catch (UnsupportedEncodingException e) {
			
//			log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: checkOutWishReturn UnsupportedEncodingException ==========", e);
//			return JsonViewUtils.failure("구매중 오류가 발생하였습니다. (Parameter 변환 오류)");
		}


        ConfigPg configPg = configPgService.getConfigPg();

        if (configPg == null) {
            log.error("PG 정보가 없습니다.");
            return JsonViewUtils.failure("구매중 오류가 발생하였습니다. (PG 정보가 없음)");
        }

        if (!configPg.isUseNpayOrder()) {
            log.error("네이버 페이(주문형)을 사용안합니다.");
            return JsonViewUtils.failure("구매중 오류가 발생하였습니다. (네이버 페이 사용안함)");
        }

        // json string로 naveItem 생성
        NaverItem naverItem = new NaverItem(paramData);

        String shopId = configPg.getNpayMid();

        List<ItemStack> items = new ArrayList<>();

        String imageDomain = PropertiesUtils.getProperty("resource.location");

        for (OrderItem orderItem : naverItem.getOrderItem()) {
            items.add(new ItemStack(Integer.toString(orderItem.getItemId()), orderItem.getItemName(), orderItem.getPrice(), imageDomain + orderItem.getImageSrc(), imageDomain + orderItem.getImageSrc(), getItemViewUrl(naverItem.getDeviceType(), orderItem.getItemUserCode()), orderItem.getItemUserCode()));
        }
        String[] itemId = null;
        try {
            NaverOrder sample = new NaverOrder(environment.getProperty("naver.checkout.wishlist.api.url"));
            itemId = sample.sendZzimToNC(shopId, configPg.getNpayKey(), items.toArray(new ItemStack[0]));
            //여기서 얻은prodSeqs로 zzim popup을 띄운다.
//            System.out.println("zzim::" + Arrays.toString(itemId));
        } catch (IOException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: checkOutWishReturn IOException ==========", e);
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
		return JsonViewUtils.success(json);
	}

    private String getItemViewUrl(String deviceType, String itemUserCode) {
        String domain = PropertiesUtils.getProperty("saleson.url.shoppingmall");
        String itemViewUrl = ("mobile".equals(deviceType) ? "/m" : "") + "/products/view/" + itemUserCode;

        return domain + itemViewUrl;
    }

    private String getCartUrl(String deviceType) {
        String domain = PropertiesUtils.getProperty("saleson.url.shoppingmall");
        String cartUrl = ("mobile".equals(deviceType) ? "/m" : "") + "/cart";

        return domain + cartUrl;
    }
}

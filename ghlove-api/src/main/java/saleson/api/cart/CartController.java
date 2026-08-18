package saleson.api.cart;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.cart.CartService;
import saleson.shop.cart.domain.Cart;
import saleson.shop.cart.domain.CartSet;
import saleson.shop.cart.support.CartParam;
import saleson.shop.config.ConfigService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.Receiver;
import saleson.shop.order.domain.Shipping;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.order.support.OrderException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@RestController("ApiCartController")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ConfigPgService configPgService;

    @Autowired
    private OrderGivePointService orderGivePointService;

    /**
     * 장바구니 목록 API
     *
     * @param cartParam
     * @return
     */
    @GetMapping
    public ResponseEntity list(HttpServletRequest request, CartParam cartParam){
        ResponseEntity result = null;
        Buy buy = null;

        if (cartParam == null) {
            cartParam = new CartParam();
        }
        long id = UserUtils.getUserId();
        if (UserUtils.isUserLogin()) {
            cartParam.setUserId(UserUtils.getUserId());
        } else {
//            cartParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            return ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
        }

        List<OrderGivePoint> givePointList = orderGivePointService.getGiveBlcePointListByUserId(UserUtils.getUserId());

        try {
            cartParam.setEntryPage("cart");
            buy = cartService.getBuyInfoByCart(cartParam); // 사용자 장바구니 데이터 조회
            result = ApiResponseEntity.data()
                    .put("list", cartListDataSet(buy.getReceivers()))
                    .put("orderPrice",buy.getOrderPrice())
                    .put("displayNaverPayFlag", configPgService.isDisplayNaverPayFlag())
                    .put("givePointList", givePointList)
                    .ok();
        } catch(RuntimeException e){
            //result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getMessage());
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-30: System Error (시스템 에러)");
        }
        return result;
    }

    private List<Receiver> cartListDataSet(List<Receiver> data){
        List<Receiver> resultList = new ArrayList<>();
        if (data != null) {
            Receiver receiver = data.get(0);
            List<Shipping> itemGroups = receiver.getItemGroups();

            for (Shipping itemGroup : itemGroups) {
                if (itemGroup.getBuyItem() != null) {
                    BuyItem item = itemGroup.getBuyItem();
                    item.getItem().setSupplyPrice(0);
                    item.setOptions(ShopUtils.viewItemOptions(item.getSetItemFlag(), item.getOptions()));
                    item.getItem().setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItem().getItemImage(), "S"));
                } else {
                    for (BuyItem item : itemGroup.getBuyItems()) {
                        item.getItem().setSupplyPrice(0);
                        item.setOptions(ShopUtils.viewItemOptions(item.getSetItemFlag(), item.getOptions()));
                        item.getItem().setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItem().getItemImage(), "S"));
                    }
                }
            }

            receiver.setItemGroups(itemGroups);
            resultList.add(receiver);
        }
        return resultList;
    }

    /**
     * 장바구니 담기 API (arrayRequiredItems)
     *
     * @param cart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity add(HttpServletRequest request, @RequestBody(required = false) Cart cart, HttpSession session) {
        ResponseEntity result = null;
        if (cart == null) {
            cart = new Cart();
        }
        if (cart.getItemId() != 0) {
            cart.setArrayRequiredItems(addParamSet(cart));
        }

        cart.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
        if (UserUtils.isUserLogin()) {
            cart.setUserId(UserUtils.getUserId());
        }

        try {
            CartParam cartParam = new CartParam();
            cartParam.setUserId(UserUtils.getUserId());
            cartParam.setSessionId(session.getId());

            int cbmc = 0;
            // 세트상품이 존재하면
            if(cart.getItemSets() != null && cart.getItemSets().size() > 0){
                for(CartSet cs : cart.getItemSets()){
                    cbmc = cartBuyMaxMinCheck(cartParam, CommonUtils.dataAryNvl(cs.getArrayItemSets()));
                    if(cbmc == 1 || cbmc == 2 || cbmc == 3){
                        break;
                    }
                }
            } else {
                cbmc = cartBuyMaxMinCheck(cartParam, CommonUtils.dataAryNvl(cart.getArrayRequiredItems()));
            }

            if(cbmc == 1){
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_CART_BUY_MIN_FAIL);
            } else if(cbmc == 2){
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_CART_BUY_MAX_FAIL);
            } else if(cbmc == 3){
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_CART_BUY_LIMIT_FAIL);
            } else {
                int insertCount = cartService.insertCart(cart);
                if (insertCount == 0) {
                    throw new IllegalStateException("주문 상품 처리중 오류가 발생 하였습니다. \\n주문을 다시 시도해 주세요.");
                }
                result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
            }
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    private String[] addParamSet(Cart cart){
        StringBuilder buffer = new StringBuilder();
        String[] result = new String[1];
        buffer.append(cart.getItemId());
        buffer.append("||");
        buffer.append(cart.getQuantity());
        buffer.append("||");
        result[0] = buffer.toString();
        return result;
    }

    private int cartBuyMaxMinCheck(CartParam cartParam, String[] arrayRequiredItems){
        int result = 0;

        int itemQuantity = cartParam.getQuantity();

        if (arrayRequiredItems.length > 0) {

            for(int i = 0; i < arrayRequiredItems.length; i++) {
                String itemString = arrayRequiredItems[i];
                String[] itemInfo = StringUtils.delimitedListToStringArray(itemString, "||");

                if (itemInfo.length != 3) {
                    continue;
                }

                cartParam.setItemId(Integer.parseInt(itemInfo[0]));
                itemQuantity += Integer.parseInt(itemInfo[1]);
            }

        }

        List<BuyItem> itemList = cartService.getCartList(cartParam, true);
        // 장바구니가 있는 경우
        if (!itemList.isEmpty()) {
            int totalQuantity = 0;
            int maxQuantity = 0;
            int minQuantity = 0;
            for (BuyItem buyItem : itemList) {
                // [손준의] 추가하려는 상품과 동일한 장바구니 상품인지 확인 후 해당 상품의 구매제한 확인
                if (cartParam.getItemId() == buyItem.getItemId() || buyItem.getItemPrice().getQuantity() != 0) {
                    totalQuantity += CommonUtils.intNvl(buyItem.getItemPrice().getQuantity());
                    maxQuantity = buyItem.getItem().getOrderMaxQuantity();
                    minQuantity = buyItem.getItem().getOrderMinQuantity();
                }
            }

            totalQuantity += itemQuantity;
            if (maxQuantity > 0 && totalQuantity > maxQuantity) {
                result = 2;
            }
            if (minQuantity > 0 && totalQuantity < minQuantity){
                result = 1;
            }
            if (totalQuantity > 999) {
                result = 3;
            }

            // 장바구니가 없는 경우
        } else {
            Item item = itemService.getItemById(cartParam.getItemId());
            // 최소 구매 수량 미달 시
            if(item.getOrderMinQuantity() > 0 && itemQuantity < item.getOrderMinQuantity()){
                result = 1;
            }
            // 최대 구매수량 초과 시
            if(item.getOrderMaxQuantity() > 0 && itemQuantity > item.getOrderMaxQuantity()){
                result = 2;
            }
            if (itemQuantity > 999) {
                result = 3;
            }
        }
        return result;
    }

    /**
     * 장바구니 삭제 API (id)
     *
     * @param cartParam
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity delete(HttpServletRequest request, @RequestBody(required = false) CartParam cartParam){
        ResponseEntity result = null;
        if(cartParam == null){
            cartParam = new CartParam();
        }
        if (UserUtils.isUserLogin()) {
            cartParam.setUserId(UserUtils.getUserId());
        } else {
            cartParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
        }
        try {
            cartService.deleteListData(cartParam);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 장바구니 수량 변경 API (cartId, quantity)
     *
     * @param session
     * @param cartParam
     * @return
     */
    @PostMapping("/update-quantity")
    public ResponseEntity updateQuantity(HttpServletRequest request, HttpSession session, @RequestBody(required = false) CartParam cartParam){
        ResponseEntity result = null;
        try {
            if (UserUtils.isUserLogin()) {
                cartParam.setUserId(UserUtils.getUserId());
            } else {
                cartParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            }

            cartService.updateQuantity(cartParam);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(OpRuntimeException e){
            if ("장바구니에 담긴 수량이 최대 구매 가능 수량을 초과하였습니다.".equals(e.getErrorMessage())) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_CART_BUY_MAX_FAIL);
            } else if ("장바구니에 담긴 수량이 최소 구매 가능 수량을 미달하였습니다.".equals(e.getErrorMessage())) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_CART_BUY_MIN_FAIL);
            } else if ("장바구니에 담긴 수량은 999개를 초과할 수 없습니다.".equals(e.getErrorMessage())) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_CART_BUY_LIMIT_FAIL);
            } else {
                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            }

        }
        return result;
    }

    /**
     *  장바구니 배송비 지불방법 설정 API
     * @param cartParam
     * @return
     */
    @PostMapping("shipping-payment-type")
    public ResponseEntity shippingPaymentType(HttpServletRequest request, @RequestBody(required = false) CartParam cartParam){
        ResponseEntity result = null;

        if (UserUtils.isUserLogin()) {
            cartParam.setUserId(UserUtils.getUserId());
        } else {
            cartParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
        }

        try {
            cartService.updateShippingPaymentType(cartParam);
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }


}

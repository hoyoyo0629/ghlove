package saleson.shop.cart;

import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.NumberUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import net.sf.json.JSONObject;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.GiftItem;
import saleson.shop.cart.domain.Cart;
import saleson.shop.cart.domain.CartSet;
import saleson.shop.cart.domain.OrderQuantity;
import saleson.shop.cart.support.CartParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.giftitem.GiftItemService;
import saleson.shop.item.ItemMapper;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemOption;
import saleson.shop.item.domain.ItemSet;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.*;
import saleson.shop.order.support.OrderException;
import saleson.shop.point.PointService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("cartService")
public class CartServiceImpl extends EgovAbstractServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GiftItemService giftItemService;

    @Override
    public List<BuyItem> getCartList(CartParam cartParam, boolean isOrder) {
        List<BuyItem> list = new ArrayList<>();

        cartParam.setEntryPage("cart");
        List<BuyItem> baseList = cartMapper.getCartList(cartParam);
        for (BuyItem item : baseList) {
            if ("Y".equals(item.getSetItemFlag())) {
                cartParam.setParentCartId(item.getCartId());
                item.setItemSets(cartMapper.getCartSetList(cartParam));
            }

            list.add(item);
        }

        if (ValidationUtils.isNotNull(list)) {
            List<String> systemComments = new ArrayList<>();
            List<BuyItem> impossibleToPurchaseProducts = null;

            // 재고 차감 + 재고 검증용
            HashMap<String, Integer> buyQuantityMap = new HashMap<>();

            // 옵션 데이터 셋팅
            for(int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);

                Item item = buyItem.getItem();
                item.setItemOptions(itemMapper.getItemOptionList(item.getItemId()));
                buyItem.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItem.getOptions()));    // 상품의 필수 옵션을 구성
            }

            // 구매 상품중에 재고연동 사용하는 상품의 총 재고량 목록
            HashMap<String, HashMap<String, Integer>> stockMap = ShopUtils.makeStockMap(list);

            if (stockMap == null) {
                stockMap = new HashMap<>();
            }

            // 최소 구매수량 체크용
            HashMap<String, Integer> buyQuantityItemUserCodeMap = new HashMap<>();
            for(int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);
                Item item = buyItem.getItem();
                if(item.getItemUserCode() != null && buyItem.getItemPrice() != null) {
                	 if (buyQuantityItemUserCodeMap.get(item.getItemUserCode()) == null) {
                 		buyQuantityItemUserCodeMap.put(item.getItemUserCode(), CommonUtils.intNvl(buyItem.getItemPrice().getQuantity()));
                     } else {
                         buyQuantityItemUserCodeMap.put(item.getItemUserCode(), buyQuantityItemUserCodeMap.get(item.getItemUserCode()) + CommonUtils.intNvl(buyItem.getItemPrice().getQuantity()));
                     }
                }

            }

            // 사은품 데이터 셋팅
            for(int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);

                if (buyItem == null) {
                    continue;
                }

                try {
                    List<GiftItem> freeGiftItemList = giftItemService.getGiftItemListForFront(buyItem.getItemId());
                    buyItem.setFreeGiftItemText(ShopUtils.makeGiftItemText(freeGiftItemList));
                    buyItem.setFreeGiftItemList(ShopUtils.conventGiftItemInfoList(freeGiftItemList));
                } catch (RuntimeException ignore) {
                    log.error(getClass().getName() + " :: getCartList RuntimeException1 ============");
                }
            }

            for (int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);

                if (buyItem == null) {
                    continue;
                }

                Item item = buyItem.getItem();
                ItemPrice itemPrice = buyItem.getItemPrice();

                ShopUtils.setBuyQuantityMap(buyItem, stockMap, buyQuantityMap);
                OrderQuantity orderQuantity = buyItem.getOrderQuantity();

                // 상품이 판매 종료된 상품인지 검사
                if (orderQuantity.getMaxQuantity() == 0) {
                    buyItem.setAvailableForSaleFlag("N");
                    buyItem.setSystemComment(MessageUtils.getMessage("M00483")); // 이쪽의 상품은 재고가 없습니다.
                }

                // 상품 코드가 같은 상품의 구매 총 수량이 상품에 설정된 최소 구매수량보다 적은경우 구매 못함
                String itemUserCode = item.getItemUserCode();
                if (buyQuantityItemUserCodeMap.get(itemUserCode) != null) {
                    if (item.getOrderMinQuantity() > buyQuantityItemUserCodeMap.get(itemUserCode)) {
                        buyItem.setAvailableForSaleFlag("N");
                    }
                }

                // 추가 구성품인경우 본품이 있는지 체크
                if ("Y".equals(buyItem.getAdditionItemFlag())) {
                    boolean isDelete = true;
                    for (BuyItem bItem : list) {
                        if (bItem.getItemId() == buyItem.getParentItemId()) {
                            isDelete = false;
                            break;
                        }
                    }

                    if (isDelete) {
                        buyItem.setAvailableForSaleFlag("N");
                    }
                }

                // 세트상품의 구성품이 변경되었는지 체크
                if ("Y".equals(buyItem.getSetItemFlag())) {
                    List<BuyItem> buyItemSets = buyItem.getItemSets();
                    List<ItemSet> itemSets = itemMapper.getItemSetListByItemId(buyItem.getItemId());

                    // 세트 정보 없을경우 구매 불가
                    if (buyItemSets == null || buyItemSets.isEmpty() || itemSets == null || itemSets.isEmpty()) {
                        buyItem.setAvailableForSaleFlag("N");
                        buyItem.setSystemComment(buyItem.getItemName() + "상품의 세트 구성이 없습니다.");
                    } else {    // 구매 세트 정보와 실제 세트 정보 비교하여 구성품이 달라졌는지 체크
                        // 세트상품 재고차감 + 재고검증용
                        HashMap<String, Integer> buySetQuantityMap = new HashMap<>();
                        HashMap<String, HashMap<String, Integer>> stockSetMap = ShopUtils.makeStockSetMap(buyItem, buyItemSets);

                        if (stockSetMap == null) {
                            stockSetMap = new HashMap<>();
                        }

                        for (BuyItem buyItemSet : buyItemSets) {
                            boolean isDelete = true;
                            if(buyItemSet.getItemPrice() != null) {
                            	for (ItemSet itemSet : itemSets) {
                                    if (CommonUtils.intNvl(buyItemSet.getItemId())  == CommonUtils.intNvl(itemSet.getItemId()) && CommonUtils.intNvl(buyItemSet.getItemPrice().getQuantity()) == CommonUtils.intNvl(itemSet.getQuantity())) {
                                        isDelete = false;
                                        break;
                                    }
                                }
                            }


                            // 세트 구성품이 변경되었으면 구매 불가
                            if (isDelete) {
                                buyItemSet.setAvailableForSaleFlag("N");
                                buyItemSet.setSystemComment(buyItem.getItemName() + "상품의 세트 구성이 변경되어 구매가 불가능합니다. (" + buyItemSet.getItemName() + ")");
                            } else {
                                // 1. 세트상품의 옵션 설정
                                buyItemSet.getItem().setItemOptions(itemMapper.getItemOptionList(buyItemSet.getItem().getItemId()));
                                buyItemSet.setOptionList(ShopUtils.getRequiredItemOptions(buyItemSet.getItem(), buyItemSet.getOptions()));

                                // 2. 세트상품 재고 차감 정보 생성
                                ShopUtils.setBuySetQuantityMap(buyItem, buyItemSet, stockSetMap, buySetQuantityMap);

                                // 3. 상품이 판매 종료된 상품인지 검사
                                if (buyItemSet.getOrderQuantity().getMaxQuantity() == 0) {
                                    buyItem.setAvailableForSaleFlag("N");
                                    buyItem.setSystemComment(buyItemSet.getItemName() + "의 재고가 없습니다.");
                                }
                            }
                        }
                    }
                }

                if ("Y".equals(buyItem.getAvailableForSaleFlag())) {

                    // 적립금은 로그인한 회원에게만 부여됨..
                    if (UserUtils.isUserLogin() && isOrder) {
                        buyItem.setPointPolicy(pointService.getPointPolicyByItemId(item.getItemId()));
                    }

                    // 최대 구매가능 수량보다 장바구니에 담겨있는 수량이 많은경우 장바구니 구매수량 변경
                    if (itemPrice.getQuantity() > orderQuantity.getMaxQuantity() && orderQuantity.getMaxQuantity() > 0) {
                        itemPrice.setQuantity(orderQuantity.getMaxQuantity());
                        buyItem.getItemPrice().setQuantity(orderQuantity.getMaxQuantity());

                        systemComments.add(item.getItemName() + MessageUtils.getMessage("M00487")); // oo상품의 재고 부족으로 상품 구매 수량이 변경 되었습니다.

                        // DB의 상품의 구매수량을 변경함..
                        CartParam quantityUpdateCartParam = cartParam;
                        quantityUpdateCartParam.setQuantity(itemPrice.getQuantity());
                        quantityUpdateCartParam.setCartId(buyItem.getCartId());
                        cartMapper.updateQuantity(quantityUpdateCartParam);
                    }

                    // 상품별 금액 셋팅!!
                    buyItem.setItemPrice(new ItemPrice(buyItem));

                } else {

                    // 해당 상품이 구매 불가 상품인 경우 해당 영역에서 처리됨
                    if (impossibleToPurchaseProducts == null) {
                        impossibleToPurchaseProducts = new ArrayList<>();
                    }

                    impossibleToPurchaseProducts.add(buyItem);

                    if (!ObjectUtils.isEmpty(buyItem.getSystemComment()) && isOrder) {
                        throw new OrderException(buyItem.getSystemComment());
                    }

                    // 장바구니에 담겨있는 상품중에 구매 불가 상품을 삭제함.
                    CartParam deleteCartParam = new CartParam();
                    deleteCartParam.setUserId(cartParam.getUserId());
                    deleteCartParam.setSessionId(cartParam.getSessionId());
                    deleteCartParam.setCartId(buyItem.getCartId());
                    deleteCartParam.setItemId(buyItem.getItemId());

                    cartMapper.deleteCart(deleteCartParam);

                    list.remove(i--);
                }
            }
        }

        return list;
    }

    //
    @Override
    public List<BuyItem> getCartBuyList(CartParam cartParam, boolean isSetPoint) {
        List<BuyItem> list = cartMapper.getCartList(cartParam);

        List<BuyItem> setList = cartMapper.getCartSetList(cartParam);

        // 세트상품이 있을 시 list add
        if(setList != null && setList.size() > 0){
            list.addAll(setList);
        }

        if (ValidationUtils.isNotNull(list)) {
            List<String> systemComments = new ArrayList<>();
            List<BuyItem> impossibleToPurchaseProducts = null;

            // 재고 차감 + 재고 검증용

            HashMap<String, Integer> buyQuantityMap = new HashMap<>();

            if (buyQuantityMap == null) {
                buyQuantityMap = new HashMap<>();
            }

            // 옵션 데이터 셋팅
            for(int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);
                Item item = buyItem.getItem();

                item.setItemOptions(itemMapper.getItemOptionList(item.getItemId()));

                // 1. 상품의 필수 옵션을 구성
                buyItem.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItem.getOptions()));
            }

            // 구매 상품중에 재고연동 사용하는 상품의 총 재고량 목록
            HashMap<String, HashMap<String, Integer>> stockMap = ShopUtils.makeStockMap(list);

            if (stockMap == null) {
                stockMap = new HashMap<>();
            }

            // 최소 구매수량 체크용
            HashMap<String, Integer> buyQuantityItemUserCodeMap = new HashMap<>();
            for(int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);
                Item item = buyItem.getItem();

                if(buyItem.getItemPrice() != null) {
                	if (buyQuantityItemUserCodeMap.get(item.getItemUserCode()) == null) {
                        buyQuantityItemUserCodeMap.put(item.getItemUserCode(), CommonUtils.intNvl(buyItem.getItemPrice().getQuantity()));
                    } else {
                        buyQuantityItemUserCodeMap.put(item.getItemUserCode(), buyQuantityItemUserCodeMap.get(item.getItemUserCode()) + CommonUtils.intNvl(buyItem.getItemPrice().getQuantity()));
                    }
                }
            }

            // 사은품 데이터 셋팅
            for(int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);

                if (buyItem == null) {
                    continue;
                }

                try {
                    List<GiftItem> freeGiftItemList = giftItemService.getGiftItemListForFront(buyItem.getItemId());
                    buyItem.setFreeGiftItemText(ShopUtils.makeGiftItemText(freeGiftItemList));
                    buyItem.setFreeGiftItemList(ShopUtils.conventGiftItemInfoList(freeGiftItemList));
                } catch (RuntimeException ignore) {
                	log.error(getClass().getName() + " :: getCartBuyList RuntimeException1 ============");
                }
            }

            for (int i = 0; i < list.size(); i++) {
                BuyItem buyItem = list.get(i);

                if (buyItem == null) {
                    continue;
                }

                Item item = buyItem.getItem();
                ItemPrice itemPrice = buyItem.getItemPrice();

                ShopUtils.setBuyQuantityMap(buyItem, stockMap, buyQuantityMap);
                OrderQuantity orderQuantity = buyItem.getOrderQuantity();

                // 3. 상품이 판매 종료된 상품인지 검사
                if (orderQuantity.getMaxQuantity() == 0) {
                    buyItem.setAvailableForSaleFlag("N");
                    buyItem.setSystemComment(MessageUtils.getMessage("M00483")); // 이쪽의 상품은 재고가 없습니다.
                }

                // 상품 코드가 같은 상품의 구매 총 수량이 상품에 설정된 최소 구매수량보다 적은경우 구매 못함
                String itemUserCode = item.getItemUserCode();
                if (buyQuantityItemUserCodeMap.get(itemUserCode) != null) {
                    if (item.getOrderMinQuantity() > buyQuantityItemUserCodeMap.get(itemUserCode)) {
                        buyItem.setAvailableForSaleFlag("N");
                    }
                }

                // 추가 구성품인경우 본품이 있는지 체크
                if("Y".equals(buyItem.getAdditionItemFlag())) {

                    boolean isDelete = true;
                    for(BuyItem bItem : list) {
                        if (bItem.getItemId() == buyItem.getParentItemId()) {
                            isDelete = false;
                            break;
                        }
                    }

                    if (isDelete) {
                        buyItem.setAvailableForSaleFlag("N");
                    }

                }

                if ("Y".equals(buyItem.getAvailableForSaleFlag())) {

                    // 적립금은 로그인한 회원에게만 부여됨..
                    if (UserUtils.isUserLogin() && isSetPoint) {
                        buyItem.setPointPolicy(pointService.getPointPolicyByItemId(item.getItemId()));
                    }

                    // 최대 구매가능 수량보다 장바구니에 담겨있는 수량이 많은경우 장바구니 구매수량 변경
                    if (itemPrice.getQuantity() > orderQuantity.getMaxQuantity() && orderQuantity.getMaxQuantity() > 0) {
                        itemPrice.setQuantity(orderQuantity.getMaxQuantity());
                        buyItem.getItemPrice().setQuantity(orderQuantity.getMaxQuantity());

                        systemComments.add(item.getItemName() + MessageUtils.getMessage("M00487")); // oo상품의 재고 부족으로 상품 구매 수량이 변경 되었습니다.

                        // DB의 상품의 구매수량을 변경함..
                        CartParam quantityUpdateCartParam = cartParam;
                        quantityUpdateCartParam.setQuantity(itemPrice.getQuantity());
                        quantityUpdateCartParam.setCartId(buyItem.getCartId());
                        cartMapper.updateQuantity(quantityUpdateCartParam);
                    }

                    // 상품별 금액 셋팅!!
                    buyItem.setItemPrice(new ItemPrice(buyItem));

                } else {

                    // 해당 상품이 구매 불가 상품인 경우 해당 영역에서 처리됨
                    if (impossibleToPurchaseProducts == null) {
                        impossibleToPurchaseProducts = new ArrayList<>();
                    }

                    impossibleToPurchaseProducts.add(buyItem);
                    if (ValidationUtils.isNotNull(buyItem.getSystemComment())) {
                        //systemComments.add(orderItemTemp.getSystemComment());
                    }

                    // 장바구니에 담겨있는 상품중에 구매 불가 상품을 삭제함.
                    CartParam deleteCartParam = new CartParam();
                    deleteCartParam.setUserId(cartParam.getUserId());
                    deleteCartParam.setSessionId(cartParam.getSessionId());
                    deleteCartParam.setCartId(buyItem.getCartId());

                    cartMapper.deleteCart(deleteCartParam);

                    list.remove(i--);
                }
            }
        }

        return list;
    }

    // 살때?
    @Override
    public Buy getBuyInfoByCart(CartParam cartParam) {

        Buy buy = new Buy();

        Receiver receiver = new Receiver();

        List<BuyItem> items = this.getCartList(cartParam, true);

        if (!items.isEmpty()) {
            // 배송지별 구매상품 초기화
            List<BuyQuantity> buyQuantitys = new ArrayList<>();
            for (BuyItem buyItem : items) {
                BuyQuantity buyQuantity = new BuyQuantity();
                buyQuantity.setItemSequence(buyItem.getItemSequence());
                if(buyItem.getItemPrice() != null) {
                	buyQuantity.setQuantity(CommonUtils.intNvl(buyItem.getItemPrice().getQuantity()));
                }
                buyQuantitys.add(buyQuantity);
            }

            receiver.setBuyQuantitys(buyQuantitys);

            receiver.setItems(items);
            receiver.setShipping("");

            List<Receiver> receivers = new ArrayList<>();
            receivers.add(receiver);

            buy.setReceivers(receivers);
            buy.setOrderPrice(0, configService.getShopConfig(Config.SHOP_CONFIG_ID));
        }

        return buy;
    }

    @Override
    public void immediatelyBuy(Cart cart) {
        List<Integer> cartSequence = new ArrayList<>();

        // 세트상품이 있는 경우
        if (cart.getItemSets() != null && !cart.getItemSets().isEmpty()) {
            for (int i = 0; i < cart.getItemSets().size(); i++) {
                CartSet cs = cart.getItemSets().get(i);
                Cart c = new Cart();

                int sequence = 0;
                int quantity = cs.getQuantity();       // 마스터 수량
                c.setSessionId(cart.getSessionId());
                c.setUserId(cart.getUserId());

                // 구매 수량이 0인경우 에러
                if (quantity <= 0) {
                    throw new OrderException(MessageUtils.getMessage("M01590"));
                }

                sequence = sequenceService.getId("OP_CART");
                c.setItemId(cs.getItemId());
                c.setCartId(sequence);
                c.setQuantity(quantity);
                c.setSetItemFlag("Y");

                // 추가 구성품의 부모 상품 ID를 저장
                if ("Y".equals(c.getAdditionItemFlag())) {
                    c.setParentItemId(itemMapper.getParentAdditionItemId(c.getItemId()));
                }

                // 세트 정보
                List<Cart> itemSets = this.setItemSetOptions(c, cs.getArrayItemSets());

                cartMapper.insertCart(c);
                cartSequence.add(sequence);

                for (int j = 0; j < itemSets.size(); j++) {
                    Cart cartSet = itemSets.get(j);
                    cartSet.setCartId(sequenceService.getId("OP_CART_SET"));
                    cartSet.setParentCartId(sequence);
                    cartMapper.insertCartSet(cartSet);
                }
            }
        } else {
            // 필수 옵션 정보 구성
            List<Cart> list = this.makeCartListByPostData(cart.getArrayRequiredItems(), false);

            // 추가 구성상품 정보 구성
            List<Cart> additionList = this.makeCartListByPostData(cart.getArrayAdditionItems(), true);
            if (additionList != null) {
                if (list == null) {
                    list = new ArrayList<>();
                }

                list.addAll(additionList);
            }

            if (list == null) {
                throw new OrderException(MessageUtils.getMessage("M00481")); // 잘못된 접근입니다.
            }

            if (list != null) {
                for (Cart c : list) {
                    int sequence = sequenceService.getId("OP_CART");
                    try {
                        c.setCartId(sequence);

                        int quantity = c.getQuantity();

                        // 구매 수량이 0인경우 에러
                        if (quantity <= 0) {
                            throw new OrderException(MessageUtils.getMessage("M01590"));
                        }

                        c.setSessionId(cart.getSessionId());
                        c.setUserId(cart.getUserId());
                        c.setSetItemFlag("N");
                        // 필수 추가정보 <<이 부분 문제 있을까?>>
                        c.setTextOption(cart.getTextOption());

                        // 추가 구성품의 부모 상품 ID를 저장
                        if ("Y".equals(c.getAdditionItemFlag())) {
                            c.setParentItemId(itemMapper.getParentAdditionItemId(c.getItemId()));
                        }

                        cartMapper.insertCart(c);
                        cartSequence.add(sequence);
                    } catch(RuntimeException e) {
//                        log.warn(e.getMessage());
                        log.warn(getClass().getName() + " :: immediatelyBuy RuntimeException ============");
                    }
                }
            }
        }

        if (cartSequence.isEmpty()) {
            // 주문 상품 처리중 오류가 발생 하였습니다. \n주문을 다시 시도해 주세요.
            throw new OrderException(MessageUtils.getMessage("M00486"));
        }

        CartParam cartParam = new CartParam();
        cartParam.setUserId(cart.getUserId());
        cartParam.setSessionId(cart.getSessionId());
        cartParam.setCartIds(cartSequence);
        cartParam.setCampaignCode(cart.getCampaignCode());
        cartParam.setItemId(cart.getItemId());

        // 장바구니에 담긴 상품을 Temp로 복사..
        this.copyCartToOrderItemTemp(cartParam);

        cartMapper.deleteCartByCartIds(cartParam);
    }

    /**
     * 상품페이지에서 넘어온 정보를 Cart로 변경
     * @param items
     * @param isAdditionItem
     * @return
     */
    private List<Cart> makeCartListByPostData(String[] items, boolean isAdditionItem) {

        if (items == null) {
            return null;
        }

        List<Cart> cartList = new ArrayList<>();
        for(int j = 0; j < items.length; j++) {
            String itemString = items[j];

            String[] itemInfo = StringUtils.delimitedListToStringArray(itemString, "||");
            if (itemInfo.length != 3) {
                continue;
            }

            int itemId = Integer.parseInt(itemInfo[0]);
            int quantity = Integer.parseInt(itemInfo[1]);
            Item item = itemMapper.getItemById(itemId);

            if (item == null) {
                continue;
            }

            Cart cartTemp = new Cart();
            cartTemp.setItemId(itemId);
            cartTemp.setQuantity(quantity);
            cartTemp.setItemId(item.getItemId());
            cartTemp.setItemName(item.getItemName());
            cartTemp.setAdditionItemFlag(isAdditionItem ? "Y" : "N");
            cartTemp.setShippingGroupCode(item.getShippingGroupCode());

            String optionsText = itemInfo[2];
            String options = "";
            if (StringUtils.isNotEmpty(optionsText)) {
                item.setItemOptions(itemMapper.getItemOptionList(itemId));
                String[] optionList = StringUtils.delimitedListToStringArray(optionsText, "^^^");

                for (String optionText : optionList) {
                    String[] optionInfo = StringUtils.delimitedListToStringArray(optionText, "```");
                    if (optionInfo.length != 2) {
                        continue;
                    }

                    int optionId = Integer.parseInt(optionInfo[0]);
                    String text = StringUtils.isNotEmpty(optionInfo[1]) ? optionInfo[1] : "";

                    for (ItemOption itemOption : item.getItemOptions()) {
                        if (itemOption.getItemOptionId() == optionId) {
                            if (StringUtils.isNotEmpty(options)) {
                                options += "^^^";
                            }

                            options += ShopUtils.makeOptionText(item, itemOption, text);
                            break;
                        }

                    }
                }

            }

            cartTemp.setOptions(options);
            cartList.add(cartTemp);
        }

        return cartList;
    }

    @Override
    public void copyCartToOrderItemTemp(CartParam cartParam) {

        if (cartParam.getCartIds() == null || cartParam.getCartIds().isEmpty()) {
            throw new OrderException();
        }

        Buy buy = this.getBuyInfoByCart(cartParam);
        List<BuyItem> list = buy.getItems();

        try {
            // 구매가능여부 채크
//            ShopUtils.buyVerification(list, cartParam.getCartIds().size());

        	// 기부포인트 답례품 구매 로직으로 변경
        	orderService.buyGiveGoodsVerification(list, cartParam.getCartIds().size());
        } catch (OrderException oe) {
            throw new OrderException(oe.getMessage(), "/cart");
        }

        // 장바구니 결제 금액에 최소 결제 금액 재한이 걸려 있는 경우 총 결제 예정 금액을 체크함..

        Config shopConfig = ShopUtils.getConfig();
        if (shopConfig.getMinimumPaymentAmount() > 0) {

            int minimumPaymentAmount = shopConfig.getMinimumPaymentAmount();
            if (buy.getOrderPrice().getOrderPayAmount() < minimumPaymentAmount) {

                String message = MessageUtils.getMessage("M01046") +" (" + NumberUtils.formatNumber(minimumPaymentAmount, "#,##0") + ") "+ MessageUtils.getMessage("M01266");
                throw new OrderException(message);

            }

        }

        orderService.deleteOrderItemTemp(cartParam.getUserId(), cartParam.getSessionId());
        orderService.deleteOrderItemSetTemp(cartParam.getUserId(), cartParam.getSessionId());

        int itemSequence = 0;
        for (BuyItem buyItem : list) {

            ItemPrice itemPrice = buyItem.getItemPrice();

            // 구매 수량이 0보다 작은경우
            if (itemPrice.getQuantity() <= 0) {
                throw new OrderException(MessageUtils.getMessage("M01590"));
            }

            if (UserUtils.isUserLogin() == false) {
                buyItem.setUserId(0);
            }

            buyItem.setItemSequence(itemSequence);
            buyItem.setSessionId(cartParam.getSessionId());
//            buyItem.setCampaignCode(cartParam.getCampaignCode());

            if ("Y".equals(buyItem.getAdditionItemFlag())) {
                for (BuyItem temp : list) {
                    if ("N".equals(temp.getAdditionItemFlag()) && buyItem.getParentItemId() == temp.getItemId()) {
                        buyItem.setParentItemSequence(temp.getItemSequence());
                        buyItem.setParentItemId(temp.getItemId());
                        break;
                    }
                }
            }

            orderService.insertOrderItemTemp(buyItem);

            if (buyItem.getItemSets() != null && !buyItem.getItemSets().isEmpty()) {
                int setItemSequence = 0;
                for (BuyItem item : buyItem.getItemSets()) {
                    item.setUserId(buyItem.getUserId());
                    item.setSessionId(buyItem.getSessionId());
                    item.setItemSequence(setItemSequence++);
                    item.setSetItemSequence(buyItem.getItemSequence());
                    orderService.insertOrderItemSetTemp(item);
                }
            }

            itemSequence++;
        }

        // 기존 주문 임시 데이터 삭제
        orderService.deleteOrderTemp(cartParam.getUserId(), cartParam.getSessionId());
    }

    // 장바구니에 등록
    @Override
    public int insertCart(Cart cart) {
        int insertCount = 0;
        // 필수 추가정보
        boolean textOptionFlag = cart.getTextOption() != null && !cart.getTextOption().trim().isEmpty();

        // 세트상품이 있는 경우
        if (cart.getItemSets() != null && cart.getItemSets().size() > 0) {
            for (int i = 0; i < cart.getItemSets().size(); i++) {
                CartSet cs = cart.getItemSets().get(i);
                Cart c = new Cart();

                int cartSequence = 0;
                int quantity = cs.getQuantity();       // 마스터 수량
                c.setSessionId(cart.getSessionId());
                c.setUserId(cart.getUserId());

                // 구매 수량이 0인경우 에러
                if (quantity <= 0) {
                    throw new OrderException(MessageUtils.getMessage("M01590"));
                }

                cartSequence = sequenceService.getId("OP_CART");
                c.setItemId(cs.getItemId());
                c.setCartId(cartSequence);
                c.setQuantity(quantity);
                c.setSetItemFlag("Y");

                // 추가 구성품의 부모 상품 ID를 저장
                if ("Y".equals(c.getAdditionItemFlag())) {
                    c.setParentItemId(itemMapper.getParentAdditionItemId(c.getItemId()));
                }

                // 세트 정보
                List<Cart> itemSets = this.setItemSetOptions(c, cs.getArrayItemSets());

                // 세트상품 중복체크
                List<Cart> searchCartList = cartMapper.getDuplicateCart(c);

                // 신규
                if (searchCartList.isEmpty()) {
                    cartMapper.insertCart(c);

                    for (int j = 0; j < itemSets.size(); j++) {
                        Cart cartSet = itemSets.get(j);
                        cartSet.setCartId(sequenceService.getId("OP_CART_SET"));
                        cartSet.setParentCartId(cartSequence);
                        cartMapper.insertCartSet(cartSet);
                    }
                } else {
                    // 중복인 경우
                    cartSequence = sequenceService.getId("OP_CART");
                    c.setCartId(cartSequence);

                    // 중복이 있는 경우
                    int makeQuantity = 0;
                    for (Cart searchCart : searchCartList) {
                        makeQuantity += searchCart.getQuantity();
                    }

                    makeQuantity = makeQuantity + quantity;
                    c.setQuantity(makeQuantity);

                    if (searchCartList.size() > 1) {
                        // 중복상품 통합
                        cartMapper.deleteDuplicateCart(c);
                        c.setCartId(sequenceService.getId("OP_CART"));
                        cartMapper.insertCart(c);
                    } else {
                        cartMapper.updateDuplicateCartQuantity(c);
                    }
                }
            }
        } else {
            // 필수 옵션 정보 구성
            List<Cart> list = this.makeCartListByPostData(cart.getArrayRequiredItems(), false);

            if (list == null) {
            	list = new ArrayList<>();
            }

            // 추가 구성상품 정보 구성
            List<Cart> additionList = this.makeCartListByPostData(cart.getArrayAdditionItems(), true);
            if (additionList != null) {

                list.addAll(additionList);
            }

            // 세트상품이 없는 경우 노말 상태
            for (Cart c : list) {
                int quantity = c.getQuantity();
                c.setSessionId(cart.getSessionId());
                c.setUserId(cart.getUserId());
                c.setSetItemFlag("N");
                // 필수 추가정보
                if(cart.getTextOption() != null) {
                	c.setTextOption(cart.getTextOption());
                }

                // 추가 구성품의 부모 상품 ID를 저장
                if ("Y".equals(c.getAdditionItemFlag())) {
                    c.setParentItemId(itemMapper.getParentAdditionItemId(c.getItemId()));
                }

                // 구매 수량이 0인경우 에러
                if (quantity <= 0) {
                    throw new OrderException(MessageUtils.getMessage("M01590"));
                }

                List<Cart> searchCartList = cartMapper.getDuplicateCart(c);


                // 필수 추가정보: textOption이 있는 경우 중복 상품이 아님
                if(!searchCartList.isEmpty() && textOptionFlag == false) {
                    // 중복이 있는 경우
                    int makeQuantity = 0;
                    for (Cart searchCart : searchCartList) {
                        makeQuantity += searchCart.getQuantity();
                    }

                    makeQuantity = makeQuantity + quantity;
                    c.setQuantity(makeQuantity);

                    if (searchCartList.size() > 1) {
                        // 중복상품 통합
                        cartMapper.deleteDuplicateCart(c);
                        c.setCartId(sequenceService.getId("OP_CART"));
                        cartMapper.insertCart(c);
                    } else {
                        cartMapper.updateDuplicateCartQuantity(c);
                    }
                } else {
                    // 세트상품이 아닌 경우 일반 장바구니 담기
                    c.setCartId(sequenceService.getId("OP_CART"));
                    cartMapper.insertCart(c);
                }
            }
        }

        insertCount++;
        return insertCount;
    }

    // 마스터 세트상품 옵션정보
    private List<Cart> setItemSetOptions(Cart cart, String[] arrayItemSets) {
        List<Cart> cartSets = this.makeCartListByPostData(arrayItemSets, false);
        List<String> resultList = new ArrayList<>();

        if (cartSets != null) {
	        for (Cart cartSet : cartSets) {
	            for (int i = 0; i < CommonUtils.dataAryNvl(arrayItemSets).length; i++) {
	                JSONObject json = new JSONObject();

	                // itemId, quantity, itemOptionId
	                String[] itemSetOption = StringUtils.delimitedListToStringArray(arrayItemSets[i], "||");
	                int itemId = Integer.parseInt(itemSetOption[0]);

	                if (cartSet.getItemId() == itemId) {
	                    json.put("itemId", itemId);
	                    json.put("itemName", cartSet.getItemName());
	                    json.put("quantity", itemSetOption[1]);

	                    if (itemSetOption.length >= 3 && !ObjectUtils.isEmpty(itemSetOption[2])) {
	                        json.put("itemOptionId", itemSetOption[2].split("```")[0]);
	                        json.put("options", cartSet.getOptions());
	                    }

	                    resultList.add(json.toString());
	                }
	            }
	        }
        }

        cart.setOptions(resultList.toString());
        return cartSets;
    }

    @Override
    public void updateQuantity(CartParam cartParam) {

        List<BuyItem> itemList = this.getCartList(cartParam, true);

        int addItemQuantity = cartParam.getQuantity();
        int maxQuantity = 0;
        int minQuantity = 0;

        int result = 0;

        for (BuyItem buyItem : itemList) {
            // [손준의] 추가하려는 상품과 동일한 장바구니 상품인지 확인 후 해당 상품의 구매제한 확인
            if (cartParam.getCartId() == buyItem.getCartId()) {
                maxQuantity = buyItem.getItem().getOrderMaxQuantity();
                minQuantity = buyItem.getItem().getOrderMinQuantity();
            }
        }

        if (maxQuantity > 0 && addItemQuantity > maxQuantity) {
            result = 2;
        }
        if (minQuantity > 0 && addItemQuantity < minQuantity){
            result = 1;
        }
        if (addItemQuantity > 999) {
            result = 3;
        }

        CartParam setParam = new CartParam();
        setParam.setUserId(cartParam.getUserId());
        setParam.setSessionId(cartParam.getSessionId());
        setParam.setParentCartId(cartParam.getParentCartId());

        if (result == 0) {
            cartMapper.updateQuantity(cartParam);

            // 세트상품 수량 업데이트
            for (BuyItem buyItem : itemList) {
                // 동일한 장바구니 상품이면서 세트상품이 존재한다면
                if ((cartParam.getCartId() == buyItem.getCartId())
                        && (buyItem.getItemSets() != null && buyItem.getItemSets().size() > 0)) {
                    for(BuyItem setItem : buyItem.getItemSets()){
                        setParam.setItemId(setItem.getItemId());
                        // 변경 마스터 수량 * 세트상품 수량 - 세트는 마스터 상품수 곱에 따라 결제 시 계산처리 되기 때문에 기본설정값만 저장
                        setParam.setQuantity(setItem.getItemPrice().getQuantity());
                        cartMapper.updateSetQuantity(setParam);
                    }
                }
            }

        } else if (result == 1) {
            throw new OrderException("장바구니에 담긴 수량이 최소 구매 가능 수량을 미달하였습니다.");
        } else if (result == 2) {
            throw new OrderException("장바구니에 담긴 수량이 최대 구매 가능 수량을 초과하였습니다.");
        } else if (result == 3) {
            throw new OrderException("장바구니에 담긴 수량은 999개를 초과할 수 없습니다.");
        }
    }

    @Override
    public void updateUserIdBySessionId(long userId, String sessionId) {
        if (UserUtils.isUserLogin()) {

            CartParam param = new CartParam();
            param.setUserId(userId);
            param.setSessionId(sessionId);

            List<Cart> guestCartList = null;

            if (cartMapper.getCountForUserItemByUserId(userId) == 0) {
                guestCartList = cartMapper.getCartListBySessionId(sessionId);
            } else {
                guestCartList = cartMapper.getGuestConvertibleItems(param);
            }

            if (guestCartList != null) {
                for (Cart cart : guestCartList) {

                    int quantity = cart.getQuantity();
                    if (quantity > 0) {
                        cart.setUserId(userId);

                        List<Cart> searchCartList = cartMapper.getDuplicateCart(cart);

                        if (searchCartList.isEmpty()) {
                            cartMapper.updateUserIdByCart(cart);
                        } else {
                            int makeQuantity = 0;
                            for(Cart searchCart : searchCartList) {
                                makeQuantity += searchCart.getQuantity();
                            }

                            makeQuantity = makeQuantity + quantity;
                            cart.setQuantity(makeQuantity);

                            // 중복상품 통합 - 회원의 카드가 삭제됨
                            cartMapper.deleteDuplicateCart(cart);
                            cartMapper.updateUserIdByCart(cart);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void deleteListData(CartParam cartParam) {
        if (cartParam.getId() != null) {
            for (String id : cartParam.getId()) {
                cartParam.setCartId(Integer.parseInt(id));
                cartMapper.deleteCart(cartParam);
            }
        }
    }

    @Override
    public void updateShippingPaymentType(CartParam cartParam) {
        cartMapper.updateShippingPaymentType(cartParam);
    }

    @Override
    public List<Cart> makeCartListByCart(Cart cart) {

        if (cart == null) {
            return null;
        }

        // 필수 옵션 정보 구성
        List<Cart> list = this.makeCartListByPostData(cart.getArrayRequiredItems(), false);

        // 추가 구성상품 정보 구성
        List<Cart> additionList = this.makeCartListByPostData(cart.getArrayAdditionItems(), true);
        if (additionList != null) {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.addAll(additionList);
        }

        return list;
    }
}

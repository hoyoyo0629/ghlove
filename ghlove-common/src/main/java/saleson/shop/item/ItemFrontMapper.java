package saleson.shop.item;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerUser;
import saleson.shop.brand.domain.Brand;
import saleson.shop.cart.support.CartParam;
import saleson.shop.categories.support.CategoryParam;
import saleson.shop.code.domain.Code;
import saleson.shop.coupon.domain.ChosenItem;
import saleson.shop.item.domain.*;
import saleson.shop.item.support.*;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.seasonalfood.domain.SeasonalFood;
import saleson.shop.shipment.domain.Shipment;
import saleson.shop.shipmentreturn.domain.ShipmentReturn;
import saleson.shop.wishlist.domain.WishlistGroup;

import java.util.HashMap;
import java.util.List;

/**
 * @author CJA
 *
 */
@Mapper("ItemFrontMapper")
public interface ItemFrontMapper {

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getFrontItemCount(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemList(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getFrontItemTypecListCount(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemTypecList(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getFrontItemTypelListCount(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemTypelList(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getFrontItemCountBySeason(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemListBySeason(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getFrontItemCountBySpeciality(ItemParam itemParam);

	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemListBySpeciality(ItemParam itemParam);

	/**
	 * 국정자원진단 제시 코드 적용 답례품 목록 조회
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemTypelListNew(ItemParam itemParam);

	/**
	 * 링크뷰 답례품 이미지 설명 조회
	 * @param itemParam
	 * @return
	 */
	List<ItemImageExplain> getItemImagesExplainByLinkView(String itemUserCode);

	/**
	 * 답례품 노출 기준 변경으로 상품 카운트 조회 조건 변경
	 * @param itemParam
	 * @return
	 * */
	int getFrontItemTypecListCountForNewPolicy(ItemParam itemParam);
	int getFrontItemTypelListCountForNewPolicy(ItemParam itemParam);
	int getFrontItemCountForNewPolicy(ItemParam itemParam);
	List<Item> getFrontItemTypelListForNewPolicy(ItemParam itemParam);

	/**
	 * 마을 기업 리스트 조회
	 * @param itemParam
	 * @return
	 */
	List<Item> getFrontItemListByCommunity(ItemParam itemParam);

	/**
	 * 마을 기업 리스트 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getFrontItemCountByCommunity(ItemParam itemParam);
}

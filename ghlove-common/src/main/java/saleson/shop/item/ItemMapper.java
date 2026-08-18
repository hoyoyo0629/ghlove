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
import java.util.Map;

/**
 * @author CJA
 *
 */
@Mapper("itemMapper")
public interface ItemMapper {

	/**
	 * 아이템 변경내역 로그 조회
	 * @param itemParam
	 * @return
	 */
	List<Item> getItemLogListById(ItemParam itemParam);

	/**
	 * 아이템 변경내역 총 개수 조회
	 * @param itemParam
	 * @return
	 */
	int getItemLogCountById(ItemParam itemParam);

	ItemOption getItemOptionByItemOption(ItemOption itemOption);

	/**
	 * 상품정보를 등록한다.
	 * @param item
	 */
	void insertItem(Item item);

	/**
	 * 상품 접근성 이미지 설명을 등록한다.
	 * @param item
	 */
	void insertItemImagesExplain(Item item);

	/**
	 * 추가 구성품의 부모 상품 ID를 조회
	 * @param additionItemId
	 * @return
	 */
	int getParentAdditionItemId(int additionItemId);

	/**
	 * 상품정보를 업데이트 한다.
	 * @param item
	 */
	void updateItem(Item item);

	void insertFullItem(Item item);

	void insertItemOption(ItemOption itemOption);

	/**
	 * 상품ID로 상품을 조회한다.
	 * @param itemId
	 * @return
	 */
	Item getItemById(int itemId);

	/**
	 * 상품 코드로 미리보기 상품 정보를 조회함
	 * @param itemUserCode
	 * @return
	 */
	Item getItemByItemUserCodeForPreview(String itemUserCode);

	/**
	 * 상품코드로 상품을 조회한다.
	 * @param itemUserCode
	 * @return
	 */
	Item getItemByItemUserCode(String itemUserCode);

	/**
	 * 상품정보 조회
	 * @param itemParam
	 * @return
	 */
	Item getItemByParam(ItemParam itemParam);


	/**
	 * 상품ID에 해당하는 상품 옵션정보를 조회 (관리자용-숨김 옵션을 노출함)
	 * @param itemId
	 * @return
	 */
	List<ItemOptionGroup> getItemOptionGroupListForManager(int itemId);



	/**
	 * 상품ID에 해당하는 관련 상품을 조회
	 * @param itemParam
	 * @return
	 */
	List<ItemRelation> getItemRelationList(ItemParam itemParam);


	/**
	 * 상품별 기본정보
	 * @param itemId
	 * @return
	 */
	List<ItemInfo> getItemInfoListByItemId(int itemId);


	/**
	 * 상품 기본정보 (모바일)
	 * @param itemId
	 * @return
	 */
	List<ItemInfo> getItemInfoMobileListByItemId(int itemId);


	int getItemCountByItemUserCode(String itemUserCode);


	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	int getItemCount(ItemParam itemParam);


	/**
	 * 조건에 해당하는 상품 카운트
	 * @param itemParam
	 * @return
	 */
	List<Item> getItemList(ItemParam itemParam);

	/**
	 * 그룹베너에 설정된 상품 목록
	 * @param value
	 * @return
	 */
	List<Item> getItemListForGroupBanner(HashMap<String, String[]> value);

	/**
	 * 상품 카테고리 등록
	 * @param itemCategory
	 */
	void insertItemCategory(ItemCategory itemCategory);


	/**
	 * 관련상품 등록
	 * @param itemRelation
	 */
	void insertItemRelation(ItemRelation itemRelation);


	/**
	 * 상품ID로 관련상품 삭제
	 * @param itemId
	 */
	void deleteItemRelationByItemId(int itemId);


	/**
	 * 상품 목록 이미지 정보 업데이트.
	 * @param item
	 */
	void updateItemImageName(Item item);


	/**
	 * 상품 상세 이미지 정보를 저장.
	 * @param itemImage
	 */
	void insertItemImage(ItemImage itemImage);


	/**
	 * 카테고리ID로 등록된 모든 데이터를 삭제한다.
	 * @param categoryId
	 */
	void deleteItemOrderingByCategoryId(int categoryId);


	/**
	 * ITEM 노출 순서 정보를 저장한다.
	 * @param itemOrdering
	 */
	void insertItemOrdering(ItemOrdering itemOrdering);


	/**
	 * 상품을 삭제한다.
	 * 실제 데이터를 삭제함.
	 * @param itemId
	 */
	void deleteItemById(int itemId);


	/**
	 * 상품을 삭제한다.
	 * DATA_STATUS_CODE = 2로 번경.
	 * @param item
	 */
	void deleteItem(Item item);


	/**
	 * 대표상품을 삭제한다.
	 * REPRESENTATIVE_ITEM_YN = 'N'으로 번경.
	 * @param item
	 */
	void deleteRepresentativeItem(Item item);


	/**
	 * 상품ID에 해당하는 상품카테고리를 삭제한다.
	 * @param itemId
	 */
	void deleteItemCategoryByItemId(int itemId);

	/**
	 * 카테고리 클래스에 해당하는 상품카테고리를 삭제 한다
	 * @param categoryClass
	 */
	void deleteItemCategoryByCategoryClass(String categoryClass);

	/**
	 * 상품ID에 설정된는 상품 옵션 정보를 삭제한다.
	 * @param itemId
	 */
	void deleteItemOptionByItemId(int itemId);

	/**
	 * 상품ID에 설정되는 상품 접근성 이미지 설명 정보를 삭제한다.
	 * @param itemId
	 */
	void deleteItemImagesExplainByItemId(int itemId);

	/**
	 * 지자체 대표상품을 등록한다.
	 * @param itemId
	 */
	void registerMainItem(Item item);

	/**
	 * 지자체 대표상품을 삭제한다.
	 * @param itemId
	 */
	void deleteMainItem(int itemId);


	/**
	 * 상품테이블에 ITEM_IMAGE 컬럼 값을 ""으로 업데이트.
	 * @param itemId
	 */
	void updateItemImageOfItemByItemId(int itemId);

	/**
	 * 상품 재고량을 변경한다
	 * @param itemStockQuantityParam
	 */
	void updateItemStockQuantityByItemStockQuantityParam(ItemStockQuantityParam itemStockQuantityParam);

	/**
	 * 상품 옵션 재고량을 변경한다
	 * @param itemStockQuantityParam
	 */
	void updateItemOptionStockQuantityByItemStockQuantityParam(ItemStockQuantityParam itemStockQuantityParam);


	/**
	 * 상품 이미지 목록을 가져옴
	 * @param itemId
	 * @return
	 */
	List<ItemImage> getItemImageListByItemId(int itemId);

	/**
	 * 상품 이미지 목록을 가져옴 (이미지 복사용)
	 * @param copyItemImageIds
	 * @return
	 */
	List<ItemImage> getItemImageListByCopyIds(int[] copyItemImageIds);

	/**
	 * 상품 상세 이미지 정보를 가져옴
	 * @param itemImageId
	 * @return
	 */
	ItemImage getItemImageById(int itemImageId);

	/**
	 * 상품 상세 이미지정보를 삭제함
	 * @param itemImageId
	 */
	void deleteItemImageById(int itemImageId);

	/**
	 * 상품 상세이미지의 Max 정렬순서를 가져온다.
	 * @param itemId
	 * @return
	 */
	int getMaxOrderingOfItemImageByItemId(int itemId);


	/**
	 * 상품 상세이미지의 정렬 순서를 변경한다.
	 * @param itemImage
	 */
	void updateOrderingOfItemImage(ItemImage itemImage);


	/**
	 * 상품 옵션을 상품 옵션 ID(List) 로 조회함
	 * @param cartParam
	 * @return
	 */
	List<ItemOption> getItemOptionListByItemOptionIdsForOrder(CartParam cartParam);


	/**
	 * 상품 옵션 ID로 옵션 목록을 조회함.
	 * @param itemOptions
	 * @return
	 */
	List<ItemOptionGroup> getItemOptionListByItemOptionIds(String[] itemOptions);


	/**
	 * 상품ID와 상품 옵션 코드로 옵션 목록을 조회함.
	 * @param wishlistHash
	 * @return
	 */
	List<ItemOptionGroup> getItemOptionListByWishlistHash(HashMap<String, Object> wishlistHash);


	/**
	 * 상품ID와 OPTION_NAME1, OPTION_NAME2 가 일치하는 옵션 목록을 조회함.
	 * @param itemOptions
	 * @return
	 */
	List<ItemOptionGroup> getItemOptionListByItemOptions(List<HashMap<String, Object>> itemOptions);



	/**
	 * 관련상품 5개 - 임의 출력 (동일 카테고리)
	 * @param itemParam
	 * @return
	 */
	List<ItemRelation> getItemRelationRandomList(ItemParam itemParam);


	/**
	 * 상품 리뷰 카운트
	 * @param itemParam
	 * @return
	 */
	int getItemReviewCountByParam(ItemParam itemParam);


	/**
	 * 상품 리뷰 목록
	 * @param itemParam
	 * @return
	 */
	List<ItemReview> getItemReviewListByParam(ItemParam itemParam);


	/**
	 * 상품 리뷰 등록
	 * @param itemReview
	 */
	int insertItemReview(ItemReview itemReview);


	/**
	 * 같이 구매한 상품 목록 6개
	 * @param itemId
	 * @return
	 */
	List<ItemOther> getItemOtherList(int itemId);


	/**
	 * 상품 옵션 ID로 옵션 정보를 조회한다.
	 * @param itemOptionId
	 * @return
	 */
	ItemOption getItemOptionById(int itemOptionId);

	/**
	 * 신상품 조회 (카테고리별 - 팀,그룹,카테고리)
	 * @param itemParam
	 * @return
	 */
	List<Item> getNewArrivalItemList(ItemParam itemParam);


	/**
	 * 신상품 조회 (메인페이지)
	 * @param itemParam
	 * @return
	 */
	List<Item> getNewArrivalItemListForMain(ItemParam itemParam);


	/**
	 * 관심상품 그룹에 포함된 상품 목록을 조회한다.
	 * @param wishlistGroup
	 * @return
	 */
	List<Item> getWishlistItemList(WishlistGroup wishlistGroup);


	/**
	 * 목록에서 체크한 데이터로 상품정보를 수정한다.
	 * @param item
	 */
	void updateItemByListParam(Item item);

	/**
	 * 승인 대기 목록에서 체크한 상품 승인처리
	 * @param item
	 */
	void updateItemApprovalByListParam(Item item);

	/**
	 * 목록에서 체크한 데이터로 공개여부를 수정한다.
	 * @param item
	 */
	void updateItemDisplayByListParam(Item item);


	/**
	 * 목록에서 체크한 데이터로 상품라벨을 수정한다.
	 * @param item
	 */
	void updateItemLabelByListParam(Item item);


	/**
	 * 해당 배송비설정을 가진 상품수량 조회
	 * @param deliveryId
	 * @return
	 */
	int getItemCountByDeliveryId(int deliveryId);

	/**
	 * 해당 배송비 금액정보를 가진 상품수량 조회
	 * @param deliveryChargeId
	 * @return
	 */
	int getItemCountByDeliveryChargeId(int deliveryChargeId);

	int getSearchNewArrivalItemCount(ItemParam itemParam);

	List<Item> getSearchNewArrivalItemList(ItemParam itemParam);

	/**
	 * 해당 리뷰 조회
	 * @param itemReviewId
	 * @return
	 */
	ItemReview getItemReviewById(int itemReviewId);

	/**
	 * 리뷰수정
	 * @param itemReview
	 */
	void updateItemReview(ItemReview itemReview);

	/**
	 * 리뷰 삭제
	 * @param itemReviewId
	 */
	void deleteItemReview(int itemReviewId);

	/**
	 * 리뷰 이미지 삭제
	 * @param id
	 */
	void deleteItemReviewImageById(long id);

	/**
	 * 상품 입하예정일 수정
	 * @param item
	 */
	public void updateItemStockScheduleDate(Item item);


	/**
	 * 상품 옵션 입하예정일 수정
	 * @param itemOption
	 */
	public void updateItemOptionStockScheduleDate(ItemOption itemOption);



	/**
	 * 상품별 카테고리 목록 (엑셀 다운로드 용)
	 * @param itemParam
	 * @return
	 */
	List<ExcelItemCategory> getItemCategoryListForExcel(ItemParam itemParam);


	/**
	 * 상품별 관련상품 목록 (엑셀 다운로드 용)
	 * @param itemParam
	 * @return
	 */
	List<ExcelItemRelation> getItemRelationListForExcel(ItemParam itemParam);


	/**
	 * 상품별 포인트 목록 (엑셀 다운로드 용)
	 * @param itemParam
	 * @return
	 */
	List<ExcelItemPointConfig> getItemPointListForExcel(ItemParam itemParam);


	/**
	 * 상품별 기본정보 목록 (엑셀 다운로드 용)
	 * @param itemParam
	 * @return
	 */
	List<ItemInfo> getItemInfoListForExcel(ItemParam itemParam);


	/**
	 * 상품별 기본정보 목록 (엑셀 다운로드 용)
	 * @param itemParam
	 * @return
	 */
	List<ItemInfo> getItemInfoMobileListForExcel(ItemParam itemParam);


	/**
	 * 상품별 검색어 정보 (엑셀 다운로드 용)
	 * @param itemParam
	 * @return
	 */
	List<Item> getItemKeywordListForExcel(ItemParam itemParam);



	/**
	 * [엑셀업로드] 상품List로 일괄등록
	 * @param insertItems
	 */
	void insertItemListForExcel(List<Item> insertItems);


	/**
	 * [엑셀업로드] 상품코드로 ITEM_ID를 조회함.
	 * @param itemUserCode
	 * @return
	 */
	Integer getItemIdByItemUserCode(String itemUserCode);


	/**
	 * 색인 검색 - 인덱스 카운트 목록
	 * @param searchIndexParam
	 * @return
	 */
	List<HashMap<String, Object>> getIndexList(SearchIndexParam searchIndexParam);


	/**
	 * 색인 검색 - 인덱스 카운트 목록
	 * @param searchIndexParam
	 * @return
	 */
	List<HashMap<String, Object>> getSubIndexList(SearchIndexParam searchIndexParam);


	/**
	 * 색인 검색 - 상품목록
	 * @param searchIndexParam
	 * @return
	 */
	List<ItemIndex> getItemIndexList(SearchIndexParam searchIndexParam);


	/**
	 * 오늘 본 상품 조회
	 * @param itemParam
	 * @return
	 */
	List<Item> getTodayItemList(ItemParam itemParam);


	/**
	 * 대표이미지 업데이트. (상세이미지 중 첫번째가 상품의 대표 이미지가 됨)
	 * @param itemId
	 */
	void updateItemImage(int itemId);



	/**
	 * 신상품
	 * @param itemParam
	 * @return
	 */
	int getItemCountForNewArrival(ItemParam itemParam);

	/**
	 * 신상품.
	 * @param itemParam
	 * @return
	 */
	List<Item> getItemListForNewArrival(ItemParam itemParam);


	/**
	 * 전시상품 조회
	 * @param itemParam
	 * @return
	 */
	int getMainDisplayItemCountByParam(ItemParam itemParam);

	/**
	 * 전시상품 조회
	 * @param itemParam
	 * @return
	 */
	List<Item> getMainDisplayItemListByParam(ItemParam itemParam);

	/**
	 * 상품 조회 수 증가.
	 * @param itemId
	 */
	void updateItemHitsByItemId(int itemId);

	/**
	 * 옵션코드가 등록된 상품의 ItemId를 조회
	 * @param optionCode
	 * @return
	 */
	List<Item> getItemListByOptionCode(String optionCode);

	/**
	 * 오픈마켓에서 판매되는 상품중에 복수 옵션에 대한 처리
	 * @param openMarketItemParam
	 * @return
	 */
	List<Item> getItemListByOpenMarketItemParam(OpenMarketItemParam openMarketItemParam);

	/**
	 * 상품에 설정된 카테고리 정보 조회
	 * @param itemId
	 * @return
	 */
	List<ItemCategory> getItemCategoryListByItemId(int itemId);


	/**
	 * 상품 고시 유형 목록.
	 * @return
	 */
	List<ItemNotice> getItemNoticeCodes();

	/**
	 * 상품 유형 코드에 해당하는 상품 고시 목록을 가져옴.
	 * @param itemNoticeCode
	 */
	List<ItemNotice> getItemNoticeListByCode(String itemNoticeCode);

	/**
	 * 상품 옵션 목록..
	 * @param itemId
	 * @return
	 */
	List<ItemOption> getItemOptionListForManager(int itemId);

	List<ItemOption> getItemOptionList(int itemId);

	void updateItemOption(ItemOption itemOption);


	/**
	 * 추가구성 상품 등록
	 * @param additionItem
	 */
	void insertAddtionItem(Item additionItem);

	/**
	 * 추가구성 상품 정보 등록.
	 * @param itemAddition
	 */
	void insertItemAddtion(HashMap<String, Integer> itemAddition);

	/**
	 * 추가구성 상품 삭제
	 * @param itemId
	 */
	void deleteAdditionItemByItemId(int itemId);

	/**
	 * 추가구성 상품 정보 삭제
	 * @param itemId
	 */
	void deleteItemAdditionByItemId(int itemId);


	/**
	 * 추가 상품 목록
	 * @param itemId
	 * @return
	 */
	List<Item> getItemAdditionList(int itemId);


	/**
	 * 상품 옵션 이미지 목록
	 * @param itemId
	 * @return
	 */
	List<ItemOptionImage> getItemOptionImageList(int itemId);

	/**
	 * 출고지 배송금액 수정
	 * @param shipment
	 */
	void updateShipmentPrice(Shipment shipment);

	/**
	 * 출고지 배송정보 수정
	 * @param shipment
	 */
	void updateShipment(Shipment shipment);

	/**
	 * 교환/반품 배송금액 수정
	 * @param shipmentReturn
	 */
	void updateShipmentReturn(ShipmentReturn shipmentReturn);

	/**
	 *	관리자 메인페이지 제품카운트
	 * @param sellerId
	 *
	 */
	List<Item> getItemCountForMain(long sellerId);

	/**
	 * 상품정보 + 옵션정보 카운트
	 * @param itemParam
	 * @return
	 */
	int getItemAddOptionCountByParam(ItemParam itemParam);

	/**
	 * 상품정보 + 옵션정보 리스트
	 * @param itemParam
	 * @return
	 */
	List<ItemAddOption> getItemAddOptionListByParam(ItemParam itemParam);

	/**
	 * 판매자 정보 수정 시 상품 배송비 정보 일괄 업데이트
	 * @param seller
	 */
	void updateShipmentPriceForSeller(Seller seller);

	/**
	 * 판매자 정보의 담당MD가 변경된 경우 해당 업체 상품 정보에 변경 전 담당MD가 지정된 경우에만 상품MD 일괄 UPDATE
	 * @param seller
	 */
	int updateItemMdUserForSeller(Seller seller);

	/**
	 * 가격변경요청
	 * @param itemSaleEdit
	 */
	void insertItemSaleEdit(ItemSaleEdit itemSaleEdit);

	/**
	 * 가격변경요청리스트
	 * @param itemSaleEditParam
	 * @return
	 */
	List<ItemSaleEdit> getItemSaleEdit(ItemSaleEditParam itemSaleEditParam);

	/**
	 * 가격변경요청카운트
	 * @param itemSaleEditParam
	 * @return
	 */
	int getItemSaleEditCountByParam(ItemSaleEditParam itemSaleEditParam);

	/**
	 * 가격변경요청삭제
	 * @param itemSaleEdit
	 */
	public void deleteItemSaleEdit(ItemSaleEdit itemSaleEdit);

	/**
	 * 가격변경요청수정
	 * @param itemSaleEdit
	 */
	public void updateSaleEdit(ItemSaleEdit itemSaleEdit);

	/**
	 * 가격변경요청상태변경
	 * @param itemSaleEdit
	 */
	public void updateSaleEditStatus(ItemSaleEdit itemSaleEdit);

	/**
	 * 가격요청가져오기
	 * @param itemSaleEditParam
	 * @return
	 */
	public ItemSaleEdit getItemSaleEditByParam(ItemSaleEditParam itemSaleEditParam);

	/**
	 * 상품가격변경
	 * @param itemSaleEdit
	 * @return
	 */
	public void updateItemPrice(ItemSaleEdit itemSaleEdit);

	/**
	 * 아이템 가격정보 가져오기
	 * @param itemSaleEdit
	 * @return
	 */
	public Item getItemByItemSaleEdit(ItemSaleEdit itemSaleEdit);



	public List<ChosenItem> getChosenItemList(List<String> list);
	public List<ChosenItem> getExcludeItemList(List<String> list);

	public List<ChosenItem> getSearchItemList(ChosenItem chosenItem);


	/**
	 * 상품 카테고리 데이터를 삭제한다. (itemListParam)
	 * @param itemListParam
	 */
	void deleteItemCategoryByListParam(ItemListParam itemListParam);



	/**
	 * 상품 카테고리 데이터를 추가한다. (itemListParam)
	 * @param itemListParam
	 */
	void insertItemCategoryByListParam(ItemListParam itemListParam);

	/**
	 * 상품 간편 관리 일괄 업데이트.
	 * @param item
	 */
	void updateItemForSimple(Item item);

	/**
	 * 상품 변경 로그를 기록.
	 * @param item
	 */
	void insertItemLog(Item item);

	/**
	 * 상품 변경 로그를 기록. (판매자 상품 승인 시)
	 * @param item
	 */
	void insertItemLogForApproval(Item item);

	/** kye 추가
	 * 상품 미등록 리뷰 카운트
	 * @param itemParam
	 * @return
	 */
	int getItemNonregisteredReviewCount(ItemParam itemParam);

	/** kye 추가
	 * 상품 미등록 리뷰 리스트
	 * @param itemParam
	 * @return
	 */
	List<OrderItem> getItemNonregisteredReviewList(ItemParam itemParam);

	/**
	 * OP_ITEM_OPTION_SOLDOUT 테이블 데이터를 모두 삭제.
	 */
	void deleteItemOptionSoldout();

	/**
	 * OP_ITEM_OPTION_SOLDOUT 데이터 전체 등록
	 */
	void insertItemOptionSoldout();

	/**
	 * 그룹 배너에 설정된 상품 목록 (그룹별 한방 쿼리로 조회)
	 * @param shopCategoryGroups
	 * @return
	 */
	List<saleson.shop.categories.domain.Group> getGroupItemsForGroupBanner(
			List<saleson.shop.categories.domain.Group> shopCategoryGroups);

	/**
	 * OP_ITEM_HIT 테이블에 조회 수를 등록한다.
	 * @param itemId
	 */
	void mergeItemHits(int itemId);


	/**
	 * OP_ITEM 테이블에 조회 수 일괄 반영
	 */
	void updateItemHitsAll();

	/**
	 * 해당 카테고리에 속한 상품들 중 판매 금액 정보 조회
	 * @param itemParam
	 * @return
	 */
	SalePriceInfo getSalePriceInfoByCategoryClass(ItemParam itemParam);

	/**
	 * 상품후기 이미지 Insert
	 * @param itemReview
	 */
	void insertItemReviewImage(ItemReview itemReview);

	/**
	 * 상품후기 개수 조회
	 * @param itemReviewId
	 * @return
	 */
	int getItemReviewImageCount(int itemReviewId);

	/**
	 * 세트상품 등록
	 * @param item
	 */
	void insertItemSet(Item item);

	/**
	 * 세트상품 삭제
	 * @param parentId
	 */
	void deleteItemSetByParentItemId(int parentId);

	/**
	 * 세트상품 삭제
	 * @param itemId
	 */
	void deleteItemSetByItemId(int itemId);

	/**
	 * 세트상품 조회 (by id)
	 * @param itemId
	 */
	List<ItemSet> getItemSetListByItemId(int itemId);

	/**
	 * 상품 > 세트상품 조회 (by list)
	 * @param ids
	 * @return
	 */
	List<Item> getItemListForItemSet(List<Integer> ids);

	/**
	 * 도움된 상품 후기 카운트 업데이트
	 * @param itemReviewId
	 */
	void updateItemReviewLikeCount(int itemReviewId);

	/**
	 * 브랜드 수정시 상품의 브랜드명 동기화
	 * @param brand
	 */
	void updateItemBrandName(Brand brand);

	/**
	 * 판매자 정보
	 * @param userId
	 * @return
	 */
	SellerUser getLoginSellerInfo(long userId);

	/**
	 * 카테고리, 금액대, 키워드, 자치구 로 상품 조회
	 * @param CategoryParam
	 * @return
	 */
	List<Item> getItemListByCategoryParam(CategoryParam categoryParam);

	/**
	 * 카테고리, 금액대, 키워드, 자치구 로 상품 조회 카운트
	 * @param CategoryParam
	 * @return
	 */
	int getItemListCountByCategoryParam(CategoryParam categoryParam);


	/**
	 * 제철식품관 상품 검색
	 * @return List<Item>
	 */
	List<Item> getSeasonalFoodKeywordItem(SeasonalFood seasonalFood);


	/**
	 * 제철식품관 상품 검색 카운트
	 * @return int
	 */
	int getSeasonalFoodKeywordItemCnt(SeasonalFood seasonalFood);

	/**
	 * NEW 삭제
	 * @return int
	 */
	void deleteItemNew();

	/**
	 * 제철식품관상품 삭제
	 * @return
	 */
	void deleteSeasonFoodItem(int itemId);

	/**
	 * 제철식품관상품 등록한다.
	 * @param seasonFoodItem
	 */
	void insertSeasonFoodItem(SeasonFoodItem seasonFoodItem);

	/**
	 * 제철식품관상품 조회한다.
	 * @param seasonFoodItem
	 */
	List<Integer> getSeasonFoodItem(int itemId);

	/**
	 * 상품의 접근성 이미지 설명을 조회한다.
	 * @param itemId
	 * @return
	 */
	List<ItemImageExplain> getItemImagesExplain(int itemId);



	/**
	 * 장바구니 삭제 (품절)
	 * @param itemId
	 */
	void deleteCartBySoldOut(Item item);


	/**
	 * 답례품 옵션의 재고가 모두 0인 답례품이 장바구니에 있을 경우 삭제
	 *
	 */
	void deleteSoldoutItemOption();

	/**
	 * 지자체에 해당하는 관리자 목록
	 * @param locgovCode
	 * @return
	 */
	List<Item> getLocgovManagerList(String locgovCode);



	/**
	 * 지자체 대표 답례품 등록
	 * @param ItemParam
	 * @return
	 */
	int insertLclgvRprsGds(ItemParam param);

	/**
	 * 지자체 대표 답례품 삭제
	 * @param ItemParam
	 * @return
	 */
	int deleteLclgvRprsGds(ItemParam param);

	/**
	 * 지자체에 해당하는 대표 답례품 목록 조회
	 * @param userId
	 * @return
	 */
	List<Item> getLclgvRprsGdsList(long userId);

	/**
	 * 지자체에 해당하는 대표 답례품 목록 조회(시스템관리자용)
	 * @param userId
	 * @return
	 */
	List<Item> getLclgvRprsGdsListManager(String lclgvCd);

	// 오프라인 대표 답례품 관련 메서드
	/**
	 * 오프라인 대표 답례품 삭제
	 * @param itemParam
	 * */
	void deleteLclgvOffRprsGds(ItemParam itemParam);

	/**
	 * 오프라인 대표 답례품 등록
	 * @param itemParam
	 * */
	void insertLclgvOffRprsGds(List<Map<String, String>> optionList);

	/**
	 * 오프라인 대표 답례품 조회 - 지자체용
	 * @param userId
	 * */
	List<Item> getLclgvOffRprsGdsList(long userId);

	/**
	 * 오프라인 대표 답례품 조회 - 시스템 관리자용
	 * @param lclgvCd
	 * */
	List<Item> getLclgvOffRprsGdsListManager(String lclgvCd);

	/**
	 * 답례품 조회
	 * @param itemId
	 * return
	 * */
	Item getItem(Integer itemId);

	/**
	 * 답례품 옵션 조회
	 * @param item
	 * return
	 * */
	List<ItemOption> getItemOption(Item item);

	/**
	 * 답례품 조합형 옵션 종류 및 가격 조회
	 *
	 * @param optionMap
	 * @return
	 */
	List<Map<String, String>> getComboOptionList(Map<String, Object> optionMap);

	/**
	 * 답례품 조합형 가격 조회
	 *
	 * @param optionMap
	 * @return
	 * @throws Exception
	 */
	String getSelectOption(Integer itemId);

	/**
	 * 상품정보 조회(답례품승인관리 미리보기)
	 * @param itemParam
	 * @return
	 */
	Item getItemByParamPreview(ItemParam itemParam);
}

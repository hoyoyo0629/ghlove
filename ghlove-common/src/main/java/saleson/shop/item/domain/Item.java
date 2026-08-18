package saleson.shop.item.domain;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import saleson.common.utils.CommonUtils;
import saleson.model.GiftItem;
import saleson.seller.main.domain.Seller;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.seo.domain.Seo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SuperBuilder
@AllArgsConstructor
public class Item extends ItemBase {
	private int itemLogId;
	private String managerLoginId;
	private String userName;
	private long createdManagerId;
	private long createdSellerId;
	private String actionType;
	private String processPage; // 등록 처리 페이지 기준 ('mananger', 'seller')
	private String couponUseFlag = "Y";
	private String cartOptions;
	private String representativeItemYn;	// 대표상품여부
	private String options;					// 오프라인 답례품 테이블 옵션 값
	private String itemOptionId;
	private String changedOption;			// 옵션 변경 여부

	private List<ItemImageExplain> itemImageExplain = new ArrayList<>();

	private String communityBusinessYn;


	public List<ItemImageExplain> getItemImageExplain() {
		return itemImageExplain;
	}

	public void setItemImageExplain(List<ItemImageExplain> itemImageExplain) {
		this.itemImageExplain = itemImageExplain;
	}

	// 네이버
	private String naverShoppingItemName;	// 네이버쇼핑 상품명

	private String naverShoppingFlag = "N";	// 네이버쇼핑 사용여부
	private String naverPayFlag = "Y";		// 네이버페이 사용여부


	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getItemOptionId() {
		return itemOptionId;
	}

	public void setItemOptionId(String itemOptionId) {
		this.itemOptionId = itemOptionId;
	}

	public String getChangedOption() {
		return changedOption;
	}

	public void setChangedOption(String changedOption) {
		this.changedOption = changedOption;
	}

	public String getCouponUseFlag() {
		return couponUseFlag;
	}

	public void setCouponUseFlag(String couponUseFlag) {
		this.couponUseFlag = couponUseFlag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getManagerLoginId() {
		return managerLoginId;
	}

	public void setManagerLoginId(String managerLoginId) {
		this.managerLoginId = managerLoginId;
	}

	public int getItemLogId() {
		return itemLogId;
	}

	public void setItemLogId(int itemLogId) {
		this.itemLogId = itemLogId;
	}

	public long getCreatedManagerId() {
		return createdManagerId;
	}

	public void setCreatedManagerId(long createdManagerId) {
		this.createdManagerId = createdManagerId;
	}

	public long getCreatedSellerId() {
		return createdSellerId;
	}

	public void setCreatedSellerId(long createdSellerId) {
		this.createdSellerId = createdSellerId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getProcessPage() {
		return processPage;
	}

	public void setProcessPage(String processPage) {
		this.processPage = processPage;
	}

	private long sellerId;
	private Seller seller;
	private String itemCode;

	/**
	 * ERP 엑셀 다운로드시 에러 대응 코드
	 * 1 : 옵션 상품 판매시 상품과 옵션을 ERP에 같이 나오게 한다 (추가 상품처럼~)
	 */
	private String erpExceptionType;
	public String getErpExceptionType() {
		return erpExceptionType;
	}

	public void setErpExceptionType(String erpExceptionType) {
		this.erpExceptionType = erpExceptionType;
	}

	private String itemDataType = "1";
	private String supplyType;				// 공급사 구분 (1:본사상품, 2:공급사상품)
	private String itemLabel = "0";

	private String itemType1;
	private String itemType2;
	private String itemType3;
	private String itemType4;
	private String itemType5;
	private String itemNewFlag = "N";	// 신상품여부

	private String originCountry = "";
	private String manufacturer = "";
	private int brandId;
	private String brand = "";
	private String color = "";
	private String weight;				// 상품무게.
	private int salePoint;
	private int costPrice;
	private int supplyPrice;

	private int sellerGivePoint;			// 판매자 부담 지급(적립)포인트.

	private String commissionType = "1";
	private float commissionRate;
	private String priceCriteria = "2"; 	//가격 입력 기준 (1: 공급가기준-수수료자동입력, 2: 수수료기준-공급가 자동입력)
	private String stockFlag = "N";
	private String stockCode = "";
	private int stockQuantity = -1;
	private String stockScheduleAutoFlag;
	private String stockScheduleType = "";
	private String stockScheduleDate = "";
	private String stockScheduleText = "";
	private int orderMinQuantity = -1;
	private int orderMaxQuantity = -1;
	private int saleQuantity;
	private String itemOptionTitle1 = "";
	private String itemOptionTitle2 = "";
	private String itemOptionTitle3 = "";
	private String itemOptionType = "S";
	private String itemAdditionFlag = "N";

	private String freeGiftFlag = "N";
	private String freeGiftName = "";
	private String[] freeGiftItemIds;
	private List<GiftItem> freeGiftItemList = new ArrayList<>();

	// 세트상품
	private int[] setItemIds;
	private int[] setQuantities;

	private String itemKeyword = "";

	private String listContent = "";
	private String detailContentTop = "";
	private String detailContentTopMobile = "";
	private String detailContent = "";
	private String detailContentMobile = "";
	private String useManual = "";
	private String makeManual = "";

	private String itemNoticeCode;

	private String team = "";
	private String opentime = "";
	private String baseItem = "";
	private String otherFlag = "1";
	private String recommendFlag = "N";
	private String relationItemDisplayType = "1";	// 1: 자동, 2: 직접선택

	// 택배사 정보
	private int deliveryCompanyId;
	private String deliveryCompanyName;

	private String deliveryType;	// 배송구분 (1: 본사배송, 2: 업체배송)
	private String shipmentGroupCode; 	// 배송정책 묶음배송 기준 코드

	// 배송지 / 배송비
	private int shipmentId;
	private String shipmentAddress;		// 출고지 주소
	private int shipmentReturnId;
	private String shipmentReturnType;
	private String shipmentReturnAddress;		// 반송지 주소
	private String shippingType;
	private String shippingGroupCode;
	private int shipping;
	private int shippingFreeAmount;
	private int shippingItemCount = 1;		// 개당 배송비 부과 시 기준 상품 수.
	private int shippingExtraCharge1;
	private int shippingExtraCharge2;
	private int shippingReturn;			// 반품/교환 배송비 (편도 기준금액)
	private int hits;
	private int displayOrder;

	private String privateType; 		// 전용상품 (OP_COMMON_CODE > ITEM_PRIVATE_TYPE)
	private String mdId;				// 담당 MDID
	private String mdName;				// 담당 MD 이름.

	private String seoTitle = "";
	private String seoKeywords = "";
	private String seoDescription = "";
	private String seoHeaderContents1 = "";
	private String seoThemawordTitle = "";
	private String seoThemawordDescription = "";
	private String dataStatusMessage;
	private long updatedUserId;
	private String updatedDate;
	private long createdUserId;
	private String createdDate;

	private Seo seo = new Seo();

	// 상품 리뷰
	private ItemReview itemReview;

	// 상품 리뷰 카운트 및 점수.
	private String reviewCount;
	private String reviewScore;

	private List<Breadcrumb> breadcrumbs;

	private List<ItemInfo> itemInfos = new ArrayList<>();
	private List<ItemInfo> itemInfoMobiles = new ArrayList<>();
	private List<ItemCategory> itemCategories = new ArrayList<>();
	private List<ItemImage> itemImages = new ArrayList<>();
	private List<ItemOption> itemOptions = new ArrayList<>();
	private List<ItemOptionGroup> itemOptionGroups = new ArrayList<>();
	private List<ItemRelation> itemRelations = new ArrayList<>();

	// 옵션이미지
	private List<ItemOptionImage> itemOptionImages = new ArrayList<>();

	// 추가상품목록
	private List<Item> itemAdditions = new ArrayList<>();

	// 상품카테고리
	private int[] categoryIds;

	// 관련상품
	private int[] relatedItemIds;


	// 상품옵션
	private String[] optionId;
	private String[] optionType;
	private String[] optionName1;		// 옵션 숨김 여부 (부모)
	private String[] optionName2;		// 옵션 세부항목 노출여
	private String[] optionName3;
	private String[] optionPrice;
	private String[] optionCostPrice;
	private String[] optionStockFlag;
	private String[] optionStockQuantity;
	private String[] optionSoldOutFlag;
	private String[] optionDisplayFlag;
	private String[] optionStockCode;

	/*
	private String[] optionDisplayType;
	private String[] optionHideFlag;		// 옵션 숨김 여부 (부모)
	private String[] optionDisplayFlag;		// 옵션 세부항목 노출여
	private String[] optionName1;
	private String[] optionName2;
	private String[] optionCode;
	private String[] optionCostPrice;
	private String[] optionPrice;
	private String[] optionPriceNonmemer;
	private String[] optionStockQuantity;
	private String[] optionStockScheduleText;
	private String[] optionStockScheduleDate;
	*/


	// 추가구성 상품
	private String[] additionItemId;
	private String[] additionItemName;
	private String[] additionSalePrice;
	private String[] additionCostPrice;
	private String[] additionStockFlag;
	private String[] additionStockQuantity;
	private String[] additionStockCode;
	private String[] additionSoldOut;
	private String[] additionTaxType;
	private String[] additionDisplayFlag;
	private String[] additionWeight;

	// 적립금
	private String[] pointType;
	private double[] point;
	private String[] pointStartDate;
	private String[] pointStartTime;
	private String[] pointEndDate;
	private String[] pointEndTime;
	private String[] pointRepeatDay;


	// 상품 상세 정보
	private String[] itemInfoTitles;
	private String[] itemInfoDescriptions;

	// 상품 상세 정보
	private String[] itemInfoMobileTitles;
	private String[] itemInfoMobileDescriptions;



	// 상품이미지
	private MultipartFile itemImageFile;


	// 상품이미지
	private MultipartFile[] itemDetailImageFiles;

	// 상세이미지 순서변경
	private int[] itemImageIds;

	// 이미지 복사
	private int[] copyItemImageIds;

	//관리자, 판매관리자 메인페이지에 제품 카운트를 위한값
	private int cnt;

	// 없어서 에러 발생 그래서 일단추가
	private String simpleContent;

	// 반품신청 가능여부
	private String itemReturnFlag;

	private String tempId;
	private String tempControl;

	// 카테고리 필터 관련
	private String[] filterGroups;
	private Map<String, List<String>> filterCodes;

	private boolean wishlistFlag;

	// 20230306 추가, ItemBase 에 있어서 주석처리
//	private String mobileItemYn;	// 모바일상품여부
//	private String adultItemYn;		// 성인상품여부
	List<Integer> seasonFoodMonthList = new ArrayList<>();

	public String getItemReturnFlag() {
		return itemReturnFlag;
	}

	public void setItemReturnFlag(String itemReturnFlag) {
		this.itemReturnFlag = itemReturnFlag;
	}

	public String getSimpleContent() {
		return simpleContent;
	}

	public void setSimpleContent(String simpleContent) {
		this.simpleContent = simpleContent;
	}
	// 없어서 에러 발생 그래서 일단추가

	public Item() {}

	// 오늘 본 상품용 생성자
	public Item(int itemId, String itemUserCode, String itemImage) {
		super(itemId, itemUserCode, itemImage);
	}


	public MultipartFile getItemImageFile() {
		return itemImageFile;
	}
	public void setItemImageFile(MultipartFile itemImageFile) {
		this.itemImageFile = itemImageFile;
	}
	public MultipartFile[] getItemDetailImageFiles() {
		return CommonUtils.copy(itemDetailImageFiles);
	}
	public void setItemDetailImageFiles(MultipartFile[] itemDetailImageFiles) {
		this.itemDetailImageFiles = CommonUtils.copy(itemDetailImageFiles);
	}


	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}


	public String getItemDataType() {
		return itemDataType;
	}

	public void setItemDataType(String itemDataType) {
		this.itemDataType = itemDataType;
	}

	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public String getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(String supplyType) {
		this.supplyType = supplyType;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getShipmentGroupCode() {
		return shipmentGroupCode;
	}

	public void setShipmentGroupCode(String shipmentGroupCode) {
		this.shipmentGroupCode = shipmentGroupCode;
	}

	public String getItemType1() {
		return itemType1;
	}
	public void setItemType1(String itemType1) {
		this.itemType1 = itemType1;
	}
	public String getItemType2() {
		return itemType2;
	}
	public void setItemType2(String itemType2) {
		this.itemType2 = itemType2;
	}
	public String getItemType3() {
		return itemType3;
	}
	public void setItemType3(String itemType3) {
		this.itemType3 = itemType3;
	}
	public String getItemType4() {
		return itemType4;
	}
	public void setItemType4(String itemType4) {
		this.itemType4 = itemType4;
	}
	public String getItemType5() {
		return itemType5;
	}
	public void setItemType5(String itemType5) {
		this.itemType5 = itemType5;
	}

	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getSalePoint() {
		return salePoint;
	}

	public void setSalePoint(int salePoint) {
		this.salePoint = salePoint;
	}

	public int getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}


	public int getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(int supplyPrice) {
		this.supplyPrice = supplyPrice;
	}


	public int getSellerGivePoint() {
		return sellerGivePoint;
	}

	public void setSellerGivePoint(int sellerGivePoint) {
		this.sellerGivePoint = sellerGivePoint;
	}

	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public float getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(float commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getPriceCriteria() {
		return priceCriteria;
	}

	public void setPriceCriteria(String priceCriteria) {
		this.priceCriteria = priceCriteria;
	}

	public String getStockFlag() {
		return stockFlag;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public void setStockFlag(String stockFlag) {
		this.stockFlag = stockFlag;
	}
	public int getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public String getStockScheduleAutoFlag() {
		return stockScheduleAutoFlag;
	}
	public void setStockScheduleAutoFlag(String stockScheduleAutoFlag) {
		this.stockScheduleAutoFlag = stockScheduleAutoFlag;
	}
	public String getStockScheduleType() {
		return stockScheduleType;
	}
	public void setStockScheduleType(String stockScheduleType) {
		this.stockScheduleType = stockScheduleType;
	}
	public String getStockScheduleDate() {
		return stockScheduleDate;
	}
	public void setStockScheduleDate(String stockScheduleDate) {
		this.stockScheduleDate = stockScheduleDate;
	}
	public String getStockScheduleText() {
		return stockScheduleText;
	}
	public void setStockScheduleText(String stockScheduleText) {
		this.stockScheduleText = stockScheduleText;
	}

	public int getOrderMinQuantity() {
		return orderMinQuantity;
	}

	public void setOrderMinQuantity(int orderMinQuantity) {
		this.orderMinQuantity = orderMinQuantity;
	}

	public int getOrderMaxQuantity() {
		return orderMaxQuantity;
	}

	public void setOrderMaxQuantity(int orderMaxQuantity) {
		this.orderMaxQuantity = orderMaxQuantity;
	}

	public int getSaleQuantity() {
		return saleQuantity;
	}
	public void setSaleQuantity(int saleQuantity) {
		this.saleQuantity = saleQuantity;
	}
	public String getItemOptionTitle1() {
		return itemOptionTitle1;
	}
	public void setItemOptionTitle1(String itemOptionTitle1) {
		this.itemOptionTitle1 = itemOptionTitle1;
	}
	public String getItemOptionTitle2() {
		return itemOptionTitle2;
	}
	public void setItemOptionTitle2(String itemOptionTitle2) {
		this.itemOptionTitle2 = itemOptionTitle2;
	}
	public String getItemOptionTitle3() {
		return itemOptionTitle3;
	}
	public void setItemOptionTitle3(String itemOptionTitle3) {
		this.itemOptionTitle3 = itemOptionTitle3;
	}

	public String getItemOptionType() {
		return itemOptionType;
	}

	public void setItemOptionType(String itemOptionType) {
		this.itemOptionType = itemOptionType;
	}

	public String getItemAdditionFlag() {
		return itemAdditionFlag;
	}
	public void setItemAdditionFlag(String itemAdditionFlag) {
		this.itemAdditionFlag = itemAdditionFlag;
	}
	public String getFreeGiftFlag() {
		return freeGiftFlag;
	}
	public void setFreeGiftFlag(String freeGiftFlag) {
		this.freeGiftFlag = freeGiftFlag;
	}
	public String getFreeGiftName() {
		return freeGiftName;
	}
	public void setFreeGiftName(String freeGiftName) {
		this.freeGiftName = freeGiftName;
	}

	public String[] getFreeGiftItemIds() {
		if (freeGiftItemIds == null) {
			return null;
		} else {
			int length = freeGiftItemIds.length;
			String[] array = new String[length];
			for(int idx = 0 ; idx < length ; idx++) {
				array[idx] = freeGiftItemIds[idx];
			}
			return array;
		}
	}

	public void setFreeGiftItemIds(String[] freeGiftItemIds) {
		if (freeGiftItemIds == null) {
			this.freeGiftItemIds = null;
		} else {
			int length = freeGiftItemIds.length;
			this.freeGiftItemIds = new String[length];
			// private 배열의 값을 복사한 별도의 배열을 리턴
			for (int i = 0 ; i < length ; i++) {
				this.freeGiftItemIds[i] = freeGiftItemIds[i];
			}
		}
	}

	public List<GiftItem> getFreeGiftItemList() {
		return freeGiftItemList;
	}

	public void setFreeGiftItemList(List<GiftItem> freeGiftItemList) {
		this.freeGiftItemList = freeGiftItemList;
	}

	public String getItemKeyword() {
		return itemKeyword;
	}
	public void setItemKeyword(String itemKeyword) {
		this.itemKeyword = itemKeyword;
	}

	public String getListContent() {
		return listContent;
	}
	public void setListContent(String listContent) {
		this.listContent = listContent;
	}

	public String getDetailContentTop() {
		return detailContentTop;
	}

	public void setDetailContentTop(String detailContentTop) {
		this.detailContentTop = detailContentTop;
	}

	public String getDetailContentTopMobile() {
		return detailContentTopMobile;
	}

	public void setDetailContentTopMobile(String detailContentTopMobile) {
		this.detailContentTopMobile = detailContentTopMobile;
	}

	public String getDetailContent() {
		return detailContent;
	}
	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}
	public String getDetailContentMobile() {
		return detailContentMobile;
	}
	public void setDetailContentMobile(String detailContentMobile) {
		this.detailContentMobile = detailContentMobile;
	}
	public String getUseManual() {
		return useManual;
	}
	public void setUseManual(String useManual) {
		this.useManual = useManual;
	}
	public String getMakeManual() {
		return makeManual;
	}
	public void setMakeManual(String makeManual) {
		this.makeManual = makeManual;
	}

	public String getItemNoticeCode() {
		return itemNoticeCode;
	}

	public void setItemNoticeCode(String itemNoticeCode) {
		this.itemNoticeCode = itemNoticeCode;
	}

	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getBaseItem() {
		return baseItem;
	}
	public void setBaseItem(String baseItem) {
		this.baseItem = baseItem;
	}
	public String getOtherFlag() {
		return otherFlag;
	}
	public void setOtherFlag(String otherFlag) {
		this.otherFlag = otherFlag;
	}
	public String getRecommendFlag() {
		return recommendFlag;
	}
	public void setRecommendFlag(String recommendFlag) {
		this.recommendFlag = recommendFlag;
	}
	public String getRelationItemDisplayType() {
		return relationItemDisplayType;
	}
	public void setRelationItemDisplayType(String relationItemDisplayType) {
		this.relationItemDisplayType = relationItemDisplayType;
	}

	public int getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(int deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}

	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}

	public int getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(int shipmentId) {
		this.shipmentId = shipmentId;
	}

	public int getShipmentReturnId() {
		return shipmentReturnId;
	}

	public void setShipmentReturnId(int shipmentReturnId) {
		this.shipmentReturnId = shipmentReturnId;
	}


	public String getShipmentAddress() {
		return shipmentAddress;
	}

	public void setShipmentAddress(String shipmentAddress) {
		this.shipmentAddress = shipmentAddress;
	}

	public String getShipmentReturnAddress() {
		return shipmentReturnAddress;
	}

	public void setShipmentReturnAddress(String shipmentReturnAddress) {
		this.shipmentReturnAddress = shipmentReturnAddress;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getShippingGroupCode() {
		return shippingGroupCode;
	}

	public void setShippingGroupCode(String shippingGroupCode) {
		this.shippingGroupCode = shippingGroupCode;
	}

	public int getShipping() {
		return shipping;
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	public int getShippingFreeAmount() {
		return shippingFreeAmount;
	}

	public void setShippingFreeAmount(int shippingFreeAmount) {
		this.shippingFreeAmount = shippingFreeAmount;
	}


	public int getShippingItemCount() {
		return shippingItemCount;
	}

	public void setShippingItemCount(int shippingItemCount) {
		this.shippingItemCount = shippingItemCount;
	}

	public int getShippingExtraCharge1() {
		return shippingExtraCharge1;
	}

	public void setShippingExtraCharge1(int shippingExtraCharge1) {
		this.shippingExtraCharge1 = shippingExtraCharge1;
	}

	public int getShippingExtraCharge2() {
		return shippingExtraCharge2;
	}

	public void setShippingExtraCharge2(int shippingExtraCharge2) {
		this.shippingExtraCharge2 = shippingExtraCharge2;
	}

	public int getShippingReturn() {
		return shippingReturn;
	}

	public void setShippingReturn(int shippingReturn) {
		this.shippingReturn = shippingReturn;
	}

	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getPrivateType() {
		return privateType;
	}

	public void setPrivateType(String privateType) {
		this.privateType = privateType;
	}

	public String getMdId() {
		return mdId;
	}

	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	public String getMdName() {
		return mdName;
	}

	public void setMdName(String mdName) {
		this.mdName = mdName;
	}

	public String getSeoTitle() {
		return seoTitle;
	}
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}
	public String getSeoDescription() {
		return seoDescription;
	}
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoHeaderContents1() {
		return seoHeaderContents1;
	}
	public void setSeoHeaderContents1(String seoHeaderContents1) {
		this.seoHeaderContents1 = seoHeaderContents1;
	}
	public String getSeoThemawordTitle() {
		return seoThemawordTitle;
	}
	public void setSeoThemawordTitle(String seoThemawordTitle) {
		this.seoThemawordTitle = seoThemawordTitle;
	}

	public String getSeoThemawordDescription() {
		return seoThemawordDescription;
	}
	public void setSeoThemawordDescription(String seoThemawordDescription) {
		this.seoThemawordDescription = seoThemawordDescription;
	}

	public String getDataStatusMessage() {
		return dataStatusMessage;
	}

	public void setDataStatusMessage(String dataStatusMessage) {
		this.dataStatusMessage = dataStatusMessage;
	}

	public long getUpdatedUserId() {
		return updatedUserId;
	}
	public void setUpdatedUserId(long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(long createdUserId) {
		this.createdUserId = createdUserId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public List<ItemCategory> getItemCategories() {
		return itemCategories;
	}
	public void setItemCategories(List<ItemCategory> itemCategories) {
		this.itemCategories = itemCategories;
	}


	public List<ItemOption> getItemOptions() {
		return itemOptions;
	}

	public void setItemOptions(List<ItemOption> itemOptions) {
		this.itemOptions = itemOptions;
	}

	public List<ItemOptionGroup> getItemOptionGroups() {
		return itemOptionGroups;
	}
	public void setItemOptionGroups(List<ItemOptionGroup> itemOptionGroups) {
		this.itemOptionGroups = itemOptionGroups;
	}
	public int[] getRelatedItemIds() {
		return CommonUtils.copy(relatedItemIds);
	}
	public void setRelatedItemIds(int[] relatedItemIds) {
		this.relatedItemIds = CommonUtils.copy(relatedItemIds);
	}
	public String[] getOptionDisplayFlag() {
		return CommonUtils.copy(optionDisplayFlag);
	}
	public void setOptionDisplayFlag(String[] optionDisplayFlag) {
		this.optionDisplayFlag = CommonUtils.copy(optionDisplayFlag);
	}
	public String[] getOptionName1() {
		return CommonUtils.copy(optionName1);
	}
	public void setOptionName1(String[] optionName1) {
		this.optionName1 = CommonUtils.copy(optionName1);
	}
	public String[] getOptionName2() {
		return CommonUtils.copy(optionName2);
	}
	public void setOptionName2(String[] optionName2) {
		this.optionName2 = CommonUtils.copy(optionName2);
	}




	public String[] getOptionId() {
		return CommonUtils.copy(optionId);
	}

	public void setOptionId(String[] optionId) {
		this.optionId = CommonUtils.copy(optionId);
	}

	public String[] getOptionType() {
		return CommonUtils.copy(optionType);
	}

	public void setOptionType(String[] optionType) {
		this.optionType = CommonUtils.copy(optionType);
	}

	public String[] getOptionName3() {
		return CommonUtils.copy(optionName3);
	}

	public void setOptionName3(String[] optionName3) {
		this.optionName3 = CommonUtils.copy(optionName3);
	}

	public String[] getOptionPrice() {
		return CommonUtils.copy(optionPrice);
	}

	public void setOptionPrice(String[] optionPrice) {
		this.optionPrice = CommonUtils.copy(optionPrice);
	}

	public String[] getOptionCostPrice() {
		return CommonUtils.copy(optionCostPrice);
	}

	public void setOptionCostPrice(String[] optionCostPrice) {
		this.optionCostPrice = CommonUtils.copy(optionCostPrice);
	}

	public String[] getOptionStockFlag() {
		return CommonUtils.copy(optionStockFlag);
	}

	public void setOptionStockFlag(String[] optionStockFlag) {
		this.optionStockFlag = CommonUtils.copy(optionStockFlag);
	}

	public String[] getOptionStockQuantity() {
		return CommonUtils.copy(optionStockQuantity);
	}

	public void setOptionStockQuantity(String[] optionStockQuantity) {
		this.optionStockQuantity = CommonUtils.copy(optionStockQuantity);
	}



	public String[] getOptionSoldOutFlag() {
		return CommonUtils.copy(optionSoldOutFlag);
	}

	public void setOptionSoldOutFlag(String[] optionSoldOutFlag) {
		this.optionSoldOutFlag = CommonUtils.copy(optionSoldOutFlag);
	}

	public String[] getOptionStockCode() {
		return CommonUtils.copy(optionStockCode);
	}

	public void setOptionStockCode(String[] optionStockCode) {
		this.optionStockCode = CommonUtils.copy(optionStockCode);
	}



	public String[] getAdditionItemId() {
		return CommonUtils.copy(additionItemId);
	}

	public void setAdditionItemId(String[] additionItemId) {
		this.additionItemId = CommonUtils.copy(additionItemId);
	}

	public String[] getAdditionItemName() {
		return CommonUtils.copy(additionItemName);
	}

	public void setAdditionItemName(String[] additionItemName) {
		this.additionItemName = CommonUtils.copy(additionItemName);
	}

	public String[] getAdditionSalePrice() {
		return CommonUtils.copy(additionSalePrice);
	}

	public void setAdditionSalePrice(String[] additionSalePrice) {
		this.additionSalePrice = CommonUtils.copy(additionSalePrice);
	}

	public String[] getAdditionCostPrice() {
		return CommonUtils.copy(additionCostPrice);
	}

	public void setAdditionCostPrice(String[] additionCostPrice) {
		this.additionCostPrice = CommonUtils.copy(additionCostPrice);
	}

	public String[] getAdditionStockFlag() {
		return CommonUtils.copy(additionStockFlag);
	}

	public void setAdditionStockFlag(String[] additionStockFlag) {
		this.additionStockFlag = CommonUtils.copy(additionStockFlag);
	}

	public String[] getAdditionStockQuantity() {
		return CommonUtils.copy(additionStockQuantity);
	}

	public void setAdditionStockQuantity(String[] additionStockQuantity) {
		this.additionStockQuantity = CommonUtils.copy(additionStockQuantity);
	}

	public String[] getAdditionStockCode() {
		return CommonUtils.copy(additionStockCode);
	}

	public void setAdditionStockCode(String[] additionStockCode) {
		this.additionStockCode = CommonUtils.copy(additionStockCode);
	}

	public String[] getAdditionSoldOut() {
		return CommonUtils.copy(additionSoldOut);
	}

	public void setAdditionSoldOut(String[] additionSoldOut) {
		this.additionSoldOut = CommonUtils.copy(additionSoldOut);
	}

	public String[] getAdditionTaxType() {
		return CommonUtils.copy(additionTaxType);
	}

	public void setAdditionTaxType(String[] additionTaxType) {
		this.additionTaxType = CommonUtils.copy(additionTaxType);
	}

	public String[] getAdditionDisplayFlag() {
		return CommonUtils.copy(additionDisplayFlag);
	}

	public void setAdditionDisplayFlag(String[] additionDisplayFlag) {
		this.additionDisplayFlag = CommonUtils.copy(additionDisplayFlag);
	}

	public String[] getAdditionWeight() {
		return CommonUtils.copy(additionWeight);
	}

	public void setAdditionWeight(String[] additionWeight) {
		this.additionWeight = CommonUtils.copy(additionWeight);
	}

	public String[] getPointType() {
		return CommonUtils.copy(pointType);
	}
	public void setPointType(String[] pointType) {
		this.pointType = CommonUtils.copy(pointType);
	}
	public double[] getPoint() {
		return CommonUtils.copy(point);
	}
	public void setPoint(double[] point) {
		this.point = CommonUtils.copy(point);
	}
	public String[] getPointStartDate() {
		return CommonUtils.copy(pointStartDate);
	}
	public void setPointStartDate(String[] pointStartDate) {
		this.pointStartDate = CommonUtils.copy(pointStartDate);
	}
	public String[] getPointStartTime() {
		return CommonUtils.copy(pointStartTime);
	}
	public void setPointStartTime(String[] pointStartTime) {
		this.pointStartTime = CommonUtils.copy(pointStartTime);
	}
	public String[] getPointEndDate() {
		return CommonUtils.copy(pointEndDate);
	}
	public void setPointEndDate(String[] pointEndDate) {
		this.pointEndDate = CommonUtils.copy(pointEndDate);
	}

	public String[] getPointEndTime() {
		return CommonUtils.copy(pointEndTime);
	}
	public void setPointEndTime(String[] pointEndTime) {
		this.pointEndTime = CommonUtils.copy(pointEndTime);
	}
	public String[] getPointRepeatDay() {
		return CommonUtils.copy(pointRepeatDay);
	}
	public void setPointRepeatDay(String[] pointRepeatDay) {
		this.pointRepeatDay = CommonUtils.copy(pointRepeatDay);
	}
	public int[] getCategoryIds() {
		return CommonUtils.copy(categoryIds);
	}
	public void setCategoryIds(int[] categoryIds) {
		this.categoryIds = CommonUtils.copy(categoryIds);
	}

	public String getItemNewFlag() {
		return itemNewFlag;
	}
	public void setItemNewFlag(String itemNewFlag) {
		this.itemNewFlag = itemNewFlag;
	}
	public Seo getSeo() {
		return seo;
	}
	public void setSeo(Seo seo) {
		this.seo = seo;
	}

	public List<ItemImage> getItemImages() {
		return itemImages;
	}
	public void setItemImages(List<ItemImage> itemImages) {
		this.itemImages = itemImages;
	}
	public List<ItemRelation> getItemRelations() {
		return itemRelations;
	}
	public void setItemRelations(List<ItemRelation> itemRelations) {
		this.itemRelations = itemRelations;
	}
	public List<Breadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}
	public void setBreadcrumbs(List<Breadcrumb> breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
	}
	public int[] getItemImageIds() {
		return CommonUtils.copy(itemImageIds);
	}
	public void setItemImageIds(int[] itemImageIds) {
		this.itemImageIds = CommonUtils.copy(itemImageIds);
	}



	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(String reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(String reviewScore) {
		this.reviewScore = reviewScore;
	}


	public ItemReview getItemReview() {
		return itemReview;
	}

	public void setItemReview(ItemReview itemReview) {
		this.itemReview = itemReview;
	}



	public List<ItemInfo> getItemInfos() {
		return itemInfos;
	}

	public void setItemInfos(List<ItemInfo> itemInfos) {
		this.itemInfos = itemInfos;
	}

	public String[] getItemInfoTitles() {
		return CommonUtils.copy(itemInfoTitles);
	}

	public void setItemInfoTitles(String[] itemInfoTitles) {
		this.itemInfoTitles = CommonUtils.copy(itemInfoTitles);
	}

	public String[] getItemInfoDescriptions() {
		return CommonUtils.copy(itemInfoDescriptions);
	}

	public void setItemInfoDescriptions(String[] itemInfoDescriptions) {
		this.itemInfoDescriptions = CommonUtils.copy(itemInfoDescriptions);
	}

	public List<ItemInfo> getItemInfoMobiles() {
		return itemInfoMobiles;
	}

	public void setItemInfoMobiles(List<ItemInfo> itemInfoMobiles) {
		this.itemInfoMobiles = itemInfoMobiles;
	}

	public String[] getItemInfoMobileTitles() {
		return CommonUtils.copy(itemInfoMobileTitles);
	}

	public void setItemInfoMobileTitles(String[] itemInfoMobileTitles) {
		this.itemInfoMobileTitles = CommonUtils.copy(itemInfoMobileTitles);
	}

	public String[] getItemInfoMobileDescriptions() {
		return CommonUtils.copy(itemInfoMobileDescriptions);
	}

	public void setItemInfoMobileDescriptions(String[] itemInfoMobileDescriptions) {
		this.itemInfoMobileDescriptions = CommonUtils.copy(itemInfoMobileDescriptions);
	}

	public List<Item> getItemAdditions() {
		return itemAdditions;
	}

	public void setItemAdditions(List<Item> itemAdditions) {
		this.itemAdditions = itemAdditions;
	}


	public List<ItemOptionImage> getItemOptionImages() {
		return itemOptionImages;
	}

	public void setItemOptionImages(List<ItemOptionImage> itemOptionImages) {
		this.itemOptionImages = itemOptionImages;
	}

	// 상품 품절 여부 (상품 / 옵션 포함)
	public boolean isItemSoldOut() {
		if ("Y".equals(getItemSoldOutFlag())) {
			return true;
		} else {
			return false;
		}
	}

	public List<OptionGroup> getOptionGroups() {
		List<OptionGroup> optionGroups = new ArrayList<>();

		if ("N".equals(getItemOptionFlag())) {
			return optionGroups;
		}

		// S(선택형) 인 경우
		if ("S".equals(getItemOptionType())) {
			for (ItemOption itemOption : this.itemOptions) {

				boolean inArray = false;
				for(OptionGroup group : optionGroups) {
					if (group.getTitle().equals(itemOption.getOptionName1())) {
						inArray = true;
						break;
					}
				}

				if (inArray == true) {
					continue;
				}

				OptionGroup optionGroup = new OptionGroup();
				optionGroup.setTitle(itemOption.getOptionName1());
				optionGroups.add(optionGroup);
			}
		} else {	// S2, S3
			List<String> option1 = new ArrayList<>();
			List<String> option2 = new ArrayList<>();
			List<String> option3 = new ArrayList<>();

			for (ItemOption itemOption : this.itemOptions) {
				if (!"T".equals(itemOption.getOptionType())) {
					option1.add(itemOption.getOptionName1());
					option2.add(itemOption.getOptionName2());
					option3.add(itemOption.getOptionName3());
				}
			}

			// 중복제거
			HashSet<String> hs1 = new HashSet<>(option1);
			HashSet<String> hs2 = new HashSet<>(option2);
			HashSet<String> hs3 = new HashSet<>(option3);

			// ArrayList 형태로 다시 생성
			option1 = new ArrayList<>(hs1);
			option2 = new ArrayList<>(hs2);
			option3 = new ArrayList<>(hs3);

			OptionGroup optionGroup1 = new OptionGroup();
			optionGroup1.setTitle(this.itemOptionTitle1);
			optionGroup1.setOptions(option1);
			optionGroups.add(optionGroup1);

			OptionGroup optionGroup2 = new OptionGroup();
			optionGroup2.setTitle(this.itemOptionTitle2);
			optionGroup2.setOptions(option2);
			optionGroups.add(optionGroup2);

			if ("S3".equals(getItemOptionType())) {
				OptionGroup optionGroup3 = new OptionGroup();
				optionGroup3.setTitle(this.itemOptionTitle3);
				optionGroup3.setOptions(option3);
				optionGroups.add(optionGroup3);
			}
		}

		return optionGroups;
	}

	public List<ItemOption> getTextOptions() {
		List<ItemOption> textOptions = new ArrayList<>();

		if ("N".equals(getItemOptionFlag())) {
			return textOptions;
		}

		for (ItemOption itemOption : this.itemOptions) {
			if ("T".equals(itemOption.getOptionType())) {
				textOptions.add(itemOption);
			}
		}
		return textOptions;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getTempControl() {
		return tempControl;
	}

	public void setTempControl(String tempControl) {
		this.tempControl = tempControl;
	}

	public String getShipmentReturnType() {
		return shipmentReturnType;
	}

	public void setShipmentReturnType(String shipmentReturnType) {
		this.shipmentReturnType = shipmentReturnType;
	}

	public String[] getFilterGroups() {
		if (filterGroups == null) {
			return null;
		} else {
			int length = filterGroups.length;
			String[] array = new String[length];

			for(int i = 0 ; i < length ; i++) {
				array[i] = filterGroups[i];
			}
			return array;
		}
	}

	public void setFilterGroups(String[] filterGroups) {
		if (filterGroups == null) {
			this.filterGroups = null;
		} else {
			int length = filterGroups.length;
			this.filterGroups = new String[length];
			// private 배열의 값을 복사한 별도의 배열을 리턴
			for (int i = 0 ; i < length ; i++) {
				this.filterGroups[i] = filterGroups[i];
			}
		}
	}

	public Map<String, List<String>> getFilterCodes() {
		return filterCodes;
	}

	public void setFilterCodes(Map<String, List<String>> filterCodes) {
		this.filterCodes = filterCodes;
	}

	public String getNaverShoppingItemName() {
		if (!"Y".equals(this.naverShoppingFlag)) {
			return null;
		}
		return naverShoppingItemName;
	}

	public void setNaverShoppingItemName(String naverShoppingItemName) {
		this.naverShoppingItemName = naverShoppingItemName;
	}

	public String getNaverShoppingFlag() {
		return naverShoppingFlag;
	}

	public void setNaverShoppingFlag(String naverShoppingFlag) {
		this.naverShoppingFlag = naverShoppingFlag;
	}

	public String getNaverPayFlag() {
		return naverPayFlag;
	}

	public void setNaverPayFlag(String naverPayFlag) {
		this.naverPayFlag = naverPayFlag;
	}

	public int[] getSetItemIds() {
		if (setItemIds == null) {
			return null;
		} else {
			int length = setItemIds.length;
			int[] array = new int[length];
			for(int i = 0 ; i < length ; i++) {
				array[i] = setItemIds[i];
			}
			return array;
		}
	}

	public void setSetItemIds(int[] setItemIds) {
		if (setItemIds == null) {
			this.setItemIds = null;
		} else {
			int length = setItemIds.length;
			this.setItemIds = new int[length];
			// private 배열의 값을 복사한 별도의 배열을 리턴
			for (int i = 0 ; i < length ; i++) {
				this.setItemIds[i] = setItemIds[i];
			}
		}
	}

	public int[] getSetQuantities() {
		if (setQuantities == null) {
			return null;
		} else {
			int length = setQuantities.length;
			int[] array = new int[length];
			for (int i = 0 ; i < length ; i++) {
				array[i] = setQuantities[i];
			}

			return array;
		}
	}

	public void setSetQuantities(int[] setQuantities) {
		if (setQuantities == null) {
			this.setQuantities = null;
		} else {
			int length = setQuantities.length;
			this.setQuantities = new int[length];
			// private 배열의 값을 복사한 별도의 배열을 리턴
			for (int i = 0 ; i < length ; i++) {
				this.setQuantities[i] = setQuantities[i];
			}
		}
	}

	private boolean displayNaverPayFlag;

	public boolean isDisplayNaverPayFlag() {
		return displayNaverPayFlag;
	}

	public void setDisplayNaverPayFlag(boolean displayNaverPayFlag) {
		this.displayNaverPayFlag = displayNaverPayFlag;
	}

	public int[] getCopyItemImageIds() {
		if (copyItemImageIds == null) {
			return null;
		} else {
			int length = copyItemImageIds.length;
			int[] array = new int[length];
			for (int i = 0 ; i < length ; i++) {
				array[i] = copyItemImageIds[i];
			}
			return array;
		}
	}

	public void setCopyItemImageIds(int[] copyItemImageIds) {
		if (copyItemImageIds == null) {
			this.copyItemImageIds = null;
		} else {
			int length = copyItemImageIds.length;
			this.copyItemImageIds = new int[length];
			// private 배열의 값을 복사한 별도의 배열을 리턴
			for (int i = 0 ; i < length ; i++) {
				this.copyItemImageIds[i] = copyItemImageIds[i];
			}
		}
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public String getShippingTypeText() {
		if (StringUtils.hasText(this.shippingType)) {
			switch (this.shippingType) {
				case "1":
					return "무료배송";
				case "2":
					return "판매자조건부";
				case "3":
					return "출고지조건부";
				case "4":
					return "상품조건부";
				case "5":
					return "개당배송비";
				case "6":
					return "고정배송비";
			}
		}
		return "-";
	}

	public int getDisplaySupplyPrice() {
		int supplyPrice = getSalePrice();

		if ("3".equals(getItemType())) {
			return 0;
		}

		if ("1".equals(this.commissionType)
				&& this.seller != null && this.seller.getCommissionRate() > 0f) {
			supplyPrice = getSalePrice() - Math.round((float) getSalePrice() * (this.seller.getCommissionRate() / 100f));
		}
		if ("2".equals(this.commissionType) && this.commissionRate > 0f) {
			supplyPrice = getSalePrice() - Math.round((float) getSalePrice() * (this.commissionRate / 100f));
		}
		if ("3".equals(this.commissionType)) {
			supplyPrice = this.supplyPrice;
		}

		return supplyPrice;
	}

	public float getDisplayCommissionRate() {
		float commissionRate = this.commissionRate;

		if ("3".equals(getItemType())) {
			return 0f;
		}

		if ("1".equals(this.commissionType) && !ObjectUtils.isEmpty(this.seller)) {
			commissionRate = this.seller.getCommissionRate();
		}
		if ("3".equals(this.commissionType)) {
			commissionRate = ((float) (getSalePrice() - this.supplyPrice) / getSalePrice()) * 100f;
			commissionRate = Math.round(commissionRate * 10f) / 10f;
		}

		return commissionRate;
	}

	public boolean isWishlistFlag() {
		return wishlistFlag;
	}

	public void setWishlistFlag(boolean wishlistFlag) {
		this.wishlistFlag = wishlistFlag;
	}

	public String getRepresentativeItemYn() {
		return representativeItemYn;
	}

	public void setRepresentativeItemYn(String representativeItemYn) {
		this.representativeItemYn = representativeItemYn;
	}

//	public String getMobileItemYn() {
//		return mobileItemYn;
//	}
//
//	public void setMobileItemYn(String mobileItemYn) {
//		this.mobileItemYn = mobileItemYn;
//	}
//
//	public String getAdultItemYn() {
//		return adultItemYn;
//	}
//
//	public void setAdultItemYn(String adultItemYn) {
//		this.adultItemYn = adultItemYn;
//	}

	public List<Integer> getSeasonFoodMonthList() {
		return seasonFoodMonthList;
	}

	public void setSeasonFoodMonthList(List<Integer> seasonFoodMonthList) {
		this.seasonFoodMonthList = seasonFoodMonthList;
	}

	public String getCartOptions() {
		return cartOptions;
	}

	public void setCartOptions(String cartOptions) {
		this.cartOptions = cartOptions;
	}

	public String getCommunityBusinessYn() {
		return communityBusinessYn;
	}

	public void setCommunityBusinessYn(String communityBusinessYn) {
		this.communityBusinessYn = communityBusinessYn;
	}

}

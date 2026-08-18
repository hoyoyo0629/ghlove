package saleson.shop.item;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.binding.BindingException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.ArrayUtils;
import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.PoiUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.privacy.pCrypto;

import saleson.common.Const;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.SmsType;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.file.infra.FileStorage;
import saleson.common.file.support.ThumbUtils;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.ModelUtils;
import saleson.common.utils.PointUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.review.ItemReviewFilter;
import saleson.model.review.ItemReviewLike;
import saleson.seller.main.SellerMapper;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.seller.main.domain.SellerUser;
import saleson.shop.brand.BrandService;
import saleson.shop.brand.domain.Brand;
import saleson.shop.cardbenefits.CardBenefitsService;
import saleson.shop.categories.CategoriesMapper;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.categories.domain.BreadcrumbCategory;
import saleson.shop.categoriesfilter.CategoriesFilterService;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.ChosenItem;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.deliverycompany.DeliveryCompanyService;
import saleson.shop.deliverycompany.domain.DeliveryCompany;
import saleson.shop.giftitem.GiftItemService;
import saleson.shop.item.domain.BenefitInfo;
import saleson.shop.item.domain.CustomerInfo;
import saleson.shop.item.domain.ExcelItemCategory;
import saleson.shop.item.domain.ExcelItemCheck;
import saleson.shop.item.domain.ExcelItemKeyword;
import saleson.shop.item.domain.ExcelItemPointConfig;
import saleson.shop.item.domain.ExcelItemRelation;
import saleson.shop.item.domain.ExcelItemSub;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemCategory;
import saleson.shop.item.domain.ItemEarnPoint;
import saleson.shop.item.domain.ItemImage;
import saleson.shop.item.domain.ItemImageExplain;
import saleson.shop.item.domain.ItemIndex;
import saleson.shop.item.domain.ItemInfo;
import saleson.shop.item.domain.ItemNotice;
import saleson.shop.item.domain.ItemOption;
import saleson.shop.item.domain.ItemOther;
import saleson.shop.item.domain.ItemRelation;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.item.domain.ItemReviewCriteriaEncryptor;
import saleson.shop.item.domain.ItemReviewEncryptor;
import saleson.shop.item.domain.ItemReviewImage;
import saleson.shop.item.domain.ItemSaleEdit;
import saleson.shop.item.domain.ItemSet;
import saleson.shop.item.domain.SearchIndexParam;
import saleson.shop.item.domain.SeasonFoodItem;
import saleson.shop.item.support.AsyncReport;
import saleson.shop.item.support.ItemListParam;
import saleson.shop.item.support.ItemParam;
import saleson.shop.item.support.ItemReviewLikeDto;
import saleson.shop.item.support.ItemSaleEditParam;
import saleson.shop.item.support.ItemStockQuantityParam;
import saleson.shop.offgive.OffgiveMapper;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.point.PointMapper;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.Point;
import saleson.shop.point.domain.PointConfig;
import saleson.shop.point.domain.PointPolicy;
import saleson.shop.point.support.OrderPointParam;
import saleson.shop.qna.QnaService;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.reviewfilter.ItemReviewFilterRepository;
import saleson.shop.shipment.ShipmentService;
import saleson.shop.shipment.domain.Shipment;
import saleson.shop.shipment.support.ShipmentParam;
import saleson.shop.shipmentreturn.ShipmentReturnService;
import saleson.shop.shipmentreturn.domain.ShipmentReturn;
import saleson.shop.shipmentreturn.support.ShipmentReturnParam;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserMapper;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;
import saleson.shop.wishlist.WishlistService;
import saleson.shop.wishlist.domain.WishlistGroup;

@Service("itemService")
public abstract class ItemServiceImpl extends EgovAbstractServiceImpl implements ItemService {
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private OffgiveMapper offgiveMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private ItemMapperBatch itemMapperBatch;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private CategoriesMapper categoriesMapper;

    @Autowired
    private PointService pointService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentReturnService shipmentReturnService;

    @Autowired
    private DeliveryCompanyService deliveryCompanyService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GiftItemService giftItemService;

	@Autowired
	Environment environment;

	@Autowired
    CardBenefitsService cardBenefitsService;

    @Autowired
    QnaService qnaService;

    @Autowired
    CouponService couponService;

    @Autowired
    CategoriesFilterService categoriesFilterService;

    @Autowired
    ItemReviewLikeRepository itemReviewLikeRepository;

    @Autowired
    ItemReviewFilterRepository itemReviewFilterRepository;

    @Autowired
    ConfigPgService configPgService;

    @Autowired
    private ItemReviewEncryptor itemReviewEncryptor;

    @Autowired
    private ItemReviewCriteriaEncryptor itemReviewCriteriaEncryptor;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private SellerEncryptor sellerEncryptor;

	@Autowired
	private UserDetailEncryptor userDetailEncryptor;

	@Autowired
	private LocgovService locgovService;

    /**
     * 아이템 변경사항 로그 목록 조회
     */
    @Override
    public List<Item> getItemLogListById(ItemParam itemParam) {
        return itemMapper.getItemLogListById(itemParam);
    }

    /**
     * 아이템 변경사항 총 개수 조회
     */
    @Override
    public int getItemLogCountById(ItemParam itemParam) {
        return itemMapper.getItemLogCountById(itemParam);
    }

    @Override
    public void makeShoppingHowFile(String fileName) {

        ItemParam itemParam = new ItemParam();

        // 정렬조건은 CategoryId가 있는 경우에만 허용.
        if (itemParam.getOrderBy() != null && "ORDERING".equals(itemParam.getOrderBy())
            && (itemParam.getCategoryId() == null || "".equals(itemParam.getCategoryId()))) {
            itemParam.setOrderBy("");
            itemParam.setSort("DESC");
        }

        itemParam.setDataStatusCode("1");
        itemParam.setDisplayFlag("Y");
        itemParam.setConditionType("SHOPPING_HOW");
        List<Item> list = itemMapper.getItemList(itemParam);

        if (list == null) {
            return;
        }

        StringBuffer tsb = new StringBuffer();
        for(Item item : list) {
            if (item.getStockQuantity() != 0) {
                StringBuffer sb = new StringBuffer();
                this.shoppingHowTextTag(sb, "begin");
                this.shoppingHowTextTag(sb, "pid", item.getItemUserCode());
                this.shoppingHowTextTag(sb, "price", item.getPresentPrice());
                this.shoppingHowTextTag(sb, "pname", item.getItemName());

                String itemUrl = environment.getProperty("saleson.url.shoppingmall") + "/products/view/" + item.getItemUserCode();

                this.shoppingHowTextTag(sb, "pgurl", itemUrl);
                this.shoppingHowTextTag(sb, "igurl", environment.getProperty("saleson.url.shoppingmall") + item.getImageSrc());

                String imageUpdateDate = "";
                if (item.getItemImages() != null) {
                    for(ItemImage itemImage : item.getItemImages()) {
                        if (itemImage.getOrdering() == 1) {
                            imageUpdateDate = itemImage.getCreatedDate();
                            break;
                        }
                    }
                }

                this.shoppingHowTextTag(sb, "updateimg", imageUpdateDate);
                if (item.getItemCategories().isEmpty()) {
                    List<ItemCategory> categories = itemMapper.getItemCategoryListByItemId(item.getItemId());
                    item.setItemCategories(categories);
                }

                List<Breadcrumb> breadcrumbs = new ArrayList<>();
                if (!item.getItemCategories().isEmpty()) {
                    breadcrumbs = categoriesMapper.getBreadcrumbListByCollection(item.getItemCategories());
                }
                item.setBreadcrumbs(breadcrumbs);

                if (item.getBreadcrumbs().isEmpty()) {
                    continue;
                }

                Breadcrumb breadcrumb = item.getBreadcrumbs().get(0);
                if (breadcrumb.getBreadcrumbCategories() != null) {

                    String[] categoryElement = new String[]{"cate1", "cate2", "cate3", "cate4"};
                    int categoryIndex = 0;
                    String elementName = "";

                    elementName = categoryElement[categoryIndex];
                    this.shoppingHowTextTag(sb, elementName, breadcrumb.getTeamUrl());
                    categoryIndex++;

                    elementName = categoryElement[categoryIndex];
                    this.shoppingHowTextTag(sb, elementName, breadcrumb.getGroupUrl());
                    categoryIndex++;

                    for(BreadcrumbCategory category : breadcrumb.getBreadcrumbCategories()) {
                        if (categoryElement.length <= categoryIndex) {
                            break;
                        }

                        elementName = categoryElement[categoryIndex];
                        this.shoppingHowTextTag(sb, elementName, category.getCategoryUrl());

                        categoryIndex++;
                    }

                    String[] categoryNameElement = new String[]{"catename1", "catename2", "catename3", "catename4"};
                    int categoryNameIndex = 0;


                    elementName = categoryNameElement[categoryNameIndex];
                    this.shoppingHowTextTag(sb, elementName, breadcrumb.getTeamName());
                    categoryNameIndex++;

                    elementName = categoryNameElement[categoryNameIndex];
                    this.shoppingHowTextTag(sb, elementName, breadcrumb.getGroupName());
                    categoryNameIndex++;

                    for(BreadcrumbCategory category : breadcrumb.getBreadcrumbCategories()) {
                        if (categoryElement.length <= categoryNameIndex) {
                            break;
                        }

                        elementName = categoryNameElement[categoryNameIndex];
                        this.shoppingHowTextTag(sb, elementName, category.getCategoryName());

                        categoryNameIndex++;
                    }
                }

                this.shoppingHowTextTag(sb, "brand", item.getBrand());
                this.shoppingHowTextTag(sb, "maker"); // 제조사
                this.shoppingHowTextTag(sb, "pdate"); // 출시일

                this.shoppingHowTextTag(sb, "weight"); // 가중치 - 인기순위
                this.shoppingHowTextTag(sb, "sales"); // 누적 판매량
                this.shoppingHowTextTag(sb, "coupon"); // 쿠폰
                this.shoppingHowTextTag(sb, "pcard"); // 무이자/할부

                PointPolicy pointPolicy = pointService.getPointPolicyByItemId(item.getItemId());
                int point = 0;
                if (ValidationUtils.isNotNull(pointPolicy)) {

                    try {
                        point = ShopUtils.getEarnPoint(item.getPresentPrice(), pointPolicy);
                    } catch (RuntimeException e) {
                        log.error("[Exception] ShopUtils.getEarnPoint(item.getPresentPrice(), pointPolicy) : {}", "RuntimeException");
                        point = 0;
                    }

                }

                this.shoppingHowTextTag(sb, "point", point); // 적립금/포인트

                // [SKC임시]
				/*
				//DeliveryCharge deliveryCharge = deliveryMapper.getDeliveryChargeById(item.getDeliveryChargeId());
				// DeliveryCharge deliveryCharge = new DeliveryCharge();

				String deliveryType = "0";
				String deliveryPrice = "";
				if ("2".equals(deliveryCharge.getDeliveryChargeType())) {
					deliveryType = "1";
					deliveryPrice = StringUtils.numberFormat(deliveryCharge.getDeliveryCharge())+"원";
				} else if ("3".equals(deliveryCharge.getDeliveryChargeType())) {
					deliveryType = "2";
					deliveryPrice = StringUtils.numberFormat(deliveryCharge.getDeliveryFreeAmount())+"원 이하" + StringUtils.numberFormat(deliveryCharge.getDeliveryCharge())+"원";
				}
				*/
                String deliveryType = "0";
                String deliveryPrice = "";

                this.shoppingHowTextTag(sb, "deliv", deliveryType);
                this.shoppingHowTextTag(sb, "deliv2", deliveryPrice);

                this.shoppingHowTextTag(sb, "review", item.getReviewCount());
                this.shoppingHowTextTag(sb, "event");
                this.shoppingHowTextTag(sb, "eventurl");
                this.shoppingHowTextTag(sb, "sellername", "세븐뷰티");
                this.shoppingHowTextTag(sb, "sellershop", environment.getProperty("saleson.url.shoppingmall"));
                this.shoppingHowTextTag(sb, "sellergrade");		// 판매자 등급

                this.shoppingHowTextTag(sb, "end");

                tsb.append(sb);
            }
        }


        try (
        		FileOutputStream fos = new FileOutputStream(fileName);
        		OutputStreamWriter os = new OutputStreamWriter(fos, "UTF-8");
        		BufferedWriter bufferedWriter = new BufferedWriter(os);
        		) {
            bufferedWriter.write(tsb.toString());
        } catch (IOException e) {
            log.error("BufferedWriter Exception : {}", "IOException");
//        } finally {
//            if (bufferedWriter != null) {
//                try {
//                    bufferedWriter.close();
//                } catch (IOException e) {
//                    log.error("BufferedWriter Exception : {}", e.getMessage(), e);
//                }
//
//            }
        }
    }

    private void shoppingHowTextTag(StringBuffer sb, String tagName) {
        this.shoppingHowTextTag(sb, tagName, "");
    }

    private void shoppingHowTextTag(StringBuffer sb, String tagName, int value) {
        this.shoppingHowTextTag(sb, tagName, Integer.toString(value));
    }

    private void shoppingHowTextTag(StringBuffer sb, String tagName, String value) {
        sb.append("<<<"+tagName+">>>"+value + "\n");
    }

    @Override
    public Item getItemBy(int itemId) {
        return itemMapper.getItemById(itemId);
    }


    @Override
    public Item getItemBy(String itemUserCode) {
        return itemMapper.getItemByItemUserCode(itemUserCode);
    }



    @Override
    public Item getItemById(int itemId) {

        Item item = itemMapper.getItemById(itemId);

        bindItemAdditionInfo(item);
        return item;
    }


    @Override
    public Item getItemByIdForManager(int itemId) {
        Item item = getItemById(itemId);

        item.setItemOptions(itemMapper.getItemOptionListForManager(item.getItemId()));

        // 답례품 옵션정보 (관리자는 숨겨진 옵션정보까지 다 보여야함.)
        item.setItemOptionGroups(itemMapper.getItemOptionGroupListForManager(item.getItemId()));

        // 추가 답례품 목록
        item.setItemAdditions(itemMapper.getItemAdditionList(item.getItemId()));

        return item;
    }

    @Override
    public Item getItemByItemUserCodeForPreview(String itemUserCode) {
        Item item = itemMapper.getItemByItemUserCodeForPreview(itemUserCode);

        bindItemAdditionInfo(item);

        return item;
    }

    @Override
    public Item getItemByItemUserCode(String itemUserCode) {

        ItemParam itemParam = new ItemParam();
        if (UserUtils.isUserLogin()) {
            itemParam.setUserId(UserUtils.getUserId());
        }

        if (!UserUtils.isManagerLogin()) {
            // 전용답례품 조회
            itemParam.setPrivateTypes(ItemUtils.getPrivateTypes());
        }

        itemParam.setItemUserCode(itemUserCode);

        /**
         * CJH 2016.08.02 ITEM 조회시 회원의 할인율을 반영하기 위해 ItemParam으로 조회 변경
         * Item item = itemMapper.getItemByItemUserCode(itemUserCode);
         */

        Item item = itemMapper.getItemByParam(itemParam);

        bindItemAdditionInfo(item);

        return item;
    }

    // 답례품 추가 정보 조회
    private void bindItemAdditionInfo(Item item) {
        if (item == null) {
            throw new OpRuntimeException(MessageUtils.getMessage("M00378"));	// 답례품정보가 없습니다.
        }

        // Breadcrumbs
        if (item.getItemCategories() != null) {
            List<Breadcrumb> breadcrumbs = categoriesMapper.getBreadcrumbListByCollection(item.getItemCategories());
            item.setBreadcrumbs(breadcrumbs);
        }

        // 답례품 기본 정보
        item.setItemInfos(itemMapper.getItemInfoListByItemId(item.getItemId()));


        // 답례품 기본 정보
        item.setItemInfoMobiles(itemMapper.getItemInfoMobileListByItemId(item.getItemId()));

        // 답례품 옵션
        item.setItemOptions(itemMapper.getItemOptionList(item.getItemId()));

        // 답례품 옵션 이미지
        //item.setItemOptionImages(itemMapper.getItemOptionImageList(item.getItemId()));

        // 추가 구성품.
        //item.setItemAdditions(itemMapper.getItemAdditionList(item.getItemId()));

        // 속도를 위해 FRONT 는 비동기 처리
        if (UserUtils.isManagerLogin() || UserUtils.isSellerLogin()) {
            item.setItemRelations(getItemRelationsByItemId(item.getRelationItemDisplayType(),item.getItemId()));
        }

        // 사은품 정보
        if ("Y".equals(item.getFreeGiftFlag())) {
            try {
                item.setFreeGiftItemList(giftItemService.getGiftItemListForFront(item.getItemId()));
            } catch (RuntimeException e) {
                log.error("사은품 조회시 오류 :  {}", item.getItemUserCode());
            }
        }

        // 세트답례품 정보
        if ("3".equals(item.getItemType())) {
            List<ItemSet> itemSets = itemMapper.getItemSetListByItemId(item.getItemId());

            item.setItemSets(itemSets);
        }

        item.setDisplayNaverPayFlag(configPgService.isDisplayNaverPayFlag());

        // 관심 답례품목록에 포함 여부
        if (UserUtils.isUserLogin()) {
            item.setWishlistFlag(wishlistService.includedByItem(item.getItemId(), UserUtils.getUserId()));
        }

    }

    @Override
    public int getItemCountByItemUserCode(String itemUserCode) {
        return itemMapper.getItemCountByItemUserCode(itemUserCode);
    }

    @Override
    public int getItemCount(ItemParam itemParam) {

        // categoryGroupId가 있는 경우 조회 조건 추가.
        if (itemParam.getCategoryGroupId() > 0 && "".equals(itemParam.getCategoryClass())) {
            itemParam.setGroupCategoryClassCodes(categoriesMapper.getCategoryClassCodesByCategoryGroupId(itemParam.getCategoryGroupId()));
        }
        return itemMapper.getItemCount(itemParam);
    }

    @Override
    public List<Item> getItemList(ItemParam itemParam) {
        // categoryGroupId가 있는 경우 조회 조건 추가.
        if (itemParam.getCategoryGroupId() > 0 && "".equals(itemParam.getCategoryClass())) {
            itemParam.setGroupCategoryClassCodes(categoriesMapper.getCategoryClassCodesByCategoryGroupId(itemParam.getCategoryGroupId()));
        }

        List<Item> itemList = itemMapper.getItemList(itemParam);

        return itemList;
    }

    @Override
    public List<Item> getMainDisplayItemList(ItemParam itemParam) {

        int totalCount = itemMapper.getMainDisplayItemCountByParam(itemParam);

        if (itemParam.getItemsPerPage() == 10) {
            itemParam.setItemsPerPage(50);
        }

        Pagination pagination = Pagination.getInstance(totalCount, itemParam.getItemsPerPage());
        itemParam.setPagination(pagination);
        itemParam.setLanguage(CommonUtils.getLanguage());

        return itemMapper.getMainDisplayItemListByParam(itemParam);
    }

    @Override
    public void insertItem(Item item) {
        int itemId = sequenceService.getId("OP_ITEM");
        String itemCode = "G2" + StringUtils.lPad(Integer.toString(itemId), 9, '0');

        item.setItemId(itemId);
        item.setItemCode(itemCode);

        if (ShopUtils.isMallInMall()) {
            item.setItemUserCode(itemCode);
        }

        Seller seller = sellerMapper.getSellerById(item.getSellerId());

        String itemApprovalType = seller.getItemApprovalType();

        if (ShopUtils.isOpmanagerPage() && UserUtils.isManagerLogin()) {
//            item.setDataStatusCode("1");
//            item.setDataStatusMessage(ShopUtils.getItemStatusMessage("1", "운영자 등록 (자동승인)"));
//            item.setProcessPage("manager");
        	throw new UserException("답례품 관리자만 답례품 등록 가능합니다.");
        } else if (ShopUtils.isSellerPage() && SellerUtils.isSellerLogin()) {
            if ("2".equals(itemApprovalType)){
                item.setDataStatusCode("1");
                item.setDataStatusMessage(ShopUtils.getItemStatusMessage("1", "판매자 답례품 등록 신청. (자동승인)"));
                //접근성 관련 이미지 설명 insert
                if(!item.getItemImageExplain().isEmpty()) {
	                itemMapper.deleteItemImagesExplainByItemId(item.getItemId());
	                item.setUpdatedUserId(UserUtils.getUser().getUserId());
	                itemMapper.insertItemImagesExplain(item);
                }
            } else {
                item.setDataStatusCode("20");
                item.setDataStatusMessage(ShopUtils.getItemStatusMessage("20", "판매자 답례품 등록 신청. (승인대기)"));

                String locgovCode = locgovService.getLocgovCodeByOpId(item.getSellerId(), IdType.SELLER);

                List<Item> managers = itemMapper.getLocgovManagerList(locgovCode);	// 판매자 지자체에 해당하는 주관리자, 부관리자 목록
                //접근성 관련 이미지 설명 insert
                if(!item.getItemImageExplain().isEmpty()) {
                    itemMapper.deleteItemImagesExplainByItemId(item.getItemId());
                    item.setUpdatedUserId(UserUtils.getUser().getUserId());
                    itemMapper.insertItemImagesExplain(item);
                }
                for (Item manager : managers) {

                	// 국민비서 알림 전송 (상품등록 승인요청시)
            		try {
            			User user = userService.getUserByUserId(manager.getCreatedUserId());
            			UserDetail userDetail = (UserDetail) user.getUserDetail();
        				userDetail.decrypt(userDetailEncryptor, false);
            			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
	            			List<ReceiverInfo> receiverInfos = new ArrayList<>();
	            			ReceiverInfo receiverInfo = new ReceiverInfo();
	            			receiverInfo.setSmsType(SmsType.ITEM_APPROVAL_REGISTER);
	            			receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
	            			StringBuilder sb = new StringBuilder();
	            			sb.append(seller.getCompanyName());	// 상호명
	            			sb.append("|");
	            			sb.append(receiverInfo.getLocalDateTimeToStr());	// 승인요청일시
	        				sb.append("|");
	        				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
	            			receiverInfo.setSndngCntnts(sb.toString());
	            			receiverInfos.add(receiverInfo);
	            			smsIpsService.insertTifIpsSndngM(receiverInfos);
            			}
            		} catch (NullPointerException | ClassCastException e) {
            			log.error(getClass().getName() +  " :: insertItem send sms error", e);
            		}

                }
            }

            item.setProcessPage("seller");
        }

        item.setHits(0);
        item.setCreatedUserId(UserUtils.getManagerId());

        // 신답례품 공개일시 (신답례품이고 공개인 경우에만 공개일시가 등록됨. - 최초 1회)
        if ("2".equals(item.getItemLabel()) && "Y".equalsIgnoreCase(item.getDisplayFlag())) {
            item.setOpentime(DateUtils.getToday(Const.DATETIME_FORMAT));
        }

        // 신답례품 플래그
        item.setItemNewFlag("N");
        if ("2".equals(item.getItemLabel())) {
            item.setItemNewFlag("Y");
        }

        // 답례품 반품 신청 가능 여부
        if (ObjectUtils.isEmpty(item.getItemReturnFlag())) {
            item.setItemReturnFlag("Y");
        }

        // 20230307 추가
	  	item.setMobileItemYn(ObjectUtils.isEmpty(item.getMobileItemYn()) ? "N" : item.getMobileItemYn());	// 모바일상품여부
	  	item.setAdultItemYn(ObjectUtils.isEmpty(item.getAdultItemYn()) ? "N" : item.getAdultItemYn());	// 성인상품여부

        itemMapper.insertItem(item);

        item.setActionType("insert");
        saveItem(item);
    }

    @Override
    public void updateItem(Item item) {

        // 신답례품 공개일시 (신답례품이고 공개인 경우에만 공개일시가 등록됨. - 최초 1회)
        if (ValidationUtils.isEmpty(item.getOpentime())
            && "2".equals(item.getItemLabel()) && "".equalsIgnoreCase(item.getDisplayFlag())) {
            item.setOpentime(DateUtils.getToday(Const.DATETIME_FORMAT));
        }

        // 신답례품 플래그
        item.setItemNewFlag("N");
        if ("2".equals(item.getItemLabel())) {
            item.setItemNewFlag("Y");
        }

        Seller seller = sellerMapper.getSellerById(item.getSellerId());
        String itemApprovalType = seller.getItemApprovalType();

        if (ShopUtils.isOpmanagerPage() && UserUtils.isManagerLogin()) {
            //item.setDataStatusMessage(item.getDataStatusMessage() + ShopUtils.getItemStatusMessage("1000", "운영자 수정"));
            //item.setProcessPage("manager");
        	throw new UserException("답례품 관리자만 답례품 수정 가능합니다.");

        } else if (ShopUtils.isSellerPage() && SellerUtils.isSellerLogin()) {
            // 판매관리자일 경우 자동등록 설정이 되어있는지 여부만 체크(itemApprovalType)
            if("2".equals(itemApprovalType)){
                item.setDataStatusCode("1");
                item.setDataStatusMessage(ShopUtils.getItemStatusMessage("1", "판매자 답례품 수정 신청. (자동승인)"));
            }else{

                if (!"20".equals(item.getDataStatusCode())) {
                    item.setDataStatusCode("30");
                    item.setDataStatusMessage(item.getDataStatusMessage() + ShopUtils.getItemStatusMessage("30", "판매자 답례품 수정 신청."));

                    String locgovCode = locgovService.getLocgovCodeByOpId(item.getSellerId(), IdType.SELLER);

                    List<Item> managers = itemMapper.getLocgovManagerList(locgovCode);	// 판매자 지자체에 해당하는 주관리자, 부관리자 목록

                    //접근성 관련 이미지 설명 insert
                    if(!item.getItemImageExplain().isEmpty()) {
	                    itemMapper.deleteItemImagesExplainByItemId(item.getItemId());
	                    item.setUpdatedUserId(UserUtils.getUser().getUserId());
	                    itemMapper.insertItemImagesExplain(item);
                    }

                    for (Item manager : managers) {

                    	// 국민비서 알림 전송 (상품등록 승인요청시)
                		try {
                			User user = userService.getUserByUserId(manager.getCreatedUserId());
                			UserDetail userDetail = (UserDetail) user.getUserDetail();
            				userDetail.decrypt(userDetailEncryptor, false);
                			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
	                			List<ReceiverInfo> receiverInfos = new ArrayList<>();
	                			ReceiverInfo receiverInfo = new ReceiverInfo();
	                			receiverInfo.setSmsType(SmsType.ITEM_APPROVAL_REGISTER);
	                			receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
	                			StringBuilder sb = new StringBuilder();
	                			sb.append(seller.getCompanyName());	// 상호명
	                			sb.append("|");
	                			sb.append(receiverInfo.getLocalDateTimeToStr());	// 승인요청일시
	            				sb.append("|");
	            				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
	                			receiverInfo.setSndngCntnts(sb.toString());
	                			receiverInfos.add(receiverInfo);
	                			smsIpsService.insertTifIpsSndngM(receiverInfos);
                			}
                		} catch (NullPointerException | ClassCastException e) {
                			log.error(getClass().getName() +  " :: insertItem send sms error", e);
                		}

                    }
                }
            }

            item.setProcessPage("seller");
        }

        item.setActionType("update");
        // 20230405 추가
	  	item.setMobileItemYn(ObjectUtils.isEmpty(item.getMobileItemYn()) ? "N" : item.getMobileItemYn());	// 모바일상품여부
	  	item.setAdultItemYn(ObjectUtils.isEmpty(item.getAdultItemYn()) ? "N" : item.getAdultItemYn());	// 성인상품여부
        saveItem(item);

    }

    @Override
    public List<Item> getMainDisplayItemListForMain(String templateId, String team, int limit) {

        ItemParam itemParam = new ItemParam();
        itemParam.setTemplateId(templateId);
        itemParam.setTeam(team);

        if (limit > 0) {
            itemParam.setLimit(limit);
        }

        return itemMapper.getMainDisplayItemListByParam(itemParam);
    }

    /**
     * 답례품 등록 및 수정 처리를 한다.
     * 파일, 관련답례품, 답례품옵션 기타 등등
     * @param item
     */
    private void saveItem(Item item) {

    	if (item == null) {
    		throw new NullPointerException();
    	}

    	// 답례품 품절로 변경시 장바구니 삭제
    	if ("1".equals(item.getSoldOut())) {
    		itemMapper.deleteCartBySoldOut(item);
    	}
    	// 답례품 옵션중 사용자가 선택한 옵션이 품절되면 장바구니 삭제
    	for (int opt = 0; opt < saleson.common.utils.CommonUtils.dataAryNvl(item.getOptionId()).length; opt++) {
       		if("Y".equals(item.getOptionSoldOutFlag()[opt])) {
       			item.setCartOptions(item.getOptionName2()[opt]);
       			itemMapper.deleteCartBySoldOut(item);
       		}
       	}

        // 공급가 설정
        if (item.getSupplyPrice() > item.getSalePrice()) {
            throw new UserException("공급가는 판매금액보다 높게 설정할 수 없습니다.\\n판매금액 및 공급가를 확인해 주세요.");
        }

        if ("1".equals(item.getCommissionType())) {         // 입점업체 수수료로 설정
            item.setCommissionRate(0);
            item.setSupplyPrice(0);
        } else if ("2".equals(item.getCommissionType())) {  // 답례품별 수수료로 설정
            item.setSupplyPrice(0);
        } else if ("3".equals(item.getCommissionType())) {  // 공급가 설정
            item.setCommissionRate(0);
        }

        // 처리자 정보
        item.setCreatedManagerId(UserUtils.getManagerId());
        item.setCreatedSellerId(SellerUtils.getSellerId());

        // 공통 - 배송비
        if ("2".equals(item.getShippingType())) {
            item.setShippingGroupCode("SELLER-" + item.getSellerId());
        } else if ("3".equals(item.getShippingType())) {
            item.setShippingGroupCode("SHIPMENT-" + item.getShipmentId());
        } else if ("4".equals(item.getShippingType())) {
            item.setShippingGroupCode("ITEM-" + item.getItemId());
        } else if ("5".equals(item.getShippingType())) {
            item.setShippingGroupCode("ITEM-PER-" + item.getItemId());
        } else {
            item.setShippingGroupCode("");
        }

        // 배송비 기준이 출고지조건부가 아닌 경우 shipmentGroupCode 초기화
        if (!"3".equals(item.getShippingType())) {
            item.setShipmentGroupCode("");
        } else if (ObjectUtils.isEmpty(item.getShipmentGroupCode())) {
            ShipmentParam shipmentParam = new ShipmentParam();
            shipmentParam.setShipmentId(item.getShipmentId());
            Shipment shipment = shipmentService.getShipmentByParam(shipmentParam);

            if (shipment != null && !ObjectUtils.isEmpty(shipment.getShipmentGroupCode())) {
                item.setShipmentGroupCode(shipment.getShipmentGroupCode());
            }
        }

        // 몰인몰(입점) 형태이면 itemUserCode는 판매자가 직접 선택할 수 없음.
        if (ShopUtils.isMallInMall()) {
            item.setItemUserCode(item.getItemCode());
        }

        // 답례품 반품 신청 가능 여부
        if (ObjectUtils.isEmpty(item.getItemReturnFlag())) {
            item.setItemReturnFlag("Y");
        }

        // 1. 답례품 카테고리
        if (item.getCategoryIds() != null) {
            // 1.1 기존 답례품 카테고리 삭제
            itemMapper.deleteItemCategoryByItemId(item.getItemId());

            // 1.2 답례품 카테고리 등록
            for (int i = 0; i < item.getCategoryIds().length; i++) {
                ItemCategory itemCategory = new ItemCategory();

                itemCategory.setItemCategoryId(sequenceService.getId("OP_ITEM_CATEGORY"));
                itemCategory.setItemId(item.getItemId());
                itemCategory.setCategoryId(item.getCategoryIds()[i]);
                itemCategory.setOrdering(i + 1);

                itemMapper.insertItemCategory(itemCategory);
            }
        }

        // 2. 복사-답례품 이미지 복사 || 수정-답례품 이미지 순서 변경
        boolean hasItemImage = false;
        if (item.getCopyItemImageIds() != null) {
            List<ItemImage> copyItemImages = itemMapper.getItemImageListByCopyIds(item.getCopyItemImageIds());

            if (copyItemImages != null) {
                String date = DateUtils.getToday(Const.DATETIME_FORMAT);
                String uploadPath = item.getUploadPath() + File.separator;
                fileService.makeUploadPath(uploadPath + item.getItemUserCode());

                int ordering = 1;
                for (ItemImage copyItemImage : copyItemImages) {
                    if (!ObjectUtils.isEmpty(copyItemImage.getImageName())) {
                        String saveFileName = "";

                        // 답례품 이미지 복사 :: 마지막 섬네일 타입만 DB에 저장되므로, 해당 이미지의 모든 섬네일 타입을 읽어와서 복사하여야 함
                        String[] thumbnailTypes = ShopUtils.getThumbnailType();
                        for (String sizeName : thumbnailTypes) {
                            try {
                                String defaultFileName = date + "_" + sizeName + "." + FileUtils.getExtension(copyItemImage.getImageName());
                                String fileName = FileUtils.getNewFileName(uploadPath + item.getItemUserCode(), defaultFileName);

                                File file = new File(uploadPath + copyItemImage.getItemUserCode() + File.separator + copyItemImage.getImageName().replaceAll(thumbnailTypes[thumbnailTypes.length - 1], sizeName));
                                File saveFile = new File(uploadPath + item.getItemUserCode() + File.separator + fileName);

                                fileStorage.upload(file, saveFile);

                                saveFileName = fileName;
                            } catch (IOException e) {
//                                log.error("답례품 이미지 복사 오류 : {}", e.getMessage());
                                log.error("답례품 이미지 복사 오류 : {}", getClass().getName() + " :: saveItem IOException ===========");
                                continue;
                            }
                        }

                        if (!ObjectUtils.isEmpty(saveFileName)) {
                            // 첫 번째 이미지 목록에 출력
                            if (ordering == 1) {
                                item.setItemImage(saveFileName);
                                hasItemImage = true;
                            }

                            ItemImage itemImage = new ItemImage();

                            itemImage.setItemImageId(sequenceService.getId("OP_ITEM_IMAGE"));
                            itemImage.setItemId(item.getItemId());
                            itemImage.setImageName(saveFileName);
                            itemImage.setOrdering(ordering);

                            itemMapper.insertItemImage(itemImage);

                            ordering++;
                        }
                    }
                }
            }
        } else if (item.getItemImageIds() != null) {
            int i = 1;
            for (int itemImageId : item.getItemImageIds()) {
                if (i == 1) {
                    // 첫 번째 이미지가 목록에 출력
                    ItemImage firstItemImage = itemMapper.getItemImageById(itemImageId);
                    item.setItemImage(firstItemImage.getImageName());
                }
                ItemImage itemImage = new ItemImage(itemImageId, i);
                itemMapper.updateOrderingOfItemImage(itemImage);
                i++;
            }
            hasItemImage = true;
        }

        // 3. 답례품상세이미지
        if (item.getItemDetailImageFiles() != null) {

            // ordering 조회
            int ordering = itemMapper.getMaxOrderingOfItemImageByItemId(item.getItemId());
            int uploadFileCount = 0;

            StringBuilder uploadPath = new StringBuilder();

            uploadPath
                    .append(item.getUploadPath())
                    .append(File.separator)
                    .append(item.getItemUserCode());

            fileService.makeUploadPath(uploadPath.toString());

            for (MultipartFile multipartFile : item.getItemDetailImageFiles()) {
                if (multipartFile != null && multipartFile.getSize() > 0) {

                    StringBuilder saveFileName = new StringBuilder();
                    BufferedImage bufferedImage = null;
                    String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);

                    // 이미지를 사이즈별로 저장[2017-05-31]minae.yun
                    try {
                    	if (ShopUtils.getThumbnailType() == null) {
                    		throw new IOException("썸네일 타입 없음");
                    	}
                        for (String sizeName : ShopUtils.getThumbnailType()) {
                            try (InputStream is = multipartFile.getInputStream();) {
                                bufferedImage = ImageIO.read(is);
                                if (bufferedImage == null) {
                                    throw new IOException("");
                                }
                            } catch (IOException e1) {
                                log.error("이미지 읽어오기 실패 : {}", getClass().getName() + " :: saveItem IOException1 ==========", e1);
                                throw new UserException("이미지 읽어오기 실패");
                            }

                            // 2. 파일명
                            saveFileName.setLength(0);
                            saveFileName.append(dateNanoTime);
                            saveFileName.append("_").append(sizeName).append(".").append(FileUtils.getExtension(multipartFile.getOriginalFilename()));

                            // 3. 저장될 파일
                            File saveFile = new File(new StringBuilder(uploadPath).append(File.separator).append(saveFileName).toString());
                            int imageSize = ShopUtils.getImageSize(sizeName);

                            // 이상우 [2017-03-31 수정] 이미지의 가로 세로 중 작은 쪽이 imageSize보다 크면 true, imageSize+10하면 사이즈 비율에 맞게 조절
                            if (ShopUtils.checkImageSize2(bufferedImage.getWidth(), bufferedImage.getHeight(), imageSize)) {
                                try {
                                    bufferedImage = ShopUtils.getThumbnailImage(bufferedImage, imageSize);
                                } catch (IOException e) {
                                    log.error("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException2 ==========", e);
                                    throw new UserException("썸네일 생성중 오류");
                                }
                            }
                            try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
	                            output.flush();
	                            ImageIO.write(bufferedImage, FileUtils.getExtension(multipartFile.getOriginalFilename()), output);
//	                            output.close();
	                            fileStorage.upload(output.toByteArray(), saveFile);
                            } catch (IOException e) {
                                log.error("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException3 ==========", e);
                            	throw new IOException(e);
                            }
                        }
                    } catch (IOException e) {
                        log.error("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException4 ==========", e);
                        throw new UserException("썸네일 생성중 오류");
                    }

                    ItemImage itemImage = new ItemImage();

                    itemImage.setItemImageId(sequenceService.getId("OP_ITEM_IMAGE"));
                    itemImage.setItemId(item.getItemId());
                    itemImage.setImageName(saveFileName.toString());
                    itemImage.setOrdering(ordering);

                    itemMapper.insertItemImage(itemImage);

                    // 첫 번째 이미지가 목록에 출력
                    if (!hasItemImage && uploadFileCount == 0) {
                        item.setItemImage(saveFileName.toString());
                    }

                    uploadFileCount++;
                    ordering++;
                }
            }
        }

        // 4. 적립금 설정
        if ("Y".equals(item.getSellerPointFlag())) {
            pointMapper.deletePointConfigByItemId(item.getItemId());

            if (item.getPointType() != null) {
                for (int i = 0; i < item.getPointType().length; i++) {
                    String periodType = ArrayUtils.get(item.getPointRepeatDay(), i);
                    if (periodType == null || "".equals(periodType.trim())) {
                        periodType = "1";
                    } else {
                        periodType = "2";
                    }

                    PointConfig pointConfig = new PointConfig();

                    pointConfig.setPointConfigId(sequenceService.getId("OP_POINT_CONFIG"));

                    pointConfig.setConfigType("2");
                    pointConfig.setPeriodType(periodType);
                    pointConfig.setPointType(ArrayUtils.get(item.getPointType(), i));
                    pointConfig.setPoint(item.getPoint()[i]);
                    pointConfig.setStartDate(ArrayUtils.get(item.getPointStartDate(), i));
                    pointConfig.setStartTime(ArrayUtils.get(item.getPointStartTime(), i));
                    pointConfig.setEndDate(ArrayUtils.get(item.getPointEndDate(), i));
                    pointConfig.setEndTime(ArrayUtils.get(item.getPointEndTime(),i));
                    pointConfig.setRepeatDay(ArrayUtils.get(item.getPointRepeatDay(), i));
                    pointConfig.setItemId(item.getItemId());
                    pointConfig.setStatusCode("1");
                    pointConfig.setCreatedUserId(UserUtils.getManagerId());

                    pointMapper.insertPointConfig(pointConfig);
                }
            }
        } else {
            pointMapper.deletePointConfigByItemId(item.getItemId());
        }

        // 5.답례품옵션
		/*if ("N".equalsIgnoreCase(item.getItemOptionFlag())) {
			// 사용안함 상태일때 옵션이 남는다..
			itemMapper.deleteItemOptionByItemId(item.getItemId());
		}*/

        // 5.1 답례품옵션 삭제
        itemMapper.deleteItemOptionByItemId(item.getItemId());

        if (item.getItemOptionFlag() != null && "Y".equals(item.getItemOptionFlag().toUpperCase()) && item.getOptionName1() != null) {

            // 5.2 답례품옵션 등록
            for (int i = 0; i < item.getOptionName1().length; i++) {
                String optionType = ArrayUtils.get(item.getOptionType(), i);
                String optionName1 = ArrayUtils.get(item.getOptionName1(), i);

                // 옵션형태나 옵션명이 없는 경우
                if (ObjectUtils.isEmpty(optionType) || ObjectUtils.isEmpty(optionName1.trim())) {
                    continue;
                }

                String optionId = ArrayUtils.get(item.getOptionId(), i);
                int itemOptionId = "".equals(optionId) || "0".equals(optionId) ? sequenceService.getId("OP_ITEM_OPTION") : Integer.parseInt(optionId);


                ItemOption itemOption = new ItemOption();
                itemOption.setItemOptionId(itemOptionId);
                itemOption.setItemId(item.getItemId());
                itemOption.setOptionType(optionType);


                itemOption.setOptionDisplayType("T".equals(optionType) ? "text" : "select");
                itemOption.setOptionName1(optionName1);
                itemOption.setOptionName2(ArrayUtils.get(item.getOptionName2(), i));
                itemOption.setOptionName3(ArrayUtils.get(item.getOptionName3(), i));
                itemOption.setOptionStockCode(ArrayUtils.get(item.getOptionStockCode(), i));

                if ("".equals(ArrayUtils.get(item.getOptionCostPrice(), i))) {
                    itemOption.setOptionCostPrice(0);
                } else {
                    itemOption.setOptionCostPrice(Integer.parseInt(ArrayUtils.get(item.getOptionCostPrice(), i)));
                }

                if ("".equals(ArrayUtils.get(item.getOptionPrice(), i))) {
                    itemOption.setOptionPrice(0);
                } else {
                    itemOption.setOptionPrice(Integer.parseInt(ArrayUtils.get(item.getOptionPrice(), i)));
                }


                String optionStockFlag = ArrayUtils.get(item.getOptionStockFlag(), i);
                itemOption.setOptionStockFlag(optionStockFlag);

                if ("Y".equals(optionStockFlag)) {
                    itemOption.setOptionStockQuantity(Integer.parseInt(ArrayUtils.get(item.getOptionStockQuantity(),i)));

                } else {
                    itemOption.setOptionStockQuantity(-1);

                }

                itemOption.setOptionSoldOutFlag(ArrayUtils.get(item.getOptionSoldOutFlag(), i));
                itemOption.setOptionDisplayFlag(ArrayUtils.get(item.getOptionDisplayFlag(), i));

                itemOption.setCreatedUserId(UserUtils.getManagerId());

                // 사용안함.
                itemOption.setOptionPriceNonmember(0);
                itemOption.setOptionHideFlag("N");
                itemOption.setOptionStockScheduleDate("");
                itemOption.setOptionStockScheduleText("");


                if (StringUtils.isNotEmpty(itemOption.getOptionType())) {
                    itemMapper.insertItemOption(itemOption);
                }
            }

            // 5.3. 답례품 옵션 - 텍스트일 경우, 세트답례품 마스터에서 제거
            if (item.getItemId() > 0 && "T".equals(item.getItemOptionType())) {
                itemMapper.deleteItemSetByItemId(item.getItemId());
            }
        }

        // 6. 추가 구성 답례품
        if ("N".equalsIgnoreCase(item.getItemAdditionFlag())) {
            // 6.1 추가구성 답례품 삭제
            itemMapper.deleteAdditionItemByItemId(item.getItemId());	// 추가구성 답례품 삭제 	(OP_ITEM)
            itemMapper.deleteItemAdditionByItemId(item.getItemId());	// 추가구성 답례품 정보 삭제 (OP_ITEM_ADDITION) - 답례품 삭제 시 같이 삭제 (forign Key) 주석.
        } else if ("Y".equalsIgnoreCase(item.getItemAdditionFlag())) {

            // 6.1 추가구성 답례품 삭제
            itemMapper.deleteAdditionItemByItemId(item.getItemId());	// 추가구성 답례품 삭제 	(OP_ITEM)
            itemMapper.deleteItemAdditionByItemId(item.getItemId());	// 추가구성 답례품 정보 삭제 (OP_ITEM_ADDITION) - 답례품 삭제 시 같이 삭제 (forign Key) 주석.

            // 6.2 추가 구성 답례품 등록

            if (item.getAdditionItemName() != null && item.getAdditionItemName().length > 0) {
                for (int i = 0; i < item.getAdditionItemName().length; i++) {
                    String optionType = ArrayUtils.get(item.getOptionType(), i);
                    String optionName1 = ArrayUtils.get(item.getOptionName1(), i);

                    // 텍스트형 옵션인데 옵션명이 없는 경우..
                    if ("T".equals(optionType) && "".equals(optionName1.trim())) {
                        continue;
                    }

                    String additionItemId = ArrayUtils.get(item.getAdditionItemId(), i);
                    int itemAdditionId = "".equals(additionItemId) || "0".equals(additionItemId) ? sequenceService.getId("OP_ITEM") : Integer.parseInt(additionItemId);
                    String additionItemCode = "A1" + StringUtils.lPad(Integer.toString(itemAdditionId), 9, '0');
                    String additionItemName = ArrayUtils.get(item.getAdditionItemName(), i);
                    String additionSalePrice = ArrayUtils.get(item.getAdditionSalePrice(), i);
                    String additionCostPrice = ArrayUtils.get(item.getAdditionCostPrice(), i);
                    String additionStockFlag = ArrayUtils.get(item.getAdditionStockFlag(), i);
                    String additionStockQuantity = ArrayUtils.get(item.getAdditionStockQuantity(), i);
                    String additionStockCode = ArrayUtils.get(item.getAdditionStockCode(), i);
                    String additionSoldOut = ArrayUtils.get(item.getAdditionSoldOut(), i);
                    String additionTaxType = ArrayUtils.get(item.getAdditionTaxType(), i);
                    String additionDisplayFlag = ArrayUtils.get(item.getAdditionDisplayFlag(), i);
                    String additionWeight = ArrayUtils.get(item.getAdditionWeight(), i);



                    Item additionItem = new Item();
                    additionItem.setItemDataType("2"); 	// 추가구성 답례품 타입.
                    additionItem.setSellerId(SellerUtils.getSellerId());
                    additionItem.setItemId(itemAdditionId);
                    additionItem.setItemCode(additionItemCode);
                    additionItem.setItemUserCode(additionItemCode);

                    additionItem.setShipmentId(item.getShipmentId());
                    additionItem.setShipmentReturnId(item.getShipmentReturnId());
                    additionItem.setShippingType("1");
                    additionItem.setShippingGroupCode("");
                    additionItem.setShipping(0);
                    additionItem.setShippingFreeAmount(0);
                    additionItem.setCommissionType(item.getCommissionType());
                    additionItem.setCommissionRate(item.getCommissionRate());

                    additionItem.setItemName(additionItemName);
                    additionItem.setSalePrice("".equals(additionSalePrice) ? 0 : Integer.parseInt(additionSalePrice));
                    additionItem.setCostPrice("".equals(additionCostPrice) ? 0 : Integer.parseInt(additionCostPrice));
                    additionItem.setStockFlag(additionStockFlag);
                    additionItem.setStockQuantity("".equals(additionStockQuantity) ? -1 : Integer.parseInt(additionStockQuantity));
                    additionItem.setStockCode(additionStockCode);
                    additionItem.setSoldOut(additionSoldOut);
                    additionItem.setTaxType(additionTaxType);
                    additionItem.setDisplayFlag(additionDisplayFlag);
                    additionItem.setWeight(additionWeight);
                    additionItem.setDataStatusCode("1");

                    // 추가구성 답례품 등록.
                    itemMapper.insertAddtionItem(additionItem);


                    // 추가구성 답례품 정보 등록.
                    HashMap<String, Integer> itemAddition = new HashMap<>();
                    itemAddition.put("itemId", item.getItemId());
                    itemAddition.put("additionItemId", additionItem.getItemId());

                    itemMapper.insertItemAddtion(itemAddition);

                }
            }
        } else {
            // 사용안함 상태일때 옵션이 남는다..
            itemMapper.deleteItemAdditionByItemId(item.getItemId());
        }

        // 6. 관련답례품 등록
        // 6.1. 관련답례품 삭제
        itemMapper.deleteItemRelationByItemId(item.getItemId());

        if ("2".equals(item.getRelationItemDisplayType()) && item.getRelatedItemIds() != null) {


            // 6.2 관련답례품 등록
            int ordering = 1;
            for (int relatedItemId : item.getRelatedItemIds()) {
                int itemRelationId = sequenceService.getId("OP_ITEM_RELATION");

                ItemRelation itemRelation = new ItemRelation();

                itemRelation.setItemId(item.getItemId());
                itemRelation.setItemRelationId(itemRelationId);
                itemRelation.setRelatedItemId(relatedItemId);
                itemRelation.setOrdering(ordering);

                itemMapper.insertItemRelation(itemRelation);

                ordering++;
            }
        }

        // 7. 답례품 상세 정보
        if (item.getItemInfoTitles() != null) {
            // 7.1 답례품 상세 정보 삭제
            itemMapperBatch.deleteItemInfoByItemId(item.getItemId());

            // 7.2 답례품옵션 등록
            List<ItemInfo> itemInfos = new ArrayList<>();

            for (int i = 0; i < item.getItemInfoTitles().length; i++) {
                String title = ArrayUtils.get(item.getItemInfoTitles(), i);
                String description = ArrayUtils.get(item.getItemInfoDescriptions(), i);

                if ("".equals(title)) {
                    continue;
                }

                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setItemId(item.getItemId());
                itemInfo.setItemInfoId(sequenceService.getId("OP_ITEM_INFO"));
                itemInfo.setInfoCode("ITEM_0001");
                itemInfo.setTitle(title);
                itemInfo.setDescription(description);

                itemInfos.add(itemInfo);
            }

            if (!itemInfos.isEmpty()) {
                //itemMapperBatch.insertItemInfoListForExcel(itemInfos);
                for (ItemInfo itemInfo : itemInfos) {
                    itemMapperBatch.insertItemInfo(itemInfo);
                }
            }
        }

        // 8. 사은품 정보
        try {

            giftItemService.deleteGiftItemRelation(item.getItemId());

            if ("Y".equals(item.getFreeGiftFlag())) {

                List<Long> freeGiftItemIds = ModelUtils.getIds(item.getFreeGiftItemIds());

                if (freeGiftItemIds != null && !freeGiftItemIds.isEmpty()) {
                    giftItemService.insertGiftItemRelation(item.getItemId(), freeGiftItemIds);
                }
            }
        } catch (RuntimeException e) {
//            log.error("사은품 정보 처리시 오류 :  {}", e.getMessage(), e);
            log.error("사은품 정보 처리시 오류 :  {}", getClass().getName() + " :: saveItem RuntimeException1 ============");
        }

        // 9. 대표이미지 업데이트. (상세이미지 중 첫번째가 답례품의 대표 이미지가 됨)
        itemMapper.updateItemImage(item.getItemId());

        // 10. 카테고리 필터 매핑
        try {
            categoriesFilterService.saveItemFilter(item.getItemId(), item.getFilterCodes());
        } catch (RuntimeException e) {
//            log.error("답례품 카테고리 매핑 오류 :  {}", e.getMessage(), e);
            log.error("사답례품 카테고리 매핑 오류 :  {}", getClass().getName() + " :: saveItem RuntimeException2 ============");
            throw new UserException("답례품 카테고리 매핑에 실패하였습니다.");
        }

        // 11. 세트답례품 등록
        if ("3".equals(item.getItemType())) {
            try {
                if (item.getSetItemIds() == null) {
                    throw new UserException("세트답례품을 추가해주세요.");
                }

                // 11.1. 세트답례품 마스터 삭제
                itemMapper.deleteItemSetByParentItemId(item.getItemId());

                // 11.2. 세트답례품 조건 검사
                ItemParam itemParam = new ItemParam();
                itemParam.setConditionType("FIND_ITEM_FOR_SET");
                itemParam.setSaleStatus("sale");
                itemParam.setItemIds(item.getSetItemIds());

                // 추가한 답례품 조회
                List<Item> addItems = itemMapper.getItemList(itemParam);
                List<ItemSet> itemSets = new ArrayList<>();

                for (int i = 0; i < item.getSetItemIds().length; i++) {
                    int itemId = ArrayUtils.get(item.getSetItemIds(), i);
                    int quantity = ArrayUtils.get(item.getSetQuantities(), i);

                    for (Item addItem : addItems) {
                        if (itemId == addItem.getItemId()) {
                            // int orderMinQuantity = addItem.getOrderMinQuantity() > 0 ? addItem.getOrderMinQuantity() : 1;
                            // int orderMaxQuantity = addItem.getOrderMaxQuantity();

                            // 최소, 최대 주문 수량 체크
                            /*if (quantity < orderMinQuantity || (orderMaxQuantity > 0 && quantity > orderMaxQuantity)) {
                                throw new UserException(ShopUtils.unescapeHtml(addItem.getItemName()) + " 답례품 최소, 최대 주문 수량 <-> 구매 수량 조건 불일치");
                            }*/

                            ItemSet itemSet = new ItemSet();
                            itemSet.setItemId(itemId);
                            itemSet.setQuantity(quantity);
                            itemSet.setOrdering(i);

                            itemSets.add(itemSet);
                            break;
                        }
                    }
                }

                if (itemSets != null && !itemSets.isEmpty()) {
                    item.setItemSets(itemSets);
                    itemMapper.insertItemSet(item);
                }
            } catch (OpRuntimeException e) {
                log.error("세트답례품 등록 오류 : {}", "OpRuntimeException");
                throw new UserException("세트답례품 등록에 실패하였습니다. (" + "OpRuntimeException" + ")");
            }
        }

		// 12. 제철 식품관 상품 등록 20230306 (삭제 후 등록)
		itemMapper.deleteSeasonFoodItem(item.getItemId());

		SeasonFoodItem seasonFoodItem = new SeasonFoodItem();
		seasonFoodItem.setItemId(item.getItemId());
		seasonFoodItem.setFrstRegisterId(UserUtils.getManagerId());
		for (int month : item.getSeasonFoodMonthList()) {
			seasonFoodItem.setSeasonFoodMonth(month);
			itemMapper.insertSeasonFoodItem(seasonFoodItem);
		}

        itemMapper.updateItem(item);

        if ("insert".equals(item.getActionType())) {
            itemMapper.insertItemLog(item);
        } else if ("update".equals(item.getActionType())) {
            itemMapper.insertItemLogForApproval(item);
        }
    }



    @Override
    public void insertItemOption(ItemOption itemOption) {
        itemMapper.insertItemOption(itemOption);

    }

    @Override
    public void updateItemOrdering(ItemListParam itemListParam) {



        if (itemListParam.getId() != null && itemListParam.getCategoryId() > 0) {

            itemMapperBatch.deleteItemOrderingByCategoryId(itemListParam.getCategoryId());
            itemMapperBatch.insertItemOrderingByListParam(itemListParam);

			/*
			int i = 1;
			for (String itemId : itemListParam.getId()) {

				ItemOrdering itemOrdering = new ItemOrdering();

				itemOrdering.setItemOrderingId(sequenceService.getId("OP_ITEM_ORDERING"));
				itemOrdering.setItemId(Integer.parseInt(itemId));
				itemOrdering.setCategoryId(itemListParam.getCategoryId());
				itemOrdering.setOrdering(i);


				itemMapperBatch.insertItemOrdering(itemOrdering);
				i++;
			}
			*/
        }
    }

    @Override
    public void deleteItemApproval(Item item) {
        item.setUpdatedUserId(UserUtils.getManagerId());
        item.setDataStatusMessage(ShopUtils.getItemStatusMessage("99", "운영자 승인처리."));

        itemMapper.deleteItem(item);
    }

    @Override
    public void deleteListData(ItemListParam itemListParam) {

        if (itemListParam.getId() != null) {

            for (String itemId : itemListParam.getId()) {
                // DATA_STATUS_CODE = '40'로 업데이트 한다. - 삭제신청(40), 삭제 시 관리자 승인 프로세스 추가
                Item item = new Item();
                item.setItemId(Integer.parseInt(itemId));
                item.setDataStatusCode("40");
                item.setDataStatusMessage(ShopUtils.getItemStatusMessage("40", "판매자 답례품 삭제 신청. (승인대기)"));
                item.setUpdatedUserId(UserUtils.getManagerId());

                //itemMapper.deleteItem(item);
                itemMapper.updateItemByListParam(item);

                //로그 기록
                item.setCreatedManagerId(UserUtils.getManagerId());
                item.setCreatedSellerId(item.getSellerId());
                item.setProcessPage("manager");
                item.setActionType("delete");

                itemMapper.insertItemLogForApproval(item);
            }

        }
    }

    @Override
    public void deleteItemData(ItemListParam itemListParam) {

        if (itemListParam.getId() != null) {

            for (String itemId : itemListParam.getId()) {
                // 대표답례품여부를 'N'으로 업데이트 한다.
                Item item = new Item();
                item.setItemId(Integer.parseInt(itemId));
                item.setRepresentativeItemYn("N");
                item.setUpdatedUserId(UserUtils.getManagerId());

                itemMapper.deleteRepresentativeItem(item);
            }

        }
    }

    @Override
    public void updateItemApproval(Item item) {
        item.setUpdatedUserId(UserUtils.getManagerId());
        item.setDataStatusCode("1");
        item.setDataStatusMessage(ShopUtils.getItemStatusMessage("1", "운영자 승인처리."));

        itemMapper.updateItemApprovalByListParam(item);

        // 2. 로그 기록
        item.setCreatedManagerId(UserUtils.getManagerId());
        item.setCreatedSellerId(item.getSellerId());
        item.setProcessPage("manager");
        item.setActionType("approval");

        itemMapper.insertItemLogForApproval(item);
    }

    @Override
    public void updateListData(ItemListParam itemListParam) {

        if (itemListParam.getId() != null) {

			List<ReceiverInfo> receiverInfos = new ArrayList<>();

            // 승인처리
            if ("approval".equals(itemListParam.getProcessType())) {

                for (int i = 0; i < itemListParam.getId().length; i++) {
                    Item item = itemMapper.getItemById(Integer.parseInt(ArrayUtils.get(itemListParam.getId(), i)));
//                    item.setItemId(Integer.parseInt(ArrayUtils.get(itemListParam.getId(), i)));

                    if(item.getDataStatusCode() == "40" || "40".equals(item.getDataStatusCode())) {
                    	this.deleteItemApproval(item);
                    } else {
	                    this.updateItemApproval(item);

	                    Seller seller = sellerMapper.getUserIdByItemId(item.getItemId());

	                	// 국민비서 알림 전송 (상품등록 승인시)
	            		try {
	        				seller.decrypt(sellerEncryptor, false);
	            			if (StringUtils.hasLength(seller.getPhoneNumber().replaceAll("-", "")) && StringUtils.hasLength(seller.getMberCi()) && "0".equals(seller.getReceiveSms())) {
	            				ReceiverInfo receiverInfo = new ReceiverInfo();
	            				receiverInfo.setSmsType(SmsType.ITEM_APPROVAL_COMPLETE);
	            				receiverInfo.setPrvcIdntfcInfo(seller.getMberCi());
	            				StringBuilder sb = new StringBuilder();
	            				sb.append(seller.getCompanyName());	// 상호명
	            				sb.append("|");
	            				sb.append(receiverInfo.getLocalDateTimeToStr());	// 승인요청일시
	            				sb.append("|");
	            				sb.append(item.getItemUserCode());	// 상품코드
	            				sb.append("|");
	            				sb.append(seller.getPhoneNumber().replaceAll("-", ""));
	            				receiverInfo.setSndngCntnts(sb.toString());
	            				receiverInfos.add(receiverInfo);
	            			}
	            		} catch (NullPointerException | ClassCastException e) {
	            			log.error(getClass().getName() +  " :: updateItemApproval send sms error", e);
	            		}
                    }
                }

            } else if ("reject".equals(itemListParam.getProcessType())) {	// 등록 보류
                for (int i = 0; i < itemListParam.getId().length; i++) {
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(ArrayUtils.get(itemListParam.getId(), i)));
                    item.setUpdatedUserId(UserUtils.getManagerId());
                    item.setDataStatusCode("21");
                    item.setDataStatusMessage(ShopUtils.getItemStatusMessage("21", itemListParam.getApprovalMessage()));

                    itemMapper.updateItemByListParam(item);

                    // 2. 로그 기록
                    item.setCreatedManagerId(UserUtils.getManagerId());
                    item.setCreatedSellerId(item.getSellerId());
                    item.setProcessPage("manager");
                    item.setActionType("reject");

                    itemMapper.insertItemLogForApproval(item);


                    Seller seller = sellerMapper.getUserIdByItemId(item.getItemId());

                	// 국민비서 알림 전송 (상품등록 승인거절시)
            		try {
        				seller.decrypt(sellerEncryptor, false);
            			if (StringUtils.hasLength(seller.getPhoneNumber()) && StringUtils.hasLength(seller.getMberCi()) && "0".equals(seller.getReceiveSms())) {
	            			ReceiverInfo receiverInfo = new ReceiverInfo();
	            			receiverInfo.setSmsType(SmsType.ITEM_APPROVAL_CANCEL);
	            			receiverInfo.setPrvcIdntfcInfo(seller.getMberCi());
	            			StringBuilder sb = new StringBuilder();
	            			sb.append(seller.getCompanyName());	// 상호명
	            			sb.append("|");
	            			sb.append(receiverInfo.getLocalDateTimeToStr());	// 승인요청일시
	            			sb.append("|");
	            			sb.append(itemListParam.getApprovalMessage());	// 등록보류사유
	        				sb.append("|");
	        				sb.append(seller.getPhoneNumber().replaceAll("-", ""));
	            			receiverInfo.setSndngCntnts(sb.toString());
	            			receiverInfos.add(receiverInfo);
            			}
            		} catch (NullPointerException | ClassCastException e) {
            			log.error(getClass().getName() +  " :: updateItemApproval send sms error", e);
            		}

                }

            } else if ("update-item-simple".equals(itemListParam.getProcessType())) {	// 답례품 간편 관리
                for (int i = 0; i < itemListParam.getId().length; i++) {
                    Item item = itemListParam.getItemSimple(i);

                    if (item != null) {
                        item.setUpdatedUserId(UserUtils.getManagerId());
                        itemMapper.updateItemForSimple(item);

                        // 2. 로그 기록
                        item.setCreatedManagerId(UserUtils.getManagerId());
                        item.setCreatedSellerId(item.getSellerId());
                        item.setProcessPage("manager");
                        item.setActionType("update-item-simple");

                        itemMapper.insertItemLogForApproval(item);


                    }

                }

            } else {
                for (int i = 0; i < itemListParam.getId().length; i++) {


                    // DATA_STATUS_CODE = '2'로 업데이트 한다.
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(ArrayUtils.get(itemListParam.getId(), i)));
                    item.setUpdatedUserId(UserUtils.getManagerId());

                    item.setItemPrice(ArrayUtils.get(itemListParam.getItemPrice(), i));
                    item.setSalePrice(ShopUtils.emptyToNegativeNumber(ArrayUtils.get(itemListParam.getSalePrice(), i)));
                    //item.setSalePriceNonmember(ShopUtils.emptyToNegativeNumber(ArrayUtils.get(itemListParam.getSalePriceNonmember(), i)));

                    itemMapper.updateItemByListParam(item);
                }
            }

			smsIpsService.insertTifIpsSndngM(receiverInfos);

        }
    }

    @Override
    public void updateListDataByDisplay(ItemListParam itemListParam) {

        if (itemListParam.getId() != null) {
            for (int i = 0; i < itemListParam.getId().length; i++) {

                // DATA_STATUS_CODE = '2'로 업데이트 한다.
                Item item = new Item();
                item.setItemId(Integer.parseInt(ArrayUtils.get(itemListParam.getId(), i)));
                item.setUpdatedUserId(UserUtils.getManagerId());
                item.setDisplayFlag(itemListParam.getDisplayFlag());

                itemMapper.updateItemDisplayByListParam(item);
            }
        }
    }

    @Override
    public void updateListDataByLabel(ItemListParam itemListParam) {

        if (itemListParam.getId() != null) {
            for (int i = 0; i < itemListParam.getId().length; i++) {

                // DATA_STATUS_CODE = '2'로 업데이트 한다.
                Item item = new Item();
                item.setItemId(Integer.parseInt(ArrayUtils.get(itemListParam.getId(), i)));
                item.setUpdatedUserId(UserUtils.getManagerId());

                item.setSoldOut(itemListParam.getSoldOut());

                if ("90".equals(itemListParam.getSoldOut())) {
                    item.setDataStatusMessage(ShopUtils.getItemStatusMessage("90", (ShopUtils.isSellerPage() ? "판매자" : "관리자") + "판매 종료 처리"));
                } else if ("1".equals(itemListParam.getSoldOut())) {
                	itemMapper.deleteCartBySoldOut(item);	// 품절처리하면 장바구니에서도 삭제
                	item.setCreatedManagerId(UserUtils.getManagerId());
                    item.setCreatedSellerId(item.getSellerId());
                    item.setProcessPage("manager");
                    if(ShopUtils.isSellerPage()) {						// 20260219 seller가 답례품 품절 처리 시에 매니저로 들어가는것 수정. 추승연
                    	item.setProcessPage("seller");
                    }
                    item.setActionType("update");

                    itemMapper.insertItemLogForApproval(item);
                }
                //item.setStockQuantity(itemListParam.getStockQuantity());

                itemMapper.updateItemLabelByListParam(item);
            }
        }
    }

    @Override
    public void insertItemCategoryByItemListParam(ItemListParam itemListParam) {
        if (itemListParam.getId() != null && itemListParam.getCategoryId() > 0) {
            itemMapper.deleteItemCategoryByListParam(itemListParam);
            itemMapper.insertItemCategoryByListParam(itemListParam);

        }

    }


    @Override
    public void registerMainItem(Item item) {

        itemMapper.registerMainItem(item);

    }

    @Override
    public void deleteMainItem(int itemId) {

        itemMapper.deleteMainItem(itemId);

    }


    @Override
    public void deleteItemImageByItemId(int itemId) {
        Item item = itemMapper.getItemById(itemId);
        // 1. 이미지 파일 삭제
        if (item.getItemImage().indexOf("/") > -1) {
            fileStorage.delete((FileUtils.getWebRootPath() + ShopUtils.unescapeHtml(item.getItemImage())).replaceAll("/",  File.separator));

        } else {
            String detailsUploadBase = "/item/" + itemId;

            String catalogImage = detailsUploadBase + "/catalog/" + ShopUtils.unescapeHtml(item.getItemImage());
            String listImage = detailsUploadBase + "/list/" + ShopUtils.unescapeHtml(item.getItemImage());
            log.debug(">>> catalogImage : {}", listImage);
            log.debug(">>> catalogImage : {}", listImage);

            fileStorage.delete(catalogImage);
            fileStorage.delete(listImage);
        }

        // 2. 답례품 이미지 정보 업데이트.
        itemMapper.updateItemImageOfItemByItemId(itemId);

    }


    @Override
    public void deleteItemImageById(int itemImageId) {
        ItemImage itemImage = itemMapper.getItemImageById(itemImageId);

        if (itemImage != null) {

            // 1. 이미지 파일 삭제.
            if (itemImage.getImageName().indexOf("/") > -1) {
                fileStorage.delete((FileUtils.getWebRootPath() + ShopUtils.unescapeHtml(itemImage.getImageName())).replaceAll("/",  File.separator));
            } else {
                String detailsUploadBase = "/item/" + itemImage.getItemUserCode();

                //썸네일 사이즈별 이미지 삭제 [2017-06-01] minae.yun
                for (String size : ShopUtils.getThumbnailType()) {

                    String imageName = itemImage.getImageName().substring(0, 14);
                    String detailsImage = "";

                    //파일이름에 괄호()가 있을 경우
                    if (itemImage.getImageName().contains("(") && itemImage.getImageName().contains(")")) {
                        int startIndex = itemImage.getImageName().lastIndexOf("(");
                        int endIndex = itemImage.getImageName().lastIndexOf(")");
                        String subName = itemImage.getImageName().substring(startIndex, endIndex+1);
                        detailsImage = detailsUploadBase + "/" + ShopUtils.unescapeHtml(imageName) + size + subName + "." + FileUtils.getExtension(itemImage.getImageName());
                    } else {
                        detailsImage = detailsUploadBase + "/" + ShopUtils.unescapeHtml(imageName) + size + "." + FileUtils.getExtension(itemImage.getImageName());
                    }

                    fileStorage.delete(detailsImage);
                }

				/*
				String detailsUploadBase = "/item/" + itemImage.getItemUserCode() + "/details";
				String detailsImage = detailsUploadBase + "/" + itemImage.getImageName();
				String detailsBigImage = detailsUploadBase + "/big/" + itemImage.getImageName();
				String detailsThumbImage = detailsUploadBase + "/thumb/" + itemImage.getImageName();

				fileStorage.delete(detailsImage);
				fileStorage.delete(detailsBigImage);
				fileStorage.delete(detailsThumbImage);
				*/

            }

            // 2. 이미지 파일 정보 삭제.
            itemMapper.deleteItemImageById(itemImageId);

            // 3. 대표 이미지 설정.
            List<ItemImage> itemImages = itemMapper.getItemImageListByItemId(itemImage.getItemId());

            Item item = new Item();
            item.setItemId(itemImage.getItemId());

            if (itemImages.isEmpty()) {
                item.setItemImage("");
            } else {
                item.setItemImage(itemImages.get(0).getImageName());
            }
            itemMapper.updateItemImageName(item);

        }

    }

    @Override
    public int insertItemReview(ItemReview itemReview) {
        // 1. 답례품의 sellerId 조회하자..
        Item item = itemMapper.getItemById(itemReview.getItemId());

        int itemReviewId = sequenceService.getId("OP_ITEM_REVIEW");

        itemReview.setSubject(ObjectUtils.isEmpty(itemReview.getSubject()) ? "-" : itemReview.getSubject());
        itemReview.setItemReviewId(itemReviewId);
        itemReview.setRecommendFlag("N");
        itemReview.setUserId(UserUtils.getUserId());
        itemReview.setUserName(UserUtils.getUser().getUserName());
        itemReview.setSellerId(item.getSellerId());
        itemReview.setItem(item);

        // 2. 답례품리뷰 이미지
        if (itemReview.getItemReviewImageFiles() != null && !itemReview.getItemReviewImageFiles().isEmpty()) {
            List<ItemReviewImage> itemReviewImages = new ArrayList<>();

            int index = 0;
            for (MultipartFile multipartFile : itemReview.getItemReviewImageFiles()) {
                if (multipartFile.getSize() > 0) {
                    String[] ITEM_DEFAULT_IMAGE_SAVE_PREFIX = new String[]{"", "thumb_"};
                    String[] ITEM_DEFAULT_IMAGE_SAVE_SIZE = new String[]{"500x-1", "150x-1"};

                    String fileExtension = FileUtils.getExtension(multipartFile.getOriginalFilename());
                    String defaultFileName = fileStorage.getNewFileName(multipartFile.getOriginalFilename());

                    for (int i = 0; i < ITEM_DEFAULT_IMAGE_SAVE_PREFIX.length; i++) {
                        defaultFileName = ITEM_DEFAULT_IMAGE_SAVE_PREFIX[i] + defaultFileName;

                        // 1. 업로드 경로설정
                        String uploadPath = itemReview.getUploadPath();
                        fileService.makeUploadPath(uploadPath);

                        // 2. 파일명 중복파일 삭제
                        fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(defaultFileName));

                        // 2-1. 새로운 파일명.
                        defaultFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

                        // 3. 저장될 파일
                        File saveFile = new File(uploadPath + File.separator + defaultFileName);

                        // 4. 섬네일 사이즈
                        String[] thumbnailSize = StringUtils.delimitedListToStringArray(ITEM_DEFAULT_IMAGE_SAVE_SIZE[i], "x");

                        // 5. 이미지 생성
                        try {
                            ThumbUtils.create(multipartFile, saveFile, Integer.parseInt(thumbnailSize[0]), Integer.parseInt(thumbnailSize[1]), fileStorage);
                        } catch (IOException e) {
//                            log.error("ThumbUtils.create(... Exception : {}", e.getMessage(), e);
                            log.error("ThumbUtils.create(... IOException : {}", getClass().getName() + " :: insertItemReview IOException ==========");
                        }
                        //fileService.createThumbnail(saveFile, uploadPath, newFileName, thumbnailSize, "0");
                    }

                    ItemReviewImage image = new ItemReviewImage();
                    image.setReviewImage(defaultFileName.replace("thumb_", ""));
                    image.setOrdering(index);

                    itemReviewImages.add(image);
                    index++;
                }
            }

            itemReview.setItemReviewImages(itemReviewImages);
        }

        // 3. 리뷰 노출 설정 (1: 즉시 노출, 2: 관리자 승인 후 노출)
        Config config = configService.getShopConfig(Config.SHOP_CONFIG_ID);
        if ("1".equals(config.getReviewDisplayType())) {
            itemReview.setDisplayFlag("Y");
            itemReview.setLoginId(UserUtils.getLoginId());
            itemReview = earnItemReviewPoint(itemReview, config);
        }

        itemReview.encrypt(itemReviewEncryptor);

        itemMapper.insertItemReview(itemReview);

        List<ItemReviewFilter> itemReviewFilters = itemReview.getItemReviewFilters();

        if (itemReviewFilters != null && !itemReviewFilters.isEmpty()) {
            itemReviewFilters.forEach(f->{
                f.setItemReviewId(itemReviewId);
            });

            itemReviewFilterRepository.saveAll(itemReviewFilters);
        }


        if (itemReview.getItemReviewImages() != null && !itemReview.getItemReviewImages().isEmpty()) {
            itemMapper.insertItemReviewImage(itemReview);
        }

        return itemReviewId;

    }


    @Override
    public List<ItemRelation> getItemRelationRandomList(ItemParam itemParam) {
        return itemMapper.getItemRelationRandomList(itemParam);
    }


    @Override
    public int getItemReviewCountByParam(ItemParam itemParam) {
        return itemMapper.getItemReviewCountByParam(itemParam);
    }

    @Override
    public List<ItemReview> getItemReviewListByParam(ItemParam itemParam) {

        itemParam.encrypt(itemReviewCriteriaEncryptor);
        List<ItemReview> list = itemMapper.getItemReviewListByParam(itemParam);

        itemParam.decrypt(itemReviewCriteriaEncryptor);

        if (list != null) {
            for (ItemReview review : list) {
                String starScore = "";
                for (int i = 0; i < review.getScore(); i++) {
                    starScore += "<span class='on'></span>";
                }

                review.setStarScore(starScore);
                review.decrypt(itemReviewEncryptor);
            }
        }
        return list;
    }


    @Override
    public List<ItemOther> getItemOtherList(int itemId) {
        return itemMapper.getItemOtherList(itemId);
    }


    @Override
    public ItemOption getItemOptionById(int itemOptionId) {
        return itemMapper.getItemOptionById(itemOptionId);
    }

    @Override
    public void updateItemStockQuantityByItemUserCodeNoTx(String stockCode, int quantity, String sign) {
        if ("1".equals(sign) || "2".equals(sign)) {
            ItemStockQuantityParam itemStockQuantityParam = new ItemStockQuantityParam();

            itemStockQuantityParam.setStockCode(stockCode);
            itemStockQuantityParam.setQuantity(quantity);
            itemStockQuantityParam.setSign(sign);

            itemMapper.updateItemStockQuantityByItemStockQuantityParam(itemStockQuantityParam);
        }
    }


    @Override
    public void updateItemOptionStockQuantityByOptionCodeNoTx(String stockCode, int quantity, String sign) {
        if ("1".equals(sign) || "2".equals(sign)) {
            ItemStockQuantityParam itemStockQuantityParam = new ItemStockQuantityParam();

            itemStockQuantityParam.setStockCode(stockCode);
            itemStockQuantityParam.setQuantity(quantity);
            itemStockQuantityParam.setSign(sign);

            itemMapper.updateItemOptionStockQuantityByItemStockQuantityParam(itemStockQuantityParam);
        }
    }


    @Override
    public void updateItemStockQuantityByItemStockQuantityParamNoTx(int itemId, int quantity, String sign) {
        if (itemId > 0) {
            if ("1".equals(sign) || "2".equals(sign)) {
                ItemStockQuantityParam itemStockQuantityParam = new ItemStockQuantityParam();

                List<Integer> keys = new ArrayList<>();
                keys.add(itemId);

                itemStockQuantityParam.setKeys(keys);
                itemStockQuantityParam.setQuantity(quantity);
                itemStockQuantityParam.setSign(sign);

                itemMapper.updateItemStockQuantityByItemStockQuantityParam(itemStockQuantityParam);
            }
        }
    }

    @Override
    public void updateItemOptionStockQuantityByItemStockQuantityParamNoTx(List<Integer> optionIds, int quantity, String sign) {
        if (!optionIds.isEmpty()) {
            if ("1".equals(sign) || "2".equals(sign)) {
                ItemStockQuantityParam itemStockQuantityParam = new ItemStockQuantityParam();

                itemStockQuantityParam.setKeys(optionIds);
                itemStockQuantityParam.setQuantity(quantity);
                itemStockQuantityParam.setSign(sign);

                itemMapper.updateItemOptionStockQuantityByItemStockQuantityParam(itemStockQuantityParam);
            }
        }
    }

    @Override
    public List<Item> getNewArrivalItemList(ItemParam itemParam) {
        return itemMapper.getNewArrivalItemList(itemParam);
    }


    @Override
    public List<Item> getNewArrivalItemListForMain(ItemParam itemParam) {
        return itemMapper.getNewArrivalItemListForMain(itemParam);
    }


    @Override
    public List<Item> getWishlistItemList(WishlistGroup wishlistGroup) {
        return itemMapper.getWishlistItemList(wishlistGroup);
    }



    @Override
    public int getSearchNewArrivalItemCount(ItemParam itemParam) {
        return itemMapper.getSearchNewArrivalItemCount(itemParam);
    }


    @Override
    public List<Item> getSearchNewArrivalItemList(ItemParam itemParam) {
        return itemMapper.getSearchNewArrivalItemList(itemParam);
    }

    @Override
    public ItemReview getItemReviewById(int itemReviewId) {
        ItemReview itemReview = itemMapper.getItemReviewById(itemReviewId);
        itemReview.decrypt(itemReviewEncryptor);

        return itemReview;
    }

    @Override
    public void updateItemReview(ItemReview itemReview) {

        Config config = configService.getShopConfig(Config.SHOP_CONFIG_ID);
        itemReview = earnItemReviewPoint(itemReview, config);

        if (ObjectUtils.isEmpty(itemReview.getContent())) {
            itemReview.setContent("");
        }

        itemReview.encrypt(itemReviewEncryptor);

        itemMapper.updateItemReview(itemReview);
    }

    private ItemReview earnItemReviewPoint(ItemReview itemReview, Config config) {
        if ("N".equals(itemReview.getPointPayment()) && "Y".equals(itemReview.getDisplayFlag())) {

        	String deLoginStr = "";
			try {
				deLoginStr = pCrypto.Encrypt("normal", itemReview.getLoginId(), "");
			} catch (UnsupportedEncodingException e) {
				log.error("■■■ERROR■■■ earnItemReviewPoint Exception {}",e.getStackTrace()[0]);
			}

            User user  = userMapper.getUserByLoginId(deLoginStr);

            Point point = new Point();
            String message = "";

            int imageCount = itemReview.getItemReviewImages() != null && !itemReview.getItemReviewImages().isEmpty() ?
                    itemReview.getItemReviewImages().size() : itemMapper.getItemReviewImageCount(itemReview.getItemReviewId());

            if (imageCount == 0) {
                point.setPoint(config.getPointReview());                    // 이미지가 없는 일반 후기일때
                message = "「" + itemReview.getItem().getItemName() + "」에 대한 이용후기 " + MessageUtils.getMessage("M00246");
            } else {
                point.setPoint(config.getPhotoPointReview());				// 이미지가 있는 포토 후기일때
                message = "「" + itemReview.getItem().getItemName() + "」에 대한 포토이용후기 " + MessageUtils.getMessage("M00246");
            }

            point.setReason(message);

            if (user != null) {
                point.setUserId(user.getUserId());
                point.setPointType(PointUtils.DEFAULT_POINT_CODE);
                pointService.earnPoint("review", point);
                itemReview.setPointPayment("Y");
                itemReview.setPoint(point.getPoint());
            }
        }

        return itemReview;
    }

    @Override
    public void deleteItemReview(int itemReviewId) {
        itemMapper.deleteItemReview(itemReviewId);
    }

    @Override
    public void deleteItemReviewImage(ItemReview itemReview, ItemReviewImage itemReviewImage) {

        fileStorage.delete(itemReview.getUploadPath(), ShopUtils.unescapeHtml(itemReviewImage.getReviewImage()));
        itemMapper.deleteItemReviewImageById(itemReviewImage.getItemReviewImageId());
    }

    @Async
    @Override
    public Future<AsyncReport> uploadCsv(MultipartFile[] multipartFiles) {
        AsyncReport report = new AsyncReport("ITEM");
        try {
            Thread.sleep(30000);

            String message = "";
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.getSize() > 0) {

                    message += multipartFile.getOriginalFilename() + " " + multipartFile.getSize() + "\n";
                }
            }

            report.setMessage(message);

            //            if (1 == 1) {
            //            	throw new RuntimeException("등록 오류가 발생했습니다.");
            //            }
        } catch (InterruptedException e) {
        	report.setMessage("InterruptedException Occured"); //report.setMessage(e.getMessage());
            log.error("InterruptedException Occured");		// log.error(e.getMessage(), e);
        }



        return new AsyncResult<AsyncReport>(report);
    }

    @Override
    public List<ItemInfo> getItemInfoListForExcel(ItemParam itemParam) {
        return itemMapper.getItemInfoListForExcel(itemParam);
    }


    @Override
    public List<ItemInfo> getItemInfoMobileListForExcel(ItemParam itemParam) {
        return itemMapper.getItemInfoMobileListForExcel(itemParam);
    }


    @Override
    public List<ExcelItemCategory> getItemCategoryListForExcel(ItemParam itemParam) {
        return itemMapper.getItemCategoryListForExcel(itemParam);
    }


    @Override
    public List<ExcelItemRelation> getItemRelationListForExcel(ItemParam itemParam) {
        return itemMapper.getItemRelationListForExcel(itemParam);
    }


    @Override
    public List<ExcelItemPointConfig> getItemPointListForExcel(ItemParam itemParam) {
        return itemMapper.getItemPointListForExcel(itemParam);
    }


    @Override
    public List<Item> getItemKeywordListForExcel(ItemParam itemParam) {
        return itemMapper.getItemKeywordListForExcel(itemParam);
    }


    @Override
    public String insertExcelData(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new UserException(MessageUtils.getMessage("M01532")); // 파일을 선택해 주세요.
        }

        String fileName = multipartFile.getOriginalFilename();
        String fileExtension = FileUtils.getExtension(fileName);

        // 확장자 체크
        if (!(fileExtension.equalsIgnoreCase("xlsx"))) {
            throw new UserException(MessageUtils.getMessage("M01533"));	// 엑셀 파일(.xlsx)만 업로드가 가능합니다.
        }

        // 용량체크
        String maxUploadFileSize = "20";
        Long maxUploadSize = Long.parseLong(maxUploadFileSize) * 1000 * 1000;

        if (multipartFile.getSize() > maxUploadSize) {
            throw new UserException("Maximum upload file Size : " + maxUploadFileSize + "MB");
        }

        // 엑셀 셀 읽기 : http://poi.apache.org/spreadsheet/quick-guide.html#CellContents 자세한 건 여기서 확인 - skc
        XSSFWorkbook wb = null;

        String excelUploadReport = "";
        try {
            wb = new XSSFWorkbook(multipartFile.getInputStream());

            excelUploadReport += "<p class=\"upload_file\">" + multipartFile.getOriginalFilename() + "</p>\n";
            ArrayList<Item> oldItems = new ArrayList<>();

            // 답례품 Main SHEET (신규, 수정)
            excelUploadReport += processItemMainExcelSheet(wb.getSheet("item_main"), false, oldItems);

            // 답례품 Seo SHEET
            excelUploadReport += processItemSubExcelSheet(wb.getSheet("item_seo"), oldItems);

            // 답례품 상세설명 (테이블) SHEET
            excelUploadReport += processItemInfoExcelSheet(wb.getSheet("item_table"), oldItems);

            // 답례품 상세설명 (테이블) SHEET
            // excelUploadReport += processItemInfoMobileExcelSheet(wb.getSheet("item_table_mobile"));

            // 답례품 옵션 SHEET
            excelUploadReport += processItemOptionExcelSheet(wb.getSheet("item_option"), oldItems);

            // 답례품 이미지 SHEET
            excelUploadReport += processItemImageExcelSheet(wb.getSheet("item_image"), oldItems);

            // 답례품 카테고리 SHEET
            excelUploadReport += processItemCategoryExcelSheet(wb.getSheet("item_category"), oldItems);

            // 답례품 관련 답례품 SHEET
            excelUploadReport += processItemRelationExcelSheet(wb.getSheet("item_relation"), oldItems);

            // 답례품 포인트설정 SHEET
            // excelUploadReport += processItemPointConfigExcelSheet(wb.getSheet("item_point"));

            // 답례품 선택 정보 SHEET
            excelUploadReport += processItemCheckExcelSheet(wb.getSheet("item_check"));

            // 답례품 사이트내 검색어 SHEET
            excelUploadReport += processItemKeywordExcelSheet(wb.getSheet("item_keyword"));


            // 답례품 Main SHEET (삭제용)
            // 답례품 Main SHEET (답례품 삭제 처리 때문에 하단에 처리. -  'd' 인 경우 삭제 후 참조 테이블 업데이트로 오류 발생함.
            excelUploadReport += processItemMainExcelSheet(wb.getSheet("item_main"), true, oldItems);


            return excelUploadReport;

        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw new UserException(MessageUtils.getMessage("M01534") + "(" + e.getMessage() + ")"); // 엑셀 파일 로드 시 오류가 발생하였습니다.
            log.error(getClass().getName() + " :: insertExcelData IOException =============");
//            throw new UserException(MessageUtils.getMessage("M01534")); // 엑셀 파일 로드 시 오류가 발생하였습니다.
            throw new UserException("엑셀 파일 로드 시 오류가 발생하였습니다."); // 엑셀 파일 로드 시 오류가 발생하였습니다.
        } catch (Exception e) {
//			log.error(e.getMessage(), e);
//            throw new UserException(MessageUtils.getMessage("M01534") + "(" + e.getMessage() + ")"); // 엑셀 파일 로드 시 오류가 발생하였습니다.
        	log.error(getClass().getName() + " :: insertExcelData Exception =============");
//            throw new UserException(MessageUtils.getMessage("M01534")); // 엑셀 파일 로드 시 오류가 발생하였습니다.
            throw new UserException("엑셀 파일 로드 시 오류가 발생하였습니다."); // 엑셀 파일 로드 시 오류가 발생하였습니다.
        }


    }

    /**
     * 답례품 정보 (ITEM_MAIN)
     * @param sheet
     * @param isDelete (true: 삭제용, false: 신규, 수정) 처리용.
     * @return
     */
    @SuppressWarnings("static-access")
    private String processItemMainExcelSheet(XSSFSheet sheet, boolean isDelete, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        // 수정, 삭제 인 경우 Batch로 처리
        List<Item> insertItems = new ArrayList<>();
        List<PointConfig> pointConfigures = new ArrayList<>();
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수
        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
                        CellReference cellReference = new CellReference(cell);

                        HashMap<String, String> cellInfo = new HashMap<>();
                        cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
                        cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

                        cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            String control = ShopUtils.getString(row.getCell(0)).toUpperCase();
            String itemUserCode = ShopUtils.getString(row.getCell(1));

            // 답례품코드가 입력되지 않은 경우
            if (!"N".equals(control) && ObjectUtils.isEmpty(itemUserCode)) {
                HashMap<String, String> cellReference = cellReferences.get(1);
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                executionLog.append(PoiUtils.log(cellReference, "답례품코드가 입력되지 않았습니다."));

                rowErrorCount++;
                continue;
            }

            Item item = null;
            PointConfig pointConfig = new PointConfig();
            Shipment shipment = null;
            ShipmentReturn shipmentReturn = null;
            DeliveryCompany deliveryCompany = null;

            int shipping = 0;
            int shippingFreeAmount = 0;
            int shippingExtraCharge1 = 0;
            int shippingExtraCharge2 = 0;

            // 삭제용 vs 신규, 수정 구분
            if (isDelete) {
                if ("N".equals(control) || "U".equals(control)) {
                    continue;
                }
            } else {
                if ("D".equals(control)) {
                    continue;
                }
            }

            pointConfig.setPointConfigId(sequenceService.getId("OP_POINT_CONFIG"));
            pointConfig.setConfigType("2");		// 2: 답례품포인트
            pointConfig.setStatusCode("1");
            pointConfig.setPeriodType("1");		// 1: 기간설정 (일반)
            pointConfig.setStartDate("");
            pointConfig.setStartTime("");
            pointConfig.setEndDate("");
            pointConfig.setEndTime("");
            pointConfig.setRepeatDay("");		// 특정일
            pointConfig.setCreatedUserId(UserUtils.getUserId());

            // 처리방법에 따른 기본 사항 체크
            if ("N".equals(control)) {				// N: 등록
                item = new Item();
                int itemId = sequenceService.getId("OP_ITEM");
                String itemCode = "G2" + StringUtils.lPad(Integer.toString(itemId), 9, '0');

                pointConfig.setItemId(itemId);

                item.setItemId(itemId);
                item.setItemUserCode(itemCode);
                item.setTempId(ShopUtils.getString(row.getCell(1)));
                item.setTempControl("N");

                oldItems.add(item);

                item.setItemId(itemId);
                item.setItemCode(itemCode);
                item.setItemUserCode(itemCode);
                item.setItemType("1");
                item.setItemType1("0");
                item.setItemType2("0");
                item.setItemType3("0");
                item.setItemType4("0");
                item.setItemType5("0");
                item.setPrivateType("000");
                item.setItemLabel("0");
                item.setNonmemberOrderType("1");
                item.setSellerDiscountType("1");
                item.setShippingGroupCode("");
                item.setDataStatusCode("1");
                item.setUpdatedUserId(UserUtils.getUserId());
                item.setCreatedUserId(UserUtils.getUserId());
                item.setItemOptionTitle1("옵션명");
                item.setItemOptionTitle2("옵션값");

            } else if ("U".equals(control)) {		// U: 수정
                // 답례품코드 중복여부
                int itemCount = itemMapper.getItemCountByItemUserCode(itemUserCode);

	    		/*
	    		if (itemCount > 0) {
	    			HashMap<String, String> cellReference = cellReferences.get(1);
	    			cellReference.put("rowIndex", Integer.toString(rowIndex));

	    			executionLog.append(PoiUtils.log(cellReference, "답례품코드가 중복되었습니다. (" + itemUserCode + ")"));

	    			rowErrorCount++;
	    			continue;
	    		}
	    		*/

                item = new Item();
                item.setItemUserCode(itemUserCode);
                item.setTempId(itemUserCode);
                item.setTempControl("U");

                oldItems.add(item);

                int itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);

                item.setItemId(itemId);
                pointConfig.setItemId(itemId);
            } else if ("D".equals(control)) {		// D: 삭제
                item = new Item();
                item.setItemUserCode(itemUserCode);
                item.setTempId(itemUserCode);
                item.setTempControl("D");
                item.setSellerId(UserUtils.getManagerId());

                oldItems.add(item);


                item.setTempId(itemUserCode);
                item.setTempControl("D");

                oldItems.add(item);

                itemMapperBatch.deleteItemByItemUserCode(item);

                rowDataCount++;
                continue;

            } else {
                HashMap<String, String> cellReference = cellReferences.get(0);
                cellReference.put("rowIndex", Integer.toString(rowIndex));
                executionLog.append(PoiUtils.log(cellReference, "Control value is empty. (use 'I', 'U', 'D')"));

                rowErrorCount++;
                continue;

            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;
            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                    log.warn(" cellReferences.get(cell.getColumnIndex()) : RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {

                    case 2: 	// 답례품명
                        item.setItemName(ShopUtils.getString(cell));
                        break;

                    case 3: 	// 공급사
                        int cellValue = ShopUtils.getInt(cell);
                        if (ShopUtils.isSellerPage()) {
                            if(SellerUtils.getSellerId() != cellValue) {
                                executionLog.append(PoiUtils.log(cellReference, "잘못된 공급사를 입력하셨습니다."));
                                cellErrorCount++;
                                break;
                            } else {
                                item.setSellerId(cellValue);
                            }
                        } else {
                            if(cellValue <= 0) {
                                item.setSellerId(90000000);
                            } else {
                                item.setSellerId(cellValue);
                            }
                        }
                        break;

                    case 4: 	// 제조사
                        item.setManufacturer(ShopUtils.getString(cell));
                        break;

                    case 5:		// 브랜드
                        Brand brand = brandService.getBrandById(ShopUtils.getInt(cell));
                        if (brand == null && ShopUtils.getInt(cell)  != 0) {
                            executionLog.append(PoiUtils.log(cellReference, "잘못된 브랜드를 입력하셨습니다."));
                            cellErrorCount++;
                            break;
                        }
                        if (brand != null) {
                            item.setBrandId(ShopUtils.getInt(cell));
                            item.setBrand(brand.getBrandName());
                        }
                        break;

                    case 6: 	// 원산지
                        item.setOriginCountry(ShopUtils.getString(cell));
                        break;

                    case 7: 	// 무게
                        item.setWeight(ShopUtils.getString(cell));
                        break;

                    case 8: 	// 과세구분
                        item.setTaxType("2".equals(ShopUtils.getString(cell)) ? "2" : "1");
                        break;

                    case 9: 	// 사은품 사용유무
                        item.setFreeGiftFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 10: 	// 사은품 정보
                        if ("Y".equals(item.getFreeGiftFlag().toUpperCase())) {
                            item.setFreeGiftName(ShopUtils.getString(cell));
                        }
                        break;

                    case 11: 	// 답례품간략설명
                        item.setItemSummary(ShopUtils.getString(cell));
                        break;

                    case 12: 	// 검색어
                        item.setItemKeyword(ShopUtils.getString(cell));
                        break;

                    case 13:	// 판매가격
                        item.setSalePrice(ShopUtils.getInt(cell));
                        break;

                    case 14:	// 공급가 설정
                        String[] available = {"1", "2", "3"};
                        String cellString = PoiUtils.getString(cell);
                        if (Arrays.asList(available).contains(cellString)) {
                            item.setCommissionType(cellString);
                        } else {
                            item.setCommissionType("1");
                        }
                        break;

                    case 15:	// 공급가
                        if ("3".equals(item.getCommissionType())) {
                            item.setSupplyPrice(ShopUtils.getInt(cell));
                        }
                        if (item.getSupplyPrice() > item.getSalePrice()) {
                            executionLog.append(PoiUtils.log(cellReference, "공급가는 판매금액보다 높게 설정할 수 없습니다."));
                            cellErrorCount++;
                            break;
                        }
                        break;

                    case 16:	// 수수료율
                        if ("1".equals(item.getCommissionType())) {
                            if (UserUtils.isManagerLogin()) {
                                Seller seller = sellerMapper.getSellerById(item.getSellerId());
                                item.setCommissionRate(seller.getCommissionRate());
                            } else if(SellerUtils.getSeller() != null) {
                            	item.setCommissionRate(SellerUtils.getSeller().getCommissionRate());
                            }
                        }
                        if ("2".equals(item.getCommissionType())) {
                            item.setCommissionRate(Float.parseFloat(ShopUtils.getString(cell)));
                        }
                        break;

                    case 17:	// 정가
                        item.setItemPrice(ShopUtils.getString(cell));
                        break;


                    case 18:	// 재고연동
                        item.setStockFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 19:	// 답례품재고
                        if (ObjectUtils.isEmpty(ShopUtils.getString(cell))) {
                            item.setStockFlag("N");
                        }

                        if ("Y".equals(item.getStockFlag().toUpperCase())) {
                            item.setStockQuantity(ShopUtils.getInt(cell));
                        } else {
                            item.setStockQuantity(-1);
                        }
                        break;

                    case 20:	// 관리코드
                        item.setStockCode(ShopUtils.getString(cell));
                        break;

                    case 21:	// 품절여부
                        item.setSoldOut("1".equals(ShopUtils.getString(cell)) ? "1" : "0");
                        break;

                    case 22:	// 최소 구매 수량
                        String orderMinQuantity = ShopUtils.getString(cell);
                        item.setOrderMinQuantity("".equals(orderMinQuantity) ? -1 : Integer.parseInt(orderMinQuantity));
                        break;

                    case 23:	// 최대 구매 수량
                        String orderMaxQuantity = ShopUtils.getString(cell);
                        item.setOrderMaxQuantity("".equals(orderMaxQuantity) ? -1 : Integer.parseInt(orderMaxQuantity));
                        break;

                    case 24:	// 쿠폰 사용 가능 여부
                        item.setCouponUseFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 25:	// 즉시할인
                        item.setSellerDiscountFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 26:	// 즉시할인 금액
                        if ("Y".equals(item.getSellerDiscountFlag().toUpperCase())) {
                            item.setSellerDiscountAmount(ShopUtils.getInt(cell));
                        }
                        break;

                    case 27:	// 포인트 지급
                        item.setSellerPointFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 28:	// 포인트
                        if ("Y".equals(item.getSellerPointFlag().toUpperCase())) {
                            pointConfig.setPoint(ShopUtils.getInt(cell));
                        }
                        break;

                    case 29:	// 적립구분
                        pointConfig.setPointType("1".equals(ShopUtils.getString(cell)) ? "1" : "2");
                        break;

                    case 30: 	// 답례품공개유무
                        item.setDisplayFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 31:	// 배송구분
                        item.setDeliveryType("2".equals(ShopUtils.getString(cell)) ? "2" : "1");
                        break;

                    case 32:	// 택배사
                        deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(ShopUtils.getInt(cell));
                        if (deliveryCompany != null) {
                            item.setDeliveryCompanyId(ShopUtils.getInt(cell));
                            item.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
                        } else {
                            executionLog.append(PoiUtils.log(cellReference, "잘못된 택배사를 입력하셨습니다."));
                            cellErrorCount++;
                            break;
                        }
                        break;

                    case 33:	// 출고지 주소
                        ShipmentParam shipmentParam = new ShipmentParam();
                        shipmentParam.setShipmentId(ShopUtils.getInt(cell));
                        shipmentParam.setSellerId("1".equals(item.getDeliveryType()) ? 90000000 : item.getSellerId());

                        shipment = shipmentService.getShipmentByParam(shipmentParam);

                        if (shipment != null) {
                            item.setShipmentId(ShopUtils.getInt(cell));
                            shipping = shipment.getShipping();
                            shippingFreeAmount = shipment.getShippingFreeAmount();
                            shippingExtraCharge1 = shipment.getShippingExtraCharge1();
                            shippingExtraCharge2 = shipment.getShippingExtraCharge2();
                        } else {
                            executionLog.append(PoiUtils.log(cellReference, "잘못된 출고지 주소를 입력하셨습니다."));
                            cellErrorCount++;
                            break;
                        }
                        break;

                    case 34:	// 배송비 종류
                        item.setShippingType(ShopUtils.getString(cell));
                        if ("2".equals(item.getShippingType())) {
                            item.setShippingGroupCode("SELLER-" + item.getSellerId());
                        } else if ("3".equals(item.getShippingType())) {
                            item.setShippingGroupCode("SHIPMENT-" + item.getShipmentId());
                        } else if ("4".equals(item.getShippingType())) {
                            item.setShippingGroupCode("ITEM-" + item.getItemId());
                        } else if ("5".equals(item.getShippingType())) {
                            item.setShippingGroupCode("ITEM-PER-" + item.getItemId());
                        }
                        break;

                    case 35:	// 배송비
                        if ("3".equals(item.getShippingType())) {
                            item.setShipping(shipping);
                        }
                        if ("4".equals(item.getShippingType()) || "5".equals(item.getShippingType()) || "6".equals(item.getShippingType())) {
                            item.setShipping(ShopUtils.getInt(cell));
                        }
                        break;

                    case 36:	// 배송비 무료 기준금액
                        if ("3".equals(item.getShippingType())) {
                            item.setShippingFreeAmount(shippingFreeAmount);
                        }
                        if ("4".equals(item.getShippingType())) {
                            item.setShippingFreeAmount(ShopUtils.getInt(cell));
                        }
                        break;

                    case 37:	// 배송비 추가 기준수량
                        if ("5".equals(item.getShippingType())) {
                            item.setShippingItemCount(ShopUtils.getInt(cell));
                        }
                        break;

                    case 38:	// 제주 추가배송비
                        if ("3".equals(item.getShippingType())) {
                            item.setShippingExtraCharge1(shippingExtraCharge1);
                        }
                        if (!"2".equals(item.getShippingType()) && !"3".equals(item.getShippingType())) {
                            item.setShippingExtraCharge1(ShopUtils.getInt(cell));
                        }
                        break;

                    case 39:	// 도서산간 추가배송비
                        if ("3".equals(item.getShippingType())) {
                            item.setShippingExtraCharge2(shippingExtraCharge2);
                        }
                        if (!"2".equals(item.getShippingType()) && !"3".equals(item.getShippingType())) {
                            item.setShippingExtraCharge2(ShopUtils.getInt(cell));
                        }
                        break;

                    case 40:	// 반품신청 가능 여부
                        item.setItemReturnFlag("N".equalsIgnoreCase(ShopUtils.getString(cell)) ? "N" : "Y");
                        break;

                    case 41:	// 반품/교환 구분
                        item.setShipmentReturnType("2".equals(ShopUtils.getString(cell)) ? "2" : "1");
                        break;

                    case 42:	// 반품/교환 주소
                        ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
                        shipmentReturnParam.setShipmentReturnId(ShopUtils.getInt(cell));
                        shipmentReturnParam.setSellerId("1".equals(item.getShipmentReturnType()) ? 90000000 : item.getSellerId());

                        shipmentReturn = shipmentReturnService.getShipmentReturnByParam(shipmentReturnParam);
                        if (shipmentReturn != null) {
                            item.setShipmentReturnId(ShopUtils.getInt(cell));
                        } else {
                            executionLog.append(PoiUtils.log(cellReference, "잘못된 반품/교환 주소를 입력하셨습니다."));
                            cellErrorCount++;
                            break;
                        }
                        break;

                    case 43:	// 반품/교환 배송비
                        item.setShippingReturn(ShopUtils.getInt(cell));
                        break;

                    case 44:	// 답례품 상세 설명
                        item.setDetailContent(ShopUtils.getString(cell));
                        break;

                    case 45:	// 답례품 상세 설명(모바일)
                        item.setDetailContentMobile(ShopUtils.getString(cell));
                        break;

                    case 46:	// 조회수
                        break;

                    case 47:	// 등록일
                        break;

                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 등록 / 수정 처리
            String message = "";			// dataStatusMessage
            String actionMessage = "";		// itemLog actionType

            // itemLog 공통 data setting
            if (!ShopUtils.isSellerPage()) {
                item.setCreatedManagerId(UserUtils.getManagerId());
                item.setCreatedSellerId(90000000);					// 관리자 아이디 하드코딩
                item.setProcessPage("manager");
            } else {
                item.setCreatedManagerId(SellerUtils.getSellerId());
                item.setCreatedSellerId(SellerUtils.getSellerId());
                item.setProcessPage("seller");
            }

            if ("N".equals(control)) {			// N: 등록
                actionMessage = "insert-by-excel";
                if(!ShopUtils.isSellerPage()) {								// 관리자 페이지 일 때
                    item.setDataStatusCode("1");
                    message =  "관리자 엑셀업로드 답례품 등록.";
                    item.setCreatedUserId(UserUtils.getManagerId());
                } else {
                    Seller seller = sellerMapper.getSellerById(SellerUtils.getSellerId());

                    if ("1".equals(seller.getItemApprovalType())) {				// 판매관리자의 답례품승인타입이 운영자 승인일 때
                        item.setDataStatusCode("20");	//  등록신청
                        message =  "판매자 엑셀업로드 답례품 등록 신청.";
                        actionMessage = "apply-reg-by-excel";
                    } else {													// 판매관리자의 답례품승인타입이 자동승인일 때
                        item.setDataStatusCode("1");
                        message =  "판매자 엑셀업로드 답례품 등록(자동승인).";
                    }
                    item.setCreatedUserId(seller.getSellerId());
                }

                item.setDataStatusMessage(ShopUtils.getItemStatusMessage(item.getDataStatusCode(), message));
                insertItems.add(item);

                item.setPriceCriteria("2");
                item.setActionType(actionMessage);
                itemMapper.insertItemLog(item);

                pointConfigures.add(pointConfig);

            } else if ("U".equals(control)) {	// U: 수정
                Item itemOrgData = itemMapper.getItemByItemUserCodeForPreview(item.getItemUserCode());
                actionMessage =  "update-by-excel";
                if(!ShopUtils.isSellerPage()) {
                    message =  "관리자 엑셀업로드 답례품 수정.";
                    item.setUpdatedUserId(UserUtils.getManagerId());
                    item.setDataStatusCode(itemOrgData.getDataStatusCode());
                } else {
                    Seller seller = sellerMapper.getSellerById(SellerUtils.getSellerId());
                    if("1".equals(seller.getItemApprovalType())) {
                        item.setDataStatusCode("30");	// 재등록신청
                        message =  "판매자 엑셀업로드 답례품 수정 신청.";
                        actionMessage = "apply-mod-by-excel";
                    } else {
                        item.setDataStatusCode("1");
                        message =  "판매자 엑셀업로드 답례품 수정(자동승인).";

                    }
                    item.setUpdatedUserId(seller.getSellerId());
                }
                item.setDataStatusMessage(itemOrgData.getDataStatusMessage() + ShopUtils.getItemStatusMessage(item.getDataStatusCode(), message));
                itemMapperBatch.updateItemForExcel(item);
                itemMapperBatch.deleteItemPointConfigByItemId(item.getItemId());

                item.setPriceCriteria("2");
                item.setActionType(actionMessage);
                itemMapper.insertItemLog(item);

                pointConfigures.add(pointConfig);
            }

            rowDataCount++;

        } // row


        // 답례품 신규 등록
        if (!insertItems.isEmpty()) {
            for (int i=0; i<insertItems.size(); i++) {
                itemMapper.insertItem(insertItems.get(i));
            }
        }

        // 포인트 등록
        if (!pointConfigures.isEmpty()) {
            itemMapperBatch.insertItemPointConfigListForExcel(pointConfigures);
        }


        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }
        return result;
    }

    /**
     * 답례품 SUB
     * @param sheet
     * @return
     */
    @SuppressWarnings("static-access")
    private String processItemSubExcelSheet(XSSFSheet sheet, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();



        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        // 수정, 삭제 인 경우 Batch로 처리
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수
        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
                        CellReference cellReference = new CellReference(cell);

                        HashMap<String, String> cellInfo = new HashMap<>();
                        cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
                        cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

                        cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }

            String itemUserCode = ShopUtils.getString(row.getCell(0));

            // 답례품No.가 입력되지 않은 경우
            if (ObjectUtils.isEmpty(ShopUtils.getString(row.getCell(0)))) {
                HashMap<String, String> cellReference = cellReferences.get(1);
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                executionLog.append(PoiUtils.log(cellReference, "답례품No.가 입력되지 않았습니다."));

                rowErrorCount++;
                continue;
            }

            // 답례품코드 유효 여부
            try {
                int itemId = 0;
                String control = "N";

                for (int i=0; i<oldItems.size(); i++) {
                    if ("N".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                        itemId = oldItems.get(i).getItemId();
                        itemUserCode = oldItems.get(i).getItemUserCode();
                        control = "N";
                    } else if ("U".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                        control = "U";
                    }
                }

                if ("U".equals(control)) {
                    itemId = itemMapper.getItemIdByItemUserCode(ShopUtils.getString(row.getCell(0)));
                }

            } catch(BindingException e) {
                HashMap<String, String> cellReference = cellReferences.get(1);
                cellReference.put("rowIndex", Integer.toString(rowIndex));
                executionLog.append(PoiUtils.log(cellReference, "답례품코드가 존재하지 않습니다. (" + itemUserCode + ")"));

                rowErrorCount++;
                continue;
            }


            ExcelItemSub item = new ExcelItemSub();

            item.setItemUserCode(itemUserCode);



            // 등록, 수정인 경우
            int cellErrorCount = 0;
            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "Exception Occured"); //log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", e.getMessage());
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {

                    case 1: 	// 답례품명
                        item.setItemName(ShopUtils.getString(cell));
                        break;

                    case 2:	// SEO > INDEX
                        item.getSeo().setIndexFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "N" : "Y");
                        break;

                    case 3:	// SEO > 브라우저 타이틀
                        item.setSeoTitle(ShopUtils.getString(cell));
                        break;

                    case 4:	// SEO > Meta 키워드
                        item.setSeoKeywords(ShopUtils.getString(cell));
                        break;

                    case 5:	// SEO > Meta Description
                        item.setSeoDescription(ShopUtils.getString(cell));
                        break;

                    case 6:	// SEO > H1
                        item.setSeoHeaderContents1(ShopUtils.getString(cell));
                        break;

                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 수정.
            itemMapperBatch.updateItemSubForExcel(item);


            rowDataCount++;

        } // row



        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + (rowDataCount + rowErrorCount)
            + ", Process:" + rowDataCount
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }

        return result;

    }


    /**
     * 답례품 답례품고시(테이블) 엑셀 시트 처리 (ITEM_TABLE)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemInfoExcelSheet(XSSFSheet sheet, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.

        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수

        for (Row row : sheet) {

            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
                        CellReference cellReference = new CellReference(cell);

                        HashMap<String, String> cellInfo = new HashMap<>();
                        cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
                        cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

                        cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }

            // 등록, 수정인 경우
            int cellErrorCount = 0;
            boolean isDelete = false;		// 옵션 삭제 후 신규로 등록하는데.. 삭제하는 경우 체크.
            String control = "D";

            Item item = new Item();
            ItemInfo itemInfo = new ItemInfo();
            List<ItemInfo> itemInfos = new ArrayList<>();

            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));


                switch (cell.getColumnIndex()) {
                    case 0: 	// 답례품코드

                        String itemUserCode = ShopUtils.getString(cell);

                        // 답례품코드가 입력되지 않은 경우
                        if (ObjectUtils.isEmpty(itemUserCode)) {
                            executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                            cellErrorCount++;
                            continue;
                        } else {

                            // 답례품코드 존재여부
                            try {

                                int itemId = 0;

                                for (int i=0; i<oldItems.size(); i++) {
                                    if ("N".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        itemId = oldItems.get(i).getItemId();
                                        itemUserCode = oldItems.get(i).getItemUserCode();
                                        control = "N";
                                    } else if ("U".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        control = "U";
                                    }
                                }

                                if ("U".equals(control)) {
                                    itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
                                }

                                item.setItemId(itemId);

                            } catch(BindingException e) {
                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));
                                cellErrorCount++;
                                continue;
                            }


                        }

                        break;

                    case 1: 	// 답례품명
                        break;

                    case 2:		// 답례품의 답례품군
                        item.setItemNoticeCode(ShopUtils.getString(cell));

                        if ("N".equals(control)) {
                            List<ItemNotice> itemNoticeList = this.getItemNoticeListByCode(item.getItemNoticeCode());
                            itemInfo = new ItemInfo();

                            for (int i=0; i<itemNoticeList.size(); i++) {
                                itemInfo.setItemId(item.getItemId());
                                itemInfo.setItemInfoId(sequenceService.getId("OP_ITEM_INFO"));
                                itemInfo.setTitle(itemNoticeList.get(i).getNoticeTitle());
                                itemInfo.setDescription("상세정보 별도표기");
                                itemInfo.setInfoCode("IMSI0001");

                                itemMapperBatch.insertItemInfo(itemInfo);
                            }
                        }
                        break;

                    default:
                        if ("U".equals(control)) {
                            if (cell.getColumnIndex() >= 3 && cell.getColumnIndex() <= 42) {
                                if (cell.getColumnIndex() % 2 == 1) {
                                    itemInfo = null;
                                    if (!"".equals(ShopUtils.getString(cell))) {
                                        itemInfo = new ItemInfo();
                                        itemInfo.setTitle(ShopUtils.getString(cell));
                                    }
                                }

                                if (cell.getColumnIndex() % 2 == 0) {
                                    if (itemInfo != null) {
                                        itemInfo.setItemId(item.getItemId());
                                        itemInfo.setItemInfoId(sequenceService.getId("OP_ITEM_INFO"));
                                        itemInfo.setInfoCode("IMSI0001");
                                        itemInfo.setDescription(ShopUtils.getString(cell));

                                        itemInfos.add(itemInfo);
                                    }
                                }
                            }
                        }

                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }

            if (!itemInfos.isEmpty()) {
                // 해당 답례품의 답례품정보(테이블) 모두 삭제.
                itemMapperBatch.deleteItemInfoByItemId(item.getItemId());
                itemMapperBatch.insertItemInfoListForExcel(itemInfos);
            }

            itemMapperBatch.updateItemContentByItemIdForExcel(item);
            rowDataCount++;

        } // row

        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }
        return result;
    }



    /**
     * 답례품 상세설명(테이블) 엑셀 시트 처리 (ITEM_INFO)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemInfoMobileExcelSheet(XSSFSheet sheet) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.

        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수


        int oldItemId = 0;

        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
                        CellReference cellReference = new CellReference(cell);

                        HashMap<String, String> cellInfo = new HashMap<>();
                        cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
                        cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

                        cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;


            Item item = new Item();
            ItemInfo itemInfo = new ItemInfo();
            List<ItemInfo> itemInfos = new ArrayList<>();


            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));


                switch (cell.getColumnIndex()) {
                    case 0: 	// 답례품코드

                        String itemUserCode = ShopUtils.getString(cell);

                        // 답례품코드가 입력되지 않은 경우
                        if (itemUserCode == null || "".equals(itemUserCode)) {
                            if (oldItemId == 0) {
                                executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                                cellErrorCount++;
                                continue;
                            }
                        } else {

                            // 답례품코드 존재여부
                            int itemId = 0;

                            try {
                                itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
                                item.setItemId(itemId);

                            } catch(BindingException e) {
                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));
                                cellErrorCount++;
                                continue;
                            }


                        }

                        break;

                    case 1: 	// 답례품명
                        break;

                    case 2:	// 답례품상세설명 (모바일)
                        item.setDetailContentMobile(ShopUtils.getString(cell));
                        break;

                    default:
						/*
						if (cell.getColumnIndex() >= 4 && cell.getColumnIndex() <= 43) {
							if (cell.getColumnIndex() % 2 == 0) {
								itemInfo = new ItemInfo();
								if (!"".equals(ShopUtils.getString(cell))) {
					    			itemInfo.setTitle(ShopUtils.getString(cell));
								}
							}

							if (cell.getColumnIndex() % 2 == 1) {
								if (!"".equals(itemInfo.getTitle())) {
									itemInfo.setItemId(item.getItemId());
									itemInfo.setItemInfoId(sequenceService.getId("OP_ITEM_INFO_MOBILE"));
					    			itemInfo.setInfoCode("IMSI0002");
					    			itemInfo.setDescription(ShopUtils.getString(cell));

					    			itemInfos.add(itemInfo);
								}
							}
						}
						*/
                        break;
                }





            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }

            // 답례품 이미지 신규 등록
            if (!itemInfos.isEmpty()) {

                // 해당 답례품의 답례품정보(테이블) 모두 삭제.
                itemMapperBatch.deleteItemInfoMobileByItemId(item.getItemId());

                itemMapperBatch.insertItemInfoMobileListForExcel(itemInfos);
            }




            itemMapperBatch.updateItemContentMobileByItemIdForExcel(item);
            rowDataCount++;

        } // row




        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }
        return result;
    }


    /**
     * 답례품 옵션 엑셀 시트 처리 (ITEM_OPTION)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemOptionExcelSheet(XSSFSheet sheet, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        // 수정, 삭제 인 경우 Batch로 처리
        List<ItemOption> itemOptions = new ArrayList<>();
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수

        List<Integer> deleteItemIds = new ArrayList<>();

        String oldOptionName1 = "";
        String oldOptionDisplayType = "";
        String oldOptionHideFlag = "";

        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
                        CellReference cellReference = new CellReference(cell);

                        HashMap<String, String> cellInfo = new HashMap<>();
                        cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
                        cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

                        cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;
            boolean optionDeleteFlag = false;		// 옵션 삭제 후 신규로 등록하는데.. 삭제하는 경우 체크.

            String itemUserCode = ShopUtils.getString(row.getCell(0));

            ItemOption itemOption = new ItemOption();
            itemOption.setOptionDisplayType("select");
            itemOption.setOptionHideFlag("N");
            itemOption.setCreatedUserId(UserUtils.getUserId());

            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {
                    case 0: 	// 답례품코드

                        // 답례품코드가 입력되지 않은 경우
                        if (ObjectUtils.isEmpty(itemUserCode)) {
                            executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                            cellErrorCount++;
                            continue;
                        } else {

                            // 답례품코드 존재여부
                            int itemId = 0;
                            String control = "N";

                            try {
                                for (int i=0; i<oldItems.size(); i++) {
                                    if ("N".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        itemId = oldItems.get(i).getItemId();
                                        itemUserCode = oldItems.get(i).getItemUserCode();
                                        control = "N";
                                    } else if ("U".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        control = "U";
                                    }
                                }

                                if ("U".equals(control)) {
                                    itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
                                }
                            } catch(BindingException e) {
                                log.error("itemMapper.getItemIdByItemUserCode( Exception : {}", "BindingException");
                                // itemUserCode로 조회한 데이터가 Null인 경우 int로 바인딩 할 수 없음.
                                // 이런 경우는 답례품이 존재하지 않는 것으로 판단하자.!
                            }

                            if (itemId == 0) {
                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));

                                cellErrorCount++;
                                continue;
                            }

                            // 기존 답례품 옵션 삭제..
                            optionDeleteFlag = true;		// 답례품코드란에 값이 입력된 경우.

                            itemOption.setItemId(itemId);
                            deleteItemIds.add(itemId);
                        }

                        break;

                    case 1: 	// 답례품명
                        break;

                    case 2: 	// 답례품옵션 형태
                        if ("S".equals(ShopUtils.getString(cell)) || "S2".equals(ShopUtils.getString(cell))
                            || "S3".equals(ShopUtils.getString(cell)) || "T".equals(ShopUtils.getString(cell))) {
                            itemOption.setOptionType(ShopUtils.getString(cell));
                        } else {
                            executionLog.append(PoiUtils.log(cellReference, "잘못된 답례품옵션 형태입니다."));

                            cellErrorCount++;
                            continue;
                        }
                        break;

                    case 3:		// 옵션명1
                        String optionName1 = ShopUtils.getString(cell);

                        if (ObjectUtils.isEmpty(optionName1)) {
                            executionLog.append(PoiUtils.log(cellReference, "옵션명1이 입력되지 않았습니다."));

                            cellErrorCount++;
                            continue;
                        }

                        if (!ObjectUtils.isEmpty(optionName1)) {
                            itemOption.setOptionName1(optionName1);
                            // oldOptionName1 = optionName1;
                        }
                        break;

                    case 4:		// 옵션명2
                        String optionName2 = ShopUtils.getString(cell);

                        // 답례품명이 비어있는 경우 - 조건에 따른 처리 (삭제를 위한 조건 - 답례품 번호를 제외한 나머지 항목이 모두 비어 있을 때.)
                        if (ObjectUtils.isEmpty(optionName2)) {

                            // 삭제 데이터?
                            boolean isDeleteOption = true;
                            for (int i = 2; i <= 11; i++) {
                                if (!ObjectUtils.isEmpty(ShopUtils.getString(row.getCell(i)))) {
                                    isDeleteOption = false;
                                    break;
                                }
                            }

                            // 삭제데이터가 아닌 경우는 에러.
                            if (!(!ObjectUtils.isEmpty(optionName2) && isDeleteOption) && !"T".equals(itemOption.getOptionType())) {
                                executionLog.append(PoiUtils.log(cellReference, "옵션명2가 입력되지 않았습니다."));

                                cellErrorCount++;
                                continue;
                            }
                        }

                        itemOption.setOptionName2(ShopUtils.getString(cell));
                        break;

                    case 5:		// 옵션명3
                        if (ObjectUtils.isEmpty(ShopUtils.getString(cell)) && "S3".equals(itemOption.getOptionType().toUpperCase())) {
                            executionLog.append(PoiUtils.log(cellReference, "옵션명3이 입력되지 않았습니다."));

                            cellErrorCount++;
                            continue;
                        } else {
                            itemOption.setOptionName3(ShopUtils.getString(cell));
                        }
                        break;

                    case 6:		// 추가금액
                        itemOption.setOptionPrice(ShopUtils.getInt(cell));
                        break;

                    case 7:		// 재고연동
                        itemOption.setOptionStockFlag("N".equalsIgnoreCase(ShopUtils.getString(cell)) ? "N" : "Y");
                        break;

                    case 8:		// 재고수량
                        if (itemOption != null && itemOption.getOptionStockFlag() != null
                            && "Y".equals(itemOption.getOptionStockFlag().toUpperCase())) {
                            itemOption.setOptionStockQuantity(ShopUtils.getInt(cell));
                        }
                        break;

                    case 9:	// 판매상태
                        itemOption.setOptionSoldOutFlag("Y".equalsIgnoreCase(ShopUtils.getString(cell)) ? "Y" : "N");
                        break;

                    case 10:	// 노출여부
                        itemOption.setOptionDisplayFlag("N".equalsIgnoreCase(ShopUtils.getString(cell)) ? "N" : "Y");
                        break;

                    case 11:	// 관리코드
                        itemOption.setOptionStockCode(ShopUtils.getString(cell));
                        break;

                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }

            // 답례품 옵션 모두 삭제.
            if (optionDeleteFlag) {
                itemMapperBatch.deleteItemOptionByItemId(itemOption.getItemId());
            }

            if("T".equals(itemOption.getOptionType().toUpperCase()) && itemOption.getItemId() != 0 && !"".equals(itemOption.getOptionName1())) {
                itemOption.setItemOptionId(sequenceService.getId("OP_SHOP_ITEM_OPTION"));
                itemOption.setCreatedUserId(UserUtils.getUserId());
                itemOption.setOptionDisplayType("text");

                itemOptions.add(itemOption);
            }

            // 옵션 등록 처리. (답례품코드가 있고 옵션 답례품명이 없는 경우를 제외하고 모두 등록처리)
            if (itemOption.getItemId() != 0 && !"".equals(itemOption.getOptionName2())) {
                itemOption.setItemOptionId(sequenceService.getId("OP_ITEM_OPTION"));
                itemOption.setCreatedUserId(UserUtils.getUserId());

                itemOptions.add(itemOption);
                //temMapperBatch.insertItemOption(itemOption);

            }

            rowDataCount++;

        } // row


        // 답례품 옵션 신규 등록
        if (!itemOptions.isEmpty()) {
            itemMapperBatch.insertItemOptionListForExcel(itemOptions);
        }

        // 답례품 ITEM_OPTION_FLAG 상태 업데이트.
        for (Integer itemId : deleteItemIds) {
            int matchCount = 0;
            for (ItemOption option : itemOptions) {
                if (option.getItemId() == itemId) {
                    matchCount++;
                    break;
                }
            }

            Item item = new Item();
            item.setItemId(itemId);

            if (matchCount == 0) {		// 삭제
                item.setItemOptionFlag("N");
            } else {
                item.setItemOptionFlag("Y");
            }

            itemMapperBatch.updateItemOptionFlag(item);

            // OP_SHOP_ITEM 테이블의 OPTION 관련 컬럼을 UPDATE
            for (ItemOption option : itemOptions) {
                if (option.getItemId() == itemId) {
                    item.setItemOptionType(option.getOptionType());
                }
				/*item.setItemOptionTitle1(option.getOptionName1());
			item.setItemOptionTitle2(option.getOptionName2());
			item.setItemOptionTitle3(option.getOptionName3());*/

                // 옵션 재고 사용시 답례품 재고 사용 안함
                if ("Y".equals(option.getOptionStockFlag())) {
                    item.setStockFlag("N");
                }

                itemMapperBatch.updateItemOptionFlag(item);
            }
        }


        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }
        return result;
    }


    /**
     * 답례품 이미지 엑셀 시트 처리 (ITEM_IMAGE)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemImageExcelSheet(XSSFSheet sheet, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        List<ItemImage> itemImages = new ArrayList<>();
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수

        List<Integer> deleteItemIds = new ArrayList<>();

        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
                        CellReference cellReference = new CellReference(cell);

                        HashMap<String, String> cellInfo = new HashMap<>();
                        cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
                        cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

                        cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;
            boolean isDelete = false;		// 옵션 삭제 후 신규로 등록하는데.. 삭제하는 경우 체크.

            ItemImage itemImage = new ItemImage();

            for (Cell cell : row) {
            	if (cell != null) {
	                HashMap<String, String> cellReference = null;
	                try {
	                    cellReference = cellReferences.get(cell.getColumnIndex());
	                } catch (RuntimeException e) {
	                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
	                }
	                if (cellReference == null) {
	                    continue;
	                }
	                cellReference.put("rowIndex", Integer.toString(rowIndex));

	                switch (cell.getColumnIndex()) {
	                    case 0: 	// 답례품코드

	                        String itemUserCode = ShopUtils.getString(cell);

	                        // 답례품코드가 입력되지 않은 경우
	                        if (itemUserCode == null || "".equals(itemUserCode)) {
	                            executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

	                            cellErrorCount++;
	                            continue;
	                        } else {

	                            // 답례품코드 존재여부
	                            int itemId = 0;
	                            String control = "N";

	                            try {

	                                for (int i=0; i<oldItems.size(); i++) {
	                                    if ("N".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
	                                        itemId = oldItems.get(i).getItemId();
	                                        itemUserCode = oldItems.get(i).getItemUserCode();
	                                        control = "N";
	                                    } else if ("U".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
	                                        control = "U";
	                                    }
	                                }

	                                if ("U".equals(control)) {
	                                    itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
	                                }

	                                itemImage.setItemId(itemId);
	                            } catch(BindingException e) {
	                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));
	                                cellErrorCount++;
	                                continue;
	                            }


	                            // 답례품코드 중복체크 (엑셀 데이터) - 신규 코드가 입력되었지만 기존에 이미 사용 중인 경우는 삭제 처리 하지 않음.
	                            boolean isDuplicationItemId = false;
	                            for (ItemImage data : itemImages) {
	                                if (data.getItemId() == itemId) {
	                                    isDuplicationItemId = true;
	                                    break;
	                                }
	                            }

	                            // 답례품코드란에 값이 입력된 경우.
	                            if (!isDuplicationItemId) {
	                                isDelete = true;			// 기존 답례품 옵션 삭제..
	                            }

	                            itemImage.setItemId(itemId);
	                            deleteItemIds.add(itemId);
	                        }

	                        break;

	                    case 1: 	// 답례품명
	                        break;

	                    case 2: 	// 이미지명

	                        String imageName = ShopUtils.getString(cell);

	                        if(ObjectUtils.isEmpty(imageName)) {
	                            imageName = cell.getCellFormula();
	                        }

	                        if(ObjectUtils.isEmpty(imageName) && cell.getHyperlink() != null) {
	                            imageName = cell.getHyperlink().getAddress();
	                        }

	                        itemImage.setImageName(imageName);


	                        if ("".equals(itemImage.getImageName())) {
	                            executionLog.append(PoiUtils.log(cellReference, "Image name is empty!"));
	                            cellErrorCount++;
	                            continue;
	                        }

	                        if(itemImage.getImageName().contains("HYPERLINK")) {
	                            int start = imageName.indexOf('"');
	                            int end = imageName.indexOf(",");
	                            String linkedImageName = imageName.substring(start + 1, end - 1);
	                            itemImage.setImageName(linkedImageName);
	                        }

	                        break;

	                    case 3: 	// 정렬 순서
	                        String ordering = Integer.toString(ShopUtils.getInt(cell));
	                        itemImage.setOrdering("".equals(ordering) ? 0 : Integer.parseInt(ordering));

	                        break;
	                    default:
	                        break;
	                }
            	}
            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 답례품 이미지 모두 삭제.
            if (isDelete) {
                itemMapperBatch.deleteItemImageByItemId(itemImage.getItemId());
            }


            // 답례품 이미지 등록 처리. (답례품코드가 있고 옵션 답례품명이 없는 경우를 제외하고 모두 등록처리)
            if (itemImage != null && itemImage.getItemId() != 0
                && !"".equals(itemImage.getImageName())) {

                itemImage.setItemImageId(sequenceService.getId("OP_ITEM_IMAGE"));
                itemImages.add(itemImage);
                //temMapperBatch.insertItemOption(itemOption);

            }

            rowDataCount++;

        } // row


        // 답례품 이미지 신규 등록
        if (!itemImages.isEmpty()) {
            itemMapperBatch.insertItemImageListForExcel(itemImages);

            // 이미지 목록 중 첫번째는 목록 이미지로 사용.
            int previousItemId = 0;
            for (ItemImage itemImage : itemImages) {
                if (itemImage.getItemId() != previousItemId && !"".equals(itemImage.getImageName())) {
                    itemMapperBatch.updateItemImageInfoByItemImage(itemImage);
                    previousItemId = itemImage.getItemId();
                }
            }
        }

        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }
        return result;
    }

    /**
     * 답례품 카테고리 엑셀 시트 처리 (ITEM_CATEGORY)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemCategoryExcelSheet(XSSFSheet sheet, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        List<ItemCategory> itemCategories = new ArrayList<>();
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수

        List<Integer> deleteItemIds = new ArrayList<>();

        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
	                    CellReference cellReference = new CellReference(cell);

	                    HashMap<String, String> cellInfo = new HashMap<>();
	                    cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
	                    cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

	                    cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;
            boolean isDelete = false;		// 옵션 삭제 후 신규로 등록하는데.. 삭제하는 경우 체크.

            ItemCategory itemCategory = new ItemCategory();

            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {
                    case 0: 	// 답례품코드

                        String itemUserCode = ShopUtils.getString(cell);

                        // 답례품코드가 입력되지 않은 경우
                        if (itemUserCode == null || "".equals(itemUserCode)) {
                            executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                            cellErrorCount++;
                            continue;
                        } else {

                            // 답례품코드 존재여부
                            int itemId = 0;
                            String control = "N";

                            try {

                                for (int i=0; i<oldItems.size(); i++) {
                                    if ("N".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        itemId = oldItems.get(i).getItemId();
                                        itemUserCode = oldItems.get(i).getItemUserCode();
                                        control = "N";
                                    } else if ("U".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        control = "U";
                                    }
                                }

                                if ("U".equals(control)) {
                                    itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
                                }

                            } catch(BindingException e) {
                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));
                                cellErrorCount++;
                                continue;
                            }

                            // 답례품코드 중복체크 (엑셀 데이터) - 신규 코드가 입력되었지만 기존에 이미 사용 중인 경우는 삭제 처리 하지 않음.
                            boolean isDuplicationItemId = false;
                            for (ItemCategory data : itemCategories) {
                                if (data.getItemId() == itemId) {
                                    isDuplicationItemId = true;
                                    break;
                                }
                            }

                            // 답례품코드란에 값이 입력된 경우.
                            if (!isDuplicationItemId) {
                                isDelete = true;			// 기존 답례품 카테고리 삭제..
                            }

                            itemCategory.setItemId(itemId);
                            deleteItemIds.add(itemId);
                        }

                        break;

                    case 1: 	// 답례품명
                        break;

                    case 2: 	// 카테고리 코드
                        String categoryUrl = ShopUtils.getString(cell);

                        if ("".equals(categoryUrl)) {
                            executionLog.append(PoiUtils.log(cellReference, "Category code is empty!"));
                            cellErrorCount++;
                            continue;
                        }

                        // CategoryId 조회
                        int categoryId = 0;
                        try {
                            categoryId = categoriesMapper.getCategoryIdByCategoryUrl(categoryUrl);

                        } catch (BindingException e) {
                            executionLog.append(PoiUtils.log(cellReference, "Category is not exist. (" + categoryUrl + ")"));
                            cellErrorCount++;
                            continue;

                        }

                        // 동일 답례품에 중복 카테고리가 있는지 체크.
                        boolean isDuplcateCategory = false;
                        for(ItemCategory itemCate : itemCategories) {
                            if (itemCate.getItemId() == itemCategory.getItemId()
                                && itemCate.getCategoryId() == categoryId) {
                                isDuplcateCategory = true;
                                break;
                            }
                        }

                        if (isDuplcateCategory) {
                            executionLog.append(PoiUtils.log(cellReference, "There is the same category. (" + categoryUrl + ")"));
                            cellErrorCount++;
                            continue;
                        }

                        itemCategory.setCategoryId(categoryId);
                        break;

                    case 3: 	// 카테고리명
                        break;

                    case 4: 	// 정렬 순서
                        String ordering = Integer.toString(ShopUtils.getInt(cell));
                        itemCategory.setOrdering(ObjectUtils.isEmpty(ordering) ? 0 : Integer.parseInt(ordering));

                        break;
                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 답례품 이미지 모두 삭제.
            if (isDelete) {
                itemMapperBatch.deleteItemCategoryByItemId(itemCategory.getItemId());
            }


            // 답례품 이미지 등록 처리. (답례품코드가 있고 옵션 답례품명이 없는 경우를 제외하고 모두 등록처리)
            if (itemCategory.getItemId() != 0 && itemCategory.getCategoryId() != 0) {
                itemCategory.setItemCategoryId(sequenceService.getId("OP_ITEM_CATEGORY"));
                itemCategories.add(itemCategory);
            }

            rowDataCount++;

        } // row


        // 답례품 카테고리 신규 등록
        if (!itemCategories.isEmpty()) {
            itemMapperBatch.insertItemCategoryListForExcel(itemCategories);
        }

        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }

        return result;
    }


    /**
     * 답례품 관련답례품 엑셀 시트 처리 (ITEM_RELATION)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemRelationExcelSheet(XSSFSheet sheet, ArrayList<Item> oldItems) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        List<ItemRelation> itemRelations = new ArrayList<>();
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수

        List<Integer> deleteItemIds = new ArrayList<>();

        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
	                    CellReference cellReference = new CellReference(cell);

	                    HashMap<String, String> cellInfo = new HashMap<>();
	                    cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
	                    cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

	                    cellReferences.add(cellInfo);
                	}
                }
                continue;
            }

            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;
            boolean isDelete = false;		// 삭제 대상 데이터 플래그.

            ItemRelation itemRelation = new ItemRelation();

            for (Cell cell : row) {

                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {
                    case 0: 	// 답례품코드

                        String itemUserCode = ShopUtils.getString(cell);

                        // 답례품코드가 입력되지 않은 경우
                        if (ObjectUtils.isEmpty(itemUserCode)) {
                            executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));
                            cellErrorCount++;
                            continue;
                        } else {

                            // 답례품코드 존재여부 (DB)
                            int itemId = 0;
                            String control = "N";

                            try {

                                for (int i=0; i<oldItems.size(); i++) {
                                    if ("N".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        itemId = oldItems.get(i).getItemId();
                                        itemUserCode = oldItems.get(i).getItemUserCode();
                                        control = "N";
                                    } else if ("U".equals(oldItems.get(i).getTempControl()) && ShopUtils.getString(row.getCell(0)).equals(oldItems.get(i).getTempId())) {
                                        control = "U";
                                    }
                                }

                                if ("U".equals(control)) {
                                    itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
                                }

                            } catch(BindingException e) {
                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));
                                cellErrorCount++;
                                continue;
                            }

                            // 답례품코드 중복체크 (엑셀 데이터) - 신규 코드가 입력되었지만 기존에 이미 사용 중인 경우는 삭제 처리 하지 않음.
                            boolean isDuplicationItemId = false;
                            for (ItemRelation data : itemRelations) {
                                if (data.getItemId() == itemId) {
                                    isDuplicationItemId = true;
                                    break;
                                }
                            }

                            // 답례품코드란에 값이 입력된 경우.
                            if (!isDuplicationItemId) {
                                isDelete = true;			// 기존 관련 답례품 삭제..
                            }

                            itemRelation.setItemId(itemId);
                            deleteItemIds.add(itemId);
                        }

                        break;

                    case 1: 	// 답례품명
                        break;

                    case 2: 	// 관련 답례품코드
                        String relationItemUserCode = ShopUtils.getString(cell);

                        if ("".equals(relationItemUserCode)) {
                            executionLog.append(PoiUtils.log(cellReference, "Relation product code is empty!"));
                            cellErrorCount++;
                            continue;
                        }

                        // 관련 답례품 ID조회
                        int relationItemId = 0;
                        try {
                            relationItemId = itemMapper.getItemIdByItemUserCode(relationItemUserCode);

                        } catch (BindingException e) {
                            executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + relationItemUserCode + ")"));
                            cellErrorCount++;
                            continue;
                        }

                        // 동일 답례품에 중복된 관련답례품이 있는지 체크.
                        boolean isDuplcateCategory = false;
                        for(ItemRelation itemRelate : itemRelations) {
                            if (itemRelate.getItemId() == itemRelation.getItemId()
                                && itemRelate.getRelatedItemId() == relationItemId) {
                                isDuplcateCategory = true;
                                break;
                            }
                        }

                        if (isDuplcateCategory) {
                            executionLog.append(PoiUtils.log(cellReference, "Product code is already in use. (" + relationItemUserCode + ")"));
                            cellErrorCount++;
                            continue;
                        }

                        itemRelation.setRelatedItemId(relationItemId);
                        break;

                    case 3: 	// 카테고리명
                        break;

                    case 4: 	// 정렬 순서
                        String ordering = Integer.toString(ShopUtils.getInt(cell));
                        itemRelation.setOrdering("".equals(ordering) ? 0 : ShopUtils.getInt(cell));

                        break;
                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 관련 답례품 모두 삭제.
            if (isDelete) {
                itemMapperBatch.deleteItemRelationByItemId(itemRelation.getItemId());
            }


            // 관련 답례품 등록 처리. (답례품코드가 있고 옵션 답례품명이 없는 경우를 제외하고 모두 등록처리)
            if (itemRelation.getItemId() != 0 && itemRelation.getRelatedItemId() != 0) {
                itemRelation.setItemRelationId(sequenceService.getId("OP_ITEM_RELATION"));
                itemRelations.add(itemRelation);
            }

            rowDataCount++;

        } // row


        // 관련 답례품 신규 등록
        if (!itemRelations.isEmpty()) {
            itemMapperBatch.insertItemRelationListForExcel(itemRelations);
        }


        // 답례품 ITEM_OPTION_FLAG 상태 업데이트.
        for (Integer itemId : deleteItemIds) {
            int matchCount = 0;
            for (ItemRelation itemRelation : itemRelations) {
                if (itemRelation.getItemId() == itemId) {
                    matchCount++;
                    break;
                }
            }


            Item item = new Item();
            item.setItemId(itemId);

            if (matchCount == 0) {		// 삭제
                item.setRelationItemDisplayType("1");		// 임의 출력
            } else {
                item.setRelationItemDisplayType("2");		// 관련답례품 선택
            }
            itemMapperBatch.updateRelationItemDisplayType(item);
        }


        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }

        return result;
    }


    /**
     * 답례품 포인트 설정 엑셀 시트 처리 (SHOP_POINT_CONFIG)
     * @param sheet
     */
    @SuppressWarnings("static-access")
    private String processItemPointConfigExcelSheet(XSSFSheet sheet) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();

        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        List<PointConfig> pointConfigures = new ArrayList<>();
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수

        List<Integer> deleteItemIds = new ArrayList<>();

        int oldItemId = 0;

        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
	                    CellReference cellReference = new CellReference(cell);

	                    HashMap<String, String> cellInfo = new HashMap<>();
	                    cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
	                    cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

	                    cellReferences.add(cellInfo);
                	}
                }
                continue;
            }

            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            // 등록, 수정인 경우
            int cellErrorCount = 0;
            boolean isDelete = false;		// 옵션 삭제 후 신규로 등록하는데.. 삭제하는 경우 체크.


            PointConfig pointConfig = new PointConfig();
            pointConfig.setItemId(oldItemId);


            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {
                    case 0: 	// 답례품코드

                        String itemUserCode = ShopUtils.getString(cell);

                        // 답례품코드가 입력되지 않은 경우
                        if (itemUserCode == null || "".equals(itemUserCode)) {
                            if (oldItemId == 0) {
                                executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                                cellErrorCount++;
                                continue;
                            }
                        } else {

                            // 답례품코드 존재여부
                            int itemId = 0;

                            try {
                                itemId = itemMapper.getItemIdByItemUserCode(itemUserCode);
                            } catch(BindingException e) {
                                executionLog.append(PoiUtils.log(cellReference, "Product is not exist. (" + itemUserCode + ")"));
                                cellErrorCount++;
                                continue;
                            }

                            // 답례품코드 중복체크 (엑셀 데이터) - 신규 코드가 입력되었지만 기존에 이미 사용 중인 경우는 삭제 처리 하지 않음.
                            boolean isDuplicationItemId = false;
                            for (PointConfig data : pointConfigures) {
                                if (data.getItemId() == itemId) {
                                    isDuplicationItemId = true;
                                    break;
                                }
                            }

                            // 답례품코드란에 값이 입력된 경우.
                            if (!isDuplicationItemId) {
                                isDelete = true;			// 기존 답례품 포인트 설정 삭제..
                            }

                            oldItemId = itemId;
                            pointConfig.setItemId(itemId);
                            deleteItemIds.add(itemId);
                        }

                        break;

                    case 1: 	// 답례품명
                        break;

                    case 2: 	// 적립포인트
                        pointConfig.setPoint("".equals(ShopUtils.getString(cell)) ? 0 : ShopUtils.getInt(cell));
                        break;

                    case 3: 	// 적립구분 (1:비율, 2:금액)
                        pointConfig.setPointType("2".equals(ShopUtils.getString(cell)) ? "2" : "1");
                        break;

                    case 4: 	// 적립기간 - 시작일
                        String startDate = ShopUtils.getString(cell);
                        if (!DateUtils.checkDate(startDate)) {
                            executionLog.append(PoiUtils.log(cellReference, "Date format is invalid. (input:'" + startDate + "', format:yyyymmdd(ex.20140706))"));
                            cellErrorCount++;
                            continue;
                        }
                        pointConfig.setStartDate(startDate);
                        break;

                    case 5: 	// 적립기간 - 시작시간
                        String startTime = ShopUtils.getString(cell);

                        boolean hasValidationError = false;

                        if ("".equals(startTime)) {
                            hasValidationError = true;
                        }

                        boolean isMatchTime = false;
                        for (int i = 0; i <= 23; i++) {
                            String checkString = "" + i;
                            if (i < 10) {
                                checkString = "0" + i;
                            }

                            if (checkString.equals(startTime) || Integer.toString(i).equals(startTime)) {
                                isMatchTime = true;
                                break;
                            }
                        }

                        if (!isMatchTime) {
                            hasValidationError = true;
                        }

                        if (hasValidationError) {
                            executionLog.append(PoiUtils.log(cellReference, "Time format is invalid. (input:'" + startTime + "', format:00 ~ 23)"));
                            cellErrorCount++;
                            continue;
                        }
                        pointConfig.setStartTime(startTime.length() == 1 ? "0" + startTime : startTime);
                        break;

                    case 6: 	// 적립기간 - 종료일
                        String endDate = ShopUtils.getString(cell);
                        if (!DateUtils.checkDate(endDate)) {
                            executionLog.append(PoiUtils.log(cellReference, "Date format is invalid. (input:'" + endDate + "', format:yyyymmdd(ex.20140706))"));
                            cellErrorCount++;
                            continue;
                        }
                        pointConfig.setEndDate(endDate);
                        break;

                    case 7: 	// 적립기간 - 종료시간
                        String endTime = ShopUtils.getString(cell);

                        boolean hasEndTimeError = false;

                        if ("".equals(endTime)) {
                            hasEndTimeError = true;
                        }

                        boolean isMatchEndTime = false;
                        for (int i = 0; i <= 23; i++) {
                            String checkString = "" + i;
                            if (i < 10) {
                                checkString = "0" + i;
                            }

                            if (checkString.equals(endTime) || Integer.toString(i).equals(endTime)) {
                                isMatchEndTime = true;
                                break;
                            }
                        }

                        if (!isMatchEndTime) {
                            hasEndTimeError = true;
                        }

                        if (hasEndTimeError) {
                            executionLog.append(PoiUtils.log(cellReference, "Time format is invalid. (input:'" + endTime + "', format:00 ~ 23)"));
                            cellErrorCount++;
                            continue;
                        }
                        pointConfig.setEndTime(endTime.length() == 1 ? "0" + endTime : endTime);
                        break;
					/*
					case 8: 	// 특정일
						String repeatDay = ShopUtils.getString(cell);

						if (!"".equals(repeatDay)) {
							boolean isMatchDay = false;
							for (int i = 1; i <= 31; i++) {
								String checkString = "" + i;

								if (checkString.equals(repeatDay)) {
									isMatchDay = true;
									break;
								}
							}

							if (!isMatchDay) {
								executionLog.append(PoiUtils.log(cellReference, "Day format is invalid. (input:'" + repeatDay + "', format:1 ~ 31)"));
								cellErrorCount++;
				    			continue;
							}
						}

						pointConfig.setRepeatDay(repeatDay);

						// 기간구분 (1:일반, 2:특정일)
						pointConfig.setPeriodType("".equals(repeatDay) ? "1" : "2");
					*/
                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 답례품 포인트 설정 정보 모두 삭제.
            if (isDelete) {
                itemMapperBatch.deleteItemPointConfigByItemId(pointConfig.getItemId());
            }


            // 답례품 포인트 설정 정보 등록 처리.
            if (pointConfig.getItemId() != 0) {
                pointConfig.setPointConfigId(sequenceService.getId("OP_POINT_CONFIG"));
                pointConfig.setConfigType("2");		// 2: 답례품포인트
                pointConfig.setStatusCode("1");
                pointConfig.setPeriodType("1");		// 1: 기간설정 (일반)
                pointConfig.setRepeatDay("");		// 특정일
                pointConfig.setCreatedUserId(UserUtils.getUserId());
                pointConfigures.add(pointConfig);
            }

            rowDataCount++;
        } // row


        // 답례품 포인트 설정 정보 신규 등록
        if (!pointConfigures.isEmpty()) {
            itemMapperBatch.insertItemPointConfigListForExcel(pointConfigures);
        }

        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }

        return result;
    }


    /**
     * 답례품 ITEM_CHECK
     *
     * 답례품재고, 재입고 표시 방법을 제외한 나머지 항목은 기본 값 없음.
     * 공백인 경우 업데이트 하지 않음.
     * @param sheet
     * @return
     */
    @SuppressWarnings("static-access")
    private String processItemCheckExcelSheet(XSSFSheet sheet) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();



        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        // 수정, 삭제 인 경우 Batch로 처리
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수
        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
	                    CellReference cellReference = new CellReference(cell);

	                    HashMap<String, String> cellInfo = new HashMap<>();
	                    cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
	                    cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

	                    cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            String itemUserCode = ShopUtils.getString(row.getCell(0));

            // 답례품코드가 입력되지 않은 경우
            if (itemUserCode == null || "".equals(itemUserCode)) {
                HashMap<String, String> cellReference = cellReferences.get(1);
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                rowErrorCount++;
                continue;
            }

            ExcelItemCheck item = new ExcelItemCheck();

            item.setItemUserCode(itemUserCode);



            // 등록, 수정인 경우
            int cellErrorCount = 0;
            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {

                    case 1: 	// 답례품명
                        item.setItemName(ShopUtils.getString(cell));
                        break;

                    case 2: 	// 소속팀
                        String teamNumber = ShopUtils.getString(cell);
                        if ("1".equals(teamNumber)) {			// 1. 화장품
                            item.setTeam("esthetic");

                        } else if ("2".equals(teamNumber)) {	// 2. 네일
                            item.setTeam("nail");

                        } else if ("3".equals(teamNumber)) {	// 3. 마사지
                            item.setTeam("matsuge_extension");

                        } else if ("4".equals(teamNumber)) {	// 4. 헤어
                            item.setTeam("hair");

                        } else if ("5".equals(teamNumber)) {	// 5. 세일
                            item.setTeam("sale_outlets");

                        } else if ("6".equals(teamNumber)) {	// 6. 무소속
                            item.setTeam("-");
                        }

                        break;

                    case 3: 	// 공개/비공개
                        String displayFlag = ShopUtils.getString(cell).toUpperCase();
                        if ("Y".equals(displayFlag) || "N".equals(displayFlag)) {
                            item.setDisplayFlag(displayFlag);
                        }
                        break;

                    case 4:	// SEO > NO INDEX
                        String noIndexDisplayFlag = ShopUtils.getString(cell).toUpperCase();
                        if ("Y".equals(noIndexDisplayFlag) || "N".equals(noIndexDisplayFlag)) {
                            item.setSeoNoIndexDisplayFlag(noIndexDisplayFlag);
                        }
                        break;

                    case 5:		// 답례품라벨 (0:없음, 2:NEW, 3:SALE, 4:사기)

                        String itemLabel = ShopUtils.getString(cell);
                        if ("0".equals(itemLabel) || "2".equals(itemLabel) || "3".equals(itemLabel) || "5".equals(itemLabel)) {
                            item.setItemLabel(itemLabel);

                            item.setItemNewFlag("N");		// 신답례품 여부 (정렬을 위한 컬럼)
                            if ("2".equals(itemLabel)) {
                                item.setItemNewFlag("Y");
                            }
                        }


                        break;

                    case 6:	// 답례품구분 (1:통상답례품, 2:메어커직송, 3:메일편)
                        if ("1".equals(ShopUtils.getString(cell)) || "2".equals(ShopUtils.getString(cell)) || "3".equals(ShopUtils.getString(cell))) {
                            item.setItemType(ShopUtils.getString(cell));
                        }
                        break;

                    case 7:	// 답례품재고
                        String stockQuantity = ShopUtils.getString(cell);
                        item.setStockQuantity("".equals(stockQuantity) ? -1 : Integer.parseInt(stockQuantity));
                        break;

                    case 8:	// 답례품상태-재고0일때 (1:입하대기, 2:판매종료)
                        if ("1".equals(ShopUtils.getString(cell)) || "2".equals(ShopUtils.getString(cell))) {
                            item.setItemLabelSoldOut(ShopUtils.getString(cell));
                        }
                        break;

                    case 9:	// 재입고 표시방법 - (1:'', 2:date, 3:text)
                        item.setStockScheduleType("");
                        if ("2".equals(ShopUtils.getString(cell))) {
                            item.setStockScheduleType("data");

                        } else if ("3".equals(ShopUtils.getString(cell))) {
                            item.setStockScheduleType("text");

                        }
                        break;

                    case 10:	// 입하예정일
                        item.setStockScheduleDate(ShopUtils.getString(cell));
                        break;

                    case 11:	// 입하텍스트
                        item.setStockScheduleText(ShopUtils.getString(cell));
                        break;

                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 수정.
            itemMapperBatch.updateItemCheckForExcel(item);


            rowDataCount++;

        } // row



        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" + rowDataCount
            + ", Process:" + (rowDataCount - rowErrorCount)
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }

        return result;

    }


    /**
     * 답례품 ITEM_KEYWORD  사이트내 검색어 업데이트..
     *
     * @param sheet
     * @return
     */
    @SuppressWarnings("static-access")
    private String processItemKeywordExcelSheet(XSSFSheet sheet) {
        String result = "";
        if (sheet == null) {
            return result;
        }

        StringBuffer executionLog = new StringBuffer();



        // 등록인 경우 INSER VALUE (), () 형태로 일괄 등록.
        // 수정, 삭제 인 경우 Batch로 처리
        List<HashMap<String, String>> cellReferences = new ArrayList<>();

        int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
        int rowErrorCount = 0;			// 오류 수
        for (Row row : sheet) {
            int rowIndex = row.getRowNum() + 1;

            if (row.getRowNum() < 1) {
                continue;
            }

            // 헤더 - 타이틀 가져오기
            if (row.getRowNum() == 1) {
                for (Cell cell : row) {
                	if (cell != null) {
	                    CellReference cellReference = new CellReference(cell);

	                    HashMap<String, String> cellInfo = new HashMap<>();
	                    cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
	                    cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

	                    cellReferences.add(cellInfo);
                	}
                }
                continue;
            }


            // 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
            if (PoiUtils.isEmptyAllCell(row)) {
                continue;
            }


            String itemUserCode = ShopUtils.getString(row.getCell(0));

            // 답례품코드가 입력되지 않은 경우
            if (itemUserCode == null || "".equals(itemUserCode)) {
                HashMap<String, String> cellReference = cellReferences.get(1);
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                executionLog.append(PoiUtils.log(cellReference, "Product code is empty!"));

                rowErrorCount++;
                continue;
            }

            ExcelItemKeyword item = new ExcelItemKeyword();

            item.setItemUserCode(itemUserCode);



            // 등록, 수정인 경우
            int cellErrorCount = 0;
            for (Cell cell : row) {
                HashMap<String, String> cellReference = null;
                try {
                    cellReference = cellReferences.get(cell.getColumnIndex());
                } catch (RuntimeException e) {
                	log.warn(" cellReferences.get(cell.getColumnIndex()) : {}", "RuntimeException");
                }
                if (cellReference == null) {
                    continue;
                }
                cellReference.put("rowIndex", Integer.toString(rowIndex));

                switch (cell.getColumnIndex()) {

                    case 1: 	// 사이트내 검색어.
                        item.setItemKeyword(ShopUtils.getString(cell));
                        break;


                    default:
                        break;
                }

            } // cell

            // 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
            if (cellErrorCount > 0) {
                rowErrorCount++;
                continue;
            }


            // 수정.
            itemMapperBatch.updateItemKeywordForExcel(item);


            rowDataCount++;

        } // row



        // 처리결과
        result = "\n<p class=\"sheet\"><span>[" + sheet.getSheetName() + "]</span> Total:" +  (rowDataCount + rowErrorCount)
            + ", Process:" + rowDataCount
            + ", Error:" + rowErrorCount + "</p>\n";

        if (rowErrorCount > 0) {
            result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
            result += executionLog.toString();
            result += "\n";
        }

        return result;

    }

    @Override
    public List<HashMap<String, Object>> getIndexList(
        SearchIndexParam searchIndexParam) {
        return itemMapper.getIndexList(searchIndexParam);
    }


    @Override
    public List<HashMap<String, Object>> getSubIndexList(SearchIndexParam searchIndexParam) {
        if ("".equals(searchIndexParam.getStartIndex())) {
            return searchIndexParam.getSubIndexList();
        }

        return itemMapper.getSubIndexList(searchIndexParam);
    }


    @Override
    public List<ItemIndex> getItemIndexList(SearchIndexParam searchIndexParam) {
        return itemMapper.getItemIndexList(searchIndexParam);
    }

    @Override
    public List<Item> getTodayItemList(ItemParam itemParam) {
        return itemMapper.getTodayItemList(itemParam);
    }


    @Override
    public void updateItemHitsByItemId(int itemId) {
        itemMapper.updateItemHitsByItemId(itemId);
    }

    @Override
    public List<Item> getItemListForGroupBanner(String value) {

        if (ObjectUtils.isEmpty(value)) {
            return new ArrayList<Item>();
        }

        HashMap<String, String[]> ids = new HashMap<String, String[]>();
        String[] idArray = value.split(",");

        ids.put("ids", idArray);

        return itemMapper.getItemListForGroupBanner(ids);
    }

    @Override
    public List<saleson.shop.categories.domain.Group> getGroupItemsForGroupBanner(
        List<saleson.shop.categories.domain.Group> shopCategoryGroups) {

        List<saleson.shop.categories.domain.Group> groupParam = new ArrayList<>();

        // 미설정 그룹 제외.
        for (saleson.shop.categories.domain.Group group : shopCategoryGroups) {
            if (!ObjectUtils.isEmpty(group.getItemList())) {
                groupParam.add(group);
            }
        }
        return itemMapper.getGroupItemsForGroupBanner(groupParam);
    }


    @Override
    public List<ItemNotice> getItemNoticeCodes() {
        return itemMapper.getItemNoticeCodes();
    }

    @Override
    public List<ItemNotice> getItemNoticeListByCode(String itemNoticeCode) {
        return itemMapper.getItemNoticeListByCode(itemNoticeCode);
    }

    @Override
    public void updateShipmentPrice(Shipment shipment) {
        itemMapper.updateShipmentPrice(shipment);
    }

    @Override
    public void updateShipment(Shipment shipment) {
        itemMapper.updateShipment(shipment);
    }

    @Override
    public void updateShipmentReturn(ShipmentReturn shipmentReturn) {
        itemMapper.updateShipmentReturn(shipmentReturn);
    }

    @Override
    public List<Item> getItemCountForMain(long sellerId){
        return itemMapper.getItemCountForMain(sellerId);
    }

    @Override
    public void insertItemSaleEdit(ItemSaleEdit itemSaleEdit) {
        itemMapper.insertItemSaleEdit(itemSaleEdit);
    };

    @Override
    public List<ItemSaleEdit> getItemSaleEdit(ItemSaleEditParam itemSaleEditParam) {
        return itemMapper.getItemSaleEdit(itemSaleEditParam);
    };

    @Override
    public int getItemSaleEditCountByParam(ItemSaleEditParam itemSaleEditParam){
        return itemMapper.getItemSaleEditCountByParam(itemSaleEditParam);
    };

    @Override
    public void deleteItemSaleEdit(ItemSaleEdit itemSaleEdit){
        itemMapper.deleteItemSaleEdit(itemSaleEdit);
    };

    @Override
    public void updateSaleEdit(ItemSaleEdit itemSaleEdit){
        itemMapper.updateSaleEdit(itemSaleEdit);
    };

    @Override
    public void updateSaleEditStatus(ItemSaleEdit itemSaleEdit){
        itemMapper.updateSaleEditStatus(itemSaleEdit);
    };

    @Override
    public ItemSaleEdit getItemSaleEditByParam(ItemSaleEditParam itemSaleEditParam){
        return itemMapper.getItemSaleEditByParam(itemSaleEditParam);
    };

    @Override
    public void updateItemPrice(ItemSaleEdit itemSaleEdit){
        itemMapper.updateItemPrice(itemSaleEdit);
    };

    @Override
    public Item getItemByItemSaleEdit(ItemSaleEdit itemSaleEdit){
        return itemMapper.getItemByItemSaleEdit(itemSaleEdit);
    };

    public List<ChosenItem> getChosenItemList(List<String> list){
        return itemMapper.getChosenItemList(list);
    }

    @Override
    public List<ChosenItem> getSearchItemList(ChosenItem chosenItem) {

        return itemMapper.getSearchItemList(chosenItem);
    }

    @Override
    public Integer getItemIdByItemUserCode(String itemUserCode){
        return itemMapper.getItemIdByItemUserCode(itemUserCode);
    };

    //kye 추가
    @Override
    public int getItemNonregisteredReviewCount(ItemParam itemParam) {
        return itemMapper.getItemNonregisteredReviewCount(itemParam);
    }

    //kye 추가
    @Override
    public List<OrderItem> getItemNonregisteredReviewList(ItemParam itemParam) {
        return itemMapper.getItemNonregisteredReviewList(itemParam);
    }

    @Override
    public void updateItemOptionSoldout() {
        // 답례품 옵션 품절정보 삭제.
        itemMapper.deleteItemOptionSoldout();

        // 답례품 옵션 품절정보 등록
        itemMapper.insertItemOptionSoldout();

        // 답례품 옵션이 모두 재고가 0일 경우 삭제
        itemMapper.deleteSoldoutItemOption();
    }

    @Override
    public List<ItemRelation> getItemRelationsByItemId(String relationItemDisplayType, int itemId) {
    	// 불필요, 속도 저하 판단되어 주석처리 해봄
//        if (ObjectUtils.isEmpty(relationItemDisplayType)) {
//            relationItemDisplayType = "1";
//        }
//
//        ItemParam itemParam = ItemUtils.bindItemParam(new ItemParam());
//        itemParam.setItemId(itemId);
//        List<ItemRelation> itemRelations = itemMapper.getItemRelationList(itemParam);
//
//        // 관련답례품 임의 출력인 경우 동일 카테고리 답례품 5개 조회.
//        if ("1".equals(relationItemDisplayType) && itemRelations.size() == 0) {
//            itemParam.setItemId(itemId);
//            itemRelations = itemMapper.getItemRelationRandomList(itemParam);
//        }
//
//        return itemRelations;
    	return new ArrayList<>();
    }

    @Override
    public BenefitInfo getBenefitInfoByItemId(int itemId){

        BenefitInfo benefitInfo = new BenefitInfo();

        Config shopConfig = ShopUtils.getConfig();

        // 지급 포인트 정보
        OrderPointParam orderPointParam = new OrderPointParam();
        orderPointParam.setItemId(itemId);
        orderPointParam.setRepeatDayEndTime(shopConfig.getRepeatDayEndTime());
        orderPointParam.setRepeatDayStartTime(shopConfig.getRepeatDayStartTime());

        PointPolicy pointPolicy =  pointService.getPointPolicyByOrderPointParam(orderPointParam);

        benefitInfo.setPointPolicy(pointPolicy);
        benefitInfo.setCardBenefits(cardBenefitsService.getTodayCardBenefits(DateUtils.getToday()));

        benefitInfo.setItemEarnPoint(new ItemEarnPoint(pointPolicy, getItemBy(itemId)));

        return benefitInfo;
    }

    @Override
    public CustomerInfo getCustomerInfoByItemId(int itemId){

        // 답례품리뷰
        ItemParam itemReviewParam = new ItemParam();
        itemReviewParam.setItemId(itemId);
        itemReviewParam.setConditionType("FRONT_ITEM_DETAIL");

        int reviewCount = getItemReviewCountByParam(itemReviewParam);

        // QNA 목록 가져오기
        QnaParam qnaParam = new QnaParam();
        qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
        qnaParam.setItemId(itemId);
        int qnaCount = qnaService.getQnaListCountByParam(qnaParam);

        CustomerInfo customerInfo = new CustomerInfo();

        customerInfo.setQnaCount(qnaCount);
        customerInfo.setReviewCount(reviewCount);

        return customerInfo;
    }

    @Override
    public void setDownloadableCouponListPagination(UserCouponParam userCouponParam) {
        // LIMIT 값이 존재하면 페이징 처리 안함
        if (userCouponParam.getLimit() <= 0) {

            int count  = couponService.getUserDownloadableCouponListCountByParam(userCouponParam);

            Pagination pagination = Pagination.getInstance(count, userCouponParam.getItemsPerPage());

            ShopUtils.setPaginationInfo(pagination, userCouponParam.getConditionType(), userCouponParam.getPage());

            userCouponParam.setPagination(pagination);

        }
    }

    @Override
    public boolean saveItemReviewLike(HttpServletRequest request, int itemReviewId) {

        boolean saveFlag = false;
        String ip = saleson.common.utils.CommonUtils.getClientIp(request);
        long userId = UserUtils.getUserId();

        ItemReviewLikeDto dto = new ItemReviewLikeDto();
        dto.setItemReviewId(itemReviewId);

        if (UserUtils.isUserLogin()) {
            dto.setUserId(userId);
        } else {
            dto.setIp(ip);
        }

        saveFlag = itemReviewLikeRepository.count(dto.getPredicate()) == 0;

        if (saveFlag) {
            ItemReviewLike like = new ItemReviewLike();

            like.setItemReviewId(itemReviewId);
            like.setIp(ip);
            like.setUserId(userId);

            itemReviewLikeRepository.save(like);
            itemMapper.updateItemReviewLikeCount(itemReviewId);

            return true;
        }

        return false;
    }

    @Override
    public List<Item> getItemListForItemSet(List<Integer> ids) {
        return itemMapper.getItemListForItemSet(ids);
    }

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void deleteItemReviewList(ListParam listParam) throws RuntimeException {
		try {
			for (String idStr : listParam.getId()) {
				int id = Integer.valueOf(idStr);
				deleteItemReview(id);
			}
		} catch (NumberFormatException e) {
//			throw new RuntimeException(MessageUtils.getMessage("삭제에 실패했습니다."));
			throw new RuntimeException("삭제에 실패했습니다.");
		}
	}

	@Override
	public SellerUser getLoginSellerInfo(long userId) {
		return itemMapper.getLoginSellerInfo(userId);
	}

	@Override
	public List<Integer> getSeasonFoodItem(int itemId) {
		return itemMapper.getSeasonFoodItem(itemId);
    }

	@Override
	public List<ItemImageExplain> getItemImagesExplain(int itemId) {
		List<ItemImageExplain> item = itemMapper.getItemImagesExplain(itemId);
		return item;
    }

	@Override
	public void insertLclgvRprsGds(ItemParam param, boolean isInsert) {
		long userId = UserUtils.getUser().getUserId();
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		ItemParam itemParam = new ItemParam();
		if (StringUtils.hasLength(locgovCode)) {
			itemParam.setLocgov(locgovCode);
			itemMapper.deleteLclgvRprsGds(itemParam);

			if (isInsert) {
				int[] itemIds = getLocgovItemIds(param, locgovCode);

				if (itemIds != null) {
					itemParam.setItemIds(itemIds);
					itemParam.setUserId(userId);
					itemMapper.insertLclgvRprsGds(itemParam);
				}
			}
		}
	}

	/**
	 * 지자체에 속한 답례품 아이디만 추출
	 * @param param
	 * @param locgovCode
	 * @return
	 */
	private int[] getLocgovItemIds(ItemParam param, String locgovCode) {
		List<Item> list = new ArrayList<>();
		ItemParam itemParam = new ItemParam();
		itemParam.setLocgov(locgovCode);
		for (int itemId : param.getItemIds()) {			// 지자체 답례품인지 확인
			itemParam.setItemId(itemId);
			Item item = itemMapper.getItemByParam(itemParam);
			if (item != null && item.getItemId() > 0) {
				list.add(item);
			}
		}

		if (!list.isEmpty()) {
			int length = list.size();
			int[] ids = new int[length];
			for (int i = 0 ; i < length ; i++) {
				ids[i] = list.get(i).getItemId();
			}
			return ids;
		}
		return null;
	}


	@Override
	public List<Item> getLclgvRprsGdsList(long userId) {
		return itemMapper.getLclgvRprsGdsList(userId);
	}

	@Override
	public List<Item> getLclgvRprsGdsListManager(String lclgvCd) {
		return itemMapper.getLclgvRprsGdsListManager(lclgvCd);
	}

	/**
	 * 오프라인 대표 답례품 등록
	 * */
	@Override
	public void insertLclgvOffRprsGds(List<Map<String, String>> optionList, boolean isInsert) {
		long userId = UserUtils.getUser().getUserId();
		String locgovCode = locgovService.getLocgovCodeByOpId(userId, IdType.MANAGER);
		ItemParam itemParam = new ItemParam();
		// itemId를 배열에 추가
		int length = optionList.size();
		int[] itemIdsTmp = new int[length];
		int idx = 0;
		for(Map<String, String> option : optionList) {
			itemIdsTmp[idx++] = Integer.parseInt(option.get("itemId"));
		}
		if (StringUtils.hasLength(locgovCode)) {
			itemParam.setLocgov(locgovCode);
			// LOCGOV가 일치한 오프라인 대표 답례품 정보를 모두 삭제
			itemMapper.deleteLclgvOffRprsGds(itemParam);

			if (isInsert) {
				itemParam.setItemIds(itemIdsTmp);
				// 검증 로직
				// itemIdsTmp에 있는 itemId가 해당 지자체 상품이 맞는지 검증하는 메서드
				int[] itemIds = getLocgovItemIds(itemParam, locgovCode);

				if (itemIds != null) {
					List<Map<String, String>> checkOptionList = new ArrayList<>();
					// 검증을 한 itemIds와 일치한 itemId의 map정보만 checkOptionList에 추가
					// insert를 위한 지자체 코드(locgov)와 유저 아이디(userId)를 Map에 추가
					for(int i = 0; i < itemIds.length; i++) {
						for(Map<String, String> map : optionList) {
							if(itemIds[i] == Integer.parseInt(map.get("itemId"))) {
								map.put("locgov", locgovCode);
								map.put("userId", new String("" + userId));
								checkOptionList.add(map);
							}
						}
					}
					itemMapper.insertLclgvOffRprsGds(checkOptionList);
				}
			}
		}
	}

	/**
	 * 오프라인 대표 답례품 조회 - 지자체용
	 * @param userId
	 * return
	 * */
	@Override
	public List<Item> getLclgvOffRprsGdsList(long userId) {
		List<Item> list = itemMapper.getLclgvOffRprsGdsList(userId);
		// 오프라인 답례품 테이블의 답례품 옵션값을 기준으로 OP_ITEM_OPTION 테이블의 답례품 옵션 조회

		Map<String, Object> s = new HashMap<String, Object>(); // 옵션조회할 조건 데이터
		Map<String, Object> sResult = new HashMap<String, Object>(); // 조회한 옵션 데이터
		if(list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				String option = (String) list.get(i).getOptions();

				if(option != null && !option.isEmpty()) {
					String[] sp = option.split("\\|\\|");

					//	option_type : S
					if(sp.length == 4) {
						s.put("gdsId", list.get(i).getItemId());
						s.put("optionType", sp[0]);
						s.put("optionName2", sp[2]);
						s.put("optionPrice", sp[3]);
					};

					// option_type : S3
					if(sp.length == 8) {
						s.put("gdsId", list.get(i).getItemId());
						s.put("optionType", sp[0]);
						s.put("optionName1", sp[2]);
						s.put("optionName2", sp[4]);
						s.put("optionName3", sp[6]);
						s.put("optionPrice", sp[7]);
					}

					// 분리한 option 값으로 option 상세 조회
					sResult = offgiveMapper.selectOffRprsOption(s);

					if (sResult != null) {
						// List 순회하면서 가져온 Object 추가 하기
						list.get(i).setItemOptionId(String.valueOf(sResult.get("item_option_id")));
						list.get(i).setOptionStockQuantity(new String[]{String.valueOf(sResult.get("option_stock_quantity"))});
						list.get(i).setChangedOption("N");
					} else {
						list.get(i).setChangedOption("Y");
					}
				} else {
					// option이 없음
					list.get(i).setChangedOption("N");

				}
	  		}
		} else {
			list = null;
		}

		return list;
	}
	/**
	 * 오프라인 대표 답례품 조회 - 시스템 관리자용
	 * @param lclgvCd
	 * return
	 * */
	@Override
	public List<Item> getLclgvOffRprsGdsListManager(String lclgvCd) {
		List<Item> list = itemMapper.getLclgvOffRprsGdsListManager(lclgvCd);
		// 오프라인 답례품 테이블의 답례품 옵션값을 기준으로 OP_ITEM_OPTION 테이블의 답례품 옵션 조회

		Map<String, Object> s = new HashMap<String, Object>(); // 옵션조회할 조건 데이터
		Map<String, Object> sResult = new HashMap<String, Object>(); // 조회한 옵션 데이터
		if(list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				String option = (String) list.get(i).getOptions();

				if(option != null && !option.isEmpty()) {
					String[] sp = option.split("\\|\\|");

					//	option_type : S
					if(sp.length == 4) {
						s.put("gdsId", list.get(i).getItemId());
						s.put("optionType", sp[0]);
						s.put("optionName2", sp[2]);
						s.put("optionPrice", sp[3]);
					};

					// option_type : S3
					if(sp.length == 8) {
						s.put("gdsId", list.get(i).getItemId());
						s.put("optionType", sp[0]);
						s.put("optionName1", sp[2]);
						s.put("optionName2", sp[4]);
						s.put("optionName3", sp[6]);
						s.put("optionPrice", sp[7]);
					}

					// 분리한 option 값으로 option 상세 조회
					sResult = offgiveMapper.selectOffRprsOption(s);

					if (sResult != null) {
						// List 순회하면서 가져온 Object 추가 하기
						list.get(i).setItemOptionId(String.valueOf(sResult.get("item_option_id")));
						list.get(i).setOptionStockQuantity(new String[]{String.valueOf(sResult.get("option_stock_quantity"))});
						list.get(i).setChangedOption("N");
					} else {
						list.get(i).setChangedOption("Y");
					}
				} else {
					// option이 없음
					list.get(i).setChangedOption("N");

				}
	  		}
		} else {
			list = null;
		}

		return list;
	}

	/**
	 * 답례품 조회
	 * @param itemId
	 * return
	 * */
	@Override
	public Item getItem(Integer itemId) {
		return itemMapper.getItem(itemId);
	}

	/**
	 * 답례품 옵션 조회
	 * @param itemId
	 * return
	 * */
	@Override
	public List<ItemOption> getItemOption(Item item) {
		return itemMapper.getItemOption(item);
	}

	/**
	 * 답례품 조합형 옵션 종류 및 가격 조회
	 *
	 * @param optionMap
	 * @return
	 */
	@Override
	public List<Map<String, String>> getComboOptionList(Map<String, Object> optionMap) {
		return itemMapper.getComboOptionList(optionMap);
	}

	/**
	 * 오프라인 답례품의 선택한 옵션 조회
	 *
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getSelectOption(Integer itemId) {
		return itemMapper.getSelectOption(itemId);
	}

	/**
	 * 상품코드로 상품을 조회(답례품승인관리 미리보기)
	 *
	 * @param itemUserCode
	 * @return
	 * @throws Exception
	 */
	@Override
    public Item getItemByItemUserCodePreview(String itemUserCode) {

        ItemParam itemParam = new ItemParam();
        itemParam.setItemUserCode(itemUserCode);

        Item item = itemMapper.getItemByParamPreview(itemParam);

        bindItemAdditionInfo(item);

        return item;
    }

	/**
	 * 스트리밍 엑셀다운로드
	 *
	 * @param	itemParam
	 * @throws	Exception
	 */
	@Override
	@Transactional(readOnly = true)
	public SXSSFWorkbook streamReviewData(ItemParam itemParam) throws Exception {
		// Cursor<DTO>가 스트리밍 방식으로 사용하기에는 적절하나, CUBRID DB에서는 사용이 불가
		// JDBC API 기반의 데이터 접근 방식 : Cursor
		int pageSize	= 0;
		int offset		= 0;
		int rowNum		= 0;
		int limit		= 0;

		// 전체 데이터 count
		itemParam.getPagination().setItemsPerPage(pageSize);
		itemParam.getPagination().setCurrentPage(offset);

		int totalCount = getItemReviewCountByParam(itemParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// Cursor 를 이용한 스트리밍 방식의 엑셀 다운로드 불가
		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize 		= 1000;
		offset 			= 1;

		itemParam.getPagination().setItemsPerPage(pageSize);
		itemParam.getPagination().setCurrentPage(offset);

		// SXSSF	: window size = 100
		// workbook	: 엑셀생성을 위한 내부 문서 모델
		// 메모리 적재 최대 100 row 로 설정, 나머지는 disk 로 flush
		// disk 저장 파일은 압축
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		// 엑셀 시트 이름 설정
		Sheet sheet = workbook.createSheet("REVIEW_DATA");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "답례품 후기 목록");
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 15000);
		sheet.setColumnWidth(4, 2500);
		sheet.setColumnWidth(5, 1500);
		sheet.setColumnWidth(6, 15000);
		sheet.setColumnWidth(7, 5000);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0, "No");
		cellStyle.header(header, 1, "지자체");
		cellStyle.header(header, 2, "공개유무");
		cellStyle.header(header, 3, "내용");
		cellStyle.header(header, 4, "작성자");
		cellStyle.header(header, 5, "평가");
		cellStyle.header(header, 6, "상품명");
		cellStyle.header(header, 7, "작성일");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {

			// 엑셀 시트 구성 데이터 조회(1000건)
			List<ItemReview> reviewList = (List<ItemReview>) itemMapper.getItemReviewListByParam(itemParam);

			if (reviewList.isEmpty()) break;

			// 1000건 데이터를 엑셀 시트에 입력 가능하도록 row 단위의 전처리 실행
			for (ItemReview itemReview : reviewList) {
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1, itemReview.getShLocgovName());
				cellStyle.data(row, 2, itemReview.getDisplayFlag());
				cellStyle.data(row, 3, itemReview.getContent());
				cellStyle.data(row, 4, itemReview.getUserName());
				cellStyle.data(row, 5, String.valueOf(itemReview.getScore()));
				cellStyle.data(row, 6, itemReview.getItem().getItemUserCode() + "_" + itemReview.getItem().getItemName());
				cellStyle.data(row, 7, itemReview.getCreatedDate());
			}

			// 다음 1000 row 조회를 위한 offset 설정
			// 쿼리문
			// LIMIT (#{pagination.currentPage} - 1) * #{pagination.itemsPerPage}, #{pagination.itemsPerPage}
			itemParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만 데이터 저장(이상은 엑셀 파일에 작성 불가)
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
	}
}

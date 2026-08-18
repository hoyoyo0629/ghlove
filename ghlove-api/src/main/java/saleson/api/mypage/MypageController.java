package saleson.api.mypage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.mypage.domain.CntrInfo;
import saleson.api.mypage.domain.CntrPointInfo;
import saleson.api.mypage.domain.IntrstLocGovInfo;
import saleson.api.mypage.domain.ItemReviewInfo;
import saleson.api.mypage.domain.ReviewFilterInfo;
import saleson.common.utils.QrUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.FilterGroup;
import saleson.model.review.ItemReviewFilter;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.item.support.ItemParam;
import saleson.shop.lclgvHnrUser.LclgvHnrUserMngService;
import saleson.shop.lclgvHnrUser.domain.HnrUserInfo;
import saleson.shop.mypage.MyPageService;
import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.mypage.domain.CntrPointDetail;
import saleson.shop.mypage.domain.IntrstLocGov;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.mypage.support.CntrReceipt;
import saleson.shop.mypage.support.IntrsLocgovParam;
import saleson.shop.mypage.support.IntrstLocGovParam;
import saleson.shop.mypage.support.QrInfo;
import saleson.shop.mypage.support.ReceiptParam;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.qna.QnaService;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.reviewfilter.ReviewFilterService;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userdelivery.UserDeliveryService;
import saleson.shop.wishlist.WishlistService;
import saleson.shop.wishlist.domain.Wishlist;
import saleson.shop.wishlist.support.WishlistListParam;
import saleson.shop.wishlist.support.WishlistParam;

@CrossOrigin(origins = "*")
@RestController("ApiMypageController")
@RequestMapping("/api/mypage")
public class MypageController {

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ReviewFilterService reviewFilterService;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private UserDeliveryService userDeliveryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LclgvHnrUserMngService lclgvHnrUserMngService;

    private Logger log = LoggerFactory.getLogger(MypageController.class);

    @GetMapping("")
    public ResponseEntity details(HttpServletRequest request, Model model){
        ResponseEntity result = null;

        try {
        	long userId = UserUtils.getUserId();
      	  	//기부내역 현황(실 납부액 기준)
        	CntrParam cntrParam = new CntrParam();
      	  	cntrParam.setUserId(userId);
      	  	int totalCntrAmt = myPageService.getTotalCntrAmt(cntrParam);

      	  	//총 잔여 포인트
      	  	CntrPointParam gntrPointParam = new CntrPointParam();
    		gntrPointParam.setUserId(userId);
            int blcePointTotal = myPageService.blcePointTotal(gntrPointParam);

            IntrstLocGovParam intrstLocGovParam = new IntrstLocGovParam();
        	intrstLocGovParam.setUserId(userId);

            //파라미터용 - common-support ex)NoticeParam

            long intrstLocGovCnt = myPageService.intrstLocGovCnt(intrstLocGovParam);		// 관심지자체 등록 수

            long favItemCnt = wishlistService.getWishlistCountByParam(new WishlistParam(userId));		// 관심답례품 등록 수

            long orderItemCnt = orderService.getOrderItemCntForMyPage(userId);

            long orderItemClaimCnt = orderService.getOrderItemClaimCntForMyPage(userId);

            ItemParam itemParam = new ItemParam();
            itemParam.setUserId(userId);
            long reviewCnt = itemService.getItemReviewCountByParam(itemParam);			// 답례품 후기 등록 수

            QnaParam qnaParam = new QnaParam();
            qnaParam.setUserId(userId);
            qnaParam.setQnaType("1");			// 답례품 문의
            long qnaCnt = qnaService.getQnaListCountByParam(qnaParam);			// 답례품 문의 등록 수

            long deliveryCnt = userDeliveryService.getDeliveryCount(userId);		// 배송지 관리 등록 수

            result = ApiResponseEntity.data()
            		.put("userName", UserUtils.getUser().getUserName())
            		.put("totalCntrAmt", totalCntrAmt)
            		.put("blcePointTotal", blcePointTotal)
            		.put("favLocgovCnt", intrstLocGovCnt)
            		.put("favItemCnt", favItemCnt)
            		.put("orderItemCnt", orderItemCnt)
            		.put("orderItemClaimCnt", orderItemClaimCnt)
            		.put("reviewCnt", reviewCnt)
            		.put("qnaCnt", qnaCnt)
            		.put("deliveryCnt", deliveryCnt)
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/intrstLocGovInfo")
    public ResponseEntity intrstLocGovInfo(IntrstLocGovParam intrstLocGovParam){
        ResponseEntity result = null;

        try {

        	if (intrstLocGovParam.getPage() <= 0) {
        		intrstLocGovParam.setPage(1);
            }

        	intrstLocGovParam.setUserId(UserUtils.getUserId());

            //파라미터용 - common-support ex)NoticeParam
            int totalCount = myPageService.intrstLocGovCnt(intrstLocGovParam);

            Pagination pagination = Pagination.getInstance(totalCount, intrstLocGovParam.getItemsPerPage());

            intrstLocGovParam.setPagination(pagination);

            // db 결과 리턴용 common-domain ex)Notice.java
            List<IntrstLocGov> list = myPageService.intrstLocGovInfo(intrstLocGovParam);

            // 화면 리턴용 api-domain ex) NoticeInfo.java
            List<IntrstLocGovInfo> infoList = new ArrayList<>();

            if(!list.isEmpty()) {
            	for(IntrstLocGov intrstLocGov : list) {
            		infoList.add(new IntrstLocGovInfo(intrstLocGov));
            	}
            }

            result = ApiResponseEntity.data()
            		.list(infoList)
            		.pagination(pagination)
            		.put("totalCnt", totalCount)
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @PostMapping("/deleteIntrstLocGov")
    public ResponseEntity deleteIntrstLocGov(@RequestBody IntrstLocGovParam intrstLocGovParam){
        ResponseEntity result = null;

        try {

        	if(!UserUtils.isUserLogin()) {
        		return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
        	}

        	intrstLocGovParam.setUserId(UserUtils.getUserId());

        	int resultCount = myPageService.deleteIntrstLocGov(intrstLocGovParam);

            result = ApiResponseEntity.data()
            		.put("resultCnt", resultCount)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/getCntrPoint")
    public ResponseEntity getGntrPoint(CntrPointParam gntrPointParam){
        ResponseEntity result = null;

        try {

        	if (gntrPointParam.getPage() <= 0) {
        		gntrPointParam.setPage(1);
            }

        	gntrPointParam.setUserId(UserUtils.getUserId());

            //파라미터용 - common-support ex)NoticeParam
            int totalCount = myPageService.getCntrPointCnt(gntrPointParam);

            Pagination pagination = Pagination.getInstance(totalCount, gntrPointParam.getItemsPerPage());

            gntrPointParam.setPagination(pagination);

            // db 결과 리턴용 common-domain ex)Notice.java
            List<CntrPoint> list = myPageService.getCntrPointInfo(gntrPointParam);

            // 화면 리턴용 api-domain ex) NoticeInfo.java
            List<CntrPointInfo> infoList = new ArrayList<>();

            if(!list.isEmpty()) {
            	for(CntrPoint cntrPoint : list) {
            		infoList.add(new CntrPointInfo(cntrPoint));
            	}
            }

            //총 적립 포인트
            int cntrPointTotal =  myPageService.cntrPointTotal(gntrPointParam);

            //총 사용 포인트
            int usePointTotal =  myPageService.usePointTotal(gntrPointParam);

            //총 잔여 포인트
            int blcePointTotal = myPageService.blcePointTotal(gntrPointParam);

            result = ApiResponseEntity.data()
            		.list(infoList)
            		.pagination(pagination)
            		.put("totalCnt", totalCount)
            		.put("userName", UserUtils.getUser().getUserName())
            		.put("userGrade", list.isEmpty() ? "일반" : list.get(0).getUserGrade())
            		.put("cntrPointTotal", cntrPointTotal)
            		.put("usePointTotal", usePointTotal)
            		.put("blcePointTotal", blcePointTotal)
              		.put("wdrList", CodeUtils.getCodeInfoList("WDR"))
              		.put("phoneList", CodeUtils.getCodeInfoList("PHONE"))
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/getCntrPointDetail")
    public ResponseEntity getCntrPointDetail(CntrPointParam gntrPointParam){
        ResponseEntity result = null;

        try {

        	if (gntrPointParam.getPage() <= 0) {
        		gntrPointParam.setPage(1);
            }

        	gntrPointParam.setUserId(UserUtils.getUserId());

            //파라미터용 - common-support ex)NoticeParam
            int totalCount = myPageService.getCntrPointDetailCnt(gntrPointParam);

            Pagination pagination = Pagination.getInstance(totalCount, gntrPointParam.getItemsPerPage());

            gntrPointParam.setPagination(pagination);

            // db 결과 리턴용 common-domain ex)Notice.java
            List<CntrPoint> list = myPageService.getCntrPointDetail(gntrPointParam);

            // 화면 리턴용 api-domain ex) NoticeInfo.java
            List<CntrPointDetail> infoList = new ArrayList<>();

            if(!list.isEmpty()) {
            	for(CntrPoint cntrPoint : list) {
            		infoList.add(new CntrPointDetail(cntrPoint));
            	}
            }

            //총 적립 포인트
            int cntrPointTotal =  myPageService.cntrPointTotal(gntrPointParam);

            //총 사용 포인트
            int usePointTotal =  myPageService.usePointTotal(gntrPointParam);

            //총 잔여 포인트
            int blcePointTotal = myPageService.blcePointTotal(gntrPointParam);

            //지자체 명
            CntrPointDetail locgovNm =  myPageService.getLocgovNm(gntrPointParam);

            result = ApiResponseEntity.data()
            		.list(infoList)
            		.pagination(pagination)
            		.put("totalCnt", totalCount)
            		.put("userName", UserUtils.getUser().getUserName())
            		.put("cntrPointTotal", cntrPointTotal)
            		.put("usePointTotal", usePointTotal)
            		.put("blcePointTotal", blcePointTotal)
            		.put("locgovNm", locgovNm)
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/getCntrInitInfo")
    public ResponseEntity getCntrInitInfo(CntrParam cntrParam){
      ResponseEntity result = null;

      try {

    	  cntrParam.setUserId(UserUtils.getUser().getUserId());

    	  int totalCntrAmt = myPageService.getTotalCntrAmt(cntrParam);


    	  if (cntrParam.getPage() <= 0) {
      		cntrParam.setPage(1);
          }

    	  cntrParam.setUserId(UserUtils.getUserId());

          //파라미터용 - common-support ex)NoticeParam
          int totalCount = myPageService.getGntrListTotalCnt(cntrParam);

          Pagination pagination = Pagination.getInstance(totalCount, cntrParam.getItemsPerPage());

          cntrParam.setPagination(pagination);

          // db 결과 리턴용 common-domain ex)Notice.java
          List<Cntr> list = myPageService.getCntrListInfo(cntrParam);

          // 화면 리턴용 api-domain ex) NoticeInfo.java
          List<CntrInfo> infoList = new ArrayList<>();

          if(!list.isEmpty()) {
          	for(Cntr cntr : list) {
          		infoList.add(new CntrInfo(cntr));
          	}
          }

          //세액공제 예상금액
          int taxRedutionEstimate = myPageService.getTaxRedutionEstimate(cntrParam);

          result = ApiResponseEntity.data()
          		.put("totalCntrAmt", totalCntrAmt)
          		.put("userName", UserUtils.getUser().getUserName())
          		.put("wdrList", CodeUtils.getCodeInfoList("WDR"))
          		.put("yearList", CodeUtils.getCodeInfoList("YYYY"))
          		.put("phoneList", CodeUtils.getCodeInfoList("PHONE"))
          		.list(infoList)
        		.pagination(pagination)
        		.put("totalCnt", totalCount)
        		.put("taxRedutionEstimate", taxRedutionEstimate)
          		.ok();
      } catch (UserException e) {
          return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
      } catch(Exception e){
          result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
      }
      return result;
    }

    @GetMapping("/getCntrList")
    public ResponseEntity getGntrPoint(CntrParam cntrParam){
        ResponseEntity result = null;

        try {

        	if (cntrParam.getPage() <= 0) {
        		cntrParam.setPage(1);
            }

        	cntrParam.setUserId(UserUtils.getUserId());

            //파라미터용 - common-support ex)NoticeParam
            int totalCount = myPageService.getGntrListTotalCnt(cntrParam);

            Pagination pagination = Pagination.getInstance(totalCount, cntrParam.getItemsPerPage());

            cntrParam.setPagination(pagination);

            // db 결과 리턴용 common-domain ex)Notice.java
            List<Cntr> list = myPageService.getCntrListInfo(cntrParam);

            // 화면 리턴용 api-domain ex) NoticeInfo.java
            List<CntrInfo> infoList = new ArrayList<>();

            if(!list.isEmpty()) {
            	for(Cntr cntr : list) {
            		infoList.add(new CntrInfo(cntr));
            	}
            }

            //총 기부금액
            int totalCntrAmt =  myPageService.getTotalCntrAmt(cntrParam);

            result = ApiResponseEntity.data()
            		.list(infoList)
            		.pagination(pagination)
            		.put("totalCnt", totalCount)
            		.put("totalCntrAmt", totalCntrAmt)
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
	 * 지자체 정보 가져오기
	 * */
	@PostMapping("/getLocGov")
	public ResponseEntity getLocGov(@RequestBody LocGovInfo locGovInfo) {
		ResponseEntity result = null;

		try {

			result = ApiResponseEntity.data()
					.put("locGovList", myPageService.getLocGovList(locGovInfo))
					.put("status", HttpStatus.OK).ok();
		} catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        }catch(Exception e){
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 회원 정보 가져오기
	 * */
	@PostMapping("/getUserInfo")
	public ResponseEntity getUserInfo(@RequestBody LocGovInfo locGovInfo) {
		ResponseEntity result = null;

		try {

			result = ApiResponseEntity.data()
					.put("locGovList", myPageService.getLocGovList(locGovInfo))
					.put("status", HttpStatus.OK).ok();
		} catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        }catch(Exception e){
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}


	/**
	 * 주문 단계 목록 가져오기
	 * */
	@GetMapping("/getOrderLevelList")
	public ResponseEntity getOrderLevelList(CntrParam cntrParam) {
		ResponseEntity result = null;

		try {
			cntrParam.setUserId(UserUtils.getUserId());

			//Loginid가 확인되지 않을 경우 비정상적인 접근으로 처리
			if(UserUtils.getLoginId()== null) {
				return result = ApiResponseEntity.error(ApiError.BAD_REQUEST);
			}

			result = ApiResponseEntity.data()
					.put("orderLevelList", myPageService.getOrderLevelList(cntrParam))
					.put("status", HttpStatus.OK).ok();
		} catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        }catch(Exception e){
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 주문 클레임 단계 목록 가져오기
	 * */
	@GetMapping("/getOrderClaimLevelList")
	public ResponseEntity getOrderClaimLevelList(CntrParam cntrParam) {
		ResponseEntity result = null;

		try {
			cntrParam.setUserId(UserUtils.getUserId());

			result = ApiResponseEntity.data()
					.put("orderClaimLevelList", myPageService.getOrderClaimLevelList(cntrParam))
					.put("status", HttpStatus.OK).ok();
		} catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        }catch(Exception e){
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

    @PostMapping("/delete-wishlist")
    public ResponseEntity deleteWishlist(HttpServletRequest request, @RequestBody(required = false) WishlistListParam listParm){
        ResponseEntity result = null;
        try {
            listParm.setUserId(UserUtils.getUserId());
            wishlistService.deleteWishlistByListParam(listParm);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 답례품 후기
     *
     * @param searchParam
     * @return
     */
    @GetMapping("reviews")
    public ResponseEntity review(ItemParam searchParam) {

        ResponseEntity result = null;

        try {

            searchParam.setUserId(UserUtils.getUserId());
            searchParam.setConditionType(searchParam.getConditionType());
            searchParam.setSearchStartDate(searchParam.getSearchStartDate().replaceAll("-", ""));
            searchParam.setSearchEndDate(searchParam.getSearchEndDate().replaceAll("-", ""));

            int reviewCount = itemService.getItemReviewCountByParam(searchParam);
            Pagination pagination = Pagination.getInstance(reviewCount, searchParam.getItemsPerPage());

            searchParam.setPagination(pagination);

            List<ItemReview> itemReviews = itemService.getItemReviewListByParam(searchParam);
            List<ItemReviewInfo> infos = new ArrayList<>();
            List<ItemReviewFilter> itemReviewFilters = new ArrayList<>();
            List<ReviewFilterInfo> reviewFilterInfos = new ArrayList<>();

            if (itemReviews != null && !itemReviews.isEmpty()) {

                for (ItemReview itemReview : itemReviews) {
                    infos.add(new ItemReviewInfo((itemReview)));

                    List<ItemReviewFilter> tempItemReviewFilters = itemReview.getItemReviewFilters();
                    if (tempItemReviewFilters != null && !tempItemReviewFilters.isEmpty()) {
                        itemReviewFilters.addAll(tempItemReviewFilters);
                    }
                }

                List<FilterGroup> filterGroups = reviewFilterService.getFilterGroupsByItemReviewFilters(itemReviewFilters);

                if (filterGroups != null && !filterGroups.isEmpty()) {
                    filterGroups.forEach(f -> {
                        reviewFilterInfos.add(new ReviewFilterInfo(f));
                    });
                }
            }

            result = ApiResponseEntity.data()
                    .pagination(pagination)
                    .list(infos)
                    .put("reviewFilters", reviewFilterInfos)
                    .ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @GetMapping("/wishlist")
    public ResponseEntity wishlist(HttpServletRequest request, WishlistParam wishlistParam) {
        ResponseEntity result = null;
        List<Wishlist> wishlists = null;
        Pagination pagination = null;
        int wishlistCount = 0;
        try {
            wishlistCount = wishlistService.getWishlistCountByUserId(UserUtils.getUserId());
            pagination = Pagination.getInstance(wishlistCount, wishlistParam.getItemsPerPage());
            wishlistParam.setPagination(pagination);
            wishlistParam.setUserId(UserUtils.getUserId());
            wishlists = wishListDataSet(wishlistService.getWishlistList(wishlistParam));
            result = ApiResponseEntity.data().list(wishlists).pagination(pagination).ok();
        }catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    private List<Wishlist> wishListDataSet(List<Wishlist> wishlistList){

        List<Integer> ids = new ArrayList<>();
        List<Item> sItemList = new ArrayList<>();

        List<Wishlist> resultList = new ArrayList<>();

        if (wishlistList != null) {

            // 세트상품 정보 set
            for (Wishlist wishlist : wishlistList) {
                if ("3".equals(wishlist.getItem().getItemType())) {
                    ids.add(wishlist.getItem().getItemId());
                }
            }

            if (ids.size() > 0) {
                sItemList = itemService.getItemListForItemSet(ids);
            }

            for (Wishlist wishlist : wishlistList) {
                wishlist.getItem().setSupplyPrice(0);    // 공급자 0으로 설정
                wishlist.getItem().setItemImage(ShopUtils.loadImage(wishlist.getItem().getItemUserCode(), wishlist.getItem().getItemImage(), "M"));

                if ("3".equals(wishlist.getItem().getItemType()) && sItemList != null && !sItemList.isEmpty()) {
                    for (Item sItem : sItemList) {
                        if (wishlist.getItem().getItemId() == sItem.getItemId()) {
                            wishlist.getItem().setItemSets(sItem.getItemSets());
                            break;
                        }
                    }
                }

                resultList.add(wishlist);
            }
        }
        return resultList;
    }

    /**
     * 리뷰삭제처리
     *
     * @param itemReviewId
     * @return
     */
    @PostMapping("delete-review/{itemReviewId}")
    public ResponseEntity deleteReview(@PathVariable("itemReviewId") int itemReviewId) {

        ResponseEntity result = null;

        try {

            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }

            ItemReview itemReview = itemService.getItemReviewById(itemReviewId);

            if (itemReview == null) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            if (UserUtils.getUserId() != itemReview.getUserId()) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            itemService.deleteItemReview(itemReviewId);

            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 미등록 이용후기
     *
     * @param searchParam
     * @return
     */
    @GetMapping("nonregistered-reviews")
    public ResponseEntity reviewNone(ItemParam searchParam) {

        Pagination pagination = null;
        ResponseEntity result = null;

        try {

            searchParam.setUserId(UserUtils.getUserId());

            if (searchParam.isPaging()) {
                int nonReviewCount = itemService.getItemNonregisteredReviewCount(searchParam);

                pagination = Pagination.getInstance(nonReviewCount, searchParam.getItemsPerPage());
                searchParam.setPagination(pagination);
            }

            List<OrderItem> orderItems = itemService.getItemNonregisteredReviewList(searchParam);

            result = ApiResponseEntity.data().list(orderItems).pagination(pagination).ok();

        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 영수증 출력하기
     *
     * @param searchParam
     * @return
     */
    @PostMapping("receipt-print")
    public ResponseEntity receiptPrint(ReceiptParam receiptParam) {

        ResponseEntity result = null;

        try {

            receiptParam.setUserId(UserUtils.getUserId());
            Cntr cntr = myPageService.getCntrReceipt(receiptParam);

            result = ApiResponseEntity.data().put("cntr", cntr).ok();

        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 영수증 출력 로그 저장
     *
     * @param searchParam
     * @return
     */
    @PostMapping("receipt-print/insert")
    public ResponseEntity insertReceiptPrint(ReceiptParam receiptParam) {

        ResponseEntity result = null;

        try {

            receiptParam.setUserId(UserUtils.getUserId());
            myPageService.insertCntrReceipt(receiptParam);

            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();

        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("honorList")
    public ResponseEntity getUserHonorList() {

    	ResponseEntity result = null;

    	try {
    		long userId = UserUtils.getUserId();

            result = ApiResponseEntity.data()
            						  .put("total", myPageService.getUserHonorListTotal(userId))
            						  .put("honorList", myPageService.getUserHonorList(userId)).ok();

        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

    	return result;
    }

    @PostMapping("/deleteIntrstLocGovAll")
    public ResponseEntity deleteIntrstLocGovAll(@RequestBody List<String> locgovArr){
        ResponseEntity result = null;

        try {

        	if(!UserUtils.isUserLogin()) {
        		return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
        	}

        	IntrsLocgovParam intrsLocgov = IntrsLocgovParam.builder()
        												   .userId(UserUtils.getUserId())
        												   .locgovArr(locgovArr)
        												   .build();

        	int resultCount = myPageService.deleteIntrstLocGovAll(intrsLocgov);



            result = ApiResponseEntity.data()
            		.put("resultCnt", resultCount)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }


	/**
	 * qr 정보
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	@PostMapping("qr")
	public ResponseEntity makeQr() {
		QrInfo qrInfo = myPageService.getQrData();
		if (qrInfo.isSuccess()) {
			LocalDateTime now = LocalDateTime.now();
			int timeDif = qrInfo.getExpireTime().compareTo(now);
			return ApiResponseEntity.data()
					.put("qrCodes", qrInfo.getHonorInfos())
					.put("timeDif", qrInfo.getExpireSeconds() - timeDif)
					.put("status", HttpStatus.OK)
					.ok();
		} else {
			return ApiResponseEntity.data()
					.put("qrCode", "")
					.put("errCode", qrInfo.getErrCode())
					.put("errMsg", qrInfo.getErrMsg())
					.put("status", HttpStatus.OK)
					.ok();
		}
	}


	/**
	 * qr 테스트
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	@GetMapping("qrTest")
	public ResponseEntity qrTest() {
		String testData = "테스트데이터 입니다. qr 정보를 보여줍니다.\n줄바꿈되나요?\n줄바꿈 굿이네요~";

		return ApiResponseEntity.data()
				.put("qrCode", QrUtils.getQrCodeBase64String(testData))
				.put("status", HttpStatus.OK)
				.ok();
	}

	/**
	 * 명예시도민증 목록 조회
	 * @return
	 */
    @PostMapping("honorList-new")
    public ResponseEntity getUserHonorListNew() {

    	ResponseEntity result = null;

    	if (UserUtils.getUser() == null || UserUtils.getUser().getUserId() == 0) {
    		return ApiResponseEntity.data()
						  .put("errCode", ApiError.NO_LOGIN)
						  .put("errMsg", ApiError.NO_LOGIN.getDescription()).ok();
    	}

    	try {
    		List<HnrUserInfo> list = lclgvHnrUserMngService.getHnrUserInfoList();
    		if (list == null) {
    			list = new ArrayList<>();
    		}
    		int goldCnt = 0;
    		int silverCnt = 0;
    		int bronzeCnt = 0;
    		for (HnrUserInfo hnrUserInfo : list) {
				if ("GOLD".equals(hnrUserInfo.getHnrGrd())) {
					goldCnt++;
				} else if ("SILVER".equals(hnrUserInfo.getHnrGrd())) {
					silverCnt++;
				} else if ("BRONZE".equals(hnrUserInfo.getHnrGrd())) {
					bronzeCnt++;
				}
			}
            result = ApiResponseEntity.data()
            						  .put("goldCnt", goldCnt)
            						  .put("silverCnt", silverCnt)
            						  .put("bronzeCnt", bronzeCnt)
            						  .put("honorList", list).ok();

        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

    	return result;
    }

    /**
     * 기부확인증
     * 초기 조회
     * */
    @GetMapping("/getReceiptInitInfo")
    public ResponseEntity getReceiptInitInfo(CntrReceipt receiptParam){
      ResponseEntity result = null;

      try {

    	  /* 유저 ID 값 셋팅 */
    	  receiptParam.setUserId(UserUtils.getUserId());

    	  /* 검색 조건에 맞는 총 기부금액 및 총 기부건수 */
    	  Map<String, Object> receiptCntrMap = myPageService.getReceiptCntrAmtAndTotalCnt(receiptParam);
    	  int totalCntrAmt = Integer.parseInt(receiptCntrMap.get("cntr_amt").toString());
    	  int totalCount = Integer.parseInt(receiptCntrMap.get("cntr_cnt").toString());

    	  if (receiptParam.getPage() <= 0) {
    		  receiptParam.setPage(1);
          }

          Pagination pagination = Pagination.getInstance(totalCount, receiptParam.getItemsPerPage());
          receiptParam.setPagination(pagination);

          /* 기부확인증 리스트 출력 */
          List<Cntr> list = myPageService.getReceiptListInfo(receiptParam);

          /* 화면 리턴 List */
          List<CntrInfo> infoList = new ArrayList<>();

          if(!list.isEmpty()) {
          	for(Cntr cntr : list) {
          		infoList.add(new CntrInfo(cntr));
          	}
          }

          result = ApiResponseEntity.data()
          		.put("totalCntrAmt", totalCntrAmt)
          		.put("userName", UserUtils.getUser().getUserName())
          		.put("wdrList", CodeUtils.getCodeInfoList("WDR"))
          		.put("yearList", CodeUtils.getCodeInfoList("YYYY"))
          		.put("phoneList", CodeUtils.getCodeInfoList("PHONE"))
          		.list(infoList)
        		.pagination(pagination)
        		.put("totalCnt", totalCount)
          		.ok();
      } catch (UserException e) {
          return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
      } catch(Exception e){
          result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
      }
      return result;
    }

    /**
     * 기부확인증
     * 팝업 조회
     * */
    @PostMapping("/getReceiptPopInfo")
    public ResponseEntity getReceiptPopInfo(@RequestBody CntrReceipt receiptParam){
      ResponseEntity result = null;

      try {

    	  /* 유저 ID 값 셋팅 */
    	  receiptParam.setUserId(UserUtils.getUserId());

    	  /* 기부코드 기준 총 기부금액 및 총 기부건수 */
    	  Map<String, Object> receiptCntrMap = myPageService.getReceiptCntrPopInfo(receiptParam);
    	  int totalCntrAmt = Integer.parseInt(receiptCntrMap.get("cntr_amt").toString());
    	  int totalCount = Integer.parseInt(receiptCntrMap.get("cntr_cnt").toString());

    	  if (receiptParam.getPage() <= 0) {
    		  receiptParam.setPage(1);
          }

          Pagination pagination = Pagination.getInstance(totalCount, receiptParam.getItemsPerPage());
          receiptParam.setPagination(pagination);

          /* 기부확인증 기부코드 기준 리스트 출력 */
          List<Cntr> list = myPageService.getReceiptPopListInfo(receiptParam);

          /* 화면 리턴 List */
          List<CntrInfo> infoList = new ArrayList<>();
          if(!list.isEmpty()) {
          	for(Cntr cntr : list) {
          		infoList.add(new CntrInfo(cntr));
          	}
          }

          /* 기부지자체 출력시 상위 자자체 */
          Map<String,Object> topLocGovMap = myPageService.getTopLocGov(receiptParam);

          UserDetail userDetail = (UserDetail) UserUtils.getUser().getUserDetail();

          result = ApiResponseEntity.data()
          		.put("totalCnt", totalCount)
          		.put("totalCntrAmt", totalCntrAmt)
          		.put("topLocGovMap", topLocGovMap)
          		.put("userName", UserUtils.getUser().getUserName())
          		.put("userBirthday", userDetail.getBirthday())
          		.list(infoList)
        		.pagination(pagination)
          		.ok();
      } catch (UserException e) {
          return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
      } catch(Exception e){
          result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
      }
      return result;
    }

    /**
     * 기부혜택증 열람기록 저장
     *
     * @param searchParam
     * @return
     */
    @PostMapping("/saveHonorViewHist")
    public ResponseEntity saveHonorViewHist(ReceiptParam receiptParam) {

    	log.debug("saveHonorViewHist : " + receiptParam);
        ResponseEntity result = null;

        try {

        	receiptParam.setUserId(UserUtils.getUserId());
            myPageService.saveHonorViewHist(receiptParam);
//
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
            throw new OpRuntimeException ("test");
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

}


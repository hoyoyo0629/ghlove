package saleson.api.event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.event.domain.EventInfo;
import saleson.api.item.domain.ItemList;
import saleson.api.mypage.domain.ItemReviewInfo;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.display.DisplayService;
import saleson.shop.featured.FeaturedService;
import saleson.shop.featured.domain.Featured;
import saleson.shop.featured.domain.FeaturedReply;
import saleson.shop.featured.support.FeaturedItem;
import saleson.shop.featured.support.FeaturedParam;
import saleson.shop.featured.support.FeaturedReplyParam;
import saleson.shop.item.support.ItemParam;

@RestController("ApiEventController")
@RequestMapping("/api/event")
public class EventController {

    private static Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private FeaturedService featuredService;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private DisplayService displayService;


	/**
	 * 진행중인 이벤트 리스트 API (page, itemsPerPage)
	 *
	 * @param featuredParam
	 * @return
	 */
	@GetMapping("")
	public ResponseEntity list(FeaturedParam featuredParam) {
		ResponseEntity result = null;
		Pagination pagination = null;
		List<Featured> featuredList = null;

		if (featuredParam == null) {
			featuredParam = new FeaturedParam();
		}

		try {
			featuredParam.setFeaturedType("1");
			featuredParam.setFeaturedFlag("Y");
			featuredParam.setDisplayListFlag("Y");
			featuredParam.setConditionType("FRONT");

			if(StringUtils.isNotEmpty(featuredParam.getIngCond()) && featuredParam.getIngCond().equals("N")) {
				featuredParam.setProgression("3");
				featuredParam.setConditionType(null);
			}

			if (featuredParam.getFeaturedCodeChecked() == null) {
				featuredParam.setFeaturedCodeChecked("");
			}

			pagination = Pagination.getInstance(featuredService.getFeaturedCountByParamForFront(featuredParam), featuredParam.getItemsPerPage());
			featuredParam.setPagination(pagination);

			featuredList = featuredService.getFeaturedListByParamForFront(featuredParam);
			result = ApiResponseEntity.data().list(eventListDataSet(featuredList)).pagination(pagination).ok();
		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 이벤트 아이템 리스트 API (featuredId, page, itemsPerPage)
	 *
	 * @param featuredParam
	 * @return
	 */
	@GetMapping("/items")
	public ResponseEntity itemList(FeaturedParam featuredParam) {
		ResponseEntity result = null;
		List<Map<String, Object>> resultList = new ArrayList<>();
		Featured featured = null;

		if (featuredParam == null) {
			featuredParam = new FeaturedParam();
		}

		try {
			featured = featuredService.getFeaturedById(featuredParam);

			if (featured == null || !"Y".equals(featured.getFeaturedFlag())) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_FEATURED);
			}

			if (!ObjectUtils.isEmpty(featured.getStartDate()) && !ObjectUtils.isEmpty(featured.getEndDate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
				Date currentDate = new Date();

				int startDate = Integer.parseInt(featured.getStartDate() + featured.getStartTime());
				int endDate = Integer.parseInt(featured.getEndDate() + featured.getEndTime());
				int today = Integer.parseInt(sdf.format(currentDate));

				// 기간 만료, 비공개 상태인 기획전
				if ((today < startDate || today > endDate) || (!"Y".equals(featured.getDisplayListFlag()))) {
					return ApiResponseEntity.error(ApiError.BAD_REQUEST_FEATURED_FINISH);
				}
			}

			List<HashMap<String, String>> itemTypeMaps = featuredService.getItemTypeList(featured.getProdState(), featuredParam);
			HashMap<String, List<FeaturedItem>> itemListMap = featuredService.getItemListMap(featured.getProdState(), featuredParam);

			for (HashMap<String, String> itemTypeMap : itemTypeMaps) {

				List<FeaturedItem> featuredItems = itemListMap.get(itemTypeMap.get(Featured.ITEM_TYPE_ID_KEY));
				Map<String, Object> resultMap = new HashMap<>();

				resultMap.put("itemType", itemTypeMap.get(Featured.ITEM_TYPE_NAME_KEY));
				resultMap.put("items", featuredItemsSet(featuredItems));

				resultList.add(resultMap);
			}

			result = ApiResponseEntity.data().put("featuredItems", resultList).ok();

		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 스팟세일 API
	 *
	 * @param itemParam
	 * @param
	 * @return
	 */
	@GetMapping("/spot")
	public ResponseEntity spotList(ItemParam itemParam) {
		ResponseEntity result = null;
		try {
			itemParam = ItemUtils.bindItemParam(itemParam);

			Pagination pagination = Pagination.getInstance(displayService.getItemCountForSpot(itemParam), itemParam.getItemsPerPage());
			itemParam.setPagination(pagination);

			result = ApiResponseEntity.data().list(displayService.getItemListForSpot(itemParam)).pagination(pagination).ok();
		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	private List<ItemList> featuredItemsSet(List<FeaturedItem> list) {

		List<ItemList> resultList = new ArrayList<>();
		if (!list.isEmpty()) {

			for (FeaturedItem item : list) {
				resultList.add(new ItemList(item));
			}
		}
		return resultList;

	}

	private List<EventInfo> eventListDataSet(List<Featured> featuredList) {
		List<EventInfo> eventInfoList = new ArrayList<>();

		if (featuredList != null && !featuredList.isEmpty()) {

			for (Featured featured : featuredList) {
				eventInfoList.add(new EventInfo(featured));
			}

		}

		return eventInfoList;
	}

	/**
	 * 진행중인 이벤트 상세 API (featuredUrl)
	 *
	 * @param featuredParam
	 * @return
	 */
	@GetMapping("/{featuredUrl}")
	public ResponseEntity details(@PathVariable("featuredUrl") String featuredUrl, FeaturedParam featuredParam) {
		ResponseEntity result = null;
        Pagination pagination = null;
		try {
			Featured featured = featuredService.getFeaturedById(featuredParam);

			if ("N".equals(featured.getFeaturedFlag())) {
				throw new PageNotFoundException();
			}

			if (featured.getStartDate() != null && !"".equals(featured.getStartDate()) && featured.getEndDate() != null && !"".equals(featured.getEndDate())) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
				Date currentDate = new Date();

				int startDate = Integer.parseInt(featured.getStartDate() + featured.getStartTime());
				int endDate = Integer.parseInt(featured.getEndDate() + featured.getEndTime());
				int today = Integer.parseInt(sdf.format(currentDate));

				// Jun-Eu Son 2017.4.24 기간만료, 비공개상태인 기획전 진입금지
				// 2026-03-17 종료된 이벤트 다시 진입가능
				/*
				 * if ((!UserUtils.isManagerLogin() && (today < startDate || today > endDate))
				 * || (!UserUtils.isManagerLogin() &&
				 * (!featured.getDisplayListFlag().equals("Y")))) { throw new
				 * UserException("해당 기획전은 종료되었습니다."); }
				 */
			}

			if (ValidationUtils.isNotNull(featured.getSeo())) {
				if (!featured.getSeo().isSeoNull()) {
					ShopUtils.setSeo(featured.getSeo());
				}
			}

			featuredParam.setFeaturedId(featured.getFeaturedId());
			featuredParam.setProdState(featured.getProdState());

			Map<String, Object> resultMap = new HashMap<>();
			List<HashMap<String, String>> itemTypeList = featuredService.getItemTypeList(featured.getProdState(), featuredParam);

			pagination = Pagination.getInstance(featuredService.getItemListCountMap(featured.getProdState(), featuredParam), featuredParam.getItemsPerPage());
	        if (featuredParam.getPage() > 0) {
	        	pagination.setCurrentPage(featuredParam.getPage());
	        } else {
	        	pagination.setCurrentPage(1);
	        }
	        pagination.setItemsPerPage(featuredParam.getItemsPerPage());
	        featuredParam.setPagination(pagination);

	        // 랜덤 무제한 조회 되도록 추가..
//	        pagination.setCurrentPage(1);
//	        featuredParam.setFeaturedType("display");
	        // 랜덤 무제한 조회 되도록 추가..

	        HashMap<String, List<FeaturedItem>> itemListMap = featuredService.getItemListMap(featured.getProdState(), featuredParam);
			Map<String, Object> itemMap = new HashMap<>();

			if (itemListMap != null) {

				Set<String> keySet = itemListMap.keySet();

				for (String key : keySet) {
					List<FeaturedItem> featuredItems = itemListMap.get(key);
					List<ItemList> itemLists = new ArrayList<>();

					featuredItems.stream().forEach(i ->{
						itemLists.add(new ItemList(i));
					});

					itemMap.put(key,itemLists);
				}

			}

			resultMap.put("featuredUrl", featuredUrl);
			resultMap.put("itemTypeList", itemTypeList);
			resultMap.put("itemListMap", itemMap);
			resultMap.put("featured", featured);
			resultMap.put("itemTypeIdKey", Featured.ITEM_TYPE_ID_KEY);
			resultMap.put("itemTypeNameKey", Featured.ITEM_TYPE_NAME_KEY);

			result = ApiResponseEntity.data().pagination(pagination).put("featuredList", resultMap).put("adultYn", UserUtils.isAdult() ? "Y" : "N").ok();

		} catch (UserException e2) {
			log.error(getClass().getName() + " details error", e2);
			result = ApiResponseEntity.error(ApiError.END_FEATURED);
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " details error", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 기획전 댓글 목록 조회 API (page, featuredUrl, featuredId)
	 *
	 * @param featuredReplyParam
	 * @return
	 */

	@GetMapping("/replies")
	@RequestProperty(layout = "blank")
	public ResponseEntity listFeaturedReply(FeaturedReplyParam featuredReplyParam, FeaturedParam featuredParam) {
		ResponseEntity result = null;

		try {
			Featured featured = featuredService.getFeaturedById(featuredParam);

			featuredReplyParam.setFeaturedId(featuredReplyParam.getFeaturedId());
			featuredReplyParam.setDataStatus("0");

			int replyCount = featuredService.getFeaturedReplyCountByParam(featuredReplyParam);

			Pagination pagination = Pagination.getInstance(replyCount);
			pagination.setLink("javascript:paginationFeaturedReply([page])");
			featuredReplyParam.setPagination(pagination);

			result = ApiResponseEntity.data()
					.list(getReplyListByParam(featuredReplyParam))
					.pagination(pagination).ok();

		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 기획전 댓글 쓰기 API (replyContent, featuredUrl, featuredId)
	 *
	 * @param featuredReply
	 * @return
	 */
	@PostMapping("/reply")
	public ResponseEntity createFeaturedReply(@RequestBody(required = false) FeaturedReply featuredReply) {
		ResponseEntity result = null;

		try {
			Config shopConfig = configService.getShopConfig(Config.SHOP_CONFIG_ID);
			for (String banWord : shopConfig.getBanWords()) {
				if (featuredReply.getReplyContent().indexOf(banWord) > -1) {
					return ApiResponseEntity.error(ApiError.BAD_REQUEST_NOT_ALLOWED_WORD);
				}
			}

			long id = sequenceService.getId("OP_FEATURED_REPLY");

			featuredReply.setId(id);
			featuredReply.setUserId(UserUtils.getUserId());
			featuredReply.setUserName(UserUtils.getLoginId());
			featuredReply.setDataStatus("0");
			featuredReply.setCreatedBy(UserUtils.getUserId());

			featuredService.insertFeaturedReply(featuredReply);

			FeaturedReplyParam featuredReplyParam = new FeaturedReplyParam();
			featuredReplyParam.setId(id);

			result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	private List<FeaturedReply> getReplyListByParam(FeaturedReplyParam featuredReplyParam) {

		List<FeaturedReply> list = featuredService.getFeaturedReplyByParam(featuredReplyParam);

		if (list != null && !list.isEmpty()) {

			list.stream().forEach(reply -> {
				reply.setCreated(DateUtils.datetime(reply.getCreated()));
				reply.setUserName(UserUtils.reMasking(reply.getUserName(), "id"));
			});

		}

		return list;
	}

}

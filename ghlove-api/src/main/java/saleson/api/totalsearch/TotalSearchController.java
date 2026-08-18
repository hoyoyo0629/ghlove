package saleson.api.totalsearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.enumeration.mapper.EnumMapper;
import saleson.common.utils.UserUtils;
import saleson.shop.totalsearch.TotalSearchService;
import saleson.shop.totalsearch.domain.MyRecent;
import saleson.shop.totalsearch.support.Paginator;
import saleson.shop.totalsearch.support.TotalSearchParam;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.net.MalformedURLException;
import java.text.ParseException;

@Slf4j
@RestController("ApiTotalSearchController")
@RequestMapping("/api/totalsearch")
public class TotalSearchController {

	private final int PAGES_PER_BLOCK = 1;

	@Autowired
	TotalSearchService totalSearchService;

	@Autowired
    EnumMapper enumMapper;

	@GetMapping("/locgov")
	public ResponseEntity locgovResult(TotalSearchParam	param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			if(UserUtils.isUserLogin()) {
				param.setUserid(UserUtils.getLoginId());
			}

			HashMap search = totalSearchService.locgovResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("locgov".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			List<Map<String, Object>> objList = (List<Map<String, Object>>) search.get("result");

			if (objList != null && !objList.isEmpty()) {
				objList.stream().forEach(m -> m.put("view_url", "/donation/list-select.html?locgovCode=" + String.valueOf(m.get("locgov_code"))));
				search.put("result", objList);
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);

			result = ApiResponseEntity.data().map(map).ok();

		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/item")
	public ResponseEntity itemResult(TotalSearchParam param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.itemResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("item".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);
			map.put("adultYn", UserUtils.isAdult() ? "Y" : "N");

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "itemResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "itemResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "itemResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "itemResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/notice")
	public ResponseEntity noticeResult(TotalSearchParam	param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.noticeResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("notice".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "noticeResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "noticeResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "noticeResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "noticeResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/qna")
	public ResponseEntity qnaResult(TotalSearchParam param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.qnaResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("qna".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "qnaResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "qnaResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "qnaResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "qnaResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/faq")
	public ResponseEntity faqResult(TotalSearchParam param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.faqResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("faq".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);
			map.put("types", enumMapper.get("FaqType"));

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "faqResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "faqResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "faqResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "faqResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/databoard")
	public ResponseEntity databoardResult(TotalSearchParam param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.databoardResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("databoard".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "databoardResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "databoardResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "databoardResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "databoardResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/guidance")
	public ResponseEntity guidanceResult(TotalSearchParam param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.guidanceResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("guidance".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "guidanceResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "guidanceResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "guidanceResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "guidanceResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/event")
	public ResponseEntity eventResult(TotalSearchParam	param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.eventResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("event".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "eventResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "eventResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "eventResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "eventResult - Exception Error");
		}
		return result;
		*/
	}
	@GetMapping("/specialitem")
	public ResponseEntity specialitemResult(TotalSearchParam param) {
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
		try {
			HashMap<String, Object> map = new HashMap<>();
			HashMap<String, Object> pageInfo = new HashMap<>();

			HashMap search = totalSearchService.specialitemResult(param);
			Long totalCount = Long.parseLong(search.get("count").toString());

			if("specialitem".equals(param.getCategory())) {
				Paginator paginator = new Paginator(PAGES_PER_BLOCK, param.getPageSize(), totalCount);
				pageInfo = paginator.getFixedBlock(param.getPageNumber());
			}

			map.put("data", search);
			map.put("pageInfo", pageInfo);
			map.put("adultYn", UserUtils.isAdult() ? "Y" : "N");

			result = ApiResponseEntity.data().map(map).ok();
		} catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "spacialitemResult - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "spacialitemResult - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "spacialitemResult - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "spacialitemResult - Exception Error");
		}
		return result;
		*/
	}

	@GetMapping("/autokeyword")
    public ResponseEntity getAutoKeyword(TotalSearchParam param){
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
        try{
        	HashMap<String, Object> map = new HashMap<>();
        	if(!"".equals(param.getSearchKeyword())) {
        		List<HashMap> search = totalSearchService.autoKeywordResult(param);
        		map.put("data", search);
        	}

        	result = ApiResponseEntity.data().map(map).ok();
        } catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getAutoKeyword - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getAutoKeyword - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getAutoKeyword - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getAutoKeyword - Exception Error");
		}
        return result;
        */
    }

	@GetMapping("/popular")
    public ResponseEntity getPupular(TotalSearchParam param){
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
        try{
        	HashMap<String, Object> map = new HashMap<>();
        	List<HashMap> search = totalSearchService.popularResult(param);
        	map.put("data", search);

			result = ApiResponseEntity.data().map(map).ok();
        } catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getPupular - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getPupular - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getPupular - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getPupular - Exception Error");
		}
        return result;
        */
    }

	@GetMapping("/myrecent")
	public ResponseEntity getMyRecentKeyword(TotalSearchParam param){
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
        try{
        	HashMap<String, Object> map = new HashMap<>();
        	List<MyRecent> recent = totalSearchService.myRecentResult(UserUtils.getUserId());
        	map.put("data", recent);

        	result = ApiResponseEntity.data().map(map).ok();
        } catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getMyRecentKeyword - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getMyRecentKeyword - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getMyRecentKeyword - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "getMyRecentKeyword - Exception Error");
		}
        return result;
		*/
	}

	@GetMapping("/insert-recent")
	public ResponseEntity insertRecent(TotalSearchParam param){
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
        try{
        	MyRecent recent = new MyRecent();
        	HashMap<String, Object> map = new HashMap<>();

        	if (UserUtils.isUserLogin()) {
	        	recent.setUserId(UserUtils.getUserId());
	        	recent.setKeyword(param.getSearchKeyword().trim());

	        	if(recent.getKeyword().length() > 0) {
	        		totalSearchService.insertRecent(recent);
	        	}

	        	List<MyRecent> recentList = totalSearchService.myRecentResult(UserUtils.getUserId());

	        	if(recentList.size() > 10) {
	        		for(int i=10; i < recentList.size(); i++){
	        			totalSearchService.deleteRecent(recentList.get(i).getRecentId());
	        		}
	        		recentList = totalSearchService.myRecentResult(UserUtils.getUserId());
	        	}
	        	map.put("data", recentList);

        	}

        	result = ApiResponseEntity.data().map(map).ok();
        } catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "insertRecent - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "insertRecent - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "insertRecent - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "insertRecent - Exception Error");
		}
        return result;
        */
	}

	/**
	 * 내가찾은 검색어 삭제
	 *
	 * @param recent
	 * @return
	 */
	@GetMapping("/delete-recent")
	public ResponseEntity deleteRecent(MyRecent recent){
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
        try{
        	HashMap<String, Object> map = new HashMap<>();

        	if (UserUtils.isUserLogin()) {
	        	if(recent.getRecentId() > 0) {
	        		totalSearchService.deleteRecent(recent.getRecentId());
	        	}
	        	List<MyRecent> recentList = totalSearchService.myRecentResult(UserUtils.getUserId());
	        	map.put("data", recentList);
        	}

        	result = ApiResponseEntity.data().map(map).ok();
        } catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecent - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecent - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecent - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecent - Exception Error");
		}
        return result;
	*/
	}

	/**
	 * 내가찾은 검색어 전체삭제
	 *
	 * @param recent
	 * @return
	 */
	@GetMapping("/delete-recent-all")
	public ResponseEntity deleteRecentAll(){
		ResponseEntity result = null;
		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "locgovResult - MalformedURLException Error");
		return result;
		/*
        try{
        	HashMap<String, Object> map = new HashMap<>();

        	if (UserUtils.isUserLogin()) {
	        	totalSearchService.deleteRecentAll(UserUtils.getUserId());
	        	List<MyRecent> recentList = totalSearchService.myRecentResult(UserUtils.getUserId());
	        	map.put("data", recentList);
        	}

        	result = ApiResponseEntity.data().map(map).ok();
        } catch (MalformedURLException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecentAll - MalformedURLException Error");
		} catch (IOException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecentAll - IOException Error");
		} catch (ParseException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecentAll - ParseException Error");
		} catch (Exception e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "deleteRecentAll - Exception Error");
		}
        return result;
		*/
	}
}

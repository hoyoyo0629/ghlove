package saleson.api.designateddonation;

import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.UserException;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.designateddonation.domain.PrjNoticeFile;
import saleson.shop.designateddonation.support.DesignatedCntr;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.faq.FaqDto;
import saleson.shop.faq.FaqService;
import com.privacy.pCrypto;

@RestController("ApiDesignatedDonationController")
@RequestMapping("/api/designated-donation")
public class DesignatedDonationController {

	private static final Logger log = LoggerFactory.getLogger(DesignatedDonationController.class);

    @Autowired
    private DesignatedDonationService designatedDonationService;

    @Autowired
    private FaqService faqService;

    // 검색엔진 현재 환경
    @Value("${integration.env}")
	private String env;


	/**
	 * 지정기부 목록 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getList")
    public ResponseEntity<Map<String, Object>> selectDesignatedDonationList(DesignatedDonationSearchParam params){
    	ResponseEntity<Map<String, Object>> result = null;

        try {
        	params.setDisplay("FRONT");
        	List<DesignatedDonation> list = designatedDonationService.selectDesignatedDonationList(params);

            result = ApiResponseEntity.data()
            		.put("content", list)
            		.put("totalPages", params.getPagination().getTotalPages())
            		.put("currentPage", params.getPage())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectDesignatedDonationList error :: " + e.getErrorMessage());
        }
        return result;
    }

    /**
	 * 지정기부 목록 조회 - 검색 엔진
	 * @param
	 * @return
	 */
    @GetMapping("/getListBySearchEngine")
    public ResponseEntity<Map<String, Object>> selectDesignatedDonationListBySearchEngine(DesignatedDonationSearchParam params){
    	ResponseEntity<Map<String, Object>> result = null;

        try {
        	params.setDisplay("FRONT");
        	List<DesignatedDonation> list = null;
        	if("local".equals(env)) {
        		list = designatedDonationService.selectDesignatedDonationList(params);
        	}else {
        		list = designatedDonationService.selectDesignatedDonationListBySearchEngine(params);
        	}

            result = ApiResponseEntity.data()
            		.put("content", list)
            		.put("totalPages", params.getPagination().getTotalPages())
            		.put("currentPage", params.getPage())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectDesignatedDonationList error :: " + e.getErrorMessage());
        }
        return result;
    }


	/**
	 * 지정기부 사업구분, 상태 조회
	 * @param
	 * @return
	 */
    @PostMapping("/getBsnsTypes")
    public ResponseEntity<Map<String, Object>> getBsnsTypes(){
        return ApiResponseEntity.data()
        		.put("bsnsTypes", designatedDonationService.getDesignatedDonationBsnsTypes())
        		.put("prjStatus", designatedDonationService.getDesignatedDonationPrjStatus())
        		.ok();
    }


	/**
	 * 지정기부 상세 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getDetail")
    public ResponseEntity<Map<String, Object>> selectDesignatedDonationDetail(DesignatedDonationSearchParam params){
    	ResponseEntity<Map<String, Object>> result = null;

        try {
        	params.setDisplay("FRONT");
        	DesignatedDonation data = designatedDonationService.selectDesignatedDonationDetail(params);

        	if (data == null) {
        		return ApiResponseEntity.data()
        				.put("errMsg", "특정사업에 기부하기 상세내역이 없습니다.")
        				.ok();
        	}

            result = ApiResponseEntity.data()
            		.put("content", data)
            		.put("cntrListCurrentPage", params.getPagination().getCurrentPage())
            		.put("cntrListTotalCnt", params.getPagination().getTotalItems())
            		.put("cntrListTotalPages", params.getPagination().getTotalPages())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectDesignatedDonationDetail error :: " + e.getErrorMessage());
        }
        return result;
    }


	/**
	 * 지정기부 내역 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getDesignatedCntrList")
    public ResponseEntity<Map<String, Object>> selectDesignatedCntrList(DesignatedDonationSearchParam params){
    	ResponseEntity<Map<String, Object>> result = null;

        try {
        	params.setDisplay("FRONT");

            result = ApiResponseEntity.data()
            		.put("content", designatedDonationService.selectDesignatedCntrList(null, params))
            		.put("cntrListCurrentPage", params.getPagination().getCurrentPage())
            		.put("cntrListTotalCnt", params.getPagination().getTotalItems())
//            		.put("cntrListItemPerPage", params.getPagination().getItemsPerPage())
            		.put("cntrListTotalPages", params.getPagination().getTotalPages())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectDesignatedCntrList error :: " + e.getErrorMessage());
        }
        return result;
    }


	/**
	 * 지정기부 faq 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getFaqList")
    public ResponseEntity<Map<String, Object>> selectFaqList(FaqDto faqDto, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
    	ResponseEntity<Map<String, Object>> result = null;

        try {
//			faqDto.setFaqType(FaqType.CNTR_DESIGNATED);
			faqDto.setUseYn("Y");

            result = ApiResponseEntity.data()
            		.put("content", faqService.findAll(faqDto.getPredicate(), pageable))
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectFaqList error :: " + e.getErrorMessage());
        }
        return result;
    }


	/**
	 * 지정기부 응원메시지 저장
	 * @param
	 * @return
	 */
    @GetMapping("/saveCheerMsg")
    public ResponseEntity<Map<String, Object>> saveCheerMsg(DesignatedCntr designatedCntr){
		try {
		    return ApiResponseEntity.data()
					.put("saveCnt", designatedDonationService.saveCheerMsg(designatedCntr))
					.ok();
		} catch (UserException e) {
			return ApiResponseEntity.data()
					.put("errMsg", e.getErrorMessage())
					.ok();
		}
    }


	/**
	 * 지정기부 공지사항 조회
	 * @param
	 * @return
	 */
    @PostMapping("/getNoticeList")
    public ResponseEntity<Map<String, Object>> getNoticeList(@RequestBody DesignatedDonationSearchParam noticeParam){
    	noticeParam.setDisplay("FRONT");

    	List<DesignatedDonationNotice> noticeList = designatedDonationService.selectPrjNoticeListFront(noticeParam);

    	for(DesignatedDonationNotice notice : noticeList) {
    		List<PrjNoticeFile> prjFiles = notice.getOriginalPrjFiles();
    		for(PrjNoticeFile prjNoticeFile : prjFiles) {
    			try {
					prjNoticeFile.setFileName(pCrypto.Encrypt("normal", prjNoticeFile.getFileName(), ""));
				} catch (Exception e) {
					log.error("ERROR: {}", "=========== getNoticeList =============");
				}

    		}
    	}


        return ApiResponseEntity.data()
				.put("content", noticeList)
        		.put("noticeListCurrentPage", noticeParam.getPagination().getCurrentPage())
        		.put("noticeListTotalCnt", noticeParam.getPagination().getTotalItems())
//        		.put("noticeListItemPerPage", noticeParam.getPagination().getItemsPerPage())
        		.put("noticeListTotalPages", noticeParam.getPagination().getTotalPages())
				.ok();
    }



}

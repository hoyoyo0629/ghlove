package saleson.shop.welfarecenter;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.enumeration.IdType;
import saleson.common.utils.UserUtils;
import saleson.shop.welfarecenter.domain.WlfrCntrMng;
import saleson.shop.welfarecenter.support.WlfrCntrMngParam;
import saleson.shop.slave.SlaveOffgiveMapper;
import saleson.shop.user.LocgovService;
import saleson.shop.user.ManagerRequestService;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.domain.ManagerRequestResult;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequest;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequestResult;
import saleson.shop.welfarecenter.support.WlfrCntrMngManagerRequestSearchParam;

@Service("welfareCenterService")
public class WelfareCenterServiceImpl implements WelfareCenterService {

	private static final Logger log = LoggerFactory.getLogger(WelfareCenterServiceImpl.class);

    @Autowired
    private WelfareCenterMapper welfareCenterMapper;

    @Autowired
    private SlaveOffgiveMapper slaveOffgiveMapper;

    @Autowired
    private ManagerRequestService managerRequestService;

    @Autowired
    private LocgovService locgovService;


    /**
	 * 로그인 사용자 지자체코드정보 조회
	 * @param userId
	 * @return
	 */
	@Override
	public String getLocgovCodeByUserId(Long userId) {
		return slaveOffgiveMapper.getLocgovCodeByUserId(userId);
	}

	/**
	 * 행정복지센터 권한 승인관리 - '거절'
	 * @param WlfrCntrMngManagerRequest
	 * @return
	 */
    @Override
	public WlfrCntrMngManagerRequestResult updateManagerRequestReject(WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest) {
    	WlfrCntrMngManagerRequestResult wlfrCntrMngResult = new WlfrCntrMngManagerRequestResult();

		ManagerRequest param = new ManagerRequest();
		param.setUserId(wlfrCntrMngManagerRequest.getUserId());
		param.setReqstSn(wlfrCntrMngManagerRequest.getReqstSn());
		param.setLastUpdusrId(UserUtils.getUser().getUserId());
		param.setConfmSttusCode(wlfrCntrMngManagerRequest.getConfmSttusCode());
		param.setRejectResn(wlfrCntrMngManagerRequest.getRejectResn());

		ManagerRequestResult result = managerRequestService.updateManagerRequestReject(param);

		wlfrCntrMngResult.setCode(result.getCode());
		wlfrCntrMngResult.setStatusCode(result.getStatusCode());

		return wlfrCntrMngResult;
	}

	/**
	 * 행정복지센터 권한 승인관리 - '승인'
	 * @param wlfrCntrMngManagerRequest
	 * @return
	 */

	@Override
	public WlfrCntrMngManagerRequestResult updateManagerRequestApproval(WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest) {
		WlfrCntrMngManagerRequestResult wlfrCntrMngManagerRequestResult = new WlfrCntrMngManagerRequestResult();

		ManagerRequest param = new ManagerRequest();
		param.setUserId(wlfrCntrMngManagerRequest.getUserId());
		param.setReqstSn(wlfrCntrMngManagerRequest.getReqstSn());
		param.setLastUpdusrId(UserUtils.getUser().getUserId());
		param.setConfmSttusCode(wlfrCntrMngManagerRequest.getConfmSttusCode());
		param.setRejectResn(wlfrCntrMngManagerRequest.getRejectResn());

		ManagerRequestResult result = managerRequestService.updateManagerRequestApproval(param);
		wlfrCntrMngManagerRequestResult.setCode(result.getCode());
		wlfrCntrMngManagerRequestResult.setStatusCode(result.getStatusCode());


		switch (wlfrCntrMngManagerRequestResult.getCode()) {
		case "NO_AUTH":
			wlfrCntrMngManagerRequestResult.setStatusCode("901");
			break;
		case "FAIL":
			wlfrCntrMngManagerRequestResult.setStatusCode("902");
			break;
		}

		return wlfrCntrMngManagerRequestResult;
	}

	/**
	 * 행정복지센터 권한 수정
	 * @param wlfrCntrMngManagerRequest
	 * @return
	 */

	@Override
	public int updateManagerRequest(WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest) {
		ManagerRequest param = new ManagerRequest();
		param.setReqstSn(wlfrCntrMngManagerRequest.getReqstSn());
		param.setPsitnNm(wlfrCntrMngManagerRequest.getPsitnNm());
		param.setPsitnCode(wlfrCntrMngManagerRequest.getPsitnCode());

		return welfareCenterMapper.updateManagerRequest(param);
	}

	 /**
	 * 행정복지센터 권한승인 목록 조회
	 * @param wlfrCntrMngManagerRequest
	 * @return
	 */
	@Override
	public List<WlfrCntrMngManagerRequest> getManagerRequestListByParam(WlfrCntrMngManagerRequestSearchParam searchParam) {

		if (SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			searchParam.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		searchParam.setReqstSeCode("ROLE_ADMIN_11");
		if (!UserUtils.hasMasterManagerRole()) {
			searchParam.setSuperUserId(UserUtils.getUser().getUserId());
		}
		int cnt = welfareCenterMapper.getManagerRequestCountByParam(searchParam);
		if (searchParam.getItemsPerPage() < 10) {
			searchParam.setItemsPerPage(10);
		}

		Pagination pagination = Pagination.getInstance(cnt, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		return welfareCenterMapper.getManagerRequestListByParam(searchParam);
	}

	@Override
	public WlfrCntrMngManagerRequest getManagerRequestDetails(WlfrCntrMngManagerRequestSearchParam searchParam) {
		return welfareCenterMapper.getManagerRequestDetails(searchParam);
	}

	@Override
	public List<WlfrCntrMngManagerRequest> getWlfrCntrMngHistory(WlfrCntrMngManagerRequestSearchParam searchParam) {
		return welfareCenterMapper.getWlfrCntrMngHistory(searchParam);
	}


	/**
     * 행정복지센터 목록 조회
     */
    @Override
    public List<WlfrCntrMng> selectWlfrCntrMngList(WlfrCntrMngParam wlfrCntrMngParam) {

    	int selectWlfrCntrMngListCnt = welfareCenterMapper.selectWlfrCntrMngListCnt(wlfrCntrMngParam);
		Pagination pagination = Pagination.getInstance(selectWlfrCntrMngListCnt);

		wlfrCntrMngParam.setPagination(pagination);

		return welfareCenterMapper.selectWlfrCntrMngList(wlfrCntrMngParam);
    };

    /**
     * 행정복지센터 정보 조회
     */
    @Override
    public List<WlfrCntrMng> wlfrCntrMngInfo(WlfrCntrMng wlfrCntrMng) {
		return welfareCenterMapper.wlfrCntrMngInfo(wlfrCntrMng);

    };

    /**
     * 행정복지센터 정보 상세 조회
     */
    @Override
    public WlfrCntrMng selectwlfrCntrMngDetail(Long pbadmsWlfrCntrId) {
        return welfareCenterMapper.selectwlfrCntrMngDetail(pbadmsWlfrCntrId);
    }

    /**
     * 행정복지센터 목록 카운트 조회
     */
    @Override
    public int selectWlfrCntrMngListCnt(WlfrCntrMngParam wlfrCntrMngParam) {
    	return welfareCenterMapper.selectWlfrCntrMngListCnt(wlfrCntrMngParam);

    };

    /**
     * 행정복지센터 등록
     */
    @Override
    public int insertWlfrCntrMng(WlfrCntrMng wlfrCntrMng) {
    	return welfareCenterMapper.insertWlfrCntrMng(wlfrCntrMng);

    };

    /**
     * 행정복지센터 수정
     */
    @Override
    public int updatetWlfrCntrMng(WlfrCntrMngParam wlfrCntrMngParam) {
    	return welfareCenterMapper.updatetWlfrCntrMng(wlfrCntrMngParam);
    }

    /**
     * 행정복지센터 중복조회
     */
	@Override
	public boolean isDuplicateWlfrCntrMng(WlfrCntrMng wlfrCntrMng) {
        int dupCnt = welfareCenterMapper.isDuplicateWlfrCntrMng(wlfrCntrMng);
        boolean result = dupCnt > 0 ? true : false;
		return result;
    }


}

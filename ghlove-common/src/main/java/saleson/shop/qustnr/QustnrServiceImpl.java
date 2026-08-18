package saleson.shop.qustnr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.UserAdminRole;
import saleson.common.utils.UserUtils;
import saleson.shop.qustnr.domain.Qestnar;
import saleson.shop.qustnr.domain.QestnarResponseDto;
import saleson.shop.qustnr.domain.QustnrIem;
import saleson.shop.qustnr.domain.QustnrQesitm;
import saleson.shop.qustnr.domain.QustnrRspnsResult;
import saleson.shop.qustnr.domain.SurveyVerificationResults;
import saleson.shop.qustnr.support.QustnrDetailParams;
import saleson.shop.qustnr.support.QustnrSearchParam;
import saleson.shop.user.ManagerRequestMapper;

@Slf4j
@RequiredArgsConstructor
@Service("qustnrService")
public class QustnrServiceImpl extends EgovAbstractServiceImpl implements QustnrService{

	@Autowired
	private QustnrMapper qustnrMapper;
	private final ManagerRequestMapper managerRequestMapper;

	@Override
	public int getQustnrListCnt(QustnrSearchParam searchParam) {
		return qustnrMapper.getQustnrListCnt(searchParam);
	}

	@Override
	public List<Qestnar> getQustnrList(QustnrSearchParam searchParam) {
		return qustnrMapper.getQustnrList(searchParam);
	}

	@Override
	public int insertQustnr(Qestnar qestnar) {

		long userId = UserUtils.getUser().getUserId();

		qestnar.setFrstRegisterId(userId);

		qustnrMapper.insertQustnr(qestnar);

		long sn = 0;
		for (QustnrQesitm question : qestnar.getQustnrQesitm()) {

			question.setQustnrSn(qestnar.getQustnrSn());
			question.setFrstRegisterId(userId);
			qustnrMapper.insertQustnrQesitm(question);

			for (QustnrIem qustnrIem : question.getQustnrIem()) {
				qustnrIem.setQustnrSn(qestnar.getQustnrSn());
				qustnrIem.setQustnrQesitmSn(question.getQustnrQesitmSn());
				qustnrIem.setFrstRegisterId(userId);

				qustnrMapper.insertQustnrIem(qustnrIem);
			}


		}

		return 0;
	}

	@Override
	public Qestnar getQustnr(long qustnrSn) {
		QustnrDetailParams params = QustnrDetailParams.builder().qustnrSn(qustnrSn).build();
		Qestnar qestnar = qustnrMapper.getQustnr(qustnrSn);
		qestnar.setQustnrQesitmList(qustnrMapper.getQustnrQesitmDetail(params));

		return qestnar;
	}

	@Override
	public int deleteQustrn(ListParam listParam) {

		if (listParam != null && listParam.getId() != null) {
			for (String q : listParam.getId()) {
				long qustrnSn = Long.parseLong(q);

				qustnrMapper.deleteQustnrRspnsResult(qustrnSn);
				qustnrMapper.deleteQustnrIem(qustrnSn);
				qustnrMapper.deleteQustnrQesitm(qustrnSn);
				qustnrMapper.deleteQustnr(qustrnSn);

			}
		}

		return 0;
	}

	@Override
	public int editQustnr(Qestnar qestnar) {

		long userId = UserUtils.getUser().getUserId();

		qestnar.setFrstRegisterId(userId);
		qustnrMapper.updateQustnr(qestnar);

		for (QustnrQesitm question : qestnar.getQustnrQesitm()) {

			question.setQustnrSn(qestnar.getQustnrSn());
			int qesitmCheck = qustnrMapper.checkQusitm(question);

			if (question.getQustnrQesitmSn() > 0 && qesitmCheck > 0) {
				question.setLastUpdusrId(userId);
				qustnrMapper.updateQustnrQesitm(question);
			} else {
				question.setFrstRegisterId(userId);
				qustnrMapper.insertQustnrQesitm(question);
			}

			for (QustnrIem qustnrIem : question.getQustnrIem()) {

				qustnrIem.setQustnrSn(qestnar.getQustnrSn());
				qustnrIem.setQustnrQesitmSn(question.getQustnrQesitmSn());
				int iemCheck = qustnrMapper.checkIEM(qustnrIem);

				if (qustnrIem.getQustnrIemSn() > 0 && iemCheck > 0) {
					qustnrIem.setLastUpdusrId(userId);
					qustnrMapper.updateQustnrIem(qustnrIem);
				} else {
					qustnrIem.setFrstRegisterId(userId);
					qustnrMapper.insertQustnrIem(qustnrIem);
				}

			}
			// 저장, 또는 수정 되지 않은 답변 삭제
			qustnrMapper.deleteQustnrIemAll(question);

		}

		qustnrMapper.deleteQustnrQesitmAll(qestnar);

		return 0;
	}

	@Override
	public int getQustnrRspnsResultCnt(long qustnrSn) {
		// TODO Auto-generated method stub
		return qustnrMapper.getQustnrRspnsResultCnt(qustnrSn);
	}

	@Override
	public int insertQustnrRspnsResult(List<QustnrRspnsResult> answerList) {

		long userId = UserUtils.getUser().getUserId();

		for (QustnrRspnsResult q : answerList) {
			q.setUserId(userId);
		}

		return qustnrMapper.insertQustnrRspnsResult(answerList);
	}

	@Override
	public QestnarResponseDto getQustnrByApi(long qustnrSn) {

		long userId = -1;
		long rspnsCount = -1;

		if (UserUtils.getUser() == null) {
			return QestnarResponseDto.builder().qestnar(null).resultCode(SurveyVerificationResults.FAIL_INCORRECT_APPROACH.getCode()).resultMsg(SurveyVerificationResults.FAIL_INCORRECT_APPROACH.getMsg()).build();
		}else {
			userId = UserUtils.getUser().getUserId();
		}

		rspnsCount = qustnrMapper.getQustnrRspnsResultCnt(qustnrSn);

		QustnrDetailParams params = QustnrDetailParams.builder()
													  .qustnrSn(qustnrSn)
													  .userId(userId)
													  .build();

		Qestnar qestnar = new Qestnar();

		UserAdminRole role = UserAdminRole.findByUserRole(managerRequestMapper.getUserRole(userId));
		qestnar = qustnrMapper.getQustnrAndValidate(params);

		if(qestnar.getQustnrCnt() == 0 || qestnar.getRegCnt() > 0) {
			SurveyVerificationResults survey = qestnar.getQustnrCnt() == 0 ? SurveyVerificationResults.FAIL_NOT_EXIST : SurveyVerificationResults.FAIL_AlREADY_DONE;
			return QestnarResponseDto.builder().qestnar(qestnar).resultCode(survey.getCode()).resultMsg(survey.getMsg()).build();
		}
		else if(!UserAdminRole.LOC.equals(role) && qestnar.getSrvyTrgt().equals("M")) {
			return QestnarResponseDto.builder().qestnar(qestnar).resultCode(SurveyVerificationResults.FAIL_INCORRECT_APPROACH.getCode()).resultMsg(SurveyVerificationResults.FAIL_INCORRECT_APPROACH.getMsg()).build();
		} else {
			qestnar.setQustnrQesitmList(qustnrMapper.getQustnrQesitmDetail(params));
			return QestnarResponseDto.builder().qestnar(qestnar).rspnsCount(rspnsCount).resultCode(SurveyVerificationResults.SUCCESS.getCode()).resultMsg(SurveyVerificationResults.SUCCESS.getMsg()).build();
		}
	}

	@Override
	public QestnarResponseDto checkQustnr(long qustnrSn, long userId) {

		return null;

	}

}

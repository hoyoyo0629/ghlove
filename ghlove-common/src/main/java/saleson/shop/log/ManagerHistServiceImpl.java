package saleson.shop.log;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.UserUtils;
import saleson.shop.log.domain.ManagerHist;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Service("managerHistService")
public class ManagerHistServiceImpl extends EgovAbstractServiceImpl implements ManagerHistService {
	private static final Logger log = LoggerFactory.getLogger(ManagerHistServiceImpl.class);

	private final ManagerHistMapper managerHistMapper;

	/**
	 * 관리자 정보 변경이력 추가
	 * @param T histParam, type: B - 배치 / H - 사용자직접조작
	 * @return 추가성공 시 1
	 */
	@Override
	public <T> int insertManagerHist(T histParam, String type) {
		int addResult = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			ManagerHist managerHist = mapper.convertValue(histParam, ManagerHist.class);

			if ("B".equals(type)) {
				managerHist.setRegUsertxt("batch");
			} else {
				if (null != UserUtils.getUser()) {
					managerHist.setRegUsertxt(String.valueOf(UserUtils.getUser().getUserId()));
				} else {
					// 로그인 전인 경우 (예) 관리자 이메일 정보 없을때, 로그인 전에 이메일 정보 업데이트 함
					if (null != managerHist.getLoginId()
							&& !"".equals(managerHist.getLoginId())) {
						ManagerHist manager = managerHistMapper.getMangerUserIdByLoginId(managerHist.getLoginId());
						managerHist.setRegUsertxt(String.valueOf(manager.getUserId()));
					}
				}
			}
			addResult = managerHistMapper.insertManagerHist(managerHist);

		} catch (IllegalArgumentException e1) {
			log.error("ERROR: {}", getClass().getName() + " :: insertManagerHist IllegalArgumentException ===========");
		} catch (Exception e2) {
			log.error("ERROR: {}", getClass().getName() + " :: insertManagerHist Exception ===========");
		}

		return addResult;
	}
}

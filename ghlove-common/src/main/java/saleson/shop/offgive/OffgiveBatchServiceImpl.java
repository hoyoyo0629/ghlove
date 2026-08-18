package saleson.shop.offgive;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service("offgiveBatchService")
@RequiredArgsConstructor
public class OffgiveBatchServiceImpl extends EgovAbstractServiceImpl implements OffgiveBatchService {

	private static final Logger logger = LoggerFactory.getLogger(OffgiveBatchServiceImpl.class);

	@Autowired
	OffgiveMapper offgiveMapper;

	/**
	 *  오프라인 기부 답례품 주문 건 중 기부 수납처리가 안된
	 *  답례품 주문건수를 화면에 보이지 않게
	 *  상태 변경
	 */
	@Override
	public void ItemStatusUpdateBatch() {
		try {
			// 오프라인기부이고, 미수납이며 90일전부터 ~ 10일전까지 조회
			// 주문상태가 입금대기 상태만 조회 추가
			List<Map<String, Object>> offgiveItemList = offgiveMapper.selectOffItem();

			// 0개 이면 답례품 주문건수가 없음
			if(offgiveItemList.size() > 0) {

				// filler9 cntr PK로 기부 순회하여 기부건 더블 체크
				for(int i = 0; i < offgiveItemList.size(); i++) {
					String cntr_sn = offgiveItemList.get(i).get("filler9").toString();

					Map<String, Object> giveSunap = offgiveMapper.selectGiveSunapCheck(cntr_sn);

					// 답례품 주문은 있지만 기부 데이터가 없는경우 체크
					if(giveSunap != null) {
						offgiveMapper.updateItemStatus(giveSunap.get("cntr_sn").toString());
					}
				}
			}
		} catch (Exception e) {
			logger.error("[OffGiveItemStatus >>> Update Error] ---->>>>", e.getMessage());
			logger.error("ItemStatusUpdateBatch Error", e);
		}
	}

}

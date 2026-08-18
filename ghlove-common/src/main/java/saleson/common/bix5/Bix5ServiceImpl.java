package saleson.common.bix5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bix5Service")
public class Bix5ServiceImpl implements Bix5Service{

	@Autowired
	Bix5Mapper bix5Mapper;

	/**
	 * BIX5 통계를 위한 배치
	 * 1. 기부금액(전년비)
	 * 2. 기부횟수(전년비)
	 */
	@Override
	public void bix5CntrAmt() {
		// 데이터 삭제 후 INSERT
		bix5Mapper.deleteBix5CntrAmt();
		bix5Mapper.bix5CntrAmt();
	};

	/**
	 * 3. 평균 기부금액 비교
	 */

	/**
	 * 4. 전년대비 월별 기부금액
	 */
	@Override
	public void bix5CntrAmtMonth() {
		bix5Mapper.bix5CntrAmtMonth();
	};

	/**
	 * 5. 대상별 기부금액
	 */
	@Override
	public void bix5CntrAmtBiz() {
		bix5Mapper.deleteBix5CntrAmtBiz();
		bix5Mapper.bix5CntrAmtBiz();
	};

	/**
	 * 6. 특정사업별 모금현황
	 */
	@Override
	public void bix5CntrBiz() {
		bix5Mapper.deleteBix5CntrBiz();
		bix5Mapper.bix5CntrBiz();
	};

	/**
	 * 7. 기부 접수창구별 모금액
	 */
	@Override
	public void bix5CntrPath(){
		bix5Mapper.deleteBix5CntrPath();
		bix5Mapper.bix5CntrPath();
	};

	/**
	 * 8. 기부 금액별 건수
	 */
	@Override
	public void bix5CntrAmtCat() {
		bix5Mapper.deleteBix5CntrAmtCat();
		bix5Mapper.bix5CntrAmtCat();
	};

	/**
	 * 9. 연령대별 기부 건수
	 */
	@Override
	public void bix5CntrOld(){
		bix5Mapper.deleteBix5CntrOld();
		bix5Mapper.bix5CntrOld();
	};

	/**
	 * 10. 기부자 주소지 기준 모금 순위 TOP 10
	 */
	@Override
	public void bix5CntrAddr(){
		bix5Mapper.deleteBix5CntrAddr();
		bix5Mapper.bix5CntrAddr();
	};

	/**
	 * 11. 판매 포인트(전년비)
	 */
	@Override
	public void bix5GiftUsePoint() {
		bix5Mapper.deleteBix5GiftUsePoint();
		bix5Mapper.bix5GiftUsePoint();
	}
	/**
	 * 12. 답례품 종 개수
	 */
	@Override
	public void bix5GiftCnt() {
		bix5Mapper.deleteBix5GiftCnt();
		bix5Mapper.bix5GiftGiftCnt();
	}
	/**
	 * 13. 평균 판매 포인트 비교
	 */
	@Override
	public void bix5GiftUsePointCpr() {
		bix5Mapper.deleteBix5GiftUsePointCpr();
		bix5Mapper.bix5GiftUsePointCpr();
	}

	/**
	 * 14. 답례품 등록 현황-카테고리 기준
	 */
	@Override
	public void bix5GiftCat() {
		bix5Mapper.deleteBix5GiftCat();
		bix5Mapper.bix5GiftCat();
	};

	/**
	 * 15. 답례품 등록 현황(포인트 구간 기준)
	 */
	@Override
	public void bix5GiftPoint() {
		bix5Mapper.deleteBix5GiftPoint();
		bix5Mapper.bix5GiftPoint();
	};

	/**
	 * 16. 카테고리별 판매수량
	 */
	@Override
	public void bix5GiftCatSell() {
		bix5Mapper.deleteBix5GiftCatSell();
		bix5Mapper.bix5GiftCatSell();
	};

	/**
	 * 17. 답례품 제공자 발송 소요 시간 TOP 5
	 */
	@Override
	public void bix5GiftDeli() {
		bix5Mapper.deleteBix5GiftDeli();
		bix5Mapper.bix5GiftDeli();
	};

	/**
	 * 18. 총 잔여 포인트(소진율)
	 */
	@Override
	public void bix5PointUse() {
		bix5Mapper.deleteBix5PointUse();
		bix5Mapper.bix5PointUse();
	};

	/**
	 * 19. 연간 발생 포인트
	 */
	@Override
	public void bix5PointUseYear() {
		bix5Mapper.deleteBix5PointUseYear();
		bix5Mapper.bix5PointUseYear();
	};

	/**
	 * 20. 포인트 현황
	 */
	@Override
	public void bix5PointYear() {
		bix5Mapper.deleteBix5PointYear();
		bix5Mapper.bix5PointYear();
	};

}

package saleson.common.bix5;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("bix5Mapper")
public interface Bix5Mapper {

	/**
	 * BIX5 통계를 위한 배치
	 * 1. 기부금액(전년비)
	 * 2. 기부횟수(전년비)
	 */
	void bix5CntrAmt();
	void deleteBix5CntrAmt();

	/**
	 * 3. 평균 기부금액 비교
	 */

	/**
	 * 4. 전년대비 월별 기부금액
	 */
	void bix5CntrAmtMonth();

	/**
	 * 5. 대상별 기부금액
	 */
	void bix5CntrAmtBiz();
	void deleteBix5CntrAmtBiz();

	/**
	 * 6. 특정사업별 모금현황
	 */
	void bix5CntrBiz();
	void deleteBix5CntrBiz();
	/**
	 * 7. 기부 접수창구별 모금액
	 */
	void bix5CntrPath();
	void deleteBix5CntrPath();

	/**
	 * 8. 기부 금액별 건수
	 */
	void bix5CntrAmtCat();
	void deleteBix5CntrAmtCat();

	/**
	 * 9. 연령대별 기부 건수
	 */
	void bix5CntrOld();
	void deleteBix5CntrOld();

	/**
	 * 10. 기부자 주소지 기준 모금 순위 TOP 10
	 */
	void bix5CntrAddr();
	void deleteBix5CntrAddr();

	/**
	 * 11. 판매 포인트(전년비)
	 */
	void deleteBix5GiftUsePoint();
	void bix5GiftUsePoint();

	/**
	 * 12. 답례품 종 개수
	 */
	void deleteBix5GiftCnt();
	void bix5GiftGiftCnt();

	/**
	 * 13. 평균 판매 포인트 비교
	 */
	void deleteBix5GiftUsePointCpr();
	void bix5GiftUsePointCpr();

	/**
	 * 14. 답례품 등록 현황-카테고리 기준
	 */
	void deleteBix5GiftCat();
	void bix5GiftCat();

	/**
	 * 15. 답례품 등록 현황(포인트 구간 기준)
	 */
	void deleteBix5GiftPoint();
	void bix5GiftPoint();

	/**
	 * 16. 카테고리별 판매수량
	 */
	void deleteBix5GiftCatSell();
	void bix5GiftCatSell();

	/**
	 * 17. 답례품 제공자 발송 소요 시간 TOP 5
	 */
	void deleteBix5GiftDeli();
	void bix5GiftDeli();

	/**
	 * 18. 총 잔여 포인트(소진율)
	 */
	void deleteBix5PointUse();
	void bix5PointUse();

	/**
	 * 19. 연간 발생 포인트
	 */
	void deleteBix5PointUseYear();
	void bix5PointUseYear();

	/**
	 * 20. 포인트 현황
	 */
	void deleteBix5PointYear();
	void bix5PointYear();
}

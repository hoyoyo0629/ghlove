package saleson.shop.give.givepoint;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import saleson.shop.give.givepoint.domain.GivePoint;

public interface GivePointService {

	// 기부금 모금현황 합계
	public GivePoint getGivePointSum(GivePoint givePoint);

	// 기부금 모금현황 목록 count
	public int getGivePointListCount(GivePoint givePoint);

	// 기부금 모금현황 목록
	public List<GivePoint> getGivePointList(GivePoint givePoint);

	// 기부금 모금현황 목록 엑셀다운로드
	public SXSSFWorkbook streamGivePointDetailData(GivePoint givePoint, int totalCount);

	// 기부금 모금현황 상세 누적합계
	public GivePoint getGivePointDetail(GivePoint givePoint);

	// 기부금 모금현황 상세 검색합계
	public GivePoint getGivePointDetailSum(GivePoint givePoint);

	// 기부금 모금현황 상세 목록 count
	public int getGivePointDetailListCount(GivePoint givePoint);

	// 기부금 모금현황 상세 목록
	public List<GivePoint> getGivePointDetailList(GivePoint givePoint);

	// 지자체 코드 조회
	public HashMap<String, Object> getLocgovCode(long userId);

	// 지자체 기부 포인트현황 목록 count
	public int getGivePointLocgovListCount(GivePoint givePoint);

	// 지자체 기부 포인트현황 목록 count
	public List<GivePoint> getGivePointLocgovList(GivePoint givePoint);

	// 기부 포인트현황 목록 엑셀 다운로드
	public SXSSFWorkbook streamGivePointList(GivePoint givePoint, boolean isLocgov);

}

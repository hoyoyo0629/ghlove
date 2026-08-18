package saleson.shop.give.givepoint;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.util.StringUtils;

import saleson.common.file.ExcelCellStyleUtils;
import saleson.shop.give.givepoint.domain.GivePoint;
import saleson.shop.slave.SlaveGivePointMapper;

@Service("givePointService")
public class GivePointServiceImpl implements GivePointService {

	@Autowired
	SlaveGivePointMapper slaveGivePointMapper;

	/**
	 * 기부 포인트현황 합계
	 */
	@Override
	public GivePoint getGivePointSum(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointSum(givePoint);
	}

	/**
	 * 기부 포인트현황 목록 count
	 */
	@Override
	public int getGivePointListCount(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointListCount(givePoint);
	}

	/**
	 * 기부 포인트현황 목록
	 */
	@Override
	public List<GivePoint> getGivePointList(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointList(givePoint);
	}

	/**
	 * 기부 포인트현황 상세 누적합계
	 */
	@Override
	public GivePoint getGivePointDetail(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointDetail(givePoint);
	}

	/**
	 * 기부 포인트현황 상세 검색합계
	 */
	@Override
	public GivePoint getGivePointDetailSum(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointDetailSum(givePoint);
	}

	/**
	 * 기부 포인트현황 상세 목록 count
	 */
	@Override
	public int getGivePointDetailListCount(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointDetailListCount(givePoint);
	}

	/**
	 * 기부 포인트현황 상세 목록
	 */
	@Override
	public List<GivePoint> getGivePointDetailList(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointDetailList(givePoint);
	}

	/**
	 * 기부 포인트현황 상세 목록 엑셀 다운로드
	 */
	@Override
	public SXSSFWorkbook streamGivePointDetailData(GivePoint searchParam, int totalCount) {
		int pageSize 	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize = 1000;
		offset = 1;

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		searchParam.getPagination().setItemsPerPage(pageSize);
		searchParam.getPagination().setCurrentPage(offset);

		// SXSSF	: window size = 100
		// workbook	: 엑셀생성을 위한 내부 문서 모델
		// 메모리 적재 최대 100 row 로 설정, 나머지는 disk 로 flush
		// disk 저장 파일은 압축
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("GIVE_POINT_DETAIL");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "기부 포인트 상세현황 목록(" + searchParam.getLocgovFullNm() + ")");
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0,		2000);
		sheet.setColumnWidth(1,		5000);
		sheet.setColumnWidth(2,		7500);
		sheet.setColumnWidth(3,		5000);
		sheet.setColumnWidth(4,		5000);
		sheet.setColumnWidth(5,		5000);
		sheet.setColumnWidth(6,		5000);
		sheet.setColumnWidth(7,		5000);
		sheet.setColumnWidth(8,		5000);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0,		"No");
		cellStyle.header(header, 1,		"납부일자");
		cellStyle.header(header, 2,		"납부번호");
		cellStyle.header(header, 3,		"기부자명");
		cellStyle.header(header, 4,		"아이디");
		cellStyle.header(header, 5,		"기부금액");
		cellStyle.header(header, 6,		"발생포인트");
		cellStyle.header(header, 7,		"사용포인트");
		cellStyle.header(header, 8,		"포인트잔액");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;

			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			List<GivePoint> givePointList = slaveGivePointMapper.getGivePointDetailList(searchParam);

			// 1000건 데이터를 엑셀 시트에 입력 가능하도록 row 단위의 전처리 실행
			for (GivePoint givePoint : givePointList) {
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0,	StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1,	givePoint.getSttemntPayDe());
				cellStyle.data(row, 2,	givePoint.getElctrnPayNo());
				cellStyle.data(row, 3,	givePoint.getUserName());
				cellStyle.data(row, 4,	givePoint.getLoginId());
				cellStyle.data(row, 5,	givePoint.getCntrAmt());
				cellStyle.data(row, 6,	givePoint.getCntrPoint());
				cellStyle.data(row, 7,	givePoint.getCntrUsePoint());
				cellStyle.data(row, 8,	givePoint.getCntrBlcePoint());
			}

			// 다음 1000 row 조회를 위한 offset 설정
			// 쿼리문
			// LIMIT (#{pagination.currentPage} - 1) * #{pagination.itemsPerPage}, #{pagination.itemsPerPage}
			searchParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
	}

	/**
	 * 회원 지자체 조회
	 */
	@Override
	public HashMap<String, Object> getLocgovCode(long userId) {
		return slaveGivePointMapper.getLocgovCode(userId);
	}

	/**
	 * 지자체 기부 포인트현황 목록 count
	 */
	@Override
	public int getGivePointLocgovListCount(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointLocgovListCount(givePoint);
	}

	/**
	 * 지자체 기부 포인트현황 목록
	 */
	@Override
	public List<GivePoint> getGivePointLocgovList(GivePoint givePoint) {
		return slaveGivePointMapper.getGivePointLocgovList(givePoint);
	}

	/**
	 * 기부 포인트현황 목록 엑셀 다운로드
	 */
	@Override
	public SXSSFWorkbook streamGivePointList(GivePoint givePoint, boolean isLocgov) {
		int pageSize	= 0;
		int offset		= 0;
		int rowNum		= 0;
		int limit		= 0;
		int cellCount 	= 0;
		int totalCount	= 0;

		givePoint.getPagination().setItemsPerPage(pageSize);
		givePoint.getPagination().setCurrentPage(offset);

		if (isLocgov) {
			totalCount = slaveGivePointMapper.getGivePointLocgovListCount(givePoint);
		} else {
			totalCount = slaveGivePointMapper.getGivePointListCount(givePoint);
		}

		limit = (int) Math.ceil((double) totalCount / 1000);

		pageSize 		= 1000;
		offset 			= 1;

		givePoint.getPagination().setItemsPerPage(pageSize);
		givePoint.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("GIVE_POINT_LIST");

		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "기부 포인트 현황");
		Integer lastColIndex;
		if (isLocgov) {
			cellCount = 0;
			sheet.setColumnWidth(cellCount++, 2000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);

			cellCount = 0;
			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, cellCount++, "No.");
			cellStyle.header(header, cellCount++, "년도");
			cellStyle.header(header, cellCount++, "기부금액");
			cellStyle.header(header, cellCount++, "발생포인트");
			cellStyle.header(header, cellCount++, "사용포인트");
			cellStyle.header(header, cellCount++, "포인트잔액");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			cellCount = 0;
			sheet.setColumnWidth(cellCount++, 2000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);
			sheet.setColumnWidth(cellCount++, 5000);

			cellCount = 0;
			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, cellCount++, "No.");
			cellStyle.header(header, cellCount++, "지자체명");
			cellStyle.header(header, cellCount++, "기부금액");
			cellStyle.header(header, cellCount++, "발생포인트");
			cellStyle.header(header, cellCount++, "사용포인트");
			cellStyle.header(header, cellCount++, "잔액누계");

			lastColIndex = header.getLastCellNum() - 1;
		}
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		boolean exceedTotalRow = false;

		while (!exceedTotalRow) {
			List<GivePoint> givePointList;
			if (isLocgov) {
				givePointList = slaveGivePointMapper.getGivePointLocgovList(givePoint);
			} else {
				givePointList = slaveGivePointMapper.getGivePointList(givePoint);
			}

			if (givePointList.isEmpty()) break;

			for (GivePoint givePointItem : givePointList) {
				Row row = sheet.createRow(rowNum++);
				cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(totalCount--));
				if (isLocgov) {
					cellStyle.data(row, cellCount++, String.valueOf(givePointItem.getCntrYear()));
				} else {
					cellStyle.data(row, cellCount++, givePointItem.getUpperLocgovNm() + givePointItem.getLocgovNm());
				}
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(givePointItem.getCntrAmt()));
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(givePointItem.getCntrPoint()));
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(givePointItem.getCntrUsePoint()));
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(givePointItem.getCntrBlcePoint()));
			}

			givePoint.getPagination().setCurrentPage(++offset);

			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}
		return workbook;
	}
}

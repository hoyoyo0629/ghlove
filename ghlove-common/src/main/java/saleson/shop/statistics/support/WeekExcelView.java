package saleson.shop.statistics.support;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import saleson.common.Const;
import saleson.shop.remittance.domain.RemittanceDetail;
import saleson.shop.statistics.domain.BaseStats;
import saleson.shop.statistics.domain.StatsSummary;
import saleson.shop.statistics.domain.order.OrderCountAmtStat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class WeekExcelView extends AbstractSXSSFExcelView {

	public WeekExcelView() {
		setFileName("주별 통계_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}

	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		Cookie cookie = new Cookie("DOWNLOAD_STATUS", "complete");
		cookie.setHttpOnly(true);
		cookie.setPath("/");					// 모든 경로에서 접근 가능하도록
		cookie.setSecure(true);					// 시큐어 코딩 점걸 처리함.(2022.02.21)
		response.addCookie(cookie);				// 쿠키저장

		// 1. 데이터 가져오기.
		List<OrderCountAmtStat> list = (List<OrderCountAmtStat>) model.get("list");

		// 2. 시트별 데이터 생성
		buildUserSheet(workbook, list, model.get("date").toString());

	}

	private void buildUserSheet(SXSSFWorkbook workbook, List<OrderCountAmtStat> list, String param) {
		String sheetTitle1 = "고향사랑기부제 기부 현황 일일보고("+ param +"기준)"; 	// 상품정보
		HeaderCell[] headerCells = new HeaderCell[] {
				new HeaderCell(512, "구분"),
				new HeaderCell(512, ""),
				new HeaderCell(512, param),

				new HeaderCell(512, "전일 대비"),
				new HeaderCell(2048, "전년 동안 대비(누적)"),
				new HeaderCell(512, "비고")
		};

		// 5. Row병합(세로)이 필요한 셀 인덱스 설정;
		//int[] mergeRowCellIndexes = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 22, 23, 24, 25 };

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(sheetTitle1);
		Row row = sheet.createRow((short) 0);
		//createSheetHeader(sheet, row, columWidth, titles, sheetTitle);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, sheetTitle1);

		row = sheet.createRow((short) 1);
		row.setHeight((short) 512);
		CellIndex cellIndex = new CellIndex(-1);
		setTextLeft(sheet, 1, cellIndex, "□총괄 현황");
		setText(sheet, 1, cellIndex, "");
		setText(sheet, 1, cellIndex, "");
		setText(sheet, 1, cellIndex, "");
		setText(sheet, 1, cellIndex, "");
		setText(sheet, 1, cellIndex, "");

		cellIndex = new CellIndex(-1);
		row = sheet.createRow(2);
		Cell[] cells = new Cell[headerCells.length];
		for (int j = 0; j < headerCells.length; j++) {
			cells[j] = row.createCell(j);
			cells[j].setCellType(CellType.STRING); //개행 문자 적용
			cells[j].setCellStyle(headerStyle);
			cells[j].setCellValue(headerCells[j].getTitle());
		}

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
		sheet.addMergedRegion(new CellRangeAddress(4, 5, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(6, 8, 0, 0));

		sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(10, 11, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(10, 11, 5, 5));

		sheet.addMergedRegion(new CellRangeAddress(18, 18, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(19, 19, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(20, 20, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(20, 20, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(20, 21, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(20, 21, 5, 5));

		// Table Body
		int rowIndex = 3;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);

		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "회원 수");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 4;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);

		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "기부");
		setText(sheet, rowIndex, cellIndex, "기부건수");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 5;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "기부금액");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");


		rowIndex = 6;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "답례품");
		setText(sheet, rowIndex, cellIndex, "등록건수");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");


		rowIndex = 7;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "신청건수");
		setTextRight(sheet, rowIndex, cellIndex, String.valueOf(list.get(0).getAddTotalCnt()));
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");


		rowIndex = 8;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "판매액");
		setTextRight(sheet, rowIndex, cellIndex, String.valueOf(list.get(0).getAddTotalSaleAmt()));
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");


		rowIndex = 9;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setTextLeft(sheet, rowIndex, cellIndex, "□기부현황");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		HeaderCell[] headerCells2 = new HeaderCell[] {
				new HeaderCell(512, "구분"),
				new HeaderCell(512, "2024"),
				new HeaderCell(512, ""),
				new HeaderCell(512, "2023"),
				new HeaderCell(512, ""),
				new HeaderCell(512, "비고")
		};
		rowIndex = 10;
		row = sheet.createRow(rowIndex);
		Cell[] cells2 = new Cell[headerCells2.length];
		for (int j = 0; j < headerCells2.length; j++) {
			cells2[j] = row.createCell(j);
			cells2[j].setCellType(CellType.STRING); //개행 문자 적용
			cells2[j].setCellStyle(headerStyle);
			cells2[j].setCellValue(headerCells2[j].getTitle());
		}

		HeaderCell[] headerCells3 = new HeaderCell[] {
				new HeaderCell(512, ""),
				new HeaderCell(512, param),
				new HeaderCell(512, "누계"),
				new HeaderCell(512, param),
				new HeaderCell(512, "누계"),
				new HeaderCell(512, "")
		};
		rowIndex = 11;
		row = sheet.createRow(rowIndex);
		Cell[] cells3 = new Cell[headerCells3.length];
		for (int j = 0; j < headerCells3.length; j++) {
			cells3[j] = row.createCell(j);
			cells3[j].setCellType(CellType.STRING); //개행 문자 적용
			cells3[j].setCellStyle(headerStyle);
			cells3[j].setCellValue(headerCells3[j].getTitle());
		}

		rowIndex = 12;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "금액 계");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 13;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "온라인");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 14;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "오프라인");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 15;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "건수 계");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 16;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "온라인");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 17;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "오프라인");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 18;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setTextLeft(sheet, rowIndex, cellIndex, "※누계는 해당연도 1.1. 부터 작성기준 날짜까지의 총합을 의미");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");


		rowIndex = 19;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setTextLeft(sheet, rowIndex, cellIndex, "□기부현황");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		HeaderCell[] headerCells4 = new HeaderCell[] {
				new HeaderCell(512, "구분"),
				new HeaderCell(512, "2024"),
				new HeaderCell(512, ""),
				new HeaderCell(512, "2023"),
				new HeaderCell(512, ""),
				new HeaderCell(512, "비고")
		};
		rowIndex = 20;
		row = sheet.createRow(rowIndex);
		Cell[] cells4 = new Cell[headerCells4.length];
		for (int j = 0; j < headerCells4.length; j++) {
			cells4[j] = row.createCell(j);
			cells4[j].setCellType(CellType.STRING); //개행 문자 적용
			cells4[j].setCellStyle(headerStyle);
			cells4[j].setCellValue(headerCells4[j].getTitle());
		}

		HeaderCell[] headerCells5 = new HeaderCell[] {
				new HeaderCell(512, ""),
				new HeaderCell(512, param),
				new HeaderCell(512, "누계"),
				new HeaderCell(512, param),
				new HeaderCell(512, "누계"),
				new HeaderCell(512, "")
		};
		rowIndex = 21;
		row = sheet.createRow(rowIndex);
		Cell[] cells5 = new Cell[headerCells5.length];
		for (int j = 0; j < headerCells5.length; j++) {
			cells5[j] = row.createCell(j);
			cells5[j].setCellType(CellType.STRING); //개행 문자 적용
			cells5[j].setCellStyle(headerStyle);
			cells5[j].setCellValue(headerCells5[j].getTitle());
		}

		rowIndex = 22;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "등록 건수");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 23;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "신청 건수");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");

		rowIndex = 24;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		cellIndex = new CellIndex(-1);
		setText(sheet, rowIndex, cellIndex, "총 판매액");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
		setText(sheet, rowIndex, cellIndex, "");
	}

}
package saleson.shop.statistics.support;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.NumberUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import saleson.common.Const;
import saleson.shop.statistics.domain.BaseStats;
import saleson.shop.statistics.domain.DateStatsSummary;
import saleson.shop.statistics.domain.MonthLocgovItemStat;
import saleson.shop.statistics.domain.StatsSummary;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class MonthLocgovStatExcel extends AbstractSXSSFExcelView {

	public MonthLocgovStatExcel(String fileName) {
		setFileName(fileName + "_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}

	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

	    // 데이터 생성(시트생성)
	    buildSheet(workbook, (List<MonthLocgovItemStat>)model.get("monthLocgovStatExcelList"), model.get("title"));
	}

	private void buildSheet(SXSSFWorkbook workbook, List<MonthLocgovItemStat> list, Object titleObj) {

		if(list == null){
			return;
		}
		
		// 3. 시트생성
		String sheetTitle = "RemittanceExcel";
		String title = "정산내역";
		if (titleObj != null) {
			title = titleObj.toString();
			sheetTitle = title;
		}
		
		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(sheetTitle);
		Row row = sheet.createRow((short) 0);
		
		createSheetHeader(sheet, row, title);
		
		CellIndex cellIndex;
		
		Row row1 = sheet.createRow((short) 3);			// 전체 통계
		row1.setHeight((short) 600);
		cellIndex = new CellIndex(-1);
		
		CellStyle cellStyle = alignCenterStyle;
		cellStyle.setWrapText(true);

		MonthLocgovItemStat total = list.get(0);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
		setText(sheet, 3, cellIndex, "합계(전체)");
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, "");
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getItemTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getTourTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getFarmTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getAquaticTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getProcessTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getDailyTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getTicketTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getOrderTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getSalePriceTotalAll()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		
		setText(sheet, 3, cellIndex, total.getAllTop1Name());
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getAllTop1Cnt()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, total.getAllTop2Name());
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getAllTop2Cnt()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, total.getAllTop3Name());
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);
		setText(sheet, 3, cellIndex, StringUtils.numberFormat(total.getAllTop3Cnt()));
		getCell(sheet, 3, cellIndex.getIndex()).setCellStyle(cellStyle);

		// Table Body
		int rowIndex = 4;
		
		// 행 높이 설정
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		
		for(MonthLocgovItemStat monthLocgovStatExcel : list){
			cellIndex = new CellIndex(-1);
			setText(sheet, rowIndex, cellIndex, monthLocgovStatExcel.getUpperLocgovNm());
			setText(sheet, rowIndex, cellIndex, monthLocgovStatExcel.getLocgovNm());
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getItemTotalCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getTourCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getFarmCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getAquaticCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getProcessCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getDailyCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getTicketCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getOrderTotalCnt()));
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getSalePrice()));
			
			setText(sheet, rowIndex, cellIndex, monthLocgovStatExcel.getTop1Name());
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getTop1Cnt()));
			setText(sheet, rowIndex, cellIndex, monthLocgovStatExcel.getTop2Name());
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getTop2Cnt()));
			setText(sheet, rowIndex, cellIndex, monthLocgovStatExcel.getTop3Name());
			setText(sheet, rowIndex, cellIndex, StringUtils.numberFormat(monthLocgovStatExcel.getTop3Cnt()));
			rowIndex++;
		}
	}
	
	/**
	 * 테이블 헤더 설정 
	 */
	public void createSheetHeader(Sheet sheet, Row row, String sheetTitle) {
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		row.setHeight((short) 800);
		
		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(1500, 	"지역",	""),
				new HeaderCell(1500, 	"지자체명",	""),
				new HeaderCell(1200, 	"합계",	""),
				new HeaderCell(1200, 	"관광서비스",	""),
				new HeaderCell(1200, 	"농축산물", 	""),
				new HeaderCell(1200, 	"수산물", 	""),
				new HeaderCell(1200, 	"가공식품", 	""),
				new HeaderCell(1200, 	"생활용품", 	""),
				new HeaderCell(1200, 	"지역상품권", 	""),
				new HeaderCell(1500, 	"답례품 선택건수", 	""),
				new HeaderCell(1500, 	"답례품 선택금액", 	""),
				new HeaderCell(6000, 	"가장많이 선택한 답례품명1", 	""),
				new HeaderCell(1500, 	"가장많이 선택한\n답례품 건수1", 	""),
				new HeaderCell(6000, 	"가장많이 선택한 답례품명2", 	""),
				new HeaderCell(1500, 	"가장많이 선택한\n답례품 건수2", 	""),
				new HeaderCell(6000, 	"가장많이 선택한 답례품명3", 	""),
				new HeaderCell(1500, 	"가장많이 선택한\n답례품 건수3", 	"")
		};
		
		int columCount = headerCells.length;
		for(int i = 0; i < headerCells.length; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + headerCells[i].getWidth());
		}
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (columCount - 1)));
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		
		Cell[] cells = new Cell[columCount];
		for(int i = 0; i < cells.length; i++){
			cells[i] = row.createCell(i);
			cells[i].setCellStyle(titleStyle);
		}
		
		cells[0].setCellValue(sheetTitle);

		Row row1 = sheet.createRow((short) 1);
		row1.setHeight((short) 512);
		
		CellStyle cellStyle = getHeaderCellStyle(IndexedColors.GREY_25_PERCENT.index);
		cellStyle.setWrapText(true);

		Cell[] cells1 = new Cell[columCount];
		for(int i = 0; i < cells.length; i++){
			cells1[i] = row1.createCell(i);
			cells1[i].setCellStyle(cellStyle);
		}
		
		Row row2 = sheet.createRow((short) 2);
		row2.setHeight((short) 512);

		Cell[] cells2 = new Cell[columCount];
		for(int i = 0; i < cells.length; i++){
			cells2[i] = row2.createCell(i);
			cells2[i].setCellStyle(cellStyle);
		}

		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 8));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 16));
		
		cells1[0].setCellValue("지역");
		cells1[1].setCellValue("지자체명");
		
		cells1[2].setCellValue("지자체별 답례품 선정/등록 현황");
		cells1[9].setCellValue("지자체별 답례품 구매 현황");
		
		for(int i = 0; i < cells.length; i++){
			cells[i] = row2.createCell(i);
			
//			cells[i].setCellType(CellType.STRING); //개행 문자 적용
			
			// 셀 타이틀 설정.
			cells[i].setCellValue(headerCells[i].getTitle().replace("(x)", ""));
			
			cells[i].setCellStyle(cellStyle);

			// 셀 코멘트
			if (headerCells[i].getComment() != null && !headerCells[i].getComment().equals("")) {
				setComment(cells[i], 1, i, headerCells[i].getComment().replace(", ", ",").replace(",", "\n"), headerCells[i].getCommentCol(), headerCells[i].getCommentRow());
			}
		}
		
	}

}
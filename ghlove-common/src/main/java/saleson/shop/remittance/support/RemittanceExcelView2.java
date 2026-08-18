package saleson.shop.remittance.support;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import saleson.common.Const;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class RemittanceExcelView2 extends AbstractSXSSFExcelView{
	
	public RemittanceExcelView2(String fileName) {
		
		setFileName(fileName + "_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}
	
	@Override
	public void buildExcelDocument(Map<String, Object> model,
			SXSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	    
	    // 데이터 생성(시트생성)
	    buildSheet(workbook, (HeaderCell[])model.get("headerCells"), (List<List<String>>)model.get("remittanceExcelList"), model.get("title"));
	}
	
	private void buildSheet(SXSSFWorkbook workbook, HeaderCell[] headerCells, List<List<String>> list, Object titleObj) {
		
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
		
		createSheetHeader(sheet, row, headerCells, title);
		
		// Table Body
		int rowIndex = 2;
		
		// 행 높이 설정
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		
		CellIndex cellIndex;
	
		for(List<String> remittanceExcel : list){
			cellIndex = new CellIndex(-1);

			for (String cell : remittanceExcel) {
				setText(sheet, rowIndex, cellIndex, cell);
			}
			rowIndex++;
		}
	}
	
	/**
	 * 테이블 헤더 설정 
	 */
	public void createSheetHeader(Sheet sheet, Row row, HeaderCell[] headerCells, String sheetTitle) {
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		row.setHeight((short) 800);
		
		int columCount = headerCells.length;
		for(int i = 0; i < headerCells.length; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + headerCells[i].getWidth());
		}
		
		Cell[] cells = new Cell[columCount];
		for(int i =0; i < cells.length; i++){
			cells[i] = row.createCell(i);
			cells[i].setCellStyle(titleStyle);
		}

		cells[0].setCellValue(sheetTitle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (columCount - 1))); 


		row = sheet.createRow((short) 1);
		row.setHeight((short) 512);
		
		for(int i = 0; i < cells.length; i++){
			cells[i] = row.createCell(i);
			
//			cells[i].setCellType(CellType.STRING); //개행 문자 적용
			
			// 셀 타이틀 설정.
			cells[i].setCellValue(headerCells[i].getTitle().replace("(x)", ""));
			
			cells[i].setCellStyle(getHeaderCellStyle(IndexedColors.GREY_25_PERCENT.index));

			// 셀 코멘트
			if (headerCells[i].getComment() != null && !headerCells[i].getComment().equals("")) {
				setComment(cells[i], 1, i, headerCells[i].getComment().replace(", ", ",").replace(",", "\n"), headerCells[i].getCommentCol(), headerCells[i].getCommentRow());
			}
		}
		
//		cells[11].setCellStyle(getHeaderCellStyle(IndexedColors.LEMON_CHIFFON.index));
//		cells[12].setCellStyle(getHeaderCellStyle(IndexedColors.LEMON_CHIFFON.index));
	}
	
}

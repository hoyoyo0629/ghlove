package saleson.shop.user.support;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.user.domain.PersonInCharge;

public class OffPersonInChargeExcelView extends AbstractSXSSFExcelView {
	
	private List<PersonInCharge> list;
	
	public OffPersonInChargeExcelView() {
		setFileName("OFFLINE_MANAGER_"+DateUtils.getToday(Const.DATETIME_FORMAT)+".xlsx");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void buildExcelDocument(Map<String, Object> model, SXSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 1. 목록 조회
		list = (List<PersonInCharge>)model.get("list");
		
		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);
	}
	
	private void buildItemSheet(SXSSFWorkbook workbook, List<PersonInCharge> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "offline_manager";
		String title = "오프라인담당자";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, "No."),
			new HeaderCell(2000,"구분"),
			new HeaderCell(5000, "소속지점"),
			new HeaderCell(2500, "아이디"),
			new HeaderCell(2000, "이름"),
			new HeaderCell(2000, "개인번호"),
			new HeaderCell(1000, "사용여부"),
			new HeaderCell(2000, "중지일자")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(sheetTitle);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title);

		// Table Body
		int rowIndex = 2;
		int index = 0;
		for(PersonInCharge personInCharge : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);
			
			String authority = "ROLE_ADMIN_7".equals(personInCharge.getAuthority()) ? "주관리자" : "-";
			String bankNm = this.nvl(personInCharge.getBankNm()) + " "+ this.nvl(personInCharge.getPsitnNm());
			String statusNm = personInCharge.getStatusCode() == 2 ? "중지" : "사용";
			String denyDate = personInCharge.getStatusCode() == 2 ? this.nvl(personInCharge.getDenyDate()) : "";

			setText(sheet, rowIndex, cellIndex, this.nvl(++index));							// No.
			setText(sheet, rowIndex, cellIndex, this.nvl(authority));						// 구분
			setText(sheet, rowIndex, cellIndex, this.nvl(bankNm));							// 소속 지점
			setText(sheet, rowIndex, cellIndex, this.nvl(personInCharge.getLoginId()));		// 아이디
			setText(sheet, rowIndex, cellIndex, this.nvl(personInCharge.getUserName()));	// 이름
			setText(sheet, rowIndex, cellIndex, this.nvl(personInCharge.getEmpId()));		// 개인번호
			setText(sheet, rowIndex, cellIndex, this.nvl(statusNm));						// 사용여부
			setText(sheet, rowIndex, cellIndex, this.nvl(denyDate));						// 중지일자

			rowIndex++;
		}
	}
	
	private String nvl(Object value) {
		return (value == null) ? "" : String.valueOf(value);
	}
}

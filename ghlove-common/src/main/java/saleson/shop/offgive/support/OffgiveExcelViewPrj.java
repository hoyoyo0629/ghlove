package saleson.shop.offgive.support;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.offgive.domain.Offgive;

public class OffgiveExcelViewPrj extends AbstractSXSSFExcelView{

	private List<Offgive> list;

	public OffgiveExcelViewPrj() {

		setFileName("OFFGIVE_" + DateUtils.getToday(Const.DATETIME_FORMAT) + "_PRJ.xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<Offgive>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<Offgive> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "offgive_main";
		String title = "기부금 접수관리-특정사업기부";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"신고일"),
			new HeaderCell(3500, 	"수납일"),
			new HeaderCell(3500, 	"이름"),
			new HeaderCell(3500, 	"기부 지자체"),
			new HeaderCell(3500, 	"기부 금액"),
			new HeaderCell(3500, 	"지점/센터명"),
			new HeaderCell(3500, 	"전자납부번호"),
			new HeaderCell(3500, 	"접수번호(지자체)"),
			new HeaderCell(3500, 	"상태"),
			new HeaderCell(6000, 	"특정사업기부 사업명"),
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
		for(Offgive obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, rowIndex, cellIndex, String.valueOf(obj.getFrstRegistPnttm()).substring(0, 19));
			setText(sheet, rowIndex, cellIndex, StringUtils.isEmpty(obj.getSttemntPayDe()) ? "" : obj.getSttemntPayDe().substring(0, 4) + "-" + obj.getSttemntPayDe().substring(4, 6) + "-" + obj.getSttemntPayDe().substring(6, 8) );
			setText(sheet, rowIndex, cellIndex, StringUtils.isEmpty(obj.getUserName()) ? "" : obj.getUserName().substring(0, 1) + '*' + obj.getUserName().substring(2));
			setText(sheet, rowIndex, cellIndex, obj.getUpperLocgovNm() + obj.getLocgovNm());
			setNumberFormat(sheet, rowIndex, cellIndex, Integer.parseInt(obj.getCntrAmt()));
			if ("300".equals(obj.getCntrPathCode())) {
				setText(sheet, rowIndex, cellIndex, obj.getUpperLocgovNm() + " " + obj.getLocgovNm() + " " + obj.getRceptBankNm());
			} else {
				setText(sheet, rowIndex, cellIndex, obj.getRceptBankCodeNm() + " " + obj.getRceptBankNm());
			}
			setText(sheet, rowIndex, cellIndex, obj.getElctrnPayNo());
			setText(sheet, rowIndex, cellIndex, obj.getCntrSn());
			setText(sheet, rowIndex, cellIndex, "100".equals(obj.getCntrSttusCode()) ? "신고" : "200".equals(obj.getCntrSttusCode()) ? "정상" : "취소");
			setText(sheet, rowIndex, cellIndex, obj.getPrjSubject());

			rowIndex++;
		}
	}


}

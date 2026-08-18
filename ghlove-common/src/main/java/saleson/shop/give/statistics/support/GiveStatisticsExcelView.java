package saleson.shop.give.statistics.support;

import java.util.List;
import java.util.Map;

import javax.mail.search.IntegerComparisonTerm;
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
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.statistics.domain.GiveStatistics;

public class GiveStatisticsExcelView extends AbstractSXSSFExcelView{

	private List<GiveStatistics> list;
	private String type;

	public GiveStatisticsExcelView() {

	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GiveStatistics>)model.get("list");
		type = String.valueOf(model.get("type"));
		
		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list, type);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GiveStatistics> list, String type) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "main";
		String title = "";
		
		switch (type) {
		case "person": title="기부 인원 통계"; break;
		case "amount": title="기부 금액 통계"; break;
		case "number": title="기부 건수 통계"; break;
		case "date": title="일자별 통계"; break;
		case "personal": title="인원별 통계"; break;
		case "locgov": title="기부금 모금 현황"; break;
		}
		
		setFileName(title.replaceAll(" ", "_") + "_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"년도"),
			new HeaderCell(3500, 	"지자체"),
			new HeaderCell(3500, 	"기부인원"),
			new HeaderCell(3500, 	"기부금액"),
			new HeaderCell(3500, 	"기부건수")
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
		for(GiveStatistics obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrYear()));
			setText(sheet, 			rowIndex, cellIndex, obj.getUpperLocgovNm() + " " + obj.getLocgovNm());
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getGivePersons()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getCntrAmt()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getGiveCnt()));

			rowIndex++;
		}
	}


}

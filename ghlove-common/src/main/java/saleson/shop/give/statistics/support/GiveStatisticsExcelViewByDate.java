package saleson.shop.give.statistics.support;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class GiveStatisticsExcelViewByDate extends AbstractSXSSFExcelView{

	private List<GiveStatistics> list;
	
	private GiveStatistics total;

	public GiveStatisticsExcelViewByDate() {

		setFileName("일자별_통계_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GiveStatistics>)model.get("list");
		total = (GiveStatistics) model.get("total");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list, total);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GiveStatistics> list, GiveStatistics total) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "main";
		String title = "일자별 통계";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(2500, 	"일자"),
			new HeaderCell(3500, 	"건수"),
			new HeaderCell(3500, 	"기부금액"),
			new HeaderCell(3500, 	"기부인원"),
			new HeaderCell(3500, 	"발생포인트")
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
		
		
		LocalDate ld = null; 
		
		
		
		for(GiveStatistics obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);
			
			ld = LocalDate.parse(obj.getCntrDe(), DateTimeFormatter.ofPattern("yyyyMMdd"));

			setText(sheet, 			rowIndex, cellIndex, ld.toString()); //No.
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getGivePersons()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getGiveAmt()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getGiveCnt()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getGivePoint()));

			rowIndex++;
		}
		
		//합계
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);

		CellIndex cellIndex = new CellIndex(-1);
		
		setText(sheet, 			rowIndex, cellIndex, "합계"); //No.
		setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(total.getGivePersons()));
		setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(total.getGiveAmt()));
		setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(total.getGiveCnt()));
		setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(total.getGivePoint()));
		
	}


}

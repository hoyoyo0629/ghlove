package saleson.shop.give.statistics.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.search.IntegerComparisonTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.statistics.domain.GiveOperate;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;

public class GiveStatisticsOperateExcelView extends AbstractSXSSFExcelView{

	private List<GiveOperate> list;
	private List<Code> codeList;
	

	public GiveStatisticsOperateExcelView() {

		setFileName("기부금_운영현황_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GiveOperate>)model.get("list");
		codeList = (List<Code>)model.get("codeList");
		

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list, codeList);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GiveOperate> list, List<Code> codeList) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "main";
		
		String title = "기부금 운영현황";
		
		
		List<HeaderCell> cellList = new ArrayList<HeaderCell>();
		cellList.add(new HeaderCell(700, "No."));
		cellList.add(new HeaderCell(3500, "지자체"));
		for (Code c : codeList) {
			cellList.add(new HeaderCell(3500, c.getLabel()));
		}
		
		cellList.add(new HeaderCell(3500, "합계"));
		
		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[cellList.size()];
		cellList.toArray(headerCells);

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
		for(GiveOperate obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, obj.getUpperLocgovNm() + " " + obj.getLocgovNm());
			
			for (Code c : codeList) {
				setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getAmt(c.getId())));
			}
			
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getExpndtrSum()));

			rowIndex++;
		}
		
	}
	
	/**
	 * 월 마지막날 가져오기
	 * @param year, month
	 * @return
	 */
	private int getLastDateOfMonth(String year, String month) {	
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
		
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		return lastDay;
	}


}

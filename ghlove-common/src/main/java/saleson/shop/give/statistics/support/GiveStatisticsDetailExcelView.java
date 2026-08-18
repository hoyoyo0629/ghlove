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

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;

public class GiveStatisticsDetailExcelView extends AbstractSXSSFExcelView{

	private List<GiveStatistics> list;
	private GiveStatisticsDetailSearch searchParam;
	private String searchDate;

	public GiveStatisticsDetailExcelView() {

	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GiveStatistics>)model.get("list");
		searchParam = (GiveStatisticsDetailSearch) model.get("searchParam");
		String type = (String) model.get("type");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list, searchParam, type);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GiveStatistics> list, GiveStatisticsDetailSearch searchParam, String type) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "give_statistics_main";
		
		String title = "";
		
		if ("person".equals(type)) {
			title = "기부 인원 통계";
			setFileName("기부_인원_통계_상세_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
		} else if ("amount".equals(type)) {
			title = "기부 금액 통계";			
			setFileName("기부_금액_통계_상세_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
		} else if ("number".equals(type)) {
			title = "기부 건수 통계";						
			setFileName("기부_건수_통계_상세_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
		}
		
		title += " (" + searchParam.getFromYear() + " ~ " + searchParam.getToYear() + ")";
		
		
		List<HeaderCell> cellList = new ArrayList<HeaderCell>();
		cellList.add(new HeaderCell(700, ""));
		for (int i = 1; i <= 31; i++) {
			cellList.add(new HeaderCell(700, i + ""));
		}
		
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
		
		//전체년도 출력 초기 세팅
		int month = 12;
		int date = 31;
		int value = 0;
		String fromMonth = "", fromDate = "";
		
		
		for (int k = Integer.parseInt(searchParam.getFromYear()); k <= Integer.parseInt(searchParam.getToYear()); k++) {
			
			for (int i = 1; i <= month; i++) {
				
				fromMonth = i < 10 ? "0" + i : i + "";
				
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);
				
				CellIndex cellIndex = new CellIndex(-1);
				
				setText(sheet, rowIndex, cellIndex, k + "년 " + fromMonth + "월");
				
				date = this.getLastDateOfMonth(k + "", fromMonth);
				
				for (int j = 1; j <= 31; j++) {
					
					if (j > date) {
						setText(sheet, rowIndex, cellIndex,"");
						continue;
					}
					
					fromDate = j < 10 ? "0" + j : j + "";
					searchDate = k + fromMonth + fromDate;
					Optional<GiveStatistics> person = list.stream().filter(s -> s.getCntrDe().equals(searchDate)).findFirst();
					
					if (person.isPresent()) {
						
						if ("person".equals(type)) {
							value = Integer.parseInt(person.get().getGivePersons());
						} else if ("amount".equals(type)) {
							value = Integer.parseInt(person.get().getGiveAmt());
						} else if ("number".equals(type)) {
							value = Integer.parseInt(person.get().getGiveCnt());
						}
						
						setNumberFormat(sheet, rowIndex, cellIndex, value);
					} else {
						setNumberFormat(sheet, rowIndex, cellIndex, 0);
					}
					
				}
				
				rowIndex++;
				
			}
		}
		
		
		/*
		 * for(GivePerson obj : list){
		 * 
		 * // 행 높이 설정 row = sheet.createRow(rowIndex); row.setHeight((short) 400);
		 * 
		 * CellIndex cellIndex = new CellIndex(-1);
		 * 
		 * setText(sheet, rowIndex, cellIndex, String.valueOf(++index)); //No.
		 * setText(sheet, rowIndex, cellIndex, String.valueOf(obj.getCntrYear()));
		 * setText(sheet, rowIndex, cellIndex, obj.getUpperLocgovNm() + " " +
		 * obj.getLocgovNm()); setNumberFormat(sheet, rowIndex, cellIndex,
		 * Integer.parseInt(obj.getGivePersons())); setNumberFormat(sheet, rowIndex,
		 * cellIndex, Integer.parseInt(obj.getCntrAmt())); setNumberFormat(sheet,
		 * rowIndex, cellIndex, Integer.parseInt(obj.getGiveCnt()));
		 * 
		 * rowIndex++; }
		 */
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

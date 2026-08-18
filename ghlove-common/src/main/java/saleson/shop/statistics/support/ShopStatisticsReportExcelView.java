package saleson.shop.statistics.support;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.hibernate.engine.transaction.jta.platform.internal.WebSphereJtaPlatform.WebSphereEnvironment;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.statistics.domain.StatisticsReport;

public class ShopStatisticsReportExcelView extends AbstractSXSSFExcelView{

	public ShopStatisticsReportExcelView() {

		setFileName("통계_보고서_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		StatisticsParam statisticsParam = (StatisticsParam)model.get("statisticsParam"); // 공용 파라미터
		List<StatisticsReport> mbrTnocsStats = (List<StatisticsReport>)model.get("mbrTnocsStats"); // 1.총괄현황_회원수
		List<StatisticsReport> dntnTnocsStats = (List<StatisticsReport>)model.get("dntnTnocsStats"); // 2.총괄현황_기부건수
//		List<StatisticsReport> dntnTnocs500Stats = (List<StatisticsReport>)model.get("dntnTnocs500Stats"); // 3-1.총괄현황_기부건수_500만원
		List<StatisticsReport> dntnTnocsMaxAmtStats = (List<StatisticsReport>)model.get("dntnTnocsMaxAmtStats"); // 3-2.총괄현황_기부한도_기부건수_2000만원
		List<StatisticsReport> gdsTnocsStats = (List<StatisticsReport>)model.get("gdsTnocsStats"); // 4.총괄현황_답례품건수
		List<StatisticsReport> mctpvDntnStats = (List<StatisticsReport>)model.get("mctpvDntnStats"); // 5.지자체별_기부현황
//		List<StatisticsReport> mctpvDntn500Stats = (List<StatisticsReport>)model.get("mctpvDntn500Stats"); // 6-1.지자체별_기부현황_500만원
		List<StatisticsReport> mctpvDntnMaxAmtStats = (List<StatisticsReport>)model.get("mctpvDntnMaxAmtStats"); // 6-2.지자체별_기부한도_기부현황_2000만원
		List<StatisticsReport> lclgvAodStats = (List<StatisticsReport>)model.get("lclgvAodStats"); // 7.지자체_기부금현황
//		List<StatisticsReport> lclgvAod500Stats = (List<StatisticsReport>)model.get("lclgvAod500Stats"); // 8-1.지자체_기부금현황_500만원
		List<StatisticsReport> lclgvAodMaxAmtStats = (List<StatisticsReport>)model.get("lclgvAodMaxAmtStats"); // 8-2.지자체_기부한도_기부금현황_2000만원
		List<StatisticsReport> mctpvGdsStats = (List<StatisticsReport>)model.get("mctpvGdsStats"); // 9.지자체별_답례품제공현황
		List<StatisticsReport> lclgvGdsStats = (List<StatisticsReport>)model.get("lclgvGdsStats"); // 10.지자체_답례품현황
		List<StatisticsReport> dntnPathTnocsStats = (List<StatisticsReport>)model.get("dntnPathTnocsStats"); // 11.기부방법별_기부건수
		List<StatisticsReport> dntnAmtTnocsStats = (List<StatisticsReport>)model.get("dntnAmtTnocsStats"); // 12.기부금액별_기부건수
		List<StatisticsReport> dntnAgeTnocsStats = (List<StatisticsReport>)model.get("dntnAgeTnocsStats"); // 13.기부연령별_기부건수
		List<StatisticsReport> habDntnMctpvTnocsStats = (List<StatisticsReport>)model.get("habDntnMctpvTnocsStats"); // 14.거주지역->기부지역별_기부건수

		// 2. 데이터 생성(시트생성)

		if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			buildItemSheetForLclgv(workbook, lclgvAodStats, statisticsParam, "1.지자체_기부금현황");
//			buildItemSheetForLclgv(workbook, lclgvAod500Stats, statisticsParam, "2.지자체_기부금현황_500만원");
			buildItemSheetForLclgv(workbook, lclgvAodMaxAmtStats, statisticsParam, "2.지자체_기부금현황_2000만원");
			buildItemSheetForLclgv(workbook, lclgvGdsStats, statisticsParam, "3.지자체_답례품현황");
		} else {
			buildItemSheetForMbr(workbook, mbrTnocsStats, statisticsParam, "1.총괄현황_회원수");
			buildItemSheetForMctpv(workbook, dntnTnocsStats, statisticsParam, "2.총괄현황_기부건수");
//			buildItemSheetForMctpv(workbook, dntnTnocs500Stats, statisticsParam, "3-1.총괄현황_기부건수(500만원)");
			buildItemSheetForMctpv(workbook, dntnTnocsMaxAmtStats, statisticsParam, "3.총괄현황_기부건수(2000만원)");
			buildItemSheetForMctpv(workbook, gdsTnocsStats, statisticsParam, "4.총괄현황_답례품건수");
			buildItemSheetForMctpvM(workbook, mctpvDntnStats, statisticsParam, "5.지자체별_기부금_현황(누계)");
//			buildItemSheetForMctpvM(workbook, mctpvDntn500Stats, statisticsParam, "6-1.지자체별_기부금_현황_500만원(누계)");
			buildItemSheetForMctpvM(workbook, mctpvDntnMaxAmtStats, statisticsParam, "6.지자체별_기부금_현황_2000만원(누계)");
			buildItemSheetForLclgv(workbook, lclgvAodStats, statisticsParam, "7.지자체_기부금현황");
//			buildItemSheetForLclgv(workbook, lclgvAod500Stats, statisticsParam, "8-1.지자체_기부금현황_500만원");
			buildItemSheetForLclgv(workbook, lclgvAodMaxAmtStats, statisticsParam, "8.지자체_기부금현황_2000만원");
			buildItemSheetForMctpvM(workbook, mctpvGdsStats, statisticsParam, "9.지자체별_답례품제공현황(누계)");
			buildItemSheetForLclgv(workbook, lclgvGdsStats, statisticsParam, "10.지자체_답례품현황");
			buildItemSheetForPath(workbook, dntnPathTnocsStats, statisticsParam, "11.기부방법별_기부건수");
			buildItemSheetForAmt(workbook, dntnAmtTnocsStats, statisticsParam, "12.기부금액별_기부건수");
			buildItemSheetForAge(workbook, dntnAgeTnocsStats, statisticsParam, "13.기부연령별_기부건수");
			buildItemSheetForHabDntn(workbook, habDntnMctpvTnocsStats, statisticsParam, "14.거주지역->기부지역별_기부건수");
		}
	}

	private void buildItemSheetForMbr(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[cellMonth+1];

		for(int i=0;i<cellMonth;i++) {
			headerCells[i] = new HeaderCell(700, 	(i+1)+"월");
		}
		headerCells[cellMonth] = new HeaderCell(700, 	"총");

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title+" (단위: 명)");


		// Table Body
		int rowIndex = 2;
		int index = 0;
		for(StatisticsReport obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntJan()))); // 1월
			if(cellMonth > 1) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntFeb()))); // 2월
			if(cellMonth > 2) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntMar()))); // 3월
			if(cellMonth > 3) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntApr()))); // 4월
			if(cellMonth > 4) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntMay()))); // 5월
			if(cellMonth > 5) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntJun()))); // 6월
			if(cellMonth > 6) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntJul()))); // 7월
			if(cellMonth > 7) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntAug()))); // 8월
			if(cellMonth > 8) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntSep()))); // 9월
			if(cellMonth > 9) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntOct()))); // 10월
			if(cellMonth > 10) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntNov()))); // 11월
			if(cellMonth > 11) setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrCntDec()))); // 12월
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMbrTnocs()))); // 총계

			rowIndex++;
		}
	}

	private void buildItemSheetForMctpv(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[cellMonth*3+3];

		headerCells[0] = new HeaderCell(700, 	"시/도");
		headerCells[1] = new HeaderCell(700, 	"올해 총계");
		headerCells[2] = new HeaderCell(700, 	"전년도");
		int j=1;
		for(int i=3;i<cellMonth*3+3;i+=3) {
			headerCells[i] = new HeaderCell(700, 	j+"월");
			headerCells[i+1] = new HeaderCell(700, 	"전년도"+j+"월");
			headerCells[i+2] = new HeaderCell(700, 	"전년대비");

			j++;
		}

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title+" (단위: 건, %)");

		// Table Body
		int rowIndex = 2;
		int index = 0;
		for(StatisticsReport obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMctpvNm()))); // 시/도
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYr()))); // 올해총계
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyr()))); // 전년도
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrJan() ))); // 1월
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrJan() ))); // 전년도1월
			if(obj.getPrvyrNowYrRtJan().equals("0")) {
				setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
			} else if (obj.getPrvyrNowYrRtJan().equals("-1")) {
				setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
			} else {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJan()))+"%")); // 전년대비
			}
			if(cellMonth > 1) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrFeb() ))); // 2월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrFeb() ))); // 전년도2월
				if(obj.getPrvyrNowYrRtFeb().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtFeb().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtFeb()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 2) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMar() ))); // 3월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMar() ))); // 전년도3월
				if(obj.getPrvyrNowYrRtMar().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtMar().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtMar()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 3) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrApr() ))); // 4월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrApr() ))); // 전년도4월
				if(obj.getPrvyrNowYrRtApr().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtApr().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtApr()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 4) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMay() ))); // 5월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMay() ))); // 전년도5월
				if(obj.getPrvyrNowYrRtMay().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtMay().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtMay()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 5) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrJun() ))); // 6월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrJun() ))); // 전년도6월
				if(obj.getPrvyrNowYrRtJun().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtJun().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJun()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 6) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrJul() ))); // 7월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrJul() ))); // 전년도7월
				if(obj.getPrvyrNowYrRtJul().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtJul().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJul()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 7) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrAug() ))); // 8월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrAug() ))); // 전년도8월
				if(obj.getPrvyrNowYrRtAug().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtAug().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtAug()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 8) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrSep() ))); // 9월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrSep() ))); // 전년도9월
				if(obj.getPrvyrNowYrRtSep().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtSep().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtSep()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 9) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrOct() ))); // 10월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrOct() ))); // 전년도10월
				if(obj.getPrvyrNowYrRtOct().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtOct().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtOct()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 10) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrNov() ))); // 11월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrNov() ))); // 전년도11월
				if(obj.getPrvyrNowYrRtNov().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtNov().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtNov()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 11) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrDec() ))); // 12월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrDec() ))); // 전년도12월
				if(obj.getPrvyrNowYrRtDec().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtDec().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtDec()))+"%")); // 전년대비
				}
			}

			rowIndex++;
		}
	}

	private void buildItemSheetForMctpvM(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[cellMonth*3+1];

		headerCells[0] = new HeaderCell(700, 	"시/도");
		int j=1;
		for(int i=1;i<cellMonth*3+1;i+=3) {
			headerCells[i] = new HeaderCell(700, 	j+"월");
			headerCells[i+1] = new HeaderCell(700, 	"전년도"+j+"월");
			headerCells[i+2] = new HeaderCell(700, 	"전년대비");

			j++;
		}

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title+" (단위: 백만원, %)");

		// Table Body
		int rowIndex = 2;
		int index = 0;
		for(StatisticsReport obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMctpvNm()))); // 시/도
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMJan() ))); // 1월
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMJan() ))); // 전년도1월
			if(obj.getPrvyrNowYrRtJan().equals("0")) {
				setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
			} else if (obj.getPrvyrNowYrRtJan().equals("-1")) {
				setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
			} else {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJan()))+"%")); // 전년대비
			}
			if(cellMonth > 1) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMFeb() ))); // 2월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMFeb() ))); // 전년도2월
				if(obj.getPrvyrNowYrRtFeb().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtFeb().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtFeb()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 2) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMMar() ))); // 3월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMMar() ))); // 전년도3월
				if(obj.getPrvyrNowYrRtMar().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtMar().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtMar()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 3) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMApr() ))); // 4월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMApr() ))); // 전년도4월
				if(obj.getPrvyrNowYrRtApr().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtApr().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtApr()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 4) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMMay() ))); // 5월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMMay() ))); // 전년도5월
				if(obj.getPrvyrNowYrRtMay().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtMay().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtMay()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 5) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMJun() ))); // 6월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMJun() ))); // 전년도6월
				if(obj.getPrvyrNowYrRtJun().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtJun().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJun()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 6) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMJul() ))); // 7월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMJul() ))); // 전년도7월
				if(obj.getPrvyrNowYrRtJul().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtJul().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJul()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 7) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMAug() ))); // 8월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMAug() ))); // 전년도8월
				if(obj.getPrvyrNowYrRtAug().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtAug().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtAug()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 8) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMSep() ))); // 9월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMSep() ))); // 전년도9월
				if(obj.getPrvyrNowYrRtSep().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtSep().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtSep()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 9) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMOct() ))); // 10월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMOct() ))); // 전년도10월
				if(obj.getPrvyrNowYrRtOct().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtOct().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtOct()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 10) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMNov() ))); // 11월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMNov() ))); // 전년도11월
				if(obj.getPrvyrNowYrRtNov().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtNov().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtNov()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 11) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrMDec() ))); // 12월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrMDec() ))); // 전년도12월
				if(obj.getPrvyrNowYrRtDec().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtDec().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtDec()))+"%")); // 전년대비
				}
			}

			rowIndex++;
		}
	}

	private void buildItemSheetForLclgv(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[cellMonth*3+4];

		headerCells[0] = new HeaderCell(700, 	"시/도");
		headerCells[1] = new HeaderCell(700, 	"지자체");
		headerCells[2] = new HeaderCell(700, 	"올해 총계");
		headerCells[3] = new HeaderCell(700, 	"전년도");
		int j=1;
		for(int i=4;i<cellMonth*3+4;i+=3) {
			headerCells[i] = new HeaderCell(700, 	j+"월");
			headerCells[i+1] = new HeaderCell(700, 	"전년도"+j+"월");
			headerCells[i+2] = new HeaderCell(700, 	"전년대비");

			j++;
		}

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title+" (단위: 천원, %)");


		// Table Body
		int rowIndex = 2;
		int index = 0;
		for(StatisticsReport obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getMctpvNm()))); // 시/도
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getLclgvNm()))); // 지자체
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrK()))); // 올해총계
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrK()))); // 전년도
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKJan() ))); // 1월
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKJan() ))); // 전년도1월
			if(obj.getPrvyrNowYrRtJan().equals("0")) {
				setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
			} else if (obj.getPrvyrNowYrRtJan().equals("-1")) {
				setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
			} else {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJan()))+"%")); // 전년대비
			}
			if(cellMonth > 1) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKFeb() ))); // 2월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKFeb() ))); // 전년도2월
				if(obj.getPrvyrNowYrRtFeb().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtFeb().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtFeb()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 2) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKMar() ))); // 3월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKMar() ))); // 전년도3월
				if(obj.getPrvyrNowYrRtMar().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtMar().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtMar()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 3) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKApr() ))); // 4월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKApr() ))); // 전년도4월
				if(obj.getPrvyrNowYrRtApr().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtApr().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtApr()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 4) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKMay() ))); // 5월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKMay() ))); // 전년도5월
				if(obj.getPrvyrNowYrRtMay().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtMay().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtMay()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 5) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKJun() ))); // 6월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKJun() ))); // 전년도6월
				if(obj.getPrvyrNowYrRtJun().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtJun().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJun()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 6) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKJul() ))); // 7월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKJul() ))); // 전년도7월
				if(obj.getPrvyrNowYrRtJul().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtJul().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtJul()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 7) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKAug() ))); // 8월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKAug() ))); // 전년도8월
				if(obj.getPrvyrNowYrRtAug().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtAug().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtAug()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 8) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKSep() ))); // 9월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKSep() ))); // 전년도9월
				if(obj.getPrvyrNowYrRtSep().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtSep().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtSep()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 9) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKOct() ))); // 10월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKOct() ))); // 전년도10월
				if(obj.getPrvyrNowYrRtOct().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtOct().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtOct()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 10) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKNov() ))); // 11월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKNov() ))); // 전년도11월
				if(obj.getPrvyrNowYrRtNov().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtNov().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtNov()))+"%")); // 전년대비
				}
			}
			if(cellMonth > 11) {
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getNowYrKDec() ))); // 12월
				setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrvyrKDec() ))); // 전년도12월
				if(obj.getPrvyrNowYrRtDec().equals("0")) {
					setText(sheet,rowIndex, cellIndex, "0"); // 전년대비
				} else if (obj.getPrvyrNowYrRtDec().equals("-1")) {
					setText(sheet,rowIndex, cellIndex, "-"); // 전년대비
				} else {
					setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.format("%.2f", 100 * Float.parseFloat(obj.getPrvyrNowYrRtDec()))+"%")); // 전년대비
				}
			}

			rowIndex++;
		}
	}

	private void buildItemSheetForPath(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Font titleFont = workbook.createFont();
		titleFont.setFontName("맑은 고딕");
		titleFont.setFontHeightInPoints((short) 14);
		titleFont.setBold(true);

		//셀병합 타이틀
		CellStyle mergedTitle = workbook.createCellStyle();
		mergedTitle.setVerticalAlignment(VerticalAlignment.CENTER);
		mergedTitle.setFont(titleFont);

		Sheet sheet = workbook.createSheet(title);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
		sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));

		Row row = sheet.createRow((short) 0);
		row.setHeight((short) 800);
		Cell titleCell = row.createCell(0);
		titleCell.setCellStyle(mergedTitle);
		titleCell.setCellValue(title);

		Row row1 = sheet.createRow((short) 1);
		row1.setHeight((short) 600);
		Font subFont = workbook.createFont();
		subFont.setFontName("맑은 고딕");
		subFont.setFontHeightInPoints((short) 10);
		subFont.setBold(true);
		subFont.setColor(IndexedColors.BLACK.getIndex());
		XSSFCellStyle xssfStyle = (XSSFCellStyle)workbook.createCellStyle();
		xssfStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 153)));
		xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		xssfStyle.setAlignment(HorizontalAlignment.CENTER);
		xssfStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		xssfStyle.setBorderBottom(BorderStyle.THIN);
		xssfStyle.setBorderTop(BorderStyle.THIN);
		xssfStyle.setBorderRight(BorderStyle.THIN);
		xssfStyle.setBorderLeft(BorderStyle.THIN);
		xssfStyle.setFont(subFont);

		row1.createCell(0).setCellStyle(xssfStyle);
		row1.createCell(1).setCellStyle(xssfStyle);
		row1.createCell(2).setCellStyle(xssfStyle);
		row1.createCell(3).setCellStyle(xssfStyle);
		row1.createCell(4).setCellStyle(xssfStyle);
		row1.createCell(5).setCellStyle(xssfStyle);

		row1.getCell(0).setCellValue("구분");
		row1.getCell(2).setCellValue("기부금액");
		row1.getCell(3).setCellValue("기부건수");
		row1.getCell(4).setCellValue("기부금액(%)");
		row1.getCell(5).setCellValue("기부건수(%)");

		// Table Body
		int rowIndex = 2;
		Row rowContent = null;
		for(StatisticsReport obj : list) {

			XSSFCellStyle xssfStyleTmp = (XSSFCellStyle)workbook.createCellStyle();
			xssfStyleTmp.setAlignment(HorizontalAlignment.CENTER);
			xssfStyleTmp.setVerticalAlignment(VerticalAlignment.CENTER);
			xssfStyleTmp.setBorderBottom(BorderStyle.THIN);
			xssfStyleTmp.setBorderTop(BorderStyle.THIN);
			xssfStyleTmp.setBorderRight(BorderStyle.THIN);
			xssfStyleTmp.setBorderLeft(BorderStyle.THIN);

			Font subFontTmp = workbook.createFont();
			subFontTmp.setFontName("맑은 고딕");
			subFontTmp.setColor(IndexedColors.BLACK.getIndex());
			subFontTmp.setFontHeightInPoints((short) 10);
			subFontTmp.setBold(false);
			xssfStyleTmp.setFont(subFontTmp);

			rowContent = sheet.createRow(rowIndex);
			rowContent.setHeight((short) 400);
			rowContent.createCell(0).setCellStyle(xssfStyleTmp);
			rowContent.createCell(1).setCellStyle(xssfStyleTmp);
			rowContent.createCell(2).setCellStyle(xssfStyleTmp);
			rowContent.createCell(3).setCellStyle(xssfStyleTmp);
			rowContent.createCell(4).setCellStyle(xssfStyleTmp);
			rowContent.createCell(5).setCellStyle(xssfStyleTmp);

			if(rowIndex >= 3 && rowIndex <= (list.size()-1)) {
				if(ValidationUtils.isNull(obj.getDntnPath())) {
					rowContent.getCell(1).setCellValue("");
				}else {
					rowContent.getCell(1).setCellValue(obj.getDntnPath());
				}
				rowContent.getCell(0).setCellValue("민간플랫폼");

			}else {
				sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,1));
				if(ValidationUtils.isNull(obj.getDntnPath())) {
					rowContent.getCell(0).setCellValue("");
				}else {
					rowContent.getCell(0).setCellValue(obj.getDntnPath());
				}
			}

			rowContent.getCell(2).setCellValue(StringUtils.numberFormat(String.valueOf(obj.getGramt())));
			rowContent.getCell(3).setCellValue(StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			rowContent.getCell(4).setCellValue(obj.getGramtrt()+"%");
			rowContent.getCell(5).setCellValue(obj.getRt()+"%");

			rowIndex++;
		}
		if(list.size() > 3) {
			sheet.addMergedRegion(new CellRangeAddress(3,(list.size()-1),0,0));
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}
	}

	private void buildItemSheetForAmt(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[] {
		  new HeaderCell(700, 	"구분")
		  ,new HeaderCell(700, 	"합계")
		  ,new HeaderCell(3000, 	"10만원 미만")
		  ,new HeaderCell(3000, 	"10만원")
		  ,new HeaderCell(3000, 	"10만원~100만원 미만")
		  ,new HeaderCell(3000, 	"100만원~500만원 미만")
		  ,new HeaderCell(3000, 	"500만원")
		  ,new HeaderCell(3000, 	"500~2000만원 미만")
		  ,new HeaderCell(3000, 	"2000만원")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.생성
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title);

		// Table Body
		int rowIndex = 2;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		CellIndex cellIndex1 = new CellIndex(-1);
		for(StatisticsReport obj : list){
			if(obj.getDntnAmtCd().equals("00")) {
				setText(sheet,rowIndex, cellIndex1, "건수");
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAmtCd().equals("10")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAmtCd().equals("20")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAmtCd().equals("30")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAmtCd().equals("40")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAmtCd().equals("50")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAmtCd().equals("60")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			}
		}
		if(list.size() < 8) {
			setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf("-")));
			setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf("-")));
		}
		rowIndex = 3;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		CellIndex cellIndex2 = new CellIndex(-1);
		for(StatisticsReport obj : list){
			if(obj.getDntnAmtCd().equals("00")) {
				setText(sheet,rowIndex, cellIndex2, "비율");
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAmtCd().equals("10")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAmtCd().equals("20")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAmtCd().equals("30")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAmtCd().equals("40")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAmtCd().equals("50")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAmtCd().equals("60")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			}
		}
		if(list.size() < 8) {
			setText(sheet,rowIndex, cellIndex2, StringUtils.numberFormat(String.valueOf("-")));
			setText(sheet,rowIndex, cellIndex2, StringUtils.numberFormat(String.valueOf("-")));
		}
	}

	private void buildItemSheetForAge(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[] {
				  new HeaderCell(700, 	"구분")
				  ,new HeaderCell(700, 	"합계")
				  ,new HeaderCell(700, 	"10대")
				  ,new HeaderCell(700, 	"20대")
				  ,new HeaderCell(700, 	"30대")
				  ,new HeaderCell(700, 	"40대")
				  ,new HeaderCell(700, 	"50대")
				  ,new HeaderCell(700, 	"60대")
				  ,new HeaderCell(700, 	"70대")
				  ,new HeaderCell(700, 	"80대 이상")
				  ,new HeaderCell(700, 	"생년월일 없음")
				};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title);

		int rowIndex = 2;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		CellIndex cellIndex1 = new CellIndex(-1);
		for(StatisticsReport obj : list){
			if(obj.getDntnAgeCd().equals("00")) {
				setText(sheet,rowIndex, cellIndex1, "건수");
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("10")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("20")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("30")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("40")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("50")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("60")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("70")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else if(obj.getDntnAgeCd().equals("80")) {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			} else {
				setText(sheet,rowIndex, cellIndex1, StringUtils.numberFormat(String.valueOf(obj.getTnocs())));
			}
		}
		rowIndex = 3;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);
		CellIndex cellIndex2 = new CellIndex(-1);
		for(StatisticsReport obj : list){
			if(obj.getDntnAgeCd().equals("00")) {
				setText(sheet,rowIndex, cellIndex2, "비율");
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("10")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("20")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("30")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("40")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("50")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("60")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("70")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else if(obj.getDntnAgeCd().equals("80")) {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			} else {
				setText(sheet,rowIndex, cellIndex2, obj.getRt()+"%");
			}
		}
	}

	private void buildItemSheetForHabDntn(SXSSFWorkbook workbook, List<StatisticsReport> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		int cellMonth = statisticsParam.getShMonth();

		HeaderCell[] headerCells = new HeaderCell[] {
			new HeaderCell(1100, "거주지역>기부지역")
			,new HeaderCell(1100, "서울특별시")
			,new HeaderCell(1100, "부산광역시")
			,new HeaderCell(1100, "대구광역시")
			,new HeaderCell(1100, "인천광역시")
			,new HeaderCell(1100, "광주광역시")
			,new HeaderCell(1100, "대전광역시")
			,new HeaderCell(1100, "울산광역시")
			,new HeaderCell(1100, "세종특별자치시")
			,new HeaderCell(1100, "경기도")
			,new HeaderCell(1100, "충청북도")
			,new HeaderCell(1100, "충청남도")
			,new HeaderCell(1100, "전라남도")
			,new HeaderCell(1100, "경상북도")
			,new HeaderCell(1100, "경상남도")
			,new HeaderCell(1100, "제주특별자치도")
			,new HeaderCell(1100, "강원특별자치도")
			,new HeaderCell(1100, "전북특별자치도")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, title);

		// Table Body
		int rowIndex = 2;
		int index = 0;
		for(StatisticsReport obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getHabMctpvNm ())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntSeoul())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntBusan())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntDaegu())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntIncheon())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntGwangju())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntDaejeon())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntUlsan())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntSejong())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntGyeonggi())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntChungbuk())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntChungnam())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntJeonnam())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntGyeongbuk())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntGyeongnam())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntJeju())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntGangwon())));
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getDntnCntJeonbuk())));

			rowIndex++;
		}
	}
}

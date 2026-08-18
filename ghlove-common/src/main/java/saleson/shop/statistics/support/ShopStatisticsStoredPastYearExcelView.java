package saleson.shop.statistics.support;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.statistics.domain.StatisticsReport;

public class ShopStatisticsStoredPastYearExcelView extends AbstractSXSSFExcelView{


	private XSSFCellStyle xssfStyle;
	private XSSFCellStyle xssfStyleTmp;
	private Font subFont;
	private CellStyle mergedTitle;
	private Font titleFont;
	private Font subFontTmp;

	public ShopStatisticsStoredPastYearExcelView() {
		setFileName("연통계_보고서_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}

	@Override
	public void buildExcelDocument(Map<String, Object> model, SXSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setStyleBase(workbook);

		StatisticsParam statisticsParam = (StatisticsParam)model.get("statisticsParam"); // 공용 파라미터
		List<HashMap<String, Object>> mbrTnocsStatsStored = (List<HashMap<String, Object>>)model.get("mbrTnocsStatsStored"); // 총괄현황
		List<HashMap<String, Object>> dntnPathPastYearStats = (List<HashMap<String, Object>>)model.get("dntnPathPastYearStats"); // 기부 방법별
		List<HashMap<String, Object>> dntnPricePastYearStats = (List<HashMap<String, Object>>)model.get("dntnPricePastYearStats"); // 기부 금액별
		List<HashMap<String, Object>> dntnAgesPastYearStats = (List<HashMap<String, Object>>)model.get("dntnAgesPastYearStats"); // 기부 연령별
		List<HashMap<String, Object>> dntnPsintPastYearStats = (List<HashMap<String, Object>>)model.get("dntnPsintPastYearStats"); // 기부 기부자 주소지 광역시
		List<HashMap<String, Object>> dntnLocGovPastYearStats = (List<HashMap<String, Object>>)model.get("dntnLocGovPastYearStats"); // 기부 기부자 주소지 지역별

		List<HashMap<String, Object>> dntnMonthPastYearStats = (List<HashMap<String, Object>>)model.get("dntnMonthPastYearStats"); // 기부 월별

		List<HashMap<String, Object>> dntnGoodsPastYearStats = (List<HashMap<String, Object>>)model.get("dntnGoodsPastYearStats"); // 답례품 인기 답례품 현황



		buildItemSheetForMbrPastYearDntn(workbook, mbrTnocsStatsStored, statisticsParam, "기부현황");
		buildItemSheetForPathPastYearDntn(workbook, dntnPathPastYearStats, statisticsParam, "기부현황");
		buildItemSheetForPricePastYearDntn(workbook, dntnPricePastYearStats, statisticsParam, "기부현황");
		buildItemSheetForAgesPastYearDntn(workbook, dntnAgesPastYearStats, statisticsParam, "기부현황");
		buildItemSheetForPsintPastYearDntn(workbook, dntnPsintPastYearStats, statisticsParam, "기부현황");
		buildItemSheetForLocGovPastYearDntn(workbook, dntnLocGovPastYearStats, statisticsParam, "기부현황");
		buildItemSheetForMonthPastYearDntn(workbook, dntnMonthPastYearStats, statisticsParam, "기부현황");
		buildItemSheetForGoodsPastYearDntn(workbook, dntnGoodsPastYearStats, statisticsParam, "답례품현황");
	}

	private void buildItemSheetForMbrPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[2];

		headerCells[0] = new HeaderCell(700, 	"총괄");
		headerCells[1] = new HeaderCell(700, 	"");

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "1. "+title);

		sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));

		// Table Body
		int rowIndex = 2;
		int index = 0;

		// 행 높이 설정
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);

		CellIndex cellIndexC = new CellIndex(-1);
		if (list.size() >= 1) {
			setText(sheet,rowIndex, cellIndexC, "건수");
			setTextRight(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(list.get(0).get("SUM_TNOCS"))));
		}

		rowIndex++;
		row = sheet.createRow(rowIndex);
		row.setHeight((short) 400);

		CellIndex cellIndexA = new CellIndex(-1);
		if (list.size() >= 1) {
			setText(sheet,rowIndex, cellIndexA, "금액");
			setTextRight(sheet,rowIndex, cellIndexA, StringUtils.numberFormat(String.valueOf(list.get(0).get("SUM_AMT"))));
		}

	}


	private void buildItemSheetForPathPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		Sheet sheetPath = workbook.createSheet(title+"_기부방법별");
		sheetPath.addMergedRegion(new CellRangeAddress(0,0,0,5));
		sheetPath.addMergedRegion(new CellRangeAddress(1,1,0,1));

		Row rowPath = sheetPath.createRow((short) 0);//2. 기부방법별
		rowPath.setHeight((short) 800);
		Cell titleCell = rowPath.createCell(0);
		titleCell.setCellStyle(mergedTitle);
		titleCell.setCellValue("2. 기부방법별");

		Row rowPath1 = sheetPath.createRow((short) 1);
		rowPath1.setHeight((short) 500);
		rowPath1.createCell(0).setCellStyle(xssfStyle);
		rowPath1.createCell(1).setCellStyle(xssfStyle);
		rowPath1.createCell(2).setCellStyle(xssfStyle);
		rowPath1.createCell(3).setCellStyle(xssfStyle);
		rowPath1.createCell(4).setCellStyle(xssfStyle);
		rowPath1.createCell(5).setCellStyle(xssfStyle);

		rowPath1.getCell(0).setCellValue("구분");
		rowPath1.getCell(2).setCellValue("기부금액");
		rowPath1.getCell(3).setCellValue("기부건수");
		rowPath1.getCell(4).setCellValue("기부금액(%)");
		rowPath1.getCell(5).setCellValue("기부건수(%)");

		int rowIndex = 2;
		Row rowContent = null;
		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list) {

	//			XSSFCellStyle xssfStyleTmp = (XSSFCellStyle)workbook.createCellStyle();
	//			Font subFontTmp = workbook.createFont();

				rowContent = sheetPath.createRow(rowIndex);
				rowContent.setHeight((short) 400);
				rowContent.createCell(0).setCellStyle(xssfStyleTmp);
				rowContent.createCell(1).setCellStyle(xssfStyleTmp);
				rowContent.createCell(2).setCellStyle(xssfStyleTmp);
				rowContent.createCell(3).setCellStyle(xssfStyleTmp);
				rowContent.createCell(4).setCellStyle(xssfStyleTmp);
				rowContent.createCell(5).setCellStyle(xssfStyleTmp);

				if(rowIndex >= 3 && rowIndex <= (list.size()-1)) {
					if(ValidationUtils.isNull(obj.get("DNTN_PATH"))) {
						rowContent.getCell(1).setCellValue("");
					}else {
						rowContent.getCell(1).setCellValue(String.valueOf(obj.get("DNTN_PATH")));
					}
					rowContent.getCell(0).setCellValue("민간플랫폼");

				}else {
					sheetPath.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,1));
					if(ValidationUtils.isNull(obj.get("DNTN_PATH"))) {
						rowContent.getCell(0).setCellValue("");
					}else {
						rowContent.getCell(0).setCellValue(String.valueOf(obj.get("DNTN_PATH")));
					}
				}

				rowContent.getCell(2).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("GRAMT"))));
				rowContent.getCell(3).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
				rowContent.getCell(4).setCellValue(obj.get("GRAMTRT")+"%");
				rowContent.getCell(5).setCellValue(obj.get("RT")+"%");

				rowIndex++;
			}
		}
		if(list.size() > 4) {
			sheetPath.addMergedRegion(new CellRangeAddress(3,(list.size()-1),0,0));
		}

		((SXSSFSheet) sheetPath).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheetPath.autoSizeColumn(i);
			sheetPath.setColumnWidth(i, sheetPath.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetForPricePastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[] {

			new HeaderCell(1600, 	"구분")
			,new HeaderCell(900, 	"건수")
			,new HeaderCell(900, 	"금액")
			,new HeaderCell(900, 	"(백만원환산)")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title+"_기부금액별");
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "3. 기부금액별");

		// Table Body
		int rowIndex = 2;
		int index = 0;

		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list){
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndexC = new CellIndex(-1);

				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("GRAMT")));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("ATM"))));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("ATMHUND"))));

				rowIndex++;

			}
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetForAgesPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[] {

				new HeaderCell(1300, 	"구분")
				,new HeaderCell(900, 	"건수")
				,new HeaderCell(900, 	"비율")
				,new HeaderCell(900, 	"금액")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title+"_기부연령별");
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "4. 기부연령별");

		// Table Body
		int rowIndex = 2;
		int index = 0;

		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list){
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndexC = new CellIndex(-1);

				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("AGES")));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("RT"))+"%"));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("ASUM"))));

				rowIndex++;

			}
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetForPsintPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[] {

				new HeaderCell(1300, 	"구분")
				,new HeaderCell(900, 	"건수")
				,new HeaderCell(900, 	"비율")
				,new HeaderCell(900, 	"금액")
				,new HeaderCell(900, 	"(백만원환산)")
				,new HeaderCell(900, 	"비중")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title+"_기부자주소지_광역");
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "5. 기부자주소지별_광역");

		// Table Body
		int rowIndex = 2;
		int index = 0;

		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list){
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndexC = new CellIndex(-1);

				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("GRAMT")));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("TNOCSRT"))+"%");
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("ASUM"))));
				setText(sheet,rowIndex, cellIndexC, exchangHundFlot(obj.get("ASUM")));
				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("ASUMRT"))+"%");

				rowIndex++;

			}
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}

	}
	private void buildItemSheetForLocGovPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[] {

				new HeaderCell(1300, 	"구분")
				,new HeaderCell(900, 	"건수")
				,new HeaderCell(900, 	"비율")
				,new HeaderCell(900, 	"금액")
				,new HeaderCell(900, 	"(백만원환산)")
				,new HeaderCell(900, 	"비중")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title+"_기부자주소지_전체");
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "6. 기부자주소지별_전체");

		// Table Body
		int rowIndex = 2;
		int index = 0;

		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list){
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndexC = new CellIndex(-1);

				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("GRAMT")));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("TNOCSRT"))+"%");
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("ASUM"))));
				setText(sheet,rowIndex, cellIndexC, exchangHundFlot(obj.get("ASUM")));
				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("ASUMRT"))+"%");

				rowIndex++;

			}
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}

	}


	private void buildItemSheetForMonthPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[] {

				new HeaderCell(900, 	"구분")
				,new HeaderCell(900, 	"건수")
				,new HeaderCell(900, 	"비중")
				,new HeaderCell(900, 	"금액")
				,new HeaderCell(900, 	"비중")
				,new HeaderCell(900, 	"고유아이디")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title+"_기부월별");
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "7. 기부월별");

		// Table Body
		int rowIndex = 2;
		int index = 0;

		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list){
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndexC = new CellIndex(-1);

				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("CATE_MONTH")));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("TNOCS_RT"))+"%");
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("ASUM"))));
				setText(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("ASUM_RT"))+"%");
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("UNIQ_ID"))));

				rowIndex++;

			}
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}

	}
	private void buildItemSheetForGoodsPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		// 1. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[] {

				new HeaderCell(900, 	"행레이블")
				,new HeaderCell(900, 	"판매가")
				,new HeaderCell(900, 	"합계:판매가")
				,new HeaderCell(900, 	"개수:수량")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheet = workbook.createSheet(title);
		Row row = sheet.createRow((short) 0);

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		createSheetHeader(sheet, row, headerCells, "1. 답례품 인기순");

		// Table Body
		int rowIndex = 2;
		int index = 0;

		if (list.size() >= 1) {
			for(HashMap<String, Object> obj : list){
				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndexC = new CellIndex(-1);

				setTextLeft(sheet,rowIndex, cellIndexC, String.valueOf(obj.get("ITEM_NAME")));

				if("총합계".equals(String.valueOf(obj.get("ITEM_NAME")))) setText(sheet,rowIndex, cellIndexC, "");
				else setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("PRICE"))));

				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("SUM_PRICE"))));
				setText(sheet,rowIndex, cellIndexC, StringUtils.numberFormat(String.valueOf(obj.get("SUM_QUANTITY"))));

				rowIndex++;

			}
		}
		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 200);
		}

	}








	/**
	 * //내부 유틸용 입니다. 백만원단위변환 및 소수점처리기능
	 * @param oVal
	 * @return
	 */
	@SuppressWarnings("unused")
	private String exchangHundFlot (Object oVal) {
		DecimalFormat formatter = new DecimalFormat("###,###");// 포인트 세자리 콤마 처리
		BigDecimal bd = new BigDecimal(Double.parseDouble(String.valueOf(oVal)));//데시말변환
		String bdSrt = bd.setScale(-6, RoundingMode.HALF_UP).toPlainString();//백만원단위변환 반올림

		//백만 아래 제거 준비
		int strLength = bdSrt.length();
		String tmpExHund = bdSrt; //임시저장 백만원 환산

		if (strLength > 6) {//백만이상 정수처리
			tmpExHund = bdSrt.substring(0, strLength-6);//백만이상일경우 정수처리->백만 아래 제거
			tmpExHund = formatter.format(Double.parseDouble(tmpExHund));
		} else { //백만이하일경우 소수점처리
			tmpExHund = String.format("%.0f",Integer.parseInt(tmpExHund)*0.000001);//소수점 3자리
		}
		return tmpExHund;
	}

	@SuppressWarnings("unused")
	private void setStyleBase(SXSSFWorkbook workbook) {
		titleFont = workbook.createFont();
		titleFont.setFontName("맑은 고딕");
		titleFont.setFontHeightInPoints((short) 14);
		titleFont.setBold(true);
		//셀병합 타이틀
		mergedTitle = workbook.createCellStyle();
		mergedTitle.setVerticalAlignment(VerticalAlignment.CENTER);
		mergedTitle.setFont(titleFont);

		subFont = workbook.createFont();
		subFont.setFontName("맑은 고딕");
		subFont.setFontHeightInPoints((short) 9);
		subFont.setBold(true);
		subFont.setColor(IndexedColors.BLACK.getIndex());
		xssfStyle = (XSSFCellStyle)workbook.createCellStyle();
		xssfStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 153)));
		xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		xssfStyle.setAlignment(HorizontalAlignment.CENTER);
		xssfStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		xssfStyle.setBorderBottom(BorderStyle.THIN);
		xssfStyle.setBorderTop(BorderStyle.THIN);
		xssfStyle.setBorderRight(BorderStyle.THIN);
		xssfStyle.setBorderLeft(BorderStyle.THIN);
		xssfStyle.setFont(subFont);

		subFontTmp = workbook.createFont();
		subFontTmp.setFontName("맑은 고딕");
		subFontTmp.setColor(IndexedColors.BLACK.getIndex());
		subFontTmp.setFontHeightInPoints((short) 9);
		subFontTmp.setBold(false);

		xssfStyleTmp = (XSSFCellStyle)workbook.createCellStyle();
		xssfStyleTmp.setAlignment(HorizontalAlignment.CENTER);
		xssfStyleTmp.setVerticalAlignment(VerticalAlignment.CENTER);
		xssfStyleTmp.setBorderBottom(BorderStyle.THIN);
		xssfStyleTmp.setBorderTop(BorderStyle.THIN);
		xssfStyleTmp.setBorderRight(BorderStyle.THIN);
		xssfStyleTmp.setBorderLeft(BorderStyle.THIN);
		xssfStyleTmp.setFont(subFontTmp);
	}

}

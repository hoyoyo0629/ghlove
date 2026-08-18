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

public class ShopStatisticsPastYearExcelView extends AbstractSXSSFExcelView{
	private Font titleFont;
	private CellStyle mergedTitle;
	private Sheet sheet;
	private Row row;//큰제목
	private Row row1;//소
	private Font subFont;
	private XSSFCellStyle xssfStyle;
	private XSSFCellStyle xssfStyleTmp;

	private Font subFontTmp;
	private Row rowCntrCnt;

	Sheet sheetAddrDntn;//기부현황-주소지별건수금액 시트
	Sheet sheetMonthDntn;//기부현황-월별건수금액 시트

	public ShopStatisticsPastYearExcelView() {
		setFileName("연통계_보고서_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}

	@Override
	public void buildExcelDocument(Map<String, Object> model, SXSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		StatisticsParam statisticsParam = (StatisticsParam)model.get("statisticsParam"); // 공용 파라미터
		List<HashMap<String, Object>> dntnTnocsPastYearStats = (List<HashMap<String, Object>>)model.get("dntnTnocsPastYearStats"); // 1.총괄현황_기부 건수(같은시트)
		List<HashMap<String, Object>> dntnMctpvPastYearStats = (List<HashMap<String, Object>>)model.get("dntnMctpvPastYearStats"); // 1.총괄현황_기부 금액(같은시트)
		List<HashMap<String, Object>> dntnDntnPathPastYearStats = (List<HashMap<String, Object>>)model.get("dntnDntnPathPastYearStats"); // 2.기부방법별 건수,금액
		List<HashMap<String, Object>> dntnDntnAmtPastYearStats = (List<HashMap<String, Object>>)model.get("dntnDntnAmtPastYearStats"); // 3.금액별 건수
		List<HashMap<String, Object>> dntnAgeDntnAmtPastYearStats = (List<HashMap<String, Object>>)model.get("dntnAgeDntnAmtPastYearStats"); // 4.연령별 건수

		List<HashMap<String, Object>> dntnMonthMctpvUniqIdPastYearStats = (List<HashMap<String, Object>>)model.get("dntnMonthMctpvUniqIdPastYearStats"); // 6. 쿼리 새로만듬  -> 금액 고유아이디 데이터
		List<HashMap<String, Object>> dntnPubGoodsPastYearStats = (List<HashMap<String, Object>>)model.get("dntnPubGoodsPastYearStats"); // 7. 쿼리 새로만듬  -> 인기답례품 판매량 상위 30개

		buildItemSheetForPastYearDntn(workbook, dntnTnocsPastYearStats, statisticsParam, "기부현황-총괄");//1.총괄 건수 같은시트임 1-1번
		buildItemSheetForPastYearMctpv(workbook, dntnMctpvPastYearStats, statisticsParam, "기부현황-총괄");//1.총괄 금액 같은시트임 1-2번
		buildItemSheetForPastYearPath(workbook, dntnDntnPathPastYearStats, statisticsParam, "기부현황-기부방법별");//2.
		buildItemSheetForPastYearAmt(workbook, dntnDntnAmtPastYearStats, statisticsParam, "기부현황-금액별건수");//3.
		buildItemSheetForPastYearAgeAmtDntn(workbook, dntnAgeDntnAmtPastYearStats, statisticsParam, "기부현황-연령별건수금액");//4.
		buildItemSheetAddrPastYearDntn(workbook, dntnTnocsPastYearStats, statisticsParam, "기부현황-주소지별건수금액"); //5. 1-1번과 같은 쿼리
		buildItemSheetAddrPastYearMctpv(workbook, dntnMctpvPastYearStats, statisticsParam, "기부현황-주소지별건수금액"); //5. 1-2번과 같은 쿼리

		buildItemSheetMonthPastYearDntn(workbook, dntnTnocsPastYearStats, statisticsParam, "기부현황-월별건수금액"); 				//6. 1-1번과 같은 쿼리 -> 건수 데이터
		buildItemSheetMonthPastYearMctpv(workbook, dntnMonthMctpvUniqIdPastYearStats, statisticsParam, "기부현황-월별건수금액"); 	//6. 쿼리 새로만듬  -> 금액 고유아이디 데이터

		buildItemSheetPubGoodsPastYearMctpv(workbook, dntnPubGoodsPastYearStats, statisticsParam, "답례품현황-인기답례품현황"); 	//7. 쿼리 새로만듬  -> 인기답례품현황
	}

	private void buildItemSheetForPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		titleFont = workbook.createFont();
		titleFont.setFontName("맑은 고딕");
		titleFont.setFontHeightInPoints((short) 14);
		titleFont.setBold(true);

		//셀병합 타이틀
		mergedTitle = workbook.createCellStyle();
		mergedTitle.setVerticalAlignment(VerticalAlignment.CENTER);
		mergedTitle.setFont(titleFont);

		sheet = workbook.createSheet(title);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
		sheet.addMergedRegion(new CellRangeAddress(1,1,0,1));

		row = sheet.createRow((short) 0);
		row.setHeight((short) 800);
		Cell titleCell = row.createCell(0);
		titleCell.setCellStyle(mergedTitle);
		titleCell.setCellValue(title);

		row1 = sheet.createRow((short) 1);//1. 총괄
		row1.setHeight((short) 600);
		subFont = workbook.createFont();
		subFont.setFontName("맑은 고딕");
		subFont.setFontHeightInPoints((short) 10);
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

		row1.createCell(0).setCellStyle(xssfStyle);
		row1.createCell(1).setCellStyle(xssfStyle);
		row1.getCell(0).setCellValue("총괄");

		xssfStyleTmp = (XSSFCellStyle)workbook.createCellStyle();
		xssfStyleTmp.setAlignment(HorizontalAlignment.CENTER);
		xssfStyleTmp.setVerticalAlignment(VerticalAlignment.CENTER);
		xssfStyleTmp.setBorderBottom(BorderStyle.THIN);
		xssfStyleTmp.setBorderTop(BorderStyle.THIN);
		xssfStyleTmp.setBorderRight(BorderStyle.THIN);
		xssfStyleTmp.setBorderLeft(BorderStyle.THIN);

		subFontTmp = workbook.createFont();
		subFontTmp.setFontName("맑은 고딕");
		subFontTmp.setColor(IndexedColors.BLACK.getIndex());
		subFontTmp.setFontHeightInPoints((short) 10);
		subFontTmp.setBold(false);
		xssfStyleTmp.setFont(subFontTmp);

		rowCntrCnt = sheet.createRow((short) 2);//총괄-건수
		rowCntrCnt.setHeight((short) 400);
		rowCntrCnt.createCell(0).setCellStyle(xssfStyleTmp);
		rowCntrCnt.createCell(1).setCellStyle(xssfStyleTmp);
		rowCntrCnt.getCell(0).setCellValue("건수");
		rowCntrCnt.getCell(1).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(0).get("NOW_YR"))));

		// Table Body
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> list: " + list.get(0).get("NOW_YR"));

	}

	private void buildItemSheetForPastYearMctpv(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		Row rowMctpvAmt = sheet.createRow((short) 3);
		rowMctpvAmt.setHeight((short) 400);
		rowMctpvAmt.createCell(0).setCellStyle(xssfStyleTmp);
		rowMctpvAmt.createCell(1).setCellStyle(xssfStyleTmp);
		rowMctpvAmt.getCell(0).setCellValue("금액");
		rowMctpvAmt.getCell(1).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(0).get("NOW_YR_DEC"))));

		((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
		int cellCnt = 6;
		for(int i = 0; i < cellCnt; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 700);
		}
	}

	private void buildItemSheetForPastYearPath(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		Sheet sheetPath = workbook.createSheet(title);
		sheetPath.addMergedRegion(new CellRangeAddress(0,0,0,5));
		sheetPath.addMergedRegion(new CellRangeAddress(1,1,0,1));

		Row rowPath = sheetPath.createRow((short) 0);//2. 기부방법별
		rowPath.setHeight((short) 800);
		Cell titleCell = rowPath.createCell(0);
		titleCell.setCellStyle(mergedTitle);
		titleCell.setCellValue(title);

		Row rowPath1 = sheetPath.createRow((short) 1);
		rowPath1.setHeight((short) 600);
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

	private void buildItemSheetForPastYearAmt(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		// 3. Cell (컬럼) 설정

		HeaderCell[] headerCells = new HeaderCell[] {
		  new HeaderCell(700, 	"구분")
		  ,new HeaderCell(700, 	"건수")
		  ,new HeaderCell(3000, 	"금액")
		  ,new HeaderCell(3000, 	"(백만원 환산)")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheetAmt = workbook.createSheet(title);
		Row rowAmt = sheetAmt.createRow((short) 0);

		((SXSSFSheet) sheetAmt).trackAllColumnsForAutoSizing();
		createSheetHeader(sheetAmt, rowAmt, headerCells, title);

		// Table Body

		int rowIndex = 2;
		for(HashMap<String, Object> obj : list){

			rowAmt = sheetAmt.createRow(rowIndex);
			rowAmt.setHeight((short) 400);

			rowAmt.createCell(0).setCellStyle(xssfStyleTmp);
			rowAmt.createCell(1).setCellStyle(xssfStyleTmp);
			rowAmt.createCell(2).setCellStyle(xssfStyleTmp);
			rowAmt.createCell(3).setCellStyle(xssfStyleTmp);

			rowAmt.getCell(0).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("GRAMT"))));
			rowAmt.getCell(1).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
			rowAmt.getCell(2).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("ATM"))));
			rowAmt.getCell(3).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("ATMHUND"))));

			rowIndex++;
		}

		((SXSSFSheet) sheetAmt).trackAllColumnsForAutoSizing();
		int cellCnt = 4;
		for(int i = 0; i < cellCnt; i++) {
			sheetAmt.autoSizeColumn(i);
			sheetAmt.setColumnWidth(i, sheetAmt.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetForPastYearAgeAmtDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		// 3. Cell (컬럼) 설정

		HeaderCell[] headerCells = new HeaderCell[] {
				new HeaderCell(700, 	"구분")
				,new HeaderCell(700, 	"건수")
				,new HeaderCell(3000, 	"비율")
				,new HeaderCell(3000, 	"금액")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheetAge = workbook.createSheet(title);
		Row rowAge = sheetAge.createRow((short) 0);

		((SXSSFSheet) sheetAge).trackAllColumnsForAutoSizing();
		createSheetHeader(sheetAge, rowAge, headerCells, title);

		// Table Body

		int rowIndex = 2;
		for(HashMap<String, Object> obj : list){

			rowAge = sheetAge.createRow(rowIndex);
			rowAge.setHeight((short) 400);

			rowAge.createCell(0).setCellStyle(xssfStyleTmp);
			rowAge.createCell(1).setCellStyle(xssfStyleTmp);
			rowAge.createCell(2).setCellStyle(xssfStyleTmp);
			rowAge.createCell(3).setCellStyle(xssfStyleTmp);

			rowAge.getCell(0).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("AGES"))));
			rowAge.getCell(1).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("TNOCS"))));
			rowAge.getCell(2).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("RT"))+"%"));
			rowAge.getCell(3).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("ASUM"))));

			rowIndex++;
		}

		((SXSSFSheet) sheetAge).trackAllColumnsForAutoSizing();
		int cellCnt = 4;
		for(int i = 0; i < cellCnt; i++) {
			sheetAge.autoSizeColumn(i);
			sheetAge.setColumnWidth(i, sheetAge.getColumnWidth(i) + 700);
		}

	}


	private void buildItemSheetAddrPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		sheetAddrDntn = workbook.createSheet(title);
		sheetAddrDntn.addMergedRegion(new CellRangeAddress(0,0,0,5));
		Row rowAd = sheetAddrDntn.createRow((short) 0);
		rowAd.setHeight((short) 800);
		Cell titleCell = rowAd.createCell(0);
		titleCell.setCellStyle(mergedTitle);
		titleCell.setCellValue(title);

		Row rowAdTitle = sheetAddrDntn.createRow((short) 1);
		rowAdTitle.setHeight((short) 600);
		rowAdTitle.createCell(0).setCellStyle(xssfStyle);
		rowAdTitle.createCell(1).setCellStyle(xssfStyle);
		rowAdTitle.createCell(2).setCellStyle(xssfStyle);
		rowAdTitle.createCell(3).setCellStyle(xssfStyle);
		rowAdTitle.createCell(4).setCellStyle(xssfStyle);
		rowAdTitle.createCell(5).setCellStyle(xssfStyle);
		rowAdTitle.getCell(0).setCellValue("구분");
		rowAdTitle.getCell(1).setCellValue("건수");
		rowAdTitle.getCell(2).setCellValue("비율");
		rowAdTitle.getCell(3).setCellValue("금액");
		rowAdTitle.getCell(4).setCellValue("(백만원 환산)");
		rowAdTitle.getCell(5).setCellValue("비중");

		int sumDntn = 0;//건수합계
		Float sumRt = (float) 0.0;//비율합계
		int rowIndex = 1;
		Row rowContent = null;
		for(HashMap<String, Object> obj : list) {
			if(rowIndex == 1) {// 첫번째 리스트오브젝트의 값은 총계라서 필요없음 두번째 부터 서울~
				rowIndex++;
				sumDntn = Integer.parseInt(String.valueOf(obj.get("NOW_YR")));// 합산만 꺼내놓고
				continue;
			}
			rowContent = sheetAddrDntn.createRow(rowIndex);//rowIndex는
			rowContent.setHeight((short) 400);
			rowContent.createCell(0).setCellStyle(xssfStyleTmp);
			rowContent.createCell(1).setCellStyle(xssfStyleTmp);
			rowContent.createCell(2).setCellStyle(xssfStyleTmp);
			rowContent.createCell(3).setCellStyle(xssfStyleTmp);
			rowContent.createCell(4).setCellStyle(xssfStyleTmp);
			rowContent.createCell(5).setCellStyle(xssfStyleTmp);

			rowContent.getCell(0).setCellValue(String.valueOf(obj.get("UPPER_LOCGOV_NM")));
			rowContent.getCell(1).setCellValue(StringUtils.numberFormat(String.valueOf(obj.get("NOW_YR"))));
			String rt_jan = String.format("%.3f",(Float.parseFloat(obj.get("NOW_YR").toString())*100 / Float.parseFloat(String.valueOf(sumDntn))));
			sumRt += Float.parseFloat(rt_jan);
			rowContent.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

			rowIndex++;
		}
		rowContent = sheetAddrDntn.createRow(rowIndex);//rowIndex마지막 합계만들 마지막 줄
		rowContent.setHeight((short) 400);
		rowContent.createCell(0).setCellStyle(xssfStyleTmp);
		rowContent.createCell(1).setCellStyle(xssfStyleTmp);
		rowContent.createCell(2).setCellStyle(xssfStyleTmp);
		rowContent.createCell(3).setCellStyle(xssfStyleTmp);
		rowContent.createCell(4).setCellStyle(xssfStyleTmp);
		rowContent.createCell(5).setCellStyle(xssfStyleTmp);
		rowContent.getCell(0).setCellValue("합계");
		rowContent.getCell(1).setCellValue(StringUtils.numberFormat(String.valueOf(sumDntn)));
		rowContent.getCell(2).setCellValue(String.valueOf((int)Math.round(sumRt)+"%"));


		((SXSSFSheet) sheetAddrDntn).trackAllColumnsForAutoSizing();
		int cellCnt = 5;
		for(int i = 0; i < cellCnt; i++) {
			sheetAddrDntn.autoSizeColumn(i);
			sheetAddrDntn.setColumnWidth(i, sheetAddrDntn.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetAddrPastYearMctpv(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		double sumRt = 0;//개별금액합계
		Object sumMctpv = null;//12월 누계값 금액합계
		Object exchHund = null;
		int rowIndex = 1;
		Row rowContent = null;
		DecimalFormat formatter = new DecimalFormat("###,###");// 포인트 세자리 콤마 처리
		for(HashMap<String, Object> obj : list) {
			if(rowIndex == 1) {// 첫번째 리스트오브젝트의 값은 총계라서 필요없음 두번째 부터 서울~
				rowIndex++;
				sumMctpv = obj.get("NOW_YR_DEC");//(12월의 누계값) 합산만 꺼내놓고
				continue;
			}
			rowContent = sheetAddrDntn.getRow(rowIndex);//rowIndex는
			//(금액컬럼) 포인트 세자리 콤마 처리
			exchHund = obj.get("NOW_YR_DEC");
			rowContent.getCell(3).setCellValue(formatter.format(obj.get("NOW_YR_DEC")));//StringUtils.numberFormat(String.valueOf(obj.get("AGES")))

			//(백만원환산컬럼)
			String bdSrt = exchangHundFlot(exchHund, formatter);//값 변환 컴마처리 소수점처리
			rowContent.getCell(4).setCellValue(bdSrt);

			//(비중컬럼)
			String rt_jan = String.format("%.3f",(Double.parseDouble(obj.get("NOW_YR_DEC").toString())*100 / Double.parseDouble(String.valueOf(sumMctpv))));
			sumRt += Float.parseFloat(rt_jan);
			rowContent.getCell(5).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

			rowIndex++;
		}
		rowContent = sheetAddrDntn.getRow(rowIndex);//rowIndex마지막 합계만들 마지막 줄
		rowContent.setHeight((short) 400);
		rowContent.getCell(0).setCellStyle(xssfStyleTmp);
		rowContent.getCell(1).setCellStyle(xssfStyleTmp);
		rowContent.getCell(2).setCellStyle(xssfStyleTmp);
		rowContent.getCell(3).setCellStyle(xssfStyleTmp);
		rowContent.getCell(4).setCellStyle(xssfStyleTmp);
		rowContent.getCell(5).setCellStyle(xssfStyleTmp);

		rowContent.getCell(3).setCellValue(formatter.format(sumMctpv));//금액 합계 컬럼
		rowContent.getCell(4).setCellValue(exchangHundFlot(sumMctpv, formatter));//금액 합계 환산 컬럼
		rowContent.getCell(5).setCellValue(String.valueOf((int)Math.round(sumRt)+"%"));

		((SXSSFSheet) sheetAddrDntn).trackAllColumnsForAutoSizing();
		int cellCnt = 5;
		for(int i = 0; i < cellCnt; i++) {
			sheetAddrDntn.autoSizeColumn(i);
			sheetAddrDntn.setColumnWidth(i, sheetAddrDntn.getColumnWidth(i) + 700);
		}


	}


	private void buildItemSheetMonthPastYearDntn(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		sheetMonthDntn = workbook.createSheet(title);
		sheetMonthDntn.addMergedRegion(new CellRangeAddress(0,0,0,5));
		Row rowAd = sheetMonthDntn.createRow((short) 0);
		rowAd.setHeight((short) 800);
		Cell titleCell = rowAd.createCell(0);
		titleCell.setCellStyle(mergedTitle);
		titleCell.setCellValue(title);

		Row rowAdTitle = sheetMonthDntn.createRow((short) 1);
		rowAdTitle.setHeight((short) 600);
		rowAdTitle.createCell(0).setCellStyle(xssfStyle);
		rowAdTitle.createCell(1).setCellStyle(xssfStyle);
		rowAdTitle.createCell(2).setCellStyle(xssfStyle);
		rowAdTitle.createCell(3).setCellStyle(xssfStyle);
		rowAdTitle.createCell(4).setCellStyle(xssfStyle);
		rowAdTitle.createCell(5).setCellStyle(xssfStyle);
		rowAdTitle.getCell(0).setCellValue("구분");
		rowAdTitle.getCell(1).setCellValue("건수");
		rowAdTitle.getCell(2).setCellValue("비중");
		rowAdTitle.getCell(3).setCellValue("금액");
		rowAdTitle.getCell(4).setCellValue("비중");
		rowAdTitle.getCell(5).setCellValue("고유아이디");

//		int sumDntn = 0;//건수합계
//		Float sumRt = (float) 0.0;//비율합계
		int rowIndex = 2;
		Row rowContent = null;
		for(HashMap<String, Object> obj : list) {
			rowContent = sheetMonthDntn.createRow(rowIndex);//rowIndex는
			rowContent.setHeight((short) 400);
			rowContent.createCell(0).setCellStyle(xssfStyleTmp);
			rowContent.createCell(1).setCellStyle(xssfStyleTmp);
			rowContent.createCell(2).setCellStyle(xssfStyleTmp);
			rowContent.createCell(3).setCellStyle(xssfStyleTmp);
			rowContent.createCell(4).setCellStyle(xssfStyleTmp);
			rowContent.createCell(5).setCellStyle(xssfStyleTmp);

			if((rowIndex-2) == 12) { //12월까지만
				rowContent.getCell(0).setCellValue("합계");//13월대신 합계
				break;
			}else {
				rowContent.getCell(0).setCellValue(String.valueOf(rowIndex-1)+"월");
			}
			rowIndex++;
		}
		//합계
		rowContent.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR").toString()));//합계

		//건수 컬럼 값넣기
		Row rowDntn = null;
		String rt_jan = "";//건수 비중
		Float sumMonthDntnRt = (float) 0.0;//건수 비중 누계
		Float allMaontDntn = Float.parseFloat(list.get(0).get("NOW_YR").toString());//건수 합계
		rowDntn = sheetMonthDntn.getRow(2);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_JAN").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_JAN").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(3);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_FEB").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_FEB").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(4);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_MAR").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_MAR").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(5);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_APR").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_APR").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(6);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_MAY").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_MAY").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(7);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_JUN").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_JUN").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(8);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_JUL").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_JUL").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(9);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_AUG").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_AUG").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(10);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_SEP").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_SEP").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(11);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_OCT").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_OCT").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(12);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_NOV").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_NOV").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(13);
		rowDntn.getCell(1).setCellValue(StringUtils.numberFormat(list.get(0).get("NOW_YR_DEC").toString()));
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(0).get("NOW_YR_DEC").toString())*100 / allMaontDntn));
		sumMonthDntnRt += Math.round(Float.parseFloat(rt_jan));
		rowDntn.getCell(2).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");

		rowDntn = sheetMonthDntn.getRow(14);//(비중컬럼)
		rowDntn.getCell(2).setCellValue(Math.round(sumMonthDntnRt)+"%");


		((SXSSFSheet) sheetMonthDntn).trackAllColumnsForAutoSizing();
		int cellCnt = 5;
		for(int i = 0; i < cellCnt; i++) {
			sheetMonthDntn.autoSizeColumn(i);
			sheetMonthDntn.setColumnWidth(i, sheetMonthDntn.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetMonthPastYearMctpv(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}

		//금액 컬럼 값넣기
		Row rowMctpv = null;
		String rt_jan = "";
		Float sumMonthAmtSum = (float) 0.0;//금액합계

		rowMctpv = sheetMonthDntn.getRow(2);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_JAN")));//StringUtils.numberFormat(String.valueOf(obj.get("AGES")))
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_JAN")));

		rowMctpv = sheetMonthDntn.getRow(3);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_FEB")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_FEB")));

		rowMctpv = sheetMonthDntn.getRow(4);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_MAR")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_MAR")));

		rowMctpv = sheetMonthDntn.getRow(5);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_APR")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_APR")));

		rowMctpv = sheetMonthDntn.getRow(6);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_MAY")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_MAY")));

		rowMctpv = sheetMonthDntn.getRow(7);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_JUN")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_JUN")));

		rowMctpv = sheetMonthDntn.getRow(8);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_JUL")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_JUL")));

		rowMctpv = sheetMonthDntn.getRow(9);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_AUG")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_AUG")));

		rowMctpv = sheetMonthDntn.getRow(10);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_SEP")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_SEP")));

		rowMctpv = sheetMonthDntn.getRow(11);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_OCT")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_OCT")));

		rowMctpv = sheetMonthDntn.getRow(12);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_NOV")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_NOV")));

		rowMctpv = sheetMonthDntn.getRow(13);
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(list.get(17).get("NOW_YR_DEC")));
		sumMonthAmtSum += Float.parseFloat(String.valueOf(list.get(17).get("NOW_YR_DEC")));

		rowMctpv = sheetMonthDntn.getRow(14);//합계금액로우
		rowMctpv.getCell(3).setCellValue(StringUtils.numberFormat(String.valueOf(sumMonthAmtSum)));

		//금액의 비중 값, 고유아이디 넣기
		int sumMonthMctpvRt  = 0;
		rowMctpv = sheetMonthDntn.getRow(2);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_JAN").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_JAN"))));

		rowMctpv = sheetMonthDntn.getRow(3);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_FEB").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_FEB"))));

		rowMctpv = sheetMonthDntn.getRow(4);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_MAR").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_MAR"))));

		rowMctpv = sheetMonthDntn.getRow(5);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_APR").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_APR"))));

		rowMctpv = sheetMonthDntn.getRow(6);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_MAY").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_MAY"))));

		rowMctpv = sheetMonthDntn.getRow(7);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_JUN").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_JUN"))));

		rowMctpv = sheetMonthDntn.getRow(8);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_JUL").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_JUL"))));

		rowMctpv = sheetMonthDntn.getRow(9);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_AUG").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_AUG"))));

		rowMctpv = sheetMonthDntn.getRow(10);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_SEP").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_SEP"))));

		rowMctpv = sheetMonthDntn.getRow(11);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_OCT").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_OCT"))));

		rowMctpv = sheetMonthDntn.getRow(12);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_NOV").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_NOV"))));

		rowMctpv = sheetMonthDntn.getRow(13);
		rt_jan = String.format("%.3f",(Float.parseFloat(list.get(17).get("NOW_YR_DEC").toString())*100 / sumMonthAmtSum));
		sumMonthMctpvRt += Math.round(Float.parseFloat(rt_jan));
		rowMctpv.getCell(4).setCellValue(Math.round(Float.parseFloat(rt_jan))+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(list.get(18).get("NOW_YR_DEC"))));

		int uIdSum = 0;
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_JAN")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_FEB")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_MAR")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_APR")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_MAY")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_JUN")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_JUL")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_AUG")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_SEP")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_OCT")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_NOV")));
		uIdSum += Integer.parseInt(String.valueOf(list.get(18).get("NOW_YR_DEC")));
		rowMctpv = sheetMonthDntn.getRow(14);
		rowMctpv.getCell(4).setCellValue(sumMonthMctpvRt+"%");
		rowMctpv.getCell(5).setCellValue(StringUtils.numberFormat(String.valueOf(uIdSum)));
//		rowMctpv.getCell(5).setTextLeft(sheet, uIdSum, null, rt_jan);



		((SXSSFSheet) sheetMonthDntn).trackAllColumnsForAutoSizing();
		int cellCnt = 5;
		for(int i = 0; i < cellCnt; i++) {
			sheetMonthDntn.autoSizeColumn(i);
			sheetMonthDntn.setColumnWidth(i, sheetMonthDntn.getColumnWidth(i) + 700);
		}

	}

	private void buildItemSheetPubGoodsPastYearMctpv(SXSSFWorkbook workbook, List<HashMap<String, Object>> list, StatisticsParam statisticsParam, String title) {
		if (list == null) {
			return;
		}
		// 3. Cell (컬럼) 설정

		HeaderCell[] headerCells = new HeaderCell[] {
				new HeaderCell(700, 	"행레이블")
				,new HeaderCell(700, 	"합계:판매가")
				,new HeaderCell(700, 	"개수:수량")
		};

		/**
		 * 상단 타이틀 및 테이블 헤더 생성.
		 */
		Sheet sheetPubGoods = workbook.createSheet(title);
		Row rowPubGoods = sheetPubGoods.createRow((short) 0);

		((SXSSFSheet) sheetPubGoods).trackAllColumnsForAutoSizing();
		createSheetHeader(sheetPubGoods, rowPubGoods, headerCells, title+" (판매량순 상위 30개)");

		// Table Body
		int rowIndex = 2;
		for(HashMap<String, Object> obj : list){

			rowPubGoods = sheetPubGoods.createRow(rowIndex);
			rowPubGoods.setHeight((short) 400);

			// CELL 데이터.
			CellIndex cellIndex = new CellIndex(-1);
			setTextLeft(sheetPubGoods, 	rowIndex, cellIndex, "  "+ String.valueOf(obj.get("ITEM_NAME")));
			setText(sheetPubGoods, 	rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.get("SUMPRICE"))));
			setText(sheetPubGoods, 	rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.get("SUMQUANTITY"))));

			rowIndex++;
		}

		((SXSSFSheet) sheetPubGoods).trackAllColumnsForAutoSizing();
		int cellCnt = 5;
		for(int i = 0; i < cellCnt; i++) {
			sheetPubGoods.autoSizeColumn(i);
			sheetPubGoods.setColumnWidth(i, sheetPubGoods.getColumnWidth(i) + 700);
		}


	}
	/**
	 * //내부 유틸용 입니다. 백만원단위변환 및 소수점처리기능
	 * @param oVal
	 * @return
	 */
	@SuppressWarnings("unused")
	private String exchangHundFlot (Object oVal, DecimalFormat formatter) {
		BigDecimal bd = new BigDecimal(Double.parseDouble(String.valueOf(oVal)));//데시말변환
		String bdSrt = bd.setScale(-6, RoundingMode.HALF_UP).toPlainString();//백만원단위변환 반올림

		//백만 아래 제거 준비
		int strLength = bdSrt.length();
		String tmpExHund = bdSrt; //임시저장 백만원 환산

		if (strLength > 6) {//백만이상 정수처리
			tmpExHund = bdSrt.substring(0, strLength-6);//백만이상일경우 정수처리->백만 아래 제거
			tmpExHund = formatter.format(Double.parseDouble(tmpExHund));
		} else { //백만이하일경우 소수점처리
			tmpExHund = String.format("%.3f",Integer.parseInt(tmpExHund)*0.000001);//소수점 3자리
		}
		return tmpExHund;
	}

}

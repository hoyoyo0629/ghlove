package saleson.shop.statistics.support;

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
import saleson.shop.statistics.domain.BaseStats;
import saleson.shop.statistics.domain.DateStatsSummary;

public class ShopStatisticsMoisExcelView extends AbstractSXSSFExcelView{

	private List<DateStatsSummary> list;

	public ShopStatisticsMoisExcelView() {

		setFileName("답례품 구매현황 통계_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<DateStatsSummary>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<DateStatsSummary> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "main";
		String title = "답례품 구매현황";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(3500, 	"지자체"),
			new HeaderCell(3500, 	"주문 건수"),
			new HeaderCell(3500, 	"주문 금액(P)"),
			new HeaderCell(3500, 	"취소/반품 건수"),
			new HeaderCell(3500, 	"취소/반품 금액(P)"),
			new HeaderCell(3500, 	"합계 건수"),
			new HeaderCell(3500, 	"합계 금액(P)")
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
		int length = list.size();
		for(DateStatsSummary obj : list){
			for(BaseStats obj2 : obj.getGroupStats()){

				// 행 높이 설정
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 400);

				CellIndex cellIndex = new CellIndex(-1);

				setText(sheet, 			rowIndex, cellIndex, String.valueOf(length - (rowIndex - 2))); //No.
				setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getGroupObject()));
				setNumberFormat(sheet, 			rowIndex, cellIndex, Long.valueOf(obj2.getSaleCount()).intValue());
				setNumberFormat(sheet, 			rowIndex, cellIndex, Long.valueOf(obj2.getSaleAmount()).intValue());
				setNumberFormat(sheet, 			rowIndex, cellIndex, Long.valueOf(obj2.getCancelCount()).intValue());
				setNumberFormat(sheet, 			rowIndex, cellIndex, Long.valueOf(obj2.getCancelAmount()).intValue());
				setNumberFormat(sheet, 			rowIndex, cellIndex, Long.valueOf(obj2.getSumCount()).intValue());
				setNumberFormat(sheet, 			rowIndex, cellIndex, Long.valueOf(obj2.getSumAmount()).intValue());

				rowIndex++;
			}
		}
	}


}

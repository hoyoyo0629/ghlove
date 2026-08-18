package saleson.shop.give.givepoint.support;

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
import saleson.shop.give.givepoint.domain.GivePoint;

public class GivePointLocgovExcelView extends AbstractSXSSFExcelView{

	private List<GivePoint> list;

	public GivePointLocgovExcelView() {

		setFileName("GIVE_POINT_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GivePoint>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GivePoint> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "give_state_main";
		String title = "기부포인트 현황";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"년도"),
			new HeaderCell(3500, 	"기부금액"),
			new HeaderCell(3500, 	"발생포인트"),
			new HeaderCell(3500, 	"사용포인트"),
			new HeaderCell(3500, 	"포인트잔액")
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
		for(GivePoint obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrYear()));
			setText(sheet, 			rowIndex, cellIndex, obj.getCntrAmt());
			setText(sheet, 			rowIndex, cellIndex, obj.getCntrPoint());
			setText(sheet, 			rowIndex, cellIndex, obj.getCntrUsePoint());
			setText(sheet, 			rowIndex, cellIndex, obj.getCntrBlcePoint());

			rowIndex++;
		}
	}


}

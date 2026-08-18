package saleson.shop.lclgvHnrUser.support;

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
import saleson.shop.give.giveoperation.domain.GiveOperation;
import saleson.shop.lclgvHnrUser.domain.LclgvHnrUserMng;

public class LclgvHnrUserViewHistExcelView extends AbstractSXSSFExcelView{

	private List<LclgvHnrUserMng> list;

	public LclgvHnrUserViewHistExcelView() {

		setFileName("기부혜택증열람현황_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<LclgvHnrUserMng>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<LclgvHnrUserMng> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "honorViewHist";
		String title = "기부혜택증 열람현황";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"열람일자"),
			new HeaderCell(3500, 	"시도"),
			new HeaderCell(3500, 	"지자체"),
			new HeaderCell(2500, 	"사용자명"),
			new HeaderCell(3500, 	"열람횟수"),
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
		for(LclgvHnrUserMng obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getViewYm()));
			setText(sheet, 			rowIndex, cellIndex, obj.getUpperLocgovNm());
			setText(sheet, 			rowIndex, cellIndex, obj.getLclgvCdNm());
			setText(sheet, 			rowIndex, cellIndex, obj.getUserName());
			setTextRight(sheet, rowIndex, cellIndex, String.valueOf(obj.getViewCnt()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ","));

			rowIndex++;
		}
	}


}

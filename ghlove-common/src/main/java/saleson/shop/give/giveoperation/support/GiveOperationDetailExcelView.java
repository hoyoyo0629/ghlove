package saleson.shop.give.giveoperation.support;

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
import saleson.shop.give.giveoperation.domain.CtbnyOpratn;
import saleson.shop.give.giveoperation.domain.GiveOperation;

public class GiveOperationDetailExcelView extends AbstractSXSSFExcelView{

	private List<CtbnyOpratn> list;

	public GiveOperationDetailExcelView() {

		setFileName("기부금_운용정보_상세_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		
		list = (List<CtbnyOpratn>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<CtbnyOpratn> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "main";
		String title = "기부금 운용정보 상세";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"지출일"),
			new HeaderCell(3500, 	"사업명"),
			new HeaderCell(3500, 	"사업목적"),
			new HeaderCell(3500, 	"사용금액"),
			new HeaderCell(3500, 	"내용"),
			new HeaderCell(3500, 	"작성자")
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
		for(CtbnyOpratn obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getExpndtrDe()));	// 지출일
			setText(sheet, 			rowIndex, cellIndex, obj.getBsnsNm());	// 사업명
			setText(sheet, 			rowIndex, cellIndex, obj.getBsnsPurpsNm());	// 사업목적
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getExpndtrAmt()));	// 사용금액
			setText(sheet, 			rowIndex, cellIndex, obj.getBsnsCn());							// 내용
			setText(sheet, 			rowIndex, cellIndex, obj.getLoginId() + "(" + obj.getUserName() + ")"); // 작성자

			rowIndex++;
		}
	}


}

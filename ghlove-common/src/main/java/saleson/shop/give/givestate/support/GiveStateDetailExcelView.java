package saleson.shop.give.givestate.support;

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
import saleson.shop.give.givestate.domain.GiveState;

public class GiveStateDetailExcelView extends AbstractSXSSFExcelView{

	private List<GiveState> list;

	public GiveStateDetailExcelView() {

		setFileName("GIVE_STATE_DETAIL" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GiveState>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GiveState> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "give_state_main";
		String title = "기부금 모금현황";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"기부일시"),
			new HeaderCell(2500, 	"납부일자"),
			new HeaderCell(2500, 	"전자납부번호"),
			new HeaderCell(2500, 	"기부자명"),
			new HeaderCell(2500, 	"아이디"),
			new HeaderCell(2500, 	"생년월일"),
			new HeaderCell(2500, 	"핸드폰번호"),
			new HeaderCell(2500, 	"SMS 수신여부"),
			new HeaderCell(2500, 	"거소지자체"),
			new HeaderCell(2500, 	"특정사업기부 사업명"),
			new HeaderCell(2500, 	"기부금액"),
			new HeaderCell(2500, 	"발생포인트"),
			new HeaderCell(2500, 	"잔여포인트"),
			new HeaderCell(2500, 	"답례품"),
			new HeaderCell(2500, 	"기부형태"),
			new HeaderCell(2500, 	"민간연계기관"),
			new HeaderCell(2500, 	"기부영수증 처리여부")
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
		for(GiveState obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrDe()));
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getSttemntPayDe()));
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getElctrnPayNo()));
			setText(sheet, 			rowIndex, cellIndex, obj.getUserName());
			setText(sheet, 			rowIndex, cellIndex, obj.getLoginId());
			setText(sheet, 			rowIndex, cellIndex, (obj.getBirthday() == null) ? "" : String.valueOf(obj.getBirthday()));

			String phoneStr	= (obj.getPhoneNumber() == null) ? "" : String.valueOf(obj.getPhoneNumber());
			setText(sheet, 			rowIndex, cellIndex, phoneStr);
			setText(sheet, 			rowIndex, cellIndex, obj.getPbancName());
			setText(sheet, 			rowIndex, cellIndex, obj.getPsitnLocgovName());
			setText(sheet, 			rowIndex, cellIndex, obj.getPrjSubject());
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getCntrAmt()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getCntrPoint()));
			setNumberFormat(sheet, 			rowIndex, cellIndex, Integer.parseInt(obj.getCntrBlcePoint()));
			setText(sheet, 			rowIndex, cellIndex, obj.getRtnpsntReqstCode().equals("100") ? "제공받음" : "제공받지 않음");
			setText(sheet, 			rowIndex, cellIndex, obj.getCntrPathName());
			String detailStr	= (obj.getDetail() == null) ? "" : String.valueOf(obj.getDetail());
			setText(sheet, 			rowIndex, cellIndex, detailStr);
			setText(sheet, 			rowIndex, cellIndex, obj.getNtsCnt() > 0 ? "신고" : "미신고");


			rowIndex++;
		}
	}


}

package saleson.shop.give.givestate.support;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.dreamsecurity.magice2e.util.Log;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.domain.GiveStateTest;

public class GiveStateAllExcelView extends AbstractSXSSFExcelView{

	private List<GiveStateTest> list;

	public GiveStateAllExcelView() {

		setFileName("ALL_GIVE_STATE_DETAIL" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		list = (List<GiveStateTest>)model.get("list");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, list);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<GiveStateTest> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "all_give_state_main";
		String title = "기부금 전체현황";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(4000, 	"기부번호"),
			new HeaderCell(2500, 	"기부일자"),
			new HeaderCell(3000, 	"기부신고시각"),
			new HeaderCell(2500, 	"결제일자"),
			new HeaderCell(3000, 	"최종시각"),
			new HeaderCell(4000, 	"전자납부번호"),
			new HeaderCell(2500, 	"기부자명"),
			new HeaderCell(2500, 	"아이디"),
			new HeaderCell(2500, 	"소속지자체"),
			new HeaderCell(2500, 	"기부지자체"),
			new HeaderCell(2500, 	"특정사업기부 사업명"),
			new HeaderCell(2500, 	"기부금액"),
			new HeaderCell(700, 	"답례품여부"),
			new HeaderCell(2500, 	"발생포인트"),
			new HeaderCell(2500, 	"잔액포인트"),
			new HeaderCell(1000, 	"기부형태"),
			new HeaderCell(2500, 	"납부유효일자"),
			new HeaderCell(2500, 	"민간연계기관"),
			new HeaderCell(2500, 	"접수은행/복지센터"),
			new HeaderCell(2500, 	"접수자명"),
			new HeaderCell(1000, 	"기부상태"),
			new HeaderCell(2500, 	"국세청"),
			new HeaderCell(1000, 	"삭제여부")
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
		for(GiveStateTest obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			String strCntrDe 		=  String.valueOf(obj.getCntrDe());
			String strSttemntPayDe 	= obj.getSttemntPayDe() != null ? String.valueOf(obj.getSttemntPayDe()) : "";
			
			strCntrDe 		= strCntrDe.substring(0, 4) + "-" + strCntrDe.substring(4, 6) + "-" + strCntrDe.substring(6, 8);
			if(strSttemntPayDe != "") {
				strSttemntPayDe = strSttemntPayDe.substring(0, 4) + "-" + strSttemntPayDe.substring(4, 6) + "-" + strSttemntPayDe.substring(6, 8);
			}
			
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrSn()));	//기부번호
			setText(sheet, 			rowIndex, cellIndex, strCntrDe);	//기부일자
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getFrstRegistPnttm()));	//기부신고시각
			setText(sheet, 			rowIndex, cellIndex, strSttemntPayDe);	//결제일자
			setText(sheet, 			rowIndex, cellIndex, obj.getLastUpdtPnttm() != null ? String.valueOf(obj.getLastUpdtPnttm()) : "");	//최종시각
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getElctrnPayNo()));	//전자납부번호
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getUserName()));	//기부자명
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getLoginId()));	//아이디
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getPsitnLocgovCode()));	//소속지자체
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrLocgovCode()));	//기부지자체
			setText(sheet, 			rowIndex, cellIndex, obj.getPrjSubject() != null ? String.valueOf(obj.getPrjSubject()) : "자치단체기부");	//특정사업기부 사업명
			setNumberFormat(sheet, 	rowIndex, cellIndex, Integer.parseInt(obj.getCntrAmt()));	//기부금액
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getRtnpsntReqstCode()));	//답례품여부
			setNumberFormat(sheet, 	rowIndex, cellIndex, obj.getCntrPoint() != null ? Integer.parseInt(obj.getCntrPoint()) : 0);	//발생포인트
			setNumberFormat(sheet, 	rowIndex, cellIndex, obj.getCntrBlcePoint() != null ? Integer.parseInt(obj.getCntrBlcePoint()): 0);	//잔액포인트
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrPathCode()));	//기부형태
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getPayValidDe()));	//납부유효일자
			String detailStr	= (obj.getDetail() == null) ? "" : String.valueOf(obj.getDetail());
			setText(sheet, 			rowIndex, cellIndex, detailStr);//민간연계기관
			setText(sheet, 			rowIndex, cellIndex, obj.getRceptBankNm() != null ? String.valueOf(obj.getRceptBankNm()) : "");	//접수은행
			setText(sheet, 			rowIndex, cellIndex, obj.getRcepterNm() != null ? String.valueOf(obj.getRcepterNm()) : "");	//접수자명
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getCntrSttusCode()));	//기부상태
			setText(sheet, 			rowIndex, cellIndex, obj.getNtsSttusMssage() != null ? String.valueOf(obj.getNtsSttusMssage()) : "");	//국세청
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(obj.getDeleteAt()));	//삭제여부
			
			rowIndex++;
		}
	}


}

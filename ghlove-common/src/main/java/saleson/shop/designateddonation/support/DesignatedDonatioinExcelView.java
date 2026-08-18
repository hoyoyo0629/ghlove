package saleson.shop.designateddonation.support;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.shop.code.domain.Code;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.statistics.domain.StatisticsReport;

public class DesignatedDonatioinExcelView extends AbstractSXSSFExcelView{
	
	public DesignatedDonatioinExcelView() {

		setFileName("특정사업기부 목록" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		
		DesignatedDonationSearchParam designatedDonationSearchParam = (DesignatedDonationSearchParam)model.get("designatedDonationSearchParam"); // 공용 파라미터
		List<DesignatedDonation> designatedDonationList = (List<DesignatedDonation>)model.get("designatedDonationList");
		List<Code> bsnsTypes = (List<Code>)model.get("bsnsTypeList");

		// 2. 데이터 생성(시트생성)
		if(SecurityUtils.hasRole("ROLE_ADMIN_1") || SecurityUtils.hasRole("ROLE_ADMIN_2") || SecurityUtils.hasRole("ROLE_ADMIN_3") || SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			buildItemSheet(workbook, designatedDonationList, bsnsTypes, designatedDonationSearchParam, "특정사업기부 목록");
		} else {
			buildItemSheetForLclgv(workbook, designatedDonationList, bsnsTypes, designatedDonationSearchParam, "특정사업기부 목록");
		}
	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<DesignatedDonation> list, List<Code> codeList, DesignatedDonationSearchParam designatedDonationSearchParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		
		HeaderCell[] headerCells = new HeaderCell[] {
			new HeaderCell(512, 	"ID"),
			new HeaderCell(5120, 	"지자체 명"),
			new HeaderCell(512, 	"모금상태"),
			new HeaderCell(7068, 	"사업구분"),
			new HeaderCell(7068, 	"사업명"),
			new HeaderCell(512, 	"기부건수"),
			new HeaderCell(1024, 	"시작일"),
			new HeaderCell(1024, 	"종료일"),
			new HeaderCell(1024, 	"목표금액"),
			new HeaderCell(1024, 	"모금액"),
			new HeaderCell(512, 	"달성율"),
			new HeaderCell(512, 	"공개여부"),
			new HeaderCell(1024, 	"사업부서")
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
		String prjStatus;
		String displayFlag;
		for(DesignatedDonation obj : list){

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrjId())));//ID
			setText(sheet,rowIndex, cellIndex, obj.getLocgovNm());//지자체
			if(obj.getPrjStatus() != null)  {
				prjStatus = obj.getPrjStatus().equals("1") ? "승인 대기" : obj.getPrjStatus().equals("2") ? "진행" : obj.getPrjStatus().equals("9") ? "종료" : "";
				setText(sheet,rowIndex, cellIndex, prjStatus);//모금상태
			}
			for(Code code : codeList) {
				if(code.getId() != null && code.getId().equals(obj.getBsnsType())) {
					setText(sheet,rowIndex, cellIndex, code.getDetail());//사업구분
				}
			}
			setText(sheet,rowIndex, cellIndex, obj.getPrjSubject());//사업명
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getCntrCnt())));//기부건수
			setText(sheet,rowIndex, cellIndex, obj.getPrjStDtForm());//시작일
			setText(sheet,rowIndex, cellIndex, obj.getPrjEdDtForm());//종료일
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getTargetAmt())));//목표금액
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getSumAmt())));//모금액
			setText(sheet,rowIndex, cellIndex, obj.getRateAmtStr());//달성율
			displayFlag = obj.getDisplayFlag().equals("Y") ? "공개" : "비공개";
			setText(sheet,rowIndex, cellIndex, displayFlag);//공개여부
			setText(sheet,rowIndex, cellIndex, obj.getDsgncntrPartName());//사업부서
			rowIndex++;
		}
	}
	
	private void buildItemSheetForLclgv(SXSSFWorkbook workbook, List<DesignatedDonation> list, List<Code> codeList, DesignatedDonationSearchParam designatedDonationSearchParam, String title) {
		if (list == null) {
			return;
		}

		// 3. Cell (컬럼) 설정
		
		HeaderCell[] headerCells = new HeaderCell[] {
			new HeaderCell(512, 	"ID"),
			new HeaderCell(512, 	"모금상태"),
			new HeaderCell(7068, 	"사업구분"),
			new HeaderCell(7068, 	"사업명"),
			new HeaderCell(512, 	"기부건수"),
			new HeaderCell(1024, 	"시작일"),
			new HeaderCell(1024, 	"종료일"),
			new HeaderCell(1024, 	"목표금액"),
			new HeaderCell(1024, 	"모금액"),
			new HeaderCell(512, 	"달성율"),
			new HeaderCell(512, 	"공개여부"),
			new HeaderCell(1024, 	"사업부서")
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
		String prjStatus;
		String displayFlag;
		for(DesignatedDonation obj : list){
			
			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getPrjId())));//ID
			if(obj.getPrjStatus() != null)  {
				prjStatus = obj.getPrjStatus().equals("1") ? "승인 대기" : obj.getPrjStatus().equals("2") ? "진행" : obj.getPrjStatus().equals("9") ? "종료" : "";
				setText(sheet,rowIndex, cellIndex, prjStatus);//모금상태
			}
			for(Code code : codeList) {
				if(code.getId() != null && code.getId().equals(obj.getBsnsType())) {
					setText(sheet,rowIndex, cellIndex, code.getDetail());//사업구분
				}
			}
			setText(sheet,rowIndex, cellIndex, obj.getPrjSubject());//사업명
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getCntrCnt())));//기부건수
			setText(sheet,rowIndex, cellIndex, obj.getPrjStDtForm());//시작일
			setText(sheet,rowIndex, cellIndex, obj.getPrjEdDtForm());//종료일
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getTargetAmt())));//목표금액
			setText(sheet,rowIndex, cellIndex, StringUtils.numberFormat(String.valueOf(obj.getSumAmt())));//모금액
			setText(sheet,rowIndex, cellIndex, obj.getRateAmtStr());//달성율
			displayFlag = obj.getDisplayFlag().equals("Y") ? "공개" : "비공개";
			setText(sheet,rowIndex, cellIndex, displayFlag);//공개여부
			setText(sheet,rowIndex, cellIndex, obj.getDsgncntrPartName());//사업부서
			rowIndex++;
		}
	}
}

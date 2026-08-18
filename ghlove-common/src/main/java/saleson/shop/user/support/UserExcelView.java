package saleson.shop.user.support;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.NumberUtils;
import com.onlinepowers.framework.web.servlet.view.AbstractSXSSFExcelView;
import com.onlinepowers.framework.web.servlet.view.support.CellIndex;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;
import saleson.common.Const;
import saleson.shop.user.domain.UserDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class UserExcelView extends AbstractSXSSFExcelView{

	private List<User> userList;

	public UserExcelView() {

		setFileName("USER_" + DateUtils.getToday(Const.DATETIME_FORMAT) + ".xlsx");
	}


	@Override
	public void buildExcelDocument(Map<String, Object> model,
									  SXSSFWorkbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		userList = (List<User>)model.get("userList");

		// 2. 데이터 생성(시트생성)
		buildItemSheet(workbook, userList);

	}

	private void buildItemSheet(SXSSFWorkbook workbook, List<User> list) {
		if (list == null) {
			return;
		}

		// 3. 시트생성
		String sheetTitle = "user_main";
		String title = "회원정보";

		// 4. Cell (컬럼) 설정
		HeaderCell[] headerCells = new HeaderCell[]{
			new HeaderCell(700, 	"No."),
			new HeaderCell(2500, 	"아이디"),
			new HeaderCell(2000, 	"이름"),
			new HeaderCell(700, 	"성별"),
			new HeaderCell(2000, 	"휴대폰"),
			new HeaderCell(3500, 	"이메일"),
			new HeaderCell(1000, 	"우편번호"),
			new HeaderCell(8000, 	"주소"),
			new HeaderCell(2000, 	MessageUtils.getMessage("M00049")),
			new HeaderCell(700, 	"방문횟수"),
			new HeaderCell(1100, 	"가입일"),
			new HeaderCell(1500, 	"SMS 수신동의"),
			new HeaderCell(1500, 	"Email 수신동의"),
			new HeaderCell(1500, 	"회원가입경로")
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
		for(User user : list){

			UserDetail userDetail = new UserDetail();

			userDetail = (UserDetail)user.getUserDetail();

			// 행 높이 설정
			row = sheet.createRow(rowIndex);
			row.setHeight((short) 400);

			CellIndex cellIndex = new CellIndex(-1);

			setText(sheet, 			rowIndex, cellIndex, String.valueOf(++index)); //No.
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(user.getLoginId())); //아이디
			setText(sheet, 			rowIndex, cellIndex, user.getUserName()); //이름
			setText(sheet, 			rowIndex, cellIndex, "M".equals(userDetail.getGender()) ? "남자" : "여자"); //성별
			setText(sheet, 			rowIndex, cellIndex, userDetail.getPhoneNumber()); //휴대폰
			setText(sheet, 			rowIndex, cellIndex, user.getEmail()); //이메일
			setText(sheet, 			rowIndex, cellIndex, userDetail.getPost()); //우편번호
			setTextLeft(sheet, 		rowIndex, cellIndex, userDetail.getAddress() + " " + userDetail.getAddressDetail()); //주소
			setText(sheet, 			rowIndex, cellIndex, NumberUtils.formatNumber(userDetail.getPoint(),"#,##0") + MessageUtils.getMessage("M00049")); //포인트
			setText(sheet, 			rowIndex, cellIndex, String.valueOf(user.getLoginCount())); //방문횟수
			setText(sheet, 			rowIndex, cellIndex, DateUtils.date(String.valueOf(user.getCreatedDate()))); //가입일

			String snsFlag = MessageUtils.getMessage("M00233");
			if (!"0".equals(userDetail.getReceiveSms())) {
				snsFlag = MessageUtils.getMessage("M00234");
			}
			setText(sheet, 			rowIndex, cellIndex, snsFlag); //SMS 수신동의

			String emailFlag = MessageUtils.getMessage("M00233");
			if (!"0".equals(userDetail.getReceiveEmail())) {
				emailFlag = MessageUtils.getMessage("M00234");
			}
			setText(sheet, 			rowIndex, cellIndex, emailFlag); //Email 수신동의

			String pbancFlag = MessageUtils.getMessage("M00233");
			if (!"0".equals(userDetail.getReceivePbanc())) {
				pbancFlag = MessageUtils.getMessage("M00234");
			}
			setText(sheet, 			rowIndex, cellIndex, pbancFlag); //광고 수신동의

			String siteFlag = "PC";
			if (StringUtils.hasText(userDetail.getSiteFlag())) {
				switch (userDetail.getSiteFlag()) {
					case "1":
						siteFlag = "Mobile";
						break;
					case "2":
						siteFlag = "Call";
						break;
					case "3":
						siteFlag = "SNS";
						break;
					default:
						break;
				}
			}
			setText(sheet, 			rowIndex, cellIndex, siteFlag); //회원가입경로

			rowIndex++;
		}
	}


}

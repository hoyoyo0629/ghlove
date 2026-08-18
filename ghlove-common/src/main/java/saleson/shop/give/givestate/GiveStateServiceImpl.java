package saleson.shop.give.givestate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.common.enumeration.SmsType;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.sms.SmsIpsService;
import saleson.shop.donation.CntrTaxTempDto;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.domain.GiveStateCntrUsePoint;
import saleson.shop.give.givestate.domain.GiveStateNts;
import saleson.shop.give.givestate.domain.GiveStateTest;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.qna.domain.Qna;
import saleson.shop.slave.SlaveGiveStateMapper;

@Service("giveStateService")
public class GiveStateServiceImpl implements GiveStateService {

	private static final Logger logger = LoggerFactory.getLogger(GiveStateServiceImpl.class);

	@Autowired
	GiveStateMapper giveStateMapper;

	@Autowired
	SlaveGiveStateMapper slaveGiveStateMapper;

	@Autowired
	private SmsIpsService smsIpsService;

	/**
	 * 기부금 모금현황 합계
	 */
	@Override
	public GiveState getGiveStateSum(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateSum(giveState);
	}

	/**
	 * 기부금 모금현황 목록 count
	 */
	@Override
	public int getGiveStateListCount(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateListCount(giveState);
	}

	/**
	 * 기부금 모금현황 목록
	 */
	@Override
	public List<GiveState> getGiveStateList(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateList(giveState);
	}

	/**
	 * 기부금 모금현황 상세 누적합계
	 */
	@Override
	public GiveState getGiveStateDetail(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateDetail(giveState);
	}

	/**
	 * 기부금 모금현황 상세 검색합계
	 */
	@Override
	public GiveState getGiveStateDetailSum(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateDetailSum(giveState);
	}

	/**
	 * 기부금 모금현황 상세 목록 count
	 */
	@Override
	public int getGiveStateDetailListCount(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateDetailListCount(giveState);
	}

	/**
	 * 기부금 모금현황 상세 목록
	 */
	@Override
	public List<GiveState> getGiveStateDetailList(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateDetailList(giveState);
	}

	/**
	 * 기부금 모금현황 상세 목록 엑셀 다운로드
	 */
	@Override
	public SXSSFWorkbook streamGiveStateDetailData(GiveState searchParam, int totalCount) {
		int pageSize 	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize = 1000;
		offset = 1;

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		searchParam.getPagination().setItemsPerPage(pageSize);
		searchParam.getPagination().setCurrentPage(offset);

		// SXSSF	: window size = 100
		// workbook	: 엑셀생성을 위한 내부 문서 모델
		// 메모리 적재 최대 100 row 로 설정, 나머지는 disk 로 flush
		// disk 저장 파일은 압축
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("GIVE_STATE_DETAIL");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "기부금 모금 상세현황 목록(" + searchParam.getLocgovFullNm() + ")");
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0,		2000);
		sheet.setColumnWidth(1,		5000);
		sheet.setColumnWidth(2,		3000);
		sheet.setColumnWidth(3,		7500);
		sheet.setColumnWidth(4,		3000);
		sheet.setColumnWidth(5,		3000);
		sheet.setColumnWidth(6,		7500);
		sheet.setColumnWidth(7,		5000);
		sheet.setColumnWidth(8,		5000);
		sheet.setColumnWidth(9,		5000);
		sheet.setColumnWidth(10,	5000);
		sheet.setColumnWidth(11,	5000);
		sheet.setColumnWidth(12,	5000);
		sheet.setColumnWidth(13,	5000);
		sheet.setColumnWidth(14,	5000);
		sheet.setColumnWidth(15,	5000);
		sheet.setColumnWidth(16,	5000);
		sheet.setColumnWidth(17,	5000);
		sheet.setColumnWidth(18,	5000);
		sheet.setColumnWidth(19,	5000);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0,		"No");
		cellStyle.header(header, 1,		"기부일시");
		cellStyle.header(header, 2,		"납부일자");
		cellStyle.header(header, 3,		"전자납부번호");
		cellStyle.header(header, 4,		"국세청");
		cellStyle.header(header, 5,		"기부자명");
		cellStyle.header(header, 6,		"아이디");
		cellStyle.header(header, 7,		"생년월일");
		cellStyle.header(header, 8,		"핸드폰번호");
		cellStyle.header(header, 9,		"SMS 수신여부");
		cellStyle.header(header, 10,	"거소지자체");
		cellStyle.header(header, 11,	"특정사업기부 사업명");
		cellStyle.header(header, 12,	"기부금액");
		cellStyle.header(header, 13,	"발생포인트");
		cellStyle.header(header, 14,	"잔여포인트");
		cellStyle.header(header, 15,	"답례품");
		cellStyle.header(header, 16,	"기부형태");
		cellStyle.header(header, 17,	"민간연계기관");
		cellStyle.header(header, 18,	"기부영수증 처리여부");
		cellStyle.header(header, 19,	"변경신청일시");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;

			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			List<GiveState> giveStateList = slaveGiveStateMapper.getGiveStateDetailList(searchParam);

			// 1000건 데이터를 엑셀 시트에 입력 가능하도록 row 단위의 전처리 실행
			for (GiveState giveState : giveStateList) {
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0,	StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1,	giveState.getCntrDe());
				cellStyle.data(row, 2,	giveState.getSttemntPayDe());
				cellStyle.data(row, 3,	giveState.getElctrnPayNo());
				cellStyle.data(row, 4,	giveState.getNtsSttusMssage());
				cellStyle.data(row, 5,	giveState.getUserName());
				cellStyle.data(row, 6,	giveState.getLoginId());
				cellStyle.data(row, 7,	giveState.getBirthday() == null ? "" : giveState.getBirthday());
				cellStyle.data(row, 8,	giveState.getPhoneNumber() == null ? "" : giveState.getPhoneNumber());
				cellStyle.data(row, 9,	giveState.getPbancName());
				cellStyle.data(row, 10,	giveState.getPsitnLocgovName());
				cellStyle.data(row, 11,	giveState.getPrjSubject());
				cellStyle.data(row, 12,	giveState.getCntrAmt());
				cellStyle.data(row, 13,	giveState.getCntrPoint());
				cellStyle.data(row, 14,	giveState.getCntrBlcePoint());
				cellStyle.data(row, 15,	giveState.getRtnpsntReqstCode().equals("100") ? "제공받음" : "제공받지 않음");
				cellStyle.data(row, 16,	giveState.getCntrPathName());
				cellStyle.data(row, 17,	giveState.getDetail() == null ? "" : giveState.getDetail());
				cellStyle.data(row, 18,	giveState.getNtsCnt() > 0 ? "신고" : "미신고");
				String frstRegistPnttm 	= giveState.getFrstRegistPnttm();
				String cntrReqmngCode 	= giveState.getCntrReqmngCode();
				String reqStatusCode 	= giveState.getReqStatusCode();
				if (
					frstRegistPnttm == null	||
					frstRegistPnttm == ""	||
					(cntrReqmngCode == "200" && reqStatusCode == "999")
				) {
					frstRegistPnttm = "-";
				}
				cellStyle.data(row, 19,	frstRegistPnttm);
			}

			// 다음 1000 row 조회를 위한 offset 설정
			// 쿼리문
			// LIMIT (#{pagination.currentPage} - 1) * #{pagination.itemsPerPage}, #{pagination.itemsPerPage}
			searchParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
	}

	/**
	 * 지자체 코드 목록
	 */
	@Override
	public List<HashMap<String, Object>> getLocgovCodeList(String code) {
		return slaveGiveStateMapper.getLocgovCodeList(code);
	}

	/**
	 * 지자체 코드 조회
	 */
	@Override
	public HashMap<String, Object> getLocgovCode(long userId) {
		return slaveGiveStateMapper.getLocgovCode(userId);
	}

	/**
	 * 기부내역 변경 상세 조회
	 * @param userId
	 * @return
	 */
	@Override
	public GiveStateTest getGiveStateModifyInfo(String elctrnPayNo) {
		return slaveGiveStateMapper.getGiveStateModifyInfo(elctrnPayNo);
	}

	// 기부금 전체조회 count
	@Override
	public int getGiveStateDetailListCountTest(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateDetailListCountTest(giveState);
	}

	// 기부금 전체조회 목록
	@Override
	public List<GiveStateTest> getGiveStateDetailListTest(GiveState giveState) {
		return slaveGiveStateMapper.getGiveStateDetailListTest(giveState);
	}

	// 기부금 전체조회 목록 엑셀 다운로드
	@Override
	public SXSSFWorkbook streamGiveStateDetailListTest(GiveState searchParam, int totalCount) {
		// Cursor<DTO>가 스트리밍 방식으로 사용하기에는 적절하나, CUBRID DB에서는 사용이 불가
		// JDBC API 기반의 데이터 접근 방식 : Cursor
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// 전체 데이터 count
		searchParam.getPagination().setItemsPerPage(pageSize);
		searchParam.getPagination().setCurrentPage(offset);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// Cursor 를 이용한 스트리밍 방식의 엑셀 다운로드 불가
		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize 		= 1000;
		offset 			= 1;

		searchParam.getPagination().setItemsPerPage(pageSize);
		searchParam.getPagination().setCurrentPage(offset);

		// SXSSF	: window size = 100
		// workbook	: 엑셀생성을 위한 내부 문서 모델
		// 메모리 적재 최대 100 row 로 설정, 나머지는 disk 로 flush
		// disk 저장 파일은 압축
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		// 엑셀 시트 이름 설정
		Sheet sheet = workbook.createSheet("GIVE_STATE_ALL");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "기부금 전체현황 목록");
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 7500);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 5000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);

		sheet.setColumnWidth(10, 7500);
		sheet.setColumnWidth(11, 7500);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(15, 5000);
		sheet.setColumnWidth(16, 5000);
		sheet.setColumnWidth(17, 5000);
		sheet.setColumnWidth(18, 5000);
		sheet.setColumnWidth(19, 5000);

		sheet.setColumnWidth(20, 5000);
		sheet.setColumnWidth(21, 5000);
		sheet.setColumnWidth(22, 5000);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0, "No");
		cellStyle.header(header, 1, "기부번호");
		cellStyle.header(header, 2, "부과일자");
		cellStyle.header(header, 3, "수납일자");
		cellStyle.header(header, 4, "전자납부번호");
		cellStyle.header(header, 5, "기부형태");
		cellStyle.header(header, 6, "기부상태");
		cellStyle.header(header, 7, "국세청");
		cellStyle.header(header, 8, "기부자명");
		cellStyle.header(header, 9, "아이디");

		cellStyle.header(header, 10, "소속지자체");
		cellStyle.header(header, 11, "기부지자체");
		cellStyle.header(header, 12, "특정사업기부 사업명");
		cellStyle.header(header, 13, "기부금액");
		cellStyle.header(header, 14, "발생포인트");
		cellStyle.header(header, 15, "잔액포인트");
		cellStyle.header(header, 16, "답례품여부");
		cellStyle.header(header, 17, "납부유효일자");
		cellStyle.header(header, 18, "민간연계기관");
		cellStyle.header(header, 19, "접수은행/복지센터");

		cellStyle.header(header, 20, "접수자명");
		cellStyle.header(header, 21, "등록일시");
		cellStyle.header(header, 22, "수정일시");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;

			// 1000건 데이터를 엑셀 시트에 입력 가능하도록 row 단위의 전처리 실행
			List<GiveStateTest> giveStateTestList = slaveGiveStateMapper.getGiveStateDetailListTest(searchParam);

			for (GiveStateTest giveStateTest : giveStateTestList) {
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1, giveStateTest.getCntrSn());
				cellStyle.data(row, 2, giveStateTest.getCntrDe());
				cellStyle.data(row, 3, giveStateTest.getSttemntPayDe());
				cellStyle.data(row, 4, giveStateTest.getElctrnPayNo());
				cellStyle.data(row, 5, giveStateTest.getCntrPathCode());
				cellStyle.data(row, 6, giveStateTest.getCntrSttusCode());
				cellStyle.data(row, 7, giveStateTest.getNtsSttusMssage());
				cellStyle.data(row, 8, giveStateTest.getUserName());
				cellStyle.data(row, 9, giveStateTest.getLoginId());

				cellStyle.data(row, 10, giveStateTest.getPsitnLocgovCode());
				cellStyle.data(row, 11, giveStateTest.getCntrLocgovCode());
				cellStyle.data(row, 12, giveStateTest.getPrjId() == 0 ? "자치단체기부" : giveStateTest.getPrjSubject());
				cellStyle.data(row, 13, giveStateTest.getCntrAmt());
				cellStyle.data(row, 14, giveStateTest.getCntrPoint());
				cellStyle.data(row, 15, giveStateTest.getCntrBlcePoint());
				cellStyle.data(row, 16, giveStateTest.getRtnpsntReqstCode());
				cellStyle.data(row, 17, giveStateTest.getPayValidDe());
				cellStyle.data(row, 18, giveStateTest.getDetail());
				cellStyle.data(row, 19, giveStateTest.getRceptBankNm());

				cellStyle.data(row, 20, giveStateTest.getRcepterNm());
				cellStyle.data(row, 21, giveStateTest.getFrstRegistPnttm());
				cellStyle.data(row, 22, giveStateTest.getLastUpdtPnttm());
			}

			// 다음 1000 row 조회를 위한 offset 설정
			// 쿼리문
			// LIMIT (#{pagination.currentPage} - 1) * #{pagination.itemsPerPage}, #{pagination.itemsPerPage}
			searchParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
	}

	 @Override
	 public int getGiveStateDetailListCountCntr(GiveState giveState) {
	     return slaveGiveStateMapper.getGiveStateDetailListCountCntr(giveState);
	 }
	 @Override
	 public List<GiveStateTest> getGiveStateDetailListCntr(GiveState giveState) {
	     return slaveGiveStateMapper.getGiveStateDetailListCntr(giveState);
	 }
	 @Override
	 public List<GiveStateCntrUsePoint> getGiveStateDetailListCntrUsePoint(GiveState giveState) {
	     return slaveGiveStateMapper.getGiveStateDetailListCntrUsePoint(giveState);
	 }

	/**
	 * 기부금변경신청관리 목록 수 조회
	 * @param giveState
	 * @return
	 */
	@Override
	public int getGiveReqmngCount(GiveState giveState) {
		return slaveGiveStateMapper.getGiveReqmngCount(giveState);
	}
	/**
	 * 기부금변경신청관리 목록 조회
	 * @param giveState
	 * @return
	 */
	@Override
	public List<GiveState> getGiveReqmngList(GiveState giveState) {
		return slaveGiveStateMapper.getGiveReqmngList(giveState);
	}

	/**
	 * 기부금변경신청관리 등록
	 * @param giveStateTest
	 * @return
	 */
	@Override
	public HashMap<String,Object> giveReqmngInsert(GiveStateTest giveStateTest) {
		HashMap<String, Object> resultMap = new HashMap<>();
		boolean processChk = true;
		int check = 0;
		try {
			if(giveStateTest.getCntrReqmngCode().equals("100")) {
				check = giveStateMapper.cancelTemPointSet(giveStateTest);
				if(check != 1) {
					resultMap.put("result_code", "FAIL");
					resultMap.put("result_msg", "임시포인트 저장 중 오류발생했습니다.");
					processChk = false;
				}
			}
			if(processChk) {
				giveStateMapper.giveReqmngInsert(giveStateTest);
				resultMap.put("result_code", "SUCCESS");
				resultMap.put("result_msg", "정상 등록 처리 되었습니다.");
			}
		}catch (RuntimeException e) {
			if(giveStateTest.getCntrReqmngCode().equals("100")) {//변경신청 등록 중 오류로 포인트 복구
				giveStateMapper.cancelPointReset(giveStateTest);
			}
			resultMap.put("result_code", "FAIL");
			resultMap.put("result_msg", "에러가 발생하여 등록처리가 되지 않았습니다. 고객센터에 문의 하세요.");
			return resultMap;
		} catch(Exception e) {
			if(giveStateTest.getCntrReqmngCode().equals("100")) {//변경신청 등록 중 오류로 포인트 복구
				giveStateMapper.cancelPointReset(giveStateTest);
			}
			resultMap.put("result_code", "FAIL");
			resultMap.put("result_msg", "에러가 발생하여 등록처리가 되지 않았습니다. 고객센터에 문의 하세요.");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 기부금변경신청관리 승인
	 * @param giveStateTest
	 * @return
	 */
	@Override
	public HashMap<String, Object> giveReqmngApprove(GiveStateTest giveStateTest) {
		HashMap<String, Object> resultMap = new HashMap<>();
		boolean validationChk = true;

		try {
			if(giveStateTest.getElctrnPayNo().equals("")) {
				resultMap.put("result_code", "FAIL");
				resultMap.put("result_msg", "전자납부번호가 존재하지 않습니다.");
				validationChk = false;
			}

			if(giveStateTest.getUserId().equals("")) {
				resultMap.put("result_code", "FAIL");
				resultMap.put("result_msg", "요청신청아이디가 존재하지 않습니다.");
				validationChk = false;
			}

			if(validationChk) {

				// 1. 기부금 변경 유형별 처리
				if(giveStateTest.getCntrReqmngCode().equals("100")) {
			   /*        CntrReqmngCode:100 과오납 처리                */

					//2024-05-21 현재는 과오납 부분에만 처리 추후 등록 및 다른 부분 추가 시 과오납 승인 국민비서 알리미 위쪽으로 위치(310라인) 변경 후 로직 약간 수정
					CntrTaxTempDto cntrTaxTempDto = null;
					cntrTaxTempDto = giveStateMapper.getCntrTaxTemp(giveStateTest);

					if(cntrTaxTempDto != null) { //기부 건 조회 성공
						//cntrSttusCode - 300:과오납 / 200:납부완료 / 100:신고(미수납)
						if("200".equals(cntrTaxTempDto.getCntrSttusCode())) { //납부완료 건을 과오납 처리
							int pointChk = this.getCntrUsePointCheck(giveStateTest.getCntrSn());
							if(pointChk == 0) { //납부 완료 건이면서 기부금 사용이력 없음 - 정상처리 건
								giveStateMapper.giveCancelProcess(giveStateTest);

								//elcr_apl_cd null >> g_cntr_tax_temp 데이터 없음
								if(StringUtils.isNull(cntrTaxTempDto.getElcrAplCd())) {
									cntrTaxTempDto.setElcrAplCd("03");
									giveStateMapper.insertCntrTaxTemp(cntrTaxTempDto);
								}else { //g_cntr_tax_temp 있을 경우 업데이트
									//if(cntrTaxTempDto.getLogCnt() > 0) {
									//	giveStateMapper.deleteCntrTaxTempLog(cntrTaxTempDto);
									//}
									cntrTaxTempDto.setElcrAplCd("03");
									giveStateMapper.updateCntrTaxTemp(cntrTaxTempDto);
								}
							}else {
								resultMap.put("result_code", "FAIL");
								resultMap.put("result_msg", "기부금 사용이력이 존재한 기부입니다.");

								return resultMap;
							}
						}else {
							resultMap.put("result_code", "FAIL");
							resultMap.put("result_msg", "수납처리가 안된 기부입니다.");

							return resultMap;
						}
					}
				}else if(giveStateTest.getCntrReqmngCode().equals("200")){
					// CntrReqmngCode:200 포인트 생성 처리

					giveStateMapper.givePointRenew(giveStateTest);
				}else if(giveStateTest.getCntrReqmngCode().equals("300")) {
					// CntrReqmngCode:300 수납처리(+배치동작처리)

					//서울외 기부인 경우 차세대 수납정보 조회
					if("N".equals(giveStateTest.getSeoulTrgetAt())) {
						int cntCheck = 0;
						cntCheck  = giveStateMapper.getSunapInfo(giveStateTest.getCntrSn());

						//차세대 수납정보 없으면 수납정보 등록
						if(cntCheck  == 0 ) {
							giveStateMapper.insertGiveSunapInfo(giveStateTest);
						}
					}
					giveStateMapper.sunapUpdateY(giveStateTest);
				}else if(giveStateTest.getCntrReqmngCode().equals("400")) {
					// CntrReqmngCode:400 수납취소(시스템 관리자)

					giveStateMapper.sunapCancel(giveStateTest);
				}


				// 2. 기부금 변경신청 승인
				giveStateMapper.giveReqmngApprove(giveStateTest);

				//과오납 승인 국민비서 알리미
				if (giveStateTest.getCntrReqmngCode().equals("100")) {
					GiveUserSmsInfo info = slaveGiveStateMapper.giveReqmngSmsUserInfo(giveStateTest);
					smsIpsService.giveSendSms(Arrays.asList(info), SmsType.OVERPAYMENT);
				}

				resultMap.put("result_code", "SUCCESS");
				resultMap.put("result_msg", "정상 승인 처리 되었습니다.");
			}

		} catch(RuntimeException e ) {
			resultMap.put("result_code", "FAIL");
			resultMap.put("result_msg", "에러가 발생하여 승인처리가 되지 않았습니다. 고객센터에 문의 하세요.");
			return resultMap;
		} catch (Exception e) {
			resultMap.put("result_code", "FAIL");
			resultMap.put("result_msg", "에러가 발생하여 승인처리가 되지 않았습니다. 고객센터에 문의 하세요.");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 기부금변경신청관리 취소
	 * @param giveStateTest
	 * @return
	 */
	@Override
	public HashMap<String,Object> giveReqmngCancel(GiveStateTest giveStateTest) {
		HashMap<String, Object> resultMap = new HashMap<>();
		boolean processChk = true;
		int check = 0;
		try {
			if(giveStateTest.getCntrReqmngCode().equals("100")) {
				check = giveStateMapper.cancelPointReset(giveStateTest);
					if(check != 1) {
						resultMap.put("result_code", "FAIL");
						resultMap.put("result_msg", "임시포인트 복구 중 오류발생했습니다.");
						processChk = false;
						check = 0;
					}
				}
			if(processChk) {
				check = giveStateMapper.giveReqmngCancel(giveStateTest);
					resultMap.put("result_code", "SUCCESS");
					resultMap.put("result_msg", "정상 취소 처리 되었습니다.");
				}
			} catch (RuntimeException e) {
				if(giveStateTest.getCntrReqmngCode().equals("100")) {//변경신청 등록 중 오류로 포인트 복구
					giveStateMapper.cancelTemPointSet(giveStateTest);
				}
				resultMap.put("result_code", "FAIL");
				resultMap.put("result_msg", "에러가 발생하여 취소처리가 되지 않았습니다. 고객센터에 문의 하세요.");
				return resultMap;
			} catch(Exception e) {
				if(giveStateTest.getCntrReqmngCode().equals("100")) {//변경신청 등록 중 오류로 포인트 복구
					giveStateMapper.cancelTemPointSet(giveStateTest);
				}
				resultMap.put("result_code", "FAIL");
				resultMap.put("result_msg", "에러가 발생하여 취소처리가 되지 않았습니다. 고객센터에 문의 하세요.");
				return resultMap;
			}
		return resultMap;
	}

	/**
	 * 기부금 변경신청 관련 포인트사용 체크
	 * @param cntrSn
	 * @return
	 */
	@Override
	public int getCntrUsePointCheck(String cntrSn) {
		return slaveGiveStateMapper.getCntrUsePointCheck(cntrSn);
	}

	/**
	 * 기부금 영수증 이력
	 * @param elctrnPayNo
	 * @return List<GiveStateNts>
	 */
	@Override
	public List<GiveStateNts> getNtsList(String elctrnPayNo) {
		return slaveGiveStateMapper.getNtsList(elctrnPayNo);
	}

	/**
	 * 기부금 모금현황 목록 엑셀 다운로드
	 */
	@Override
	public SXSSFWorkbook streamGiveStateList(GiveState giveState, boolean isLocgov) {
		int pageSize	= 0;
		int offset		= 0;
		int rowNum		= 0;
		int limit		= 0;

		giveState.getPagination().setItemsPerPage(pageSize);
		giveState.getPagination().setCurrentPage(offset);

		int totalCount = slaveGiveStateMapper.getGiveStateListCount(giveState);

		limit = (int) Math.ceil((double) totalCount / 1000);

		pageSize 		= 1000;
		offset 			= 1;

		giveState.getPagination().setItemsPerPage(pageSize);
		giveState.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("GIVE_STATE_LIST");

		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "기부금 모금 현황");
		Integer lastColIndex;
		if (isLocgov) {
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No.");
			cellStyle.header(header, 1, "년도");
			cellStyle.header(header, 2, "기부모금액");
			cellStyle.header(header, 3, "기부인원");
			cellStyle.header(header, 4, "발생포인트");
			cellStyle.header(header, 5, "답례품 금액");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 5000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No.");
			cellStyle.header(header, 1, "년도");
			cellStyle.header(header, 2, "광역지자체");
			cellStyle.header(header, 3, "기부지자체");
			cellStyle.header(header, 4, "기부모금액");
			cellStyle.header(header, 5, "기부건수");
			cellStyle.header(header, 6, "기부인원");
			cellStyle.header(header, 7, "발생포인트");
			cellStyle.header(header, 8, "답례품 금액");

			lastColIndex = header.getLastCellNum() - 1;
		}
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		boolean exceedTotalRow = false;

		while (!exceedTotalRow) {

			List<GiveState> giveStateList = slaveGiveStateMapper.getGiveStateList(giveState);

			if (giveStateList.isEmpty()) break;

			for (GiveState giveStateItem : giveStateList) {
				Row row = sheet.createRow(rowNum++);
				int cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, cellCount++, String.valueOf(giveStateItem.getCntrYear()));
				if (!isLocgov) {
					cellStyle.data(row, cellCount++, giveStateItem.getUpperLocgovNm());
					cellStyle.data(row, cellCount++, giveStateItem.getLocgovNm());
				}
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(giveStateItem.getCntrAmt()));
				if (!isLocgov) {
					cellStyle.data(row, cellCount++, StringUtils.numberFormat(giveStateItem.getGiveCnt()));
				}
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(giveStateItem.getGivePersons()));
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(giveStateItem.getCntrPoint()));
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(giveStateItem.getCntrUsePoint()));
			}

			giveState.getPagination().setCurrentPage(++offset);

			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}
		return workbook;
	}
}

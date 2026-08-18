package saleson.shop.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.StringUtils;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.log.ManagerHistService;
import saleson.shop.slave.SlavePersonInChargeMapper;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PersonInChargeEncryptor;
import saleson.shop.user.domain.PersonInChargeResult;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.PersonInChargeSearchParam;

import com.onlinepowers.framework.security.userdetails.User;

@Slf4j
@RequiredArgsConstructor
@Service("chargerService")
public class PersonInChargeServiceImpl extends EgovAbstractServiceImpl implements PersonInChargeService {

	private static final Logger logger = LoggerFactory.getLogger(PersonInChargeServiceImpl.class);

	private final PersonInChargeMapper personInChargeMapper;
	private final SlavePersonInChargeMapper slavePersonInChargeMapper;
	private final PersonInChargeEncryptor personInChargeEncryptor;
	private final UserService userService;
	private final SecurityService securityService;
	private final DataMasking dataMasking;

	private final ManagerHistService managerHistService;

	@Autowired
	private final UserMapper userMapper;

	/**
	 * 담당자 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getChargerCountByParam(PersonInChargeSearchParam searchParam) {
		return slavePersonInChargeMapper.getChargerCountByParam(searchParam);
	}

	/**
	 * 담당자 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<PersonInCharge> getChargerListByParam(PersonInChargeSearchParam searchParam) {
		return slavePersonInChargeMapper.getChargerListByParam(searchParam);
	}

	/**
	 * 담당자 목록 엑셀 다운로드
	 * @param searchParam
	 * @return
	 */
	@Override
	public SXSSFWorkbook streamChargerData(PersonInChargeSearchParam searchParam, int totalChargerCount) throws Exception {
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;
		int totalCount 	= totalChargerCount;

		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize 		= 1000;
		offset 			= 1;

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

		// 엑셀 시트 이름 설정
		Sheet sheet = workbook.createSheet("CHARGER_DATA");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "오프라인 회원 목록");
		// 생성 컬럼 수에 따른 셀 병합을 위한 변수
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 5000);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0, "No");
		cellStyle.header(header, 1, "구분");
		cellStyle.header(header, 2, "소속 지점");
		cellStyle.header(header, 3, "아이디");
		cellStyle.header(header, 4, "이름");
		cellStyle.header(header, 5, "개인번호");
		cellStyle.header(header, 6, "사용여부");
		cellStyle.header(header, 7, "중지일자");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while(!exceedTotalRow) {
			if (totalCount == 0) break;

			List<PersonInCharge> chargerList = slavePersonInChargeMapper.getChargerListByParam(searchParam);

			if (chargerList.isEmpty()) break;

			for(PersonInCharge personInCharger : chargerList) {
				// 소속 지점 데이터 설정
				String branchName = "-";
				if (personInCharger.getBankNm() != null) {
					branchName =  ShopUtils.unescapeHtml(personInCharger.getBankNm());
					if (personInCharger.getPsitnNm() != null) {
						branchName += " " +  ShopUtils.unescapeHtml(personInCharger.getPsitnNm());
					}
				}
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1, "ROLE_ADMIN_7".equals(personInCharger.getAuthority()) ? "주관리자" : "-");
				cellStyle.data(row, 2, branchName);
				cellStyle.data(row, 3, personInCharger.getLoginId());
				cellStyle.data(row, 4, personInCharger.getUserName());
				cellStyle.data(row, 5, personInCharger.getEmpId());
				cellStyle.data(row, 6, personInCharger.getStatusCode() == 2 ? "중지" : "사용");
				cellStyle.data(row, 7, personInCharger.getStatusCode() == 2 ? personInCharger.getDenyDate() : "-");
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
	 * 로그인 사용자 관리자 권한 조회
	 * @return
	 */
	@Override
	public String getLoginUserAdminAuthority() {
		String adminRole = "";

		// 로그인 사용자 권한 목록 조회
		List<UserRole> userRoleList = UserUtils.getUser().getUserRoles();

		// 로그인 사용자 관리자 권한 체크 (시스템/행안부/지자체/오프라인)
		if(userRoleList != null) {
			for(UserRole userRole : userRoleList) {
				String auth = userRole.getAuthority();
				if(auth != null && auth.indexOf("ROLE_ADMIN_") > -1) {
					adminRole = auth;
					break;
				};
			};
		};

		return adminRole;
	}

	/**
	 * 담당자 삭제
	 * @param charger
	 * @return
	 */
	@Override
	public PersonInChargeResult deleteCharger(PersonInCharge charger) throws RuntimeException {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		// 0. 유효성 검사
		if(charger == null || charger.getUserIdList() == null || charger.getUserIdList().size() == 0) {
			result.setCode("ERR_VALID");
			return result;
		}

		// 1. 관리자 비밀번호 변경 이력 삭제
		personInChargeMapper.deleteManagerPasswordLog(charger);

		// 2. 관리자 삭제
		int nResult = personInChargeMapper.deleteCharger(charger);

		// 3. 관리자 권한 삭제
		if(nResult > 0) {
			personInChargeMapper.deleteChargerRole(charger);
		}
		// 4. 지점 연락처 삭제
		/*
		 * if(nResult > 0) { personInChargeMapper.deleteChargerTelNumber(charger); }
		 */

		// 5. 결과값
		if(nResult > 0) {
			result.setCode("SUCC");
		}

		return result;
	}

	/**
	 * 담당자 상세 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public PersonInCharge getChargerDetails(PersonInChargeSearchParam searchParam) {

		PersonInCharge detail = slavePersonInChargeMapper.getChargerDetails(searchParam);
//		detail.setPhoneNumber(dataMasking.mask(detail.getPhoneNumber(), Masking.GH_PHONE_NUMBER));

		//op_user_detail에서 지점연락처 가져오기
//		String telNumber = personInChargeMapper.getUserOffTelNumber(searchParam.getUserId());//오프라인담당자용
//		if(!StringUtils.isNotEmpty(telNumber)) {
//			detail.setTelNumber("");
//		} else {
//			detail.setTelNumber(dataMasking.mask(telNumber, Masking.GH_PHONE_NUMBER));
//		}

		return detail;
	}

	/**
	 * 하위 지자체 정보 조회
	 * @param upperLocgovCode
	 * @return
	 */
	@Override
	public List<Locgov> getLocgovList(String upperLocgovCode) {
		return slavePersonInChargeMapper.getLocgovList(upperLocgovCode);
	}

	/**
	 * 지자체 주담당자 인원수 확인
	 * @param personInCharge
	 * @return
	 */
	@Override
	public int getLocgovMainPersonInChargeCount(PersonInCharge personInCharge) {
		return slavePersonInChargeMapper.getLocgovMainPersonInChargeCount(personInCharge);
	}

	/**
	 * 담당자 정보 수정
	 * @param personInCharge
	 * @return
	 */
	@Override
	public PersonInChargeResult updatePersonInCharge(PersonInCharge personInCharge) throws RuntimeException {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		if(personInCharge == null || personInCharge.getUserId() == null || "".equals(String.valueOf(personInCharge.getUserId()))) {
			result.setCode("ERR_VALID");
			return result;
		}

		// 담당자 정보 수정
		if(personInChargeMapper.updatePersonInCharge(personInCharge) > 0) {
			personInChargeMapper.deletePersonInChargeUserRole(personInCharge);
			personInChargeMapper.insertPersonInChargeUserRole(personInCharge);
			// [GGSR-26-369] 이력저장
			managerHistService.insertManagerHist(personInCharge, "H");
			result.setCode("SUCC");
		}

		return result;
	}

	/**
	 * 행안부 주담당자 인원수 확인
	 * @param personInCharge
	 * @return
	 */
	@Override
	public int getGovMainPersonInChargeCount(PersonInCharge personInCharge) {
		return slavePersonInChargeMapper.getGovMainPersonInChargeCount(personInCharge);
	}

	/**
	 * 오프라인 담당자관리 목록 > 상태코드 수정 (사용, 중지)
	 * @param personInCharge
	 * @return
	 */
	@Override
	public int updateStatusCode(PersonInCharge personInCharge) {
		int rslt = personInChargeMapper.updateStatusCode(personInCharge);
		// [GGSR-26-369] 이력저장
		managerHistService.insertManagerHist(personInCharge, "H");
		return rslt;
	}

	/**
	 * 오프라인 주담당자 인원수 확인
	 * @param personInCharge
	 * @return
	 */
	@Override
	public int getOffPersonInChargeMainCount(PersonInCharge personInCharge) {
		return personInChargeMapper.getOffPersonInChargeMainCount(personInCharge);
	}

	/**
	 * 오프라인담당자 등록 처리
	 * @param personInCharge
	 * @return
	 */
	@Override
	public PersonInChargeResult insertOffPersonInCharge(PersonInCharge personInCharge) throws RuntimeException {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		User user = new User();
		UserDetail userDetail = new UserDetail();
		UserRole userRole = new UserRole();

		// password가 없는 경우 기본 값 세팅 (농협아이디발급.xlsx 파일 참고)
		if(personInCharge.getPassword() == null || personInCharge.getPassword().isBlank()) {
			personInCharge.setPassword("nacf1234");
		}

		// email 값이 없는 경우 소속 지점에 따라 직위 명에 기본 값 세팅 (농협아이디발급.xlsx 파일 참고)
//		if(personInCharge.getEmail() == null | personInCharge.getEmail().isBlank()) {
			if(("011").equals(personInCharge.getBankCode())) {
				personInCharge.setOfcpsNm("농협");
			}else if(("012").equals(personInCharge.getBankCode())) {
				personInCharge.setOfcpsNm("농축협");
			}else {
				personInCharge.setOfcpsNm("제주은행");
			}
//		}

		// 0. selectNewUserId create
		long userId = userMapper.selectNewUserId();
		user.setUserId(userId);
		user.setStatusCode("9");

		user.setLoginId(personInCharge.getLoginId());
        user.setPassword(personInCharge.getPassword());
        user.setUserName(personInCharge.getUserName());
        user.setEmail(personInCharge.getEmail());
        user.setUserDetail(userDetail);
        user.setPhoneNumber(personInCharge.getPhoneNumber());


		// 1. insertUser
		userService.insertUser(user);

		// 2. insertUserDetail
		userDetail.setUserId(userId);
		userService.insertUserDetail(userDetail);

		// 3. inserUserRole
		String[] roles = {"ROLE_ADMIN_8", "ROLE_OPMANAGER", "ROLE_USER"};

		for(String role : roles) {
			userRole.setUserId(userId);
			userRole.setAuthority(role);
			userService.insertUserRole(userRole);
		}

		// 4. insertManeger
		user.setStatusCode(String.valueOf(personInCharge.getStatusCode()));
		userService.insertChargerManager(user, personInCharge);

		// 5. result code setting
		result.setCode("SUCC");

		return result;
	}

	/**
	 * 오프라인담당자 수정 처리
	 * @param personInCharge
	 * @return
	 */
	@Override
	public PersonInChargeResult updateOffPersonInCharge(PersonInCharge personInCharge) throws RuntimeException {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		if(personInCharge == null || personInCharge.getUserId() == null || "".equals(String.valueOf(personInCharge.getUserId()))) {
			result.setCode("ERR_VALID");
			return result;
		}

		// 20260127. 이현민부장님 요청(updateOffPersonInCharge, updateUserSync 쿼리 수정)
		// 오프라인 담당자 email은 추후 op_user.email, op_manager.email, op_manger.ofcps_nm 3개 컬럼에 update함
		// 사유: srhan. 20251029. 이메일 인증 추가하며 '직책' 입력 UI를 '이메일'로 변경 작업
		// 1. 오프라인 담당자 수정
		personInCharge.encrypt(personInChargeEncryptor);
		if(personInChargeMapper.updateOffPersonInCharge(personInCharge) > 0) {

			// 2. 회원 정보 수정 (이름)
			personInChargeMapper.updateUserSync(personInCharge.getUserId());

			// 3. 회원 상세 정보 수정 (휴대폰 번호)
			personInChargeMapper.updateUserDetailSync(personInCharge.getUserId());

			// 4. 관리자 권한 삭제, 등록 (ROLE_ADMIN_*)
			personInChargeMapper.deletePersonInChargeUserRole(personInCharge);
			personInChargeMapper.insertPersonInChargeUserRole(personInCharge);

			// [GGSR-26-369] 이력저장
			managerHistService.insertManagerHist(personInCharge, "H");

			// 5. 결과값 셋팅
			result.setCode("SUCC");
		}

		return result;
	}

	/**
	 * 임시 비밀번호 발급
	 * @param charger
	 * @return
	 */
	@Override
	public String updatePasswordInit(PersonInCharge charger) {
		String adminRole = this.getLoginUserAdminAuthority();
		String tempPassword = "";
		String encPassword = "";

		// [유효성] 페이지 접근한 사람이 시스템 주/부관리자, 행안부 주/부관리자, 오프라인 주관리자여야함.
		if("ROLE_ADMIN_5".equals(adminRole) || "ROLE_ADMIN_6".equals(adminRole) || "ROLE_ADMIN_8".equals(adminRole)) {
			return tempPassword;
		}

		// [유효성] 비밀번호 변경하려는 대상이 오프라인 담당자여야 함.
		String targetUserRole = slavePersonInChargeMapper.getUserAdminRole(charger.getUserId());
		if(!("ROLE_ADMIN_7".equals(targetUserRole) || "ROLE_ADMIN_8".equals(targetUserRole))) {
			return tempPassword;
		}

		// 임시 비밀번호 발급 및 암호화 적용
		tempPassword = RandomStringUtils.getRandomString("", 4, 8);
		try {
			encPassword = pCrypto.Encrypt("hash.5", tempPassword, "");
		} catch (UnsupportedEncodingException e) {
			log.error("PersonInChargerServiceImpl :: updatePasswordInit");
		}

		// 파라미터 셋팅
		charger.setPassword(encPassword);
		charger.setPasswordExpiredDate(userService.getPasswordExpiredDate());

		// 회원, 관리자 비밀번호 수정
		personInChargeMapper.updateUserPassword(charger);
		personInChargeMapper.updateManagerPassword(charger);

		// 회원, 관리자 로그인 실패 갯수 초기화
		String loginId = CommonUtils.dataNvl(slavePersonInChargeMapper.getLoginIdByUserId(charger.getUserId()));
		if(!"".equals(loginId)) {
			securityService.updateClearLoginFailCountForManager(loginId);
			securityService.updateClearLoginFailCountForUser(loginId);
		}

		return tempPassword;
	}

	/**
	 * 관리자 정보갱신 여부 - Y/N
	 * @param userId
	 * @return
	 */
	@Override
	public String getOffPersonInChargeInfoUpdateFlag() {
		String infoUpdtFlag = "Y";

		// 정보 갱신 여부 확인
		if("".equals(slavePersonInChargeMapper.getManagerInfoUpdateDate(UserUtils.getUser().getUserId()))) {
			infoUpdtFlag = "N";
		}

		return infoUpdtFlag;
	}

	@Override
	public int getLocgovChargerCountByParam(PersonInChargeSearchParam searchParam) {
		// TODO Auto-generated method stub
		return slavePersonInChargeMapper.getLocgovChargerCountByParam(searchParam);
	}

	@Override
	public List<PersonInCharge> getLocgovChargerListByParam(PersonInChargeSearchParam searchParam) {
		// TODO Auto-generated method stub
		List<PersonInCharge> list = slavePersonInChargeMapper.getLocgovChargerListByParam(searchParam);
		try {
			for(PersonInCharge vo : list) {
				vo.setPhoneNumber(dataMasking.mask(vo.getPhoneNumber(), Masking.GH_PHONE_NUMBER));
			}
		} catch(NullPointerException e) {
			log.error(e.getMessage());
		}

		return list;
	}
}

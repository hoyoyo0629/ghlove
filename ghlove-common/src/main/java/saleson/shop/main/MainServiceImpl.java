package saleson.shop.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;

import lombok.RequiredArgsConstructor;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.opmanager.count.OpmanagerMainCount;
import saleson.common.utils.LocalDateUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.give.givestate.domain.CallState;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.main.domain.MainChart;
import saleson.shop.offgive.OffgiveService;
import saleson.shop.offgive.domain.Manager;
import saleson.shop.slave.SlaveMainMapper;
import saleson.shop.user.LocgovService;

@RequiredArgsConstructor
@Service("mainService")
public class MainServiceImpl extends EgovAbstractServiceImpl implements MainService {
	private static final Logger log = LoggerFactory.getLogger(MainServiceImpl.class);

	private final MainMapper mainMapper;

	private final SlaveMainMapper slaveMainMapper;

	/** 지자체관리 Service */
	@Autowired
	private LocgovService locgovService;

	@Autowired
	private OffgiveService offgiveService;

	private static final Map<String, Object> cache = new ConcurrentHashMap<>();

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${NetFunnel.env}")
	private String env;

	/**
	 * 관리자 지자체코드
	 */
	@Override
	public String getLocgovCdByUerId(long userId) {

		return mainMapper.getLocgovCdByUerId(userId);
	}

	/**
	 * 관리자 메인 전체회원/금일가입회원/관리자권한요청(대기)/오프라인접수현황/총기부현황/총답례품현황 카운트
	 */
	@Override
	public List<OpmanagerMainCount> getOpmanagerMainInfo(String locgovCd) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		Map<String, String> param = new HashMap<>();
		param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
		param.put("previousYearDate", String.valueOf(LocalDate.now().getYear() - 1) + "1231235959"); // 20250401 조형원추가 : 이번년도 체크 Param
		param.put("locgovCd", locgovCd);
		param.put("userRole", adminRole);


		// 20221205 추가
		User user = UserUtils.getUser();
		Manager manager = offgiveService.getManager(UserUtils.getUser().getUserId());
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_7".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_11".equals(userRole.getAuthority())) {
				param.put("shRceptBankCode", manager.getBankCode());
				param.put("shRceptBankNm", "");
				break;
			} else if ("ROLE_ADMIN_8".equals(userRole.getAuthority())) {
				param.put("shRceptBankCode", manager.getBankCode());
				param.put("shRceptBankNm", manager.getPsitnNm());
				break;
			} if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority()) || "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
				param.put("shRceptBankCode", "");
				param.put("shRceptBankNm", "");
				break;
			} else {
				param.put("shRceptBankCode", "");
				param.put("shRceptBankNm", "");
				break;
			}
		}

		//return mainMapper.getOpmanagerMainInfo(param);
		return slaveMainMapper.getOpmanagerMainInfo(param);
	}

	/**
	 * 관리자 메인 게시판 정보
	 */
	@Override
	public List<OpmanagerCount> getOpmanagerMainBoardInfo() {

		Map<String, String> param = new HashMap<>();
		param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));

//		return mainMapper.getOpmanagerMainBoardInfo(param);
		return slaveMainMapper.getOpmanagerMainBoardInfo(param);
	}

	/**
	 * 관리자 메인 기부금액 chart
	 */
	@Override
	public List<MainChart> getOpmanagerMainAmountChart(String locgovCd) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		Map<String, String> param = new HashMap<>();
		param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
		param.put("locgovCd", locgovCd);
		param.put("userRole", adminRole);

//		return mainMapper.getOpmanagerMainAmountChart(param);
		return slaveMainMapper.getOpmanagerMainAmountChart(param);
	}

	/**
	 * 관리자 메인 기부건수 chart
	 */
	@Override
	public List<MainChart> getOpmanagerMainCountChart(String locgovCd) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		Map<String, String> param = new HashMap<>();
		param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
		param.put("locgovCd", locgovCd);
		param.put("userRole", adminRole);

//		return mainMapper.getOpmanagerMainCountChart(param);
		return slaveMainMapper.getOpmanagerMainCountChart(param);
	}

	/**
	 * 관리자 메인 chart 정보
	 */
	@Override
	public List<OpmanagerCount> getOpmanagerMainChartInfo(String locgovCd) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		Map<String, String> param = new HashMap<>();
		param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
		param.put("locgovCd", locgovCd);
		param.put("userRole", adminRole);

//		return mainMapper.getOpmanagerMainChartInfo(param);
		return slaveMainMapper.getOpmanagerMainChartInfo(param);
	}

	/**
	 * 관리자 메인 chart 정보
	 * 2023-02-21 추가
	 */
	@Override
	public List<OpmanagerMainCount> getOpmanagerMainTableInfo(GiveState giveState) {
		return mainMapper.getOpmanagerMainTableInfo(giveState);
	}

	@Override
	public CallState getOpmanagerMainCallTableInfo(GiveState giveState) {

		CallState callState = mainMapper.getOpmanagerMainCallTableInfo(giveState);

		if(callState == null) {
			callState = new CallState();
			callState.setCallKookmin(0);
			callState.setCallLov(0);
			callState.setCallGiver(0);
			callState.setCallNhbank(0);
			callState.setCallTotal(0);		}

		return callState;
	}
	/**
	 *
	 * <pre>
	 * comment       : 콜 등록
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 8. 4.
	 *
	 * </pre>
	 * @param giveState
	 * @return
	 * int
	 */
	@Override
	public int opmanagerInsertCall(GiveState giveState) {
		mainMapper.opmanagerdeleteCall(giveState);
		return mainMapper.opmanagerMainCall(giveState);
	}
   /**
    *
    * <pre>
    * comment       : 답례품 지자체
    * preMethodName :
    * author        : csh
    * date          : 2023. 8. 4.
    *
    * </pre>
    * @return
    * List<String>
    */
	@Override
	public List<String> opamanagerMainGift() {

//		return mainMapper.opamanagerMainGift();
		return slaveMainMapper.opamanagerMainGift();
	}
	/**
	 *
	 * <pre>
	 * comment       : 답례품 (판매, 품절) 수
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 8. 4.
	 *
	 * </pre>
	 * @return
	 * int
	 */
	@Override
	public long opmanageMainAmountGift(GiveState giveState) {
	 long giftB = mainMapper.opmanageMainAmountGiftB(giveState);
	 long giftA = mainMapper.opmanageMainAmountGiftA(giveState);
	 long giftC = giftA + giftB;
	 return giftC;

	}

	@Override
	public int getUserDashboard(GiveState giveState) {
		return mainMapper.getUserDashboard(giveState);
	}

	@Override
	public CallState dashBoardReport(GiveState giveState) {
//		CallState dashBoard = mainMapper.dashBoardReport(giveState);
		CallState dashBoard = slaveMainMapper.dashBoardReport(giveState);
		if (dashBoard == null) {
			dashBoard = new CallState();
		}
		return dashBoard;
	}

	/**
	 * @return Map
	 * */
	@Override
	public Long getGiveTotalAmt(String std) {
		// 기부금이 없을 경우 NPE 발생 방지
		Long totalAmount = mainMapper.getGiveTotalAmt(std);
		if (totalAmount == null) {
			totalAmount = 0L;
		}
		return totalAmount;
	}


	/**
	 * <pre>
	 * comment       : 오늘을 기준으로 작년, 올해 총기부금 조회
	 * date          : 2025. 09. 02.
	 * </pre>
	 * void
	 */
	@Override
	public Map<String, Object> nowDayGramt() {
		return mainMapper.nowDayGramt();
	}

	@Override
	public Map<String, Object> getGiveStateByCache() {
		LocalDate now = LocalDate.now();

		// 서버 실행 후 처음 호출 되거나, cacheDate가 서버 시간과 동일하지 않으면(낲짜가 바뀌면) DB 접근
		if(cache.get("cacheDate") == null || !cache.get("cacheDate").equals(now)) {
			// 오늘 날짜 캐쉬에 추가
			cache.put("cacheDate", now);

			// [금년 1월 1일 ~ 현재 -1일] 올해 총 기부금 조회
			cache.put("nowYearTotalAmt", getGiveTotalAmt("now"));
			// [작년 1월 1일 ~ 현재 -1년 -1일] 작년 총 기부금 조회
			cache.put("prevYearTotalAmt", getGiveTotalAmt("prev"));

			// 전일 : 현재 - 1일
			cache.put("stdDay", now.minusDays(1).toString().replace("-", "."));

			// D-Day : 올해까지 남은 날짜
			Long dDay = ChronoUnit.DAYS.between(now, LocalDate.of(now.getYear(), 12, 31));
			cache.put("dDay", dDay);

			// nowDayPercent : 오늘까지 경과 퍼센테이지 (1일 ~ 현재 / 1년)
			boolean isLeap = Year.isLeap(Year.now().getValue()); // 윤년 확인
			int totalDay = isLeap ? 366 : 365;
			Long nowDayPercent = (long)(((double)totalDay - dDay) / totalDay * 100);
			cache.put("nowDayPercent", nowDayPercent);

			//1월1일일 경우 예외처리
			if ( 	(dDay == 365 && isLeap == true)
				|| 	(dDay == 364 && isLeap == false)	) {
				cache.put("nowDayPercent", 100);
				cache.put("dDay", -1);
			}
		}

		return cache;
	}

	// jobServiceImpl에서 실행시킬 배치
	@Override
	public void updateWaitUser() {
		// NetFunnel API 호출
		JsonNode node = requestNetFunnelApi();

		// ServerCache Insert NetFunnel Response
		cache.put("actionList", node.path("action_list"));
	}

	@Override
	public Map<String, Object> getRealTimeWaitUser() {
		// 서버 최초 실행 시 actionList가 null이면 updateWaitUser 실행
		if(cache.get("actionList") == null) {
			updateWaitUser();
		}

		// actionList 값이 있다면, cache 리턴
		return cache;
	}

	private JsonNode requestNetFunnelApi(){
		JsonNode root = null;
		try {
			// NetFunnel Api Call
			ObjectMapper mapper = new ObjectMapper();
			if(env.equals("dev") || env.equals("production")) {
				URL url = new URL("http://192.168.239.21:80/getinfo.nfl?select=service_info:statct=service_info:stat^true:recursive^true");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/json");

				root = mapper.readTree(conn.getInputStream());
				log.info("■■■■■■■■ NetFunnel Api Execute - Dev or Prod ■■■■■■■■");
			} else if(env.equals("local")) { // 로컬 테스트 [ 경로 : ghlove-common/src/main/resource/sample.json ]
				ClassPathResource resource = new ClassPathResource("sample.json");
				root = mapper.readTree(resource.getInputStream());
				log.info("■■■■■■■■ NetFunnel Api Execute - local ■■■■■■■■");
			}
		} catch (Exception e) {
			log.error("■■■■■■■■ NetFunnel Api Error■■■■■■■■ : {}", e);
		}

		return root.path("service_info").path("data").path("service_1");
	}

	/**
	 *
	 * 2026. 1. 21
	 * 관리자 메인 화면
	 * 알림설정 동의 여부 멤버 카운트
	 *
	 */
	@Override
	public Map<String, Object> notificationAgreeMemberCount() {
		return slaveMainMapper.notificationAgreeMemberCount();
	}
}

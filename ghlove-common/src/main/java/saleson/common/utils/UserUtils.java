package saleson.common.utils;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onlinepowers.framework.common.LoginRequest;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;

import saleson.common.enumeration.AuthorityType;
import saleson.seller.main.domain.Seller;
import saleson.shop.user.domain.GuestUser;
import saleson.shop.user.domain.UserDetail;

public class UserUtils {

	private static final Logger log = LoggerFactory.getLogger(UserUtils.class);

	private UserUtils() {}

	/**
	 * 회원(고객) 로그인 여부
	 * @return
	 */
	public static boolean isUserLogin() {
		return SecurityUtils.hasRole("ROLE_USER");
	}


	public static boolean isGuestLogin() {
		try {
			HttpSession session = RequestContextUtils.getSession();

			if(session.getAttribute("guestDomain") != null) {
				return true;
			}

			HttpServletRequest request = RequestContextUtils.getRequestContext().getRequest();
			if (request != null) {
				GuestUser guestUser = JwtUtils.getGuestUser(request);

				if (guestUser != null) {
					return true;
				}

			}

		} catch (OpRuntimeException e) {
			return false;
		}

		return false;
	}

	/**
	 * 시스템관리자 여부 (ROLE_SUPERVISOR)
	 * @return
	 */
	public static boolean isSupervisor() {
		return SecurityUtils.isSupervisor();
	}

	/**
	 * 관리자 로그인 여부 (ROLE_OPMANAGER)
	 * @return
	 */
	public static boolean isManagerLogin() {
		if (SecurityUtils.hasRole("ROLE_OPMANAGER")) {
			return true;
		}
		return false;
	}

	/**
	 * MD 여부 (ROLE_MD)
	 * @author minae.yun
	 * @return
	 */
	public static boolean isMd() {
		if (SecurityUtils.hasRole("ROLE_MD")) {
			return true;
		}
		return false;
	}

	/**
	 * 로그인 회원 정보
	 * @return
	 */
	public static User getUser() {
		return SecurityUtils.getCurrentUser();
	}

	/**
	 * 로그인 회원 USER_ID
	 * @return
	 */
	public static long getUserId() {
		if (getUser() == null) {
			return 0;
		}

		if (!isUserLogin()) {
			return 0;
		}

		return getUser().getUserId();
	}

	/**
	 * 로그인 관리자 USER_ID
	 * 판매자 페이지이고 판매자 로그인 된 경우는 판매자 아이디를 리턴.
	 * @return
	 */
	public static long getManagerId() {
		if (ShopUtils.isSellerPage() && SellerUtils.isSellerLogin()) {
			return SellerUtils.getSellerId();
		}
		if (getUser() == null) {
			return 0;
		}

		return getUser().getUserId();
	}

	/**
	 * 로그인 관리자 USER_ID
	 * 판매자 페이지이고 판매자 로그인 된 경우는 판매자 아이디를 리턴.
	 * @return
	 */
	public static String getManagerName() {
		if (ShopUtils.isSellerPage() && SellerUtils.isSellerLogin()) {
			if (SellerUtils.getSeller() != null) {
				return SellerUtils.getSeller().getSellerName();
			}
		}

		if (getUser() == null) {
			return "";
		}

		return getUser().getUserName();
	}

	/**
	 * 회윈의 이메일 정보를 가져온다.
	 * @return
	 */
	public static String getEmail() {
		if (getUser() == null) {
			return "";
		}
		return getUser().getEmail();
	}


	/**
	 * 로그인 회원의 LOGIN_ID
	 * @return
	 */
	public static String getLoginId() {
		return getUser().getLoginId();
	}




	/**
	 * 회원 상세 정보
	 * @return
	 */
	public static UserDetail getUserDetail() {
		if (getUser() != null && getUser().getUserDetail() != null) {
			return (UserDetail) getUser().getUserDetail();
		} else {
			return new UserDetail();
		}
	}


	public static void setGuestLogin(String username, String phoneNumber) {

		User user = new User();
		UserDetail userDetail = new UserDetail();

		user.setUserName(username);
		userDetail.setPhoneNumber(phoneNumber);

		user.setUserDetail(userDetail);

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("guestDomain", user);

	}

	public static User getGuestLogin() {
		HttpSession session = RequestContextUtils.getSession();

		if (session != null && session.getAttribute("guestDomain") != null) {
			return (User) session.getAttribute("guestDomain");
		}

		HttpServletRequest request = RequestContextUtils.getRequestContext().getRequest();
		if (request != null) {

			try {
				GuestUser guestUser = JwtUtils.getGuestUser(request);

				if (guestUser != null) {
					User user = new User();
					UserDetail userDetail = new UserDetail();

					user.setUserName(guestUser.getUserName());
					userDetail.setPhoneNumber(guestUser.getPhoneNumber());

					user.setUserDetail(userDetail);

					return user;
				}


			} catch (OpRuntimeException ignore) {
				log.error("UserUtils getGuestLogin error", ignore);
			}

		}

		return null;
	}


	/**
	 * 전용구분 상품 조회용 코드 (상품 목록 조회 시 검색 조건으로 포함)
	 *
	 * OP_COMMON_CODE : ITEM_PRIVATE_TYPE 항목
	 *
	 * 업체별 설정에 따라 변경이 필요함.
	 * @return
	 */
	public static List<String> getPrivateTypes() {
		List<String> privateTypes = new ArrayList<>();

		privateTypes.add("00");		// 00:일반상품


		List<Code> itemPrivateTypes = CodeUtils.getCodeList("ITEM_PRIVATE_TYPE");

		for (Code code : itemPrivateTypes) {

			if (code.getDetail() == null) {
				continue;
			}

			// 1. 회원 ROLE 체크
			List<String> userRoles =SecurityUtils.getAuthorities();

			if (userRoles != null) {
				for (String role : userRoles) {
					if (code.getDetail().indexOf(role) > -1) {
						privateTypes.add(code.getValue());
					}
				}
			}

			// 2. 회원 그룹 체크
			if (getUserDetail() != null
					&& getUserDetail().getGroupCode() != null
					&& code.getDetail().indexOf(getUserDetail().getGroupCode()) > -1) {
				privateTypes.add(code.getValue());
			}
		}

		return privateTypes;
	}

	/**
	 * 휴면 계정인가?
	 * ROLE_USER, STATUS_CODE = 4
	 * @return
	 */
	public static boolean isDormantUser() {
		// ROLE_USER, STATUS_CODE = 4

		if (!UserUtils.isUserLogin()) {
			return false;
		}


		User currentUser = UserUtils.getUser();
		if ("4".equals(currentUser.getStatusCode())) {
    		return true;
    	}
		return false;
	}

	public static String masking(String str, String kind) {
		String returnValue = "";
		switch (kind) {
			case "name":
				// 한글명이면 2성은 성 다음 한자리 *
				// 1성이면 가운데 자리 *
				if(charCheck(str) == 5) {
					if(str.length() > 2) {
						if("|강전|고전|길강|길성|독고|남궁|동방|망절|사공|서문|선우|소봉|어금|장곡|제갈|황보|"
								.indexOf(str.substring(0, 2)) > -1) {
							returnValue = str.substring(0, 2) + "*" + str.substring(3, str.length());
						} else {
							returnValue = str.substring(0, 1) + "*" + str.substring(2, str.length());
						}
					} else {
						returnValue = str.substring(0, 1) + "*";
					}

					// 영문명이면 전체사이즈의 1/3 만큼 마스킹 처리
				} else {
					int str_length = str.length() / 3 * 2 ;
					returnValue =  str.substring(0,str_length) + "***";
				}
				break;
			case "tel":
				String[] arr = str.split("-");
				if(arr.length > 2) {
					returnValue = arr[0] + "-****-" + arr[2];
				}
				break;
			case "day":
				arr = str.split("-");
				if(arr.length > 2) {
					returnValue = "****-**-**";
				}
				break;
			case "cntr-name":
				if(charCheck(str) == 5) {
					returnValue = str.substring(0, 1) + "**";
				} else {
					int str_length = str.length() / 3 * 2 ;
					if (str_length > 5) {
						str_length = 5;
					}
					returnValue =  str.substring(0, str_length) + "***";
				}
				break;
			case "cntr-login-id":
				int str_length = str.length() / 3 * 2 ;
				if (str_length > 5) {
					str_length = 5;
				}
				returnValue =  str.substring(0, str_length) + "***";
				break;
			default :
		}

		return returnValue;
	}

	public static int charCheck(String chr){
		if (StringUtils.isEmpty(chr)) {			// 아무 값 없음		// 오류 발생하여 추가
			return 0;
		}
		char char_ASCII = chr.charAt(0);
		//alert(char_ASCII);

		//공백
		if (char_ASCII == 32)
			return 0;
			//숫자
		else if (char_ASCII >= 48 && char_ASCII <= 57 )
			return 1;
			//영어(대문자)
		else if (char_ASCII>=65 && char_ASCII<=90)
			return 2;
			//영어(소문자)
		else if (char_ASCII>=97 && char_ASCII<=122)
			return 3;
			//특수기호
		else if ((char_ASCII>=33 && char_ASCII<=47)
				|| (char_ASCII>=58 && char_ASCII<=64)
				|| (char_ASCII>=91 && char_ASCII<=96)
				|| (char_ASCII>=123 && char_ASCII<=126))
			return 4;
			//한글
		else if ((char_ASCII >= 12592) || (char_ASCII <= 12687))
			return 5;
		else
			return 9;
	}

	public static String reMasking(String str, String kind){
		String result = str;
		Matcher matcher = null;
		StringBuffer resultStr = new StringBuffer();
		switch (kind) {
			case "id":

			case "name":
				String pattern = "";
				if(str.length() == 2) {
					pattern = "^(.)(.+)$";
				} else {
					pattern = "^(.)(.+)(.)$";
				}
				matcher = Pattern.compile(pattern).matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');

							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
					}
				}
				break;
			case "account":
				if(str.split("-").length > 2){
					char[] acc = new char[str.split("-")[1].length()];
					Arrays.fill(acc, '*');
					resultStr.append(str.split("-")[0]);
					resultStr.append("-");
					resultStr.append(String.valueOf(acc));
					resultStr.append("-");
					resultStr.append(str.split("-")[2]);
					result = resultStr.toString();
				}
				break;
			case "phone" :
			case "tel":
				matcher = Pattern.compile("^(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$").matcher(str);
				if(matcher.matches()) {
					result = "";
					boolean isHyphen = false;
					if(str.indexOf("-") > -1) {
						isHyphen = true;
					}
					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');
							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
						if(isHyphen && i < matcher.groupCount()) {
							result = result + "-";
						}
					}
				}
				break;
			case "addr":
				String[] ary = result.split(" ");
				StringBuffer resultBf = new StringBuffer();
				char[] adc = null;
				for(int i = 0; i < ary.length; i ++){
					if(i == (ary.length-1)){
						adc = new char[ary[i].split("").length-1];
						Arrays.fill(adc, '*');
						resultBf.append(adc);
					} else {
						resultBf.append(ary[i].toString()+" ");
					}
				}
				result = resultBf.toString();
				break;
			case "addrDetail":
				String aryDetail[] =  result.split(" ");
				StringBuffer bufferDetail = new StringBuffer();
				char[] addc = null;
				for(int i = 0; i < aryDetail.length; i ++){
					addc = new char[aryDetail[i].split("").length-1];
					Arrays.fill(addc, '*');
					bufferDetail.append(String.valueOf(addc)+" ");
				}
				result = bufferDetail.toString();
				break;
			case "zipCode":
				if(result.split("-").length > 1){
					char[] zc = new char[result.split("-")[1].length()];
					Arrays.fill(zc, '*');
					resultStr.append(result.split("-")[0]);
					resultStr.append("-");
					resultStr.append(String.valueOf(zc));
				}
				result = resultStr.toString();
				break;
			case "newZipCode":
				char[] zc = new char[result.length()];
				Arrays.fill(zc, '*');
				resultStr.append(String.valueOf(zc));
				result = resultStr.toString();
				break;
			case "email":
				matcher = Pattern.compile("^(..)(.*)([@]{1})(.*)$").matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 2) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');

							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
					}
				}
				break;
			case "ip":
				matcher = Pattern.compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$").matcher(str);

				if(matcher.matches()) {
					result = "";

					for(int i=1;i<=matcher.groupCount();i++) {
						String replaceTarget = matcher.group(i);
						if(i == 3) {
							char[] c = new char[replaceTarget.length()];
							Arrays.fill(c, '*');

							result = result + String.valueOf(c);
						} else {
							result = result + replaceTarget;
						}
						if(i < matcher.groupCount()) {
							result =result + ".";
						}
					}
				}
				break;
			default :
		}

		return result;
	}

	public static boolean isSellerLogin() {
		return SecurityUtils.hasRole("ROLE_SELLER");
	}

	public static long getSellerUserId() {

		if (getUser() == null) {
			return 0;
		}

		if (!UserUtils.isSellerLogin()) {
			return 0;
		}

		return getUser().getUserId();
	}

	public static Seller getSeller() {

		if (UserUtils.isSellerLogin() && (getUser() != null && getUser().getObject() != null)) {
			return (Seller)UserUtils.getUser().getObject();
		}

		return null;
	}

	/**
	 * 판매관리자 마스터 운영자 여부
	 * @return
	 */
	public static boolean isSellerMasterUser() {

		return UserUtils.isSellerLogin()
				&& SecurityUtils.hasRole(AuthorityType.SELLER_MASTER.getCode());
	}


	/**
	 * 로그인 실패 시 이동할 페이지
	 * @param failureUrl
	 * @param loginType
	 * @return
	 */
	public static String resolveFailureUrl(String failureUrl, String loginType) {
		final Map<String, String> LOGIN_PAGES = new HashMap<>();
		LOGIN_PAGES.put(LoginRequest.USER, "/users/login");
		LOGIN_PAGES.put(LoginRequest.OPMANAGER, "/opmanager/login");
		LOGIN_PAGES.put(LoginRequest.SELLER, "/seller/login");

		String loginPage = LOGIN_PAGES.get(loginType);

		if (loginPage == null) {
			return LOGIN_PAGES.get(LoginRequest.USER);
		}

		if (failureUrl == null || failureUrl.isEmpty()) {
			return loginPage;
		}

		if (failureUrl.startsWith(loginPage)) {
			return failureUrl;
		}

		if (LoginRequest.USER.equals(loginType) && failureUrl.startsWith(ShopUtils.getMobilePrefix() + loginPage)) {
			return failureUrl;
		}

		return loginPage;
	}

	public static boolean isAdult() {
		if (!isUserLogin())
			return false;
		UserDetail userDetail = getUserDetail();
		LocalDate now = LocalDate.now();
		LocalDate parsedBirthDate = LocalDate.parse(userDetail.getBirthday(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		int americanAge = now.minusYears(parsedBirthDate.getYear()).getYear();
		return americanAge >= 19;
	}

	/**
	 * <pre>
	 * comment       : 로그인 2부제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 12. 14.
	 *
	 * </pre>
	 * @param step
	 * @param birthday
	 * @return
	 * HashMap<String,Object>
	 */
	public static HashMap<String, Object> isAlternateSystem(int step, String birthday) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("result", true);
		hashMap.put("today", DateUtils.getToday("yyyyMMdd"));

		try {
			Calendar cal = Calendar.getInstance();
			int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeekNumber == 2 || dayOfWeekNumber == 3 || dayOfWeekNumber == 4 || dayOfWeekNumber == 5 || dayOfWeekNumber == 6)
			{
				if(step == 0) {
					hashMap.put("result", true);
					hashMap.put("resultMsg", "2부제 중지");
					return hashMap;
				}

				if(birthday.length() != 8) {
					hashMap.put("result", false);
					hashMap.put("resultMsg", "생년월일이 올바르지 않습니다. birthday : "+birthday);
					return hashMap;
				}

				birthday = birthday.substring(7, 8);
				if(Integer.parseInt(DateUtils.getToday("yyyyMMdd")) % 2 == 0) {

					if(Integer.parseInt(birthday) % 2 == 0) {
						hashMap.put("result", true);
						hashMap.put("resultMsg", "2부제에 해당되는 날 입니다. ");
					}
					else
					{
						hashMap.put("result", false);
						hashMap.put("resultMsg", "2부제에 해당되지 않은 날입니다. 오늘 이용가능날짜 : 생년월일 마지막 자리 짝수");
					}
				}
				else
				{
					if(Integer.parseInt(birthday) % 2 == 0) {
						hashMap.put("result", false);
						hashMap.put("resultMsg", "2부제에 해당되지 않은 날입니다. 오늘 이용가능날짜 : 생년월일 마지막 자리 홀수");
					}
					else
					{
						hashMap.put("result", true);
						hashMap.put("resultMsg", "2부제에 해당되는 날 입니다.");
					}
				}
			}
		} catch (NullPointerException e) {
			log.error("======== isAlternateSystem Exception {} ", e);
		}

		return hashMap;
	}

	public static HashMap<String, Object> isAlternateSystemBackup(int step, String birthday) {
		HashMap<String, Object> hashMap = new HashMap<>();
		birthday = StringUtils.defaultIfEmpty(birthday, "");
		if(birthday.length() != 8) {
			hashMap.put("result", false);
			hashMap.put("resultMsg", "생년월일이 올바르지 않습니다. birthday : "+birthday);
			return hashMap;
		}

		if(step == 0) {
			hashMap.put("result", true);
			hashMap.put("resultMsg", "5부제 중지");
			return hashMap;
		}

		birthday = birthday.substring(7, 8);

		String[] week = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
		Calendar cal = Calendar.getInstance();
		int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK);
		String dayOfWeek = week[cal.get(Calendar.DAY_OF_WEEK) - 1];	//한글 요일명

		ArrayList<String> weekday = new ArrayList<>(Arrays.asList("-", "1-6", "2-7", "3-8", "4-9", "5-0", "-"));	//평일 5부제
		ArrayList<String> weekend = new ArrayList<>(Arrays.asList("1-2-3-4-5", "-", "-", "-", "-", "-", "6-7-8-9-0"));			//주말 2부제
		hashMap.put("result", true);
		hashMap.put("resultMsg", "주말은 5부제를 시행하지 않습니다.");
		//2부제 가능 생일
		if(weekend.get(dayOfWeekNumber-1).contains(birthday))
		{
			hashMap.put("result", true);
			hashMap.put("resultMsg", "2부제 가능 요일입니다.");
			hashMap.put("dayOfWeek", dayOfWeek);
			hashMap.put("possibleDay", dayOfWeek);
		}
		//2부제에 해당되지 않은 생년월일
		else
		{
			for(String day : weekend) {
				if(day.contains(birthday))
				{
					hashMap.put("result", false);
					hashMap.put("resultMsg", "2부제 불가능 요일입니다.");
					hashMap.put("dayOfWeek", dayOfWeek);
					hashMap.put("possibleDay", week[weekend.indexOf(day)]);
					return hashMap;
				}
			}
		}
		//평일 5부제 실시
		if(dayOfWeekNumber == 2 || dayOfWeekNumber == 3 || dayOfWeekNumber == 4 || dayOfWeekNumber == 5 || dayOfWeekNumber == 6)
		{
			//5부제 가능 생일
			if(weekday.get(dayOfWeekNumber-1).contains(birthday))
			{
				hashMap.put("result", true);
				hashMap.put("resultMsg", "5부제 가능 요일입니다.");
				hashMap.put("dayOfWeek", dayOfWeek);
				hashMap.put("possibleDay", dayOfWeek);
			}
			//5부제에 해당되지 않은 생년월일
			else
			{
				for(String day : weekday) {
					if(day.contains(birthday))
					{
						hashMap.put("result", false);
						hashMap.put("resultMsg", "5부제 불가능 요일입니다.");
						hashMap.put("dayOfWeek", dayOfWeek);
						hashMap.put("possibleDay", week[weekday.indexOf(day)]);
						return hashMap;
					}
				}
			}
		}
		//주말	2부제 실시
		else
		{
			hashMap.put("result", true);
			hashMap.put("resultMsg", "주말은 5부제를 시행하지 않습니다.");
			//2부제 가능 생일
			if(weekend.get(dayOfWeekNumber-1).contains(birthday))
			{
				hashMap.put("result", true);
				hashMap.put("resultMsg", "2부제 가능 요일입니다.");
				hashMap.put("dayOfWeek", dayOfWeek);
				hashMap.put("possibleDay", dayOfWeek);
			}
			//2부제에 해당되지 않은 생년월일
			else
			{
				for(String day : weekend) {
					if(day.contains(birthday))
					{
						hashMap.put("result", false);
						hashMap.put("resultMsg", "2부제 불가능 요일입니다.");
						hashMap.put("dayOfWeek", dayOfWeek);
						hashMap.put("possibleDay", week[weekend.indexOf(day)]);
						return hashMap;
					}
				}
			}
		}
		log.debug("======== isAlternateSystem {} ",hashMap);
		return hashMap;
	}

	/**
	 * 지자체 관리자 권한 여부
	 * @return boolean
	 */
	public static boolean hasLocgovManagerRole() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")
				|| SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8")
				|| SecurityUtils.hasRole("ROLE_ADMIN_10")
				|| SecurityUtils.hasRole("ROLE_ADMIN_11")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 시스템/행안부 관리자 권한 여부
	 * @return boolean
	 */
	public static boolean hasMasterManagerRole() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 오프라인 권한 여부
	 * @return boolean
	 */
	public static boolean hasOfflineRole() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			return true;
		}
		return false;
	}

	/**
	 * 지정기부 관리자 권한 여부
	 * @return boolean
	 */
	public static boolean hasDsgncntrManagerRole() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_10")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 스타일 태그 제거, iframe 태그 있을경우 유튜브, 비메오 아니면 제거
	 * @return String
	 */
	public static String removeIframe(String script) {
		if(StringUtils.isEmpty(script)) {
			return "";
		} else {
			StringBuffer buf = new StringBuffer();

			script = script.replaceAll("<style>", "");

			if (script != null && script.contains("iframe")) {		// iframe 포함되어 있을 경우
				String[] splitA = script.split("iframe");
				int length = splitA.length;
				for(int i = 0 ; i < length ; i++) {
					String check = splitA[i];

					try {
						String[] checkSplit = check.split(">");
						String[] checkSplit2 = checkSplit[0].split("src=\"");			// iframe 태그 내 src 경로가 앞부분에 나오도록
						int lengthCheckSplit2 = checkSplit2.length;
						if (lengthCheckSplit2 > 1) {
 							// youtube, vimeo만 허용 추가
							String srcValue = checkSplit2[1];
							int ytIndex = srcValue.indexOf("youtube.com/embed/");
							if (ytIndex >= 0 && ytIndex < 15) {									// src 경로 내에 youtube.com/embed/, player.vimeo.com/video/ 부분이 15문자 이내에서 시작할 경우 iframe 태그 허용
								splitA[i] = "iframe" + check + "iframe";
							}
						}
					} catch (IndexOutOfBoundsException | NullPointerException e) {		// 오류 발생할 경우 iframe 미허용
						log.info("UserUtils removeIframe not allow iframe url :: " + check);
					}
				}

				for (String string : splitA) {
					buf.append(string);
				}
			} else {		// iframe 포함되어 있지 않을 경우
				return script;
			}

			return buf.toString();
		}
	}

	public static double getFileSize(Path path) {
        long bytes = path.toFile().length();
        double kilobyte = bytes / 1024.0;
        double megabyte = kilobyte / 1024.0;

        return Math.round(megabyte*1000)/1000.0;
    }

	public static List<Object> nullToEmptyArr(List<Object> arr) {
		if (arr == null) {
			return new ArrayList<>();
		} else {
			for (Object object : arr) {
				if (object instanceof String) {
					object = StringEscapeUtils.escapeHtml((String) object);
				} else {
					break;
				}
			}
			return arr;
		}
	}

	public static Map<String, Object> nullToEmptyMap(Map<String, Object> map) {
		if (map == null) {
			return new HashMap<>();
		} else {
			Set<String> keySet = map.keySet();
			Iterator<String> keys = keySet.iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Object obj = map.get(key);
				if (obj instanceof String) {
					String value = (String) obj;
					value = StringEscapeUtils.escapeHtml(value);
					map.put(key, value);
				} else {
					break;
				}
			}
			return map;
		}
	}

}

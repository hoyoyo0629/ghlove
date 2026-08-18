package saleson.api.kakaolink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.onlinepowers.framework.exception.UserException;

import saleson.api.common.ApiResponseEntity;
import saleson.common.utils.UserUtils;
import saleson.shop.kakaolink.KakaoLinkService;
import saleson.shop.kakaolink.domain.KakaoLink;
import saleson.shop.kakaolink.domain.NaverToolkitData;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController("ApiKakaoLinkController")
@RequestMapping("/api/kakao-link")
public class KakaoLinkController {
	
	private static final Logger log = LoggerFactory.getLogger(KakaoLinkController.class);
	
	@Autowired
	private KakaoLinkService kakaoLinkService;

	/**
	 * 카카오에서 연동해제시 콜백
	 * 탈퇴여부 동의를 받을 수 없어서 해제 처리만 진행
	 * 카카오 요청 콜백응답은 오류 여부 상관없이 무조건 200 OK 처리
	 * */
	@GetMapping("/unlink-kakao")
//	public ResponseEntity<?> unlinkKakao(HttpServletRequest request, @RequestBody KakaoLink param) {
	public ResponseEntity<?> unlinkKakao(HttpServletRequest request, @RequestParam(name="user_id") String id, @RequestParam(name="referrer_type") String referrerType) {
		String appAdminKey = request.getHeader("authorization");
		
		String[] splitData = appAdminKey.split("KakaoAK");
		if (splitData.length == 2) {
			appAdminKey = splitData[1].trim();
		} else {
			log.error("=================" + getClass().getName() + " unlinkKakao appAdminKey parse error :: appAdminKey :: " + appAdminKey + ", id :: " + id);
		}
		
		if (kakaoLinkService.isEqualAdminKey(appAdminKey)/* && "UNLINK_FROM_APPS".equalsIgnoreCase(referrerType)*/) {
			
			try {
				kakaoLinkService.kakaoLinkClearByKakaoUserSequence(id);
			} catch (UserException e) {
				if (StringUtils.hasLength(e.getErrorMessage())) {
					log.error("=================" + getClass().getName() + " unlinkKakao kakaoLinkClearByKakaoUserSequence UserException :: appAdminKey :: " + appAdminKey + ", id :: " + id);
				} else {
					log.error("=================" + getClass().getName() + " unlinkKakao kakaoLinkClearByKakaoUserSequence error :: appAdminKey :: " + appAdminKey + ", id :: " + id);
				}
			}
		} else {
			log.error("=================" + getClass().getName() + " unlinkKakao appAdminKey error :: appAdminKey :: " + appAdminKey + ", id :: " + id);
		}
		return ApiResponseEntity.data().ok();
	}

	/**
	 * 고향사랑에서 카카오 연동해제
	 * */
	@PostMapping("/kakao-link-clear")
	public ResponseEntity<?> kakaoLinkClear() {
		ResponseEntity<?> result;
		long userId = UserUtils.getUser().getUserId();
		try {
			KakaoLink kakaoLink = kakaoLinkService.kakaoLinkClearByUserId(userId);
			if (kakaoLink.getApiError() != null) {
				switch (kakaoLink.getApiError()) {
				case NOT_VALID_LOGIN:
					result = ApiResponseEntity.data().put("errMsg", "로그인한 사용자 정보가 없습니다.").ok();
					break;
				case BAD_REQUEST:
					result = ApiResponseEntity.data().put("errMsg", "카카오 연동해제에 실패했습니다.").ok();
					break;
				default:
					result = ApiResponseEntity.data().put("errMsg", "카카오 연동해제 중 문제가 발생했습니다.").ok();
					break;
				}
			} else {
				if ("CHECK_SECEDE".equalsIgnoreCase(kakaoLink.getCode())) {
					result = ApiResponseEntity.data().put("result", "CHECK_SECEDE").ok();
				} else {
					result = ApiResponseEntity.data().put("result", "SUCCESS").ok();
				}
			}
		} catch (UserException e) {
			if (StringUtils.hasLength(e.getErrorMessage())) {
				result = ApiResponseEntity.data().put("errMsg", e.getErrorMessage()).ok();
			} else {
				result = ApiResponseEntity.data().put("errMsg", "카카오 연동해제 중 문제가 발생했습니다.").ok();
			}
		}
		return result;
	}

	/**
	 * 고향사랑에서 카카오 연동해제 및 탈퇴처리
	 * */
	@PostMapping("/kakao-link-secede")
	public ResponseEntity<?> kakaoLinkSecede(@RequestBody Map<String, String> paramMap) {
		ResponseEntity<?> result;
		long userId = UserUtils.getUser().getUserId();

    	// 탈퇴이유
    	String leaveCode = paramMap.get("leaveCode");
    	String leaveReason = paramMap.get("leaveReason");
    	
		try {
			KakaoLink kakaoLink = kakaoLinkService.kakaoLinkSecedeByUserId(userId, leaveCode, leaveReason);
			if (kakaoLink.getApiError() != null) {
				switch (kakaoLink.getApiError()) {
				case NOT_VALID_LOGIN:
					result = ApiResponseEntity.data().put("errMsg", "올바른 접근 경로가 아닙니다.").ok();
					break;
				case BAD_REQUEST:
					result = ApiResponseEntity.data().put("errMsg", "카카오 연동해제에 실패했습니다.").ok();
					break;
				default:
					result = ApiResponseEntity.data().put("errMsg", "카카오 연동해제 중 문제가 발생했습니다.").ok();
					break;
				}
			} else {
				result = ApiResponseEntity.data().put("result", "SUCCESS").ok();
			}
		} catch (UserException e) {
			if (StringUtils.hasLength(e.getErrorMessage())) {
				result = ApiResponseEntity.data().put("errMsg", e.getErrorMessage()).ok();
			} else {
				result = ApiResponseEntity.data().put("errMsg", "카카오 연동해제 중 문제가 발생했습니다.").ok();
			}
		}
		return result;
	}

	/**
	 * 카카오 연동 / 로그인 처리
	 * */
	@PostMapping("/kakao-link-login")
	public ResponseEntity<?> kakaoLinkLogin(HttpServletRequest request, @RequestBody KakaoLink kakaoLink) {
		if (kakaoLink == null || !StringUtils.hasLength(kakaoLink.getCode())) {
			return ApiResponseEntity.data().error();
		}
		
		try {
			KakaoLink kakaoResult = kakaoLinkService.kakaoLinkProcess(kakaoLink.getCode(), kakaoLink.getRequestToken(), kakaoLink.getType());
			
			if (kakaoResult.getApiError() != null) {
				switch (kakaoResult.getApiError()) {
					case BAD_REQUEST:
						return ApiResponseEntity.data().put("errMsg", "카카오톡 인증 로그인 연결에 실패했습니다.").ok();
					case UNAUTHORIZED_TOKEN:
						return ApiResponseEntity.data().put("errMsg", "카카오톡 인증 로그인 - 인증이 만료되었습니다.").ok();
					case NOT_EXIST_AUTH:
						return ApiResponseEntity.data().put("errMsg", "필수정보가 누락되어 해당 카카오계정은 회원가입/로그인 처리가 불가능합니다.").ok();
					case BAD_REQUEST_NOT_CONFIRM_STATUS:
						return ApiResponseEntity.data().put("errMsg", "14세 미만은 카카오톡 인증 로그인으로 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_BIRTH:
						return ApiResponseEntity.data().put("errMsg", "카카오 계정 정보에 생년월일 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_NAME:
						return ApiResponseEntity.data().put("errMsg", "카카오 계정 정보에 이름 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_EMAIL:
						return ApiResponseEntity.data().put("errMsg", "카카오 계정 정보에 이메일 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_PHONE:
						return ApiResponseEntity.data().put("errMsg", "카카오 계정 정보에 전화번호 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_CI:
						return ApiResponseEntity.data().put("errMsg", "카카오 계정 정보에 개인식별 정보가 없어 로그인/회원가입이 불가능합니다.").ok();
					default:
						return ApiResponseEntity.data().put("errMsg", "카카오톡 인증 로그인에 실패했습니다.").ok();
				}
			}
			return ApiResponseEntity.data().put("data", kakaoResult).put("status", 200).ok();		// 개발서버에서 status 값이 결과에 없어서 추가
		} catch(UserException e) {
			return ApiResponseEntity.data().put("errMsg", e.getErrorMessage()).ok();
		}
	}

	/**
	 * 네이버 인증 로그인 페이지 조회
	 * */
	@PostMapping("/naver-login-page")
	public ResponseEntity<?> getNaverLoginPage(HttpServletRequest request, @RequestBody KakaoLink naverParam) {
		try {
			NaverToolkitData result = kakaoLinkService.getNaverLoginUrl("JOIN".equalsIgnoreCase(naverParam.getType()));
			return ApiResponseEntity.data().put("loginUrl", result.getLoginUrl()).ok();
		} catch (UserException e) {
			return ApiResponseEntity.data().put("errMsg", e.getErrorMessage()).ok();
		}
	}

	/**
	 * 네이버 인증 페이지 조회
	 * */
	@PostMapping("/naver-auth-polling")
	public ResponseEntity<?> getNaverAuthPollingPage(HttpServletRequest request, @RequestBody KakaoLink naverParam) {
		try {
			NaverToolkitData result = kakaoLinkService.getNaverPollingUrl(naverParam.getCode(), "JOIN".equalsIgnoreCase(naverParam.getType()));
			return ApiResponseEntity.data().put("authPollingUrl", result.getAuthPollingUrl()).ok();
		} catch (UserException e) {
			return ApiResponseEntity.data().put("errMsg", e.getErrorMessage()).ok();
		}
	}
	
	/**
	 * 네이버 연동 / 로그인 처리
	 * */
	@PostMapping("/naver-auth-login")
	public ResponseEntity<?> naverAuthLogin(HttpServletRequest request, @RequestBody KakaoLink naverParam) {
		if (naverParam == null || !StringUtils.hasLength(naverParam.getTxId())) {
			return ApiResponseEntity.data().error();
		}
		try {
			KakaoLink naverAuthResult = kakaoLinkService.naverAuthProcess(naverParam.getTxId(), "JOIN".equalsIgnoreCase(naverParam.getType()));
			
			if (naverAuthResult.getApiError() != null) {
				switch (naverAuthResult.getApiError()) {
					case BAD_REQUEST:
						return ApiResponseEntity.data().put("errMsg", "네이버 인증 로그인 연결에 실패했습니다.").ok();
					case UNAUTHORIZED_TOKEN:
						return ApiResponseEntity.data().put("errMsg", "네이버 인증 로그인 - 인증이 만료되었습니다.").ok();
					case NOT_EXIST_AUTH:
						return ApiResponseEntity.data().put("errMsg", "필수정보가 누락되어 해당 네이버계정은 회원가입/로그인 처리가 불가능합니다.").ok();
					case BAD_REQUEST_NOT_CONFIRM_STATUS:
						return ApiResponseEntity.data().put("errMsg", "14세 미만은 네이버 인증 로그인으로 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_BIRTH:
						return ApiResponseEntity.data().put("errMsg", "네이버 계정 정보에 생년월일 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_NAME:
						return ApiResponseEntity.data().put("errMsg", "네이버 계정 정보에 이름 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_EMAIL:
						return ApiResponseEntity.data().put("errMsg", "네이버 계정 정보에 이메일 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_PHONE:
						return ApiResponseEntity.data().put("errMsg", "네이버 계정 정보에 전화번호 정보가 없어 회원가입이 불가능합니다.").ok();
					case KAKAO_LINK_NO_CI:
						return ApiResponseEntity.data().put("errMsg", "네이버 계정 정보에 개인식별 정보가 없어 로그인/회원가입이 불가능합니다.").ok();
					default:
						return ApiResponseEntity.data().put("errMsg", "네이버 인증 로그인에 실패했습니다.").ok();
				}
			}
			
			return ApiResponseEntity.data().put("data", naverAuthResult).put("status", 200).ok();		// 개발서버에서 status 값이 결과에 없어서 추가
		} catch(UserException e) {
			return ApiResponseEntity.data().put("errMsg", e.getErrorMessage()).ok();
		}
	}
	

}

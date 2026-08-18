package saleson.shop.kakaolink;

import saleson.shop.kakaolink.domain.KakaoLink;
import saleson.shop.kakaolink.domain.NaverToolkitData;

public interface KakaoLinkService {
	
	/**
	 * 카카오 사용자 일련번호로 로그인 해제
	 * @param userSequence
	 */
	public void kakaoLinkClearByKakaoUserSequence(String userSequence);
	
	/**
	 * 로그인 해제
	 * @param userId
	 */
	public KakaoLink kakaoLinkClearByUserId(long userId);
	
	/**
	 * 카카오 사용자 일련번호로 로그인 해제
	 * @param kakaoCode, signData, type
	 */
	public KakaoLink kakaoLinkProcess(String kakaoCode, String signData, String type);
	
	/**
	 * 카카오 어드민 키 일치여부 체크
	 * @param appAdminKey
	 */
	public boolean isEqualAdminKey(String appAdminKey);

	/**
	 * 카카오 링크 해제 및 탈퇴
	 * @param userId, leaveCode, leaveReason
	 */
	public KakaoLink kakaoLinkSecedeByUserId(long userId, String leaveCode, String leaveReason);
	
	/**
	 * 네이버 로그인 url 가져오기 - 네이버 로그인 진행할 주소
	 * @param isJoin
	 */
	public NaverToolkitData getNaverLoginUrl(boolean isJoin);
	
	/**
	 * 네이버 로그인 polling url 가져오기 - 네이버 인증 진행할 주소
	 * @param code, isJoin
	 */
	public NaverToolkitData getNaverPollingUrl(String code, boolean isJoin);
	
	/**
	 * 네이버 사용자 정보 조회
	 * @param txId
	 */
	public KakaoLink getNaverUserInfo(String txId, boolean isJoin);
	
	
	/**
	 * 네이버 인증 로그인 / 회원가입 처리
	 * @param txId
	 */
	public KakaoLink naverAuthProcess(String txId, boolean isJoin);
	
}

package saleson.shop.kakaolink;


import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.kakaolink.domain.KakaoLink;
import saleson.shop.kakaolink.domain.NaverToolkitData;

@Mapper("kakaoLinkMapper")
public interface KakaoLinkMapper {
	
	/**
	 * userId 로 가입경로, 카카오 사용자 키 조회
	 * @param userId
	 */
	public KakaoLink selectLoginPathCode(long userId);
	
	/**
	 * 유저 정보 탈퇴 처리
	 * @param userSequence
	 */
	public void kakaoLinkClearAndLeave(String userSequence);
	
	/**
	 * 카카오 유저 아이디 삭제
	 * @param userId
	 */
	public int kakaoLinkClear(long userId);
	
	/**
	 * 로그인 해제
	 * @param userId
	 */
	public void updateLoginSession(long userId);
	
	/**
	 * 카카오 유저 아이디 삭제
	 * @param kakaoUserKey
	 */
	public int kakaoLinkClearByKakaoUserKey(String kakaoUserKey);
	
	/**
	 * 네이버 인증 관리 정보 추가
	 * @param kakaoLink
	 */
	public int insertNaverAuthLoginInfo(NaverToolkitData naverAuthLoginInfo);
	
	/**
	 * 네이버 인증 관리 정보 삭제
	 * @param accessToken
	 */
	public int deleteNaverAuthLoginInfoByAccessToken(String accessToken);
	
	/**
	 * 네이버 인증 관리 정보 삭제
	 * @param txId
	 */
	public int deleteNaverAuthLoginInfoByTxId(String txId);
	
	/**
	 * 네이버 인증 관리 정보 조회
	 * @param txId
	 */
	public NaverToolkitData selectNaverAuthLoginInfoByTxId(String txId);
	
	
}

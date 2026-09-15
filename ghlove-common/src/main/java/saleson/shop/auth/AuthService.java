package saleson.shop.auth;

import saleson.shop.user.domain.Customer;

public interface AuthService {
	
	/**
	 * 인증 번호를 생성하고 SMS를 발송한다.
	 * @param loginId
	 * @param phoneNumber
	 * @return
	 */
	public String sendSmsAuthNumber(String loginId, String phoneNumber);
	
	
	/**
	 * 인증 번호를 생성하고 SMS를 발송한다.
	 * @param phoneNumber
	 * @return 
	 */
	public String getSmsAuthNumber(String phoneNumber);
	
	
	/**
	 * 인증 번호를 생성하고 SMS를 발송한다.
	 * 회원 여부 확인 후 발송.
	 * @param loginId
	 * @param phoneNumber
	 * @return 
	 */
	public String getSmsAuthNumber(String loginId, String phoneNumber);
	
	
	/**
	 * 인증 번호를 생성하고 SMS를 발송한다.
	 * 관리자 / 회원 여부 확인 후 발송.
	 * @param loginId
	 * @param phoneNumber
	 * @param isAdmin 관리자 체크 여부 (true : 관리자 권한 체크 포함)
	 * @return 
	 */
	public String getSmsAuthNumber(String loginId, String phoneNumber, boolean isAdmin);

	
	/**
	 * 휴대폰 번호 변경을 위한 이메일 인증번호 발송하고 요청 토큰을 리턴한다.
	 * @param customer
	 * @return
	 */
	public String getEmailAuthNumber(Customer customer);


	/**
	 * LOCAL DEV ONLY: SMS 게이트웨이가 없는 로컬 환경에서, 발송 실패 시 대신 생성해 둔
	 * 인증번호를 화면에 표시하기 위해 조회(1회성, 조회 후 삭제)한다. 운영 환경에서는
	 * 실제 SMS 발송이 성공하므로 항상 null을 반환한다.
	 * @param requestToken
	 * @return
	 */
	public String getLocalDevAuthCode(String requestToken);
}

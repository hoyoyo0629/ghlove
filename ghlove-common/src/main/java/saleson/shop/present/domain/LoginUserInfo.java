package saleson.shop.present.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserInfo {

	private String userKey;			// 고향사랑 기부 기본키
	private String userID;			// 아이디
	private String userLevel;		// 01=슈퍼관리자, 09=중간관리자(지자체 관리자), 98=일반회원(구매자)
	private String userName;		// 이름
	private String userBirthday;			// 생년월일
	private String userCellPhone;	// 핸드폰 번호
	private String userZoneCode;	// 지자체코드

	List<LoginUserDonaInfo> userDonaData = new ArrayList<>();

}

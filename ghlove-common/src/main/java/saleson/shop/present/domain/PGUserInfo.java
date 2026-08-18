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
public class PGUserInfo {

	private String userKey;			// 고향사랑 기부 기본키
	private String userID;			// 아이디
	private String userName;		// 이름
	private String userCellPhone;	// 핸드폰 번호
	private String userReturnDir;	// 넘겨받을 페이지

	List<PGUserDonaInfo> userDonaData = new ArrayList<>();

}

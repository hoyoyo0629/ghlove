package saleson.shop.claim.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClaimMemo {
	private int claimMemoId;
	private long userId;
	private String userName;
	private String orderCode;
	private String claimStatus;
	private String memo;
	private long managerUserId;
	private String managerLoginId;
	private String dataStatusCode;
	private String createdDate;

	public String getClaimStatusLabel() {
		if ("1".equals(getClaimStatus())) {
			return "처리중";
		} else if ("2".equals(getClaimStatus())) {
			return "처리완료";
		}
		
		return "-";
	}


	/**
	 * 컬럼 암호화
	 * @param dataEncryptor
	 */
	public void encrypt(DataEncryptor dataEncryptor) {
		dataEncryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param dataEncryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor dataEncryptor, boolean needMasking) {
		dataEncryptor.decrypt(this, needMasking);
	}
}

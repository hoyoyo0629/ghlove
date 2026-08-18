package saleson.shop.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyInfo {
	
	/**
     * 이용약관 동의
     */
    public final static String POLICY_TYPE_AGREEMENT_POLICY = "0";
    /**
     * 개인정보처리방침
     */
    public final static String POLICY_TYPE_PRIVACY_POLICY = "1";
    /**
     * 개인정보제3자동의
     */
    public final static String POLICY_TYPE_OTHER_POLICY = "4";
    
    /**
     * 저작권정책
     */
    public final static String POLICY_TYPE_COPYRIGHT_POLICY = "5";
    
    /**
     * 개인정보 수집·이용 동의
     */
    public final static String POLICY_TYPE_COLLECTION_POLICY = "6";

	private String policyId;
	private String policyType;
	private String title;
	private String content;
}

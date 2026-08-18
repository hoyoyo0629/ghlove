package saleson.common.userauth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserAuth {
	private String appKey;
	private String serviceType;
	private String serviceMode; // IPIN, PCC
	private String serviceTarget;	// JOIN, FIND-ID, FIND-PASSWORD
	private String userIp;
	private String authKey;
	private String authName;
	private String authSex;
	private String authBirthDay;
	private String dataStatusCode;
	private String createdDate;
	
}

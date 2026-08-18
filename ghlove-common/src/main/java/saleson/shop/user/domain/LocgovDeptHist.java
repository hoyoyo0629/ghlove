package saleson.shop.user.domain;

import java.util.List;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocgovDeptHist {

	private String locgovCode;
	private String deptHistNo;
	private String processDeptCode;
	private Long frstRegisterId;
	private Long lastUpduserId;
	private String lastUpdtPnttm;
	private String userName;
	private String loginId;
	
}

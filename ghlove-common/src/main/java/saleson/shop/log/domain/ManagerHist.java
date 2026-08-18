package saleson.shop.log.domain;

import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerHist {

	private int histId;
	private int userId;
	private int histSeq;
	private String loginId;
	private String userName;
	private String email;
	private String phoneNumber;
	private String statusCode;
	private String updatedDate;
	private String createdDate;
	private String locgovCode;
	private String bankCode;
	private String empId;
	private String psitnNm;
	private String psitnDeptNm;
	private String ofcpsNm;
	private String authority;
	private String infoUpdtDe;
	private String regUsertxt;

	private List<String> userIdList;	// 회원 ID 목록 (목록에서 수정하는 경우)

}


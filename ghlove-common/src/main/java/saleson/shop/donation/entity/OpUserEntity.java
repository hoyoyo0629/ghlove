package saleson.shop.donation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "op_user")
public class OpUserEntity {

	@Id
    private Long userId;

	@Column
	private String loginId;

	@Column
	private String password;

	@Column
	private String userName;

	@Column
	private String email;

	@Column
	private Integer statusCode;

	@Column
	private Integer loginCount;

	@Column
	private String sleepMailSendDate;

	@Column
	private String loginDate;

	@Column
	private String denyDate;

	@Column
	private Integer loginFailCount;

	@Column
	private String loginTryDate;

	@Column
	private String passwordType;

	@Column
	private String passwordExpiredDate;

	@Column
	private String updatedDate;

	@Column
	private String createdDate;

	@Column
	private String locgovCode;

	@Column
	private String mberCi;

	@Column
	private String mberDi;

	@Column
	private String mberDn;

	@Column
	private String mberFinDn;

	@Column
	private String sbscrbSeCode;

	@Column
	private String loginPathCode;

	@Column
	private String userKey;

	@Column
	private String kakaoUserKey;

	@Column
	private String foreignStatusCode;

	@Column
	private String naverUserKey;
}


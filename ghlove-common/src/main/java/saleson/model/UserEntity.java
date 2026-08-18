package saleson.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OP_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userId", callSuper = false)
public class UserEntity {

	@Id
//	@GeneratedValue
	private Long userId;

	@Column
	private String loginId;

	@Column
	private String password;

	@Column
	private String userName;

	@Column
	private String eMAIL;

	@Column
	private int statusCode;

	@Column
	private int loginCount;

	@Column
	private String sleepMailSendDate;

	@Column
	private String loginDate;

	@Column
	private String denyDate;

	@Column
	private String leaveDate;

	@Column
	private int loginFailCount;

	@Column
	private String loginTryDate;

	@Column
	private String passwordType;

	@Column
	private String passwordExpiredDate;

	@Column
	private String locgovCode;

	@Column
	private String mberCi;

	@Column
	private String mberDi;

	@Column
	private String mberDn;

	@Column
	private String sbscrbSeCode;

	@Column
	private String createDate;

//	@Column
//	private String loginPathCodel;

	@Column
	private String loginPathCode;

	@Column
	private String userKey;

//	@CreatedDate
//    private Timestamp created;
//
//    @LastModifiedDate
//    private Timestamp updated;
//
//	public LocalDateTime getCreatedDateTime() {
//		if (created == null) {
//			return null;
//		} else {
//			return created.toLocalDateTime();
//		}
//	}
//
//	public LocalDateTime getUpdatedDateTime() {
//		if (updated == null) {
//			return null;
//		} else {
//			return updated.toLocalDateTime();
//		}
//	}

	@Column
	private String kakaoUserKey;

	@Column
	private String naverUserKey;



}

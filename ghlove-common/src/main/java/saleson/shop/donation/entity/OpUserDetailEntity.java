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
@Table(name = "op_user_detail")
public class OpUserDetailEntity {

	@Id
    @Column(name = "user_id")
    private Long userId;

	@Column(name = "group_code")
	private String groupCode;

	@Column(name = "level_id")
	private Integer levelId;

	@Column(name = "user_level_expiration_date")
	private String userLevelExpirationDate;

	@Column(name = "new_post")
	private String newPost;

	@Column(name = "post")
	private String post;

	@Column(name = "address")
	private String address;

	@Column(name = "address_detail")
	private String addressDetail;

	@Column(name = "tel_number")
	private String telNumber;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "fax_number")
	private String faxNumber;

	@Column(name = "receive_email")
	private String receiveEmail;

	@Column(name = "receive_sms")
	private String receiveSms;

	@Column(name = "receive_push")
	private String receivePush;

	@Column(name = "receive_pbanc")
	private String receivePbanc;

	@Column(name = "gender")
	private String gender;

	@Column(name = "age")
	private String age;

	@Column(name = "point")
	private Integer point;

	@Column(name = "buy_count")
	private Integer buyCount;

	@Column(name = "buy_price")
	private Integer buyPrice;

	@Column(name = "last_buy_date")
	private String lastBuyDate;

	@Column(name = "leave_reason")
	private String leavereason;

	@Column(name = "site_flag")
	private String siteFlag;

	@Column(name = "use_flag")
	private String useFlag;

	@Column(name = "birthday_type")
	private String birthdayType;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "leave_code")
	private String leaveCode;

	@Column(name = "leave_user_id")
	private Long leaveUserId;

}


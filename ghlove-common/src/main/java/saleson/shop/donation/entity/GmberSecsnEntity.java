package saleson.shop.donation.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "g_mber_secsn")
public class GmberSecsnEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "secsn_year")
	private String secsnYear;

	@Id
    @Column(name = "user_id")
	private String userId;

	@Column(name = "mber_ci")
	private String mberCi;

	@Column(name = "cntr_amt")
	private Long cntrAmt;

	@Column(name = "frst_register_id")
    private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

    @Column(name = "last_updusr_id")
    private Long lastUpdusrId;

    @Column(name = "last_updt_pnttm")
    private LocalDateTime lastUpdtPnttm;

}

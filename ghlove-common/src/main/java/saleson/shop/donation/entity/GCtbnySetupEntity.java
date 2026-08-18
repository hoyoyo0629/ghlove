package saleson.shop.donation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "g_ctbny_setup")
public class GCtbnySetupEntity implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "stdr_year")
    private String stdrYear;

	@Id
    @Column(name = "locgov_code")
    private String locgovCode;

    @Column(name = "lmt_amt")
    private Integer lmtAmt;

    @Column(name = "point_rate")
    private BigDecimal pointRate;

    @Column(name = "point_valid_pd")
    private Integer pointValidPd;

    @Column(name = "frst_register_id")
    private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

    @Column(name = "last_updusr_id")
    private Long lastUpdusrId;

    @Column(name = "last_updt_pnttm")
    private LocalDateTime lastUpdtPnttm;

}


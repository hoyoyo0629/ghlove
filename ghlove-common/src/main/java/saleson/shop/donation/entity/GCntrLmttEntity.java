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
@Table(name = "g_cntr_lmtt")
public class GCntrLmttEntity implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "lmtt_bgn_de")
    private String lmttBgnDe;

	@Id
    @Column(name = "lmtt_end_de")
    private String lmttEndDe;

	@Id
    @Column(name = "locgov_code")
    private String locgovCode;

	@Column(name = "violt_resn_code")
    private String violtResnCode;

	@Column(name = "violt_resn_cn")
    private String violtResnCn;

	@Column(name = "register_nm")
    private String registerNm;

	@Column(name = "frst_register_id")
	private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

    @Column(name = "last_updusr_id")
    private Long lastUpdusrId;

    @Column(name = "last_updt_pnttm")
    private LocalDateTime lastUpdtPnttm;

}


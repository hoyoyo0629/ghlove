package saleson.shop.donation.entity;

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
@Table(name = "g_instt_code")
public class GInsttCodeEntity {

	@Id
    @Column(name = "locgov_code")
    private String locgovCode;

    @Column(name = "upper_locgov_nm")
    private String upperLocgovNm;

    @Column(name = "locgov_nm")
    private String locgovNm;

    @Column(name = "locgov_mapng_code")
    private String locgovMapngCode;

    @Column(name = "administ_instt_code")
    private String administInsttCode;

    @Column(name = "instt_mapng_code")
    private String insttMapngCode;

    @Column(name = "use_at")
    private String useAt;

    @Column(name = "frst_register_id")
    private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

    @Column(name = "last_updusr_id")
    private Long lastUpdusrId;

    @Column(name = "last_updt_pnttm")
    private LocalDateTime lastUpdtPnttm;
}


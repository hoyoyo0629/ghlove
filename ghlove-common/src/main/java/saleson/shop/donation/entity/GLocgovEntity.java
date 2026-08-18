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
@Table(name = "g_locgov")
public class GLocgovEntity {

	@Id
    @Column(name = "locgov_code")
    private String locgovCode;

    @Column(name = "upper_locgov_nm")
    private String upperLocgovNm;

    @Column(name = "locgov_nm")
    private String locgovNm;

    @Column(name = "upper_locgov_code")
    private String upperLocgovCode;

    @Column(name = "locgov_intrcn_cn")
    private String locgovIntrcnCn;

    @Column(name = "charger_cttpc")
    private String chargerCttpc;

    @Column(name = "charger_nm")
    private String chargerNm;

    @Column(name = "charger_psitn_dept")
    private String chargerPsitnDept;

    @Column(name = "locgov_hmpg")
    private String locgovHmpg;

    @Column(name = "locgov_popltn_co")
    private String locgovPopltnCo;

    @Column(name = "locgov_ar")
    private String locgovAr;

    @Column(name = "locgov_spcprd")
    private String locgovSpcprd;

    @Column(name = "gcct_use_at")
    private String gcctUseAt;

    @Column(name = "etrcsh_use_at")
    private String etrcshUseAt;

    @Column(name = "locgov_budget_amt")
    private Long locgovBudgetAmt;

    @Column(name = "bizrno")
    private String bizrno;

    @Column(name = "locgov_zip")
    private String locgovZip;

    @Column(name = "bass_adres")
    private String bassAdres;

    @Column(name = "dtl_adres")
    private String dtlAdres;

    @Column(name = "achlqr_sle_at")
    private String achlqrSleAt;

    @Column(name = "use_at")
    private String useAt;

    @Column(name = "process_dept_code")
    private String processDeptCode;

    @Column(name = "administ_instt_code")
    private String administInsttCode;

    @Column(name = "offcs_nm")
    private String offcsNm;

    @Column(name = "offcs_file_nm")
    private String offcsFileNm;

    @Column(name = "orginl_file_nm")
    private String orginlFileNm;

    @Column(name = "stdr_1level_amt")
    private Long stdr1levelAmt;

    @Column(name = "stdr_2level_amt")
    private Long stdr2levelAmt;

    @Column(name = "stdr_3level_amt")
    private Long stdr3levelAmt;

    @Column(name = "fis_sp")
    private String fisSp;

    @Column(name = "charger_email")
    private String chargerEmail;

    @Column(name = "frst_register_id")
    private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

    @Column(name = "last_updusr_id")
    private Long lastUpdusrId;

    @Column(name = "last_updt_pnttm")
    private LocalDateTime lastUpdtPnttm;
}


package saleson.shop.donation.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "g_cntr")
public class GCntrEntity {

	@Id
    @Column(name = "cntr_sn")
    private String cntrSn;

    @Column(name = "cntr_de")
    private String cntrDe;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "psitn_locgov_code")
    private String psitnLocgovCode;

    @Column(name = "cntr_locgov_code")
    private String cntrLocgovCode;

    @Column(name = "cntr_amt")
    private Long cntrAmt;

    @Column(name = "cntr_point")
    private Long cntrPoint;

    @Column(name = "cntr_blce_point")
    private Long cntrBlcePoint;

    @Column(name = "point_end_de")
    private String pointEndDe;

    @Column(name = "sttemnt_pay_de")
    private String sttemntPayDe;

    @Column(name = "pay_valid_de")
    private String payValidDe;

    @Column(name = "cntr_path_code")
    private String cntrPathCode;

    @Column(name = "cntr_sttus_code")
    private String cntrSttusCode;

    @Column(name = "cntrbtr_opinion_cn")
    private String cntrbtrOpinionCn;

    @Column(name = "setle_mth_code")
    private String setleMthCode;

    @Column(name = "cntr_use_purps_code")
    private String cntrUsePurpsCode;

    @Column(name = "elctrn_pay_no")
    private String elctrnPayNo;

    @Column(name = "seoul_trget_at")
    private String seoulTrgetAt;

    @Column(name = "rcept_bank_code")
    private String rceptBankCode;

    @Column(name = "rcept_bank_nm")
    private String rceptBankNm;

    @Column(name = "rcepter_nm")
    private String rcepterNm;

    @Column(name = "rtnpsnt_reqst_code")
    private String rtnpsntReqstCode;

    @Column(name = "rtnpsnt_reqst_at")
    private String rtnpsntReqstAt;

    @Column(name = "info_agre_at")
    private String infoAgreAt;

    @Column(name = "frst_register_id")
    private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

    @Column(name = "last_updusr_id")
    private Long lastUpdusrId;

    @Column(name = "last_updt_pnttm")
    private LocalDateTime lastUpdtPnttm;

    @Column(name = "delete_at")
    @Builder.Default
    private String deleteAt = "N";

    @Column(name = "nts_sttus_mssage")
    private String ntsSttusMssage;

    @Column(name = "tmp_blce_point")
    private Long tmpBlcePoint;

    @Column(name = "foreign_status_code")
    private String foreignStatusCode;

    @Column(name = "prj_id")
    private Long prjId;

    @Column(name = "dsgn_dntn_biz_id")
    private Long dsgnDntnBizId;

    @Column(name = "link_instt_cd")
    private String linkInsttCd;
}


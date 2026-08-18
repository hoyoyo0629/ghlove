package saleson.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import saleson.model.base.GhLoveBaseEntity;

/**
 * 기부
 */
@Entity
@Table(name = "g_cntr")
@Getter
@Setter
public class GCntr extends GhLoveBaseEntity {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 기부 일련번호
     */
    @Id
    @Column(name = "CNTR_SN")
    private String cntrSn;

    /**
     * 기부 일자
     */
    @Column(name = "CNTR_DE")
    private String cntrDe;

    /**
     * 회원 고유번호
     */
    @Column(name = "USER_ID")
    private Long userId;

    /**
     *
     */
    @Column(name = "psitn_locgov_code")
    private String psitnLocgovCode;

    /**
     *
     */
    @Column(name = "cntr_locgov_code")
    private String cntrLocgovCode;

    /**
     * 기부 금액
     */
    @Column(name = "CNTR_AMT")
    private Integer cntrAmt;

    /**
     * 기부 포인트
     */
    @Column(name = "CNTR_POINT")
    private Integer cntrPoint;

    /**
     * 기부 잔액 포인트
     */
    @Column(name = "CNTR_BLCE_POINT")
    private Integer cntrBlcePoint;

    /**
     * 포인트 만료 일자
     */
    @Column(name = "POINT_END_DE")
    private String pointEndDe;

    /**
     * 신고 납부 일자
     */
    @Column(name = "STTEMNT_PAY_DE")
    private String sttemntPayDe;

    /**
     * 납부 유효 일자
     */
    @Column(name = "pay_valid_de")
    private String payValidDe;

    /**
     * 기부 경로 코드
     */
    @Column(name = "CNTR_PATH_CODE")
    private String cntrPathCode;

    /**
     * 기부 상태 코드
     */
    @Column(name = "CNTR_STTUS_CODE")
    private String cntrSttusCode;

    /**
     * 기부자 의견 내용
     */
    @Column(name = "CNTRBTR_OPINION_CN")
    private String cntrbtrOpinionCn;

    /**
     * 결제 방법 코드
     */
    @Column(name = "SETLE_MTH_CODE")
    private String setleMthCode;

    /**
     * 기부 사용 목적 코드
     */
    @Column(name = "CNTR_USE_PURPS_CODE")
    private String cntrUsePurpsCode;

    /**
     * 전자 납부 번호
     */
    @Column(name = "ELCTRN_PAY_NO")
    private String elctrnPayNo;

    /**
     * 서울시 대상 여부
     */
    @Column(name = "SEOUL_TRGET_AT")
    private String seoulTrgetAt;

    /**
     * 접수은행코드
     */
    @Column(name = "rcept_bank_code")
    private String rceptBankCode;

    /**
     * 접수은행명(지점명)
     */
    @Column(name = "rcept_bank_nm")
    private String rceptBankNm;

    /**
     * 접수자명
     */
    @Column(name = "rcepter_nm")
    private String rcepterNm;

    /**
     * 답례품 신청 코드
     */
    @Column(name = "RTNPSNT_REQST_CODE")
    private String rtnpsntReqstCode;

    /**
     * 답례품 신청 코드
     */
    @Column(name = "RTNPSNT_REQST_AT")
    private String rtnpsntReqstAt;

    /**
     * 행정정보이용동의여부
     */
    @Column(name = "info_agre_at")
    private String infoAgreAt;

    /**
     * 취소여부
     */
    @Column(name = "delete_at")
    private String deleteAt = "N";

//    /**
//     * 최초 등록자 ID
//     */
//    @CreatedBy
//    @Column(name = "FRST_REGISTER_ID")
//    private Long frstRegisterId;

//    /**
//     * 최초 등록 시점
//     */
//    @CreatedDate
//    @Column(name = "FRST_REGIST_PNTTM")
//    private LocalDateTime frstRegistPnttm;
//
//    /**
//     * 최종 수정자 ID
//     */
//    @LastModifiedBy
//    @Column(name = "LAST_UPDUSR_ID")
//    private Long lastUpdusrId;
//
//    /**
//     * 최종 수정 시점
//     */
//    @LastModifiedDate
//    @Column(name = "LAST_UPDT_PNTTM")
//    private LocalDateTime lastUpdtPnttm;


    @Column(name = "prj_id")
    private long prjId = 0;

    /**
     * 지정기부 아이디
     */
    @Column(name = "dsgn_dntn_biz_id")
    private long dsgnDntnBizId = 0;

    /**
     *	내/외국인 구분 코드
     */
    @Column(name = "foreign_status_code")
    private String foreignStatusCode = "0";


    @Column(name = "link_instt_cd")
    private String linkInsttCd;
}

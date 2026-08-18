package saleson.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import saleson.model.base.GhLoveBaseEntity;

/**
 * 기부 사용 포인트
 */
@Entity
@Table(name = "g_cntr_use_point")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(GCntrUsePointPK.class)
public class GCntrUsePoint extends GhLoveBaseEntity {

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
     * 사용 일련번호
     */
    @Id
    @Column(name = "USE_SN")
    private Integer useSn;

    /**
     * 포인트 사용 일자
     */
    @Column(name = "POINT_USE_DE")
    private String pointUseDe;

    /**
     * 기부 사용 포인트
     */
    @Column(name = "CNTR_USE_POINT")
    private Long cntrUsePoint;

    /**
     * 회원 고유번호
     */
    @Column(name = "USER_ID")
    private Long userId;

    /**
     *
     */
    @Column(name = "PSITN_LOCGOV_CODE")
    private String psitnLocgovCode;

    /**
     *
     */
    @Column(name = "cntr_locgov_code")
    private String cntrLocgovCode;

    /**
     * 사용 구분 코드
     */
    @Column(name = "USE_SE_CODE")
    private String useSeCode;

    /**
     * 사용 내용
     */
    @Column(name = "USE_CN")
    private String useCn;

    /**
     * 주문 번호
     */
    @Column(name = "ORDER_CODE")
    private String orderCode;

//    /**
//     * 최초 등록자 ID
//     */
//    @Column(name = "FRST_REGISTER_ID")
//    private Long frstRegisterId;
//
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
//    @Column(name = "LAST_UPDUSR_ID")
//    private Long lastUpdusrId;
//
//    /**
//     * 최종 수정 시점
//     */
//    @LastModifiedDate
//    @Column(name = "LAST_UPDT_PNTTM")
//    private LocalDateTime lastUpdtPnttm;

}

package saleson.shop.report.entity;

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
@Table(name = "op_call")
public class OpCall {

	@Id
    @Column(name = "call_date")
    private String callDate;

    @Column(name = "call_kookmin")
    private Long callKookmin;

    @Column(name = "call_lov")
    private Long callLov;

    @Column(name = "call_giver")
    private Long callGiver;

    @Column(name = "call_nhbank")
    private Long callNhbank;

    @Column(name = "call_platform")
    private Long callPlatform;

    @Column(name = "frst_register_id")
    private Long frstRegisterId;

    @Column(name = "frst_regist_pnttm")
    private LocalDateTime frstRegistPnttm;

}


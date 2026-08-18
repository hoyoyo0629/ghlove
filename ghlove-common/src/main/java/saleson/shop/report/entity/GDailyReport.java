package saleson.shop.report.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "g_daily_report")
public class GDailyReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "report_date")
    private String reportDate;

    @Column(name = "result_data")
    private String resultData;

    @Column(name = "frst_reg_id")
    private Long frstRegId;

    @Column(name = "frst_reg_dt")
    private LocalDateTime frstRegDt;

}


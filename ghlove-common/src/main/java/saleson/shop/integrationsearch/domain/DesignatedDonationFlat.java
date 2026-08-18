package saleson.shop.integrationsearch.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import saleson.shop.designateddonation.domain.DesignatedDonation;

@Data
public class DesignatedDonationFlat {
	@JsonProperty("prj_id")
	private long prjId;

	@JsonProperty("locgov_nm")
	private String locgovNm;

	@JsonProperty("prj_status")
	private String prjStatus;

	@JsonProperty("prj_image")
	private String prjImage;

	@JsonProperty("bsns_type")
	private String bsnsType;

	@JsonProperty("bsns_sub_type")
	private String bsnsSubType;

	@JsonProperty("prj_subject")
	private String prjSubject;

	@JsonProperty("cntr_cnt")
	private long cntrCnt;

	@JsonProperty("prj_st_dt")
	private String prjStDt;

	@JsonProperty("prj_ed_dt")
	private String prjEdDt;

	@JsonProperty("target_amt")
	private long targetAmt;

	@JsonProperty("cntr_amt")
	private long cntrAmt;

	@JsonProperty("display_flag")
	private String displayFlag;

	@JsonProperty("frst_regist_pnttm")
	private String frstRegistPnttm;

	@JsonProperty("rate_amt")
	private int rateAmt;

	@JsonProperty("in_date")
	private int inDate;

	@JsonProperty("dsgncntr_part_name")
	private String dsgncntrPartName;

	@JsonProperty("locgov_cd")
	private String locgovCd;

	@JsonProperty("upper_locgov_cd")
	private String upperLocgovCd;

	private float refineRateAmt() {
		return (float) (rateAmt/ 100.0);
	}

	public DesignatedDonation toDesignatedDonation() {
		DesignatedDonation dd = new DesignatedDonation();
		dd.setPrjId(prjId);
		dd.setLocgovNm(locgovNm);
		dd.setPrjStatus(prjStatus);
		dd.setPrjImage(prjImage);
		dd.setBsnsType(bsnsType);
		dd.setBsnsSubType(bsnsSubType);
		dd.setPrjSubject(prjSubject);
		dd.setCntrCnt(cntrCnt);
		dd.setPrjStDt(prjStDt);;
		dd.setPrjEdDt(prjEdDt);
		dd.setTargetAmt(targetAmt);
		dd.setDisplayFlag(displayFlag);
		dd.setSumAmt(cntrAmt);
		dd.setFrstRegistPnttm(Timestamp.valueOf(frstRegistPnttm));
		dd.setRateAmt(refineRateAmt());
		dd.setInDate(inDate);
		dd.setDsgncntrPartName(dsgncntrPartName);
		dd.setLocgovCode(locgovCd);
		dd.setUpperLocgovCode(upperLocgovCd);

		return dd;
	}
}

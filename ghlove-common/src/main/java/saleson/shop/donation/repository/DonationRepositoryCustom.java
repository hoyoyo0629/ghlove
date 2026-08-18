package saleson.shop.donation.repository;

import saleson.shop.donation.entity.GCntrEntity;
import saleson.shop.donation.entity.GCtbnySetupEntity;

public interface DonationRepositoryCustom {

	public GCntrEntity findByElctrnPayNoAndUserId(String elctrnPayNo, Long userId);

	public GCtbnySetupEntity findByStdrYearAndLocgovCode(String stdrYear, String locgovCode);

	public Long sumCntrAmtByUserIdAndCntrDe(String year, Long userId);

	public Long countByCntrDe(String searchDay);
	public Long countByCntrDe(String startDay, String endDay);

	public Long countByCntrDeAndCurrentDate(String searchDay); /* 2025-04-03 당일데이터 값 */
	public Long countByCntrDeAndCurrentDate(String startDay, String endDay);

	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String searchDay, String cntrPathCode);
	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String startDay, String endDay, String cntrPathCode);

	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(String searchDay, String cntrPathCode);
	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(String startDay, String endDay, String cntrPathCode);

	public Long countByCntrDeAndLinkInsttCd(String searchDay, String linkInsttCd);

	public Long countByCntrDeAndLinkInsttCdAndCurrentDate(String searchDay, String linkInsttCd);
	public Long countByCntrDeAndLinkInsttCdAndCurrentDate(String startDay, String endDay, String linkInsttCd);

	public Long sumCntrAmtByCntrDe(String searchDay);
	public Long sumCntrAmtByCntrDe(String startDay, String endDay);

	public Long sumCntrAmtByCntrDeAndCurrentDate(String searchDay); /* 2025-04-03 당일데이터 값 */
	public Long sumCntrAmtByCntrDeAndCurrentDate(String startDay, String endDay);

	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String searchDay, String cntrPathCode);
	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String startDay, String endDay, String cntrPathCode);

	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(String searchDay, String cntrPathCode);
	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(String startDay, String endDay, String cntrPathCode);

	public Long sumCntrAmtByCntrDeAndLinkInsttCd(String searchDay, String linkInsttCd);

	public Long sumCntrAmtByCntrDeAndLinkInsttCdAndCurrentDate(String searchDay, String linkInsttCd);
	public Long sumCntrAmtByCntrDeAndLinkInsttCdAndCurrentDate(String startDay, String endDay,String linkInsttCd);

	public Integer sumWegiveCntrAmtByWegiveAmtAndMberCi(String mberCi);

	public Long countByCntrDeAndNotLinkInstt(String searchDay);
	public Long countByCntrDeAndNotLinkInstt(String startDay, String endDay);

	public Long sumCntrAmtByCntrDeAndNotLinkInstt(String searchDay);
	public Long sumCntrAmtByCntrDeAndNotLinkInstt(String startDay, String endDay);
}

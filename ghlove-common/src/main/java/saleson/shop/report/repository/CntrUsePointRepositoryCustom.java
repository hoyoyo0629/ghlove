package saleson.shop.report.repository;

public interface CntrUsePointRepositoryCustom {

	public Long countOrderCodeByPointUseDe(String searchDay);
	public Long countOrderCodeByPointUseDe(String startDay, String endDay);

	public Long sumCntrUsePointByPointUseDe(String searchDay);
	public Long sumCntrUsePointByPointUseDe(String startDay, String endDay);
}

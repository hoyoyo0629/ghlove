package saleson.shop.nhapi;

import saleson.common.configuration.MapperNhApi;
import saleson.shop.analysis.domain.AnalysisVo;

@MapperNhApi("nhApiMapper")
public interface NhApiMapper {

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 유저 기부 정보 저장(CI 포함)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertUserCntrForNh(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 유저기부 정보 및 기부지자체 정보 저장(CI 포함)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertUserCntrLocgovForNh(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 지자체 코드 정보 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertLocgovForNh(AnalysisVo vo);

}

package saleson.shop.help;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.help.domain.Help;
import saleson.shop.help.support.HelpParam;

@Mapper("helpMapper")
public interface HelpMapper {

	/**
	 * 도움말 목록 갯수 조회
	 * @param helpParam
	 * @return
	 */
	int getFrontHelpListCount(HelpParam helpParam);

	/**
	 * 도움말 목록 조회
	 * @param helpParam
	 * @return
	 */
	List<Help> getFrontHelpList(HelpParam helpParam);

	/**
	 * 도움말 상세 조회
	 * @param helpParam
	 * @return
	 */
	Help getFrontHelpDetail(HelpParam helpParam);

	/**
	 * 조회수 증가
	 * @param mnlSn
	 * @return
	 */
	int addHitCount(Integer mnlSn);

	/**
	 * 도움말 파일 정보 조회
	 * @param helpParam
	 * @return
	 */
	Help getFrontHelpFileInfo(HelpParam helpParam);
}

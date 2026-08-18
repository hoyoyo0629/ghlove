package saleson.shop.stsfdg;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.stsfdg.support.StsfdgParam;

@Mapper("stsfdgMapper")
public interface StsfdgMapper {

	/**
	 * 콘텐츠 만족도 참여하기
	 * @param stsfdgInfo
	 */
	int joinSurvey(StsfdgParam stsfdgInfo);
	
}

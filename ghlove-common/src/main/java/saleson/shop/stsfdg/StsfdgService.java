package saleson.shop.stsfdg;

import saleson.shop.stsfdg.support.StsfdgParam;

public interface StsfdgService {

	/**
	 * 콘텐츠 만족도 참여하기
	 * @param stsfdgInfo
	 */
	int joinSurvey(StsfdgParam stsfdgInfo);
	
}

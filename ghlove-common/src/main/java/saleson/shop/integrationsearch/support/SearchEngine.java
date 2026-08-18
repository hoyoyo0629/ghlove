package saleson.shop.integrationsearch.support;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface SearchEngine<T> {
	/* 검색엔진 URL 생성 시 사용할 인코딩 */
	// 스페이스바
	String space = "%20";
	// Equal
	String equal = "%3D";
	// >
	String gt = "%3E";
	// <
	String lt = "%3C";
	// '(홑따옴표)
	String apostrophe = "%27";
	// ' and '
	String and = space + "and" + space;
	// order by
	String orderby = "order" + space + "by" + space;

	String createParamter(T param);
	Map<String, Object> requestApi(String parameter, String searchEngineUrl) throws IOException;
}

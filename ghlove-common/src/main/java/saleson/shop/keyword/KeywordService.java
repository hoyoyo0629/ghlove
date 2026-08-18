package saleson.shop.keyword;

import saleson.shop.item.support.ItemParam;
import saleson.shop.keyword.domain.Keyword;

import java.util.List;

/**
 * @since	2017-05-15
 * @author	seungil.lee
 */

public interface KeywordService {
	/**
	 * 인기검색어 조회
	 * @param limit
	 * @return
	 */
	List<Keyword> getBestKeyword(int limit);

	/**
	 * 검색시 키워드 추가(신규일경우) 혹은 weight증가(이미 등록 된 검색어)
	 * @param itemParam
	 */
	void mergeItemKeyword(ItemParam itemParam);

	/**
	 * 일일 키워드 설정
	 */
	void setKeywordDaily();
}

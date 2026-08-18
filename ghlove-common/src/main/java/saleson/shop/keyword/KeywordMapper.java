package saleson.shop.keyword;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import saleson.shop.keyword.domain.Keyword;

import java.util.List;

/**
 * @since	2017-05-15
 * @author	seungil.lee
 */

@Mapper("keywordMapper")
public interface KeywordMapper {

	/**
	 * 인기검색어 조회
	 * @return
	 */
	List<Keyword> getBestKeyword(int limit);

	/**
	 * 검색시 키워드 추가(신규일경우) 혹은 weight증가(이미 등록 된 검색어)
	 * @param keyword
	 */
	void mergeItemKeyword(Keyword keyword);

	/**
	 * 아이템 키워드를 String으로 return
	 * @return
	 */
	List<String> getItemKeywordString();

	/**
	 * 자동완성 검색어 삭제
	 */
	void clearDailyKeyword();

	/**
	 * 신규 자동완성 검색어 등록
	 * @param keywordList
	 */
	void setDailyKeyword(List<Keyword> keywordList);
}

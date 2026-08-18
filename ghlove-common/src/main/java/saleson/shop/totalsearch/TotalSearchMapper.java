package saleson.shop.totalsearch;


import java.util.List;


import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.totalsearch.domain.MyRecent;

@Mapper("totalSearchMapper")
public interface TotalSearchMapper {
	
	/**
	 * 내가찾은 검색어 조회
	 * @param qnaId
	 * @return
	 */
	public List<MyRecent> getRecentList(Long userId);
	
	/**
	 * 내가찾은 검색어 등록
	 * @param recent
	 */
	public void insertRecent(MyRecent recent);
	
	/**
	 * 내가찾은 검색어 삭제
	 * @param recent
	 */
	public void deleteRecent(int recentId);
	
	/**
	 * 내가찾은 검색어 삭제
	 * @param recent
	 */
	public void deleteRecentAll(Long userId);
	
	
	
}

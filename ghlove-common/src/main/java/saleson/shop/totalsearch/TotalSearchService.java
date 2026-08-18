package saleson.shop.totalsearch;

import java.util.HashMap;
import java.util.List;

import saleson.shop.totalsearch.domain.MyRecent;
import saleson.shop.totalsearch.support.TotalSearchParam;

public interface TotalSearchService {
	public HashMap locgovResult(TotalSearchParam param) throws Exception;
	public HashMap itemResult(TotalSearchParam param) throws Exception;
	public HashMap noticeResult(TotalSearchParam param) throws Exception;
	public HashMap qnaResult(TotalSearchParam param) throws Exception;
	public HashMap faqResult(TotalSearchParam param) throws Exception;
	public HashMap databoardResult(TotalSearchParam param) throws Exception;
	public HashMap guidanceResult(TotalSearchParam param) throws Exception;
	public HashMap eventResult(TotalSearchParam param) throws Exception;
	public HashMap specialitemResult(TotalSearchParam param) throws Exception;
	
	public List<HashMap> autoKeywordResult(TotalSearchParam param) throws Exception;
	public List<HashMap> popularResult(TotalSearchParam param) throws Exception;
	
	public List<MyRecent> myRecentResult(Long userId) throws Exception;
	public void insertRecent(MyRecent recent) throws Exception;
	public void deleteRecent(int recentId) throws Exception;
	public void deleteRecentAll(Long userId) throws Exception;
}

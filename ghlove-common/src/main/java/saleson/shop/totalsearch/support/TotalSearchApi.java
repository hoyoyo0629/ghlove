package saleson.shop.totalsearch.support;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
@Repository
public class TotalSearchApi{
	
	@Value("${totalsearch-server.engine-url}")
	private String engineAddress;
	
	@Value("${totalsearch-server.ksf-url}")
	private String ksfAddress;
	 
    /** REST 모듈 */
    @Resource(name = "restUtils")
    private RESTUtils restUtils;

    
    /**
     * 검색 API 전송전 파라미터 세팅
     * @param param
     * @param volumeName
     * @return
     * @throws Exception
     */
    public String searchApi(TotalSearchParam param, String volumeName) throws Exception{
    	TotalSearchUrl apiUrlDTO = new TotalSearchUrl();
    	StringBuffer searchQuery = new StringBuffer();
    	String searchApiUrl = "";
    	
    	/**
         * 검색URL 파라미터 세팅
         */
        apiUrlDTO.setCharset("UTF-8");
        apiUrlDTO.setBaseUrl("http://" + engineAddress + "/search5?");
        apiUrlDTO.setFields(param.getSelectFields());
        apiUrlDTO.setFrom(String.format("%s.post", volumeName));

        apiUrlDTO.setLogInfo(URLEncoder.encode(getCustomLoginfo(param), apiUrlDTO.getCharset()));
        
        if("locgov".equals(volumeName)) {
        	apiUrlDTO.setHilightFields(URLEncoder.encode("{\"locgov_intrcn_cn\":{\"length\":512,\"begin\":\"\", \"end\":\"\"}}", apiUrlDTO.getCharset()));
        }else if("notice".equals(volumeName) || "faq".equals(volumeName)){
        	apiUrlDTO.setHilightFields("");
        }else {
        	apiUrlDTO.setHilightFields(URLEncoder.encode("{\"subject\":{\"length\":512,\"begin\":\"\", \"end\":\"\"}},{\"content\":{\"length\":512,\"begin\":\"\", \"end\":\"\"}}", apiUrlDTO.getCharset()));
        }
        
        // 쿼리담기
        searchQuery = getSearchQuery(param, volumeName);
        apiUrlDTO.setQuery(URLEncoder.encode(searchQuery.toString(), apiUrlDTO.getCharset()));
        
        // 검색 URL 생성
        searchApiUrl = getMakeSearchUrl(param, apiUrlDTO);

        return restUtils.request(searchApiUrl);
    }
    
    /**
     * 검색엔진 쿼리생성
     * @param param
     * @param volumeName
     * @return
     * @throws Exception
     */
    public StringBuffer getSearchQuery(TotalSearchParam param, String volumeName) throws Exception{
        StringBuffer searchQuery = new StringBuffer();
        String searchField = "".equals(param.getSearchField())?"text_idx":param.getSearchField();
        
        /**
         * 검색쿼리 추가 영역
         */
        // 기본 검색 쿼리
        searchQuery.append(String.format("( ($1 %s='%s' allorderadjacent) or ($2 %s='%s' allword) or ($3 %s='%s' allwordthruindex) )", searchField, param.getSearchKeyword(), searchField, param.getSearchKeyword(), searchField, param.getSearchKeyword()));

        // qna 카테고리에서는 검색 대상에 작성자가 추가
        if("qna".equals(volumeName)){
            searchQuery.append(String.format(" or user_name='%s'", param.getSearchKeyword()));
        }

        // 정렬문 추가
        searchQuery.append(" order by $1 DESC, $2 DESC, $3 DESC");

        return searchQuery;
    }

    /**
     * 자동완성어 조회
     * @param TotalSearchParam
     * @return
     */
    public String callKsfAutoKeyword(TotalSearchParam param){
    	try {
	    	String modes = param.getAkcModes();
	        int domain_no = param.getDomainNo();
	        int max_count = param.getMaxCount();
	        String searchKeyword = URLEncoder.encode(param.getSearchKeyword(),"UTF-8");
	
	        String targetURL = String.format("http://%s/ksf/api/suggest?target=complete&term=%s&mode=%s&domain_no=%s&max_count=%s",ksfAddress, searchKeyword,modes,domain_no,max_count);
	        
	        return restUtils.request(targetURL);
		} catch (UnsupportedEncodingException e) {
			return "UnsupportedEncodingException";
		}
        
    }
    
    /**
     * 인기검색어 조회
     * @param TotalSearchParam
     * @return
     */
    public String callKsfPopular(TotalSearchParam param){
    	try {
    		String targetURL = String.format("http://%s/ksf/api/rankings?domain_no=%s&max_count=%s",ksfAddress,param.getDomainNo(),param.getMaxCount());
            return restUtils.request(targetURL);
		} catch (ParseException e) {
			return "callKsfPopular - ParseException Error";	
		} catch (Exception e) {
			return "callKsfPopular - Exception Error";
		}
    	
    }
    
    /**
     * 검색엔진 API에 전송할 URL 주소를 만든다.
     * @param param
     * @param apiUrlDTO
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getMakeSearchUrl(TotalSearchParam param, TotalSearchUrl apiUrlDTO) throws UnsupportedEncodingException{
        StringBuffer sb = new StringBuffer();

        // 검색 API URL
        sb.append(apiUrlDTO.getBaseUrl());

        // 노출 필드 및
        sb.append("select=" + apiUrlDTO.getFields());
        // 노출 필드 및
        sb.append("&from=" + apiUrlDTO.getFrom());
        // 검색쿼리
        sb.append("&where=" + apiUrlDTO.getQuery());
        // 페이지 번호
        sb.append("&offset=" + ((param.getPageNumber()-1)*param.getPageSize()));
        // 페이지 결과 사이즈
        sb.append("&pagelength=" + param.getPageSize());
        // 캐릭터셋 정의
        sb.append("&charset=" + apiUrlDTO.getCharset());
        // 하이 라이팅 필드 정의
        sb.append("&hilite-fields=" + apiUrlDTO.getHilightFields());
        // 기본 하이라이팅 정보
        sb.append("&default-hilite=" + apiUrlDTO.getHilightDefaultState());
        // 로그분석기 커스텀 로그 추가
        if(!"".equals(param.getUserid())){
            sb.append("&custom=" + apiUrlDTO.getLogInfo());
        }

        return sb.toString();
    }

    /**
     * 로그분석기용으로 검색엔진 로그에 커스텀 로그를 포맷에 맞게 생성하기 위한 함수.
     * @param param
     * @return
     */
    public String getCustomLoginfo(TotalSearchParam param){
        StringBuffer sb = new StringBuffer();

        if(!"".equals(param.getUserid())) {
        	sb.append(param.getSiteNm());
        	sb.append("@");
        	sb.append(param.getCategory());
        	sb.append("+");
        	sb.append(param.getUserid());
        	sb.append("$");
        	sb.append(param.getGender());
        	sb.append("|");
        	sb.append("첫검색");
        	sb.append("|");
        	sb.append(param.getPageNumber());
        	sb.append("|");
        	sb.append("정확도순");
        	sb.append("^");
        	sb.append(param.getSearchKeyword());
        	sb.append("]##");
        }

        return sb.toString();
    }
}

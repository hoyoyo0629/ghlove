package saleson.shop.totalsearch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.onlinepowers.framework.sequence.service.SequenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.shop.totalsearch.domain.MyRecent;
import saleson.shop.totalsearch.support.TotalSearchApi;
import saleson.shop.totalsearch.support.TotalSearchParam;

import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@RequiredArgsConstructor
@Service("totalSearchService")
public class TotalSearchServiceImpl extends EgovAbstractServiceImpl implements TotalSearchService {
	private final TotalSearchMapper totalSearchMapper;
	
	@Autowired
	private TotalSearchApi searchApi;
	@Autowired 
	private SequenceService sequenceService;
	
	@Autowired
	private DataMasking dataMasking;
	
	public HashMap<String, Object> locgovResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"locgov");
                return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
            
        } catch (MalformedURLException e) {
        	System.err.println("locgovResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("locgovResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("locgovResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("locgovResult - Exception Error");
			return null;
		}
    }
	
	public HashMap itemResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"item");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("itemResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("itemResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("itemResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("itemResult - Exception Error");
			return null;
		}
    }
	
	public HashMap noticeResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"notice");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("noticeResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("noticeResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("noticeResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("noticeResult - Exception Error");
			return null;
		}
    }
	
	public HashMap qnaResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"qna");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("qnaResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("qnaResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("qnaResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("qnaResult - Exception Error");
			return null;
		}
    }
	
	public HashMap faqResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"faq");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("faqResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("faqResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("faqResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("faqResult - Exception Error");
			return null;
		}
    }
	
	public HashMap databoardResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"databoard");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("databoardResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("databoardResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("databoardResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("databoardResult - Exception Error");
			return null;
		}
    }
	
	public HashMap guidanceResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"guidance");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("guidanceResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("guidanceResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("guidanceResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("guidanceResult - Exception Error");
			return null;
		}
    }
	
	public HashMap eventResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"event");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("eventResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("eventResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("eventResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("eventResult - Exception Error");
			return null;
		}
    }
	
	public HashMap specialitemResult(TotalSearchParam param) throws Exception{
        try{
            if(param.getSearchKeyword().length() > 0){
            	String resultJson = searchApi.searchApi(param,"specialitem");
            	return resultDataToHashMap(resultJson);
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
        	System.err.println("specialitemResult - MalformedURLException Error");
			return null;
		} catch (IOException e) {
			System.err.println("specialitemResult - IOException Error");
			return null;
		} catch (ParseException e) {
			System.err.println("specialitemResult - ParseException Error");
			return null;	
		} catch (Exception e) {
			System.err.println("specialitemResult - Exception Error");
			return null;
		}
    }
	
	public List<HashMap> autoKeywordResult(TotalSearchParam param) throws Exception{
		List<HashMap> resultMap = new ArrayList();
        Gson googleJson = new Gson();
        JsonParser jsonParser = new JsonParser();
        try{
            JsonObject jsonObject = (JsonObject)jsonParser.parse( searchApi.callKsfAutoKeyword(param));
            JsonArray jsonArr = jsonObject.getAsJsonArray("suggestions");
            JsonArray akcArr = null;
            
            if(jsonArr != null && jsonArr.size() > 0) {
            	HashMap akcMap = null;
            	akcArr = (JsonArray) jsonArr.get(0);
            	for(int i=0; i<akcArr.size(); i++) {
            		akcMap = new HashMap();
            		
            		akcMap.put("keyword", akcArr.get(i).toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("\\]",""));
            		
            		resultMap.add(akcMap);
            	}
            }

            return resultMap;
		} catch (JsonParseException e) {
			System.err.println("locgovResult - Exception Error");
			return null;
		}

    }
	
	public List<HashMap> popularResult(TotalSearchParam param) throws Exception{
        Gson googleJson = new Gson();
        JsonParser jsonParser = new JsonParser();
        List<HashMap> ppkList = new ArrayList ();
        JsonArray ppk = null;
        String metaClass = "";
        String meta, kwd = "";

        try{
            JsonArray jsonArr = (JsonArray)jsonParser.parse( searchApi.callKsfPopular(param));

            if(jsonArr != null && jsonArr.size() > 0) {
                HashMap ppkMap = null;
                for(int i=0; i<jsonArr.size(); i++) {
                    ppk = (JsonArray) jsonArr.get(i);

                    ppkMap = new HashMap();
                    ppkMap.put("num", (i+1));
                    kwd = ppk.get(0).toString().replaceAll("\"","");
                    meta = ppk.get(1).toString().replaceAll("\"","");

                    ppkMap.put("keyword", kwd);

                    if("new".equalsIgnoreCase(meta)){
                        metaClass = "new";
                    }else if("-".equalsIgnoreCase(meta)){
                        metaClass = "no";
                    }else if(Integer.parseInt(meta) > 0){
                        metaClass = "up";
                    }else if(Integer.parseInt(meta) < 0){
                        metaClass = "down";
                    }

                    ppkMap.put("meta", metaClass);

                    ppkList.add(ppkMap);
                    ppkMap = null;
                }
            }
            
            return ppkList;	
		} catch (JsonParseException e) {
			System.err.println("locgovResult - Exception Error");
			return null;
		}
	}
	
	public List<MyRecent> myRecentResult(Long userID) throws Exception{
		List<MyRecent> list = totalSearchMapper.getRecentList(userID);
				
		return list;
	}
	
	public void insertRecent(MyRecent recent) throws Exception{
		recent.setRecentId(sequenceService.getId("G_MYRECENT"));
		totalSearchMapper.insertRecent(recent);
	}
	
	public void deleteRecent(int recentId) throws Exception{
		totalSearchMapper.deleteRecent(recentId);
	}
	
	public void deleteRecentAll(Long userID) throws Exception{
		totalSearchMapper.deleteRecentAll(userID);
	}
	
	/**
	 * 검색결과 Json을 haspMap으로 재가공
	 * @param resultJson
	 * @return
	 * @throws Exception
	 */
	public HashMap resultDataToHashMap(String resultJson) throws Exception{
        HashMap resultMap = new HashMap();

        try{
            Gson googleJson = new Gson();
            JsonParser jsonParser = new JsonParser();

            JsonObject jsonObject = (JsonObject)jsonParser.parse(resultJson);
            JsonObject resultObject = jsonObject.getAsJsonObject("result");
            int total_count = Integer.parseInt(resultObject.get("total_count").toString());
            JsonArray jsonArr = resultObject.getAsJsonArray("rows");


            HashMap tempMap = null;
            List<HashMap> resultList = new ArrayList();
            JsonObject result;
            JsonObject record;

            if(jsonArr.size() > 0){
                List<String> keyList = null;
                result = (JsonObject) jsonArr.get(0);
                record = (JsonObject) result.get("fields");
                keyList = new ArrayList<>(record.keySet());

                for(int i=0; i<jsonArr.size() && 1< 2; i++) {
                    tempMap = new HashMap();
                    result = (JsonObject) jsonArr.get(i);
                    record = (JsonObject) result.get("fields");

                    for(int j=0; j<keyList.size(); j++) {
                    	String content = record.get(keyList.get(j)).toString().replaceAll("\"","");
                    	content = content.replaceAll("\\(WARNING: EVALUATION COPY\\[SEARCH\\]\\)", "");
                    	
                    	if(keyList.get(j).equals("user_name")) {
                    		tempMap.put(keyList.get(j), dataMasking.mask(content, Masking.NAME));
                    		
                    	}else {
                    		tempMap.put(keyList.get(j), content);
                    	}
                    }
                    resultList.add(tempMap);
                }
            }
            resultMap.put("count", total_count);
            resultMap.put("result",resultList);
            resultMap.put("msg", "[SUCCESS] Search");
		} catch (JsonParseException e) {
			resultMap.put("count",0);
            resultMap.put("result", new ArrayList<>());
            resultMap.put("msg", "[ERROR] parseJson :: resultDataToHashMap - Exception Error");
		}
        
        return resultMap;
    }
}

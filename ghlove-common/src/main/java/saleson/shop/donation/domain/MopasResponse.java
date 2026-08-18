package saleson.shop.donation.domain;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MopasResponse {
	private int responseCode;
	private String body;
	private Map<String, Object> headerMap = new LinkedHashMap<String, Object>();
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Map<String, Object> getHeaderMap() {
		if (headerMap == null) {
			return null;
		} else {
			Map<String, Object> returnVal = new LinkedHashMap<String, Object>();
			
			Iterator<Entry<String, Object>> iterator = headerMap.entrySet().iterator();
			
			while(iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				returnVal.put(entry.getKey(), entry.getValue());
			}
			
			return returnVal;
		}
	}
	
	public void setHeaderMap(Map<String, Object> headerMap) {
		if (headerMap == null) {
			this.headerMap = null;
		} else {
			this.headerMap = new LinkedHashMap<String, Object>();
			
			Iterator<Entry<String, Object>> iterator = headerMap.entrySet().iterator();
			
			while(iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				this.headerMap.put(entry.getKey(), entry.getValue());
			}
		}
	}
}

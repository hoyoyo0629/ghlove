package saleson.shop.donation.support;

public class GiroParam {

	private String jijacheCd;		// 지자체코드
	private String useInsttCode;    // 분류코드 
	private String giroNo;          // 지로번호
	private String host;            // 사용자접속URL
	
	public String getGiroNo() {
		return giroNo;
	}
	public void setGiroNo(String giroNo) {
		this.giroNo = giroNo;
	}
	public String getUseInsttCode() {
		return useInsttCode;
	}
	public void setUseInsttCode(String useInsttCode) {
		this.useInsttCode = useInsttCode;
	}
	public String getJijacheCd() {
		return jijacheCd;
	}
	public void setJijacheCd(String jijacheCd) {
		this.jijacheCd = jijacheCd;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@Override
	public String toString() {
		return "GiroParam [jijacheCd=" + jijacheCd + ", useInsttCode=" + useInsttCode + ", giroNo=" + giroNo + ", host="
				+ host + "]";
	}
}


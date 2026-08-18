package saleson.shop.offgive.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WlfrCntrMngParam extends SearchParam {

	// 행정 복지 센터 아이디
	private long pbadmsWlfrCntrId;
	
	// 상위 지자체 코드
    private String upperLocgovCode;
    public String getUpperLocgovCode() {
        return upperLocgovCode;
    }
    public void setUpperLocgovCode(String upperLocgovCode) {
        this.upperLocgovCode = upperLocgovCode;
    }
    
	// 지자체 코드
	private String lclgvCd;
	
	// 상위 지자체 명
	private String upperLocgovNm;
	
	// 지자체 명
	private String locgovNm;
	
	// 행정 복지 센터 명
	private String pbadmsWlfrCntrNm;

	// 행정 복지 센터 코드
	private String pbadmsWlfrCntrCd;

	// 사용 여부
	private String useYn;

	// 최초 등록 일시
	private String frstRegDt;
	
	// 검색조건(시작일, 종료일)
	private String searchStDt;
	private String searchEdDt;

	public String getSearchStDt() {
	    return searchStDt;
	}
	public void setSearchStDt(String searchStDt) {
	    this.searchStDt = searchStDt;
	}

	public String getSearchEdDt() {
	    return searchEdDt;
	}
	public void setSearchEdDt(String searchEdDt) {
	    this.searchEdDt = searchEdDt;
	}


}

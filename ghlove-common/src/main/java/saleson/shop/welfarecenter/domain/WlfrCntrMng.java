package saleson.shop.welfarecenter.domain;

import java.util.Date;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class WlfrCntrMng extends ListParam{

	// 행정 복지 센터 아이디
	private long pbadmsWlfrCntrId;

	// 상위 지자체 코드
    private String upperLocgovCode;

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

	// 최초 등록자 아이디
	private long frstRgtrId;

	// 최초 등록 일시
	private Date frstRegDt;

	// 최종 등록자 아이디
	private long lastRgtrId;

	// 최종 등록 일시
	private Date lastRegDt;

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

	// 페이징
	private int itemsPerPage;

}

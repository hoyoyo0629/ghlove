package saleson.shop.catalog.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CatalogCardNews extends ListParam {

	// 카드 뉴스
	private long cardNewsId;
	
	// 소식지 발간호
//	private String catalogYearNo;
	
	// 소식지 발간년도
	private int catalogYear;
	
	// 소식지 발간호
	private int catalogNo;
	
	// 카드뉴스 제목
	private String cardNewsSubject;
		
	// 카드뉴스 내용
	private String cardNewsCn;
	
	// 삭제여부
	private String deleteYn;
	
	// 작성자 명
	private String name;
	
	// 최초등록자 ID
	private long frstRegisterId;
	
	// 최초 등록 시점
	private Timestamp frstRegistPnttm;
	
	// 최초등록자 ID
	private long lastUpdusrId;
	
	// 최초 등록 시점
	private Timestamp lastUpdtPnttm;
	
	// 카드뉴스 이미지 설명 접근성
	private List<CatalogCardNewsImageExplain> cardNewsImageExplains = new ArrayList<>();

	
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();			
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	public String getFrstRegistPnttmStr() {
		try {
			return frstRegistPnttm.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (NullPointerException | DateTimeParseException e) {
			return "";
		}
	}

	public String getLastUpdtPnttmStr() {
		try {
			return lastUpdtPnttm.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (NullPointerException | DateTimeParseException e) {
			return "";
		}
	}
	
//	public int getCatalogYear() {
//		try {
//			return Integer.parseInt(catalogYearNo.split("-")[0]);	
//		} catch (NullPointerException | NumberFormatException e) {
//			return 0;
//		}
//	}
//	
//	public int getCatalogNo() {
//		try {
//			return Integer.parseInt(catalogYearNo.split("-")[1]);	
//		} catch (NullPointerException | NumberFormatException e) {
//			return 0;
//		}
//	}
	
	public String getCatalogYearNo() {
		return "" + catalogYear + "-" + catalogNo;
	}
	
	
	public String getCardNewsCn() {
		try {
			return URLDecoder.decode(cardNewsCn, "UTF-8");
		} catch (NullPointerException | UnsupportedEncodingException e) {
			return cardNewsCn;
		}
	}
	
}

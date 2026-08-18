package saleson.shop.catalog.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class CatalogMng{

	// 소식지 발간년도
	private int catalogYear;
	private String catalogYearStr;

	// 소식지 발간호수
	private int catalogNo;
	private String catalogNoStr;

	// 소식지 카운트
	private int rowNumber;

	// 소식지 중복체크
	private boolean isDuplicate;
	private int dupCnt;

	// 대민화면 표시 여부 - 진행상태
	private String displayYn;

	// 로그인된 아이디
	private long userId;

	// 최초 등록자
	private long frstRegisterId;

	// 최초 등록일시
	private Timestamp frstRegistPnttm;

	// 최종 수정자
	private long lastUpdusrId;

	// 최종 수정일시
	private Timestamp lastUpdtPnttm;

	/**
	 * 최초 등록일시 yyyy-MM-dd HH:mm:ss 형티로 리턴
	 * @return String
	 */
	public String getRegDateStr() {
		return timestampToStr(frstRegistPnttm);
	}

	/**
	 * 최종 수정일시 yyyy-MM-dd HH:mm:ss 형티로 리턴
	 * @return String
	 */
	public String getUpdtDateStr() {
		return timestampToStr(lastUpdtPnttm);
	}

	private String timestampToStr(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		LocalDateTime localDateTime = timestamp.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return localDateTime.format(formatter);
	}

	/**
	 * 소식지 발간호 호수 문자열 리턴 (ex: 2024년 1호)
	 * @return String
	 */
	public String getCatalogVerStr() {
		if (catalogYear > 0 && catalogNo > 0) {
			return "" + catalogYear + "년 " + catalogNo + "호";
		} else {
			return "";
		}
	}

	/**
	 * 콤보박스용 발간년도-호수 키(검색 값 용)
	 * @return String
	 */
	public String getCatalogYearNo() {
		if (catalogYear > 0 && catalogNo > 0) {
			return "" + catalogYear + "-" + catalogNo;
		} else {
			return "";
		}
	}


}

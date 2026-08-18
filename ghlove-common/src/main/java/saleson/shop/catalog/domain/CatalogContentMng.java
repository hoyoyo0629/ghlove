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
import saleson.shop.designateddonation.domain.PrjImage;
import saleson.shop.designateddonation.domain.PrjImageExplain;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CatalogContentMng extends ListParam {

	// 소식지 내용 아이디
	private long catalogContentId;

	// 소식지 발간년도
	private int catalogYear;

	// 소식지 발간호
	private int catalogNo;

	// 구분(대분류) - 공통코드 CODE_TYPE = CONTENT_TYPE 로 조회
	private String contentType;

	// 구분(소분류) - 공통코드 CODE_TYPE = CONTENT_SUB_TYPE 로 조회
	private String contentSubType;

	// 소식지 내용 제목
	private String catalogContentSubject;

	// 소식지 내용
	private String catalogContentCn;

	// 미리보기 이미지 경로
	private String thumbnailImgPath;

	// 소식지 내용2
	private String catalogContentCn2;

	// 미리보기 이미지 경로2
	private String thumbnailImgPath2;

	// 지자체코드
	private String locgovCode;

	// 상위 지자체코드
	private String upperLocgovCode;

	// 지자체명
	private String locgovNm;

	// 상위 지자체명
	private String upperLocgovNm;

	// 표시 여부
	private String displayYn;

	// 배너 핫소식 여부
	private String bannerYn;

	// 최초등록자 ID
	private long frstRegisterId;

	// 최초 등록 시점
	private Timestamp frstRegistPnttm;

	// 최초등록자 ID
	private long lastUpdusrId;

	// 최초 등록 시점
	private Timestamp lastUpdtPnttm;

	// 카운트
	private long rowNumber;

	// 소식지 내용 이미지 설명 접근성
	private List<CatalogContentImageExplain> catalogContentImageExplain = new ArrayList<>();

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


	public String getCatalogYearNo() {
		return "" + catalogYear + "-" + catalogNo;
	}


	public String getCatalogContentCn() {
		try {
			return URLDecoder.decode(catalogContentCn, "UTF-8");
		} catch (NullPointerException | UnsupportedEncodingException e) {
			return catalogContentCn;
		}
	}

	public List<CatalogContentImageExplain> getCatalogContentImageExplain() {
		return catalogContentImageExplain;
	}

	public void setCatalogContentImageExplain(List<CatalogContentImageExplain> catalogContentImageExplain) {
		this.catalogContentImageExplain = catalogContentImageExplain;
	}
}

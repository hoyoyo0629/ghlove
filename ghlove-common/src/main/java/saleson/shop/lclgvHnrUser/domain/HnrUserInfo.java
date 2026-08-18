package saleson.shop.lclgvHnrUser.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.util.StringUtils;

import lombok.Data;
import saleson.common.utils.UserUtils;

@Data
public class HnrUserInfo {

//	지자체 코드
	private String lclgvCd;

//	지자체 명
	private String lclgvNm;

//	명에사용자 선정 구분 코드
	private String hnrUserSlctnSeCd;

//	명예 등급
	private String hnrGrd;

//	만료일자 ex) 20240101
	private String expiredDate;

//	명에사용자 선정 구분 상세
	private String hnrUserSlctnSeDtl;

//	명예 사용자 혜택
	private String hnrUserRwrd;

//	기부확인증 제목
	private String hnrUserStngTtl;

//	기부확인증 제목
	private String rprsImgNm;

//	선택2 조건의 기부혜택증 시작일자
	private String nowCntrStartExpiredDate;

//	만료일자 날짜형태로 리턴
	public String getExpiredDateFormat() {
		if (StringUtils.hasLength(expiredDate)) {
			final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
			final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
			LocalDate localDate = LocalDate.parse(expiredDate, formatter1);

			return localDate.format(formatter2);
		} else {
			return "";
		}
	}

//	발급일자 날짜형태로 리턴
	public String getStartDateFormat() {

		String exDt = expiredDate;

		if ("NOW_CRTR_1YR_WTHN".equals(hnrUserSlctnSeCd)) {
			exDt = nowCntrStartExpiredDate;
		}

		if (StringUtils.hasLength(exDt)) {
			final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
			final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
			LocalDate localDate = LocalDate.parse(exDt, formatter1);

			if (!"NOW_CRTR_1YR_WTHN".equals(hnrUserSlctnSeCd)) {
				localDate = localDate.plusYears(-1);
				localDate = localDate.plusDays(1);
			}

			return localDate.format(formatter2);
		} else {
			return "";
		}
	}

	public String getUserName() {
		if (UserUtils.getUser() != null && UserUtils.getUser().getUserId() > 0) {
			return UserUtils.getUser().getUserName();
		} else {
			return "";
		}
	}

	public String getLclgvNm2() {
		if (StringUtils.hasLength(lclgvCd)) {
			if (lclgvCd.contains("000")) {
				return lclgvNm.split(" ")[0];
			} else {
				return lclgvNm;
			}
		} else {
			return "";
		}
	}

}

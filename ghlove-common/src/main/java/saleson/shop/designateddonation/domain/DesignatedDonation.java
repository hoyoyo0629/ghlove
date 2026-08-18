package saleson.shop.designateddonation.domain;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.designateddonation.support.DesignatedCntr;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DesignatedDonation extends ListParam{

	// 아이디
	private long prjId;
	
	// 상위 지자체 코드
	private String upperLocgovCode;
		
	// 지자체 코드
	private String locgovCode;
	
	// 사업구분코드
	private String bsnsType;
	
	// 사업구분하위코드
	private String bsnsSubType;
	
	// 프로젝트 시작일
	private String prjStDt;
	
	// 프로젝트 종료일
	private String prjEdDt;
	
	// 목표금액
	private long targetAmt;
	
	// 제목
	private String prjSubject;
	
	// 프로젝트 상태코드
	private String prjStatus;
	
	// 공개여부
	private String displayFlag;
	
	// 내용
	private String prjCn = "";
	
	private String prjContent;
	
	// 프로젝트 대표이미지
	private String prjImage;
	
	// 지자체 명
	private String locgovNm;
	
	// 최초등록자 ID
	private long frstRegisterId;
	
	// 최초 등록 시점
	private Timestamp frstRegistPnttm;
	
	// 최종수정자 ID
	private long lastUpdusrId;
	
	// 최종 수정 시점
	private Timestamp lastUpdtPnttm;
	
	// 상품이미지
	private MultipartFile[] prjDetailImageFiles;
	
	// 기부내역
	private List<DesignatedCntr> cntrList;
	
	// 상품 이미지 설명 접근성
	private List<PrjImageExplain> prjImageExplain = new ArrayList<>();

	
	private List<PrjImage> prjImages = new ArrayList<>();
	
	// 상세이미지 순서변경
	private int[] prjImageIds;

	// 이미지 복사
	private int[] copyPrjImageIds;

	// 관리자, 판매관리자 메인페이지에 제품 카운트를 위한값
	private int cnt;

	// 없어서 에러 발생 그래서 일단추가
	private String simpleContent;
	
	// 모금금액
	private long sumAmt;
	
	// 기타사항, 비고
	private String contentEtc;
	
	// 부서 아이디
	private long dsgncntrPartId;
	
	// 부서명
	private String dsgncntrPartName;
	
	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("prj")
				.toString();
	}
	
	// 모금비율
	private float rateAmt;
	
	// 종료까지 남은 일수
	private int leftDay;
	
	// 기부 건수
	private long cntrCnt;
	
	// 진행 중 날짜 여부, 1 : 현재 날짜가 시작일과 종료일 안, 2: 현재 날짜가 시작일 이전, 3: 현재 날짜가 종료일 이후
	private int inDate;
	
	private final String SUCCESS = "SUCCESS";
	
	private final String AFTER = "AFTER";
	
	private final String BEFORE = "BEFORE";
	
	private final String ATTAINMENT = "ATTAINMENT";
	
	private final String STATUS = "STATUS";
	
	
	
	
	public List<PrjImageExplain> getPrjImageExplain() {
		return prjImageExplain;
	}

	public void setPrjImageExplain(List<PrjImageExplain> prjImageExplain) {
		this.prjImageExplain = prjImageExplain;
	}
	
	public List<PrjImage> getPrjImages() {
		return prjImages;
	}

	public void setPrjImages(List<PrjImage> prjImages) {
		this.prjImages = prjImages;
	}
	
	public MultipartFile[] getPrjDetailImageFiles() {
		return CommonUtils.copy(prjDetailImageFiles);
	}
	
	public void setPrjDetailImageFiles(MultipartFile[] prjDetailImageFiles) {
		this.prjDetailImageFiles = CommonUtils.copy(prjDetailImageFiles);
	}
	
	public String getFrstRegistPnttmStr() {
		return timestampToStr(frstRegistPnttm);
	}

	public String getLastUpdtPnttmStr() {
		return timestampToStr(lastUpdtPnttm);
	}
	
	private String timestampToStr(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		LocalDateTime localDateTime = timestamp.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(formatter);
	}
	
	public String getPrjStDtForm() {
		return getStrToDateForm(prjStDt);
	}
	
	public String getPrjEdDtForm() {
		return getStrToDateForm(prjEdDt);
	}
	
	public String getStrToDateForm(String dateStr) {
		if (dateStr != null && dateStr.length() == 8) {
			return dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8);
		} else {
			return dateStr;
		}
	}
	
	public String getRateAmtStr() {
		return com.onlinepowers.framework.util.StringUtils.numberFormat(String.valueOf(Math.floor(rateAmt * 100) / 100));
//		return String.valueOf(Math.round(rateAmt * 100) / 100f);
//		return com.onlinepowers.framework.util.StringUtils.numberFormat(String.valueOf(Math.round(rateAmt * 100) / 100f));
	}
	
	public String getRateAmtStrWithPercent() {
		return getRateAmtStr() + "%";
	}
	
	public String getLeftDayStr() {
		if (leftDay == 0) {
			return "D-Day";
		} else if (leftDay > 0) {
			return "D+" + String.valueOf(leftDay);
		} else {
			return "D" + String.valueOf(leftDay);
		}
	}
	
	public List<DesignatedCntr> getCntrList() {
		if (cntrList == null) {
			return null;
		} else {
			List<DesignatedCntr> copy = new ArrayList<>();
			for (DesignatedCntr designatedCntr : cntrList) {
				DesignatedCntr cntr = new DesignatedCntr();
				cntr.setPrjId(designatedCntr.getPrjId());
				cntr.setUserName(designatedCntr.getUserName());
				cntr.setLoginId(designatedCntr.getLoginId());
				cntr.setCntrAmt(designatedCntr.getCntrAmt());
				cntr.setCntrDe(designatedCntr.getCntrDe());
				cntr.setGiveOrder(designatedCntr.getGiveOrder());
				cntr.setCheerMsg(designatedCntr.getCheerMsg());
				cntr.setCntrSn(designatedCntr.getCntrSn());
				copy.add(cntr);
			}
			return copy;
		}
	}
	
	public void setCntrList(List<DesignatedCntr> cntrList) {
		if (cntrList == null) {
			this.cntrList = null;
		} else {
			this.cntrList = new ArrayList<>();
			for (DesignatedCntr designatedCntr : cntrList) {
				DesignatedCntr cntr = new DesignatedCntr();
				cntr.setPrjId(designatedCntr.getPrjId());
				if (!StringUtils.hasLength(designatedCntr.getUserName())) {
					cntr.setUserName("정보없음");
				} else {
					if (designatedCntr.getGiveOrder() == 0) {
						cntr.setUserName(UserUtils.masking(designatedCntr.getUserName(), "cntr-name"));
					} else {
						cntr.setUserName(designatedCntr.getUserName());
					}
				}
				if (!StringUtils.hasLength(designatedCntr.getLoginId())) {
					cntr.setLoginId("정보없음");
				} else {
					if (designatedCntr.getGiveOrder() == 0) {
						cntr.setLoginId(UserUtils.masking(designatedCntr.getLoginId(), "cntr-login-id"));
					} else {
						cntr.setLoginId(designatedCntr.getLoginId());
					}
				}
				cntr.setCntrAmt(designatedCntr.getCntrAmt());
				cntr.setCntrDe(designatedCntr.getCntrDe());
				cntr.setGiveOrder(designatedCntr.getGiveOrder());
				cntr.setCheerMsg(designatedCntr.getCheerMsg());
				cntr.setCntrSn(designatedCntr.getCntrSn());
				this.cntrList.add(cntr);
			}
		}
	}
	
	public String getResultCode() {
		try {
			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate startDate = LocalDate.parse(prjStDt, formatter);
			LocalDate endDate = LocalDate.parse(prjEdDt, formatter);
			
			if (now.isBefore(startDate)) {
				return BEFORE;
			} else if (now.isAfter(endDate)) {
				return AFTER;
			} else if (!"2".equals(prjStatus)) {
				return STATUS;
			} else if (rateAmt >= 100f) {
				return ATTAINMENT;
			} else {
				return SUCCESS;
			}
		} catch (NullPointerException e) {
			return "";
		}
	}
	
	public String getListImg() {
		if (StringUtils.hasLength(prjImage)) {
			String path = prjImage.substring(0, prjImage.lastIndexOf("_"));
			String ext = prjImage.substring(prjImage.lastIndexOf("."));
			return path + "_M" + ext;
		} else {
			return "";
		}
	}
	
}

package saleson.shop.user.domain;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;

@Getter
@Setter
@NoArgsConstructor
public class Locgov {

	private String locgovCode;				// 지자체 코드
	private String upperLocgovNm;			// 상위지자체명
	private String locgovNm;				// 지자체 명
	private String upperLocgovCode;			// 상위 지자체 코드
	private String locgovIntrcnCn;			// 지자체 소개 내용
	private String chargerCttpc;			// 담당자 연락처
	private String chargerNm;				// 담당자 성명
	private String chargerEmail;			//담당자 이메일
	private String chargerPsitnDept;		// 담당자 소속 부서
	private String locgovHmpg;				// 지자체 홈페이지
	private String locgovPopltnCo;			// 지자체 인구 수
	private String locgovAr;				// 지자체 면적
	private String locgovSpcprd;			// 지자체 특산물
	private String gcctUseAt;				// 상품권 사용 여부
	private String etrcshUseAt;				// 전자화폐 사용 여부
	private String useAt;					// 사용 여부
	private Long locgovBudgetAmt;			// 지자체 예산 금액
	private String bizrno;					// 사업자번호
	private String locgovZip;				// 기본 주소
	private String bassAdres;				// 지자체 우편번호
	private String dtlAdres;				// 상세 주소
	private String achlqrSleAt;				// 주류 판매 여부
	private Long frstRegisterId;			// 최초 등록자 ID
	private String frstRegistPnttm;			// 최초 등록 시점
	private Long lastUpdusrId;				// 최종 수정자 ID
	private String lastUpdtPnttm;			// 최종 수정 시점
	private Double pointRate;				// 포인트 지급률
	private String processDeptCode;			// 처리부서코드
	private String administInsttCode;		// 행정기관코드


	/* 지자체 삭제시 사용 */
	private List<String> locgovCodeList;	// 지자체 코드 목록

	/* 포인트 */
	private String stdrYear;

	// 2023.02.10 추가
	private int stdr1levelAmt;				// 기준 1레벨 금액
	private int stdr2levelAmt;				// 기준 2레벨 금액
	private int stdr3levelAmt;				// 기준 3레벨 금액

	private String stdr1levelAmtCode;		// 기준 1레벨 코드
	private String stdr2levelAmtCode;		// 기준 2레벨 코드
	private String stdr3levelAmtCode;		// 기준 3레벨 코드

	private String lmttBgnDe;				// 제한 시작 일자
	private String lmttEndDe;				// 제한 종료 일자
	private String violtResnCn;				// 위반 사유 내용

	private String offcsNm;					// 직인 명
	private String offcsFileNm;				// 직인 파일 명
	private String orginlFileNm;			// 원본 파일 명
	private MultipartFile offcsFiles;		// 직인 파일

	private String fisSp;


	private MultipartFile addPcFile;		// 답례품 배경이미지(PC)
	private MultipartFile addMbFile;		// 답례품 배경이미지(모바일)

	private String pcFilePath;		// 답례품 배경이미지(PC)
	private String mbFilePath;		// 답례품 배경이미지(모바일)
	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}

	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("seal")
				.toString();
	}

	public String getFullFilePath() {
		return this.getUploadPath() + File.separator + this.offcsFileNm;
	}
}

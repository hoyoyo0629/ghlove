package saleson.common.sms.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.onlinepowers.framework.util.StringUtils;

import lombok.Data;

@Data
public class TifIpsSndngM {

	private Timestamp infoCrtDt;				// 정보생성일시 ex)2021/12/16 10:39:00
    private long listSn;							// 목록일련번호 ex)1
    private long insttCrtSn;					// 기관생성일련번호 ex)1234
    private String svcId;						// 서비스아이디 (별도전달)
    private String svcGrpId;		// 서비스그룹아이디 (별도전달)
    private String insttCrtDocId;				// 기관생성문서아이디 (해당사항 없음)
    private String prvcIdntfcInfo;				// 개인정보식별정보
    private String prvcIdntfcSeCd;				// 개인정보식별구분코드 (C0090001 : CI정보, C0090002 : 주민등록번호, C0090003 : 이름+전화번호 해시)
    private String sndngCntnts;					// 발송내용 (가변정보를 순서에 따라 입력. | 으로 구분) ex)xxx|2021년 12월 11일|통지서|사용자명|날짜|문서명

    private String img1UrlInfo;					// 이미지1URL정보 (해당사항 없음)
    private String img2UrlInfo;					// 이미지2URL정보 (해당사항 없음)
    private String img3UrlInfo;					// 이미지3URL정보 (해당사항 없음)
    private String esbIfId;						// 인터페이스아이디 ex)IF_P01_XXX_IPS_REQ01
    private String esbTxId;						// 트랜잭션아이디
    private String esbInitTime;					// 연계발생일시 ex)2021-12-15 00:00:00
    private String esbTxTime;					// 연계시작일시
    private String esbComptTime;				// 연계완료일시
    private String esbStatusCd;					// 연계처리상태코드 (S:성공, F:실패)
    private String esbWorkGbn;					// 작업구분코드 ex)I
    private String esbErrMsg;					// 연계오류메세지
    
    
    private String taxYear;						// 년도

    public String getEsbInitTime() {
    	if (StringUtils.hasLength(esbInitTime)) {
    		return esbInitTime;
    	} else {
        	return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    	}
    }
}

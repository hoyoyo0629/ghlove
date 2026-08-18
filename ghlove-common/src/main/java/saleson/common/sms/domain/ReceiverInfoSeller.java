package saleson.common.sms.domain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onlinepowers.framework.util.StringUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceiverInfoSeller {
	
//	private Logger log = LoggerFactory.getLogger(ReceiverInfoSeller.class);
//	
//	private String infoCrtDt;				// 정보생성일시 ex)2021/12/16 10:39:00
//    private int listSn;						// 목록일련번호 ex)1
//    private long insttCrtSn;				// 기관생성일련번호 ex)1234
//    private String svcId;					// 서비스아이디 (별도전달)
//    private final String svcGrpId = "070";				// 서비스그룹아이디 (별도전달)
//    private String insttCrtDocId;			// 기관생성문서아이디
////    private String prvcIdntfcInfo;			// 개인정보식별정보
//    private final String prvcIdntfcSeCd = "C0090003";			// 개인정보식별구분코드 (C0090001 : CI정보, C0090002 : 주민등록번호, C0090003 : 이름+전화번호 해시)
//    private String sndngCntnts;				// 발송내용 (가변정보를 순서에 따라 입력. | 으로 구분) ex)xxx|2021년 12월 11일|통지서|사용자명|날짜|문서명
//    
//    private String esbIfId;					// 인터페이스 아이디
//    private String esbInitTime;				// 연계발생일시
//    private String esbStatusCd;				// 연계처리 상태 코드 (N 입력, 솔루션에서 성공시 S, 실패시 F 로 변경함)
//    private String esbWorkGbn;				// 작업 구분 코드
//    
//    private String sellerName;				// 판매자 정보
//    private String sellerMobileNo;
//    
////    public void setPrvcIdntfcInfo(String prvcIdntfcInfo) {
////    	// 세팅 제외
////    }
//    
//    public String getPrvcIdntfcInfo() {
////    	prvcIdntfcInfo = getSha256();
////    	return prvcIdntfcInfo;
//    	return getSha256();
//    }
//    
//    
//    private String getSha256() {
//    	if (StringUtils.isEmpty(sellerName) || StringUtils.isEmpty(sellerMobileNo)) {
//    		throw new NullPointerException();
//    	}
//    	
//    	String result = null;
//    	
//    	try {
//    		String plainValue = sellerName + sellerMobileNo;
//    		plainValue.replaceAll("-", "").replaceAll(" ", "");
//    		MessageDigest md = MessageDigest.getInstance("SHA-256");
//    		md.update(plainValue.trim().getBytes("utf-8"));
//    		 
//    		result = new String(Base64.getEncoder().encode(md.digest()));
//		 } catch (NoSuchAlgorithmException e) {
//			 log.error("ReceiverInfoSeller NoSuchAlgorithmException error", e);
//		 } catch (UnsupportedEncodingException e) {
//			 log.error("ReceiverInfoSeller UnsupportedEncodingException error", e);
//		 }
//    		 
//		 return result;
//    }
    
}

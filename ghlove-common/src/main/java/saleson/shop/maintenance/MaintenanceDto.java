package saleson.shop.maintenance;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import saleson.common.configuration.SalesonProperty;


@Getter
@Setter
@ToString
public class MaintenanceDto extends SearchParam {

	private static final long serialVersionUID = 1L;

	private String[] shArrProcessState;			// 검색조건-완료구분

	private String startDt;						// 검색조건-시작일시
	private String endDt;						// 검색조건-종료일시
	private String query;
	private String where;
	private String shWdr;

	private Long bbsId;							//게시글 아이디
	private String srNo;						//sr 번호

	private String kiType;						//페이지구분(홈페이지, 관리자페이지)
	private String reqCrtDate;					//등록날짜
	private Date reqDt;							//상담일시
	private String reqChannel;					//요청경로 코드 (소통방,회의,이메일,sr게시판,내부메신저,sns,전화,기타)
	private String reqChannelNm;				//요청경로명 (소통방,회의,이메일,sr게시판,내부메신저,sns,전화,기타)
	private String reqType;						//업무구분 코드 (기부,답례품,기타)
	private String reqTypeNm;					//업무구분명 (기부,답례품,기타)
	private String processType;					//처리구분 코드 (기능신규,기능개선,오류수정,자료추출,데이터수정,기타)
	private String processTypeNm;				//처리구분명 (기능신규,기능개선,오류수정,자료추출,데이터수정,기타)

	private String reqManagerId;				//sr 접수 담당자 id
	private String reqManagerNm;				//sr 접수 담당자 이름

	private String locgovNm;		// 지자체
	private String upperLocgovNm;	// 상위 지자체
	private String authority;		// 소속

	private String reqUserPhoneNumber;			//요청고객 연락처(고객전화번호)
	private String reqUserInfo;					//요청고객 실명정보

	private String bbsTtl;						//게시글 제목
	private String bbsCn;						//게시글 내용
	private String urgentYn;					//긴급여부
	private String reqSource;					//출처

	private String processManagerId;			//처리 담당자 id
	private String processManagerNm;			//처리 담당자 이름
	private String processCn;					//처리내용
	private String processState;				//완료구분(접수,처리중,처리완료,제외)
	private String processStateNm;				//완료구분(접수,처리중,처리완료,제외)
	private String processReceiptDate;			//처리 접수일 YYYYMMDD
	private String processTargetEndDate;		//처리 종료 예정일 YYYYMMDD
	private String processStartDate;			//처리 시작일 YYYYMMDD
	private String processEndDate;				//처리 종료일 YYYYMMDD
	private String deployDate;					//처리 배포일 YYYYMMDD

	private String rm;							//비고


	private Date frstCrtDt;						//최초등록일시
	private Long frstCrtId;						//최초등록아이디
	private Long inqCnt;						//조회 수
	private Date lastMdfcnDt;					//최종수정일시
	private Long lastMdfcnId;					//최종수정아이디
	private String useYn;						//사용 여부
	private int attachedFileCnt;				//첨부파일갯수
	private Long atchFileSz; 					// 파일 사이즈

	//첨부파일리스트
	private List<MaintenanceFileDto> cmntyMaintenanceFileList;


	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("maintenance")
				.toString();
	}


}
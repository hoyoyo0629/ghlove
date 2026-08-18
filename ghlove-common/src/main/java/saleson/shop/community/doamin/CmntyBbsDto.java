package saleson.shop.community.doamin;

import java.util.Date;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CmntyBbsDto extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long userId; //유저 아이디
	private String userName;	// 유저 이름
	private String locgovNm;	// 지자체
	private String upperLocgovNm;	// 상위 지자체
	private String authority;	// 소속


	private String bbsCn;	//게시판 내용
	private Long bbsId;	//게시판 아이디
	private String bbsTtl;	//게시판 제목
	private Date frstCrtDt;	//최초등록일시
	private Long frstCrtId;	//최초등록아이디
	private Long inqCnt;	//조회 수
	private Date lastMdfcnDt;	//최종수정일시
	private Long lastMdfcnId;	//최종수정아이디
	private String noticeYn;	//공지 여부
	private String useYn;	//사용 여부
	private int cmntCnt;		//댓글 수
	private String isSecret; // 비밀글 사용여부
	private String isSecretYn; // 비밀글 사용




}
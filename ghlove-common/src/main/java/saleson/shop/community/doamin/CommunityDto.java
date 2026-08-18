package saleson.shop.community.doamin;

import java.util.Date;
import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommunityDto extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long id; //게시판 넘버
	private Long userId; // 유저 아이디
	private String userName;// 유저 이름
	private String adminCheck; //관리자인지 체크
	private String myInfo; // 내가 쓴글
	private String locgovNm;// 글쓴이 지자체
	private String shWdr; // 지자체 검색
	private String shLocgovCode; // 지지체 코드
	private String shCntrDeStart; // 등록일 시작
	private String shCntrDeEnd; // 등록일 끝
	private String useYn;  // 공개 , 비공개
	private String subject; // 제목
	private String content;// 내용
	private String isDelete; //삭제
	private String noticeFlag;// 상단 공지
	private	Date createdDate; //만들날짜
	private Date updatedDate; //업데이트
	private int hits; // 조회수
	private String upperLocgovNm;
	//일괄삭제
	private List<String> databoardList;

	private String adminRoleType ;

}
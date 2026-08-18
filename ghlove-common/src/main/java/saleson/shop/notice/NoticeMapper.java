package saleson.shop.notice;

import java.util.List;

import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.domain.NoticeSeller;
import saleson.shop.notice.support.NoticeParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("noticeMapper")
public interface NoticeMapper {
	
	/**
	 * 공지사항 카운트
	 * @param noticeParam
	 * @return
	 */
	int getNoticeCount(NoticeParam noticeParam);
	
	/**
	 * 공지사항 리스트(사용자)
	 * @param noticeParam
	 * @return
	 */
	List<Notice> getNoticeList(NoticeParam noticeParam);
	
	/**
	 * 공지사항 조회
	 * @param noticeId
	 * @return
	 */
	Notice getNotice(int noticeId);
	Notice getSellerNotice(NoticeParam noticeParam);
	void insertNotice(Notice notice);
	void updateNotice(Notice notice);
	void deleteNotice(int noticeId);
	
	
	/**
	 * 일괄삭제 
	 * @param noticeId
	 * @return
	 */	
	int deleteListNotice(int noticeId);
	/**
	 * 조회수 추가
	 * @param noticeId
	 */
	void addHitCount(int noticeId);
	
	List<Notice> getFrontNoticeList(NoticeParam noticeParam);
	
	int getFrontNoticeListCount(NoticeParam noticeParam);
	
	/**
	 * 카테고리 팀코드에 해당하는 공지사항 목록을 가져옴. (메인, 팀 용)
	 * @param noticeParam
	 * @return
	 */
	List<Notice> getFrontNoticeListByTeamCodes(NoticeParam noticeParam);
	
	void insertNoticeSeller(NoticeSeller noticeSeller);
	
	void deleteNoticeSeller(int noticeSellerId);
	
	List<NoticeSeller> getNoticeSellerList(int noticeId);

	/**
	 * 지자체공지사항 카운트
	 * @param noticeParam
	 * @return
	 */
	int getLocgovNoticeCount(NoticeParam noticeParam);

	/**
	 * 지자체공지사항 리스트
	 * @param noticeParam
	 * @return
	 */
	List<Notice> getLocgovNoticeList(NoticeParam noticeParam);

	void insertLocgovNotice(Notice notice);

	int locgovNoticeDelete(int noticeId);
	
	Notice getFrontNotice(int noticeId);
	
	
}

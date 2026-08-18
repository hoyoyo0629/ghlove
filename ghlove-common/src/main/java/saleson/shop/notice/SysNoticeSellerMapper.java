package saleson.shop.notice;

import java.util.List;

import saleson.shop.community.doamin.CmntyFaqBbsDto;
import saleson.shop.community.doamin.CmntyFaqBbsFileDto;
import saleson.shop.community.doamin.CmntyFileDto;
import saleson.shop.notice.domain.SysNoticeSellerDto;
import saleson.shop.notice.domain.SysNoticeSellerFileDto;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("sysNoticeSellerMapper")
public interface SysNoticeSellerMapper {

	/**
	 * 운영 공지사항_판매자 카운트
	 * @param noticeParam
	 * @return
	 */
	int getNoticeCount(SysNoticeSellerDto notice);

	/**
	 * 운영 공지사항_판매자 카운트 리스트(사용자)
	 * @param noticeParam
	 * @return
	 */
	List<SysNoticeSellerDto> getNoticeList(SysNoticeSellerDto notice);

	/**
	 * 운영 공지사항_판매자 카운트 조회
	 * @param noticeId
	 * @return
	 */
	SysNoticeSellerDto getNotice(int noticeId);

	public List<SysNoticeSellerFileDto> getSysNoticeSellerFileList(int noticeId);

	void insertNotice(SysNoticeSellerDto notice);

	void insertSysNoticeSellerFile(SysNoticeSellerFileDto file);

	void updateNotice(SysNoticeSellerDto notice);

	void deleteNotice(SysNoticeSellerDto notice);

	public void deleteNoticeFileByNoticeId(SysNoticeSellerDto notice);

	/**
	 * 조회수 추가
	 * @param noticeId
	 */
	void addHitCount(int noticeId);

	List<SysNoticeSellerDto> getFrontNoticeList(SysNoticeSellerDto notice);

	int getFrontNoticeListCount(SysNoticeSellerDto notice);

	SysNoticeSellerDto getFrontNotice(int noticeId);

	public void deleteNoticeFileByFileId(SysNoticeSellerFileDto sysNoticeSellerFileDto);

	public SysNoticeSellerFileDto getSysNoticeSellerFileDetail(long fileId);


}

package saleson.shop.notice;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import saleson.shop.notice.domain.SysNoticeSellerDto;
import saleson.shop.notice.domain.SysNoticeSellerFileDto;

public interface SysNoticeSellerService {

	/**
	 * 판매자_시스템 공지사항 카운트
	 * @param noticeParam
	 * @return
	 */
	public int getNoticeCount(SysNoticeSellerDto notice);

	/**
	 * 운영 공지사항_판매자 리스트(사용자)
	 * @param noticeParam
	 * @return
	 */
	public List<SysNoticeSellerDto> getNoticeList(SysNoticeSellerDto notice);

	/**
	 * 공지사항 조회
	 * @param noticeId
	 * @return
	 */
	public SysNoticeSellerDto getNotice(int noticeId, Model model);
	public void insertNotice(SysNoticeSellerDto notice, MultipartFile[] detailImageFiles);

	/* public void updateNotice(SysNoticeSellerDto notice); */
	public void deleteNotice(int noticeId);

	public List<SysNoticeSellerDto> getFrontNoticeList(SysNoticeSellerDto notice);

	public int getFrontNoticeListCount(SysNoticeSellerDto notice);

	/**
	 * 조회수 추가
	 * @param noticeId
	 */
	public void addHitCount(int noticeId);

	public SysNoticeSellerDto getFrontNotice(int noticeId);

	public List<SysNoticeSellerFileDto> getSysNoticeSellerFileList(int noticeId);

	void deleteNoticeFileByFileId(long fileId);

	public SysNoticeSellerFileDto getSysNoticeSellerFileDetail(long fileId);

	public void updateSysNoticeSeller(SysNoticeSellerDto sysNoticeSellerDto, MultipartFile[] detailImageFiles);


}

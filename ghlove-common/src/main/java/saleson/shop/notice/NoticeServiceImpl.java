package saleson.shop.notice;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.common.utils.UserUtils;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.domain.NoticeSeller;
import saleson.shop.notice.support.NoticeParam;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.SecurityUtils;

import saleson.shop.user.UserMapper;
import saleson.shop.user.domain.ContributionSetup;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.UserDetail;


@Service("noticeService")
public class NoticeServiceImpl extends EgovAbstractServiceImpl implements NoticeService{

	@Autowired
	NoticeMapper noticeMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired SequenceService sequenceService;

	@Override
	public void insertNotice(Notice notice) {
		int noticeId = sequenceService.getId("OP_NOTICE");
		notice.setNoticeId(noticeId);
		notice.setLocgovCode("00000");
		notice.setUserName(SecurityUtils.getCurrentUser().getUserName());
		if (null == notice.getNoticeFlag()) {
			notice.setNoticeFlag("N");
		}
		//notice.setCategoryTeam(notice.getCategoryTeam().replaceAll(",", "|"));
		noticeMapper.insertNotice(notice);
		if(notice.getSellerIds() != null){
			NoticeSeller noticeSeller = new NoticeSeller();

			for (long sellerId : notice.getSellerIds()) {
				int noticeSellerId = sequenceService.getId("OP_NOTICE_SELLER");
				noticeSeller.setNoticeSellerId(noticeSellerId);
				noticeSeller.setNoticeId(noticeId);
				noticeSeller.setSellerId(sellerId);

				noticeMapper.insertNoticeSeller(noticeSeller);
			}
		}
	}

	@Override
	public int getNoticeCount(NoticeParam noticeParam) {

		return noticeMapper.getNoticeCount(noticeParam);
	}

	@Override
	public List<Notice> getNoticeList(NoticeParam noticeParam) {

		return noticeMapper.getNoticeList(noticeParam);
	}

	@Override
	public Notice getNotice(int noticeId) {

		return noticeMapper.getNotice(noticeId);
	}

	@Override
	public void updateNotice(Notice notice) {

		//notice.setCategoryTeam(notice.getCategoryTeam().replaceAll(",", "|"));

		if (null == notice.getNoticeFlag()) {
			notice.setNoticeFlag("N");
		}

		noticeMapper.updateNotice(notice);

		if(notice.getSellerIds() != null){
			NoticeSeller noticeSeller = new NoticeSeller();
			for (long sellerId : notice.getSellerIds()) {
				int noticeSellerId = sequenceService.getId("OP_NOTICE_SELLER");
				noticeSeller.setNoticeSellerId(noticeSellerId);
				noticeSeller.setNoticeId(notice.getNoticeId());
				noticeSeller.setSellerId(sellerId);

				noticeMapper.insertNoticeSeller(noticeSeller);
			}
		}

	}

	@Override
	public void deleteNotice(int noticeId) {

		noticeMapper.deleteNotice(noticeId);
	}

	@Override
	public List<Notice> getFrontNoticeList(NoticeParam noticeParam) {

		return noticeMapper.getFrontNoticeList(noticeParam);
	}

	@Override
	public List<Notice> getFrontNoticeListByTeamCodes(NoticeParam noticeParam) {
		return noticeMapper.getFrontNoticeListByTeamCodes(noticeParam);
	}

	@Override
	public int getFrontNoticeListCount(NoticeParam noticeParam) {
		return noticeMapper.getFrontNoticeListCount(noticeParam);
	}

	@Override
	public void addHitCount(int noticeId) {

		noticeMapper.addHitCount(noticeId);
	}

	@Override
	public void deleteNoticeSeller(int noticeSellerId){
		noticeMapper.deleteNoticeSeller(noticeSellerId);
	}

	public List<NoticeSeller> getNoticeSellerList(int noticeId){
		return noticeMapper.getNoticeSellerList(noticeId);
	}

	@Override
	public String deleteListNotice(NoticeParam noticeParam) {
		String code = "FAIL";

		if(noticeParam.getNoticeList() != null && noticeParam.getNoticeList().size() > 0) {
			int nResult = 0;

			for (String noticeListId : noticeParam.getNoticeList()) {

				int noticeId = Integer.parseInt(noticeListId);
				nResult += noticeMapper.deleteListNotice(noticeId);
			}

			if (nResult == noticeParam.getNoticeList().size()) {
				code = "SUCC";
			} else {
				throw new RuntimeException();
			}
		}
		return code;
	}


	@Override
	public int getLocgovNoticeCount(NoticeParam noticeParam) {
		return noticeMapper.getLocgovNoticeCount(noticeParam);
	}

	@Override
	public List<Notice> getLocgovNoticeList(NoticeParam noticeParam) {
		return noticeMapper.getLocgovNoticeList(noticeParam);
	}

	@Override
	public void insertLocgovNotice(Notice notice) {
		int noticeId = sequenceService.getId("OP_NOTICE");
		notice.setNoticeId(noticeId);

		if (null == notice.getNoticeFlag()) {
			notice.setNoticeFlag("N");
		}

		//notice.setCategoryTeam(notice.getCategoryTeam().replaceAll(",", "|"));
		noticeMapper.insertLocgovNotice(notice);
		if(notice.getSellerIds() != null){
			NoticeSeller noticeSeller = new NoticeSeller();

			for (long sellerId : notice.getSellerIds()) {
				int noticeSellerId = sequenceService.getId("OP_NOTICE_SELLER");
				noticeSeller.setNoticeSellerId(noticeSellerId);
				noticeSeller.setNoticeId(noticeId);
				noticeSeller.setSellerId(sellerId);

				noticeMapper.insertNoticeSeller(noticeSeller);
			}
		}
	}

	/**
	 * 지자체공지사항 리스트 삭제
	 * @param noticeParam
	 * @return
	 */
	@Override
	public String locgovNoticeDelete(NoticeParam noticeParam) {
		String code = "FAIL";

		if(noticeParam.getLocgovNoticeList() != null && noticeParam.getLocgovNoticeList().size() > 0) {
			int nResult = 0;

			// 지자체 & 기부금 설정 정보 삭제
			for (String noticeListId : noticeParam.getLocgovNoticeList()) {

				int noticeId = Integer.parseInt(noticeListId);
				nResult += noticeMapper.locgovNoticeDelete(noticeId);
			}

			if (nResult == noticeParam.getLocgovNoticeList().size()) {
				code = "SUCC";
			} else {
				throw new RuntimeException();
			}
		}
		return code;
	}

	@Override
	public Notice getFrontNotice(int noticeId) {
		// TODO Auto-generated method stub
		return noticeMapper.getFrontNotice(noticeId);
	}
}

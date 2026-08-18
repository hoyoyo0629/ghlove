package saleson.shop.designateddonation.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class DesignatedDonationNotice extends ListParam {

	// 공지내역 아이디
	private long prjNoticeId;

	// 지정기부 아이디
	private long prjId;

	// 지정기부 공지내역 제목
	private String prjNoticeSubject;

	// 지정기부 공지내용
	private String prjNoticeCn;

	// 공개여부
	private String displayYn;

	// 최초등록자 ID
	private long frstRegisterId;

	// 최초 등록 시점
	private Timestamp frstRegistPnttm;

	// 업로드 파일
	private MultipartFile[] prjNoticeFiles;

	// 상품 이미지 설명 접근성
	private List<PrjNoticeImageExplain> prjNoticeImageExplains = new ArrayList<>();

	private List<PrjNoticeFile> prjFiles = new ArrayList<>();


//	public List<PrjNoticeImageExplain> getPrjNoticeImageExplains() {
//		if (prjNoticeImageExplains == null) {
//			return null;
//		} else {
//			List<PrjNoticeImageExplain> copys = new ArrayList<>();
//			int length = prjNoticeImageExplains.size();
//			if (length > 0) {
//				for (PrjNoticeImageExplain prjNoticeImageExplain : prjNoticeImageExplains) {
//					PrjNoticeImageExplain copy = new PrjNoticeImageExplain();
//					copy.setPrjNoticeId(prjNoticeImageExplain.getPrjNoticeId());
//					copy.setImgSeq(prjNoticeImageExplain.getImgSeq());
//					copy.setImgDesc(prjNoticeImageExplain.getImgDesc());
//					copy.setFrstRegisterId(prjNoticeImageExplain.getFrstRegisterId());
//					copy.setFrstRegistPnttm(prjNoticeImageExplain.getFrstRegistPnttm());
//
//					copys.add(copy);
//				}
//			}
//			return copys;
//		}
//	}

//	public void setPrjNoticeImageExplains(List<PrjNoticeImageExplain> prjNoticeImageExplains) {
//		if (prjNoticeImageExplains == null) {
//			this.prjNoticeImageExplains = null;
//		} else {
//			this.prjNoticeImageExplains = new ArrayList<>();
//			int length = prjNoticeImageExplains.size();
//			if (length > 0) {
//				for (PrjNoticeImageExplain prjNoticeImageExplain : prjNoticeImageExplains) {
//					PrjNoticeImageExplain copy = new PrjNoticeImageExplain();
//					copy.setPrjNoticeId(prjNoticeImageExplain.getPrjNoticeId());
//					copy.setImgSeq(prjNoticeImageExplain.getImgSeq());
//					copy.setImgDesc(prjNoticeImageExplain.getImgDesc());
//					copy.setFrstRegisterId(prjNoticeImageExplain.getFrstRegisterId());
//					copy.setFrstRegistPnttm(prjNoticeImageExplain.getFrstRegistPnttm());
//
//					this.prjNoticeImageExplains.add(copy);
//				}
//			}
//		}
//	}

	public List<PrjNoticeFile> getPrjFiles() {
		if (prjFiles == null) {
			return null;
		} else {
			List<PrjNoticeFile> copys = new ArrayList<>();
			int length = prjFiles.size();
			if (length > 0) {
				for (PrjNoticeFile prjFile : prjFiles) {
					PrjNoticeFile copy = new PrjNoticeFile();
					copy.setPrjNoticeId(prjFile.getPrjNoticeId());
					copy.setFileSeq(prjFile.getFileSeq());
					copy.setFileName(prjFile.getFileName());
					copy.setOrgFileName(prjFile.getOrgFileName());
//					copy.setPathName(prjFile.getPathName());
//					copy.setFrstRegisterId(prjFile.getFrstRegisterId());
//					copy.setFrstRegistPnttm(prjFile.getFrstRegistPnttm());

					copys.add(copy);
				}
			}
			return copys;
		}
	}


	public void setPrjFiles(List<PrjNoticeFile> prjFiles) {
		if (prjFiles == null) {
			this.prjFiles = null;
		} else {
			this.prjFiles = new ArrayList<>();
			int length = prjFiles.size();
			if (length > 0) {
				for (PrjNoticeFile prjFile : prjFiles) {
					PrjNoticeFile copy = new PrjNoticeFile();
					copy.setPrjNoticeId(prjFile.getPrjNoticeId());
					copy.setFileSeq(prjFile.getFileSeq());
					copy.setFileName(prjFile.getFileName());
					copy.setOrgFileName(prjFile.getOrgFileName());
//					copy.setPathName(prjFile.getPathName());
//					copy.setFrstRegisterId(prjFile.getFrstRegisterId());
//					copy.setFrstRegistPnttm(prjFile.getFrstRegistPnttm());

					this.prjFiles.add(copy);
				}
			}
		}
	}

	public String getPrjNoticeCn() {
//		try {
//			return URLDecoder.decode(prjNoticeCn, "UTF-8");
//		} catch (NullPointerException | UnsupportedEncodingException e) {
//			return prjNoticeCn;
//		}
		if(prjNoticeCn == null) {
			return null;
		}
		try {
			return URLDecoder.decode(prjNoticeCn, "UTF-8");
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			//System.out.println("디코딩 실패1 : " + prjNoticeCn);
			e.printStackTrace();
			return prjNoticeCn;
		} catch (Exception e) {
			//System.out.println("디코딩 실패2 : " + prjNoticeCn);
			e.printStackTrace();
			return prjNoticeCn;
		}
	}

	public String getDisplayYnStr() {
		if ("Y".equals(displayYn)) {
			return "공개";
		} else {
			return "비공개";
		}
	}

	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	public String getFrstRegistPnttmStr() {
		try {
			return frstRegistPnttm.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (NullPointerException | DateTimeParseException e) {
			return "";
		}
	}

	public List<PrjNoticeFile> getOriginalPrjFiles() {
		return this.prjFiles;

	}

}

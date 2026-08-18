package saleson.shop.popup;


import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.ShopUtils;
import saleson.shop.popup.domain.Popup;
import saleson.shop.popup.domain.PopupSearchParam;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service("popupService")
public class PopupServiceImpl extends EgovAbstractServiceImpl implements PopupService {

	private static final Logger log = LoggerFactory.getLogger(PopupServiceImpl.class);


	@Autowired
	private PopupMapper popupMapper;

	@Autowired
	private SequenceService sequenceService;


	@Autowired
	private FileStorage fileStorage;

	@Autowired
	private FileService fileService;


	@Override
	public int popupCount(PopupSearchParam popupSearchParam) {
		return popupMapper.popupCount(popupSearchParam);
	}

	@Override
	public List<Popup> popupList(PopupSearchParam popupSearchParam) {
		return popupMapper.popupList(popupSearchParam);
	}

	@Override
	public Popup getPopup(int popupId) {
		return popupMapper.getPopup(popupId);
	}

	@Override
	public void insertPopup(Popup popup) {

		popup.setPopupId(sequenceService.getId("OP_POPUP"));

		if (popup.getPopupImageFile() != null && "3".equals(popup.getPopupStyle())) {
			if (popup.getPopupImageFile().getSize() > 0) {
				this.saveImage(popup);
			} else {
				popup.setImageLink("");
				popup.setBackgroundColor("");
			}
		}
		popupMapper.insertPopup(popup);
	}

	@Override
	public void updatePopup(Popup popup) {
		Popup secondPopup = popupMapper.getPopup(popup.getPopupId());

		if (popup.getPopupImageFile() != null && "3".equals(popup.getPopupStyle())) {
			if (popup.getPopupImageFile().getSize() > 0) {
				if (secondPopup.getPopupImage() != null) {
					deletePopupImage(popup.getPopupId());
				}
				this.saveImage(popup);
			} else {
				popup.setPopupImage(secondPopup.getPopupImage());
			}
		} else {
			if (secondPopup.getPopupImage() != null) {
				deletePopupImage(popup.getPopupId());
				popup.setPopupImage(null);
			}
			popup.setImageLink("");
			popup.setBackgroundColor("");
		}
		popupMapper.updatePopup(popup);
	}

	private void saveImage(Popup popup) {
		MultipartFile multipartFile = popup.getPopupImageFile();

		int maxSize = 5 * 1024 * 1024; // 업로드 가능한 최대 용량 : 5MB
		String[] availableExtensions = {"jpg", "gif", "bmp", "png", "jpeg"};

		String fileName = multipartFile.getOriginalFilename();
		String fileExtension = FileUtils.getExtension(fileName);

		if (!Arrays.asList(availableExtensions).contains(fileExtension)) {
			throw new UserException("유효하지 않은 파일입니다.");
		}

		if (maxSize < multipartFile.getSize()) {
			throw new UserException("업로드 가능한 최대 용량 : 5MB 입니다");
		}

		String defaultFileName = fileStorage.getNewFileName(fileName);

		String uploadPath = popup.getUploadPath();
		fileService.makeUploadPath(uploadPath);

		try {
			fileStorage.upload(popup.getPopupImageFile(), uploadPath + File.separator + defaultFileName);
		} catch (IOException e) {
			log.error("Save Image File Error: {}");
		}

		// 대표이미지 파일명
		popup.setPopupImage(defaultFileName);
		popup.setContent("");
	}

	@Override
	public void deletePopup(int popupId) {
		popupMapper.deletePopup(popupId);
	}

	@Override
	public void deletePopupData(ListParam listparam) {

		if (listparam.getId() != null) {

			for (String popupId : listparam.getId()) {
				popupMapper.deletePopup(Integer.parseInt(popupId));
			}
		}
	}

	@Override
	public void deletePopupImage(int popupId) {
		Popup popup = popupMapper.getPopup(popupId);

		// 1. 이미지 파일 삭제
		if (popup.getPopupImage().indexOf("/") > -1) {
			fileStorage.delete((FileUtils.getWebRootPath() + ShopUtils.unescapeHtml(popup.getPopupImage())).replaceAll("/",  File.separator));
		} else {
			String popupImageSrc = "/popup/" + popupId + "/" + ShopUtils.unescapeHtml(popup.getPopupImage());
			fileStorage.delete(popupImageSrc);
		}
		// 2. 상품 이미지 정보 업데이트.
		popupMapper.updatePopupImage(popupId);
	}

	@Override
	public List<Popup> displayPopupList() {
		return popupMapper.displayPopupList();
	}
}
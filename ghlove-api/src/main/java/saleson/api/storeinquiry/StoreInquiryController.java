package saleson.api.storeinquiry;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.file.infra.FileStorage;
import saleson.shop.storeinquiry.StoreInquiryService;
import saleson.shop.storeinquiry.domain.StoreInquiry;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("ApiStoreInquiryController")
@RequestMapping("/api")
public class StoreInquiryController {

	private static final Logger log = LoggerFactory.getLogger(StoreInquiryController.class);

	@Autowired
	StoreInquiryService storeInquiryService;

	@Autowired
	FileService fileService;

	@Autowired
	SequenceService sequenceService;

	@Autowired
	FileStorage fileStorage;

	@GetMapping("/store-inquiry")
	public ResponseEntity index(HttpServletRequest request) {
		ResponseEntity result = null;
		Map<String, Object> codes = new HashMap<>();

		try {
			List<CodeInfo> phoneCodes = CodeUtils.getCodeInfoList("PHONE");
			List<CodeInfo> emailCodes = CodeUtils.getCodeInfoList("EMAIL");

			codes.put("phoneCodes", phoneCodes);
			codes.put("emailCodes", emailCodes);

			result = ApiResponseEntity.data().put("codes", codes).ok();

		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;

	}

	@PostMapping("/store-inquiry")
	public ResponseEntity create(StoreInquiry storeInquiry) {

		ResponseEntity result = null;

		try {

			int sequenceId = sequenceService.getId("OP_STORE_INQUIRY");
			String message = "등록되었습니다.";


			if (storeInquiry.getFile() != null && storeInquiry.getFile().getSize() > 0) {

				String extension = FileUtils.getExtension(storeInquiry.getFile().getOriginalFilename());
				String fileName = storeInquiry.getFile().getOriginalFilename();
				int maxSize = 5 * 1024 * 1024; // 업로드 가능한 최대 용량 : 5MB

				final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "zip"};

				boolean extenstion_check = false;

				for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
					if (extension.equals(AVAILABLE_EXTENSION[i])) {
						extenstion_check = true;
					}
				}

				if (extenstion_check) {
					if (maxSize > storeInquiry.getFile().getSize()) {

						// 1. 업로드 경로설정
						String uploadPath = storeInquiry.getUploadPath();
						fileService.makeUploadPath(uploadPath);

						String defaultFileName = sequenceId + "." + extension;
						storeInquiry.setFileName(fileName);

						// 2. 저장될 파일
						File saveFile = new File(uploadPath + File.separator + defaultFileName);

						try {
							fileStorage.upload(storeInquiry.getFile().getBytes(), saveFile);
						} catch (IOException e) {
							log.error("ERROR: {}", e.getMessage(), e);
						}

					} else {
						message = "업로드 가능한 최대 용량 : 5MB 입니다";
//						return ViewUtils.getView("/store-inquiry/inquiry", message);
					}
				} else {
					message = "유효하지 않은 파일입니다.";
//					return ViewUtils.getView("/store-inquiry/inquiry", message);
				}
			}

			// 이모티콘 제거 (얼굴 모양, 등등)
//			storeInquiry.setCompany(EmojiUtils.removeEmoticon(storeInquiry.getCompany()));
//			storeInquiry.setUserName(EmojiUtils.removeEmoticon(storeInquiry.getUserName()));
//			storeInquiry.setHomepage(EmojiUtils.removeEmoticon(storeInquiry.getHomepage()));
//			storeInquiry.setContent(EmojiUtils.removeEmoticon(storeInquiry.getContent()));

			storeInquiry.setStoreInquiryId(sequenceId);
			storeInquiryService.insertStoreInquiry(storeInquiry); //입점문의 등록


			result = ApiResponseEntity.data().put("status", HttpStatus.OK).put("message", message).ok();

		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}


		return result;
	}
}

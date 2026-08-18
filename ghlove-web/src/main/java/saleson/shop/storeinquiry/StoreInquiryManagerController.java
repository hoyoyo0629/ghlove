package saleson.shop.storeinquiry;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.common.configuration.SalesonProperty;
import saleson.common.file.infra.FileStorage;
import saleson.shop.storeinquiry.domain.StoreInquiry;
import saleson.shop.storeinquiry.support.StoreInquiryParam;

import java.io.IOException;
import java.util.StringTokenizer;

@Controller
@RequestMapping("/opmanager/store-inquiry")
@RequestProperty(title="입첨문의", layout="default", template="opmanager")
public class StoreInquiryManagerController {

	@Autowired
	StoreInquiryService storeInquiryService;

	@Autowired
	FileService fileService;

	@Autowired
	FileStorage fileStorage;

	@Autowired
	SequenceService sequenceService;

	@GetMapping("/list")
	public String list (@ModelAttribute StoreInquiryParam storeInquiryParam, Model model) {

		Pagination pagination = Pagination.getInstance(storeInquiryService.getStoreInquiryCount(storeInquiryParam));
		storeInquiryParam.setPagination(pagination);

		model.addAttribute("list", storeInquiryService.getStoreInquiryList(storeInquiryParam));
		model.addAttribute("totalCount", storeInquiryParam.getPagination().getTotalItems());
		model.addAttribute("pagination",pagination);

		return "view:/store-inquiry/list";
	}

	@GetMapping("/detail/{storeInquiryId}")
	public String detail (@PathVariable("storeInquiryId") int storeInquiryId, Model model) {

		StoreInquiry storeInquiry = storeInquiryService.getStoreInquiryByFileName(storeInquiryId);
		String fileName = storeInquiry.getFileName();

		String extension = "";

		if(fileName != null && !"".equals(fileName)){
			StringTokenizer tokens = new StringTokenizer(fileName);
			String subFileName = tokens.nextToken(".");
			extension = tokens.nextToken(".");
		}

		model.addAttribute("extension", extension);
		model.addAttribute("storeInquiry", storeInquiryService.getStoreInquiry(storeInquiryId));
		return "view:/store-inquiry/form";
	}

	@PostMapping("/detail/{storeInquiryId}")
	public String update (StoreInquiry storeInquiry, Model model) {

		storeInquiryService.updateStoreInquiryStatus(storeInquiry);
		model.addAttribute("storeInquiry", storeInquiryService.getStoreInquiry(storeInquiry.getStoreInquiryId()));

		return "redirect:/opmanager/store-inquiry/detail/" + storeInquiry.getStoreInquiryId();
	}

	@GetMapping(value = "/download-file")
	public ResponseEntity<byte[]> downloadImage(StoreInquiry storeInquiry, @RequestParam(value="extension") String extension) {

		ResponseEntity<byte[]> responseEntity = null;

		try {

			String imageUrl = new StringBuilder()
					.append(SalesonProperty.getSalesonUrlCdn())
					.append(SalesonProperty.getUploadBaseFolder())
					.append("/")
					.append("store-inquiry")
					.append("/")
					.append(storeInquiry.getStoreInquiryId() + "." + extension)
					.toString();

			responseEntity = fileStorage.download(imageUrl);
		} catch (IOException e) {
			// System.out.println("DOWNLOAD ERROR : " + e.getMessage());
			throw new UserException("파일이 존재하지 않습니다.");
		}

		return responseEntity;
	}
}

package saleson.shop.banner;

import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.banner.domain.MainBannerManager;
import saleson.shop.banner.support.MainBannerManagerSearchParam;

@Controller
@RequestMapping("/opmanager/user-login-banner")
@RequestProperty(title="메인 배너 관리", layout="default", template="opmanager")
public class MainBannerManagerController {
	private static final Logger log = LoggerFactory.getLogger(MainBannerManagerController.class);
	
	/** 메인 배너 Service */
	@Autowired
	private MainBannerService mainBannerService;
	
	/**
	 * 메인 배너 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String mainBanner(MainBannerManagerSearchParam searchParam, Model model){
		model.addAttribute("list", mainBannerService.getMainBannerList(searchParam));
		return ViewUtils.getView("/banner/main/list");
	}
	
	/**
	 * 메인 배너 관리 등록
	 * @return
	 */
	@GetMapping("/create")
	public String mainBannerCreate(){
		return "view:/banner/main/form";
	}
	
	/**
	 * 메인 배너 관리 등록 처리
	 * @param mainBannerManager
	 * @return
	 */
	@PostMapping("/create")
	public JsonView mainBannerCreateProcess(MainBannerManager mainBannerManager) {
		int nResult = 0;
		
		try {
			nResult = mainBannerService.insertMainBanner(mainBannerManager);
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", getClass().getName() + " :: mainBannerCreateProcess Exception ===========");
		}
		
		return JsonViewUtils.success(nResult > 0 ? "SUCC" : "FAIL");
	}
	
	/**
	 * 메인 배너 순서 변경
	 * @param mainBannerManager
	 * @return
	 */
	@PostMapping("/change-display-order")
	public JsonView changeDisplayOrder(MainBannerManager mainBannerManager) {
		int nResult = 0;
		
		try {
			nResult = mainBannerService.updateDisplayOrder(mainBannerManager);
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", getClass().getName() + " :: changeDisplayOrder Exception ===========");
		}
		
		return JsonViewUtils.success(nResult > 0 ? "SUCC" : "FAIL");
	}
	
	/**
	 * 메인 배너 관리 수정
	 * @return
	 */
	@GetMapping("/edit/{bannerId}")
	public String mainBannerCreate(@PathVariable Integer bannerId, Model model) {
		MainBannerManager details = mainBannerService.getMainBannerDetails(bannerId);
		model.addAttribute("details", details);
		model.addAttribute("pageValidFlag", (details == null) ? "N" : "Y");
		return "view:/banner/main/form";
	}
	
	/**
	 * 메인 배너 파일 삭제
	 * @param mainBannerManager
	 * @return
	 */
	@PostMapping("/delete-file")
	public JsonView deleteMainBannerFile(MainBannerManager mainBannerManager) {
		int nResult = 0;
		
		try {
			nResult = mainBannerService.deleteMainBannerFile(mainBannerManager);
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", getClass().getName() + " :: deleteMainBannerFile Exception ===========");
		}
		
		return JsonViewUtils.success(nResult > 0 ? "SUCC" : "FAIL");
	}
	
	/**
	 * 메인 배너 관리 수정 처리
	 * @param mainBannerManager
	 * @return
	 */
	@PostMapping("/edit")
	public JsonView mainBannerEditProcess(MainBannerManager mainBannerManager) {
		int nResult = 0;
		
		try {
			nResult = mainBannerService.updateMainBanner(mainBannerManager);
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", getClass().getName() + " :: mainBannerEditProcess Exception ===========");
		}
		
		return JsonViewUtils.success(nResult > 0 ? "SUCC" : "FAIL");
	}
	
	@GetMapping("/mainBanner/{type}/{bannerId}")
	public ResponseEntity<Resource> imageViewerToByte(@PathVariable String type,@PathVariable Integer bannerId) {
		
		// 1. 직인정보 가져오기 
		MainBannerManager details = mainBannerService.getMainBannerDetails(bannerId);
		
		if (details == null) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		
		HttpHeaders header = new HttpHeaders();
		
		Resource resource = null;
		String mimd = "";
		
		try {
			
			
			if ("pc".equals(type)) {
				resource = new FileSystemResource(details.getPcFullFilePath());
				mimd = URLConnection.guessContentTypeFromName(details.getPcFileName());				
			} else {
				resource = new FileSystemResource(details.getmFullFilePath());
				mimd = URLConnection.guessContentTypeFromName(details.getmFileName());				
			} 
			
			header.add("Content-Type", mimd);
			
			if (!resource.exists()) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			
			
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
			
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " imageViewerToByte error2", e);
		}
		
		return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		
	}
}

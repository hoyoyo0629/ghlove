package saleson.api.representativebanner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.representativebanner.support.RepresentativeBannerDataSupport;
import saleson.shop.representativebanner.RepresentativeBannerService;
import saleson.shop.representativebanner.domain.RepresentativeBanner;
import saleson.shop.representativebanner.domain.RepresentativeBannerListParam;

@RestController("RepresentativeBannerController")
@RequestMapping("/api/representative-banner")
public class RepresentativeBannerController {
	
	private static final Logger log = LoggerFactory.getLogger(RepresentativeBannerController.class);

	@Autowired
	private RepresentativeBannerService representativeBannerService;
	
	@Autowired
	private RepresentativeBannerDataSupport representativeBannerDataSupport;
	
	/**
	 * 대표 배너 조회
	 * */
	@GetMapping("/select")
	public ResponseEntity<?> select(HttpServletRequest request, RepresentativeBannerListParam listParam) {
		ResponseEntity<?> result = null;
		try {
			List<RepresentativeBanner> list = representativeBannerService.getRepresentativeBannerListFront(listParam);
			
			result = ApiResponseEntity.data()
					.list(representativeBannerDataSupport.representativeBannerDataSet(list))
					.ok();
			
		} catch (RuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		
		return result;
	}
	
}

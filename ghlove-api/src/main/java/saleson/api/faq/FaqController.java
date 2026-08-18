package saleson.api.faq;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.enumeration.mapper.EnumMapper;
import saleson.common.utils.CommonUtils;
import saleson.model.Faq;
import saleson.shop.faq.FaqDto;
import saleson.shop.faq.FaqService;

@RestController("ApiFaqController")
@RequestMapping("/api/faq")
public class FaqController {
	private static final Logger log = LoggerFactory.getLogger(FaqController.class);

	@Autowired
	FaqService faqService;

	@Autowired
	EnumMapper enumMapper;

	@GetMapping("")
	public ResponseEntity<?> list(FaqDto faqDto, @PageableDefault(size = 10, sort = "updated", direction = Sort.Direction.DESC) Pageable pageable) {
		ResponseEntity<?> result = null;
		Page<Faq> content = null;

		try {

			// 1. 사용여부 셋팅
			faqDto.setUseYn("Y");

			// 2. FAQ 목록 조회
			content = faqService.findAll(faqDto.getPredicate(), pageable);

			// 3. 결과
			result = ApiResponseEntity
						.data()
						.put("content", content)
						.put("types", enumMapper.get("FaqType"))
						.ok();

		} catch (RuntimeException e) {
			log.error("[/api/faq] ERROR : FAQ 목록 조회 에러 {}",e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("[/api/faq] ERROR : FAQ 목록 조회 에러");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * FAQ 조회수 증가
	 * @param noticeParam
	 * @return
	 */
	@PostMapping("/hits")
	public ResponseEntity<?> updateHits(@RequestBody @Valid Faq faq) {
		ResponseEntity<?> result = null;

		try {

			// 1. 데이터 조회
			Faq detail = faqService.findById(faq.getId()).get();

			// 2. 조회수 증가
			if(detail != null) {
				detail.setHit(CommonUtils.intNvl(detail.getHit()) + 1);
				faqService.save(detail);
			}

			// 3. 결과
			result = ApiResponseEntity.data().ok();

		} catch (RuntimeException e) {
			log.error("[/api/faq/hits] ERROR : FAQ 조회수 증가 에러 {}",e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("[/api/faq/hits] ERROR : FAQ 조회수 증가 에러");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
}

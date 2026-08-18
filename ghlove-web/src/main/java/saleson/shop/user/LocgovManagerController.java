package saleson.shop.user;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.donation.DonationVerification;
import saleson.shop.user.domain.ContributionSetup;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.support.ContributionSetupSearchParam;
import saleson.shop.user.support.LocgovDeptSearchParam;
import saleson.shop.user.support.LocgovSearchParam;

@Controller
@RequestMapping("/opmanager/user/locgov")
@RequestProperty(title = "지자체 관리",  layout = "default", template="opmanager")
@RequiredArgsConstructor
public class LocgovManagerController {
	private static final Logger log = LoggerFactory.getLogger(LocgovManagerController.class);

	/** 지자체관리 Service */
	@Autowired
	private LocgovService locgovService;

	/** 공통코드 Service */
	@Autowired
	private CodeService codeService;

	@Autowired
	private DonationVerification donationVerification;

	/**
	 * 지자체관리 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String locgovManagerList(LocgovSearchParam searchParam,  Model model) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		// 1. 시스템&행안부 관리자인 경우만 목록 정보 조회
		if("SYS".equals(adminRole)) {
			// 지자체관리 목록 총 갯수 조회
			int locgovCount = 0;

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(locgovCount);
			searchParam.setPagination(pagination);

			model.addAttribute("pagination"	, pagination);
			model.addAttribute("count"		, locgovCount);
			model.addAttribute("list"		, Collections.EMPTY_LIST);
			model.addAttribute("searchParam", searchParam);
			return "view:/user/locgov/list";

		// 2. 지자체 관리자인 경우 등록 또는 수정페이지로 이동
		} else if("LOC".equals(adminRole)) {

			// 2-1. 로그인 사용자 지자체 코드 조회
			Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());

			// 2-2. 지자체 등록 여부 확인
			int locgovCount = 0;
			if(locgovCodeDetails != null) {
				searchParam.setLocgovCode(locgovCodeDetails.getId());
				locgovCount = locgovService.getLocgovCountByParam(searchParam);
			}

			// 2-3. 등록되었으면 수정페이지로 이동
			if(locgovCount > 0) {
				return ViewUtils.redirect("/opmanager/user/locgov/edit/"+locgovCodeDetails.getId());
			} else {
				return ViewUtils.redirect("/opmanager/user/locgov/create");
			}

		// 3. 오프라인 관리자인 경우는 메인 페이지로 이동
		}else {
			return ViewUtils.redirect("/opmanager");
		}
	}

	@PostMapping("/list")
	public String locgovManagerListPost(LocgovSearchParam searchParam,  Model model) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		// 1. 시스템&행안부 관리자인 경우만 목록 정보 조회
		if("SYS".equals(adminRole)) {
			// 지자체관리 목록 총 갯수 조회
			int locgovCount = locgovService.getLocgovCountByParam(searchParam);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(locgovCount);
			searchParam.setPagination(pagination);

			model.addAttribute("pagination"	, pagination);
			model.addAttribute("count"		, locgovCount);
			model.addAttribute("list"		, locgovService.getLocgovListByParam(searchParam));
			model.addAttribute("searchParam", searchParam);
			return "view:/user/locgov/list";

		// 2. 지자체 관리자인 경우 등록 또는 수정페이지로 이동
		} else if("LOC".equals(adminRole)) {

			// 2-1. 로그인 사용자 지자체 코드 조회
			Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());

			// 2-2. 지자체 등록 여부 확인
			int locgovCount = 0;
			if(locgovCodeDetails != null) {
				searchParam.setLocgovCode(locgovCodeDetails.getId());
				locgovCount = locgovService.getLocgovCountByParam(searchParam);
			}

			// 2-3. 등록되었으면 수정페이지로 이동
			if(locgovCount > 0) {
				return ViewUtils.redirect("/opmanager/user/locgov/edit/"+locgovCodeDetails.getId());
			} else {
				return ViewUtils.redirect("/opmanager/user/locgov/create");
			}

		// 3. 오프라인 관리자인 경우는 메인 페이지로 이동
		}else {
			return ViewUtils.redirect("/opmanager");
		}
	}

	/**
	 * 지자체관리 목록 > 포인트 목록 조회(팝업)
	 * @param locgovCode
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/point/list/{locgovCode}")
	@RequestProperty(title = "지자체 관리", layout = "base")
	public String pointList(ContributionSetupSearchParam searchParam, Model model
			, @PathVariable("locgovCode") String locgovCode) {

		// 포인트 목록 총 갯수 조회
		int locgovPointCount = locgovService.getLocgovPointCountBySearchParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(locgovPointCount);
		searchParam.setPagination(pagination);

		// 공통 코드 조회 검색값 셋팅
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("YYYY");

		model.addAttribute("pagination"		, pagination);
		model.addAttribute("searchParam"	, searchParam);
		model.addAttribute("count"			, locgovPointCount);
		model.addAttribute("list"			, locgovService.getLocgovPointListBySearchParam(searchParam));
		model.addAttribute("yearCodeList"	, codeService.getCodeChildList(codeParam));

		return ViewUtils.getView("/user/popup/locgov-point-list");
	}

	/**
	 * 지자체 삭제
	 * @param contributionSetup
	 * @return
	 */
	@PostMapping("/delete")
	public JsonView locgovDelete(Locgov locgov) {
		String code = "";

		try {
			code = locgovService.deleteLocgov(locgov);
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========== locgovDelete RuntimeException ===========");
		}

		return JsonViewUtils.success(code);
	}

	/**
	 * 지자체 등록
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String locgovCreate(Model model) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		// 0. 공통코드 조회 파라미터 셋팅
		CodeParam codeParam = new CodeParam();

		// 1. 관리자 : 지자체(시도) 공통코드 조회 / 지자체 : 본인이 속한 지자체 정보
		if("SYS".equals(adminRole)) {
			codeParam.setCodeType("WDR");
			model.addAttribute("upperLocgovCodeList", codeService.getCodeChildList(codeParam));

		} else if("LOC".equals(adminRole)) {
			model.addAttribute("userLocgovCodeDetails", locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId()));
		}

		// 2. 연락처 공통코드 (핸드폰)
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		// 3. 연락처 공통코드 (전화번호)
		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		// 4. 명예회원 명칭 공통코드
		codeParam.setCodeType("HONOR_STD");
		model.addAttribute("honorCodeList", codeService.getCodeChildList(codeParam));

		// 5. 한도금 공통코드
		model.addAttribute("limitAmt", Integer.parseInt(donationVerification.donationLimitAmt().getLabel()));
		model.addAttribute("limitAmtString", donationVerification.donationLimitAmt().getDetail());

		model.addAttribute("adminRole", adminRole);
		return "view:/user/locgov/form";
	}

	/**
	 * 지자체 등록 처리
	 * @param locgov
	 * @return
	 */
	@PostMapping("/create")
	public JsonView locgovCreateProcess(
			Locgov locgov,
    		@RequestParam(value="offcsFile", required=false) MultipartFile offcsFile,
			@RequestParam(value="addPcFile", required=false) MultipartFile addPcFile,
			@RequestParam(value="addMbFile", required=false) MultipartFile addMbFile) {
		String code = "";
		try {
			locgov.setFrstRegisterId(UserUtils.getUser().getUserId());
			locgov.setLastUpdusrId(UserUtils.getUser().getUserId());
			locgov.setOffcsFiles(offcsFile);

			locgov.setAddPcFile(addPcFile);
			locgov.setAddMbFile(addMbFile);

			code = locgovService.insertLocgov(locgov);
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== locgovCreateProcess RuntimeException ===========");
		}

		return JsonViewUtils.success(code);
	}

	/**
	 * 지자체 등록 처리
	 * @param locgov
	 * @return
	 */
	@PostMapping("/test")
	public String test(@RequestParam(value="fileTest", required=false) MultipartFile fileTest) {

		try {
			System.out.println("test");
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== locgovCreateProcess RuntimeException ===========");
		}

		return "view:/user/locgov/form";
	}

	/**
	 * 지자체 수정
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{locgovCode}")
	public String locgovEdit(Model model, @PathVariable("locgovCode") String locgovCode) {
		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		// 0. 유효성 확인 : 지자체인 경우
		if("LOC".equals(adminRole)) {
			Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());
			if(!(locgovCode != null && locgovCode.equals(locgovCodeDetails.getId()))) {
				return ViewUtils.redirect("/opmanager/user/locgov/edit/"+locgovCodeDetails.getId());
			}
		}

		// 1. 상세 정보 조회
		LocgovSearchParam searchParam = new LocgovSearchParam();
		searchParam.setLocgovCode(locgovCode);
		model.addAttribute("details", locgovService.getLocgovDetails(searchParam));

		// 2. 공통코드 조회 파라미터 셋팅
		CodeParam codeParam = new CodeParam();

		// 2-1. 연락처 공통코드 (핸드폰)
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		// 2-2. 연락처 공통코드 (전화번호)
		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		// 2-3. 년도 공통코드
		codeParam.setCodeType("YYYY");
		model.addAttribute("yearCodeList", codeService.getCodeChildList(codeParam));

		// 2-4. 명예회원 명칭 공통코드
		codeParam.setCodeType("HONOR_STD");
		model.addAttribute("honorCodeList", codeService.getCodeChildList(codeParam));

		// 3. 한도금 공통코드
		model.addAttribute("limitAmt", Integer.parseInt(donationVerification.donationLimitAmt().getLabel()));
		model.addAttribute("limitAmtString", donationVerification.donationLimitAmt().getDetail());

		ArrayList<HashMap<String, Object>> locFisSpList = new ArrayList<>();
		HashMap<String, Object> fisSpMap = new HashMap<>();

		if(locgovCode.substring(2).equals("000")) {
			fisSpMap = new HashMap<>();
			fisSpMap.put("id", "31");
			fisSpMap.put("label", "일반회계");
			locFisSpList.add(fisSpMap);
			fisSpMap = new HashMap<>();
			fisSpMap.put("id", "51");
			fisSpMap.put("label", "특별회계");
			locFisSpList.add(fisSpMap);
		} else {
			fisSpMap = new HashMap<>();
			fisSpMap.put("id", "41");
			fisSpMap.put("label", "일반회계");
			locFisSpList.add(fisSpMap);
			fisSpMap = new HashMap<>();
			fisSpMap.put("id", "61");
			fisSpMap.put("label", "특별회계");
			locFisSpList.add(fisSpMap);
		}

		model.addAttribute("locFisSpList", locFisSpList);
		model.addAttribute("adminRole", adminRole);
		return "view:/user/locgov/edit";
	}

	/**
	 * 지자체 수정 처리
	 * @param locgov
	 * @return
	 */
	@PostMapping(value="/edit")
	public JsonView locgovEditProcess(Locgov locgov, @RequestParam(value="offcsFile", required=false) MultipartFile offcsFile
										, @RequestParam(value="addPcFile", required=false) MultipartFile addPcFile
										, @RequestParam(value="addMbFile", required=false) MultipartFile addMbFile) {
		String code = "";

		try {
			locgov.setLastUpdusrId(UserUtils.getUser().getUserId());
			locgov.setOffcsFiles(offcsFile);

			locgov.setAddPcFile(addPcFile);
			locgov.setAddMbFile(addMbFile);

			code = locgovService.updateLocgov(locgov);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", "========== locgovEditProcess RuntimeException ==========");
		} catch (IOException e) {
			log.error("ERROR: {}", "========== locgovEditProcess IOException ==========");
		}

		return JsonViewUtils.success(code);
	}

	/**
	 * 기부금 설정 상세 조회
	 * @param stdrYear
	 * @param locgovCode
	 * @return
	 */
	@GetMapping("/point/{stdrYear}/{locgovCode}")
	public JsonView getPointDetails(@PathVariable("stdrYear") String stdrYear, @PathVariable("locgovCode") String locgovCode) {
		ContributionSetupSearchParam searchParam = new ContributionSetupSearchParam();
		searchParam.setStdrYear(stdrYear);
		searchParam.setLocgovCode(locgovCode);
		return JsonViewUtils.success(locgovService.getContributionSetupDetails(searchParam));
	}

	/**
	 * 포인트 지급률 등록/수정
	 * @param contributionSetup
	 * @return
	 */
	@PostMapping("/point/edit")
	public JsonView editPointRate(ContributionSetup contributionSetup) {
		int nResult = 0;

		try {
			ContributionSetupSearchParam searchParam = new ContributionSetupSearchParam();
			searchParam.setStdrYear(contributionSetup.getStdrYear());
			searchParam.setLocgovCode(contributionSetup.getLocgovCode());

			// 등록
			if(locgovService.getLocgovPointCountBySearchParam(searchParam) == 0) {
				contributionSetup.setFrstRegisterId(UserUtils.getUser().getUserId());
				contributionSetup.setLastUpdusrId(UserUtils.getUser().getUserId());
				nResult = locgovService.insertContributionSetup(contributionSetup);

			// 수정
			} else {
				contributionSetup.setLastUpdusrId(UserUtils.getUser().getUserId());
				nResult = locgovService.updateContributionSetup(contributionSetup);
			}
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========== editPointRate RuntimeException ===========");
		}

		return JsonViewUtils.success(nResult > 0 ? "SUCC" : "FAIL");
	}

	/**
	 * 지자체 관리 > 상세화면 > 부서코드 이력 확인
	 * @param model
	 * @param locgovCode : 지자체 코드
	 * @param userId : 유저ID
	 * @return list : 해당 인원의 기부 상세 리스트
	 */
	@GetMapping("/edit/{locgovCode}/dept/popup")
	@RequestProperty(layout = "base")
	public String deptCodeHisListPopup(Model model, @PathVariable("locgovCode") String locgovCode, LocgovDeptSearchParam params) {

		params.setLocgovCode(locgovCode);
		params.setItemsPerPage(5);

		int count = locgovService.getLocgovDeptHistListCount(params);

		Pagination pagination = Pagination.getInstance(count, params.getItemsPerPage());
		params.setPagination(pagination);

		model.addAttribute("count", count);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", locgovService.getLocgovDeptHistList(params));

		return "view:/user/locgov/popup";
	}

	@GetMapping("/sealView/{locgovCode}")
	public ResponseEntity<byte[]> imageViewerToByte(@PathVariable String locgovCode) {

		// 1. 직인정보 가져오기
		Locgov locgov = locgovService.getLocgovOffcsInfo(locgovCode);

		if (locgov == null) return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);

		HttpHeaders header = new HttpHeaders();

		String str = null;
		try {
			str = Files.readString(Paths.get(locgov.getFullFilePath()));

			String deStr = pCrypto.Decrypt("normal", str, "", 0);
			String mimd = URLConnection.guessContentTypeFromName(locgov.getOffcsFileNm());

			header.add("Content-Type", mimd);


			return new ResponseEntity<byte[]>(Hex.decode(deStr), header, HttpStatus.OK);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("imageViewerToByte IOException : {}");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("imageViewerToByte Exception : {}");
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);


	}


	/*@GetMapping("/imageView/{locgovCode}")
	public ResponseEntity<Resource> imageViewer(@PathVariable String locgovCode) {

		String adminRole = locgovService.getLoginUserAdminRoleCheck();

		if("LOC".equals(adminRole)) {
			// 2-1. 로그인 사용자 지자체 코드 조회
			Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());

			if (locgovCodeDetails == null) {
				return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			} else if (!locgovCode.equals(locgovCodeDetails.getId())) {
				return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			}
		}

		// 1. 직인정보 가져오기
		Locgov locgov = locgovService.getLocgovOffcsInfo(locgovCode);

		if (locgov == null) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

		String imagePath = locgov.getUploadPath() + "/" + locgov.getOffcsFileNm();

		Resource resuorce = new FileSystemResource(imagePath);

		if (!resuorce.exists()) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

		HttpHeaders header = new HttpHeaders();
		Path filePath = null;

		try {
			filePath = Paths.get(imagePath);
			header.add("Content-Type", Files.probeContentType(filePath));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Resource>(resuorce, header, HttpStatus.OK);


	}*/

	@PostMapping("/delete/offcs")
	public JsonView deleteOffcsFile(Locgov locgov) {

		return JsonViewUtils.success(locgovService.deleteOffcsFile(locgov));
	}

	// 지자체 배경 이미지 삭제
	@PostMapping("/delete/itemFile")
	public JsonView deleteitemFile(@RequestParam String locgovCode, @RequestParam String type) {
		if (SecurityUtils.isManager()) {
			return JsonViewUtils.success(locgovService.deleteLocgovItemImage(locgovCode, type));
		} else {
			return JsonViewUtils.failure("권한이 없습니다.");
		}
	}
}

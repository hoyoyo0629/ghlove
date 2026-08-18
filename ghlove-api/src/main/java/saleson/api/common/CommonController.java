package saleson.api.common;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.SearchParam;
import com.privacy.pCrypto;

import saleson.api.common.domain.ConfigInfo;
import saleson.api.common.enumerated.ApiError;
import saleson.api.policy.PolicyController;
import saleson.common.utils.CaptchaUtil;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.ConfigPg;
import saleson.model.Island;
import saleson.shop.accountnumber.AccountNumberService;
import saleson.shop.accountnumber.domain.AccountNumber;
import saleson.shop.analysis.AnalysisMapper;
import saleson.shop.banner.MainBannerService;
import saleson.shop.banner.domain.MainBannerManager;
import saleson.shop.cart.CartService;
import saleson.shop.cart.support.CartParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.island.IslandDto;
import saleson.shop.island.IslandService;
import saleson.shop.main.MainService;
import saleson.shop.mypage.MyPageService;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.ItemPrice;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.seo.SeoService;
import saleson.shop.seo.domain.Seo;
import saleson.shop.stats.StatsService;
import saleson.shop.stats.domain.Visit;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.wishlist.WishlistService;


@RestController("ApiCommonController")
@RequestMapping("/api/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(PolicyController.class);

    @Autowired
    private StatsService statsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private IslandService islandService;

    @Autowired
    private ConfigPgService configPgService;

    @Autowired
    private AccountNumberService accountNumberService;

    @Autowired
    private SeoService seoService;

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private LocgovService locgovService;

    @Autowired
    private MainBannerService mainBannerService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Autowired
    private MainService mainService;

    /**
     * 방문통계
     *
     * @param visit
     * @return
     */
    @PostMapping("/visit")
    public ResponseEntity visitStatistics(@RequestBody Visit visit) {
        ResponseEntity result = null;
        try {
            // 방문자 통계 저장.
            statsService.saveVisitDataForApi(visit);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/confirm-pbanc")
    public ResponseEntity<Map<String, Object>> confirmPbanc() {
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
        	Map<String, Object> resultMap = new HashMap<>();
        	UserDetail userDetailInfo = userService.getUserDetail(UserUtils.getUserId());
        	String receivePbanc = Optional.ofNullable(userDetailInfo.getReceivePbanc()).orElse("empty");
        	resultMap.put("receivePbanc",	receivePbanc);

            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (RuntimeException e) {
        	log.error("/api/common/receivePbanc RuntimeException {} ", e.getStackTrace()[0]);
        	RES_RESULT = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("/api/common/receivePbanc Exception {} ", e.getStackTrace()[0]);
        	RES_RESULT = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

        return RES_RESULT;
    }

    /**
     * 카트 정보 가져오기
     *
     * @param request
     * @return
     */
    @GetMapping("/cart-info")
    public ResponseEntity cart(HttpServletRequest request) {
        ResponseEntity result = null;
        CartParam cartParam = new CartParam();

        if (UserUtils.isUserLogin()) {
            cartParam.setUserId(UserUtils.getUserId());
        } else {
            cartParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
        }

        try {
            int cartQuantity = getCartQuantityByParam(cartParam);

            result = ApiResponseEntity.data().put("cartQuantity", cartQuantity).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 퀵 정보 가져오기
     *
     * @return
     */
    @GetMapping("/quick-info")
    public ResponseEntity quickInfo(HttpServletRequest request) {
        ResponseEntity result = null;

        try {

            Map<String, Object> map = new LinkedHashMap<>();

            int wishlistCount = 0;
            int cartQuantity = 0;

            CartParam cartParam = new CartParam();

            if (UserUtils.isUserLogin()) {
                cartParam.setUserId(UserUtils.getUserId());

                UserCouponParam userCouponParam = new UserCouponParam();
                userCouponParam.setUserId(UserUtils.getUserId());
                wishlistCount = wishlistService.getWishlistCountByUserId(UserUtils.getUserId());
            } else {
                cartParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            }

            cartQuantity = getCartQuantityByParam(cartParam);

            map.put("cartQuantity", cartQuantity);
            map.put("wishlistCount", wishlistCount);

            result = ApiResponseEntity.data().map(map).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    private int getCartQuantityByParam(CartParam cartParam) {
        int cartQuantity = 0;

        List<BuyItem> cartList = cartService.getCartList(cartParam, false);

        for (BuyItem buyItem : cartList) {
        	if (buyItem != null) {
                ItemPrice itemPrice = buyItem.getItemPrice();
                if (itemPrice != null) {
                    cartQuantity += itemPrice.getQuantity();
                }
        	}
        }

        return cartQuantity;
    }

    /**
     * 은행 정보 가져오기
     *
     * @return
     */
    @GetMapping("/bank-info")
    public ResponseEntity getBankInfo() {
        ResponseEntity result = null;

        try {
        	List<Map> maps = new ArrayList<>();
            ConfigPg configPg = configPgService.getConfigPg();

            if(configPg != null && configPg.getPgType() != null) {
            	List<CodeInfo> codeInfos = ShopUtils.getBankListByKey(CommonUtils.dataNvl(configPg.getPgType().getCode()));


            	codeInfos.stream().forEach(codeInfo -> {
            		try {
            			Map<String, String> map = new LinkedHashMap();

            			map.put("key", codeInfo.getKey().getId());
            			map.put("label", codeInfo.getLabel());

            			maps.add(map);

            		} catch (NullPointerException | OpRuntimeException ignore) {
            			log.error("getBankInfo error :: ", ignore);
            		}
            	});
        	}

            result = ApiResponseEntity.data().list(maps).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }


    /**
     * 회사 소개 , 사업자정보확인 (footer popup)
     *
     * @return
     */
    @GetMapping("/about-us")
    public ResponseEntity aboutUs() {

        ResponseEntity result = null;

        try {
            Config config = ShopUtils.getConfig();

            result = ApiResponseEntity.data().put("about", new ConfigInfo(config)).ok();

        } catch (OpRuntimeException e) {
            log.error("about error", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 회사 소개
     *
     * @return
     */
    @GetMapping("/island-info")
    public ResponseEntity island(IslandDto islandDto,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        ResponseEntity result = null;

        try {


            Page<Island> pageContent = islandService.findAll(islandDto.getPredicate(), pageable);

            result = ApiResponseEntity.data().put("pageContent", pageContent).ok();

        } catch (OpRuntimeException e) {
            log.error("about error", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 온라인 입금계좌
     *
     * @return
     */
    @GetMapping("/account-numbers")
    public ResponseEntity accountNumbers() {

        ResponseEntity result = null;

        try {
            List<AccountNumber> accountNumbers = accountNumberService.getUseAccountNumberListAll();
            result = ApiResponseEntity.data().put("accountNumbers", accountNumbers).ok();

        } catch (OpRuntimeException e) {
            log.error("bank-list error", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/seo")
    public ResponseEntity shopSeo(@RequestParam(name = "uri", defaultValue = "") String uri) {

        Seo seo = seoService.getShopConfigSeo();

        if (StringUtils.hasText(uri)) {

            SearchParam param = new SearchParam();
            param.setWhere("URL");
            param.setQuery(uri);

            if (uri.startsWith("/items/details.html")) {
                seo = seoService.getSeoByItem(param);
            } else if (uri.startsWith("/category/")) {
                seo = seoService.getSeoByCategory(param);
            } else {

                List<Seo> seoList = seoService.getSeoList(param);

                for (Seo seo2 : seoList) {
                    if (seo2.getSeoUrl().equalsIgnoreCase(uri)) {


                        seo.setTitle(seo2.getTitle());
                        seo.setKeywords(seo2.getKeywords());
                        seo.setDescription(seo2.getDescription());
                        seo.setHeaderContents1(seo2.getHeaderContents1());
                        seo.setThemawordTitle(seo2.getThemawordTitle());
                        seo.setThemawordDescription(seo2.getThemawordDescription());
                        seo.setIndexFlag(seo2.getIndexFlag());
                        break;
                    }
                }
            }
        }

        return ApiResponseEntity.data().put("seo", seo).ok();
    }

    @GetMapping("/userStatusInfo")
    public ResponseEntity details(HttpServletRequest request, Model model){
        ResponseEntity result = null;

        try {

      	  	//기부내역 현황(실 납부액 기준)
        	CntrParam cntrParam = new CntrParam();
      	  	cntrParam.setUserId(UserUtils.getUser().getUserId());
      	  	int totalCntrAmt = myPageService.getTotalCntrAmt(cntrParam);

      	  	//총 잔여 포인트
      	  	CntrPointParam gntrPointParam = new CntrPointParam();
    		gntrPointParam.setUserId(UserUtils.getUser().getUserId());
            int blcePointTotal = myPageService.blcePointTotal(gntrPointParam);

            result = ApiResponseEntity.data()
            		.put("userName", UserUtils.getUser().getUserName())
            		.put("totalCntrAmt", totalCntrAmt)
            		.put("blcePointTotal", blcePointTotal)
            		.ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
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
			log.error(getClass().getName() + " imageViewerToByte error1", e);
		} catch (Exception e) {
			log.error(getClass().getName() + " imageViewerToByte error2", e);
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);

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

		} catch (RuntimeException e) {
			log.error(getClass().getName() + " imageViewerToByte error2", e);
		}

		return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

	}

	@GetMapping("/captcha/img")
	public ResponseEntity captchaImg(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity result = null;

		Map<String, Object> captchaMap = new CaptchaUtil().captchaImg(request, response);

		if (captchaMap != null) {
			result = ApiResponseEntity.data()
	            		.put("data", captchaMap)
	            		.ok();
			return result;
		} else {
			return result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

	}

	@GetMapping("/captcha/audio")
	public void captchaAudio(HttpServletRequest request, HttpServletResponse response) {
		new CaptchaUtil().captchaAudio(request, response);
	}

	@GetMapping("/alternate")
	public ResponseEntity alternate(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity result = null;

		try {
            result = ApiResponseEntity.data()
            		.put("step", configService.selectAlternateSystem())
            		.ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;

	}

	@GetMapping("/serverTime")
	public ResponseEntity serverTime(HttpServletRequest request, HttpServletResponse response) {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String serverTime = sdf.format(now);

		ResponseEntity result = null;

		try {
			result = ApiResponseEntity.data().put("serverTime", serverTime).ok();
		}catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
	}

	// 메인 화면 총 기부금 현황 조회
	@GetMapping("/getGiveState")
	public ResponseEntity getGiveState(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity result = null;

		try {
			/* AS-IS */
//			LocalDate now = LocalDate.now();

//			// TODO 예외처리 필요
//			Map<String, Object> responseData = mainService.nowDayGramt();
//
//			// 배치가 돌지 않았을 경우의 예외 처리
//			if(responseData == null) {
//				responseData = new HashMap<>();
//				// 금년 1월 1일 ~ 현재 -1일 총 기부금 조회
//				responseData.put("nowYearTotalAmt", mainService.getGiveTotalAmt("now"));
//				// 작년 1월 1일 ~ 작년 -1일 총 기부금 조회
//				responseData.put("prevYearTotalAmt", mainService.getGiveTotalAmt("prev"));
//			}
//
//			// 전일 : 현재 - 1일
//			responseData.put("stdDay", now.minusDays(1).toString().replace("-", "."));
//
//			// D-Day : 올해까지 남은 날짜
//			Long dDay = ChronoUnit.DAYS.between(now, LocalDate.of(now.getYear(), 12, 31));
//			responseData.put("dDay", dDay);
//
//			// nowDayPercent : 오늘까지 경과 퍼센테이지 (1일 ~ 현재 / 1년)
//			boolean isLeap = Year.isLeap(Year.now().getValue()); // 윤년 확인
//			int totalDay = isLeap ? 366 : 365;
//			Long nowDayPercent = (long)(((double)totalDay - dDay) / totalDay * 100);
//			responseData.put("nowDayPercent", nowDayPercent);

			// 서비스 로직 컨트롤러 → 서비스로 변경
			// AS-IS의 총 기부금 현황은 단순 Select이긴 하나 DB Connection이 매번 발생
			// 전일 합계의 고정 데이터로, static 변수로 고정해두면 서버가 리스타트 된 이후 최초 1번만 DB에 접근하고
			// 이후 DB 접근 없이 서버의 static 변수를 return 함으로써 DB의 부하 감소 기대
			Map<String, Object> responseData = mainService.getGiveStateByCache();

			result = ApiResponseEntity.data().put("data", responseData).ok();
		}catch(OpRuntimeException e) {
			log.error(e.toString());
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	// 유저 정보 조회(총기부액 / 올해 총 기부액 / 잔여포인트 / 성인여부)
	@GetMapping("/userInfo")
    public ResponseEntity userInfo(HttpServletRequest request, Model model){
        ResponseEntity result = null;

        try {

      	  	//기부내역 현황(실 납부액 기준)
        	CntrParam cntrParam = new CntrParam();
      	  	cntrParam.setUserId(UserUtils.getUser().getUserId());
      	  	int totalCntrAmt = myPageService.getTotalCntrAmt(cntrParam);

      	  	/* 올해 기부내역 현황 <기존 userStatusInfo 메서드에서 변경된 추가된 내용> */
      	  	int userThisYearDonationAmt = myPageService.getThisYearTotalCntrAmt(UserUtils.getUser().getUserId());
      	  	/* 올해 기부내역 현황 <기존 userStatusInfo 메서드에서 변경된 추가된 내용> */

      	  	//총 잔여 포인트
      	  	CntrPointParam gntrPointParam = new CntrPointParam();
    		gntrPointParam.setUserId(UserUtils.getUser().getUserId());
            int userDonationPoint = myPageService.blcePointTotal(gntrPointParam);

            //성인 여부 체크
            boolean check = UserUtils.isAdult();
            String AdultYn = "N";
            if(check) {
            	AdultYn = "Y";
            }

            result = ApiResponseEntity.data()
            		.put("userName", UserUtils.getUser().getUserName())
            		.put("userTotalDonationAmt", totalCntrAmt)
            		.put("userDonationPoint", userDonationPoint)
            		.put("userThisYearDonationAmt", userThisYearDonationAmt)
            		.put("adultYn", AdultYn)
            		.ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

	@GetMapping("/getRealTimeWaitUser")
    public ResponseEntity getRealTimeWaitUser(HttpServletRequest request, Model model){
        ResponseEntity result = null;

        try {
        	// 넷퍼넬 API 요청
        	Map<String, Object> map = mainService.getRealTimeWaitUser();

            result = ApiResponseEntity.data()
            		.put("actionList", map.get("actionList"))
            		.ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }
}

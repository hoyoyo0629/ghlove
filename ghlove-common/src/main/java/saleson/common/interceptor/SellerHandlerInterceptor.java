package saleson.common.interceptor;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.util.AjaxUtils;
import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ThreadContextUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;

import saleson.common.context.SellerContext;
import saleson.common.utils.UserUtils;
import saleson.model.user.SellerUserLogin;
import saleson.seller.menu.MenuService;
import saleson.seller.menu.SellerMenu;
import saleson.seller.user.SellerUserService;



public class SellerHandlerInterceptor implements HandlerInterceptor {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SecurityService securityService;

	@Autowired
	private MenuService sellerMenuService;

	@Autowired
	private PathMatcher antPathMatcher;


    @Autowired
    private SellerUserService sellerUserService;


	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (CommonUtils.isResourceHandler(handler)) {
			return true;
		}


		String requestUri = RequestContextUtils.getRequestUri();

		if (requestUri.startsWith("/seller/login")
			|| requestUri.startsWith("/seller/login-admin")
			|| requestUri.startsWith("/seller/logout")
			|| requestUri.startsWith("/seller/user/pki/form")
			|| requestUri.startsWith("/seller/change-password")
			|| requestUri.startsWith("/seller/login-seller")
			|| requestUri.startsWith("/seller/magicline/signedFormRGhlove")
			|| requestUri.startsWith("/seller/user/pki/update")
			|| requestUri.startsWith("/seller/user/pki/login-user-valid")
            || requestUri.startsWith("/seller/user/sci-result")
			) {
			return true;
		}

		if (!isLogin(request)) {

			if (SecurityUtils.hasRole("ROLE_OPMANAGER")) {		// 다른 권한 있을 경우 권한 삭제
				List<String> roles = SecurityUtils.getAuthorities();
				int length = roles.size();
				for(int i = length - 1 ; i >= 0 ; i--) {
					String role = roles.get(i);
					if (role.contains("ROLE_USER")
							|| role.contains("ROLE_ADMIN_")
							|| role.contains("ROLE_OPMANAGE")) {
						roles.remove(i);
					}
				}

				SecurityContextHolder.getContext().setAuthentication(null);
				response.sendRedirect(request.getContextPath() + "/seller/login?error=70");
				return false;
			}

			boolean isAjax = "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
			if (isAjax) {

				String ajaxErrorMessage = JsonViewUtils.objectToJson(JsonViewUtils.failure("로그인 세션이 만료되어, 재 로그인해주시기 바랍니다."));
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getOutputStream().write(ajaxErrorMessage.getBytes("UTF-8"));

				return false;
			}

			response.sendRedirect(request.getContextPath() + "/seller/login?target=" + RequestContextUtils.getRequestUri());
			return false;
		}

		//중복로그인 추가(2022.11.01)
		//판매자화면 바로 이동시 중복로그인 체크 안함 추가 - 2023.01.26
		HttpSession session = request.getSession();
		if (isLogin(request) && session.getAttribute("SHADOW_SELLER") == null) {
			List<SellerUserLogin> loginSessionList = sellerUserService.getLoginSessionByUserId(UserUtils.getSellerUserId());
			String sessionId = request.getSession().getId();

			long loginSessionCount = loginSessionList.stream()
					.filter(m -> sessionId.equals(m.getSessionId()))
					.count();

			if (loginSessionCount == 0) {
				log.debug("중복 사용자 체크로 인한 로그아웃");
				response.sendRedirect("/op_security_logout?target=/seller/login-disconnect");
				return false;
			}
		}


		return true;
	}

	public void postHandle(
		HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
		throws Exception {
		if (!CommonUtils.isResourceHandler(handler)
			&& isLogin(request)) {


			SellerContext sellerContext = new SellerContext(request);

			// 판매관리자 정보
            sellerContext.setSellerUserLogin(UserUtils.isSellerLogin());
            sellerContext.setSellerMasterUserLogin(UserUtils.isSellerMasterUser());
            sellerContext.setSellerUser(UserUtils.getUser());

            if (sellerContext.getSeller() == null) {
            	throw new UserException("판매관리자 메뉴 접근 권한이 없습니다.", "/seller/");
            }

            // 권한이 있는가?
			RequestContext requestContext = ThreadContextUtils.getRequestContext();
			List<String> menuCodes = CommonUtils.getMenuCode(requestContext.getRequestUri());

			HashMap<String, Object> map = new HashMap<>();

			map.put("authority", "ROLE_SUPERVISOR");
			map.put("menuCodes", menuCodes);
			map.put("userId", sellerContext.getSeller().getSellerId());

			// 메뉴 코드
			log.debug("[cache] menuService.getMenuCode");
			String menuCode = sellerMenuService.getMenuCode(map);


			if (ValidationUtils.isNull(menuCode) &&
				!(requestContext.getRequestUri().equals("/seller") || requestContext.getRequestUri().equals("/seller/"))) {

				if (isLoadMenuPage(requestContext)) {
					throw new UserException("판매관리자 메뉴 접근 권한이 없습니다.", "/seller/");
				}
			}

			if (request.getMethod().equals("GET") || "POST".equals(request.getMethod())) {

				Menu menu = new Menu();
				menu.setCacheKey(Long.toString(sellerContext.getSeller().getSellerId()));
				menu.setUserId(sellerContext.getSeller().getSellerId());
				menu.setMenuCode(menuCode);



				log.debug("[cache] sellerMenuService.getFirstMenuList");
				List<Menu> firstMenuList = sellerMenuService.getFirstMenuList(menu);

				log.debug("[cache] sellerMenuService.getSecondAndThirdMenuList");
				List<Menu> secondAndThirdMenuList = sellerMenuService.getSecondAndThirdMenuList(menu);

				int firstMenuId = 0;

				int i = 0;
				for (Menu menu2 : secondAndThirdMenuList) {
					if (i == 0){
						firstMenuId = menu2.getMenuParentId();
					}
					i++;
				}


				SellerMenu sellerMenu = new SellerMenu();
				sellerMenu.setFirstMenuId(firstMenuId);
				sellerMenu.setMenuCode(menuCode);

				sellerMenu.setFirstMenuList(firstMenuList);
				sellerMenu.setSecondAndThirdMenuList(secondAndThirdMenuList);

				sellerContext.setSellerMenu(sellerMenu);
			}

			HttpSession session = request.getSession();

			if (session.getAttribute("SHADOW_SELLER") != null){
				sellerContext.setShadowlogin(true);
			}

			if (!AjaxUtils.isAjaxRequest(request)) {
				if (modelAndView != null) {		// 파일 다운로드시 모델 없음
					modelAndView.addObject("sellerContext", sellerContext);
				}
			}
		}
	}


	private boolean isLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (UserUtils.isSellerLogin()) {
			return true;
		}

		if (session.getAttribute("SELLER") == null && session.getAttribute("SHADOW_SELLER") == null) {
			return false;
		}

		return true;
	}

	/**
	 * 메뉴 호출이 필요 없는 페이지인가?
	 * @param requestContext
	 * @return
	 */
	private boolean isLoadMenuPage(RequestContext requestContext) {
		// 휴면계정 안내 페이지 전환 예외 (WEB 기준 URI 등록)
		String[] ignores = new String[] {
				"/seller/index",
				"/seller/login",
				"/seller/login-lock",
				"/seller/login-change-password",
				"/seller/login-change-password2",
				"/seller/change-password",
				"/seller/login-seller"
				,"/seller/login-user-email"
				,"/seller/login-user-auth-check"
		};

		String requestUri = requestContext.getRequestUri();
		String originRequestURI = requestContext.getRequest().getRequestURI();

		for (String pattern : ignores) {
			if (antPathMatcher.match(pattern, requestUri)
					|| antPathMatcher.match(pattern, originRequestURI)) {
				return false;
			}
		}

		return true;
	}
}

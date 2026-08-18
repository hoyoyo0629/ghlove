package saleson.common.interceptor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlinepowers.framework.common.ServiceType;
import com.onlinepowers.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.web.opmanager.menu.MenuService;
import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;
import com.onlinepowers.framework.web.opmanager.menu.domain.OpmanagerMenu;

import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.UserUtils;
import saleson.shop.user.PersonInChargeService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.ManagerLogin;


public class OpmanagerHandlerInterceptor implements HandlerInterceptor {
    protected Logger log = LoggerFactory.getLogger(getClass());
    private String ROLE_SUPERVISOR = "ROLE_SUPERVISOR";
    private String BASE_URI = "/opmanager";

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
	PersonInChargeService personInChargerService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CommonUtils.isResourceHandler(handler) || !isManagerPage(request)) {
            return true;
        }

        boolean isLogin = UserUtils.isManagerLogin();
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
        String requestUri = request.getRequestURI();
        String forwardRequestUri = CommonUtils.getRequestUri(request);

        // srhan test. 20251014
        if(requestUri.indexOf("/opmanager/user/login-user-email") > -1)
        	return true;

        // 20251029
        if(requestUri.indexOf("/opmanager/user/pki/delete") > -1) {
        	return true;
        }

        if (!isLogin) {
            log.debug("관리자페이지({})는 로그인 후 접근 가능. 로그인페이지로 이동", forwardRequestUri);

            if (isAjax) {
                String ajaxErrorMessage = JsonViewUtils.objectToJson(JsonViewUtils.failure("로그인 세션이 만료되어, 재 로그인해주시기 바랍니다."));
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getOutputStream().write(ajaxErrorMessage.getBytes("UTF-8"));

                return false;
            }

            String target = forwardRequestUri;

            if (request.getQueryString() != null) {
            	String queryStr = request.getQueryString();
                if (!queryStr.startsWith("/opmanager")) {			// 시큐어코딩 처리... 오동작 여부 확인 필요
                	response.sendRedirect(request.getContextPath());
                    return false;
                }

            	queryStr = queryStr.replaceAll("\r", "").replaceAll("\n", "");
                target = forwardRequestUri + "?" + queryStr;
            }

            response.sendRedirect(request.getContextPath() + "/opmanager/login?target=" + target);
            return false;
        }

        if (!isAjax
                && SalesonProperty.isSalesonAuthManagerProcess()
                && !requestUri.startsWith("/opmanager/auth")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime authDate = userService.getAuthExpiredDateById(UserUtils.getManagerId());

            boolean authFlag = false;

            if (authDate != null && now.isBefore(authDate)) {
                authFlag = true;
            }

            if (!authFlag) {
                response.sendRedirect(request.getContextPath()  + "/opmanager/auth?target="+forwardRequestUri);
                return false;
            }
        }


        if (!securityService.hasRole("ROLE_OPMANAGER")) {
            log.debug("관리자페이지({}) 접근권한(ROLE_OPMANAGER)이 없음. 로그인페이지로 이동", forwardRequestUri);
            response.sendRedirect("/opmanager/login?error=2");
            return false;
        }

        // 중복세션 처리 세션정보가 DB에 없으면 로그아웃 처리
        List<ManagerLogin> loginSessionList = userService.getLoginSessionForManagerByUserId(UserUtils.getManagerId());
        String sessionId = request.getSession().getId();

        long loginSessionCount = loginSessionList.stream()
                .filter(m -> sessionId.equals(m.getSessionId()))
                .count();

        /* 소스 변경전
        if (loginSessionCount == 0) {

            if (!ServiceType.LOCAL && !"saleson".equals(UserUtils.getLoginId())) {
                log.debug("중복 사용자 체크로 인한 로그아웃");
                response.sendRedirect("/op_security_logout?target=/opmanager/login-disconnect");
                return false;
            }
        }
        */
        //중복로그인 추가(2022.11.01), 로컬 환경에서는 체크 안하도록 수정 (2022.11.02)
        if (!ServiceType.LOCAL && loginSessionCount == 0) {
            log.debug("중복 사용자 체크로 인한 로그아웃");
            response.sendRedirect("/op_security_logout?target=/opmanager/login-disconnect");
            return false;
        }



        // 권한이 있는가?
        RequestContext requestContext = ThreadContextUtils.getRequestContext();
        List<String> menuCodes = CommonUtils.getMenuCode(requestContext.getRequestUri());

        HashMap<String, Object> map = new HashMap<>();
        if (SecurityUtils.isSupervisor()) {
            map.put("authority", ROLE_SUPERVISOR);
        }
        map.put("menuCodes", menuCodes);
        map.put("userId", SecurityUtils.getCurrentUserId());

        // 메뉴 코드
        String menuCode = menuService.getMenuCode(map);

        if (menuCode == null && !isLoadMenuPage(requestUri)) {
            throw new UserException("관리자 메뉴 접근 권한이 없습니다.", "/opmanager/");
        }

        // 오프라인 부담당자이면서 정보갱신을 하지 않았으면 담당자 상세 정보 페이지로 이동
 		if(securityService.hasRole("ROLE_ADMIN_8")
 				&& "N".equals(personInChargerService.getOffPersonInChargeInfoUpdateFlag())
 				&& requestUri.indexOf("/opmanager/user/off-charger/edit") == -1) {
 			String pageEvent = ("/opmanager".equals(requestUri)) ? "init" : "change";
 			response.sendRedirect("/opmanager/user/off-charger/edit/"+UserUtils.getUser().getUserId()+"?pageEvent="+pageEvent);
 			return false;
 		}

        if ("GET".equals(request.getMethod()) || "POST".equals(request.getMethod())) {

            Menu menu = new Menu();
            menu.setCacheKey(Long.toString(SecurityUtils.getCurrentUserId()));
            menu.setUserId(SecurityUtils.getCurrentUserId());
            menu.setMenuCode(menuCode);

            if (securityService.hasRole(ROLE_SUPERVISOR)) {
                menu.setAuthority(ROLE_SUPERVISOR);
            }

            List<Menu> firstMenuList = menuService.getFirstMenuList(menu);
            List<Menu> secondAndThirdMenuList = menuService.getSecondAndThirdMenuList(menu);

            int firstMenuId = 0;
            if (secondAndThirdMenuList != null && !secondAndThirdMenuList.isEmpty()) {
                firstMenuId = secondAndThirdMenuList.get(0).getMenuParentId();
            }

            OpmanagerMenu opmanagerMenu = new OpmanagerMenu();
            opmanagerMenu.setFirstMenuId(firstMenuId);
            opmanagerMenu.setMenuCode(menuCode);
            opmanagerMenu.setFirstMenuList(firstMenuList);
            opmanagerMenu.setSecondAndThirdMenuList(secondAndThirdMenuList);

            requestContext.setOpmanagerMenu(opmanagerMenu);
            ThreadContextUtils.setRequestContext(requestContext);
        }
        return true;
    }

    /**
     * 관리자페이지 접속인가?
     * @param request
     * @return
     */
    private boolean isManagerPage(HttpServletRequest request) {
        String requestUri = request.getRequestURI();

        if (requestUri == null) {
            return false;
        }

        if (!requestUri.startsWith(BASE_URI)) {
            return false;
        }

        if (requestUri.indexOf(BASE_URI + "/login") > -1
            || requestUri.indexOf(BASE_URI + "/healthcheck") > -1
            || requestUri.indexOf(BASE_URI + "/accessdenied") > -1
            || requestUri.indexOf(BASE_URI + "/offgive/sci-result") > -1
            || requestUri.indexOf(BASE_URI + "/offgive/pdbtest") > -1
            || requestUri.indexOf(BASE_URI + "/give/give-state/options-by-locgovCode") > -1
            || requestUri.indexOf(BASE_URI + "/user/login-user-valid") > -1
            || requestUri.indexOf(BASE_URI + "/manager-request/form") > -1
            || requestUri.indexOf(BASE_URI + "/manager-request/pkiForm") > -1
            || requestUri.indexOf(BASE_URI + "/manager-request/pkiFormFin") > -1
            || requestUri.indexOf(BASE_URI + "/manager-request/create") > -1
            || requestUri.indexOf(BASE_URI + "/manager-request/code-child/list") > -1
            || requestUri.indexOf(BASE_URI + "/user/popup/temp-password-change") > -1
            || requestUri.indexOf(BASE_URI + "/user/temp-password-change") > -1
            || requestUri.indexOf(BASE_URI + "/user/pki/form") > -1
            || requestUri.indexOf(BASE_URI + "/magicline/signedFormRGhlove") > -1
            || requestUri.indexOf(BASE_URI + "/magicline/signedForm") > -1
            || requestUri.indexOf(BASE_URI + "/magicline/signedFormR") > -1
            || requestUri.indexOf(BASE_URI + "/magicline/vidClientIDN") > -1
            || requestUri.indexOf(BASE_URI + "/magicline/vidClientIDNR") > -1
            || requestUri.indexOf(BASE_URI + "/present/login") > -1
            || requestUri.indexOf(BASE_URI + "/user/pki/update") > -1
        	|| requestUri.indexOf(BASE_URI + "/user/pki/login") > - 1
//        	|| requestUri.indexOf(BASE_URI + "/transfer") > - 1
        	|| requestUri.indexOf(BASE_URI + "/user/password-enc") > - 1
        	|| requestUri.indexOf(BASE_URI + "/popup/temp") > - 1
			|| requestUri.indexOf(BASE_URI + "/log/exceldownload-reason") > - 1
        	|| requestUri.indexOf(BASE_URI + "/user/pki/login-user-valid") > - 1
        	|| requestUri.indexOf(BASE_URI + "/user/fin/token") > - 1
        	|| requestUri.indexOf(BASE_URI + "/user/fin/nonce") > - 1
        	|| requestUri.indexOf(BASE_URI + "/file/index") > - 1
        	|| requestUri.indexOf(BASE_URI + "/file/makeDir") > - 1
        	|| requestUri.indexOf(BASE_URI + "/file/uploadFile") > - 1
        	|| requestUri.indexOf(BASE_URI + "/file/downloadFile") > - 1
        	|| requestUri.indexOf(BASE_URI + "/file/sendMail") > - 1
        	|| requestUri.indexOf(BASE_URI + "/fileDownload/downloadByProgramData") > - 1
        	|| requestUri.indexOf(BASE_URI + "/designated-donation/partList") > -1
        	|| requestUri.indexOf(BASE_URI + "/user/login-user-auth-check") > -1
        	|| requestUri.indexOf(BASE_URI + "/user/popup/emailModifyInfo/") > -1		// 관리자 로그인 - opmanager, seller 이메일 설정 팝업
        	|| requestUri.indexOf(BASE_URI + "/welfareCenter/wlfrCntrMngList") > -1) {
            return false;
        }

        return true;
    }

    /**
     * 메뉴 호출이 필요 없는 페이지인가?
     * @param requestUri
     * @return
     */
    private boolean isLoadMenuPage(String requestUri) {
        String[] uriPatterns = new String[] {
            BASE_URI,
			BASE_URI + "/",
			BASE_URI + "/index",
			BASE_URI + "/dashboard",
			BASE_URI + "/reload-cache",
            BASE_URI + "/auth",
            BASE_URI + "/juso-popup"
        };

        for (String uri : uriPatterns) {
            if (uri.equals(requestUri)) {
                return true;
            }
        }
        return false;
    }
}
/**
 * SalesOn3 API Client
 *
 * @Date 2019.10.07.
 * @Author skc@onlinepowers.com
 * @type {{exception: Saleson.exception, redirect: Saleson.redirect, init: Saleson.init, debug: Saleson.debug, const: {SAVED_LOGIN_ID: string, CATEGORY: string, TOKEN: string}, notFoundException: Saleson.notFoundException, log: Saleson.log, callbackAlert: Saleson.callbackAlert, error: Saleson.error, confirm: Saleson.confirm, isLogin: (function(): (*|boolean)), authenticationException: Saleson.authenticationException, pages: {LOGIN: string, INDEX: string, isAllowAnonymous: (function(): boolean)}, handleException: Saleson.handleException, requestContext: {}, alert: Saleson.alert, config: {cdnDomain: string, apiDomain: string, HMACSecretKey: string}}}
 */

var $s = (Saleson = {
  config: {
    apiDomain: API_DOMAIN,
    cdnDomain: CDN_DOMAIN,
    virtualDomain: VIRTUAL_DOMAIN,
    domain: SERVER_POSITION,
    isUseNetFunnel: IS_USE_NET_FUNNEL,
    isUseOnepass: IS_USE_ONEPASS,
    // 설정에 없으면 무조건 false — 운영 빌드에 우회가 켜지는 일이 없도록.
    isSkipExternalAuth: typeof IS_SKIP_EXTERNAL_AUTH !== "undefined" && IS_SKIP_EXTERNAL_AUTH === true,
    ozDomain: OZ_DOMAIN,

    // SNS
    kakaoLoginAppId: "630ae2d628e0c7f71dad786c8079e10f",
    facebookLoginAppId: "604795780363142",
    naverLoginAppId: "Qnet1GaSzqf2m48fnnY1",
    naverLoginCallback: "/users/sns/naver-callback.html",

    noImage: "/static/images/thumb.png",
    loadingImage: "/static/images/loadingpage.gif",

    //금융인증서 개발
    dev_finUrl: "https://t-certapi.yeskey.or.kr/oauth/2.0/api/token",
    dev_ucpidUrl: "https://t-certapi.yeskey.or.kr/v1/ucpid/ucpid-info",
    dev_orgCode: "DG00560000",
    dev_apiKey: "dba3f9f8-0ada-4661-91f2-18e53681d255",
    dev_clientSecret: "87b70351-7d6a-42f4-96cf-b8c9d39c1770",
    dev_cpCode: "Y30000001301",

    //금융인증서 운영
    finUrl: "https://certapi.yeskey.or.kr/oauth/2.0/api/token",
    ucpidUrl: "https://certapi.yeskey.or.kr/v1/ucpid/ucpid-info",
    orgCode: "RG00560000",
    apiKey: "ea1525f5-f619-4398-abc2-2c6d9581550d",
    clientSecret: "8a6aa6d9-47d9-4999-9dd5-8f4a36d1b526",
    cpCode: "Y30000001301",

    // 중복호출 방지용
    requestUriInfo: {},
  },

  const: {
    SALESON_ID: "saleson_id",
    TOKEN: "token",
    TOKEN_STATUS: "token_status",
    TOKEN_TYPE: "token_type",
    CATEGORY: "category",
    CATEGORY_UPDATED_DATE: "category_updated_date",
    SAVED_LOGIN_ID: "saved_login_id",
    CAMPAIGN_CODE: "campaign_code",
    VISIT: "visit",
    VISIT_EXPIRE_DATE: "visit_expire_date",
    LATELY_SEARCH: "lately_search",
    BUY_ORDER: "buy_order",
    KAKAO_SHARE_INIT_FLAG: "kakao_share_init_flag",
    LOGIN_FIRST: "login_first",
    LOGIN_FIRST_STATUS: "Y",
    SAVED_ONEPASS_LOGIN_ID: "saved_onepass_login_id",
    PASSWORD_EXPIRED: "password_expired",
    STATUS_Y: "Y",
    EMAIL_AGREE: "email_agree",
    SMS_AGREE: "sms_agree",
    PBANC_AGREE: "pbanc_agree",
    KAKAO_AGREE: "kakao_agree",
    REGION_CODE: "region_code",
  },

  requestContext: {},
  pages: {
    INDEX: "/",
    LOGIN: "/users/login.html",
    FIND_ID_PW: "/users/find-idpw.html",
    JOIN: "/users/join.html",
    JOIN_COMPLETE: "/",
    SLEEP_USER: "/users/sleep-user.html",
    CHANGE_PASSWORD: "/users/change-password.html",
    MYPAGE_ORDER: "/mypage/order.html",

    /**
     * 비회원 접속이 가능한 페이지인가?
     * @returns {boolean}
     */
    isAllowAnonymous: function () {
      var allowAnonymous = [
        $s.pages.LOGIN,
        $s.pages.FIND_ID_PW,
        $s.pages.JOIN,
        $s.pages.JOIN_COMPLETE,
        $s.pages.SLEEP_USER,
        $s.pages.CHANGE_PASSWORD,
      ];
      var requestUri = $s.requestContext.requestUri;
      var isMatched = false;

      for (var i = 0; i < allowAnonymous.length; i++) {
        if (allowAnonymous[i] == requestUri) {
          isMatched = true;
          break;
        }
      }

      return isMatched;
    },
  },

  init: function (options) {
	/*
	// 주소에 www. 있는 경우와 없는 경우 로그인 세션이 달라서 처리 추가
	let hostUrl = location.host;
	if (hostUrl.startsWith("www.")) {
		let queryStr = location.search;
		let protocol = location.protocol;
		hostUrl = hostUrl.replace("www.", "");
		location.href = protocol + "//" + hostUrl + queryStr;
		return;
	}
	*/
    $s = Saleson;
    $api = $s.api;
    $c = $s.const;
    $p = $s.pages;

    // check
    if (axios == "undifined" || typeof axios !== "function") {
      alert("Axios is not loaded.");
      return;
    }
    axios.defaults.baseURL = this.config.apiDomain;
    axios.defaults.headers.get["Content-Type"] =
      "application/x-www-form-urlencoded";
    axios.defaults.headers.put["Content-Type"] =
      "application/json;charset=utf-8";
    axios.defaults.headers.patch["Content-Type"] =
      "application/json;charset=utf-8";
    axios.defaults.headers.patch["Access-Control-Allow-Origin"] = "*";
    axios.defaults.headers.delete["Content-Type"] =
      "application/json;charset=utf-8";
    axios.defaults.headers.delete["Access-Control-Allow-Origin"] = "*";
    axios.defaults.headers.post["Content-Type"] =
      "application/json;charset=utf-8";
    axios.defaults.headers.post["Access-Control-Allow-Origin"] = "*";

    let cancelSource = axios.CancelToken.source();

    // 중복호출 방지 로직
    axios.interceptors.request.use(
      function (config) {
        let nowDate = new Date();
        if (config.url) {
          if (
            $s.config.requestUriInfo[config.url] &&
            !config.responseCheckUrl
          ) {
            // url 이 제한시간 이내 재호출되고 최초 요청된 call 확인용 플래그 값이 없으면 요청 취소
            //if (nowDate - $s.config.requestUriInfo[config.url] < DUPLICATION_CALL_LIMIT) {
            cancelSource.cancel("Duplication Call");
            let error = new Error("Duplication Call");
            error.url = config.url;
            return Promise.reject(error);
            //}
          }
          config.responseCheckUrl = config.url; // 요청시 인터넵터를 2번씩 호출하여 최초 요청된 call 확인용 플래그 추가
          $s.config.requestUriInfo[config.url] = nowDate;
          return config;
        }
        cancelSource.cancel("None Url");
        let error = new Error("None Url");
        //error.config = config;
        return Promise.reject(error);
      },
      function (error) {
        return Promise.reject(error);
      }
    );

    // 응답 인터셉터
    axios.interceptors.response.use(
      function (response) {
        if (response && response.config && response.config.responseCheckUrl) {
          $s.config.requestUriInfo[response.config.responseCheckUrl] = null;
        }
        return response;
      },
      function (error) {
        if (error && error.config && error.config.responseCheckUrl) {
          $s.config.requestUriInfo[error.config.responseCheckUrl] = null;
          if (error.response.data.message == 'Invalid Token Format') {
			$s.logout();
			return;
		  }
        }
        return Promise.reject(error);
      }
    );

    this.axios = axios;
    if (options && options.url) {
		this.requestContext = this.core.parseUrl(options.url);
	} else {
    	this.requestContext = this.core.parseUrl(location.href);
	}

    // 인증설정
    try {
      var isLoginPage = false;
      var isGuestLoginPage = false;
      var initSeoFlag = true;
      let isAgency = false;

      if (typeof options != "undefined") {
        try {
          isLoginPage = options.loginPage;
          isGuestLoginPage = options.guestLoginPage;
          initSeoFlag = options.initSeoFlag;
          isAgency = options.isAgency;
        } catch (e) {
          $s.error(e);
        }
      }

      $s.setCampaignCode();

      /* 성능 이슈로 seo 호출 안함
            if (initSeoFlag) {
                $s.initSeo();
            }
            */
	  if (!isAgency) {
		$s.api.salesonId(
			function () {},
			function (error) {
				$s.error(error);
			}
		);
		$s.core.authenticationFilter(function () {
		  if (!$s.pages.isAllowAnonymous()) {
			var alertFlag;

			if (isGuestLoginPage) {
			  alertFlag = !$s.isGuestLogin();
			} else {
			  alertFlag = isLoginPage;
			}

			if (alertFlag) {
			  $s.authenticationException("로그인 후 이용이 가능합니다.");
			}
		  }
		});
      /*
            // 성능 이슈로 해당부분 호출 안함.
            $s.ga.init();
             */
		$s.ev.init();
		if ($s.isLogin()) {
			$s.api.getSessionTimeout(function(data) {
				$s.logoutConfirmPopup.init(data.timeout);
				$s.logoutConfirmPopup.start();

                    //var timeout = -1;
                    //try {
                    //    timeout = data.timeout * 1000 * 60;
                    //} catch (e){
                    //    timeout = -1;
                    //}

                    //if (timeout > 0) {
                    //    setTimeout(function() {
                    //        $s.logout();
                    //   }, timeout);
                    //}
			},function(error) {
				$s.error(error);
			});
			try {
				if (options && options.method && typeof options.method == 'function') {
					options.method();
				}
			} catch (e) {
				$s.error(e);
			}
		}
	  }
    } catch (e) {
      this.handleException(e);
      return;
    }
  },

  initSeo: function () {
    $s.api.seo(
      function (response) {
        var seo = response.seo;
        if (typeof seo != "undefined" && seo != null && !seo.seoNull) {
          $s.seo(
            seo.title,
            seo.description,
            seo.keywords,
            seo.headerContents1,
            seo.indexFlag
          );
          $s.openGraphTag(
            $s.requestContext.href,
            seo.title,
            "",
            seo.description
          );
        }
      },
      function (error) {
        $s.error(error);
      }
    );
  },

  cleanToken: function () {
    sessionStorage.removeItem($s.const.TOKEN);
    sessionStorage.removeItem($s.const.TOKEN_TYPE);
    sessionStorage.removeItem($s.const.TOKEN_STATUS);
  },

  setToken: function (token, status, type) {
    $s.core.setSession($s.const.TOKEN, token);
    $s.core.setSession($s.const.TOKEN_STATUS, status);
    $s.core.setSession($s.const.TOKEN_TYPE, type);
  },

  getHashInBase64: function (hmacMessage) {
    var hash = CryptoJS.HmacSHA256(hmacMessage, $s.config.apiDomain); //웹취약점 대응 HMACSecretKey -> apiDomain로 교체
    return CryptoJS.enc.Base64.stringify(hash);
  },

  deleteOnepassCookie: function () {
    $s.deleteCookie("userKey");
    $s.deleteCookie("intfToken");
  },

  deleteCookie: function (name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/;";
    // document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;domain=localhost;path=/;';
  },

  logout: function () {
    $s.cleanToken();
    sessionStorage.removeItem($s.const.SALESON_ID);
    // 다른 탭 세션 삭제 용도
    localStorage.setItem("storage-remove", "1");
    localStorage.removeItem("storage-remove");
    $s.deleteOnepassCookie();
    $s.api.salesOnLogout(
      function () {
        $s.debug("SalesOn Logout");
      },
      function (error) {
        $s.debug("Fail SalesOn Logout");
      }
    );

    $s.redirect($s.pages.INDEX);
  },

  isLogin: function () {
    return (
      $s.core.isAuthenticated() &&
      "USER" == $s.core.getSession($s.const.TOKEN_TYPE)
    );
  },

  isGuestLogin: function () {
    return (
      $s.core.isAuthenticated() &&
      "GUEST" == $s.core.getSession($s.const.TOKEN_TYPE)
    );
  },

  authenticationException: function (msg) {
    var err = {
      exception: "AuthenticationException",
      message: msg,
    };
    $s.debug(err);
    throw JSON.stringify(err);
  },
  exception: function (msg) {
    var err = {
      exception: "Exception",
      message: msg,
    };
    $s.debug(err);
    throw JSON.stringify(err);
  },
  notFoundException: function (msg) {
    var err = {
      exception: "PageNotFoundException",
      message: msg,
    };
    $s.debug(err);
    location.replace("/public/error/404.html");
    throw JSON.stringify(err);
  },

  handleException: function (e) {
    this.log("[handleException] " + e);

    try {
      var ex = JSON.parse(e);

      if ("AuthenticationException" === ex.exception) {
        //alert(ex.message);
        this.redirect(
          $s.pages.LOGIN +
            "?target=" +
            encodeURIComponent($s.requestContext.requestFullUri)
        );
        return;
      }
      if ("PageNotFoundException" === ex.exception) {
        //alert(ex.message);
        location.replace("/public/error/404.html");
        return;
      } else {
        this.error(ex);
      }
    } catch (e) {
      this.error(e);
    }
  },
  alertFocusId: {
    confirmFocusId: "",
    cancelFocusId: "",
  },

  customAlert: function (message, callback) {
    //$s.callbackAlert = $s.core.isFunction(callback) ? callback : $s.closeAlert;
    if ($s.core.isFunction(callback)) {
      $s.callbackAlert = callback;
    } else {
      this.alertFocusId.confirmFocusId = callback;
      $s.callbackAlert = $s.closeAlert;
    }

    var $alert = $("#op-alert");

    if (!message || typeof message == "object") {
      // 에러 메시지 없을 경우 오류 방지
      message = "잠시 후 다시 시도해주세요.";
      //message = "문제가 발생했습니다.";
    }

    var array = message.split("\n");
    let $area = $alert.find(".modal-body .pop_txt");
    $area.empty();

	$area.removeAttr('style');
    let dialog = $alert.find(".modal-dialog-centered");
    dialog.removeAttr('style');
    dialog.attr('style', "max-width: 38rem");

    for (var i = 0; i < array.length; i++) {
		 if(array[i] == '\r')
		 {
			$area.append("</br>");
		 }
		 else
		 {
			$area.append($("<p></p>").text(array[i]));
		 }

    }

    //        $alert.find('.modal-body p').text(message);
    $alert.find(".confirm-type").hide();
    $alert.find(".alert-type").show();

    $alert.modal("show");

    //$('#saleson').on('click', '#op-alert', $s.callbackAlert);

    setTimeout(function () {
      $alert.find(".op-modal-ok").focus();
    }, 500);
  },

  alert: function (message, callback) {
    //$s.callbackAlert = $s.core.isFunction(callback) ? callback : $s.closeAlert;
    if ($s.core.isFunction(callback)) {
      $s.callbackAlert = callback;
    } else {
      this.alertFocusId.confirmFocusId = callback;
      $s.callbackAlert = $s.closeAlert;
    }

    var $alert = $("#op-alert");

    if (!message || typeof message == "object") {
      // 에러 메시지 없을 경우 오류 방지
      message = "잠시 후 다시 시도해주세요.";
      //message = "문제가 발생했습니다.";
    }

    var array = message.split("\n");
    let $area = $alert.find(".modal-body .pop_txt");
    $area.empty();

	$area.removeAttr('style');
    let dialog = $alert.find(".modal-dialog-centered");
    dialog.removeAttr('style');

    for (var i = 0; i < array.length; i++) {
		 if(array[i] === '\r')
		 {
			$area.append("<br>");
		 }
		 else
		 {
			$area.append($("<p></p>").text(array[i]));
		 }

    }

    //        $alert.find('.modal-body p').text(message);
    $alert.find(".confirm-type").hide();
    $alert.find(".alert-type").show();

    $alert.modal("show");

    //$('#saleson').on('click', '#op-alert', $s.callbackAlert);

    setTimeout(function () {
      $alert.find(".op-modal-ok").focus();
    }, 500);
  },
  alertHtml: function (message, callback) {
    //$s.callbackAlert = $s.core.isFunction(callback) ? callback : $s.closeAlert;
    if ($s.core.isFunction(callback)) {
      $s.callbackAlert = callback;
    } else {
      this.alertFocusId.confirmFocusId = callback;
      $s.callbackAlert = $s.closeAlert;
    }

    var $alert = $("#op-alert");

    if (!message || typeof message == "object") {
      // 에러 메시지 없을 경우 오류 방지
      message = "잠시 후 다시 시도해주세요.";
      //message = "문제가 발생했습니다.";
    }

    //var array = message.split("\n");
    let $area = $alert.find(".modal-body .pop_txt");
    $area.empty();
    $area.attr('style', 'text-align: left;margin: 0px;');
    let dialog = $alert.find(".modal-dialog-centered");
    dialog.attr('style', 'max-width: 500px;');

    /*for (var i = 0; i < array.length; i++) {
		 if(array[i] === '\r')
		 {
			$area.append("<br>");
		 }
		 else
		 {
			$area.append($("<p></p>").text(array[i]));
		 }

    }
*/
	$area.append(message);
    //        $alert.find('.modal-body p').text(message);
    $alert.find(".confirm-type").hide();
    $alert.find(".alert-type").show();

    $alert.modal("show");

    //$('#saleson').on('click', '#op-alert', $s.callbackAlert);

    setTimeout(function () {
      $alert.find(".modal-body .pop_txt").focus();
    }, 500);
  },
  confirm: function (message, callback, cancelFocusId) {
    // $s.callbackAlert = $s.core.isFunction(callback) ? callback : $s.closeAlert;
    if ($s.core.isFunction(callback)) {
      $s.callbackAlert = callback;
    } else {
      this.alertFocusId.confirmFocusId = callback;
      $s.callbackAlert = $s.closeAlert;
    }
    $s.cancelCallback = $s.closeAlert;
    this.alertFocusId.cancelFocusId = cancelFocusId;

    var $alert = $("#op-alert");

    let $area = $alert.find(".modal-body .pop_txt");
    $area.empty();
	$area.removeAttr('style');
    let dialog = $alert.find(".modal-dialog-centered");
    dialog.removeAttr('style');

	$area.prepend($("<p></p>"));

    // 중복 생성된 p 태그 삭제
    let pTags = $alert.find(".modal-body p");
    let length = pTags.length;
    for (let i = 0; i < length; i++) {
      if (i > 0) {
        pTags[i].remove();
      }
    }

    $alert.find(".modal-body p").text(message);
    $alert.find(".modal-body .op-modal-cancel").html("취소");
    $alert.find(".modal-body .op-modal-ok").html("확인");
    $alert.find(".alert-type").hide();
    $alert.find(".confirm-type").show();

    $alert.modal("show");

    //$('#saleson').on('click', '#op-alert', $s.callbackAlert);
  },

  confirm2: function (
    message,
    applyTxt,
    cancelTxt,
    applyCallback,
    cancelCallback
  ) {
    // $s.callbackAlert = $s.core.isFunction(applyCallback) ? applyCallback : $s.closeAlert;
    // $s.cancelCallback = $s.core.isFunction(cancelCallback) ? cancelCallback : $s.closeAlert;
    if ($s.core.isFunction(applyCallback)) {
      $s.callbackAlert = applyCallback;
    } else {
      this.alertFocusId.confirmFocusId = applyCallback;
      $s.callbackAlert = $s.closeAlert;
    }
    if ($s.core.isFunction(cancelCallback)) {
      $s.cancelCallback = cancelCallback;
    } else {
      this.alertFocusId.cancelFocusId = cancelCallback;
      $s.cancelCallback = $s.closeAlert;
    }

    var $alert = $("#op-alert");

    let $area = $alert.find(".modal-body .pop_txt");
    $area.empty();
	$area.removeAttr('style');
    let dialog = $alert.find(".modal-dialog-centered");
    dialog.removeAttr('style');

	$area.prepend($("<p></p>"));

    // 중복 생성된 p 태그 삭제
    let pTags = $alert.find(".modal-body p");
    let length = pTags.length;
    for (let i = 0; i < length; i++) {
      if (i > 0) {
        pTags[i].remove();
      }
    }

    $alert.find(".modal-body p").text(message);
    $alert
      .find(".modal-body .op-modal-cancel")
      .html(cancelTxt ? cancelTxt : "취소");
    $alert.find(".modal-body .op-modal-ok").html(applyTxt ? applyTxt : "확인");
    $alert.find(".alert-type").hide();
    $alert.find(".confirm-type").show();

    $alert.modal("show");
  },

  logoutConfirmPopup: {
    loginTimer: undefined, // 로그아웃 타이머 변수
    logoutPopupTime: 5, // 로그인 만료 전 팝업 호출 시간(분)
    sessionTime: -1, // 세션 유지 시간

    init: function (timeout) {
      if (typeof timeout === "number") {
        $s.logoutConfirmPopup.sessionTime = timeout * 1000 * 60;
      }
    },

    start: function () {
      var logoutPopupsec = $s.logoutConfirmPopup.logoutPopupTime * 1000 * 60;
      if ($s.logoutConfirmPopup.sessionTime <= 0) {
        return;
      }

      // Saleson.init 실행이 여러번이라 1번만 실행되도록 조건 추가
      if (typeof $s.logoutConfirmPopup.loginTimer !== "undefined") {
        return;
      }

      // 시간이 지나면 강제 로그아웃
      $s.logoutConfirmPopup.loginTimer = setTimeout(function () {
        $s.logout();
      }, $s.logoutConfirmPopup.sessionTime);

      if ($s.logoutConfirmPopup.sessionTime > logoutPopupsec) {
        setTimeout(function () {
          $s.confirm2(
            $s.logoutConfirmPopup.logoutPopupTime +
              "분뒤에 자동 로그아웃 처리됩니다.\n연장하시겠습니까?",
            "확인",
            "로그아웃",
            $s.logoutConfirmPopup.extensionLoginTime,
            $s.logout
          );
        }, $s.logoutConfirmPopup.sessionTime - logoutPopupsec);
      }
    },

    // 로그인 연장
    extensionLoginTime: function () {
      // 연장되면 기존에 로그아웃 setTimeout clear
      $s.logoutConfirmPopup.stopTimeout();

      // 로그인 시간 초기화
      $s.logoutConfirmPopup.start();
      $s.closeAlert();
    },

    stopTimeout: function () {
      clearTimeout($s.logoutConfirmPopup.loginTimer);
      // clearTimeout 이후 상태 초기화
      $s.logoutConfirmPopup.loginTimer = undefined;
    },
  },

  closeAlert: function (isCancel) {
    $("#op-alert").modal("hide");
    let focusId;
    if (isCancel) {
      focusId = this.alertFocusId.cancelFocusId;
    } else {
      focusId = this.alertFocusId.confirmFocusId;
    }
    if (!focusId) {
      // 값이 없으면 본문으로 이동
      location.href = "#wrap";
      const urlObj = new URL(location.href);
      urlObj.hash = "";
      history.replaceState(null, null, urlObj.toString());
    } else {
      try {
        setTimeout(() => {
          $("#" + focusId).focus();
        }, 100);
      } catch (e) {
        $s.error(e);
      }
    }
    this.alertFocusId.cancelFocusId = "";
    this.alertFocusId.confirmFocusId = "";
  },

  callbackAlert: function (isCancel) {},

  cancelCallback: function (isCancel) {},

  toast: function (message) {
    var $toast = $("#op-toast .toast_bg");
    var fadeIn = 300;
    var fadeOut = 1000;

    $toast.find(".toast_wrap p").text(message);

    $toast.delay(fadeIn).fadeIn();
    $toast.delay(fadeOut).fadeOut();
  },

  log: function (message) {
	console.log = () => {};
  },
  debug: function (message) {
	console.log = () => {};
  },
  error: function (message) {
	console.log = () => {};
  },
  redirect: function (uri) {
	let decodeUri = decodeURIComponent(uri);
	try {
		if (uri != "/" && "http" != decodeUri.substring(0, 4)) {
			decodeUri = location.protocol + "//" + location.host + decodeUri;
		}
    	location.href = decodeUri;
	} catch(e) {
    	location.href = uri;
	}
  },
  pagination: function (totalPages, apiLink) {
    var result = document.createDocumentFragment();
    var page = Number(
      $s.core.getParameter("page") == "" ? 1 : $s.core.getParameter("page")
    );
    for (var i = 0; i < totalPages; i++) {
      var li = document.createElement("li");
      var a = document.createElement("a");
      li.setAttribute("class", "page-item");
      a.setAttribute("class", "page-link");
      li.appendChild(a);
      if (i == 0) {
        var pre = document.createDocumentFragment();
        var lip = li.cloneNode(a);
        lip.children[0].setAttribute("aria-label", "Previous");
        lip.children[0].appendChild(document.createElement("span"));
        lip.children[0].children[0].setAttribute("class", "sr-only");
        lip.children[0].children[0].innerHTML = "Previous";
        result.appendChild(lip);
      }
      var paramLength = location.href
        .replace(location.origin, "")
        .split("?").length;
      if (paramLength > 1) {
        a.setAttribute("href", apiLink + "&page=" + Number(i + 1));
      } else {
        a.setAttribute("href", apiLink + "?page=" + Number(i + 1));
      }
      a.innerHTML = i + 1;
      if (Number(i + 1) === page) {
        var span = document.createElement("span");
        span.setAttribute("class", "sr-only");
        span.innerHTML = page;
        a.appendChild(span);
        li.setAttribute("class", "page-item active");
      }
      li.appendChild(a);
      result.appendChild(li);
      if (i == totalPages - 1) {
        var lip = li.cloneNode(a);
        lip.children[0].setAttribute("aria-label", "Next");
        lip.children[0].removeAttribute("href");
        lip.children[0].innerHTML = "";
        lip.children[0].appendChild(document.createElement("span"));
        lip.children[0].children[0].setAttribute("class", "sr-only");
        lip.children[0].children[0].innerHTML = "Next";
        result.appendChild(lip);
      }
    }
    $(".pagination").append(result);
  },
  addZero: function (y, s) {
    var stringValue = "" + y;
    if (s > stringValue.length) {
      for (var i = 0; i < s - stringValue.length; i++) y = "0" + y;
    }

    return y;
  },

  seo: function (title, description, keywords, h1, indexFlag) {
    if (typeof title != "undefined" && title != "") {
      document.title = title;
    }

    if (typeof description != "undefined" && description != "") {
      setMetaTag("description", description);
    }

    if (typeof keywords != "undefined" && keywords != "") {
      setMetaTag("keywords", keywords);
    }

    if (typeof h1 != "undefined" && h1 != "") {
      var tags = document.getElementsByTagName("h1");
      if (typeof tags != "undefined" && tags.length > 0) {
        tags[0].innerHTML = h1;
      }
    }

    removeMetaTag("robots");
    if (
      typeof indexFlag == "undefined" ||
      indexFlag == "" ||
      indexFlag != "Y"
    ) {
      appendMetaTag("robots", "noindex,noarchive");
    }

    // 네이버
    removeMetaTag("naver-site-verification");
    appendMetaTag("naver-site-verification", $s.config.naverSiteVerification);

    function appendMetaTag(name, content) {
      var head = document.head;
      var e = document.createElement("meta");
      e.name = name;
      e.content = content;

      head.appendChild(e);
    }

    function setMetaTag(name, content) {
      if (typeof content != "undefined" && content != "") {
        var metas = document.head.getElementsByTagName("meta");

        if (typeof metas != "undefined" && metas.length > 0) {
          for (var i = 0; i < metas.length; i++) {
            if (metas[i].name == name) {
              metas[i].content = content;
              break;
            }
          }
        }
      }
    }

    function removeMetaTag(name) {
      var metas = document.head.getElementsByTagName("meta");

      if (typeof metas != "undefined" && metas.length > 0) {
        for (var i = 0; i < metas.length; i++) {
          if (metas[i].name == name) {
            document.head.removeChild(metas[i]);
          }
        }
      }
    }
  },
  openGraphTag: function (url, title, image, description) {
    try {
      var head = document.head;

      removeMeta("og:url");
      if (typeof url != "undefined" && url != "") {
        head.appendChild(getMeta("og:url", url));
      }

      removeMeta("og:title");
      if (typeof title != "undefined" && title != "") {
        head.appendChild(getMeta("og:title", title));
      }

      removeMeta("og:image");
      if (
        typeof image != "undefined" &&
        image != "" &&
        image != $s.config.noImage
      ) {
        head.appendChild(getMeta("og:image", image));
      }

      removeMeta("og:description");
      if (typeof description != "undefined" && description != "") {
        head.appendChild(getMeta("og:description", description));
      }
    } catch (e) {
      $s.error(e);
    }

    function getMeta(property, content) {
      var e = document.createElement("meta");
      e.setAttribute("property", property);
      e.content = content;

      return e;
    }

    function removeMeta(property) {
      var metas = document.head.getElementsByTagName("meta");

      if (typeof metas != "undefined" && metas.length > 0) {
        for (var i = 0; i < metas.length; i++) {
          if (metas[i].getAttribute("property") == property) {
            document.head.removeChild(metas[i]);
          }
        }
      }
    }
  },

  DateEvent: {
    set: function (element, character, sInput, eInput) {
      sInput = sInput == undefined ? 'input[name="startDate"]' : sInput;
      eInput = eInput == undefined ? 'input[name="endDate"]' : eInput;
      character = character == undefined ? "" : character;
      if (
        $(element).length > 0 &&
        $(sInput).length > 0 &&
        $(eInput).length > 0
      ) {
        var date = new Date();
        var eDate =
          date.getFullYear() +
          character +
          Saleson.addZero(date.getMonth() + 1, 2) +
          character +
          Saleson.addZero(date.getDate(), 2);
        $(element).change(function (e) {
          var val = e.target.value;
          var step = undefined;
          var mode = "today";

          if ("" == val || "clear" == val) {
            $(sInput).val("");
            $(eInput).val("");
          } else {
            if (val.indexOf("week-") == 0) {
              step = val.replace("week-", "");
              mode = "week";
            } else if (val.indexOf("day-") == 0) {
              step = val.replace("day-", "");
              mode = "day";
            } else if (val.indexOf("month-") == 0) {
              step = val.replace("month-", "");
              mode = "month";
            } else if (val.indexOf("year-") == 0) {
              step = val.replace("year-", "");
              mode = "year";
            } else if (val.indexOf("clear-") == 0) {
              $(sInput).val("");
              $(eInput).val("");
            } else {
              // today
              $(sInput).val(eDate);
              $(eInput).val(eDate);
            }
            $(sInput).val(Saleson.DateEvent.getDiffDate(mode, step, character));
            $(eInput).val(eDate);
          }
        });
      }
    },
    // 날짜를 계산해서 리턴..
    getDiffDate: function getDiffDate(mode, value, character) {
      var date = new Date();
      if (mode == "week") {
        date.setDate(date.getDate() - value * 7);
      } else if (mode == "day") {
        date.setDate(date.getDate() - value);
      } else if (mode == "month") {
        date.setMonth(date.getMonth() - value);
      } else if (mode == "year") {
        date.setFullYear(date.getFullYear() - value);
      }
      return (
        date.getFullYear() +
        character +
        Saleson.addZero(date.getMonth() + 1, 2) +
        character +
        Saleson.addZero(date.getDate(), 2)
      );
    },
  },

  // mobile device check (user agent)
  isMobile: function () {
    // https://stackoverflow.com/questions/11381673/detecting-a-mobile-browser, http://detectmobilebrowsers.com/
    if (
      /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
        navigator.userAgent
      ) ||
      navigator.maxTouchPoints > 0
    ) {
      return true;
    } else {
      return false;
    }

    //return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
    /**
        var check = false;
        (function(a){
			console.log('isMobile', a.substr(0,4), a);
			console.log('isMobile sub', a.substr(0,4));
			if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|iPhone|iPad|iPod|blazer|compal|elaine|fennec|hiptop|iemobile|iP(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4)))
			check = true;})(navigator.userAgent||navigator.vendor||window.opera);
        return check;
        */
  },

  // mobile ui check
  isMobileUI: function () {
    // 767px 이하부터 모바일로 판단 (bootstrap.css)
    var body = document.getElementsByTagName("body");
    return body[0].offsetWidth <= 767;
  },

  setCampaignCode: function () {
    var code = $s.core.getParameter("campaign_code");

    if (code != "") {
      $s.core.setSession($s.const.CAMPAIGN_CODE, code);
    }
  },

  getCampaignCode: function () {
    return $s.core.getSession($s.const.CAMPAIGN_CODE);
  },

  getCookie: function (name) {
    name = name + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(";");
    for (var i = 0; i < ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == " ") {
        c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
  },
});

$s.api = {
  domain: $s.config.apiDomain,

  /**
   * 인증 토큰 발행
   *
   * URI: /api/auth/token
   * @HeaderParam Hmac
   * @JsonPath loginType
   * @JsonPath loginId
   * @JsonPath password
   */
  getAuthToken: function (req, successHandler, failureHandler) {
    if (req == null) {
      return;
    }

    var hmacMessage = JSON.stringify(req);
    $s.debug(hmacMessage);

    var hashInBase64 = $s.getHashInBase64(hmacMessage);
    $s.debug("HMAC" + hashInBase64);

    var url = "/api/auth/token";
    try {
      url += "?uid=" + $s.ev.getUid();
    } catch (e) {
      $s.error(e);
    }
    $s.log(hashInBase64);
    $s.axios
      .post(url, req, {
        headers: {
          Hmac: hashInBase64,

        },
      })
      .then(function (response) {
        if ($s.config.isUseNetFunnel) {
          NetFunnel_Complete();
        }
        $s.setToken(response.data.token, response.status, "USER");
        $("#whitebg").css({ display: "none" });
        if ("OBUJE_FAIL" === response.data.code) {
                  vm.birthday = response.data.birthday;
                  $('#temp-birthday').addClass('show');
                  sessionStorage.removeItem("token")
                  return false;
        }
        if ("SLEEP_USER" === response.data.code) {
          // 휴면 회원
          $s.confirm("휴면해제 하시겠습니까?", function () {
            $s.setToken(response.data.token, response.status, "USER");
            $s.api.recovery(function () {
              $s.logout();
              $s.closeAlert();
            });
          });
          $s.cleanToken();
          return;
        }

        if ("PASSWORD_EXPIRED" === response.data.code) {
          // 패스워드 기간 만료
          $s.cleanToken();
          if (vm.pwdParam.loginId !== undefined) vm.pwdParam.loginId = "";
          if ($s.pages.LOGIN == location.pathname) {
            //$("#pwdChangeNotiModal").addClass("show");
            var configData = JSON.parse(response.config.data);
            vm.pwdParam.loginId = configData.loginId;
            $("#pwdChangeModal").addClass("show");
            setTimeout(function () {
              $("#userPW").focus();
            }, 100);
          } else {
            $s.core.setSession($s.const.PASSWORD_EXPIRED, $s.const.STATUS_Y);
            $s.redirect($s.pages.LOGIN);
          }
          return;
        }

        if ("PASSWORD_TEMP" == response.data.code) {
          //임시 비밀번호
          $s.cleanToken();
          $s.alert("임시 비밀번호 사용자 입니다.", function () {
            $s.redirect($s.pages.FIND_ID_PW);
          });
          return;
        }

        //if("ONEPASS_USER" == response.data.code){ // 원패스 회원일 때
        //	$s.cleanToken();
        //	$s.alert("디지털원패스 회원입니다. 디지털원패스로 로그인 하시기 바랍니다.", function () {
        //		$s.closeAlert();
        //    });
        //	if($s.pages.LOGIN != location.pathname){
        //	 	$s.redirect($s.pages.LOGIN);
        //	}
        //	return;
        //}

        if ($s.core.isFunction(successHandler)) {
			//callback(response.data);
          successHandler(response.data);
        }
      })
      .catch(function (error) {
        $("#whitebg").css({ display: "none" });
        if ($s.config.isUseNetFunnel) {
          NetFunnel_Complete();
        }
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },

  getAuthGuestToken: function (req, successHandler, failureHandler) {
    var hmacMessage = JSON.stringify(req);
    $s.debug(hmacMessage);

    var hashInBase64 = $s.getHashInBase64(hmacMessage);
    $s.debug("HMAC" + hashInBase64);

    $s.axios
      .post("/api/auth/guest-token", req, {
        headers: {
          Hmac: hashInBase64,

        },
      })
      .then(function (response) {
        $s.setToken(response.data.token, response.status, "GUEST");

        if ($s.core.isFunction(successHandler)) {
          successHandler();
        }
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },

  /**
   * SNS 인증 토큰 발행
   *
   * URI: /api/auth/sns-token
   * @HeaderParam Hmac
   * @JsonPath snsType
   * @JsonPath snsId
   * @JsonPath email
   * @JsonPath snsName
   */
  getAuthSnsToken: function (req, successHandler, failureHandler) {
    if (req == null) {
      $s.alert("인증 정보가 존재하지 않습니다.");
      return;
    }

    var hmacMessage = JSON.stringify(req);
    var hashInBase64 = $s.getHashInBase64(hmacMessage);

    var url = "/api/auth/sns-token";
    try {
      url += "?uid=" + $s.ev.getUid();
    } catch (e) {
      $s.error(e);
    }

    $s.axios
      .post(url, req, {
        headers: {
          Hmac: hashInBase64,

        },
      })
      .then(function (response) {
        $s.setToken(response.data.token, response.status, "USER");

        if ($s.core.isFunction(successHandler)) {
          successHandler();
        }
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },

  salesonId: function (callback, failureHandler) {
    var id = $s.core.getSession($s.const.SALESON_ID);
    if (id != null && id != "") {
      return;
    }

    var uuid = ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, (c) =>
      (
        c ^
        (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (c / 4)))
      ).toString(16)
    );
    $s.core.setSession($s.const.SALESON_ID, uuid);

    /*$s.axios
            .get("/api/auth/saleson-id", {}, {})
            .then(function (response) {
                $s.core.setSession($s.const.SALESON_ID, response.data.id);
            })
            .catch(function (error) {
                if ($s.core.isFunction(failureHandler)) {
                    failureHandler(error);
                } else {
                    $s.api.handleApiExeption(error, failureHandler);
                }
            });*/
  },

  salesOnLogout: function (callback, failureHandler) {
    $s.axios
      .get("/op_security_logout", {}, {})
      .then(function (response) {})
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },

  seo: function (callback, failureHandler) {
    var uri = getUri();

    if (uri.indexOf("/category?code=") > -1) {
      uri = uri.replace("/category?code=", "/category/?code=");
    }

    $s.axios
      .get("/api/common/seo?uri=" + uri, {}, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });

    function getUri() {
      return getParser().pathname + "" + getParser().search;
    }

    function getParser() {
      var parser = document.createElement("a");
      //parser.href = "http://example.com:3000/pathname/?search=test#hash";
      parser.href = location.href;

      parser.protocol; // => "http:"
      parser.host; // => "example.com:3000"
      parser.hostname; // => "example.com"
      parser.port; // => "3000"
      parser.pathname; // => "/pathname/"
      parser.hash; // => "#hash"
      parser.search; // => "?search=test"
      parser.origin;

      return parser;
    }
  },

  sendAuthNumber: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/send-auth-number", params, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  checkAuthNumber: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/check-auth-number", params, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 회원가입
  joinMember: function (param, callback, failureHandler) {
    $s.axios
      .post("/api/auth/join", param, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 휴면 계정 복구
  recovery: function (callback, failureHandler) {
    $s.axios
      .post(
        "/api/auth/recovery",
        {},
        {
          headers: this.getAuthorizationHeader(),
        }
      )
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getCategoriesUpdatedCheck: function (callback, failureHandler) {
    let d = $s.core.getSession($s.const.CATEGORY_UPDATED_DATE);

    $s.axios
      .get("/api/category/updated-check", {
        params: { d: d },
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        failureHandler(error);
        // $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 카테고리 목록
  getCategories: function (callback, failureHandler) {
    $s.axios
      .get("/api/category", {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 현재 카테고리 목록
  getCurrentCategories: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/category/current", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 카테고리 상품 금액 리스트
  getPriceAreaList: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/category/price-areas", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 카테고리 필터 목록
  getCurrentCategoriesFilter: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/category/filter", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/item", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getItemsNew: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/item/list/list-new", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getItemReviewsForDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/item/reviews", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 등록된 상품후기 리스트
  getItemReviews: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/mypage/reviews", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  addItemReviewLike: function (id, callback, failureHandler) {
    $s.axios
      .post("/api/item/review/add-like/" + id, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getItemReviewInfo: function (id, callback, failureHandler) {
    $s.axios
      .get("/api/item/review/info/" + id, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 미등록 상품후기 리스트
  getUnregisteredItemReviews: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/mypage/nonregistered-reviews", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 이용후기 삭제
  deleteReview: function (itemReviewId, callback, failureHandler) {
    var url = "/api/mypage/delete-review/" + itemReviewId;

    $s.axios
      .post(
        url,
        {},
        {
          headers: this.getAuthorizationHeader(),

        }
      )
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품문의 리스트
  getItemInquiries: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/qna/item-inquiry", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품문의 삭제
  delItemInquiry: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/qna/delete-item-inquiry", params, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 1:1문의
  getInquiries: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/qna/inquiry", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 1:1문의 작성
  createInquiry: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/qna/inquiry", params, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 1:1문의 삭제
  deleteInquiry: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/qna/delete-inquiry", params, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 개인정보보호정책
  getPrivacy: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/policy/protect", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 이용약관
  getPolicy: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/policy/clause", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 저작권 정책
  getCopyright: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/policy/copyright", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 회사소개
  getAbout: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/common/about-us", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 입점문의 작성
  createStoreInquiry: function (formData, callback, failureHandler) {
    $s.axios
      .post("/api/store-inquiry", formData, {
        headers: this.getMultipartHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getItem: function (itemUserCode, callback, failureHandler) {
    $s.axios
      .get("/api/item/" + itemUserCode, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 장바구니 목록
  getCartItems: function (callback, failureHandler) {
    $s.axios
      .get("/api/cart", {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 장바구니 담기
  addToCart: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/cart/add", params, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);

        $s.ga.addToCart(params.arrayRequiredItems);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 장바구니 삭제
  deleteCart: function (cartId, callback, failureHandler) {
    $s.axios
      .post(
        "/api/cart/delete",
        { id: cartId },
        {
          headers: this.getAuthorizationHeader(),

        }
      )
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 장바구니 수량 변경
  updateCartQuantity: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/cart/update-quantity", params, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 장바구니 배송비 지불방법 변경
  updateShippingPaymentType: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/cart/shipping-payment-type", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품 찜하기
  addToWishList: function (itemId, callback, failureHandler) {
    if (!$s.isLogin()) {
      $s.toast("로그인 후 등록이 가능합니다.");
      return;
    }

    $s.axios
      .post(
        "/api/item/wishlist",
        { itemId: itemId },
        {
          headers: this.getAuthorizationHeader(),
        }
      )
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품 찜하기 삭제
  removeToWishList: function (itemId, callback, failureHandler) {
    if (!$s.isLogin()) {
      $s.toast("로그인 후 등록이 가능합니다.");
      return;
    }

    $s.axios
      .post(
        "/api/item/remove-wishlist",
        { itemId: itemId },
        {
          headers: this.getAuthorizationHeader(),
        }
      )
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품 주문
  buyOrder: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/buy", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 마이페이지
  getMypage: function (callback, failureHandler) {
    $s.axios
      .get("/api/mypage", {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 주문목록
  getOrderList: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/order", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 주문상세
  getOrder: function (orderCode, callback, failureHandler) {
    $s.axios
      .get("/api/order/detail?orderCode=" + orderCode, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 쿠폰목록
  getCoupon: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/coupon", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 배송비 쿠폰목록
  getShippingCoupon: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/coupon/shipping-coupons", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  offlineCouponExchange: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/coupon/exchange-offline-coupon", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 쿠폰 적용가능상품
  getAppliesTo: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/coupon/applies-to/" + params.couponId + "/coupon-user", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 포인트 목록
  getPoints: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/mypage/points", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 배송지 목록
  getShipping: function (callback, failureHandler) {
    $s.axios
      .get("/api/shipping", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 배송지 추가&수정
  saveShipping: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/shipping", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 배송지 상태 변경 (목록)
  shippingListAction: function (params, callback, failureHandler) {
    var url = params.mode === "mod" ? "base-shipping" : "delete";

    $s.axios
      .post("/api/shipping/" + url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 배송완료
  shppingComplete: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/shpping-complete", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 구매확정
  confirmPurchase: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/confirm-purchase", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 주문취소
  orderCancel: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/cancel", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품후기 작성
  review: function (formData, callback, failureHandler) {
    var url = "/review";
    $s.axios
      .post("/api/item" + url, formData, {
        headers: this.getMultipartHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 공지사항 목록
  getNotice: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/notice", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 공지사항 조회수 증가
  updateNoticeHits: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/notice/hits", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 공지사항 상세
  detailNotice: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/notice/detail", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 기금사업 소개 목록
  getDonationNotice: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/notice/donation/list", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 자료실 목록
  getDataboard: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/data-board", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 자료실 파일 다운로드
  getDataboardFileDownload: function (params, callback, failureHandler) {
	params.dataFileId = encodeURIComponent(params.dataFileId);
    $s.axios
      .get("/api/data-board/file-download/" + params.dataFileId, {
        params: params,
        headers: this.getAuthorizationHeader(),
        responseType: "blob",
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 자료실 상세
  getDataboardDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/data-board/detail", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 조회
  getQnaOpen: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/qna/qna-open", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 상세 조회
  getQnaOpenDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/qna/qna-detail", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 조회수 증가
  updateQnaOpenHits: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/qna/qna-open/hits", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 파일 다운로드
  getQnaOpenFileDownload: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/qna/qna-open/file-download/" + params.qnaFileId, {
        params: params,
        headers: this.getAuthorizationHeader(),
        responseType: "blob",
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 분류 코드 조회
  getQnaGroupCode: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/qna/qna-group", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 등록
  createQnaOpen: function (formData, callback, failureHandler) {
    $s.axios
      .post("/api/qna/qna-open", formData, {
        headers: this.getMultipartHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 삭제
  deleteQnaOpen: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/qna/qna-open/delete", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // QNA 수정
  updateQnaOpen: function (formData, callback, failureHandler) {
    $s.axios
      .post("/api/qna/qna-open/update", formData, {
        headers: this.getMultipartHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 도움말 조회
  getHelp: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/help", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 도움말 상세 조회
  getHelpDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/help/detail", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 도움말 파일 다운로드
  getHelpFileDownload: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/help/file-download/" + params.mnlSn, {
        params: params,
        headers: this.getAuthorizationHeader(),
        responseType: "blob",
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 도움말 파일 정보 조회 (공통)
  getCommHelpFileInfo: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/help/comm/file-info", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // SNS 연동 정보 확인
  getSnsInfo: function (callback, failureHandler) {
    $s.axios
      .get("/api/auth/sns-info", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 비밀번호 확인
  checkPassword: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/check-password", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 회원정보조회
  getMember: function (callback, failureHandler) {
    $s.axios
      .get("/api/auth/me", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 회원정보수정
  updateMember: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/me", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 회원탈퇴
  secedeMember: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/secede", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // SNS 가입 정보 확인
  checkSnsJoin: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/check-sns-join", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // SNS 계정 연동
  snsJoin: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/sns-join", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // SNS 계정 연동 해제
  disconnectSns: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/disconnect-sns", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // FAQ 목록
  getFaq: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/faq", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // FAQ 조회수 증가
  updateFaqHits: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/faq/hits", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 다운가능한 상품쿠폰 리스트
  downloadItemCouponList: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/item/coupons", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  downloadAllItemCouponList: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/item/download-all-coupons", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  downloadCouponList: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/coupon/download-coupons", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  downloadAllCoupons: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/coupon/download-all-coupons", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 쿠폰 다운로드
  couponDownload: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/coupon/download", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 공통 - 장바구니 정보
  getCartInfo: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/cart-info", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 헤더 - 사용자 정보
  userStatusInfo: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/userStatusInfo", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  findId: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/find-id", params, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 반품신청 정보
  getReturnPop: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/order/return-apply", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 반품신청
  returnProcess: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/return-apply", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 교환신청 정보
  getExchangePop: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/order/exchange-apply", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 교환신청
  exchangeProcess: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/exchange-apply", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 취소신청 정보
  getCancelPop: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/order/cancel-apply", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 취소신청 상세정보
  getRefundAmount: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/refund-amount", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 취소신청
  cancelProcess: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/cancel-apply", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  findPasswordStep1: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/find-password-step1", params, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  findPasswordStep2: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/find-password-step2", params, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  changePassword: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/auth/change-password", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  delayChangePassword: function (callback, failureHandler) {
    $s.axios
      .post(
        "/api/auth/delay-change-password",
        {},
        {
          headers: this.getAuthorizationHeader(),
        }
      )
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 관심상품 목록
  getWishlist: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/mypage/wishlist", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 관심상품 삭제
  delWishlist: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/mypage/delete-wishlist", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 회원 등급 및 혜택안내
  getGrade: function (callback, failureHandler) {
    $s.axios
      .get("/api/mypage/grade", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getQuickInfo: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/quick-info", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getBestKeyword: function (callback, failureHandler) {
    $s.axios
      .get("/api/search/best-keyword", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getRecommendKeyword: function (callback, failureHandler) {
    $s.axios
      .get("/api/search/recommend-keyword", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getBankInfo: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/bank-info", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getPromotion: function (callback, failureHandler) {
    $s.axios
      .get("/api/display/promotion", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getMdItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/display/md", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getNewItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/display/new", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 그룹별 베스트 상품 리스트
  getGroupBestItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/display/group-best", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getEvent: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/event", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getLatelyItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/display/lately", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 베스트 상품 목록 조회
  getBestItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/display/best", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 스팟 상품 조회
  getSpotItems: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/event/spot", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 이벤트 상세
  getEventDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/event/" + params.featuredUrl, {
        headers: this.getAuthorizationHeader(),
        params,
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  naverPay: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/open-market/checkOutReturn", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  naverZzim: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/open-market/checkOutWishReturn", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  naverApiPayment: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/naverpay/payment", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getDisplayStyleBooks: function (callback, failureHandler) {
    $s.axios
      .get("/api/display/style-book", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 스타일북 목록 조회
  getStyleBooks: function (page, callback, failureHandler) {
    $s.axios
      .get("/api/style-book", {
        params: { page: page },
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getStyleBookById: function (id, callback, failureHandler) {
    $s.axios
      .get("/api/style-book/" + id, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getIslandType: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/common/island-info", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 이벤트 댓글 목록
  getEventReply: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/event/replies", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 이벤트 댓글 작성
  createEventReply: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/event/reply", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 통합검색
  getSearchResult: function (params, callback, failureHandler) {
    if (params != null && params.query != null && params.query != "") {
      params.query = decodeURIComponent(params.query);
      params.query = encodeURIComponent(params.query);
    }

    $s.axios
      .get("/api/search/result", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  viewItemRelations: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/item/relation", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 상품 카테고리/지자체/검색 별 조회
  searchGoods: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/categories/searchResult", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  searchSeasonFood: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/season-food/search", {
        headers: this.getAuthorizationHeader(),
        params,
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  searchSeasonFoodDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/season-food/search-season-detail", {
        headers: this.getAuthorizationHeader(),
        params,
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  searchCommunityBusinessDetail: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/community-business/search-community-detail", {
        headers: this.getAuthorizationHeader(),
        params,
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  searchSpecialityInfo: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/speciality/search", {
        headers: this.getAuthorizationHeader(),
        params,
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  searchSpecialityItemList: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/speciality/search-item-list", {
        headers: this.getAuthorizationHeader(),
        params,
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  /**
   * 결제화면 API
   * @param params
   * @param callback
   * @param failureHandler
   */
  paymentStep: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/payment-step", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  /**
   * 결제주문 저장 API
   * @param params
   * @param callback
   * @param failureHandler
   */
  orderSave: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/save", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * 결제처리 API
   * @param params
   * @param callback
   * @param failureHandler
   */
  pay: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/pay", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * 답례품 결제주문 저장 및 결제처리 API
   * @param params
   * @param callback
   * @param failureHandler
   */
  giveGoodsSavePay: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/order/giveGoodsSavePay", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * qr 코드 테스트
   * @param params
   * @param callback
   * @param failureHandler
   */
  getTestQrCode: function (callback, failureHandler) {
    $s.axios
      .get("/api/mypage/qrTest", "", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * 명예시도민증 qr 코드 가져오기
   * @param params
   * @param callback
   * @param failureHandler
   */
  getQrCode: function (callback, failureHandler) {
    $s.axios
      .post("/api/mypage/qr", "", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * 명예시도민증 조회 - 신규
   * @param params
   * @param callback
   * @param failureHandler
   */
  getHonorListNew: function (callback, failureHandler) {
    $s.axios
      .post("/api/mypage/honorList-new", "", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * 기부혜택증 열랍이력 저장
   * @param params
   * @param callback
   * @param failureHandler
   */
  saveHonorViewHist: function (lclgvCd, callback, failureHandler) {
    $s.axios
      .post("/api/mypage/saveHonorViewHist", lclgvCd, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },


  // 지정기부 공지사항 조회
  getDesignatedDonationNotice: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/designated-donation/getNoticeList", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 소식지 메인화면 데이터 조회 (param : {cataglogYear: '발간년도', catalogNo: '발간호수'})
  getCatalogMainInfo: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/catalog/getCatalogMainInfo", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 팝업 리스트 조회
  getPopups: function (callback, failureHandler) {
    $s.axios
      .get("/api/popup/list", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 팝업 조회 (ID)
  getPopupById: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/popup/index/" + params.id, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  /**
   * 통합검색 페이지
   */
  getTotalSearchLocgov: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/locgov", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchItem: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/item", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchNotice: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/notice", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchQna: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/qna", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchFaq: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/faq", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchDataboard: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/databoard", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchGuidance: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/guidance", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchEvent: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/event", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchSpecialitem: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/specialitem", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchPopular: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/popular", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchAutoKeyword: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/autokeyword", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getTotalSearchMyRecent: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/myrecent", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  setTotalSearchInsertRecent: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/insert-recent", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  setTotalSearchDeleteRecent: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/delete-recent", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  setTotalSearchDeleteRecentAll: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/totalsearch/delete-recent-all", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getAlternate: function(params, callback, failureHandler){
    $s.axios
      .get("/api/common/alternate", {
        param: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  /**
   * 팝업 띄우기
   */
  popup: {
    popupClose: function (popupType, popupId) {
      if (popupType === "1" && !$s.isMobile()) {
        self.close();
      } else {
        $("#popup_" + popupId).hide();
      }
    },
    setCookie: function (popupType, popupId) {
      var expire = new Date();

      // 쿠키 저장
      expire.setDate(expire.getDate() + 1);
      document.cookie =
        "popup_check_" +
        popupId +
        "=1; expires=" +
        expire.toGMTString() +
        "; path=/";

      // 팝업 닫기
      //$s.api.popup.popupClose(popupType, popupId);
    },
    makePopup: function (list) {
      try {
        var openLayerPopups = [];

        if (typeof list !== "undefined" && list !== null) {
          for (var i = 0; i < list.length; i++) {
            var popup = list[i];

            // 쿠키 정보 - 오늘 하루 이 창을 열지 않음 (1)
            var cookie = $.cookie("popup_check_" + popup.popupId);

            if (cookie !== "1") {
              if ($s.isMobile()) {
                popup.popupStyle = "";
                popup.handleStyle = "";

                openLayerPopups.push(popup);
              } else if (popup.popupType === "1") {
                0; // 윈도우 팝업 띄우기 - kdj 임시 /popup/? -> /popup/index.html? 로 바꿈
                Common.popup(
                  "/popup/index.html?id=" + popup.popupId,
                  "openPopup" + popup.popupId,
                  popup.width + 17,
                  popup.height + 117,
                  0,
                  popup.leftPosition,
                  popup.topPosition
                );
              } else if (popup.popupType === "2") {
                // 레이어 팝업 정보 세팅
                popup.popupStyle =
                  "position:absolute;" +
                  "left:" +
                  popup.leftPosition +
                  "px;" +
                  "top:" +
                  popup.topPosition +
                  "px;" +
                  "z-index:9999;" +
                  "width:" +
                  (popup.width + 20) +
                  "px;";
                  // +
                  // "height:" +
                  //popup.height +
                  //"px;";

                popup.handleStyle =
                  "width:" +
                  (popup.width + 20) +
                  "px;" +
                  "height:15px;cursor:move;" +
                  "background:" +
                  popup.backgroundColor +
                  ";";

                openLayerPopups.push(popup);
              }
            }
          }

          // 레이어 팝업 띄우기
          if (openLayerPopups.length > 0) {
            var child = Vue.child("layout-popup");
            if (child !== null) {
              child.makeLayerPopup(openLayerPopups);
            }
          }
        }
      } catch (e) {
        $s.error(e);
      }
    },
  },

  // 답례품 메인화면 대표배너 조회
  getRepresentativeBanners: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/representative-banner/select", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * sns 공유
   */
  social: {
    twitter: function (title, description, imageUrl, link) {
      // var ranNum = Math.floor(Math.random() * 10);
      const array = new Uint32Array(1);
      var ranNum = window.crypto.getRandomValues(array);
      var url =
        "http://twitter.com/share" +
        "?url=" +
        encodeURIComponent(link) +
        "&text=" +
        encodeURIComponent(title) +
        "&nocache=" +
        ranNum;

      $s.api.social.popup(url);
    },
    kakao: function (title, description, imageUrl, link) {
      try {
        var initFlag = $s.core.getSession($s.const.KAKAO_SHARE_INIT_FLAG);

        if (
          typeof initFlag == "undefined" ||
          initFlag == null ||
          initFlag == "false"
        ) {
          Kakao.init($s.config.kakaoLoginAppId);
          $s.core.setSession($s.const.KAKAO_SHARE_INIT_FLAG, "true");
        }

        if (typeof description == "undefined" || description == null) {
          description = "";
        }

        Kakao.Link.sendDefault({
          objectType: "feed",
          content: {
            title: title,
            description: description,
            imageUrl: imageUrl,
            link: {
              mobileWebUrl: link,
            },
          },
          success: function (response) {
            $s.log(response);
          },
          fail: function (error) {
            $s.log(error);
          },
        });
      } catch (e) {
        $s.toast("카카오 공유에 실패 했습니다");
        $s.error("kakao share error");
      }
    },
    facebook: function (title, description, imageUrl, link) {
      try {
        FB.init({
          appId: $s.config.facebookLoginAppId,
          xfbml: true,
          version: "v6.0",
        });

        var href = $s.api.getShareLink(title, description, imageUrl, link);

        $s.log(href);
        /*                FB.ui({
                    method:'share',
                    href: href
                });*/

        var snsShareUrl =
          "http://www.facebook.com/share.php?u=" + encodeURIComponent(href);

        var win = window.open(
          snsShareUrl,
          "sharer",
          "toolbar=0, status=0, width=626, height=436"
        );
        if (win) {
          win.focus();
        }
      } catch (e) {
        $s.toast("페이스북 공유에 실패 했습니다");
        $s.error("facebook share error");
      }
    },
    kakaoStory: function (title, description, imageUrl, link) {
      //var ranNum = Math.floor(Math.random() * 10);
      const array = new Uint32Array(1);
      var ranNum = window.crypto.getRandomValues(array);
      var url =
        "https://story.kakao.com/share" +
        "?url=" +
        encodeURIComponent(link) +
        "&text=" +
        encodeURIComponent(title) +
        "&nocache=" +
        ranNum;

      $s.api.social.popup(url);
    },
    naverBand: function (title, description, imageUrl, link) {
      //var ranNum = Math.floor(Math.random() * 10);
      const array = new Uint32Array(1);
      var ranNum = window.crypto.getRandomValues(array);
      var url =
        "http://band.us/plugin/share" +
        "?url=" +
        encodeURIComponent(link) +
        "&text=" +
        encodeURIComponent(title) +
        "&nocache=" +
        ranNum;

      $s.api.social.popup(url);
    },
    popup: function (url) {
      var win = window.open(
        url,
        "sharer",
        "toolbar=0, status=0, width=626, height=436"
      );

      if (win) {
        win.focus();
      }
    },
  },
  getShareLink: function (title, description, imageUrl, link) {
    try {
      if (typeof link == "undefined" || link == null || link == "") {
        link = $s.config.virtualDomain;
      }

      link = encodeURIComponent(link);
      title = encodeURIComponent(title);
      imageUrl = encodeURIComponent(imageUrl);
      description = encodeURIComponent(description);

      return (
        $s.config.cdnDomain +
        "" +
        "/share" +
        "?u=" +
        link +
        "" +
        "&t=" +
        title +
        "" +
        "&i=" +
        imageUrl +
        "" +
        "&d=" +
        description
      );
    } catch (e) {
      $s.error(e);
      return "";
    }
  },

  // 사용 가능한 쿠폰 목록
  getCoupons: function (callback, failureHandler) {
    $s.axios
      .get("/api/order/coupons", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 입점문의 작성시 전화번호 앞자리, 이메일주소 뒷자리 리스트
  getStoreInquiry: function (callback, failureHandler) {
    $s.axios
      .get("/api/store-inquiry", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getItemQna: function (param, callback, failureHandler) {
    $s.axios
      .get("/api/item/qna", {
        params: param,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  createItemQna: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/qna/item-inquiry", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 재입고 알림
  getRestockNotice: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/item/restock", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 재입고 알림 신청
  restockNotice: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/item/restock", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  // 마케팅이용약관
  getPolicyMarketing: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/policy/marketing", {
        params: params,
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 온라인 입금계좌
  getAccountNumbers: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/account-numbers", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  /**
   * Header Authorization Bearer
   * @returns {{headers: {Authorization: string}}}
   */
  getAuthorizationHeader: function () {
    var token = $s.core.getSession($s.const.TOKEN);
    var salesOnId = $s.core.getSession($s.const.SALESON_ID);
    return {
      Authorization: "Bearer " + token,
      SALESONID: salesOnId,
    };
  },

  getMultipartHeader: function () {
    var token = $s.core.getSession($s.const.TOKEN);
    var salesOnId = $s.core.getSession($s.const.SALESON_ID);
    return {
      Authorization: "Bearer " + token,
      SALESONID: salesOnId,
      "Content-type": "multipart/form-data",
    };
  },

  saveVisitData: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/common/visit", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // (공통) 사용자 메뉴 사용 이력 저장
  saveUserActionLog: function (params, callback, failureHandler) {
    $s.axios
      .post("/api/log/action", params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
    },

    // 지정기부 목록 조회
    getDesignatedDonationList: function (params, callback, failureHandler) {
        $s.axios
            .get("/api/designated-donation/getList", {
                params: params
                , headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },

    // 지정기부 목록 조회 - 검색 엔진
    getDesignatedDonationListBySearchEngine: function (params, callback, failureHandler) {
        $s.axios
            .get("/api/designated-donation/getListBySearchEngine", {
                params: params
                , headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },

    // 지정기부 사업구분, 상태 조회
    getBsnsTypes: function (callback, failureHandler) {
        $s.axios
            .post("/api/designated-donation/getBsnsTypes", {
                headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },

    // 지정기부 상세조회
    getDesignatedDonationDetail: function (prjId, callback, failureHandler) {
        $s.axios
            .get("/api/designated-donation/getDetail", {
                params: {prjId: prjId}
                , headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },

    // 지정기부 내역조회
    getDesignatedCntrList: function (param, callback, failureHandler) {
        $s.axios
            .get("/api/designated-donation/getDesignatedCntrList", {
                params: param
                , headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },
    // 응원메시지 저장
    saveCheerMsg: function (param, callback, failureHandler) {
        $s.axios
            .get("/api/designated-donation/saveCheerMsg", {
                params: param
                , headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },



  googleAnalyticsCommonTrackingScript: function (callback, failureHandler) {
    $s.axios
      .get("/api/google-analytics/common-tracking-script", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  googleAnalyticsPurchase: function (params, callback, failureHandler) {
    var url = "/api/google-analytics/purchase";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  googleAnalyticsAddToCart: function (params, callback, failureHandler) {
    var url = "/api/google-analytics/add-to-cart";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  googleAnalyticsRemoveFromCart: function (params, callback, failureHandler) {
    var url = "/api/google-analytics/remove-from-cart";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  googleAnalyticsChangeFromCartQuantity: function (
    params,
    callback,
    failureHandler
  ) {
    var url = "/api/google-analytics/change-from-cart-quantity";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  googleAnalyticsDetail: function (params, callback, failureHandler) {
    var url = "/api/google-analytics/detail";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  googleAnalyticsCheckout: function (params, callback, failureHandler) {
    var url = "/api/google-analytics/checkout";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  eventlogItem: function (params, callback, failureHandler) {
    var url = "/api/event-log/item";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  eventlogOrder: function (params, callback, failureHandler) {
    var url = "/api/event-log/order";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  eventlogFeatued: function (params, callback, failureHandler) {
    var url = "/api/event-log/featured";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  eventlogJoinUser: function (params, callback, failureHandler) {
    var url = "/api/event-log/join-user";

    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  confirmPbanc: function (params, callback, failureHandler) {
        $s.axios
            .get("/api/common/confirm-pbanc", {
                params: params
                , headers: this.getAuthorizationHeader()
            })
            .then(function (response) {
                callback(response);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error, failureHandler);
            });
    },

  getErrorMessage: function (error) {
    if (error.response) {
      var erd = error.response.data;
      return erd.message == null || erd.message == ""
        ? erd.description
        : erd.message;
    } else {
      return error;
    }
  },
  handleApiExeption: function (error, failureHandler) {
    // console.log(error);

	if (error && error.response && error.response.data && error.response.data.redirect) {
		$s.redirect(error.response.data.redirect);
		return;
	}

    if (
      error &&
      (error.message == "Duplication Call" || error.message == "None Url")
    ) {
      // 중복호출시 오류 처리 안함
      try {
		switch(error.url) {
			case "/api/category":
			case "/api/ngdonation/sidoList":
				if (failureHandler && typeof failureHandler == 'function') {
					failureHandler(error);
				}
				return;
		}
      } catch (e) {
        $s.error(e);
      }
      return;
    }

    if ($s.core.isFunction(failureHandler)) {
      failureHandler(error);
    } else {
      if (error.response) {
        /*console.log('data', error.response.data);
                console.log('status', error.response.status);
                console.log('headers', error.response.headers);*/

        //                $s.core.setSession($s.const.TOKEN_STATUS, error.response.status);

        if (error.response.status === 401) {
          // 토큰만료
          /*
					if ("DUPLICATION_LOGIN" === error.response.data.code) {	// 중복 로그인
						$s.alert("다른 기기에서 로그인하여 자동으로 로그아웃 되었습니다.", function () {
							$s.logout();
                    	});
	                    return;
					}
					*/

          if ("SLEEP_USER" === error.response.data.code) {
            // 휴면 회원
            // $s.alert("휴면 전환된 계정입니다.", function () {
            //     $s.redirect($s.pages.SLEEP_USER);
            // });
            // return;
          }

          //if ("PASSWORD_EXPIRED" === error.response.data.code) { // 패스워드 기간 만료
          // if($s.pages.LOGIN == location.pathname){
          //     $("#pwdChangeNotiModal").addClass("show");
          // }else{
          //     $s.core.setSession($s.const.PASSWORD_EXPIRED, $s.const.STATUS_Y);
          //     $s.redirect($s.pages.LOGIN);
          // }
          // return;
          //}
          //                    if ("SLEEP_USER" === error.response.data.code) { // 휴면 회원
          //                           $s.confirm("휴면해제 하시겠습니까?", function () {
          //					            $s.api.recovery(function(){
          //					                $s.cleanToken();
          //					                $s.closeAlert();
          //					            });
          //						    });
          //                        return;
          //                    }
          //
          if ("PASSWORD_EXPIRED" === error.response.data.code) {
            // 패스워드 기간 만료
            $s.cleanToken();
            if (vm.pwdParam.loginId !== undefined) vm.pwdParam.loginId = "";
            if ($s.pages.LOGIN == location.pathname) {
              //$("#pwdChangeNotiModal").addClass("show");
              var configData = JSON.parse(response.config.data);
              vm.pwdParam.loginId = configData.loginId;
              $("#pwdChangeModal").addClass("show");
            } else {
              $s.core.setSession($s.const.PASSWORD_EXPIRED, $s.const.STATUS_Y);
              $s.redirect($s.pages.LOGIN);
            }
            return;
          }

          $s.cleanToken();
          alert("로그인 후 이용이 가능합니다.");
          try {
              if ($s.requestContext.requestFullUri.indexOf("/admin/order-agency") > -1) {
                  $s.redirect("/admin/order-agency/login.html");
              } else {
                  $s.redirect($s.pages.LOGIN + "?target=" + encodeURIComponent($s.requestContext.requestFullUri));
              }
          } catch (e) {
              $s.redirect($s.pages.LOGIN + "?target=" + encodeURIComponent($s.requestContext.requestFullUri));
          }
          //$s.logout();
          return;
        }
        $s.alert(error.response.data.description, failureHandler);
        //$s.alert($s.api.getErrorMessage(error));
      } else {
        $s.alert(error, failureHandler);
      }
    }
  },
  // post
  postSubmitNoAuth: function (url, params, callback, failureHandler) {
    $s.axios
      .post(url, params, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },
  // get
  getSubmitNoAuth: function (url, params, callback, failureHandler) {
    $s.axios
      .get(url, { params: params }, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },
  // post
  postSubmit: function (url, params, callback, failureHandler, isNoHeader) {
    $s.axios
      .post(url, params, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.error(error);
        //if ($s.core.isFunction(failureHandler)) {
        //  failureHandler(error);
        //} else {
          $s.api.handleApiExeption(error, failureHandler);
        //}
      });
  },
  // get
  getSubmit: function (url, params, callback, failureHandler) {
    $s.axios
      .get(url, {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        //if ($s.core.isFunction(failureHandler)) {
        //  failureHandler(error);
        //} else {
          $s.api.handleApiExeption(error, failureHandler);
        //}
      });
  },
  // 외부 인증망(SCI 휴대폰 / 금융인증서)이 없는 환경에서 본인인증을 통과시키는 우회.
  // 실제 인증 팝업이 window.postMessage 로 돌려주는 것과 같은 형태의 결과를 흘려보내서
  // 이후 단계(checkMobileAuth -> 정보입력 등)는 실제 흐름 그대로 타게 한다.
  //
  // options.fresh = true  : 새 CI 발급 (회원가입 — CI 가 겹치면 기가입자로 튕겨냄)
  //           그 외        : 직전에 발급한 CI 재사용 (정보수정/아이디찾기 등 재인증 —
  //                          같은 브라우저에서 가입한 계정과 CI 가 맞아야 본인으로 인식됨)
  skipExternalAuth: function (methodName, options) {
    var STORAGE_KEY = "localTestMberCi";
    var fresh = !!(options && options.fresh);

    var pad = function (prefix, uniq, length) {
      var value = prefix + uniq;
      while (value.length < length) {
        value += "0";
      }
      return value.substring(0, length);
    };

    var uniq = null;
    if (!fresh) {
      try {
        uniq = localStorage.getItem(STORAGE_KEY);
      } catch (e) {}
    }
    if (!uniq) {
      uniq = String(Date.now());
      try {
        localStorage.setItem(STORAGE_KEY, uniq);
      } catch (e) {}
    }

    var payload = {
      userName: "테스트회원",
      birthday: "19900101",
      gender: "M",
      cellNo: "01012345678",
      mberCi: pad("LOCALTESTCI", uniq, 88),
      mberDi: pad("LOCALTESTDI", uniq, 64),
    };

    alert(
      "[로컬 인증 우회] " + methodName + "\n\n" +
        "외부 인증 연동 없이 아래 정보로 인증 성공 처리합니다.\n\n" +
        "이름: " + payload.userName + "\n" +
        "생년월일: " + payload.birthday + "\n" +
        "휴대폰: " + payload.cellNo + "\n" +
        "CI: " + payload.mberCi.substring(0, 22) + "..."
    );

    window.postMessage(payload, "*");
  },
  mobileAuth: function (callback, failureHandler) {
    $s.axios
      .get("/api/auth/mobile-auth", {}, {})
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },
  onepassLogin: function (callback, failureHandler) {
    $s.axios
      .get("/api/auth/onepass-login?serviceType=LOGIN", {}, {})
      .then(function (response) {
        callback(response);
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },
  onepassAuthToken: function (req, successHandler, failureHandler) {
    if (req == null) {
      return;
    }

    var hmacMessage = JSON.stringify(req);
    $s.debug(hmacMessage);

    var hashInBase64 = $s.getHashInBase64(hmacMessage);
    $s.debug("HMAC" + hashInBase64);

    var url = "/api/auth/onepass-token";
    try {
      url += "?uid=" + $s.ev.getUid();
    } catch (e) {
      $s.error(e);
    }

    $s.axios
      .post(url, req, {
        headers: {
          Hmac: hashInBase64,

        },
      })
      .then(function (response) {
        $s.setToken(response.data.token, response.status, "USER");

        //    if ("SLEEP_USER" === response.data.code) { // 휴면 회원
        //        $s.confirm("휴면해제 하시겠습니까?", function () {
        //            //$s.redirect($s.pages.SLEEP_USER);

        //        });
        //        return;
        //    }

        //                if ("PASSWORD_EXPIRED" === response.data.code) { // 패스워드 기간 만료
        //                    $s.alert("패스워드 기간이 만료 되었습니다.", function () {
        //                        $s.redirect($s.pages.CHANGE_PASSWORD);
        //                    });
        //                    return;
        //                }

        if ($s.core.isFunction(successHandler)) {
          successHandler();
        }
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },

  // donation - 지자체 시도 목록
  getSido: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/ngdonation/sidoList", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // donation - 지자체 시군구 목록
  getSiGunGu: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/ngdonation/sigunguList", {
        params: params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  getSimpleAuthResult: function (req, callback, failureHandler) {
    if (req == null) {
      return;
    }

    var hmacMessage = JSON.stringify(req);
    $s.debug(hmacMessage);

    var hashInBase64 = $s.getHashInBase64(hmacMessage);
    $s.debug("HMAC" + hashInBase64);

    var url = "/api/auth/getSimpleAuthResult";
    try {
      url += "?uid=" + $s.ev.getUid();
    } catch (e) {
      $s.error(e);
    }

    $s.axios
      .post(url, req, {
        headers: {
          Hmac: hashInBase64,

        },
      })
      .then(function (response) {
        if ($s.config.isUseNetFunnel) {
          NetFunnel_Complete();
        }
        var res = response.data;
        if (res.data.message == "Success") {
          $s.setToken(res.data.token, response.status, "USER");
          $s.redirect(vm.target);
        } else if (res.data.message == "NoUser") {
          $s.confirm2(
            "간편인증은 회원가입 또는 휴대폰 본인인증 후 사용 가능 합니다.",
            "회원가입",
            "확인",
            $s.redirect.bind(this, "/users/join.html"),
            null
          );
        } else if (res.data.message == "Error") {
          $s.alert("오류가 발생하였습니다.");
        }
      })
      .catch(function (error) {
        if ($s.config.isUseNetFunnel) {
          NetFunnel_Complete();
        }
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },

  getSessionTimeout: function (callback, failureHandler) {
    var url = "/api/auth/session-timeout";

    $s.axios
      .get(url, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getCategoryPath: function (params, callback, failureHandler) {
    $s.axios
      .get("/api/category/category-path", {
        params,
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },

  // 암호화 키 가져오기
  getPublicKey: function (callback, failureHandler) {
    $s.axios
      .post("/api/ngdonation/getPublicKey", "", {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },
  // 외국인 등록번호 거소조회
  rsgstadresinfoForeigner: function (param, callback, failureHandler) {
    $s.axios
      .post("/api/ngdonation/rsgstadresinfoForeigner", param, {
        headers: this.getAuthorizationHeader(),

      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        if ($s.core.isFunction(failureHandler)) {
          failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
      });
  },
  // api 서버 체크
  healthcheck: function () {
    $s.axios
      .get("/api/healthcheck", "", {
        headers: this.getAuthorizationHeader(),

      })
      .catch(function (error) {
        $s.api.handleApiExeption(error);
      });
    },
    // 주문대행 암호화 키 조회
    getOrderAgencyPublicKey: function (callback) {
        $s.axios
            .get("/api/order-agency/getPublicKey", {
                headers: this.getAuthorizationHeader(),
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error);
            });
    },
    // 주문대행 로그인 정보 체크
    checkAgencyLoginInfo: function (params, callback) {
        $s.axios
            .post("/api/order-agency/checkAgencyLoginInfo", params, {
                headers: this.getAuthorizationHeader(),
            })
            .then(function (response) {

                if (response.data.result.authToken) {
                    $s.setToken(response.data.result.authToken, response.status, "USER");
                }

                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error);
            });
    },
    // 주문대행 기부자 본인 인증 정보 저장
    saveAgencyCntrbtrInfo: function (params, callback) {
        $s.axios
            .post("/api/order-agency/saveAgencyCntrbtrInfo", params, {
                headers: this.getAuthorizationHeader(),
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
                $s.api.handleApiExeption(error);
            });
    },
    // 주문대행 기부자 기부 포인트 조회
    getAgencyPointList: function (params, callback, failureHandler) {
        $s.axios
            .post("/api/order-agency/getOrderAgencyCntrPointList", params, {
                headers: this.getAuthorizationHeader(),
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
		        if ($s.core.isFunction(failureHandler)) {
		          failureHandler(error);
		        } else {
		          $s.api.handleApiExeption(error, failureHandler);
		        }
            });
    },
    // 주문대행 답례품 상세 조회
    getAgencyItem: function (itemUserCode, callback, failureHandler) {
        $s.axios
            .post("/api/order-agency/item-detail/" + itemUserCode, "", {
                headers: this.getAuthorizationHeader(),
            })
            .then(function (response) {
                callback(response.data);
            })
            .catch(function (error) {
		        if ($s.core.isFunction(failureHandler)) {
		          failureHandler(error);
		        } else {
		          $s.api.handleApiExeption(error, failureHandler);
		        }
            });
    },
	/**
	 * 주문대행 결제화면 API
	 * @param params
	 * @param callback
	 * @param failureHandler
	 */
	getAgencyPaymentStep: function (params, callback, failureHandler) {
		$s.axios
			.post("/api/order-agency/payment-step", params, {
				headers: this.getAuthorizationHeader(),
			})
			.then(function (response) {
				callback(response.data);
			})
			.catch(function (error) {
		        if ($s.core.isFunction(failureHandler)) {
		          failureHandler(error);
		        } else {
		          $s.api.handleApiExeption(error, failureHandler);
		        }
			});
	},

	/**
	 * 답례품 결제주문 저장 및 결제처리 API
	 * @param params
	 * @param callback
	 * @param failureHandler
	 */
	orderAgencyGiveGoodsSavePay: function (params, callback, failureHandler) {
		$s.axios
			.post("/api/order-agency/giveGoodsSavePay", params, {
				headers: this.getAuthorizationHeader(),
			})
			.then(function (response) {
				callback(response.data);
			})
			.catch(function (error) {
		        if ($s.core.isFunction(failureHandler)) {
		          failureHandler(error);
		        } else {
		          $s.api.handleApiExeption(error, failureHandler);
		        }
			});
	},

	// 주문상세
	getOrderAgencyOrder: function (orderCode, callback, failureHandler) {
		$s.axios
			.post("/api/order-agency/order-detail?orderCode=" + orderCode, "", {
				headers: this.getAuthorizationHeader(),
			})
			.then(function (response) {
				callback(response.data);
			})
			.catch(function (error) {
		        if ($s.core.isFunction(failureHandler)) {
		          failureHandler(error);
		        } else {
		          $s.api.handleApiExeption(error, failureHandler);
		        }
			});
	},

	// 상품 주문
	orderAgencyBuyOrder: function (params, callback, failureHandler) {
		$s.axios
			.post("/api/order-agency/buy", params, {
				headers: this.getAuthorizationHeader(),
		})
		.then(function (response) {
			callback(response.data);
		})
		.catch(function (error) {
	        if ($s.core.isFunction(failureHandler)) {
	          failureHandler(error);
	        } else {
	          $s.api.handleApiExeption(error, failureHandler);
	        }
		});
	},

	// 상품 상세 조회(일회용)
	getOrderAgencyOrderDetail: function (dataId, callback, failureHandler) {
		$s.axios
			.post("/api/order-agency/order-detail-data?dataId=" + dataId, "", {
				headers: this.getAuthorizationHeader(),
		})
		.then(function (response) {
			callback(response.data);
		})
		.catch(function (error) {
	        if ($s.core.isFunction(failureHandler)) {
	          failureHandler(error);
	        } else {
	          $s.api.handleApiExeption(error, failureHandler);
	        }
		});
	},

	// 총 기부금 현황 조회
	getGiveState: function(params, callback, failureHandler) {
		$s.axios
			.get("/api/common/getGiveState", {
				headers: this.getAuthorizationHeader(),
				params: params,
			})
			.then((response) => {
				callback(response.data);
			})
			.catch((error) => {
				if ($s.core.isFunction(failureHandler)) {
        	failureHandler(error);
        } else {
          $s.api.handleApiExeption(error, failureHandler);
        }
			});
	},

	// 로그인 유저 정보 조회
	userInfo: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/userInfo", {
        headers: this.getAuthorizationHeader(),
      })
      .then(function (response) {
        callback(response.data);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
  getRealTimeWaitUser: function (callback, failureHandler) {
    $s.axios
      .get("/api/common/getRealTimeWaitUser", {
        headers: this.getAuthorizationHeader(),
      })
      .then((response) => {
        callback(response);
      })
      .catch(function (error) {
        $s.api.handleApiExeption(error, failureHandler);
      });
  },
};

$s.core = {
  const: {
    TOKEN: "token",
  },

  authenticationContext: {},
  authenticationFilter: function (guestHandler, authHandler) {
    // 인증정보 처리
    if ($s.isLogin()) {
      this.authenticationContext.accessToken = $s.core.getSession(
        this.const.TOKEN
      );
      //$s.log("authContext : " + JSON.stringify(this.authenticationContext));

      if (this.isFunction(authHandler)) {
        authHandler();
      }
    } else {
      if (this.isFunction(guestHandler)) {
        guestHandler();
      }
    }
  },
  isAuthenticated: function () {
    if (
      this.getSession($s.const.TOKEN) == null ||
      this.getSession($s.const.TOKEN_STATUS) != 200
    ) {
      //$s.debug('token is not set');
      return false;
    } else {
      //$s.debug('token is ' + this.getSession($s.const.TOKEN));
      return true;
    }
  },
  isFunction: function (func) {
    return func != null && typeof func === "function" ? true : false;
  },
  getSession: function (key) {
    return sessionStorage.getItem(key);
  },
  setSession: function (key, value) {
    sessionStorage.setItem(key, value);
  },
  removeSession: function (key) {
    sessionStorage.removeItem(key);
  },
  getData: function (key) {
    return localStorage.getItem(key);
  },
  setData: function (key, value) {
    localStorage.setItem(key, value);
  },
  removeData: function (key) {
    localStorage.removeItem(key);
  },
  parseUrl: function (url) {
    var parser = document.createElement("a");
    parser.href = url;

    // IE 8 and 9 dont load the attributes "protocol" and "host" in case the source URL
    // is just a pathname, that is, "/example" and not "http://domain.com/example".
    parser.href = parser.href;

    // IE 7 and 6 wont load "protocol" and "host" even with the above workaround,
    // so we take the protocol/host from window.location and place them manually
    if (parser.host === "") {
      var newProtocolAndHost =
        window.location.protocol + "//" + window.location.host;
      if (url.charAt(1) === "/") {
        parser.href = newProtocolAndHost + url;
      } else {
        // the regex gets everything up to the last "/"
        // /path/takesEverythingUpToAndIncludingTheLastForwardSlash/thisIsIgnored
        // "/" is inserted before because IE takes it of from pathname
        var currentFolder = ("/" + parser.pathname).match(/.*\//)[0];
        parser.href = newProtocolAndHost + currentFolder + url;
      }
    }

    // copies all the properties to this object
    var properties = [
      "host",
      "hostname",
      "hash",
      "href",
      "port",
      "protocol",
      "search",
    ];
    for (var i = 0, n = properties.length; i < n; i++) {
      this[properties[i]] = parser[properties[i]];
    }

    // pathname is special because IE takes the "/" of the starting of pathname
    this.pathname =
      (parser.pathname.charAt(0) !== "/" ? "/" : "") + parser.pathname;

    // requestUri
    this.requestUri = this.pathname;
    this.requestFullUri = this.pathname + this.search;

    return this;
  },

  getParameter: function (name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
    var results = regex.exec(location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  },

  salesonId: function () {},
};

$s.validator = {
  patterns: {
    number: /^[\-|0-9]+$/,
    number_negative: /^(0|[-]?[1-9][0-9]*)+$/,
    number_comma: /^[0-9\,]+$/,
    number_only: /^[0-9]+$/,
    minlength: /^[0-9]+$/,
    length: /^[0-9]+$/,
    min: /^[0-9]+$/,
    max: /^[0-9]+$/,
    korean: /^[가-힝]+$/,
    phone: /^0\d{2}[-]{0,1}\d{3,4}[-]{0,1}\d{4}$/,
    tel: /^\d{0,3}[-]{0,1}\d{3,4}[-]{0,1}\d{4}$/,
    last_phone: /^\d{3,4}[-]{0,1}\d{4}$/,
    date: /^\d{1,4}\d((0?\d)|(1[012]))\d([012]?\d|30|31)$/,
    email: /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,3}$/,
    first_email: /^([0-9a-zA-Z_\.-]+)$/,
    last_email: /^([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,3}$/,
    emailAt: /^@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,3}$/,
    id: /^([a-z]{1})([0-9a-z_@\.\-]{5,29})$/,
    password:
      /(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^*()\-_=+\\\|\[\]{};:\'",.<>\/?]).{8,20}$/,
    emoji: /^[^\uD83C-\uDBFF\uDC00-\uDFFF]+$/,
  },
  messages: {
    number: "숫자로만 입력해 주세요.",
    number_negative: "숫자로만 입력해 주세요.",
    number_comma: "숫자로만 입력해 주세요.",
    number_only: "숫자로만 입력해 주세요.",
    korean: "한글로만 입력해 주세요.",
    phone: "정확히 입력해 주세요.",
    tel: "정확히 입력해 주세요.",
    last_phone: "7~8자리의 숫자만 입력해 주세요.",
    date: "날짜 형식을 YYYYMMDD 형태로 정확히 입력해 주세요.",
    email: "이메일 주소를 정확히 입력해 주십시오.",
    first_email: "이메일 처음 부분에 한글을 입력할 수 없습니다.",
    last_email: "이메일 마지막 부분에 한글을 입력할 수 없습니다.",
    id: '아이디는 영문으로 시작하고, 영문(소문자), 숫자, 특수문자("@","-","_",".") 조합으로 6~30글자로 입력해 주십시오.',
    password:
      "비밀번호는 영문/숫자/특수문자를 혼합하여 8자 이상 20자 이하로 입력하세요.",
    password_confirm: "비밀번호가 일치하지 않습니다.",
    emoji: "에는 이모티콘을 추가할 수 없습니다.",
  },
};

$s.ev = {
  const: {
    EVENT_VIEW_UID: "_FRONTEND_EVENT_VIEW_UID",
    EVENT_VIEW_QUERY_STRING: "_FRONTEND_EVENT_VIEW_QUERY_STRING",
  },
  init: function () {
    var uid = $s.ev.getUid();

    if (typeof uid == "undefined" || uid == "") {
      var paramUid = $s.core.getParameter("uid");

      if (typeof paramUid != "undefined" && paramUid != "") {
        set($s.ev.const.EVENT_VIEW_UID, paramUid);

        var queryString = $s.ev.getQueryString();

        if (typeof queryString == "undefined" || queryString == "") {
          var qs = location.search.replace("?", "");
          set($s.ev.const.EVENT_VIEW_QUERY_STRING, qs);
        }
      }
    }

    function set(cname, cvalue) {
      var time = 30 * 60 * 1000,
        d = new Date();

      d.setTime(d.getTime() + time);

      var expires = "expires=" + d.toUTCString();

      document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }
  },
  getUid: function () {
    return $s.getCookie($s.ev.const.EVENT_VIEW_UID);
  },
  getQueryString: function () {
    return $s.getCookie($s.ev.const.EVENT_VIEW_QUERY_STRING);
  },
  log: {
    getParamMap: function () {
      return make($s.ev.getQueryString());

      function make(qs) {
        var map = [];

        if (typeof qs != "undefined" && qs != null) {
          var params = qs.split("&");

          for (var i = 0; i < params.length; i++) {
            var param = params[i],
              array = param.split("=");

            if (typeof array != "undefined" && array != null) {
              if (array.length == 2) {
                map[array[0]] = array[1];
              } else if (array.length == 1) {
                map[array[0]] = "";
              }
            }
          }
        }

        return map;
      }
    },
    getParamValue: function (map, key) {
      var value = map[key];
      return typeof value == "undefined" || value == null ? "" : value;
    },
    getLog: function (id, items) {
      var paramMap = $s.ev.log.getParamMap();

      var log = {
        eventCode: $s.ev.log.getParamValue(paramMap, "ec"),
        uid: $s.ev.getUid(),
        sourceUserId: $s.ev.log.getParamValue(paramMap, "source_user_id"),
        utmSource: $s.ev.log.getParamValue(paramMap, "utm_source"),
        utmMedium: $s.ev.log.getParamValue(paramMap, "utm_medium"),
        utmCampaign: $s.ev.log.getParamValue(paramMap, "utm_campaign"),
        utmItem: $s.ev.log.getParamValue(paramMap, "utm_item"),
        utmContent: $s.ev.log.getParamValue(paramMap, "utm_content"),
      };

      if (typeof id != "undefined" && id != null) {
        log["id"] = id;
      }

      if (
        typeof items != "undefined" &&
        items != null &&
        Array.isArray(items)
      ) {
        log["items"] = items;
      }

      return log;
    },
    item: function (itemUserCode) {
      var params = $s.ev.log.getLog(itemUserCode);

      $s.api.eventlogItem(
        params,
        function () {},
        function (error) {
          $s.error(error);
        }
      );
    },
    order: function (orderCode, itemUserCodes) {
      var params = $s.ev.log.getLog(orderCode, itemUserCodes);

      $s.api.eventlogOrder(
        params,
        function () {},
        function (error) {
          $s.error(error);
        }
      );
    },
    featured: function (itemUserCodes) {
      var params = $s.ev.log.getLog("", itemUserCodes);

      $s.api.eventlogFeatued(
        params,
        function () {},
        function (error) {
          $s.error(error);
        }
      );
    },
    joinUser: function (userId) {
      $s.log(userId);

      var params = $s.ev.log.getLog(userId);

      $s.api.eventlogJoinUser(
        params,
        function () {},
        function (error) {
          $s.error(error);
        }
      );
    },
  },
};

$s.ga = {
  const: {
    CHECKOUT_INIT_STEP: 1,
    CHECKOUT_INPUT_USER_STEP: 2,
    CHECKOUT_INPUT_PAYMENT_STEP: 3,
    CHECKOUT_CONFIRM_ORDER_STEP: 4,
    CHECKOUT_PURCHASE_STEP: 5,
  },
  init: function () {
    /* 성능 이슈로 주석
        var head = document.head;

        $s.api.googleAnalyticsCommonTrackingScript(function (response) {

            try {
                var commonTrackingScript = response.script;

                if (typeof commonTrackingScript != 'undefined' && commonTrackingScript != null) {

                    var element = document.createElement('script');
                    element.src = commonTrackingScript.src;
                    head.appendChild(element);

                    var element = document.createElement('script');
                    element.insertAdjacentText('beforeend',commonTrackingScript.run);
                    head.appendChild(element);

                }

            } catch (e) {
                $s.error(e);
            }

        }, function (error) {
            $s.error(error);
        });
        */
  },
  purchase: function (orderCode, orderSequence) {
    /* 성능 이슈로 주석
        try {

            var params = $s.ga.initParams();
            params['orderCode'] = orderCode;
            params['orderSequence'] = orderSequence;

            $s.api.googleAnalyticsPurchase(params, function() {
            }, function(error) {
                $s.error(error);
            });
        } catch (e) {
            $s.error(e);
        }
        */
  },
  addToCart: function (cartArrayRequiredItems) {
    /* 성능 이슈로 주석
        try {
            var params = $s.ga.initParams();
            params['cartArrayRequiredItems'] = cartArrayRequiredItems;

            $s.api.googleAnalyticsAddToCart(params, function() {
            }, function(error) {
                $s.error(error);
            });
        } catch (e) {
            $s.error(e);
        }

         */
  },
  removeFromCart: function (items) {
    /* 성능 이슈로 주석
        try {
            if (typeof items == 'undefined' || items == null) {
                return false;
            }

            var params = $s.ga.initParams(items);

            $s.api.googleAnalyticsRemoveFromCart(params, function () {
            }, function (error) {
                $s.error(error);
            });
        } catch (e) {
            $s.error(e);
        }
        */
  },
  changeFromCartQuantity: function (items, addQuantityFlag) {
    /* 성능 이슈로 주석
        try {
            if (typeof items == 'undefined' || items == null) {
                return false;
            }

            if (typeof addQuantityFlag == 'undefined' || addQuantityFlag == null) {
                return false;
            }

            var params = $s.ga.initParams(items);
            params['addQuantityFlag'] = addQuantityFlag;

            $s.api.googleAnalyticsChangeFromCartQuantity(params, function () {
            }, function (error) {
                $s.error(error);
            });
        } catch (e) {
            $s.error(e);
        }

         */
  },
  detail: function (itemUserCode) {
    /* 성능 이슈로 주석
        try {
            if (typeof itemUserCode == 'undefined' || itemUserCode == null) {
                return false;
            }

            var params = $s.ga.initParams();
            params['itemUserCode'] = itemUserCode;

            $s.api.googleAnalyticsDetail(params, function () {
            }, function (error) {
                $s.error(error);
            });
        } catch (e) {
            $s.error(e);
        }

         */
  },
  checkout: function (step) {
    /* 성능 이슈로 주석
        try {

            if (step == $s.ga.const.CHECKOUT_INIT_STEP) {
                var params = $s.ga.initParams();

                $s.api.googleAnalyticsCheckout(params, function () {
                }, function (error) {
                    $s.error(error);
                });
            } else {

                if (typeof gtag == 'function') {
                    gtag('event', 'checkout_progress', {
                        "checkout_step": step,
                    });
                }

            }


        } catch (e) {
            $s.error(e);
        }

         */
  },
  initParams: function (items) {
    /* 성능 이슈로 주석
        var params = {
            cid : $s.ga.getCid(),
            page : $s.requestContext.requestUri,
        }

        if (typeof items != 'undefined' && items != null) {
            params['products'] = items;
        }

        return params
        */
    return null;
  },
  getCid: function () {
    /*
        GA1.1.1019322984.1593591448
        [0] 쿠키포멧
        [1] 도메인 구성요소
        [2] 임의의 고유 ID
        [3] 타임스탬프
        */
    /* 성능 이슈로 주석
        try {
            var _ga = $s.getCookie('_ga');

            if (typeof _ga != 'undefined' && _ga != '') {
                var array = _ga.split('.'),
                    length = 4;

                if (typeof array != 'undefined' && array != null && array.length == length) {
                    return array[2]+'.'+array[3];
                }
            }

        } catch (e) {
            $s.error(e);
        }
        */
    return "";
  },
  select: function (items) {
    /* 성능 이슈로 주석
        try {

            if (typeof gtag == 'function' && typeof items != 'undefined' && items != null) {
                gtag("event",  "select_content", {
                    "content_type": "product", "items" : items
                });
            }
        } catch (e) {
            $s.error(e);
        }
        */
  },
  impression: function (items) {
    /* 성능 이슈로 주석
        try {

            if (typeof gtag == 'function' && typeof items != 'undefined' && items != null) {
                gtag("event",  "view_item_list", {"items" : items});
            }
        } catch (e) {
            $s.error(e);
        }
        */
  },
  getImpressionsItem: function (
    id,
    name,
    listName,
    brand,
    category,
    listPosition,
    price
  ) {
    var item = null;
    /* 성능 이슈로 주석
        if (isValid(id) ||isValid(name) ) {

            item = {};

            if (isValid(id)) {
                item['id'] = id;
            }

            if (isValid(name)) {
                item['name'] = name;
            }

            if (isValid(listName)) {
                item['list_name'] = listName;
            }

            if (isValid(brand)) {
                item['brand'] = brand;
            }

            if (isValid(category)) {
                item['category'] = category;
            }

            if (listPosition == 0 || listPosition > 0) {
                item['list_position'] = listPosition;
            }

            price = price+'';

            if (isValid(price)) {
                item['price'] = price;
            }
        }
        */
    return item;

    function isValid(value) {
      return typeof value != "undefined" && value != null && value != "";
    }
  },
};

$s.donation = {
  const: {
    LOCGOV_CODE: "locgov_code",
    LOCGOV_NM: "locgov_nm",
    UPPER_LOCGOV_CODE: "upper_locgov_code",
    UPPER_LOCGOV_NM: "upper_locgov_nm",
    JUMIN_NO: "jumin_no",
    POINT_RATE: "point_rate",
    ENAPBU_NO: "enapbu_no",
  },
  focusHeader: function (){
	$("#go_main").attr('tabindex','0').focus();
  },
  getData: function (key) {
    return localStorage.getItem(key);
  },
  setData: function (key, value) {
    localStorage.setItem(key, value);
  },
  goDonationPage: function (param) {
	if (!param) {
		param = '';
	} else {
		if (param.indexOf('?') == 0) {
			param = param.replace('?', '');
		}
		param = encodeURIComponent(param);
		param = '?' + param;
	}

	Saleson.init({ loginPage: true, url: '/donation/donation.html' + param, method: function () {

		param = decodeURIComponent(param);
		$s.redirect('/donation/donation.html' + param);
	} });
  },
  goDonationPageTest: function (param) {
	if (!param) {
		param = '';
	} else {
		if (param.indexOf('?') == 0) {
			param = param.replace('?', '');
		}
		param = encodeURIComponent(param);
		param = '?' + param;
	}
	Saleson.init({ loginPage: true, url: '/donation/donation-main-test.html' + param, method: function () {
		param = decodeURIComponent(param);
		$s.redirect('/donation/donation-main-test.html' + param);
	} });
  },
};

var $goods = {
  const: {
    GROUP: "GOODS_GROUP",
    CATEGORY: "GOODS_CATEGORY",
    LOCGOV: "GOODS_LOCGOV",
    PRICE: "GOODS_PRICE",
    KEYWORD: "GOODS_KEYWORD",
    ITEMS_PER_GOODS: 12,
    LOCGOV_TXT: "지자체",
    CATEGORY_TXT: "카테고리",
    KEYWORD_TXT: "검색어",
    PRICE_TXT: "금액대",
    UNDER_10000_TXT: "1만포인트 이하",
    UNDER_30000_TXT: "3만포인트 이하",
    UNDER_50000_TXT: "5만포인트 이하",
    OVER_50000_TXT: "5만포인트 이상",
  },
  // input number 에 +, -, e 값 입력을 방지위해서 추가함.../components/layouts/loading.vue 화면에서 호출
  setInputTypeNumber: function () {
    let inputTagList = document.getElementsByTagName("input");
    let length = inputTagList.length;
    for (let i = 0; i < length; i++) {
      let inputTag = inputTagList[i];
      if (inputTag.getAttribute("type") === "number") {
        inputTag.onkeydown = function (e) {
          /*if(!((e.keyCode > 95 && e.keyCode < 106)
                      || (e.keyCode > 47 && e.keyCode < 58)
                      || e.keyCode == 8)) {
                        return false;
                    }*/
          let key = e.key.toLowerCase();
          //console.log(key);
          if (
            !(
              $s.validator.patterns.number_only.test(key) ||
              key == "tab" ||
              key == "backspace" ||
              key == "delete" ||
              key == "f12" ||
              key == "arrowleft" ||
              key == "arrowright"
            )
          ) {
            return false;
          }
        };
      }
    }
  },
  goSearchGoods({ type, keyword, locgov, group, category, price, sort }, exportMode) {
	if (exportMode) {
	    $s.redirect(
	      `/goods/searchGoods.html?type=${type}${
	        keyword ? "&keyword=" + keyword : ""
	      }${locgov ? "&locgov=" + locgov : ""}${group ? "&group=" + group : ""}${
	        price ? "&price=" + price : ""
	      }${category ? "&category=" + category : ""}${sort ? "&sort=" + sort : ""}` + "&exportMode=" + exportMode
	    );
	} else {
	    $s.redirect(
	      `/goods/searchGoods.html?type=${type}${
	        keyword ? "&keyword=" + keyword : ""
	      }${locgov ? "&locgov=" + locgov : ""}${group ? "&group=" + group : ""}${
	        price ? "&price=" + price : ""
	      }${category ? "&category=" + category : ""}${sort ? "&sort=" + sort : ""}`
	    );
	}
  },
  getCommonRegionCode() {
    return new Promise((resolve, reject) => {
      const regionCode = $s.core.getSession($s.const.REGION_CODE);
      if (regionCode) {
        resolve(JSON.parse(regionCode));
      } else {
        $s.api.getSido(
          "",
          function (response) {
            let data = [{ sidoCode: "", sidoName: "시·도 선택" }];
            response.resultList.sidoList.forEach((v, i, arr) => {
              data.push({ sidoCode: v.sidoCode, sidoName: v.sidoName });
            });
            $s.core.setSession($s.const.REGION_CODE, JSON.stringify(data));
            resolve(data);
          },
          function (error) {
            //$s.api.handleApiExeption(error);
            //reject(error);
            $goods.getTempRegionCode(0, resolve, reject, error);
          }
        );
      }
    });
  },
  /**
   * 동시 api 호출로 인한 실패시 임시 조치
   */
  getTempRegionCode : function (cnt, callback, errCallback, error) {
	setTimeout(function () {
		const regionCode = $s.core.getSession($s.const.REGION_CODE);
        if (regionCode) {
          callback(JSON.parse(regionCode));
        } else {
			if (cnt < 5) {
				$goods.getTempRegionCode(++cnt, callback);
			} else {
				errCallback(error);
			}
		}
	}, 500);
    },


    goSearchGoodsMap({ type, keyword, locgov, group, category, price }) {
        $s.redirect(
            `/goods/searchGoods-main-map.html?type=${type}${keyword ? "&keyword=" + keyword : ""
            }${locgov ? "&locgov=" + locgov : ""}${group ? "&group=" + group : ""}${price ? "&price=" + price : ""
            }${category ? "&category=" + category : ""}`
        );
    },
  goSearchGoodsNew({ type, keyword, locgov, group, category, price }) {
    $s.redirect(
      `/goods/searchGoods-main-new.html?type=${type}${
        keyword ? "&keyword=" + keyword : ""
      }${locgov ? "&locgov=" + locgov : ""}${group ? "&group=" + group : ""}${
        price ? "&price=" + price : ""
      }${category ? "&category=" + category : ""}`
    );
  },

};

// 탭간 세션 공유
window.onstorage = function (event) {
  if (!event.newValue) return;

  if (event.key === "storage-temp" && sessionStorage.getItem($s.const.TOKEN)) {
    var tokenInfo = {
      token: sessionStorage.getItem($s.const.TOKEN),
      token_type: sessionStorage.getItem($s.const.TOKEN_TYPE),
      token_status: sessionStorage.getItem($s.const.TOKEN_STATUS),
    };

    localStorage.setItem("jwt-info", JSON.stringify(tokenInfo));
    localStorage.removeItem("jwt-info");
  } else if (
    event.key === "jwt-info" &&
    !sessionStorage.getItem($s.const.TOKEN)
  ) {
    var data = event.newValue;
    if (data) {
      var d = JSON.parse(data);
      var keys = Object.keys(d);
      for (var i in keys) {
        var key = keys[i];
        sessionStorage.setItem(key, d[key]);
      }
    }
  } else if (event.key === "storage-remove") {
    $s.cleanToken();
    sessionStorage.removeItem($s.const.SALESON_ID);
    $s.redirect($s.pages.INDEX);
  }
};

if (!sessionStorage.getItem($s.const.TOKEN)) {
  localStorage.setItem("storage-temp", "1");
  localStorage.removeItem("storage-temp");
}

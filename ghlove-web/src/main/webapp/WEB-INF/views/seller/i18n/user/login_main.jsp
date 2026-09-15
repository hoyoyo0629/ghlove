<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%-- LOCAL DEV ONLY: the login auth-code email never arrives locally, and the server already
     fixes the code to "250324" for localhost logins. Exposed to JS below so the login can
     auto-continue past the "check your email" step. Must be EL, not a scriptlet: the script
     lives inside <page:javascript>, whose body-content is scriptless. --%>
<c:set var="isLocalDev" value="${pageContext.request.serverName eq 'localhost'}" />
<style type="text/css">
	.disabled-div {
		pointer-events: none;		/* 마우스 이벤트 비활성화 */
		opacity: 0.6;				/* 시각적으로 비활성화된 것처럼 보이게 함 */
	}
</style>
<!-- 내용 -->
	<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyyMMdd" var="today"/>

	<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-ui.min.js"></script>
	<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery.blockUI.js"></script>
	<!-- ML4WEB JS -->
	<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/ML_Config.js"></script>
	<!-- 간편인증 JS -->
	<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ML4Web_Config.js"></script>
	<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/magicline-js-api.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>

	<script type="text/javascript" src="/content/modules/op.fincert.js"></script>
	<script type="text/javascript" src="/content/modules/jsencrypt.min.js"></script>


	<div class="admin_wrap">
		<div id="authModal" class="modal_wrap" style="z-index:10;display:none;">
			<div class="modal_overlayer">
				<button class="btn closeBtn" onclick="javascript:closeSelectBox();"></button>
				<h2 class="modal_tit">인증서를 선택하세요</h2>
				<div class="modal_con flex_box juc-sbt gap-08">
					<div class="btn_wrap flex_box col juc-center item-center">
						<div class="auth_img"></div>
						<button type="button" class="btn btn-dark-blue" onclick="javascript:doFinanceSignData();"><span>금융인증서</span></button>
					</div>
					<div class="btn_wrap flex_box col juc-center item-center">
		                <div class="auth_img"></div>
		                <button type="button" class="btn btn-dark-blue" onclick="javascript:doSignData();"><span>공동인증서</span><span class="text-lg">(구 공인인증서)</span></button>
					</div>
				</div>
			</div>
		</div>

		<div id="selectLoginIdModal" class="modal_wrap" style="z-index:10;display:none;">
			<div class="modal_overlayer">
				<button class="btn closeBtn" onclick="javascript:closeSelectBox();"></button>
				<h2 class="modal_tit">로그인 아이디를 선택하세요</h2>
				<div class="modal_con flex_box gap-08" id="selectLoginId">

				</div>
			</div>
		</div>

        <!-- 상단  -->
        <%@ include file="/WEB-INF/tags/seller/login-header.tag" %>

        <!-- 내용 -->
        <div id="container" class="login">

            <div class="login_cont seller">
                <div class="login_tit">답례품 제공자 로그인</div>
                <div class="login_form_box mt30">
					<div class="login_form auth">
                        <div class="top_area">
                            <h2 class="top_tit">인증서 로그인</h2>
                        </div>
                        <div class="cont_area">
                            <div class="button_wrap">
                                <button type="button" class="btn btn-dark-blue" onclick="doFinanceSignData();"><span>금융인증서</span></button>
                                <button type="button" class="btn btn-dark-blue" onclick="doSignData();"><span>공동인증서</span><span class="text-lg">(구 공인인증서)</span></button>
                            </div>
                            <div class="info_area">
                                <p class="text-lg">인증서로 로그인하려면 해당 인증서가 사전 등록되어 있어야 합니다.</p>
                                <button type="button" class="btn btn-default btn-mid"  onclick="pkiForm();"><span>인증서 등록/삭제</span></button>
                            </div>
                        </div>
					</div>

					<div class="login_form pwd">
                        <div class="top_area">
                            <h2 class="top_tit">아이디 로그인</h2>
                        </div>
                        <div class="cont_area">
                            <div class="flex_box mb15" id="idpwDiv">
                                <div class="flex_box col">
                                    <input type="text" maxlength="40" id="op_username"
                                        class="required ime-mode-disabled" title="아이디" placeholder="아이디" />
                                    <input type="password" maxlength="20" class="required"
                                        title="<c:out value='${op:message(\'M00150\')}'/>" placeholder="비밀번호" id="op_password" autocomplete="off" onkeyup="javascript:checkCapsLock(event, 'passwordWarn');"/>
									<div id="passwordWarn" style="display:none;" class="red">CAPS LOCK 키가 동작 중입니다.<br>대문자로 입력됩니다.</div>
                                </div>
                                <div class="button_wrap">
                                    <button type="button" id="loginBtn" class="btn btn-dark-gray btn-lg" onclick="idLogin();"><span>인증번호</br>발송</span></button>
                                    <!-- 로그인 ログイン -->
                                </div>
                            </div>
                            <div class="checkbox">
                                <input type="checkbox" id="id_save">
                                <label for="id_save">아이디 저장</label>
                            </div>
                            <div class="flex_box mb15" id="emailDiv" style='display:none'>
								<div class="flex_box col">
									<input type="text" id="op_email" maxlength="40" class="ime-mode-disabled" title="이메일" placeholder="이메일" autocomplete="off"/>
								</div>
								<div class="button_wrap">
									<button type="button" onclick="saveEmail();" class="btn btn-dark-gray btn-lg" style="height:100%"><span><c:out value="저장"/></span></button>
								</div>
							</div>

							<div class="flex_box mb15 disabled-div" id="authNumDiv">
								<div class="flex_box col" style="position:relative">
									<input type="text" id="op_auth_num" maxlength="40" class="ime-mode-disabled" title="인증번호" placeholder="인증번호" autocomplete="off"/>
									<span class="time" style="position:absolute;right:10px;top:50%;transform:translateY(-50%);">05:00</span>
								</div>
								<div class="button_wrap">
									<button type="button" onclick="authCheck();" class="btn btn-dark-gray btn-lg" style="height:100%"><span><c:out value="${op:message('M00796')}"/></span></button> <!-- 로그인 ログイン -->
								</div>
							</div>
                        </div>
                    </div>
                </div>
                <div class="login_sub_tit">
                	<ul class="mb10">
						<li><span class="txt"><span style="font-weight:bold">※ 패스워드 변경 및 개인정보 변경은 지자체에 문의 주시기 바랍니다.</span></span></li>
					</ul>
                	<ul class="mb10">
						<li><span class="txt">○ <span style="font-weight:bold">인증서 로그인</span></span></li>
						<li><span class="txt pl20">- 원활한 이용을 위해 인증서를 <span style="font-weight:bold">미리 발급 및 등록</span>해 주시기 바랍니다.</span></li>
						<li><span class="txt pl20">- 인증서가 <span style="font-weight:bold">만료</span>되었을 경우 <span style="font-weight:bold">삭제 후 등록</span>하여 사용해 주시기 바랍니다.</span></li>
						<li><span class="txt pl20">- 광역과 기초 지자체에 모두 선정된 답례품제공자의 경우 인증서 로그인 후 <span style="font-weight:bold">대상기관 아이디</span> 중 1개를 선택할 수 있습니다.</span></li>
					</ul>
					<ul class="mb30">
						<li><span class="txt">○ <span style="font-weight:bold">아이디 로그인</span></span></li>
						<li><span class="txt pl20">- 아이디와 패스워드를 입력하신 후 <span style="font-weight:bold">[인증번호 발송]</span>을 누르면 <span style="font-weight:bold">회원정보에 등록하신 메일주소</span>로 인증번호가 발송됩니다.</span></li>
						<!-- <li><span class="txt pl20">- 메일주소를 잊으셨거나 로그인에 문제가 있을 경우 <span style="color:#d40000;font-weight:bold">1522-2431</span>(고향사랑e음 고객센터)로 문의하시기 바랍니다.</span></li> -->
						<li><span class="txt pl20">- 광역과 기초 지자체에 모두 선정된 답례품제공자의 경우 <span style="font-weight:bold">각 계정에 설정하신 메일주소</span>로 인증번호가 발송됩니다.</span></li>
					</ul>
					<ul>
						<li><span style="font-weight:bold">※ 국민비서 알림서비스</span> : 국민비서 회원에게만 제공되며, 주문 등 각종 알림이 발송됩니다.</br></li>
						<li><span class="pl20" style="color: blue;">→</span><a href="https://www.ips.go.kr/pot/forwardMain.do" target="_blank" rel="noreferrer" style="text-decoration: underline; color: blue;"><strong> 국민비서 가입하기</strong></a>(신청 후 다음날부터 적용)</li>
					</ul>
				</div>
            </div>
        </div>

        <!-- 하단  -->
        <%@ include file="/WEB-INF/tags/seller/login-footer.tag" %>

		<form id="idLoginForm"  action="/op_security_login" method="POST" autocomplete="off">
			<legend class="hidden">로그인</legend>
			<input type="hidden"  name="_csrf" value="<c:out value='${_csrf.token}'/>"/>
			<input type="hidden" name="_csrf_header" value="<c:out value='${_csrf.headerName}'/>"/>
			<input type="hidden" name="op_login_type" value="ROLE_SELLER" />
			<input type="hidden" name="target" value="<c:out escapeXml="true" value="${param.target}" />" />
			<input type="hidden" name="failureUrl" value="<c:out value='${requestContext.requestUri}'/>?target=<c:out escapeXml="true" value="${param.target}" />" />
			<input type="hidden" name="op_username" />
			<input type="hidden" name="op_password" />
		</form>

		<!-- 결과 수신 메시지  -->
		<form id="reqForm" name="reqForm" method="post" action="/seller/magicline/signedFormRGhlove" target="magiclineIframe" autocomplete="off">
			<input type="hidden"	id="signOrigin"		name="signOrigin" /> <!-- 180701 서명 원문 폼 추가 -->
			<input type="hidden"	id="sign"			name="sign"/>
			<input type="hidden"	id="csCheckType"	name="csCheckType"	value="1"/>
			<input type="hidden"	id="signData"		name="signData"		value="LOGIN" />
		</form>
		<!--// 결과 수신 메시지  -->


		<!-- 결과 수신 메시지  -->
		<form id="chgPwdForm" name="chgPwdForm" method="post" autocomplete="off">
			<input type="hidden"	id="loginId"		name="loginId" />
			<input type="hidden"	id="pwd"			name="pwd"/>
			<input type="hidden"	id="newPwd"			name="newPwd"/>
			<input type="hidden"	id="confirmPwd"		name="confirmPwd"/>
		</form>
		<!--// 결과 수신 메시지  -->

		<!-- magic line 영역 -->
		<iframe name="magiclineIframe" style="display:none;" width="1200px" height="500px" title="인증처리"></iframe>
		<!--// magic line 영역 -->
    </div>





<page:javascript>
<script type="text/javascript" src="/content/popup/popup.js"></script>
<script type="text/javascript">
	var timer = null;
	var isRunning = false;

	let publicKeyStr = "${op:removeIframe(publicKeyStr)}";
	let popupType = "toolbar=no,width=700,height=405,top=150px,left=250px,directories=no,menubar=no,scrollbars=yes,location=no";
	let popup;

	let crypt = new JSEncrypt();

	const receiveMsg = async (e) => {
		if (e.data.hasOwnProperty('fnName')) {
			if (e.data.fnName == 'goMain') {
				location.href="/seller/index";
			}
		}
	}

	$(function() {

		/* const copyWrap = $('.admin_wrap').clone();
		$('.admin_wrap').remove();
		$('#header').remove();
		document.body.appendChild(copyWrap[1]);  */

		window.addEventListener("unload", (event) => {		// 화면 닫을 때 팝업 같이 닫기(화면 이동시 팝업만 남아있는 상황 방지)
			if (popup) {
				popup.close();
			}
		});
		pop.openNotice('S'); //팝업임시
		window.addEventListener("message", receiveMsg, false);			// 팝업 통신용

		crypt.setPrivateKey(publicKeyStr);

		// 포커스.
		$('#op_username').val() == '' ? $('#op_username').focus() : $('#op_password').focus();

		// enter Key Event
		$('#op_username, #op_password').keypress(function(e) {
			if (e.keyCode === 13) {
				e.preventDefault();
				idLogin();
			}
		});

		// 폼 검증..
		/* $('#loginForm').validator(function() {

		}); */

		var errorCode = '${fn:escapeXml(param.error)}';

		<%-- let failId = '<c:out escapeXml="true" value="${param.failId}" />';

		if (failId) {
			removeId();
		}
		--%>

		if (errorCode != "") {

			var errorMsg = '답례품제공자 정보가 존재하지 않아 로그인할 수 없습니다.\n키보드 대/소문자, CAPS LOCK 버튼을 확인해주세요.\n에러코드 : ' + errorCode;
			var errorLink = '';
			/*
			if (errorCode == '4') {
				errorMsg = '계정이 잠겨 있습니다.';
				errorLink = '/seller/login-lock';
			} else if (errorCode == '5') {
				errorMsg = '비밀번호상태 가 임시 또는 만료되었습니다.';
				errorLink = '/seller/login-change-password';
			}
			*/
	        /*if (errorCode == '4') {
	            errorMsg = '계정이 잠겨 있습니다.';
	            errorLink = '/seller/login-lock';
	        } else if (errorCode == '5') {
	            errorMsg = '비밀번호상태 가 임시 또는 만료되었습니다.';
	            errorLink = '/seller/login-change-password';
	        } else*/ if (errorCode == '98') {
	            errorMsg = '다른 기기에서 로그인하여 자동으로 로그아웃 되었습니다.';
	        } else if (errorCode == '99') {
	            errorMsg = '세션이 종료 되었습니다.';
	        } else if (errorCode == '70') {
	            errorMsg = '다른 권한의 계정이 로그인되어있어 로그아웃 처리되었습니다. \n재시도 해주세요.';
	            errorLink = '/seller';
	        }


			setTimeout(function() {
				alert(errorMsg);
				if (errorLink != '') {
					location.href = errorLink
				}
			}, 100);
		}

		// 아이디 저장 확인
		if(localStorage.getItem('saved_seller_login_id')) {
			$('#id_save').prop("checked", true);
			$('#op_username').val(localStorage.getItem('saved_seller_login_id'));
			$('#op_password').focus();
		}


		// 금융인증서 초기화
		<%-- fincert.init("<c:out value='${op:property('outconn.fincert.orgCode')'/>", "<c:out value='${op:property('outconn.fincert.apiKey')}'/>"); --%>

		/* alert("데이터 이관으로 인해 비밀번호가 '1111'로 초기화 되었습니다."); */
	});

	try {
		if (opener) {
			self.close();
			opener.location.href=url("${fn:escapeXml(param.target)}");
		}
	} catch(e) {alert(e.message)};

	/**
	 *	함 수 명 : validator
	 *	기	능  : 로그인 사용자 아이디/패스워드 유효성 검사
	 */
	function validator() {
		if($.trim($('#op_username').val()) == '') {
			alert('아이디를 입력해 주세요.');
			$('#op_username').focus();
			return false;
		}
		if($.trim($('#op_password').val()) == '') {
			alert('비밀번호를 입력해 주세요.');
			$('#op_password').focus();
			return false;
		}
		return true;
	}

	/**
	 *	함 수 명 : idLogin
	 *	기	능  : 아이디 로그인 실행
	 */
	// LOCAL DEV ONLY: see isLocalDev above.
	var IS_LOCAL_DEV = ${isLocalDev};

	function idLogin() {
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}

		// 1. ID/PW 유효성 검사
		if(!validator()) return false;

		loginSubmit();
	}

	/**
	 *	함 수 명 : loginSubmit
	 *	기	능  : 로그인
	 */
	function loginSubmit() {
		let id = $('#op_username').val().trim();
		let pwd = $('#op_password').val().trim();

		$.post("/seller/login-seller"
			, {"loginId": id, "pwd": pwd}
			, function(response){
				if (response) {
					if (response.isSuccess) {		// 아이디, 비밀번호 일치(세션 로그인 처리 전)
						if ($('#id_save').is(":checked")) {
							saveId();
						} else {
							removeId();
						}
						$("#loginBtn").attr("disabled", true);
						sendRedirectAfterIdLogin(response.data);
					} else {
						alert(response.errorMessage);
					}
				} else {
					alert("문제가 발생했습니다.");
				}
			}
			, "json"
		);
	}

	/**
	 *	함 수 명 : saveId
	 *	기	능  : 아이디 저장 (체크)
	 */
	function saveId() {
		localStorage.setItem('saved_seller_login_id', $('#op_username').val());
	}

	/**
	 *	함 수 명 : removeId
	 *	기	능  : 아이디 저장 (해제)
	 */
	function removeId() {
		localStorage.removeItem('saved_seller_login_id');
	}

	/**
	 *	함 수 명 : pkiForm
	 *	기	능  : 인증서 등록페이지 이동
	 */
	function pkiForm() {
		//alert('시스템 점검중입니다. 아이디/비밀번호로 로그인해주세요.');
		//return false;
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}
		location.href = "/seller/user/pki/form";
	}

	/**
	 *	함 수 명 : doSignData
	 *	기	능  : 인증서 활성화
	 */
	function doSignData() {
		//alert('시스템 점검중입니다. 아이디/비밀번호로 로그인해주세요.');
		//return false;
		// $("#authModal").removeClass('show');
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}
		$("#authModal").hide();
		var signData = $("#signData").val();
		document.reqForm.signOrigin.value = document.reqForm.signData.value;
		magicline.uiapi.MakeSignData(signData, null, mlCallBack);
	}

	/**
	 *	함 수 명 : doSignData
	 *	기	능  : MagicLine 결과값 수신 CallBack
	 *	파라미터  : code - 전자서명 결과값, message - 전자서명 메시지
	 */
	function mlCallBack(code, message) {
		if(code==0){
			document.reqForm.sign.value = encodeURIComponent( message.encMsg );
			document.reqForm.submit();
		}else{
			alert("결과값 수신에 실패하였습니다.");
			return;
		}
	}

	/**
	 *	함 수 명 : signedResponse
	 *	기	능  : 인증서 검증 결과 callback
	 *	파라미터  : result - DN, message, result 정보 리턴
	 */
	function signedResponse(response) {
		if(response && response.result && response.result == "SUCCESS" && response.DN) {
			userLoginAuth(response);
		} else {
			alert("오류가 발생했습니다.");
		}
	}

	function doFinanceSignData() {
		//alert('시스템 점검중입니다. 아이디/비밀번호로 로그인해주세요.');
		//return false;

		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}
		//alert('준비 중입니다.');
		//return;
		//$("#authModal").removeClass('show');
		$("#authModal").hide();
		fincert.getUserDn(function (response) {
			fincertResult(response);
		}, function (err) {
			if (err && err.description) {
				alert(err.description + "\n해당 현상이 지속될 경우 관리자에게 문의 부탁드립니다.");
				return false;
			} else {
				alert("금융인증서 호출에 실패하였습니다. \n해당 현상이 지속될 경우 관리자에게 문의 부탁드립니다.");
				return false;
			}
			return false;
		})
	}


	function fincertResult(response) {
		if(response && response.dn) {
			/* var param = {
				"DN" : response.dn
			}
			userLoginAuth(param); */
			response.DN = response.dn;
			userLoginAuth(response, true);
		} else {
			alert("오류가 발생했습니다.");
		}
	}

	function userLoginAuth (responseData, isFincert) {
		var params = {};

		if (isFincert) {
			params.mberFinDn = responseData.DN;

			if (!params.mberFinDn) {			// 암호화 오류날 경우
				alert("새로고침 후 재시도 해주세요.");
				return;
			}
		} else {
			params.mberDn = responseData.DN;

			if (!params.mberDn) {			// 암호화 오류날 경우
				alert("새로고침 후 재시도 해주세요.");
				return;
			}
		}


		$.post('/seller/user/pki/login-user-valid', params, function(response) {
			if (response) {
				if (response.isSuccess) {
					if (response.data && response.data.code) {
						let code = response.data.code;
						if(code == "SUCC") {
							location.href="/seller/index";
						} else if (code == "INIT_PWD") {		// 비밀번호 초기화 된 경우
							alert(response.data.errMsg);
							popup = window.open('/seller/change-password', 'sellerChgPwd', popupType);
						} else if (code == "EXPIRED") {			// 비밀번호 만료된 경우
							alert(response.data.errMsg);
							popup = window.open('/seller/change-password', 'sellerChgPwd', popupType);

						} else if (code == "INIT_KEY") {		// 세션 암호화 키 초기화시
							publicKeyStr = response.data.publicKey;
							//crypt.setPrivateKey(publicKeyStr);
							crypt = new JSEncrypt();
							crypt.setPrivateKey(response.data.publicKey);
							userLoginAuth (responseData, isFincert);		// 로그인 재시도
						} else if (code == 'SELECT_ID') {		// 한 인증서로 로그인 아이디가 여러 개 등록된 경우
							let loginIds = response.data.loginIds;
							let length = loginIds.length;
							let div = $("#selectLoginId");
							div.empty();
							for(let i = 0 ; i < length ; i++) {
								let displayInfos = loginIds[i].trim().split('\|');
								let tag = '<div class="auth_img"></div>';
								tag += "<button type=\"button\" class=\"btn btn-dark-blue\" onclick=\"javascript:selectLoginId('" + displayInfos[0] + "', '" + responseData.DN.trim() + "', " + isFincert + ");\">";
								tag += '	<span class="seller_id">' + displayInfos[0] + '</span>';
								tag += '	<span class="loc_code">' + displayInfos[1] + '</span>';
								tag += '</button>';

								div.append(tag);
							}
							$("#selectLoginIdModal").show();
						} else {
							alert(response.data.errMsg);
						}
						return;
					}
				}
			}
			alert("오류가 발생했습니다.");
		});
	}

	function selectLoginId(loginId, mberDn, isFincert) {
		let params = {
			loginId : loginId,
		};

		if (isFincert) {
			params.mberFinDn = mberDn;
		} else {
			params.mberDn = mberDn;
		}

		if (!params.loginId) {			// 암호화 오류날 경우
			alert("새로고침 후 재시도 해주세요.");
			return;
		}

		$("#selectLoginIdModal").hide();

		$.post('/seller/user/pki/login-user-valid', params, function(response) {
			if (response) {
				if (response.isSuccess) {
					if (response.data && response.data.code) {
						let code = response.data.code;
						if(code == "SUCC") {
							location.href="/seller/index";
						} else if (code == "INIT_PWD") {		// 비밀번호 초기화 된 경우
							alert(response.data.errMsg);
							popup = window.open('/seller/change-password', 'sellerChgPwd', popupType);
						} else if (code == "EXPIRED") {			// 비밀번호 만료된 경우
							alert(response.data.errMsg);
							popup = window.open('/seller/change-password', 'sellerChgPwd', popupType);
						} else if (code == "INIT_KEY") {		// 세션 암호화 키 초기화시
							publicKeyStr = response.data.publicKey;
							//crypt.setPrivateKey(publicKeyStr);
							crypt = new JSEncrypt();
							crypt.setPrivateKey(response.data.publicKey);
							selectLoginId(loginId, mberDn, isFincert);		// 로그인 재시도
						} else {
							alert(response.data.errMsg);
						}
						return;
					}
				}
			}
			alert("오류가 발생했습니다.");
		});
	}

	// 아이디 로그인 후 페이지 이동
	function sendRedirectAfterIdLogin(data) {
		var errorCode = '${fn:escapeXml(param.error)}';

		if (data.code == 'INIT_PWD') {		// 비밀번호 초기화
			popup = window.open('/seller/change-password', 'sellerChgPwd', popupType);
		} else if (data.code == 'NONE_DN') {		// 인증서 없음
			pkiForm();
		} else if (data.code == 'INIT_LOGIN') {		// 최초로그인
			location.href="/seller/index";
		} else if (data.code == 'INIT_KEY') {		// 세션 삭제
			publicKeyStr = data.publicKey;
			//crypt.setPrivateKey(publicKeyStr);
			crypt = new JSEncrypt();
			crypt.setPrivateKey(data.publicKey);
			loginSubmit();		// 로그인 재시도
		} else if (data.code == 'LOGIN_STATE') {
			// 이미 로그인 된 상태
			// 여기는 이제 호출되지 않음.
			//location.href="/seller/index";
		} else if ((data.code == 'SUCC') || (data.code == 'EXPIRED_PWD')) {
			$("#authNumDiv").addClass("disabled-div");
			// 개발시 인증서 미적용
			userLoginEmail();

			// 이메일 인증번호 만료시간 타이머 시작
			let display = $('.time');
			let leftSec = 300;		// 5분(300초)
			// 남은시간 카운트. 이미 타이머가 작동중이면 중지
			if(isRunning){
				clearInterval(timer);
				display.html("");
				startTimer(leftSec, display);
			}else{
				startTimer(leftSec, display);
			}

		} else if (data.code == 'FAIL_CNT') {
			// 로그인 5회 이상 실패 이력 있을 경우
			alert(data.errMsg);
		} else {
			// 공인인증서 로그인 해야함
			$("#authModal").show();
		}
	}

	// 팝업 숨기기
	function closeSelectBox() {
		$("#authModal").hide();
		$("#selectLoginIdModal").hide();
	}


	function checkCapsLock(event, controlDivId) {
		if(event instanceof KeyboardEvent){
			if (event.getModifierState("CapsLock")) {
				$("#" + controlDivId).show();
			} else {
				$("#" + controlDivId).hide();
			}
		}
	}

	function startTimer(count, display){
		let minutes, seconds;
		timer = setInterval(function() {

			minutes = parseInt(count / 60, 10);
			seconds = parseInt(count % 60, 10);

			minutes = minutes < 10 ? "0" + minutes : minutes;
			seconds = seconds < 10 ? "0" + seconds : seconds;

			$(".time").css('color', 'rgb(51, 51, 51)');
			display.html(minutes + ":" + seconds);

			// 타이머 끝
			if(--count < 0){
				$('#loginBtn').attr('disabled', false);
				clearInterval(timer);
				alert("시간초과");
				$(".time").html("시간초과");
				$(".time").css('color', 'red');
				$(".time").attr('disabled', 'disabled');
				isRunning = false;
			}
		}, 1000);
		isRunning = true;
	}

	function userLoginEmail (response) {
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}

		// 1. ID/PW 유효성 검사
		if(!validator()) return false;

		// 2. 로그인 사용자 확인
		var param = {
			'loginId': $('#op_username').val().trim(),
			'pwd': $('#op_password').val().trim()
		};

		$.post('/seller/login-user-email', param, function(response) {

			if(response.isSuccess && response.data) {
				if(response.data == 'NOEMAIL'){
					alert('이메일을 등록하고 수신된 인증번호를 입력해야 로그인 가능합니다.');
					//$('#emailDiv').css('display', 'flex');
					$('#authNumDiv').css('display', 'none');
					$('#authNumDiv').addClass('disabled-div');
					$('#emailDiv').css('display', 'flex');
					$('#emailDiv').removeClass('disabled-div');
					// email 입력 란, focus in
					$('#op_email').focus();
				}else if(response.data == 'FAIL') {
					// LOCAL DEV ONLY: unlike the opmanager handler, the seller handler returns FAIL
					// when the EMS mail send fails - which it always does locally. The fixed code
					// "250324" is already stored before the send, so carry on and log in anyway.
					if (IS_LOCAL_DEV) {
						$('#op_auth_num').val('250324');
						authCheck();
						return;
					}

				}else if(response.data.startsWith('SUCC')){
					// LOCAL DEV ONLY: no mail gateway locally, so the auth-code email never
					// arrives. The server already fixes the code to "250324" for localhost
					// logins, so skip the "check your email" step entirely and log straight in.
					if (IS_LOCAL_DEV) {
						$('#op_auth_num').val('250324');
						authCheck();
						return;
					}

					//idLoginResult(response.data);		// AS-IS 로그인 성공 후 main 페이지 이동
					// 이메일로 발송된 인증번호를 입력하는 UI 보이기
					$('#authNumDiv').css('display', 'flex');
					$('#authNumDiv').removeClass('disabled-div');
					$('#emailDiv').css('display', 'none');
					$('#emailDiv').addClass('disabled-div');
					$('#idpwDiv').addClass('disabled-div');
					var email = response.data.substring(4).trim();
					// 이메일 ID 앞 3개 빼고 마스킹 처리
					email = emailMasking(email);

					alert(email + ' (으)로 발송된 인증번호를 확인해주세요.\n\n※ 사용하시는 이메일에 따라 인증번호 발송에\n    시간이 소요될 수 있습니다.');

					$("#op_auth_num").focus();
				}
			}
		});


	}

	function emailMasking(email){
		let originEmail = email;
		let emailStr = originEmail.match(/([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z0-9._-]+)/gi);
		let strLength;

		if(email != null && email != ''){
			let plainLength = 3;	// 마스킹 없이 보이는 문자 수
			strLength = emailStr.toString().split('@')[0].length - (plainLength+1);

			return originEmail.toString().replace(new RegExp('.(?=.{0,' + strLength + '}@)', 'gi'), '*');

		}else{
			return originEmail;
		}
	}

	// TO-BE: 이메일이 등록되어 있지 않은 사용자가 인증하면 이메일을 저장하는 기능을 추가해야 함
	function authCheck (response) {
		if($('.time').text() === '시간초과'){
			alert('인증시간이 초과되었습니다.');
			return;
		}

		let opAuthNum = $('#op_auth_num').val();
		if(opAuthNum.trim() == ''){
			alert('이메일로 발송된 인증번호를 입력해주세요.\n\n※ 사용하시는 이메일에 따라 인증번호 발송에\n시간이 소요될 수 있습니다.');
		} else {
			// 2. 로그인 사용자 확인
			// 이메일이 없는 사람은 인증번호가 인증될 때 이메일을 등록해준다.
			let emailBlank = $('#emailDiv').css('display');
			if(emailBlank == 'flex'){
				var param = {
					'loginId': $('#op_username').val(),
					'password': $('#op_password').val(),
					'authNum': $('#op_auth_num').val(),
					'opEmail': $('#op_email').val()
				};
			}else{
				var param = {
					'loginId': $('#op_username').val(),
					'password': $('#op_password').val(),
					'authNum': $('#op_auth_num').val()
				};
			}


			$.post('/seller/login-user-auth-check', param, function(response) {

				if(response.isSuccess && response.data) {
					if(response.data == 'FAIL') {
						alert("인증번호가 일치하지 않아 로그인할 수 없습니다.");
					}else if(response.data == 'EXPIRED_PWD'){
						clearInterval(timer);
						$("#authNumDiv").addClass("disabled-div");
						popup = window.open('/seller/change-password', 'sellerChgPwd', popupType);
					}else if(response.data == 'SUCC'){
						// 관리자 로그인
						location.href = '/seller/index';
					}
				}
			});

		}
	}

	// 이메일이 등록되어 있지 않은 사용자의 이메일을 저장하며, 인증번호를 메일로 보낸다.
	function saveEmail() {
		let opEmail = $('#op_email').val();
		if(opEmail.trim() == ''){
			alert('저장할 이메일을 입력해주세요.');
		} else {
			// 2. 로그인 사용자 확인
			var param = {
				'loginId': $('#op_username').val(),
				'password': $('#op_password').val(),
				'opEmail': $('#op_email').val()
			};

			$.post('/seller/login-user-email-save', param, function(response) {

				if(response.isSuccess && response.data) {
					if(response.data == 'FAIL') {
						alert("인증번호가 일치하지 않아 로그인할 수 없습니다.");
					}else if(response.data == 'SUCC'){
						// 관리자 로그인
						//location.href = '/opmanager';
						// 이메일로 발송된 인증번호를 입력하는 UI 보이기
						//$('#authNumDiv').css('display', 'flex');
						$('#authNumDiv').css('display', 'flex');
						$('#authNumDiv').removeClass('disabled-div');
						$('#emailDiv').css('display', 'none');
						$('#emailDiv').addClass('disabled-div');
						$('#idpwDiv').addClass('disabled-div');
						alert('이메일로 발송된 인증번호를 입력해주세요.\n\n※ 사용하시는 이메일에 따라 인증번호 발송에\n시간이 소요될 수 있습니다.');
						$("#op_auth_num").focus();
					}
				}
			});

		}
	}


</script>
</page:javascript>

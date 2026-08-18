<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);

	if (request.getProtocol().equals("HTTP/1.1"))
    	response.setHeader("Cache-Control", "no-cache");
%>
<style type="text/css">
	.login_set h4 {color: #fff;}
	.locgov_login {display: inline-block;width: 50%;white-space: normal;text-align: center;font-size: 32px;font-weight: bold;color: #333;}
	.offline_login {display: inline-block;width: 50%;white-space: normal;text-align: center;font-size: 32px;font-weight: bold;color: #333;}

	.disabled-div {
		pointer-events: none;		/* 마우스 이벤트 비활성화 */
		opacity: 0.6;				/* 시각적으로 비활성화된 것처럼 보이게 함 */
	}
</style>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-ui.min.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery.blockUI.js"></script>
<!-- ML4WEB JS -->
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/ML_Config.js"></script>
<!-- 간편인증 JS -->
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ML4Web_Config.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/magicline-js-api.min.js"></script>

<script type="text/javascript" src="/content/modules/op.fincert.js"></script>

<div class="admin_wrap">
	<!-- Header -->
	<div id="header">
		<div class="header_wrap">
			<h1><a href="javascript:;"><img src="../content/opmanager/images/img_logo.png" alt="고향사랑기부제" /></a></h1>
			<div class="tnb"></div>
		</div>
	</div>
	<!--// Header -->

	<!-- Container -->
	<div id="container" class="login">
		<div class="login_cont">
			<div class="login_tit">관리자 로그인</div>
			<div class="login_tit"><!-- 지자체 담당자 --></div><!-- <div class="offline_login">오프라인 담당자</div> -->
			<div class="login_form_box mt30">
				<!-- 공동/금융인증서 -->
				<div class="login_form auth" style="margin: auto;">
					<div class="top_area">
						<h2 class="top_tit">인증서 로그인</h2>
					</div>
					<div class="cont_area">
						<div class="button_wrap">
							<button type="button" class="btn btn-dark-gray" onclick="doFinanceSignData();"><span>금융인증서</span></button>
							<button type="button" class="btn btn-dark-gray" onclick="doSignData();"><span>공동인증서</span><span class="text-lg">(구 공인인증서)</span></button>
						</div>
						<div class="info_area">
							<p class="txt pr10">인증서가 사전 등록되어 있어야<br>로그인이 가능합니다.</p>
							<button type="button" onclick="pkiForm();" class="btn btn-dark-gray btn-mini"><span>인증서 등록/삭제</span></button>
						</div>
					</div>
				</div>
				<!--// 공동/금융인증서 -->

				<!-- 아이디 로그인 -->
				<div class="login_form pwd">
					<div class="top_area">
						<h2 class="top_tit">아이디 로그인</h2>
					</div>
					<div class="cont_area">
						<div class="flex_box mb10" id="idpwDiv">
							<div class="flex_box col">
								<input type="text" id="op_username" maxlength="40" class="ime-mode-disabled" title="아이디" placeholder="${op:message('M00081')}" />
								<input type="password" id="op_password" maxlength="20" autocomplete="off" title="${op:message('M00150')}" placeholder="${op:message('M00150')}"/>
							</div>
							<div class="button_wrap">
								<button type="button" onclick="idLogin();" class="btn btn-dark-gray btn-lg"><span>인증번호</br>발송</span></button>
							</div>
						</div>
						<div class="checkbox">
							<input type="checkbox" id="id_save">
							<label for="id_save">아이디 저장</label>
						</div>


						<div class="flex_box mt15" id="emailDiv" style='display:none'>
							<div class="flex_box col">
								<input type="text" id="op_email" maxlength="40" class="ime-mode-disabled" title="이메일" placeholder="이메일" autocomplete="off"/>
							</div>
							<div class="button_wrap">
								<button type="button" onclick="saveEmail();" class="btn btn-dark-gray btn-lg" style="height:100%"><span><c:out value="저장"/></span></button>
							</div>
						</div>

						<div class="flex_box mb15 disabled-div" id="authNumDiv">
							<div class="flex_box col" style="position:relative">
								<input type="text" id="op_auth_num" maxlength="40" onkeyup="authCheckKeyup(event);" class="ime-mode-disabled" title="인증번호" placeholder="인증번호" autocomplete="off"/>
								<span class="time" style="position:absolute;right:10px;top:50%;transform:translateY(-50%);">05:00</span>
							</div>
							<div class="button_wrap">
								<button type="button" onclick="authCheck();" class="btn btn-dark-gray btn-lg" style="height:100%"><span><c:out value="${op:message('M00796')}"/></span></button> <!-- 로그인 ログイン -->
							</div>
						</div>
					</div>
				</div>
				<!--// 아이디 로그인 -->
			</div>
			<div class="login_sub_tit">
				<div id="login_after"></div>
				<div id="login_before"></div>
				<ul class="mb10">
					<li><span class="txt">○ <span style="font-weight:bold">인증서 로그인</span></span></li>
					<li><span class="txt pl20">- 원활한 이용을 위해 인증서를 <span style="font-weight:bold">미리 발급 및 등록</span>해 주시기 바랍니다.</span></li>
					<li><span class="txt pl20">- 인증서가 <span style="font-weight:bold">만료</span>되었을 경우 <span style="font-weight:bold">삭제 후 등록</span>하여 사용해 주시기 바랍니다.</span></li>
				</ul>
				<ul class="mb30">
					<li><span class="txt">○ <span style="font-weight:bold">아이디 로그인</span></span></li>
					<li><span class="txt pl20">- 아이디와 패스워드를 입력하신 후 <span style="font-weight:bold">[인증번호 발송]</span>을 누르면 <span style="font-weight:bold">회원정보에 등록하신 메일주소</span>로 인증번호가 발송됩니다.</span></li>
					<li><span class="txt pl20">- 메일주소를 잊으셨거나 로그인에 문제가 있을 경우 <span style="color:#d40000;font-weight:bold">1522-2431</span>(고향사랑e음 고객센터)로 문의하시기 바랍니다.</span></li>
				</ul>
				<ul>
					<li><span style="font-weight:bold">※ 국민비서 알림서비스</span> : 국민비서 회원에게만 제공되며, 관리자 로그인 알림이 발송됩니다.</br></li>
					<li><span class="pl20" style="color: blue;">→</span><a href="https://www.ips.go.kr/pot/forwardMain.do" target="_blank" rel="noreferrer" style="text-decoration: underline; color: blue;"><strong> 국민비서 가입하기</strong></a>(신청 후 다음날부터 적용)</li>
				</ul>
			</div>
		</div>
	</div>

	<form id="idLoginForm" action="<c:url value='/op_security_login'/>" method="POST" >
		<page:csrf />
		<input type="hidden" name="target"			value="${fn:escapeXml(target)}" />
		<input type="hidden" name="failureUrl"		value="${fn:escapeXml(requestContext.requestUri)}?target=${fn:escapeXml(target)}" />
		<input type="hidden" name="op_login_type"	value="ROLE_OPMANAGER" />
		<input type="hidden" name="_csrf"			value="${fn:escapeXml(_csrf.token)}"/>
		<input type="hidden" name="_csrf_header"	value="${fn:escapeXml(_csrf.headerName)}"/>
		<input type="hidden" name="op_username" />
		<input type="hidden" name="op_password" />
	</form>
	<!--// Container -->

	<!-- footer -->
	<div id="footer">
		<div class="footer_wrap">
			<span class="copy">© Ministry of the interior and safety. All rights reserved.</span>
		</div>
	</div>
	<!--// footer -->
</div>
<input type="hidden" id="frontendUrl" name="frontendUrl" value="${fn:escapeXml(frontendUrl)}">

<!-- 비밀번호 변경 팝업 파라미터 -->
<form id="passwordForm">
	<input type="hidden"	id="loginId"	name="loginId"	/>
	<input type="hidden"	id="pageType"	name="pageType"	/>
</form>
<!--// 비밀번호 변경 팝업 파라미터 -->

<!-- 결과 수신 메시지  -->
<form id="reqForm" name="reqForm" method="post" action="/opmanager/magicline/signedFormRGhlove" target="magiclineIframe">
	<input type="hidden"	id="signOrigin"		name="signOrigin" /> <!-- 180701 서명 원문 폼 추가 -->
	<input type="hidden"	id="sign"			name="sign"/>
	<input type="hidden"	id="csCheckType"	name="csCheckType"	value="1"/>
	<input type="hidden"	id="signData"		name="signData"		value="LOGIN" />
</form>
<!--// 결과 수신 메시지  -->

<!-- magic line 영역 -->
<iframe name="magiclineIframe" style="display:none;" width="1200px" height="500px"></iframe>
<!--// magic line 영역 -->
<script type="text/javascript" src="/content/popup/popup.js"></script>
<script type="text/javascript">
	var timer = null;
	var isRunning = false;

	$(function() {
		pop.openNotice('O');
		if(!Common.loginSetting()){
			//$('.login_sub_tit').remove();
			//$('.login_form.pwd').remove();
			//$('.login_form.auth').css("width", "600px").css("margin-top", "20px");
			$('#login_after').remove();
		}else{
			$('#login_before').remove();
			$('.locgov_login').remove();
			$('.offline_login').remove();
		}

		// 아이디 저장 확인
		if(localStorage.getItem('saved_manager_login_id')) {
			$('#id_save').prop("checked", true);
			$('#op_username').val(localStorage.getItem('saved_manager_login_id'));
		}

		// 포커스.
		$('#op_username').val() == '' ? $('#op_username').focus() : $('#op_password').focus();

		// enter Key Event
		$('#op_username, #op_password').keypress(function(e) {
			if (e.keyCode === 13) {
				e.preventDefault();
				idLogin();
			}
		});
		var errorCode = '<c:out escapeXml="true" value="${param.error}" />';

		if (errorCode != "") {
			var errorMsg = Message.get("M01344");
			var errorLink = '';

			if (errorCode == '4') {
				errorMsg = '계정이 잠겨 있습니다.';
				errorLink = '/opmanager/login-lock';

			} else if (errorCode == '5') {
				errorMsg = '비밀번호상태가 임시 또는 만료되었습니다.';
				errorLink = '/opmanager/login-change-password';

			} else if (errorCode == '98') {
				errorMsg = '다른 기기에서 로그인하여 자동으로 로그아웃 되었습니다.';

			} else if (errorCode == '99') {
				errorMsg = '세션이 종료 되었습니다.';

			} else if (errorCode == '6') {
				errorMsg = '입력하신 아이디는 관리자 권한 중지 상태입니다. 상위 관리자에게 문의주세요.';
			}

			setTimeout(function() {
				alert(errorMsg);
				if (errorLink != '') {
					location.href = errorLink
				}
			}, 100);
		}
	});

	try {
		if (opener) {
			self.close();
			opener.location.href=url("/opmanager/login?taget=${fn:escapeXml(target)}");
		}
	} catch(e) {
		alert(e.message)
	};

	/**
	 *	함 수 명 : idLogin
	 *	기	능  : 아이디 로그인 실행
	 */
	function idLogin() {
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}

		// 1. ID/PW 유효성 검사
		if(!validator()) return false;

		// 2. 로그인 사용자 확인
		var param = {
			'loginId': $('#op_username').val(),
			'password': $('#op_password').val()
		};

		$.post('/opmanager/user/login-user-valid', param, function(response) {

			if(response.isSuccess && response.data) {
				idLoginResult(response.data);
			}
		});
	}

	/**
	 *	함 수 명 : idLoginResult
	 *	기	능  : 로그인 사용자 확인 결과코드에 따른 액션 처리
	 *	파라미터  : data.code - 결과코드
	 */
	function idLoginResult(data) {
		if(!data.code) {
			alert("오류가 발생했습니다.");

//		}else if(data.code === "SUCC") {
		}else if((data.code === "SUCC") || data.code.indexOf("PASS_CHANGE_C") > -1) {
			// 아이디 저장 체크
			$('#id_save').is(":checked") ? saveId() : removeId();

			//////////////////////////
			// 20251016. 기존 로직을 유지하며 ID/PW를 비교하여 정상적으로 메인화면으로 가는 로직에서 이메일 체크를 추가한다.
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

		} else if(data.code.indexOf("PASS_CHANGE_") > -1) {
			var pageType = data.code.replace("PASS_CHANGE_", "");

			if(!pageType) {
				alert("오류가 발생했습니다.");
				return false;
			}

			// 비밀번호 변경 팝업 호출
			passwordChangePopup(pageType);

		}/* else if(data.code === "ERR_LOCGOV"){
			alert("지자체 담당자는 인증서로 로그인해 주시기 바랍니다.");

		} */ else if(data.code === "REQ") {
			alert("관리자 권한 요청을 확인중입니다.");

		} else if(data.code === "REQ_NOT") {
			managerRequestForm();

		} else if(data.code === "ERR_ROCK") {
			alert("계정이 잠겨 있습니다.");
			location.href = "/opmanager/login-lock";

		} else if(data.code === "EMPTY_CI") {
			alert('본인인증이 필요한 계정입니다. 회원 정보 수정 페이지에서 본인인증을 해주십시오.');
			location.href = $('#frontendUrl').val()+'/users/modify.html';
		} else if (data.code === "ERR") {
			alert("오류가 발생했습니다.");
		} else {
			alert("ID 또는 비밀번호 오류로 로그인할 수 없습니다.");
			$('#op_username').focus();
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

	/**
	 *	함 수 명 : loginSubmit
	 *	기	능  : 로그인
	 */
	function loginSubmit() {
		$.get("/opmanager/user/password-enc", {"password": $('#op_password').val()}, function(response){
			if(response.isSuccess && response.data) {
				$("input[name='op_username']").val($('#op_username').val());
				$("input[name='op_password']").val(response.data);
				$('#idLoginForm').submit();
			} else {
				alert("오류가 발생했습니다.");
			}
		}, "json");
	}

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
	 *	함 수 명 : saveId
	 *	기	능  : 아이디 저장 (체크)
	 */
	function saveId() {
		localStorage.setItem('saved_manager_login_id', $('#op_username').val());
	}

	/**
	 *	함 수 명 : removeId
	 *	기	능  : 아이디 저장 (해제)
	 */
	function removeId() {
		localStorage.removeItem('saved_manager_login_id');
	}

	/**
	 *	함 수 명 : managerRequestForm
	 *	기	능  : 관리자 권한 요청 폼 이동
	 */
	function managerRequestForm() {
		var loginId = $('#op_username').val();
		var password = $('#op_password').val();

		if(!loginId || !password) {
			alert("오류가 발생했습니다.");
			return false;
		}

		var form = document.createElement('form');
		form.setAttribute("charset"	, "UTF-8");
		form.setAttribute("method"	, "POST");
		form.setAttribute("action"	, "/opmanager/manager-request/form");

		var hidden = document.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setAttribute("name", "loginId");
		hidden.setAttribute("value", loginId);
		form.appendChild(hidden);

		hidden = document.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setAttribute("name", "password");
		hidden.setAttribute("value", password);
		form.appendChild(hidden);

		document.body.appendChild(form);
		form.submit();
	}

	/**
		인증서 로그인 시 권한요청
	*/
	function managerRequestPkiForm(mberDn) {

		var form = document.createElement('form');
		form.setAttribute("charset"	, "UTF-8");
		form.setAttribute("method"	, "POST");
		form.setAttribute("action"	, "/opmanager/manager-request/pkiForm");

		var hidden = document.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setAttribute("name", "mberDn");
		hidden.setAttribute("value", mberDn);
		form.appendChild(hidden);

		document.body.appendChild(form);
		form.submit();
	}

	/**
	인증서 로그인 시 권한요청 ( 금융인증서 )
	*/
	function managerRequestPkiFormFin(mberDn) {

		var form = document.createElement('form');
		form.setAttribute("charset"	, "UTF-8");
		form.setAttribute("method"	, "POST");
		form.setAttribute("action"	, "/opmanager/manager-request/pkiFormFin");

		var hidden = document.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setAttribute("name", "mberFinDn");
		hidden.setAttribute("value", mberDn);
		form.appendChild(hidden);

		document.body.appendChild(form);
		form.submit();
	}

	/**
	 *	함 수 명 : passwordChangePopup
	 *	기	능  : 패스워드 변경 팝업
	 *	파라미터  : pageType - 팝업 타입
	 */
	function passwordChangePopup(pageType) {
		// 파라미터 셋팅
		$("#passwordForm #loginId").val($('#op_username').val());
		$("#passwordForm #pageType").val(pageType);

		// 팝업 활성화
		var form = $("#passwordForm")[0];
		var url = "/opmanager/user/popup/temp-password-change";
		Common.popup("", "tempPasswordChangePopup", 550, 510, 1, 0, 0);
		form.target = "tempPasswordChangePopup";
		form.action = url;
		form.method = "post";
		form.submit();
		form.target = "_self";
	}

	/**
	 *	함 수 명 : passwordChangeCallBack
	 *	기	능  : 패스워드 변경 후 로그인
	 *	파라미터  : password - 변경된 패스워드
	 */
	function passwordChangeCallBack(password) {
		$('#op_password').val(password);
		loginSubmit();
	}

	/**
	 *	함 수 명 : pkiForm
	 *	기	능  : 인증서 등록
	 */
	function pkiForm() {
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}
		location.href = "/opmanager/user/pki/form";
	}

	/**
	 *	함 수 명 : doSignData
	 *	기	능  : 인증서 활성화
	 */
	function doSignData() {
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}

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
			$.post('/opmanager/user/pki/login-user-valid', {"mberDn": response.DN}, function(response) {
				if(response.isSuccess && response.data && response.data.code) {
					if(response.data.code == "SUCC") {
						location.href = "/opmanager";

					} else if (response.data.code == "ERR_STS" && response.data.statusCode == "2") {
						alert("입력하신 아이디는 관리자 권한 중지 상태입니다. 상위 관리자에게 문의주세요.");

					} else if (response.data.code == "ERR_MNG_NOT"){
						managerRequestPkiForm(response.DN);

					} else if (response.data.code != "ERR" && response.data.code != "FAIL") {
						alert("회원정보가 존재하지 않아 로그인할 수 없습니다.");

					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		} else {
			alert("오류가 발생했습니다.");
		}
	}

	/**
	 *	함 수 명 : loginInfoPopup
	 *	기	능  : 로그인 안내 팝업
	 */
	function loginInfoPopup() {
		var popupWidth = 340;
		var popupHeight = 345;
		var popupY = (window.screen.height / 2) - (popupHeight / 2);
		var popupX = (window.screen.width / 2) - (popupWidth / 2);
		popupX += window.screenLeft;

		var imageWin = window.open("", "login_info_popup", "status=no, height=" + popupHeight  + ", width=" + popupWidth  + ", left="+ popupX + ", top="+ popupY);
		if(imageWin) {
			imageWin.document.open();
			imageWin.document.write("<html>");
			imageWin.document.write("<head><title>${fn:escapeXml(shopContext.config.shopName)} 관리자</title></head>");
			imageWin.document.write("<body style='margin:0'>");
			imageWin.document.write("<a href=javascript:window.close()><img src='/content/images/popup/login-info.jpg' border=0></a>");
			imageWin.document.write("</body>");
			imageWin.document.write("<html>");
			imageWin.document.close();
		}
	}

	/**
	 *	함 수 명 : presentInfoPopup
	 *	기	능  : 답례품 안내 팝업
	 */
	function presentInfoPopup() {
		var popupWidth = 340;
		var popupHeight = 500;
		var popupY = (window.screen.height / 2) - (popupHeight / 2);
		var popupX = (window.screen.width / 2) - (popupWidth / 2);
		popupX += window.screenLeft;

		var imageWin = window.open("", "present_info_popup", "status=no, height=" + popupHeight  + ", width=" + popupWidth  + ", left="+ popupX + ", top="+ popupY);
		if(imageWin) {
			imageWin.document.open();
			imageWin.document.write("<html>");
			imageWin.document.write("<head><title>${fn:escapeXml(shopContext.config.shopName)} 관리자</title></head>");
			imageWin.document.write("<body style='margin:0; overflow-x:hidden; overflow-y:hidden;'>");
			imageWin.document.write("<a href=javascript:window.close()><img src='/content/images/popup/present_info.jpg' border=0></a>");
			imageWin.document.write("</body>");
			imageWin.document.write("<html>");
			imageWin.document.close();
		}
	}

	function doFinanceSignData() {
		if(!Common.login()){
			alert('시스템 점검중입니다.');
			return false;
		}

		$("#authModal").removeClass('show');

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
			var param = {
				"DN" : response.dn
			}
			userLoginAuth(param);
		} else {
			alert("오류가 발생했습니다.");
		}
	}

	function userLoginAuth (response) {
		var params = {
			mberFinDn : response.DN
		};

		$.post('/opmanager/user/pki/login-user-valid', params, function(response) {
			if(response.isSuccess && response.data && response.data.code) {

				if(response.data.code == "SUCC") {
					location.href = "/opmanager";

				} else if (response.data.code == "ERR_STS" && response.data.statusCode == "2") {
					alert("입력하신 아이디는 관리자 권한 중지 상태입니다. 상위 관리자에게 문의주세요.");
				} else if (response.data.code == "ERR_MNG_NOT"){
					managerRequestPkiFormFin(response.data.mberFinDn);
				} else if (response.data.code != "ERR" && response.data.code != "FAIL") {
					alert("회원정보가 존재하지 않아 로그인할 수 없습니다.");

				} else {
					alert("오류가 발생했습니다.");
				}
			}
		});
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
			'loginId': $('#op_username').val(),
			'password': $('#op_password').val()
		};

		$.post('/opmanager/user/login-user-email', param, function(response) {

			if(response.isSuccess && response.data) {
				if(response.data == 'NOEMAIL'){
					alert('이메일을 등록하고 수신된 인증번호를 입력해야 로그인 가능합니다.');
					$('#authNumDiv').css('display', 'none');
					$('#authNumDiv').addClass('disabled-div');
					$('#emailDiv').css('display', 'flex');
					$('#emailDiv').removeClass('disabled-div');
					// email 입력 란, focus in
					$('#op_email').focus();
					// 팝업에서 이메일 받기
					//let id = $('#op_username').val();
					//javascript:emailModifyPopup(id);
				}else if(response.data == 'FAIL') {

				}else if(response.data.startsWith('SUCC')){
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
	/**
	 *	함 수 명 : emailModifyPopup
	 *	기	능  : 이메일 등록 팝업
	 */
	function emailModifyPopup(loginId) {
		var url = "/opmanager/user/popup/emailModifyInfo/"+loginId;
		var popupName = "/opmanager/user/popup/emailModifyInfo";
		Common.popup(url, popupName, 800, 300, 1, 0, 0);
	}


	function authCheckKeyup(e){

		if (e.keyCode === 13) {
			authCheck();
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


			$.post('/opmanager/user/login-user-auth-check', param, function(response) {

				if(response.isSuccess && response.data) {
					if(response.data == 'FAIL') {
						alert("인증번호가 일치하지 않아 로그인할 수 없습니다.");
					}else if(response.data == 'SUCC'){
						// 관리자 로그인
						location.href = '/opmanager';
					}else if(response.data == 'PASS_CHANGE_C'){
						// 비밀번호 변경 팝업 호출
						clearInterval(timer);
						$("#authNumDiv").addClass("disabled-div");
						var pageType = response.data.replace("PASS_CHANGE_", "");
						passwordChangePopup(pageType);
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

			$.post('/opmanager/user/login-user-email-save', param, function(response) {

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
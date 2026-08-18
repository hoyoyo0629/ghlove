<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>

<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-ui.min.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery.blockUI.js"></script>
<!-- <script type="text/javascript" src="/MagicLine4Web/ML4Web/js/ext/json2.js"></script> -->
<!-- ML4WEB JS -->
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/ML_Config.js"></script>
<!-- 간편인증 JS -->
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ML4Web_Config.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/magicline-js-api.min.js"></script>

<%@ page import="com.dreamsecurity.magice2e.MagicE2E" %>
<%@ page import="com.dreamsecurity.magicline.util.Base64" %>
<%
	String sessionString = "";
	int result = 0;
	StringBuffer sbCert = new StringBuffer();
	MagicE2E.setConfPath(request.getAttribute("conf-path").toString());
// 	MagicE2E.setConfPath("C:/icraft/workspace/ghlove/magicline/conf/");

	// 세션에 값이 있는지 확인

	MagicE2E temp = ( MagicE2E ) session.getAttribute("Magie2e");

	if( temp == null ){
		MagicE2E ml = new MagicE2E( sbCert );
		sessionString = sbCert.toString();
		session.setAttribute( "Magie2e", ml );
	}else{
		result = MagicE2E.init();
		if( result == 0 ){
			result = temp.open( sbCert );
			if( result == 0 ){
				sessionString = sbCert.toString();
			}else{
				temp.close();
				session.invalidate();
			}
		}else{
			temp.close();
			session.invalidate();
		}

	}
%>
<script src="/content/MagicLine4Web/ML4Web/js/crypto/magicjs_1.2.7.2.min.js"></script>
<script src="/content/MagicLine4Web/ML4Web/js/magic_e2e.js"></script>

<div class="admin_wrap">
	<!-- Header  -->
	<div id="header">
		<div class="header_wrap">
			<h1><a href="javascript:;"><img src="../content/opmanager/images/img_logo.png" alt="고향사랑기부제" /></a></h1>
			<div class="tnb"></div>
		</div>
	</div>
	<!--// Header  -->

	<!-- Container -->
	<div id="container" class="login">
		<div class="login_cont">
			<div class="login_tit">관리자 로그인</div>
			<div class="login_sub_tit">최초 1회 로그인 시 관리자 권한 요청 페이지로 이동합니다.<br>승인 후 정상적으로 이용이 가능합니다.</div>
			<div class="login_form_box">
				<!-- 공동/금융인증서 -->
				<div class="login_form auth">
					<div class="top_area">
						<h2 class="top_tit">공동 · 금융인증서</h2>
					</div>
					<div class="cont_area">
						<div class="button_wrap">
							<button type="button" onclick="doSignData();" class="btn btn-dark-gray btn-large"><span>인증서 로그인</span></button>
						</div>
						<div class="info_area">
							<p class="txt">인증서가 사전 등록되어 있어야 로그인이 가능합니다.</p>
							<button type="button" onclick="alert('준비중입니다');" class="btn btn-dark-gray btn-mini"><span>인증서 등록</span></button>
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
						<div class="flex_box">
							<div class="flex_box col">
								<input type="text" id="op_username" maxlength="40" class="ime-mode-disabled" title="아이디" placeholder="${op:message('M00081')}" />
								<input type="password" id="op_password" maxlength="20" title="${op:message('M00150')}" placeholder="${op:message('M00150')}"/>
							</div>
							<div class="button_wrap">
								<button type="button" onclick="idLogin();" class="btn btn-dark-gray btn-lg"><span><c:out value="${op:message('M00796')}"/></span></button> <!-- 로그인 ログイン -->
							</div>
						</div>
						<div class="checkbox">
							<input type="checkbox" id="id_save">
							<label for="id_save">아이디 저장</label>
						</div>
					</div>
				</div>
				<!--// 아이디 로그인 -->
			</div>
		</div>
	</div>

	<form id="idLoginForm" action="<c:url value='/op_security_login'/>" method="POST" >
		<page:csrf />
		<input type="hidden" name="target"			value="${target}" />
		<input type="hidden" name="failureUrl"		value="${requestContext.requestUri}?target=${target}" />
		<input type="hidden" name="op_login_type"	value="ROLE_OPMANAGER" />
		<input type="hidden" name="_csrf"			value="${_csrf.token}"/>
		<input type="hidden" name="_csrf_header"	value="${_csrf.headerName}"/>
		<input type="hidden" name="op_username" />
		<input type="hidden" name="op_password" />
	</form>
	<!--// Container -->

	<!-- footer  -->
	<div id="footer">
		<div class="footer_wrap">
			<span class="copy">© Ministry of the interior and safety. All rights reserved.</span>
		</div>
	</div>
	<!--// footer  -->
</div>

<!-- 비밀번호 변경 팝업 파라미터 -->
<form id="passwordForm">
	<input type="hidden"	id="loginId"	name="loginId"	/>
	<input type="hidden"	id="pageType"	name="pageType"	/>
</form>
<!--// 비밀번호 변경 팝업 파라미터 -->

<form id='encForm' name='encForm' method='post'>
	<input type="hidden" id="encIdn" name="encIdn" value=""/>
</form>

<form id='reqForm' name='reqForm' method='post' action="/opmanager/magicline/vidClientIDNR" target="vidClientIDNRIframe">
	<!-- 결과 수신 메시지  -->
	<input type="hidden" id="signOrigin" name="signOrigin" /> <!-- 180701 서명 원문 폼 추가 -->
	<input type="hidden" id='sign' name='sign'/>
	<input type="hidden" id="signOrigin" name="signOrigin" />
	<input type="hidden" id="vidRandom" name="vidRandom"/>
	<input type="hidden" id="vidType" name="vidType" value="client"/>
	<input type="hidden" id="encData" name="encData" value=""/>
	<input type="hidden" id="signData" name="signData" value="LOGIN" />
	<input type="text" name="idn"  id="idn" value="742212-1234567"/><!-- 주민번호 -->
</form>
<iframe name="vidClientIDNRIframe" style="display:block;" width="500px" height="500px"></iframe>

<script type="text/javascript">
	$(function() {

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

		// 오류확인
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
				errorMsg = '입력하신 아이디는 관리자 권한 중지 상태입니다.  상위 관리자에게 문의주세요.';
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
			opener.location.href=url("/opmanager/login?taget=${target}");
		}
	} catch(e) {
		alert(e.message)
	};

	/**
	 *	함 수 명 : idLogin
	 *	기	능  : 아이디 로그인 실행
	 */
	function idLogin() {

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
	 *	파라미터  : data.code - 결과코드, data.userId - REQ_NOT 일 경우만 전달.
	 *	 1. SUCC: 성공
	 *	 2. REQ: 관리자 신청중
	 *	 3. REQ_NOT: 관리자 신청안됨
	 *	 4. ERR_MNG_PASS: 관리자 패스워드 오류
	 *	 5. ERR_USER_NOT: 일반회원 정보 존재하지 않음
	 *	 6. ERR_USER_PASS: 일반회원 패스워드 오류
	 *	 7. ERR: 오류
	 *	 8. PASS_CHANGE_* (C/I/F): 비밀번호 변경
	 *	 9. ERR_ROCK: 계정잠김
	 */
	function idLoginResult(data) {
		if(!data.code) {
			alert("오류가 발생했습니다.");

		}else if(data.code === "SUCC") {
			// 아이디 저장 체크
			$('#id_save').is(":checked") ? saveId() : removeId();

			// 관리자 로그인
			loginSubmit();

		} else if(data.code.indexOf("PASS_CHANGE_") > -1) {
			var pageType = data.code.replace("PASS_CHANGE_", "");

			if(!pageType) {
				alert("오류가 발생했습니다.");
				return false;
			}

			// 비밀번호 변경 팝업 호출
			passwordChangePopup(pageType);

		} else if(data.code === "REQ") {
			alert("관리자 권한 요청을 확인중입니다.");

		} else if(data.code === "REQ_NOT") {
			managerRequestForm(data.userId);

		} else if(data.code === "ERR_MNG_PASS" || data.code === "ERR_USER_PASS"){
			alert("패스워드가 일치하지 않습니다.");

		} else if(data.code === "ERR_ROCK") {
			loginSubmit();

		} else {
			alert("회원정보가 존재하지 않아 로그인할 수 없습니다.");
			$('#op_username').focus();
		}
	}

	/**
	 *	함 수 명 : loginSubmit
	 *	기	능  : 로그인
	 */
	function loginSubmit() {
		$("input[name='op_username']").val($('#op_username').val());
		$("input[name='op_password']").val($('#op_password').val());
		$('#idLoginForm').submit();
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
	 *	파라미터  : userId - 회원ID
	 */
	function managerRequestForm(userId) {
		if(userId) {
			var form = document.createElement('form');
			form.setAttribute("charset"	, "UTF-8");
			form.setAttribute("method"	, "POST");
			form.setAttribute("action"	, "/opmanager/manager-request/form");

			var hidden = document.createElement("input");
			hidden.setAttribute("type", "hidden");
			hidden.setAttribute("name", "userId");
			hidden.setAttribute("value", userId);
			form.appendChild(hidden);

			document.body.appendChild(form);
			form.submit();

		} else {
			alert("오류가 발생했습니다.");
		}
	}

	/**
	 *	함 수 명 : passwordChangePopup
	 *	기	능  : 패스워드 변경 팝업
	 *	파라미터  : pageType (I: 관리자 비밀번호 초기화, C: 관리자 비밀번호 변경, F: 초기 비밀번호 설정)
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
		$("input[name='op_username']").val($('#op_username').val());
		$("input[name='op_password']").val(password);
		$('#idLoginForm').submit();
	}

	// 인증서 클릭 이벤트
	function doSignData(){
		var signData = $("#signData").val();

		document.reqForm.signOrigin.value = document.reqForm.signData.value;
		magicline.uiapi.MakeSignData(document.reqForm, null, mlCallBack);
	}

	// MagicLine 결과값 수신 CallBack
	// code    : 전자서명 결과값
	// message : 전자서명 메시지
	function mlCallBack(code, message){
		console.log('code', code);
		console.log('message', message);
		if(code==0){ // 정상
			//console.log(message);
			document.reqForm.sign.value = encodeURIComponent( message.encMsg );

			// 개인정보 암호화
			if($('#idn').val() != ""){
				dataEncrypt($('#idn').val());
			}

			if(message.vidRandom != null){
				document.reqForm.vidRandom.value = encodeURIComponent(message.vidRandom);
				document.reqForm.action = "/opmanager/magicline/vidClientIDNR";
				document.reqForm.submit();
			}else{
				magicline.uiapi.getRandomfromPrivateKey(function(code, vidRandom){
					if(code == 0){
						document.reqForm.vidRandom.value = vidRandom;
						document.reqForm.action = "/opmanager/magicline/vidClientIDNR";
						document.reqForm.submit();
					}
				});
			}
			//signResultDralwer(message);

		}else{ // 수신 오류
			alert("결과값 수신에 실패하였습니다.");
			return;
		}
	}

	function dataEncrypt(idn){
		var ml = new MagicE2E(<%=sessionString%>);

		$('#encIdn').val(idn);

		document.reqForm.encData.value = ml.Encrypt($('#encForm').serialize());
	}

	// 인증서 검증 결과 callback
	function signedResponse(result) {
// 		DN		: "cn=850테스트유효001,ou=people,ou=상호연동테스트,o=Government of Korea,c=KR"
// 		message : ""
// 		result	:"SUCCESS"
		console.log('result', result);
	}
</script>
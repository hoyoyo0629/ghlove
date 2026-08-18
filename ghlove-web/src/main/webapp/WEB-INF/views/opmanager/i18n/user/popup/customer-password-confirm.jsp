<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">비밀번호 확인</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
	<div class="popup_contents">
		<div class="flex_box col item-center">
			<input id="password" title="비밀번호 입력" class="input_txt required _filter wd-150" type="password" value="" placeholder="비밀번호 입력">
			<p class="popup_btns">
				<button type="button" class="btn btn-active" onclick="passwordConfirm();">확인</button>
				<button type="button" class="btn btn-default" onclick="self.close();">취소</button>
			</p>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
		// 윈도우 팝업 사이즈 재조정
		window.resizeTo(450, 300);

		// enter key event
		$('#password').keypress(function(e) {
			if (e.keyCode === 13) {
				e.preventDefault();
				passwordConfirm();
			}
		});

		// 비밀번호 포커스
		$("#password").focus();
	});

	/**
	 *	함 수 명 : passwordConfirm
	 *	기	능  : 패스워드 확인
	 */
	function passwordConfirm() {
		if($.trim($("#password").val()) == "") {
			alert("비밀번호를 입력해 주세요.");
			$("#password").focus();
			return false;
		}

		if($.trim("${fn:escapeXml(userId)}") == "") {
			alert("사용자 정보가 올바르지 않습니다.");
			return false;
		}

		var form = document.createElement('form');
		form.setAttribute("charset"	, "UTF-8");
		form.setAttribute("method"	, "POST");
		form.setAttribute("action"	, "/opmanager/user/customer/popup/access/${fn:escapeXml(userId)}");

		var hidden = document.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setAttribute("name", "password");
		hidden.setAttribute("value", $("#password").val());
		form.appendChild(hidden);

		document.body.appendChild(form);
		form.submit();
	}
</script>
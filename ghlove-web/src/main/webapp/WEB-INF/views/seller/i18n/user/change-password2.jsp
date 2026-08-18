<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<div id="container" class="login">

	<div class="login_area">
		<form id="changeForm" method="POST" action="/seller/login-change-password2">
			<fieldset>
				<legend class="hidden">로그인</legend>

				<h2>초기 비밀번호 설정</h2> <!-- 관리자 로그인 -->
				<p class="guide">최초 접속 시 비밀번호 변경을 진행하시기 바랍니다.</p>

				<div class="inputs">
					<p>
						<span class="label" style="width: 165px;">새 <c:out value='${op:message("M00150")}'/></span> <!-- 비밀번호 -->
						<input type="password" name="changePassword" maxlength="20" class="required _password _duplicated" title="변경할 ${op:message('M00150')}" onkeyup="javascript:checkCapsLock(event, 'newPwdWarn');"/>
						<div id="newPwdWarn" style="display:none;" class="red">CAPS LOCK 키가 동작 중입니다. 대문자로 입력됩니다.</div>
					</p>
					<p>
						<span class="label" style="width: 165px;">새 <c:out value='${op:message("M00150")}'/> 확인</span> <!-- 비밀번호 -->
						<input type="password" name="reChangePassword" maxlength="20" class="required _password _duplicated" title="변경할 ${op:message('M00150')} 재입력" onkeyup="javascript:checkCapsLock(event, 'confirmPwdWarn');"/>
						<div id="confirmPwdWarn" style="display:none;" class="red">CAPS LOCK 키가 동작 중입니다. 대문자로 입력됩니다.</div>
					</p>
				</div>
				<div class="footer">
					<button type="submit" class="btn btn-orange btn-lg"><span>변경하기</span></button>
				</div>
				<div>
					<ul>
						<li>8 ~ 20자 영문, 숫자, 특수문자를 사용하세요. (3개 이상의 연속된 문자나 숫자 사용불가)</li>
						<li>아이디를 포함할 수 없습니다.</li>
					</ul>
				</div>
			</fieldset>
		</form>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		$('#changeForm').validator(function() {

			if (!confirm("비밀번호를 변경 하시겠습니까?")) {
				return false;
			}

			if($('input[name=changePassword]').val() != $('input[name=reChangePassword]').val()){
				alert('비밀번호가 일치하지 않습니다.');
				$('input[name=reChangePassword]').focus();
				return false;
			}
			
			if($('input[name=changePassword]').val().indexOf('${loginId}') > -1){
				alert('비밀번호에 아이디가 포함될수 없습니다.');
				$('input[name=changePassword]').focus();
				return false;
			}
		});
	});

	
	
	function checkCapsLock(event, controlDivId) {
		if (event.getModifierState("CapsLock")) {
			$("#" + controlDivId).show();
		} else {
			$("#" + controlDivId).hide();
		}
	}
</script>
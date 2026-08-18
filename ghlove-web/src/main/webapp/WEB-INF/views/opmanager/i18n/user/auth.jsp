<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<div id="container" class="login">

	<div class="login_area">

		<form id="authForm" method="POST" >
			<page:csrf />
			<fieldset>
				<legend class="hidden">본인인증</legend>
				
				<h2>본인인증</h2> <!-- 관리자 로그인 -->

				<div class="inputs">
					<p>
						<span class="label"></span> <!-- 휴대폰번호 -->
						<button type="button" onclick="sendAuthNumber()" class="btn btn-default btn-m">인증번호 발송</button>
					</p>
					<p>
						<span class="label">인증번호</span> <!-- 인증번호 -->
						<input type="text" name="smsAuth" id="smsAuth" maxlength="20" class="required" title="인증번호" />
						<input type="hidden" name="requestToken" id="requestToken" />
					</p>
					<p class="tip">등록된 휴대폰 번호로 인증번호가 발송 됩니다.</p>
					<div class="footer">
						<button type="submit" class="btn btn-orange btn-lg"><span>본인인증</span></button>
						<a href="/op_security_logout?target=/opmanager" class="btn btn-dark-gray btn-lg"><span>로그아웃</span></a>
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</div>

<script type="text/javascript">

	$(function() {
		$('#authForm').validator(function() {});
	});

	function sendAuthNumber() {

		$.post('/auth/manager-sms-request', {}, function(response){
			Common.responseHandler(response, function(response) {
				$('#requestToken').val(response.data);
			});
		});
	}

</script>			


<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ page import="saleson.common.configuration.SalesonProperty" %>
<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);

	if (request.getProtocol().equals("HTTP/1.1"))
    	response.setHeader("Cache-Control", "no-cache");
%>
<div class="admin_wrap">
	<div id="header">
		<div class="header_wrap">
			<h1><a href="javascript:void(0);"><img src="../content/opmanager/images/img_logo.png" alt="고향사랑기부제" /></a></h1>
		</div>
	</div>
	<div id="container" class="login">
		<div class="login_cont">
			<div class="login_tit mb50">계정 잠김 해지</div>
			<div class="login_sub_tit">비밀번호 5회 오류로 계정이 잠김 처리되었습니다.<br>새로운 비밀번호를 설정하셔야 로그인이 가능합니다.</div>
			<div class="login_sub_tit">고향사랑e음 > 로그인 > 비밀번호 찾기에서 본인인증을 통해 비밀번호 변경이 가능합니다.</div>
			<div class="login_sub_tit">*오프라인 담당자는 상위 관리자에게 비밀번호 초기화를 요청해 주세요.</div>
			<div class="button_wrap">
				<a href="${SalesonProperty.getSalesonUrlFrontend()}/users/find-idpw.html" class="btn btn-dark-gray btn-large" target="_blank" rel="noreferrer"><span>비밀번호 찾기 바로가기</span></a>
				<a href="/opmanager" class="btn btn-default btn-large"><span>관리자 로그인하기</span></a>
			</div>
		</div>
	</div>
	<div id="footer">
		<div class="footer_wrap">
			<span class="copy">© Ministry of the interior and safety. All rights reserved.</span>
		</div>
	</div>
</div>
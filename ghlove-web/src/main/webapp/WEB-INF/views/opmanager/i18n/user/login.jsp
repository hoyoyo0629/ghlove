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
</style>
<script type="text/javascript" src="/content/modules/netfunnel.js"></script>
<script type="text/javascript" src="/content/modules/netfunnel_skin.js"></script>

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
		<button type="button" onclick="goHome();" class="btn btn-dark-gray btn-lg">
			<span>관리자 홈으로 가기</span>
		</button>
	</div>
	<!--// Container -->

	<!-- footer -->
	<div id="footer">
		<div class="footer_wrap">
			<span class="copy">© Ministry of the interior and safety. All rights reserved.</span>
		</div>
	</div>
	<!--// footer -->
</div>
<!--// 결과 수신 메시지  -->
<script type="text/javascript">
	$(function() {
		if(Common.useNetfunnel()){
			NetFunnel_Action({ action_id: 'op_login' }, function (ev, ret) {
	            location.href = '/opmanager/login-main';
	        });
		}else{
			location.href = "/opmanager/login-main";
		}
	});

	function goHome() {
		location.reload();
	}
</script>
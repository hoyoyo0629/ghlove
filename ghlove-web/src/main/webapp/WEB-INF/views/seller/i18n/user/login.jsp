<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<script type="text/javascript" src="/content/modules/netfunnel.js"></script>
<script type="text/javascript" src="/content/modules/netfunnel_skin.js"></script>

<div class="admin_wrap">
	<!-- 상단  -->
	<%@ include file="/WEB-INF/tags/seller/login-header.tag" %>
	
	<!-- 내용 -->
	<div id="container" class="login">
		<button type="button" onclick="goHome();" class="btn btn-dark-gray btn-lg">
			<span>관리자 홈으로 가기</span>
		</button>
	</div>
	
	<!--// footer -->
	<!-- 하단  -->
	<%@ include file="/WEB-INF/tags/seller/login-footer.tag" %>
</div>

<script type="text/javascript">
	$(function() {
		if(Common.useNetfunnel()){
			NetFunnel_Action({ action_id: 'se_login' }, function (ev, ret) {
	            location.href = '/seller/login-main';
	        });
		}else{
			location.href = "/seller/login-main";
		}
	});
	
	function goHome() {
		location.reload();
	}
</script>

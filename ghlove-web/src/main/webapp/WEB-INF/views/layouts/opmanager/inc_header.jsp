<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy/MM/dd (EEE)" var="today"/>

	<div id="header">
		<div class="topMenu">
			<ul class="tnb clear_fix">
				<li class="info"><span>${op:removeIframe(requestContext.user.userName)}</span>님으로 접속중입니다.</li>
				<li class="login"><a class="btn_logout" href="/op_security_logout?target=/opmanager" title="LOGOUT">LOGOUT</a></li>
			</ul><!--// topMenu E-->
		</div><!--// topMenu E-->
		<div class="clear_fix">
			<h1 class="logo">
				<a href="/opmanager" title="${op:removeIframe(shopContext.config.shopName)} 관리자">

					<img src="/content/opmanager/images/cli-logo.png" width="143" alt="고향사랑e음">
				</a>
			</h1>
			<ul class="gnb clear_fix">
				<c:forEach items="${requestContext.opmanagerMenu.firstMenuList}" var="firstMenu">
					<c:set var="menuName">MENU_${firstMenu.menuId}</c:set>
					<c:choose>
						<c:when test="${firstMenu.menuId == requestContext.opmanagerMenu.firstMenuId}">
							<li class="on"><a href="${op:url(firstMenu.menuUrl)}">${op:removeIframe(firstMenu.menuName)}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${op:url(firstMenu.menuUrl)}">${op:removeIframe(firstMenu.menuName)}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul><!--// gnb E-->
		</div>
	</div><!--// header E-->
 	<!-- 내용 -->
 	<div class="admin_wrap">
	<div id="container">
		<c:if test="${main != 'main'}">
			<!-- lnb -->
			<div class="lnb">

				<h2 id="first-menu-name"></h2>
				<ul class="lnbs">

					<!-- MYSQL -->
					<c:forEach items="${requestContext.opmanagerMenu.secondAndThirdMenuList}" var="secondMenu" varStatus="i">
						<c:set var="menuName">MENU_${secondMenu.menuId}</c:set>
						<c:if test="${secondMenu.displayFlag == 'Y'}">
							<li class="menu">
								<a class="on">${op:removeIframe(secondMenu.menuName)}</a>
								<ul class="depth2">
									<c:forEach items="${secondMenu.childMenu}" var="thirdMenu">
										<c:if test="${thirdMenu.displayFlag == 'Y'}">

											<c:if test="${op:url(thirdMenu.menuUrl) != '/opmanager/shop-statistics/sales/user'}">
												<c:set var="menuName">MENU_${thirdMenu.menuId}</c:set>
												<c:choose>
													<c:when test="${thirdMenu.menuCode == requestContext.opmanagerMenu.menuCode}">
														<li class="sub_menu on"><a href="${op:url(thirdMenu.menuUrl)}" class="on">- ${op:removeIframe(thirdMenu.menuName)}</a></li>
													</c:when>
													<c:otherwise>
														<li class="sub_menu"><a href="${op:url(thirdMenu.menuUrl)}">- ${op:removeIframe(thirdMenu.menuName)}</a></li>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:if>
									</c:forEach>
								</ul>
							</li>
						</c:if>
					</c:forEach>
					<%--게시판 목록 --%>
					<c:if test="${!empty boardContext.boardCfgList}">
						<li class="menu">
							<a class="on">게시판 관리</a>
							<ul class="depth2">
								<c:forEach items="${boardContext.boardCfgList}" var="boardCfg">
									<li class="sub_menu ${op:removeIframe(boardCfg.boardCode == boardContext.boardCode ? 'on' : '')}"><a href="/opmanager/board/${op:removeIframe(boardCfg.boardCode)}">- ${op:removeIframe(boardCfg.subject)}</a></li>
								</c:forEach>
							</ul>
						</li>
					</c:if>

				</ul>
			</div>
			<a href="#" class="lnb_handle close">메뉴 닫기</a>

			<script>
				function Lnb(){
					var menu_a  = $('.menu > a');

					menu_a.click(function(e) {
						e.preventDefault();
						if(!$(this).hasClass('on')) {
							$(this).addClass('on').next().stop(true,true).slideDown('800');
						} else {
							$(this).removeClass('on');
							$(this).next().stop(true,true).slideUp('800');
						}
					});
				}
				Lnb();
			</script>

			<div class="contents">
				<div class="contents_inner">
		</c:if>

<script>
//답례품 관리 클릭
function goRresentLogin(url) {
	console.log('url', url);
	$.get('/opmanager/present/login', function(response) {
        if (response.isSuccess) {
            var param = (response.data == null) ? '' : response.data;
//            window.open(url + '?_tss_=' + param, 'presentPopup');
        } else {
			alert(response.errorMessage);
		}
	});
}

function goRresentLogin2(url) {
	console.log('url', url);
	$.get('/opmanager/present/login2', function(response) {
		$.ajax({
			type : 'post',
			url : '${op:property("present.url")}' + '/template/PLUGIN_com_rlcm_ssopg/program/api/v1/auth.php',
// 			url : 'http://gohyang01.bbiz.kr/template/PLUGIN_com_rlcm_ssopg/program/api/v1/auth.php',
// 			url : 'https://152.99.104.9/template/PLUGIN_com_rlcm_ssopg/program/api/v1/auth.php',
			dataType : "json",
			data: {'loginUserInfo': JSON.stringify(response.data)},
			beforeSend : function(xhr){
				xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded; charset=UTF-8");
// 				xhr.setRequestHeader("Access-Control-Allow-Origin","*");


			},
			error: function(xhr, status, error){
// 				alert(error);
			},
			success : function(data){
				var param = (data.resultMsg == null) ? '' : data._tss_;
// 	            window.open('http://gohyang01.bbiz.kr/kwa-com_bos_openmarket_seller?_tss_=' + param, 'presentPopup', '_blank');
// 	            window.open('https://152.99.104.9/kwa-com_bos_openmarket_seller?_tss_=' + param, 'presentPopup', '_blank');
//	            window.open(url + '?_tss_=' + param, 'presentPopup');
// 	            window.open('${op:property("present.url")}' + url + '?_tss_=' + param, 'presentPopup', '_blank');
			},
		});

	});
}
</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy/MM/dd (EEE)" var="today"/>

	<div id="header">
		<div class="topMenu">
			<h2 class="top_logo"><a href="<c:url value="/seller"/>" title="SALESON"></h2>
			<ul class="tnb clear_fix">
				<li class="info"><span>
					${op:removeIframe(sellerContext.seller.sellerName)}
					<c:if test="${sellerContext.sellerUserLogin and !sellerContext.sellerMasterUserLogin}">
						 - ${op:removeIframe(sellerContext.sellerUser.userName)}
					</c:if>
				</span>로 접속중입니다.</li>
				<li class="login"><a class="btn_logout" href="/op_security_logout?target=/seller" title="LOGOUT">LOGOUT</a></li>
			</ul><!--// topMenu E-->
		</div><!--// topMenu E-->
		<div class="clear_fix">
			<h1 class="logo"><a href="/seller" title="${op:removeIframe(shopContext.config.shopName) } 판매자"><!-- 고향사랑e음 --><img src="/content/opmanager/images/cli-logo.png" width="143" alt="고향사랑e음"></a></h1>
			<ul class="gnb clear_fix">
				<c:forEach items="${sellerContext.sellerMenu.firstMenuList}" var="firstMenu">
					<c:choose>
						<c:when test="${firstMenu.menuId == sellerContext.sellerMenu.firstMenuId}">
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
				
					<c:forEach items="${sellerContext.sellerMenu.secondAndThirdMenuList}" var="secondMenu" varStatus="i">
						<c:set var="menuName">MENU_${secondMenu.menuId}</c:set>
						<c:if test="${secondMenu.displayFlag == 'Y'}">
						<li class="menu">
							<a class="on">${op:removeIframe(secondMenu.menuName)}</a>
							<ul class="depth2">
							
								<c:forEach items="${secondMenu.childMenu}" var="thirdMenu">
									<c:set var="menuName">MENU_${thirdMenu.menuId}</c:set>
									<c:if test="${thirdMenu.displayFlag == 'Y'}">
										<c:choose>
											<c:when test="${thirdMenu.menuCode == sellerContext.sellerMenu.menuCode}">
												<li class="sub_menu on"><a href="${op:url(thirdMenu.menuUrl)}" class="on">- ${op:removeIframe(thirdMenu.menuName)}</a></li>
											</c:when>
											<c:otherwise>
												<li class="sub_menu"><a href="${op:url(thirdMenu.menuUrl)}">- ${op:removeIframe(thirdMenu.menuName)}</a></li>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
		
							</ul>
						</li>
						</c:if>
					</c:forEach>					
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
			
			<div class="contents ">
				<div class="contents_inner">
		</c:if>
	

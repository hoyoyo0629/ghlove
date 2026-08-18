<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="seo" 	tagdir="/WEB-INF/tags/seo" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="naverPay"	tagdir="/WEB-INF/tags/naverPay" %>

<meta charset="utf-8" />
<meta name="_csrf" content="${op:removeIframe(_csrf.token)}"/>
<meta name="_csrf_header" content="${op:removeIframe(_csrf.headerName)}"/>

<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

<jsp:include page="../common/open-graph-tag.jsp"/>

<link rel="shortcut icon" href="/content/images/common/favicon.png" />
<link rel="icon" href="/content/images/common/favicon.ico" />
<link rel="apple-touch-icon" href="/content/images/common/favicon_ios.png" />


<meta http-equiv="X-UA-Compatible" content="IE=edge" />


<title><seo:pagination-title /><c:out value="${shopContext.seo.title}"/> </title>


<c:set var="noFollowFlag" value="N" />
<c:if test="${!empty pagination && pagination.currentPage > 1}">
	<c:set var="noFollowFlag" value="N" />
</c:if>
<c:if test="${!empty pagination && (fn:indexOf(pagination.link, 'itemsPerPage') > 1 || fn:indexOf(pagination.link, 'orderBy') > 1)}">
	<c:set var="noFollowFlag" value="Y" />
</c:if>

<c:if test="${shopContext.seo.indexFlag == 'N' || noFollowFlag == 'Y'}"><meta name="robots" content="noindex,noarchive"/></c:if>
<c:if test="${!empty shopContext.seo.keywords}"><meta name="keywords" content="${op:removeIframe(shopContext.seo.keywords)}" /></c:if>
<c:if test="${!empty shopContext.seo.description}"><meta name="description" content="<seo:pagination-title />${op:removeIframe(shopContext.seo.description)}" /></c:if>
<seo:pagination-link />

<c:if test="${!empty shopContext.alternateBaseUri}">
	<link rel="canonical" href="${op:property('saleson.url.shoppingmall')}${op:removeIframe(shopContext.alternateBaseUri eq '/' ? '' : shopContext.alternateBaseUri)}" />
</c:if>

<link rel="stylesheet" type="text/css" href="/content/css/base.css">
<link rel="stylesheet" type="text/css" href="/content/css/magnify.css">
<link rel="stylesheet" type="text/css" href="/content/css/common.css">
<link rel="stylesheet" type="text/css" href="/content/css/layout.css">
<link rel="stylesheet" type="text/css" href="/content/css/event.css">
<link rel="stylesheet" type="text/css" href="/content/css/category.css">
<link rel="stylesheet" type="text/css" href="/content/css/product.css">
<link rel="stylesheet" type="text/css" href="/content/css/main.css">
<link rel="stylesheet" type="text/css" href="/content/css/member.css">
<link rel="stylesheet" type="text/css" href="/content/css/mypage.css">
<link rel="stylesheet" type="text/css" href="/content/css/order.css">
<link rel="stylesheet" type="text/css" href="/content/css/intro.css"> 
<link rel="stylesheet" type="text/css" href="/content/css/popup.css">
<link rel="stylesheet" type="text/css" href="/content/css/jquery-ui-datepicker.css">

<script type="text/javascript" src="/content/js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="/content/js/jquery-ui.js"></script>
<script type="text/javascript" src="/content/js/jquery.bxslider.js"></script>
<script type="text/javascript" src="/content/js/jquery.sticky.js"></script>
<script type="text/javascript" src="/content/js/common.js"></script>

<script type="text/javascript" src="/content/js/lnb.js"></script>

<script type="text/javascript" src="/content/mobile/js/browserDetect.js"></script>
<script type="text/javascript" src="/content/js/jquery.magnify.js"></script>

<script type="text/javascript" src="/content/modules/jquery/jquery.cookie.js"></script>

<script type="text/javascript" src="/content/modules/spin.min.js"></script>
<script type="text/javascript" src="/content/modules/op.common.js"></script>
<script type="text/javascript" src="/content/modules/op.site.js"></script>

<jsp:include page="../common/google-analytics-script.jsp"/>
<script type="text/javascript" src="/content/modules/op.google.analytics.js"></script>

<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<![endif]--> 

<naverPay:wcslog-head />
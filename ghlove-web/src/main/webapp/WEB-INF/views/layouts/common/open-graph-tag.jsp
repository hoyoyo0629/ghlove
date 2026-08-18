<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ogTitle" value="SalesOn3"/>
<c:set var="ogType" value="website"/>
<c:set var="ogImage" value=""/>
<c:set var="ogDescription" value=""/>
<c:set var="ogUrl" value="${op:property('saleson.url.shoppingmall')}"/>

<c:if test="${not empty openGraphInfo}">
	<c:set var="ogTitle" value="${openGraphInfo.title}"/>
	<c:set var="ogType" value="${openGraphInfo.type}"/>
	<c:set var="ogImage" value="${openGraphInfo.image}"/>
	<c:set var="ogDescription" value="${openGraphInfo.description}"/>
	<c:set var="ogUrl" value="${openGraphInfo.url}"/>
</c:if>

<c:if test="${not empty ogTitle}">
	<meta property="og:title" content="${fn:escapeXml(ogTitle)}"/>
</c:if>
<c:if test="${not empty ogUrl}">
	<meta property="og:url" content="${fn:escapeXml(ogUrl)}"/>
</c:if>
<c:if test="${not empty ogType}">
	<meta property="og:type" content="${fn:escapeXml(ogType)}"/>
</c:if>
<c:if test="${not empty ogImage}">
	<meta property="og:image" content="${fn:escapeXml(ogImage)}"/>
</c:if>
<c:if test="${not empty ogDescription}">
	<meta property="og:description" content="${fn:escapeXml(ogDescription)}"/>
</c:if>

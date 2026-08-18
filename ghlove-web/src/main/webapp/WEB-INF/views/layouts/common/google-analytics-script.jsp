<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>

<c:out value="${not empty shopContext.commonTrackingScript ? shopContext.commonTrackingScript.fullScript : ''}"/>
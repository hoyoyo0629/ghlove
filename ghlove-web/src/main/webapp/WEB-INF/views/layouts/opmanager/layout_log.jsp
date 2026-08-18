<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<!doctype html>
<html lang="ko" class="opmanager">
<head>
<tiles:insertAttribute name="head"></tiles:insertAttribute>
</head>
<body>
<div class="popup_wrap" style="min-width:1080px;">
	<div id="pop_header">
		<h1 class="popup_title">변경내역조회</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>	
	<div class="popup_contents02">
		<tiles:insertAttribute name="content"></tiles:insertAttribute>		
	</div>
</div>
		
<tiles:insertAttribute name="common"></tiles:insertAttribute>
<tiles:insertAttribute name="script"></tiles:insertAttribute>
</body>
</html>
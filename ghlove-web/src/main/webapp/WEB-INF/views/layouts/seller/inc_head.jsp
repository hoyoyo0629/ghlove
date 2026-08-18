<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
	<meta charset="UTF-8" />
	<title>${op:removeIframe(shopContext.config.shopName) } 판매관리자</title>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
	<META http-equiv="Expires" content="-1">
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="No-Cache">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="robots" content="noindex"/>

	<meta name="_csrf" content="${op:removeIframe(_csrf.token)}"/>
	<meta name="_csrf_header" content="${op:removeIframe(_csrf.headerName)}"/>
	<meta name="referrer" content="strict-origin-when-cross-origin">

	<link rel="icon" href="/content/images/common/favicon.ico" />

	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" />" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager_print.css" />" media="print" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/modules/jquery/jstree/dist/themes/default/style.min.css" />" />

	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/fileUploadModal.css" />">

	<script type="text/javascript">var OP_CONTEXT_PATH = '${op:removeIframe(requestContext.contextPath)}';</script>
	<script type="text/javascript">var OP_LANGUAGE = '${op:removeIframe(requestContext.locale.language)}';</script>
	<script type="text/javascript">var OP_MANAGER_TIMEOUT = '${op:removeIframe(shopContext.managerTimeout)}';</script>
	<script type="text/javascript">var OP_MANAGER_TIMEOUT_TYPE = 'seller';</script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jquery-1.11.0.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jquery.cookie.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jquery-ui-1.10.4.custom.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/spin.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.common.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.validator.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.file.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.shop.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.manager.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.manager.order.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/css_browser_selector.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jstree/dist/jstree.js" />"></script>
	<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script> -->

	<script src="/content/modules/jquery/jstree/docs/assets/bootstrap/js/bootstrap.min.js"></script>
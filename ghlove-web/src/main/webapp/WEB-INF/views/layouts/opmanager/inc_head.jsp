<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
	<meta charset="UTF-8" />
	<title><c:out value="${shopContext.config.shopName}"/> 관리자</title>
	<meta name="_csrf" content="${fn:escapeXml(_csrf.token)}"/>
	<meta name="_csrf_header" content="${fn:escapeXml(_csrf.headerName)}"/>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="robots" content="noindex"/>
	<link rel="icon" href="/content/images/common/favicon.ico" />
	<META http-equiv="Expires" content="0">
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="No-Cache, no-store, must-revalidate">
	<!-- 2022.09.22 추가 -->
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager.css" />">
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager_print.css" />" media="print">
	<link rel="stylesheet" type="text/css" href="<c:url value="/content/modules/jquery/jstree/dist/themes/default/style.min.css" />">
	<!--// 2022.09.22 추가 -->

	<link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/fileUploadModal.css" />">

	<!--2022.09.20 추가 (2022.09.22 임시주석) -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/base.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/board.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/common.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/customer.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/etc.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/event.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/intro.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/item.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/jquery-ui-datepicker.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/jquery.bxslider.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/layout.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/mail_qna.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/main.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/member.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/mypage.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager.css" />"> -->

	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/order.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/popup.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap.css.map" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap.min.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap_front.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/ui-lightness/jquery-ui-1.10.3.custom.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" />"> -->

	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/common/css/font-awesome.min.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/common/css/jquery.treeview.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/common/css/reset.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/common/css/style.css" />"> -->
	<!--// 2022.09.20 추가 -->

	<!-- 2022.09.20 주석 처리 -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" />" /> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/bootstrap/css/bootstrap.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager.css" />"> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/opmanager_print.css" />" media="print" /> -->
	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/modules/jquery/jstree/dist/themes/default/style.min.css" />" /> -->
	<!--// 2022.09.20 주석 처리 -->

	<script type="text/javascript">var OP_CONTEXT_PATH = '${fn:escapeXml(requestContext.contextPath)}';</script>
	<script type="text/javascript">var OP_LANGUAGE = '${fn:escapeXml(requestContext.locale.language)}';</script>
	<script type="text/javascript">var OP_MANAGER_TIMEOUT = '${fn:escapeXml(shopContext.managerTimeout)}';</script>
	<script type="text/javascript">var OP_MANAGER_TIMEOUT_TYPE = 'manager';</script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jquery-1.11.0.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jquery.cookie.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jquery-ui-1.10.4.custom.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/spin.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.common.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.main.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.validator.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.file.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.shop.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.manager.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/op.manager.order.js" />"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/css_browser_selector.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/content/modules/jquery/jstree/dist/jstree.js" />"></script>
    <!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script> -->

    <script src="/content/modules/jquery/jstree/docs/assets/bootstrap/js/bootstrap.min.js"></script>
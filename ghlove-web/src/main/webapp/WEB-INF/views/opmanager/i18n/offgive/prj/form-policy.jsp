<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

<script src="${op:property('saleson.url.oz')}/oz80/ozhviewer/jquery-2.0.3.min.js"></script>
<link rel="stylesheet" href="${op:property('saleson.url.oz')}/oz80/ozhviewer/jquery-ui.css" type="text/css"/>
<script src="${op:property('saleson.url.oz')}/oz80/ozhviewer/jquery-ui.min.js"></script>

<link rel="stylesheet" href="${op:property('saleson.url.oz')}/oz80/ozhviewer/ui.dynatree.css" type="text/css"/>
<script type="text/javascript" src="${op:property('saleson.url.oz')}/oz80/ozhviewer/jquery.dynatree.js" charset="utf-8"></script>
<script type="text/javascript" src="${op:property('saleson.url.oz')}/oz80/ozhviewer/OZJSViewer.js" charset="utf-8"></script>

<div id="OZViewer" style="width:98%;height:98%"></div>
<script type="text/javascript" >
	function SetOZParamters_OZViewer(){
		var oz;
		oz = document.getElementById("OZViewer");
		oz.sendToActionScript("connection.servlet","${op:property('saleson.url.oz')}/oz80/server");
		oz.sendToActionScript("information.debug","true");

		// 1.cntr_deposit.ozr
		oz.sendToActionScript("connection.reportname","/5.cntr_deposit_prj.ozr");

// 		//2.cntr_application_form.ozr
// 		oz.sendToActionScript("connection.reportname","/2.cntr_application_form.ozr");
// 		oz.sendToActionScript("odi.odinames","2_cntr_application_form");

// 		oz.sendToActionScript("odi.2_cntr_application_form.pcount","1");
// 		oz.sendToActionScript("odi.2_cntr_application_form.args1","CNTR_SN=2022010111110000001");


		//3.cntr_receipt.ozr
//         oz.sendToActionScript("connection.reportname","/3.cntr_receipt.ozr");
//         oz.sendToActionScript("odi.odinames","3_cntr_receipt");

//         oz.sendToActionScript("connection.pcount","1");
//         oz.sendToActionScript("connection.args1","seal_url=");

//         oz.sendToActionScript("odi.3_cntr_receipt.pcount","1");
//         oz.sendToActionScript("odi.3_cntr_receipt.args1","CNTR_SN=2022010111110000001");


        // 4.cntr_report.ozr
//         oz.sendToActionScript("connection.reportname","/4.cntr_report.ozr");
//         oz.sendToActionScript("odi.odinames","4_cntr_report");

//         oz.sendToActionScript("odi.4_cntr_report.pcount","2");
//         oz.sendToActionScript("odi.4_cntr_report.args1","START_DT=20220101");
//         oz.sendToActionScript("odi.4_cntr_report.args2","END_DT=20221201");




		return true;
	}
	start_ozjs("OZViewer","${op:property('saleson.url.oz')}/oz80/ozhviewer/");
</script>
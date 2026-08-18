<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="No-Cache">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no" />
<title>Welcome to MagicLine4Web Page</title>

<script src="/oz80/ozhviewer/jquery-2.0.3.min.js"></script>
<link rel="stylesheet" href="/oz80/ozhviewer/jquery-ui.css" type="text/css"/>
<script src="/oz80/ozhviewer/jquery-ui.min.js"></script>

<link rel="stylesheet" href="/oz80/ozhviewer/ui.dynatree.css" type="text/css"/>
<script type="text/javascript" src="/oz80/ozhviewer/jquery.dynatree.js" charset="utf-8"></script>
<script type="text/javascript" src="/oz80/ozhviewer/OZJSViewer.js" charset="utf-8"></script>


<script type="text/javascript" >
	var json = {"Category":[{"CategoryID": 1,"CategoryName":"음료","Description": "청량음료, 커피, 홍차, 맥주"},{"CategoryID":2,"CategoryName":"조미료","Description":"감미료, 향신료, 양념, 스프레드"}], "Product":[{"ProductID":75,"ProductName":"알파인 맥주","CategoryID":1,"QuantityPerUnit":"24 - 0.5 l bottles","UnitPrice":24000.0000,"UnitsInStock":125},{"ProductID":39,"ProductName":"OK 바닐라 셰이크","CategoryID":1,"QuantityPerUnit":"750 cc per bottle","UnitPrice":28000.0000,"UnitsInStock":69},{"ProductID":34,"ProductName":"태일 라이트 맥주","CategoryID":1,"QuantityPerUnit": "24 - 12 oz bottles","UnitPrice":34000.0000,"UnitsInStock":111},{"ProductID":65,"ProductName":"루이지애나 특산 후추","CategoryID":2,"QuantityPerUnit":"32 - 8 oz bottles","UnitPrice":21000.0000,"UnitsInStock":76},{"ProductID":61,"ProductName":"사계절 핫 소스","CategoryID":2,"QuantityPerUnit":"24 - 500 ml bottles","UnitPrice":28000.0000,"UnitsInStock":92},{"ProductID":6,"ProductName":"대양 특선 블루베리 잼","CategoryID":2,"QuantityPerUnit": "12 - 8 oz jars","UnitPrice":25000.0000,"UnitsInStock":120}]};
	function SetOZParamters_OZViewer(){
		var oz;
		oz = document.getElementById("OZViewer");
		oz.sendToActionScript("connection.servlet","http://192.168.100.248/oz80/server");
		oz.sendToActionScript("connection.reportname","/jsonsample.ozr");
		oz.sendToActionScript("connection.pcount","2");
		oz.sendToActionScript("connection.args1","jsondata="+ JSON.stringify(json));
		oz.sendToActionScript("connection.args2","viewerType=HTML5 Canvas Viewer");
		oz.sendToActionScript("information.debug","true");
		return true;
	}
	start_ozjs("OZViewer","http://192.168.100.248/oz80/ozhviewer/");
</script>
</head>

<body style="width:98%;height:98%">
<div id="OZViewer" style="width:98%;height:98%"></div>
<a href="/oz80/sample/sample_canvas.html" target="_blank">oz viewer test</a>
<body>
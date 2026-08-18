<%
/**************************************************************************************************************************
* Program Name  : 실명확인 요청 Sample JSP (Real)
* File Name     : name_sample_seed.jsp
* Comment       :
* History       :
*
**************************************************************************************************************************/
%>
<%
    response.setHeader("Pragma", "no-cache" );
    response.setDateHeader("Expires", 0);
    response.setHeader("Pragma", "no-store");
    response.setHeader("Cache-Control", "no-cache" );
%>
<%@ page  contentType = "text/html;charset=utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.net.URLEncoder" %>
<%@ page import = "java.net.URLDecoder" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    String id       = "XTEE001"; // request.getParameter("id");                               // 실명확인 회원사 아이디

    String srvNo    = ""; // request.getParameter("srvNo");                            // 실명확인 서비스번호
    if (request.getRequestURL().indexOf("localhost") > -1) {
    	srvNo    = "002003";
    } else if (request.getRequestURL().indexOf("192.168.100.248") > -1) {
    	srvNo    = "004001";
    } else if (request.getRequestURL().indexOf("ilovegohyang.go.kr") > -1) {
    	srvNo    = "001001";
    } else {
    	srvNo    = "002003";
    }

    String reqNum   = "123456789"; // request.getParameter("reqNum");                       // 실명확인 요청번호  (sample 페이지와 result 페이지가  동일하지 않으면 결과페이지 복호화 시 에러)
	String jumin1   = request.getParameter("sci_jumin1");                           // 실명확인 주민번호1
	String jumin2   = request.getParameter("sci_jumin2");                           // 실명확인 주민번호2

	String name     = request.getParameter("sci_name");                             // 이름(siren서버는 euc-kr이기 때문에 같은환경이면 인코딩을 안해도 상관없음)
    name     = 	java.net.URLEncoder.encode(name,"UTF-8"); 	//utf-8환경인 경우에는 주석풀고 해당방식으로 요청해야함.(개발문서 참고!)

	String exVar    = "0000000000000000";                                       // 복호화용 임시필드
    String retUrl   = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/opmanager/offgive/sci-result"; //request.getParameter("retUrl");                           // 실명확인 결과수신 URL(추가 파라미터셋팅은 http://도메인?param=value1&param2=value2 식으로 하면 됨)
	String actionUrl = "https://name.siren24.com/servlet/name_check_seed"; //submit 할 URL(내국인)

    //01. 암호화 모듈 선언
	com.sci.v2.comm.secu.SciSecuManager seed  = new com.sci.v2.comm.secu.SciSecuManager();

	//02. 1차 암호화
	String encStr = "";
	String reqInfo      = id+"/"+jumin1+"/"+jumin2+"/"+name+"/"+reqNum+"/"+srvNo+"////"+exVar;  // 데이터 암호화

	encStr              = seed.getEnc(reqInfo, "");

	//03. 위변조 검증 값 생성
	com.sci.v2.comm.secu.hmac.SciHmac hmac = new com.sci.v2.comm.secu.hmac.SciHmac();
	String hmacMsg = hmac.HMacEncript(encStr);

	//03. 2차 암호화
	reqInfo  = seed.getEnc(encStr + "/" + hmacMsg + "/" + "0000000000000000", "");  //2차암호화

	if (jumin2 == null) {
		throw new NullPointerException("주민번호2의 정보가 없습니다.");
	}
	//주민번호2의 1번째자리로 외국인인 경우 체크 submit 할 URL 변경
	if("5".equals(jumin2.substring(0,1)) || "6".equals(jumin2.substring(0,1)) || "7".equals(jumin2.substring(0,1)) || "8".equals(jumin2.substring(0,1))){
		actionUrl = "https://name.siren24.com/servlet/foreign_name_check_seed";
	}
	
	request.setAttribute("actionUrl", actionUrl);
	request.setAttribute("reqInfo", reqInfo);
	request.setAttribute("retUrl", retUrl);
%>
<html>
<head>
<title>실명확인 서비스 Sample 화면</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
</head>

<body>

<!-- 실명확인서비스 요청 form --------------------------->
<form id="checkForm" name="checkForm" method="post" action="${fn:escapeXml(actionUrl)}">
    <input type="text" name="reqInfo"     value = "${fn:escapeXml(reqInfo)}">
    <input type="text" name="ok_url"      value = "${fn:escapeXml(retUrl)}">
<!--     <input type="submit" value="실명확인서비스 요청"> -->
</form>
<!--End 실명확인서비스 요청 form ----------------------->

<script type="text/javascript">
$(function() {
	$("#checkForm").submit();
});
</script>

</BODY>
</HTML>
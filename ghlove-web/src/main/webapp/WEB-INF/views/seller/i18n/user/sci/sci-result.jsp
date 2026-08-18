<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Logger"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
/**************************************************************************************************************************
* Program Name  : 실명확인 결과 수신 Sample JSP
* File Name     : name_result_seed.jsp
* Comment       :
* History       :  결과값 위변조 검증
*
**************************************************************************************************************************/
%>

<%@ page  contentType = "text/html;charset=utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.lang.RuntimeException" %>
<%
	final Logger LOG = Logger.getGlobal();
	LOG.setLevel(Level.SEVERE);
    // 변수 -------------------------------------------------------------------------------------------------------------
    String retInfo  = "";                                                                   // 결과정보

    String reqNum           = "";                                                           // 요청번호
    String jumin1           = "";                                                           // 주민번호1
	String jumin2           = "";                                                           // 주민번호2
    String name             = "";                                                           // 성명


    String result           = "";                                                           // 실명확인 결과값

    String discrHash        = "";                                                           // 중복가입확인정보
	String ciscrHash		= "";                                                           // CI연계값
	String ciVersion        = "";                                                           // CI버전
	String memId			= "";                                                           //
	String encKey		    = "";                                                           //
	String mmdd             = "";                                                           // mmdd

	String encPara	= "";
	String encMsg	= "";
	String msgChk       = "N";
	String addVar	="";
    //-----------------------------------------------------------------------------------------------------------------

    String cookiereqNum = "123456789"; //sample 페이지의 reqNum과 동일하지 않으면 결과페이지 복호화 시 에러

    try{

        // Parameter 수신 --------------------------------------------------------------------
        
        retInfo  = request.getParameter("retInfo");
        
    	if(retInfo == null){
            %>
        	alert("결과 정보가 없습니다.");
        	return;
        	<%
        	throw new NullPointerException("결과 정보가 없습니다.");
        }

		retInfo  = retInfo.trim();

        %>
<!--             [복호화 하기전 수신값] <br> -->
<!--             <br> -->
<%--             retInfo : <%=retInfo%> <br> --%>
<!--             <br> -->
		<script type="text/javascript">
        <%


        // 1. 암호화 모듈 (jar) Loading
        com.sci.v2.comm.secu.SciSecuManager sciSecuMg = new com.sci.v2.comm.secu.SciSecuManager();
        //쿠키에서 생성한 값을 Key로 생성 한다.
        retInfo  = sciSecuMg.getDec(retInfo, cookiereqNum);

        // 2.1차 파싱---------------------------------------------------------------
        int inf1 = retInfo.indexOf("/",0);
        int inf2 = retInfo.indexOf("/",inf1+1);

		encPara  = retInfo.substring(0,inf1);         //암호화된 통합 파라미터
        encMsg   = retInfo.substring(inf1+1,inf2);    //암호화된 통합 파라미터의 Hash값

		String  encMsg2   = sciSecuMg.getMsg(encPara);
		// 3.위/변조 검증 ---------------------------------------------------------------
        if(encMsg2.equals(encMsg)){
            msgChk="Y";
        }

		if(msgChk.equals("N")){
        %>
            alert("비정상적인 접근입니다.!!<%=msgChk%>");
        	return;
		<%
			return;
		}


        // 복호화 및 위/변조 검증 ---------------------------------------------------------------
		retInfo  = sciSecuMg.getDec(encPara, cookiereqNum);

        int info1 = retInfo.indexOf("/",0);
        int info2 = retInfo.indexOf("/",info1+1);
        int info3 = retInfo.indexOf("/",info2+1);
        int info4 = retInfo.indexOf("/",info3+1);
		int info5 = retInfo.indexOf("/",info4+1);
        int info6 = retInfo.indexOf("/",info5+1);
		int info7 = retInfo.indexOf("/",info6+1);
        int info8 = retInfo.indexOf("/",info7+1);
		int info9 = retInfo.indexOf("/",info8+1);
        int info10 = retInfo.indexOf("/",info9+1);

		reqNum		= retInfo.substring(0,info1);
        jumin1		= retInfo.substring(info1+1,info2);
		jumin2		= retInfo.substring(info2+1,info3);
        name        = retInfo.substring(info3+1,info4);
		name = java.net.URLDecoder.decode(name,"UTF-8"); //utf-8환경인 경우에는 주석풀고 해당방식으로받아야함.(개발문서 참고!)

        result      = retInfo.substring(info4+1,info5);
        discrHash   = retInfo.substring(info5+1,info6);
		ciscrHash   = retInfo.substring(info6+1,info7);
		ciVersion   = retInfo.substring(info7+1,info8);
		memId		= retInfo.substring(info8+1,info9);
		encKey		= retInfo.substring(info9+1,info10);
		mmdd        = retInfo.substring(info10+1,retInfo.length());

        discrHash  = sciSecuMg.getDec(discrHash, cookiereqNum);   //암호화된 중복가입확인정보 한번더 복호화
		ciscrHash  = sciSecuMg.getDec(ciscrHash, cookiereqNum);	  //암호화된 CI연계값 한번더 복호화

		request.setAttribute("result", result);
		request.setAttribute("ciscrHash", ciscrHash);
		request.setAttribute("discrHash", discrHash);
        %>
            

//     		1	일치	요청한 주민등록 번호와 실명확인 DB가 일치
//     		2	불일치	요청한 주민등록 번호와 설명이 실명확인 DB와 불일치
//     				식별이름이 한글이 아닌 경우 (예: 영문, 숫자, 특수문자등)
//     		3	없음	DB 미등록
//     		4	오류 	주민등록 번호조합이 맞지 않는 경우
//     				주민등록번호가 13자리가 아닌 경우
//     				주민등록번호가 숫자가 아닌 경우
//     		5	에러	SYSTEM 장애가 발생한 경우
//     		7	명의도용방지	개인의 siren명의도용방지 유료서비스가 설정된 경우
//     		9	인증시도초과	10번이상 인증했을 경우

			var message = "";
			if ("${fn:escapeXml(result)}" == "2") message = "불일치";
			if ("${fn:escapeXml(result)}" == "3") message = "없음";
			if ("${fn:escapeXml(result)}" == "4") message = "오류";
			if ("${fn:escapeXml(result)}" == "5") message = "에러";
			if ("${fn:escapeXml(result)}" == "7") message = "명의도용방지";
			if ("${fn:escapeXml(result)}" == "9") message = "인증시도초과";

            var sciUser = {
                "result" : "${fn:escapeXml(result)}",	// 1: 성공
                "message" : message,
                "ci" : "${fn:escapeXml(ciscrHash)}",
                "di" : "${fn:escapeXml(discrHash)}"
            };

            //console.log('sciUser', sciUser);

            parent.sciResponse(sciUser);
//             parent.opener.sciResponse(sciUser);
//             self.close();
			</script>
        <%
        // ----------------------------------------------------------------------------------
    } catch(RuntimeException ex){
		LOG.severe("sci-result.jsp :: 위변조 검증 에러");
		%>
		let sciUser = {
			"result" : "4",
			"message" : "문제가 발생했습니다.",
			"ci" : "",
			"di" : ""
		};
		parent.sciResponse(sciUser);
		<%
    }
%>
</script>
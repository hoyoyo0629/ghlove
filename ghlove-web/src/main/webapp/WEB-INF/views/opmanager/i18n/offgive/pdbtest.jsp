<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ page language="java" contenttype="text/html; charset=euc-kr" pageencoding="euc-kr"%> --%>
<%@ page import="com.privacy.pCrypto"%>
<%@page import="java.util.logging.Logger"%>
<%@ page import="java.util.logging.Level"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	pCrypto kc = new pCrypto();
	String _sLoadErr = "";
	String sString = "test1234#$%^&";
	long slong = 10000;
	String imgPath = "./img/";    // 이미지 경로
	String sOutput = "";
	String sOutput7 = "";
	String sOutputh = "";
	String sOutputh2 = "";
	String sDecoded =  "";
	String sDecoded7 =  "";
	String LibError =  "";
	String Error =  "";
	String JavaTest =  "";
	String LibTest =  "";

	final Logger LOG = Logger.getGlobal();
	LOG.setLevel(Level.SEVERE);
%>
<html>
<head>
<title>PrivacyDB Test Page</title>
<style type="text/css">
html,body {
	height: 100%;
	margin: 0;
	padding: 0;
	background: #f9f9f9;
}

body {
	color: #3c3c3c;
	font-family: "굴림", "arial", "helvetica";
}

.style7 {
	font-size: 16pt;
	font-weight: bold;
	font-family: Geneva, Arial, Helvetica, sans-serif;
}

.nbox {
	position: relative;
	width: 100%;
	height: 100%;
	padding: 0px;
	margin: 0px;
	background: #FFFFFF;
	border: 1px solid #c6c6c6;
}

.board {
	padding: 0px;
	border-top: 1pt solid #d1d1d1;
	border-right: 1pt solid #d1d1d1;
	height: 30px;
	text-align: left;
}

.board tr td {
	padding: 1px;
	border-bottom: 1pt solid #d1d1d1;
	border-left: 1pt solid #d1d1d1;
	height: 30px;
	color: #3c3c3c;
	font-size: 10pt;
	word-break: break-all;
}

;
.board thead tr {
	height: 31px;
}

</style>
</head>
<body>
<div style="position: absolute; left: 25px; top: 75px; width:51px; height:106px ; z-index:1" ><img src="<%=imgPath%>crpto-owl.png" /></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="nbox">
		<tr height="55px">
			<td colspan="3" style="background: #ff0000; valign: middle; align: left;">
				<img src="<%=imgPath%>top_logo.jpg" width="81px" height="43px" style="cursor:pointer" onclick="location.href='http://owlsystems.co.kr';"/></td>
			<td width="150px" style="background: #ff0000;"></td>
			<td style="width: 150px; background: #ff0000; align: right; valign: middle">
				<img src="<%=imgPath%>Privacy_logo.png" width="143px" height="43px"  style="cursor:pointer"  onclick="location.href='http://owlsystems.co.kr/products/owl_privacy.php';"/></td>
		</tr>
		<tr height="55px">
			<td  width="55px" style="background: #ff0000;" rowspan="2" ></td>
			<td align='left' colspan="3"><br>
			<span class="style7">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=imgPath%>dot.gif" width="20" height="21" />
					Welcome PrivacyDB Test - pdbtest.jsp<br><br>
			</span>
			</td>
		</tr>
		<!--탑메뉴-->

		<tr>
			<td width="50px">&nbsp;</td>
			<td align="left" valign="top" colspan="1">
				<%
					try {


						 JavaTest   = pCrypto.JavaTest();

						 LibTest   = pCrypto.LibTest();
						 //System.out.println("000000");
						 sOutput = pCrypto.Encrypt("normal", sString, "");
						 sOutput7 = pCrypto.Encrypt("pattern7", sString, "");
						 sDecoded = pCrypto.Decrypt("normal", sOutput, "", 0);
						 sDecoded7 = pCrypto.Decrypt("pattern7", sOutput7, "", 0);
					     sOutputh = pCrypto.Encrypt("hash", sString, "");
					     sOutputh2 = pCrypto.Encrypt("hash.5", sString, "");

				 	} catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
				 		//System.out.println("11111111");
				 		unsatisfiedlinkerror.printStackTrace();
				 		_sLoadErr = unsatisfiedlinkerror.toString();

				 	} catch (RuntimeException e) {
				 		//System.out.println("222222");
				 		e.printStackTrace();
				 		_sLoadErr = e.toString();
				 	}finally{
				 		try{
				 		 LibError   = pCrypto.LibError();
				 		 Error   = pCrypto.Error();
				 		} catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
				 			//System.out.println("3333333");
				 			unsatisfiedlinkerror.printStackTrace();
				 		}catch(RuntimeException ex){
				 			//System.out.println("44444");
				 			// ex.printStackTrace();
			 	          LOG.severe("pdbtest.jsp :: pCrypto Encrypt/Decrypt RuntimeException");
				 		}
				 	}
				%>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="board">
					<tbody>
						<tr>
							<td width="150px" bgcolor="f2f2f2">&nbsp;<font color=red>암호화(normal)</font></td>
							<td>&nbsp;<%=sString%> <b>&nbsp;→&nbsp;</b> <font color=red><%=sOutput%></font></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;<font color=blue>복호화(normal)</font></td>
							<td>&nbsp;<%=sOutput%> <b>&nbsp;→&nbsp;</b> <font color=blue><%=sDecoded%></font></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;<font color=red>암호화(pattern7)</font></td>
							<td>&nbsp;<%=sString%> <b>&nbsp;→&nbsp;</b> <font color=red><%=sOutput7%></font></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;<font color=blue>복호화(pattern7)</font></td>
							<td>&nbsp;<%=sOutput7%> <b>&nbsp;→&nbsp;</b> <font
								color=blue><%=sDecoded7%></font></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;<font color=red>암호화(hash)</font></td>
							<td>&nbsp;<%=sString%> <b>&nbsp;→&nbsp;</b> <font color=red><%=sOutputh %></font></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;<font color=red>암호화(hash.5)</font></td>
							<td>&nbsp;<%=sString%> <b>&nbsp;→&nbsp;</b> <font color=red><%=sOutputh2 %></font></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;LibError</td>
							<td>&nbsp;<%=LibError%></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;GetErrMsg</td>
							<td>&nbsp;<%=Error%></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;TEST VIEW(JAVA)</td>
							<td>&nbsp;<%=JavaTest%></td>
						</tr>
						<tr>
							<td bgcolor="f2f2f2">&nbsp;TEST VIEW(Lib)</td>
							<td>&nbsp;<%=LibTest%></td>
						</tr>
					</tbody>
				</table>
				<br>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="board">
					<tr>
						<td width="150px" bgcolor="f2f2f2">&nbsp;ERROR</td>
						<td>&nbsp;<%=_sLoadErr%></td>
					</tr>
				</table>
			</td>
			<td colspan="2" align="center" valign="bottom"><img src="<%=imgPath%>right_privacy.jpg" /></td>
		</tr>
		<tr>
			<td colspan="5"
				style="background-color: #d8d8d8; height: 25px; text-align: center; text-valign: bottom; font-size: 9pt;">
				OWLSystems, Inc. All rights reserved</td>
		</tr>
	</table>
</body>
</html>

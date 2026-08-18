<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.*" %>


<%
	LocalDate now = LocalDate.now();
	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	request.setAttribute("today", now.format(fmt));
%>

<div class="popup_wrap">
	<div class="popup_wrap">
		<div id="pop_header" style="text-align: center;">
			<c:set var="now" value="<%=new java.util.Date()%>" />
			<h1 class="popup_title">일일 콜 등록 ${fn:escapeXml(today)}</h1>
		</div>
		<table class="board_write_table" summary="콜 수 등록">
		<caption>콜 수 등록</caption>
		<colgroup>
			<col style="width: 100px" />
			<col />
		</colgroup>
		<tbody>
			<td class="label">대국민</td>
			<td><input type="number" id="kookmin"  style= "width:100px" /></td>
			<td class="label">지자체</td>
			<td><input type="number" id="lov"  style= "width:100px"  /></td>
			<td class="label">답례품제공자</td>
			<td><input type="number" id="giver"  style= "width:100px"  /></td>
			<td class="label">농협 관리자</td>
			<td><input type="number" id="nhbank"  style= "width:100px"  /></td>
			<td class="label">민간 플랫폼</td>
			<td><input type="number" id="platform"  style= "width:100px"  /></td>
		</tbody>

		</table>
	    <div class="btn_all btn_right" style="width: 100%">
	        <div class="flex_box" style="justify-content: center">
	        	<button type="button" class="btn btn-dark-gray btn-mini" onclick="regiCall()">콜 등록</button>
	        </div>
	    </div>
	</div>
</div>
<script type="text/javascript">

function nvl(val, defaultValue){
	let returnVal;
	if( val == "" || val == undefined) {
		returnVal = defaultValue;
		return returnVal;
	}
	return val;
}

function regiCall() {

	var callKookmin = nvl($('#kookmin').val(), 0);
	var callLov = nvl($('#lov').val(), 0);
	var callGiver = nvl($('#giver').val(), 0);
	var callNhbank = nvl($('#nhbank').val(), 0);
	var callPlatform = nvl($('#platform').val(), 0);



	$.post('/opmanager/popup/insertCall',
			{callKookmin,callLov,callGiver,callNhbank, callPlatform},function(resp){
				if(resp == undefined) {
					alert("등록에 실패 했습니다 다시 시도해 주세요");
				}

				var result =  confirm("등록에 성공했습니다.")
				if(result) {
					opener.parent.location.reload();
					self.close();

					}

			});


	}








</script>
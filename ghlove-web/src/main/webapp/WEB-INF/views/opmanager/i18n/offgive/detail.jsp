<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>기탁서</span></h3>

<form:form modelAttribute="offgive" method="post" action="/opmanager/offgive/create">
	<div class="board_write">
	    <table class="board_write_table" summary="">
	        <colgroup>
	            <col style="width:220px;">
	            <col>
	            <col style="width:220px;">
	            <col>
	        </colgroup>
	        <tbody>
	            <tr>
	                <td class="label">접수번호</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.cntrSn}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">신청일</td>
	                <td colspan="3">
	                    <div><c:out value="${fn:substring(offgive.frstRegistPnttm, 0, 19)}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">아이디</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.loginId}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">기부자명</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.userName}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">생년월일</td>
	                <td colspan="3">
	                    <div><c:out value="${op:date(offgive.birthday)}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">연락처</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.phoneNumber}"/></div>
	                </td>
	            </tr>
<!-- 	            <tr> -->
<!-- 	                <td class="label">행정정보 공동이용 동의</td> -->
<!-- 	                <td colspan="3"> -->
<%-- 	                    <div><c:out value="${offgive.infoAgreAt == 1 ? '동의' : '비동의'}"/></div> --%>
<!-- 	                </td> -->
<!-- 	            </tr> -->
	            <tr>
	                <td class="label" rowspan="2">주소</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.post}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="3">
	                    <div><c:out value="${offgive.address}"/> <c:out value="${offgive.addressDetail}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">기부 지자체</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.upperLocgovNm}"/> <c:out value="${offgive.locgovNm}"/> (<c:out value="${offgive.prjSubject}"/>)</div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">기부금액</td>
	                <td>
	                    <div><c:out value="${op:numberFormat(offgive.cntrAmt)}"/>원</div>
	                </td>
	                <td class="label">금년도 누적 기부액</td>
	                <td>
	                    <div><c:out value="${op:numberFormat(offgive.cntrAmtYearTotal)}"/>원</div>
	                </td>
	            </tr>
	            <c:if test="${not empty offgive.options}">
	            <tr>
	                <td class="label">선택 답례품</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.options}" escapeXml="false"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label" rowspan="2">배송지 주소</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.rcvPost}"/></div>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="3">
	                    <div><c:out value="${offgive.rcvAddress}"/> <c:out value="${offgive.rcvAddressDetail}"/></div>
	                </td>
	            </tr>
	            </c:if>
	            <tr>
	                <td class="label">접수은행</td>
	                <td colspan="3">
	                    <div><c:out value="${offgive.rceptBankCodeNm}"/> <c:out value="${offgive.rceptBankNm}"/></div>
	                </td>
	            </tr>
	             <tr>
	                 <td class="label">약관 동의</td>
	                 <td colspan="3">
	                     <div>동의함</div>
	                 </td>
	             </tr>
	            <tr>
	                <td class="label">전자납부번호</td>
	                <td colspan="3">
	                    <div class="tip"><c:out value="${offgive.elctrnPayNo}"/></div>
	                </td>
	            </tr>
	        </tbody>
	    </table>

	    <div class="btn_all btn_center">
	        <div class="flex_box gap-08">
<%-- 	        	<c:if test="${today == fn:replace(fn:substring(offgive.frstRegistPnttm, 0, 10), '-','')}"> --%>
<!-- 	            	<button type="button" class="btn btn-dark-gray btn-small" onClick="cancel()">등록 취소</button> -->
<%-- 	            </c:if> --%>
	            <button type="button" class="btn btn-dark-default btn-small" onClick="goList()">확인</button>
	            <button type="button" class="btn btn-dark-gray btn-small" onclick="window.open('/opmanager/offgive/popup/form-apply/${fn:escapeXml(offgive.cntrSn)}','pop','width=900,height=1200');">납부신청서 출력</button>
	            <c:if test="${today == fn:replace(fn:substring(offgive.frstRegistPnttm, 0, 10), '-','')}">
	            	<button type="button" class="btn btn-dark-default btn-small" id="btnReq" onClick="offgiveCancel()" style="background-color: #fb502e; color:white ">기부취소</button>
	            	<div class="tip" id="reqTipText" style="display:none; margin-top: 8px;">※ 해당 납부처리된 기부건을 취소하실경우 기부취소 신청을 요청하십시오.</div>
	            <%-- 	<button type="button" class="btn btn-dark-gray btn-small" id="btnReqCancel" onClick='cancelReq("${offgive.reqId}")' style="background-color: #0d2d4f;">신청취소</button> --%>
	            </c:if>
	        </div>
	    </div>
	</div>

</form:form>

<script type="text/javascript">

$(function() {
	if("${fn:escapeXml(offgive.cntrSttusCode)}" === "100"){
		$("#btnReq").hide();
		$("#btnReqCancel").hide();
	}else if("${fn:escapeXml(offgive.cntrSttusCode)}" === "200"){
		$("#reqTipText").show();
		if("${fn:escapeXml(offgive.reqId)}" != 0 ){
			$("#btnReq").hide();
			$("#btnReqCancel").hide();
			$("#reqTipText").hide();
		}else{
			$("#btnReqCancel").hide();
		}
	}

});

// 등록 취소
function cancel() {
	Common.confirm("${op:message('M01700')}", function() {
		$.post(url("/opmanager/offgive/delete/${fn:escapeXml(offgive.cntrSn)}"), {}, function(response) {
			if(response.isSuccess) {
				goList();
			} else {
				alert(response.errorMessage);
			}
	    });
	});
}

function goList() {
	/*
	if (document.referrer.indexOf("create") != -1) {
		location.href = "/opmanager/offgive/list"
	} else {
		history.back()
	}
	*/
	location.href = "/opmanager/offgive/list";
}

function offgiveCancel(){

	if("${fn:escapeXml(offgive.cntrBlcePoint)}" != "${fn:escapeXml(offgive.cntrPoint)}"){
		alert("기부포인트 사용이 확인되어 취소가 불가합니다.");
		return false;
	}
	if("${fn:escapeXml(offgive.cntrUsePoint)}" > 0){
		alert("기부포인트 사용이 확인되어 취소가 불가합니다.");
		return false;
	}
		var discription =  "(사용포인트 : "+ "${fn:escapeXml(offgive.cntrUsePoint)}" + " p)";
		var str = "${fn:escapeXml(offgive.frstRegistPnttm)}";
		var sttemntPayDe = str.substr(0, 10).replaceAll("-","");

		if(confirm("해당 기부 건을 취소를 요청하시겠습니까?")){
			var param = {
					"cntrSn" : "${fn:escapeXml(offgive.cntrSn)}",
					"cntrReqmngCode": "100",
					"loginId": "${fn:escapeXml(offgive.loginId)}",
					"userName": "${fn:escapeXml(offgive.userName)}",
					"locgovCode": "${fn:escapeXml(offgive.locgovCode)}",
					"sttemntPayDe": sttemntPayDe,
					"cntrAmt": "${fn:escapeXml(offgive.cntrAmt)}",
					"discription": discription
				};
			$.post("/opmanager/offgive/offgiveCancel", param, function(response) {
						if(response.isSuccess == true) {
							alert("요청되었습니다.");
							location.reload();
						} else {
							alert("오류가 발생했습니다.");
						}
				});
 	}else{
		 alert("요청이 취소 되었습니다.");
	}

}

/**
 *	함 수 명 : cancelReq
 *	기	능  : 변경신청 취소
 */
function cancelReq(reqId){

	if(confirm("요청된 기부취소신청을 취소하시겠습니까?")){
		var param = {
				"cntrReqmngCode": "100",
				cntrSn : "${fn:escapeXml(offgive.cntrSn)}",
				reqId : reqId
		};
	 	$.post('opmanager/offgive/offgiveCancelReq',param, function(response) {
	 		if(response.isSuccess == true) {
				alert("신청내역이 취소완료되었습니다.")
	 			location.reload();
	 		}else{
	 			alert("취소 중 오류가 발생하였습니다.")
	 		}
	 	});
	}
}

</script>

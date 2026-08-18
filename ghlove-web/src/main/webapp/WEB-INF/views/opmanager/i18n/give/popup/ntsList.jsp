<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">기부금 영수증 로그</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
	<div class="popup_contents">
		<table class="board_list_table" summary="기부금 영수증 로그">
        <caption>기부금 영수증 로그</caption>
        <colgroup>
            <col style="width:50px;">

            <col style="width:100px; white-space:nowrap">


        </colgroup>
        <thead>
            <tr>
            	<th scope="col">No.</th>
                <th scope="col">전자납부번호</th>
                <th scope="col">기부일</th>
                <th scope="col">기부금액</th>
                <th scope="col">영수증코드</th>
                <th scope="col">결과코드</th>
                <th scope="col">결과메시지</th>
                <th scope="col">시작일시</th>
                <th scope="col">종료일시</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="list" varStatus="i">
	            <tr style="background:#fff;">
	                <td><c:out value='${i.count}'/></td>
	                <td><c:out value='${list.elctrnPayNo}'/></td>
	                <td><c:out value='${list.sttemntPayDe}'/></td>
	                <td><c:out value='${list.cntrAmt}'/></td>
	                <td><c:out value='${list.elcrAplCd}'/></td>
	                <td><c:out value='${list.ntsResCode}'/></td>
	                <td><c:out value='${list.ntsResMssage}'/></td>
	                <td><c:out value='${list.frstRegistPnttm}'/></td>
	                <td><c:out value='${list.lastUpdtPnttm}'/></td>
	            </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty list}">
		<div class="no_content">
        	<c:out value="${op:message('M00473')}"/>
		</div>
	</c:if>
	</div>
</div>

<script type="text/javascript">
	
	$(function() {
	});
	
</script>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>

<div class="count_title mt20">
	<h5>
		<c:out value="${op:message('M00039')}"/> : <c:out value="${op:numberFormat(totalCount)}"/>건 │
		<c:out value="${op:message('M00271')}"/> : <c:out value="${op:numberFormat(pagination.totalItems)}"/>건
	</h5>	 
</div>
<table class="board_list_table">
	<colgroup>
		<col style="width:15%;">
		<col style="width:10%;">
		<col style="width:17%;">
		<col style="width:15%;">
		<col style="width:auto;">  
	</colgroup>
	<thead>
		<tr>
			<th>작성일</th>
			<th>작성자</th>
			<th>주문번호</th>
			<th>상태</th>
			<th>상담메모</th>
		</tr>
	</thead>  
	<tbody>
		<c:forEach items="${list}" var="memo">
			<tr> 
				<td><c:out value="${op:datetime(memo.createdDate)}"/></td>
				<td><c:out value="${memo.managerLoginId}"/></td>
				<td>
					<c:if test="${memo.orderCode != null}">
						<a onclick="goUrl('${fn:escapeXml(memo.orderCode)}')"><span><c:out value="${memo.orderCode}"/></span></a>
						<a class="btn btn-gradient btn-xs" href="/opmanager/order/new-order/order-detail/0/${fn:escapeXml(memo.orderCode)}" target="blank"><span></span>새창</button>
					</c:if>
				</td>
				<td><c:out value="${memo.claimStatusLabel}"/></td>
				<td class="text-left"><a href="javascript:Common.popup('/opmanager/user/popup/claim-update/${fn:escapeXml(memo.claimMemoId)}', 'claim_write', 500, 600, 1)"><c:out value="${op:nl2br(memo.memo)}"/></a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>


<div style="display: none;">
	<span id="today"><c:out value="${today}"/></span>
	<span id="week"><c:out value="${week}"/></span>
	<span id="month1"><c:out value="${month1}"/></span>
	<span id="month2"><c:out value="${month2}"/></span>
</div>

<c:if test="${empty list}">
<div class="no_content">
	<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. --> 
</div>
</c:if>	
<page:pagination-manager /> 
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>   <!-- 20260325 필수 추가정보 -->

<c:if test="${not empty orderLogs}">
	<div class="board_list">
		<h3 class="mt70"><span>주문 상태 변경 이력결제정보</span></h3>
		<table class="board_list_table">
			<colgroup>
	            <col style="width:50px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
			</colgroup>
			<thead>
				<tr>
					<th class="label">No</th>
					<th class="label">주문 상품</th>
					<th class="label">상태 변경</th>
					<th class="label">처리자</th>
					<th class="label">변경일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orderLogs}" var="log" varStatus="i">
					<tr>
						<td>
							<div>
								<c:out value="${i.count}"/>
							</div>
						</td>
						<td class="text-left">
							<div>
								<c:out value="${log.itemName}"/>
							</div>
							<div>
								<c:out value="${log.options}"/>
							</div>
							<div>						<!-- 20260325 필수 추가정보 -->
								${shop:viewItemTextOption(log.textOption)}
							</div>
						</td>
						<td>
							<div>
								<c:out value="${log.orgOrderStatus}"/> > <c:out value="${log.orderStatus}"/>
							</div>
						</td>
						<td>
							<div>
								<c:out value="${log.createdBy}"/>
							</div>
						</td>
						<td>
							<div>
								<c:out value="${log.createdAt}"/>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
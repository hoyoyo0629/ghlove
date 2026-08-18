<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>

<style type="text/css">
	td {
		padding-left: 5px;
	}
	.no_content {
		padding:10px;
	}
	.order_return_layer {
		display: none;
		position: fixed;
		z-index: 100000;
		width:850px;
		left: 50%;
		margin-left: -425px;
		top:0px;
		padding-bottom: 20px;
		background: #fff
	}
</style>

<c:if test='${mode != "popup"}'>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>
</c:if>


<div class="board_write">

	<div class="board_list">

		<h3 class="mt10">주문 정보</h3>
		<table class="board_write_table">
			<colgroup>
				<col style="width: 220px" />
				<col />
				<col style="width: 220px" />
				<col />
			</colgroup>
			<tbody>
			<tr>
				<td class="label">주문번호</td>
				<td><div><c:out value="${order.orderCode}"/></div></td>
				<td class="label">주문일시</td>
				<td><div><c:out value="${op:datetime(order.payDate)}"/></div></td>
			</tr>
				<tr>
					<td class="label">회원정보</td>
					<td>
						<div>
							<c:choose>
								<c:when test="${order.userId > 0}">
									<c:out value="${order.userName}"/> [<c:out value="${order.loginId}"/>]
									<!-- <a href="javascript:Manager.userDetails(${order.userId})" class="btn btn-gradient btn-xs">CRM</a> -->
								</c:when>
								<c:otherwise>비회원</c:otherwise>
							</c:choose>
						</div>
					</td>
					<td class="label">발송인</td>
					<td>
						<div><c:out value="${order.userName}"/></div>
					</td>
				</tr>
			<tr>
				<td class="label">구매자 휴대폰</td>
				<td>
					<div><c:out value="${order.mobile}"/></div>
				</td>
				<td class="label">구매자 이메일</td>
				<td>
					<div><c:out value="${order.email}"/></div>
				</td>
			</tr>

			</tbody>
		</table>
	</div>

	<div class="op-item-detail-area">
		<jsp:include page="../order/order-shipping-info.jsp"></jsp:include>
	</div>


</div>



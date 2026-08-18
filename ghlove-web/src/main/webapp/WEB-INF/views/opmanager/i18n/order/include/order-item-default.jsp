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

<table class="inner-table">
	<caption><c:out value="${op:message('M00059')}"/></caption>
	<!-- 주문정보 -->
	<colgroup>
		<col style="width: 80px" />
		<col />
		<col style="width: 120px" />
		<col style="width: 120px" />
			<col style="width: 80px" />
			<col style="width: 80px" />
			<col style="width: 120px" />
			<col style="width: 120px" />
			<col style="width: 100px" />
	</colgroup>
	<thead>
		<tr>
			<th scope="col" class="none_left">이미지</th>
			<th scope="col" class="none_left">상품정보</th>
			<th scope="col" class="none_left">구분</th>
			<th scope="col" class="none_left">판매가</th>
			<th scope="col" class="none_left">수량</th>
			<th scope="col" class="none_left">클레임수량</th>
			<th scope="col" class="none_left">총금액</th>
			<th scope="col" class="none_left">배송비</th>
			<th scope="col" class="none_left">상태</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${order.orderShippingInfos}" var="receiver" varStatus="receiverIndex">
			<c:forEach items="${receiver.orderItems}" var="orderItem" varStatus="orderItemIndex">
				<tr>
					<td>
						<img src="${fn:escapeXml(orderItem.imageSrc)}" alt="${fn:escapeXml(orderItem.itemName)}" width="100%"/>
					</td>
					<td>
						[<c:out value="${orderItem.itemUserCode}"/>] <c:out value="${orderItem.itemName}"/>
						<c:if test="${!empty orderItem.options}">
							<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
						</c:if>
						<c:out value="${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}"/>
					</td>
					<td class="text-center">
						<c:choose>
							<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
							<c:otherwise>
								<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.sellerName}"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="text-right">
						<c:out value="${op:numberFormat(orderItem.salePrice)}"/>원
					</td>
					<td class="text-right">
						<c:out value="${op:numberFormat(orderItem.quantity)}"/>개
					</td>
					<td class="text-right">
						<c:out value="${op:numberFormat(orderItem.claimQuantity)}"/>개
					</td>
					<td class="text-right">
						<c:out value="${op:numberFormat(orderItem.saleAmount)}"/>원
					</td>
					<td class="text-right">
						<c:choose>
 							<c:when test="${orderItem.isShippingView == 'Y'}">
 								<c:choose>
 									<c:when test="${orderItem.orderShipping.shippingPaymentType == '2'}">
 										<span style="color:red"><c:out value="${op:numberFormat(orderItem.orderShipping.realShipping)}"/>원 (착불)</span>
 									</c:when>
 									<c:otherwise>
 										<c:choose>
 											<c:when test="${orderItem.orderShipping.payShipping == 0}">무료</c:when>
 											<c:otherwise>
 												<c:out value="${op:numberFormat(orderItem.orderShipping.payShipping)}"/>원
 											</c:otherwise>
 										</c:choose>
 									</c:otherwise>
 								</c:choose>
 							</c:when>
 							<c:otherwise>묶음배송</c:otherwise>
 						</c:choose>
					</td>
					<td class="text-center">
						<c:out value="${orderItem.orderStatusLabel}"/>
					</td>
				</tr>
			</c:forEach>
		</c:forEach>
	</tbody>
</table>
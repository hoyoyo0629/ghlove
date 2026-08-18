<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${isSellerPage == false}">
	<div class="board_list">
		<h3 class="mt70">결제정보
			<a href="javascript:Manager.payChanges('${fn:escapeXml(pageType)}', '${fn:escapeXml(order.orderSequence)}', '${fn:escapeXml(order.orderCode)}')">
				<%--<button type="button" class="btn btn-dark-gray btn-sm"><span>결제정보변경</span></button>--%> <%--사용하지않음--%>
			</a>
		</h3>
		<table class="board_list_table">
			<caption>table list</caption>
            <colgroup>
                <col style="width:200px;">
                <col style="width:200px;">
                <col style="width:200px;">
                <col style="width:200px;">
            </colgroup>
			<thead>
				<tr>
					<th scope="col" class="none_left">판매가</th>
					<!-- <th scope="col" class="none_left">할인금액</th>
					<th scope="col" class="none_left">배송비</th> -->
					<th scope="col" class="none_left">결제포인트</th>
					<th scope="col" class="none_left">결제상태</th>
					<th scope="col" class="none_left">결제일</th>

					<!-- <c:if test='${order.payAmount > 0}'>
						<th scope="col" class="none_left" style="background: #ddd">결제완료금액</th>
					</c:if>
					<c:if test='${order.cancelAmount > 0}'>
						<th scope="col" class="none_left" style="background: #ddd">취소금액</th>
					</c:if>
					<c:if test='${order.postPayAmount > 0 && !order.allItemsCanceled}'>
						<th scope="col" class="none_left" style="background: #ddd">결제 예정금액</th>
					</c:if> -->

				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<div><c:out value="${op:numberFormat(order.totalItemAmount)}"/></div>
					</td>
					<!-- <td class="text-right"><c:out value="${op:numberFormat(order.totalDiscountAmount)}"/>원</td>
					<td class="text-right"><c:out value="${op:numberFormat(order.totalShippingAmount)}"/>원</td> -->
					<td>
						<div><c:out value="${op:numberFormat(order.totalItemAmount)}"/></div>
						<%-- <div><c:out value="${op:numberFormat(order.totalOrderAmount)}"/></div> --%>
					</td>
					<td>
						<div>
							<c:choose>
								<c:when test="${order.paymentType == '1'}">결제완료</c:when>
								<c:otherwise>결제취소</c:otherwise>
							</c:choose>
						</div>
					</td>
					<td>
						<div><c:out value="${op:datetime(order.payDate)}"/></div>
					</td>

					<!-- <c:if test='${order.payAmount > 0}'>
						<td class="text-right"><c:out value="${op:numberFormat(order.payAmount)}"/>원</td>
					</c:if>

					<c:if test='${order.cancelAmount > 0}'>
						<td class="text-right"><c:out value="${op:numberFormat(order.cancelAmount)}"/>원</td>
					</c:if>

					<c:if test='${order.postPayAmount > 0 && !order.allItemsCanceled}'>
						<td class="text-right"><span style="color:red"><c:out value="${op:numberFormat(order.postPayAmount)}"/>원</span></td>
					</c:if> -->
				</tr>
			</tbody>
		</table>

		<h3 class="mt30" hidden>결제내역</h3>
		<table class="board_list_table" hidden>
			<caption>결제내역</caption>
			<colgroup>
 				<col style="width: 150px" />
 				<col style="width: 150px" />
 				<col style="width: 100px" />
 				<col style="" />
 				<col style="width: 150px" />
			    </colgroup>
			<thead>
				<tr>
					<th scope="col" class="none_left">결제방식</th>
					<th scope="col" class="none_left">결제금액</th>
					<th scope="col" class="none_left">결제상태</th>
					<th scope="col" class="none_left">결제정보</th>
					<th scope="col" class="none_left">결제일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${order.orderPayments}" var="payment">
					<c:if test="${payment.amount > 0 || payment.cancelAmount > 0 || payment.remainingAmount > 0}">
						<c:set var="unit">원</c:set>

						<c:choose>
							<c:when test="${payment.approvalType == 'point'}">
								<c:set var="unit">P</c:set>
							</c:when>
						</c:choose>

						<tr>
							<td class="text-center">
                                <c:choose>
                                    <c:when test="${payment.refundFlag == 'Y'}">
                                        환불 (은행)
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${payment.approvalTypeLabel}"/>
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${order.escrowStatus == 'Y' && payment.approvalType != 'point'}">
									(에스크로)
								</c:if>
							</td>
							<td class="text-right">
								<c:choose>
									<c:when test="${payment.paymentType == '1'}">
										<c:out value="${op:numberFormat(payment.amount)}"/><c:out value="${unit}"/>
										<c:if test="${payment.amount != payment.remainingAmount && payment.remainingAmount > 0}">
											<br/><strong>(잔여액 : <c:out value="${op:numberFormat(payment.remainingAmount)}"/><c:out value="${unit}"/>)</strong>
										</c:if>
									</c:when>
									<c:otherwise><c:out value="${op:numberFormat(payment.cancelAmount)}"/><c:out value="${unit}"/></c:otherwise>
								</c:choose>

							</td>
							<td class="text-center">
								<c:choose>
                                    <c:when test="${payment.refundFlag == 'Y'}">환불완료</c:when>
									<c:when test="${empty payment.payDate}">미결</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${payment.paymentType == '1'}">결제완료</c:when>
											<c:otherwise>결제취소</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="text-center">
								<c:choose>
                                    <c:when test="${payment.refundFlag == 'Y'}">
                                        <c:out value="${payment.paymentSummary}"/>
                                    </c:when>
									<c:when test='${order.allItemsCanceled}'>
										<strong>입금전 주문취소</strong>
									</c:when>
									<c:otherwise>
										<c:out value="${payment.payInfo}"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="text-center">
								<c:out value="${op:datetime(payment.payDate)}"/>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
        <c:if test="${!empty cashbillIssues}">
            <h3 class="mt30">현금영수증 신청 내역</h3>
            <div>
                <table class="inner-table">
                    <caption>table list</caption>
                    <thead>
                    <tr>
                        <th scope="col" class="none_left">발행구분</th>
                        <th scope="col" class="none_left">과세구분</th>
                        <th scope="col" class="none_left">신청번호</th>
                        <th scope="col" class="none_left">신청자명</th>
                        <th scope="col" class="none_left">금액</th>
                        <th scope="col" class="none_left">상태</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${cashbillIssues}" var="cashbillIssue">
                        <tr>
                            <td class="text-center"><c:out value="${cashbillIssue.cashbill.cashbillType.title}"/></td>
                            <td class="text-center"><c:out value="${cashbillIssue.taxType.title}"/></td>
                            <td class="text-center"><c:out value="${cashbillIssue.cashbill.cashbillCode}"/></td>
                            <td class="text-center"><c:out value="${cashbillIssue.cashbill.customerName}"/></td>
                            <td class="text-center"><c:out value="${op:numberFormat(cashbillIssue.amount)}"/>원</td>
                            <td class="text-center"><c:out value="${cashbillIssue.cashbillStatus.title}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
	</div>
</c:if>

<h3 class="mt70"><span>배송 정보</span></h3>
<div>
	<div id="order-info-area">
		<c:set var="viewType" scope="request">info</c:set>
		<jsp:include page="../include/order-shipping-info.jsp" />
	</div>
</div>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="popup_wrap">
	<h1 class="popup_title">환불 금액 확인하기</h1> <!-- 결제정보 수정-->
	<div class="popup_contents">

		<form id="return-process-form" method="post">

			<div style="display:none">
				<input type="hidden" name="isError" value="${fn:escapeXml(isError)}" />
				<input type="hidden" name="errorMessage" value="${fn:escapeXml(errorMessage)}" />
				<input type="hidden" name="returnAmount" value="${fn:escapeXml(returnAmount)}" />
				<input type="hidden" name="orderCode" value="${fn:escapeXml(order.orderCode)}" />
				<input type="hidden" name="orderSequence" value="${fn:escapeXml(order.orderSequence)}" />
				<c:forEach items="${applys}" var="apply">
					<c:set var="orderItem" value="${apply.orderItem}" />
					<input type="hidden" name="returnIds" value="${fn:escapeXml(apply.claimCode)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].itemSequence" value="${fn:escapeXml(orderItem.itemSequence)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimCode" value="${fn:escapeXml(apply.claimCode)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].orderSequence" value="${fn:escapeXml(orderItem.orderSequence)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].orderCode" value="${fn:escapeXml(orderItem.orderCode)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].shipmentReturnSellerId" value="${fn:escapeXml(apply.shipmentReturnSellerId)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReason" value="${fn:escapeXml(apply.returnReason)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReasonText" value="${fn:escapeXml(apply.returnReasonText)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReasonDetail" value="${fn:escapeXml(apply.returnReasonDetail)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingCompanyName" value="${fn:escapeXml(apply.returnShippingCompanyName)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingNumber" value="${fn:escapeXml(apply.returnShippingNumber)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveSido" value="${fn:escapeXml(apply.returnReserveSido)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveSigungu" value="${fn:escapeXml(apply.returnReserveSigungu)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveEupmyeondong" value="${fn:escapeXml(apply.returnReserveEupmyeondong)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveName" value="${fn:escapeXml(apply.returnReserveName)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveZipcode" value="${fn:escapeXml(apply.returnReserveZipcode)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveAddress" value="${fn:escapeXml(apply.returnReserveAddress)}" /><br/>
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveAddress2" value="${fn:escapeXml(apply.returnReserveAddress2)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReservePhone" value="${fn:escapeXml(apply.returnReservePhone)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveMobile" value="${fn:escapeXml(apply.returnReserveMobile)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="${fn:escapeXml(apply.claimStatus)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnRefusalReasonText" value="${fn:escapeXml(apply.returnRefusalReasonText)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingAskType" value="${fn:escapeXml(apply.returnShippingAskType)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].userId" value="${fn:escapeXml(orderItem.userId)}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].escrowStatus" value="${fn:escapeXml(orderItem.escrowStatus)}" />
				</c:forEach>
			</div>

			<h3 class="mt10">환불 / 추가 내역</h3>
			<table class="inner-table">
				<caption>table list</caption>
				<colgroup>
					<col />
					<col style="width: 180px" />
					<col style="width: 180px" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col" class="none_left">내용</th>
						<th scope="col" class="none_left">구분</th>
						<th scope="col" class="none_left">금액</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>상품 환불 금액</td>
						<td class="text-center">환불</td>
						<td class="text-right"><c:out value="${op:numberFormat(itemReturnAmount)}"/>원</td>
					</tr>

					<c:set var="index">0</c:set>
					<c:forEach items="${addPayments}" var="item">
						<tr>
							<td><c:out value="${item.subject}"/></td>
							<td class="text-center">
								<c:choose>
									<c:when test="${item.addPaymentType == '1'}">추가</c:when>
									<c:otherwise>환불</c:otherwise>
								</c:choose>
							</td>
							<td class="text-right">
								<c:choose>
									<c:when test="${item.addPaymentType == '1'}">
										<c:choose>
											<c:when test="${returnAmount - addAmount <= 0}">
												<c:out value="${op:numberFormat(item.amount)}"/>원
											</c:when>
											<c:otherwise>

												<c:choose>
													<c:when test="${loginSellerId == item.sellerId}">
														<input type="text" class="_number_comma text-right add-amount" name="addPayments[${fn:escapeXml(index)}].amount" value="${fn:escapeXml(item.amount)}" />원
													</c:when>
													<c:otherwise>
														<c:out value="${item.amount}"/>
														<input type="hidden" name="addPayments[${fn:escapeXml(index)}].amount" value="${fn:escapeXml(item.amount)}" />
													</c:otherwise>
												</c:choose>

												<input type="hidden" name="addPayments[${fn:escapeXml(index)}].addPaymentType" value="${fn:escapeXml(item.addPaymentType)}" />
												<input type="hidden" name="addPayments[${fn:escapeXml(index)}].subject" value="${fn:escapeXml(item.subject)}" />
												<input type="hidden" name="addPayments[${fn:escapeXml(index)}].issueCode" value="${fn:escapeXml(item.issueCode)}" />
												<input type="hidden" name="addPayments[${fn:escapeXml(index)}].sellerId" value="${fn:escapeXml(item.sellerId)}" />

												<c:set var="index"><c:out value="${index + 1}"/></c:set>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<c:out value="${op:numberFormat(item.amount)}"/>원

										<input type="hidden" name="addPayments[${fn:escapeXml(index)}].amount" value="${fn:escapeXml(item.amount)}" />
										<input type="hidden" name="addPayments[${fn:escapeXml(index)}].addPaymentType" value="${fn:escapeXml(item.addPaymentType)}" />
										<input type="hidden" name="addPayments[${fn:escapeXml(index)}].subject" value="${fn:escapeXml(item.subject)}" />
										<input type="hidden" name="addPayments[${fn:escapeXml(index)}].issueCode" value="${fn:escapeXml(item.issueCode)}" />
										<input type="hidden" name="addPayments[${fn:escapeXml(index)}].sellerId" value="${fn:escapeXml(item.sellerId)}" />

										<c:set var="index"><c:out value="${index + 1}"/></c:set>

									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<h3 class="mt10">환불 정보</h3>
			<table class="inner-table">
				<caption>table list</caption>
				<thead>
					<tr>
						<th scope="col" class="none_left">환불금 총액</th>
						<th scope="col" class="none_left">추가금 총액</th>
						<th scope="col" class="none_left">고객 환급금</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="text-right"><c:out value="${op:numberFormat(returnAmount)}"/>원</td>
						<td class="text-right">
							<span id="totalAddAmount"><c:out value="${op:numberFormat(addAmount)}"/></span>원
							<c:if test="${returnAmount - addAmount <= 0}">
								<p>환불금액이 추가금보다 작습니다. 추가금은 별도 청구 바랍니다.</p>
							</c:if>
						</td>
						<td class="text-right">
							<c:choose>
								<c:when test="${returnAmount - addAmount <= 0}">
									<c:out value="${op:numberFormat(returnAmount)}"/>원
								</c:when>
								<c:otherwise>
									<span id="totalReturnAmount"><c:out value="${op:numberFormat(returnAmount - addAmount)}"/></span>원
									<c:if test="${addAmount > 0}">
									<p>
										<label>
											<input type="checkbox" name="separateCharges" value="1" onclick="setSeparateCharges()"
												data-return-amount="${fn:escapeXml(returnAmount)}" data-add-amount="${fn:escapeXml(addAmount)}" />추가금 별도 청구
										</label>
									</p>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="popup_btns">
				<button type="submit" class="btn btn-active">처리하기</button>
				<button type="button" class="btn btn-default" onclick="Shop.closeOrderLayer('return')">취소하기</button>
			</div>
		</form>
		<a href="javascript:Shop.closeOrderLayer('return')" class="popup_close">창 닫기</a>
	</div>
</div>
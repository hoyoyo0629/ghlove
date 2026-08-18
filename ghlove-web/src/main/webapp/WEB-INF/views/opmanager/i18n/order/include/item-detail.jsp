<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="board_list mt10">

	<h3 class="mt70">취소/반품/교환 신청</h3>

	<div class="border_box mb50">
		<form id="op-admin-claim-form" method="post" action="${fn:escapeXml(requestContext.sellerPage ? '/seller' : '/opmanager')}/order/${fn:escapeXml(pageType)}/admin-claim-apply">
			<input type="hidden" name="orderCode" value="${fn:escapeXml(order.orderCode)}">
			<input type="hidden" name="orderSequence" value="${fn:escapeXml(order.orderSequence)}">

            <div class="flex_box gap-12">
                <div class="input-form">
                    <input id="rdo1-1" name="claimType" type="radio" value="1">
                    <label for="rdo1-1">취소</label>
                </div>
                <div class="input-form">
                    <input id="rdo1-2" name="claimType" type="radio" value="2">
                    <label for="rdo1-2">반품</label>
                </div>
                <div class="input-form">
                    <input id="rdo1-3" name="claimType" type="radio" value="3">
                    <label for="rdo1-3">교환</label>
                </div>
            </div>

            <div class="board_list">
				<table class="board_list_table" id="op-claim-item-table" style="display:none">
					<caption>${op:message('M00059')}</caption>
					<!-- 주문정보 -->
					<colgroup>
						<col style="width: 50px" />
						<col style="width: 150px" />
						<col style="width: 500px" />
						<col style="width: 200px" />
		 				<col style="width: 200px" />
		 				<col style="width: 100px" />
		 				<col style="width: 200px" />
		 				<!-- <col style="width: 120px" /> -->
		 				<col style="width: 200px" />
					</colgroup>
					<thead>
						<tr>
							<th scope="col" class="none_left"><input type="checkbox" id="op-admin-claim-apply-all" title="체크박스" /></th>
							<th scope="col" class="none_left">이미지</th>
							<th scope="col" class="none_left">상품정보</th>
							<th scope="col" class="none_left">구분</th>
							<th scope="col" class="none_left">판매가</th>
							<th scope="col" class="none_left">수량</th>
							<th scope="col" class="none_left">총금액</th>
							<!-- <th scope="col" class="none_left">배송비</th> -->
							<th scope="col" class="none_left">상태</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${order.orderShippingInfos}" var="receiver" varStatus="receiverIndex">
							<c:forEach items="${receiver.orderItems}" var="orderItem" varStatus="orderItemIndex">
								<c:set var="itemKey" value="${orderItem.orderCode}-${orderItem.orderSequence}-${orderItem.itemSequence}" />
								<tr id="op-claim-item-${fn:escapeXml(itemKey)}" class="op-claim-item" style="display:none">
									<td>
										<input type="checkbox" name="adminClaimApplyKey" value="${fn:escapeXml(itemKey)}" data-order-status="${fn:escapeXml(orderItem.orderStatus)}" />
										<input type="hidden" name="itemMap[${fn:escapeXml(itemKey)}].itemSequence" value="${fn:escapeXml(orderItem.itemSequence)}" />
										<input type="hidden" name="itemMap[${fn:escapeXml(itemKey)}].key" value="${fn:escapeXml(itemKey)}" />
									</td>
									<td>
										<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(orderItem.itemUserCode)}" target="_blank">
											<img src="${shop:loadImageBySrc(orderItem.imageSrc, 'XS')}" alt="${fn:escapeXml(orderItem.itemName)}" class="item_image"/>
										</a>
									</td>
									<td class="left break-word">
										[${fn:escapeXml(orderItem.itemUserCode)}] <br> ${fn:escapeXml(orderItem.itemName)}
										<c:if test="${!empty orderItem.options}">
											<p>${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}</p>
										</c:if>
										${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}
										<c:if test="${not empty orderItem.textOption}">			<!-- 20260325 필수 추가정보 -->
											${shop:viewItemTextOption(orderItem.textOption)}
										</c:if>

									</td>
									<td class="text-center">
										<c:choose>
											<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
											<c:otherwise>
												<span class="glyphicon glyphicon-user"></span>${fn:escapeXml(orderItem.sellerName)}
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										${op:numberFormat(orderItem.salePrice)}P
									</td>
									<td>
										<select name="itemMap[${fn:escapeXml(itemKey)}].quantity">
					 						<c:forEach begin="1" end="${orderItem.quantity - orderItem.claimApplyQuantity}" step="1" var="quantity">
					 							<option value="${fn:escapeXml(quantity)}" ${op:selected(quantity, orderItem.quantity - orderItem.claimApplyQuantity)}>${fn:escapeXml(quantity)}개</option>
					 						</c:forEach>
				 						</select>
									</td>
									<td>
										${op:numberFormat(orderItem.saleAmount)}P
									</td>
									<!-- <td class="text-right">
										<c:choose>
				 							<c:when test="${orderItem.isShippingView == 'Y'}">
				 								<c:choose>
				 									<c:when test="${orderItem.orderShipping.shippingPaymentType == '2'}">
				 										<span style="color:red">${op:numberFormat(orderItem.orderShipping.realShipping)}원 (착불)</span>
				 									</c:when>
				 									<c:otherwise>
				 										<c:choose>
				 											<c:when test="${orderItem.orderShipping.payShipping == 0}">무료</c:when>
				 											<c:otherwise>
				 												${op:numberFormat(orderItem.orderShipping.payShipping)}원
				 											</c:otherwise>
				 										</c:choose>
				 									</c:otherwise>
				 								</c:choose>
				 							</c:when>
				 							<c:otherwise>묶음배송</c:otherwise>
				 						</c:choose>
									</td> -->
									<td class="text-center">
										${fn:escapeXml(orderItem.orderStatusLabel)}
									</td>
								</tr>
							</c:forEach>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div class="board_write" id="reason_input_area" style="display:none">
				<table class="board_write_table">
                    <colgroup>
                        <col style="width: 20px" />
                        <col>
                    </colgroup>
					<tbody>
						<tr class="op-claim-type-cancel op-claim-type-info">
							<td class="label" colspan="2">취소 사유</td>
							<td colspan="7">
								<div class="flex_box gap-08">
									<select name="cancelClaimReason">
										<c:forEach var="code" items="${cancelClaimReasons}" varStatus="i">
											<option value="${fn:escapeXml(code.detail)}">${fn:escapeXml(code.label)}</option>
										</c:forEach>
									</select>

			 						<input type="hidden" name="cancelClaimReasonText"  />
		                            <input type="text" name="cancelClaimReasonDetail" maxlength="80" style="width:70%" />
	                            </div>
							</td>
						</tr>

						<tr class="op-claim-type-cancel op-claim-type-info hide">
							<td class="label" colspan="2">처리구분</td>
							<td colspan="7">
								<div class="flex_box gap-08">
									<label><input type="checkbox" name="refundFlag" value="Y" /> 선택한 주문을 환불내역(신청 상태)으로 보냅니다.</label>
								</div>
							</td>
						</tr>

						<tr class="op-claim-type-return op-claim-type-info">
							<td class="label" colspan="2">반품 사유</td>
							<td colspan="7">
								<div class="flex_box gap-08">
									<select name="returnClaimReason">
										<c:forEach var="code" items="${returnClaimReasons}" varStatus="i">
											<option value="${fn:escapeXml(code.detail)}">${fn:escapeXml(code.label)}</option>
										</c:forEach>
									</select>

			 						<input type="hidden" name="returnClaimReasonText"  />
			 						<input type="text" name="returnClaimReasonDetail" maxlength="80" style="width:70%" />
			 					</div>
							</td>
						</tr>

						<tr class="op-claim-type-exchange op-claim-type-info">
							<td class="label" colspan="2">교환 사유</td>
							<td colspan="7">
								<div class="flex_box gap-08">
									<select name="exchangeClaimReason">
										<c:forEach var="code" items="${exchangeClaimReasons}" varStatus="i">
											<option value="${fn:escapeXml(code.detail)}">${fn:escapeXml(code.label)}</option>
										</c:forEach>
									</select>

			 						<input type="hidden" name="exchangeClaimReasonText"  />
			 						<input type="text" name="exchangeClaimReasonDetail" maxlength="80" style="width:70%" />
			 					</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
            <div class="btn_all btn_center" id="reason_btn_area" style="display:none">
                <div class="flex_box gap-08">
                   <button type="submit" class="btn btn-dark-gray btn-small">저장</button>
                </div>
            </div>
		</form>

		<div class="op-claim-message" style="padding: 10px 20px; background: #fcf8e8; margin-top: 10px;">
			신청 항목을 선택해 주세요.
		</div>
	</div>
</div>

<div class="jq_tabonoff comm_tab1">
	<ul class="jq_tab tabs">
		<li ${viewTabIndex == '0' ? 'class="active"' : ''}><a href="javascript:;">일반</a></li>
		<li ${viewTabIndex == '1' ? 'class="active"' : ''}>
			<a href="javascript:;">취소
				<c:if test="${fn:length(activeCancels) > 0}">(${op:numberFormat(fn:length(activeCancels))})</c:if>
			</a>
		</li>
		<li ${viewTabIndex == '2' ? 'class="active"' : ''}>
			<a href="javascript:;">반품
				<c:if test="${fn:length(activeReturns) > 0}">(${op:numberFormat(fn:length(activeReturns))})</c:if>
			</a>
		</li>
		<li ${viewTabIndex == '3' ? 'class="active"' : ''}>
			<a href="javascript:;">교환
				<c:if test="${fn:length(activeExchanges) > 0}">(${op:numberFormat(fn:length(activeExchanges))})</c:if>
			</a>
		</li>
<%-- 		<c:if test="${requestContext.opmanagerPage}"> --%>
			<li ${viewTabIndex == '4' ? 'class="active"' : ''}>
				<a href="javascript:;">이력</a>
			</li>
<%-- 		</c:if> --%>
		<!--
		<li ${viewTabIndex == '5' ? 'class="on"' : ''}>
			<a href="javascript:;" class="tit">사은품</a>
		</li>
		-->
	</ul>
	<div class="jq_cont tab_container" >
		<!-- //탭1 -->
		<div class="tab_content" style="${viewTabIndex.equals('0') ? 'display:block' : 'display:none'}">
			<jsp:include page="../include/order-default-info.jsp"></jsp:include>
		</div>

		<div class="tab_content" id="order-cancel-form" style="${viewTabIndex.equals('1') ? 'display:block' : 'display:none'}">
			<jsp:include page="../include/order-item-cancel.jsp"></jsp:include>
		</div>

		<div class="tab_content" id="order-return-form" style="${viewTabIndex.equals('2') ? 'display:block' : 'display:none'}">
			<jsp:include page="../include/order-item-return.jsp"></jsp:include>
		</div>

		<div class="tab_content" id="order-exchange-form" style="${viewTabIndex.equals('3') ? 'display:block' : 'display:none'}">
			<jsp:include page="../include/order-item-exchange.jsp"></jsp:include>
		</div>

		<div class="tab_content" id="order-log" style="${viewTabIndex.equals('4') ? 'display:block' : 'display:none'}">
			<jsp:include page="../include/order-log.jsp"></jsp:include>
		</div>
		<!--
		<div class="tab_content" id="order-gift-item" ${viewTabIndex == '5' ? '' : 'style="display:none"'}>
			<jsp:include page="../include/order-gift-item.jsp"></jsp:include>
		</div>
		-->
	</div>
</div>
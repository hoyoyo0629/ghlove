<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>

<c:forEach items="${order.orderShippingInfos}" var="receiver" varStatus="receiverIndex">
	<input type="hidden" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].shippingInfoSequence" value="${receiver.shippingInfoSequence}" />
	<table class="board_write_table" ${viewType == 'info' ? 'style=""' : ''}>
		<caption>주문상품정보 - 주문자정보</caption>
		<colgroup>
			<col style="width:180px;">						<!-- 20260325 필수 추가정보 40px감소 -->
			<col>
		</colgroup>
		<tbody>
		<tr>
			<td class="label" scope="row">배송정보</td>
			<td>
				<div>
					<c:choose>
						<c:when test="${viewType == 'info'}">
							<ul style="margin-bottom:0px;">
								<c:choose>
									<%--<c:when test="${receiver.existsMobileItem == 'Y' || receiver.existsNewOrderItem != 'Y'}">--%>
									<c:when test="${receiver.existsNewOrderItem != 'Y'}">
										<li><c:out value="${receiver.receiveName}"/></li>
										<li>
											<c:choose>
												<c:when test="${!empty receiver.receivePhone}">
													<c:out value="${receiver.receivePhone}"/> / <c:out value="${receiver.receiveMobile}"/>
												</c:when>
												<c:otherwise>
													<c:out value="${receiver.receiveMobile}"/>
												</c:otherwise>
											</c:choose>
										</li>
										<li>
											<c:if test="${order.mobileItemYn != 'Y'}">
												[<c:out value="${receiver.receiveNewZipcode}"/>]&nbsp;<span>&nbsp;<c:out value="${receiver.receiveAddress}"/>&nbsp;</span>&nbsp;<span><c:out value="${receiver.receiveAddressDetail}"/></span>
											</c:if>
										</li>
									</c:when>
									<c:otherwise>
										<li>배송/발송준비중 단계부터 배송정보가 노출됩니다.</li>
									</c:otherwise>
								</c:choose>
							</ul>
						</c:when>
						<c:otherwise>
							<table class="board_write_table">
								<colgroup>
									<col style="width: 10%;" />
									<col />
									<col style="width: 10%;" />
									<col />
								</colgroup>
								<tbody>
									<tr>
										<th class="label">받는사람</th>
										<td colspan="3">
											<div>
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveName" class="required" maxlength="50" title="받는사람" value="${receiver.receiveName}" />
											</div>
										</td>
									</tr>
									<tr>
										<th class="label">전화번호</th>
										<td>
											<div>
												<select name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receivePhone1" title="전화번호" class="">
													<option value="">-선택-</option>
													<c:forEach items="${op:getCodeInfoList('TEL')}" var="tel">
														<option value="${fn:escapeXml(tel.key.id)}" ${op:selected(tel.key.id, receiver.receivePhone1)}><c:out value="${fn:escapeXml(tel.label)}"/></option>
													</c:forEach>
												</select> -
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receivePhone2"  class="_number" maxlength="4" title="전화번호" value="${fn:escapeXml(receiver.receivePhone2)}" /> -
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receivePhone3"  class="_number" maxlength="4" title="전화번호" value="${fn:escapeXml(receiver.receivePhone3)}" />
											</div>
										</td>
										<th class="label">핸드폰번호</th>
										<td>
											<div>
												<select name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveMobile1" title="핸드폰번호" class="_number required">
													<option value="">-선택-</option>
													<c:forEach items="${op:getCodeInfoList('PHONE')}" var="tel">
														<option value="${fn:escapeXml(tel.key.id)}" ${op:selected(tel.key.id, receiver.receiveMobile1)}><c:out value="${fn:escapeXml(tel.label)}"/></option>
													</c:forEach>
												</select> -
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveMobile2"  class="_number required" maxlength="4" title="핸드폰번호" value="${fn:escapeXml(receiver.receiveMobile2)}" /> -
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveMobile3"  class="_number required" maxlength="4" title="핸드폰번호" value="${fn:escapeXml(receiver.receiveMobile3)}" />
											</div>
										</td>
									</tr>
									<tr>
										<th class="label">주소</th>
										<td colspan="3">
											<div>
												<input type="hidden" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveNewZipcode" value="${fn:escapeXml(receiver.receiveNewZipcode)}" />
												<input type="hidden" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveSido" value="${fn:escapeXml(receiver.receiveSido)}" />
												<input type="hidden" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveSigungu" value="${fn:escapeXml(receiver.receiveSigungu)}" />
												<input type="hidden" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveEupmyeondong" value="${fn:escapeXml(receiver.receiveEupmyeondong)}" />

												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveZipcode" title="우편번호" maxlength="7" class="required" readonly="readonly" value="${fn:escapeXml(receiver.receiveZipcode)}" />
												<a href="javascript:;" onclick="openDaumPostcode('${fn:escapeXml(receiverIndex.index)}')" class="btn btn-gradient btn-xs">우편번호</a><br/>
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveAddress" title="주소" maxlength="7" style="width:100%" class="required" readonly="readonly" value="${fn:escapeXml(receiver.receiveAddress)}" /><br/>
												<input type="text" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].receiveAddressDetail"  style="width:100%" maxlength="100" title="상세주소" value="${fn:escapeXml(receiver.receiveAddressDetail)}" />
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</c:otherwise>
					</c:choose>
				</div>
			</td>
		</tr>


		<c:choose>
			<c:when test="${viewType == 'info'}">
				<c:if test="${!empty receiver.memo}">
					<tr>
						<td class="label" scope="row">배송시(발송) 요구사항</td>
						<td>
							<div>
								<c:out value="${receiver.memo}"/>
							</div>
						</td>
					</tr>
				</c:if>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="label" scope="row">배송시(발송) 요구사항</td>
					<td>
						<div>
							<textarea name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].memo" maxlength="200" class="" title="배송시(발송) 요구사항"><c:out value="${fn:escapeXml(receiver.memo)}"/></textarea>
						</div>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>


		<tr>
			<td class="label" scope="row">상품정보</td>
			<td>
				<div>
					<table class="board_list_table">
						<caption><c:out value="${op:message('M00059')}"/></caption>
						<!-- 주문정보 -->
						<colgroup>
	                        <col style="width:150px;">
	                        <col style="width:320px;">							<!-- 20260325 필수 추가정보 40px증가 -->
	                        <col style="width:160px;">
	                        <col style="width:160px;">
	                        <col style="width:100px;">
	                        <col style="width:200px;">
	                        <col style="width:200px;">
	                        <col style="width:150px;">
						</colgroup>
						<thead>
						<tr>
							<th scope="col" class="none_left">이미지</th>
							<th scope="col" class="none_left">답례품정보</th>
							<th scope="col" class="none_left">판매자</th>
							<th scope="col" class="none_left">대표번호</th>
							<th scope="col" class="none_left">수량</th>
							<th scope="col" class="none_left">판매가</th>
							<!-- <th scope="col" class="none_left">할인금액</th> -->
							<th scope="col" class="none_left">결제포인트</th>
							<!-- <th scope="col" class="none_left">배송비</th> -->
							<th scope="col" class="none_left">상태</th>
						</tr>
						</thead>
						<tbody id="order_items">
						<c:forEach items="${receiver.orderItems}" var="orderItem" varStatus="orderItemIndex">
							<tr>
								<td>
									<c:set var="imageNameSplit" value="${fn:split(orderItem.imageSrc, '/')}" />
									<c:set var="imageName" value="${imageName[fn:length(imageNameSplit)]}" />
									<div>
										<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(orderItem.itemUserCode)}" target="_blank">
											<img src="${shop:loadImageBySrc(orderItem.imageSrc,'XS')}" alt="${fn:escapeXml(orderItem.itemName)}" class="item_image"/>
										</a>
									</div>
								</td>
								<td class="text-left">
									<div>
										<div>[<c:out value="${orderItem.itemUserCode}"/>]</div>
										<div><c:out value="${orderItem.itemName}"/></div>
										<c:if test="${!empty orderItem.options}">
											<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
										</c:if>

										<c:out value="${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}"/>

										<c:if test="${not empty orderItem.textOption}">			<!-- 20260325 필수 추가정보 -->
											${shop:viewItemTextOption(orderItem.textOption)}
										</c:if>

										<c:if test="${orderItem.shippingDate != '00000000000000'}">
											<div>

												<c:choose>
													<c:when test="${empty orderItem.deliveryNumber}">
														<%-- 직접수령 (${op:date(orderItem.shippingDate)}) --%>
														<c:if test="${orderItem.campaignCode == 'MOBILE'}">
															모바일 번호 : <c:out value="${empty orderItem.mobileNumber ? '없음' : orderItem.mobileNumber}"></c:out>
														</c:if>
														<c:if test="${orderItem.campaignCode != 'MOBILE'}">
															<c:out value="${empty orderItem.deliveryCompanyName ? '택배사 정보 없음' : orderItem.deliveryCompanyName}"></c:out> [송장정보 없음]
														</c:if>
													</c:when>
													<c:otherwise>
														<a href="${orderItem.deliveryShippingUrl.replaceAll('-', '')}" target="_blank" class="break-word bg-info">배송추적</a>
														<c:out value="${orderItem.deliveryCompanyName}"/> [<c:out value="${orderItem.deliveryNumber}"/>] (<c:out value="${op:date(orderItem.shippingDate)}"/>)
													</c:otherwise>
												</c:choose>
											</div>
										</c:if>
									</div>
								</td>
								<td class="text-center">
									<c:choose>
										<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
										<c:otherwise>
											<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.sellerName}"/>
										</c:otherwise>
									</c:choose>
								</td>
								<td class="text-center">
									<c:out value="${orderItem.telephoneNumber}"/>
								</td>
								<td class="text-center">
									<c:out value="${op:numberFormat(orderItem.quantity)}"/>개
								</td>
								<td class="text-center">
									<c:out value="${op:numberFormat(orderItem.itemAmount)}"/>
								</td>
								<!-- <td class="text-right">
									<c:choose>
										<c:when test="${orderItem.discountAmount > 0}">
											<a href="#" class="op-discount-details-button">-<c:out value="${op:numberFormat(orderItem.discountAmount)}"/>원</a>

											<div class="op-discount-details" style="display: none; position: absolute; border: 1px solid #ccc; background: #fbfbfb; font-size: 11px">
												<c:if test="${orderItem.itemDiscountAmount > 0}">
													상품할인 : <span style="display:inline-block; width:70px"><c:out value="${op:numberFormat(orderItem.itemDiscountAmount)}"/>원</span><br />
												</c:if>
												<c:if test="${orderItem.couponDiscountAmount > 0}">
													쿠폰할인 : <span style="display:inline-block; width:70px"><c:out value="${op:numberFormat(orderItem.couponDiscountAmount)}"/>원</span><br />
												</c:if>
												<c:if test="${orderItem.userLevelDiscountAmount > 0}">
													회원할인 : <span style="display:inline-block; width:70px"><c:out value="${op:numberFormat(orderItem.userLevelDiscountAmount)}"/>원</span><br />
												</c:if>
												<c:if test="${orderItem.setDiscountAmount > 0}">
													세트할인 : <span style="display:inline-block; width:70px"><c:out value="${op:numberFormat(orderItem.setDiscountAmount)}"/>원</span>
												</c:if>
											</div>
										</c:when>
										<c:otherwise>
											0원
										</c:otherwise>
									</c:choose>
								</td> -->
								<td class="text-center">
									<c:out value="${op:numberFormat(orderItem.saleAmount)}"/>
								</td>
								<!-- <td class="text-right">
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
								</td> -->
								<td class="text-center">
									<c:choose>
										<c:when test="${orderItem.mobileItemYn == 'Y' && orderItem.orderStatus == '20' }">
											발송준비중
										</c:when>
										<c:when test="${orderItem.mobileItemYn == 'Y' && orderItem.orderStatus == '35' }">
											발송완료
										</c:when>
										<c:otherwise>
											<c:out value="${orderItem.orderStatusLabel}"/>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</td>
		</tr>
		</tbody>
	</table>
</c:forEach>
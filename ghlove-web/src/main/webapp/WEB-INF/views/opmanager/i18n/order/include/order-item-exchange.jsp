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


						<c:if test="${not empty exchangeHistorys}">
                            <h3 class="mt70"><span>교환 내역 (완료, 발송)</span></h3>
                            <div class="board_list">
                                <table class="board_list_table">
                                    <colgroup>
                                        <col style="width:400px;">
                                        <col style="width:200px;">
                                        <col style="width:100px;">
                                        <col style="width:150px;">
                                        <col style="width:200px;">
                                        <col style="width:100px;">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>답례품정보</th>
                                            <th>상호명</th>
                                            <th>수량</th>
                                            <th>상태</th>
                                            <th>교환 신청일</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
										<c:forEach items="${exchangeHistorys}" var="history">
	                                        <tr>
	                                            <td class="text-left">
	                                                <div>[${fn:escapeXml(history.orderItem.itemUserCode)}] ${fn:escapeXml(history.orderItem.itemName)}</div>
	                                                <c:if test="${!empty history.orderItem.options}">
	                                                	<div>${shop:viewItemOptions(history.orderItem.setItemFlag, history.orderItem.options)}</div>
	                                                </c:if>
	                                                <div>${shop:viewOrderGiftItemList(history.orderItem.orderGiftItemList)}</div>

	                                                <c:if test="${not empty history.orderItem.textOption}">			<!-- 20260325 필수 추가정보 -->
														${shop:viewItemTextOption(history.orderItem.textOption)}
													</c:if>

	                                                <c:if test="${history.claimStatus == '99' && not empty history.exchangeRefusalReasonText}">
														<div style="color:red">거절 사유 : ${fn:escapeXml(history.exchangeRefusalReasonText)}</div>
													</c:if>
	                                            </td>
	                                            <td>
		                                            <c:choose>
														<c:when test="${shop:sellerId() == history.orderItem.sellerId}"><div>자사</div></c:when>
														<c:otherwise>
															<div>${fn:escapeXml(history.orderItem.sellerName)}</div>
														</c:otherwise>
													</c:choose>
	                                            </td>
	                                            <td>
	                                                <div>${op:numberFormat(history.claimApplyQuantity)}개</div>
	                                            </td>
	                                            <td>
	                                                <div>${fn:escapeXml(history.claimStatusLabel)}</div>
	                                            </td>
	                                            <td>
	                                                <div>
						                                <div>${op:date(history.createdDate)}</div>
						                                <div>${op:timeFormat(history.createdDate.substring(8))}</div>
	                                                </div>
	                                            </td>
	                                            <td>
	                                                <div class="flex_box juc-center gap-08">
	                                                    <button type="button" class="btn btn-default btn-sm" onclick="javascript:Common.popup('${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/claim/exchange-log/${fn:escapeXml(history.claimCode)}', 'exchange_log', 930, 515, 1)">상세</button>
	                                                </div>
	                                            </td>
	                                        </tr>
										</c:forEach>
                                    </tbody>
                                </table>
                            </div>
						</c:if>

						<form id="exchangeForm">
                            <h3 class="mt70"><span>교환 처리</span></h3>
							<input type="hidden" name="orderCode" value="${fn:escapeXml(order.orderCode)}" />
							<input type="hidden" name="orderSequence" value="${fn:escapeXml(order.orderSequence)}" />
                            <div class="board_list">
                                <table class="board_list_table">
                                    <colgroup>
                                        <col style="width:50px;">
                                        <col style="width:150px;">
                                        <col style="width:400px;">
                                        <col style="width:200px;">
                                        <col style="width:100px;">
                                        <col style="width:100px;">
                                        <col style="width:200px;">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th scope="col"><input type="checkbox" id="exchange_all" title="체크박스"/></th>
                                            <th>이미지</th>
                                            <th>답례품정보</th>
                                            <th>판매자</th>
                                            <th>수량</th>
                                            <th>상태</th>
                                            <th>교환 신청일</th>
                                        </tr>
                                    </thead>
                                    <tbody>
										<c:forEach items="${activeExchanges}" var="apply" varStatus="groupIndex">
											<c:set var="orderItem" value="${apply.orderItem}" />
	                                        <tr>
	                                            <td rowspan="2">
	                                                <div>
														<c:if test="${shop:sellerId() == apply.shipmentReturnSellerId || requestContext.opmanagerPage}">
															<input type="checkbox" name="exchangeIds" value="${fn:escapeXml(apply.claimCode)}" />
															<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].itemSequence" value="${fn:escapeXml(orderItem.itemSequence)}" />
															<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].claimCode" value="${fn:escapeXml(apply.claimCode)}" />
															<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].orderSequence" value="${fn:escapeXml(orderItem.orderSequence)}" />
															<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].orderCode" value="${fn:escapeXml(orderItem.orderCode)}" />
														</c:if>
	                                                </div>
	                                            </td>
	                                            <!-- 이미지 -->
	                                            <td class="border_left">
													<div>
														<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(orderItem.itemUserCode)}" target="_blank">
															<img src="${shop:loadImageBySrc(orderItem.imageSrc,'XS')}" alt="${fn:escapeXml(orderItem.itemName)} 이미지" class="item_image"/>
														</a>
													</div>
	                                            </td>
	                                            <!-- // 이미지 -->
	                                            <!-- 상품정보 -->
	                                            <td class="text-left">
	                                                <div>[${fn:escapeXml(orderItem.itemUserCode)}] ${fn:escapeXml(orderItem.itemName)}</div>
	                                                <c:if test="${!empty orderItem.options}">
														<div>${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}</div>
													</c:if>
													<div>${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}</div>

													<c:if test="${not empty orderItem.textOption}">			<!-- 20260325 필수 추가정보 -->
														${shop:viewItemTextOption(orderItem.textOption)}
													</c:if>
	                                            </td>
	                                            <!-- // 상품정보 -->
	                                            <!-- 상호명 -->
	                                            <td>
		                                            <c:choose>
														<c:when test="${shop:sellerId() == orderItem.sellerId}"><div>자사</div></c:when>
														<c:otherwise>
															<div>${fn:escapeXml(orderItem.sellerName)}</div>
														</c:otherwise>
													</c:choose>
	                                            </td>
	                                            <!-- // 상호명 -->
	                                            <!-- 수량 -->
	                                            <td>
	                                                <div>${op:numberFormat(apply.claimApplyQuantity)}개</div>
	                                            </td>
	                                            <!-- // 수량 -->
	                                            <!-- 상태 -->
	                                            <td>
	                                                <div>${fn:escapeXml(apply.claimStatusLabel)}</div>
	                                            </td>
	                                            <!-- // 상태 -->
	                                            <!-- 취소 신청일 -->
	                                            <td>
	                                                <div>
						                                <div>${op:date(apply.createdDate)}</div>
						                                <div>${op:timeFormat(apply.createdDate.substring(8))}</div>
	                                                </div>
	                                            </td>
	                                            <!-- // 취소 신청일 -->
	                                        </tr>
											<c:choose>
												<c:when test="${shop:sellerId() == apply.shipmentReturnSellerId || requestContext.opmanagerPage}">
			                                        <tr>
			                                            <td colspan="7" class="border_left">
			                                                <div class="board_write">
			                                                    <table class="board_write_table">
			                                                        <colgroup>
			                                                            <col style="width: 220px" />
			                                                            <col>
			                                                            <col style="width: 220px" />
			                                                            <col>
			                                                        </colgroup>
			                                                        <tbody>
			                                                            <tr>
			                                                                <td class="label">신청사유</td>
			                                                                <td>
			                                                                    <div class="text-left">
				                                                                    <c:choose>
																						<c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
																						<c:otherwise>[판매자 신청] </c:otherwise>
																					</c:choose>
			                                                                    </div>
			                                                                    <div class="flex_box gap-12">
			                                                                        <div class="input-form">
			                                                                            <input id="exchangeReason2" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReason" type="radio" value="2" ${op:checked('2', apply.exchangeReason)} data-claim-code="${fn:escapeXml(apply.claimCode)}">
			                                                                            <label for="exchangeReason2">고객 사유</label>
			                                                                        </div>
			                                                                        <select name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReasonText" title="고객 사유">
			                                                                            <c:forEach var="code" items="${exchangeClaimReasons}" varStatus="i">
																							<option value="${fn:escapeXml(code.label)}" <c:if test="${code.label eq apply.exchangeReasonText}">selected</c:if>>${fn:escapeXml(code.label)}</option>
																						</c:forEach>
			                                                                        </select>
			                                                                    </div>
			                                                                    <div class="flex_box gap-12">
			                                                                        <div class="input-form">
			                                                                            <input id="exchangeReason1" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReason" type="radio" value="1" ${op:checked('1', apply.exchangeReason)} data-claim-code="${fn:escapeXml(apply.claimCode)}" >
			                                                                            <label for="exchangeReason1">판매자 사유</label>
			                                                                        </div>
			                                                                        <input type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReasonDetail" title="판매자 사유" class="input_txt required _filter wd-300" value="${fn:escapeXml(apply.exchangeReasonDetail)}">
			                                                                    </div>
			                                                                </td>
																			<c:choose>
																				<c:when test="${orderItem.campaignCode == 'MOBILE'}">
																					<td class="label">회수 모바일 번호</td>
																					<td>
																						<div class="flex_box gap-08 item-center">
																							<input style="display:none;" type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingAskType" value="2" data-claim-code="${fn:escapeXml(apply.claimCode)}" readonly="readonly"/>
																 							<input style="display:none;" type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingCompanyName" value="모바일번호" class="input_txt required _filter wd-400" maxlength="30" title="모바일번호" readonly="readonly"/>
																							<input type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingNumber" value="${fn:escapeXml(orderItem.mobileNumber)}" class="input_txt required _filter wd-400" maxlength="30" title="모바일 답례품 번호" readonly="readonly"/>
																						</div>
																					</td>
																				</c:when>
																				<c:otherwise>
					                                                                <td class="label">회수 송장 정보</td>
					                                                                <td>
																						<div style="display:none;">
																							<label><input type="radio" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingAskType" value="1" ${op:checked('1', apply.exchangeShippingAskType)} data-claim-code="${fn:escapeXml(apply.claimCode)}" /> 지정택배사</label>
																							<label><input type="radio" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingAskType" value="2" ${op:checked('2', apply.exchangeShippingAskType)} data-claim-code="${fn:escapeXml(apply.claimCode)}" /> 직접발송</label>
																						</div>
					                                                                    <div class="flex_box gap-08 item-center">
					                                                                        <select name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingCompanyName" title="회수 송장 정보" class="wd-150">
					                                                                            <option value="">선택</option>
					                                                                            <c:forEach items="${deliveryCompanyList}" var="deliveryCompany">
																 									<%-- <option value="${deliveryCompany.deliveryCompanyName}" ${op:selected(apply.exchangeShippingCompanyName, deliveryCompany.deliveryCompanyName)}>${deliveryCompany.deliveryCompanyName}</option> --%>
																 									<option value="${fn:escapeXml(deliveryCompany.deliveryCompanyName)}" ${op:selected(apply.exchangeShippingCompanyName, deliveryCompany.deliveryCompanyName)}>${fn:escapeXml(deliveryCompany.deliveryCompanyName)}</option>
																								</c:forEach>
					                                                                        </select>
					                                                                        <input name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeShippingNumber" title="회수 송장 정보" class="input_txt required _filter wd-400" type="text" value="${fn:escapeXml(apply.exchangeShippingNumber)}">
					                                                                    </div>
					                                                                </td>
																				</c:otherwise>
																			</c:choose>
			                                                            </tr>
																<c:choose>
																	<c:when test="${orderItem.campaignCode == 'MOBILE'}">
		                                                            	<tr style="display:none;">
		                                                            </c:when>
		                                                            <c:otherwise>
		                                                            	<tr>
		                                                            </c:otherwise>
	                                                            </c:choose>
			                                                                <td class="label">회수 요청지</td>
			                                                                <td>
			                                                                    <div>
			                                                                        <div class="flex_box gap-12">
																						<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveSido" value="${fn:escapeXml(apply.exchangeReceiveSido)}" />
																						<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveSigungu" value="${fn:escapeXml(apply.exchangeReceiveSigungu)}" />
																						<input type="hidden" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveEupmyeondong" value="${fn:escapeXml(apply.exchangeReceiveEupmyeondong)}" />

			                                                                            <input name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveName" title="이름" class="input_txt required _filter half" type="text" value="${fn:escapeXml(apply.exchangeReceiveName)}">
			                                                                            <input name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveZipcode" title="우편번호" class="input_txt required _filter half" type="text" value="${fn:escapeXml(apply.exchangeReceiveZipcode)}" readonly="readonly">
			                                                                            <button type="button" class="btn btn-default btn-sm" onclick="openDaumPostcodeForExchange('${fn:escapeXml(apply.claimCode)}')">우편번호</button>
			                                                                        </div>
			                                                                        <div class="flex_box col mt6">
			                                                                            <input name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveAddress" title="주소" class="input_txt required _filter full" type="text" value="${fn:escapeXml(apply.exchangeReceiveAddress)}" readonly="readonly">
			                                                                            <input name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveAddress2" title="상세주소" class="input_txt required _filter full" type="text" value="${fn:escapeXml(apply.exchangeReceiveAddress2)}">
			                                                                            <input name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeReceiveMobile" title="휴대전화번호" class="input_txt required _filter half" type="text" value="${fn:escapeXml(apply.exchangeReceiveMobile)}" maxlength="14">
			                                                                        </div>
			                                                                    </div>
			                                                                </td>
			                                                                <td class="label">반송지 주소</td>
			                                                                <td class="text-left">
				                                                                <c:choose>
																					<c:when test="${empty apply.shipmentReturn.addressName}"><div>반송지 정보가 없습니다.</div></c:when>
																					<c:otherwise>
																						<div>[${fn:escapeXml(apply.shipmentReturn.addressName)}]</div>
																						<div>(${fn:escapeXml(apply.shipmentReturn.zipcode)}) ${fn:escapeXml(apply.shipmentReturn.address)} ${fn:escapeXml(apply.shipmentReturn.addressDetail)}</div>
																					</c:otherwise>
																				</c:choose>
			                                                                </td>
			                                                            </tr>
			                                                            <tr>
			                                                                <td class="label">처리구분</td>
			                                                                <td colspan="3">
			                                                                    <div class="flex_box gap-12">
			                                                                        <div class="input-form">
			                                                                            <input id="claimStatus10" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" type="radio" value="10" ${op:checked('10', apply.claimStatus)} onclick="exchangeCheckedDisplay('${fn:escapeXml(apply.claimCode)}','10')">
			                                                                            <label for="claimStatus10">회수중</label>
			                                                                        </div>
			                                                                        <div class="input-form">
			                                                                            <input id="claimStatus11" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" type="radio" value="11" ${op:checked('11', apply.claimStatus)} onclick="exchangeCheckedDisplay('${fn:escapeXml(apply.claimCode)}','11')">
			                                                                            <label for="claimStatus11">회수 완료</label>
			                                                                        </div>
			                                                                        <div class="input-form">
			                                                                            <input id="claimStatus03" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" type="radio" value="03" ${op:checked('03', apply.claimStatus)} onclick="exchangeCheckedDisplay('${fn:escapeXml(apply.claimCode)}','03')">
			                                                                            <label for="claimStatus03">교환답례품 발송</label>
			                                                                        </div>
			                                                                        <div class="input-form">
			                                                                            <input id="claimStatus99" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" type="radio" value="99" ${op:checked('99', apply.claimStatus)} onclick="exchangeCheckedDisplay('${fn:escapeXml(apply.claimCode)}','99')">
			                                                                            <label for="claimStatus99">교환 거절</label>
			                                                                        </div>
			                                                                    </div>
			                                                                    <div class="text-left">
			                                                                        <p id="redText_${fn:escapeXml(apply.claimCode)}" class="point"></p>
			                                                                    </div>
			                                                                </td>
			                                                            </tr>
																		<tr id="exchangeClaimInfo1_${fn:escapeXml(apply.claimCode)}" style="display:none">
																			<td class="label">거절 사유</th>
																			<td colspan="3">
			                                                                    <div class="flex_box gap-12">
																					<input type="text" class="input_txt _filter half" maxlength="80" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeRefusalReasonText" value="${fn:escapeXml(apply.exchangeRefusalReasonText)}" maxlength="100" />
																				</div>
																				<div class="text-left" style="color:red">해당 거절사유는 구매 고객에게 노출되는 내용입니다.</div>
																			</td>
																		</tr>
																		<c:choose>
																			<c:when test="${orderItem.campaignCode == 'MOBILE'}">
																				<tr id="exchangeClaimInfo2_${fn:escapeXml(apply.claimCode)}" style="display:none">
																					<td class="label">재발송 모바일 번호</td>
																					<td colspan="3">
					                                                                    <div class="flex_box gap-12">
																							<div class="text-left half">
																								<input style="display:none;" type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeDeliveryCompanyId" value="-1" maxlength="30" class="input_txt _filter half" title="모바일번호" readonly="readonly"/>
																								<input type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeDeliveryNumber" value="${fn:escapeXml(apply.exchangeDeliveryNumber)}" maxlength="30" class="input_txt _filter half" title="모바일 답례품 번호"/>
																							</div>
																						</div>
																					</td>
																				</tr>
																			</c:when>
																			<c:otherwise>
																				<tr id="exchangeClaimInfo2_${fn:escapeXml(apply.claimCode)}" style="display:none">
																					<td class="label">재발송 송장 정보</td>
																					<td colspan="3">
					                                                                    <div class="flex_box gap-12">
																							<select name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeDeliveryCompanyId" style="margin-top:0px;">
																								<option value="0">-선택-</option>
																								<c:forEach items="${deliveryCompanyList}" var="deliveryCompany">
																									<option value="${fn:escapeXml(deliveryCompany.deliveryCompanyId)}" ${op:selected(deliveryCompany.deliveryCompanyId, orderItem.deliveryCompanyId)}>${fn:escapeXml(deliveryCompany.deliveryCompanyName)}</option>
																								</c:forEach>
																							</select>
																							<div class="text-left half">
																								<input type="text" name="exchangeApplyMap[${fn:escapeXml(apply.claimCode)}].exchangeDeliveryNumber" value="${fn:escapeXml(apply.exchangeDeliveryNumber)}" maxlength="30" class="input_txt _filter half" title="송장번호" />
																							</div>
																						</div>
																					</td>
																				</tr>
																			</c:otherwise>
																		</c:choose>
			                                                        </tbody>
			                                                    </table>
			                                                </div>
			                                            </td>
			                                        </tr>
												</c:when>
												<c:otherwise>
			                                        <tr>
			                                            <td colspan="7" class="border_left">
			                                                <div class="board_write">
			                                                    <table class="board_write_table">
			                                                        <colgroup>
			                                                            <col style="width: 220px" />
			                                                            <col>
			                                                            <col style="width: 220px" />
			                                                            <col>
			                                                        </colgroup>
			                                                        <tbody>
			                                                            <tr>
			                                                                <td class="label">신청사유</td>
			                                                                <td>
			                                                                    <div class="text-left">
				                                                                    <c:choose>
																						<c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
																						<c:otherwise>[판매자 신청] </c:otherwise>
																					</c:choose>
			                                                                    </div>
			                                                                    <div class="text-left">
				                                                                    <c:choose>
																						<c:when test="${apply.exchangeReason == '2'}">고객 사유</c:when>
																						<c:when test="${apply.exchangeReason == '1'}">판매자 사유</c:when>
																						<c:otherwise>-</c:otherwise>
																					</c:choose>
			                                                                    </div>
			                                                                    <div class="text-left">
				                                                                    ${fn:escapeXml(apply.exchangeReasonText)}
			                                                                    </div>
			                                                                    <div class="text-left">
				                                                                    ${fn:escapeXml(apply.exchangeReasonDetail)}
			                                                                    </div>
			                                                                </td>
			                                                                <td class="label">회수 송장 정보</td>
			                                                                <td>
			                                                                    <div class="text-left">
				                                                                    ${fn:escapeXml(apply.exchangeShippingNumber)}
			                                                                    </div>
			                                                                </td>
			                                                            </tr>
			                                                            <tr>
			                                                                <td class="label">반송지 주소</td>
			                                                                <td class="text-left">
																				<c:choose>
																					<c:when test="${empty apply.shipmentReturn.addressName}">반송지 정보가 없습니다.</c:when>
																					<c:otherwise>
																						[${fn:escapeXml(apply.shipmentReturn.addressName)}] <br/>
																						(${fn:escapeXml(apply.shipmentReturn.zipcode)})&nbsp;&nbsp;&nbsp;${fn:escapeXml(apply.shipmentReturn.address)}&nbsp;&nbsp;&nbsp;${fn:escapeXml(apply.shipmentReturn.addressDetail)}
																					</c:otherwise>
																				</c:choose>
			                                                                </td>
			                                                            </tr>
			                                                            <tr>
			                                                                <td class="label">처리구분</td>
			                                                                <td>
			                                                                    <div class="text-left">
				                                                                    ${fn:escapeXml(apply.claimStatusLabel)}
			                                                                    </div>
			                                                                </td>
			                                                            </tr>
			                                                        </tbody>
			                                                    </table>
			                                                </div>
			                                            </td>
			                                        </tr>
												</c:otherwise>
											</c:choose>
										</c:forEach>
                                    </tbody>
                                </table>
                            </div>

							<c:choose>
								<c:when test="${empty activeExchanges}">
									<div class="no_content">
										${op:message('M00473')} <!-- 데이터가 없습니다. -->
									</div>
								</c:when>
								<c:otherwise>
		                            <div class="btn_all btn_center">
		                                <div class="flex_box gap-08">
		                                    <button type="submit" class="btn btn-dark-gray btn-small">교환 주문 처리</button>
		                                    <!-- <button type="submit" class="btn btn-default btn-small">목록</button> -->
		                                </div>
		                            </div>
								</c:otherwise>
							</c:choose>
						</form>

<script type="text/javascript">
var emessage1 = "!!처리할 항목을 선택하세요";
var emessage4 = "!!교환신청 후 고객이 답례품을 발송하여 배송되는 중입니다.<br/>!!사용자 화면에는 '교환 처리중'으로 표기됩니다.";
var emessage5 = "!!교환신청 후 고객이 발송한 답례품을 확인하는 중입니다.<br/>!!사용자 화면에는 '교환 처리중'으로 표기됩니다.";
var emessage6 = "!!배송정보를 입력하세요.<br/>!!사용자 화면에는 '교환 배송중'으로 표기됩니다.";
var emessage2 = "!!교환신청을 거절합니다. 신청은 철회되며 사용자 화면에는 '교환거절'로 표기됩니다.<br/>!!거절사유를 입력하세요.";


var emessage3 = "!!교환신청을 보류합니다. 차후에 다시 처리할 수 있습니다.<br/>!!사용자화면에는 '교환처리중'으로 표기됩니다.";


$(function(){
	$.each($("input[name$='].claimCode']"),function(){
		var val = $(this).val();
		var checkedVal = $("input:radio[name='exchangeApplyMap["+val+"].claimStatus']:checked").val();

		if(checkedVal == "99"){  					// 교환거절
			$("#redText_"+val).html(emessage2);
		}else if(checkedVal == "02"){				// 교환보류
			$("#redText_"+val).html(emessage3);
		}else if(checkedVal == "10"){				// 회수중
			$("#redText_"+val).html(emessage4);
		}else if(checkedVal == "11"){				// 회수완료
			$("#redText_"+val).html(emessage5);
		}else if(checkedVal == "03"){				// 교환승인
			$("#redText_"+val).html(emessage6);
		}else{
			$("#redText_"+val).html(emessage1);
			/* var deliveryCompanyId = $("select[name='exchangeApplyMap["+val+"].exchangeDeliveryCompanyId']").val();
			var deliveryNumber = $("input[name='exchangeApplyMap["+val+"].exchangeDeliveryNumber']").val();
			if(deliveryCompanyId !="" && deliveryNumber != ""){
				$("input:radio[name='exchangeApplyMap["+val+"].claimStatus'][value='10']").click();
			} */
		}

		<c:forEach items="${activeExchanges}" var="ae" varStatus="i">
			var campaignCode = '${ae.orderItem.campaignCode}';

			if(campaignCode == 'MOBILE') {
				$("input[name='exchangeApplyMap["+val+"].exchangeReceiveZipcode']").removeClass("required");
				$("input[name='exchangeApplyMap["+val+"].exchangeReceiveAddress']").removeClass("required");
				$("input[name='exchangeApplyMap["+val+"].exchangeReceiveAddress2']").removeClass("required");
			}
		</c:forEach>
	});
});

function exchangeCheckedDisplay(claimCode,value){
	$("#exchangeClaimInfo1_"+claimCode).hide();
	$("#exchangeClaimInfo2_"+claimCode).hide();

	if(value == "99"){  					// 교환거절
		$("#redText_"+claimCode).html(emessage2);
		$("#exchangeClaimInfo1_"+claimCode).show();
	}else if(value == "02"){				// 교환보류
		$("#redText_"+claimCode).html(emessage3);
	}else if(value == "10"){				// 회수중
		$("#redText_"+claimCode).html(emessage4);
	}else if(value == "11"){				// 회수완료
		$("#redText_"+claimCode).html(emessage5);
	}else if(value == "03"){				// 교환승인
		$("#redText_"+claimCode).html(emessage6);
		$("#exchangeClaimInfo2_"+claimCode).show();
	}
}
</script>
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
.inDiv {
	width:100%;
	margin:2px 0;
	padding:0;
	float:left;
}
</style>
<c:if test="${not empty returnHistorys}">
	<h3 class="mt70"><span>반품 내역 (거절, 완료, 환불대기)</span></h3>
	<div class="board_list">
		<table class="board_list_table">
			<caption>${op:message('M00059')}</caption>
			<!-- 주문정보 -->
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
                  <th>상품정보</th>
                  <th>상호명</th>
                  <th>수량</th>
                  <th>상태</th>
                  <th>반품 신청일</th>
                  <th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${returnHistorys}" var="history">
					<tr>
						<td class="text-left">
							[${fn:escapeXml(history.orderItem.itemUserCode)}] ${fn:escapeXml(history.orderItem.itemName)}
							<c:if test="${!empty history.orderItem.options}">
								<div>${shop:viewItemOptions(history.orderItem.setItemFlag, history.orderItem.options)}</div>
							</c:if>
							<div>${shop:viewOrderGiftItemList(history.orderItem.orderGiftItemList)}</div>

							<c:if test="${not empty history.orderItem.textOption}">		 			<!-- 20260325 필수 추가정보 -->
								${shop:viewItemTextOption(history.orderItem.textOption)}
							</c:if>

							<c:if test="${history.claimStatus == '99' && not empty history.returnRefusalReasonText}">
								<div><p style="color:red">거절 사유 : ${fn:escapeXml(history.returnRefusalReasonText)}</p></div>
							</c:if>
						</td>
						<td>
							<div>
								<c:choose>
									<c:when test="${shop:sellerId() == history.orderItem.sellerId}">자사</c:when>
									<c:otherwise>
										<span class="glyphicon glyphicon-user"></span>${fn:escapeXml(history.orderItem.sellerName)}
									</c:otherwise>
								</c:choose>
							</div>
						</td>
						<td><div>${op:numberFormat(history.claimApplyQuantity)}</div></td>
						<td><div>${fn:escapeXml(history.claimStatusLabel)}</div></td>
						<td><div><span>${op:datetime(history.createdDate)}</span></div></td>
						<td><div class="flex_box juc-center gap-08"><button type="button" class="btn btn-default btn-sm" onclick="Common.popup('${requestContext.sellerPage == false ? "/opmanager" : "/seller"}' + '/order/claim/return-log/${fn:escapeXml(history.claimCode)}', 'return_log', 930, 400, 1)">상세</button></div></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>

<form id="returnForm">
	<h3 class="mt70"><span>반품 처리</span></h3>
	<input type="hidden" name="orderCode" value="${fn:escapeXml(order.orderCode)}" />
	<input type="hidden" name="orderSequence" value="${fn:escapeXml(order.orderSequence)}" />
	<div class="board_list">
		<table class="board_list_table">
			<caption>${op:message('M00059')}</caption>
			<!-- 주문정보 -->
			<colgroup>
               <col style="width:50px;">
               <col style="width:150px;">
               <col style="width:400px;">
               <col style="width:200px;">
               <col style="width:100px;">
               <col style="width:200px;">
               <col style="width:100px;">
               <col style="width:200px;">
			</colgroup>
			<thead>
                <tr>
                    <th scope="col"><input type="checkbox" id="return_all"></th>
                    <th>이미지</th>
                    <th>상품정보</th>
                    <th>상호명</th>
                    <th>수량</th>
                    <th>환불금액(상품)</th>
                    <th>상태</th>
                    <th>반품 신청일</th>
                </tr>
			</thead>
			<tbody>
				<c:forEach items="${activeReturns}" var="apply" varStatus="groupIndex">
					<c:set var="orderItem" value="${apply.orderItem}" />
					<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].collectionShippingAmount" value="0" />

					<tr>
						<td rowspan="2">
							<div>
							<c:if test="${shop:sellerId() == apply.shipmentReturnSellerId || requestContext.opmanagerPage}">
								<input type="checkbox" name="returnIds" value="${fn:escapeXml(apply.claimCode)}" />
								<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].itemSequence" value="${fn:escapeXml(orderItem.itemSequence)}" />
								<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimCode" value="${fn:escapeXml(apply.claimCode)}" />
								<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].orderSequence" value="${fn:escapeXml(orderItem.orderSequence)}" />
								<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].orderCode" value="${fn:escapeXml(orderItem.orderCode)}" />
								<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].shipmentReturnSellerId" value="${fn:escapeXml(apply.shipmentReturnSellerId)}" />
							</c:if>
							</div>
						</td>
						<td class="border_left">
							<div>
								<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(orderItem.itemUserCode)}" target="_blank">
									<img src="${shop:loadImageBySrc(orderItem.imageSrc,'XS')}" alt="${fn:escapeXml(orderItem.itemName)}" width="50"/>
								</a>
							</div>
						</td>
						<td class="text-left">
							<div>
								[${fn:escapeXml(orderItem.itemUserCode)}] ${fn:escapeXml(orderItem.itemName)}
								<c:if test="${!empty orderItem.options}">
									<p>${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}</p>
								</c:if>
								${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}

								<c:if test="${not empty orderItem.textOption}">			<!-- 20260325 필수 추가정보 -->
									${shop:viewItemTextOption(orderItem.textOption)}
								</c:if>
							</div>
						</td>
						<td>
							<div>
							<c:choose>
								<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-user"></span>${fn:escapeXml(orderItem.sellerName)}
								</c:otherwise>
							</c:choose>
							</div>
						</td>
						<td>
							<div>${op:numberFormat(apply.claimApplyQuantity)}</div>
						</td>
						<td>
							<div>${op:numberFormat(apply.claimApplyAmount)}</div>
						</td>
						<td>
							<div>${fn:escapeXml(apply.claimStatusLabel)}</div>
						</td>
						<td>
							<div><span>${op:datetime(apply.createdDate)}</span></div>
						</td>
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
																<input type="radio" id="rdo_return_${fn:escapeXml(apply.claimCode)}-1" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReason" value="2" ${op:checked('2', apply.returnReason)} class="returnReason" data-claim-code="${fn:escapeXml(apply.claimCode)}" />
																<label for="rdo_return_${fn:escapeXml(apply.claimCode)}-1">고객 사유</label>
															</div>
															<select name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReasonText">
																<c:forEach var="code" items="${returnClaimReasons}" varStatus="i">
																	<option value="${fn:escapeXml(code.label)}" <c:if test="${code.label eq apply.returnReasonText}">selected</c:if>>${fn:escapeXml(code.label)}</option>
																</c:forEach>
															</select>
														</div>
														<div class="flex_box gap-12">
															<div class="input-form">
																<input type="radio" id="rdo_return_${fn:escapeXml(apply.claimCode)}-2" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReason" value="1" ${op:checked('1', apply.returnReason)} class="returnReason" data-claim-code="${fn:escapeXml(apply.claimCode)}" />
																<label for="rdo_return_${fn:escapeXml(apply.claimCode)}-2">판매자 사유</label>
															</div>
															<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReasonDetail" maxlength="80" value="${fn:escapeXml(apply.returnReasonDetail)}" class="input_txt required _filter wd-300" title="판매자사유"/>
														</div>
													</td>
													<c:choose>
														<c:when test="${orderItem.campaignCode == 'MOBILE'}">
															<td class="label">회수 모바일 번호</td>
															<td>
																<div class="flex_box gap-08 item-center">
										 							<input style="display:none;" type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingCompanyName" value="모바일번호" class="input_txt required _filter wd-400" maxlength="30" title="모바일번호" readonly="readonly"/>
																	<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingNumber" value="${fn:escapeXml(orderItem.mobileNumber)}" class="input_txt required _filter wd-400" maxlength="30" title="모바일 답례품 번호" readonly="readonly"/>
																</div>
															</td>
														</c:when>
														<c:otherwise>
															<td class="label">회수 송장 정보</td>
															<td>
																<div class="flex_box gap-08 item-center">
																	<select name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingCompanyName" class="required" title="회수 송장 택배사 정보">
																		<option value="">-선택-</option>
										 								<c:forEach items="${deliveryCompanyList}" var="deliveryCompany">
										 									<option value="${fn:escapeXml(deliveryCompany.deliveryCompanyName)}" ${op:selected(apply.returnShippingCompanyName, deliveryCompany.deliveryCompanyName)}>${fn:escapeXml(deliveryCompany.deliveryCompanyName)}</option>
																		</c:forEach>
										 							</select>
																	<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnShippingNumber" value="${fn:escapeXml(apply.returnShippingNumber)}" class="input_txt required _filter wd-400" maxlength="30" title="회수송장번호" />
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
																<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveSido" value="${fn:escapeXml(apply.returnReserveSido)}" />
																<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveSigungu" value="${fn:escapeXml(apply.returnReserveSigungu)}" />
																<input type="hidden" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveEupmyeondong" value="${fn:escapeXml(apply.returnReserveEupmyeondong)}" />
																<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveName" placeholder="이름" value="${fn:escapeXml(apply.returnReserveName)}" class="input_txt required _filter half" title="이름"/>
																<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveZipcode" placeholder="우편번호" value="${fn:escapeXml(apply.returnReserveZipcode)}" class="input_txt required _filter half" readonly/>
																<button type="button" class="btn btn-default btn-sm" onclick="openDaumPostcodeForReturn('${fn:escapeXml(apply.claimCode)}')">우편번호</button>
															</div>
															<div class="flex_box col mt6">
																<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveAddress" title="주소" maxlength="7" class="input_txt required _filter full" readonly="readonly" value="${fn:escapeXml(apply.returnReserveAddress)}" />
																<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveAddress2"  maxlength="100" class="input_txt required _filter full" title="상세주소" value="${fn:escapeXml(apply.returnReserveAddress2)}" />
																<%--<input type="text" name="returnApplyMap[${apply.claimCode}].returnReservePhone" class="required" title="전화번호" placeholder="전화번호" value="${apply.returnReservePhone}" maxlength="14" />--%>
																<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnReserveMobile" class="input_txt required _filter full" title="휴대전화번호" placeholder="휴대전화번호" value="${fn:escapeXml(apply.returnReserveMobile)}" maxlength="14" />
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
													<td colspan="3" id="returnSelect_${fn:escapeXml(apply.claimCode)}">
														<div class="flex_box gap-12">
															<div class="input-form">
	                                                            <input id="rdo_return_${fn:escapeXml(apply.claimCode)}-11" type="radio" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="10" ${op:checked('10', apply.claimStatus)} onclick="returnCheckedDisplay('${fn:escapeXml(apply.claimCode)}','10')" />
	                                                            <label for="rdo_return_${fn:escapeXml(apply.claimCode)}-11">회수중</label>
	                                                        </div>
	                                                        <div class="input-form">
	                                                            <input id="rdo_return_${fn:escapeXml(apply.claimCode)}-12" type="radio" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="11" ${op:checked('11', apply.claimStatus)} onclick="returnCheckedDisplay('${fn:escapeXml(apply.claimCode)}','11')" />
	                                                            <label for="rdo_return_${fn:escapeXml(apply.claimCode)}-12">회수 완료</label>
	                                                        </div>
	                                                        <div class="input-form">
	                                                            <input type="radio" id="rdo_return_${fn:escapeXml(apply.claimCode)}-13" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="03" ${op:checked('03', apply.claimStatus)} onclick="returnCheckedDisplay('${fn:escapeXml(apply.claimCode)}','03')" />
	                                                            <label for="rdo_return_${fn:escapeXml(apply.claimCode)}-13">반품 승인</label>
	                                                        </div>
	                                                        <div class="input-form">
	                                                            <input id="rdo_return_${fn:escapeXml(apply.claimCode)}-14" type="radio" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="99" ${op:checked('99', apply.claimStatus)} onclick="returnCheckedDisplay('${fn:escapeXml(apply.claimCode)}','99')" />
	                                                            <label for="rdo_return_${fn:escapeXml(apply.claimCode)}-14">반품 거절</label>
	                                                        </div>
														</div>
														<div class="text-left">
															<p id="redText_${fn:escapeXml(apply.claimCode)}" style="margin-top:5px;color:red"></p>
														</div>
													</td>
													<td class="label" id="returnDeliveryLabel_${fn:escapeXml(apply.claimCode)}" style="display:none;">반품거절 송장 정보</td>
													<td id="returnDeliveryInput_${fn:escapeXml(apply.claimCode)}" style="display:none;">
														<div class="flex_box gap-08 item-center">
															<select name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].rejectShippingDeliveryCompanyId" >
																<option value="">-선택-</option>
								 								<c:forEach items="${deliveryCompanyList}" var="deliveryCompany">
								 									<option value="${fn:escapeXml(deliveryCompany.deliveryCompanyId)}" ${op:selected(apply.rejectShippingDeliveryCompanyId, deliveryCompany.deliveryCompanyId)}>${fn:escapeXml(deliveryCompany.deliveryCompanyName)}</option>
																</c:forEach>
								 							</select>
															<input type="text" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].rejectShippingDeliveryNumber" value="${fn:escapeXml(apply.rejectShippingDeliveryNumber)}" class="input_txt _filter wd-400" maxlength="30" title="반품거절 송장번호" />
														</div>
													</td>
												</tr>

												<tr id="returnClaimInfo1_${fn:escapeXml(apply.claimCode)}" style="display:none">
													<td class="label">거절 사유</td>
													<td colspan="3">
														<input type="text" class="form-block" maxlength="80" name="returnApplyMap[${fn:escapeXml(apply.claimCode)}].returnRefusalReasonText" value="${fn:escapeXml(apply.returnRefusalReasonText)}"  />
														<p style="color:red">해당 거절사유는 구매 고객에게 노출되는 내용입니다.</p>
													</td>
												</tr>

												<tr style="display:none">
													<td class="label">회수 비용</td>
													<td colspan="3">
														<%-- <input type="text" class="form-block _number" maxlength="100" name="returnApplyMap[${apply.claimCode}].collectionShippingAmount" ${apply.returnReason == '1' ? 'disabled="disabled"' : ''} value="${apply.collectionShippingAmount}" /> --%>
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="7">
									<div class="board_write">
										<table class="board_write_table">
											<colgroup>
												<col style="width:10%" />
												<col style="width:40%"/>
												<col style="width:10%" />
												<col style="width:40%"/>
											</colgroup>
											<tbody>
												<tr>
													<td class="label">신청사유</td>
													<td>
														<div>
															<c:choose>
																<c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
																<c:otherwise>[판매자 신청] </c:otherwise>
															</c:choose>

															<c:choose>
																<c:when test="${apply.returnReason == '2'}">고객 사유</c:when>
																<c:when test="${apply.returnReason == '1'}">판매자 사유</c:when>
																<c:otherwise>-</c:otherwise>
															</c:choose>
														</div>
														<div>
															<p class="mt5">${fn:escapeXml(apply.returnReasonText)}</p>
														</div>
														<div>
															<p>${fn:escapeXml(apply.returnReasonDetail)}</p>
														</div>
													</td>
													<td class="label">반품 송장 정보</td>
													<td>
														<div>${fn:escapeXml(apply.returnShippingNumber)}</div>
													</td>
												</tr>
												<tr>
													<td class="label">반송지 주소</td>
													<td colspan="3">
														<div>
														<c:choose>
															<c:when test="${empty apply.shipmentReturn.addressName}">반송지 정보가 없습니다.</c:when>
															<c:otherwise>
																[${fn:escapeXml(apply.shipmentReturn.addressName)}] <br/>
																(${fn:escapeXml(apply.shipmentReturn.zipcode)})&nbsp;&nbsp;&nbsp;${fn:escapeXml(apply.shipmentReturn.address)}&nbsp;&nbsp;&nbsp;${fn:escapeXml(apply.shipmentReturn.addressDetail)}
															</c:otherwise>
														</c:choose>
														</div>
													</td>
												</tr>
												<tr>
													<td class="label">처리구분</td>
													<td colspan="3">
														<div>
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
		<c:when test="${empty activeReturns}">
			<div class="no_content">
				${op:message('M00473')} <!-- 데이터가 없습니다. -->
			</div>
		</c:when>
		<c:otherwise>
            <div class="btn_all btn_center">
                 <div class="flex_box gap-08">
                     <button type="submit" class="btn btn-dark-gray btn-small">확인</button>
                 </div>
            </div>
		</c:otherwise>
	</c:choose>
</form>
<script type="text/javascript">
var rmessage1 = "!!처리할 항목을 선택하세요";
var rmessage2 = "!!반품신청을 거절합니다. 신청은 철회되며 사용자화면에는 '반품거절'로 표기됩니다.<br/>!!거절사유를 입력하세요";
var rmessage3 = "!!반품신청을 보류합니다. 차후에 다시 처리할 수 있습니다.<br/>!!사용자화면에는 '반품 처리중'으로 표기됩니다.";
var rmessage4 = "!!반품신청 후 상품이 발송하여 배송되는 중입니다.<br/>!!사용자 화면에는 '반품 처리중'으로 표기됩니다.";
//var rmessage5 = "!!반품신청 후 고객이 발송한 상품을 확인하였습니다..<br/>!!사용자화면에는 '반품처리중'로 표기됩니다.";
var rmessage5 = "!!반품신청 후 고객이 발송한 상품이 확인중입니다.<br/>!!사용자 화면에는 '반품 처리중'으로 표기됩니다.";
//var rmessage6 = "!!반품신청을 승인하면 환불내역으로 이동합니다.<br/>!!환불이 처리될때까지 사용자화면에는 '반품처리중'으로 표기됩니다.";
var rmessage6 = "!!반품신청을 승인하면 환불내역으로 이동합니다.<br/>!!사용자 화면에는 '환불 처리중'으로 표기됩니다.";

$(function(){
	$.each($("input[name$='].claimCode']"),function(){
		var val = $(this).val();
		var checkedVal = $("input:radio[name='returnApplyMap["+val+"].claimStatus']:checked").val();

		if(checkedVal == "99"){  					// 반품거절
			$("#redText_"+val).html(rmessage2);
		}else if(checkedVal == "02"){				// 반품보류
			$("#redText_"+val).html(rmessage3);
		}else if(checkedVal == "10"){				// 회수중
			$("#redText_"+val).html(rmessage4);
		}else if(checkedVal == "11"){				// 회수완료
			$("#redText_"+val).html(rmessage5);
		}else if(checkedVal == "03"){				// 반품승인
			$("#redText_"+val).html(rmessage6);
		}else{
			$("#redText_"+val).html(rmessage1);

			/* var deliveryCompanyId = $("select[name='returnApplyMap["+val+"].returnDeliveryCompanyId']").val();
			var deliveryNumber = $("input[name='returnApplyMap["+val+"].returnDeliveryNumber']").val();
			if(deliveryCompanyId !="" && deliveryNumber != ""){
				$("input:radio[name='returnApplyMap["+val+"].claimStatus'][value='10']").click();
			} */
		}

		<c:forEach items="${activeReturns}" var="ar" varStatus="i">
			var campaignCode = '${ar.orderItem.campaignCode}';

			if(campaignCode == 'MOBILE') {
				$("input[name='returnApplyMap["+val+"].returnReserveZipcode']").removeClass("required");
				$("input[name='returnApplyMap["+val+"].returnReserveAddress']").removeClass("required");
				$("input[name='returnApplyMap["+val+"].returnReserveAddress2']").removeClass("required");
			}
		</c:forEach>
	});
});

function returnCheckedDisplay(claimCode,value){
	$("#returnClaimInfo1_"+claimCode).hide();
	$("#returnClaimInfo2_"+claimCode).hide();
	/*
	$("#returnSelect_"+claimCode).attr('colspan', '3');
	$("#returnDeliveryLabel_"+claimCode).hide();
	$("#returnDeliveryInput_"+claimCode).hide();
	*/
	if(value == "99"){  					// 반품거절
		$("#redText_"+claimCode).html(rmessage2);
		$("#returnClaimInfo1_"+claimCode).show();
		/*
		$("#returnSelect_"+claimCode).attr('colspan', '1');
		$("#returnDeliveryLabel_"+claimCode).show();
		$("#returnDeliveryInput_"+claimCode).show();
		*/
	}else if(value == "02"){				// 반품보류
		$("#redText_"+claimCode).html(rmessage3);
	}else if(value == "10"){				// 회수중
		$("#redText_"+claimCode).html(rmessage4);
	}else if(value == "11"){				// 회수완료
		$("#redText_"+claimCode).html(rmessage5);
	}else if(value == "03"){				// 반품승인
		$("#redText_"+claimCode).html(rmessage6);
		$("#returnClaimInfo2_"+claimCode).show();
	}
}
</script>
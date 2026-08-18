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

<c:if test="${not empty cancelHistorys}">
	<h3 class="mt70">취소 내역 (완료, 환불대기)</h3>
	<div class="board_list">
		<table class="board_list_table">
			<caption>${op:message('M00059')}</caption>
			<!-- 주문정보 -->
			<colgroup>
	            <col style="width:400px;">
	            <col style="width:150px;">
	            <col style="width:100px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col" class="none_left">답례품정보</th>
					<th scope="col" class="none_left">상호명</th>
					<th scope="col" class="none_left">수량</th>
					<th scope="col" class="none_left">판매가</th>
					<th scope="col" class="none_left">상태</th>
					<th scope="col" class="none_left">취소 신청일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cancelHistorys}" var="history">
					<tr>
						<td class="text-left">
	                        <div>[${fn:escapeXml(history.orderItem.itemUserCode)}] ${fn:escapeXml(history.orderItem.itemName)}
	                        <c:if test="${!empty history.orderItem.options}">
	                            <p>${shop:viewItemOptions(history.orderItem.setItemFlag, history.orderItem.options)}</p>
	                        </c:if>

							${shop:viewOrderGiftItemList(history.orderItem.orderGiftItemList)}

							<c:if test="${not empty history.orderItem.textOption}">			<!-- 20260325 필수 추가정보 -->
								${shop:viewItemTextOption(history.orderItem.textOption)}
							</c:if>

							<c:if test="${history.claimStatus == '99' && not empty history.cancelRefusalReasonText}">
								<p style="color:red">거절 사유 : ${fn:escapeXml(history.cancelRefusalReasonText)}</p>
							</c:if>
							</div>
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
						<td class="text-center"><div>${op:numberFormat(history.claimApplyQuantity)}</div></td>
						<td class="text-center"><div>${op:numberFormat(history.claimApplyAmount)}</div></td>
						<td class="text-center"><div>${fn:escapeXml(history.claimStatusLabel)}</div></td>
						<td class="text-center"><div>${op:datetime(history.createdDate)}</div></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>

<%-- <c:if test="${!empty activeCancels}"> --%>
<form id="cancelForm">
	<h3 class="mt70">취소 처리</h3>
	<input type="hidden" name="orderCode" value="${fn:escapeXml(order.orderCode)}" />
	<input type="hidden" name="orderSequence" value="${fn:escapeXml(order.orderSequence)}" />
	<c:set var="cancelCount">0</c:set>
	<c:forEach items="${activeCancels}" var="group" varStatus="groupIndex">
		<c:forEach items="${group.orderItems}" var="orderItem" varStatus="orderItemIndex">
			<c:set var="apply" value="${orderItem.cancelApply}" />
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
						<%-- <col style="width: 120px" /> --%>
					</colgroup>
					<thead>
						<tr>
							<th scope="col" class="none_left"><input type="checkbox" id="cancel_all" title="체크박스" /></th>
							<th scope="col" class="none_left">이미지</th>
							<th scope="col" class="none_left">답례품정보</th>
							<th scope="col" class="none_left">상호명</th>
							<th scope="col" class="none_left">수량</th>
							<th scope="col" class="none_left">판매가</th>
							<th scope="col" class="none_left">상태</th>
							<th scope="col" class="none_left">취소 신청일</th>
							<!-- <th scope="col" class="none_left">배송비</th> -->
						</tr>
					</thead>
					<tbody>
						<tr>
	                        <td>
	                            <c:if test="${shop:sellerId() == orderItem.sellerId || requestContext.opmanagerPage}">
	                                <input type="checkbox" name="cancelIds" class="rePayShipping" value="${fn:escapeXml(apply.claimCode)}" />
	                                <input type="hidden" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].itemSequence" value="${fn:escapeXml(orderItem.itemSequence)}" />
	                                <input type="hidden" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimCode" value="${fn:escapeXml(apply.claimCode)}" />
	                                <input type="hidden" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].orderSequence" value="${fn:escapeXml(orderItem.orderSequence)}" />
	                                <input type="hidden" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].orderCode" value="${fn:escapeXml(orderItem.orderCode)}" />
	                                <input type="hidden" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimApplyQuantity" value="${fn:escapeXml(orderItem.cancelApply.claimApplyQuantity)}" />
	                            </c:if>
	                        </td>
							<td>
								<div>
									<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(orderItem.itemUserCode)}" target="_blank">
										<img src="${shop:loadImageBySrc(orderItem.imageSrc,'XS')}" alt="${fn:escapeXml(orderItem.itemName)}" width="50"/>
									</a>
								</div>
							</td>
							<td>
								[${fn:escapeXml(orderItem.itemUserCode)}] ${fn:escapeXml(orderItem.itemName)}
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
							<td class="text-right">
								${op:numberFormat(orderItem.cancelApply.claimApplyQuantity)}개
							</td>
							<td class="text-right">
								${op:numberFormat(orderItem.cancelApply.claimApplyAmount)}원
							</td>
							<td class="text-center">
								${fn:escapeXml(orderItem.cancelApply.claimStatusLabel)}
							</td>
							<td class="text-center">
								${op:datetime(orderItem.cancelApply.createdDate)}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="board_write mt20">
				<table class="board_write_table">
				    <colgroup>
				        <col style="width: 220px" />
				        <col>
				    </colgroup>
			        <tbody>
            			<c:choose>
	                        <c:when test="${shop:sellerId() == orderItem.sellerId || requestContext.opmanagerPage}">
                               <tr>
                                   <td class="label">신청사유</td>
                                   <td>
                                       <div class="flex_box gap-12">
                                           <%-- <div class="inDiv">
                                               <c:choose>
                                                   <c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
                                                   <c:otherwise>[판매자 신청] </c:otherwise>
                                               </c:choose>
                                           </div> --%>
                                           <div class="input-form" style="min-width:100px;width:8%;">
                                               <input type="radio" id="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason_2" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason" value="2" ${op:checked('2', apply.cancelReason)} class="cancelReason" data-claim-code="${fn:escapeXml(apply.claimCode)}" />
                                               <label for="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason_2">고객 사유</label>
                                           </div>
                                           <select name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReasonText">
                                               <c:forEach var="code" items="${cancelClaimReasons}" varStatus="i">
                                                   <option value="${fn:escapeXml(code.label)}" <c:if test="${code.label eq apply.cancelReasonText}">selected</c:if>>${fn:escapeXml(code.label)}</option>
                                               </c:forEach>
                                           </select>
                                        </div>
                                        <div class="flex_box gap-12">
                                           <div class="input-form" style="min-width:100px;width:8%;">
                                               <input type="radio" id="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason_1" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason" value="1" ${op:checked('1', apply.cancelReason)} class="cancelReason" data-claim-code="${fn:escapeXml(apply.claimCode)}" />
                                               <label for="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason_1">판매자 사유</label>
                                           </div>
                                           <input type="text" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReasonDetail" maxlength="80" value="${fn:escapeXml(apply.cancelReasonDetail)}" style="width:80%" />
                                       </div>
                                        <input type="hidden" name="cancelShippingMap[${fn:escapeXml(group.shippingSequence)}].shippingSequence" value="${fn:escapeXml(group.shippingSequence)}" />
                                   </td>
                               </tr>
                               <tr>
                                   <td class="label">처리구분</td>
                                   <td>
                                  		<div class="flex_box gap-12">
	                                       <div class="input-form">
	                                           <input type="radio" id="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus_98" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="98" ${op:checked('98', apply.claimStatus)} onclick="cancelCheckedDisplay('${fn:escapeXml(apply.claimCode)}','98')" />
												<c:choose>
													<c:when test="${orderItem.campaignCode == 'MOBILE' }">
														<label for="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus_98">취소 거절 + 발송 처리</label>
													</c:when>
													<c:otherwise>
														<label for="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus_98">취소 거절 + 배송 처리</label>
													</c:otherwise>
												</c:choose>
                                           </div>
                                           <div class="input-form">
	                                           <input type="radio" id="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus_99" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus" value="03" ${op:checked('03', apply.claimStatus)} onclick="cancelCheckedDisplay('${fn:escapeXml(apply.claimCode)}','03')" />
	                                           <label for="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].claimStatus_99"> 취소 승인</label>
											</div>
                                       </div>
                                       <div>
	                                       <p id="redText_${fn:escapeXml(apply.claimCode)}" class="point"></p>
                                       </div>
                                   </td>
                               </tr>
                               <tr id="cancelClaimInfo1_${fn:escapeXml(apply.claimCode)}" style="display:none">
                                   <td class="label">거절사유</td>
                                   <td>
                                       <input type="text" class="form-block" maxlength="80" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelRefusalReasonText" />
                                   </td>
                               </tr>
                               <tr id="cancelClaimInfo2_${fn:escapeXml(apply.claimCode)}" style="display:none">
                                   <td class="label">배송정보</td>
                                   <td>
                                       <p class="mb5 mt10">
											<c:choose>
												<c:when test="${orderItem.campaignCode == 'MOBILE' }">
													모바일번호 : <input type="text" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].deliveryNumber" maxlength="30" />
												</c:when>
												<c:otherwise>
		                                           택배사 : <select name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].deliveryCompanyId">
					                                           <option value="0">선택</option>
					                                           <c:forEach items="${deliveryCompanyList}" var="deliveryCompany">
					                                               <option value="${fn:escapeXml(deliveryCompany.deliveryCompanyId)}">${fn:escapeXml(deliveryCompany.deliveryCompanyName)}</option>
					                                           </c:forEach>
					                                       </select>
		                                           송장번호 : <input type="text" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].deliveryNumber" maxlength="30" />
												</c:otherwise>
											</c:choose>
                                       </p>
                                   </td>
                               </tr>

	                        </c:when>
	                        <c:otherwise>
                               <tr>
                                   <td class="label">신청사유</td>
                                   <td>
                                       <div>
                                           <div class="inDiv">
                                               <c:choose>
                                                   <c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
                                                   <c:otherwise>[판매자 신청] </c:otherwise>
                                               </c:choose>
                                           </div>
                                           <div class="inDiv">
                                               <label style="width:100px"><input type="radio" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason" value="2" ${op:checked('2', apply.cancelReason)} class="cancelReason" data-claim-code="${fn:escapeXml(apply.claimCode)}" /> 고객 사유</label>
                                               <select name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReasonText">
                                                   <c:forEach var="code" items="${cancelClaimReasons}" varStatus="i">
                                                       <option value="${fn:escapeXml(code.label)}" <c:if test="${code.label eq apply.cancelReasonText}">selected</c:if>>${fn:escapeXml(code.label)}</option>
                                                   </c:forEach>
                                               </select>
                                           </div>
                                           <div class="inDiv">
                                               <label style="width:100px"><input type="radio" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReason" value="1" ${op:checked('1', apply.cancelReason)} class="cancelReason" data-claim-code="${fn:escapeXml(apply.claimCode)}" /> 판매자 사유</label>
                                               <input type="text" name="cancelApplyMap[${fn:escapeXml(apply.claimCode)}].cancelReasonDetail" maxlength="80" value="${fn:escapeXml(apply.cancelReasonDetail)}" style="width:80%" />
                                           </div>
                                       </div>
                                   </td>
                               </tr>
                               <tr>
                                   <td class="label">처리구분</td>
                                   <td colspan="3">
                                        ${fn:escapeXml(apply.claimStatusLabel)}
                                   </td>
                               </tr>
	                        </c:otherwise>
	                    </c:choose>
  	                  </tbody>
                   </table>
                 </div>
		</c:forEach>
	</c:forEach>

	<c:choose>
		<c:when test="${empty activeCancels}">
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
						<%-- <col style="width: 120px" /> --%>
					</colgroup>
					<thead>
						<tr>
							<th scope="col" class="none_left"><input type="checkbox" id="cancel_all" title="체크박스" /></th>
							<th scope="col" class="none_left">이미지</th>
							<th scope="col" class="none_left">답례품정보</th>
							<th scope="col" class="none_left">상호명</th>
							<th scope="col" class="none_left">수량</th>
							<th scope="col" class="none_left">판매가</th>
							<th scope="col" class="none_left">상태</th>
							<th scope="col" class="none_left">취소 신청일</th>
							<!-- <th scope="col" class="none_left">배송비</th> -->
						</tr>
					</thead>
				</table>
			</div>
			<div class="no_content">
				${op:message('M00473')} <!-- 데이터가 없습니다. -->
			</div>
		</c:when>
		<c:otherwise>
			<div class="btn_all btn_center">
                <div class="flex_box gap-08">
                    <button type="submit" class="btn btn-dark-gray btn-small">주문 처리</button>
                </div>
            </div>
		</c:otherwise>
	</c:choose>
</form>
<%-- </c:if> --%>

<script type="text/javascript">
var cmessage1 = "!!처리할 항목을 선택하세요";
var cmessage2 = "!!취소신청을 보류합니다. 차후에 다시 처리할 수 있습니다.<br/>!!사용자화면에는 '취소처리중'으로 표기됩니다.";
var cmessage3 = "!!취소신청을 거절합니다. 신청은 철회되어 해당 주문건은 신규목록으로 이동합니다.<br/>!!사용자화면에는 '결제완료'로 표기됩니다.";
var cmessage4 = "!!취소신청을 거절합니다. 해당 주문건은 '배송중'으로 이동합니다.<br/>!!사용자화면에는 '배송중'로 표기됩니다.";
var cmessage5 = "!!취소신청을 승인하면 환불내역으로 이동합니다.<br/>!!환불이 처리될때까지 사용자화면에는 '취소처리중'으로 표기됩니다.";

$(function(){
	$.each($("input[name$='].claimCode']"),function(){
		var val = $(this).val();
		var checkedVal = $("input:radio[name='cancelApplyMap["+val+"].claimStatus']:checked").val();

		if(checkedVal == "02"){
			$("#redText_"+val).html(cmessage2);
		}else if(checkedVal == "99"){
			$("#redText_"+val).html(cmessage3);
		}else if(checkedVal == "98"){
			$("#redText_"+val).html(cmessage4);
		}else if(checkedVal == "03"){
			$("#redText_"+val).html(cmessage5);
		}else{
			$("#redText_"+val).html(cmessage1);
		}
	});

	/* 취소처리 되지 않아서 숨김 처리 2017-06-13 yulsun.yoo
	$("#cancelForm").validator({
		'submitHandler' : function() {
			if (!confirm('취소 처리 하시겠습니까?')) {
				return false;
			}
		}
	});  */
});

function cancelCheckedDisplay(claimCode,value){
	$("#cancelClaimInfo1_"+claimCode).hide();
	$("#cancelClaimInfo2_"+claimCode).hide();

	if(value == "02"){
		$("#redText_"+claimCode).html(cmessage2);
	}else if(value == "99"){
		$("#redText_"+claimCode).html(cmessage3);
		$("#cancelClaimInfo1_"+claimCode).show();
	}else if(value == "98"){
		$("#redText_"+claimCode).html(cmessage4);
		$("#cancelClaimInfo1_"+claimCode).show();
		$("#cancelClaimInfo2_"+claimCode).show();
	}else if(value == "03"){
		$("#redText_"+claimCode).html(cmessage5);
	}
}
</script>
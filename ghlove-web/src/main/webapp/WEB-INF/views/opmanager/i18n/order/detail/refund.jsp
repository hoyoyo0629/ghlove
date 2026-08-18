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

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3 class="mt10">환불 관리</h3>
<div class="board_write">
	<table class="board_write_table">
		<colgroup>
			<col style="width: 220px;" />
			<col />
			<col style="width: 220px;" />
			<col />
		</colgroup>
		<tbody>

			<tr>
				<td class="label">주문번호</th>
				<td><div><a href="javascript:Manager.orderDetails('refund', '${fn:escapeXml(refund.orderSequence)}', '${fn:escapeXml(refund.orderCode)}', '1', '${fn:escapeXml(requestContext.sellerPage) == false ? "N" : "Y"}')">${fn:escapeXml(refund.orderCode)}</a></div></td>
				<td class="label">환불번호</th>
				<td><div>${fn:escapeXml(refund.refundCode)}</div></td>
			</tr>

			<tr>
				<td class="label">승인 요청일</th>
				<td><div>${op:datetime(refund.createdDate)}</div></td>
				<!-- <td class="label">신청자</th> -->
				<td class="label">주문자</th>
				<td>
					<div>
						<%-- <span>${refund.requestManagerUserName}</span> --%>
						<span>${fn:escapeXml(refund.userName)}</span>
						<span>[${fn:escapeXml(refund.loginId)}]</span>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<c:forEach items="${refund.groups}" var="group">
	<!-- <div style="border:1px solid #a29b9b;padding:10px;" class="mt10"> -->
<h3 class="mt50 fs24"><span>[${fn:escapeXml(group.seller.sellerName)}] 정보</span></h3>
<div class="board_write">
	<table class="board_write_table">
		<colgroup>
			<col style="width:220px;"/>
			<col/>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">업체정보</td>
				<td>
					<div class="flex_box gap-08 item-center">
						<span>${fn:escapeXml(group.seller.companyName)}</span>
						<span>/</span>
						<span>${fn:escapeXml(group.seller.telephoneNumber)}</span>
						<span>/</span>
						<span>${fn:escapeXml(group.seller.phoneNumber)}</span>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label">총 환불금액</td>
				<td>
					<div class="bold fs24">${op:numberFormat(group.returnAmount)}</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<div class="board_list">
       <div class="border_box mt10">
			<table class="board_list_table">
				<colgroup>
                   <col style="width:200px;"/>
                   <col style="width:200px;"/>
                   <col style="width:150px;"/>
                   <col style="width:400px;"/>
                   <col style="width:100px;"/>
                   <col style="width:200px;"/>
                   <col style="width:200px;"/>
				</colgroup>
				<thead>
                   <tr>
                       <th scope="col">구분</th> <!-- 구분 -->
                       <th scope="col">클레임번호</th> <!-- 클레임번호 -->
                       <th scope="col">이미지</th> <!-- 이미지 -->
                       <th scope="col">상품정보</th> <!-- 상품정보 -->
                       <th scope="col">수량</th> <!-- 수량 -->
                       <th scope="col">환불금액(상품)</th> <!-- 환불금액(상품) -->
                       <th scope="col">신청일</th> <!-- 신청일 -->
                   </tr>
				</thead>
				<tbody>
					<c:forEach items="${group.orderCancelApplys}" var="cancel">
						<tr style="background:#fff;">
							<td ><div>취소 ${fn:escapeXml(cancel.claimStatusLabel)}</div></td>
							<td ><div>${fn:escapeXml(cancel.claimCode)}</div></td>
							<td>
								<div>
									<img src="${shop:loadImageBySrc(cancel.orderItem.imageSrc, 'XS')}" alt="${fn:escapeXml(cancel.orderItem.itemName)}" class="item_image"/>
								</div>
							</td>
							<td class="left break-word">
								<!-- <a class="break-word"> -->
									<div>
										${fn:escapeXml(cancel.orderItem.itemName)}
										<c:if test="${!empty cancel.orderItem.options}">
											<p>${shop:viewItemOptions(cancel.orderItem.setItemFlag, cancel.orderItem.options)}</p>
										</c:if>
										${shop:viewOrderGiftItemList(cancel.orderItem.orderGiftItemList)}
									</div>
								<!--</a> -->
							</td>
							<td ><div>${op:numberFormat(cancel.claimApplyQuantity)}</div></td>
							<td ><div>${op:numberFormat(cancel.claimApplyAmount)}</div></td>
							<td ><div>${op:datetime(cancel.createdDate)}</div></td>
						</tr>
					</c:forEach>
					<c:forEach items="${group.orderReturnApplys}" var="returnApply">
						<tr style="background:#fff;">
							<td><div>반품 ${fn:escapeXml(returnApply.claimStatusLabel)}</div></td>
							<td><div>${fn:escapeXml(returnApply.claimCode)}</div></td>
							<td>
								<div>
									<img src="${shop:loadImageBySrc(returnApply.orderItem.imageSrc, 'XS')}" alt="${fn:escapeXml(returnApply.orderItem.itemName)}" class="item_image"/>
								</div>
							</td>
							<td>
								<div>
									${fn:escapeXml(returnApply.orderItem.itemName)}
									<c:if test="${!empty returnApply.orderItem.options}">
										<p>${shop:viewItemOptions(returnApply.orderItem.setItemFlag, returnApply.orderItem.options)}</p>
									</c:if>
									${shop:viewOrderGiftItemList(returnApply.orderItem.orderGiftItemList)}
								</div>
							</td>
							<td><div>${op:numberFormat(returnApply.claimApplyQuantity)}</div></td>
							<td ><div>${op:numberFormat(returnApply.claimApplyAmount)}</div></td>
							<td ><div>${op:datetime(returnApply.createdDate)}</div></td>
						</tr>
					</c:forEach>
				</tbody>

			</table>

		<c:if test="${not empty group.orderAddPayments}">
			<table class="board_list_table mt0">
				<colgroup>
					<col style="width:200px;">
	                <col style="width:200px;">
					<col />
				</colgroup>
				<thead>
					<tr>
						<th scope="col">구분</th>
						<th scope="col">금액</th>
						<th scope="col">내용</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${group.orderAddPayments}" var="addPayment">
						<tr style="background:#fff;">
							<td>
								<div>
								<c:choose>
									<c:when test="${addPayment.addPaymentType == '1'}">추가</c:when>
									<c:otherwise>환불</c:otherwise>
								</c:choose>
								</div>
							</td>
							<td ><div>${op:numberFormat(addPayment.amount)}</div></td>
							<td><div>${fn:escapeXml(addPayment.subject)}</div></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
       </div>
  </div>
<!-- 	</div> -->
</c:forEach>

<!-- 2017.04.28 Jun-Eu Son 환불받을 은행명 설정 -->
<c:forEach items="${bankListByKey}" var="bankList">
	<c:if test="${bankList.key.id eq refund.returnBankName or bankList.label eq refund.returnBankName}">
		<c:set var="returnBankName" value="${bankList.label}" />
	</c:if>
</c:forEach>

<c:choose>
	<c:when test="${refund.refundStatusCode == '1'}">
		<div class="board_write mt40">
			<form id="changePaymentForm" method="post" enctype="multipart/form-data">
				<c:forEach var="refundPayment" items="${refund.orderPayments}">
					<c:if test="${refundPayment.approvalType == 'vbank'}">
						<input type="hidden" name="vbankFlag" value="true">
					</c:if>
					<c:if test="${refundPayment.approvalType == 'realtimebank'}">
						<input type="hidden" name="realtimebankFlag" value="true">
					</c:if>
					<c:if test="${refundPayment.approvalType == 'card'}">
						<input type="hidden" name="cardFlag" value="true">
					</c:if>
				</c:forEach>
		
				<c:if test="${not empty nicepayPgData}">
					<input type="hidden" name="MID" value="${fn:escapeXml(nicepayPgData.pgServiceMid)}" />
					<input type="hidden" name="TID" value="${fn:escapeXml(nicepayPgData.pgKey)}" />
					<input type="hidden" name="CancelAmt" value="${refund.groups.get(0).returnAmount}" />
					<input type="hidden" name="CancelMsg" value="환불" />
					<input type="hidden" name="CancelPwd" value="123456" /> <%--취소 패스워드(가맹점 관리 취소 패스워드 설정 시 application.properties 값 가져오기)--%>
					<input type="hidden" name="PartialCancelCode" value="${fn:escapeXml(partCancel)}" /> <%--부분취소 여부--%>
					<input type="hidden" name="ccPartCl" value="${fn:escapeXml(nicepayPgData.partCancelFlag)}" /> <%--부분취소 가능여부--%>
				</c:if>
				<table class="board_write_table">
					<caption>table list - A</caption>
					<colgroup>
						<col style="width:220px;">
		                      <col>
					</colgroup>
					<tbody>
						<tr>
							<td class="label">환불 총액</td> <!-- 환불 총액 -->
							<td class="text-right">
								<div class="bold fs24">${op:numberFormat(refund.totalReturnAmount)}</div>
							</td>
						</tr>
						<%--
						<c:if test="${not empty refund.returnVirtualNo && refund.orderPayments[0].escrowStatus ne '40'}">
							<tr>
								<th class="label" rowspan="3">환불정보</th>
								<th>은행명</th>
								<td>
									<input type="text" name="returnBankName" value="${returnBankName}" style="width:100%" />
								</td>
							</tr>
							<tr>
								<th>예금주</th>
								<td>
									<input type="text" name="returnBankInName" value="${refund.returnBankInName}" style="width:100%" />
								</td>
							</tr>
							<tr>
								<th>계좌번호</th>
								<td>
									<input type="text" name="returnVirtualNo" value="${refund.returnVirtualNo}" style="width:100%" class="_number" />
								</td>
							</tr>
						</c:if>--%>
					</tbody>
				</table>
				<input type="hidden" name="orderCode" value="${fn:escapeXml(refund.orderCode)}"/>
				<input type="hidden" name="orderSequence" value="${fn:escapeXml(refund.orderSequence)}"/>
		
				<c:set var="orderPayments" scope="request" value="${refund.orderPayments}" />
				<c:set var="pageType" scope="request" value="refund" />
				<%-- <jsp:include page="../include/change-payment.jsp" /> --%>
		
		<!--               <div class="text-info mt10">
		                  - 결제 수단별 부분 취소가 되지 않는 경우에는 <strong>'은행 환불'</strong>을 통해 환불 처리가 가능합니다.
		              </div> -->
		
				<div class="board_list mt40">
				    <table class="board_list_table">
				        <colgroup>
				            <col style="width:200px;"/>
				            <col style="width:200px;"/>
				            <col style="width:200px;"/>
				            <col style="width:200px;"/>
				        </colgroup>
				        <thead>
				            <tr>
				                <th scope="col">결제금액</th> <!-- 결제금액 -->
				                <th scope="col">결제상태</th> <!-- 결제상태 -->
				                <th scope="col">결제일</th> <!-- 결제일 -->
				                <th scope="col">환불금액</th> <!-- 환불금액 -->
				            </tr>
				        </thead>
				        <tbody>
				            <tr style="background:#fff;">
				                <!-- 결제금액 -->
				                <td>
				                    <div>${op:numberFormat(refund.totalReturnAmount)}</div>
				                </td>
				                <!-- // 결제금액 -->
				                <!-- 결제상태 -->
				                <td>
				                    <div>결제완료</div>
				                </td>
				                <!-- // 결제상태 -->
				                <!-- 결제일 -->
				                <td>
				                    <div>
										${op:datetime(refund.orderDate)}
				                    </div>
				                </td>
				                <!-- // 결제일 -->
				                <!-- 환불금액 -->
				                <td>
				                    <div>
				                        <input id="refundAmount" name="refundAmount" title="환불금액" class="input_txt required _filter half amount _number_comma" type="text" value="">
				                    </div>
				                </td>
				                <!-- // 환불금액 -->
				            </tr>
				        </tbody>
				    </table>
				</div>
		
				<div class="btn_all btn_center">
					<div class="flex_box gap-08">
						<button type="submit" class="btn btn-dark-gray btn-small">환불 처리</button>
						<%-- <button type="button" class="btn btn-dark-gray btn-small" id="cancelBtn">신청 취소</button> --%>
						<button type="button" onclick="Link.list('${fn:escapeXml(requestContext.managerUri)}/order/refund/list')" class="btn btn-default btn-small">목록</button>
					</div>
				</div>
			</form>
		</div>
	</c:when>
	<c:otherwise>
		<c:set var="refundBankInfo" value=""/>
		<c:forEach var="refundPayment" items="${refund.orderPayments}">
			<c:if test='${refundPayment.approvalType == "bank" and refundPayment.paymentType == "2" and refundPayment.refundFlag == "Y"}'>
				<c:set var="refundBankInfo" value="${refundPayment.paymentSummary}"/>
			</c:if>
		</c:forEach>
		<div class="board_write mt40">
			<table class="board_write_table">
				<caption>table list - B</caption>
				<colgroup>
					<col style="width:220px;"/>
			              <col/>
				</colgroup>
				<tbody>
					<tr>
						<td class="label">환불 총액</td> <!-- 환불 총액 -->
						<td class="text-right">
		                     <div class="bold fs24">${op:numberFormat(refund.totalReturnAmount)}</div>
		                 </td>
					</tr>
				</tbody>
		    </table>
		</div>	
		<div class="btn_all btn_center">
			<div class="flex_box gap-08">
				<button type="button" onclick="Link.list('${fn:escapeXml(requestContext.managerUri)}/order/refund/list')" class="btn btn-default btn-small">목록</button>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
$(function(){
	// 환불신청 취소처리.
	<%-- 
    $('#cancelBtn').on('click', function(e) {
		e.preventDefault();

		if (!confirm('환불 신청을 취소하시겠습니까?')) {
		    return;
		}

		// 신청 취소 처리...
		var refundCode = '${refund.refundCode}';
		var orderCode =  '${refund.orderCode}';

		var param = {
				orderCode 	:	orderCode,
				refundCode	:	refundCode
		};

		var urlPrefix = "${requestContext.sellerPage ? '/seller' : '/opmanager'}";
		$.post(urlPrefix + "/order/refund/cancel/" + refundCode, param, function(response) {
			Common.responseHandler(response, function() {
			    alert('환불 신청이 취소되었습니다.');
				location.href= urlPrefix + '/order/return/list';
			}, function() {
			    alert(response.errorMessage);
			})
		})
	});
	--%>

	$('#changePaymentForm').validator(function() {
        if (!confirm('해당 답례품을 환불 처리 하시겠습니까?')) {
        	return false;
        }

        Common.removeNumberComma();
		var isError = false;
        var totalReturnAmount = Number('${fn:escapeXml(refund.totalReturnAmount)}');
        if (Number($('#refundAmount').val()) != totalReturnAmount) {
        	alert('환불 금액은 [' + totalReturnAmount +'] 입니다. 환불할 금액을 확인해 주세요.');
        	$('#refundAmount').focus();
        	return false;
        }
	});
})
</script>

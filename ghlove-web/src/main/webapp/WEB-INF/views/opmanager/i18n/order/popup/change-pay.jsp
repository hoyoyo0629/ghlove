<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>


<style>
.table_btn {position: absolute; top: 77px; right: 18px;}
 li {line-height:150%; }
</style>

	<div class="popup_wrap">
		<form id="changePaymentForm" method="post" action="/opmanager/order/${fn:escapeXml(pageType)}/change-pay/process" >
			<input type="hidden" name="orderCode" value="${fn:escapeXml(order.orderCode)}"/>
			<input type="hidden" name="orderSequence" value="${fn:escapeXml(order.orderSequence)}"/>

			<h1 class="popup_title">결제정보 수정</h1> <!-- 결제정보 수정-->
			<div class="popup_contents">

				<h3>결제 정보</h3>
				<table class="inner-table">
					<caption>table list</caption>
					<thead>
						<tr>
							<th scope="col" class="none_left">총 상품금액</th>
							<th scope="col" class="none_left">총 배송비</th>
							<th scope="col" class="none_left">총 결제금액</th>
							<th scope="col" class="none_left">결제 예정금액</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="text-right"><c:out value="${op:numberFormat(order.itemTotalAmount)}"/>원</td>
							<td class="text-right"><c:out value="${op:numberFormat(order.shippingTotalAmount)}"/>원</td>
							<td class="text-right"><c:out value="${op:numberFormat(order.payAmount)}원"/></td>
							<td class="text-right">
								<c:choose>
                                    <c:when test="${order.postPayAmount > 0}">
                                        <span style="color:red"><c:out value="${op:numberFormat(order.postPayAmount)}"/>원</span>
                                    </c:when>
									<c:otherwise>없음</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>

				<c:set var="orderPayments" scope="request" value="${order.orderPayments}" />
				<c:set var="pageType" scope="request" value="" />
				<jsp:include page="../include/change-payment.jsp" />

				<div id="buttons" class="tex_c mt20">
					<button type="submit" class="btn btn-active">저장</button>
				</div>
			</div>
		</form>
		<a href="#" class="popup_close">창 닫기</a>
	</div>
</div>

<script type="text/javascript">

	var TOTAL_ORDER_AMOUNT = ${ order.itemTotalAmount + order.shippingTotalAmount };

	$(function(){

		$('.required').removeClass("required"); // 불필요한 필수 입력 클래스 제거

		$('#changePaymentForm').validator(function() {

			var isError = false;
			var totalCancelAmount = 0, totalRemainingAmount = 0;
			var refundAmount = Number($('input[name=refundAmount]').val());

			if ($('input.payAmounts').length > 0){ // 결제내역의 length (결제한 내역이 없다면 환불도 불가)

				$.each($('input.payAmounts'), function(){

					if ($(this).val() == '') {
						$(this).val('0');
					}

					var remainingAmount = Number($(this).data('remaining-amount'));

					if (remainingAmount > 0) {
						var cancelAmount = Number($(this).val());
						if (cancelAmount > remainingAmount) {
							isError = true;
							alert('취소 가능 금액을 확인해 주세요.');
							$(this).focus();
							return false;
						}
						totalRemainingAmount += remainingAmount;
						totalCancelAmount += cancelAmount;
					}
				});

				// 결제 부분취소 validation
				if(!isError && !checkChangePayment()){
					isError = true;
				}

				// 그 외 은행 환불 validation
				if (!isError && refundAmount > 0) {

					/*if (refundAmount > totalCancelAmount) { // 입력하는 은행 환불 금액이 totalCancelAmount 보다 클 수 없음
						alert("은행 환불 금액이 부분취소금액보다 큽니다. 금액을 확인해 주세요.");
						return false;
					} else {*/
						if(!checkInput($('input[name=refundAccountNumber]'), '계좌번호 항목을 입력해 주세요.')){
							isError = true;
							return false;
						}
						if(!checkInput($('input[name=refundAccountName]'), '예금주 항목을 입력해 주세요.')){
							isError = true;
							return false;
						}
					//}
				}

				if (isError) {
					return false;
				}

				// 결제총금액, 취소금액 비교 확인 : 결제금액보다 취소금이 클 수 없음
				if (TOTAL_ORDER_AMOUNT < totalCancelAmount) {
					alert("부분취소금액이 주문금액보다 큽니다. 금액을 확인해 주세요.");
					return false;
				}

				var cancelWaitingAmount = 0, waitingAmount = 0;

				// 입금대기(결제에정금액)
				$.each($('input[name=deletePaymentIds]'), function(){

					var thisWaitAmount = $(this).closest('td').next().data('wait-amount');

					if (!$(this).is(":checked")) {
						// 입금 대기
						waitingAmount += Number(thisWaitAmount);

					}else {
                        // 입금 대기 취소
                        //cancelWaitingAmount += Number(thisWaitAmount); // 포함하지 않음
                    }
				});

				// 결제 총금액, 수정된 금액 비교 확인 : 결제금액 = 결제금액 - 결제취소 금액 - 은행환불 금액 + 입금 대기 금액 + 추가결제 금액
				var addPaymentAmount = Number($('input[name="newPayments[0].amount"]').val()); // 결제 추가 : 은행입금

				// 포인트 추가 결제 input 이 존재한다면 포함
				var $newPaymentsAmount1 = $('input[name="newPayments[1].amount"]'); // 결제 추가 : 포인트
				if ($newPaymentsAmount1.length == 1) {
					addPaymentAmount += Number($newPaymentsAmount1.val());
				}

				// var amountCheckFlag = TOTAL_ORDER_AMOUNT == (totalRemainingAmount - totalCancelAmount - refundAmount - cancelWaitingAmount + waitingAmount + addPaymentAmount);
				var amountCheckFlag = TOTAL_ORDER_AMOUNT == (totalRemainingAmount - totalCancelAmount - cancelWaitingAmount + waitingAmount + addPaymentAmount); // 금액계산시, 은행환불 금액을 제외

				if(!amountCheckFlag) {
					alert("결제 합계 금액을 확인해 주세요.");
					return false;
				}

			} else { // 결제된 내역이 1건도 없는 경우

				if (refundAmount > 0) {
					// 결제한 내역이 없다면 환불도 불가 (은행환불 관련 입력불가)
					alert('취소할 수 있는 금액이 없습니다.');
					$('input[name=refundAmount]').focus();
					return false;
				}

				var sumAmount = 0;

				// 입금 대기 내역의 은행 입금 취소 부분(체크박스) 체크하는 경우
				$.each($('input[name=deletePaymentIds]'), function(){

					var thisWaitAmount = $(this).closest('td').next().data('wait-amount');

					if (!$(this).is(":checked")) {
						// 입금 대기
						sumAmount += Number(thisWaitAmount);
					} else {
						//sumAmount -= Number(thisWaitAmount); // 포함하지 않음
					}
				});

				// 결제 총금액, 수정된 금액 비교 확인 : 결제금액 = 결제금액 + 결제취소 금액 + 입금 대기 금액 + 추가결제 금액
				var addPaymentAmount = Number($('input[name="newPayments[0].amount"]').val()); // 결제 추가 : 은행입금

				// 포인트 추가 결제 input 이 존재한다면 포함
				var $newPaymentsAmount1 = $('input[name="newPayments[1].amount"]'); // 결제 추가 : 포인트
				if ($newPaymentsAmount1.length == 1) {
					addPaymentAmount += Number($newPaymentsAmount1.val());
				}
				sumAmount += addPaymentAmount;

				if(TOTAL_ORDER_AMOUNT != sumAmount){
					alert("결제 합계 금액을 확인해 주세요.");
					return false;
				}
			}

			if (!confirm('저장하시겠습니까?')) {
				return false;
			}
		});
	})

	function checkChangePayment(){
		// 변경 금액이 입력되어 있고 환불계좌 입력 input이 존재하는 경우, input 입력 여부 확인

		var resultFlag = true; // validation 체크 결과

		$('input[name$=cancelAmount]').each(function() { // 부분취소금액

			// 부분취소금액이 입력되어 있는 경우
			if (Number($(this).val()) > 0) {

				var name = $(this).attr('name');
				var nameArr = name.split('.');

				if(nameArr.length > 0){
					var prefix = nameArr[0];
					var returnBankVirtualNo = prefix + '.returnBankVirtualNo';
					var returnBankInName = prefix + '.returnBankInName';

					var $returnBankVirtualNo = $('input[name="'+ returnBankVirtualNo + '"]');
					var $returnBankInName = $('input[name="'+ returnBankInName + '"]');

					// 환불계좌정보(계좌번호, 예금주) 입력 항목이 존재한다면 입력 여부 확인
					if($returnBankVirtualNo.length > 0){
						if(!checkInput($returnBankVirtualNo, '계좌번호 항목을 입력해 주세요.')){
							resultFlag = false;
							return false; // break
						}
					}
					if($returnBankInName.length > 0){
						if(!checkInput($returnBankInName, '예금주 항목을 입력해 주세요.')){
							resultFlag = false;
							return false; // break
						}
					}
				}
			}
		});

		return resultFlag;
	}

	function checkInput(selector, message) {
		var inputValue = selector.val().trim();
		if (typeof inputValue == 'undefined' || inputValue == null || inputValue == "") {
			alert(message);
			selector.focus();
			return false;
		}
		return true;
	}
</script>
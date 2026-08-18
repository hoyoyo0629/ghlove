<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>



<h1 class="popup_title"><c:out value="${op:date(statisticsParam.searchDate)"/>} <c:out value="${op:message('M01401')}"/> <c:out value="${op:message('M01296')}"/></h1> <!-- 매출 상세 --> <!-- 팝업 -->
<div class="popup_contents">
	 <h3><span><c:out value="${op:date(statisticsParam.searchDate)}"/> <c:out value="${op:message('M01401')}"/></span></h3> <!-- 매출 상세 -->

	<div class="board_list">

		<table class="board_list_table">
			<thead>

				<tr>
					<th><c:out value="${op:message('M00018')}"/></th> <!-- 상품명 -->
					<th class="border_left"><c:out value="${op:message('M00783')}"/></th> <!-- 상품 코드 -->
					<th class="border_left"><c:out value="${op:message('M00357')}"/></th> <!-- 수량 -->
					<th class="border_left"><c:out value="${op:message('M00627')}"/><br />(VAT <c:out value="${op:message('M00958')}"/>)</th> <!-- 상품금액 --> <!-- 포함 -->
				</tr>
			</thead>
			<tbody>
				<c:set var="totalPrice">0</c:set>
				<c:forEach items="${userOrderItemList}" var="list" varStatus="i">
					<c:set var="totalPrice"><c:out value="${totalPrice + list.itemsExcisePrice }"/></c:set>
					<tr>
						<td style="text-align: left;">
							<img src="${fn:escapeXml(list.itemImage)}" class="item_image" alt="상품이미지"  />
							<c:out value="${list.itemName }"/>
						</td>
						<td class="border_left"><c:out value="${list.itemUserCode }"/></td>
						<td class="border_left"><c:out value="${op:numberFormat(list.quantity) }"/></td>
						<td class="border_left"><c:out value="${minors}"/> <c:out value="${op:numberFormat(list.itemsExcisePrice) }"/></td>
					</tr>
				</c:forEach>
				<tr style="background-color : #d5d5d5">
					<td colspan="16" style="font-size: 17px; font-family: '나눔고딕 Bold', 'NanumGothicBold', 'ng_bold', '돋움', 'Dotum', sans-serif; text-align: right; ">
					 <c:out value="${op:message('M00445')}"/>(VAT <c:out value="${op:message('M00958')}"/>) : <c:out value="${op:numberFormat(totalPrice) }"/> - <c:out value="${op:message('M00897')}"/> : <c:out value="${op:numberFormat(order.sumUsePoint + order.orderVendor.vendorAddDiscountAmount + cartCouponDiscountAmount)}"/> + <c:out value="${op:message('M01402')}"/> <c:out value="${op:numberFormat(order.sumDeliveryPrice+order.orderVendor.vendorAddDeliveryExtraCharge)}"/> <br />
					 = <c:out value="${op:message('M01403')}"/> : <c:out value="${op:numberFormat( (totalPrice+sumDeliveryPrice+order.orderVendor.vendorAddDeliveryExtraCharge+ order.sumDeliveryPrice+order.orderVendor.vendorAddDeliveryExtraCharge) - (order.sumUsePoint + order.orderVendor.vendorAddDiscountAmount + cartCouponDiscountAmount) )}"/>

					</td>
				</tr>
			</tbody>
		</table>

	</div>
</div><!--//popup_contents E-->
	<a href="#" class="popup_close">창 닫기</a>
</div>


<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
	});
</script>
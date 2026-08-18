<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style type="text/css">

.no_data {
	padding: 100px;
	text-align: center;
	border: 1px solid #999999;
}
.board_list_table th {
	padding: 2px;
	border: 1px solid #999999;
}
.board_list_table td {
	padding: 2px;
	border: 1px solid #999999;
}
.order_color {
	background: #d9edf7;
}

.cancel_color {
	background: #f2dede;
}

.total-price-title {
	background-color: #FCF8E8;
	font-weight: bold;
	font-size: 13px !important;
	color: #000 !important;
	padding: 10px !important;
}

.grand-total {
	background-color: #dff0d8;
	font-weight: bold;
	font-size: 14px !important;
	color: #000 !important;
	padding: 10px !important;
}

</style>

<h1 class="popup_title"><c:out value="${title}"/></h1>
<div class="popup_contents">
	<c:choose>
		<c:when test="${ mode eq 'user' or mode eq 'day' }">
			<form:form modelAttribute="statisticsParam" method="post">
				<div class="board_write">
					<table class="board_write_table" summary="${fn:escapeXml(item.itemName)} ${op:message('M01382')}">
						<c:if test="${ statisticsParam.userId > 0 }">
							<form:hidden path="userId" />
						</c:if>

						<input type="hidden" name="type" value="detail" />
						<caption><c:out value="${item.itemName}"/> <c:out value="${op:message('M01382')}"/></caption>
						<colgroup>
							<col style="width: 120px;">
							<col style="width: auto;">
						</colgroup>
						<tbody>
							<tr>
								<td class="label"><c:out value="${op:message('M01347')}"/></td> <!-- 기간 -->
								<td>
							 		<div>
										<span class="datepicker"><form:input path="startDate" class="term datepicker" title="${op:message('M00507')}" id="dp28" /></span> <!-- 시작일 -->
										<span class="wave">~</span>
										<span class="datepicker"><form:input path="endDate" class="term datepicker" title="${op:message('M00509')}" id="dp29" /></span> <!-- 종료일 -->
											<span class="day_btns">
												<a href="javascript:;" class="btn_date today">  <c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
												<a href="javascript:;" class="btn_date week-1"> <c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
												<a href="javascript:;" class="btn_date day-15"> <c:out value="${op:message('M00028')}"/></a><!-- 15일 -->
												<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
												<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
												<a href="javascript:;" class="btn_date year-1"> <c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
											</span>
									</div>
						 		</td>
							</tr>
							<tr>
								<td class="label">검색구분</td>
								<td>
									<div>
										<form:select path="where" title="상품명 선택">
											<form:option value="ITEM_NAME"><c:out value="${op:message('M00018')}"/></form:option> <!-- 상품명 -->
											<form:option value="ITEM_USER_CODE"><c:out value="${op:message('M00783')}"/></form:option> <!-- 상품코드 -->
										</form:select>
										<form:input path="query" class="three" title="상세검색 입력" />
									</div>
								</td>
							</tr>
							<tr>
								<td class="label">판매자</td>
								<td>
									<div>
										<form:select path="sellerId">
											<form:option value="0"><c:out value="${op:message('M00039')}"/></form:option>
											<c:forEach items="${sellerList}" var="list" varStatus="i">
												<form:option value="${fn:escapeXml(list.sellerId)}">[<c:out value="${list.loginId}]"/> <c:out value="${list.sellerName}"/></form:option>
											</c:forEach>
										</form:select>&nbsp;
										<a href="javascript:Common.popup('/opmanager/seller/find', 'find_seller', 800, 500, 1)" class="btn btn-dark-gray btn-sm"> <span class="glyphicon glyphicon-search"></span> 검색</a>
									</div>
								</td>
							</tr>
						</tbody>
					</table>

				</div>

				<div class="btn_all">
					<div class="btn_right">
						<button type="submit" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> <c:out value="${op:message('M00048')}"/> <!-- 검색 --></button>
						<a href="/opmanager/shop-statistics/sales/${fn:escapeXml(mode)}/detail-excel-download?${fn:escapeXml(queryString)}"  class="btn btn-success btn-sm"><span class="glyphicon glyphicon-save"></span><c:out value="${op:message('M00254')}"/></a> <!-- 엑셀 다운로드 -->
					</div>
				</div>
			</form:form>
		</c:when>
		<c:otherwise>
			<div class="btn_all">
				<div class="btn_right">
					<a href="/opmanager/shop-statistics/sales/${fn:escapeXml(mode)}/detail-excel-download?${fn:escapeXml(queryString)}"  class="btn btn-success btn-sm"><span class="glyphicon glyphicon-save"></span><c:out value="${op:message('M00254')}"/></a> <!-- 엑셀 다운로드 -->
				</div>
			</div>
		</c:otherwise>
	</c:choose>
	<c:if test="${sellerIdForParam == null}">
		<p class="text-info text-sm">
			* 배송비는 해당 주문 시 발생한 전체 배송비이며 첫번째 상품에만 표기 됩니다.
		</p>
	</c:if>
	<div class="board_list">
		<table class="board_list_table">
			<thead>
				<tr>
					<th><c:out value="${op:message('M01377')}"/></th> <!-- 일자 -->
					<th>구분</th> <!-- 구분 -->
					<th><c:out value="${op:message('M00013')}"/></th> <!-- 주문번호 -->
					<th>주문방법</th>
					<th>브랜드</th>
					<th><c:out value="${op:message('M00018')}"/></th> <!-- 상품명 -->
					<th><c:out value="${op:message('M00019')}"/></th> <!-- 상품코드 -->
					<th><c:out value="${op:message('M00357')}"/></th> <!-- 수량 -->
					<th>상품판매가</th> <!-- 상품판매가 -->
					<th>쿠폰할인</th> <!-- 쿠폰할인 -->
					<th><c:out value="${op:message('M00812')}"/></th> <!-- 추가할인 -->
					<th style="font-weight: bold;">판매가</th> <!-- 판매가 -->
					<c:if test="${sellerIdForParam == null}">
						<th style="font-weight: bold;">배송비</th> <!-- 배송비 -->
					</c:if>
					<th style="font-weight: bold;"><c:out value="${op:message('M00064')}"/></th> <!-- 소계 -->
				</tr>
			</thead>
			<tbody>
				<c:set var="totalAmount" value="0" />
				<c:forEach items="${ orderList }" var="base">
					<c:set var="payTotalAmount" value="0" /> <%--총 판매가(결제)--%>
					<c:set var="payTotalDeliveryAmount" value="0" /><%--총 배송비(결제)--%>
					<c:set var="cancelTotalAmount" value="0" /><%--총 판매가(취소)--%>
					<c:set var="cancelTotalDeliveryAmount" value="0" /><%--총 배송비(취소)--%>
					<tr>
						<td rowspan="${fn:escapeXml(base.rowspan)}"><c:out value="${ op:date(base.key) }"/></td>
						<c:set var="payIndex" value="0" />
						<c:set var="index" value="0" />

						<c:forEach items="${ base.list }" var="list" varStatus="i">

							<c:if test ="${ list.osType == 'Admin' }">
								<c:set var="osType" value="Call"/>
							</c:if>
							<c:if test ="${ list.osType != 'Admin' }">
								<c:set var="osType" value="${ list.osType }"/>
							</c:if>
							<c:if test="${ list.orderType eq 'PAY' }">

								<c:set var="rowspan" value="${ fn:length(list.items) }" />
								<c:if test="${ index > 0 }"></tr><tr></c:if>

								<c:if test="${ payIndex == 0 }">
									<td rowspan="${ base.payRowspan + 1 }" class="order_color">결제</td> <!-- 결제 -->
								</c:if>

								<td rowspan="${fn:escapeXml(rowspan)}"><c:out value="${ list.orderCode }"/></td>
								<td rowspan="${fn:escapeXml(rowspan)}"><c:out value="${ list.osType }"/></td>
								<c:forEach items="${ list.items }" var="item" varStatus="itemIndex">

									<c:if test="${ itemIndex.first == false }"></tr><tr></c:if>
									<td><c:out value="${item.sellerName }"/></td>
									<td style="text-align:left;">
										<c:out value="${ item.itemName }"/>

										<c:if test="${ !empty item.requiredOptionsList }">
							 				<br />
							 				<c:forEach items="${ item.requiredOptionsList }" var="option" varStatus="requiredOptionIndex">
							 					<c:if test="${requiredOptionIndex.index > 0}"><br/></c:if>
							 					<strong>
								 					<span>- <c:out value="${ shop:viewOptionTextNoUl(option.optionName1) }"/></span> <c:out value="${shop:viewOptionTextNoUl(option.optionName2)}"/>
								 					<c:if test="${!empty option.extraPrice && option.extraPrice != 0}">
														(<c:out value="${op:numberFormat(option.extraPrice)}"/>원)
														<c:set var="optionPrice"><c:out value="${ optionPrice + option.extraPrice }"/></c:set>
													</c:if>

													<c:if test="${!empty option.optionCode}">
														(옵션번호 : <c:out value="${option.optionCode}"/>)
													</c:if>
												</strong>
							 				</c:forEach>
							 			</c:if>
										<c:if test="${ not empty item.openMarketOption }">
											<p>
												<strong>
													<c:if test="${item.setItemFlag != 'Y'}">-</c:if>
													<c:out value="${shop:viewItemOptions(item.setItemFlag, item.openMarketOption)}"/>
												</strong>
											</p>
										</c:if>
									</td>
									<td><c:out value="${ item.itemUserCode }"/></td>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.quantity) }"/></td>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.itemPrice) }"/></td><%--itemPrice=상품판매가+옵션가격*수량 --%>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.itemCouponDiscountAmount) }"/></td>
									<td class="border_left number" ><c:out value="${ op:numberFormat(item.vendorAddDiscountAmount) }"/></td>
									<td class="border_left number" style="background-color: #f2f7f4"><c:out value="${ op:numberFormat(item.totalItemPrice) }"/></td>

									<c:if test="${itemIndex.first == true}">
										<c:if test="${sellerIdForParam == null}">
											<td class="border_left number" rowspan="${fn:escapeXml(rowspan)}" style="background-color: #f2f7f4"><c:out value="${op:numberFormat(item.orderShipping)}"/></td>
										</c:if>
										<td class="border_left number" rowspan="${fn:escapeXml(rowspan)}" style="background-color: #f2f7f4"><c:out value="${op:numberFormat(item.subTotal)}"/></td>
									</c:if>

									<c:set var="payTotalAmount"><c:out value="${ payTotalAmount + item.totalItemPrice }"/></c:set>
									<c:set var="payTotalDeliveryAmount"><c:out value="${ payTotalDeliveryAmount + item.orderShipping"/>}</c:set>
								</c:forEach>

								<c:set var="payIndex"><c:out value="${ payIndex + 1 }"/></c:set>
								<c:set var="index"><c:out value="${ index + 1 }"/></c:set>
							</c:if>
						</c:forEach>

						<c:if test="${ payIndex > 0 }">
							<c:if test="${ true }"></tr><tr></c:if>
							<td class="order_color" colspan="9" style="text-align:right; padding:5px;">결제총합</td> <!-- 결제총합 -->
							<td class="order_color number "><c:out value="${ op:numberFormat(payTotalAmount) }"/></td>
							<c:if test="${sellerIdForParam == null}">
								<td class="order_color number "><c:out value="${ op:numberFormat(payTotalDeliveryAmount) }"/></td>
							</c:if>
							<td class="order_color number "><c:out value="${ op:numberFormat(payTotalAmount + payTotalDeliveryAmount) }"/></td>
						</c:if>

						<c:set var="cancelIndex" value="0" />
						<c:forEach items="${ base.list }" var="list" varStatus="i">

							<c:if test ="${ list.osType == 'Admin' }">
								<c:set var="osType" value="Call"/>
							</c:if>
							<c:if test ="${ list.osType != 'Admin' }">
								<c:set var="osType" value="${ list.osType }"/>
							</c:if>

							<c:if test="${ list.orderType eq 'CANCEL' }">
								<c:set var="rowspan" value="${ fn:length(list.items) }" />

								<c:if test="${ index > 0 || payIndex > 0 }"></tr><tr></c:if>

								<c:if test="${ cancelIndex == 0 }">
									<td rowspan="${ base.cancelRowspan + 1 }" class="cancel_color">취소</td> <!-- 취소 -->
								</c:if>

								<td rowspan="${fn:escapeXml(rowspan)}"><c:out value="${ list.orderCode }"/></td>
								<td rowspan="${fn:escapeXml((rowspan)}"><c:out value="${ list.osType }"/></td>

								<c:forEach items="${ list.items }" var="item" varStatus="itemIndex">
									<c:if test="${ itemIndex.first == false }"></tr><tr></c:if>
									<td><c:out value="${item.sellerName}"/></td>
									<td style="text-align:left;">
										<c:out value="${ item.itemName }"/>

										<c:if test="${ !empty item.requiredOptionsList }">
											<br />
											<c:forEach items="${ item.requiredOptionsList }" var="option" varStatus="requiredOptionIndex">
												<c:if test="${requiredOptionIndex.index > 0}"><br/></c:if>
												<strong>
													<span>- <c:out value="${ shop:viewOptionTextNoUl(option.optionName1) }"/></span> <c:out value="${shop:viewOptionTextNoUl(option.optionName2)}"/>
													<c:if test="${!empty option.extraPrice && option.extraPrice != 0}">
														(<c:out value="${op:numberFormat(option.extraPrice)}"/>원)
														<c:set var="optionPrice"><c:out value="${ optionPrice + option.extraPrice }"/></c:set>
													</c:if>

													<c:if test="${!empty option.optionCode}">
														(옵션번호 : <c:out value="${option.optionCode}"/>)
													</c:if>
												</strong>
											</c:forEach>
										</c:if>
										<c:if test="${ not empty item.openMarketOption }">
											<p>
												<strong>
													<c:if test="${item.setItemFlag != 'Y'}">-</c:if>
														<c:out value="${shop:viewItemOptions(item.setItemFlag, item.openMarketOption)}"/>
												</strong>
											</p>
										</c:if>
									</td>
									<td><c:out value="${ item.itemUserCode }"/></td>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.quantity) }"/></td>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.itemPrice + list.sumDeliveryPrice) }"/></td><%--itemPrice=상품판매가+옵션가격*수량 --%>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.itemCouponDiscountAmount) }"/></td>
									<td class="border_left number"><c:out value="${ op:numberFormat(item.vendorAddDiscountAmount)}  "/></td>
									<td class="border_left number" style="background-color: #f2f7f4"><c:out value="${ op:numberFormat(item.totalItemPrice) }"/></td>

									<c:if test="${itemIndex.first == true}">
										<c:if test="${sellerIdForParam == null}">
											<td class="border_left number" rowspan="${fn:escapeXml(rowspan)}" style="background-color: #f2f7f4"><c:out value="${ op:numberFormat(item.orderShipping)}"/></td>
										</c:if>
										<td class="border_left number" rowspan="${fn:escapeXml(rowspan)}" style="background-color: #f2f7f4"><c:out value="${op:numberFormat(item.subTotal)}"/></td>
									</c:if>

									<c:set var="cancelTotalAmount"><c:out value="${ cancelTotalAmount + item.totalItemPrice }"/></c:set>
									<c:set var="cancelTotalDeliveryAmount"><c:out value="${ cancelTotalDeliveryAmount + item.orderShipping }"/></c:set>
								</c:forEach>

								<c:set var="index"><c:out value="${ index + 1 }"/></c:set>
								<c:set var="cancelIndex"><c:out value="${ cancelIndex + 1 }"/></c:set>
							</c:if>
						</c:forEach>

						<c:if test="${ cancelIndex > 0 }">
							<c:if test="${ true }"></tr><tr></c:if>
							<td class="cancel_color" colspan="9" style="text-align:right; padding:5px;">취소총합</td> <!-- 취소총합 -->
							<td class="cancel_color number "><c:out value="${ op:numberFormat(cancelTotalAmount) }"/></td>
							<c:if test="${sellerIdForParam == null}">
								<td class="cancel_color number "><c:out value="${ op:numberFormat(cancelTotalDeliveryAmount) }"/></td>
							</c:if>
							<td class="cancel_color number "><c:out value="${ op:numberFormat(cancelTotalAmount + cancelTotalDeliveryAmount) }"/></td>
						</c:if>
					</tr>
					<tr>
						<td class="total-price-title" colspan="${ sellerIdForParam == null ? 12:11 }" style="text-align:right; padding:5px;"><c:out value="${ op:date(base.key) }"/> 소계</td>
						<td class="total-price-title number"><c:out value="${ op:numberFormat((payTotalAmount + payTotalDeliveryAmount) - ((cancelTotalAmount + cancelTotalDeliveryAmount)  *  nagativeNumber    ))}"/></td>
					</tr>

					<c:set var="totalAmount"><c:out value="${ totalAmount + ((payTotalAmount + payTotalDeliveryAmount) - ((cancelTotalAmount + cancelTotalDeliveryAmount)  *  nagativeNumber)) }"/></c:set>

				</c:forEach>
				<c:if test="${!empty orderList }">
					<tr>
						<td class="grand-total" colspan="${ sellerIdForParam == null ? 13:12 }" style="text-align:right; padding:5px;">합계</td>
						<td class="grand-total number"><c:out value="${ op:numberFormat(totalAmount) }"/></td>
					</tr>
				</c:if>
			</tbody>
		</table>
		<c:if test="${empty orderList}">
			<div class="no_data">
				<c:out value="${op:message('M00473')}"/><!-- 데이터가 없습니다. -->
			</div>
		</c:if>
	</div>
	<a href="#" class="popup_close"><c:out value="${op:message('M00353')}"/></a> <!-- 창 닫기 -->
</div>



<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
	});

	function sellerSeller(sellerId) {
		$('#sellerId').val(sellerId)
	}
</script>
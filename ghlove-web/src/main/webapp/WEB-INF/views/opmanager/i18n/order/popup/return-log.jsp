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
</style>

<div class="popup_wrap">
	<h1 class="popup_title">반품 로그</h1>
	<div class="popup_contents">

		<table class="inner-table">
			<caption><c:out value="${op:message('M00059')}"/></caption>
			<!-- 주문정보 -->
			<colgroup>
				<col style="width: 80px" />
				<col />
				<col style="width: 120px" />
				<col style="width: 120px" />
				<col style="width: 100px" />
				<col style="width: 120px" />
				<col style="width: 120px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="col" class="none_left">이미지</th>
					<th scope="col" class="none_left">상품정보</th>
					<th scope="col" class="none_left">구분</th>
					<th scope="col" class="none_left">클레임수량</th>
					<th scope="col" class="none_left">환불금액(상품)</th>
					<th scope="col" class="none_left">상태</th>
					<th scope="col" class="none_left">신청일</th>
				</tr>
			</thead>
			<tbody>

				<c:set var="orderItem" value="${apply.orderItem}" />
				<tr>
					<td>
						<img src="${fn:escapeXml(orderItem.imageSrc)}" alt="${fn:escapeXml(orderItem.itemName)}" width="100%"/>
					</td>
					<td>
						[<c:out value="${orderItem.itemUserCode}"/>] <c:out value="${orderItem.itemName}"/>
						<c:if test="${!empty orderItem.options}">
							<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
						</c:if>

						<c:if test="${not empty orderItem.textOption}">		 			<!-- 20260325 필수 추가정보 -->
							${shop:viewItemTextOption(orderItem.textOption)}
						</c:if>
					</td>
					<td class="text-center">
						<c:choose>
							<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
							<c:otherwise>
								<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.sellerName}"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="text-right">
						<c:out value="${op:numberFormat(apply.claimApplyQuantity)}"/>개
					</td>
					<td class="text-right">
						<c:out value="${op:numberFormat(apply.claimApplyAmount)}"/>원
					</td>
					<td class="text-center">
						<c:out value="${apply.claimStatusLabel}"/>
					</td>
					<td class="text-center">
						<c:out value="${op:datetime(apply.createdDate)}"/>
					</td>
				</tr>


				<tr>
					<td colspan="7">
						<table class="inner-table">
							<colgroup>
								<col style="width:15%" />
								<col style="width:35%"/>
								<col style="width:15%" />
								<col style="width:35%"/>
							</colgroup>
							<tbody>
								<tr>
									<th>신청사유</th>
									<td>
										<p>
											<c:choose>
												<c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
												<c:otherwise>[판매자 신청] </c:otherwise>
											</c:choose>

											<c:choose>
												<c:when test="${apply.returnReason == '2'}">고객 사유</c:when>
												<c:when test="${apply.returnReason == '1'}">판매자 사유</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</p>
										<p class="mt5"><c:out value="${apply.returnReasonText}"/></p>
										<p><c:out value="${apply.returnReasonDetail}"/></p>
									</td>
                            		<c:if test="${orderItem.campaignCode == 'MOBILE'}">
                            			<th>반품 정보</th>
										<td>
											[<c:out value="${apply.returnShippingCompanyName}"/>] <c:out value="${apply.returnShippingNumber}"/>
										</td>
                            		</c:if>
                            		<c:if test="${orderItem.campaignCode != 'MOBILE'}">
										<th>반품 송장 정보</th>
										<td>
											<c:choose>
												<c:when test="${apply.returnShippingAskType == '1'}">지정택배사</c:when>
												<c:otherwise>직접발송</c:otherwise>
											</c:choose>
											[<c:out value="${apply.returnShippingCompanyName}"/>] <c:out value="${apply.returnShippingNumber}"/>
										</td>
									</c:if>
								</tr>
								<tr>
									<!-- <th>회수 요청지 주소</th> -->
									<th>회수 요청지</th>
									<td colspan="3">
                            			<c:if test="${orderItem.campaignCode != 'MOBILE'}">
											[<c:out value="${apply.returnReserveName}"/>]<br/>
											(<c:out value="${apply.returnReserveZipcode}"/>) <c:out value="${apply.returnReserveAddress}"/> <c:out value="${apply.returnReserveAddress2}"/> <br/>
										</c:if>
										<c:out value="${apply.returnReserveMobile}"/>
									</td>
								</tr>
								<c:if test="${not empty apply.returnRefusalReasonText}">
									<tr>
										<th class="text-center">거절사유</th>
										<td colspan="3">
											<c:out value="${apply.returnRefusalReasonText}"/>
										</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>

	<a href="#" class="popup_close">창 닫기</a>
</div>
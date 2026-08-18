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
        <div id="pop_header">
            <h1 class="popup_title">교환 로그</h1>
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

        <div class="popup_contents">
            <div class="board_list">
                <table class="board_list_table" summary="교환 로그">
                    <caption>교환 로그</caption>
                    <colgroup>
                        <col style="width:150px;">
                        <col style="width:500px;">
                        <col style="width:150px;">
                        <col style="width:100px;">
                        <%-- <col style="width:200px;"> --%>
                        <col style="width:200px;">
                        <col style="width:200px;">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>이미지</th>
                            <th>답례품정보</th>
                            <th>상호명</th>
                            <th>수량</th>
                            <!-- <th>환불금액(상품)</th> -->
                            <th>상태</th>
                            <th>교환 신청일</th>
                        </tr>
                    </thead>
                    <tbody class="sortable">
						<c:set var="orderItem" value="${apply.orderItem}" />
                        <tr>
                            <!-- 이미지 -->
                            <td>
                                <div>
                                    <img src="${fn:escapeXml(orderItem.imageSrc)}" class="item_image" alt="${fn:escapeXml(orderItem.itemName)}" />
                                </div>
                            </td>
                            <!-- // 이미지 -->
                            <!-- 상품정보 -->
                            <td class="left break-word">
                                <div>
                                    <!-- <a href="" class="break-word"> -->
                                        <div>[<c:out value="${orderItem.itemUserCode}"/>]</div>
                                        <div><c:out value="${orderItem.itemName}"/></div>
                                        <c:if test="${!empty orderItem.options}">
                                        	<div><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></div>
                                        </c:if>

                                         <c:if test="${not empty orderItem.textOption}"> 			<!-- 20260325 필수 추가정보 -->
											${shop:viewItemTextOption(orderItem.textOption)}
										</c:if>
                                    <!-- </a> -->
                                </div>
                            </td>
                            <!-- // 상품정보 -->
                            <!-- 상호명 -->
                            <td>
                                <div>
                                	<c:choose>
										<c:when test="${shop:sellerId() == orderItem.sellerId}">
											자사
										</c:when>
										<c:otherwise>
											<c:out value="${orderItem.sellerName}"/>
										</c:otherwise>
									</c:choose>
                                </div>
                            </td>
                            <!-- // 상호명 -->
                            <!-- 수량 -->
                            <td>
                                <div><c:out value="${op:numberFormat(apply.claimApplyQuantity)}"/>개</div>
                            </td>
                            <!-- // 수량 -->
                            <!-- 환불금액(상품) -->
                            <!-- <td>
                                <div>18,000</div>
                            </td> -->
                            <!-- // 환불금액(상품) -->
                            <!-- 상태 -->
                            <td>
                                <div><c:out value="${apply.claimStatusLabel}"/></div>
                            </td>
                            <!-- // 상태 -->
                            <!-- 반품 신청일 -->
                            <td>
                                <div><c:out value="${op:date(apply.createdDate)}"/></div>
                                <div><c:out value="${op:timeFormat(apply.createdDate.substring(8))}"/></div>
                            </td>
                            <!-- // 반품 신청일 -->
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="board_write mt40">
                <table class="board_write_table" summary="">
                    <colgroup>
                        <col style="width:150px;">
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label">신청사유</td>
                            <td>
                                <div>
                                    <p>
                                    	<c:choose>
											<c:when test="${apply.claimApplySubject == '01'}">[구매자 신청] </c:when>
											<c:otherwise>[판매자 신청] </c:otherwise>
										</c:choose>
                                    	<c:choose>
											<c:when test="${apply.exchangeReason == '2'}">고객 사유</c:when>
											<c:when test="${apply.exchangeReason == '1'}">판매자 사유</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
                                   	</p>
                                    <p><c:out value="${apply.exchangeReasonText}"/></p>
                                    <p><c:out value="${apply.exchangeReasonDetail}"/></p>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        	<c:if test="${apply.orderItem.campaignCode != 'MOBILE'}">
	                            <td class="label">반품 송장 번호</td>
	                            <td>
	                                <div>
	                                    <%-- [<c:out value="${apply.exchangeShippingCompanyName}"/>] <c:out value="${apply.exchangeShippingNumber}"/> --%>
	                                    [<c:out value="${apply.exchangeShippingCompanyName}"/>] <c:out value="${apply.exchangeDeliveryNumber}"/>
	                                </div>
	                            </td>
                            </c:if>
                            <c:if test="${apply.orderItem.campaignCode == 'MOBILE'}">
	                            <td class="label">교환 정보</td>
	                            <td>
	                                <div>
	                                    [<c:out value="${apply.exchangeShippingCompanyName}"/>] <c:out value="${apply.exchangeDeliveryNumber}"/>
	                                </div>
	                            </td>
                            </c:if>
                        </tr>
                        <tr>
                            <!-- <td class="label">회수 요청지 주소</td> -->
                            <td class="label">회수 요청지</td>
                            <td>
                                <div>
                                	<c:if test="${apply.orderItem.campaignCode != 'MOBILE'}">
	                                    <p>[<c:out value="${apply.exchangeReceiveName}"/>]</p>
	                                    <p>(<c:out value="${apply.exchangeReceiveZipcode}"/>) <c:out value="${apply.exchangeReceiveAddress}"/> <c:out value="${apply.exchangeReceiveAddress2}"/></p>
                                    </c:if>
                                    <p><c:out value="${apply.exchangeReceiveMobile}"/></p>
                                </div>
                            </td>
                        </tr>
						<c:if test="${not empty apply.exchangeRefusalReasonText}">
							<tr>
								<th class="label">거절사유</th>
								<td>
	                                <div>
										<c:out value="${apply.exchangeRefusalReasonText}"/>
									</div>
								</td>
							</tr>
						</c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
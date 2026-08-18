<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>

<c:forEach items="${order.orderShippingInfos}" var="receiver" varStatus="receiverIndex">
	<input type="hidden" name="orderShippingInfos[${fn:escapeXml(receiverIndex.index)}].shippingInfoSequence" value="${fn:escapeXml(receiver.shippingInfoSequence)}" />
	<table class="board_write_table" ${viewType == 'info' ? 'style=""' : ''}>
		<caption>주문상품정보 - 주문자정보</caption>
		<colgroup>
			<col style="width:220px;">
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
								<li>${fn:escapeXml(receiver.receiveName)}</li>
								<li>
									<c:choose>
										<c:when test="${!empty receiver.receivePhone}"> 
											${fn:escapeXml(receiver.receivePhone)} / ${fn:escapeXml(receiver.receiveMobile)}
										</c:when>
										<c:otherwise>
											${fn:escapeXml(receiver.receiveMobile)}
										</c:otherwise>
									</c:choose>
								</li>
								<li>[${fn:escapeXml(receiver.receiveNewZipcode)}] ${fn:escapeXml(receiver.receiveAddress)} ${fn:escapeXml(receiver.receiveAddressDetail)}</li>
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
											<c:out value="${receiver.receiveName}"/>
										</div>
									</td>
								</tr>
								<tr>
									<th class="label">전화번호</th>
									<td>
										<div>
											<c:out value="${receiver.receivePhone1}"/>-<c:out value="${receiver.receivePhone2}"/>-<c:out value="${receiver.receivePhone3}"/>
										</div>
									</td>
									<th class="label">핸드폰번호</th>
									<td>
										<div>
											<c:out value="${receiver.receiveMobile1}"/>-<c:out value="${receiver.receiveMobile2}"/>-<c:out value="${receiver.receiveMobile3}"/>
										</div>
									</td>
								</tr>
								<tr>
									<th class="label">주소</th>
									<td colspan="3">
										<div>
											<c:out value="${receiver.receiveZipcode}" /><br/>
											<c:out value="${receiver.receiveAddress}" /><br/>
											<c:out value="${receiver.receiveAddressDetail}" />
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
						<td class="label" scope="row">배송시 요구사항</td>
						<td>
							<div>
								${fn:escapeXml(receiver.memo)}
							</div>
						</td>
					</tr>
				</c:if>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="label" scope="row">배송시 요구사항</td>
					<td>
						<div>
							${fn:escapeXml(receiver.memo)}
						</div>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
		</tbody>
	</table>
</c:forEach>
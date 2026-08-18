<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<h2><span>기본정보</span></h2>

<table cellpadding="0" cellspacing="0" summary="" class="board_list_table mb30">
	<caption>기본정보</caption>
	<colgroup>
		<col style="width:120px;" />
		<col style="width:*;" />
		<col style="width:120px;" />
		<col style="width:*;" />
	</colgroup>
	<tbody>
		<tr>
			<th scope="row">아이디</th>
			<td class="tleft">
				<div><c:out value="${user.loginId}"/> / <c:out value="${user.userDetail.userlevel.levelName}"/></div>
			</td>
			<th scope="row">이름</th>
			<td class="tleft">
				<div>
					<c:out value="${user.userName}"/>
				</div>
			</td>
		</tr>
		<tr>
			<th scope="row">성별</th>
			<td class="tleft">
				<div><c:out value="${user.userDetail.gender == 'M' ? '남자' : '여자'}"/></div>
			</td>
			<th scope="row">생년월일</th>
			<td class="tleft">
				<div>
					(<c:out value="${user.userDetail.birthdayType == '1' ? '양력' : '음력'}"/>) <c:out value="${user.userDetail.birthday}"/>
				</div>
			</td>
		</tr>
		<tr>
			<th scope="row">휴대폰 번호</th>
			<td class="tleft">
				<div>
					<c:out value="${user.userDetail.phoneNumber}"/>
				</div>
			</td>
			<th scope="row">이메일</th>
			<td class="tleft">
				<div><c:out value="${user.email}"/></div>
			</td>
		</tr>
		<tr>
			<th scope="row">주소</th>
			<td class="tleft" colspan="3">
				<div>
					(<c:out value="${empty user.userDetail.newPost ? user.userDetail.post : user.userDetail.newPost}"/>)
					&nbsp;<c:out value="${user.userDetail.address}"/>&nbsp;<c:out value="${user.userDetail.addressDetail}"/>
				</div>
			</td>
		</tr>
		<tr>
			<th scope="row">회원가입일</th>
			<td class="tleft">
				<div><c:out value=""/>${op:datetime(user.createdDate)}</div>
			</td>
			<th scope="row">마지막로그인</th>
			<td class="tleft">
				<div><c:out value="${op:datetime(user.loginDate)}"/> (login : <c:out value="${user.loginCount}"/>회 방문)</div>
			</td>
		</tr>
	</tbody>
</table> <!-- // 기본정보 끝 -->

<h2><span>최근 주문내역</span></h2>
<table class="board_list_table" summary="주문내역 리스트">
	<caption>주문내역 리스트</caption>
	<colgroup>
		<col style="width:5%;" />
		<col style="width:10%;" />
		<col style="width:7%;" />
		<col style="width:7%;" />
		<col style="width:7%;" />
		<col />
		<col style="width:10%;" />
		<col style="width:10%;" />
		<col style="width:10%;" />
		<col style="width:10%;" />
	</colgroup>
	<thead>
		<tr>
			<th scope="col">주문번호</th>
			<th scope="col">주문일자</th>
			<th scope="col">수취인</th>
			<th scope="col">판매자</th>
			<th scope="col" colspan="2">상품정보</th>
			<th scope="col">수량</th>
			<th scope="col">판매금액</th>
			<th scope="col">주문상태</th>
			<th scope="col">배송정보</th>
		</tr>

	</thead>
	<tbody>
		<c:forEach items="${ orderList }" var="order" varStatus="index">
			<c:forEach items="${order.orderShippingInfos}" var="info">
				<c:forEach items="${info.orderItems}" var="orderItem" varStatus="itemIndex">

					<tr>
						<td>
							<a href="javascript:goUrl('/opmanager/order/view/order-detail/${fn:escapeXml(order.orderSequence)}/${fn:escapeXml(orderItem.orderCode)}')"><c:out value="${orderItem.orderCode}"/></a><br />
						</td>
						<td>
							<c:out value="${op:datetime(order.createdDate)}"/>
						</td>
						<td><c:out value="${info.receiveName}"/></td>
						<td>
							<c:choose>
								<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.sellerName}"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<img src="${shop:loadImageBySrc(orderItem.imageSrc, 'XS')}" alt="${fn:escapeXml(orderItem.itemName)}" width="100%"/>
						</td>
						<td class="text-left">
							<c:out value="${orderItem.itemName}"/> [<c:out value="${orderItem.itemUserCode}"/>]
							<c:if test="${!empty orderItem.options}">
								<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
							</c:if>
						</td>
						<td class="text-right"><strong><c:out value="${orderItem.quantity}"/>개</strong></td>
						<td class="text-right"><c:out value="${op:numberFormat(orderItem.saleAmount)}"/>원</td>
						<td><c:out value="${orderItem.orderStatusLabel}"/></td>
						<td>
							<c:if test="${orderItem.shippingDate != '00000000000000'}">
								<c:choose>
									<c:when test="${empty orderItem.deliveryNumber}">
										직접수령
									</c:when>
									<c:otherwise>
										송장 번호 : <c:out value="${orderItem.deliveryNumber}(${orderItem.deliveryCompanyName}"/>)
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</c:forEach>
	</tbody>
</table>
<c:if test="${empty orderList}">
<div class="no_content">
	<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
</div>
</c:if>

<script type="text/javascript">
$(function() {
	Manager.activeUserDetails("details");
});

function goUrl(url) {
	opener.location.href = url;
}

</script>

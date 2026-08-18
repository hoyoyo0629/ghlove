<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<table class="board_list_table" summary="포인트 내역">
	<caption>포인트 내역</caption>
	<colgroup>
		<col style="width:3%;">
		<col style="width:10%;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">No.</th>
			<th scope="col">발생일</th>
			<th scope="col">지자체</th>
			<th scope="col">기부금액</th>
			<th scope="col">발생포인트</th>
			<th scope="col">사용포인트</th>
			<th scope="col">포인트잔액</th>
			<th scope="col">답례품 주문번호</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty list}">
				<c:forEach items="${list}" var="item" varStatus="i">
					<tr style="background:#fff;">
						<td><c:out value="${pagination.itemNumber - i.count}"/></td>
						<td><c:out value="${item.cntrDe}"/></td>
						<td><c:out value="${item.cntrUpperLocgovNm}"/>&nbsp;<c:out value="${item.cntrLocgovNm}"/></td>
						<c:choose>
							<c:when test="${item.pointType eq 'OCC'}">
								<td><fmt:formatNumber value="${fn:escapeXml(item.cntrAmt)}" pattern="#,###.##"/></td>
								<td><fmt:formatNumber value="${fn:escapeXml(item.cntrPoint)}" pattern="#,###.##"/></td>
								<td>-</td>
								<td><fmt:formatNumber value="${fn:escapeXml(item.cntrBlcePoint)}" pattern="#,###.##"/></td>
							</c:when>
							<c:otherwise>
								<td>-</td>
								<td>-</td>
								<td><fmt:formatNumber value="${fn:escapeXml(item.cntrUsePoint)}" pattern="#,###.##"/></td>
								<td><fmt:formatNumber value="${fn:escapeXml(item.cntrBlcePoint)}" pattern="#,###.##"/></td>
							</c:otherwise>
						</c:choose>
						<td>
							<c:if test="${not empty item.orderCode and item.orderCode ne '0'}">
								<a href="javascript:goPresentOrderPage('${fn:escapeXml(item.orderCode)}')"><c:out value="${fn:escapeXml(item.orderCode)}"/></a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="8">포인트 내역이 존재하지 않습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<c:if test="${not empty list}">
	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</c:if>

<script type="text/javascript">
	/**
	 *	함 수 명 : goPresentOrderPage
	 *	기	능  : 답례품 주문 정보 페이지로 이동
	 *	파라미터  : orderCode - 주문번호
	 */
	function goPresentOrderPage(orderCode) {
		if(orderCode) {
			location.href = '/opmanager/order/all/order-detail/0/' + orderCode;

			/*$.get('/opmanager/present/login', function(response) {
				if (response.isSuccess) {
					var param = (response.data == null) ? '' : response.data;

					var url = "";
					if (window.location.hostname.indexOf("ilovegohyang.go.kr") > -1) {
						url = "https://shop.ilovegohyang.go.kr/kwa-ABS_sell_v-"+orderCode+"?OTSKIN=layout_bl_admin.php";
					} else {
						url = "https://gohyang01.bbiz.kr/kwa-ABS_sell_v-"+orderCode+"?OTSKIN=layout_bl_admin.php";
					}
					window.open(url + '&_tss_=' + param, 'presentPopup');
				} else {
					alert("오류가 발생했습니다.");
				}
			});*/
		}
	}
</script>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


<h2><span><c:out value="${op:message('M01571')}"/> <!-- 나의 배송지 관리 --></span></h2>
<form method="post" id="listForm">
	<input type="hidden" name="userId" value="${fn:escapeXml(userId)}" />
	<div class="board_write">
		<table class="board_list_table">
			<colgroup>
				<col style="width:5%;">
				<col style="width:15%;">
				<col style="width:15%;">
				<col style="width:*;">
				<col style="width:15%;">
				<col style="width:15%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col"><c:out value="${op:message('M00431')}"/></th> <!-- 선택 -->
 					<th scope="col"><c:out value="${op:message('M01569')}"/></th> <!-- 배송지 -->
 					<th scope="col"><c:out value="${op:message('M01570')}"/></th> <!-- 받는사람 -->
 					<th scope="col"><c:out value="${op:message('M00118')}"/></th> <!-- 주소 -->
 					<th scope="col"><c:out value="${op:message('M00016')}"/></th> <!-- 전화번호 -->
 					<th scope="col"><c:out value="${op:message('M00329')}"/></th> <!-- 휴대전화번호 -->
				</tr>

			</thead>
			<tbody>
				<c:forEach items="${ list }" var="item">
					<tr>
						<td id="delivery-info-${fn:escapeXml(item.userDeliveryId)}">
							<input type="radio" name="userDeliveryId" value="${fn:escapeXml(item.userDeliveryId)}" />
						</td>
						<td>
							<c:out value="${ item.title }"/>
							<c:if test="${ item.defaultFlag eq 'Y' }">
								(<c:out value="${op:message('M01580')}"/>) <!-- 기본 -->
							</c:if>
						</td>
						<td><c:out value="${ item.userName }"/></td>
						<td class="left"><c:out value="${ item.address } "/><c:out value="${ item.addressDetail }"/></td>
						<td><c:out value="${ item.phone }"/></td>
						<td><c:out value="${ item.mobile }"/></td>
					</tr>
				</c:forEach>
				<c:if test="${empty list}">
					<tr class="no_content">
						<td colspan="6"><c:out value="${op:message('M00473')} "/><!-- 데이터가 없습니다. --></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div> <!-- // board_write -->
</form>
<div class="btn_all">
	<div class="btn_left">
		<button type="button" class="btn btn-default btn-sm" onclick="editDelivery()">v <c:out value="${op:message('M00087')}"/></button> <!-- 수정 -->
		<button type="button" class="btn btn-default btn-sm" onclick="deleteAction()">x <c:out value="${op:message('M00074')}"/></button> <!-- 삭제 -->
	</div>
	<div class="btn_right">
		<button type="button" class="btn btn-active btn-sm" onclick="setDefaultAddr()"><c:out value="${op:message('M01572')}"/></button>  <!-- 기본 배송지로 설정 -->
		<button type="button" class="btn btn-active btn-sm" onclick="location.href='/opmanager/user/popup/delivery-write/${fn:escapeXml(userId)}'"><span class="glyphicon glyphicon-plus"></span> <c:out value="${op:message('M01573')}"/></button> <!-- 배송지 추가 -->
	</div>
</div>	<!--// btn_all E-->

<script type="text/javascript">
$(function() {
	Manager.activeUserDetails("delivery");
});

function deleteAction() {
	$radio = $('#listForm').find('input[name="userDeliveryId"]:checked');
	if($radio.size() == 0) {
		alert(Message.get("M01574"));	// 삭제하실 배송지를 선택해 주세요.
		return;
	}

	if (!confirm(Message.get("M01575"))) {	// 선택하신 배송지를 삭제 하시겠습니까?
		return;
	}

	$('#listForm').attr('action', '/opmanager/user/popup/delivery-list-action/del').submit();
}

function editDelivery() {
	$radio = $('#listForm').find('input[name="userDeliveryId"]:checked');
	if($radio.size() == 0) {
		alert(Message.get("M01577"));	// 수정하실 배송지를 선택해 주세요.
		return;
	}

	location.href = '/opmanager/user/popup/delivery-edit/${fn:escapeXml(userId)}/' + $radio.val();
}

function setDefaultAddr() {
	$radio = $('#listForm').find('input[name="userDeliveryId"]:checked');
	if($radio.size() == 0) {
		alert(Message.get("M01576"));	// 기본배송지로 설정하실 배송지를 선택해주세요.
		return;
	}

	if (!confirm(Message.get("M01578"))) {	// 선택하신 배송지를 기본 배송지로 설정 하시겠습니까?
		return;
	}

	$('#listForm').attr('action', '/opmanager/user/popup/delivery-list-action/mod').submit();
}
</script>
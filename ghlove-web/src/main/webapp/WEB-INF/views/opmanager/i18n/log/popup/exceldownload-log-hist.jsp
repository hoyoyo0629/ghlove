<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ page import="saleson.common.Const" %>

<!-- List -->
<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">엑셀 다운로드 사유 수정 이력 관리</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
	<!-- List Header -->
	<div class="count_title mt-40">
		<h5>총 <fmt:formatNumber value="${fn:escapeXml(count)}" pattern="#,###"/> 건</h5>
	</div>
	<!--// List Header -->

	<table class="board_list_table" summary="엑셀 다운로드 사유 수정 이력 관리">
		<colgroup>
			<col style="width:50px;">
			<col style="width:150px;">
			<col style="width:100px;">
			<col style="width:150px;">
			<col style="width:250px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">등록일</th>
				<th scope="col">관리자ID</th>
				<th scope="col">사유타입</th>
				<th scope="col">사유</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><div><c:out value="${pagination.itemNumber - i.count}"/></div></td>
							<td><div><c:out value="${item.createdAt}"/></div></td>
							<td><div><c:out value="${item.loginId}"/></div></td>
							<td><div><c:out value="${item.reasonType}"/></div></td>
							<td><div><c:out value="${item.reason}"/></div></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="4">엑셀 다운로드 사유 수정 이력 정보가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<div class="pagination-wrap">
		<page:pagination-manager />
 	</div>
	<p class="popup_btns">
		<button type="button" class="btn btn-active" style="margin-bottom:20px;" onclick="self.close();">닫기</button>
	</p>
</div>
<!-- List -->

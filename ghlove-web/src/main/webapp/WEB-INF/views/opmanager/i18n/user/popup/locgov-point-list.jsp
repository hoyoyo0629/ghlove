<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">포인트 지급률 변경이력</h1>
		<a href="javascript:self.close();" class="btn_close">
			<img src="/content/opmanager/images/btn/btn_close.png" alt="닫기">
		</a>
	</div>
	<div class="popup_contents">
		<div class="count_title">
			<span class="flex_box juc-right">
				<select id="stdrYear" title="" class="wd-150" onchange="changeStdrYear();">
					<option value="" selected="selected">-전체-</option>
					<c:forEach items="${yearCodeList}" var="code" varStatus="i">
						<option value="${fn:escapeXml(code.id)}" <c:if test="${code.id eq searchParam.stdrYear}">selected</c:if>><c:out value="${fn:escapeXml(code.label)}"/>년</option>
					</c:forEach>
				</select>
			</span>
		</div>
		<div class="board_list">
			<table class="board_list_table" summary="포인트 지급률 변경이력">
				<caption>포인트 지급률 변경이력</caption>
				<colgroup>
					<col style="width:10%;">
					<col style="width:20%;">
					<col style="width:20%;">
					<col style="width:20%;">
					<col style="width:20%;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">no</th>
						<th scope="col">변경일</th>
						<th scope="col">년도</th>
						<th scope="col">포인트률</th>
						<th scope="col">작성자</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty list}">
							<c:forEach items="${list}" var="item" varStatus="i">
								<tr style="background:#fff;">
									<td><c:out value="${pagination.itemNumber - i.count}"/></td>
									<td><c:out value="${item.lastUpdtPnttm}"/></td>
									<td><c:out value="${item.stdrYear}"/></td>
									<td><fmt:formatNumber value="${fn:escapeXml(item.pointRate)}" pattern="#.##"/></td>
									<td><c:out value="${item.lastUpdusrName}"/></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr style="background:#fff;">
								<td colspan="5">포인트 지급률 변경이력이 존재하지 않습니다.</td>
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
		</div>

		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="self.close();">확인</button>
		</p>
	</div>
</div>

<script type="text/javascript">

	/**
	 *	함 수 명 : changeStdrYear
	 *	기	능  : 년도 변경
	 */
	function changeStdrYear() {
		var form = document.createElement("form");
		form.setAttribute("charset", "UTF-8");
		form.setAttribute("method", "GET");
		form.setAttribute("action", "/opmanager/user/locgov/popup/point/list/${fn:escapeXml(locgovCode)}");

		var hiddenField = document.createElement("input");
		hiddenField.setAttribute("type", "hidden");
		hiddenField.setAttribute("name", "stdrYear");
		hiddenField.setAttribute("value", $("#stdrYear").val());
		form.appendChild(hiddenField);

		document.body.appendChild(form);
		form.submit();
	}
</script>

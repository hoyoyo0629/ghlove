<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<!-- List Header -->
<div class="count_title mt-40">
	<h5>총 <fmt:formatNumber value="${fn:escapeXml(count)}" pattern="#,###"/>건</h5>
	<span>
		<select name="displayCount" id="displayCount" title="${op:message('M00239')} ">
			<option value="10"><c:out value="${op:message('M00240')}"/></option>
			<option value="20"><c:out value="${op:message('M00241')}"/></option>
			<option value="50"><c:out value="${op:message('M00242')}"/></option>
			<option value="100"><c:out value="${op:message('M00243')}"/></option>
		</select>
	</span>
</div>
<!--// List Header -->

<!-- List -->
<table class="board_list_table" summary="사용자 로그관리">
	<caption>사용자 로그관리</caption>
	<colgroup>
		<col style="width:50px;">
		<col style="width:200px;">
		<col style="width:500px;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">No.</th>
			<th scope="col">접속일</th>
			<th scope="col">URL</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty list}">
				<c:forEach items="${list}" var="item" varStatus="i">
					<tr style="background:#fff;">
						<td>
							<div><c:out value="${pagination.itemNumber - i.count}"/></div>
						</td>
						<td>
							<div><c:out value="${item.createdDate}"/></div>
						</td>
						<td>
							<div><c:out value="${item.requestUri}"/></div>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr style="background:#fff;">
					<td colspan="3">관리자 로그관리 상세 내용이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<div class="btn_all btn_right">
	<div class="flex_box gap-08">
		<button type="button" class="btn btn-default btn-mini" onclick="moveListPage();">목록</button>
	</div>
</div>
<div class="pagination-wrap">
	<c:if test="${not empty list}">
		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>
	</c:if>
</div>
<!--// List -->

<script type="text/javascript">
	$(function() {

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();
	});

	/**
	 *	함 수 명 : displayChange
	 *	기	능  : 한 페이지 출력 갯수 변경 이벤트
	 */
	function displayChange() {
		$("#displayCount").on('change', function(){
			getActionLogList(1, $(this).val());
		});
	}

	/**
	 *	함 수 명 : displaySelected
	 *	기	능  : 선택된 페이지 출력 갯수 셋팅
	 */
	function displaySelected(){
		$("#displayCount").val("${fn:escapeXml(actionLogParam.itemsPerPage)}");
	}
</script>
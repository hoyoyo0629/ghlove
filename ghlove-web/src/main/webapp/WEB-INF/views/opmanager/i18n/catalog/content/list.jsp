<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<style>
.tbl-summary {
	border-right: 1px solid #dedede;
	border-bottom: 1px solid #dedede;
}
.tbl-summary th,
.tbl-summary td {
	border-left: 1px solid #dedede;
	border-top: 1px solid #dedede;
	text-align: center;
}

.tbl-summary th {
	background: #f4f4f4;
	padding: 10px;
	color: #000;
}
.tbl-summary td {
	padding: 0 10px;
	font-family: verdana;
}
.tbl-summary td {
	padding: 22px;
	font-size: 20px;
	font-family: verdana;

}
</style>

<!-- <div class="admin_wrap"> -->
<div>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

	<div class="item_list">
		<h3><span>컨텐츠목록</span></h3>

		<form:form modelAttribute="catalogContentMngParam" method="post">

				<%-- 검색조건 --%>
				<div class="board_write">
				<table class="board_write_table" summary="목록">
					<caption>컨텐츠목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
	                    <tr>
	                        <td class="label">발간호</td>
	                        <td>
	                            <div class="flex_box gap-08">
				                	<form:select path="catalogYear" title="년도" class="wd-150">
					                		<form:option value="0" label="전체" />
					                	<c:forEach items="${ catalogYearList }" var="catalog">
				                        	<form:option value="${fn:escapeXml(catalog.catalogYear)}" label="${fn:escapeXml(catalog.catalogYear)}" />
				                    	</c:forEach>
				                    </form:select>
				                </div>
	                        </td>
	                    </tr>
					</tbody>
				</table>

				<%-- 검색버튼 --%>
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/catalog/content/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:fnSearch();"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>

			<div class="count_title mt-40">
			<%-- 총 건수 --%>
			<h5>
				<c:out value="${op:message('M00045')}"/> <%-- 총 --%>
				<c:out value="${op:numberFormat(catalogContentMngParam.pagination.totalItems)}"/>
				<c:out value="${op:message('M00272')}"/> <%-- 건 --%>
			</h5>

			<%-- 출력개수 --%>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
 						onchange="$('form#catalogContentMngParam').submit();"> <!-- 화면 출력수 -->
 																		<!-- 개 출력 -->
 						<form:option value="10" label="10${op:message('M00053')}" />
 						<form:option value="50" label="50${op:message('M00053')}" />
 						<form:option value="100" label="100${op:message('M00053')}" />
 						<form:option value="200" label="200${op:message('M00053')}" />
 						<form:option value="500" label="500${op:message('M00053')}" />
					</form:select>
				</span>
			</div>
			</form:form>

			<%-- 출력리스트 --%>
			<div class="board_list">

				<form id="listForm" action="">
				<input type="hidden" id="rowNumber" value="${fn:escapeXml(catalogContentMng.rowNumber)}">
					<table class="board_list_table" summary="컨텐츠 목록">
					<caption>컨텐츠 목록</caption>
					<colgroup>
						<col style="width:5%;">
		                <col style="width:30%;">
		                <col style="width:5%;">
		                <col style="width:10%;">
		                <col style="width:15%;">
		                <col style="width:20%;">
		                <col style="width:5%;">
		                <col style="width:15%;">
					</colgroup>
					<thead>
						<tr>
							<th>No.</th>
							<th>제목</th>
							<th>공개여부</th>
							<th>발간호</th>
							<th>구분</th>
							<th>세부 컨텐츠</th>
							<th>배너</th>
							<th>등록일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ catalogContentList }" var="catalog" varStatus="index">
						<tr>
							<td>
								<c:out value="${pagination.itemNumber - index.count}"/>
							</td>
							<td>
								<a href="${fn:escapeXml(requestContext.managerUri)}/catalog/content/form/${fn:escapeXml(catalog.catalogContentId)}">
								    <c:out value="${catalog.catalogContentSubject}"/>
								</a>
							</td>
							<td>${fn:escapeXml(catalog.displayYn)}</td>
							<td>${fn:escapeXml(catalog.catalogYear)} - ${fn:escapeXml(catalog.catalogNo)}</td>
							<td>${fn:escapeXml(catalog.contentType)}</td>
							<td>${fn:escapeXml(catalog.contentSubType)}</td>
							<td>${fn:escapeXml(catalog.bannerYn)}</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd" value="${fn:escapeXml(catalog.frstRegistPnttm)}"/></td>
						</tr>
						</c:forEach>
					</tbody>
					</table>
				</form>

				<%-- 등록 버튼 --%>
                	<div class="btn_all btn_right">
                		<div class="flex_box gap-08">
	                        <a href="/opmanager/catalog/content/form" class="btn btn-dark-gray btn-mini">등록</a>
                        </div>
                	</div>
			</div>

			<%-- 페이징 --%>
			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>

	</div>
</div>

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}
</style>

<script type="text/javascript">
// 컨텐츠 목록 검색
function fnSearch() {
	var catalogYear = $("#catalogYear").val();
	var catalogNo = $("#catalogNo").val();

	$("#catalog").submit();
}

// 컨텐츠 목록 등록
function fnInsertCatalogMng(rowNumber) {
    Common.popup('/opmanager/catalog/create', 'createCatalog', 800, 600, 1);
}

// 컨텐츠 상세 보기
function catalogDetail(catalogContentId) {
	var paramUrl = '/opmanager/catalog/content/form?catalogContentId=' + catalogContentId

}


</script>


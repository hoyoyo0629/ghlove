<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<h3><span>카드뉴스 목록</span></h3>
	
		<form:form modelAttribute="cardNewsParam" method="post">
				
			<div class="board_write">
				<table class="board_write_table" summary="카드뉴스 목록">
					<caption>카드뉴스 목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
						<tr>
							<td class="label">제목</td>
							<td>
								<div>
									<form:input path="searchKeyword" class="input_txt wd-500" title="제목 검색"/><!-- 검색어 -->
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">등록일</td>
							<td>
								<div>
									<span class="datepicker"><form:input path="startDate" maxlength="8" class="datepicker" title="등록 시작일" /></span>
									<span class="wave">~</span>
									<span class="datepicker mr10"><form:input path="endDate" maxlength="8" class="datepicker" title="등록 종료일" /></span>
									<span class="day_btns">
									<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
									<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
									<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
									<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
									<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
									<a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a>
			                       </span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
	
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/card-news/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>
				
			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(cardNewsParam.pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
				</h5>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
						onchange="$('form#cardNewsParam').submit();"> <!-- 화면 출력수 -->
						<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
					</form:select>
				</span>
			</div>
		</form:form>
		
		<div class="board_list">

			<form id="listForm" method="post">
				<table class="board_list_table" summary="카드뉴스 목록">
					<caption>특정사업기부 목록</caption>
					<colgroup>
						<col style="width:50px;">
						<col style="width:100px;">
		                <col style="width:800px;">
		                <col style="width:200px;">
		                <col style="width:200px;">
		                <col style="width:200px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="check_all" /></th>
							<th scope="col">ID</th>
							<th scope="col">제목</th>
							<th scope="col">발간호</th>
							<th scope="col">작성자</th>
							<th scope="col">등록일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ list }" var="cardNews" varStatus="index">
							<c:set var="key"><c:out value="${cardNews.cardNewsId}"/></c:set>
							<tr style="background:#fff;">
								<td>
									<input type="checkbox" name="id" value="${fn:escapeXml(key)}" />
								</td>
								<td>
									<c:out value="${op:numberFormat(key)}"/>
								</td>
								<td>
									<a href="${fn:escapeXml(requestContext.managerUri)}/catalog/card-news/form/${fn:escapeXml(cardNews.cardNewsId)}"><c:out value="${cardNews.cardNewsSubject}"/></a>
								</td>
								<td>
									<c:out value="${cardNews.catalogYearNo}"/>
								</td>
								<td>
									<c:out value="${cardNews.name}"/>
								</td>
								<td>
									<c:out value="${cardNews.frstRegistPnttmStr}"/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<c:if test="${empty list}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

            <div class="flex_box">
                <div class="btn_all btn_right" style="width:100%;">
                    <div class="flex_box gap-08">
	                    <button type="button" onclick="location.href='${fn:escapeXml(requestContext.managerUri)}/catalog/card-news/form'" class="btn btn-default btn-mini">신규등록</button>
                        <button type="button" onclick="updateListDataLabel('Y')" class="btn btn-default btn-mini">삭제</button>
                    </div>
                </div>
            </div>

			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>
		
		</div>
	
	</div>
</div>

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>
<script type="text/javascript">

	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});


    function updateListDataLabel(flag) {
        let message = "정보를 삭제하시겠습니까?";
        if ($('#listForm').find('input[name=id]:checked').size() == 0) {
            alert("삭제할 항목을 선택해 주세요.");
            return;
        } else {
            Common.updateListData("/opmanager/catalog/card-news/list/update-label/" + flag, message);
        }
    }


	function checkDate(event) {
		let startDate = document.getElementById('startDate').value;
		let endDate = document.getElementById('endDate').value;
		let isDate = false;
		if (!startDate && !endDate) {
			return;
		}
		if (Common.validateDate(startDate) && Common.validateDate(endDate)) {
			if (Number(startDate) <= Number(endDate)) {
				isDate = true;
			}
		}

		if (!isDate) {
			event.preventDefault();
			alert('검색 등록일을 확인해주세요.');
		}
	}
	
	
</script>


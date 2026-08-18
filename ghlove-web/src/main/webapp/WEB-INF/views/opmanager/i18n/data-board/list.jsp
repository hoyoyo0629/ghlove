<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<div class="location">
		<a href="#"><c:out value="${op:message('MENU_5000')}"/></a> > <a href="#"></a> > <a href="#"><c:out value="${op:message('MENU_5110')}"/></a>
	</div>
	<h3><span><c:out value="${op:message('MENU_5110')}"/></span></h3>
	<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>
	<div class="board_write">
		<table class="board_write_table" summary="${op:message('MENU_5110')}">
			<colgroup>
				<col style="width:220px;">
			</colgroup>
			<tbody>
			 <tr>
			 	<td class="label"><c:out value="${op:message('M00011')}"/></td>
			 	<td>
			 		<div class="flex_box gap-08">
			 			<form:select path="where" class="wd-150">
<%-- 			 				<form:option value="">전체</form:option> --%>
			 				<form:option value="SUBJECT" label="${op:message('M00275')}" />
			 				<form:option value="CONTENT">내용</form:option>
			 				<%-- <form:option value="FILE_TY" label="${op:message('M01698')}" /> --%>
			 			</form:select>
			 			<form:input path="query" cssClass="input_txt required _filter half" title="${op:message('M00021')}"/>
					</div>
			 	</td>
			 </tr>


<%-- 			<tr>
				<td class="label">${op:message('M00275')}</td>
				<td>
					<div class="flex_box gap-08">
						<form:hidden path="where" value="SUBJECT"/>
						<form:input path="query" cssClass="input_txt required _filter half" title="${op:message('M00021')}"/>
					</div>
				</td>
			</tr> --%>
			<tr>
				<td class="label"><c:out value="${op:message('M00202')}"/> </td>
				<td>
					<div class="search-date">
						<span class="datepicker"><form:input path="startCreateDate" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
						<span class="wave">~</span>
						<span class="datepicker"><form:input path="endCreateDate" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>

						<span class="day_btns">
							<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
							<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
							<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
							<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
							<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
							<a href="javascript:void(0);" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a>
						</span>
					</div>
				</td>
			</tr>
			</tbody>
		</table>
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
				<li>
			    	검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			    </li>
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/data-board/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();"><c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
			</div>
		</div>
	</div>
	</form:form>

	<div class="count_title mt-40">
		<h5>
			총 <c:out value="${op:numberFormat(pagination.totalItems)}"/>건
		</h5>	 <!-- 전체 -->   <!-- 건 조회 -->
		<span>
			<select name="displayCount" id="displayCount" title="${op:message('M00239')}">
				<option value="10"><c:out value="${op:message('M00240')}"/></option>
				<option value="20"><c:out value="${op:message('M00241')}"/></option>
				<option value="50"><c:out value="${op:message('M00242')}"/></option>
				<option value="100"><c:out value="${op:message('M00243')}"/></option>
			</select>
		</span>
	</div>

	<div class="board_list">
		<table class="board_list_table" summary="${op:message('MENU_5000')}">
			<caption><c:out value="${op:message('MENU_5000')}"/></caption>
			<colgroup>
				<col style="width:50px;">
                <col style="width:50px;">
                <col style="width:500px;">
                <col style="width:150px;">
                <col style="width:100px;">
                <col style="width:150px;">
                <col style="width:150px;">
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="check_all"></th>
				<th scope="col">No.</th>
                <th scope="col">제목</th>
                <th scope="col">작성자</th>
                <th scope="col">조회수</th>
                <th scope="col">등록일</th>
                <th scope="col">게시여부</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="databoard" varStatus="i">
				<tr>
					<td>
						<input type="checkbox" value="${fn:escapeXml(databoard.dataId)}" id="check_${fn:escapeXml(databoard.dataId)}">
					</td>
					<td><c:out value="${pagination.itemNumber - i.count}"/></td>
					<td>
						<div class="tex_l">
							<c:if test="${databoard.noticeFlag == 'Y'}">
								<span class="label_notice">추천</span>
							</c:if>
							<a href="javascript:databoardDetail('${fn:escapeXml(databoard.dataId)}')"><c:out value="${databoard.subject}"/></a>
						</div>
					</td>
					<td><c:out value="${op:date(databoard.userName)}"/></td>
					<td><c:out value="${op:numberFormat(databoard.hits)}"/></td>
					<td><c:out value="${op:date(databoard.createdDate)}"/></td>
					<td><c:out value="${databoard.useYn == 'Y' ? '-' : '중지'}"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<c:if test="${empty list}">
			<div class="no_content">
				<c:out value="${op:message('M00277')}"/>
			</div>
		</c:if>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a href="<c:url value="/opmanager/data-board/create" />" class="btn btn-dark-gray btn-mini"><c:out value="${op:message('M00088')}"/></a> <!-- 등록 -->
				<a href="javascript:deleteCheckDataboard()" class="btn btn-default btn-mini"><c:out value="${op:message('M00074')}"/></a>
			</div>
		</div>
		<div class="pagination-wrap">
			<p class="pagination op-pagination">
				<page:pagination-manager />
			</p>
		</div>
	</div>


	<div style="display: none;">
		<span id="today"><c:out value="${today}"/></span>
		<span id="week"><c:out value="${week}"/></span>
		<span id="month1"><c:out value="${month1}"/></span>
		<span id="month2"><c:out value="${month2}"/></span>
	</div>

<script type="text/javascript">
$(function(){
	displayChange();
	displaySelected();

	// 체크박스 이벤트 (체크&해제)
	checkedEventSet();

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startCreateDate"]' , 'input[name="endCreateDate"]');
	EventHandler.calendarStartDateAndEndDateVaild();
})
function displayChange() {
	$("#displayCount").on('change', function(){
		$("#itemsPerPage").val($(this).val());
		$('#searchParam').submit();
	});
}
function displaySelected(){
	$("#displayCount").val($("#itemsPerPage").val());
}

function deleteNotice(dataId) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/data-board/delete/" + dataId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.reload();
			});
		});
	});

}

function deleteCheckDataboard() {
	var databoardList = new Array;

	$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
		databoardList.push($(item).val());
	});

	if(databoardList.length == 0) {
		alert("삭제할 게시물을 선택해주세요.");
		return false;
	}

	if(confirm("삭제하시겠습니까?")) {
		$.post('/opmanager/data-board/deleteDataboard', {"databoardList": databoardList}, function(response) {
			if(response.isSuccess && response.data) {
				if(response.data == "SUCC") {
					alert("삭제되었습니다.");
					location.reload();
				} else {
					alert("오류가 발생했습니다.");
				}
			}
		});
	}
}

function databoardDetail(dataId) {

	location.href = '/opmanager/data-board/edit/'+dataId;
}

/**
 *	함 수 명 : checkedEvent
 *	기	능  : 체크&해제 이벤트 셋팅
 */
function checkedEventSet() {
	var childObjs = $("input[id^='check_']").not("[id='check_all']");

	// 체크박스 '전체' 체크&해지
	$("#check_all").click(function(){
		$(childObjs).prop("checked", $(this).is(":checked"));
	});

	// '개별' 체크박스에 따른 '전체' 체크박스 체크&해지
	$(childObjs).click(function() {
		if($(childObjs).length == $("input[id^='check_']:checked").not("[id='check_all']").length) {
			$("#check_all").prop("checked", true);
		} else {
			$("#check_all").prop("checked", false);
		}
	});
}

function search() {
	var strStartDate = $("#startCreateDate").val();
	var strEndDate = $("#endCreateDate").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		if(strStartDate > strEndDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			$("#endCreateDate").focus();
			return false;
		}
	}

	$("#searchParam").submit();
}
</script>
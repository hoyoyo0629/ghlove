<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<div class="location">
        <a href="">고객센터</a> &gt; <a href="">매뉴얼 관리</a> &gt; <a href="">사용자 매뉴얼</a>
    </div>

	<h3><span>사용자 매뉴얼</span></h3>
	<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>

	<div class="board_write">
	    <table class="board_write_table" summary="사용자 매뉴얼">
	        <colgroup>
	            <col style="width:220px;">
	        </colgroup>
	        <tbody>
	            <tr>
	                <td class="label">검색구분</td>
	                <td>
	                    <div>
	                        <form:select path="where" title="검색구분" class="wd-150">
<%-- 	                            <form:option value="" label="전체" /> --%>
	                            <form:option value="PAGE" label="페이지 구분" />
	                            <form:option value="SUBJECT" label="제목" />
	                            <form:option value="CONTENT" label="내용" />
	                        </form:select>
	                        <form:input path="query" title="검색구분" class="input_txt required _filter half" />
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">등록일</td>
	                <td>
	                    <div>
	                        <span class="datepicker"><form:input path="startCreateDate" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
						<span class="wave">~</span>
						<span class="datepicker"><form:input path="endCreateDate" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>
	                        <span class="day_btns">
								<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
								<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
								<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
								<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
								<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
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
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/manual/list'">초기화</button>
	            <!-- <button type="submit" class="btn btn-dark-gray btn-mini">검색</button> -->
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
	        </div>
	    </div>
	</div>

	</form:form>

	<div class="board_list mt-40">
	    <div class="count_title mt-40">
	        <h5>총 <c:out value="${op:numberFormat(pagination.totalItems)}"/>건</h5>
	        <span>
	            <select name="displayCount" id="displayCount" title="${op:message('M00239')}">
					<option value="10"><c:out value="${op:message('M00240')}"/></option>
					<option value="20"><c:out value="${op:message('M00241')}"/></option>
					<option value="50"><c:out value="${op:message('M00242')}"/></option>
					<option value="100"><c:out value="${op:message('M00243')}"/></option>
				</select>
	        </span>
	    </div>

	    <table class="board_list_table" summary="사용자 매뉴얼">
	        <caption>사용자 매뉴얼</caption>
	        <colgroup>
	            <col style="width:50px;">
	            <col style="width:50px;">
	            <col style="width:200px;">
	            <col style="width:500px;">
	            <col style="width:200px;">
	            <col style="width:100px;">
	            <col style="width:150px;">
	        </colgroup>
	        <thead>
	            <tr>
	                <th scope="col"><input type="checkbox" id="check_all"></th>
	                <th scope="col">No.</th>
	                <th scope="col">페이지 구분</th>
	                <th scope="col">제목</th>
	                <th scope="col">작성자</th>
	                <th scope="col">조회수</th>
	                <th scope="col">등록일</th>
	            </tr>
	        </thead>
	        <tbody>
	        	<c:forEach items="${list}" var="list" varStatus="i">
	            <tr style="background:#fff;">
	                <td>
	                    <div>
	                        <input type="checkbox" value="${fn:escapeXml(list.mnlSn)}" id="check_${fn:escapeXml(list.mnlSn)}">
	                    </div>
	                </td>
	                <td>
	                    <div><c:out value="${pagination.itemNumber - i.count}"/></div>
	                </td>
	                <td>
	                    <div>
	                        <c:out value="${list.pageGbn }"/>
	                    </div>
	                </td>
	                <td class="tleft">
	                    <div>
	                        <a href="javascript:location.href='/opmanager/manual/edit/${fn:escapeXml(list.mnlSn)}'"><c:out value="${list.menuSj}"/></a>
	                    </div>
	                </td>
	                <td>
	                    <div>
	                        <c:out value="${list.userName}"/>
	                    </div>
	                </td>
	                <td>
	                    <div><c:out value="${list.inqireCo}"/></div>
	                </td>
	                <td>
	                    <div>
	                        <c:out value="${list.frstRegistPnttm}"/>
	                    </div>
	                </td>
	            </tr>
	            </c:forEach>
	        </tbody>
	    </table>
	    <div class="btn_all btn_right">
	        <div class="flex_box gap-08">
	            <button type="button" class="btn btn-default btn-mini" onclick="javascript:location.href='/opmanager/manual/create'">등록</button>
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteCheckManual()">삭제</button>
	        </div>
	    </div>
	    <div class="pagination-wrap">
			<p class="pagination op-pagination">
				<page:pagination-manager />
			</p>
		</div>
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

function deleteCheckManual() {
	var manualList = new Array;

	$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
		manualList.push($(item).val());
	});

	if(manualList.length == 0) {
		alert("삭제할 게시물을 선택해주세요.");
		return false;
	}

	if(confirm("삭제하시겠습니까?")) {
		$.post('/opmanager/manual/deleteManual', {"manualList": manualList}, function(response) {
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

function manualDetail(id) {

	location.href = '/opmanager/manual/edit/'+id;
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

/**
 *	함 수 명 : search
 *	기	능  : 검색
 */
function search() {
	var strStartDate = $("#startCreateDate").val();
	var strEndDate = $("#endCreateDate").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
		var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			var value = $("#startCreateDate").val();
			$("#endCreateDate").val("");
			$("#endCreateDate").focus();
			$("#endCreateDate").val(value);
			return false;
		}
		var searchChk = Common.searchDateMonth(startDate, endDate);
		if(!searchChk) return false;
	}

	$("#searchParam").submit();
}
</script>
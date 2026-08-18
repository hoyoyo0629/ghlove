<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


			<div class="location">
				<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
			</div>

			<!--공지사항 시작-->
			<h3><span><c:out value="${op:message('M00269')}"/> </span></h3>
			<div class="board_write" >
				<form:form modelAttribute="searchParam" method="post">
				<form:hidden path="sort" />
				<form:hidden path="orderBy" />
				<form:hidden path="itemsPerPage"/>
				<table class="board_write_table" summary="${op:message('M00269')} ">
					<caption><c:out value="${op:message('M00269')}"/> </caption>
					<colgroup>
						<col style="width:220px;" />
					</colgroup>
					<tbody>
						 <tr>
						 	<td class="label">검색구분</td>
						 	<td>
						 		<div class="flex_box gap-08">
						 			<form:select path="where" title="검색구분" class="wd-150">
                                        <form:option value="SUBJECT">제목</form:option>
                                        <form:option value="CONTENT">내용</form:option>
                                    </form:select>
						 			<form:input path="query" cssClass="input_txt required _filter full" title="${op:message('M00021')}"/>
								</div>
						 	</td>
						 </tr>
						<tr>
							<td class="label"><c:out value="${op:message('M00202')}"/> </td>
							<td>
								<div class="search-date">
									<span class="datepicker"><form:input path="startDt" cssClass="datepicker optional" type="text" value="" maxlength="8" title="${op:message('M00507')}" /></span>
									<span class="wave">~</span>
									<span class="datepicker"><form:input path="endDt" cssClass="datepicker optional" type="text" value="" maxlength="8" title="${op:message('M00509')}" /></span>

									<span class="day_btns">
										<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
										<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
										<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
										<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
										<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
										<a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
									</span>
								</div>
							</td>
						</tr>
						 <%-- <tr>
						 	<td class="label">상단공지</td>
						 	<td>
						 		<div>
									<p>
					 					<form:radiobutton path="noticeFlag" label="전체" value="" checked="checked"/>
					 					<form:radiobutton path="noticeFlag" label="사용" value="Y" />
										<form:radiobutton path="noticeFlag" label="사용안함" value="N" />
									</p>
								</div>
						 	</td>
						 </tr> --%>
					</tbody>
				</table>
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
				<li>
			    	검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			    </li>
		</div>
				<!-- 버튼시작 -->
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/sellerNotice/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();"><c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
					</div>
				</div>
				<!-- 버튼 끝-->
				</form:form>
			</div> <!-- // board_write -->

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

			<div class="board_write">
				<table class="board_list_table" summary="${op:message('M00273')}">
					<caption><c:out value="${op:message('M00273')}"/></caption>
					<colgroup>
						<col style="width:50px;">
                        <col style="width:50px;">
                        <col style="width:500px;">
                        <col style="width:100px;">
                        <col style="width:150px;">
                        <col style="width:150px;">
                        <col style="width:100px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="check_all"></th>
							<th scope="col">No.</th>
                            <th scope="col">제목</th>
                            <th scope="col">조회수</th>
                            <th scope="col">등록일시</th>
                            <th scope="col">수정일시</th>
                            <th scope="col">게시여부</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="notice" varStatus="i">
							<tr>
								<td>
									<input type="checkbox" value="${fn:escapeXml(notice.noticeId)}" id="check_${fn:escapeXml(notice.noticeId)}">
								</td>
								<td><c:out value="${pagination.itemNumber - i.count}"/></td>
								<td>
									<div class="tex_l">
									<c:if test="${notice.noticeFlag == 'Y'}">
										<span class="label_notice">공지</span>
									</c:if>
									<a href="javascript:noticeEdit('${fn:escapeXml(notice.noticeId)}')"><c:out value="${notice.subject}"/></a>
									<c:if test="${notice.attachedFileCnt > 0}">
										<img class="databoard-icon"
											src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료 있음" />
									</c:if>
									</div>
								</td>
								<td><c:out value="${notice.hits}"/></td>
								<td><fmt:formatDate value="${notice.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatDate value="${notice.lastMdfcnDt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><c:out value="${notice.displayFlag == 'Y' ? '게시' : '중지'}"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<c:if test="${empty list}">
				<div class="no_content">
					<c:out value="${op:message('M00277')}"/>
				</div>
				</c:if>
			</div><!--// board_write E-->


		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a href="<c:url value="/opmanager/sellerNotice/create" />" class="btn btn-dark-gray btn-mini"><c:out value="${op:message('M00088')}"/></a> <!-- 등록 -->
				<a href="javascript:deleteCheckNotice(${fn:escapeXml(notice.noticeId)})" class="btn btn-default btn-mini"><c:out value="${op:message('M00074')}"/></a>
			</div>
		</div>
		<div class="pagination-wrap">
			<p class="pagination op-pagination">
				<page:pagination-manager />
			</p>
		</div>

			<!--// 자주 묻는 질문관리 끝-->
<script type="text/javascript">
$(function(){
	displayChange();
	displaySelected();

	// 체크박스 이벤트 (체크&해제)
	checkedEventSet();

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDt"]' , 'input[name="endDt"]');
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

function deleteNotice(noticeId) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/sellerNotice/delete/" + noticeId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.reload();
			});
		});
	});

}

function noticeEdit(noticeId) {

	location.href = '/opmanager/sellerNotice/edit/'+noticeId;
}

function deleteCheckNotice() {
	var noticeList = new Array;

	// 선택된 지역코드 셋팅
	$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
		noticeList.push($(item).val());
	});

	if(noticeList.length == 0) {
		alert("삭제할 게시물을 선택해주세요.");
		return false;
	}

	if(confirm("삭제하시겠습니까?")) {
		$.post('/opmanager/sellerNotice/deleteNotice', {"noticeList": noticeList}, function(response) {
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
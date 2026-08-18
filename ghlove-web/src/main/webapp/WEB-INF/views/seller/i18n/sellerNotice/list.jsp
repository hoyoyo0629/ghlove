<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>


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
									<span class="datepicker"><form:input path="startDt" cssClass="datepicker optional " type="text" maxlength="8" title="${op:message('M00507')}" /></span>
									<span class="wave">~</span>
									<span class="datepicker"><form:input path="endDt" cssClass="datepicker optional " type="text" maxlength="8" title="${op:message('M00509')}" /></span>

									<span class="day_btns">
		                                <a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
		                                <a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
		                                <a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
		                                <a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
		                                <a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
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
						<button type="button" class="btn btn-default btn-mini" onclick="location.href='/seller/sys-notice/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
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
                        <col style="width:100px;">
                        <col style="width:*;">
                        <col style="width:150px;">
                        <col style="width:200px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col">No.</th>
                            <th scope="col">제목</th>
                            <th scope="col">조회수</th>
                            <th scope="col">등록일시</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="notice" varStatus="i">
							<tr>
								<td><c:out value="${pagination.itemNumber - i.count}"/></td>
								<td>
									<div class="tex_l">
										<c:if test="${notice.noticeFlag == 'Y'}">
											<span class="label_notice">공지</span>
										</c:if>
										<a href="javascript:noticeDetail('${fn:escapeXml(notice.noticeId)}')"><c:out value="${notice.subject}"/></a>
										<c:if test="${notice.attachedFileCnt > 0}">
											<img class="databoard-icon"
												src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료 있음" />
										</c:if>
									</div>
								</td>
								<td><c:out value="${notice.hits}"/></td>
								<td><fmt:formatDate value="${notice.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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

function noticeDetail(noticeId) {
	location.href = '/seller/sys-notice/detail/'+noticeId;
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<div class="location">
		<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#"></a>
	</div>
	<h3><span></span></h3>
	<form:form modelAttribute="searchParam" method="post">
		<form:hidden path="query"/>
		<div class="board_write">
			<table class="board_write_table" summary="${op:message('이메일발송')}">
				<colgroup>
					<col style="width:220px;">
				</colgroup>
				<tbody>
				<tr>
					<td class="label">검색구분</td>
					<td>
						<div class="flex_box gap-08">
							<form:select path="searchType" title="검색구분" class="wd-150">
<%-- 	                                    <form:option value="">전체</form:option> --%>
	                                    <form:option value="SUBJECT">제목</form:option>
	                                    <form:option value="CONTENT">내용</form:option>
	                                </form:select>
									<form:input path="searchContent" cssClass="input_txt required _filter full" title="${op:message('M00021')}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><c:out value="${op:message('M00202')}"/> </td>
					<td>
						<div class="search-date">
							<span class="datepicker"><form:input path="searchStartDate" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="searchEndDate" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>

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

			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/email/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
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
		<table class="board_list_table" summary="${op:message('')}">
			<caption><c:out value="${op:message('')}"/></caption>
			<colgroup>
                <col style="width:50px;">
                <col style="width:500px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:150px;">
                <col style="width:100px;">
			</colgroup>
			<thead>
			<tr>
				<th scope="col">No.</th>
                <th scope="col">제목</th>
                <th scope="col">발송일</th>
                <th scope="col">발송 상태</th>
                <th scope="col">발송자</th>
                <th scope="col">등록 일자</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="email" varStatus="i">
				<tr>
					<td>
						<c:out value="${email.emailId}" />
					</td>
					<td>
						<a href="<c:url value='/opmanager/email/${email.emailId}' />"><c:out value="${email.subject}" /></a>
					</td>
					<td><c:out value="${op:datetime(email.sendDate)}"/></td>
					<td><c:out value="${email.statusName}"/></td>
					<td><c:out value="${email.userName}"/></td>
					<td><c:out value="${op:date(email.frstRegistPnttm)}"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<c:if test="${empty list}">
			<div class="no_content">
				등록된 발송이력이 없습니다.
			</div>
		</c:if>
		<div class="pagination-wrap">
			<p class="pagination op-pagination">
				<page:pagination-manager />
			</p>
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a href="/opmanager/email/form" class="btn btn-dark-gray btn-mini">발송</a> <!-- 등록 -->
			</div>
		</div>
	</div>

<script type="text/javascript">
$(function(){
	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
	EventHandler.calendarStartDateAndEndDateVaild();
})
</script>
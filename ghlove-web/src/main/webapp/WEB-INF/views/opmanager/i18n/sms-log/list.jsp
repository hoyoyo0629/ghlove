<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
				<form:hidden path="itemsPerPage"/>
				<table class="board_write_table" summary="${op:message('M00269')} ">
					<caption><c:out value="${op:message('M00269')}"/> </caption>
					<colgroup>
						<col style="width:220px;" />
						<col />
						<col style="width:220px;" />
						<col />
					</colgroup>
					<tbody>
						 <tr>
						 	<td class="label">문자 구분</td>
						 	<td>
						 		<div class="flex_box gap-08">
						 			<form:select path="smsTypeStr" title="문자 구분" class="wd-400">
						 				<form:option value="">전체</form:option>
						 				<c:forEach items="${smsTypes}" var="smsType" varStatus="i">
						 					<form:option value="${fn:escapeXml(smsType.code)}"><c:out value="${smsType.description}"/></form:option>
						 				</c:forEach>
                                    </form:select>
								</div>
						 	</td>
						 	<td class="label">검색내용<br>(이름, 전화번호 등)</td>
						 	<td>
						 		<div>
							 		<form:hidden path="where" value="CONTENT"/>
						 			<form:input path="query" cssClass="input_txt required _filter full" title="${op:message('M00021')}" placeholder=""/>
					 			</div>
						 	</td>
						 </tr>
						<tr>
							<td class="label"><c:out value="${op:message('M01692')}"/> </td>
							<td colspan="3">
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

				<!-- 버튼시작 -->
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/sms-log/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
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
						<col style="width:150px;">
                        <col style="width:300px;">
                        <col style="width:300px;">
                        <col style="width:150px;">
                        <col style="width:150px;">
                        <col style="width:150px;">
                        <col style="width:100px;">
                        <col style="width:200px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col">일련번호</th>
							<th scope="col">전화번호</th>
                            <th scope="col">구분</th>
                            <th scope="col">발송내용</th>
                            <th scope="col">생성일시</th>
                            <th scope="col">전송시작일시</th>
                            <th scope="col">전송완료일시</th>
                            <th scope="col">문자처리상태</th>
                            <th scope="col">오류내용</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${smsLogList}" var="smsLog" varStatus="i">
							<tr>
								<td><c:out value="${smsLog.insttCrtSn}"/></td>
								<td><c:out value="${smsLog.phoneNumber}"/></td>
								<td><c:out value="${smsLog.svcName}"/></td>
								<td title="${fn:escapeXml(smsLog.smsContent)}"><c:out value="${op:strcut(smsLog.smsContent, 20)}"/></td>
								<td><c:out value="${smsLog.esbInitTimeF}"/></td>
								<td><c:out value="${smsLog.esbTxTimeF}"/></td>
								<td><c:out value="${smsLog.esbComptTimeF}"/></td>
								<td><c:out value="${smsLog.esbStatus}"/></td>
								<td title="${fn:escapeXml(smsLog.esbErrMsg)}"><c:out value="${op:strcut(smsLog.esbErrMsg, 20)}"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<c:if test="${empty smsLogList}">
				<div class="no_content">
					조회된 내용이 없습니다.
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
	
	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
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

function search() {
	var startDate = $("#searchStartDate").val();
	var endDate = $("#searchEndDate").val();
	
	let isDate = false;
	
	if (startDate || endDate) {
		if (Common.validateDate(startDate) && Common.validateDate(endDate)) {
			if (Number(startDate) <= Number(endDate)) {
				isDate = true;
			}
		}
		
		if (!isDate) {
			alert('생성일자를 확인해주세요.');
			return;
		}
	}

	$("#searchParam").submit();
}

</script>
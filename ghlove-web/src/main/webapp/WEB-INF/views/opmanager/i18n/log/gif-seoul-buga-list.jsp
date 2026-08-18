<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.onlinepowers.framework.util.DateUtils" %>
<%@ page import="saleson.common.Const" %>

<div class="location">
	<a href="#"></a>&gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>서울세외 부과연계 로그</span></h3>

<form:form modelAttribute="gifSeoulParam" cssClass="opmanager-search-form clear" method="post" id="gifSeoulParam">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>
	<form:hidden path="itemsPerPageTemp"/>
	<form:hidden path="query"/>

	<div class="board_write">
		<table class="board_write_table" summary="지방세외 부과 로그">
			<colgroup>
				<col style="width:15%;">
				<col style="width:35%;">
				<col style="width:15%;">
				<col style="width:35%;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">결과여부</td>
					<td>
						<div class="flex_box gap-08">
							<form:select path="srchErrorCd" title="아이디" class="wd-150">
								<form:option value="">전체</form:option>
								<form:option value="SUCCESS">성공</form:option>
								<form:option value="FAIL">실패</form:option>
							</form:select>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">전자납부번호</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:input path="srchTxt" title="전자납부번호" class="input_txt required _filter wd-500" type="text" value="${fn:escapeXml(gifSeoulParam.srchTxt)}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">등록일</td>
					<td colspan="3">
						<div>
							<span class="datepicker">
								<form:input path="srchStartLogDate" cssClass="datepicker optional " title="${op:message('M00507')}" />
							</span>
							<span class="wave">~</span>
							<span class="datepicker">
								<form:input path="srchEndLogDate" cssClass="datepicker optional " title="${op:message('M00509')}" />
							</span>
							<span class="day_btns day_btn1">
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/log/gif-seoul-buga'">초기화</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>
</form:form>
<!-- List -->
<div class="board_list mt-40">
	<!-- List Header -->
	<div class="count_title mt-40">
		<h5>총 <fmt:formatNumber value="${fn:escapeXml(count)}" pattern="#,###"/> 건</h5>
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

	<table class="board_list_table" summary="서울세외 부과 로그">
		<caption>서울세외 부과 로그</caption>
		<colgroup>
			<col style="width:13%;">
			<col style="width:8%;">
			<col style="width:8%;">
			<col style="width:5%;">
			<col style="width:5%;">
			<col style="width:5%;">
			<col style="width:5%;">
			<col style="width:8%;">
			<col style="width:5%;">
			<col style="width:*;">
			<col style="width:13%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">전자납부번호</th>
				<th scope="col">기관코드</th>
				<th scope="col">세목코드</th>
				<th scope="col">과세년월</th>
				<th scope="col">과세구분</th>
				<th scope="col">시도코드</th>
				<th scope="col">납세자구분</th>
				<th scope="col">본세합계</th>
				<th scope="col">에러코드</th>
				<th scope="col">에러메세지</th>
				<th scope="col">연계시작일시</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><div><c:out value="${item.enapbuNo}"/></div></td>
							<td><div><c:out value="${item.siguCd}"/></div></td>
							<td><div><c:out value="${item.semokCd}"/></div></td>
							<td><div><c:out value="${item.taxYm}"/></div></td>
							<td><div><c:out value="${item.taxGubun}"/></div></td>
							<td><div><c:out value="${item.sidoCd}"/></div></td>
							<td><div><c:out value="${item.napGubun}"/></div></td>
							<td style="text-align: right;"><div><fmt:formatNumber value="${fn:escapeXml(item.taxAmt)}" pattern="#,###"/></div></td>
							<td><div><c:out value="${item.errorCd}"/></div></td>
							<td><div><c:out value="${item.errorMsg}"/></div></td>
							<td><div><c:out value="${item.ifStDt}"/></div></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="11">데이터가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<div class="pagination-wrap">
		<%-- <c:if test="${not empty list}">
			<div class="pagination-wrap"> --%>
				<page:pagination-manager />
			<%-- </div>
		</c:if> --%>
	</div>
</div>
<!-- List -->

<!-- 날짜 셋팅 영역 -->
<div style="display: none;">
	<c:set var="today" value="${DateUtils.getToday(Const.DATE_FORMAT)}"/>
	<span id="today"><c:out value="${today}"/></span>
	<span id="week"><c:out value="${DateUtils.addYearMonthDay(today, 0, 0, -7)}"/></span>
	<span id="month1"><c:out value="${DateUtils.addYearMonthDay(today, 0, -1, 0)}"/></span>
	<span id="month3"><c:out value="${DateUtils.addYearMonthDay(today, 0, -3, 0)}"/></span>
	<span id="year1"><c:out value="${DateUtils.addYearMonthDay(today, 0, -12, 0)}"/></span>
</div>
<!--// 날짜 셋팅 영역  -->

<script type="text/javascript">

	$(function() {
	  searchEnterKey();
	  displayChange();
      displaySelected();

	  Common.DateButtonEvent.set('.day_btn1 > a[class^=btn_date]', '', 'input[name="srchStartLogDate"]' , 'input[name="srchEndLogDate"]');
	  EventHandler.calendarStartDateAndEndDateVaild();

	});


	function search() {
		var strStartDate = $("#srchStartLogDate").val();
		var strEndDate = $("#srchEndLogDate").val();

		if(strStartDate === '' || strEndDate === '') {
			alert("등록일자는 필수입니다.");
			return false;
		}

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#shCntrDeEnd").val();
				$("#srchEndLogDate").val("");
				$("#srchEndLogDate").focus();
				$("#srchEndLogDate").val(value);
				return false;
			}
		}
		$("#gifSeoulParam").submit();
	}

	function displayChange() {
		$("#displayCount").on('change', function(){
			$("#itemsPerPage").val($(this).val());
			$("#itemsPerPageTemp").val($(this).val());
			$('#gifSeoulParam').submit();
		});
	}
	function displaySelected(){
		$("#displayCount").val($("#itemsPerPage").val());
	}


	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#srchTxt").on('keydown', function(e){
			if (e.keyCode == '13') {
				$("#gifSeoulParam").submit();
			}
		});
	}
	</script>
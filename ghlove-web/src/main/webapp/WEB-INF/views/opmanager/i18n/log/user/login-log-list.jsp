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

<h3><span>사용자 로그관리</span></h3>

<!-- Search -->
<form:form modelAttribute="loginLogParam" cssClass="opmanager-search-form clear" method="post" id="searchForm">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>
	<form:hidden path="itemsPerPageTemp"/>
	<form:hidden path="query"/>

	<div class="board_write">
		<table class="board_write_table" summary="사용자 로그관리">
			<colgroup>
				<col style="width:220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">검색구분</td>
					<td>
						<div class="flex_box gap-08">
							<form:select path="srchKey" title="아이디" class="wd-150">
								<form:option value="LOGIN_ID">사용자ID</form:option>
								<form:option value="REMOTE_ADDR">접속IP</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter wd-500" type="text"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">접속일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="srchStartLoginDate" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="srchEndLoginDate" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>
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
				<tr>
					<td class="label">로그인 성공여부</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<form:radiobutton path="srchSuccessFlag" value="" label="전체" checked="checked" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchSuccessFlag" value="Y" label="성공" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchSuccessFlag" value="N" label="실패" />
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/log/user/login-log'">초기화</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>
</form:form>
<!--// Search -->

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

	<table class="board_list_table" summary="사용자 로그관리">
		<caption>사용자 로그관리</caption>
		<colgroup>
			<col style="width:50px;">
			<col style="width:200px;">
			<col style="width:200px;">
			<col style="width:100px;">
			<col style="width:200px;">
			<col style="width:200px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">접속일</th>
				<th scope="col">사용자 ID</th>
				<th scope="col">로그인 성공유무</th>
				<th scope="col">접속 IP</th>
				<th scope="col">관리</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><div><c:out value="${pagination.itemNumber - i.count}"/></div></td>
							<td><div><c:out value="${item.loginDate}"/></div></td>
							<td><div><c:out value="${item.loginId}"/></div></td>
							<td><div><c:out value="${item.successFlag eq 'Y' ? '성공' : '실패'}"/></div></td>
							<td><div><c:out value="${item.remoteAddr}"/></div></td>
							<td>
								<div>
									<c:choose>
										<c:when test="${item.successFlag eq 'Y'}">
											<a href="javascript:details('${fn:escapeXml(item.loginLogId)}');">상세보기</a>
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
								</div>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="6">사용자 로그관리 정보가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<div class="pagination-wrap">
		<c:if test="${not empty list}">
			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>
		</c:if>
	</div>
</div>
<!-- List -->

<!--// 날짜 셋팅 영역  -->

<script type="text/javascript">
	$(function(){

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();

		// 날짜 변경 이벤트
		datepickerChange();

		// 검색 enter key
		searchEnterKey();

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="srchStartLoginDate"]' , 'input[name="srchEndLoginDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});


	/**
	 *	함 수 명 : displayChange
	 *	기	능  : 한 페이지 출력 갯수 변경 이벤트
	 */
	function displayChange() {
		$("#displayCount").on('change', function(){
			$("#itemsPerPage").val($(this).val());
			$("#itemsPerPageTemp").val($(this).val());
			search();
		});
	}

	/**
	 *	함 수 명 : displaySelected
	 *	기	능  : 선택된 페이지 출력 갯수 셋팅
	 */
	function displaySelected(){
		$("#displayCount").val($("#itemsPerPage").val());
	}

	/**
	 *	함 수 명 : details
	 *	기	능  : 상세보기
	 *	파라미터  : loginLogId - 로그인 로그 ID
	 */
	function details(loginLogId) {
		location.href = "/opmanager/log/user/login-log/details/"+loginLogId+location.search;
	}

	/**
	 *	함 수 명 : datepickerChange
	 *	기	능  : 날짜 변경 이벤트
	 */
	function datepickerChange() {
		if($("input.datepicker").length) {
			$("input.datepicker").keyup(function(e) {
				if(e && e.target && e.target.value) {
					var value = e.target.value.replace(/[^0-9]/g, "");
					if(value.length > 8) { value = value.slice(0, 8); }
					e.target.value = value;
				}
			});
		}
	}

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		var strStartDate = $("#srchStartLoginDate").val();
		var strEndDate = $("#srchEndLoginDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#srchEndLoginDate").val();
				$("#srchEndLoginDate").val("");
				$("#srchEndLoginDate").focus();
				$("#srchEndLoginDate").val(value);
				return false;
			}
		}

		$("#searchForm").submit();
	}

	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#srchValue, #srchStartLoginDate, #srchEndLoginDate").on('keydown', function(e){
			if (e.keyCode == '13') {
				search();
			}
		});
	}
</script>
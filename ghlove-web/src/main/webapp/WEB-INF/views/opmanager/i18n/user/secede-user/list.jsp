<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.onlinepowers.framework.util.DateUtils" %>
<%@ page import="saleson.common.Const" %>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span><c:out value="${op:message('MENU_4105')}"/></span></h3>

<!-- Search -->
<form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="post" id="searchForm">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>
	<form:hidden path="query"/>

	<div class="board_write">
		<table class="board_write_table" summary="">
			<colgroup>
				<col style="width:220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">검색구분</td>
					<td>
						<div class="flex_box gap-08">
							<form:select path="srchKey" title="전체" class="wd-150">
								<form:option value="LOGIN_ID">아이디</form:option>
								<form:option value="LEAVE_REASON">탈퇴사유</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter half" type="text"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">탈퇴일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="srchStartLeaveDate" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="srchEndLeaveDate" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>
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
				<tr>
					<td class="label">탈퇴구분</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<form:radiobutton path="srchLeaveType" value="" label="전체" checked="checked" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchLeaveType" value="U" label="회원탈퇴" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchLeaveType" value="M" label="관리자탈퇴" />
							</div>
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
	        </ul>
	  	</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/user/secede-user/list'">초기화</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>
</form:form>
<!--// Search -->

<!-- List Header -->
<div class="count_title mt-40">
	<h5>총 <fmt:formatNumber value="${fn:escapeXml(count)}" pattern="#,###"/>건</h5>
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

<!-- List -->
<div class="board_list">
	<table class="board_list_table" summary="회원탈퇴관리">
		<caption>회원탈퇴관리</caption>
		<colgroup>
			<col style="width:5%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">탈퇴일</th>
				<th scope="col">아이디</th>
				<th scope="col">탙퇴사유</th>
				<th scope="col">탈퇴구분</th>
				<th scope="col">담당자</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<c:set var="leaveType" value="${(not empty item.leaveUserId) ? 'M' : 'U'}" />
						<c:choose>
							<c:when test="${not empty item.leaveCodeLabel and not empty item.leaveReason}">
								<c:set var="leaveReason" value="${item.leaveCodeLabel += ' / ' += item.leaveReason}" />
							</c:when>
							<c:when test="${not empty item.leaveCodeLabel}">
								<c:set var="leaveReason" value="${item.leaveCodeLabel}" />
							</c:when>
							<c:when test="${not empty item.leaveReason}">
								<c:set var="leaveReason" value="${item.leaveReason}" />
							</c:when>
						</c:choose>

						<tr style="background:#fff;">
							<td><c:out value="${pagination.itemNumber - i.count}"/></td>
							<td><c:out value="${item.leaveDate}"/></td>
							<td><c:out value="${item.loginId}"/></td>
							<td>
								<a href="javascript:reasonDetailsPopup('${fn:escapeXml(item.userId)}')">
									<c:out value="${fn:substring(leaveReason, 0, 50)}"/><c:out value="${fn:length(leaveReason) > 50 ? '...' : ''}"/>
								</a>
							</td>
							<td><c:out value="${(leaveType eq 'M') ? '관리자탈퇴' : '회원탈퇴'}"/></td>
							<td>
								<c:choose>
									<c:when test="${leaveType eq 'M'}">
										<c:out value="${item.roleName}"/>
										<c:if test="${not empty item.leaveUserName}">
											<br/>(<c:out value="${item.leaveUserName}"/>)
										</c:if>
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="6">회원탈퇴관리 정보가 존재하지 않습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<c:if test="${not empty list}">
		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>
	</c:if>
</div>
<!--/ List -->

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

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="srchStartLeaveDate"]' , 'input[name="srchEndLeaveDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});

	/**
	 *	함 수 명 : displayChange
	 *	기	능  : 한 페이지 출력 갯수 변경 이벤트
	 */
	function displayChange() {
		$("#displayCount").on('change', function(){
			$("#itemsPerPage").val($(this).val());
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
	 *	함 수 명 : reasonDetailsPopup
	 *	기	능  : 탈퇴사유 상세 팝업
	 */
	function reasonDetailsPopup(userId) {
		var url = "/opmanager/user/secede-user/popup/reason-details/"+userId;
		var popupName = "/opmanager/user/secede-user/popup/reason-details";
		Common.popup(url, popupName, 600, 350, 1, 0, 0);
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
		var strStartDate = $("#srchStartLeaveDate").val();
		var strEndDate = $("#srchEndLeaveDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#srchEndLeaveDate").val();
				$("#srchEndLeaveDate").val("");
				$("#srchEndLeaveDate").focus();
				$("#srchEndLeaveDate").val(value);
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
		$("#srchValue, #srchStartLeaveDate, #srchEndLeaveDate").on('keydown', function(e){
			if (e.keyCode == '13') {
				search();
			}
		});
	}
</script>
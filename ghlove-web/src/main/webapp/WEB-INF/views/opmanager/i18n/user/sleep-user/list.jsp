<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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

<h3><span><c:out value="${op:message('MENU_4107')}"/></span></h3>

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
						<div class="flex_box gap-08 item-center">
							<form:select path="srchKey" title="아이디" class="wd-150">
								<form:option value="LOGIN_ID">아이디</form:option>
								<form:option value="USER_NAME">이름</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter wd-200" type="text"/>
							<p class="tip">※ “이름, 아이디”는 정확하게 입력해야 합니다.</p>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">최종 방문일</td>
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
	        </ul>
	  	</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/user/sleep-user/list'">초기화</button>
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
	<table class="board_list_table" summary="휴먼회원관리">
		<caption>휴먼회원관리</caption>
		<colgroup>
			<col style="width:5%;">
			<col style="width:5%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="check_all" /></th>
				<th scope="col">No.</th>
				<th scope="col">최종 방문일</th>
				<th scope="col">아이디</th>
				<th scope="col">이름</th>
				<th scope="col">주소</th>
				<th scope="col">기부누적액</th>
				<th scope="col">포인트잔액</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><input type="checkbox" value="${fn:escapeXml(item.userId)}" id="check_${fn:escapeXml(item.userId)}"></td>
							<td><c:out value="${pagination.itemNumber - i.count}"/></td>
							<td><c:out value="${item.loginDate}"/></td>
							<td><c:out value="${item.loginId}"/></td>
							<td><c:out value="${item.userName}"/></td>
							<td><c:out value="${item.address}"/> <c:out value="${item.addressDetail}"/></td>
							<td>
								<c:choose>
									<c:when test="${not empty item.totalCntrAmt}">
										<fmt:formatNumber value="${fn:escapeXml(item.totalCntrAmt)}" pattern="#,###.##"/>
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${not empty item.totalCntrBlcePoint}">
										<fmt:formatNumber value="${fn:escapeXml(item.totalCntrBlcePoint)}" pattern="#,###.##"/>
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
					<tr>
						<td colspan="8">휴면회원 정보가 존재하지 않습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="wakeupSleepUser();">휴면회원 해제</button>
		</div>
	</div>
	<c:if test="${not empty list}">
		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>
	</c:if>
</div>
<!--// List -->

<script type="text/javascript">
	$(function(){

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();

		// 체크박스 이벤트 (체크&해제)
		checkedEventSet();

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
	 *	함 수 명 : wakeup
	 *	기	능  : 휴면회원 해제
	 */
	function wakeupSleepUser() {
		var userIdList = new Array();

		// 선택된 사용자 셋팅
		$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
			userIdList.push($(item).val());
		});

		if(userIdList.length == 0) {
			alert("해제할 휴면회원을 선택해 주세요.");
			return false;
		}

		if(confirm("선택한 휴면회원의 휴면을 해제하시겠습니까?")) {
			$.post("/opmanager/user/sleep-user/wakeup", {"userIdList" : userIdList}, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data == "SUCC") {
						alert("휴면회원 해제되었습니다.");
						location.reload();
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
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
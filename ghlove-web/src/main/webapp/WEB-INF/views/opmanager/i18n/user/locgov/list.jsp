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

<h3><span><c:out value="${op:message('MENU_4401')}"/></span></h3>

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
							<form:select path="srchKey" title="구분" class="wd-150">
								<form:option value="LOCGOV_NM">지자체명</form:option>
								<form:option value="CHARGER_NM">담당자명</form:option>
								<form:option value="CHARGER_CTTPC">연락처</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter half" type="text"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">등록일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="srchStartCreated" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="srchEndCreated" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>
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
	        </ul>
	  	</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/user/locgov/list';">초기화</button>
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
	<table class="board_list_table" summary="지자체관리">
		<caption>지자체관리</caption>
		<colgroup>
			<col style="width:5%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">지자체</th>
				<th scope="col">예산(백만원)</th>
				<th scope="col">인구(명)</th>
				<th scope="col">담당자명</th>
				<th scope="col">연락처</th>
				<th scope="col">포인트 지급률</th>
				<th scope="col">등록일</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><c:out value="${pagination.itemNumber - i.count}"/></td>
							<td><a href="javascript:locgovEdit('${fn:escapeXml(item.locgovCode)}');"><c:out value="${item.upperLocgovNm}"/>&nbsp;<c:out value="${item.locgovNm}"/></a></td>
							<td>
								<c:if test="${not empty item.locgovBudgetAmt}">
									<fmt:formatNumber value="${fn:escapeXml(item.locgovBudgetAmt)}" pattern="#,###.##"/>
								</c:if>
							</td>
							<td>
								<c:if test="${not empty item.locgovPopltnCo}">
									<fmt:formatNumber value="${fn:escapeXml(item.locgovPopltnCo)}" pattern="#,###.##"/>
								</c:if>
							</td>
							<td><c:out value="${item.chargerNm}"/></td>
							<td><c:out value="${item.chargerCttpc}"/></td>
							<td>
								<a href="javascript:pointListPopup('${fn:escapeXml(item.locgovCode)}');">
									<fmt:formatNumber value="${fn:escapeXml(item.pointRate)}" pattern="#.##"/>
								</a>
							</td>
							<td><c:out value="${item.frstRegistPnttm}"/></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="9">지자체관리 정보가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="locgovCreate();">등록</button>
			<!-- 2022.11.18 주석처리 -->
			<!-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="locgovDelete();">삭제</button> -->
		</div>
	</div>
	<c:if test="${not empty list}">
		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>
	</c:if>
</div>
<!--// List -->

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

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="srchStartCreated"]' , 'input[name="srchEndCreated"]');
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
	 *	함 수 명 : locgovCreate
	 *	기	능  : 지자체 등록
	 */
	function locgovCreate() {
		location.href = "/opmanager/user/locgov/create"+location.search;
	}

	/**
	 *	함 수 명 : locgovEdit
	 *	기	능  : 지자체 수정
	 */
	function locgovEdit(locgovCode) {
		if(!locgovCode) return false;
		location.href = "/opmanager/user/locgov/edit/"+locgovCode+location.search;
	}

	/**
	 *	함 수 명 : locgovDelete
	 *	기	능  : 지자체 삭제
	 */
	function locgovDelete() {
		var locgovCodeList = new Array;

		// 선택된 지역코드 셋팅
		$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
			locgovCodeList.push($(item).val());
		});

		if(locgovCodeList.length == 0) {
			alert("삭제할 지자체를 선택해주세요.");
			return false;
		}

		if(confirm("삭제하시겠습니까?")) {
			$.post('/opmanager/user/locgov/delete', {"locgovCodeList": locgovCodeList}, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data == "SUCC") {
						alert("삭제되었습니다.");
						location.reload();
					} else if(response.data == "NOT_DEL") {
						alert("삭제할 수 없는 지자체가 선택되었습니다.");
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}

	/**
	 *	함 수 명 : pointListPopup
	 *	기	능  : 포인트 지급률 변경이력 (팝업) 활성화
	 *	파라미터  : locgovCode - 지자체코드
	 */
	function pointListPopup(locgovCode) {
		var today = new Date();
		var year = today.getFullYear();
		var url = '/opmanager/user/locgov/popup/point/list/'+locgovCode+'?stdrYear='+year;
		var popupName = '/opmanager/user/locgov/popup/point/list';

		// 상세 팝업 활성화
		Common.popup(url, popupName, 600, 675 ,1, 0, 0);
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
		var strStartDate = $("#srchStartCreated").val();
		var strEndDate = $("#srchEndCreated").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#srchEndCreated").val();
				$("#srchEndCreated").val("");
				$("#srchEndCreated").focus();
				$("#srchEndCreated").val(value);
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
		$("#srchValue, #srchStartCreated, #srchEndCreated").on('keydown', function(e){
			if (e.keyCode == '13') {
				search();
			}
		});
	}
</script>
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

<h3><span>엑셀 다운로드 사유 관리</span></h3>

<form:form modelAttribute="privacyLogParam" cssClass="opmanager-search-form clear" method="post" id="privacyLogParam">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>
	<form:hidden path="itemsPerPageTemp"/>
	<form:hidden path="query"/>

	<div class="board_write">
		<table class="board_write_table" summary="엑셀 다운로드 사유 관리">
			<colgroup>
				<col style="width:220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">검색구분</td>
					<td>
						<div class="flex_box gap-08">
							<form:select path="srchKey" title="아이디" class="wd-150">
								<form:option value="LOGIN_ID">관리자ID</form:option>
								<form:option value="DOWNLOAD_MENU">다운로드 메뉴</form:option>
								<form:option value="REASON">사유</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter wd-500" type="text"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">등록일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="srchStartLogDate" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="srchEndLogDate" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>
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
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/log/exceldownload-log'">초기화</button>
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

	<table class="board_list_table" summary="엑셀 다운로드 사유 관리">
		<caption>엑셀 다운로드 사유 관리</caption>
		<colgroup>
			<col style="width:50px;">
			<col style="width:150px;">
			<col style="width:100px;">
			<col style="width:150px;">
			<col style="width:200px;">
			<col style="width:250px;">
			<col style="width:150px;">
			<col style="width:100px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">등록일</th>
				<th scope="col">관리자ID</th>
				<th scope="col">다운로드 메뉴</th>
				<th scope="col">URL</th>
				<th scope="col">사유</th>
				<th scope="col">사유타입</th>
				<th scope="col">수정이력</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><div><c:out value="${pagination.itemNumber - i.count}"/></div></td>
							<td><div><c:out value="${fn:escapeXml(item.createdAt)}"/></div></td>
							<td><div><c:out value="${fn:escapeXml(item.loginId)}"/></div></td>
							<td><div><c:out value="${fn:escapeXml(item.name)}"/></div></td>
							<td><div><c:out value="${fn:escapeXml(item.url)}"/></div></td>
							<td>
								<div>
									<a href="javascript:reasonPopup('${fn:escapeXml(item.id)}')">
										<c:out value="${fn:substring(item.reason, 0, 50)}"/><c:out value="${fn:length(item.reason) > 50 ? '...' : ''}"/>
									</a>
								</div>
							</td>
							<td><div><c:out value="${item.reasonType}"/></div></td>
							<c:choose>
								<c:when test="${item.histCnt > 1}">
									<td>
										<div>
											<a href="javascript:reasonHistPopup('${fn:escapeXml(item.id)}')">이력보기</a>
										</div>
									</td>
								</c:when>
								<c:otherwise>
									<td><div>-</div></td>
								</c:otherwise>
							</c:choose>
							<%-- <td><div><c:out value="${item.histCnt}"/></div></td> --%>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="8">엑셀 다운로드 사유 정보가 없습니다.</td>
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

		// 기간 검색 적용 (검색조건 : 등록일)
		serachDate();

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();

		// 날짜 변경 이벤트
		datepickerChange();

		// 검색 enter key
		searchEnterKey();

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="srchStartLogDate"]' , 'input[name="srchEndLogDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});

	/**
	 *	함 수 명 : serachDate
	 *	기	능  : 기간 검색 적용
	 */
	function serachDate() {
		$(".btn_date").on('click',function(){
			var $id = $(this).attr('class').replace('btn_date ','');
			if ($id == 'all') {
				$("input[type=text]",$(this).parent().parent()).val('');

			} else {
				var today = $("#today").text();
				var date1 = '';
				var date2 = '';

				if ($id == 'today') {
					date1 = today;
					date2 = today;
				} else {
					date1 = $("#"+$id).text();
					date2 = today;
				}

				$("input[type=text]",$(this).parent().parent()).eq(0).val(date1);
				$("input[type=text]",$(this).parent().parent()).eq(1).val(date2);
			}
		});
	}

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
		var strStartDate = $("#srchStartLogDate").val();
		var strEndDate = $("#srchEndLogDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#srchEndLoginDate").val();
				$("#srchEndLogDate").val("");
				$("#srchEndLogDate").focus();
				$("#srchEndLogDate").val(value);
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		$("#privacyLogParam").submit();
	}

	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#srchValue, #srchStartLogDate, #srchEndLogDate").on('keydown', function(e){
			if (e.keyCode == '13') {
				search();
			}
		});
	}

	/**
	 *	함 수 명 : reasonPopup
	 *	기	능  : 엑셀 다운로드 사유 상세 팝업
	 */
	function reasonPopup(id) {
		var url = "/opmanager/log/exceldownload-log/popup/"+id;
		var popupName = "/opmanager/log/exceldownload-log/popup/exceldownload-log-detail";
		Common.popup(url, popupName, 700, 450, 1, 0, 0);
	}

	/**
	 *	함 수 명 : reasonHistPopup
	 *	기	능  : 엑셀 다운로드 수정 이력 내역 팝업
	 */
	function reasonHistPopup(id) {
		var url = "/opmanager/log/exceldownload-log/popup/hist/"+id;
		var popupName = "/opmanager/log/exceldownload-log/popup/exceldownload-log-hist";
		Common.popup(url, popupName, 800, 450, 1, 0, 0);
	}
</script>
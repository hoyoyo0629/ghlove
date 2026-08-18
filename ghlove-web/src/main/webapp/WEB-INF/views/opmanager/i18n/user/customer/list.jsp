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

<h3><span><c:out value="${op:message('M00210')}"/></span></h3>

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
							<form:select path="srchKey" title="아이디" class="wd-150">
								<form:option value="LOGIN_ID">아이디</form:option>
								<form:option value="USER_NAME">이름</form:option>
								<form:option value="ADDRESS">주소</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter half" type="text"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>가입일</td>
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
								<a href="javascript:void(0);" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">가입구분</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<form:radiobutton path="srchSbscrbSeCode" value="" label="전체" checked="checked" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchSbscrbSeCode" value="100" label="온라인" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchSbscrbSeCode" value="200" label="오프라인" />
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">Email 수신동의</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<form:radiobutton path="srchReceiveEmail" value="" label="전체" checked="checked" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchReceiveEmail" value="0" label="동의" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchReceiveEmail" value="1" label="비동의" />
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/user/customer/list'">초기화</button>
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
	<table class="board_list_table" summary="일반회원관리">
		<caption>일반회원관리</caption>
		<colgroup>
			<col style="width:5%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">가입일</th>
				<th scope="col">아이디</th>
				<th scope="col">이름</th>
				<th scope="col">주소</th>
				<th scope="col">이메일 수신동의</th>
				<th scope="col">기부누적액</th>
				<th scope="col">포인트잔액</th>
				<th scope="col">가입경로</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><c:out value="${pagination.itemNumber - i.count}"/></td>
							<td><c:out value="${item.createdDate}"/></td>
							<td><a href="javascript:customerDetails('${fn:escapeXml(item.userId)}');"><c:out value="${item.loginId}"/></a></td>
							<td><a href="javascript:customerDetails('${fn:escapeXml(item.userId)}');"><c:out value="${item.userName}"/></a></td>
							<td><c:out value="${item.address}"/> <c:out value="${item.addressDetail}"/></td>
							<td><c:out value="${item.receiveEmail eq '0' ? '동의' : '비동의'}"/></td>
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
							<td><c:out value="${item.sbscrbSeNm}"/></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="9">일반회원 정보가 존재하지 않습니다.</td>
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

		// 기간 검색 적용 (검색조건 : 가입일)
		serachDate();

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();

		// 날짜 변경 이벤트
		datepickerChange();

		// 검색 enter key
		searchEnterKey();

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="srchStartCreated"]' , 'input[name="srchEndCreated"]');
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
	 *	함 수 명 : customerDetails
	 *	기	능  : 일반회원 상세 조회
	 *	파라미터  : userId - 사용자 ID
	 */
	function customerDetails(userId) {
		location.href = "/opmanager/user/customer/details/"+userId+location.search;
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
		 console.log('event call~~~');
		var strStartDate = $("#srchStartCreated").val();
		var strEndDate = $("#srchEndCreated").val();

		if(strStartDate === '' || strEndDate === '') {
			alert('가입일을 입력 하십시오');
			return false;
		}
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
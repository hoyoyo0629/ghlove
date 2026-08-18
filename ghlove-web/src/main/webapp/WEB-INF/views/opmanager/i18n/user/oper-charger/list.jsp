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

<h3><span><c:out value="${op:message('MENU_1101')}"/></span></h3>

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
							<form:select path="srchKey" title="회원구분" class="wd-150">
								<form:option value="LOGIN_ID">아이디</form:option>
								<form:option value="USER_NAME">이름</form:option>
								<form:option value="PHONE_NUMBER">휴대폰</form:option>
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
								<a href="javascript:void(0);" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">회원구분</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form"><form:checkbox id="srchAuthority_all" path="srchArrAuthority" value="ALL" label="전체"/></div>
							<div class="input-form"><form:checkbox id="srchAuthority_1" path="srchArrAuthority" value="ROLE_ADMIN_1" label="시스템 주관리자"/></div>
							<div class="input-form"><form:checkbox id="srchAuthority_2" path="srchArrAuthority" value="ROLE_ADMIN_2" label="시스템 부관리자"/></div>
							<div class="input-form"><form:checkbox id="srchAuthority_3" path="srchArrAuthority" value="ROLE_ADMIN_3" label="행안부 주담당자" /></div>
							<div class="input-form"><form:checkbox id="srchAuthority_4" path="srchArrAuthority" value="ROLE_ADMIN_4" label="행안부 부담당자"/></div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">사용여부</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<form:radiobutton path="srchStatusCode" value="" label="전체" checked="checked" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchStatusCode" value="9" label="사용" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchStatusCode" value="2" label="중지" />
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/user/oper-charger/list'">초기화</button>
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
	<table class="board_list_table" summary="운영자관리">
		<caption>운영자관리</caption>
		<colgroup>
			<col style="width:5%;">
			<col style="width:5%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="check_all"></th>
				<th scope="col">No.</th>
				<th scope="col">권한</th>
				<th scope="col">소속부서</th>
				<th scope="col">아이디</th>
				<th scope="col">이름</th>
				<th scope="col">휴대폰</th>
				<th scope="col">등록일</th>
				<th scope="col">사용여부</th>
				<th scope="col">중지일자</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td><input type="checkbox" value="${fn:escapeXml(item.userId)}" id="check_${fn:escapeXml(item.userId)}"></td>
							<td><c:out value="${pagination.itemNumber - i.count}"/></td>
							<td>
								<c:choose>
									<c:when test="${item.authority eq 'ROLE_ADMIN_1'}">시스템 주관리자</c:when>
									<c:when test="${item.authority eq 'ROLE_ADMIN_2'}">시스템 부관리자</c:when>
									<c:when test="${item.authority eq 'ROLE_ADMIN_3'}">행안부 주담당자</c:when>
									<c:when test="${item.authority eq 'ROLE_ADMIN_4'}">행안부 부담당자</c:when>
								</c:choose>
							</td>
							<td><c:out value="${item.psitnDeptNm}"/></td>
							<td><a href="javascript:chargerEdit('${fn:escapeXml(item.userId)}')"><c:out value="${item.loginId}"/></a></td>
							<td><a href="javascript:chargerEdit('${fn:escapeXml(item.userId)}')"><c:out value="${item.userName}"/></a></td>
							<td><c:out value="${item.phoneNumber}"/></td>
							<td><c:out value="${item.createdDate}"/></td>
							<td><c:out value="${item.statusCode eq 2 ? '중지' : '사용'}"/></td>
							<td><c:if test="${item.statusCode eq 2}"><c:out value="${item.denyDate}"/></c:if></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="10">운영관리자 정보가 존재하지 않습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<c:if test="${adminRole eq 'ROLE_ADMIN_1' or adminRole eq 'ROLE_ADMIN_3'}">
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-default btn-mini" onclick="chargerDelete();">삭제</button>
			</div>
		</div>
	</c:if>
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
		checkedEventSet("check_", "check_all");

		// 회원구분 검색 체크박스 이벤트 (체크&해제)
		checkedEventSet("srchAuthority_", "srchAuthority_all");

		// 회원구분 페이지 진입시 전체 체크
		if("${fn:escapeXml(searchParam.srchKey)}" == "") {
			$("#srchAuthority_all").trigger("click");
		}

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
	function checkedEventSet(groupId, groupHeaderId) {
		var childObjs = $("input[id^='"+groupId+"']").not("[id='"+groupHeaderId+"']");

		// 체크박스 '전체' 체크&해지
		$("#"+groupHeaderId).click(function(){
			$(childObjs).prop("checked", $(this).is(":checked"));
		});

		// '개별' 체크박스에 따른 '전체' 체크박스 체크&해지
		$(childObjs).click(function() {
			if($(childObjs).length == $("input[id^='"+groupId+"']:checked").not("[id='"+groupHeaderId+"']").length) {
				$("#"+groupHeaderId).prop("checked", true);
			} else {
				$("#"+groupHeaderId).prop("checked", false);
			}
		});
	}

	/**
	 *	함 수 명 : chargerDelete
	 *	기	능  : 운영관리자 삭제
	 */
	function chargerDelete() {
		var userIdList = new Array;

		// 선택된 사용자 셋팅
		$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
			userIdList.push($(item).val());
		});

		if(userIdList.length == 0) {
			alert("처리할 항목을 선택해 주세요.");
			return false;
		}

		if(confirm("삭제하시겠습니까?")) {
			$.post('/opmanager/user/locgov-charger/delete', {"userIdList": userIdList}, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						alert("삭제되었습니다.");

						if(response.data.isLogout == "Y") {
							location.href = "/op_security_logout?target=/opmanager";
						} else {
							location.reload();
						}
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}

	/**
	 *	함 수 명 : chargerEdit
	 *	기	능  : 운영관리자 상세 조회
	 *	파라미터  : userId - 사용자 ID
	 */
	function chargerEdit(userId) {
		location.href = "/opmanager/user/oper-charger/edit/"+userId+location.search;
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
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

<h3><span><c:out value="${op:message('MENU_4601')}"/></span></h3>

<!-- Search -->
<form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="post">
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
							<form:select path="srchKey" title="지점명" class="wd-150">
								<form:option value="PSITN_NM">지점명</form:option>
								<form:option value="LOGIN_ID">아이디</form:option>
								<form:option value="USER_NAME">이름</form:option>
								<form:option value="EMP_ID">개인번호</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter half" type="text"/>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/user/off-charger/list'">초기화</button>
				<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_7')}">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="chargerCreate()" >등록</button>
				</c:if>
				<button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
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
	<table class="board_list_table" summary="오프라인 담당자관리">
		<caption>오프라인 담당자관리</caption>
		<colgroup>
			<col style="width:5%;">
			<col style="width:5%;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="check_all"></th>
				<th scope="col">No.</th>
				<th scope="col">구분</th>
				<th scope="col">소속 지점</th>
				<th scope="col">아이디</th>
				<th scope="col">이름</th>
				<th scope="col">개인번호</th>
				<th scope="col">사용여부</th>
				<th scope="col">중지일자</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td>
								<c:set var="disabledCheck" value="${(adminRole eq 'ROLE_ADMIN_7' and searchParam.loginUserId eq item.userId) ? 'disabled' : ''}" />
								<input type="checkbox" value="${fn:escapeXml(item.userId)}" id="check_${fn:escapeXml(item.userId)}" ${fn:escapeXml(disabledCheck)}>
							</td>
							<td><c:out value="${pagination.itemNumber - i.count}"/></td>
							<td><c:out value="${item.authority eq 'ROLE_ADMIN_7' ? '주관리자' : '-'}"/></td>
							<td><c:out value="${item.bankNm}"/>&nbsp;<c:out value="${item.psitnNm}" escapeXml="false"/></td>
							<td><a href="javascript:chargerEdit('${fn:escapeXml(item.userId)}')"><c:out value="${item.loginId}"/></a></td>
							<td><c:out value="${item.userName}"/></td>
							<td><c:out value="${item.empId}"/></td>
							<td><c:out value="${item.statusCode eq 2 ? '중지' : '사용'}"/></td>
							<td><c:if test="${item.statusCode eq 2}"><c:out value="${item.denyDate}"/></c:if></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr style="background:#fff;">
						<td colspan="9">오프라인담당자 정보가 존재하지 않습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>

	<div class="flex_box juc-sbt">
		<div class="btn_all">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel();">전체 목록 엑셀 다운로드</button>
			</div>
		</div>
		<div class="btn_all">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="statusChange('9');">사용</button>
				<button type="button" class="btn btn-default btn-mini" onclick="statusChange('2');">중지</button>
			</div>
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

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">
	$(function(){

		// 기간 검색 적용 (검색조건 : 등록일)
		serachDate();

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();

		// 체크박스 이벤트 (체크&해제)
		checkedEventSet();
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
			$('#searchParam').submit();
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
		var childObjs = $("input[id^='check_']").not(":disabled").not("[id='check_all']");

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
	 *	함 수 명 : downloadExcel
	 *	기	능  : 엑셀 다운로드
	 */
	function downloadExcel() {
		Shop.downloadExcelOrder("/opmanager/user/off-charger/list/download-excel", $('#searchParam').serialize(), true);
	}

	/**
	 *	함 수 명 : statusChange
	 *	기	능  : 사용여부 변경
	 *	파라미터  : type - 2(중지), 9(사용)
	 */
	function statusChange(type) {
		var userIdList = new Array;

		// 선택된 사용자 셋팅
		$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
			userIdList.push($(item).val());
		});

		if(userIdList.length == 0) {
			alert("처리할 항목을 선택해 주세요.");
			return false;
		}

		var msg = "선택된 아이디 권한을 사용으로 변경 하시겠습니까?";
		if(type == 2) msg = "선택된 아이디 사용 권한을 중지 하시겠습니까?";
		if(confirm(msg)) {
			$.post("/opmanager/user/off-charger/status/edit", {"userIdList": userIdList, "statusCode": type}, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						msg = (type == 2) ? "중지" : "사용";
						alert(msg+"되었습니다.");
						location.reload();
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}

	/**
	 *	함 수 명 : chargerEdit
	 *	기	능  : 지자체 담당자 상세 조회
	 *	파라미터  : userId - 사용자 ID
	 */
	function chargerEdit(userId) {
		location.href = "/opmanager/user/off-charger/edit/"+userId+location.search;
	}


	/**
	 *	함 수 명 : insertCharger
	 *	기	능  : 지자체 담당자 등록
	 *	파라미터  :
	 */
	function chargerCreate() {
		location.href = "/opmanager/user/off-charger/create";
	}

</script>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h3>
	<span></span>
</h3>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage" />

	<div class="board_write">
		<table class="board_write_table" summary="${op:message('MENU_5110')}">
			<colgroup>
				<col style="width: 220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">소속</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:select path="searchRole" title="소속" class="wd-150"
								onChange="adminChange(this.value)">
								<form:option value="">전체</form:option>
								<form:option value="ROLE_ADMIN_1">시스템 관리자</form:option>
								<form:option value="ROLE_ADMIN_3">행정안전부</form:option>
								<form:option value="ROLE_ADMIN_5">지자체</form:option>
							</form:select>
							<!-- 지자체가 선택되면 2, 3차 카테고리(시도/군구) 선택 -->
							<form:select path="shWdr" class="wd-150" style="display:none"
								onChange="wdrChange(this.value)">
								<form:option value="">-시·도 선택-</form:option>
								<c:forEach items="${wdr}" var="wdr">
									<form:option value="${wdr.id}" label="${wdr.label}" />
								</c:forEach>
							</form:select>

							<form:select path="locgovCode" class="wd-150"
								style="display:none">
								<option value="">-시·군·구-</option>
							</form:select>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><c:out value="${op:message('M00011')}" /></td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:select path="where" class="wd-150">
								<form:option value="">전체</form:option>
								<form:option value="SUBJECT" label="${op:message('M00275')}" />
								<%-- <form:option value="USERNAME">작성자</form:option> --%>
							</form:select>
							<form:input path="query"
								cssClass="input_txt required _filter half"
								title="${op:message('M00021')}" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><c:out value="${op:message('M00202')}" /></td>
					<td colspan="3">
						<div class="search-date">
							<span class="datepicker"><form:input path="startDt"
									cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span> <span class="datepicker"><form:input
									path="endDt" cssClass="datepicker optional "
									title="${op:message('M00509')}" /></span> <span class="day_btns">
								<a href="javascript:void(0);" class="btn_date today"><c:out
										value="${op:message('M00026')}" /></a> <a
								href="javascript:void(0);" class="btn_date week-1"><c:out
										value="${op:message('M00027')}" /></a> <a
								href="javascript:void(0);" class="btn_date month-1"><c:out
										value="${op:message('M00029')}" /></a> <a
								href="javascript:void(0);" class="btn_date month-3"><c:out
										value="${op:message('M00030')}" /></a> <a
								href="javascript:void(0);" class="btn_date year-1"><c:out
										value="${op:message('M00031')}" /></a> <a
								href="javascript:void(0);" class="btn_date clear"><c:out
										value="${op:message('M00039')}" /></a>
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
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-default btn-mini"
					onclick="location.href='/opmanager/community/databoard/list'">
					<c:out value="${op:message('M00047')}" />
				</button>
				<!-- 초기화 -->
				<button type="button" class="btn btn-dark-gray btn-mini"
					onclick="search();">
					<c:out value="${op:message('M00048')}" />
				</button>
				<!-- 검색 -->
			</div>
		</div>
	</div>
</form:form>

<div class="count_title mt-40">
	<h5>
		총
		<c:out value="${op:numberFormat(pagination.totalItems)}" />
		건
	</h5>
	<!-- 전체 -->
	<!-- 건 조회 -->
	<span> <select name="displayCount" id="displayCount"
		title="${op:message('M00239')}">
			<option value="10"><c:out value="${op:message('M00240')}" /></option>
			<option value="20"><c:out value="${op:message('M00241')}" /></option>
			<option value="50"><c:out value="${op:message('M00242')}" /></option>
			<option value="100"><c:out value="${op:message('M00243')}" /></option>
	</select>
	</span>
</div>

<div class="board_list">
	<table class="board_list_table" summary="${op:message('MENU_5000')}">
		<caption>
			<c:out value="${op:message('MENU_5000')}" />
		</caption>
		<colgroup>
			<col style="width: 50px;">
			<col style="width: 150px;">
			<%-- <col style="width: 50px;"> --%>
			<col style="width: 600px;">
			<col style="width: 100px;">
			<col style="width: 150px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">소속</th>
				<!-- <th scope="col">작성자</th> -->
				<th scope="col">제목</th>
				<th scope="col">조회수</th>
				<th scope="col">등록일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="databoard" varStatus="i">
				<tr>
					<td><c:out value="${pagination.itemNumber - i.count}" /></td>
					<c:choose>
						<c:when
							test="${databoard.authority == 'ROLE_ADMIN_1' || databoard.authority == 'ROLE_ADMIN_2'}">
							<td><c:out value="시스템 관리자" /></td>
							<%-- <td><c:out value="${databoard.userName }" /></td> --%>
						</c:when>
						<c:when
							test="${databoard.authority == 'ROLE_ADMIN_3' || databoard.authority == 'ROLE_ADMIN_4'}">
							<td><c:out value="행정안전부" /></td>
							<%-- <td><c:out value="${databoard.userName }" /></td> --%>
						</c:when>
						<c:otherwise>
							<td><c:out value="${databoard.upperLocgovNm}" /> <c:out
									value="${databoard.locgovNm}" /></td>
							<%-- <td><c:out value="${databoard.userName }" /></td> --%>
						</c:otherwise>
					</c:choose>
					<td>
						<div class="databoard-title-wrap">
							<c:if test="${databoard.noticeYn == 'Y'}">
								<span class="label_notice">공지</span>
							</c:if>
							<a class="databoard-title"
								href="javascript:databoardDetail(${fn:escapeXml(databoard.rpstrId)})"> <c:out
									value="${databoard.rpstrTtl}" /></a>
							<c:if test="${not empty databoard.orgnlAtchFileNm}">
								<img class="databoard-icon"
									src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료 있음" />
							</c:if>
						</div>
					</td>
					<td><c:out value="${op:numberFormat(databoard.inqCnt)}" /></td>
					<c:set var="formattedDate">
						<fmt:formatDate value="${databoard.frstCrtDt}"
							pattern="yyyy-MM-dd" />
					</c:set>
					<td><c:out value="${formattedDate}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<c:if test="${empty list}">
		<div class="no_content">
			<c:out value="${op:message('M00170')}" />
		</div>
	</c:if>
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<!-- 체크박스 일괄 삭제  -->
			<a href="<c:url value="/opmanager/community/databoard/create" />"
				class="btn btn-dark-gray btn-mini"><c:out
					value="${op:message('M00088')}" /> </a>
		</div>
	</div>
	<div class="pagination-wrap">
		<p class="pagination op-pagination">
			<page:pagination-manager />
		</p>
	</div>
</div>



<div style="display: none;">
	<span id="today"><c:out value="${today}" /></span> <span id="week"><c:out
			value="${week}" /></span> <span id="month1"><c:out value="${month1}" /></span>
	<span id="month2"><c:out value="${month2}" /></span>
</div>

<link rel="stylesheet" type="text/css" href="/content/css/community.css">
<script type="text/javascript">
	$(function() {
		searchEnterKey()
		displayChange();
		displaySelected();

		adminChange($("#searchRole").val());
		wdrChange($("#shWdr").val());

		// 체크박스 이벤트 (체크&해제)
		checkedEventSet();

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '',
				'input[name="startDt"]', 'input[name="endDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	})

	function displayChange() {
		$("#displayCount").on('change', function() {
			$("#itemsPerPage").val($(this).val());
			$('#searchParam').submit();
		});
	}

	/**
	 *	함 수 명 : adminChange
	 *	기	능  : 소속 변경 이벤트, 지자체 선택 시 2, 3차 카테고리 보여줌
	 */
	function adminChange(value) {
		if ($("#searchRole").val() == "ROLE_ADMIN_5") {
			$("#shWdr").css('display', 'block');
			$("#locgovCode").css('display', 'block');
		} else {
			$("#shWdr").css('display', 'none');
			$("#locgovCode").css('display', 'none');
			$("#shWdr").val('');
			$("#locgovCode").val('');
		}
	}

	function wdrChange(value) {
		Common.loading.display = false;
		$("#locgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$
					.post(
							url("/opmanager/give/give-state/options-by-locgovCode"),
							{
								'code' : value
							},
							function(response) {

								for (var i = 0; i < response.length; i++) {
									var options = '<option value="' + response[i].LOCGOV_CODE + '">'
											+ response[i].LOCGOV_NM
											+ '</option>';
									$('#locgovCode').append(options);
								}

								// 조회 된 값 유지
								if ("${searchParam.locgovCode}" != "") {
									$("#locgovCode").val(
											'${searchParam.locgovCode}').prop(
											"selected", true);
								}

							});
		} else {
			$('#locgovCode').append('<option value="">-시·군·구-</option>');
		}
	}

	function displaySelected() {
		$("#displayCount").val($("#itemsPerPage").val());
	}

	function deleteNotice(dataId) {
		Common.confirm("${op:message('M00196')}", function() {
			$.post(url("/opmanager/community/databoard/" + dataId), {},
					function(response) {
						Common.responseHandler(response, function() {
							alert("${op:message('M00205')}");
							location.reload();
						});
					});
		});

	}

	function deleteCheckDataboard() {
		var databoardList = new Array;

		$("input[id^='check_']:checked").not("[id='check_all']").map(
				function(index, item) {
					databoardList.push($(item).val());
				});

		if (databoardList.length == 0) {
			alert("삭제할 게시물을 선택해주세요.");
			return false;
		}

		if (confirm("삭제하시겠습니까?")) {
			$.post('/opmanager/community/databoard/deleteDataboard', {
				"databoardList" : databoardList
			}, function(response) {
				if (response.isSuccess && response.data) {
					if (response.data == "SUCC") {
						alert("삭제되었습니다.");
						location.reload();
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}

	function databoardDetail(dataId) {

		location.href = '/opmanager/community/databoard/detail/' + dataId;
	}

	/**
	 *	함 수 명 : checkedEvent
	 *	기	능  : 체크&해제 이벤트 셋팅
	 */
	function checkedEventSet() {
		var childObjs = $("input[id^='check_']").not("[id='check_all']");

		// 체크박스 '전체' 체크&해지
		$("#check_all").click(function() {
			$(childObjs).prop("checked", $(this).is(":checked"));
		});

		// '개별' 체크박스에 따른 '전체' 체크박스 체크&해지
		$(childObjs).click(
				function() {
					if ($(childObjs).length == $("input[id^='check_']:checked")
							.not("[id='check_all']").length) {
						$("#check_all").prop("checked", true);
					} else {
						$("#check_all").prop("checked", false);
					}
				});
	}

	function search() {
		var strStartDate = $("#startDt").val();
		var strEndDate = $("#endDt").val();

		if (strStartDate && strStartDate.length == 8 && strEndDate
				&& strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate
					.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(
					4, 2), strEndDate.substr(6, 2));
			if (startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#endDt").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if (!searchChk)
				return false;
		}

		$("#searchParam").submit();
	}

	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#query").on('keydown', function(e) {
			if (e.keyCode == '13') {
				$("#searchParam").submit();
			}
		});
	}
</script>
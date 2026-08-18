<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>


<div>
	<h3>
		<span></span>
	</h3>
	<!-- <p style="margin-top:10px; margin-bottom:50px; text-align:center; font-size: 20px; font-weight: 400;">행안부와 지자체를 위한 자유로운 소통방입니다.</p> -->
	<p style="margin-top:10px; margin-bottom:50px; text-align:center; font-size: 20px; font-weight: 400; color:red;">개선하고 싶은 기능에 대한 건의 및 시스템 오류 사항에 대해 작성 부탁드립니다.</p>
</div>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>


<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage" />
	<div class="board_write">
		<table class="board_write_table" summary="${op:message('운영관리 SR게시판')}">
			<colgroup>
				<col style="width: 220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">검색구분</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:select path="where" title="검색구분" class="wd-150">
								<form:option value="ALL">전체</form:option>
								<form:option value="CSRNO">CSR번호</form:option>
								<form:option value="SUBJECT">제목</form:option>
								<form:option value="CN">내용</form:option>
								<%-- <form:option value="USERNAME">작성자</form:option> --%>
							</form:select>
							<form:input path="query"
								cssClass="input_txt required _filter half"
								title="${op:message('M00021')}" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">완료구분</td>
					<td colspan="3">
						<div class="flex_box gap-12">
							<div class="input-form">
								<div class="input-form"><form:checkbox id="shProcessState_all" path="shArrProcessState" value="ALL" label="전체"/></div>
	                        </div>
							<c:forEach items="${procsStateCds}" var="stateCd"  varStatus="status">
								<div class="input-form">
									<form:checkbox id="shProcessState_${status.index+1}" path="shArrProcessState" value="${fn:escapeXml(stateCd.id)}" label="${fn:escapeXml(stateCd.label)}"/>
								</div>
			                 </c:forEach>
                        </div>
					</td>
				</tr>
				<tr>
					<td class="label">등록일자</td>
					<td colspan="3">
						<div class="search-date">
							<span class="datepicker"> <form:input path="startDt"
									cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span> <span class="datepicker"> <form:input
									path="endDt" cssClass="datepicker optional "
									title="${op:message('M00509')}" /></span> <span
								class="day_btns day_btn1"> <a href="javascript:void(0);"
								class="btn_date today"><c:out
										value="${op:message('M00026')}" /></a> <a
								href="javascript:void(0);" class="btn_date week-1"><c:out
										value="${op:message('M00027')}" /></a> <a
								href="javascript:void(0);" class="btn_date month-1"><c:out
										value="${op:message('M00029')}" /></a> <a
								href="javascript:void(0);" class="btn_date month-3"><c:out
										value="${op:message('M00030')}" /></a> <a
								href="javascript:void(0);" class="btn_date year-1"><c:out
										value="${op:message('M00031')}" /></a> <a
								href="javascript:void(0);" class="btn_date all-1"><c:out
										value="${op:message('M00039')}" /></a> <!-- 전체 -->
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
					onclick="location.href='/opmanager/maintenance/list'">
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
	<table class="board_list_table" summary="${op:message('')}">
		<caption>
			<c:out value="${op:message('')}" />
		</caption>
		<colgroup>
			<col style="width: 50px;">
			<col style="width: 70px;">
			<col style="width: 80px;">
			<col style="width: 120px;">
			<col style="width: 80px;">
			<col style="width: 200px;">
			<col style="width: *;">
			<col style="width: 100px;">
			<col style="width: 70px;">
			<col style="width: 200px">
			<col style="width: 70px;">
			<col style="width: 100px;">
			<col style="width: 100px;">
			<col style="width: 100px;">
			<col style="width: 100px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">작성자</th>
				<th scope="col">업무구분</th>
				<th scope="col">CSR번호</th>
				<th scope="col">처리구분</th>
				<th scope="col">제목</th>
				<th scope="col">내용</th>
				<th scope="col">요청경로</th>
				<th scope="col">담당자</th>
				<th scope="col">답변</th>
				<th scope="col">완료구분</th>
				<th scope="col">등록일시</th>
				<th scope="col">수정일시</th>
				<th scope="col">처리시작일</th>
				<th scope="col">처리종료일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="community" varStatus="i">
				<tr>
					<td><c:out value="${pagination.itemNumber - i.count}" /></td>
					<td><c:out value="${fn:escapeXml(community.reqManagerNm)}" /></td>
					<td><c:out value="${fn:escapeXml(community.reqTypeNm)}" /></td>
					<td><c:out value="${fn:escapeXml(community.srNo)}" /></td>
					<td><c:out value="${fn:escapeXml(community.processTypeNm)}" /></td>
					<td>
						<div style="align-items: center; text-align: left; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
							<a class="communityId"
								href="javascript:maintenanceDetail('${fn:escapeXml(community.bbsId)}')"
								style="max-width: 400px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block;">${fn:escapeXml(community.bbsTtl)}</a>
							<c:if test="${community.attachedFileCnt > 0}">
								<img class="databoard-icon"
									src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료 있음" />
							</c:if>
						</div>
					</td>
					<td><c:out value="${community.bbsCn}" /></td>
					<td><c:out value="${fn:escapeXml(community.reqChannelNm)}" /></td>
					<td><c:out value="${fn:escapeXml(community.processManagerNm)}" /></td>
					<td><c:out value="${community.processCn}" /></td>
					<td><c:out value="${fn:escapeXml(community.processStateNm)}" /></td>
					<td><fmt:formatDate value="${community.frstCrtDt}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${community.lastMdfcnDt}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><c:out value="${community.processStartDate}" /></td>
					<td><c:out value="${community.processEndDate}" /></td>

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
			<a href="<c:url value="/opmanager/maintenance/form" />"
				class="btn btn-dark-gray btn-mini">등록</a>
			<button type="button" class="btn btn-dark-gray btn-mini btn_left" onclick="javascript:excelDownload();">엑셀</button>
		</div>
	</div>
	<div class="pagination-wrap">
		<p class="pagination op-pagination">
			<page:pagination-manager />
		</p>
	</div>
</div>

<link rel="stylesheet" type="text/css"
	href="/content/css/community.css?after">
<script type="text/javascript">
	$(function() {
		searchEnterKey()
		displayChange();
		datepickerChange()
		displaySelected();

		// 회원구분 검색 체크박스 이벤트 (체크&해제)
		checkedEventSet("shProcessState_", "shProcessState_all");

		// 회원구분 페이지 진입시 전체 체크?
		if("${searchParam.shArrProcessState}" == "") {
			$("#shProcessState_all").trigger("click");
		}

		Common.DateButtonEvent.set('.day_btn1 > a[class^=btn_date]', '', 'input[name="startDt"]', 'input[name="endDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();

	})



	function displayChange() {
		$("#displayCount").on('change', function() {
			$("#itemsPerPage").val($(this).val());
			$('#searchParam').submit();
		});
	}
	function displaySelected() {
		$("#displayCount").val($("#itemsPerPage").val());
	}

	/**
	 *	함 수 명 : datepickerChange
	 *	기	능  : 날짜 변경 이벤트
	 */
	function datepickerChange() {
		if ($("input.datepicker").length) {
			$("input.datepicker").keyup(function(e) {
				if (e && e.target && e.target.value) {
					var value = e.target.value.replace(/[^0-9]/g, "");
					if (value.length > 8) {
						value = value.slice(0, 8);
					}
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
		if(0 == $("input[id^='shProcessState_']:checked").not("[id='shProcessState_all']").length) {
			$("#shProcessState_all").trigger("click");
		}

		$("#searchParam").submit();
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
			$.post('/opmanager/community/deleteDataboard', {
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

	function maintenanceDetail(id) {
// 		location.href = '/opmanager/maintenance/detail/' + id; //detail화면대신 edit화면으로
		location.href = '/opmanager/maintenance/edit/' + id;
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

	/**
	 * 함 수 명 : excelDownload
	 * 기  능  : 엑셀 다운로드
	 */
	function excelDownload() {
		let startDate = document.getElementById('startDt').value;
		let endDate = document.getElementById('endDt').value;
		let isDate = false;

		if (Common.validateDate(startDate) && Common.validateDate(endDate)) {
			if (Number(startDate) <= Number(endDate)) {
				isDate = true;
			}
		}
		if (!isDate) {
			alert('등록일자를 확인해주세요.');
			return;
		}
		let param = $('#searchParam').serialize();

		Shop.downloadExcelOrder("/opmanager/maintenance/list/mainten-excel-download", param, true);
	}
</script>




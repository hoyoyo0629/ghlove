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
		<table class="board_write_table" summary="${op:message('SR게시판')}">
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
									<form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
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
					<td class="label">검색구분</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:select path="where" title="검색구분" class="wd-150">
								<form:option value="ALL">전체</form:option>
								<form:option value="SUBJECT">제목</form:option>
								<%-- <form:option value="USERNAME">작성자</form:option> --%>
							</form:select>
							<form:input path="query"
								cssClass="input_txt required _filter half"
								title="${op:message('M00021')}" />
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
								href="javascript:void(0);" class="btn_date clear"><c:out
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
					onclick="location.href='/opmanager/community/srBbs/list'">
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
			<col style="width: 80px;">
			<col style="width: 150px;">
			<%-- <col style="width: 150px;"> --%>
			<col style="width: *;">
			<col style="width: 100px;">
			<col style="width: 150px;">
			<col style="width: 150px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">소속</th>
				<!-- <th scope="col">작성자</th> -->
				<th scope="col">제목</th>
				<th scope="col">조회수</th>
				<th scope="col">등록일시</th>
				<th scope="col">수정일시</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="community" varStatus="i">
				<tr>
					<td><c:out value="${pagination.itemNumber - i.count}" /></td>
					<c:choose>
						<c:when
							test="${community.authority == 'ROLE_ADMIN_1' || community.authority == 'ROLE_ADMIN_2'}">
							<td><c:out value="시스템 관리자" /></td>
							<%-- <td><c:out value="${community.userName }" /></td> --%>
						</c:when>
						<c:when
							test="${community.authority == 'ROLE_ADMIN_3' || community.authority == 'ROLE_ADMIN_4'}">
							<td><c:out value="행정안전부" /></td>
							<%-- <td><c:out value="${community.userName }" /></td> --%>
						</c:when>
						<c:otherwise>
							<td><c:out value="${community.upperLocgovNm}" /> <c:out
									value="${community.locgovNm}" /></td>
							<%-- <td><c:out value="${community.userName }" /></td> --%>
						</c:otherwise>
					</c:choose>
					<td>
						<div style="align-items: center; text-align: left; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
							<c:if test="${community.noticeYn == 'Y'}">
								<span class="label_notice">공지</span>
							</c:if>
							<a class="communityId"
								href="javascript:communityDetail('${fn:escapeXml(community.bbsId)}')"
								style="max-width: 400px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; display: inline-block;">${fn:escapeXml(community.bbsTtl)}</a>

							<c:if test="${community.cmntCnt > 0}">
								<span class="comment-count" data-community-id="${fn:escapeXml(community.bbsId)}" style="color: red; margin-left: 10px">[${fn:escapeXml(community.cmntCnt)}]</span>
							</c:if>
							<c:if test="${community.attachedFileCnt > 0}">
								<img class="databoard-icon"
									src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료 있음" />
							</c:if>
							<c:if test="${community.isSecret == 'Y'}">
								<img class="databoard-icon"
									src="/content/images/icon/icon_lock.png" alt="비밀글" />
							</c:if>
						</div>
					</td>
					<td><c:out value="${community.inqCnt}" /></td>
					<td><fmt:formatDate value="${community.frstCrtDt}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${community.lastMdfcnDt}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
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
			<a href="<c:url value="/opmanager/community/srBbs/form" />"
				class="btn btn-dark-gray btn-mini">등록</a>
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

		adminChange($("#searchRole").val());
		wdrChange($("#shWdr").val());

		// 체크박스 이벤트 (체크&해제)
		checkedEventSet();
		//댓글 갯수
		// getCommentCounts();
		Common.DateButtonEvent.set('.day_btn1 > a[class^=btn_date]', '',
				'input[name="startDt"]', 'input[name="endDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();

	})

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
								if ("${fn:escapeXml(searchParam.locgovCode)}" != "") {
									$("#locgovCode").val(
											'${fn:escapeXml(searchParam.locgovCode)}').prop(
											"selected", true);
								}

							});
		} else {
			$('#locgovCode').append('<option value="">-시·군·구-</option>');
		}
	}

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

	function communityDetail(id) {
		location.href = '/opmanager/community/srBbs/detail/' + id;
	}


	/*
	* 2024.09.10
	* 커뮤니티 테이블 변경으로 해당 함수 사용 안 함
	*/
	function getCommentCounts() {
		// 각 .comment-count 요소에 대한 댓글 수를 가져옵니다.
		$('.comment-count').each(
				function() {
					// 현재 요소의 data-community-id 속성을 사용하여 community.id를 추출합니다.
					var communityId = $(this).data('community-id');

					// 댓글 수를 비동기적으로 가져옵니다.
					$.get("/opmanager/community/comment/count/" + communityId,
							function(data) {
								if (data == 0) {
									// 데이터가 0이면 처리할 내용을 작성

								} else {
									// 댓글 수가 0이 아니면 실제 댓글 수를 출력
									$(this).text("[" + data + "]");
								}
							}.bind(this)); // 현재 요소를 함수 내에서 사용할 수 있도록 bind합니다.
				});
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




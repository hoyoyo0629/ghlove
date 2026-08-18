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
</div>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>


<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage" />
	<div class="board_write">
		<table class="board_write_table" summary="${op:message('FAQ게시판')}">
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
		<div class="jq_tabonoff comm_tab1">
			<%-- ul 한줄에 li태그 6개까지 배치 --%>
			<c:set var="faqTypeCnt" value="${fn:length(faqTypes)}"/>
			<c:set var="faqMod" value="${faqTypeCnt mod 6}"/>
			<input type="hidden" name="shFaqType" id="shFaqType" value="${searchParam.shFaqType }"/>
			<ul class="jq_tab tabs">
				<li <c:if test="${empty searchParam.shFaqType }">class="active"</c:if> data-id='' ><a href="javascript:;">전체</a></li>
				<c:forEach var="faqType" items="${faqTypes}" varStatus="status">
					<c:if test="${status.index != 0 && (status.index+1)%6 == 0 }">
						</ul>
						<ul class="jq_tab tabs">
					</c:if>
					<li <c:if test="${faqType.code eq searchParam.shFaqType }">class="active"</c:if> data-id='${faqType.code}'><a href="javascript:;" class="">${fn:escapeXml(faqType.title)}</a></li>
				</c:forEach>
				<c:if test="${faqMod != 0 }">
					<c:forEach begin="${faqMod +1}" end="5">
						<li><a href="javascript:;" class=""></a></li>
					</c:forEach>
				</c:if>
			</ul>
		</div>
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
				<li>
			    	질문유형 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			    </li>
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-default btn-mini"
					onclick="location.href='/opmanager/community/faqBbs/list'">
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
				<th scope="col">질문유형</th>
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
					<c:set var="faqTitle" value=""/>
					<c:forEach var="faqType" items="${faqTypes}">
						<c:if test="${faqType.code eq community.faqType }">
							<c:set var="faqTitle" value="${faqType.title}"/>
						</c:if>
                    </c:forEach>
                    <td><c:out value="${faqTitle}"/></td>
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
	<c:if test ="${ fn:escapeXml(role) eq 'ROLE_ADMIN_1'}">			<!-- 시스템주관리자만 올릴 수 있음 -->
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<a href="<c:url value="/opmanager/community/faqBbs/form" />"
				class="btn btn-dark-gray btn-mini">등록</a>
		</div>
	</div>
	</c:if>
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
		 showTab();
		//댓글 갯수
		// getCommentCounts();
		Common.DateButtonEvent.set('.day_btn1 > a[class^=btn_date]', '',
				'input[name="startDt"]', 'input[name="endDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();

	})

    function showTab() {
    	//$(".tab_content:first").show();
        $('.jq_tabonoff').delegate('.jq_tab>li', 'click', function() {
            var index = $(this).parent().children().index(this);
            $('.jq_tab>li').removeClass();
            $(this).addClass('active');
            $("#shFaqType").val($(this).data('id'));
            search();
           // $(this).parent().next('.jq_cont').children().hide().eq(index).show();
        });
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

	function communityDetail(id) {
		location.href = '/opmanager/community/faqBbs/detail/' + id;
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

<style>
	.admin_wrap .tabs li.active{
		border-bottom: 1px solid #999;

	}
</style>


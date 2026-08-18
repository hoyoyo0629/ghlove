<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

		<div class="location">
			<a href="#"></a>&gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
		</div>

		<h3>1:1 문의</h3>
		<form:form modelAttribute="qna" method="post" enctype="multipart/form-data">
			<div class="board_write">
				<table class="board_write_table">
					<caption><c:out value="${op:message('M00458')}"/></caption>
					<colgroup>
						<col style="width:220px;">
					</colgroup>
					<tbody>
<!-- 						 <tr> -->
<!-- 						 	<div> -->
<%-- 							 	<td class="label">${op:message('M00460')}</td> <!-- 문의유형 -->  --%>
<!-- 							 	<td> -->
<!-- 							 		<div> -->
<%-- 			 							<form:select path="qnaGroup" class="required form-control03"> --%>
<%-- 			 								<form:option value="" label="전체" /> --%>
<%-- 			 								<c:forEach items="${qnaGroups}" var="group"> --%>
<%-- 			 									<form:option value="${group.id}" label="${group.label}" /> --%>
<%-- 			 								</c:forEach> --%>
<%-- 			 							</form:select> --%>
<!-- 	 								</div> -->
<!-- 								</td> -->
<!-- 							</div> -->
<!-- 						 </tr> -->

						 <!-- 2014.12.30 추가 -->
<!-- 						<tr>	 -->
<!-- 							<div> -->
<!-- 							 	<td class="label">답변상태</td> -->
<!-- 							 	<td> -->
<!-- 							 		<div> -->
<%-- 							 			<form:radiobutton path="answerCount" value="0" checked="checked" label="${op:message('M00039')}" /> --%>
<%-- 										<form:radiobutton path="answerCount" value="1" label="${op:message('M00463')}" />  --%>
<%-- 										<form:radiobutton path="answerCount" value="2" label="${op:message('M00464')}" />  --%>
<!-- 							 		</div> -->
<!-- 							 	</td> -->
<!-- 							 </div>		 -->
<!-- 						</tr> -->

						<tr>
							<td class="label">검색구분</td> <!-- 검색구분 -->
							<td>
								<div class="flex_box gap-08">
								<form:select path="where" title="${op:message('M00468')}" class="wd-150"> <!-- 키워드선택 -->
									<%-- <form:option value="USER_NAME" label="${op:message('M00005')}" />  --%><!-- 이름 -->
									<!-- 2014.12.28 -->
									<%-- <form:option value="ALL" label="전체"></form:option> --%>
									<form:option value="GROUP" label="문의유형"/> <!-- 문의유형 -->
									<form:option value="LOGIN_ID" label="아이디" /> <!-- 아이디 -->
									<form:option value="SUBJECT" label="제목" /> <!-- 제목 -->
									<form:option value="QUESTION" label="내용" /> <!-- 문의내용 -->
								</form:select>
								<form:input type="text" path="query" class="input_txt required _filter half" title="${op:message('M00021')} " />  <!-- 검색어 입력 -->
							</div>
							</td>
						</tr>


						<tr>
							<div>
							 	<td class="label"><c:out value="${op:message('M00202')}"/></td> <!-- 등록일 -->
							 	<td>
							 		<div>
										<span class="datepicker"><form:input type="text" path="searchStartDate" class="datepicker _number" title="${op:message('M00507')}" /></span> <!-- 시작일 -->
										<span class="wave">~</span>
										<span class="datepicker"><form:input type="text" path="searchEndDate" class="datepicker _number" title="${op:message('M00509')}" /></span> <!-- 종료일 -->
										<span class="day_btns">
											<a href="javascript:;" class="table_btn today"><c:out value="${op:message('M00026')}"/></a>   <!-- 오늘 -->
											<a href="javascript:;" class="table_btn week-1"><c:out value="${op:message('M00027')}"/></a> <!-- 1주일 -->
											<a href="javascript:;" class="table_btn month-1"><c:out value="${op:message('M00029')}"/></a>  <!-- 한달 -->
											<a href="javascript:;" class="table_btn month-3"><c:out value="${op:message('M00030')}"/></a>  <!-- 3달 -->
											<a href="javascript:;" class="table_btn clear"><c:out value="${op:message('M00039')}"/></a> <!-- 전체 -->
										</span>
									</div>
							 	</td>
							 </div>
						 </tr>
						 <tr>
					 		<td class="label">상태</td> <!-- 등록일 -->
					 		<td>
			                    <div class="flex_box gap-12">
			                        <div class="input-form">
			                            <form:radiobutton path="qnaAnswerCode" value="" label="전체" checked="checked"/>
			                        </div>
			                        <div class="input-form">
			                            <form:radiobutton path="qnaAnswerCode" value="0" label="답변완료"/>
			                        </div>
			                        <div class="input-form">
			                            <form:radiobutton path="qnaAnswerCode" value="1" label="답변미완료"/>
			                        </div>
			                    </div>
			                </td>
						 </tr>

					</tbody>
				</table>
			</div> <!-- // board_write -->
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
				<li>
			    	검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			    </li>
		</div>
		<!-- 버튼시작 -->
			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/qna/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();"><c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
				</div>
			</div>
		<!-- 버튼 끝-->
			<div class="count_title mt20">
				<h5>총 <c:out value="${op:numberFormat(qnaCount)}"/>건</h5>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00239')}"> <!-- 화면출력 -->
						<form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
						<form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
						<form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
						<form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
					</form:select>
				</span>
			</div>
		</form:form>

		<!-- 2015.1.6 -->
		<form action="/opmanager/qna/list" method="post" id="listForm">
			<input type="hidden" value="${fn:escapeXml(list.qnaId)}">
				<div class="board_write">
					<table class="board_list_table" summary="${op:message('M00273')}"> <!-- 주문내역 리스트 -->
						<caption><c:out value="${op:message('M00273')}"/></caption>
						<colgroup>
							<col style="width:50px;">
                            <col style="width:50px;">
                            <col style="width:150px;">
                            <col style="width:500px;">
                            <col style="width:150px;">
                            <col style="width:150px;">
                            <col style="width:150px;">
						</colgroup>
						<thead>
							<tr>
								<th scope="col"><input type="checkbox" id="check_all" title="${op:message('M00169')}" /></th> <!-- 체크박스 -->
								<th scope="col">No.</th>
                                <th scope="col">문의유형</th>
                                <th scope="col">제목</th>
                                <th scope="col">작성자(아이디)</th>
                                <th scope="col">상태</th>
                                <th scope="col">등록일</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${qnaList}" var="list" varStatus="i">
								<tr>
									<td>
										<input type="checkbox" name="id" value="${fn:escapeXml(list.qnaId)}" title=${op:message('M00169')} />
									</td>
									<td><c:out value="${pagination.itemNumber - i.count}"/></td>
									<td>
										<c:out value="${list.qnaGroup}"/>
									</td>
									<td class="tex_l">
										<%-- <a href="/opmanager/qna/view/${list.qnaId}">${list.subject}</a> <!-- 제목 --> --%>
										<a href="javascript:qnaDetail(${fn:escapeXml(list.qnaId)});">${op:nl2br(list.subject)}</a> <!-- 제목 -->
									</td>
<%-- 									<td>
										<div>
											${list.userId > 0 ? op:message('M00465') : op:message('M00466')}
										</div>
									</td> --%>
									 <!-- 회원 --> <!-- 비회원 -->
									<td>
										<c:choose>
											<c:when test="${list.userId > 0 && !empty list.loginId}">
<%-- 												<a href="javascript:Common.popup('/opmanager/user/popup/details/${list.userId}', '/user/popup/details', 1100, 800, 1, 0, 0)">${list.userName} (${list.loginId})</a> --%>
												<c:out value="${list.userName}"/> (<c:out value="${list.loginId}"/>)
											</c:when>
											<c:when test="${!empty list.userName}">
												<c:out value="${list.userName}"/>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td><c:out value="${list.answerCount > 0 ? op:message('M00463') : op:message('M00464')}"/></td> <!-- 답변완료 --> <!-- 답변대기 -->
									<td><c:out value="${op:datetime(list.createdDate)}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div><!--// board_write E-->

			<c:if test="${empty qnaList}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>
		</form>

		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a id="delete_list_data" href="#" class="btn btn-dark-gray btn-mini"><span>삭제</span></a>
			</div>
		</div>
		<div class="pagination-wrap">
			<p class="pagination op-pagination">
				<page:pagination-manager />
			</p>
		</div>

<div style="display: none;">
	<span id="today"><c:out value="${today}"/></span>
	<span id="week"><c:out value="${week}"/></span>
	<span id="month1"><c:out value="${month1}"/></span>
	<span id="month3"><c:out value="${month3}"/></span>
</div>

<iframe id="downloadFrame" name="downloadFrame" style="display: none;"></iframe>

<script type="text/javascript">
var $qnaTypeOptions;
$(function() {
	$qnaTypeOptions = $('<select />').append($('#qnaType option').clone());
	//목록데이터 - 삭제처리
	$('#delete_list_data').on('click', function() {
		Common.updateListData("/opmanager/qna/delete", "정말로 삭제하시겠습니까?");// 선택된 데이터를 삭제하시겠습니까?
	});

	$('#itemsPerPage').on("change", function() {
		$('#qna').submit();
	});

	$(".btn_date").on('click',function() {

		var $id = $(this).attr('class').replace('btn_date ','');		// id[0] : type, id[1] : value

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

			$("input[type=text]", $(this).parent().parent()).eq(0).val(date1);
			$("input[type=text]", $(this).parent().parent()).eq(1).val(date2);

		}
	});

	$('#qnaGroup').on('change', function() {
		var type = $(this).val();
		$('#qnaType').find('option').not(':first-child').remove();
		$option = $qnaTypeOptions.find('.qna_type_' + type).show().clone();
		$('#qnaType').append($option).val("0");
	});


});

//엑셀 다운로드 팝업.
function downloadExcel() {
	if (confirm("엑셀파일로 다운로드 받으시겠습니까?")) {
		$('#listForm').submit();

		// 다운로드 체크.
		$.cookie('DOWNLOAD_STATUS', 'in progress', {path:'/'});
		checkDownloadStatus();
	} else {
		return false;
	}
}

//다운로드 체크
function checkDownloadStatus() {
     if ($.cookie('DOWNLOAD_STATUS') == 'complete') {
     	Common.loading.hide();
		return;
     } else {
		setTimeout("checkDownloadStatus()", 1000);
     }
}

function qnaDetail(qnaId) {
	$("#qna").attr("method", "get").attr("action", "/opmanager/qna/answer/"+qnaId).submit();
}
</script>

<script type="text/javascript">
$(function() {
	Common.DateButtonEvent.set('.day_btns > a[class^=table_btn]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
	EventHandler.calendarStartDateAndEndDateVaild();
	searchEnterKey();
});

$(function() {
	var openerReload = '${fn:escapeXml(openerReload)}';
	if (openerReload == '-after-login') {
		opener.location.reload();
	}
	$('#itemReview').validator();
});

function search() {
	var strStartDate = $("#searchStartDate").val();
	var strEndDate = $("#searchEndDate").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		if(strStartDate > strEndDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			$("#endCreateDate").focus();
			return false;
		}
	}

	$("#qna").submit();
}

/**
 *	함 수 명 : searchEnterKey
 *	기	능  : 검색 엔터키 이벤트
 */
 function searchEnterKey() {
		$("#query").on('keydown', function(e) {
			if (e.keyCode == '13') {
				$("#qna").submit();
			}
		});
	}


</script>

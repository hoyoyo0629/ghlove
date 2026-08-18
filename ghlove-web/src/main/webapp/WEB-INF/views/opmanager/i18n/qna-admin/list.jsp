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
            <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
        </div>
        <h3><span>지자체 문의 게시판</span></h3>
		<form:form modelAttribute="qnaAdminParam" method="post">
	        <div class="board_write">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
                        <col>
	                </colgroup>
	                <tbody>
		            	<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
		                	<tr>
	                            <td class="label">지자체</td>
	                            <td>
	                                <div class="flex_box gap-08">
			                            <form:select title="${op:message('지자체')}" class="wd-150" onchange="javascript:searchSigungu(this);" path="upperLocgovCode"><!-- 지자체 -->
			                                <option value="">-시,도-</option><!-- -시,도- -->
			                                <c:forEach items="${sido}" var="data" varStatus="i" >
			                                	<option value="${fn:escapeXml(data.value)}">${fn:escapeXml(data.label)}</option>
			                                </c:forEach>
			                            </form:select>
			                            <form:select title="${op:message('지자체')}" class="wd-150" path="locgovCode"><!-- 지자체 -->
			                                <option value="">-시,군,구-</option>
			                            </form:select>
			                        </div>
	                            </td>
	                        </tr>
                        </c:if>
	                    <tr>
	                        <td class="label">${op:message('M00011')}</td> <!-- 검색구분 -->
	                        <td>
	                            <div class="flex_box gap-08">
	                                <form:select path="where" title="${op:message('M00011')}" class="wd-150"> <!-- 검색구분 -->
										<form:option value="" label="${op:message('M00039')}" /><!-- 전체 -->
										<form:option value="QNA_GROUP" label="${op:message('M00460')}" /><%-- 문의유형 --%>
										<%-- <form:option value="LOGIN_ID" label="${op:message('M00081')}" /> <!-- 아이디 -->
										<form:option value="USER_NAME" label="${op:message('M00005')}" /> <!-- 이름 --> --%>
										<!-- 2014.12.28 -->
										<form:option value="SUBJECT" label="${op:message('M00275')}" /> <!-- 제목 -->
										<form:option value="QUESTION" label="${op:message('M00006')}" /> <!-- 내용 -->
									</form:select>
									<form:input type="text" path="query" class="input_txt required _filter full" title="${op:message('M00011')}" />  <!-- 검색구분 -->
									<form:select path="query" title="${op:message('M00460')}" class="wd-150"><%-- 문의유형 --%>
										<c:forEach items="${qnaGroups}" var="group">
		 									<form:option value="${fn:escapeXml(group.id)}" label="${fn:escapeXml(group.label)}" />
		 								</c:forEach>
									</form:select>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('M00202')}</td> <%-- 등록일 --%>
	                        <td>
	                            <div>
	                                <span class="datepicker">
	                                    <form:input path="searchStartDate" title="${op:message('M00507')}"
	                                        class="datepicker _number" type="text" value="" maxlength="8"
	                                        autocomplete="off" /><!-- 시작일 -->
	                                    <!-- <button type="button" class="ui-datepicker-trigger">
	                                        <span class="icon_calendar">날짜 선택</span>
	                                    </button> -->
	                                </span>
	                                <span class="wave">~</span>
	                                <span class="datepicker">
	                                    <form:input path="searchEndDate" title="${op:message('M00509')}"
	                                        class="datepicker _number" type="text" value="" maxlength="8"
	                                        autocomplete="off" /><!-- 종료일 -->
	                                    <!-- <button type="button" class="ui-datepicker-trigger">
	                                        <span class="icon_calendar">날짜 선택</span>
	                                    </button> -->
	                                </span>
	                                <span class="day_btns">
	                                    <a href="javascript:;" class="btn_date today">${op:message('M00026')}</a>   <!-- 오늘 -->
	                                    <a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a> <!-- 1주일 -->
	                                    <a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a>  <!-- 한달 -->
	                                    <a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a>  <!-- 3개월 -->
	                                    <a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a> <!-- 1년 -->
                                        <c:choose>
                                            <c:when test="${op:hasRole('ROLE_ADMIN_CALL')}">
                                                <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
                                            </c:when>
                                        </c:choose>
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
	                    <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/qna-admin/list'">${op:message('M00047')}</button><!-- 초기화 -->
	                    <%-- <button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkKeyword(event);">${op:message('M00048')}</button><!-- 검색 --> --%>
	                    <button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
	                </div>
	            </div>
	        </div>

	        <div class="count_title mt-40">
	            <h5>총 ${fn:escapeXml(qnaCount)}건</h5><!-- 총 545,423건 -->
	            <span>
	                <form:select path="itemsPerPage" title="${op:message('M00239')}"> <!-- 화면출력 -->
						<%-- <form:option value="1" label="${op:message('1개 출력')}" />  <!-- 1개 출력 테스트용--> --%>
						<form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
						<form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
						<form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
						<form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
	                </form:select>
	            </span>
	        </div>
		</form:form>

		<form action="/opmanager/qna-admin/list" method="post" id="listForm">
	        <div class="admin_wrap board_list">
	            <table class="board_list_table" summary="">
	                <colgroup>
	                    <col style="width:50px;">
                        <col style="width:50px;">
                        <c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
                        	<col style="width:200px;">
                        </c:if>
                        <col style="width:150px;">
                        <col style="width:500px;">
                        <col style="width:200px;">
                        <col style="width:100px;">
                        <col style="width:200px;">
	                </colgroup>
	                <thead>
	                    <tr>
	                        <th scope="col"><input type="checkbox" id="check_all" title="${op:message('M00169')}"></th>
	                        <th scope="col">${op:message('No.')}</th><!-- No. -->
	                        <c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
	                        	<th scope="col">지자체</th>
	                        </c:if>
	                        <th scope="col">${op:message('M00460')}</th><!-- 문의유형 -->
	                        <th scope="col">${op:message('M00275')}</th><!-- 제목 -->
	                        <th scope="col">작성자(아이디)</th>
	                        <th scope="col">${op:message('M01476')}</th><!-- 상태 -->
	                        <th scope="col">${op:message('M00202')}</th><!-- 등록일 -->
	                    </tr>
	                </thead>
	                <tbody>
	                	<c:forEach items="${qnaAdminList}" var="list" varStatus="i">
							<tr style="background:#fff;">
								<td>
									<div>
										<input type="checkbox" name="id" value="${fn:escapeXml(list.qnaAdminId)}" title=${op:message('M00169')} />
									</div>
								</td>
								<td>${pagination.itemNumber - i.count}</td>
								<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
									<td>
										<div>
											${fn:escapeXml(list.locgovName)}
										</div>
									</td>
								</c:if>
								<td>
									<div>
										${fn:escapeXml(list.qnaGroupName)}
									</div>
								</td>
								<td class="tleft">
									<div>
										<a href="/opmanager/qna-admin/edit/${fn:escapeXml(list.qnaAdminId)}">${fn:escapeXml(list.subject)}</a> <!-- 제목 -->
									</div>
								</td>
								<td>
									<div>
										${fn:escapeXml(list.userName)}<br>
										(${fn:escapeXml(list.loginId)})
									</div>
								</td>
								<td>
									<div>
										${list.qnaAdminAnswer.qnaAdminAnswerId > 0 ? op:message('M00463') : op:message('M00464')}<!-- 답변완료 --> <!-- 답변대기 -->
									</div>
								</td>
								<td>
									<div>
										${fn:escapeXml(list.createdDateStr)}
									</div>
								</td>
							</tr>
						</c:forEach>
	                </tbody>
	            </table>

				<c:if test="${empty qnaAdminList}">
					<div class="no_content">
						${op:message('M00473')} <!-- 데이터가 없습니다. -->
					</div>
				</c:if>

                <div class="btn_all btn_right">
                    <div class="flex_box gap-08">
                        <button type="button" id="delete_list_data" class="btn btn-dark-gray btn-mini">${op:message('M00074')}</button> <!-- 삭제 -->
                    </div>
                </div>

	            <div class="pagination-wrap">
                    <page:pagination-manager />
	            </div>
	        </div>
		</form>


<%-- <div style="display: none;">
	<span id="today">${today}</span>
	<span id="week">${week}</span>
	<span id="month1">${month1}</span>
	<span id="month3">${month3}</span>
</div> --%>

<!-- <iframe id="downloadFrame" name="downloadFrame" style="display: none;"></iframe> -->

<page:javascript>

<script type="text/javascript">
var $qnaTypeOptions;
$(function() {
	$qnaTypeOptions = $('<select />').append($('#qnaType option').clone());
	//목록데이터 - 삭제처리
	$('#delete_list_data').on('click', function() {
		Common.updateListData("/opmanager/qna-admin/qnaAdmin/delete", "삭제하시겠습니까?"
				, function () {
					alert('삭제되었습니다.');
					location.reload();
				}
				, function (response) {
					alert(response.errorMessage);
					location.reload();
		});// 삭제하시겠습니까?
	});

	/*$('#itemsPerPage').on("change", function() {
		$('#qna').submit();
	});*/

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

	/* $('#qnaGroup').on('change', function() {
		var type = $(this).val();
		$('#qnaType').find('option').not(':first-child').remove();
		$option = $qnaTypeOptions.find('.qna_type_' + type).show().clone();
		$('#qnaType').append($option).val("0");
	}); */

	controlInputElement("${fn:escapeXml(param.where)}", false);

	$('#where').on('change', function() {
		let value = $(this).val();
		controlInputElement(value, true);
	});

	// 화면 로딩 후 시도, 시군구 정보 세팅
	if ("${fn:escapeXml(param.upperLocgovCode)}" != "") {		// 세팅된 시도 값 있을 경우
    	$("#upperLocgovCode").val("${fn:escapeXml(param.upperLocgovCode)}").prop("selected", true);		// 시도 값 세팅
        searchSigungu(document.getElementById("upperLocgovCode"));		// 시군구 목록 조회
    }

});

//엑셀 다운로드 팝업.
/* function downloadExcel() {
	if (confirm("엑셀파일로 다운로드 받으시겠습니까?")) {
		$('#listForm').submit();

		// 다운로드 체크.
		$.cookie('DOWNLOAD_STATUS', 'in progress', {path:'/'});
		checkDownloadStatus();
	} else {
		return false;
	}
} */

//다운로드 체크
/* function checkDownloadStatus() {
     if ($.cookie('DOWNLOAD_STATUS') == 'complete') {
     	Common.loading.hide();
		return;
     } else {
		setTimeout("checkDownloadStatus()", 1000);
     }
} */

// 검색구분 UI 및 검색 키값 제어
function controlInputElement(value, isChangeEvent) {
	let inputQueryElement = $('#where').parent().children("input")[0];		// 검색어 입력 인풋박스
	let selectQueryElement = $('#where').parent().children("select")[1];	// 문의유형 항목 셀렉트박스
	if (value === "QNA_GROUP") {		// 문의 유형 선택했을 경우
		inputQueryElement.setAttribute("id", "temp");
		inputQueryElement.setAttribute("name", "temp");
		if (isChangeEvent) {
			//inputQueryElement.value = "";
		}

		selectQueryElement.setAttribute("id", "query");
		selectQueryElement.setAttribute("name", "query");

		$("#query").val("1").prop("selected", true);
	} else {
		if (value) {
			if (isChangeEvent) {
				//inputQueryElement.value = "";
				//$("#temp").val("1").prop("selected", true);
				$("#temp").val("");
			}
			inputQueryElement.setAttribute("id", "query");
			inputQueryElement.setAttribute("name", "query");

			selectQueryElement.setAttribute("id", "temp");
			selectQueryElement.setAttribute("name", "temp");

		} else {
			inputQueryElement.setAttribute("id", "temp1");
			inputQueryElement.setAttribute("name", "temp1");

			selectQueryElement.setAttribute("id", "temp2");
			selectQueryElement.setAttribute("name", "temp2");

			inputQueryElement.value = "";

			$("#temp1").hide();
			$("#temp2").hide();
			return;
		}
	}
	$("#query").show();
	$("#temp").hide();
}

function checkKeyword(event) {
	/* let keyword = $("#query").val();
	if (!keyword) {
		alert("검색어를 입력해주세요.");
		event.preventDefault();
		return;
	} */
}

//시군구 목록 조회
function searchSigungu(sidoElement) {
	let sidoValue = sidoElement.value;
	$("#locgovCode option").remove();
    $('#locgovCode').append('<option value="">-시,군,구-</option>');
    if (sidoValue) {
        $.post(
        		url("/common/getLocgovCode")
        		, {'code' : sidoValue}
        		, function(response) {
        			Common.responseHandler(response, function(){
        				let data = response.data;
			            for (var i = 0; i < data.length; i++) {
			                var options = '<option value="' + data[i].LOCGOV_CODE + '">' + data[i].LOCGOV_NM + '</option>';
			                $('#locgovCode').append(options);
			            }

			            // 조회 된 값 유지
			            if ("${fn:escapeXml(param.locgovCode)}" != "") {
			            	$("#locgovCode").val("${fn:escapeXml(param.locgovCode)}").prop("selected", true);

			            	if (!$("#locgovCode").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
			            		$("#locgovCode option:eq(0)").prop("selected", true);
			            	}
			            }
        			});
	            }
        )
        .always(function () {
        	Common.loading.hide();
        });
    } else {
    	Common.loading.hide();
    }
}

/**
 *	함 수 명 : search
 *	기	능  : 검색
 */
function search() {
	var strStartDate = $("#searchStartDate").val();
	var strEndDate = $("#searchEndDate").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
		var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			$("#searchEndDate").focus();
			return false;
		}
		var searchChk = Common.searchDateMonth(startDate, endDate);
		if(!searchChk) return false;
	}

	$("#qnaAdminParam").submit();
}

</script>

<script type="text/javascript">
$(function() {
	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');

	EventHandler.calendarStartDateAndEndDateVaild();
});

$(function() {
	var openerReload = '${fn:escapeXml(openerReload)}';
	if (openerReload == '-after-login') {
		opener.location.reload();
	}
	$('#itemReview').validator();
});
</script>
</page:javascript>
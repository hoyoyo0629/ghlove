<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

	<div class="item_list">
		<h3><span>기부혜택증 열람현황목록</span></h3>

		<form:form modelAttribute="lclgvHnrUserMngParam" method="post">

				<%-- 검색조건 --%>
				<div class="board_write">
				<table class="board_write_table" summary="">
					<caption></caption>
					<colgroup>
						<col style="width:100px;" />
						<col style="width:auto;" />
					</colgroup>
					<tbody>
						<tr>
							<td class="label"><c:out value="${op:message('M01347')}"/></td> <!-- 기간 -->
							<td>
						 		<div>
									<span class="datepicker"><form:input path="startDate" class="term datepicker" title="${op:message('M00507')}" id="dp28" /></span> <!-- 시작일 -->
									<span class="wave">~</span>
									<span class="datepicker"><form:input path="endDate" class="term datepicker" title="${op:message('M00509')}" id="dp29" /></span> <!-- 종료일 -->
									<span class="day_btns">
										<a href="javascript:;" class="btn_date today"><c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
										<a href="javascript:;" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
										<a href="javascript:;" class="btn_date day-15"><c:out value="${op:message('M00028')}"/></a><!-- 15일 -->
										<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
										<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
										<a href="javascript:;" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
									</span>
								</div>
					 		</td>
						</tr>
						<c:if test="${!((op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')))}">
						<tr>
							<td class="label">지자체</td>
							<td>
								<div class="flex_box gap-08">
									<form:select path="upperLocgovCode" class="wd-150" onChange="wdrChange(this.value)">
										<form:option value="">-${op:message('M00039')}-</form:option>
									    <c:forEach items="${wdr}" var="wdr">
											<form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
										</c:forEach>
									</form:select>
									<form:select path="lclgvCd" class="wd-150">
										<option value="">-시·군·구-</option>
									</form:select>
								</div>
							</td>
						</tr>
						</c:if>
						<tr>
							<td class="label">사용자명</td>
							<td>
								<div class="flex_box gap-08">
									<form:input path="userName" title="사용자명" class="input_txt required _filter half" type="text"/>
								</div>
							</td>
						</tr>
					</tbody>
					</table>

				<%-- <c:if test="${empty lclgvHnrUserYear}">
					<div class="no_content">
						<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
					</div>
				</c:if> --%>
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
		        <li>
		            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
		        </li>
	        </ul>
	  	</div>
				<%-- 검색버튼 --%>
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/lclgvHnrUser/lclgvHnrUserViewHist/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:search();"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
					</div>
				</div>
			</div>

			<div class="count_title mt-40">
			<%-- 총 건수 --%>
			<h5>
				<c:out value="${op:message('M00045')}"/> <%-- 총 --%>
				<c:out value="${op:numberFormat(lclgvHnrUserMngParam.pagination.totalItems)}"/>
				<c:out value="${op:message('M00272')}"/> <%-- 건 --%>
			</h5>

			<%-- 출력개수 --%>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
 						onchange="$('form#lclgvHnrUserMngParam').submit();"> <!-- 화면 출력수 -->
 																		<!-- 개 출력 -->
 						<form:option value="10" label="10${op:message('M00053')}" />
 						<form:option value="50" label="50${op:message('M00053')}" />
 						<form:option value="100" label="100${op:message('M00053')}" />
 						<form:option value="200" label="200${op:message('M00053')}" />
 						<form:option value="500" label="500${op:message('M00053')}" />
					</form:select>
				</span>
			</div>
			</form:form>

			<%-- 출력리스트 --%>
			<div class="board_list">

				<form id="listForm" action="">
<%-- 	 				<input type="hidden" id="rowNumber" value="${lclgvHnrUserMngList.rowNumber}"> --%>
					<table class="board_list_table" summary="지자체 기부혜택증 기준 목록">
					<caption>지자체 기부혜택증 기준 목록</caption>
					<colgroup>
						<col style="width:10%;">
						<col style="width:10%">
		                <col style="width:20%;">
		                <col style="width:20%;">
		                <col style="width:10%;">
		                <col style="width:*">
					</colgroup>
					<thead>
						<tr>
							<th>No.</th>
							<th>열람일자</th>
							<th>시도</th>
							<th>지자체</th>
							<th>사용자명</th>
							<th>열람횟수</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
					<c:when test="${not empty lclgvHnrUserMngList}">
						<c:forEach items="${lclgvHnrUserMngList}" var="lclgvHnrUser" varStatus="index">
						<tr>
							<td>${fn:escapeXml(lclgvHnrUser.rowNumber)}</td>
							<td><c:out value="${lclgvHnrUser.viewYm}"/></td>
							<td><c:out value="${lclgvHnrUser.upperLocgovNm}"/></td>
							<td>
							    <c:out value="${lclgvHnrUser.lclgvCdNm}"/>
							</td>
							<td>${op:numberFormat(lclgvHnrUser.userName)}</td>
							<td>${op:numberFormat(lclgvHnrUser.viewCnt)}</td>
						</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="6">조회 정보가 존재하지 않습니다.</td>
						</tr>
					</c:otherwise>
			</c:choose>
					</tbody>
					</table>
				</form>

				<%-- 등록 버튼 --%>
<!--                 	<div class="btn_all btn_right"> -->
<!--                 		<div class="flex_box gap-08"> -->
<!-- 	                        <button type="button" onclick="fnInsertCatalogMng()" class="btn btn-default btn-mini">등록</button> -->
<!--                         </div> -->
<!--                 	</div> -->
			</div>

			<%-- 페이징 --%>
			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>

	</div>
</div>

<script type="text/javascript">

	var changeYn = "N";

	$("select[name='upperLocgovCode']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	})

$(function() {

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');

	let upperLocgovCode = '<c:out value="${lclgvHnrUserMngParam.upperLocgovCode}"/>';

	if (upperLocgovCode) {
		wdrChange(upperLocgovCode);
	}

	paging();
});

// 지자체 변경
function wdrChange(value) {
	Common.loading.display = false;

	$("#lclgvCd option").remove();
	if ($("#upperLocgovCode").val() != "") {
		$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
			$('#lclgvCd').append('<option value="">-전체-</option>');
			for (var i = 0; i < response.length; i++) {
	            let options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
	            $('#lclgvCd').append(options);
	        }

			// 조회 된 값 유지
			if ("${fn:escapeXml(lclgvHnrUserMngParam.upperLocgovCode)}" != "" && changeYn == 'N') {
				$("#lclgvCd").val('${fn:escapeXml(lclgvHnrUserMngParam.upperLocgovCode)}').prop("selected", true);
			}
			let lclgvCd = '<c:out value="${lclgvHnrUserMngParam.lclgvCd}"/>';
			let optionLists = $("#lclgvCd")[0];
			for(let option of optionLists) {
				if (option.value == lclgvCd) {
					option.setAttribute("selected", "selected");
					break;
				}
			}
	    });
	} else {
        $('#lclgvCd').append('<option value="">-시·군·구-</option>');
	}
}

//기부혜택증 열람현황 조회
function search() {
	$("#lclgvHnrUserMngParam").submit();
}

function excelDownload() {
	Shop.downloadExcelOrder("/opmanager/lclgvHnrUser/lclgvHnrUserViewHist/list/excel", $('#lclgvHnrUserMngParam').serialize(), false);
}

//페이징 처리
function paging(){
	var $pagination = $('.op-pagination');
    if ($pagination.size() == 0) {
        return;
    }

    var $form = $('#lclgvHnrUserMngParam');

    // 페이지 링크 이벤트 처리.
    $pagination.find('a').on('click', function(e) {
        var href = $(this).attr('href');

        var pattern = /page=[0-9]+/;
        var m = href.match(pattern);
        if (m != null) {
            e.preventDefault();

            var page = m[0].replace('page=', '');

            if ($form.find('input[name=page]').size() == 0) {
                $form.append('<input type="hidden" name="page" value="' + page + '" />')
            } else {
                $form.find('input[name=page]').val(page);
            }

            $form.submit();

        }
    });
}

</script>

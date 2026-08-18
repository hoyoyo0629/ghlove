<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>기부금 운용정보</span></h3>

<form:form modelAttribute="searchParam" method="post">
	<div class="board_write">
		<table class="board_write_table" summary="기부금 운용정보">
			<colgroup>
				<col style="width:220px;">
				<col>
				<col style="width:220px;">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<td class="label">년도</td>
					<td>
						<div>
							<form:select path="shCntrYear" title="년도" class="wd-150">
								<c:forEach items="${yyyy}" var="yyyy">
									<form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
								</c:forEach>
							</form:select>
						</div>
					</td>
					<td class="label">지자체</td>
					<td>
						<div class="flex_box gap-08">
							<c:choose>
								<c:when test="${locgovFullNm ne null}">
									<span><c:out value="${locgovFullNm}"/></span>
								</c:when>
								<c:otherwise>
									<form:select path="shWdr" title="지자체" class="wd-150" onChange="wdrChange(this.value)">
										<form:option value="">-시,도 선택-</form:option>
										<c:forEach items="${wdr}" var="wdr">
											<form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
										</c:forEach>
									</form:select>
									<form:select path="shLocgovCode" title="지자체" class="wd-150">
										<option value="">-시,군,구-</option>
									</form:select>
								</c:otherwise>
							</c:choose>
						</div>
			            <form:hidden path="query"/>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/give/give-operation/list';">초기화</button>
				<button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
			</div>
		</div>
	</div>


	<h3 class="mt50 fs24"><span>합계</span></h3>

	<div class="board_write">
		<table class="board_write_table" summary="기부모금액">
			<colgroup>
				<col style="width:220px;">
				<col>
				<col style="width:220px;">
				<col>
				<col style="width:220px;">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<td class="label">기부금액</td>
					<td>
						<div><c:out value='${op:numberFormat(total.cntrAmt)}'/></div>
					</td>
					<td class="label">사용금액</td>
					<td>
						<div><c:out value='${op:numberFormat(total.expndtrAmt)}'/></div>
					</td>
					<td class="label">잔액</td>
					<td>
						<div><c:out value='${op:numberFormat(total.balanceAmt)}'/></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="count_title mt-40">
		<h5>총 <c:out value='${op:numberFormat(count)}'/>건</h5>
		<span>
			<form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();" > <!-- 화면출력 -->
				<form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
				<form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
				<form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
				<form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
			</form:select>
		</span>
	</div>
</form:form>

<div class="board_list">
	<table class="board_list_table" summary="기부금 운용정보">
		<caption>기부금 운용정보</caption>
		<colgroup>
			<col style="width:50px;">
			<col style="width:100px;">
			<col style="width:300px;">
			<col style="width:200px;">
			<col style="width:200px;">
			<col style="width:200px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">년도</th>
				<th scope="col">지자체</th>
				<th scope="col">기부금액</th>
				<th scope="col">사용금액</th>
				<th scope="col">잔액</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="i">
				<tr style="background:#fff;">
					<td>
						<div><c:out value='${pagination.itemNumber - i.count}'/></div>
					</td>
					<td>
						<div><c:out value='${item.cntrYear}'/></div>
					</td>
					<td>
						<div>
							<a href="javascript:operationList('${fn:escapeXml(item.locgovCode)}');"><c:out value='${item.upperLocgovNm}'/> <c:out value='${item.locgovNm}'/></a>
						</div>
					</td>
					<td>
						<div><c:out value='${op:numberFormat(item.cntrAmt)}'/></div>
					</td>
					<td>
						<div><c:out value='${op:numberFormat(item.expndtrAmt)}'/></div>
					</td>
					<td>
						<div><c:out value='${op:numberFormat(item.balanceAmt)}'/></div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="btn_all btn_left">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
		</div>
	</div>
	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</div>

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">
$(function() {
	wdrChange($("#shWdr").val());
});

	  // 지자체 변경
function wdrChange(value) {
	Common.loading.display = false;

	$("#shLocgovCode option").remove();
	if ($("#shWdr").val() != "") {
		$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {

			for (var i = 0; i < response.length; i++) {
	            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
	            $('#shLocgovCode').append(options);
	        }

			// 조회 된 값 유지
			if ("${fn:escapeXml(searchParam.shLocgovCode)}" != "") {
			    $("#shLocgovCode").val('${fn:escapeXml(searchParam.shLocgovCode)}').prop("selected", true);
			}

	    });
	} else {
        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
	}
}

function operationList(locgovCode) {
	// location.href = "/opmanager/give/give-operation/list/" + locgovCode;

	let form = document.createElement('form');
	form.action = "/opmanager/give/give-operation/list/" + locgovCode;
	form.method = 'post';

	form.innerHTML += '<input name="shCntrYear" type="hidden" value="' + $("#shCntrYear").val() + '">';
	document.body.append(form);
	form.submit();
}

function downloadExcel() {
    Shop.downloadExcelOrder("/opmanager/give/give-operation/list/excel", $('#searchParam').serialize(), true);
}
</script>

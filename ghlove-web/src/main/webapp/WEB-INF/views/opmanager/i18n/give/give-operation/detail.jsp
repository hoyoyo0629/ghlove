<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

<style>
	.line_limit {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		word-break: break-all;
	}
</style>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>기부금 운용정보</span></h3>

<form:form modelAttribute="searchParam" method="post" id="searchParam">
<div class="board_write">
	<table class="board_write_table" summary="기부금 운용정보">
		<colgroup>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">지자체명</td>
				<td>
					<div>
						<c:out value="${upperLocgov.label}" /> <c:out value="${locgov.label}" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="label">구분</td>
				<td>
					<div class="flex_box gap-08">
						<form:select path="searchType" title="구분" class="wd-150">
							<form:option value="ALL" label="전체" />
							<form:option value="NAME" label="사업명" />
							<form:option value="PURPOSE" label="사업목적" />
							<form:option value="CONTENT" label="내용" />
						</form:select>
						<form:input path="searchTxt" title="구분" class="input_txt required _filter full" type="text" value="" />
					</div>
				</td>
			</tr>
			<tr>
			   <td class="label">수정일</td>
			   <td>
				   <div>
					   <span class="datepicker">
							<form:input path="shCntrDeStart" cssClass="datepicker optional" title="${op:message('M00507')}" />
					   </span>
					   <span class="wave">~</span>
					   <span class="datepicker">
							<form:input path="shCntrDeEnd" cssClass="datepicker optional" title="${op:message('M00509')}" />
					   </span>
					   <span class="day_btns">
							<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
							<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
							<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
							<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
							<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
						</span>
				   </div>
			   </td>
			</tr>
		</tbody>
	</table>

	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<%-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/give/give-operation/list/${fn:escapeXml(searchParam.shLocgovCode)}/detail';">초기화</button> --%>
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:initParam();">초기화</button>
			<!-- <button type="submit" class="btn btn-dark-gray btn-mini">검색</button> -->
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
		</div>
	</div>
</div>

<h3 class="mt50 fs24"><span>올해 누적합계</span></h3>

<div class="board_write">
	<table class="board_write_table" summary="기부금 운용정보">
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

<h3 class="mt50 fs24"><span>검색 합계</span></h3>

<div class="board_write">
	<table class="board_write_table" summary="기부금 운용정보">
		<colgroup>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">사용금액</td>
				<td>
					<div><c:out value='${op:numberFormat(searchTotal)}'/></div>
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
			<col style="width:150px;">
			<col style="width:200px;">
			<col style="width:200px;">
			<col style="width:200px;">
			<col style="width:500px;">
			<col style="width:150px;">
			<col style="width:200px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No.</th>
				<th scope="col">지출일</th>
				<th scope="col">사업명</th>
				<th scope="col">사업목적</th>
				<th scope="col">사용금액</th>
				<th scope="col">내용</th>
				<th scope="col">첨부</th>
				<th scope="col">작성자</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${list.size() > 0}">
					<c:forEach items="${list}" var="item" varStatus="i">
						<tr style="background:#fff;">
							<td>
								<div><c:out value='${pagination.itemNumber - i.count}'/></div>
							</td>
							<td>
								<div><c:out value='${item.expndtrDe}'/></div>
							</td>
							<td>
								<div><c:out value='${item.bsnsNm}'/></div>
							</td>
							<td>
								<div class="line_limit" style="max-width : 200px"><c:out value='${item.bsnsPurpsNm}'/></div>
							</td>
							<td>
								<div><c:out value='${op:numberFormat(item.expndtrAmt)}'/></div>
							</td>
							<td>
								<div class="line_limit" style="max-width : 500px">
									<a href="javascript:openModifyPopup('${fn:escapeXml(searchParam.shLocgovCode)}', '${fn:escapeXml(item.registSn)}')"><c:out value='${item.bsnsCn}'/></a>
								</div>
							</td>
							<td>
								<div>
									<c:choose>
										<c:when test="${item.fileCnt > 0}">
											<a href="javascript:downloadFiles('${fn:escapeXml(item.registSn)}')">다운로드</a>
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
								</div>
							</td>
							<td>
								<div><c:out value='${item.loginId}'/>(<c:out value='${item.userName}'/>)</div>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="8">등록된 사용 내역이 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>

	<div class="flex_box juc-sbt mt15">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
		</div>
		<div class="flex_box gap-08">
			<c:if test="${adminRole eq 'LOC'}">
				<button type="button" class="btn btn-default btn-mini" onclick="openPopup('${fn:escapeXml(searchParam.shLocgovCode)}')">등록</button>
			</c:if>
			<button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/give/give-operation/list/${fn:escapeXml(searchParam.shLocgovCode)}'">목록</button>
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
$(function () {
	$(".contents_inner").find("div.location a").removeClass("on");
	$(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="shCntrDeStart"]' , 'input[name="shCntrDeEnd"]');
	EventHandler.calendarStartDateAndEndDateVaild();
});

function openPopup(locgovCode) {
	Common.popup(url("/opmanager/give/give-operation/list/" + locgovCode + "/popup"), 'give-operation', 800, 700);
}

function openModifyPopup(locgovCode, registSn) {
	Common.popup(url("/opmanager/give/give-operation/list/" + locgovCode + "/popup/" + registSn), 'give-operation', 800, 700);
}

function downloadFiles(registSn) {
	location.href = "/opmanager/give/give-operation/file/download/all/" + registSn;
}


function downloadExcel() {
    Shop.downloadExcelOrder("/opmanager/give/give-operation/list/${fn:escapeXml(give-operationsearchParam.shLocgovCode)}/excel", $('#searchParam').serialize(), true);
}

/**
 *	함 수 명 : search
 *	기	능  : 검색
 */
function search() {
	var strStartDate = $("#shCntrDeStart").val();
	var strEndDate = $("#shCntrDeEnd").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
		var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			var value = $("#shCntrDeStart").val();
			$("#shCntrDeEnd").val("");
			$("#shCntrDeEnd").focus();
			$("#shCntrDeEnd").val(value);
			return false;
		}
		var searchChk = Common.searchDateMonth(startDate, endDate);
		if(!searchChk) return false;
	}

	$("#searchParam").submit();
}

function initParam() {
	$("#searchType").val("ALL");
	$("#searchTxt").val("");
	$("#shCntrDeStart").val("");
	$("#shCntrDeEnd").val("");
	$("#searchParam").submit();
}


</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.onlinepowers.framework.util.DateUtils" %>
<%@ page import="saleson.common.Const" %>

<div class="location">
	<a href="#"></a>&gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>지방세외 수납연계 로그</span></h3>

<form:form modelAttribute="nextSunapRequestLogDto" cssClass="opmanager-search-form clear" method="post" id="nextSunapRequestLogDto">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>
	<form:hidden path="itemsPerPageTemp"/>
	<form:hidden path="query"/>

	<div class="board_write">
		<table class="board_write_table" summary="지방세외 부과 로그">
			<colgroup>
				<col style="width:15%;">
				<col style="width:35%;">
				<col style="width:15%;">
				<col style="width:35%;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">전자납부번호</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:input path="epayNo" title="전자납부번호" class="input_txt required _filter wd-500" type="text" value="${fn:escapeXml(nextSunapRequestLogDto.epayNo)}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">수납일자</td>
					<td colspan="3">
						<div>
							<span class="datepicker">
								<form:input path="srchStartLogDate" cssClass="datepicker optional " title="${op:message('M00507')}" />
							</span>
							<span class="wave">~</span>
							<span class="datepicker">
								<form:input path="srchEndLogDate" cssClass="datepicker optional " title="${op:message('M00509')}" />
							</span>
							<span class="day_btns day_btn1">
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/log/gif-stnd-sunap'">초기화</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>
</form:form>
<!-- List -->
<div class="board_list mt-40">
	<!-- List Header -->
	<div class="count_title mt-40">
		<h5>총 <fmt:formatNumber value="${fn:escapeXml(count)}" pattern="#,###"/> 건</h5>
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
	<div style="overflow:auto;">
		<table class="board_list_table" summary="지방세외 부과 로그">
			<caption>지방세외 부과 로그</caption>
			<colgroup>
				<col style="min-width:150px;"></col> <!-- 연계관리키 -->
				<col style="min-width:120px;"></col> <!-- 자치단체코드 -->
				<col style="min-width:150px;"></col> <!-- 자치단체명 -->
				<col style="min-width:240px;"></col> <!-- 과세번호 -->
				<col style="min-width:150px;"></col> <!-- 통합과세번호 -->
				<col style="min-width:120px;"></col> <!-- 부서코드 -->
				<col style="min-width:170px"></col> <!-- 부서명 -->
				<col style="min-width:120px;"></col> <!-- 특별회계사업코드 -->
				<col style="min-width:150px;"></col> <!-- 특별회계사업명 -->
				<col style="min-width:100px;"></col> <!-- 회계연도 -->
				<col style="min-width:100px;"></col> <!-- 회계구분코드 -->
				<col style="min-width:180px;"></col> <!-- 회계구분명 -->
				<col style="min-width:100px;"></col> <!--  대표세입과목코드 -->
				<col style="min-width:180px;"></col> <!-- 세입과목명 -->
				<col style="min-width:100px;"></col> <!-- 운영항목코드 -->
				<col style="min-width:120px;"></col> <!-- 운영항목명 -->
				<col style="min-width:100px;"></col> <!-- 부과번호 -->
				<col style="min-width:100px;"></col> <!-- 분납번호 -->
				<col style="min-width:150px;"></col> <!-- 전자납부번호 -->
				<col style="min-width:80px;"></col> <!-- 수납번호 -->
				<col style="min-width:80px;"></col> <!-- 수납구분코드 -->
				<col style="min-width:80px;"></col> <!-- 수납구분명 -->
				<col style="min-width:100px;"></col> <!-- 수납일자 -->
				<col style="min-width:100px"></col> <!-- 회계일자 -->
				<col style="min-width:100px;"></col> <!-- 이체일자 -->
				<col style="min-width:100px;"></col> <!-- 수납본세금액 -->
				<col style="min-width:100px;"></col> <!-- 수납가산금액 -->
				<col style="min-width:100px;"></col> <!-- 수납이자금액 -->
				<col style="min-width:100px"></col> <!-- 은행명 -->
				<col style="min-width:100px;"></col> <!-- 수납유형코드 -->
				<col style="min-width:120px;"></col> <!-- 수납유형 -->
				<col style="min-width:120px;"></col> <!-- 예비항목1 -->
				<col style="min-width:120px;"></col> <!-- 예비항목2 -->
				<col style="min-width:120px;"></col> <!-- 예비항목3 -->
				<col style="min-width:200px;"></col> <!-- 예비항목4 -->
				<col style="min-width:120px;"></col> <!-- 예비항목5 -->
				<col style="min-width:200px;"></col> <!-- 생성일시-->
				
			</colgroup>
			<thead>
				<tr>
					<th scope="col">연계관리키</th>
					<th scope="col">자치단체코드</th>
					<th scope="col">자치단체명</th>
					<th scope="col">과세번호</th>
					<th scope="col">통합과세번호</th>
					<th scope="col">부서코드</th>
					<th scope="col">부서명</th>
					<th scope="col">특별회계사업코드</th>
					<th scope="col">특별회계사업명</th>
					<th scope="col">회계연도</th>
					<th scope="col">회계구분코드</th>
					<th scope="col">회계구분명</th>
					<th scope="col">대표세입과목코드</th>
					<th scope="col">세입과목명</th>
					<th scope="col">운영항목코드</th>
					<th scope="col">운영항목명</th>
					<th scope="col">부과번호</th>
					<th scope="col">분납번호</th>
					<th scope="col">전자납부번호</th>
					<th scope="col">수납번호</th>
					<th scope="col">수납구분코드</th>
					<th scope="col">수납구분명</th>
					<th scope="col">수납일자</th>
					<th scope="col">회계일자</th>
					<th scope="col">이체일자</th>
					<th scope="col">수납본세금액</th>
					<th scope="col">수납가산금액</th>
					<th scope="col">수납이자금액</th>
					<th scope="col">은행명</th>
					<th scope="col">수납유형코드</th>
					<th scope="col">수납유형</th>
					<th scope="col">예비항목1</th>
					<th scope="col">예비항목2</th>
					<th scope="col">예비항목3</th>
					<th scope="col">예비항목4</th>
					<th scope="col">예비항목5</th>
					<th scope="col">생성일시</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="i">
							<tr style="background:#fff;">
								<td><div><c:out value="${item.linkMngKey}"/></div></td>
								<td><div><c:out value="${item.sgbCd}"/></div></td>
								<td><div><c:out value="${item.sgbNm}"/></div></td>
								<td><div><c:out value="${item.taxnNo}"/></div></td>
								<td><div><c:out value="${item.untyTaxnNo}"/></div></td>
								<td><div><c:out value="${item.dptCd}"/></div></td>
								<td><div><c:out value="${item.dptNm}"/></div></td>
								<td><div><c:out value="${item.spclFisBizCd}"/></div></td>
								<td><div><c:out value="${item.spclFisBizNm}"/></div></td>
								<td><div><c:out value="${item.fyr}"/></div></td>
								<td><div><c:out value="${item.actSeCd}"/></div></td>
								<td><div><c:out value="${item.actSeNm}"/></div></td>
								<td><div><c:out value="${item.rprsTxmCd}"/></div></td>
								<td><div><c:out value="${item.rprsTxmNm}"/></div></td>
								<td><div><c:out value="${item.operItemCd}"/></div></td>
								<td><div><c:out value="${item.operItemNm}"/></div></td>
								<td><div><c:out value="${item.lvyNo}"/></div></td>
								<td><div><c:out value="${item.itmNo}"/></div></td>
								<td><div><c:out value="${item.epayNo}"/></div></td>
								<td><div><c:out value="${item.rcvmtNo}"/></div></td>
								<td><div><c:out value="${item.rcvmtSeCd}"/></div></td>
								<td><div><c:out value="${item.rcvmtSeNm}"/></div></td>
								<td><div><c:out value="${item.rcvmtYmd}"/></div></td>
								<td><div><c:out value="${item.actYmd}"/></div></td>
								<td><div><c:out value="${item.tsfYmd}"/></div></td>
								<td><div><c:out value="${item.rcvmtPctAmt}"/></div></td>
								<td><div><c:out value="${item.rcvmtAdtnAmt}"/></div></td>
								<td><div><c:out value="${item.rcvmtIntrAmt}"/></div></td>
								<td><div><c:out value="${item.bankNm}"/></div></td>
								<td><div><c:out value="${item.rcvmtTyCd}"/></div></td>
								<td><div><c:out value="${item.rcvmtTy}"/></div></td>
								<td><div><c:out value="${item.rsveItem1}"/></div></td>
								<td><div><c:out value="${item.rsveItem2}"/></div></td>
								<td><div><c:out value="${item.rsveItem3}"/></div></td>
								<td><div><c:out value="${item.rsveItem4}"/></div></td>
								<td><div><c:out value="${item.rsveItem5}"/></div></td>
								<td><div><c:out value="${item.frstRegistPnttm}"/></div></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr style="background:#fff;">
							<td colspan="12">데이터가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
	<div class="pagination-wrap">
		<c:if test="${not empty list}">
			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>
		</c:if>
	</div>
</div>
<!-- List -->

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

<script type="text/javascript">

	$(function() {
	  searchEnterKey();
	  displayChange();
      displaySelected();
	  Common.DateButtonEvent.set('.day_btn1 > a[class^=btn_date]', '', 'input[name="srchStartLogDate"]' , 'input[name="srchEndLogDate"]');
	  EventHandler.calendarStartDateAndEndDateVaild();

	});


	function displayChange() {
		$("#displayCount").on('change', function(){
			$("#itemsPerPage").val($(this).val());
			$("#itemsPerPageTemp").val($(this).val());
			$('#nextSunapRequestLogDto').submit();
		});
	}
	function displaySelected(){
		$("#displayCount").val($("#itemsPerPage").val());
	}

	function search() {
		var strStartDate = $("#srchStartLogDate").val();
		var strEndDate = $("#srchEndLogDate").val();

		if(strStartDate === '' || strEndDate === '') {
			alert("수납일자는 필수입니다.");
			return false;
		}

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#shCntrDeEnd").val();
				$("#srchEndLogDate").val("");
				$("#srchEndLogDate").focus();
				$("#srchEndLogDate").val(value);
				return false;
			}
		}
		$("#nextSunapRequestLogDto").submit();
	}

	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#epayNo").on('keydown', function(e){
			if (e.keyCode == '13') {
				$("#nextSunapRequestLogDto").submit();
			}
		});
	}
	</script>
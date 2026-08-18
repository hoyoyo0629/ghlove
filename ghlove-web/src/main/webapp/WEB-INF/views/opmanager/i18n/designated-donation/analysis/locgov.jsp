<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<style>
.tbl-summary {
	border-right: 1px solid #dedede;
	border-bottom: 1px solid #dedede;
}
.tbl-summary th,
.tbl-summary td {
	border-left: 1px solid #dedede;
	border-top: 1px solid #dedede;
	text-align: center;
}

.tbl-summary th {
	background: #f4f4f4;
	padding: 10px;
	color: #000;
}
.tbl-summary td {
	padding: 0 10px;
	font-family: verdana;
}
.tbl-summary td {
	padding: 22px;
	font-size: 20px;
	font-family: verdana;

}
</style>
<!-- <div class="admin_wrap"> -->
<div>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<div class="item_list">
		<h3><span>지자체별 통계</span></h3>

		<form:form modelAttribute="locgovSearchParam" method="post">
			<form:hidden path="query"/>

			<div class="board_write">
				<table class="board_write_table" summary="지자체별 통계 목록">
					<caption>지자체별 통계 목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
						<tr>
							<c:choose>
								<c:when test="${op:hasRole('ROLE_ADMIN_1')
											|| op:hasRole('ROLE_ADMIN_2')
											|| op:hasRole('ROLE_ADMIN_3')
											|| op:hasRole('ROLE_ADMIN_4')}">
			                        <td class="label">지자체</td>
			                        <td>
			                            <div class="flex_box gap-08">
				                            <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
							                    <form:option value="">-${op:message('M00039')}-</form:option>
							                    <c:forEach items="${wdr}" var="wdr">
							                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
							                    </c:forEach>
							                </form:select>
				                            <form:select path="shLocgovCode" class="wd-150">
							                    <option value="">-시,군,구-</option>
							                </form:select>
			                            </div>
			                        </td>
			                        <td class="label">사업구분</td>
									<td>
										<div class="flex_box gap-08">
											<form:select path="bsnsType" class="wd-300" title="사업구분 선택">
												<form:option value="" label="전체" />
												<c:forEach items="${bsnsTypeList}" var="bsnsType">
							                        <form:option value="${fn:escapeXml(bsnsType.id)}" label="${fn:escapeXml(bsnsType.detail)}" />
							                    </c:forEach>
											</form:select>
										</div>
									</td>
								</c:when>
								<c:otherwise>
			                        <td class="label">사업구분</td>
									<td colspan="3">
										<div class="flex_box gap-08">
											<form:select path="bsnsType" class="wd-300" title="사업구분 선택">
												<form:option value="" label="전체" />
												<c:forEach items="${bsnsTypeList}" var="bsnsType">
							                        <form:option value="${fn:escapeXml(bsnsType.id)}" label="${fn:escapeXml(bsnsType.detail)}" />
							                    </c:forEach>
											</form:select>
										</div>
									</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td class="label">기간</td>
							<td>
								<div>
									<span class="datepicker"><form:input path="frDt" maxlength="8" class="datepicker" title="조회 시작일" /></span>
									<span class="wave">~</span>
									<span class="datepicker"><form:input path="toDt" maxlength="8" class="datepicker" title="조회 종료일" /></span>
									<span class="day_btns" style="margin-left:0px;">
										<a href="javascript:;" class="btn_date today"><c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
											<a href="javascript:;" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
											<a href="javascript:;" class="btn_date day-15"><c:out value="${op:message('M00028')}"/></a><!-- 15일 -->
											<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
											<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
											<a href="javascript:;" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
									</span>
								</div>
							</td>
							<td class="label">상태</td>
							<td>
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="0" label="전체"  checked="checked"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="2" label="진행" />
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="9" label="종료" />
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="1" label="대기" />
									</div>
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
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/designated-donation/analysis/locgov';"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini" onclick="search(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>

			<div class="mt-40">
				<table class="tbl-summary">
					<colgroup>
						<col style="width: 200px" />
						<col style="width: 200px;" />
						<col style="width: 200px" />
						<col style="width: 200px;" />
					</colgroup>
					<thead>
						<tr>
							<th>총 목표금액</th> <!-- 총 목표금액 -->
							<th>총 모금액</th> <!-- 총 모금액 -->
							<th>총 모금 달성율</th> <!-- 총 모금 달성율 -->
							<th>총 기부건수</th> <!-- 총 참여자 수 -->
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><c:out value="${op:numberFormat(summaryData.totTargetAmt)}"/>원</td>
							<td><c:out value="${op:numberFormat(summaryData.totCntrAmt)}"/>원</td>
							<td><c:out value="${op:numberFormat(summaryData.totAchvRt)}"/>%</td>
							<td><c:out value="${op:numberFormat(summaryData.totCntrCnt)}"/>명</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(locgovSearchParam.pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
				</h5>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
						onchange="$('form#locgovSearchParam').submit();"> <!-- 화면 출력수 -->
						<form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
		                <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
		                <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
		                <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
					</form:select>
				</span>
			</div>
		</form:form>

		<div class="board_list">

			<table class="board_list_table" summary="지자체별 통계 목록">
				<caption>지자체별 통계 목록</caption>
				<colgroup>
					<col style="width:50px;">
					<col style="width:150px;">
	                <col style="width:100px;">
	                <col style="width:100px;">
	                <col style="width:100px;">
	                <col style="width:150px;">
	                <col style="width:100px;">
	                <col style="width:100px;">
	                <col style="width:100px;">
	                <col style="width:100px;">
	                <col style="width:150px;">
	                <col style="width:150px;">
	                <col style="width:100px;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">No</th>
						<th scope="col">지자체 명</th>
						<th scope="col">사업<br>(총건수)</th>
						<th scope="col">진행중<br>(건수)</th>
						<th scope="col">종료<br>(건수)</th>
						<th scope="col">기부건수</th>
						<th scope="col">취약계층<br>(건수)</th>
						<th scope="col">문화/예술<br>(건수)</th>
						<th scope="col">자원봉사<br>(건수)</th>
						<th scope="col">복리증진<br>(건수)</th>
						<th scope="col">목표금액</th>
						<th scope="col">모금액</th>
						<th scope="col">달성율</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ list }" var="list" varStatus="index">
						<tr style="background:#fff;">
							<td>
								<c:out value="${op:numberFormat(locgovSearchParam.pagination.itemNumber - index.count)}"/>
							</td>
							<td>
								<div>
									<a onclick="javascript:movePage('${fn:escapeXml(list.locgovCode)}', '${fn:escapeXml(locgovSearchParam.bsnsType)}', '${fn:escapeXml(locgovSearchParam.prjStatus)}', '${fn:escapeXml(locgovSearchParam.frDt)}', '${fn:escapeXml(locgovSearchParam.toDt)}');" style="cursor: pointer;">
										<c:out value="${list.locgovNm}"/>
									</a>
								</div>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.prjCnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.statu2Cnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.statu9Cnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.totCntrCnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.bsnsType100Cnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.bsnsType200Cnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.bsnsType300Cnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.bsnsType400Cnt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.targetAmt)}"/>원
							</td>
							<td>
								<c:out value="${op:numberFormat(list.cntrAmt)}"/>원
							</td>
							<td>
								<c:out value="${list.achvRt}"/>%
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${empty list}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>

		</div>

	</div>
</div>

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>
<script type="text/javascript">

	let changeYn = "N";

	$("select[name='shWdr']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	});

	$(function(){
	    wdrChange($("#shWdr").val());
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="frDt"]' , 'input[name="toDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});

	// 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();
		$("#shLocgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				$('#shLocgovCode').append('<option value="">-전체-</option>');
				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#shLocgovCode').append(options);
		        }

				// 조회 된 값 유지
				if ("${fn:escapeXml(locgovSearchParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(locgovSearchParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					//$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}

				let locgovCode = '<c:out value="${locgovSearchParam.shLocgovCode}"/>';

				let optionLists = $("#shLocgovCode")[0];
				if(optionLists != undefined) {
					for(let option of optionLists) {
						if (option.value == locgovCode) {
							option.setAttribute("selected", "selected");
							break;
						}
					}
				}
		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}


	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search(event) {
		var strStartDate = $("#frDt").val();
		var strEndDate = $("#toDt").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#toDt").focus();
				event.preventDefault();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) {
				event.preventDefault();
				return false;
			}
		}

		$("#locgovSearchParam").submit();
	}


	function movePage(locgovCode, bsnsType, prjStatus, frDt, toDt) {
		if (bsnsType == '0') {
			bsnsType = "";
		}
		if (prjStatus == '0') {
			prjStatus = "";
		}
		location.href = '/opmanager/designated-donation/list?locgovCode=' + locgovCode
						+ '&bsnsType=' + bsnsType
						+ '&prjStDt=' + frDt
						+ '&prjEdDt=' + toDt
						+ '&prjStatus=' + prjStatus;
	}


</script>


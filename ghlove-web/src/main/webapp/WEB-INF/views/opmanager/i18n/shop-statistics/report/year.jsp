<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">연간통계 현황</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span>연간통계 현황</span></h3>
	<form:form modelAttribute="statisticsParam" method="post" >
		<form:input type="hidden" path="tabId" value="1"/>

		<div class="board_write mt10">
			<table class="board_write_table" summary="연간통계 현황">
		            <colgroup>
		                <col style="width:220px;">
		                <col>
		                <col style="width:220px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <tr>
		                    <td class="label">기간</td>
		                    <td>
		                        <div class="flex_box gap-08" style="align-items: center;">
		                            <form:select path="shYear" title="${op:message('년도')}" class="wd-150" onChange="monthChange(this.value, ${statisticsParam.shMonth})">
		                              <c:forEach items="${yyyy}" var="yyyy">
						                  <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}" />
						              </c:forEach>
		                            </form:select>
		                            년
		                        </div>
		                    </td>
		                    <td class="label">지자체</td>
					        <td>

					          <div class="flex_box gap-08" style="align-items: center;">
		                        	<c:if test="${!op:hasRole('ROLE_ADMIN_5') && !op:hasRole('ROLE_ADMIN_6')}">
			                            <form:select path="shWdr" title="${op:message('광역지자체')}" class="wd-150" onChange="searchSigungu(this.value)">
			                                <option value="">시,도</option>
			                                <c:forEach items="${mctpv}" var="data" varStatus="i">
												<option value="${data.mctpvCd}">${data.mctpvNm}</option>
											</c:forEach>
			                            </form:select>
			                            <form:select path="shLocgovCode" title="${op:message('지자체')}" class="wd-150">
			                                <option value="">시,군,구</option>
			                            </form:select>
			                    	</c:if>
			                    	<c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
			                            <c:if test="${lclgvCd.length() > 0}">
			                            	<c:if test="${lclgvCd == mctpvCd}">
			                            		<c:forEach items="${mctpv}" var="data" varStatus="i">
			                            			<c:if test="${data.mctpvCd == mctpvCd}">
			                            				${data.mctpvNm }
			                            			</c:if>
			                            		</c:forEach>
			                            	</c:if>
			                            	<c:if test="${lclgvCd != mctpvCd}">
			                            		<c:forEach items="${mctpv}" var="data" varStatus="i">
			                            			<c:if test="${data.mctpvCd == mctpvCd}">
			                            				${data.mctpvNm }
			                            			</c:if>
			                            		</c:forEach>
			                            		<c:forEach items="${lclgv}" var="data" varStatus="i">
			                            			<c:if test="${data.lclgvCd == lclgvCd}">
			                            				${data.lclgvNm }
			                            			</c:if>
			                            		</c:forEach>
			                            	</c:if>
			                            </c:if>
			                    	</c:if>
		                        </div>
					        </td>
		                </tr>
		            </tbody>
		        </table>

		</div> <!-- // board_write -->
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
		        <li>
		            엑셀 버튼을 클릭하면 조건에 맞는 엑셀파일을 다운로드할 수 있습니다.
		        </li>
	        </ul>
	  	</div>
		<%-- 검색버튼 --%>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>매년 2월 28일 기준 집계 완료된 통계자료(작년)까지만 조회 가능합니다.</caption>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelPastYearStatsStored()">엑셀</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/report/year'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
			</div>
		</div>
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<!-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="statisticsYearBatch();">연통계집계-적재</button> -->
			</div>
		</div>
	</form:form>


</div>

<script type="text/javascript">
	function excelPastYearStatsStored() {
		if($("#shWdr").val() == "") {
			alert("상위 지자체를 선택해 주세요.");
			$("#shWdr").focus();
			return false;
		}

		if($("#shLocgovCode").val() == "") {
			alert("지자체을 선택해 주세요.");
			$("#shLocgovCode").focus();
			return false;
		}
		Shop.downloadExcelOrder("/opmanager/shop-statistics/report/lclgv/excel-pastYearStatsStored", $('#statisticsParam').serialize(), false);
	}


	function monthChange(year, month) {
		$("#shYear").val(year).prop("selected", true);
		var nowYear = '${nowYear}';
		var nowMonth = '${nowMonth}';
		var searchYear = '${statisticsParam.shYear}';
		var searchMonth = '${statisticsParam.shMonth}';
		Common.loading.display = false;

		$("#shMonth option").remove();
		if ($("#shYear").val() != "") {
			if(year == nowYear) {
				for (var i = 1; i < parseInt(nowMonth); i++) {
		            var options = '<option value="' + i + '">' + i + '</option>';
		            $('#shMonth').append(options);
		        }
				if(year == searchYear) {
					$("#shMonth").val(month).prop("selected", true);
				} else {
					$("#shMonth").val(parseInt(nowMonth)-1).prop("selected", true);
				}
			} else {
				for (var i = 1; i <= 12; i++) {
		            var options = '<option value="' + i + '">' + i + '</option>';
		            $('#shMonth').append(options);
		        }
				if(year == searchYear) {
					$("#shMonth").val(month).prop("selected", true);
				} else {
					$("#shMonth").val(12).prop("selected", true);
				}
			}
		} else {
	        $('#shMonth').append('<option value="">검색월</option>');
		}
	}

	function searchSigungu(mctpv, lclgv, tabId) {
		$("#shWdr").val(mctpv).prop("selected", true);
		$("#shLocgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$.post(url("/opmanager/shop-statistics/report/lclgv/options-by-lclgv"), {'mctpv' : mctpv, 'tabId' : tabId}, function(response) {
				$('#shLocgovCode').append('<option value="">시,군,구</option>');
				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].lclgvCd + '">' + response[i].lclgvNm + '</option>';
		            $('#shLocgovCode').append(options);
		        }

				$("#shLocgovCode").val(lclgv).prop("selected", true);

		    });
		} else {
	        $('#shLocgovCode').append('<option value="">시,군,구</option>');
		}
	}

	function statisticsYearBatch() {
		uri = "/opmanager/shop-statistics/report/general/statisticsYearBatch";
		location.href = uri;
	}
</script>
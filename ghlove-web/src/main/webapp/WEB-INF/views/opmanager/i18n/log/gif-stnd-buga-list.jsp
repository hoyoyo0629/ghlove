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

<h3><span>지방세외 부과연계 로그</span></h3>

<form:form modelAttribute="nextBugaRequestLogDto" cssClass="opmanager-search-form clear" method="post" id="nextBugaRequestLogDto">
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
					<td class="label">응답코드</td>
					<td>
						<div>
							<form:input path="bugaStatusCd" title="응답코드"  value="${fn:escapeXml(nextBugaRequestLogDto.bugaStatusCd)}" type="text"/>
						</div>
					</td>

					<td class="label">연계결과코드</td>
					<td>
						<div>
							<form:input path="linkRstCd" title="연계결과코드" value="${fn:escapeXml(nextBugaRequestLogDto.linkRstCd)}" type="text" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">전자납부번호</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:input path="epayNo" title="전자납부번호" class="input_txt required _filter wd-500" type="text" value="${fn:escapeXml(nextBugaRequestLogDto.epayNo)}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">납부자명</td>
					<td colspan="3">
						<div class="flex_box gap-08">
							<form:input path="srchpyrNm" title="납부자명" class="input_txt required _filter wd-500" type="text" value="${fn:escapeXml(nextBugaRequestLogDto.srchpyrNm)}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">부과일자</td>
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
				<tr>
					<td class="label">연계결과</td>
					<td colspan="3">
						 <div class="flex_box gap-12">
						 	<div class="input-form">
			                	<form:radiobutton path="srchlinkRstYn" value="" label="전체" checked="checked"/>
			                </div>
						 	<div class="input-form">
			                	<form:radiobutton path="srchlinkRstYn" value="Y" label="성공" />
			                </div>
			                <div class="input-form">
			                	<form:radiobutton path="srchlinkRstYn" value="N" label="실패"/>
			                </div>
			             </div>
					</td>

				</tr>
			</tbody>
		</table>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/log/gif-stnd-buga'">초기화</button>
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
				<col style="min-width:100px;"></col> <!-- 지자체 (시도)  -->
				<col style="min-width:100px"></col> <!-- 지자체 (시군구)  -->
				<col style="min-width:150px"></col> <!--  연계관리키  -->
				<col style="min-width:120px"></col> <!-- 자치단체코드  -->
				<col style="min-width:120px"></col> <!-- 연계대상코드  -->
				<col style="min-width:120px"></col> <!-- 부서코드  -->
				<col style="min-width:120px"></col> <!-- 특별회계사업코드  -->
				<col style="min-width:100px"></col> <!-- 회계연도 -->
				<col style="min-width:100px"></col> <!-- 회계구분코드  -->
				<col style="min-width:100px"></col> <!-- 대표세입과목코드  -->
				<col style="min-width:100px"></col> <!-- 운영항목코드 -->
				<col style="min-width:100px"></col> <!-- 부과일자  -->
				<col style="min-width:100px"></col> <!-- 최초본세금액  -->
				<col style="min-width:120px"></col> <!-- 최초납기일자  -->
				<col style="min-width:120px"></col> <!-- 납부자구분코드  -->
				<col style="min-width:100px"></col> <!-- 납부자번호  -->
				<col style="min-width:100px"></col> <!-- 납부자명  -->
				<col style="min-width:120px"></col> <!-- 대표납부자번호  -->
				<col style="min-width:120px"></col> <!-- 대표납부자명  -->
				<col style="min-width:120px"></col> <!-- 납부자상태코드  -->
				<col style="min-width:130px"></col> <!-- 지번도로주소구분코드  -->
				<col style="min-width:100px"></col> <!-- 우편번호  -->
				<col style="min-width:100px"></col> <!-- 도로명코드  -->
				<col style="min-width:100px"></col> <!-- 건물본번  -->
				<col style="min-width:100px"></col> <!-- 건물부번  -->
				<col style="min-width:100px"></col> <!-- 법정동코드  -->
				<col style="min-width:100px"></col> <!-- 행정동코드  -->
				<col style="min-width:150px"></col> <!-- 도로상세주소  -->
				<col style="min-width:120px"></col> <!-- 물건지명  -->
				<col style="min-width:120px"></col> <!-- 관리1항목  -->
				<col style="min-width:100px"></col> <!-- 연계상태  -->
				<col style="min-width:100px"></col> <!-- 연계결과코드  -->
				<col style="min-width:280px"></col> <!-- 연계결과메세지  -->
				<col style="min-width:200px"></col> <!-- 생성일시  -->

			</colgroup>
			<thead>
				<tr>
					<th scope="col">지자체(시도)</th>
					<th scope="col">지자체(시군구)</th>
					<th scope="col">연계관리키</th>
					<th scope="col">자치단체코드</th>
					<th scope="col">연계대상코드</th>
					<th scope="col">부서코드</th>
					<th scope="col">특별회계사업코드</th>
					<th scope="col">회계연도</th>
					<th scope="col">회계구분코드</th>
					<th scope="col">대표세입과목코드</th>
					<th scope="col">운영항목코드</th>
					<th scope="col">부과일자</th>
					<th scope="col">최초본세금액</th>
					<th scope="col">최초납기일자</th>
					<th scope="col">납부자구분코드</th>
					<th scope="col">납부자번호</th>
					<th scope="col">납부자명</th>
					<th scope="col">대표납부자번호</th>
					<th scope="col">대표납부자명</th>
					<th scope="col">납부자상태코드</th>
					<th scope="col">지번도로주소구분코드</th>
					<th scope="col">우편번호</th>
					<th scope="col">도로명코드</th>
					<th scope="col">건물본번</th>
					<th scope="col">건물부번</th>
					<th scope="col">법정동코드</th>
					<th scope="col">행정동코드</th>
					<th scope="col">도로명상세주소</th>
					<th scope="col">물건지명</th>
					<th scope="col">관리1항목</th>
					<th scope="col">연계상태</th>
					<th scope="col">연계결과코드</th>
					<th scope="col">연계결과메시지</th>
					<th scope="col">생성일시</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="i">
							<tr style="background:#fff;">
								<td><div><c:out value="${item.upperLocgovNm}"/></div></td>
								<td><div><c:out value="${item.locgovNm}"/></div></td>
								<td><div><c:out value="${item.linkMngKey}"/></div></td>
								<td><div><c:out value="${item.sgbCd}"/></div></td>
								<td><div><c:out value="${item.linkTrgtCd}"/></div></td>
								<td><div><c:out value="${item.dptCd}"/></div></td>
								<td><div><c:out value="${item.spclFisBizCd}"/></div></td>
								<td><div><c:out value="${item.fyr}"/></div></td>
								<td><div><c:out value="${item.actSeCd}"/></div></td>
								<td><div><c:out value="${item.rprsTxmCd}"/></div></td>
								<td><div><c:out value="${item.operItemCd}"/></div></td>
								<td><div><c:out value="${item.lvyYmd}"/></div></td>
								<td><div><c:out value="${item.frstPctAmt}"/></div></td>
								<td><div><c:out value="${item.frstPidYmd}"/></div></td>
								<td><div><c:out value="${item.pyrSeCd}"/></div></td>
								<td><div><c:out value="${item.pyrNo}"/></div></td>
								<td><div><c:out value="${item.pyrNm}"/></div></td>
								<td><div><c:out value="${item.rprsPyrNo}"/></div></td>
								<td><div><c:out value="${item.rprsPyrNm}"/></div></td>
								<td><div><c:out value="${item.pyrSttCd}"/></div></td>
								<td><div><c:out value="${item.lotnoRoadAddrSeCd}"/></div></td>
								<td><div><c:out value="${item.zip}"/></div></td>
								<td><div><c:out value="${item.roadNmCd}"/></div></td>
								<td><div><c:out value="${item.bmno}"/></div></td>
								<td><div><c:out value="${item.bsno}"/></div></td>
								<td><div><c:out value="${item.stdgCd}"/></div></td>
								<td><div><c:out value="${item.dongCd}"/></div></td>
								<td><div><c:out value="${item.roadNmDaddr}"/></div></td>
								<td><div><c:out value="${item.glNm}"/></div></td>
								<td><div><c:out value="${item.mngItemCn1}"/></div></td>
								<td><div><c:out value="${item.bugaStatusCd}"/></div></td>
								<td><div><c:out value="${item.linkRstCd}"/></div></td>
								<td><div><c:out value="${item.linkRstMsg}"/></div></td>
								<td><div><c:out value="${item.frstRegistPnttm}"/></div></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr style="background:#fff;">
							<td colspan="14">데이터가 없습니다.</td>
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
			$('#nextBugaRequestLogDto').submit();
		});
	}
	function displaySelected(){
		$("#displayCount").val($("#itemsPerPage").val());
	}


	function search() {
		var strStartDate = $("#srchStartLogDate").val();
		var strEndDate = $("#srchEndLogDate").val();

		if(strStartDate === '' || strEndDate === '') {
			alert("부과일자는 필수입니다.");
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
		$("#nextBugaRequestLogDto").submit();
	}

	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#epayNo").on('keydown', function(e){
			if (e.keyCode == '13') {
				$("#nextBugaRequestLogDto").submit();
			}
		});

		$("#srchpyrNm").on('keydown', function(e){
			if (e.keyCode == '13') {
				$("#nextBugaRequestLogDto").submit();
			}
		});
	}
	</script>
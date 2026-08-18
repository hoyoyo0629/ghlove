<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">총괄 누계 현황</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span>총괄 누계 현황</span></h3>
	<form:form modelAttribute="statisticsParam" method="post" >
		<form:input type="hidden" path="tabId" value="1"/>

		<div class="board_write mt10">
			<table class="board_write_table" summary="총괄 누계 현황">
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
		                                <c:forEach items="${crtrYear}" var="data" varStatus="i">
											<option value="${data.crtrYr}">${data.crtrYr}</option>
										</c:forEach>
		                            </form:select>
		                            년
		                            <form:select path="shMonth" title="${op:message('월')}" class="wd-150">
		                                	<c:forEach var="i" begin="1" end="12" step="1">
		                                		<c:if test="${i < nowMonth}">
													<option value="${i}">${i}</option>
												</c:if>
											</c:forEach>
		                            </form:select>
		                            월
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
	        </ul>
	  	</div>
		<%-- 검색버튼 --%>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>현재 날짜 기준 집계 완료된 통계자료(직전월)까지만 조회 가능합니다.</caption>
				<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:search()"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/report/generalTotal'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
			</div>
		</div>
	</form:form>

	<div class="board_list">
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<button type="button" id="btn_tab1" class="btn btn-dark-gray btn-mini">기부방법별 기부 현황(누계)</button>
				<button type="button" id="btn_tab2"  class="btn btn-dark-gray btn-mini">금액별 기부건수 현황(누계)</button>
				<button type="button" id="btn_tab3"  class="btn btn-dark-gray btn-mini">연령별 기부건수 현황(누계)</button>
				<button type="button" id="btn_tab4"  class="btn btn-dark-gray btn-mini">거주지역->기부지역 건수 현황(누계)</button>
			</div>
		</div>

		<div id="divCaptionForPath" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 건, 원, %)</caption>
			</div>
		</div>
		<div id="tbDntnPathTnocsStats">
			<table class="board_list_table" summary="기부방법별 기부 현황">
				<thead>
					<tr>
						<th scope="col">구분</th>
						<th scope="col">기부금액</th>
						<th scope="col">기부건수</th>
						<th scope="col">기부금액(%)</th>
						<th scope="col">기부건수(%)</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${dntnPathTnocsStats}" var="list" varStatus="i">
						<tr style="background:#fff;">
							<td>
								<c:out value="${op:numberFormat(list.dntnPath)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.gramt)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.tnocs)}"/>
							</td>
							<td>
								<c:out value="${op:numberFormat(list.gramtrt)}"/>%
							</td>
							<td>
								<c:out value="${op:numberFormat(list.rt)}"/>%
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div id="divCaptionForAmt" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 건, 원, %)</caption>
			</div>
		</div>
		<div id="tbDntnAmtTnocsStats">
			<table class="board_list_table" summary="기부금액별 기부 현황">
				<thead>
					<tr>
						<th scope="col">구분</th>
						<th scope="col">합계</th>
						<th scope="col">10만원 미만</th>
						<th scope="col">10만원</th>
						<th scope="col">10만원~100만원 미만</th>
						<th scope="col">100만원~500만원 미만</th>
						<th scope="col">500만원</th>
						<th scope="col">500만원~2000만원 미만</th>
						<th scope="col">2000만원</th>
					</tr>
				</thead>
				<tbody>
					<tr style="background:#fff;">
						<c:forEach items="${dntnAmtTnocsStats}" var="list" varStatus="i">
							<c:if test="${list.dntnAmtCd == '00'}">
								<td>
									건수
								</td>
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '10'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '20'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '30'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '40'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '50'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '60'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '70'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
						</c:forEach>
					</tr>
					<tr style="background:#fff;">
						<c:forEach items="${dntnAmtTnocsStats}" var="list" varStatus="i">
							<c:if test="${list.dntnAmtCd == '00'}">
								<td>
									비율
								</td>
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '10'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '20'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '30'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '40'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '50'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '60'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAmtCd == '70'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
						</c:forEach>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="divCaptionForAge" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 건, 원, %)</caption>
			</div>
		</div>
		<div id="tbDntnAgeTnocsStats">
			<table class="board_list_table" summary="기부연령별 기부 현황">
				<thead>
					<tr>
						<th scope="col">구분</th>
						<th scope="col">합계</th>
						<th scope="col">20대 미만</th>
						<th scope="col">20대</th>
						<th scope="col">30대</th>
						<th scope="col">40대</th>
						<th scope="col">50대</th>
						<th scope="col">60대</th>
						<th scope="col">70대</th>
						<th scope="col">80대 이상</th>
						<th scope="col">생년월일 없음</th>
					</tr>
				</thead>
				<tbody>
					<tr style="background:#fff;">
						<c:forEach items="${dntnAgeTnocsStats}" var="list" varStatus="i">
							<c:if test="${list.dntnAgeCd == '00'}">
								<td>
									건수
								</td>
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '10'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '20'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '30'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '40'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '50'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '60'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '70'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '80'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '90'}">
								<td>
									<c:out value="${op:numberFormat(list.tnocs)}"/>
								</td>
							</c:if>
						</c:forEach>
					</tr>
					<tr style="background:#fff;">
						<c:forEach items="${dntnAgeTnocsStats}" var="list" varStatus="i">
							<c:if test="${list.dntnAgeCd == '00'}">
								<td>
									비율
								</td>
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '10'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '20'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '30'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '40'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '50'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '60'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '70'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '80'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
							<c:if test="${list.dntnAgeCd == '90'}">
								<td>
									<c:out value="${op:numberFormat(list.rt)}"/>%
								</td>
							</c:if>
						</c:forEach>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="tbHabDntnMctpvTnocsStats">
			<table class="board_list_table"summary="기부연령별 기부 현황">
				<thead>
					<tr>
						<th scope="col">거주지역>기부지역</th>
						<th scope="col">서울특별시</th>
						<th scope="col">부산광역시</th>
						<th scope="col">대구광역시</th>
						<th scope="col">인천광역시</th>
						<th scope="col">광주광역시</th>
						<th scope="col">대전광역시</th>
						<th scope="col">울산광역시</th>
						<th scope="col">세종특별자치시</th>
						<th scope="col">경기도</th>
						<th scope="col">충청북도</th>
						<th scope="col">충청남도</th>
						<th scope="col">전라남도</th>
						<th scope="col">경상북도</th>
						<th scope="col">경상남도</th>
						<th scope="col">제주특별자치도</th>
						<th scope="col">강원특별자치도</th>
						<th scope="col">전북특별자치도</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${habDntnMctpvTnocsStats}" var="list" varStatus="i">
						<tr>
							<td>
								<c:out value="${list.habMctpvNm }" />
							</td>
							<td>
								<c:out value="${list.dntnCntSeoul}" />
							</td>
							<td>
								<c:out value="${list.dntnCntBusan}" />
							</td>
							<td>
								<c:out value="${list.dntnCntDaegu}" />
							</td>
							<td>
								<c:out value="${list.dntnCntIncheon}" />
							</td>
							<td>
								<c:out value="${list.dntnCntGwangju}" />
							</td>
							<td>
								<c:out value="${list.dntnCntDaejeon}" />
							</td>
							<td>
								<c:out value="${list.dntnCntUlsan}" />
							</td>
							<td>
								<c:out value="${list.dntnCntSejong}" />
							</td>
							<td>
								<c:out value="${list.dntnCntGyeonggi}" />
							</td>
							<td>
								<c:out value="${list.dntnCntChungbuk}" />
							</td>
							<td>
								<c:out value="${list.dntnCntChungnam}" />
							</td>
							<td>
								<c:out value="${list.dntnCntJeonnam}" />
							</td>
							<td>
								<c:out value="${list.dntnCntGyeongbuk}" />
							</td>
							<td>
								<c:out value="${list.dntnCntGyeongnam}" />
							</td>
							<td>
								<c:out value="${list.dntnCntJeju}" />
							</td>
							<td>
								<c:out value="${list.dntnCntGangwon}" />
							</td>
							<td>
								<c:out value="${list.dntnCntJeonbuk}" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<%-- 엑셀버튼 --%>
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
	function excelDownload() {
		Shop.downloadExcelOrder("/opmanager/shop-statistics/report/lclgv/excel-download", $('#statisticsParam').serialize(), false);
	}

	$("#btn_tab1").click(function() {
		document.getElementById("tbDntnPathTnocsStats").style.display = "";
		document.getElementById("tbDntnAmtTnocsStats").style.display = "none";
		document.getElementById("tbDntnAgeTnocsStats").style.display = "none";
		document.getElementById("tbHabDntnMctpvTnocsStats").style.display = "none";
		document.getElementById("divCaptionForPath").style.display = "";
		document.getElementById("divCaptionForAmt").style.display = "none";
		document.getElementById("divCaptionForAge").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${dntnPathTnocsStats}" var="dpts" varStatus="i">
			var crtrYr = '${dpts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "1";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	$("#btn_tab2").click(function() {
		document.getElementById("tbDntnPathTnocsStats").style.display = "none";
		document.getElementById("tbDntnAmtTnocsStats").style.display = "";
		document.getElementById("tbDntnAgeTnocsStats").style.display = "none";
		document.getElementById("tbHabDntnMctpvTnocsStats").style.display = "none";
		document.getElementById("divCaptionForPath").style.display = "none";
		document.getElementById("divCaptionForAmt").style.display = "";
		document.getElementById("divCaptionForAge").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${dntnPathTnocsStats}" var="dpts" varStatus="i">
			var crtrYr = '${dpts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "2";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	$("#btn_tab3").click(function() {
		document.getElementById("tbDntnPathTnocsStats").style.display = "none";
		document.getElementById("tbDntnAmtTnocsStats").style.display = "none";
		document.getElementById("tbDntnAgeTnocsStats").style.display = "";
		document.getElementById("tbHabDntnMctpvTnocsStats").style.display = "none";
		document.getElementById("divCaptionForPath").style.display = "";
		document.getElementById("divCaptionForAmt").style.display = "none";
		document.getElementById("divCaptionForAge").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${dntnPathTnocsStats}" var="dpts" varStatus="i">
			var crtrYr = '${dpts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "3";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	$("#btn_tab4").click(function() {
		document.getElementById("tbDntnPathTnocsStats").style.display = "none";
		document.getElementById("tbDntnAmtTnocsStats").style.display = "none";
		document.getElementById("tbDntnAgeTnocsStats").style.display = "none";
		document.getElementById("tbHabDntnMctpvTnocsStats").style.display = "";
		document.getElementById("divCaptionForPath").style.display = "none";
		document.getElementById("divCaptionForAmt").style.display = "none";
		document.getElementById("divCaptionForAge").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#00215A");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${dntnPathTnocsStats}" var="dpts" varStatus="i">
			var crtrYr = '${dpts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "4";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	document.addEventListener("DOMContentLoaded", function(){
		var tabVal = '${tabId}';
		if(tabVal == "1") {
			$("#btn_tab1").trigger("click");
		} else if(tabVal == "2") {
			$("#btn_tab2").trigger("click");
		} else if(tabVal == "3") {
			$("#btn_tab3").trigger("click");
		} else {
			$("#btn_tab4").trigger("click");
		}
	});

	function monthChange(year, month) {
		$("#shYear").val(year).prop("selected", true);
		var nowYear = '${nowYear}';
		var nowMonth = '${nowMonth}';
		var searchYear = '${statisticsParam.shYear}';
		var searchMonth = '${statisticsParam.shMonth}';
		Common.loading.display = false;

		console.log("year : " + year + ", nowYear : " + nowYear + ", searchYear : " + searchYear + ", nowMonth : " + nowMonth + ", searchMonth : " + searchMonth);

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
</script>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">월별 현황보고</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span>월별 매출</span></h3>
	<form:form modelAttribute="statisticsParam" method="post" >
		<div class="board_write">
			<table class="board_write_table" summary="${op:message('M01391')}"> <!-- 월별 매출 -->
				<caption>${op:message('M01391')}</caption> <!-- 월별 매출 -->
				<colgroup>
					<col style="width: 140px;">
					<col style="width: auto;">
				</colgroup>
				<tbody>
					<tr>
						<td class="label">${op:message('M01347')}</td> <!-- 기간 -->
						<td>
					 		<div>
								<select title="${op:message('M01392')}" id="startYear" name="startYear"> <!-- 시작 년도 -->
									<c:forEach begin="2010" end="${lastYear}" varStatus="i">
										<option value="${i.index}" ${op:selected(i.index,statisticsParam.startYear) }>${i.index }</option>
									</c:forEach>
								</select> ${op:message('M01076')}<!-- 년 -->

								<select title="${op:message('M01393')}" id="startMonth" name="startMonth"> <!-- 시작 월 -->
									<c:forEach begin="1" end="12" varStatus="i">
										<c:if test="${i.index < 10 }">
											<c:set var="month">0${i.index}</c:set>
											<option value="0${i.index}" ${op:selected(month,statisticsParam.startMonth) }>0${i.index }</option>
										</c:if>
										<c:if test="${i.index >= 10 }">
											<option value="${i.index}" ${op:selected(i.index,statisticsParam.startMonth) }>${i.index }</option>
										</c:if>
									</c:forEach>
								</select> ${op:message('M01077')}<!-- 월 -->
							</div>
				 		</td>
					</tr>
					<%--
					<tr>
						<td class="label">판매자</td>
						<td>
							<div>
								<form:select path="sellerId">
									<form:option value="0">${op:message('M00039')}</form:option>
									<c:forEach items="${sellerList}" var="list" varStatus="i">
										<form:option value="${list.sellerId}">[${list.loginId}] ${list.sellerName}</form:option>
									</c:forEach>
								</form:select>
								<a href="javascript:Common.popup('/opmanager/seller/find', 'find_seller', 800, 500, 1)" class="btn btn-gradient btn-xs">검색</a>
							</div>
						</td>
					</tr>
					--%>
				</tbody>
			</table>

		</div> <!-- // board_write -->

		<%-- 검색버튼 --%>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/dashboard/day'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:fnSearch();"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>
	</form:form>

	<div class="sort_area mt30">
		<div class="left">
			<span>${op:message('M01363')} : <span class="font_b">${op:numberFormat(total.totalCount)}</span>${op:message('M00272')} (${op:message('M01364')} : ${op:numberFormat(total.saleCount)}${op:message('M00272')}, ${op:message('M01365')} : ${op:numberFormat(total.cancelCount)}${op:message('M00272')})</span> | <span>${op:message('M01366')} : <span class="font_b">${op:numberFormat(total.totalPayAmount)}</span>${op:message('M00814')}</span>
		</div>
	</div>

	<div class="board_list">

		<c:set var="viewType" value="month" scope="request" />
		<table class="board_list_table stats ${statisticsParam.displaySubtotal == 'N' ? 'odd-even' : ''}">
			<thead>
				<tr>
					<th colspan="5">총괄현황</th>
				</tr>
				<tr>
					<th colspan="2">구 분</th>
					<th></th>
					<th>전일 대비</th>
					<th>전년동일<br>대비(누적)비고</th>
				</tr>
			</thead>
			<tbody>
<%-- 			<c:forEach items="${ statisticsParamList }" var="total"> --%>
<%-- 			<c:set value="${ statisticsMonthInfo }" var="total" /> --%>
<%-- 			${op:numberFormat(total.totalSaleAmt)} --%>
				<tr>
					<td colspan="2">회원 수</td> <!-- 회원수 -->
					<td class="number"></td>
					<td class="number"></td>
					<td class="number"></td>
				</tr>
				<tr>
					<td rowspan="2">기부</td>
					<td>기부건수</td>
					<td class="number"></td>
					<td class="number"></td>
					<td class="number"></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">기부금액</td>
					<td class="number"></td>
					<td class="number"></td>
					<td class="number"></td>
				</tr>
				<tr>
					<td rowspan="3">답례품</td>
					<td>등록건수</td>
					<td class="number"></td>
					<td class="number"></td>
					<td class="number"></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">신청건수</td>
					<td class="number">${statisticsMonthInfo.totalCnt}</td>
					<td class="number">${statisticsMonthInfo.totalCnt}</td>
					<td class="number">${statisticsMonthInfo.totalCnt}</td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">판매액</td>
					<td class="number">${statisticsMonthInfo.totalSaleAmt}</td>
					<td class="number">${statisticsMonthInfo.totalSaleAmt}</td>
					<td class="number">${statisticsMonthInfo.totalSaleAmt}</td>
				</tr>
<%-- 			</c:forEach> --%>
			</tbody>
		</table>

		<table class="board_list_table stats ${statisticsParam.displaySubtotal == 'N' ? 'odd-even' : ''}">
			<thead>
				<tr>
					<th colspan="11">연령대별 기부현황</th>
				</tr>
				<tr>
					<th colspan="2">구 분</th>
					<th>합계</th>
					<th>19세이하</th>
					<th>20대</th>
					<th>30대</th>
					<th>40대</th>
					<th>50대</th>
					<th>60대</th>
					<th>70대</th>
					<th>80대<br>이상</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td rowspan="2">2024</td>
					<td>건수</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">비율</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td rowspan="2">2023</td>
					<td>건수</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">비율</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tbody>
		</table>

		<table class="board_list_table stats ${statisticsParam.displaySubtotal == 'N' ? 'odd-even' : ''}">
			<thead>
				<tr>
					<th colspan="9">금액대별 기부 현황</th>
				</tr>
				<tr>
					<th rowspan="3">구 분</th>
					<th rowspan="2" colspan="2">24년</th>
					<th rowspan="2" colspan="2">23년</th>
					<th colspan="4">증감</th>
				</tr>
				<tr>
					<th colspan="2" style="border-left:1px solid #d5d5d5;">2월</th>
					<th colspan="2">누계</th>
				</tr>
				<tr>
					<th style="border-left:1px solid #d5d5d5;">2월<br>(A)</th>
					<th>누계<br>(B)</th>
					<th>2월<br>(C)</th>
					<th>누계<br>(D)</th>
					<th>건수<br>(C-A)</th>
					<th>비율<br>(C-A)/C</th>
					<th>건수<br>(D-B)</th>
					<th>비율<br>(D-B)/D</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>합계</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>~10만원 미만</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>10만원</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>10만원 초과~<br>100만원 미만</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>100만원 이상<br>~500만원 미만</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>500만원</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tbody>
		</table>


	    <div class="graph_wrap all" style="width: 1000px;">
	        <div class="bd_gray mb15">
	            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 기부건수</p>
	            <div class="graph">
	                <canvas id="giveCountLineChart"></canvas>
	            </div>
	        </div>
	    </div>


		<sec:authorize access="hasRole('ROLE_EXCEL')">
			<div class="btn_all">
				<div class="right">
					<a href="/opmanager/shop-statistics/sales/month/excel-download?${fn:escapeXml(queryString)}"  class="btn btn-success btn-sm"><span class="glyphicon glyphicon-save"></span>${op:message('M00254')}</a> <!-- 엑셀 다운로드 -->
				</div>
			</div>
		</sec:authorize>

	</div>


</div>

<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		statisticsAllByMonth();
	});

	function sellerSeller(sellerId) {
		$('#sellerId').val(sellerId)
	}

	function excelDownload() {
		let startDate = document.getElementById('startDate').value;
		let isDate = false;

		if (Common.validateDate(startDate + '01') && Common.validateDate(endDate + '01')) {
			if (Number(startDate) <= Number(endDate)) {
				isDate = true;
			}
		}

		if (!isDate) {
			alert('정산 예정일을 확인해주세요.');
			return;
		}
		let param = $('#remittanceParam').serialize();

		Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/list-excel", param, true);
	}

	function statisticsAllByMonth() {
        var monthArr = [];
        var cntArr = [	// multi type
        	{type: 'line', label : '건수별(건)', data : [], backgroundColor:'#09C2C7', borderColor:'#09C2C7', minBarLength: 0, yAxisID: 'y'}
        	, {type: 'bar', label : '금액별(천원)', data : [], backgroundColor:'#004CCE', borderColor:'#004CCE', minBarLength: 0, yAxisID: 'y1'}
        	];

        $.post('/opmanager/shop-statistics/dashboard/month/' + $("#startYear").val() , {}, function(resp) {

            if (resp.isSuccess) {
                resp.data.forEach(function (d, i) {
                    monthArr.push(d.payMonthStr ? d.payMonthStr : 0);
                    cntArr[0].data.push(d.totalCnt ? d.totalCnt : 0);
                    cntArr[1].data.push(d.totalSaleAmt && d.totalSaleAmt > 0 ? Math.floor(d.totalSaleAmt/1000) : 0);
                });

				monthChart = ChartCommon.drawChartMultiYaxis(
							'giveCountLineChart'
							, 'bar'
							, {
								scales:{
					                x:{ // x축값 누적
					                    stacked:false
					                },
					                y:{ // y축값 누적
					                    stacked:false
					                },
					                y1:{ // y1축값 누적
					                    stacked:false
					                }
					            }
					        }
							, monthArr
							, cntArr
							, {
								x : '월',
								y : '건',
								y1: '천원'
							}
// 							, "FIXED"
						);

            } else {
                alert(resp.errorMessage);
            }
        });
    }

</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<script type="text/javascript" src="/content/modules/clipboard.min.js"></script>
<script type="text/javascript" src="/content/modules/report/daily.report.js?v=<%= System.currentTimeMillis() %>"></script>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">일일 현황보고</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span><c:out value="${op:message('M01376')}"/></span></h3> <!-- 일자별 매출 -->
	<form:form modelAttribute="statisticsParam" method="post" >
		<div class="board_write">
			<table class="board_write_table" summary="${op:message('M01376')}">
				<caption><c:out value="${op:message('M01376')}"/></caption>
				<colgroup>
					<col style="width: 140px;">
					<col style="width: auto;">
				</colgroup>
				<tbody>
					<tr>
						<td class="label"><c:out value="${op:message('M01347')}"/></td> <!-- 기간 -->
						<td>
					 		<div>
								<span class="datepicker"><form:input path="startDate" class="term datepicker" title="${op:message('M00507')}" id="dp28" /></span> <!-- 시작일 -->
								<span class="datepicker" style="display:none;"><form:input path="endDate" class="datepicker" maxlength="6" title="정산일자 종료일"/><!-- 정산일자 종료일 --></span>
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
			<div class="flex_box gap-08" style="align-items: center;">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/dashboard/day'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:fnSearch();"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
				<span class="datepicker"><input type="text" class="term datepicker" id="report-searchdate"/></span>
				<button type="button" class="btn btn-dark-gray btn-mini" id="daily-report">행안부 일일보고</button>
				<button type="button" class="btn btn-dark-gray btn-mini" id="week-report">행안부 주간보고</button>
			</div>
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08" style="align-items: center;">
				<span style="color:red;">*일일보고: 지정한 날짜 기준 / 주간보고: 지정한 날짜 부터 -6일 사이 기준</span>
			</div>
		</div>
	</form:form>
	<style>
		textarea {
			width: 50%;
			height: 400px;
			padding: 10px;
			box-sizing: border-box;
			border: solid 2px #1E90FF;
			border-radius: 5px;
			resize: both;
			margin-top: 15px;
		}
	</style>
<textarea id="daily-report-area" >
	</textarea>
<button type="button" class="btn btn-dark-gray btn-mini" id="daily-report-copy">복사</button>
	<div class="sort_area mt30">
		<div class="left">
			<span><c:out value="${op:message('M01363')}"/> : <span class="font_b"><c:out value="${op:numberFormat(total.totalCount)}"/></span><c:out value="${op:message('M00272')}"/> (<c:out value="${op:message('M01364')}"/> : <c:out value="${op:numberFormat(total.saleCount)}"/><c:out value="${op:message('M00272')}"/>, <c:out value="${op:message('M01365')}"/> : <c:out value="${op:numberFormat(total.cancelCount)}"/><c:out value="${op:message('M00272')}"/>)</span> | <span><c:out value="${op:message('M01366')}"/> : <span class="font_b"><c:out value="${op:numberFormat(total.totalPayAmount)}"/></span><c:out value="${op:message('M00814')}"/></span>
		</div>
	</div>

	<div class="board_list">

		<c:set var="viewType" value="day" scope="request" />
		<table class="board_list_table stats ${statisticsParam.displaySubtotal == 'N' ? 'odd-even' : ''}">
			<thead>
				<tr>
					<th colspan="5">총괄현황</th>
				</tr>
				<tr>
					<th colspan="2">구 분</th>
					<th></th> <!-- 날짜 -->
					<th>전일 대비</th>
					<th>전년동일<br>대비(누적)비고</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="2">회원 수</td> <!-- 회원수 -->
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td rowspan="2">기부</td>
					<td>기부건수</td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelDiscountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelShipping)}"/></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">기부금액</td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelDiscountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelShipping)}"/></td>
				</tr>
				<tr>
					<td rowspan="3">답례품</td>
					<td>등록건수</td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelDiscountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelShipping)}"/></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">신청건수</td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelDiscountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelShipping)}"/></td>
				</tr>
				<tr>
					<td style="border-left:1px solid #d5d5d5;">판매액</td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelDiscountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.cancelShipping)}"/></td>
				</tr>
			</tbody>
		</table>

		<table class="board_list_table stats ${statisticsParam.displaySubtotal == 'N' ? 'odd-even' : ''}">
			<thead>
				<tr>
					<th colspan="5">기부현황</th>
				</tr>
				<tr>
					<th rowspan="2">구 분</th>
					<th colspan="2">2024년</th>
					<th colspan="2">2023년</th>
				</tr>
				<tr>
					<th style="border-left:1px solid #d5d5d5;"></th> <!-- 날짜 -->
					<th>누계</th>
					<th><c:out value=""/></th> <!-- 날짜 -->
					<th>누계</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>금액 계</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>온라인</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>오프라인</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>건수 계</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>온라인</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>오프라인</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
			</tbody>
		</table>

		<table class="board_list_table stats ${statisticsParam.displaySubtotal == 'N' ? 'odd-even' : ''}">
			<thead>
				<tr>
					<th colspan="5">답례품현황</th>
				</tr>
				<tr>
					<th rowspan="2">구 분</th>
					<th colspan="2">2024년</th>
					<th colspan="2">2023년</th>
				</tr>
				<tr>
					<th style="border-left:1px solid #d5d5d5;"></th> <!-- 날짜 -->
					<th>누계</th>
					<th><c:out value=""/></th> <!-- 날짜 -->
					<th>누계</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>등록건수</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>신청건수</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
				<tr>
					<td>총 판매액</td>
					<td class="number"><c:out value="${op:numberFormat(total.saleCount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.itemPrice)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
					<td class="number"><c:out value="${op:numberFormat(total.discountAmount)}"/></td>
				</tr>
			</tbody>
		</table>

		<sec:authorize access="hasRole('ROLE_EXCEL')">
			<div class="btn_all">
				<div class="right">
					<a href="/opmanager/shop-statistics/sales/day/excel-download?${fn:escapeXml(queryString)}" class="btn btn-success btn-sm"><span class="glyphicon glyphicon-save"></span><c:out value ="${op:message('M00254')}"/></a> <!-- 엑셀 다운로드 -->
				</div>
			</div>
		</sec:authorize>
	</div>
	<div class="graph_wrap">
	    <div class="bd_gray mb15">
	        <h4 style="margin-bottom: 20px; font-size: 20px; font-weight: bold; text-align:center;">금액별</h4>
	        <div class="graph">
	            <canvas id="amountBarChart"></canvas>
	        </div>
	    </div>
	    <div class="bd_gray mb15">
	        <h4 style="margin-bottom: 20px; font-size: 20px; font-weight: bold; text-align:center;">건수별</h4>
	        <div class="graph">
	            <canvas id="countBarChart"></canvas>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
	});

	function excelDownload() {
		let startDate = document.getElementById('dp28').value;
		Shop.downloadExcelOrder("/opmanager/shop-statistics/dashboard/day/list-excel", startDate, false);
	}

	function drawChart(id, type, options, labelArr, values, explain) {
        var context = document.getElementById(id).getContext('2d');
        var dataSets = [];

        values.forEach(function (ele) {
            var d = {
                lineTension: 0,
                backgroundColor: "#92278F",
                borderColor: "#92278F",
                fill: false
            }
            dataSets.push(Object.assign(d, ele));
        });

        var defaultData = {
            labels : labelArr,
            datasets : dataSets
        };

        var defualtOptions = {
            plugins: {
                legend: {
                    display: true,
                    position : 'bottom'
                },
                title: {
                    display: false
                },
                tooltip : {
                    callbacks : {}
                }
            },
            scales: {
                x: {
                    title : {}
                },
                y: {
                    suggestedMin: 0,
                    title : {}
                }
            }

        };

        if (explain) {

            if (explain.x) {
                defualtOptions.scales.x.title = {
                    display : true,
                    align: 'end',
                    text : '[' + explain.x + ']'
                };

                defualtOptions.plugins.tooltip.callbacks.title = function (tootipItem) {
                    return tootipItem[0].label + '(' + explain.x + ')';
                };

            }

            if (explain.y) {
                defualtOptions.scales.y.title = {
                    display : true,
                    align: 'end',
                    text : '[' + explain.y + ']'
                };

                defualtOptions.plugins.tooltip.callbacks.label = function (tootipItem) {
                    return tootipItem.formattedValue + '(' + explain.y + ')';
                };
            }

        }


        if (options != null) {
            Object.keys(options).forEach(function (key) {

                Object.keys(options[key]).forEach(function (childKey) {
                    Object.assign(defualtOptions[key][childKey], options[key][childKey]);
                });
            });
        }

        var countChart = new Chart(context, {
            type : type,
            data : defaultData,
            options : defualtOptions
        });
   };
</script>
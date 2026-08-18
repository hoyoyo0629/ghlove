<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

<!-- 개발 영역 -->
<div class="location">
    <a href="">통계</a> &gt; <a href="">답례품 구매현황</a> &gt; <a href="">전체</a>
</div>

<h3><span>답례품 구매현황</span></h3>
<form:form modelAttribute="statisticsParam" method="post">

<div class="board_write">
    <table class="board_write_table mb30">
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
                    <div class="flex_box gap-08">
                        <form:select path="year" title="년도" class="wd-150">
                            <c:forEach items="${yyyy}" var="yyyy">
                                <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
                            </c:forEach>
                        </form:select>
                        <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</form:form>



<div class="mainArea loc_details mb30">

    <c:forEach items="${dateList}" var="list" varStatus="i">
    <c:forEach items="${ list.groupStats }" var="item" varStatus="groupIndex">

	<div class="list_box">
	    <ul class="list goods">
	        <li class="bg01 col-md-4">
	            <p class="txt under mb15">주문</p>
	            <div class="text_wrap flex_box">
	                <div class="text_col">
	                    <p class="tit">건수</p>
	                    <p class="txt"><c:out value='${op:numberFormat(item.saleCount)}'/></p>
	                </div>
	                <div class="text_col">
	                    <p class="tit">금액 (p)</p>
	                    <p class="txt"><c:out value='${op:numberFormat(item.saleAmount)}'/></p>
	                </div>
	            </div>
	        </li>
	        <li class="bg01 col-md-4">
	            <p class="txt under mb15">취소/반품</p>
	            <div class="text_wrap flex_box">
	                <div class="text_col">
	                    <p class="tit">건수</p>
	                    <p class="txt"><c:out value='${op:numberFormat(item.cancelCount)}'/></p>
	                </div>
	                <div class="text_col">
	                    <p class="tit">금액 (p)</p>
	                    <p class="txt"><c:out value='${op:numberFormat(item.cancelAmount)}'/></p>
	                </div>
	            </div>
	        </li>
	        <li class="bg01 col-md-4">
	            <p class="txt under mb15">합계</p>
	            <div class="text_wrap flex_box">
	                <div class="text_col">
	                    <p class="tit">건수</p>
	                    <p class="txt"><c:out value='${op:numberFormat(item.sumCount)}'/></p>
	                </div>
	                <div class="text_col">
	                    <p class="tit">금액 (p)</p>
	                    <p class="txt"><c:out value='${op:numberFormat(item.sumAmount)}'/></p>
	                </div>
	            </div>
	        </li>

	    </ul>
	</div>
	</c:forEach>
	</c:forEach>

	<c:if test="${empty dateList}">
	<div class="list_box">
        <ul class="list goods">
            <li class="bg01 col-md-4">
                <p class="txt under mb15">주문</p>
                <div class="text_wrap flex_box">
                    <div class="text_col">
                        <p class="tit">건수</p>
                        <p class="txt">0</p>
                    </div>
                    <div class="text_col">
                        <p class="tit">금액 (p)</p>
                        <p class="txt">0</p>
                    </div>
                </div>
            </li>
            <li class="bg01 col-md-4">
                <p class="txt under mb15">취소/반품</p>
                <div class="text_wrap flex_box">
                    <div class="text_col">
                        <p class="tit">건수</p>
                        <p class="txt">0</p>
                    </div>
                    <div class="text_col">
                        <p class="tit">금액 (p)</p>
                        <p class="txt">0</p>
                    </div>
                </div>
            </li>
            <li class="bg01 col-md-4">
                <p class="txt under mb15">합계</p>
                <div class="text_wrap flex_box">
                    <div class="text_col">
                        <p class="tit">건수</p>
                        <p class="txt">0</p>
                    </div>
                    <div class="text_col">
                        <p class="tit">금액 (p)</p>
                        <p class="txt">0</p>
                    </div>
                </div>
            </li>

        </ul>
    </div>
    </c:if>

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

<!-- // 시스템/운영자 화면 case -->
<!-- <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script> -->
<script type="text/javascript" src="/content/modules/chart.min.js"></script>
<script type="text/javascript">
    $(function () {

        $(".contents_inner > h3:first > span").html("답례품 구매현황");


        statisticsAllByMonth();

    });

//     function init() {

//         //시간대 별 기부금액 추가
//         for (var i = 1; i <= 7; i++) {
//             var d = Common.DateButtonEvent.getDiffDate('day',i,'-');
//             $('#giveDate').append($('<option></option>').attr('value',d.replaceAll('-','')).text(d));
//         }


//         statisticsAllByMonth();
//         statisticsAllByAge();

//         statisticsAllByTime();
//     }


    function statisticsAllByMonth() {
        var monthArr = [01,02,03,04,05,06,07,08,09,10,11,12];
        var onlineArr = [{label : '주문', data : []}, {label : '취소/반품', data : [0,0,0,0,0,0,0,0,0,0,0,0], backgroundColor:'#9B57D3', borderColor:'#9B57D3'}];

        $.post('/opmanager/shop-statistics/sales/all/month', $("#statisticsParam").serialize(), function(resp) {

            if (resp.isSuccess) {

                resp.data.forEach(function (d, i) {
                	for(var i = 0; i < monthArr.length; i++) {
                		if (monthArr[i] == d.groupObject.substring(4,6)) {
                			onlineArr[0].data[i] = d.subSaleAmount;
                			onlineArr[1].data[i] = d.subCancelAmount;
                		}
                	}
                });

                drawChart('amountBarChart', 'bar', {
                	scales:{
		                x:{ //x축값 누적
		                	stacked:false
		                },
		                y:{ //y축값 누적
		                    stacked:false
		                }
		            },
	            }, monthArr, onlineArr, {x : '월', y : 'P'});

                resp.data.forEach(function (d, i) {
                    for(var i = 0; i < monthArr.length; i++) {
                        if (monthArr[i] == d.groupObject.substring(4,6)) {
                            onlineArr[0].data[i] = d.subSaleCount;
                            onlineArr[1].data[i] = d.subCancelCount;
                        }
                    }
                });

                drawChart('countBarChart', 'bar', {
                    scales:{
                        x:{ //x축값 누적
                            stacked:false
                        },
                        y:{ //y축값 누적
                            stacked:false
                        }
                    },
                }, monthArr, onlineArr, {x : '월', y : '건'});


            } else {
                alert(resp.errorMessage);
            }
        });
    }

//     function statisticsAllByAge () {
//         var ageArr = [];
//         var cntArr = [{ label : '건수별', data : [] }];
//         var amtArr = [{ label : '금액별', data : [] }];

//         $.post('/opmanager/give/statistics/all/' + $("#shCntrYear").val() + '/age', {}, function(resp) {

//             if (resp.isSuccess) {
//                 resp.data.forEach(function (d, i) {
//                     ageArr.push(d.cntrAge ? d.cntrAge : 0);
//                     cntArr[0].data.push(d.cntrCnt ? d.cntrCnt : 0);
//                     amtArr[0].data.push(d.cntrAmt && d.cntrAmt > 0 ? Math.floor(d.cntrAmt/1000) : 0);
//                 });

//                 //
//                 drawChart('giveAgeCntBarChart', 'bar', null, ageArr, cntArr, {x : '대', y : '건'});
//                 drawChart('giveAgeAmountBarChart', 'bar', null, ageArr, amtArr, {x : '대', y : '천원'});


//             } else {
//                 alert(resp.errorMessage);
//             }
//         });
//     }

//     function statisticsAllByTime() {
//         var timeArr = [];
//         var amtArr = [{ label : '시간별', data : []}];

//         $.post('/opmanager/give/statistics/all/' + $("#giveDate").val() + '/hour', {}, function(resp) {

//             if (resp.isSuccess) {
//                 resp.data.forEach(function (d, i) {
//                     timeArr.push(d.cntrHour ? d.cntrHour : 0);
//                     amtArr[0].data.push(d.cntrAmt && d.cntrAmt > 0 ? Math.floor(d.cntrAmt/1000) : 0);
//                 });

//                 var options = {
//                     plugins: {
//                         tooltip : {
//                             callbacks : {
//                                 title : function (tootipItem) {
//                                     var time = tootipItem[0].label < 10 ? '0' + tootipItem[0].label : tootipItem[0].label;
//                                     return tootipItem[0].label + "시 - " + time + ':00:00 ~ ' + time + ':59:59';
//                                 },
//                                 label : function (tootipItem) {
//                                     return tootipItem.formattedValue + '(천원)';
//                                 }
//                             }
//                         }
//                     }
//                 }


//                 var p = $('#giveCountLineByTimeChart').parent('div');
//                 var ele = $('#giveCountLineByTimeChart').clone();
//                 p.empty();
//                 p.append(ele);

//                 drawChart('giveCountLineByTimeChart', 'line', options, timeArr, amtArr, {x : '시(h)', y : '천원'});


//             } else {
//                 alert(resp.errorMessage);
//             }

//         });

//     }


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
                    // suggestedMax: 100000,
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

   window.onpageshow = function(event) {
       if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
           // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
           window.location.reload();
       }
   }



</script>

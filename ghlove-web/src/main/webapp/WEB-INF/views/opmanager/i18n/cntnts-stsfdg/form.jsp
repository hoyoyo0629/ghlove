<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<style>
    .hourGraph canvas {
        position: absolute; width: 100%; top: 0;
    }
</style>
		<h3><span>만족도 조사</span></h3>

        <div class="btn_all btn_right mb15">
            <div class="flex_box gap-08">
                <button type="button" class="btn btn-default btn-mini" onclick="javascript:location.href='/opmanager/cntnts-stsfdg/list'">목록</button>
            </div>
        </div>

        <form:form modelAttribute="searchParam" method="post">
        	<form:hidden path="menuUrl" />

            <div class="board_write mb30">
                <table class="board_write_table" summary="">
                    <colgroup>
                        <col style="width:220px;">
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label col-md-2">년도</td>
                            <td>
                                <div class="flex_box gap-08 item-center">
                                    <form:select path="searchYear" title="년도" class="wd-150">
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

            <div class="mainArea loc_details mb30">
                <div class="list_box">
                    <ul class="list">
                        <li class="bg01 col-md-4">
                            <p class="tit">URL</p>
                            <p class="s-txt"><a href="${op:property('saleson.url.frontend')}${fn:escapeXml(sum.menuUrl)}" target="_black"><c:out value="${sum.menuUrl}"/></a></p>
                        </li>
                        <li class="bd02 col-md-2">
                            <p class="tit">매우 만족</p>
                            <p class="txt"><c:out value="${sum.stsfdg4 }"/></p>
                        </li>
                        <li class="bd02 col-md-2">
                            <p class="tit">만족</p>
                            <p class="txt"><c:out value="${sum.stsfdg3 }"/></p>
                        </li>
                        <li class="bd02 col-md-2">
                            <p class="tit">불만족</p>
                            <p class="txt"><c:out value="${sum.stsfdg2 }"/></p>
                        </li>
                        <li class="bd02 col-md-2">
                            <p class="tit">매우 불만족</p>
                            <p class="txt"><c:out value="${sum.stsfdg1 }"/></p>
                        </li>
                        <li class="bd02 col-md-2">
                            <p class="tit">합계</p>
                            <p class="txt"><c:out value="${sum.sumCount }"/></p>
                        </li>
                    </ul>
                </div>
                <div class="graph_wrap all">
                    <div class="bd_gray mb15">
                        <div class="graph">
                            <canvas id="cntntsStsfdgChart" style="max-height: 350px;"></canvas>
                        </div>
                    </div>
                </div>
            </div>
</form:form>

<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
$(function(){
    detail();
})

function detail() {

    var params = {
        searchYear : $("#searchYear").val(),
        menuUrl : $("#menuUrl").val()
    }

    $.post('/opmanager/cntnts-stsfdg/statistics', params, function(resp) {

        if (resp.isSuccess) {

            var monthArr = [];
            var stsfdgArr = [
                {
                    lineTension: 0,
                    backgroundColor: "#004CCE",
                    borderColor: "#004CCE",
                    fill: false,
                    label : '매우 만족',
                    data : []
                },
                {
                    lineTension: 0,
                    backgroundColor: "#09C2C7",
                    borderColor: "#09C2C7",
                    fill: false,
                    label : '만족',
                    data : []
                },
                {
                    lineTension: 0,
                    backgroundColor: "#00215A",
                    borderColor: "#00215A",
                    fill: false,
                    label : '불만족',
                    data : []
                },
                {
                    lineTension: 0,
                    backgroundColor: "#6e90cd",
                    borderColor: "#00215A",
                    fill: false,
                    label : '매우 불만족',
                    data : []
                }
            ];

            resp.data.forEach(function (d, i) {
                monthArr.push(d.frstRegistPnttm ? d.frstRegistPnttm : 0);
                stsfdgArr[0].data.push(d.stsfdg4 ? d.stsfdg4 : 0);
                stsfdgArr[1].data.push(d.stsfdg3 ? d.stsfdg3 : 0);
                stsfdgArr[2].data.push(d.stsfdg2 ? d.stsfdg2 : 0);
                stsfdgArr[3].data.push(d.stsfdg1 ? d.stsfdg1 : 0);
            });

            var data = {
                labels : monthArr,
                datasets : stsfdgArr
            };

            var options = {
                scales: {
                    x: {
                        stacked : true
                    },
                    y: {
                        stacked : true
                    }
                }
            };

            ChartCommon.drawChart('cntntsStsfdgChart', 'bar', options, monthArr, stsfdgArr, {x : '월', y : '건'});



        } else {
            alert(resp.errorMessage);
        }

    });
}


</script>


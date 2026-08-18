<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>기부금 운영현황</span></h3>


<div class="mainArea loc_details mb30">
    <div class="graph_wrap">
        <div class="bd_gray mb15">
            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">금액별</p>
            <div class="graph">
                <canvas id="giveCountLineChart"></canvas>
            </div>
        </div>
        <div class="bd_gray mb15">
            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">건수별</p>
            <div class="graph">
                <canvas id="giveCountBarChart"></canvas>
            </div>
        </div>
    </div>
</div>


<!-- 결과 영역 -->
<div class="board_list mt40">
    <table class="board_list_table" id="operateTable">
        <colgroup>
            <col style="width:100px;">
            <c:forEach  var="i" begin="1" end="${codeList.size() * 2}">
                <c:choose>
                    <c:when test="${i % 2 != 0}">
                        <col style="width:150px;">
                    </c:when>
                    <c:otherwise>
                        <col style="width:100px;">
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <col style="width:150px;">
            <col style="width:100px;">
        </colgroup>
        <thead>
            <tr>
                <th scope="col" rowspan="2">년도</th>
                <c:forEach items="${codeList}" var="list" varStatus="i">
                    <th scope="col" colspan="2" class="border_left"><c:out value="${list.label}" /></th>
                </c:forEach>
                <th scope="col" colspan="2" class="border_left">합계</th>
            </tr>
            <tr>
                <c:forEach  var="i" begin="1" end="${codeList.size() * 2}">
                    <c:choose>
                        <c:when test="${i % 2 != 0}">
                            <th scope="col" class="border_left">사용금액</th>
                        </c:when>
                        <c:otherwise>
                            <th scope="col" class="border_left">건수</th>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <th scope="col" class="border_left">사용금액</th>
                <th scope="col" class="border_left">건수</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
        <tfoot>
        </tfoot>
    </table>
    <c:if test="${adminRole ne 'LOC'}">
        <div class="btn_all">
            <div class="flex_box juc-sbt">
                <button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/give/statistics/operate';">목록</button>
            </div>
        </div>
    </c:if>
</div>



<!-- // 시스템/운영자 화면 case -->
<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
$(function() {
    $(".contents_inner").find("div.location a").removeClass("on");
    $(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');

    detail();
});


function detail() {

    $.post('/opmanager/give/statistics/operate/${fn:escapeXml(searchParam.shLocgovCode)}/detail', {}, function(resp) {

        if (resp.isSuccess) {
            var colorArr = ['#004CCE','#00215A','#09C2C7','#6e90cd'];
            var codeList = resp.data.codeList;
            var list = resp.data.list;
            var html = {body : '', footer : ''};
            var yearArr = [];
            var amtArr = [];
            var cntArr = [];
            for (var i in codeList) {
                var c = codeList[i];
                amtArr.push({
                    label : c.label,
                    id : c.id,
                    data : [],
                    backgroundColor : colorArr[i],
                    borderColor : colorArr[i]
                });
                cntArr.push({
                    label : c.label,
                    id : c.id,
                    data : [],
                    backgroundColor : colorArr[i],
                    borderColor : colorArr[i]
                });
            };

            if (list.length <= 0) {
                html.body = '<tr><td colspan="' + ((codeList.length * 2) + 3) + '"><div>데이터가 없습니다.</div></td></tr>';

            }

            for (var i = 0; i < list.length; i++) {

                var d = list[i];
                var chart = list[list.length - 1 - i];
                if (chart.cntrYear != '0') {
                    yearArr.push(chart.cntrYear);
                    for (var j in amtArr) {
                        amtArr[j].data.push(chart['amt' + amtArr[j].id]);
                        cntArr[j].data.push(chart['cnt' + cntArr[j].id]);
                    }
                }

                html = drawTable(html, codeList, d);



            }

            $("#operateTable tbody").append(html.body);
            $("#operateTable tfoot").append(html.footer);

            ChartCommon.drawChart('giveCountLineChart', 'line', null, yearArr, amtArr, {x : '년', y : '천원'});
            ChartCommon.drawChart('giveCountBarChart', 'line', null, yearArr, cntArr, {x : '년', y : '건'});



        } else {
            alert(resp.errorMessage);
            return false;
        }

    });
}

function drawTable(html, codeList, data) {
    var txt = 'body';
    var className = '';

    if (data.cntrYear == '0') {
        txt = 'footer';
        className = 'class="border_left"';
        data.cntrYear = '합계';
    }

    html[txt] += '<tr style="background:#fff;">';
    html[txt] += '<td><div>' + data.cntrYear + '</div></td>';

    for (var j in codeList) {
        var c = codeList[j];
        html[txt] += '<td ' + className + '><div>' + Common.numberFormat(data['amt' + c.id]) + '</div></td>';
        html[txt] += '<td ' + className + '><div>' + Common.numberFormat(data['cnt' + c.id]) + '</div></td>';
    }

    html[txt] += '<td ' + className + '><div>' + Common.numberFormat(data.expndtrSum) + '</div></td>';
    html[txt] += '<td ' + className + '><div>' + Common.numberFormat(data.expndtrCnt) + '</div></td>';


    html[txt] += '</ tr>';

    return html;
}

</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

<style>
    .hourGraph canvas {
        position: absolute; width: 100%; top: 0;
    }
</style>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>관심 지자체</span></h3>
<c:if test="${adminRole ne 'LOC'}">
    <div class="btn_all btn_right mb15">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/statistics/locgov/like'">목록</button>
        </div>
    </div>
</c:if>

<form:form modelAttribute="searchParam" method="post">
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
                            <form:select path="shCntrYear" title="년도" class="wd-150">
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
    <div class="list_box">
        <ul class="list">
            <li class="bg01 col-md-6">
                <p class="tit">총 등록수 <span class="s-txt">(현재기준)</span></p>
                <p class="txt"><c:out value="${op:numberFormat(total.likeCnt)}" /></p>
            </li>
            <li class="bd02 col-md-6">
                <p class="tit">년도별 등록수</p>
                <p class="txt"><c:out value="${op:numberFormat(total.yearLikeCnt)}" /></p>
            </li>
        </ul>
    </div>x
    <div class="graph_wrap all">
        <div class="bd_gray mb15">
            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 등록건수</p>
            <div class="graph">
                <canvas id="locgovLikeMonthChart" style="max-height: 300px;"></canvas>
            </div>
        </div>
    </div>
</div>







<!-- // 시스템/운영자 화면 case -->
<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
    $(function () {

        $(".contents_inner > h3:first > span").html("관심 지자체");
        $(".contents_inner").find("div.location a").removeClass("on");
	    $(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');


        init();

    });

    function init() {

        statisticsLocgovLikeByMonth();
        //statisticsAllByAge();

    }


    function statisticsLocgovLikeByMonth() {
        var monthArr = [];
        var likeArr = [{ label : '건수별', data : [], backgroundColor:'#004CCE', borderColor:'#004CCE' }];

        $.post('/opmanager/statistics/locgov/like/${fn:escapeXml(searchParam.shLocgovCode)}/' + $("#shCntrYear").val(), {}, function(resp) {

            if (resp.isSuccess) {
                resp.data.forEach(function (d, i) {
                    monthArr.push(d.cntrMonth ? d.cntrMonth : 0);
                    likeArr[0].data.push(d.likeCnt ? d.likeCnt : 0);
                });

                ChartCommon.drawChart('locgovLikeMonthChart', 'bar', null, monthArr, likeArr, {x : '월', y : '건'});

            } else {
                alert(resp.errorMessage);
            }
        });
    }





</script>

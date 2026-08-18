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

<h3><span></span></h3>
<div class="btn_all btn_right">
    <div class="flex_box gap-08 md15">
        <c:if test="${adminRole ne 'LOC'}">
			<c:choose>
				<c:when test="${searchParam.foreignerFlag == 'Y'}">
            		<button type="button" class="btn btn-default btn-mini" style="margin-left : auto" onClick="location.href='/opmanager/give/statistics/foreigner/locgov'">목록</button>
				</c:when>
				<c:otherwise>
            		<button type="button" class="btn btn-default btn-mini" style="margin-left : auto" onClick="location.href='/opmanager/give/statistics/locgov'">목록</button>
				</c:otherwise>
			</c:choose>
        </c:if>
    </div>
</div>
<form:form modelAttribute="searchParam" action="/opmanager/give/statistics/${searchParam.foreignerFlag == 'Y' ? 'foreigner/' : ''}locgov/${searchParam.shLocgovCode}" method="post">
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
                                    <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}" />
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
            <li class="bg01 col-md-4">
				<c:choose>
					<c:when test="${searchParam.foreignerFlag == 'Y'}">
                		<p class="tit">외국인 총 기부금액</p>
					</c:when>
					<c:otherwise>
                		<p class="tit">총 기부금액</p>
					</c:otherwise>
				</c:choose>
                <p class="txt"><c:out value="${op:numberFormat(total.cntrAmt)}" /></p>
            </li>
            <li class="bd02 col-md-4">
				<c:choose>
					<c:when test="${searchParam.foreignerFlag == 'Y'}">
                		<p class="tit">외국인 총 기부인원 <span class="s-txt">(중복제외)</span></p>
					</c:when>
					<c:otherwise>
                		<p class="tit">총 기부인원 <span class="s-txt">(중복제외)</span></p>
					</c:otherwise>
				</c:choose>
                <p class="txt"><c:out value="${op:numberFormat(total.cntrPerson)}" /></p>
            </li>
            <li class="bd02 col-md-4">
				<c:choose>
					<c:when test="${searchParam.foreignerFlag == 'Y'}">
                		<p class="tit">외국인 총 기부건수</p>
					</c:when>
					<c:otherwise>
                		<p class="tit">총 기부건수</p>
					</c:otherwise>
				</c:choose>
                <p class="txt"><c:out value="${op:numberFormat(total.cntrCnt)}" /></p>
            </li>
        </ul>
    </div>

    <div class="graph_wrap">
        <div class="bd_gray mb15">
			<c:choose>
				<c:when test="${searchParam.foreignerFlag == 'Y'}">
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">외국인 월별 기부건수</p>
				</c:when>
				<c:otherwise>
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 기부건수</p>
				</c:otherwise>
			</c:choose>
            <div class="graph">
                <canvas id="giveCountLineChart"></canvas>
            </div>
        </div>
        <div class="bd_gray mb15">
			<c:choose>
				<c:when test="${searchParam.foreignerFlag == 'Y'}">
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">외국인 월별 기부건수</p>
				</c:when>
				<c:otherwise>
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 기부건수</p>
				</c:otherwise>
			</c:choose>
            <div class="graph">
                <canvas id="giveCountBarChart"></canvas>
            </div>
        </div>
        <div class="bd_gray mb15">
			<c:choose>
				<c:when test="${searchParam.foreignerFlag == 'Y'}">
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">외국인 월별 기부인원</p>
				</c:when>
				<c:otherwise>
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 기부인원</p>
				</c:otherwise>
			</c:choose>
            <div class="graph">
                <canvas id="givePersonLineChart"></canvas>
            </div>
        </div>
        <div class="bd_gray mb15">
			<c:choose>
				<c:when test="${searchParam.foreignerFlag == 'Y'}">
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">외국인 월별 기부금액</p>
				</c:when>
				<c:otherwise>
            		<p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 기부금액</p>
				</c:otherwise>
			</c:choose>
            <div class="graph">
                <canvas id="giveAmountLineChart"></canvas>
            </div>
        </div>
        <%-- <div class="bd_gray mb15">
            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">나이대별 기부건수</p>
            <div class="graph">
                <canvas id="giveAgeCntBarChart"></canvas>
            </div>
        </div>
        <div class="bd_gray mb15">
            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">나이대별 기부금액</p>
            <div class="graph">
                <canvas id="giveAgeAmountBarChart"></canvas>
            </div>
        </div> --%>
    </div>
</div>







<!-- // 시스템/운영자 화면 case -->
<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
    $(function () {
		<c:choose>
			<c:when test="${searchParam.foreignerFlag == 'Y'}">
				$(".contents_inner > h3:first > span").html("외국인 기부 현황 (${fn:escapeXml(locgovNm)})");
			</c:when>
			<c:otherwise>
	        	$(".contents_inner > h3:first > span").html("기부금 모금현황(${fn:escapeXml(locgovNm)})");
			</c:otherwise>
		</c:choose>
        init();
    });

    function init() {

        statisticsAllByMonth();
        //statisticsAllByAge();

    }


    function statisticsAllByMonth() {
        var monthArr = [];
        var cntArr = [{ label : '건수별', data : [], backgroundColor:'#004CCE', borderColor:'#004CCE' }];
        var personArr = [{ label : '인원별', data : [], backgroundColor:'#00215A', borderColor:'#00215A' }];
        var amtArr = [{ label : '금액별', data : [], backgroundColor:'#09C2C7', borderColor:'#09C2C7' }];
        var onlineArr = [{label : '온라인', data : [], backgroundColor:'#00215A', borderColor:'#00215A'}, {label : '오프라인', data : [], backgroundColor:'#09C2C7', borderColor:'#09C2C7'}];

        let url = '';
		<c:choose>
			<c:when test="${searchParam.foreignerFlag == 'Y'}">
        		url = '/opmanager/give/statistics/foreigner/locgov/${fn:escapeXml(searchParam.shLocgovCode)}/';
			</c:when>
			<c:otherwise>
				url = '/opmanager/give/statistics/locgov/${fn:escapeXml(searchParam.shLocgovCode)}/';
			</c:otherwise>
		</c:choose>

        $.post(url + $("#shCntrYear").val() + '/month', {}, function(resp) {

            if (resp.isSuccess) {
                resp.data.forEach(function (d, i) {
                    monthArr.push(d.cntrMonth ? d.cntrMonth : 0);
                    cntArr[0].data.push(d.cntrCnt ? d.cntrCnt : 0);
                    personArr[0].data.push(d.cntrPerson ? d.cntrPerson : 0);
                    amtArr[0].data.push(d.cntrAmt && d.cntrAmt > 0 ? Math.floor(d.cntrAmt/1000) : 0);

                    onlineArr[0].data.push(d.cntrOnline ? d.cntrOnline : 0);
                    onlineArr[1].data.push(d.cntrOffline ? d.cntrOffline : 0);
                });

                //
                ChartCommon.drawChart('giveCountLineChart', 'line', null, monthArr, cntArr, {x : '월', y : '건'});
                ChartCommon.drawChart('givePersonLineChart', 'line', null, monthArr, personArr, {x : '월', y : '명'});
                ChartCommon.drawChart('giveAmountLineChart', 'line', null, monthArr, amtArr, {x : '월', y : '천원'});

                ChartCommon.drawChart('giveCountBarChart', 'bar', {scales:{
                x:{ //x축값 누적
                    stacked:true
                },
                y:{ //y축값 누적
                    stacked:true
                }
            },
            }, monthArr, onlineArr, {x : '월', y : '건'});


            } else {
                alert(resp.errorMessage);
            }
        });
    }

    function statisticsAllByAge () {
        var ageArr = [];
        var cntArr = [{ label : '건수별', data : [], backgroundColor:'#004CCE', borderColor:'#004CCE' }];
        var amtArr = [{ label : '금액별', data : [], backgroundColor:'#09C2C7', borderColor:'#09C2C7' }];

        let url = '';
		<c:choose>
			<c:when test="${searchParam.foreignerFlag == 'Y'}">
				url = '/opmanager/give/statistics/foreigner/locgov/${fn:escapeXml(searchParam.shLocgovCode)}/';
			</c:when>
			<c:otherwise>
	        	url = '/opmanager/give/statistics/locgov/${fn:escapeXml(searchParam.shLocgovCode)}/';
			</c:otherwise>
		</c:choose>

        $.post( + $("#shCntrYear").val() + '/age', {}, function(resp) {

            if (resp.isSuccess) {
                resp.data.forEach(function (d, i) {
                    ageArr.push(d.cntrAge ? d.cntrAge : 0);
                    cntArr[0].data.push(d.cntrCnt ? d.cntrCnt : 0);
                    amtArr[0].data.push(d.cntrAmt && d.cntrAmt > 0 ? (d.cntrAmt < 1000 ? d.cntrAmt/1000 : (Math.floor(d.cntrAmt/1000))) : 0);
                });

                //
                ChartCommon.drawChart('giveAgeCntBarChart', 'bar', null, ageArr, cntArr, {x : '대', y : '건'});
                ChartCommon.drawChart('giveAgeAmountBarChart', 'bar', null, ageArr, amtArr, {x : '대', y : '천원'});


            } else {
                alert(resp.errorMessage);
            }
        });
    }


</script>

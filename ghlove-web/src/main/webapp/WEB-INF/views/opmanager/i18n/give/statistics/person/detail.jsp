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

<h3><span>기부인원통계</span></h3>
<form:form modelAttribute="searchParam" method="post">
  <div class="board_write">
    <table class="board_write_table">
        <colgroup>
            <col style="width:220px;">
            <col>
        </colgroup>
        <tbody>
            <tr>
                <td class="label">기간</td>
                <td>
                    <div class="flex_box gap-08 item-center">
                        <form:select path="fromYear" title="년도" class="wd-150">
                            <c:forEach items="${yyyy}" var="yyyy">
                              <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
                            </c:forEach>
                        </form:select>
                        <span>~</span>
                        <form:select path="toYear" title="년도" class="wd-150">
                            <c:forEach items="${yyyy}" var="yyyy">
                              <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
                            </c:forEach>
                        </form:select>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>

    <div class="btn_all btn_right">
        <div class="flex_box juc-sbt">
            <div class="flex_box gap-08">
                <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
                <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel('${fn:escapeXml(searchParam.shLocgovCode)}')">엑셀 다운로드</button>
            </div>
            <c:if test="${adminRole ne 'LOC'}">
                <div>
                    <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/give/statistics/person'">목록</button>
                </div>
            </c:if>
        </div>
    </div>
  </div>

  <h3 class="mt50 fs24"><span>월별 기부인원통계</span></h3>
  <div id="month_stats" class="chart_horizontal chart_detail">
    <ul>
        <c:forEach items="${list}" var="list" varStatus="i">
            <li>
                <p class="name"><a href="javascript:detail('${fn:escapeXml(searchParam.shLocgovCode)}','${fn:escapeXml(list.cntrMonth)}')"><c:out value="${fn:substring(list.cntrMonth,0,4)}년 ${fn:substring(list.cntrMonth,4,6)}월" /></a></p>
                <div class="bar_wrap">
                    <p class="bar yellow" style="width: ${fn:escapeXml(list.percent)}%">
                        <span class="number" title=""><c:out value="${op:numberFormat(list.givePersons)}" /></span>
                        <span class="number number_over" title=""></span>
                        <span class="tip"></span>
                    </p>
                </div>
            </li>
        </c:forEach>

    </ul>
  </div>
</form:form>

<h3 id="day_stats_title" class="mt50 fs24"><span>2022년 1월 일별 기부인원통계</span></h3>
<div id="day_stats">
  <div class="chart_vertical daily">
      <table>
          <tr>
          </tr>
      </table>
  </div>
</div>

<!-- // 시스템/운영자 화면 case -->

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">
    $(function() {

	    $(".contents_inner").find("div.location a").removeClass("on");
	    $(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');

        $("#month_stats ul li:first a").get(0).click();
    });


    function detail(locgovCode, cntrMonth) {
        var params = {
            cntrMonth : cntrMonth
        }

        $.post('/opmanager/give/statistics/person/' + locgovCode, params, function(resp) {

            if (resp.isSuccess) {
                var html = "";

                for (var i in resp.data) {
                    var ele = resp.data[i];
                    html += '<td>';
                    html += '   <div>';
                    html += '       <p class="name">' + ele.cntrDate + '</p>';
                    html += '       <div class="bar_wrap">';
                    html += '           <p class="bar" style="height: ' + ele.percent + '%">';
                    html += '               <span class="number">' + ele.givePersons + '</span>';
                    html += '           </p>';
                    html += '       </div>';
                    html += '   </div>';
                    html += '</td>';
                }

                $("#day_stats_title").html('<span>' + cntrMonth.substring(0,4) + "년 " + Number(cntrMonth.substring(4,6)) + '월 일별 기부인원통계</span>');
                $("#day_stats table tr").html(html);
                //resp.data.forEach(e => {
                //    console.log(e.percent);
                //});

            } else {
                alert(resp.errorMessage);
            }

        });
    }

    function downloadExcel(locgovCode) {
        Shop.downloadExcelOrder("/opmanager/give/statistics/person/" + locgovCode + "/excel", $('#searchParam').serialize(), true);
    }

</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"     uri="http://www.springframework.org/security/tags"%>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>일자별 통계</span></h3>
<form:form modelAttribute="searchParam" method="post">
  <div class="board_write">
    <table class="board_write_table" summary="">
        <colgroup>
            <col style="width:220px;">
        </colgroup>
        <tbody>
            <tr>
               <td class="label">기간</td>
               <td>
                   <div>
                        <span class="datepicker">
                            <form:input path="shCntrDeStart" cssClass="datepicker optional " title="${op:message('M00507')}" />
                        </span>
                        <span class="wave">~</span>
                        <span class="datepicker">
                            <form:input path="shCntrDeEnd" cssClass="datepicker optional " title="${op:message('M00507')}" />
                        </span>
                        <span class="day_btns">
                        	<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
							<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
							<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
							<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
							<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
                        </span>
                   </div>
               </td>
            </tr>
        </tbody>
    </table>

    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/give/statistics/date/${fn:escapeXml(searchParam.shLocgovCode)}';">초기화</button>
            <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
        </div>
    </div>

  </div>

  <!-- 결과 영역 -->
  <div class="board_list mt40">
    <table class="board_list_table">
        <colgroup>
            <col style="width:200px;">
            <col style="width:200px;">
            <col style="width:200px;">
            <col style="width:200px;">
            <col style="width:200px;">
        </colgroup>
        <thead>
            <tr>
                <th scope="col">일자</th>
                <th scope="col">건수</th>
                <th scope="col">기부금액</th>
                <th scope="col">기부인원</th>
                <th scope="col">발생포인트</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="list" varStatus="i">
                <tr style="background:#fff;">
                    <td>
                        <div>
                            <a href="javascript:openPopup('${fn:escapeXml(searchParam.shLocgovCode)}', '${fn:escapeXml(list.cntrDe)}')"><c:out value='${op:date(list.cntrDe)}'/></a>
                        </div>
                    </td>
                    <td>
                        <div><c:out value='${op:numberFormat(list.giveCnt)}'/></div>
                    </td>
                    <td>
                        <div><c:out value='${op:numberFormat(list.giveAmt)}'/></div>
                    </td>
                    <td>
                        <div><c:out value='${op:numberFormat(list.givePersons)}'/></div>
                    </td>
                    <td>
                        <div><c:out value='${op:numberFormat(list.givePoint)}'/></div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
        <tfoot>
            <tr>
                <td>
                    <div>합계</div>
                </td>
                <td>
                    <div><c:out value='${op:numberFormat(total.giveCnt)}'/></div>
                </td>
                <td>
                    <div><c:out value='${op:numberFormat(total.giveAmt)}'/></div>
                </td>
                <td>
                    <div><c:out value='${op:numberFormat(total.givePersons)}'/></div>
                </td>
                <td>
                    <div><c:out value='${op:numberFormat(total.givePoint)}'/></div>
                </td>
            </tr>
        </tfoot>
    </table>

    <div class="btn_all">
        <div class="flex_box juc-sbt">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel('${fn:escapeXml(searchParam.shLocgovCode)}')">엑셀</button>
            <c:if test="${adminRole ne 'LOC'}">
                <button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/give/statistics/date';">목록</button>
            </c:if>
        </div>
    </div>
</div>
<!-- // 결과 영역 -->
</form:form>

<!-- // 시스템/운영자 화면 case -->

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">
    $(function () {
        $(".contents_inner").find("div.location a").removeClass("on");
	    $(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');

	    Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="shCntrDeStart"]' , 'input[name="shCntrDeEnd"]');
		EventHandler.calendarStartDateAndEndDateVaild();
    });

    function downloadExcel(locgovCode) {
        Shop.downloadExcelOrder("/opmanager/give/statistics/date/" + locgovCode + "/excel", $('#searchParam').serialize(), true);
    }

    function openPopup(locgovCode, date) {
        Common.popup(url("/opmanager/give/statistics/date/" + locgovCode + "/popup/" + date), 'dateDetail', 800, 500);
    }

</script>

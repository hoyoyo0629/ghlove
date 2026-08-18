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

<h3><span>인원별통계</span></h3>
<!-- 조회 영역 -->
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

        <div class="btn_all">
            <div class="flex_box juc-sbt">
                <div class="flex_box gap-08">
                    <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
                    <button type="button" class="btn btn-dark-gray btn-mini">엑셀 다운로드</button>
                </div>
                <c:if test="${adminRole ne 'LOC'}">
                    <div>
                        <button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/give/statistics/personal'">목록</button>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <!-- // 조회 영역 -->

    <div class="count_title mt-40">
        <h5>총 <c:out value="${list.size()}" />건</h5>
        <span>
            <form:select path="itemsOrder" title="정렬" class="wd-120" onchange="$('form#searchParam').submit();">
                <form:option value="" label="정렬"/>
                <form:option value="cnt" label="기부건수"/>
                <form:option value="amt" label="기부금액" />
                <form:option value="point" label="발생포인트" />
          </form:select>
        </span>
    </div>
</form:form>


<!-- 결과 영역 -->
<div class="board_list">
    <table class="board_list_table">
        <colgroup>
            <col style="width:200px;">
            <col style="width:200px;">
            <col style="width:100px;">
            <col style="width:200px;">
            <col style="width:200px;">
        </colgroup>
        <thead>
            <tr>
                <th scope="col">아이디</th>
                <th scope="col">이름</th>
                <th scope="col">건수</th>
                <th scope="col">기부금액</th>
                <th scope="col">발생포인트</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="list" varStatus="i" >
                <tr style="background:#fff;">
                    <td>
                        <div>
                            <a href="javascript:openPopup('${fn:escapeXml(searchParam.shLocgovCode)}', '${fn:escapeXml(list.userId)}')"><c:out value="${list.loginId}" /></a>
                        </div>
                    </td>
                    <td>
                        <div><c:out value="${list.userName}" /></div>
                    </td>
                    <td>
                        <div><c:out value="${op:numberFormat(list.giveCnt)}" /></div>
                    </td>
                    <td>
                        <div><c:out value="${op:numberFormat(list.giveAmt)}" /></div>
                    </td>
                    <td>
                        <div><c:out value="${op:numberFormat(list.givePoint)}" /></div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
        <tfoot>
            <tr>
                <td colspan="2">
                    <div>합계</div>
                </td>
                <td>
                    <div><c:out value="${op:numberFormat(total.giveCnt)}" /></div>
                </td>
                <td>
                    <div><c:out value="${op:numberFormat(total.giveAmt)}" /></div>
                </td>
                <td>
                    <div><c:out value="${op:numberFormat(total.givePoint)}" /></div>
                </td>
            </tr>
        </tfoot>
    </table>
</div>
<!-- // 결과 영역 -->

<!-- // 시스템/운영자 화면 case -->

<script type="text/javascript">

    $(function () {
        $(".contents_inner").find("div.location a").removeClass("on");
	    $(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');
    })

    function openPopup(locgovCode, userId) {
        Common.popup(url("/opmanager/give/statistics/personal/" + locgovCode + "/popup/" + userId), 'personalDetail', 800, 500);
    }

</script>

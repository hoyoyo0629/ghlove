<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>


<div class="location">
    <a href="#"></a>&gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<!-- 2022.06.23 -->
<h3>결제현황</h3>
<form:form modelAttribute="payInfoParam" method="post">
    <div class="board_write">
        <table class="board_write_table">
            <caption>결제현황</caption>
            <colgroup>
                <col style="width:150px;" />
                <col style="width:*;" />
            </colgroup>
            <tbody>
            <tr>
                <td class="label">키워드검색</td> <!-- 검색구분 -->
                <td>
                    <div>
                        <form:select path="where" title="${op:message('M00468')}"> <!-- 키워드선택 -->
                            <form:option value="payUserName" label="결제자" />
                        </form:select>
                        <form:input type="text" path="query" class="three" title="${op:message('M00021')} " />  <!-- 검색어 입력 -->
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label">결제방법</td>
                <td>
                    <div>
                        <form:radiobutton path="approvalType" value="" checked="checked" label="전체" />
                        <form:radiobutton path="approvalType" value="bank" label="온라인입금" />
                        <form:radiobutton path="approvalType" value="card" label="신용카드" />
                        <form:radiobutton path="approvalType" value="vbank" label="가상계좌" />
                        <form:radiobutton path="approvalType" value="point" label="포인트" />
                    </div>
                </td>
            </tr>
            <tr>
                <div>
                    <td class="label">일자검색</td> <!-- 작성일 -->
                    <td>
                        <div>
                            <form:radiobutton path="searchDate" value="orderDate" checked="" label="주문일"/>
                            <form:radiobutton path="searchDate" value="payDate" checked="" label="결제일"/>
                            &nbsp;&nbsp;
                            <span class="datepicker"><form:input type="text" path="searchStartDate" class="datepicker _number" title="${op:message('M00507')}" /></span> <!-- 시작일 -->
                            <span class="wave">~</span>
                            <span class="datepicker"><form:input type="text" path="searchEndDate" class="datepicker _number" title="${op:message('M00509')}" /></span> <!-- 종료일 -->
                            <span class="day_btns">
								<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
								<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
								<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
								<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
								<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
							</span>
                        </div>
                    </td>
                </div>
            </tr>
            </tbody>
        </table>
    </div> <!-- // board_write -->

    <!-- 버튼시작 -->
    <div class="btn_all">
        <div class="btn_left">
            <button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/pay-info/list'"><span><c:out value="${op:message('M00047')}"/></span></button> <!-- 초기화 -->
            <a href="javascript:downloadExcel()" class="btn btn-success btn-sm"><span class="glyphicon glyphicon-save" aria-hidden="true"></span> 엑셀 다운로드</a>
        </div>
        <div class="btn_right">
            <button type="submit" class="btn btn-dark-gray btn-sm"><span><c:out value="${op:message('M00048')}"/></span></button><!-- 검색 -->
        </div>
    </div>
    <!-- 버튼 끝-->

    <div class="count_title mt20">
        <h5>전체 :<c:out value="${payInfoCount}"/>건 조회</h5>
        <span>출력수
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
                                 onchange="$('form#payInfoParam').submit();"> <!-- 화면 출력수 -->
                        <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
                        <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
                        <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
                        <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
                    </form:select>
				</span>
    </div>
</form:form>

<form action="/opmanager/pay-info/list" method="post" id="listForm">
    <input type="hidden" value="${fn:escapeXml(list.qnaId)}">
    <div class="board_write">
        <table class="board_list_table" summary="결제현황 리스트">
            <caption>결제현황리스트</caption>
            <colgroup>
                <col style="width:5%;" />
                <col style="width:15%;" />
                <col style="width:15%;" />
                <col style="width:10%;" />
                <col style="width:25%;" />
                <col style="width:10%;" />
            </colgroup>
            <thead>
            <tr>
                <th scope="col">순번</th> <!-- 체크박스 -->
                <th scope="col">결제자</th>
                <th scope="col">주문일</th>
                <th scope="col">결제일</th>
                <th scope="col">결제방법</th>
                <th scope="col">결제금액</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${payInfoList}" var="list" varStatus="i">
                <tr>
                    <td><c:out value="${pagination.itemNumber - i.count}"/></td>

                    <td><a href="javascript:Common.popup('/opmanager/order/confirm/order-detail-popup/${fn:escapeXml(list.orderSequence)}/${fn:escapeXml(list.orderCode)}', 'confirm', 1000, 700, 1);"><c:out value="${list.payUserName}"/></a></td>

                    <td><c:out value="${list.orderDate}"/></td>
                    <td><c:out value="${list.payDate}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${list.approvalType == 'bank'}">
                                온라인입금
                            </c:when>
                            <c:when test="${list.approvalType == 'card'}">
                                신용카드
                            </c:when>
                            <c:when test="${list.approvalType == 'vbank'}">
                                가상계좌
                            </c:when>
                            <c:otherwise>
                                포인트
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${op:numberFormat(list.amount) }"/>원</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div><!--// board_write E-->

    <c:if test="${empty payInfoList}">
        <div class="no_content">
                <c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
        </div>
    </c:if>

</form><br/>
<div style="text-align:center;">
    <page:pagination-manager />
</div>

<span>
		</span>


<script type="text/javascript">
    $(function() {
	  	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});


    function downloadExcel() {
        Shop.downloadExcelOrder("/opmanager/pay-info/download-excel", $('#payInfoParam').serialize(), false);
    }


</script>
<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<form:form modelAttribute="pointCheckParam" method="post">
    <h3>포인트 정합성 검사</h3>
    <div class="board_write">
        <table class="board_write_table" summary="Point Check">
            <colgroup>
                <col style="width:150px;" />
                <col style="width:400px;" />
                <col style="width:150px;" />
                <col style="width:*;" />
                <col style="width:150px;" />
                <col style="width:*;" />
            </colgroup>
            <tbody>
            <tr>
                <td class="label">주문 번호 검색</td> <!-- 작업 검색 -->
                <td colspan="5">
                    <div>
                        <form:select path="searchType" title="작업명"> <!-- 작업 검색 -->
                            <!-- <form:option value="ID">메서드명</form:option> --> <!-- 메서드명 -->
                            <form:option value="orderCode">주문번호</form:option> <!-- 작업명 -->
                        </form:select>
                        <form:input type="text" path="query" class="three" title="검색어 입력" style="width:250px;" placeholder="Search.."/> <!-- 검색어 입력 -->
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label">정합여부 검색</td> <!-- 작업 검색 -->
                <td colspan="5">
                    <div>
                        <form:select path="matchYnType" title="작업명"> <!-- 작업 검색 -->
                            <!-- <form:option value="ID">메서드명</form:option> --> <!-- 메서드명 -->
                            <form:option value="">전체</form:option>
                            <form:option value="Y">금액일치</form:option>
                            <form:option value="N">금액불일치</form:option>
                        </form:select>
                        <!-- <form:input type="text" path="query" class="three" title="검색어 입력" style="width:250px;" placeholder="Search.."/> --> <!-- 검색어 입력 -->
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label">주문일자</td><!-- op:message에 등록필요 -->
                <td colspan="5">
                    <div>
                       <form:select path="searchDateType">
                            <form:option value="EXECUTION_DATE" label="주문일자" />
                       </form:select>
                       <span class="datepicker"><form:input path="searchStartDate" class="datepicker" maxlength="8" title="${op:message('M00024')}" /><!-- 주문일자 시작일 --></span>
                       <span class="wave">~</span>
                       <span class="datepicker"><form:input path="searchEndDate" class="datepicker" maxlength="8" title="${op:message('M00025')}" /><!-- 주문일자 종료일 --></span>

                       <span class="day_btns">
                           <a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
                           <a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
                           <a href="javascript:;" class="btn_date day-15">${op:message('M00028')}</a><!-- 15일 -->
                           <a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
                           <a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
                        </span>
                   </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- 버튼시작 -->
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/point-check/list';"><span class="glyphicon glyphicon-repeat"></span> <c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
            <button type="submit" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> 검색</button> <!-- 검색 -->
        </div>
    </div>
    <!-- 버튼 끝-->

    <div class="count_title mt-40">
		<h5>
			<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
		</h5>
		<span>
			<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
				onchange="$('form#pointParam').submit();"> <!-- 화면 출력수 -->
				<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
			</form:select>
		</span>
	</div>
</form:form>

<form id="listForm">
    <div class="board_write">
        <table class="board_list_table" summary="배치 작업">
            <caption>배치 작업</caption>
            <colgroup>
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:3%;" />
                <col style="width:10%;" />
                <col style="width:8%;" />
            </colgroup>
            <thead>
            <tr>
                <th scope="col">주문번호</th>
                <th scope="col">지자체명</th>
                <th scope="col">주문상태</th>
                <th scope="col">결제금액</th>
                <th scope="col">취소금액</th>
                <th scope="col">입금대기금액</th>
                <th scope="col">정산금액</th>
                <th scope="col">사용완료 포인트</th>
                <th scope="col">정합여부</th>
                <th scope="col">미정주문건수</th>
                <th scope="col">주문일자</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${pointCheckList}" var="list" varStatus="i">
                <tr>
                    <td><c:out value='${list.orderCode}'/></td>
                    <td><c:out value='${list.locgovNm}'/></td>
                    <td><c:out value='${list.orderStatusDesc}'/></td>
                    <td><c:out value='${list.orderAmt}'/></td>
                    <td><c:out value='${list.orderAmtCancel}'/></td>
                    <td><c:out value='${list.orderAmt0}'/></td>
                    <td><c:out value='${list.remittanceAmt}'/></td>
                    <td><c:out value='${list.cntrUsePoint}'/></td>
                    <td><c:out value='${list.matchYn}'/></td>
                    <td><c:out value='${list.holdCnt}'/></td>
                    <td><c:out value='${op:date(list.createdDate)}'/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div><!--// board_write E-->

    <c:if test="${empty pointCheckList}">
        <div class="no_content">
                <c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
        </div>
    </c:if>

	<!-- <div class="btn_all btn_center">
		<div class="flex_box gap-08">
            <a id="delete_list_data" href="#" class="btn btn-default btn-sm"><span><c:out value="${op:message('M00576')}"/></span></a> <!-- 선택삭제 -->
    <!--        <a href="javascript:fnBatchLogCreate();" class="btn btn-active btn-sm"><span class="glyphicon glyphicon-plus"></span> <c:out value="${op:message('M00088')}"/></a> <!-- 신규등록 -->
    <!--    </div>
    </div> -->
    <div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</form>



<script type="text/javascript">
    //window.onload = function() {

    //};
    matchYnSelected.addEventListener("change", (event) => {


    });

    //  matchYnSelected.selected = true;

    $(function() {
        Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
        //데이터 출력량 적용
        $('#itemsPerPage').on("change", function(){
            $('#messageParam').submit();
        });

        //목록데이터 - 삭제처리
        // $('#delete_list_data').on('click', function() {
        //     Common.updateListData("/opmanager/batch-log/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
        // });

    });

    // function fnBatchLogUpdate(pointCheckId, logName) {
    //     Common.popup('/opmanager/batch-log/detail?pointCheckId=' + pointCheckId, 'update', 1250, 310, 1);
    // }

    // function fnBatchLogCreate(pointCheckId) {
    //     Common.popup('/opmanager/batch-log/create', 'create', 1250, 310, 1);
    // }

    function setYesterDayTimeToCurrentTime(yesterDayTime){

        var now = new Date();
        var hour = now.getHours();

        if (hour < 10) {
            hour = '0'+hour;
        }

        $('select#conditionType').val('PAY_DATE');
        /* $('select[name = searchStartDateTime]').val(yesterDayTime);
        $('select[name = searchEndDateTime]').val(hour); */
}
</script>
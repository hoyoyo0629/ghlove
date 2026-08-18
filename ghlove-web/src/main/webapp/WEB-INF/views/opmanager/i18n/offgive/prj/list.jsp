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

<h3><span>기부금 접수관리</span></h3>

<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="query"/>
	<div class="board_write">
	    <table class="board_write_table" summary="">
	        <colgroup>
	            <col style="width:220px;">
	            <col>
	            <col style="width:220px;">
	            <col>
	        </colgroup>
	        <tbody>
	            <tr>
	                <td class="label">검색구분</td>
	                <td colspan="3">
	                    <div class="flex_box gap-08">
	                        <form:select path="shKeyword" title="검색구분" class="wd-150">
	                            <form:option value="CNTR_SN">접수번호</form:option>
	                            <form:option value="LOCGOV_NM">기부 지자체</form:option>
	                            <form:option value="ELCTRN_PAY_NO">전자납부번호</form:option>
	                            <form:option value="RCEPT_BANK_NM">지점명</form:option>
	                        </form:select>
	                        <form:input path="shText" title="검색구분" class="input_txt required _filter half"
	                            type="text" value="" />
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">신청일</td>
	                <td colspan="3">
	                    <div>
	                    	<span class="datepicker"><form:input path="shFrstRegistPnttmStart" cssClass="datepicker optional " title="${op:message('M00507')}" maxlength="8" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="shFrstRegistPnttmEnd" cssClass="datepicker optional " title="${op:message('M00509')}" maxlength="8" /></span>
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
	            <tr>
	                <td class="label">금액</td>
	                <td colspan="3">
	                    <div class="flex_box gap-08 item-center">
	                        <form:input path="shCntrAmtStart" title="검색구분" class="input_txt required _filter wd-150" type="text" value="" />
	                        <span class="wave">~</span>
	                        <form:input path="shCntrAmtEnd" title="검색구분" class="input_txt required _filter wd-150" type="text" value="" />
	                        <span class="wave">원</span>
	                        <span>
	                            <a href="javascript:;" class="btn_date" onClick="shCntrAmtClick(0, 500000)">50만원 이하</a>
	                            <a href="javascript:;" class="btn_date" onClick="shCntrAmtClick(0, 1000000)">100만원</a>
	                            <a href="javascript:;" class="btn_date" onClick="shCntrAmtClick(0, 2000000)">200만원</a>
	                            <a href="javascript:;" class="btn_date" onClick="shCntrAmtClick('', '')">전체</a>
	                        </span>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label">기부상태</td>
	                <td <c:if test="${role != 'SYSTEM'}">colspan="3"</c:if>>
	                    <div class="flex_box gap-12">
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrSttusCode" id="rdo1-1" value="" checked="checked" />
	                            <label for="rdo1-1">전체</label>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrSttusCode" id="rdo1-2" value="100" />
	                            <label for="rdo1-2">신고</label>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrSttusCode" id="rdo1-3" value="200" />
	                            <label for="rdo1-3">수납</label>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrSttusCode" id="rdo1-4" value="300" />
	                            <label for="rdo1-4">과오납</label>
	                        </div>
	                    </div>
	                </td>
	                <c:if test="${role == 'SYSTEM'}">
	                <td class="label">소속지점</td>
	                <td>
	                    <div class="flex_box gap-12">
	                    	<div class="input-form">
	                            <form:radiobutton path="shRceptBankCode" id="rdo1-0" value="" checked="checked" />
	                            <label for="rdo1-1">전체</label>
	                        </div>
		                	<c:forEach items="${shRceptBankCode}" var="shRceptBankCode" varStatus="i">
		                		<div class="input-form">
		                			<form:radiobutton id="rdo2-${i.count}" path="shRceptBankCode" value="${fn:escapeXml(shRceptBankCode.id)}" />
		                			<label for="rdo2-${i.count}">${fn:escapeXml(shRceptBankCode.label)}</label>
	                			</div>
				            </c:forEach>
	                    </div>
	                </td>
	                </c:if>
	                <c:if test="${role != 'SYSTEM'}">
	                	<form:hidden path="shRceptBankCode" value="${fn:escapeXml(offBank) }"/>
	                	<form:hidden path="shRceptBankNm" value="${fn:escapeXml(psitnNm) }"/>
	                	<form:hidden path="shRceptBankCodeNm" value="${fn:escapeXml(offBankNm.label) }"/>
	                </c:if>
	            </tr>
	        </tbody>
	    </table>
	    <p class="tip mt15">* 기탁서 등록 후 정상 처리까지 최대 1시간 가량 소요될 수 있습니다.</p>

	    <div class="btn_all btn_right">
	        <div class="flex_box gap-08">
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/offgive/prj/list';">초기화</button>
	            <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
	        </div>
	    </div>
	</div>

	<div class="count_title mt-40">
	    <h5>총 <c:out value="${op:numberFormat(count)}"/>건</h5>
	    <span>
	        <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();"> <!-- 화면출력 -->
		       	<form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
		       	<form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
		       	<form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
		       	<form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
	    	</form:select>
	    </span>
	</div>

</form:form>

	<div class="board_list">
	    <table class="board_list_table" summary="오프라인 기부금 접수">
	        <caption>오프라인 기부금 접수</caption>
	        <colgroup>
                <col style="width:6%;">
                <col style="width:*;">
                <col style="width:6%;">
                <col style="width:14%;">
                <col style="width:9%;">
                <col style="width:12%;">
                <col style="width:6%;">
                <col style="width:9%;">
                <col style="width:14%;">
                <col style="width:14%;">
                <col style="width:14%;">
            </colgroup>
	        <thead>
	            <tr>
	                <th scope="col">No.</th>
	                <th scope="col">접수번호(지자체)</th>
	                <th scope="col">기부상태</th>
	                <th scope="col">전자납부번호</th>
	                <th scope="col">수납일</th>
	                <th scope="col">기부 지자체</th>
	                <th scope="col">이름</th>
	                <th scope="col">기부 금액</th>
	                <th scope="col">지점명</th>
	                <th scope="col">신고일</th>
	                <th scope="col">특정사업에 기부하기 사업명</th>
	            </tr>
	        </thead>
	        <tbody>
	        	<c:forEach items="${list}" var="list" varStatus="i">
	            <tr style="background:#fff;">
<!-- 	                <td> -->
<!-- 	                    <input type="checkbox" name="" value="" id=""> -->
<!-- 	                </td> -->
	                <td>
	                    <c:out value="${pagination.itemNumber - i.count}"/>
	                    <input type="hidden" id="cntrPoint" nama="cntrPoint" value="${fn:escapeXml(list.cntrPoint) }" />
	                    <input type="hidden" id="rtnpsntReqstCode" nama="rtnpsntReqstCode" value="${fn:escapeXml(list.rtnpsntReqstCode) }" />
	                </td>
	                <td>
	                    <a href="/opmanager/offgive/prj/detail/${fn:escapeXml(list.cntrSn)}"><c:out value="${list.cntrSn}"/></a>
	                </td>
	                <td>
	                    <c:out value="${list.cntrSttusCode == 100 ? '신고' : (list.cntrSttusCode == 200 ? '수납' : '과오납')}"/>
	                </td>
	                <td>
	                    <c:out value="${list.elctrnPayNo}"/>
	                </td>
	                <td>
	                    <c:out value="${op:date(list.sttemntPayDe)}"/>
	                </td>
	                <td>
	                    <c:out value="${list.upperLocgovNm} ${list.locgovNm}"/>
	                </td>
	                <td>
	                    <c:out value="${fn:substring(list.userName, 0, 1)} * ${fn:substring(list.userName, 2, 100)}"/>
	                </td>
	                <td>
	                    <c:out value="${op:numberFormat(list.cntrAmt)}"/>
	                </td>
	                <td>
	                    <c:out value="${list.rceptBankCodeNm} ${list.rceptBankNm}"/>
	                </td>

	                <td>
	                    <c:out value="${fn:substring(list.frstRegistPnttm, 0, 19)}"/>
	                </td>
	                <td>
	                    <c:out value="${list.prjSubject}"/>
	                </td>
	            </tr>
	            </c:forEach>
	        </tbody>
	    </table>
	    <c:if test="${empty list}">
		  	<div class="no_content">
		      	<c:out value="${op:message('M00473')}"/>
		  	</div>
	  	</c:if>
	    <div class="btn_all flex_box juc-sbt">
	        <div class="btn_left">
	            <div class="flex_box gap-08">
	                <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
	                <button type="button" class="btn btn-dark-gray btn-mini" onclick="listPrint()">출력</button>
	            </div>
	        </div>
	        <div class="btn_right">
	            <div class="flex_box gap-08">
	            	<c:if test="${role != 'SYSTEM'}">
	                	<button type="button" class="btn btn-dark-default btn-mini" onClick="location.href='/opmanager/offgive/prj/create'">기탁서 등록</button>
	                </c:if>
<!-- 	                <button type="button" class="btn btn-dark-gray btn-mini">삭제</button> -->
	            </div>
	        </div>
	    </div>
	    <div class="pagination-wrap">
	      	<page:pagination-manager />
	  	</div>
	</div>

<div style="display: none;">
    <span id="today"><c:out value="${today}"/></span>
    <span id="week"><c:out value="${week}"/></span>
    <span id="month1"><c:out value="${month1}"/></span>
    <span id="month3"><c:out value="${month3}"/></span>
    <span id="year"><c:out value="${year}"/></span>
</div>

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">

$(function() {
	serachDate();

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="shFrstRegistPnttmStart"]' , 'input[name="shFrstRegistPnttmEnd"]');
	EventHandler.calendarStartDateAndEndDateVaild();
});

/**
 * 조회 기간 설정
 * @return
 */
function serachDate() {
    $(".day_btns .btn_date").on('click',function(){
        var $id = $(this).attr('class').replace('btn_date ','');        // id[0] : type, id[1] : value

        if ($id == 'all') {
        	$("input[type=text]",$(this).parent().parent()).val('');
        } else {
            var today = $("#today").text();
            var date1 = '';
            var date2 = '';

            if ($id == 'today') {
                date1 = today;
                date2 = today;
            } else {
                date1 = $("#"+$id).text();
                date2 = today;
            }

            $("input[type=text]",$(this).parent().parent()).eq(0).val(date1);
            $("input[type=text]",$(this).parent().parent()).eq(1).val(date2);
        }
    });
}

// 금액 click
function shCntrAmtClick(start, end) {
	$("#shCntrAmtStart").val(start);
	$("#shCntrAmtEnd").val(end);
}

function downloadExcel() {
    if ($('#shFrstRegistPnttmStart').val() == "" || $('#shFrstRegistPnttmEnd').val() == "") {
		alert("조회 진행 후 엑셀 다운로드를 진행해주세요.");
		return;
	}

    Shop.downloadExcelOrder("/opmanager/offgive/prj/list/download-excel", $('#searchParam').serialize(), true);
}

function listPrint() {
	if ($('#shFrstRegistPnttmStart').val() == "") {
		alert("신청일 시작일자를 기입해 주세요.");
		$('#shFrstRegistPnttmStart').focus();
		return;
	}
	if ($('#shFrstRegistPnttmEnd').val() == "") {
		alert("신청일 종료일자를 기입해 주세요.");
		$('#shFrstRegistPnttmEnd').focus();
		return;
	}

	var param = "";

	if('${fn:escapeXml(role) }' == 'OFF_MAIN') {
		param = "&rceptBankCode=${fn:escapeXml(offBank) }&rceptBankNm=&rceptBankCodeNm=${fn:escapeXml(offBankNm.label) }";
	} else if('${fn:escapeXml(role) }' == 'OFF_SUB') {
		param = "&rceptBankCode=${fn:escapeXml(offBank) }&rceptBankNm=${fn:escapeXml(psitnNm) }&rceptBankCodeNm=${fn:escapeXml(offBankNm.label) }";
	} else {
		param = "&rceptBankCode=&rceptBankNm=&rceptBankCodeNm=";
	}

	window.open('/opmanager/offgive/prj/popup/form-list?startDt='+$('#shFrstRegistPnttmStart').val()
													+ '&endDt='+$('#shFrstRegistPnttmEnd').val()
													+ param
													+ '&cntrSttusCode=' + $('input[name=shCntrSttusCode]:checked').val()
													+ '&cntrAmtStart=' + $('#shCntrAmtStart').val()
													+ '&cntrAmtEnd=' +  $('#shCntrAmtEnd').val()
													,'pop','width=1200,height=1200');
}

</script>

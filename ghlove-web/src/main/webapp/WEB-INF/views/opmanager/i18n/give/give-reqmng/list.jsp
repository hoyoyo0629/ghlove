<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"     uri="http://www.springframework.org/security/tags"%>


<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>기부금•포인트 변경</span></h3>

<form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="post" id="searchForm">
<form:hidden path="query"/>
<div class="board_write">
    <table class="board_write_table" summary="">
        <colgroup>
            <col style="width:200px;">
        </colgroup>
        <tbody>
	      <tr>
	        <td class="label">기부 지자체</td>
	        <td colspan="2">
	          <div class="flex_box gap-08">
	             <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
	                 <form:option value="">-시·도 선택-</form:option>
	                 <c:forEach items="${wdr}" var="wdr">
	                   <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
	                 </c:forEach>
	             </form:select>

	             <form:select path="shLocgovCode" class="wd-150">
	                 <option value="">-시·군·구-</option>
	             </form:select>

	          </div>
	        </td>
                <td class="label"><span class="required_mark">*</span>변경신청일자</td>
                <td colspan="4">
                    <div>
                        <span class="datepicker">
                            <form:input path="shFrstRegistPnttmStart" cssClass="datepicker optional " title="${op:message('M00507')}" />
                        </span>
                        <span class="wave">~</span>
                        <span class="datepicker">
                            <form:input path="shFrstRegistPnttmEnd" cssClass="datepicker optional " title="${op:message('M00509')}" />
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
           		 <tr>
	                <td class="label">요청분류</td>
	                <td colspan="2">
	                    <div class="flex_box gap-08">
	                        <form:select id="shCntrReqmngCode" path="shCntrReqmngCode" title="요청분류" class="wd-150">
	                         		<form:option value="">- 전체 -</form:option>
									<c:forEach items="${reqmngCodeList}" var="code" varStatus="status">
									<form:option value="${fn:escapeXml(code.id)}"><c:out value="${code.label}"/></form:option>
							</c:forEach>
	                        </form:select>
	                    </div>
	                </td>
	                    <td class="label">승인여부</td>
 	                <td colspan="4">
	                    <div class="flex_box gap-08">
	                        <form:select id="shReqStatusCode" path="shReqStatusCode" title="승인여부" class="wd-150">
	                           <form:option value="">- 전체 -</form:option>
									<c:forEach items="${reqStatusCodeList}" var="code" varStatus="status">
									<form:option value="${fn:escapeXml(code.id)}"><c:out value="${code.label}"/></form:option>
									</c:forEach>
	                        </form:select>
	                    </div>
	                </td>
            	</tr>
            	<tr>
            		<td class="label">전자납부번호</td>
	                <td colspan="3">
	                    <div class="flex_box gap-08">
	                        <form:input path="shElctrnPayNo" title="검색구분" class="input_txt _filter half" type="text" value="${fn:escapeXml(searchParam.shElctrnPayNo)}" />
	                    </div>
	                </td>
            	</tr>
        </tbody>
    </table>
    <div class="btn_all btn_left">
			<ul class="list-bullet point">
		        <li>
		            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
		        </li>
	        </ul>
	  	</div>
    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="searchClear();">초기화</button>
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
        </div>
    </div>
</div>

<div class="count_title mt-40">
    <h5>총 <c:out value='${count}'/>건&nbsp;&nbsp;<font color="red">[정렬 기준은 요청일시 최신순입니다.]</font></h5>
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
<div>
    <table class="board_list_table" summary="기부금변경신청관리">
        <caption>기부금변경신청관리현황</caption>
        <colgroup>
           <col style="width:50px;">
            <col style="width:100px;">
            <col style="width:100px;">
            <col style="width:150px;">
            <col style="width:100px;">
            <col style="width:100px;">
            <col style="width:150px;">
            <col style="width:100px;">
            <col style="width:100px;">
            <col style="width:100px;">
            <col style="width:200px;">
            <col style="width:100px;">
            <col style="width:100px;">
            <col style="width:100px;">
            <col style="width:100px;">

        </colgroup>
        <thead>
            <tr>
                <th scope="col">No.</th>
                <th scope="col">요청분류</th>
                <th scope="col">관리자ID</th>
                <th scope="col">기부지자체</th>
                <th scope="col">기부자명</th>
                <th scope="col">기부자ID</th>
                <th scope="col">기부일자</th>
                <th scope="col">전자납부번호</th>
                <th scope="col">기부액</th>
                <th scope="col">민간연계기관</th>
                <th scope="col">비고</th>
                <th scope="col">요청일시</th>
                <th scope="col">승인일시</th>
                <th scope="col">취소일시</th>
                <th scope="col"></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="list" varStatus="i">
            <tr style="background:#fff;">
                <td>
                    <c:out value='${pagination.itemNumber - i.count}'/>
                </td>
                 <td>
                    <c:out value='${list.cntrReqmngCodeName}'/>
                </td>
               <td>
                   <c:out value='${list.frstRegisterId}'/>
                </td>
                <td>
                    <c:out value='${list.locgovCodeName}'/>
                </td>
                <td>
                    <c:out value='${list.userName}'/>
                </td>
                <td>
                    <c:out value='${list.loginId}'/>
                </td>
                <td>
                    <c:out value='${op:date(list.sttemntPayDe)}'/>
                </td>
                <td>
                    <c:out value='${list.elctrnPayNo}'/>
                </td>
                <td style="text-align: right;">
                    <c:out value='${op:numberFormat(list.cntrAmt)}'/>
                </td>
                <td>
                	  <c:out value='${list.detail}'/>
                </td>
                <td>
                    <div id="discription" title ="${fn:escapeXml(list.discription)}"
                    style= "width :200px; text-align: left; display: block; text-overflow: ellipsis; white-space: nowrap; overflow: hidden;">
                    <c:out value='${list.discription}'/></div>
                </td>
                 <td>
                    <c:out value='${list.frstRegistPnttm}'/>
                </td>
                 <td>
                 <c:choose>
                 	<c:when test="${empty list.apprDt}">
	    				<span>-</span>
                 	</c:when>
                 	<c:otherwise>
                    	<c:out value='${list.apprDt}'/>
                 	</c:otherwise>
                </c:choose>
                </td>
                 <td>
                 <c:choose>
                 	<c:when test="${empty list.cancleDt}">
	    				<span>-</span>
                 	</c:when>
                 	<c:otherwise>
                    	<c:out value='${list.cancleDt}'/>
                 	</c:otherwise>
                </c:choose>
                </td>
                <td>
                 	<c:choose>
                 		<c:when test="${list.reqStatusCode eq '100'}">
	    					<button type="button" class="btn btn-dark-gray btn-mini" value ='${fn:escapeXml(list.reqId)}' onClick="approveReq('${fn:escapeXml(list.reqId)}','${fn:escapeXml(list.cntrReqmngCode)}','${fn:escapeXml(list.elctrnPayNo)}','${fn:escapeXml(list.userId)}', '${fn:escapeXml(list.cntrSn)}', '${fn:escapeXml(list.linkInsttCd)}')">요청승인</button>
	    					<%-- <button type="button" class="btn btn-dark-gray btn-mini" value ='${fn:escapeXml(list.reqId)}' onClick="approveReq('${fn:escapeXml(list.reqId)}','${fn:escapeXml(list.cntrReqmngCode)}','${fn:escapeXml(list.elctrnPayNo)}','${fn:escapeXml(list.userId)}', '${fn:escapeXml(list.cntrSn)}', '${fn:escapeXml(list.linkInsttCd)}', '${fn:escapeXml(list.sttemntPayDe)}')">요청승인</button> --%>
                 		</c:when>
                 		<c:when test="${list.reqStatusCode eq '200'}">
							<span>요청취소됨</span>
                 		</c:when>
                 		<c:otherwise>
                 			<span>승인 완료</span>
                 		</c:otherwise>
                 	</c:choose>
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
    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/give/give-state/list'">목록</button>
        </div>
    </div>
    <div class="pagination-wrap">
	    <page:pagination-manager />
	</div>
</div>
  <!-- // 개발 영역 -->

<div style="display: none;">
    <span id="today"><c:out value='${today}'/></span>
    <span id="week"><c:out value='${week}'/></span>
    <span id="month1"><c:out value='${month1}'/></span>
    <span id="month3"><c:out value='${month3}'/></span>
    <span id="year"><c:out value='${year}'/></span>
</div>


<script type="text/javascript">

$(function() {
	serachDate();

	$(".contents_inner").find("h3 span:first").text("기부금•포인트 변경");
	$(".contents_inner").find("div.location a").removeClass("on");
	$(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">목록</a>');

	wdrChange($("#shWdr").val());
	searchEnterKey();

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="shFrstRegistPnttmStart"]' , 'input[name="shFrstRegistPnttmEnd"]');
	EventHandler.calendarStartDateAndEndDateVaild();
});


/**
 * 조회 기간 설정
 * @return
 */
function serachDate() {
    $(".btn_date").on('click',function(){
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

function searchClear() {
    $("#shFrstRegistPnttmStart").val("");
    $("#shFrstRegistPnttmEnd").val("");
    $("#shWdr").val("");
    $("#shLocgovCode option").remove();
    $('#shLocgovCode').append('<option value="">-시·군·구-</option>');
    $("#shReqStatusCode").val("");
    $("#shCntrReqmngCode").val("");
    $("#shElctrnPayNo").val("");

    $("#searchForm").submit();
}

function search() {

	strStartDate = $("#shFrstRegistPnttmStart").val();
	strEndDate = $("#shFrstRegistPnttmEnd").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
		var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			var value = $("#shFrstRegistPnttmEnd").val();
			$("#shFrstRegistPnttmEnd").val("");
			$("#shFrstRegistPnttmEnd").focus();
			$("#shFrstRegistPnttmEnd").val(value);
			return false;
		}
	}

	$("#shElctrnPayNo").val($("#shElctrnPayNo").val().trim());
	$("#searchForm").submit();
}

/**
 *	함 수 명 : searchEnterKey
 *	기	능  : 검색 엔터키 이벤트
 */
function searchEnterKey() {
	$("#shElctrnPayNo, #shCntrDeStart, #shCntrDeEnd").on('keydown', function(e){
		if (e.keyCode == '13') {
			search();
		}
	});
}




//지자체 변경
function wdrChange(value) {
	Common.loading.display = false;

	$("#shLocgovCode option").remove();
	if ($("#shWdr").val() != "") {
		$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {

			for (var i = 0; i < response.length; i++) {
	            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
	            $('#shLocgovCode').append(options);
	        }
			// 조회 된 값 유지
			if ("${fn:escapeXml(searchParam.shLocgovCode)}" != "") {
			    $("#shLocgovCode").val('${fn:escapeXml(searchParam.shLocgovCode)}').prop("selected", true);
			}
	    });
	} else {
        $('#shLocgovCode').append('<option value="">-시·군·구-</option>');
	}
}

function approveReq(reqID,cntrReqmngCode,elctrnPayNo,userId, cntrSn, linkInsttCd){

	var noBankInsttCds = ["I0000036", "I0000047","I0000048","I0000092","I0000118","I0000144"]; // 20260608 추가.

	 if(cntrReqmngCode == 200 && noBankInsttCds.includes(linkInsttCd)){
			alert("민간연계 기부건은 포인트 생성이 불가합니다.");
			return false;
	 }

	if(confirm("변경신청을 승인하시겠습니까?")){
		var param = {
				reqId : reqID ,
				cntrReqmngCode : cntrReqmngCode,
				elctrnPayNo : elctrnPayNo,
				userId : userId,
				cntrSn : cntrSn,
				/* sttemntPayDe : sttemntPayDe */
		};

	 	$.post('opmanager/give/give-state/approveReq',param, function(response) {
			console.log(response);
	 		if(response.isSuccess == true) {
				alert("변경신청 승인이 되었습니다.");
	 			location.reload();
	 		}else{
	 			alert(response.errorMessage);
	 		}

	 	});

	}
}



function cancelReq(reqID,cntrReqmngCode){

	if(confirm("변경신청을 취소하시겠습니까?")){
		var param = {
				reqId : reqID,
				cntrReqmngCode : cntrReqmngCode
		};

	 	$.post('opmanager/give/give-state/cancelReq',param, function(response) {
			console.log(response);
	 		if(response.isSuccess == true) {
				alert("변경신청 취소가 완료되었습니다.")
	 			location.reload();
	 		}else{
	 			alert("변경신청 취소 중 오류가 발생하였습니다.")
	 		}

	 	});

	}

}

</script>


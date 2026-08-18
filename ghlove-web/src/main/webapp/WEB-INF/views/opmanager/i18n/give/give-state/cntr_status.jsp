<meta http-equiv="expires" content="-1" >

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

<h3><span>기부금 모금 상세현황</span></h3>

<form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="post" id="searchForm">
	<form:hidden path="query"/>
	<div class="board_write">
	    <table class="board_write_table" summary="">
	        <colgroup>
	            <col style="width:200px;">
	            <col style="width:*;">
	            <col style="width:200px;">
	            <col style="width:*;">

	        </colgroup>
			<tbody>
				<tr>
					<td class="label">기부 지자체</td>
			        <td colspan="">
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
			        <td class="label">검색구분</td>
		            <td colspan="">
		                <div>
		                    <ul class="input-group col-3">
		                        <li class="">
		                            <form:select path="shKeyword" title="기부자명" class="wd-160" onchange="keywordChange(this.value)">
		                                <form:option value="USER_NAME">기부자명</form:option>
		                                <form:option value="LOGIN_ID">기부자ID</form:option>
		                                <form:option value="ELCTRN_PAY_NO">전자납부번호</form:option>
		                                <form:option value="PRJ_SUBJECT">특정사업명</form:option>
		                            </form:select>
		                        </li>
		                        <li class="input">
		                            <form:input path="shText" title="검색구분" class="input_txt required _filter" type="text" value="${fn:escapeXml(searchParam.shText)}" />
		                        </li>
		                    </ul>
		                </div>
		            </td>
				</tr>
	            <tr>
	                <td class="label">기부일자</td>
	                <td colspan="">
	                    <div>
	                        <span class="datepicker">
	                            <form:input path="shCntrDeStart" cssClass="datepicker optional " title="${op:message('M00507')}" />
	                        </span>
	                        <span class="wave">~</span>
	                        <span class="datepicker">
	                            <form:input path="shCntrDeEnd" cssClass="datepicker optional " title="${op:message('M00509')}" />
	                        </span>
	                        <span class="day_btns day_btn1">
	                        	<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
	                        </span>
	                    </div>
	                </td>
	                <td class="label">기부채널</td>
	                <td colspan="">
	                    <div class="flex_box gap-12">
	                        <div class="input-form">
	                            <form:radiobutton path="shLinkInsttCdAt" value="" label="전체" checked="checked"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shLinkInsttCdAt" value="N" label="고향사랑e음" />
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shLinkInsttCdAt" value="Y"/>
	                            <form:select path="shLinkInsttCd" class="wd-150" onClick="clickLinkInsttCdAt()" onChange="linkInsttChange(this.value)" style="margin-left:5px">
					                 <form:option value="">민간연계기관 전체</form:option>
					                 <c:forEach items="${linkInsttCdList}" var="insttCd">
					                   <form:option value="${fn:escapeXml(insttCd.id)}" label="${fn:escapeXml(insttCd.label)}" />
					                 </c:forEach>
					            </form:select>
	                        </div>
	                    </div>
	                </td>
	            </tr>
	            <!-- 납부일자 검색조건 추가 2023-02-13 배예림 -->
	            <%-- <tr>
	                <td class="label">납부일자(결제일자)</td>
	                <td colspan="3">
	                    <div>
	                        <span class="datepicker">
	                            <form:input path="shSttemntPayDeStart" cssClass="datepicker optional " title="${op:message('M00507')}" />
	                        </span>
	                        <span class="wave">~</span>
	                        <span class="datepicker">
	                            <form:input path="shSttemntPayDeEnd" cssClass="datepicker optional " title="${op:message('M00509')}" />
	                        </span>
	                        <span class="day_btns day_btn2">
	                        	<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
	                            <a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
	                        </span>
	                    </div>
	                </td>
	            </tr> --%>
	            <tr>
	            	<td class="label">온·오프라인</td>
	                <td colspan="">
	                    <div class="flex_box gap-12">
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrPathCode" value="" label="전체" checked="checked"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrPathCode" value="100" label="온라인"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrPathCode" value="200" label="오프라인"/>
	                        </div>
	                    </div>
	                </td>
	            	<td class="label">기부상태</td>
	                <td colspan="">
	                    <div class="flex_box gap-12">
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrStatusCode" value="" label="전체" checked="checked"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrStatusCode" value="100" label="미수납"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrStatusCode" value="200" label="수납" />
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shCntrStatusCode" value="300" label="과오납" />
	                        </div>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	            	<td class="label">기부금액</td>
	                <td colspan="">
	                    <div>
	                        <ul class="input-group col-3">
	                            <li class="input"><input id="cntrAmtStart" title="기부금액검색 최소금액" type="text" class="input_txt _filter shPrice" value="${op:numberFormat(searchParam.shCntrAmtStart)}"></li>
	                            <li><label for="cntrAmtStart">원</label></li>
	                            <li class="text">~</li>
	                            <li class="input"><input id="cntrAmtEnd" title="기부금액검색 최대금액" type="text" class="input_txt _filter shPrice" value="${op:numberFormat(searchParam.shCntrAmtEnd)}"></li>
	                            <li><label for="cntrAmtEnd">원</label></li>
	                            <form:input type="hidden" id="shCntrAmtStart" path="shCntrAmtStart"/>
	                            <form:input type="hidden" id="shCntrAmtEnd" path="shCntrAmtEnd"/>
	                        </ul>
	                    </div>
	                </td>
	            	<td class="label">기부대상</td>
	                <td colspan="">
	                    <div class="flex_box gap-12">
	                        <div class="input-form">
	                            <form:radiobutton path="shDsgnDonateAt" value="" label="전체" checked="checked"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shDsgnDonateAt" value="N" label="자치단체에 기부하기"/>
	                        </div>
	                        <div class="input-form">
	                            <form:radiobutton path="shDsgnDonateAt" value="Y" label="특정사업에 기부하기" />
	                        </div>
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
	        	<!-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button> -->
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="searchClear()">초기화</button>
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
	           <!--  <button type="submit" class="btn btn-dark-gray btn-mini">검색</button> -->
	        </div>
	    </div>
	</div>

	<div class="count_title mt-40">
	    <h5>총 <c:out value='${count}'/>건</h5>
	    <span>
	        <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchForm').submit();"> <!-- 화면출력 -->
	         <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
	         <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
	         <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
	         <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
	     </form:select>
	    </span>
	</div>
</form:form>
<div class="board_list" style="overflow-x:scroll;">
    <table class="board_list_table" summary="기부금 모금 상세현황" style="width:150%">
        <caption>기부금 모금 상세현황</caption>
        <colgroup>
            <col style="min-width:40px;">

            <col style="min-width:120px; white-space:nowrap">
            <col style="min-width:100px;">
            <col style="min-width:100px;">
            <col style="min-width:120px;">
            <col style="min-width:80px;">
            <col style="min-width:80px;">
            <col style="min-width:80px;">
            <col style="min-width:80px;">
            <col style="min-width:100px;">
            <col style="min-width:120px;">
            <col style="min-width:120px;">

            <col style="min-width:150px;">
            <col style="min-width:100px;">
            <col style="min-width:100px;">
            <col style="min-width:100px;">
            <col style="min-width:100px;">
            <col style="min-width:100px;">
            <col style="min-width:120px;">
            <col style="min-width:120px;">
            <col style="min-width:100px;">
            <col style="min-width:120px;">
            <col style="min-width:120px;">

        </colgroup>
        <thead>
            <tr>
                <th scope="col">No.</th>

                <th scope="col">기부번호</th>
                <th scope="col">부과일자</th>
                <th scope="col">수납일자</th>
                <th scope="col">전자납부번호</th>
                <th scope="col">기부형태</th>
                <th scope="col">기부상태</th>
				<th scope="col">국세청</th>
                <th scope="col">기부자명</th>
                <th scope="col">아이디</th>
                <th scope="col">소속지자체</th>
                <th scope="col">기부지자체</th>

                <th scope="col">특정사업명</th>
                <th scope="col">기부금액</th>
                <th scope="col">발생포인트</th>
                <th scope="col">잔액포인트</th>
                <th scope="col">답례품여부</th>
                <th scope="col">납부유효일자</th>
                <th scope="col">민간연계기관</th>
                <th scope="col">접수은행/복지센터</th>
                <th scope="col">접수자명</th>
                <th scope="col">등록일시</th>
                <th scope="col">수정일시</th>

            </tr>
        </thead>
        <tbody>
            <c:forEach items="${list}" var="list" varStatus="i">
            <tr style="background:#fff;">
                <td><c:out value='${pagination.itemNumber - i.count}'/></td>
                <td><c:out value='${list.cntrSn}'/></td>
                <td><c:out value='${op:date(list.cntrDe)}'/></td>
                <td><c:out value='${op:date(list.sttemntPayDe)}'/></td>
                <!-- 수정 -->
                <td><a href="javascript:giveModifyPopup('${fn:escapeXml(list.elctrnPayNo)}')"> <c:out value='${list.elctrnPayNo}'/></a></td>
                  <td> <c:out value='${list.cntrPathCode}'/></td>
                <td> <c:out value='${list.cntrSttusCode}'/></td>
                <td>
                	<c:choose>
                		<c:when test="${list.ntsSttusMssage eq '-'}">
                			<c:out value='${list.ntsSttusMssage}'/>
                		</c:when>
                		<c:otherwise>
                			<a href="javascript:ntsList('${list.elctrnPayNo}')">
                				<c:out value='${list.ntsSttusMssage}'/>
                			</a>
                		</c:otherwise>
                	</c:choose>
                </td>
                <td> <c:out value='${list.userName}'/></td>
                <td> <c:out value='${list.loginId}'/></td>
                <td style="white-space:nowrap"> <c:out value='${list.psitnLocgovCode}'/></td>
                <td style="white-space:nowrap"> <c:out value='${list.cntrLocgovCode}'/></td>
                <td style="white-space:nowrap; text-align:left">
                	<c:choose>
                		<c:when test="${list.prjId eq 0}">자치단체기부</c:when>
                		<c:otherwise><c:out value='${list.prjSubject}'/></c:otherwise>
                	</c:choose>
                </td>
                <td style="text-align:right"> <c:out value='${op:numberFormat(list.cntrAmt)}'/></td>
                <td style="text-align:right"> <c:out value='${op:numberFormat(list.cntrPoint)}'/></td>
                <td style="text-align:right"> <c:out value='${op:numberFormat(list.cntrBlcePoint)}'/></td>
				<td> <c:out value='${list.rtnpsntReqstCode}'/></td>
                <td><c:out value='${op:date(list.payValidDe)}'/></td>
                <td style="white-space:nowrap"><c:out value='${list.detail}'/></td>
                <td style="white-space:nowrap"> <c:out value='${list.rceptBankNm}'/></td>
                <td> <c:out value='${list.rcepterNm}'/></td>
               	<td style="white-space:nowrap"> <c:out value='${list.frstRegistPnttm}'/></td>
                <td style="white-space:nowrap"> <c:out value='${list.lastUpdtPnttm}'/></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty list}">
      <div class="no_content">
          <c:out value="${op:message('M00473')}"/>
      </div>
  </c:if>
<!--     <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/give/give-state/detail_list'">목록</button>
        </div>
    </div> -->
    <div class="pagination-wrap">
      <page:pagination-manager />
  </div>
</div>
<div class="btn_all btn_right">
    <div class="flex_box gap-08">
        <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
        <!-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="reSunap()">재수납확인</button> -->
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

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">

$(function() {
	initShLocgovCode();

  searchEnterKey();

  Common.DateButtonEvent.set('.day_btn1 > a[class^=btn_date]', '', 'input[name="shCntrDeStart"]' , 'input[name="shCntrDeEnd"]');
//  Common.DateButtonEvent.set('.day_btn2 > a[class^=btn_date]', '', 'input[name="shSttemntPayDeStart"]' , 'input[name="shSttemntPayDeEnd"]');
  EventHandler.calendarStartDateAndEndDateVaild();

	$('.shPrice').on('input', function(e) {
		$(this).val(Common.numberFormat($(this).val()));
	});

});


function clickLinkInsttCdAt(){
	$('input:radio[name=shLinkInsttCdAt]:input[value="Y"]').prop("checked", true);
}


function searchClear() {
    $("#shWdr").val("");																// 시도
    $("#shLocgovCode option").remove();													// 시군구
    $('#shLocgovCode').append('<option value="">-시·군·구-</option>');

    $("#shText").val("");																// 검색어
    $("#shKeyword option:eq(0)").prop("selected", true);								// 검색구분 셀렉트박스

    $("#shCntrDeStart").val(<c:out value="${today}"/>);									// 기부일자 검색 시작일
    $("#shCntrDeEnd").val(<c:out value="${today}"/>);									// 기부일자 검색 종료일
    //$("#shSttemntPayDeStart").val("");												// 납부일자 검색 시작일
    //$("#shSttemntPayDeEnd").val("");													// 납부일자 검색 종료일
    $('input:radio[name=shCntrPathCode]:input[value=""]').prop("checked", true);		// 온·오프라인 라디오버튼
    $('input:radio[name=shDsgnDonateAt]:input[value=""]').prop("checked", true);		// 기부대상 라디오버튼
    $('input:radio[name=shCntrStatusCode]:input[value=""]').prop("checked", true);		// 기부상태 라디오버튼
    $('input:radio[name=shLinkInsttCdAt]:input[value=""]').prop("checked", true);		// 기부채널 라디오버튼


    $("#shLinkInsttCd option:eq(0)").prop("selected", true);							// 민간연계기관 셀렉트박스

    $('#cntrAmtStart').val('');															// 기부금액 검색 입력 텍스트박스 시작금액
    $('#cntrAmtEnd').val('');															// 기부금액 검색 입력 텍스트박스 종료금액
    $('#shCntrAmtStart').val('');														// 기부금액 검색 시작금액
    $('#shCntrAmtEnd').val('');															// 기부금액 검색 종료금액

    $('#searchForm').submit();
}

// 지자체 초기값 처리(조회된 값 유지)
function initShLocgovCode(){
	if ($("#shWdr").val() == "") {
		return;
	}

	$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : $("#shWdr").val()}, function(response) {
		$("#shLocgovCode option").remove();

		for (var i = 0; i < response.length; i++) {
            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
            $('#shLocgovCode').append(options);
        }
		// 조회 된 값 유지
		if ("${fn:escapeXml(searchParam.shLocgovCode)}" != "") {
		    $("#shLocgovCode").val('${fn:escapeXml(searchParam.shLocgovCode)}').prop("selected", true);
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
			/* if ("${fn:escapeXml(searchParam.shLocgovCode)}" != "") {
			    $("#shLocgovCode").val('${fn:escapeXml(searchParam.shLocgovCode)}').prop("selected", true);
			} */
	    });
	} else {
        $('#shLocgovCode').append('<option value="">-시·군·구-</option>');
	}
}

function search() {
	var strStartDate 	= $("#shCntrDeStart").val();
	var strEndDate 		= $("#shCntrDeEnd").val();

    if(strStartDate === '' || strEndDate === '') {
        alert("기부일자는 필수 입력입니다.");
        return false;
    }

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate 	= Number(strStartDate);
		var endDate		= Number(strEndDate);

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			var value = $("#shCntrDeEnd").val();
			$("#shCntrDeEnd").val("");
			$("#shCntrDeEnd").focus();
			$("#shCntrDeEnd").val(value);
			return false;
		}
	}

	/* 납부일자 검색조건 추가 2023-02-14 배예림 */
/* 	strStartDate 	= $("#shSttemntPayDeStart").val();
	strEndDate 		= $("#shSttemntPayDeEnd").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate 	= Number(strStartDate);
		var endDate		= Number(strEndDate);

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			var value = $("#shSttemntPayDeEnd").val();
			$("#shSttemntPayDeEnd").val("");
			$("#shSttemntPayDeEnd").focus();
			$("#shSttemntPayDeEnd").val(value);
			return false;
		}
	} */
	/* 추가 끝 */


	/* 기부금액 검색조건 추가 20260414 */
	var cntrAmtStart 	= $("#cntrAmtStart").val().replace(/,/g, "");
	var cntrAmtEnd 		= $("#cntrAmtEnd").val().replace(/,/g, "");

	if(cntrAmtStart && cntrAmtEnd) {
		if(Number(cntrAmtStart) > Number(cntrAmtEnd)) {
			alert("기부금액검색 최대금액이 기부금액검색 최소금액보다 작습니다.");
			$("#cntrAmtEnd").val("");
			$("#cntrAmtEnd").focus();
			return false;
		}
	}
	$('#shCntrAmtStart').val(cntrAmtStart);
	$('#shCntrAmtEnd').val(cntrAmtEnd);
	/* 추가 끝 */

	$("#shText").val($("#shText").val().trim());

	$("#searchForm").submit();
}

/**
 *	함 수 명 : searchEnterKey
 *	기	능  : 검색 엔터키 이벤트
 */
function searchEnterKey() {
	$("#shText, #shCntrDeStart, #shCntrDeEnd").on('keydown', function(e){
		if (e.keyCode == '13') {
			$("#shText").val($("#shText").val().trim());
			$("#searchForm").submit();
		}
	});
}

/**
 *	함 수 명 : downloadExcel
 *	기	능  : 기부금 전체현황 엑셀 다운로드
 */
function downloadExcel() {
	Shop.downloadExcelOrder("/opmanager/give/give-state/detail_list/download-excel", $('#searchForm').serialize(), true);
}

/**
 *	함 수 명 : giveModifyPopup
 *	기	능  : 기부내역변경 상세 팝업
 */
function giveModifyPopup(elctrnPayNo) {
	var url = "/opmanager/give/give-state/popup/giveModifyInfo/"+elctrnPayNo;
	var popupName = "/opmanager/give/give-state/popup/giveModifyInfo";
	Common.popup(url, popupName, 800, 600, 1, 0, 0);
}

/**
 * 취소된건에 대해 재수납확인
 */
function reSunap() {
	let param = {};
	let shCntrDeStart = $("#shCntrDeStart").val();
	let shCntrDeEnd = $("#shCntrDeEnd").val();

	param.shCntrDeStart = shCntrDeStart;
	param.shCntrDeEnd = shCntrDeEnd;
	param.shKeyword = $('#shKeyword').val();
	param.shLocgovCode = $('#shLocgovCode').val();
	param.shText = $('#shText').val();

	let startDt = new Date(shCntrDeStart.substring(0,4)+'-'+shCntrDeStart.substring(4,6)+'-'+shCntrDeStart.substring(6,8));
	let endDt = new Date(shCntrDeEnd.substring(0,4)+'-'+shCntrDeEnd.substring(4,6)+'-'+shCntrDeEnd.substring(6,8));
	let diff = Math.abs(endDt.getTime() - startDt.getTime());
	diff = Math.ceil(diff / (1000 * 60 * 60 * 24));
	if(diff > 31) {
		alert('날짜기간이 너무 큽니다. 한달 이내로 확인하십시오.');
		return false;
	}

	let request = $.ajax({
	  	url: "/opmanager/give/give-state/reConfirmSunap",
		method: "POST",
		data: JSON.stringify(param),
		contentType: 'application/json; charset=utf-8',
		dataType: "json"
	});

	request.done(function( res ) {
		alert(res.msg);
	});

	request.fail(function( jqXHR, textStatus ) {
	  	alert( "Request failed: " + textStatus );
	});

}

function ntsList(elctrnPayNo){
	var url = "/opmanager/give/give-state/popup/ntsList/"+elctrnPayNo;
	var popupName = "/opmanager/give/give-state/popup/ntsList";
	Common.popup(url, popupName, 1000, 600, 1, 0, 0);
}
</script>


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

<form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="get" id="searchParam">
    <form:hidden path="locgovFullNm" />

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
                <td class="label">지자체명</td>
                <td colspan="">
                    <div>
                        <c:out value='${locgovFullNm}'/>
                        <form:input type="hidden" path="shLocgovCode" value="${fn:escapeXml(searchParam.shLocgovCode)}" />
                    </div>
                </td>
                <td class="label">검색구분</td>
                <td colspan="">
                    <div class="flex_box gap-08">
                        <ul class="input-group col-3">
                            <li class="">
                                <form:select path="shKeyword" title="기부자명" class="wd-160">
                                	<form:option value="USER_NAME">기부자명</form:option>
                                    <form:option value="LOGIN_ID">기부자ID</form:option>
                                    <form:option value="ELCTRN_PAY_NO">전자납부번호</form:option>
                                    <form:option value="PRJ_SUBJECT">특정사업명</form:option>
                                </form:select>
                            </li>
                            <li class="input">
                                <form:input path="shText" title="검색구분" class="input_txt required _filter half" type="text" value="${fn:escapeXml(searchParam.shText)}" />
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
                        	<a href="javascript:void(0);" class="btn_date yesterday"><c:out value="${op:message('M01701')}"/></a>
                            <a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
                            <a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
                            <!-- <a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>  -->
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
                            <form:select path="shLinkInsttCd" class="wd-150" onClick="clickLinkInsttCdAt()" style="margin-left:5px">
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
            	<td class="label">잔여포인트</td>
                <td colspan="">
                    <div class="flex_box gap-12">
                        <div class="input-form">
                            <form:radiobutton path="shCntrBlcePointAt" value="" label="전체" checked="checked"/>
                        </div>
                        <div class="input-form">
                            <form:radiobutton path="shCntrBlcePointAt" value="Y" label="있음"/>
                        </div>
                        <div class="input-form" style="margin-left:12px;">
                            <form:radiobutton path="shCntrBlcePointAt" value="N" label="없음"/>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
            	<td class="label">기부금액</td>
                <td colspan="">
                    <div>
                        <ul class="input-group col-3">
                            <li class="input"><input id="cntrAmtStart" title="기부금액검색 최소금액" type="text" class="input_txt _filter shPrice" size="20" value="${op:numberFormat(searchParam.shCntrAmtStart)}"></li>
                            <li><label for="cntrAmtStart">원</label></li>
                            <li class="text">~</li>
                            <li class="input"><input id="cntrAmtEnd" title="기부금액검색 최대금액" type="text" class="input_txt _filter shPrice" size="20" value="${op:numberFormat(searchParam.shCntrAmtEnd)}"></li>
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
            <tr>
            	<td class="label">SMS수신여부</td>
                <td colspan="3">
                    <div class="flex_box gap-12">
                        <div class="input-form">
                            <form:radiobutton path="shPbancAt" value="" label="전체" checked="checked"/>
                        </div>
                        <div class="input-form">
                            <form:radiobutton path="shPbancAt" value="Y" label="동의"/>
                        </div>
                        <div class="input-form" style="margin-left:12px;">
                            <form:radiobutton path="shPbancAt" value="N" label="미동의"/>
                        </div>
                    </div>
                </td>
                <!-- <td class="label" colspan='1'><div></div></td>
                <td colspan=""></td> -->
            </tr>
        </tbody>
    </table>
    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="searchClear()">초기화</button>
            <!-- <button type="submit" class="btn btn-dark-gray btn-mini">검색</button> -->
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
        </div>
    </div>
</div>
<div class="flex_box gap-20">
    <div class="board_write">
        <h3 class="mt50 fs24"><span>누적 합계</span></h3>
        <table class="board_write_table" summary="기부모금액">
            <colgroup>
                <col>
                <col>
                <col>
                <col>
            </colgroup>
            <tbody>
                <tr>
                    <td class="label">기부금액</td>
                    <td>
                        <div>
                            <c:out value='${op:numberFormat(currentSum.cntrAmt)}'/>
                        </div>
                    </td>
                    <td class="label">기부인원</td>
                    <td>
                        <div>
                            <c:out value='${op:numberFormat(currentSum.givePersons)}'/>
                        </div>
                    </td>
                    <td class="label">발생포인트</td>
                    <td>
                        <div>
                            <c:out value='${op:numberFormat(currentSum.cntrPoint)}'/>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="board_write">
        <h3 class="mt50 fs24"><span>검색 합계</span></h3>
        <table class="board_write_table" summary="기부모금액">
            <colgroup>
                <col>
                <col>
                <col>
                <col>
            </colgroup>
            <tbody>
                <tr>
                    <td class="label">기부모금액</td>
                    <td>
                        <div>
                            <c:out value='${op:numberFormat(sum.cntrAmt)}'/>
                        </div>
                    </td>
                    <td class="label">기부인원</td>
                    <td>
                        <div>
                            <c:out value='${op:numberFormat(sum.givePersons)}'/>
                        </div>
                    </td>
                    <td class="label">발생포인트</td>
                    <td>
                        <div>
                            <c:out value='${op:numberFormat(sum.cntrPoint)}'/>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
<div class="count_title mt-40">
    <h5>총 <c:out value='${count}'/>건&nbsp;&nbsp;<font color="red">전자적 전송매체를 이용한 모금 관련 시행령 개정('24.8.21.)에 따라, 기부자의 SMS 수신 동의 여부를 추가하였으니 모금활동에 참고해 주시기 바랍니다.</font></h5>
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
	<div class="board_list" style="overflow-x:scroll;">
	    <table class="board_list_table" summary="기부금 모금 상세현황" style="width:max-content;">
	        <caption>기부금 모금 상세현황</caption>
	        <colgroup>
	            <col style="width:50px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col>
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	        </colgroup>
	        <thead>
	            <tr>
	                <th scope="col">No.</th>
	                <th scope="col">기부일시</th>
	                <th scope="col">납부일자</th>
	                <th scope="col">전자납부번호</th>
	                <th scope="col">국세청</th>
	                <th scope="col">기부자명</th>
	                <th scope="col">아이디</th>
	                <th scope="col">생년월일</th>
	                <th scope="col">핸드폰번호</th>
	                <th scope="col">SMS수신여부</th>
	                <th scope="col">거소지자체</th>
	                <th scope="col">특정사업명</th>
	                <th scope="col">기부금액</th>
	                <th scope="col">발생포인트</th>
	                <th scope="col">잔여포인트</th>
	                <th scope="col">답례품</th>
	                <th scope="col">기부형태</th>
	                <th scope="col">민간연계기관</th>
	                <th scope="col">변경일시</th>
	                <th scope="col">변경</th>
	            </tr>
	        </thead>
	        <tbody>
	            <c:forEach items="${list}" var="list" varStatus="i">
	            <tr style="background:#fff;">
	                <td>
	                    <c:out value='${pagination.itemNumber - i.count}'/>
	                </td>
	                <td>
	                    <c:out value='${list.cntrDe}'/>
	                </td>
	                <td>
	                    <c:out value='${list.sttemntPayDe}'/>
	                </td>
	                <td>
	                    <c:out value='${list.elctrnPayNo}'/>
	                </td>
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
                    <!-- <c:choose>
	                 	<c:when test="${list.ntsSttusMssage eq 'SUCCESS'}">
	                 		<div>처리완료</div>
	                 	</c:when>
	                 	<c:otherwise>
	                 		<div><font>미처리</font></div>
	                 	</c:otherwise>
	                </c:choose> -->
	                <!-- <c:out value='${list.ntsSttusMssage}'/> -->
	                </td>
	                <td>
	                    <c:out value='${list.userName}'/>
	                </td>
	                <td>
	                    <c:out value='${list.loginId}'/>
	                </td>
	                <td>
	                    <c:out value='${list.birthday}'/>
	                </td>
	                <td>
	                    <c:out value='${list.phoneNumber}'/>
	                </td>
	                <td>
	                    <c:out value='${list.pbancName}'/>
	                </td>
	                <td>
	                    <c:out value='${list.psitnLocgovName}'/>
	                </td>
	                <td>
	                	<c:choose>
	                		<c:when test="${list.prjId eq 0}">자치단체기부</c:when>
	                		<c:otherwise><c:out value='${list.prjSubject}'/></c:otherwise>
	                	</c:choose>
	                </td>
	                <td>
	                    <c:out value='${op:numberFormat(list.cntrAmt)}'/>
	                </td>
	                <td>
	                    <c:out value='${op:numberFormat(list.cntrPoint)}'/>
	                </td>
	                <td>
	                    <c:out value='${op:numberFormat(list.cntrBlcePoint)}'/>
	                </td>
	                <td>
	                    <c:out value='${list.rtnpsntReqstCode == 100 ? "제공받음" : "제공받지않음"}'/>
	                </td>
	                <td>
	                    <c:out value='${list.cntrPathName}'/>
	                </td>
	                <td>
	                    <c:out value='${list.detail}'/>
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
	                    	<%-- cntrReqmngCode: 기부금변경신청 코드. 100: 과오납 | 200: 포인트 생성 | 300: 배치동작처리 | 400:수납취소 --%>
	                    	<%-- reqStatusCode: 변경요청 상태코드. 100: 변경신청요청 | 200: 변경신청 취소 | 999: 변경승인 --%>
		                 	<c:when test="${empty list.reqStatusCode}">
		                 		<button type="button" class="btn btn-dark-gray btn-mini" value ='${fn:escapeXml(list.elctrnPayNo)}' onclick="toReq(this.value)">변경</button>
		                 	</c:when>
		                 	<c:when test="${list.cntrReqmngCode eq '200' && list.reqStatusCode eq '999'}">
		                 		<button type="button" class="btn btn-dark-gray btn-mini" value ='${fn:escapeXml(list.elctrnPayNo)}' onclick="toReq(this.value)">변경</button>
		                 	</c:when>
	                 		<c:when test="${list.reqStatusCode eq '100'}">
			                 	<span>
			                 		승인대기중<br>(<c:out value='${list.cntrReqmngCodeName}'/>)
			                 	</span>
	                 		</c:when>
	                 		<c:when test="${list.cntrReqmngCode == 100 && list.reqStatusCode == 999}">
			                 	<span>
			                 		승인됨<br>(<c:out value='${list.cntrReqmngCodeName}'/>)
			                 	</span>
	                 		</c:when>
							<c:otherwise>
		                 			<button type="button" class="btn btn-dark-gray btn-mini" value ='${fn:escapeXml(list.elctrnPayNo)}' onclick="toReq(this.value)">변경</button>
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
	</div>
    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
            <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/give/give-state/list'">목록</button>
        </div>
    </div>
    <div class="pagination-wrap">
	    <page:pagination-manager />
	</div>
</div>
  <!-- // 개발 영역 -->

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">

$(function() {
	$(".contents_inner").find("h3 span:first").text("기부금 모금 상세현황");
	$(".contents_inner").find("div.location a").removeClass("on");
	$(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="shCntrDeStart"]' , 'input[name="shCntrDeEnd"]');
	EventHandler.calendarStartDateAndEndDateVaild();

	$("#shText, #shCntrDeStart, #shCntrDeEnd").on('keydown', function(e){
		if (e.keyCode == '13') {
			$("#shText").val($("#shText").val().trim());
			search();
		}
	});

	$('.shPrice').on('input', function(e) {						// 검색 기부금액 입력 이벤트()
		$(this).val(Common.numberFormat($(this).val()));
	});

});

// 기부채널-민간연계기관 라디오 버튼 클릭 이벤트
function clickLinkInsttCdAt(){
	$('input:radio[name=shLinkInsttCdAt]:input[value="Y"]').prop("checked", true);
}

// 검색조건 초기화
function searchClear() {
    $("#shKeyword option:eq(0)").prop("selected", true);								// 검색구분 셀렉트박스
	$("#shText").val("");																// 검색어

    $("#shCntrDeStart").val("");														// 기부일자 검색 시작일
    $("#shCntrDeEnd").val("");															// 기부일자 검색 종료일
    $('input:radio[name=shLinkInsttCdAt]:input[value=""]').prop("checked", true);		// 기부채널 라디오버튼
    $("#shLinkInsttCd option:eq(0)").prop("selected", true);							// 기부채널 민간연계기관 셀렉트박스

    $('input:radio[name=shCntrPathCode]:input[value=""]').prop("checked", true);		// 온·오프라인 라디오버튼
    $('input:radio[name=shCntrBlcePointAt]:input[value=""]').prop("checked", true);		// 잔여포인트 라디오버튼

    $('#cntrAmtStart').val('');															// 기부금액 검색 입력 텍스트박스 시작금액
    $('#cntrAmtEnd').val('');															// 기부금액 검색 입력 텍스트박스 종료금액
    $('#shCntrAmtStart').val('');														// 기부금액 검색 시작금액
    $('#shCntrAmtEnd').val('');															// 기부금액 검색 종료금액
    $('input:radio[name=shDsgnDonateAt]:input[value=""]').prop("checked", true);		// 기부대상 라디오버튼

    $('input:radio[name=shPbancAt]:input[value=""]').prop("checked", true);				// SMS 수신여부 라디오버튼

    //$("#shSttemntPayDeStart").val("");												// 납부일자 검색 시작일
    //$("#shSttemntPayDeEnd").val("");													// 납부일자 검색 종료일

    $('#searchParam').submit();
}

function downloadExcel() {
    Shop.downloadExcelOrder("/opmanager/give/give-state/detail/download-excel", $('#searchParam').serialize(), true);
}

function toReq(elctrnPayNo){
	if(confirm('기부취소(과오납)를 하시려면\n세외수입시스템에서 먼저 취소 처리를 하셔야 합니다.\n변경하시겠습니까?')){
		window.location="/opmanager/give/give-state/req_form/"+elctrnPayNo;
	}
}

/**
 *	함 수 명 : search
 *	기	능  : 검색
 */
function search() {
	var strStartDate = $("#shCntrDeStart").val();
	var strEndDate = $("#shCntrDeEnd").val();

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
		var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

		if(startDate > endDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			var value = $("#shCntrDeStart").val();
			$("#shCntrDeEnd").val("");
			$("#shCntrDeEnd").focus();
			$("#shCntrDeEnd").val(value);
			return false;
		}
		var searchChk = Common.searchDateMonth(startDate, endDate);
		if(!searchChk) return false;
	}

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

	$("#searchParam").submit();
}

function ntsList(elctrnPayNo){
	var url = "/opmanager/give/give-state/popup/ntsList/"+elctrnPayNo;
	var popupName = "/opmanager/give/give-state/popup/ntsList";
	Common.popup(url, popupName, 1000, 600, 1, 0, 0);
}
</script>


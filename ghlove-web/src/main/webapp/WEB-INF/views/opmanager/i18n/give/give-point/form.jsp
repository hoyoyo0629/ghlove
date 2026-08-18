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

<h3><span>기부포인트 상세현황</span></h3>

<!-- <form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="post"> -->
    <!--
    	form:hidden path="변수명"으로 할 경우 name 속성에 의해 검색할 때마다 '서울시청시 시청,서울시청시 시청,서울시청시 시청,...'으로 잡힘
    	form 문법 제거 후 기존 HTML 문법으로 변경하여 해결 완료
    -->
	<form:hidden path="query"/>
<div class="board_write">
	<table class="board_write_table" summary="">
	    <colgroup>
	        <col style="width:220px;">
	    </colgroup>
	    <tbody>
        <tr>
            <td class="label">지자체명</td>
            <td>
                <div>
                    <c:out value='${locgovFullNm}'/>
                    <!--
				    	form:hidden path="변수명"으로 할 경우 name 속성에 의해 검색할 때마다 '11000,11000,11000,...'으로 잡힘
				    	form 문법 제거 후 기존 HTML 문법으로 변경하여 해결 완료
				    -->
                </div>
            </td>
            <td class="label">기부 년도</td>
            <td>
	        	<div class="flex_box gap-08 item-center">
	        		<!--
				    	form:select path="변수명"으로 할 경우 name 속성에 의해 검색할 때마다 '2025,2025,2025,...'으로 잡힘
				    	form 문법 제거 후 기존 HTML 문법으로 변경하여 해결 완료
				    -->
	            	<select id="shCntrYear" title="기부년도" class="wd-150" name="shCntrYear">
				    	<c:forEach items="${yyyy}" var="yyyy">
					    	<option value="${yyyy.id}" label="${yyyy.label}" ${fn:escapeXml(searchParam.shCntrYear) eq yyyy.id ? 'selected' : ''}/>
					    </c:forEach>
			        </select>
	            </div>
	        </td>
		</tr>
        <tr>
            <td class="label">검색구분</td>
            <td colspan="3">
                <div class="flex_box gap-08">
                    <form:select path="" title="기부자명" class="wd-150">
                           <form:option value="">기부자명</form:option>
                       </form:select>
                       <form:input path="shUserName" title="검색구분" class="input_txt required _filter half"
                           type="text" value="${fn:escapeXml(searchParam.shUserName)}" />
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="searchClear()">초기화</button>
               <button class="btn btn-dark-gray btn-mini" onclick="formSubmit()">검색</button>
        </div>
    </div>
</div>
<div class="flex_box gap-20">
    <div class="board_write">
        <h3 class="mt50 fs24"><span>합계</span></h3>
        <table class="board_write_table" summary="기부모금액">
            <colgroup>
            <col>
            <col>
            <col>
            <col>
        </colgroup>
        <tbody>
            <tr>
                <td class="label">총 발생포인트</td>
                <td>
                    <div>
                        <c:out value='${op:numberFormat(totalSum.cntrPoint)}'/>
                    </div>
                </td>
                <td class="label">총 사용포인트</td>
                <td>
                    <div>
                        <c:out value='${op:numberFormat(totalSum.cntrUsePoint)}'/>
                    </div>
                </td>
                <td class="label">총 포인트잔액</td>
                <td>
                    <div>
                        <c:out value='${op:numberFormat(totalSum.cntrBlcePoint)}'/>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label"><strong><c:out value='${searchParam.shCntrYear}'/>년</strong> 포인트</td>
                <td>
                    <div>
                        <c:out value='${op:numberFormat(cntrSum.cntrPoint)}'/>
                    </div>
                </td>
                <td class="label"><strong><c:out value='${searchParam.shCntrYear}'/>년</strong> 사용포인트</td>
                <td>
                    <div>
                        <c:out value='${op:numberFormat(cntrSum.cntrUsePoint)}'/>
                    </div>
                </td>
                <td class="label"><strong><c:out value='${searchParam.shCntrYear}'/>년</strong> 포인트잔액</td>
                <td>
                    <div>
                        <c:out value='${op:numberFormat(cntrSum.cntrBlcePoint)}'/>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</div>
<div class="count_title mt-40">
    <h5>총 : <c:out value='${count}'/>건</h5>
    <span>
        <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();"> <!-- 화면출력 -->
		    <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
		    <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
		    <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
		    <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
		</form:select>
    </span>
</div>
<!-- </form:form> -->

<div class="board_list">
    <table class="board_list_table" summary="기부포인트 현황">
        <caption>기부포인트 현황</caption>
    <colgroup>
        <col style="width:50px;">
        <col style="width:100px;">
        <col style="width:150px;">
        <col style="width:100px;">
        <col style="width:100px;">
        <col style="width:200px;">
        <col style="width:200px;">
        <col style="width:200px;">
        <col style="width:200px;">
    </colgroup>
    <thead>
        <tr>
            <th scope="col">No.</th>
            <th scope="col">납부일자</th>
            <th scope="col">납부번호</th>
            <th scope="col">기부자명</th>
            <th scope="col">아이디</th>
            <th scope="col">기부금액</th>
            <th scope="col">발생포인트</th>
            <th scope="col">사용포인트</th>
            <th scope="col">포인트잔액</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="list" varStatus="i">
        <tr style="background:#fff;">
            <td>
                <c:out value='${pagination.itemNumber - i.count}'/>
            </td>
            <td>
                <c:out value='${op:date(list.sttemntPayDe)}'/>
            </td>
            <td>
                <c:out value='${list.elctrnPayNo}'/>
            </td>
            <td>
                <c:out value='${list.userName}'/>
            </td>
            <td>
                <c:out value='${list.loginId}'/>
            </td>
            <td>
                <c:out value='${op:numberFormat(list.cntrAmt)}'/>
            </td>
            <td>
                <c:out value='${op:numberFormat(list.cntrPoint)}'/>
            </td>
            <td>
                <c:out value='${op:numberFormat(list.cntrUsePoint)}'/>
            </td>
            <td>
                <c:out value='${op:numberFormat(list.cntrBlcePoint)}'/>
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
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
           <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/give/give-point/list'">목록</button>
        </div>
    </div>
    <div class="pagination-wrap">
        <page:pagination-manager />
    </div>
</div>

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>
<form name="searchParam" id="searchParam" action="/opmanager/give/give-point/detail" method="post">
	<input type="hidden" id="locgovFullNm" name="locgovFullNm" value="${fn:escapeXml(locgovFullNm)}" />
	<input type="hidden" id="shLocgovCode" name="shLocgovCode" value="${fn:escapeXml(searchParam.shLocgovCode)}" />
</form>

<script type="text/javascript">

$(function() {
	$(".contents .contents_inner").find("h3 span:first").text("기부포인트 상세현황");
	$(".contents .contents_inner").find("div.location a").removeClass("on");
	$(".contents .contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');
});

function searchClear() {
    $("#shUserName").val("");

    const locgovFullNm = $('#locgovFullNm').val();
	const shLocgovCode = $('#shLocgovCode').val();
	const shCntrYear = $('#shCntrYear').val();

    let form = document.createElement('form');

    form.innerHTML = '<input name="shCntrYear" type="hidden" value="'+shCntrYear+'">';
    form.innerHTML += '<input name="shLocgovCode" type="hidden" value="'+shLocgovCode+'">';
    form.innerHTML += '<input name="locgovFullNm" type="hidden" value="'+locgovFullNm+'">';
    form.innerHTML += '<input name="shUserName" type="hidden" value="">';
    document.body.append(form);

    form.submit();
}

function formSubmit() {

	const locgovFullNm = $('#locgovFullNm').val();
	const shLocgovCode = $('#shLocgovCode').val();
	const shCntrYear = $('#shCntrYear').val();
	const shUserName = $('#shUserName').val();
    let form = document.createElement('form');
    form.action = '/opmanager/give/give-point/detail';
    form.method = 'post';

    form.innerHTML = '<input name="shCntrYear" type="hidden" value="'+shCntrYear+'">';
    form.innerHTML += '<input name="shLocgovCode" type="hidden" value="'+shLocgovCode+'">';
    form.innerHTML += '<input name="locgovFullNm" type="hidden" value="'+locgovFullNm+'">';
    form.innerHTML += '<input name="shUserName" type="hidden" value="'+shUserName+'">';
    document.body.append(form);

    form.submit();
}

function downloadExcel() {
	const locgovFullNm = $('#locgovFullNm').val();
	const shLocgovCode = $('#shLocgovCode').val();
	const shCntrYear = $('#shCntrYear').val();
	const shUserName = $('#shUserName').val();

    let form = document.createElement('form');
    form.action = '/opmanager/give/give-point/detail';
    form.method = 'post';

    form.innerHTML = '<input name="shCntrYear" type="hidden" value="'+shCntrYear+'">';
    form.innerHTML += '<input name="shLocgovCode" type="hidden" value="'+shLocgovCode+'">';
    form.innerHTML += '<input name="locgovFullNm" type="hidden" value="'+locgovFullNm+'">';
    form.innerHTML += '<input name="shUserName" type="hidden" value="'+shUserName+'">';
    document.body.append(form);

    Shop.downloadExcelOrder("/opmanager/give/give-point/detail/download-excel", $(form).serialize(), true);
}

</script>


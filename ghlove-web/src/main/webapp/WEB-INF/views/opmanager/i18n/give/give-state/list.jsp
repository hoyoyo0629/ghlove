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

<h3><span>기부금 모금현황</span></h3>

<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="isSubmit"/>
	<form:hidden path="query"/>
<fieldset>
	<!-- 시스템/운영자 화면 case -->
	<div class="board_write">
	  <table class="board_write_table" summary="기부금 모금현황">
	    <colgroup>
	      <col style="width:220px;">
	      <col>
	      <col style="width:220px;">
	      <col>
	    </colgroup>
	    <tbody>
	      <tr>
	        <td class="label">년도</td>
	        <td>
	          <div>
	            <form:select path="shCntrYear" title="구분" class="wd-150">
	              <form:option value="">전체</form:option>
	              <c:forEach items="${yyyy}" var="yyyy">
	                  <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}" />
	              </c:forEach>
	            </form:select>
	          </div>
	        </td>
	        <td class="label">지자체</td>
	        <td>
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
	      <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/give/give-state/list';">초기화</button>
	      <button type="submit" class="btn btn-dark-gray btn-mini" >검색</button>
	    </div>
	  </div>
	</div>


	<h3 class="mt50 fs24"><span>합계</span></h3>
	<div class="board_write">
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
	        <td class="label">답례품 금액</td>
	        <td>
	          <div>
	            <c:out value='${op:numberFormat(sum.cntrUsePoint)}'/>
	          </div>
	        </td>
	      </tr>
	    </tbody>
	  </table>
	</div>

	<div class="count_title mt-40">
	  <h5>총 <c:out value='${count}'/>건</h5>
	  <span>
	   <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();"> <!-- 화면출력 -->
	       <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
	       <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
	       <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
	       <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
	   </form:select>
	  </span>
	</div>

</fieldset>
</form:form>

<div class="board_list">
  <table class="board_list_table" summary="기부금 모금현황">
    <caption>기부금 모금현황</caption>
    <colgroup>
      <col style="width:5%;">
      <col style="width:5%;">
    </colgroup>
    <thead>
      <tr>
        <th scope="col">No.</th>
        <th scope="col">년도</th>
        <th scope="col">광역지자체</th>
        <th scope="col">기부지자체</th>
        <th scope="col">기부모금액</th>
        <th scope="col">기부건수</th>
        <th scope="col">기부인원</th>
        <th scope="col">발생포인트</th>
        <th scope="col">답례품 금액</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${list}" var="list" varStatus="i">
      <tr style="background:#fff;">
        <td>
          <c:out value='${pagination.itemNumber - i.count}'/>
        </td>
        <td>
          <c:out value='${list.cntrYear}'/>
        </td>
        <td>
          <a href="javascript:detail('${fn:escapeXml(list.cntrYear)}', '${fn:escapeXml(list.upperLocgovCode)}', '${fn:escapeXml(list.locgovCode)}', '${fn:escapeXml(list.upperLocgovNm)}', '${fn:escapeXml(list.locgovNm)}')"><c:out value='${list.upperLocgovNm}'/></a>
        </td>
        <td>
          <a href="javascript:detail('${fn:escapeXml(list.cntrYear)}', '${fn:escapeXml(list.upperLocgovCode)}', '${fn:escapeXml(list.locgovCode)}', '${fn:escapeXml(list.upperLocgovNm)}', '${fn:escapeXml(list.locgovNm)}')"><c:out value='${list.locgovNm}'/></a>
        </td>
        <td>
          <a href="javascript:detail('${fn:escapeXml(list.cntrYear)}', '${fn:escapeXml(list.upperLocgovCode)}', '${fn:escapeXml(list.locgovCode)}', '${fn:escapeXml(list.upperLocgovNm)}', '${fn:escapeXml(list.locgovNm)}')"><c:out value='${op:numberFormat(list.cntrAmt)}'/></a>
        </td>
        <td>
          <c:out value='${op:numberFormat(list.giveCnt)}'/>
        </td>
        <td>
          <c:out value='${op:numberFormat(list.givePersons)}'/>
        </td>
        <td>
          <c:out value='${op:numberFormat(list.cntrPoint)}'/>
        </td>
        <td>
          <c:out value='${op:numberFormat(list.cntrUsePoint)}'/>
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
<!--   <sec:authorize access="hasRole('ROLE_EXCEL')"> -->
  <div class="btn_all btn_right">
    <div class="flex_box gap-08">
      <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
    </div>
  </div>
<!--   </sec:authorize> -->

  <div class="pagination-wrap">
      <page:pagination-manager />
  </div>
</div>
<!-- // 시스템/운영자 화면 case -->

<form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form>

<script type="text/javascript">

$(function() {
    wdrChange($("#shWdr").val());

    let f = document.forms[0];

    f.addEventListener('submit',(event)=>{
       	event.preventDefault();
       	$('#isSubmit').val("true");
		f.submit();
    });
});



// 지자체 변경
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

// 상세화면으로 이동
function detail(cntrYear, upperLocgovCode, locgovCode, upperLocgovNm, locgovNm) {
	var locgovFullNm = upperLocgovNm + ' ' + locgovNm;
	var url = "?shCntrYear="+cntrYear+"&shWdr="+upperLocgovCode+"&shLocgovCode="+locgovCode+"&locgovFullNm="+locgovFullNm;
	location.href = '/opmanager/give/give-state/detail'+url;
}

function downloadExcel() {
    Shop.downloadExcelOrder("/opmanager/give/give-state/list/download-excel", $('#searchParam').serialize(), true);
}

function yearChagne(){
	var date = new Date();
	var setYear = date.getFullYear();
}

</script>

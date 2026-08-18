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

<h3><span>기부금 운영현황</span></h3>
<form:form modelAttribute="searchParam" method="post">
<form:hidden path="query"/>
    <div class="board_write">
        <table class="board_write_table">
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
                            <form:select path="shCntrYear" title="년도" class="wd-150">
                                <c:forEach items="${yyyy}" var="yyyy">
                                    <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
                                </c:forEach>
                            </form:select>
                        </div>
                    </td>
                    <td class="label">지자체</td>
                    <td>
                        <div class="flex_box gap-08">
                            <c:choose>
                                <c:when test="${locgovFullNm ne null}">
                                    <span><c:out value="${locgovFullNm}"/></span>
                                </c:when>
                                <c:otherwise>
                                    <form:select path="shWdr" title="지자체" class="wd-150" onChange="wdrChange(this.value)">
                                        <form:option value="">-시,도 선택-</form:option>
                                        <c:forEach items="${wdr}" var="wdr">
                                            <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
                                        </c:forEach>
                                    </form:select>
                                    <form:select path="shLocgovCode" title="지자체" class="wd-150">
                                        <option value="">-시,군,구-</option>
                                    </form:select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="btn_all btn_right">
            <div class="flex_box gap-08">
                <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/give/statistics/operate';">초기화</button>
                <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
            </div>
        </div>
    </div>

    <div class="count_title mt-40 flex_box juc-sbt item-center">
        <h5>총 <c:out value="${op:numberFormat(count)}"/>건</h5>
        <div class="flex_box gap-08">
            <span>
                <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();" > <!-- 화면출력 -->
                    <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
                    <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
                    <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
                    <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
                </form:select>
            </span>
        </div>
    </div>
</form:form>

<div class="board_list">
  <table class="board_list_table">
      <caption></caption>
      <colgroup>
            <col style="width:50px;">
            <col style="width:300px;">
            <c:forEach items="${useList}" var="list" varStatus="i">
                <col style="width:200px;">
            </c:forEach>
            <col style="width:100px;">
      </colgroup>
      <thead>
          <tr>
            <th scope="col">No.</th>
            <th scope="col">지자체</th>
            <c:forEach items="${useList}" var="list" varStatus="i">
                <th scope="col"><c:out value="${list.label}" /></th>
            </c:forEach>
            <th scope="col">합계</th>
          </tr>
      </thead>
      <tbody>
        <c:forEach items="${list}" var="list" varStatus="i">
            <tr style="background:#fff;">
                <td>
                    <div><c:out value='${pagination.itemNumber - i.count}'/></div>
                </td>
                <td>
                    <div><a href="javascript:detail('${fn:escapeXml(list.locgovCode)}')"><c:out value='${list.upperLocgovNm} ${list.locgovNm}'/></a></div>
                </td>
                <c:forEach items="${useList}" var="code" varStatus="i">
                    <c:set var="amtName">amt<c:out value="${code.id}"/></c:set>
                    <td>
                        <div><c:out value="${op:numberFormat(list[amtName])}" /></div>
                    </td>
                </c:forEach>
                <td>
                    <div><c:out value="${op:numberFormat(list.expndtrSum)}" /></div>
                </td>
            </tr>
        </c:forEach>
      </tbody>
  </table>
  <div class="btn_all btn_left">
      <div class="flex_box gap-08">
          <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
      </div>
  </div>
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
        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
	}
}

function detail(locgovCode) {
    location.href="/opmanager/give/statistics/operate/" + locgovCode;
}

function downloadExcel() {
    Shop.downloadExcelOrder("/opmanager/give/statistics/operate/excel", $('#searchParam').serialize(), true);
}



</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<!-- 개발 영역 -->
<h3><span>답례품 선호도</span></h3>
<form:form modelAttribute="wishlistParam" method="post">
<form:hidden path="query"/>

<div class="board_write">
    <table class="board_write_table">
        <colgroup>
            <col style="width:220px;">
            <col>
        </colgroup>
        <tbody>
            <tr>
                <td class="label">지자체</td>
                <td>
                    <c:if test="${role == 'mois'}">
                    <div class="flex_box gap-08">
                        <form:select path="upperLocgovCode" title="지자체" class="wd-150" onChange="wdrChange(this.value)">
                            <form:option value="">-시,도 선택-</form:option>
                            <c:forEach items="${wdr}" var="wdr">
                                <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
                            </c:forEach>
                        </form:select>
                        <form:select path="locgovCode" title="지자체" class="wd-150">
                            <option value="">-시,군,구-</option>
                        </form:select>
                    </div>
                    </c:if>
                    <c:if test="${role != 'mois'}">
                        <div class="flex_box gap-08">
                            ${fn:escapeXml(locgovNm)}
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
               <td class="label">답례품명</td>
               <td>
                   <div class="flex_box gap-08">
                       <form:input path="itemName" cssClass="input_txt required _filter full" title="${op:message('M00021')}"/>
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
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/wish/list';">초기화</button>
            <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
        </div>
    </div>
</div>


<div class="count_title mt-40 flex_box juc-sbt item-center">
  <h5>총 <c:out value='${count}'/>건</h5>
  <div class="flex_box gap-08">
   <span>
    <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#wishlistParam').submit();"> <!-- 화면출력 -->
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
        <col style="width:100px;">
        <col style="width:200px;">
        <col style="width:100px;">
        <col style="width:100px;">
        <c:if test="${role == 'mois'}">
            <col style="width:100px;">
        </c:if>
    </colgroup>
    <thead>
        <tr>
            <th scope="col">No.</th>
            <th scope="col">등록수</th>
            <th scope="col">답례품명</th>
            <th scope="col">상호명</th>
            <th scope="col">금액</th>
            <c:if test="${role == 'mois'}">
                <th scope="col">지자체</th>
            </c:if>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${dateList}" var="list" varStatus="i">
        <tr style="background:#fff;">
            <td>
                <div><c:out value='${pagination.itemNumber - i.count}'/></div>
            </td>
            <td>
                <div>
                    <c:out value='${list.registrationNumber}'/>
                </div>
            </td>
            <td class="tleft">
                <div>
                    <a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(list.itemCode)}" target="_blank"><c:out value='${list.itemName}'/></a>
                </div>
            </td>
            <td>
                <div>
                    <c:out value='${list.companyName}'/>
                </div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(list.salePrice)}'/></div>
            </td>
            <c:if test="${role == 'mois'}">
            <td>
                <div><c:out value='${list.locgovNm}'/></div>
            </td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
    </table>
    <c:if test="${empty dateList}">
        <div class="no_content">
            <c:out value="${op:message('M00473')}"/>
        </div>
    </c:if>
<!--     <div class="btn_all flex_box juc-sbt"> -->
<!--         <div class="flex_box gap-08"> -->
<!--             <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button> -->
<!--         </div> -->
<!--     </div> -->

    <div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadExcel()">엑셀</button>
        </div>
    </div>

    <div class="pagination-wrap">
      <page:pagination-manager />
  </div>
</div>

<%-- <form name="excelLogForm" id="excelLogForm">
    <input type="hidden" name="uri" value=""/>
    <input type="hidden" name="url" value=""/>
</form> --%>

<!-- // 시스템/운영자 화면 case -->
<script type="text/javascript">
    $(function () {
        $(".contents_inner > h3:first > span").html("답례품 선호도");

        wdrChange($("#upperLocgovCode").val());

//         init();

    });

     // 지자체 변경
    function wdrChange(value) {
    	Common.loading.hide();

        $("#locgovCode option").remove();
        if ($("#upperLocgovCode").val() != "") {
            $.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
                $('#locgovCode').append('<option value="">-시,군,구-</option>');

                for (var i = 0; i < response.length; i++) {
                    var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
                    $('#locgovCode').append(options);
                }

                // 조회 된 값 유지
                if ("${fn:escapeXml(wishlistParam.locgovCode)}" != "") {
                    $("#locgovCode").val('${fn:escapeXml(wishlistParam.locgovCode)}').prop("selected", true);
                }

            });
        } else {
            $('#locgovCode').append('<option value="">-시,군,구-</option>');
        }
    }

    function downloadExcel() {
    	if ("${fn:escapeXml(role)}" == "mois") {
    		Shop.downloadExcelOrder("/opmanager/shop-statistics/wish/list/mois/download-excel", $('#wishlistParam').serialize(), false);		// 조회내역에 개인정보 없어서 팝업뜨지 않도록 수정
    	} else {
    		Shop.downloadExcelOrder("/opmanager/shop-statistics/wish/list/locgov/download-excel", $('#wishlistParam').serialize(), false);		// 조회내역에 개인정보 없어서 팝업뜨지 않도록 수정
    	}
    	alert('다운로드가 시작되었습니다.');
    }

    window.onpageshow = function(event) {
        if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
            // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
            window.location.reload();
        }
    }

</script>

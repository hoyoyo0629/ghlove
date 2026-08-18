<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>


<!-- 개발 영역 -->
<div class="location">
    <a href="">통계</a> &gt; <a href="">답례품 구매현황</a> &gt; <a href="">지자체별</a>
</div>

<h3><span>답례품 구매현황</span></h3>
<form:form modelAttribute="statisticsParam" method="post">
    <form:hidden path="locgovCode" />
	<form:hidden path="orderBy" />
	<form:hidden path="isDesc" />
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
	                    <div class="flex_box gap-08">
	                        <form:select path="year" title="년도" class="wd-150">
	                            <c:forEach items="${yyyy}" var="yyyy">
	                                <form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
	                            </c:forEach>
	                        </form:select>
	                        <select id=startMonth name=locgovSearchMonth title="월" class="wd-150">
	                        	<option value="" label="전체" <c:if test="${statisticsParam.locgovSearchMonth==''}">selected="selected"</c:if> />
	                            <c:forEach begin="1" end="12" var="i">
	                                <option value="${i}" label="${i}월" <c:if test="${statisticsParam.locgovSearchMonth==i}">selected="selected"</c:if> />
	                            </c:forEach>
	                        </select>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	               <td class="label">검색구분</td>
	               <td>
	                   <div class="flex_box gap-08">
	                       <form:select path="where" title="검색구분" class="wd-150">
	                           <form:option value="ITEM_NAME">답례품명</form:option>
	                           <form:option value="COMPANY_NAME">상호명</form:option>
	                       </form:select>
	                       <form:input path="query" cssClass="input_txt required _filter full" title="${op:message('M00021')}"/>
	                   </div>
	               </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="btn_all btn_left">
	  	  <ul class="list-bullet point">
			  <li>검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.</li>
	      </ul>
	    </div>
	    <div class="btn_all btn_right">
	        <div class="flex_box gap-08">
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/sales/locgov?locgovCode=${fn:escapeXml(statisticsParam.locgovCode)}';">초기화</button>
	            <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
	        </div>
	    </div>
	</div>


<div class="count_title mt-40 flex_box juc-sbt item-center">
  <h5>총 <c:out value='${count}'/>건</h5>
  <div class="flex_box gap-08">
   <span>
    <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#statisticsParam').submit();"> <!-- 화면출력 -->
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
        <col style="width:10px;">
        <col style="width:30px;">
        <col style="width:200px;">
        <col style="width:100px;">
        <col style="width:200px;">
        <col style="width:200px;">
        <col style="width:200px;">
    </colgroup>
    <thead>
        <tr>
            <th scope="col" class="col-md-1" rowspan="2">No.</th>
            <th scope="col" class="col-md-2" rowspan="2">답례품 코드</th>
            <th scope="col" class="col-md-2" rowspan="2">답례품명</th>
            <th scope="col" class="col-md-2" rowspan="2">상호명</th>
            <th scope="col" class="col-md-3" colspan="2">주문</th>
            <th scope="col" class="col-md-3" colspan="2">반품/환불</th>
            <th scope="col" class="col-md-3" colspan="2">합계</th>
        </tr>
        <tr>
            <th scope="col" class="col-md-1"><a href="" onclick="javascript:orderBySelect(event, 'orderCase');">건수 <span id="orderCase"></a></span></th>
            <th scope="col" class="col-md-1"><a href="" onclick="javascript:orderBySelect(event, 'orderPoint');">금액(p) <span id="orderPoint"></a></th>
            <th scope="col" class="col-md-1"><a href="" onclick="javascript:orderBySelect(event, 'claimCase');">건수 <span id="claimCase"></a></th>
            <th scope="col" class="col-md-1"><a href="" onclick="javascript:orderBySelect(event, 'claimPoint');">금액(p) <span id="claimPoint"></a></th>
            <th scope="col" class="col-md-1"><a href="" onclick="javascript:orderBySelect(event, 'sumCase');">건수 <span id="sumCase"></a></th>
            <th scope="col" class="col-md-1"><a href="" onclick="javascript:orderBySelect(event, 'sumPoint');">금액(p) <span id="sumPoint"></a></th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${dateList}" var="list" varStatus="i">
    <c:forEach items="${ list.groupStats }" var="item" varStatus="groupIndex">
        <tr style="background:#fff;">
            <td>
                <div><c:out value='${pagination.itemNumber - i.count}'/></div>
            </td>
            <td>
                <div><c:out value='${item.itemUserCode}'/></div>
            </td>
            <td class="tleft">
                <div>
                    <a href="javascript:detailPopupOpen(${fn:escapeXml(list.groupObject)})"><c:out value='${item.itemName}'/></a>
                </div>
            </td>
            <td>
                <div>
                    <c:out value='${item.companyName}'/>
                </div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(item.saleCount)}'/></div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(item.saleAmount)}'/></div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(item.cancelCount)}'/></div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(item.cancelAmount)}'/></div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(item.sumCount)}'/></div>
            </td>
            <td>
                <div><c:out value='${op:numberFormat(item.sumAmount)}'/></div>
            </td>
        </tr>
        </c:forEach>
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
            <c:if test="${role == 'mois'}">
                <button type="button" class="btn btn-default btn-mini" onClick="location.href='/opmanager/shop-statistics/sales/locgov'">목록</button>
            </c:if>
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

<!-- // 시스템/운영자 화면 case -->
<script type="text/javascript">

	let orderBy = "${fn:escapeXml(statisticsParam.orderBy)}";
	let isDesc = ${fn:escapeXml(statisticsParam.isDesc)};

    $(function () {
        $(".contents_inner > h3:first > span").html("답례품 구매현황");

//         init();

		initOrderby();
    });

    function downloadExcel() {
		// 개인정보 없어서 팝업 없이 다운로드 되도록 수정
        Shop.downloadExcelOrder("/opmanager/shop-statistics/sales/locgov/download-excel", $('#statisticsParam').serialize(), false);
    	alert('다운로드가 시작되었습니다.');
    }

    function detailPopupOpen(id) {
        var popupWidth = 700;
        var popupHeight = 580;
        var popupY = (window.screen.height / 2) - (popupHeight / 2);
        var popupX = (window.screen.width / 2) - (popupWidth / 2);
        popupX += window.screenLeft;

        window.open('/opmanager/shop-statistics/sales/locgov/detail?itemId='+id+'&year='+${fn:escapeXml(statisticsParam.year)},'pop','toolbar=no, menubar=no, location=no, resizable=no, scrollbars=no, status=no, titlebar=no, width='+popupWidth+',height='+popupHeight+', left='+ popupX + ', top='+ popupY);
    }

    window.onpageshow = function(event) {
        if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
            // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
            window.location.reload();
        }
    }


    function initOrderby() {
    	if (orderBy) {
            if (isDesc) {
            	$("#" + orderBy).addClass("sort_arrow d_down");
            } else {
            	$("#" + orderBy).addClass("sort_arrow d_up");
            }
    	}
    }

    function orderBySelect(event, tagId) {
    	if (event) {
        	event.preventDefault();
    	}
    	let oldTagId = getOrderbyTagId();
        if (oldTagId == tagId) {
        	isDesc = !isDesc;
        } else {
        	$("#" + oldTagId).removeClass();
        	isDesc = true;
        }
        $("#" + tagId).removeClass();
        if (isDesc) {
        	$("#" + tagId).addClass("sort_arrow d_down");
        } else {
        	$("#" + tagId).addClass("sort_arrow d_up");
        }
        $("#isDesc").val(isDesc);
        $("#orderBy").val(tagId);

        $("#statisticsParam").submit();
    }

    function getOrderbyTagId() {
    	if($("#orderCase").attr('class')) {
    		return "orderCase";
    	} else if ($("#orderPoint").attr('class')) {
    		return "orderPoint";
    	} else if ($("#claimCase").attr('class')) {
    		return "claimCase";
    	} else if ($("#claimPoint").attr('class')) {
    		return "claimPoint";
    	} else if ($("#sumCase").attr('class')) {
    		return "sumCase";
    	} else if ($("#sumPoint").attr('class')) {
    		return "sumPoint";
    	} else {
    		return "";
    	}
    }

</script>

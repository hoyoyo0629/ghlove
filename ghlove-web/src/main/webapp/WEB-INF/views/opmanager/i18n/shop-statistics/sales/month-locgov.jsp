<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>


	<!-- 상단 타이틀 영역 -->
    <!-- 20221114 수정 -->
    <div class="location">
        <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
    </div>
    <h3><span></span></h3>
    <!-- // 20221114 수정 -->
    <!-- // 상단 타이틀 영역 -->

    <!-- 조회 영역 -->
   	<form:form modelAttribute="statisticsParam" method="post">
    	<div class="item_list">
		    <div class="board_write">
		        <table class="board_write_table" summary="월별 지자체별 답례품 현황">
		            <colgroup>
		                <col style="width:220px;">
		                <col>
		                <col style="width:220px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <tr>
		                    <td class="label">지자체</td>
		                    <td>
		                        <div class="flex_box gap-08">
		                            <form:select id="selectSidoCode" name="" title="${op:message('지자체')}" class="wd-150" onchange="javascript:searchSigungu(this);" path="upperLocgovCode"><!-- 지자체 -->
		                                <option value="">-시,도-</option><!-- -시,도- -->
		                                <c:forEach items="${sido}" var="data" varStatus="i" >
		                                	<option value="${fn:escapeXml(data.value)}">${fn:escapeXml(data.label)}</option>
		                                </c:forEach>
		                            </form:select>
		                            <form:select id="selectSigungu" name="" title="${op:message('지자체')}" class="wd-150" path="locgovCode"><!-- 지자체 -->
		                                <option value="">-시,군,구-</option>
		                            </form:select>
		                        </div>
		                    </td>
		                    <td class="label">기간</td>
		                    <td>
		                        <div class="flex_box gap-08" style="align-items: center;">
		                            <form:select path="searchYear" title="${op:message('년도')}" class="wd-150">
		                                <c:forEach items="${yyyy}" var="year" varStatus="i" >
		                                	<option value="${fn:escapeXml(year.value)}" ${year.value == statisticsParam.searchYear ? 'selected': ''}>${fn:escapeXml(year.label)}</option>
		                                </c:forEach>
		                            </form:select>
		                            년
		                            <form:select path="searchMonth" title="${op:message('월')}" class="wd-150">
		                                <c:forEach var="month" varStatus="i" begin="1" end="12">
		                                	<option value="${i.index}" ${i.index == statisticsParam.searchMonth ? 'selected': ''}>${i.index}</option>
		                                </c:forEach>
		                            </form:select>
		                            월
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
		        <div class="flex_box juc-sbt">
			        <div class="btn_all">
			            <div class="flex_box gap-08">
			                <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button><!-- 초기화 -->
			            </div>
			        </div>
			        <div class="btn_all btn_right">
			            <div class="flex_box gap-08">
			                <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/sales/list';">${op:message('M00047')}</button><!-- 초기화 -->
			                <button type="submit" class="btn btn-dark-gray btn-mini" >${op:message('M00048')}</button><!-- 검색 -->
			            </div>
			        </div>
		        </div>
		    </div>
	    </div>
	    <!-- // 조회 영역 -->

	    <!-- 결과 영역 -->
	    <div class="count_title mt-40">
	        <h5>총 ${list.size()} ${op:message('M00272')}</h5>	<!-- 총 n,nnn,nnn건 -->
	        <%-- <span>
	            <form:select path="itemsPerPage" title="화면출력수">
	                <option value="10" ${itemsPerPage == '10' ? 'selected="selected"' : ''}>${op:message('M00240')}</option><!-- 10개 출력 -->
	                <option value="20" ${itemsPerPage == '20' ? 'selected="selected"' : ''}>${op:message('M00241')}</option><!-- 20개 출력 -->
	                <option value="50" ${itemsPerPage == '50' ? 'selected="selected"' : ''}>${op:message('M00242')}</option><!-- 50개 출력 -->
	                <option value="100" ${itemsPerPage == '100' ? 'selected="selected"' : ''}>${op:message('M00243')}</option><!-- 100개 출력 -->
	                <option value="1" ${itemsPerPage == '1' ? 'selected="selected"' : ''}>1개 출력</option>
	            </form:select>
	        </span> --%>
	    </div>
    </form:form>

    <div class="board_list">
        <table class="board_list_table">
            <colgroup>
                <col style="width:120px;">
                <col style="width:100px;">
                <col style="width:150px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:150px;">
                <col style="width:200px;">
                <col style="width:300px;">
                <col style="width:100px;">
            </colgroup>
            <thead>
                <tr>
                    <th scope="col" rowspan="2"><c:out value="${op:message('지역')}"/></th>
                    <th scope="col" rowspan="2"><c:out value="${op:message('지자체명')}"/></th>
                    <th scope="col" colspan="7"><c:out value="${op:message('지자체별 답례품 선정/등록 현황')}"/></th>
                    <th scope="col" colspan="4"><c:out value="${op:message('지자체별 답례품 구매 현황')}"/></th>
                </tr>
                <tr>
                    <th scope="col">합계</th>
                    <th scope="col">관광<br>서비스</th>
                    <th scope="col">농축산물</th>
                    <th scope="col">수산물</th>
                    <th scope="col">가공식품</th>
                    <th scope="col">생활용품</th>
                    <th scope="col">지역상품권</th>
                    <th scope="col">답례품<br>선택건수</th>
                    <th scope="col">답례품<br>선택금액</th>
                    <th scope="col">가장많이 선택한<br>답례품명</th>
                    <th scope="col">가장많이 선택한<br>답례품 건수</th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                	<td colspan="2" rowspan="3">
                		합계(전체)
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].itemTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].tourTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].farmTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].aquaticTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].processTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].dailyTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].ticketTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].orderTotalAll)}"/>
                	</td>
                	<td rowspan="3">
                		<c:out value="${op:numberFormat(list[0].salePriceTotalAll)}"/>
                	</td>
                	<td title="${list[0].allTop1Name}" style="height:38px;">
                		<c:out value="${op:strcut(list[0].allTop1Name, 20)}"/>
                	</td>
                	<td>
                		<c:out value="${op:numberFormat(list[0].allTop1Cnt)}"/>
                	</td>
                </tr>
                <tr>
                	<td title="${list[0].allTop2Name}" style="height:38px;">
                		<c:out value="${op:strcut(list[0].allTop2Name, 20)}"/>
                	</td>
                	<td>
                		<c:out value="${op:numberFormat(list[0].allTop2Cnt)}"/>
                	</td>
                </tr>
                <tr>
                	<td title="${list[0].allTop3Name}" style="height:38px;">
                		<c:out value="${op:strcut(list[0].allTop3Name, 20)}"/>
                	</td>
                	<td>
                		<c:out value="${op:numberFormat(list[0].allTop3Cnt)}"/>
                	</td>
                </tr>
            	<c:forEach items="${list}" var="item" varStatus="i">
	                <tr>
	            		<c:choose>
	            			<c:when test="${item.locgovCode == ''}">
			                	<td colspan="2" rowspan="3">
			                		합계(전체)
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.itemTotalCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.tourCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.farmCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.aquaticCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.processCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.dailyCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.ticketCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.orderTotalCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.salePrice)}"/>
			                	</td>
			                	<td title="${fn:escapeXml(item.top1Name)}" style="height:38px;">
			                		<c:out value="${op:strcut(item.top1Name, 20)}"/>
			                	</td>
			                	<td>
			                		<c:out value="${op:numberFormat(item.top1Cnt)}"/>
			                	</td>
	            			</c:when>
	            			<c:otherwise>
			                	<td rowspan="3">
			                		<c:out value="${item.upperLocgovNm}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${item.locgovNm}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.itemTotalCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.tourCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.farmCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.aquaticCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.processCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.dailyCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.ticketCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.orderTotalCnt)}"/>
			                	</td>
			                	<td rowspan="3">
			                		<c:out value="${op:numberFormat(item.salePrice)}"/>
			                	</td>
			                	<td title="${fn:escapeXml(item.top1Name)}" style="height:38px;">
			                		<c:out value="${op:strcut(item.top1Name, 20)}"/>
			                	</td>
			                	<td>
			                		<c:out value="${op:numberFormat(item.top1Cnt)}"/>
			                	</td>
	            			</c:otherwise>
	            		</c:choose>
	                </tr>
	                <tr>
	                	<td title="${fn:escapeXml(item.top2Name)}" style="height:38px;">
	                		<c:out value="${op:strcut(item.top2Name, 20)}"/>
	                	</td>
	                	<td>
	                		<c:out value="${op:numberFormat(item.top2Cnt)}"/>
	                	</td>
	                </tr>
	                <tr>
	                	<td title="${fn:escapeXml(item.top3Name)}" style="height:38px;">
	                		<c:out value="${op:strcut(item.top3Name, 20)}"/>
	                	</td>
	                	<td>
	                		<c:out value="${op:numberFormat(item.top3Cnt)}"/>
	                	</td>
	                </tr>
                </c:forEach>
            </tbody>
        </table>
		<c:if test="${empty list}">
			<div class="no_content">
				<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
			</div>
		</c:if>

        <%-- <div class="pagination-wrap">
        	<page:pagination-manager />
        </div> --%>
    </div>
    <!-- // 결과 영역 -->


<!-- <style>
td {background: #fff;}
.sortable-placeholder td {
	height: 70px;
	background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
	opacity: 0.5;
}
#change_ordering, #change_ordering2 {
	display: none;
}
#change_ordering2 {
	position:fixed; right:0; bottom: 166px; z-index: 1000;
}
</style> -->


<script type="text/javascript">
$(function(){
	// 화면 로딩 후 시도, 시군구 정보 세팅
	if ("${fn:escapeXml(statisticsParam.upperLocgovCode)}" != "") {		// 세팅된 시도 값 있을 경우
    	$("#selectSidoCode").val("${fn:escapeXml(statisticsParam.upperLocgovCode)}").prop("selected", true);		// 시도 값 세팅
        searchSigungu(document.getElementById("selectSidoCode"));		// 시군구 목록 조회
    }
});


// 페이지 당 조회수 파라미터 세팅
function setItemPerPage() {
	let form = document.getElementById("featuredParam");
	let itemCntPerPage = document.getElementById("perPageCntStr");
	let pageElement = document.getElementById("selectItemPerPage");
	if (itemCntPerPage) {
		itemCntPerPage.value = pageElement.value;
	} else {
		itemCntPerPage = document.createElement("input");
		itemCntPerPage.setAttribute("id", "perPageCntStr");
		itemCntPerPage.setAttribute("type", "hidden");
		itemCntPerPage.setAttribute("name", "itemsPerPage");
		itemCntPerPage.setAttribute("value", pageElement.value);
	}

	form.appendChild(itemCntPerPage);
}


// 시군구 목록 조회
function searchSigungu(sidoElement) {
	let sidoValue = sidoElement.value;
	$("#selectSigungu option").remove();
    $('#selectSigungu').append('<option value="">-시,군,구-</option>');
    if (sidoValue) {
        $.post(
        		url("/common/getLocgovCode")
        		, {'code' : sidoValue}
        		, function(response) {
        			Common.responseHandler(response, function(){
        				let data = response.data;
			            for (var i = 0; i < data.length; i++) {
			                var options = '<option value="' + data[i].LOCGOV_CODE + '">' + data[i].LOCGOV_NM + '</option>';
			                $('#selectSigungu').append(options);
			            }

			            // 조회 된 값 유지
			            if ("${fn:escapeXml(param.locgovCode)}" != "") {
			            	$("#selectSigungu").val("${fn:escapeXml(param.locgovCode)}").prop("selected", true);

			            	if (!$("#selectSigungu").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
			            		$("#selectSigungu option:eq(0)").prop("selected", true);
			            	}
			            }
        			});
	            }
        )
        .always(function () {
        	Common.loading.hide();
        });
    } else {
    	Common.loading.hide();
    }
}



function excelDownload() {
	let param = $('#statisticsParam').serialize();
	let uri = "/opmanager/shop-statistics/sales/month-locgov/excel-download";
	// Shop.downloadExcelOrder(uri, param, true);
    uri += "?" + param;
	location.href = uri;
}

</script>
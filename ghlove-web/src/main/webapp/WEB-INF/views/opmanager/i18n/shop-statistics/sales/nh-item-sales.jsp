<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3><span></span></h3>

<form:form modelAttribute="statisticsParam" action="" method="post">

	<div class="board_write">

		<table class="board_write_table" summary="${ fn:escapeXml(title) }">
			<caption><c:out value="${ title }"/></caption>
			<colgroup>
				<col style="width:150px;" />
				<col style="width:*;" />
			</colgroup>
			<tbody>
                <tr>
                    <td class="label">지자체</td>
                    <td>
                        <div class="flex_box gap-08">
                            <form:select path="upperLocgovCode" class="wd-150" onChange="wdrChange(this.value)">
			                    <form:option value="">-시,도-</form:option>
			                    <c:forEach items="${sido}" var="wdr">
			                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
			                    </c:forEach>
			                </form:select>
                            <form:select path="locgovCode" class="wd-150">
			                    <option value="">-시,군,구-</option>
			                </form:select>
                        </div>
                    </td>
                </tr>
				<tr>
				 	<td class="label"><c:out value="${op:message('M00011')}"/> <!-- 검색구분 --> </td>
				 	<td>
				 		<div class="flex_box gap-08">
							<form:select path="where" title="${op:message('M00011')}" class="wd-150">
								<form:option value="" label="전체" />
								<form:option value="BUSINESS_NUMBER" label="사업자번호" />
								<form:option value="LOGIN_ID" label="아이디" />
								<form:option value="COMPANY_NAME" label="상호명" />
								<form:option value="ITEM_NAME" label="답례품명" />
							</form:select>
							<form:input path="query" class="input_txt required _filter wd-500" title="${op:message('M00022')}" maxlength="20" /><!-- 검색어 -->
						</div>
				 	</td>
				</tr>
				<tr>
				 	<td class="label">년도</td>
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
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>

	<div class="count_title mt-40">
		<h5>
			<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
		</h5>
		<span>
			<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
				onchange="$('form#statisticsParam').submit();"> <!-- 화면 출력수 -->
				<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
			</form:select>
		</span>
	</div>

</form:form>

<div class="board_list">

	<table class="board_list_table" summary="농협 사업자 답례품 현황">
		<caption>농협 사업자 답례품 현황</caption>
		<colgroup>
            <col style="width:100px;">
           	<col style="width:250px;">
            <col style="width:150px;">
            <col style="width:250px;">
            <col style="width:200px;">
            <col style="width:250px;">
            <col style="width:200px;">
            <col style="width:200px;">
            <col style="width:200px;">
            <col style="width:150px;">
            <col style="width:150px;">
            <col style="width:300px;">
            <col style="width:200px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No</th>
				<th scope="col">사업자번호</th>
				<th scope="col">아이디</th>
				<th scope="col">상호명</th>
				<th scope="col">지자체</th>
				<th scope="col">답례품명</th>
				<th scope="col">카테고리</th>
				<th scope="col">하위 카테고리</th>
				<th scope="col">가격(단가)</th>
				<th scope="col">주문수</th>
				<th scope="col">판매개수</th>
				<th scope="col">총 판매액</th>
				<th scope="col">상품코드</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ list }" var="item" varStatus="index">
				<tr style="background:#fff;">
					<td>
						${op:numberFormat(totalCount - (statisticsParam.itemsPerPage) * (pagination.currentPage - 1) - index.index)}
					</td>
					<td>
						${fn:escapeXml(item.businessNumber)}
					</td>
					<td>
						${fn:escapeXml(item.loginId)}
					</td>
					<td>
						${fn:escapeXml(item.companyName)}
					</td>
					<td>
						${fn:escapeXml(item.upperLocgovNm)} ${fn:escapeXml(item.locgovNm)}
					</td>
					<td>
						${fn:escapeXml(item.itemName)}
					</td>
					<td>
						${fn:escapeXml(item.categoryNms)}
					</td>
					<td>
						${fn:escapeXml(item.categoryDetailNms)}
					</td>
					<td>
						${op:numberFormat(item.price)}
					</td>
					<td>
						${op:numberFormat(item.orderCnt)}
					</td>
					<td>
						${op:numberFormat(item.saleCnt)}
					</td>
					<td>
						${op:numberFormat(item.totalSalePrice)}
					</td>
					<td>
						${fn:escapeXml(item.itemUserCode)}
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

	<div class="flex_box juc-sbt">
		<div class="btn_all">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>
	</div>

	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</div>

<script type="text/javascript">
	$(function(){
	    wdrChange($("#upperLocgovCode").val(), true);
	});

	function validate() {
		return true;
	}

	// 지자체 변경
	function wdrChange(value, isInit) {
		Common.loading.hide();

		$("#locgovCode option").remove();

		if ($("#upperLocgovCode").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				$('#locgovCode').append('<option value="">-전체-</option>');
				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#locgovCode').append(options);
		        }

				// 조회 된 값 유지
				if ("${fn:escapeXml(statisticsParam.locgovCode)}" != "" && isInit) {
				    $("#locgovCode").val('${fn:escapeXml(statisticsParam.locgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#locgovCode select").val("");
				}

		    });
		} else {
	        $('#locgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	function dateChange(element) {
		let value = element.value;
		if (value.length > 6) {
			element.value = value.substring(0, 6);
		}
	}


	function excelDownload() {
		let param = $('#statisticsParam').serialize();

		Shop.downloadExcelOrder("/opmanager/shop-statistics/sales/nh-item-sales/excel", param, false);
	}

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		$("#statisticsParam").submit();
	}
</script>

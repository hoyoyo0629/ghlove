<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style type="text/css">
.options {
	margin:0px !important;
	padding-bottom:0px !important;
}
</style>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3>정산마감 내역</h3>

<h3 class="mt50 fs24">[<c:out value="${seller.companyName}"/>] <%-- <c:out value="${op:date(remittanceParam.startDate)}"/> ~ <c:out value="${op:date(remittanceParam.endDate)}"/> --%><c:out value="${op:date(remittanceConfirmDate)}"/></h3>

<div class="board_write">

	<div class="board_list">
		<c:if test="${!requestContext.sellerPage}">

		<table class="board_write_table">
			<colgroup>
	            <col style="width:220px;">
	            <col>
	            <col style="width:220px;">
	            <col>
			</colgroup>
			<tbody>
				<tr>
					<td class="label">대표자</td>
					<td>
						<div><c:out value="${seller.representativeName}"/></div>
					</td>
					<td class="label">소속 지자체</td>
					<td>
						<div><c:out value="${seller.locgovNm}"/></div>
					</td>
				</tr>
				<tr>
					<td class="label">휴대폰</td>
					<td>
						<div><c:out value="${seller.phoneNumber}"/></div>
					</td>
					<td class="label">연락처</td>
					<td>
						<div><c:out value="${seller.telephoneNumber}"/></div>
					</td>
				</tr>
			</tbody>
		</table>

		</c:if>

		<form:form modelAttribute="remittanceParam" method="post">
			<form:hidden path="remittanceId" />
			<form:hidden path="query" />
			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
				</h5>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
						onchange="$('form#remittanceParam').submit();"> <!-- 화면 출력수 -->
						<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
					</form:select>
				</span>
			</div>
		</form:form>


		<table class="board_list_table" summary="정산내역 리스트">
			<caption>정산내역 리스트</caption>
			<colgroup>
                <col style="width:50px;">
                <col style="width:150px;">
                <col style="width:150px;">
                <col style="width:150px;">
                <col style="width:400px;">
                <col style="width:200px;">
                <col style="width:150px;">
                <col style="width:200px;">
                <col style="width:150px;">
                <col style="width:200px;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">No</th>
					<th scope="col">결제일</th>
					<th scope="col">구매확정일</th>
					<th scope="col">주문번호</th>
					<th scope="col">답례품번호</th>
					<th scope="col">답례품명</th>
					<th scope="col">옵션</th>
					<th scope="col">주문자명</th>
					<th scope="col">판매가(1개)</th>
					<th scope="col">주문수량</th>
					<th scope="col">정산금액</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="item" varStatus="index">
					<tr style="background:#fff;">
						<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
						<td><c:out value="${op:datetime(item.payDate)}"/></td>
						<td><c:out value="${op:datetime(item.confirmDate)}"/></td>
						<td><c:out value="${item.orderCode}"/></td>
						<td><c:out value="${item.itemUserCode}"/></td>
						<td><c:out value="${item.itemName}"/></td>
						<td>
							<c:out value="${shop:viewItemOptions(item.setItemFlag, item.options)}"/>
						</td>
						<td><c:out value="${item.buyerName}"/></td>
						<%-- <td><c:out value="${op:numberFormat(item.commissionBasePrice * item.quantity + item.etcAmt)} P</td> --%>
						<%-- <td><c:out value="${op:numberFormat(item.salePrice * item.quantity + item.etcAmt)}"/> P</td> --%>
						<td><c:out value="${op:numberFormat(item.salePrice)}"/> P</td>
						<td><c:out value="${op:numberFormat(item.quantity)}"/> 개</td>
						<td><c:out value="${op:numberFormat(item.remittancePrice * item.quantity + item.etcAmt)}"/> 원</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<c:if test="${empty list}">
			<div class="no_content">
				<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
			</div>
		</c:if>
	</div>



	<div class="flex_box juc-sbt">
		<div class="btn_all">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<%-- <a href="${requestContext.getRequestUri()}/excel-download?${queryString}" class="btn btn-success btn-sm hidden"><span class="glyphicon glyphicon-save"></span> ${op:message('M00254')}</a> --%> <!-- 엑셀 다운로드 -->
				<a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/finish/list" class="btn btn-dark-gray btn-mini">목록</a>
			</div>
		</div>
	</div>

	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</div>

<div>
    <ul class="list-bullet point">
	    <li>
	        정산기간 ( 매월 1일 ~ 매월 말일 기준 구매확정건 )
	    </li>
	    <li>
	        정산예정일 : 익월 1일(답례품 담당자 확인시작일: 확인 기간 - 1일 ~ 15일)
	    </li>
	    <li>
	        정산확정일 : 익월 15일( 정산지급 기간 : 15일 ~ 20일 )
	    </li>
	    <li>
	        정산마감내역
	    </li>
	    <br>
        <li>
            다운로드된 엑셀정보를 정산 이외의 목적으로 이용, 유출하면 개인정보보호법에 따라 처벌 받게 됩니다.
        </li>
        <li>
            정산 이후 엑셀정보는 즉시 삭제 및 파기하시기 바랍니다.
        </li>
    </ul>
</div>

<script type="text/javascript">
	$(function(){
		$.each($('td.amount'), function(){
			if ($.trim($(this).html()) == '0원') {
				$(this).css('color', '#bfbebe');
			}
		});
	});

	function excelDownload() {
		let param = $('#remittanceParam').serialize();

		Shop.downloadExcelOrder("${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/remittance/finish/detail-excel/${fn:escapeXml(remittanceParam.sellerId)}/${fn:escapeXml(remittanceParam.remittanceId)}", param, true);
	}
</script>
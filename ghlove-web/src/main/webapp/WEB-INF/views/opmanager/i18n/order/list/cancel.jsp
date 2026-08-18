<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style type="text/css">
	.board_list_table th{
		text-align:center;
	}

	.order_cancel_layer {display: none;position: fixed; z-index: 100000; width:850px; left: 50%; margin-left: -425px; top:10px; padding-bottom: 20px; background: #fff}
</style>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3><span>주문취소 목록</span></h3>

<form:form modelAttribute="claimApplyParam" method="post">

	<div class="board_write">

		<table class="board_write_table" summary="${fn:escapeXml(title)}">
			<caption><c:out value="${ title }"/></caption>
			<colgroup>
				<col style="width:220px;" />
				<col />
			</colgroup>
			<tbody>
				<c:if test="${requestContext.sellerPage == false}">
					<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
		                <tr>
		                    <td class="label">지자체</td>
		                    <td>
		                        <div class="flex_box gap-08">
		                            <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
					                    <form:option value="">-시,도-</form:option>
					                    <c:forEach items="${wdr}" var="wdr">
					                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
					                    </c:forEach>
					                </form:select>
		                            <form:select path="shLocgovCode" class="wd-150">
					                    <option value="">-시,군,구-</option>
					                </form:select>
		                        </div>
		                    </td>
		                </tr>
	                </c:if>
                </c:if>
				<tr>
				 	<td class="label"><c:out value="${op:message('M00011')}"/> <!-- 검색구분 --> </td>
				 	<td>
				 		<div class="flex_box gap-08">
							<form:select path="where" title="${op:message('M00011')}" class="wd-150">
								<form:option value="" label="구분" />
								<form:option value="USER_NAME" label="주문자명" />
								<form:option value="CLAIM_CODE" label="클레임번호" />
								<form:option value="ORDER_CODE" label="주문번호" />
								<c:if test="${!requestContext.sellerPage}">
								    <form:option value="COMPANY_NAME" label="상호명" />
								</c:if>
								<form:option value="ITEM_NAME" label="답례품명" />
								<%-- <form:option value="ITEM_SELLER_CODE" label="고유코드" /> --%>
							</form:select>
							<form:input path="query" class="input_txt required _filter" title="${op:message('M00022')}" maxlength="20" /><!-- 검색어 -->
						</div>
				 	</td>
				</tr>
				<tr>
				 	<td class="label"><c:out value="${op:message('M01520')}"/></td><!-- 신청일자 -->
				 	<td>
				 		<div>
							<span class="datepicker"><form:input path="searchStartDate" class="datepicker" maxlength="8" title="${op:message('M00024')}" /><!-- 주문일자 시작일 --></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="searchEndDate" class="datepicker" maxlength="8" title="${op:message('M00025')}" /><!-- 주문일자 종료일 --></span>
							<span class="day_btns">
								<a href="javascript:;" class="btn_date today"><c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
								<a href="javascript:;" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
								<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
								<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
								<a href="javascript:;" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
                                <c:choose>
                                    <c:when test="${op:hasRole('ROLE_ADMIN_CALL')}">
                                        <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
                                    </c:when>
                                </c:choose>
							</span>
						</div>
				 	</td>
				 </tr>
				 <tr>
				 	<td class="label">처리상태</td>
				 	<td>
				 		<div class="flex_box gap-08">
				 			<div class="checkbox">
								<form:checkbox path="claimStatus" value="01" label=" 신청" />
							</div>

							<%--
				 			<div class="checkbox">
								<form:checkbox path="claimStatus" value="02" label=" 보류" />
							</div>
							 --%>

							<%--
							<div class="checkbox">
								<form:checkbox path="claimStatus" value="03" label=" 취소 환불대기" />
							</div>
							 --%>
						 	<%-- <c:if test="${requestContext.sellerPage != true}"> --%>
					 			<div class="checkbox">
									<form:checkbox path="claimStatus" value="99" label=" 취소거절" />
								</div>
							<%-- </c:if> --%>
							<div class="checkbox">
								<form:checkbox path="claimStatus" value="04" label=" 취소완료" />
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
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='<c:out value="${requestContext.sellerPage ? '/seller' : '/opmanager'}"/>/order/cancel/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button> --%>
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
				onchange="$('form#claimApplyParam').submit();"> <!-- 화면 출력수 -->
				<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
				<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
				<%-- <form:option value="500" label="500${op:message('M00053')}" /> --%> <!-- 개 출력 -->
			</form:select>
		</span>
	</div>
</form:form>



<div class="board_list">

	<div class="btn_all">
		<div class="btn_left mb0">

		</div>
		<div class="btn_right mb0">

		</div>
	</div>
	<form id="listForm">
		<table class="board_list_table" summary="주문내역 리스트">
			<caption>주문내역 리스트</caption>
			<colgroup>
	            <col style="width:50px;">
	            <c:if test="${requestContext.sellerPage == false}">
	            	<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
	            		<col style="width:200px;">
	            	</c:if>
	            </c:if>
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:500px;">
	            <col style="width:100px;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">No</th>
					<c:if test="${requestContext.sellerPage == false}">
						<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
							<th scope="col">지자체</th>
						</c:if>
					</c:if>
					<th scope="col">주문일</th>
					<th scope="col">신청일</th>
					<th scope="col">주문번호</th>
					<th scope="col">처리상태</th>
					<th scope="col">클레임번호</th>
					<th scope="col">주문자</th>
					<th scope="col">수취인</th>
					<!-- <th scope="col">판매자</th> -->
					<th scope="col">상호명</th>
					<th scope="col">상품정보</th>
					<th scope="col">신청수량</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="listIndex" value="0" />
				<c:forEach items="${ list }" var="apply" varStatus="index">
					<c:set var="orderItem" value="${apply.orderItem}" />


					<c:set var="tdColor">#fff</c:set>
					<c:if test="${index.count % 2 == 0}">
						<c:set var="tdColor"></c:set>
					</c:if>

					<tr style="background:${fn:escapeXml(tdColor)};">
						<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
						<c:if test="${requestContext.sellerPage == false}">
							<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
								<td><div><c:out value="${orderItem.locgovNm}"/></div></td>
							</c:if>
						</c:if>
						<td><c:out value="${op:datetime(orderItem.createdDate)}"/></td>
						<td><c:out value="${op:datetime(apply.createdDate)}"/></td>
						<td>
							<a href="<c:out value="${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/cancel/order-detail/${orderItem.orderSequence}/${orderItem.orderCode}?url=${requestContext.currentUrl}"/>"><c:out value="${orderItem.orderCode}"/></a>
						</td>
						<td><c:out value="${apply.claimStatusLabel}"/></td>
						<td><c:out value="${apply.claimCode}"/></td>
						<td>
							<c:out value="${apply.buyerName}"/>
							<c:if test="${not empty apply.loginId}">
								<p>[<c:out value="${apply.loginId}"/>]</p>
							</c:if>
						</td>
						<td><c:out value="${apply.receiveName}"/></td>
						<td>
							<c:choose>
								<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.companyName}"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="left">
							<c:out value="${orderItem.itemName}"/> [<c:out value="${orderItem.itemUserCode}"/>]
							<c:if test="${!empty orderItem.options}">
								<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
							</c:if>
							<c:out value="${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}"/>
						</td>
						<td><c:out value="${op:numberFormat(apply.claimApplyQuantity)}"/>개</td>
					</tr>
					<c:set var="listIndex"><c:out value="${listIndex + 1}"/></c:set>
				</c:forEach>
			</tbody>
		</table>
	</form>

	<c:if test="${empty list}">
	<div class="no_content">
		<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
	</div>
	</c:if>

	<div class="btn_all">
		<div class="btn_left mb0">

		</div>
		<div class="btn_right mb0">

		</div>
	</div>

	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>

</div>

<div class="board_guide ml10 hidden">
	<p class="tip">Tip</p>
	<p class="tip"></p>
</div>
<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});

	$(function() {
	    wdrChange($("#shWdr").val());
	});

	var changeYn = "N";

	// 지차체 변경여부 체크
	$("select[name='shWdr']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	});

	// 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();
		$("#shLocgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {

				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#shLocgovCode').append(options);
		        }

				// 조회 된 값 유지
				if ("${fn:escapeXml(claimApplyParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(claimApplyParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}

		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		var strStartDate = $("#searchStartDate").val();
		var strEndDate = $("#searchEndDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#searchEndDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		$("#claimApplyParam").submit();
	}
</script>
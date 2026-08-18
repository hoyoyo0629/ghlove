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
<h3>정산예정 내역</h3>

<h3 class="mt50 fs24"><span>▫ [<c:out value="${seller.companyName}"/>] ${op:date(remittanceParam.startDate)} ~ ${op:date(remittanceParam.endDate)}</span></h3>
<div class="board_write">

	<div class="board_list">
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

		<form:form modelAttribute="remittanceParam" method="post">
			<form:hidden path="sellerId" />
			<form:hidden path="startDate" />
			<form:hidden path="endDate" />
			<form:hidden path="query" />

			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
				</h5>
				<span>
					<c:out value="${op:message('M00052')}"/> : <!-- 출력수 -->
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




		<form id="listForm" method="post">
			<input type="hidden" name="sellerId" value="${fn:escapeXml(remittanceParam.sellerId)}" />
			<input type="hidden" name="startDate" value="${fn:escapeXml(remittanceParam.startDate)}" />
			<input type="hidden" name="endDate" value="${fn:escapeXml(remittanceParam.endDate)}" />
			<input type="hidden" name="conditionType" />

			<table class="board_list_table" summary="정산내역 리스트">
				<caption>정산내역 리스트</caption>
				<colgroup>
	                <col style="width:50px;">
	                <col style="width:150px;">
	                <col style="width:150px;">
	                <col style="width:150px;">
	                <col style="width:150px;">
	                <col style="width:300px;">
	                <col style="width:200px;">
	                <col style="width:150px;">
	                <col style="width:200px;">
	                <col style="width:150px;">
	                <col style="width:200px;">
	                <col style="width:150px;">
				</colgroup>
				<thead>
					<tr>
						<!-- <th scope="col"><input type="checkbox" id="check_all" /></th> -->
						<th scope="col">No</th>
						<th scope="col">결제일</th>
						<th scope="col">구매확정일</th>
						<th scope="col">주문번호</th>
						<th scope="col">답례품번호</th>
						<th scope="col">답례품명</th>
						<th scope="col">옵션</th>
						<th scope="col">구분</th>
						<th scope="col">판매가(1개)</th>
						<th scope="col">주문수량</th>
						<th scope="col">정산금액</th>
						<th scope="col">정산예정일</th>
					</tr>	
				</thead>
				<tbody>
					<c:forEach items="${ list }" var="item" varStatus="index">
						<c:set var="key">${item.orderCode}^^^${item.orderSequence}^^^${item.itemSequence}</c:set>
						<tr ${item.remittanceStatusCode == '2' ? 'style="background-color:#f3aaaa"' : ''}>
							<!-- <td><input type="checkbox" name="id" value="${fn:escapeXml(key)}" /></td> -->
							<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
							<td><c:out value="${op:datetime(item.createdDate)}"/></td>
							<td><c:out value="${op:datetime(item.confirmDate)}"/></td>
							<td><c:out value="${item.orderCode}"/></td>
							<td><c:out value="${item.itemCode}"/></td>
							<td><c:out value="${item.itemName}"/></td>
							<td>
								<c:out value="${shop:viewItemOptions(item.setItemFlag, item.options)}"/>
							</td>
							<td><c:out value="${item.taxType == '1' ? '과세' : '면세'}"/></td>

							<c:choose>
								<c:when test="${item.commissionBasePrice != null && item.commissionBasePrice > 0}">
									<td class="amount text-right">
										<%-- ${op:numberFormat(item.commissionBasePrice * item.quantity)} --%>
										<c:out value="${op:numberFormat(item.commissionBasePrice)}"/>
									</td>
								</c:when>
								<c:otherwise><!-- 리얼커머스 이관 데이터용 -->
									<td class="amount text-right">
										<%-- ${op:numberFormat((item.supplyPrice - item.sellerDiscountPrice - item.sellerPoint - item.setDiscountPrice) * item.quantity)} --%>
										<c:out value="${op:numberFormat((item.supplyPrice - item.sellerDiscountPrice - item.sellerPoint - item.setDiscountPrice))}"/> P
									</td>
								</c:otherwise>
							</c:choose>
							<td class="amount text-right"><c:out value="${op:numberFormat(item.quantity)}"/> 개</td>
							<td class="amount text-right"><c:out value="${op:numberFormat((item.supplyPrice - item.sellerDiscountPrice - item.sellerPoint - item.setDiscountPrice) * item.quantity + item.etcAmt)}"/> 원</td>
							<td>
								<%-- <input type="text" name="editItemRemittanceMap[${key}].remittanceExpectedDate" class="required" value="${item.remittanceExpectedDate}" style="width:80px" /> --%>
								<c:out value="${op:formatDate(item.remittanceExpectedDate, '-')}"/>
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
		</form>
	</div>

	<div class="flex_box juc-sbt">
		<div class="btn_all">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>
		<div class="btn_all btn_right">
			<%-- <div class="btn_left mb0">
				<button type="button" class="btn btn-default btn-sm update-remittance hidden">정보 수정</button>
				<button type="button" class="btn btn-default btn-sm confirm-remittance hidden">정산 확정</button>
			</div> --%>
			<div class="flex_box gap-08">
				<a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/list" class="btn btn-dark-gray btn-mini">목록</a>
			</div>
		</div>
	</div>

	<div class="pagination-wrap">
		<page:pagination-manager />
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
</div>
</div>

<script type="text/javascript">
	$(function(){

		$.each($('td.amount'), function(){
			if ($.trim($(this).html()) == '0원') {
				$(this).css('color', '#bfbebe');
			}
		});

		$('.update-remittance').on('click', function() {

			var $form = $('#listForm');
			if ($form.find('input[name=id]:checked').size() == 0) {
				alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
				return;
			}

			if (validate() == false) {
				return;
			}

			if (confirm("선택하신 정산정보를 수정 하시겠습니까?\n이미 확정된 내역에 대해서는 대기상태로 변경됩니다.")) {
				Common.removeNumberComma();
				$form.find('input[name="conditionType"]').val('update');
				$form.submit();
			}



		});

		$('.confirm-remittance').on('click', function() {

			var $form = $('#listForm');
			if ($form.find('input[name=id]:checked').size() == 0) {
				alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
				return;
			}

			if (validate() == false) {
				return;
			}

			if (confirm("선택하신 정산정보를 확정 하시겠습니까?\n확정 일자는 설정하신 정산 예정일로 설정 됩니다.")) {
				Common.removeNumberComma();
				$form.find('input[name="conditionType"]').val('confirm');
				$form.submit();
			}

		});
	});

	function validate() {

		var $form = $('#listForm');
		var isError = false;
		$.each($form.find('input[name=id]:checked'), function(){
			var key = $(this).val();

			$date = $form.find('input[name="editItemRemittanceMap['+key+'].remittanceExpectedDate"]');
			if ($date.size() == 0) {
				isError = true;
				return false;
			} else {
				if (Common.validateDate($date.val()) == false) {
					isError = true;
					alert('입력하신 날짜를 확인바랍니다.');
					$date.focus();
					return false;
				}
			}
		});

		if (isError == true) {
			return false;
		}

		return true;
	}

	function sellerSeller(sellerId) {
		$('#sellerId').val(sellerId)
	}


	function excelDownload() {
		let param = $('#remittanceParam').serialize();

		Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/detail/item-excel", param, true);
	}

</script>
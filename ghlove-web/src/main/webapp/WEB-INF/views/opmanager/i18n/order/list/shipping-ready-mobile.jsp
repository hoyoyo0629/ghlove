<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<style type="text/css">
	.board_list_table th{
		text-align:center;
	}
	.order_cancel_layer {display: none;position: fixed; z-index: 100000; width:850px; left: 50%; margin-left: -425px; top:10px; padding-bottom: 20px; background: #fff}
</style>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3><span>배송준비중 목록</span></h3>

<form:form modelAttribute="orderParam" action="${fn:escapeXml(requestContext.requestUri)}" method="post">
	<div class="board_write">

		<table class="board_write_table" summary="${ fn:escapeXml(title) }">
			<caption><c:out value="${ title }"/></caption>
			<colgroup>
				<col style="width:220px;" />
				<col />
			</colgroup>
			<tbody>
				<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
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
				<tr>
				 	<td class="label"><c:out value="${op:message('M00011')}"/> <!-- 검색구분 --> </td>
				 	<td>
				 		<div class="flex_box gap-08">
							<form:select path="where" title="${op:message('M00011')}" class="wd-150">
								<form:option value="" label="구분" />
								<form:option value="ORDER_CODE" label="주문번호" />
								<form:option value="USER_NAME" label="주문자명" />
								<form:option value="RECEIVE_NAME" label="받는사람" />
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
				 	<td class="label">배송준비일</td>
				 	<td>
				 		<div>
				 			<form:select path="searchDateType" class="hidden">
				 				<form:option value="OI.SHIPPING_READY_DATE" label="배송준비일" />
				 				<form:option value="OI.CREATED_DATE" label="주문일" />
				 			</form:select>
							<span class="datepicker"><form:input path="searchStartDate" class="datepicker" maxlength="8" title="${op:message('M00024')}" /><!-- 주문일자 시작일 --></span>
							<form:select path="searchStartDateTime">
								<form:option value="" label="-선택-" />
								<form:option value="00" label="00시" />
								<c:forEach varStatus="i" begin="1" end="23">
									<c:if test="${i.count < 10 }">
										<form:option value="0${i.count}" label="0${i.count}시" />
									</c:if>
									<c:if test="${i.count >= 10 }">
										<form:option value="${i.count}" label="${i.count}시" />
									</c:if>
								</c:forEach>
							</form:select>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="searchEndDate" class="datepicker" maxlength="8" title="${op:message('M00025')}" /><!-- 주문일자 종료일 --></span>
							<form:select path="searchEndDateTime">
								<form:option value="" label="-선택-" />
								<form:option value="00" label="00시" />
								<c:forEach varStatus="i" begin="1" end="23">
									<c:if test="${i.count < 10 }">
										<form:option value="0${i.count}" label="0${i.count}시" />
									</c:if>
									<c:if test="${i.count >= 10 }">
										<form:option value="${i.count}" label="${i.count}시" />
									</c:if>
								</c:forEach>
							</form:select>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/shipping-ready'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button> --%>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>

	<div>
	    <ul class="list-bullet point">
	        <li>
	            다운로드된 엑셀정보를 배송 이외의 목적으로 이용, 유출하면 개인정보보호법에 따라 처벌 받게 됩니다.
	        </li>
	        <li>
	            배송이 시작된 엑셀정보는 즉시 삭제 및 파기하시기 바랍니다.
	        </li>
	    </ul>
	</div>

	<div class="count_title mt-40">
		<h5>
			<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
		</h5>
		<span>
			<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
				onchange="$('form#orderParam').submit();"> <!-- 화면 출력수 -->
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
	<sec:authorize access="hasRole('ROLE_EXCEL')">
		<div class="board_guide hidden" style="border:1px solid #d5d5d5; padding: 15px; margin-top:15px;">
			<p class="tip">
				<a href="javascript:;" class="btn_write gray_small" onclick="downloadOrderExcel(${fn:escapeXml(totalCount)})"><img src="/content/opmanager/images/icon/icon_excel.png" alt=""><span>주문내역 다운로드</span> </a>
			</p>

			<p class="tip">
				<br/>다운로드 하실 주문을 선택후 다운로드 하시면 선택다운로드 됩니다.
				<br/>한번에 선택 다운로드 가능한 주문은 최대 500건 입니다.(우측 출력수 조정 최대치)
			</p>
		</div>
	</sec:authorize>
	<%-- <div class="btn_all hidden">
		<div class="btn_left mb0">
			<button type="button" class="btn btn-default btn-sm shipping-start">배송 시작(SMS 미전송)</button>
			<button type="button" class="btn btn-default btn-sm shipping-start-send-message">배송 시작(SMS 전송)</button>
			<button type="button" class="btn btn-default btn-sm shipping-cancel">배송지시 취소</button>
		</div>
		<div class="btn_right mb0">
			<button type="button" class="btn btn-success btn-sm shipping-info-update"><span class="glyphicon glyphicon-open" aria-hidden="true"></span> 배송정보 일괄등록</button>
		</div>
	</div> --%>
	<form id="listForm">
		<table class="board_list_table" summary="주문내역 리스트">
			<caption>주문내역 리스트</caption>
			<colgroup>
	            <col style="width:50px;">
	            <col style="width:50px;">
	            <c:if test="${requestContext.sellerPage == false}">
	            	<col style="width:200px;">
	            </c:if>
	            <col style="width:200px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:500px;">
	            <col style="width:100px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col"><input type="checkbox" id="check_all" /></th>
					<th scope="col">No</th>
					<c:if test="${requestContext.sellerPage == false}">
						<th scope="col">지자체</th>
					</c:if>
					<!-- <th scope="col">발송준비일</th> -->
					<th scope="col">발송준비일</th>
					<th scope="col">주문번호</th>
					<th scope="col">주문자</th>
					<th scope="col">수취인</th>
					<th scope="col">상호명</th>
					<th scope="col">답례품정보</th>
					<th scope="col">수량</th>
					<th scope="col">판매가</th>
					<!-- <th scope="col">발송정보</th> -->
					<th scope="col">배송정보</th>
				</tr>

			</thead>
			<tbody>
				<c:forEach items="${list}" var="orderItem" varStatus="index">
					<c:set var="tdColor">#f4f4f4</c:set>
					<c:if test="${index.count % 2 == 0}">
						<c:set var="tdColor"></c:set>
					</c:if>

					<tr style="background:${fn:escapeXml(tdColor)};">
						<td>
							<input type="checkbox" name="id" value="${fn:escapeXml(orderItem.itemKey)}" />
						</td>
						<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
                        <c:if test="${requestContext.sellerPage == false}">
                        	<td><div><c:out value="${orderItem.locgovNm}"/></div></td>
                        </c:if>
						<td>
							<c:out value="${op:datetime(orderItem.shippingReadyDate)}"/>

							<!-- 배송 지연일 -->
							<c:if test="${orderItem.delayDay > 0}">
								<br/><strong style="color:red">(+<c:out value="${orderItem.delayDay}"/>)</strong>
							</c:if>
						</td>
						<td>
							<a href="${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/order/shipping-ready-mobile/order-detail/${fn:escapeXml(orderItem.orderSequence)}/${fn:escapeXml(orderItem.orderCode)}">${fn:escapeXml(orderItem.orderCode)}</a>
						</td>
						<td>
							<c:out value="${orderItem.userName}"/>
							<c:if test="${not empty orderItem.loginId}">
								<p>[<c:out value="${orderItem.loginId}"/>]</p>
							</c:if>
						</td>
						<td><c:out value="${orderItem.receiveName}"/></td>
						<td>
							<c:choose>
								<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.companyName}"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="left" >
							<p style="font-size: 11px; color: #333;font-weight: bold;"><c:out value="${orderItem.itemUserCode}"/></p>
							<c:out value="${orderItem.itemName}"/>
							<c:if test="${!empty orderItem.options}">
								<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
							</c:if>
							<c:out value="${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}"/>

							<c:if test="${not empty orderItem.memo}">
								<p style="font-size:11px; color: #e84700">
									- 배송 요청사항 : <c:out value="${op:nl2br(orderItem.memo)}"/>
								</p>
							</c:if>
						</td>
						<td><div><c:out value="${orderItem.quantity}"/>개</div></td>
						<td><div><c:out value="${op:numberFormat(orderItem.saleAmount)}"/></div></td>
						<td>
							<div class="flex_box col">
								<input type="hidden" name="shippings[${fn:escapeXml(index.index)}].key" value="${fn:escapeXml(orderItem.itemKey)}" />
								<c:choose>
									<c:when test="${orderItem.mobileNumber != null && orderItem.mobileNumber != ''}">
										<input type="text" id="${fn:escapeXml(orderItem.itemKey)}" name="shippings[${fn:escapeXml(index.index)}].mobileNumber" class="form-block" maxlength="30" value="${fn:escapeXml(orderItem.mobileNumber)}" />
									</c:when>
									<c:otherwise>
										<input type="text" id="${fn:escapeXml(orderItem.itemKey)}" name="shippings[${fn:escapeXml(index.index)}].mobileNumber" class="form-block" maxlength="30" />
									</c:otherwise>
								</c:choose>
							</div>
						</td>
					</tr>

				</c:forEach>
			</tbody>
		</table>
	</form>

	<c:if test="${empty list}">
	<div class="no_content">
		<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
	</div>
	</c:if>

	<div class="flex_box juc-sbt">
		<div class="btn_all">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadOrderExcel(${fn:escapeXml(totalCount)})">엑셀</button>
				<button type="button" class="btn btn-success btn-sm shipping-info-update"><span class="glyphicon glyphicon-open" aria-hidden="true"></span> 배송정보 일괄등록</button>
			</div>
		</div>
		<div class="btn_all">
			<div class="flex_box gap-08">
				<!-- <button type="button" class="btn btn-dark-gray btn-mini shipping-cancel">발송지시 취소</button> -->
				<button type="button" class="btn btn-dark-gray btn-mini shipping-cancel">배송지시 취소</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:tempSave();">임시저장</button>
				<!-- <button type="button" class="btn btn-dark-gray btn-mini shipping-mobile-direct">발송완료</button> -->
				<button type="button" class="btn btn-dark-gray btn-mini shipping-mobile-direct">배송완료</button>
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
            <strong>"배송지시 취소"</strong> 하시면 해당 주문은 <strong>"신규주문(모바일)"</strong>로 이동합니다.
        </li>
        <li>
            <strong>"임시저장"</strong> 하시면 해당 주문의 <strong>"배송정보"</strong>를 저장합니다.
        </li>
        <li>
            <strong>"배송완료"</strong> 하시면 해당 주문은 <strong>배송정보 저장과 동시에 "배송완료"</strong>로 이동합니다.
        </li>
        <li>
            발송준비중 상태에서 주문자가 취소 요청시 <strong>답례품 제공자의 처리</strong>가 필요합니다.
        </li>
        <li>
            주문처리시 <strong>200개 이내</strong>로 선택하여 진행해주세요.
        </li>
        <li>
            <strong>"배송정보 일괄등록"</strong> 기능으로 업로드할 엑셀파일은 "엑셀"버튼으로 다운로드 후 <strong>모바일번호</strong>만 수정하여 업로드 합니다.
        </li>
        <li>
            <strong>"배송정보 일괄등록"</strong> 기능으로 엑셀파일 업로드시 <strong>"배송준비중"상태 항목만 "배송완료" 상태로 변경</strong>됩니다.
        </li>
    </ul>
</div>

<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();

		$('.shipping-cancel').on('click', function() {

			var $form = $('#listForm');
			if ($form.find('input[name=id]:checked').size() == 0) {
				alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
				return;
			}

			//if (confirm("선택하신 상품을 발송지시 취소 하시겠습니까?\n해당 주문은 신규주문 목록으로 이동 합니다.")) {
			if (confirm("선택하신 상품을 배송지시 취소 하시겠습니까?\n해당 주문은 신규주문 목록으로 이동 합니다.")) {
				Manager.Order.listUpdate('shipping-ready', 'shipping-cancel');
			}
		});


		$('.shipping-mobile-direct').on('click', function() {

			var $form = $('#listForm');
			if ($form.find('input[name=id]:checked').size() == 0) {
				alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
				return;
			}

			let formData = $form.find('input[name=id]:checked');	// 발송완료 체크한 주문건
			let length = formData.length;
			for (let i = 0 ; i < length ; i++) {
				let key = formData[i].value;

				let dataList = $form.find('input[id="'+key+'"]');	// 해당 주문건의 모바일 넘버 리스트

				let dataListLength = dataList.length;
				if (dataListLength == 0) {
					return false;
				} else {
					for (let j = 0 ; j < dataListLength ; j++) {	// 발송정보(모바일 넘버) 입력필수
						let number = dataList[j].value;
						if (number == "") {
							//alert('발송정보(모바일넘버)를 입력해 주세요.');
							alert('배송정보(모바일넘버)를 입력해 주세요.');
							dataList[j].focus();
							return false;
						}
					}
				}
			}

			//if (confirm("선택하신 답례품을 발송완료 하시겠습니까?")) {
			if (confirm("선택하신 답례품을 배송완료 하시겠습니까?")) {
				Manager.Order.listUpdate('shipping-ready', 'shipping-mobile-direct');
			}
		});

		//엑셀로 배송정보 일괄등록 [2017-09-05]minae.yun
		$('.shipping-info-update').on('click', function() {
            Common.popup("${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/upload-excel-mobile", 'upload-excel', 600, 550, 0);
		});

	});

	function downloadOrderExcel(totalCount) {
		var $id = $('input[name="id"]:checked');
		var param = "";
		if ($id.size() > 0) {
			param = $id.serialize();
		} else{
            param = $('#orderParam').serialize();
		}

		if (totalCount == 0) {
			alert(Message.get("M00297"));	// 조회된 데이터가 없습니다.
			return;
		}

		if (totalCount > 500) {
			if ($id.size() == 0) {
				alert('주문 내역이 500건이 넘는경우 선택 다운로드 혹은 일자별 검색후 다운로드 하시기 바랍니다.');
				return;
			}
		}

		// Get(QueryString)으로 다운로드 요청할 경우 엑셀 200건 다운로드 시 413 에러(Entity Too Large) 발생
		var checkedLength = $("input[name='id']:checked").length
		if(checkedLength == 0) // 체크박스가 없으면 Get 방식으로 QueryString 요청
			Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/shipping-ready-mobile/order-excel-download", param, true);
		else // 체크박스가 있으면 파라미터를 Post 방식으로 요청
			Shop.downloadExcelOrderPost("${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/shipping-ready-mobile/order-excel-download", param, true);
	}

	$(function() {
	    wdrChange($("#shWdr").val());
	});

	var changeYn = "N";

	// 지차체 변경여부 체크
	$("select[name='shWdr']").on('focus', function () {
	    changeYn = "Y";
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
				if ("${fn:escapeXml(orderParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(orderParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}

		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}



	function tempSave() {
		var $form = $('#listForm');
		if ($form.find('input[name=id]:checked').size() == 0) {
			alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
			return;
		}

		let formData = $form.find('input[name=id]:checked');	// 발송완료 체크한 주문건
		let length = formData.length;
		for (let i = 0 ; i < length ; i++) {
			let key = formData[i].value;

			let dataList = $form.find('input[id="'+key+'"]');	// 해당 주문건의 모바일 넘버 리스트

			let dataListLength = dataList.length;
			if (dataListLength == 0) {
				return false;
			} else {
				for (let j = 0 ; j < dataListLength ; j++) {	// 발송정보(모바일 넘버) 입력필수
					let number = dataList[j].value;
					if (number == "") {
						//alert('발송정보(모바일넘버)를 입력해 주세요.');
						alert('배송정보(모바일넘버)를 입력해 주세요.');
						dataList[j].focus();
						return false;
					}
				}
			}
		}

		//if (confirm("선택하신 답례품의 발송정보를 임시저장 하시겠습니까?")) {
		if (confirm("선택하신 답례품의 배송정보를 임시저장 하시겠습니까?")) {
			Manager.Order.listUpdate('mobile-temp-save', 'mobile-temp-save');
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

		$("#orderParam").submit();
	}
</script>
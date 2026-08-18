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
<h3>정산예정 내역</h3>

<form:form modelAttribute="remittanceParam" action="" method="post">

	<div class="board_write">

		<table class="board_write_table" summary="${ fn:escapeXml(title) }">
			<caption><c:out value="${ title }"/></caption>
			<colgroup>
				<col style="width:150px;" />
				<col style="width:*;" />
			</colgroup>
			<tbody>
				<c:if test="${!requestContext.sellerPage
							&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
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
								<form:option value="COMPANY_NAME" label="상호명" />
								<form:option value="REPRESENTATIVE_NAME" label="대표자명" />
							</form:select>
							<form:input path="query" class="input_txt required _filter wd-500" title="${op:message('M00022')}" maxlength="20" /><!-- 검색어 -->
						</div>
				 	</td>
				</tr>
				<tr>
				 	<td class="label">정산 예정일</td>
				 	<td>
				 		<div>
				 			<span class="datepicker"><form:input path="startDate" class="datepicker" maxlength="6" title="정산일자 시작일" onchange="javascript:dateChange(this)"/><!-- 정산일자 시작일 --></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="endDate" class="datepicker" maxlength="6" title="정산일자 종료일" onchange="javascript:dateChange(this)"/><!-- 정산일자 종료일 --></span>
							<span class="day_btns">
								<%-- <a href="javascript:;" class="btn_date today"><c:out value="${op:message('M00026')}"/></a> --%><!-- 오늘 -->
								<%-- <a href="javascript:;" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a> --%><!-- 1주일 -->
								<a href="javascript:;" class="btn_date month-0"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
								<a href="javascript:;" class="btn_date month-2"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
								<a href="javascript:;" class="btn_date month-11"><c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
                                <c:choose>
                                    <c:when test="${op:hasRole('ROLE_ADMIN_CALL')}">
                                        <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
                                    </c:when>
                                </c:choose>
							</span>
						</div>
				 	</td>
				 </tr>
				 <tr class="hidden">
				 	<td class="label">판매자</td>
					<td>
						<div>
							<form:select path="sellerId">
								<form:option value="0"><c:out value="${op:message('M00039')}"/></form:option>
								<c:forEach items="${sellerList}" var="list" varStatus="i">
									<c:if test="${list.sellerId != remittanceParam.defaultOpmanagerSellerId}">
										<form:option value="${fn:escapeXml(list.sellerId)}">[<c:out value="${list.loginId}"/>] <c:out value="${list.sellerName}"/></form:option>
									</c:if>
								</c:forEach>
							</form:select>
							<a href="javascript:Common.popup('/opmanager/seller/find?defaultOpmanagerSellerId=${fn:escapeXml(defaultOpmanagerSellerId)}', 'find_seller', 800, 500, 1)" class="btn btn-dark-gray btn-sm"> <span class="glyphicon glyphicon-search"></span> 검색</a>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<%-- <button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button> --%>
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

<div class="board_list">

	<form id="listForm" action="${fn:escapeXml(requestContext.managerUri)}/remittance/expected/list/update" method="post">
		<input type="hidden" name="sellerId" value="${fn:escapeXml(remittanceParam.sellerId)}" />
		<input type="hidden" name="startDate" value="${fn:escapeXml(remittanceParam.startDate)}" />
		<input type="hidden" name="endDate" value="${fn:escapeXml(remittanceParam.endDate)}" />

		<table class="board_list_table" summary="정산내역 리스트">
			<caption>정산내역 리스트</caption>
			<colgroup>
				<c:if test="${!requestContext.sellerPage}">
	            	<col style="width:50px;">
	            </c:if>
	            <col style="width:50px;">
	            <c:if test="${!requestContext.sellerPage
								&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
	            	<col style="width:200px;">
	            </c:if>
            	<col style="width:200px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:100px;">
	            <%-- <col style="width:150px;"> --%>
	            <col style="width:150px;">
			</colgroup>
			<thead>
				<tr>
					<c:if test="${!requestContext.sellerPage}">
						<th scope="col"><input type="checkbox" id="check_all" /></th>
					</c:if>
					<th scope="col">No</th>
					<c:if test="${!requestContext.sellerPage
								&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
						<th scope="col">지자체</th>
					</c:if>
					<th scope="col">상호명</th>
					<th scope="col">대표자명</th>
					<th scope="col">휴대폰</th>
					<th scope="col">판매가</th>
					<th scope="col">정산금액</th>
					<th scope="col">주문건수</th>
					<!-- <th scope="col">정산대상월</th> -->
					<th scope="col">정산예정일</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="item" varStatus="index">
					<%-- <c:set var="key">${item.sellerId}^^^${remittanceParam.startDate}^^^${remittanceParam.endDate}</c:set> --%>
					<c:set var="key"><c:out value="${item.sellerId}"/>^^^<c:out value="${item.remittanceExpectedDate}"/>^^^<c:out value="${item.itemRemittanceAmount + item.shippingTotalAmount + item.addPaymentTotalAmount}"/></c:set>
					<c:set var="remittanceDate"><c:out value="${item.remittanceDate}"/></c:set>
					<tr style="background:#fff;">
						<c:if test="${!requestContext.sellerPage}">
							<td><input type="checkbox" name="id" value="${fn:escapeXml(key)}" /></td>
						</c:if>
						<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
						<c:if test="${!requestContext.sellerPage
								&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
							<td><div><c:out value="${item.locgovNm}"/></div></td>
						</c:if>
						<td>
							<%-- <a href="/opmanager/remittance/expected/detail/item/${item.sellerId}/${remittanceParam.startDate}/${remittanceParam.endDate}?${queryString}">${item.companyName}</a> --%>
							<a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/detail/item/${fn:escapeXml(item.sellerId)}/${fn:escapeXml(item.remittanceExpectedDate)}/${fn:escapeXml(item.remittanceExpectedDate)}"><c:out value="${item.companyName}"/></a>
						</td>
						<td><c:out value="${item.representativeName}"/></td>
						<td><c:out value="${item.phoneNumber}"/></td>
						<%-- <c:choose>
							<c:when test="${remittanceDate != null && !remittanceDate.isEmpty()}">
								<td class="amount">
									<c:out value="${op:numberFormat(item.itemTotalCommissionBaseAmount)}"/>
								</td>
							</c:when>
							<c:otherwise> --%><!-- 리얼커머스 이관 데이터용 -->
								<td class="amount">
									<c:out value="${op:numberFormat(item.itemRemittanceAmount + item.shippingTotalAmount + item.addPaymentTotalAmount)}"/>
								</td>
							<%-- </c:otherwise>
						</c:choose> --%>

						<%-- <td>
							<p><a href="/opmanager/remittance/expected/detail/item/${item.sellerId}/${remittanceParam.startDate}/${remittanceParam.endDate}?${queryString}" class="btn btn-gradient btn-xs">수정</a></p>
						</td> --%>
						<td class="amount"><c:out value="${op:numberFormat(item.itemRemittanceAmount + item.shippingTotalAmount + item.addPaymentTotalAmount)}"/></td>
						<td><c:out value="${item.orCnt}"/></td>
						<%-- <td>
							<input type="text" maxlength="8" name="editItemRemittanceMap[${key}].remittanceExpectedDate" class="required" value="${remittanceParam.endDate}" style="width:100px" onkeydown="javascript:inputCheck1(event);" onkeyup="javascript:inputCheck2(event);"/>
						</td> --%>
						<%-- <td><c:out value="${item.remittanceTargetMonth}"/></td> --%>
						<c:choose>
							<c:when test="${item.remittanceExpectedDate != null && !item.remittanceExpectedDate.isEmpty()}">
								<td>
									<c:out value="${op:formatDate(item.remittanceExpectedDate, '-')}"/>
								</td>
							</c:when>
							<c:otherwise>
								<td>
									미정
								</td>
							</c:otherwise>
						</c:choose>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>

		<c:if test="${!requestContext.sellerPage
									&& (op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-dark-gray btn-mini confirm-remittance">정산 확정</button>
				</div>
			</div>
		</c:if>
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
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
		$.each($('td.amount'), function(){
			if ($.trim($(this).html()) == '0원') {
				$(this).css('color', '#bfbebe');
			}
		});

		$('.confirm-remittance').on('click', function() {

			var $form = $('#listForm');
			if ($form.find('input[name=id]:checked').size() == 0) {
				alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
				return;
			}

			if (!validate()) {
				return;
			}

			if (confirm("선택한 정산정보를 확정 하시겠습니까?")) {
				$form.submit();
			}

		});

		const datepickers = document.getElementsByClassName("datepicker");
		let length = datepickers.length;
		for(let i = 0 ; i < length ; i++) {
			if (datepickers[i].tagName.toLowerCase() == 'input') {
				dateChange(datepickers[i]);
			}
		}
	});

	function validate() {
		/*
		var $form = $('#listForm');
		var isError = false;
		$.each($form.find('input[name=id]:checked'), function(){
			var key = $(this).val();

			$date = $form.find('input[name="editItemRemittanceMap['+key+'].remittanceExpectedDate"]');

			if ($date.size() == 0) {
				isError = true;
				return false;
			} else {
				if (!Common.validateDate($date.val())) {
					isError = true;
					alert('입력하신 날짜를 확인바랍니다.');
					$date.focus();
					return false;
				}
			}
		});

		if (isError) {
			return false;
		}
		*/		// foreach가 비동기 동작이어서 for 문으로 수정

		// 정산 확정일 체크로직 주석처리
		/*
		let form = $('#listForm');
		let formData = form.find('input[name=id]:checked');
		let length = formData.length;
		for (let i = 0 ; i < length ; i++) {
			let key = formData[i].value;

			let dateList = form.find('input[name="editItemRemittanceMap['+key+'].remittanceExpectedDate"]');

			let dateListLength = dateList.length;
			if (dateListLength == 0) {
				return false;
			} else {
				for (let j = 0 ; j < dateListLength ; j++) {
					let date = dateList[j].value;
					if (!Common.validateDate(date)) {
						alert('입력하신 날짜를 확인바랍니다.');
						dateList[j].focus();
						return false;
					}
				}
			}
		}
		*/
		return true;
	}

	function sellerSeller(sellerId) {
		$('#sellerId').val(sellerId)
	}

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
				if ("${fn:escapeXml(remittanceParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(remittanceParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}

		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	// 정산 확정일 숫자만 입력가능하도록 추가
	function inputCheck1(event) {
		let key = event.key.toLowerCase();
        if (!($.validator.patterns._number_only.test(key)
            || key == 'tab'
            || key == 'backspace'
            || key == 'delete'
            || key == 'f12'
            || key == 'arrowleft'
            || key == 'arrowright')) {
        	return event.preventDefault();
        }
	}

	// 정산 확정일 숫자만 입력가능하도록 추가
	function inputCheck2(event) {
		let key = event.key.toLowerCase();
        if (!($.validator.patterns._number_only.test(key)
            || key == 'tab'
            || key == 'backspace'
            || key == 'delete'
            || key == 'f12'
            || key == 'arrowleft'
            || key == 'arrowright')) {
        	event.target.value =  event.target.value.replace(/[^0-9]/g, '');
        }
	}

	function dateChange(element) {
		let value = element.value;
		if (value.length > 6) {
			element.value = value.substring(0, 6);
		}
	}

	function checkDate(event) {
		let startDate = document.getElementById('startDate').value;
		let endDate = document.getElementById('endDate').value;
		let isDate = false;

		if (Common.validateDate(startDate + '01') && Common.validateDate(endDate + '01')) {
			if (Number(startDate) <= Number(endDate)) {
				isDate = true;
			}
		}

		if (!isDate) {
			event.preventDefault();
			alert('정산 예정일을 확인해주세요.');
		}
	}

	function excelDownload() {
		let startDate = document.getElementById('startDate').value;
		let endDate = document.getElementById('endDate').value;
		let isDate = false;

		if (Common.validateDate(startDate + '01') && Common.validateDate(endDate + '01')) {
			if (Number(startDate) <= Number(endDate)) {
				isDate = true;
			}
		}

		if (!isDate) {
			alert('정산 예정일을 확인해주세요.');
			return;
		}
		let param = $('#remittanceParam').serialize();

		Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/expected/list-excel", param, true);
	}

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		var strStartDate = $("#startDate").val();
		var strEndDate = $("#endDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#endDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}else if(strStartDate && strStartDate.length == 6 && strEndDate && strEndDate.length == 6){
			startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), '01');
			endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), '01');
			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#endDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		$("#remittanceParam").submit();
	}
</script>

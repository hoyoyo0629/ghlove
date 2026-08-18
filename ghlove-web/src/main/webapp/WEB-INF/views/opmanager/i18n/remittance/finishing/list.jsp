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
<h3>정산마감 내역</h3>

<form:form modelAttribute="remittanceParam" action="" method="post">

	<div class="board_write">

		<table class="board_write_table" summary="${ fn:escapeXml(title) }">
			<caption><c:out value="${ title }"/></caption>
			<colgroup>
				<col style="width:150px;" />
				<col style="width:*;" />
			</colgroup>
			<tbody>
				<c:if test="${!requestContext.sellerPage}">
				<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2')
								|| op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
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
				 	<td class="label"><%-- ${op:message('M01523')} --%>입금처리일</td>
				 	<td>
				 		<div>
				 			<span class="datepicker"><form:input path="startDate" class="datepicker" maxlength="8" title="정산일자 시작일" /><!-- 정산일자 시작일 --></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="endDate" class="datepicker" maxlength="8" title="정산일자 종료일" /><!-- 정산일자 종료일 --></span>
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
				</c:if>
				<c:if test="${requestContext.sellerPage}">
                    <tr>
                        <!-- <td class="label">확정일자 검색</td> -->
                        <td class="label">입금처리일자 검색</td>
                        <td>
							 <div>
							 	<form:select path="startDateYear" class="wd-150">
									<%-- <form:option value="">선택</form:option> --%>
									<c:forEach begin="0" end="100" step="1" var="index">
										<option value="${years - index}" label="${years - index}">
									</c:forEach>
								</form:select>
								<span class="wave"> 년 </span>
								<form:select path="startDateMonth" class="wd-150">
									<%-- <form:option value="">선택</form:option> --%>
									<c:forEach begin="1" end="12" step="1" var="index">
										<option value="${index}" label="${index}">
									</c:forEach>
								</form:select>
							 	<span class="wave"> 월 </span>
							 	<span class="wave">~</span>
							 	<form:select path="endDateYear" class="wd-150">
								<%-- <form:option value="">선택</form:option> --%>
									<c:forEach begin="0" end="100" step="1" var="index">
										<option value="${years - index}" label="${years - index}">
									</c:forEach>
								</form:select>
								<span class="wave"> 년 </span>
								<form:select path="endDateMonth" class="wd-150">
									<%-- <form:option value="">선택</form:option> --%>
									<c:forEach begin="1" end="12" step="1" var="index">
										<option value="${index}" label="${index}">
									</c:forEach>
								</form:select>
							 	<span class="wave"> 월 </span>
							 </div>
                        </td>
                    </tr>
				</c:if>
				<tr>
					<td class="label">상태검색</td>
					<td>
					    <div class="flex_box gap-08">
					        <form:select path="searchStatus" title="상태검색" class="wd-150">
								<form:option value="" label="전체" />
								<form:option value="5" label="입금확인대기" />
								<form:option value="9" label="입금확인완료" />
							</form:select>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/finish/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
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

	<form id="listForm" action="${fn:escapeXml(requestContext.managerUri)}/remittance/finish/list/check" method="post">

		<table class="board_list_table" summary="정산내역 리스트">
			<caption>정산내역 리스트</caption>
			<colgroup>
				<c:if test="${!requestContext.sellerPage}">
		            <col style="width:50px;">
					<c:if test="${!requestContext.sellerPage
								&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
			            <col style="width:200px;">
		            </c:if>
		            <col style="width:100px;">
		            <col style="width:200px;">
		            <col style="width:150px;">
		            <col style="width:150px;">
		            <%-- <col style="width:200px;"> --%>
		            <col style="width:200px;">
		            <col style="width:200px;">
		            <col style="width:200px;">
		            <col style="width:50px;">
				</c:if>
				<c:if test="${requestContext.sellerPage}">
	                <col style="width:50px;">
	                <col style="width:50px;">
	                <%-- <col style="width:200px;"> --%>
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:100px;">
				</c:if>
			</colgroup>
			<thead>
				<tr>
					<c:if test="${!requestContext.sellerPage}">
			            <th scope="col">정산아이디</th>
						<c:if test="${!requestContext.sellerPage
									&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
					        <th scope="col">지자체</th>
				        </c:if>
						<th scope="col">상호명</th>
						<th scope="col">대표자명</th>
						<th scope="col">휴대폰</th>
						<th scope="col">판매가</th>
						<th scope="col">정산금액</th>
						<!-- <th scope="col">정산대상월</th> -->
						<th scope="col">지급일</th>
						<th scope="col">입금확인일</th>
						<th scope="col">계좌번호</th>
						<th scope="col">주문건수</th>
					</c:if>
					<c:if test="${requestContext.sellerPage}">
						<th scope="col"><!-- <input type="checkbox" id="check_all" /> --></th>
						<th scope="col">정산아이디</th>
						<!-- <th scope="col">정산대상월</th> -->
						<th scope="col">확정일</th>
						<th scope="col">지급일</th>
						<th scope="col">총 판매액</th>
						<th scope="col">총 정산금액</th>
						<th scope="col">상태</th>
						<th scope="col"></th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ list }" var="item" varStatus="index">
					<c:if test="${requestContext.sellerPage == false}">
						<tr style="background:#fff;">
							<td><%-- <c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/> --%><c:out value="${item.remittanceId}"/></td>
							<c:if test="${!requestContext.sellerPage
										&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
								<td><div><c:out value="${item.locgovNm}"/></div></td>
							</c:if>
							<td>
								<div>
									<a href="javascript:;" onclick="detailView('${requestContext.sellerPage ? '/seller' : '/opmanager'}', '${fn:escapeXml(item.sellerId)}', '${fn:escapeXml(item.remittanceId)}');"><c:out value="${item.companyName}"/></a>
								</div>
							</td>
							<td><c:out value="${item.representativeName}"/></td>
							<td><c:out value="${item.phoneNumber}"/></td>
							<td>
								<%-- <c:choose>
									<c:when test="item.itemTotalCommissionBaseAmount != null && item.itemTotalCommissionBaseAmount > 0">
										<c:out value="${op:numberFormat(item.itemTotalCommissionBaseAmount)}"/>
									</c:when>
									<c:otherwise> --%><!-- 리얼커머스 데이터용 -->
										<c:out value="${op:numberFormat(item.salesAmount)}"/>
									<%-- </c:otherwise>
								</c:choose> --%>
								<!-- 원 -->
							</td>
							<td class="amount"><c:out value="${op:numberFormat(item.finishingAmount)}"/><!-- 원 --></td>
							<%-- <td><c:out value="${item.remittanceTargetMonth}"/></td> --%>
							<td><c:out value="${op:formatDate(item.paymentDate, '-')}"/></td>
							<c:choose>
								<c:when test="${item.remittanceStatusCode == 9}">
									<td><c:out value="${op:formatDate(item.finishingDate, '-')}"/></td>
								</c:when>
								<c:otherwise>
									<td>입금 확인 대기</td>
								</c:otherwise>
							</c:choose>
							<td>[<c:out value="${item.bankName}"/>] <c:out value="${item.bankAccountNumber}"/> <br> <c:out value="${item.bankInName}"/></td>
							<td><c:out value="${item.orCnt}"/></td>
						</tr>
					</c:if>
					<c:if test="${requestContext.sellerPage == true}">
						<tr style="background:#fff;">
							<c:set var="key"><c:out value="${item.remittanceId}"/></c:set>
							<td>
								<c:choose>
									<c:when test="${item.remittanceStatusCode != 9}">
										<input type="checkbox" name="id" value="${fn:escapeXml(key)}" />
										<%-- <input type="hidden" name="finishingRemittanceMap[${key}].amount" value="${item.salesAmount}" />
										<input type="hidden" name="finishingRemittanceMap[${key}].sellerId" value="${item.sellerId}" /> --%>
									</c:when>
								</c:choose>
							</td>
							<td><%-- <c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/> --%><c:out value="${item.remittanceId}"/></td>
							<%-- <td><c:out value="${item.remittanceTargetMonth}"/></td> --%>
							<td>
								<c:out value="${op:date(item.confirmDate)}"/>
							</td>
							<td>
								<c:out value="${op:date(item.paymentDate)}"/>
							</td>
							<td><c:out value="${op:numberFormat(item.salesAmount)}"/> 원</td>
							<td>
								<%-- <c:out value="${op:numberFormat(item.itemRemittanceAmount + item.shippingTotalAmount + item.addPaymentTotalAmount)} --%>
								<c:out value="${op:numberFormat(item.finishingAmount)}"/> 원
							</td>
							<c:choose>
								<c:when test="${item.remittanceStatusCode == 9}">
									<td>완료</td>
								</c:when>
								<c:otherwise>
									<td>입금확인대기</td>
								</c:otherwise>
							</c:choose>
							<td>
								<div>
									<a href="javascript:;" onclick="detailView('${requestContext.sellerPage ? '/seller' : '/opmanager'}', '${fn:escapeXml(item.sellerId)}', '${fn:escapeXml(item.remittanceId)}');">상세보기</a>
								</div>
							</td>
						</tr>
					</c:if>
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
		<c:if test="${requestContext.sellerPage == true}">
		    <div class="btn_all btn_right">
		        <div class="flex_box gap-08">
		            <button type="button" class="btn btn-dark-gray btn-mini finishing-remittance">입금확인</button>
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

		<c:if test="${requestContext.sellerPage}">
			$('.finishing-remittance').on('click', function() {

				let $form = $('#listForm');
				let checked = $form.find('input[name=id]:checked');

				if ($form.find('input[name=id]:checked').size() == 0) {
					alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
					return;
				}
				if (confirm("선택하신 내용을 입금확인 처리 하시겠습니까?")) {
					$form.submit();
				}

			});
			const urlParams = new URLSearchParams(location.search);

			let date = '' + ${fn:escapeXml(today)};
			let nowYear = Number(date.substring(0, 4));
			let nowMonth = Number(date.substring(4, 6));
/*
			if (urlParams.get('startDateYear')) {
				$("#startDateYear").val(urlParams.get('startDateYear'));
			} else {
				$("#startDateYear").val(nowYear);
			}
			if (urlParams.get('startDateMonth')) {
				$("#startDateMonth").val(urlParams.get('startDateMonth'));
			} else {
				$("#startDateMonth").val(nowMonth);
			}
			if (urlParams.get('endDateYear')) {
				$("#endDateYear").val(urlParams.get('endDateYear'));
			} else {
				$("#endDateYear").val(nowYear);
			}
			if (urlParams.get('endDateMonth')) {
				$("#endDateMonth").val(urlParams.get('endDateMonth'));
			} else {
				$("#endDateMonth").val(nowMonth);
			}
*/

			if ('${fn:escapeXml(remittanceParam.startDateYear)}') {
				$("#startDateYear").val(${fn:escapeXml(remittanceParam.startDateYear)});
			} else {
				$("#startDateYear").val(nowYear);
			}
			if ('${fn:escapeXml(remittanceParam.startDateMonth)}') {
				$("#startDateMonth").val(${fn:escapeXml(remittanceParam.startDateMonth)});
			} else {
				$("#startDateMonth").val(nowMonth);
			}
			if ('${fn:escapeXml(remittanceParam.endDateYear)}') {
				$("#endDateYear").val(${fn:escapeXml(remittanceParam.endDateYear)});
			} else {
				$("#endDateYear").val(nowYear);
			}
			if ('${fn:escapeXml(remittanceParam.endDateMonth)}') {
				$("#endDateMonth").val(${fn:escapeXml(remittanceParam.endDateMonth)});
			} else {
				$("#endDateMonth").val(nowMonth);
			}

		</c:if>
	});

	function sellerSeller(sellerId) {
		$('#sellerId').val(sellerId)
	}

	<%--이상우 [2017-04-25 추가] 상세페이지 이동 시 검색한 날짜를 parameter로 가져가도록 수정--%>
	function detailView(type, sellerId, remittanceId) {
		location.href = type+"/remittance/finish/detail/"+sellerId+"/"+remittanceId+"?"+$('form#remittanceParam').serialize();
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

	function checkDate(event) {
		<c:if test="${requestContext.sellerPage}">
			let startYear = document.getElementById('startDateYear').value;
			let endYear = document.getElementById('endDateYear').value;

			let startMonth = '0' + document.getElementById('startDateMonth').value;
			let endMonth = '0' + document.getElementById('endDateMonth').value;

			startMonth = startMonth.substring(startMonth.length - 2);
			endMonth = endMonth.substring(endMonth.length - 2);

			let startDate = startYear + startMonth;
			let endDate = endYear + endMonth;

			let isDate = false;

			if (Common.validateDate(startDate + '01') && Common.validateDate(endDate + '01')) {
				if (Number(startDate) <= Number(endDate)) {
					isDate = true;
				}
			}
		</c:if>

		<c:if test="${!requestContext.sellerPage}">
			let startDate = document.getElementById('startDate').value;
			let endDate = document.getElementById('endDate').value;

			let isDate = false;

			if (Common.validateDate(startDate) && Common.validateDate(endDate)) {
				if (Number(startDate) <= Number(endDate)) {
					isDate = true;
				}
			}
		</c:if>

		if (!isDate) {
			event.preventDefault();
			alert('입금처리일자를 확인해주세요.');
		}
	}

	function excelDownload() {
		<c:if test="${requestContext.sellerPage}">
			let startYear = document.getElementById('startDateYear').value;
			let endYear = document.getElementById('endDateYear').value;

			let startMonth = '0' + document.getElementById('startDateMonth').value;
			let endMonth = '0' + document.getElementById('endDateMonth').value;

			startMonth = startMonth.substring(startMonth.length - 2);
			endMonth = endMonth.substring(endMonth.length - 2);

			let startDate = startYear + startMonth;
			let endDate = endYear + endMonth;

			let isDate = false;

			if (Common.validateDate(startDate + '01') && Common.validateDate(endDate + '01')) {
				if (Number(startDate) <= Number(endDate)) {
					isDate = true;
				}
			}
		</c:if>

		<c:if test="${!requestContext.sellerPage}">
			let startDate = document.getElementById('startDate').value;
			let endDate = document.getElementById('endDate').value;

			let isDate = false;

			if (Common.validateDate(startDate) && Common.validateDate(endDate)) {
				if (Number(startDate) <= Number(endDate)) {
					isDate = true;
				}
			}
		</c:if>

		if (!isDate) {
			alert('입금처리일자를 확인해주세요.');
			return;
		}
		let param = $('#remittanceParam').serialize();

		Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/finish/list-excel", param, true);
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
		}

		$("#remittanceParam").submit();
	}
</script>





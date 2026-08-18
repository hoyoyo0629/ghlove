<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style type="text/css">
	.board_list_table th{
		text-align:center;
	}
	.order_return_layer {display: none;position: fixed; z-index: 100000; width:850px; left: 50%; margin-left: -425px; top:10px; padding-bottom: 20px; background: #fff}
</style>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3>미완료 주문 목록(2023. 6. 9. 이전 데이터)</h3>

<form:form modelAttribute="holdOrderListParam" action="${fn:escapeXml(requestContext.requestUri)}" method="post">

	<div class="board_write">

		<table class="board_write_table" summary="${fn:escapeXml(title)}">
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
								<form:option value="COMPANY_NAME" label="상호명" />
							</form:select>
							<form:input path="query" class="input_txt required _filter" title="${op:message('M00022')}" maxlength="20" /><!-- 검색어 -->
						</div>
				 	</td>
				 </tr>
				 <tr>
				 	<td class="label"><c:out value="${op:message('M00023')}"/></td><!-- 주문일자 -->
				 	<td>
				 		<div>
				 			<form:select path="searchDateType" class="hidden">
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
							</span>
						</div>
				 	</td>
				 </tr>
				 <tr>
				 	<td class="label" rowspan="2">주문상태</td>
				 	<td>
				 		<div class="flex_box gap-08">
				 			<div class="checkbox">
				 				<!-- <input type="checkbox" id="op-order-default" /><label>전체</label> -->
				 				<form:checkbox path="opOrderDefault" id="op-order-default" value="Y" label=" 전체" />
				 			</div>
				 			<div class="checkbox">
								<form:checkbox path="status" value="10" label="결제완료" cssClass="op-order-default" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="20" label="배송준비중" cssClass="op-order-default" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="30" label="배송중" cssClass="op-order-default" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="35" label="배송완료" cssClass="op-order-default" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="40" label="구매확정" cssClass="op-order-default" />
							</div>
						</div>
				 	</td>
				 </tr>
				 <tr>
				 	<td>
						<div class="flex_box gap-08">
							<div class="checkbox">
								<!-- <input type="checkbox" id="op-order-claim" /><label>클레임</label> -->
								<form:checkbox path="opOrderClaim" id="op-order-claim" value="Y" label=" 클레임" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="50" label=" 교환처리중" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="55" label=" 교환배송중" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="58" label=" 교환완료" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="60" label=" 반품처리중" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="65" label=" 반품완료" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="70" label=" 취소처리중" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="75" label=" 취소완료" cssClass="op-order-claim" />
							</div>
						</div>
				 	</td>
				 </tr>
				 <tr>
				 	<td class="label" rowspan="2">처리상태</td>
				 	<td>
				 		<div class="flex_box gap-08">
				 			<div class="checkbox">
								<form:checkbox path="holdStatus" value="N" label="정상주문 여부 미확인" cssClass="op-order-status" />
							</div>
							<div class="checkbox">
								<form:checkbox path="holdStatus" value="S" label="판매자 정상주문처리 승인요청" cssClass="op-order-status" />
							</div>
							<%-- <div class="checkbox">
								<form:checkbox path="holdStatus" value="M" label="관리자 정상주문처리 승인완료" cssClass="op-order-status" />
							</div> --%>
							<div class="checkbox">
								<form:checkbox path="holdStatus" value="R" label="관리자 정상주문처리 승인반려" cssClass="op-order-status" />
							</div>
		 				</div>
	 				</td>
			 	</tr>
			</tbody>
		</table>

		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
			</div>
		</div>
	</div>

	<div class="count_title mt-40">
		<h5>
			<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
		</h5>
		<span>
			<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
				onchange="$('form#holdOrderListParam').submit();"> <!-- 화면 출력수 -->
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

	<%--<sec:authorize access="hasRole('ROLE_EXCEL')">
		<div class="board_guide" style="border:1px solid #d5d5d5; padding: 15px; margin-top:15px;">
			<p class="tip">
				<a href="javascript:;" class="btn_write gray_small" onclick="downloadOrderExcel(${totalCount})"><img src="/content/opmanager_image/icon/icon_excel.png" alt=""><span>주문내역 다운로드</span> </a>
			</p>

			<p class="tip">
				<br/>현재 페이지의 주문건을 전체 다운로드 합니다.
				<br/>한번에 선택 다운로드 가능한 주문은 최대 500건 입니다.(우측 출력수 조정 최대치)
			</p>
		</div>
	</sec:authorize>--%>

	<div class="btn_all">
		<div class="btn_left mb0">

		</div>
		<div class="btn_right mb0">

		</div>
	</div>
	<form id="listForm" method="post">
		<table class="board_list_table" summary="주문내역 리스트">
			<caption>주문내역 리스트</caption>
			<colgroup>
				<col style="width:50px;">
	            <col style="width:50px;">
	            <c:if test="${requestContext.sellerPage == false}">
	           		<col style="width:150px;">
	            </c:if>
	            <col style="width:200px;">
	            <col style="width:150px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <c:if test="${requestContext.sellerPage == false}">
	            	<col style="width:200px;">
	            </c:if>
	            <col style="width:500px;">
	            <col style="width:100px;">
	            <col style="width:200px;">
	            <col style="width:150px;">
	            <%-- <col style="width:200px;"> --%>
	            <col style="width:150px;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col"><input type="checkbox" id="check_all" /></th>
					<th scope="col">No</th>
					<c:if test="${requestContext.sellerPage == false}">
						<th scope="col">지자체</th>
					</c:if>
					<th scope="col">주문일</th>
					<th scope="col">주문번호</th>
					<th scope="col">주문자</th>
					<th scope="col">수취인</th>
					<c:if test="${requestContext.sellerPage == false}">
						<th scope="col">상호명</th>
					</c:if>
					<th scope="col">답례품명</th>
					<th scope="col">수량</th>
					<th scope="col">판매가</th>
					<th scope="col">주문상태</th>
					<th scope="col">배송정보</th>
					<!-- <th scope="col">포인트차감</th> -->
					<th scope="col">처리상태</th>
				</tr>

			</thead>
			<tbody>

				<c:forEach items="${list}" var="orderItem" varStatus="index">

					<tr style="background:#fff;">
						<td>
							<c:if test="${orderItem.matchYn == 'Y'}">
								<c:if test="${requestContext.sellerPage && (orderItem.holdConfirmStatus == 'N' || orderItem.holdConfirmStatus == 'R' )}">
									<input type="checkbox" name="id" value="${fn:escapeXml(orderItem.rcOrderCode)}" id="${fn:escapeXml(orderItem.rcOrderCode)}" />
								</c:if>
								<c:if test="${!requestContext.sellerPage && orderItem.holdConfirmStatus == 'S'}">
									<input type="checkbox" name="id" value="${fn:escapeXml(orderItem.rcOrderCode)}" id="${fn:escapeXml(orderItem.rcOrderCode)}" />
								</c:if>
							</c:if>
						</td>
						<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
						<c:if test="${requestContext.sellerPage == false}">
							<td><div><c:out value="${orderItem.locgovNm}"/></div></td>
						</c:if>
						<td>
							<c:out value="${op:datetime(orderItem.createdDate)}"/>
						</td>
						<td>
							<c:if test="${orderItem.orderStatus == '10' || orderItem.orderStatus == '20'}">
								<a href="${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/temp-process/order/order-detail/${fn:escapeXml(orderItem.rcOrderCode)}"><c:out value="${orderItem.rcOrderCode}"/></a>
							</c:if>
							<c:if test="${orderItem.orderStatus != '10' && orderItem.orderStatus != '20'}">
								<c:out value="${orderItem.rcOrderCode}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${empty orderItem.loginId}">
								<c:out value="${orderItem.buyerName}"/>
								<p>[비회원]</p>
							</c:if>

							<c:if test="${not empty orderItem.loginId}">
								<c:out value="${orderItem.userName}"/>
								<p>[<c:out value="${orderItem.loginId}"/>]</p>
							</c:if>
						</td>
						<td><c:out value="${orderItem.receiveName}"/></td>
						<c:if test="${requestContext.sellerPage == false}">
						<td>
							<c:choose>
								<c:when test="${shop:sellerId() == orderItem.sellerId}">자사</c:when>
								<c:otherwise>
									<span class="glyphicon glyphicon-user"></span><c:out value="${orderItem.companyName}"/>
								</c:otherwise>
							</c:choose>
						</td>
						</c:if>
						<td class="left break-word">
							<c:out value="${orderItem.itemName}"/> [<c:out value="${orderItem.itemUserCode}"/>]
							<c:if test="${!empty orderItem.options}">
								<p><c:out value="${shop:viewItemOptions(orderItem.setItemFlag, orderItem.options)}"/></p>
							</c:if>
							<c:out value="${shop:viewOrderGiftItemList(orderItem.orderGiftItemList)}"/>
						</td>
						<td><div><c:out value="${orderItem.quantity}"/>개</div></td>
						<td><div><c:out value="${op:numberFormat(orderItem.saleAmount)}"/></div></td>
						<td>
							<div>
								<c:choose>
									<c:when test="${!empty orderItem.mobileNumber && orderItem.orderStatus == '35'}">
										발송완료
									</c:when>
									<c:otherwise>
										<c:out value="${orderItem.orderStatusLabel}"/>
									</c:otherwise>
								</c:choose>
							</div>
						</td>
						<td>
							<c:if test="${orderItem.shippingDate != '00000000000000'}">
								<c:choose>
									<c:when test="${empty orderItem.deliveryNumber && empty orderItem.mobileNumber}">
										송장 번호 : 없음 (<c:out value="${orderItem.deliveryCompanyName ? orderItem.deliveryCompanyName : '택배사 정보 없음'}"/>)
									</c:when>
									<c:when test="${empty orderItem.deliveryNumber && !empty orderItem.mobileNumber}">
										모바일 : <c:out value="${orderItem.deliveryNumber}"/>
									</c:when>
									<c:otherwise>
										송장 번호 : <c:out value="${orderItem.deliveryNumber}"/>(<c:out value="${orderItem.deliveryCompanyName}"/>)
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<%-- <td>
							<c:out value="${orderItem.matchYn == 'Y' ? '일치' : '불일치'}"/>
						</td> --%>
						<td>
							<c:out value="${orderItem.holdConfirmStatusName}"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input id="inputStatus" name="inputStatus" type="hidden" />
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


	<div class="flex_box juc-sbt">
		<div class="btn_all">
		</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<c:if test="${requestContext.sellerPage}">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:request('S');">승인 요청</button>
				</c:if>
				<c:if test="${!requestContext.sellerPage}">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:request('M');">승인 완료</button>
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:request('R');">승인 반려</button>
				</c:if>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

	var checkAllCnt;	// 일반주문 전체 체크 카운트
	var checkClaimCnt;	// 클레임주문 전체 체크 카운트

	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();

		$('#op-order-default').on('click', function(){
			$('.op-order-default').prop('checked', $(this).prop('checked'));
		});

		$('.op-order-default').on('click', function(){

			checkAllCnt = 0;	// 초기화

            $.each($('.op-order-default:checked'), function(i){
            	checkAllCnt++;	// 일반주문 5단계 체크할떄마다 카운트 증가
            });

			if(checkAllCnt < 5) {	// 모두 체크하면 전체선택
				$('#op-order-default').prop('checked', false);
			} else {	// 한개라도 해제하면 전체해제
				$('#op-order-default').prop('checked', true);
			}
		});

		$('#op-order-claim').on('click', function(){
			$('.op-order-claim').prop('checked', $(this).prop('checked'));
		});

		$('.op-order-claim').on('click', function(){

			checkClaimCnt = 0;	// 초기화

            $.each($('.op-order-claim:checked'), function(i){
            	checkClaimCnt++;	// 클레임주문 7단계 체크할떄마다 카운트 증가
            });

			if(checkClaimCnt < 7) {	// 모두 체크하면 전체선택
				$('#op-order-claim').prop('checked', false);
			} else {	// 한개라도 해제하면 전체해제
				$('#op-order-claim').prop('checked', true);
			}
		});

		$('.op-order-status').on('click', function(){
			let cnt = 0;	// 초기화

            $.each($('.op-order-status:checked'), function(i){
            	cnt++;
            });

			if (cnt == 0) {
				$('.op-order-status').prop('checked', true);
			}
		});
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
				if ("${fn:escapeXml(holdOrderListParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(holdOrderListParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}

		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	function request(requestStatus) {
		let $form = $('#listForm');
		let checked = $form.find('input[name=id]:checked');
		let length = checked.size();
		if (length == 0) {
			alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
			return;
		}
		let msg = "";
		switch(requestStatus) {
			case 'S':
				msg = "요청처리";
				break;
			case 'M':
				msg = "완료처리";
				break;
			case 'R':
				msg = "반려처리";
				break;
			default:
				return;
		}

		if (confirm("선택한 주문정보를 " + msg + " 하시겠습니까?")) {
			$("#inputStatus").val(requestStatus);
			$form.submit();
		}
	}



</script>
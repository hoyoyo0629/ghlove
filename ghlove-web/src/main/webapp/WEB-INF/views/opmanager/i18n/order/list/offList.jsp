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

<script type="text/javascript" src="/content/modules/netfunnel.js"></script>
<script type="text/javascript" src="/content/modules/netfunnel_skin.js"></script>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3><span>주문전체 목록</span></h3>

<form:form modelAttribute="orderParam" action="${fn:escapeXml(requestContext.requestUri)}" method="post">

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
				 	<td class="label" rowspan="2">처리구분</td>
				 	<td>
				 		<div class="flex_box gap-08">
				 			<div class="checkbox">
				 				<!-- <input type="checkbox" id="op-order-default" /><label>전체</label> -->
				 				<form:checkbox path="opOrderDefault" id="op-order-default" value="Y" label=" 전체" />
				 			</div>
							<div class="checkbox">
								<form:checkbox path="status" value="0" label="입금대기" cssClass="op-order-default"/>
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
								<form:checkbox path="status" value="59" label=" 교환거절" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="60" label=" 반품처리중" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="65" label=" 반품완료" cssClass="op-order-claim" />
							</div>
							<div class="checkbox">
								<form:checkbox path="status" value="69" label=" 반품거절" cssClass="op-order-claim" />
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button> --%>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search_net();">검색</button>
			</div>
		</div>
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
				<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
			</form:select>
		</span>
	</div>
</form:form>



<div class="board_list">


<%-- 	<div class="board_guide" style="border:1px solid #d5d5d5; padding: 15px; margin-top:15px;">
		<p class="tip">
			<a href="javascript:;" class="btn_write gray_small" onclick="downloadOrderExcel(${totalCount})"><img src="/content/opmanager_image/icon/icon_excel.png" alt=""><span>주문내역 다운로드</span> </a>
		</p>
	</div> --%>

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
	            <col style="width:200px;">
	            <c:if test="${!requestContext.sellerPage}">
	            	<col style="width:200px;">
	            </c:if>
			</colgroup>
			<thead>
				<tr>
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
					<c:if test="${!requestContext.sellerPage}">
					<th scope="col">생년월일</th>
					</c:if>
				</tr>

			</thead>
			<tbody>

				<c:forEach items="${list}" var="orderItem" varStatus="index">

					<tr style="background:#fff;">
						<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
						<c:if test="${requestContext.sellerPage == false}">
							<td><div><c:out value="${orderItem.locgovNm}"/></div></td>
						</c:if>
						<td>
							<c:out value="${op:datetime(orderItem.createdDate)}"/>
						</td>
						<td>
							<a href="${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/order/all/order-detail/${fn:escapeXml(orderItem.orderSequence)}/${fn:escapeXml(orderItem.orderCode)}"><c:out value="${orderItem.orderCode}"/></a><br />
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
								<%-- <c:choose>
									<c:when test="${!empty orderItem.mobileNumber && orderItem.orderStatus == '35'}">
										발송완료
									</c:when>
									<c:otherwise> --%>
										<c:out value="${orderItem.orderStatusLabel}"/>
								<%-- 	</c:otherwise>
								</c:choose> --%>
							</div>
						</td>
						<td>
							<c:if test="${orderItem.shippingDate != '00000000000000'}">
								<c:choose>
									<c:when test="${empty orderItem.deliveryNumber && empty orderItem.mobileNumber}">
										송장 번호 : 없음 (<c:out value="${orderItem.deliveryCompanyName ? orderItem.deliveryCompanyName : '택배사 정보 없음'}"/>)
									</c:when>
									<c:when test="${empty orderItem.deliveryNumber && !empty orderItem.mobileNumber}">
										모바일 : <c:out value="${orderItem.mobileNumber}"/>
									</c:when>
									<c:otherwise>
										송장 번호 : <c:out value="${orderItem.deliveryNumber}"/>(<c:out value="${orderItem.deliveryCompanyName}"/>)
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<c:if test="${!requestContext.sellerPage}">
							<td><div><c:out value="${orderItem.birthday}"/></div></td>
						</c:if>
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

	<%-- <div class="btn_all">
		<div class="flex_box juc-sbt">
			<div class="btn_all">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="downloadOrderExcel(${fn:escapeXml(totalCount)})">엑셀</button>
				</div>
			</div>
		</div>
		<div class="btn_right mb0">

		</div>
	</div> --%>

	<div class="pagination-wrap">
		<page:pagination-manager />
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

	function search_net() {
		if(Common.useNetfunnel()){
			NetFunnel_Action({ action_id: 'se_login' }, function (ev, ret) {
	        	search();
	        });
		}else{
			search();
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

	/* function downloadOrderExcel(totalCount) {
		if (totalCount <= 2000) {
	        Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/list/order-excel-download", $('#orderParam').serialize(), true);
		} else if (confirm('주문 내역이 많아 서버에 무리가 갈 수 있습니다. 진행하시겠습니까?')) {
			Shop.downloadExcelOrder("${requestContext.sellerPage ? '/seller' : '/opmanager'}/order/list/order-excel-download", $('#orderParam').serialize(), true);
		}
    } */

</script>
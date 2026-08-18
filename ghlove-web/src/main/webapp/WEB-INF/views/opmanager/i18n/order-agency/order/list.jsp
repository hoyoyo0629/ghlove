<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<style>
.tbl-summary {
	border-right: 1px solid #dedede;
	border-bottom: 1px solid #dedede;
}
.tbl-summary th,
.tbl-summary td {
	border-left: 1px solid #dedede;
	border-top: 1px solid #dedede;
	text-align: center;
}

.tbl-summary th {
	background: #f4f4f4;
	padding: 10px;
	color: #000;
}
.tbl-summary td {
	padding: 0 10px;
	font-family: verdana;
}
.tbl-summary td {
	padding: 22px;
	font-size: 20px;
	font-family: verdana;

}
</style>
<!-- <div class="admin_wrap"> -->
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<div class="item_list">
		<h3><span>답례품주문대행목록</span></h3>

		<form:form modelAttribute="orderParam" method="post">

			<div class="board_write">
				<table class="board_write_table" summary="답례품 주문대행 목록">
					<caption>답례품 주문대행 목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
						<c:if test="${op:hasRole('ROLE_ADMIN_1')
									|| op:hasRole('ROLE_ADMIN_2')
									|| op:hasRole('ROLE_ADMIN_3')
									|| op:hasRole('ROLE_ADMIN_4')}">
		                    <tr>
		                        <td class="label">지자체</td>
		                        <td colspan="3">
		                            <div class="flex_box gap-08">
			                            <form:select path="upperLocgovCode" class="wd-150" onChange="wdrChange(this.value)">
						                    <form:option value="">-${op:message('M00039')}-</form:option>
						                    <c:forEach items="${wdr}" var="wdr">
						                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
						                    </c:forEach>
						                </form:select>
			                            <form:select path="locgovCode" class="wd-150">
						                    <option value="">-시,군,구-</option>
						                </form:select>
		                            </div>
		                        </td>
		                    </tr>
	                    </c:if>
	                    <tr>
							<td class="label">주문일</td>
	                        <td colspan="3">
								<div>
									<span class="datepicker"><form:input path="searchStartDate" maxlength="8" class="datepicker" title="시작일" /></span>
									<span class="wave">~</span>
									<span class="datepicker mr10"><form:input path="searchEndDate" maxlength="8" class="datepicker" title="종료일" /></span>
									<span class="day_btns mt3" style="margin-left:0px;">
										<a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
										<a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
										<a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
										<a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
										<a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
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
							<td class="label"><c:out value="${op:message('M00011')}"/> <!-- 검색구분 --> </td>
	                        <td colspan="3">
								<div class="flex_box">
									<form:select path="where" title="${op:message('M00011')}" class="wd-150">
										<form:option value="" label="구분" />
										<form:option value="ORDER_CODE" label="주문번호" />
										<form:option value="USER_NAME" label="주문자명" />
										<form:option value="RECEIVE_NAME" label="받는사람" />
										<form:option value="ITEM_NAME" label="답례품명" />
										<form:option value="MANAGER_NAME" label="주문대행 관리자명" />
									</form:select>
									<form:input path="query" class="input_txt _filter" title="${op:message('M00022')}" maxlength="50" style="width: 100%;"/><!-- 검색어 -->
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="btn_all flex_box juc-sbt">
					<div class="btn_left">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:moveOrderAgencyPage();">주문대행 로그인 페이지 이동</button>
					</div>
					<div class="btn_right">
						<div class="flex_box gap-08">
							<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/order-agency/order/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
							<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
						</div>
					</div>
				</div>
			</div>

			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(orderParam.pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
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

			<form id="listForm" method="post">
				<table class="board_list_table" summary="답례품 주문대행 목록">
					<caption>답례품 주문대행 목록</caption>
					<colgroup>
						<col style="width:100px;">
						<col style="width:150px;">
		                <col style="width:100px;">
		                <col style="width:100px;">
		                <col style="width:400px;">
		                <col style="width:100px;">
						<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
							<col style="width:150px;">
						</c:if>
		                <col style="width:150px;">
		                <col style="width:100px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col">주문번호</th>
							<th scope="col">주문일자</th>
							<th scope="col">주문자</th>
							<th scope="col">수취인</th>
							<th scope="col">답례품명</th>
							<th scope="col">주문대행 관리자</th>
							<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
								<th scope="col">관리자 지자체 명</th>
							</c:if>
							<th scope="col">행정복지센터/권한 명</th>
							<th scope="col">인쇄페이지</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ list }" var="order" varStatus="index">
							<tr style="background:#fff;">
								<td>
									<a href="/opmanager/order-agency/order/list/order-detail/${fn:escapeXml(order.orderCode)}"><c:out value="${order.orderCode}"/></a><br />
								</td>
								<td>
									<c:out value="${order.payDate}"/>
								</td>
								<td>
									<c:out value="${order.buyerName}"/>
								</td>
								<td>
									<c:out value="${order.receiveName}"/>
								</td>
								<td>
									<c:out value="${order.itemName}"/>
								</td>
								<td>
									<c:out value="${order.managerNm}"/>
								</td>
								<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
									<td><div><c:out value="${order.locgovNm}"/></div></td>
								</c:if>
								<td>
									<c:out value="${order.pbadmsWlfrCntrNm}"/>
								</td>
								<td>
									<div class="flex_box juc-center gap-08">
										<a href="javascript:movePrintPage('${fn:escapeXml(order.orderCode)}');" class="btn btn-gradient btn-xs">인쇄페이지</a>
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

			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>

		</div>

	</div>
<!-- </div> -->

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>
<script type="text/javascript">

	var changeYn = "N";

	$("select[name='upperLocgovCode']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	})


	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();

		let upperLocgovCode = '<c:out value="${orderParam.upperLocgovCode}"/>';

		if (upperLocgovCode) {
			wdrChange(upperLocgovCode);
		}

		try {
			$('#orderParam').validator(function() {
				let strStartDate = $("#searchStartDate").val();
				let strEndDate = $("#searchEndDate").val();
	            if (strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
	                var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
	                var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

	                if (startDate > endDate) {
	                    alert("종료일이 시작일보다 빠릅니다.");
	                    $("#searchEndDate").focus();
	                    return false;
	                }
	                var searchChk = Common.searchDateMonth(startDate, endDate);
	                if (!searchChk) return false;
	            }
			});
		} catch (e) {
            alert("검색일을 확인해주세요.");
            $("#searchEndDate").focus();
            return false;
		}
	});


	// 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();
		$("#locgovCode option").remove();
		if ($("#upperLocgovCode").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				$('#locgovCode').append('<option value="">-전체-</option>');
				for (var i = 0; i < response.length; i++) {
		            let options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#locgovCode').append(options);
		        }

				// 조회 된 값 유지
				if ("${fn:escapeXml(orderParam.upperLocgovCode)}" != "" && changeYn == 'N') {
				    $("#locgovCode").val('${fn:escapeXml(orderParam.upperLocgovCode)}').prop("selected", true);

				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					//$("#locgovCode option:eq(0)").attr("selected", "selected");
				}
					let locgovCode = '<c:out value="${orderParam.locgovCode}"/>';

				//if (locgovCode) {
					let optionLists = $("#locgovCode")[0];
					for(let option of optionLists) {
							if (option.value == locgovCode) {
								option.setAttribute("selected", "selected");
								break;
							}
						}
				//}
		    });
		} else {
	        $('#locgovCode').append('<option value="">-시,군,구-</option>');
		}
	}
	
	
	function movePrintPage(orderCode) {
		let data = {"orderCode" : orderCode};
		console.log(JSON.stringify(data));
		$.post('/opmanager/order-agency/order/list/getTempOrderInfoKey', data, function(resp){
			if (resp.isSuccess && resp.data) {
				window.open(getFrontUrl() + "/admin/order-agency/order-agency-detail.html?dataId=" + resp.data, "_blank");
			} else {
				if (resp.errorMessage) {
					alert(resp.errorMessage);
				} else {
					alert('문제가 발생했습니다.');
				}
			}

		}, 'json');
	}
	
	function getFrontUrl() {
		let host = location.host;
		let protocol = location.protocol;
		let port = "";
		if (host.indexOf(":") > -1) {
			let hostSplits = host.split(":");
			host = hostSplits[0];
			port = ":3000";
		}
		
		let url = protocol + "//" + host + port;
		
		return url
	}
	
	function moveOrderAgencyPage() {
		window.open(getFrontUrl() + '/admin/order-agency/login.html', "_blank"); 
	}


</script>


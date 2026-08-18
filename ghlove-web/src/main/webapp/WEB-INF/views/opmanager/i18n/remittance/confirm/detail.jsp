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
<h3>정산확정 내역</h3>

<c:if test="${requestContext.sellerPage == false}">
	<h3 class="mt50 fs24"><span>▫ [<c:out value="${seller.companyName}"/>] <c:out value="${op:date(remittanceParam.startDate)}"/></span></h3>
</c:if>
<form:form modelAttribute="remittanceParam" action="" method="post">

<div class="board_write">

	<div class="board_list">

		<c:if test="${requestContext.sellerPage == false}">

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

		<c:if test="${requestContext.sellerPage == true}">

		<table class="board_write_table">
			<colgroup>
                <col style="width:220px;">
			</colgroup>
			<tbody>
	            <%-- <tr>
	                <td class="label">기간검색</td>
	                <td>
						 <div>
						 	<form:select path="" class="wd-150">
								<form:option value="">선택</form:option>
								<c:forEach begin="0" end="100" step="1" var="index">
									<option value="${years - index}" label="${years - index}">
								</c:forEach>
							</form:select>
							<span class="wave"> 년 </span>
							<form:select path="" class="wd-150">
								<form:option value="">선택</form:option>
								<c:forEach begin="1" end="12" step="1" var="index">
									<option value="${index}" label="${index}">
								</c:forEach>
							</form:select>
						 	<span class="wave"> 월 </span>
						 	<span class="wave">~</span>
						 	<form:select path="" class="wd-150">
							<form:option value="">선택</form:option>
								<c:forEach begin="0" end="100" step="1" var="index">
									<option value="${years - index}" label="${years - index}">
								</c:forEach>
							</form:select>
							<span class="wave"> 년 </span>
							<form:select path="" class="wd-150">
								<form:option value="">선택</form:option>
								<c:forEach begin="1" end="12" step="1" var="index">
									<option value="${index}" label="${index}">
								</c:forEach>
							</form:select>
						 	<span class="wave"> 월 </span>
						 </div>
	                </td>
	            </tr> --%>
	            <tr>
	                <td class="label">키워드 검색</td>
	                <td>
	                    <div class="flex_box gap-08">
	                        <form:select path="where" title="키워드 검색" class="wd-200">
	                        	<form:option value="" label="전체" />
								<form:option value="ORDER_CODE" label="주문번호" />
								<form:option value="USER_NAME" label="주문자명" />
								<form:option value="ITEM_CODE" label="답례품번호" />
								<form:option value="ITEM_NAME" label="답례품명" />
	                        </form:select>
	                        <form:input path="query" class="input_txt required _filter full" title="${op:message('M00022')}" maxlength="20" /><!-- 검색어 -->
	                    </div>
	                </td>
	            </tr>
			</tbody>
		</table>

        <div class="btn_all btn_right">
            <div class="flex_box gap-08">
                <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/remittance/confirm/detail/view/${fn:escapeXml(seller.sellerId)}/${fn:escapeXml(remittanceParam.startDate)}'">초기화</button>
                <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
            </div>
        </div>

		</c:if>


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

		<c:if test="${!requestContext.sellerPage}">

		<table class="board_list_table" summary="정산내역 리스트">
			<caption>정산내역 리스트</caption>
			<colgroup>
	            <col style="width:50px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
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
					<th scope="col">답례품코드</th>
					<th scope="col">답례품명</th>
					<th scope="col">옵션</th>
					<th scope="col">주문자명</th>
					<th scope="col">판매가(1개)</th>
					<th scope="col">주문수량</th>
					<th scope="col">정산금액</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${list != null && !list.isEmpty()}">
					<c:forEach items="${list}" var="item" varStatus="index">
						<tr ${item.remittanceStatusCode == '2' ? 'style="background-color:#fff;"' : ''}>
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
							<td>
								<c:choose>
									<c:when test="${item.commissionBasePrice != null && item.commissionBasePrice > 0}">
										<%-- <c:out value="${op:numberFormat(item.commissionBasePrice * item.quantity + item.etcAmt)}"/> P --%>
										<c:out value="${op:numberFormat(item.commissionBasePrice)}"/> P
									</c:when>
									<c:otherwise><!-- 리얼커머스 데이터용 -->
										<%-- <c:out value="${op:numberFormat(item.salePrice * item.quantity + item.etcAmt)}"/> P --%>
										<c:out value="${op:numberFormat(item.salePrice)}"/> P
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:out value="${op:numberFormat(item.quantity)}"/>개</td>
							<td>
								<c:out value="${op:numberFormat(item.remittancePrice * item.quantity + item.etcAmt)}"/> 원
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>

		</c:if>

		<c:if test="${requestContext.sellerPage}">

		<table class="board_list_table" summary="정산내역 리스트">
			<caption>정산내역 리스트</caption>
			<colgroup>
	            <col style="width:50px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
	            <col style="width:300px;">
	            <col style="width:150px;">
	            <col style="width:200px;">
	            <col style="width:200px;">
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
					<th scope="col">옵션명</th>
					<th scope="col">주문자명</th>
					<th scope="col">판매가(1개)</th>
					<th scope="col">주문수량</th>
					<th scope="col">결제금액</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${list != null && !list.isEmpty()}">
					<c:forEach items="${list}" var="item" varStatus="index">
						<tr ${item.remittanceStatusCode == '2' ? 'style="background-color:#fff;"' : ''}>
							<td><c:out value="${op:numberFormat(pagination.itemNumber - index.count)}"/></td>
							<td><c:out value="${op:datetime(item.payDate)}"/></td>
							<td><c:out value="${op:datetime(item.confirmDate)}"/></td>
							<%-- <td>${item.payDate}</td>
							<td>${item.confirmDate}</td> --%>
							<td>
								<a href="${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/order/confirm/order-detail/${fn:escapeXml(item.orderSequence)}/${fn:escapeXml(item.orderCode)}"><c:out value="${item.orderCode}"/></a>
							</td>
							<td><c:out value="${item.itemUserCode}"/></td>
							<td><c:out value="${item.itemName}"/></td>
							<td>
								<c:out value="${shop:viewItemOptions(item.setItemFlag, item.options)}"/>
							</td>
							<td><c:out value="${item.buyerName}"/></td>
							<%-- <td>${op:numberFormat(item.commissionBasePrice * item.quantity + item.etcAmt)}</td> --%>
							<td>
								<c:choose>
									<c:when test="${item.commissionBasePrice != null && item.commissionBasePrice > 0}">
										<%-- <c:out value="${op:numberFormat(item.commissionBasePrice * item.quantity + item.etcAmt)}"/> P --%>
										<c:out value="${op:numberFormat(item.commissionBasePrice)}"/> P
									</c:when>
									<c:otherwise><!-- 리얼커머스 데이터용 -->
										<%-- <c:out value="${op:numberFormat(item.salePrice * item.quantity + item.etcAmt)}"/> P --%>
										<c:out value="${op:numberFormat(item.salePrice)}"/> P
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:out value="${op:numberFormat(item.quantity)}"/> 개</td>
							<td>
								<c:choose>
									<c:when test="${item.commissionBasePrice != null && item.commissionBasePrice > 0}">
										<c:out value="${op:numberFormat(item.commissionBasePrice * item.quantity + item.etcAmt)}"/> 원
									</c:when>
									<c:otherwise><!-- 리얼커머스 데이터용 -->
										<c:out value="${op:numberFormat(item.salePrice * item.quantity + item.etcAmt)}"/> 원
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>

		</c:if>

		<c:if test="${list == null || list.isEmpty()}">
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
				<c:if test="${requestContext.sellerPage}">
					<c:if test="${remittanceStatusCode == 3}">
						<button type="button" class="btn btn-dark-gray btn-mini finishing-remittance" id="remittanceBtn">정산 확인</button>
					</c:if>
					<c:if test="${remittanceStatusCode == 4}">
						<button type="button" class="btn btn-dark-gray btn-mini finishing-remittance" id="remittanceBtn">파일 전송</button>
					</c:if>
				</c:if>
				<%-- <c:if test="${!requestContext.sellerPage && remittanceStatusCode == 4}">
					<button type="button" class="btn btn-dark-gray btn-mini finishing-remittance">첨부파일 보기</button>
				</c:if> --%>
				<a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/list" class="btn btn-dark-gray btn-mini">목록</a>
			</div>
		</div>
	</div>

	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>

	<form id="listForm" action="${fn:escapeXml(requestContext.managerUri)}/remittance/confirm/list/check" method="post">
		<c:if test="${list != null && !list.isEmpty()}">
			<%-- <input type="hidden" name="id" value="${list.get(0).sellerId}^^^${list.get(0).remittanceDate}"/> --%>
			<input type="hidden" name="id" value="${list.get(0).remittanceId}"/>
		</c:if>

	</form>

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

<script type="text/javascript">

	<c:if test="${requestContext.sellerPage}">
		const receiveMsg = async (e) => {
			if (e.data.hasOwnProperty('fnName')) {
				if (e.data.fnName == 'btnChg') {
					btnChg();
				}
			}
		}
	</c:if>

	$(function(){
		$.each($('td.amount'), function(){
			if ($.trim($(this).html()) == '0원') {
				$(this).css('color', '#bfbebe');
			}
		});

		<c:if test="${requestContext.sellerPage}">

			window.addEventListener("message", receiveMsg, false);			// 팝업 통신용

			window.addEventListener("unload", (event) => {		// 화면 닫을 때 팝업 같이 닫기(화면 이동시 팝업만 남아있는 상황 방지)
				if (popup) {
					popup.close();
				}
			});

			$('.finishing-remittance').on('click', function() {

				var $form = $('#listForm');

				openPop();


			});

			<c:if test="${list != null && !list.isEmpty()}">
				remittanceId = "${list.get(0).remittanceId}";
			</c:if>
		</c:if>
	});


	<c:if test="${requestContext.sellerPage}">

		let popupType = "toolbar=no,width=700,height=400,top=150px,left=250px,directories=no,menubar=no,scrollbars=yes,location=no";
		let popup;

		let remittanceId;

		// 정산확인/파일첨부 버튼 클릭시
		function openPop() {
			popup = window.open("${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/popup", 'remittancePopup', popupType);
			//Common.popup("${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/popup", 'remittancePopup', 700, 310, 'y', 150, 250);
		}

		// 팝업에서 호출
		function getRemittanceId() {
			if (popup) {
				popup.sendPopupData(remittanceId);
			} else {
				openPop();
			}
		}

		function btnChg() {
			$("#remittanceBtn").text("파일 전송");
		}

	</c:if>

	function excelDownload() {
		let param = $('#remittanceParam').serialize();

		Shop.downloadExcelOrder("${fn:escapeXml(requestContext.sellerPage) ? '/seller' : '/opmanager'}/remittance/confirm/detailNew-excel/view/${fn:escapeXml(remittanceParam.remittanceId)}", param, true);
	}
</script>
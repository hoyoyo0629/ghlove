<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop" 		uri="/WEB-INF/tlds/shop" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.*" %>


<%
	LocalDate now = LocalDate.now();
	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
	request.setAttribute("today", now.format(fmt));
%>

<c:set var="prefixMonth" value="최근 "/>

<!-- 내용 -->
	<div  class="mainArea">
		<!-- 본문 -->
		<div class="orderList">
			<ul class="list">
				<li class="typeA">
					<%-- <a href="<c:url value="/seller/order/new-order?searchStartDate=${today}&searchEndDate=${today}" />"> --%>
					<a href="<c:url value="/seller/order/new-order" />">
						<p class="num_new" id="new-order-count">0</p>
						<p class="tit">신규주문</p>
					</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%-- <a href="<c:url value="/seller/order/new-order-mobile?searchStartDate=${today}&searchEndDate=${today}" />"> --%>
					<a href="<c:url value="/seller/order/new-order-mobile" />">
						<p class="num_new" id="new-mobile-order-count">0</p>
						<p class="tit">모바일</p>
					</a>
					<p class="info">신규 주문수</p>
				</li>
				<li class="typeA">
					<a href="<c:url value="/seller/order/shipping-ready" />">
						<p class="num_new" id="shipping-ready-count">0</p>
						<p class="tit">배송준비중</p>
					</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="<c:url value="/seller/order/shipping-ready-mobile" />">
						<p class="num_new" id="shipping-ready-mobile-count">0</p>
						<p class="tit">모바일</p>
					</a>
					<p class="info">배송준비중인 주문수</p>
				</li>
				<li class="typeB">
					<a href="<c:url value="/seller/order/shipping" />">
						<p class="num_new" id="shipping-count">0</p>
						<p class="tit">배송중</p>
					</a>
					<p class="info">배송중인 주문수</p>
				</li>
				<li class="typeB">
					<a href="<c:url value="/seller/order/finish" />">
						<p class="num_new" id="finish-count">0</p>
						<p class="tit">배송완료</p>
					</a>
					<p class="info">배송완료된 주문수</p>
				</li>
			</ul><!--// list E-->
			<ul class="list">
				<li class="typeA">
					<a href="<c:url value="/seller/order/confirm?&searchStartDate=${fn:escapeXml(today)}&searchStartDateTime=00&searchEndDate=${fn:escapeXml(today)}&searchEndDateTime=23" />">
						<p class="num_new" id="confirm-count">0</p>
						<p class="tit">구매확정</p>
					</a>
					<p class="info">구매확정된 주문수</p>
				</li>
				<li class="typeA">
					<a href="/seller/order/cancel/list?claimStatus=01">
						<p class="num_re" id="cancel-request-count">0</p>
						<p class="tit">취소요청</p>
					</a>
					<p class="info">현재까지 미처리된 요청 수</p>
				</li>
				<li class="typeB">
					<a href="/seller/order/exchange/list?claimStatus=01">
						<p class="num_re" id="exchange-request-count">0</p>
						<p class="tit">교환요청</p>
					</a>
					<p class="info">현재까지 미처리된 요청 수</p>
				</li>
				<li class="typeB">
					<a href="/seller/order/return/list?claimStatus=01">
						<p class="num_re" id="return-request-count">0</p>
						<p class="tit">반품요청</p>
					</a>
					<p class="info">현재까지 미처리된 요청 수</p>
				</li>
			</ul><!--// list E-->
		</div><!--// orderList E-->

		<div class="orderList" style="margin-top:20px; display:none;">
			<ul class="list">
				<li class="delay">
					<div class="detail det01">
						<a href="<c:url value="/seller/order/new-order"/>">
							<p class="num" id="shipping-delay-count">0</p>
							<p class="tit">배송출고 지연</p>
						</a>
					</div>
					<p class="info">신규주문, 배송준비중 상태의 주문이 ${fn:escapeXml(delayDays.shippingDelay) }일 이상 출고되지 않음</p>
				</li>
				<li class="delay">
					<div class="detail det02">
						<a href="<c:url value="/seller/order/exchange/list"/>">
							<p class="num none" id="exchange-delay-count">0</p>
							<p class="tit">교환 지연</p>
						</a>
					</div>
					<p class="info">교환 승인된 상품의 처리가 ${fn:escapeXml(delayDays.exchangeDelay )}일 이상 회수되지 않음</p>
				</li>
				<li class="delay">
					<div class="detail det03">
						<a href="<c:url value="/seller/order/return/list"/>">
							<p class="num" id="return-delay-count">0</p>
							<p class="tit">반품 지연</p>
						</a>
					</div>
					<p class="info">반품 승인된 상품의 처리가 ${fn:escapeXml(delayDays.returnDelay )}일 이상 회수되지 않음</p>
				</li>
			</ul>
		</div><!--// orderList E-->

		<div class="main_notice clear_fix" style=" display:none;">
			<div class="notice">
				<span class="tit">공지사항</span>
				<c:forEach items="${noticeList }" var="notice">
					<a href="<c:url value="/seller/notice/view/${fn:escapeXml(notice.noticeId) }"/>" class="cont">${fn:escapeXml(notice.subject) }</a>
					<span class="date">${op:date(notice.createdDate) }</span>
				</c:forEach>
			</div>
			<a href="<c:url value="/seller/notice/list"/>" class="more" title="공지사항 더보기"><img src="/content/opmanager/images/btn_more.gif" alt="+"></a>
		</div><!--// main_notice E-->

		<div class="summary_list clear_fix">
			<div class="board" style="width: 49.5%;">
				<p class="list_tit">답례품문의&#40;답변대기&#41;<a href="<c:url value="/seller/qna-item/list?answerCount=2"/>" class="number">${empty qnaCount ? 0 : op:numberFormat(qnaCount) }건</a></p>
				<ul class="list">
					<c:if test="${!empty qnaList}">
						<c:forEach items="${qnaList}" var="qna">
							<li><a href="<c:url value="/seller/qna-item/answer/${fn:escapeXml(qna.qnaId)}" />" class="cont">${fn:escapeXml(qna.subject)}</a><span class="date">${op:date(qna.createdDate)}</span></li>
						</c:forEach>
					</c:if>
					<c:if test="${empty qnaList}">
						<li>답변대기중인 답례품문의가 없습니다.</li>
					</c:if>
				</ul>
			</div><!--// board E-->

			<div class="board" style="width: 49.5%;">
				<p class="list_tit">답례품 후기&#40;비공개&#41;<a href="<c:url value="/seller/item/review/list?reviewDisplayFlag=N"/>" class="number">${empty reviewCount ? 0 : op:numberFormat(reviewCount) }건</a></p>
				<ul class="list">
					<c:if test="${!empty sellerReviewList}">
						<c:forEach items="${sellerReviewList}" var="review">
							<li><a href="<c:url value="/seller/item/review/edit/${fn:escapeXml(review.itemReviewId)}" />" class="cont">${fn:escapeXml(review.subject)}</a><span class="date">${op:date(review.createdDate)}</span></li>
						</c:forEach>
					</c:if>
					<c:if test="${empty sellerReviewList}">
						<li>등록된 답례품문의가 없습니다.</li>
					</c:if>
				</ul>
			</div><!--// board E-->
		</div><!--// summary_list E-->

	</div><!--// mainArea E-->

<script type="text/javascript" src="/content/modules/op.main.js"></script>
<script type="text/javascript" src="/content/popup/popup.js?v=260803"></script>
<script>
$(function() {
	setOrderCount();
	setShippingDelayCount();
	pop.openNotice('S');
	setDisplay();
	window.onresize = function () {
		setDisplay();
	}

	// 2026-06-05 인천행정체제 개편으로 인한 중구동구서구 관리자 메시지 표시
	//var showIncheonPopup = "${fn:escapeXml(showIncheonPopup)}";
	//if (showIncheonPopup != null && showIncheonPopup == "Y") {
	//	// const today = new Date();
	//	// const target = new Date(today.getFullYear(), 5, 13); // 월은 0부터 시작 → 5 = 6월 / 06월 12일 23시 59분 59초까지(06.12 00:00:00)
	//	// if(today <= target) {
	//		var d = {
	//				width : 450,
	//				height : 430,
	//				subject : '답례품 제공자 안내사항',
	//				// content : '2026년 7월 1일 인천시 행정체제 개편에 따라 인천 중구, 동구, 서구로 주문된 답례품은 6월 30일까지 배송이 완료되도록 처리하여 주시기 바랍니다.',
	//				content : '<img style="max-width: 100%; height: auto" src="/content/images/popup/area_change_seller.png" alt="2026년 7월 1일 인천시 행정체제 개편에 따라 인천 중구, 동구, 서구로 주문된 답례품은 6월 30일까지 배송이 완료되도록 처리하여 주시기 바랍니다."/>',
	//				managerNoticeId : 9998
	//		};

	//		pop.openInfoPopup(d, 1, 10);
	//	// }
	//}

	var showPopup2 = "${fn:escapeXml(showPopup2)}";
	if(showPopup2 != null && showPopup2 == "Y") { //

		var d = {
				width : 550,
				height : 570,
				subject : '답례품 제공자 안내사항',
				//content : '내용',
				content : '<img src="/upload/popup/264/202608031449447664.png" style="width:100%;height:440px;padding:0px;object-fit:contain;" alt="보안강화 안내">', //운영
				//content : '<a href="http://localhost:3000/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607281714265325.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
				//content : '<a href="http://152.99.104.8/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607281714265325.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
				managerNoticeId : 9991
		};

		pop.openInfoPopup2(d, 10, 11);
	}
});

function setDisplay() {
	let footerPaddingTop = window.getComputedStyle($("#footer")[0], null).getPropertyValue('padding-top');
	let footerPaddingBottom = window.getComputedStyle($("#footer")[0], null).getPropertyValue('padding-bottom');

	footerPaddingTop = footerPaddingTop.substring(0, footerPaddingTop.indexOf('px'));
	footerPaddingBottom = footerPaddingBottom.substring(0, footerPaddingBottom.indexOf('px'));

	$('#container').attr('style', 'height:' + (window.innerHeight - $("#header").height() - $("#footer").height() - Number(footerPaddingTop) - Number(footerPaddingBottom)) + 'px;');
}



//주문내역 Count
function setOrderCount() {
	$.post('/common/seller/order-count', {'month' : 0 }, function(resp){
		if (resp.data == undefined) {
			return;
		}
		$.each(resp.data, function(i, state) {
			$object = $('p#' + state.key + "-count");
			if ($object.size() > 0) {
				$object.html(Common.numberFormat(state.count));
			}

		});

	}, 'json');
}

<%--이상우 [2017-05-11 추가]--%>
//배송,교환,반품 지연 Count
function setShippingDelayCount() {
	 $.post('/common/opmanager/shipping-delay-count', null, function(resp){

		if (resp.data == undefined) {
			return;
		}

		$.each(resp.data, function(i, state) {
			$object = $('p#' + state.id + "-count");
	 		if ($object.size() > 0) {
				$object.html(Common.numberFormat(state.count));
			}

		});

	}, 'json');
}


</script>

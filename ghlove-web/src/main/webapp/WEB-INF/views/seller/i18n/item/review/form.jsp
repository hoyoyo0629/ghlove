<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>


		<!-- 상단 타이틀 영역 -->
        <div class="location">
            <a href=""></a> &gt; <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
        </div>
        <h3><span></span></h3>
        <!-- // 상단 타이틀 영역 -->

     	<form id="itemParam" method="post" action="/seller/item/review/list">
			<input type="hidden" name="where" value="${fn:escapeXml(itemParam.where)}">
			<input type="hidden" name="query" value="${fn:escapeXml(itemParam.query)}">
			<input type="hidden" name="sellerId" value="${fn:escapeXml(itemParam.sellerId)}">
			<input type="hidden" name="reviewDisplayFlag" value="${fn:escapeXml(itemParam.reviewDisplayFlag)}">
			<input type="hidden" name="recommendFlag" value="${fn:escapeXml(itemParam.recommendFlag)}">
			<input type="hidden" name="searchStartDate" value="${fn:escapeXml(itemParam.searchStartDate)}">
			<input type="hidden" name="searchEndDate" value="${fn:escapeXml(itemParam.searchEndDate)}">
			<input type="hidden" name="reviewScore" value="${fn:escapeXml(itemParam.reviewScore)}">
		</form>

     	<form:form modelAttribute="itemReview" method="post" enctype="multipart/form-data">
			<form:hidden path="itemReviewId" />
			<form:hidden path="item.itemName" />

	        <div class="btn_all btn_right mb15">
	            <div class="flex_box gap-08">
	                <button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:validCheck(event);">${op:message('M00087')}</button><!-- 수정 -->
	                <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteItemReview(${fn:escapeXml(itemReview.itemReviewId)});">${op:message('M00074')}</button><!-- 삭제 -->
	                <button type="button" class="btn btn-default btn-mini" onclick="javascript:reviewList();">${op:message('M00480')}</button><!-- 목록 -->
	            </div>
	        </div>

	        <div class="board_write">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
	                    <col>
	                    <col style="width:220px;">
	                </colgroup>
	                <tbody>
	                    <tr>
	                        <td class="label">${op:message('M00472')}</td><!-- 작성자 -->
	                        <td>
	                            <div>
									${fn:escapeXml(itemReview.userName)}
									<form:input type="hidden" path="loginId" value="${fn:escapeXml(itemReview.loginId)}" />
	                            </div>
	                        </td>
	                        <td class="label">${op:message('M00276')}</td><!-- 작성일 -->
	                        <td>
	                            <div>
									${op:datetime(itemReview.createdDate)}
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('M00018')}</td><!-- 상품명 -->
	                        <td colspan="3">
	                            <div>
	                                <c:choose>
										<c:when test="${op:property('saleson.view.type') eq 'api'}">
											<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(itemReview.item.itemUserCode)}" target="_blank">
												${fn:escapeXml(itemReview.item.itemName)} [ ${fn:escapeXml(itemReview.item.itemUserCode)} ]
											</a>
										</c:when>
										<c:otherwise>
											<a href="/products/view/${fn:escapeXml(itemReview.item.itemUserCode)}" target="_blank">
												${fn:escapeXml(itemReview.item.itemName)} [ ${fn:escapeXml(itemReview.item.itemUserCode)} ]
											</a>
										</c:otherwise>
									</c:choose>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('M00757')}</td><!-- 평가 -->
	                        <td colspan="3">
	                            <div>
	                                <%-- <form:select path="score" title="${op:message('M00757')}" class="wd-150"  readonly="true"><!-- 평가 -->
	                                    <form:option value="1">★</form:option>
										<form:option value="2">★★</form:option>
										<form:option value="3">★★★</form:option>
										<form:option value="4">★★★★</form:option>
										<form:option value="5">★★★★★</form:option>
									</form:select> --%>
									<form:input path="score" type="hidden"/>
									<input id="starScore" title="${op:message('M00757')}" class="wd-150" type="text" readonly="true"/>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('제목')}</td><!-- 제목 -->
	                        <td colspan="3">
	                            <div>
	                                <form:input path="subject" title="${op:message('제목')}" class="input_txt required _filter full" type="text" />
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('M00006')}</td><!-- 내용 -->
	                        <td colspan="3">
	                            <div>
	                                <div class="file_camera">
	                                	<c:forEach items="${itemReview.itemReviewImages}" var="image" varStatus="i">
	                                		<c:if test="${image.itemReviewImageId > 0}">
			                                	<div class="image-box" id="item-reivew-image-${fn:escapeXml(image.itemReviewImageId)}">
			                                        <img src="${fn:escapeXml(image.imageSrc)}" class="item_image size-100 none" alt="${op:message('첨부이미지')}"><!-- 첨부이미지 -->
			                                        <%-- <a class="delete_image" data-id="${image.itemReviewImageId}" data-image="${image.reviewImage}">
			                                        	<img src="/content/images/btn/file_close.gif" alt="close" style="cursor:pointer;">
		                                        	</a> --%>
			                                    </div>
		                                    </c:if>
	                                	</c:forEach>
	                                </div>

	                                <span class="placeholder_wrap">
	                                    <form:textarea path="content" cols="30" rows="10" maxlength="140" class="required _filter" title="${op:message('M00006')}"></form:textarea><!-- 내용 -->
	                                </span>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('관리자 댓글')}</td><!-- 관리자 댓글 -->
	                        <td colspan="3">
	                            <div>
	                                <span class="placeholder_wrap">
	                                    <form:textarea path="adminComment" cols="30" rows="10" maxlength="140" class="_filter" title="${op:message('관리자 댓글')}"></form:textarea><!-- 관리자 댓글 -->
	                                </span>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label">${op:message('M00191')}</td><!-- 공개유무 -->
	                        <td colspan="3">
	                            <div class="flex_box gap-12">
	                            	<form:radiobutton path="displayFlag" value="Y" label="${op:message('M00096')}" /> <!-- 공개 -->
									<form:radiobutton path="displayFlag" value="N" label="${op:message('M00097')}" /> <!-- 비공개 -->
	                            </div>
	                        </td>
	                    </tr>
	                </tbody>
	            </table>
	        </div>
        </form:form>
        <!-- // 조회 영역 -->

<!--<module:smarteditorInit />
<module:smarteditor id="content" />-->

<script type="text/javascript">

$(function() {

	Common.checkedMaxStringLength($('#adminComment'), null, 1000);

	// validator
	try{
		$('#itemReview').validator(function() {
		});
	} catch(e) {
		alert(e.message);
	}
	let score = "";
	let reviewScore = Number("${fn:escapeXml(itemReview.score)}");
	if (typeof reviewScore == "number") {
		for(let i = 0 ; i < reviewScore ; i++) {
			score += "★";
		}
	}
	$("#starScore").val(score);
	//deleteItemReviewImage();
});

// 목록페이지로 이동
function reviewList() {
	location.href='/seller/item/review/list';
}
<%--
// 리뷰 이미지 삭제
function deleteItemReviewImage() {
	$('.delete_image').on('click', function() {
		var param = {
			'itemReviewId': $('#itemReviewId').val(),
			'itemReviewImageId': $(this).data('id'),
			'reviewImage': $(this).data('image')
		};

		//if(confirm("${op:message('M00755')}")) {		// 이미지가 실제로 삭제됩니다.(복구불가)\\n삭제하시겠습니까?		// M00755 확인 필요... db 에는 \ 하나만 들어가야 함
		if(confirm("${op:message('M00196')}")) {		// 삭제하시겠습니까?
			$.post("/seller/item/delete-item-review-image", param, function(resp){
				Common.responseHandler(resp, function(){
					$("#item-reivew-image-" + param.itemReviewImageId).remove();
					alert("${op:message('M00538')}");		// 이미지파일이 삭제되었습니다.
				});
			});
		}
	});
}
--%>

// 리뷰 삭제
function deleteItemReview(itemReviewId) {
	if (itemReviewId) {
		if (!confirm('${op:message("M00196")}')) {		// 삭제하시겠습니까?
			return;
		}
		Common.loading.show();
		$.ajax ({
			url	: "/seller/item/review/delete/" + itemReviewId,
			type	: "POST",
			//timeout : 3000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
			data  : "", // 요청 시 포함되어질 데이터
			processData : true,
			contentType : "application/json",
			dataType    : "json",
			success : function(response, status, xhr) {
				Common.responseHandler(response, function(resp){
					alert("${op:message('M00205')}");			// 삭제되었습니다.
			    	reviewList();
				});
			},
			error	: function(xhr, status, error) {
				try {
					popupWindow.openMsg(Message.get("실패했습니다."));		// 실패했습니다.
				} catch (e) {
					alert(Message.get("실패했습니다."));		// 실패했습니다.
				}
			},
			complete : function(xhr, status) {
				Common.loading.hide();
			}
		});
	}
}

// 수정 전 컨펌
function validCheck(event) {
	if (!confirm("${op:message('정보를 수정 하시겠습니까?')}")) {		// 정보를 수정 하시겠습니까?
		event.preventDefault();
	}
}


</script>

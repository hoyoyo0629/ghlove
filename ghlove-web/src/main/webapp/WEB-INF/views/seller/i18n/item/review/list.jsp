<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

		<div class="location">
            <a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#"></a>
        </div>

        <div class="item_list">
            <h3><span>${op:message('M00716')}</span></h3><!-- 상품후기 - 관리 -->
			<form:form modelAttribute="itemParam" method="post" enctype="multipart/form-data">
	            <!-- 조회 조건 테이블 -->
	            <div class="board_write">
	                <table class="board_write_table" summary="${op:message('M00716')}"><!-- 상품후기 - 관리 -->
	                    <caption>${op:message('M00716')}</caption><!-- 상품후기 -  관리 -->
	                    <colgroup>
	                        <col style="width: 220px" />
	                        <col>
	                        <col style="width: 220px" />
	                        <col>
	                    </colgroup>
	                    <tbody>
	                        <tr>
	                            <td class="label">${op:message('M00011')}</td><!-- 검색구분 -->
	                            <td colspan="3">
	                                <div class="flex_box gap-08">
	                                    <form:select path="where" title="${op:message('M00011')}" class="wd-150"><!-- 검색구분 -->
	                                        <form:option value="">${op:message('M00039')}</form:option><!-- 전체 -->
	                                        <form:option value="ITEM_NAME">${op:message('M00018')}</form:option> <!-- 상품명 -->
											<form:option value="ITEM_USER_CODE">${op:message('M00783')}</form:option> <!-- 상품코드 -->
											<form:option value="USER_NAME">${op:message('M00472')}</form:option> <!-- 작성자 -->
											<form:option value="COMPANY_NAME">${op:message('M00104')}</form:option> <!-- 상호명 -->
	                                    </form:select>
	                                    <form:input path="query" title="${op:message('M00011')}" class="input_txt required _filter full" type="text" /><!-- 검색구분 -->
	                                </div>
	                            </td>
	                        </tr>
	                        <tr>
	                           <td class="label">${op:message('M00276')}</td><!-- 작성일 -->
	                           <td colspan="3">
	                               <div>
	                                   <span class="datepicker">
	                                       <form:input path="searchStartDate" title="${op:message('M00507')}"
	                                           class="datepicker" type="text" maxlength="8"
	                                           autocomplete="off" />		<%-- 시작일 --%>
	                                   </span>
	                                   <span class="wave">~</span>
	                                   <span class="datepicker">
	                                       <form:input path="searchEndDate" title="${op:message('M00509')}"
	                                           class="datepicker" type="text" maxlength="8"
	                                           autocomplete="off" />		<%-- 종료일 --%>
	                                   </span>
	                                   <span class="day_btns">
	                                    <a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
	                                    <a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
	                                    <a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
	                                    <a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
	                                    <a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
	                                </span>
	                               </div>
	                           </td>
	                        </tr>
	                        <tr>
	                            <td class="label">${op:message('M00191')}</td><!-- 공개유무 -->
	                            <td>
	                              <div class="flex_box gap-12">
	                                <div class="input-form">
	                                	<form:radiobutton path="reviewDisplayFlag" value="" label="${op:message('M00039')}" /><!-- 전체 -->
	                                </div>
	                                <div class="input-form">
	                                	<form:radiobutton path="reviewDisplayFlag" value="Y" label="${op:message('M00096')}" />   <!-- 공개 -->
	                                </div>
	                                <div class="input-form">
	                                	<form:radiobutton path="reviewDisplayFlag" value="N" label="${op:message('M00097')}" /> <!-- 비공개 -->
	                                </div>
	                              </div>
	                            </td>
	                            <td class="label">${op:message('M00757')}</td><!-- 평가 -->
	                            <td>
	                              <div class="flex_box">
	                                <form:select path="reviewScore" title="${op:message('M00757')}" class="wd-150"><!-- 평가 -->
	                                    <form:option value="0">${op:message('M00039')}</form:option> <!-- 전체 -->
										<form:option value="1">★</form:option>
										<form:option value="2">★★</form:option>
										<form:option value="3">★★★</form:option>
										<form:option value="4">★★★★</form:option>
										<form:option value="5">★★★★★</form:option>
	                                </form:select>
	                              </div>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div> <!-- // board_write -->
	            <!-- // 조회 조건 테이블 -->
				<div class="btn_all btn_left">
					<ul class="list-bullet point">
				        <li>
				            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
				        </li>
			        </ul>
			  	</div>
	            <div class="btn_all btn_right">
	                <div class="flex_box gap-08">
	                    <button type="button" class="btn btn-dark-gray btn-mini"
	                        onclick="location.href='/seller/item/review/list';">${op:message('M00047')}</button><!-- 초기화 -->
	                    <button type="submit" class="btn btn-dark-gray btn-mini">${op:message('M00048')}</button><!-- 검색 -->
	                </div>
	            </div>


	            <div class="count_title mt-40">
	                <h5>${op:message('M00045')} ${fn:escapeXml(sellerReviewCount)}${op:message('M00272')}</h5> <!-- 총 n건 -->
	                <span>
	                    <form:select path="itemsPerPage" title="${op:message('화면출력수')}"> <!-- 화면출력수 -->
	                        <form:option value="10" selected="selected">${op:message('M00240')}</form:option><!-- 10개 출력 -->
	                        <form:option value="20">${op:message('M00241')}</form:option><!-- 20개 출력 -->
	                        <form:option value="50">${op:message('M00242')}</form:option><!-- 50개 출력 -->
	                        <form:option value="100">${op:message('M00243')}</form:option><!-- 100개 출력 -->
	                    </form:select>
	                </span>
	            </div>
            </form:form>

            <!-- 조회 결과 테이블 -->
            <form id="listForm">
	            <div class="admin_wrap board_list" >
	                <table class="board_list_table" summary="${op:message('상품승인관리리스트')}"><!-- 상품승인관리리스트 -->
	                    <caption>${op:message('MENU_16351')}${op:message('M00100')}</caption><!-- 상품승인관리리스트 -->
	                    <colgroup>
	                        <col style="width:50px;">
	                        <col style="width:50px;">
	                        <col style="width:100px;">
	                        <col style="width:300px;">
	                        <col style="width:150px;">
	                        <col style="width:200px;">
	                        <col style="width:300px;">
	                        <col style="width:200px;">
	                    </colgroup>
	                    <thead>
	                        <tr>
	                            <th><input type="checkbox" id="check_all" title="${op:message('M00169')}" /></th><!-- 체크박스 -->
	                            <th>${op:message('No.')}</th><!-- No. -->
	                            <th>${op:message('M00191')}</th><!-- 공개유무 -->
	                            <th>${op:message('M00006')}</th><!-- 내용 -->
	                            <th>${op:message('M00472')}</th><!-- 작성자 -->
	                            <th>${op:message('M00757')}</th><!-- 평가 -->
	                            <th>${op:message('M00018')}</th><!-- 상품명 -->
	                            <th>${op:message('M00276')}</th><!-- 작성일 -->
	                        </tr>
	                    </thead>
	                    <tbody class="sortable">
	                    	<c:forEach items="${sellerReviewList}" var="list" varStatus="i">
		                        <tr>
		                            <!-- 체크박스 -->
		                            <td>
		                                <div>
		                                    <input type="checkbox" name="id" id="check" value="${fn:escapeXml(list.itemReviewId)}">
		                                </div>
		                            </td>
		                            <!-- // 체크박스 -->
		                            <!-- No. -->
		                            <td>
		                                <div>${pagination.itemNumber - i.count}</div>
		                            </td>
		                            <!-- // No. -->
		                            <!-- 공개유무 -->
		                            <td>
		                                <div>${list.displayFlag == "Y" ? op:message('M00096') : op:message('M00097')}</div>
		                            </td>
		                            <!-- // 공개유무 -->
		                            <!-- 내용 -->
		                            <td class="left break-word">
		                                <div class="flex_box item-center gap-08">
		                                 <a href="javascript:reviewDetail('${fn:escapeXml(list.itemReviewId)}');" class="break-word">
		                                 	<c:if test="${list.itemReviewImages != null && list.itemReviewImages.size() > 0 && list.itemReviewImages[0].getItemReviewImageId() > 0}">
		                                        <img src="${fn:escapeXml(list.thumbnailSrc)}" class="item_image" alt="${op:message('M00983')}" /><!-- 내용이미지 -> 상세이미지 -->
	                                        </c:if>
	                                        ${op:strcut(list.subject, 20)}		<!-- 제목 -->
	                                        <%-- ${op:strcut(list.content,50)}	<!-- 내용 --> --%>

		                                 </a>
		                                </div>
		                            </td>
		                            <!-- // 내용 -->
		                            <!-- 작성자 -->
		                            <td>
		                                <div>${fn:escapeXml(list.userName)}</div>
		                            </td>
		                            <!-- // 작성자 -->
		                            <!-- 평가 -->
		                            <td>
		                                <div>${fn:escapeXml(list.starScore)}</div>
		                            </td>
		                            <!-- // 평가 -->
		                            <!-- 상품명 -->
		                            <td>
		                                <div>[${fn:escapeXml(list.item.itemUserCode)}]</div>
		                                <div>${fn:escapeXml(list.item.itemName)}</div>
		                            </td>
		                            <!-- // 상품명 -->
		                            <!-- 작성일 -->
		                            <td>
		                                <div>${op:date(list.createdDate)}</div>
		                            </td>
		                            <!-- // 작성일 -->
		                        </tr>
	                        </c:forEach>
	                    </tbody>
	                </table>

	                <c:if test="${empty sellerReviewList}">
	                    <div class="no_content">
	                        ${op:message('M00473')}
	                        <!-- 데이터가 없습니다. -->
	                    </div>
	                </c:if>

	                <div class="btn_all btn_right">
	                    <div class="flex_box gap-08">
	                        <button type="button" id="delete_list_data" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteItemReview();">${op:message('M00074')}</button> <!-- 삭제 -->
	                    </div>
	                </div>

	                <!-- 페이지네이션 -->
	                <div class="pagination-wrap">
	                	<page:pagination-manager />
	                </div>
	                <!-- // 페이지네이션 -->
	            </div> <!-- // board_list -->
            </form>
            <!-- // 조회 결과 테이블 -->
        </div>

<div style="display: none;">
	<span id="today">${fn:escapeXml(today)}</span>
	<span id="week">${fn:escapeXml(week)}</span>
	<span id="month1">${fn:escapeXml(month1)}</span>
	<span id="month3">${fn:escapeXml(month3)}</span>
</div>

<script type="text/javascript">
$(function() {

	Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
	EventHandler.calendarStartDateAndEndDateVaild();
	//페이지당 보여줄 데이터 수량지정
	/* $('#itemsPerPage').on("change", function(){
		$('#itemParam').submit();
	}); */
});

function deleteCheck(reviewId) {

	if (typeof reviewId == "undefined" || reviewId == null || reviewId == '') {
		return false;
	}else{

		if(confirm(Message.get("M00196"))) {	// 삭제하시겠습니까?

			$.post("/seller/item/review/delete/" + reviewId, {}, function(response){
				Common.responseHandler(response, function(){
					alert("${op:message('M00205')}"); //삭제되었습니다.
					location.reload();
				});
			}, 'json');
		}
	}

}
function reviewDetail(reviewId) {
	location.href='/seller/item/review/edit/'+reviewId;
}


// 삭제 버튼 클릭시
function deleteItemReview() {
	Common.updateListData("/seller/item/review/delete-list", "${op:message('M00196')}"			// 삭제하시겠습니까?
			, function () {
		alert('삭제되었습니다.');
		location.reload();
	}
	, function (response) {
		alert(response.errorMessage);
		location.reload();
	});
}

</script>
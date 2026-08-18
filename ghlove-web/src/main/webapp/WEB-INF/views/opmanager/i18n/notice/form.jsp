<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


		<div class="location">
			<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
		</div>

		<!--${op:message('M00269')} 시작-->
		<h3><span><c:out value="${op:message('M00269')}"/></span></h3>

		<form:form modelAttribute="notice" method="post" action='${fn:escapeXml(requestContext.requestUri)}'>
			<form:hidden path="noticeId"/>
			<input type="hidden" name="noticeParam" id="noticeParam" />


		<c:if test="${notice.noticeId != 0}">
		<div class="btn_all btn_right mb15">
		    <div class="flex_box gap-08">
		        <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
		        <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteNotice(${fn:escapeXml(notice.noticeId)})">삭제</button>
		        <button type="button" class="btn btn-dark-default btn-mini" onClick="location.href='/opmanager/notice/list'">목록</button>
		    </div>
		</div>
		</c:if>


		<div class="board_write">
			<table class="board_write_table" summary="${op:message('M00269')}">
				<caption><c:out value="${op:message('M00269')}"/></caption>
				<colgroup>
					<col style="width:220px;">
				</colgroup>
				<tbody>
					<tr>
						<td class="label">상단공지</td>
						<td>
							<div class="flex_box gap-08 item-center">
								<div class="checkbox">
									<form:checkbox path="noticeFlag" label="${op:message('M00083')}" value="Y" />
								</div>
							</div>
						</td>
					</tr>
					<%--
					 <tr>
					 	<td class="label">노출위치</td> <!-- 노출위치 -->
					 	<td>
					 		<div>
					 			<p>
					 				<form:radiobutton path="visibleType" label="전체" value="1" title="노출위치" cssClass="required" />
					 				<form:radiobutton path="visibleType" label="사용자" value="2" title="노출위치" cssClass="required"  />
					 				<form:radiobutton path="visibleType" label="판매자" value="3" title="노출위치" cssClass="required"  />
								</p>
							</div>
					 	</td>
					 </tr>
					 <c:if test="${notice.visibleType != '3' || notice.noticeId == 0}">
					 	<c:set var="sellerFlagHide" value="hide"/>
					 </c:if>
					 <tr class="${sellerFlagHide}" id="sellerFlag-tr">
					 	<td class="label">판매자별노출여부</td> <!-- 판매자별노출여부 -->
					 	<td>
					 		<div>
					 			<p>
					 				<form:radiobutton path="sellerSelectFlag" label="전체노출" value="N" title="판매자별노출여부" cssClass="required" />
					 				<form:radiobutton path="sellerSelectFlag" label="판매자별노출" value="Y" title="판매자별노출여부" cssClass="required"  />
								</p>
							</div>
					 	</td>
					 </tr>
					 <c:if test="${notice.sellerSelectFlag == 'N' || notice.noticeId == 0}">
					 	<c:set var="sellerHide" value="hide"/>
					 </c:if>
					 <tr>
						<tr class="${sellerHide}" id="seller-tr">
						 	<td class="label">${op:message('M01630')}</td> <!-- 판매자명 -->
						 	<td>
						 		<div id="seller-div">

									<select name="sellerId" id="sellerId" title="${op:message('M01630')}" multiple="multiple" size="5" style="min-width:120px;"> <!-- 판매자선택 -->
										<c:forEach items="${sellerList}" var="list" varStatus="i">
										<option value="${list.sellerId}">${list.sellerName}</option>
										</c:forEach>
									</select>
									<a href="javascript:addSeller();" class="table_btn">추가</a>

									<c:if test="${!empty noticeSellerList}">
										<ul id="seller_box" class="notice_seller_box ui-sortable">
										<c:forEach items="${noticeSellerList}" var="seller">
											<li id="item_category_${seller.sellerId}">
												${seller.sellerName}<a href="javascript:deleteNoticeSeller(${seller.noticeSellerId}, ${seller.sellerId})" class="delete">[삭제]</a>
											</li>
										</c:forEach>
									</c:if>
								</div>
						 	</td>
						 </tr>
					 </tr>
					 <tr>
					 	<td class="label">URL ${op:message('M01290')}</td> <!-- 속성 -->
					 	<td>
					 		<div>
					 			<form:checkbox path="targetOption" label="${op:message('M01035')}" value="Y" /> &nbsp;&nbsp; <!-- 새창으로 열기 -->
					 			<form:checkbox path="relOption" label="rel=\"nofollow\"" value="Y" />
							</div>
					 	</td>
					 </tr>

					 <tr>
					 	<td class="label">${op:message('M00278')}</td>
					 	<td>
					 		<div>
					 			<input type="text" name="link" title="${op:message('M00278')}" class="nine optional _filter" maxlength="255" value="${notice.url}"/>
							</div>
					 	</td>
					 </tr>
					 --%>
					 <tr>
					 	<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00275')}"/></td>
					 	<td>
					 		<div>
					 			<form:input path="subject" title="${op:message('M00275')}" cssClass="input_txt required _filter half" maxlength="255" />
							</div>
					 	</td>
					 </tr>
					 <tr>
					 	<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td>
					 	<td>
							<!-- smart_editor2 area -->
			                <div>
			                    <form:textarea path="content" cols="30" rows="20" class="editor-content" title="${op:message('M00006')}" />
			                </div>
			                <!-- smart_editor2 area -->
					 	</td>
					 </tr>
					<tr>
						<td class="label"><span class="required_mark">*</span>게시여부</td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<form:radiobutton path="useYn" label="게시" value="Y" title="게시여부" cssClass="required" checked="checked" />
								</div>
								<div class="input-form">
									<form:radiobutton path="useYn" label="중지" value="N" title="게시여부" cssClass="required" />
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>

			<!-- 버튼시작 -->
			<c:if test="${notice.noticeId == 0}">
			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
				<button type="submit" class="btn btn-dark-gray btn-small"><span>등록</span></button>
 				<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/notice/list'"><c:out value="${op:message('M00037')}"/></button>

 				</div>
			</div>
			</c:if>
			<!-- 버튼 끝-->

		</div> <!-- // board_write -->

		</form:form>

		<%-- 검색 후 목록으로 이동하기 위한 조건 2017-03-09 yulsun.yoo
		<form:form modelAttribute="searchParam" action="/opmanager/notice/list" method="post">
			<form:hidden path="sort"/>
			<form:hidden path="orderBy"/>
			<form:hidden path="itemsPerPage"/>
			<form:hidden path="where"/>
			<form:hidden path="query"/>
			<form:hidden path="noticeFlag"/>
			<form:hidden path="visibleType"/>
		</form:form>
		--%>

	<!--// ${op:message('M00269')} 끝-->
	<module:smarteditorInit />
	<module:smarteditor id="content" />

<script type="text/javascript">

$(function() {

	try{
		$('#notice').validator(function() {
			Common.getEditorContent("content");

			if ($('#content').val().toLowerCase() == '<p>&nbsp;</p>' || $('#content').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

			if($("input[name='sellerSelectFlag']:checked").val() == 'Y'){

				if($('#seller_box > li').length == 0){
					alert('판매자를 선택해주세요.'); return false;
				}
			}

			$("#noticeParam").val($("#searchParam").serialize());
		});
	} catch(e) {
		alert(e.message);
	}


	$("input[name='visibleType']").on("change",function(){
		if($(this).val() != '3'){
			$('#sellerFlag-tr').addClass('hide');
		}else{
			$('#sellerFlag-tr').removeClass('hide');
		}
	});

	$("input[name='sellerSelectFlag']").on("change",function(){
		if($(this).val() == 'N'){
			$('#seller-tr').addClass('hide');
		}else{
			$('#seller-tr').removeClass('hide');
		}
	});
});

function addSeller(){

	$("#sellerId option:selected").each(function() {

		var sellerId = $(this).val();
		//id="item_categories" class="sortable_item_category notice_seller_box ui-sortable"
		if($('#item_category_'+sellerId).length == 0){
			if($('#seller_box').length > 0){
				$('#seller_box').append('<li id="item_category_'+sellerId+'">'+$(this).text()+'<a href="javascript:deleteSeller('+sellerId+')" class="delete">[삭제]</a><input type="hidden" name="sellerIds" value="'+sellerId+'"></li>');
			}else{
				$('#seller-div').append('<ul id="seller_box" class="notice_seller_box ui-sortable"><li id="item_category_'+sellerId+'">'+$(this).text()+'<a href="javascript:deleteSeller('+sellerId+')" class="delete">[삭제]</a><input type="hidden" name="sellerIds" id="sellerIds-'+sellerId+'" value="'+sellerId+'"></li></ul>');

			}

		}
	});
}

function deleteSeller(sellerId){
	$('#item_category_'+sellerId).remove();
	$('sellerIds-'+sellerId).remove();
	if($('#seller_box > li').length == 0){
		$('#seller_box').remove();
	}
}

function deleteNoticeSeller(noticeSellerId, sellerId){

	$.post('/opmanager/notice/delete-notice-seller/'+noticeSellerId, null, function(response) {
		Common.responseHandler(response, function(response) {

			$('#item_category_'+sellerId).remove();
			if($('#seller_box > li').length == 0){
				$('#seller_box').remove();
			}
			alert('삭제되었습니다.');

		}, function(response){

			alert(response.errorMessage);
		});
	}, 'json');

}

function noticeList() {
	$('#searchParam').submit();
}

function deleteNotice(noticeId) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/notice/delete/" + noticeId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/notice/list";
// 				location.reload();
			});
		});
	});

}
</script>


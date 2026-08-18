<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

		<div class="location">
			<a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
		</div>
		<h3><span></span></h3>

		<form:form modelAttribute="notice" method="post" action='${fn:escapeXml(requestContext.requestUri)}'>
			<form:hidden path="noticeId"/>
			<input type="hidden" name="noticeParam" id="noticeParam" />

		<c:if test="${notice.noticeId != 0}">
		<div class="btn_all btn_right mb15">
		    <div class="flex_box gap-08">
		        <c:if test="${role == 'LOC' }">
			        <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
			        <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteNotice(${fn:escapeXml(notice.noticeId)})">삭제</button>
		        </c:if>
		        <button type="button" class="btn btn-dark-default btn-mini" onClick="location.href='/opmanager/locgov-notice/list'">목록</button>
		    </div>
		</div>
		</c:if>

		<div class="board_write">
			<table class="board_write_table" summary="${op:message('M00269')}">
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
							<form:textarea path="content" cols="30" rows="20" class="w90 editor-content" title="${op:message('M00006')}" />
						</div>
						<!-- smart_editor2 area -->
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>게시여부</td>
					<td>
						<div class="flex_box gap-12">
							<form:radiobutton path="useYn" label="게시" value="Y" title="게시여부" cssClass="required" checked="checked" />
							<form:radiobutton path="useYn" label="중지" value="N" title="게시여부" cssClass="required" />
						</div>
					</td>
				</tr>
				</tbody>
			</table>

			<c:if test="${notice.noticeId == 0}">
			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
					<button type="submit" class="btn btn-dark-gray btn-small"><span><c:out value="${op:message('M00088')}"/></span></button>
					<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/locgov-notice/list'"><c:out value="${op:message('M00037')}"/></button>
				</div>
			</div>
			</c:if>

		</div>
		</form:form>

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
		$.post(url("/opmanager/locgov-notice/delete/" + noticeId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/locgov-notice/list";
// 				location.reload();
			});
		});
	});

}
</script>


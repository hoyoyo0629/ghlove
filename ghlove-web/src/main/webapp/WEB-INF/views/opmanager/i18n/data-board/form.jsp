<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

		<div class="location">
			<a href=""><c:out value="${op:message('MENU_5000')}"/></a> &gt; <a href=""></a> &gt; <a href="" class="on">글쓰기</a>
		</div>
		<h3><span>글쓰기</span></h3>

		<form:form modelAttribute="databoard" method="post" action='${fn:escapeXml(requestContext.requestUri)}' enctype="multipart/form-data">
			<form:hidden path="dataId"/>
			<input type="hidden" name="databoardParam" id="databoardParam" />

		<c:if test="${databoard.dataId != 0}">
		<div class="btn_all btn_right mb15">
		    <div class="flex_box gap-08">
		        <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
		        <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteDataboard(${fn:escapeXml(databoard.dataId)})">삭제</button>
		        <button type="button" class="btn btn-dark-default btn-mini" onClick="location.href='/opmanager/data-board/list'">목록</button>
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
					<td class="label">상단게시</td>
					<td>
						<div class="flex_box gap-08">
                            <div class="checkbox">
								<form:checkbox path="noticeFlag" label="${op:message('M00083')}" value="Y" checked="checked" />
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
					<td class="label"><c:out value="${op:message('M01699')}"/></td><!-- 첨부파일 -->
					<td>
						<div class="flex_box item-center">
							<c:if test="${databoard.dataId != 0}">
								<span class="input_file_txt">파일 수정 : </span>
							</c:if>
							<input type="file" name="detailImageFiles[]" multiple="multiple" /><!-- accept="image/png, image/jpeg, image/gif"  -->
<!--                             <input type="file" name="" class="full input_file" title="이미지" -->
<!--                                 multiple="multiple" accept="image/png, image/jpeg, image/gif" /> -->

<!-- 							<div id="multiple_files"></div> -->
                        </div>

						<c:forEach items="${databoard.databoardFiles}" var="itemImage" varStatus="i">
                        <div class="file_camera" id="file_camera_${fn:escapeXml(i.index)}">
                            <a href="javascript:downloadItemImage('${fn:escapeXml(itemImage.dataFileId)}');" ><c:out value="${itemImage.orgFileName }"/></a>
                            <a href="javascript:deleteItemImage('${fn:escapeXml(itemImage.dataFileId)}', ${fn:escapeXml(i.index)});"><img src="/content/images/btn/file_close.gif" alt="close"></a>
                        </div>
                        </c:forEach>

					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td>
					<td>
						<!-- smart_editor2 area -->
						<div class="smart_editor2-wrap">
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
								<form:radiobutton path="useYn" label="게시" value="Y" title="게시여부" cssClass="required" />
							</div>
							<div class="input-form">
								<form:radiobutton path="useYn" label="중지" value="N" title="게시여부" cssClass="required" />
							</div>
						</div>
					</td>
				</tr>
				</tbody>
			</table>

			<c:if test="${databoard.dataId == 0}">
			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
					<button type="submit" class="btn btn-dark-gray btn-small"><span><c:out value="${op:message('M00088')}"/></span></button>
					<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/data-board/list'"><c:out value="${op:message('M00037')}"/></button>
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

// 	$( window ).scroll(function() {
// 		setHeight();
// 	});

	try{
		$('#databoard').validator(function() {
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

			$("#databoardParam").val($("#searchParam").serialize());
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

	$.post('/opmanager/databoard/delete-notice-seller/'+noticeSellerId, null, function(response) {
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

function downloadItemImage(id) {
	location.href = '/opmanager/data-board/file-download/'+encodeURIComponent(id);
}

function deleteItemImage(id, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'itemId': encodeURIComponent(id)};
	$.post('/opmanager/data-board/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$("#file_camera_"+index).remove();
	});
}

function deleteDataboard(dataId) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/data-board/delete/" + dataId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/data-board/list";
// 				location.reload();
			});
		});
	});

}
</script>


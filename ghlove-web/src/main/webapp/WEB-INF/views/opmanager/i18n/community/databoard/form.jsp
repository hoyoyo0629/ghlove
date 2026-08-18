<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>

<div class="location">
		<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3>
	<span></span>
</h3>

<form:form modelAttribute="cmntyRpstrDto" method="post"
	action='${fn:escapeXml(requestContext.requestUri)}' enctype="multipart/form-data">
	<form:hidden path="rpstrId" />
	<input type="hidden" name="databoardParam" id="databoardParam" />

	<div class="board_write">
		<table class="board_write_table" summary="${op:message('M00269')}">
			<colgroup>
				<col style="width: 220px;">
			</colgroup>
			<tbody>
				<c:if test ="${ fn:escapeXml(role) eq 'ROLE_ADMIN_1'|| fn:escapeXml(role) eq 'ROLE_ADMIN_2' || fn:escapeXml(role) eq 'ROLE_ADMIN_3'|| fn:escapeXml(role) eq 'ROLE_ADMIN_4' }">
					<tr>
						<td class="label">상단공지 <c:out value="${noticeYn}"/></td>
						<td>
							<div class="flex_box gap-08">
								<div class="checkbox">
									<form:checkbox path="noticeYn" label="${op:message('M00083')}"
										value="N"/>
								</div>
							</div>
						</td>
					</tr>
				</c:if>
				<tr>
					<td class="label"><span class="required_mark">*</span>
					<c:out value="${op:message('M00275')}" /></td>
					<td>
						<div>
							<form:input path="rpstrTtl" title="${op:message('M00275')}"
								cssClass="input_txt required _filter half" maxlength="255" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><c:out value="${op:message('M01699')}" /></td>
					<!-- 첨부파일 -->
					<td>
						<div class="flex_box item-center">
							<c:if test="${cmntyRpstrDto.rpstrId == 0}">
								<span class="input_file_txt">파일 등록 : </span>
							</c:if>
							<c:if test="${cmntyRpstrDto.rpstrId != 0}">
								<span class="input_file_txt">파일 수정 : </span>
							</c:if>
							<input type="file" name="detailImageFiles[]" onchange="addFile(this);"/>
							<!-- accept=".pdf,.jpg,.jpeg,.gif,.png,.docx,.xlsx,.pptx,.hwp,.zip"  -->
							<!--                             <input type="file" name="" class="full input_file" title="이미지" -->
							<!--                                 multiple="multiple" accept="image/png, image/jpeg, image/gif" /> -->

							<!-- 							<div id="multiple_files"></div> -->

						</div>
						<div>
							<p style="color:red;">※ 첨부파일은 1개(50MB 이하) / jp(e)g, png, ppt(x), xlsx, hwp(x), doc(x), pdf,  bmp, gif, zip, 7z만 가능합니다.</p>
						</div>
						<c:if test ="${not empty fileList}">
							<c:forEach items="${fileList}" var="file"
								varStatus="i">
								<div class="file_camera" id="file_camera_${fn:escapeXml(file.fileId)}">
									<a href="javascript:downloadItemImage(${fn:escapeXml(file.fileId)});">
										<c:out value="${file.orgnlAtchFileNm }" />
									</a>
									<a href="javascript:deleteItemImage(${fn:escapeXml(file.fileId)}, ${fn:escapeXml(i.index)});">
										<img src="/content/images/btn/file_close.gif" alt="close">
									</a>
								</div>
							</c:forEach>
						</c:if>


					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>
					<c:out value="${op:message('M00006')}" /></td>
					<td>
						<!-- smart_editor2 area -->
						<div class="smart_editor2-wrap">
							<form:textarea path="rpstrCn" cols="30" rows="20" maxlength="300"
								class="w90 editor-content" title="${op:message('M00006')}"/>
						</div> <!-- smart_editor2 area -->
					</td>
				</tr>
			</tbody>
		</table>

		<c:if test="${cmntyRpstrDto.rpstrId == 0}">
			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
					<button type="submit" class="btn btn-dark-gray btn-small">
						<span><c:out value="${op:message('M00088')}" /></span>
					</button>
					<button type="button" class="btn btn-defualt btn-small"
						onclick="location.href='/opmanager/community/databoard/list'">
						<c:out value="${op:message('M00037')}" />
					</button>
				</div>
			</div>
		</c:if>
		<c:if test="${cmntyRpstrDto.rpstrId != 0}">
			<div class="btn_all btn_center">
					<div class="flex_box gap-08">
						<button type="submit" class="btn btn-dark-gray btn-small">
							<c:out value="${op:message('M00087')}" />
						</button>
						<button type="button" class="btn btn-defualt btn-small" onclick="goToDetail()">
							<c:out value="${op:message('M00037')}" />
						</button>
					</div>
				</div>
		</c:if>

	</div>

</form:form>

<!--// ${op:message('M00269')} 끝-->
<module:smarteditorInit />
<module:smarteditor id="rpstrCn" />


<script type="text/javascript">
$(function() {

// 	$( window ).scroll(function() {
// 		setHeight();
// 	});

	try{
		$('#cmntyRpstrDto').validator(function() {
			Common.getEditorContent("rpstrCn");

			if ($('#rpstrCn').val().toLowerCase() == '<p>&nbsp;</p>' || $('#rpstrCn').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

			if($("input[name='sellerSelectFlag']:checked").val() == 'Y'){

				if($('#seller_box > li').length == 0){
					alert('판매자를 선택해주세요.'); return false;
				}
			}

			$("#databoardParam").val($("#searchParam").serialize());
			$('#rpstrCn')[0].value = encodeURIComponent($('#rpstrCn')[0].value);
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

	//체크박스 변화에 따라 noticeYn 값 세팅
	$("#noticeYn1").click(function(){
		var checked = $("#noticeYn1").is(":checked");

		if (checked == true){
			$("#noticeYn1").val('Y');
		}
		if (checked == false){
			$("#noticeYn1").val('N');
		}
	});

	if('${cmntyRpstrDto.noticeYn}' == "" || '${cmntyRpstrDto.noticeYn}' == null || '${cmntyRpstrDto.noticeYn}' == "N" ){
		$("#noticeYn1").prop("checked",false);
	}else{
		$("#noticeYn1").prop("checked",true);
		$("#noticeYn1").val('Y');
	}

});


/*
 * 함 수 명 : addFile
 * 기 능 : 첨부파일 유효성 검사
 */

function addFile(obj){
	let size = 50 * 1024 * 1024; // 30MB? 50MB? 용량에 따라 수정할것
	let fileTypes = ['jpg', 'jpeg', 'gif', 'bmp', 'png', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'pdf', 'tif', 'tiff', 'hwp', 'hwpx','zip', '7z'] // 파일 확장자 , 이후에도 확장자 추가 가능

	if ($('.file_camera').length > 0) {
		alert("이미 등록된 파일이이 있습니다.");
		$("input:file[name='detailImageFiles[]']").val('');
		return false;
	}

	//파일개수 체크 ( 현재 단일 파일등록으로 체크안함)
	if(obj.files.length > 5){
		alert("첨부파일은 최대 5개 까지 첨부 가능합니다.");
	   $("input:file[name='detailImageFiles[]']").val('');
	   return false;
	}

	for (const file of obj.files) {
		let type = file.name.split('.').pop().toLowerCase(); // 파일 확장자 가져오기

		if(file.size > size) {
			alert("첨부파일 용량은 최대 50MB 까지 가능합니다.");
			$("input:file[name='detailImageFiles[]']").val('');
			return false;
		}else if($.inArray(type, fileTypes) == -1) {
			alert("등록할 수 없는 첨부파일 입니다.")
			$("input:file[name='detailImageFiles[]']").val('');
			return false;
		}
	}
}

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

	$.post('/opmanager/community/databoard/delete-notice-seller/'+noticeSellerId, null, function(response) {
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

function downloadItemImage(fileId) {
	/*
	$.get(url("/opmanager/community/databoard/file-download/"+userId), {}, function(response) {
		if(response.isSuccess && response.data){
			Common.loading.hide();
		}else{
			alert("첨부파일 다운로드 중 오류가 발생했습니다.")
		}
	}); */
	location.href = '/opmanager/community/databoard/file-download/'+fileId
}

function deleteItemImage(fileId, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'fileId': fileId};
	$.post('/opmanager/community/databoard/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$("#file_camera_"+fileId).remove();
// 		$(".file_camera").eq(index).remove();
		alert("파일이 삭제되었습니다.");
	});
}

function deleteDataboard(dataId) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/community/databoard/delete/" + dataId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/community/databoard/list";
			});
		});
	});

}

function goToDetail(){
	let message = '작성 중인 내용을 취소하시겠습니까';
	if (confirm(message)) {
		location.href = "/opmanager/community/databoard/list";
	}
}

</script>


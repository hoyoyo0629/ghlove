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

		<form:form modelAttribute="sysNoticeSellerDto" method="post" action='${fn:escapeXml(requestContext.requestUri)}' enctype="multipart/form-data">
			<form:hidden path="noticeId"/>

			<c:if test="${sysNoticeSellerDto.noticeId != 0}">
				<div class="btn_all btn_right mb15">
				    <div class="flex_box gap-08">
				        <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
				        <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteNotice(${fn:escapeXml(sysNoticeSellerDto.noticeId)})">삭제</button>
				        <button type="button" class="btn btn-dark-default btn-mini" onClick="location.href='/opmanager/sellerNotice/list'">목록</button>
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
							<td class="label"><c:out value="${op:message('M01699')}" /></td>
							<!-- 첨부파일 -->
							<td>
								<div class="flex_box item-center">
										<span class="input_file_txt">파일 등록 : </span>
									<input type="file" name="detailImageFiles" onchange="addFile(this);"/>
								</div>
								<div>
									<p style="color:red;">※ 첨부파일은 1개(50MB 이하) / jp(e)g, png, ppt(x), xlsx, hwp(x), doc(x), pdf,  bmp, gif, zip, 7z만 가능합니다.</p>
								</div>
								<div class="file-list-title">
									<c:if test="${!empty fileList}">
			             	         	<c:forEach items="${fileList}" var="file" varStatus="i">
										   <div class="file_camera" id="fileId_${fn:escapeXml(file.fileId)}">
									       	 <a href="javascript:downloadItemImage(${fn:escapeXml(file.fileId)});" ><c:out value="${file.orgnlAtchFileNm}"/></a>
									       	 <span style="padding:3px">(<c:out value="${filesize}"/>)</span>
									       	 <a href="javascript:deleteItemImage(${fn:escapeXml(file.fileId)});">
													<img src="/content/images/btn/file_close.gif" alt="close">
												</a>
									  	    </div>
										</c:forEach>
									</c:if>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>게시여부</td>
							<td>
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="displayFlag" label="게시" value="Y" title="게시여부" cssClass="required" checked="checked" />
									</div>
									<div class="input-form">
										<form:radiobutton path="displayFlag" label="중지" value="N" title="게시여부" cssClass="required" />
									</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>

				<!-- 버튼시작 -->
				<c:if test="${sysNoticeSellerDto.noticeId == 0}">
					<div class="btn_all btn_center">
						<div class="flex_box gap-08">
						<button type="submit" class="btn btn-dark-gray btn-small"><span>등록</span></button>
		 				<button type="button" class="btn btn-defualt btn-small" onclick="goToDetail()"><c:out value="${op:message('M00037')}"/></button>

		 				</div>
					</div>
				</c:if>
				<!-- 버튼 끝-->

			</div> <!-- // board_write -->

		</form:form>

		<%-- 검색 후 목록으로 이동하기 위한 조건 2017-03-09 yulsun.yoo
		<form:form modelAttribute="searchParam" action="/opmanager/sellerNotice/list" method="post">
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
		$('#sysNoticeSellerDto').validator(function() {
			Common.getEditorContent("content");

			if ($('#content').val().toLowerCase() == '<p>&nbsp;</p>' || $('#content').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

		//	$("#noticeParam").val($("#searchParam").serialize());
		});
	} catch(e) {
		alert(e.message);
	}

});

$(function(){
	$("#sysNoticeSellerDto").on("submit", function(e){
		if(!confirm("공지를 등록하시겠습니까?")){
			e.preventDefault();
		}
	})
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
		$("input:file[name='detailImageFiles']").val('');
		return false;
	}

	//파일개수 체크 ( 현재 단일 파일등록으로 체크안함)
	if(obj.files.length > 5){
		alert("첨부파일은 최대 5개 까지 첨부 가능합니다.");
	   $("input:file[name='detailImageFiles']").val('');
	   return false;
	}

	for (const file of obj.files) {
		let type = file.name.split('.').pop().toLowerCase(); // 파일 확장자 가져오기

		if(file.size > size) {
			alert("첨부파일 용량은 최대 50MB 까지 가능합니다.");
			$("input:file[name='detailImageFiles']").val('');
			return false;
		}else if($.inArray(type, fileTypes) == -1) {
			alert("등록할 수 없는 첨부파일 입니다.")
			$("input:file[name='detailImageFiles']").val('');
			return false;
		}
	}
}

function downloadItemImage(fileId) {
	location.href = '/opmanager/sellerNotice/file-download/'+fileId
}

function deleteItemImage(fileId, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'fileId': fileId};
	$.post('/opmanager/sellerNotice/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$("#fileId_"+fileId).remove();
// 		$(".file_camera").eq(index).remove();
		alert("파일이 삭제되었습니다.");
	});
}

function noticeList() {
	$('#searchParam').submit();
}

function deleteNotice(noticeId) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/sellerNotice/delete/" + noticeId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/sellerNotice/list";
// 				location.reload();
			});
		});
	});

}

function goToDetail(){
	let message = '공지 작성을 취소하시겠습니까?';
	if (confirm(message)) {
		location.href = "/opmanager/sellerNotice/list";
	}
}
</script>


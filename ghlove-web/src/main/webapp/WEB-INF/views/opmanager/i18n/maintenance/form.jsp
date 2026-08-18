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

<form:form modelAttribute="maintenanceDto" method="post" action='${fn:escapeXml(requestContext.requestUri)}' enctype="multipart/form-data">
	<div class="board_write">
		<table class="board_write_table" summary="${op:message('M00269')}">
			<colgroup>
				<col style="width: 220px;">
			</colgroup>
			<tbody>
				<c:if test ="${ fn:escapeXml(role) eq 'ROLE_ADMIN_1'|| fn:escapeXml(role) eq 'ROLE_ADMIN_2'|| fn:escapeXml(role) eq 'ROLE_ADMIN_3'|| fn:escapeXml(role) eq 'ROLE_ADMIN_4'}">
				<tr>
					<td class="label"><span class="required_mark">*</span>KI구분</td>
					<td colspan="3">
						<div class="flex_box gap-12">
							<div class="input-form">
								<input id="ki_1" name="kiType" type="radio" value="1" checked>
								<label for="ki_1">홈페이지</label>
							</div>
							<div class="input-form">
								<input id="ki_2" name="kiType" type="radio" value="2">
								<label for="ki_2">관리자시스템</label>
							</div>
							<div class="input-form">
								<input id="ki_3" name="kiType" type="radio" value="3">
								<label for="ki_3">기타</label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>요청경로</td>
					<td>
						<div class="flex_box gap-08 item-center">
							<select id="reqChannel" name="reqChannel" title="요청경로" class="wd-200 required">
								<option value="">-선택-</option>
								<c:forEach items="${maintenPathList}" var="code" varStatus="status">
									<option value="${fn:escapeXml(code.id)}"
										<c:if test="${maintenanceDto.reqChannel eq code.id}">selected="selected"</c:if>>
										<c:out value="${fn:escapeXml(code.label)}"/>
									</option>
								</c:forEach>
							</select>
						</div>
					</td>
					<td class="label"><span class="required_mark">*</span>업무구분</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<input id="req_1" name="reqType" type="radio" value="1" checked>
								<label for="req_1">기부</label>
							</div>
							<div class="input-form">
								<input id="req_2" name="reqType" type="radio" value="2">
								<label for="req_2">답례품</label>
							</div>
							<div class="input-form">
								<input id="req_3" name="reqType" type="radio" value="3">
								<label for="req_3">기타</label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>처리구분</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<input id="proc_1" name="processType" type="radio" value="1" checked>
								<label for="proc_1">기능신규</label>
							</div>
							<div class="input-form">
								<input id="proc_2" name="processType" type="radio" value="2">
								<label for="proc_2">기능개선</label>
							</div>
							<div class="input-form">
								<input id="proc_3" name="processType" type="radio" value="3">
								<label for="proc_3">오류수정</label>
							</div>
							<div class="input-form">
								<input id="proc_4" name="processType" type="radio" value="4">
								<label for="proc_4">자료추출</label>
							</div>
							<div class="input-form">
								<input id="proc_5" name="processType" type="radio" value="5">
								<label for="proc_5">데이터수정</label>
							</div>
							<div class="input-form">
								<input id="proc_6" name="processType" type="radio" value="6">
								<label for="proc_6">기타</label>
							</div>
						</div>
					</td>
					<td class="label"><span class="required_mark">*</span>완료구분</td>
					<td>
						<div class="flex_box gap-12">
							<div class="input-form">
								<input id="stat_1" name="processState" type="radio" value="1" checked>
								<label for="stat_1">접수</label>
							</div>
							<div class="input-form">
								<input id="stat_2" name="processState" type="radio" value="2">
								<label for="stat_2">처리중</label>
							</div>
							<div class="input-form">
								<input id="stat_3" name="processState" type="radio" value="3">
								<label for="stat_3">처리완료</label>
							</div>
							<div class="input-form">
								<input id="stat_4" name="processState" type="radio" value="9">
								<label for="stat_4">제외</label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark"></span>고객전화번호</td>
					<td>
						<div class="flex_box gap-08 item-center">

						<form:hidden path="reqUserPhoneNumber" />

							<select id="chargerCttpc1" title="-선택-" class="wd-150">
								<option value="">-선택-</option>
								<c:forEach items="${phoneCodeList}" var="code" varStatus="status">
									<option value="${fn:escapeXml(code.id)}"><c:out value="${code.label}"/></option>
								</c:forEach>
								<c:forEach items="${telCodeList}" var="code" varStatus="status">
									<option value="${fn:escapeXml(code.id)}"><c:out value="${code.label}"/></option>
								</c:forEach>
							</select>
							<span class="wave">-</span>
							<input id="chargerCttpc2" class="basic wd-150" title="" class="input_txt required _filter half _number" type="text" maxlength="4" value="">
							<span class="wave">-</span>
							<input id="chargerCttpc3" class="basic wd-150" title="" class="input_txt required _filter half _number" type="text" maxlength="4" value="">
						</div>
					</td>
					<td class="label"><span class="required_mark"></span>고객실명정보(아이디 이메일 등)</td>
					<td>
						<div>
							<form:input path="reqUserInfo" title="고객정보"
								cssClass="input_txt _filter full" maxlength="255" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>
					<c:out value="${op:message('M00275')}" /></td>
					<td colspan="3">
						<div>
							<form:input path="bbsTtl" title="${op:message('M00275')}"
								cssClass="input_txt required _filter half" maxlength="255" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>
					<c:out value="${op:message('M00006')}" /></td>
					<td colspan="3">
						<!-- smart_editor2 area -->
						<div class="smart_editor2-wrap">
						<!-- <div class="smart_editor2-wrap" style="position:relative; width:100;">
							<div id="editorPlaceholder" style="position:absolute; top:70px; left:30px; color:#aaa; pointer-events:none; z-index:10;">
								내용 작성시 개인정보는 실명정보란에 기입해주세요.
							</div> -->
							<form:textarea path="bbsCn" cols="30" rows="20" maxlength="300"
								class="w90 editor-content" title="${op:message('M00006')}"/>
						</div> <!-- smart_editor2 area -->
					</td>
				</tr>
				<tr>
					<td class="label"><c:out value="${op:message('M01699')}" /></td>
					<!-- 첨부파일 -->
					<td colspan="3">
						<div class="flex_box item-center">
								<span class="input_file_txt">파일 등록 : </span>
							<input type="file" name="detailImageFiles" onchange="addFile(this);"/>
						</div>
						<div>
							<p style="color:red;">※ 첨부파일은 1개(50MB 이하) / jp(e)g, png, ppt(x), xlsx, hwp(x), doc(x), pdf,  bmp, gif, zip, 7z만 가능합니다.</p>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark"></span>담당자</td>
					<td>
						<div>
							<form:input path="processManagerNm" title="담당자" disabled="${fn:escapeXml(role) ne 'ROLE_ADMIN_1' ? 'true' : 'false'}"
								cssClass="input_txt _filter half" maxlength="255" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark"></span>조치결과</td>
					<td colspan="3">
						<div>
							<span class="placeholder_wrap">
								<span class="placeholder"></span>
<%-- 								<textarea id="processCn" name="processCn" cols="30" rows="10" class="required _filter" title="조치결과" ${fn:escapeXml(role) ne 'ROLE_ADMIN_1' ? 'disabled="disabled"' : ''}>${op:removeIframe(maintenanceDto.processCn)}</textarea> --%>
								<form:textarea id="processCn" name="processCn" path="processCn" cols="30" rows="10" cssClass="input_txt _filter full" title="조치결과" disabled="${fn:escapeXml(role) ne 'ROLE_ADMIN_1' ? 'true' : 'false'}"/>
							</span>
						</div>
					</td>
				</tr>

				<tr>
					<td class="label"><span class="required_mark"></span>접수일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="processReceiptDate" pattern="[0-9]{8}" maxlength="8" class="datepicker" title="접수일" /><!-- 접수일 --></span>
						</div>
					</td>
					<td class="label"><span class="required_mark"></span>종료예정일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="processTargetEndDate" pattern="[0-9]{8}" maxlength="8" class="datepicker" title="종료예정일" /><!-- 종료예정일 --></span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark"></span>시작일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="processStartDate" pattern="[0-9]{8}" maxlength="8" class="datepicker" title="시작일" /><!-- 시작일 --></span>
						</div>
					</td>
					<td class="label"><span class="required_mark"></span>종료일</td>
					<td>
						<div>
							<span class="datepicker"><form:input path="processEndDate" pattern="[0-9]{8}" maxlength="8" class="datepicker" title="종료일" /><!-- 종료일 --></span>
						</div>
					</td>
				</tr>
				</c:if>
			</tbody>
		</table>

			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
					<button type="submit" class="btn btn-dark-gray btn-small">
						<span><c:out value="${op:message('M00088')}" /></span>
					</button>
					<button type="button" class="btn btn-defualt btn-small"
						onclick="goToDetail()">
						<c:out value="${op:message('M00037')}" />
					</button>
				</div>
			</div>
	</div>

</form:form>

<!--// ${op:message('M00269')} 끝-->
<module:smarteditorInit />
<module:smarteditor id="bbsCn" />


<script type="text/javascript">
$(function() {
	try{
		if(${fn:escapeXml(role) != 'ROLE_ADMIN_1'}){
			$('.datepicker').datepicker('disable');
		}
		$('#maintenanceDto').validator(function() {
			Common.getEditorContent("bbsCn");

			if ($('#bbsCn').val().toLowerCase() == '<p>&nbsp;</p>' || $('#bbsCn').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

			$("#databoardParam").val($("#searchParam").serialize());
			$('#bbsCn')[0].value = encodeURIComponent($('#bbsCn')[0].value);
		});
	} catch(e) {
		alert(e.message);
	}
});

$(function(){
	$("#maintenanceDto").on("submit", function(e){
		var chargerCttpc = $("#chargerCttpc1").val() +"-"+ $("#chargerCttpc2").val() +"-"+ $("#chargerCttpc3").val();
		$("#reqUserPhoneNumber").val(chargerCttpc);
		if(!confirm("게시글을 등록하시겠습니까?")){
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
	let message = '게시글 작성을 취소하시겠습니까?';
	if (confirm(message)) {
		location.href = "/opmanager/maintenance/list";
	}
}



</script>


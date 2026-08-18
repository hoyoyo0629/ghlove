<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>
<% pageContext.setAttribute("replaceChar", "\n"); %>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">기부금 지출내역</h1>
		<a href="javascript:closePopup();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<form id="opertaion" method="post">
		<div class="popup_contents">
			<div>
				<div class="board_write">
					<table class="board_write_table" summary="">
						<colgroup>
							<col style="width:220px;">
						</colgroup>
						<tbody>
							<tr>
								<td class="label"><span class="required_mark">*</span>지출일자</td>
								<td>
									<div>
										<c:choose>
											<c:when test="${adminRole == 'LOC'}">
												<span class="datepicker">
													<input name="expndtrDe" title="지출일자"
														class="datepicker optional required" type="text" value="${fn:escapeXml(detail.expndtrDe)}" maxlength="8"/>
												</span>
											</c:when>
											<c:otherwise>
												<c:out value="${detail.expndtrDe}" />
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="label"><span class="required_mark">*</span>사업명</td>
								<td>
									<div>
										<c:choose>
											<c:when test="${adminRole == 'LOC'}">
												<input name="bsnsNm" title="사업명" class="input_txt required _filter wd-500" type="text" value="${fn:escapeXml(detail.bsnsNm)}" />
											</c:when>
											<c:otherwise>
												<c:out value="${detail.bsnsNm}" />
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="label"><span class="required_mark">*</span>지출사업목적</td>
								<td>
									<div class="flex_box gap-08 item-center">
										<c:choose>
											<c:when test="${adminRole == 'LOC'}">
												<select name="bsnsPurpsCode" title="지출사업목적" class="wd-500 required">
													<c:forEach items="${cntrUse}" var="item" varStatus="i">
														<option value="${fn:escapeXml(item.id)}" <c:if test="${detail.bsnsPurpsCode == item.id}">selected</c:if>><c:out value="${item.label}" /></option>
													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>
												<c:forEach items="${cntrUse}" var="item" varStatus="i">
													<c:if test="${detail.bsnsPurpsCode == item.id}">
														<c:out value="${item.label}" />
													</c:if>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="label"><span class="required_mark">*</span>사용금액</td>
								<td>
									<div class="flex_box gap-08 item-center">
										<c:choose>
											<c:when test="${adminRole == 'LOC'}">
												<input name="expndtrAmt" title="사용금액" class="input_txt required _filter wd-500 _number" type="text" value="${fn:escapeXml(detail.expndtrAmt)}" />
												<span class="wave">원</span>
											</c:when>
											<c:otherwise>
												<c:out value="${op:numberFormat(detail.expndtrAmt)}원" />
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="label"><span class="required_mark">*</span>사용내용</td>
								<td>
									<div>
										<c:choose>
											<c:when test="${adminRole == 'LOC'}">
												<span class="placeholder_wrap">
													<span class="placeholder"></span>
													<textarea name="bsnsCn" cols="30" rows="10" maxlength="140" class="required _filter wd-500" title="사용내용" ><c:out value="${detail.bsnsCn}" /></textarea>
												</span>
											</c:when>
											<c:otherwise>
												<c:out value="${fn:replace(detail.bsnsCn, replaceChar, '<br />')}" escapeXml = "true" />
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="label">비고</td>
								<td>
									<div>
										<c:choose>
											<c:when test="${adminRole == 'LOC'}">
												<input name="rm" title="비고" class="input_txt _filter wd-500" type="text" value="${fn:escapeXml(detail.rm)}" />
											</c:when>
											<c:otherwise>
												<c:out value="${detail.rm}" />
											</c:otherwise>
										</c:choose>
									</div>
								</td>
							</tr>
							<tr>
								<td class="label">증빙서류</td>
								<td>
									<c:if test="${adminRole == 'LOC'}">
										<div class="flex_box item-center">
											<input type="file" id="operationFiles" name="operationFiles" class="full input_file" title="이미지" multiple="multiple" onchange="validationFile(this)"  />
										</div>
									</c:if>
									<c:if test="${detail.fileList != null && detail.fileList.size() > 0}">
										<div class="flex_box item-center">
											<c:forEach items="${detail.fileList}" var="file" varStatus="status">
												<div class="file_camera">
													<a href="javascript:downloadFile('${fn:escapeXml(file.registFileId)}');" ><c:out value="${fn:escapeXml(file.orginlFileNm)}" /></a>
													<c:if test="${adminRole == 'LOC'}">
														<a href="javascript:void(0);" class="file_delete" onclick="deleteFile(this,'${fn:escapeXml(file.registSn)}','${fn:escapeXml(file.registFileId)}')"><img src="/content/images/btn/file_close.gif" alt="close"></a>
													</c:if>
												</div>
											</c:forEach>
										</div>
									</c:if>

								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<p class="popup_btns">
					<div class="btn_all btn_center">
						<div class="btn_left gap-08">
							<c:if test="${adminRole == 'LOC'}">
								<button type="button" class="btn btn-active" onclick="update()"><c:out value="${detail == null ? '등록':'수정'}" /></button>
								<button type="button" class="btn btn-default" onclick="closePopup();">취소</button>
								<c:if test="${detail != null}">
									<button type="button" class="btn btn-active" onclick="deleteCtbnyOpratn()">삭제</button>
								</c:if>
							</c:if>
						</div>
					</div>
				</p>
			</div>
		</div>
	</form>
</div>


<script type="text/javascript">

$(function () {
	if ('${fn:escapeXml(adminRole)}' != 'LOC') {
		$("#opertaion input, select, textarea").attr('disabled',true);
		$(".ui-datepicker-trigger").attr('disabled',true);
	}
})

function update() {
	if (!validation() || !confirm("정보를 저장 하시겠습니까?")) return false;
	var registSn = '${fn:escapeXml(detail.registSn)}';
	var url = '${fn:escapeXml(detail.registSn)}' == '' ? '/opmanager/give/give-operation/list/${fn:escapeXml(locgovCode)}/popup': '/opmanager/give/give-operation/list/${fn:escapeXml(locgovCode)}/popup/${fn:escapeXml(detail.registSn)}';

	$.ajax({
		url : url,
		type : 'post',
		data: setParams(),
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		cache: false,
		success : function(response) {
			if(response.isSuccess && response.data == 'SUCC') {
				alert("등록되었습니다.");
				closePopup();
			} else {
				alert(response.data ? response.data : response.errorMessage);
				return false;
			}
		}
	});
}

function downloadFile(id) {
	location.href = '/opmanager/give/give-operation/file/download/'+id;
}

function deleteFile(that, registSn, registFileId) {

	if(confirm('파일이 실제로 삭제됩니다.\n정말 삭제하시겠습니까?')) {
		$.post('/opmanager/give/give-operation/${fn:escapeXml(locgovCode)}/file/delete',{registSn : registSn, registFileId : registFileId}, function (response) {
			if (response.isSuccess) {
				alert("삭제되었습니다.");
				if ($(".file_camera").length <= 1) {
					$(that).parent('div').parent('div').remove();
				} else {
					$(that).parent('div').remove();
				}
			} else {
				alert(response.errorMessage);
			}

		});
	}
}



function deleteCtbnyOpratn() {
	if (!confirm("삭제 하시겠습니까?")) return false;

	$.post('/opmanager/give/give-operation/list/${fn:escapeXml(locgovCode)}/popup/${fn:escapeXml(detail.registSn)}/delete',{}, function (response) {
		if(response.isSuccess && response.data == 'SUCC') {
			alert("삭제 되었습니다.");
			closePopup();
		} else {
			alert(response.data ? response.data : response.errorMessage);
			return false;
		}
	})
}


function validationFile(e) {
	var imageReg = /(.*?)\.(jpg|jpeg|png|gif|bmp|hwp|doc|docx|pdf|zip|ppt|pptx)$/;
	var maxSize = 20* 1024 * 1024;	//20MB
	var isSucc = true;

	if (e.files) {

		for (var i = 0; i < e.files.length; i++) {
			if (isSucc && !e.files[i].name.match(imageReg)) {
				alert("jpg|jpeg|png|gif|bmp|hwp|doc|docx|pdf|zip|ppt|pptx 파일만 등록 가능합니다.");
				$(e).focus();
				isSucc = false;
				break;
			}

			if (isSucc && e.files[i].size > maxSize) {
				alert("이미지 파일은 개당 20MB 이하로 등록 가능합니다.");
				$(e).focus();
				isSucc = false;
				break;
			}
		}

		if (!isSucc) {
			if (Browser.getName() == 'ie') {
				$(e).replaceWith($(e).clone(true));
			} else {
				$(e).val("");
			}
		}



	}
}

function validation() {
	var regNumber = /[0-9]/;
	var isSucc = true;

	$("#opertaion .required").each(function (index, that) {
		if ($(that).val() == '') {
			alert($(that).attr('title') + '를 입력해주세요.');
			$(that).focus();
			isSucc = false;
			return false;
		}
	});

	if (!isSucc) return false;

	if ($('input[name=expndtrAmt]').val() < 0) {
		alert("사용 금액은 0원 이하로 입력할 수 없습니다.");
		$('input[name=expndtrAmt]').focus();
		return false;
	}

	if ($('input[name=expndtrAmt]').val() >= 2100000000) {
		alert("사용 금액은 21억 이하로 입력해주세요.");
		$('input[name=expndtrAmt]').focus();
		return false;
	}

	if(!Common.validateDate($("input[name=expndtrDe]").val())) {
		alert("날짜 형식이 아닙니다.");
		$("input[name=expndtrDe]").focus();
		return false;
	}

	if (!regNumber.test($("input[name=expndtrAmt]").val())) {
		alert("숫자만 입력해주세요.");
		$("input[name=expndtrAmt]").focus();
		return false;
	}

	return true;
}

function setParams() {
	/*var formData = new FormData();
	formData.append("expndtrDe", $("input[name=expndtrDe]").val());										// 지출일자
	formData.append("bsnsNm", $("input[name=bsnsNm]").val());											// 사업명
	formData.append("bsnsPurpsCode", $("select[name=bsnsPurpsCode]").val());							// 사업목적 코드
	formData.append("expndtrAmt", $("input[name=expndtrAmt]").val());									// 사용금액
	formData.append("bsnsCn", $("textarea[name=bsnsCn]").val());										// 내용

	if ($("input[name=operationFiles]")[0].files.length > 0) {
		for (var i in $("input[name=operationFiles]")[0].files) {
			formData.append("operationFiles", $("input[name=operationFiles]")[0].files[i]);				// 제출파일
		}
	}*/

	var formData = new FormData($("#opertaion")[0]);

	return formData;
}

function closePopup() {
	self.close();
	opener.location.reload();
}


</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>



			<div class="location">
			    <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
			</div>
			<h3><span><c:out value="${op:message('지자체 문의 게시판')}"/></span></h3><!-- 지자체 문의 게시판 -->
			<c:if test="${requestContext.user.userId == qnaAdmin.userId && qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId <= '0'}">	<!-- 작성자, 답변 없을 경우만 확인 가능 -->
				<form:form modelAttribute="qnaAdmin" method="post" enctype="multipart/form-data">
					<div class="admin_wrap btn_all btn_right mb15">
                        <div class="flex_box gap-08">
                        	<c:if test="${qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId <= '0'}">
                            	<button id="submitBtn" type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkValidate(event);"><c:out value="${op:message('M00087')}"/></button><!-- 수정 -->
                            </c:if>
                            <button type="button" class="btn btn-default btn-mini" onclick="javascript:document.location.href='/seller/qna-locgov/list';"><c:out value="${op:message('M00037')}"/></button><!-- 취소 -->
                            <c:if test="${qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId <= '0'}">
                            	<button type="button" class="btn btn-default btn-mini" onclick="javascript:deleteQnaAdmin('${fn:escapeXml(qnaAdmin.qnaAdminId)}');"><c:out value="${op:message('M00074')}"/></button><!-- 삭제 -->
                            </c:if>
                        </div>
                    </div>
					<form:hidden path="qnaAdminId" />
					<div class="board_write">
					    <table class="board_write_table" summary="">
					        <colgroup>
					            <col style="width:220px;">
					            <col>
					        </colgroup>
					        <tbody>
					            <tr>
					                <td class="label"><c:out value="${op:message('M00460')}"/></td><!-- 문의유형 -->
					                <td>
					                    <div>
					                        <form:select path="qnaGroup" title="${op:message('M00460')}" class="wd-150"><!-- 문의유형 -->
					                            <c:forEach items="${qnaGroups}" var="group">
				 									<form:option value="${fn:escapeXml(group.id)}" label="${fn:escapeXml(group.label)}" />
				 								</c:forEach>
					                        </form:select>
					                    </div>
					                </td>
					            </tr>
					            <tr>
					                <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00275')}" escapeXml="true"/></td><!-- 제목 -->
					                <td>
					                    <div>
					                        <form:input path="subject" title="${op:message('M00275')}" class="input_txt required _filter full" type="text" value="" /><!-- 제목 -->
					                    </div>
					                </td>
					            </tr>
					            <tr>
					                <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}" /></td><!-- 내용 -->
					                <td>
				                        <div class="smart_editor2-wrap">
				                            <form:textarea path="question" cols="30" rows="20" class="editor-content" title="message('M00006')" /><!-- 내용 -->
					                    </div>
					                </td>
					            </tr>
					            <tr>
					                <td class="label"><c:out value="${op:message('M01699')}"/></td><!-- 첨부파일 -->
					                <td>
					                    <div class="flex_box item-center">
					                    	<input id="addFile" type="file" name="addFile" class="full input_file" title="${op:message('M01699')}" multiple="multiple">
					                    </div>
					                    <div id="displayAddFile">
						                    <c:choose>
					                        	<c:when test="${empty qnaAdminFiles}">
													<div class="file_camera">
														<c:out value="${op:message('첨부파일이 없습니다.')}"/> <!-- 첨부파일이 없습니다. -->
													</div>
												</c:when>
												<c:otherwise>
													<c:forEach items="${qnaAdminFiles}" var="file" varStatus="i">
														<div class="file_camera">
															<a href="/seller/qna-locgov/downloadQnaAdminFile/${fn:escapeXml(qnaAdmin.qnaAdminId)}/${fn:escapeXml(file.qnaAdminFileId)}"><c:out value="${fn:escapeXml(file.orgFileName)}"/></a>
															<img src="/content/images/btn/file_close.gif" alt="close" style="margin-left:5px;padding:3px 0px;cursor:pointer;"
																onclick="javascript:removeQnaAdminFile('${fn:escapeXml(qnaAdmin.qnaAdminId)}', '${fn:escapeXml(file.qnaAdminFileId)}');">
														</div>
													</c:forEach>
												</c:otherwise>
				                        	</c:choose>
					                    </div>
					                </td>
					            </tr>
					        </tbody>
					    </table>
					</div>
				</form:form>
			</c:if>
<module:smarteditorInit />
<module:smarteditor id="question" />


<script type="text/javascript">

$(function(){
	$('#qnaAdmin').validator(function() {
		Common.getEditorContent("question");
		$("textarea").html($("#question").val());
		if (!$("#question").val()) {
			alert("내용 항목을 입력해주세요.");
			return false;
		}

	});
});


/* function changeAddFile(inputElement) {
	let files = inputElement.files;
	let displayAddFile = document.getElementById('displayAddFile');		// 첨부파일 목록 영역
	displayAddFile.replaceChildren();		// 하위 태그 초기화
	displayAddFile.removeAttribute("style");
	if (files && files.length > 0) {
		let fileSize = 0;
		let fileCnt = files.length;
		for (let idx = 0 ; idx < fileCnt ; idx++) {
			let file = files[idx];
			let imgElement = document.createElement('img');		// img 요소 추가
			let divElement = document.createElement('div');		// div 요소 추가
			displayAddFile.appendChild(divElement);					// displayAddFile 요소 하위에 div 요소 할당
			divElement.innerHTML = file.name;
			divElement.appendChild(imgElement);
			imgElement.setAttribute('id', 'file' + idx);	// img 요소에 아이디 부여
			imgElement.setAttribute('alt', "close");
			imgElement.setAttribute('onclick', 'javascript:removeFileList("' + idx + '")');
			imgElement.setAttribute('src', "../../content/images/btn/file_close.gif");
			imgElement.setAttribute('style', "margin-left:5px;padding-bottom:3px;cursor:pointer;");
		}
		$("#addFileNone").hide();
	} else {
		displayAddFile.setAttribute("style", "display:none;");
		$("#addFileNone").show();
	}
}

// input file 숨겨서 별도 버튼으로 동작하도록 함
function addFileClick() {
	document.getElementById("addFile").click();
}

//첨부파일 삭제
function removeFileList(i) {
	let filesElement = document.getElementById('addFile');
	let files = filesElement.files;
	if (files && files.length > 0) {
		let fileCnt = files.length;
		const dataTransfer = new DataTransfer();		// 폼 객체 내 파일 정보 수정시 처리용 객체
		for (let idx = 0 ; idx < fileCnt ; idx++) {
			if (i != idx) {
				dataTransfer.items.add(files[idx]);
			}
		}
		filesElement.files = dataTransfer.files;
		//files = dataTransfer.files;
		changeAddFile(filesElement);		// 목록 다시그리기
	} else {
		let displayAddFile = document.getElementById('displayAddFile');		// 첨부파일 목록 영역
		displayAddFile.replaceChildren();		// 하위 태그 초기화
		displayAddFile.setAttribute("style", "display:none;");
	}
} */

// 첨부파일 개별 삭제
function removeQnaAdminFile(qnaAdminId, qnaAdminFileId) {
	if (confirm(Message.get("M00196"))) {			// 삭제하시겠습니까?
		Common.loading.show();
		$.post(
	    		url("/seller/qna-locgov/deleteQnaAdminFile")
	    		, {'qnaAdminId' : qnaAdminId
	    			, 'qnaAdminFileId' : qnaAdminFileId}
	    		, function(resp) {
	    			Common.responseHandler(resp, function(response){
	        			$("#displayAddFile").empty();

	        			let data = response.data;
	    	            for (let i = 0; i < data.length; i++) {
	    	                let div = "";
	    	                div += '<div class="file_camera">';
	    	                div += '	<a href="/seller/qna-locgov/downloadQnaAdminFile/' + qnaAdminId + '/' + data[i].qnaAdminFileId + '">' + data[i].orgFileName + '</a>';
	    	                div += '	<img src="/content/images/btn/file_close.gif" alt="close" style="margin-left:5px;padding:3px 0px;cursor:pointer;"'
	    							+ 'onclick="javascript:removeQnaAdminFile(\'' + qnaAdminId + '\', \'' + data[i].qnaAdminFileId + '\');">';
	    	                div += '</div>';

	    	                $('#displayAddFile').append(div);
	    	            }
	    			});
	            }
	    )
	    .always(function () {
	    	Common.loading.hide();
	    });
	}
}

// 문의 삭제
function deleteQnaAdmin(qnaAdminId) {
	<c:choose>
		<c:when test="${qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId > 0}">
			alert("답변이 완료된 게시글은 삭제가 불가능합니다. 관리자에게 문의하세요.");
		</c:when>
		<c:otherwise>
			if (confirm(Message.get("M00196"))) {			// 삭제하시겠습니까?
				Common.loading.show();
				document.location.href = "/seller/qna-locgov/deleteQnaAdmin/" + qnaAdminId;
			}
		</c:otherwise>
	</c:choose>
}

function checkValidate(event) {
	if (!confirm(Message.get("정보를 수정 하시겠습니까?"))) {			// 정보를 수정 하시겠습니까?
		event.preventDefault();
		return;
	}
	Common.loading.show();
}




</script>
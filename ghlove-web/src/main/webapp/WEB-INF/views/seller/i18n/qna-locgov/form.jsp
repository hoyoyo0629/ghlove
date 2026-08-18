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
			
			<form:form modelAttribute="qnaAdmin" method="post" enctype="multipart/form-data">
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
			 									<form:option value="${group.id}" label="${group.label}" />
			 								</c:forEach>
				                        </form:select>
				                    </div>
				                </td>
				            </tr>
				            <tr>
				                <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00275')}"/></td><!-- 제목 -->
				                <td>
				                    <div>
				                        <form:input path="subject" title="${op:message('M00275')}" class="input_txt required _filter full" type="text" value="" /><!-- 제목 -->
				                    </div>
				                </td>
				            </tr>
				            <tr>
				                <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td><!-- 내용 -->
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
				                </td>
				            </tr>
				        </tbody>
				    </table>
				</div>
				
				<div class="admin_wrap btn_all btn_center">
				    <div class="flex_box gap-08">
				        <button type="submit" class="btn btn-dark-gray btn-small" onclick="javascript:checkValidate(event);"><c:out value="${op:message('M00088')}"/></button><!-- 등록 -->
				        <button type="button" class="btn btn-default btn-small" onclick="javascript:document.location.href='/seller/qna-locgov/list';"><c:out value="${op:message('M00037')}"/></button><!-- 취소 -->
				    </div>
				</div>
			</form:form>

<module:smarteditorInit />
<module:smarteditor id="question" />


<script type="text/javascript">

$(function(){
	$("#displayAddFile").hide();
	
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

function checkValidate(event) {
	if (!confirm(Message.get("정보를 등록 하시겠습니까?"))) {			// 정보를 등록 하시겠습니까?
		event.preventDefault();
		return;
	}
	Common.loading.show();
}



</script>
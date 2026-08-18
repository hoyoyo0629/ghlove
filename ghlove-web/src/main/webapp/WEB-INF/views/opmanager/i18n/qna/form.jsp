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
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>
	<h3><span>1:1문의</span></h3>

	<c:if test="${qna.answerCount == 0}">
	<div class="btn_all btn_right mb15">
        <div class="flex_box gap-08">
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteQna('${fn:escapeXml(qna.qnaId)}')">문의글 삭제</button>
            <button type="button" class="btn btn-defualt btn-mini" onclick="goToList()">목록</button>
        </div>
    </div>
    </c:if>

    <div class="board_write">
           <table class="board_write_table" summary="">
               <colgroup>
                   <col style="width:220px;">
                   <col>
                   <col style="width:220px;">
               </colgroup>
               <tbody>
                   <tr>
                       <td class="label">문의유형</td>
                       <td colspan="3">
                           <div>
							<c:out value="${qnaAnswerTypeLabel}"/>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td class="label">작성자</td>
                       <td>
                           <div>
							<c:choose>
								<c:when test="${qna.userId > 0 && !empty qna.loginId}">
									<c:out value="${qna.userName}"/> <a href="/opmanager/user/customer/details/${qna.userId}" target="_blank" rel="noopener noreferrer">(<c:out value="${qna.loginId}" />)</a>
									<!-- a href="javascript:Common.popup('/opmanager/user/popup/details/${fn:escapeXml(qna.userId)}', '/user/popup/details', 1100, 800, 1, 0, 0)">${fn:escapeXml(qna.userName)} (${fn:escapeXml(qna.loginId)})</a-->
								</c:when>
								<c:when test="${!empty qna.userName}">
									<c:out value="${qna.userName}"/>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</div>
                       </td>
                       <td class="label">작성일</td>
                       <td>
                           <div>
                               <c:out value="${op:date(qna.createdDate)}"/>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td class="label">제목</td>
                       <td colspan="3">
                           <div>
                               ${op:nl2br(qna.subject)}
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td class="label">내용</td>
                       <td colspan="3">
                           <div>
                               ${op:nl2br(qna.question)}
                           </div>
                       </td>
                   </tr>
                   <c:if test="${fn:length(qnaImage) > 0}">
	                   <tr>
	                       <td class="label">첨부파일</td>
	                       <td colspan="3">
	                           <c:forEach items="${qnaImage}" var="itemImage" varStatus="i">
	                        	<div class="file_camera_${i.index}">
		                            <a href="javascript:downloadItemImage('${fn:escapeXml(itemImage.qnaFileId)}', 'Q');" ><c:out value="${fn:escapeXml(itemImage.orgFileName)}"/></a>
		                            <a href="javascript:deleteItemImage('${fn:escapeXml(itemImage.qnaFileId)}', ${fn:escapeXml(i.index)});"><img src="/content/images/btn/file_close.gif" alt="close"></a>
		                        </div>
		                        </c:forEach>
	                       </td>
	                   </tr>
	               </c:if>
               </tbody>
           </table>
       </div>

       <form id="qnaParam" method="post" action="/opmanager/qna/list">
		<input type="hidden" name="answerCount" value="${fn:escapeXml(qnaParam.answerCount)}" />
		<input type="hidden" name="qnaGroup" value="${fn:escapeXml(qnaParam.qnaGroup)}" />
		<input type="hidden" name="qnaId" value="${fn:escapeXml(qnaParam.qnaId)}"/>
		<input type="hidden" name="searchStartDate" value="${fn:escapeXml(qnaParam.searchStartDate)}"/>
		<input type="hidden" name="searchEndDate" value="${fn:escapeXml(qnaParam.searchEndDate)}"/>
		<input type="hidden" name="where" value="${fn:escapeXml(qnaParam.where)}"/>
		<input type="hidden" name="query" value="${fn:escapeXml(qnaParam.query)}"/>
		<input type="hidden" name="itemsPerPage" value="${fn:escapeXml(qnaParam.itemsPerPage)}"/>
	</form>
	<form:form modelAttribute="qnaAnswer" action="/opmanager/qna/answer/${fn:escapeXml(qna.qnaId)}" method="post" enctype="multipart/form-data">
		<input type="hidden" name="qnaAnswerId" value="${fn:escapeXml(qnaAnswer.qnaAnswerId)}" />
		<input type="hidden" name="email" value="${fn:escapeXml(qna.email)}" />
		<input type="hidden" name="subject" value="${fn:escapeXml(qna.subject)}" />
		<input type="hidden" name="userName" value="${fn:escapeXml(qna.userName)}" />
		<input type="hidden" name="userId" value="${fn:escapeXml(userId)}" />


	<div class="board_write mt30">
	    <table class="board_write_table" summary="">
	        <colgroup>
	            <col style="width:220px;">
                <col>
                <col style="width:220px;">
	        </colgroup>
	        <tbody>
	        	<c:if test="${qna.answerCount > 0}">
	        	<tr>
                    <td class="label">답변자</td>
                    <td>
                        <div>
							<c:out value="${qnaAnswer.roleNm}"/> (<c:out value="${qnaAnswer.answerLoginId}"/>)
                        </div>
                    </td>
                    <td class="label">답변일</td>
                    <td>
                        <div>
                            <c:out value="${op:date(qnaAnswer.answerDate)}"/>
                        </div>
                    </td>
                </tr>
                </c:if>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>답변제목</td>
	                <td colspan="3">
	                    <div class="flex_box gap-08 item-center">
	                        <form:input path="title" title="답변제목" class="input_txt required _filter full" type="text" value="문의에 대한 답변입니다." maxlength="100" />
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>답변내용</td>
	                <td colspan="3">
	                    <div class="flex_box gap-08 item-center">
	                        <span class="placeholder_wrap">
	                            <span class="placeholder"></span>
	                            <form:textarea path="answer" cols="30" rows="10" style="height:200px" maxlength="500" class="required _filter" title="답변내용" ></form:textarea>
	                        </span>
	                    </div>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	</div>

	<c:if test="${qna.answerCount == 0}">
	<div class="btn_all btn_right">
	    <div class="flex_box gap-08">
	        <button type="submit" class="btn btn-dark-gray btn-small">답변 등록</button>
	    </div>
	</div>
	</c:if>

	<c:if test="${qna.answerCount > 0}">
	<div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="submit" class="btn btn-dark-gray btn-mini">답변 수정</button>
            <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteQnaAnswer('${fn:escapeXml(qna.qnaId)}', '${fn:escapeXml(qnaAnswer.qnaAnswerId)}')" >답변 삭제</button>
            <button type="button" class="btn btn-default btn-mini" onclick="goToList()">목록</button>
        </div>
    </div>
    </c:if>

	</form:form>

<page:javascript>
<!-- 2015.1.15 QNA 검증 -->
<script type="text/javascript">
$(function() {
	$('#qnaAnswer').validator();
	Common.checkedMaxStringLength('textarea[name=answer]',null, 500);

	// 답변이 없을 때
	if("${fn:escapeXml(qna.answerCount)}" == 0){
		$('#answer').val("안녕하세요. 고향사랑e음 상담센터입니다.\n\n감사합니다.");
	}
});
function qnaList() {
	$("#qnaParam").attr("method", "get").attr("action", "/opmanager/qna/list").submit();
}

function deleteQna(id) {
	Common.confirm("삭제하시겠습니까?", function() {
		$.get(url("/opmanager/qna/delete/" + id), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/qna/list";
// 				location.reload();
			});
		});
	});

}

function deleteQnaAnswer(qnaId, answerId) {
	Common.confirm("삭제하시겠습니까?", function() {
		$.get(url("/opmanager/qna-open/delete/"+ qnaId +"/answer/" + answerId), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/qna/list";
// 				location.reload();
			});
		});
	});

}

function downloadItemImage(id, qnaDetailType) {
	console.log(id)
	location.href = '/opmanager/qna/file-download/'+encodeURIComponent(id)+'/'+qnaDetailType;
}

function deleteItemImage(id, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'itemId': encodeURIComponent(id)};
	$.post('/opmanager/qna/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$(".file_camera_"+index).remove();
	});
}

function goToList(){
	location.href = "/opmanager/qna/list";
}

</script>
</page:javascript>
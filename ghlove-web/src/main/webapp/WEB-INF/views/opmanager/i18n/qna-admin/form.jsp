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
        <h3><span><c:out value="${op:message('관리자 문의')}"/></span></h3><!-- 관리자 문의 -->

		<form:form modelAttribute="qnaAdminAnswer" method="post" enctype="multipart/form-data">
			<form:input path="qnaAdminAnswerId" type="hidden" />
			<c:if test="${qnaAdminAnswer.qnaAdminAnswerId <= 0}">
		        <div class="btn_all btn_right mb15">
		            <div class="flex_box gap-08">
		            	<c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
							<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkInputData(event);">답변</button>
							<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteLocgovQna('${fn:escapeXml(qnaAdmin.qnaAdminId)}');">삭제</button>
						</c:if>
						<button type="button" class="btn btn-default btn-mini" onclick="javascript:document.location.href='/opmanager/qna-admin/list';"><c:out value="${op:message('M00480')}"/></button><!-- 목록 -->
		            </div>
		        </div>
	        </c:if>
	        <div class="board_write">
	        	<form:form modelAttribute="qnaAdmin">
		            <table class="board_write_table" summary="${op:message('M01424')}"><!-- 문의 -->
		                <colgroup>
		                    <col style="width:220px;">
		                    <col>
		                    <col style="width:220px;">
		                    <col>
		                </colgroup>
		                <tbody>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('M01476')}"/></td><!-- 상태 -->
		                        <td colspan="3">
		                            <div>
		                                <c:out value="${qnaAdminAnswer.qnaAdminAnswerId > 0 ? op:message('M00463') : op:message('M00464')}"/><!-- 답변완료 --><!-- 답변대기 -->
		                            </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('M00460')}"/></td><!-- 문의유형 -->
		                        <td>
		                            <div>
		                                <c:out value="${qnaAdmin.qnaGroupName}"/>
		                            </div>
		                        </td>
		                        <td class="label"><c:out value="${op:message('M00276')}"/></td><!-- 작성일 -->
		                        <td>
		                            <div>
		                                <c:out value="${qnaAdmin.createdDateStr}"/>
		                            </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00275')}"/></td><!-- 제목 -->
		                        <td colspan="3">
		                            <div>
		                                <c:out value="${qnaAdmin.subject}" escapeXml="true"/>
		                            </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td><!-- 내용 -->
		                        <td colspan="3">
		                            <div>
		                                 ${op:nl2br(qnaAdmin.question)}
		                            </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('M01699')}"/></td><!-- 첨부파일 -->
		                        <td colspan="3">
		                        	<div>
			                        	<c:choose>
				                        	<c:when test="${empty qnaAdminFiles}">
												<div class="file_camera">
													<c:out value="${op:message('첨부파일이 없습니다.')}"/> <!-- 첨부파일이 없습니다. -->
												</div>
											</c:when>
											<c:otherwise>
												<c:forEach items="${qnaAdminFiles}" var="file" varStatus="i">
													<div class="file_camera">
						                                <a href="/opmanager/qna-admin/downloadQnaAdminFile/${fn:escapeXml(qnaAdmin.qnaAdminId)}/${fn:escapeXml(file.qnaAdminFileId)}"><c:out value="${fn:escapeXml(file.orgFileName)}"/></a>
						                            </div>
												</c:forEach>
											</c:otherwise>
			                        	</c:choose>
		                        	</div>
		                        </td>
		                    </tr>
		                </tbody>
		            </table>
	            </form:form>


	          		<div class="board_write mt30">
		            <table class="board_write_table" summary="">
		                <colgroup>
		                    <col style="width:220px;">
		                    <col>
		                    <col style="width:220px;">
		                    <col>
		                </colgroup>
		                <tbody>
		                	<c:if test="${qnaAdminAnswer.qnaAdminAnswerId > 0}">
			                    <tr>
			                    	<td class="label"><c:out value="${op:message('M00472')}"/></td><!-- 작성자 -->
	                                   <td>
	                                       <div>
	                                           <c:out value="${qnaAdminAnswer.userNm}"/>
	                                       </div>
	                                   </td>
			                        <td class="label"><c:out value="${op:message('M00476')}"/></td><!-- 답변일 -->
			                        <td>
			                            <div>
			                                <c:out value="${qnaAdminAnswer.answerDateStr}"/>
			                            </div>
			                        </td>
			                    </tr>
		                    </c:if>
		                    <tr>
		                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('답변 제목')}"/></td><!-- 답변 제목 -->
		                        <td colspan="3">
		                            <div class="flex_box gap-08 item-center">
		                                <form:input path="title" title="${op:message('답변 제목')}" class="input_txt required _filter full" type="text" />
		                            </div>
		                        </td>
	                        </tr>
		                    <tr>
		                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00477')}"/></td><!-- 답변내용 -->
		                        <td colspan="3">
		                        	<div class="flex_box gap-08 item-center">
	                                       <span class="placeholder_wrap">
	                                           <span class="placeholder"></span>
	                                           <form:textarea path="answer" cols="30" rows="10" maxlength="140" class="required required _filter" title="${op:message('M00477')}" />
	                                       </span>
	                                   </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('M01699')}"/></td><!-- 첨부파일 -->
		                        <td colspan="3" id="addFileTd">
		                            <c:choose>
			                        	<c:when test="${empty qnaAdminAnswerFiles}">
											<div class="file_camera">
												<input type="file" id="addFile" name="addFile" class="full input_file" title="${op:message('M01699')}"
	                                           	    multiple="multiple"/>
											</div>
											<c:if test="${qnaAdminAnswer.qnaAdminAnswerId > 0}">
												<div class="file_camera">
													<c:out value="${op:message('첨부파일이 없습니다.')}"/> <!-- 첨부파일이 없습니다. -->
												</div>
											</c:if>
										</c:when>
										<c:otherwise>
											<div id="displayAddFile">
												<c:forEach items="${qnaAdminAnswerFiles}" var="file" varStatus="i">
													<div class="file_camera">
					                                	<a href="/opmanager/qna-admin/downloadQnaAdminAnswerFile/${fn:escapeXml(qnaAdminAnswer.qnaAdminAnswerId)}/${fn:escapeXml(file.qnaAdminAnswerFileId)}"><c:out value="${fn:escapeXml(file.orgFileName)}"/></a>
					                                	<c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
						                                	<img src="/content/images/btn/file_close.gif" alt="close" style="margin-left:5px;padding:3px 0px;cursor:pointer;"
																	onclick="javascript:removeQnaAdminAnswerFile('${fn:escapeXml(qnaAdminAnswer.qnaAdminAnswerId)}', '${fn:escapeXml(file.qnaAdminAnswerFileId)}');">
														</c:if>
					                                </div>
												</c:forEach>
				                            </div>
										</c:otherwise>
		                        	</c:choose>
		                        </td>
		                    </tr>
		                </tbody>
		            </table>
	            </div>

	            <c:if test="${qnaAdminAnswer.qnaAdminAnswerId > 0}">
					<div class="btn_all btn_right">
						<div class="flex_box gap-08">
							<c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
								<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkInputData(event);"><c:out value="${op:message('답변 수정')}"/></button><!-- 답변 수정 -->
								<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteAnswer('${fn:escapeXml(qnaAdminAnswer.qnaAdminAnswerId)}');"><c:out value="${op:message('답변 삭제')}"/></button><!-- 답변 삭제 -->
							</c:if>
							<button type="button" class="btn btn-default btn-mini" onclick="javascript:document.location.href='/opmanager/qna-admin/list';"><c:out value="${op:message('M00480')}"/></button><!-- 목록 -->
						</div>
					</div>
	            </c:if>
	        </div>
		</form:form>

<page:javascript>
<script type="text/javascript">
	$(function() {
		/* $('#delete_data').on('click', function() {
			if (!confirm("삭제된 게시글을 복구할 수 없습니다. 정말로 삭제하시겠습니까?")) {
				Common.loading.hide();
				return false;
			}
		}); */
	});


	function checkInputData(event) {
		if (!$("#title").val()) {
			alert("답변 제목 항목은 필수 입력사항입니다.");
			event.preventDefault();
			$("#title").focus();
			return;
		}
		if (!$("#answer").val()) {
			alert("답변 내용 항목은 필수 입력사항입니다.");
			event.preventDefault();
			$("#answer").focus();
			return;
		}

		<c:choose>
			<c:when test="${qnaAdminAnswer.qnaAdminAnswerId > 0}">
				if (!confirm(Message.get("정보를 수정 하시겠습니까?"))) {
					event.preventDefault();
					return;
				}
			</c:when>
			<c:otherwise>
				if (!confirm(Message.get("정보를 등록 하시겠습니까?"))) {
					event.preventDefault();
					return;
				}
			</c:otherwise>
		</c:choose>
	}

	// 첨부파일 개별 삭제
	function removeQnaAdminAnswerFile(qnaAdminAnswerId, qnaAdminAnswerFileId) {
		Common.loading.show();
		$.post(
    		url("/opmanager/qna-admin/deleteQnaAdminAnswerFile")
    		, {'qnaAdminAnswerId' : qnaAdminAnswerId
    			, 'qnaAdminAnswerFileId' : qnaAdminAnswerFileId}
    		, function(response) {
    			Common.responseHandler(response, function(resp){
        			$("#displayAddFile").empty();

    				let data = resp.data;
    				if (data && data.length > 0) {
	    	            for (let i = 0; i < data.length; i++) {
	       	                let div = "";
	    	                div += '<div class="file_camera">';
	    	                div += '	<a href="/seller/qna-locgov/downloadQnaAdminFile/' + qnaAdminAnswerId + '/' + data[i].qnaAdminAnswerFileId + '">' + data[i].orgFileName + '</a>';

	    	                <c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
		    	                div += '	<img src="/content/images/btn/file_close.gif" alt="close" style="margin-left:5px;padding:3px 0px;cursor:pointer;"'
		    							+ 'onclick="javascript:removeQnaAdminAnswerFile(\'' + qnaAdminAnswerId + '\', \'' + data[i].qnaAdminAnswerFileId + '\');">';
	    					</c:if>

	    	                div += '</div>';
	    	                $('#displayAddFile').append(div);
	    	            }
    				} else {
       	                let div = "";
    					$('#addFileTd').empty(div);
    					div += '<div>';
    					div += 	'<input type="file" id="addFile" name="addFile" class="full input_file" title="';
    					div +=		"${op:message('M01699')}";
    					div += 		'" multiple="multiple"/>';
   						div += '</div>';
  							div += '<c:if test="${qnaAdminAnswer.qnaAdminAnswerId > 0}">';
						div += 	'<div>';
						div += 		"${op:message('첨부파일이 없습니다.')}";
						div += 	'</div>';
						div += '</c:if>';
    	                $('#addFileTd').append(div);
    				}
				});
            }
	    )
	    .always(function () {
	    	Common.loading.hide();
	    });
	}

	function deleteAnswer(qnaAdminAnswerId) {
		if (confirm(Message.get("삭제하시겠습니까?"))) {
			location.href='/opmanager/qna-admin/deleteQnaAdminAnswer/' + qnaAdminAnswerId;
		}
	}

	function deleteLocgovQna(qnaAdminId) {
		if (confirm(Message.get("삭제하시겠습니까?"))) {
			location.href='/opmanager/qna-admin/deleteQnaAdmin/' + qnaAdminId;
		}
	}

</script>
</page:javascript>
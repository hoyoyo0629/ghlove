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

        <div class="admin_wrap btn_all btn_right mb15">
            <div class="flex_box gap-08">
            	<c:if test="${requestContext.user.userId == qnaAdmin.userId && qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId <= '0'}">
            		<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:document.location.href='/seller/qna-locgov/edit/${fn:escapeXml(qnaAdmin.qnaAdminId)}';"><c:out value="${op:message('M00087')}"/></button><!-- 수정 -->
            	</c:if>
                <button type="button" class="btn btn-default btn-mini" onclick="javascript:document.location.href='/seller/qna-locgov/list';"><c:out value="${op:message('M00480')}"/></button><!-- 목록 -->
            </div>
        </div>
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
	                                <c:out value="${qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId > '0' ? op:message('M00463') : op:message('M00464')}"/><!-- 답변완료 --><!-- 답변대기 -->
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
	                                ${op:removeIframe(qnaAdmin.subject)}
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td><!-- 내용 -->
	                        <td colspan="3">
	                            <div>
	                                ${op:removeIframe(qnaAdmin.question)}
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
					                                <a href="/seller/qna-locgov/downloadQnaAdminFile/${fn:escapeXml(qnaAdmin.qnaAdminId)}/${fn:escapeXml(file.qnaAdminFileId)}"><c:out value="${fn:escapeXml(file.orgFileName)}"/></a>
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

            <c:if test="${qnaAdmin.qnaAdminAnswer.qnaAdminAnswerId > '0'}">
            	<form:form modelAttribute="qnaAdminAnswer">
		            <table class="board_write_table" summary="">
		                <colgroup>
		                    <col style="width:220px;">
		                    <col>
		                    <col style="width:220px;">
		                    <col>
		                </colgroup>
		                <tbody>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('답변 제목')}"/></td><!-- 답변 제목 -->
		                        <td>
		                            <div>
		                                ${op:removeIframe(qnaAdminAnswer.title)}
		                            </div>
		                        </td>
		                        <td class="label"><c:out value="${op:message('M00476')}"/></td><!-- 답변일 -->
		                        <td>
		                            <div>
		                                <c:out value="${qnaAdminAnswer.answerDateStr}"/>
		                            </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('M00477')}"/></td><!-- 답변내용 -->
		                        <td colspan="3">
		                            <div>
		                                ${op:removeIframe(qnaAdminAnswer.answer)}
		                            </div>
		                        </td>
		                    </tr>
		                    <tr>
		                        <td class="label"><c:out value="${op:message('M01699')}"/></td><!-- 첨부파일 -->
		                        <td colspan="3">
		                            <c:choose>
			                        	<c:when test="${empty qnaAdminAnswerFiles}">
											<div class="file_camera">
												<c:out value="${op:message('첨부파일이 없습니다.')}"/> <!-- 첨부파일이 없습니다. -->
											</div>
										</c:when>
										<c:otherwise>
											<div>
												<c:forEach items="${qnaAdminAnswerFiles}" var="file" varStatus="i">
													<div class="file_camera">
						                                <a href="/seller/qna-locgov/downloadQnaAdminAnswerFile/${fn:escapeXml(qnaAdminAnswer.qnaAdminAnswerId)}/${fn:escapeXml(file.qnaAdminAnswerFileId)}"><c:out value="${fn:escapeXml(file.orgFileName)}"/></a>
						                            </div>
												</c:forEach>
											</div>
										</c:otherwise>
		                        	</c:choose>
		                        </td>
		                    </tr>
		                </tbody>
		            </table>
	            </form:form>
            </c:if>
        </div>

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
</script>
</page:javascript>
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


		<!-- 상단 타이틀 영역 -->
        <div class="location">
            <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
        </div>
        <h3><span></span></h3>
        <!-- // 상단 타이틀 영역 -->
        <!-- 조회 영역 -->
        <c:if test="${qna.qnaAnswer == null || qna.qnaAnswer.qnaAnswerId == 0}">
	        <div class="btn_all btn_right mb15">
	            <div class="flex_box gap-08">
	                <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteQna('${fn:escapeXml(qna.qnaId)}');"><c:out value="${op:message('문의글 삭제')}"/></button><!-- 문의글 삭제 -->
	                <button type="button" class="btn btn-defualt btn-mini" onclick="location.href='/opmanager/qna-item/list';"><c:out value="${op:message('M00480')}"/></button><!-- 목록 -->
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
                        <td class="label"><c:out value="${op:message('M00460')}"/></td><!-- 문의유형 -->
                        <td>
                            <div>
                            	<c:out value="${qna.qnaGroup}"/>
                            </div>
                        </td>
                        <td class="label"><c:out value="${'비밀글여부'}"/></td><!-- 문의유형 -->
                        <td>
                            <div>
                            	<c:out value="${qna.secretFlag}"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><c:out value="${op:message('M00472')}"/></td><!-- 작성자 -->
                        <td>
                            <div>
                                <c:choose>
									<c:when test="${qna.userId > 0 && !empty qna.loginId}">
										<%-- <a href="javascript:Common.popup('/opmanager/user/popup/details/${qna.userId}', '/user/popup/details', 1100, 800 ,1, 0, 0)"> --%><c:out value="${qna.userName}"/> (<c:out value="${qna.loginId}"/>)<%-- </a> --%>
									</c:when>
									<c:when test="${!empty qna.userName}">
										<c:out value="${qna.userName}"/>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
                            </div>
                        </td>
                        <td class="label"><c:out value="${op:message('M00276')}"/></td><!-- 작성일 -->
                        <td>
                            <div>
                                <c:out value="${op:date(qna.createdDate)}"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><c:out value="${op:message('M00018')}"/></td><!-- 상품명 -->
                        <td colspan="3">
                            <div>
                                <!-- <a href="">베이직 자켓</a> -->
                                <c:choose>
	                                <c:when test='${op:property("saleson.view.type") eq "api"}'>
										<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(qna.itemUserCode)}" target="_blank"><c:out value="${qna.itemName}"/> [ <c:out value="${qna.itemUserCode}"/> ]</a>
									</c:when>
									<c:otherwise>
										<a href="/products/view/${fn:escapeXml(qna.itemUserCode)}" target="_blank"><c:out value="${qna.itemName}"/> [ <c:out value="${qna.itemUserCode}"/> ]</a>
									</c:otherwise>
								</c:choose>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><c:out value="${op:message('M00275')}"/></td><!-- 제목 -->
                        <td colspan="3">
                            <div>
                                <c:out value="${qna.subject}"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><c:out value="${op:message('M00006')}"/></td><!-- 내용 -->
                        <td colspan="3">
                            <div>
                                ${op:nl2br(qna.question)}
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!-- // 조회 영역 -->

		<form:form modelAttribute="qnaAnswer" action="/opmanager/qna-item/answer/${fn:escapeXml(qna.qnaId)}" method="post">
			<form:hidden path="qnaAnswerId" />
	        <!-- 결과 영역 -->
	        <div class="board_write mt30">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
	                    <col>
	                    <col style="width:220px;">
	                    <col>
	                </colgroup>
	                <tbody>
	                	<c:if test="${qnaAnswer != null && qnaAnswer.qnaAnswerId > 0}">
		                	<tr>
	                            <td class="label">작성자</td>
	                            <td>
	                                <div>
	                                    <c:out value="${qnaAnswer.userNm}"/>
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
	                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('답변제목')}"/></td><!-- 답변제목 -->
	                        <td colspan="3">
	                            <div class="flex_box gap-08 item-center">
	                                <form:input path="title" title="${op:message('답변제목')}" maxlength="100" class="input_txt required _filter full" type="text" /><!-- 답변제목 -->
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00477')}"/></td><!-- 답변내용 -->
	                        <td colspan="3">
	                            <div class="flex_box gap-08 item-center">
	                                <span class="placeholder_wrap">
	                                    <span class="placeholder"></span>
	                                    <form:textarea path="answer" cols="30" rows="10" maxlength="30000" class="required _filter" title="${op:message('M00477')}"></form:textarea><!-- 답변내용 -->
	                                </span>
	                            </div>
	                        </td>
	                    </tr>
	                </tbody>
	            </table>
	        </div>
	        <!-- // 결과 영역 -->

	        <!-- 하단 버튼 영역 -->


        	<div class="btn_all btn_right">
		        <c:choose>
			        <c:when test="${qna.qnaAnswer != null && qna.qnaAnswer.qnaAnswerId > 0}">
						<div class="flex_box gap-08">
						    <button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:validCheck(event);"><c:out value="${op:message('답변 수정')}"/></button><!-- 답변 수정 -->
						    <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteQnaAnswer('${fn:escapeXml(qna.qnaId)}');"><c:out value="${op:message('답변 삭제')}"/></button><!-- 답변 삭제 -->
						    <button type="button" class="btn btn-defualt btn-mini" onclick="location.href='/opmanager/qna-item/list';"><c:out value="${op:message('M00480')}"/></button><!-- 목록 -->
						</div>
			        </c:when>
			        <c:otherwise>
			            <div class="flex_box gap-08">
			                <button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:validCheck(event, true);"><c:out value="${op:message('답변 등록')}"/></button><!-- 답변 등록 -->
			            </div>
			        </c:otherwise>
		        </c:choose>
	        <!-- // 하단 버튼 영역 -->
	        </div>
        </form:form>

<page:javascript>
<!-- 2015.1.15 QNA 검증 -->
<script type="text/javascript">

$(function() {
	Common.checkedMaxStringLength('textarea[name=answer]',null, 30000);
});

// 문의글 삭제
function deleteQna(qnaId) {
	if (confirm("${op:message('M00196')}")) {			// 삭제하시겠습니까?
		location.href="/opmanager/qna-item/delete/"+ qnaId;
	}
}

//문의글 삭제
function deleteQnaAnswer(qnaId) {
	if (confirm("${op:message('M00196')}")) {			// 삭제하시겠습니까?
		location.href="/opmanager/qna-item/answer/delete/"+ qnaId;
	}
}

// 필수 입력사항 체크
function validCheck(event, isReg) {
	if (!$("#title").val()) {
		alert("${op:message('답변제목은 필수 입력사항입니다.')}");		// 답변제목은 필수 입력사항입니다.
		event.preventDefault();
		return;
	}
	if (!$("#answer").val()) {
		alert("${op:message('답변내용은 필수 입력사항입니다.')}");		// 답변내용은 필수 입력사항입니다.
		event.preventDefault();
		return;
	}
	if (isReg) {
		if (!confirm("${op:message('답변 등록을 하시겠습니까?')}")) {		// 답변 등록을 하시겠습니까?
			event.preventDefault();
			return;
		}
	} else {
		if (!confirm("${op:message('정보를 수정 하시겠습니까?')}")) {		// 정보를 수정 하시겠습니까?
			event.preventDefault();
			return;
		}
	}
}

</script>
</page:javascript>
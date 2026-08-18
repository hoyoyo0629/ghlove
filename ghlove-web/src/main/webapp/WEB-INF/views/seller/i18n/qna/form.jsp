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
	<div class="board_write">
		<table class="board_write_table">
			<colgroup>
				<col style="width:150px;">
				<col style="width:auto;">
				<col style="width:150px;">
				<col style="width:auto;">
			</colgroup>
			<h3>1:1문의</h3>
			<tbody>
				<tr>
					<td class="label"><c:out value="${op:message('M00460')}"/></td> <!-- 문의구분 -->
					<td>
						<div>
							<c:out value="${qnaAnswerTypeLabel}"/>
						</div>
					</td>

					<td class="label"><c:out value="${op:message('M00467')}"/></td> <!-- 회원여부 -->
					<td>
						<div>
							<c:out value="${qna.userId > 0 ? op:message('M00465') : op:message('M00466')}"/> <!-- 회원 --> <!-- 비회원 -->
					    </div>
					</td>
				</tr>
				<tr>
					<td class="label"><c:out value="${op:message('M00472')}"/></td> <!-- 작성자 -->
					<td>
						<div>
							<c:choose>
								<c:when test="${qna.userId > 0 && !empty qna.loginId}">
									<a href="javascript:Common.popup('/opmanager/user/popup/details/${fn:escapeXml(qna.userId)}', '/user/popup/details', 1100, 800 ,1, 0, 0)"><c:out value="${fn:escapeXml(qna.userName)}"/> (<c:out value="${fn:escapeXml(qna.loginId)}"/>)</a>
								</c:when>
								<c:when test="${!empty qna.userName}">
									<c:out value="${qna.userName}" />
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</div>
					</td>
					<td class="label"><c:out value="${op:message('M00276')}"/></td> <!-- 작성일 -->
					<td>
						<div>
							<c:out value="${op:date(qna.createdDate)}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">제목</td> <!-- 제목 -->
					<td colspan="3">
						<div>
							<c:out value="${qna.subject}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">내용</td> <!-- 내용 -->
					<td colspan="3">
						<div>
							<c:out value="${op:nl2br(qna.question)}"/>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div><br/>

	<div class="board_write">
		<form:form modelAttribute="qnaAnswer" action="/opmanager/qna/answer/${fn:escapeXml(qna.qnaId)}" method="post">
			<input type="hidden" name="qnaAnswerId" value="${fn:escapeXml(qnaAnswer.qnaAnswerId)}" />
			<input type="hidden" name="email" value="${fn:escapeXml(qna.email)}" />
			<input type="hidden" name="subject" value="${fn:escapeXml(qna.subject)}" />
			<input type="hidden" name="userName" value="${fn:escapeXml(qna.userName)}" />
			<input type="hidden" name="userId" value="${fn:escapeXml(adminUser.userId)}" />
			<table class="board_write_table">
				<colgroup>
					<col style="width:150px;">
					<col style="width:auto;">
				</colgroup>
				<h3>1:1문의 답변하기</h3>

				<tbody>
					<tr>
						<td class="label">답변제목</td> <!-- 답변제목 -->
						<td>
							<div>
								<form:input path="title" title="답변제목" class="half" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label"><c:out value="${op:message('M00477')}"/></td> <!-- 답변내용 -->
						<td>
							<div>
								<form:textarea path="answer" rows="5" cols="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">답변전달방식</td> <!-- 답변전달방식 -->
						<td>
							<div>
								<form:checkbox path="sendSmsFlag" title="SMS" value="Y" label="SMS" />
								<form:checkbox path="sendMailFlag" title="이메일" value="Y" label="이메일" /> (* 체크하시면 답변 완료시 동시에 전달이 됩니다.)
							</div>
						</td>
					</tr>
				</tbody>
			</table>

			<div class="btn_center">
				<div>
					<a href="/opmanager/qna/list" class="btn btn-default"><span><c:out value="${op:message('M00480')}"/></span></a> <!-- 목록 -->
					<button type="submit" class="btn btn-active">저장</button>
					<button type="button" class="btn btn-default" onclick="location.href='/opmanager/qna/view/${fn:escapeXml(qnaId)}'">
						<span>취소</span>
					</button>
				</div>
			</div>
		</form:form>
	</div>


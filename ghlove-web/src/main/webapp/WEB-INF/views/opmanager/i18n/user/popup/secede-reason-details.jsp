<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">회원탈퇴</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<div class="popup_contents">
		<table class="board_write_table" summary="회원탈퇴">
			<caption>회원탈퇴</caption>
			<colgroup>
				<col style="width: 150px" />
				<col />
			</colgroup>
			<tbody>
				<tr>
					<td class="label">탈퇴사유</td>
					<td colspan="3">
						<div>
							<c:if test="${not empty details.leaveCodeLabel}"><c:out value="${details.leaveCodeLabel}"/><br/></c:if>
							<c:out value="${details.leaveReason}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">탈퇴구분</td>
					<td>
						<div>
							<c:out value="${(not empty details.leaveUserId) ? '관리자탈퇴' : '회원탈퇴'}"/>
						</div>
					</td>
					<td class="label">담당자</td>
					<td>
						<div>
							<c:choose>
								<c:when test="${not empty details.leaveUserId}">
									<c:out value="${details.roleName}"/>
									<c:if test="${not empty details.leaveUserName}">
										<br/>(<c:out value="${details.leaveUserName}"/>)
									</c:if>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>

			</tbody>
		</table>
		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="self.close();">확인</button>
		</p>
	</div>
</div>
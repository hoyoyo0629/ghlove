<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">개인정보 열람</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<div class="popup_contents">
		<table class="board_write_table" summary="개인정보 열람">
			<caption>개인정보 열람</caption>
			<colgroup>
				<col style="width: 150px" />
				<col />
			</colgroup>
			<tbody>
				<tr>
					<td class="label">회원구분</td>
					<td><div>일반회원</div></td>
				</tr>
				<tr>
					<td class="label">이름</td>
					<td><div><c:out value="${details.userName}"/></div></td>
				</tr>
				<tr>
					<td class="label">아이디</td>
					<td><div><c:out value="${details.loginId}"/></div></td>
				</tr>
				<tr>
					<td class="label">생년월일</td>
					<td>
						<div>
							<c:if test="${not empty details.birthday}">
								<c:set var="birthdayCustom" value="${fn:replace(details.birthday, '-', '')}" />
								<c:if test="${fn:length(birthdayCustom) eq 8}">
									<fmt:parseDate var="birthdayDateFmt" pattern="yyyyMMdd" value="${birthdayCustom}" />
									<fmt:formatDate var="birthdayStringFmt" pattern="yyyy-MM-dd" value="${birthdayDateFmt}" />
									<c:out value="${birthdayStringFmt}"/>
								</c:if>
							</c:if>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">휴대폰</td>
					<td><div><c:out value="${details.phoneNumber}"/></div></td>
				</tr>
				<tr>
					<td class="label">이메일 주소</td>
					<td><div><c:out value="${details.email}"/></div></td>
				</tr>
				<tr>
					<td class="label" rowspan="2">주소</td>
					<td><div><c:out value="${details.post}"/></div></td>
				</tr>
				<tr>
					<td><div><c:out value="${details.address}"/> <c:out value="${details.addressDetail}"/></div></td>
				</tr>
				<tr>
					<td class="label">EMAIL 수신동의</td>
					<td><div><c:out value="${details.receiveEmail eq '0' ? '동의' : '비동의'}"/></div></td>
				</tr>
				<tr>
					<td class="label">SMS 수신동의</td>
					<td><div><c:out value="${details.receiveSms eq '0' ? '동의' : '비동의'}"/></div></td>
				</tr>
				<%--
				<tr>
					<td class="label">성별</td>
					<td><div><c:out value="${details.gender eq '0' ? '남' : '여'}"/></div></td>
				</tr>
				 --%>
				<tr>
					<td class="label">가입일</td>
					<td><div><c:out value="${details.createdDate}"/></div></td>
				</tr>
				<tr>
					<td class="label">마지막 로그인</td>
					<td><div><c:out value="${details.loginDate}"/></div></td>
				</tr>
			</tbody>
		</table>
		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="self.close();">확인</button>
		</p>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		// 윈도우 팝업 사이즈 재조정
		window.resizeTo(600, 810);
	});
</script>
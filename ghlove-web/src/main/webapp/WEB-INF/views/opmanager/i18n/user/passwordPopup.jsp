<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


<div class="popup_wrap">
	<h1 class="popup_title">비밀번호 변경</h1>
	
	<div class="popup_contents">
		<form id="passwordForm" action="/opmanager/user/manager/change-password" method="POST" >
			<input type="hidden" name="userId" value="${fn:escapeXml(manager.userId)}" />
			<input type="hidden" name="loginId" value="${fn:escapeXml(manager.loginId)}" />
			<fieldset>
				<legend class="hidden">비밀번호 변경</legend>

				<h2>비밀번호 변경</h2>
				<div class="board_write">
					<table class="board_write_table">
						<colgroup>
							<col style="width:130px;" />
							<col style="" />
						</colgroup>
						<tbody>
							<tr>
								<td class="label">현재 비밀번호</td>
								<td>
									<div>
										<input type="password" name="password" maxlength="20" class="required" title="현재 ${op:message('M00150')}" />
								    </div>
								</td>
							</tr>
							<tr>
								<td class="label">새 비밀번호</td>
								<td>
									<div>
										<input type="password" name="changePassword" id="change_password" maxlength="20" class="required _password _duplicated" title="새 ${op:message('M00150')}" />
								    </div>
								</td>
							</tr>
							<tr>
								<td class="label">새 비밀번호 확인</td>
								<td>
									<div>
										<input type="password" name="reChangePassword" id="password_confirm" maxlength="20" class="required _password _duplicated" title="새 ${op:message('M00150')} 확인" />
									</div>
								</td>
							</tr>	
						</tbody>
					</table>
				</div>

				<div class="buttons">
					<button type="submit" class="btn btn-active"><c:out value="${op:message('M00087')}"/> <!-- 저장 --></button>	
				</div>
			</fieldset>
		</form>
		<a href="#" class="popup_close">창 닫기</a>
	</div>
</div>


<page:javascript>
	<script type="text/javascript">
		try {
			$('#passwordForm').validator(function () {

				if ($("#password_confirm").val() != '') {
					if ($("#change_password").val() != $("#password_confirm").val()) {
						alert("${op:message('M00158')}");
						$("#password_confirm").focus();
						return false;
					}
				}
			});
		} catch (e) {
			alert(e.message);
		}
	</script>
</page:javascript>

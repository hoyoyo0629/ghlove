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
					<td class="label">기부누적액</td>
					<td>
						<div>
							<fmt:formatNumber value="${cumclativeTotal.totalCntrAmt}" pattern="#,###.##"/>
						</div>
					</td>
					<td class="label">포인트잔액</td>
					<td>
						<div>
							<fmt:formatNumber value="${cumclativeTotal.totalCntrBlcePoint}" pattern="#,###.##"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">탈퇴사유</td>
					<td colspan="3">
						<div>
							<span class="placeholder_wrap">
								<span class="placeholder"></span>
								<textarea id="leaveReason" cols="30" rows="10" maxlength="250" class="required _filter" title="탈퇴사유"></textarea>
							</span>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="secede();">회원탈퇴</button>
			<button type="button" class="btn btn-normal" onclick="self.close();">취소</button>
		</p>
	</div>
</div>

<script type="text/javascript">

	/**
	 *	함 수 명 :
	 *	기	능  :
	 */
	function secede() {
		if($.trim($("#leaveReason").val()) == "") {
			alert("탈퇴사유 항목을 입력해 주세요.")
			$("#leaveReason").focus();
			return false;
		}

		if(confirm("회원을 탈퇴 시키겠습니까?")) {
			var param = {
				"userId": "${fn:escapeXml(userId)}",
				"leaveReason" : $("#leaveReason").val()
			};

			$.post("/opmanager/user/customer/secede", param, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						alert("탈퇴되었습니다.");
						opener.customerSecedeCallBack(response.data.isLogout);
						self.close();
					} else if(response.data.code == "ERR_ALR_SECEDE") {
						alert("이미 탈퇴된 회원입니다.");
						opener.customerSecedeCallBack();
						self.close();
					} else if(response.data.code == "ERR_ONE_PASS") {
						alert("디지털원패스 회원은 탈퇴 처리 불가능합니다.");
					} else {
						alert("오류가 발생했습니다.")
					}
				}
			});
		}
	}
</script>
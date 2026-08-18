<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


	<script type="text/javascript" src="/content/modules/jsencrypt.min.js"></script>

    <!-- 개발 영역 -->
    <!-- 초기 비밀번호 설정 popup -->
    <div class="popup_wrap">
        <div id="pop_header">
            <h1 class="popup_title">비밀번호 설정</h1>
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

        <div class="popup_contents">
            <div class="flex_box col item-center">
                <p class="txt mb20 txt_center">비밀번호 변경을 진행하시기 바랍니다.</p>

                <div class="board_write" id="pwdDiv">
                    <table class="board_write_table" summary="">
                        <colgroup>
                            <col style="width:150px;">
                        </colgroup>
                        <tbody>
                            <tr>
                                <td class="label">새 비밀번호</td>
                                <td>
                                    <div class="flex_box gap-08">
                                        <input id="newPwd" name="newPwd" title="새 비밀번호" class="input_txt required _filter full _password _duplicated" type="password" value="" onkeyup="javascript:checkCapsLock(event, 'newPwdWarn');">
                                    </div>
                                    <div id="newPwdWarn" style="display:none;" class="red">CAPS LOCK 키가 동작 중입니다. 대문자로 입력됩니다.</div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">새 비밀번호 확인</td>
                                <td>
                                    <div class="flex_box gap-08">
                                        <input id="confirmPwd" name="confirmPwd" title="새 비밀번호 확인" class="input_txt required _filter full _password _duplicated" type="password" value="" onkeyup="javascript:checkCapsLock(event, 'confirmPwdWarn');">
                                    </div>
                                    <div id="confirmPwdWarn" style="display:none;" class="red">CAPS LOCK 키가 동작 중입니다. 대문자로 입력됩니다.</div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <ul class="list-bullet mt15">
                    <li>9 ~ 20자 영문, 숫자, 특수문자를 사용하세요. (3개 이상의 연속된 문자나 숫자 사용불가)</li>
                    <li>아이디를 포함할 수 없습니다.</li>
                </ul>

                <p class="popup_btns">
                    <button type="button" class="btn btn-dark-gray" onclick="javascript:changePwd();">변경하기</button>
                </p>
            </div>

        </div>
    </div>
    <!-- // 초기 비밀번호 설정 popup -->
    <!-- // 개발 영역 -->

<script type="text/javascript">

	let publicKeyStr = "${fn:escapeXml(publicKeyStr)}";
	let loginId = "${fn:escapeXml(loginId)}";

	function changePwd() {
		let defaultOptions = {
			'requiredClass': 'required',
			'optionalClass': '',
			'submitHandler': ''
		};
		if ($.validator.validate($("#pwdDiv"), defaultOptions)) {
			if($('input[name=newPwd]').val() != $('input[name=confirmPwd]').val()){
				alert('입력한 비밀번호가 일치하지 않습니다.');
				$('input[name=reChangePassword]').focus();
				return false;
			}

			if($('input[name=newPwd]').val().indexOf(loginId) > -1){
				alert('비밀번호에 아이디를 포함할 수 없습니다.');
				$('input[name=changePassword]').focus();
				return false;
			}

			if (confirm("비밀번호를 변경 하시겠습니까?")) {
				let crypt = new JSEncrypt();
				crypt.setPrivateKey(publicKeyStr);

				$.post("/seller/change-password"
					, {"loginId": loginId, "password" : $('input[name=newPwd]').val()}
					, function(response){
						if (response) {
							if (response.isSuccess) {
								window.opener.postMessage({fnName: 'goMain'}, '*');
							} else {
								alert(response.errorMessage);
							}
						} else {
							alert("문제가 발생했습니다.");
						}
					}
					, "json"
				);
			}
		};
	}


	function checkCapsLock(event, controlDivId) {
		if (event.getModifierState("CapsLock")) {
			$("#" + controlDivId).show();
		} else {
			$("#" + controlDivId).hide();
		}
	}

</script>
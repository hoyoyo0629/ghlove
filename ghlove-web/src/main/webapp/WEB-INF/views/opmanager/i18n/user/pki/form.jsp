<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery-ui.min.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/jquery.blockUI.js"></script>
<!-- ML4WEB JS -->
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/ML_Config.js"></script>
<!-- 간편인증 JS -->
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ML4Web_Config.js"></script>
<script type="text/javascript" src="/content/MagicLine4Web/ML4Web/js/ext/magicline-js-api.min.js"></script>

<script type="text/javascript" src="/content/modules/op.fincert.js"></script>
<style>
.offCharge {
	display:none;
}
</style>

<div class="admin_wrap">
	<!-- Header -->
	<div id="header">
		<div class="header_wrap">
			<h1><a href="javascript:;"><img src="/content/opmanager/images/img_logo.png" alt="고향사랑기부제" /></a></h1>
		</div>
	</div>
	<!--// Header -->

	<!-- Container -->
	<div id="container" class="login">
		<div class="login_cont">
			<div class="login_tit mt-70">인증서 등록</div>
			<div class="form-wrap">
				<div class="board_write">
					<table class="board_write_table">
						<colgroup>
							<col style="width: 220px;" />
						</colgroup>
						<tr>
							<td class="label">인증서 종류</td>
							<td>
								<div class="flex_box gap-08">
									<input type="radio" id="fin" name="cert-type" value="F" />
									<label for="fin"><p class="txt">금융인증서</p></label>
									<input type="radio" id="pki" name="cert-type" value="P" />
									<label for="pki"><p class="txt">공동인증서(구 공인인증서)</p></label>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">담당자 선택</td>
							<td>
								<div class="flex_box gap-08">
									<input type="radio" id="loc" name="charge-type" value="L"/>
									<label for="loc"><p class="txt">지자체 담당자</p></label>
									<input type="radio" id="off" name="charge-type" value="O" />
									<label for="off"><p class="txt">오프라인 담당자</p></label>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>아이디</td>
							<td>
								<div>
									<input id="loginId" class="optional sixsix" title="아이디" type="text" placeholder="아이디">
								</div>
							</td>
						</tr>
						<tr class="locInfo">
							<td class="label"><span class="required_mark">*</span>휴대폰번호</td>
							<td>
								<div>
									<input id="phoneNumber" class="optional sixsix" title="휴대폰번호" type="text" placeholder="01012345678 (특수기호 제외)">
								</div>
							</td>
						</tr>
						<tr class="locInfo">
							<td class="label"><span class="required_mark">*</span>생년월일</td>
							<td>
								<div>
									<span class="datepicker">
										<input id="birthday" class="datepicker optional" title="생년월일" type="text" placeholder="19940101(8자)" style="width:200px;">
									</span>
								</div>
							</td>
						</tr>
						<tr class="offCharge">
							<td class="label"><span class="required_mark">*</span>지점 연락처</td>
							<td>
								<div>
									<input id="telNumber" class="optional sixsix" title="지점 연락처" type="text" placeholder="0551234567 (특수기호 제외)">
								</div>
							</td>
						</tr>
						<tr class="offCharge">
							<td class="label"><span class="required_mark">*</span>개인번호</td>
							<td>
								<div>
									<input id="offEmpId" class="optional optional" title="개인번호" type="text" placeholder="개인번호">
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="button_wrap">
				<button type="button" class="btn btn-dark-gray btn-mid h-large" onclick="pkiModify('D');"><span>삭제</span></button>
				<button type="button" class="btn btn-dark-gray btn-mid h-large" onclick="pkiModify('C');"><span>등록</span></button>
				<button type="button" class="btn btn-default btn-mid h-large" onclick="moveLoginPage();"><span>취소</span></button>
			</div>
		</div>
	</div>
	<!--// Container -->

	<!-- footer -->
	<div id="footer">
		<div class="footer_wrap">
			<span class="copy">© Ministry of the interior and safety. All rights reserved.</span>
		</div>
	</div>
	<!--// footer -->
</div>

<!-- 결과 수신 메시지  -->
<form id="reqForm" name="reqForm" method="post" action="/opmanager/magicline/signedFormRGhlove" target="magiclineIframe">
	<input type="hidden"	id="signOrigin"		name="signOrigin" /> <!-- 180701 서명 원문 폼 추가 -->
	<input type="hidden"	id="sign"			name="sign"/>
	<input type="hidden"	id="csCheckType"	name="csCheckType"	value="1"/>
	<input type="hidden"	id="signData"		name="signData"		value="LOGIN" />
</form>
<!--// 결과 수신 메시지  -->

<!-- magic line 영역 -->
<iframe name="magiclineIframe" style="display:none;" width="1200px" height="500px"></iframe>
<!--// magic line 영역 -->

<!-- 회원 정보 -->
<input type="hidden" id="userId" />
<input type="hidden" id="mberDn" />
<input type="hidden" id="mberFinDn" />
<!--// 회원 정보 -->

<script type="text/javascript">
	$(function() {
		// 날짜 변경 이벤트
		datepickerChange();

		// 휴대폰번호 변경 이벤트
		phoneNumberChange();

		// enter Key Event
		$('#loginId, #phoneNumber, #birthday').keypress(function(e) {
			if (e.keyCode === 13) {
				e.preventDefault();
				pkiModify("C");
			}
		});

		//지자체담당자 오프라인담당자 input 입력 스위치
		$('input:radio[name=charge-type]').click(function(e) {
			//휴대폰번호 생년월일 안보이고 지자체번호 개인번호 보임
			if ($("input[name=charge-type]:checked").val() == 'O') {
				$('.locInfo').hide();
				$('.offCharge').show();
			} else if ($("input[name=charge-type]:checked").val() == 'L') {
				$('.offCharge').hide();
				$('.locInfo').show();
			}
		});

	});

	/**
	 *	함 수 명 : moveLoginPage
	 *	기	능  : 로그인 페이지 이동
	 */
	function moveLoginPage() {
		location.href = "/opmanager/login";
	}

	/**
	 *	함 수 명 : pkiModify
	 *	기	능  : 인증서 수정 (등록/삭제)
	 */
	function pkiModify(type) {
		if(!validator()) return false;

		var param;
		if($("input[name=charge-type]:checked").val() == 'L') {// 지자체 담당자일 경우
			param = {
				"loginId": $("#loginId").val(),
				"phoneNumber": $("#phoneNumber").val(),
				"birthday": $("#birthday").val(),
				"charge": "loc"
			};
		} else if($("input[name=charge-type]:checked").val() == 'O') { // 오프라인 담당자일 경우
			param = {
				"loginId": $("#loginId").val(),
				"phoneNumber": $("#telNumber").val(), //백단에서 phoneNumber으로 비교함
				"offEmpId": $("#offEmpId").val(),
				"charge": "off"
			};
		}

		var pkiType = $("input[name=cert-type]:checked").val();

		$.get("/opmanager/user/pki/form/valid", param, function(response){
			console.table(response);
			if(response.isSuccess && response.data) {
				if(response.data.code && response.data.userId && response.data.code == "SUCC") {
					// 회원 정보 저장
					$("#userId").val(response.data.userId);
					$("#mberDn").val(response.data.mberDn);
					$("#mberFinDn").val(response.data.mberFinDn);

					console.log(response.data);
					// 공동인증서
					if (pkiType == 'P') {
						// 인증서 등록
						if(type == "C") {

							// 공동인증서 존재여부
							if(!response.data.mberDn) {
								doSignData();
							} else {
								alert("등록한 공동인증서가 존재합니다. 삭제후 등록해주세요.");
								return;
							}

						// 인증서 삭제
						} else if(type == "D") {
							pkiDelete(pkiType);
						}
					}

					// 금융인증서
					if (pkiType == 'F') {
						// 인증서 등록
						if(type == "C") {
							// 금융인증서 존재여부
							if(!response.data.mberFinDn) {
								fincertSignData(response.data.mberCi);
							} else {
								alert("등록한 금융인증서가 존재합니다. 삭제후 등록해주세요.");
								return;
							}
						// 인증서 삭제
						} else if(type == "D") {
							pkiDelete(pkiType);
						}
					}



				} else if(response.data.code && response.data.code == "ERR_PHONE") {
					if($("input[name=charge-type]:checked").val() == 'L') {// 지자체 담당자일 경우
						alert("휴대폰번호가 존재하지 않습니다.");
					} else if($("input[name=charge-type]:checked").val() == 'O') { // 오프라인 담당자일 경우
						alert("지점 연락처가 존재하지 않습니다.");
					}
				} else if (response.data.code && response.data.code == "ERR_CI") {
					alert("CI값이 존재하지 않습니다.");
				} else {
					var message = (type == "C") ? "등록" : "삭제";
					alert("회원정보가 존재하지 않아 인증서 "+ message +"할 수 없습니다.");
				}
			} else {
				alert("오류가 발생했습니다.");
			}
		}, "json");
	}

	/**
	 *	함 수 명 : validator
	 *	기	능  : 유효성 검사
	 */
	function validator() {

		if (!$("input[name=cert-type]").is(':checked')) {
			alert("인증서 종류를 선택해 주세요.");
			$("input[name=cert-type]").focus();
			return false;
		}

		if (!$("input[name=charge-type]").is(':checked')) {
			alert("담당자 종류를 선택해 주세요.");
			$("input[name=charge-type]").focus();
			return false;
		}

		if($.trim($("#loginId").val()) == "") {
			alert("아이디를 입력해 주세요.");
			$("#loginId").focus();
			return false;
		}

		if($("input[name=charge-type]:checked").val() == 'L') {// 지자체 담당자일 경우
			if($.trim($("#phoneNumber").val()) == "") {
				alert("휴대폰번호를 입력해 주세요.");
				$("#phoneNumber").focus();
				return false;
			}

			if($.trim($("#birthday").val()) == "") {
				alert("생년월일을 입력해 주세요.");
				$("#birthday").focus();
				return false;
			}
		} else if($("input[name=charge-type]:checked").val() == 'O') { // 오프라인 담당자일 경우
			if($.trim($("#telNumber").val()) == "") {
				alert("지점 연락처를 입력해 주세요.");
				$("#telNumber").focus();
				return false;
			}

			if($.trim($("#offEmpId").val()) == "") {
				alert("개인번호를 입력해 주세요.");
				$("#offEmpId").focus();
				return false;
			}
		}

		return true;
	}

	/**
	 *	함 수 명 : doSignData
	 *	기	능  : 인증서 활성화
	 */
	function doSignData() {
		var signData = $("#signData").val();
		document.reqForm.signOrigin.value = document.reqForm.signData.value;
		magicline.uiapi.MakeSignData(signData, null, mlCallBack);
	}

	/**
	 *	함 수 명 : doSignData
	 *	기	능  : MagicLine 결과값 수신 CallBack
	 *	파라미터  : code - 전자서명 결과값, message - 전자서명 메시지
	 */
	function mlCallBack(code, message) {
		if(code==0){
			document.reqForm.sign.value = encodeURIComponent( message.encMsg );
			document.reqForm.submit();
		}else{
			alert("결과값 수신에 실패하였습니다.");
			return;
		}
	}
	/**
	 *	함 수 명 : signedResponse
	 *	기	능  : 인증서 검증 결과 callback
	 *	파라미터  : result - DN, message, result 정보 리턴
	 */
	function signedResponse(response) {

		var param = {
			"userId": $("#userId").val(),
			"mberDn" : "",
			"mberFinDn" : ""
		};

		if ($("input[name=cert-type]:checked").val() == 'P') {
			if(response && response.result && response.result == "SUCCESS" && response.DN) {
				param.mberDn = response.DN;
			}
		} else if ($("input[name=cert-type]:checked").val() == 'F') {
			if (response && response.dn) {
				param.mberFinDn = response.dn;
				//param.mberDn = response.dn;
			}
		}

		if (param.mberDn == '' && param.mberFinDn == '') {
			alert('인증서 등록에 실패했습니다.');
			return false;
		}
		//if (param.mberDn == '') return false;

		pkiModifyProccess(param, function(response) {
			if(response.code && response.code == "SUCC") {
				alert("인증서 등록이 완료되었습니다.");
				moveLoginPage();
			} else {
				alert("오류가 발생했습니다.");
			}
		});

	}

	/**
	 *	함 수 명 : pkiDelete
	 *	기	능  : 인증서 삭제
	 */
	function pkiDelete(pkiType) {
		const mberDn = $("#mberDn").val();
		const mberFinDn = $("#mberFinDn").val();
		const param = {
			"userId": $("#userId").val(),
			"type": pkiType
		};
		if(pkiType == 'P') {
			param.mberDn = mberDn;
			param.mberFinDn = '';
			if(mberDn == "" || mberDn == "undefined" || mberDn.toLowerCase() == "null") {
				alert("등록된 공동인증서가 없습니다.");
				return false;
			}
		}

		if(pkiType == 'F') {
			param.mberFinDn = mberFinDn;
			param.mberDn = '';
			if(mberFinDn == "" || mberFinDn == "undefined" || mberFinDn.toLowerCase() == "null") {
				alert("등록된 금융인증서가 없습니다.");
				return false;
			}
		}

		if(confirm("등록된 인증서를 삭제 하시겠습니까?")) {

			pkiDeleteProccess(param, function(response) {
				console.log(response);
				if(response.code && response.code == "SUCC") {
					alert("인증서가 삭제되었습니다.");
					location.reload();
				} else {
					alert("오류가 발생했습니다.");
				}
			});
		}
 	}

	/**
	 *	함 수 명 : pkiModifyProccess
	 *	기	능  : 인증서 수정 처리
	 */
	function pkiModifyProccess(param, callBack) {
		$.post('/opmanager/user/pki/update', param, function(response) {
			if(response.isSuccess && response.data) {
				callBack(response.data);
			}
		});
	}

	/**
	 *	함 수 명 : pkiDeleteProccess
	 *	기	능  : 인증서 삭제 처리
	 */
	function pkiDeleteProccess(param, callBack) {
		$.post('/opmanager/user/pki/delete', param, function(response) {
			if(response.isSuccess && response.data) {
				callBack(response.data);
			}
		});
	}

	/**
	 *	함 수 명 : datepickerChange
	 *	기	능  : 날짜 변경 이벤트
	 */
	function datepickerChange() {
		if($("input.datepicker").length) {
			$("input.datepicker").keyup(function(e) {
				if(e && e.target && e.target.value) {
					var value = e.target.value.replace(/[^0-9]/g, "");
					if(value.length > 8) { value = value.slice(0, 8); }
					e.target.value = value;
				}
			});
		}
	}

	/**
	 *	함 수 명 : phoneNumberChange
	 *	기	능  : 휴대폰번호 변경 이벤트
	 */
	function phoneNumberChange() {
		if($("#phoneNumber").length) {
			$("#phoneNumber").keyup(function(e) {
				if(e && e.target && e.target.value) {
					var value = e.target.value.replace(/[^0-9]/g, "");
					if(value.length > 11) { value = value.slice(0, 11); }
					e.target.value = value;
				}
			});
		}
	}

	function fincertSignData(mberCi) {
		fincert.getUserDn(function (response) {//fincert에서 인증서 정보 ci 사용자정보 등
			if (mberCi != response.ci) {
				alert('회원정보와 인증서정보가 일치하지 않습니다.');
				return false;
			}

			signedResponse(response);
		}, function (err) {
			if (err && err.description) {
				alert(err.description + "\n해당 현상이 지속될 경우 관리자에게 문의 부탁드립니다.");
				return false;
			} else {
				alert("금융인증서 호출에 실패하였습니다. \n해당 현상이 지속될 경우 관리자에게 문의 부탁드립니다.");
				return false;
			}
		});
	}

	function fincertSignDeleteData(mberCi, pkiType) {
		fincert.getUserDn(function (response) {//fincert에서 인증서 정보 ci 사용자정보 등
			if (mberCi != response.ci) {
				alert('회원정보와 인증서정보가 일치하지 않습니다.');
				return false;
			}

			pkiDelete(pkiType);
		}, function (err) {
			if (err && err.description) {
				alert(err.description + "\n해당 현상이 지속될 경우 관리자에게 문의 부탁드립니다.");
				return false;
			} else {
				alert("금융인증서 호출에 실패하였습니다. \n해당 현상이 지속될 경우 관리자에게 문의 부탁드립니다.");
				return false;
			}
		});
	}

</script>
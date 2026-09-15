<template>
    <form action="/" id="authModalForm" name="authModalForm"  @submit.prevent>
        <!-- 공인인증서 | 휴대폰 인증 모달 -->
        <div class="black-bg" id="authModal" tabindex="0">
            <div class="modal-overlayer join-complete">
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기" class="closeModal"
                    @click="closeModal()">
                <div class="overlayer-header">
                    본인인증
                </div>
                <div class="overlayer-body">
                    <div class="overlayer-body-wrap">
                        <h2>아래의 본인 인증 수단 중<br>가능한 방식을 선택해서 인증을 진행하시기 바랍니다.</h2>
                        <p>휴대폰 인증은 본인 소유의 휴대폰만 해당 됩니다.</p>

                        <div class="authentication-area">
                            <!-- 금융 인증서 -->
                            <div class="authentication-box financ">
                                <div class="auth_tit">
                                    <h3><span>금융</span> <span>인증서</span></h3>
                                </div>
                                <button class="formBtn financ" type="button" @click="doCertInit()">인증하기</button>
                            </div>

                            <!-- 휴대폰 인증 -->
                            <div class="authentication-box">
                                <div class="auth_tit">
                                    <h3>휴대폰</h3>
                                    <p class="s-txt">본인 명의로 등록된 휴대폰으로<br> 본인 인증 하기</p>
                                </div>
                                <button class="formBtn financ" type="button" @click="mobileAuth()">인증하기</button>
                            </div>

                            <!-- 공동 인증서 -->
                            <!-- 공동 인증서 미사용으로 주석 처리 함 -->
                            <!-- <div class="authentication-box auth" id="signBox">
                                <div class="auth_tit">
                                    <h3><span>공동</span> <span>인증서</span></h3>
                                    <p class="s-txt">(구 공인인증서)</p>
                                </div>

                                <div class="btn_group">
                                    <button class="blueBtn financ" type="button" @click="goSignRegist()">인증서 등록</button>
                                    <button class="formBtn financ" type="button" @click="doSignData()">인증하기</button>
                                </div>
                            </div> -->
                        </div>

                        <!-- 버튼그룹 -->
                        <div class="btn-box">
                            <button type="button" class="blueBtn cancellation" @click="closeModal()">
                                취소
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="btn-arrow">
                                </span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- //공인인증서 | 휴대폰 인증 모달 -->
    </form>
</template>

<script>
module.exports = {
    data: function () {
        return {

        }
    }, methods: {
        goToMain: function () {
            location.href = "/";
            // 메인화면 URL 지정하기
        },
        openWin: function (event) {
            console.log = () => {};
        },
        nextStep: function () {
            $(".black-bg").addClass("show");
            setTimeout(function () { $("#authModal").focus(); }, 100);
        },
        closeModal: function () {
            $(".black-bg").removeClass("show");

            // 아이디/비밀번호 찾기
            if (this.$parent.tab) {
                if (this.$parent.tab == 'id') {
                    setTimeout(function () {$("#idAuthBtn").focus();}, 100);
                } else {
                    setTimeout(function () {$("#pwAuthBtn").focus();}, 100);
                }
            } else if (this.$parent.tabId) {
                setTimeout(function () {$("#loginBtn").focus();}, 100);
            }

            //setTimeout(function () {$(".blueBtn u-confirm").focus();})
        },
        mobileAuth: function () {

            // 재인증이므로 가입 때 쓴 CI 를 그대로 재사용해야 본인으로 인식된다.
            if ($s.config.isSkipExternalAuth) {
                $s.api.skipExternalAuth("휴대폰 인증");
                return;
            }

            $s.api.mobileAuth(function (response) {

                var PCC_window = window.open('', 'PCCV3Window', 'width=400, height=630, resizable=1, scrollbars=no, status=0, titlebar=0, toolbar=0, left=300, top=200');
                if (PCC_window == null) {
                    alert("팝업 차단을 해제해주세요.");
                    return;
                }

                var form = document.createElement("form");
                form.setAttribute('target', 'PCCV3Window');
                // form.setAttribute("charset", "UTF-8");
                form.setAttribute("method", "Post");
                form.setAttribute("action", "https://pcc.siren24.com/pcc_V3/jsp/pcc_V3_j10_v4.jsp");

                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "reqInfo");
                hiddenField.setAttribute("value", response.reqInfo);
                form.appendChild(hiddenField);

//                 hiddenField = document.createElement("input");
//                 hiddenField.setAttribute("type", "hidden");
//                 hiddenField.setAttribute("name", "retUrl");
//                 hiddenField.setAttribute("value", response.retUrl);
//                 form.appendChild(hiddenField);

                hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "verSion");
                hiddenField.setAttribute("value", response.verSion);
                form.appendChild(hiddenField);

				// Server to Server 추가 (crypto_token_id, integrity_value)
                hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "crypto_token_id");
                hiddenField.setAttribute("value", response.crypto_token_id);
                form.appendChild(hiddenField);

                hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", "integrity_value");
                hiddenField.setAttribute("value", response.integrity_value);
                form.appendChild(hiddenField);

                document.body.appendChild(form);
                form.submit();

            });

        },
        // 인증서 클릭 이벤트
        doSignData: function () {
            $(".black-bg").removeClass("show")
            this.$emit("signdata");

            //magicline.uiapi.MakeSignData( "LOGIN", null, mlCallBack);
        },
        doCertInit: function () {
            if ($s.config.isSkipExternalAuth) {
                $s.api.skipExternalAuth("금융인증서");
                return;
            }

            fCert.initSign(vm.findType);
        },
        goSignRegist: function () {
            location.href = "/users/sign-certificate.html";
        }
    }, mounted: function () {
        this.$nextTick(function () {
            // 수정 : 공동 인증서 pc일 경우 사용, 휴대기기일 경우 사용불가(화면에서 가리기) 230424
            var isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ? true : false;
            if (!isMobile) {
                //모바일이 아닌 경우 스크립트
                $('#signBox').show();
            } else {
                //모바일인 경우 스크립트
                $('#signBox').hide();
            }
        });
    }
}

// MagicLine 결과값 수신 CallBack
// code    : 전자서명 결과값
// message : 전자서명 메시지
function mlCallBack(code, message) {
    if (code == 0) {
        var formData = {
            "signOrigin": "LOGIN",
            "sign": encodeURIComponent(message.encMsg),
            "csCheckType": "1",
            "signData": "LOGIN"
        }

        $s.api.postSubmit('/api/magicline/signedFormRGhlove', formData,
            function (response) {
                if (response.status === "OK") {
                    if (response.result === "SUCCESS") {
                        // 		DN		: "cn=850테스트유효001,ou=people,ou=상호연동테스트,o=Government of Korea,c=KR"
                        // 		message : ""
                        // 		result	: "SUCCESS"

                    } else {
                        // 		DN		: ""
                        // 		message : "서명 검증에 실패 하였습니다."
                        // 		result	: "FALI"
                        $s.alert(response.message);
                    }
                }
            }, function (error) {
                $s.alert(error.response.data.description);
            });

    } else {
        alert("결과값 수신에 실패하였습니다.");
        return;
    }
}

</script>
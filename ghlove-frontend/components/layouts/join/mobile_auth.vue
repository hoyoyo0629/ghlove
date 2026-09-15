<template>
    <form action="/" id="join" name="join">
        <fieldset>
            <h3 class="sr-only">본인 인증</h3>
            <!-- 약관동의 -->
            <div class="accept-terms-area">
                <div class="accept-terms-item">
                    <p class="info-txt">
                        아래의 본인 인증 수단중 가능한 방식을 선택해서 인증을 진행하시기 바랍니다.<br>
                        휴대폰 인증은 본인 소유의 휴대폰만 해당 됩니다.<br>
                        국내 전자금융거래서비스를 가입한 해외 체류중인 국민(재외국민)도 금융인증서를 활용하여 고향사랑e음 이용이 가능합니다.
                    </p>
                </div>
                <!--수정: 230509-->
                <div class="auth_wrap">
                    <div class="authentication-area financ">
                        <div class="authentication-box">
                            <div class="auth_tit">
                                <h3>금융인증서</h3>
                                <p class="s-txt"><span>금융기관에 등록된</span> <span>금융인증서로 본인 인증 하기</span></p>
                                <p class="pointRed"><span>※ 해외 체류중인 국민</span> <span> (재외국민) 활용 가능</span></p>
                            </div>
                            <button id="finAuthBtn" class="formBtn financ" type="button" @click="doCertInit()" title="새 창 알림">인증하기</button>
                        </div>
                    </div>
                    <div class="authentication-area mobi">
                        <div class="authentication-box">
                            <div class="auth_tit">
                                <h3>휴대폰</h3>
                                <p class="s-txt">본인 명의로 등록된 휴대폰으로<br> 본인 인증 하기</p>
                            </div>
                            <button class="formBtn financ" type="button" @click="nextStep()" title="새 창 알림">인증하기</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 버튼그룹 -->
            <div class="btn-box">
                <button type="button" class="blueBtn cancellation" @click="goToMain()">취소<span><img
                            src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt=""></span>
                </button>
            </div>
        </fieldset>
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
        nextStep: function () {

            // 회원가입은 매번 새 CI 여야 한다 (같은 CI 면 서버가 기가입자로 튕겨냄).
            if ($s.config.isSkipExternalAuth) {
                $s.api.skipExternalAuth("휴대폰 인증", { fresh: true });
                return;
            }

            $s.api.mobileAuth(function (response) {
                $s.log(response);

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
        doCertInit: function () {
            if ($s.config.isSkipExternalAuth) {
                $s.api.skipExternalAuth("금융인증서", { fresh: true });
                return;
            }

            fCert.initSign('join');
        },
        mobileAuthResponse: function (response) {
            $s.log('mobile_auth.vue');
            $s.log(response);
        }
    }, mounted: function () {
        this.$nextTick(function () {
        });
    }
}
</script>
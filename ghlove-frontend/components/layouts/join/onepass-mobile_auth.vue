<template>
      <form action="/" id="join" name="join">
            <fieldset>
                  <!-- 약관동의 -->
                  <div class="accept-terms-area">
                        <div class="accept-terms-item">
                              <p class="info-txt">
                                    디지털원패스를 이용한 고향사랑e음 로그인 시 회원 연동을 위해 본인인증이 필요합니다.<br>
                                    아래 버튼을 눌러 핸드폰 인증 후 이용해 주시기 바랍니다.
                              </p>
                        </div>
                        <!-- <div class="authentication-area">
                              <div class="authentication-box">
                                    <h3>휴대폰</h3>
                                    <p class="s-txt">본인 명의로 등록된 휴대폰으로<br>
                                          본인 인증 하기</p>
                                    <button class="blueBtn authenBtn" type="button" @click="nextStep()">인증하기</button>
                              </div>
                        </div> -->
                        <!-- 수정 : 230410 -->
                        <div class="auth_wrap">
                              <div class="authentication-area financ">
                                    <div class="authentication-box">
                                          <div class="auth_tit">
                                                <h3><span>금융</span><span>인증서</span></h3>
                                          </div>
                                          <button class="formBtn financ" type="button" @click="doCertInit()">인증하기</button>
                                    </div>
                              </div>
                              <div class="authentication-area mobi">
                                    <div class="authentication-box">
                                          <div class="auth_tit">
                                                <h3>휴대폰</h3>
                                                <p class="s-txt">본인 명의로 등록된 휴대폰으로<br>
                                                      본인 인증 하기</p>
                                          </div>
                                          <button class="formBtn financ" type="button" @click="nextStep()">인증하기</button>
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

                  $s.api.getSubmit('/api/auth/mobile-auth', '', function (response) {
                        $s.log(response);

                        var PCC_window = window.open('', 'PCCV3Window', 'width=400, height=630, resizable=1, scrollbars=no, status=0, titlebar=0, toolbar=0, left=300, top=200');
                        if (PCC_window == null) {
                              alert(" ※ 윈도우 XP SP2 또는 인터넷 익스플로러 7 사용자일 경우에는 \n    화면 상단에 있는 팝업 차단 알림줄을 클릭하여 팝업을 허용해 주시기 바랍니다. \n\n※ MSN,야후,구글 팝업 차단 툴바가 설치된 경우 팝업허용을 해주시기 바랍니다.");
                        }

                        var form = document.createElement("form");
                        form.setAttribute('target', 'PCCV3Window');
                        // form.setAttribute("charset", "UTF-8");
                        form.setAttribute("method", "Post");
                        form.setAttribute("action", "https://pcc.siren24.com/pcc_V3/jsp/pcc_V3_j10_v2.jsp");

                        var hiddenField = document.createElement("input");
                        hiddenField.setAttribute("type", "hidden");
                        hiddenField.setAttribute("name", "reqInfo");
                        hiddenField.setAttribute("value", response.reqInfo);
                        form.appendChild(hiddenField);

                        hiddenField = document.createElement("input");
                        hiddenField.setAttribute("type", "hidden");
                        hiddenField.setAttribute("name", "retUrl");
                        hiddenField.setAttribute("value", response.retUrl);
                        form.appendChild(hiddenField);

                        hiddenField = document.createElement("input");
                        hiddenField.setAttribute("type", "hidden");
                        hiddenField.setAttribute("name", "verSion");
                        hiddenField.setAttribute("value", response.verSion);
                        form.appendChild(hiddenField);

                        document.body.appendChild(form);
                        form.submit();

                  });

            },
            doCertInit: function (response) {
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
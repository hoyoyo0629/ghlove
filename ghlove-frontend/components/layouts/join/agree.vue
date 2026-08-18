<template>
    <form action="/" id="agreeForm" name="agreeForm" @submit.prevent>
        <fieldset>
            <h3 class="sr-only">약관 동의</h3>
            <!-- 약관동의 -->
            <div class="accept-terms-area step1">
                <div class="accept-terms-item all_check">
                    <div class="accept-terms_wrap">
                        <div class="accept-check step1">
                            <input type="checkbox" name="all_check" id="all_check" v-model="all_check"
                                @click="allCheck($event)">
                            <label for="all_check">전체약관 동의하기</label>
                        </div>
                    </div>
                    <div class="line nomargin">
                    </div>
                </div>
                <div class="accept-terms-item agree_check" v-if="Object.keys(policy).length > 0">
                    <div class="accept-terms_wrap">
                        <div class="accept-check">
                            <input type="checkbox" name="check" :id="policy.policyId" v-model="param.checkResult"
                                :value="policy.policyId" @change="selected($event, policy.policyId)">
                            <label :for="policy.policyId">{{ policy.title }} <strong>(필수)</strong></label>
                            <input class="disagreeInput" type="checkbox" id="disagreeUsePolicy" name="disagreeUsePolicy" @change="selectDisagree($event, policy.policyId)"
                            v-model="param.disagreeResult" :value="policy.policyId" style="margin:0 0 0 20px">
                             <label for="disagreeUsePolicy">비동의</label>
                        </div>
                        <a href="/policy/auth.html" target="_blank" title="새창 열림" class="moreView"><span class="sr-only">이용약관 </span>자세히 보기</a> <!-- 이용약관 -->
                    </div>
                    <div class="terms-box" tabindex="0">
                        <div class="terms-article" v-html="policy.content">
                        </div>
                    </div>
                </div>

                <!-- 추가 : 230503 -->
                <div class="accept-terms-item agree_check" v-if="Object.keys(collectionAgree).length > 0">
                    <div class="accept-terms_wrap">
                        <div class="accept-check">
                            <input type="checkbox" name="check" :id="collectionAgree.policyId" v-model="param.checkResult"
                                :value="collectionAgree.policyId" @change="selected($event, collectionAgree.policyId)">
                            <label :for="collectionAgree.policyId">{{collectionAgree.title}} <strong>(필수)</strong></label>
                        <input class="disagreeInput" type="checkbox" id="disagreeInfoPolicy" name="disagreeInfoPolicy"  @change="selectDisagree($event, collectionAgree.policyId)"
                        v-model="param.disagreeResult" :value="collectionAgree.policyId" style="margin:0 0 0 20px">
                             <label for="disagreeInfoPolicy">비동의</label>
                        </div>
                        <a href="/policy/privacy.html" target="_blank" title="새창 열림" class="moreView"><span class="sr-only">개인정보처리방침 </span>자세히 보기</a><!-- 개인정보 수집·이용 안내 -->
                    </div>
                    <div class="terms-box" tabindex="0">
                        <div class="terms-article" v-html="appendCdnDomain(unescapeHtml(collectionAgree.content))">
                        </div>
                    </div>
                </div>

	            <!-- 광고성 정보 수신 동의 영역 -->
	            <div class="accept-terms-item agree_check" v-if="Object.keys(adInfoAgree).length > 0"> <!-- 이용약관 동의에 대한 정보가 있을 경우만 출력되도록 -->
		            <div class="accept-terms_wrap" style="padding-bottom: 0px;">
		                <div class="accept-check">
		                    <input type="checkbox" name="check" :id="adInfoAgree.policyId" v-model="param.checkResult"
		                        :value="adInfoAgree.policyId" @change="selected($event, adInfoAgree.policyId)">
		                    <label :for="adInfoAgree.policyId">{{adInfoAgree.title}} <strong>(선택)</strong></label>
<!-- 		                    <input class="disagreeInput" type="checkbox" id="disagreeUsePolicy" name="disagreeUsePolicy" @change="selectDisagree($event, adInfoAgree.policyId)" -->
<!-- 		                    v-model="param.disagreeResult" :value="policy.policyId" style="margin:0 0 0 20px"> -->
<!-- 		                     <label for="disagreeUsePolicy">비동의</label> -->
		                </div>
<!-- 		                <a href="/policy/auth.html" target="_blank" title="새창 열림" class="moreView"><span class="sr-only">이용약관 </span>자세히 보기</a> 이용약관 -->
		            </div>
		            <div class="ad-info-agree">
		                <div class="agree-accept-check">
							<input type="checkbox" name="check" :id="adInfoAgree.sms.policyId" v-model="param.checkResult"
		                        :value="adInfoAgree.sms.policyId" @change="selected($event, adInfoAgree.sms.policyId)">
		                    <label :for="adInfoAgree.sms.policyId">{{adInfoAgree.sms.title}} <strong>(선택)</strong></label>
						</div>
						<div class="agree-accept-check">
		                    <input type="checkbox" name="check" :id="adInfoAgree.email.policyId" v-model="param.checkResult"
		                        :value="adInfoAgree.email.policyId" @change="selected($event, adInfoAgree.email.policyId)">
		                    <label :for="adInfoAgree.email.policyId">{{adInfoAgree.email.title}} <strong>(선택)</strong></label>
						</div>
						<div class="agree-accept-check">
		                    <input type="checkbox" name="check" :id="adInfoAgree.pbanc.policyId" v-model="param.checkResult"
		                        :value="adInfoAgree.pbanc.policyId" @change="selected($event, adInfoAgree.pbanc.policyId)">
		                    <label :for="adInfoAgree.pbanc.policyId">{{adInfoAgree.pbanc.title}} <strong>(선택)</strong></label>
						</div>
						<div class="agree-accept-check">
		                    <input type="checkbox" name="check" :id="adInfoAgree.kakao.policyId" v-model="param.checkResult"
		                        :value="adInfoAgree.kakao.policyId" @change="selected($event, adInfoAgree.kakao.policyId)">
		                    <label :for="adInfoAgree.kakao.policyId">{{adInfoAgree.kakao.title}} <strong>(선택)</strong></label>
						</div>
					</div>
		            <div class="terms-box" tabindex="0">
		                <div class="terms-article" v-html="adInfoAgree.content">
		                </div>
		            </div>
	            </div>
	        	<!-- 광고성 정보 수신 동의 영역 -->

            </div>


            <!-- 버튼그룹 -->
            <div class="btn-box many">
                <button type="button" class="blueBtn cancellation" @click="goToMain()">
                    취소<span>
                        <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                    </span>
                </button>
                <button type="button" class="blueBtn u-confirm" @click="nextStep()">
                    다음<span>
                        <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                    </span>
                </button>

                <button type="button" class="btn-simple btn-simple--kakao"
                        @click.prevent="submitKakao" title="카카오톡 인증 로그인" id="kakaoLoginBtn">
                    <div class="btn-simple__logo">
                        <img src="/static/images/kakao/kakaotalk_symbol_screen.png" class="btn-simple__img" alt="" aria-hidden="true" />
                    </div>
                    <span class="btn-simple__txt">카카오톡 인증</span>
                </button>

                <button type="button" class="btn-simple btn-simple--naver"
                        @click.prevent="submitNaver" title="네이버 인증 로그인" id="naverLoginBtn">
                    <div class="btn-simple__logo">
                        <img src="/static/images/new/naver_logo_2.png" style="height: 44px; border-radius: 8px;" alt="" aria-hidden="true" />
                    </div>
                    <span class="btn-simple__txt">네이버 인증</span>
                </button>
            </div>
        </fieldset>
    </form>
</template>

<script>
module.exports = {
    data: function () {
        return {
            param: {
                checkResult: [],
                disagreeResult: [],
                agreeCheck: false
            },
            all_check: false,
            policy: [],
            collectionAgree: [],
            adInfoAgree: []
        }
    }, methods: {
        goToMain: function () {
            location.href = "/"
            // 메인화면 URL 지정하기
        },
        allCheck: function (event) {
            var checked = event.target.checked;
            if (!checked) {
                this.param.checkResult = [];
                this.param.disagreeResult = [];
            } else {
                this.param.checkResult = [];
                this.param.disagreeResult = [];
                if (Object.keys(this.policy).length > 0) {
                    this.param.checkResult.push(this.policy.policyId);
                }
                if (Object.keys(this.collectionAgree).length > 0) {
                    this.param.checkResult.push(this.collectionAgree.policyId);
                }
                if (Object.keys(this.adInfoAgree).length > 0) {
                    this.param.checkResult.push(this.adInfoAgree.policyId);
                    this.param.checkResult.push(this.adInfoAgree.sms.policyId);
                    this.param.checkResult.push(this.adInfoAgree.email.policyId);
                    this.param.checkResult.push(this.adInfoAgree.pbanc.policyId);
                    this.param.checkResult.push(this.adInfoAgree.kakao.policyId);
                }

            }
        },
        nextStep: function () {
            if ((Object.keys(this.policy).length > 0) && (this.param.checkResult.indexOf(this.policy.policyId) == -1)) {
                $s.alert("이용약관에 동의해주세요", this.policy.policyId)
                return false;
            }
            if ((Object.keys(this.collectionAgree).length > 0) && (this.param.checkResult.indexOf(this.collectionAgree.policyId) == -1)) {
                $s.alert("개인정보 수집·이용에 동의해주세요", this.collectionAgree.policyId);
                return false;
            }
            this.param.agreeCheck = true;
            this.$emit("agree", this.param);
        },
        selected: function (e, value) {

            var totalLen = 0;

            if (Object.keys(this.policy).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.collectionAgree).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.adInfoAgree).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.adInfoAgree.sms).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.adInfoAgree.email).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.adInfoAgree.pbanc).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.adInfoAgree.kakao).length > 0) {
                totalLen += 1
            }

            if (this.param.checkResult.length != totalLen) {
                this.all_check = false;
            } else {
                this.all_check = true;
            }

            this.param.disagreeResult = this.param.disagreeResult.filter(item => item !== value);

            // 광고성 정보 수신 동의 클릭 이벤트
            const subAd = [this.adInfoAgree.sms.policyId,this.adInfoAgree.email.policyId,this.adInfoAgree.pbanc.policyId,this.adInfoAgree.kakao.policyId];
            const checkedSubAd = this.param.checkResult.filter(item => subAd.includes(item));

            if(e.target.id === this.adInfoAgree.policyId){
				if(!e.target.checked) {
					const subAdSet = new Set(subAd);
					this.param.checkResult = this.param.checkResult.filter(item => !subAdSet.has(item))
				}else{
					subAd.filter(item => !this.param.checkResult.includes(item)).map(item => this.param.checkResult.push(item));
				}
            }
        	// 광고성 정부 수신 동의 하위 항목 클릭 이벤트
            if(subAd.includes(e.target.id)){
            	if(checkedSubAd.length > 0){
            		if(!this.param.checkResult.includes(this.adInfoAgree.policyId)) this.param.checkResult.push(this.adInfoAgree.policyId);
    			}else{
    				this.param.checkResult = this.param.checkResult.filter(item => item !== this.adInfoAgree.policyId);
    			};
            }

        },
        selectDisagree : function(e, value){
            this.all_check=false;
            this.param.checkResult = this.param.checkResult.filter(item => item !== value);
        },
        getPolicyInfo: function () {
            var self = this;
            $s.api.postSubmitNoAuth('/api/join/getPolicyInfo', {}, function (response) {
                if (response.policy != null) {
                    self.policy = response.policy;
                }
                if (response.collectionAgree != null) {
                    self.collectionAgree = response.collectionAgree;
                }

                /* 광고성 정보 수신 동의 */
                self.adInfoAgree.title = "알림서비스 수신 동의";
                self.adInfoAgree.policyId = "adInfoAgree";
                self.adInfoAgree.content = "<p><span style=\"font-family: 나눔고딕; letter-spacing: 0pt; font-weight: bold; font-size: 14pt;\">■</span><span lang=\"EN-US\" style=\"font-weight: bold; font-size: 11pt;\">&nbsp;<font face=\"나눔고딕\">알림서비스 수신 동의</font></span>&nbsp;</p><p class=\"0\" style=\"line-height:146%;margin-left:18.9pt;text-indent:-18.9pt;margin-top:2.0pt;text-autospace:none;\"></p><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">&nbsp;고향사랑e음에서 제공하는 유익한 홍보성 정보를 SMS나 이메일 또는 카카오톡으로 받아 보실 수 있습니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">단, 주요 정책과 관련된 내용은 수신 동의 여부와 관계없이 발송됩니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">선택 약관에 동의하지 않으셔도 회원가입은 가능하며, 회원가입 후 <a href=\"/users/modify.html\" target=\"_blank\" title=\"새창 열림\" rel=\"noopener\" style=\"text-decoration: underline; color: blue; \"><strong>마이페이지 &gt; 회원정보수정</strong></a>에서 언제든지 수신여부를 변경하실 수 있습니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\"></span></font></div><p></p><div><span style=\"font-family: 나눔고딕; letter-spacing: 0pt; font-weight: bold; font-size: 14pt;\"><br></span></div><div><span style=\"font-family: 나눔고딕; letter-spacing: 0pt; font-weight: bold; font-size: 14pt;\"><span style=\"letter-spacing: 0pt; font-family: 나눔고딕; font-weight: bold; font-size: 14pt;\">■</span><span lang=\"EN-US\" style=\"font-weight: bold; font-size: 11pt;\"><span style=\"font-size: 14pt;\">&nbsp;</span>국민비서 알림서비스</span><br></span></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">&nbsp;알림서비스는 국민비서 회원에게 제공되니 회원가입 해 주시기 바랍니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\"><a href=\"https://www.ips.go.kr/pot/forwardMain.do\" target=\"_blank\" title=\"새창 열림\" rel=\"noopener\" style=\"text-decoration: underline; color: blue; \"><strong>국민비서 가입하기</strong></a> 국민비서 홈 &gt; 알림설정(기타-고향사랑e음 안내 알림 선택)</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">※ 알림서비스 설정 후 다음날부터 되는 점 참고 바랍니다.</span></font></div>";

                self.adInfoAgree.sms = {};
                self.adInfoAgree.sms.title = "국민비서";
                self.adInfoAgree.sms.policyId = "receiveSms";
                self.adInfoAgree.sms.content = "국민비서";

                self.adInfoAgree.email = {};
                self.adInfoAgree.email.title = "Email";
                self.adInfoAgree.email.policyId = "receiveEmail";
                self.adInfoAgree.email.content = "Email";

                self.adInfoAgree.pbanc = {};
                self.adInfoAgree.pbanc.title = "SMS";
                self.adInfoAgree.pbanc.policyId = "receivePbanc";
                self.adInfoAgree.pbanc.content = "SMS";

                self.adInfoAgree.kakao = {};
                self.adInfoAgree.kakao.title = "알림톡";
                self.adInfoAgree.kakao.policyId = "receiveKakao";
                self.adInfoAgree.kakao.content = "알림톡";

            });
        },


        submitKakao: function () {
            if (this.isKakaoInit()) {
            	if ((Object.keys(this.policy).length > 0) && (this.param.checkResult.indexOf(this.policy.policyId) == -1)) {
	                $s.alert("이용약관에 동의해주세요", this.policy.policyId);
	                return false;
	            }
	            if ((Object.keys(this.collectionAgree).length > 0) && (this.param.checkResult.indexOf(this.collectionAgree.policyId) == -1)) {
	                $s.alert("개인정보 수집·이용에 동의해주세요", this.collectionAgree.policyId);
	                return false;
	            }

                let target = vm.getQueryStr("target");
                if (target) {
                    target = "?target=" + target;
                } else {
                    target = '/';
                }

                localStorage.setItem('kakao-login-target', target);

                let signData = this.getRandomKey(10);

				setTimeout(
                    function () {
                	    Kakao.Auth.authorizeForCert({
                    	    redirectUri : KAKAO_REDIRECT_URL2,
                    	    settleId : KAKAO_SETTLE_ID,
                    	    signData : signData,
				  			identifyItems: 'ci,name,birthday,phone_number,gender',
                	    });
                    }
                    , 500
                );

            } else {
                $s.alert("새로고침 후 다시 시도해주세요.", "kakaoLoginBtn");
            }
            return false;
        },
        submitNaver: function () {
        	if ((Object.keys(this.policy).length > 0) && (this.param.checkResult.indexOf(this.policy.policyId) == -1)) {
                $s.alert("이용약관에 동의해주세요", this.policy.policyId);
                return false;
            }
            if ((Object.keys(this.collectionAgree).length > 0) && (this.param.checkResult.indexOf(this.collectionAgree.policyId) == -1)) {
                $s.alert("개인정보 수집·이용에 동의해주세요", this.collectionAgree.policyId);
                return false;
            }

            let param = { type: "JOIN" };
            vm.showLoading(true);
            $s.api.postSubmitNoAuth(
                "/api/kakao-link/naver-login-page",
                param,
                function (response) {
                    vm.showLoading(false);
                    let loginUrl = response.loginUrl;
                    if (loginUrl) {
                        $s.redirect(loginUrl);
                    } else {
                        $s.alert("잠시 후 다시 시도해주세요.", "naverLoginBtn");
                    }
                },
                function (error) {
                    vm.showLoading(false);
                    $s.alert("오류가 발생했습니다.", "naverLoginBtn");
                    history.replaceState({}, null, location.pathname);
                }
            );
            return false;
        },
        getQueryStr: function (paramKey) {
            return new URLSearchParams(location.search).get(paramKey);
            //return location.search;
        },
        isKakaoInit: function () {
            if (Kakao.isInitialized()) {
                return true;
            } else {
                $s.alert('카카오 기능 불러오기에 실패했습니다.\n새로고침 후 다시 진행해주세요.\n지속 문제 발생시 고객센터로 문의바랍니다.');
                return false;
            }
        },
        getRandomKey: function (length) {
            const characters ='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
            let result = '';
            const charactersLength = characters.length;
            for (let i = 0; i < length; i++) {
              result += characters.charAt(Math.floor(Math.random() * charactersLength));
            }
            localStorage.setItem('kakao-sign-key', result);
            return result;
        },



    }, mounted: function () {
        this.$nextTick(function () {
            //Saleson.init();
            this.getPolicyInfo();
        });
    }
}
</script>
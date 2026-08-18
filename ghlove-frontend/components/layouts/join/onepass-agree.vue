<template>
    <form action="/" id="agreeForm" name="agreeForm" @submit.prevent>
        <fieldset>
            <!-- 약관동의 -->
            <div class="accept-terms-area step1">
                <p class="info-txt">디지털원패스와 고향사랑 e음 회원정보를 연동합니다.<br>
                    아래 약관에 동의해주시기 바랍니다.</p>
                <div class="accept-terms-item all_check">
                    <div class="accept-terms_wrap">
                        <div class="accept-check">
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
                                :value="policy.policyId" @change="selected($event)">
                            <label :for="policy.policyId">{{ policy.title }} <strong>(필수)</strong></label>
                        </div>
                        <a href="/policy/auth.html" target="_blank" title="새창 열림" class="moreView">자세히 보기</a> <!-- 이용약관 -->
                    </div>
                    <div class="terms-box">
                        <div class="terms-article" v-html="policy.content">
                        </div>
                    </div>
                </div>

                <!-- 추가 : 230503 -->
                <div class="accept-terms-item agree_check" v-if="Object.keys(collectionAgree).length > 0">
                    <div class="accept-terms_wrap">
                        <div class="accept-check">
                            <input type="checkbox" name="check" :id="collectionAgree.policyId" v-model="param.checkResult"
                                :value="collectionAgree.policyId" @change="selected($event)">
                                <label :for="collectionAgree.policyId">{{collectionAgree.title}} <strong>(필수)</strong></label>
                        </div>
                        <a href="/policy/privacy.html" target="_blank" title="새창 열림" class="moreView">자세히 보기</a><!-- 개인정보 수집·이용 안내 -->
                    </div>
                    <div class="terms-box">
                        <div class="terms-article" v-html="appendCdnDomain(unescapeHtml(collectionAgree.content))">
                        </div>
                    </div>
                </div>
                <!-- //추가 : 230503 -->

                <div class="accept-terms-item agree_check" v-if="Object.keys(otherAgree).length > 0">
                    <div class="accept-terms_wrap">
                        <div class="accept-check">
                            <input type="checkbox" name="check" :id="otherAgree.policyId" v-model="param.checkResult"
                                :value="otherAgree.policyId" @change="selected($event)"><label :for="otherAgree.policyId">{{
                                    otherAgree.title }}
                                <strong>(필수)</strong></label>
                        </div>
                        <a href="/policy/privacy.html" target="_blank" title="새창 열림" class="moreView">자세히 보기</a><!-- 개인정보 제3자 이용동의 -->
                    </div>
                    <div class="terms-box">
                        <div class="terms-article" v-html="appendCdnDomain(unescapeHtml(otherAgree.content))">
                        </div>
                    </div>
                </div>

            </div>
            <!-- 버튼그룹 -->
            <div class="btn-box many">
                <button type="button" class="blueBtn cancellation" @click="goToMain()">취소<span><img
                            src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt=""></span>
                </button>
                <button type="button" class="blueBtn u-confirm" @click="nextStep()">다음<span><img
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
            param: {
                checkResult: [],
                agreeCheck: false
            },
            all_check: false,
            policy: [],
            otherAgree: [],
            collectionAgree: []
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
            } else {
                this.param.checkResult = [];
                if (Object.keys(this.policy).length > 0) {
                    this.param.checkResult.push(this.policy.policyId);
                }
                if (Object.keys(this.collectionAgree).length > 0) {
                    this.param.checkResult.push(this.collectionAgree.policyId);
                }
                if (Object.keys(this.otherAgree).length > 0) {
                    this.param.checkResult.push(this.otherAgree.policyId);
                }
            }
        },
        nextStep: function () {
            if ((Object.keys(this.policy).length > 0) && (this.param.checkResult.indexOf(this.policy.policyId) == -1)) {
                $s.alert("이용약관에 동의해주세요");
                return false;
            }
            if ((Object.keys(this.collectionAgree).length > 0) && (this.param.checkResult.indexOf(this.collectionAgree.policyId) == -1)) {
                $s.alert("개인정보 수집·이용에 동의해주세요");
                return false;
            }
            if ((Object.keys(this.otherAgree).length > 0) && (this.param.checkResult.indexOf(this.otherAgree.policyId) == -1)) {
                $s.alert("개인정보 수집 제3자 이용에 동의해 주세요");
                return false;
            }
            this.param.agreeCheck = true;
            this.$emit("agree", this.param);
        },
        selected: function (e) {
            var totalLen = 0;

            if (Object.keys(this.policy).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.collectionAgree).length > 0) {
                totalLen += 1
            }

            if (Object.keys(this.otherAgree).length > 0) {
                totalLen += 1
            }

            if (this.param.checkResult.length != totalLen) {
                this.all_check = false;
            } else {
                this.all_check = true;
            }
        },
        getPolicyInfo: function () {
            var self = this;
            $s.api.postSubmitNoAuth('/api/join/getPolicyInfo', {}, function (response) {
                if (response.policy != null) {
                    self.policy = response.policy;
                }
                if (response.otherAgree != null) {
                    self.otherAgree = response.otherAgree;
                }
                if (response.collectionAgree != null) {
                    self.collectionAgree = response.collectionAgree;
                }
            });
        }
    }, mounted: function () {
        this.$nextTick(function () {
            //Saleson.init();
            this.getPolicyInfo();
        });
    }
}
</script>
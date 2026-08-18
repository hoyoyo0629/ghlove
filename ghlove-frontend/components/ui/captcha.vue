<template>
   <div class="captcha">
        <div class="captcha-content">
            <img id="captchaImg" ref="catpchaImg" alt="자동 등록 방지 숫자" />
            <div style="display : none;" id="captchaAudio"></div>
            <div>
                <button class="blueBtn cancellation captchaFrm" @click="captchaAudio()">음성듣기</button>
                <button class="blueBtn cancellation captchaFrm" @click="initImage()">새로고침</button>
            </div>
        </div>
        <div class="captcha-submit">
            <input type="text" v-model="captchaTxt" id="captchaTxt" class="captchaFrm" title="자동 등록 방지 숫자 입력" />
        </div>
    </div>
</template>
<script>
    module.exports = {
        data: function () {
            return {
                captchaTxt : '',
                answer : '',
                isSucc : false
            }
        },
        methods: {
            captchaAudio: function() {

                if (this.answer == '') {
                    $s.alert("캡챠 이미지 호출에 실패하였습니다.\n관리자에게 문의 부탁드립니다.");
                    return false;
                }

                var url =  `${$s.config.apiDomain}/api/common/captcha/audio?answer=${this.answer}`;
                var uAgent = navigator.userAgent;
                
                if (uAgent.indexOf("Trident") > -1 || uAgent.indexOf('MSIE') > -1) {
                    this.winPlayer(url);
                } else if (document.createElement('audio').canPlayType) {
                    
                    try {
                        new Audio(url).play();
                    } catch (e) {
                        this.winPlayer(url);
                    }
                    
                    
                } else {
                    window.open(url,'','width=1,height=1');
                }
            },
            winPlayer: function (url) {
                $("#captchaAudio").html('<bgsoun src="' + url + '">');
            },
            initImage: function () {
                var that = this;
                $.ajax({
                    url : `${$s.config.apiDomain}/api/common/captcha/img`,
                    type: 'get',
                    dataType: 'json',
                    cache : false,
                    success: function (response) {
                        if (response.data && response.data.answer && response.data.buff) {
                            that.$refs.catpchaImg.src = 'data:image/jpeg;base64,' + response.data.buff;
                            that.$data.answer = response.data.answer;
                        } else {
                            $s.alert("캡챠 이미지 호출에 실패하였습니다.\n관리자에게 문의 부탁드립니다.");
                        }
                    }
                });
            },
            isCaptcha: function () {
                return this.captchaTxt == this.answer;
            }
        },
        mounted: function(){
            this.$nextTick(function () {
                //Saleson.init();

                this.initImage();
            });
        }
    };
</script>
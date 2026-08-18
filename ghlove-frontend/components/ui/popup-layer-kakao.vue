<template>
    <div id="popupArea">
        <div v-for="data in result" :id="'popup_' + data.popupId" class="p_admin_win" :style="data.popupStyle">
            <div class="p_body">
                <div v-if="data.popupImage !== '' && data.popupImage !== null
                    && data.imageLink !== '' && data.imageLink !== null">
                    <a :href="data.imageLink" target="_blank" title="새창 열림">
                        <img :src="itemImage(data.popupImageSrc)" border="0" :alt="altText(data.popupId) || '고향사랑 팝업'"/>
                    </a>
                </div>
                <div v-else-if="data.popupImage !== '' && data.popupImage !== null" style="padding-top:5px;">
                    <img :src="itemImage(data.popupImageSrc)" border="0" :alt="altText(data.popupId) || '고향사랑 팝업'"/>
                </div>

				<!-- CONTENT -->
				<div class="popup-terms-area">
					<div class="popup-terms-comment">고향사랑e음 카카오톡채널 알림 서비스가 추가되었습니다. 알림서비스 동의를 해주시면 고향사랑e음의 유익한 정보를 받아 보실 수 있습니다.</div>
					<!-- 광고성 정보 수신 동의 영역 -->
<!-- 		            <div class="popup-terms-item agree_check" v-if="Object.keys(adInfoAgree).length > 0"> 이용약관 동의에 대한 정보가 있을 경우만 출력되도록 -->
			            <div class="popup-terms_wrap" style="padding-bottom: 0px;">
			                <div class="popup-check">
			                    <input type="checkbox" name="check" :id="adInfoAgree.policyId" v-model="checkResult"
			                        :value="adInfoAgree.policyId" @change="selected($event, adInfoAgree.policyId)">
			                    <label :for="adInfoAgree.policyId">{{adInfoAgree.title}} <strong>(선택)</strong></label>
			                </div>
			            </div>
			            <div class="popup-ad-info-agree">
			                <div class="popup-agree-accept-check">
								<input type="checkbox" name="check" :id="adInfoAgree.sms.policyId" v-model="checkResult"
			                        :value="adInfoAgree.sms.policyId" @change="selected($event, adInfoAgree.sms.policyId)">
			                    <label :for="adInfoAgree.sms.policyId">{{adInfoAgree.sms.title}} <strong>(선택)</strong></label>
							</div>
							<div class="popup-agree-accept-check">
			                    <input type="checkbox" name="check" :id="adInfoAgree.email.policyId" v-model="checkResult"
			                        :value="adInfoAgree.email.policyId" @change="selected($event, adInfoAgree.email.policyId)">
			                    <label :for="adInfoAgree.email.policyId">{{adInfoAgree.email.title}} <strong>(선택)</strong></label>
							</div>
							<div class="popup-agree-accept-check">
			                    <input type="checkbox" name="check" :id="adInfoAgree.pbanc.policyId" v-model="checkResult"
			                        :value="adInfoAgree.pbanc.policyId" @change="selected($event, adInfoAgree.pbanc.policyId)">
			                    <label :for="adInfoAgree.pbanc.policyId">{{adInfoAgree.pbanc.title}} <strong>(선택)</strong></label>
							</div>
							<div class="popup-agree-accept-check">
			                    <input type="checkbox" name="check" :id="adInfoAgree.kakao.policyId" v-model="checkResult"
			                        :value="adInfoAgree.kakao.policyId" @change="selected($event, adInfoAgree.kakao.policyId)">
			                    <label :for="adInfoAgree.kakao.policyId">{{adInfoAgree.kakao.title}} <strong>(선택)</strong></label>
							</div>
						</div>
<!-- 			            <div class="terms-box" tabindex="0"> -->
<!-- 			                <div class="terms-article" v-html="adInfoAgree.content"> -->
<!-- 			                </div> -->
<!-- 			            </div> -->
                        <div class="popup-terms-comment" style="padding-top: 10px">※ 동의를 원치 않으시다면 체크 해제 후 저장버튼을 눌러주십시오.</div>
		            </div>
		        	<!-- 광고성 정보 수신 동의 영역 -->
				</div>
            <div class="check_area">
                <button class="submit-btn" @click="popupSubmit(data.popupId)" tabindex="1" >
                	저장
                </button>
            </div>
           
        </div>
    </div>
</template>
<style scoped>
	.popup-terms-area{
	    background-color: #fff;
	    padding: 20px 20px 0px;
	    max-width: 900px;
	    text-align : left;
	}
	.popup-terms-area .popup-terms-comment{
		font: var(--pc-font-medium);
	    color: var(--color-black);
    	font-weight: 500;
	}
	.popup-terms-area .popup-terms_wrap {
	    display: flex;
	    justify-content: space-between;
	    align-items: flex-end;
	    padding: 10px 0 20px;
	}
	.popup-terms-area .popup-check {
	    display: flex;
	    align-items: center;
	}
	.popup-terms-area .popup-check input[type='checkbox'] {
	    transform: translateY(1px);
	}
	.popup-terms-area .popup-check label {
	    padding-left: calc(var(--margin-padding-8)*1);
	    font-size: 22px;
	    letter-spacing: -1px;
	    font-weight: 400;
	}

	/* 회원가입 광고성 정보 수신 동의 스타일 추가 */
	.popup-ad-info-agree{
	    display: flex;
	    flex-direction: column;
	}
	.popup-ad-info-agree .popup-agree-accept-check {
		padding : 5px 10px;
	}
	/* 약관내용 */
	.popup-terms-area .terms-box {
		margin-top : 10px;
	    background-color: var(--gray5);
	    border: 1px solid var(--gray4);
	    border-radius: 5px;
	    color: var(--gray3);
	    padding: calc(var(--margin-padding-8)*2) calc(var(--margin-padding-8)*1);
	    height: 250px;
	    line-height: 2em;
	    letter-spacing: -1px;
	    overflow-x: hidden;
	    overflow-y: scroll;
	    font-size: 14px;
	    white-space: pre-wrap;
	}

	.check_area .submit-btn {
	  padding: 6px 20px 6px 21px;
	  border-radius: 3px;
	  box-sizing: content-box;
	  background-size: 18px auto;
	  background-color: rgba(255, 255, 255, 0.7);
	  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.16);
	}
</style>

<script>
module.exports = {
    data: function () {
        return {
        	result: [],
            param: {
                locgovCode: "",
                upperLocgovNm: "",
                locgovNm: "",
                userName: "",
                loginId: "",
                phoneNumber: "",
                birthday: "",
                email: "",
                post: "",
                address: "",
                addressDetail: "",
                receiveEmail: "",
                receiveSms: "",
                receivePbanc: "",
                userKeyYN: "",
                mberCiYN: "",
                loginPathCode: "",
                receiveKakao: ""
            },
            popup_content : {
                popupId: 999,
                popupClose: "3",
                popupType: "2",
                popupStyle: "position:absolute;left:0px;top:0px;z-index:9999;width:400px;height:500px;",
                subject: "개발서버 알림서비스 수신동의 팝업",
                content: "asdas",
                startDate: "20260201",
                startTime: "00",
                endDate: "20260207",
                endTime: "00",
                width: 400,
                height: 500,
                topPosition: 0,
                leftPosition: 0,
                popupImage: "KakaoTalk_20260224.png",
                imageLink: "",
                backgroundColor: "",
                popupImageFile: null,
                popupImageSrc: "/static/images/popup/KakaoTalk_20260224.png",
                handleStyle: "width:420px;height:15px;cursor:move;background:;"
            },
            all_check: false,
            policy: [],
            collectionAgree: [],
            adInfoAgree: [],
            checkResult: [],
        }
    },
    methods: {
    	popupSubmit: function (popupId) {
    		var self = this;

    		self.param.receiveEmail = self.checkResult.includes("receiveEmail")? "0" : "1";
    		self.param.receiveSms = self.checkResult.includes("receiveSms")? "0" : "1";
    		self.param.receivePbanc = self.checkResult.includes("receivePbanc")? "0" : "1";
    		self.param.receiveKakao = self.checkResult.includes("receiveKakao")? "0" : "1";

    		$s.api.postSubmit("/api/user/modifyReceive", self.param, function (response) {
               	$s.api.popup.popupClose('2', popupId);
                $s.alert("수정되었습니다", function () {
                    $s.closeAlert();
                  });
    		}, function (error) {
                $s.alert(error.response.data.message);
            });
        },
        getPopups: function () {
        	var self = this;
            $s.api.postSubmit("/api/user/getUserInfo", "", function (response) {

            	if(!response.userInfo.receiveKakao){
            		/* 광고성 정보 수신 동의 */
                    self.adInfoAgree.title = "알림서비스 수신 동의";
                    self.adInfoAgree.policyId = "adInfoAgree";
                    self.adInfoAgree.content = "<p><span style=\"font-family: 나눔고딕; letter-spacing: 0pt; font-weight: bold; font-size: 14pt;\">■</span><span lang=\"EN-US\" style=\"font-weight: bold; font-size: 11pt;\">&nbsp;<font face=\"나눔고딕\">알림서비스 수신 동의</font></span>&nbsp;</p><p class=\"0\" style=\"line-height:146%;margin-left:18.9pt;text-indent:-18.9pt;margin-top:2.0pt;text-autospace:none;\"></p><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">&nbsp;고향사랑e음에서 제공하는 유익한 정보를 SMS나 이메일 또는 카카오톡으로 받아 보실 수 있습니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">단, 주요 정책과 관련된 내용은 수신 동의 여부와 관계없이 발송됩니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">선택 약관에 동의하지 않으셔도 회원가입은 가능하며, 회원가입 후 <a href=\"/users/modify.html\" target=\"_blank\" title=\"새창 열림\" rel=\"noopener\" style=\"text-decoration: underline; color: blue; \"><strong>마이페이지 &gt; 회원정보수정</strong></a>에서 언제든지 수신여부를 변경하실 수 있습니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\"></span></font></div><p></p><div><span style=\"font-family: 나눔고딕; letter-spacing: 0pt; font-weight: bold; font-size: 14pt;\"><br></span></div><div><span style=\"font-family: 나눔고딕; letter-spacing: 0pt; font-weight: bold; font-size: 14pt;\"><span style=\"letter-spacing: 0pt; font-family: 나눔고딕; font-weight: bold; font-size: 14pt;\">■</span><span lang=\"EN-US\" style=\"font-weight: bold; font-size: 11pt;\"><span style=\"font-size: 14pt;\">&nbsp;</span>국민비서 알림서비스</span><br></span></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">&nbsp;알림서비스는 국민비서 회원에게 제공되니 회원가입 해 주시기 바랍니다.</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\"><a href=\"https://www.ips.go.kr/pot/forwardMain.do\" target=\"_blank\" title=\"새창 열림\" rel=\"noopener\" style=\"text-decoration: underline; color: blue; \"><strong>국민비서 가입하기</strong></a> 국민비서 홈 &gt; 알림설정(기타-고향사랑e음 안내 알림 선택)</span></font></div><div><font face=\"나눔고딕\"><span style=\"font-size: 13.3333px;\">※ 알림서비스 설정 후 다음날부터 되는 점 참고 바랍니다.</span></font></div>";

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
                    self.adInfoAgree.kakao.title = "카카오톡(채널/알림톡/브랜드메시지)";
                    self.adInfoAgree.kakao.policyId = "receiveKakao";
                    self.adInfoAgree.kakao.content = "카카오톡(채널/알림톡/브랜드메시지)";

                	self.makePopup(self.popup_content);

            		/* 회원정보 초기 셋팅 */

           			self.param.locgovCode = response.userInfo.locgovCode;
	              	self.param.upperLocgovNm = response.userInfo.upperLocgovNm;
		            self.param.locgovNm = response.userInfo.locgovNm;
		            self.param.userName = response.userInfo.userName;
		            self.param.loginId = response.userInfo.loginId;
		            self.param.phoneNumber = response.userInfo.phoneNumber;
		            self.param.email = response.userInfo.email;
		            self.param.birthday = response.userInfo.birthday;
		            self.param.post = response.userInfo.post;
		            self.param.address = response.userInfo.address;
		            self.param.addressDetail = response.userInfo.addressDetail;

            		self.param.receiveEmail = response.userInfo.receiveEmail ? response.userInfo.receiveEmail : "1";
            		self.param.receiveSms = response.userInfo.receiveSms ? response.userInfo.receiveSms : "1";
            		self.param.receivePbanc = response.userInfo.receivePbanc ? response.userInfo.receivePbanc : "1";
            		self.param.receiveKakao = response.userInfo.receiveKakao ? response.userInfo.receiveKakao : "1";

					self.param.userKeyYN = response.userInfo.userKeyYN;
					self.param.mberCiYN = response.userInfo.mberCiYN;
					if (response.userInfo.loginPathCode != null) {
						self.param.loginPathCode = response.userInfo.loginPathCode;
					}


                    if(self.param.receiveEmail == "0") self.checkResult.push(self.adInfoAgree.email.policyId);
                    if(self.param.receiveSms == "0") self.checkResult.push(self.adInfoAgree.sms.policyId);
                    if(self.param.receivePbanc == "0") self.checkResult.push(self.adInfoAgree.pbanc.policyId);
                    if(self.param.receiveKakao == "0") self.checkResult.push(self.adInfoAgree.kakao.policyId);
                    if(self.checkResult.length > 0) self.checkResult.push(self.adInfoAgree.policyId);
            	}

            }, function (error) {
                $s.alert(error.response.data.message);
            });
        },
        makePopup: function (popup) {
            var self = this;
            try {
                var openLayerPopups = [];

                if (typeof popup !== 'undefined' && popup !== null) {

                   // 쿠키 정보 - 오늘 하루 이 창을 열지 않음 (1)
                   var cookie = $.cookie('popup_check_' + popup.popupId);

                       if ($s.isMobile()) {
                           popup.popupStyle = '';
                           popup.handleStyle = '';

                           openLayerPopups.push(popup);
                       } else { // 레이어 팝업 정보 세팅
                           popup.popupStyle = 'position:absolute;' +
                               'left:' + popup.leftPosition + 'px;' +
                               'top:' + popup.topPosition + 'px;' +
                               'z-index:9999;' +
                               'width:' + popup.width + 'px;' +
                               'height:' + popup.height + 'px;';

                           popup.handleStyle = 'width:' + (popup.width + 20) + 'px;' +
                               'height:15px;cursor:move;' +
                               'background:' + popup.backgroundColor + ';';

                           openLayerPopups.push(popup);
                       }

                    // 레이어 팝업 띄우기
                    if (openLayerPopups.length > 0) {
                        self.result = openLayerPopups;
                    }
                }
            } catch (e) {
            	console.log(e)
            }
        },
        selected: function (e, value) {
            // 광고성 정보 수신 동의 클릭 이벤트
            const subAd = [this.adInfoAgree.sms.policyId,this.adInfoAgree.email.policyId,this.adInfoAgree.pbanc.policyId,this.adInfoAgree.kakao.policyId];
            const checkedSubAd = this.checkResult.filter(item => subAd.includes(item));

            if(e.target.id === this.adInfoAgree.policyId){
				if(!e.target.checked) {
					const subAdSet = new Set(subAd);
					this.checkResult = this.checkResult.filter(item => !subAdSet.has(item))
				}else{
					subAd.filter(item => !this.checkResult.includes(item)).map(item => this.checkResult.push(item));
				}
            }
        	// 광고성 정부 수신 동의 하위 항목 클릭 이벤트
            if(subAd.includes(e.target.id)){
            	if(checkedSubAd.length > 0){
            		if(!this.checkResult.includes(this.adInfoAgree.policyId)) this.checkResult.push(this.adInfoAgree.policyId);
    			}else{
    				this.checkResult = this.checkResult.filter(item => item !== this.adInfoAgree.policyId);
    			};
            }
        },
        altText(popupId){
            return "";
        }
    },
    mounted: function () {
        this.getPopups();
    }
}
</script>
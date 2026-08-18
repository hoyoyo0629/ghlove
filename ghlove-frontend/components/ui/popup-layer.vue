<template>
    <div id="popupArea">
        <div v-for="data in result" :id="'popup_' + data.popupId" class="p_admin_win" :style="data.popupStyle">
            <!-- <div class="p_header">
                <h3 class="p_header_tit">{{data.subject}}</h3>
            </div> -->
            <div class="p_body">
                <div v-if="data.popupImage !== '' && data.popupImage !== null
                    && data.imageLink !== '' && data.imageLink !== null">
                    <a :href="data.imageLink" target="_blank" title="새창 열림">
                        <img :src="itemImage(data.popupImageSrc)" border="0" :alt="altText(data.popupId) || '고향사랑 팝업'"/>
                    </a>
                </div>
                <div v-else-if="data.popupImage !== '' && data.popupImage !== null">
                    <img :src="itemImage(data.popupImageSrc)" border="0" :alt="altText(data.popupId) || '고향사랑 팝업'"/>
                </div>
                <div v-else v-html="unescapeHtml(nl2br(data.content))"></div>
            </div>


            <div class="check_area">
                <input type="checkbox" :id="'check' + data.popupId" @click="setCookie(data.popupId)" tabindex="1">
                <label :for="'check' + data.popupId" style="background : none">오늘 하루 이 창을 열지 않음</label>
            </div>
            <button class="p_admin_win_close" @click="popupClose(data.popupId)" tabindex="1">
                <span class="screen_out">닫기</span>
            </button>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: function () {
        return {
            result: []
        }
    },
    methods: {
        setCookie: function (popupId) {

            //$s.api.popup.setCookie('2', popupId);

            // '오늘 하루 보지 않음' 체크 시 팝업이 닫히도록 설정
            this.popupClose(popupId);
        },
        popupClose: function (popupId) {
            if (document.getElementById('check' + popupId).checked) $s.api.popup.setCookie('2', popupId);
            $s.api.popup.popupClose('2', popupId);
        },
        makeLayerPopup: function (list) {
            this.result = list;
        },
        getPopups: function () {
            $s.api.getPopups(function (data) {
                $s.api.popup.makePopup(data.list);
            }, function (error) {
                $s.alert(error.response.data.message);
            });
        },
        makePopup: function (list) {
            var self = this;
            try {
                var openLayerPopups = [];

                if (typeof list !== 'undefined' && list !== null) {
                    for (var i = 0; i < list.length; i++) {
                        var popup = list[i];

                        // 쿠키 정보 - 오늘 하루 이 창을 열지 않음 (1)
                        var cookie = $.cookie('popup_check_' + popup.popupId);

                        if (cookie !== '1') {
                            if ($s.isMobile()) {
                                popup.popupStyle = '';
                                popup.handleStyle = '';

                                openLayerPopups.push(popup);
                            } else if (popup.popupType === '1') {
                                0          // 윈도우 팝업 띄우기
                                Common.popup("/popup/?id=" + popup.popupId, 'openPopup' + popup.popupId,
                                    popup.width + 17, popup.height + 117, 0, popup.leftPosition, popup.topPosition);
                            } else if (popup.popupType === '2') {           // 레이어 팝업 정보 세팅
                                popup.popupStyle = 'position:absolute;' +
                                    'left:' + popup.leftPosition + 'px;' +
                                    'top:' + popup.topPosition + 'px;' +
                                    'z-index:9999;' +
                                    'width:' + (popup.width + 20) + 'px;' +
                                    'height:' + popup.height + 'px;';

                                popup.handleStyle = 'width:' + (popup.width + 20) + 'px;' +
                                    'height:15px;cursor:move;' +
                                    'background:' + popup.backgroundColor + ';';

                                openLayerPopups.push(popup);
                            }
                        }
                    }

                    // 레이어 팝업 띄우기
                    if (openLayerPopups.length > 0) {
                        self.result = openLayerPopups;
                        //var child = Vue.child("layout-popup");
                        //if (child !== null) {
                        //    child.makeLayerPopup(openLayerPopups);
                        //}
                    }
                }
            } catch (e) {
                let errorBoolean = true;
            }
        },
        focusPopup(){
            // $(".p_admin_win").focus();
        },
        altText(popupId){
            let altText ="";

            if(popupId == 185){
                altText = "더 나은 고향사랑e음을 위해 설문조사에 참여해주세요. 설문기간은 2024년 11월 13일(수)~11월26일(화) 고향사랑e음 서비스 품질 개선을 위한 사용자의견을 수렴합니다. 일반 사용자 설문조사도 메인페이지 접속해서 설문가능합니다.(www.ilovegohyang.go.kr)";
            }
            return altText;
        }
    },
    mounted: function () {
        this.getPopups();
    }
}
</script>
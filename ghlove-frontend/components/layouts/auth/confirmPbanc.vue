<template>
    <form action="/" id="pbancModalForm" name="pbancModalForm"  @submit.prevent>
        <!-- 공인인증서 | 휴대폰 인증 모달 -->
        <div class="black-bg" id="confirmPbanc" tabindex="0">
            <div class="modal-overlayer join-complete" style="max-width: 700px;" >
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기" class="closeModal"
                    @click="closeModal()">
                <div class="overlayer-header">SMS수신 동의 여부 확인 안내</div>
                <div class="overlayer-body" style="max-height: 50vh;" >
                    <div class="overlayer-body-wrap">
                        <h4>본 안내는 SMS 수신 동의 여부 확인을 진행하고자 합니다. </h4>
                        <br>
						<p>SMS 동의에 대한 유지 또는 철회 의사를 표시하는 방법
						<br>하단의 <b> [고향사랑e음 회원정보 수정 바로가기] </b>버튼을 눌러 SMS수신 동의 여부를 변경하실 수 있습니다.

                        <!-- 버튼그룹 -->
                        <div class="btn-box" style="margin-bottom:0px;">
                            <button type="button" class="blueBtn u-confirm" @click="closeModal()">
                                고향사랑e음 회원정보 수정 바로가기
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="btn-arrow">
                                </span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
        	location.href="/users/modify.html";
        },
    }, mounted: function () {
        this.$nextTick(function () {
        });
    }
}

</script>
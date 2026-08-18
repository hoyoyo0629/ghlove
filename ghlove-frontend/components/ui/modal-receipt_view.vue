<template>
    <div id="receipt_modal">
        <div class="modal_con">
            <div class="modal-body-receipt">
                <div class="modal_body_wrap">
                    <section id="contents" class="honor">
                        <!-- 모바일 -->
                        <div class="m_ver">
                            <div class="center" style="max-width: 550px">
                                <div class="receipt_card_area">

                                    <!-- 기부확인증 카드 -->
                                    <div class="card-wrapper" v-if="resultReceiptList && resultReceiptList.length > 0">
                                        <div class="card-face" id="cardContainer" @click="receipt_list()">

                                            <!-- 카드 앞면 -->
                                            <div class="lineBox card-front">
                                                <!-- 타이틀 -->
                                                <div class="title-area">
                                                    <div class="donation-comment">
                                                        <img alt="기부확인증" src="/static/images/donation_receipt/donation-comment.png" />
                                                    </div>
                                                    <h5>고향사랑기부 확인증</h5>
                                                </div>

                                                <!-- 내용 -->
                                                <div class="section-area">
                                                    <table class="table-area">
                                                    <caption class="sr-only">고향사랑기부 확인증</caption>
                                                        <colgroup>
                                                            <col style="width:40%">
                                                            <col style="width:60%">
                                                        </colgroup>
                                                        <tr>
                                                            <th style="letter-spacing: 2.55em;">성명</th>
                                                            <td>{{this.$parent.receiptInfo.result.userName}}</td>
                                                        </tr>
                                                        <tr>
                                                            <th style="letter-spacing: 0.25em;">생년월일</th>
                                                            <td>{{this.$parent.receiptInfo.result.birthday}}</td>
                                                        </tr>
                                                        <tr>
                                                            <th>기부지자체</th>
                                                            <td>{{this.$parent.receiptInfo.result.topLocGov}}</td>
                                                        </tr>
                                                        <tr>
                                                            <th style="letter-spacing: 0.25em;">기부금액</th>
                                                            <td>총 {{ formatNumber(this.$parent.receiptInfo.result.totalCntrAmt) }}원</td>
                                                        </tr>
                                                    </table>
                                                </div>

                                                <!-- 하단 -->
                                                <div class="bottom-area">
                                                    <div class="comment-area">위와 같이 고향사랑기부에 참여하였음을 확인함</div>
                                                    <div class="date-area">{{this.$parent.receiptInfo.result.nowDate}}</div>
                                                    <div class="icon-area">
                                                        <img alt="로고" src="/static/images/donation_receipt/logo.png" />
                                                    </div>
                                                </div>

                                            </div>
                                            <!-- 카드 앞면 END -->

                                            <!-- 카드 뒷면 -->
                                            <div class="lineBox card-back">

                                                <!-- 타이틀 -->
                                                <div class="title-area">
                                                    <h5>고향사랑기부내역</h5>
                                                </div>

                                                <!-- 내용 -->
                                                <div class="section-area">
                                                    <div class="receipt-result-body">
               		                                    <hr />
                                                        <table>
                                                        <caption class="sr-only">고향사랑기부내역</caption>
                                                            <colgroup>
                                                                <col style="width:15%">
                                                                <col style="width:70%">
                                                                <col style="width:15%">
                                                            </colgroup>
                                                            <tr class="result-title">
                                                                <th>기부일자</th>
                                                                <th>기부지자체</th>
                                                                <th>기부액 (원)</th>
                                                            </tr>
                                                            <tr class="result-row" v-for="(data,i) in resultReceiptList" :key="i">
                                                                <td class="">{{data.cntrDe}}</td>
                                                                <td class="">{{data.upperLocgovNm}} {{data.locgovNm}}{{data.spelDstrYn == 'Y' ? `(특별재난지역)`: ''}}<br/>(사업자번호 : {{data.bizRno}})</td>
                                                                <td class="amount">{{formatNumber(data.cntrAmt)}}</td>
                                                            </tr>
                                                        </table>
               		                                    <hr />
                                                    </div>
                                                </div>

                                               <!-- 하단 -->
                                                <div class="bottom-area">
                                                    <div class="comment-area">본 기부확인증은 고향사랑e음 기부내역을 바탕으로 발급되었으며,</div>
                                                    <div class="comment-area">전체 기부내역은 고향사랑e음을 통해서 확인이 가능합니다.</div>
                                                </div>

                                            </div>
                                            <!-- 카드 뒷면 END -->

                                        </div>
                                    </div>
                                    <!-- 기부확인증 카드 END -->
                                </div>
                            </div>
                        </div>
                    </section>

                    <div class="receipt_btn-box">
                        <button type="button" class="blueBtn u-confirm" @click="receiptPrint()">
                        확인증 출력
                        <span><img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="확인증 출력"/></span>
                        </button>
                        <button type="button" class="blueBtn u-confirm" @click="modalClose()">
                        닫기
                        <span><img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="닫기"/></span>
                        </button>
                    </div>
                </div>
            </div>
            <button type="button" class="closeBtn" @click="modalClose()">
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기">
            </button>
        </div>
    </div>

</template>
<script>
module.exports = {
    data: function () {
         return {
            cardMode : "1", //  "1": 기부확인증 앞면, "2": 기부확인증 뒷면
            btnType : {
                "1" : "기부내역 보기",
                "2" : "기부확인증 보기"
            },

          	userName: "",
          	popupTitle: "",
          	locgovNm: "",
          	result: {},
          	wdrList: "",
          	locGovList: "",
            param: {
                page : 1,
            	itemsPerPage: 10,
            	upperLocgovCode: "",
            	locgovCode: "",
            	searchStartDate: "",
            	searchEndDate: "",
            },
            contentLength: 0,
        }
    },

    methods: {
        // 모달 비활성화
        modalClose: function () {
            if(this.cardMode != 0){
                // 모달 종료 시 기부확인증 앞면으로 셋팅
                $("#cardContainer").removeClass("is-flipped")
                this.cardMode = "1"
            }

            $("#receipt_modal").hide();
            $(".btnOrderCancel").focus();
        },

        // 버튼 클릭시 뒤 화면 출력
        receipt_list: function(){
            if(this.cardMode == "1"){
                $("#cardContainer").addClass("is-flipped")
                this.cardMode = "2"
            }else if(this.cardMode == "2"){
                $("#cardContainer").removeClass("is-flipped")
                this.cardMode = "1"
            }
        },
        receiptPrint: function(){
            sessionStorage.setItem("receiptPrintData", JSON.stringify(this.$parent.receiptInfo.result));
            window.open("/mypage/receiptListPrint.html","_blank")
        }


    },
    computed:{
        btnLabel(){
            return this.btnType[this.cardMode] || "";
        },
        resultReceiptList(){

            return this.$parent.receiptInfo.result.list || [];

            //추후 테이블에 빈row가 필요할 경우 사용
            //const original = this.$parent.receiptInfo.result.list || [];
            //const defaultMinRows = 10;
            //if(original.length >= defaultMinRows){
            //    return original;
            //}
            //const padding = Array.from({ length : defaultMinRows - original.length }, () => ({}));
            //return [...original, ...padding];
        },
    },
}
</script>
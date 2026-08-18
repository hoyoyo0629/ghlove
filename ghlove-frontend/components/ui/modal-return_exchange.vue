<template>
    <div id="mypage_modal">
        <div class="modal_con">
            <div class="modal-title">
                {{ statusBtn }}
                <img class="closeBtn" src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기"
                    @click="modalClose()">
            </div>
            <div class="modal-body">
                <div class="modal_body_wrap">
                    <div class="g_info_wrap">
                        <div class="g_info__img"><img src="/static/images/goods/g_info__img.png" alt="">
                        </div>
                        <div class="g_info__txt">
                            <div class="info_loc">{{ content[4].upperLocgovNm }} {{ content[4].locgov }}</div>
                            <div class="info_title">정성가득한 사과 선물세트 12과 외 2건</div>
                            <div class="info_opt">옵션 : (특) 4kg</div>
                            <div class="info_total">
                                <span class="amount"><span class="deepBlue">1</span>개</span>
                                <span class="deepBlue">25,000P</span>
                            </div>
                        </div>
                    </div>

                    <div class="reason_wrap">
                        <div class="reason_item">
                            <label class="item__label" for="reason">교환사유</label>
                            <div class="item__select">
                                <select name="" id="reason">
                                    <option value="" v-for="(data, i) in reason" :key="i">{{ data }}</option>
                                </select>
                                <textarea name="" id=""></textarea>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">반송장 정보</label>
                            <div class="item__select">
                                <select name="" id="reason">
                                    <option value="" v-for="(data, i) in deliveryCom" :key="i">{{ data }}</option>
                                </select>
                                <input type="number" value="송장번호">
                            </div>
                        </div>
                    </div>
                    <div class="line"></div>

                    <div class="return-info">
                        <h4>반송지정보</h4>
                        <div class="reason_item">
                            <label class="item__label" for="reason">고객명</label>
                            <div class="item__select">
                                <input type="text" :value="userName" readonly>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">휴대폰번호</label>
                            <div class="item__select">
                                <span class="form-field">
                                    <select name="phoneCode" id="phoneCode" v-model="param.phoneCode">
                                        <option v-for="code in phoneCodes" :value="code.key.id">{{ code.label }}
                                        </option>
                                    </select>
                                    <span class="s-txt">-</span>
                                    <input type="number" name="phoneMid" id="phoneMid" v-model="param.phoneMid"
                                        maxlength="4"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                    <span class="s-txt">-</span>
                                    <input type="number" name="phoneLast" id="phoneLast" v-model="param.phoneLast"
                                        maxlength="4"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                </span>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">베송지 주소</label>
                            <div class="item__select">
                                <div class="address-field">
                                    <div class="m-field">
                                        <input type="text" name="post" id="post" v-model="param.post">
                                        <button type="button" class="formBtn" @click="searchAddress()">주소찾기</button>
                                    </div>
                                    <div class="input-address">
                                        <input type="text" name="address" id="address" value="기본주소"
                                            v-model="param.address">
                                        <input type="text" name="addressDetail" id="addressDetail" value="상세주소"
                                            v-model="param.addressDetail">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- .modal-title에 바인딩 데이터 값 '교환신청' || '반품신청' 
                    바인딩 데이터 값이 '반품신청'일 경우에만 환불정보 보여주기 -->
                    <div class="return-info" v-show="statusBtn == '반품신청'">
                        <h4>환불</h4>
                        <div class="reason_item return">
                            <label class="item__label">환불방식</label>
                            <div class="item__txt">포인트 환급</div>
                        </div>
                        <div class="reason_item return">
                            <label class="item__label">환불총액</label>
                            <div class="item__txt">{{ content[0].amt }}P (답례품 포인트 : {{ content[0].amt }}) + 배송비 : 무료배송)
                            </div>
                        </div>

                    </div>

                    <!-- 버튼그룹 -->
                    <div class="btn-box many">
                        <button type="button" class="blueBtn cancellation" @click="modalClose()">
                            취소
                            <span>
                                <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                            </span>
                        </button>
                        <button type="button" class="blueBtn u-confirm" @click="goToNext()">
                            신청
                            <span>
                                <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                            </span>
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </div>
</template>
<style scoped>

</style>
<script>
module.exports = {
    props: {
        statusBtn: {
            type: String,
            required: false,
            default: function () {
                return '';
            }
        }
    },
    data: function () {
        return {
            userName: "홍길동",
            reason: ['사유선택', '단순변심', '제품이상'],
            deliveryCom: ['택배사', '롯데택배', 'CJ대한통운'],
            content: [
                { deliveryCompanyName: '', deliveryCompanyNum: '', propsBtn: '주문취소', steps: '결제완료', year: 2023, month: '1월', date: '2022-01-05', orderNum: 123456789, goods: "고구마", upperLocgovNm: "서울시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '41,000' },
                { deliveryCompanyName: '', deliveryCompanyNum: '', propsBtn: '배송완료', steps: '배송준비중', year: 2023, month: '2월', date: '2022-01-12', orderNum: 234567891, goods: "고등어", upperLocgovNm: "경기도", locgov: "의정부시", case1: 30, case2: 50, case3: 40, case4: 10, amt: '1,110,000' },
                { deliveryCompanyName: '롯데택배', deliveryCompanyNum: '1235689001', propsBtn: '구매확정', steps: '배송중', year: 2023, month: '3월', date: '2022-01-15', orderNum: 345678912, goods: "사과", upperLocgovNm: "강원도", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '3,110,000' },
                { deliveryCompanyName: 'CJ대한통운', deliveryCompanyNum: '1235689001', propsBtn: '반품신청', steps: '배송완료', year: 2023, month: '4월', date: '2022-06-14', orderNum: 456789123, goods: "배", upperLocgovNm: "세종특별자치시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '1,110,000' },
                { deliveryCompanyName: 'CJ대한통운', deliveryCompanyNum: '1235689001', propsBtn: '교환신청', step: '구매확정', year: 2023, month: '5월', date: '2022-05-25', orderNum: 567891234, goods: "황태", upperLocgovNm: "대구광역시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '3,000,000' },
                { propsBtn: '주문취소', steps: '교환', year: 2023, month: '6월', date: '2022-06-05', orderNum: 123456789, goods: "고구마", upperLocgovNm: "서울시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '1,110,000' },
                { propsBtn: '배송완료', steps: '반품', year: 2023, month: '7월', date: '2022-07-12', orderNum: 234567891, goods: "고등어", upperLocgovNm: "경기도", locgov: "의정부시", case1: 30, case2: 50, case3: 40, case4: 10, amt: '201,110,000' },
                { propsBtn: '구매확정', steps: '취소', year: 2023, month: '8월', date: '2022-08-15', orderNum: 345678912, goods: "사과", upperLocgovNm: "강원도", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '30,110,000' },
            ],
            param: {
                locgovCode: "",
                upperLocgovNm: "",
                locgovNm: "",
                userName: "",
                loginId: "",
                phoneNumber: "",
                email: "",
                post: "",
                address: "",
                addressDetail: "",
                receiveEmail: "",
                userKeyYN: "",
                mberCiYN: "",
                password: "",
                corfirmPassword: "",
                upperLocgovCode: "",
                locgovCodeSet: "",
                interestLocGov: [],
                emailFirst: "",
                phoneMid: "",
                phoneLast: "",
                locGovList: [],
                emailAddress: ""
            },
            // 개발 후 지워주세요
        }
    },
    methods: {
        goToNext: function () {
            location.href = '/_pub/mypage/UI_P08030201.html';
        },

        modalClose: function () {
            $("#mypage_modal").hide();
        },
    },
    mounted: function () {

    }
}
</script>
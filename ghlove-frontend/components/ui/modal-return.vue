<template>
    <div id="return_modal">
        <div class="modal_con">
            <div class="modal-title">
                {{ statusBtn }}
            </div>
            <div class="modal-body">
                <div class="modal_body_wrap">
                    <label class="sr-only" for="returnShipmentReturnId">반품배송아이디</label>
                    <input type="hidden" :value="this.$parent.returnInfo.result.returnApply.shipmentReturnId"
                        id="returnShipmentReturnId">
                    <div class="g_info_wrap">
                        <div class="g_info__img">
                            <button type="button"
                                @click="viewDetails('/items/details.html?code=' + $parent.returnInfo.result.returnApply.orderItem.itemUserCode, 'paging', 'order')">
                                <img :src="itemImage(this.$parent.returnInfo.result.returnApply.orderItem.imageSrc)"
                                    :alt="unescapeHtml(this.$parent.returnInfo.result.returnApply.orderItem.itemName)+ ' 이미지'"
                                    @error="errorImage">
                            </button>
                        </div>
                        <div class="g_info__txt">
                            <div class="info_loc">{{ locgovNm }}</div>
                            <div class="info_title">
                                {{ unescapeHtml(this.$parent.returnInfo.result.returnApply.orderItem.itemName) }}</div>
                            <div class="info_opt" v-if="this.$parent.returnInfo.result.returnApply.orderItem.options != ''"
                                v-html="'옵션 [ ' + unescapeHtml(this.$parent.returnInfo.result.returnApply.orderItem.options) + ' ]'">
                            </div>
                            <div class="info_total">
                                <span class="amount"><span
                                        class="deepBlue">{{ formatNumber(this.$parent.returnInfo.result.returnApply.orderItem.quantity) }}</span>개</span>
                                <span
                                    class="deepBlue">{{ formatNumber(this.$parent.returnInfo.result.returnApply.orderItem.saleAmount) }}P</span>
                            </div>
                        </div>
                    </div>

                    <div class="reason_wrap">
                        <div class="reason_item">
                            <label class="item__label" for="reason">반품사유 <span class="essential pointRed">*</span></label>
                            <div class="item__select">
                            <label class="sr-only" for="returnCode">반품사유목록</label>
                                <select id="returnCode" v-model="$parent.returnInfo.param.claimReasonText" @change="claimChange($event)">
                                    <option disabled value="">사유선택</option>
                                    <option :value="returnData.label"
                                        v-for="returnData in this.$parent.returnInfo.result.claimReasons"
                                        :class="returnData.detail">{{ returnData.label }}</option>
                                </select>
                                <textarea placeholder="반품사유를 입력해주세요" title="반품사유를 입력해주세요" id="returnReason" maxlength="1000"
                                    v-model="$parent.returnInfo.param.claimReasonDetail"
                                    @input="changeSubjectInput($event)"></textarea>
                                <span id="contentTxtLength" class="s-txt"
                                    style="float: right;">[{{ contentLength }}/1000]</span>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">반송 송장 정보</label>
                            <div class="item__select">
                            <label class="sr-only" for="deliveryCompanyName">택배사 목록</label>
                                <select id="deliveryCompanyName" v-model="$parent.returnInfo.param.deliveryCompanyName">
                                    <option disabled value="">택배사</option>
                                    <option :value="data.deliveryCompanyName"
                                        v-for="data in this.$parent.returnInfo.result.deliveryCompanyList">{{
                                            data.deliveryCompanyName }}</option>
                                </select>
                                <label class="sr-only" for="returnShippingNumber">송장번호 입력</label>
                                <input id="returnShippingNumber" type="number" placeholder="송장번호를 입력하세요" title="송장번호를 입력하세요"
                                    v-model="$parent.returnInfo.param.returnShippingNumber">
                            </div>
                        </div>
                    </div>
                    <div class="line"></div>

                    <div class="return-info">
                        <h4>반송지정보</h4>
                        <div class="reason_item">
                            <label class="item__label" for="reason">고객명 <span class="essential pointRed">*</span></label>
                            <div class="item__select">
                            <label class="sr-only" for="returnReserveName">고객명</label>
                                <input id="returnReserveName" type="text" v-model="$parent.returnInfo.result.returnApply.returnReserveName"
                                    readonly>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">휴대폰번호 <span class="essential pointRed">*</span></label>
                            <div class="item__select">
                                <span class="form-field">
                                    <label class="sr-only" for="phoneCode">전화번호코드</label>
                                    <select name="phoneCode" id="phoneCode" v-model="$parent.returnInfo.param.mobile1">
                                        <option disabled value="">선택</option>
                                        <option v-for="(data, i) in phoneCodes" :value="data.key.id">{{ data.label }}
                                        </option>
                                    </select>
                                    <span class="s-txt">-</span>
                                    <label class="sr-only" for="mobileNum2">전화번호 가운데</label>
                                    <input type="text" name="mobile2" id="mobileNum2"
                                        v-model="$parent.returnInfo.param.mobile2" maxlength="4" @keyup="onlyNumber($event)"
                                        @blur="blurFn($event)"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                    <span class="s-txt">-</span>
                                    <label class="sr-only" for="mobileNum3">전화번호 끝</label>
                                    <input type="text" name="mobile3" id="mobileNum3"
                                        v-model="$parent.returnInfo.param.mobile3" maxlength="4" @keyup="onlyNumber($event)"
                                        @blur="blurFn($event)"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                </span>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">반송지 주소 <span class="essential pointRed">*</span></label>
                            <div class="item__select">
                                <div class="address-field">
                                    <div class="m-field">
                                    <label class="sr-only" for="post">우편번호</label>
                                        <input type="text" name="post" id="post"
                                            v-model="$parent.returnInfo.param.zipCodeInfo" readonly>
                                        <button type="button" class="formBtn" @click="searchAddress()">주소찾기</button>
                                    </div>
                                    <div class="input-address">
                                     <label class="sr-only" for="address">주소</label>
                                        <input type="text" name="address" id="address" value="기본주소"
                                            v-model="$parent.returnInfo.param.addressInfo" readonly>
                                        <label class="sr-only" for="addressDetail2">상세주소</label>    
                                        <input type="text" name="addressDetail" id="addressDetail2" value="상세주소"
                                            v-model="$parent.returnInfo.result.returnApply.returnReserveAddress2">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- .modal-title에 바인딩 데이터 값 '교환신청' || '반품신청' 
                    바인딩 데이터 값이 '반품신청'일 경우에만 환불정보 보여주기 -->
                    <div class="return-info" v-show="statusBtn == '반품신청'">
                        <h4>환불</h4>
                        <table>
                            <tr class="reason_item return">
                                <td class="item__label">환불방식</td>
                                <td class="item__txt">포인트 환급</td>
                            </tr>
                            <tr class="reason_item return">
                                <td class="item__label">환불총액</td>
                                <td class="item__txt">{{
                                    formatNumber(this.$parent.returnInfo.result.returnApply.orderItem.saleAmount) }}P (답례품
                                    포인트 : {{ formatNumber(this.$parent.returnInfo.result.returnApply.orderItem.saleAmount)
                                    }}P + 배송비 : 무료배송)</td>
                            </tr>
                        </table>
                    </div>

                    <!-- 버튼그룹 -->
                    <div class="btn-box many">
                        <button type="button" class="blueBtn cancellation" @click="modalClose()">
                            취소
                            <span>
                                <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                            </span>
                        </button>
                        <button id="btnReturn" type="button" class="blueBtn u-confirm" @click="returnProcess()">
                            신청
                            <span>
                                <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                            </span>
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
<style scoped></style>
<script>
module.exports = {
    props: {
        statusBtn: String,
        locgovNm: String,
        phoneCodes: Array
    },
    data: function () {
        return {
            content: [
                { deliveryCompanyName: '', deliveryCompanyNum: '', propsBtn: '주문취소', steps: '결제완료', year: 2023, month: '1월', date: '2022-01-05', orderNum: 123456789, goods: "고구마", upperLocgovNm: "서울시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '41,000' },
                { deliveryCompanyName: '', deliveryCompanyNum: '', propsBtn: '배송완료', steps: '배송준비중', year: 2023, month: '2월', date: '2022-01-12', orderNum: 234567891, goods: "고등어", upperLocgovNm: "경기도", locgov: "의정부시", case1: 30, case2: 50, case3: 40, case4: 10, amt: '1,110,000' },
                { deliveryCompanyName: '롯데택배', deliveryCompanyNum: '1235689001', propsBtn: '구매확정', steps: '배송중', year: 2023, month: '3월', date: '2022-01-15', orderNum: 345678912, goods: "사과", upperLocgovNm: "강원도", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '3,110,000' },
                { deliveryCompanyName: 'CJ대한통운', deliveryCompanyNum: '1235689001', propsBtn: '반품신청', steps: '배송완료', year: 2023, month: '4월', date: '2022-06-14', orderNum: 456789123, goods: "배", upperLocgovNm: "세종특별자치시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '1,110,000' },
                { deliveryCompanyName: 'CJ대한통운', deliveryCompanyNum: '1235689001', propsBtn: '교환신청', steps: '구매확정', year: 2023, month: '5월', date: '2022-05-25', orderNum: 567891234, goods: "황태", upperLocgovNm: "대구광역시", locgov: "본청", case1: 30, case2: 50, case3: 40, case4: 10, amt: '3,000,000' },
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
                locGovList: [],
                emailAddress: "",
                phoneCode: ""
            },
            contentLength: 0,
        }
    },
    methods: {
        // 반품신청
        returnProcess: function () {
            vm.returnInfo.param.shipmentReturnId = $("#returnShipmentReturnId").val();
            vm.returnInfo.param.returnReserveMobile = vm.returnInfo.param.mobile1 + vm.returnInfo.param.mobile2 + vm.returnInfo.param.mobile3;
            vm.returnInfo.param.returnReserveName = vm.returnInfo.result.returnApply.returnReserveName;
            vm.returnInfo.param.returnReserveAddress2 = vm.returnInfo.result.returnApply.returnReserveAddress2;
            vm.returnInfo.param.returnShippingCompanyName = vm.returnInfo.param.deliveryCompanyName;
            vm.returnInfo.param.applyQuantity = vm.returnInfo.result.returnApply.orderItem.quantity;
            vm.returnInfo.param.returnShippingAskType = "2";

            if (vm.returnInfo.param.returnReserveZipcode === "" || vm.returnInfo.param.returnReserveZipcode == undefined) {
                vm.returnInfo.param.returnReserveZipcode = vm.returnInfo.result.returnApply.returnReserveZipcode;
                vm.returnInfo.param.returnReserveAddress = vm.returnInfo.result.returnApply.returnReserveAddress;
                vm.returnInfo.param.returnReserveAddress2 = vm.returnInfo.result.returnApply.returnReserveAddress2;
                vm.returnInfo.param.returnReserveSido = vm.returnInfo.result.returnApply.returnReserveSido;
                vm.returnInfo.param.returnReserveSigungu = vm.returnInfo.result.returnApply.returnReserveSigungu;
                vm.returnInfo.param.returnReserveEupmyeondong = vm.returnInfo.result.returnApply.returnReserveEupmyeondong;
            }

            if (vm.returnInfo.param.applyQuantity === "" || vm.returnInfo.param.applyQuantity == undefined) {
                $s.alert("수량을 선택하세요.");
                return false;
            }
            if (vm.returnInfo.param.claimReasonText === "" || vm.returnInfo.param.claimReasonText == undefined) {
                $s.alert("반품사유를 선택하세요.", "returnCode");
                return false;
            }
            /*if (vm.returnInfo.param.returnShippingAskType != "1" &&
                (vm.returnInfo.param.returnShippingAskType === "" || vm.returnInfo.param.returnShippingAskType == undefined ||
                vm.returnInfo.param.deliveryCompanyName === "" || vm.returnInfo.param.deliveryCompanyName == undefined)) {
                $s.alert("택배사를 입력해주세요.");
                return false;
            }
            if (vm.returnInfo.param.returnShippingAskType != "1"
                && (vm.returnInfo.param.returnShippingNumber === "" || vm.returnInfo.param.returnShippingNumber == undefined)) {
                $s.alert("송장번호를 입력하세요.");
                return false;
            }*/
            if (vm.returnInfo.param.returnReserveZipcode === "" || vm.returnInfo.param.returnReserveZipcode == undefined) {
                vm.returnInfo.param.returnReserveZipcode = vm.returnInfo.result.returnApply.returnReserveZipcode;
                vm.returnInfo.param.returnReserveAddress = vm.unescapeHtml(vm.returnInfo.result.returnApply.returnReserveAddress);
            }
            if (vm.returnInfo.param.returnReserveZipcode === "" || vm.returnInfo.param.returnReserveZipcode == undefined ||
                vm.returnInfo.param.returnReserveAddress === "" || vm.returnInfo.param.returnReserveAddress == undefined ||
                vm.returnInfo.param.returnReserveAddress2 === "" || vm.returnInfo.param.returnReserveAddress2 == undefined) {
                $s.alert("반송지 정보를 입력해주세요.", "addressDetail2");
                return false;
            }
            if (vm.returnInfo.param.mobile1 === "" || vm.returnInfo.param.mobile1 == undefined) {
                $s.alert("휴대폰 첫번째 번호를 입력해주세요.", "phoneCode");
                return false;
            }
            if (vm.returnInfo.param.mobile2 === "" || vm.returnInfo.param.mobile2 == undefined) {
                $s.alert("휴대폰 두번째 번호를 입력해주세요.", "mobileNum2");
                return false;
            }
            if (vm.returnInfo.param.mobile2.length != 4) {
                $s.alert("휴대폰 두번째 번호 4자리를 \n 모두 입력해주세요.", "mobileNum2");
                return false;
            }
            if (vm.returnInfo.param.mobile3 === "" || vm.returnInfo.param.mobile3 == undefined) {
                $s.alert("휴대폰 세번째 번호를 입력해주세요.", "mobileNum3");
                return false;
            }
            if (vm.returnInfo.param.mobile3.length != 4) {
                $s.alert("휴대폰 세번째 번호 4자리를 \n 모두 입력해주세요.", "mobileNum3");
                return false;
            }
            if (vm.returnInfo.param.returnBankName === "" || vm.returnInfo.param.returnBankName == undefined ||
                vm.returnInfo.param.returnBankInName === "" || vm.returnInfo.param.returnBankInName == undefined ||
                vm.returnInfo.param.returnVirtualNo === "" || vm.returnInfo.param.returnVirtualNo == undefined) {
                //$s.alert("환불계좌 정보를 입력해주세요.");
                //return false;
            }
            $s.confirm('선택하신 답례품을 반품신청 하시겠습니까?', function () {
                vm.showLoading(true);
                $s.api.returnProcess(vm.returnInfo.param,
                    function (response) {
                        if (response.status === "OK") {
                            vm.showLoading(false);
                            $s.alert("반품신청 되었습니다.", function () {
                                location.reload();
                            });
                        }
                    }, function (error) {
                        vm.showLoading(false);
                        $s.alert(error.response.data.message);
                    }
                );
            }, 'btnReturn');
        },
        claimChange: function (e) {
            var index = e.target.selectedIndex;
            this.$parent.returnInfo.param.claimReason = e.target.options[index].className;
        },
        onlyNumber: function (e) {

            // 휴대폰 번호 숫자 이외의 입력값 초기화
            const regExp = /[^0-9]/g;
            var inputVal = e.target.value;

            if (regExp.test(inputVal)) {
                e.target.value = inputVal.replace(regExp, "");
                this.$parent.returnInfo.param.mobile2 = String(this.$parent.returnInfo.param.mobile2).replace(regExp, "");
                this.$parent.returnInfo.param.mobile3 = String(this.$parent.returnInfo.param.mobile3).replace(regExp, "");
            }

        },
        blurFn: function (e) {

            const regExp = /[^0-9]/g;
            var inputVal = e.target.value;

            // Tab키로 이동하면 지워졌던 한글이 복구되어 다시 초기화
            if (regExp.test(inputVal)) {
                e.target.value = inputVal.replace(regExp, "");
                this.$parent.returnInfo.param.mobile2 = String(this.$parent.returnInfo.param.mobile2).replace(regExp, "");
                this.$parent.returnInfo.param.mobile3 = String(this.$parent.returnInfo.param.mobile3).replace(regExp, "");
            }

        },
        modalClose: function () {
            $("#return_modal").hide();
            $(".btnReturnApply").focus();
        },
        // 배송지 주소찾기
        searchAddress: function () {
            this.$parent.returnOpenDaumPostcode();
        },
        changeSubjectInput: function (e) {
            let txtLength = e.target.value.length;
            if (e.target.id === 'returnReason') {
                this.contentLength = txtLength;
            }
        },
    },
    mounted: function () {

    }
}
</script>
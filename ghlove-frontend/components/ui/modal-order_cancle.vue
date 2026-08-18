<template>
    <div id="cancle_modal">
        <div class="modal_con">
            <div class="modal-title">
                <!-- {{ statusBtn }} -->
                취소신청
            </div>
            <div class="modal-body">
                <div class="modal_body_wrap">
                    <div class="list_all_select">
                        <input type="checkbox" name="" id="allSelect" @click="checkAll()" @keydown="checkEnter($event)">
                        <label for="allSelect">전체선택</label>
                    </div>
                    <div class="order-list"
                        v-for="data in this.$parent.cancelInfo.result.claimApply.order.orderShippingInfos">
                        <ul>
                            <li class="g_info_wrap" v-for="item in data.orderItems" v-if="item.claimApplyFlag === 'Y'">
                                <div class="date-col chk_date">
                                    <div class="check_input">
                                        <input type="hidden" v-model="$parent.cancelInfo.param.orderCode">
                                        <input type="hidden" v-model="$parent.cancelInfo.param.orderSequence">
                                        <input type="hidden" v-model="$parent.cancelInfo.param.claimType">
                                        <input type="checkbox" name="" :id="item.claimApplyItemKey" class="op-key"
                                            :data-item-sequence="item.itemSequence"
                                            :data-parent-item-sequence="item.parentItemSequence"
                                            :data-addition-item-flag="item.additionItemFlag"
                                            :data-applyQuantity="item.quantity - item.claimQuantity" title="선택"
                                            :value="item.claimApplyItemKey"
                                            @click="cancelRefundCheck($event, item.claimApplyItemKey)"
                                            @keydown="cancelRefundCheckKeyEvent($event, item.claimApplyItemKey)">
                                    </div>
                                </div>
                                <div class="g_info__img">
                                    <button type="button"
                                        @click="viewDetails('/items/details.html?code=' + item.itemUserCode, 'paging', 'order')" :title="item.itemName + ' 상세 페이지 이동'">
                                        <img :src="itemImage(item.imageSrc)" :alt="unescapeHtml(item.itemName) + ' 이미지'"
                                            @error="errorImage">
                                    </button>
                                </div>
                                <div class="g_info__txt">
                                    <div class="info_loc">{{ item.locgovNm }} {{ data.locgov }}</div>
                                    <div class="info_title">{{ unescapeHtml(item.itemName) }}</div>
                                    <div class="info_opt" v-if="item.options != ''"
                                        v-html="'옵션 [ ' + unescapeHtml(item.options) + ' ]'"></div>
                                    <div class="info_total">
                                        <span class="amount"><span class="deepBlue">{{ item.quantity - item.claimQuantity
                                        }}</span>개</span>
                                        <span class="deepBlue">{{ formatNumber(item.saleAmount) }}P</span>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>


                    <div class="reason_wrap">
                        <div class="reason_item">
                            <label class="item__label" for="reason">취소사유선택 <span class="essential pointRed">*</span></label>
                            <div class="item__select">
                                <label class="sr-only" for="claimReasonCode">취소사유 목록</label>
                                <select id="claimReasonCode" v-model="$parent.cancelInfo.param.claimReasonText"
                                    @change="claimReasonsSelect($event)">
                                    <option disabled value="">사유선택</option>
                                    <option :value="cancelData.label"
                                        v-for="cancelData in this.$parent.cancelInfo.result.claimReasons">
                                        {{ cancelData.label }}</option>
                                </select>
                                <textarea name="" id="claimReasonText" style="display: none;" placeholder="취소사유를 입력해주세요"
                                    title="취소사유를 입력해주세요" v-model="$parent.cancelInfo.param.claimReasonDetail"
                                    maxlength="1000" @input="changeSubjectInput($event)"></textarea>
                                <span id="contentTxtLength" class="s-txt" style="float: right;display: none;">[{{
                                    contentLength }}/1000]</span>
                            </div>
                        </div>
                        <!-- <div class="reason_item info-txt s-txt pointRed">
                            <span>※</span>
                            <span>주문내역중 선택하신 상품과 주문/배송상태가 동일한 답례품만 목록에 노출됩니다.</span>
                        </div> -->
                    </div>




                    <div class="return-info">
                        <h4>환불</h4>
                        <table>
                            <tr class="reason_item return">
                                <td class="item__label">환불방식</td>
                                <td class="item__txt">포인트 환급</td>
                            </tr>
                            <tr class="reason_item return">
                                <td class="item__label">환불총액</td>
                                <td class="item__txt"> {{
                                    formatNumber(this.$parent.cancelInfo.refundResult.orderRefundApiInfo.totalReturnAmount)
                                }}P
                                    (답례품 포인트 :
                                    {{
                                        formatNumber(this.$parent.cancelInfo.refundResult.orderRefundApiInfo.totalItemReturnAmount)
                                    }}P
                                    + 배송비 : 무료배송)</td>
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
                        <button id="btnApply" type="button" class="blueBtn u-confirm" @click="cancelProcess()">
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
        statusBtn: String
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
                emailAddress: "",
            },
            contentLength: 0,
        }
    },
    methods: {
        goToNext: function () {
            location.href = '/_pub/mypage/UI_P08030201.html';
        },
        modalClose: function () {
            $("#cancle_modal").hide();
            $(".btnOrderCancel").focus();
        },
        // 취소신청-선택상세
        cancelRefundCheck: function (e, claimApplyItemKey) {
            if ($("input[type='checkbox']:checked.op-key").length > 0) {
                var id = new Array();
                vm.cancelInfo.param.claimApplyItemMap = {};
                $.each($("input[type='checkbox']:checked.op-key"), function (e) {
                    id.push(this.value);
                    vm.cancelInfo.param.claimApplyItemMap[this.value] = {
                        orderCode: vm.cancelInfo.param.orderCode,
                        orderSequence: vm.cancelInfo.param.orderSequence,
                        applyQuantity: this.getAttribute("data-applyQuantity"),
                        itemSequence: this.getAttribute("data-item-sequence")
                    };
                });
                vm.cancelInfo.param.claimReason = "2";
                vm.cancelInfo.param.id = id;

                $s.api.getRefundAmount(vm.cancelInfo.param,
                    function (response) {
                        vm.cancelInfo.refundResult = response.refundInfo;
                        var orai = vm.cancelInfo.refundResult.orderRefundApiInfo;
                        var pgPaymentType = 'deposit';
                        var claimApplyQuantity = orai.claimApplyQuantity;
                        var totalOrderQuantity = orai.totalOrderQuantity;
                        var orderStatus = vm.cancelInfo.param.orderStatus;

                        if (vm.cancelInfo.refundResult.orderPgData != null) {
                            pgPaymentType = vm.cancelInfo.refundResult.orderPgData.pgPaymentType;
                        } else {
                            vm.cancelInfo.refundResult.orderPgData = ({
                                'pgPaymentType': pgPaymentType
                            });
                        }

                        $("#refundInfo").show();
                        if (orai.isAutoCancel == false || orai.isWriteBankInfo == true) {
                            if (pgPaymentType == 'deposit') {
                                $('#partCancelComment').hide();

                            } else if (claimApplyQuantity == totalOrderQuantity) {
                                $('#partCancelComment').text('자동 환불이 불가능한 결제방식의 경우 위에 입력하신 계좌로 취소금액을 입금해드립니다.');

                            } else if (claimApplyQuantity != totalOrderQuantity) {
                                $('#partCancelComment').text('부분취소가 불가능한 결제방식의 경우 위에 입력하신 계좌로 취소금액을 입금해드립니다.');
                            }

                            $("#refundBank").show();
                            // 결제완료 상태 - 부분취소 불가능한 카드결제 전체취소 시 부분취소 가능여부(PART_CANCEL_FLAG)와 상관없이 환불계좌를 입력받지 않고 PG취소
                            if (orderStatus == '10' && pgPaymentType == 'CARD' && claimApplyQuantity == totalOrderQuantity) {
                                $("#refundBank").hide();
                            }
                        } else {
                            $("#refundBank").hide();
                        }
                    }, function (error) {
                        vm.showLoading(false);
                        $s.alert(error.response.data.message);
                    }
                );
            } else {
                vm.cancelInfo.refundResult.orderRefundApiInfo.totalReturnAmount = 0;
                vm.cancelInfo.refundResult.orderRefundApiInfo.totalItemReturnAmount = 0;
            }
        },
        checkAll: function () {
            if ($('#allSelect').prop("checked")) {
                $("input[type='checkbox']").prop("checked", true);
                this.cancelRefundCheck();
            } else {
                $("input[type='checkbox']").prop("checked", false);
                vm.cancelInfo.refundResult.orderRefundApiInfo.totalReturnAmount = 0;
                vm.cancelInfo.refundResult.orderRefundApiInfo.totalItemReturnAmount = 0;
            }
        },
        checkEnter: function (e) {
            if (e.keyCode === 13) {
                if (!$('#allSelect').prop("checked")) {
                    $("input[type='checkbox']").prop("checked", true);
                    this.cancelRefundCheck();
                } else {
                    $("input[type='checkbox']").prop("checked", false);
                    vm.cancelInfo.refundResult.orderRefundApiInfo.totalReturnAmount = 0;
                    vm.cancelInfo.refundResult.orderRefundApiInfo.totalItemReturnAmount = 0;
                }
            }
        },
        cancelRefundCheckKeyEvent: function (e) {
              if (e.keyCode === 13) {
                if(!$(e.target).prop("checked")) {
                    $(e.target).prop("checked", true);
                    this.cancelRefundCheck();
                } else {
                    $(e.target).prop("checked", false);
                    vm.cancelInfo.refundResult.orderRefundApiInfo.totalReturnAmount = 0;
                    vm.cancelInfo.refundResult.orderRefundApiInfo.totalItemReturnAmount = 0;
                }
            }      
        },        
        claimReasonsSelect: function (event) {
            var value = event.target.value;

            // 취소 사유 초기화
            this.contentLength = 0;
            this.$emit('init-reason-detail', 'cancel');
            // 취소 사유 초기화

            $("#claimReasonText").hide();
            $("#contentTxtLength").hide();
            if (value == "기타") {
                $("#claimReasonText").show();
                $("#contentTxtLength").show();
            }
        },
        // 취소신청
        cancelProcess: function () {
            if ($("input[type='checkbox']:checked.op-key").length == 0) {
                $s.alert('취소 상품을 선택해주세요.', 'allSelect');
                return false;
            }

            var a = vm.cancelInfo.refundResult.orderRefundApiInfo;

            // 결제완료 상태 - 카드 전체 취소일 경우 부분취소 가능여부(PART_CANCEL_FLAG)와 상관없이 환불계좌를 입력받지 않고 PG취소
            var pgPaymentType = 'deposit';
            if (vm.cancelInfo.refundResult.orderPgData != null) {
                pgPaymentType = vm.cancelInfo.refundResult.orderPgData.pgPaymentType;
            } else {
                vm.cancelInfo.refundResult.orderPgData = ({
                    'pgPaymentType': pgPaymentType
                });
            }

            var claimApplyQuantity = a.claimApplyQuantity;
            var totalOrderQuantity = a.totalOrderQuantity;
            var orderStatus = vm.cancelInfo.param.orderStatus;

            if (orderStatus == '10') {	// 결제완료 상태일 경우 즉시 취소 및 환불처리 실행
                vm.cancelInfo.param.claimRefundType = 1;
            } else {	// 배송준비중 상태일 경우 취소신청만 실행
                vm.cancelInfo.param.claimRefundType = 2;
            }
            vm.cancelInfo.param.claimType = vm.cancelInfo.result.claimApply.claimType;	// 클레임 구분 (1:취소)

            if (vm.cancelInfo.param.applyQuantity === "" || vm.cancelInfo.param.applyQuantity == undefined) {
                $s.alert("수량을 선택해주세요.");
                return false;
            }
            if (vm.cancelInfo.param.claimReasonText === "" || vm.cancelInfo.param.claimReasonText == undefined) {
                $s.alert("취소사유를 선택해주세요.", 'claimReasonCode');
                return false;
            }
            if (vm.cancelInfo.param.claimReasonText === "기타") {
                if (vm.cancelInfo.param.claimReasonDetail === "" || vm.cancelInfo.param.claimReasonDetail === " " || vm.cancelInfo.param.claimReasonDetail == undefined) {
                    $s.alert("취소사유를 입력해주세요.", 'claimReasonText');
                    return false;
                }
            } else {
                // 취소사유가 기타가 아닌 경우 클레임 텍스트 공백문자 삽입
                vm.cancelInfo.param.claimReasonDetail = " ";
            }
            var orai = vm.cancelInfo.refundResult.orderRefundApiInfo;
            if (orai.isAutoCancel == false || orai.isWriteBankInfo == true) {
                if (vm.cancelInfo.param.returnBankName === "" || vm.cancelInfo.param.returnBankName == undefined ||
                    vm.cancelInfo.param.returnBankInName === "" || vm.cancelInfo.param.returnBankInName == undefined ||
                    vm.cancelInfo.param.returnVirtualNo === "" || vm.cancelInfo.param.returnVirtualNo == undefined) {

                    // 부분취소 불가능한 카드결제 전체취소 시 부분취소 가능여부(PART_CANCEL_FLAG)와 상관없이 환불계좌를 입력받지 않고 PG취소
                    if (!(pgPaymentType == 'CARD' && claimApplyQuantity == totalOrderQuantity)) {
                        //$s.alert("환불계좌 정보를 입력해주세요.");
                        //return false;
                    }
                }
            }

            $s.confirm('선택하신 상품을 취소신청 하시겠습니까?', function () {
                vm.showLoading(true);
                $s.api.cancelProcess(vm.cancelInfo.param,
                    function (response) {
                        if (response.status === "OK") {
                            vm.showLoading(false);
                            $s.alert("취소신청 되었습니다.", function () {
                                location.reload();
                            });
                        }
                    }, function (error) {
                        vm.showLoading(false);
                        $s.alert(error.response.data.message);
                    }
                );
            }, 'btnApply');
        },
        changeSubjectInput: function (e) {
            let txtLength = e.target.value.length;
            if (e.target.id === 'claimReasonText') {
                this.contentLength = txtLength;
            }
        },
    },
    mounted: function () {

    }
}
</script>
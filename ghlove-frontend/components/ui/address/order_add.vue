<template>
    <div id="add_modal" ref="addModal">
        <!-- <div class="modal_con" v-show="stepStatus == '1'"> -->
        <div class="modal_con" v-show="viewMode == '1'">
            <div class="modal-title">
                배송지 목록
            </div>
            <div class="modal-body">
                <div class="modal_body_wrap">
                    <div class="btn_area">
                        <button class="formBtn del" @click="setViewMode('2')" id="regDeliveryInfo">배송지 추가</button>
                        <button class="formBtn del" @click="setViewMode('3')" style="margin-left: 10px;" id="modDeliveryInfo">배송지 수정</button>
                    </div>
                    <div class="address_list_wrap" v-show="!isNoData">
                        <div class="list-title">
                            <div class="date-col s_radio">선택</div>
                            <div class="address_wrap">
                                <div class="date-col a_name">배송지명</div>
                                <div class="date-col a_info">배송정보</div>
                            </div>
                        </div>
                        <ul class="list-search-group">
                            <!-- <li class="list-items" v-for="(data, index) in content" :key="index">
                                <div class="date-col s_radio">
                                    <input type="radio" name="selectedAdd" :id="index">
                                </div>
                                <label :for="index" class="address_wrap">
                                    <span class="date-col a_name">{{ data.aName }}</span>
                                    <span class="a_info">
                                        <span class="a_tag" v-if="data.basicSet == true">
                                            <button class="formBtn default">기본배송지</button>
                                        </span>
                                        <span class="a_address">{{ data.address }}</span>
                                        <span class="a_nacell">
                                            <span class="p_name">{{ data.name }}</span>
                                            <span class="vLine">|</span>
                                            <span class="p_phone">{{ data.cellphone }}</span>
                                        </span>
                                    </span>
                                </label>
                            </li> -->
                            <li class="list-items" v-for="(data, index) in deliveryList" :key="index">
                                <div class="date-col s_radio">
                                    <input type="radio" name="radioDeliveryList" :id="index" :value="data.userDeliveryId"
                                        v-model="selectedDeliveryId">
                                </div>
                                <label :for="index" class="address_wrap">
                                    <span class="date-col a_name">{{ data.title }}</span>
                                    <span class="a_info">
                                        <span class="a_tag" v-show="data.defaultFlag == 'Y'">
                                            <button class="formBtn default">기본</button>
                                        </span>
                                        <span class="a_address">{{ data.address }}</span>
                                        <span class="a_nacell">
                                            <span class="p_name">{{ data.userName }}</span>
                                            <span class="vLine">|</span>
                                            <span class="p_phone">{{ data.mobile }}</span>
                                        </span>
                                    </span>
                                </label>
                            </li>
                        </ul>
                        <!-- 버튼그룹 -->
                        <div class="btn-box many">
                            <button type="button" class="blueBtn cancellation" @click="setDefaultDelivery()"
                                id="setDefaultDeliveryBtn">
                                기본배송지로 설정
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                                </span>
                            </button>
                            <button type="button" class="blueBtn u-confirm" @click="copyDeliveryInfoByList()"
                                id="selectDeliveryBtn">
                                배송지 선택
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                                </span>
                            </button>
                        </div>
                    </div>
                </div>

                <div class="list-none" v-show="isNoData">
                    등록된 배송지가 없습니다.
                </div>
            </div>
            <button type="button" class="closeBtn" @click.prevent="modalClose(2)">
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기">
            </button>
        </div>

        <div class="modal_con" v-show="viewMode == '2' || viewMode == '3' || viewMode == '4'">
            <div class="modal-title" v-show="viewMode == '2' || viewMode == '4'">
                배송지 추가
            </div>
            <div class="modal-title" v-show="viewMode == '3'">
                배송지 수정
            </div>
            <div class="modal-body">
                <div class="modal_body_wrap addlist">
                    <form action="" class="info-field">
                        <div class="info-field-group">
                            <div class="info-field-items aName">
                                <label for="aNameModal" class="flied-title">배송지명 <span class="pointblue">*</span></label>
                                <span class="form-field">
                                    <input type="text" name="aNameModal" id="aNameModal" v-model="addDeliveryInfo.title">
                                    <span class="basic_address">
                                        <input type="checkbox" name="" id="b_add_address"
                                            v-model="addDeliveryInfo.isDefault">
                                        <label for="b_add_address">기본 주소 설정</label>
                                    </span>
                                </span>
                            </div>
                            <div class="info-field-items aName">
                                <label for="reNameModal" class="flied-title">받으시는 분 <span class="pointblue">*</span></label>
                                <span class="form-field">
                                    <input type="text" name="reNameModal" id="reNameModal" v-model="addDeliveryInfo.userName">
                                </span>
                            </div>
                            <div class="info-field-items userNum">
                                <label class="flied-title">휴대전화번호 <span class="pointblue">*</span></label>
                                <span class="form-field">
                                    <label for="phoneCodeModal" class="sr-only">휴대폰번호 앞자리를 선택하세요</label>
                                    <select name="phoneCodeModal" id="phoneCodeModal" v-model="addDeliveryInfo.mobile1">
                                        <option v-for="code in phoneCodes" :value="code.key.id">
                                            {{ code.label }}
                                        </option>
                                    </select>
                                    <span class="s-txt">-</span>
                                    <label for="phoneMidModal" class="sr-only">휴대폰번호 가운데 3-4자리를 입력하세요</label>
                                    <input type="number" name="phoneMidModal" id="phoneMidModal" v-model="addDeliveryInfo.mobile2"
                                        maxlength="4"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                    <span class="s-txt">-</span>
                                    <label for="phoneLastModal" class="sr-only">휴대폰번호 마지막 4자리를 입력하세요</label>
                                    <input type="number" name="phoneLastModal" id="phoneLastModal" v-model="addDeliveryInfo.mobile3"
                                        maxlength="4"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                </span>
                            </div>
                            <div class="info-field-items userNum">
                                <label class="flied-title">전화번호</label>
                                <span class="form-field">
                                    <label for="telCode" class="sr-only">전화번호 앞자리를 선택하세요</label>
                                    <select name="telCode" id="telCode" v-model="addDeliveryInfo.phone1">
                                        <option v-for="code in telCodes" :value="code.key.id">
                                            {{ code.label }}
                                        </option>
                                    </select>
                                    <span class="s-txt">-</span>
                                    <label for="telMid" class="sr-only">전화번호 가운데 3-4자리를 입력하세요</label>
                                    <input type="number" name="telMid" id="telMid" v-model="addDeliveryInfo.phone2"
                                        maxlength="4"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                    <span class="s-txt">-</span>
                                    <label for="telLast" class="sr-only">전화번호 마지막 4자리를 입력하세요</label>
                                    <input type="number" name="telLast" id="telLast" v-model="addDeliveryInfo.phone3"
                                        maxlength="4"
                                        oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);">
                                </span>
                            </div>
                            <!-- 주문자주소 -->
                            <div class="info-field-items">
                                <label for="addressModal" class="flied-title">배송지 주소 <span class="pointblue">*</span></label>
                                <div class="form-field">
                                    <div class="search-address m-field">
                                        <input type="text" name="addressModal" id="addressModal" v-model="addDeliveryInfo.address"
                                            readonly="readonly">
                                        <button type="button" class="formBtn" id="searchAddressBtnModal"
                                            @click="searchAddress()">주소찾기</button>
                                    </div>
                                    <div class="input-address">
                                        <label for="addressDetailModal" class="sr-only">상세주소를 입력하세요</label>
                                        <div class="address_m-field">
                                        <label for="addressZipcodeModal" class="sr-only">우편번호</label>
                                            <input type="text" name="addressZipcodeModal" id="addressZipcodeModal" v-model="addDeliveryInfo.zipcode"
                                                readonly="readonly">
                                            <input type="text" name="addressDetailModal" id="addressDetailModal"
                                                v-model="addDeliveryInfo.addressDetail">
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <!-- 버튼그룹 -->
                        <div class="btn-box many">
                            <button type="button" class="blueBtn cancellation" @click="modalClose(7)"
                                v-if="viewMode == '3' || viewMode == '4'" id="deliveryCancelBtn1">
                                취소하기
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                                </span>
                            </button>
                            <button type="button" class="blueBtn cancellation" @click="setViewMode('1')" v-else
                                id="deliveryCancelBtn2">
                                취소하기
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                                </span>
                            </button>
                            <button type="button" class="blueBtn u-confirm" @click="saveNewDeliveryInfo()"
                                id="deliveryConfirmBtn">
                                확인하기
                                <span>
                                    <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                                </span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            <button type="button" class="closeBtn" @click="modalClose(3)" v-show="viewMode == '2' || viewMode == '4'">
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기">
            </button>
            <button type="button" class="closeBtn" @click="modalClose(5)" v-show="viewMode == '3'">
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기">
            </button>
        </div>
    </div>
</template>
<script>
module.exports = {
    props: {
        deliveryList: Array,
        viewMode: String,			// 1 : 배송지 목록, 2 : 배송지 추가(주문하기 화면), 3 : 배송지 수정, 4: 배송지 추가(배송지 관리)
        phoneCodes: Array,
        telCodes: Array,
        addDeliveryInfo: Object,
        deliveryOpenPage: String,
    },
    data: function () {
        return {
            isNoData: false,
            stepStatus: '1',			// 1 : 배송지 목록, 2 : 배송지 추가(주문하기 화면), 3 : 배송지 수정, 4: 배송지 추가(배송지 관리)
            selectedDeliveryId: 0,		// 배송지 목록에서 선택된 배송지 아이디
        }
    },
    components: {

    },
    methods: {
        goToNext: function () {
            //$("#add_modal").hide();
            // location.href = '/_pub/mypage/UI_P07160100.html';   // 주문/결제 페이지
            this.$refs.addModal.style.display = "none";
        },
        modalClose: function (number) {
            //$("#add_modal").hide();
            $("body").removeAttr("style");
            this.$refs.addModal.style.display = "none";
            let thisVue = this;
            try {
                if (number) {
                    setTimeout(() => {
                        if (thisVue.deliveryOpenPage == "step1" && number == 2) {
                            $("#deliveryListBtn").focus();
                        } else {
                            if (thisVue.viewMode == '2' || thisVue.viewMode == '4') {
                                $("#addDeliveryBtn").focus();
                            } else if (thisVue.viewMode == '3') {
                                if (number == 7) {
                                    $("#deliveryListBtn").focus();
                                } else {
                                    if (thisVue.addDeliveryInfo && thisVue.addDeliveryInfo.userDeliveryId) {
                                        thisVue.selectedDeliveryId = thisVue.addDeliveryInfo.userDeliveryId;
                                    }
                                    $("#modBtn" + thisVue.selectedDeliveryId).focus();
                                }
                            }
                        }
                    }, 100);
                }
            } catch (e) {
                $s.error(e);
            }

        },
        setViewMode: function (mode) {		// 1 : 배송지 목록, 2 : 배송지 추가
            let selectedData;
            if (mode == '3') {
                if (this.selectedDeliveryId) {
                    let length = this.deliveryList.length;
                    for (let i = 0; i < length; i++) {
                        let data = this.deliveryList[i];
                        if (data.userDeliveryId == this.selectedDeliveryId) {
                            selectedData = this.deliveryList[i];
                            if (selectedData.defaultFlag === "Y") {
                                selectedData.isDefault = true;
                            }
                            break;
                        }
                    }
                } else {
                    $s.alert('수정할 배송지를 선택해주세요.', 'modDeliveryInfo');
                    return;
                }
            } else if (mode == '1') {
                setTimeout(() => {
                    try {
                        $("#regDeliveryInfo").focus();
                    } catch (e) {
                        $s.error(e);
                    }
                }, 100);
            }
            this.$emit("set-view-mode", mode, selectedData);
        },
        searchAddress: function () {
            this.$emit("open-daum-postcode", "addDelivery");
        },
        saveNewDeliveryInfo: function () {          // todo :: me-058(등록 하시겠습니까?) me-059(수정 하시겠습니까?)   전화번호 입력 필드 추가
            if (!this.addDeliveryInfo.title) {		// 받는 사람 이름 체크
                $s.alert("배송지명을 입력해주세요.", "aNameModal");
                return;
            }
            if (!this.addDeliveryInfo.userName) {		// 받는 사람 이름 체크
                $s.alert("받으시는 분을 입력해주세요.", "reNameModal");
                return;
            }
            if (!this.addDeliveryInfo.mobile1) {
                $s.alert("휴대폰번호 앞 번호를 선택해주세요.", "phoneCodeModal");
                return;
            }
            if (!this.addDeliveryInfo.mobile2) {
                $s.alert("휴대폰번호 가운데 번호를 입력해주세요.", "phoneMidModal");
                return;
            }
            if (!this.addDeliveryInfo.mobile3) {
                $s.alert("휴대폰번호 마지막 번호를 입력해주세요.", "phoneLastModal");
                return;
            }
            if (!this.addDeliveryInfo.zipcode) {
                $s.alert("주문자 주소를 입력해주세요.", "searchAddressBtnModal");
                return;
            }
            if (!this.addDeliveryInfo.addressDetail) {
                $s.alert("주문자 주소 상세를 입력해주세요.", "addressDetailModal");
                return;
            }

            let msg = "";
            if (this.addDeliveryInfo.userDeliveryId) {
                msg = "수정 하시겠습니까?";
            } else {
                msg = "등록 하시겠습니까?";
            }

			if (!this.addDeliveryInfo.phone2 || !this.addDeliveryInfo.phone3) { // 전화번호 null insert (ex. 010-null-null) 방지
				this.addDeliveryInfo.phone1 = "";
			}

            let vue = this;
            $s.confirm(msg, function () {
                $s.closeAlert();
                vue.$emit("save-new-delivery-info", vue.addDeliveryInfo.userDeliveryId > 0);
            }, "deliveryConfirmBtn");
        },
        setDefaultDelivery: function () {            // todo :: me-041(배송지를 선택해 주세요.)
            this.$emit("set-default-delivery", this.selectedDeliveryId);
        },
        copyDeliveryInfoByList: function (type) {        // todo :: me-041(배송지를 선택해 주세요.)
            if (!this.selectedDeliveryId) {
                $s.alert("배송지를 선택해주세요.", "selectDeliveryBtn");
                return;
            }
            let selectedData;
            let length = this.deliveryList.length;
            for (let i = 0; i < length; i++) {
                let data = this.deliveryList[i];
                if (data.userDeliveryId == this.selectedDeliveryId) {
                    selectedData = this.deliveryList[i];
                    break;
                }
            }
            this.$emit("copy-delivery-info-by-list", selectedData);
            this.modalClose(1);
        },
    },
    mounted: function () {

    }
}
</script>
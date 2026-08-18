<template>
    <div id="goodsQnA_modal">
        <div class="modal_con">
            <div class="modal-title">
                답례품 Q&A 작성하기
            </div>
            <div class="modal-body">
                <div class="modal_body_wrap">
                    <div class="g_info_wrap">
                        <div class="g_info__img"><!-- <img src="/static/images/goods/g_info__img.png" alt=""> -->
                            <img :src="convertImgSize(targetItem.imageSrc)" alt="답례품 사진" @error="errorImage"
                                style="width: 70px;height: 70px;">
                        </div>
                        <div class="g_info__txt">
                            <div class="info_loc">{{ targetItem.locgovNm }}</div>
                            <div class="info_title">{{ unescapeHtml(targetItem.itemName) }}</div>
                            <div class="info_total">
                                <span class="deepBlue">{{ formatNumber(targetItem.salePrice) }}P</span>
                            </div>
                        </div>
                    </div>

                    <div class="reason_wrap">

                        <div class="reason_item">
                            <label class="item__label" for="name">이름</label>
                            <div class="item__select">
                                <input type="text" v-model="itemQna.userName" readonly id="name">
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="reason">문의유형 <span class="pointblue">*</span></label>
                            <div class="item__select">
                                <select name="" id="reason" v-model="itemQna.qnaGroup">
                                    <option value="">선택</option>
                                    <option v-for="(data, i) in qnaGroups" :value="data.id" :key="i">{{ data.label }}
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="reason_wrap">
                        <div class="reason_item">
                            <label class="item__label" for="qnaTit">제목 <span class="pointblue">*</span></label>
                            <div class="item__select">
                                <input type="text" placeholder="문의제목" id="qnaTit" v-model="itemQna.subject"
                                    @input="changeSubjectInput($event, 100)" maxlength="100"> <span
                                    class="s-txt">[{{ subjectLength }}/100]</span>
                            </div>
                        </div>
                        <div class="reason_item">
                            <label class="item__label" for="qnaTxt">내용 <span class="pointblue">*</span></label>
                            <div class="item__select">
                                <textarea name="" id="qnaTxt" v-model="itemQna.question" maxlength="1000"
                                    @input="changeSubjectInput($event, 1000)"></textarea> <!-- 기획서 5000자, db 1000자 -->
                                <span class="s-txt" style="float: right;">[{{ contentLength }}/1000]</span>
                                <div class="item__inner">
                                    <input type="checkbox" id="priv" v-model="isHidden"><label for="priv">비공개</label>

                                </div>
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
                        <button type="button" class="blueBtn u-confirm" @click="goToNext()" id="modalQnaSaveBtn">
                            저장
                            <span>
                                <img src="/static/images/icon/cli-icon_btn-hover-arrow.png" alt="">
                            </span>
                        </button>
                    </div>

                </div>
            </div>
            <button type="button" class="closeBtn" @click="modalClose()">
                <img src="/static/images/icon/cli-icon_btn-close-modal.png" alt="닫기"
                    @keyup.enter="modalClose()">
            </button>
        </div>
    </div>
</template>
<style scoped></style>
<script>
module.exports = {
    props: {
        statusBtn: {
            type: String,
            required: false,
            default: function () {
                return '';
            },
        },
        itemQna: Object,
        qnaGroups: Array,
        targetItem: Object,
    },
    data: function () {
        return {
            subjectLength: 0,
            contentLength: 0,
        }
    },
    methods: {
        goToNext: function () {
            if (!this.itemQna.qnaGroup) {
                $s.alert("문의유형을 선택해주세요.", "reason");
                return;
            }
            if (!this.itemQna.subject) {
                $s.alert("제목을 입력해주세요.", "qnaTit");
                return;
            }
            if (!this.itemQna.question) {
                $s.alert("내용을 입력해주세요.", "qnaTxt");
                return;
            }
            this.$emit('register-qna');
        },

        modalClose: function (isSave) {
            $("#goodsQnA_modal").hide();
            try {
                if (!isSave) {
                    setTimeout(() => {
                        $("#qnaWriteBtn").focus();
                    }, 100);
                }
            } catch (e) {
                $s.log(e);
            }
        },
        changeSubjectInput: function (e, length) {
            //let txt = e.target.value;
            let txtLength = e.target.value.length;
            // if (txtLength > length) {
            //     if (e.target.id === 'qnaTit') {
            //         this.subjectLength = length;
            //     }
            //     //e.target.value = txt.substring(0, length);
            // } else {
            if (e.target.id === 'qnaTit') {
                this.subjectLength = txtLength;
            } else if (e.target.id === 'qnaTxt') {
                this.contentLength = txtLength;
            }
            // }
        },
        convertImgSize: function (imgSrc) {
            try {
                if (!imgSrc) {
                    return "";
                }
                let splitList1 = imgSrc.split('.');
                let splitList2 = imgSrc.split('_');
                let splitList1Length = splitList1.length;
                let splitList2Length = splitList2.length;
                let ext = splitList1[splitList1Length - 1];
                let fileName = '';
                for (let i = 0; i < splitList2Length; i++) {
                    if (i < splitList2Length - 1) {
                        if (i > 0) {
                            fileName += '_';
                        }
                        fileName += splitList2[i];
                    }
                }
                return this.itemImage(fileName + "_XS." + ext);
            } catch (e) {
                $s.log(e);
                return "";
            }
        },
    },
    computed: {
        isHidden: {
            get: function () {
                return this.itemQna.secretFlag === "Y" ? true : false;
            },
            set: function (newValue) {
                if (newValue) {
                    this.itemQna.secretFlag = 'Y';
                } else {
                    this.itemQna.secretFlag = 'N';
                }
            },
        }
    },
    mounted: function () {

    },
}
</script>
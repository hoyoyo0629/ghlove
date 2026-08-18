<template>
    <header id="header_g" class="header2">
        <nav class="gnb">
            <div class="center">
                <div class="gnb_slider_m">
                    <div class="cathetory_main">
                        <a href="#" class="total_menu" @click="allMenu()">
                            <span class="total_menu_h">
                                <img :src="menuImageSrc" alt="전체메뉴열기">
                            </span>
                            <span>전체 카테고리</span>
                        </a>
                        <span class="v-line"></span>
                        <ul class="top_menu">
                            <li class="" :class="showGnbTargetFlag('evevt') ? 'on' : ''">
                                <a href="/featured/event.html">지역 이벤트</a>
                            </li>
                            <li class="" :class="showGnbTargetFlag('seasonal') ? 'on' : ''">
                                <a href="/event/seasonal.html">제철식품관</a>
                            </li>
                            <li class="" :class="showGnbTargetFlag('special') ? 'on' : ''">
                                <a href="/event/special.html">특산물관</a>
                            </li>
                        </ul>
                    </div>


                    <span class="goods_search">
                        <input type="text" id="g_search" @keydown='keydown' v-model="searchKeyword"
                            placeholder="답례품 검색">
                        <button @click.prevent="search">
                            <img src="/static/images/icon/search-white.png" alt="검색하기" class="icon-img">
                        </button>
                    </span>
                    <span class="local_shop" @click="selectMenuOpen($event)">
                        <a href="javascript:void(0)" class="shop_select">
                            <img src="/static/images/goods/cli-icon_local-loc.png" alt="">
                            <span class="locNameArea">
                                {{ upperLocSearchTxt }}
                                {{ locSearchTxt }}
                            </span>
                            <img src="/static/images/goods/cli-icon_local-arrow.png" alt="펼쳐보기">
                        </a>
                        <span class="goods_loc_search" @click.stop="">
                            <select name="upperLocgovCode" id="upperLocgovCode" v-model="sidoCode"
                                @change="changeSidoEvent">
                                <option v-for="(sido, index) in sidoList" :value="sido.sidoCode" :key="index">
                                    {{ sido.sidoName }}
                                </option>
                            </select>
                            <select name="locgovCodeSet" id="locgovCodeSet" v-model="sigunguCode"
                                @change="changeSigunguEvent">
                                <option v-for="(sigungu, index) in sigunguList" :value="sigungu.cityCode" :key="index">
                                    {{ sigungu.cityName }}
                                </option>
                            </select>
                            <span class="btn-box">
                                <button type="button" class="formBtn" @click="this.$s.redirect('/goods/index.html')">통합몰
                                    가기</button>
                                <button type="button" class="formBtn loc" @click="searchLocgov">지자체몰 가기</button>
                            </span>
                            <span class="s-txt">지역을 선택하시면 해당 지역의 지자체몰로 이동합니다.</span>
                            <span class="loc_search_del">
                                <button type="button" @click="selectMenuClose()">
                                    <img src="/static/images/icon/loc_search_del.png" alt="지자체별 답례품 검색창 닫기">
                                </button>
                            </span>
                        </span>
                    </span>

                </div>
            </div>
        </nav>
        <nav class="main_lnb" id="allMenu">
            <div class="center">
                <ul class="top_category">
                    <li v-for="(category, i) in category.groups" :key="i">
                        <a href="" @mouseover.stop="overEvt(category)"
                            @click.prevent="categoryGroupLink(category.url, category.name)">{{
                                category.name
                            }}
                            <span>
                                <img src="/static/images/icon/cli-icon_btn-illust-arrow1.png" alt="">
                            </span>
                        </a>
                    </li>
                </ul>
                <div class="detail_cate" :style="dpOn">
                    <ul class="middle_category" v-for="(category, i) in topCategory" :key="i">
                        <li>
                            <a href="" @click.prevent="categoryLink(category.url, category.name)">{{
                                category.name
                            }}</a>
                            <ul class="detail_category">
                                <li v-for="(childCategory, j) in category.childCategories" :key="j">
                                    <a href="" @click.prevent="categoryLink(childCategory.url, childCategory.name)">{{
                                        childCategory.name
                                    }}</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>
</template>

<style scoped>
input[type='text'] {
    background: transparent;
    border: none;
    color: #fff;
    font-size: 16px;
    outline: 1px;
}

::placeholder {
    color: #fff;
}
</style>
<script type="text/babel">
module.exports = {
    props: {
        newCartQuantity: 0,
        menuUrl: {
            type: String,
            required: true,
            default: function () {
                return '';
            }
        },
    },
    data: function () {
        return {
            getMenuUrl: this.menuUrl,
            topCategory: [
                /*{ topCate: "농산물", category: "과일", },
                { topCate: "수산물", category: "채소", },
                { topCate: "축산물", category: "쌀/잡곡/견과", },
                { topCate: "가공식품", category: "가공식품", },
                { topCate: "생활용품", category: "생활용품", },
                { topCate: "건강식품", category: "기타", }*/
            ],
            /*detailsCategory: [
                ["쌀", "잡곡", "과일", "버섯", "채소/나물/야채", "견과류", "농산물 기타"],
                ["생선", "건어물", "김/해초", "해산물/어류", "젓갈", "수산물 기타"],
                ["쌀", "잡곡", "과일", "버섯", "채소/나물/야채", "견과류", "농산물 기타"],
                ["가공식품농산", "가공식품수산", "가공식품기타", "김치", "장류", "떡류", "소금", "즙류", "차류", "가공기타"],
                ["주방용품", "거실용품"],
                ["기타"],

            ],*/
            upperLocSearchTxt: '지자체몰',
            locSearchTxt: '선택하기',

            searchTxt: '',
            DmenuUrl: "/goods/index.html",

            cartQuantity: 0,
            isLogin: false,
            isGuestLogin: false,
            category: {
                groups: []
            },
            searchWord: '',
            latelySearch: [],
            bestSearchWord: null,
            latelyItems: [],
            recommendSearch: {},
            dpOn: { display: "none" },
            outDp: { display: "block" },
            sidoCode: "",
            sigunguCode: "",
            selectSigunguName: "",
            sidoList: [],
            sigunguList: [],
            isOpenMainLnb: false,
            searchKeyword: '',
        }
    },
    computed: {
        menuImageSrc() {
            return this.isOpenMainLnb ? '/static/images/goods/all-menu_close.png' : '/static/images/goods/all-menu_open.png';
        },
    },
    methods: {
        getPageUrl: function (searchTxt) {
            // console.log(this.getMenuUrl);
            // if (menuUrl != DmenuUrl) {
            //       return searchTxt = '지자체 답례품 검색'
            // } else {
            //       return searchTxt = '답례품 검색'
            // }
            return searchTxt = '답례품 검색'
        },
        allMenu: function () {
            if (!this.isOpenMainLnb) {
                $("#allMenu").stop().slideDown('fast');
                this.isOpenMainLnb = true;
            } else {
                $("#allMenu").stop().slideUp('fast');
                this.isOpenMainLnb = false;
            }
        },
        overEvt: function (category) {
            this.dpOn.display = "flex";
            this.topCategory = category.categories;
        },
        link: function (url, childCategories) {
            var flag = typeof childCategories != 'undefined'
                && childCategories != null
                && childCategories.length > 0;

            if (!flag) {
                $s.redirect('/category/?code=' + url);
            }
        },
        search: function () {
            if (!this.searchKeyword) {
                $s.alert("검색할 단어를 입력해주세요.");
                return;
            }
            this.$emit("search-keyword", this.searchKeyword);		// 부모요소 함수 호출
        },
        getBestSearchWord: function () {
            var self = this
            $s.api.getBestKeyword(function (response) {
                self.bestSearchWord = response.list;
            }, function () {
                $s.alert(error.response.data.message);
            });
        },
        getRecommendSearchWord: function () {
            var self = this
            $s.api.getRecommendKeyword(function (response) {
                self.recommendSearch = response.search;
            }, function () {
                $s.alert(error.response.data.message);
            });
        },
        showGnbTargetFlag: function (target) {
            return this.gnbMenuTarget == target;
        },
        categoryLink: function (value, valueName) {
            this.$emit("search-category", value, valueName);		// 부모요소 함수 호출
        },
        categoryGroupLink: function (value, valueName) {
            this.$emit("search-category-group", value, valueName);		// 부모요소 함수 호출
        },
        keydown: function (e) {
            if (e.keyCode == 13) {
                this.search();
            }
        },
        // 지자체별 답례품 보기
        selectMenuOpen: function (e) {
            ``
            $(".goods_loc_search").toggle();
        },
        selectMenuClose: function () {

            $(".goods_loc_search").hide();
        },
        changeSidoEvent: function () {
            this.loadSiGungu().then((res) => {
                this.sigunguList = res;
            });
        },
        changeSigunguEvent: function () {
        },
        searchLocgov: function () {
            const sido = this.sidoList.find((v, i, arr) => { return v.sidoCode === this.sidoCode });
            const sigungu = this.sigunguList.find((v, i, arr) => { return v.cityCode === this.sigunguCode });

            this.upperLocSearchTxt = sido.sidoName;
            this.locSearchTxt = sigungu.cityName;
            this.$emit("search-locgov", this.sigunguCode, this.selectSigunguName);		// 부모요소 함수 호출
        },
        initData(jsonValues) {
            this.sidoCode = jsonValues.sidoCode;
            this.sigunguCode = jsonValues.sigunguCode;
        },
        loadSido() {
            return new Promise((resolve, reject) => {
                $s.api.getSido('',
                    function (response) {
                        let data = [{ sidoCode: '', sidoName: '시·도 선택' }];
                        response.resultList.sidoList.forEach((v, i, arr) => {
                            data.push({ sidoCode: v.sidoCode, sidoName: v.sidoName });
                        });
                        resolve(data);
                    },
                    function (error) {
                        $s.api.handleApiExeption(error);
                        reject(error);
                    });
            });
        },
        loadSiGungu() {
            return new Promise((resolve, reject) => {
                $s.api.getSiGunGu({ sidoCode: this.sidoCode },
                    function (response) {
                        let data = [{ cityCode: '', cityName: '시·군·구 선택' }];
                        response.resultList.cityList.forEach((v, i, arr) => {
                            data.push({ cityCode: v.cityCode, cityName: v.cityName });
                        });
                        resolve(data);
                    },
                    function (error) {
                        $s.api.handleApiExeption(error);
                    });
            });
        },
        handleWindowResize() {
            $("#allMenu").stop().slideUp('fast');
            this.isOpenMainLnb = false;
        },
    },
    mounted: function () {
        window.addEventListener('resize', this.handleWindowResize);
        this.$nextTick(function () {
            this.categoryInfo(this);
            this.loadSido().then((res) => {
                this.sidoList = res;
            });
            this.sigunguList = [{ cityCode: '', cityName: '시·군·구 선택' }];
        });
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.handleWindowResize);
    }
};
</script>

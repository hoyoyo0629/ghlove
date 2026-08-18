<template>
    <div>
        <nav class="goods_lnb">
            <div class="tabbar">
                <div class="center">
                    <a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="menuField($event)" id="btn01">
                        <span class="tabbar_icon"></span>
                        <span class="tabbar_txt">카테고리</span>
                    </a>
                    <!-- <a href="" @click.prevent="keywordLink" data-name="tabbar_icon-goods_search-on"> -->
                    <a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="searchField($event)" id="btn02">
                        <span class="tabbar_icon"></span>
                        <span class="tabbar_txt">답례품검색</span>
                    </a>
                    <a :href="urlSet.mall" :class="{ 'on': menuUrl == urlSet.mall }">
                        <span class="tabbar_icon"></span>
                        <span class="tabbar_txt">답례품몰</span>
                    </a>
                    <!-- <a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="showSidoList"> -->
                    <!--<a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="selectField($event)" id="btn04">-->
                    <a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="showMapSelect" id="btn04">
                        <span class="tabbar_icon"></span>
                        <span class="tabbar_txt">지자체별</span>
                    </a>
                    <a href="javascript:void(0);" :class="{ 'on': false }" @click="getMyPage()">
                        <span class="tabbar_icon"></span>
                        <span class="tabbar_txt">마이페이지 </span>
                    </a>
                </div>
            </div>
        </nav>
        <div class="g_category" id="goodsMenu">
            <div class="cathegory_wrap">
                <div class="cath_header">
                    <div class="center">
                        <span class="menu_tit">전체 카테고리</span>
                        <span class="close_btn" @click="close_area()">
                            <button type="button"><img class="icon-img" src="/static/images/icon/btn_close-modal.png"
                                    alt="카테고리 닫기"></button>
                        </span>
                    </div>
                </div>
                <div class="cath_body">
                    <nav class="cathe">
                        <div class="center">
                            <ul class="top_category">
                                <li v-for="(categoryGroup, i) in category.groups" :key="i" tabindex="0">
                                    <a href="javascript:void(0);" @click.prevent="clickEvt($event, categoryGroup)"
                                        :id="`firstMenu_${i}`">{{ categoryGroup.name }}
                                        <span><img src="/static/images/icon/cli-icon_btn-illust-arrow1.png" alt=""></span>
                                    </a>
                                    <ul class="middle_category" :style="dpOn">
                                        <li>
                                            <a href="javascript:void(0);"
                                                @click.prevent="categoryGroupLink(categoryGroup.url, categoryGroup.name)">전체보기</a>
                                        </li>
                                        <li v-for="(category, j) in topCategory" :key="j" tabindex="0">
                                            <a href="javascript:void(0);"
                                                @click.prevent="categoryLink(category.url, category.name)">{{ category.name
                                                }}</a>
                                            <ul class="detail_category">
                                                <li v-for="(childCategory, k) in category.childCategories" :key="k"
                                                    tabindex="0">
                                                    <a href="javascript:void(0);"
                                                        @click.prevent="categoryLink(childCategory.url, childCategory.name)">{{
                                                            childCategory.name }}</a>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </div>

            </div>
        </div>
        <div class="loc_search" id="goodsSearch">
            <div class="loc_search_area">
                <span class="close_area" @click="close_area()"><button type="button"><img class="icon-img"
                            src="/static/images/icon/tabbar_close_arrow.png" alt="답례품 검색 닫기"></button>
                </span>
                <span class="toggle_tit">전체 답례품 검색</span>
                <span class="goods_search" tabindex="0">
                    <input type="text" placeholder="답례품 검색" @keydown='keydown' v-model="searchKeyword">
                    <a href="javascript:void(0);" @click.prevent="search">
                        <img src="/static/images/icon/tab_gsearch-on.png" alt="검색하기">
                    </a>
                </span>
            </div>
        </div>
        
    </div>
</template>

<style scoped>
.tabbar a {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    color: var(--gray3);
}

.tabbar a .tabbar_icon {
    display: block;
    width: 26px;
    height: 26px;
    background-size: 26px auto;
    background-position: center center;
    background-repeat: no-repeat;
}

.tabbar a:nth-child(1) .tabbar_icon {
    background-image: url('/static/images/icon/tab_category.png');
}

.tabbar a:nth-child(2) .tabbar_icon {
    background-image: url('/static/images/icon/tab_gsearch.png');
}

.tabbar a:nth-child(3) .tabbar_icon {
    background-image: url('/static/images/icon/tab_gmall.png');
}

.tabbar a:nth-child(4) .tabbar_icon {
    background-image: url('/static/images/icon/tab_locsearch.png');
}

.tabbar a:nth-child(5) .tabbar_icon {
    background-image: url('/static/images/icon/tab_my.png');
}

/* .tabbar a:hover, */
.tabbar a.on {
    color: var(--main-color2);
}

/* .tabbar a:hover:nth-child(1) .tabbar_icon, */
.tabbar a.on:nth-child(1) .tabbar_icon {
    background-image: url('/static/images/icon/tab_category-on.png');
}

/* .tabbar a:hover:nth-child(2) .tabbar_icon, */
.tabbar a.on:nth-child(2) .tabbar_icon {
    background-image: url('/static/images/icon/tab_gsearch-on.png');
}

/* .tabbar a:hover:nth-child(3) .tabbar_icon, */
.tabbar a.on:nth-child(3) .tabbar_icon {
    background-image: url('/static/images/icon/tab_gmall-on.png');
}

/* .tabbar a:hover:nth-child(4) .tabbar_icon, */
.tabbar a.on:nth-child(4) .tabbar_icon {
    background-image: url('/static/images/icon/tab_locsearch-on.png');
}

/* .tabbar a:hover:nth-child(5) .tabbar_icon, */
.tabbar a.on:nth-child(5) .tabbar_icon {
    background-image: url('/static/images/icon/tab_my-on.png');
}

input[type='text'] {
    background: transparent;
    border: none;
    color: var(--main-color2);
    font-size: 16px;
    /* outline: 1px; */
}
</style>
<script>
$(document).ready(function () {
    let currentPageTitle = '';
    let tabbarMenu = $(".tabbar a");
    let imgName = tabbarMenu.attr("data-name");
    // switch (currentPageTitle) {
    //     case "답례품검색":
    //         tabbarMenu.eq(1).children("img").attr("src","/static/images/goods/tabbar_icon-total_menu-on.png").siblings("span").css("color","var(--main-color2)");
    //         break;

    //         case "답례품몰":
    //         tabbarMenu.eq(2).children("img").attr("src","/static/images/goods/tabbar_icon-goods_search-on.png").siblings("span").css("color","var(--main-color2)");
    //         break;

    //         case "지자체별":
    //         tabbarMenu.eq(3).children("img").attr("src","/static/images/goods/tabbar_icon-loc_store-on.png").siblings("span").css("color","var(--main-color2)");

    //         break;

    //         case "장바구니":
    //         tabbarMenu.eq(4).children("img").attr("src","/static/images/goods/tabbar_icon-cart-on.png").siblings("span").css("color","var(--main-color2)");
    //         break;

    //     default:
    //         break;
    // }

});
module.exports = {
    props: {
        menuUrl: {
            type: String,
            default: function () {
                return '';
            }
        },
    },
    data: function () {
        return {
            goodsMenu: [
                {
                    depth: 1, name: "관광서비스", url: "#", detailYN: "Y", detailItem: [
                        {
                            depth: 2, name: "전체보기", url: "http://m.naver.com", detailYN: "N",
                        },
                        {
                            depth: 2, name: "여행/숙박", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "여행", url: "http://m.naver.com", detailYN: "N", },
                                { depth: 3, name: "숙박", url: "http://m.google.com", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "티켓/공연", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "티켓/공연", url: "#", detailYN: "N", },
                                { depth: 3, name: "티켓", url: "#", detailYN: "N", },
                                { depth: 3, name: "공연", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "지역특화서비스", url: "#", detailYN: "N", detailItem: [
                                // { depth: 3, name: "지역특화서비스", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "기타", url: "#", detailYN: "N", detailItem: [
                                // { depth: 3, name: "기타", url: "#", detailYN: "N", },
                            ]
                        },
                    ]
                },
                {
                    depth: 1, name: "지역상품권", url: "#", detailYN: "N", detailItem: [
                        {
                            depth: 2, name: "전체보기", url: "#", detailYN: "N",
                        },
                    ]
                },
                {
                    depth: 1, name: "농산/축산물", url: "#", detailYN: "Y", detailItem: [
                        {
                            depth: 2, name: "전체보기", url: "#", detailYN: "N",
                        },
                        {
                            depth: 2, name: "곡물", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "곡물", url: "#", detailYN: "N", },
                                { depth: 3, name: "쌀류", url: "#", detailYN: "N", },
                                { depth: 3, name: "잡곡/홈합곡류", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "과일/견과", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "견과", url: "#", detailYN: "N", },
                                { depth: 3, name: "과일류", url: "#", detailYN: "N", },
                                { depth: 3, name: "건과일류", url: "#", detailYN: "N", },
                                { depth: 3, name: "견과류", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "채소/버섯", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "버섯", url: "#", detailYN: "N", },
                                { depth: 3, name: "채소류", url: "#", detailYN: "N", },
                                { depth: 3, name: "버섯류", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "한우", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "한우", url: "#", detailYN: "N", },
                                { depth: 3, name: "세트류", url: "#", detailYN: "N", },
                                { depth: 3, name: "구이류", url: "#", detailYN: "N", },
                                { depth: 3, name: "국거리/사골류", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "한돈", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "한돈", url: "#", detailYN: "N", },
                                { depth: 3, name: "세트류", url: "#", detailYN: "N", },
                                { depth: 3, name: "구이류", url: "#", detailYN: "N", },
                                { depth: 3, name: "국거리/사골류", url: "#", detailYN: "N", },
                                { depth: 3, name: "찜/불고기류", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "닭/달걀", url: "#", detailYN: "Y", detailItem: [
                                { depth: 3, name: "닭/달걀", url: "#", detailYN: "N", },
                                { depth: 3, name: "닭고기류", url: "#", detailYN: "N", },
                                { depth: 3, name: "달걀", url: "#", detailYN: "N", },
                            ]
                        },
                        {
                            depth: 2, name: "기타", url: "#", detailYN: "N", detailItem: [
                                { depth: 2, name: "기타", url: "#", detailYN: "N", detailItem: [] },
                            ]
                        },
                    ]
                },
            ],
            dpOn: { display: "none" },
            // outDp: { display: "block" },
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
            // gnbSwiperOption: {
            //     slidesPerView: 'auto',
            //     spaceBetween: 40,
            //     navigation: {
            //         nextEl: '.swiper-contents .swiper-button-next',
            //         prevEl: '.swiper-contents .swiper-button-prev',
            //     },
            //     sliderMove: 1,
            //     on: {
            //         init: function () {
            //             if ($(".gnb_slider_pc .swiper-button-prev").is(".swiper-button-disabled")) {
            //                 $(".gnb_slider_pc .swiper-wrapper").removeClass("prv_v");
            //             } else {
            //                 $(".gnb_slider_pc .swiper-wrapper").addClass("prv_v");
            //             }
            //             if ($(".gnb_slider_pc .swiper-button-next").is(".swiper-button-disabled")) {
            //                 $(".gnb_slider_pc .swiper-wrapper").removeClass("nxt_v");
            //             } else {
            //                 $(".gnb_slider_pc .swiper-wrapper").addClass("nxt_v");
            //             }
            //         }
            //     }
            // },
            selectedLocgov: {},
            urlSet: {
                mall: '/goods/index.html',
            },
            upperLocSearchTxt: '지자체몰',
            locSearchTxt: '선택하기',
            sidoList: [],
            sigunguList: [],
            searchKeyword: '',
            sidoCode: '',
            sigunguCode: '',
            topCategory: [],
        }
    },
    computed: {
        displayCartQuantites: function () {
            return this.newCartQuantity > this.cartQuantity ? this.newCartQuantity : this.cartQuantity;
        },
        showLogin: function () {
            return !(this.isLogin || this.isGuestLogin);
        }
    },
    methods: {
        getCartInfo: function () {
            var self = this;
            // $s.api.getCartInfo(function (response) {
            //     self.cartQuantity = response.cartQuantity;
            // }, function (error) {
            //     $s.alert(error.response.data.message);
            // });
        },
        logout: function () {
            $s.logout();
        },
        link: function (url, childCategories) {
            var flag = typeof childCategories != 'undefined'
                && childCategories != null
                && childCategories.length > 0;

            if (!flag) {
                $s.redirect('/category/?code=' + url);
            }
        },
        // 모바일 탭바 - 카테고리
        menuField(e) {
            $("#goodsMenu").toggle();
            $("#locSearch, #goodsSearch").hide();
            $('#btn02, #btn04').removeClass('on');
            //console.log( e.target);

            let tTarget = e.target;
            $(tTarget).parent('a').toggleClass('on');

            var isShow = $("#goodsMenu").is(':visible');
            if (isShow) {
                $("#firstMenu_0").addClass('target').parent().siblings().children('a').removeClass('target');
                $("#firstMenu_0").siblings('.middle_category').css('display', 'flex').parent().siblings().children('.middle_category').css('display', 'none');
                this.topCategory = this.category.groups[0].categories;
            }
        },
        clickEvt: function (e, category) {
            // this.dpOn.display = "flex";
            //   this.topCategory = category.categories;
            let tTarget = e.target;
            // tTarget = $(tTarget).parent().index();
            console.log(tTarget);
            // $('.middle_category').css('display','none');
            $(tTarget).addClass('target').parent().siblings().children('a').removeClass('target');
            $(tTarget).siblings('.middle_category').css('display', 'flex').parent().siblings().children('.middle_category').css('display', 'none');

            this.topCategory = category.categories;
        },
        // 모바일 탭바 - 카테고리
        searchField(e) {
            $("#goodsSearch").toggle();
            $("#locSearch, #goodsMenu").hide();
            $('#btn01, #btn04').removeClass('on');
            // console.log( e.target);
            let tTarget = e.target;
            $(tTarget).parent('a').toggleClass('on');
        },
        selectField(e) {
            $("#locSearch").toggle();
            //$("#goodsSearch, # ").hide();
            $('#btn01, #btn02').removeClass('on');
            // console.log( e.target);
            let tTarget = e.target;
            $(tTarget).parent('a').toggleClass('on');
        },
        close_area() {
            $(".loc_search, .g_category").hide();
            $('#btn01, #btn02, #btn04').removeClass('on');
        },
        search: function () {
            if (!this.searchKeyword) {
                $s.alert("검색할 단어를 입력해주세요.");
                return;
            }
            $goods.goSearchGoods({ type: 'T', keyword: encodeURIComponent(this.searchKeyword) });
        },
        keydown: function (e) {
            if (e.keyCode == 13) {
                this.search();
            }
        },

        categoryLink: function (categoryCode, categoryName) {		// 2~3차 카테고리 검색
            //test
            categoryCode = "fruit";
            categoryName = "과일";
            //test
            this.$emit("search-category", categoryCode, categoryName);		// 부모요소 함수 호출
        },
        categoryGroupLink: function (categoryGroupCode, categoryGroupName) {		// 1차 카테고리 검색
            //test
            categoryGroupCode = "farm";
            categoryGroupName = "농산물";
            //test
            this.$emit("search-category-group", categoryGroupCode, categoryGroupName);		// 부모요소 함수 호출
        },
        keywordLink: function () {		// 키워드 검색
            //test
            this.searchWord = "사과";
            //test
            this.$emit("search-keyword", this.searchWord, this.searchWord);		// 부모요소 함수 호출
        },
        /*locgovLink: function(locgovCode, locgovName) {		// 지자체 검색
            this.$emit("search-locgov", locgovCode, locgovName);		// 부모요소 함수 호출
        },*/

        showSidoList: function () {		// 테스트
            this.selectSido(this.sidoList[1].sidoCode, this.sidoList[1].sidoName);		// 첫번째(시도를 선택하세요.)는 코드값이 없어서 2번째 값으로 테스트..
            //this.selectSido('42000', '강원도');
        },
        openPresent: function (type) {
            if (type == 'sbag') {
                $s.redirect("/cart/index.html");
                // $s.redirect("/_pub/cart/UI_P07150000.html");
            } else {
                $s.redirect("/goods/index.html");
            }
        },
        getMyPage: function () {
            this.isLogin = $s.isLogin();
            if (this.isLogin) {
                location.href = "/mypage/index.html";
            } else {
                this.redirect($s.pages.LOGIN + "?target=" + "/mypage/index.html");

            }
        },
        changeSidoEvent: function () {
            this.sigunguCode = '';
            return new Promise((resolve, reject) => {
                this.loadSiGungu().then((res) => {
                    this.sigunguList = res;
                    resolve(this.sigunguList);
                }).catch((e) => {
                    reject(e);
                });
            });

        },
        changeSigunguEvent: function () {
        },
        searchLocgov: async function () {
            if (!this.sidoCode) {
                $s.alert("시·도를 선택하세요.");
                return;
            }

            /*if (!this.sigunguCode) {
                $s.alert("시·군·구를 선택하세요.");
                return;
            }*/

            var locgovCode;
            // 시·도만 선택하면 시·도 코드를 파라미터로 넘김
            if (!this.sigunguCode) {
            	locgovCode = "U"+this.sidoCode;
            } else {
            	locgovCode = this.sigunguCode;
            }

            const sido = this.getCurrentSidoName();
            //const sigungu = this.getCurrentSigunguName();
            this.upperLocSearchTxt = sido;
            //this.locSearchTxt = sigungu;
            $goods.goSearchGoods({ type: 'L', locgov: locgovCode });
        },
        async initData(jsonValues) {
            this.type = jsonValues.type;

            this.loadSido().then((res) => {
                this.sidoList = res;
            });

            if (this.type === 'L') {
                this.sidoCode = `${jsonValues.sigunguCode.replace("U", "").substring(0, 2)}000`;
                await this.changeSidoEvent();
                if (!jsonValues.sigunguCode.includes("U")) { this.sigunguCode = jsonValues.sigunguCode; }
                const sido = this.getCurrentSidoName();
                var sigungu = "";
                this.upperLocSearchTxt = sido;

                if (jsonValues.sigunguCode.includes("U")) {
                	sigungu = "";
                	this.locSearchTxt = "";
                } else {
                	sigungu = this.getCurrentSigunguName();
                	this.locSearchTxt = sigungu;
                }
                return { sido, sigungu };
            } else if (this.type === 'T') {
                this.searchKeyword = jsonValues.keyword;
            }

            return null;
        },
        loadSido() {
            return $goods.getCommonRegionCode();
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
                        reject(error);
                    });
            });
        },
        getCurrentSidoName() {
            const sido = this.sidoList.find((v, i, arr) => { return v.sidoCode === this.sidoCode });
            return sido ? sido.sidoName.trim() : '';
        },
        getCurrentSigunguName() {
            const sigungu = this.sigunguList.find((v, i, arr) => { return v.cityCode === this.sigunguCode });
            return sigungu ? sigungu.cityName : '';
        },
        categoryLink: function (value, valueName) {
            $s.redirect(`/goods/searchGoods.html?category=${value}&type=C`);
        },
        categoryGroupLink: function (value, valueName) {
            $s.redirect(`/goods/searchGoods.html?group=${value}&type=C`);
        },
        showMapSelect: function () {
            this.$emit('show-map-select');
        },
        searchLocgovMap: function (lclgvUpperCd, lclgvCd, lclgvUpperNm, lclgvNm) {
            if (lclgvUpperCd) {
                this.sidoCode = lclgvUpperCd;
                this.sigunguCode = lclgvCd;
                /*this.upperLocSearchTxt = lclgvUpperNm;
                this.locSearchTxt = lclgvNm;*/
                this.searchLocgov();
            } else {
                $s.redirect('/goods/index-main-map.html');
            }
        },
    },
    mounted: function () {
        this.$nextTick(function () {
            this.isLogin = $s.isLogin();
            this.isGuestLogin = $s.isGuestLogin();

            //this.getCartInfo();
            this.categoryInfo(this);
            //this.latelyInfo(this);
            //this.latelyItemInfo(this);
            // this.saveVisitData();

            //initializeHeaderEvent();
            this.loadSido().then((res) => {
                this.sidoList = res;
            });
            this.sigunguList = [{ cityCode: '', cityName: '시·군·구 선택' }];
        });
    },
};
</script>

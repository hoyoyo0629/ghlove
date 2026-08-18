<template>
    <div>
        <nav class="goods_lnb">
            <div class="tabbar">
                <div class="center">
                    <a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="searchField($event)" id="btn02">
                        <span class="tabbar_icon prj-search"></span>
                        <span class="tabbar_txt">특정사업기부 검색</span>
                    </a>
                    <a :href="exportMode ? urlSet.mall + '?exportMode=true' : urlSet.mall" :class="{ 'on': menuUrl == urlSet.mall }">
                        <span class="tabbar_icon main"></span>
                        <span class="tabbar_txt">특정사업기부 메인</span>
                    </a>
                    <a href="javascript:void(0);" :class="{ 'on': false }" @click.prevent="selectField($event)" id="btn04" style="display:none;">
                        <span class="tabbar_icon loc-search"></span>
                        <span class="tabbar_txt">지자체 검색</span>
                    </a>
                </div>
            </div>
        </nav>

        <div class="loc_search" id="goodsSearch">
            <div class="loc_search_area">
                <span class="close_area" @click="close_area()"><button type="button"><img class="icon-img"
                            src="/static/images/icon/tabbar_close_arrow.png" alt="검색 닫기"></button>
                </span>
                <span class="toggle_tit">특정사업기부 검색</span>
                <span class="goods_search" tabindex="0">
                	<label for="total-search" style="min-width: 120px;text-align: center;">특정사업기부 검색</label>
                    <input type="text" placeholder="검색어를 입력하세요." title="검색어를 입력하세요." @keydown='keydown' v-model="searchKeyword" id="total-search">
                    <a href="javascript:void(0);" @click.prevent="searchInput">
                        <img src="/static/images/icon/tab_gsearch-on.png" alt="검색하기">
                    </a>
                </span>
            </div>
        </div>

    </div>
</template>

<style scoped>
.goods_lnb .center{
    justify-content: center;
    gap: 50px;
}
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

.tabbar a .tabbar_icon.prj-search {
    background-image: url('/static/images/icon/tab_gsearch.png');
}

.tabbar a .tabbar_icon.main {
    background-image: url('/static/images/icon/tab_gmall.png');
}

.tabbar a .tabbar_icon.loc-search {
    background-image: url('/static/images/icon/tab_locsearch.png');
}

.tabbar a.on {
    color: var(--main-color2);
}


/* .tabbar a:hover:nth-child(2) .tabbar_icon, */
.tabbar a.on .tabbar_icon.prj-search {
    background-image: url('/static/images/icon/tab_gsearch-on.png');
}

/* .tabbar a:hover:nth-child(3) .tabbar_icon, */
.tabbar a.on .tabbar_icon.main {
    background-image: url('/static/images/icon/tab_gmall-on.png');
}

/* .tabbar a:hover:nth-child(4) .tabbar_icon, */
.tabbar a.on .tabbar_icon.loc-search {
    background-image: url('/static/images/icon/tab_locsearch-on.png');
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

});
module.exports = {
    props: {
        menuUrl: {
            type: String,
            default: function () {
                return '';
            }
        },
        exportMode: {
            type: Boolean,
            default: function () {
                return false;
            },
        },
    },
    data: function () {
        return {
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
                mall: '/designated-donation/index.html',
            },
            upperLocSearchTxt: '지자체',
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

    },
    methods: {
        searchField(e) {
            $("#locSearch, #goodsMenu").hide();
            $("#goodsSearch").toggle();
            $('#btn01, #btn04').removeClass('on');
            let tTarget = e.target;
            $(tTarget).parent('a').toggleClass('on');
        },
        selectField(e) {
            $("#goodsSearch, #goodsMenu").hide();
            $("#locSearch").toggle();
            $('#btn01, #btn02').removeClass('on');
            let tTarget = e.target;
            $(tTarget).parent('a').toggleClass('on');
        },
        close_area() {
            $(".loc_search, .g_category").hide();
            $('#btn01, #btn02, #btn04').removeClass('on');
        },
        searchInput: function () {
            this.search();
        },
        keydown: function (e) {
            if (e.keyCode == 13) {
                this.searchInput();
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
            this.search();
        },
        async initData(jsonValues) {
            this.type = jsonValues.type;
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
        search: function () {
        	this.$emit("tab-bar-search", this.sidoCode, this.sigunguCode, this.searchKeyword);
            this.close_area();
        },
    },
    mounted: function () {
        this.$nextTick(function () {
            this.loadSido().then((res) => {
                this.sidoList = res;
            });
            this.sigunguList = [{ cityCode: '', cityName: '시·군·구 선택' }];
        });
    },
};
</script>

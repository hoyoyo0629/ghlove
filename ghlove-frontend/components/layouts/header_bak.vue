<template>
    <header id="header" class="fixed-top">
        <div class="header_top">
            <div class="container">
                <div class="row no-gutters top_area">
                    <div class="col-6">
                        <h1 class="logo">
                            <a href="/"><img src="/static/images/logo.png" alt="고향사랑e음 로고"></a>
                        </h1>
                    </div>
                    <div class="col-6">
                        <div class="utile_menu">
                            <a href="/mypage/index.html" class="btn_mypage d-none d-md-block">
                                <img src="/static/images/icon/btn_mypage.png" alt="마이페이지로 이동">
                            </a>
                            <a href="/cart/index.html" class="btn_cart">
                                <img src="/static/images/icon/btn_cart.png" alt="장바구니로 이동">
                                <span class="badge badge_cart">{{displayCartQuantites}}</span>
                            </a>
                            <div class="header_search">
                                <a href="javascript:void(0);" class="btn_search">
                                    <span class="screen_out">검색창 열기</span>
                                </a>
                                <div class="search_area">
                                    <div class="row no-gutters search_inner">
                                        <div class="col-12 col-md-10 col-lg-8 col-xl-5">
                                            <div class="search_top">
                                                <form @submit.prevent="searchSubmit">
                                                    <input type="text" v-model="searchWord" class="form-control"
                                                           placeholder="검색어를 입력하세요">
                                                </form>
                                            </div>
                                            <div class="search_list">
                                                <div class="row no-gutters">
                                                    <dl class="col-12 col-md-6 lately">
                                                        <dt>최근검색어</dt>
                                                        <dd v-for="lately in latelySearch" :key="lately">
                                                            <a href="javascript:;"
                                                               @click="search(lately)">{{lately}}</a>
                                                            <button type="button" class="btn_del"
                                                                    @click="removeLately(lately)">
                                                                <span class="screen_out">검색어 지우기</span>
                                                            </button>
                                                        </dd>
                                                    </dl>
                                                    <dl class="col-12 col-md-6">
                                                        <dt>인기검색어</dt>
                                                        <dd v-for="word in bestSearchWord" :key="word.keyword">
                                                            <a href="javascript:;" @click="search(word.keyword)">{{word.keyword}}</a>
                                                        </dd>
                                                    </dl>
                                                </div>
                                            </div>
                                            <button type="button" class="search_close">취소</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <a href="#" class="btn_logout d-none d-md-block" v-if="!showLogin" @click.prevent="logout">
                                <img src="/static/images/icon/btn_logout.png" alt="로그아웃 하기">
                            </a>
                            <a href="/users/login.html" class="btn_login d-none d-md-block" v-else-if="showLogin">
                                <img src="/static/images/icon/btn_login.png" alt="로그인페이지로 이동">
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <nav class="gnb">
            <div class="container">
                <div class="gnb_slider_m">
                    <ul class="swiper-wrapper">
                        <li class="swiper-slide" :class="showGnbTargetFlag('new') ? 'on' : ''"><a href="/event/new.html">신상품</a></li>
                        <li class="swiper-slide" :class="showGnbTargetFlag('best') ? 'on' : ''"><a href="/event/best.html">베스트</a></li>
                        <li class="swiper-slide" :class="showGnbTargetFlag('spot') ? 'on' : ''"><a href="/event/spot.html">타임세일</a></li>
                        <li class="swiper-slide" :class="showGnbTargetFlag('featured') ? 'on' : ''"><a href="/featured/list.html">기획전</a></li>
                        <li class="swiper-slide" :class="showGnbTargetFlag('stylebook') ? 'on' : ''"><a href="/event/stylebook.html">룩북</a></li>
                    </ul>
                </div>


                <div class="gnb_slider_pc tablet swiper-container">
                    <ul class="swiper-wrapper">
                        <li class="swiper-slide" v-for="group in category.groups" :key="group.url">
                            <a href="javascript:void(0);" class="gnb_tit">{{group.name}}</a>
                        </li>
                    </ul>
                </div>
                <div class="depth_wrap tablet">
                    <div class="gnb_depth" v-for="group in category.groups" :key="group.url">
                        <div class="gnb_inner">
                            <dl v-for="category1 in group.categories" :key="category1.url">
                                <dt>{{category1.name}}</dt>

                                <dd class="depth2_area" v-for="category2 in category1.childCategories"
                                    :key="category2.categoryId">

                                    <a href="javascript:void(0);"
                                       @click="link(category2.url, category2.childCategories)" class="depth2_tit">{{category2.name}}</a>

                                   <!-- <template v-if="category2.childCategories.length > 0">

                                        <ul class="depth3">
                                            <li class="depth3_area" v-for="category3 in category2.childCategories"
                                                :key="category3.categoryId">

                                                <a href="javascript:void(0);"
                                                   @click="link(category3.url, category3.childCategories)"
                                                   class="depth3_tit">{{category3.name}}</a>

                                                <template v-if="category3.childCategories.length > 0">
                                                    <ul class="depth4">
                                                        <li v-for="category4 in category3.childCategories"
                                                            :key="category4.categoryId">
                                                            <a href="javascript:void(0);"
                                                               @click="link(category4.url, category4.childCategories)">{{category4.name}}</a>
                                                        </li>
                                                    </ul>
                                                </template>

                                            </li>
                                        </ul>

                                    </template>-->
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    </header>
</template>


<script>
    module.exports = {
        props: {
            newCartQuantity: 0,
            gnbMenuTarget: {
                type: String,
                default: function() {
                    return '';
                }
            }
        },
        data: function() {
            return {
                cartQuantity:0,
                isLogin: false,
                isGuestLogin: false,
                category: {
                    groups: []
                },
                searchWord: '',
                latelySearch: [],
                bestSearchWord: null,
                latelyItems: []
            }
        },

        methods: {
            getCartInfo: function () {
                var self = this;
                $s.api.getCartInfo(function (response) {
                    self.cartQuantity = response.cartQuantity;
                });
            },
            logout: function () {
                $s.logout();
            },
            link: function (url, childCategories) {

                var flag = typeof childCategories != 'undefined'
                    && childCategories != null
                    && childCategories.length > 0;

                if (!flag) {
                    $s.redirect('/category/?code='+ url);
                }
            },
            search: function (word) {
                $s.alert(word + '으로 검색');

                var lately = this.latelySearch;

                if (lately == null) {
                    lately = [];
                }

                if (lately.indexOf(word) < 0) {
                    lately.push(word);
                }

                $s.core.setData($s.const.LATELY_SEARCH, JSON.stringify(lately));
                this.latelySearch = lately;

                $s.redirect("/items/result.html?where=ITEM_NAME&query=" + word);

            },
            removeLately: function (word) {

                var lately = this.latelySearch;
                var index = lately.indexOf(word);

                lately.splice(index, 1);

                $s.core.setData($s.const.LATELY_SEARCH, JSON.stringify(lately));
                this.latelySearch = lately;
            },
            searchSubmit: function () {
                var word = this.searchWord;

                if (word != '') {
                    this.search(word);
                }
            },
            getBestSearchWord: function () {
                var self = this
                $s.api.getBestKeyword(function (data) {
                    self.bestSearchWord = data.list;
                }, function () {
                    //$s.error('getBestSearchWord error');
                });
            },
            showGnbTargetFlag: function (target) {
                return this.gnbMenuTarget == target;
            }
        },
        mounted: function() {

            this.$nextTick(function () {
                this.isLogin = $s.isLogin();
                this.isGuestLogin = $s.isGuestLogin();

                this.getCartInfo();
                this.getBestSearchWord();
                this.categoryInfo(this);
                this.latelyInfo(this);
                this.latelyItemInfo(this);

                initializeHeaderEvent();

            });

        },
        computed: {
            displayCartQuantites: function() {
                return this.newCartQuantity > this.cartQuantity ? this.newCartQuantity :   this.cartQuantity;
            },
            showLogin: function () {
                return !(this.isLogin || this.isGuestLogin);
            }
        }

    };
</script>
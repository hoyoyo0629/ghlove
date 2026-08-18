<template>
      <header id="header_g" class="header2">
            <nav class="gnb">
                  <div class="center">
                        <div class="gnb_slider_m">
                              <button type="button" class="total_menu" @click="allMenu()">
                                    <span class="total_menu_h">
                                          <img :src="menuImageSrc" alt="전체메뉴열기">
                                    </span>
                                    <span>전체 카테고리</span>
                              </button>
                              <span class="v-line"></span>
                              <ul class="top_menu">
                                    <li v-for="(goods,i) in goodsMenu1" :key="i">
                                          <a :href="goods.pinUrl" :class="{ 'on': goods.pinUrl == menuUrl || isChildMenu(goods.key) }">{{ goods.menuName }}</a>
                                    </li>
                              </ul>
                              <span class="goods_search">
                                    <input type="text" placeholder="답례품 전체 검색" @keydown='keydown' v-model="searchKeyword" title="답례품 전체 검색">
                                    <button type="button" @click.prevent="search">
                                          <img class="icon-img" src="/static/images/icon/search-white.png" alt="검색하기">
                                    </button>
                              </span>
                              <span class="local_shop" @click="selectMenuOpen($event)">
                                    <button type="button" class="shop_select" @click="showMapSelect">
                                          <span class="local_shop_wrap">
                                                <img class="icon-img" src="/static/images/goods/local-loc.png" alt="">
                                                <span class="locNameArea">
                                                      {{ upperLocSearchTxt }}
                                                      {{ locSearchTxt }}
                                                </span>
                                          </span>
                                          <img src="/static/images/goods/cli-icon_local-arrow.png" alt="지자체 선택 팝업 열기">
                                    </button>
                                    
                              </span>

                        </div>
                  </div>
            </nav>
            <nav class="main_lnb" id="allMenu">
                  <div class="center">
                        <ul class="top_category">
                              <li v-for="(category, i) in category.groups" :key="i">
                                    <button type="button" @mouseover.stop="overEvt(category, i)" @focus="overEvt(category, i)"
                                          @click.prevent="categoryGroupLink(category.url, category.name)" @keyup.right="rightToSubCate()" v-bind:id="'cate' + i"
                                          title="키보드 화살표 오른쪽 키를 누르면 하위 메뉴로 이동합니다.">{{ category.name }}
                                          <span><img src="/static/images/icon/cli-icon_btn-illust-arrow1.png" alt=""></span>
                                    </button>
                              </li>
                        </ul>
                        <div class="detail_cate" :style="dpOn">
                              <ul class="middle_category" v-for="(category, i) in topCategory" :key="i">
                                    <li>
                                          <button type="button" @click.prevent="categoryLink(category.url, category.name)" v-bind:id="'subCate' + i"
                                                @keyup.left="leftToCate()" v-bind:title="i == 0 ? '키보드 화살표 왼쪽 키를 누르면 상위 메뉴로 이동합니다.' : ''">{{
                                                category.name
                                          }}</button>
                                          <ul class="detail_category">
                                                <li v-for="(childCategory, j) in category.childCategories" :key="j">
                                                      <!--tabindex="0">-->
                                                      <button type="button" @click.prevent="categoryLink(childCategory.url, childCategory.name)">{{
                                                                  childCategory.name }}</button>
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
      font-size: 14px;
      outline: 1px;
}

::placeholder {
      color: #fff;
}
</style>
<script type="text/babel">
    module.exports = {
          props: {
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
                      goodsMenu1: [
                            // {menuName:"지역이벤트", pinUrl: '/featured/eventList.html', key: '1' }, //오픈 후 주석 제거
                            {menuName:"제철식품관", pinUrl: '/event/seasonList.html', key: '2' },
                            // {menuName:"특산물관", pinUrl: '/event/specialityList.html', key: '3' }, //오픈 후 주석 제거
                      ],
                      goodsMenuChild: [
                            // {menuName:"지역이벤트", pinUrl: '/featured/eventDetail.html', pKey: '1'  }, //오픈 후 주석 제거
                            // {menuName:"특산물관", pinUrl: '/event/specialityDetail.html', pKey: '3' }, //오픈 후 주석 제거
                      ],
                      upperLocSearchTxt: '지자체몰',
                      locSearchTxt: '선택하기',

                      searchTxt: '',
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
                      focusedCateIndex: 0,
                }
          },
          computed: {
                menuImageSrc() {
                      return this.isOpenMainLnb ? '/static/images/goods/all-menu_close.png' : '/static/images/goods/all-menu_open.png';
                },
                // searchPlaceHolder() {
                //     return this.upperLocSearchTxt && this.upperLocSearchTxt !== '지자체몰' ? `${this.upperLocSearchTxt} ${this.locSearchTxt}` : '전체 답례품';
                // },
          },
          methods: {
                allMenu: function () {
                    //console.log(this.category);
                    let category = this.category;
                    if (!category || !category.groups || category.groups.length == 0) {
                        //this.
                    } else {
                        // 세로선 끝까지 처리하기 위함
                        try {
                            let length = this.category.groups.length;
                            for(let i = 0 ; i < length ; i++) {
                                let categories = this.category.groups[i].categories;
                                let categoryLength = categories.length;
                                let remain = categoryLength % 6;
                                if (categoryLength > 0 && categoryLength > 6 && remain > 0)  {
                                    for(let j = 0 ; j < 6 - remain ; j++) {
                                        categories.push({name: ""});
                                    }
                                }
                            }
                        } catch (e) {
                            console.log(e);
                        }
                        // 세로선 끝까지 처리하기 위함

                        if (!this.isOpenMainLnb) {
                            $("#allMenu").stop().slideDown('fast');
                            this.isOpenMainLnb = true;
                            this.overEvt(this.category.groups[0]);
                        } else {
                            $("#allMenu").stop().slideUp('fast');
                            this.isOpenMainLnb = false;
                        }
                    }
                },
                overEvt: function (category, index) {
                      this.dpOn.display = "flex";
                      this.topCategory = category.categories;
                      this.focusedCateIndex = index;
                },
                rightToSubCate: function () {
                      $("#subCate0").focus();
                      //console.log('rightToSubCate');
                },
                leftToCate: function () {
                      $("#cate" + this.focusedCateIndex).focus();
                      //console.log('leftToCate');
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
                      //this.$emit("search-keyword", this.searchKeyword);		// 부모요소 함수 호출
                      $goods.goSearchGoods({ type: 'T', keyword: encodeURIComponent(this.searchKeyword) });
                },
                categoryLink: function (value, valueName) {
                    $s.redirect(`/goods/searchGoods.html?category=${value}&type=C`);
                },
                categoryGroupLink: function (value, valueName) {
                    $s.redirect(`/goods/searchGoods.html?group=${value}&type=C`);
                },
                keydown: function (e) {
                      if (e.keyCode == 13) {
                            this.search();
                      }
                },
                // 지자체별 답례품 보기
                selectMenuOpen: function (e) {
                      $(".goods_loc_search").toggle();
                },
                selectMenuClose: function () {
                      $(".goods_loc_search").hide();
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
                    /*if (!this.sidoCode) {
                        $s.alert("시·도를 선택하세요.");
                        return;
                    }*/

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

                    //const sido = this.getCurrentSidoName();
                    //const sigungu = this.getCurrentSigunguName();
                    //this.upperLocSearchTxt = sido;
                    //this.locSearchTxt = sigungu;
                    $goods.goSearchGoods({ type: 'L', locgov: locgovCode });
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
                handleWindowResize() {
                    $("#allMenu").stop().slideUp('fast');
                    this.isOpenMainLnb = false;
                },
                getCurrentSidoName() {
                    const sido = this.sidoList.find((v, i, arr) => { return v.sidoCode === this.sidoCode });
                    return sido ? sido.sidoName.trim() : '';
                },
                getCurrentSigunguName() {
                    const sigungu = this.sigunguList.find((v, i, arr) => {return v.cityCode === this.sigunguCode});
                    return sigungu ? sigungu.cityName : '';
                },
                isChildMenu(key){
                    //console.log(key, ':', this.goodsMenuChild.findIndex((v) => {return v.pKey = key}));
                    const idx = this.goodsMenuChild.findIndex((v) => {return v.pinUrl === this.menuUrl});
                    return idx > -1 ? this.goodsMenuChild[idx].pKey === key : false;

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

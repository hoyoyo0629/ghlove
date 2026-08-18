<template>
      <header id="header_g" class="header2">
            <nav class="gnb">
                  <div class="center">
                        <div class="gnb_slider_m dsg">
                              <span class="goods_search">
                                    <input type="text" placeholder="지자체별 특정사업 보기" @keydown='keydown' @input="onChange($event)" v-model="searchKeyword" title="특정사업에 기부하기 검색">
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

                      upperLocSearchTxt: '지자체별',
                      locSearchTxt: '검색',

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
                      /*if (!this.searchKeyword) {
                            $s.alert("검색할 단어를 입력해주세요.");
                            return;
                      }*/
                      //this.$emit("search-keyword", this.searchKeyword);		// 부모요소 함수 호출
                      //$goods.goSearchGoods({ type: 'T', keyword: encodeURIComponent(this.searchKeyword) });
                    this.$emit("search-by-keyword", this.searchKeyword);
                },
                /*categoryLink: function (value, valueName) {
                    $s.redirect(`/goods/searchGoods.html?category=${value}&type=C`);
                },
                categoryGroupLink: function (value, valueName) {
                    $s.redirect(`/goods/searchGoods.html?group=${value}&type=C`);
                },*/
                keydown: function (e) {
                      if (e.keyCode == 13) {
                            this.search();
                      }
                },
                onChange: function(event) {
                	vm.searchKeyword = event.target.value;
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
                    if (!this.sidoCode) {
                        $s.alert("시·도를 선택하세요.");
                        return;
                    }

                    /*if (!this.sigunguCode) {
                        $s.alert("시·군·구를 선택하세요.");
                        return;
                    }

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
                    //$goods.goSearchGoods({ type: 'L', locgov: locgovCode });
                    */
                    this.upperLocSearchTxt = this.getCurrentSidoName();
                    if (this.sigunguCode) {
                        this.locSearchTxt = this.getCurrentSigunguName();
                    } else {
                        this.locSearchTxt = '';
                    }
                    this.selectMenuClose();
                    this.$emit("search-by-locgov", this.sidoCode, this.sigunguCode);
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
                setLocgovNm: async function (sigunguCode) {
                	if (sigunguCode) {
						this.sidoCode = sigunguCode.substring(0, 2) + '000';
						await this.changeSidoEvent();
						this.sigunguCode = sigunguCode;
						let sido = this.getCurrentSidoName();
						this.upperLocSearchTxt = sido;

						let sigungu = this.getCurrentSigunguName();
						this.locSearchTxt = sigungu;
                	} else {
	                	this.upperLocSearchTxt = '지자체별';
	                	this.locSearchTxt = '검색';
                	}
                },
          },
          mounted: function () {
                window.addEventListener('resize', this.handleWindowResize);
                this.$nextTick(function () {
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

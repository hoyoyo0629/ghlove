<template>
  <div class="footer_wrap">
    <footer id="footer" v-if="!exportMode">
      <div class="footer_link">
        <div class="container">
          <a :href="fnbData[i].url" v-for="(eachFnbMenu, i) in fnbData" :key="i">{{ fnbData[i].menuTxt }}</a>
        </div>
      </div>
      <div class="footer_content">
        <div class="center">
          <div class="logo_row">
            <img src="/static/images/gov-logo.png" alt="행정안전부 로고" />
            <img class="klid_logo" src="/static/images/klid-logo.png" alt="한국지역정보개발원 로고" />
          </div>
          <div class="footer-txt">
            <ul class="footer-list">
              <li>(행정안전부) 30112 세종특별자치시 도움6로 42(어진동)</li>
              <li>30116 세종특별자치시 가름로 143(어진동)</li>
            </ul>
            <ul class="footer-list">
              <li>(한국지역정보개발원) 03923 서울특별시 마포구 성암로 301(상암동)</li>
            </ul>
            <ul class="footer-list">
              <li>고향사랑e음 기부 가능 시간 : 01:00 ~ 23:30</li>
              <li>고향사랑e음 고객센터 1522-2431 (월~금 9:00~18:00, 공휴일 제외)</li>
            </ul>
            <p>
              본 홈페이지에 게시된 이메일주소가 자동 수집되는 것을 거부하며, 이를 위반시 정보통신망법에 의해 처벌됨을 유념하시기 바랍니다.<br />
              고향사랑e음은 통신판매중개자이며 통신판매의 당사자가 아닙니다. 상품, 상품정보, 거래에 관한 의무와 책임은 판매자에게 있습니다.
            </p>
            <p class="copyright">© Ministry of the Interior and Safety. All rights reserved.</p>
          </div>
          <a href="/static/images/accessibility.png" target="_blank" title="품질인증서_새창열림"
            ><img src="/static/images/accessibility-logo.png" alt="국가공인 정보통신접근성 품질인증마크" style="width: 50px; margin-top: 15px" /><span
              class="sr-only"
              >한국디지털접근성진흥원 홈페이지 바로가기</span
            ></a
          >
        </div>
      </div>
    </footer>

    <!-- 이용약관 팝업 -->
    <div class="modal fade modal_full pop_terms_use" v-if="!exportMode">
      <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <h3 class="modal_tit">{{ terms.title }}</h3>
          </div>
          <div class="modal-body">
            <div class="terms_wrap" v-html="terms.content"></div>
          </div>
          <button type="button" class="modal_close" data-dismiss="modal">
            <span class="screen_out">이용약관 닫기</span>
          </button>
        </div>
      </div>
    </div>

    <alert></alert>
    <toast></toast>
    <layout-loading ref="layoutLoading" @loading-mounted="loadingMounted"></layout-loading>
    <donation-loading ref="donationLoading" @donationLoading-mounted="donationLoadingMounted"></donation-loading>
  </div>
</template>

<script>
module.exports = {
  props: {
    newCartQuantity: 0,
    newLatelyItemCount: 0,
    exportMode: {
      type: Boolean,
      default: function () {
        return false;
      },
    },
  },
  data: function () {
    return {
      // alicia_Binding Test Data start
      fnbData: [
        {
          url: "/policy/auth.html",
          menuTxt: "이용약관",
        },
        {
          url: "/policy/privacy.html",
          menuTxt: "개인정보처리방침",
        },
        {
          url: "/policy/copyright.html",
          menuTxt: "저작권정책",
        },
        {
          url: "/guide/sitemap.html",
          menuTxt: "사이트맵",
        },
        {
          url: "/notice/list.html",
          menuTxt: "공지사항",
        },
      ],
      // alicia_Binding Test Data end

      terms: {},
      about: {},
      isLogin: false,
      isGuestLogin: false,
      terminal: {
        equipmentCode: "",
      },
      category: {
        groups: [],
      },
      quick: {
        cartQuantity: 0,
        wishlistCount: 0,
      },
      latelyItems: [],
      latelyItemList: [],
    };
  },
  components: {
    alert: httpVueLoader("/components/layouts/alert.vue"),
    toast: httpVueLoader("/components/layouts/toast.vue"),
    layoutLoading: httpVueLoader("/components/layouts/loading.vue"),
    donationLoading: httpVueLoader("/components/layouts/do_loading.vue"),
  },
  methods: {
    getAbout: function () {
      let self = this;
      $s.api.getAbout("", function (response) {
        self.about = response.about;
      });
    },
    logout: function () {
      $s.logout();
    },
    getQuickInfo: function () {
      var self = this;
      $s.api.getQuickInfo(
        function (data) {
          self.quick = data;
        },
        function () {
          //$s.error('getBestSearchWord error');
        }
      );
    },
    link: function (url, childCategories) {
      var flag = typeof childCategories != "undefined" && childCategories != null && childCategories.length > 0;

      if (!flag) {
        $s.redirect("/category/?code=" + url);
      }
    },

    policy: function () {
      var param = {
        // TODO
      };
      $s.api.getPolicy(param, function (response) {
        vm.terms = response.clause;
      });
    },

    getLatelyItems: function () {
      var self = this;

      var param = {
        ids: this.latelyItems.toString(),
        limit: 4,
      };

      $s.api.getLatelyItems(
        param,
        function (response) {
          var list = response.list;
          self.latelyItemList = list;
        },
        function (error) {
          $s.error(error);
        }
      );
    },
    showLoading: function (isLoading) {
      this.$refs.layoutLoading.showLoading(isLoading);
    },

    showDonationLoading: function (isLoading) {
      this.$refs.donationLoading.showDonationLoading(isLoading);
    },

    loadingMounted: function () {
      // 로딩바 컴포넌트가 화면에 그려진 이후 호출되는 함수. 부모컴포넌트에서 바로 로딩바 호출시 그려지기 전에 호출하여 오류 발생하는 경우 있음
      this.$emit("loading-mounted");

      // 로딩바 테스트시 아래 주석 풀고 위 emit 부분 주석처리
      /*
    	this.showLoading(true);
    	setTimeout(() => this.showLoading(false)
    		, 1000);
    	*/
    },

    donationLoadingMounted: function () {
      // 로딩바 컴포넌트가 화면에 그려진 이후 호출되는 함수. 부모컴포넌트에서 바로 로딩바 호출시 그려지기 전에 호출하여 오류 발생하는 경우 있음
      this.$emit("donationLoading-mounted");

      // 로딩바 테스트시 아래 주석 풀고 위 emit 부분 주석처리
      /*
    	this.showDonationLoading(true);
    	setTimeout(() => this.showDonationLoading(false)
    		, 1000);
    	*/
    },
  },

  computed: {
    cartQuantity: function () {
      return this.formatNumber(this.quick.cartQuantity);
    },
    wishlistCount: function () {
      return this.formatNumber(this.quick.wishlistCount);
    },
    latelyItemCount: function () {
      return this.newLatelyItemCount > this.latelyItems.length
        ? this.formatNumber(this.newLatelyItemCount)
        : this.formatNumber(this.latelyItems.length);
    },
    displayCartQuantites: function () {
      return this.newCartQuantity > this.quick.cartQuantity ? this.formatNumber(this.newCartQuantity) : this.formatNumber(this.quick.cartQuantity);
    },
    showLogin: function () {
      return !(this.isLogin || this.isGuestLogin);
    },
    firestlatelyItem: function () {
      var item = null;
      var list = this.latelyItemList;

      if (list != null && list.length > 0) {
        item = list[0];
      }

      return item;
    },
  },
  mounted: function () {
    this.$nextTick(function () {
      this.isLogin = $s.isLogin();
      this.isGuestLogin = $s.isGuestLogin();

      // this.getAbout();

      // this.getQuickInfo();
      // this.policy();
      // this.categoryInfo(this);
      // this.latelyInfo(this);
      // this.latelyItemInfo(this);
      // this.getLatelyItems();

      initializeFooterEvent();
    });
  },
};
</script>

<template>
  <header id="header" class="header2">
    <sitemap-modal></sitemap-modal>
    <ul id="go_main">
      <li><a href="#contents">본문 바로가기</a></li>
      <li><a href="#gnb">주메뉴 바로가기</a></li>
    </ul>

    <div class="header_top top-header">
      <div class="container">
        <div class="top-navi">
          <div class="user-status-info" v-if="!showLogin">
            <span class="top_navi_info">
              <span class="db-userName cBlue">{{
                userStatusInfoData.userName
              }}</span>
              회원님 환영합니다.
            </span>
            <span class="top_navi_info">
              기부총액
              <span class="db-totalPoint cBlue"
                >{{
                  formatNumber(userStatusInfoData.userTotalDonation)
                }}
                원</span
              >
            </span>
            <span class="top_navi_info">
              기부포인트
              <span class="db-totalPoint cBlue"
                >{{
                  formatNumber(userStatusInfoData.userDonationPoint)
                }}
                P</span
              >
            </span>
          </div>

          <button
            type="button"
            href="javascript:void(0)"
            class="btn_logout d-md-block"
            v-if="!showLogin"
            @click.prevent="logout"
          >
            로그아웃
          </button>
          <button
            type="button"
            class="btn_login d-md-block"
            v-else-if="showLogin"
            @click.prevent="goLogin"
          >
            로그인
          </button>

          <button
            type="button"
            v-if="!showLogin"
            @click="getMyPage()"
            class="btn_mypage d-md-block"
          >
            마이페이지
          </button>

          <button
            type="button"
            v-else-if="showLogin"
            @click="goToJoinPageBefore()"
            class="btn_mypage d-md-block"
          >
            회원가입
          </button>

          <button type="button" class="btn_cart" @click="openPresent('sbag')">
            <span class="cartIconWrap">
              <span class="badge badge_cart" style="display: none">{{
                cartQuantity
              }}</span>
            </span>
            장바구니
          </button>

          <button type="button" class="btn_orderList" @click="getOrderList()">
            주문조회
          </button>
        </div>
      </div>
    </div>
    <div class="gnb_focus"></div>
  </header>
</template>

<style scoped>
#go_main {
  position: absolute;
  height: 0;
}

#go_main a {
  opacity: 0;
}

#go_main a:focus {
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 99;
  padding: 10px;
  white-space: nowrap;
  opacity: 1;
  background-color: var(--gray5);
}
</style>
<script type="text/javascript" src="/static/js/netfunnel.js"></script>
<script type="text/javascript" src="/static/js/netfunnel_skin.js"></script>
<script>
module.exports = {
  props: {
    gnbMenuTarget: {
      type: String,
      default: function () {
        return "";
      },
    },
  },
  components: {
    "sitemap-modal": httpVueLoader("/components/layouts/sitemapModal.vue"),
  },
  data: function () {
    return {
      domainType: "",
      // alicia_Binding Test Data start
      userStatusInfoData: {
        userName: "",
        userTotalDonation: "0",
        userDonationPoint: "0",
      },
      // alicia_Binding Test Data end

      cartQuantity: 0,
      isLogin: false,
      isGuestLogin: false,
      category: {
        groups: [],
      }
    };
  },
  computed: {
    showLogin: function () {
      return !(this.isLogin || this.isGuestLogin);
    },
  },
  methods: {
    // 전체메뉴 :사이트맵
    sitemapModal() {
      $("#allSitemap").show();
      $("#firstFocus").focus();
    },

    goToJoinPageBefore: function () {
      if ($s.config.isUseNetFunnel) {
        NetFunnel_Action({ action_id: "joinin" }, function (ev, ret) {
          location.href = "/users/join.html";
        });
      } else {
        location.href = "/users/join.html";
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
    getOrderList: function () {
      this.isLogin = $s.isLogin();
      if (this.isLogin) {
        location.href = "/mypage/orderList.html";
      } else {
        this.redirect($s.pages.LOGIN + "?target=" + "/mypage/orderList.html");
      }
    },
    getCartInfo: function () {
      var self = this;
      /*$s.api.getCartInfo(
        function (response) {
          self.cartQuantity = response.cartQuantity;
        },
        function (error) {
          //$s.alert(error.response.data.message);
          console.log(error);
        }
      );*/
    },
    userStatusInfo: function () {
      this.isLogin = $s.isLogin();

      if (!this.isLogin) {
        return false;
      }

      var self = this;

      $s.api.userStatusInfo(function (response) {
        self.userStatusInfoData.userName = response.userName;
        self.userStatusInfoData.userTotalDonation = response.totalCntrAmt;
        self.userStatusInfoData.userDonationPoint = response.blcePointTotal;
      });
    },
    logout: function () {
      $s.logout();
    },
    link: function (url, childCategories) {
      var flag =
        typeof childCategories != "undefined" &&
        childCategories != null &&
        childCategories.length > 0;

      if (!flag) {
        $s.redirect("/category/?code=" + url);
      }
    },
    openPresent: function (type) {
      /*
        $s.api.getSubmit('/api/present/login', '', function (response) {
          //         console.log('response', response);
          //         console.log('response.data', response.data);
  
          var param = '';
          //var domain = 'https://gohyang01.bbiz.kr';
          var domain = 'https://shop.ilovegohyang.go.kr';
          if (response.isSuccess) {
            //          domain = (type == 'sbag') ? 'https://gohyang01.bbiz.kr/kwa-ABS_sbag_l' : 'https://gohyang01.bbiz.kr';
            domain = (type == 'sbag') ? 'https://shop.ilovegohyang.go.kr/kwa-ABS_sbag_l' : 'https://shop.ilovegohyang.go.kr';
            param = (response.data == null) ? '' : response.data;
            window.open(domain + '?_tss_=' + param, 'presentPopup', '_blank');
          }
        });
        */

      if (type == "sbag") {
        $s.redirect("/cart/index.html");
        // $s.redirect("/_pub/cart/UI_P07150000.html");
      } else {
        $s.redirect("/goods/index.html");
      }
    },
    saveUserActionLog: function () {
      $s.api.saveUserActionLog(
        { requestUri: location.pathname },
        function () {}
      );
    },
    goLogin: function () {
      $s.redirect(
        $s.pages.LOGIN +
          "?target=" +
          encodeURIComponent($s.requestContext.requestFullUri)
      ); // 로그인 후 로그인 진입 전 화면으로 이동하도록 수정
    },
  },
  mounted: function () {
    this.$nextTick(function () {
      this.domainType = $s.config.domain;

      this.isLogin = $s.isLogin();
      this.isGuestLogin = $s.isGuestLogin();
      this.getCartInfo();

      this.userStatusInfo();
      this.saveVisitData();

      initializeHeaderEvent();

      let header = this;
      window.onpageshow = function (event) {
		if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
        	header.domainType = $s.config.domain;

        	header.isLogin = $s.isLogin();
        	header.isGuestLogin = $s.isGuestLogin();
        	header.getCartInfo();

        	header.userStatusInfo();

        	initializeHeaderEvent();
        }
      };
    });
  },
};
</script>

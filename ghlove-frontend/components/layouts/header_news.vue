<template>
  <header id="header" class="header2 newsletter">
    <!--<total-search @query="goToSearch"></total-search>
    <sitemap-modal></sitemap-modal>-->
    <ul id="go_main">
      <li><a href="#contents">본문 바로가기</a></li>
      <li><a href="#gnb">주메뉴 바로가기</a></li>
    </ul>

  
    <div class="main-header">
      <div class="container">
        <h1 class="logo">
			<img src="/static/images/news/logo.png" alt="고향사랑e음 소식지 로고"/>
        </h1>
<!--
        <nav class="m_top-navi">
          <nav id="m-hamberger">
            <button type="button">
              <img
                class="icon-img"
                src="/static/images/icon/cli-icon_hamberger.png"
                alt="전체메뉴"
              />
            </button>
          </nav>
        </nav>
-->
		                <div class="tab_menu_wrap" id="tabMenu" v-if="menuUrl == '/catalog/catalog-main.html'">

                  <div class="tab_group_wrap">
                    <div class="tab_group" style="margin-bottom: 0px; flex-wrap: nowrap;">
                      <button type="button" id="navTab0" class="tab_items on" @click="goToTabMenu('0')" style="min-width: 90px;">기부제소식</button>
                      <button type="button" id="navTab1" class="tab_items" @click="goToTabMenu('1')" style="min-width: 80px;">고향소식</button>
                      <button type="button" id="navTab2" class="tab_items" @click="goToTabMenu('2')" style="min-width: 100px;">베스트 답례품</button>
                      <button type="button" id="navTab3" class="tab_items" @click="goToTabMenu('3')" style="min-width: 80px;">우수사례</button>
                    </div>
                  </div>

                </div>
	      
	      
      </div>
    </div>
    <div class="gnb_focus"></div>

    <!-- mobile All Menu 모바일 메뉴  -->
    <div class="m-all-menu" id="m-all-menu">
      <div class="m-all-menu-wrap">
        <div class="top-navi">
          <!--<button type="button" class="btn_cart" @click="openPresent('sbag')">-->
            <span class="m_news_logo" style="margin: auto 0;">
              <img
                class="icon-img-logo"
                src="/static/images/news/logo_m.png"
                alt="고향사랑e음 소식지 로고"
              />
            </span>
          <!--</button>-->
          <button type="button" class="icon-btn-close" @click="closeMenu" title="메뉴 닫기 버튼">
            <img
              class="icon-img icon-img-close"
              src="/static/images/icon/cli-icon_btn-close.png"
            />
          </button>
        </div>
        
        <div class="container">
          <div class="all-menu">
            <nav class="m-all-menu-list">
              <ul class="all-main-menu mobileM">

                <li class=""><a class="tab_items" id="mTab1" href="javascript:void(0);" @click="goToTabMenu(1)">주요답례품</a></li>
                <li class=""><a class="tab_items" id="mTab2" href="javascript:void(0);" @click="goToTabMenu(2)">기금사업 공감</a></li>
                <li class=""><a class="tab_items" id="mTab3" href="javascript:void(0);" @click="goToTabMenu(3)">인기 답례품</a></li>
                <li class=""><a class="tab_items" id="mTab4" href="javascript:void(0);" @click="goToTabMenu(4)">제철식품</a></li>

              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<style scoped>
  #go_main{position:absolute;height:0;}
  #go_main a{opacity:0;}
  #go_main a:focus{display:block;position:absolute;top:0;left:0;z-index:99;padding:10px;white-space:nowrap;opacity:1;background-color:var(--gray5);}
</style>

<script type="text/javascript" src="/static/js/netfunnel.js"></script>
<script type="text/javascript" src="/static/js/netfunnel_skin.js"></script>
<script>
$(document).ready(function () {
  // browser resizing => 초기화 되야 할 부분 작성
  var delay = 100;
  var timer = null;
  // var winInnerWidth = window.innerWidth;

  //jQuery
  $(window).on("resize", function () {
    $(".gnb_focus").slideUp(100);
    // $('.mb-drawer-menu').animate({ "right": "-100%" }, 300);

    // 브라우저 과부하 방지
    clearTimeout(timer);
    timer = setTimeout(function () {
      let winWidth = window.innerWidth;
      // console.log(winWidth);
      if (winWidth <= 1030) {
        $(".dt-all-menu").css("display", "none");
      }
      $(".m-all-menu").animate({ right: "-100%" }, 0);
    }, delay);
  });

  //main-menu SlideToggle
  $("ul.main-menu").hover(
    function () {
      $("ul.sub-menu").stop().slideDown(100);
      $(".gnb_focus").stop().slideDown(100);
    },
    function () {
      $("ul.sub-menu").stop().slideUp(100);
      $(".gnb_focus").stop().slideUp(100);
    }
  );
  $("ul.main-menu > li a").focus(function () {
    $("ul.sub-menu").stop().slideDown(100);
    $(".gnb_focus").stop().slideDown(100);
  });
  $("ul.main-menu > li a").blur(function () {
    $("ul.sub-menu").stop().slideUp(100);
    $(".gnb_focus").stop().slideUp(100);
  }); 

  // Desktop Hamberger Click
  $(".hamberger").click(function () {
    $(".dt-all-menu").slideToggle(100);
  });
  // close Desktop All Menu
  $("button.dt-all-close").click(function () {
    $(".dt-all-menu").slideUp(100);
  });
  // close Desktop All Menu
  $(".all_close").click(function () {
    $(".dt-all-menu").slideUp(100);
  });

  // Mobile Hamberger Click
  $("#m-hamberger").click(function () {
    $("#m-all-menu")
      .fadeIn(100)
      .animate({ right: "0" }, 200, function () {
        $(this).css({ "background-color": "rgba(0, 0, 0, 0.3)" });
      });
  });

  // close Mobile Menu
  $("#closebtn").click(function () {
    $("#m-all-menu")
      .fadeOut(100)
      .animate({ right: "-100%", "background-color": "" }, 200);
  });

  // 소식지 close Mobile Menu
  $("#m-all-menu ul li a").click(function () {
    $("#m-all-menu").fadeOut(100).animate({ right: "-100%", "background-color": "" }, 200);
  });

});

module.exports = {
  props: {
    gnbMenuTarget: {
      type: String,
      default: function () {
        return "";
      },
    },
    menuUrl: {
      type: String,
      default: function () {
        return "";
      },
    },
  },
  components: {
    // "total-search": httpVueLoader("/components/layouts/totalSearch.vue"),
    // "sitemap-modal": httpVueLoader("/components/layouts/sitemapModal.vue"),
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
      },
      searchWord: "",
      latelySearch: [],
      bestSearchWord: null,
      latelyItems: [],
      recommendSearch: {},
      // gnbSwiperOption: {
      //   slidesPerView: "auto",
      //   spaceBetween: 40,
      //   navigation: {
      //     nextEl: ".swiper-contents .swiper-button-next",
      //     prevEl: ".swiper-contents .swiper-button-prev",
      //   },
      //   sliderMove: 1,
      //   on: {
      //     init: function () {
            
      //     },
      //   },
      // },
      goToSearch: "",
      menu: [
      	{
      		name: '전체'
      		, searchType: 'ALL'
      	},
      	{
      		name: '기부제소식'
      		, searchType: 'NEWS'
      	},
      	{
      		name: '고향소식'
      		, searchType: 'HOMETOWN'
      	},
      ],
    };
  },
  computed: {
    showLogin: function () {
      return !(this.isLogin || this.isGuestLogin);
    },
  },
  methods: {
    goToTabMenu: function (n) { // 햄버거에서만 tabs 이동
		// 네비 탭 on 붙이기
		$(".tab_items").eq(n).addClass("on").siblings().removeClass("on");
		
        // pc + mo
		$(".tab_con00 #conNavBox1").click(function(){$("#navTab1").addClass("on").siblings().removeClass("on");});
		$(".tab_con00 #conNavBox2").click(function(){$("#navTab2").addClass("on").siblings().removeClass("on");});
		$(".tab_con00 #conNavBox3").click(function(){$("#navTab3").addClass("on").siblings().removeClass("on");});
		$(".tab_con00 #conNavBox4").click(function(){$("#navTab4").addClass("on").siblings().removeClass("on");});
		
		this.$emit('go-to-tab-menu', n);
    },
    // 전체메뉴 :사이트맵
    sitemapModal() {
      $("#allSitemap").show();
      $("#firstFocus").focus();
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
    link: function (url, childCategories) {
      var flag =
        typeof childCategories != "undefined" &&
        childCategories != null &&
        childCategories.length > 0;

      if (!flag) {
        $s.redirect("/category/?code=" + url);
      }
    },
    search: function (word) {
      if (this.latelySearch == null) {
        this.latelySearch = [];
      }

      if (this.latelySearch.indexOf(word) < 0) {
        this.latelySearch.unshift(word);
      }

      $s.core.setData(
        $s.const.LATELY_SEARCH,
        JSON.stringify(this.latelySearch)
      );

      word = encodeURIComponent(word);
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
      var recommend = this.recommendSearch;

      if (
        word === "" &&
        recommend != null &&
        recommend.searchContents != null &&
        recommend.searchContents !== ""
      ) {
        var searchLink = "";
        var targetBlank = false;

        if ($s.isMobile()) {
          searchLink = recommend.searchMobileLink;
          if (recommend.searchMobileLinkTargetFlag == "Y") {
            targetBlank = true;
          }
        } else {
          searchLink = recommend.searchLink;
          if (recommend.searchLinkTargetFlag == "Y") {
            targetBlank = true;
          }
        }

        // 팝업 혹은 링크 연결
        if (targetBlank) {
          window.open(searchLink, recommend.searchContents);
        } else {
          $s.redirect(searchLink);
        }
      }

      if (word != "") {
        this.search(word);
      }
    },
    getBestSearchWord: function () {
      var self = this;
      $s.api.getBestKeyword(
        function (response) {
          self.bestSearchWord = response.list;
        },
        function () {
          $s.alert(error.response.data.message);
        }
      );
    },
    getRecommendSearchWord: function () {
      var self = this;
      $s.api.getRecommendKeyword(
        function (response) {
          self.recommendSearch = response.search;
        },
        function () {
          $s.alert(error.response.data.message);
        }
      );
    },
    showGnbTargetFlag: function (target) {
      return this.gnbMenuTarget == target;
    },
    saveVisitData: function () {
      var today = new Date().getTime();
      var visit = $s.core.getData($s.const.VISIT);
      var visitExpireDate = $s.core.getData($s.const.VISIT_EXPIRE_DATE);

      if (visit == null || visit == "" || today > visitExpireDate) {
        var agent = navigator.userAgent;
        var referrer = document.referrer;
        var domain = vm.findDomain(referrer);
        var self = this;
        var param = {
          agent: agent,
          referer: referrer,
          browser: self.getBrowser(agent),
          os: self.getOs(agent),
          domain: domain,
          domainName: self.getDomainName(domain),
          language: navigator.language,
        };

        $s.api.saveVisitData(param, function (response) {
          if (response.status === "OK") {
            $s.core.setData("visit", "1");

            var expireDate = today + 1 * 1000 * 60 * 60 * 24;
            $s.core.setData("visit_expire_date", expireDate);
          }
        });

        /*$s.axios.get("https://api.ipify.org", {}, {}).then(function (response) {
          var param = {
            agent: agent,
            referer: referrer,
            browser: self.getBrowser(agent),
            os: self.getOs(agent),
            domain: domain,
            domainName: self.getDomainName(domain),
            remoteAddr: response.data,
            language: navigator.language,
          };

          $s.api.saveVisitData(param, function (response) {
            if (response.status === "OK") {
              $s.core.setData("visit", "1");

              var expireDate = today + 1 * 1000 * 60 * 60 * 24;
              $s.core.setData("visit_expire_date", expireDate);
            }
          });
        });*/
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
    closeMenu: function() {
	    $("#m-all-menu")
	      .fadeOut(100)
	      .animate({ right: "-100%", "background-color": "" }, 200);
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      this.domainType = $s.config.domain;

      this.isLogin = $s.isLogin();
      this.isGuestLogin = $s.isGuestLogin();
      this.getCartInfo();

      this.userStatusInfo();
      //this.getBestSearchWord();
      //this.getRecommendSearchWord();
      //this.categoryInfo(this);
      //this.latelyInfo(this);
      //this.latelyItemInfo(this);
      this.saveVisitData();
      //this.saveUserActionLog();

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

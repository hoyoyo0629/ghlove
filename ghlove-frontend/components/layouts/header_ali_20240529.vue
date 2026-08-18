<template>
  <header id="header" class="header2">
    <total-search @query="goToSearch"></total-search>
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
            class="btn_logout d-none d-md-block"
            v-if="!showLogin"
            @click.prevent="logout"
          >
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_logout.png"
              alt=""
              title="로그아웃 하기"
            />
            로그아웃
          </button>
          <button
            type="button"
            class="btn_login d-none d-md-block"
            v-else-if="showLogin"
            @click.prevent="goLogin"
          >
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_login.png"
              alt=""
              title=""
            />
            로그인
          </button>

          <button
            type="button"
            v-if="!showLogin"
            @click="getMyPage()"
            class="btn_mypage d-none d-md-block"
          >
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_mypage.png"
              alt=""
              title="마이페이지"
            />
            마이페이지
          </button>

          <button
            type="button"
            v-else-if="showLogin"
            @click="goToJoinPageBefore()"
            class="btn_mypage d-none d-md-block"
          >
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_mypage.png"
              alt=""
              title="회원가입"
            />
            회원가입
          </button>

          <button type="button" class="btn_cart" @click="openPresent('sbag')">
            <span class="cartIconWrap">
              <img
                class="icon-img"
                src="/static/images/icon/cli-icon_cart.png"
                alt=""
              />
              <span class="badge badge_cart" style="display: none">{{
                cartQuantity
              }}</span>
            </span>
            장바구니
          </button>

          <button type="button" class="btn_orderList" @click="getOrderList()">
            <img
              class="icon-img"
              src="/static/images/icon/order-inquiry.png"
              alt=""
            />
            주문조회
          </button>

		<!--
          <button type="button" class="btn_orderList" @click="goCatalog()">
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_news.png"
              alt=""
            />
            소식지
          </button>
		-->
        </div>
      </div>
    </div>
    <div class="main-header">
      <div class="container">
        <h1 class="logo">
          <a href="/"
            ><img src="/static/images/cli-logo.png" alt="고향사랑e음 로고"
          /></a>
        </h1>
        <nav id="gnb">
          <ul class="main-menu">
            <li>
              <a href="javascript:void(0);" onclick="javascript:$s.donation.goDonationPage();">
              <!--<a href="/donation/donation.html">-->
                <span>기부하기<span class="underLine"></span></span>
              </a>
              <ul class="sub-menu">
                <li>
                  <a href="javascript:void(0);" onclick="javascript:$s.donation.goDonationPage();">
                  <!--<a href="/donation/donation.html">-->기부하기</a>
                </li>
                <li>
                  <a href="/donation/list-select.html">기금사업 소개</a>
                </li>
              </ul>
            </li>
            <li>
              <a href="/donation/guide1.html">
                <span>기부안내<span class="underLine"></span></span>
              </a>
              <ul class="sub-menu">
                <li><a href="/donation/guide1.html">고향사랑기부제 안내</a></li>
                <li>
                  <a href="/donation/guide2.html">온라인 기부절차 안내</a>
                </li>
                <li>
                  <a href="/donation/guide5.html">오프라인 기부절차 안내</a>
                </li>
                <li>
                  <a href="/donation/guide3.html">연말정산 간편서비스 안내</a>
                </li>
                <!-- <li><a href="/donation/guide4.html">종합소득세 확정신고 서비스 안내</a> </li> -->
                <li>
                  <a href="/donation/guide6.html">고향사랑 기부시 유의사항</a>
                </li>
              </ul>
            </li>
            <li>
              <a href="/goods/index.html">
                <span>답례품<span class="underLine"></span></span
              ></a>
              <ul class="sub-menu">
                <li><a href="/goods/index.html">답례품몰</a></li>
                <!--<li><a href="/featured/eventList.html">지역이벤트</a></li>-->
                <li><a href="/event/seasonList.html">제철식품관</a></li>
                <!--<li><a href="/event/specialityList.html">특산물관</a></li>-->
              </ul>
            </li>
            <li>
              <a href="/notice/list.html">
                <span>고객센터<span class="underLine"></span></span>
              </a>
              <ul class="sub-menu">
                <li><a href="/notice/list.html">공지사항</a></li>
                <li><a href="/data-board/list.html">자료실</a></li>
                <li><a href="/qna/qna-open.html">Q&A</a></li>
                <li><a href="/faq/list.html">FAQ</a></li>
                <!--<li><a href="/guide/help.html">도움말</a></li>-->
                <li><a href="/guide/sitemap.html">사이트맵</a></li>
              </ul>
            </li>
          </ul>
          <div class="icon_wrapper">
            <div class="total_search">
              <!-- <button type="button" id="searchBtn" @click.prevent="searchModal($event)">
                <img class="icon-img" src="/static/images/icon/total-search.png" alt="통합검색">
                <span>통합검색</span>
              </button> -->
            </div>
            <div class="hamberger">
              <button
                type="button"
                id="totalMenuBtn"
                @click.prevent="sitemapModal($event)"
              >
                <img
                  class="icon-img"
                  src="/static/images/icon/cli-icon_hamberger.png"
                  alt="전체메뉴"
                />
              </button>
            </div>
          </div>
        </nav>
        <nav class="m_top-navi">
          <!-- <a href="javascript:void(0)" class="btn_logoutk" v-if="!showLogin" @click.prevent="logout">
            <img class="icon-img" src="/static/images/icon/m-icon_logout.png" alt="로그아웃">
          </a> -->
          <a href="/users/login.html" class="btn_login" v-if="showLogin">
            <img
              class="icon-img"
              src="/static/images/icon/m-icon_login.png"
              alt="로그인"
            />
          </a>
          <!-- <button type="button" @click="searchModal($event)">
            <img class="icon-img" src="/static/images/icon/total-search.png" alt="통합검색">
          </button> -->
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
      </div>
    </div>
    <div class="gnb_focus"></div>

    <!-- mobile All Menu 모바일 메뉴  -->
    <div class="m-all-menu" id="m-all-menu">
      <div class="m-all-menu-wrap">
        <div class="top-navi">
          <button type="button" class="btn_cart" @click="openPresent('sbag')">
            <span class="cartIconWrap">
              <img
                class="icon-img"
                src="/static/images/icon/m-cart.png"
                alt="장바구니로 이동"
              />
              <span class="badge badge_cart" style="display: none">{{
                cartQuantity
              }}</span>
            </span>
          </button>
          <!-- <button type="button" @click.prevent="searchModal($event)">
            <img class="icon-img" src="/static/images/icon/total-search.png" alt="통합검색">
          </button> -->
          <button type="button">
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_btn-close.png"
              alt=""
              id="closebtn"
            />
          </button>
        </div>
        <div class="quick_navi">
          <button
            type="button"
            v-if="!showLogin"
            @click="getMyPage()"
            class="btn_mypage d-md-block"
          >
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_mypage.png"
              alt="마이페이지로 이동"
              title="마이페이지"
            />
            마이페이지
          </button>

          <button
            type="button"
            v-else-if="showLogin"
            @click="goToJoinPageBefore()"
            class="btn_mypage d-md-block"
          >
            <img
              class="icon-img"
              src="/static/images/icon/cli-icon_mypage.png"
              alt="회원가입으로 이동"
              title="회원가입"
            />
            회원가입
          </button>
          <button type="button" class="btn_orderList" @click="getOrderList()">
            <img
              class="icon-img"
              src="/static/images/icon/order-inquiry.png"
              alt="주문조회로 이동"
            />
            주문조회
          </button>
        </div>
        <div class="container">
          <div class="user-status-info" v-if="!showLogin">
            <div class="info-top">
              <div class="user-name">
                <img src="/static/images/icon/cli-icon_bullet-a.png" alt="" />
                <span class="db-userName cBlue">{{
                  userStatusInfoData.userName
                }}</span>
                회원님 환영합니다.
              </div>
              <button
                class="btn_logout d-md-block"
                v-if="!showLogin"
                @click.prevent="logout"
              >
                <img
                  class="icon-img"
                  src="/static/images/icon/logout-white.png"
                  alt=""
                  title="로그아웃 하기"
                />
                로그아웃
              </button>
            </div>
            <div class="user-data">
              <div class="user_data_cols">
                <span class="pointblue"
                  ><img
                    class="icon-img"
                    src="/static/images/icon/cli-icon_my-amount-all.png"
                    alt=""
                  />
                  기부총액</span
                >
                <span class="db-totalPoint cBlue"
                  >{{
                    formatNumber(userStatusInfoData.userTotalDonation)
                  }}
                  원</span
                >
              </div>
              <div class="user_data_cols">
                <span class="pointblue"
                  ><img
                    class="icon-img"
                    src="/static/images/icon/cli-icon_my-point.png"
                    alt=""
                  />기부포인트</span
                >
                <span class="db-totalPoint cBlue"
                  >{{
                    formatNumber(userStatusInfoData.userDonationPoint)
                  }}
                  P</span
                >
              </div>
            </div>
          </div>
          <div class="user-status-info" v-else-if="showLogin">
            <div class="info-top">
              <div class="user-name">
                <img src="/static/images/icon/cli-icon_bullet-a.png" alt="" />
                <span class="db-userName cBlue">{{
                  userStatusInfoData.userName
                }}</span>
                로그인 해주세요.
              </div>
              <a
                href="/users/login.html"
                class="btn_login d-md-block"
                v-if="showLogin"
              >
                <img
                  class="icon-img"
                  src="/static/images/icon/login-white.png"
                  alt=""
                  title="로그인 하기"
                />
                로그인
              </a>
            </div>
          </div>
          <div class="all-menu">
            <nav class="m-all-menu-list">
              <ul class="all-main-menu mobileM">
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown"
                    >기부하기</a
                  >
                  <ul class="all-sub-menu dropdown-menu" aria-label="dLabel">
                    <li>
					  <a href="javascript:void(0);" onclick="javascript:$s.donation.goDonationPage();">
                      <!--<a href="/donation/donation.html">-->기부하기</a>
                    </li>
                    <li>
                      <a href="/donation/list-select.html">기금사업 소개</a>
                    </li>
                  </ul>
                </li>
                <li class="dropdown">
                  <a
                    href="/donation/guide1.html"
                    class="dropdown-toggle"
                    data-toggle="dropdown"
                    >기부안내</a
                  >
                  <ul class="all-sub-menu dropdown-menu" aria-label="dLabel">
                    <li>
                      <a href="/donation/guide1.html">고향사랑기부제 안내</a>
                    </li>
                    <li>
                      <a href="/donation/guide2.html">온라인 기부절차안내</a>
                    </li>
                    <li>
                      <a href="/donation/guide5.html">오프라인 기부절차안내</a>
                    </li>
                    <li>
                      <a href="/donation/guide3.html"
                        >연말정산 간편서비스 안내</a
                      >
                    </li>
                    <!-- <li><a href="/donation/guide4.html">종합소득세 확정신고 서비스 안내</a></li> -->
                    <li>
                      <a href="/donation/guide6.html"
                        >고향사랑 기부시 유의사항</a
                      >
                    </li>
                  </ul>
                </li>

                <li class="dropdown">
                  <a
                    href="/goods/index.html"
                    class="dropdown-toggle"
                    data-toggle="dropdown"
                    >답례품</a
                  >
                  <ul class="all-sub-menu dropdown-menu" aria-label="dLabel">
                    <li><a href="/goods/index.html">답례품몰</a></li>
                    <!--<li><a href="/featured/eventList.html">지역 이벤트</a></li>-->
                    <li><a href="/event/seasonList.html">제철식품관</a></li>
                    <!--<li><a href="/event/specialityList.html">특산물관</a></li>-->
                  </ul>
                </li>
                <li class="dropdown">
                  <a
                    href="/notice/list.html"
                    class="dropdown-toggle"
                    data-toggle="dropdown"
                    >고객센터</a
                  >
                  <ul class="all-sub-menu dropdown-menu" aria-label="dLabel">
                    <li><a href="/notice/list.html">공지사항</a></li>
                    <li><a href="/data-board/list.html">자료실</a></li>
                    <li><a href="/qna/qna-open.html">Q＆A</a></li>
                    <li><a href="/faq/list.html">FAQ</a></li>
                    <!--<li><a href="/guide/help.html">도움말</a></li>-->
                    <li><a href="/guide/sitemap.html">사이트맵</a></li>
                  </ul>
                </li>
                <li class="dropdown">
                  <a
                    href="/mypage/index.html"
                    class="dropdown-toggle"
                    data-toggle="dropdown"
                    >마이페이지</a
                  >
                  <ul class="all-sub-menu dropdown-menu" aria-label="dLabel">
                    <li><a href="/mypage/index.html">마이페이지</a></li>
                    <li>
                      <a href="/mypage/cntrList.html">기부</a>
                      <ul class="all-sub-sub-menu">
                        <li>
                          <a href="/mypage/cntrList.html">기부내역 현황</a>
                        </li>
                        <li>
                          <a href="/mypage/cntrPoint.html">기부포인트 현황</a>
                        </li>
                      </ul>
                    </li>
                    <li>
                      <a href="/mypage/orderList.html">답례품</a>
                      <ul class="all-sub-sub-menu">
                        <li><a href="/mypage/orderList.html">주문조회</a></li>
                        <li>
                          <a href="/mypage/orderCancel.html">취소반품교환</a>
                        </li>
                        <li><a href="/mypage/review.html">답례품후기</a></li>
                        <li>
                          <a href="/mypage/inquiryItem.html">답례품Q＆A</a>
                        </li>
                        <li>
                          <a href="/mypage/deliveryInfo.html">배송지 관리</a>
                        </li>
                        <li><a href="/cart/index.html">장바구니</a></li>
                      </ul>
                    </li>
                    <li>
                      <a href="/mypage/intrstLocGov.html">관심정보</a>
                      <ul class="all-sub-sub-menu">
                        <li>
                          <a href="/mypage/intrstLocGov.html">관심지자체</a>
                        </li>
                        <li><a href="/mypage/favorItem.html">관심답례품</a></li>
                      </ul>
                    </li>
                    <li><a href="/users/modify.html">회원정보수정</a></li>
                    <li><a href="/mypage/inquiry.html">1:1 문의</a></li>
                    <li><a href="/mypage/honorList.html">명예의 전당</a></li>
                  </ul>
                </li>
                <li class="dropdown">
                  <a
                    href="/users/join.html"
                    class="dropdown-toggle"
                    data-toggle="dropdown"
                    >로그인</a
                  >
                  <ul class="all-sub-menu dropdown-menu" aria-label="dLabel">
                    <li>
                      <a
                        href="javascript:void(0);"
                        onclick="goToJoinPageBefore()"
                        v-if="showLogin"
                        >회원가입</a
                      >
                    </li>
                    <li>
                      <a href="/users/login.html" v-if="showLogin">로그인</a>
                      <ul class="all-sub-sub-menu" v-if="showLogin">
                        <li><a href="/users/login.html">아이디 로그인</a></li>
                        <li>
                          <a href="/users/login.html">간편/금융 인증 로그인</a>
                        </li>
                        <li>
                          <a href="/users/login.html">디지털원패스 로그인</a>
                        </li>
                      </ul>
                    </li>
                    <li>
                      <a href="/users/find-idpw.html" v-if="showLogin"
                        >아이디 / 비밀번호 찾기</a
                      >
                      <ul class="all-sub-sub-menu" v-if="showLogin">
                        <li><a href="/users/find-idpw.html">아이디 찾기</a></li>
                        <li>
                          <a href="/users/find-idpw.html">비밀번호 찾기</a>
                        </li>
                      </ul>
                    </li>
                    <li><a href="/policy/auth.html">이용약관</a></li>
                    <li><a href="/policy/privacy.html">개인정보처리방침</a></li>
                    <li><a href="/policy/copyright.html">저작권정책</a></li>
                  </ul>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
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
});

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
    "total-search": httpVueLoader("/components/layouts/totalSearch.vue"),
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
      },
      searchWord: "",
      latelySearch: [],
      bestSearchWord: null,
      latelyItems: [],
      recommendSearch: {},
      gnbSwiperOption: {
        slidesPerView: "auto",
        spaceBetween: 40,
        navigation: {
          nextEl: ".swiper-contents .swiper-button-next",
          prevEl: ".swiper-contents .swiper-button-prev",
        },
        sliderMove: 1,
        on: {
          init: function () {
            if (
              $(".gnb_slider_pc .swiper-button-prev").is(
                ".swiper-button-disabled"
              )
            ) {
              $(".gnb_slider_pc .swiper-wrapper").removeClass("prv_v");
            } else {
              $(".gnb_slider_pc .swiper-wrapper").addClass("prv_v");
            }
            if (
              $(".gnb_slider_pc .swiper-button-next").is(
                ".swiper-button-disabled"
              )
            ) {
              $(".gnb_slider_pc .swiper-wrapper").removeClass("nxt_v");
            } else {
              $(".gnb_slider_pc .swiper-wrapper").addClass("nxt_v");
            }
          },
        },
      },
      goToSearch: "",
    };
  },
  computed: {
    showLogin: function () {
      return !(this.isLogin || this.isGuestLogin);
    },
  },
  methods: {
    // 통합검색
    searchModal(e) {
      e.stopPropagation();
      $(".main-search-box").show();
      $("#m-all-menu")
        .fadeOut(100)
        .animate({ right: "-100%", "background-color": "" }, 200);
      setTimeout(() => {
        $('.serch-input-area input[type="search"]').focus();
      }, 100);
    },

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
    goLogin: function () {
      $s.redirect(
        $s.pages.LOGIN +
          "?target=" +
          encodeURIComponent($s.requestContext.requestFullUri)
      ); // 로그인 후 로그인 진입 전 화면으로 이동하도록 수정
    },
    goCatalog: function () {
    	$s.redirect('/catalog/index.html');
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

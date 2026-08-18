<template>
  <header id="header" class="header2">
    <div class="header_top top-header">
      <div class="container">
        <div class="top-navi">
          <div class="user-status-info" v-if="!showLogin">
          <!-- <div class="user-status-info"> -->
            <a href="/">
              <span class="db-userName cBlue">{{
                userStatusInfoData.userName
              }}</span>
              회원님 환영합니다.
            </a>
            <a href="/">
              기부총액
              <span class="db-totalPoint cBlue"
                >{{ userStatusInfoData.userTotalDonation }} 원</span
              >
            </a>
            <a href="/">
              기부포인트
              <span class="db-totalPoint cBlue"
                >{{ userStatusInfoData.userDonationPoint }} P</span
              >
            </a>
          </div>
          <!-- <div class="user-status-info" v-else-if="showLogin">
            <a href="UP_0101.html"> 로그인하세요:) </a>
          </div> -->
          <a
            href="/"
            class="btn_logout d-none d-md-block"
            v-if="!showLogin"
            @click.prevent="logout"
          >
            <img
              src="/static/images/icon/cli-icon_logout.png"
              alt=""
              title="로그아웃 하기"
            />
            로그아웃
          </a>
          <a
            href="/users/login.html"
            class="btn_login d-none d-md-block"
            v-else-if="showLogin"
          >
            <img
              src="/static/images/icon/cli-icon_login.png"
              alt=""
              title="로그인 하기"
            />
            로그인
          </a>
          <a
            href="javascript:void(0)"
            v-if="!showLogin"
            @click="getMyPage()"
            class="btn_mypage d-none d-md-block"
          >
            <img
              src="/static/images/icon/cli-icon_mypage.png"
              alt=""
              title="마이페이지"
            />
            마이페이지
          </a>

          <a
            href="UI_P010301.html"
            v-else-if="showLogin"
            @click="goToJoinPage()"
            class="btn_mypage d-none d-md-block"
          >
            <img
              src="/static/images/icon/cli-icon_mypage.png"
              alt=""
              title="회원가입"
            />
            회원가입
          </a>

          <a href="/cart/index.html" class="btn_cart">
            <img
              src="/static/images/icon/cli-icon_cart.png"
              alt=""
            />
            장바구니
          </a>
        </div>
      </div>
    </div>
    <div class="main-header">
      <div class="container">
        <h1 class="logo">
          <a href="UI_P02010.html"
            ><img src="/static/images/cli-logo.png" alt="고향사랑e음 로고"
          /></a>
        </h1>
        <nav id="gnb">
          <ul class="main-menu">
            <li>
              <a href="UI_P03010.html">
                <span>고향사랑e음<span class="underLine"></span></span>
              </a>
              <ul class="sub-menu">
                <li>
                  <a href="UI_P03010.html">기부안내</a>
                  <ul class="sub-sub-menu">
                    <li>
                      <a href="UI_P03020.html">고향사랑e음 안내</a>
                    </li>
                    <li><a href="UI_P03030.html">기부금 절차 안내</a></li>
                    <li>
                      <a href="UI_P03040.html">연말정산 간편서비스 안내</a>
                    </li>
                    <li>
                      <a href="UI_P03050.html"
                        >종합소득세 확정신고 서비스 안내</a
                      >
                    </li>
                    <li>
                      <a href="UI_P03060.html">오프라인 기부금 안내</a>
                    </li>
                    <li><a href="UI_P03070.html">기부금 위반사례</a></li>
                  </ul>
                </li>
                <li>
                  <a href="UI_P03080101.html">기부하기</a>
                  <ul class="sub-sub-menu">
                    <li>
                      <a href="UI_P03080102.html"
                        >기부 지자체 선택 - 지도</a
                      >
                    </li>
                    <li>
                      <a href="UI_P03090101.html"
                        >기부 지자체 선택 - 목록</a
                      >
                    </li>
                  </ul>
                </li>
              </ul>
            </li>
            <li>
              <!--<a href="https://gohyang01.bbiz.kr//" target="_blank" title="새창 열림">
                <span>답례품<span class="underLine"></span></span>
              </a>-->
              <a href="https://shop.ilovegohyang.go.kr//" target="_blank" title="새창 열림">
                <span>답례품<span class="underLine"></span></span>
              </a>
            </li>
            <li>
              <a href="UI_P050101.html">
                <span>고객센터<span class="underLine"></span></span>
              </a>
              <ul class="sub-menu">
                <li><a href="UI_P050101.html">공지사항</a></li>
                <li><a href="UI_P050201.html">자료실</a></li>
                <li><a href="UI_P050301.html">Q&A</a></li>
                <li><a href="UI_P050401.html">FAQ</a></li>
                <li><a href="UI_P050601.html">도움말</a></li>
                <li><a href="UI_P050701.html">1:1 문의</a></li>
                <li><a href="UI_P050801.html">민원신고</a></li>
                <li><a href="UI_P050501.html">사이트맵</a></li>
              </ul>
            </li>
          </ul>
          <div class="hamberger">
            <img src="/static/images/icon/cli-icon_hamberger.png" alt="" />
          </div>
        </nav>

        <nav id="m-hamberger">
          <img src="/static/images/icon/cli-icon_hamberger.png" alt="" />
        </nav>
      </div>
    </div>

    <!-- Desktop All Menu  -->
    <div class="dt-all-menu">
      <div class="container all-menu">
        <nav class="dt-all-menu-list">
          <ul class="all-main-menu">
            <li>
              <a href="UI_P03010.html">고향사랑e음</a>
              <ul class="all-sub-menu">
                <li>
                  <a href="UI_P03010.html">기부안내</a>
                  <ul class="all-sub-sub-menu">
                    <li>
                      <a href="UI_P03020.html">고향사랑e음 안내</a>
                    </li>
                    <li><a href="UI_P03030.html">기부 절차 안내</a></li>
                    <li>
                      <a href="UI_P03040.html">연말정산 간편서비스 안내</a>
                    </li>
                    <li>
                      <a href="UI_P03050.html"
                        >종합소득세 확정신고 서비스 안내</a
                      >
                    </li>
                    <li>
                      <a href="UI_P03060.html">오프라인 기부 안내</a>
                    </li>
                    <li>
                      <a href="UI_P03070.html">고향사랑e음 위반사례</a>
                    </li>
                  </ul>
                </li>
                <li>
                  <a href="UI_P03080101.html">기부하기</a>
                  <ul class="all-sub-sub-menu">
                    <li>
                      <a href="UI_P03080102.html"
                        >기부 지자체 선택(지도)</a
                      >
                    </li>
                    <li>
                      <a href="UI_P03090101.html"
                        >기부 지자체 선택(목록)</a
                      >
                    </li>
                  </ul>
                </li>
              </ul>
            </li>
          </ul>

          <ul class="all-main-menu">
            <li>
              <a href="UI_P040101.html">마이페이지</a>
              <ul class="all-sub-menu">
                <li><a href="UI_P040201.html">기부 내역 현황</a></li>
                <li><a href="UI_P040301.html">기부 포인트 현황</a></li>
                <li><a href="UI_P04050.html">관심지자체</a></li>
                <li>
                  <a href="UI_P04060.html">회원정보수정</a>
                  <ul class="all-sub-sub-menu">
                    <li><a href="UI_P040601.html">회원탈퇴</a></li>
                  </ul>
                </li>
              </ul>
            </li>
            <li class="addlist">
              <a href="UI_P040101.html">답례품</a>
              <ul class="all-sub-menu">
                <li><a href="UI_P040201.html">답례품 메인</a></li>
                <li><a href="UI_P04060.html">장바구니</a></li>
              </ul>
            </li>
          </ul>

          <ul class="all-main-menu">
            <li>
              <a href="UI_P050101.html">고객센터</a>
              <ul class="all-sub-menu">
                <li><a href="UI_P050101.html">공지사항</a></li>
                <li><a href="UI_P050201.html">자료실</a></li>
                <li><a href="UI_P050301.html">QnA</a></li>
                <li><a href="UI_P050401.html">FAQ</a></li>
                <li><a href="UI_P050601.html">도움말</a></li>
                <li><a href="UI_P050701.html">1:1문의</a></li>
                <li><a href="UI_P050801.html">민원신고</a></li>
                <li><a href="UI_P050501.html">사이트맵</a></li>
              </ul>
            </li>
          </ul>

          <ul class="all-main-menu">
            <li>
              <a href="UI_P0101.html">로그인</a>
              <ul class="all-sub-menu">
                <li>
                  <a href="UI_P0101.html"
                    >로그인
                    <span class="smallText"
                      >(아이디, 디지털원패스, 간편인증)</span
                    ></a
                  >
                </li>
                <li>
                  <a href="UI_P010201.html">아이디 / 비밀번호 찾기</a>
                </li>
                <li><a href="UI_P010301.html">회원가입</a></li>
                <li><a href="UI_P01060.html">이용약관</a></li>
                <li><a href="UI_P01070.html">개인정보보호정책</a></li>
                <li><a href="UI_P01080.html">저작권정책</a></li>
              </ul>
            </li>
          </ul>
        </nav>
      </div>
    </div>
<!-- mobile All Menu  -->
<div class="m-all-menu" id="m-all-menu">
      <div class="m-all-menu-wrap">
        <img
          src="/static/images/icon/cli-icon_btn-close.png"
          alt=""
          id="closebtn"
        />
        <div class="top-navi">
          <a
            href="/"
            class="btn_logout d-md-block"
            v-if="!showLogin"
            @click.prevent="logout"
          >
            <img
              src="/static/images/icon/cli-icon_logout.png"
              alt=""
              title="로그아웃 하기"
            />
            로그아웃
          </a>
          <a
            href="UI_P0101.html"
            class="btn_login d-md-block"
            v-else-if="showLogin"
          >
            <img
              src="/static/images/icon/cli-icon_login.png"
              alt=""
              title="로그인 하기"
            />
            로그인
          </a>
          <a
            href="UI_P040101.html"
            v-if="!showLogin"
            @click="getMyPage()"
            class="btn_mypage d-md-block"
          >
            <img
              src="/static/images/icon/cli-icon_mypage.png"
              alt=""
              title="마이페이지"
            />
            마이페이지
          </a>

          <a
            href="UI_P010301.html"
            v-else-if="showLogin"
            @click="goToJoinPage()"
            class="btn_mypage d-md-block"
          >
            <img
              src="/static/images/icon/cli-icon_mypage.png"
              alt=""
              title="회원가입"
            />
            회원가입
          </a>

          <a href="/cart/index.html" class="btn_cart">
            <img
              src="/static/images/icon/cli-icon_cart.png"
              alt=""
            />
            장바구니
          </a>
        </div>
        <div class="container">
          <div class="user-status-info" v-if="!showLogin">
            <div class="user-name">
              <img src="/static/images/icon/cli-icon_bullet-a.png" alt="" />
              <span class="db-userName cBlue">{{
                userStatusInfoData.userName
              }}</span>
              회원님 환영합니다.
            </div>
            <div class="user-data">
              <a href="">
                <span class="pointblue"
                  ><img
                    src="/static/images/icon/cli-icon_my-amount-all.png"
                    alt=""
                  />
                  기부총액</span
                >
                <span class="db-totalPoint cBlue"
                  >{{ userStatusInfoData.userTotalDonation }} 원</span
                >
              </a>
              <div class="v-line"></div>
              <a href="/">
                <span class="pointblue"
                  ><img
                    src="/static/images/icon/cli-icon_my-point.png"
                    alt=""

                  />기부포인트</span
                >
                <span class="db-totalPoint cBlue"
                  >{{ userStatusInfoData.userDonationPoint }} P</span
                >
              </a>
            </div>
          </div>
          <div class="all-menu">
            <nav class="m-all-menu-list">
              <ul class="all-main-menu">
                <li>
                  <a href="#"
                    >고향사랑e음
                    <img
                      src="/static/images/icon/cli-icon_menu-arrow.png"
                      alt=""
                      class="m-arrow"
                  /></a>
                  <ul class="all-sub-menu">
                    <li>
                      <a href="UI_P03010.html">기부안내</a>
                      <ul class="all-sub-sub-menu">
                        <li>
                          <a href="UI_P03020.html">고향사랑e음 안내</a>
                        </li>
                        <li>
                          <a href="UI_P03030.html">기부 절차 안내</a>
                        </li>
                        <li>
                          <a href="UI_P03040.html"
                            >연말정산 간편서비스 안내</a
                          >
                        </li>
                        <li>
                          <a href="UI_P03050.html"
                            >종합소득세 확정신고 서비스 안내</a
                          >
                        </li>
                        <li>
                          <a href="UI_P03060.html">오프라인 기부 안내</a>
                        </li>
                        <li>
                          <a href="UI_P03070.html"
                            >고향사랑e음 위반사례</a
                          >
                        </li>
                      </ul>
                    </li>
                    <li>
                      <a href="UI_P03080101.html">기부하기</a>
                      <ul class="all-sub-sub-menu">
                        <li>
                          <a href="UI_P03080102.html"
                            >기부 지자체 선택(지도)</a
                          >
                        </li>
                        <li>
                          <a href="UI_P03090101.html"
                            >기부 지자체 선택(목록)</a
                          >
                        </li>
                      </ul>
                    </li>
                  </ul>
                </li>

                <li>
                  <a href="#"
                    >마이페이지
                    <img
                      src="/static/images/icon/cli-icon_menu-arrow.png"
                      alt=""
                      class="m-arrow"
                  /></a>
                  <ul class="all-sub-menu">
                    <li><a href="UI_P040201.html">기부 내역 현황</a></li>
                    <li><a href="UI_P040301.html">기부 포인트 현황</a></li>
                    <li><a href="UI_P04050.html">관심지자체</a></li>
                    <li>
                      <a href="UI_P04060.html">회원정보수정</a>
                      <ul class="all-sub-sub-menu">
                        <li><a href="UI_P040601.html">회원탈퇴</a></li>
                      </ul>
                    </li>
                  </ul>
                </li>
                <li>
                  <a href="#"
                    >답례품
                    <img
                      src="/static/images/icon/cli-icon_menu-arrow.png"
                      alt=""
                      class="m-arrow"
                  /></a>
                  <ul class="all-sub-menu">
                    <li><a href="UI_P040201.html">답례품 메인</a></li>
                    <li><a href="UI_P04060.html">장바구니</a></li>
                  </ul>
                </li>

                <li>
                  <a href="#"
                    >고객센터
                    <img
                      src="/static/images/icon/cli-icon_menu-arrow.png"
                      alt=""
                      class="m-arrow"
                  /></a>
                  <ul class="all-sub-menu">
                    <li><a href="UI_P050101.html">공지사항</a></li>
                    <li><a href="UI_P050201.html">자료실</a></li>
                    <li><a href="UI_P050301.html">QnA</a></li>
                    <li><a href="UI_P050401.html">FAQ</a></li>
                    <li><a href="UI_P050601.html">도움말</a></li>
                    <li><a href="UI_P050701.html">1:1문의</a></li>
                    <li><a href="UI_P050801.html">민원신고</a></li>
                    <li><a href="UI_P050501.html">사이트맵</a></li>
                  </ul>
                </li>

                <li>
                  <a href="#"
                    >로그인
                    <img
                      src="/static/images/icon/cli-icon_menu-arrow.png"
                      alt=""
                      class="m-arrow"
                  /></a>
                  <ul class="all-sub-menu">
                    <li>
                      <a href="UI_P0101.html"
                        >로그인
                        <span class="smallText"
                          >(아이디, 디지털원패스, 간편인증)</span
                        ></a
                      >
                    </li>
                    <li>
                      <a href="UI_P010201.html">아이디 / 비밀번호 찾기</a>
                    </li>
                    <li><a href="UI_P010301.html">회원가입</a></li>
                    <li><a href="UI_P01060.html">이용약관</a></li>
                    <li><a href="UI_P01070.html">개인정보보호정책</a></li>
                    <li><a href="UI_P01080.html">저작권정책</a></li>
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


<script>
$(document).ready(function () {
  // scroll Fixed Header
  window.onscroll = function() {myFunction()};

  var navbar = document.getElementById("header");
  var sticky = navbar.offsetTop;

  function myFunction() {
    if (window.pageYOffset >= sticky || window.pageYOffset >= 0) {
      navbar.classList.add("sticky");
    } else {
      navbar.classList.remove("sticky");
    }
  }


  // browser resizing
  var delay = 100;
  var timer = null;

  $(window).on("resize", function () {

    // 브라우저 과부하 방지
    clearTimeout(timer);
    timer = setTimeout(function () {
      $(".dt-all-menu").css("display", "none");
      $(".m-all-menu").animate({ right: "-100%" }, 0);

    }, delay);
  });

  // main-menu SlideToggle
  $("ul.main-menu > li").hover(
    function () {
      $("ul.sub-menu", this).stop().slideDown("fast");
    },
    function () {
      $("ul.sub-menu").stop().slideUp("fast");
    }
  );

  // Desktop Hamberger Click
  $(".hamberger").click(function () {
    $(".dt-all-menu").slideToggle(100);
  });
  // close Desktop All Menu
  $("button.dt-all-close").click(function () {

    $(".dt-all-menu").slideUp(100);
  });

  // Mobile Hamberger Click
  $("#m-hamberger").click(function () {
    $("#m-all-menu").fadeIn(100).animate({ right: "0" }, 200, function () {
      $(this).css({ "background-color": "rgba(0, 0, 0, 0.3)" });
    });
  });

  // close Mobile Menu
  $("#closebtn").click(function () {
    $("#m-all-menu").fadeOut(100).animate({ right: "-100%", "background-color": "" }, 200);
  });

  // main-list Click
  $("ul.all-main-menu>li>a").click(function(){
    $(this).children("img").css("transform","rotate(0deg)");
    // $(this).parent().siblings().children("m-arrow").css("transform","rotate(180deg)");
    $(this).siblings().slideToggle().parent().siblings().children('.all-sub-menu').slideUp().siblings().children("img").css("transform","rotate(180deg)");
  });

});


module.exports = {
  props: {
    newCartQuantity: 0,
    gnbMenuTarget: {
      type: String,
      default: function () {
        return "";
      },
    },
  },
  data: function () {
    return {
      // alicia_Binding Test Data start
      userStatusInfoData: {
        userName: "홍길동",
        userTotalDonation: "25,000,000",
        userDonationPoint: "25,000,000",
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
    };
  },
  computed: {
    displayCartQuantites: function () {
      return this.newCartQuantity > this.cartQuantity
        ? this.newCartQuantity
        : this.cartQuantity;
    },
    showLogin: function () {
      return !(this.isLogin || this.isGuestLogin);
    },
  },
  methods: {
    goToJoinPage: function () {
      location.href = "UI_P010404.html";
      // 회원가입 URL 지정하기
    },

    getMyPage: function () {
      this.isLogin = $s.isLogin();
      if (this.isLogin) {
        location.href = "/mypage/index.html";
      } else {
        this.redirect($s.pages.LOGIN + "?target=" + "/mypage/index.html");
      }
    },
    getCartInfo: function () {
      var self = this;
      $s.api.getCartInfo(
        function (response) {
          self.cartQuantity = response.cartQuantity;
        },
        function (error) {
          $s.alert(error.response.data.message);
        }
      );
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

        $s.axios.get("https://api.ipify.org", {}, {}).then(function (response) {
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
        });
      }
    },
  },
  mounted: function () {
    this.$nextTick(function () {
      this.isLogin = $s.isLogin();
      this.isGuestLogin = $s.isGuestLogin();

      this.getCartInfo();
      this.getBestSearchWord();
      this.getRecommendSearchWord();
      this.categoryInfo(this);
      this.latelyInfo(this);
      this.latelyItemInfo(this);
      this.saveVisitData();

      initializeHeaderEvent();
    });
  },
};
</script>

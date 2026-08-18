<template>
  <div class="footer_wrap">
    <footer id="krds-footer">
      <!-- inner -->
      <div class="inner">
        <div class="f-logo">
          <span class="sr-only">행정안전부</span>
        </div>
        <div class="f-info">
          <p class="f-addr">
            <span>본관 : 30112 세종특별자치시 도움6로 42(어진동)</span>
            <span class="f-addr-bar">/</span>
            <span>별관 : 30116 세종특별자치시 가름로 143(어진동)</span>
          </p>
          <p class="f-addr f-addr--mob">
              <span>고향사랑e음 기부 가능 시간 <strong>1:00 ~ 23:30</strong></span>
              <span>고객센터 <strong>1522-2431</strong> (월~금 9:00 ~ 18:00, 공휴일 제외)</span>
            </p>
          <div class="info-cs--pc">
            <div class="f-title">고향사랑e음 고객센터</div>
            <ul class="info-cs">
              <li>
                <strong class="key-info">
                  <strong>1522-2431</strong>
                  <span>(월 ~ 금 9:00 ~ 18:00, 공휴일 제외)</span>
                </strong>
                <span class="more-info">고향사랑e음 기부 가능 시간 : 1:00 ~ 23:30</span>
              </li>
            </ul>
          </div>
        </div>
        <div class="f-btm">
          <ul class="f-menu">
            <li v-for="(fnb, i) in fnbData" :key="i">
            	<a href="javascript:void(0);" class="krds-btn text open-modal modal-opened"
            		v-if="fnb.url == undefined"  data-target="modal_sitemap"

            	>{{fnb.menuTxt}}</a>
            	<a :href="fnb.url" class="krds-btn text" :class="fnb.menuTxt == '개인정보처리방침'? 'point' : ''" v-else :target="fnb.accessibility ? '_blank' : null" :title="fnb.accessibility ? '새창열림' : null">{{fnb.menuTxt}}</a>
            </li>
          </ul>
          <p class="f-copy">© Ministry of the Interior and Safety. All rights reserved.</p>
        </div>
		<a href="http://www.kwacc.or.kr/CertificationSite/WA/2326/Detail?page=1" target="_blank" title="한국디지털접근성진흥원 홈페이지 바로가기_새창" class="btn-accessibility"><img src="/static/images/accessibility-logo_2026.png"  alt="국가공인 정보통신접근성 품질인증마크"></a>
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

		<!-- 사이트맵 모달 -->
    <section id="modal_sitemap" class="krds-modal" role="dialog" data-type="full">
      <div class="modal-dialog">
        <div class="modal-content sitemap">
          <!-- modal contents -->
          <div class="modal-header">
            <h2 class="modal-title sitemap__tit">사이트맵</h2>
          </div>
          <!-- [250820] 수정 -->
          <div class="modal-conts">
            <nav class="sitemap__nav" aria-label="사이트맵">
              <div class="sitemap__grid">
                <div class="sitemap__col sitemap__col--donation">
                  <h3 class="sitemap__title">기부</h3>
                  <ul class="sitemap__list">
                  	<li v-for="(give, idx) in siteMapList.give" :key="idx">
                  		<a :href="give.url">{{give.title}}</a>
                  	</li>
                  </ul>
                </div>
                <div class="sitemap__col sitemap__col--goods">
                  <h3 class="sitemap__title">답례품</h3>
                  <ul class="sitemap__list">
                    <li v-for="(item, idx) in siteMapList.item" :key="idx">
                  		<a :href="item.url">{{item.title}}</a>
                  	</li>
                  </ul>
                </div>
                <div class="sitemap__col sitemap__col--center">
                  <h3 class="sitemap__title">고객센터</h3>
                  <ul class="sitemap__list">
                    <li v-for="(cs, idx) in siteMapList.cs" :key="idx">
                  		<a :href="cs.url">{{cs.title}}</a>
                  	</li>
                  </ul>
                </div>
                <div class="sitemap__col sitemap__col--guide">
                  <h3 class="sitemap__title">안내사항</h3>
                  <ul class="sitemap__list">
                    <li v-for="(notice, idx) in siteMapList.notice" :key="idx">
                  		<a :href="notice.url">{{notice.title}}</a>
                  	</li>
                  </ul>
                </div>
                <div class="sitemap__col sitemap__col--event">
                  <h3 class="sitemap__title">이벤트</h3>
                  <ul class="sitemap__list">
                    <li v-for="(event, idx) in siteMapList.event" :key="idx">
                  		<a :href="event.url">{{event.title}}</a>
                  	</li>
                  </ul>
                </div>
                <div class="sitemap__col sitemap__col--mypage">
                  <h3 class="sitemap__title">마이페이지</h3>
                  <ul class="sitemap__list">
                    <li v-for="(mypage, idx) in siteMapList.mypage" :key="idx">
                  		<a :href="mypage.url">{{mypage.title}}</a>
                  	</li>
                  </ul>
                </div>
              </div>
            </nav>
          </div>
          <button type="button" class="krds-btn icon btn-close close-modal">
            <span class="btn-close-txt">닫기</span>
            <i class="svg-icon ico-popup-close"></i>
          </button>
        </div>
      </div>
      <div class="modal-back"></div>
    </section>
    <!-- 사이트맵 모달 -->

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
          url: "/policy/privacy.html",
          menuTxt: "개인정보처리방침",
        },
        {
          url: "/policy/copyright.html",
          menuTxt: "저작권정책",
        },
        {
          url: "/policy/auth.html",
          menuTxt: "이용약관",
        },
        {
          menuTxt: "사이트맵",
        },
        {
            url: "/notice/list.html",
            menuTxt: "공지사항",
        },
      ],
      // 사이트맵 리스트
      siteMapList: {
    	  give: [
    		  {
    			  title: '자치단체에 기부하기',
    			  url: '/donation/donation-main.html'
    		  },
    		  {
    			  title: '특정사업에 기부하기',
    			  url: '/designated-donation/index.html'
    		  },
    	  ],
    	  cs: [
    		  {
    			  title: '공지사항',
    			  url: '/notice/list.html',
    		  },
    		  {
    			  title: '자료실',
    			  url: '/data-board/list.html',
    		  },
    		  {
    			  title: 'Q&A',
    			  url: '/qna/qna-open.html',
    		  },
    		  {
    			  title: 'FAQ',
    			  url: '/faq/list.html',
    		  },
    	  ],
    	  item: [
    		  {
    			  title: '답례품몰',
    			  url: '/goods/index-main.html',
    		  },
    		  {
    			  title: '제철식품관',
    			  url: '/event/seasonList-main.html',
    		  },
    	  ],
    	  // TODO :: (김대원) 기존 사이트맵에 없었음 URL 뭔지 확인 필요
    	  event: [
    		  {
    			  title: '진행중인 이벤트',
    			  url: '/featured/eventList.html?ing=Y',
    		  },
    		  {
    			  title: '종료된 이벤트',
    			  url: '/featured/eventList.html?ing=N',
    		  },
    	  ],
    	  notice: [
    		  {
    			  title: '기금사업 소개',
    			  url: '/donation/list-select.html'
    		  },
    		  {
    			  title: '고향사랑기부제 안내',
    			  url: '/donation/guide1.html'
    		  },
    		  {
    			  title: '온라인 기부방법',
    			  url: '/donation/guide2.html'
    		  },
    		  {
    			  title: '오프라인 기부방법',
    			  url: 'http://localhost:3000/donation/guide5.html'
    		  },
    		  {
    			  title: '연말정산 세액공제 안내',
    			  url: '/donation/guide3.html'
    		  },
    		  {
    			  title: '고향사랑기부 주의사항',
    			  url: '/donation/guide6.html'
    		  },
    	  ],
    	  mypage: [
    		  {
    			  title: '기부내역 조회',
    			  url: '/mypage/cntrList.html',
    		  },
    		  {
    			  title: '기부포인트 조회',
    			  url: '/mypage/cntrPoint.html',
    		  },
    		  {
    			  title: '주문조회',
    			  url: '/mypage/orderList.html',
    		  },
    		  {
    			  title: '장바구니',
    			  url: '/cart/index.html',
    		  },
    		  {
    			  title: '취소반품교환',
    			  url: '/mypage/orderCancel.html',
    		  },
    		  {
    			  title: '배송지 관리',
    			  url: '/mypage/deliveryInfo.html',
    		  },
    		  {
    			  title: '답례품 Q&A',
    			  url: '/mypage/inquiryItem.html',
    		  },
    		  {
    			  title: '답례품 후기',
    			  url: '/mypage/review.html',
    		  },
    		  {
    			  title: '기부확인증 보기',
    			  url: '/mypage/receiptList.html',
    		  },
    		  {
    			  title: '기부혜택증 보기',
    			  url: '/mypage/honorList.html',
    		  },
    		  {
    			  title: '1:1 문의',
    			  url: '/mypage/inquiry.html',
    		  },
    		  {
    			  title: '관심지자체',
    			  url: '/mypage/intrstLocGov.html',
    		  },
    		  {
    			  title: '관심답례품',
    			  url: '/mypage/favorItem.html',
    		  },
    		  {
    			  title: '회원탈퇴',
    			  url: '/users/secede.html',
    		  },
    	  ]
      },
      // alicia_Binding Test Data end

      terms: {},
      about: {},
      isLogin: false,
      isGuestLogin: false,
      category: {
        groups: [],
      },
      quick: {
        cartQuantity: 0,
        wishlistCount: 0,
      },
      latelyItems: [],
      latelyItemList: [],

  		// 사이트맵 모달 관련 데이터
      modalOpenTriggers: null,
      modalCloseTriggers: null,
      outsideClickHandlers: {},
    };
  },
  components: {
    alert: httpVueLoader("/components/layouts/alert.vue"),
    toast: httpVueLoader("/components/layouts/toast.vue"),
    layoutLoading: httpVueLoader("/components/layouts/loading.vue"),
    donationLoading: httpVueLoader("/components/layouts/do_loading.vue"),
  },
  methods: {
    logout: function () {
      $s.logout();
    },
    link: function (url, childCategories) {
      var flag = typeof childCategories != "undefined" && childCategories != null && childCategories.length > 0;

      if (!flag) {
        $s.redirect("/category/?code=" + url);
      }
    },

    showLoading: function (isLoading) {
      this.$refs.layoutLoading.showLoading(isLoading);
    },

    showDonationLoading: function (isLoading) {
      this.$refs.donationLoading.showDonationLoading(isLoading);
    },

    loadingMounted: function () {
      this.$emit("loading-mounted");
    },

    donationLoadingMounted: function () {
      this.$emit("donationLoading-mounted");
    },

    /* 사이트맵 모달 관련 함수 */
    modalInit() {
	    this.modalOpenTriggers = document.querySelectorAll(".open-modal");
	    this.modalCloseTriggers = document.querySelectorAll(".close-modal");

	    if (!this.modalOpenTriggers.length || !this.modalCloseTriggers.length) return;

	    this.setupTriggers();
	  },
	  setupTriggers() {
	    // 모달 열기 이벤트 설정
	    this.modalOpenTriggers.forEach((trigger) => {
	      trigger.addEventListener("click", (event) => {
	        event.preventDefault();
	        const modalId = trigger.getAttribute("data-target");
	        if (modalId) {
	          // aria 설정
	          trigger.setAttribute("data-modal-id", modalId);
	          trigger.classList.add("modal-opened");
	          trigger.setAttribute("tabindex", "-1");

	          this.openModal(modalId);
	        }
	      });
	    });
	    // 모달 닫기 이벤트 설정
	    this.modalCloseTriggers.forEach((trigger) => {
	      trigger.addEventListener("click", (event) => {
	        event.preventDefault();
	        const modalId = trigger.closest(".krds-modal").getAttribute("id");

	        if (modalId) {
	          this.closeModal(modalId);
	        }
	      });
	    });
	  },
	  openModal(id) {
	    const modalElement = document.getElementById(id);
	    const dialogElement = modalElement.querySelector(".modal-content");
	    const modalBack = modalElement.querySelector(".modal-back");
	    // const modalTitle = modalElement.querySelector(".modal-title");
	    const modalConts = modalElement.querySelector(".modal-conts");

	    document.querySelector("body").classList.add("scroll-no");
	    dialogElement.removeAttribute("tabindex");
	    modalElement.setAttribute("role", "dialog");
	    modalElement.classList.add("shown");
	    modalBack.classList.add("in");
	    // modalTitle.setAttribute("tabindex", "0");

	    // modal-conts 스크롤 일때 tabindex 처리
	    if (modalConts.scrollHeight > modalConts.clientHeight) {
	      modalConts.setAttribute("tabindex", "0");
	    } else {
	      modalConts.removeAttribute("tabindex");
	    }

	    // css transition 딜레이
	    setTimeout(() => {
	      modalElement.classList.add("in");
	    }, 150);

	    //열린 팝업창 포커스
	    const focusables = modalElement.querySelectorAll(`a, button, [tabindex="0"], input, textarea, select`);
	    setTimeout(() => {
	      // modalTitle.focus();
	      focusables[0].focus();
	    }, 350);

	    // ESC 모달 닫기
	    dialogElement.addEventListener(
	      "keydown",
	      (event) => {
	        if (event.key === "Escape" || event.key === "Esc") {
	          this.closeModal(dialogElement.closest(".krds-modal").id);
	        }
	      },
	      { once: true }
	    );

	    // 모달 외부 클릭 처리 핸들러 정의 및 저장
	    if (!this.outsideClickHandlers[id]) {
	      this.outsideClickHandlers[id] = (event) => {
	        if (!event.target.closest(".modal-content")) {
	          // modalTitle.focus();
	          focusables[0].focus();

	          // dialogElement.focus();
	          // this.closeModal(id);
	        }
	      };
	    }
	    // 이벤트 리스너 제거 후 다시 등록
	    modalElement.removeEventListener("click", this.outsideClickHandlers[id]);
	    modalElement.addEventListener("click", this.outsideClickHandlers[id]);

	    // 포커스 트랩 설정
	    common.focusTrap(dialogElement);

	    // 2개 이상의 모달이 열려 있는 경우 z-index 업데이트
	    this.updateZIndex(modalElement);

	    // inert 설정
	    document.getElementById("wrap")?.setAttribute("inert", "");
	  },
	  closeModal(id) {
	    const modalElement = document.getElementById(id);
	    const openModals = document.querySelectorAll(".modal.in:not(.sample)");
	    const modalBack = modalElement.querySelector(".modal-back");

	    modalElement.classList.remove("in");
	    modalBack.classList.remove("in");

	    // css transition 딜레이
	    setTimeout(() => {
	      modalElement.classList.remove("shown");
	    }, 350);

	    // 마지막 모달이 닫힐 때 페이지 스크롤 복원
	    if (openModals.length < 2) {
	      document.querySelector("body").classList.remove("scroll-no");
	    }

	    // inert 설정
	    document.getElementById("wrap")?.removeAttribute("inert");

	    // 모달을 열었던 버튼으로 포커스 복귀
	    this.returnFocusToTrigger(id);
	  },
	  updateZIndex(modalElement) {
	    const openModals = document.querySelectorAll(".modal.in:not(.sample)");
	    const openModalsLengtn = openModals.length + 1;
	    const newZIndex = 1010 + openModalsLengtn;
	    if (openModalsLengtn > 1) {
	      modalElement.style.zIndex = newZIndex;
	      modalElement.querySelector(".modal-back").classList.remove("in");
	    }
	  },
	  returnFocusToTrigger(id) {
	    const triggerButton = document.querySelector(`.modal-opened[data-modal-id="${id}"]`);
	    if (triggerButton) {
	      triggerButton.focus();
	      triggerButton.setAttribute("tabindex", "0");
	      triggerButton.classList.remove("modal-opened");
	      triggerButton.removeAttribute("data-modal-id");
	    }
	  },
    /* 모달 관련 함수 */
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

      initializeFooterEvent();

      // 사이트맵 모달 Init
      this.modalInit();
    });
  },
};
</script>

<template>
	<div id="header-wrap">
	<!-- 본문 바로가기 영역  -->
      <div id="krds-skip-link">
        <a href="#contents">본문 바로가기</a>
      </div>
      <!-- //본문 바로가기 영역  -->

      <!-- 상단 배너영역 -->
	    <div id="krds-masthead">
	      <div class="toggle-wrap">
	        <div class="toggle-head">
	          <div class="inner">
	            <span class="nuri-txt">이 누리집은 대한민국 공식 전자정부 누리집입니다.</span>
	          </div>
	        </div>
	      </div>
	    </div>
	    <!-- //상단 배너영역 -->
			<!-- 헤더 영역 -->
			<header id="krds-header">

      <!-- 헤더 컨텐츠 영역  -->
      <div class="header-in">
        <!-- 헤더 상단 기타메뉴 -->
      	<div class="header-container">
           <div class="inner">
             <div class="header-branding">
               <h1 class="logo">
                 <a href="/" rel="nosublink nofollow" title="메인페이지로 이동">
                   <span class="sr-only">고향사랑e음 로고</span>
                 </a>
               </h1>
               <span style="margin-left: 10px;" v-if="isDevelopment">개발테스트</span>
               <div class="header-actions">
								<button type="button" class="btn-navi navi-row logout" v-if="!showLogin" @click.prevent="logout">로그아웃</button>
                <button type="button" class="btn-navi navi-row login" v-else-if="showLogin" @click.prevent="goLogin">로그인</button>
                <button type="button" class="btn-navi navi-row join" v-if="showLogin" @click="goToJoinPageBefore">회원가입</button>

			  				<button type="button" class="btn-navi navi-row my" @click="getMyPage">마이페이지</button>
							  <button type="button" class="btn-navi navi-row basket" @click="openPresent">장바구니</button>
                <button type="button" class="btn-navi navi-row confirm" @click="getMyReceipt">기부확인증</button>
<!--                 <button type="button" class="btn-navi navi-row sch open-modal" title="통합검색 레이어" data-target="popTotalSchType1"><span class="sr-only">통합검색</span></button> -->
                <button type="button" class="btn-navi navi-row all" aria-controls="mobile-nav" @click="mobileMenuOpen"><span class="sr-only">전체메뉴</span></button>
               </div>
            </div>
          </div>
        </div>
        <!-- //헤더 상단 기타메뉴 -->

          <!-- 메인메뉴 : 데스크탑 -->
          <nav class="krds-main-menu">
            <div class="inner">
              <!-- [250820] gnb 수정 : 기부 메뉴 수정, 안내사항 추가, 아이콘 삭제 -->
              <ul class="gnb-menu" aria-label="메인 메뉴">
	              <li v-for="(item, index) in menuItems" :key="index">
								  <button
								  	class="gnb-main-trigger"
								  	:class="{ active : activeMainIndex === index }"
								  	@click="toggleMainMenu(index)"
								  	@keydown="mainMenuKeyNav"
								  	data-trigger
								  	:aria-expanded="activeMainIndex === index"
								  	aria-haspopup="true"
								  	:aria-controls="`main-menu-${index}`">{{ item.mainMenu }}</button>
								  <!-- gnb-toggle-wrap -->
								  <div class="gnb-toggle-wrap" :id="`main-menu-${index}`" :class="{ 'is-open' : activeMainIndex === index }">
									<!-- gnb-main-list -->
									<div class="gnb-main-list inner">
									  <!-- gnb-sub-list -->
									  <div class="gnb-sub-list single-list">
											<div class="gnb-sub-content">

											  <ul class="type-description" v-if="item.subMenu.length <= 1">
													<li v-for="(subItem, subIndex) in item.subMenu[0].contents" :key="subIndex">
													  <strong class="tit">
														<a href="javascript:void(0)" @click="handleLocation(subItem)" >{{ subItem.value }}</a>
													  </strong>
													  <p class="txt">{{ subItem.content }}</p>
													</li>
												</ul>

												<!-- [250826] 마이페이지 구조 변경 -->
												<ul class="type-description" v-else-if="item.subMenu.length > 1">
													<li v-for="(subItem, subIndex) in item.subMenu" :key="subIndex">
														<ul class="type-description--sub">
															<li v-for="(subContents, subContentsIndex) in subItem.contents" :key="subContentsIndex">
															  <strong class="tit">
																	<a href="javascript:void(0)" @click="handleLocation(subContents)" >{{ subContents.value }}</a>
															  </strong>
															  <p class="txt">{{ subContents.content }}</p>
															</li>
														</ul>
													</li>
											  </ul>

											</div>
									  </div>
									  <!-- //gnb-sub-list -->
									</div>
									<!-- //gnb-main-list -->
								  </div>
								  <!-- //gnb-toggle-wrap -->
								</li>
              </ul>
              <!-- //[250820] gnb 수정 : 기부 메뉴 수정, 안내사항 추가, 아이콘 삭제 -->

              <button type="button" class="gnb-all-btn open-modal" data-target="modal_sitemap"><span class="sr-only">사이트맵</span></button><!-- [250825] 버튼 추가 -->
            </div>
          </nav>
          <!-- //메인메뉴 : 데스크탑 -->

        </div>
        <!-- //헤더 컨텐츠 영역  -->

        <!-- 메인메뉴 : 모바일 -->
        <nav id="mobile-nav" class="krds-main-menu-mobile">
          <div class="gnb-wrap">
            <!-- gnb-header -->
            <div class="gnb-header">
              <div class="header-actions">
                <button type="button" class="btn-navi navi-row my" @click="getMyPage">마이페이지</button>
                <button type="button" class="btn-navi navi-row basket" @click="openPresent">장바구니</button>
                <button type="button" class="btn-navi navi-row confirm" @click="getMyReceipt">기부확인증</button>
              </div>
            </div>
            <!-- //gnb-header -->

            <!-- gnb-body -->
            <div class="gnb-body">

              <div class="gnb-bod__inner">
	           	  <!-- 총 기부금 -->
		          	<total-give-state></total-give-state>
  	            <!-- //총 기부금 -->

	              <!-- 로그인 전 -->
		            <login-require v-if="!isLogin"></login-require>
		            <!-- //로그인 전 -->

		            <!-- 로그인 후 -->
		            <login-complete v-if="isLogin"></login-complete>
		            <!-- //로그인 후 -->
              </div>

              <!-- gnb-menu -->
              <div class="krds-accordion">

                <div class="accordion-item" v-for="(item, index) in menuItems" :key="index" :class="{ active : activeAccordionIndex === index }">
                  <div class="accordion-header">
                  	<button
										type="button"
										class="btn-accordion"
							  		:class="{ active : activeAccordionIndex === index }"
								  	:aria-expanded="activeAccordionIndex === index"
								  	aria-haspopup="true"
								  	:aria-controls="`accordion-${index}`"
								  	@click="toggleAccordionMenu(index)"
											>{{ item.mainMenu }}</button>
                 	</div>
                  <div class="accordion-collapse" :id="`accordion-${index}`">
                    <div class="accordion-body">
                      <ul class="gnb-mob">
                      	<template v-for="subItem in item.subMenu">
	                        <li v-for="(subContents, subContentsIndex) in subItem.contents" :key="`${item.mainMenu}>${subContents.value}`">
	                        	<a href="javascript:void(0)" @click="handleLocation(subContents)" class="gnb-mob-menu">{{ subContents.value }}</a>
	                       	</li>
                       	</template>
                      </ul>
                    </div>
                  </div>
                </div>


              </div>
              <!-- //gnb-menu -->
            </div>
            <!-- //gnb-body -->

            <!-- gnb-close -->
            <button type="button" class="krds-btn medium icon" id="close-nav" @click="closeMainMenu()">
              <span class="sr-only">전체메뉴 닫기</span>
              <i class="svg-icon ico-popup-close"></i>
            </button>
            <!-- //gnb-close -->
          </div>
        </nav>

        <!-- //메인메뉴 : 모바일 -->
      </header>
      <!-- //헤더 영역 -->
    </div>
</template>
<script type="text/javascript" src="/static/js/netfunnel.js"></script>
<script type="text/javascript" src="/static/js/netfunnel_skin.js"></script>

<script>

module.exports = {
		props:{},
		components:{
// 		    "total-search": httpVueLoader("/components/layouts/totalSearch.vue"),
// 		    "sitemap-modal": httpVueLoader("/components/layouts/sitemapModal.vue"),
				"total-give-state": httpVueLoader("/components/ui/total_give_state.vue"),
				"login-require": httpVueLoader("/components/ui/login-require.vue"),
				"login-complete": httpVueLoader("/components/ui/login-complete.vue"),
		},
		data: function(){
			return{
		      isDevelopment: SERVER_POSITION == "development",
		      isLogin: false,
		      isGuestLogin: false,
		      activeMainIndex:null,
		      activeAccordionIndex:null,
		      backdrop: null,
		      _scrollY: 0,
		      _scrollH: 0,
		      _lastScrollY: 0,


		      menuItems : [
		    			{
		    				mainMenu : "기부"
		    				,subMenu : [
		    						{
		    							colInx	: 1
		    							,contents : [
		    								{
		    									value : "자치단체에 기부하기"
		    									,content : "내 고향에 직접 기부 할 수 있어요"
		    									,url : ""
		    									,func : $s.donation.goDonationPage
		    								},{
		    									value : "특정사업에 기부하기"
		    									,content : "내 고향의 특정사업에 기부 할 수 있어요"
		    									,url : "/designated-donation/index.html"
		    									,func : ""
		    								}
		    							]
		    						}
	    						]
		    			},{
		    			    mainMenu : "답례품"
		    			    ,subMenu : [
		    						{
		    							colInx	: 1
		    							,contents : [
		    								{
		    									value : "답례품몰"
		    									,content : "지역의 답례품을 확인 할 수 있어요"
		    									,url : "/goods/index.html"
		    									,func : ""
		    								},{
		    									value : "제철식품관"
		    									,content : "지역에서 제공하는 제철 식품들을 확인 할 수 있어요"
		    									,url : "/event/seasonList.html"
		    									,func : ""
		    								},{
												value : "마을기업관"
												,content : "마을기업이 생산한 답례품들을 확인 할 수 있어요"
												,url : "/community-business/communityList-main.html"
												,func : ""
											}
		    							]
		    						}
		    					]
		    			},{
		    			    mainMenu : "안내사항"
		    			    ,subMenu : [
		    						{
		    							colInx	: 1
		    							,contents : [
		    								{
		    									value : "기금사업 소개"
		    									,content : "지역의 기금사업들을 볼 수 있어요"
		    									,url : "/donation/list-select.html"
		    									,func : ""
		    								},{
		    									value : "고향사랑기부제 안내"
		    									,content : "고향사랑기부제에 대해 확인할 수 있어요"
		    									,url : "/donation/guide1.html"
		    									,func : ""
		    								},{
		    									value : "온라인 기부방법"
		    									,content : "온라인 기부방법에 대해 확인할 수 있어요"
		    									,url : "/donation/guide2.html"
		    									,func : ""
		    								},{
		    									value : "오프라인 기부방법"
		    									,content : "오프라인 기부방법에 대해 확인할 수 있어요"
		    									,url : "/donation/guide5.html"
		    									,func : ""
		    								},{
		    									value : "연말정산 세액공제 안내"
		    									,content : "내가 기부한 기부금이 연말정산 때 어떻게 세액공제 되는지 확인 할 수 있어요"
		    									,url : "/donation/guide3.html"
		    									,func : ""
		    								},{
		    									value : "고향사랑기부 주의사항"
		    									,content : "고향사랑기부를 하기 전에 주의해야 할 사항을 확인할 수 있어요"
		    									,url : "/donation/guide6.html"
		    									,func : ""
		    								}
		    							]
		    						}
		    					]
		    			},{
		    			    mainMenu : "이벤트"
		    			    ,subMenu : [
		    						{
		    							colInx	: 1
		    							,contents : [
		    								{
		    									value : "진행중인 이벤트"
		    									,content : "진행중인 이벤트를 확인할 수 있어요"
		    									,url : "/featured/eventList.html?ing=Y"
		    									,func : ""
		    								},{
		    									value : "종료된 이벤트"
		    									,content : "종료된 이벤트를 확인할 수 있어요"
		    									,url : "/featured/eventList.html?ing=N"
		    									,func : ""
		    								}
		    							]
		    						}
		    					]
		    			},{
		    			    mainMenu : "고객센터"
		    			    ,subMenu : [
		    			    	{
		    							colInx	: 1
		    							,contents : [
		    								{
		    									value : "공지사항"
		    									,content : "새소식, 운영점검 등 서비스 이용에 필요한 정보를 확인할 수 있어요"
		    									,url : "/notice/list.html"
		    									,func : ""
		    								},{
		    									value : "자료실"
		    									,content : "고향사랑e음 서비스 이용 관련 자료를 확인할 수 있어요"
		    									,url : "/data-board/list.html"
		    									,func : ""
		    								},{
		    									value : "Q&A"
		    									,content : "고향사랑e음 이용 중 궁금한 사항에 대한 상담을 받을 수 있어요"
		    									,url : "/qna/qna-open.html"
		    									,func : ""
		    								},{
		    									value : "FAQ"
		    									,content : "가장 많이 질문 받은 내용을 FAQ 형식으로 확인할 수 있어요"
		    									,url : "/faq/list.html"
		    									,func : ""
		    								}
		    							]
		    						}
		    					]
		    			},{
		    			  mainMenu : "마이페이지"
		    			  ,subMenu: [
		    					{
		    						colInx	: 1
		    						,contents : [
			    						{
			    							value : "기부내역 조회"
			    							,content : "내가 기부한 내역을 확인할 수 있어요"
			    							,url : "/mypage/cntrList.html"
			    							,func : ""
			    						},{
			    							value : "기부포인트 조회"
			    							,content : "나의 기부포인트를 확인할 수 있어요"
			    							,url : "/mypage/cntrPoint.html"
			    							,func : ""
			    						}
		    						]
		    					},{
		    						colInx	: 2
		    						,contents : [
			    						{
			    							value : "주문조회"
			    							,content : "나의 주문내역을 조회할 수 있어요"
			    							,url : ""
			    							,func : this.getOrderList
			    						},{
			    							value : "장바구니"
			    							,content : "내가 담은 답례품을 확인할 수 있어요"
			    							,url : ""
			    							,func : this.openPresent
			    						},{
			    							value : "취소반품교환"
			    							,content : "나의 취소반품교환내역을 확인할 수 있어요"
			    							,url : "/mypage/orderCancel.html"
			    							,func : ""
			    						},{
			    							value : "배송지 관리"
			    							,content : "나의 배송지를 관리할 수 있어요"
			    							,url : "/mypage/deliveryInfo.html"
			    							,func : ""
			    						}
		    						]
		    					},{
		    						colInx	: 3
		    						,contents : [
			    						{
			    							value : "기부확인증 보기"
			    							,content : "내가 기부한 내역을 확인할 수 있어요"
			    							,url :	""
			    							,func : this.getMyReceipt
			    						},{
			    							value : "기부혜택증 보기"
			    							,content : "내가 기부한 고향에서 제공하는 혜택을 확인할 수 있어요"
			    							,url : "/mypage/honorList.html"
			    							,func : ""
			    						}
		    						]
		    					},{
		    						colInx	: 4
		    						,contents : [
			    						{
			    							value : "답례품 Q&A"
			    							,content : "답례품에 대해 궁금한 사항에 대한 상담을 받을 수 있어요"
			    							,url : "/mypage/inquiryItem.html"
			    							,func : ""
			    						},{
			    							value : "답례품 후기"
			    							,content : "내가 남긴 답례품 후기를 확인할 수 있어요"
			    							,url : "/mypage/review.html"
			    							,func : ""
			    						}
		    						]
		    					},{
		    						colInx	: 5
		    						,contents : [
			    						{
			    							value : "1:1 문의"
			    							,content : "고향사랑e음 이용 중 궁금한 사항에 대한 상담을 받을 수 있어요"
			    							,url : "/mypage/inquiry.html"
			    							,func : ""
			    							,colnum : 5
			    						},{
			    							value : "관심지자체"
			    							,content : "내가 관심있는 지역을 확인할 수 있어요"
			    							,url : "/mypage/intrstLocGov.html"
			    							,func : ""
			    							,colnum : 5
			    						},
			    						{
			    							value : "관심답례품"
			    							,content : "내가 관심있는 답례품을 확인할 수 있어요"
			    							,url : "/mypage/favorItem.html"
			    							,func : ""
			    							,colnum : 5
			    						},{
			    							value : "회원탈퇴"
			    							,content : "회원탈퇴를 할 수 있어요"
			    							,url : "/users/secede.html"
			    							,func : ""
			    							,colnum : 5
			    						}
		    						]
		    					}
		    				]
		    			}
		    		],
				}
		},
	  computed: {
	    showLogin: function () {
	      return !(this.isLogin || this.isGuestLogin);
	    },
	  },
	  methods:{
		  /********************** 기존 기능 **********************/
		  /* 로그아웃 */
	    logout: function () {
	        $s.logout();
      },
      /* 로그인 */
      goLogin: function () {
          $s.redirect($s.pages.LOGIN + "?target=" + encodeURIComponent($s.requestContext.requestFullUri)); // 로그인 후 로그인 진입 전 화면으로 이동하도록 수정
      },
      /* 회원가입 */
	    goToJoinPageBefore: function () {
	       if ($s.config.isUseNetFunnel) {
	         NetFunnel_Action({ action_id: "join" }, function (ev, ret) {
	           location.href = "/users/join.html";
	         });
	       } else {
	         location.href = "/users/join.html";
	       }
  		},
      /* 마이페이지 */
      getMyPage: function () {
   	      this.isLogin = $s.isLogin();
   	      if (this.isLogin) {
   	        location.href = "/mypage/index.html";
   	      } else {
   	        this.redirect($s.pages.LOGIN + "?target=" + "/mypage/index.html");
   	      }
 	    },
 	    /* 장바구니 */
 	    openPresent: function () {
      	this.isLogin = $s.isLogin();
 	      if (this.isLogin) {
	         $s.redirect("/cart/index.html");
       	} else {
       		$s.redirect($s.pages.LOGIN + "?target=" + "/mypage/index.html");
       	}
	    },
	    /* 기부확인증 */
 	    getMyReceipt: function () {
				this.isLogin = $s.isLogin();
 	      	if (this.isLogin) {
 	        	sessionStorage.setItem("receiptFromType","main");
 	         	location.href = "/mypage/receiptList.html";
					} else {
						this.redirect($s.pages.LOGIN + "?target=" + "/mypage/index.html");
 	       }
			},
			/* 주문조회 */
    	getOrderList: function () {
        this.isLogin = $s.isLogin();
        if (this.isLogin) {
          location.href = "/mypage/orderList.html";
        } else {
          this.redirect($s.pages.LOGIN + "?target=" + "/mypage/orderList.html");
        }
      },
      /********************** 기존 기능 **********************/

		  /* 메뉴 페이지 이동 이벤트 */
		  handleLocation: function(menuItem){
			  if(menuItem.url){
				  window.location.href = menuItem.url;
			  }else if (typeof menuItem.func === 'function'){
				  menuItem.func();
			  }else{;
					$s.alert("서비스 오픈예정 입니다.");
			  }
		  },
			/* 포커스 트랩 설정 */
	 	  focusTrap: function(trap) {
 		    const focusableElements = trap.querySelectorAll(`a, button, [tabindex="0"], input, textarea, select`);
 		    if (!focusableElements.length) return;

 		    const firstFocusableElement = focusableElements[0];
 		    const lastFocusableElement = focusableElements[focusableElements.length - 1];

 		    trap.addEventListener("keydown", (event) => {
 		      if (event.key === "Tab") {
 		        if (event.shiftKey && document.activeElement === firstFocusableElement) {
 		          event.preventDefault();
 		          lastFocusableElement.focus();
 		        } else if (!event.shiftKey && document.activeElement === lastFocusableElement) {
 		          event.preventDefault();
 		          firstFocusableElement.focus();
 		          // 모달 오픈 후 첫 초점 역방향 제어(modal-content가 첫초점이 아니면 사용 안해도 됨)
 		        } else if (event.key === "Tab" && event.shiftKey && document.activeElement === trap) {
 		          event.preventDefault();
 		          lastFocusableElement.focus();
 		        }
 		      }
 		    });
 		  },
		  /********************** PC 버전 **********************/
	    /* PC버전 초기화 */
		  krds_mainMenuPC_init : function(){
		    // dimed 요소를 설정, 기존 dimed가 없을 경우 생성
		    this.backdrop = document.querySelector(".gnb-backdrop") || this.createBackdrop();

		    // krds-main-menu 외부 클릭시 닫기
		    document.addEventListener("click", ({ target }) => {
		      if (!target.closest(".krds-main-menu")) this.toggleMainMenu(null);
		    });

		    // ESC 키를 눌러 메뉴를 닫거나, TAB 키로 초점이 메뉴 외부로 이동했을 때 메뉴 닫기
		    document.addEventListener("keyup", (event) => {
		      if (event.code === "Escape" || !event.target.closest(".krds-main-menu")) {
		    	  this.toggleMainMenu(null);
		      }
		    });

		  },
			/* 메인메뉴컨트롤 */
			toggleMainMenu: function(index){
				//기존 index와 클릭되는 index를 비교하여 toggle
				this.activeMainIndex = this.activeMainIndex === index ? null : index;
				//document 컨트롤영역
				const isOpen = this.activeMainIndex !== null;
				//메뉴활성화.
				this.toggleBackdrop(isOpen);
				this.toggleScrollbar(isOpen);
				this.adjustSubMenuHeight(document.querySelector(".gnb-main-list"));
			},
			/* document 컨트롤영역 */
			toggleBackdrop: function(isOpen){
				document.querySelector(".gnb-backdrop").classList.toggle("active", isOpen);
			  document.body.classList.toggle("is-gnb-web", isOpen);
			},
			/* 스크롤에 따른 hasScrollY 클래스요소 toggle */
		  toggleScrollbar: function(isEnabled) {
		    const isScrollNeeded = document.body.scrollHeight > window.innerHeight;
		    document.body.classList.toggle("hasScrollY", isEnabled && isScrollNeeded);
		  },
			/* 서브 메뉴 높이를 활성 메뉴에 맞춰 조정 */
		  adjustSubMenuHeight: function(target) {
		    const activeSubList = target.querySelector(".gnb-sub-list.active");
		    const height = activeSubList?.scrollHeight || 0;
		    target.style.minHeight = `${height}px`;
		  },
			/* 배경을 위한 div 생성 */
		  createBackdrop: function() {
		    const backdrop = document.createElement("div");
		    backdrop.classList.add("gnb-backdrop");
		    document.body.appendChild(backdrop);
		    return backdrop;
		  },
		  /* 메뉴 키 이벤트 */ /*오류 수정필요.*/
      mainMenuKeyNav: function(event){

    	  const findDataTrigger = (element) => {
					const ul = element.closest("li").parentNode;
					const sibilngs = [...ul.children];
					return sibilngs.map((li) => li.querySelector("[data-trigger]")).filter(Boolean);
				};

				const findFocusableElement = (element, direction) => {
 	        const sibling = direction === "next" ? "nextElementSibling" : "previousElementSibling";
 	        const parent = element.closest("li")?.[sibling];
 	        return parent ? parent.querySelector("[data-trigger]") : null;
 	      };

				const focusMenuItem = (element) => {
	 	    	if (element) {
	        	element.focus();
	 				}
	 			};

    	  const mainTriggers = findDataTrigger(event.currentTarget);

				const target = event.target;

	 	    	switch (event.key) {
		      	 case "Home":
			         event.preventDefault();
			         focusMenuItem(mainTriggers[0]);
			         break;
		       	case "End":
			         event.preventDefault();
			         focusMenuItem(mainTriggers[mainTriggers.length - 1]);
			         break;
		       	case "ArrowRight":
		       	case "ArrowDown":
			         event.preventDefault();
			         const nextElement = findFocusableElement(target, "next");
			         focusMenuItem(nextElement);
			         break;
		       	case "ArrowLeft":
		       	case "ArrowUp":
			         event.preventDefault();
			         const previousElement = findFocusableElement(target, "prev");
			         focusMenuItem(previousElement);
			         break;
		       	default:
		        	 break;
		     }
      },
      /********************** PC 버전 END **********************/

      /********************** MOBILE 버전 **********************/
      /* MOBILE 버전 초기화 */
      krds_mainMenuMobile_init : function(){
    	    const mobileGnb = document.querySelector(".krds-main-menu-mobile:not(.sample)");

    	    if (!mobileGnb) return;

    	    if (mobileGnb.classList.contains("is-open")) {
    	      this.openMainMenu(mobileGnb);
    	    } else {
    	      mobileGnb.style.display = "none";
    	    }

    	    // 반응형 처리
    	    window.addEventListener("resize", () => {
    	      const isPC = windowSize.getWinSize() === "pc";
    	      if (isPC) this.closeMainMenu();
    	    });
      },
      /* 모바일 전체메뉴 클릭시 오픈 설정 */
      mobileMenuOpen : function(event){
				const id = event.currentTarget.getAttribute("aria-controls");
				const mobileGnb = document.getElementById(id);

    	  if (!mobileGnb.closest(".gnb-wrap")) {
    		  this.openMainMenu();
    		  mobileGnb.querySelector(".gnb-wrap").focus();
 	      }

      },
      /* 모바일에서의 메뉴 활성화를 위한 function */
      openMainMenu: function() {
  	    const mobileGnb = document.querySelector(".krds-main-menu-mobile:not(.sample)");
   	    const navContainer = mobileGnb.querySelector(".gnb-wrap");

   	    mobileGnb.style.display = "block";

   	    // active 메뉴로 스크롤 이동
   	    const activeTrigger = document.querySelector(".gnb-main-trigger.active");
   	    if (activeTrigger) {
   	      const id = activeTrigger.getAttribute("aria-controls");
   	      const top = document.getElementById(id).offsetTop;
   	      const gnbBody = document.querySelector(".gnb-body");
   	      gnbBody.style.scrollBehavior = "auto";
   	      gnbBody.scrollTop = top;
   	    }

   	    setTimeout(() => {
   	      mobileGnb.classList.add("is-backdrop");
   	      mobileGnb.classList.add("is-open");
   	      document.body.classList.add("is-gnb-mobile");
   	    }, 100);

   	    const commponent = this;
   	    // transition 종료후 실행
   	    mobileGnb.addEventListener("transitionend", function onTransitionEnd() {
   	      navContainer.focus();
   	      mobileGnb.removeEventListener("transitionend", onTransitionEnd);

   	      // inert 설정
		      document.querySelector("#krds-header .header-in").setAttribute("inert", "");
		      document.getElementById("contents")?.setAttribute("inert", "");
		      document.getElementById("krds-footer")?.setAttribute("inert", "");

	 	      // 포커스 트랩 설정
	 	      commponent.focusTrap(mobileGnb);
   	    });
   	  },
   	  /* 메뉴 닫기 */
	   	closeMainMenu: function() {
   	    const mobileGnb = document.querySelector(".krds-main-menu-mobile:not(.sample)");
   	    const id = mobileGnb.getAttribute("id");
   	    const openGnb = document.querySelector(`[aria-controls=${id}]`);

	 	    mobileGnb.classList.remove("is-backdrop");
	 	    mobileGnb.classList.remove("is-open");

	 	    // inert 설정
		    document.querySelector("#krds-header .header-in").removeAttribute("inert");
		    document.getElementById("contents")?.removeAttribute("inert");
		    document.getElementById("krds-footer")?.removeAttribute("inert");

	 	    // transition 종료후 실행
	 	    mobileGnb.addEventListener("transitionend", function onTransitionEnd() {
	 	      openGnb.focus();
 		      mobileGnb.removeEventListener("transitionend", onTransitionEnd);
	 	    });

	 	    setTimeout(() => {
	 	      mobileGnb.style.display = "none";
	 	      document.body.classList.remove("is-gnb-mobile");
	 	    }, 400);
	 	  },
			/* 메인메뉴컨트롤 */
			toggleAccordionMenu: function(index){
				this.activeAccordionIndex = this.activeAccordionIndex === index ? null : index;
			},
			/********************** MOBILE 버전 END **********************/

			/********************** 스크롤에 따른 이벤트 설정 **********************/
			initScrollEvent: function(){
		  	window.addEventListener("scroll", () => {
			  	  this.updateScrollValues();
			  	  this.handleScrollDirection();
			  	  this.closeMainMenu();
			  	});
			},
			/* 스크롤 이벤트 적용 */
		  updateScrollValues: function() {
		    this._scrollY = window.scrollY;
		    this._scrollH = document.body.scrollHeight;
		  },
		  handleScrollDirection: function() {
		    const $wrap = document.querySelector("#saleson");
		    if ($wrap) {
		      const _conOffsetTop = document.querySelector("#contents").offsetTop;
		      const _scrollY = window.scrollY;
		      const _scrollDown = _scrollY > this._lastScrollY;
		      const _scrollUp = _scrollY < this._lastScrollY;

		      if (_scrollY > _conOffsetTop + 50 && _scrollDown) {
		        $wrap.classList.add("scroll-down");
		        $wrap.classList.remove("scroll-up");
		      } else if (_scrollY > _conOffsetTop + 50 && _scrollUp) {
		        $wrap.classList.add("scroll-up");
		        $wrap.classList.remove("scroll-down");
		      } else {
		        $wrap.classList.remove("scroll-down", "scroll-up");
		      }

		      this._lastScrollY = _scrollY;
		    }
		  },

	  },
	  mounted: function(){

      this.isLogin = $s.isLogin();
      this.isGuestLogin = $s.isGuestLogin();

		  // 초기 메뉴 초기화.
	  	this.krds_mainMenuPC_init()

	  	//모바일 전체메뉴 초기화.
	  	this.krds_mainMenuMobile_init();

		  //스크롤에 대한 이벤트.
			this.initScrollEvent();


	  },

};
</script>

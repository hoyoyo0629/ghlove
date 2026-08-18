<template>
	<!-- 헤더 영역 -->
	<header id="krds-header">

      <!-- 본문 바로가기 영역  -->
      <div id="krds-skip-link">
        <a href="#container">본문 바로가기</a>
      </div>
      <!-- //본문 바로가기 영역  -->

<!-- 	    <total-search @query="goToSearch"></total-search> -->
<!-- 	    <sitemap-modal></sitemap-modal> -->

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

      <!-- 헤더 컨텐츠 영역  -->
      <div class="header-in">
        <!-- 헤더 상단 기타메뉴 -->
      	<div class="header-container">
           <div class="inner">
             <div class="header-branding">
               <h1 class="logo sample">
                 <a href="/" rel="nosublink nofollow">
                   <span class="sr-only">고향사랑e음</span>
                 </a>
               </h1>
               <span style="margin-left: 10px;" v-if="isDevelopment">개발테스트</span>
               <div class="header-actions">
								<button type="button" class="btn-navi navi-row logout" v-if="!showLogin" @click.prevent="logout">로그아웃</button>
                <button type="button" class="btn-navi navi-row login" v-else-if="showLogin" @click.prevent="goLogin">로그인</button>

			  				<button type="button" class="btn-navi navi-row my drop-btn" v-if="!showLogin" @click="getMyPage()">마이페이지</button>
                <button type="button" class="btn-navi navi-row join" v-else-if="showLogin" @click="goToJoinPageBefore()">회원가입</button>
							  <button type="button" class="btn-navi navi-row basket" @click="openPresent('sbag')">장바구니</button>
                <button type="button" class="btn-navi navi-row confirm" @click="getMyReceipt()">기부확인증</button>
                <button type="button" class="btn-navi navi-row sch open-modal" title="통합검색 레이어" data-target="popTotalSchType1">통합검색</button>
                <button type="button" class="btn-navi navi-row all" aria-controls="mobile-nav">전체메뉴</button>
               </div>
            </div>
          </div>
        </div>
        <!-- //헤더 상단 기타메뉴 -->

          <!-- 메인메뉴 : 데스크탑 -->
          <nav class="krds-main-menu">
            <div class="inner">
              <!-- [250820] gnb 수정 : 기부 메뉴 수정, 안내사항 추가, 아이콘 삭제 -->
              <ul class="gnb-menu">
	              <li v-for="(item, index) in menuItems" :key="index">
								  <button
								  	class="gnb-main-trigger"
								  	:class="{ active : activeMainIndex === index }"
								  	@click="toggleMainMenu(index)"
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
											  <ul class="type-description">
													<li v-for="(subItem, subIndex) in item.subMenu" :key="subIndex">
													  <strong class="tit">
														<!-- 새 창 열림 일 경우
														<a href="#" title="새 창 열림">
														  자치단체에기부하기
														  <i class="svg-icon ico-go"></i>
														</a>
														-->
														<a @click=handleLocation(subItem)>{{ subItem.value }}</a>
													  </strong>
													  <p class="txt">{{ subItem.content }}</p>
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
                <button type="button" class="btn-navi navi-row basket">장바구니</button>
                <button type="button" class="btn-navi navi-row confirm">기부인증</button>
              </div>
            </div>
            <!-- //gnb-header -->

            <!-- gnb-body -->
            <div class="gnb-body">
              <div class="gnb-bod__inner">
                <!-- 총 기부금 -->
                <div class="donation">
                  <div class="donation__header">
                    <h3 class="donation__title">총 기부금</h3>
                    <span class="donation__date">2025.6.14. (전일기준)</span>
                  </div>
                  <div class="donation__amount">
                    <span class="amount__number">40,024,199,340원</span>
                    <span class="amount__days">D-200</span>
                  </div>
                  <div class="donation__progress">
                    <div class="progress__bar">
                      <div class="progress__fill" style="width: 40%">
                        <div class="progress__mark"></div>
                      </div>
                    </div>
                    <div class="progress__target">
                      <strong>40%</strong>
                      <span>목표금액 : 1,000억</span>
                    </div>
                  </div>
                </div>
                <!-- //총 기부금 -->

                <!-- 로그인 전 -->
                <div class="login-section" style="display: none">
                  <div class="login__message">
                    여러분의 작은 기부가
                    <br />
                    고향에 큰 힘이
                    <br />
                    됩니다.
                  </div>
                  <button class="login__btn">로그인</button>
                  <div class="login__links">
                    <a href="#" class="login__link">아이디 찾기</a>
                    <a href="#" class="login__link">비밀번호 찾기</a>
                    <a href="#" class="login__link">회원가입</a>
                  </div>
                </div>
                <!-- //로그인 전 -->

                <!-- 로그인 후 -->
                <div class="login-section">
                  <div class="login__top">
                    <div class="login__mark">
                      <i class="ico-gift-mark"></i>
                      <span>기부혜택증</span>
                    </div>
                    <div class="login__user">
                      <p class="login__name">홍길동 님</p>
                      <p class="login__txt">환영합니다.</p>
                    </div>
                  </div>
                  <div class="login__donation">
                    <ul class="login__items">
                      <li class="login__item">
                        <span class="login__tit">올해 기부액</span>
                        <span class="login__num">100,000원</span>
                      </li>
                      <li class="login__item">
                        <span class="login__tit">기부총액</span>
                        <span class="login__num">1,000,000원</span>
                      </li>
                    </ul>
                  </div>
                  <div class="login__point">
                    <ul class="login__items">
                      <li class="login__item">
                        <span class="login__tit">보유 포인트</span>
                        <span class="login__num">30,000P</span>
                      </li>
                    </ul>
                  </div>
                  <button class="login__btn--out">
                    로그아웃
                    <i class="svg-icon ico-logout"></i>
                  </button>
                </div>
                <!-- //로그인 후 -->
              </div>

              <!-- gnb-menu -->
              <div class="krds-accordion">
                <div class="accordion-item">
                  <div class="accordion-header"><button type="button" class="btn-accordion">기부</button></div>
                  <div class="accordion-collapse">
                    <div class="accordion-body">
                      <ul class="gnb-mob">
                        <li><a href="#" class="gnb-mob-menu">자치단체에 기부하기</a></li>
                        <li><a href="#" class="gnb-mob-menu">특정사업에 기부하기</a></li>
                        <li><a href="#" class="gnb-mob-menu">기금사업 소개</a></li>
                        <li><a href="#" class="gnb-mob-menu">고향사랑기부제 안내</a></li>
                        <li><a href="#" class="gnb-mob-menu">온라인 기부절차안내</a></li>
                        <li><a href="#" class="gnb-mob-menu">오프라인 기부절차안내</a></li>
                        <li><a href="#" class="gnb-mob-menu">기부금 연말정산 세액공제 안내</a></li>
                        <li><a href="#" class="gnb-mob-menu">고향사랑e음 위반 주의사항</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
                <div class="accordion-item">
                  <div class="accordion-header"><button type="button" class="btn-accordion">답례품</button></div>
                  <div class="accordion-collapse">
                    <div class="accordion-body">
                      <ul class="gnb-mob">
                        <li><a href="#" class="gnb-mob-menu">답례품몰</a></li>
                        <li><a href="#" class="gnb-mob-menu">제철식품관</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
                <div class="accordion-item">
                  <div class="accordion-header"><button type="button" class="btn-accordion">이벤트</button></div>
                  <div class="accordion-collapse">
                    <div class="accordion-body">
                      <ul class="gnb-mob">
                        <li><a href="#" class="gnb-mob-menu">진행중인 이벤트</a></li>
                        <li><a href="#" class="gnb-mob-menu">종료된 이벤트</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
                <div class="accordion-item">
                  <div class="accordion-header"><button type="button" class="btn-accordion">고객센터</button></div>
                  <div class="accordion-collapse">
                    <div class="accordion-body">
                      <ul class="gnb-mob">
                        <li><a href="#" class="gnb-mob-menu">공지사항</a></li>
                        <li><a href="#" class="gnb-mob-menu">자료실</a></li>
                        <li><a href="#" class="gnb-mob-menu">Q&A</a></li>
                        <li><a href="#" class="gnb-mob-menu">FAQ</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
                <div class="accordion-item">
                  <div class="accordion-header"><button type="button" class="btn-accordion">마이페이지</button></div>
                  <div class="accordion-collapse">
                    <div class="accordion-body">
                      <ul class="gnb-mob">
                        <li><a href="#" class="gnb-mob-menu">기부내역 조회</a></li>
                        <li><a href="#" class="gnb-mob-menu">장바구니</a></li>
                        <li><a href="#" class="gnb-mob-menu">1:1 문의</a></li>
                        <li><a href="#" class="gnb-mob-menu">기부혜택증 보기</a></li>
                        <li><a href="#" class="gnb-mob-menu">관심정보</a></li>
                        <li><a href="#" class="gnb-mob-menu">기부포인트 조회</a></li>
                        <li><a href="#" class="gnb-mob-menu">취소반품교환</a></li>
                        <li><a href="#" class="gnb-mob-menu">답례품 Q&A</a></li>
                        <li><a href="#" class="gnb-mob-menu">기부확인증 보기</a></li>
                        <li><a href="#" class="gnb-mob-menu">회원탈퇴</a></li>
                        <li><a href="#" class="gnb-mob-menu">주문조회</a></li>
                        <li><a href="#" class="gnb-mob-menu">배송지 관리</a></li>
                        <li><a href="#" class="gnb-mob-menu">답례품 후기</a></li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
              <!-- //gnb-menu -->
            </div>
            <!-- //gnb-body -->

            <!-- gnb-close -->
            <button type="button" class="krds-btn medium icon" id="close-nav">
              <span class="sr-only">전체메뉴 닫기</span>
              <i class="svg-icon ico-popup-close"></i>
            </button>
            <!-- //gnb-close -->
          </div>
        </nav>

        <!-- //메인메뉴 : 모바일 -->
      </header>
      <!-- //헤더 영역 -->
</template>

<script type="text/javascript" src="/static/js/netfunnel.js"></script>
<script type="text/javascript" src="/static/js/netfunnel_skin.js"></script>

<script>

module.exports = {
		props:{},
		components:{
		    "total-search": httpVueLoader("/components/layouts/totalSearch.vue"),
		    "sitemap-modal": httpVueLoader("/components/layouts/sitemapModal.vue"),
		},
		data: function(){
			return{
		      isDevelopment: SERVER_POSITION == "development",
		      activeMainIndex:null,
		      backdrop: null,
		      menuItems : [{
		    	  mainMenu : "기부"
		    		  ,subMenu :
		    		  	[{
		    		  		value : "자치단체에 기부하기"
		    		  		,content : "내 고향에 직접 기부 할 수 있어요"
			    		  	,url : ""
			    		  	,func : function(){$s.donation.goDonationPage()}
		    		  	},{
		    		  		value : "특정사업에 기부하기"
		    		  		,content : "내 고향의 특정사업에 기부 할 수 있어요"
			    		  	,url : "/designated-donation/index.html"
			    		  	,func : ""
		    		  	}]
		    		  },{
	    		  mainMenu : "답례품"
		    		  ,subMenu :
		    		  	[{
		    		  		value : "답례품몰"
		    		  		,content : "지역의 답례품을 확인 할 수 있어요"
		    		  		,url : "/goods/index.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "제철식품관"
		    		  		,content : "지역에서 제공하는 제철 식품들을 확인 할 수 있어요"
		    		  		,url : "/event/seasonList.html"
			    		  	,func : ""
		    		  	}]
		    		  },{
	    		  mainMenu : "안내사항"
		    		  ,subMenu :
		    		  	[{
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
		    		  		value : "온라인 기부절차안내"
		    		  		,content : "온라인 기부방법에 대해 확인할 수 있어요"
		    		  		,url : "/donation/guide2.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "오프라인 기부절차안내"
		    		  		,content : "오프라인 기부방법에 대해 확인할 수 있어요"
		    		  		,url : "/donation/guide5.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "기부금 연말정산 세액공제 안내"
		    		  		,content : "내가 기부한 기부금이 연말정산 때 어떻게 세액공제 되는지 확인 할 수 있어요"
		    		  		,url : "/donation/guide3.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "고향사랑e음 위반 주의사항"
		    		  		,content : "고향사랑기부를 하기 전에 주의해야 할 사항을 확인할 수 있어요"
		    		  		,url : "/donation/guide6.html"
			    		  	,func : ""
		    		  	}]
		    		  },{
	    		  mainMenu : "이벤트"
		    		  ,subMenu :
		    		  	[{
		    		  		value : "진행중인 이벤트"
		    		  		,content : "진행중인 이벤트를 확인할 수 있어요"
		    		  		,url : ""
			    		  	,func : ""
		    		  	},{
		    		  		value : "종료된 이벤트"
		    		  		,content : "종료된 이벤트를 확인할 수 있어요"
		    		  		,url : ""
			    		  	,func : ""
		    		  	}]
		    		  },{
	    		  mainMenu : "고객센터"
		    		  ,subMenu :
		    		  	[{
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
		    		  	}]
		    		  },{
	    		  mainMenu : "마이페이지"
		    		  ,subMenu :
		    		  	[{
		    		  		value : "기부내역 조회"
		    		  		,content : "내가 기부한 내역을 확인할 수 있어요"
		    		  		,url : "/mypage/cntrList.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "장바구니"
		    		  		,content : "내가 담은 답례품을 확인할 수 있어요"
		    		  		,url : "/cart/index.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "1:1 문의"
		    		  		,content : "고향사랑e음 이용 중 궁금한 사항에 대한 상담을 받을 수 있어요"
		    		  		,url : "/mypage/inquiry.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "기부혜택증 보기"
		    		  		,content : "내가 기부한 고향에서 제공하는 혜택을 확인할 수 있어요"
		    		  		,url : "/mypage/honorList.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "관심정보"
		    		  		,content : "내가 관심있는 지역과 답례품을 확인할 수 있어요"
		    		  		,url : "/mypage/intrstLocGov.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "기부포인트 조회"
		    		  		,content : "나의 기부포인트를 확인할 수 있어요"
		    		  		,url : "/mypage/cntrPoint.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "취소반품교환"
		    		  		,content : "나의 취소반품교환내역을 확인할 수 있어요"
		    		  		,url : "/mypage/orderCancel.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "답례품 Q&A"
		    		  		,content : "답례품에 대해 궁금한 사항에 대한 상담을 받을 수 있어요"
		    		  		,url : "/mypage/inquiryItem.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "기부확인증 보기"
		    		  		,content : "내가 기부한 내역을 확인할 수 있어요"
		    		  		,url : "/mypage/receiptList.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "회원탈퇴"
		    		  		,content : "회원탈퇴를 할 수 있어요"
		    		  		,url : ""
			    		  	,func : ""
		    		  	},{
		    		  		value : "주문조회"
		    		  		,content : "나의 주문내역을 조회할 수 있어요"
		    		  		,url : ""
			    		  	,func : function(){getOrderList()}
		    		  	},{
		    		  		value : "배송지 관리"
		    		  		,content : "나의 배송지를 관리할 수 있어요"
		    		  		,url : "/mypage/deliveryInfo.html"
			    		  	,func : ""
		    		  	},{
		    		  		value : "답례품 후기"
		    		  		,content : "내가 남긴 답례품 후기를 확인할 수 있어요"
		    		  		,url : "/mypage/review.html"
			    		  	,func : ""
		    		  	}]
	    		  }]
			}


		},
	  computed: {
	    showLogin: function () {
	      return !(this.isLogin || this.isGuestLogin);
	    },
	  },
	  methods:{
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
 	    openPresent: function (type) {
 	       if (type == "sbag") {
 	         $s.redirect("/cart/index.html");
 	         // $s.redirect("/_pub/cart/UI_P07150000.html");
 	       } else {
 	         $s.redirect("/goods/index.html");
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
			/* 메인메뉴컨트롤 */
			toggleMainMenu: function(index){
				this.activeMainIndex = this.activeMainIndex === index ? null : index;

				//document 컨트롤영역
				const isOpen = this.activeMainIndex !== null;
				document.querySelector(".gnb-backdrop").classList.toggle("active", isOpen);
			  document.body.classList.toggle("is-gnb-web", isOpen);

		    const isScrollNeeded = document.body.scrollHeight > window.innerHeight;
		    document.body.classList.toggle("hasScrollY", isOpen && isScrollNeeded);
			},
			/* 배경을 위한 div 생성 */
		  createBackdrop: function() {
		    const backdrop = document.createElement("div");
		    backdrop.classList.add("gnb-backdrop");
		    document.body.appendChild(backdrop);
		    return backdrop;
		  },
		  /* 메뉴 클릭시 백그라운드 색상 변경 */
		  toggleBackdrop: function(isOpen) {
			    this.backdrop.classList.toggle("active", isOpen);
			    document.body.classList.toggle("is-gnb-web", isOpen);
		  },
		  createBackdrop: function() {
			    const backdrop = document.createElement("div");
			    backdrop.classList.add("gnb-backdrop");
			    document.body.appendChild(backdrop);
			    return backdrop;
		  },
		  /* 메뉴 페이지 이동 이벤트 */
		  handleLocation: function(menuItem){
			  if(menuItem.url){
				  window.location.href = menuItem.url;
			  }else if (typeof menuItem.func === 'function'){
				  console.log(menuItem);
				  menuItem.func();
			  }else{;
					$s.alert("서비스 오픈예정 입니다.");
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

	  },
	  mounted: function(){
		  /* 헤더가 마운트되었을 때 백그라운드 조정을 위한 태그 셋팅 */
		  document.querySelector(".gnb-backdrop") || this.createBackdrop();

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

	  }
};
</script>

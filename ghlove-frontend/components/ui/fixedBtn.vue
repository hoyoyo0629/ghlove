<template>
	<div class="float-area" id="fixedBtn">
		<!-- 소식지 -->
	  <div class="float__item float__item--toggle" style="display:none;">
	    <button type="button" class="float__btn float__btn--news" @click="newsletter">
	      <i class="ico_news"></i>
	      <span class="sr-only">소식지</span>
	    </button>
	  </div>
	  <!-- 카탈로그 -->
	  <!--<div class="float__item float__item--toggle">
	    <button type="button" class="float__btn" @click="catalogConfirm">
	      <i class="ico_catalog"></i>
	      <span class="sr-only">답례품 카탈로그</span>
	    </button>
	  </div>-->
		<!-- 카카오채널 -->
	  <div class="float__item float__item--toggle">
	    <button type="button" class="float__btn" title="새 창 열림" @click="kakao">
	      <i class="ico_ch"></i>
	      <span class="sr-only">카카오채널 추가</span>
	    </button>
	  </div>
	  <!-- 챗봇상담 -->
	  <div class="float__item float__item--toggle">
	    <button type="button" class="float__btn" title="새 창 열림" @click="chatbot">
	      <i class="ico_chatbot"></i>
	      <span class="sr-only">챗봇상담</span>
	    </button>
	  </div>
	  <!--  TOP 이동 -->
	  <div class="float__item">
	    <button type="button" class="float__btn" @click="goTop">
	      <i class="ico_top"></i>
	      <span class="sr-only">TOP</span>
	    </button>
	  </div>

	  <!--  접기/열기 -->
	  <div class="float__item">
	    <button type="button"
	    	class="float__btn-toggle float_btn"
	    	:aria-expanded="isExpanded.toString()"
	    	@click="toggle"
	    >
	      <i class="ico_toggle"></i>
	      <span class="sr-only">{{ isExpanded ? '닫기':'열기' }}</span>
	    </button>
	  </div>
	</div>
</template>

<script>
module.exports = {
	data() {
		return {
			isExpanded: true,
		}
	},
  methods: {
    chatbot() {
      var url = "http://www.chatbot.go.kr/chatbotPop.ndo?bnrId=2Qe7TBzEHOthcAi";
      var popTarget = "bnrPopup";
      var wnd = window.open("", popTarget, "width=600, height=800");
      var referLink = document.createElement("a");
      referLink.href = url;
      referLink.target = popTarget;
      document.body.appendChild(referLink);
      referLink.click();
    },
		kakao() {
      window.open('http://pf.kakao.com/_cxdtlG','_blank')
    },
    newsletter() {
      $s.redirect("/newsletter/main.html");
    },
    catalogConfirm() {
		if(confirm('답례품 카탈로그의 용량은 13 메가바이트 정도입니다.\n가입하고 계신 통신 요금제나 다운로드 환경에 따라 요금이 발생할 수 있사오니 참고 부탁드립니다.')) {
			window.open('https://drive.google.com/uc?export=download&id=1zqQmb0s3hi7xH6CN_HYHwiXzMHgWaPqn');
		}
	},
    goTop(){
    	$s.donation.focusHeader();
    	window.scrollTo({ top: 0, behavior: 'smooth', });
		const body = document.body;
		body.setAttribute("tabindex","-1");
		body.focus();
		body.removeAttribute("tabindex");
    },
    toggle(){
    	const toggleButton = document.querySelector(".float__btn-toggle");
      const toggleTargets = document.querySelectorAll(".float__item--toggle");


    	toggleButton.setAttribute("aria-expanded", this.isExpanded ? "true" : "false");
        toggleTargets.forEach((el) => {
          el.hidden = this.isExpanded;
        });
    	this.isExpanded = !this.isExpanded;
    },
  },
  mounted: function () {
    this.$nextTick(function () {
      //Saleson.init();
    });
  },
};
</script>

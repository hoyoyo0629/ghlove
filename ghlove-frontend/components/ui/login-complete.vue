<template>
	<div>
	  <!-- 로그인 후 -->
	  <div class="login-section">
	    <div class="login__top">
	      <a href="/mypage/honorList.html" class="login__mark">
	        <i class="ico-gift-mark"></i>
	        <span>기부혜택증</span>
	      </a>
	      <div class="login__user">
	        <p class="login__name">OOO 님</p>
	        <p class="login__txt">환영합니다.</p>
	      </div>
	    </div>
	    <div class="login__donation">
	      <ul class="login__items">
	        <li class="login__item">
	          <span class="login__tit">올해 기부액</span>
	          <span class="login__num" id="thisYearAmt">0원</span>
	        </li>
	        <li class="login__item">
	          <span class="login__tit">기부총액</span>
	          <span class="login__num" id="totalAmt">0원</span>
	        </li>
	      </ul>
	    </div>
	    <div class="login__point">
	      <ul class="login__items">
	        <li class="login__item">
	          <span class="login__tit">보유 포인트</span>
	          <span class="login__num">0P</span>
	        </li>
	      </ul>
	    </div>
	    <button type="button" class="login__btn--out" @click="logout()">
	      로그아웃
	      <i class="svg-icon ico-logout"></i>
	    </button>
	  </div>
	  <!-- //로그인 후 -->
	</div>
</template>

<script>
	module.exports = {
		data() {
			return {
				// 로그인 유저 정보
				userInfoData: {
					userName: "",
					userTotalDonationAmt : 0,
					userDonationPoint: 0,
					userThisYearDonationAmt: 0,
				},
			}
		},
		methods: {
			// 로그인 버튼 클릭 시 로그인 페이지 이동
			moveLoginPage() {
				location.href="/users/login.html"
			},

			// 로그인 유저 정보 초기화 & 바인딩 함수
			userInfoInit() {
				const self = this;
				$s.api.userInfo(function (response){
					// 유저 정보 초기화
					self.userInfoData = response;
					// 유저 정보 바인딩
					$('.login-section').find('.login__user .login__name').html(`${self.userInfoData.userName} 님`);
					$('.login-section').find('#thisYearAmt').html(`${self.userInfoData.userThisYearDonationAmt.toLocaleString()}원`);
					$('.login-section').find('#totalAmt').html(`${self.userInfoData.userTotalDonationAmt.toLocaleString()}원`);
					$('.login-section').find('.login__point .login__num').html(`${self.userInfoData.userDonationPoint.toLocaleString()}P`);
				});
			},

			// 로그아웃
			logout() {
     		$s.logout();
	    },
		},
		mounted() {
			this.$nextTick(function () {
				this.userInfoInit();
			});
		}
	};
</script>
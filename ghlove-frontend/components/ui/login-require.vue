<template>
	<!-- 로그인 전 -->
  <div class="login-section">
    <div class="login__message">
      여러분의 소중한 기부가
      <br />
      고향에 큰 힘이 됩니다.
    </div>
    <button type="button" class="login__btn" @click="moveLoginPage()">로그인</button>
    <div class="login__links">
    	<!-- TODO :: (김대원) 우선은 같은 페이지로 이동하는데, 아이디 찾기 or 비밀번호 찾기에 따라 탭 버튼 눌리게 변경 가능한지 확인 -->
      <a href="/users/find-idpw.html" class="login__link">아이디 찾기 &middot; 비밀번호 찾기</a>
			<!-- <i class="login__bar"></i>[250828] 추가 -->
      <!-- <a href="/users/find-idpw.html" class="login__link">비밀번호 찾기</a> -->
			<i class="login__bar"></i><!-- [250828] 추가 -->
      <a href="/users/join.html" class="login__link">회원가입</a>
    </div>
  </div>
  <!-- //로그인 전 -->
</template>

<script>
	module.exports = {
		methods: {
			// 로그인 버튼 클릭 시 로그인 페이지 이동
			moveLoginPage: function() {
				location.href="/users/login.html"
			},
		},
	};
</script>
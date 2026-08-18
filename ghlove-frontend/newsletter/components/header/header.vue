<template>
	<header class="hd" id="header">
		<ul id="go_main">
			<li><a href="#con01">기부제 소식 목록 바로가기</a></li> 
			<li><a href="#con02">고향소식 목록 바로가기</a></li>
			<li><a href="#con03">우수사례 목록 바로가기</a></li>
		</ul>
		<div class="hd_inner">
			<div class="h1_wrap">
				<h1 class="logo">
					<div class="wrap">
						<a href="javascript:void(0);" @click="goToNewsletterMain">
							<img src="/newsletter/static/images/common/header_logo_v2.png" alt="고향사랑 기부제 소식 로고">
						</a>
						<!-- <select name="ver" title="소식지 버전 선택" onchange="window.open(value,'_self');">
							<option value="/newsletter/vol001/main.html" title="24년도 1버전">VOL 1</option>
							<option value="/newsletter/vol002/main.html" title="24년도 1버전">VOL 2</option>
						</select> -->
						<div class="select-wrap dropdown-toggle" data-toggle="dropdown">
							<select name="version" :value="selectedVer" @change="changeVer($event)" class="dropdown-toggle-arr">
								<option :title="version.title" :value="version.ver" v-for="version in newletterList">{{version.label}}</option>
							</select>
						</div>
					</div>
				</h1>
			</div>
			<button type="button" class="btn-paging" onclick="location.href='/main.html'"><span>전체 페이지 보기</span></button>
			
			<div class="right_logo">
				<img src="/newsletter/static/images/common/header_logo2.png" class="right_logo_img" alt="고향사랑 e음 로고">
				<a href="/" class="btn_done">
					<img src="/newsletter/static/images/common/dona_btn.png" class="" alt="```1기부하기 버튼">
				</a>
			</div>
			
		</div>
	</header>
</template>
<script>
	module.exports = {
		props: {
			ver: {
				type: String,
				default: function () {
					return "";
				},
			},
		},
		data: function () {
			return {
				newletterList: [
					{ title: '소식지 24년도 2버전', label: 'VOL.2', ver: 'vol002'}
					, { title: '소식지 24년도 1버전', label: 'VOL.1', ver: 'vol001'}
				],
				selectedVer: "",
			};
		},
		methods: {
			changeVer: function (e) {
				location.href = '/newsletter/' + e.target.value + '/main.html';
			},
			goToNewsletterMain: function () {
				location.href = '/newsletter/' + this.ver + '/main.html';
			},
		},
		mounted: function () {
		    this.$nextTick(function () {
				let header = this;
				header.selectedVer = header.ver;
				window.onpageshow = function (event) {			// 뒤로가기로 화면 진입시 버전 다시 세팅
					if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
						header.selectedVer = "";
						header.selectedVer = header.ver;
					}
				};
		    });
		},
	};

	$(document).ready(function(){
		$(".h1_wrap select").click(function() {
			$(this).parent().toggleClass("show");
		});
		
		$(".h1_wrap select").keydown(function(e) {			// 엔터나 스페이스바 키 눌렀을 경우
			if (e.keyCode == 13 || e.keyCode == 32) {
				$(this).parent().toggleClass("show");
			}
		});
		
		$(".h1_wrap select").blur(function(e) {				// 포커스 해제되었을 경우
			$(this).parent().removeClass("show");
		});
	});
	
</script>
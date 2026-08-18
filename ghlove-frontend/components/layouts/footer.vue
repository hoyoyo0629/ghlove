<template>
	<div class="footer_wrap footer2">
		<footer id="footer">
			<div class="footer_link">
				<div class="container">
					<div class="no-gutters">
						<ul>
							<li v-if="isLogin" @click.prevent="logout">
								<a href="javascript:;">로그아웃</a>
							</li>
							<li v-else-if="!isLogin">
								<a href="/users/login.html">로그인</a>
							</li>
							<li><a href="/company/about.html">회사소개</a></li>
							<li><a href="/notice/list.html">고객센터</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="container">
				<div class="footer_content">
					<div class="row">
						<div class="col-12 col-md-3 col-lg-4 footer_center">
							<h5>고객센터</h5>
							<span>평일 10시 ~ 18시 <em class="word">/ 토요일 10:00 ~ 18:00<br>(점심시간 12시 ~
									13시)</em></span>
							<p class="number">{{ about.telNumber }}</p>
						</div>
						<div class="col-12 col-md-3 col-lg-4 footer_bank">
							<h5>온라인입금 계좌</h5>
							<ul v-for="(account, index) in accountNumbers" v-if="index === 0">
								<li class="word">{{ account.bankName }}</li>
								<li class="word">{{ account.accountNumber }}</li>
								<li class="word">{{ account.accountHolder }}</li>
							</ul>
						</div>
						<div class="col-12 col-md-6 col-lg-4 footer_email">
							<h5>입점문의</h5>
							<span>이메일 문의는 help@onlinepowes.com로 보내주세요.</span>
							<button type="button" class="btn btn_sm btn_primary"><a
									href="/store/apply.html">입점 문의
									신청하기</a></button>
						</div>
						<div class="col-12 col-md-6 col-lg-12 footer_etc">
							<ul>
								<li><a href="#" data-toggle="modal"
										data-target=".pop_terms_use">이용약관</a></li>
								<li><a href="/policy/privacy.html">개인정보취급방침</a></li>
								<li><a href="/store/apply.html">입점문의</a></li>
							</ul>
							<div class="footer_company">
								<p>{{ about.companyName }}</p>
								<p>{{ about.bossName }}</p>
								<p>{{ about.address }} {{ about.addressDetail }}</p>
								<p>{{ about.telNumber }}</p>
								<p>사업자등록번호 {{ about.companyNumber }}</p>
								<p>통신판매업신고 {{ about.mailOrderNumber }}</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</footer>
		<div class="float_menu_m mobile">
			<ul class="row no-gutters">
				<li class="col all-menu">
					<a href="javascript:;" class="menu"><img src="/static/images/icon/icon_menu.png"
							alt="메뉴"></a>
				</li>
				<li class="col">
					<a href="/mypage/recent-view.html">

						<img src="/static/images/icon/icon_recent.png" alt="최근본 상품"
							v-if="firestlatelyItem == null">
						<!-- 최근본상품이미지 노출 -->
						<img class="thumb" :src="itemImage(firestlatelyItem.itemImage)" alt="최근본 상품"
							@error="errorImage" v-else>
					</a>
				</li>
				<li class="col">
					<a href="/"><img src="/static/images/icon/icon_home.png" alt="메인"></a>
				</li>
				<li class="col">
					<a href="/mypage/index.html"><img src="/static/images/icon/icon_mypage.png"
							alt="마이페이지"></a>
				</li>
			</ul>
			<button type="button" class="btn_top"><img src="/static/images/icon/btn_top.png" alt="상단으로"></button>
		</div>
		<div class="float_menu_pc tablet">
			<div class="menu_list">
				<button type="button" class="btn_float_link">
					<span class="screen_out">화살표</span>
				</button>
				<ul>
					<li>
						<a href="/cart/index.html">
							<span class="txt">장바구니</span>
							<span class="num">{{ displayCartQuantites }}</span>
						</a>
					</li>
					<li>
						<a href="/mypage/wishlist.html">
							<span class="txt">관심상품</span>
							<span class="num">{{ wishlistCount }}</span>
						</a>
					</li>
					<li>
						<a href="/mypage/recent-view.html">
							<span class="txt">최근 본 상품</span>
							<span class="num">{{ latelyItemCount }}</span>
						</a>
					</li>
				</ul>
				<button type="button" class="btn_top">TOP</button>
			</div>
		</div>
		<div class="footer_category">
			<ul class="info">
				<li v-if="isLogin" @click.prevent="logout">
					<a href="javascript:;">로그아웃</a>
				</li>
				<li v-else-if="!isLogin">
					<a href="/users/login.html">로그인</a>
				</li>
				<li v-if="!isLogin">
					<a href="/users/join.html">회원가입</a>
				</li>
				<li class="mypage">
					<a href="/mypage/index.html">마이페이지</a>
				</li>
				<!-- ======= 드루와 마찬가지로 로그인이 안 됐을 땐 회원가입, 로그인 됐을 땐 회원가입 자리에 마이페이지가 나와야 합니다. ======= -->
			</ul>
			<div class="hr"></div>

			<div class="accordion mobile_menu">
				<div class="accordion_wrap group" v-for="group in category.groups" :key="group.url">
					<a href="javascript:void(0)" class="group_tit">{{ group.name }}</a>
					<ul class="depth1">
						<li v-for="(category1, category1Index) in group.categories" :key="category1.url"
							:class="category1.childCategories.length > 0 ? 'off' : ''">
							<template v-if="category1.childCategories.length > 0">
								<a href="javascript:void(0)" class="depth1_tit">{{ category1.name }}</a>
								<ul class="depth2">
									<li><a href="javascript:void(0)"
											@click="link(category1.url)">All</a></li>
									<li v-for="category2 in category1.childCategories"
										:key="category2.categoryId">
										<a href="javascript:void(0);"
											@click="link(category2.url)">{{ category2.name }}</a>
									</li>
								</ul>
							</template>
							<template v-else>
								<a href="javascript:void(0)" class="depth1_tit"
									@click="link(category1.url)">
									{{ category1.name }}
								</a>
							</template>
						</li>
					</ul>
				</div>
			</div>
			<div class="footer_content">
				<div class="today_list" v-if="latelyItemList != null && latelyItemList.length > 0">
					<h5>최근 본 상품</h5>
					<ul>
						<li v-for="data in latelyItemList" :key="data.itemId">
							<a :href="'/items/details.html?code=' + data.itemUserCode">
								<img :src="itemImage(data.itemImage)" :alt="data.itemName"
									@error="errorImage">
							</a>
						</li>
					</ul>
				</div>
				<div class="service">
					<h5>서비스</h5>
					<!--					<p class="m_lookbook">
						<a href="/event/stylebook.html">룩북</a>
					</p>-->
					<ul>
						<li><a href="/event/new.html">신상품</a></li>
						<li><a href="/event/best.html">베스트</a></li>
						<li><a href="/featured/list.html">기획전</a></li>
						<li><a href="/event/spot.html">타임세일</a></li>
						<li><a href="/event/stylebook.html">룩북</a></li>
						<li><a href="/notice/list.html">고객센터</a></li>
					</ul>
				</div>
			</div>
			<a href="javascript:;" class="layer_close screen_out">닫기</a>
		</div>

		<!-- 이용약관 팝업 -->
		<div class="modal fade modal_full pop_terms_use" v-if="terms != null">
			<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
				<div class="modal-content">
					<div class="modal-header">
						<h3 class="modal_tit">{{ terms.title }}</h3>
					</div>
					<div class="modal-body">
						<div class="terms_wrap" v-if="terms != null" v-html="terms.content"></div>
					</div>
					<button type="button" class="modal_close" data-dismiss="modal">
						<span class="screen_out">이용약관 닫기</span>
					</button>
				</div>
			</div>
		</div>

		<alert></alert>
		<toast></toast>
	</div>
</template>

<script>
module.exports = {
	props: {
		newCartQuantity: 0,
		newLatelyItemCount: 0,
		newWishlistCount: 0
	},
	data: function () {
		return {
			terms: {},
			about: {},
			accountNumbers: {},

			isLogin: false,
			isGuestLogin: false,
			terminal: {
				equipmentCode: ""
			},
			category: {
				groups: []
			},
			quick: {
				"cartQuantity": 0,
				"wishlistCount": 0
			},
			latelyItems: [],
			latelyItemList: []
		};
	},
	components: {
		'alert': httpVueLoader('/components/layouts/alert.vue'),
		'toast': httpVueLoader('/components/layouts/toast.vue'),
	},
	methods: {
		logout: function () {
			$s.logout();
		},
		getQuickInfo: function () {
			var self = this;
			$s.api.getQuickInfo(function (data) {
				self.quick = data;
			}, function () {
				//$s.error('getBestSearchWord error');
			});
		},
		link: function (url, childCategories) {

			var flag = typeof childCategories != 'undefined'
				&& childCategories != null
				&& childCategories.length > 0;

			if (!flag) {
				$s.redirect('/category/?code=' + url);
			}
		},

		policy: function () {
			let self = this;
			$s.api.getPolicy('',
				function (response) {
					self.terms = response.clause;
				}
			);
		},

		getLatelyItems: function () {

			var self = this;

			var param = {
				ids: this.latelyItems.toString(),
				limit: 4
			}

			$s.api.getLatelyItems(param,
				function (response) {
					var list = response.list;
					self.latelyItemList = list;
				},
				function (error) {
					$s.error(error);
				});
		},

		getAbout: function () {
			let self = this;
			$s.api.getAbout('', function (response) {
				self.about = response.about;
			});
		},

		getAccountNumbers: function () {
			let self = this;
			$s.api.getAccountNumbers(function (response) {
				self.accountNumbers = response.accountNumbers;
			});
		},

	},


	computed: {
		cartQuantity: function () {
			return this.formatNumber(this.quick.cartQuantity);
		},
		wishlistCount: function () {
			return this.newWishlistCount > this.quick.wishlistCount ? this.formatNumber(this.newWishlistCount) : this.formatNumber(this.quick.wishlistCount);
		},
		latelyItemCount: function () {
			return this.newLatelyItemCount > this.latelyItems.length ? this.formatNumber(this.newLatelyItemCount) : this.formatNumber(this.latelyItems.length);
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
		}
	},
	mounted: function () {

		this.$nextTick(function () {

			this.isLogin = $s.isLogin();
			this.isGuestLogin = $s.isGuestLogin();

			this.getQuickInfo();
			this.policy();
			this.categoryInfo(this);
			this.latelyInfo(this);
			this.latelyItemInfo(this);
			//this.getLatelyItems();
			this.getAbout();
			this.getAccountNumbers();

			initializeFooterEvent();
		});

	},
	updated: function () {
		// initializeFooterEvent();
	}
};
</script>
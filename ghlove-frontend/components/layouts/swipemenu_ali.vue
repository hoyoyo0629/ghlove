<template>
	
		<nav class="gnb">
			
			<div class="container">
				<div class="gnb_slider_m">
					<div class="swiper-contents">
						<div class="swiper-container">
							<ul class="swiper-wrapper">
								<li class="swiper-slide" :class="showGnbTargetFlag('new') ? 'on' : ''"><a href="/event/new.html">신상품</a></li>
								<li class="swiper-slide" :class="showGnbTargetFlag('best') ? 'on' : ''"><a href="/event/best.html">베스트</a></li>
								<li class="swiper-slide" :class="showGnbTargetFlag('spot') ? 'on' : ''"><a href="/event/spot.html">타임세일</a></li>
								<li class="swiper-slide" :class="showGnbTargetFlag('featured') ? 'on' : ''"><a href="/featured/list.html">기획전</a></li>
								<li class="swiper-slide" :class="showGnbTargetFlag('stylebook') ? 'on' : ''"><a href="/event/stylebook.html">룩북</a></li>
							</ul>
							<!--<div class="swiper-button-next"></div>
							<div class="swiper-button-prev"></div>-->
						</div>
					</div>
				</div>

				<div class="gnb_slider_pc tablet">
					<div class="swiper-contents">

						<swiper ref="awesomeGnbSwiper" class="gnb-pc" :options="gnbSwiperOption">
						<!-- slides -->
						<swiper-slide v-for="group in category.groups" :key="group.url">
							<a href="javascript:void(0);" class="gnb_tit">{{group.name}}</a>
						</swiper-slide>

						<div class="swiper-button-prev" slot="button-prev"></div>
						<div class="swiper-button-next" slot="button-next"></div>
						</swiper>

<!--						<div class="swiper-container">
							<ul class="swiper-wrapper">
								<li class="swiper-slide" v-for="group in category.groups" :key="group.url">
									<a href="javascript:void(0);" class="gnb_tit">{{group.name}}</a>
								</li>
							</ul>
							<div class="swiper-button-next"></div>
							<div class="swiper-button-prev"></div>

						</div>-->
					</div>
				</div>
			</div>
			<div class="depth_wrap tablet">
				<!-- ======= 태블릿일 때 닫기 버튼(btn_close)를 눌러도 닫혀야 하고 팀/그룹을 눌러도 닫혀야 합니다 ======= -->
				<div class="gnb_depth" v-for="group in category.groups" :key="group.url">
					<div class="container">
						<h2 class="tit_team">{{group.name}}</h2> <!-- 팀/그룹 -->
						<div class="gnb_inner">
							<dl v-for="category1 in group.categories" :key="category1.url">
								<dt>{{category1.name}}</dt>

								<dd class="depth2_area" v-for="category2 in category1.childCategories"
									:key="category2.categoryId">

									<a href="javascript:void(0);"
									   @click="link(category2.url, category2.childCategories)" class="depth2_tit">{{category2.name}}</a>

									<template v-if="category2.childCategories.length > 0">

										<ul class="depth3">
											<li class="depth3_area" v-for="category3 in category2.childCategories"
												:key="category3.categoryId">

												<a href="javascript:void(0);"
												   @click="link(category3.url, category3.childCategories)"
												   class="depth3_tit">{{category3.name}}</a>

												<template v-if="category3.childCategories.length > 0">
													<ul class="depth4">
														<li v-for="category4 in category3.childCategories"
															:key="category4.categoryId">
															<a href="javascript:void(0);"
															   @click="link(category4.url, category4.childCategories)">{{category4.name}}</a>
														</li>
													</ul>
												</template>

											</li>
										</ul>

									</template>
								</dd>
							</dl>
						</div>
					</div>
					<a href="#" class="btn_close"></a>
				</div>
			</div>
		</nav>
	
</template>


<script>
	module.exports = {
		props: {
			newCartQuantity: 0,
			gnbMenuTarget: {
				type: String,
				default: function() {
					return '';
				}
			}
		},
		data: function() {
			return {
				cartQuantity:0,
				isLogin: false,
				isGuestLogin: false,
				category: {
					groups: []
				},
				searchWord: '',
				latelySearch: [],
				bestSearchWord: null,
				latelyItems: [],
				recommendSearch: {},
                gnbSwiperOption: {
                    slidesPerView: 'auto',
                    spaceBetween: 40,
                    navigation: {
                        nextEl: '.swiper-contents .swiper-button-next',
                        prevEl: '.swiper-contents .swiper-button-prev',
                    },
                    sliderMove:1,
                    on: {
                        init: function() {
                            if ($(".gnb_slider_pc .swiper-button-prev").is(".swiper-button-disabled")) {
                                $(".gnb_slider_pc .swiper-wrapper").removeClass("prv_v");
                            } else {
                                $(".gnb_slider_pc .swiper-wrapper").addClass("prv_v");
                            }
                            if ($(".gnb_slider_pc .swiper-button-next").is(".swiper-button-disabled")) {
                                $(".gnb_slider_pc .swiper-wrapper").removeClass("nxt_v");
                            } else {
                                $(".gnb_slider_pc .swiper-wrapper").addClass("nxt_v");
                            }
                        }
                    }
                }
            }
		},
		computed: {
			/********************************
            displayCartQuantites: function() {
                return this.newCartQuantity > this.cartQuantity ? this.newCartQuantity :   this.cartQuantity;
            },
            showLogin: function () {
                return !(this.isLogin || this.isGuestLogin);
            }*************************** */
        },
		methods: {
			
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
				// $s.api.getCartInfo(function (response) {
				// 	self.cartQuantity = response.cartQuantity;
				// }, function(error) {
                //     $s.alert(error.response.data.message);
                // });
			},
			logout: function () {
				$s.logout();
			},
			link: function (url, childCategories) {

				var flag = typeof childCategories != 'undefined'
					&& childCategories != null
					&& childCategories.length > 0;

				if (!flag) {
					$s.redirect('/category/?code='+ url);
				}
			},
			search: function (word) {
				if (this.latelySearch == null) {
                    this.latelySearch = [];
                }

                if (this.latelySearch.indexOf(word) < 0) {
                    this.latelySearch.unshift(word);
                }

                $s.core.setData($s.const.LATELY_SEARCH, JSON.stringify(this.latelySearch));

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

				if (word === '' && recommend != null && recommend.searchContents != null && recommend.searchContents !== '') {
                    var searchLink = '';
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

                if (word != '') {
                    this.search(word);
                }
				
			},
			getBestSearchWord: function () {
				var self = this
				$s.api.getBestKeyword(function(response) {
					self.bestSearchWord = response.list;
				}, function () {
					$s.alert(error.response.data.message);
				});
			},
            getRecommendSearchWord: function () {
                var self = this
                $s.api.getRecommendKeyword(function(response) {
                    self.recommendSearch = response.search;
                }, function () {
                    $s.alert(error.response.data.message);
                });
            },
			showGnbTargetFlag: function (target) {
				return this.gnbMenuTarget == target;
			},
			saveVisitData: function() {
				var today = new Date().getTime();
				var visit = $s.core.getData($s.const.VISIT);
				var visitExpireDate = $s.core.getData($s.const.VISIT_EXPIRE_DATE);

				if (visit == null || visit == '' || today > visitExpireDate) {
					var agent = navigator.userAgent;
					var referrer = document.referrer;
					var domain = vm.findDomain(referrer);
					var self = this;

					$s.axios
						.get("https://api.ipify.org", {}, {})
						.then(function (response) {
							var param = {
								'agent': agent,
								'referer': referrer,
								'browser': self.getBrowser(agent),
								'os': self.getOs(agent),
								'domain': domain,
								'domainName': self.getDomainName(domain),
								'remoteAddr': response.data,
								'language': navigator.language
							};

							$s.api.saveVisitData(param, function (response) {
								if (response.status === 'OK') {
									$s.core.setData('visit', '1');

									var expireDate = today + 1 * 1000 * 60 * 60 * 24;
									$s.core.setData('visit_expire_date', expireDate);
								}
							});
						})
				}
			}
		},
		mounted: function() {

		
			this.$nextTick(function () {
				this.isLogin = $s.isLogin();
				this.isGuestLogin = $s.isGuestLogin();

                this.getCartInfo();
                this.getBestSearchWord();
                this.getRecommendSearchWord();
                this.categoryInfo(this);
                this.latelyInfo(this);
                this.latelyItemInfo(this);
                // this.saveVisitData();

                initializeHeaderEvent(); 
			});
			
		},
	};
</script>

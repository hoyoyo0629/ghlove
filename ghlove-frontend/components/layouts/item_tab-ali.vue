<template>
	<div class="item_tab" id="item_tab_wrap">
		<ul class="nav nav-tabs nav-justified center">
			<li class="nav-item">
				<a href="#nav-detail" class="nav-link active">
					<!-- <a href="#nav-detail" class="nav-link active" data-toggle="tab"> -->
					<span class="txt">답례품정보</span>
					<span class="sr-only">선택됨</span>
				</a>
			</li>
			<li class="nav-item">
				<a href="#nav-review" class="nav-link">
					<!-- <a href="#nav-review" class="nav-link" data-toggle="tab"> -->
					<span class="txt">답례품후기<span class="pointblue">&nbsp{{formatNumber(review.totalElements)}}</span></span>
				</a>
			</li>
			<li class="nav-item">
				<a href="#nav-qna" class="nav-link">
					<!-- <a href="#nav-qna" class="nav-link" data-toggle="tab"> -->
					<span class="txt">답례품Q&amp;A<span class="pointblue">&nbsp{{formatNumber(qna.totalElements)}}</span></span>
				</a>
			</li>
			<li class="nav-item">
				<a href="#nav-buyer" class="nav-link">
					<!-- <a href="#nav-buyer" class="nav-link" data-toggle="tab"> -->
					<span class="txt">배송/반품/교환</span>
				</a>
			</li>
			<li class="nav-item">
				<a href="#nav-notice" class="nav-link">
					<!-- <a href="#nav-notice" class="nav-link" data-toggle="tab"> -->
					<span class="txt">상품고시</span>
				</a>
			</li>
		</ul>
	</div>
	<!--// item_tab E -->
</template>
<script>
module.exports = {
	props: {
		review: Object,
		qna: Object
	},
	data: function () {
		return {
			isMobile: $s.isMobile(),
			pageUrl: '',
			showReviewForm: false,
			itemUserCode: '',
			error: {},
			itemRelations: [],
			newCartQuantity: 0,
			newLatelyItemCount: 0,
			newWishlistCount: 0,
			latelyItems: [],
			display: {
				quantity: 1,
				presentPrice: 0
			},
			param: {
				filter: {
					filterCodeIds: [],
					initFilterCodeIds: []
				},
				order: {
					arrayRequiredItems: [],
					itemSets: [],
					campaignCode: null
				},
				review: {
					page: 1,
					itemsPerPage: 5,
					itemUserCode: '',
					size: 5,
					orderBy: 'LIKE_COUNT',
					fcIds: ''
				},
				qna: {
					page: 1,
					itemsPerPage: 5,
					itemUserCode: '',
					size: 5
				},
				coupon: {
					page: 1,
					itemsPerPage: 5,
					itemUserCode: '',
					size: 5
				},
				delivery: {
					page: 1,
					itemsPerPage: 5,
					query: '',
					where: 'ADDRESS',
					size: 5
				},
				category: {
					categoryCode: '',
					page: 1,
					itemsPerPage: 20,
				},
				unRegisterReview: {
					showStarFlags: [],
					content: '',
					subject: '',
					files: [],
					fileImages: []
				}
			},
			item: {
				freeGiftItemList: [],
				itemImages: [],
				itemInfos: [],
				itemOptions: [],
				itemRelations: [],
				itemSets: []
			},
			itemOptionInfo: {
				optionPrices: [],
				optionStockFlags: [],
				optionStockQuantity: [],
				selectOptionName1: '',
				selectOptionName2: '',
				selectOptionName3: '',
				selectOptionIndex1: '',
				selectOptionIndex2: '',
				selectOptionIndex3: '',
				optionName1: [],
				optionName2: [],
				optionName3: [],
				optionName1Ids: [],
				optionName2Ids: [],
				optionName3Ids: [],
				optionSoldOuts: [],
				textOptionValues: [],
				addOptionList: []
			},
			itemSetInfo: {
				amount: 0,
				optionCount: 0,
				selectList: [],
				addOptionList: []
			},
			list: [],
			pointPolicy: {},
			config: {},
			cardBenefits: {},
			seller: {
				companyName: '',
				representativeName: '',
				businessNumber: '',
				telephoneNumber: '',
				email: '',
				faxNumber: '',
				address: '',
				addressDetail: ''
			},
			userId: 0,
			result: {
				review: {
					currentPage: 1,
					totalPages: 0,
					totalElements: 0,
					content: {}
				},
				qna: {
					currentPage: 1,
					totalPages: 0,
					totalElements: 0,
					content: {}
				},
				coupon: {
					currentPage: 1,
					totalPages: 0,
					totalElements: 0,
					content: {}
				},
				delivery: {
					number: 0,
					content: {}
				}
			},
			itemQna: {
				qnaGroup: '',
				secretFlag: 'N'
			},
			secretFlag: false,
			slides: [
				{
					imageId: '',
					image: ''
				},
			],
			category: {
				groups: []
			},
			currentCategories: [],
			currentChildForMobile: [],
			current: {
				categoryLevel: '',
				groupUrl: '',
				groupName: '',
				category1Url: '',
				category1Name: '',
				category2Url: '',
				category2Name: '',
				category3Url: '',
				category3Name: '',
			},
			breadcrumbs: [],
			isRestockNotice: '',
			earnPoint: {
				levelPoint: 0,
				point: 0,
				totalPoint: 0,
				levelPointRate: 0,
				pointRate: 0,
				levelName: ''
			},
			reviewFilters: [],
			qnaGroups: [],
			swiperOption: {
				thumbSlider: {
					slidesPerView: 'auto',
					spaceBetween: 8,
					freeMode: true,
					watchSlidesVisibility: true,
					watchSlidesProgress: true,
					// allowTouchMove: false
				},
				mainSlider: {
					spaceBetween: 15,
					navigation: {
						nextEl: '.view_photo .swiper-button-next',
						prevEl: '.view_photo .swiper-button-prev',
					},
					pagination: {
						el: '.view_photo .swiper-pagination',
					},
					thumbs: {
						swiper: this.viewThumbsSwiper
					}
				},
				itemSlider: {
					slidesPerView: 4,
					centerSlides: true,
					spaceBetween: 30,
					navigation: {
						nextEl: '.item_slider .swiper-button-next',
						prevEl: '.item_slider .swiper-button-prev',
					},
					breakpoints: {
						767: {
							slidesPerView: 2,
							spaceBetween: 16
						},
						991: {
							slidesPerView: 3,
							spaceBetween: 20
						}
					}
				}
			},

			/* event */
			event: {
				showSetItem: true,
				showQnaForm: true,
				showBuyWrap: false,
				showBuyView: false,
				isBuyViewFixed: false,
				optionType: "",
				itemReviewId: 0,
				qnaId: 0
			}
		}
	},
	methods: {
		optSelect: function () {
			$(".dropdown_box").addClass("on");
			e.stopPropagation();
		},
		optClose: function () {
			$(".dropdown_box").removeClass("on");
		},
		handleScrollByTab: function (evt, el) {
			// jquery (el.offset().top) -> vanilla js (el.getBoundingClientRect().top + window.scrollY)
			const top = el.getBoundingClientRect().top + window.scrollY;
			const scrollTop = document.scrollingElement.scrollTop;
			const width = window.innerWidth;
			vm.event.isBuyViewFixed = width >= 768 && top - 100 < scrollTop;
		},
		handleScrollByView: function (evt, el) {
			if (vm.event.isBuyViewFixed) {
				// jquery (el.innerHeight()) -> vanilla js (el.offsetHeight - border)
				const c = window.getComputedStyle(el);
				const border = parseFloat(c.borderTopWidth) + parseFloat(c.borderBottomWidth);
				el.style.bottom = -(el.offsetHeight - border + 4) + 'px';
			}
		},
		fileSelect: function (e) {
			var size = 5;
			var limit = 4;
			var extensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp'];

			var selectFiles = this.param.unRegisterReview.files;
			var images = this.param.unRegisterReview.fileImages;
			var fileList = e.target.files;

			// 파일 첨부 개수 체크
			if (limit < selectFiles.length + fileList.length) {
				$s.alert('사진은 ' + limit + '장까지 첨부 가능합니다.');
				return;
			}

			for (var i = 0; i < fileList.length; i++) {
				var file = fileList[i];
				var fileExt = fileList[i].name;

				// 사이즈 체크
				if (fileExt != "") {
					if (file.size > (size * 1024 * 1024)) {
						$s.alert("파일크기는 " + size + "MB 이내로 등록 가능합니다.");
						return;
					}
				}

				// 확장자 체크
				fileExt = fileExt.slice(fileExt.indexOf(".") + 1).toLowerCase();
				if (!extensions.includes(fileExt)) {
					$s.alert("이미지 파일(jpg, jpeg, png, gif, bmp) 만 등록 가능합니다.");
					return;
				}

				// 미리보기 생성
				try {
					var reader = new FileReader();
					var self = this;

					reader.onload = function (ie) {
						images.push(ie.target.result);
						self.param.unRegisterReview.fileImages = images;
					};

					selectFiles.push(file);
					self.param.unRegisterReview.files = selectFiles;

					reader.readAsDataURL(file);
				} catch (e) {
					$s.error(e);
				}
			}
		},

		fileDelete: function (fileIndex) {
			var files = this.param.unRegisterReview.files;
			var images = this.param.unRegisterReview.fileImages;

			files.splice(fileIndex, 1);
			images.splice(fileIndex, 1);
		},

		rating: function (starIndex) {

			var score = starIndex + 1;

			this.param.unRegisterReview.score = score;

			var showStarFlags = [false, false, false, false, false];

			for (var i = 0; i <= starIndex; i++) {
				showStarFlags[i] = true;
			}

			this.param.unRegisterReview.showStarFlags = showStarFlags;
		},
		closeReviewForm: function () {
			vm.showReviewForm = false;
		},
		writeReview: function () {
			$s.redirect('/mypage/order.html');
		},
		getItemRelations: function () {

			var param = {
				itemUserCode: this.itemUserCode
			}

			$s.api.viewItemRelations(param, function (response) {
				vm.itemRelations = response.list;
			});
		},
		inputQuantity: function (e, index) {
			var isSet = vm.item.itemType === '3';
			var keyValue = e.target.value.replace(/[^0-9]/g, "");

			if (keyValue.substring(0, 1) == 0) {
				keyValue = keyValue.substring(1, keyValue.length);
			}

			if (!this.checkForItem("manual", index)) {
				return;
			}

			if (isSet) {
				var itemSets = vm.param.order.itemSets;

				itemSets[index].quantity = parseInt(keyValue);
				itemSets[index].amount = itemSets[index].tempAmount * itemSets[index].quantity;
			} else {
				var addOptionList = vm.itemOptionInfo.addOptionList;
				addOptionList[index].quantity = parseInt(keyValue);
				addOptionList[index].optionPrice = addOptionList[index].tempOptionPrice * addOptionList[index].quantity;

				this.setRequiredItem(index);
			}
		},
		getItemReviews: function (page) {
			this.param.review.itemUserCode = this.itemUserCode;
			this.param.review.page = page;

			$s.api.getItemReviewsForDetail(this.param.review,
				function (response) {
					vm.result.review = response;
				}
			);
		},
		getItemQna: function (page) {
			this.param.qna.page = page;
			this.param.qna.itemUserCode = this.itemUserCode;
			console.log(this.param.qna.itemUserCode);
			$s.api.getItemQna(this.param.qna,
				function (response) {
					vm.result.qna = response;
					console.log(vm.result.qna);
				}
			);
		},
		getDownloadCouponList: function (page) {
			this.param.coupon.itemUserCode = this.itemUserCode;
			this.param.coupon.page = page;
			$s.api.downloadItemCouponList(this.param.coupon,
				function (response) {
					vm.result.coupon = response;
				}
			);
		},
		getIslandType: function (page) {
			this.param.delivery.page = page;

			$s.api.getIslandType(this.param.delivery,
				function (response) {
					vm.result.delivery = response.pageContent;
				}
			);
		},
		paging: function (page, target) {

			if (target == 'review') {

				this.getItemReviews(page);

			} else if (target == 'qna') {

				this.getItemQna(page);

			} else if (target == 'coupon') {

				this.getDownloadCouponList(page);

			} else if (target == 'delivery') {

				this.getIslandType(page);

			} else if (target == 'all') {

				this.getItemReviews(page);
				this.getItemQna(page);
				this.getDownloadCouponList(page);
				this.getIslandType(page);

			}
		},

		isUserDiscount: function () {
			return this.item.exceptUserDiscountPresentPrice != this.item.presentPrice;
		},

		plus: function (index) {
			var isSet = vm.item.itemType === '3';

			if (isSet) {
				var itemSets = vm.param.order.itemSets;

				itemSets[index].quantity += 1;
				if (!this.checkForItem("plus", index)) {
					return;
				}

				itemSets[index].amount = itemSets[index].tempAmount * itemSets[index].quantity;
			} else {
				if (!this.checkForItem("plus", index)) {
					return;
				}

				var addOptionList = vm.itemOptionInfo.addOptionList;

				addOptionList[index].quantity += 1;
				addOptionList[index].optionPrice = addOptionList[index].tempOptionPrice * addOptionList[index].quantity;

				this.setRequiredItem(index);
			}
		},

		minus: function (index) {
			var isSet = vm.item.itemType === '3';

			if (!this.checkForItem("minus", index)) {
				return;
			}

			if (isSet) {
				var itemSets = vm.param.order.itemSets;

				itemSets[index].quantity -= 1;
				itemSets[index].amount = itemSets[index].tempAmount * itemSets[index].quantity;
			} else {
				var addOptionList = this.itemOptionInfo.addOptionList;

				addOptionList[index].quantity -= 1;
				addOptionList[index].optionPrice = addOptionList[index].tempOptionPrice * addOptionList[index].quantity;

				this.setRequiredItem(index);
			}
		},
		setRequiredItem: function (index) {
			let addOptionList = vm.itemOptionInfo.addOptionList;
			let item = vm.item.itemId;
			let option = '';
			let arrayRequiredItems = vm.param.order.arrayRequiredItems;
			let arrayRequiredItem = arrayRequiredItems[index];
			let itemPrice = vm.item.presentPrice;

			if (this.item.itemOptionFlag === 'N' && addOptionList[index] == null) {
				addOptionList.push({
					'quantity': vm.item.orderMinQuantity,
					'optionPrice': itemPrice,
					'tempOptionPrice': itemPrice
				});
			} else {
				option = arrayRequiredItem.substr(arrayRequiredItem.lastIndexOf("||") + 2);
			}

			let quantity = addOptionList[index].quantity;
			if (arrayRequiredItems.length == 0) {
				arrayRequiredItems.push(item + '||' + quantity + '||' + option);
			} else {
				arrayRequiredItems[index] = item + '||' + quantity + '||' + option;
			}
		},
		setRequiredItemSets: function () {
			let itemSets = vm.item.itemSets;
			let itemSetInfo = vm.itemSetInfo;

			itemSetInfo.selectList = new Array();
			itemSetInfo.optionCount = 0;

			for (let i = 0; i < itemSets.length; i++) {
				// 최대 & 최소 구매수량 조정
				if (itemSets[i].item.orderMinQuantity === -1) itemSets[i].item.orderMinQuantity = 1;
				if (itemSets[i].item.orderMaxQuantity === -1) itemSets[i].item.orderMaxQuantity = 999;

				// if (itemSets[i].item.itemOptionFlag === "Y") {
				itemSetInfo.optionCount++;
				// }

				itemSetInfo.selectList.push({
					itemId: itemSets[i].item.itemId,
					itemName: itemSets[i].item.itemName,
					stockFlag: itemSets[i].item.stockFlag,
					stockQuantity: itemSets[i].item.stockQuantity,
					quantity: itemSets[i].quantity,
					itemOption: null
				});
			}
		},
		deleteOption: function (index) {
			this.itemOptionInfo.addOptionList.splice(index, 1);
			this.param.order.arrayRequiredItems.splice(index, 1);
		},
		buyOrder: function () {
			if ((!$s.isMobileUI() || vm.event.showBuyWrap) && this.checkForItem('buy_now', 0)) {
				var campaignCode = $s.core.getSession('campaign_code');
				vm.param.order.campaignCode = campaignCode;

				$s.api.buyOrder(vm.param.order, function (response) {
					var url = '/order/step1.html';

					if (!$s.isLogin()) {
						$s.core.setSession($s.const.BUY_ORDER, JSON.stringify(vm.param.order));
						url = '/order/no-member.html';
					}

					$s.redirect(url);
				}, function (error) {
					$s.alert(error.response.data.message);
				});
			}
			vm.event.showBuyWrap = true;
		},
		addToCart: function () {
			if ((!$s.isMobileUI() || vm.event.showBuyWrap) && this.checkForItem('cart', 0)) {
				$s.api.addToCart(vm.param.order, function (response) {
					if (response.status == 'OK') {
						// $s.api.getCartInfo(function (response) {
						// 	vm.newCartQuantity = response.cartQuantity;
						// });
						$s.toast('해당 답례품이 장바구니에 담겼습니다.');
					}
				}, function (error) {
					$s.alert(error.response.data.message);
				});
			}
			vm.event.showBuyWrap = true;
		},

		addToWishList: function () {
			$(".wishBtn").addClass("on");
			$s.api.addToWishList(vm.item.itemId, function (response) {
				if (response.status == 'OK') {
					$s.toast('해당 답례품이 관심답례품에 담겼습니다.');
					$s.api.getQuickInfo(function (data) {
						vm.newWishlistCount = data.wishlistCount;
					});
				}


			})
		},
		reviewAssessment: function (index, score) {
			if (index <= score) {
				return true;
			}
			return false;
		},
		clearOptionItems: function () {
			vm.itemOptionInfo.optionName1 = new Array();
			vm.itemOptionInfo.optionName1Ids = new Array();
			vm.itemOptionInfo.optionName2 = new Array();
			vm.itemOptionInfo.optionName2Ids = new Array();
			vm.itemOptionInfo.optionName3 = new Array();
			vm.itemOptionInfo.optionName3Ids = new Array();
			vm.itemOptionInfo.selectOptionName1 = '';
			vm.itemOptionInfo.selectOptionName2 = '';
			vm.itemOptionInfo.selectOptionName3 = '';
			vm.itemOptionInfo.selectOptionIndex1 = '';
			vm.itemOptionInfo.selectOptionIndex2 = '';
			vm.itemOptionInfo.selectOptionIndex3 = '';
			vm.itemOptionInfo.textOptionValues = new Array(vm.item.itemOptions.length);

			vm.itemOptionInfo.optionPrices = new Array();
			vm.itemOptionInfo.optionSoldOuts = new Array();
		},
		addOptionItems: function (index, pIndex, level) {
			let isSet = vm.item.itemType === "3";
			let checkDuplication = false;
			let itemOptionInfo = vm.itemOptionInfo;
			let optionId = "";

			// 선택한 옵션이 품절일 경우
			if (itemOptionInfo.optionSoldOuts[index]) {
				return false;
			}

			if (level === 1) {
				optionId = itemOptionInfo.optionName1Ids[index];
			} else if (level === 2) {
				optionId = itemOptionInfo.optionName2Ids[index];
			} else if (level === 3) {
				optionId = itemOptionInfo.optionName3Ids[index];
			}

			if (isSet) {
				let itemSets = vm.item.itemSets;
				let itemSetInfo = vm.itemSetInfo;

				// 선택 옵션정보 loop
				for (let i = 0; i < itemOptionInfo.addOptionList.length; i++) {
					// 선택 정보 중복시, 기존 선택정보 삭제
					if (itemOptionInfo.addOptionList[i].itemId === itemSets[pIndex].item.itemId) {
						itemOptionInfo.addOptionList.splice(i, 1);
						break;
					}
				}

				// 옵션 정보 추가
				this.addOptionList(index, pIndex, level, optionId);

				// 선택 옵션 숨김
				vm.event.optionType = "";

				// 옵션 초기화
				this.clearOptionItems();

				// 모든 옵션 선택시, 세트답례품 추가
				if (itemOptionInfo.addOptionList.length === itemSetInfo.optionCount) {
					this.addItemSets();

					// 옵션 정보 초기화
					itemOptionInfo.addOptionList = new Array();
				}
			} else {
				for (let i = 0; i < vm.param.order.arrayRequiredItems.length; i++) {
					let requiredItem = vm.param.order.arrayRequiredItems[i];

					if (requiredItem.indexOf(optionId) >= 0) {
						checkDuplication = true;
						break;
					}
				}

				if (!checkDuplication) {
					vm.param.order.arrayRequiredItems.push(vm.item.itemId + "||" + vm.item.orderMinQuantity + "||" + optionId + "```");
					this.addOptionList(index, pIndex, level, optionId);
				}

				vm.event.optionType = '';

				// 옵션 초기화
				this.clearOptionItems();

				// 옵션 세팅
				this.writeOptionName();
			}
		},
		addTextOption: function () {
			let itemOptions = vm.item.itemOptions;
			let textOptionValues = vm.itemOptionInfo.textOptionValues;
			let addOptionList = vm.itemOptionInfo.addOptionList;
			let optionData = "";
			let optionName = "";

			let isTemp = itemOptions.some((option, i) => {
				if (textOptionValues[i] === "" || textOptionValues[i] == null) {
					optionName = "";
					$s.alert(option.optionName1 + "의 옵션을 입력해주세요.");
					return true;
				}

				if (i > 0) {
					optionName += " / ";
					optionData += "^^^";
				}

				optionData += option.itemOptionId + "```" + textOptionValues[i];
				optionName += option.optionName1 + ": " + textOptionValues[i];
			});

			if (!isTemp) {
				let isAdded = addOptionList.some(option => {
					if (option.optionName === optionName) {
						return true;
					}
				});

				if (!isAdded) {
					vm.param.order.arrayRequiredItems.push(vm.item.itemId + "||" + vm.item.orderMinQuantity + "||" + optionData);
					addOptionList.push({
						'optionName': optionName,
						'quantity': vm.item.orderMinQuantity,
						'optionPrice': vm.item.presentPrice * vm.item.orderMinQuantity,
						'tempOptionPrice': vm.item.presentPrice,
						'optionStockQuantity': vm.item.itemOptions[0].optionStockQuantity
					});
				}

				this.clearOptionItems();
			}
		},
		addItemSets: function () {
			if (vm.item.itemSoldOutFlag === "Y") {
				return false;
			}

			let itemSetInfo = vm.itemSetInfo;						// 세트답례품 temp (데이터 만드는 용도)
			let orderItemSets = vm.param.order.itemSets;			// 세트답례품 주문정보

			itemSetInfo.selectList.forEach(selectInfo => {
				let optionInfo = "";
				if (selectInfo.itemOption != null) {
					optionInfo = selectInfo.itemOption.optionId + "```";
				}
				itemSetInfo.addOptionList.push(selectInfo.itemId + "||" + selectInfo.quantity + "||" + optionInfo);
			});

			// 세트 옵션 중복 체크
			let isDuplicate = orderItemSets.some(orderItemSet => {
				if (JSON.stringify(orderItemSet.arrayItemSets) === JSON.stringify(itemSetInfo.addOptionList)) {
					return true;
				}
			});

			if (!isDuplicate) {
				// 옵션금액 총합 + 현재 답례품가격
				itemSetInfo.amount += vm.item.presentPrice;

				// 세트답례품 주문 정보 세팅
				orderItemSets.push({
					'itemId': vm.item.itemId,
					'quantity': 1,
					'amount': itemSetInfo.amount,
					'tempAmount': itemSetInfo.amount,
					'arrayItemSetInfos': itemSetInfo.selectList,
					'arrayItemSets': itemSetInfo.addOptionList
				});

				// 재고 체크
				if (!vm.isAvailableSetStock('create')) {
					orderItemSets.pop();
					return false;
				}
			}

			// 세트답례품 정보 초기화
			itemSetInfo.amount = 0;
			itemSetInfo.addOptionList = new Array();
			this.setRequiredItemSets();
		},
		isAvailableSetStock: function (target, index) {
			index = index || 0;	// 현재 세트답례품 주문 데이터 index
			var buyItems = [];	// 재고 체크용 temp array

			var order = vm.param.order;	// 주문 정보

			var isAvailable = true;	// 재고 가능 여부
			var alertText = '';		// alert 표시 text
			var inputQuantity = order.itemSets[index].quantity; // 입력 수량

			// 세트답례품 주문 정보 loop
			for (var i = 0; i < order.itemSets.length; i++) {
				// 세트답례품별 답례품정보 loop
				for (var j = 0; j < order.itemSets[i].arrayItemSetInfos.length; j++) {

					var info = order.itemSets[i].arrayItemSetInfos[j];

					// 각 답례품별 최종 구매 수량 (세트 수량 * 세트 답례품별 수량)
					var buyQuantity = order.itemSets[i].quantity * order.itemSets[i].arrayItemSetInfos[j].quantity;

					// 재고 체크용 데이터 만들기
					var isAdd = false;
					for (var z = 0; z < buyItems.length; z++) {
						// 이미 추가된 답례품일 경우, buyQuantity 를 변경
						// 옵션이 없는 경우 itemId 매칭, 옵션이 있는 경우 optionId 매칭
						var item = buyItems[z].item;
						if ((item.itemOption == null && item.itemId === info.itemId) ||
							(item.itemOption != null && info.itemOption != null && item.itemOption.optionId === info.itemOption.optionId)) {
							buyItems[z].buyQuantity += buyQuantity;
							isAdd = true;
							break;
						}
					}

					if (!isAdd) {
						// 새로 추가된 답례품 저장
						buyItems.push({
							'item': info,
							'buyQuantity': buyQuantity
						});
					}
				}
			}

			// 재고 수량 내림차순 정렬
			if (buyItems.length > 1) {
				buyItems.sort(function (a, b) {
					return Math.floor(b.item.stockQuantity / b.item.quantity) - Math.floor(a.item.stockQuantity / a.item.quantity);
				});
			}

			// 만들어진 데이터로 재고 체크 및 수량 조절 (재고 수량이 많은 답례품부터 내림차순 조절)
			for (var i = 0; i < buyItems.length; i++) {
				var buyQuantity = buyItems[i].buyQuantity;
				var stockFlag = buyItems[i].item.stockFlag;
				var stockQuantity = buyItems[i].item.stockQuantity;

				if (stockFlag === 'Y' && buyQuantity > stockQuantity) {
					// 재고 내 가능한 최대값을 세팅
					// 수량 조절 : [재고수량 - {총 구매수량 - (입력수량 * 세트수량)}] / 세트수량
					if (target === 'manual' || target === 'order' || target === 'plus') {
						order.itemSets[index].quantity = Math.floor((stockQuantity - (buyQuantity - (inputQuantity * buyItems[i].item.quantity))) / buyItems[i].item.quantity);
					}

					// alert text 세팅
					alertText = this.unescapeHtml(buyItems[i].item.itemName);
					if (buyItems[i].item.itemOption != null) { // 옵션 존재시, 옵션명 노출
						alertText += ' <' + this.unescapeHtml(buyItems[i].item.itemOption.optionName) + '>';
					}
					alertText += '의 최대 구매 수량은 ' + buyItems[i].item.stockQuantity + '개 입니다.';

					isAvailable = false;
				}
			}

			// 재고 수량이 가장 적은 마지막 답례품 정보만 노출됨
			if (!isAvailable) {
				$s.alert(alertText);
			}

			return isAvailable;
		},
		writeOptionName: function (level, optionName, index, pIndex) {
			level = level || 0;
			index = index || 0;
			pIndex = pIndex || 0;
			let isSet = vm.item.itemType === '3';
			let item = isSet ? vm.item.itemSets[pIndex].item : vm.item;
			let itemOptions = item.itemOptions;
			let itemOptionInfo = vm.itemOptionInfo;
			let checkDuplication = '';

			if (item.itemOptionType === "T") {
				itemOptionInfo.textOptionValues = new Array(itemOptions.length);
			} else {
				if (level === 1) {
					itemOptionInfo.selectOptionName1 = optionName;
					itemOptionInfo.selectOptionIndex1 = index;
					itemOptionInfo.optionName2 = [];
					itemOptionInfo.optionName3 = [];
					itemOptionInfo.optionName2Ids = [];
					itemOptionInfo.optionName3Ids = [];

					if (item.itemOptionType === "S") {
						this.addOptionItems(index, pIndex, level);
						return false;
					} else {
						// 다음 옵션 표시
						this.displayNextOptionGroup(pIndex, level);
						itemOptionInfo.optionPrices = [];
						itemOptionInfo.optionSoldOuts = [];
						itemOptionInfo.optionStockFlags = [];
						itemOptionInfo.optionStockQuantity = [];
					}
				} else if (level === 2) {
					itemOptionInfo.selectOptionName2 = optionName;
					itemOptionInfo.selectOptionIndex2 = index;
					itemOptionInfo.optionName3 = [];
					itemOptionInfo.optionName3Ids = [];

					if (item.itemOptionType === "S2") {
						this.addOptionItems(index, pIndex, level);
						return false;
					} else {
						this.displayNextOptionGroup(pIndex, level);
						itemOptionInfo.optionPrices = [];
						itemOptionInfo.optionSoldOuts = [];
						itemOptionInfo.optionStockFlags = [];
						itemOptionInfo.optionStockQuantity = [];
					}

				} else if (level === 3) {
					itemOptionInfo.selectOptionName3 = optionName;
					itemOptionInfo.selectOptionIndex3 = index;
					this.addOptionItems(index, pIndex, level);
				}

				for (let i = 0; i < itemOptions.length; i++) {
					if (level === 0) {
						itemOptionInfo.itemOptionTitle1 = item.itemOptionTitle1;
						itemOptionInfo.itemOptionTitle2 = item.itemOptionTitle2;
						itemOptionInfo.itemOptionTitle3 = item.itemOptionTitle3;

						if (item.itemOptionType === "S") {
							if (checkDuplication != itemOptions[i].optionName2) {
								itemOptionInfo.optionName1.push(itemOptions[i].optionName2);
								itemOptionInfo.optionPrices.push(itemOptions[i].optionPrice);
								itemOptionInfo.optionSoldOuts.push(itemOptions[i].soldOut);
								itemOptionInfo.optionStockFlags.push(itemOptions[i].optionStockFlag);
								itemOptionInfo.optionStockQuantity.push(itemOptions[i].optionStockQuantity);
							}

							checkDuplication = itemOptions[i].optionName2;
						} else {
							if (checkDuplication != itemOptions[i].optionName1) {
								itemOptionInfo.optionName1.push(itemOptions[i].optionName1);
								itemOptionInfo.optionPrices.push(itemOptions[i].optionPrice);
								itemOptionInfo.optionSoldOuts.push(itemOptions[i].soldOut);
								itemOptionInfo.optionStockFlags.push(itemOptions[i].optionStockFlag);
								itemOptionInfo.optionStockQuantity.push(itemOptions[i].optionStockQuantity);
							}

							checkDuplication = itemOptions[i].optionName1;
						}

						itemOptionInfo.optionName1Ids.push(itemOptions[i].itemOptionId);
					} else if (level === 1) {
						if (checkDuplication != itemOptions[i].optionName2 && itemOptions[i].optionName1 == itemOptionInfo.selectOptionName1) {
							itemOptionInfo.optionName2.push(itemOptions[i].optionName2);
							itemOptionInfo.optionName2Ids.push(itemOptions[i].itemOptionId);
							itemOptionInfo.optionPrices.push(itemOptions[i].optionPrice);
							itemOptionInfo.optionSoldOuts.push(itemOptions[i].soldOut);
							itemOptionInfo.optionStockFlags.push(itemOptions[i].optionStockFlag);
							itemOptionInfo.optionStockQuantity.push(itemOptions[i].optionStockQuantity);
							checkDuplication = itemOptions[i].optionName2;
						}
					} else if (level === 2) {
						if (itemOptions[i].optionName1 == itemOptionInfo.selectOptionName1 && itemOptions[i].optionName2 == itemOptionInfo.selectOptionName2) {
							itemOptionInfo.optionName3.push(itemOptions[i].optionName3);
							itemOptionInfo.optionName3Ids.push(itemOptions[i].itemOptionId);
							itemOptionInfo.optionPrices.push(itemOptions[i].optionPrice);
							itemOptionInfo.optionSoldOuts.push(itemOptions[i].soldOut);
							itemOptionInfo.optionStockFlags.push(itemOptions[i].optionStockFlag);
							itemOptionInfo.optionStockQuantity.push(itemOptions[i].optionStockQuantity);
						}
					} else if (level === 3) {
						if (itemOptions[i].optionName1 == itemOptionInfo.selectOptionName1
							&& itemOptions[i].optionName2 == itemOptionInfo.selectOptionName2
							&& itemOptions[i].optionName3 == itemOptionInfo.selectOptionName3) {

							itemOptionInfo.optionPrices.push(itemOptions[i].optionPrice);
							itemOptionInfo.optionSoldOuts.push(itemOptions[i].soldOut);
							itemOptionInfo.optionStockFlags.push(itemOptions[i].optionStockFlag);
							itemOptionInfo.optionStockQuantity.push(itemOptions[i].optionStockQuantity);
						}
					}
				}// for E
			}
		},
		writeSetOptionName: function (optionType, pIndex) {
			// 옵션 초기화
			this.clearOptionItems();

			// 옵션 세팅
			this.writeOptionName(0, '', 0, pIndex);

			vm.event.optionType = optionType;
		},
		displayNextOptionGroup: function (pIndex, level) {
			var isSet = vm.item.itemType === '3';
			var item = isSet ? vm.item.itemSets[pIndex].item : vm.item;
			var itemOptionType = item.itemOptionType;
			var itemOptionInfo = vm.itemOptionInfo;

			if (itemOptionType === 'S2') {
				itemOptionInfo.optionName1 = new Array();
				itemOptionInfo.optionName1Ids = new Array();
			} else if (itemOptionType === 'S3') {
				if (level === 1) {
					itemOptionInfo.optionName1 = new Array();
					itemOptionInfo.optionName1Ids = new Array();
				} else if (level === 2) {
					itemOptionInfo.optionName2 = new Array();
					itemOptionInfo.optionName2Ids = new Array();
				}
			}
		},
		checkForItem: function (target, index) {
			var isSet = vm.item.itemType === '3';

			// 비회원 구매 불가인 경우 로그인 페이지로 이동
			if (this.checkForNonmemberOrder() === false) {
				return false;
			}

			// 품절 확인
			if (vm.item.itemSoldOutFlag === 'Y') {
				$s.alert('해당 답례품은 판매 종료 되었습니다.');
				return false;
			}

			if (target === 'wishlist') {
				return true;
			}

			if (isSet) {
				// 세트 답례품
				var itemSets = vm.param.order.itemSets;

				if (target === 'cart' || target === 'buy_now') {
					if (itemSets.length === 0) {
						$s.alert('답례품 필수옵션을 선택하세요.');
						return false;
					}

					if (!vm.isAvailableSetStock('order', index)) {
						return false;
					}
				} else if (target === 'minus') {
					if (itemSets[index].quantity <= 1) {
						$s.alert('답례품을 1개 이상 선택해주세요.');
						return false;
					}
				} else if (target === 'plus') {
					if (!vm.isAvailableSetStock('plus', index)) {
						return false;
					}
				} else if (target === 'manual') {
					if (!vm.isAvailableSetStock('manual', index)) {
						itemSets[index].amount = itemSets[index].tempAmount * itemSets[index].quantity;
						return false;
					}

					if (itemSets[index].quantity < 1) {
						$s.alert('답례품을 1개 이상 선택해주세요.');
						itemSets[index].quantity = 1;
						itemSets[index].amount = itemSets[index].tempAmount * itemSets[index].quantity;
						return false;
					}
				} else {
					$s.alert('처리할 수 없습니다.');
					return false;
				}
			} else {
				// 일반 답례품
				var addOptionList = vm.itemOptionInfo.addOptionList;
				var optionName = '';
				var stockFlag = vm.item.stockFlag;
				var stockQuantity = vm.item.stockQuantity;

				if (target === 'cart' || target === 'buy_now') {
					if (vm.param.order.arrayRequiredItems.length === 0) {
						$s.alert('답례품 필수옵션을 선택하세요.');
						return false;
					}
				} else if (target === 'minus') {
					if (vm.item.orderMinQuantity === addOptionList[index].quantity) {
						$s.alert('최소 구매 수량은 ' + vm.item.orderMinQuantity + '개 입니다.');
						return false;
					}

					if (addOptionList[index].quantity <= 1) {
						$s.alert('답례품품품을 1개 이상 선택해주세요.');
						return false;
					}
				} else if (target === 'plus') {
					if (vm.item.itemOptionFlag === 'Y') {
						optionName = '<' + addOptionList[index].optionName + '>의 ';
						stockFlag = addOptionList[index].optionStockFlag;
						stockQuantity = addOptionList[index].optionStockQuantity;
					}

					if (stockFlag === 'Y' && stockQuantity >= 0 && addOptionList[index].quantity >= stockQuantity) {
						$s.alert(optionName + '최대 구매 수량은 ' + stockQuantity + '개 입니다.');
						addOptionList[index].quantity = stockQuantity;
						return false;
					}

					if (vm.item.orderMaxQuantity <= addOptionList[index].quantity) {
						$s.alert('최대 구매 수량은 ' + vm.item.orderMaxQuantity + '개 입니다.');
						addOptionList[index].quantity = vm.item.orderMaxQuantity;
						return false;
					}
				} else if (target === 'manual') {
					if (vm.item.itemOptionFlag === 'Y') {
						optionName = '<' + addOptionList[index].optionName + '>의 ';
						stockFlag = addOptionList[index].optionStockFlag;
						stockQuantity = addOptionList[index].optionStockQuantity;
					}

					if (stockFlag === 'Y' && stockQuantity >= 0 && addOptionList[index].quantity > stockQuantity) {
						$s.alert(optionName + '최대 구매 수량은 ' + stockQuantity + '개 입니다.');
						addOptionList[index].quantity = stockQuantity;
						this.setRequiredItem(index);
						return false;
					}

					if (vm.item.orderMaxQuantity < addOptionList[index].quantity) {
						$s.alert('최대 구매 수량은 ' + vm.item.orderMaxQuantity + '개 입니다.');
						addOptionList[index].quantity = vm.item.orderMaxQuantity;
						this.setRequiredItem(index);
						return false;
					}

					if (vm.item.orderMinQuantity > addOptionList[index].quantity) {
						$s.alert('최소 구매 수량은 ' + vm.item.orderMinQuantity + '개 입니다.');
						addOptionList[index].quantity = vm.item.orderMinQuantity;
						vm.setRequiredItem(index);
						return false;
					}

					if (addOptionList[index].quantity < 1) {
						$s.alert('답례품을 1개 이상 선택해주세요.');
						addOptionList[index].quantity = 1;
						this.setRequiredItem(index);
						return false;
					}
				} else {
					$s.alert('처리할 수 없습니다.');
					return false;
				}
			}

			return true;
		},
		addOptionList: function (index, pIndex, level, optionId) {
			if (vm.item.itemSoldOutFlag === "Y") {
				return false;
			}

			let isSet = vm.item.itemType === "3";
			let item = isSet ? vm.item.itemSets[pIndex].item : vm.item;
			let itemOptions = item.itemOptions;
			let itemOptionInfo = vm.itemOptionInfo;
			let addOptionList = itemOptionInfo.addOptionList;
			let optionName = "";
			let optionPrice = itemOptionInfo.optionPrices[index];
			let itemPrice = item.presentPrice;

			if (item.itemOptionType === "S") {
				// 선택형
				optionName = item.itemOptions[0].optionName1 + ": " + itemOptionInfo.selectOptionName1;
			} else if (item.itemOptionType === "S2" || item.itemOptionType === "S3") {
				// S2
				optionName = item.itemOptionTitle1 + ": " + itemOptionInfo.selectOptionName1 + " | "
					+ item.itemOptionTitle2 + ": " + itemOptionInfo.selectOptionName2;

				if (item.itemOptionType === "S3") {
					// S3
					optionName += " | " + item.itemOptionTitle3 + ": " + itemOptionInfo.selectOptionName3;
				}
			}

			optionName += optionPrice > 0 ? " (+" + this.formatNumber(optionPrice) + "원)" : "";

			let addOption = {
				itemId: item.itemId,
				optionId: optionId,
				optionName: optionName,
				quantity: item.orderMinQuantity,
				optionPrice: (itemPrice + optionPrice) * item.orderMinQuantity,
				tempOptionPrice: itemPrice + optionPrice,
				optionStockFlag: item.itemOptions[index].optionStockFlag,
				optionStockQuantity: item.itemOptions[index].optionStockQuantity,
				soldOut: item.itemOptions[index].soldOut,
				baseOptionPrice: optionPrice
			}
			addOptionList.push(addOption);

			itemOptionInfo.optionName1 = [];
			itemOptionInfo.optionName2 = [];
			itemOptionInfo.optionName3 = [];

			for (let i = 0; i < itemOptions.length; i++) {
				itemOptionInfo.optionName1.push(itemOptions[i].optionName1);
				itemOptionInfo.optionName1Ids.push(itemOptions[i].itemOptionId);
			}

			if (isSet) {
				vm.itemSetInfo.amount += addOption.baseOptionPrice;

				// 옵션 정보 세팅 (설정된 재고는 옵션 고유 재고로 덮어씌움)
				vm.itemSetInfo.selectList[pIndex].itemOption = addOption;
				vm.itemSetInfo.selectList[pIndex].stockFlag = addOption.optionStockFlag;
				vm.itemSetInfo.selectList[pIndex].stockQuantity = addOption.optionStockQuantity;
			}
		},
		checkForNonmemberOrder: function () {
			var nonmemberOrderType = this.item.nonmemberOrderType;

			if (nonmemberOrderType != '1') {
				// if (this.userId <= 0 && nonmemberOrderType == '1') {
				var message = '회원만 구매가 가능합니다. 로그인 페이지로 이동하시겠습니까?';	// 회원만 구매가 가능합니다. 로그인 페이지로 이동하시겠습니까?
				if (confirm(message)) {
					$s.redirect($s.pages.LOGIN + "?target=" + encodeURIComponent($s.requestContext.requestFullUri));
				}
				return false;
			}
		},

		snsShare: function (target) {

			var title = this.item.itemName;
			var description = this.item.itemSummary;
			var imageUrl = this.itemImage(this.item.imageSrc);
			var link = this.item.eventViewUrl;

			if (target == 'facebook') {
				$s.api.social.facebook(title, description, imageUrl, link);
			} else if (target == 'twitter') {
				$s.api.social.twitter(title, description, imageUrl, link);
			} else if (target == 'kakao') {
				$s.api.social.kakao(title, description, imageUrl, link);
			} else if (target == 'kakaoStory') {
				$s.api.social.kakaoStory(title, description, imageUrl, link);
			} else if (target == 'naverBand') {
				$s.api.social.naverBand(title, description, imageUrl, link);
			} else if (target == 'urlCopy') {
				navigator.clipboard.writeText(link);
				$s.alert('링크가 복사되었습니다.');
			} else {
				$s.alert(target + '는 추가작업이 필요합니다.');
			}
		},

		timeFormat: function (date) {
			return date.replace(/(\d{2})(\d{2})(\d{2})/, '$1:$2:$3');
		},
		downloadCoupon: function (downloadTargetId, index) {
			var itemCoupons = vm.result.coupon.content;

			// 전체 쿠폰 일괄발급
			if (downloadTargetId == 'all') {

				var param = {
					itemUserCode: vm.itemUserCode
				}

				$s.api.downloadAllItemCouponList(param, function (response) {
					vm.paging(1, 'coupon');
					$s.alert('쿠폰 모두받기가 완료 되었습니다.');
				});

			} else {
				var param = {
					'couponId': downloadTargetId
				};

				$s.api.couponDownload(param, function (response) {
					if (response.status === 'OK') {
						itemCoupons.splice(index, 1);
					}

					$s.alert(response.message);
				});

				this.paging(this.param.coupon.page, 'coupon');
			}
		},
		registerQna: function () {
			this.itemQna.itemId = this.item.itemId;

			if ($s.isLogin()) {
				$s.api.createItemQna(this.itemQna, function (response) {
					if (response.status === 'OK') {
						$s.alert('답례품Q&A가 등록되었습니다.');
						vm.paging(vm.param.coupon.page, 'qna');

						vm.itemQna.qnaGroup = '';
						vm.itemQna.question = '';
						vm.itemQna.subject = '';
						vm.itemQna.secretFlag = 'N';
						vm.secretFlag = false;

						vm.event.showQnaForm = false;
					}
				}, function (error) {
					$s.alert(error.response.data.description);
				});
			} else {
				$s.alert('로그인이 필요 합니다.');
				return;
			}
		},
		telLink: function () {
			location.href = 'tel:' + this.seller.telephoneNumber;
		},
		getNewLatelyItems: function () {
			var lately = $s.core.getSession('lately_item');

			if (typeof lately == 'undefined' || lately == null) {
				this.latelyItems = [];
			} else {
				this.latelyItems = JSON.parse(lately);
			}

			this.newLatelyItemCount = this.latelyItems.length;
		},
		getCurrentCategories: function () {
			var breadcrumbs = vm.breadcrumbs[0];

			if (breadcrumbs !== undefined) {
				vm.breadcrumbs = breadcrumbs;
				vm.current.groupUrl = breadcrumbs.groupUrl;
				vm.current.groupName = breadcrumbs.groupName;
				vm.current.category1Url = breadcrumbs.breadcrumbCategories[0].categoryUrl;
				vm.current.category1Name = breadcrumbs.breadcrumbCategories[0].categoryName;
				vm.current.categoryLevel = "1";

				if (breadcrumbs.breadcrumbCategories[1] !== undefined) {
					vm.current.category2Url = breadcrumbs.breadcrumbCategories[1].categoryUrl;
					vm.current.category2Name = breadcrumbs.breadcrumbCategories[1].categoryName;
					vm.current.categoryLevel = "2";
				} else {
					vm.current.category2Name = "전체";
				}

				if (breadcrumbs.breadcrumbCategories[2] !== undefined) {
					vm.current.category3Url = breadcrumbs.breadcrumbCategories[2].categoryUrl;
					vm.current.category3Name = breadcrumbs.breadcrumbCategories[2].categoryName;
					vm.current.categoryLevel = "3";
				} else {
					vm.current.category3Name = "전체";
				}

				var currentCategories = [];

				currentCategories.push({
					'title': vm.current.groupName,
					'codes': vm.category.groups
				});

				currentCategories.push({
					'title': vm.current.category1Name,
					'codes': vm.childCategories(vm.current.groupUrl)
				});

				currentCategories.push({
					'title': vm.current.category2Name,
					'codes': vm.childCategories(vm.current.groupUrl, vm.current.category1Url)
				});

				if (vm.current.categoryLevel === '2' || vm.current.categoryLevel === '3') {
					currentCategories.push({
						'title': vm.current.category3Name,
						'codes': vm.childCategories(vm.current.groupUrl, vm.current.category1Url, vm.current.category2Url)
					});
				}

				var childCategories = vm.childCategories(breadcrumbs.groupUrl, breadcrumbs.categoryUrl1, breadcrumbs.categoryUrl2);
				vm.currentCategories = currentCategories;

				var categoryName = vm.currentCategories[vm.current.categoryLevel].title;
				var categories = vm.currentCategories[vm.current.categoryLevel].codes;
				var categoryCode = '';

				for (var i = 0; i < categories.length; i++) {
					var category = categories[i];

					if (category.categoryId == vm.breadcrumbs.categoryClass) {
						categoryCode = category.url;
					}
				}

				var currentChildForMobile = {
					categoryName: categoryName,
					categoryCode: vm.currentCategories[vm.current.categoryLevel].codes,
					categories: categories,
					childCategories: childCategories
				}
				vm.currentChildForMobile = currentChildForMobile;
			}
		},
		link: function (url, index) {
			if (index === 0) {
				$s.redirect('/category/?code=' + this.childCategories(url)[0].url);
			} else {
				$s.redirect('/category/?code=' + url);
			}
		},
		selectOptionTitle: function (level, pIndex) {
			let itemOptionInfo = vm.itemOptionInfo;

			if (level === 1) {
				// 초기화
				this.clearOptionItems();

				this.writeOptionName(0, '', 0, pIndex);

			} else if (level === 2) {
				itemOptionInfo.optionName3 = new Array();
				itemOptionInfo.optionName3Ids = new Array();
				itemOptionInfo.selectOptionName3 = '';
				itemOptionInfo.selectOptionIndex2 = '';
				itemOptionInfo.selectOptionIndex3 = '';

				this.writeOptionName(1, itemOptionInfo.selectOptionName1, itemOptionInfo.selectOptionIndex1, pIndex);
			}
		},
		getRestockNotice: function (itemId) {
			var param = {
				'itemId': itemId
			};
			$s.api.getRestockNotice(param, function (response) {
				vm.isRestockNotice = response.isRestockNotice;
			});
		},

		restockNotice: function () {
			if (vm.isRestockNotice) {
				$s.alert('이미 신청한 상태입니다.');
				return false;
			}

			if ($s.isLogin() === false) {
				$s.alert('로그인 후 이용이 가능합니다.', function () {
					$s.redirect($s.pages.LOGIN + '?target=' + encodeURIComponent($s.requestContext.requestFullUri));
				});
			} else {
				var param = {
					'itemId': vm.item.itemId
				};
				$s.api.restockNotice(param, function (response) {
					if (response.status === 'OK') {
						vm.isRestockNotice = true;
						$s.alert('재입고 알림을 신청했습니다.');
					}
				});
			}
		},

		addItemReviewLike: function (index) {

			var review = vm.result.review.content[index],
				likeCount = review.likeCount;

			$s.api.addItemReviewLike(review.itemReviewId, function (response) {

				var message = '';

				if (response.flag) {
					message = '해당 리뷰가 도움이 되었습니다.';
					review.likeCount = likeCount + 1;
				} else {
					message = '이미 도움을 주셨습니다.';
				}

				$s.alert(message);
			});
		},
		selectReviewOrderBy: function (e) {

			var value = e.target.value;

			this.param.review.orderBy = value;
			this.paging(1, 'review');
		},
		isDisplayReviewInfo: function (index) {

			var review = vm.result.review.content[index];

			return review.filters.length > 0 || (review.displayOptionsFlag && review.options.length > 0);
		},

		validFilter: function (groupId, codeId) {
			var group = this.getFilterGroup(groupId);

			if (group == null) {
				return false;
			}

			var code = this.getFilterCode(groupId, codeId);

			if (code == null) {
				return false;
			}

			return true;
		},

		getFilterGroup: function (groupId) {

			var groups = this.reviewFilters;

			if (groups != null && groups.length > 0) {

				for (var i = 0; i < groups.length; i++) {

					if (groupId == groups[i].id) {
						return groups[i];
					}

				}
			}

			return null;
		},

		getFilterCode: function (groupId, codeId) {

			var group = this.getFilterGroup(groupId);

			if (group == null) {
				return null;
			}

			var codes = group.codes;

			if (codes != null && codes.length > 0) {

				for (var i = 0; i < codes.length; i++) {

					if (codeId == codes[i].id) {
						return codes[i];
					}

				}
			}

			return null
		},

		getFilterGroupName: function (groupId) {

			var group = this.getFilterGroup(groupId);

			if (group != null) {
				return this.unescapeHtml(group.label);
			}

			return '';
		},
		getFilterCodeName: function (groupId, codeId) {

			var code = this.getFilterCode(groupId, codeId);

			if (code != null) {
				return this.unescapeHtml(code.label);
			}

			return '';
		},

		openReviewFilter: function () {
			var filters = this.param.filter.initFilterCodeIds;
			this.param.filter.filterCodeIds = filters;
		},
		closeReviewFilter: function () {
			$('.pop_rv_myfilter').modal("hide");
		},
		applyReviewFilter: function () {
			var filters = this.param.filter.filterCodeIds;
			this.param.filter.initFilterCodeIds = filters;

			this.param.review.fcIds = filters.join('||');
			this.paging(1, 'review');
			this.closeReviewFilter();
		},
		clearReviewFilter: function () {
			this.param.filter.filterCodeIds = [];
			this.param.filter.initFilterCodeIds = [];
			this.param.review.fcIds = '';
		},
		toSlideViewImage: function (index) {
			alert(index);
			this.viewImageSwiper.slideTo(index);
		},
		showItemReview: function (review) {
			if (vm.event.itemReviewId > 0 && vm.event.itemReviewId === review.itemReviewId) vm.event.itemReviewId = 0;
			else vm.event.itemReviewId = review.itemReviewId;
		},
		showItemQna: function (qna) {
			if ((vm.event.qnaId > 0 && vm.event.qnaId === qna.qnaId)
				|| (qna.secretFlag === 'Y' && qna.userId !== vm.userId)) {
				vm.event.qnaId = 0;
			}
			else vm.event.qnaId = qna.qnaId;
		}
	},
	watch: {
		secretFlag: function (value) {
			var result = 'N';
			if (value) {
				result = 'Y';
			}
			this.itemQna.secretFlag = result;
		},
	},
	computed: {
		ratingStarHtml: function () {
			var maxScore = 5,
				ratingScore = Math.ceil(Number(this.item.reviewScore)),
				// ratingScore = Math.ceil(4.8);
				// console.log(`${ratingScore}`);
				subScore = maxScore - ratingScore;

			if (ratingScore > maxScore) ratingScore = maxScore;

			var ratingStarHtml = '';

			for (var i = 0; i < ratingScore; i++) {
				ratingStarHtml += "\<span class='on'\>\<\/span\>";
			}

			if (subScore > 0) {
				for (var i = 0; i < subScore; i++) {
					ratingStarHtml += "\<span\>\<\/span\>";
				}
			}
			return ratingStarHtml;
		},
		ratingStarScore: function () {
			var maxScore = 5,
				score = Number(this.item.reviewScore).toFixed(1);
			if (score > maxScore) score = maxScore;

			return score;
		},
		total: function () {

			var itemPrice = this.item.presentPrice;

			return this.display.quantity * itemPrice;
		},
		setItemTotal: function () {
			var itemSets = this.param.order.itemSets;
			var sum = 0;
			if (typeof itemSets !== 'undefined') {
				for (var i = 0; i < itemSets.length; i++) {
					sum += itemSets[i].amount;
				}
			}
			return sum;
		},
		optionItemTotal: function () {
			var addOptionList = this.itemOptionInfo.addOptionList;
			var sum = 0;
			if (typeof addOptionList !== 'undefined') {
				for (var i = 0; i < addOptionList.length; i++) {
					sum += addOptionList[i].optionPrice;
				}
			}
			return sum;
		},
		pointInfoText: function () {

			var earnPoint = this.earnPoint;
			var text = '';

			if (typeof earnPoint != 'undefined') {

				var pointText = '';
				var levelPointText = '';

				if (earnPoint.point > 0 && earnPoint.pointRate > 0) {
					pointText = '기본적립 ' + earnPoint.pointRate + '%';
				}
				if (earnPoint.levelPoint > 0 && earnPoint.levelPoint > 0) {
					levelPointText = earnPoint.levelName + '회원 추가적립 ' + earnPoint.levelPointRate + '%';
				}

				/*text += (earnPoint.pointRate + earnPoint.levelPointRate) + '% ';*/

				text += this.formatNumber(earnPoint.totalPoint) + 'P 적립 ';

				text += '(';

				if (pointText != '' && levelPointText != '') {
					text += pointText + ' + ' + levelPointText;
				} else if (pointText == '' && levelPointText != '') {
					text += levelPointText;
				} else if (pointText != '' && levelPointText == '') {
					text += pointText;
				}

				text += ')';
			}
			return text;
		},
		viewImageSwiper() {
			return this.$refs.awesomeViewImageSwiper.swiper
		},
		viewThumbsSwiper() {
			return this.$refs.awesomeViewThumbsSwiper.swiper
		},
		wishlistClass: function () {
			return 'btn btn_save' + (this.item.wishlistFlag ? ' on' : '');
		},
		makeShipping: function () {

			var item = this.item;
			var shippingType = item.shippingType;

			if (shippingType == '1') {
				return '무료배송'
			}

			if (shippingType == '2' || shippingType == '3' || shippingType == '4') {

				var baseText = this.formatNumber(item.shipping) + '원';
				var freeText = '무료배송';
				if (item.shippingFreeAmount > 0) {
					freeText = this.formatNumber(item.shippingFreeAmount) + '원이상 무료배송';
				}

				return baseText + ' (' + freeText + ')';
			}

			if (shippingType == '5') {
				return this.formatNumber(item.shippingItemCount) + '개당 ' + this.formatNumber(item.shipping) + '원';
			}

			if (shippingType == '6') {
				return this.formatNumber(item.shipping) + '원'
			}

			return '';
		}
	},
	created: function () {
		try {
			var head = document.getElementsByTagName('head')[0];

			if (typeof url != 'undefined' && url != '') {
				head.appendChild(getMeta('og:url', url));
			}

			if (typeof title != 'undefined' && title != '') {
				head.appendChild(getMeta('og:title', this.unescapeHtml(title)));
			}

			if (typeof image != 'undefined' && image != '' && image != $s.config.noImage) {
				head.appendChild(getMeta('og:image', image));
			}

			if (typeof description != 'undefined' && description != '') {
				head.appendChild(getMeta('og:description', this.unescapeHtml(description)));
			}
		} catch (e) {
			$s.error(e);
		}

		function getMeta(name, content) {
			var e = document.createElement('meta');

			e.name = name;
			e.content = content;

			return e;
		}
	},
	mounted: function () {
		this.$nextTick(function () {
			// Saleson.init();

			// scroll Fixed Header
			window.onscroll = function () { myFunction() };

			var itemTab = document.getElementById("item_tab_wrap");
			var sticky = itemTab.offsetTop - 30;

			function myFunction() {
				// if (window.pageYOffset >= sticky || window.pageYOffset >= 0) {
				if (window.pageYOffset >= sticky) {
					itemTab.classList.add("sticky");
				} else {
					itemTab.classList.remove("sticky");
				}
			}

			$(".nav-item a").click(function(){
				$(this).addClass('active').append('<span class="sr-only">선택됨</span>').parent().siblings().children('a').removeClass('active').find(".sr-only").remove();
			});

			/*$s.core.removeSession($s.const.KAKAO_SHARE_INIT_FLAG);

			this.pageUrl = $s.requestContext.href;
			var code = $s.core.getParameter('code');
			if (code == '') {
				$s.notFoundException();
				return;
			}

			this.itemUserCode = code;

			this.categoryInfo(this);
			this.deleteOption(0);
			$s.api.getItem(code, function (response) {
				var item = response.item;
				vm.item = item;
				vm.list = response.list;
				vm.pointPolicy = response.pointPolicy;
				vm.config = response.config;
				vm.cardBenefits = response.cardBenefits;
				vm.seller = response.seller;
				vm.userId = response.userId;
				vm.breadcrumbs = response.breadcrumbs;
				vm.isRestockNotice = response.isRestockNotice;
				vm.earnPoint = response.earnPoint;
				vm.reviewFilters = response.reviewFilters;
				vm.qnaGroups = response.qnaGroups;

				var slides = new Array();

				for (var i = 0; i < item.itemImages.length; i++) {
					var slide = {
						imageId: item.itemImages[i].itemImageId,
						image: item.itemImages[i].imageSrc
					};

					slides.push(slide);
				}

				vm.slides = slides;

				// 최대 & 최소 구매수량 조정
				if (item.orderMinQuantity === -1) item.orderMinQuantity = 1;
				if (item.orderMaxQuantity === -1) item.orderMaxQuantity = 999;

				vm.display.quantity = item.orderMinQuantity;

				// 세트답례품 분기 처리
				var isSet = item.itemType === '3';
				if (isSet) {
					if (item.itemSoldOutFlag === "N") {
						vm.setRequiredItemSets();
						if (item.itemOptionFlag === "N") vm.addItemSets();
					}
				} else {
					if (item.itemOptionFlag === "N" && item.itemSoldOutFlag === "N") {
						vm.setRequiredItem(0);
					}

					vm.writeOptionName();
				}

				vm.addlatelyItem(item.itemId);
				vm.getNewLatelyItems();
				vm.getCurrentCategories();
				vm.getRestockNotice(item.itemId);

				vm.makeOpenGraphTag($s.requestContext.href, item.itemName, vm.itemImage(item.imageSrc), item.itemSummary);
				// vm.setPageInfo(item.itemName, item.itemSummary, '');

				$s.ga.detail(item.itemUserCode);
				$s.ev.log.item(item.itemUserCode);
			}, function (error) {
				if ('BAD_REQUEST_NO_ITEM' == error.response.data.code) {
					$s.alert(error.response.data.message, function () {
						$s.redirect($s.pages.INDEX);
					});
					// modal alert 바깥영역 클릭해도 이전페이지로 이동
					$('.items_detail_wrap').on('click', '#op-alert', function () {
						history.back();
					});
				}
			});

			this.getItemRelations();
			this.paging(1, 'all');*/
		});
	},
};
</script>
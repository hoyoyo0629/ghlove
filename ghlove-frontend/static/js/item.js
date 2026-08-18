var mlistCategory = null;

/*$(document).ready(function(){
	if(0 < $(".item_contents .menu_depth2").length){
		itemCategory();
	}
});*/

// -------------------------------------------------- 상품
function initializeItemsEvent() {



	// 상품리스트 카테고리
	/*var windowWidth = $(window).innerWidth();
	if ($('.item_category_m .container').length > 0) {
		if (mlistCategory != null) {
			mlistCategory.update();
		} else {
			mlistCategory = new Swiper('.item_category_m .container', {
				slidesPerView: 'auto',
				spaceBetween: 0,
				freeMode: true
			});
		}
	}
*/
	var windowWidth = $(window).innerWidth();

	$(window).on('load resize', function () {
		if (windowWidth < 992) {
			if ($('.item_category_m').length > 0) {
				if (mlistCategory != null) {
					mlistCategory.update();
				} else {
					mlistCategory = new Swiper('.item_category_m', {
						slidesPerView: 'auto',
						resistance: false,
						// resistanceRatio: 0,
						freeMode: true
					});
				}
			}
		}
	});

	window.onload = () => {
		// 상품 구매하기 - pc
		var detailViewWrap = document.querySelector(".detail_view_wrap");

		detailViewWrap.querySelector(".dropdown_box .custom-select").addEventListener("click",function(e) {
			// $('.type_option').addClass('on');
			detailViewWrap.querySelector(".option_drop").style.display = "none";

			if (this.nextElementSibling.style.display === "none") {
				this.nextElementSibling.style.display = "block";
			}
		});

		detailViewWrap.querySelector(".drop_close").addEventListener("click", function(e) {
			detailViewWrap.querySelector(".option_drop").style.display = "none";
		});

		// 상품 구매하기 - mobile
		var btnBuy = document.querySelector(".view_info .btn_area1 .btn_buy");
		var btnOpen = document.querySelector(".view_info .btn_open");
		var viewBg = document.querySelector(".viewBg");
		var optionArea = document.querySelector('.option_area');

		btnBuy.addEventListener("click", optionAreaEvent);
		btnOpen.addEventListener("click", optionAreaEvent);
		viewBg.addEventListener("click", optionAreaEvent);

		function optionAreaEvent() {
			if (optionArea.classList.contains("on")) {
				optionArea.classList.remove("on");

				document.querySelector("html").classList.remove('scroll_none');
				document.querySelector("body").classList.remove('scroll_none');
				viewBg.style.display = "none";
			} else {
				optionArea.classList.add("on");

				document.querySelector("html").classList.add('scroll_none');
				document.querySelector("body").classList.add('scroll_none');
				viewBg.style.display = "block";
			}
		}

		optionArea.querySelector(".dropdown_box .custom-select").addEventListener("click", function(e) {
			optionArea.querySelector(".option_drop").style.display = "none";

			if (this.nextElementSibling.style.display === "none") {
				this.nextElementSibling.style.display = "block";
			}
		});

		optionArea.querySelector(".drop_close").addEventListener("click", event => {
			optionArea.querySelector(".option_drop").style.display = "none";
		});
	}

	// 상품 구매하기 - pc
	/*$('.option_open').off('click').on('click',function(){
		$(this).toggleClass('on');
		$('.option_view').stop().slideToggle();
	});
	$('.option_layer').off('click').on('click', '.btn_option_close', function(){
		$('.option_view').stop().slideUp();
		$('.option_open').removeClass('on');
	});*/


	// 상품옵션 - pc
	/*$('.option_layer').off('click').on('click','.dropdown_box .custom-select',function(){
		$('.dropdown_box .custom-select').show();
		$(this).hide();

		if(!$(this).next('.option_drop').is(':visible')){
			$(this).next('.option_drop').show();
		}
	});
	$('.option_layer').off('click').on('click', '.drop_close', function(){
		$('.option_layer .custom-select').show();
		$('.option_layer .option_drop').hide();
	});
*/

	//상품상세 상품옵션
	function fnScroll(){
		var winScroll = $(window).scrollTop();

		if($(".detailArea").index() != -1){
			var orderPosition = $(".orderBtnArea").offset().top;
			if(orderPosition <= winScroll){
				optionLayer.show();
			}else{
				optionLayer.hide();
			}
			if(tabPosition <= winScroll){
				detailTab.css({
					position: 'fixed',
					top: '0px',
					left: '50%',
					width: tabWidth+ 'px',
					marginLeft: -(tabWidth/2) + 'px',
					zIndex: 900
				});
				$("#divGoodsEssentialInfo").css({
					paddingTop: detailTab.height()
				});
			}else{
				detailTab.css({
					position: 'relative',
					top: '0px',
					left: '0px',
					width: tabWidth+ 'px',
					marginLeft: '0px',
					zIndex: 1
				});
				$("#divGoodsEssentialInfo").css({
					paddingTop: ''
				});
			}


			for(var i=(tabTotal-1); i>=0; i--){
				if(winScroll >= $(".detailInfo:eq("+i+")").offset().top - 5){
					tabM.removeClass('active');
					tabM.eq(i).addClass('active');
					break;
				};
			};
		}

	};

	$(window).scroll(function(){
		fnScroll();
	});

	$(window).on('resize', function () {
		var width = window.innerWidth;

		// 리사이즈시 스크롤막기 해제
		if (width >= 1200 && $('.view_info .option_area').hasClass('on')) {
			$('html, body').removeClass('scroll_none');

		}
	})


	// 상품후기 - 숨김영역열기
	$(document).on('click', '.review_list .review_open', function() {
		if($(this).closest(".list_area").is(".on")){
			$(this).closest(".list_area").removeClass("on");
			$(this).closest(".list_area").find(".rv_my_info").stop().slideUp(400);
			$(this).closest(".list_area").find(".review_img").stop().slideUp(400);
			$(this).closest(".list_area").find(".rv_comment").stop().slideUp(400);
		} else {
			$(this).closest(".list_area").addClass("on");
			$(this).closest(".list_area").find(".rv_my_info").stop().slideDown(400);
			$(this).closest(".list_area").find(".review_img").stop().slideDown(400);
			$(this).closest(".list_area").find(".rv_comment").stop().slideDown(400);
		}
	});
	/*$('.review_list').off("click").on('click', 'li .review_open', function (e) {

		var check = $(this).hasClass('on');

		$('.review_list .list_area').removeClass('on');
		$('.review_list .review_open').removeClass('on');
		$('.review_list .hide_area').hide();

		if(!check){
			$(this).addClass('on');
			$(this).parents('.list_area').addClass('on');
			$(this).next('.hide_area').show();
		}
	})*/

	// 전체조회
	/*$('.total_top').on('click', '.btn_inquiry', function () {
		$(this).parents('.total_area').next('.inquiry_write').show();
	});
	$('.total_top').on('click', '.btn_cancel', function () {
		$(this).parents('.inquiry_write').hide();
	});*/

	// 리스트공통 - 숨김영역열기
	$('.faq_list').off("click").on('click', '.list_open', function (e) {

		var check = $(this).hasClass('on');

		$('.list_wrap .list_open').removeClass('on');
		$('.list_wrap .list_open .hide_area').hide();

		if(!check){
			$(this).addClass('on');
			$(this).children('.list_in').children('.hide_area').show();
		}
	});
	// 필터 고정
	$(window).scroll(function(){
		var height = $(document).scrollTop(); //실시간으로 스크롤의 높이를 측정
		if(height > 55){
			$('.item_contents .filter_container').addClass('fixed_filter');
		}else if(height == 0){
			$('.item_contents .filter_container').removeClass('fixed_filter');
		}
	});
	//pc 필터
	$('.item_contents').on('click','.btn_filter',function(){
		$('.container').toggleClass('filter_open_pc');
		$('.bg_dim').hide();
		$('.filter_wrap').removeClass('trans_none');
	});
	//mobile 필터
	$('.item_contents').on('click', '.btn_filter', function(){
		
		$('.item_contents').addClass('filter_open_m');
		$('.filter_open_m .bg_dim').show();
	});
	$('.item_contents').on('click', '.bg_dim', function(){
		
		$('.item_contents').removeClass('filter_open_m');
		$(this).hide();
	});

	//드루 필터 
	if(0 < $(".item_filter_wrap").length) {
		$(window).on("scroll", function () {
			var wd_Top = $(window).scrollTop();
			var wd_Btm = $('body').height() - $(window).height() - wd_Top;
			var itemFw_Top = $('.item_filter_wrap').offset().top;
			//console.log(wd_Top + '/' + itemFw_Top + '/' + wd_Btm);

			if ((itemFw_Top - 100) <= wd_Top && 0 < (wd_Btm - 390)) {
				// .item_list_wrap 하위의 높이가 필터높이보다 작을때
				if ($('.item_list_wrap > div').innerHeight() <= $('.filter_wrap .filter_trans').innerHeight()) {
					//console.log('active_false');
				} else {
					$('.pc_item .item_head').addClass('active');
					$('.item_filter_wrap .sort_area').addClass('active');
					$('.item_filter_wrap .filter_wrap').addClass('active');
					$('.filter_wrap').addClass('trans_none');
					//console.log('active_true');
				}
				//console.log("ok")
			} else {
				$('.item_head').removeClass('active');
				$('.sort_area').removeClass('active');
				$('.filter_wrap').removeClass('active');
				$('.filter_wrap').removeClass('trans_none');
				//console.log("ddd");
			}
		});
	}


	//ie 말줄임
	$(".item_list .title > a").each(function() {
		var listTit_H = $(this).innerHeight();
		var listTit_maxH = $(this).css("max-height").replace("px", "");
		

		if (listTit_maxH == listTit_H) {
			$(this).addClass("multi");
		} else {
			$(this).removeClass("multi");
		}
	});
	/*$(window).on("scroll",function(){
		if($(this).scrollTop() >= 54) {
			$('.item_contents .item_category2').addClass('fixed');
		} else {
			$('.item_contents .item_category2').removeClass('fixed');
		}
	});*/
	/*$(window).on("scroll",function(){
		if($(this).scrollTop() >= 57) {
			$('.item_category2 .item_dropdown.tablet').addClass('fixed');
		} else {
			$('.item_category2 .item_dropdown.tablet').removeClass('fixed');
		}
	});*/
	//mobile 스크롤
	$(window).on("scroll",function(){
		if($(this).scrollTop() >= 54) {
			$('.mobile_item .item_head_m').addClass('active');
		} else {
			$('.item_head_m').removeClass('active');
		}
	});

	$('.item_contents').on('click', '.filter_content li > a', function (e) {
		e.preventDefault();

		$(this).toggleClass('on');
		$(this).siblings('.filter_lists').stop().slideToggle();
	});


/*
	// 페이지 로드시, 클래스 초기선언
	$(window).on("load", function() {
		// 2차 카테고리있을경우, off 클래스 추가
		$(".item_contents .menu_depth2").parent().find("> a").addClass("off");
		// 현재 페이지의 2차메뉴는 '-' & 펼침상태
		if ($(".item_contents .menu_depth1 > li > a.now").parent().find(".menu_depth2").length > 0) {
			$(".item_contents .menu_depth1 > li > a.now").addClass("on");
			$(".item_contents .menu_depth1 > li > a.now").parent().find(".menu_depth2").stop().slideDown();
		}
	});
*/

	$(".item_contents").on("click", ".menu_depth1 > li > a", function(e){
		if ($(this).parent().find(".menu_depth2").length > 0){
			e.preventDefault();
			if ($(this).hasClass("on")){
				$(this).removeClass("on");
				$(this).addClass("off");
				$(this).parent().find(".menu_depth2").stop().slideUp();
			} else {
				$(".menu_depth1 > li > a").removeClass("on");
				$(".menu_depth2").stop().slideUp();
				$(this).addClass("on");
				$(this).parent().find(".menu_depth2").stop().slideDown();
			}
		} else {
		}
	});


	//필터 부분이 컨텐츠 영역이 끝나는 지점에서 css fixed 해제
	var contentFooter = $('.footer_wrap'),
		contentsSide = $('.item_filter_wrap');

	var newHeaderSticky = function(){
		var windowWrap = $(window).scrollTop(),
			filterWrap = $('.item_filter_wrap').offset().top, // 필터 위치
			scrollBottom = $(document).height() - $(windowWrap).height() - contentFooter.height(); // 푸터 위치

		if (windowWrap >= filterWrap) {
			contentsSide.addClass('active');
			contentsSide.removeClass('fixedBottom');
			if (windowWrap > scrollBottom) {
				contentsSide.addClass('fixedBottom');
				contentsSide.removeClass('active');
			}
		} else {
			contentsSide.removeClass('active');
			contentsSide.removeClass('fixedBottom');
		}
	}

	//네이버페이가 있는 옵션 부분
	if(0 < $(".items_detail_wrap").length){
		$(window).on("load resize", function () {
			/*var count = 0;*/
			var winW = $(window).width();
			if (winW < 768) {
				$(".detail_view_wrap").removeClass("wfix");
				$(".detail_view_wrap .view_wrap").removeClass("row");
				$(".detail_view_wrap .view_wrap > div").removeClass("col");
			} else {
				$(window).on("scroll", function(){
					var wd_Top = $(window).scrollTop();
					var detCt_Top = $('.detail_contents .item_tab').offset().top;
					//var detVw_H = $(".detail_view_wrap").innerHeight();

					if ((detCt_Top - 100) <= wd_Top){
						$(".detail_view_wrap").addClass("wfix");
						$(".detail_view_wrap .view_wrap").addClass("row");
						$(".detail_view_wrap .view_wrap > div").addClass("col");

						var ddd = $(".detail_view_wrap.wfix").innerHeight();
						$(".detail_view_wrap.wfix").css("bottom", -(ddd + 4));
						/*if(count <= 0){
                            $("html, body").animate({scrollTop: (detCt_Top - 100)}, 400);
                        }

                        count += 1;
                        console.log(count);*/

					} else {
						$(".detail_view_wrap").removeClass("wfix");
						$(".detail_view_wrap .view_wrap").removeClass("row");
						$(".detail_view_wrap .view_wrap > div").removeClass("col");

						/*if(count < -1) {
                            count -= 1;
                        }
                        console.log(count);*/
					}
				});
			}
		});

	}


	/*function Pgsc() {
		$("html, body").animate({scrollTop: (detCt_Top - 100)}, 100);
	}*/


	$(document).on('click', '.detail_view_wrap .btn_open2', function(e) {
		e.preventDefault();

		if($(this).is(".view")){
			$(this).removeClass("view");
			$(this).parent().removeClass("view");
		} else {
			$(this).addClass("view");
			$(this).parent().addClass("view");
		}
	});

	// 할인혜택 Tooltip - 200812 추가
	$(".view_info .sale_area").on("mouseenter click", ".ttip_btn", function(){
		$(this).find(".price_ttip").stop().fadeIn(400);
	}).on("mouseleave", ".ttip_btn", function(){
		$(this).find(".price_ttip").stop().fadeOut(400);
	});

	// 할인혜택 Tooltip - 200812 추가
	$(".view_info .sale_area").on("mouseenter click", ".ttip_btn", function(){
		$(this).find(".price_ttip").stop().fadeIn(400);
	}).on("mouseleave", ".ttip_btn", function(){
		$(this).find(".price_ttip").stop().fadeOut(400);
	});


}
function itemCategory(){
	// 2차 카테고리있을경우, off 클래스 추가
	$(".item_contents .menu_depth2").parent().find("> a").addClass("off");
	// 현재 페이지의 2차메뉴는 '-' & 펼침상태
	if ($(".item_contents .menu_depth1 > li > a.now").parent().find(".menu_depth2").length > 0) {
		$(".item_contents .menu_depth1 > li > a.now").addClass("on");
		$(".item_contents .menu_depth1 > li > a.now").parent().find(".menu_depth2").stop().slideDown();
	}
}
function itemDetail(){
	/*var count = 0;*/
	var winW = $(window).width();
	if (winW < 768) {
		$(".detail_view_wrap").removeClass("wfix");
		$(".detail_view_wrap .view_wrap").removeClass("row");
		$(".detail_view_wrap .view_wrap > div").removeClass("col");
	} else {
		$(window).on("scroll", function(){
			var wd_Top = $(window).scrollTop();
			var detCt_Top = $('.detail_contents .item_tab').offset().top;
			//var detVw_H = $(".detail_view_wrap").innerHeight();

			if ((detCt_Top - 100) <= wd_Top){
				$(".detail_view_wrap").addClass("wfix");
				$(".detail_view_wrap .view_wrap").addClass("row");
				$(".detail_view_wrap .view_wrap > div").addClass("col");

				var ddd = $(".detail_view_wrap.wfix").innerHeight();
				$(".detail_view_wrap.wfix").css("bottom", -(ddd + 4));
				/*if(count <= 0){
					$("html, body").animate({scrollTop: (detCt_Top - 100)}, 400);
				}

				count += 1;
				console.log(count);*/

			} else {
				$(".detail_view_wrap").removeClass("wfix");
				$(".detail_view_wrap .view_wrap").removeClass("row");
				$(".detail_view_wrap .view_wrap > div").removeClass("col");

				/*if(count < -1) {
					count -= 1;
				}
				console.log(count);*/
			}
		});
	}

	// 세트상품 옵션박스
	$(document).on('click', '.set_box .set_btn', function(e) {
		e.preventDefault();
		if($(this).is(".open")){
			$(this).removeClass("open");
			$(this).next(".set_det").stop().slideUp(400);
		} else {
			$(this).addClass("open");
			$(this).next(".set_det").stop().slideDown(400);
		}
	});
}
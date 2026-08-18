$(function () {
	// initializeListEvent();		// 리스트스타일
})


$(document).ready(function(){
	initializeListEvent();
	textEllipsis();
});



function initializeListEvent() {
	// 전체조회
	$(document).on('click', '.total_top .btn_inquiry', function () {
		$(this).parents('.total_area').next('.inquiry_write').show();
	});
	$(document).on('click', '.total_top .btn_cancel', function () {
		$(this).parents('.inquiry_write').hide();
	});

	// 전체조회 - 검색조회 없을때
	$(document).on('click', '.item_qna .btn_inquiry', function () {
		$('.faq_list').addClass('on');
	});
	$(document).on('click', '.item_qna .btn_cancel', function () {
		$('.faq_list').removeClass('on');
	});


	// 리스트공통 - 숨김영역열기
	$('.list_wrap').on('click', '.list_open', function (e) {
		e.stopPropagation();

		var check = $(this).hasClass('on');

		$('.list_wrap .list_open').removeClass('on');
		$('.list_wrap .list_open .hide_area').hide();

		if(!check){
			$(this).addClass('on');
			$(this).children('.list_in').children('.hide_area').show();
		}
	});


}
function textEllipsis(){
	if(0 < $('.item_list').length) {
		if($('.item_list').is(".basic")){
			// Basic a태크 X
			$(".item_list.basic .title").each(function() {
				var listTit_H = $(this).find("span").innerHeight();
				var listTit_maxH = $(this).css("max-height").replace("px", "");

				if (listTit_maxH < listTit_H) {
					$(this).addClass("multi");
				}
			});
		} else {
			$(".item_list .title").each(function () {
				var listTit_H = $(this).find("a").innerHeight();
				var listTit_maxH = $(this).css("max-height").replace("px", "");

				if (listTit_maxH < listTit_H) {
					$(this).addClass("multi");
				}
			});
		}
	}

	//ie 말줄임
	if(0 < $('.feautred_contents').length){
		$(".feautred_contents .txt").each(function() {
			var listTit_H = $(this).find("> a > p").innerHeight();
			var listTit_maxH = $(this).find("a").css("max-height").replace("px", "");

			if (listTit_maxH < listTit_H) {
				$(this).addClass("multi");
			}
		});
	}
}
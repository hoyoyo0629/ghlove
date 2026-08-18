$(function () {
    initializeHeaderEvent();		// 헤더
})

function initializeHeaderEvent() {
    // 검색창
    $('#header').on('click', '.utile_menu .btn_search', function () {
        $('.header_search .search_area').show();
        $('html, body').addClass('scroll_none');
    });
    $('#header').on('click', '.header_search .search_close', function () {
        $('.header_search .search_area').hide();
        $('html, body').removeClass('scroll_none');
    });


    // gnb - 슬라이드
    var gnbSliderM = new Swiper('.gnb_slider_m', {
        slidesPerView: 'auto',
        spaceBetween: 0,
        resistance: true,
        resistanceRatio: 0,
        freeMode: true
    });
    var gnbSliderPC = new Swiper('.gnb_slider_pc', {
        slidesPerView: 'auto',
        spaceBetween: 0,
        resistance: true,
        resistanceRatio: 0,
        freeMode: true
    });


    // gnb - 메뉴열기
    $('#header').on('mouseenter', '.gnb .gnb_slider_pc .swiper-slide', function() {
        pcGnbHide();
        var index = $(this).index();

        $(this).children('.gnb_tit').addClass('on');
        $('.gnb .gnb_depth').eq(index).show();
    });


    // gnb - 메뉴닫기
    $('#header').on('mouseover', '.gnb .container', function(e) {
        var check = $(e.target).closest('.gnb_slider_pc .swiper-slide, .gnb .gnb_depth').length;

        if(!check) pcGnbHide();
    });
    $('#header').on('mouseleave', '.gnb .container', function(e){
        pcGnbHide();
    });

    function pcGnbHide() {
        if ($('.gnb .gnb_slider_pc .swiper-slide .gnb_tit.on').length > 0) {
            $('.gnb .gnb_slider_pc .gnb_tit').removeClass('on');
            $('.gnb .gnb_depth').hide();
        }
    }


    // gnb - 2차카테고리 아코디언
    $('#header').on('click', '.gnb .gnb_depth .depth2_tit', function () {
        var check = $(this).hasClass('on');

        $('.gnb .gnb_depth .depth2_tit').removeClass('on');
        $('.gnb .gnb_depth .depth3').stop().slideUp('fast');

        if(!check){
            $(this).addClass('on');
            $(this).next('.depth3').stop().slideDown();
        }
    });


    // gnb - 3차카테고리
    $('#header').on('mouseover', '.gnb .gnb_depth .depth3_area', function () {
        $(this).addClass('on');
        $(this).children('.depth4').show();
    }).on('mouseout', '.gnb .gnb_depth .depth3_area', function () {
        $('.gnb .gnb_depth .depth3_area').removeClass('on');
        $('.gnb .gnb_depth .depth4').hide()
    });
    $('#header').on('mouseover', '.gnb .gnb_depth .depth4 a', function () {
        $(this).addClass('on');
    }).on('mouseout', '.gnb .gnb_depth .depth3_area', function () {
        $('.gnb .gnb_depth .depth4 a').removeClass('on');
    });
}
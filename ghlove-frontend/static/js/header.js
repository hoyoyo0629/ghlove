
var gnbSlider;

function initializeJqueryEvent() {
    // 검색창
    $('#header').on('click', '.utile_menu .btn_search', function () {
        $('.header_search .search_area').show();
        $('html, body').addClass('scroll_none');
    });
    $('#header').on('click', '.header_search .search_close', function () {
        $('.header_search .search_area').hide();
        $('html, body').removeClass('scroll_none');
    });

    var gnbSliderM = new Swiper('.gnb_slider_m .swiper-container', {
        /*
        spaceBetween: 0,
        freeMode: true,*/
        slidesPerView: 'auto',
        /*navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },*/

    });

    // gnb - 메뉴열기
    $('#header').on('mouseenter', '.gnb .gnb_slider_pc .swiper-slide', function() {
        pcGnbHide();
        var index = $(this).index();

        $('.gnb_tit').eq(index).addClass('on');
        $('.gnb .gnb_depth').eq(index).show();
        $('.btn_close').addClass('block');
    });


    // gnb - 메뉴닫기
    $('#header').on('mouseover', '.gnb', function(e) {
        var check = $(e.target).closest('.gnb_slider_pc .swiper-slide, .gnb .gnb_depth').length;
        if(!check) pcGnbHide();
    });
    $('#header').on('mouseleave', '.gnb', function(e){
        pcGnbHide();
    });
    $('#header').on('click','.gnb_depth .tit_team',function(e){
        $('.gnb .btn_close').removeClass('block');
        $('.gnb .gnb_depth').hide();
    });
    $('#header').on('click','.gnb .btn_close',function(e){
        e.preventDefault();
        $(this).removeClass('block');
        $('.gnb .gnb_depth').hide();
    });
    $('#header').on('mouseover', '.gnb_slider_pc .btn_close', function(e) {
        $('.gnb .gnb_depth').addClass('block');
    });
    $('#header').on('mouseleave', '.gnb_slider_pc .btn_close', function(e){
        $('.gnb .gnb_depth').addClass('block');
    });
    function pcGnbHide() {
        if ($('.gnb .gnb_slider_pc .swiper-slide .gnb_tit.on').length > 0) {
            $('.gnb .gnb_slider_pc .gnb_tit').removeClass('on');
            $('.gnb .gnb_depth').hide();
            $('.gnb .btn_close').removeClass('block');
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

function initializeHeaderEvent() {
    var $slides = $(".gnb_slider_pc").find('.swiper-slide');
    var $firstSlide = $slides.eq(0);

    if ($firstSlide.length === 0) {
        setTimeout(function() {
            initializeHeaderEvent();
        }, 1000);
    } else {

        initializeJqueryEvent();

    }
}
$(function () {
    // initializeFooterEvent();		// 푸터
})

function initializeFooterEvent() {
    // 퀵메뉴
    $(window).on('load scroll', function () {
        var height = $(window).scrollTop();

        if (height < 100) {
            $('.float_menu_m .btn_top').fadeOut();
        } else {
            $('.float_menu_m .btn_top').fadeIn();
        }

        // 탑버튼 - 모바일
        // 퀵메뉴 - 피씨
        /*clearTimeout($.data(this, 'scrollCheck'));
        $.data(this, 'scrollCheck', setTimeout(function () {
            $('.float_menu_pc .menu_list').fadeOut();
        }, 3000));

        if (height > 50) {
            $('.float_menu_pc .menu_list').fadeIn();
        }*/
        $('.float_menu_pc').on('click','.btn_float_link',function(){
            $('.float_menu_pc').toggleClass('on');
        });
    });


    // 탑버튼
    $('.footer_wrap').on('click', '.btn_top', function () {
        $('html, body').animate({scrollTop: 0}, 500);
    });


    // gnb - 모바일
    $('.footer_wrap').on('click', '.float_menu_m .all-menu', function (e) {
        e.preventDefault();
        $('.footer_category').show();
        $('html, body').addClass('scroll_none');
    });

    $('.footer_wrap').on('click', '.layer_close', function (e) {
        e.preventDefault();
        $('.footer_category').hide();
        $('html, body').removeClass('scroll_none');
    });

    //style1 기존 차세대 스크립트
    /*$('.footer_wrap').on('click', '.accordion_depth1_tit', function (e) {
        e.preventDefault();

        $('.accordion_depth2_wrap').stop().slideUp();
        $('.accordion_depth1_tit').removeClass('on');

        if (!$(this).siblings('.accordion_depth2_wrap').is(':visible')) {
            $(this).addClass('on');
            $(this).siblings('.accordion_depth2_wrap').stop().slideDown();
        }
    });

    $('.footer_wrap').on('click', '.accordion_depth2_tit', function (e) {
        e.preventDefault();
        $('.accordion_depth2_tit').removeClass('on');
        $('.accordion_depth3_wrap').stop().slideUp();
        if (!$(this).next('.accordion_depth3_wrap').is(':visible')) {
            $(this).addClass('on');
            $(this).next('.accordion_depth3_wrap').stop().slideDown();
        }
    });*/
    //style1 footer2 드루 디자인 적용

    $('.footer2').on('click', '.mobile_menu .group_tit', function (e) {
        e.preventDefault();

        $('.mobile_menu .depth1').stop().slideUp();
        $('.group_tit').removeClass('on');

        if (!$(this).siblings('.mobile_menu .depth1').is(':visible')) {
            $(this).addClass('on');
            $(this).siblings('.mobile_menu .depth1').stop().slideDown();
        }
    });

    $('.footer2').on('click', '.mobile_menu .depth1 .off > .depth1_tit', function (e) {
        e.preventDefault();
        $('.mobile_menu .depth1 .off > a').removeClass('on');
        $('.mobile_menu .depth2').stop().slideUp();
        if (!$(this).next('.mobile_menu .depth2').is(':visible')) {
            $(this).addClass('on');
            $(this).next('.mobile_menu .depth2').stop().slideDown();
        }
    });
}
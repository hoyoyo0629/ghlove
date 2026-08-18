$(function () {
    initializeFooterEvent();		// 푸터
})

function initializeFooterEvent() {
    // 퀵메뉴
    $(window).on('load scroll', function () {
        var height = $(window).scrollTop();

        // 탑버튼 - 모바일
        if (height < 100) {
            $('.float_menu_m .btn_top').fadeOut();
        } else {
            $('.float_menu_m .btn_top').fadeIn();
        }

        // 퀵메뉴 - 피씨
        clearTimeout($.data(this, 'scrollCheck'));
        $.data(this, 'scrollCheck', setTimeout(function () {
            $('.float_menu_pc .menu_list').fadeOut();
        }, 3000));

        if (height > 50) {
            $('.float_menu_pc .menu_list').fadeIn();
        }
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

    $('.footer_wrap').on('click', '.accordion_depth1_tit', function (e) {
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
    });
}
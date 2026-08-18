$(function () {
   //initializeCustomerEvent();		// 고객센터
})

function initializeCustomerEvent() {
    // 이용후기 - 후기작성열기
    $(".review_list .review_open").off("click").on("click", function(){

        if($(this).closest(".list_area").is(".on")){
            $(this).closest(".list_area").removeClass("on");
            $(this).closest(".list_area").find(".rv_my_info").stop().slideUp(400);
            $(this).closest(".list_area").find(".review_img").stop().slideUp(400);
            $(this).closest(".list_area").find(".rv_btm").show();
            $(this).closest(".list_area").find(".rv_comment").stop().slideUp(400);
        } else {
            $(this).closest(".list_area").addClass("on");
            $(this).closest(".list_area").find(".rv_my_info").stop().slideDown(400);
            $(this).closest(".list_area").find(".review_img").stop().slideDown(400);
            $(this).closest(".list_area").find(".rv_btm").hide();
            $(this).closest(".list_area").find(".rv_comment").stop().slideDown(400);
        }
    });
    /*$('.review_list').on('click', '.btn_review', function () {
        var check = $(this).parents('.list_area').hasClass('on');

        $('.review_list .list_area').removeClass('on');
        $('.review_list .hide_area').hide();
        $('.review_list .btn_right').show();

        if(!check){
            $(this).parents('.list_area').addClass('on');
            $(this).parents('.btn_right').next('.hide_area').show();
            $(this).parents('.btn_right').hide();
        }
    });
    $('.review_list').on('click', '.btn_cancel', function () {
        $('.review_list .list_area').removeClass('on');
        $('.review_list .hide_area').hide();
        $('.review_list .btn_right').show();
    });*/
    
    
    // 공지사항 - 숨김영역열기
    $('.notice_list').on('click', '.list_area', function (e) {
        e.stopPropagation();

        var check = $(this).hasClass('on');

        $('.notice_list .list_area').removeClass('on');
        $('.notice_list .hide_area').hide();

        if(!check){
            $(this).addClass('on');
            $(this).children('.list_in').children('.hide_area').show();
        }
    });
}
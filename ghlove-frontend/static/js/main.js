var mainSlider = null;
var mdSlider = null;
var newSlider = null;
var bestSlider = null;

function initializeMainEvent() {

    // 베스트 - 탭
    $('.best_section').off('click').on('click', '.best_category .swiper-slide', function () {

        $('.best_category .swiper-slide').removeClass('on');
        $('.best_tab .tab_area').hide();

        $(this).addClass('on');
        var id = '#best_tab-'+$(this).data('group-url')
        $(id).find('.tab_area').show();

    });

    // 베스트 - 첫번째 탭
    $('.best_section').find('.best_category .swiper-slide').eq(0).addClass('on');

    // 룩북
    $(window).on("load resize", function(){
        $(".look_section .img_area li").each(function(index, item) {
            $(this).find("a").css("padding-top", $(this).innerHeight());

            // 이미지 비율 확인 : absolute라 값을 못가져옴
            //var img_w = $(this).find("a > p > img").naturalWidth;
            //var img_h = $(this).find("a > p > img").naturalHeight;
            var img_w = $(this).find("a > p > img").width;
            var img_h = $(this).find("a > p > img").height;
            //console.log(img_w + "/" + img_h);

            /*if(img_w < img_h){// 세로가 긴 형태
                $(this).addClass("verti");
            } else { // 가로가 긴 형태
                //$(this).addClass("hori");
            }*/
        });
    });
}
var STYLE_MASONRY = null;

$(function () {
    //initializeSubEvent();		// 이벤트
});

function initializeSubEvent() {
    // 스타일북
    STYLE_MASONRY = new Masonry('.look_wrap .img_inner', {
        itemSelector: '.look_wrap .img_area'
    });
}

function appendMasonry(element) {

    if (STYLE_MASONRY != null) {
        try {

            STYLE_MASONRY.appended(element);

            new imagesLoaded(STYLE_MASONRY.element, function() {
                STYLE_MASONRY.layout();
            });

        } catch (e) {
            $s.error('imagesLoaded error');
        }
    }
}

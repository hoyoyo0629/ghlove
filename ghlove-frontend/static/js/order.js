$(function () {
	//initializeOrderEvent();		// 주문
})

function initializeOrderEvent() {
	// 최종 결제금액
	$('.pay_wrap .counter').counterUp({
		delay: 50,
		time: 1000
	})
}
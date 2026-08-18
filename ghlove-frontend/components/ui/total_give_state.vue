<template>
	<!-- 총 기부금 -->
  <div class="donation">
    <div class="donation__header">
      <h3 class="donation__title">총 기부금</h3>
      <span class="donation__date">(전일 기준)</span>
    </div>
    <div class="donation__amount">
      <span class="amount__number"></span>
      <span class="amount__days"></span>
    </div>
    <div class="donation__progress">
      <div class="progress__bar">
        <div class="progress__fill" style="width: 0%">
          <div class="progress__mark"></div>
        </div>
      </div>
      <div class="progress__target">
        <span>전년 동기 대비</span>
        <strong>%</strong>
      </div>
    </div>
  </div>
  <!-- //총 기부금 -->
</template>

<script>
	module.exports = {
		methods: {
			// 총기부금 현황 바인딩 함수
			getGiveState: function() {
				$s.api.getGiveState({},function(response) {
					let res = response.data;
					let amt = Math.round(res.nowYearTotalAmt / 100) * 100;
					//$('.donation__date').html(`(전일 기준)`);
					$('.amount__number').html(`${amt.toLocaleString()}원`);
					//1월1일에만 안나오게
					if (Math.floor(res.dDay) == 0 ) {
						$('.amount__number').html(`0 원`);

					}
					if (Math.floor(res.dDay) > 0 ) {
						$('.amount__days').html(`D-${res.dDay}`);
					}
					//else if (Math.floor(res.dDay) > -1) {
						//$('.amount__days').html(`D-Day`);
					//}
					else {
						$('.amount__days').html(``);
					}
					//let percentage = Math.floor(res.nowYearTotalAmt / res.prevYearTotalAmt * 100) + '%';
					let percentage = 0;
					if (Math.floor(res.dDay) == 0 ) {
						percentage = Math.floor(0*0) + '%';
					} else {
						percentage = Math.floor(res.nowYearTotalAmt / res.prevYearTotalAmt * 100) + '%';
					}
					$('.progress__target strong').html(percentage);
					$('.progress__fill').css('width', `${res.nowDayPercent}%`);
				});
			},
		},
		mounted: function() {
			this.$nextTick(function () {
				this.getGiveState();
			});
		}
	};
</script>
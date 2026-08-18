<template>
  <!-- <div id="whitebg" v-if="isLoading"> -->
  <div id="do_whitebg" v-bind:style="setLoading">
    <div class="donation-re">
      <div class="animation_box">
        <img class="fix_img" src="/static/images/loading/d_box.png" alt="">
        <img class="ani_img" src="/static/images/loading/d_heart.png" alt="">
      </div>
      <!-- <h3 class="progress-title">기부해주셔서 감사드립니다.</h3>
      <p class="progress-subtitle">
        기부정보를<span class="pointblue"> 처리중</span>입니다. 잠시만 기다려주세요.
      </p> -->
      <!-- <p class="s-txt">결제 완료 후 아래 확인 버튼을 클릭 하시면 마이페이지에서 귀하의 기부 처리 현황을 확인하실수 있습니다.</p> -->
      <h3 class="progress-title">기부절차가 <span class="pointRed">진행중</span> 입니다.<br>잠시만 기다려주세요.</h3>
    </div>


  </div>
</template>
<style>
#do_whitebg {
  background-color: rgba(255, 255, 255, 0.9);
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 3999;
}

/* #do_whitebg img { */
#do_whitebg .donation-re {
  position: absolute;
  width: 100%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: var(--gray3);
}
#do_whitebg .donation-re h3{
  padding: 20px 0;
  color: var(--sub-color1);
}

.animation_box{
  height: 150px;
  position: relative;
}
.animation_box .fix_img{
  position: absolute;
  bottom:10px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
}
.animation_box .ani_img{
  position: absolute;
  top:10px;
  left: 50%;
  transform: translateX(-50%) scale(0.9);
  animation: moveani 1s 0.3s ease-in-out infinite;
}
@keyframes moveani{
  0%{ top:10px; transform: translateX(-50%) scale(0.9); opacity: 1;}
  80%{opacity: 1;}
  85%{ top:65px; transform: translateX(-50%) scale(0.5); opacity: 1;}
  100%{ top:65px; transform: translateX(-50%) scale(0.5); opacity: 0;}
}
</style>
<script>
module.exports = {
  props: {

  },
  data: function () {
    return {
      // 로딩 종료 시점에 
      isLoading: false,
      setLoading: "display:none;",
      //isLoading: true,
    };
  },
  components: {

  },
  methods: {
    showDonationLoading: function (isLoading) {
      if (isLoading) {
        this.setLoading = "display:block;";
      } else {
        this.setLoading = "display:none;";
      }
    },
  },

  computed: {

  },
  mounted: function () {
    this.$nextTick(function () {
      //Saleson.init();
      this.$emit('donationLoading-mounted');
    });
  }
}
</script>
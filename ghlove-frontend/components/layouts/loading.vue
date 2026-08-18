<template>
  <div id="whitebg" v-show="isLoading">
    <img src="/static/images/loading.gif" alt="통신 중">
  </div>
</template>
<style>
#whitebg {
  background-color: rgba(255, 255, 255, 0.8);
  position: fixed;
  top:0; left: 0;
  width: 100%;
  height: 100%;
  z-index: 3999;
}
#whitebg img{
  position: absolute;
  top:50%; left: 50%;
  transform: translate(-50%,-50%);
}
</style>
<script>
module.exports = {
  props: {

  },
  data: function () {
    return {
      // 로딩 종료 시점에 
      isLoading : false,
    };
  },
  components: {

  },
  methods: {
     showLoading: function (isLoading) {
     	this.isLoading = isLoading;
     },
  },

  computed: {
    
  },
  mounted: function () {
    this.$nextTick(function () {
      //Saleson.init();
      this.$emit('loading-mounted');
      $goods.setInputTypeNumber();      // input number 숫자 입력만 가능하도록 추가
    });
  }
}
</script>
<template>
  <div>
    <!-- <div class="page_title">
      <h2 class="sub_title">통계</h2>
    </div> -->
    <nav class="lnb">
      <div class="lnb-bar_2dep">
        <ul>
          <li v-for="(lnb2list, i) in lnb2depMenu" :key="i">
            <a :href="lnb2depMenu[i].dep2Url" :class="{'currentT': lnb2depMenu[i].dep2Url == menuUrl}">{{ lnb2depMenu[i].depLabel }}</a>
          </li>
        </ul>
       
      </div>
    </nav>
  </div>
</template>
<style>
.page_title{
  background-image: url('/static/images/subtitle-bg_statistics.png');
}
</style>
<script>
module.exports = {
  props: {
    menuUrl: {
      type: String,
      required: false,
      default: function() {
        return '';
      }
    }
  },
  data: function () {
    return {
      //************** FNB Data
      lnb2depMenu: [
                    {dep2Url : "/_pub/statistics/index.html", depLabel :"기부금운용계획" },
                    {dep2Url : "/", depLabel :"기부금운용현황" },
                    {dep2Url : "/", depLabel :"기부금모금현황" },
                    {dep2Url : "/", depLabel :"답례품구매현황" },
                    {dep2Url : "/", depLabel :"답례품선호도"},
                ],

    };
  },
  computed: {
  },
  methods: {
  },
  mounted: function () {
    
  },
};
</script>

<template>
  <div>
    <nav class="lnb" id="donationLnb">
      <div class="lnb-bar_2dep">
        <ul>
          <li v-for="(list, i) in lnb2depMenu" :key="i">
            <!--<a :href="lnb2depMenu[i].dep2Url" :class="{ 'currentT': lnb2depMenu[i].dep2Url == menuUrl }">{{
              lnb2depMenu[i].depLabel
            }}</a>-->
            <a href="javascript:void(0);" :class="{ 'currentT': lnb2depMenu[i].dep2Url == menuUrl }" v-if="lnb2depMenu[i].dep2Url == '/donation/donation.html'"
            	onclick="javascript:$s.donation.goDonationPage();">{{
              lnb2depMenu[i].depLabel
            }}</a>
            <a :href="lnb2depMenu[i].dep2Url" :class="{ 'currentT': lnb2depMenu[i].dep2Url == menuUrl }" v-if="lnb2depMenu[i].dep2Url == '/donation/list-select.html'">{{
              lnb2depMenu[i].depLabel
            }}</a>
            <a :href="lnb2depMenu[i].dep2Url" :class="{ 'currentT': lnb2depMenu[i].dep2Url == menuUrl }" v-if="lnb2depMenu[i].dep2Url == '/featured/eventList.html'">{{
              lnb2depMenu[i].depLabel
            }}</a>
          </li>
        </ul>

      </div>
    </nav>
  </div>
</template>
<style>
.page_title {
  background-image: url('/static/images/subtitle-bg_donation.png');
}

.lnb .lnb-bar_3dep a:hover {
  border-bottom: 2px solid rgba(255, 255, 255, 0.6);
  color: #fff;
}
</style>

<script>
module.exports = {
  props: {
    newCartQuantity: 0,
    menuUrl: {
      type: String,
      required: false,
      default: function () {
        return '';
      }
    }
  },
  data: function () {
    return {
      lnb2depMenu: [
        {
          dep2Url: "/donation/donation.html",
          depLabel: "기부하기",
        },
        {
          dep2Url: "/donation/list-select.html",
          depLabel: "기금사업 소개",
        },
        {
          dep2Url: "/featured/eventList.html",
          depLabel: "지역이벤트",
        },
      ],
    };
  },
  computed: {},
  methods: {
  },

  mounted: function () {
    this.$nextTick(function () {
      initializeHeaderEvent();
    });

    setTimeout(() => {
      	const span = document.createElement('span');
		span.innerHTML = "선택됨";
		span.classList.add("sr-only");
		document.querySelectorAll('#donationLnb ul li span').forEach((element) => {
			element.remove();
		});
		if(!!document.querySelector('#donationLnb ul li a.currentT')) {
			document.querySelector('#donationLnb ul li a.currentT').append(span);
		}
      }, 1000);

  },
};
</script>

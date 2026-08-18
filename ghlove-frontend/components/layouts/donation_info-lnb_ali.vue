<template>
    <div>
        <nav class="lnb" id="donationLnb">
            <div class="lnb-bar_2dep">
                <ul>
                    <li v-for="(list, i) in lnb2depMenu" :key="i">
                        <a :href="lnb2depMenu[i].dep2Url" :class="{ 'currentT': lnb2depMenu[i].dep2Url == menuUrl }">{{
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
                { dep2Url: "/donation/guide1.html", depLabel: "고향사랑기부제 안내" },
                { dep2Url: "/donation/guide2.html", depLabel: "온라인 기부절차 안내" },
                { dep2Url: "/donation/guide5.html", depLabel: "오프라인 기부절차 안내" },
                { dep2Url: "/donation/guide3.html", depLabel: "기부금 연말정산 세액공제 안내" },
                // { dep2Url: "/donation/guide4.html", depLabel: "종합소득세 확정신고 서비스 안내" },
                { dep2Url: "/donation/guide6.html", depLabel: "고향사랑기부 주의사항" },
            ],
        };
    },
    computed: {},
    methods: {
    },

    mounted: function () {
        this.$nextTick(function () {
            initializeHeaderEvent();
            const span = document.createElement('span');
			span.innerHTML = "선택됨";
			span.classList.add("sr-only");
			document.querySelector('#donationLnb ul li a.currentT').append(span);
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

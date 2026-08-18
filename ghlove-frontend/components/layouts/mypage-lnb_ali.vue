<template>
  <div>
    <!-- <div class="page_title">
      <h2 class="sub_title">마이페이지</h2>
    </div> -->
    <nav class="lnb">
      <div class="lnb-bar_2dep">
        <ul>
          <li v-for="(list, i) in lnb2depMenu" :key="i" :class="{ currentT: lnb2depMenu[i].pinUrl == menuUrl }">
            <a :href="list.dep2Url">{{ list.depLabel }}</a>
          </li>
        </ul>
      </div>

      <div class="lnb-bar_3dep" v-for="(lnb3list, i) in lnb2depMenu" :key="i" v-show="lnb2depMenu[i].lnb3depMenu != ''">
        <ul>
          <li v-for="(lnb3list, j) in lnb2depMenu[i].lnb3depMenu" :key="j">
            <a :href="lnb3list.dep3Url" :class="{ currentP: lnb3list.dep3Url == menuUrl }">{{ lnb3list.depLabel }}</a>
          </li>
        </ul>
      </div>
    </nav>
  </div>
</template>
<style>
.page_title {
  background-image: url("/static/images/subtitle-bg_mypage.png");
}

.lnb .lnb-bar_3dep a:hover {
  border-bottom: 2px solid rgba(255, 255, 255, 0.6);
  color: #fff;
}
</style>
<script>
// mounted 로 위치 이동
// $(document).ready(function () {
//   $(".lnb-bar_3dep:gt(0)").hide();
//   // $(".lnb-bar_2dep li").eq(0).addClass("currentT");

//   $(".lnb-bar_2dep li").click(function () {
//     $(this).addClass("currentT").siblings("li").removeClass("currentT");
//     let idx = $(this).index();
//     idx = idx;
//     console.log(idx);
//     if (0 <= idx && idx < 4) {
//       $(".lnb-bar_3dep").eq(idx).show().siblings(".lnb-bar_3dep").hide();
//     } else {
//       location.href = "/mypage/index.html";
//     }
//     // return false;
//   });

// });

module.exports = {
  props: {
    menuUrl: {
      type: String,
      required: false,
      default: function () {
        return "";
      },
    },
  },
  data: function () {
    return {
      //************** FNB Data
      lnb2depMenu: [
        {
          dep2Url: "/mypage/index.html",
          depLabel: "마이페이지 메인",
          lnb3depMenu: "",
        },
        {
          dep2Url: "#",
          depLabel: "기부",
          lnb3depMenu: [
            { dep3Url: "/mypage/cntrList.html", depLabel: "기부내역현황" },
            { dep3Url: "/mypage/cntrPoint.html", depLabel: "기부포인트현황" },
            { dep3Url: "/mypage/receiptList.html", depLabel: "기부확인증" },
            { dep3Url: "/mypage/honorList.html", depLabel: "기부혜택증(지자체별)" },
          ],
        },
        {
          dep2Url: "#",
          depLabel: "답례품",
          lnb3depMenu: [
            { dep3Url: "/mypage/orderList.html", depLabel: "주문조회" },
            { dep3Url: "/mypage/orderCancel.html", depLabel: "취소반품교환" },
            { dep3Url: "/mypage/review.html", depLabel: "답례품후기" },
            { dep3Url: "/mypage/inquiryItem.html", depLabel: "답례품Q＆A" },
            { dep3Url: "/mypage/deliveryInfo.html", depLabel: "배송지관리" },
          ],
        },
        {
          pinurl: "#",
          dep2Url: "#",
          depLabel: "관심정보",
          lnb3depMenu: [
            { dep3Url: "/mypage/intrstLocGov.html", depLabel: "관심지자체" },
            { dep3Url: "/mypage/favorItem.html", depLabel: "관심답례품" },
          ],
        },
        { pinUrl: "/users/modify.html", dep2Url: "/users/modify.html", depLabel: "회원정보수정", lnb3depMenu: "" },
        { pinUrl: "/mypage/inquiry.html", dep2Url: "/mypage/inquiry.html", depLabel: "1:1 문의", lnb3depMenu: "" },
        /*{ pinUrl: "/mypage/honorList.html", dep2Url: '/mypage/honorList.html', depLabel: "기부확인증", lnb3depMenu:'' },*/
      ],
      // lnb3depMenu: [
      //   [
      //     { dep3Url: "/mypage/cntrList.html", depLabel: "기부내역현황" },
      //     { dep3Url: "/mypage/cntrPoint.html", depLabel: "기부포인트현황" },
      //   ],
      //   [
      //     { dep3Url: "/mypage/orderList.html", depLabel: "주문조회" },
      //     { dep3Url: "/mypage/orderCancel.html", depLabel: "취소반품교환" },
      //     { dep3Url: "/mypage/review.html", depLabel: "답례품후기" },
      //     { dep3Url: "/mypage/inquiryItem.html", depLabel: "답례품Q＆A" },
      //     { dep3Url: "/mypage/deliveryInfo.html", depLabel: "배송지관리" },
      //   ],
      //   [
      //     { dep3Url: "/mypage/intrstLocGov.html", depLabel: "관심지자체" },
      //     { dep3Url: "/mypage/favorItem.html", depLabel: "관심답례품" },
      //   ],
      //   [
      //     { dep3Url: "/users/modify.html", depLabel: "회원정보수정" },
      //     { dep3Url: "/users/secede.html", depLabel: "회원탈퇴" },
      //     { dep3Url: "/users/modify.html", depLabel: "비번변경" }, //회원정보수정과 같은 URL사용
      //   ],
      // ]
    };
  },
  computed: {},
  methods: {},
  mounted: function () {
    this.$nextTick(() => {
      $(".lnb-bar_3dep:gt(0)").hide();

      $(".lnb-bar_2dep li").click(function () {
        $(this).addClass("currentT").siblings("li").removeClass("currentT");
        let idx = $(this).index();
        idx = idx;
        if (0 <= idx && idx < 4) {
          $(".lnb-bar_3dep").eq(idx).show().siblings(".lnb-bar_3dep").hide();
        } else {
          location.href = "/mypage/index.html";
        }
        // return false;
      });

      // 현재 선택 메뉴 활성화
      let length = this.lnb2depMenu.length;
      for (let i = 0; i < length; i++) {
        let dep2Url = this.lnb2depMenu[i].dep2Url;
        if (dep2Url == "#") {
          // 3depth
          let dep3Length = this.lnb2depMenu[i].lnb3depMenu.length;
          for (let j = 0; j < dep3Length; j++) {
            let dep3Url = this.lnb2depMenu[i].lnb3depMenu[j].dep3Url;
            if (dep3Url == this.menuUrl) {
              $(".lnb-bar_2dep li").eq(i).addClass("currentT");
              $(".lnb-bar_3dep").eq(i).show();
              return;
            }
          }
        } else {
          // 2depth
          if (dep2Url == this.menuUrl) {
            $(".lnb-bar_2dep li").eq(i).addClass("currentT");
            return;
          }
        }
      }

    });

	setTimeout(() => {
        const span = document.createElement("span");
        span.innerHTML = "선택됨";
        span.classList.add("sr-only");
        document.querySelectorAll('.lnb ul li span').forEach((element) => {
			element.remove();
		});
		if(!!document.querySelector(".lnb ul li.currentT")) {
			document.querySelector(".lnb ul li.currentT").append(span);
		}

      }, 1000);
  },
};
</script>

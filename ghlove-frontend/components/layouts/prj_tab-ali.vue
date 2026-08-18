<template>
  <div class="item_tab prj_tab" id="item_tab_wrap">
    <div class="sticky-prj_info">
      <div class="center">
        <div class="prj_progress_wrap prj_details">
          <div class="prj_progress">
            <div class="prj_gauge">
              <div class="gauge_back">
                  <div class="gauge_result"
                       :style="{ width: prjCard.rateAmtStr + '%' }"
                       :class="{
                       lt_goal: prjCard.rateAmt < 100,
                        goal: prjCard.rateAmt === 100,
                        gt_goal: prjCard.rateAmt > 100,
                  }"
                ></div>
              </div>
            </div>
            <div class="inner_wrap">
              <div class="prj_gaugePer">
                <img
                  src="/static/images/icon/cli-icon_btn-donation2.png"
                  alt="초과달성"
                  v-show="prjCard.rateAmt > 100"
                />
                <span class="perNum">{{ prjCard.rateAmtStr }}</span>%
              </div>
              <div class="prj_status"
                   :class="[prjCard.prjStatus == '2' ? 'on' : '']">
                  {{ prjCard.leftDayStr }}
              </div>
            </div>
          </div>
          <div class="donation_amount">
            <span class="txt">기부총액 </span>
            <span class="pointblue"><strong> {{ formatNumber(prjCard.sumAmt) }}</strong> 원</span>
          </div>
        </div>
        <button class="formBtn donation" type="button" @click="goDonationPage" id="tabDonationBtn" v-if="!exportMode">
          <img
            src="/static/images/icon/cli-icon_btn-donation2.png"
            alt=""
          /><span>기부하기</span>
        </button>
      </div>
    </div>
    <ul class="nav nav-tabs nav-justified">
      <li class="nav-item">
        <a href="#nav-detail" class="nav-link active" title="선택됨">
          <span class="txt">사업소개</span>
        </a>
      </li>
      <li class="nav-item">
        <a href="#nav-review" class="nav-link">
          <span class="txt">응원메시지<br>(기부내역)</span>
        </a>
      </li>
      <li class="nav-item">
        <a href="#nav-qna" class="nav-link">
          <span class="txt">공지사항</span>
        </a>
      </li>
    </ul>
  </div>
  <!--// item_tab E -->
</template>

<script>
module.exports = {
    props: {
        /*prjCard: {
            type: Object,
            default: function () {
                return {
                    bsnsType: ''
                    , cntrCnt: 0
                    , leftDayStr: ''
                    , locgovNm: ''
                    , rateAmt: ''
                    , rateAmtStr: ''
                    , sumAmt: ''
                    , targetAmt: ''
                };
            },
        },*/
        exportMode: {
            type: Boolean,
            default: function () {
                return false;
            },
        },
    },
  data() {
    return {
      prjCard: {

      },
      isMobile: $s.isMobile(),

      /* event */
      event: {
        showSetItem: true,
        showQnaForm: true,
        showBuyWrap: false,
        showBuyView: false,
        isBuyViewFixed: false,
        optionType: "",
        itemReviewId: 0,
        qnaId: 0,
      },
    };
  },
  methods: {
    //   부모 컴포넌트에 기부 게이지 계산 copy
    /*gaugePersent() {
      let amount = this.prjCard.donationAmount;
      let amountWithoutCommas = amount.replace(/,/g, "");
      let numAmount = parseInt(amountWithoutCommas, 10);

      let goal = this.prjCard.goal;
      let goaltWithoutCommas = goal.replace(/,/g, "");
      let numGoal = parseInt(goaltWithoutCommas, 10);

      let persent = parseInt((numAmount / numGoal) * 100, 10);
      console.log(numAmount, numGoal, persent);
      return (this.prjCard.gaugeResult = persent);
    },*/
    optSelect: function () {
      $(".dropdown_box").addClass("on");
      e.stopPropagation();
    },
    optClose: function () {
      $(".dropdown_box").removeClass("on");
    },
    /*handleScrollByTab: function (evt, el) {
      // jquery (el.offset().top) -> vanilla js (el.getBoundingClientRect().top + window.scrollY)
      const top = el.getBoundingClientRect().top + window.scrollY;
      const scrollTop = document.scrollingElement.scrollTop;
      const width = window.innerWidth;
      vm.event.isBuyViewFixed = width >= 768 && top - 100 < scrollTop;
    },
    handleScrollByView: function (evt, el) {
      if (vm.event.isBuyViewFixed) {
        // jquery (el.innerHeight()) -> vanilla js (el.offsetHeight - border)
        const c = window.getComputedStyle(el);
        const border =
          parseFloat(c.borderTopWidth) + parseFloat(c.borderBottomWidth);
        el.style.bottom = -(el.offsetHeight - border + 4) + "px";
      }
    },*/
      setPrjCard: function (prjCardData) {
          //this.prjCard = prjCardData;
          this.prjCard = Object.assign(
              {},
              this.prjCard,
              prjCardData
          );
      },
      goDonationPage: function() {
      	this.$emit('go-donation-page', this.prjCard.prjId, this.prjCard.locgovCode,'tabDonationBtn');
      }
  },
  watch: {

  },
  computed: {

  },
  created: function () {
    try {
      var head = document.getElementsByTagName("head")[0];

      if (typeof url != "undefined" && url != "") {
        head.appendChild(getMeta("og:url", url));
      }

      if (typeof title != "undefined" && title != "") {
        head.appendChild(getMeta("og:title", this.unescapeHtml(title)));
      }

      if (
        typeof image != "undefined" &&
        image != "" &&
        image != $s.config.noImage
      ) {
        head.appendChild(getMeta("og:image", image));
      }

      if (typeof description != "undefined" && description != "") {
        head.appendChild(
          getMeta("og:description", this.unescapeHtml(description))
        );
      }
    } catch (e) {
      $s.error(e);
    }

    function getMeta(name, content) {
      var e = document.createElement("meta");

      e.name = name;
      e.content = content;

      return e;
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      window.onscroll = function () {
        myFunction();
      };

      var itemTab = document.getElementById("item_tab_wrap");
      /*var sticky = itemTab.offsetTop - 30;*/
      let sticky = itemTab.offsetTop + 120;

      if (this.isMobile) {
      	sticky += 160;
      }

      function myFunction() {
        // if (window.pageYOffset >= sticky || window.pageYOffset >= 0) {
        if (window.pageYOffset >= sticky) {
          itemTab.classList.add("sticky");
        } else {
          itemTab.classList.remove("sticky");
        }
      }

      $(".nav-item a").click(function () {
        $(this)
          .addClass("active")
          .attr("title","선택됨")
          .parent()
          .siblings()
          .children("a")
          .removeClass("active")
          .removeAttr("title");
      });

    });
  },
};
</script>

<style scoped>
.item_tab .nav-tabs {
  justify-content: center;
}
.nav-justified .nav-item {
  flex-basis: unset;
  flex-grow: unset;
  width: 20%;
}
</style>
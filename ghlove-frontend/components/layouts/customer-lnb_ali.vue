<template>
  <div>
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
  background-image: url('/static/images/subtitle-bg_customer.png');
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
                    {dep2Url : "/notice/list.html", depLabel :"공지사항" },
                    {dep2Url : "/data-board/list.html", depLabel :"자료실" },
                    {dep2Url : "/qna/qna-open.html", depLabel :"Q＆A" },
                    {dep2Url : "/faq/list.html", depLabel :"FAQ" },
                    // AS-IS : 사이트맵 삭제 → TO-BE : 공지사항 / 자료실 / Q&A / FAQ
                    // {dep2Url : "/guide/sitemap.html", depLabel :"사이트맵" },
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

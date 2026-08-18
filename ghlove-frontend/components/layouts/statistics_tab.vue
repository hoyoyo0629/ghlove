<template>
    <div class="chart-tab" id="tabs">
        <ul class="tab_menu">
            <li v-for="(tablist, i) in tabList" :key="i" :class="{ on: currentTab === i }" @click="currentTab = i">{{tablist}}</li>
        </ul>
        <div class="chart-tab_con">
            <div class="chart_area" v-show="currentTab == 0">
                <img src="/static/images/statistics/st_monthly.png" alt="">
            </div>
            <div class="chart_area" v-show="currentTab == 1">
                <img src="/static/images/statistics/st_cases.png" alt="">
            </div>
        </div>
    </div>
</template>
<script>
module.exports = {
    props: {
        menuUrl: {
            type: String,
            required: false,
            default: function () {
                return '';
            }
        }
    },
    name: 'tabs',
    data: function () {
        return {
            currentTab: 0,
            tabList: ['월별 기부금 현황', '월별 기부건수 현황'],
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

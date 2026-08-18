<template>
    <div class="main-search-box">
        <div class="search_top">
            <div class="center">
                <h3 class="search-tit">통합검색</h3>
            </div>
        </div>
        <div class="search_bar">
            <div class="center">
                <div class="search-main">
                    <div class="serch-input-area">
                    	<label for="search-field-id">통합검색</label>

                        <input type="search" role="combobox" aria-autocomplete="list" aria-owns="serchAutoKeywordBox" title="지자체/답례품을 검색하세요"
                            aria-label="지자체/답례품을 검색하세요" class="search-filed" placeholder="지자체/답례품을 검색하세요."
                            autocomplete="off" v-model="searchKeyword" @click="focusFn($event)" @keyup.enter="searchPage()"
                            @keyup="getAutoKeyword($event)" id="search-field-id">
                        <button type="button" class="search-icon-box" @click="searchPage()">
                            <img class="icon-img" src="/static/images/icon/search-blue.png" alt="검색">
                        </button>

                        <div id="serchAutoKeywordBox">
                            <div class="serch-auto-keyword-box" v-show="isAutoKeywordView">
                                <div>
                                    <ul>
                                        <li v-for="(result, i) in autoKeywordList" :key="i" v-html="result.highLightKeyword"
                                            role="option" @click="searchPage(result.keyword)" :title="result.keyword"></li>
                                    </ul>
                                </div>
                                <div class="serch-auto-keyword-box-sub">
                                    <span @click="isAutoKeywordView = false">닫기 <img src="/static/images/icon/btn_close.png"
                                            alt="닫기"></span>
                                </div>
                            </div>
                        </div>


                    </div>
                </div>
            </div>
        </div>

        <div class="keyword_bar">
            <div class="center">
                <div class="keyword_wrap">
                    <div class="header-title">
                        <span>인기검색어</span>
                    </div>
                    <!-- 검색어 개발필요 : 키워드 클릭시 검색되도록.. -->
                    <div class="popular-keyword" v-if="popularList.length > 0">
                        <span class="keyword cursor" v-if="index < 10" v-for="(data, index) in popularList" :key="index"
                            @click="searchPage(data.keyword)" :title="data.keyword" tabindex="0"
                            @keyup.enter="searchPage(data.keyword)">
                            <span class="pointRed">{{ data.num }}</span>{{ data.keyword }}
                        </span>
                    </div>
                    <!-- 검색한 키워드 없음 -->
                    <div class="header-keyword noneR" v-else="popularList.length === 0" style="display:block">
                        <img src="/static/images/icon/cli-icon_search-non_keyword.png" alt="결과없음">
                    </div>
                </div>
                <div class="keyword_wrap">
                    <div class="header-title">
                        <span>최근 검색어</span>
                        <button type="button" class="moreView s-txt" @click="delRecentAll()">전체삭제</button>
                    </div>
                    <div class="header-keyword" v-if="recentList.length > 0">
                        <div class="keyword" v-if="index < 5" v-for="(data, index) in recentList" :key="index" tabindex="0"
                            v-bind:alt="recentList[index].keyword + ' 검색'"
                            @keyup.enter="searchPage(recentList[index].keyword)">
                            <span class="cursor" @click="searchPage(data.keyword)">{{ data.keyword }}</span>
                            <span class="cursor" @click="delRecent(data.recentId)" tabindex="0"
                                @keyup.stop="keydownDelSchKwd($event, data.recentId)"><img class="icon-img"
                                    src="/static/images/icon/icon_close_black.png"
                                    v-bind:alt="'최근 검색어 ' + data.keyword + ' 삭제'"></span>
                        </div>

                    </div>
                    <!-- 검색한 키워드 없음 -->
                    <div class="header-keyword noneR" v-if="recentList.length === 0 && isLogin">
                        <img src="/static/images/icon/cli-icon_search-non_keyword.png" alt="결과없음"><br>
                        <div class="s-txt"> 최근 검색어가 없습니다.</div>
                    </div>

                    <div class="header-keyword noneR" v-if="!isLogin">
                        <img src="/static/images/icon/cli-icon_search-non_keyword.png" alt="결과없음"><br>
                        <div class="s-txt"> 로그인 후 사용가능합니다.</div>
                    </div>
                </div>
            </div>
        </div>
        <button class="closeSearchField" @click="closeSearchModal()">
            <img class="icon-img" src="/static/images/icon/cli-icon_btn-close.png" alt="통합검색닫기">
            <span class="close_txt">검색닫기</span>
        </button>
    </div>
</template>

<style>
.main-search-box {
    position: fixed;
    top: 0px;
    width: 100%;
    background: #fff;
    height: 100%;
    background: linear-gradient(#fff 40%, rgba(0, 0, 0, 0.6) 40%);
    display: none;
    z-index: 1031;
    /* padding-top:50px; */
}

.search_top {
    padding: 24px 0;
    height: 80px;
    text-align: center;
}

.search_top h3 {
    font-size: 26px;
}

.closeSearchField {
    position: absolute;
    right: 30px;
    top: 50px;
    transform: translate(0, -50%);
}

.closeSearchField img {
    transform: translate(-2px, -2px);
}

.search_bar {
    background-color: var(--main-color2);
    /* border: solid var(--gray4);
    border-width: 1px 0; */
}

.search_bar .center {
    padding: 16px 0;
    max-width: 900px;
    position: relative;
}


.keyword_bar .center,
.search_bar .center,
serch-input-area {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.search_bar .center {
    justify-content: center;
}

.search-tit {
    /* font-size: 16px !important; */
    color: var(--gray2);
    line-height: 30px;
}

.search-main {
    display: inline-block;
    position: relative;
    text-align: left;
    border-radius: 5px;
}

.serch-input-area {
    display: block;
    min-width: 320px;
    background-color: #fff;
    padding: 2px 0;
    /* border-bottom: 3px solid transparent; */
}

.serch-input-area input {
    height: 38px;
    border: 0;
    /* background-color: transparent; */
    width: 90%;
    position: relative;
    z-index: 100;
}

.serch-input-area input[type='search'] {
    background-image: none !important;
}

.serch-input-area.focuson {
    /* border-bottom: 3px solid var(--main-color1); */

}

.serch-input-area.focuson input[type='search'] {
    outline: none;
    box-shadow: none;
}

.serch-input-area button {
    line-height: 1em;
}

.main-search-box p {
    font-size: 14px;
    color: #fff;
    padding: calc(var(--margin-padding-4) * 3) 0;
}

/* 자동완성 창*/
.serch-auto-keyword-box {
    position: absolute;
    font-size: 14px;
    background-color: #fff;
    width: 100%;
    left: 0px;
    top: 40px;
    z-index: 1;
    box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.16);
}

.serch-auto-keyword-box ul {
    padding: 12px 0;
}

.serch-auto-keyword-box li {
    padding: 0 10px 0 18px;
    height: 25px;
    overflow: hidden;
    line-height: 25px;
    color: var(--gray2);
    cursor: pointer;
}

.serch-auto-keyword-box li:hover,
.serch-auto-keyword-box li.on {
    background-color: #f9fafb;
}

.serch-auto-keyword-box-sub {
    border-top: 1px solid var(--gray5);
}

.serch-auto-keyword-box-sub span {
    float: right;
    padding: 10px 20px 15px;
    color: var(--gray3);
    cursor: pointer;
}

.serch-auto-keyword-box-sub span img {
    margin-left: 6px;
    transform: translateY(-1px);
}

/* 키워드 */
.keyword_bar {
    width: 100%;
    height: 266px;
    background-color: #fff;
    box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
}

.keyword_bar .center {
    max-width: 900px;
    height: inherit;
    padding: 30px 0 50px;
    display: flex;
    justify-content: flex-start;
    align-items: stretch;
}

.keyword_bar .center .keyword_wrap {
    min-width: 48%;
}

.keyword_bar .center .keyword_wrap:last-child {
    /* border-left: 1px solid var(--gray4); */
    padding-left: 20px;
}

.header-title {
    font-size: 16px;
    font-weight: bold;
    color: var(--gray2);
    white-space: nowrap;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding-bottom: 14px;
}

.popular-keyword,
.header-keyword {
    width: 100%;
    text-align: center;
}

.popular-keyword {
    height: 165px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    flex-wrap: wrap
}

.popular-keyword .keyword {
    padding: 6px 0;
}

.popular-keyword .keyword .pointRed {
    margin-right: 10px;
}

.popular-keyword .keyword .pointRed::after {
    content: ". ";
}

.header-keyword {
    display: flex;
    justify-content: flex-start;
    align-items: flex-start;
    flex-wrap: wrap;
    gap: 6px;
}

.keyword {
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.header-keyword .keyword {
    padding: 6px 10px 4px;
    border: 2px solid var(--gray4);
    border-radius: 20px;
    max-width: 180px;
}

.header-keyword.noneR {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    height: 180px;
}

.keyword .icon-img {
    transform: translateY(-2px);
}

@media screen and (max-width:1030px) {
    .main-search-box {
        top: 0px;
    }

    .search_top {
        padding: 12px 0;
        height: 50px;
        line-height: 20px;
    }

    .search_top h3 {
        font-size: 18px;
    }

    .search_top .logo img {
        height: 24px !important;
    }

    .closeSearchField {
        right: 20px;
        top: 28px;
    }

    .closeSearchField .close_txt {
        display: none;
    }

    /*
    .closeSearchField img {
        transform: translate(0, 0);
    } */
}


@media screen and (max-width:768px) {

    .search-tit {
        font-size: 14px !important;
    }

    .main-search-box {
        background: #fff;
        /*  position: absolute;
        height: unset; */
        padding-bottom: 35px;
    }

    .search_bar {
        background-color: var(--main-color2);
    }

    .keyword_bar {
        height: 73vh;
        overflow-x: hidden;
        overflow-y: scroll;
        box-shadow: none;
    }

    .search_bar .center,
    .keyword_bar .center {
        flex-direction: column;
        padding: 20px 0;
    }

    .search_bar .center {
        gap: 10px;
    }

    .search-main {
        width: 100%;
        background: #fff;
    }

    .header-title {
        padding-bottom: 4px;
    }

    .header-keyword {
        padding-top: 10px;
    }

    .keyword_bar .center .keyword_wrap:last-child {
        border: none;
        padding: 0px 0px 30px;
        order: -1;
    }

    .popular-keyword {
        height: unset;
    }

    .popular-keyword .keyword {
        width: 100%;
        text-align: left;
        padding: 10px 0;
        border-bottom: 1px solid var(--gray4);
    }

    .popular-keyword .keyword:last-child {
        border: none;
    }


}
</style>

<script>

module.exports = {
    props: {

    },
    data() {
        return {
            searchKeyword: '',
            keywordOld: '',
            autoKeywordList: [],
            isAutoKeywordView: false,
            isAutoKeywordBox: false,
            popularList: [], // 인기검색어 결과
            recentList: [],  // 내가찾은검색어 결과
            akc_idx: -1, // 자동완성어 리스트의 현위치
            isLogin: $s.isLogin(),
        };
    },

    methods: {
        focusFn: function (e) {
            if (e.target.value !== '') {
                this.isAutoKeywordView = !this.isAutoKeywordView;
            } else {
                this.isAutoKeywordView = false;
            }

            $(e.target).parent('.serch-input-area').addClass('focuson');
        },
        blurFn: function (e) {
            if (!this.isAutoKeywordBox) {
                this.isAutoKeywordView = false;
            }
            $(e.target).parent('.serch-input-area').removeClass('focuson');
        },
        // 인기검색어
        getPopular: function () {
            let vm = this;
            $s.api.getTotalSearchPopular(vm.param,
                function (response) {
                    vm.popularList = response.data;
                }
            )
        },
        // 내가 찾은 검색어 조회
        getRecent: function () {
            if ($s.isLogin()) {
                let vm = this;
                $s.api.getTotalSearchMyRecent(vm.param,
                    function (response) {
                        vm.recentList = response.data;
                    }
                );
            }
        },
        // 내가 찾은 검색어 등록
        setRecent: function () {
            if ($s.isLogin()) {
                const param = {};
                param.searchKeyword = this.searchKeyword;

                $s.api.setTotalSearchInsertRecent(param,
                    function (response) {
                        vm.recentList = response.data;
                    }
                )
            }
        },
        // 내가 찾은 검색어 삭제
        delRecent: function (recentId) {
            if ($s.isLogin()) {
                let vm = this;
                const param = {};
                param.recentId = recentId;

                $s.api.setTotalSearchDeleteRecent(param,
                    function (response) {
                        vm.recentList = response.data;
                    }
                )

            }
        },
        // 내가 찾은 검색어 전체삭제
        delRecentAll: function () {
            if ($s.isLogin()) {
                let vm = this;
                const param = {};
                $s.confirm("최근검색어를 모두 삭제하시겠습니까?", function () {
                    $s.api.setTotalSearchDeleteRecentAll(param,
                        function (response) {
                            vm.recentList = response.data;
                            $s.closeAlert();
                        }
                    )
                })
            }
        },
        /* 자동완성 항목에 대한 키보드 이벤트 제공 (상호작용 필수)

            - 위 화살표 키(keyCode 38), 아래 화살표 키(keyCode 40): 이전/다음 자동완성 목록 항목 탐색
            스크린 리더 사용자와 키보드 사용자가 자동완성 항목을 선택할 수 있도록 항목 탐색 단축키를 제공해야 합니다.
            마지막 항목에서 다음 항목 탐색 진행 시, 첫 번째 항목으로 순환되어야 하며,
            마찬가지로 첫 번째 항목에서 이전 항목 탐색 진행 시 마지막 항목으로 순환되어야 합니다.

            - Enter 키(keyCode 13): 자동완성 편집을 종료하고, 목록 상자가 축소되어야 합니다. (검색의 경우, 검색이 진행되어야 합니다)

            - Escape 키(keyCode 27): ESC 키를 눌러 자동완성 목록 상자를 축소할 수 있어야 합니다.
            Option 요소에 button이나 a 태그와 같은 초점이 있는 요소가 함께 제공되었을 경우, 별도의 초점 처리가 필요합니다.
            a나 button 요소와 같은 기본 초점이 있는 요소가 목록 상자의 옵션 요소 하위에 제공된 경우,
            해당 a 요소나 button 요소는 Tab키로 탐색되지 않도록 tabindex=”-1”로 제공해야 하며,
            role=”none”으로 해당 요소명을 읽지 않도록 제공해 주어야 합니다.
       */
        // 자동완성어 조회
        getAutoKeyword: function (e) {
            let $this = this;
            let currentKeyword = e.target.value;
            let autoKeywordCount = $this.autoKeywordList.length;
            let keyCode = e.keyCode;
            switch (keyCode) {
                case 38:
                    if ($this.isAutoKeywordView) {
                        $this.akc_idx--;
                        if ($this.akc_idx == -1) {
                            $this.akc_idx = (autoKeywordCount - 1);
                        }
                        $this.currentAutoKeywordLi($this.akc_idx);
                    }
                    break;

                case 40:
                    if ($this.isAutoKeywordView) {
                        $this.akc_idx++;
                        if ($this.akc_idx == autoKeywordCount) {
                            $this.akc_idx = 0;
                        }
                        $this.currentAutoKeywordLi($this.akc_idx);

                    }
                    break;

                default:
                    $this.akc_idx = -1;

                    if (keyCode !== 13 && $this.keywordOld != currentKeyword) {
                        const param = {};
                        param.searchKeyword = currentKeyword;
                        param.domainNo = 0;
                        param.akcModes = 'sc';

                        $s.api.getTotalSearchAutoKeyword(param,
                            function (response) {
                                if (typeof (response.data) !== 'undefined' && response.data.length > 0) {
                                    response.data.forEach(res => res.highLightKeyword = $this.highLight(res.keyword, currentKeyword));
                                    $this.autoKeywordList = response.data;
                                    $this.isAutoKeywordView = true;
                                } else {
                                    $this.autoKeywordList = [];
                                    $this.isAutoKeywordView = false;
                                }

                            }
                        );
                    }
                    $this.keywordOld = currentKeyword;
                    break;
            }

        },
        currentAutoKeywordLi: function (index) {
            let $this = this;
            const lis = document.querySelectorAll('.serch-auto-keyword-box li');
            const selectedLi = lis[index];

            // 모든 li 요소들의 배경색 초기화
            lis.forEach((li) => { li.classList.remove('on'); });

            // 선택한 li 요소의 배경색 변경
            selectedLi.classList.add('on');
            $this.searchKeyword = $this.autoKeywordList[index].keyword;
        },
        highLight: function (text, keyword) {
            let regex_startwith = new RegExp('^' + keyword);
            let regex_spacewith = new RegExp(' ' + keyword);
            let result = text;

            result = result.replace(regex_startwith, "<span class='pointRed'>" + keyword + "</span>");
            result = result.replace(regex_spacewith, "<span class='pointRed'> " + keyword + "</span>");

            return result;
        },
        searchPage: function (keyword) {
            if (typeof (keyword) !== 'undefined') {
                this.searchKeyword = keyword;
            }
            if (this.searchKeyword == '') {
                alert('검색어를 입력하세요');
            } else {
                this.setRecent();
                location.href = "/totalsearch/index.html?searchKeyword=" + this.searchKeyword;
            }
        },
        closeSearchModal() {
            $('.main-search-box').hide();
            $('#searchBtn').focus();
        },
        keydownDelSchKwd: function (event, recentId) {
            if (event && event.key == 'Enter') {
                this.delRecent(recentId);
            }
        },
    },
    mounted: function () {
        // 내가찾은검색어 세팅.
        // 로그인 되어있을 때만 보여준다.
        //this.getRecent();
        //this.getPopular(); 	// 인기검색어 세팅.
    }
};
</script>
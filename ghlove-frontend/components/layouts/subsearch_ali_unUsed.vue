<template>
  <div class="main-search-box">
    <div class="center">
      <h3>통합검색</h3>
      <span class="search-main">
        <span class="serch-input-area" >
          <input type="search" class="search-filed" @click="focusFn($event)" @blur="blurFn($event)" autocomplete="off" v-model="searchKeyword" @keyup.enter="searchPage()" @keyup="getAutoKeyword($event)">
        </span>
        <span class="search-icon-box" @click="searchPage()">
          <img src="/static/images/icon/cli-icon_search.png" alt="검색">
        </span>
          <div @mouseover="isAutoKeywordBox=true" @mouseout="isAutoKeywordBox=false">
		      <div class="serch-auto-keyword-box" v-show="isAutoKeywordView">
		      	<div>
			      	<ul>
			      		<li v-for="(result,i) in autoKeywordList" :key="i" v-html="result.highLightKeyword" @click="searchPage(result.keyword)" :title="result.keyword"></li>
			      	</ul>
		      	</div>
		      	<div class="serch-auto-keyword-box-sub">
		      		<span @click="isAutoKeywordView=false">닫기 <img src="/static/images/icon/btn_close.png" alt="닫기"></span>
		      	</div>
	      	</div>
	      </div>
      </span>
    </div>
  </div>
</template>
<style>
.main-search-box {
   padding: 20px 0;
 /* text-align: center;*/
  background-color:var(--main-color1); 
  background-image: url("/static/images/cli-search-bg.png");
  background-repeat: no-repeat;
  background-size: cover;

}
.main-search-box .center{
  display: flex;
  justify-content: center;
  align-items: center;
}
.main-search-box h3 {
  font-family: var(--fs-kangwonGyoT), sans-serif;
  color: #ffe8c6;
  font-size: 28px;
  /* padding-top: 10px; */
  padding-right: 20px;
   transform: translateY(25%);
}
.search-main {
  display: inline-block;
  position: relative;
  text-align: left;
}
.serch-input-area {
  display: inline-block;
  background-color: #fff;
  min-width: 420px;
  height: 48px;
  border-radius: 50px;
  padding: 4px 10px;
  position: relative;
  z-index: 2;

  box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.2) inset;
}
.serch-input-area input {
  height: 38px;
  border: 0;
  background-color: transparent;
  width: 90%;
  position: relative;
  z-index: 100;
}
.serch-input-area  input[type='search']{
  background-image: none;
}
.serch-input-area  input[type='search']:focus{
  background-image: none;
  box-shadow:none;  
  border:none;
  outline: 0;
}
.search-icon-box {
  background-color: var(--main-color1);
  padding: 9px 11px;
  border-radius: 50px;
  position: absolute;
  z-index: 101;
  right: 3px;
  top: 3px;
  width: 42px;
  height: 42px;
  cursor:pointer;
}

.serch-input-area.focuson {
  border: 3px solid var(--main-color3);
  padding: 2px 10px 6px;
 box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.2);
}
.serch-input-area.focuson + .search-icon-box {
  background-color: var(--main-color3);
}
.main-search-box p {
  font-size: 14px;
  color: #fff;
  padding: calc(var(--margin-padding-4) * 3) 0;
}

/* 자동완성 창*/
.serch-auto-keyword-box{
  position: absolute;
  font-size: 14px;
  background-color: #fff;
  border: 3px solid var(--main-color3);
  width:100%;
  left: 0px;
  top:0px;
  z-index: 1;
  padding-top:44px;
  border-radius: 25px;
}

.serch-auto-keyword-box ul{
  padding: 12px 0;
}

.serch-auto-keyword-box li{
  padding: 0 10px 0 18px;
  height: 25px;
  overflow: hidden;
  line-height: 25px;
  color: var(--gray2);
  cursor: pointer;
}

.serch-auto-keyword-box li:hover{
  background-color: #f9fafb;
}

.serch-auto-keyword-box-sub{
  border-top: 1px solid var(--gray5);
}
.serch-auto-keyword-box-sub span{
  float: right;
  padding: 10px 20px 15px;
  color: var(--gray3);
  cursor: pointer;
}
.serch-auto-keyword-box-sub span img{
  margin-left: 6px;
  transform: translateY(-1px);
}

@media screen and (max-width:1030px) {
  .main-search-box{
    padding: 20px 0;
    background-image:none;
  }
  .main-search-box h3{
    font-size:22px;
  }
  .main-search-box p
  {
    display:none
  }
  .serch-input-area{
    min-width: 310px;
    height: 41px;
  }
  .serch-input-area input[type='search'] {
    height:32px;
  }
  .search-icon-box {
    width:37px; height:37px;
    padding: 6px 8px;
    top:2px;
    right:2px;
  }
  .serch-auto-keyword-box{
	/* width:255px; */
  }
}
@media screen and (max-width:460px) {
  .serch-input-area{
    min-width: 240px;
  }
}
</style>

<script>
// $(document).ready(function(){
//   $("input[type='search']").focus(function(){
//     $(this).attr("value","");
//   });
// });
module.exports = {
  props: {
     
  },
  data() {
    return {
    	searchKeyword: '',
    	keywordOld: '',
    	autoKeywordList: [],
    	isAutoKeywordView: false,
    	isAutoKeywordBox: false
    };
  },

  methods: {
    ifFocusFn: function(e) {
      let errorBoolean = true;
      var obj = e.target;
      var inputS = $(".search-filed");
      if( obj == inputS ){
        $(inputS).parent('.serch-input-area').addClass('focuson');
      } else{
        $(e.target).find('.serch-input-area').removeClass('focuson');
      }
    },
    focusFn: function(e) {
      //console.log(e.target);
      if(e.target.value !== ''){
      	this.isAutoKeywordView = !this.isAutoKeywordView;
      }else{
      	this.isAutoKeywordView = false;
      }
      
      $(e.target).parent('.serch-input-area').addClass('focuson');
    },
    blurFn: function(e) {
      if(!this.isAutoKeywordBox){
	    this.isAutoKeywordView = false;
      }
      $(e.target).parent('.serch-input-area').removeClass('focuson');
    },
    // 내가 찾은 검색어 등록
    setRecent: function (){
	    if($s.isLogin()){
		    const param = {};
			param.searchKeyword = this.searchKeyword;
			
			$s.api.setTotalSearchInsertRecent(param,
	            function (response) {
					//console.log(response);
				}
			)
		}
	},
	// 자동완성어 조회
	getAutoKeyword: function (e) {
		let $this = this;
		let currentKeyword = e.target.value;
		if(e.keyCode !== 13 && this.keywordOld != currentKeyword){
		 	const param = {};
				param.searchKeyword = currentKeyword;
				param.domainNo = 0;
		    	param.akcModes = 'sc';
	
	        $s.api.getTotalSearchAutoKeyword(param,
	            function (response) {
	            	if(typeof(response.data) !== 'undefined' && response.data.length > 0){
		            	response.data.forEach(res => res.highLightKeyword = $this.highLight(res.keyword, currentKeyword));
		            	$this.autoKeywordList = response.data;
		            	$this.isAutoKeywordView = true;
	            	}else{
	            		$this.autoKeywordList = [];
	            		$this.isAutoKeywordView = false;
	            	}
	            	
	            }
	        );
        }
        this.keywordOld = currentKeyword;
    },
    highLight: function(text, keyword) {
    	let regex_startwith = new RegExp('^'+keyword);
    	let regex_spacewith = new RegExp(' '+keyword);
    	let result = text;
    	
    	result = result.replace(regex_startwith, "<span class='pointRed'>" + keyword + "</span>");
    	result = result.replace(regex_spacewith, "<span class='pointRed'> " + keyword + "</span>");
    	
    	return result;
    },
    searchPage: function(keyword){
      if(typeof(keyword) !== 'undefined'){
      	this.searchKeyword = keyword;
      }
      if(this.searchKeyword == ''){
      	alert('검색어를 입력하세요');
      }else{
		  if(location.pathname === '/totalsearch/index.html'){
			this.$emit('query', this.searchKeyword);
	        history.pushState(null, null, "?searchKeyword="+this.searchKeyword);
	        this.searchKeyword = '';
	        this.autoKeywordList = [];
	        this.isAutoKeywordView = false;
		  }else{
			this.setRecent();
		    location.href = "/totalsearch/index.html?searchKeyword="+this.searchKeyword;
		  }
      }
    },
  },
};
</script>
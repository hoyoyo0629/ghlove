<template>
    <!-- 콘텐츠 만족도 -->
    <div class="research-box">
        <div class="research-img">
            <img src="/static/images/cli_research.png" alt="">
        </div>
        <form class="research-area" name="stsfdgForm" @submit.prevent>
            <div class="research-group">
                <div class="research-title">
                    <span>현재 화면 내용에</span>
                    <span><strong>얼마나 만족하십니까?</strong></span>
                </div>
                <div class="research-select">
                    <div class="research-select-item">
                        <input type="radio" v-model="param.stsfdg" value="4" id="survey01" name="radioVal">
                        <label for="survey01">매우만족</label>
                    </div>
                    <div class="research-select-item">
                        <input type="radio" v-model="param.stsfdg" value="3" id="survey02" name="radioVal" >
                        <label for="survey02">만족</label>
                    </div>
                    <div class="research-select-item">
                        <input type="radio" v-model="param.stsfdg" value="2" id="survey03" name="radioVal" >
                        <label for="survey03">불만족</label>
                    </div>
                    <div class="research-select-item">
                        <input type="radio" v-model="param.stsfdg" value="1" id="survey04" name="radioVal">
                        <label for="survey04">매우불만족</label>
                    </div>
                </div>
                <button id="surveyBtn" type="button" class="illustBtn" @click="goToSurvey()">
                    참여하기
                    <span class="arrowAni">
                        <img src="/static/images/icon/cli-icon_btn-illust-arrow1.png" alt="">
                    </span>
                </button>
            </div>
        </form>
    </div>

</template>

<script>
    module.exports = {
        data: function() {
            return{
               param : {
                stsfdg : '',
                menuUrl : ''
               }
            }
        },methods: { 
            goToSurvey : function(){
                if(!this.param.stsfdg){
                    $s.alert("만족도 평가 항목을 선택해 주세요","survey01");
                    return false;
                }

                $s.api.postSubmit("/api/stsfdg/joinSurvey", this.param, function(response){
                    $s.alert('만족도 평가를 등록하였습니다. 감사합니다.','surveyBtn');
                });
            },
            setMenuUrl : function(){
                this.param.menuUrl = location.pathname;
            }
        }, mounted: function(){
            this.$nextTick(function () {
                //Saleson.init();
                this.setMenuUrl();
                
            });
        }
    }
</script>
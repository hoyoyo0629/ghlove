<template>
    <div class="help-btn" id="fixedhelp" @click="helpFileDownload">
        <img class="d-img" src="/static/images/icon/cli-icon_help-btn.png" alt="도움말">
        <span class="d-img">도움말</span>
        <img class="m-img help-icon" src="/static/images/icon/cli-icon_help-btn_m.png" alt="도움말">
    </div>
</template>
<script>
    module.exports = {
        methods: {
            helpFileDownload: function() {
                var downloadFn = this.helpFileDownloadCallback;
                var errorFn = this.helpFileDownloadError;
                $s.api.getCommHelpFileInfo({"menuUrl": location.pathname},
                    function(response) {
                        if(response && response.fileInfo && response.fileInfo.fileNm) {
                            downloadFn(response.fileInfo.mnlSn, response.fileInfo.orginlFileNm);
                        } else {
                            errorFn();
                        }
                    },
                    function(error) {
                        errorFn();
                    }
                )
            },
            helpFileDownloadCallback: function(mnlSn, orginlFileNm) {
                var errorFn = this.helpFileDownloadError;
                $s.api.getHelpFileDownload({"mnlSn": mnlSn}, 
                    function(response) {
                        const link = document.createElement("a");
                        link.href = window.URL.createObjectURL(new Blob([response]));
                        link.setAttribute("download", orginlFileNm);
                        link.style.cssText = "display:none";
                        document.body.appendChild(link);
                        link.click();
                        link.remove();
                    },
                    function(response) {
                        errorFn();
                    }
                );
            },
            helpFileDownloadError: function() {
                $s.alert("도움말을 다운로드 받을 수 없습니다.");
            }
        },
        mounted: function(){
            this.$nextTick(function () {
                //Saleson.init();
            });
        }
    };
</script>
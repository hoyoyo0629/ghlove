<template>
    <div id="daumModal" class="modal fade p_title" data-backdrop='static'>
        <div class="modal-dialog modal-dialog-centered">
            <div id="layerDaumAddr" class="modal-content">
                <div class="modal-header">
                    <h3 class="modal_tit">우편번호 찾기</h3>
                </div>
                <div id="daumAddr" class="modal-body"></div>
                <button type="button" class="modal_close" data-dismiss="modal" @click="closeDaumPostcode()">
                    <span class="screen_out">닫기</span>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
    module.exports = {
        data: function() {
            return {
                param: {
                    newZipcode: "",
                    zipcode1: "",
                    zipcode2: "",
                    zipcode: "",
                    sido: "",
                    sigungu: "",
                    eupmyeondong: "",
                    jibunAddress: "",
                    jibunAddressDetail: "",
                    roadAddress: "",
                    buildingCode: "",
                    closeFocusId: "",
                }
            }
        },
        methods: {
			openDaumAddress: function(callBack, focusId) {
			    var perant_element_layer = document.getElementById('layerDaumAddr');
			    var element_layer = document.getElementById('daumAddr');
			    this.param.closeFocusId = focusId;

			    var self = this;
                new daum.Postcode({
                    oncomplete: function(data) {
                        try {
                            var post = data.postcode;
                            if (post == '') {
                                post = data.zonecode;
                            }

                            var jibunAddress = data.jibunAddress;
                            if (jibunAddress == '') {
                                jibunAddress = data.autoJibunAddress;
                            }

                            var roadAddress = data.roadAddress;
                            if (roadAddress == '') {
                                roadAddress = data.autoRoadAddress;
                            }

                            var addr = jibunAddress;
                            if(data.userSelectedType == 'R'){
                                addr = roadAddress;
                            }

                            if(data.buildingName != ''){
                                addr += ' ('+data.buildingName+')';
                                roadAddress += ' ('+data.buildingName+')';
                            }

                            self.param.newZipcode = data.zonecode;
                            self.param.zipcode = post;
                            self.param.zipcode1 = data.postcode1;
                            self.param.zipcode2 = data.postcode2;

                            self.param.sido = data.sido;
                            self.param.sigungu = data.sigungu;
                            self.param.eupmyeondong = data.bname;

                            self.param.jibunAddress = addr;

                            self.param.roadAddress = roadAddress;
                            self.param.buildingCode = data.buildingCode;

                            if ($.isFunction(callBack)) {
                                callBack(self.param);
                            }
                        } catch (e) {
                            $s.alert(e.message);
                        }

                        $("#daumModal").modal("hide");
                    },
                    width : '100%',
                    height : '100%'
                }).embed(element_layer);

                perant_element_layer.style.display = 'block';
                element_layer.style.display = 'block';

                // var width = element_layer.offsetWidth; //우편번호서비스가 들어갈 element의 width
                var height = window.innerHeight*0.7; //우편번호서비스가 들어갈 element의 height

                // 위에서 선언한 값들을 실제 element에 넣는다.
                element_layer.style.width = 100 + '%';
                element_layer.style.height = height + 'px';
                element_layer.style.maxHeight = 470 + 'px';
                element_layer.style.borderBottom = 1 + 'px solid';
                element_layer.style.padding = 0;

                $("#daumModal").modal("show");
			},
			closeDaumPostcode: function() {
                // iframe을 넣은 element를 안보이게 한다.
                document.getElementById('layerDaumAddr').style.display = 'none';
                document.getElementById('daumAddr').style.display = 'none';
                this.$parent.closeFunction(this.param.closeFocusId);
            }
		}
    }
</script>
<template>
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
                    buildingCode: ""
                }
            }
        },
        methods: {
			openDaumAddress: function(callBack) {
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
                    }
                }).open();
			}
		}
    }
</script>
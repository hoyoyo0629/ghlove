// SNS 계정연동 및 해제
function connectSns(snsType, snsUserId, pageType) {
    if (snsUserId != undefined && snsUserId > 0) {
        $s.confirm("선택하신 SNS의 연결해제를 진행하시겠습니까?", function () {
            var snsUser = {
                "snsUserId" : snsUserId,
                "snsType" : snsType
            };

            $s.api.disconnectSns(snsUser,
                function (response) {
                    if (response.info.status == "00") {
                        $s.alert(response.info.message, function() {
                            if (response.info.value == "00") {
                                location.reload();
                            }
                            $s.closeAlert();
                        });
                    } else {
                        let errorBoolean = true;
                    }
                }, function (error) {
                    $s.alert(error.response.data.description);
                }
            );
        });
    } else {
        if (snsType === "naver") {
            var naverLogin = new naver.LoginWithNaverId(
                {
                    clientId: $s.config.naverLoginAppId,
                    callbackUrl: $s.config.virtualDomain + $s.config.naverLoginCallback,
                    isPopup: true, /* 팝업을 통한 연동처리 여부 */
                    loginButton: {color: "green", type: 3, height: 40} /* 로그인 버튼의 타입을 지정 */
                }
            );

            /* 설정정보를 초기화하고 연동을 준비 */
            naverLogin.init();

            naverProcess();
        } else if (snsType === "facebook") {
            facebookLogin(pageType);
        } else if (snsType === "kakao") {
            loginWithKakao(pageType);
        }
    }
}

// KAKAO 계정연동
// 사용할 앱의 JavaScript 키를 설정해 주세요.
Kakao.init($s.config.kakaoLoginAppId);
function loginWithKakao(pageType) {
    // 로그인 창을 띄웁니다.
    Kakao.Auth.login({
        success : function(authObj) {
            Kakao.API.request({
                url : '/v2/user/me',
                success : function(response) {
                    // 카톡 닉네임 이모티콘 제거 정규식
                    var patterns = "[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+";
                    var nickName = response.properties.nickname;
                    nickName = nickName.replace(new RegExp(patterns), '');
                    response.properties.nickname = nickName;

                    // 카카오에서 던지는 response.id값은 정수형
                    // 스크립트에서 id가 문자형으로 파싱되면서 hash값이 변경되므로 뒤에 ''를 붙여 hash값 변조를 막는다.
                    var snsUser = {
                        "snsType" : "kakao",
                        "snsId" : response.id + '',
                        "email" : response.kakao_account.email === undefined ? '' : response.kakao_account.email,
                        "snsName" : response.properties.nickname
                    };

                    snsUserSubmit(snsUser, pageType);
                },
                fail : function(error) {
                    let errorBoolean = true;
                }
            });
        },
        fail : function(err) {
            $s.alert(JSON.stringify(err));
        }
    });
}

// facebook api 선언 스크립트
window.fbAsyncInit = function() {
    FB.init({
        appId		: $s.config.facebookLoginAppId,
        xfbml		: true,
        version		: 'v6.0'
    });

    FB.AppEvents.logPageView();
};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

// FACEBOOK 계정연동
function facebookLogin(pageType) {
    FB.login(function(response) {
        if (response.authResponse) {
            access_token = response.authResponse.accessToken; //get access token
            user_id = response.authResponse.userID; //get FB UID

            FB.api('/me', {fields: 'email, name, id'}, function(response) {
                var snsUser = {
                    "snsType" : "facebook",
                    "snsId" : response.id,
                    "email" : response.email,
                    "snsName" : response.name
                };

                snsUserSubmit(snsUser, pageType);
            });
        } else {
            let errorBoolean = true;
        }
    }, {scope: 'public_profile, email'});
}

// NAVER 계정연동
function naverResponse(snsUser, pageType) {
    snsUserSubmit(snsUser, pageType);
}

function naverProcess() {
    $('#naverIdLogin_loginButton img').click();
}

function snsUserSubmit(snsUser, pageType) {
    if (pageType === "mypage") {
        snsUser.isMypage = true;
    }

    var isJoined = true;
    if (pageType != "mypage") {
        $s.api.checkSnsJoin(snsUser,
            function (response) {
                if (response.info.value === "0") {
                    isJoined = false;
                }

                joinSnsUser(snsUser, isJoined, pageType);

            });
    }


}

function joinSnsUser(snsUser, isJoined, pageType) {
    if (isJoined && pageType != "mypage") {
        // 가입정보가 있을경우, SNS 로그인 진행
        snsUserLogin(snsUser);
    } else {
        // 가입정보가 없을경우, SNS DB 연동처리
        $s.api.snsJoin(snsUser,
            function (response) {

                if (response.info.status === "00") {    // 처리 성공
                    if (response.info.value === "00") {    // 연동 성공
                        $s.ev.log.joinUser(response.info.userId);
                        if (pageType === "login") {
                            snsUserLogin(snsUser);         // 로그인 페이지에서 접근시 login 처리
                        } else {
                            location.reload();             // 그 외 페이지에서 접근시 reload 처리
                        }
                    }
                }
            }, function (error) {
                $s.alert(error.response.data.description);
            }
        );
    }
}

function snsUserLogin(snsUser) {
    var target = $s.core.getParameter("target");
    var redirectUrl = $s.pages.INDEX;

    if (target != null && target != '') {
        redirectUrl = decodeURIComponent(target);
    }

    $s.api.getAuthSnsToken(snsUser, function () {
        $s.redirect(redirectUrl);
    }, function (error) {
        $s.alert(error.response.data.message);
    });
}
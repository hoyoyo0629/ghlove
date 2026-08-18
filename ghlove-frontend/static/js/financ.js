
/*$(document).ready(function(){
	var scriptElem = document.createElement('script');
	scriptElem.src = "https://t-4user.yeskey.or.kr/v1/fincert.js?dt=20230310";
	document.querySelector('body').appendChild(scriptElem);
});*/

var typeCode = '';
var strNonce = '';
var strDomain = $s.config.domain;

var fCert = {
	initSign : function (str){
		var todayCheck = fCert.getYYYYMMDD();
        
        if(todayCheck < '20230502'){
            $s.alert('오픈 준비중입니다.',"finAuthBtn");
            return false;
        }

		typeCode = str;
		if(document.readyState == 'interactive'){
			document.readyState = 'complete';
		}
		
		if (!document.getElementById("finCertSdk")) {
			fCert.loadFinancSdk(true);
		  } else {
			fCert._init(true);
		  }
	},
	loadFinancSdk : function (signGo) {
		var scriptElem = document.createElement("script");
        var scriptUrl = "";

        if(strDomain == 'production'){
            scriptUrl = 'https://4user.yeskey.or.kr';
        }else{
            scriptUrl = 'https://t-4user.yeskey.or.kr';
        }

		scriptElem.src = scriptUrl + "/v1/fincert.js?dt=" + fCert.getYYYYMMDD();
		scriptElem.id = "finCertSdk";
		document.querySelector("body").appendChild(scriptElem);

		scriptElem.onerror = function () {
			alert("fincert.js 로딩시 오류 발생");
		};
		scriptElem.onload = function () {
			fCert._init(signGo);
		};
	},
	getYYYYMMDD : function () {
		let today = new Date();
		let yearNow = String(today.getFullYear());
		let monthNow = String(today.getMonth() + 1);
		let dayNow = String(today.getDate());
	
		monthNow = (monthNow < 10) ? '0' + monthNow : monthNow;
		dayNow = (dayNow < 10) ? '0' + dayNow : dayNow;
	
		today = Number(yearNow + monthNow + dayNow);
	
		return today;
	},
	_init : function (signGo) {
		$s.api.postSubmitNoAuth('/api/user/getNonce', null, function (response) {
			strNonce = response.resultNonce;

			var orgCode = strDomain == 'production' ? $s.config.orgCode : $s.config.dev_orgCode;	//이용기관에게 부여된 이용기관 코드
			var apiKey = strDomain == 'production' ? $s.config.apiKey : $s.config.dev_apiKey;	//이용기관에게 부여된 이용기관 API KEY (client_id)	
			var clientOrigin = '';	//앱 고유 정보 (Only App)	Android, iOS시 필수
			var uniqVal = '';	//사용자 연결기기 프로그램에서만 유도할 수 있는 고유값 (Only App)	Android, iOS시 필수
			var lang = '';	//UI에 표시될 언어의 종류 (ISO 3166-1)
			var clientType = '';	//클라이언트 종류 (Only App)	Android, iOS시 필수
			//var showAutoConn = vm.fincert.param.showAutoConn;	//APP 환경에서 MO 인증을 위한 정보입력 화면에서	'자동연결하기'를 표시할지 여부
			var showAutoConn = false;
			var useAutoConnInfo = '';	//makeAutoConnInfo / setAutoConnInfo 함수를 통해 자동연결 정보를 APP 내에 저장할지 여부
			//var raIssueAppFunc	//사용자가 인증서가 존재하지 않아 사용자가 인증서 발급 버튼을 클릭했을 때 호출되는 함수

			var param = {};
			if (orgCode.length > 0) {
				param.orgCode = orgCode;
			}

			if (apiKey.length > 0) {
				param.apiKey = apiKey;
			}

			if (clientOrigin.length > 0) {
				param.clientOrigin = clientOrigin;
			}

			if (uniqVal.length > 0) {
				param.uniqVal = uniqVal;
			}

			if (lang.length > 0) {
				param.lang = lang;
			}

			if (clientType.length > 0) {
				param.clientType = clientType;
			}

			/*var css1 = document.getElementById("css1").value;
			var css2 = document.getElementById("css2").value;
			var css3 = document.getElementById("css3").value;

			var cssNum = 0;
			if (css1.length > 0 || css2.length > 0 || css3.length > 0) {
			param.cssUrls = [];
			if (css1.length > 0) {
				param.cssUrls[cssNum] = css1;
				cssNum++;
			}
			if (css2.length > 0) {
				param.cssUrls[cssNum] = css2;
				cssNum++;
			}
			if (css3.length > 0) {
				param.cssUrls[cssNum] = css3;
				cssNum++;
			}
			}*/

			if (typeof saveInLocalStroage === "boolean") {
				param.saveInLocalStroage = saveInLocalStroage;
			}

			if (typeof saveInLocalStorage === "boolean") {
				param.saveInLocalStorage = saveInLocalStorage;
			}

			if (typeof showAutoConn === "boolean") {
				param.showAutoConn = showAutoConn;
			}

			if (typeof useAutoConnInfo === "boolean") {
				param.useAutoConnInfo = useAutoConnInfo;
			}

			if (typeof raIssueAppFunc === "string") {
				if (raIssueAppFunc === "string") {
					param.raIssueAppFunc = "string";
				} else if (raIssueAppFunc === "alertFunction") {
					param.raIssueAppFunc = function () {
						//alert("사용자가 인증서 발급 버튼을 클릭했을 때 이벤트를 받을 콜백 함수가 호출됨!");
					};
				}
			}

			if (JSON.stringify(param) === "{}") {
				try {
					FinCert.Sdk.init();
				} catch (e) {
					alert("예외발생\n[" + e.code + "] " + e.message);
				}
			} else {
				param.success = function () {
					if (signGo) {
						fCert.sign(true);
					}
				};

				param.fail = fCert.failCallback;

				FinCert.Sdk.init(param);
			}
		}, function (error) {
			$s.error(error.response.data.description);
		});
	},
	failCallback : function (error) {
        $s.alert(error.message,"finAuthBtn");
        return false;
		//$(".black-bg").removeClass("show");
	},
	errorMessage : function (error) {
		var message = '';
		if(error.response !== undefined){
			message = error.response.data.description;
		}else{
			message = '서버 오류가 발생했습니다.'
		}
		return message;
	},
	setMobileAuthInfo: function (response, sParams) {
		vm.param.auth = true;
		var age = fCert.getAge(sParams.birthDay);
		if (age < 14) {
			vm.stepStatus = 2;
			vm.isUnderAge = true;
			vm.param.gender = (sParams.gender == 'M') ? '0' : '1';
			vm.param.userName = sParams.userName.replace(/\+/gi, '').trim();
			vm.param.mberDi = sParams.mberDi;
			vm.param.mberCi = sParams.mberCi;
			vm.param.mberFinDn = sParams.mberFinDn;
			var birthday = sParams.birthDay;
			vm.param.birthday = birthday.substring(0, 4) + '-' + birthday.substring(4, 6) + '-' + birthday.substring(6, 8);
			vm.param.birthdayFull = birthday;
			/*var cellNo = response.cellNo;
			if (cellNo.length >= 11) {
				vm.param.phoneCode = cellNo.substring(0, 3);
				vm.param.phoneMid = cellNo.substring(3, 7);
				vm.param.phoneLast = cellNo.substring(7, 11);
			} else {
				vm.param.phoneCode = cellNo.substring(0, 3);
				vm.param.phoneMid = cellNo.substring(3, 6);
				vm.param.phoneLast = cellNo.substring(6, 10);
			}*/

			vm.param.auth = false;
			$("#getParentModal").addClass("show");
		} else {
			if (vm.isUnderAge == false) {
				vm.param.gender = (sParams.gender == 'M') ? '0' : '1';
				vm.param.userName = sParams.userName.replace(/\+/gi, '').trim();
				
				if(!vm.isUnderAge){
                    vm.param.mberDi = sParams.mberDi;
                    vm.param.mberCi = sParams.mberCi;
                    vm.param.mberFinDn = sParams.mberFinDn;
                }

				var birthday = sParams.birthDay;
				vm.param.birthday = birthday.substring(0, 4) + '-' + birthday.substring(4, 6) + '-' + birthday.substring(6, 8);
				vm.param.birthdayFull = birthday;
				vm.param.auth = true;
				/*var cellNo = sParams.cellNo;
				if (cellNo.length >= 11) {
					vm.param.phoneCode = cellNo.substring(0, 3);
					vm.param.phoneMid = cellNo.substring(3, 7);
					vm.param.phoneLast = cellNo.substring(7, 11);
				} else {
					vm.param.phoneCode = cellNo.substring(0, 3);
					vm.param.phoneMid = cellNo.substring(3, 6);
					vm.param.phoneLast = cellNo.substring(6, 10);
				}*/

			}
			vm.stepStatus = 3;
			vm.isUnderAge = false;

		}
	},
	getAge: function (birthday) {
		birthday = Number(birthday.replace(/-/gi, '')); // '-' 문자 모두 '' 변경

		let today = new Date();
		let yearNow = String(today.getFullYear());
		let monthNow = String(today.getMonth() + 1);
		let dayNow = String(today.getDate());

		monthNow = (monthNow < 10) ? '0' + monthNow : monthNow;
		dayNow = (dayNow < 10) ? '0' + dayNow : dayNow;

		today = Number(yearNow + monthNow + dayNow);

		let age = Math.floor((today - birthday) / 10000);

		return age;
	},
	sign : function (isWithUI, isSignEnvelop) {
        // 화면에서 삭제
        //document.getElementById("signedVals").value = "";
		vm.signedVals = "";

        var param = {};

        var userAgreement = '금융분야 마이데이터 통합인증을 위한 인증서 본인확인서비스 이용약관, 개인정보 처리, 고유식별정보 수집·이용 및 위탁에 동의합니다.';
        var realName = true;
        var gender = true;
        var nationalInfo = true;
        var birthDate = true;
        var ci = true;
        var ispUrlInfo = $s.config.virtualDomain;
		var ucpidNonce = strNonce;

        if (
			userAgreement.length > 0 ||
			realName === true ||
			realName === false ||
			gender === true ||
			gender === false ||
			nationalInfo === true ||
			nationalInfo === false ||
			birthDate === true ||
			birthDate === false ||
			ci === true ||
			ci === false ||
			ispUrlInfo.length > 0 ||
			ucpidNonce.length > 0
        ) {
            // content에 해당 되는 내용이 있음
        	param.content = {};

			if (
				userAgreement.length > 0 ||
				realName === true ||
				realName === false ||
				gender === true ||
				gender === false ||
				nationalInfo === true ||
				nationalInfo === false ||
				birthDate === true ||
				birthDate === false ||
				ci === true ||
				ci === false ||
				ispUrlInfo.length > 0 ||
				ucpidNonce.length > 0
				) {
				// ucpidInfo에 해당 되는 내용이 있음
				param.content.ucpidInfo = {};

				if (userAgreement.length > 0) {
					param.content.ucpidInfo.userAgreement = userAgreement;
				}

				if (ispUrlInfo.length > 0) {
					param.content.ucpidInfo.ispUrlInfo = ispUrlInfo;
				}

				if (ucpidNonce.length > 0) {
					param.content.ucpidInfo.ucpidNonce = ucpidNonce;
				}

				if (
					realName === true ||
					realName === false ||
					gender === true ||
					gender === false ||
					nationalInfo === true ||
					nationalInfo === false ||
					birthDate === true ||
					birthDate === false ||
					ci === true ||
					ci === false
				) {
					param.content.ucpidInfo.userAgreeInfo = {};

					if (realName === true || realName === false) {
						param.content.ucpidInfo.userAgreeInfo.realName = realName;
					}

					if (gender === true || gender === false) {
						param.content.ucpidInfo.userAgreeInfo.gender = gender;
					}

					if (nationalInfo === true || nationalInfo === false) {
						param.content.ucpidInfo.userAgreeInfo.nationalInfo = nationalInfo;
					}

					if (birthDate === true || birthDate === false) {
						param.content.ucpidInfo.userAgreeInfo.birthDate = birthDate;
					}

					if (ci === true || ci === false) {
						param.content.ucpidInfo.userAgreeInfo.ci = ci;
					}
				}
			}
        }

        // info 채우기
        var signType = '12';	//UCPID signType
        if (isWithUI === false) {
          // UI 없는 전자서명
          // signType만 있음
          if (signType.length > 0) {
            param.info = {};
            param.info.signType = signType;
          }
        } else {
          // UI 있는 전자서명
          // signType, simpleKeyReq가 있음
          var simpleKeyReq = fCert.getValueFromSelect("signSimpleKeyReq");

          if (
            signType.length > 0 ||
            simpleKeyReq === true ||
            simpleKeyReq === false
          ) {
            // info 항목이 필요함
            param.info = {};

            if (signType.length > 0) {
              param.info.signType = signType;
            }

            if (simpleKeyReq === true || simpleKeyReq === false) {
              param.info.simpleKeyReq = simpleKeyReq;
            }
          }
        }

        if (JSON.stringify(param) === "{}") {
			if (isWithUI) {
				if (isSignEnvelop) {
					try {
						FinCert.Sdk.signEnvelop();
					} catch (e) {
						alert("예외발생\n[" + e.code + "] " + e.message);
					}
				} else {
					try {
						FinCert.Sdk.sign();
					} catch (e) {
						alert("예외발생\n[" + e.code + "] " + e.message);
					}
				}
			} else {
				try {
					FinCert.Sdk.signWithoutUI();
				} catch (e) {
					alert("예외발생\n[" + e.code + "] " + e.message);
				}
			}
        } else {
			param.success = function (_result) {
				//alertPassedTime();
				
				var resultArr;
				if (Array.isArray(_result)) {
					resultArr = _result;
				} else {
					resultArr = [];
					resultArr[0] = _result;
				}

				var signedValString = "";

				vm.signedVals = resultArr[0].signedVals[0];
				signedValString = vm.signedVals;

				//fCert.financApiToken();

				var params = {
					"client_secret" : strDomain == 'production' ? $s.config.clientSecret : $s.config.dev_clientSecret,
					"scope" : "ucpid",
					"grant_type" : "client_credentials",
					"server_id" : "server_server_ilovegohyang"
				}

				var tokenUrl = strDomain == 'production' ? $s.config.finUrl : $s.config.dev_finUrl;
				var ucpicUrl = strDomain == 'production' ? $s.config.ucpidUrl : $s.config.dev_ucpidUrl;
				var cpCode = strDomain == 'production' ? $s.config.cpCode : $s.config.dev_cpCode;
				var apiKey = strDomain == 'production' ? $s.config.apiKey : $s.config.dev_apiKey;

				$.ajax({
					url : tokenUrl,
					type : 'post',
					contentType: 'application/x-www-form-urlencoded;charset=utf-8',
					data : params,
					beforeSend : function(xhr){
						xhr.setRequestHeader("client_id", apiKey); 
					},
					error : function(error) {
						$s.eroor(error);
					},
					success : function (response) {
						$.ajax({
							url : ucpicUrl,
							type : 'post',
							contentType: 'application/json;charset=utf-8',
							data : JSON.stringify({
								"ucpid_request_info" : vm.signedVals,
								"cp_code" : cpCode,
								"ucpid_nonce" : strNonce
							}),
							beforeSend : function(xhr){
								xhr.setRequestHeader("Authorization", response.token_type+ " " + response.access_token); 
							},
							error : function(error) {
								$s.error(error);
							},
							success : function (ucpidResponse) {

								var params = {};
								var url = '';
								params.signedVals = vm.signedVals;
								params.ucpidNonce = strNonce;

								if(ucpidResponse != null){
							
									let sParams = {};
									let inParams = {};
									let changeParams = {};
									if(typeCode == 'insert' || typeCode == 'delete'){
										inParams.birthday = ucpidResponse.birth_date;
										inParams.loginId = vm.param.loginId;
										inParams.phoneNumber = vm.param.phoneNumber;
										inParams.userName = ucpidResponse.real_name;
										inParams.mberFinDn = ucpidResponse.dn;
										inParams.mberCi = ucpidResponse.ci;
									}else if(typeCode == undefined){
										changeParams.loginId = vm.pwdParam.loginId;
										changeParams.mberCi = ucpidResponse.ci;
									}else{
										sParams.mberCi = ucpidResponse.ci;
										sParams.mberDi = ucpidResponse.di;
										sParams.mberFinDn = ucpidResponse.dn;
										sParams.userName = ucpidResponse.real_name;
										sParams.birthDay = ucpidResponse.birth_date;
										sParams.gender = ucpidResponse.gender;
									}
							
									if(typeCode == 'idSearch'){	//아이디 찾기
										url = '/api/user/checkMobileAuth';
									}else if(typeCode == 'passwordSearch'){	//비밀번호 찾기
										url = '/api/user/checkMobileAuthPwd';
									}else if(typeCode == 'login'){  //로그인
										url = '/api/user/getLoginCiInfo';
									}else if(typeCode == 'join'){   //회원가입
										url = '/api/join/checkMobileAuth';
									}else{  //비밀번호 만료 변경
										url = '/api/user/checkMobileAuth';
									}

									if(typeCode == 'idSearch' || typeCode == 'passwordSearch'){	//아이디 찾기 및 비밀번호 찾기
										fCert.searchInfo(url, sParams, ucpidResponse.real_name);
									}else if(typeCode == 'login'){	//로그인 시
										fCert.financLogin(url, sParams);
									}else if(typeCode == 'join'){	//회원가입 시
										$s.api.postSubmitNoAuth(url, sParams, function(responseCheck){
											fCert.setMobileAuthInfo(responseCheck, sParams);
										}, function (error) {
											//$s.alert('서버 오류가 발생했습니다.');
											$s.alert(fCert.errorMessage(error));
										});
									}else{  //비밀번호 만료 변경
										fCert.financPasswordChange(url, changeParams)
									}
								}
							}
						})
					}
					
				})

			};

			param.fail = fCert.failCallback;

			if (isSignEnvelop) {
				var encCert = document.getElementById("encCert").value;
				if (encCert.length > 0) {
				param.encCert = encCert;
				}
			}

			var signParam;
			var count = 1;
			if (count.length > 0) {
				signParam = [];
				var i;
				var countNum = Number.parseInt(count);
				for (i = 0; i < count; i++) {
				signParam[i] = param;
				}
			} else {
				signParam = param;
			}

			//setStartTime();
			try {
				if (isWithUI) {
				if (isSignEnvelop) {
					FinCert.Sdk.signEnvelop(signParam);
				} else {
					FinCert.Sdk.sign(signParam);
				}				
				} else {
				FinCert.Sdk.signWithoutUI(signParam);
				}
			} catch (err) {
				alert(err);
			}
        }
    },
    financApiToken : function () {
		var params = {};
		var url = '';
		params.signedVals = vm.signedVals;
		params.ucpidNonce = strNonce;
		$s.api.postSubmitNoAuth('/api/user/financPid', params, function (response) {

			if(response.result !== undefined && response.result !== null){
				if(response.result.err_code !== undefined){
					$s.alert(response.result.err_msg);
					return false;
				}else{
					var params = {};
					var url = '';
					var ucpidResponse = response.result;
					params.signedVals = vm.signedVals;
					params.ucpidNonce = strNonce;

					if(vm.param.isOnepass == 'Y'){
						if(vm.param.opMberCi != ucpidResponse.ci){
							$s.alert('원패스 인증정보와 동일한 인증정보가 아닙니다.');
							return false;
						}
					}

					if(ucpidResponse != null){
				
						let sParams = {};
						let inParams = {};
						let changeParams = {};
						if(typeCode == 'insert' || typeCode == 'delete'){
							inParams.birthday = ucpidResponse.birth_date;
							inParams.loginId = vm.param.loginId;
							inParams.phoneNumber = vm.param.phoneNumber;
							inParams.userName = ucpidResponse.real_name;
							inParams.mberFinDn = ucpidResponse.dn;
							inParams.mberCi = ucpidResponse.ci;
						}else if(typeCode == undefined){
							changeParams.loginId = vm.pwdParam.loginId;
							changeParams.mberCi = ucpidResponse.ci;
						}else{
							sParams.mberCi = ucpidResponse.ci;
							sParams.mberDi = ucpidResponse.di;
							sParams.mberFinDn = ucpidResponse.dn;
							sParams.userName = ucpidResponse.real_name;
							sParams.birthDay = ucpidResponse.birth_date;
							sParams.gender = ucpidResponse.gender;
						}
				
						if(typeCode == 'idSearch'){	//아이디 찾기
							url = '/api/user/checkMobileAuth';
						}else if(typeCode == 'passwordSearch'){	//비밀번호 찾기
							url = '/api/user/checkMobileAuthPwd';
						}else if(typeCode == 'login'){  //로그인
							url = '/api/user/getLoginCiInfo';
						}else if(typeCode == 'join'){   //회원가입
							url = '/api/join/checkMobileAuth';
						}else{  //비밀번호 만료 변경
							url = '/api/user/checkMobileAuth';
						}

						if(typeCode == 'idSearch' || typeCode == 'passwordSearch'){	//아이디 찾기 및 비밀번호 찾기
							fCert.searchInfo(url, sParams, ucpidResponse.real_name);
						}else if(typeCode == 'login'){	//로그인 시
							fCert.financLogin(url, sParams);
						}else if(typeCode == 'join'){	//회원가입 시
							$s.api.postSubmitNoAuth(url, sParams, function(responseCheck){
								fCert.setMobileAuthInfo(responseCheck, sParams);
							}, function (error) {
								//$s.alert('서버 오류가 발생했습니다.');
								$s.alert(fCert.errorMessage(error));
							});
						}else{  //비밀번호 만료 변경
							fCert.financPasswordChange(url, changeParams)
						}
					}
				}
				
			}else{
				$s.alert('금융인증 정보를 확인 할 수 없습니다. 관리자에게 문의해 주세요.');
			}
			$(".black-bg").removeClass("show");
		}, function (error) {
			$s.alert(error.response.data.message);
		});
    },
	getValueFromSelect : function (id) {
        var selectElement = document.getElementById(id);

		if(selectElement == null){
			return false;
		}else{
			var value = selectElement.value;
			if (value === "true") {
				return true;
			} else if (value === "false") {
				return false;
			} else {
				return value;
			}
		}
	},
    searchInfo : function (url, sParams, real_name) {
        $s.api.postSubmitNoAuth(url, sParams, function(responseCheck){
            if(responseCheck !== undefined && responseCheck !== null){
                if(typeCode == 'passwordSearch'){
                    var resultId = responseCheck.info.loginId;	//계정 체크 후 아이디
                    var inId = vm.password.param.loginId;		// 입력한 아이디
                    var resultName = real_name;	//UCPID 인증 후 이름
                    var inName = vm.password.param.userName;	// 입력한 이름
                    if(resultId != inId || resultName != inName){
                        $s.alert('입력한 정보가 다릅니다. 다시 확인 후 인증해주세요.');
                        return false;
                    }
                }

                if(responseCheck.info.loginId){
                    if('Y' == responseCheck.info.userKeyYN){
                        if(typeCode == 'idSearch') vm.id.step = '1';
                        else if(typeCode == 'passwordSearch') vm.password.step = '1';
                        $s.alert('디지털원패스 회원입니다. 디지털원패스로 로그인 하시기 바랍니다.');
                    }else{
                        if(typeCode == 'idSearch') {
                            vm.id.result.loginId = responseCheck.info.loginId;
                            vm.id.step = '2';
                        }else if(typeCode == 'passwordSearch') {
                            vm.password.result.loginId = responseCheck.info.loginId;    
                            vm.password.step = '2';
                            vm.password.param.mberCi = sParams.mberCi;
                            vm.password.param.mberFinDn = sParams.mberFinDn;
                        }
                    }
                }else{
                    if(typeCode == 'idSearch') {
                        vm.id.step = '3';
                        vm.id.result.loginId = '';
                    }else if(typeCode == 'passwordSearch') {
                        vm.password.step = '3';
                        vm.password.result.loginId = '';
                    }
                    
                }
            }
            $(".black-bg").removeClass('show');
        }, function (error) {
            //$s.alert(fCert.errorMessage(error));
            //console.log(error.message);
            $(".black-bg").removeClass('show');
            $s.alert('아이디와 비밀번호를 찾을 수 없습니다. 다시 확인 후 인증해주세요.');
        });

    },
    financLogin : function (url, sParams) {
        $s.api.postSubmit(url, sParams, function (responseCheck) {
            vm.loginRequest.loginId = responseCheck.result.loginId;

            var logParam = {};
            logParam.loginType = 'ROLE_USER';
            logParam.loginId = responseCheck.result.loginId;
            logParam.password = '';
            logParam.financ = 'financ';

            var passParam = {};
            passParam.loginId = responseCheck.result.loginId;
            passParam.passwordType = 'P';

            $s.api.postSubmit('/api/user/passwordtype', passParam, function (passResponse) {
				if(!passResponse.result && !passResponse.resultCnt){
					vm.birthday = passResponse.birthday;
					$('#temp-birthday').addClass('show');
					return false;
				}
                // 인증
                $s.api.getAuthToken(logParam, function (responseToken) {

                    var savedLoginId = vm.saveId ? vm.loginRequest.loginId : '';
                    $s.core.setData($s.const.SAVED_LOGIN_ID, savedLoginId);

                    var order = $s.core.getSession($s.const.BUY_ORDER);
                    if (order != null) {
                        $s.core.removeSession($s.const.BUY_ORDER);

                        vm.order = JSON.parse(order);
                        vm.order.noMemberLogin = true;

                        $s.api.buyOrder(vm.order, function () { }
                            , function (error) {
                                $s.error(error);	// 실패시 이전 주문정보 조회 (redirect 진행)
                            });
                    }

                    $(".black-bg").removeClass('show');
                    $s.redirect(vm.target);

                }, function (error) {
                    //$s.alert(fCert.errorMessage(error));
                    $s.alert('아이디/비밀번호를 정확히 입력해 주세요.');
                    /*var message = '아이디/비밀번호를 정확히 입력해 주세요.';
                    if (error.response1.data.code == 'UNAUTHORIZED_LOCK') {
                        message = error.response1.data.message;
                    }
                    $s.alert(message, function () {
                        $s.closeAlert();
                    });*/
                });
            });
        }, function (error) {
            //console.log(error.response.data.description);
            //$s.alert(fCert.errorMessage(error));
            $s.alert('인증서 정보에 대한 회원정보를 찾을 수 없습니다.',function () {
				location.href = "/users/join.html";
			});
        });
    },
    financPasswordChange : function(url, params) {
        $s.api.postSubmitNoAuth(url, params, function (response) {
            if (response.info.loginId) {
                params.loginId = vm.loginRequest.loginId;
                vm.pwdParam.loginId = vm.loginRequest.loginId;
                vm.pwdParam.mberCi = params.mberCi;
                //$("#pwdChangeModal").addClass("show");
                
                //휴대폰 본인인증
                var url = '/api/user/changeUserPasswordForNoLogin';
                //공인인증
                if (vm.authType == 'sign') {
                    url = '/api/user/changeUserPwdForSignNoLogin';
                }

                $s.api.postSubmitNoAuth(url, vm.pwdParam, function (response) {
                    var loginRequest = {};
                    loginRequest.loginType = 'ROLE_USER';
                    loginRequest.loginId = vm.pwdParam.loginId;
                    loginRequest.password = vm.pwdParam.corfirmPassword;
                    vm.target = $s.pages.INDEX;
                    vm.submit(loginRequest);
                    //location.href = '/users/login.html';
                }, function(error){
                    $s.alert(error.response.data.message);
                });

            } else {
                $s.alert('해당 사용자가 없습니다.');
            }
        });
    }
}
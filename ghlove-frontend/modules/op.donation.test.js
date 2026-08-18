Vue.use(VueAwesomeSwiper);

let vm = new Vue({
    el: '#saleson',
    data: {
        initLocgovCode: "",
        selectedUpperLocgovCode : "",
        selectedLocgovCode: "",
        selectedRegion: "",
        selectedRegionCd: "",
		sidoList : [],
		cityList : [],
		locgovList : [],
		duplecatedList : [],
		params: {},
		isTesting : false,
		locgovText : "",
		locgovTotalCnt : 0,
		locgovFaCnt : 0,
		locgovScCnt : 0,
		failLocgovText :""
    },
    methods: {
		addLocgovCode : function() {
			if(vm.selectedLocgovCode != ''){
				if(!vm.duplecatedList.includes(vm.selectedLocgovCode)) {
					vm.duplecatedList.push(vm.selectedLocgovCode);
					let obj = new Object();
					obj.locgovCode = vm.selectedLocgovCode;
					obj.locgovName = $("#upperLocgovCode :selected").text() + ' ' + $("#sigunguCd :selected").text();
					vm.locgovList.push(obj);
					$('#locgovText').val($('#locgovText').val() + '\n' + obj.locgovName);
				}
			} else {
				alert('선택된 지자체가 없습니다.');
			}
		},

		locgovListDonation : async () => {
			vm.locgovList = $('#locgovText').val().split(',');
			if(vm.isTesting === true ) {
				alert("테스트 진행 중입니다.");
				return false;
			}
			if($('#userName').val() === '' || $('#jumin').val().trim().length === 0) {
				alert('이름을 입력하세요');
				return false;
			}

			if($('#jumin').val() === '' || $('#jumin').val().trim().length != 13) {
				alert('주민번호를을 정확히 입력하세요');
				return false;
			}

			if(vm.locgovList.length === 0) {
				alert('테스트 할 지자체가 존재하지 않아요');
				return false;
			}

			let obj = new Object();
			obj.locgov = '테스트';
			obj.result = '진행중입니다.';
			obj.log = 'Log Start !';
			test.createTable(obj);

			// 테스트지자체 카운트 및 실패지자체 초기화 
			vm.locgovTotalCnt = 0;
			vm.locgovFaCnt = 0;
			vm.locgovScCnt = 0;
			vm.failLocgovText ="";

			for(let i of vm.locgovList) {
				vm.isTesting = true;
				if(i.substring(0,2) === '11') {
					await test.seoulBuga(i);
				} else {
					await test.ngBuga(i);
				}

				vm.locgovTotalCnt++;
			}
			vm.isTesting = false;
			obj.locgov = '테스트';
			obj.result = '종료되었습니다.';
			obj.log = 'Log End !';
			test.createTable(obj);
		}
    },
    computed: {

    },
    watch: {
		selectedUpperLocgovCode: function (sido) {
			vm.selectedLocgovCode = '';
            if (sido != '') {
				test.sidoChange(sido);
            }
        }
    },
    mounted: function () {
        this.$nextTick(function () {
            // 페이지 로딩 후 실행
            Saleson.init();
				test.init();

        });
    }
});

var test  = {
	init : function() {
		$s.api.getSido(vm.selectedUpperLocgovCode, function (response) {
	        vm.sidoList = response.resultList.sidoList;
	        if (Common.getParameter('locgovCode') != '') {
	            var locgovCode = Common.getParameter('locgovCode');
	            var upperLocgovCode = locgovCode.slice(0, 2) + "000";
	            vm.selectedUpperLocgovCode = upperLocgovCode;
	            vm.initLocgovCode = locgovCode;
	        }
	    });

		vm.locgovText = "11000,11110,11140,11200,11215,11230,11260,11320,11350,11380,11410,11440,11470,11500,11530,11545,11560,11590,11620,11650,11680,11710,11740,26000,27710,26110,26140,26170,26200,26230,26260,26290,26320,26350,26380,26410,26440,26470,26500,26530,26710,27000,27110,27140,27170,27200,27230,27260,27290,44710,28000,28110,28140,28185,28200,28237,28245,28260,28710,28720,29000,29110,43760,43770,44270,36000,29140,29155,29170,29200,30000,30110,30140,30170,30200,30230,31000,31110,31140,31170,31200,31710,41000,41110,41130,41150,41170,41190,41210,41220,41250,41270,41280,41290,41310,41360,41370,41390,41410,41430,41460,41480,41500,41550,41570,41590,41610,41630,41650,41800,41820,41830,52190,52210,52710,52720,52730,52740,52750,52770,52790,52000,43750,43000,43110,43130,43150,43720,44760,27720,43730,43740,43745,43800,44000,44130,44150,44180,44200,44210,44230,44250,44770,44790,44800,44810,44825,46000,46110,46130,46150,46170,46230,46710,46720,46730,46770,46780,46790,46800,46810,46820,46830,46840,46860,46870,46880,46890,46900,11290,28177,41670,46910,47000,47110,47130,47150,47170,47190,47940,47210,47230,47250,47280,47290,47730,47750,48730,47760,47770,47820,47830,47840,47850,47900,47920,47930,48000,48120,48170,48220,48240,48250,48270,48310,48330,48720,48740,48820,48840,48850,48860,48870,48880,48890,50000,11305,11170,51000,51110,51130,51150,51170,51190,51210,51230,51720,51730,51750,51760,51770,51780,51790,51800,51810,51820,51830,52110,52130,52140,52180,52800";
	},

	sidoChange: function (locgovCode) {
        vm.cityList = [];
        vm.selectedRegion = '';
        vm.selectedRegionCd = '';
        vm.params.sidoCode = locgovCode;
        if (vm.params.sidoCode != "") {
            var params = {
                'sidoCode': vm.params.sidoCode
            }
            $s.api.getSiGunGu(params, function (response) {
                    vm.cityList = response.resultList.cityList;

                    if (vm.cityList.length <= 0) {
                        vm.selectedLocgovCode = '';
                        vm.initLocgovCode = '';
                        return;
                    }

                    // 파라미터로 지자체 코드가 넘어온 경우
                    // 파라미터로 넘어온 지자체가 하위 지자체에 포함이 되어있는지 확인 후
                    // 지자체 정보를 세팅 한다.
                    if (vm.initLocgovCode != '') {
                        var isEquals = false;
                        for (var i in vm.cityList) {
                            if (vm.cityList[i].cityCode == vm.initLocgovCode) {
                                isEquals = true;
                                break;
                            }
                        }

                        if (isEquals) {
                            vm.selectedLocgovCode = vm.initLocgovCode;
                            vm.initLocgovCode = '';
                        } else {
                            vm.selectedLocgovCode = '';
                            vm.initLocgovCode = '';
                        }
                    }

                }
                , vm.commError
            )
        }
    },

    ngBuga : function(locgovCode) {
		var params = {
            'frstPctAmt': 100,   // 기부금액
            'pyrNo': $('#jumin').val(), // 납부자번호
            'pyrNm': $('#userName').val(), // 납부자 이름
            'roadNmCd': '411923184001',    // 도로명 코드
            'bmno': '196', // 건물본번
            'bsno': '0',  // 건물부번
            'zip': '14539',       // 우편번호
            'dongCd': '4119210800',    // 행정동코드
            'roadNmDaddr': '(중동)',  // 상세주소
            'glNm': "고향사랑기부금",     // 물건지명
            'selectedRegionCd': locgovCode,
            'userRegionCd': '41190',
			'cntrLocgovCode': locgovCode,
            'psitnLocgovCode': '41190',
            'presentType': '100',
			'foreignStatusCode': '0',				// 내/외국인 구분 코드, 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
			'prjId': '0'										// 지정기부 프로젝트 아이디
        }

        var params1 = {userName: $('#userName').val(), juminNo: '1042310'};

		return new Promise((resolve) => {
			setTimeout(() => {
				$s.api.postSubmit('/api/ngdonation/nextBugaRequest', params, function (response) {
				//$s.api.postSubmit('/api/ngdonation/rsgstadresinfo', params1, function (response) {
	                if (response !== undefined && response !== null) {
						if(response.result.linkRstCd == "000") {
							let obj = new Object();
							obj.locgov = locgovCode;
							obj.log = JSON.stringify(response.result);
							obj.result = "SUCCESS";
							vm.locgovScCnt ++;
							test.createTable(obj);
							resolve();
						} else {
							let obj = new Object();
							obj.locgov = locgovCode;
							obj.log = JSON.stringify(response.result);
							obj.result = "FAIL";
							vm.locgovFaCnt ++;
							vm.failLocgovText += (vm.failLocgovText.length == 0 ? locgovCode : ','.concat(locgovCode));
							test.createTable(obj);
							resolve();
						}
	                } else {
						let obj = new Object();
						obj.locgov = locgovCode;
						obj.log = 'null';
						obj.result = "FAIL";
						vm.locgovFaCnt ++;
						vm.failLocgovText += (vm.failLocgovText.length == 0 ? locgovCode : ','.concat(locgovCode));
	          test.createTable(obj);
	          resolve();
	                }
	            }, function (error) {
					let obj = new Object();
					obj.locgov = locgovCode;
					obj.log = 'null';
					obj.result = "FAIL";
					vm.locgovFaCnt ++;
					vm.failLocgovText += (vm.failLocgovText.length == 0 ? locgovCode : ','.concat(locgovCode));
					test.createTable(obj);
					resolve();
	            });
			}, 1000);

		});
	},

	seoulBuga : function(locgovCode) {
		let semokCd = '';
		var js_date = new Date();
        var year = js_date.getFullYear();
        var month = js_date.getMonth() + 1;

		if (locgovCode == "11000") {
            semokCd = "11228802";
        } else {
            semokCd = "51228802";
        }

        var bugaParams = {
            'systemCd': "01",                   // 인터페이스 구분코드
            'jijacheCd': locgovCode,   // 기부지자체 코드
            'siguCd': "6110000",                //시구코드
            'semokCd': semokCd,                 //세목코드
            'taxYm': year + month,              //과세년월
            'taxGubun': "3",                    //과세구분
            'sidoCd': "11",                     //시도코드
            'napId': $('#jumin').val(), 				//납세자 ID
            'napNm': $('#userName').val(),             //납세자 이름
            'napGubun': "10",                   //납세자 구분
            'taxAmt': '100',    //본세합계
            'sise': '100',      //병기항목 아닌경우 본세
            'resideStatus': "10",                                   //거주상태
            'mulGubun': '03',                                       //물건구분
            'mulNm': "고향사랑기부제",                               //물건명
            'bookNo': "",                                           //원천 시스템의 대장번호
            'sysGubun': "LVHT",                                     //시스템 고유번호 LVHT
            'selectedRegionCd': locgovCode,
            'userRegionCd': '41190',
            'enapbuNo': '',
            'presentType': '100',                           //답례품 여부
			'foreignStatusCode': '0',				// 내/외국인 구분 코드, 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
			'prjId': '0'									// 지정기부 프로젝트 아이디
        }
        var params1 = {userName: $('#userName').val(), juminNo: '1042310'};

		return new Promise((resolve, reject) => {
			setTimeout(() => {
				$s.api.postSubmit('/api/ngdonation/sntrBugaInsert', bugaParams, function (response) {
				//$s.api.postSubmit('/api/ngdonation/rsgstadresinfo', params1, function (response) {
	                if (response !== undefined && response !== null) {
						let sntrBugaInfo = JSON.parse(response.result.api_response);
						if(sntrBugaInfo.errorCode == 0) {
							let obj = new Object();
							obj.locgov = locgovCode;
							obj.log = JSON.stringify(response.result.api_response);
							obj.result = "SUCCESS";
							vm.locgovScCnt ++;
							test.createTable(obj);
							resolve();
						} else {
							let obj = new Object();
							obj.locgov = locgovCode;
							obj.log = JSON.stringify(response.result.api_response);
							obj.result = "FAIL";
							vm.locgovFaCnt ++;
							vm.failLocgovText += (vm.failLocgovText.length == 0 ? locgovCode : ','.concat(locgovCode));
		          test.createTable(obj);
		          resolve();
						}
	                } else {
						let obj = new Object();
						obj.locgov = locgovCode;
						obj.log = 'null';
						obj.result = "FAIL";
	          vm.locgovFaCnt ++;
						vm.failLocgovText += (vm.failLocgovText.length == 0 ? locgovCode : ','.concat(locgovCode));
						test.createTable(obj);
	          resolve();
	                }
	            }, function (error) {
					let obj = new Object();
					obj.locgov = locgovCode;
					obj.log = 'null';
					obj.result = "FAIL";
					vm.locgovFaCnt ++;
					vm.failLocgovText += (vm.failLocgovText.length == 0 ? locgovCode : ','.concat(locgovCode));
					test.createTable(obj);
					resolve();
	            });
			}, 1000);

		});
	},

	createTable : function(obj) {
		if(obj.locgov === '테스트') {
			$('#tblResult').append('<tr style="height: 30px; color: blueviolet; font-size: 13px;"><td>'+obj.locgov+ ' : ' + obj.result + ' ' + obj.log +'</td></tr>');
		} else {
			$('#tblResult').append('<tr style="height: 30px; font-size: 13px;"><td><span style="color: blue;">'+obj.locgov+ '</span> : <span style="color: red;">' + obj.result + '</span></td></tr>');
			$('#tblResult').append('<tr style="height: 30px; font-size: 13px;"><td><span style="color: gray;">' + obj.log +'</td></tr>');
		}

	}
}
var seoul = {
	/**
	     * 서울시 전자번호 발행
	     * @param json
	     * selectedRegionCd : 기부지자체 코드
	     * jumin 			: 주민번호
	     * userName 		: 납세자 이름
	     * cntrAmt 			: 납세 금액
	     * userRegionCd 	: 납세자 지자체 코드
	     * presentType		: 답례품 여부
	     */
	getElecNo: function (json) {
		var semokCd = "";
		var js_date = new Date();
		var year = js_date.getFullYear();
		var month = js_date.getMonth() + 1;

		if (json.selectedRegionCd == "11000") {
			semokCd = "11228802";
		} else {
			semokCd = "51228802";
		}
		
		if (!json.prjId) {	
			// 값이 없으면 0으로 세팅
			json.prjId = 0;
		}

		var bugaParams = {
			'systemCd': "01",                   					  	// 인터페이스 구분코드
			'jijacheCd': json.selectedRegionCd,   					  	// 기부지자체 코드
			'siguCd': "6110000",                						//시구코드
			'semokCd': semokCd,                 						//세목코드
			'taxYm': year + month,              						//과세년월
			'taxGubun': "3",                    						//과세구분
			'sidoCd': "11",                    							//시도코드
			'napId': json.jumin, 										//납세자 ID
			'napNm': json.userName,             						//납세자 이름
			'napGubun': "10",                   						//납세자 구분
			'taxAmt': String(json.cntrAmt).replace(/[^\d]+/g, ''),    //본세합계
			'sise': String(json.cntrAmt).replace(/[^\d]+/g, ''),      //병기항목 아닌경우 본세
			'resideStatus': "10",                                     //거주상태
			'mulGubun': '03',                                         //물건구분
			'mulNm': "고향사랑기부제",                                 //물건명
			'bookNo': "",                                             //원천 시스템의 대장번호
			'sysGubun': "LVHT",                                       //시스템 고유번호 LVHT
			'selectedRegionCd': json.selectedRegionCd,
			'userRegionCd': json.userRegionCd,
			'enapbuNo': '',
			'presentType': json.presentType,                          //답례품 여부
			'foreignStatusCode': json.foreignStatusCode,			  // 내/외국인 구분 코드, 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
			'prjId': json.prjId										  // 지정기부 프로젝트 아이디
		}

		return new Promise(function (resolve, reject) {
			$s.api.postSubmit('/api/seoultax/bugaRequest', bugaParams, function (response) {
				if (json.prjId && json.prjId > 0 && response.data.resultCode) {
					let resultCode = response.data.resultCode;		// 지정기부 결과 코드
					let errMsg = "";
					if (resultCode == "BEFORE" || resultCode == "AFTER" ) {
						errMsg = "해당 지정기부의 기부가능 기간이 아닙니다.";
					} else if (resultCode == "ATTAINMENT") {
						errMsg = "해당 지정기부의 목표금액을 달성하여 기부가 불가능합니다.";
					} else if (resultCode == "STATUS") {
						errMsg = "해당 지정기부는 진행 중 상태가 아닙니다.";
					}
					if (errMsg) {
						reject(errMsg);
						return;	
					}
				}
				
				if ((response !== undefined && response !== null) && 'SUCCESS' === response.result) {
					var sntrBugaInfo = response.data;

					if (sntrBugaInfo.errorCode == 0) {
						resolve(sntrBugaInfo);
					} else {
						reject('서울세외시스템 부과정보 등록이 실패했습니다. 고객센터에 문의 부탁드립니다.\n 실패원인 : ' + sntrBugaInfo.errorMsg);
					}
				} else {
					reject(`${response.message}\n[${response.result}]`);
				}

			}, function (error) {
				reject('서울세외시스템 API호출에 실패하였습니다. 고객센터에 문의 부탁드립니다.');
			});
		});


	},

	/**
		* 이택스 팝업 호출
		*/
	etaxPopOpen: function (mngNo, elctrnPayNo, taxAmt) {
		donation.modal.show();
		var eTaxPopup = window.open('about:blank', 'eTaxPopup', 'width=750, height=700, titlebar=0, toolbar=0, left=300, top=200', '_blank');
		return new Promise(function (resolve, reject) {
			var etaxUrl = "https://etax.seoul.go.kr/jsp/etaxlink.jsp?pay_link=GO&checkable=N&mng_no=" + mngNo + "&tax_info=1@@" + elctrnPayNo + "@@" + taxAmt;
			eTaxPopup.location.href = etaxUrl;

			var etaxPopTimer = setInterval(function () {
				if (eTaxPopup.closed) {
					clearInterval(etaxPopTimer);
					setTimeout(function () {
						donation.modal.hide();
						resolve();
					}, 1500);
				}
			}, 1000);

		});

	},

	/**
		* 이택스수납확인
		*/
	etaxSunapConfirm: function (etaxParam) {
		var params = {
			'systemCd': "02",                   // 인터페이스 구분코드
			'jijacheCd': etaxParam.jijacheCd,   // 기부지자체 코드
			'elctrnPayNo': etaxParam.elctrnPayNo,  // 전자납부번호
			'bookNo': etaxParam.mngNo           // 전문대장관리번호
		}

		return new Promise(function (resolve, reject) {
			$s.api.postSubmit('/api/seoultax/etaxSunapInfo',params, function(response) {

				/*
				if (!response.result || response.result.api_response == '') {
					reject('서울 수납확인 API호출 결과가 없습니다. 고객센터에 문의 부탁드립니다.');
					return false;
				}

				var res = JSON.parse(response.result.api_response);

				resolve(res);
				*/ 
				console.log('수납완룡!');


			}, function (err) {
				reject('서울 수납확인 API호출에 실패하였습니다. 고객센터에 문의 부탁드립니다.');
			});
		});
	}
}
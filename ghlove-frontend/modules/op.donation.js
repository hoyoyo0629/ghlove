let polling = {
  param: {
    contryBugaProcess: false,
    contryBugaCompleted: false,
    giroInterval: null,
    bugaData: null,
  },

  giroPolling: function () {
    $s.log("금결원 연계 polling start");
    polling.giroInterval = setInterval(function () {
      if (polling.param.contryBugaProcess === true) {
        if (polling.param.contryBugaCompleted === true) {
          $s.log("contryBugaCompleted");
          $("#do_whitebg").hide();
          $("#giro-c").addClass("show");
          clearInterval(polling.giroInterval);
        }
      }
    }, 1000);
  },

  //초기화
  giroPollingEnd: function () {
    polling.param.contryBugaProcess = false;
    polling.param.contryBugaCompleted = false;
    $("#giro-c").removeClass("show");
    $("#do_whitebg").hide();
    clearInterval(polling.giroInterval);
  },
};

const payLogGiro = new PayLog(PAYMENT_METHOD.LOCAL, PAYMENT_PROCESS.DONATION_PROGRESS);
const payLogMypage = new PayLog(PAYMENT_METHOD.LOCAL, PAYMENT_PROCESS.DONATION_PROGRESS);

var donation = {
  giroPopup: null,
  giroWaitingSec: 15, // 부과등록 연계가 진행되는 예상시간(초)
  testAjax: function (url, data, callback, failCallback) {
    $.ajax({
      url: "https://www.ilovegohyang.go.kr" + url,
      type: "post",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data),
      beforeSend: function (xhr) {
        xhr.setRequestHeader(
          "Authorization",
          "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCAiOiJ3aGdrc2R1ZDgzIiwib3BfbG9naW5fdHlwZSAiOiJST0xFX1VTRVIiLCJzaWduIjoiZlVWRUxJcFFqMW1DKzZyQnhlMXBWNVJqSnNFaHUzTDRuTGkwZ0VFbnM1ST0iLCJqdGkiOiI1ZjVlMmY3YjBhYTA0ZWY3OTg1YmQyMTU4NGE0YmUwYiIsImlzcyI6Imh0dHBzOi8vaWxvdmVnb2h5YW5nLmdvLmtyIiwiZXhwIjoxNjkyMjM0OTQ4fQ.6m9hfdpHbJNz9FwlXyfk8hOgOlLbMclLr4UjRsRzVUU"
        );
        xhr.setRequestHeader("SALESONID", "0dd95938-21e5-4a20-b631-4c08fbf44254");
      },
      error: function (error) {
        failCallback(error);
      },
      success: function (response) {
        callback(response);
      },
    });
  },
  modal: {
    modalElement: null,
    show: function () {
      if (!this.modalElement) {
        this.modalElement = document.createElement("div");
        this.modalElement.classList.add("black-bg");
        this.modalElement.classList.add("show");
      }
      document.getElementById("wrap").prepend(this.modalElement);
    },
    hide: function () {
      this.modalElement.remove();
    },
  },
  seoul: {
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
        systemCd: "01", // 인터페이스 구분코드
        jijacheCd: json.selectedRegionCd, // 기부지자체 코드
        siguCd: "6110000", //시구코드
        semokCd: semokCd, //세목코드
        taxYm: year + month, //과세년월
        taxGubun: "3", //과세구분
        sidoCd: "11", //시도코드
        napId: json.jumin, //납세자 ID
        napNm: json.userName, //납세자 이름
        napGubun: "10", //납세자 구분
        taxAmt: String(json.cntrAmt).replace(/[^\d]+/g, ""), //본세합계
        sise: String(json.cntrAmt).replace(/[^\d]+/g, ""), //병기항목 아닌경우 본세
        resideStatus: "10", //거주상태
        mulGubun: "03", //물건구분
        mulNm: "고향사랑기부제", //물건명
        bookNo: "", //원천 시스템의 대장번호
        sysGubun: "LVHT", //시스템 고유번호 LVHT
        selectedRegionCd: json.selectedRegionCd,
        userRegionCd: json.userRegionCd,
        enapbuNo: "",
        presentType: json.presentType, //답례품 여부
        foreignStatusCode: json.foreignStatusCode, // 내/외국인 구분 코드, 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
        prjId: json.prjId, // 지정기부 프로젝트 아이디
      };

      return new Promise(function (resolve, reject) {
        $s.api.postSubmit(
          "/api/ngdonation/sntrBugaInsert",
          bugaParams,
          function (response) {
            if (json.prjId && json.prjId > 0 && response.result.resultCode) {
              let resultCode = response.result.resultCode; // 지정기부 결과 코드
              let errMsg = "";
              if (resultCode == "BEFORE" || resultCode == "AFTER") {
                errMsg = "해당 특정사업기부의 기부가능 기간이 아닙니다.";
              } else if (resultCode == "ATTAINMENT") {
                errMsg = "해당 특정사업기부의 목표금액을 달성하여 기부가 불가능합니다.";
              } else if (resultCode == "STATUS") {
                errMsg = "해당 특정사업기부는 진행 중 상태가 아닙니다.";
              }
              if (errMsg) {
                reject(errMsg);
                return;
              }
            }

            if (response.result !== undefined && response.result !== null) {
              var sntrBugaInfo = JSON.parse(response.result.api_response);
              sntrBugaInfo.mngNo = response.result.mngNo;
              if (sntrBugaInfo.errorCode == 0) {
                resolve(sntrBugaInfo);
              } else {
                reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.\n 실패원인 : " + sntrBugaInfo.errorMsg);
              }
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
            }
          },
          function (error) {
            if (error.response.data.message) {
              reject(error.response.data.message);
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
            }
          }
        );
      });
    },

    etaxPopOpen: function (mngNo, enapbuNo, taxAmt) {
      donation.modal.show();
      var eTaxPopup = window.open("about:blank", "eTaxPopup", "width=750, height=700, titlebar=0, toolbar=0, left=300, top=200", "_blank");
      return new Promise(function (resolve, reject) {
        var etaxUrl =
          "https://etax.seoul.go.kr/jsp/etaxlink.jsp?pay_link=GO&checkable=N&mng_no=" + mngNo + "&tax_info=1@@" + enapbuNo + "@@" + taxAmt;
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

    etaxPopOpenExternal: function (mngNo, enapbuNo, taxAmt) {
      donation.modal.show();

      let etaxUrl = "https://etax.seoul.go.kr/jsp/etaxlink.jsp?pay_link=GO&checkable=N&mng_no=" + mngNo + "&tax_info=1@@" + enapbuNo + "@@" + taxAmt;
      //giroPopup.location.href = etaxUrl;

      donation.giroPopup = window.open(etaxUrl, "eTaxPopup", "width=750, height=700, titlebar=0, toolbar=0, left=300, top=200", "_blank");

      return new Promise(function (resolve, reject) {
        if (!donation.giroPopup || donation.giroPopup.closed || typeof donation.giroPopup.closed == "undefined") {
          donation.modal.hide();
          reject();
        } else {
          resolve();
        }
      });
    },

    etaxSunapConfirm: function (etaxParam) {
      var params = {
        systemCd: "02", // 인터페이스 구분코드
        jijacheCd: etaxParam.jijacheCd, // 기부지자체 코드
        enapbuNo: etaxParam.enapbuNo, // 전자납부번호
        bookNo: etaxParam.mngNo, // 전문대장관리번호
      };

      return new Promise(function (resolve, reject) {
        $s.api.postSubmit(
          "/api/ngdonation/etaxSunapInfo",
          params,
          function (response) {
            if (!response.result || response.result.api_response == "") {
              reject("서울 수납확인 API호출 결과가 없습니다. 고객센터에 문의 부탁드립니다.");
              return false;
            }

            var res = JSON.parse(response.result.api_response);

            resolve(res);
          },
          function (err) {
            reject("서울 수납확인 API호출에 실패하였습니다. 고객센터에 문의 부탁드립니다.");
          }
        );
      });
    },
  },
  contry: {
    getJusoApi: function (juso, idx) {
      try {
        var formData = new FormData();
        formData.append("confmKey", "U01TX0FVVEgyMDIyMTEyNTE0MTUzMTExMzI1OTc=");
        formData.append("encodingType", "");
        formData.append("cssUrl", "");
        formData.append("resultType", "4");
        formData.append("currentPage", "1");
        formData.append("countPerPage", "1");

     //   console.log("op.donation.js:: getJusoApi:: juso:: idx:"+idx+", juso:" + juso.rstJuso.split(",")[0]);

        if (idx == 0) {
          formData.append("keyword", juso.rstJuso.split(",")[0]);
        } else {
          formData.append("keyword", juso.bassAdres.split(",")[0]);
        }

        var queryString = new URLSearchParams(formData).toString();

        return new Promise(function (resolv, reject) {
          try {
            $.ajax({
              url: "https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do", //인터넷망
              type: "post",
              data: queryString,
              dataType: "jsonp",
              crossDomain: true,
              success: function (xmlStr) {
                try {
                    var xmlData = xmlStr.returnXml;
           //         console.log("op.donation.js:: getJusoApi:: return:: " + JSON.stringify(xmlData));
                    if ($(xmlData).find("totalCount").text() === "0" || $(xmlData).find("totalCount").text() == undefined) {
                      if (idx == 0) {
                        return donation.contry
                          .getJusoApi(juso, 1)
                          .then(function (response) {
                            resolv(response);
                          })
                          .catch(function (msg) {
                            console.log(msg);
                            reject(msg);
                          });
                      } else {
                        // 조회 0건인 경우에도 errorCode 0: 정상 반환됨. 
                        console.log("주소기반산업지원서비스 errorCode:" + $(xmlData).find("errorCode").text() + ", errorMessage:" + $(xmlData).find("errorMessage").text());
                        reject("주소 조회에 실패하였습니다. 입력하신 주소를 확인해주세요.");
                      }
                    }

                    var rnMgtSn = $(xmlData).find("rnMgtSn").text(); // 도로명 코드
                    var udrtYn = $(xmlData).find("udrtYn").text(); // 지하여부
                    var buldMnnm = $(xmlData).find("buldMnnm").text(); // 건물본번
                    var buldSlno = $(xmlData).find("buldSlno").text(); // 건물부번
                    var zipNo = $(xmlData).find("zipNo").text(); // 우편번호
                    var admCd = $(xmlData).find("admCd").text(); // 행정동코드
                    var bdMgtSn = $(xmlData).find("bdMgtSn").text(); // 건물관리번호
                    var roadAddr = $(xmlData).find("roadAddrPart2").text(); // 상세주소

                    //var rnMgtSn = "114403113012"; // 도로명 코드
                //var udrtYn = "N"; // 지하여부
                //var buldMnnm = "31"; // 건물본번
              // var buldSlno = "0"; // 건물부번
                //var zipNo = "03923"; // 우편번호
                //var admCd = "1144012700"; // 행정동코드
              // var bdMgtSn = "0"; // 건물관리번호
                //var roadAddr = "한국지역정보개발원"; // 상세주소

                  resolv({
                    rnMgtSn: rnMgtSn,
                    udrtYn: udrtYn,
                    buldMnnm: buldMnnm,
                    buldSlno: buldSlno,
                    zipNo: zipNo,
                    admCd: admCd,
                    bdMgtSn: bdMgtSn,
                    roadAddr: roadAddr,
                  });
                } catch (e) {
                  console.log(e);
                  reject("주소기반산업지원서비스 조회에 실패하였습니다.. 고객센터에 문의해주세요.");
                }
              },
              error: function (e) {
                console.log(e);
                reject("주소기반산업지원서비스에 실패하였습니다. 고객센터에 문의 부탁드립니다.\n(회사, 관공서 pc로 기부를 진행할 경우 방화벽, 보안프로그램으로 인해 기부진행에 제한이 있습니다.)");
              },
              
            });

          } catch (e) {
            console.log(e);
            reject("주소기반산업지원서비스 조회에 실패하였습니다. 고객센터에 문의해주세요..");
          }

        });
      } catch (e) {
        console.log(e);
        return Promise.reject("주소기반산업지원서비스 조회에 실패하였습니다. 고객 센터에 문의해주세요.");
      }

    },

    contryBugaInsert: function (params) {
      var js_date = new Date();
      var year = js_date.getFullYear();

      let txprSp = "01";
      switch (params.foreignStatusCode) {
        case "1":
        case "2":
        case "3":
          txprSp = "05"; // 외국인
          break;
        default:
          txprSp = "01"; // 내국인
          break;
      }

      if (!params.prjId) {
        // 값이 없으면 0으로 세팅
        params.prjId = 0;
      }

      var bugaParams = {
        systemCd: "03", // 인터페이스 구분코드
        jijacheCd: params.selectedRegionCd, // 기부지자체 코드
        deptCd: "99999999999", // 자치단체 부서코드
        fisyy: year, // 회계년도
        fisSp: "", // 회계구분코드
        ptclCd: "260004", // 세목코드
        taxAmt: params.taxAmt, // 기부금액
        impsSp: "02", // 부과구분
        decsSp: "02", // 감경구분
        //'txprSp': "01",        // 납부자구분
        txprSp: txprSp, // 납부자구분
        txprNo: params.jumin, // 납부자번호
        txprNm: params.userName, // 납부자 이름
        newAddrYn: "1", // 새주소 여부
        txprRoadCd: params.rnMgtSn, // 도로명 코드
        txprBdFlrSp: params.udrtYn, // 지하여부
        txprBdPrcpNo: params.buldMnnm, // 건물본번
        txprBdSubNo: params.buldSlno, // 건물부번
        statCd: "10", // 납부자상태
        spclFisBizCd: "092020", // 특별회계사업코드
        txprZipCd: params.zipNo, // 우편번호
        txprTwnvilCd: params.admCd, // 행정동코드
        txprBdMngNo: params.bdMgtSn, // 건물관리번호
        txprDtlAddr: params.roadAddr, // 상세주소
        objNm: "고향사랑기부금", // 물건지명
        taxObjSp: "15", // 부과대상구분코드
        taxObjNewAddrYn: "1", // 물건지 새주소 여부
        mngHtm1: "고향사랑기부금", // 기부금 명칭 및 기타항목
        mngHtm5: "", // 기부금 시스템키(유일키)
        sysCd: "S020", // 시스템 코드
        intgrtnSp: "02", // 통합구분
        selectedRegionCd: params.selectedRegionCd,
        userRegionCd: params.userRegionCd,
        enapbuNo: "",
        presentType: params.presentType,
        foreignStatusCode: params.foreignStatusCode, // 내/외국인 구분 코드, 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
        prjId: params.prjId, // 지정기부 프로젝트 아이디
      };

      return new Promise(function (resolve, reject) {
        let contryBugaInfo;
        $s.api.postSubmit(
          "/api/ngdonation/contryBugaInsert",
          bugaParams,
          function (response) {
            if (response.result !== undefined && response.result !== null) {
              if (params.prjId && params.prjId > 0 && response.result.resultCode) {
                let resultCode = response.result.resultCode; // 지정기부 결과 코드
                let errMsg = "";
                if (resultCode == "BEFORE" || resultCode == "AFTER") {
                  errMsg = "해당 특정사업기부의 기부가능 기간이 아닙니다.";
                } else if (resultCode == "ATTAINMENT") {
                  errMsg = "해당 특정사업기부의 목표금액을 달성하여 기부가 불가능합니다.";
                } else if (resultCode == "STATUS") {
                  errMsg = "해당 특정사업기부는 진행 중 상태가 아닙니다.";
                }
                if (errMsg) {
                  reject(errMsg);
                  return;
                }
              }

              contryBugaInfo = JSON.parse(response.result.api_response);
              contryBugaInfo.mngNo = response.result.mngNo;
              if (contryBugaInfo.result_code == "100") {
                resolve(contryBugaInfo);
              } else {
                reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
              }
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
            }
          },
          function (error) {
            if (error.response.data.message) {
              reject(error.response.data.message);
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
            }
          }
        );
      });
    },

    contryNextBugaInsert: function (params) {
      var js_date = new Date();
      var year = js_date.getFullYear();

      let txprSp = "01";
      switch (params.foreignStatusCode) {
        case "1":
        case "2":
        case "3":
          txprSp = "05"; // 외국인
          break;
        default:
          txprSp = "01"; // 내국인
          break;
      }

      if (!params.prjId) {
        // 값이 없으면 0으로 세팅
        params.prjId = 0;
      }

      var bugaParams = {
        frstPctAmt: params.taxAmt, // 기부금액
        pyrNo: params.jumin, // 납부자번호
        pyrNm: params.userName, // 납부자 이름
        roadNmCd: params.rnMgtSn, // 도로명 코드
        bmno: params.buldMnnm, // 건물본번
        bsno: params.buldSlno, // 건물부번
        zip: params.zipNo, // 우편번호
        dongCd: params.admCd, // 행정동코드
        roadNmDaddr: params.roadAddr, // 상세주소
        glNm: "고향사랑기부금", // 물건지명
        selectedRegionCd: params.selectedRegionCd,
        userRegionCd: params.userRegionCd,
        cntrLocgovCode: params.selectedRegionCd,
        psitnLocgovCode: params.userRegionCd,
        presentType: params.presentType,
        foreignStatusCode: params.foreignStatusCode, // 내/외국인 구분 코드, 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
        prjId: params.prjId, // 지정기부 프로젝트 아이디
      };

      return new Promise(function (resolve, reject) {
        let contryBugaInfo;
        $s.api.postSubmit(
          "/api/ngdonation/nextBugaRequest",
          bugaParams,
          function (response) {
            if (response.result !== undefined && response.result !== null) {
              if (params.prjId && params.prjId > 0 && response.result.resultCode) {
                let resultCode = response.result.resultCode; // 지정기부 결과 코드
                let errMsg = "";
                if (resultCode == "BEFORE" || resultCode == "AFTER") {
                  errMsg = "해당 특정사업기부의 기부가능 기간이 아닙니다.";
                } else if (resultCode == "ATTAINMENT") {
                  errMsg = "해당 특정사업기부의 목표금액을 달성하여 기부가 불가능합니다.";
                } else if (resultCode == "STATUS") {
                  errMsg = "해당 특정사업기부는 진행 중 상태가 아닙니다.";
                }
                if (errMsg) {
                  reject(errMsg);
                  return;
                }
              }

              //contryBugaInfo = JSON.parse(response.result.api_response);
              contryBugaInfo = response.result;
              contryBugaInfo.mngNo = response.result.linkMngKey;
              if (contryBugaInfo.linkRstCd == "000") {
                resolve(contryBugaInfo);
              } else {
                reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다");
              }
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다");
            }
          },
          function (error) {
            if (error.response.data.message) {
              reject(error.response.data.message);
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다");
            }
          }
        );
      });
    },

    giroPay: function () {
      let data = polling.param.bugaData;
      let domain = window.location.protocol + "//" + window.location.host;
      let params = {
        domain: domain,
        jijacheCd: data.selectedRegionCd,
        enapbuNo: data.epayNo,
        userCntrPoint: data.userPoint,
        taxAmt: data.taxAmt,
        presentType: data.presentType,
      };

      giroPopup = window.open("", "popOpen", "width=550, height=800, titlebar=0, toolbar=0, left=300, top=200");

      $s.api.postSubmit(
        "/api/ngdonation/giroPay",
        params,
        function (response) {
          $("#do_whitebg").hide();
          if (response.result.errorMsg) {
            reject(response.result.errorMsg);
            return false;
          }

          if (response.result.errorMsg) {
            $s.alert(response.result.errorMsg);
            return false;
          }
          if (response.isMobile == "Y") {
            //모바일
            giroPopup.resizeTo(550, 800);
          } else {
            giroPopup.resizeTo(600, 800);
          }
          let form = document.createElement("form");
          form.setAttribute("method", "post");
          form.setAttribute("target", "popOpen");
          form.setAttribute("action", response.result.popUrl);

          let input = document.createElement("input");
          input.setAttribute("type", "hidden"); // 태그 속성 설정
          input.setAttribute("id", "ENC_DATA");
          input.setAttribute("name", "ENC_DATA");
          input.setAttribute("value", response.result.data);

          form.appendChild(input);

          if ($("#giroPopResult").length <= 0) {
            $("body").append("<input type='hidden' name='giroPopResult' id='giroPopResult' />");
          } else {
            $("#giroPopResult").val("");
          }
          donation.contry.giroSunapConfirmStart(params, form);

          document.body.appendChild(form);
          payLogGiro.startPayLog(data.epayNo).then((response) => (payLogGiro.payLogId = response.data.payLogId));
          form.submit();
        },
        function (err) {
          $("#do_whitebg").hide();
          $s.confirm2(
            "관련 시스템과 연계 지연으로 기부가 원활하지 않습니다. 다시 시도 하시겠습니까?",
            "확인",
            "취소",
            function () {
              $s.closeAlert();
              donation.contry.giroPay();
            },
            function () {
              $s.closeAlert();
              location.href = "/mypage/cntrList.html";
            }
          );
        }
      );
    },

    giroSunapConfirmStart: function (giroParams, form) {
      var giroPopTimer = setInterval(function () {
        if (giroPopup.closed) {
          form.remove();
          clearInterval(giroPopTimer);
          if ($("#giroPopResult").val() === "SUCCESS") {
            donation.contry.sunapSuccess(giroParams);
            payLogGiro.endPayLog(payLogGiro.payLogId, PAYMENT_PROCESS.DONATION_SUCCESS);
          } else if ($("#giroPopResult").val() === "FAIL") {
            payLogGiro.endPayLog(payLogGiro.payLogId, PAYMENT_PROCESS.DONATION_FAIL);
            $s.confirm2(
              "금결원 지로 요청을 계속 하시겠습니까?",
              "확인",
              "취소",
              function () {
                $s.closeAlert();
                donation.contry.giroPay();
              },
              function () {
                $s.closeAlert();
                location.reload();
              }
            );
          } else {
            payLogGiro.endPayLog(payLogGiro.payLogId, PAYMENT_PROCESS.DONATION_CLOSE);
            location.reload();
          }

          $("#giroPopResult").remove();
        }
      }, 1000);
    },

    contryBugaInsertLocalTest: function (params) {
      var js_date = new Date();
      var year = js_date.getFullYear();

      var bugaParams = {
        systemCd: "03", // 인터페이스 구분코드
        jijacheCd: params.selectedRegionCd, // 기부지자체 코드
        deptCd: "99999999999", // 자치단체 부서코드
        fisyy: year, // 회계년도
        fisSp: "", // 회계구분코드
        ptclCd: "260004", // 세목코드
        taxAmt: params.taxAmt, // 기부금액
        impsSp: "02", // 부과구분
        decsSp: "02", // 감경구분
        txprSp: "01", // 납부자구분
        txprNo: params.jumin, // 납부자번호
        txprNm: params.userName, // 납부자 이름
        newAddrYn: "1", // 새주소 여부
        txprRoadCd: params.rnMgtSn, // 도로명 코드
        txprBdFlrSp: params.udrtYn, // 지하여부
        txprBdPrcpNo: params.buldMnnm, // 건물본번
        txprBdSubNo: params.buldSlno, // 건물부번
        statCd: "10", // 납부자상태
        spclFisBizCd: "092020", // 특별회계사업코드
        txprZipCd: params.zipNo, // 우편번호
        txprTwnvilCd: params.admCd, // 행정동코드
        txprBdMngNo: params.bdMgtSn, // 건물관리번호
        txprDtlAddr: params.roadAddr, // 상세주소
        objNm: "고향사랑기부금", // 물건지명
        taxObjSp: "15", // 부과대상구분코드
        taxObjNewAddrYn: "1", // 물건지 새주소 여부
        mngHtm1: "고향사랑기부금", // 기부금 명칭 및 기타항목
        mngHtm5: "", // 기부금 시스템키(유일키)
        sysCd: "S020", // 시스템 코드
        intgrtnSp: "02", // 통합구분
        selectedRegionCd: params.selectedRegionCd,
        userRegionCd: params.userRegionCd,
        enapbuNo: "",
        presentType: params.presentType,
        foreignStatusCode: params.foreignStatusCode,
      };

      return new Promise(function (resolve, reject) {
        $("#do_whitebg").show();
        $.ajax({
          url: "https://www.ilovegohyang.go.kr/api/ngdonation/contryBugaInsert",
          type: "post",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(bugaParams),
          beforeSend: function (xhr) {
            xhr.setRequestHeader(
              "Authorization",
              "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCAiOiJ3aGdrc2R1ZDgzIiwib3BfbG9naW5fdHlwZSAiOiJST0xFX1VTRVIiLCJzaWduIjoiZlVWRUxJcFFqMW1DKzZyQnhlMXBWNVJqSnNFaHUzTDRuTGkwZ0VFbnM1ST0iLCJqdGkiOiI1ZjVlMmY3YjBhYTA0ZWY3OTg1YmQyMTU4NGE0YmUwYiIsImlzcyI6Imh0dHBzOi8vaWxvdmVnb2h5YW5nLmdvLmtyIiwiZXhwIjoxNjkyMjM0OTQ4fQ.6m9hfdpHbJNz9FwlXyfk8hOgOlLbMclLr4UjRsRzVUU"
            );
            xhr.setRequestHeader("SALESONID", "0dd95938-21e5-4a20-b631-4c08fbf44254");
          },
          error: function (error) {
            if (error.response.data.message) {
              reject(error.response.data.message);
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
            }
          },
          success: function (response) {
            if (response.result !== undefined && response.result !== null) {
              contryBugaInfo = JSON.parse(response.result.api_response);
              contryBugaInfo.mngNo = response.result.mngNo;
              if (contryBugaInfo.result_code == "100") {
                resolve(contryBugaInfo);
              } else {
                reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
              }
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
            }
          },
        });
      });
    },

    giroPayLocalTest: function () {
      let data = polling.param.bugaData;
      let domain = window.location.protocol + "//" + window.location.host;
      let params = {
        domain: domain,
        jijacheCd: data.selectedRegionCd,
        enapbuNo: data.elctPayNo,
        userCntrPoint: data.userPoint,
        taxAmt: data.taxAmt,
        presentType: data.presentType,
      };

      giroPopup = window.open("", "popOpen", "width=550, height=800, titlebar=0, toolbar=0, left=300, top=200");

      $.ajax({
        url: "https://www.ilovegohyang.go.kr/api/ngdonation/giroPay",
        type: "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(params),
        beforeSend: function (xhr) {
          xhr.setRequestHeader(
            "Authorization",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCAiOiJ3aGdrc2R1ZDgzIiwib3BfbG9naW5fdHlwZSAiOiJST0xFX1VTRVIiLCJzaWduIjoiZlVWRUxJcFFqMW1DKzZyQnhlMXBWNVJqSnNFaHUzTDRuTGkwZ0VFbnM1ST0iLCJqdGkiOiI1ZjVlMmY3YjBhYTA0ZWY3OTg1YmQyMTU4NGE0YmUwYiIsImlzcyI6Imh0dHBzOi8vaWxvdmVnb2h5YW5nLmdvLmtyIiwiZXhwIjoxNjkyMjM0OTQ4fQ.6m9hfdpHbJNz9FwlXyfk8hOgOlLbMclLr4UjRsRzVUU"
          );
          xhr.setRequestHeader("SALESONID", "0dd95938-21e5-4a20-b631-4c08fbf44254");
        },
        error: function (error) {
          $s.confirm2(
            "관련 시스템과 연계 지연으로 기부가 원활하지 않습니다. 다시 시도 하시겠습니까?",
            "확인",
            "취소",
            function () {
              $s.closeAlert();
              donation.contry.giroPayLocalTest(data);
            },
            function () {
              $s.closeAlert();
              location.href = "/mypage/cntrList.html";
            }
          );
        },
        success: function (response) {
          if (response.result.errorMsg) {
            $s.alert(response.result.errorMsg);
            return false;
          }
          if (response.isMobile == "Y") {
            //모바일
            giroPopup.resizeTo(550, 800);
          } else {
            giroPopup.resizeTo(600, 800);
          }
          let form = document.createElement("form");
          form.setAttribute("method", "post");
          form.setAttribute("target", "popOpen");
          form.setAttribute("action", response.result.popUrl);

          let input = document.createElement("input");
          input.setAttribute("type", "hidden"); // 태그 속성 설정
          input.setAttribute("id", "ENC_DATA");
          input.setAttribute("name", "ENC_DATA");
          input.setAttribute("value", response.result.data);

          form.appendChild(input);

          if ($("#giroPopResult").length <= 0) {
            $("body").append("<input type='hidden' name='giroPopResult' id='giroPopResult' />");
          } else {
            $("#giroPopResult").val("");
          }
          donation.contry.giroSunapConfirmStartTest(params, form);

          document.body.appendChild(form);
          form.submit();
        },
      });
    },

    giroSunapConfirmStartTest: function (giroParams, form) {
      var giroPopTimer = setInterval(function () {
        if (giroPopup.closed) {
          form.remove();
          clearInterval(giroPopTimer);
          if ($("#giroPopResult").val() === "SUCCESS") {
            donation.contry.sunapSuccess(giroParams);
          } else if ($("#giroPopResult").val() === "FAIL") {
            $s.confirm2(
              "금결원 지로 요청을 계속 하시겠습니까?",
              "확인",
              "취소",
              function () {
                $s.closeAlert();
                donation.contry.giroPay();
              },
              function () {
                $s.closeAlert();
                location.reload();
              }
            );
          } else {
            location.reload();
          }

          $("#giroPopResult").remove();
        }
      }, 1000);
    },

    sunapSuccess: function (params) {
      $s.api.postSubmit(
        "/api/ngdonation/sunapSuccess",
        params,
        function (response) {
          if (response.result !== undefined && response.result !== null) {
            if (response.result.sunap === "SUCCESS") {
              $(".black-bg.donation-c").addClass("show");
              setTimeout(function () {
                $("#donation-c").focus();
              }, 100);
            }
          } else {
            alert("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
          }
        },
        function (error) {
          alert("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
        }
      );
    },

    /** 지로 수납확인 시작 */
    /*
        giroSunapConfirmStart: function(data, giroParams, form, resolve, reject) {

            var giroPopTimer = setInterval(function () {
                if (donation.giroPopup.closed) {
					form.remove();
					clearInterval(giroPopTimer);
                    if($("#giroPopResult").val() === 'SUCCESS') {
                        resolve(giroParams);
                    }else if($("#giroPopResult").val() === 'FAIL') {
						$s.confirm2('기부정보처리 요청을 계속 하시겠습니까?','확인','취소', function (){
							$s.closeAlert();
							setTimeout(function () {
								donation.contry.giroPay(data)
								.then(function (response) {
									resolve(response);
								})
								.catch(function () {
									reject('수납처리에 실패하였습니다.');
								});
							}, 1500);
						}, function (){
							$s.closeAlert();
							setTimeout(function () {reject('수납처리에 실패하였습니다.');},500);
						});
                    } else {
						reject('수납처리에 실패하였습니다.');
					}

                    $("#giroPopResult").remove();
                }
            }, 1000);
        },
        */

    contrySunapInfo: function (data) {
      var params = {
        systemCd: "03", // 인터페이스 구분코드
        jijacheCd: data.selectedRegionCd, // 기부지자체 코드
        enapbuNo: data.elctPayNo, // 전자납부번호
        mngNo: data.mngNo, // 전문대장관리번호
      };

      return new Promise(function (resolve, reject) {
        $s.api.postSubmit(
          "/api/ngdonation/contryNextSunapInfo",
          params,
          function (response) {
            if (!response.result) {
              reject("수납확인 API호출 결과가 없습니다. 고객센터에 문의 부탁드립니다.");
              return false;
            }

            var res = JSON.parse(response.result.api_response);

            resolve(res);
          },
          function (err) {
            reject("수납확인 API호출에 실패하였습니다. 고객센터에 문의 부탁드립니다.");
          }
        );
      });
    },

    // 외부 연계용 giroPay
    giroPayExternal: function () {
      let data = polling.param.bugaData;
      let domain = window.location.protocol + "//" + window.location.host;
      let params = {
        domain: domain,
        jijacheCd: data.selectedRegionCd,
        enapbuNo: data.epayNo,
        userCntrPoint: data.userPoint,
        taxAmt: data.taxAmt,
        presentType: data.presentType,
        userId: data.userId,
        userName: data.userName,
        exGubun: "EXTERNAL",
      };

      return new Promise(function (resolve, reject) {
        donation.giroPopup = window.open("", "popOpen", "width=550, height=830, titlebar=0, toolbar=0, left=300, top=200", "_blank");

        $s.api.postSubmit(
          "/api/ngdonation/giroPay",
          params,
          function (response) {
            $("#do_whitebg").hide();
            if (response.result.errorMsg) {
              reject(response.result.errorMsg);
              return false;
            }

            if (response.isMobile == "Y") {
              //모바일
              if (donation.giroPopup) {
                donation.giroPopup.resizeTo(550, 830);
              }
            } else {
              if (donation.giroPopup) {
                donation.giroPopup.resizeTo(600, 830);
              }
            }

            let form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("target", "popOpen");
            form.setAttribute("action", response.result.popUrl);

            let input = document.createElement("input");
            input.setAttribute("type", "hidden"); // 태그 속성 설정
            input.setAttribute("id", "ENC_DATA");
            input.setAttribute("name", "ENC_DATA");
            input.setAttribute("value", response.result.data);

            form.appendChild(input);

            if ($("#giroPopResult").length <= 0) {
              $("body").append("<input type='hidden' name='giroPopResult' id='giroPopResult' />");
            } else {
              $("#giroPopResult").val("");
            }
            document.body.appendChild(form);
            form.submit();

            if (!donation.giroPopup || donation.giroPopup.closed || typeof donation.giroPopup.closed == "undefined") {
              $s.alert("팝업이 차단되어 있습니다.", function () {
                $s.closeAlert();
                reject("PREVENT_POPUP");
              });
              //reject("PREVENT_POPUP");
            } else {
              let res = {
                params: params,
                form: form,
              };

              resolve(res);
            }
          },
          function (err) {
            $("#do_whitebg").hide();
            $s.confirm2(
              "관련 시스템과 연계 지연으로 기부가 원활하지 않습니다. 다시 시도 하시겠습니까?",
              "확인",
              "취소",
              function () {
                $s.closeAlert();
                donation.contry.giroPayExternal();
              },
              function () {
                $s.closeAlert();
                donation.giroPopup.close();
                //reject('관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.');
                reject("결제완료 후 확인버튼 대신 창을 닫으신 경우(X버튼)에는 기부시스템에 기부내역 반영이 지연될 수 있습니다.(약3일)");
              }
            );
          }
        );
      });
    },
    // 외부 연계용 giroSunapConfirmStartExternal
    giroSunapConfirmStartExternal: function (giroParams, form) {
      return new Promise(function (resolve, reject) {
        var giroPopTimer = setInterval(function () {
          if (donation.giroPopup.closed) {
            form.remove();
            clearInterval(giroPopTimer);
            if ($("#giroPopResult").val() === "SUCCESS") {
              resolve(giroParams);
            } else if ($("#giroPopResult").val() === "FAIL") {
              $s.confirm2(
                "금결원 지로 요청을 계속 하시겠습니까?",
                "확인",
                "취소",
                function () {
                  $s.closeAlert();
                  reject("RE_TRY");
                },
                function () {
                  $s.closeAlert();
                  //reject('관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.');
                  reject("결제완료 후 확인버튼 대신 창을 닫으신 경우(X버튼)에는 기부시스템에 기부내역 반영이 지연될 수 있습니다.(약3일)");
                }
              );
            } else {
              //reject('관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.');
              reject("결제완료 후 확인버튼 대신 창을 닫으신 경우(X버튼)에는 기부시스템에 기부내역 반영이 지연될 수 있습니다.(약3일)");
            }

            $("#giroPopResult").remove();
          }
        }, 1000);
      });
    },
    localSunapConfirm: function (data) {
      let params = {
        locgovCode: data.selectedRegionCd, // 기부지자체 코드
        epayNo: data.elctPayNo, // 전자납부번호
        linkMngKey: data.cntrSn, // 연계일련키 (기부일련번호)
      };

      return new Promise(function (resolve, reject) {
        $s.api.postSubmit(
          "/api/ngdonation/local-sunap-confirm",
          params,
          function (response) {
            if (!response.result) {
              reject("수납확인 API호출 결과가 없습니다. 고객센터에 문의 부탁드립니다.");
              return false;
            }

            var res = response.result;
            resolve(res);
          },
          function (err) {
            reject("수납확인 API호출에 실패하였습니다. 고객센터에 문의 부탁드립니다.");
          }
        );
      });
    }
  },

  sunapSuccess: function (params) {
    return new Promise(function (resolve, reject) {
      $s.api.postSubmit(
        "/api/ngdonation/sunapSuccess",
        params,
        function (response) {
          if (response.result !== undefined && response.result !== null) {
            if (response.result.sunap === "SUCCESS") {
              $(".black-bg.donation-c").addClass("show");
              setTimeout(function () {
                $("#donation-c").focus();
              }, 100);
              resolve(response);
            }
          } else {
            reject("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
          }
        },
        function (error) {
          reject("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
        }
      );
    });
  },

  myPageGiroPay: function (data) {
    var domain = window.location.protocol + "//" + window.location.host;
    var params = {
      domain: domain,
      jijacheCd: data.selectedRegionCd,
      enapbuNo: data.elctPayNo,
      userCntrPoint: data.userPoint,
      taxAmt: data.taxAmt,
      presentType: data.presentType,
    };
    giroPopup = window.open("", "popOpen", "width=550, height=800, titlebar=0, toolbar=0, left=300, top=200");

    return new Promise(function (resolve, reject) {
      $s.api.postSubmit(
        "/api/ngdonation/giroPay",
        params,
        function (response) {
          $("#do_whitebg").hide();
          if (response.result.errorMsg) {
            reject(response.result.errorMsg);
            return false;
          }

          if (response.isMobile == "Y") {
            //모바일
            giroPopup.resizeTo(550, 800);
          } else {
            giroPopup.resizeTo(600, 800);
          }
          var form = document.createElement("form");
          form.setAttribute("method", "post");
          form.setAttribute("target", "popOpen");
          form.setAttribute("action", response.result.popUrl);

          var input = document.createElement("input");
          input.setAttribute("type", "hidden"); // 태그 속성 설정
          input.setAttribute("id", "ENC_DATA");
          input.setAttribute("name", "ENC_DATA");
          input.setAttribute("value", response.result.data);

          form.appendChild(input);
          document.body.appendChild(form);
          payLogMypage.startPayLog(data.elctPayNo).then((response) => (payLogMypage.payLogId = response.data.payLogId));
          form.submit();

          if ($("#giroPopResult").length <= 0) {
            $("body").append("<input type='hidden' name='giroPopResult' id='giroPopResult' />");
          } else {
            $("#giroPopResult").val("");
          }

          donation.myPageGiroSunapConfirmStart(data, params, form, resolve, reject);
        },
        function (err) {
          $("#do_whitebg").hide();
          $s.confirm2(
            "관련 시스템과 연계 지연으로 기부가 원활하지 않습니다. 다시 시도 하시겠습니까?",
            "확인",
            "취소",
            function () {
              $s.closeAlert();
              $("#do_whitebg").show();
              setTimeout(function () {
                donation.myPageGiroPay(data).then(function (response) {
                  resolve(response);
                });
              }, 3000);
            },
            function () {
              $s.closeAlert();
              location.href = "/mypage/cntrList.html";
            }
          );
        }
      );
    });
  },

  myPageGiroSunapConfirmStart: function (data, giroParams, form, resolve, reject) {
    var giroPopTimer = setInterval(function () {
      if (giroPopup.closed) {
        form.remove();
        clearInterval(giroPopTimer);
        if ($("#giroPopResult").val() === "SUCCESS") {
          payLogMypage.endPayLog(payLogMypage.payLogId, PAYMENT_PROCESS.MYPAGE_SUCCESS);
          resolve(giroParams);
        } else if ($("#giroPopResult").val() === "FAIL") {
          payLogMypage.endPayLog(payLogMypage.payLogId, PAYMENT_PROCESS.MYPAGE_FAIL);
          $s.confirm2(
            "기부정보처리 요청을 계속 하시겠습니까?",
            "확인",
            "취소",
            function () {
              $s.closeAlert();
              setTimeout(function () {
                donation
                  .myPageGiroPay(data)
                  .then(function (response) {
                    resolve(response);
                  })
                  .catch(function () {
                    reject("수납처리에 실패하였습니다.");
                  });
              }, 1500);
            },
            function () {
              $s.closeAlert();
              setTimeout(function () {
                reject("수납처리에 실패하였습니다.");
              }, 500);
            }
          );
        } else {
          payLogMypage.endPayLog(payLogMypage.payLogId, PAYMENT_PROCESS.MYPAGE_CLOSE);
          reject("수납처리에 실패하였습니다.");
        }

        $("#giroPopResult").remove();
      }
    }, 1000);
  },

  contryBugaInsertNgTest: function (params) {
    var bugaParams = {
      locgovCode: vm.selectedRegionCd, // 기부지자체 코드
      frstPctAmt: String(vm.cntrAmt).replace(/[^\d]+/g, ""), // 기부금액
      pyrSeCd: "01",
      pyrNo: vm.juminNo1 + vm.juminNo2, // 납부자번호
      pyrNm: vm.userName, // 납부자 이름

      zip: params.zip,
      roadNmCd: params.rnMgtSn,
      addrUdgdYn: params.udrtYn,
      bmno: params.buldMnnm,
      bsno: params.buldSlno,
      dongCd: params.admCd,
      roadNmDaddr: params.roadAddr,

      selectedRegionCd: vm.selectedRegionCd,
      userRegionCd: vm.userRegionCd,
      presentType: vm.presentType,
    };

    return new Promise(function (resolve, reject) {
      $s.api.postSubmit(
        "/api/ngdonation/nextGnrSend",
        bugaParams,
        function (response) {
          if (response.result !== undefined && response.result !== null) {
            var contryBugaInfo = JSON.parse(response.result.api_response);
            contryBugaInfo.mngNo = response.result.mngNo;
            if (contryBugaInfo.result_code == "100") {
              $("#do_whitebg").show();
              /** 금결원 지로 팝업 호출
               *  15초 : 부과등록 연계가 진행되는 예상시간.
               */
              setTimeout(function () {
                resolve(contryBugaInfo);
              }, donation.giroWaitingSec * 1000);
            } else {
              reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.\n 실패원인 : " + sntrBugaInfo.errorMsg);
            }
          } else {
            reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
          }
        },
        function (error) {
          if (error.response.data.message) {
            reject(error.response.data.message);
          } else {
            reject("관련 시스템과 연계 지연으로 기부가 원활하지 않습니다.");
          }
        }
      );
    });
  },

  sunapSuccessExternal: function (params) {
    return new Promise(function (resolve, reject) {
      $s.api.postSubmit(
        "/api/external/sunapSuccess",
        params,
        function (response) {
          if (response.result !== undefined && response.result !== null) {
            if (response.result.sunap === "SUCCESS") {
              $(".black-bg.donation-c").addClass("show");
              setTimeout(function () {
                $("#donation-c").focus();
              }, 100);
              resolve(response);
            }
          } else {
            reject("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
          }
        },
        function (error) {
          reject("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
        }
      );
    });
  },

  sunapSuccessExternal2: function (params) {
    return new Promise(function (resolve, reject) {
      $s.api.postSubmit(
        "/api/external/sunapSuccess2",
        params,
        function (response) {
          if (response.result !== undefined && response.result !== null) {
            if (response.result.sunap === "SUCCESS") {
              $(".black-bg.donation-c").addClass("show");
              setTimeout(function () {
                $("#donation-c").focus();
              }, 100);
              resolve(response);
            }
          } else {
            reject("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
          }
        },
        function (error) {
          reject("수납확인완료처리에 문제가 생겼습니다. 고객센터에 문의 부탁드립니다.");
        }
      );
    });
  },

  closeGiroPop: function () {
    donation.giroPopup.close();
  },
};

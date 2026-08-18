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
    etaxPopOpenExternal: function (mngNo, enapbuNo, taxAmt) {
      donation.modal.show();

      let etaxUrl = "https://etax.seoul.go.kr/jsp/etaxlink.jsp?pay_link=GO&checkable=N&mng_no=" + mngNo + "&tax_info=1@@" + enapbuNo + "@@" + taxAmt;

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
  },
  contry: {
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
            resolve(giroParams);
          }
        }, 1000);
      });
    }
  },

  sunapSuccessExternal2: function (params) {
    return new Promise(function (resolve, reject) {
      $s.api.postSubmit(
        "/api/external/sunapSuccess2",
        params,
        function (response) {
          if (response.result && response.result.sunap) {
			resolve(response);
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

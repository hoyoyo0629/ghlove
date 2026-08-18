const PAYMENT_METHOD = {
  LOCAL: "LOCAL",
  SEOUL: "SEOUL",
};

const PAYMENT_PROCESS = {
  DONATION_PROGRESS: "DONATION_PROGRESS",
  DONATION_SUCCESS: "DONATION_SUCCESS",
  DONATION_FAIL: "DONATION_FAIL",
  DONATION_CLOSE: "DONATION_CLOSE",
  MYPAGE_PROGRESS: "MYPAGE_PROGRESS",
  MYPAGE_SUCCESS: "MYPAGE_SUCCESS",
  MYPAGE_FAIL: "MYPAGE_FAIL",
  MYPAGE_CLOSE: "MYPAGE_CLOSE",
};

class Payment {
  constructor(payMethod, payProcess) {
    this.payMethod = payMethod;
    this.payProcess = payProcess;
  }
}

class PayLog extends Payment {
  constructor(payMethod, payProcess) {
    super(payMethod, payProcess);
    this._payLogId = null;
  }

  get payLogId() {
    return this._payLogId;
  }

  set payLogId(value) {
    if (!value) {
      return;
    }
    this._payLogId = value;
  }

  startPayLog = async (elctrnPayNo) => {
    const params = {
      elctrnPayNo: elctrnPayNo,
      payMethod: this.payMethod,
      payProcess: this.payProcess,
    };

    const response = await fetch($s.config.apiDomain + "/api/payment/start-pay-log", {
      method: "POST",
      headers: Object.assign($s.api.getAuthorizationHeader(), { "Content-Type": "application/json;charset=utf-8" }),
      body: JSON.stringify(params),
    });

    return await response.json();
  };

  endPayLog = async (payLogId, process) => {
    const params = {
      payLogId: payLogId,
      payMethod: this.payMethod,
      payProcess: process,
    };

    if (this.payLogId === undefined || this.payLogId == null) {
      alert("TestPayLog.payLogId empty");
      return false;
    }

    const response = await fetch($s.config.apiDomain + "/api/payment/end-pay-log", {
      method: "POST",
      headers: Object.assign($s.api.getAuthorizationHeader(), { "Content-Type": "application/json;charset=utf-8" }),
      body: JSON.stringify(params),
    });

    return await response.json();
  };
}

const testPayLog = () => {
  const testPayLog1 = new PayLog(PAYMENT_METHOD.LOCAL, PAYMENT_PROCESS.DONATION_PROGRESS);
  //SAMPLE1
  testPayLog1.startPayLog("1122334455").then((response) => (testPayLog1.payLogId = response.data.payLogId));

  setTimeout(() => testPayLog1.endPayLog(testPayLog1.payLogId, PAYMENT_PROCESS.DONATION_SUCCESS), 5000);
  //SAMPLE2
  //testPayLog1.startPayLog("1122334455");
  //testPayLog1.endPayLog();

  //const payLogEtax = new PayLog(PAYMENT_METHOD.SEOUL, PAYMENT_PROCESS.DONATION_PROGRESS);
  //payLogEtax.startPayLog(response.enapbuNo);
  //payLogEtax.endPayLog();
};

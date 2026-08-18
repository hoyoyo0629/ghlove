//4.2.0.0 Fasoo Secure Web Script
//URL Authentication code 192.168.100.248 , 192.168.100.250 , ilovegohyang.go.kr, 152.99.104.8, www.ilovegohyang.go.kr
var arrSerial = Array("73E4A3024C1CEE9083201334167AADB0","F1550F8AEC580B987CED17DBE3A4B0D1","AC9DE863DB7FE1F1C40A89FA5760C961", "78ECE410212FF6F134334E7DD10D365B", "FDCAB96BDC84E6C93E513F56DF44D040");
//Rights
/*
IE 권한
- 인쇄(PRINT), 보안 인쇄(SECURE_PRINT), 복사(COPY), 화면 캡처(SCREENCAPTURE), 보내기(EMAILPAGE)
  소스 코드 보기(VIEWSOURCE), 다른 이름으로 저장(SAVEAS), 대상 저장(SAVELINK), 끌어 놓기(DRAGDROP_NOCOPY)
  그림 저장(SAVEIMAGE), 가상 환경 허용(VMVIEW), 단축키(SECURE_KEY)
*/
var arrRights = Array();

var arrRights_Multi = Array();	//외부에서 권한적용 API 사용 용도

//Exception IP Address
//var arrIPGroup = Array(Array("COPY","192.168.229.1~192.168.229.254"), Array("PRINT","192.168.0.0"), Array("EXTRACT_CONTENT","192.168.229.1~192.168.229.254;192.167.1~192.167.229.254"));
var arrIPGroup = Array();

//Error Report & Installation
var strErrUrl = "/webDrm/html/webguide_main.html";

//Client Version (f_swv.dll)
var ClientSetupVersion = Array(4, 1, 0, 13);

//0:complement code execution if a XHR fails... Default value
//1:err page execution if a XHR fails...
var ExceptionXHR = 1;

//secure page option
//The decision whether to apply in some way
/*
	1: Plugin
	2: non Plugin
	3: Plugin + non Plugin
*/
var ApplyOption = 3;

//--------------------Do not modify---------------------------------
var JSONPaddingResult = 0;
var httpRequest1 = null;
var httpRequest2 = null;
var SuccessPort = 0;
var Session = 0;
var SecureProtocol = 0;
var JsonpTimer = null;
var SerialNum = 0;
var IpgroupNum = 0;
var JSONPaddingPort = 0;
var FireFoxSuccess = 0;
var fasoo_web_info = new FasooWebInfo();

function GoError(win, errNo) {
	if (null !== JsonpTimer) {
		clearTimeout(JsonpTimer);
		JsonpTimer = null;
	}
	win.location.href = strErrUrl + "?errNo=" + errNo;
	return;
}

function GetIERights() {
	var rights = null;
	var iRights = 0;

	try {
		for (iRights = 0; iRights < arrRights.length; iRights++) {
			if (iRights == 0) {
				rights = arrRights[iRights];
			} else {
				rights += "," + arrRights[iRights];
			}
		}
	} catch (e) {
		return null;
	}

	return rights;
}

function GetIPGroupRights(rights, nIndex) {
	var ipgroup = null;
	var iIPGroup = 0;
	var arrIPRange;

	try {
		if (nIndex == 'all') {
			for(iIPGroup = 0; iIPGroup < arrIPGroup.length; i++){
				arrIPRange = arrIPGroup[iIPGroup];
				if(iIPGroup === 0){
					ipgroup = arrIPRange[0] + "-" + arrIPRange[1];
				}else {
					ipgroup = ',' + arrIPRange[0] + "-" + arrIPRange[1];
				}
			}
		} else {
			arrIPRange = arrIPGroup[nIndex];
			ipgroup = arrIPRange[0] + "-" + arrIPRange[1];
		}
	} catch (e) {
		return null;
	}

	return ipgroup;
}

function JSONPCheck() {
	if (JSONPaddingResult == 0) {
		GoError(top, -110);
	}
}

function SecureWebJSONPadding(port) {
	var url;
	var protocol;
	var rights;
	var serial;
	var ipgroup;
	var date = new Date();
	JSONPaddingPort = port;

	if (SecureProtocol == 1) {
		protocol = "https";
	} else {
		protocol = "http";
	}

	rights = arrRights;

	serial = arrSerial;

	ipgroup = GetIPGroupRights(rights, "all");

	var SecureInfo = "FSWJSONP@RIGHTS:" + rights +
		"@IPGROUP:" + ipgroup +
		"@VER:" + ClientSetupVersion.join(",") +
		"@SERIAL:" + serial +
		"@ERRURL:" + strErrUrl +
		"@LOCATION:" + (top.location.href).split('#')[0] +
		"@CAPTION:" + window.top.document.title +
		"@" + date;


	var url = protocol + "://localhost:" + port + "/" + SecureInfo;

	var scriptAdd = document.createElement("script");
	scriptAdd.type = "text/javascript";
	scriptAdd.src = url;
	document.getElementsByTagName("head")[0].appendChild(scriptAdd);
	return;
}

function SecurePageStatusJSONPaddingCallback(arg) {
	if (arg.result == 'checkver fail') {
		GoError(top, -112);
		return;
	}

	if (arg.result == 'sps init fail') {
		GoError(top, -114);
		return;
	}

	if (arg.result == 'client vm') {
		GoError(top, -115);
		return;
	}

	if (arg.result == 'notfound window') {
		GoError(top, -116);
		return;
	}

	if (arg.result == 'pipe err') {
		GoError(top, -117);
		return;
	}

	if (arg.result == 'param fail') {
		GoError(top, -118);
		return;
	}

	if (arg.result == 'Option Set') {
		GoError(top, -120);
		return;
	}

	if (arg.result == 'Option Set Before') {
		GoError(top, -121);
		return;
	}

	if (arg.result == 'checkserial fail') {
		GoError(top, -113);
		return;
	}

	if (arg.result == 'secure success') {
		if (null !== JsonpTimer) {
			clearTimeout(JsonpTimer);
			JsonpTimer = null;
		}
		JSONPaddingResult = 1;
	}
}

function GetOsInfo() {
	var pf = navigator.platform;
	var	ua = navigator.userAgent;
	var result = {};

	// platform
	if (pf == "Win32" || pf == "Win64") result.platform = "WINDOWS";
	else if (pf == "MacIntel") result.platform = "MACOSX";
	else if (pf.search("Linux") >= 0) result.platform = "LINUX";
	else result.platform = "UNKNOWN";

	// version, bit
	if (result.platform == "WINDOWS") {
		if (ua.indexOf("Windows NT 5.1") != -1) { result.version = "5.1"; result.name = "XP"; }
		else if (ua.indexOf("Windows NT 5.2") != -1) { result.version = "5.2"; result.name = "XP_64"; }
		else if (ua.indexOf("Windows NT 6.0") != -1) { result.version = "6.0"; result.name = "VISTA"; }
		else if (ua.indexOf("Windows NT 6.1") != -1) { result.version = "6.1"; result.name = "7"; }
		else if (ua.indexOf("Windows NT 6.2") != -1) { result.version = "6.2"; result.name = "8"; }
		else if (ua.indexOf("Windows NT 6.3") != -1) { result.version = "6.3"; result.name = "8.1"; }
		else if (ua.indexOf("Windows NT 6.4") != -1) { result.version = "6.4"; result.name = "10"; }
		else if (ua.indexOf("Windows NT 10.0") != -1) { result.version = "10.0"; result.name = "10"; }
		else if (ua.indexOf("Windows NT") != -1) {
			result.version = "UNKNOWN"; result.name = "UNKNOWN";
		} else {
			result.version = "UNKNOWN"; result.name = "UNKNOWN";
		}

		if (ua.indexOf("WOW64") != -1 || ua.indexOf("Win64") != -1) result.bit = "64";
		else result.bit = "32";

	} else if (result.platform == "MACOSX") {
		if ((ua.indexOf("Mac OS X 10_5") || ua.indexOf("Mac OS X 10.5")) != -1) { result.version = "10.5"; result.name = "Leopard"; }
		else if ((ua.indexOf("Mac OS X 10_6") || ua.indexOf("Mac OS X 10.6")) != -1) { result.version = "10.6"; result.name = "Snow Leopard"; }
		else if ((ua.indexOf("Mac OS X 10_7") || ua.indexOf("Mac OS X 10.7")) != -1) { result.version = "10.7"; result.name = "Lion"; }
		else if ((ua.indexOf("Mac OS X 10_8") || ua.indexOf("Mac OS X 10.8")) != -1) { result.version = "10.8"; result.name = "Mountain Lion"; }
		else if ((ua.indexOf("Mac OS X 10_9") || ua.indexOf("Mac OS X 10.9")) != -1) { result.version = "10.9"; result.name = "Mavericks"; }
		else if ((ua.indexOf("Mac OS X 10_10") || ua.indexOf("Mac OS X 10.10")) != -1) { result.version = "10.10"; result.name = "Yosemite"; }
		else if (ua.indexOf("Mac OS X 10") != -1) {
			result.version = "UNKNOWN"; result.name = "UNKNOWN";
		} else {
			result.version = "UNKNOWN"; result.name = "UNKNOWN";
		}

		result.bit = "64";

	} else if (result.platform == "LINUX") {
		// TODO
	} else {
		result.version = "UNKNOWN"; result.name = "UNKNOWN";
	}

	return result;
}

function FasooWebInfo() {
	this.browserInfo = new BrowserInfo();

	function BrowserInfo() {

		//private
		var _init = false;
		var _browser_name = "";
		var _chrome_version = 0;
		// Browser Byte
		var CHROME_BYTE = 0x0001;
		var SAFARI_BYTE = 0x0002;
		var FIREFOX_BYTE = 0x0004;
		var IE_BYTE = 0x0008;
		var OPERA_BYTE = 0x0010;
		var EDGE_BYTE = 0x0020;
		var WHALE_BYTE = 0x0040;

		//private
		var InitBrowserInfo = function () {
			var _agent = navigator.userAgent.toLowerCase();

			var browser_byte = 0;
			if (_agent.indexOf("chrome") > -1) {
				browser_byte |= CHROME_BYTE;
				_chrome_version = Number(navigator.userAgent.split("Chrome/")[1].split(".")[0]);
			}
			if (_agent.indexOf("safari") > -1) {
				browser_byte |= SAFARI_BYTE;
			}
			if (_agent.indexOf("firefox") > -1) {
				browser_byte |= FIREFOX_BYTE;
			}
			if (_agent.indexOf("msie") > -1 || _agent.indexOf("trident") > -1) {
				browser_byte |= IE_BYTE;
			}
			if (_agent.indexOf("opr") > -1) {
				browser_byte |= OPERA_BYTE;
			}
			if (_agent.indexOf("edg") > -1) {
				browser_byte |= EDGE_BYTE;
			}
			if (_agent.indexOf("whale") > -1) {
				browser_byte |= WHALE_BYTE;
			}

			if (browser_byte == IE_BYTE) {
				_browser_name = "Msie";
			}
			else if (browser_byte == SAFARI_BYTE) {
				_browser_name = "Safari";
			}
			else if (browser_byte == (CHROME_BYTE | SAFARI_BYTE)) {
				_browser_name = "Chrome";
			}
			else if (browser_byte == FIREFOX_BYTE) {
				_browser_name = "Firefox";
			}
			else if (browser_byte == (OPERA_BYTE | CHROME_BYTE | SAFARI_BYTE)) {
				_browser_name = "Opera";
			}
			else if (browser_byte == (EDGE_BYTE | CHROME_BYTE | SAFARI_BYTE)) {
				_browser_name = "Edge";
			}
			else if (browser_byte == (WHALE_BYTE | CHROME_BYTE | SAFARI_BYTE)) {
				_browser_name = "Whale";
			}
			else {
				_browser_name = "Unknown";
			}
		};

		//public
		this.getBrowserName = function () {
			if (_init == false) {
				InitBrowserInfo();
			}
			return _browser_name;
		};

		this.getChromeVersion = function () {
			if (_init == false) {
				InitBrowserInfo();
			}
			return _chrome_version;
		}
	}
}

// XMLHttpRequest response Data Callback Function
function callbackFunction1() {
	if (httpRequest1.readyState == 4) {
		if (httpRequest1.status == 200) {
			if (SecureProtocol == 1) {
				SuccessPort = 12457;
			} else {
				SuccessPort = 2457;
			}
			Session = httpRequest1.responseText;
			FireFoxSuccess = 1;
		} else if (httpRequest1.status == 405) {
			GoError(top, -111);
		} else if (httpRequest1.status == 400) {
			GoError(top, -112);
		} else if (httpRequest1.status == 404) {
			GoError(top, -113);
		} else if (httpRequest1.status == 401) {
			GoError(top, -114);
		} else if (httpRequest1.status == 409) {
			GoError(top, -115);
		} else if (httpRequest1.status == 410) {
			GoError(top, -116);
		} else if (httpRequest1.status == 900) {
			GoError(top, -120);
		} else if (httpRequest1.status == 901) {
			GoError(top, -121);
		} else {
			if (fasoo_web_info.browserInfo.getBrowserName() == "Firefox" && httpRequest1.status == 0) {
				// Nothing
			} else {
				GoError(top, -110);
			}
		}

		delete httpRequest1;
	}

	return;
}

function callbackFunction2() {
	if (httpRequest2.readyState == 4) {
		if (httpRequest2.status == 200) {
			if (SecureProtocol == 1) {
				SuccessPort = 14620;
			} else {
				SuccessPort = 4620;
			}
			Session = httpRequest2.responseText;
			FireFoxSuccess = 1;
		} else if (httpRequest2.status == 405) {
			GoError(top, -111);
		} else if (httpRequest2.status == 400) {
			GoError(top, -112);
		} else if (httpRequest2.status == 404) {
			GoError(top, -113);
		} else if (httpRequest2.status == 401) {
			GoError(top, -114);
		} else if (httpRequest2.status == 409) {
			GoError(top, -115);
		} else if (httpRequest2.status == 410) {
			GoError(top, -116);
		} else if (httpRequest2.status == 900) {
			GoError(top, -120);
		} else if (httpRequest2.status == 901) {
			GoError(top, -121);
		} else {
			if (fasoo_web_info.browserInfo.getBrowserName() == "Firefox" && httpRequest1.status == 0) {
				// Nothing
			} else {
				GoError(top, -110);
			}
		}

		delete httpRequest2;
	}
	return;
}

// XMLHttpRequest Object getter Function
function getXMLHttpRequest() {
	//return new window.XDomainRequest();
	if (window.ActiveXObject) {
		try {
			return new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e1) {
				return null;
			}
		}
	} else if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else {
		return null;
	}
}

function SecureWebPort(xmlhttprequest, port, callback, serial, Rights, IpGroupRights) {

	var date = new Date();
	var url = null;
	var protocol = null;

	if (SecureProtocol == 1) {
		protocol = "https";
	} else {
		protocol = "http";
	}

	var url = protocol + "://localhost:" + port + "/" +
		"ENABLE@RIGHTS:" + Rights +
		"@IPGROUP:" + IpGroupRights +
		"@VER:" + ClientSetupVersion.join(",") +
		"@SERIAL:" + serial +
		"@ERRURL:" + strErrUrl +
		"@LOCATION:" + (top.location.href).split('#')[0] +
		"@CAPTION:" + window.top.document.title +
		"@" + date;

	try {
		xmlhttprequest.onreadystatechange = callback;
		xmlhttprequest.open("GET", url, true);
		xmlhttprequest.send(null);
	} catch (e) {
		return 0;
	}
	return 1;
}

function SecureWebPageLocalAgent() {

	var metaFSW = document.createElement("meta");
	metaFSW.name = "securepage";
	metaFSW.content = "fsw";
	document.getElementsByTagName("head")[0].appendChild(metaFSW);

	httpRequest1 = getXMLHttpRequest();
	httpRequest2 = getXMLHttpRequest();

	if (httpRequest1 == null || httpRequest2 == null) {
		GoError(top, -111);
		return;
	}

	var rights = null;
	var ipgroup = null;
	var serial = null;

	if (fasoo_web_info.browserInfo.getBrowserName() == "Msie") {
		rights = GetIERights();
		ipgroup = GetIPGroupRights(rights, "all");

		serial = arrSerial.join(",") + ",";
	}

	var mainPort = 0;
	var subPort = 0;

	if (top.location.href.indexOf("https://") != -1) {
		SecureProtocol = 1;
		mainPort = 12457;
		subPort = 14620;
	} else {
		SecureProtocol = 0;
		mainPort = 2457;
		subPort = 4620;
	}

	var nSuccess = SecureWebPort(httpRequest1, mainPort, callbackFunction1, serial, rights, ipgroup);

	if (nSuccess != 0) {
		if (ExceptionXHR == 0) {
			SecureWebPort(httpRequest2, subPort, callbackFunction2, serial, rights, ipgroup);
		}
	} else {
		SecureWebJSONPadding(mainPort);
		JsonpTimer = setTimeout(JSONPCheck, 3000);
		return;
	}

	window.onbeforeunload = function () {
		if (Session != 0 && SuccessPort != 0) {
			httpRequestDisable = getXMLHttpRequest();

			var splitData = Session.split(",");
			var sessionNum = splitData[0];
			var releasePID = splitData[1];

			var sendJsonString = '{"contentType":"FSWAJAX","controlType":"DISABLE","session":' + sessionNum + ',"releasePid":' + releasePID + '}';
			var url;
			if (SecureProtocol == 1) {
				url = "https://localhost:" + SuccessPort + "/" + sendJsonString;
			} else {
				url = "http://localhost:" + SuccessPort + "/" + sendJsonString;
			}

			httpRequestDisable.open("GET", url, false);
			httpRequestDisable.send(null);
			delete httpRequestDisable;
		}
		return;
	};
}

function SecureWebActiveX(errOption) {

	try{
	var webdmModule = document.createElement("object");
	webdmModule.id = "WebDM";
	webdmModule.style.display = "none";
	webdmModule.classid = "clsid:4FEBA4F2-1906-44BB-B269-7B5A4AE8CC6D";
	document.getElementsByTagName('head')[0].appendChild(webdmModule);

	var swvModule = document.createElement("object");
	swvModule.id = "f_swv";
	swvModule.style.display = "none";
	swvModule.classid = "clsid:30A39E90-1C8A-4EA4-8733-8C3DD0818281";
	document.getElementsByTagName('head')[0].appendChild(swvModule);
	} catch(e){
		if (errOption == 0) {
			GoError(top, -2);
		}
		return -1;
	}

	//--------  f_swv.dll -------------------------
	try {
		var iVersion;
		var InstalledClientVer;
		for (iVersion = 0; iVersion < ClientSetupVersion.length; iVersion++) {
			InstalledClientVer = document.getElementById("f_swv").GetClientVersion(iVersion);

			if (InstalledClientVer < ClientSetupVersion[iVersion]) {
				GoError(top, -1);
				return 0;
			} else if (InstalledClientVer > ClientSetupVersion[iVersion]) {
				break;
			}
		}
	} catch (e) {
		if (errOption == 0) {
			GoError(top, -2);
		}
		return -1;
	}

	//--------  f_webdm.dll -------------------------
	try {
		var nInitialize = 1;
		var iSerial = 0;
		nInitialize = document.getElementById("WebDM").SetLocationRef(document.location.href);

		do {
			nInitialize = document.getElementById("WebDM").Initialize(arrSerial[iSerial++]);
		} while (nInitialize != 0 && iSerial < arrSerial.length);

		if (nInitialize != 0) {
			GoError(top, nInitialize);
			return;
		}

		document.getElementById("WebDM").SetErrPageMode(strErrUrl, 0, 0, 0);
		nInitialize = document.getElementById("WebDM").ProtectWebPage(1);

		if (nInitialize != 1) {
			GoError(top, nInitialize);
			return 0;
		}
	} catch (e) {
		if (errOption == 0)
			GoError(top, -3);
		return -1;
	}

	//--------  f_webdm.dll  -------------------------
	var iRights = 0;
	for (iRights = 0; iRights < arrRights.length; iRights++) {
		try {
			document.getElementById("WebDM").Enable(arrRights[iRights]);
		} catch (e) {
			if (errOption == 0)
				GoError(top, -4);
			return -1;
		}
	}

	//--------  f_webdm.dll  -------------------------
	var iIPGroup = 0;
	for (iIPGroup = 0; iIPGroup < arrIPGroup.length; iIPGroup++) {
		var arrIPRange = arrIPGroup[iIPGroup];
		var bRightsEnable = 1;

		for (iRights = 0; iRights < arrRights.length; iRights++) {
			if (arrIPRange[0] == arrRights[iRights])
				bRightsEnable = 0;
		}

		try {
			document.getElementById("WebDM").ProtectWithIP(arrIPRange[0], arrIPRange[1], bRightsEnable);
		} catch (e) {
			if (errOption == 0)
				GoError(top, -5);
			return -1;
		}
	}

	window.onunload = function ReleaseWeb() {
		document.getElementById("WebDM").ProtectWebPage(0);
	};

	return 0;
}


function isActivexEnabled() {
	var supported = null;
	var webdmSupported = null;

	try {
		supported = !!new ActiveXObject("htmlfile");
	} catch (e) {
		supported = false;
	}

	try {
		webdmSupported = !!new ActiveXObject("F_webdm.xWebDM");
	} catch (e) {
		webdmSupported = false;
	}

	if (supported == false && webdmSupported == false)
		return false;
	else
		return true;
}


function FswExcept(bExcept) {
	document.getElementById("WebDM").nExceptCtrl = bExcept;
}


function ProtectSecurePage(PluginOption) {
	var osInfo = GetOsInfo();
	var osName = osInfo.name.toLowerCase();

	if (osName != 'xp' &&
		osName != 'vista' &&
		osName != '7' &&
		osName != '8' &&
		osName != '8.1' &&
		osName != '10' &&
		(osName == 'xp_64' && osInfo.bit == '64')) {
		GoError(top, -7);
		return;
	}

	if (navigator.userLanguage != 'en-US' &&
		navigator.userLanguage != 'ko-KR' &&
		navigator.userLanguage != 'en' &&
		navigator.userLanguage != 'ko') {
		GoError(top, -8);
		return;
	}

	if (fasoo_web_info.browserInfo.getBrowserName() == "Msie") {
		var version = "";
		if (navigator.userAgent.toLowerCase().indexOf("msie") == -1) {
			// IE 11 이상
			var regex = navigator.userAgent.toLowerCase().match(/(rv:)\/?\s*(\.?\d+(\.\d+)*)/i);
			if (regex != null) {
				version = regex[2];
			}
		}
		else {
			// IE 8 ~ 10
			var regex = navigator.userAgent.toLowerCase().match(/(trident)\/?\s*(\.?\d+(\.\d+)*)/i);
			if (regex != null) {
				version = regex[2];
			}
		}

		if (PluginOption == 1) {
			SecureWebActiveX(0);
		}
		else if (PluginOption == 2) {
			if (version == "")
				GoError(top, -10);

			SecureWebPageLocalAgent();
		} else {
			if (isActivexEnabled() == false) {
				if (version == "")
					GoError(top, -10);

				SecureWebPageLocalAgent();
			} else {
				if (SecureWebActiveX(1) != 0) {
					SecureWebPageLocalAgent();
				}
			}
		}
	}
	else {
		GoError(top, -6);
	}
	return;
}

ProtectSecurePage(ApplyOption);
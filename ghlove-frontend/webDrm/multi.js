//4.2.0.0 Fasoo Secure Web Script
//URL Authentication code 192.168.100.248 , 192.168.100.250 , ilovegohyang.go.kr, 152.99.104.8, www.ilovegohyang.go.kr
var arrSerial_CrossBrowser = Array("F178C6A3F384336CEC6E4C48A5A4BF25","C3668E8126622526D1312A9C75151240","5AC204617E345DDBA376D6AA7310FCD2", "9698C6B5D2834AFB2CE44380AF46D4A3", "5F61A48A424A696A60E7949DD6FF5476");
/*
권한 타입
0 : IE 기반으로 정의된 권한 사용
1.0 : Multi Browser를 기반으로 새롭게 정의된 권한 사용
*/
//var ProtocolVersion = "0";
var ProtocolVersion = "1.0";

//Rights
/*
ProtocolVersion = 0 일 경우, 아래 권한으로 설정
- 인쇄(PRINT), 보안 인쇄(SECURE_PRINT), 복사(COPY), 화면 캡처(SCREENCAPTURE), 
  소스 코드 보기(VIEWSOURCE), 다른 이름으로 저장(SAVEAS), 대상 저장(SAVELINK), 끌어 놓기(DRAGDROP_NOCOPY)
  그림 저장(SAVEIMAGE), 가상 환경 허용(VMVIEW), 단축키(SECURE_KEY)

ProtocolVersion = 1.0 일 경우, 아래 권한으로 설정
- 소스코드(SOURCECODE), 컨텐츠 추출(EXTRACT_CONTENT), 인쇄(PRINT), 워터마크 인쇄(WATERMARK_PRINT), 캡처(CAPTURE), 가상환경 허용(VMVIEW)
*/
var arrRights_Multi = Array();

var arrRights = Array();	//외부에서 권한적용 API 사용 용도

//Exception IP Address
//var arrIPGroup = Array(Array("COPY","192.168.1.1~192.168.21.70"), Array("PRINT","192.168.0.0"));
var arrIPGroup = Array();

//Error Report & Installation
var strErrUrl = "/webDrm/html/webguide_main.html";

//Client Version (f_swv.dll)
var ClientSetupVersion_Multi = "4,1,0,13";

//secure page option
//The decision whether to apply in some way
/*
	1: Plugin 
	2: non Plugin
	3: Plugin + non Plugin
*/
var ApplyOption = 3;

//--------------------Do not modify---------------------------------
var ScriptVersion = "4.2.0.0";
var FASOO_CHROME_SUPPORT_VERSION = 71;
var CHROME_CORS_HTTP_AJAX_DENIED_VERSION = 93;	//Chrome http CORS Update version
var Agent = navigator.userAgent.toLowerCase();
var JSONPaddingResult = 0;
var httpRequest1 = null;
var httpRequest2 = null;
var SecureProtocol = 0;
var JsonpTimer = null;
var SerialNum = 0;
var IpgroupNum = 0;
var JSONPaddingPort = 0;
var FireFoxSuccess = 0;
var fasoo_web_info = new FasooWebInfo();
var fasoo_rights = 0;

var fasoo_right_print = 		0x0001;
var fasoo_right_secure_print = 	0x0002;
var fasoo_right_saveas = 		0x0004;
var fasoo_right_dragdrop = 		0x0008;
var fasoo_right_viewsource = 	0x0010;
var fasoo_right_capture = 		0x0020;
var fasoo_right_saveimage = 	0x0040;
var fasoo_right_vmview = 		0x0080;
var fasoo_right_copy = 			0x0100;
var fasoo_right_savelink = 		0x0200;
var fasoo_right_securekey =		0x0400;

var fasoo_multi_right_print = 			0x00010000;
var fasoo_multi_right_watermarkprint = 	0x00020000;
var fasoo_multi_right_capture = 		0x00040000;
var fasoo_multi_right_vmview = 			0x00080000;
var fasoo_multi_right_extractcontent = 	0x00100000;
var fasoo_multi_right_sourcecode = 		0x00200000;

function GoError(win, errNo) {
	if (null !== JsonpTimer) {
		clearTimeout(JsonpTimer);
		JsonpTimer = null;
	}
	win.location.href = strErrUrl + "?errNo=" + errNo;
	return;
}

function GetIPGroupRights(rights, nIndex) {
	var ipgroup = null;
	var iIPGroup = 0;
	var arrIPRange;

	try {
		if (nIndex == 'all') {
			ipgroup = arrIPGroup;
		} else {
			arrIPRange = arrIPGroup[nIndex];
			ipgroup = arrIPRange[0] + "-" + arrIPRange[1];
		}
	} catch (e) {
		return null;
	}

	return ipgroup;
}


function GetFasooRights(){
	fasoo_rights = 0;
	if (0 == ProtocolVersion) {
		if (null != arrRights_Multi) {
			arrRights_Multi.forEach(function (right) {
				if (-1 != right.indexOf("PRINT")) {
					fasoo_rights |= fasoo_right_print;
				} else if (-1 != right.indexOf("SECURE_PRINT")) {
					fasoo_rights |= fasoo_right_secure_print;
				} else if (-1 != right.indexOf("COPY")) {
					fasoo_rights |= fasoo_right_copy;
				} else if (-1 != right.indexOf("SCREENCAPTURE")) {
					fasoo_rights |= fasoo_right_capture;
				} else if (-1 != right.indexOf("VIEWSOURCE")) {
					fasoo_rights |= fasoo_right_viewsource;
				} else if (-1 != right.indexOf("SAVEAS")) {
					fasoo_rights |= fasoo_right_saveas;
				} else if (-1 != right.indexOf("SAVELINK")) {
					fasoo_rights |= fasoo_right_savelink;
				} else if (-1 != right.indexOf("DRAGDROP_NOCOPY")) {
					fasoo_rights |= fasoo_right_dragdrop;
				} else if (-1 != right.indexOf("SAVEIMAGE")) {
					fasoo_rights |= fasoo_right_saveimage;
				} else if (-1 != right.indexOf("VMVIEW")) {
					fasoo_rights |= fasoo_right_vmview;
				} else if (-1 != right.indexOf("SECURE_KEY")) {
					fasoo_rights |= fasoo_right_securekey;
				}
			});
		}
	} else if ("1.0" == protocolVersion){
		if (null != arrRights_Multi) {
			arrRights_Multi.forEach(function (right) {
				if (-1 != right.indexOf("SOURCECODE")) {
					fasoo_rights |= fasoo_multi_right_sourcecode;
				} else if (-1 != right.indexOf("EXTRACT_CONTENT")) {
					fasoo_rights |= fasoo_multi_right_extractcontent;
				} else if (-1 != right.indexOf("PRINT")) {
					fasoo_rights |= fasoo_multi_right_print;
				} else if (-1 != right.indexOf("WATERMARK_PRINT")) {
					fasoo_rights |= fasoo_multi_right_watermarkprint;
				} else if (-1 != right.indexOf("CAPTURE")) {
					fasoo_rights |= fasoo_multi_right_capture;
				} else if (-1 != right.indexOf("VMVIEW")) {
					fasoo_rights |= fasoo_multi_right_vmview;
				}
			});
		}
	}
	return fasoo_rights;
}

function FasooClearSelection() {
	// clear selection
	if (window.getSelection) {
		if (window.getSelection().empty) {  // Chrome
			window.getSelection().empty();
		} else if (window.getSelection().removeAllRanges) {  // Firefox
			window.getSelection().removeAllRanges();
		}
	} else if (document.selection) {  // IE?
		document.selection.empty();
	}
}

function FasooProtectEvent(rights) {

	fasoo_rights = rights || 0;	

	// EXTRACT | DRAG_DROP & COPY
	var extract = rights & 0x00100000;
	var drag_copy = (rights & 0x0100) & (rights & 0x008);
	if (!extract && !drag_copy) {

		// add event
		document.addEventListener("selectstart", e => {
			var extract = fasoo_rights & 0x00100000;
			var drag_copy = (fasoo_rights & 0x0100) && (fasoo_rights & 0x008);
			if (!extract && !drag_copy) {
				e.preventDefault();
				e.stopPropagation();
			}
		});

		// clear selection
		setInterval('FasooClearSelection()', 500);
	}
}
function JSONPCheck() {
	if (JSONPaddingResult == 0) {
		GoError(top, -110);
	}
}

function AgentCheck() {
	if (FireFoxSuccess == 0) {
		GoError(top, -110);
	}
}

function SecureWebJSONPadding(port) {
	var url;
	var protocol;
	var rights;
	var serial;
	var ipgroup;
	JSONPaddingPort = port;

	if (SecureProtocol == 1) {
		protocol = "https";
	} else {
		protocol = "http";
	}

	rights = arrRights_Multi;

	serial = arrSerial_CrossBrowser;

	ipgroup = GetIPGroupRights(rights, "all");

	var SecureInfo = {
		"contentType": "FSWJSONP",
		"controlType": "ENABLE",
		"rights": rights, 
		"ipGroup": ipgroup,
		"ver": ClientSetupVersion_Multi,
		"serial": serial,
		"errUrl": strErrUrl,
		"location": location.href.split('#')[0],
		"caption": window.top.document.title,
		"protocolVer": ProtocolVersion,
		"scriptVersion": ScriptVersion
	}

	var url = protocol + "://localhost:" + port + "/" + JSON.stringify(SecureInfo);

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

		if (arg.rights) {
			FasooProtectEvent(arg.rights);
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
			if (_agent.indexOf("edg/") > -1) {
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
			
			var response = httpRequest1.responseText;
			var divide = response.split(',');
			if (null != divide || divide.length > 3) {
				var rights = Number(divide[2]);
				FasooProtectEvent(rights);
			}

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
			
			var response = httpRequest2.responseText;
			var divide = response.split(',');
			if (null != divide || divide.length > 3) {
				var rights = Number(divide[2]);
				FasooProtectEvent(rights);
			}

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

function ConnectWebSocket(code, msg) {
	if (code === 1000) {
		// WebSocket은 정상 연결 완료
	} else if (code === 1006) {
		$s.error("CONNECTION ERROR, WEBSOCKET SERVER NOT FOUND" + code + msg);
		GoError(top, -110);
	} else { // UNKNOWN ERROR
		$s.error("UNKNOWN ERROR" + code + msg);
		GoError(top, -116);
	}
}

function ResultWebSocket(msg) {
	if (!msg) {
		$s.error("msg undefined");
		GoError(top, -113);
	}

	if (!msg.resultCode || !msg.msg) {
		$s.error("msg element is undefined");
		GoError(top, -113);
	}

	if (msg.resultCode === 200) {
		if (null != msg.rights && typeof(msg.rights) === "number") {
			FasooProtectEvent(Number(msg.rights));
		}
	} else if (msg.resultCode === 405) {
		GoError(top, -111);
	} else if (msg.resultCode === 400) {
		GoError(top, -112);
	} else if (msg.resultCode === 404) {
		GoError(top, -113);
	} else if (msg.resultCode === 401) {
		GoError(top, -114);
	} else if (msg.resultCode === 409) {
		GoError(top, -115);
	} else if (msg.resultCode === 410) {
		GoError(top, -116);
	} else if (msg.resultCode === 900) {
		GoError(top, -120);
	} else if (msg.resultCode === 901) {
		GoError(top, -121);
	} else {
		GoError(top, -110);
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

	
	var secureInfo = {
		"contentType": "FSWAJAX",
		"controlType": "ENABLE",
		"rights": Rights,
		"ipGroup": IpGroupRights,
		"ver": ClientSetupVersion_Multi,
		"serial": serial,
		"errUrl": strErrUrl,
		"location": location.href.split('#')[0],
		"caption": window.top.document.title,
		"protocolVer": ProtocolVersion,
		"scriptVersion": ScriptVersion,
		"date": date
	};

	var url = protocol + "://localhost:" + port + "/" + JSON.stringify(secureInfo);

	try {
		xmlhttprequest.onreadystatechange = callback;
		xmlhttprequest.open("GET", url, true);
		xmlhttprequest.send(null);
	} catch (e) {
		$s.error(e);
		return 0;
	}
	return 1;
}

function WebsocketRequest(port, serial, Rights, IpGroupRights) {
	var date = new Date();

	var secureInfo = {
		"contentType": "FSWAJAX",
		"controlType": "ENABLE",
		"rights": Rights,
		"ipGroup": IpGroupRights,
		"ver": ClientSetupVersion_Multi,
		"serial": serial,
		"errUrl": strErrUrl,
		"location": location.href.split('#')[0],
		"caption": window.top.document.title,
		"protocolVer": ProtocolVersion,
		"scriptVersion": ScriptVersion,
		"date": date
	};
	
	var url = "ws://localhost:" + port;

	var webSocket = new WebSocket(url);
	webSocket.addEventListener('open', (event) => {
		$s.log(event);
		try {
			$s.log(encodeURI(JSON.stringify(secureInfo)));
			webSocket.send(encodeURI(JSON.stringify(secureInfo)));
		} catch (e) {
			$s.error(e);
		}
	});

	webSocket.addEventListener('message', function (event) {
		$s.log(event);
		const msg = JSON.parse(decodeURIComponent(event.data));
		$s.log(msg); // {"resultCode":XXX,"msg":""}
		ResultWebSocket(msg);
		//aWebSocket.close();
	});

	webSocket.onclose = function (event) {
		$s.log(event);
		ConnectWebSocket(event.code, event.reason);
	};

	webSocket.onerror = function (event) {
		$s.error(event);
		GoError(top, -116);
	};
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

	rights = arrRights_Multi;
	ipgroup = GetIPGroupRights(rights, "all");

	serial = arrSerial_CrossBrowser;

	var mainPort = 0;
	var subPort = 0;
	var request_type = 0; // 1 = Ajax, 2 = Websocket

	if (location.href.indexOf("https://") != -1) {
		SecureProtocol = 1;
		request_type = 1;
		mainPort = 12457;
		subPort = 14620;
	} else {
		SecureProtocol = 0;
		if (fasoo_web_info.browserInfo.getChromeVersion() < CHROME_CORS_HTTP_AJAX_DENIED_VERSION) {
			request_type = 1;
			mainPort = 2457;
			subPort = 4620;
		} else {
			request_type = 2;
			mainPort = 2458;
		}
	}

	if (request_type == 1) {
		var nSuccess = SecureWebPort(httpRequest1, mainPort, callbackFunction1, serial, rights, ipgroup);
		if (nSuccess != 1) {
			nSuccess = SecureWebPort(httpRequest2, subPort, callbackFunction2, serial, rights, ipgroup);
			if (nSuccess != 1) {
				SecureWebJSONPadding(mainPort);
				JsonpTimer = setTimeout(JSONPCheck, 3000);
				return;
			}
		}

		if (fasoo_web_info.browserInfo.getBrowserName() == "Firefox") {
			setTimeout(AgentCheck, 15000);
		}
	}
	else if (request_type == 2) {
		WebsocketRequest(mainPort, serial, rights, ipgroup);
	}
}

function ProtectSecurePage(PluginOption) {
	var osInfo = GetOsInfo();
	var osName = osInfo.name.toLowerCase();

	if (osInfo.platform != "WINDOWS"){
		GoError(top, -7);
		return;
	}

	if (navigator.language != 'en-US' &&
		navigator.language != 'ko-KR' &&
		navigator.language != 'en' &&
		navigator.language != 'ko') {
		GoError(top, -8);
		return;
	}


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

	if (fasoo_web_info.browserInfo.getBrowserName() == "Chrome" ||
		fasoo_web_info.browserInfo.getBrowserName() == "Opera" ||
		fasoo_web_info.browserInfo.getBrowserName() == "Edge" ||
		fasoo_web_info.browserInfo.getBrowserName() == "Whale") {

		if (fasoo_web_info.browserInfo.getChromeVersion() < FASOO_CHROME_SUPPORT_VERSION) {
			GoError(top, -7);
			return;
		}
		SecureWebPageLocalAgent();
	}
	else if (fasoo_web_info.browserInfo.getBrowserName() == "Firefox") {
		SecureWebPageLocalAgent();
	}
	else {
		GoError(top, -6);
	}
	return;
}

ProtectSecurePage(ApplyOption);
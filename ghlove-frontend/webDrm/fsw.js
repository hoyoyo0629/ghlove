//4.2.0.0 Fasoo Secure Web Script
// ie.js, multi.js 실제 경로로 변경
var ieScriptLocation = "/webDrm/ie.js?" + new Date().getTime();
var multiScriptLocation = "/webDrm/multi.js";

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
			if (_agent.indexOf("edg") > -1 && _agent.indexOf("edge") == -1) {
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

		this.getChromeVersion = function(){
			if (_init == false) {
				InitBrowserInfo();
			}
			return _chrome_version;
		}
	}
}

// local일 경우 제외
function importScript() {
	if (typeof($s) != 'undefined') {
		if ($s.config.domain != 'local') {
			var browser_info_ = new FasooWebInfo();

			if(browser_info_.browserInfo.getBrowserName() === "Msie"){
			    var insert = document.createElement("script");
			    insert.type = "text/javascript"; 
			    insert.src = ieScriptLocation;
			    document.getElementsByTagName("head")[0].appendChild(insert);
			} else {
			    var insert = document.createElement("script");
			    insert.type = "text/javascript"; 
			    insert.src = multiScriptLocation;
			    document.getElementsByTagName("head")[0].appendChild(insert);
			}
		}		
	} else {
		setTimeout(importScript, 1000);
	}
}

importScript();
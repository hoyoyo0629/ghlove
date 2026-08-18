
var Chrome_byte = 0x0001;
var Safari_byte = 0x0002;
var Firefox_byte = 0x0004;
var IE_byte = 0x0008;
var Opera_byte = 0x0010;
var Edge_byte = 0x0020;
var Whale_byte = 0x0040;

function GetOSName()
{
    var OSName="Can't find OS Information"; 
    
    //The below few line of code will find the OS name 
    if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows"; 
    if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS"; 
    if (navigator.appVersion.indexOf("X11")!=-1) OSName="UNIX"; 
    if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux"; 
   
    return OSName;
}

function GetOSVersion()
{
    var OSVer=""; 
    if (navigator.userAgent.indexOf("Mac OS X 10.4")!=-1) OSVer="Tiger"; 
    if (navigator.userAgent.indexOf("Mac OS X 10.5")!=-1) OSVer="Leopard"; 
    if (navigator.userAgent.indexOf("Mac OS X 10.6")!=-1) OSVer="Snow Leopard";
    if (navigator.appVersion.indexOf("NT 5.1")!=-1) OSVer="XP";
    if (navigator.appVersion.indexOf("NT 6.0")!=-1) OSVer="Vista";
    if (navigator.appVersion.indexOf("NT 6.1")!=-1) OSVer="7";    
    if (navigator.appVersion.indexOf("NT 6.2")!=-1) OSVer="8";    
    if (navigator.appVersion.indexOf("NT 6.3")!=-1) OSVer="8.1";    
    if (navigator.appVersion.indexOf("NT 7.0")!=-1) OSVer="9";    
    if (navigator.appVersion.indexOf("NT 10.0")!=-1) OSVer="10";    
     

    return OSVer;
}

function GetWindowsBitType()
{
    var ieBitVersion = "64";
    
    var osVersion = GetOSVersion();    
    if (osVersion == "XP" || osVersion == "Vista" || osVersion == "7")
    {
    	ieBitVersion = GetWindowsBitType_Navigator();
    	
    	//test msg
        //alert("Windows8 and less - GetWindowsBitType()-GetWindowsBitType_Navigator() return : " + ieBitVersion);
		return ieBitVersion;        
    }    

	try
	{
	    var bWindows64 = document.getElementById("f_swv").IsWin64();
	    
	    //test msg
	    //alert("Windows 64bit ?: " + bWindows64);
	    
	    if (bWindows64 != true)
	    {
	        ieBitVersion = "32";
	        
    	    //test msg
            //alert("Windows 8 more than, 64bit - GetWindowsBitType() return : " + ieBitVersion);	    	        
	    }
	    else
	    {
	        //test msg
	        //alert("Windows 8 more than, 64bit - GetWindowsBitType() return : " + ieBitVersion);
	    }

	}
	catch (e)
	{
	    //test msg
		//alert(" try catch()");				
		ieBitVersion = GetWindowsBitType_Navigator();
		
        //test msg
        //alert("Windows  more than, Exception() - GetWindowsBitType() - GetWindowsBitType_Navigator() return : " + ieBitVersion);		        
		return ieBitVersion;
	}

    return ieBitVersion;       
}

function GetWindowsBitType_Navigator()
{

    var Agent = navigator.userAgent.toLowerCase();
    if( Agent.indexOf("wow64") > -1 || Agent.indexOf("win64") > -1 )
        ieBitVersion = "64";
    else
        ieBitVersion = "32";

    return ieBitVersion;       

}

function GetBrowserByte()
{
    var Agent = navigator.userAgent.toLowerCase();
    
    var browser_byte = 0;
    if (Agent.indexOf("chrome") > -1) {
        browser_byte |= Chrome_byte;
    } 
    if (Agent.indexOf("safari") > -1) 
    {
        browser_byte |= Safari_byte;
    }
    if (Agent.indexOf("firefox") > -1)
    {
        browser_byte |= Firefox_byte;
    }
    if (Agent.indexOf("msie") > -1 || Agent.indexOf("trident") > -1) 
    {
        browser_byte |= IE_byte;
    }
    if (Agent.indexOf("opr") > -1)
    {
        browser_byte |= Opera_byte;
    }
    if (Agent.indexOf("edg") > -1)
    {
        browser_byte |= Edge_byte;
    }
    if (Agent.indexOf("whale") > -1)
    {
        browser_byte |= Whale_byte;
    }

    return browser_byte;
}

function GetIEInfo() {
    var nowBrowser = "";    
    var browser_byte = GetBrowserByte()

    if (browser_byte == IE_byte) 
    {
        nowBrowser = "Internet Explorer";

        //Internet Explore 버전
        var ieVersion;
        var Agent = navigator.userAgent.toLowerCase();
        var trident = Agent.match(/trident\/(\d.\d)/i);
        if (trident == null) {
            ieVersion = parseInt(navigator.userAgent.charAt(30));
        }
        else if (trident[1] == "4.0") {
            ieVersion = "8";
        }
        else if (trident[1] == "5.0") {
            ieVersion = "9";
        }
        else if (trident[1] == "6.0") {
            ieVersion = "10";
        }
        else if (trident[1] == "7.0") {
            ieVersion = "11";
        }
        else if (trident[1] == "8.0") {
            //Windows10 IE11
            ieVersion = "11";
        }
        else {
            ieVersion = "?";
        }

        nowBrowser += ieVersion;
    }
    else if (browser_byte == (Chrome_byte | Safari_byte ))        
    {
        nowBrowser = "Chrome";
    }
    else if (browser_byte == Firefox_byte)
    {
        nowBrowser = "Firefox";
    }
    else if (browser_byte == (Opera_byte | Chrome_byte | Safari_byte))
    {
        nowBrowser = "Opera";
    }
    else if (browser_byte == (Edge_byte | Chrome_byte | Safari_byte))
    {
        nowBrowser = "Edge";
    }
    else if (browser_byte == (Whale_byte | Chrome_byte | Safari_byte))
    {
        nowBrowser = "Whale";
    }
    else 
    {
        nowBrowser = "NOT_SUPPORT";
    }

    return nowBrowser;
}
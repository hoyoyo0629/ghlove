
var ExpDate = new Date();

function getCookieVal (offset) 
{
    var endstr = document.cookie.indexOf (";", offset);

    if (endstr == -1) 
        endstr = document.cookie.length;

    return unescape(document.cookie.substring(offset, endstr));
}

function GetCookie (name) 
{
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;

    while (i < clen) 
    { 
        var j = i + alen;

        if (document.cookie.substring(i, j) == arg)
        {
            return getCookieVal (j);
        }

        i = document.cookie.indexOf(" ", i) + 1;

        if (i == 0) break; 
      
    } //while close

    return null;
}

function SetCookie (name, value) 
{
    var argv = SetCookie.arguments;
    var argc = SetCookie.arguments.length;

    var expires = (2 < argc) ? argv[2] : null;
    var path = (3 < argc) ? argv[3] : null;
    var domain = (4 < argc) ? argv[4] : null;
    var secure = (5 < argc) ? argv[5] : false;

    //var msg;
    //msg = "SetCookie (name, value) => " + name + " : " + value + ", expires : " + expires + ", path : " + path + ", domain : " + domain;
    //alert(msg);
      
    document.cookie = name + "=" + escape (value) +
      ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
      ((path == null) ? "" : ("; path=" + path)) +
      ((domain == null) ? "" : ("; domain=" + domain)) +
      ((secure == true) ? "; secure" : "");
}

function SetCookieVal (name, value) 
{
    pathname = location.pathname;
    var myPath = pathname.substring(0, pathname.lastIndexOf('/')) +'/';

    ExpDate.setTime(ExpDate.getTime() + 1000*60*60);
    SetCookie(name, value, ExpDate, myPath);
}
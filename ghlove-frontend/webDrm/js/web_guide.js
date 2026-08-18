var ClientSetupExeUrl;
var ClientSetupExe32Url = "../Setup/Fasoo_DRM_FSW_for_Default_Set_Wix_20230208_v4.07.2001.exe";
var ClientSetupExe64Url = "../Setup/Fasoo_DRM_FSW_for_Default_Set_Wix_x64_20230208_v4.07.2001.exe";

var companyInfo = "xxx.com";
var telInfo = "02-xxx-xxxx";
var emailInfo = "helpdesk@xxx.com";

var helpdesk_guide_option = 0;	// 0 -- not support, 1 -- support (need to helpdesk information like company, tel, email)

$(document).ready(function(){
	$( "button" ).click(function() {
		$( ".cont_open" ).show();
	});

	$(function() {
		$( ".accordion" ).accordion({
		  heightStyle: "content"
		});
	});
	
	openLook();   
});

function ReplaceHelpdeskGuide(lang)
{
    var helpdesk_guide_menu = document.getElementById("helpdesk_guide_menu");

	if (helpdesk_guide_menu == null)
		return;

	if (helpdesk_guide_option == 0)
	{
		helpdesk_guide_menu.remove();
		return;
	}
	if (lang == "KOR")
		helpdesk_guide_menu.innerHTML = "* 고객 지원 센터 문의";
	else
		helpdesk_guide_menu.innerHTML = "* Contact Customer Support";
		
	var helpdesk_guide = document.getElementById("helpdesk_guide");
	
	if (helpdesk_guide == null)
		return;
   
    if (lang == "KOR")
        helpdesk_guide.innerHTML = '<strong class="text_01">전화 문의</strong> :  ' + telInfo + '<br/> <strong class="text_01">E-MAIL 문의</strong> : <a href="mailto:"' + emailInfo + '">' + emailInfo + '</a> <br/><br/> <span style="color:#999;">장애지원을 위해서 원격 프로그램을 사용하여 지원이 되며,<br/>장애 처리 관리를 위해 접수 시 고객님의 성함, 연락처를 확인합니다.<br/>고객님의 정보는 장애 처리 관리에만 사용되며 상업적 목적의 무단전재, 복사, 배포 등을 절대 금합니다.</span>';
    else
        helpdesk_guide.innerHTML = '<strong class="text_01">Telephone</strong> :  ' + telInfo + '<br/> <strong class="text_01">E-mail</strong> : <a href="mailto:"' + emailInfo + '">' + emailInfo + '</a>    	<br/><br/> <span style="color:#999;">' + companyInfo + ' may use remote desktop connections for troubleshooting,<br/>and may request your name and telephone number.<br/>Your information will be used only for troubleshooting purposes.<br/>  </span>';
}

function ReplaceHelpdeskInfo(lang)
{
	var helpdesk_info = document.getElementById("helpdesk_Info");
	
    if (helpdesk_info == null)
		return;

	if (helpdesk_guide_option == 0)
	{
		helpdesk_info.remove();
		return;
	}

    if (lang == "KOR")
        helpdesk_info.innerHTML = '고객센터: ' + telInfo + ' / <a href="mailto:"' + emailInfo + '">' + emailInfo + '</a>';   
    else
        helpdesk_info.innerHTML = 'HELP : ' + telInfo + '/ <a href="mailto:"' + emailInfo + '">' + emailInfo + '</a>';   
}

function GetFSWErrorNumber()
{
	var param = window.location.href;
	
	var pos1 = param.indexOf("?");
	
	if ( pos1 == -1 )
		return "-1";

	param = param.substr(pos1+1);
	
	pos1 = param.indexOf("errNo=");
	if ( pos1 == -1 )
		return "-1";
	
	pos1 += 6;	// errNo=
	var pos2 = param.indexOf("&", pos1);

	if ( pos2 == 0 )
		return "-1";
		
	if ( pos2 == -1 )
		pos2 = param.length;
	
	if ( pos1 == pos2 )
		return "-1";
		
	return param.substring(pos1, pos2);
}
	
function openLook(){

    if($(".cont_open").css("display")=="none"){
        $(".cont_open").show();
        $(".btn_open span.icon_up").removeClass("icon_up").addClass("icon_down");
        $(".btn_open span.icon_down").removeClass("icon_down").addClass("icon_up");
    }else{
        $(".cont_open").hide();
        $(".btn_open span.icon_down").removeClass("icon_down").addClass("icon_up");
        $(".btn_open span.icon_up").removeClass("icon_up").addClass("icon_down");
    }  
}

function GetOSInfo()
{ 
    var full_os_name= GetOSName() + GetOSVersion() + " " + GetWindowsBitType() + " " + "bit" ; 
    return full_os_name;
}

function SetWindowsInfo()
{
    var textErrorNum = document.getElementById("ErrorNum");
    textErrorNum.innerHTML = "<b>확인 번호 : </b>" + GetFSWErrorNumber();
    
    var textOSIE = document.getElementById("osie");
    textOSIE.innerHTML = GetOSInfo() + "  /  " + GetIEInfo() + "  /  " + GetFSWErrorNumber();
}

//cookieValue, id
function DisplayChangeGuideList(cookieValue, elementId)
{
	if(cookieValue != undefined && cookieValue != null)
	{
		if (GetCookie(cookieValue) == 'true')
		{	
			//Helpdesk 문의 가이드 호출	 
			if(elementId == undefined || elementId == null)
			{
				elementId = "guideList_1";
			}			
			var txtBtn2 = document.getElementById(elementId);
			
			if (txtBtn2)
			{
				txtBtn2.click();	
			}
		}
		else
		{
			SetCookieVal (cookieValue, 'true');	
		}
	}
}

function DisplayErrorGuide(cookie,listId,lang)
{
    DisplayChangeGuideList(cookie, listId);   
    SetWindowsInfo();   
    ReplaceHelpdeskGuide(lang);
    ReplaceHelpdeskInfo(lang);   
}
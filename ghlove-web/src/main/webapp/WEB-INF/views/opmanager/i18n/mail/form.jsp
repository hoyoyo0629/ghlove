<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<style>
	.searchResult {
		width: 250px;
		position: absolute;
		background: #fff;
		top: 80px;
		border: 1px solid #ccc;
		border-top: none;
		border-radius: 0px 0px 5px 5px !important;
		z-index: 99;
		padding: 12px 0px;
		font-size: 14px;
	}
	.searchResult .searchList {
		max-height: 300px;
		overflow-y: auto;
		padding: 0px 16px;
	}

	.searchResult ul li {
		display: flex;
		padding: 1px 0;
	}
	.searchResult ul li span {
		flex : 1;
	}
	.searchResult .closeDiv {
		padding: 12px 16px 0px;
		border-top: 1px solid #f5f5f5;
		text-align: right;
	}
	.searchResult .closeDiv span {
		color: #a5a5a5;
		font-size: 12px;
		font-weight: bold;
		cursor: pointer;
	}

	.searchResult .closeDiv span img {
		opacity: 0.4;
    	vertical-align: baseline;
	}
</style>

		<div class="location">
			<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
		</div>

		<!--${op:message('M00269')} 시작-->
		<h3><span>이메일 발송</span></h3>

		<div class="board_write">
			<table class="board_write_table" summary="${op:message('M00269')}" style="position: relative;">
				<caption>이메일 발송</caption>
				<colgroup>
					<col style="width:220px;">
				</colgroup>
				<tbody>
					<tr>
						<td class="label">발송대상</td>
						<td>
							<div class="flex_box gap-08 item-center">
								<div class="flex_box gap-12">
									<div class="input-form">
										<input type="radio" id="auth" name="authTarget"  value="A" />
										<label for="auth">권한별</label>
									</div>
									<div class="input-form">
										<input type="radio" id="seller" name="authTarget"  value="S" />
										<label for="seller">답례품</label>
									</div>
									<div class="input-form">
										<input type="radio" id="each" name="authTarget"  value="E" />
										<label for="each">개별</label>
									</div>
								</div>
							</div>
							<div class="target target_A">
								<c:forEach items="${groupList}" var="g" varStatus="i">
									<input type="checkbox" id="auth_${fn:escapeXml(i.index)}" name="authority" value="${fn:escapeXml(g.authority)}" />
									<label for="auth_${fn:escapeXml(i.index)}"><c:out value="${fn:escapeXml(g.groupName)}"/></label>
								</c:forEach>
							</div>
							<div class="target target_E">
								<div class="flex_box gap-08">
									<input path="searchContent" type="text" name="userName" class="input_txt _filter" title="${op:message('M00021')}" style="width: 250px;" onkeydown='searchKeyDown(this)'/>
									<a href="javascript:;" class="btn_date" onclick="searchUser()">검색</a>
									<div class="searchResult" style="display: none;">
										<div class="searchList">
											<ul></ul>
										</div>
										<div class="closeDiv" >
											<span onclick="hideSearchDiv()">닫기
    											<img src="/content/images/btn/option-close-btn.gif" alt="닫기">
											</span>
										</div>
									</div>
								</div>
								<div  style="width : 250px; margin-top : 15px; font-size: 13px;">
									<ul id="sendUserListTxt"></ul>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">발송 시점</td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<input type="radio" id="now" name="sendType" value="D" />
									<label for="now">즉시</label>
								</div>
								<div class="input-form">
									<input type="radio" id="reservation" name="sendType" value="R" />
									<label for="reservation">지정</label>
								</div>
							</div>
							<div class="flex_box gap-08 item-center send_date">
								<div class="search-date">
									<span class="datepicker">
										<input type="text" id="sendDate" class="datepicker optional " title="${op:message('M00507')}" />
									</span>
								</div>
								<select id="sendHour">
									<c:forEach begin="0" end="23" step="1" varStatus="i">
										<option value="${i.index >= 10 ? i.index : '0' += i.index}"><c:out value="${i.index}" /></option>
									</c:forEach>
								</select>
								<span class="wave">시</span>
								<select id="sendTime">
									<c:forEach begin="0" end="59" step="1" varStatus="i">
										<option value="${i.index >= 10 ? i.index : '0' += i.index}"><c:out value="${i.index}" /></option>
									</c:forEach>
								</select>
								<span class="wave">분</span>
							</div>
						</td>
					</tr>
					 <tr>
					 	<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00275')}"/></td>
					 	<td>
					 		<div>
					 			<input type="text" id="subject" name="subject" title="${op:message('M00275')}" class="input_txt required _filter half require" maxlength="255" />
							</div>
					 	</td>
					 </tr>
					 <tr>
						<td class="label"><c:out value="${op:message('M01699')}"/></td>
						<td>
							<div>
								<input type="file" name="files" onchange="validationFile(this)"  />
						   </div>
						</td>
					</tr>
					 <tr>
					 	<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td>
					 	<td>
							<!-- smart_editor2 area -->
			                <div>
			                    <textarea id="content" name="content" cols="30" rows="20" class="editor-content require" title="${op:message('M00006')}">
									<p><font face="Malgun Gothic, 돋움, dotum, sans-serif"><meta charset="UTF-8"><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>고향사랑관리자</title></font></p><table width="100%" cellspacing="0" cellpadding="0" border="0" style="font-family: 'Noto Sans KR', -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo',, 'Malgun Gothic', sans-serif; font-size: 14px; line-height: 1; font-weight: 400; vertical-align: top; word-wrap: break-word; word-break: break-all; box-sizing: border-box;"><tbody><tr><td><table align="center" style="width: 600px; border: 5px solid #d6d6d6; border-collapse: collapse; margin: 0 auto;"><thead><tr><td style="padding: 25px 25px 55px"><h1 style="margin: 0; text-align: left;"><a style="display: block;" href="https://www.ilovegohyang.go.kr" title="고향사랑이음"><img style="border: 0; width: 190px; height: 38px;" src="https://www.ilovegohyang.go.kr/content/opmanager/images/img_logo_02.png" alt="고향사랑이음 로고"></a></h1></td></tr></thead><tbody><tr><td style="padding: 0 36px; text-align: center;"><h2 style="margin: 0 0 40px; font-size: 38px; line-height: 45px; font-weight: bold; color: #1c2957">제목</h2></td></tr><tr><td style="padding: 0 36px"><ul style="margin: 0; padding: 30px 0; background: #f7f7f7; font-size: 14px; color: #4c4c4c; text-align: center; list-style: none">내용</ul></td></tr><tr><td style="padding: 40px 36px 0; text-align: center;"><br></td></tr></tbody><tfoot><tr><td style="padding: 50px 36px 30px; line-height: 18px; text-align: center"><div style="padding: 20px; background: #f5f5f5;"><p style="margin: 4px 0 0 0">메일수신을 원하지 않을 경우, 홈페이지 로그인 후 회원정보변경에서&nbsp;</p><p style="margin: 4px 0 0 0">e-mail수신여부를 변경해 주세요.</p></div></td></tr><tr><td style="padding: 20px 36px 0; line-height: 18px; color: #959595; text-align: center; letter-spacing: -1px"><p style="margin: 0">30128 세종특별자치시 정부2청사로13(나성동)<span style="padding: 0px 6px; color: #bbb; font-size: 10px">|</span>30116 세종특별자치시 한누리대로411(어진동)<span style="padding: 0px 6px; color: #bbb; font-size: 10px">|</span>고객센터 1522-2431</p></td></tr><tr><td style="padding: 0 36px 35px; line-height: 18px; color: #959595; text-align: center; letter-spacing: -1px"><p style="margin: 7px 0 0 0; color: #b0b0b0">©Ministry of the interior and safety.All rights reserved.</p></td></tr></tfoot></table></td></tr></tbody></table><br><br>&nbsp;&nbsp;<p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>

								</textarea>
			                </div>
			                <!-- smart_editor2 area -->
					 	</td>
					 </tr>
				</tbody>
			</table>

			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-small" onclick="insertEmail()"><span>발송</span></button>
 				<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/email/list'"><c:out value="${op:message('M00037')}"/></button>

 				</div>
			</div>

		</div> <!-- // board_write -->

	<!--// ${op:message('M00269')} 끝-->
	<module:smarteditorInit />
	<module:smarteditor id="content" />


	<iframe id="submitFrm" style="display : none" name="submitFrm"></iframe>

<script type="text/javascript">

var sendUserList = [];

$(function() {

	try{
		$('#notice').validator(function() {
			Common.getEditorContent("content");

			if ($('#content').val().toLowerCase() == '<p>&nbsp;</p>' || $('#content').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}
		});


		$("input[name=authTarget]").on('change', function () {
			$(".target").hide();
			$(".target_" + $("input[name=authTarget]:checked").val()).show();
		});

		$("input[name=sendType]").on('change', function () {
			var date = new Date();
            var eDate = date.getFullYear() + Common.addZero((date.getMonth() + 1), 2) + Common.addZero(date.getDate(), 2);
			$("#sendDate").val(eDate);

			if ($(this).val() == 'D') {
				$(".send_date").find('input, select, button').prop("disabled",true);
			} else {
				$(".send_date").find('input, select, button').prop("disabled",false);
			}

		});


		$("#auth").click();
		$("#now").click();

		$('html').click(function(e){
			if($(e.target).parents('.searchResult').length < 1 && $(".searchResult").is(":visible")){
				hideSearchDiv();
			}
		});

	} catch(e) {
		alert(e.message);
	}

});

function insertEmail() {
	Common.getEditorContent("content");

	var authList = [];

	if (!validation()) return false;

	var data = {
		subject : $("#subject").val(),
		content : $('#content').val(),
		sendType : $("input[name=sendType]:checked").val(),
		sendDate : $("#sendDate").val() + $("#sendHour").val() + $("#sendTime").val() + '00',
		authTarget : $("input[name=authTarget]:checked").val(),
	}

	$('input:checkbox[name=authority]:checked').each(function (idx) {
		data['authList[' + idx + '].authority'] = $(this).val();
	});


	var formData = new FormData();
	formData.append("subject", $("#subject").val());
	formData.append("content", $('#content').val());
	formData.append("sendType", $("input[name=sendType]:checked").val());
	formData.append("sendDate", $("#sendDate").val() + $("#sendHour").val() + $("#sendTime").val() + '00');
	formData.append("authTarget", $("input[name=authTarget]:checked").val());

	var arr = [];
	$('input:checkbox[name=authority]:checked').each(function (idx) {
		formData.append('authList[' + idx + '].authority', $(this).val());
	});


	var files = $("input[name=files]")[0].files;
	for(i=0; i<files.length; i++) {
      formData.append("files", files[i]);
    }

	$.ajax({
		url : '/opmanager/email/form',
		type : 'post',
		data: formData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		cache: false,
		success : function(response) {
			if (response.isSuccess && response.data) {

				var form  = $('<form></form>');
				form.attr('action','/opmanager/email/send');
				form.attr('target','submitFrm');
				form.attr('method','post');

				form.append($('<input />', {type: 'hidden', name : 'emailId', value: response.data} ));

				// 개별 전송인 경우
				if ($("input[name=authTarget]:checked").val() == 'E') {
					for (var i in sendUserList) {
						var d = sendUserList[i];
						form.append($('<input />', {type: 'hidden', name : 'sendUserList[' + i + '].userName', value: d.userName} ));
						form.append($('<input />', {type: 'hidden', name : 'sendUserList[' + i + '].email', value: d.email} ));
					}
				}

				form.appendTo('body');

				form.submit();
				form.remove();
				alert("발송을 요청하였습니다.");
				location.href = '/opmanager/email/list';
			}
		}
	});




	/*$.post(url("/opmanager/email/form"), data, function(response) {

		if (response.isSuccess && response.data) {
			$("input[name=emailId]").val(response.data.emailId);
			$("input[name=sendDate]").val(data.sendDate);
			document.frm.action = "/opmanager/email/send";
			document.frm.target = "submitFrm";
			document.frm.method = "post";
			document.frm.submit();

		} else {

		}

	});*/

}

function searchKeyDown() {
	if (event.keyCode == '13') {
		searchUser();
	}
}

function searchUser() {
	if ($("input[name=userName]").val() == '') {
		alert("발송자명 입력해주세요.");
		return false;
	}

	$.ajax({
		url : '/opmanager/email/search',
		type : 'post',
		data : {userName : $("input[name=userName]").val()},
		success : function (response) {

			if (response.isSuccess) {

				$(".searchResult > .searchList > ul").empty();

				if (response.data.length > 0) {
					$(".searchResult").show();
					var html =  "";
					for (var i in response.data) {
						var d = response.data[i];
							html += "<li>";
							html += "	<span class='searchEmil'>" + d.email + "</span>";
							html += "	<a href='javascript:;' onclick=\"addSendUser('" + d.email + "', '" + d.userName + "')\">추가</a>";
							html += "</li>";

					}
					$(".searchResult > .searchList > ul").append(html);
				}



			} else {
				alert("검색 중 오류가 발생하였습니다.\n담당자에게 문의 부탁드립니다.");
				return false;
			}

		},
		error : function () {
			alert("검색 중 오류가 발생하였습니다.\n담당자에게 문의 부탁드립니다.");
			return false;
		}
	})

	/*$.post("/opmanager/email/search", {userName : $("input[name=userName]").val()}, function (response) {
		console.log(response);
	})*/
}

function addSendUser(email, userName) {

	var idx = sendUserList.findIndex(function (u) {
		return u.email === email
	});

	if (idx > -1) return false;

	var html = "";
	html += "<li style='display : flex'>";
	html += "	<span class='searchEmil' style='flex : 1'>" + email + "</span>";
	html += "	<a href='javascript:;' onclick=\"removeSendUser(this)\">삭제</a>";
	html += "</li>";

	sendUserList.push({
		userName : userName,
		email : email
	});

	$("#sendUserListTxt").append(html);
}

function removeSendUser(e) {

	var email = $(e).parent().find('span').html();

	sendUserList.forEach((item,index) => {
		item.email === email ? sendUserList.splice(index,1) : '' ;
	});

	$(e).parent().remove();
}

function validation () {

	if (($("input[name=authTarget]:checked").val() == 'A' && $("input[name=authority]:checked").length <= 0) ||
	    ($("input[name=authTarget]:checked").val() == 'E' && sendUserList.length <= 0)) {
		alert("발송 대상이 없습니다.");
		$("input[name=authTarget]").focus();
		return false;
	}


	var isValid = true;
	$(".require").each(function (idx, that) {
		if ($(that).val() == '') {
			alert($(that).attr('title') + "이 입력되지 않았습니다.");
			$(that).focus();
			isValid = false;
			return false;
		}

	});

	return isValid;
}

function validationFile(e) {
	var imageReg = /(.*?)\.(jpg|jpeg|png|gif|bmp|hwp|doc|docx|pdf|zip|ppt|pptx)$/;
	var maxSize = 10 * 1024 * 1024;	//10MB
	var isSucc = true;

	if (e.files) {

		for (var i = 0; i < e.files.length; i++) {
			if (isSucc && !e.files[i].name.match(imageReg)) {
				alert("jpg|jpeg|png|gif|bmp|hwp|doc|docx|pdf|zip|ppt|pptx 파일만 등록 가능합니다.");
				$(e).focus();
				isSucc = false;
				break;
			}

			if (isSucc && e.files[i].size > maxSize) {
				alert("이미지 파일은 개당 10MB 이하로 등록 가능합니다.");
				$(e).focus();
				isSucc = false;
				break;
			}
		}

		if (!isSucc) {
			if (Browser.getName() == 'ie') {
				$(e).replaceWith($(e).clone(true));
			} else {
				$(e).val("");
			}
		}



	}
}

function hideSearchDiv() {
	$(".searchResult").hide();
}

</script>


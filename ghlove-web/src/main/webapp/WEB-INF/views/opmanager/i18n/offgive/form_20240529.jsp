<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>기탁서 등록</span></h3>

<div class="btn_all btn_right mb15">
    <div class="flex_box gap-08">
    	<!-- <button type="button" class="btn btn-dark-gray btn-small" onclick="" id="testBtn" style="display:none;" disabled="disabled">테스트</button> -->
        <button type="button" class="btn btn-dark-gray btn-small" onclick="window.open('/opmanager/offgive/popup/form-policy','pop','width=900,height=1200');">기탁서 양식 및 약관 출력</button>
    </div>
</div>

<form:form modelAttribute="offgive" method="post" action="/opmanager/offgive/create">
	<input type="hidden" id="mberCi" name="mberCi" value="" />
	<input type="hidden" id="mberDi" name="mberDi" value="" />
	<input type="hidden" id="gender" name="gender" value="" />
	<input type="hidden" id="birthday" name="birthday" value="" />
	<input type="hidden" id="userId" name="userId" value="" />
	<input type="hidden" id="cntrAmtMax" name="cntrAmtMax" value="" />
	<input type="hidden" id="juso" name="juso" value="" />
	<input type="hidden" id="elctrnPayNo" name="elctrnPayNo" value="" />
	<input type="hidden" id="linkMngKey" name="linkMngKey" value="" />
	<input type="hidden" id="serviceResult" name="serviceResult" value="1" />
	<input type="hidden" id="tempUserName" name="tempUserName" value="" />

	<input type="hidden" id="infoAgreAt_1_psitnLocgovUpperCodeTmp" name="infoAgreAt_1_psitnLocgovUpperCodeTmp" value="" />
	<input type="hidden" id="infoAgreAt_1_psitnLocgovCodeTmp" name="infoAgreAt_1_psitnLocgovCodeTmp" value="" />
	<input type="hidden" id="infoAgreAt_1_psitnLocgovNameTmp" name="infoAgreAt_1_psitnLocgovNameTmp" value="시,구,군" />
	<input type="hidden" id="infoAgreAt_2_psitnLocgovUpperCodeTmp" name="infoAgreAt_2_psitnLocgovUpperCodeTmp" value="" />
	<input type="hidden" id="infoAgreAt_2_psitnLocgovCodeTmp" name="infoAgreAt_2_psitnLocgovCodeTmp" value="" />
	<input type="hidden" id="infoAgreAt_2_psitnLocgovNameTmp" name="infoAgreAt_2_psitnLocgovNameTmp" value="시,구,군" />

	<input type="hidden" id="addrRnMgtSn" name="addrRnMgtSn" value="" />
	<input type="hidden" id="addrUdrtYn" name="addrUdrtYn" value="" />
	<input type="hidden" id="addrBuldMnnm" name="addrBuldMnnm" value="" />
	<input type="hidden" id="addrBuldSlno" name="addrBuldSlno" value="" />
	<input type="hidden" id="addrZipNo" name="addrZipNo" value="" />
	<input type="hidden" id="addrAdmCd" name="addrAdmCd" value="" />
	<input type="hidden" id="addrBdMgtSn" name="addrBdMgtSn" value="" />
	<input type="hidden" id="addrBoadAddr" name="addrBoadAddr" value="" />

	<input type="hidden" id="loginLocGovCode" name="loginLocGovCode" value="" />

	<div class="board_write">
	    <table class="board_write_table" summary="">
	        <colgroup>
	            <col style="width:220px;">
	        </colgroup>
	        <tbody>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>이름</td>
	                <td>
	                    <div class="flex_box gap-08 item-center">
	                        <input id="userName" name="userName" title="이름" class="input_txt required _filter wd-150" type="text" autocomplete="off" value="" maxlength="50">
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>주민등록번호</td>
	                <td>
	                    <div class="flex_box gap-08 item-center">
	                        <input id="jumin1" name="jumin1" title="주민등록번호 앞자리" class="input_txt required _filter wd-150 _number" type="text" autocomplete="off" value="" maxlength="6" >
	                        <span class="wave">-</span>
	                        <input id="jumin2" name="jumin2" title="주민등록번호 뒷자리" class="input_txt required _filter wd-150 _number" type="password" autocomplete="new-password" value="" maxlength="7" >

	                        <button type="button" class="btn btn-default btn-mini" id="btnSciCall" onClick="sciCallClick()"><span>실명인증</span></button>
	                    </div>
	                    &nbsp;&nbsp;&nbsp;&nbsp;<span class="wave tip">* 실명 등록 이 필요한 경우 아래 URL 을 이용하시기 바랍니다.<br></span>
	                    &nbsp;&nbsp;&nbsp;&nbsp;바로가기 ▶ <a href="https://www.siren24.com/mysiren/customer/sir_g0201_01.jsp" target="_blank">https://www.siren24.com/mysiren/customer/sir_g0201_01.jsp</a>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>휴대폰</td>
	                <td>
	                    <div class="flex_box gap-08 item-center">
	                        <select id="phoneNumber1" name="phoneNumber1" title="선택" class="wd-100 _number">
	                        	<c:forEach items="${phone}" var="phone">
				                	<option value="${fn:escapeXml(phone.id)}" label="${fn:escapeXml(phone.label)}" />
				                </c:forEach>
	                        </select>
	                        <span class="wave">-</span>
	                        <input id="phoneNumber2" name="phoneNumber2" title="휴대폰중간번호" class="input_txt required _filter wd-100 _number" type="text" autocomplete="off" maxlength="4">
	                        <span class="wave">-</span>
	                        <input id="phoneNumber3" name="phoneNumber3" title="휴대폰마지막번호" class="input_txt required _filter wd-100 _number" type="text" autocomplete="off" maxlength="4">
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>거주지 주소</td>
	                <td>
	                    <div class="flex_box gap-08">
	                        <input id="post" name="post" title="" class="input_txt required _filter wd-150" type="text" disabled>
	                        <button type="button" class="btn btn-default btn-mini" id="btn_address" onClick="jusoPopup()">주소찾기</button>
	                    </div>
	                    <div class="flex_box gap-08">
	                        <input id="address" name="address" title="" class="input_txt required _filter half" type="text" disabled>
	                        <input id="addressDetail" name="addressDetail" title="" class="input_txt required _filter half" type="text">
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>기부자 행정지 주소</td>
	                <td>
	                    <div class="flex_box gap-08 item-center">
	                        <select id="psitnLocgovUpperCode" name="psitnLocgovUpperCode" title="시도선택" class="wd-150" onChange="psitnLocgovUpperCodeChange(this.value)" disabled>
	                            <option value="">시도선택</option>
	                            <c:forEach items="${wdr}" var="wdr">
				                	<option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
				                </c:forEach>
	                        </select>
	                        <select id="psitnLocgovCode" name="psitnLocgovCode" title="시,구,군" class="wd-150" disabled>
	                            <option value="">시,구,군</option>
	                        </select>
	                        <!-- 이미 스크립트가 들어가 있는 상황이라 checked 로 display none 처리 했음. -->
	                        <input id="rdo2-1" name="infoAgreAt" type="radio" value="1" checked="checked" style="display:none">
	                        <button type="button" class="btn btn-default btn-mini" id="btn_infoAgreAt" onClick="mopas()">행정주소검색</button>
	                        <span id="locUserSpan" style="display:none">주민등록상 성함 : <input id="locUserName" name="locUserName" title="행정주소 검색에 필요한 이름" class="input_txt wd-150 " type="text" maxlength="10" ><span class="wave tip">※ 실명인증 성함과 등본 성함이 다를 수 있습니다. (ex. 유>류, 열>렬 기타 등등)</span></span>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>기부 지자체</td>
	                <td>
	                    <div class="flex_box gap-08 item-center">
	                        <select id="locgovUpperCode" name="locgovUpperCode" title="시도선택" class="wd-150" onChange="locgovUpperCodeChange(this.value)">
	                            <option value="">시도선택</option>
	                            <c:forEach items="${wdr}" var="wdr">
				                	<option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
				                </c:forEach>
	                        </select>
	                        <select id="locgovCode" name="locgovCode" title="시,구,군" class="wd-150" onChange="getDesignatedDonationInfo(this.value)">
	                            <option value="">시,구,군</option>
	                        </select>
	                        <select id="prjId" name="prjId" title="특정사업에 기부하기 선택" class="wd-300" style="display:none;">
	                            <option value="0">자치단체에 기부하기</option>
	                        </select>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>기부금액</td>
	                <td>
	                    <div class="flex_box gap-08 item-center">
	                        <input id="cntrAmt" name="cntrAmt" title="" class="input_txt required _filter wd-150 _number_comma" type="text" maxlength="7" disabled>
	                        <span class="wave">원</span>

                            <span class="wave tip wd-120 flex_box juc-sbt"><span class="mr-auto">(한도&nbsp;:&nbsp;</span><span id="cntrAmtMaxText">0</span>원)</span>


	                        <button type="button" class="btn btn-default btn-mini" id="btnMaxCheck" onClick="maxCheck()" disabled>한도체크</button>
	                        <span class="wave tip">※ 일년에 최대 5백만원까지 기부가 가능합니다.</span>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>답례품</td>
	                <td>
	                	<!-- 20221215 수정 -->
                        <div class="flex_box gap-12 item-center">
                            <div class="input-form">
                                <input id="rdo1-1" name="rtnpsntReqstCode" type="radio" value="100" checked="checked">
                                <label for="rdo1-1">답례품을 제공 받음</label>
                            </div>
                            <div class="input-form">
                                <input id="rdo1-2" name="rtnpsntReqstCode" type="radio" value="300">
                                <label for="rdo1-2">답례품을 제공 받지 않음</label>
                            </div>

                        </div>
                        &nbsp;&nbsp;&nbsp;<span class="wave tip">※ '답례품 제공받지 않음'</span> 선택 시 <font color="blue">포인트</font>가 <span class="wave tip"><font size="3" style="font-weight:bold">미지급</font></span> 됩니다.&nbsp;&nbsp;&nbsp;<br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[주의] '답례품을 제공받지 않음' 선택 시 변경이 불가능 합니다.
	                </td>
	            </tr>
	            <tr>
	                <td class="label">접수은행</td>
	                <td>
	                    <div>
	                        <c:out value="${offBank.label}"/>&nbsp;<c:out value="${psitnNm}"/>
	                    </div>
	                </td>
	            </tr>
	            <tr>
	                <td class="label"><span class="required_mark">*</span>약관 동의</td>
	                <td>
	                    <div class="flex_box gap-12">
	                        <div class="checkbox">
	                            <input type="checkbox" id="chk" name="chk">
	                            <label for="chk">동의함</label>
	                        </div>
	                    </div>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	</div>
	<div class="btn_all btn_center">
	    <div class="flex_box gap-08">
	        <button type="button" class="btn btn-dark-gray btn-small" onClick="save()">등록</button>
	        <button type="button" class="btn btn-defualt btn-small" onClick="location.href='/opmanager/offgive/create'">초기화</button>
	    </div>
	</div>

</form:form>

<form id="sciCall" name="sciCall" method="post" action="/opmanager/offgive/sci-call" target="sciCallIframe">
	<input type="hidden" id="sci_jumin1" name="sci_jumin1" value="" />
	<input type="hidden" id="sci_jumin2" name="sci_jumin2" value="" />
	<input type="hidden" id="sci_name" name="sci_name" value="" />
</form>
<iframe name="sciCallIframe" style="display:none;" width="500px" height="500px"></iframe>

<form name="AKCFrm" id="AKCFrm" method="post">
    <input type="hidden" name="confmKey" value="U01TX0FVVEgyMDIyMTEyNTE0MTUzMTExMzI1OTc=" />
    <input type="hidden" name="encodingType"   value=""   />
    <input type="hidden" name="cssUrl" value="" />
    <input type="hidden" name="resultType" value="4" />
    <input type="hidden" name="currentPage" id="currentPage" value="1" />
    <input type="hidden" name="countPerPage" value="1" />
    <input type="hidden" name="keyword" id="keyword" value="" />
</form>

<!-- 암호화 모듈 -->
<script type="text/javascript" src="/content/modules/jsencrypt.min.js"></script>
<script type="text/javascript" src="/content/popup/popup.js"></script>
<script type="text/javascript">

let txprSp;			// 지방 세외 내국인(01), 외국인(05) 등 납부자구분

$(function() {
	// 초기값
	$("#phoneNumber1").val("010");
	$("#psitnLocgovUpperCode").prop("disabled", true);
	$("#psitnLocgovCode").prop("disabled", true);
	pop.openNotice('N');

	// 행정정보 공동이용 동의 활성/비활성화
	$("input[name='infoAgreAt']").click(function() {
    	$("#btn_infoAgreAt").prop("disabled", this.value == 1 ? false : true);


		if( this.value == 1) {	// 동의
			$("#psitnLocgovUpperCode").val($("#infoAgreAt_1_psitnLocgovUpperCodeTmp").val());
			$("#psitnLocgovCode option").val($("#infoAgreAt_1_psitnLocgovCodeTmp").val());
			$("#psitnLocgovCode option").text($("#infoAgreAt_1_psitnLocgovNameTmp").val());
		} else {	// 비동의
			$("#psitnLocgovUpperCode").val($("#infoAgreAt_2_psitnLocgovUpperCodeTmp").val());
			$("#psitnLocgovCode option").val($("#infoAgreAt_2_psitnLocgovCodeTmp").val());
			$("#psitnLocgovCode option").text($("#infoAgreAt_2_psitnLocgovNameTmp").val());
		}
	});

	// 답례품을 제공 받지 않음 선택 시 메시지 띄움
	$('input:radio[name=rtnpsntReqstCode]').change(function(){
		if (this.value != "100") {
			alert("답례품 제공받지 않음 선택 시 포인트가 미지급 됩니다.");
		}
	});

	getUserInfo();
	//getTestBtn();
});

/*function getTestBtn(){
	var loginId = '${fn:escapeXml(requestContext.user.loginId)}';
	var testUserList = ['jimcsh1','test'];
	var btnBool = false;

	for(var i=0; i<testUserList.length; i++){
		if(loginId == testUserList[i]) btnBool = true;
	}

	if(btnBool){
		$("#testBtn").css("display","");
		$("#testBtn").prop("disabled", false);
	}else{
		$("#testBtn").css("display","none");
		$("#testBtn").prop("disabled", true);
	}
}*/

//로그인 지자체코드 정보 조회
function getUserInfo () {
	var userId = ${fn:escapeXml(requestContext.user.userId)};
	//var userId = $("#userId").val();
	$.get(url("/opmanager/offgive/locgov/"+userId), {}, function(response) {
		if(response.isSuccess && response.data){
			$("#loginLocGovCode").val(response.data);
		}
	});
}

// 기부자 행정지 주소 변경
function psitnLocgovUpperCodeChange(value) {
	Common.loading.display = false;

	$("#psitnLocgovCode option").remove();
	if (value != "") {
		$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {

			for (var i = 0; i < response.length; i++) {
	            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
	            $('#psitnLocgovCode').append(options);
	        }


	    });
	} else {
        $('#psitnLocgovCode').append('<option value="">시,구,군</option>');
	}
}

// 기부 지자체 변경
function locgovUpperCodeChange(value) {
	Common.loading.display = false;

	$("#locgovCode option").remove();
	if (value != "") {
		$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {

			for (var i = 0; i < response.length; i++) {
	            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
	            $('#locgovCode').append(options);
	        }

			getLocGovLmtt(response[0].LOCGOV_CODE);

// 			// 조회 된 값 유지
// 			if ("${fn:escapeXml(searchParam.shLocgovCode)}" != "") {
// 			    $("#psitnLocgovCode").val('${fn:escapeXml(searchParam.shLocgovCode)}').prop("selected", true);
// 			}

	    });
	} else {
        $('#locgovCode').append('<option value="">시,구,군</option>');
	}

	getDesignatedDonationInfo(value);
}

function getLocGovLmtt(locgovCode) {
	Common.loading.display = false;

	$.ajax({
		url : '/opmanager/offgive/getLocGovLmtt',
		type : 'post',
		data : {cityCode : locgovCode},
		async : true,
		success : function (response) {
			if (response.isSuccess && response.data) {
				alert("선택하신 지자체는\n" + response.data.violtResnCn + "으로\n" + response.data.lmttBgnDe + "부터 " + response.data.lmttEndDe + "일까지 \n기부가 불가능합니다.");
				$("#locgovCode").focus();
				return false;
			} else {
				return true;
			}
		}
	});
}

// 실명인증
function sciCallClick() {
	Common.loading.display = true;

	if(validation("userName", "이름")) return false;
	if(validation("jumin1", "주민등록번호")) return false;
	if(validation("jumin2", "주민등록번호")) return false;

	if ($("#offgive #jumin1").val().length + $("#offgive #jumin2").val().length != 13) {
		alert("주민번호는 13자리로 입력하셔야 합니다.")
		return;
	}

	$("#sciCall #sci_jumin1").val($("#offgive #jumin1").val());
	$("#sciCall #sci_jumin2").val($("#offgive #jumin2").val());
	$("#sciCall #sci_name").val($("#offgive #userName").val());

	$("#sciCall").submit();

}

// 실명인증 callbak
function sciResponse(sciUser) {
	//console.log('sciUser', sciUser);
	//console.log('ci', sciUser.ci);

	if (sciUser.result == 1) {	// 성공
		$("#mberCi").val(sciUser.ci);
		$("#mberDi").val(sciUser.di);

		// 우리쪽 회원 유무 확인
		$.post(url("/opmanager/offgive/user-check"), {'mberCi' : sciUser.ci}, function(response) {
			var userId = "";
			var post = "";
			var address = "";
			var addressDetail = "";
			var phoneNumber1 = "010";
			var phoneNumber2 = "";
			var phoneNumber3 = "";
			var upper_locgov_mapng_code = "";
			var locgov_mapng_code = "";
			var locgov_nm = "시,구,군";

			var disabled = false;

			if(response.isSuccess){
				if (response.data != null) {
					disabled = true;

					userId = response.data.user.userId;
					post = response.data.user.userDetail.post;
					address = response.data.user.userDetail.address;
					addressDetail = response.data.user.userDetail.addressDetail;

					if (response.data.user.phoneNumber != null) {
						phoneNumber1 = response.data.user.phoneNumber.split("-")[0];
						phoneNumber2 = response.data.user.phoneNumber.split("-")[1];
						phoneNumber3 = response.data.user.phoneNumber.split("-")[2];

						$("#phoneNumber1").prop("disabled", true);
						$("#phoneNumber2").prop("disabled", true);
						$("#phoneNumber3").prop("disabled", true);
					} else {
						$("#phoneNumber1").prop("disabled", false);
						$("#phoneNumber2").prop("disabled", false);
						$("#phoneNumber3").prop("disabled", false);
					}
				}

				$("#userId").val(userId);
				$("#post").val(post);
				$("#address").val(address);
				$("#addressDetail").val(addressDetail);
				$("#phoneNumber1").val(phoneNumber1);
				$("#phoneNumber2").val(phoneNumber2);
				$("#phoneNumber3").val(phoneNumber3);

				$("#addressDetail").prop("disabled", disabled);
				//$("#btn_address").prop("disabled", false);

				// 실명인증 시 공통 적으로 초기화 (무조건 disabled true false)
				$("#btnSciCall").prop("disabled", true);
				$("#userName").val($("#userName").val().replace(/ /gi, ''));	// 공백제거
				$("#userName").prop("disabled", true);
				$("#jumin1").prop("disabled", true);
				$("#jumin2").prop("disabled", true);
				$("#btnMaxCheck").prop("disabled", false);
				$("#cntrAmt").prop("disabled", false);
				$("#cntrAmt").val("");	// 기부금액

				alert("실명인증 되었습니다. \n다른 정보의 실명인증이 필요하시면 페이지를 초기화해 주세요.");
			} else {
				alert(response.errorMessage);
			}

	    });
	} else if (sciUser.result == 3) {	// 없음
		alert("실명이 확인되지 않았습니다.\n실명 등록이 필요 합니다. \n실명등록 후 이용하시기 바랍니다.");
		window.open("https://www.siren24.com/mysiren/customer/sir_g0201_01.jsp", "siren24");
	} else {
		alert("실명인증에 실패하셨습니다. [" + sciUser.message + "]");
	}

	// 로딩바 display none
	$("#loading-dimmed").css("display","none");
	$("#loading").css("display","none");

}

/**
 *	함 수 명 : jusoPopup
 *	기	능  : 주소팝업 호출
 */
function jusoPopup() {
	Common.popup("/opmanager/juso-popup", "/opmanager/juso-popup", 600, 420 ,1, 0, 0);
}

/**
 *	함 수 명 : jusoCallBack
 *	기	능  : 주소팝업 콜백 함수
 */
function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn
		, detBdNmList, bdNm, bdKdcd, siNm, sggNm, emdNm, liNm, rn, udrtYn, buldMnnm, buldSlno, mtYn, lnbrMnnm, lnbrSlno, emdNo){
	$("#post").val(zipNo);
	$("#address").val(roadAddrPart1+" "+roadAddrPart2);
	$("#addressDetail").val(addrDetail);

	// 기부자 행정지 주소 셋팅 (tmp 에도 저장)
	$.post(url("/opmanager/offgive/getLocgovMapngCode"), {'locgovCode' : admCd.substring(0,5)}, function(response) {
		//console.log('response', response);

		if(response.isSuccess && response.data) {
			if ($("input:radio[name='infoAgreAt']:checked").val() == '0') {
				$("#psitnLocgovUpperCode").val(response.data.upper_locgov_mapng_code);
				$("#psitnLocgovCode option").val(response.data.locgov_mapng_code);
				$("#psitnLocgovCode option").text(response.data.locgov_nm);
			}

			$("#infoAgreAt_2_psitnLocgovUpperCodeTmp").val(response.data.upper_locgov_mapng_code);
			$("#infoAgreAt_2_psitnLocgovCodeTmp").val(response.data.locgov_mapng_code);
			$("#infoAgreAt_2_psitnLocgovNameTmp").val(response.data.locgov_nm);
		}
    });
}

// 한도체크
function maxCheck() {
	Common.loading.display = false;

	if(validation("mberCi", "실명인증")) return false;

	$.post(url("/opmanager/offgive/maxCheck"), {'userId' : $("#userId").val(), 'mberCi' : $("#mberCi").val()}, function(response) {
		//console.log('response', response);

		if(response.isSuccess) {
// 			$("#cntrAmt").val(response.data.toLocaleString());
			$("#cntrAmtMax").val(response.data);
			$("#cntrAmtMaxText").text(response.data.toLocaleString());
		}
    });
	$("#cntrAmt").prop("disabled", false);
	$("#cntrAmt").focus();
}

function save() {

	// 접수은행 정보가 없으면 오프라인 담당자가 아니기 때문에 등록 할수 없다.
	if ("${fn:escapeXml(offBank.label)}" == "" || "${fn:escapeXml(psitnNm)}" == "") {
		alert("오프라인 담당자만 등록 하실 수 있습니다.");
		return;
	}

	if ($("#mberCi").val() == "") {
		alert("실명인증을 진행해 주십시오!\n기부자의 실명인증이 되지 않으면 기부가 불가능합니다.");
		$("#jumin2").focus()
		return;
	}

	if (!txprSp) {
		alert('행정주소검색을 진행해주세요.');
		return;
	}

	if(validation("userName", "이름")) return false;
	if(validation("jumin1", "주민등록번호")) return false;
	if(validation("jumin2", "주민등록번호")) return false;
	if(validation("phoneNumber1", "휴대폰")) return false;
	if(validation("phoneNumber2", "휴대폰")) return false;
	if(validation("phoneNumber3", "휴대폰")) return false;
	if(validation("post", "주소")) return false;
	if(validation("address", "주소")) return false;
	if(validation("addressDetail", "주소")) return false;
	if(validation("psitnLocgovCode", "기부자 행정지 주소")) return false;
	if(validation("locgovCode", "기부 지자체")) return false;
	if(validation("cntrAmt", "기부금액")) return false;
	if (!$("#chk").is(":checked")) {
		alert("약관 동의 정보를 확인해 주세요.");
		return;
	}

	// 최대한도 체크
	// 한도체크는 무조건 한번은 해야한다.
	if(validation("cntrAmtMax", "한도체크")) return false;
	if (Number($("#cntrAmt").val().replace(/,/g, "")) > Number($("#cntrAmtMax").val())) {
		alert("기 신고한 기부정보(기부한도 사용)가 있어 추가 등록할 수 없습니다.");
		return;
	}
	// 기부 최소금액 체크
	if (Number($("#cntrAmt").val().replace(/,/g, "")) < 100) {
		alert("기부 최소 금액은 100원 입니다.");
		$("#cntrAmt").val("100");
		return;
	}
	// 기부금액 100원단위 절사
	if (!Number.isInteger(Number($("#cntrAmt").val().replace(/,/g, "")) / 100)) {
		alert("기부 금액 단위는 100원 단위입니다.");
		$("#cntrAmt").val(Math.floor(Number($("#cntrAmt").val().replace(/,/g, "")) / 100) * 100);
		return;
	}

	// 행정지 주소와 지자체 주소 동일 시 체크 (같으면 안되고, 본청도 안됨.)
	if ($("#psitnLocgovCode").val() == $("#locgovCode").val()) {
		alert("행정지 주소와 같은 지자체에는 기부 할 수 없습니다.");
		return;
	}
	if ($("#psitnLocgovUpperCode").val() == $("#locgovUpperCode").val() && ( $("#locgovCode option:selected").text() == "시청" || $("#locgovCode option:selected").text() == "도청")) {
		alert("행정지 주소와 같은 지자체에는 기부 할 수 없습니다.");
		return;
	}

	var confirmMessage = "";
	if ($("input:radio[name='rtnpsntReqstCode']:checked").val() == '300') {
		configmMessage = "답례품을 제공받지 않음을 선택하셨습니다. \n선택된 정보로 등록 하시겠습니까?";
	} else {
		configmMessage = "${op:message('M00159')}";
	}

	// 전자납부번호 가져오기
	Common.confirm(configmMessage, function() {
// 		$("#elctrnPayNo").val("123123123123");	// 전자납부번호
// 		goSave();
		if($("#serviceResult").val() === '3') {
			$("#tempUserName").val($("#userName").val());
			$("#userName").val($("#locUserName").val());
		}

		if($("#locgovUpperCode").val() == "11000"){ // 서울시
           goSeoul();
       	}else{ // 서울시 제외 나머지 시군구
           goContry2();
       	}

	});

}

function nextSave() {

	// 접수은행 정보가 없으면 오프라인 담당자가 아니기 때문에 등록 할수 없다.
	if ("${fn:escapeXml(offBank.label)}" == "" || "${fn:escapeXml(psitnNm)}" == "") {
		alert("오프라인 담당자만 등록 하실 수 있습니다.");
		return;
	}

	if ($("#mberCi").val() == "") {
		alert("실명인증을 진행해 주십시오!\n기부자의 실명인증이 되지 않으면 기부가 불가능합니다.");
		$("#jumin2").focus()
		return;
	}

	if (!txprSp) {
		alert('행정주소검색을 진행해주세요.');
		return;
	}

	if(validation("userName", "이름")) return false;
	if(validation("jumin1", "주민등록번호")) return false;
	if(validation("jumin2", "주민등록번호")) return false;
	if(validation("phoneNumber1", "휴대폰")) return false;
	if(validation("phoneNumber2", "휴대폰")) return false;
	if(validation("phoneNumber3", "휴대폰")) return false;
	if(validation("post", "주소")) return false;
	if(validation("address", "주소")) return false;
	if(validation("addressDetail", "주소")) return false;
	if(validation("psitnLocgovCode", "기부자 행정지 주소")) return false;
	if(validation("locgovCode", "기부 지자체")) return false;
	if(validation("cntrAmt", "기부금액")) return false;
	if (!$("#chk").is(":checked")) {
		alert("약관 동의 정보를 확인해 주세요.");
		return;
	}

	// 최대한도 체크
	// 한도체크는 무조건 한번은 해야한다.
	if(validation("cntrAmtMax", "한도체크")) return false;
	if (Number($("#cntrAmt").val().replace(/,/g, "")) > Number($("#cntrAmtMax").val())) {
		alert("기 신고한 기부정보(기부한도 사용)가 있어 추가 등록할 수 없습니다.");
		return;
	}
	// 기부 최소금액 체크
	if (Number($("#cntrAmt").val().replace(/,/g, "")) < 100) {
		alert("기부 최소 금액은 100원 입니다.");
		$("#cntrAmt").val("100");
		return;
	}
	// 기부금액 100원단위 절사
	if (!Number.isInteger(Number($("#cntrAmt").val().replace(/,/g, "")) / 100)) {
		alert("기부 금액 단위는 100원 단위입니다.");
		$("#cntrAmt").val(Math.floor(Number($("#cntrAmt").val().replace(/,/g, "")) / 100) * 100);
		return;
	}

	// 행정지 주소와 지자체 주소 동일 시 체크 (같으면 안되고, 본청도 안됨.)
	if ($("#psitnLocgovCode").val() == $("#locgovCode").val()) {
		alert("행정지 주소와 같은 지자체에는 기부 할 수 없습니다.");
		return;
	}
	if ($("#psitnLocgovUpperCode").val() == $("#locgovUpperCode").val() && ( $("#locgovCode option:selected").text() == "시청" || $("#locgovCode option:selected").text() == "도청")) {
		alert("행정지 주소와 같은 지자체에는 기부 할 수 없습니다.");
		return;
	}

	var confirmMessage = "";
	if ($("input:radio[name='rtnpsntReqstCode']:checked").val() == '300') {
		configmMessage = "답례품을 제공받지 않음을 선택하셨습니다. \n선택된 정보로 등록 하시겠습니까?";
	} else {
		configmMessage = "${op:message('M00159')}";
	}

	// 전자납부번호 가져오기
	Common.confirm(configmMessage, function() {
// 		$("#elctrnPayNo").val("123123123123");	// 전자납부번호
// 		goSave();
		if($("#serviceResult").val() === '3') {
			$("#tempUserName").val($("#userName").val());
			$("#userName").val($("#locUserName").val());
		}

		if($("#locgovUpperCode").val() == "11000"){ // 서울시
           goSeoul();
       	}else{ // 서울시 제외 나머지 시군구
           goContry();
       	}

	});

}

function validation(id, text) {
	if($("#"+id).val() == "") {
		alert(text+" 정보를 확인해 주세요.");
		$("#"+id).focus();
		return true;
	}
	return false;
}

function getDateOfBirth(jumin1, jumin2) {
	var divisionCode = jumin2.substring(0, 1);
	var dateOfBirth = "";

	if(divisionCode == "1" || divisionCode == "2" || divisionCode == "5" || divisionCode == "6"){
		// 한국인 1900~, 외국인 1900~
		dateOfBirth = "19"+jumin1;
	}else if(divisionCode == "3" || divisionCode == "4" || divisionCode == "7" || divisionCode == "8"){
		// 한국인 2000~, 외국인 2000~
		dateOfBirth = "20"+jumin1;
	}else if(divisionCode == 9 || divisionCode == 0){
		// 한국인 1800~
		dateOfBirth = "18"+jumin1;
	}
	return dateOfBirth;
}

function getGender(jumin2){
	var genderCode = jumin2.substring(0, 1);
	var gender = "";
	if(genderCode % 2 > 0){
		gender = "0";
	}else if(genderCode % 2 == 0){
		gender = "1";
	}

	// 성별 (0;남, 1;여)
	return gender;
}

// 행공센 주소검색
function mopas() {
	Common.loading.display = true;

	if(validation("userName", "이름")) return false;
	if(validation("jumin1", "주민등록번호")) return false;
	if(validation("jumin2", "주민등록번호")) return false;

	if ($("#offgive #jumin1").val().length + $("#offgive #jumin2").val().length != 13) {
		alert("주민번호는 13자리로 입력하셔야 합니다.")
		return;
	}

	if(validation("mberCi", "실명인증")) return false;

	if($("#serviceResult").val() === "3" && $.trim($("#locUserName").val()) === "") {
		alert("행정주소 검색을 위해 주민등록등본 상 이름을 입력하십시오.");
		return;
	}

	var param = {
		'juminNo' : $("#offgive #jumin1").val() + $("#offgive #jumin2").val()
		,'userName' : $("#offgive #userName").val()
	};

	if($("#serviceResult").val() === "3") {
		param.userName = $("#locUserName").val();
	}

	txprSp = "";
	if (isForeign($("#offgive #jumin2").val())) {
		$.post("/opmanager/offgive/getPublicKey", {}, function(response) {
			Common.responseHandler(response, function(){
				try {
	                if (response.isSuccess && response.data.publicKey) {
	                    let crypt = new JSEncrypt();
	                    crypt.setPrivateKey(response.data.publicKey);

	                    let encryptParam = {
	                   		'juminNo' : crypt.encrypt($("#offgive #jumin1").val() + $("#offgive #jumin2").val())
	                   		, 'userName' : $("#offgive #userName").val()
	                   		, 'locgovCode' : ""
	                    };

	                    if($("#serviceResult").val() === "3") {
	                    	encryptParam.userName = $("#locUserName").val();
	                	}

	                    getUserAddressInfoForeign(encryptParam);
	                } else {
	                	alert("데이터 암/복호화에 실패했습니다.");
	                }
	            } catch(e) {
	            	console.log = () => {};
	            }
			});
		});
	} else {
		$.post(url("/opmanager/offgive/getUserAddressInfo"), param, function(response) {

			if(response.isSuccess && response.data) {
				if(response.data.serviceResult === '1') {
					txprSp = "01";		// 내국인
					alert("행정주소 검색이 성공하였습니다.");
					if ($("input:radio[name='infoAgreAt']:checked").val() == '1') {	// 1 : 동의 , 0 : 비동의
						$("#psitnLocgovUpperCode").val(response.data.upper_locgov_mapng_code);
						$("#psitnLocgovCode option").val(response.data.locgov_mapng_code);
						$("#psitnLocgovCode option").text(response.data.locgov_nm);
					}

					$("#infoAgreAt_1_psitnLocgovUpperCodeTmp").val(response.data.upper_locgov_mapng_code);
					$("#infoAgreAt_1_psitnLocgovCodeTmp").val(response.data.locgov_mapng_code);
					$("#infoAgreAt_1_psitnLocgovCodeTmp").val(response.data.locgov_mapng_code);

					$("#juso").val(response.data.juso);
				} else if(response.data.serviceResult === '3') {
					$("#serviceResult").val("3");
					alert(response.data.serviceResultMsg);
					$("#locUserSpan").show();
				} else {
					alert(response.data.serviceResultMsg);
				}
			} else {
				alert(response.errorMessage);
			}
		});
	}
}

//주소정보 못 얻어올 시 실명 인증 시 넘어오는 지자체 코드로 지자체 주소 정보 가져와서 세팅
function getAddLink () {
	//var addrChk = false;

	var lgCode = $("#infoAgreAt_1_psitnLocgovCodeTmp").val();
	if(lgCode == "") logGovCode = $("#infoAgreAt_2_psitnLocgovCodeTmp").val();

 	if(lgCode == ""){
 		//로그인 지자체코드 정보로 조회
 		lgCode = $("#loginLocGovCode").val();
 	}

 	//지차체 정보
	$.ajaxSetup({
		async: false
	});
	$.get("/opmanager/offgive/view/"+lgCode, {}, function(response) {
		if(response.isSuccess && response.data) {
			$("#juso").val(response.data.bassAdres);
		} else {
			alert("해당 지자체 정보를 확인 할 수 없습니다.");
			return false;
		}
	});


    var frm = document.AKCFrm;
    var jusoVal = $("#juso").val().split(',');
    frm.keyword.value = jusoVal[0];

	$.ajax({
        url :"https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do"  //인터넷망
        ,type:"post"
        ,data:$("#AKCFrm").serialize()
        ,dataType:"jsonp"
        ,crossDomain:true
        ,async: false
        ,success:function(xmlStr){
        	//console.log("xmlStr", xmlStr);

            var xmlData = xmlStr.returnXml;
            var totCnt = $(xmlData).find("totalCount").text();	// 결과 건 수
            var rnMgtSn = $(xmlData).find("rnMgtSn").text();    // 도로명 코드
            var udrtYn = $(xmlData).find("udrtYn").text();      // 지하여부
            var buldMnnm = $(xmlData).find("buldMnnm").text();  // 건물본번
            var buldSlno = $(xmlData).find("buldSlno").text();    // 건물부번
            var zipNo = $(xmlData).find("zipNo").text();      // 우편번호
            var admCd = $(xmlData).find("admCd").text();    // 행정동코드
            var bdMgtSn = $(xmlData).find("bdMgtSn").text();    // 건물관리번호
            var roadAddr = $(xmlData).find("roadAddrPart2").text();    // 상세주소

            $("#addrRnMgtSn").val(rnMgtSn);
       		$("#addrUdrtYn").val(udrtYn);
       		$("#addrBuldMnnm").val(buldMnnm);
       		$("#addrBuldSlno").val(buldSlno);
       		$("#addrZipNo").val(zipNo);
       		$("#addrAdmCd").val(admCd);
    		$("#addrBdMgtSn").val(bdMgtSn);
      		$("#addrBoadAddr").val(roadAddr);

      		if(totCnt == "0"){
      			alert("해당 지자체 정보를 확인 할 수 없습니다. 관리자에게 문의해 주세요.");
      	    	return false;
      		}else{
      			//addrChk = true;
      			goContryDonation();
      		}
      		//if(addrChk) goContryDonation();
        }
	});
}

//주소정보 못 얻어올 시 실명 인증 시 넘어오는 지자체 코드로 지자체 주소 정보 가져와서 세팅
function getAddLink2 () {
	//var addrChk = false;

	var lgCode = $("#infoAgreAt_1_psitnLocgovCodeTmp").val();
	if(lgCode == "") logGovCode = $("#infoAgreAt_2_psitnLocgovCodeTmp").val();

 	if(lgCode == ""){
 		//로그인 지자체코드 정보로 조회
 		lgCode = $("#loginLocGovCode").val();
 	}

 	//지차체 정보
	$.ajaxSetup({
		async: false
	});
	$.get("/opmanager/offgive/view/"+lgCode, {}, function(response) {
		if(response.isSuccess && response.data) {
			$("#juso").val(response.data.bassAdres);
		} else {
			alert("해당 지자체 정보를 확인 할 수 없습니다.");
			return false;
		}
	});


    var frm = document.AKCFrm;
    var jusoVal = $("#juso").val().split(',');
    frm.keyword.value = jusoVal[0];

	$.ajax({
        url :"https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do"  //인터넷망
        ,type:"post"
        ,data:$("#AKCFrm").serialize()
        ,dataType:"jsonp"
        ,crossDomain:true
        ,async: false
        ,success:function(xmlStr){
        	//console.log("xmlStr", xmlStr);

            var xmlData = xmlStr.returnXml;
            var totCnt = $(xmlData).find("totalCount").text();	// 결과 건 수
            var rnMgtSn = $(xmlData).find("rnMgtSn").text();    // 도로명 코드
            var udrtYn = $(xmlData).find("udrtYn").text();      // 지하여부
            var buldMnnm = $(xmlData).find("buldMnnm").text();  // 건물본번
            var buldSlno = $(xmlData).find("buldSlno").text();    // 건물부번
            var zipNo = $(xmlData).find("zipNo").text();      // 우편번호
            var admCd = $(xmlData).find("admCd").text();    // 행정동코드
            var bdMgtSn = $(xmlData).find("bdMgtSn").text();    // 건물관리번호
            var roadAddr = $(xmlData).find("roadAddrPart2").text();    // 상세주소

            $("#addrRnMgtSn").val(rnMgtSn);
       		$("#addrUdrtYn").val(udrtYn);
       		$("#addrBuldMnnm").val(buldMnnm);
       		$("#addrBuldSlno").val(buldSlno);
       		$("#addrZipNo").val(zipNo);
       		$("#addrAdmCd").val(admCd);
    		$("#addrBdMgtSn").val(bdMgtSn);
      		$("#addrBoadAddr").val(roadAddr);

      		if(totCnt == "0"){
      			alert("해당 지자체 정보를 확인 할 수 없습니다. 관리자에게 문의해 주세요.");
      	    	return false;
      		}else{
      			//addrChk = true;
      			nextBugaRequest();
      		}
      		//if(addrChk) goContryDonation();
        }
	});
}

//표준 세외(현세대) 부과정보 웹서비스 호출 & 위택스 결재창 open
function goContry (){

	var frm = document.AKCFrm;

    var jusoVal = $("#juso").val().split(',');

    frm.keyword.value = jusoVal[0];
    //var addrChk = false;
//  frm.keyword.value = $("#juso").val().replace(/\,/g,''); // 기존(버그있어서 수정))
    $.ajax({
        url :"https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do"  //인터넷망
        ,type:"post"
        ,data:$("#AKCFrm").serialize()
        ,dataType:"jsonp"
        ,crossDomain:true
        ,async: false
        ,success:function(xmlStr){
        	//console.log("xmlStr", xmlStr);

            var xmlData = xmlStr.returnXml;
            var totCnt = $(xmlData).find("totalCount").text();	// 결과 건 수
            var rnMgtSn = $(xmlData).find("rnMgtSn").text();    // 도로명 코드
            var udrtYn = $(xmlData).find("udrtYn").text();      // 지하여부
            var buldMnnm = $(xmlData).find("buldMnnm").text();  // 건물본번
            var buldSlno = $(xmlData).find("buldSlno").text();    // 건물부번
            var zipNo = $(xmlData).find("zipNo").text();      // 우편번호
            var admCd = $(xmlData).find("admCd").text();    // 행정동코드
            var bdMgtSn = $(xmlData).find("bdMgtSn").text();    // 건물관리번호
            var roadAddr = $(xmlData).find("roadAddrPart2").text();    // 상세주소

            $("#addrRnMgtSn").val(rnMgtSn);
       		$("#addrUdrtYn").val(udrtYn);
       		$("#addrBuldMnnm").val(buldMnnm);
       		$("#addrBuldSlno").val(buldSlno);
       		$("#addrZipNo").val(zipNo);
       		$("#addrAdmCd").val(admCd);
    		$("#addrBdMgtSn").val(bdMgtSn);
      		$("#addrBoadAddr").val(roadAddr);

            if(totCnt == "0" || totCnt == undefined){
            	getAddLink();
            }else{
            	//addrChk = true;
            	goContryDonation();
            }

            //if(addrChk) goContryDonation();
        },error: function(xhr,status, error){
            //alert("에러발생");
            alert("검색에 실패하였습니다 \n 다시 검색하시기 바랍니다.");
        }
    });

}


//표준 세외(현세대) 부과정보 웹서비스 호출 & 위택스 결재창 open
function goContry2 (){

	var frm = document.AKCFrm;

  var jusoVal = $("#juso").val().split(',');

  frm.keyword.value = jusoVal[0];
  //var addrChk = false;
//frm.keyword.value = $("#juso").val().replace(/\,/g,''); // 기존(버그있어서 수정))
  $.ajax({
      url :"https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do"  //인터넷망
      ,type:"post"
      ,data:$("#AKCFrm").serialize()
      ,dataType:"jsonp"
      ,crossDomain:true
      ,async: false
      ,success:function(xmlStr){
      	//console.log("xmlStr", xmlStr);

          var xmlData = xmlStr.returnXml;
          var totCnt = $(xmlData).find("totalCount").text();	// 결과 건 수
          var rnMgtSn = $(xmlData).find("rnMgtSn").text();    // 도로명 코드
          var udrtYn = $(xmlData).find("udrtYn").text();      // 지하여부
          var buldMnnm = $(xmlData).find("buldMnnm").text();  // 건물본번
          var buldSlno = $(xmlData).find("buldSlno").text();    // 건물부번
          var zipNo = $(xmlData).find("zipNo").text();      // 우편번호
          var admCd = $(xmlData).find("admCd").text();    // 행정동코드
          var bdMgtSn = $(xmlData).find("bdMgtSn").text();    // 건물관리번호
          var roadAddr = $(xmlData).find("roadAddrPart2").text();    // 상세주소

          $("#addrRnMgtSn").val(rnMgtSn);
			$("#addrUdrtYn").val(udrtYn);
     		$("#addrBuldMnnm").val(buldMnnm);
     		$("#addrBuldSlno").val(buldSlno);
     		$("#addrZipNo").val(zipNo);
     		$("#addrAdmCd").val(admCd);
  		$("#addrBdMgtSn").val(bdMgtSn);
    		$("#addrBoadAddr").val(roadAddr);

          if(totCnt == "0" || totCnt == undefined){
          	getAddLink2();
          }else{
          	//addrChk = true;
          	nextBugaRequest();
          }

          //if(addrChk) goContryDonation();
      },error: function(xhr,status, error){
          //alert("에러발생");
          alert("검색에 실패하였습니다 \n 다시 검색하시기 바랍니다.");
      }
  });

}

function goContryDonation () {

    var js_date = new Date();
    var year = js_date.getFullYear();
    var month = js_date.getMonth() + 1;
    var params2 = {
        'systemCd': "03",       // 인터페이스 구분코드
        'jijacheCd': $("#locgovCode").val(),   // 기부지자체 코드
        'deptCd': "99999999999",    // 자치단체 부서코드
        'fisyy' : year,         // 회계년도
        'fisSp' : '',        // 회계구분코드
        'ptclCd' : "260004",    // 세목코드
        'taxAmt' : $("#cntrAmt").val().replace(/[^\d]+/g, ''),   // 기부금액
        'impsSp' : "02",        // 부과구분
        'decsSp' : "02",        // 감경구분
        // 'txprSp' : "01",        // 납부자구분
        'txprSp' : txprSp,        // 납부자구분
        'txprNo' : $("#jumin1").val()+$("#jumin2").val(), // 납부자번호
        'txprNm' : $("#userName").val(), // 납부자 성명
        'newAddrYn' : "1",      // 새주소 여부
        'txprRoadCd' : $("#addrRnMgtSn").val(),    // 도로명 코드
        'txprBdFlrSp' : $("#addrUdrtYn").val(),    // 지하여부
        'txprBdPrcpNo' : $("#addrBuldMnnm").val(), // 건물본번
        'txprBdSubNo' : $("#addrBuldSlno").val(),  // 건물부번
        'statCd' : "10",        // 납부자상태
        'spclFisBizCd' : "092020",   // 특별회계사업코드
        'txprZipCd' : $("#addrZipNo").val(),       // 우편번호
        'txprTwnvilCd' : $("#addrAdmCd").val(),    // 행정동코드
        'txprBdMngNo' : $("#addrBdMgtSn").val(),   // 건물관리번호
        'txprDtlAddr' : $("#addrBoadAddr").val(),  // 상세주소
        'objNm' : "고향사랑기부금",     // 물건지명
        'taxObjSp' : "15",            // 부과대상구분코드
        'taxObjNewAddrYn' : "1",        // 물건지 새주소 여부
        'mngHtm1' : "고향사랑기부금",   // 기부금 명칭 및 기타항목
        'mngHtm5' : "",                 // 기부금 시스템키(유일키)
        'sysCd': "S020",        // 시스템 코드
        'intgrtnSp' : "02",     // 통합구분
        'selectedRegionCd' : $("#locgovCode").val(),
        'userRegionCd'  : $("#addrAdmCd").val().substring(0,5),	// 행정구역코드
        'enapbuNo'  : '',
        'presentType' : $("input:radio[name='rtnpsntReqstCode']:checked").val()
    }

    //console.log('params2', params2);
    // 표준 세외(현세대) 부과정보 웹서비스
    $.post(url("/opmanager/offgive/getCdonationCharge"), params2, function(response) {
		//console.log('response', response);

		if(response.isSuccess && response.data) {
			$("#elctrnPayNo").val(response.data.elctPayNo);	// 전자납부번호

			goSave();
		} else {
			alert(response.errorMessage);
		}
	});

}

function nextBugaRequest() {

    var params2 = {
        'frstPctAmt' : $("#cntrAmt").val().replace(/[^\d]+/g, ''),   // 기부금액
        'pyrNo' : $("#jumin1").val()+$("#jumin2").val(), // 납부자번호
        'pyrNm' : $("#userName").val(), // 납부자 성명
        'roadNmCd' : $("#addrRnMgtSn").val(),    // 도로명 코드
        'bmno' : $("#addrBuldMnnm").val(), // 건물본번
        'bsno' : $("#addrBuldSlno").val(),  // 건물부번
        'zip' : $("#addrZipNo").val(),       // 우편번호
        'dongCd' : $("#addrAdmCd").val(),    // 행정동코드
        'roadNmDaddr' : $("#addrBoadAddr").val(),  // 상세주소
        'glNm' : "고향사랑기부금",     // 물건지명
        'selectedRegionCd' : $("#locgovCode").val(),
        'cntrLocgovCode': $("#locgovCode").val(),
        'userRegionCd'  : $("#addrAdmCd").val().substring(0,5),	// 행정구역코드
        'presentType' : $("input:radio[name='rtnpsntReqstCode']:checked").val(),
        'prjId' : $("#prjId").val()	// 지정기부 초기값
    }

    $.post(url("/opmanager/offgive/nextBugaRequest"), params2, function(res) {

    	if(res.data.linkRstCd === undefined || res.data.linkRstCd === 'undefined')
    	{
    		alert("지방세외 시스템 연결에 실패하였습니다.");
    	}
    	else if(res.data.linkRstCd === '000' && res.data.epayNo)
    	{
    		$("#elctrnPayNo").val(res.data.epayNo);	// 전자납부번호
    		$("#linkMngKey").val(res.data.linkMngKey);	// linkMngKey

    		goSave();
    	}
    	else
    	{
    		alert("기부 부과등록에 실패하였습니다.");
    	}
    });


}

function goSave() {
		$("#psitnLocgovUpperCode").prop("disabled", false);
		$("#psitnLocgovCode").prop("disabled", false);
		$("#post").prop("disabled", false);
		$("#address").prop("disabled", false);
		$("#addressDetail").prop("disabled", false);
		$("#userName").prop("disabled", false);

		$("#birthday").val(getDateOfBirth($("#offgive #jumin1").val(), $("#offgive #jumin2").val()));	// 성별
		$("#gender").val(getGender($("#offgive #jumin2").val()));									// 생년월일
		$("#cntrAmt").val($("#cntrAmt").val().replace(/,/g, ""));									// 금액 , 제거

		$.post(url("/opmanager/offgive/create"), $('#offgive').serialize(), function(response) {
			//console.log('response', response);

			//if(response.isSuccess && response.data) {
			if(response.isSuccess) {
				location.href = "/opmanager/offgive/detail/" + response.data
			} else {
				alert(response.errorMessage);

				// 등록 실패시 다시 disabled true
		 		$("#psitnLocgovUpperCode").prop("disabled", true);
		 		$("#psitnLocgovCode").prop("disabled", true);
		 		$("#post").prop("disabled", true);
		 		$("#address").prop("disabled", true);
		 		$("#addressDetail").prop("disabled", true);
		 		$("#userName").prop("disabled", true);
			}
	    });
}

//서울시 세외 부과정보 웹서비스 호출 & 이택스 결재창 open
function goSeoul() {

    var semokCd = "";
    if($("#locgovCode").val() == "11000"){
        semokCd = "11228802";
    }else{
        semokCd = "51228802";
    }
    var js_date = new Date();
    var year = js_date.getFullYear();
    var month = js_date.getMonth() + 1;
    var params = {
        'systemCd': "01",               // 인터페이스 구분코드
        'jijacheCd': $("#locgovCode").val(),   // 기부지자체 코드
        'siguCd': "6110000",
        'semokCd': semokCd,
        'taxYm': year+month,
        'taxGubun': "3",
        'sidoCd' : "11",
        'napId' : $("#jumin1").val()+$("#jumin2").val(),
        'napNm' : $("#userName").val(),
        'napGubun' : "10",
        'taxAmt' : $("#cntrAmt").val().replace(/[^\d]+/g, ''),
        'sise' : $("#cntrAmt").val().replace(/[^\d]+/g, ''),
        'resideStatus' : "10",
        'mulGubun' : '03',
        'mulNm' : "고향사랑기부금", // $("#juso").val()
        'bookNo' : "", // api 에서 채번 => 수정 필요
        'sysGubun' : "LVHT",
        'selectedRegionCd' : $("#locgovCode").val(),
        'userRegionCd'  : $("#psitnLocgovCode").val(),
        'enapbuNo'  : '',
        'presentType' : $("input:radio[name='rtnpsntReqstCode']:checked").val()
    }

    // 서울시 세외 수입시스템 부과정보 등록 서비스 호출
    $.post(url("/opmanager/offgive/getSdonationCharge"), params, function(response) {
		//console.log('response', response);

		if(response.isSuccess && response.data) {
			$("#elctrnPayNo").val(response.data.elctPayNo);	// 전자납부번호

			goSave();
		} else {
			alert(response.errorMessage);
		}
	});

}


function isForeign(value) {
	try {
		let firstChar = value.substring(0, 1);
		switch (firstChar) {
			case "5" :
			case "6" :
			case "7" :
			case "8" :
				return true;
			default :
				return false;
		}
	} catch (e) {
		console.log = () => {};
		return false;
	}
}

function getUserAddressInfoForeign(encryptParam) {
	setTimeout(function () {
		Common.loading.show();
	}, 1);
    $.post("/opmanager/offgive/getUserAddressInfoForeign", encryptParam, function (rsp) {
    	Common.loading.hide();
		Common.responseHandler(rsp, function(){
	    	if (!rsp.isSuccess) {
	    		alert(rsp.errorMessage);
	    	} else if (rsp.data.errCode) {
	            alert('외국인 거소정보 조회에 실패했습니다.');
	        } else if (rsp.data.rst == 'SUCCESS') {       // 기부 가능
	        	txprSp = "05";		// 외국인
	            alert("행정주소 검색이 성공하였습니다.");
	            if (rsp.data.isTodayMsg) {
	                //alert(rsp.data.isTodayMsg,"postBtn");
	            }

				if ($("input:radio[name='infoAgreAt']:checked").val() == '1') {	// 1 : 동의 , 0 : 비동의
					$("#psitnLocgovUpperCode").val(rsp.data.upper_locgov_mapng_code);
					$("#psitnLocgovCode option").val(rsp.data.locgov_mapng_code);
					$("#psitnLocgovCode option").text(rsp.data.locgov_nm);
				}

				$("#infoAgreAt_1_psitnLocgovUpperCodeTmp").val(rsp.data.upper_locgov_mapng_code);
				$("#infoAgreAt_1_psitnLocgovCodeTmp").val(rsp.data.locgov_mapng_code);
				$("#infoAgreAt_1_psitnLocgovCodeTmp").val(rsp.data.locgov_mapng_code);

				$("#juso").val(rsp.data.juso);
	        } else if (rsp.data.rst == 'IN_LOCGOV') {
	            alert("자신의 거소지 지자체에는 기부를 하실 수 없습니다.");
				if ($("input:radio[name='infoAgreAt']:checked").val() == '1') {	// 1 : 동의 , 0 : 비동의
					$("#psitnLocgovUpperCode").val(rsp.data.upper_locgov_mapng_code);
					$("#psitnLocgovCode option").val(rsp.data.locgov_mapng_code);
					$("#psitnLocgovCode option").text(rsp.data.locgov_nm);
				}
				/*
				$("#infoAgreAt_1_psitnLocgovUpperCodeTmp").val(rsp.data.upper_locgov_mapng_code);
				$("#infoAgreAt_1_psitnLocgovCodeTmp").val(rsp.data.locgov_mapng_code);
				$("#infoAgreAt_1_psitnLocgovCodeTmp").val(rsp.data.locgov_mapng_code);

				$("#juso").val(rsp.data.juso);*/
	        } else if (rsp.data.rst == 'EXPIRED') {
	            alert("체류만료일이 지나 기부가 불가능합니다.");
	        } else {
	            alert("외국인 거소정보 조회에 실패했습니다.");
	        }
		});
    });
}

function getDesignatedDonationInfo(locgovCode) {
	if (!locgovCode) {
		return;
	}
	Common.loading.display = true;
	setTimeout(function () {
		Common.loading.show();
	}, 1);
	$("#prjId option").remove();
    $.post(
    	"/opmanager/offgive/getDesignatedDonationList"
    	, {'locgovCode' : locgovCode}
    	, function (rsp) {
	    	Common.loading.hide();
	    	Common.loading.display = false;
			Common.responseHandler(rsp, function(){
				$("#prjId").append('<option value="0">일반기부</option>');
				$("#prjId").val("0");

				if (rsp.isSuccess) {
					let prjList = rsp.data;
		    		let length = prjList.length;
		    		if (length > 0) {
		    			for(let i = 0 ; i < length ; i++) {
				            let options = '<option value="' + prjList[i].prjId + '">' + prjList[i].prjSubject + '</option>';
				            $('#prjId').append(options);
		    			}
		    		}
		    	}
			});
    	}
    );
}


</script>
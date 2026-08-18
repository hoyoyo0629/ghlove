<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span><c:out value="${op:message('MENU_1101')}"/></span></h3>

<!-- 관리자 권한 셋팅 -->
<c:choose>
	<c:when test="${adminRole eq 'ROLE_ADMIN_7'}">
		<c:set var="adminRoleType" value="OFF" />
		<c:set var="adminRoleGrade" value="M" />
	</c:when>
	<c:when test="${adminRole eq 'ROLE_ADMIN_8'}">
		<c:set var="adminRoleType" value="OFF" />
		<c:set var="adminRoleGrade" value="S" />
	</c:when>
	<c:when test="${(adminRole eq 'ROLE_ADMIN_1') or (adminRole eq 'ROLE_ADMIN_2')
					or (adminRole eq 'ROLE_ADMIN_3') or (adminRole eq 'ROLE_ADMIN_4')}">
		<c:set var="adminRoleType" value="SYS" />
	</c:when>
</c:choose>
<!-- 관리자 권한 셋팅 -->

<!-- 수정/삭제/목록 버튼 -->
<div class="btn_all btn_right mb15">
	<div class="flex_box gap-08">
		<button type="button" class="btn btn-defualt btn-mini" onclick="moveListPage();">목록</button>
		<button type="button" class="btn btn-dark-gray btn-mini" onclick="chargerCreate();">저장</button>
	</div>
</div>
<!--// 수정/삭제/목록 버튼 -->

<div class="board_write">
	<table class="board_write_table" summary="">
		<colgroup>
			<col style="width:220px;">
		</colgroup>
		<tbody>
			<tr>
				<td class="label">구분</td>
				<td>
					<div class="flex_box gap-12">
								<div class="input-form">
									<input id="authorityM" name="authority" type="radio" value="ROLE_ADMIN_7" disabled="disabled" >
									<label for="authorityM">오프라인 주담당자</label>
								</div>
								<div class="input-form">
									<input id="authorityS" name="authority" type="radio" value="ROLE_ADMIN_8" checked disabled="disabled">
									<label for="authorityS">오프라인 부담당자</label>
								</div>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>아이디</td>
				<td>
					<div>
						<input id="loginId" title="아이디" class="input_txt required _filter wd-200" type="text">
						<button id="userIdBtn" class="formBtn formBtn" type="button" onclick="checkIdUsedYn();">중복확인</button>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark"></span>비밀번호</td>
				<td>
					<div>
						<input id="password" title="비밀번호" placeholder="********" class="input_txt required _filter wd-200" type="password" disabled="true">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>이름</td>
				<td>
					<div>
						<input id="userName" title="이름" class="input_txt required _filter wd-200" type="text">
					</div>
				</td>
			</tr>
			<%-- <tr style="display:none">
				<td class="label"><span class="required_mark">*</span>담당자 연락처</td>
				<td>
					<div class="flex_box gap-08 item-center">
						<select id="phoneNumber1" title="담당자 연락처" class="wd-100" disabled>
							<option value="">-선택-</option>
							<c:forEach items="${phoneCodeList}" var="code" varStatus="status">
								<option value="${fn:escapeXml(code.id)}" <c:if test="${code.id eq fn:split(details.phoneNumber, '-')[0]}">selected</c:if>><c:out value="${code.label}"/></option>
							</c:forEach>
							<c:forEach items="${telCodeList}" var="code" varStatus="status">
								<option value="${fn:escapeXml(code.id)}" <c:if test="${code.id eq fn:split(details.phoneNumber, '-')[0]}">selected</c:if>><c:out value="${code.label}"/></option>
							</c:forEach>
						</select>
						<span class="wave">-</span>
						<input id="phoneNumber2" title="" class="input_txt required _filter wd-100 _number" type="text" maxLength="4" value="${fn:split(details.phoneNumber, '-')[1]}" disabled>
						<span class="wave">-</span>
						<input id="phoneNumber3" title="" class="input_txt required _filter wd-100 _number" type="text" maxLength="4" value="${fn:split(details.phoneNumber, '-')[2]}" disabled>
					</div>
				</td>
			</tr> --%>

			<tr>
				<td class="label"><span class="required_mark">*</span>지점 연락처</td>
				<td>
					<div class="flex_box gap-08 item-center">
						<select id="phoneNumber1" title="지점 연락처" class="wd-100">
							<option value="">-선택-</option>
							<c:forEach items="${phoneCodeList}" var="code" varStatus="status">
								<option value="${fn:escapeXml(code.id)}" <c:if test="${code.id eq fn:split(details.phoneNumber, '-')[0]}">selected</c:if>><c:out value="${code.label}"/></option>
							</c:forEach>
							<c:forEach items="${telCodeList}" var="code" varStatus="status">
								<option value="${fn:escapeXml(code.id)}" <c:if test="${code.id eq fn:split(details.phoneNumber, '-')[0]}">selected</c:if>><c:out value="${code.label}"/></option>
							</c:forEach>
						</select>
						<span class="wave">-</span>
						<input id="phoneNumber2" title="" class="input_txt required _filter wd-100 _number" type="text" maxLength="4">
						<span class="wave">-</span>
						<input id="phoneNumber3" title="" class="input_txt required _filter wd-100 _number" type="text" maxLength="4">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>소속 지점</td>
				<td>
					<div class="flex_box gap-08 item-center">
							<select id="bankCode" title="소속 지점" class="wd-100">
								<c:forEach items="${bankCodeList}" var="code" varStatus="status">
									<option value="${fn:escapeXml(code.id)}" <c:if test="${code.id eq details.bankCode}">selected</c:if>><c:out value="${code.label}"/></option>
								</c:forEach>
							</select>
						<input id="psitnNm" title="" class="input_txt required _filter wd-150" type="text">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>개인번호</td>
				<td>
					<div class="flex_box gap-08 item-center">
						<input id="empId" title="개인번호" class="input_txt required _filter wd-200" type="text" maxlength="20">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>EMAIL</td>
				<td>
					<div>
						<input id="email" title="EMAIL" class="input_txt required _filter wd-200" type="text">&nbsp;@
						<input id="emailDomain" title="EMAILDOMAIN" placeholder="nonghyup.com" class="input_txt required _filter wd-200" type="text" disabled="true">
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!-- 목록 검색 조건 -->
<input type="hidden" id="listSearchParam" />
<!--// 목록 검색 조건  -->

<script type="text/javascript">
	$(function() {

		// 네비게이션 수정
		$(".contents .contents_inner").find("div.location a").removeClass("on");
		$(".contents .contents_inner").find("div.location").append('> <a href="'+location.pathname+'" class="on">등록</a>');

		// 목록 검색 조건 저장
		listSearchParamSave();

		// 지점담당자(부담당자)의 최초 접속인 경우 alert 활성화
		if($("#listSearchParam").val().indexOf("pageEvent=change") > -1) {
			alert("지점 정보를 입력하지 않으시면 다른 페이지 이용이 제한됩니다.");
		}

		// 관리자 권한에 따른 화면 체크
		adminRoleCheck();

		// 이름 공백 제거
		if($("#userName").length) {
			$("#userName").keyup(function(e) {
				if(e && e.target && e.target.value) {
					e.target.value = e.target.value.replace(/\s/g, "");
				}
			});
		}
	});

	let checkDuplication = false;

	/**
	 *	함 수 명 : adminRoleCheck
	 *	기	능  : 관리자 권한에 따른 화면 체크
	 */
	function adminRoleCheck() {
		if("${fn:escapeXml(adminRole)}" == "ROLE_ADMIN_7" && "${fn:escapeXml(loginUserId)}" == "${fn:escapeXml(details.userId)}") {
			$("input[name=statusCode]").prop("disabled", true);
		}
	}

	/**
	 *	함 수 명 : listSearchParamSave
	 *	기	능  : 목록 검색 조건 저장
	 */
	function listSearchParamSave() {

		// 목록 검색 조건 저장
		$("#listSearchParam").val(location.search);

		// 검색 조건 삭제
		history.replaceState({}, null, location.pathname);
	}

	/**
	 *	함 수 명 : moveListPage
	 *	기	능  : 목록 페이지 이동
	 */
	function moveListPage() {
		location.href = "/opmanager/user/off-charger/list"+$("#listSearchParam").val();
	}

	/**
	 *	함 수 명 : chargerCreate
	 *	기	능  : 오프라인담당자 등록
	 */
	function chargerCreate() {
		 // todo validation check 보강 필요
		if(isNaN($("#phoneNumber2").val())){
			alert("전화번호는 숫자로 입력해 주세요.");
			$("#phoneNumber2").focus();
			return false;
		}
		if(isNaN($("#phoneNumber3").val())){
			alert("전화번호는 숫자로 입력해 주세요.");
			$("#phoneNumber3").focus();
			return false;
		}
		var confirmMsg = "신규 오프라인 담당자를 등록하시겠습니까?";
		var resultMsg = "등록되었습니다.";

		if(validator() && confirm(confirmMsg)) {
			// 이름 공백 제거
			if($("#userName").val()) {
				$("#userName").val($("#userName").val().replace(/\s/g, ""));
			}

			const phoneParts = [
				$("#phoneNumber1").val(),
				$("#phoneNumber2").val(),
				$("#phoneNumber3").val()
			].filter(v => v);


			// 파라미터
			var param = {
				"loginId": $("#loginId").val(),
				"password" : $("#password").val(),
				"authority": $("input[name='authority']:checked").val(),
				"userName": $("#userName").val(),
				"phoneNumber": phoneParts.length > 0 ? phoneParts.join("-") : "",
// 				"telNumber": $("#telNumber1").val()+"-"+ $("#telNumber2").val()+"-"+$("#telNumber3").val(),
				"bankCode": $("#bankCode").val(),
				"psitnNm": $("#psitnNm").val(),
				"empId": $("#empId").val(),
				"email": $("#email").val() + "@nonghyup.com"
			};

			$.post("/opmanager/user/off-charger/createProcess", param, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						alert(resultMsg);

						if($("#listSearchParam").val().indexOf("pageEvent") > -1) {
							location.href = "/opmanager";
						} else {
							$("#listSearchParam").val("");
							moveListPage();
						}
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}

	/**
	 *	함 수 명 : validator
	 *	기	능  : 유효성 검사
	 */
	function validator() {
		if(checkDuplication == false){
			alert("중복확인을 해주세요.");
			return false;
		}

		if(/<|>|&lt;|&gt;/.test($("#psitnNm").val())){
			alert("<, > 문자는 입력할 수 없습니다.");
			return false;
		}

// 		if($("input[name='authority']:checked").length == 0 || $("input[name='authority']:checked").val() == "") {
// 			alert("회원구분을 선택해 주세요.");
// 			$("#authorityM").focus();
// 			return false;
// 		}

		if($.trim($("#userName").val()) == "") {
			alert("이름을 입력해 주세요.");
			$("#userName").focus();
			return false;
		}

		if($("#phoneNumber1").val() == "") {
			alert("지점 연락처를 선택해 주세요.");
			$("#phoneNumber1").focus();
			return false;
		}

		if($.trim($("#phoneNumber2").val()) == "") {
			alert("지점 연락처를 입력해 주세요.");
			$("#phoneNumber2").focus();
			return false;
		}

		if($.trim($("#phoneNumber3").val()) == "") {
			alert("지점 연락처를 입력해 주세요.");
			$("#phoneNumber3").focus();
			return false;
		}

// 		if($("#telNumber1").val() == "") {
// 			alert("지점 연락처를 선택해 주세요.");
// 			$("#telNumber1").focus();
// 			return false;
// 		}

// 		if($.trim($("#telNumber2").val()) == "") {
// 			alert("지점 연락처를 입력해 주세요.");
// 			$("#telNumber2").focus();
// 			return false;
// 		}

// 		if($.trim($("#telNumber3").val()) == "") {
// 			alert("지점 연락처를 입력해 주세요.");
// 			$("#telNumber3").focus();
// 			return false;
// 		}

		if($("#bankCode").val() == "") {
			alert("소속 지점을 선택해 주세요.");
			$("#bankCode").focus();
			return false;
		}

		if($.trim($("#psitnNm").val()) =="") {
			alert("소속 지점을 입력해 주세요.");
			$("#psitnNm").focus();
			return false;
		}

		if($.trim($("#empId").val()) == "") {
			alert("개인번호를 입력해 주세요.");
			$("#empId").focus();
			return false;
		}

 		// srhan. 20251029. 이메일 인증 추가하며 '직책' 입력 UI를 '이메일'로 변경
		if($.trim($("#email").val()) == "") {
			alert("이메일을 입력해 주세요.");
			$("#email").focus();
			return false;
		}

// 		if($("input[name='statusCode']").length > 0
// 				&& ($("input[name='statusCode']:checked").length == 0 || $("input[name='statusCode']:checked").val() == "")) {
// 			alert("사용여부를 선택해 주세요.");
// 			$("#statusCode9").focus();
// 			return false;
// 		}

		return true;
	}

	function checkIdUsedYn() {

        checkDuplication = false;

        if ($("#loginId").val() == '') {
        	alert("아이디를 입력해주세요.")
            return false;
        }
        var param = {loginId: $("#loginId").val()}


        $.post("/opmanager/user/off-charger/getUserInfoByUserId", param, function(response) {
			if(response.isSuccess && response.data) {
				if(response.data.code == "SUCC") {
					alert("중복된 아이디입니다. 다른 아이디를 사용해주세요.");
				} else {
					alert("사용가능한 아이디 입니다.");
                    checkDuplication = true;
				}
			}
		});

    }


</script>
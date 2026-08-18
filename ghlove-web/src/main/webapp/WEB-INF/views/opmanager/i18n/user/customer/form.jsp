<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>
<style>
	.phone_number {width: 45px;}
</style>

	<h3><span>회원가입</span></h3>

	<form:form modelAttribute="user" method="post">
		<input type="hidden" name="siteFlag" value="0" />
		<input type="hidden" id="idCheck" />
		<div class="board_write">
			<table class="board_write_table" summary="회원가입에 관련한 정보를 입력하는 칸입니다.">
				<caption>회원가입</caption>
				<colgroup>
					<col style="width:15%;" />
					<col style="width:85%;" />
				</colgroup>
				<tbody>
					<tr>
						<td class="label">아이디 <span class="require">*</span></td>
						<td>
							<div class="input_wrap col-w-7">
								<form:input path="loginId" title="아이디" cssClass="two required _id" maxlength="100" />
								<button type="button" id="idCheckBtn" class="table_btn">중복검사</button>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">비밀번호 <span class="require"><span class="require">*</span></span></td>
						<td>
							<div class="input_wrap col-w-7">
								<input type="password"  name="password" id="password" title="비밀번호" class="half required _password _duplicated" minlength="4"  maxlength="20"/>
								<span>비밀번호는 영문/숫자/특수문자를 혼합하여 8자 이상 20자 이하로 입력하세요.</span>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">비밀번호 확인 <span class="require"><span class="require">*</span></span></td>
						<td>
							<div class="input_wrap col-w-7">
								<input type="password" name="password2" id="password_confirm" title="비밀번호 확인" class="half required _password _duplicated" minlength="4"  maxlength="20"/>
								<span>비밀번호 확인을 위하여 다시 한번 입력하여 주세요.</span>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">이름 <span class="require">*</span></td>
						<td>
							<div class="input_wrap col-w-7">
								<form:input path="userName" title="이름" maxlength="50" cssClass="two required" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">성별</td>
						<td>
							<div>
								<input type="radio" name="gender" id="gender1" value="M" title="남자" class="required" checked="checked" /><label for="gender1">남자</label>
								<input type="radio" name="gender" id="gender2" value="F" title="여자" class="required" /><label for="gender2">여자</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">생년월일</td>
						<td>
							<div>
								<p class="mt5">
									<input type="radio" name="birthdayType" id="birthdayType1" value="1" title="양력" checked="checked"><label for="birthdayType1">양력</label>
									<input type="radio" name="birthdayType" id="birthdayType2" value="2" title="음력"><label for="birthdayType2">음력</label>
									&nbsp;
									<select name="birthdayYear" title="생년월일 년" class="required">
										<option value="">선택</option>
										<c:forEach begin="0" end="100" step="1" var="index">
											<option value="${years - index}" label="${years - index}"><c:out value="${years - index}"/></option>
										</c:forEach>
									</select>년
									<select name="birthdayMonth" title="생년월일 월" class="required">
										<option value="">선택</option>
										<c:forEach begin="1" end="12" step="1" var="index">
											<option value="${index}" label="${index}"><c:out value="${index}"/></option>
										</c:forEach>
									</select>월
									<select name="birthdayDay" title="생년월일 일" class="required">
										<option value="">선택</option>
										<c:forEach begin="1" end="31" step="1" var="index">
											<option value="${index}" label="${index}"><c:out value="${index}"/></option>
										</c:forEach>
									</select>일
								</p>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">휴대폰 번호 <span class="require">*</span></td>
						<td>
							<div class="">
								<select class="custom_select large required" title="휴대폰번호 첫번째" id="phoneNumber1" name="phoneNumber1">
									<option selected="selected" value="">-선택-</option>
									<c:forEach items="${phoneCodes}" var="codes">
										<option><c:out value="${codes.label}"/></option>
									</c:forEach>
								</select> -
								<input type="text" name="phoneNumber2" title="휴대폰번호 두번째" class="phone_number required _number" maxlength="4" /> -
								<input type="text" name="phoneNumber3" title="휴대폰번호 세번째" class="phone_number required _number" maxlength="4" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">이메일 <span class="require">*</span></td>
						<td>
							<div>
								<input type="text" id="email" name="email" title="이메일" class="two required _email" maxlength="50" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">주소 <span class="require">*</span></td>
						<td>
							<div class="">
								우편번호 <input type="hidden" name="post" value="${fn:escapeXml(user.userDetail.post)}">
								<input type="text" name="newPost" id="newPost" value="${fn:escapeXml(user.userDetail.newPost)}" class="required" title="우편번호" maxlength="7" class="one" readonly="readonly">
								<button type="button" onclick="openDaumPostcode()" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> 검색</button>
								<p class="mt5">
									주소 <input type="text" name="address" id="address" title="주소" maxlength="100" class="half required disabled" readonly="readonly">
								</p>
								<p class="mt5">
									상세주소 <input type="text" name="addressDetail" id="addressDetail" title="상세주소" maxlength="100" class="half required">
								</p>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">SMS 수신동의 <span class="require">*</span></td>
						<td>
							<div>
								<input type="radio" name="receiveSms" id="receiveSms1" value="0" title="수신" class="required" checked="checked"><label for="receiveSms1">수신</label>
								<input type="radio" name="receiveSms" id="receiveSms2" value="1" title="수신안함" class="required" ><label for="receiveSms2">수신안함</label>
								<p class="mt5">
									쇼핑몰에서 제공하는 다양한 정보 sms를 받아보실 수 있습니다.<br/>
									(거래정보-결제/교환/환불 등과 관련된 내용은 고객님의 거래안전을 위하여 수신동의 여부와 관계없이 발송됩니다.)
								</p>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">Email 수신동의 <span class="require">*</span></td>
						<td>
							<div>
								<input type="radio" name="receiveEmail" id="receiveEmail1" value="0" title="수신" class="required" checked="checked"><label for="receiveEmail1">수신</label>
								<input type="radio" name="receiveEmail" id="receiveEmail2" value="1" title="수신안함" class="required" ><label for="receiveEmail2">수신안함</label>
								<p class="mt5">
									쇼핑몰에서 제공하는 다양한 정보 E-mail를 받아보실 수 있습니다.<br/>
									(거래정보-결제/교환/환불 등과 관련된 내용은 고객님의 거래안전을 위하여 수신동의 여부와 관계없이 발송됩니다.)
								</p>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div> <!--// board_write E-->
		<div class="btn_center_end">
			<button type="submit" class="btn btn-active"><c:out value="${op:message('M00101')}"/> <!-- 저장 --></button>
		</div>
	</form:form>

<!-- 다음 주소검색 -->
<daum:address />
<script type="text/javascript">
	var loginCheckId = '';
	$(function(){
		$('#user').validator(function() {
			if ($("#idCheck").val() == 0 || loginCheckId != $("#loginId").val()) {
				alert("${op:message('M00157')}");
				$("#idCheckBtn").focus();
				return false;
			}

			if (userAvailabilityCheck($("#loginId").val(), 'loginId') == false) {
				alert("이미 등록된 아이디입니다. 같은 아이디로 회원등록은 불가능합니다.");
				$("#loginId").focus();
				return false;
			}

			if ($("#password").val() != $("#password_confirm").val()) {
				alert("${op:message('M00158')}");
				$("#password_confirm").focus();
				return false;
			}

			Common.confirm("${op:message('M00159')}", function(form) {
				$('#user').submit();
			});

			return false;
		});

		$("#idCheckBtn").on('click',function(){
			var loginId = $("#loginId").val();

			if (loginId != '') {

				if (!$.validator.patterns._id.test(loginId)) {
					alert($.validator.messages._id);
					$("#loginId").focus();
					return;
				}

				var params = {
					'loginId' : loginId
				};

				$.post("/opmanager/user/find-user",params,function(response){
					Common.loading.hide();
					if (response.data > 0) {
						alert("이미 등록된 아이디입니다.");
						$("#idCheck").focus();
					} else {
						$("#idCheck").val('1');
						alert("사용가능합니다.");
						loginCheckId = $("#loginId").val();
					}
				});
			} else {
				alert("${op:message('M00162')}");
				return false;
			}
		});
	});

	function userAvailabilityCheck(value, type) {
		var params = {
			'type'		: type,
			'value' 	: value
		};

		var returnValue = false;
		$.post("/opmanager/user/user-availability-check", params, function(response){
			Common.loading.hide();
			returnValue = response.data;
		}, 'json').error(function(e){
			alert(e.message);
		});

		return returnValue;
	}

	function openDaumPostcode() {

		var tagNames = {
			'newZipcode'			: 'newPost',
			'zipcode' 				: 'post',
			'zipcode1' 				: 'post1',
			'zipcode2' 				: 'post2',
		}

		openDaumAddress(tagNames);

}
</script>


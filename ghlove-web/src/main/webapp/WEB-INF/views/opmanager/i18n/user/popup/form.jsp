<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

	<h2><span>회원정보 수정</span></h2>
	<form:form modelAttribute="user" method="post" enctype="multipart/form-data">
		<input type="hidden" name="receivePush" value="1" />

		<table cellpadding="0" cellspacing="0" summary="" class="board_list_table">
			<caption>회원정보 수정</caption>
			<colgroup>
				<col style="width:120px;" />
				<col style="width:*" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">아이디</th>
					<td class="tleft">
						<div>
							${fn:escapeXml(user.loginId)}
							<input type="hidden" name="loginId" value="${fn:escapeXml(user.loginId)}"/>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">회원 그룹</th>
					<td class="tleft">
						<div>
							<select id="groupCode" name="groupCode" title="회원 그룹">
								<option value="default">그룹 미지정</option>
								<c:forEach items="${groupList}" var="group">
									<option value="${fn:escapeXml(group.groupCode)}" ${op:selected(user.userDetail.groupCode, group.groupCode)}>${fn:escapeXml(group.groupName)}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">회원 Level</th>
					<td class="tleft">
						<div>
							<select id="levelId" name="levelId" title="회원 Level">
								<option value="0">Level 미지정</option>
								<c:forEach items="${userLevelGroup}" var="levelGroup">
									<c:set var="groupLabel">${levelGroup.key}</c:set>
									<c:forEach items="${groupList}" var="group">
										<c:if test="${groupLabel == group.groupCode}">
											<c:set var="groupLabel">${group.groupName}</c:set>
										</c:if>
									</c:forEach>
									<optgroup label="${fn:escapeXml(groupLabel)}">
										<c:forEach items="${levelGroup.value}" var="userLevel">
											<option value="${fn:escapeXml(userLevel.levelId)}" data-group-code="${fn:escapeXml(userLevel.groupCode)}" ${user.userDetail.levelId == userLevel.levelId ? "selected" : ""}>${fn:escapeXml(userLevel.levelName)}</option>
										</c:forEach>
									</optgroup>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">이름</th>
					<td class="tleft">
						<input type="text" id="userName" name="userName" title="이름" class="two required" value="${fn:escapeXml(user.userName)}" maxlength="50" />
					</td>
				</tr>
				<tr>
					<th scope="row">성별</th>
					<td class="tleft">
						<div>
							<input type="radio" name="gender" id="gender1" value="M" title="남자" class="${fn:escapeXml(required)}" ${op:checked(user.userDetail.gender,'M')} /><label for="gender1">남자</label>
							<input type="radio" name="gender" id="gender2" value="F" title="여자" class="${fn:escapeXml(required)}" ${op:checked(user.userDetail.gender,'F')} /><label for="gender2">여자</label>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">생년월일</th>
					<td class="tleft">
						<div>
							<p class="mt5">
								<input type="radio" name="birthdayType" id="birthdayType1" value="1" title="양력" ${op:checked(user.userDetail.birthdayType,'1')}><label for="birthdayType1">양력</label>
								<input type="radio" name="birthdayType" id="birthdayType2" value="2" title="음력" ${op:checked(user.userDetail.birthdayType,'2')}><label for="birthdayType2">음력</label>
								&nbsp;
								<select name="birthdayYear" title="생년월일 년">
									<option value="">선택</option>
									<c:forEach begin="0" end="100" step="1" var="index">
										<option value="${years - index}" label="${years - index}" ${op:selected(user.userDetail.birthdayYear, years - index)}>${years - index}</option>
									</c:forEach>
								</select>년
								<select name="birthdayMonth" title="생년월일 월">
									<option value="">선택</option>
									<c:forEach begin="1" end="12" step="1" var="index">
										<option value="${index}" label="${index}" ${op:selected(user.userDetail.birthdayMonth, index)}>${index}</option>
									</c:forEach>
								</select>월
								<select name="birthdayDay" title="생년월일 일">
									<option value="">선택</option>
									<c:forEach begin="1" end="31" step="1" var="index">
										<option value="${index}" label="${index}" ${op:selected(user.userDetail.birthdayDay, index)}>${index}</option>
									</c:forEach>
								</select>일
							</p>
						</div>
					</td>
				</tr>
				<tr>
					<th>휴대폰 번호</th>
					<td class="tleft">
						<div>
							<select class="required" title="휴대폰 번호 첫번째" id="phoneNumber1" name="phoneNumber1">
								<option value="" label="-선택-">-선택-</option>
								<c:forEach items="${op:getCodeInfoList('PHONE')}" var="codes">
									<option value="${fn:escapeXml(codes.detail)}" ${op:selected(user.userDetail.phoneNumber1, codes.detail)}> ${fn:escapeXml(codes.label)}</option>
								</c:forEach>
							</select>
							- <input type="text" name="phoneNumber2" title="휴대폰 번호 두번째" value="${fn:escapeXml(user.userDetail.phoneNumber2)}" class="one _number" maxlength="4" />
							- <input type="text" name="phoneNumber3" title="휴대폰 번호 세번째" value="${fn:escapeXml(user.userDetail.phoneNumber3)}" class="one _number" maxlength="4" />
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">이메일</th>
					<td class="tleft">
						<div>
							<input type="text" id="email" name="email" title="이메일" class="two required _email" value="${fn:escapeXml(user.email)}" maxlength="50" />
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row" rowspan="3">주소</th>
					<td class="tleft">
						<div>
							우편번호
							<input type="hidden" name="post" value="${fn:escapeXml(user.userDetail.post)}">
							<input type="text" name="newPost" id="newPost" value="${fn:escapeXml(user.userDetail.newPost)}" class="required" title="우편번호" maxlength="5" class="one" readonly="readonly">
							<a href="javascript:;" onclick="openDaumPostcode()" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> 우편번호 검색</a>
						</div>
					</td>
				</tr>
				<tr>
					<td class="tleft">
						<div>
							주소 <input type="text" name="address" id="address" value="${fn:escapeXml(user.userDetail.address)}" class="seven required" title="주소" maxlength="100" readonly="readonly" >
					 	</div>
					</td>
				</tr>
				<tr>
					<td class="tleft">
						<div>
							상세주소 <input type="text" name="addressDetail" id="addressDetail" value="${fn:escapeXml(user.userDetail.addressDetail)}" class="seven required" title="상세주소" maxlength="100" />
					 	</div>
					</td>
				</tr>
				<tr>
					<th scope="row">SMS 수신동의</th>
					<td class="tleft">
						<div>
 							<input type="radio" name="receiveSms" id="receiveSms1" value="0" title="${op:message('M00250')}" ${op:checked(user.userDetail.receiveSms,'0')} class="required" ><label for="receiveSms1">${op:message('M00233')}</label>
							<input type="radio" name="receiveSms" id="receiveSms2" value="1" title="${op:message('M00250')}" ${op:checked(user.userDetail.receiveSms,'1')} class="required" ><label for="receiveSms2">${op:message('M00234')}</label>
							<p class="mt5">
								쇼핑몰에서 제공하는 다양한 정보 sms를 받아보실 수 있습니다.<br/>
								(거래정보-결제/교환/환불 등과 관련된 내용은 고객님의 거래안전을 위하여 수신동의 여부와 관계없이 발송됩니다.)
							</p>
 						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">Email 수신동의</th>
					<td class="tleft">
						<div>
							<input type="radio" name="receiveEmail" id="receiveEmail1" value="0" title="${op:message('M00250')}" ${op:checked(user.userDetail.receiveEmail,'0')} class="required" /><label for="receiveEmail1">${op:message('M00233')}</label>
							<input type="radio" name="receiveEmail" id="receiveEmail2" value="1" title="${op:message('M00250')}" ${op:checked(user.userDetail.receiveEmail,'1')} class="required" /><label for="receiveEmail2">${op:message('M00234')}</label>
							<p class="mt5">
								쇼핑몰에서 제공하는 다양한 정보 E-mail를 받아보실 수 있습니다.<br/>
								(거래정보-결제/교환/환불 등과 관련된 내용은 고객님의 거래안전을 위하여 수신동의 여부와 관계없이 발송됩니다.)
							</p>
						</div>
					</td>
				</tr>
			</tbody>
		</table> <!-- // 기본정보 끝 -->

		<div class="popup_btns">
			<button type="submit" class="btn btn-active">${op:message('M00101')}</button> <!-- 저장 -->
		</div>
	</form:form>

<!-- 다음 주소검색 -->
<daum:address />

<script type="text/javascript">
$(function() {

	var hasRoleIsms = false;
	<sec:authorize access="hasRole('ROLE_ISMS')">
	hasRoleIsms = true;
	</sec:authorize>

	if (!hasRoleIsms) {
		// ROLE_ISMS 권한이 아닌 경우, 이메일 패턴 체크 제외
		$('#email').removeClass('_email');
	}

	// 메뉴 활성화
	Manager.activeUserDetails("modify");

    // [SKC] GroupCode와 LevelId 관계 맺기..
    handleGroupCodeAndLevelIdEvent();


	$('#user').validator(function() {

		if (!hasRoleIsms) {
			alert('수정 권한이 없습니다. (ISMS 권한 필요)');
			return false;
		}

		// 이메일 중복 확인
		var $email = $('input[name=email]');
		var isDuplicationEmail = false;
		if ($email.val() != $('#currentEmail').val()) {


			var params = {
				'loginId' : $email.val()
			};

			$.ajaxSetup({
				async: false
			});

			$.post("/users/find-user",params,function(response){
				Common.loading.hide();

				if (response.userCount > 0) {
					isDuplicationEmail = true;
				}
			});
		}

		if (isDuplicationEmail) {
			alert("${op:message('M00160')}");
			$email.focus();
			return false;
		}

		Common.confirm("${op:message('M00159')} ", function(form) {

			$('#user').submit();
		});
		return false;
	});
});

// [SKC] GroupCode와 LevelId 관계 맺기..
function handleGroupCodeAndLevelIdEvent() {
    var $groupCode = $('#groupCode');
    var $levelId = $('#levelId');

    $groupCode.on('change', function(e) {
        $levelId.val('0');  // 레벨 초기화 (미지정)
    });


    $levelId.on('change', function(e) {
        var groupCode = $levelId.find('option:selected').data('groupCode');
        if (groupCode == undefined) {
            groupCode = "default";
        }
        $groupCode.val(groupCode);
    });
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

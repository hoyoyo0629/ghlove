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

<!-- 수정/삭제/목록 버튼 -->
<div class="btn_all btn_right mb15">
	<div class="flex_box gap-08">
		<c:if test="${adminRole eq 'ROLE_ADMIN_1' or adminRole eq 'ROLE_ADMIN_3'}">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="chargerEdit();">수정</button>
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="chargerDelete();">삭제</button>
		</c:if>
		<button type="button" class="btn btn-defualt btn-mini" onclick="moveListPage();">목록</button>
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
				<td class="label"><span class="required_mark">*</span>회원구분</td>
				<td>
					<div class="flex_box gap-12">
						<div class="input-form">
							<input id="authoritySys_M" name="authority" type="radio" value="ROLE_ADMIN_1"
								${details.authority eq 'ROLE_ADMIN_1' ? 'checked' : ''}>
							<label for="authoritySys_M">시스템 주관리자</label>
						</div>
						<div class="input-form">
							<input id="authoritySys_S" name="authority" type="radio" value="ROLE_ADMIN_2"
								${details.authority eq 'ROLE_ADMIN_2' ? 'checked' : ''}>
							<label for="authoritySys_S">시스템 부관리자</label>
						</div>
						<div class="input-form">
							<input id="authorityGov_M" name="authority" type="radio" value="ROLE_ADMIN_3"
								${details.authority eq 'ROLE_ADMIN_3' ? 'checked' : ''}>
							<label for="authorityGov_M">행안부 주담당자</label>
						</div>
						<div class="input-form">
							<input id="authorityGov_S" name="authority" type="radio" value="ROLE_ADMIN_4"
								${details.authority eq 'ROLE_ADMIN_4' ? 'checked' : ''}>
							<label for="authorityGov_S">행안부 부담당자</label>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>아이디</td>
				<td><div><c:out value="${details.loginId}"/></div></td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>이름</td>
				<td><div><c:out value="${details.userName}"/></div></td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>휴대폰</td>
				<td><div><c:out value="${details.phoneNumber}"/></div></td>
			</tr>
			<tr>
				<td class="label">소속 부서</td>
				<td>
					<div>
						<c:choose>
							<c:when test="${adminRole eq 'ROLE_ADMIN_1' or adminRole eq 'ROLE_ADMIN_3'}">
								<input id="psitnDeptNm" title="담당부서" class="input_txt required _filter half" type="text" value="${fn:escapeXml(details.psitnDeptNm)}">
							</c:when>
							<c:otherwise>
								<c:out value="${details.psitnDeptNm}"/>
							</c:otherwise>
						</c:choose>

					</div>
				</td>
			</tr>
			<tr>
				<td class="label">직위</td>
				<td>
					<div>
						<c:choose>
							<c:when test="${adminRole eq 'ROLE_ADMIN_1' or adminRole eq 'ROLE_ADMIN_3'}">
								<input id="ofcpsNm" title="직위" class="input_txt required _filter half" type="text" value="${fn:escapeXml(details.ofcpsNm)}">
							</c:when>
							<c:otherwise>
								<c:out value="${details.ofcpsNm}"/>
							</c:otherwise>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>사용여부</td>
				<td>
					<div class="flex_box gap-12">
						<div class="input-form">
							<input id="statusCode9" name="statusCode" type="radio" value="9"
								${details.statusCode eq 9 ? 'checked' : ''}>
							<label for="statusCode9">사용</label>
						</div>
						<div class="input-form">
							<input id="statusCode2" name="statusCode" type="radio" value="2"
								${details.statusCode eq 2 ? 'checked' : ''}>
							<label for="statusCode2">중지</label>
						</div>
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
	$(function(){

		// 페이지 유효성 확인
		if("${fn:escapeXml(details.userId)}" == "") {
			alert("잘못된 접근입니다.");
			moveListPage();
		}

		// 네비게이션 수정
		$(".contents .contents_inner").find("div.location a").removeClass("on");
		$(".contents .contents_inner").find("div.location").append('> <a href="'+location.pathname+'" class="on">상세</a>');

		// 목록 검색 조건 저장
		listSearchParamSave();

		// 관리자 권한에 따른 화면 체크
		adminRoleCheck();
	});

	/**
	 *	함 수 명 : adminRoleCheck
	 *	기	능  : 관리자 권한에 따른 화면 체크
	 */
	function adminRoleCheck() {

		let authority = '${fn:escapeXml(details.authority)}';
		let adminRole = '${fn:escapeXml(adminRole)}';

		// 시스템 담당자인 경우 행안부 담당자 선택 불가
		if(authority == "ROLE_ADMIN_1" || authority == "ROLE_ADMIN_2" || adminRole == "ROLE_ADMIN_1" || adminRole == "ROLE_ADMIN_2") {
			$("#authorityGov_M, #authorityGov_S").prop("disabled", true);
		}

		// 행안부 담당자인 경우 시스템 담당자 선택 불가
		if(authority == "ROLE_ADMIN_3" || authority == "ROLE_ADMIN_4" || adminRole == "ROLE_ADMIN_3" || adminRole == "ROLE_ADMIN_4") {
			$("#authoritySys_M, #authoritySys_S").prop("disabled", true);
		}

		// 시스템, 행안부 주담당자인 경우 본인의 사용여부는 선택 불가
		if((adminRole == "ROLE_ADMIN_1" || adminRole == "ROLE_ADMIN_3") && "${fn:escapeXml(details.userId)}" == "${fn:escapeXml(loginUserId)}") {
			$("input[name=statusCode]").prop("disabled", true);
		}

		// 시스템 부관리자, 행안부 부담당자는 회원구분, 사용여부 라디오 버튼 선택 불가
		if(adminRole == "ROLE_ADMIN_2" || adminRole == "ROLE_ADMIN_4") {
			$("input[name=authority], input[name=statusCode]").prop("disabled", true);
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
		location.href = "/opmanager/user/oper-charger/list"+$("#listSearchParam").val();
	}

	/**
	 *	함 수 명 : chargerDelete
	 *	기	능  : 운영관리자 삭제
	 */
	function chargerDelete() {
		if(confirm("삭제하시겠습니까?")) {
			$.post("/opmanager/user/oper-charger/delete", {"userIdList": ["${fn:escapeXml(details.userId)}"]}, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						alert("삭제되었습니다.");

						if(response.data.isLogout == "Y") {
							location.href = "/op_security_logout?target=/opmanager";
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
	 *	함 수 명 : chargerEdit
	 *	기	능  : 운영관리자 수정
	 */
	function chargerEdit() {
		if($("input[name='authority']:checked").length == 0 || $("input[name='authority']:checked").val() == "") {
			alert("회원구분을 선택해 주세요.");
			$("#authoritySys_M").focus();
			return false;
		}

		if($("input[name='statusCode']:checked").length == 0 || $("input[name='statusCode']:checked").val() == "") {
			alert("사용여부를 선택해 주세요.");
			$("#statusCode9").focus();
			return false;
		}

		if(confirm("정보를 수정 하시겠습니까?")) {
			var param = {
				"userId": "${fn:escapeXml(details.userId)}",
				"authority": $("input[name='authority']:checked").val(),
				"psitnDeptNm": $("#psitnDeptNm").val(),
				"ofcpsNm": $("#ofcpsNm").val(),
				"statusCode": $("input[name='statusCode']:checked").val()
			};

			$.post("/opmanager/user/oper-charger/edit", param, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						alert("수정되었습니다.");
						$("#listSearchParam").val("");
						moveListPage();
					} else if(response.data.code == "ERR_MAIN_CNT") {
						alert("주관리자는 최대 2명까지만 등록이 가능합니다.");
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}
</script>
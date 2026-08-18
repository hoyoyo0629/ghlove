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

<h3><span><c:out value="${op:message('MENU_4402')}"/></span></h3>

<!-- 관리자 권한 셋팅 -->
<c:choose>
	<c:when test="${adminRole eq 'ROLE_ADMIN_5'}">
		<c:set var="adminRoleType" value="LOC" />
		<c:set var="adminRoleGrade" value="M" />
	</c:when>
	<c:when test="${adminRole eq 'ROLE_ADMIN_6'}">
		<c:set var="adminRoleType" value="LOC" />
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
		<c:if test="${adminRoleType eq 'SYS' || (adminRoleType eq 'LOC' and adminRoleGrade eq 'M')}">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="chargerEdit();">수정</button>
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="chargerDelete();" style="display:none;">삭제</button>
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
							<input id="authorityM" name="authority" type="radio" value="ROLE_ADMIN_5"
								 ${details.authority eq 'ROLE_ADMIN_5' ? 'checked' : ''}>
							<label for="authorityM">지자체 주관리자</label>
						</div>
						<div class="input-form">
							<input id="authorityS" name="authority" type="radio" value="ROLE_ADMIN_6"
								 ${details.authority eq 'ROLE_ADMIN_6' ? 'checked' : ''}>
							<label for="authorityS">지자체 부관리자</label>
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
				<td class="label"><span class="required_mark">*</span>지자체명</td>
				<td>
					<div class="flex_box gap-08">
						<c:choose>
							<c:when test="${adminRoleType eq 'SYS'}">
								<select id="upperLocgovCode" title="시" class="wd-150" onchange="changeUpperLocgov('N');">
									<option value="">- 시도 선택 -</option>
									<c:forEach items="${upperLocgovCodeList}" var="item" varStatus="i">
										<option value="${fn:escapeXml(item.id)}" ${fn:escapeXml(item.id) eq fn:escapeXml(details.upperLocgovCode) ? 'selected' : ''}><c:out value="${item.label}"/></option>
									</c:forEach>
								</select>
								<select id="locgovCode" title="구" class="wd-150">
									<option value="">- 시군구 -</option>
								</select>
							</c:when>
							<c:otherwise>
								<input type="hidden" id="locgovCode" value="${fn:escapeXml(details.locgovCode)}"/>
								<c:out value="${details.upperLocgovNm}"/>&nbsp;<c:out value="${details.locgovNm}"/>
							</c:otherwise>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label">담당부서</td>
				<td>
					<div>
						<c:choose>
							<c:when test="${adminRoleType eq 'SYS' || (adminRoleType eq 'LOC' and adminRoleGrade eq 'M')}">
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
							<c:when test="${adminRoleType eq 'SYS' || (adminRoleType eq 'LOC' and adminRoleGrade eq 'M')}">
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
		// 시스템&행안부 관리자인 경우 지자체 선택
		if("${fn:escapeXml(adminRoleType)}" == "SYS") {
			changeUpperLocgov('Y');
		}

		// 지자체 부관리자인 경우 회원구분, 사용여부 라디오 버튼 선택 불가
		if("${fn:escapeXml(adminRoleType)}" == "LOC" && "${fn:escapeXml(adminRoleGrade)}" == "S") {
			$("input[name=authority], input[name=statusCode]").prop("disabled", true);
		}

		// 지자체 주관리자인 경우 사용여부 라디오 버튼 선택 불가
		if("${fn:escapeXml(adminRoleType)}" == "LOC" && "${fn:escapeXml(adminRoleGrade)}" == "M" && "${fn:escapeXml(details.userId)}" == "${fn:escapeXml(loginUserId)}") {
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
		location.href = "/opmanager/user/locgov-charger/list"+$("#listSearchParam").val();
	}

	/**
	 *	함 수 명 : changeUpperLocgov
	 *	기	능  : 지자체(시도) 변경 이벤트
	 *	파라미터  : 초기화 여부 - Y/N
	 */
	function changeUpperLocgov(initYn) {
		var upperLocgovCode = $("#upperLocgovCode").val();
		$("#locgovCode").html("<option value=''>- 시군구 -</option>");

		if(upperLocgovCode) {
			$.get("/opmanager/user/locgov-charger/locgov/"+upperLocgovCode+"/list", {}, function(response) {
				if(response.isSuccess && response.data) {
					$.each(response.data, function(index, item) {
						$("#locgovCode").append('<option value="'+item.locgovCode+'">'+item.locgovNm+'</option>');
					});

					if(initYn == "Y") {
						$("#locgovCode").val("${fn:escapeXml(details.locgovCode)}");
					}
				}
			})
		}
	}

	/**
	 *	함 수 명 : chargerDelete
	 *	기	능  : 지자체 담당자 삭제
	 */
	function chargerDelete() {
		if(confirm("삭제하시겠습니까?")) {
			$.post("/opmanager/user/locgov-charger/delete", {"userIdList": ["${fn:escapeXml(details.userId)}"]}, function(response) {
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
	 *	기	능  : 지자체 담당자 수정
	 */
	function chargerEdit() {
		if($("input[name='authority']:checked").length == 0 || $("input[name='authority']:checked").val() == "") {
			alert("회원구분을 선택해 주세요.");
			$("#authorityM").focus();
			return false;
		}

		if("${fn:escapeXml(adminRoleType)}" == "SYS" && $("#upperLocgovCode").val() == "") {
			alert("상위 지자체를 선택해 주세요.");
			$("#upperLocgovCode").focus();
			return false;
		}

		if("${fn:escapeXml(adminRoleType)}" == "SYS" && $("#locgovCode").val() == "") {
			alert("지자체을 선택해 주세요.");
			$("#locgovCode").focus();
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
				"locgovCode": $("#locgovCode").val(),
				"psitnDeptNm": $("#psitnDeptNm").val(),
				"ofcpsNm": $("#ofcpsNm").val(),
				"statusCode": $("input[name='statusCode']:checked").val()
			};

			$.post("/opmanager/user/locgov-charger/edit", param, function(response) {
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
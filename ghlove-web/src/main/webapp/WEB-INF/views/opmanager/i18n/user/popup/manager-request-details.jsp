<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<!-- DataSet : 상태 별 서브 타이틀명 ex) (승인) / (거절) -->
<c:set var="titleConfmSttusNm" value="${details.confmSttusCode eq 200 ? '' : ('('+=details.confmSttusNm+=')')}" />
<!--// DataSet -->

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">관리자 권한 요청 <c:out value="${titleConfmSttusNm}"/></h1>
		<a href="javascript:self.close();" class="btn_close">
			<img src="/content/opmanager/images/btn/btn_close.png" alt="닫기">
		</a>
	</div>
	<div class="popup_contents">
		<table class="board_write_table" summary="관리자 권한 요청<c:out value='${titleConfmSttusNm}'/>">
			<caption>관리자 권한 요청<c:out value="${titleConfmSttusNm}"/></caption>
			<colgroup>
				<col style="width: 150px" />
				<col/>
			</colgroup>
			<tbody>
				<tr>
					<td class="label">아이디</td>
					<td colspan="3">
						<div><c:out value="${details.loginId}"/></div>
					</td>
				</tr>
				<tr>
					<td class="label">이름</td>
					<td colspan="3">
						<div><c:out value="${details.userName}"/></div>
					</td>
				</tr>
				<tr>
					<td class="label">생년월일</td>
					<td colspan="3">
						<div>
							<c:if test="${not empty details.birthday}">
								<c:set var="birthdayCustom" value="${fn:replace(details.birthday, '-', '')}" />
								<c:if test="${fn:length(birthdayCustom) eq 8}">
									<%-- <fmt:parseDate var="birthdayDateFmt" pattern="yyyyMMdd" value="${fn:escapeXml(birthdayCustom)}" />
									<fmt:formatDate var="birthdayStringFmt" pattern="yyyy-MM-dd" value="${fn:escapeXml(birthdayDateFmt)}" />
									<c:out value="${birthdayStringFmt}"/> --%>
									${op:formatDate(birthdayCustom, "-")}
								</c:if>
							</c:if>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">휴대폰</td>
					<td colspan="3">
						<div><c:out value="${details.phoneNumber}"/></div>
					</td>
				</tr>
				<tr>
					<td class="label">이메일 주소</td>
					<td colspan="3">
						<div><c:out value="${details.email}"/></div>
					</td>
				</tr>
				<tr>
					<td class="label">소속 구분</td>
					<td colspan="3">
						<div><c:out value="${details.reqstSeNm}"/></div>
					</td>
				</tr>
				<c:if test="${details.reqstSeCode eq 'ROLE_ADMIN_8' or details.reqstSeCode eq 'ROLE_ADMIN_6'}">
					<tr>
						<td class="label"><c:out value="${details.reqstSeCode eq 'ROLE_ADMIN_8' ? '소속은행' : '소속 지자체'}"/></td>
						<td colspan="3">
							<div>
								<c:choose>
									<c:when test="${details.reqstSeCode eq 'ROLE_ADMIN_6'}">
										<!-- 구분값이 '지자체' 인 경우 '소속' 필드에 선택된 지자체 표시 -->
										<c:out value="${details.upperlocgovNm}"/>&nbsp;<c:out value="${details.locgovNm}"/>
									</c:when>
									<c:when test="${details.reqstSeCode eq 'ROLE_ADMIN_8'}">
										<!-- 구분값이 '오프라인' 인 경우 '소속' 필드에 선택된 은행 정보 표시 -->
										<c:out value="${details.bankNm}"/>&nbsp;<c:out value="${details.psitnNm}"/>
									</c:when>
								</c:choose>
							</div>
						</td>
					</tr>
				</c:if>
				<tr>
					<td class="label">소속 부서</td>
					<td>
						<div><c:out value="${details.psitnDeptNm}"/></div>
					</td>
					<td class="label">직위</td>
					<td>
						<div><c:out value="${details.ofcpsNm}"/></div>
					</td>
				</tr>
				<!-- 상태 필드 : 200(대기), 100(승인), 300(거절) -->
				<c:choose>
					<c:when test="${details.confmSttusCode eq 200}">
						<!-- 상태 : 대기 -->
						<tr>
							<td class="label"><span class="required_mark">*</span>상태</td>
							<td colspan="3">
								<div class="flex_box gap-12">
									<div class="input-form">
										<input id="rdoConfmSttusCode_100" name="confmSttusCode" type="radio" value="100" checked="checked">
										<label for="rdoConfmSttusCode_100">승인</label>
									</div>
									<div class="input-form">
										<input id="rdoConfmSttusCode_300" name="confmSttusCode" type="radio" value="300">
										<label for="rdoConfmSttusCode_300">거절</label>
									</div>
								</div>
							</td>
						</tr>
						<tr id="trRejectResn" style="display:none;">
							<td class="label"><span class="required_mark">*</span>거절사유</td>
							<td colspan="3">
								<div>
									<span class="placeholder_wrap">
										<span class="placeholder">거절사유를 입력해 주세요.</span>
										<textarea id="rejectResn" name="rejectResn" cols="30" rows="10" maxlength="2000" class="required _filter" title="사유입력"></textarea>
									</span>
								</div>
							</td>
						</tr>
						<!--// 상태 : 대기 -->
					</c:when>
					<c:when test="${details.confmSttusCode eq 100}">
						<!-- 상태 : 승인 -->
						<tr>
							<td class="label">상태</td>
							<td colspan="3">
								<div><c:out value="${details.confmSttusNm}"/></div>
							</td>
						</tr>
						<!--// 상태 : 승인 -->
					</c:when>
					<c:when test="${details.confmSttusCode eq 300}">
						<!-- 상태 : 거절 -->
						<tr>
							<td class="label">상태</td>
							<td colspan="3">
								<div><c:out value="${details.confmSttusNm}"/></div>
							</td>
						</tr>
						<tr>
							<td class="label">거절사유</td>
							<td colspan="3">
								<div><c:out value="${details.rejectResn}"/></div>
							</td>
						</tr>
						<!--// 상태 : 거절 -->
					</c:when>
				</c:choose>
				<!--// 상태 필드 -->
			</tbody>
		</table>
		<p class="popup_btns">
			<c:choose>
				<c:when test="${details.confmSttusCode eq 200}">
					<button type="button" class="btn btn-active" onclick="managerConfirm()">확인</button>
					<button type="button" class="btn btn-default" onclick="self.close();">취소</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-active" onclick="self.close();">확인</button>
				</c:otherwise>
			</c:choose>
		</p>
	</div>
</div>

<script type="text/javascript">
	$(function(){

		// 페이지 유효성 검사
		pageValidator();

		// 승인 상태 변경 이벤트 셋팅
		confmSttusChange();
	});

	/**
	 *	함 수 명 : pageValidator
	 *	기	능  : 페이지 유효성 검사
	 */
	function pageValidator() {
		if("${fn:escapeXml(details.userId)}" == "" || "${fn:escapeXml(details.userId)}" == "null") {
			alert("상세정보가 존재하지 않습니다.");
			self.close();
		}
	}

	/**
	 *	함 수 명 : confmSttusChange
	 *	기	능  : 승인 상태 변경 이벤트
	 */
	function confmSttusChange() {
		$("input[name='confmSttusCode']").change(function() {
			if($("input[name='confmSttusCode']:checked").val() == '300') {
				$("#trRejectResn").show();
			} else {
				$("#trRejectResn").hide();
				$("#rejectResn").val("");
			}
		});
	}

	/**
	 *	함 수 명 : managerConfirm
	 *	기	능  : 관리자 확인 (승인 / 거절)
	 */
	function managerConfirm() {
		var selectedObj = $("input[name='confmSttusCode']:checked");

		if(selectedObj && selectedObj.length > 0) {
			var confirmMsg = "관리자 권한을 승인 하시겠습니까?";
			var param = {
				"confmSttusCode": $(selectedObj).val(),
				"userId": "${fn:escapeXml(details.userId)}",
				"reqstSn": "${fn:escapeXml(details.reqstSn)}"
			};

			if($(selectedObj).val() == '300') {
				confirmMsg = "권한 요청을 거절 하시겠습니까?";

				if($.trim($("#rejectResn").val()) == '') {
					alert("거절사유를 입력해 주세요.");
					$("#rejectResn").focus();
					return false;

				} else {
					param.rejectResn = $("#rejectResn").val();
				}
			}

			if(confirm(confirmMsg)) {
				$.post("/opmanager/manager-request/confirm", param, function(response) {
					if(response.isSuccess && response.data) {
						managerConfirmResult(response.data);
					}
				});
			}

		} else {
			alert("관리자 권한 요청에 대한 상태값을 선택해 주세요.");
		}
	}

	/**
	 *	함 수 명 : managerConfirmResult
	 *	기	능  : 관리자 확인 (승인 / 거절) 결과
	 *	파라미터  : data.code - 결과코드
	 */
	function managerConfirmResult(data) {
		if(data.code == "SUCC") {
			var confmSttusCode = $("input[name='confmSttusCode']:checked").val();
			alert((confmSttusCode == "100") ? "승인되었습니다." : "거절되었습니다.");
			opener.parent.location.reload();
			self.close();

		} else if(data.code == "ERR_STS" && (data.statusCode == "3" || data.statusCode == "4")) {
			if(data.statusCode == "3") alert("탈퇴한 회원은 관리자 권한 승인 불가능합니다. \n관리자 권한 거절만 가능합니다.");
			else if(data.statusCode == "4") alert("휴면 회원은 관리자 권한 승인 불가능합니다. \n관리자 권한 거절만 가능합니다.");

		} else {
			alert("오류가 발생했습니다.");
		}
	}
</script>

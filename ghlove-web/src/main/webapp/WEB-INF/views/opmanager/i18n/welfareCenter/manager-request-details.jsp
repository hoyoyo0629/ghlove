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
									<fmt:parseDate var="birthdayDateFmt" pattern="yyyyMMdd" value="${birthdayCustom}" />
									<fmt:formatDate var="birthdayStringFmt" pattern="yyyy-MM-dd" value="${birthdayDateFmt}" />
									<c:out value="${birthdayStringFmt}"/>
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
				<tr>
					<td class="label">지자체</td>
					<td colspan="3">
						<div><c:out value="${details.upperLocgovNm}"/> <c:out value="${details.locgovNm}"/></div>
					</td>
				</tr>
				<tr>
					<td class="label">소속 구분</td>
					<td colspan="3">
						<div><c:out value="${details.reqstSeNm}"/></div>
					</td>
				</tr>
				<c:if test="${details.confmSttusNm ne '승인' || details.active ne 'Y'}">
					<tr>
						<td class="label">행정복지센터 명</td>
						<td colspan="3">
							<div>
								<c:out value="${details.pbadmsWlfrCntrNm}"/>
							</div>
						</td>
					</tr>
				</c:if>
				<c:if test="${details.confmSttusNm eq '승인' && details.active eq 'Y'}">
					<tr>
						<td class="label">행정복지센터 명</td>
						<td colspan="3">
							<div>
								<select id="wlfrCntrMng" name="wlfrCntrMng" title="구분" class="wd-150">
									<option value="">전체</option>
										<c:forEach items="${list}" var="list">
											<c:choose>
												<c:when test="${list.pbadmsWlfrCntrId == details.psitnCode}">
													<option value="${fn:escapeXml(list.pbadmsWlfrCntrId)}" label="${fn:escapeXml(list.pbadmsWlfrCntrNm)}" selected="selected"><c:out value="${list.pbadmsWlfrCntrNm}"/></option>
												</c:when>
												<c:otherwise>
													<option value="${fn:escapeXml(list.pbadmsWlfrCntrId)}" label="${fn:escapeXml(list.pbadmsWlfrCntrNm)}"><c:out value="${list.pbadmsWlfrCntrNm}"/></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
					            <select>
							</div>
						</td>
					</tr>
				</c:if>
				<tr>
					<td class="label">담당 소속</td>
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
					<button type="button" class="btn btn-default" onclick="self.close();">닫기</button>
				</c:when>
				<c:otherwise>
					<c:if test="${details.confmSttusCode eq 100 && details.active eq 'Y'}">
						<button type="button" class="btn btn-active" onclick="updateWlfrCntMng('${fn:escapeXml(details.reqstSn)}')">수정</button>
					</c:if>
					<button type="button" class="btn btn-active" onclick="self.close();">닫기</button>
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
				$("#trDsgncntrPart").hide();
			} else {
				$("#trRejectResn").hide();
				$("#trDsgncntrPart").show();
				$("#rejectResn").val("");
			}
		});
	}

	/**
	 *	함 수 명 : managerConfirm
	 *	기	능  : 관리자 확인 (승인 / 거절)
	 */
	function managerConfirm() {
		let selectedObj = $("input[name='confmSttusCode']:checked");

		if(selectedObj && selectedObj.length > 0) {
			let confirmMsg = "관리자 권한을 승인 하시겠습니까?";
			let param = {
				"confmSttusCode": $(selectedObj).val(),
				"userId": '<c:out value="${details.userId}"/>',
				"reqstSn" : '<c:out value="${details.reqstSn}"/>',
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
				$.post("/opmanager/welfareCenter/confirm", param, function(response) {
					if(response.isSuccess && response.data) {
						managerConfirmResult(response.data);
					} else {
						alert('문제가 발생했습니다.');
						console.log(response);
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
			let confmSttusCode = $("input[name='confmSttusCode']:checked").val();
			let msg = "";
			if (confmSttusCode == "100") {
				if (data.statusCode == "901") {
					msg = "승인되었습니다. 권한이 없어 사업 부서 정보등록에는 실패했습니다.";
				} else if (data.statusCode == "902") {
					msg = "승인되었습니다. 사업 부서 정보등록에는 실패했습니다.";
				} else {
					msg = "승인되었습니다.";
				}
			} else {
				msg = "거절되었습니다.";
			}
			alert(msg);
			opener.parent.location.reload();
			self.close();

		} else if(data.code == "ERR_STS") {
			if(data.statusCode == "3") alert("탈퇴한 회원은 관리자 권한 승인 불가능합니다. \n관리자 권한 거절만 가능합니다.");
			else if(data.statusCode == "4") alert("휴면 회원은 관리자 권한 승인 불가능합니다. \n관리자 권한 거절만 가능합니다.");
			else alert("오류가 발생했습니다.");
		} else {
			alert("오류가 발생했습니다.");
		}
	}

	// 승인 회원 부서 정보 변경
	function changePart() {
		let dsgncntrPartId = $("#dsgncntrPartId option:selected").val();

		if (dsgncntrPartId > 0) {
			let param = {"dsgncntrPartId" : dsgncntrPartId
						, "partUserId" : '<c:out value="${details.userId}"/>'
						, "reqstSn" : '<c:out value="${details.reqstSn}"/>'
			};

			$.post(url("/opmanager/designated-donation/request/partUpdate"), param, function(response) {
				if (response.isSuccess) {
					switch (response.data.code) {
						case 'SUCC' :
							alert('변경되었습니다.');
							opener.parent.location.reload();
							break;
						case 'NO_AUTH' :
							alert('권한이 없습니다.');
							break;
						case 'FAIL' :
							alert('실패했습니다.');
							break;
						default:
							alert('문제가 발생했습니다.');
							break;
					}
				} else {
					alert('문제가 발생했습니다.');
				}
			});
		} else {
			alert('특정사업 기부 사업 부서를 선택해주세요.');
		}
	}

	function updateWlfrCntMng(reqstSn) {
		var psitnCode = $("#wlfrCntrMng").val();
		var psitnNm = $("#wlfrCntrMng option:selected").text();
		$.post('/opmanager/welfareCenter/update/'+reqstSn+'/'+psitnCode+'/'+psitnNm, function(response) {
			if (response > 0) {
				alert('변경되었습니다.');
				opener.parent.location.reload();
				self.close();
			} else {
				alert('실패했습니다.');
			}
		});
	}

</script>

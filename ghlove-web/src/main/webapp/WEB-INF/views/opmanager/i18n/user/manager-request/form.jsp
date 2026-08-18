<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ page import="saleson.common.configuration.SalesonProperty" %>

<div class="admin_wrap">
	<!-- Header -->
	<div id="header">
		<div class="header_wrap">
			<h1><a href="javascript:;"><img src="/content/opmanager/images/img_logo.png" alt="고향사랑기부제" /></a></h1>
			<div class="tnb"></div>
		</div>
	</div>
	<!-- Header -->

	<!-- Container -->
	<div id="container" class="login">
		<div class="login_cont">
			<div class="login_tit mt-70">관리자 권한 요청</div>
			<div class="login_sub_tit">최초 1회 로그인시 관리자 권한 요청페이지를 작성하셔야 합니다.<br>승인 후 정상적으로 이용이 가능합니다.</div>

			<div class="form-wrap">
				<div class="board_write">
					<table class="board_write_table">
						<colgroup>
							<col style="width: 220px;" />
						</colgroup>
						<tr>
							<td class="label">아이디</td>
							<td colspan="3">
								<div>
									<p class="txt"><c:out value="${loginId}"/></p>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">이름</td>
							<td colspan="3">
								<div>
									<p class="txt"><c:out value="${userName}"/></p>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">생년월일</td>
							<td colspan="3">
								<div>
									<p class="txt">
										<c:if test="${not empty birthday}">
											<c:set var="birthdayCustom" value="${fn:replace(birthday, '-', '')}" />
											<c:if test="${fn:length(birthdayCustom) eq 8}">
												<%-- <fmt:parseDate var="birthdayDateFmt" pattern="yyyyMMdd" value="${birthdayCustom}" />
												<fmt:formatDate var="birthdayStringFmt" pattern="yyyy-MM-dd" value="${fn:escapeXml(birthdayDateFmt)}" />
												<c:out value="${birthdayStringFmt}"/> --%>
												${op:formatDate(birthdayCustom, "-")}
											</c:if>
										</c:if>
									</p>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">휴대폰</td>
							<td colspan="3">
								<div>
									<p class="txt"><c:out value="${phoneNumber}"/></p>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">이메일 주소</td>
							<td colspan="3">
								<div>
									<p class="txt"><c:out value="${email}"/></p>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">소속 구분</td>
							<td colspan="3">
								<div class="flex_box gap-12">
									<c:forEach items="${reqstSeCodeList}" var="code" varStatus="status">
										<c:if test="${code.id ne 'ROLE_ADMIN_8'}">
											<div class="input-form">
												<input id="rdoReqstSeCode-${fn:escapeXml(code.id)}" name="reqstSeCode" type="radio" value="${fn:escapeXml(code.id)}" onclick="changeReqstSeCode('${fn:escapeXml(code.id)}')"
												<c:if test="${status.index eq 0}">checked</c:if>><label for="rdoReqstSeCode-${fn:escapeXml(code.id)}"><c:out value="${fn:escapeXml(code.label)}"/></label>
											</div>
										</c:if>
									</c:forEach>
								</div>
							</td>
						</tr>
						<tr id="trLov">
							<td class="label"><span class="required_mark">*</span>소속 지자체</td>
							<td colspan="3">
								<div>
									<div class="flex_box gap-08">
										<select id="upperLocgov" onchange="changeUpperLocgov()">
											<option value="">- 시도 선택 -</option>
											<c:forEach items="${upperLocgovCodeList}" var="code" varStatus="status">
												<option value="${fn:escapeXml(code.id)}"><c:out value="${fn:escapeXml(code.label)}"/></option>
											</c:forEach>
										</select>
										<select id="locgov" onchange="javascript:changeLocgov(this.value);">
											<option value="">- 시군구 -</option>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<tr id="trOff" style="display:none;">
							<td class="label"><span class="required_mark">*</span>소속 은행</td>
							<td colspan="3">
								<div>
									<div class="flex_box gap-08">
										<select id="psitnCode" class="three">
											<c:forEach items="${bankCodeList}" var="code" varStatus="status">
												<option value="${fn:escapeXml(code.id)}"><c:out value="${fn:escapeXml(code.label)}"/></option>
											</c:forEach>
										</select>
										<input id="psitnNm" class="optional seven" title="지점명" type="text" placeholder="지점명">
									</div>
								</div>
							</td>
						</tr>
						<tr id="trOff2" style="display:none;">
							<td class="label"><span class="required_mark">*</span>지점 연락처</td>
							<td colspan="3">
								<div class="flex_box gap-08 item-center">
									<select id="phoneNumber1" title="지점 연락처" class="wd-100">
										<option value="">-선택-</option>
										<c:forEach items="${phoneCodeList}" var="code" varStatus="status">
											<option value="${fn:escapeXml(code.id)}"><c:out value="${code.label}"/></option>
										</c:forEach>
										<c:forEach items="${telCodeList}" var="code" varStatus="status">
											<option value="${fn:escapeXml(code.id)}"><c:out value="${code.label}"/></option>
										</c:forEach>
									</select>
									<span class="wave">-</span>
									<input id="phoneNumber2" title="" class="input_txt required _filter wd-100 _number" type="text" maxLength="4" value="${fn:split(details.phoneNumber, '-')[1]}">
									<span class="wave">-</span>
									<input id="phoneNumber3" title="" class="input_txt required _filter wd-100 _number" type="text" maxLength="4" value="${fn:split(details.phoneNumber, '-')[2]}">
								</div>
							</td>
						</tr>
						<tr id="trWcm" style="display:none;">
							<td class="label"><span class="required_mark">*</span>행정복지센터 명</td>
							<td colspan="3">
								<div>
									<div class="flex_box gap-08">
										<select id="wlfrCntrMng">
											<option value="">- 행정복지센터 선택 -</option>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<!-- // 오프라인 case -->
						<tr id="managerPart">
							<td class="label">
								<p class="test">소속 부서</p>
							</td>
							<td>
								<div>
									<input id="psitnDeptNm" class="optional seven" title="소속 부서" type="text" placeholder="부서명" maxlength="200">
								</div>
							</td>
							<td class="label">직위</td>
							<td>
								<div>
									<input id="ofcpsNm" class="optional seven" title="직위" type="text" placeholder="직위" maxlength="100">
								</div>
							</td>
						</tr>
						<tr id="dsgncntrPart" style="display:none;">
							<td class="label">지정기부 부서</td>
							<td>
								<div class="gap-08">
									<select class="optional seven" id="dsgncntrPartId">
										<option value="0">- 부서 미선택 -</option>
									</select>
								</div>
							</td>
						</tr>
					</table>
					<p class="table-info">
						* 회원정보의 이메일 수신 동의 항목에 미동의시 요청완료 메일을 전달받지 못할 수 있습니다.<br/>
						&nbsp;&nbsp;고향사랑 기부금 메인 > 마이페이지 >
						<a href="${SalesonProperty.getSalesonUrlFrontend()}/users/modify.html" target="_blank">회원정보수정</a> 항목에서 변경 가능합니다.
					</p>
				</div>
			</div>

			<div class="button_wrap">
				<button type="button" class="btn btn-dark-gray btn-large" onclick="managerRequest();"><span>관리자 권한 요청하기</span></button>
				<button type="button" class="btn btn-dark-gray btn-mid h-large" onclick="moveLoginPage();"><span>취소</span></button>
			</div>
		</div>
	</div>
	<!-- Container -->

	<!-- footer  -->
	<div id="footer">
		<div class="footer_wrap">
			<span class="copy">© Ministry of the interior and safety. All rights reserved.</span>
		</div>
	</div>
	<!--// footer -->
</div>

<script type="text/javascript">
	$(function() {
		// 회원 유효성 체크
		userValidator();
	});

	/**
	 *	함 수 명 : userValidator
	 *	기	능  : 회원 유효성 체크
	 */
	function userValidator() {
		if("${fn:escapeXml(userId)}" == "" || "${fn:escapeXml(userId)}" == "null") {
			alert("회원 정보가 존재하지 않습니다.");
			moveLoginPage();
		};
	}

	/**
	 *	함 수 명 : moveLoginPage
	 *	기	능  : 로그인 페이지 이동
	 */
	function moveLoginPage() {
		location.href = "/opmanager/login";
	}

	/**
	 *	함 수 명 : changeReqstSeCode
	 *	기	능  : 소속구분 변경 이벤트
	 *	파라미터  : code - 변경된 소속구분 코드
	 */
	function changeReqstSeCode(code) {
		// 소속구분 상세폼 초기화
		clearReqstSeDetailForm();

		// 지자체 - 소속지자체 활성화, 오프라인 - 소속은행 활성화
		if(code == "ROLE_ADMIN_6" || code == "ROLE_ADMIN_10") {
			$("#trLov").show();
			if (code == "ROLE_ADMIN_10") {
				$("#dsgncntrPart").show();
				$("#managerPart").hide();
			}
		} else if (code == "ROLE_ADMIN_8") {
			$("#trOff").show();
			$("#trOff2").show();
			$("#trLov").show();
		} else if (code == "ROLE_ADMIN_11") {
			$("#trWcm").show();
			$("#trLov").show();
		}
	}

	/**
	 *	함 수 명 : clearReqstSeDetailForm
	 *	기	능  : 소속구분 상세폼 초기화
	 */
	function clearReqstSeDetailForm() {

		// 영역 초기화
		$("#trOff").hide();
		$("#trOff2").hide();
		$("#trLov").hide();
		$("#trWcm").hide();
		$("#dsgncntrPart").hide();
		$("#managerPart").show();
		$("#dsgncntrPartId").html('<option value="0">- 부서 미선택 -</option>');

		// 데이터 초기화 (소속 은행은 '농협'이 기본값)
		$("#psitnCode").val("011");
		$("#psitnNm").val("");
		$("#psitnDeptNm").val("");
		$("#ofcpsNm").val("");
		$("#upperLocgov").val("");
		$("#dsgncntrPartId").val("0");

		changeUpperLocgov();
	}

	/**
	 *	함 수 명 : changeUpperLocgov
	 *	기	능  : 지자체(시도) 변경 이벤트
	 */
	function changeUpperLocgov() {
		var upperLocgovCode = $("#upperLocgov").val();
		$("#locgov").html('<option value="">- 시군구 -</option>');

		if(upperLocgovCode) {
			$.post('/opmanager/manager-request/code-child/list', {"codeType": "LOCGOV_CODE"}, function(response) {
				if(response.isSuccess && response.data) {
					$.each(response.data, function(index, item) {
						if(item.id && upperLocgovCode && (item.id.substring(0, 2) == upperLocgovCode.substring(0, 2))) {
							$("#locgov").append('<option value="'+item.id+'">'+item.label+'</option>');
						}
					});
				}
			});
		}
	}

	/**
	 *	함 수 명 : managerRequest
	 *	기	능  : 관리자 권한 요청하기
	 */
	function managerRequest() {
		if(!validator() || !confirm("등록된 정보로 권한을 요청 하시겠습니까?")) return false;

		$.post('/opmanager/manager-request/create', setParam(), function(response) {
			if(response.isSuccess && response.data && response.data == "SUCC") {
				alert("권한 요청이 완료되었습니다.");
				moveLoginPage();
			} else {
				alert("오류가 발생했습니다.");
			}
		});
	}

	/**
	 *	함 수 명 : validator
	 *	기	능  : 등록 값 유효성 체크
	 */
	function validator() {
		var reqstSeCodeObj = $("input[name='reqstSeCode']:checked");

		if($(reqstSeCodeObj).length == 0) {
			alert('소속 구분을 선택해주세요.');
			return false;
		}

		if($(reqstSeCodeObj).val() == 'ROLE_ADMIN_6' || $(reqstSeCodeObj).val() == 'ROLE_ADMIN_10') {
			if($('#upperLocgov').val() == '') {
				alert('소속 지자체(시도)를 선택해주세요.');
				$('#upperLocgov').focus();
				return false;

			} else if($('#locgov').val() == '') {
				alert('소속 지자체(시군구)를 선택해주세요.');
				$('#locgov').focus();
				return false;
			}
		}

		if($(reqstSeCodeObj).val() == 'ROLE_ADMIN_8') {
			if($('#upperLocgov').val() == '') {
				alert('소속 지자체(시도)를 선택해주세요.');
				$('#upperLocgov').focus();
				return false;

			} else if($('#locgov').val() == '') {
				alert('소속 지자체(시군구)를 선택해주세요.');
				$('#locgov').focus();
				return false;
			} else if($('#psitnCode').val() == '') {
				alert('소속 은행을 선택해주세요.');
				$('#psitnCode').focus();
				return false;

			} else if($.trim($('#psitnNm').val()) == '') {
				alert('소속 은행 지점명을 입력해주세요.');
				$('#psitnNm').focus();
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
		}

		if($(reqstSeCodeObj).val() == 'ROLE_ADMIN_11') {
			console.log("111:"+$('#wlfrCntrMng').val());
			if($('#wlfrCntrMng').val() == '' || $('#wlfrCntrMng').val() == 0) {
				console.log("2222");
				alert('행정복지센터를 선택해주세요.');
				$('#wlfrCntrMng').focus();
				return false;
			}
		}
		return true;
	}

	/**
	 *	함 수 명 : setParam
	 *	기	능  : 파라미터 셋팅
	 */
	function setParam() {
		 var reqstSeCode = $("input[name='reqstSeCode']:checked").val();

		var param = {
			'userId': '${fn:escapeXml(userId)}',
			'loginId' : '${fn:escapeXml(loginId)}',
			'reqstSeCode': reqstSeCode,
			'psitnDeptNm': $('#psitnDeptNm').val(),
			'ofcpsNm': $('#ofcpsNm').val(),
			'cttpc': "${fn:escapeXml(phoneNumber)}",
			'dsgncntrPartId' : $('#dsgncntrPartId').val(),
		};

		// 소속구분 : 지자체
		if(reqstSeCode == 'ROLE_ADMIN_6' || reqstSeCode == 'ROLE_ADMIN_8' || reqstSeCode == 'ROLE_ADMIN_10' || reqstSeCode == 'ROLE_ADMIN_11') {
			param.locgovCode = $("#locgov").val();
		}

		// 소속구분 : 오프라인
		if(reqstSeCode == 'ROLE_ADMIN_8') {
			param.psitnCode = $("#psitnCode").val();
			param.psitnNm = $("#psitnNm").val();
			param.phoneNumber = $("#phoneNumber1").val()+"-"+ $("#phoneNumber2").val()+"-"+$("#phoneNumber3").val(); //cttpc필드를 param에서 사용해서 phoneNumber를 지점연락처로 셋팅함

		}

		// 소속구분 : 행정복지센터
		if(reqstSeCode == 'ROLE_ADMIN_11') {
			param.psitnCode = $("#wlfrCntrMng").val();
			param.psitnNm = $("#wlfrCntrMng option:selected").text();
		}

		return param;
	}

	function changeLocgov(locgovCode) {
		$("#dsgncntrPartId").html('<option value="0">- 부서 미선택 -</option>');
		let reqstSeCode = $("input[name='reqstSeCode']:checked").val();
		if (reqstSeCode == 'ROLE_ADMIN_10') {
			$.post('/opmanager/designated-donation/partList', {"locgovCode" : locgovCode, "conditionType" : "REQUEST"}, function(response) {
				if(response.isSuccess && response.data) {
					$.each(response.data, function(index, item) {
						$("#dsgncntrPartId").append('<option value="'+item.dsgncntrPartId+'">'+item.dsgncntrPartName+'</option>');
					});
				}
			});
		}
		changeWlfrCntMng(locgovCode);
	}

	function changeWlfrCntMng(locgovCode) {
		$("#wlfrCntrMng").html('<option value="0">- 행정복지센터 선택 -</option>');
		$.post('/opmanager/welfareCenter/wlfrCntrMngList', {"lclgvCd" : locgovCode, "conditionType" : "REQUEST"}, function(response) {
			if(response.isSuccess && response.data) {
				$.each(response.data, function(index, item) {
					$("#wlfrCntrMng").append('<option value="'+item.pbadmsWlfrCntrId+'">'+item.pbadmsWlfrCntrNm+'</option>');
				});
			}
		});
	}
</script>
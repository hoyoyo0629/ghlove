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

<h3><span>기부금 변경 등록</span></h3>

<div class="board_write">
	<table class="board_write_table" summary="">
		<colgroup>
			<col style="width:150px;">
			<col style="width:150px;">
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label" colspan="2">요청분류</td>
				<td colspan="2">
					<div>
						<select id="cntrReqmngCode" title="-선택-" class="wd-160" onChange="cntrReqmngCodeChange(this.value);">
							<c:forEach items="${reqmngCodeList}" var="code" varStatus="status" begin="0" end="1">
								<c:set var="reqLabel" value="${fn:escapeXml(code.label)}"/>
								<c:if test="${fn:escapeXml(code.id) eq 100 }"><c:set var="reqLabel" value="기부금 취소(과오납)"/></c:if>
								<option value="${fn:escapeXml(code.id)}"><c:out value="${reqLabel}"/></option>
							</c:forEach>
						</select>
					</div>
				</td>
			</tr>
			<c:if test="${!empty reqinfo.linkInsttCd}">
			<tr>
				<td class="label" colspan="2">민간기관명</td>
				<td colspan="2">
					<div><c:out value="${reqinfo.detail}"/></div>
				</td>
			</tr>
			</c:if>
			<tr>
				<td class="label" colspan="2">지자체명</td>
				<td colspan="2">
					<div><c:out value="${reqinfo.cntrLocgovCodeName}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label" colspan="2">기부자명</td>
				<td colspan="2">
					<div><c:out value="${reqinfo.userName}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label" colspan="2">기부자ID</td>
				<td colspan="2">
					<div><c:out value="${reqinfo.loginId}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label" colspan="2">기부일자</td>
				<td colspan="2">
					<div><c:out value="${op:date(reqinfo.sttemntPayDe)}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label"  colspan="2">전자납부번호</td>
				<td colspan="2">
					<div><c:out value="${reqinfo.elctrnPayNo}"/></div>
				</td>
			</tr>
			<tr>
			<td class="label" colspan="2">기부액 (잔여포인트)</td>
				<td colspan="2">
				    <div><c:out value='${op:numberFormat(reqinfo.cntrAmt)} (${op:numberFormat(reqinfo.cntrPoint - reqinfo.cntrUsePoint)}P)'/></div>
				</td>
			</tr>
			<tr>
			<td class="label" colspan="2">전자기부금영수증</td>
				<td colspan="2">
				   <c:choose>
	                 	<c:when test="${reqinfo.ntsSttusMssage eq 'SUCCESS'}">
	                 		<div><font color="red"><b>고향사랑e음 처리완료</b></font></div>
	                 	</c:when>
	                 	<c:otherwise>
	                 		<div><font>미처리</font></div>
	                 	</c:otherwise>
	                </c:choose>
				</td>
			</tr>
			<tr class="evidence">
				<td class="label" colspan="2">세외수입시스템 과오납 결의일자</td>
				<td colspan="2">
				    <div>
				    	<span class="datepicker">
	                     	<input id="taxSysCancelDe" name="taxSysCancelDe" title="${op:message('세외수입시스템 과오납 결의일자 날짜 선택')}" class="datepicker evidenceData" type="text" value="" maxlength="8" autocomplete="off" />
	                    </span>
	                </div>
				</td>
			</tr>
			<tr class="evidence">
				<td class="label" rowspan="3">관련문서</td>
				<td class="label" style="border: 1px solid #ececec;">생산부서명</td>
				<td colspan="2">
				   <div><input type="text" id="relatedDocDptNm" name="relatedDocDptNm" title="관련문서 생산부서명" class="form-block evidenceData"/></div>
				</td>
			</tr>
			<tr class="evidence">
				<td class="label" style="border: 1px solid #ececec;">문서번호</td>
				<td colspan="2">
				   <div><input type="text" id="relatedDocNum" name="relatedDocNum" oninput="this.value = this.value.replace(/[^0-9.]/g, '');" title="관련문서 문서번호" class="form-block number evidenceData"/></div>
				</td>
			</tr>
			<tr class="evidence">
				<td class="label" style="border: 1px solid #ececec;">시행일</td>
				<td colspan="2">
				   <div>
				   		<span class="datepicker">
	                     	<input id="relatedDocDe" name="relatedDocDe" title="${op:message('관련문서 시행일 날짜 선택')}" class="datepicker evidenceData" type="text" value="" maxlength="8" autocomplete="off" />
	                    </span>
	               </div>
				</td>
			</tr>

			<tr>
				<td class="label" colspan="2">사유 (최소 10자 이상 작성)</td>
				<td colspan="2">
					<div><input type="text" id="discription" name="discription" title="사유" class="form-block"/></div>
				</td>
			</tr>

		</tbody>
	</table>
</div>
<div>
 <br>
 <h4>※ <span style="text-decoration:underline; font-size:17px;font-weight:bold;margin-left:0px;">포인트 생성</span>의 경우 포인트 체계를 사용하는 민간플랫폼에 한해 적용이 가능합니다.</h4>
 <h4>※ 답례품이 신청된 건은 시스템으로 변경이 불가능하여 개발원으로 공문 처리가 필요합니다.</h4>
 <h4>※ 세액공제를 받았을 경우 반드시 홈택스에서 경정청구를 하도록 기부자에게 안내 바랍니다.</h4>
 <h4>※ 기부 취소는 아래의 내용을 반드시 확인 후 요청바랍니다.
 </h4>
 <ul class="ml22">
		<li><span class="txt">- <span style="font-weight:bold">취소절차</span>: 세외수입시스템 과오납 처리(기관담당자) → 고향사랑e음 기부취소(개발원) → 홈택스 경정청구(세액공제 받은 기부자)</span></li>
		<li><span class="txt">- <span style="font-weight:bold">유의사항</span></span></li>
		<li><span class="txt pl20">• 세외수입시스템 과오납 처리 후 기부 취소 신청을 요청하시기 바랍니다.</span></li>
		<li><span class="txt pl20">• 위 입력정보(결의일, 관련문서)는 <span style="font-weight:bold">기부 취소 근거(증빙)자료</span>이므로 잘못된 정보 입력으로 인해 기부 취소에 문제가 발생하였을 경우 요청(해당)기관에 책임이 있습니다.</span></li>
	</ul>
 <!-- <h4><font color="red">※ 변경신청 등록 전 국세청 홈텍스에서 본 기부건과 관련한 세액공제 내역을 삭제 해야 합니다.</font></h4> -->

</div>
	<div class="btn_all btn_center">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-small" value='${fn:escapeXml(reqinfo.cntrSn)}' onclick="reqmngReg('${fn:escapeXml(reqinfo.cntrSn)}','${fn:escapeXml(reqinfo.ntsSttusMssage)}');">변경</button>
			<button type="button" class="btn btn-dark-default btn-small" onclick="goList();">취소</button>
		</div>
	</div>

<!-- 목록 검색 조건 -->
<input type="hidden" id="listSearchParam" />
<!--// 목록 검색 조건  -->

<script type="text/javascript">

$(function() {
	cntrReqmngCodeChange('100');
	$(".contents_inner").find("h3 span:first").text("기부금 취소•포인트 생성");
	$(".contents .contents_inner").find("div.location a").removeClass("on");
	$(".contents .contents_inner").find("div.location").append('> <a href="/opmanager/give/give-state/req_form/${fn:escapeXml(reqinfo.elctrnPayNo)}" class="on">변경</a>');
});


	function cntrReqmngCodeChange(reqmngCd){
		if(reqmngCd == '100'){
			$('.evidence').show();
		}else{
			$('.evidence').hide();
			$('.evidenceData').val('');
		}
	}

	/**
	 *	함 수 명 : reqmngSave
	 *	기	능  : 변경신청 내용 등록
	 */
	function reqmngReg(cntrSn, ntsSttusMssage){
		var cntrReqmngCode = $("#cntrReqmngCode option:selected").val();
		if(cntrReqmngCode == 100){											// 과오납 처리
			if($.trim($('#taxSysCancelDe').val()) == ''){
				 $('#taxSysCancelDe').focus();
				 alert("세외수입시스템 과오납 결의일자를 입력해주세요.");
				 return false;
			 }

			if($.trim($('#relatedDocDptNm').val()) == ''){
				 $('#relatedDocDptNm').focus();
				 alert("관련문서 생산부서명을 입력해주세요.");
				 return false;
			 }

			if($.trim($('#relatedDocNum').val()) == ''){
				 $('#relatedDocNum').focus();
				 alert("관련문서 문서번호를 입력해주세요.");
				 return false;
			 }

			if($.trim($('#relatedDocDe').val()) == ''){
				 $('#relatedDocDe').focus();
				 alert("관련문서 시행일을 입력해주세요.");
				 return false;
			 }

		 }

		 if($.trim($('#discription').val()) == ''){
			 $('#discription').focus();
			 alert("사유를 입력해주세요.");
			 return false;
		 }

		 if($.trim($('#discription').val()).length < 10){
			 $('#discription').focus();
			 alert("사유는 10자 이상 작성해주세요.");
			 return false;
		 }

		 if(confirm("작성하신 내용으로 확정하시겠습니까?\n이후 복구는 불가능합니다.")){
			 var linkInsttCd = "${fn:escapeXml(reqinfo.linkInsttCd)}";
			 var discription = "(사용포인트 : "+ "${fn:escapeXml(reqinfo.cntrUsePoint)}" + " p)"+ $("input[name=discription]").val();
			 var noBankInsttCds = ["I0000036", "I0000047","I0000048","I0000092","I0000118","I0000144"];	// 20260608 추가

			 if(cntrReqmngCode == 200 && noBankInsttCds.includes(linkInsttCd)){
				alert("민간연계 기부건은 포인트 생성이 불가합니다.");
				return false;
			 }


			 if(cntrReqmngCode == 100 && ntsSttusMssage == 'SUCCESS'){
				 if(confirm("해당 기부건은 국세청 기부금영수증 처리가 되었습니다.\n홈텍스에서 기부금영수증 취소처리 후 변경 하시길 바랍니다.\n변경 하시겠습니까?")){
						var param = {
								"cntrSn" : "${fn:escapeXml(reqinfo.cntrSn)}",
								"cntrReqmngCode": cntrReqmngCode,
								"loginId": "${fn:escapeXml(reqinfo.loginId)}",
								"userId": "${fn:escapeXml(reqinfo.userId)}",
								"userName": "${fn:escapeXml(reqinfo.userName)}",
								"locgovCode": "${fn:escapeXml(reqinfo.cntrLocgovCode)}",
								"sttemntPayDe": "${fn:escapeXml(reqinfo.sttemntPayDe)}",
								"cntrAmt": "${fn:escapeXml(reqinfo.cntrAmt)}",
								"elctrnPayNo": "${fn:escapeXml(reqinfo.elctrnPayNo)}",
								"discription": discription,
								"taxSysCancelDe": $("#taxSysCancelDe").val(),
								"relatedDocDptNm": $("#relatedDocDptNm").val(),
								"relatedDocNum": $("#relatedDocNum").val(),
								"relatedDocDe": $("#relatedDocDe").val()
							};

							$.post("/opmanager/give/give-state/reqReg", param, function(response) {
								if(response.isSuccess == true) {
									alert("변경되었습니다.");
									location.href = "/opmanager/give/give-state/reqmng-list";
								} else {
									var errMessage = response.errorMessage;
									if(errMessage == '' || errMessage == undefined){
										alert("오류가 발생했습니다.");
									}else{
										alert(errMessage);
									}
								}
							});
			 	}else{
					alert("변경이 취소되었습니다.");
			 	}

			}else{//포인트 생성 혹은 영수증 미처리건
				var param = {
					"cntrSn" : "${fn:escapeXml(reqinfo.cntrSn)}",
					"cntrReqmngCode": cntrReqmngCode,
					"loginId": "${fn:escapeXml(reqinfo.loginId)}",
					"userId": "${fn:escapeXml(reqinfo.userId)}",
					"userName": "${fn:escapeXml(reqinfo.userName)}",
					"locgovCode": "${fn:escapeXml(reqinfo.cntrLocgovCode)}",
					"sttemntPayDe": "${fn:escapeXml(reqinfo.sttemntPayDe)}",
					"cntrAmt": "${fn:escapeXml(reqinfo.cntrAmt)}",
					"elctrnPayNo": "${fn:escapeXml(reqinfo.elctrnPayNo)}",
					"discription": discription,
					"taxSysCancelDe": $("#taxSysCancelDe").val(),
					"relatedDocDptNm": $("#relatedDocDptNm").val(),
					"relatedDocNum": $("#relatedDocNum").val(),
					"relatedDocDe": $("#relatedDocDe").val()
				};

				$.post("/opmanager/give/give-state/reqReg", param, function(response) {
					if(response.isSuccess == true) {
						alert("변경되었습니다.");
						location.href = "/opmanager/give/give-state/reqmng-list";
					} else {
						var errMessage = response.errorMessage;
						if(errMessage == '' || errMessage == undefined){
							alert("오류가 발생했습니다.");
						}else{
							alert(errMessage);
						}
					}
				});

			}
		 }
	}

	/**
	 *	함 수 명 : goList
	 *	기	능  : 변경신청 취소
	 */
	 function goList() {
		history.back();
		// location.href = "/opmanager/give/give-state/reqmng-list";
	 }


</script>
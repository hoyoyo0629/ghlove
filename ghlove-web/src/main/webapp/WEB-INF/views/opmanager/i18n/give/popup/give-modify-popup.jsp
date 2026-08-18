<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">기부상세정보</h1>
		<a href="javascript:self.close();" class="btn_close"><img
			src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<div class="popup_contents">
		<table class="board_write_table" summary="기부내역정보 번경">
			<caption>기부내역변경</caption>
			<colgroup>
				<col style="width: 180px" />
			</colgroup>
			<tbody>
				<tr>
					<td class="label">기부자명</td>
					<td><div id='userName' ><c:out value="${infoPop.userName}"/></div></td>
					<td class="label">아이디</td>
					<td><div id='loginId'><c:out value="${infoPop.loginId}"/></div></td>
				</tr>
				<tr>
					<td class="label">전자납부번호</td>
					<td><div id='elctrnPayNo'><c:out value="${infoPop.elctrnPayNo}"/></div></td>
					<td class="label">납부일자</td>
					<td><div id='sttemntPayDe'><c:out value="${infoPop.sttemntPayDe}"/></div></td>
				</tr>
				<tr>
					<td class="label">기부금액</td>
					<td><div id='cntrAmt'><c:out value="${infoPop.cntrAmt}"/></div></td>
					<td class="label">답례품여부</td>
					<td><div id='rtnpsntReqstCode'><c:out value="${infoPop.rtnpsntReqstCode}"/></div></td>
				</tr>
				<tr>
					<td class="label">발생포인트</td>
					<td><div id='cntrPoint'><c:out value="${infoPop.cntrPoint}"/></div></td>
					<td class="label">잔여포인트</td>
					<td><div id='cntrBlcePoint'><c:out value="${infoPop.cntrBlcePoint}"/></div></td>
				</tr>
				<tr>
					<td class="label">기부상태</td>
					<td><div id='cntrSttusCode'><c:out value="${infoPop.cntrSttusCode}"/></div></td>
					<td class="label">삭제여부</td>
					<td><div id='deleteAt'><c:out value="${infoPop.deleteAt}"/></div></td>
				</tr>
			</tbody>
			</table>
			<table class="" summary="기부내역정보 번경버튼" style="margin-top: 20px; text-align :center;">
			<caption>기부내역변경</caption>
			<colgroup>
				<col style="width: 20%" />
				<col style="width: 20%" />
				<col style="width: 20%" />
			</colgroup>
			<tbody>
				<tr>
					<td><button type="button" class="btn btn-dark-gray btn-mini" onclick="modifyGive('${fn:escapeXml(infoPop.userId)}','${fn:escapeXml(infoPop.elctrnPayNo)}','100','과오납')">과오납처리</button></td>
					<td><button type="button" class="btn btn-dark-gray btn-mini" onclick="modifyGive('${fn:escapeXml(infoPop.userId)}','${fn:escapeXml(infoPop.elctrnPayNo)}','200','포인트생성')">포인트생성</button></td>
					<td><button type="button" class="btn btn-dark-gray btn-mini" onclick="modifyGive('${fn:escapeXml(infoPop.userId)}','${fn:escapeXml(infoPop.elctrnPayNo)}','400','수납취소')">수납취소</button></td>
					<td>
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="modifyGive('${fn:escapeXml(infoPop.userId)}','${fn:escapeXml(infoPop.elctrnPayNo)}','300','수납처리')">수납처리</button>
						<input id="sttemntPayDe" name="sttemntPayDe" title="납부일자" class="input_txt required _filter wd-120" type="text" value="${fn:escapeXml(infoPop.cntrDe)}" maxlength="10">
					</td>
				</tr>
				<tr>
					<td>
						<button type="button"
							style="margin-top: 20px; width: 102px;"
							class="btn btn-dark-gray btn-mini" onclick="confirmSunap('${infoPop.seoulTrgetAt}', '${infoPop.elctrnPayNo}', '${infoPop.cntrLocgovCode}', '${infoPop.mngNo}','${infoPop.userId}','${infoPop.cntrDe}','${infoPop.cntrSn}')">수납확인</button>
					</td>
				</tr>
			</tbody>
		</table>
		<p class="popup_btns">
			<button type="button" class="btn btn-gray btn-mini" onclick="self.close()">닫기</button>
		</p>
	</div>
</div>

<script type="text/javascript">

/* 수납확인 */
function confirmSunap(seoulTrgetAt, elctrnPayNo, cntrLocgovCode, mngNo, userId, cntrDe, cntrSn) {
	if(confirm("수납확인을 진행하시겠습니까?")){
		let param = {
			enapbuNo : elctrnPayNo
			,mngNo : mngNo
			,jijacheCd : cntrLocgovCode
			,seoulTrgetAt : seoulTrgetAt
			,userId : userId
			,cntrDe : cntrDe
			,cntrSn : cntrSn
		};

		$.post('opmanager/give/give-state/popup/giveModifyInfo/confirmSunap',param, function(response) {
			console.dir(response);
			if(response.isSuccess == true) {
				//location.reload();
				alert("수납이력이 확인되었습니다.");
				//self.close();
			}else{
				//let errMsg = (seoulTrgetAt != 'Y') ? response.errorMessage : "수납확인에 실패하였습니다.";
				alert(response.errorMessage);
			}
		});
	}
}

/* 단건 영수증 처리 */
function sendNts(enapbuNo) {
	if(confirm("영수증처리를 진행하시겠습니까?")){
		let param = {enapbuNo : enapbuNo};
		let sttemntPayDe = "${fn:escapeXml(infoPop.sttemntPayDe)}";
		let deleteAt = "${fn:escapeXml(infoPop.deleteAt)}";
		let cntrSttusCode = "${fn:escapeXml(infoPop.cntrSttusCode)}";

		if(sttemntPayDe === '') {
			alert('납부일자가 존재하지 않습니다.');
			return false;
		}

		if(deleteAt === '' || deleteAt === 'Y') {
			alert('취소된 기부건 입니다.');
			return false;
		}

		if(cntrSttusCode !== '정상') {
			alert('정상 납부건이 아닙니다.');
			return false;
		}

		$.post('opmanager/give/give-state/popup/giveModifyInfo/sendNts',param, function(response) {
			console.log(response);
			if(response.isSuccess == true) {
				location.reload();
				alert("영수증 처리가 완료되었습니다.");
				self.close();
			}else{
				alert("영수증 처리를 실패하였습니다.");
			}
		});
	}
}

/* 기부내역처리 통합(과오납/포인트생성/삭제여부변경/수납취소) */
function modifyGive(userId,elctrnPayNo,cntrReqmngCode,reqMsg){
	var discription =  "시스템관리자에 의해 처리됨";
	var sttemntPayDeTemp = $("input[name=sttemntPayDe]").val();
	if(confirm( reqMsg + " 처리를 진행하시겠습니까?")){
		var param = {
				elctrnPayNo : elctrnPayNo,
				userId : userId,
				"cntrSn" : "${fn:escapeXml(infoPop.cntrSn)}",
				"cntrReqmngCode": cntrReqmngCode,
				"loginId": "${fn:escapeXml(infoPop.loginId)}",
				"userName": "${fn:escapeXml(infoPop.userName)}",
				"locgovCode": "${fn:escapeXml(infoPop.cntrLocgovCode)}",
				"sttemntPayDe": sttemntPayDeTemp,
				"cntrAmt": "${fn:escapeXml(infoPop.cntrAmt)}",
				"discription": discription,
				"seoulTrgetAt":"${fn:escapeXml(infoPop.seoulTrgetAt)}"
		}

		$.post("opmanager/give/give-state/popup/giveModifyInfo/modifyGive",param,function(response){
			console.log(response);
	 		//if(response.isSuccess == true && response.data.result_code.toUpperCase("SUCCESS")) {
	 		if(response.isSuccess == true && response.data.result_code == "SUCCESS") {
	 			location.reload();
				alert(reqMsg + " 건 기부변경 완료");
				self.close();
			}else{
	 			alert('기부변경에 실패했습니다.');
	 		}
		});
	}
}

</script>
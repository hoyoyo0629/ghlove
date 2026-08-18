<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">엑셀다운로드 사유</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
	<form id="formPrivacy">
		<input type="hidden" id="privacyAccessLogId" name="privacyAccessLogId" value="${fn:escapeXml(detail.id)}" />
		<input type="hidden" id="managerId" name="managerId" value="${fn:escapeXml(detail.managerId)}" />
		<input type="hidden" id="reasonTempCode" name="reasonTempCode" value="${fn:escapeXml(detail.reasonType)}"/>
		<input type="hidden" id="reasonTempNm" name="reasonTempNm" value="${fn:escapeXml(detail.reasonTypeNm)}"/>
		<input type="hidden" id="succChk" name="succChk" value="${fn:escapeXml(succChk)}"/>
		<div class="popup_contents">
			<table class="board_write_table" summary="엑셀다운로드 사유">
				<caption>엑셀다운로드 사유</caption>
				<colgroup>
					<col style="width: 150px" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<td class="label">다운로드 사유 타입</td>
						<td colspan="3">
							<div id="reasonTy"></div>
						</td>
					</tr>
					<tr>
						<td class="label">사유</td>
						<td colspan="3">
							<div>
								<c:choose>
									<c:when test="${succChk == true}">
										<textarea name="reason" id="reason" title="개인정보 엑셀 다운로드 사유" class="required" style="height:200px;" maxlength="900"><c:out value="${fn:escapeXml(detail.reason)}"/></textarea>
									</c:when>
									<c:otherwise>
										<c:out value="${op:nl2br(detail.reason)}" escapeXml="true"/>
									</c:otherwise>
								</c:choose>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<p class="popup_btns">
				<c:choose>
					<c:when test="${succChk == true}">
						<button type="button" class="btn btn-active" onclick="updateReason();">수정</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btn-active" onclick="self.close();">확인</button>
					</c:otherwise>
				</c:choose>
			</p>
		</div>
	</form>
</div>

<script type="text/javascript">

	$(function() {
		var selectType 		= $("#reasonType")[0];
		var succChk 		= $("#succChk").val();
		var reasonTempCode	= $("#reasonTempCode").val();
		var reasonTempNm 	= $("#reasonTempNm").val();

		if(succChk != 'true'){
			$("#reasonTy").text(reasonTempNm);
		}else{
			if(selectType === undefined){
				var typeLength = Object.keys(reasonType).length;
				var typeText = '';

				typeText += '<select id="reasonType" title="엑셀다운로드타입" name="reasonType" class="wd-400 required" style="margin-left:15px;margin-top:20px">';
				typeText += '<option value="">-선택-</option>';
				if(typeLength > 0){
					Object.keys(reasonType).forEach(function(key) {
						typeText += '<option value="' + key + '">'+ reasonType[key] +'</option>';
					});
				}
				typeText += '</select>';
				$("#reasonTy").before(typeText);
				$("#reasonType").val(reasonTempCode);
			}else{
				$("#reasonTy").val('');
			}
		}
	});

	function updateReason(){
		var $form = $("#formPrivacy");

		var reasonChk 	= $form.find("textarea[name='reason']").val();
		var reasonType 	= $form.find("select[name='reasonType']").val()

		if(reasonType == ''){
			alert("엑셀 다운로드 사유 타입을 고르세요.");
			return false;
		}
		if(reasonChk.length < 5) {
			alert("엑셀 다운로드 사유를 5자 이상 입력해주세요.");
			return false;
		}

		$.post("/common/${requestContext.sellerPage ? 'seller' : 'opmanager'}/privacy-access-log-update", $form.serialize(), function(response) {
            if (response.isSuccess) {
            	//submitCheck = true;

                alert("수정 되었습니다.");

                $form.find("textarea[name='reason']").val('');
              	self.close();
              	opener.parent.search();
            } else {
                alert(response.errorMessage);
            }
        }, 'json');
	}
</script>
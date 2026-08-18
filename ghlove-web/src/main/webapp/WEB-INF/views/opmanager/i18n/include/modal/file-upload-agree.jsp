<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>

	<div class="modal fade" id="fileAgreeModal" tabindex="-1" role="dialog" aria-labelledby="fileUploadAgreeModal">

		<div class="modal-dialog-centered" role="document" style="width: 782px">
			<div class="modal-content popup_wrap">
				<form id="formFileAgree">
					<input type="hidden" name="uri" />
					<input type="hidden" name="url" />
					<!-- POST로 보낼 시 param에 주문번호(id)를 넣음 -->
					<input type="hidden" name="param" />
					<div id="pop_header">
						<h1 class="popup_title">자료 업로드 전 보안 점검</h1>
						<a href="javascript:fileUploadPopupClose();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<div class="security-check-wrap">
								<div class="security-check-box">
									<div class="security-title">
										자료 업로드 전 보안 점검
									</div>

									<p class="security-desc">
										개인정보 포함 여부와 암호화 상태를 확인한 뒤<br class="d-none d-md-block">
										업로드할 수 있습니다.
									</p>

									<div class="security-question">
										<p class="question-title">
											주민등록번호, 계좌번호, 연락처 등 개인정보가 포함되어 있지 않습니다.
										</p>

										<div class="radio-group">
											<div class="form-check form-check-inline">
												<input class="form-check-input" type="radio" name="check01" id="check01Yes"  value="Y">
												<label class="form-check-label" for="check01Yes">예</label>
											</div>

											<div class="form-check form-check-inline">
												<input class="form-check-input" type="radio" name="check01" id="check01No" checked="" value="N">
												<label class="form-check-label" for="check01No">아니오</label>
											</div>
										</div>
									</div>

									<div class="security-question">
										<p class="question-title">
											개인정보가 포함된 경우 승인 - 승인된 방식으로 암호화했습니다.
										</p>

										<div class="radio-group">
											<div class="form-check form-check-inline">
												<input class="form-check-input" type="radio" name="check02" id="check02Yes" value="Y">
												<label class="form-check-label" for="check02Yes">예</label>
											</div>
											<div class="form-check form-check-inline">
												<input class="form-check-input" type="radio" name="check02" id="check02No" checked="" value="N">
												<label class="form-check-label" for="check02No">아니오</label>
											</div>
										</div>
									</div>

									<div class="security-question security">
										<p class="question-title">
											업로드자료의 공유범위와 열람 권한을 다시 확인했습니다.
										</p>
										<div class="radio-group">
											<div class="form-check form-check-inline">
												<input class="form-check-input" type="radio" name="check03" id="check03Yes" value="Y">
												<label class="form-check-label" for="check03Yes">예</label>
											</div>

											<div class="form-check form-check-inline">
												<input class="form-check-input" type="radio" name="check03" id="check03No" checked="" value="N">
												<label class="form-check-label" for="check03No">아니오</label>
											</div>
										</div>
										<div class="info-text">
											<span><i class="svg-icon file-text"></i>보안정보 정보</span>
											<span><i class="svg-icon circle-close text-danger"></i>미확인 자료 업로드 금지</span>
											<span><i class="svg-icon lock"></i>민감정보 포함 시 암호화 필수</span>
										</div>
									</div>

									<div class="security-btn-area">
										<button type="button" class="btn security-btn" onclick="checkFileUploadAgree()">
											업로드 진행
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<input type="hidden" id="fileBtnId" />
				</form>
			</div>
		</div>
	</div>

<script type="text/javascript">
	$(function() {

	});

	function checkFileUploadAgree(){
		var fileRadioTotCnt = $("#formFileAgree .form-check-input[value='Y']").length;
		var fileRadioCheckCnt = $("#formFileAgree .form-check-input[value='Y']:checked").length;
		if(fileRadioTotCnt > fileRadioCheckCnt){
			alert("모든 점검 항목이 체크되어야 업로드가 가능합니다.");
			return false;
		}else{
			$("#fileAgreeModal").modal('hide');
			$("#formFileAgree .form-check-input[value='N']").prop("checked", true);
			//$("input[name='"+$("#fileBtnId").val()+"']").trigger('click');	
			$("#"+$("#fileBtnId").val()).trigger('click');	// 동일 name을 가진 input file이 많아서 id로 처리
		}
	}

	function fileUploadPopupClose() {
		$("#fileBtnId").val('');
		$("#fileAgreeModal").modal('hide');
		$("#formFileAgree .form-check-input[value='N']").prop("checked", true);
	}

	function fileUploadPopupOpen (fileBtnId) {
		$("#fileBtnId").val(fileBtnId);
        $("#fileAgreeModal").modal({backdrop: 'static'}, 'show');
	}
</script>

<%-- <link rel="stylesheet" type="text/css" href="<c:url value="/content/opmanager/css/common.css" />"> --%>

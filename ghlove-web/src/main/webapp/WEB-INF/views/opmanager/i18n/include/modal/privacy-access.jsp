<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>

    <div class="modal fade" id="privacyModal" tabindex="-1" role="dialog" aria-labelledby="privacyAccessModal">

        <div class="modal-dialog-centered" role="document" style="width: 450px">
            <div class="modal-content popup_wrap">
                <form id="formPrivacy">
                    <input type="hidden" name="uri" />
                    <input type="hidden" name="url" />
					<!-- POST로 보낼 시 param에 주문번호(id)를 넣음 -->
                    <input type="hidden" name="param" />
                    <div id="pop_header">
                        <h1 class="popup_title">개인정보 엑셀 다운로드 사유</h1>
                        <a href="javascript:popupClose();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <textarea name="reason" id="reason" title="개인정보 엑셀 다운로드 사유" class="required" style="height:200px;" maxlength="900"></textarea>
<!--                             <p class="text-info pt10"> -->
<!--                                 * 엑셀 다운로드 사유를 입력 후 '<strong>등록</strong>' 버튼을 클릭합니다.<br /> -->
<!--                                 * 엑셀 다운로드 사유를 입력하지 않으면 다운로드가 불가합니다. -->
<!--                             </p> -->
                            <div class="text-info pt10" style="display: flex;">
                            	<div style="margin-right: 5px;">1. </div>
                            	<div>개인정보는 기부금 관리 및 답례품 관리 목적 외에 활용할 수 없으며, 무단으로 유출하거나 목적외 활용할 경우 개인정보보호법에 따른 책임이 있음을 확인합니다.</div>
                            </div>
                            <div class="text-info pt10" style="display: flex;">
                           		<div style="margin-right: 5px;">2. </div>
                            	<div>개인정보가 포함된 자료는 반드시 암호화하여 보관하여야 합니다.</div>
                            </div>
		                    <div class="text-info pt10" style="display: flex;">
                           		<div style="margin-right: 5px;">3. </div>
                            	<div>활용 목적 완료 후 폐기하여야 합니다.</div>
                            </div>
							<div class="text-info pt10">
								<input name="checkbox" type="checkbox" id="checkAccess" style="cursor:pointer" />
								<label for="checkAccess" style="cursor:pointer">
								위 사항을 확인하였습니다.
								</label>
							</div>
		                    <div class="text-info pt10" style="display: flex; color: black;">
                           		<div style="margin-right: 5px;">★ </div>
                            	<div>엑셀은 개인정보보호를 위해 암호화되며 비밀번호는 <br>다운받으시는 분의 <span class="text-info pt10">고향사랑e음 ID</span>로 생성됩니다.</div>
							</div>

						</div>
	                    <div class="popup_btns" style="display: flex; justify-content: center; align-items: center; gap: 8px;">
	                        <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
	                        <button type="submit" class="btn btn-active">등록</button>
	                    </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

<script type="text/javascript">
    $(function() {
        var $form = $("#formPrivacy");
		//var reasonTxt = $form.find("textarea[name='reason']").val();
		if ($form.validator) {
			$form.validator(function() {
				if ( $("#reason").val().length < 5 ) {
			    	alert("엑셀다운로드 사유를 5글자 이상 입력해주세요.");
			        return false;
			    }

				if(!$("#checkAccess").is(":checked")){
					alert("동의 항목을 체크해주시기 바랍니다.");
			        return false;
				}

	            $.post("/common/${requestContext.sellerPage ? 'seller' : 'opmanager'}/privacy-access-log", $form.serialize(), function(response) {
	                if (response.isSuccess) {
	                    alert("다운로드가 진행 중입니다. 잠시만 기다려주세요.");

	                    $form.find("textarea[name='reason']").val('');
	                    $("#privacyModal").modal('hide');

	                    let param = $form.find("input[name='param']").val();
	                    // 체크박스가 선택되지 않았으면 param은 비어있는 상태
	                    if(param === undefined || param === '' || param === null)
	                    	location.href = $form.find("input[name='uri']").val();
	                    else{
	                    	// Ajax로 요청 시 엑셀 다운로드가 되지 않음
	                    	// $.post("/opmanager/order/shipping-ready-mobile/order-excel-download", {'param' : param}, function(response){})
	                    	// 체크박스가 선택 되었다면, param을 POST 방식으로 URL 요청
							const form = $('<form>', {
	                    		method : 'POST',
	                    		action : '/${requestContext.sellerPage ? "seller" : "opmanager"}/order/shipping-ready-mobile/order-excel-download',
	                    		target : '_self'
	                    	}).append($('<input>', {
	                    		type : 'hidden',
	                    		name : 'param',
	                    		value : param
	                    	}));

	                    	form.appendTo('body').submit().remove();
	                    }


	                } else {
	                    alert(response.errorMessage);
	                }
	            }, 'json');
	            return false;
	        });
		}
    });

	function popupClose() {
		var $form = $("#formPrivacy");
		$form.find("textarea[name='reason']").val('');
		$("#privacyModal").modal('hide');
	}

	$('#privacyModal').on('hidden.bs.modal',function(){
		$("#checkAccess").prop('checked',false);
	});

	$("#formPrivacy").on("change", "#reasonType", function() {
		const selectedType = $(this).find("option:selected").text();
		$("#reason").val(selectedType);
	});
</script>
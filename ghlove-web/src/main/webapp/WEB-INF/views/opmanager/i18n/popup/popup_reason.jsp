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
	<div class="popup_wrap">
		<h1 class="popup_title">개인정보 엑셀 다운로드 사유</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
	<form id="formPrivacy">
        <input type="hidden" id="uri" name="uri" value=""/>
        <input type="hidden" id="url" name="url" value=""/>
        <div class="modal-body">
        	<div class="flex_box gap-08">
                <form:select path="reasonType" id="reasonType" name="reasonType" class="wd-400 required" title="엑셀다운로드타입">
                    <form:option value="">-${op:message('M00431')}-</form:option>
                    <c:forEach items="${reasonType}" var="reasonType">
                        <form:option value="${reasonType.id}" label="${reasonType.label}" />
                    </c:forEach>
                </form:select>
            </div>
            <div class="form-group">
                <textarea name="reason" id="reason" title="개인정보 엑셀 다운로드 사유" class="required" style="height:200px;" maxlength="900"></textarea>
                <p class="text-info pt10">
                    * 엑셀 다운로드 사유를 5글자 이상 입력 후 '<strong>등록</strong>' 버튼을 클릭합니다.<br />
                    * 엑셀 다운로드 사유를 입력하지 않으면 다운로드가 불가합니다.
                </p>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close();">취소</button>
            <button type="submit" class="btn btn-active">등록</button>
        </div>
    </form>
</div>

<script type="text/javascript">
	var submitCheck = false;
	$(function() {
			
	    var $form = $("#formPrivacy");
		
	    var strUri = $(opener.document).find("#excelLogForm").find("input[name='uri']").val();
	    var strUrl = $(opener.document).find("#excelLogForm").find("input[name='url']").val();
	    
	    $form.find("input[name='uri']").val(strUri);
	    $form.find("input[name='url']").val(strUrl);
	    
	    $form.validator(function() {
	    	if(!submitCheck){
	    		if(strUrl == ""){
	    			alert("엑셀다운로드 URL 정보가 없습니다. 창을 닫고 새로 고침 후에 다시 진행해주세요.");
	    			return false;
	    		}
	    		if ( $("#reason").val().length < 5 ) {
	    	    	alert("엑셀다운로드 사유를 5글자 이상 입력해주세요.");
	    	        return false;
	    	    }
	    		$.post("/common/${requestContext.sellerPage ? 'seller' : 'opmanager'}/privacy-access-log", $form.serialize(), function(response) {
		            if (response.isSuccess) {
		            	submitCheck = true;
		            	
		                alert("저장되었습니다.");
		                
		                $form.find("textarea[name='reason']").val('');
		                $("#privacyModal").modal('hide');
		                opener.location.href = $form.find("input[name='uri']").val();
		              	//$("#formPrivacy").find("button[type=submit]").attr("disabled", true)
		                //location.href = $form.find("input[name='uri']").val();
		              	self.close();
		            } else {
		                alert(response.errorMessage);
		            }
		        }, 'json');
		        return false;
	    	}else{
	    		alert("다운로드를 진행한 이력이 었어서 창을 닫고 새로고침 후 진행해주세요.");
	    		return false;
	    	}
	    });
	});
	
	$( "formPrivacy" ).submit(function( event ) {
	    if ( $( "#reason" ).val().length < 4 ) {
	    	alert("test");
	        return;
	    }

	    event.preventDefault();
	});
</script>
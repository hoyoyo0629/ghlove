<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"     uri="http://www.springframework.org/security/tags"%>


<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>대표배너관리</span></h3>
<form id="representativeBanner" method="post" enctype="multipart/form-data">
	<c:if test="${banner.representativeBannerId > 0}">
	    <div class="btn_all btn_right mb15">
	        <div class="flex_box gap-08">
	            <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
	            <button type="button" class="btn btn-default btn-mini" onclick="location.href='/opmanager/representative-banner/list'">목록</button>
	        </div>
	    </div>
    </c:if>

    <div class="board_write">
        <table class="board_write_table mt30" summary="">
            <colgroup>
                <col style="width:220px;">
                <col style="width:180px;">
            </colgroup>
            <tbody>
                <tr>
                    <td class="label"><span class="required_mark">*</span>배너명</td>
                    <td colspan="3">
                        <div class="flex_box gap-08">
                            <input name="title" title="배너명" class="input_txt required _filter half" type="text" value="${fn:escapeXml(banner.title)}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="label">배너 설명</td>
                    <td colspan="3">
                        <div class="flex_box gap-08">
                            <textArea name="bannerContent" title="배너 설명" class="input_txt required _filter half" type="text">${fn:escapeXml(banner.bannerContent)}</textArea>
                        </div>
                    </td>
                </tr>
                <c:if test="${banner.representativeBannerId == null}">
                <tr>
                    <td class="label" rowspan="2"><span class="required_mark">*</span>이미지</td>
                    <td class="line-right tcenter">
                        <div>
                            <p>PC</p>
                            <p class="mt10 point">권장사이즈는<br>1280px X 400px 입니다.</p>
                        </div>
                    </td>
                    <td colspan="2">
                        <div class="flex_box item-center">
                            <input type="file" name="uploadFilePc" class="full input_file" title="이미지" accept="image/png, image/jpeg, image/gif" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="line-right tcenter">
                        <div>
                            <p>mobile</p>
                            <p class="mt10 point">권장사이즈는<br>768px X 280px 입니다.</p>
                        </div>
                    </td>
                    <td colspan="2">
                        <div class="flex_box item-center">
                            <input type="file" name="uploadFileMobile" class="full input_file" title="이미지" accept="image/png, image/jpeg, image/gif" />
                        </div>
                    </td>
                </tr>
                </c:if>
                <c:if test="${banner.representativeBannerId > 0}">
                <tr>
                    <td class="label" rowspan="2"><span class="required_mark">*</span>이미지</td>
                    <td class="line-right tcenter">
                        <div>
                            <p>PC</p>
                            <p class="mt10 point">권장 사이즈는<br>1280px X 400px 입니다.</p>
                        </div>
                    </td>
                    <td colspan="2">
                        <div class="flex_box item-center" id="imagePc">
                            <span class="input_file_txt">파일 수정 : </span>
                            <input type="file" name="uploadFilePc" class="full input_file" title="이미지" accept="image/png, image/jpeg, image/gif" />
                        </div>
                        <c:if test="${banner.fileNamePc != ''}">
	                        <div class="file_camera">
	                            <div class="image-box">
	                                <img src="${fn:escapeXml(banner.imageSrcPc)}" class="item_image size-100 none" alt="상품이미지">
	                                <a href="javascript:deleteBanner('${fn:escapeXml(banner.representativeBannerId)}','pc');"><img src="/content/images/btn/file_close.gif" alt="close"></a>
	                            </div>
	                        </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td class="line-right tcenter">
                        <div>
                            <p>mobile</p>
                            <p class="mt10 point">권장사이즈는<br>768px X 280px 입니다.</p>
                        </div>
                    </td>
                    <td colspan="2">
                        <div class="flex_box item-center" id="imageMobile">
                            <span class="input_file_txt">파일 수정 : </span>
                            <input type="file" name="uploadFileMobile" class="full input_file" title="이미지" accept="image/png, image/jpeg, image/gif" />
                        </div>
                        <c:if test="${banner.fileNameMobile != ''}">
	                        <div class="file_camera">
	                            <div class="image-box">
	                                <img src="${fn:escapeXml(banner.imageSrcMobile)}" class="item_image size-100 none" alt="상품이미지">
	                                <a href="javascript:deleteBanner('${fn:escapeXml(banner.representativeBannerId)}','mobile');"><img src="/content/images/btn/file_close.gif" alt="close"></a>
	                            </div>
	                        </div>
                        </c:if>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <td class="label">이미지 링크</td>
                    <td colspan="3">
                        <div class="flex_box gap-08">
                            <input id="" name="linkUrl" title="이미지 링크" class="input_txt _filter half" type="text" value="${fn:escapeXml(banner.linkUrl)}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="label"><span class="required_mark">*</span>사용여부</td>
                    <td colspan="3">
                        <div class="flex_box gap-12">
                            <div class="input-form">
                                <input id="rdo1-1" name="useYn" type="radio" value="Y" ${banner.useYn == 'Y' ? 'checked="checked"' : ''} checked="checked">
                                <label for="rdo1-1">사용</label>
                            </div>
                            <div class="input-form">
                                <input id="rdo1-2" name="useYn" type="radio" value="N" ${banner.useYn == 'N' ? 'checked="checked"' : ''}>
                                <label for="rdo1-2">중지</label>
                            </div>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

	<c:if test="${banner.representativeBannerId == null}">
	    <div class="btn_all btn_center mt30">
	        <div class="flex_box gap-08">
	            <button type="submit" class="btn btn-dark-gray btn-small">등록</button>
	            <button type="button" class="btn btn-default btn-small" onclick="location.href='/opmanager/representative-banner/list'">취소</button>
	        </div>
	    </div>
	</c:if>
</form>

<script type="text/javascript">

	$(document).ready(function(){
		// 파일수정 영역 숨기기
   		if('${fn:escapeXml(banner.fileNamePc)}' != '') $('#imagePc').hide();
  		if('${fn:escapeXml(banner.fileNameMobile)}' != '') $('#imageMobile').hide();
	});

	$(function() {

		// validator
		try{
			$('#representativeBanner').validator(function() {

		    	// 지자체 관리자만 권한있음
		    	if('${fn:escapeXml(adminRole)}' != 'SYS') {
		    		alert("시스템 관리자만 등록/수정이 가능합니다.");
		    		return false;
		    	}

				var mode = location.href.includes("/0");	// 편집모드

				var selectFileCount = 0;
				$.each($(':file[class*="input_file"]'), function(){
					if ($(this).val() != '') {
						selectFileCount++;
					}
				});

				var title = $('input[name="title"]').val();

				if (title == '') {
					alert("배너명을 입력해 주세요.");
					$('input[name="title"]').focus();
					return false;
				}

				if (mode && selectFileCount < 2) {	// 등록일때는 파일 2개 모두 체크
					alert('PC와 모바일 이미지 모두 등록해주세요.');
					return false;
				}

				if (!mode && (selectFileCount == 0 && '${fn:escapeXml(banner.fileNamePc)}' == '') || (selectFileCount == 0 && '${fn:escapeXml(banner.fileNameMobile)}' == '') || (selectFileCount < 2 && '${fn:escapeXml(banner.fileNamePc)}' == '' && '${fn:escapeXml(banner.fileNameMobile)}' == '')) {	// 수정일때는 삭제된 파일만 체크
					alert('PC와 모바일 이미지 모두 등록해주세요.');
					return false;
				}

				if( $('input[name=uploadFilePc]').val() != "" ){
			      var ext = $('input[name=uploadFilePc]').val().split('.').pop().toLowerCase();
			  	  if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	     alert('등록할 수 없는 파일확장자입니다.');
			  	     $('input[name=uploadFilePc]').val(""); // input file 파일명을 다시 지워준다.
			  	     return false;
			 	  }
			    }

				if( $('input[name=uploadFileMobile]').val() != "" ){
			      var ext = $('input[name=uploadFileMobile]').val().split('.').pop().toLowerCase();
			  	  if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	     alert('등록할 수 없는 파일확장자입니다.');
			  	     $('input[name=uploadFileMobile]').val(""); // input file 파일명을 다시 지워준다.
			  	     return false;
			 	  }
			    }

				if( $('input[name=uploadFilePc]').val() != "" ){
				    var fileSize = $('input[name=uploadFilePc]')[0].files[0].size;
				    var maxSize = 20 * 1024 * 1024;	// 20MB

				    if(fileSize > maxSize){
				       alert("첨부파일 사이즈는 20MB 이내로 등록 가능합니다. ");
				        $('input[name=uploadFilePc]').val("");
				        return false;
				     }
				}

				if( $('input[name=uploadFileMobile]').val() != "" ){
				    var fileSize = $('input[name=uploadFileMobile]')[0].files[0].size;
				    var maxSize = 20 * 1024 * 1024;	// 20MB

				    if(fileSize > maxSize){
				       alert("첨부파일 사이즈는 20MB 이내로 등록 가능합니다. ");
				        $('input[name=uploadFileMobile]').val("");
				        return false;
				     }
				}

				if(mode) {	// 등록 하시겠습니까?
					Common.confirm("정보를 ${op:message('M00159')}", function(form) {
						$('#representativeBanner').submit();
					});
				} else {	// 수정 하시겠습니까?
					Common.confirm("정보를 ${op:message('M00365')}", function(form) {
						$('#representativeBanner').submit();
					});
				}

				return false;

			});
		} catch(e) {
			alert(e.message);
		}

	});

	// 파일 삭제
	function deleteBanner(id,type) {

		var message = Message.get("M00196");

		var param = {
			"representativeBannerId" : id,
			"deleteFlag" : type
		};

		if (confirm(message)) {
			$.post("/opmanager/representative-banner/delete", param, function(resp){
				Common.responseHandler(resp, function(){

					//이미지 삭제되고 파일선택 버튼 노출
					if(type == 'pc') {
						$('#imagePc').show();
					} else if(type == 'mobile') {
						$('#imageMobile').show();
					}

					alert(Message.get("M00205"));	// 삭제 되었습니다.

					location.reload();
				});
			});
		}

	}

</script>

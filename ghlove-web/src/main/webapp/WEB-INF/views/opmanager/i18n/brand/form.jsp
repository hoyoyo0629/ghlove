<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

		<div class="location">
			<a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
		</div>
		<h3><span><!-- 등록 --></span></h3>
		<form:form modelAttribute="brand" method="post" enctype="multipart/form-data">
			<div class="board_write">
				<table class="board_write_table" summary="">
					<colgroup>
			            <col style="width:220px;"/>
			            <col/>
			        </colgroup>
			        <tbody>
			            <tr>
			                <td class="label"><span class="required_mark">*</span><c:out value="${op:message('브랜드명')}"/></td><!-- 브랜드명 -->
			                <td>
			                    <div>
			                        <form:input id="" name="" path="brandName" title="${op:message('브랜드명')}" class="input_txt required _filter full" type="text" /><!-- 브랜드명 -->
			                    </div>
			                </td>
			            </tr>
			            <tr>
			                <td class="label">브랜드 <c:out value="${op:message('M00752')}"/></td><!-- 브랜드 이미지 -->
			                <td>
			                    <div class="flex_box item-center">
			                        <input type="file" name="file" class="full input_file" title="${op:message('M00752')}" accept="image/png, image/jpeg, image/gif" onchange="javascript:checkSize(this, 5);"/><!-- 이미지 -->
									<!-- <c:if test="${!empty brand.brandImage}">
										<div style="margin-top: 10px">
											<input type="hidden" name="brandImage" value="${fn:escapeXml(brand.brandImage)}" />
											<img src="${fn:escapeXml(brand.brandImageSrc)}" alt="" style="border: 1px solid #ccc; padding: 5px;" />
											<p>
												<label><input type="checkbox" name="brandImageDeleteFlag" value="Y" />이미지 삭제</label>
												<input type="hidden" name="!brandImageDeleteFlag" value="N" />
											</p>
										</div>
									</c:if> -->
			                    </div>
			                </td>
			            </tr>
			            <tr>
			                <td class="label"><c:out value="${op:message('M01431')}"/></td><!-- 공개여부 -->
			                <td>
			                    <div class="flex_box gap-12">
			                        <!-- <div class="input-form">
			                            <input id="rdo1-1" name="rdo1" type="radio" value="" checked="checked">
			                            <label for="rdo1-1">공개</label>
			                        </div>
			                        <div class="input-form">
			                            <input id="rdo1-2" name="rdo1" type="radio" value="">
			                            <label for="rdo1-2">비공개</label>
			                        </div> -->
			                        <form:radiobutton path="displayFlag" label="${op:message('M00096')}" value="Y" checked="checked" /><!-- 공개 -->
									<form:radiobutton path="displayFlag" label="${op:message('M00097')}" value="N" /><!-- 비공개 -->
			                    </div>
			                </td>
			            </tr>
			            <tr>
			                <td class="label"><c:out value="${op:message('')}"/>브랜드 상단내용</td><!-- 브랜드 상단내용 -->
			                <td>
			                    <!-- smart_editor2-wrap -->
			                    <!-- <div style="width: 100%; height:300px; background:#eee;">스마트 에디터 영역 입니다.</div> -->
			                    <!-- 실 사용 소스 -->
			                    <div class="smart_editor2-wrap">
			                        <form:textarea path="brandContent" cols="30" rows="20" class="editor-content" title="${op:message('M00006')}" />
			                    </div>
			                    <!-- // 실 사용 소스 -->
			                    <!-- smart_editor2-wrap -->
			                </td>
			            </tr>
			        </tbody>
			    </table>
			</div>
			<div class="btn_all btn_center">
			    <div class="flex_box gap-08">
			        <button type="submit" class="btn btn-dark-gray btn-small" onclick="javascript:checkMsg(event);"><c:out value="${op:message('M00088')}"/></button><!-- 등록 -->
			        <button type="button" class="btn btn-default btn-small" onclick="document.location.href='/opmanager/brand/list';"><c:out value="${op:message('M00037')}"/></button><!-- 취소 -->
			    </div>
			</div>
		</form:form>



<module:smarteditorInit />
<module:smarteditor id="brandContent" />
<script type="text/javascript">
$(function(){

	$('#brand').validator(function() {
		Common.getEditorContent("brandContent");
	});

});

/* function openDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {

            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            // 우편번호와 주소 정보를 해당 필드에 넣고, 커서를 상세주소 필드로 이동한다.
            document.getElementById('post1').value = data.postcode1;
            document.getElementById('post2').value = data.postcode2;
            document.getElementById('address').value = data.address;

            //전체 주소에서 연결 번지 및 ()로 묶여 있는 부가정보를 제거하고자 할 경우,
            //아래와 같은 정규식을 사용해도 된다. 정규식은 개발자의 목적에 맞게 수정해서 사용 가능하다.
            //var addr = data.address.replace(/(\s|^)\(.+\)$|\S+~\S+/g, '');
            //document.getElementById('address').value = addr;

            document.getElementById('detailAddress').focus();
        }
    }).open();
} */

// 첨부파일 용량 체크
function checkSize(input, maxSize) {
	try {
	    if (input.files && input.files[0].size > (maxSize * 1024 * 1024)) {
	        alert("파일 사이즈가 " + maxSize + "mb 를 넘습니다.");
	        input.value = null;
	    }
	} catch (e) {
        alert("문제가 발생했습니다.");
        input.value = null;
	}
}

//수정버튼 클릭시
function checkMsg(event) {
	if (!confirm("정보를 등록 하시겠습니까?")) {/* 정보를 등록 하시겠습니까? */
		event.preventDefault();
		return;
	}
}
</script>

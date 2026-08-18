<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a> &gt; 공지사항입력
</div>


<form:form modelAttribute="prjNotice" id="prjNotice" method="post" enctype="multipart/form-data" action='${fn:escapeXml(requestContext.managerUri)}/designated-donation/list/notice/save'>
    <h3 class="custom">공지사항입력</h3>
    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="특정사업에 기부하기 공지사항">
                    <colgroup>
                        <col style="width: 120px" />
                        <col style="width: auto;" />
                        <col style="width: 120px" />
                        <col style="width: auto;" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>제목</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:input path="prjNoticeSubject" maxlength="100" class="input_txt required _filter wd-500" title="제목"/>
                                </div>
                            </td>
                            <td class="label"><span class="required_mark">*</span>공개 여부</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:radiobutton path="displayYn" value="Y" label="공개" />
                                    <form:radiobutton path="displayYn" value="N" label="비공개" />
                                </div>
                            </td>
                        </tr>
                    </tbody>

                </table>
            </div>
		</div>

        <div class="item_list mt70">
            <h3>
                <span>파일 첨부</span>
            </h3>

            <div class="board_write">

                <table class="board_write_table item_image_info">
                    <colgroup>
                        <col style="width: 220px;">
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><p>첨부파일</p></td> <!-- 상세이미지 -->
                            <td>
                                <div class="flex_box item-center" style="display: block;">
                                    <button type="button" id="add_detail_image_file" style="display:none" class="table_btn"><span>+ ${op:message('M00984')}</span></button>
                                    <p class="text-info text-sm" style="margin-bottom: 10px;">
                                    	* 여러 개의 파일을 드래그하거나 선택한 후 한 번에 등록할 수 있습니다.<br />
		      							* 첨부가능 확장자 : jpg, jpeg, gif, bmp, png, doc, docx, xls, xlsx, ppt, pptx, pdf, tif, tiff, hwp, hwpx, zip<br />
<!--                                         * 상품 이미지는 제한 없이 등록이 가능합니다.<br /> -->
<!--                                         * [파일선택] 버튼을 선택한 후 파일 선택 창에서 상품 이미지를 복수로 선택하거나 드레그 하여 선택 후 등록해 주십시오.<br /><br /> -->
                                    </p>
                                    <input type="file" name="prjNoticeFiles" multiple="multiple" class="full input_file" />
                                </div>
                                <div>
                                    <c:forEach items="${prjNotice.prjFiles}" var="prjFile" varStatus="i">
	                                    <div id="file_${fn:escapeXml(prjFile.fileSeq)}">
		                                	<a href="${fn:escapeXml(requestContext.managerUri)}/designated-donation/list/notice/download-file?prjNoticeId=${fn:escapeXml(prjFile.prjNoticeId)}&fileSeq=${fn:escapeXml(prjFile.fileSeq)}">
		                                		<c:out value="${prjFile.orgFileName}"></c:out>
		                                	</a>
	                                		<img src="/content/opmanager/images/icon/icon_x.gif" alt="삭제" onclick="javascript:deletePrjNoticeFile(${fn:escapeXml(prjFile.prjNoticeId)}, ${fn:escapeXml(prjFile.fileSeq)});" style="cursor:pointer;width:15px;height:15px;margin-left:10px;"/>
		                                </div>
                                    </c:forEach>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="item_list mt70">
            <h3><span>내용</span></h3> <!-- 상세 설명 -->
            <form:textarea path="prjNoticeCn" cols="30" rows="20" class="editor-content" style="width:99%;" title="${op:message('M00990')}" />
        </div>
        <div class="item-option-table2">
            <div>
            <p class="text-info text-sm">* 시각 장애인을 위한 이미지 설명을 입력하여 주시길 부탁드립니다. *</p>
                <table class="inner-table tbl-option active" id="sActiveOption2">
                    <colgroup>
                        <col style="width: 100px;"/>
                        <col/>
                        <col style="width: 50px;"/>
                    </colgroup>
                    <thead>
                        <tr>
                            <th style="text-align:center;">순번</th>
                            <th style="text-align:center;">이미지 설명</th>
                            <th>삭제</th>
                        </tr>
                    </thead>
                    <tbody id="item-options2">
                        <c:forEach items="${prjNotice.prjNoticeImageExplains}" var="prjNoticeImageExplain" varStatus="loopStatus">
                            <tr>
                                <td>
                                    <input type="text" id="prjNoticeImageExplains[${fn:escapeXml(loopStatus.index)}].imgSeq" name="prjNoticeImageExplains[${fn:escapeXml(loopStatus.index)}].imgSeq" maxlength="200" class="이미지 설명 순서" title="이미지 설명 순서" value="${fn:escapeXml(loopStatus.index) + 1}"  readonly/>
                                </td>
                                <td>
                                    <textarea id="prjNoticeImageExplains[${fn:escapeXml(loopStatus.index)}].imgDesc" name="prjNoticeImageExplains[${fn:escapeXml(loopStatus.index)}].imgDesc">${fn:escapeXml(prjNoticeImageExplain.imgDesc)}</textarea>
                                </td>
                                <td><a href="#" class="btn btn-dark-gray btn-sm delete-item-option2">삭제</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="item-options-info">
                    <div class="btn_all btn_right">
                        <div class="flex_box">
                            <button type="button" class="btn btn-gradient btn-sm add-item-option2"><span>+ 추가</span></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 전송 -->
        <div id="buttons" class="btn_all btn_right">
            <div class="flex_box gap-08">
                    <button type="submit" class="btn btn-dark-gray btn-mini">${prjNotice.prjNoticeId == 0 || prjNotice.prjNoticeId == '' ? '등록' : '수정'}</button>
                    <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/designated-donation/list/notice/${fn:escapeXml(prjNotice.prjId)}')" class="btn btn-default">${op:message('M00480')}</a>
            </div>
        </div>
    </div>
	<input id="prjId" name="prjId" type="hidden" value="${fn:escapeXml(prjNotice.prjId)}" />
	<input id="prjId" name="prjNoticeId" type="hidden" value="${fn:escapeXml(prjNotice.prjNoticeId)}" />
</form:form>
<module:smarteditorInit />
<module:smarteditor id="prjNoticeCn" />

<script type="text/javascript">


$(function() {

	window.onpageshow = function(event) {
	    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
	        // 에디트 내용을 decode하여 표시
	        $('#prjNoticeCn')[0].value = decodeURIComponent($('#prjNoticeCn')[0].value);
	    }
	}

    scrollButtons();
    // 폼체크
    $("#prjNotice").validator({
        'submitHandler' : function() {

        	if (!$('input[name=displayYn]:checked').val()) {
				alert('공개여부 항목을 입력해주세요.');
				$("#displayYn1")[0].focus();
        		return false;
        	}
            // 이미지 파일 갯수 제한
			if ($('input[name="prjNoticeFiles"]')[0].files.length > 10) {
				alert('파일은 최대 10까지 등록할 수 있습니다.');
				$('input[name="prjNoticeFiles"]').focus();
				return false;
			}
            /*
			let maxSize = 500 * 1024;	//500KB

			for(let i=0; i < $('input[name="prjImageFiles[]"]')[0].files.length; i++) {

				if($('input[name="prjImageFiles[]"]')[0].files[i].size > maxSize) {
					alert("이미지 용량은 파일당 500KB 이하로 등록 가능합니다.\n첨부된 파일의 용량을 확인해주세요!");
					$('input[name="prjImageFiles[]"]').focus();
					return false;
				}
			}
*/
            //접근성 이미지 설명 입력란에 공백이 들어 올 시 해당 로우 제거하여 not null 데이터가 null로 들어가지 않도록 방지
            let options = $('#item-options2 tr');
            let optionSize = options.length;
            for(let i = optionSize - 1 ; i >= 0 ; i--) {
                let inputValue = $($($(options[i]).children()[1]).children()[0]).val();
                if (!inputValue || inputValue.trim() === "") {
                    $(options[i]).remove();
                }
            }

            options = $('#item-options2 tr');
            optionSize = options.length;
            updateRowIndex();

            // 스마트 에디터 이미지 조회
            let outerIframe = document.querySelectorAll("#editor_frame")[0];
            let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
            let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");
            let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
            let images = innerIframeDoc.querySelectorAll("img");

            let imageSize = images.length;

            if (optionSize == 0 && imageSize > 0) {
                alert("시각 장애인을 위한 이미지 설명을 입력 부탁드립니다.");
                return false;
            }

            /* 접근성 이미지 설명 이미지와 갯수 매칭 유효성 검사 */
            if (optionSize != imageSize) {
                alert('상세설명에 올리신 사진의 갯수와 \n이미지 설명의 갯수를 동일하게 맞추어 주셔야 합니다. \n현재 작성하신 이미지 설명 갯수: '+optionSize+' , 상세설명에 올리신 이미지 갯수: '+imageSize+' ');
                return false;
            }

            //접근성 이미지 설명 이미지 alt에 입력
            for (let i = 0; i < imageSize; i++) {
                let img = images[i];
                img.setAttribute("alt", $($($(options[i]).children()[1]).children()[0]).val());
            }

            Common.getEditorContent("prjNoticeCn"); //넣는 로직 전에 넣으면 alt도 저장 안됨

            if ($('#prjNoticeCn').val().toLowerCase() == '<p>&nbsp;</p>' || $('#prjNoticeCn').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

            //smarteditor값 따로 encode하여 전송하여 서버에서 decode
            $('#prjNoticeCn')[0].value = encodeURIComponent($('#prjNoticeCn')[0].value);
            //return false;
        }
    }); // formcheck validator end

}); //function() end

	// 이미지 설명 로우 추가
	$('.add-item-option2').on('click', function() {
	    let newHtml = '<tr>' +
	    '<td>' +
	    '<input type="text" id="prjNoticeImageExplains[0].imgSeq" name="prjNoticeImageExplains[0].imgSeq" class="" title="이미지 설명 순서" readonly/>' +
	    '</td>' +
	    '<td>' +
	    '<textarea id="prjNoticeImageExplains[0].imgDesc" name="prjNoticeImageExplains[0].imgDesc"   title="이미지 설명" ></textarea>' +
	    '</td>' +
	    '<td><a href="#" class="btn btn-dark-gray btn-sm delete-item-option2" data-index="0">삭제</a></td>'+
	    '</tr>';
	    $('#item-options2').append(newHtml);
	    updateRowIndex();
	});

    // 이미지 설명 로우 삭제
	$('#item-options2').on('click', '.delete-item-option2', function(e) {
		e.preventDefault();

		if ($('#item-options2 tr').size() > 0) {
			$(this).closest('tr').remove();
		}
		updateRowIndex();
	});



    //접근성 이미지 설명 로우 추가 혹은 삭제
	function updateRowIndex() {
    	let trList = $('#item-options2 tr');
		let size = trList.size();

		for(i = 0; i < size; i++) {
			$($($(trList[i]).children()[0]).children()[0]).attr('name', 'prjNoticeImageExplains[' + i + '].imgSeq');		// 순번
			$($($(trList[i]).children()[0]).children()[0]).attr('id', 'prjNoticeImageExplains[' + i + '].imgSeq');
			$($($(trList[i]).children()[0]).children()[0]).val(i+1);
			$($($(trList[i]).children()[1]).children()[0]).attr('name', 'prjNoticeImageExplains[' + i + '].imgDesc');		// 이미지 설명
			$($($(trList[i]).children()[1]).children()[0]).attr('id', 'prjNoticeImageExplains[' + i + '].imgDesc');
			$($($(trList[i]).children()[2]).children()[0]).attr('data-index', i);									// 삭제버튼
		}
	}

    //버튼 스크롤에 따라 반응형으로 작동
    function scrollButtons() {
        $(window).scroll(function () {
            var st = $(window).scrollTop();
            var scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();

            if (scrollBottom < 170) {
                $('#buttons').removeClass('fixed_button');
            } else {
                $('#buttons').addClass('fixed_button');
            }
        });
    }

        // 파일 삭제
    function deletePrjNoticeFile(prjNoticeId, fileSeq) {
        let message = '파일이 실제로 삭제됩니다.(복구불가)\n삭제하시겠습니까?';
		if (confirm(message)) {
	        let param = {'prjId' : <c:out value="${prjNotice.prjId}"></c:out>, 'prjNoticeId': prjNoticeId, 'fileSeq' : fileSeq};
	        $.post('${fn:escapeXml(requestContext.managerUri)}/designated-donation/list/notice/delete-file', param, function(response){
	            Common.responseHandler(response);
	            if (response.data) {
	            	if (response.data.result == "S") {
	            		alert("삭제되었습니다.");
	                    $('#file_' + fileSeq).remove();
	            	} else if (response.data.result == "F") {
	            		alert(response.data.msg);
	            	}
	            }
	        });
		}

    }


</script>
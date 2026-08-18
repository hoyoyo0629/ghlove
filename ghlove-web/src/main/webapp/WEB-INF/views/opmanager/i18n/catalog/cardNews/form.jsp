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
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>


<form:form modelAttribute="cardNews" id="cardNews" method="post" action='${fn:escapeXml(requestContext.managerUri)}/catalog/card-news/form/save'>
	<c:choose>
		<c:when test="${cardNews.cardNewsId > 0}">
	    	<h3 class="custom">카드뉴스 수정</h3>
		</c:when>
		<c:otherwise>
			<h3 class="custom">카드뉴스 등록</h3>
		</c:otherwise>
	</c:choose>
    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="카드뉴스">
                    <colgroup>
                        <col style="width: 150px" />
                        <col style="width: auto;" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>제목</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:input path="cardNewsSubject" maxlength="100" class="input_txt required _filter wd-500" title="제목"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>발간호</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
									<form:select path="catalogYear" class="wd-150 required" onChange="catalogYearChange(this.value)" title="발간년도">
										<form:option value= "0">-선택-</form:option>
										<c:forEach items="${regCatalogYearList}" var="regCatalogYear">
											<form:option value="${fn:escapeXml(regCatalogYear.catalogYear)}" selected="${fn:escapeXml(regCatalogYear.catalogYear) eq catalogYear ? 'selected' : ''}" label="${fn:escapeXml(regCatalogYear.catalogYear)}" />
										</c:forEach>
									</form:select>

									<form:select path="catalogNo" class="wd-150" title="발간호">
										<option value="0">-선택-</option>
									</form:select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>내용</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:textarea path="cardNewsCn" cols="30" rows="20" class="editor-content" style="width:99%;" title="${op:message('M00990')}" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="label">이미지 설명</td>
                            <td>
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
					                        <c:forEach items="${cardNews.cardNewsImageExplains}" var="cardNewsImageExplain" varStatus="loopStatus">
					                            <tr>
					                                <td>
					                                    <input type="text" id="cardNewsImageExplains[${fn:escapeXml(loopStatus.index)}].imgSeq" name="cardNewsImageExplains[${fn:escapeXml(loopStatus.index)}].imgSeq" maxlength="200" class="이미지 설명 순서" title="이미지 설명 순서" value="${fn:escapeXml(loopStatus.index) + 1}"  readonly/>
					                                </td>
					                                <td>
					                                    <textarea id="cardNewsImageExplains[${fn:escapeXml(loopStatus.index)}].imgDesc" name="cardNewsImageExplains[${fn:escapeXml(loopStatus.index)}].imgDesc">${fn:escapeXml(cardNewsImageExplain.imgDesc)}</textarea>
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
                            </td>
                        </tr>
                    </tbody>

                </table>
            </div>
		</div>

        <!-- 전송 -->
        <div id="buttons" class="btn_all btn_right">
            <div class="flex_box gap-08">
                    <button type="submit" class="btn btn-dark-gray btn-mini">${cardNews.cardNewsId == 0 || cardNews.cardNewsId == '' ? '등록' : '수정'}</button>
                    <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/catalog/card-news/list')" class="btn btn-default">${op:message('M00480')}</a>
            </div>
        </div>
    </div>
	<input id="cardNewsId" name="cardNewsId" type="hidden" value="${fn:escapeXml(cardNews.cardNewsId)}" />
</form:form>
<module:smarteditorInit />
<module:smarteditor id="cardNewsCn" />

<script type="text/javascript">


$(function() {

	window.onpageshow = function(event) {
	    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
	        // 에디트 내용을 decode하여 표시
	        $('#cardNewsCn')[0].value = decodeURIComponent($('#cardNewsCn')[0].value);
	    }
	    catalogYearChange(<c:out value="${cardNews.catalogYear}"/>, <c:out value="${cardNews.catalogNo}"/>);
	}

    // scrollButtons();
    // 폼체크
    $("#cardNews").validator({
        'submitHandler' : function() {
        	if (!$("#catalogYear").val() || $("#catalogYear").val() == 0) {
        		alert("발간년도를 선택해주세요.");
        		$("#catalogYear").focus();
        		return false;
        	}

        	if (!$("#catalogNo").val() || $("#catalogNo").val() == 0) {
        		alert("발간호를 선택해주세요.");
        		$("#catalogNo").focus();
        		return false;
        	}

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

            Common.getEditorContent("cardNewsCn"); //넣는 로직 전에 넣으면 alt도 저장 안됨

            if ($('#cardNewsCn').val().toLowerCase() == '<p>&nbsp;</p>' || $('#cardNewsCn').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

            //smarteditor값 따로 encode하여 전송하여 서버에서 decode
            $('#cardNewsCn')[0].value = encodeURIComponent($('#cardNewsCn')[0].value);
            //return false;
        }
    });

}); //function() end

	// 이미지 설명 로우 추가
	$('.add-item-option2').on('click', function() {
	    var newHtml = '<tr>' +
	    '<td>' +
	    '<input type="text" id="prjImageExplain[0].imgSeq" name="cardNewsImageExplains[0].imgSeq" class="" title="이미지 설명 순서" readonly/>' +
	    '</td>' +
	    '<td>' +
	    '<textarea id="prjImageExplain[0].imgDesc" name="cardNewsImageExplains[0].imgDesc"   title="이미지 설명" ></textarea>' +
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
			$($($(trList[i]).children()[0]).children()[0]).attr('name', 'cardNewsImageExplains[' + i + '].imgSeq');		// 순번
			$($($(trList[i]).children()[0]).children()[0]).attr('id', 'cardNewsImageExplains[' + i + '].imgSeq');
			$($($(trList[i]).children()[0]).children()[0]).val(i+1);
			$($($(trList[i]).children()[1]).children()[0]).attr('name', 'cardNewsImageExplains[' + i + '].imgDesc');		// 이미지 설명
			$($($(trList[i]).children()[1]).children()[0]).attr('id', 'cardNewsImageExplains[' + i + '].imgDesc');
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


    // 발간년도 변경
	function catalogYearChange(catalogYear, catalogNo) {
		$("#catalogNo option").remove();
		if ($("#catalogYear").val() != "") {
			$.post(url("/opmanager/catalog/locgovFavItem/catalogMngNoList"), {'code' : catalogYear}, function(response) {
				if (response.isSuccess) {
					let options = '<option value="0">-선택-</option>';
					for (var i = 0; i < response.data.length; i++) {
						if (catalogNo == response.data[i].CATALOG_NO) {
							options += '<option value="' + response.data[i].CATALOG_NO + '" selected="selected">' + response.data[i].CATALOG_NO + '</option>';
						} else {
							options += '<option value="' + response.data[i].CATALOG_NO + '">' + response.data[i].CATALOG_NO + '</option>';
						}
			        }
		            $('#catalogNo').append(options);
	                /* $("#catalogYear").val('${fn:escapeXml(cardNews.catalogYear)}').prop("selected", true);
	                $("#catalogNo").val('${fn:escapeXml(cardNews.catalogNo)}').prop("selected", true); */
				} else {
					alert("문제가 발생했습니다.");
					location.href = "${fn:escapeXml(requestContext.managerUri)}/catalog/card-news/list";
				}
		    });
		} else {
	        $('#catalogNo').append('<option value="0">-선택-</option>');
		}
	}

        // 파일 삭제
    /* function deletePrjNoticeFile(prjNoticeId, fileSeq) {
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
    } */


</script>
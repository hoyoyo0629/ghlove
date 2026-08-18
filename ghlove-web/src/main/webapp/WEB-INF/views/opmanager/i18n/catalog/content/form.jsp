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

<form:form modelAttribute="catalogContent" id="catalogContent" method="post" enctype="multipart/form-data" action='${fn:escapeXml(requestContext.managerUri)}/catalog/content/form/save'>
    <h3 class="custom">컨텐츠 등록</h3>
    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="컨텐츠 등록">
                    <colgroup>
                        <col style="width: 120px" />
                        <col style="width: auto;" />
                    </colgroup>
                    <tbody>
                    	<tr>
							<td class="label"><span class="required_mark">*</span>제목</td>
							<td>
								<div>
									<form:input path="catalogContentSubject" title="제목" style="width: 100%;" class="input_txt" type="text" value="${fn:escapeXml(catalogContent.catalogContentSubject)}"/><!-- 검색어 -->
								</div>
							</td>
						</tr>
                        <tr>
							<td class="label"><span class="required_mark">*</span>공개여부</td>
							<td>
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="displayYn" value="Y" label="공개" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="displayYn" value="N" label="비공개"/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>발간호</td>
							<td>
								<div>
									<form:select path="catalogYear" title="구분" class="wd-150">
										<form:option value="0">-${op:message('M00039')}-</form:option>
										<c:forEach items="${catalogYearList}" var="yyyy">
										    <form:option value="${fn:escapeXml(yyyy.catalogYear)}" selected="${fn:escapeXml(catalogContent.catalogYear) eq fn:escapeXml(yyyy.catalogYear) ? 'selected' : ''}" label="${fn:escapeXml(yyyy.catalogYear)}" />
										</c:forEach>
						            </form:select>년
						            <form:select path="catalogNo" title="구분" class="wd-150">
						            	<form:option value="0">-${op:message('M00039')}-</form:option>
										<c:forEach items="${catalogYearList}" var="yyyy">
										    <form:option value="${fn:escapeXml(yyyy.catalogNo)}" selected="${fn:escapeXml(catalogContent.catalogNo) eq fn:escapeXml(yyyy.catalogNo) ? 'selected' : ''}" label="${fn:escapeXml(yyyy.catalogNo)}" />
										</c:forEach>
						            </form:select>호
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>구분</td>
							<td>
	                       		<div class="flex_box gap-08">
		                            <form:select path="contentType" class="wd-150">
					                    <form:option value="">-${op:message('M00039')}-</form:option>
					                    <c:forEach items="${contentTypeList}" var="contentType">
					                        <form:option value="${fn:escapeXml(contentType.id)}" label="${fn:escapeXml(contentType.label)}" />
					                    </c:forEach>
					                </form:select>
		                            <form:select path="contentSubType" class="wd-150">
		                            	<form:option value="">-${op:message('M00039')}-</form:option>
					                    <c:forEach items="${contentSubTypeList}" var="contentSubType">
					                        <form:option value="${fn:escapeXml(contentSubType.id)}" label="${fn:escapeXml(contentSubType.label)}" />
					                    </c:forEach>
					                </form:select>
	                            </div>
							</td>
						</tr>
						<tr>
                            <td class="label"><span class="required_mark">*</span>지자체</td>
							<td>
	                       		<div class="flex_box gap-08">
		                            <form:select path="upperLocgovCode" class="wd-150">
					                    <form:option value="">-${op:message('M00039')}-</form:option>
					                    <c:forEach items="${wdr}" var="wdr">
					                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
					                    </c:forEach>
					                </form:select>
		                            <form:select path="locgovCode" class="wd-150">
					                    <option value="">-시,군,구-</option>
					                </form:select>
	                            </div>
							</td>
                        </tr>
                        <tr>
                        	<td class="label">핫소식(배너)여부</td>
                        	<td>
                        		<div class="input-form">
                        			<input id="bannerYn" name="bannerYn" type="checkbox" value="" <c:if test="${catalogContent.bannerYn eq 'Y'}">checked</c:if>>
                        		</div>
                        	</td>
                        </tr>
                        <tr>
                            <td class="label"><p><span class="required_mark">*</span>이미지 등록</p></td> <!-- 상세이미지 -->
                            <td>
                                <div class="flex_box item-center">

                                    <button type="button" id="add_detail_image_file" style="display:none" class="table_btn"><span>+ ${op:message('M00984')}</span></button> <!-- 이미지추가 -->
                                    <p class="text-info text-sm hidden">
                                        * 상품 이미지는 제한 없이 등록이 가능합니다.<br />
                                        * [파일선택] 버튼을 선택한 후 파일 선택 창에서 상품 이미지를 복수로 선택하거나 드레그 하여 선택 후 등록해 주십시오.<br /><br />
                                    </p>

                                        <input type="file" name="prjImageFiles[]" multiple="multiple" accept="image/png, image/jpeg, image/gif" class="full input_file" />


                                    <div id="multiple_files">

                                    </div>

                                    <ul id="prjImages" style="margin:0;">
										<li id="prj_image_id">
										    <p>
		                                        ${fn:escapeXml(catalogContent.thumbnailImgPath)}
		                                    </p>
										</li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>내용</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:textarea path="catalogContentCn" cols="30" rows="20" class="editor-content" style="width:99%;" title="${op:message('M00990')}" />
                                </div>
                            </td>
                        </tr>
                        <%-- <tr>
                            <td class="label"><span class="required_mark">*</span>내용</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:textarea path="catalogContentCn2" cols="30" rows="20" class="editor-content" style="width:99%;" title="${op:message('M00990')}" />
                                </div>
                            </td>
                        </tr> --%>
                        <%-- <tr>
                            <td class="label"><span class="required_mark">*</span>이미지 설명</td>
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
					                        <c:forEach items="${catalogContent.catalogContentImageExplain}" var="catalogContentImageExplain" varStatus="loopStatus">
					                            <tr>
					                                <td>
					                                    <input type="text" id="catalogContentImageExplain[${loopStatus.index}].imgSeq" name="catalogContentImageExplain[${loopStatus.index}].imgSeq" maxlength="200" class="이미지 설명 순서" title="이미지 설명 순서" value="${loopStatus.index + 1}"  readonly/>
					                                </td>
					                                <td>
					                                    <textarea id="catalogContentImageExplain[${loopStatus.index}].imgDesc" name="catalogContentImageExplain[${loopStatus.index}].imgDesc">${catalogContentImageExplain.imgDesc}</textarea>
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
                        </tr> --%>
                    </tbody>
                </table>
            </div>
        </div>
		 <!-- 전송 -->
        <div id="buttons" class="btn_all btn_right">
            <div class="flex_box gap-08">
                    <button type="submit" class="btn btn-dark-gray btn-mini">${catalogContent.catalogContentId == 0 || catalogContent.catalogContentId == '' ? '등록' : '수정'}</button>
                    <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/catalog/content/list')" class="btn btn-default">${op:message('M00480')}</a>
            </div>
        </div>
    </div>
    <c:if test="${catalogContent.catalogContentId != 0 && catalogContent.catalogContentId != ''}">
    	<input id="catalogContentId" name="catalogContentId" type="hidden" value="${fn:escapeXml(catalogContent.catalogContentId)}" />
    </c:if>
</form:form>
<module:smarteditorInit />
<module:smarteditor id="catalogContentCn" />
<%-- <module:smarteditor id="catalogContentCn2" /> --%>

<script type="text/javascript">
    // 숫자 컴마.
	Common.addNumberComma();

$(function() {

	window.onpageshow = function(event) {
	    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
	        // 에디트 내용을 decode하여 표시
	        $('#catalogContentCn')[0].value = decodeURIComponent($('#catalogContentCn')[0].value);
	    }
	}

	wdrChange($("#upperLocgovCode").val());
    scrollButtons();
 // 폼체크
    $("#catalogContent").validator({
        'submitHandler' : function() {
        	if ($("#catalogContentSubject").val() == "" || $("#catalogContentSubject").val() == null) {
        		alert("제목을 입력해주세요.");
        		$("#catalogContentSubject").focus();
        		return false;
        	}

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

        	if ($("#contentType").val() == "" || $("#contentType").val() == null) {
        		alert("구분을 선택해주세요.");
        		$("#contentType").focus();
        		return false;
        	}

        	if ($("#contentSubType").val() == "" || $("#contentSubType").val() == null) {
        		alert("세부구분을 선택해주세요.");
        		$("#contentSubType").focus();
        		return false;
        	}

        	if ($("#upperLocgovCode").val() == "" || $("#upperLocgovCode").val() == null) {
        		alert("지자체를 선택해주세요.");
        		$("#upperLocgovCode").focus();
        		return false;
        	}

        	if ($("#locgovCode").val() == "" || $("#locgovCode").val() == null) {
        		alert("시,군,구를 선택해주세요.");
        		$("#locgovCode").focus();
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

            /* if (optionSize == 0 && imageSize > 0) {
                alert("시각 장애인을 위한 이미지 설명을 입력 부탁드립니다.");
                return false;
            } */

            /* 접근성 이미지 설명 이미지와 갯수 매칭 유효성 검사 */
            /* if (optionSize != imageSize) {
                alert('상세설명에 올리신 사진의 갯수와 \n이미지 설명의 갯수를 동일하게 맞추어 주셔야 합니다. \n현재 작성하신 이미지 설명 갯수: '+optionSize+' , 상세설명에 올리신 이미지 갯수: '+imageSize+' ');
                return false;
            } */

            //접근성 이미지 설명 이미지 alt에 입력
            for (let i = 0; i < imageSize; i++) {
                let img = images[i];
                img.setAttribute("alt", $($($(options[i]).children()[1]).children()[0]).val());
            }

            Common.getEditorContent("catalogContentCn"); //넣는 로직 전에 넣으면 alt도 저장 안됨

            if ($('#catalogContentCn').val().toLowerCase() == '<p>&nbsp;</p>' || $('#catalogContentCn').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

            if($("#catalogContentCn").val().match("<img")){
            	alert("내용란에 이미지파일은 넣을 수 없습니다.");
            	return false;
            }

            //smarteditor값 따로 encode하여 전송하여 서버에서 decode
            $('#catalogContentCn')[0].value = encodeURIComponent($('#catalogContentCn')[0].value);
            //return false;

            if (document.getElementById("bannerYn").checked) {
				$("#bannerYn").val("Y");
			} else {
				$("#bannerYn").val("N");
			}
        }
    });

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

	$("select[name='upperLocgovCode']").change(function() {
        searchSigungu(document.getElementById("upperLocgovCode"));
	});

	$("#catalogYear").change(function() {
		searchCatalogMngNoList($("#catalogYear").val());
	});

	$("#contentType").change(function() {
		searchContentSubTypeList($("#contentType").val());
	});

	// 스마트 에디터 사진, 동영상 버튼 숨기기
	findAttchArea(0);
}); //function() end

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

	//시군구 목록 조회
	function searchSigungu(sidoElement) {
		let sidoValue = sidoElement.value;
		$("#locgovCode option").remove();
	    $('#locgovCode').append('<option value="">-시,군,구-</option>');
	    Common.loading.show();
	    if (sidoValue) {
	        $.post(
	        		url("/common/getLocgovCode")
	        		, {'code' : sidoValue}
	        		, function(response) {
	        			Common.responseHandler(response, function(){
	        				let data = response.data;
				            for (var i = 0; i < data.length; i++) {
				                var options = '<option value="' + data[i].LOCGOV_CODE + '">' + data[i].LOCGOV_NM + '</option>';
				                $('#locgovCode').append(options);
				            }

				         	// 조회 된 값 유지
				            if ("${fn:escapeXml(catalogContent.locgovCode)}" != "") {
				            	$("#locgovCode").val("${fn:escapeXml(catalogContent.locgovCode)}").prop("selected", true);

				            	if (!$("#locgovCode").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
				            		$("#locgovCode option:eq(0)").prop("selected", true);
				            	}
				            }
	        			});
		            }
	        )
	        .always(function () {
	        	Common.loading.hide();
	        });
	    } else {
	    	Common.loading.hide();
	    }
	}

	//발간호 조회
	function searchCatalogMngNoList(year) {
		$("#catalogNo option").remove();
	    $('#catalogNo').append('<option value="">-호 선택-</option>');
	    Common.loading.show();
	    if (year) {
	        $.post(
	        		url("/opmanager/catalog/locgovFavItem/catalogMngNoList")
	        		, {'code' : year}
	        		, function(response) {
	        			Common.responseHandler(response, function(){
	        				let data = response.data;
				            for (var i = 0; i < data.length; i++) {
				                var options = '<option value="' + data[i].CATALOG_NO + '">' + data[i].CATALOG_NO + '</option>';
				                $('#catalogNo').append(options);
				            }

				         	// 조회 된 값 유지
// 				            if ("${fn:escapeXml(catalogContentParam.locgovCode)}" != "") {
// 				            	$("#locgovCode").val("${fn:escapeXml(catalogContentParam.locgovCode)}").prop("selected", true);

// 				            	if (!$("#locgovCode").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
// 				            		$("#locgovCode option:eq(0)").prop("selected", true);
// 				            	}
// 				            }
	        			});
		            }
	        )
	        .always(function () {
	        	Common.loading.hide();
	        });
	    } else {
	    	Common.loading.hide();
	    }
	}

	//구분 세부 조회
	function searchContentSubTypeList(code) {
		$("#contentSubType option").remove();
	    $('#contentSubType').append('<option value="">-세부구분 선택-</option>');
	    Common.loading.show();
	    if (code) {
	        $.post(
	        		url("/opmanager/catalog/content/contentSubTypeList")
	        		, {'code' : code}
	        		, function(response) {
	        			Common.responseHandler(response, function(){
	        				let data = response.data;
				            for (var i = 0; i < data.length; i++) {
				                var options = '<option value="' + data[i].id + '">' + data[i].label + '</option>';
				                $('#contentSubType').append(options);
				            }

				         	// 조회 된 값 유지
// 				            if ("${fn:escapeXml(catalogContentParam.locgovCode)}" != "") {
// 				            	$("#locgovCode").val("${fn:escapeXml(catalogContentParam.locgovCode)}").prop("selected", true);

// 				            	if (!$("#locgovCode").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
// 				            		$("#locgovCode option:eq(0)").prop("selected", true);
// 				            	}
// 				            }
	        			});
		            }
	        )
	        .always(function () {
	        	Common.loading.hide();
	        });
	    } else {
	    	Common.loading.hide();
	    }
	}

	// 지자체 변경
	function wdrChange(value) {
    	//Common.loading.hide();
		$("#locgovCode option").remove();
		if ($("#upperLocgovCode").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				for (var i = 0; i < response.length; i++) {
					let options = "";
					if (response[i].LOCGOV_CODE == value) {
						options = '<option value="' + response[i].LOCGOV_CODE + '" selected="selected">' + response[i].LOCGOV_NM + '</option>';
		            } else {
		            	options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            }

		            $('#locgovCode').append(options);
		        }
                //지자체 관리자 값으로 selected
                <c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
	                $("#upperLocgovCode").val('${fn:escapeXml(catalogContent.upperLocgovCode)}').prop("selected", true);
	                $("#locgovCode").val('${fn:escapeXml(catalogContent.locgovCode)}').prop("selected", true);
                </c:if>
		    });
		} else {
	        $('#locgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	function findItem(dest) {

		<%-- var featuredType = '${featured.featuredType}';
		var conditionType = "";
		if (featuredType == '8' || featuredType == '9') {
			conditionType = "FIND_ITEM_POPUP_FOR_PLANNER";
		}

		Shop.findItem(dest, conditionType); --%>
		Shop.findItem(dest, "");
	}

   	//날짜 정합성 체크
    function dateValidate(vDate) {
    	var vValue = vDate;
    	var vValue_Num = vValue.replace(/[^0-9]/g, "");

    	var rxDatePattern = /^(\d{4})(\d{1,2})(\d{1,2})$/;
    	var dtArray = vValue_Num.match(rxDatePattern);

    	if (dtArray == null) {
    		return false;
    	}

    	dtYear = dtArray[1];
    	dtMonth = dtArray[2];
    	dtDay = dtArray[3];

    	if (dtMonth < 1 || dtMonth > 12) {
    		alert("유효하지 않은 날짜입니다. 다시 한 번 확인해주세요");
    		return false;
    	}
    	else if (dtDay < 1 || dtDay > 31) {
    		alert("유효하지 않은 날짜입니다. 다시 한 번 확인해주세요");
    		return false;
    	}
    	else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31) {
    		alert("유효하지 않은 날짜입니다. 다시 한 번 확인해주세요");
    		return false;
    	}
    	else if (dtMonth == 2) {
    		var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
    		if (dtDay > 29 || (dtDay == 29 && !isleap)) {
				alert("유효하지 않은 날짜입니다. 다시 한번 확인해주세요");
    			return false;
    		}
    	}

    	return true;
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

    function getCurrentKoreanDate() {
        const options = { timeZone: 'Asia/Seoul' };
        const koreanDate = new Date().toLocaleString('en-US', options);
        const [datePart, timePart] = koreanDate.split(', ');

        if (datePart && timePart) {
            const [month, day, year] = datePart.split('/');
            return `${fn:escapeXml(year)}${fn:escapeXml(month)}${fn:escapeXml(day)}`;
        }

        return null; // 오류 처리
    }

    function cancle() {
		location.replace('/opmanager/catalog/content/list');
	}

    // 이미지 삭제
    function deleteCatalogContentImageId (type, id) {
        var message = '이미지가 실제로 삭제됩니다.(복구불가)\n삭제하시겠습니까?';

        //이미지가 1개 이하이면 return false
        if($("[id^='prj_image_id_']").length < 2) {
            alert("이미지는 최소 1개 이상은 등록 되어야 합니다.");
            return false;
        }


		if (!confirm(message)) {
			return;
		}

        var param = {'prjImageId': id};
        $.post('${requestContext.managerUri}/catalog/content/deletePic', param, function(response){
            Common.responseHandler(response);
            $('#prj_image_id_' + param.prjImageId).remove();
        });

    }

    // iframe 영역 셀렉트
    function findAttchArea(cnt) {
		let iframeContents = $('iframe').contents();
		let iframeContentsLength = iframeContents.length;
		if (iframeContentsLength > 0) {
			for(let i = 0 ; i < iframeContentsLength ; i++) {
				if (iframeContents[i].location == 'about:blank') {
					if (cnt < 10) {
						setTimeout(function () {
							findAttchArea(++cnt);
						}, 50);
						break;
					} else {
						console.log('findAttchArea fail :: ' + cnt);
						console.log(iframeContents[i]);
					}
				} else {
					hideAttchArea($(iframeContents[i]), 0);
				}
			}

		}
    }

    // iframe 내 스마트 에디터 사진, 동영상 버튼 숨기기
    function hideAttchArea(element, cnt) {
    	let btnElement = element.find('ul.se2_multy')
    	if (btnElement.length > 0) {
    		btnElement.hide();
		} else {
			if (cnt < 10) {
				setTimeout(function () {
					hideAttchArea(element, ++cnt);
				}, 50);
			} else {
				console.log('hideAttchArea fail :: ' + cnt);
				console.log(element);
			}
		}
    }

</script>
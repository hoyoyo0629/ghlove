<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<link rel="stylesheet" type="text/css" href="/content/opmanager/css/swiper.min.css"><!-- 추가 250804 -->
<script src="/content/modules/swiper.min.js"></script><!-- 추가 250804 -->

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<h3>${fn:escapeXml(lclgvHnrUserMng.lclgvCdNm)}&nbsp;<span>기부혜택증 기준 등록</span></h3>
<form:form modelAttribute="lclgvHnrUserMng" method="post" enctype="multipart/form-data" action='${fn:escapeXml(requestContext.managerUri)}/lclgvHnrUser/lclgvHnrUserMng/form/update'>

    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="컨텐츠 등록">
                    <colgroup>
						<col style="width:220px;">
						<col>
						<col style="width:220px;">
						<col>
						<col style="width:220px;">
						<col>
                    </colgroup>
                    <tbody>
                    	<tr>
							<td class="label"><span class="required_mark">*</span>지자체</td>
							<td colspan="2">
								<div>
									${fn:escapeXml(lclgvHnrUserMng.lclgvCdNm)}
								</div>
							</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
                        <tr>
							<td class="label"><span class="required_mark">*</span>사용여부</td>
							<td colspan="2">
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="useYn" value="Y" label="사용" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="useYn" value="N" label="미사용"/>
									</div>
								</div>
							</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<%-- <td class="label"><span class="required_mark">*</span>골드 등급 기부 금액</td>
							<td>
								<div>
									<form:input path="gldGrdDntnAmt" title="골드 등급 기부 금액" class="input_txt _number" style="width:80%; text-align: right;" type="text" value="${fn:escapeXml(lclgvHnrUserMng.gldGrdDntnAmt)}"/>
									원 이상
								</div>
							</td>
							<td class="label"><span class="required_mark">*</span>실버 등급 기부 금액</td>
							<td>
								<div>
									<form:input path="slvrGrdDntnAmt" title="실버 등급 기부 금액" class="input_txt _number" style="width:80%; text-align: right;" type="text" value="${fn:escapeXml(lclgvHnrUserMng.slvrGrdDntnAmt)}"/>
									원 이상
								</div>
							</td> --%>
							<td class="label"><span class="required_mark">*</span>발급기준금액</td>
							<td colspan="5">
								<div>
									<form:input path="brnzGrdDntnAmt" title="브론즈 등급 기부 금액" class="input_txt _number" style="text-align: right; width:8%;" type="text" value="${fn:escapeXml(lclgvHnrUserMng.brnzGrdDntnAmt)}"/>
									원 이상
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">
							<span class="required_mark">*</span>
							기부혜택증 발급 기준
							<img id="hnrUserSlctnSeGuideImg" width="20" height="20" src="/content/images/btn/btn_qna2.png" onclick="javascript:popupHnrGuide();" /></td>
							<td colspan="5">
								<c:forEach items="${hnrUserSlctnSeCdList}" var="hnrUserSlctnSeCdList">
									<div class="input-form">
										<form:radiobutton path="hnrUserSlctnSeCd" value="${fn:escapeXml(hnrUserSlctnSeCdList.id)}" label="&nbsp;(${fn:escapeXml(hnrUserSlctnSeCdList.label)})" />
										<!-- onchange="javascript:changeSeCd(this.value);" -->
										&nbsp;
										${fn:escapeXml(hnrUserSlctnSeCdList.detail)}
									</div>
								</c:forEach>
								<div id="hnrUserSlctnSeImg_popup" style="display:none; position:fixed; top:15%; left:50%; transform:translateX(-50%); background:#fff; border:1px solid #ccc; padding:10px; z-index:999;">
									<img id="hnrUserSlctnSeImg_popupImg" src="/content/images/honor/honor_rule4.png" style="max-width:500px;">
									<br>
									<a href="javascript:closePopUp();" class="btn btn-default"><span><c:out value="${op:message('M00569')}"/></span></a> <!-- 닫기 -->
									<!-- <button id="closePopUp" onclick="javascript:closePopUp();">닫기</button> -->
								</div>
								<!-- <div style="text-align: center;">
									<img id="hnrUserSlctnSeImg" src="" />
								</div> -->
								<!-- <span style="margin-left: 20px;" id="hnrUserSlctnSeCdDetail">asb</span> -->
							</td>
						</tr>
						<tr>
							<td class="label"><span class=""></span>기부혜택증 명칭</td>
							<td colspan="5">
								<div>
									<form:input path="hnrUserStngTtl" title="기부혜택증 명칭" class="input_txt" style="width:27%;" type="text" value="${fn:escapeXml(lclgvHnrUserMng.hnrUserStngTtl)}"/> (권장길이 : 8자)
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class=""></span>기부혜택증 발급자 혜택</td>
							<td colspan="5">
								</br>
								<p class="text-info text-sm" style="text-align:right;">동영상 사용은 권고하지 않습니다.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
								<div class="item_list mt30 mb30">
					            	<form:textarea path="hnrUserRwrd" cols="30" rows="20" class="editor-content" style="width:99%;" title="기부혜택증 발급자 혜택"/>
						        </div>
							</td>
						</tr>

                        <tr><td colspan="6">
                            <div class="item-option-table2">
                                <div>
                                <p class="text-info text-sm">시작장애인을 위한 이미지 설명을 입력해주세요.<br>
                                ※이미지 등록 수와 이미지 설명 항목 수가 반드시 일치해야 함
                                </p>
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
                                            <c:forEach items="${lclgvHnrUserMngParam.prjImageExplain}" var="prjImageExplain" varStatus="loopStatus">
                                                <tr>
                                                    <td>
                                                        <input type="text" id="prjImageExplain[${fn:escapeXml(loopStatus.index)}].prjsIndexes" name="prjImageExplain[${fn:escapeXml(loopStatus.index)}].prjsIndexes" maxlength="200" class="이미지 설명 순서" title="이미지 설명 순서" value="${fn:escapeXml(loopStatus.index) + 1}"  readonly/>
                                                    </td>
                                                    <td>
                                                        <textarea id="prjImageExplain[${fn:escapeXml(loopStatus.index)}].prjsValues" name="prjImageExplain[${fn:escapeXml(loopStatus.index)}].prjsValues">${fn:escapeXml(prjImageExplain.prjsValues)}</textarea>
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
                            </td></tr>
						<tr>
               			<tr>
							<td class="label" rowspan="2">기부혜택증 메인 이미지</td>
							<td colspan="1">
								<div style="display: flex; align-items: center;">
									<div style="min-width:20%; text-align: center;">
										권장 사이즈 : 720 X 500<br>
										<input id="prjImages" type="file" name="prjImageFiles[]" multiple="multiple" accept="image/png, image/jpeg, image/gif" class="full input_file" title="${op:message('M01699')}"
							                    		onchange="javascript:changePcImgFile(event);" style="margin:0 auto; width: 170px;" accept="image/*">
									</div>
								</div>
							</td>
							<td colspan="2">
								<div>
									<div class="flex_box gap-08 item-center" style="margin-bottom: 10px;">
										<div style="min-width: 100px;text-align: center;">
											업로드할 이미지
										</div>
										<img id="uploadPcImg" src="/content/images/common/no-image-gray.gif" style="width : auto; height: 110px;" />
									</div>
									<div class="flex_box gap-08 item-center" style="width:60%;">
										<div style="min-width: 100px;text-align: center;">
											업로드된 이미지<br>
											<button id="delPcItemImg" onclick="javascript:deleteItemFile('PC');">삭제하기</button>
										</div>
										<%-- <img id="uploadedPcImg" src="/opmanager/fileDownload/downloadByProgramData?programName=HNR_USER_MNG
										&programId=${fn:escapeXml(lclgvHnrUserMng.lclgvCd)}&fileName=${fn:escapeXml(lclgvHnrUserMng.rprsImgNm)}&orgFileName=${fn:escapeXml(lclgvHnrUserMng.rprsImgNm)}" style="width : auto; height: 110px;"
											onerror="this.src='/content/images/common/no-image-gray.gif';" /> --%>
										<img id="uploadedPcImg" src="/upload/lclgvHnrUserMng/${fn:escapeXml(lclgvHnrUserMng.lclgvCd)}
											/${fn:escapeXml(lclgvHnrUserMng.rprsImgNm)}" style="width : auto; height: 110px;"
											onerror="this.src='/content/images/common/no-image-gray.gif';" />
									</div>
								</div>
							</td>
							<td colspan="2">
								<div class="gap-08 item-center" style="margin-bottom: 10px;">
									<div style="min-width: 100px; height: 50px; text-align: center; vertical-align: middle; ">
										<p class="text-info text-sm">
											기본 이미지<br>
										</p>
									</div>
									<div style="text-align: center;">
										<img id="basicImg" src="/content/images/honor/default_honor_img.png" style="height: 110px;"
											onerror="this.src='/content/images/common/no-image-gray.gif';" />
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
        		<!-- <span class="red" style="margin: auto 10px;font-size: 15px;">* 현재 시범적용중인 제주도만 사용가능합니다.</span> -->
				<button type="button" id="submit" class="btn btn-dark-gray btn-mini modal-open-btn" data-target="modal-benefit" onclick="modalBenefit()">미리보기</button>
				<button type="submit" id="submit" class="btn btn-dark-gray btn-mini">수정</button>
				<c:if test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
					<a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/lclgvHnrUser/lclgvHnrUserMng/list')" class="btn btn-default">${op:message('M00480')}</a>
				</c:if>

            </div>
        </div>
    </div>
    <form:hidden path="lclgvCd" value="${fn:escapeXml(lclgvHnrUserMng.lclgvCd)}" />
</form:form>
<module:smarteditorInit />
<module:smarteditor id="hnrUserRwrd" />
<%-- <module:smarteditorInit />
<module:smarteditor id="lclgvHnrUserMngCn" /> --%>

 <div class="modalW" id="modal-benefit">
		<div class="modalW__content">
			<div class="honor">
				<div class="honor_card_area">
					<!-- swiper -->
					<div class="swiper-button-prev"></div>
					<div ref="idCard" class="slideArea swiper">
						<div class="swiper-wrapper" id="swiper-wrapper">

							<div class="swiper-slide">
								<div class="lineBox">
									<div class="honor_header">
										<div class="subTitle">
											<span><img alt="" src="/content/opmanager/images/honors/cli-logo-icon.png" />기부혜택증</span>
										</div>
										<div class="honor_time">
											<div class="countdownLine"></div>
											<div class="countdownBox">
												<div class="countdownTimer">
													<div style="text-align: right">현재시각</div>
													<span ref="countdown" class="countdownTime" id="countdownTime"></span>
												</div>
											</div>
										</div>
									</div>
									<h5 class="lineBoxTit" id="preview_hnrUserStngTtl"></h5>
									<div class="qrArea">
										<img src="/content/opmanager/images/honors/default_honor_img.png" id="preview_prjImages" alt="" />
									</div>
									<div class="userInfo">
										<div class="name" id="preview_userName">강성호</div>
										<ul>
											<li class="lclgvInfo" id=preview_lclgvCdNm></li>
										</ul>
										<div class="honor_date">
											<div>
												<div class="verticalLine"></div>
												<ul>
													<li>
														<div style="text-align: center">
															<span class="miniTit">발급일</span>
															<span class="userTxt" id="issueDate"></span>
														</div>
													</li>
													<li>
														<div style="text-align: center">
															<span class="miniTit">기&nbsp;&nbsp;&nbsp;&nbsp;간</span>
															<span class="userTxt" id="validDate"></span>
														</div>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</div>
								<div class="reloadBtm">
									<a class="modal-open-btn" data-target="op-alert" tabindex="0">혜택보기</a>
								</div>
							</div>

						</div>
					</div>
					<div class="swiper-button-next"></div>
					<!-- // swiper -->
				</div>
			</div>
			<button type="button" class="modalW__close-btn modalW__close"><span class="sr-only">닫기</span></button>
        </div>
    </div>

<div class="modalW" id="op-alert">
		<div class="modalW__content">
            <div class="modalW__header">
                <h2 class="modalW__title" id="preview_benefitTitle">서울특별시 광진구 혜택</h2>
            </div>
            <div class="modalW__body">
                <div class="pop_txt" id="preview_benefitContent">
                    <!-- 내용 -->
                   <p>광진 기부 혜택증&nbsp;</p>
                   <!-- //내용 -->
               </div>
           </div>
		<div class="modalW__footer">
               <div class="btn-group">
				<button type="button" data-dismiss="modal" class="btn btn_lg btn_primary modalW__close">확인</button>
		    </div>
           </div>
		<button type="button" class="modalW__close-btn modalW__close"><span class="sr-only">닫기</span></button>
	</div>
</div>

<script type="text/javascript">
    // 숫자 컴마.
	Common.addNumberComma();

    //기부혜택증 대표이미지(미리보기용)
    var rprsImgNm = "${lclgvHnrUserMng.rprsImgNm}"

$(function() {
	window.onpageshow = function(event) {
	    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
	        // 에디트 내용을 decode하여 표시
	        $('#hnrUserRwrd')[0].value = decodeURIComponent($('#hnrUserRwrd')[0].value);
	    }
	}

	$('#lclgvHnrUserMng').validator(function () {
		/*if ("${fn:escapeXml(lclgvHnrUserMng.lclgvCd)}" != "50000") {
			alert('"제주특별자치도 시범서비스"를 제외하고는 서비스 준비중입니다.');
	        event.preventDefault();
			return;
		}*/

		let hnrUserStngTtl = $('#hnrUserStngTtl').val();
		let length = hnrUserStngTtl.length;

		if (length > 18) {
	        alert("기부확인증 명칭은 18자까지 입력가능합니다.");
	        event.preventDefault();
	        return false;
		}

		/* if (($('#gldGrdDntnAmt').val() <= 0) || ($('#slvrGrdDntnAmt').val() <= 0)  || ($('#brnzGrdDntnAmt').val() <= 0)) {
	        alert("등급별 기부 금액은 0보다 커야합니다.");
	        event.preventDefault();
	        return false;
		} */

		let brnzGrdDntnAmt = parseInt($('#brnzGrdDntnAmt').val());

		if (isNaN(brnzGrdDntnAmt)) {
			brnzGrdDntnAmt = 0;
		}

		if (brnzGrdDntnAmt <= 0) {
	        alert("발급기준금액은 0보다 커야합니다.");
	        event.preventDefault();
	        return false;
		}

		if (!$('input[name="hnrUserSlctnSeCd"]:checked').val() || $('input[name="hnrUserSlctnSeCd"]:checked').val() == null) {
	        //alert("명예 사용자 선정 구분을 선택해 주세요.");
	        alert("기부확인증 발급 기준을 선택해 주세요.");
	        event.preventDefault();
			return;
		}

		/* if (((parseInt($('#gldGrdDntnAmt').val())) < parseInt($('#slvrGrdDntnAmt').val()))
		      || ((parseInt($('#slvrGrdDntnAmt').val())) < parseInt($('#brnzGrdDntnAmt').val()))) {
			alert("등급별 기부 금액을 다시 입력해 주세요. (골드 > 실버 > 브론즈)");
			event.preventDefault();
			return false;
		} */

		// 접근성 이미지 설명 입력란에 공백이 들어 올 시 해당 로우 제거하여 not null 데이터가 null로 들어가지 않도록 방지
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

		// 접근성 이미지 설명 이미지 alt에 입력
        let outerIframe = document.querySelectorAll("#editor_frame")[0];
        let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
        let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");
        let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
        let images = innerIframeDoc.querySelectorAll("img");

        let imageSize = images.length;

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

		Common.getEditorContent("hnrUserRwrd"); //넣는 로직 전에 넣으면 alt도 저장 안됨
		// smarteditor값 따로 encode하여 전송하여 서버에서 decode
        $('#hnrUserRwrd')[0].value = encodeURIComponent($('#hnrUserRwrd')[0].value);

		// 240920 지자체 명예 사용자 혜택 필수 제외
// 		if ($('#hnrUserRwrd').val().toLowerCase() == '<p>&nbsp;</p>' || $('#hnrUserRwrd').val() == '') {
// 			alert($.validator.messages['text'].format("${op:message('M00006')}"));
// 			event.preventDefault();
// 			return false;
// 		}

	});

	//changeSeCd("${fn:escapeXml(lclgvHnrUserMng.hnrUserSlctnSeCd)}");
});
	//function() end
    function cancle() {
		location.replace('/opmanager/lclgvHnrUser/lclgvHnrUserMng/list');
	}


	// 명예사용자 이미지 업로드
	function changePcImgFile(event) {
		let inputElement = document.getElementById('prjImages');
		let files = inputElement.files;
		let displayAddFile = document.getElementById('uploadPcImg');		// 첨부파일 목록 영역

		displayAddFile.setAttribute("src", '/content/images/common/no-image-gray.gif');

		if (files && files.length > 0) {
			let file = files[0];
			if (file.type && file.type.indexOf('image/') > -1) {
				setThumbnail(event, displayAddFile);
			} else {
				const dataTransfer = new DataTransfer();		// 폼 객체 내 파일 정보 수정시 처리용 객체
				inputElement.files = dataTransfer.files;
				alert("이미지 파일만 업로드 가능합니다.");
			}
		}
	}

	// 이미지 썸네일
	function setThumbnail(event, imgElement) {
		let reader = new FileReader();
		reader.onload = function(event) {
			imgElement.setAttribute("src", event.target.result);
		};
		reader.readAsDataURL(event.target.files[0]);
	}

	// 이미지 삭제
	function deleteItemFile() {
		$.post('/opmanager/lclgvHnrUser/lclgvHnrUserMng/form/deleteItemFile', {'lclgvCd': ${fn:escapeXml(lclgvHnrUserMng.lclgvCd)}}, function(response) {

			if (response > 0) {
			    alert("파일을 삭제했습니다.");
			    //대표이미지값 제거(미리보기용)
			    rprsImgNm = '';
			} else {
				alert("파일 삭제에 실패했습니다.");
			}
        });
		$("#uploadedPcImg").attr("src","/content/images/common/no-image-gray.gif");
		event.preventDefault();
	}

	//기부확인증 발급 선택
	function changeSeCd(hnrUserSlctnSeCd) {

		if ("BF_YR_1YR" == hnrUserSlctnSeCd){
			$("#hnrUserSlctnSeImg").attr('src', '/content/images/common/banner01.jpg');
		}else if ("NOW_CRTR_1YR_WTHN" == hnrUserSlctnSeCd){
			$("#hnrUserSlctnSeImg").attr('src', '/content/images/common/banner02.jpg');
		}else if ("JEJU_REQ_CRTR" == hnrUserSlctnSeCd){
			$("#hnrUserSlctnSeImg").attr('src', '/content/images/common/event_list.jpg');
		}else{
			$("#hnrUserSlctnSeImg").attr('src', '');
		}
	}

	//기부확인증 조건 이미지 가이드 팝업
	function popupHnrGuide(){
		$("#hnrUserSlctnSeImg_popup").show();
	}

	function closePopUp(){
		$("#hnrUserSlctnSeImg_popup").hide();
	}


	// 이미지 설명 로우 추가
	$('.add-item-option2').on('click', function() {

		let newHtml = '<tr>' +
	    '<td>' +
	    '<input type="text" id="prjImageExplain[0].prjsIndexes" name="prjImageExplain[0].prjsIndexes" class="" title="이미지 설명 순서" readonly/>' +
	    '</td>' +
	    '<td>' +
	    '<textarea id="prjImageExplain[0].prjsValues" name="prjImageExplain[0].prjsValues"   title="이미지 설명" ></textarea>' +
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
		} else {
		}
		updateRowIndex();
	});

    // 접근성 이미지 설명 로우 추가 혹은 삭제
	function updateRowIndex() {
		let trList = $('#item-options2 tr');
		let size = trList.size();

		for(i = 0; i < size; i++) {
			$($($(trList[i]).children()[0]).children()[0]).attr('name', 'prjImageExplain[' + i + '].prjsIndexes');		// 순번
			$($($(trList[i]).children()[0]).children()[0]).attr('id', 'prjImageExplain[' + i + '].prjsIndexes');
			$($($(trList[i]).children()[0]).children()[0]).val(i+1);
			$($($(trList[i]).children()[1]).children()[0]).attr('name', 'prjImageExplain[' + i + '].prjsValues');		// 이미지 설명
			$($($(trList[i]).children()[1]).children()[0]).attr('id', 'prjImageExplain[' + i + '].prjsValues');
			$($($(trList[i]).children()[2]).children()[0]).attr('data-index', i);										// 삭제버튼
		}
	}

    // 접근성 설명 이미지 alt로 update
    function updateImgAlt(endflag) {
        Common.getEditorContent("prjCn");
        //재귀 end 로직 추가
        let values = [...document.querySelectorAll("#item-options2 textarea[id='prjsValues']")].map(input => input.value);
        let outerIframe = document.getElementById("editor_frame");
        let timer;
        if (!outerIframe) {
            timer = setTimeout(updateImgAlt, 500);
            return;
        }

        let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
        let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");

        if (!innerIframe) {
            timer = setTimeout(updateImgAlt, 500);
            return;
        }

        let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
        let images = innerIframeDoc.querySelectorAll("img");

        if (images.length === 0) {
            timer = setTimeout(updateImgAlt, 500);
            return;
        }

        images.forEach((img, index) => {
            if (values[index]) {
                img.setAttribute("alt", values[index]);
            }
        });
        endflag = true;
        if (endflag) {
            clearTimeout(timer);
        }

    }


  //##############################모달 세팅###################################

  //모달열기
    document.querySelectorAll(".modal-open-btn").forEach(btn=>{
        btn.addEventListener("click",()=>{
            const modalId = btn.getAttribute("data-target");
            const modal = document.getElementById(modalId);
            if(modal){
                modal.classList.add("active");
                document.body.classList.add("scroll-no");
            }

            setModalData();


        });
    });
    //모달닫기
    document.querySelectorAll(".modalW__close").forEach(btn=>{
        btn.addEventListener("click",()=>{
            const modal = btn.closest(".modalW");
            let activeBtns = document.querySelectorAll(".modalW.active").length;
            modal.classList.remove("active");
			if(activeBtns <=1){
				document.body.classList.remove("scroll-no");
			}
        })
    });
    //모달 닫기 (배경 클릭 시)
    window.addEventListener("click",(e)=>{
        if(e.target.classList.contains("modalW")){
            let activeBtns = document.querySelectorAll(".modalW.active").length;
            e.target.classList.remove("active");
			if(activeBtns <=1){
				document.body.classList.remove("scroll-no");
			}
        }
    });

	const modalBenefit= ()=>{
		setTimeout(function () {
			new Swiper(".swiper", {
				// Optional parameters
				loop: false,
				slidesPerView: 1,
				// If we need pagination
				pagination: {
				el: ".swiper-pagination",
				},

				// Navigation arrows
				navigation: {
				nextEl: ".swiper-button-next",
				prevEl: ".swiper-button-prev",
				},

				// And if we need scrollbar
				scrollbar: {
				el: ".swiper-scrollbar",
				},
			});

		}, 100);
	}

	function setModalData(){
		//기부혜택증 명칭
		$("#preview_hnrUserStngTtl").text($("#hnrUserStngTtl").val())

		//사용자 이름
		$("#preview_userName").text($(".info > span" ).text())

		//지자체
		$("#preview_lclgvCdNm").text("${lclgvHnrUserMng.lclgvCdNm}")

		//발급기간
		var now = new Date();

		var year = now.getFullYear();
		var month = String(now.getMonth() + 1).padStart(2, '0');
		var date = String(now.getDate()).padStart(2, '0');

		$("#issueDate").text(year + '.' + month + '.' + date )
		$("#validDate").text((year+1) + '.' + month + '.' + date )

		//기부혜택증 이미지
		//업로드 할 이미지가 있는 경우
		if($("#prjImages")[0].files[0]){
			var imageFile = $("#prjImages")[0].files[0]
			var fileReader = new FileReader();
			fileReader.onload = function(e){
				$("#preview_prjImages").attr('src', e.target.result)
			}
			fileReader.readAsDataURL(imageFile);
		}
		//업로드 된 이미지가 있는 경우
		else if(rprsImgNm != ''){
			$("#preview_prjImages").attr('src',
					"/upload/lclgvHnrUserMng/${fn:escapeXml(lclgvHnrUserMng.lclgvCd)}/"+ rprsImgNm)
		}
		//이미지가 없는 경우 기본 이미지
		else{
			$("#preview_prjImages").attr('src', '/content/opmanager/images/honors/default_honor_img.png')
		}
		$("#preview_prjImages").attr('alt', "${lclgvHnrUserMng.lclgvCdNm}" + ' 기부혜택증 대표이미지' )

		//혜택보기
		$("#preview_benefitTitle").text("${lclgvHnrUserMng.lclgvCdNm}" + ' 혜택')

		//혜택보기-본문

		let outerIframe = document.querySelectorAll("#editor_frame")[0];
        let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
        let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");
        let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
        let images = innerIframeDoc.querySelectorAll("img");

		$("#preview_benefitContent").html(innerIframeDoc.body.innerHTML)
		$("#preview_benefitContent img").attr("style","width:100%")
	}

	function updateTime(){
		var now = new Date();

		var year = now.getFullYear();
		var month = String(now.getMonth() + 1).padStart(2, '0');
		var date = String(now.getDate()).padStart(2, '0');

		var hours = String(now.getHours()).padStart(2, '0');
		var minutes = String(now.getMinutes()).padStart(2, '0');
		var seconds = String(now.getSeconds()).padStart(2, '0');

		var formattedTime = year + '.' + month + '.' + date + ' ' + hours + ':' + minutes + ':' + seconds;

		$("#countdownTime").text(formattedTime);
	}

	$(document).ready(function(){
		updateTime();
		setInterval(updateTime, 1000);
	});


</script>
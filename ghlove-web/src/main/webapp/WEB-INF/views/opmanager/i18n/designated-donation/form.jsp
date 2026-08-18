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

<%-- <form:form modelAttribute="designatedDonation" id="${adminRole eq 'ROLE_ADMIN_5' || adminRole eq 'ROLE_ADMIN_6' ? 'prj' : 'impossible-form'}" method="post" enctype="multipart/form-data" onsubmit=''> --%>
<form:form modelAttribute="designatedDonation" id="prj" method="post" enctype="multipart/form-data" onsubmit=''>
    <h3 class="custom">특정사업에 기부하기 정보</h3>
    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="특정사업에 기부하기 정보">
                    <colgroup>
                        <col style="width: 120px" />
                        <col style="width: auto;" />
                        <col style="width: 120px" />
                        <col style="width: auto;" />
						<c:choose>
							<c:when test="${mode == 'edit'}">
	                        	<col style="width: 120px" />
		                        <col style="width: auto;" />
	                        </c:when>
                        </c:choose>
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>지자체</td>
                                <td>
                                    <div class="flex_box gap-08">
                                        <form:select path="upperLocgovCode" class="wd-150 required" onChange="wdrChange(this.value)" title="상위 지자체">
                                            <form:option value= "">-${op:message('M00039')}-</form:option>
	                                        <c:forEach items="${wdr}" var="wdr">
	                                                <!-- <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" /> -->
	                                            <form:option value="${fn:escapeXml(wdr.id)}" selected="${fn:escapeXml(wdr.id) eq fn:escapeXml(upperLocgovCode) ? 'selected' : ''}" label="${fn:escapeXml(wdr.label)}" />
                                            </c:forEach>
                                        </form:select>

                                        <form:select path="locgovCode" class="wd-150" title="지자체" onChange="getDsgncntrPartList(this.value)">
                                            <option value="${fn:escapeXml(locgovCode)}">-시,군,구-</option>
                                            <!-- <option value="">-시,군,구-</option> -->
                                        </form:select>

                                    </div>
                                </td>
                            <!-- <td colspan="3">
                                <div class="flex_box gap-08">
                                    <form:select path="upperLocgovCode" class="wd-150" onChange="wdrChange(this.value)">
                                        <form:option value= "">-${op:message('M00039')}-</form:option>
                                        <c:forEach items="${wdr}" var="wdr">
                                            <form:option value="${fn:escapeXml(wdr.id)}" selected="${fn:escapeXml(wdr.id) eq fn:escapeXml(upperLocgovCode) ? 'selected' : ''}" label="${fn:escapeXml(wdr.label)}" />
                                        </c:forEach>
                                    </form:select>
                                    <form:select path="locgovCode" class="wd-150">
                                        <option value="${fn:escapeXml(locgovCode)}">-시,군,구-</option>
                                    </form:select>
                                </div>
                            </td> -->
                            <td class="label"><span class="required_mark">*</span>사업구분</td>
							<c:choose>
								<c:when test="${mode == 'edit'}">
                            		<td colspan="3">
								</c:when>
								<c:otherwise>
                            		<td>
								</c:otherwise>
							</c:choose>
	                            <div class="flex_box gap-08">
	                                <form:select path="bsnsType" class="wd-400 required" title="사업구분" onChange="bsnsTypeChange(this.value)">
	                                    <form:option value="">-${op:message('M00431')}-</form:option>
	                                    <c:forEach items="${business_type}" var="bsnsType">
	                                        <form:option value="${fn:escapeXml(bsnsType.id)}" label="${fn:escapeXml(bsnsType.detail)}" />
	                                    </c:forEach>
	                                </form:select>
	                                <form:select path="bsnsSubType" class="wd-200 required" title="사업부문">
	                                    <form:option value="">-${op:message('M00431')}-</form:option>
	                                    <c:forEach items="${business_sub_type}" var="bsnsSubType">
	                                        <form:option value="${fn:escapeXml(bsnsSubType.id)}" label="${fn:escapeXml(bsnsSubType.detail)}" />
	                                    </c:forEach>
	                                </form:select>
	                            </div>
                            </td>
                        </tr>

                        <tr>
                            <td class="label"><span class="required_mark">*</span>기간</td>
                            <td>
                                <div>
                                    <span class="datepicker"><form:input path="prjStDt" pattern="[0-9]{8}" maxlength="8" class="datepicker required" title="기부 시작 기간"/><!-- 주문일자 시작일 --></span>
                                    <span class="wave">~</span>
                                    <span class="datepicker mr10"><form:input path="prjEdDt" pattern="[0-9]{8}" maxlength="8" class="datepicker required" title="기부 종료 기간"/><!-- 주문일자 종료일 --></span>
                                </div>
                            </td>
                            <td class="label"><span class="required_mark">*</span>목표금액(원)</td>
							<td>
                                <div class="flex_box gap-12 item-center">
                                    <form:input path="targetAmt" maxlength="12" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" class="input_txt required _filter wd-500" title="목표금액"/>
                                </div>
                            </td>
                            <c:choose>
                                <c:when test="${mode == 'edit'}">
                                    <td class="label">남은 일수</td>
                                    <td>
                                        <div class="flex_box gap-12 item-center" >
                                            <%-- <form:input path="leftDayStr" maxlength="50" class="input_txt required _filter wd-500" title="${op:message('M00018')}" /> --%>
                                            <c:out value="${designatedDonation.leftDayStr}"></c:out> 일
                                        </div>
                                    </td>
                                </c:when>
                            </c:choose>
                        </tr>

                        <tr>
                            <td class="label "><span class="required_mark">*</span>사업명</td>
                            <%-- <c:choose>
	                            <c:when test="${mode == 'edit'}">
                            		<td>
	                            </c:when>
	                            <c:otherwise>
                            		<td colspan="3">
	                            </c:otherwise>
                            </c:choose> --%>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:input path="prjSubject" maxlength="50" class="input_txt required _filter wd-500" title="특정사업에 기부하기 사업명"/>
                                </div>
                            </td>
                            <td class="label ">사업부서</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
	                                <form:select path="dsgncntrPartId" class="wd-500" title="사업부서">
	                                    <form:option value="0">-부서 미지정-</form:option>
	                                    <c:forEach items="${dsgncntrPartList}" var="dsgncntrPart">
	                                    	<c:choose>
	                                    		<c:when test="${dsgncntrPart.dsgncntrPartId == designatedDonation.dsgncntrPartId}">
	                                        		<form:option value="${dsgncntrPart.dsgncntrPartId}" label="${dsgncntrPart.dsgncntrPartName}" selected="selected"/>
	                                    		</c:when>
	                                    		<c:otherwise>
	                                    			<form:option value="${dsgncntrPart.dsgncntrPartId}" label="${dsgncntrPart.dsgncntrPartName}" />
	                                    		</c:otherwise>
	                                    	</c:choose>
	                                    </c:forEach>
	                                </form:select>
                                </div>
                            </td>
                            <c:choose>
                                <c:when test="${mode == 'edit'}">
                                    <td class="label">모금 된 금액</td>
                                    <td>
                                        <div class="flex_box gap-12 item-center">
                                            <%-- <form:input path="sumAmt" maxlength="50" class="input_txt required _filter wd-500" title=""/> --%>
                                            <c:out value="${op:numberFormat(designatedDonation.sumAmt)}"></c:out>원
                                        </div>
                                    </td>
                                </c:when>
                            </c:choose>
                        </tr>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>상태</td>
                            <td>
                                <div class="flex_box gap-12">
									<div class="input-form">
									   	<form:radiobutton path="prjStatus" value="2" label="진행중(승인)" />
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="9" label="종료" />
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="1" label="대기(승인 전)" />
									</div>
								</div>
                            </td>
                            <td class="label"><span class="required_mark">*</span>공개여부</td>
                            <%-- <c:choose>
                                <c:when test="${mode == 'edit'}">
                            		<td colspan="3">
                                </c:when>
                                <c:otherwise>
                            		<td>
                                </c:otherwise>
							</c:choose> --%>
							<td>
                                <div class="flex_box gap-12">
									<div class="input-form">
                                        <form:radiobutton path="displayFlag" value="Y" label="공개" />
									</div>
									<div class="input-form">
                                        <form:radiobutton path="displayFlag" value="N" label="비공개" />
									</div>
                                </div>
	                            <c:choose>
	                                <c:when test="${mode == 'edit'}">
	                                    <td class="label">달성률</td>
	                                    <td>
	                                        <div class="flex_box gap-12 item-center">
	                                            <%-- <form:input path="rateAmtStrWithPercent" maxlength="50" class="input_txt required _filter wd-500" title="${op:message('M00018')}"/> --%>
	                                            &nbsp;<c:out value="${designatedDonation.rateAmtStrWithPercent}"></c:out>
	                                            <!-- ${fn:escapeXml(RATE_AMT)} -->
	                                        </div>
	                                    </td>
	                                </c:when>
	                            </c:choose>
                            </td>
                        </tr>

                    </tbody>

                </table>
            </div>
        </div>
        <div class="item_list mt70">
            <h3>
                <span>특정사업에 기부하기 이미지 등록</span>
            </h3>

            <div class="board_write">

                <table class="board_write_table item_image_info">
                    <colgroup>
                        <col style="width: 220px;">
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><p><span class="required_mark">*</span>특정사업에 기부하기 이미지</p><p>(600px * 600px)</p></td> <!-- 상세이미지 -->
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

                                    <ul id="prjImages" class="sortable_item_image clear">
                                        <c:forEach items="${designatedDonation.prjImages}" var="prjImage" varStatus="i">
                                                <li id="prj_image_id_${fn:escapeXml(prjImage.dsgncntrPrjImageId)}">
                                                    <img src="/upload/prj/${fn:escapeXml(prjImage.prjId)}/${fn:escapeXml(prjImage.imageName)}" class="item_image size-100" alt="" />
                                                    <span class="ordering">${fn:escapeXml(i.count)}</span>
                                                    <a href="javascript:deleteDsgncntrPrjImageId('details', ${fn:escapeXml(prjImage.dsgncntrPrjImageId)});" class="delete_item_image"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
                                                </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="item_list mt70">
            <h3><span>상세 설명</span></h3> <!-- 상세 설명 -->
            <form:textarea path="prjCn" cols="30" rows="20" class="editor-content" style="width:99%;" title="${op:message('M00990')}" />
        </div>

        <div class="copy_button hidden">
            <button type="button" class="btn btn-gradient btn-sm add_item_point" onclick="copyContent('prjCn')"><span>&nbsp;⬇︎ COPY &nbsp;︎</span></button>
        </div>

            <!-- 모바일 -->
            <!-- <div class="item_list mt70 hidden">
                <h3><span>${op:message('M00990')} (${op:message('M00236')})</span></h3>

                <form:textarea path="prjCn" cols="30" rows="6" class="editor-content" style="width: 1085px" title="${op:message('M00990')} (${op:message('M00236')})" />
            </div> -->

        <!-- 이미지 설명 이용자 선택 버전 -->

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
                        <c:forEach items="${designatedDonation.prjImageExplain}" var="prjImageExplain" varStatus="loopStatus">
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

        <div class="item_list mt70">
            <h3><span>기타 사항</span></h3> <!-- 상세 설명 -->
        	<form:textarea path="contentEtc" style="height:200px;"/>
        </div>

        <!-- 전송 -->
        <div id="buttons" class="btn_all btn_right">
            <div class="flex_box gap-08">
            		<c:if test="${designatedDonation.prjId > 0
            						&& designatedDonation.prjStatus == '1'
            						&& (op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
            			<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:confirmProcess();">승인</button>
            		</c:if>
                    <button type="submit" class="btn btn-dark-gray btn-mini">${designatedDonation.prjId == 0 || designatedDonation.prjId == '' ? '등록' : '수정'}</button>
                    <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/designated-donation/list')" class="btn btn-default">${op:message('M00480')}</a>
            </div>
        </div>
    </div>
</form:form>
<module:smarteditorInit />
<module:smarteditor id="prjCn" />

<script type="text/javascript">

    // 지차체 변경여부 체크
    var changeYn = "N";
    // 숫자 컴마.
	Common.addNumberComma();
    $('.ordering').on('change', function() {

	});

$(function() {

	window.onpageshow = function(event) {
	    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
	        // 에디트 내용을 decode하여 표시
	        $('#prjCn')[0].value = decodeURIComponent($('#prjCn')[0].value);
	    }
	}

    wdrChange($("#upperLocgovCode").val());
    scrollButtons();
    bsnsTypeChange($("#bsnsType").val(), true);

    $('#impossible-form').submit(function() {
		alert('지자체 관리자/부관리자만 등록/수정이 가능합니다.');
		Common.loading.hide();
		return false;
	});

    // 폼체크
    $("#prj").validator({
        'submitHandler' : function() {

            //테스트용
            //return false;

            var selectFileCount = 0;
			$.each($(':file[class*="input_file"]'), function(){
				if ($(this).val() != '') {
					selectFileCount++;
				}
			});

            /*
             유효성 검사
            */

            //사업구분 유효성 체크
            let bsnsType = $('#bsnsType');
            if (bsnsType.val() == '' || bsnsType.val() == null){
                alert("사업 구분을 선택 해주세요.");
                return false;
            }

            //사업구분하위 유효성 체크
            let bsnsSubType = $('#bsnsSubType');
            if (bsnsSubType.val() == '' || bsnsSubType.val() == null){
                alert("사업 구분 하위항목을 선택 해주세요.");
                return false;
            }

            //종료일 > 시작일 체크 로직
            var strStartDate = $("#prjStDt").val();
            var strEndDate = $("#prjEdDt").val();

             if (strStartDate == null || strStartDate == '' || strEndDate == null || strEndDate == ''){
                 alert("시작일과 종료일을 입력해 주세요");
                 return false;
             }

            // // 현재 날짜 가져오기
            // const currentDate = new Date();

            // // 현재 날짜의 시간 부분을 00시 00분으로 설정
            // currentDate.setHours(0, 0, 0, 0);

            // // yymmdd 문자열을 Date 객체로 변환
            // const year = Number(strStartDate.slice(0, 4)); // 20을 추가하여 4자리 연도로 변환
            // const month = Number(strStartDate.slice(4, 6)); // 월은 0부터 시작하므로 1을 빼줌
            // const day = Number(strStartDate.slice(6, 8));
            // const targetDate = new Date(year, month-1, day);

            // // 현재 날짜와 비교
            // if (currentDate.getTime() >= targetDate.getTime()) {
            //     alert("시작 날짜는 현재 날짜보다 이후여야 합니다.");
            //     return false;
            // }


            if (strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
                var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
                var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

                if (startDate > endDate) {
                    alert("종료일이 시작일보다 빠릅니다.");
                    $("#prjEdDt").focus();
                    return false;
                }
                var searchChk = Common.searchDateMonth(startDate, endDate);
                if (!searchChk) return false;
            }

            // 날짜 정합성 체크
            if(!dateValidate(strStartDate)) {$("#prjStDt").focus();return false;};
            if(!dateValidate(strEndDate)) {$("#prjEdDt").focus();return false;};

            if (!is3Years(strStartDate, strEndDate)) {
            	$("#prjEdDt").focus();
            	alert("모금기간은 최대 3년까지 가능합니다.");
            	return false;
            }

            //목표 금액 공란 체크
            let targetAmt = $('#targetAmt').val();
            if (targetAmt == '0' || targetAmt == null) {
                alert("목표 금액을 입력해 주세요");
                return false;
            }

            let prjstat = $('input[name=prjStatus]:checked').val();

            if (prjstat == null) {
                alert("특정사업에 기부하기 사업 진행 상태를 선택해 주세요.");
                return false;
            }

            <c:if test="${adminRole eq 'ROLE_ADMIN_10'}">
            	if (prjstat == '2') {
                    if(!confirm("대기 상태로 저장됩니다. 저장하시겠습니까?")) {
                    	return false;
                    }
            	}
            </c:if>

            let displayflag = $('input[name=displayFlag]:checked').val();

            if (displayflag == null) {
                alert("특정사업에 기부하기 사업 공개 여부를 선택해 주세요.");
                return false;
            }


            // 이미지 등록 여부
            if (selectFileCount == 0  && $('input[name="prjImageFiles[]"]')[0].files.length == 0 && !($("[id^='prj_image_id_']").length > 0)) {
            //if (selectFileCount == 0  && $('input[name="detailImageFiles[]"]').length == 0) {
                alert('최소 1개의 이미지를 등록하셔야 합니다.');
                 $('input[name="prjImageFiles[]"]').focus();
                 return false;
             }

            // 이미지 파일 갯수 제한
			if ($('input[name="prjImageFiles[]"]')[0].files.length > 10) {
				alert('이미지 파일은 최대 10까지 등록할 수 있습니다.');
				$('input[name="prjImageFiles[]"]').focus();
				return false;
			}
			var maxSize = 500 * 1024;	//500KB

			for(var i=0; i < $('input[name="prjImageFiles[]"]')[0].files.length; i++) {
			    var ext = $('input[name="prjImageFiles[]"]')[0].files[i].name.split('.').pop().toLowerCase();
			  	if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	   alert('등록할 수 없는 파일확장자가 포함되어 있습니다.');
			  	   $('input[name="prjImageFiles[]"]').focus();
			  	   return false;
			 	}

				if($('input[name="prjImageFiles[]"]')[0].files[i].size > maxSize) {
					alert("이미지 용량은 파일당 500KB 이하로 등록 가능합니다.\n첨부된 파일의 용량을 확인해주세요!");
					$('input[name="prjImageFiles[]"]').focus();
					return false;
				}
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



            //접근성 이미지 설명 이미지 alt에 입력
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

            //Common.getEditorContent("prjCn",true); // ? alt저장 안됨
            Common.getEditorContent("prjCn"); //넣는 로직 전에 넣으면 alt도 저장 안됨

            if ($('#prjCn').val().toLowerCase() == '<p>&nbsp;</p>' || $('#prjCn').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}

            //smarteditor값 따로 encode하여 전송하여 서버에서 decode
            $('#prjCn')[0].value = encodeURIComponent($('#prjCn')[0].value);
        }
    }); // formcheck validator end

    <c:if test="${op:hasRole('ROLE_ADMIN_10')}">
    unableChangePart();
    //$("#dsgncntrPartId").val('<c:out value="${designatedDonation.dsgncntrPartId}"/>');
    </c:if>
}); //function() end


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
		    // index 값 조정
		    //currentIndex = Math.max(currentIndex - 1, 0);
		} else {
			//resetItemOption($(this).closest('tr'));
			//$("#sActiveOption").hide();
		}
        //let flag = true;
		updateRowIndex();
        //updateImgAlt(flag);
	});


    /*
        function
    */


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




    //접근성 이미지 설명 로우 추가 혹은 삭제
	function updateRowIndex() {
		let trList = $('#item-options2 tr');
		let size = trList.size();

		for(i = 0; i < size; i++) {
			$($($(trList[i]).children()[0]).children()[0]).attr('name', 'prjImageExplain[' + i + '].prjsIndexes');		// 순번
			$($($(trList[i]).children()[0]).children()[0]).attr('id', 'prjImageExplain[' + i + '].prjsIndexes');
			$($($(trList[i]).children()[0]).children()[0]).val(i+1);
			$($($(trList[i]).children()[1]).children()[0]).attr('name', 'prjImageExplain[' + i + '].prjsValues');		// 이미지 설명
			$($($(trList[i]).children()[1]).children()[0]).attr('id', 'prjImageExplain[' + i + '].prjsValues');
			$($($(trList[i]).children()[2]).children()[0]).attr('data-index', i);									// 삭제버튼
		}
	}

    //접근성 설명 이미지 alt로 update
    function updateImgAlt(endflag) {
        Common.getEditorContent("prjCn");
        //재귀 end 로직 추가
        let values = [...document.querySelectorAll("#item-options2 textarea[id='prjsValues']")].map(input => input.value);
        let outerIframe = document.getElementById("editor_frame");
        let timer;
        if (!outerIframe) {
            timer = setTimeout(updateImgAlt, 500);
            //setTimeout(clearTimeout(timer),1000000);
            return;
        }

        let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
        let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");

        if (!innerIframe) {
            timer = setTimeout(updateImgAlt, 500);
            //setTimeout(clearTimeout(timer),1000000);
            return;
        }

        let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
        let images = innerIframeDoc.querySelectorAll("img");

        if (images.length === 0) {
            timer = setTimeout(updateImgAlt, 500);
            //setTimeout(clearTimeout(timer),10000);
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

        // 상품이미지 삭제
    function deleteDsgncntrPrjImageId (type, id) {
        var message = '이미지가 실제로 삭제됩니다.(복구불가)\n삭제하시겠습니까?';

        //이미지가 1개 이하이면 return false
        if($("[id^='prj_image_id_']").length < 2) {
            alert("이미지는 최소 1개 이상은 등록 되어야 합니다.");
            return false;
        }


		if (!confirm(message)) {
			return;
		}

        var param = {'dsgncntrPrjImageId': id};
        $.post('${fn:escapeXml(requestContext.managerUri)}/designated-donation/delete-prj-details-image', param, function(response){
            Common.responseHandler(response);
            $('#prj_image_id_' + param.dsgncntrPrjImageId).remove();
        });

    }

    if (!File.isSupportMultiple) {
		// 파일 추가
		$('#add_detail_image_file').css('margin-bottom', '10px').show().on("click", function() {
			var html = '<p><input type="file" name="detailImageFiles[]" multiple="multiple" accept="image/png, image/jpeg, image/gif" /> <a href="#" class="delete_detail_image_file">[' + Message.get("M00074") + ']</a></p>';  // 삭제
			$('#multiple_files').append(html);
		});

		// 추가항목 삭제
		$('#multiple_files').on('click', '.delete_detail_image_file', function(e) {
			e.preventDefault();
			$(this).parent().remove();
		});
	}

    // 상세이미지 드레그
	// $('.sortable_item_image').sortable({
	// 	placeholder: "sortable_item_image_placeholder"
	// });

    // $(".sortable_item_image, .sortable_item_relation, .sortable_item_set").disableSelection();

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
                <c:choose>
	                <c:when test="${adminRole eq 'ROLE_ADMIN_5' || adminRole eq 'ROLE_ADMIN_6' || adminRole eq 'ROLE_ADMIN_7' || adminRole eq 'ROLE_ADMIN_8' || adminRole eq 'ROLE_ADMIN_10'}">
		                $("#upperLocgovCode").val('${fn:escapeXml(designatedDonation.upperLocgovCode)}').prop("selected", true);
		                $("#locgovCode").val('${fn:escapeXml(designatedDonation.locgovCode)}').prop("selected", true);
	                </c:when>
	                <c:otherwise>
	                	if ('${fn:escapeXml(designatedDonation.locgovCode)}') {
			                $("#upperLocgovCode").val('${fn:escapeXml(designatedDonation.upperLocgovCode)}').prop("selected", true);
			                $("#locgovCode").val('${fn:escapeXml(designatedDonation.locgovCode)}').prop("selected", true);
	                	}
	                </c:otherwise>
                </c:choose>
                //getDsgncntrPartList(value);
		    });
		} else {
	        $('#locgovCode').append('<option value="">-시,군,구-</option>');
		}
        //지자체 수정 불가
        changeYn = 'Y'
        if ("${fn:escapeXml(designatedDonation.upperLocgovCode)}" != "" && changeYn == 'Y') {
            var selectElements = document.querySelectorAll('#upperLocgovCode, #locgovCode');

            selectElements.forEach(function(selectElement) {
                selectElement.addEventListener('mousedown', function(e) {
                    e.preventDefault();
                }, false);
            });
        }
	}

    function bsnsTypeChange(value, isInit) {
    	$("#bsnsSubType option").remove();
		if ($("#bsnsType").val() != "") {
			$.post(url("/opmanager/designated-donation/bsnsSubType"), {'code' : value}, function(response) {
		    	let subType = $('#bsnsSubType').val();
		    	$('#bsnsSubType').append('<option value="">-선택-</option>');
				for (var i = 0; i < response.length; i++) {
					let options = "";
					if (response[i].id == subType) {
						options = '<option value="' + response[i].id + '" selected="selected">' + response[i].label + '</option>';
		            } else {
		            	options = '<option value="' + response[i].id + '">' + response[i].label + '</option>';
		            }

		            $('#bsnsSubType').append(options);
		        }
				if (isInit) {
	                $("#bsnsSubType").val('${fn:escapeXml(designatedDonation.bsnsSubType)}').prop("selected", true);
				}
			});
		} else {
	        $('#bsnsSubType').append('<option value="">-선택-</option>');
		}
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

    function is3Years(startDateStr, endDateStr) {
    	let startDate = new Date(Number(startDateStr.substring(0, 4)), Number(startDateStr.substring(4, 6)), Number(startDateStr.substring(6)));
    	let endDate = new Date(Number(endDateStr.substring(0, 4)), Number(endDateStr.substring(4, 6)), Number(endDateStr.substring(6)));
    	startDate.setFullYear(startDate.getFullYear() + 3);
    	if (startDate.getMonth() == 1 && startDate.getDate() == 29) {
    		startDate.setDate(28);
    	}
    	if (startDate > endDate) {
    		return true;
    	}
    	return false;
    }

    function getDsgncntrPartList(locgovCode) {
		$("#dsgncntrPartId option").remove();
		$('#dsgncntrPartId').append('<option value="0">-부서 미지정-</option>');
		$("#dsgncntrPartId").val("0");
    	if (locgovCode) {
    		$.post(url("/opmanager/designated-donation/partList"), {'locgovCode' : locgovCode}, function(response) {
    			if (response.isSuccess) {
    				let partList = response.data;
    				let length = partList.length;
    				let options = "";
        			for (var i = 0; i < length; i++) {
    	            	options += '<option value="' + partList[i].dsgncntrPartId + '">' + partList[i].dsgncntrPartName + '</option>';
        	        }
    	            $('#dsgncntrPartId').append(options);
    			} else {
    				alert("부서정보 조회에 실패했습니다.");
    			}
    	    });
    	}
    }

    function confirmProcess() {
    	let prjId = <c:out value="${designatedDonation.prjId}"/>;
    	if (prjId) {
        	let data = {id: prjId}
        	$.post("/opmanager/designated-donation/list/update-label/2", data, function(response) {
                Common.responseHandler(response, function(response){
                	alert("승인처리 되었습니다.");
                	location.href = '<c:out value="${requestContext.managerUri}/designated-donation/list"/>';
                }, function (error) {
                	console.log(error);
                	alert("승인처리에 실패했습니다.");
                });
            });
    	} else {
    		alert("등록 후 승인처리가 가능합니다.");
    	}
    }

    function unableChangePart() {
    	let selectElements = document.querySelectorAll('#dsgncntrPartId');

        selectElements.forEach(function(selectElement) {
            selectElement.addEventListener('mousedown', function(e) {
                e.preventDefault();
            }, false);
        });
    }

</script>
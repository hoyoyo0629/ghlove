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

<form:form modelAttribute="designatedPart" id="designatedPart" method="post" enctype="multipart/form-data" onsubmit='' action="${fn:escapeXml(requestContext.managerUri)}/designated-donation/part/save">
    <h3 class="custom">사업부서 정보</h3>
    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="사업부서 정보">
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
							<td colspan="3">
							    <div class="flex_box gap-08">
							        <form:select path="upperLocgovCode" class="wd-150" onChange="wdrChange(this.value)" title="상위 지자체">
										<form:option value= "">-${op:message('M00039')}-</form:option>
										<c:forEach items="${wdr}" var="wdr">
										<!-- <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" /> -->
											<form:option value="${fn:escapeXml(wdr.id)}" selected="${fn:escapeXml(wdr.id) eq fn:escapeXml(upperLocgovCode) ? 'selected' : ''}" label="${fn:escapeXml(wdr.label)}" />
										</c:forEach>
									</form:select>

									<form:select path="locgovCode" class="wd-150 required" title="지자체">
										<option value="${fn:escapeXml(locgovCode)}">-시,군,구-</option>
									</form:select>
							    </div>
							</td>
                        </tr>

                        <tr>
                            <td class="label "><span class="required_mark">*</span>부서명</td>
                            <td>
                                <div class="flex_box gap-12 item-center">
                                    <form:input path="dsgncntrPartName" maxlength="50" class="input_txt required _filter wd-500" title="부서명"/>
                                </div>
                            </td>
                            <td class="label"><span class="required_mark">*</span>사용여부</td>
                            <td>
                                <div class="flex_box gap-12">
									<div class="input-form">
									   	<form:radiobutton path="useYn" value="Y" label="사용" />
									</div>
									<div class="input-form">
										<form:radiobutton path="useYn" value="N" label="미사용" />
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
                    <button type="submit" class="btn btn-dark-gray btn-mini">${designatedPart.dsgncntrPartId == 0 || designatedPart.dsgncntrPartId == '' ? '등록' : '수정'}</button>
                    <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/designated-donation/part/list')" class="btn btn-default">${op:message('M00480')}</a>
            </div>
        </div>
    </div>
    <input type="hidden" id="dsgncntrPartId" name="dsgncntrPartId" value="<c:out value='${dsgncntrPartId}'/>">
</form:form>

<script type="text/javascript">

    // 지차체 변경여부 체크
    let changeYn = "N";

	$(function() {
	    wdrChange($("#upperLocgovCode").val());
	    $('#designatedPart').validator(function () {
	    	if (!$("#upperLocgovCode").val()) {
	    		alert("시도 지자체를 선택하세요.");
    			$("#upperLocgovCode").focus();
	    		return false;
	    	}

	    	if (!$("#locgovCode").val()) {
	    		alert("시군구 지자체를 선택하세요.");
    			$("#locgovCode").focus();
	    		return false;
	    	}

	    	if (!$("#dsgncntrPartName").val()) {
	    		alert("부서명을 입력하세요.");
    			$("#dsgncntrPartName").focus();
	    		return false;
	    	}

	    	if (!$("input[name=useYn]:checked").val()) {
	    		alert("사용여부를 선택하세요.");
    			$("input[name=useYn]").eq(0).focus();
	    		return false;
	    	}

	    });
	}); //function() end


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
                <c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8') || op:hasRole('ROLE_ADMIN_10'))}">
	                $("#upperLocgovCode").val('${fn:escapeXml(designatedPart.upperLocgovCode)}').prop("selected", true);
	                $("#locgovCode").val('${fn:escapeXml(designatedPart.locgovCode)}').prop("selected", true);
                </c:if>
		    });
		} else {
	        $('#locgovCode').append('<option value="">-시,군,구-</option>');
		}
        //지자체 수정 불가
        changeYn = 'Y'
        if ("${designatedPart.upperLocgovCode}" != "" && changeYn == 'Y') {
            var selectElements = document.querySelectorAll('#upperLocgovCode, #locgovCode');

            selectElements.forEach(function(selectElement) {
                selectElement.addEventListener('mousedown', function(e) {
                    e.preventDefault();
                }, false);
            });
        }
	}

    function getCurrentKoreanDate() {
        const options = { timeZone: 'Asia/Seoul' };
        const koreanDate = new Date().toLocaleString('en-US', options);
        const [datePart, timePart] = koreanDate.split(', ');

        if (datePart && timePart) {
            const [month, day, year] = datePart.split('/');
            return `${year}${month}${day}`;
        }

        return null; // 오류 처리
    }

    function is3Years(startDateStr, endDateStr) {
    	let startDate = new Date(Number(startDateStr.substring(0, 4)), Number(startDateStr.substring(4, 6)), Number(startDateStr.substring(6)));
    	let endDate = new Date(Number(endDateStr.substring(0, 4)), Number(endDateStr.substring(4, 6)), Number(endDateStr.substring(6)));
    	startDate.setFullYear(startDate.getFullYear() + 3);
    	if (startDate.getMonth() == 1 && startDate.getDate() == 29) {
    		//startDate.setDate(28);
    	}
    	if (startDate > endDate) {
    		return true;
    	}
    	return false;
    }

</script>
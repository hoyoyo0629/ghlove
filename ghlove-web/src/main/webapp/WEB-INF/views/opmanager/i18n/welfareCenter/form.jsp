<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop" %>

<div class="location">
    <a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<div class="item_list">
    <h3 class="custom">행정복지센터 관리</h3>

    <form:form modelAttribute="wlfrCntrMng" id="wlfrCntrMng" name="wlfrCntrMng" method="post" >
        <form:hidden path="pbadmsWlfrCntrId" value="${fn:escapeXml(pbadmsWlfrCntrId)}"/>

        <div class="item_info_wrap">
            <div class="item_list mt70">
                <div class="board_write">
                    <table class="board_write_table" summary="행정복지센터 관리 목록">
                        <colgroup>
                            <col style="width: 140px" />
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
                                    <c:choose>
									    <c:when test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
										    <c:if test="${wlfrCntrMng.lclgvCd != null}">
										    	<form:select path="upperLocgovCode" class="wd-150 required" onChange="wdrChange(this.value)" title="상위 지자체">
		                                            <form:option value= "">-${op:message('M00039')}-</form:option>
			                                        <c:forEach items="${wdr}" var="wdr">
			                                            <form:option value="${fn:escapeXml(wdr.id)}" selected="${fn:escapeXml(wdr.id) eq fn:escapeXml(wlfrCntrMng.upperLocgovCode) ? 'selected' : ''}" label="${fn:escapeXml(wdr.label)}" />
		                                            </c:forEach>
		                                        </form:select>
		                                        <form:select path="lclgvCd" id="lclgvCd" class="wd-150 required" title="지자체">
		                                            <option value="">-시,군,구-</option>
		                                        </form:select>
										    </c:if>
									    </c:when>
									    <c:otherwise>
									        <form:select path="upperLocgovCode" class="wd-150 required" onChange="wdrChange(this.value)" title="상위 지자체">
	                                            <form:option value= "">-${op:message('M00039')}-</form:option>
		                                        <c:forEach items="${wdr}" var="wdr">
		                                            <form:option value="${fn:escapeXml(wdr.id)}" selected="${fn:escapeXml(wdr.id) eq fn:escapeXml(locgovObj.UPPER_LOCGOV_CODE) ? 'selected' : ''}" label="${fn:escapeXml(wdr.label)}" />
	                                            </c:forEach>
	                                        </form:select>
	                                        <form:select path="lclgvCd" id="lclgvCd" class="wd-150 required" title="지자체">
	                                            <option value="${fn:escapeXml(lclgvCd)}">-시,군,구-</option>
	                                        </form:select>
										</c:otherwise>
                                    </c:choose>
								    </div>
                                </td>
                                <td class="label"><span class="required_mark">*</span>사용여부</td>
                                <td colspan="3">
                                    <div class="flex_box gap-08">
                                        <div class="input-form">
                                            <form:radiobutton path="useYn" name="useYn" value="Y" label="사용" checked="checked" />
                                        </div>
                                        <div class="input-form">
                                            <form:radiobutton path="useYn" name="useYn" value="N" label="미사용" />
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label"><span class="required_mark">*</span>행정복지센터명</td>
                                <td colspan="3">
                                    <div class="flex_box gap-08 item-center">
                                        <form:input path="pbadmsWlfrCntrNm" name="pbadmsWlfrCntrNm" maxlength="50" class="input_txt required _filter wd-400" title="행정복지센터명" autocomplete='off'/>
                                    </div>
                                </td>
                                <td class="label"><span class="required_mark"></span>행정복지센터 코드</td>
                                <td colspan="3">
                                    <div class="flex_box gap-08 item-center">
                                        <form:input path="pbadmsWlfrCntrCd" name="pbadmsWlfrCntrCd" maxlength="50" class="input_txt _filter wd-400" title="행정복지센터코드" autocomplete='off'/>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <div class="btn_all btn_right">
                        <div class="flex_box gap-08">
                            <button type="submit" class="btn btn-dark-gray btn-mini">${wlfrCntrMng.pbadmsWlfrCntrId == 0 || wlfrCntrMng.pbadmsWlfrCntrId == '' ? '등록' : '수정'}</button>
                            <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/welfareCenter/main/')" class="btn btn-default">${op:message('M00480')}</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
</div>

<script type="text/javascript">
    // 지자체 변경여부 체크
    let changeYn = "N";

    // 등록, 수정
    document.getElementById('wlfrCntrMng').addEventListener('submit', function(event) {
        var form = event.target; // 폼 요소 가져오기
        var pbadmsWlfrCntrId = form.querySelector('input[name="pbadmsWlfrCntrId"]').value;
        var baseUrl = "${fn:escapeXml(requestContext.managerUri)}/welfareCenter/form";

        if (pbadmsWlfrCntrId > 0) {
            // pbadmsWlfrCntrId 값 존재 > action URL에 값 추가
            console.log("수정 pbadmsWlfrCntrId >>>> ", pbadmsWlfrCntrId)
            form.action = baseUrl + '/' + pbadmsWlfrCntrId;
        } else {
            console.log("등록 pbadmsWlfrCntrId >>>> ", pbadmsWlfrCntrId)
            form.action = baseUrl;
        }
    });

    // 밸리데이션
    $(function() {
        wdrChange($("#upperLocgovCode").val());
        $('#wlfrCntrMng').validator(function () {
            if (!$("#upperLocgovCode").val() || $("#upperLocgovCode").val() === '0') {
                alert("상위 지자체 정보를 선택해 주세요.");
                event.preventDefault();
                return;
            }
            if (!$("#lclgvCd").val() || $("#lclgvCd").val() === '0') {
                alert("시, 군, 구 정보를 선택해 주세요.");
                return false;
            }
            if (!$("input[name='useYn']:checked").val()) {
                alert("사용여부를 선택해 주세요.");
                return false;
            }
            if (!$("#pbadmsWlfrCntrNm").val() || $("#pbadmsWlfrCntrNm").val() === '0') {
                alert("행정복지센터명을 입력해 주세요.");
                return false;
            }
        });
    }); //function() end

	function wdrChange(value) {
		//Common.loading.hide();
		$("#lclgvCd option").remove();
		if ($("#upperLocgovCode").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				for (var i = 0; i < response.length; i++) {
					let options = "";
					if (response[i].LOCGOV_CODE == value) {
						options = '<option value="' + response[i].LOCGOV_CODE + '" selected="selected">' + response[i].LOCGOV_NM + '</option>';
		            } else {
		            	options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            }

		            $('#lclgvCd').append(options);
		        }
                // 지자체 관리자 값으로 selected
				<c:choose>
	                <c:when test="${adminRole eq 'ROLE_ADMIN_5' || adminRole eq 'ROLE_ADMIN_6'}">
		                $("#upperLocgovCode").val('${fn:escapeXml(wlfrCntrMng.upperLocgovCode)}').prop("selected", true);
		                $("#lclgvCd").val('${fn:escapeXml(wlfrCntrMng.lclgvCd)}').prop("selected", true);
	                </c:when>
	                <c:otherwise>
	                	if ('${fn:escapeXml(wlfrCntrMng.lclgvCd)}') {
	                		$("#upperLocgovCode").val('${fn:escapeXml(wlfrCntrMng.upperLocgovCode)}').prop("selected", true);
	    	                $("#lclgvCd").val('${fn:escapeXml(wlfrCntrMng.lclgvCd)}').prop("selected", true);
	                	}
	                </c:otherwise>
	            </c:choose>
		    });
		} else {
	        $('#lclgvCd').append('<option value="">-시,군,구-</option>');
		}

        // 지자체 수정 불가
        changeYn = 'Y'
        if ("${fn:escapeXml(wlfrCntrMng.upperLocgovCode)}" != "" && changeYn == 'Y') {
            var selectElements = document.querySelectorAll('#upperLocgovCode, #lclgvCd');

            selectElements.forEach(function(selectElement) {
                selectElement.addEventListener('mousedown', function(e) {
                    e.preventDefault();
                }, false);
            });
        }
	}
</script>

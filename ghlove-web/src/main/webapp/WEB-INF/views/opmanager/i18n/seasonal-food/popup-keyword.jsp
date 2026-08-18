<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

	<!-- 키워드 등록 popup -->
    <div class="popup_wrap">
        <div id="pop_header">
            <h1 class="popup_title"><c:out value="${op:message('키워드 등록')}"/></h1><!-- 키워드 등록 -->
			<a href="javascript:self.close();" class="btn_close"><img src="../../content/opmanager/images/btn/btn_close.png" alt="${op:message('M00569')}"></a><!-- 닫기 -->
		</div>

        <div class="popup_contents">
            <div class="board_write">
                <table class="board_write_table" summary="">
                    <colgroup>
                        <col style="width:150px;">
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><span class="required_mark">*</span><c:out value="${op:message('키워드')}"/></td><!-- 키워드 -->
                            <td>
                                <div class="flex_box gap-08">
                                    <input id="keyword" name="keyword" title="${op:message('M00011')}" class="input_txt required _filter wd-200"
                                    	type="text" value="" oninput="javascript:inputCheck(this);"><!-- 검색구분 -->
                                </div>
                            </td>

                        </tr>
                        <tr>
                            <td class="label"><span class="required_mark">*</span><c:out value="${op:message('M01347')}"/></td><!-- 기간 -->
                            <td>
                                <div class="flex_box gap-04 item-center">
                                    <select id="startMonth" name="startMonth" title="${op:message('M00431')}" class="wd-200"><!-- 선택 -->
                                        <option value=""><c:out value="${op:message('M00431')}"/></option><!-- 선택 -->
                                        <c:forEach var="no" begin="1" end="12">
                                        	<option value="${no}"><c:out value="${no}"/></option>
                                        </c:forEach>
                                    </select>
                                    <span class="wave"><c:out value="${op:message('M01077')}"/></span><!-- 월 -->
                                    <span class="wave">~</span>
                                    <select id="endMonth" name="endMonth" title="${op:message('M00431')}" class="wd-200"><!-- 선택 -->
                                        <option value=""><c:out value="${op:message('M00431')}"/></option><!-- 선택 -->
                                        <c:forEach var="no" begin="1" end="12">
                                        	<option value="${no}"><c:out value="${no}"/></option>
                                        </c:forEach>
                                    </select>
                                    <span class="wave"><c:out value="${op:message('M01077')}"/></span><!-- 월 -->
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <p class="popup_btns">
                    <button type="button" class="btn btn-active" onclick="javascript:save();"><c:out value="${op:message('M00192')}"/></button><!-- 추가 -->
                    <button type="button" class="btn btn-default" onclick="javascript:self.close();"><c:out value="${op:message('M00569')}"/></button><!-- 닫기 -->
                </p>
            </div>
        </div>
    </div>
    <!-- // 키워드 등록 popup -->

    <form id="seasonalFoodKeywordData" >
	    <input type="hidden" id="month">
	    <input type="hidden" id="keyword">
    </form>

<script type="text/javascript">

$(function() {

});

function save() {
	let startMonth = $("#startMonth").val();
	let endMonth = $("#endMonth").val();
	let keyword = $("#keyword").val();

	if (!keyword) {
		alert("${op:message('키워드를 입력해주세요.')}");	// 키워드를 입력해주세요.
		return;
	}
	if (!startMonth || !endMonth) {
		alert("${op:message('기간을 선택해주세요.')}");		//
		return;
	}

	try {
		if (confirm("${op:message('정보를 저장하시겠습니까?')}")) {
			// opener.regSeasonalFoodData(startMonth, endMonth, keyword);		// parent window 에서 개발자 도구 띄우지 않으면 호출 안됨....
			window.opener.postMessage({fnName: 'regSeasonalFoodData', startMonth: startMonth, endMonth: endMonth, keyword: keyword}, '*');
		}
	} catch (e) {
		$s.error(e);
		alert("${op:message('제철식품관 관리 화면에서 진행해주세요.')}");		// 제철식품관 관리 화면에서 진행해주세요.
	}
}

// 부모화면에서 알림 호출
function openMsg(msg) {
	alert(msg);
	location.reload();
}

//입력 제한
function inputCheck(inputElement) {
	let beforePosition = inputElement.selectionStart - 1;		// 키 입력 후 위치 - 1 => 입력 전 위치
	let beforeLength = inputElement.value.length;			// 입력 후 길이
//	inputElement.value = inputElement.value.replace(/[^0-9.]/g, '');		// 숫자만
	inputElement.value = inputElement.value.replace(/[^a-zA-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|0-9|,|]/g, '');		// 알파벳,한글,숫자, ',' 만 입력 가능
	let afterLength = inputElement.value.length;			// 입력 전 길이
	if (beforeLength > afterLength) {		// 다르면 가능한 입력 값이 아님
		inputElement.setSelectionRange(beforePosition, beforePosition);		// 기존 위치로 커서 이동
	}
}

//부모화면에서 알림 호출
function confirmMsg(msg) {
	return confirm(msg);
}


</script>

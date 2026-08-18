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

	<!-- 키워드 수정 popup -->
    <div class="popup_wrap">
        <div id="pop_header">
            <h1 class="popup_title"><c:out value="${op:message('키워드 수정')}"/></h1><!-- 키워드 수정 -->
			<a href="javascript:self.close();" class="btn_close"><img src="../../content/opmanager/images/btn/btn_close.png" alt="${op:message('M00569')}"></a><!-- 닫기 -->
		</div>

        <div class="popup_contents" style="padding-bottom:30px;">
            <div class="board_write">
                <table class="board_write_table" summary="">
                    <colgroup>
                        <col style="width:150px;">
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><c:out value="${op:message('M01347')}"/></td><!-- 기간 -->
                            <td>
                                <div id="month">
                                    
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"><c:out value="${op:message('키워드')}"/></td><!-- 키워드 -->
                            <td>
                                <div>
                                    <span class="placeholder_wrap">
                                        <!-- <span class="placeholder">키워드를 입력해주세요.</span> -->
                                        <textarea id="comments" name="comments" cols="30" rows="10" maxlength="140" class="required _filter" title="${op:message('키워드')}"
                                        	oninput="javascript:inputCheck(this);"></textarea><!-- 키워드 -->
                                    </span>
                                </div>
                            </td>
                        </tr>
           
                    </tbody>
                </table>
            </div>

            <p class="popup_btns">
                <button type="button" class="btn btn-active" onclick="javascript:save();"><c:out value="${op:message('M00101')}"/></button><!-- 저장 -->
                <button type="button" class="btn btn-default" onclick="javascript:self.close();"><c:out value="${op:message('M00569')}"/></button><!-- 닫기 -->
            </p>
        </div>
    </div>
    <!-- // 키워드 수정 popup -->
    
    <form id="seasonalFoodMonthData" >
	    <input type="hidden" id="month">
	    <input type="hidden" id="keyword">
    </form>

<script type="text/javascript">

let seasonalFoodMonth;

$(function() {
	try {
		// window.opener.getSeasonalFoodMonthData();		// 화면 오픈 후 부모화면 함수 호출(기존 데이터 가져오기..)
		window.opener.postMessage({fnName: 'getSeasonalFoodMonthData'}, '*');
	} catch (e) {
		let errorBoolean = true;
		alert('${op:message("제철식품관 관리 화면에서 진행해주세요.")}');		//제철식품관 관리 화면에서 진행해주세요.
		// self.close();
	}
});

// 저장버튼 클릭시
function save() {
	try {
		let keyword = $("#comments").val();
		//window.opener.saveSeasonalFoodMonthData(seasonalFoodMonth, keyword);		// parent window 에서 개발자 도구 띄우지 않으면 호출 안됨....
		window.opener.postMessage({fnName: 'saveSeasonalFoodMonthData', month : seasonalFoodMonth, keyword: keyword}, '*');
	} catch (e) {
		let errorBoolean = true;
		alert('${op:message("제철식품관 관리 화면에서 진행해주세요.")}');		// 제철식품관 관리 화면에서 진행해주세요.
	}
}

// 부모화면에서 넘겨준 데이터 요소에 세팅
function setData(month, keyword) {		// 부모화면에서 호출하여 데이터 세팅
	if (!month) {
		alert('${op:message("M00473")}');		// 데이터가 없습니다.
		self.close();
	}
	seasonalFoodMonth = month;
	$("#month").text("" + month + "${op:message('M01077')}");		// 월
	$("#comments").text(keyword);
}

// 입력 제한
function inputCheck(inputElement) {
	let beforePosition = inputElement.selectionStart - 1;		// 키 입력 후 위치 - 1 => 입력 전 위치
	let beforeLength = inputElement.value.length;			// 입력 후 길이
//	inputElement.value = inputElement.value.replace(/[^0-9.]/g, '');		// 숫자만
	inputElement.value = inputElement.value.replace(/[^a-zA-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|0-9|,|]/g, '');		// 알파벳 한글 숫자 , 만 입력 가능
	let afterLength = inputElement.value.length;			// 입력 전 길이
	if (beforeLength > afterLength) {		// 다르면 가능한 입력 값이 아님
		inputElement.setSelectionRange(beforePosition, beforePosition);		// 기존 위치로 커서 이동
	}
}

//부모화면에서 알림 호출
function openMsg(msg) {
	alert(msg);
}

//부모화면에서 알림 호출
function confirmMsg(msg) {
	return confirm(msg);
}


</script>

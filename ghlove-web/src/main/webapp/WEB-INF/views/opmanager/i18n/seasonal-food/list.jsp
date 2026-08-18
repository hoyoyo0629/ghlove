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

		<div class="location">
            <a href=""></a> &gt; <a href=""></a> &gt; <a href=""></a>
        </div>
        <h3><span><c:out value="${op:message('제철식품관 관리')}"/></span></h3><!-- 제철식품관 관리 -->
        <!-- // 상단 타이틀 영역 -->
		<form:form modelAttribute="seasonFoodData">
	        <div class="board_list">
	            <table class="board_list_table" summary="${op:message('제철식품관 관리')}"><!-- 제철식품관 관리 -->
	                <caption><c:out value="${op:message('제철식품관 관리')}"/></caption><!-- 제철식품관 관리 -->
	                <colgroup>
	                    <col style="width:100px;">
	                    <col style="width:500px;">
		                <c:if test="${op:hasRole('ROLE_ADMIN_1')
		                			 || op:hasRole('ROLE_ADMIN_2')
		                			 || op:hasRole('ROLE_ADMIN_3')
		                			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
	                    	<col style="width:100px;">
	                    </c:if>
	                </colgroup>
	                <thead>
	                    <tr>
	                        <th scope="col"><c:out value="${op:message('M01379')}"/></th><!-- 월별 -->
	                        <th scope="col"><c:out value="${op:message('키워드')}"/></th><!-- 키워드 -->
			                <c:if test="${op:hasRole('ROLE_ADMIN_1')
			                			 || op:hasRole('ROLE_ADMIN_2')
			                			 || op:hasRole('ROLE_ADMIN_3')
			                			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
	                        	<th scope="col"><c:out value="${op:message('M00590')}"/></th><!-- 관리 -->
	                        </c:if>
	                    </tr>
	                </thead>
	                <tbody id="seasonFoodTable">
	                	<c:forEach var="seasonFood" items="${seasonFoodData}">
		                    <tr style="background:#fff;">
		                        <td>
		                            <div><c:out value="${seasonFood.seasonalFoodMonth}"/><c:out value="${op:message('M01077')}"/></div><!-- 월 -->
		                        </td>
		                        <td>
		                            <div class="text-left" id="data${fn:escapeXml(seasonFood.seasonalFoodMonth)}"><c:out value="${seasonFood.seasonalFoodKeyword}"/></div>
		                        </td>
				                <c:if test="${op:hasRole('ROLE_ADMIN_1')
				                			 || op:hasRole('ROLE_ADMIN_2')
				                			 || op:hasRole('ROLE_ADMIN_3')
				                			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
			                        <td>
			                            <div class="flex_box juc-center gap-08">
			                                <button type="button" class="btn btn-dark-gray btn-sm" onclick="javascript:modSeasonalFoodKeyword('${fn:escapeXml(seasonFood.seasonalFoodMonth)}', '${fn:escapeXml(seasonFood.seasonalFoodKeyword)}');"><c:out value="${op:message('M00087')}"/></button><!-- 수정 -->
			                            </div>
			                        </td>
		                        </c:if>
		                    </tr>
	                	</c:forEach>
	                </tbody>
	            </table>
	        </div>
		</form:form>
        <c:if test="${op:hasRole('ROLE_ADMIN_1')
        			 || op:hasRole('ROLE_ADMIN_2')
        			 || op:hasRole('ROLE_ADMIN_3')
        			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
	        <div class="btn_all btn_right mt20">
	            <div class="flex_box gap-08">
	                <button type="button" class="btn btn-default btn-small" onclick="javascript:regSeasonalFoodKeyword();"><c:out value="${op:message('M00088')}"/></button><!-- 등록 -->
	            </div>
	        </div>
        </c:if>

<script type="text/javascript">

const receiveMsg = async (e) => {
	if (e.data.hasOwnProperty('fnName')) {
		if (e.data.fnName == 'saveSeasonalFoodMonthData') {
			saveSeasonalFoodMonthData(e.data.month, e.data.keyword);
		} else if (e.data.fnName == 'regSeasonalFoodData') {
			regSeasonalFoodData(e.data.startMonth, e.data.endMonth, e.data.keyword);
		} else if (e.data.fnName == 'getSeasonalFoodMonthData') {
			getSeasonalFoodMonthData();
		}
	}
}


$(function() {
	window.addEventListener("unload", (event) => {		// 화면 닫을 때 팝업 같이 닫기(화면 이동시 팝업만 남아있는 상황 방지)
		if (popupMonth) {
			popupMonth.close();
		}
		if (popupKeyword) {
			popupKeyword.close();
		}
	});
	
	window.addEventListener("message", receiveMsg, false);			// 팝업 통신용
});

let popupType = "toolbar=no,width=700,height=310,top=150px,left=250px,directories=no,menubar=no,scrollbars=yes,location=no";
let popupMonth;
let popupKeyword;

let clickMonth;

<c:if test="${!(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6'))}">
// 수정버튼 클릭시
function modSeasonalFoodKeyword(month, keyword) {
	clickMonth = month;
	popupMonth = window.open('/opmanager/seasonal-food/popup-month', 'seasonalFoodPopupMonth', popupType);
}

// 수정버튼 팝업에서 데이터 가져오기 위해 호출
function getSeasonalFoodMonthData() {
	if (popupMonth) {
		popupMonth.setData(clickMonth, $("#data" + clickMonth).html());
	} else {
		popupMonth = window.open('/opmanager/seasonal-food/popup-month', 'seasonalFoodPopupMonth', popupType);
	}
	//clickMonth = "";
}

// 등록버튼 클릭시
function regSeasonalFoodKeyword() {
	popupKeyword = window.open('/opmanager/seasonal-food/popup-keyword', 'seasonalFoodPopupKeyword', popupType);
}

// 수정버튼 팝업에서 호출, 데이터 저장 후 화면 갱신
function saveSeasonalFoodMonthData(month, keyword) {
	if (month) {
		Common.loading.show();
		sendSaveData(popupMonth, {'seasonalFoods' : [{seasonalFoodMonth : month
													, seasonalFoodKeyword : keyword}]});
	} else {
		try {
			popupMonth.openMsg(Message.get("M00473"));			
		} catch(e) {
			alert(Message.get("M00473"));	// 데이터가 없습니다.
		}
	}
}

// 등록버튼 팝업에서 호출, 데이터 저장 후 화면 갱신
function regSeasonalFoodData(startMonthStr, endMonthStr, keyword) {
	let startMonth = Number(startMonthStr);
	let endMonth = Number(endMonthStr);
	
	if (startMonth > 0 && startMonth < 13 && endMonth > 0 && endMonth < 13) {
		let months = [];
		if (startMonth > endMonth) {		// 시작월이 클 때
			endMonth += 12;
		}/* else if (startMonth < endMonth) {		// 시작월이 작을 때
			
		} else {		// 같을 때
			
		}*/
		
		for (let i = 0 ; i <= (endMonth - startMonth) ; i++) {
			months.push(makeSeasonalKeywordData(startMonth + i, keyword));
		}
		
		Common.loading.show();
		sendSaveData(popupKeyword, {'seasonalFoods' : months});
	} else {
		try {
			popupKeyword.openMsg(Message.get("M00473"));			
		} catch(e) {
			alert(Message.get("M00473"));	// 데이터가 없습니다.
		}
	}
}

// null, undefine 값 empty 로 변환
function nullToEmpty(value) {
	if (!value) {
		return "";
	}
	return value;
}

// 등록 팝업에서 입력한 키워드를 기존 키워드와 합치기(중복안되는 거만)
function makeSeasonalKeywordData(month, inputKeyword) {
	if (month > 12) {
		month -= 12;
	}
	let savedDatas = $("#data" + month).html();
	let savedDatasArr = savedDatas.split(",");
	const savedDatasArrCopy = savedDatas.split(",");
	let inputKeywordArr = inputKeyword.split(",");
	
	let savedDatasLength = savedDatasArrCopy.length;
	let inputKeywordLength = inputKeywordArr.length;
	
	for (let i = 0 ; i < inputKeywordLength ; i++) {
		let isDupl = false;
		dupCheck: for (let j = 0 ; j < savedDatasLength ; j++) {
			if (inputKeywordArr[i] == savedDatasArrCopy[j]) {
				isDupl = true;
				break dupCheck;
			}
		}
		if (!isDupl) {
			if (inputKeywordArr[i]) {
				savedDatasArr.push(inputKeywordArr[i]);
			}
		}
	}
	
	return {seasonalFoodMonth : month, seasonalFoodKeyword : savedDatasArr.join(",")};
}

function sendSaveData(popupWindow, dataList) {
	//if (confirm("정보를 저장하시겠습니까?")) {
	if (popupWindow.confirmMsg("정보를 저장하시겠습니까?")) {
		$.ajax ({
			url	: "/opmanager/seasonal-food/saveSeasonalFood",
			type	: "POST",
			//timeout : 3000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
			data  : JSON.stringify(dataList), // 요청 시 포함되어질 데이터
			processData : true,
			contentType : "application/json",
			dataType    : "json",
			success : function(resp, status, xhr) {
				Common.responseHandler(resp, function(response){
					$("#seasonFoodTable").empty();
		 			if (response.data) {
		 				let data = response.data;
		 				let length = data.length;
		 				for(let i = 0; i < length ; i++) {
		 					let tr = "";
		 					tr += '<tr style="background:#fff;">';
		 					tr += '    <td>';
							tr += '        <div>' + nullToEmpty(data[i].seasonalFoodMonth) + '${op:message("M01077")}</div>';
							tr += '    </td>';
							tr += '    <td>';
							tr += '        <div class="text-left" id="data' + nullToEmpty(data[i].seasonalFoodMonth) + '">' + nullToEmpty(data[i].seasonalFoodKeyword) + '</div>';
							tr += '    </td>';
							tr += '    <td>';
							tr += '        <div class="flex_box juc-center gap-08">';
							tr += '            <button type="button" class="btn btn-dark-gray btn-sm" onclick="javascript:modSeasonalFoodKeyword(\'' + nullToEmpty(data[i].seasonalFoodMonth) + '\', \'' + nullToEmpty(data[i].seasonalFoodKeyword) + '\');">${op:message("M00087")}</button>';
							tr += '        </div>';
							tr += '    </td>';
							tr += '</tr>';
		 					$('#seasonFoodTable').append(tr);
		 				}
		 			}
	 				try {
	 					popupWindow.openMsg(Message.get("M00406"));	//저장되었습니다.
	 				} catch (e) {
	 					
	 					alert(Message.get("M00406"));	//저장되었습니다.
	 				}
				});
			},
			error	: function(xhr, status, error) {
				
				try {
					popupWindow.openMsg(Message.get("실패했습니다."));		// 실패했습니다.
				} catch (e) {
					
					alert(Message.get("실패했습니다."));		// 실패했습니다.
				}
			},
			complete : function(xhr, status) {
				Common.loading.hide();
			}
		});
	}
}
</c:if>


</script>

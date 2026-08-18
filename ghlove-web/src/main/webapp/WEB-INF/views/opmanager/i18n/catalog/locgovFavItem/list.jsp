<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<style>
.tbl-summary {
	border-right: 1px solid #dedede;
	border-bottom: 1px solid #dedede;
}
.tbl-summary th,
.tbl-summary td {
	border-left: 1px solid #dedede;
	border-top: 1px solid #dedede;
	text-align: center;
}

.tbl-summary th {
	background: #f4f4f4;
	padding: 10px;
	color: #000;
}
.tbl-summary td {
	padding: 0 10px;
	font-family: verdana;
}
.tbl-summary td {
	padding: 22px;
	font-size: 20px;
	font-family: verdana;
	
}
</style>
<!-- <div class="admin_wrap"> -->
<div>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>
	<div class="item_list">
		<h3><span>인기답례품 목록</span></h3>
		
		<form:form modelAttribute="locgovFavItemMngParam" method="post">
			
			<div class="board_write">
				<table class="board_write_table" summary="인기답례품 목록">
					<caption>인기답례품 목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
						<tr>
	                        <td class="label">지자체</td>
	                        <td>
	                        	<c:choose>
			                   		<c:when test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
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
			                   		</c:when>
		                   			<c:otherwise>
			                   			<div>
			                            	<c:out value="${locgovNm}"/>
			                            </div>
		                   			</c:otherwise>
		                   		</c:choose>
	                        </td>
	                    </tr>
						<tr>
							<td class="label">공개여부</td>
							<td>
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="deleteYn" value="" label="전체" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="deleteYn" value="N" label="공개" />
									</div>
									<div class="input-form">
										<form:radiobutton path="deleteYn" value="Y" label="비공개" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">등록기간</td>
							<td>
								<div>
									<span class="datepicker"><form:input path="searchStartDate" maxlength="8" class="datepicker" title="등록 시작일" /></span>
									<span class="wave">~</span>
									<span class="datepicker mr10"><form:input path="searchEndDate" maxlength="8" class="datepicker" title="등록 종료일" /></span>
									<span class="day_btns">
										<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>  
									<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a> 
									<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a> 
									<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a> 
									<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a> 
			                       </span>
								</div>
							</td>
						</tr>				                				
						<tr>
							<td class="label">발간호</td>
							<td>
								<div>
									<select id="catalogYear" title="발간년도" class="wd-150">
										<option value="">전체</option>
										<c:forEach items="${yyyy}" var="yyyy">
										    <option value="${fn:escapeXml(yyyy.catalogYear)}" label="${fn:escapeXml(yyyy.catalogYear)}" />
										</c:forEach>
						            </select>
<!-- 						            <input type="text" id="catalogNo" class="input_txt" title="발간호수" />검색어 -->
						            <form:input path="catalogYearNo" type="hidden" value=""></form:input>
								</div>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/catalog/locgovFavItem/list';"><c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>
			
			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(locgovFavItemMngParam.pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
				</h5>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
						onchange="$('form#locgovFavItemMngParam').submit();"> <!-- 화면 출력수 -->
						<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
						<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
					</form:select>
				</span>
			</div>
		</form:form>
		
		<div class="board_list">
			<form id="listForm" action="${fn:escapeXml(requestContext.managerUri)}/remittance" method="post">
				<table class="board_list_table" summary="인기답례품 목록">
					<caption>인기답례품 목록</caption>
					<colgroup>
						<col style="width:50px;">
						<col style="width:50px;">
						<col style="width:150px;">
						<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
							<col style="width:150px;">
						</c:if>
		                <col style="width:100px;">
		                <col style="width:150px;">
		                <col style="width:150px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="check_all" /></th>
							<th scope="col">No</th>
							<th scope="col">발간호</th>
							<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
								<th scope="col">지자체 명</th>
							</c:if>
							<th scope="col">상태</th>
							<th scope="col">등록일</th>
							<th scope="col">수정/삭제</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ locgovFavItemMngList }" var="locgovFavItemMng" varStatus="index">
							<c:set var="key"><c:out value="${locgovFavItemMng.catalogYear}-${locgovFavItemMng.catalogNo}-${locgovFavItemMng.locgovCode}"/></c:set>
							<tr style="background:#fff;">
								<td>
									<input type="checkbox" id="item_${fn:escapeXml(key)}" name="id" value="${fn:escapeXml(locgovFavItemMng.catalogYear)}-${fn:escapeXml(locgovFavItemMng.catalogNo)}-${fn:escapeXml(locgovFavItemMng.locgovCode)}" />
									<input type="hidden" class="item_catalogNo" value="${fn:escapeXml(locgovFavItemMng.catalogNo)}"/>
									<input type="hidden" class="item_catalogYear" value="${fn:escapeXml(locgovFavItemMng.catalogYear)}"/>
									<input type="hidden" class="item_locgovCode" value="${fn:escapeXml(locgovFavItemMng.locgovCode)}"/>
								</td>
								<td>
									<c:out value="${pagination.itemNumber - index.count}"/>
								</td>
								<td>
									<a href="${fn:escapeXml(requestContext.managerUri)}/catalog/locgovFavItem/form/${fn:escapeXml(locgovFavItemMng.catalogYear)}-${fn:escapeXml(locgovFavItemMng.catalogNo)}/${fn:escapeXml(locgovFavItemMng.locgovCode)}/${fn:escapeXml(locgovFavItemMng.locgovNm)}"> 
										<c:out value="${locgovFavItemMng.catalogYear}"/> - <c:out value="${locgovFavItemMng.catalogNo}"/>
									</a>
								</td>
								<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
									<td><div><c:out value="${locgovFavItemMng.locgovNm}"/></div></td>
								</c:if>
								<td>
									<c:out value="${locgovFavItemMng.deleteYn == 'N' ? '공개' : locgovFavItemMng.deleteYn == 'Y' ? '비공개' : ''}"/>
								</td>
								<td>
									<c:out value="${locgovFavItemMng.frstRegistPnttm}"/>
								</td>
								<td>
 									<div class="btn_all btn_center">
										<div class="flex_box gap-08">
											<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6'))}">
												<a href="/opmanager/catalog/locgovFavItem/form/${fn:escapeXml(locgovFavItemMng.catalogYear)}-${fn:escapeXml(locgovFavItemMng.catalogNo)}/${fn:escapeXml(locgovFavItemMng.locgovCode)}/${fn:escapeXml(locgovFavItemMng.locgovNm)}" class="btn btn-gradient btn-sm">${op:message('M00087')}<!-- 수정 --></a>
											</c:if>
											<button type="button" class="btn btn-dark-gray btn-mini" onclick='javascript:deleteLocgov("${fn:escapeXml(locgovFavItemMng.catalogYear)}","${fn:escapeXml(locgovFavItemMng.catalogNo)}","${fn:escapeXml(locgovFavItemMng.locgovCode)}")'> <c:out value="${op:message('M00074')}"/><!-- 삭제 --></button>
										</div>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<c:if test="${empty locgovFavItemMngList}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6'))}">
						<a href="/opmanager/catalog/locgovFavItem/form" class="btn btn-dark-gray btn-mini">등록</a>
					</c:if>
					<!-- 등록 -->
					<a href="javascript:deleteCheckedItem()" class="btn btn-default btn-mini">선택 삭제</a>
				</div>
			</div>

			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>
		</div>
	</div>
</div>

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>
<script type="text/javascript">

	var changeYn = "N";
	
	$("select[name='upperLocgovCode']").change(function() {
        searchSigungu(document.getElementById("upperLocgovCode"));
	});
	
	// 발간년도 선택 시 호수 필수값으로 설정
//  	$("#catalogYear").change(function() {
//  		const catalogNo = document.getElementById("catalogNo");
//  		catalogNo.classList.add("required");
//  	});
	
	// 발간년도 선택 시 호수 필수값으로 설정
 	$("#catalogYear").change(function() {
 		$("input[name='catalogYearNo']").val($("#catalogYear").val()+"-0");
 	});
	
	
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();
		
		let upperLocgovCode = '<c:out value="${locgovFavItemMngParam.upperLocgovCode}"/>'; 
		
		if (upperLocgovCode) {
			wdrChange(upperLocgovCode);
		}
		
		// 체크박스 이벤트 (체크&해제)
		checkedEventSet();
		
		try {
			$('#locgovFavItemMngParam').validator(function() {
				let strStartDate = $("#searchStartDate").val();
				let strEndDate = $("#searchEndDate").val();
	            if (strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
	                var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
	                var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));
	                
	                if (startDate > endDate) {
	                    alert("종료일이 시작일보다 빠릅니다.");
	                    $("#searchEndDate").focus();
	                    return false;                                                           
	                }
	                var searchChk = Common.searchDateMonth(startDate, endDate);
	                if (!searchChk) return false; 
	            } 
			});	
		} catch (e) {
			let errBln = true;
		}
	});
	
	/**
	 *	함 수 명 : checkedEvent
	 *	기	능  : 체크&해제 이벤트 셋팅
	 */
	function checkedEventSet() {
		var childObjs = $("input[name='no']").not("[id='check_all']");
		
		// 체크박스 '전체' 체크&해지
		$("#check_all").click(function(){
			$(childObjs).prop("checked", $(this).is(":checked"));
		});
		
		// '개별' 체크박스에 따른 '전체' 체크박스 체크&해지
		$(childObjs).click(function() {
			if($(childObjs).length == $("input[id^='check_']:checked").not("[id='check_all']").length) {
				$("#check_all").prop("checked", true);
			} else {
				$("#check_all").prop("checked", false);
			}
		});
	}

	function deleteLocgov(catalogYear,catalogNo,locgovCode,locgovNm) {
		var catalogYearNo = catalogYear+"-"+catalogNo;
		if (confirm("인기답례품을 삭제하시겠습니까?")) {
			Common.loading.show();
			$.post(url("/opmanager/catalog/locgovFavItem/form/"+catalogYearNo+"/"+locgovCode+"/"+locgovNm), {"deleteYn" : "Y"}, function(response) {
				alert("삭제되었습니다.")
				location.reload(true);
			});
		} else {
			return false;
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
				            if ("${fn:escapeXml(locgovFavItemMngParam.locgovCode)}" != "") {
				            	$("#locgovCode").val("${fn:escapeXml(locgovFavItemMngParam.locgovCode)}").prop("selected", true);
				            	
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
	
	// 체크한 상품 일괄 삭제
	function deleteCheckedItem() {
		if ($('input[name=id]:checked').size() == 0) {
			var message = Message.get("M01216");	// 추가할 상품을 선택해 주세요.
			alert("삭제할 항목을 선택해 주세요");
			$('#check_all').focus();
			return;
		}
		Common.updateListData("/opmanager/catalog/locgovFavItem/form/delete-list", "선택된 데이터를 삭제하시겠습니까?");	// 선택된 데이터를 수정하시겠습니까?
		alert("삭제되었습니다.")
	}
	
</script>


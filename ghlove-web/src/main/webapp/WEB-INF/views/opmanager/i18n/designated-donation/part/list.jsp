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
		<h3><span>사업부서 관리</span></h3>
		
		<form:form modelAttribute="param" method="post">
			
			<div class="board_write">
				<table class="board_write_table" summary="사업부서 관리목록">
					<caption>사업부서 관리목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
					<c:if test="${op:hasRole('ROLE_ADMIN_1')
								|| op:hasRole('ROLE_ADMIN_2')
								|| op:hasRole('ROLE_ADMIN_3')
								|| op:hasRole('ROLE_ADMIN_4')}">
	                    <tr>
	                        <td class="label">지자체</td>
	                        <td>
	                            <div class="flex_box gap-08">
		                            <form:select path="upperLocgovCode" class="wd-150" onChange="wdrChange(this.value)">
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
                    </c:if>
					<tr>
						<td class="label">부서명</td>
						<td>
							<div>
								<form:input path="dsgncntrPartName" class="input_txt" title="사업명" style="width: 100%;" /><!-- 검색어 -->
							</div>
						</td>
						<td class="label">사용유무</td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<form:radiobutton path="useYn" value="" label="${op:message('M00039')}" /> <!-- 전체 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="useYn" value="Y" label="사용" /> <!-- 판매중 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="useYn" value="N" label="미사용" /> <!--  품절 -->
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">등록일</td>
						<td colspan="3">
							<div>
								<span class="datepicker"><form:input path="searchStDt" maxlength="8" class="datepicker" title="조회 시작일" /></span>
								<span class="wave">~</span>
								<span class="datepicker mr10"><form:input path="searchEdDt" maxlength="8" class="datepicker" title="조회 종료일" /></span>
								<span class="day_btns mt3" style="margin-left:0px;">
									<a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
									<a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
									<a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
									<a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
									<a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
                                    <%-- <c:choose>
                                        <c:when test="${op:hasRole('ROLE_ADMIN_CALL')}">
                                            <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
                                        </c:when>
                                    </c:choose> --%>
								</span>
							</div>
						</td>
					</tr>
					</tbody>
				</table>

				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/designated-donation/part/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>
			
				
			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalItems)}"/> <c:out value="${op:message('M00272')}"/>
				</h5>
				<span>
					<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
						onchange="$('form#param').submit();"> <!-- 화면 출력수 -->
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

			<form id="listForm" method="post">
				<table class="board_list_table" summary="사업부서 관리목록">
					<caption>사업부서 관리목록</caption>
					<colgroup>
						<%-- <col style="width:50px;"> --%>
						<col style="width:50px;">
						<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
							<col style="width:150px;">
						</c:if>
		                <col style="width:400px;">
		                <col style="width:50px;">
		                <col style="width:150px;">
					</colgroup>
					<thead>
						<tr>
							<!-- <th scope="col"><input type="checkbox" id="check_all" /></th> -->
							<th scope="col">ID</th>
							<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
								<th scope="col">지자체 명</th>
							</c:if>
							<th scope="col">부서명</th>
							<th scope="col">사용유무</th>
							<th scope="col">등록일시</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ list }" var="part" varStatus="index">
							<c:set var="key"><c:out value="${part.dsgncntrPartId}"/></c:set>
							<tr style="background:#fff;">
								<%-- <td>
									<input type="checkbox" name="id" value="${key}" />
								</td> --%>
								<td>
									<%-- <c:out value="${op:numberFormat(designatedDonationSearchParam.pagination.itemNumber - index.count)}"/> --%>
									<c:out value="${op:numberFormat(key)}"/>
								</td>
								<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
									<td><div><c:out value="${part.locgovNm}"/></div></td>
								</c:if>
								<td>
		                        	<div>
		                        		<a href="/opmanager/designated-donation/part/form/<c:out value='${part.dsgncntrPartId}'/>">
		                        			<c:out value="${part.dsgncntrPartName}"/>
	                        			</a>
		                        	</div>
								</td>
								<td>
									<c:out value="${part.useYn == 'Y' ? '사용' : '미사용'}"/>
								</td>
								<td>
									<c:out value="${part.createdDateStr}"/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<c:if test="${empty list}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

            <div class="flex_box juc-sbt">
                <div class="btn_all">
                </div>
                <div class="btn_all">
                    <button type="button" onclick="location.href='/opmanager/designated-donation/part/form/0'" class="btn btn-default btn-mini">신규등록</button>
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

	$("select[name='upperLocgovCode']").on('focus', function () {
	    
	}).change(function() {
		changeYn = "Y";
	});
	
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStDt"]' , 'input[name="searchEdDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();
		
		let upperLocgovCode = '<c:out value="${param.upperLocgovCode}"/>'; 
		
		if (upperLocgovCode) {
			wdrChange(upperLocgovCode);
		}
		
		try {
			$('#param').validator(function() {
				let strStartDate = $("#prjStDt").val();
				let strEndDate = $("#prjEdDt").val();
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
			});	
        } catch (e) {
            alert("검색일을 확인해주세요.");
            $("#prjEdDt").focus();
            return false;
		}
	});

    // function updateListDataDisplay(flag) {

    //     if ($('#listForm').find('input[name=id]:checked').size() == 0) {
    //         alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
    //         return;
    //     } else {
    //         Common.updateListData("/opmanager/designated-donation/list/update-display/" + flag, "정보를 "+"${op:message('M00365')}");
    //     }
    // }

    function updateListDataLabel(flag) {
        var message = "정보를 "+"${op:message('M00365')}";
        if ($('#listForm').find('input[name=id]:checked').size() == 0) {
            alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
            return;
        } else {
            Common.updateListData("/opmanager/designated-donation/list/update-label/" + flag, message);
        }
    }
	
	// 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();
		$("#locgovCode option").remove();
		if ($("#upperLocgovCode").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				$('#locgovCode').append('<option value="">-전체-</option>');
				for (var i = 0; i < response.length; i++) {
		            let options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#locgovCode').append(options);
		        }
		        
				// 조회 된 값 유지
				if ("${fn:escapeXml(param.upperLocgovCode)}" != "" && changeYn == 'N') {
				    $("#locgovCode").val('${fn:escapeXml(param.upperLocgovCode)}').prop("selected", true);
					
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					//$("#locgovCode option:eq(0)").attr("selected", "selected");
				}
					let locgovCode = '<c:out value="${param.locgovCode}"/>';
					
				//if (locgovCode) {
					let optionLists = $("#locgovCode")[0];
					for(let option of optionLists) {
							if (option.value == locgovCode) {
								option.setAttribute("selected", "selected");
								break;
							}
						}
				//}
		    });
		} else {
	        $('#locgovCode').append('<option value="">-시,군,구-</option>');
		}
	}
	
	
	
	
	
</script>


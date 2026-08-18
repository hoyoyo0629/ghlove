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
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a> &gt; 공지사항
	</div>
	
	<div class="item_list">
		<h3> <c:out value="${designatedDonation.prjSubject}"/> 공지사항</h3>
		
		
	
	<form:form modelAttribute="designatedDonationSearchParam" method="post">
			
		<div class="board_write">
			<table class="board_write_table" summary="특정사업에 기부하기 공지사항 목록">
				<caption>공지사항 목록</caption>
				<colgroup>
					<col style="width: 150px" />
					<col style="width: auto;" />
				</colgroup>
				<tbody>
					<tr>
						<td class="label">제목</td>
						<td colspan="3">
							<div>
								<form:input path="searchKeyword" class="input_txt wd-500" title="제목"/><!-- 검색어 -->
							</div>
						</td>
					</tr>
				</tbody>
			</table>

			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/designated-donation/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
					<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
				</div>
			</div>
		</div>
			
		<div class="count_title mt-40">
			<h5>
				<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(designatedDonationSearchParam.pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
			</h5>
			<span>
				<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
					onchange="$('form#designatedDonationSearchParam').submit();"> <!-- 화면 출력수 -->
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
				<table class="board_list_table" summary="특정사업에 기부하기 목록">
					<caption>특정사업에 기부하기 목록</caption>
					<colgroup>
						<col style="width:50px;">
						<col style="width:100px;">
		                <col style="width:1000px;">
		                <col style="width:100px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="check_all" /></th>
							<th scope="col">ID</th>
							<th scope="col">제목</th>
							<th scope="col">공개여부</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ prjNoticeList }" var="prjNotice" varStatus="index">
							<c:set var="key"><c:out value="${prjNotice.prjNoticeId}"/></c:set>
							<tr style="background:#fff;">
								<td>
									<input type="checkbox" name="id" value="${fn:escapeXml(key)}" />
								</td>
								<td>
									<c:out value="${op:numberFormat(key)}"/>
								</td>
								<td>
									<a href="${requestContext.managerUri}/designated-donation/list/notice/form/${fn:escapeXml(designatedDonation.prjId)}/${fn:escapeXml(prjNotice.prjNoticeId)}"><c:out value="${prjNotice.prjNoticeSubject}"/></a>
								</td>
								<td>
									<c:out value="${prjNotice.displayYnStr}"/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<c:if test="${empty prjNoticeList}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

            <div class="flex_box juc-sbt">
                <div class="btn_all">
                    <div class="flex_box gap-08">
                        <button type="button" id="update_list_data_display" onclick="updateListDataLabel('Y')" class="btn btn-default btn-mini">공개</button>
                        <button type="button" id="update_list_data_display" onclick="updateListDataLabel('N')" class="btn btn-default btn-mini">비공개</button>
                    </div>
                </div>
                <div class="btn_all">
                    <div class="flex_box gap-08">
	                    <button type="button" onclick="location.href='${fn:escapeXml(requestContext.managerUri)}/designated-donation/list/notice/form/${fn:escapeXml(designatedDonation.prjId)}/0'" class="btn btn-default btn-mini">신규등록</button>
	                    <button type="button" onclick="location.href='${fn:escapeXml(requestContext.managerUri)}/designated-donation/list'" class="btn btn-default btn-mini">특정사업에 기부하기 목록</button>
                    </div>
                </div>
            </div>

			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>
		
		</div>
	
	</div>
<!-- </div> -->

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
		/*Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="prjStDt"]' , 'input[name="prjEdDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();
		
		let upperLocgovCode = '<c:out value="${designatedDonationSearchParam.upperLocgovCode}"/>'; 
		
		if (upperLocgovCode) {
			wdrChange(upperLocgovCode);
		}
		
		try {
			$('#designatedDonationSearchParam').validator(function() {
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
			console.log(e);
		}
		*/
	});


    function updateListDataLabel(flag) {
        var message = "정보를 "+"${op:message('M00365')}";
        if ($('#listForm').find('input[name=id]:checked').size() == 0) {
            alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
            return;
        } else {
            Common.updateListData("/opmanager/designated-donation/list/notice/update-label/" + flag, message);
        }
    }
	/*
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
				if ("${fn:escapeXml(designatedDonationSearchParam.upperLocgovCode)}" != "" && changeYn == 'N') {
				    $("#locgovCode").val('${fn:escapeXml(designatedDonationSearchParam.upperLocgovCode)}').prop("selected", true);
					
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					//$("#locgovCode option:eq(0)").attr("selected", "selected");
				}
					let locgovCode = '<c:out value="${designatedDonationSearchParam.locgovCode}"/>';
					
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
	*/
	
	
	
	
</script>


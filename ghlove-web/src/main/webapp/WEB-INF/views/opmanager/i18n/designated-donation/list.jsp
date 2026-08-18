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
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<div class="item_list">
		<h3><span>특정사업에 기부하기 목록</span></h3>

		<form:form modelAttribute="designatedDonationSearchParam" method="post">

			<div class="board_write">
				<table class="board_write_table" summary="특정사업에 기부하기 목록">
					<caption>특정사업에 기부하기 목록</caption>
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
						<td class="label">사업구분</td>    <!-- 검색구분 -->
						<td>
							<div class="flex_box gap-08">
								<form:select path="bsnsType" title="사업구분 선택" style="max-width: 100%;">
									<form:option value="" label="전체" />
									<c:forEach items="${bsnsTypeList}" var="bsnsType">
				                        <form:option value="${fn:escapeXml(bsnsType.id)}" label="${fn:escapeXml(bsnsType.detail)}" />
				                    </c:forEach>
								</form:select>
							</div>
						</td>
						<td class="label">기간</td>
						<td>
							<div>
								<span class="datepicker"><form:input path="prjStDt" maxlength="8" class="datepicker" title="모금 시작일" /></span>
								<span class="wave">~</span>
								<span class="datepicker mr10"><form:input path="prjEdDt" maxlength="8" class="datepicker" title="모금 종료일" /></span>
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
					<tr>
						<td class="label">사업명</td>
						<td colspan="3">
							<div>
								<%-- <form:input path="searchKeyword" class="input_txt" title="사업명" style="width: 100%;" /> --%><!-- 검색어 -->
								<form:input path="query" class="input_txt" title="사업명" style="width: 100%;" />
							</div>
						</td>
					</tr>

					<tr>
						<td class="label">상태</td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<form:radiobutton path="prjStatus" value="" label="전체" />
								</div>
								<div class="input-form">
									<form:radiobutton path="prjStatus" value="2" label="진행" />
								</div>
								<div class="input-form">
									<form:radiobutton path="prjStatus" value="9" label="종료" />
								</div>
								<div class="input-form">
									<form:radiobutton path="prjStatus" value="1" label="대기" />
								</div>
							</div>
						</td>
						<td class="label">공개여부</td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<form:radiobutton path="displayFlag" value="" label="전체" />
								</div>
								<div class="input-form">
									<form:radiobutton path="displayFlag" value="Y" label="공개" />
								</div>
								<div class="input-form">
									<form:radiobutton path="displayFlag" value="N" label="비공개" />
								</div>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
				<li>
			    	검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			    </li>
		</div>
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/designated-donation/list'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>

			<div class="mt-40">
				<table class="tbl-summary">
					<colgroup>
						<col style="width: 200px" />
						<col style="width: 200px;" />
						<col style="width: 200px" />
						<col style="width: 200px;" />
					</colgroup>
					<thead>
						<tr>
							<th>총 목표금액</th> <!-- 총 목표금액 -->
							<th>총 모금액</th> <!-- 총 모금액 -->
							<th>총 모금 달성율</th> <!-- 총 모금 달성율 -->
							<th>총 기부건수</th> <!-- 총 참여자 수 -->
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><c:out value="${op:numberFormat(designatedStat.totTargetAmt)}"/>원</td>
							<td><c:out value="${op:numberFormat(designatedStat.totCntrAmt)}"/>원</td>
							<td><c:out value="${op:numberFormat(designatedStat.totAchvRt)}"/>%</td>
							<td><c:out value="${op:numberFormat(designatedStat.totCntrCnt)}"/>명</td>
						</tr>
					</tbody>
				</table>
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
						<col style="width:50px;">
						<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
							<col style="width:150px;">
						</c:if>
		                <col style="width:100px;">
		                <col style="width:150px;">
		                <col style="width:400px;">
		                <col style="width:150px;">
		                <col style="width:150px;">
		                <col style="width:150px;">
		                <col style="width:150px;">
		                <col style="width:150px;">
		                <col style="width:100px;">
		                <col style="width:100px;">
		                <col style="width:100px;">
		                <col style="width:100px;">
		                <col style="width:100px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="check_all" /></th>
							<th scope="col">ID</th>
							<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
								<th scope="col">지자체 명</th>
							</c:if>
							<th scope="col">모금상태</th>
							<th scope="col">사업구분</th>
							<th scope="col">사업명</th>
							<th scope="col">기부건수</th>
							<th scope="col">시작일</th>
							<th scope="col">종료일</th>
							<th scope="col">목표금액</th>
							<th scope="col">모금액</th>
							<th scope="col">달성율</th>
							<th scope="col">공개여부</th>
							<th scope="col">사업부서</th>
							<th scope="col">공지사항</th>
							<th scope="col">승인이력</th>
							<th scope="col">미리보기</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ designatedDonationList }" var="designatedDonation" varStatus="index">
							<c:set var="key"><c:out value="${designatedDonation.prjId}"/></c:set>
							<tr style="background:#fff;">
								<td>
									<input type="checkbox" name="id" value="${fn:escapeXml(key)}" />
								</td>
								<td>
									<%-- <c:out value="${op:numberFormat(designatedDonationSearchParam.pagination.itemNumber - index.count)}"/> --%>
									<c:out value="${op:numberFormat(key)}"/>
								</td>
								<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
									<td><div><c:out value="${designatedDonation.locgovNm}"/></div></td>
								</c:if>
								<td>
									<c:out value="${designatedDonation.prjStatus == '1' ? '승인 대기' : designatedDonation.prjStatus == '2' ? '진행' : designatedDonation.prjStatus == '9' ? '종료' : ''}"/>
								</td>
								<td>
									<c:forEach items="${bsnsTypeList}" var="bsnsType">
				                        <c:if test="${bsnsType.id == designatedDonation.bsnsType}">
				                        	<div><c:out value="${bsnsType.detail}"/></div>
				                        </c:if>
				                    </c:forEach>
								</td>
								<td style="text-align: left;">
									<a href="${fn:escapeXml(requestContext.managerUri)}/designated-donation/form/${fn:escapeXml(designatedDonation.prjId)}">
										<img src="/upload/prj/${fn:escapeXml(key)}/${fn:escapeXml(designatedDonation.prjImage)}" alt="" style="width:30px;height:30px;" />
										<c:out value="${designatedDonation.prjSubject}"/>
									</a>
								</td>
								<td>
									<c:out value="${op:numberFormat(designatedDonation.cntrCnt)}"/>건
								</td>
								<td>
									<c:out value="${designatedDonation.prjStDtForm}"/>
								</td>
								<td>
									<c:out value="${designatedDonation.prjEdDtForm}"/>
								</td>
								<td>
									<c:out value="${op:numberFormat(designatedDonation.targetAmt)}"/>원
								</td>
								<td><c:out value="${op:numberFormat(designatedDonation.sumAmt)}"/>원</td>
								<td><c:out value="${op:numberFormat(designatedDonation.rateAmtStr)}"/>%</td>
								<td><c:out value="${designatedDonation.displayFlag == 'Y' ? '공개' : '비공개'}"/></td>
								<td><c:out value="${designatedDonation.dsgncntrPartName}"/></td>
								<td>
									<div class="flex_box juc-center gap-08">
										<a href="/opmanager/designated-donation/list/notice/${fn:escapeXml(designatedDonation.prjId)}" class="btn btn-gradient btn-xs">공지사항</a>
									</div>
								</td>
								<td>
									<div class="flex_box juc-center gap-08">
										<a href="javascript:showLog('${fn:escapeXml(designatedDonation.prjId)}');" class="btn btn-gradient btn-xs">승인이력</a>
									</div>
								</td>
								<td>
									<div class="flex_box juc-center gap-08">
										<button type="button" class="btn btn-gradient btn-xs" onclick="javascript:openPreview('${fn:escapeXml(designatedDonation.prjId)}')">미리보기</button>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<c:if test="${empty designatedDonationList}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

            <div class="flex_box juc-sbt">
                <div class="btn_all">
                    <div class="flex_box gap-08">
                    	<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4') || op:hasRole('ROLE_ADMIN_5')}">
                        	<button type="button" id="update_list_data_sold_out" onclick="updateListDataLabel('2')" class="btn btn-default btn-mini">진행(승인처리)</button>
                    	</c:if>
                        <button type="button" id="update_list_data_sold_out" onclick="updateListDataLabel('9')" class="btn btn-default btn-mini">종료</button>
                        <button type="button" id="update_list_data_display" onclick="updateListDataLabel('Y')" class="btn btn-default btn-mini">공개</button>
                        <button type="button" id="update_list_data_display" onclick="updateListDataLabel('N')" class="btn btn-default btn-mini">비공개</button>

                        <!-- <button type="button" id="update_list_data_sale_off" onclick="updateListDataLabel('90')" class="btn btn-default btn-mini">판매종료처리</button>


                        <button type="button" id="update_list_data_resale" class="btn btn-dark-gray btn-sm">품절해제</button>

                        <button type="button" id="update_list_data_add_category" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-plus"></span> ${op:message('M00789')}</button> <!-- 카테고리추가 -->

                    </div>
                </div>
                <div class="btn_all">
                	<div class="flex_box gap-08">
	                	<button type="button" onclick="javascript:excelDownload();" class="btn btn-default btn-mini">엑셀</button>
	                    <button type="button" onclick="location.href='/opmanager/designated-donation/form'" class="btn btn-default btn-mini">신규등록</button>
	                </div>
                </div>
            </div>

			<%-- <div class="flex_box juc-sbt">
				<div class="btn_all">
                	<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
	                </div>
				</div>
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
							<button type="button" class="btn btn-dark-gray btn-mini finishing-remittance">등록</button>
						</c:if>
					</div>
				</div>
			</div> --%>

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
	function excelDownload() {
		Shop.downloadExcelOrder("/opmanager/designated-donation/list/excel-download", $('#designatedDonationSearchParam').serialize(), false);
	}

	var changeYn = "N";

	$("select[name='upperLocgovCode']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	})

	window.addEventListener("unload", (event) => {		// 화면 닫을 때 팝업 같이 닫기(화면 이동시 팝업만 남아있는 상황 방지)
		if (popup) {
			popup.close();
		}
	});;

	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="prjStDt"]' , 'input[name="prjEdDt"]');
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
        let message = "정보를 "+"${op:message('M00365')}";
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

	let popup;
	let popupType = "toolbar=no,width=1510,height=700,top=150px,left=250px,directories=no,menubar=no,scrollbars=yes,location=no";

	function showLog(prjId) {
		popup = window.open('/opmanager/designated-donation/popup-confirm-log/' + prjId, 'dsgncntrConfirmPopup', popupType);
	}

	function openPreview(prjId){
		Common.popup('/opmanager/designated-donation/preview?prjId='+prjId, "preview", 1600, 800, 0, 0, 0)
	}



</script>


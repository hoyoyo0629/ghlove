<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

			<div class="location">
				<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
			</div>


			<div class="item_list">
				<h3><span>입점업체리스트</span></h3>
				<form:form modelAttribute="sellerParam" method="post" enctype="multipart/form-data">
					<div class="board_write">
						<table class="board_write_table" summary="입점업체관리">
							<caption>입점업체관리</caption>
							<colgroup>
								<col style="width:150px;" />
								<col style="width:auto;" />
								<col style="width:150px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
								<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
								<tr>
			                        <td class="label">지자체</td>
			                        <td colspan="3">
			                            <div class="flex_box gap-08">
				                           <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
							                    <form:option value="">-${op:message('M00039')}-</form:option>
							                    <c:forEach items="${wdr}" var="wdr">
							                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
							                    </c:forEach>
							                </form:select>
				                            <form:select path="shLocgovCode" class="wd-150">
							                    <option value="">-시,군,구-</option>
							                </form:select>
			                            </div>
			                        </td>
			                    </tr>
			                    </c:if>
								<tr>
									<td class="label"><c:out value="${op:message('M00011')}"/></td> <!-- 검색구분 -->
								 	<td colspan="3">
								 		<div class="flex_box gap-08">
											<form:select path="where" title="${op:message('M00468')}" class="wd-150"> <!-- 키워드선택 -->
												<%-- <form:option value="" label="${op:message('M00039')}" /><!-- 전체 --> --%>
												<form:option value="COMPANY_NAME" label="${op:message('M01635')}" />  <!-- 상호명 -->
												<form:option value="LOGIN_ID" label="${op:message('M00081')}" />  <!-- 아이디 -->
												<form:option value="REPRESENTATIVE_NAME" label="${op:message('M00112')}" />  <!-- 대표자명 -->
												<form:option value="PHONE_NUMBER" label="${op:message('M00155')}" />  <!-- 휴대폰 -->
											</form:select>
											<form:input type="text" path="query" class="input_txt required _filter full" title="${op:message('M00011')}" />
										</div>
								 	</td>
								</tr>
								<tr>
								 	<td class="label"><c:out value="${op:message('M00202')}"/></td>
								 	<td colspan="3">
								 		<div>
								 			<span class="datepicker"><form:input path="startDate" class="term datepicker" maxlength="8" title="등록일 시작일" /><!-- 등록일 시작일 --></span>
											<span class="wave">~</span>
											<span class="datepicker"><form:input path="endDate" class="term datepicker" maxlength="8" title="등록일 종료일" /><!-- 등록일 종료일 --></span>
											<span class="day_btns">
												<a href="javascript:;" class="btn_date today"><c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
												<a href="javascript:;" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
												<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
												<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
												<a href="javascript:;" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
                                                <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
											</span>
										</div>
								 	</td>
								</tr>
								<tr>
								 	<td class="label"><c:out value="${op:message('M00082')}"/></td> <!-- 사용여부 -->
								 	<td>
								 		<div class="flex_box gap-12">
								 			<div class="input-form">
									 			<form:radiobutton path="statusCode" value="" label="전체" checked="checked"/>
									 		</div>
									 		<div class="input-form">
												<form:radiobutton path="statusCode" value="2" label="사용" />
											</div>
											<div class="input-form">
												<form:radiobutton path="statusCode" value="3" label="중지" />
											</div>
											<div class="input-form">
												<form:radiobutton path="statusCode" value="5" label="승인대기" />
											</div>
										</div>
								 	</td>
								 	<td class="label">권한</td>
								 	<td>
								 		<div class="flex_box gap-12">
								 			<div class="input-form">
									 			<form:radiobutton path="itemApprovalType" value="" label="전체" checked="checked"/>
									 		</div>
									 		<div class="input-form">
												<form:radiobutton path="itemApprovalType" value="2" label="자동" />
											</div>
											<div class="input-form">
												<form:radiobutton path="itemApprovalType" value="1" label="승인" />
											</div>
										</div>
								 	</td>
								 </tr>
							</tbody>
						</table>
					</div> <!-- // board_write -->
					<div class="btn_all btn_left">
						<ul class="list-bullet point">
					        <li>
					            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
					        </li>
				        </ul>
				  	</div>

					<!-- 버튼시작 -->
					<div class="btn_all btn_right">
						<div class="flex_box gap-08">
							<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/seller/list';"> <c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
							<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/></button> --%> <!-- 검색 -->
							<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
						</div>
					</div>
					<!-- 버튼 끝-->

					<div class="count_title mt-40">
						<h5>
							<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
						</h5>
						<span>
							<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
								onchange="$('form#sellerParam').submit();"> <!-- 화면 출력수 -->
								<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
								<form:option value="20" label="20${op:message('M00053')}" /> <!-- 개 출력 -->
								<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
								<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
							</form:select>
						</span>
					</div>
				</form:form>
				<div class="board_list">
					<form id="listForm">
						<table class="board_list_table" summary="전체업체리스트">
							<caption>전체업체리스트</caption>
							<colgroup>
                                <col style="width:50px;">
                                <col style="width:50px;">
                                <c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
                                <col style="width:200px;">
                                </c:if>
                                <col style="width:100px;">
                                <col style="width:100px;">
                                <col style="width:200px;">
                                <col style="width:150px;">
                                <col style="width:150px;">
                                <col style="width:100px;">
                                <col style="width:100px;">
                                <col style="width:150px;">
                                <col style="width:150px;">
                                <col style="width:150px;">
                                <col style="width:150px;">
                                <col style="width:150px;">
							</colgroup>
							<thead>
								<tr>
									<th scope="col"><input type="checkbox" id="check_all" title="체크박스" /></th>
									<th scope="col">No</th> <!-- No -->
									<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
									<th scope="col">지자체</th> <!-- 지자체 -->
									</c:if>
									<th scope="col"><c:out value="${op:message('M00088')}"/><c:out value="${op:message('M00001')}"/></th> <!-- 등록상품 -->
									<th scope="col"><c:out value="${op:message('M00081')}"/></th> <!-- 아이디 -->
									<th scope="col"><c:out value="${op:message('M00104')}"/> <!-- 상호명 --></th>
									<th scope="col"><c:out value="${op:message('M00112')}"/> <!-- 대표자명 --></th>
									<th scope="col">휴대폰 </th>
									<th scope="col"><c:out value="${op:message('M01476')}"/></th> <!-- 상태 -->
									<th scope="col">권한 </th>
									<th scope="col">공동인증서등록여부 </th>
									<th scope="col">금융인증서등록여부 </th>
									<th scope="col">본인인증등록여부 </th>
									<th scope="col"><c:out value="${op:message('M00202')}"/> <!-- 등록일 --></th>
									<th scope="col"><c:out value="${op:message('M00796')}"/> <!-- 로그인 --></th>
								</tr>
							</thead>
							<tbody>

							<c:forEach items="${list}" var="seller" varStatus="i">
								<tr style="background:#fff;">
									<!-- checkBox -->
									<td><input type="checkbox" name="id" value="${fn:escapeXml(seller.sellerId)}"/></td>
									<!-- 넘버링 -->
									<td>
										<div>
											<c:choose>
												<c:when test="${itemParam.orderBy == 'ORDERING' && itemParam.sort == 'ASC'}">
													<c:out value="${pagination.number + i.count}"/>
												</c:when>
												<c:otherwise>
													<c:out value="${pagination.itemNumber - i.count}"/>
												</c:otherwise>
											</c:choose>
										</div>
									</td>
									<!-- 지자체 -->
									<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
									<td>
										<div>
											<c:out value="${seller.locgovNm}"/>
										</div>
									</td>
									</c:if>
									<!--  -->
                                    <td>
                                        <div class="flex_box juc-center gap-08">
                                        	<c:set var="now" value="<%=new java.util.Date()%>" />
											<c:set var="today"><fmt:formatDate value="${now}" pattern="yyyyMMdd" /></c:set>
                                        	<a href="/opmanager/item/list?sellerId=${fn:escapeXml(seller.sellerId)}&searchStartDate=20221001&searchEndDate=${today}" class="btn btn-default btn-sm">보기</a>
                                        </div>
                                    </td>
									<td>
										<div>
											<a href="/opmanager/seller/edit/${fn:escapeXml(seller.sellerId)}"><c:out value="${seller.loginId}"/></a>
										</div>
									</td>
									<td>
										<div>
											<c:out value="${seller.companyName}"/>
										</div>
									</td>
									<td>
										<div>
											<c:out value="${seller.representativeName}"/>
										</div>
									</td>
									<td>
										<div>
											<c:out value="${seller.phoneNumber}"/>
										</div>
									</td>
									<td>
										<div>
											<c:choose>
												<c:when test="${seller.statusCode == 2}">
													<c:out value="${op:message('M00083')}"/>
												</c:when>
												<c:when test="${seller.statusCode == 3}">
													중지
												</c:when>
												<c:when test="${seller.statusCode == 5}">
													승인대기
												</c:when>
											</c:choose>
										</div>
									</td>
									<td>
										<div>
											<c:choose>
												<c:when test="${seller.itemApprovalType == 1}">
													승인
												</c:when>
												<c:when test="${seller.itemApprovalType == 2}">
													자동
												</c:when>
											</c:choose>
										</div>
									</td>
									<!-- 공동 인증서 등록 여부 -->
									<td>
										<div>
											<c:out value="${seller.mberDn}"/>
										</div>
									</td>
									<!-- 금융인증서 등록 여부 -->
									<td>
										<div>
											<c:out value="${seller.mberFinDn}"/>
										</div>
									</td>
									<!-- 본인인증 여부 -->
									<td>
										<div>
											<c:out value="${seller.mberCi}"/>
										</div>
									</td>
									<td>
										<div>
											<c:out value="${op:date(seller.createdDate)}"/>
										</div>
									</td>
									<td>
										<div class="flex_box juc-center gap-08">
											<c:if test="${seller.statusCode == 2}">
												<c:choose>
													<c:when test="${adminRole == 'LOC' || op:hasRole('ROLE_ADMIN_CALL')}">
														<a href="/opmanager/seller/shadow-login?sellerId=${fn:escapeXml(seller.sellerId)}" class="btn btn-default btn-sm"><c:out value="${op:message('M00796')}"/></a>
													</c:when>
													<c:otherwise>

													</c:otherwise>
												</c:choose>
											</c:if>
										</div>
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
					<br />
					<div class="btn_all btn_right">
						<div class="flex_box gap-08">
							<button type="button" id="insert_company" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00088')}"/> </button> <!-- 등록 -->
							<!-- <button type="button" id="delete_company" class="btn btn-defualt btn-mini"> <c:out value="${op:message('M00074')}"/> </button>  삭제 -->
						</div>
					</div>

					<div class="pagination-wrap">
						<page:pagination-manager />
					</div>

				</div> <!-- // board_list -->

			</div>

<style>
td {background: #fff;}
.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
opacity: .5;}



</style>
<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();

        // 업체등록
        $('#insert_company').on('click', function() {

	    	// 지자체 관리자만 등록가능
	    	if('${fn:escapeXml(adminRole)}' == 'SYS') {
	    		alert("업체등록권한이 없습니다.");
	    		return false;
	    	}

        	location.href = "/opmanager/seller/create";
        });

        // 업체삭제
        $('#delete_company').on('click', function() {

	    	// 지자체 관리자만 등록가능
	    	if('${fn:escapeXml(adminRole)}' == 'SYS') {
	    		alert("업체삭제권한이 없습니다.");
	    		return false;
	    	}

            var $form = $('#listForm');
            if ($form.find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }

            Common.updateListData("/opmanager/seller/list/delete", Message.get("M00196"), function () {	// 삭제하시겠습니까?
            	alert(Message.get("M00205"));	// 삭제 되었습니다.
            	location.reload();
           });
        });


	});

	$(function() {
	    wdrChange($("#shWdr").val());
	});

	var changeYn = "N";

	// 지차체 변경여부 체크
	$("select[name='shWdr']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	});
	 // 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();

		$("#shLocgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$.post(url("/opmanager/item/options-by-locgovCode"), {'code' : value}, function(response) {

				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#shLocgovCode').append(options);
		        }

				// 조회 된 값 유지
				if ("${fn:escapeXml(sellerParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(sellerParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}

		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		var strStartDate = $("#startDate").val();
		var strEndDate = $("#endDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#endDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		$("#sellerParam").submit();
	}
</script>
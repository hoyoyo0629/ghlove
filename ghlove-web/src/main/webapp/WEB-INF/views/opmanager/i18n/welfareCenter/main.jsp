<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<div class="item_list">
    <h3 class="custom">행정복지센터 관리</h3>

	<form:form modelAttribute="wlfrCntrMngParam" method="get">

	    <div class="item_info_wrap">
	        <div class="item_list mt70">
	            <div class="board_write">
	                <table class="board_write_table" summary="행정복지센터 관리 목록">
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
	                            <td class="label"><span class="required_mark"></span>지자체</td>
								<td colspan="3">
								    <div class="flex_box gap-08">
									<c:choose>
									    <c:when test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
										    <c:if test="${wlfrCntrMngParam.lclgvCd.length() > 0}">
										    	${fn:escapeXml(locgovNm)}
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
	                                            <option value="">-시,군,구-</option>
	                                        </form:select>
										</c:otherwise>
                                    </c:choose>
								    </div>
								</td>
	                        </tr>

	                        <tr>
	                            <td class="label "><span class="required_mark"></span>행정복지센터명</td>
	                            <td>
	                                <div class="flex_box gap-12 item-center">
	                                    <form:input path="pbadmsWlfrCntrNm" maxlength="50" class="input_txt _filter wd-500" title="행정복지센터명" autocomplete='off'/>
	                                </div>
	                            </td>
	                            <td class="label"><span class="required_mark"></span>사용여부</td>
	                            <td>
	                                <div class="flex_box gap-12">
										<div class="input-form">
											<form:radiobutton path="useYn" value="" label="전체"  checked="checked" />
										</div>
										<div class="input-form">
										   	<form:radiobutton path="useYn" value="Y" label="사용" />
										</div>
										<div class="input-form">
											<form:radiobutton path="useYn" value="N" label="미사용" />
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
									</span>
								</div>
							</td>
						</tr>
						</tbody>
					</table>

					<div class="btn_all btn_right">
						<div class="flex_box gap-08">
							<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/welfareCenter/main'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
							<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:checkDate(event);"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
						</div>
					</div>
				</div>


				<div class="count_title mt-40">
					<h5>
						<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(wlfrCntrMngParam.pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
					</h5>
					<span>
						<form:select path="itemsPerPage" title="${op:message('M00054')}${op:message('M00052')}"
 							onchange="$('form#wlfrCntrMngParam').submit();"> <!-- 화면 출력수 -->
 							<form:option value="10" label="10${op:message('M00053')}" /> <!-- 개 출력 -->
 							<form:option value="50" label="50${op:message('M00053')}" /> <!-- 개 출력 -->
 							<form:option value="100" label="100${op:message('M00053')}" /> <!-- 개 출력 -->
 							<form:option value="200" label="200${op:message('M00053')}" /> <!-- 개 출력 -->
 							<form:option value="500" label="500${op:message('M00053')}" /> <!-- 개 출력 -->
 						</form:select>
					</span>
				</div>
			</div>
		</div>
	</form:form>

	<div class="board_list">

		<form id="listForm" method="post">
			<table class="board_list_table" summary="행정복지센터 관리 목록">
				<caption>사업부서 관리목록</caption>
				<colgroup>
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
						<th scope="col">ID</th>
						<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
							<th scope="col">지자체 명</th>
						</c:if>
						<th scope="col">행정복지센터명</th>
						<th scope="col">사용여부</th>
						<th scope="col">등록일시</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ list }" var="center" varStatus="index">
						<c:set var="key"><c:out value="${center.pbadmsWlfrCntrId}"/></c:set>
						<tr style="background:#fff;">
							<td>
								<c:out value="${op:numberFormat(key)}"/>
							</td>
							<c:if test="${op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4')}">
								<td><div><c:out value="${center.upperLocgovNm} ${center.locgovNm}"/></div></td>
							</c:if>
							<td>
	                        	<div>
	                        		<a href="/opmanager/welfareCenter/form/${fn:escapeXml(center.pbadmsWlfrCntrId)}">
	                        			<c:out value="${center.pbadmsWlfrCntrNm}"/>
	                       			</a>
	                        	</div>
							</td>
							<td>
								<c:out value="${center.useYn == 'Y' ? '사용' : '미사용'}"/>
							</td>
							<td>
								<fmt:formatDate value="${center.frstRegDt}" pattern="yyyy-MM-dd HH:mm:ss" />
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
                <button type="button" onclick="location.href='/opmanager/welfareCenter/form'" class="btn btn-default btn-mini">신규등록</button>
            </div>
        </div>
	</div>
	 <%-- 페이징 --%>
	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</div>

<script type="text/javascript">

    // 지차체 변경여부 체크
/*	var changeYn = "N";

	$("select[name='upperLocgovCode']").on('focus', function () {

	}).change(function() {
		changeYn = "Y";
	});
*/
	// 검색 실행
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStDt"]' , 'input[name="searchEdDt"]');
		EventHandler.calendarStartDateAndEndDateVaild();

		let upperLocgovCode = '<c:out value="${wlfrCntrMngParam.upperLocgovCode}"/>';

		if (upperLocgovCode) {		// 화면 열때 상위지자체, 하위지자체 세팅
			wdrChange(upperLocgovCode, '${fn:escapeXml(wlfrCntrMngParam.lclgvCd)}');
		}

		try {
			$('#wlfrCntrMng').validator(function() {
				let searchStDt = $("#searchStDt").val();
				let searchEdDt = $("#searchEdDt").val();
	            if (searchStDt && searchStDt.length == 8 && searchEdDt && searchEdDt.length == 8) {
	                var startDate = new Date(searchStDt.substr(0, 4), searchStDt.substr(4, 2), searchStDt.substr(6, 2));
	                var endDate = new Date(searchEdDt.substr(0, 4), searchEdDt.substr(4, 2), searchEdDt.substr(6, 2));

	                if (searchStDt > searchEdDt) {
	                    alert("종료일이 시작일보다 빠릅니다.");
	                    $("#searchEdDt").focus();
	                    return false;
	                }
	                var searchChk = Common.searchDateMonth(searchStDt, searchEdDt);
	                if (!searchChk) return false;
	            }
			});
		} catch (e) {
			console.log(e);
		}
	});


    // 상위 지자체 변경 함수(화면 열때 하위 지자체값 전달, 상위지자체 변경시 하위지자체값 미전달)
	function wdrChange(value, initLclgvCd) {
		$('#lclgvCd').val("");				// 상위 지자체 바뀌면 하위 지자체 값 초기화
		$("#lclgvCd option").remove();

		if ($("#upperLocgovCode").val() != "") {

			$('#lclgvCd').append('<option value="">-전체-</option>');

			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				for (var i = 0; i < response.length; i++) {
					let options = "";
					//if (response[i].LOCGOV_CODE == value) {
					//	options = '<option value="' + response[i].LOCGOV_CODE + '" selected="selected">' + response[i].LOCGOV_NM + '</option>';
		            //} else {
		            	options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            //}

		            $('#lclgvCd').append(options);
		        }

                // 지자체 관리자 값으로 selected
				<%--<c:choose>
	                <c:when test="${adminRole eq 'ROLE_ADMIN_5' || adminRole eq 'ROLE_ADMIN_6'}">
		                $("#upperLocgovCode").val('${fn:escapeXml(wlfrCntrMng.upperLocgovCode)}').prop("selected", true);
		                $("#lclgvCd").val('${fn:escapeXml(wlfrCntrMng.lclgvCd)}').prop("selected", true);
	                </c:when>
	                <c:otherwise> --%>
	                	if (initLclgvCd) {		// 화면 열때 하위 지자체값이 있으면 하위 지자체값 세팅
	    	                $("#lclgvCd").val(initLclgvCd).prop("selected", true);
	                	}
	                <%--</c:otherwise>
	            </c:choose>--%>

		    });
		} else {
	        $('#lclgvCd').append('<option value="">-시,군,구-</option>');
		}
	}


</script>
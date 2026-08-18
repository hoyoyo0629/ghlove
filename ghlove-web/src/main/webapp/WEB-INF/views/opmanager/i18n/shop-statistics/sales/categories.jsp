<%@ page language="java" 	contentType="text/html; charset=UTF-8" 				pageEncoding="UTF-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<script type="text/javascript" src="/content/modules/netfunnel.js"></script>
<script type="text/javascript" src="/content/modules/netfunnel_skin.js"></script>

<style>
	.scroll-wrap {
		overflow-y: auto;
		height: 450px;
	}
	.sticky thead {
		position: sticky;
		top: -1px;
		z-index: 10;
	}
</style>

<div class="location">
	<a href="#">통계</a> &gt; <a href="#">답례품 구매현황</a> &gt; <a href="#" class="on">카테고리별</a>
</div>

<h3><span>카테고리별 통계현황</span></h3>

<form:form modelAttribute="statisticsParam" action="${fn:escapeXml(requestContext.requestUri)}" method="post">
	<div class="board_write">
		<table class="board_write_table" summary="${op:message('M01370')}">
			<caption><c:out value="${title}"/></caption>
			<colgroup>
				<col style="width:220px;" />
				<col />
			</colgroup>
			<tbody>
				<tr>
					<td class="label"><c:out value="${op:message('M01347')}"/></td> <!-- 기간 -->
					<td>
				 		<div>
							<span class="datepicker"><form:input path="startDate" class="term datepicker" title="${op:message('M00024')}" id="dp28" /></span> <!-- 시작일 -->
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="endDate" class="term datepicker" title="${op:message('M00025')}" id="dp29" /></span> <!-- 종료일 -->
							<span class="day_btns">
								<a href="javascript:;" class="btn_date today">  <c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
								<a href="javascript:;" class="btn_date week-1"> <c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
								<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
								<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
								<a href="javascript:;" class="btn_date year-1"> <c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
								<c:if test="${op:hasRole('ROLE_ADMIN_1') or op:hasRole('ROLE_ADMIN_5')}">
									<a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
								</c:if>
							</span>
						</div>
			 		</td>
				</tr>
				<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
	                <tr>
	                    <td class="label">지자체</td>
		                <td>
		                    <div>
		                        <form:select path="shWdr" title="지자체" class="wd-150" onChange="wdrChange(this.value)">
		                            <form:option value="">-시,도 선택-</form:option>
		                            <c:forEach items="${wdr}" var="wdr">
		                                <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
		                            </c:forEach>
		                        </form:select>
		                        <form:select path="shLocgovCode" title="지자체" class="wd-150">
		                            <option value="">-시,군,구-</option>
		                        </form:select>
		                    </div>
		                </td>
	                </tr>
                </c:if>
			</tbody>
		</table>
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
		        <li>
		            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
		        </li>
	        </ul>
	  	</div>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='${requestContext.sellerPage ? '/seller' : '/opmanager'}/shop-statistics/sales/categories'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
				<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button> --%>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search_net();">검색</button>
			</div>
		</div>
	</div>

	<div class="count_title mt-40">
		<h5>
			<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00272')}"/>
		</h5>
	</div>
</form:form>

<div class="board_list scroll-wrap sticky" style="height: 822px;">
    <table class="board_list_table" style="margin-top: 0;">
        <caption></caption>
	    <colgroup>
	        <col style="width:10px;">
	        <col style="width:150px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	        <col style="width:100px;">
	    </colgroup>
	    <thead>
	        <tr style="border-bottom: hidden;">
	            <th scope="col" rowspan="2">No.</th>
	            <th scope="col" rowspan="2">기부지자체광역지역</th>
	            <th scope="col" rowspan="2">기부지자체</th>
	            <th scope="col" colspan="2">수산물</th>
	            <th scope="col" colspan="2">농축산물</th>
	            <th scope="col" colspan="2">지역상품권</th>
	            <th scope="col" colspan="2">생활용품</th>
	            <th scope="col" colspan="2">가공식품</th>
	            <th scope="col" colspan="2">관광서비스</th>
	            <th scope="col" colspan="2"><span style="font-weight: bold;">총계</span></th>
	        </tr>
	        <tr>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	            <th scope="col">건수</th>
	            <th scope="col">금액(원)</th>
	        </tr>
	    </thead>
	    <tbody>
	    	<c:forEach items="${categoriesStatsList}" var="list" varStatus="i">
        		<tr style="background:#fff;">
		            <td>
		                <div><c:out value='${i.count}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.upperLocgovNm}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.locgovNm}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.mpCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.mpAmt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.alpCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.alpAmt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.lvCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.lvAmt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.hgCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.hgAmt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.pfCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.pfAmt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.tsCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.tsAmt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.totalCnt}'/></div>
		            </td>
		            <td>
		                <div><c:out value='${list.totalAmt}'/></div>
		            </td>
	            </tr>
	        </c:forEach>
	    </tbody>
    </table>


    <c:if test="${empty categoriesStatsList}">
        <div class="no_content">
            <c:out value="${op:message('M00473')}"/>
        </div>
    </c:if>

	<!--
    <div class="pagination-wrap">
		<page:pagination-manager />
	</div>
	-->
</div>

<script type="text/javascript">

	$(function() {
		// 캘린더 날짜 선택 이벤트
		Common.AdjustDateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');

		// 상위 지자체 선택에 따른 하위 지자체 옵션 세팅
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
				if ("${fn:escapeXml(statisticsParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(statisticsParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}
		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}

	function search_net() {
		if(Common.useNetfunnel()){
			NetFunnel_Action({ action_id: 'se_login' }, function (ev, ret) {
	        	search();
	        });
		}else{
			search();
		}
	}

	function search() {
		var strStartDate = $("#startDate").val();
		var strEndDate = $("#endDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate 	= Number(strStartDate);
			var endDate		= Number(strEndDate);

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#endDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		$("#statisticsParam").submit();
	}
</script>
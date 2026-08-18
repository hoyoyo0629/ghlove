<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<div class="location">
		<a href="#">통계</a> &gt;  <a href="#">매출통계</a> &gt; <a href="#" class="on">비활동회원 통계</a>
	</div>
	<div class="statistics_web">
		<h3><span><c:out value="${op:message('M01411')}"/></span></h3> <!-- 비활동회원 통계 -->
		<form:form modelAttribute="statisticsParam" method="post" >
			<form:hidden path="query"/>
			<div class="board_write">
				<table class="board_write_table" summary="${op:message('M01411')}"> <!-- 비활동회원 통계 -->
					<caption><c:out value="${op:message('M01411')}"/></caption> <!-- 비활동회원 통계 -->
					<colgroup>
						<col style="width: 20%;">
						<col style="width: auto;">
					</colgroup>
					<tbody>
						<tr>
							<td class="label"><c:out value="${op:message('M01412')}"/></td> <!-- 최종결제일 -->
							<td>
						 		<div>
									<span class="datepicker"><form:input path="startDate" class="term datepicker" title="${op:message('M00507')}" id="dp28" /></span> <!-- 시작일 -->
									<span class="wave">~</span>
									<span class="datepicker"><form:input path="endDate" class="term datepicker" title="${op:message('M00509')}" id="dp29" /></span> <!-- 종료일 -->
									<span class="day_btns">
										<a href="javascript:;" class="btn_date today"><c:out value="${op:message('M00026')}"/></a><!-- 오늘 -->
										<a href="javascript:;" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a><!-- 1주일 -->
										<a href="javascript:;" class="btn_date day-15"><c:out value="${op:message('M00028')}"/></a><!-- 15일 -->
										<a href="javascript:;" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a><!-- 한달 -->
										<a href="javascript:;" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a><!-- 3개월 -->
										<a href="javascript:;" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a><!-- 1년 -->
									</span>
								</div>
					 		</td>
						</tr>
						<tr>
							<td class="label">매출건수</td>
							<td>
								<div>
									<form:input path="salesCount" class="_number" title="매출건수"/> 이하
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">매출금액</td>
							<td>
								<div>
									<form:input path="salesPrice" class="_number" title="매출금액"/> 이하
								</div>
							</td>
						</tr>
					</tbody>
				</table>

			</div> <!-- // board_write -->

			<div class="btn_all">
				<!-- <div class="btn_left">
					<button type="button" class="btn btn-dark-gray btn-sm"><span>초기화</span></button>
				</div> -->
				<div class="btn_right">
					<button type="submit" class="btn btn-dark-gray btn-sm"><span><c:out value="${op:message('M00048')}"/></span></button> <!-- 검색 -->
				</div>
			</div>
		</form:form>

		<div class="board_list mt30">

			<table class="board_list_table">
				<caption>쿠폰리스트</caption>
				<colgroup>
					<col style="width: 15%;">
					<col style="width: 15%;">
					<col style="width: 35%;">
					<col style="width: 10%;">
					<col style="width: 15%;">
					<col style="width: 5%;">
					<col style="width: 5%;">
				</colgroup>
				<thead>
					<tr>
						<th><c:out value="${op:message('M01356')}"/></th> <!-- 회원이름 -->
						<th class="border_left"><c:out value="${op:message('M00125')}"/> (<c:out value="${op:message('M00081')}"/>) </th> <!-- 이메일 --><!-- 아이디 -->
						<th class="border_left"><c:out value="${op:message('M00118')}"/></th> <!-- 주소 -->
						<th class="border_left"><c:out value="${op:message('M00016')}"/></th> <!-- 전화번호 -->
						<th class="border_left"><c:out value="${op:message('M00219')}"/></th> <!-- 가입일 -->
						<th class="border_left">매출건수</th>
						<th class="border_left">매출금액</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${userList}" var="list">
						<tr>
							<td>
								<c:out value="${list.userName }"/>
							</td>
							<td class="border_left" ><c:out value="${list.email}"/></td>
							<td class="border_left" style="text-align: left;">(<c:out value="${list.userDetail.post}"/>) <c:out value="${list.userDetail.dodobuhyun}"/> <c:out value="${list.userDetail.address }"/> <c:out value="${list.userDetail.addressDetail }"/></td>
							<td class="border_left"><c:out value="${list.userDetail.telNumber}"/></td>
							<td class="border_left"><c:out value="${op:datetime(list.creationDate)}"/></td>
							<td class="border_left number"><c:out value="${list.payCount }"/></td>
							<td class="border_left number"><c:out value="${ op:numberFormat(list.totalItemPrice) }"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<c:if test="${empty userList }">
				<div class="no_content">
					<p>
						<c:out value="${op:message('M00591')}"/> <!-- 등록된 데이터가 없습니다. -->
					</p>
				</div>
			</c:if>

			<div class="sort_area">
				<div class="right">
					<a href="/opmanager/shop-statistics/no-user/excel-download?${fn:escapeXml(queryString)}" class="btn_write gray_small"><img src="/content/opmanager/images/icon/icon_excel.png" alt=""><span><c:out value="${op:message('M00254')}"/></span> </a> <!-- 엑셀 다운로드 -->
				</div>
			</div>

		<page:pagination-manager />
		</div>

	</div>


<script type="text/javascript">
	$(function(){
		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="startDate"]' , 'input[name="endDate"]');
	});
</script>
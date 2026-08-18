<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


<h2>
	<span>
	¶ <c:out value=""/>${op:message('M00873')} &nbsp;
		[
		<c:out value="${op:message('M00875')}"/> <!-- 지급된 총 쿠폰 수 --> : <span><c:out value="${couponCount.totalCount}"/></span> <c:out value="${op:message('M00853')}"/> <!-- 장 -->  |
		<c:out value="${op:message('M01417')}"/> <!-- 보유한 쿠폰 수 --> : <span class="red_b"><c:out value="${couponCount.downloadCouponCount}"/></span> <c:out value="${op:message('M00853')}"/> <!-- 장 -->  |
		<c:out value="${op:message('M00877')}"/> <!-- 사용한 쿠폰 수 --> : <span><c:out value="${couponCount.usedCouponCount}"/></span> <c:out value="${op:message('M00853')}"/> <!-- 장 -->  |
		기간만료 쿠폰수 <!-- 사용한 쿠폰 수 --> : <span><c:out value="${couponCount.expirationCouponCount}"/></span> <c:out value="${op:message('M00853')}"/> <!-- 장 -->
		]
	</span>
</h2>

<div class="mb30">

	<form:form modelAttribute="userCouponParam" cssClass="opmanager-search-form clear" method="post">
		<fieldset>
			<legend class="hidden"><c:out value="${op:message('M00048')}"/></legend>
			<form:hidden path="sort" />
			<form:hidden path="orderBy" />
			<form:hidden path="itemsPerPage"/>

			<div class="board_write">
				<table class="board_write_table">
					<colgroup>
						<col style="width: 150px;" />
						<col style="width: auto;" />
						<col style="width: 150px;" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
					<tr>
						<td class="label"><c:out value="${op:message('M00011')}"/></td>
						<td>
							<div>
								<form:select path="where" title="${op:message('M00211')} ">
									<form:option value="COUPON_NAME">쿠폰명</form:option>
								</form:select>
								<form:input path="query" cssClass="optional seven _filter" title="${op:message('M00211')} " />
							</div>
						</td>
						<td class="label"><c:out value="${op:message('M00011')}"/></td>
						<td>
							<div>
								<form:radiobutton path="couponDataStatusCode" value="" label="전체"/>
								<form:radiobutton path="couponDataStatusCode" value="0" label="사용가능 쿠폰"/>
								<form:radiobutton path="couponDataStatusCode" value="1" label="사용한 쿠폰"/>
								<form:radiobutton path="couponDataStatusCode" value="2" label="기간만료 쿠폰"/>
							</div>
						</td>
					</tr>

					<tr>
						<td class="label">발행일자</td>
						<td colspan="3">
							<div class="search-date">
								<span class="datepicker"><form:input path="searchStartDate" cssClass="datepicker optional " title="${op:message('M00220')}" /></span>
								<span class="wave">~</span>
								<span class="datepicker"><form:input path="searchEndDate" cssClass="datepicker optional " title="${op:message('M00221')}" /></span>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
			</div> <!--// board_write E-->

			<div class="btn_all">
				<div class="btn_left">
					<button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/user/popup/coupon/${fn:escapeXml(userId)}';"><span><c:out value="${op:message('M00047')}"/></span></button> <!-- 초기화 -->
				</div>
				<div class="btn_right">
					<button type="submit" class="btn btn-dark-gray btn-sm"><span><c:out value="${op:message('M00048')}"/></span></button>
				</div>
			</div>	<!--// btn_all E-->
		</fieldset>
	</form:form>

</div> <!-- // f_left -->

<div class="board_write">
	<table class="board_list_table" summary="주문 리스트">
		<caption>주문 리스트</caption>
		<colgroup>
			<col style="width:auto;">
			<col style="width:15%;">
			<col style="width:15%;">
			<col style="width:11%;">
			<col style="width:20%;">
		</colgroup>
		<thead>
		<tr>
			<th scope="col"><c:out value="${op:message('M00879')}"/></th> <!-- 쿠폰명 -->
			<th scope="col"><c:out value="${op:message('M00880')}"/></th> <!-- 발행일자 -->
			<th scope="col"><c:out value="${op:message('M00669')}"/></th> <!-- 사용유무 -->
			<th scope="col"><c:out value="${op:message('M00881')}"/></th> <!-- 사용일자 -->
			<th scope="col"><c:out value="${op:message('M00882')}"/></th> <!-- 유효기간 -->
		</tr>

		</thead>
		<tbody>
		<c:forEach items="${couponList}" var="coupon" varStatus="i">
			<tr>
				<td><c:out value="${coupon.couponName}"/></td>
				<td><c:out value="${op:date(coupon.createdDate)}"/></td>
				<td>
					<c:choose>
						<c:when test="${coupon.couponDataStatusCode == '0'}">
							<span class="icon white blue" style="width : 65px;"><c:out value="${op:message('M00885')}"/></span> <!-- 사용가능 -->
						</c:when>
						<c:when test="${coupon.couponDataStatusCode == '1'}">
							<span class="icon gray" style="width : 65px; color: #fff!important;"><c:out value="${op:message('M00886')}"/></span> <!-- 사용완료 -->
						</c:when>
						<c:when test="${coupon.couponDataStatusCode == '2'}">
							<span class="icon gray" style="width : 65px; color: #fff!important;">기간만료</span>
						</c:when>
					</c:choose>
				</td>
				<td><c:out value="${not empty coupon.couponUseDate ? op:datetime(coupon.couponUseDate) : ''}"/></td>
				<td>
					<c:choose>
						<c:when test="${not empty coupon.couponApplyStartDate && not empty coupon.couponApplyEndDate}">
							<c:out value="${op:date(coupon.couponApplyStartDate)}"/> ~ <c:out value="${op:date(coupon.couponApplyEndDate)}"/>
						</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${empty couponList}">
			<tr class="no_content">
				<td colspan="6"><c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. --></td>
			</tr>
		</c:if>
		</tbody>
	</table>

	<page:pagination-manager/>

</div> <!-- // board_write -->


<script type="text/javascript">
	$(function() {

		Manager.activeUserDetails("coupon");
	});
</script>
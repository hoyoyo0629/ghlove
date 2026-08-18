<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<table class="board_list_table" summary="기부내역">
	<caption>기부내역</caption>
	<colgroup>
		<col style="width:3%;">
		<col style="width:10%;">
	</colgroup>
	<thead>
		<tr>
			<th scope="col">No.</th>
			<th scope="col">기부신청일</th>
			<th scope="col">기부지자체</th>
			<th scope="col">기부형태</th>
			<th scope="col">민간연계기관명</th>
			<th scope="col">기부금액</th>
			<th scope="col">발생포인트</th>
			<th scope="col">납부번호</th>
			<th scope="col">납부일</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty list}">
				<c:forEach items="${list}" var="item" varStatus="i">
					<tr style="background:#fff;">
						<td><c:out value="${pagination.itemNumber - i.count}"/></td>
						<td><c:out value="${item.cntrDe}"/></td>
						<td><c:out value="${item.cntrUpperLocgovNm}"/>&nbsp;<c:out value="${item.cntrLocgovNm}"/></td>
						<td><c:out value="${item.cntrPathNm}"/></td>
						<td>
							<c:choose>
								<c:when test="${not empty item.linkInsttNm}">
									<c:out value="${item.linkInsttNm}"/>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>
						</td>
						<td><fmt:formatNumber value="${fn:escapeXml(item.cntrAmt)}" pattern="#,###.##"/></td>
						<td>
							<c:choose>
								<c:when test="${not empty item.sttemntPayDe}">
									<fmt:formatNumber value="${fn:escapeXml(item.cntrPoint)}" pattern="#,###.##"/>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>
						</td>
						<td><c:out value="${item.elctrnPayNo}"/></td>
						<td><c:out value="${not empty item.sttemntPayDe ? item.sttemntPayDe : '미결제'}"/></td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="9">기부내역이 존재하지 않습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<c:if test="${not empty list}">
	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
</c:if>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>


<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">배송지 관리</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<div class="popup_contents">
		<div class="board_list">
			<table class="board_list_table" summary="배송지 관리">
				<caption>배송지 관리</caption>
				<colgroup>
					<col style="width:150px;">
					<col style="width:200px;">
					<col style="width:400px;">
					<col style="width:200px;">
					<col style="width:200px;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">배송지</th>
						<th scope="col">받는 사람</th>
						<th scope="col">주소</th>
						<th scope="col">휴대폰</th>
						<th scope="col">연락처</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${list.size() > 0}">
							<c:forEach items="${list}" var="item" varStatus="i">
								<tr style="background:#fff;">
									<td>
										<div>
											<c:out value="${item.title}" />
											<c:if test="${item.defaultFlag eq 'Y'}">(기본)</c:if> 
										</div>
									</td>
									<td>
										<div><c:out value="${item.userName}" /></div>
									</td>
									<td>
										<div><c:out value="${item.address}" /> <c:out value="${item.addressDetail}"/></div>
									</td>
									<td>
										<div><c:out value="${item.mobile}" /></div>
									</td>
									<td>
										<div><c:out value="${item.phone}" /></div>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr style="background:#fff;">
								<td colspan="5">데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
		<p class="popup_btns">
			<button type="button" class="btn btn-active" onclick="self.close();">확인</button>
		</p>
	</div>
</div>

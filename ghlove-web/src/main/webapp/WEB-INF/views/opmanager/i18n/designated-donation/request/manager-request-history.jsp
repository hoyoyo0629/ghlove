<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">권한 상태 변경이력</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<div class="popup_contents">
		<div class="board_list">
			<table class="board_list_table" summary="권한 상태 변경이력">
				<caption>권한 상태 변경이력</caption>
				<colgroup>
					<col style="width:50px;">
					<col style="width:150px;">
					<col style="width:300px;">
					<col style="width:100px;">
					<col style="width:200px;">
					<col style="width:200px;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">No</th>
						<th scope="col">권한 요청일</th>
						<th scope="col">신청 구분</th>
						<th scope="col">상태</th>
						<th scope="col">작성자</th>
						<th scope="col">변경일</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${list.size() > 0}">
							<c:forEach items="${list}" var="item" varStatus="i">
								<tr style="background:#fff;">
									<td>
										<div>
											<c:out value="${fn:length(list) - i.index}"/>
										</div>
									</td>
									<td>
										<div>
											<c:out value="${item.frstRegistPnttm}" />
										</div>
									</td>
									<td>
										<div><c:out value="${item.reqstSeNm}" /></div>
									</td>
									<td>
										<div><c:out value="${item.confmSttusNm}" /></div>
									</td>
									<td>
										<c:if test="${item.loginId != null && item.loginId != ''}">
											<div><c:out value="${item.updIdRole}" /></div>
											<div>(<c:out value="${item.loginId}" />)</div>
										</c:if>
									</td>
									<td>
										<c:if test="${item.loginId != null && item.loginId != ''}">
											<div><c:out value="${item.lastUpdtPnttm}" /></div>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr style="background:#fff;">
								<td colspan="6">데이터가 없습니다.</td>
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

<script type="text/javascript">
	
</script>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>



<h3>자유게시판</h3>
<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>


<section id="contents">
	<div class="contents-wrap">
		<div class="center">
			<div class="s-contents list-page-wrapper">
				<div class="list-page-area">
					<div class="list-page-body notice">
						<div class="page-title"
							style="border-top: 1px solid black; margin-top: 50px;">
							<c:out value="${item.subject}"></c:out>
						</div>
						<div class="page-info">
							<div class="page-info-list">
								<div class="date-created"
									style="color: black; padding: 5px; display: flex;">
									<c:set var="formattedDate">
										<fmt:formatDate value="${item.createdDate}"
											pattern="yyyy-MM-dd" />
									</c:set>
									<td><c:out value="${formattedDate}" /></td>
								</div>
								<div class="views" style="color: black; padding: 5px;">
									조회
									<c:out value="${item.hits}"></c:out>
								</div>
								<div style="color: black; padding: 5px;">
									<c:out value="관리자" />
								</div>
							</div>
						</div>
						<div class="page-contents"
							style="min-height: 400px; padding: 53px 15px 53px 15px; text-align: start;">
							${op:nl2br(item.content)}
						</div>
					</div>
				</div>
			</div>
			<!-- 버튼그룹 -->
			<div class="btn-box">
				<button type="button" class="btn btn-dark-gray btn-small"
					onclick="gotolist()">목록</button>
				<c:choose>
					<c:when test="${ item.adminCheck == '1'}">
						<button type="button" class="btn btn-defualt btn-small"
							onclick="location.href='/opmanager/community/locv-faq/edit/${fn:escapeXml(item.id)}'">
							<c:out value="${op:message('M00087')}" />
						</button>
						<button type="button" class="btn btn-defualt btn-small"
							onclick="deleteItem(${fn:escapeXml(item.id)})">
							<c:out value="${op:message('M00074')}" />
						</button>
					</c:when>
				</c:choose>
			</div>




		</div>
	</div>
</section>

<link rel="stylesheet" type="text/css" href="/content/css/community.css">
<script type="text/javascript">

$(function(){

})

function gotolist(){

	location.href = '/opmanager/community/locv-faq/list'
}

function deleteItem(itemId) {
	var message = '게시물을 삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}
	var url = "/opmanager/community/locv-faq/delete/" + itemId
	$.post(url, {}, function(response) {
		Common.responseHandler(response, function() {
			alert("${op:message('M00205')}");
			location.href = '/opmanager/community/locv-faq/list'

		});
	});
}

</script>

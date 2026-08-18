<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<style>
	.searchResult {
		width: 250px;
		position: absolute;
		background: #fff;
		top: 80px;
		border: 1px solid #ccc;
		border-top: none;
		border-radius: 0px 0px 5px 5px !important;
		z-index: 99;
		padding: 12px 16px;
		font-size: 14px;
	}
</style>

		<div class="location">
			<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
		</div>

		<!--${op:message('M00269')} 시작-->
		<h3><span>발송결과</span></h3>
		<c:if test="${mail.isEmailSend()}">
			<div class="board_write" style="padding: 40px 0px;">
				<table class="board_write_table" summary="발송 현황">
				<tbody>
					<tr>
					<td class="label">총 발송 건수</td>
					<td>
						<div><c:out value='${op:numberFormat(cnt.totCnt)}'/>건</div>
					</td>
					<td class="label">성공</td>
					<td>
						<div><c:out value='${op:numberFormat(cnt.succCnt)}'/>건</div>
					</td>
					<td class="label">실패</td>
					<td>
						<div><c:out value='${op:numberFormat(cnt.failCnt)}'/>건</div>
					</td>
					</tr>
				</tbody>
				</table>
			</div>
		</c:if>

		<div class="board_write">
			<table class="board_write_table" summary="${op:message('M00269')}" style="position: relative;">
				<caption>이메일 발송</caption>
				<colgroup>
					<col style="width:220px;">
				</colgroup>
				<tbody>
					<tr>
						<td class="label">발송상태</td>
						<td><div><c:out value="${mail.statusName}" /></div></td>
					</tr>
					<tr>
						<td class="label">발송대상</td>
						<td>
							<c:choose>
								<c:when test="${not empty mail.authList}">
									<c:forEach items="${mail.authList}" var="auth" varStatus="i">
										<div><c:out value="${auth.roleName}" /></div>
									</c:forEach>
								</c:when>
								<c:when test="${mail.authTarget == 'S'}">
									<div>답례품</div>
								</c:when>
								<c:otherwise>
									<div>개별</div>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<c:if test="${mail.isEmailSend()}">
						<tr>
							<td class="label">발송 인원</td>
							<td>
								<div class="board_list">
									<table id="emsList" class="board_list_table">
										<caption>발송 명단</caption>
										<thead>
											<tr>
												<th>이름</th>
												<th>이메일</th>
												<th>실발송 시간</th>
												<th>발송 여부</th>
											</tr>
										</thead>
										<tbody></tbody>
									</table>
									<div class="pagination-wrap">
										<p class="pagination op-pagination" id="page">
										</p>
										<p></p>
									</div>
								</div>
							</td>
						</tr>
					</c:if>
					<tr>
						<td class="label">발송 요청 일시</td>
						<td>
							<div><c:out value="${op:datetime(mail.sendDate)}" /></div>
						</td>
					</tr>
					 <tr>
					 	<td class="label"><c:out value="${op:message('M00275')}"/></td>
					 	<td>
							<div><c:out value="${mail.subject}" /></div>
					 	</td>
					 </tr>
					 <tr>
						<td class="label"><c:out value="${op:message('M01699')}"/></td>
						<td>
							<div>
								<c:choose>
									<c:when test="${not empty mail.fileList}">
										<c:forEach items="${mail.fileList}" var="file" varStatus="i">
											<a href="<c:url value='/opmanager/email/download/${file.emailFileId}' />"><c:out value="${file.orgFileName}"  /></a>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<div>없음</div>
									</c:otherwise>
								</c:choose>
						   </div>
						</td>
					</tr>
					 <tr>
					 	<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00006')}"/></td>
					 	<td>
							<!-- smart_editor2 area -->
			                <div>
			                    <textarea id="content" name="content" cols="30" rows="20" class="editor-content require" title="${op:message('M00006')}"><c:out value="${mail.content}" /></textarea>
			                </div>
			                <!-- smart_editor2 area -->
					 	</td>
					 </tr>
				</tbody>
			</table>

			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
 				<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/email/list'">목록</button>

 				</div>
			</div>

		</div> <!-- // board_write -->

	<!--// ${op:message('M00269')} 끝-->
	<module:smarteditorInit />
	<module:smarteditor id="content" />


<script type="text/javascript">

var sendUserList = [];

$(function() {
	getEmsUserList(1);

});

function getEmsUserList(page) {
	$.post('/opmanager/email/${fn:escapeXml(mail.emailId)}', {page : page}, function (response) {

		if (response.isSuccess) {
			var d = response.data;
			var regExp = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
			if (d.count > 0) {
				var html = "";
				for (var i in d.list) {
					var u = d.list[i];
					html += '<tr>';
					html += '	<td>' + u.userName + '</td>';
					html += '	<td>' + u.email + '</td>';
					html += '	<td>' + u.processedDate.replace(regExp, '$1년$2월$3일 $4시$5분') + '</td>';
					html += '	<td>' + u.resultNm + '</td>';
					html += '</tr>';
				}
				$("#emsList").find('tbody').html(html);

				drawPage(d.pagination);

			} else {

			}

		} else {
			alert("요청 중 오류가 발생하였습니다.");
			return false;
		}

	})
}

function drawPage(page) {

	var html = "";
	var length = page.itemsPerPage;
	var idx = 1;

	if (page.itemsPerPage > page.totalPages) {
		length = page.totalPages;
	}else if (page.currentPage - 4 <= 1) {
		length = page.itemsPerPage;
	} else if (page.currentPage + 5 >= page.totalPages)  {
		length = page.totalPages;
		idx = page.totalPages - page.itemsPerPage + 1;
	} else {
		length = page.currentPage + 5;
		idx = page.currentPage - 4;
	}

	for (var i = idx; i <= length; i++) {
		if (page.currentPage == i) {
			html += '<strong>' + i + '</strong>';
		} else {
			html += '<a href="javascript:;" onclick="getEmsUserList(' + i + ')">' + i + '</a>';
		}
	}

	if (page.currentPage != 1) {
		var prev = '<a href="javascrpt:;" onclick="getEmsUserList(1)" class="first"><img src="/content/images/btn/btn_first.gif" alt="First Page" /></a>';
		prev += '<a href="javascrpt:;" onclick="getEmsUserList(' + (page.currentPage - 1) + ')" class="prev"><img src="/content/images/btn/btn_prev.gif" alt="Previous Page" /></a>';

		html = prev + html;
	}

	if (page.currentPage != page.totalPages) {
		var next = '<a href="javascript:;" onclick="getEmsUserList(' + (page.currentPage + 1) + ')" class="next"><img src="/content/images/btn/btn_next.gif" alt="Next Page" /></a>';
		next += '<a href="javascript:;" onclick="getEmsUserList(' + page.totalPages + ')" class="last"><img src="/content/images/btn/btn_last.gif" alt="Last Page" /></a>';

		html = html + next;
	}

	$("#page").html(html);

}


</script>


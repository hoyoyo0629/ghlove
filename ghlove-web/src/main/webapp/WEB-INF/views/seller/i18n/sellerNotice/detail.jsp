<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


		<div class="location">
			<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
		</div>

		<input type="hidden" id="noticeId" value="${fn:escapeXml(detail.noticeId)}"/>
		<section id="contents">
			<div class="contents-wrap">
				<div class="center">
					<div class="s-contents list-page-wrapper">

						<!-- 게시글 영역 -->
						<div class="list-page-area">
							<div class="list-page-body notice">
								<div class="page-title" style="border-top: 1px solid black; margin-top: 50px; text-align: left;"><c:out value="${detail.subject}"></c:out></div>

								<!-- 게시글 헤더 영역 -->
								<div class="page-info">
									<div class="page-info-list">
										<span style="color: black; font-size: 14px; padding: 5px;">작성자 : </span>
										<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">시스템 관리자</span>
									</div>
									<div class="page-info-list">
										<span style="color: black; font-size: 14px; padding: 5px;">작성일시 : </span>
										<div class="date-created" style="color: black; padding: 5px; display: flex;">
											<fmt:formatDate value="${detail.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" />
										</div>
										<div class="views" style="color: black; padding: 5px;">조회 <c:out value="${detail.hits}"></c:out></div>
									</div>
								</div>
								<!-- 게시글 헤더 영역 -->

								<!-- 게시글 본문 영역 -->
								<div class="page-contents" style="min-height: 400px; padding: 53px 15px 53px 15px; text-align: start;">
									<c:out value="${op:nl2br(detail.content)}" escapeXml="false" />
								</div>
		                     <div style="padding: 10px; border-bottom:1px solid #000; border-top:1px solid #dfdede; display:flex;">
		                       <div class="file-list-label" style=" line-height: 2.0; margin-right:20px;">
		                       		<img  src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료"> 첨부자료
		                       </div>
		                       <div class="file-list-title">
		                         <c:if test="${!empty fileList}">
		             	         	<c:forEach items="${fileList}" var="file" varStatus="i">
									   <div class="file_camera" id="fileId_${fn:escapeXml(file.fileId)}">
								       	 <a href="javascript:downloadItemImage(${fn:escapeXml(file.fileId)});" ><c:out value="${file.orgnlAtchFileNm}"/></a>
								       	 <span style="padding:3px">(<c:out value="${filesize}"/>)</span>
								  	    </div>
									</c:forEach>
								</c:if>
		     				  </div>
		    		  		 </div>
								<!-- 게시글 본문 영역 -->
							</div>
						</div>
						<!-- 게시글 영역 -->
					</div>
					<div class="btn-box">
						<button type="button" onclick="noticeList();"class="btn btn-defualt btn-small list">목록</button>
					</div>
				</div>
			</div>
		</section>

 		 <form id="noticeParam" method="get">
		 	<input type="hidden" name="where" value="${fn:escapeXml(noticeParam.where)}">
		 	<input type="hidden" name="query" value="${fn:escapeXml(noticeParam.query)}">
		 	<input type="hidden" name="sort" value="${fn:escapeXml(noticeParam.sort)}">
		 	<input type="hidden" name="orderBy" value="${fn:escapeXml(noticeParam.orderBy)}">
		 	<input type="hidden" name="itemsPerPage" value="${fn:escapeXml(noticeParam.itemsPerPage)}">
		 </form>

<link rel="stylesheet" type="text/css" href="/content/css/community.css">
<script type="text/javascript">

$(function() {


});


function downloadItemImage(fileId) {
	location.href = '/seller/sys-notice/file-download/'+fileId
}

function noticeList() {
	location.href = "/seller/sys-notice/list";
}

</script>


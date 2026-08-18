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

<h3><span></span></h3>
<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<input type="hidden" id="bbsId" value="${fn:escapeXml(detail.bbsId)}"/>
<section id="contents">
	<div class="contents-wrap">
		<div class="center">
			<div class="s-contents list-page-wrapper">

				<!-- 게시글 영역 -->
				<div class="list-page-area">
					<div class="list-page-body notice">
						<div class="page-title" style="border-top: 1px solid black; margin-top: 50px; text-align: left;"><c:out value="${detail.bbsTtl}"></c:out></div>

						<!-- 게시글 헤더 영역 -->
						<div class="page-info">
							<div class="page-info-list">
								<span style="color: black; font-size: 14px; padding: 5px;">작성자 : </span>
								<c:choose>
									<c:when test="${detail.authority eq 'ROLE_ADMIN_1' || detail.authority eq 'ROLE_ADMIN_2'}">
											<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">시스템 관리자</span>
									</c:when>
									<c:when test="${detail.authority eq 'ROLE_ADMIN_3' || detail.authority eq 'ROLE_ADMIN_4'}">
											<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">행정안전부</span>
											<span style="color: black; font-size: 14px; padding: 5px;"><c:out value="${detail.userName}" /></span>
									</c:when>
									<c:otherwise>
										<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;"><c:out value="${detail.upperLocgovNm}" /> <c:out value="${detail.locgovNm}" /></span>
										<span style="color: black; font-size: 14px; padding: 5px;"><c:out value="${detail.userName}" /></span>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="page-info-list">
								<span style="color: black; font-size: 14px; padding: 5px;">작성일시 : </span>
								<div class="date-created" style="color: black; padding: 5px; display: flex;">
									<fmt:formatDate value="${detail.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" />
								</div>
								<div class="views" style="color: black; padding: 5px;">조회 <c:out value="${detail.inqCnt}"></c:out></div>
							</div>
						</div>
						<!-- 게시글 헤더 영역 -->

						<!-- 게시글 본문 영역 -->
						<div class="page-contents" style="min-height: 400px; padding: 53px 15px 53px 15px; text-align: start;">
							<c:out value="${op:nl2br(detail.bbsCn)}" escapeXml="false" />
						</div>
						<!-- 게시글 본문 영역 -->
					</div>
				</div>
				<!-- 게시글 영역 -->

				<!-- 댓글 영역 -->
				<div>
					<div style="padding: 10px 0px; margin: 10px 0px; font-size: 20px; border-bottom: 1px solid #000">댓글<span style="color: red; margin-left: 10px;">[<c:out value="${bbsCmntListCnt}" />]</span></div>
					<div id="cmntList">
						<c:forEach items="${bbsCmntList}" var="cmnt" varStatus="i">
							<li style="border-bottom: 1px solid #ccc; padding: 15px 10px; margin-bottom: 10px; display:inline-block; width:100%">
								<c:choose>
									<c:when test="${cmnt.authority eq 'ROLE_ADMIN_1' || cmnt.authority eq 'ROLE_ADMIN_2'}">
											<span style="font-size: 14px; font-weight:bold">시스템 관리자</span>
									</c:when>
									<c:when test="${cmnt.authority eq 'ROLE_ADMIN_3' || cmnt.authority eq 'ROLE_ADMIN_4'}">
											<span style="font-size: 14px; font-weight:bold">행정안전부</span>
											<span style="font-size: 14px;"><c:out value="${cmnt.userName}" /></span>
									</c:when>
									<c:otherwise>
										<span style="font-size: 14px; font-weight:bold"><c:out value="${cmnt.upperLocgovNm}" /> <c:out value="${cmnt.locgovNm}" /></span>
										<span style="font-size: 14px;"><c:out value="${cmnt.userName}" /></span>
									</c:otherwise>
								</c:choose>
								<fmt:formatDate value="${cmnt.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" />
								<c:if test="${cmnt.frstCrtId eq userId }">
				                    <button type="button" style="float: right; margin-left:10px; background-color: #fb502e;" class="btn btn-dark-gray btn-sm delete" cmntId="${fn:escapeXml(cmnt.cmntId)}">삭제</button>
									<button type="button" style="float: right;" class="btn btn-dark-gray btn-sm openUpdate" cmntId="${fn:escapeXml(cmnt.cmntId)}">수정</button>
								</c:if>
								<div style="margin-top: 20px; font-size: 13px;"><p> <pre class="cmntCn"><c:out value="${cmnt.cmntCn}" escapeXml="false"/></pre></p></div>
								<div style="position: relative; top: 12px; width: 90%; display:none;" class="updateArea">
									<textarea rows="3" cols="60" maxlength="1000" placeholder="댓글을 입력하세요"><c:out value="${cmnt.cmntCn}" escapeXml="false"/></textarea>
									<button class="commentButton2 update" style="background: #004CCE;" cmntId="${fn:escapeXml(cmnt.cmntId)}">완료</button>
								</div>
							</li>
						</c:forEach>
					</div>
					<div class="comment-wrap">
						<textarea id="cmntCn" class="reply_area" rows="3" cols="60" maxlength="1000" placeholder="댓글을 입력하세요"></textarea>
						<button type="button" class="commentButton add" style="background: #004CCE;">등록</button>
					</div>
				</div>
			</div>
			<div class="btn-box">
				<button type="button" class="btn btn-defualt btn-small list">목록</button>
				<c:if test="${detail.frstCrtId eq userId }">
					<button type="button" class="btn btn-dark-gray btn-small update"> <c:out value="${op:message('M00087')}" /> </button>
					<button type="button" class="btn btn-danger btn-small delete"> <c:out value="${op:message('M00074')}" /> </button>
				</c:if>
			</div>
		</div>
	</div>
</section>
<link rel="stylesheet" type="text/css" href="/content/css/community.css">

<script type="text/javascript" src="/content/modules/cmnty/cmnt.js?<spring:eval expression="@environment.getProperty('jsVersion')" />"></script>
<script type="text/javascript">

$(function(){
	cmnt.init();
})

</script>

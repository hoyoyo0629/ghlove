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
									<c:when test="${detail.authority eq 'ROLE_ADMIN_1' || detail.authority eq 'ROLE_ADMIN_2'}">		<%-- 시스템관리자 --%>
											<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">시스템 관리자</span>
									</c:when>
									<c:when test="${detail.authority eq 'ROLE_ADMIN_3' || detail.authority eq 'ROLE_ADMIN_4'}">		<%-- 행정안전부 --%>
											<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">행정안전부</span>
											<span style="color: black; font-size: 14px; padding: 5px;"><c:out value="${detail.userName}" /></span>
									</c:when>
									<c:when test="${detail.authority eq 'ROLE_ADMIN_7' || detail.authority eq 'ROLE_ADMIN_8'}">
											<span style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">[<c:out value="${detail.bankNm}" />] <c:out value="${detail.psitnNm}" /></span>
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

				<!-- 댓글 영역 -->
				<div>


					<div style="padding: 10px 0px; margin: 10px 0px; font-size: 20px; border-bottom: 1px solid #000">댓글<span style="color: red; margin-left: 10px;">[<c:out value="${offSrBbsCmntListCnt}" />]</span></div>
					<div class="comment-wrap" style="margin: 10px 0px";>
						<textarea id="cmntCn" class="reply_area" rows="3" cols="60" maxlength="1000" placeholder="댓글을 입력하세요"></textarea>
						<button type="button" class="commentButton" onClick="addCmnt()" style="background: #004CCE;">등록</button>
					</div>
					<div id="cmntList" style="border-top: 1px solid #000">
						<c:forEach items="${detail.cmntyOffSrBbsCmntList}" var="cmnt" varStatus="i">
							<li style="border-bottom: 1px solid #ccc; padding: 15px 10px; margin-bottom: 10px; display:inline-block; width:100%">
								<c:choose>
									<c:when test="${cmnt.authority eq 'ROLE_ADMIN_1' || cmnt.authority eq 'ROLE_ADMIN_2'}">
											<span style="font-size: 14px; font-weight:bold">시스템 관리자</span>
									</c:when>
									<c:when test="${cmnt.authority eq 'ROLE_ADMIN_3' || cmnt.authority eq 'ROLE_ADMIN_4'}">
											<span style="font-size: 14px; font-weight:bold">행정안전부</span>
											<span style="font-size: 14px;"><c:out value="${cmnt.userName}" /></span>
									</c:when>
									<c:when test="${cmnt.authority eq 'ROLE_ADMIN_5' || cmnt.authority eq 'ROLE_ADMIN_6'}">
											<span style="font-size: 14px; font-weight:bold"><c:out value="${cmnt.upperLocgovNm}" /> <c:out value="${cmnt.locgovNm}" /></span>
											<span style="font-size: 14px;"><c:out value="${cmnt.userName}" /></span>
									</c:when>
									<c:when test="${cmnt.authority eq 'ROLE_ADMIN_7' || cmnt.authority eq 'ROLE_ADMIN_8'}">
											<span style="font-size: 14px; font-weight:bold">[<c:out value="${cmnt.bankNm}" />] <c:out value="${cmnt.psitnNm}" /></span>
											<span style="font-size: 14px;"><c:out value="${cmnt.userName}" /></span>
									</c:when>
									<c:otherwise>
										<span style="font-size: 14px; font-weight:bold"><c:out value="${cmnt.upperLocgovNm}" /> <c:out value="${cmnt.locgovNm}" /></span>
										<span style="font-size: 14px;"><c:out value="${cmnt.userName}" /></span>
									</c:otherwise>
								</c:choose>
								<fmt:formatDate value="${cmnt.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" />
								<c:if test="${cmnt.frstCrtId eq userId }">

									<button type="button" style="float: right; margin-left:10px; background-color: green;"
									data-bbsid="${fn:escapeXml(cmnt.bbsId)}" data-cmntid="${fn:escapeXml(cmnt.cmntId)}"
									class="btn btn-dark-gray btn-sm fileUpload">파일등록</button>
									<button type="button" style="float: right; margin-left:10px; background-color: #fb502e;" class="btn btn-dark-gray btn-sm delete" cmntId="${fn:escapeXml(cmnt.cmntId)}">삭제</button>
									<button type="button" style="float: right;" class="btn btn-dark-gray btn-sm openUpdate" cmntId="${fn:escapeXml(cmnt.cmntId)}">수정</button>

								</c:if>
								<div style="margin-top: 20px; font-size: 13px;">
									<p>
										 <pre class="cmntCn" style="white-space:pre-wrap; word-wrap:break-word"; ><c:out value="${cmnt.cmntCn}" escapeXml="false"/></pre>
									</p>
								</div>
								<div style="position: relative; top: 12px; width: 90%; display:none;" class="updateArea">
									<textarea rows="3" cols="60" maxlength="1000" placeholder="댓글을 입력하세요"><c:out value="${cmnt.cmntCn}" escapeXml="false"/></textarea>
									<button class="commentButton2 update" style="background: #004CCE;" cmntId="${fn:escapeXml(cmnt.cmntId)}">완료</button>
								</div>
								<c:if test="${!empty cmnt.cmntyOffSrBbsCmntFileList}">
			                     <div style="padding: 10px; display:flex;">
			                       <div class="file-list-label" style=" line-height: 2.0; margin-right:20px;">
			                       		<img  src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료"> 첨부자료
			                       </div>
			                       <div class="file-list-title">

			             	         	<c:forEach items="${cmnt.cmntyOffSrBbsCmntFileList}" var="cmntFile" varStatus="i">
			             	         		<c:if test="${cmntFile.fileId ne 0}">
										   <div class="file_camera" id="fileId_${cmntFile.fileId}">
									       	 <a style="color:black;" href="javascript:downloadCmntItemImage(${fn:escapeXml(cmntFile.fileId)});" ><c:out value="${cmntFile.orgnlAtchFileNm}"/></a>
									       	 <span style="padding:3px">(<c:out value="${cmntFile.fileSize}"/>)</span>
									       	 <span style="padding:3px"><fmt:formatDate value="${cmntFile.frstCrtDt}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
									       	 <c:if test="${cmntFile.frstCrtId eq userId || role eq 'ROLE_ADMIN_1'|| role eq 'ROLE_ADMIN_2'}">
									       	 <a href="javascript:deleteItemImage(${fn:escapeXml(cmntFile.fileId)});">
											<img src="/content/images/btn/file_close.gif" alt="close">
											</a>
											</c:if>
									  	    </div>
									  	    </c:if>
										</c:forEach>

			     				  </div>
			    		  		 </div>
			    		  		 </c:if>
							</li>
							<form:form class="cmntFileUploadForm" id="cmntFileUploadForm${cmnt.cmntId}" method="post" action='/opmanager/community/offSrBbs/cmnt/upload' enctype="multipart/form-data">
							<input type="file" id="cmntFileUpload${cmnt.cmntId}" name="detailImageFile" style="display:none">
							<input type="hidden" name="cmntId" value="${cmnt.cmntId}">
							<input type="hidden" name="bbsId" value="${cmnt.bbsId}">
							</form:form>
						</c:forEach>
					</div>


				</div>
			</div>
			<div class="btn-box">
				<button type="button" class="btn btn-defualt btn-small list">목록</button>
				<c:if test="${detail.frstCrtId eq userId }">
					<button type="button" class="btn btn-dark-gray btn-small"
					onClick="location.href='/opmanager/community/offSrBbs/edit/${detail.bbsId}'">
					<c:out value="${op:message('M00087')}" /> </button>
					<button type="button" class="btn btn-danger btn-small"
					onclick="deleteItem(${fn:escapeXml(detail.bbsId)})"
					> <c:out value="${op:message('M00074')}" /> </button>
				</c:if>
			</div>
		</div>
	</div>
</section>
<link rel="stylesheet" type="text/css" href="/content/css/community.css">

<script type="text/javascript" src="/content/modules/cmnty/offSrBbsCmnt.js?<spring:eval expression="@environment.getProperty('jsVersion')" />"></script>
<script type="text/javascript">

$(function(){
	cmnt.init();
})

function downloadItemImage(fileId) {
	location.href = '/opmanager/community/offSrBbs/file-download/'+fileId
}

function deleteItem(bbsId) {
	var url = "/opmanager/community/offSrBbs/deleteOffSrBbs/" + bbsId
	if(confirm("등록된 게시글을 삭제합니다. 글 삭제 시 댓글도 함께 삭제됩니다.")){
	$.post(url, {}, function(response) {
		Common.responseHandler(response, function() {
			alert(response.data);
			location.href = "/opmanager/community/offSrBbs/list";
		});
	});
	}
}

function addCmnt() {
	let message = '댓글을 등록하시겠습니까?';
	if (!confirm(message)) return;

	let cmntyOffSrBbsCmntDto = {bbsId : $('#bbsId').val(), cmntCn : $('#cmntCn').val()};
	$.post("/opmanager/community/offSrBbs/cmnt/add/", cmntyOffSrBbsCmntDto, function(response) {
		Common.responseHandler(response, function() {
			if(response.isSuccess) {
				alert(response.data);
				location.href = "/opmanager/community/offSrBbs/detail/"+$('#bbsId').val();
			}
		});
	});
}


$(".fileUpload").on("click", function(){
	var cmntFileId = "cmntFileUpload" + $(this).data("cmntid");
	fileUploadPopupOpen(cmntFileId);
})

$(".cmntFileUploadForm").on("change", function(){
	let size = 30 * 1024 * 1024;
	let fileTypes = ['jpg', 'jpeg', 'gif', 'bmp', 'png', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'pdf', 'tif', 'tiff', 'hwp', 'hwpx','zip', '7z'] // 파일 확장자 , 이후에도 확장자 추가 가능

	let file = this[0].files[0];

	let type = file.name.split('.').pop().toLowerCase(); // 파일 확장자 가져오기

	if(file.size > size) {
		alert("첨부파일 용량은 최대 30MB 까지 가능합니다.");
		$(this).find("input[type='file']").val("");
		return false;
	}else if($.inArray(type, fileTypes) == -1) {
		alert("등록할 수 없는 첨부파일 입니다.")
		$(this).find("input[type='file']").val("");
		return false;
	}


	if(confirm("파일을 등록하시겠습니까?")){
		this.submit();
	}else{
		$(this).find("input[type='file']").val("");
		return false;
	}
})

function addFile(obj){

	for (const file of obj.files) {

	}
}

function deleteItemImage(fileId) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'fileId': fileId};
	$.post('/opmanager/community/offSrBbs/cmnt/deleteFile', param, function(response){
		Common.responseHandler(response);
		$("#fileId_"+fileId).remove();
		alert("파일이 삭제되었습니다.");
	});
}

function downloadCmntItemImage(fileId) {
	location.href = '/opmanager/community/offSrBbs/cmnt/file-download/'+fileId
}
</script>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>




<h3><span></span></h3>
<div class="location">
		<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<section id="contents">
   <div class="contents-wrap">
       <div class="center">
           <div class="s-contents list-page-wrapper">
               <div class="list-page-area">
                   <div class="list-page-body notice" style="border-top: 2px solid;">
                     <div class="page-title" style="border-top: 1px solid black; margin-top:50px; text-align:left;">
                         <c:out value="${detail.rpstrTtl}" />
                     </div>
                    <div class="page-info">
                    	<div class="page-info-list">
                    		<span style="color: black; font-size: 14px; padding: 5px;">작성자 : </span>
                    		<c:choose>
                      			<c:when test="${detail.authority eq 'ROLE_ADMIN_1'|| detail.authority eq 'ROLE_ADMIN_2'}">
                      				<span class="writer" style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">시스템 관리자</span>
                      			</c:when>
                      			<c:when test="${detail.authority eq 'ROLE_ADMIN_3'|| detail.authority eq 'ROLE_ADMIN_4'}">
                      				<span class="writer" style="color: black; font-size: 14px; font-weight:bold; padding: 5px;">행정안전부</span>
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
                     <div class="page-contents" style="min-height: 400px; padding: 53px 15px 53px 15px; text-align: start;">
                  		 ${op:nl2br(detail.rpstrCn)}
                     </div >
                     <div style="padding: 10px; border-bottom:1px solid #000; border-top:1px solid #dfdede; display:flex;">
                       <div class="file-list-label" style=" line-height: 2.0; margin-right:20px;">
                       		<img  src="/content/images/icon/cli-icon_file-list.png" alt="첨부자료"> 첨부자료
                       </div>
                       <div class="file-list-title">
                         <c:if test="${!empty fileList}">
             	         	<c:forEach items="${fileList}" var="file" varStatus="i">
							   <div class="file_camera">
						       	 <a href="javascript:downloadItemImage(${fn:escapeXml(file.fileId)});" ><c:out value="${file.orgnlAtchFileNm}"/></a>
						       	 <span style="padding:3px">(<c:out value="${filesize}"/>)</span>
						  	    </div>
							</c:forEach>
						</c:if>
     				  </div>
    		  		 </div>
               </div>
           </div>
       </div>
          <!-- 버튼그룹 -->
           <div class="btn-box">
              <button type="button" class="btn btn-defualt btn-small list" onclick="gotolist()">
                목록
              </button>
              <c:if test="${detail.frstCrtId eq userId }">
					<button type="button" class="btn btn-dark-gray btn-small update"
						onClick="location.href='/opmanager/community/databoard/edit/${detail.rpstrId}'">
						<c:out value="${op:message('M00087')}" />
					</button>
					<button type="button" class="btn btn-danger btn-small delete" onclick="deleteItem(${fn:escapeXml(detail.rpstrId)})"> <c:out value="${op:message('M00074')}" /> </button>
			  </c:if>
           </div>
   </div>
</section>


<link rel="stylesheet" type="text/css" href="/content/css/community.css">
<script type="text/javascript">

$(function(){
	formatBytes();
})

function gotolist(){
	location.href = '/opmanager/community/databoard/list'
}

function deleteItem(rpstrId) {
	var url = "/opmanager/community/databoard/delete/" + rpstrId
	$.post(url, {}, function(response) {
		Common.responseHandler(response, function() {
			alert("${op:message('M00205')}");
			location.href = "/opmanager/community/databoard/list";

		});
	});
}

function downloadItemImage(fileId) {
	location.href = '/opmanager/community/databoard/file-download/'+fileId
}

function deleteItemImage(id, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'itemId': id};
	$.post('/opmanager/qna/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$("#file_camera_"+id).remove();
// 		$(".qnaOpenAnswerFileList").eq(index).remove();
	});
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}


</script>

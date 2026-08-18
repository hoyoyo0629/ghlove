<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

		<div class="location">
			<a href=""><c:out value="${op:message('MENU_5000')}"/></a> &gt; <a href=""></a> &gt; <a href="" class="on">글쓰기</a>
		</div>
		<h3><span>사용자 매뉴얼</span></h3>

		<form:form modelAttribute="manual" method="post" action='${fn:escapeXml(requestContext.requestUri)}' enctype="multipart/form-data">
			<form:hidden path="mnlSn"/>
			<input type="hidden" name="manualParam" id="manualParam" />

		<c:if test="${manual.mnlSn != null}">
		<div class="btn_all btn_right mb15">
		    <div class="flex_box gap-08">
		        <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
		        <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteManual(${fn:escapeXml(manual.mnlSn)})">삭제</button>
		        <button type="button" class="btn btn-dark-default btn-mini" onClick="location.href='/opmanager/manual/user/list'">목록</button>
		    </div>
		</div>
		</c:if>

		<div class="board_write">
	        <table class="board_write_table" summary="">
	            <colgroup>
	                <col style="width:220px;">
	            </colgroup>
	            <tbody>
	                <tr>
	                    <td class="label"><span class="required_mark">*</span>페이지 구분</td>
	                    <td>
	                        <div class="flex_box gap-08">
	                            <form:select path="menuSeCode" title="페이지 구분" class="wd-350">
		                            <c:forEach items="${menuUrlList}" var="menuUrlList">
					                	<option value="${fn:escapeXml(menuUrlList.id)}" label="${fn:escapeXml(menuUrlList.detail)}" />
					                </c:forEach>
	                            </form:select>
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="label"><span class="required_mark">*</span>제목</td>
	                    <td>
	                        <div class="flex_box gap-08">
	                            <form:input path="menuSj" title="제목" class="input_txt required _filter half"
	                                type="text" value="" />
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="label">첨부파일</td>
	                    <td>
	                        <div class="flex_box item-center">
							<c:if test="${manual.mnlSn != null}">
								<span class="input_file_txt">파일 수정 : </span>
							</c:if>
							<input type="file" name="detailImageFiles[]" /><!-- accept="image/png, image/jpeg, image/gif"  -->
<!--                             <input type="file" name="" class="full input_file" title="이미지" -->
<!--                                 multiple="multiple" accept="image/png, image/jpeg, image/gif" /> -->

<!-- 							<div id="multiple_files"></div> -->
                        </div>

                        <c:if test="${manual.orginlFileNm != null}">
                        <div class="file_camera" id="file_camera_${fn:escapeXml(manual.mnlSn)}">
                            <a href="javascript:downloadItemImage(${fn:escapeXml(manual.mnlSn)});" ><c:out value="${fn:escapeXml(manual.orginlFileNm)}"/></a>
                            <a href="javascript:deleteItemImage(${fn:escapeXml(manual.mnlSn)});"><img src="/content/images/btn/file_close.gif" alt="close"></a>
                        </div>
                        </c:if>

	                    </td>
	                </tr>
	                <tr>
	                    <td class="label"><span class="required_mark">*</span>내용</td>
	                    <td>
	                        <div class="flex_box gap-08">
	                            <span class="placeholder_wrap">
	                                <span class="placeholder"></span>
	                                <form:textarea path="menuCn" cols="30" rows="10" maxlength=""
	                                    class="required _filter" title="내용"></form:textarea>
	                            </span>
	                        </div>
	                    </td>
	                </tr>
	            </tbody>
	        </table>
	    </div>
	    <c:if test="${manual.mnlSn == null}">
	    <div class="btn_all btn_center">
	        <div class="flex_box gap-08">
	            <button type="submit" class="btn btn-dark-gray btn-small">등록</button>
	            <button type="button" class="btn btn-default btn-small" onclick="location.href='/opmanager/manual/user/list'">취소</button>
	        </div>
	    </div>
	    </c:if>

		</form:form>

	<!--// ${op:message('M00269')} 끝-->
<%-- 	<module:smarteditorInit /> --%>
<%-- 	<module:smarteditor id="content" /> --%>


<script type="text/javascript">

$(function() {

	$( window ).scroll(function() {
		setHeight();
	});

	$("#menuSeCode").val("${fn:escapeXml(manual.menuSeCode)}");

});

function downloadItemImage(id) {
	location.href = '/opmanager/manual/file-download/'+id;
}

function deleteManual(mnlSn) {
	Common.confirm("${op:message('M00196')}", function() {
		$.post(url("/opmanager/manual/delete/" + mnlSn), {}, function(response) {
			Common.responseHandler(response, function() {
				alert("${op:message('M00205')}");
				location.href = "/opmanager/manual/user/list";
// 				location.reload();
			});
		});
	});

}

function deleteItemImage(id, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'itemId': id};
	$.post('/opmanager/manual/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$("#file_camera_"+id).remove();
// 		$(".file_camera").eq(0).remove();
	});
}
</script>


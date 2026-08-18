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

<script type="text/javascript" src="/content/modules/jquery/jquery-1.11.0.min.js"></script>

<style>
 html { overflow-y:hidden; }
</style>

</head>

<body>
<form:form modelAttribute="manual" method="post" action='${fn:escapeXml(requestContext.requestUri)}' enctype="multipart/form-data">
	<form:hidden path="menuId"/>
	<form:hidden path="menuNm"/>

	<!-- 개발 영역 -->
    <div class="popup_wrap">
        <div id="pop_header">
            <h1 class="popup_title">메뉴얼 등록</h1>
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

        <div class="popup_contents">
            <table class="board_write_table" summary="메뉴얼 등록">
                <caption>메뉴얼 등록</caption>
                <colgroup>
                    <col style="width: 150px" />
                    <col />
                </colgroup>
                <tbody>
                    <tr>
                        <td class="label">메뉴명</td>
                        <td>
                            <div>
                                <c:out value="${menuName }"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">첨부파일</td>
                        <td>
                            <div class="flex_box item-center">
                                <input type="file" name="detailImageFiles[]" class="full input_file"/>
                            </div>
                            <c:if test="${manual.orginlFileNm != null}">
                            <div class="file_camera" id="file_camera_${fn:escapeXml(manual.menuId)}">
                                <a href="javascript:downloadItemImage(${fn:escapeXml(manual.menuId)});"><c:out value="${fn:escapeXml(manual.orginlFileNm)}"/></a>
                                <a href="javascript:deleteItemImage(${fn:escapeXml(manual.menuId)});"><img src="/content/images/btn/file_close.gif" alt="close"></a>
                            </div>
                            </c:if>
                        </td>
                    </tr>
                    <!-- 첨부파일 첨부 후 case -->
<!--                     <tr> -->
<!--                         <td class="label">최종 수정일</td> -->
<!--                         <td> -->
<!--                             <div> -->
<!--                                 2022-09-01 -->
<!--                             </div> -->
<!--                         </td> -->
<!--                     </tr> -->
<!--                     첨부파일 첨부 후 case -->

                </tbody>
            </table>
            <p class="popup_btns">
                <button type="submit" class="btn btn-active">저장</button>
                <button type="button" class="btn btn-default" onclick="opener.location.reload();self.close()">취소</button>
            </p>
        </div>
    </div>
  <!-- // 개발 영역 -->
</form:form>
</body>
</html>

<script>
function downloadItemImage(id) {
	location.href = '/opmanager/manual/manager/file-download/'+id;
}

function deleteItemImage(id, index) {
	var message = '파일이 실제로 삭제됩니다.\n삭제하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'menuId': id};
	$.post('/opmanager/manual/manager/delete-item-image', param, function(response){
		Common.responseHandler(response);
		$("#file_camera_"+id).remove();
// 		$(".file_camera").eq(0).remove();
	});
}
</script>

<%@page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>

<div id="image-content"><c:out value="${imageContent}"/></div>
<div id="err-msg"><c:out value="${errMsg}"/></div>

<script type="text/javascript">
    let imageContent = document.getElementById('image-content').innerHTML;
    let errMsg = document.getElementById('err-msg').innerHTML;
    try {
        parent.loadingHide();
    } catch (e) {
    	console.log(e);
    }
    if (errMsg) {
    	alert(errMsg);
    }
    if (imageContent === "") {
    	if (!errMsg) {
            alert('이미지 파일만 등록이 가능합니다.');
    	}
    } else {
        parent.pasteHtmlToEditor(imageContent);
    }
</script>



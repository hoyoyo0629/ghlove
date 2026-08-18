<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<div class="popup_wrap">

	<h1 class="popup_title">${fn:escapeXml(title)} ${op:message('M00404')} <!-- 대체코드 --> </h1>

	<div class="popup_contents">

		<div class="board_write">
		<table class="board_write_table">
			<colgroup>
				<col style="width: 165px;" />
			</colgroup>
			<c:forEach items="${ codeList }" var="item">

				<tr>
					<td class="label">${fn:escapeXml(item.key)}</td>
					<td><div>${fn:escapeXml(item.value)}</div></td>
				</tr>
			</c:forEach>
		</table>

	</div> <!-- // board_write -->

	<!-- p class="popup_btns">
		<a href="#" class="btn btn-active">저장</a>
	</p -->

	</div> <!-- // popup_contents -->

	<a href="javascript:self.close();" class="popup_close">${op:message('M00353')} <!-- 창 닫기 --></a>
</div>
<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="popup_wrap">
	<div id="pop_header">
		<h1 class="popup_title">임시 비밀번호 발급</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>

	<div class="popup_contents">
		<p class="txt mb30 txt_center">해당 관리자의 임시 비밀번호가 발급 되었습니다.</p>
		<div class="board_list">
			<table class="board_list_table" summary="메인 배너관리" class="container">
				<caption>메인 배너관리</caption>
				<colgroup>
					<col style="width:100%;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">임시 비밀번호</th>
					</tr>
				</thead>
				<tbody>
					<tr style="background:#fff;">
						<td><c:out value="${password}"/></td>
					</tr>
				</tbody>
			</table>
		</div>
		<p class="popup_btns">
			<button type="submit" class="btn btn-active" onclick="self.close();">확인</button>
		</p>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		if("${fn:escapeXml(password)}" == "") {
			alert("임시 비밀번호 발급이 불가능합니다.");
			self.close();
		}
	});
</script>
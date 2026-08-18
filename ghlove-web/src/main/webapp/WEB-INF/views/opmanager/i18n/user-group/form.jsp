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
	

<div class="popup_wrap">
	<h1 class="popup_title">
		<c:choose>
			<c:when test="${(type != '' && type == '등록')}">
				<c:out value="${op:message('M01695')}"/> <!-- 권한그룹 생성 -->
			</c:when>
			<c:when test="${(type != '' && type == '수정')}">
				<c:out value="${op:message('M01698')}"/> <!-- 권한그룹 수정 -->
			</c:when>
		</c:choose>
	</h1> 
    <div class="popup_contents">
 
		<form:form modelAttribute="role" method="post" enctype="multipart/form-data">
			<input type="hidden" id="authority" />

			<c:choose>
				<c:when test="${(type != '' && type == '등록')}">
					<table class="board_write_table" summary="사용자 권한 그룹 생성">
						<caption><c:out value="${op:message('M01695')}"/></caption> <!-- 권한그룹 생성 -->
				</c:when>
				<c:when test="${(type != '' && type == '수정')}">
					<table class="board_write_table" summary="사용자 권한 그룹 수정">
						<caption><c:out value="${op:message('M01698')}"/></caption> <!-- 권한그룹 수정 -->
				</c:when>
			</c:choose>
				<colgroup>
					<col style="width: 150px" />
				  	<col/>
				</colgroup>
				<tbody>
				  	<tr>
						<td class="label"><span class="required_mark">*</span><c:out value="${op:message('M00571')}"/></td> <!-- 그룹명 -->
						<td>
					  		<div>
								<form:input type="text" path="roleName" title="${op:message('M00571')}" class="input_txt required _filter seven" maxlength=""/>
					  		</div>
						</td>
				  	</tr>
				  	<tr>
						<td class="label"><c:out value="${op:message('M01696')}"/></td> <!-- 그룹설명 -->
						<td>
					  		<div>
								<form:input type="text" path="roleDesc" title="${op:message('M01696')}" class="input_txt required _filter seven" maxlength=""/>
					  		</div>
						</td>
				  	</tr>
				</tbody>
			</table>

			<!-- 버튼시작 -->
			<p class="popup_btns">
				<c:choose>
					<c:when test="${(type != '' && type == '등록')}">
						<button type="submit" class="btn btn-active"><c:out value="${op:message('M01697')}"/></button> <!-- 생성하기 -->
					</c:when>
					<c:when test="${(type != '' && type == '수정')}">
						<button type="submit" class="btn btn-active"><c:out value="${op:message('M00101')}"/></button> <!-- 저장 -->
					</c:when>
				</c:choose>
				<button type="" class="btn btn-default" onclick="self.close()"><c:out value="${op:message('M00037')}"/></button> <!-- 취소 -->
			</p>
			<!-- 버튼 끝-->
		</form:form>
    </div>
</div>		
	
<script type="text/javascript">

$(function() { 
	
	// validator
	// try{
	// 	$('#code').validator(function() {
	// 		var codeType = $("#codeType").val();
	// 		var id = $("#id").val();

	// 		if (codeType.length==0) {
	// 			alert("코드구분은 필수 입력항목입니다.");
	// 			$("#codeType").focus();
	// 			return false;
	// 		} 
	// 		if (id.length==0) {
	// 			alert("코드값은 필수 입력항목입니다.");
	// 			$("#id").focus();
	// 			return false;
	// 		} 
	// 	});
	// } catch(e) {
	// 	alert(e.message);
	// }
	
});

</script>
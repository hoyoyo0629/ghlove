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

	<!-- 네비게이션 -->
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

  <!-- 상단 타이틀 -->
	<h3><span><c:out value="${op:message('MENU_1405')}"/></span></h3> <!-- 사용자 권한그룹 관리 -->

	<!-- 버튼시작 -->
	<div class="btn_all btn_right mb10">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="fnGrpCreate()"><c:out value="${op:message('M01691')}"/></button>
		</div>
	</div>
	<!-- 버튼 끝-->

  <!-- 조회 목록 -->
  <div class="board_list">
    <table class="board_list_table" summary="사용자 권한 그룹관리">
      <caption><c:out value="${op:message('MENU_1405')}"/></caption>
      <colgroup>
        <col>
        <col>
        <col style="width:5%;">
        <col style="width:15%;">
        <col style="width:15%;">
      </colgroup>
      <thead>
        <tr>
          <th scope="col"><c:out value="${op:message('M00571')}"/></th> <!-- 그룹명 -->
          <th scope="col"><c:out value="${op:message('M00998')}"/></th> <!-- 설명 -->
          <th scope="col"><c:out value="${op:message('M01694')}"/></th> <!-- 인원 -->
          <th scope="col"><c:out value="${op:message('M01692')}"/></th> <!-- 생성일 -->
          <th scope="col"><c:out value="${op:message('M01693')}"/></th> <!-- 관리기능 -->
        </tr>
      </thead>

      <tbody>
        <c:forEach items="${list}" var="role" varStatus="i">
          <tr style="background:#fff;">
            <td><c:out value="${role.groupName}"/></td>
            <td><c:out value="${role.groupExplanation}"/> </td>
						<td><c:out value="${role.userCount}"/></td>
						<td><c:out value="${op:date(role.createdDate)}"/></td>

            <td>
              <div class="flex_box juc-center gap-08">
                <button type="button" class="btn btn-default btn-sm" onclick="fnGrpUpdate('${fn:escapeXml(role.authority)}')"><c:out value="${op:message('M00087')}"/></button> <!-- 수정 -->
                <%-- <button type="button" class="btn btn-default btn-sm" onclick="fnGrpDelete('${role.authority}')"><c:out value="${op:message('M00074')}"/></button> --%> <!-- 삭제 -->
              </div>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <c:if test="${ empty list }">
      <div class="no_content">
        <p><c:out value="${op:message('M00170')}"/> </p>
      </div>
    </c:if>
  </div>


	<div class="pagination-wrap">
		<c:if test="${not empty list}">
			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>
		</c:if>
	</div>


<script type="text/javascript">

// 권한그룹 생성
function fnGrpCreate(){
	Common.popup('/opmanager/user-group/create', 'createGroup', 800, 330, 1);
}

// 권한그룹 수정
function fnGrpUpdate(id) {
  Common.popup('/opmanager/user-group/edit?authority=' + encodeURIComponent(id), 'editGroup', 800, 330, 1);
}

// 권한그룹 삭제
function fnGrpDelete(id) {
  Common.confirm("${op:message('M00196')}", function() {
			$.post(url("/opmanager/user-group/delete"), {"authority" : id}, function(response) {
				Common.responseHandler(response, function() {
					alert("${op:message('M00205')}");
					location.reload();
				});
			});
		});
}
</script>
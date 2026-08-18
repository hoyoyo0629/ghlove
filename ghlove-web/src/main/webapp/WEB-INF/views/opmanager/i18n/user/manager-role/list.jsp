<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<h3><span><c:out value="${op:message('M00099')}"/> </span></h3>
	
	<div class="board_write">
		<form:form modelAttribute="roleParam" class="opmanager-search-form clear" method="post">
			<form:hidden path="sort" />
			<form:hidden path="orderBy" />
			<form:hidden path="itemsPerPage"/>
			<form:hidden path="extra" value="manager" />
			<table class="board_write_table">
				<caption><c:out value="${op:message('M00099')}"/>  <c:out value="${op:message('M00088')}"/> </caption>
				<colgroup>
					<col style="width:150px;" />
					<col style="*" />
				</colgroup>
				<tbody>
					<tr>
						<td class="label"><c:out value="${op:message('M00011')}"/> <!-- 검색구분 --></td>
						<td>
							<div>
								<form:select path="where">
									<form:option value="ROLE_NAME" label="역할명"></form:option>
								</form:select>
								<form:input path="query" cssClass="optional seven" title="${op:message('M00022')}" />
							</div>
						</td>
					</tr>
				</tbody>		
			</table>		
			<div class="btn_all">
				<div class="btn_left">
					<button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/user/manager-role/list';"><span class="glyphicon glyphicon-repeat"></span> <c:out value="${op:message('M00047')}"/></button>
				</div>
				<div class="btn_right">
					<button type="submit" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> <c:out value="${op:message('M00048')}"/></button>
				</div>
			</div>						 							
		</form:form>			 
	</div> <!-- // board_write -->
	
	<!-- 버튼시작 -->

	<!-- 버튼 끝-->
 	<div class="sort_area mt30">
		<div class="left2">
			<span><c:out value="${op:message('M00039')}"/> : <c:out value="${op:numberFormat(totalCount)}"/> <c:out value="${op:message('M00743')}"/>   </span>
		</div>
	</div>
		
	<!-- 리스트 테이블 시작-->
	<div class="board_list mt20">
		<form id="listForm">		
			<table class="board_list_table">
				<caption><c:out value="${op:message('M00099')}"/>  <c:out value="${op:message('M00100')}"/> </caption>
				<colgroup>
					<col style="width: 30px" />
					<col style="width: 60px;" />
					<col style="">
					<col style="">
					<col style="">
					<col style="width:120px;">
				</colgroup>
				<thead>
					<tr>
						<th><input type="checkbox" id="check_all" title="${op:message('M00039')}  선택" /></th>
						<th><c:out value="${op:message('M00200')}"/></th> <!-- 순번 -->
						<th>역할명</th>
						<th>역할설명</th>
						<th>등록일</th>
						<th><c:out value="${op:message('M00087')}"/>/<c:out value="${op:message('M00074')}"/> </th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${list}" var="role" varStatus="i">
					<c:set var="visibleFlag" value="${role.authority != 'ROLE_MD' && role.authority != 'ROLE_ISMS' && role.authority != 'ROLE_EXCEL'}"/>
					<tr>
						<td>
							<c:if test="${visibleFlag}">
								<input type="checkbox" name="id" value="${fn:escapeXml(role.authority)}" title="체크박스"  />
							</c:if>
						</td>
						<td><c:out value="${pagination.itemNumber - i.count}"/></td>
						<td>
							<c:choose>
								<c:when test="${not visibleFlag}">
									<c:out value="${role.roleName}"/>
								</c:when>
								<c:otherwise>
									<a href="edit/${fn:escapeXml(role.authority)}"><c:out value="${role.roleName}"/></a>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
								<c:out value="${role.roleDesc}"/>
						</td>
						<td>
								<c:out value=""/>${op:datetime(role.createdDate)}
						</td>
						<td>
							<c:if test="${visibleFlag}">
								<div>
									<a href="/opmanager/user/manager-role/edit/${fn:escapeXml(role.authority)}" class="btn btn-gradient btn-xs" title="${op:message('M00087')} "><c:out value="${op:message('M00087')}"/></a>
								</div>
							</c:if>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		
			<c:if test="${empty list}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>
		</form>

		<div class="btn_all">
			<div class="btn_left">
				<button type="button" onclick="Common.deleteCheckedList(url('/opmanager/user/manager-role/list/delete'))" class="btn btn-default btn-sm"><span><c:out value="${op:message('M00576')}"/> <!-- 선택삭제 --></span></button>
			</div>
			<div class="btn_right">
				<a href="/opmanager/user/manager-role/create" class="btn btn-active btn-sm"><span class="glyphicon glyphicon-plus"></span> <c:out value="${op:message('M00088')}"/> </a>
			</div>
		</div>

		<page:pagination-manager />

	</div><!--//board_list E-->

<script type="text/javascript">
	$(function() {
		$('#searchParam').validator(function(selector) {});
	});

</script>

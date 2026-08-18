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


	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

	<!-- 상단타이틀 -------------------------------------------------------------------- -->
		<h3><span>메뉴관리</span></h3>
	<!-- 상단타이틀 -------------------------------------------------------------------- -->
<div>

  	<!-- 코드 데이터  -->
	<!-- 조회 부 -------------------------------------------------------------------- -->
	<form:form modelAttribute="menuParam" method="post" enctype="multipart/form-data">
		<table class="board_write_table"  summary="메뉴목록">
			<colgroup>
				<col style="width:220px;" />
				<col style="width:*;" />
			</colgroup>
			<tbody>
				 <tr>
				 	<td class="label"><c:out value="${op:message('M01223')}"/></td> <!-- 검색구분 -->
				 	<td>
				 		<div>
							<form:select path="where" title="${op:message('M00054')}" onchange="fnMenuSearch()"> <!-- 화면 선택 -->
								<form:option value="OPMANAGER">관리자</form:option> <!-- 관리자 -->
								<form:option value="SELLER">답례품관리자</form:option> <!-- 답례품관리자 -->
							</form:select>
							<!--
							<form:input type="text" path="query" class="three" title="${op:message('M00021')}" placeholder="Search.."/>
							-->
						</div>
				 	</td>
				 </tr>
			</tbody>
		</table>

		<!-- 버튼시작 -->
		<div class="btn_all">
			<div class="btn_right">
                <div class="flex_box gap-08">
                    <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/menu/list';"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
                    <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
                </div>
			</div>
		</div>
		<!-- 버튼 끝-->

	</form:form>
	<!-- 조회 부 -------------------------------------------------------------------- -->

		<form id="listForm">
			<c:choose>
				<c:when test="${not empty menuList}">
					<div class="board_write" style="height:500px; margin:20px 0px; overflow:auto">
				</c:when>
				<c:otherwise>
					<div class="board_write" style="margin:20px 0px;">
				</c:otherwise>
			</c:choose>
						<table class="board_list_table" summary="${op:message('MENU_1415')}" style="margin:0px 0px;">
							<caption><c:out value="${op:message('MENU_1415')}"/></caption>
							<colgroup>
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
								<col style="" />
							</colgroup>
							<thead>
								<tr>
									<th scope="col">메뉴ID</th>
									<th scope="col">메뉴명</th>
									<th scope="col">메뉴타입</th>
									<th scope="col">페이지 URL</th>
									<th scope="col">메뉴순서</th>
									<th scope="col">노출여부</th>
									<th scope="col">메뉴상태</th>
									<th scope="col" style="display:none;">하위메뉴</th>
									<th scope="col">변경</th>
									<th scope="col" style="display:none;">메뉴경로</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${menuList}" var="list" varStatus="i">
									<tr>
										<td><c:out value='${list.menuId}'/></td>
										<c:if test="${list.menuType == 1}">
											<td style="text-align:left;"><c:out value='${list.menuName}'/></td>
										</c:if>
										<c:if test="${list.menuType == 2}">
											<td style="text-align:left; padding-left: 30px;"><c:out value='${list.menuName}'/></td>
										</c:if>
										<c:if test="${list.menuType == 3}">
											<td style="text-align:left; padding-left: 60px;"><c:out value='${list.menuName}'/></td>
										</c:if>
										<td><c:out value='${list.menuType}'/></td>
										<td style="text-align:left;">
											<div>
												<!--<a href='javascript:fnUpdatePage("${fn:escapeXml(list.menuId)}")'>${fn:escapeXml(list.menuUrl)}</a>-->
												<c:out value='${list.menuUrl}'/>
											</div>
										</td>
										<td><c:out value='${list.menuSeq}'/></td>
										<td><c:out value='${list.displayFlag}'/></td>
										<td><c:out value='${list.statusCode}'/></td>
										<td style="display:none;"><c:out value='${list.menuChild}'/></td>
										<td>
		                                    <div class="flex_box juc-center gap-08">
		                                        <a href='javascript:fnUpdateMenu("${fn:escapeXml(list.menuId)}","${fn:escapeXml(list.menuPath)}")' class="btn btn-default btn-sm"><c:out value="${op:message('M00087')}"/></a> <!-- 수정 -->
		                                        <a href='javascript:fnDeleteMenu("${fn:escapeXml(list.menuId)}","${fn:escapeXml(list.menuChild)}")' class="delete_item btn btn-default btn-sm"><c:out value="${op:message('M00074')}"/></a> <!-- 삭제 -->
		                                    </div>
										</td>
										<td style="display:none;"><c:out value='${list.menuPath}'/></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

			<c:if test="${empty menuList}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

			<div class="btn_all btn_right">
				<div class="flex_box">
					<a href="javascript:fnInsertMenu();" class="btn btn-default btn-mini"><c:out value="${op:message('M01697')}"/></a>
				</div>
			</div>

		</form>
</div>
<script type="text/javascript">

	$(document).ready(function(){

	});

	// 메뉴 생성하기
	function fnInsertMenu(){
		if($('#where').val() == 'USER') { alert("사용자 메뉴는 시스템관리 > 공통코드관리(MENU_URL)에서 생성합니다."); return false; }
		Common.popup('/opmanager/menu/create?menuGubun=' + $("#where").val(), 'createMenu', 800, 600, 1);
	}

	// 메뉴 수정
	function fnUpdateMenu(menuId, menuPath) {
		Common.popup('/opmanager/menu/edit?menuGubun=' + $("#where").val() + "&menuId=" + encodeURIComponent(menuId) + "&menuPath=" + encodeURIComponent(menuPath), 'editMenu', 800, 600, 1);
	}

	// 메뉴리스트 조회
	function fnMenuSearch(){
		$('#menuParam').submit();
	}

	// 메뉴 삭제
	function fnDeleteMenu(menuId,menuChild) {
		if(menuChild > 0) {
			alert("하위메뉴가 존재하여 삭제할 수 없습니다.");
			return false;
		}

		Common.confirm("${op:message('M00196')}", function() {
			$.post(url("/opmanager/menu/delete"), {"menuGubun" : $('#where').val(), "menuId" : menuId}, function(response) {
				Common.responseHandler(response, function() {
					alert("${op:message('M00633')}");
					fnMenuSearch();
				});
			});
		});
	}

	// 페이지 관리
	function fnUpdatePage(menuId) {
		location.href = "/opmanager/menu/page?menuGubun=" + $("#where").val() + "&menuId=" + encodeURIComponent(menuId);
	}
</script>
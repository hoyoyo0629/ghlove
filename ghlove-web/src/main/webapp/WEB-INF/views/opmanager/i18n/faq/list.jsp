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

    <h3><span>FAQ</span></h3>

    <form:form modelAttribute="faqDto" enctype="multipart/form-data" method="post">
        <form:hidden path="page" />

		<div class="board_write">
			<table class="board_write_table">
				<caption>${op:message('M00675')}</caption>
				<colgroup>
					<col style="width:150px;" />
					<col />
				</colgroup>
				<tbody>
	 				<tr>
					<td class="label">${op:message('M00275')}</td>
					<td>
						<div class="flex_box gap-08">
							<form:hidden path="where" value="title"/>
							<form:input path="query" cssClass="input_txt required _filter half" title="${op:message('M00021')}"/>
						</div>
					</td>
					</tr>
<%-- 					<tr>
					    <td class="label">검색 구분</td>
					 	<td>
					 		<div class="row">
                                <div class="col-xs-2 pr-0">
                                    <form:select path="where" class="form-block" title="검색구분">
                                        <form:option value="title">제목</form:option>
                                        <form:option value="content">내용</form:option>
                                    </form:select>
                                </div>
                                <div class="col-xs-5">
								    <form:input type="text" path="query" class="form-block" title="검색어" />
                                </div>
							</div>
					 	</td>
                    </tr> --%>
                    <tr>
                        <td class="label">질문 유형</td>
                        <td>
                             <div class="row">
                                 <div class="col-xs-2 pr-0">
                                     <form:select path="faqType" class="form-block" title="질문 유형">
                                         <form:option value="">전체 </form:option>
                                         <c:forEach var="faqType" items="${faqTypes}">
                                             <form:option value="${fn:escapeXml(faqType.code)}">${fn:escapeXml(faqType.title)}</form:option>
                                         </c:forEach>
                                     </form:select>
                                 </div>
                             </div>
                         </td>
                     </tr>
				</tbody>
			</table>
		</div> <!-- // board_write -->
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
				<li>
			    	검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			    </li>
		</div>
		<!-- 버튼시작 -->
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/faq/list';"><span class="glyphicon glyphicon-repeat"></span> ${op:message('M00047')}</button> <!-- 초기화 -->
				<button type="submit" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> ${op:message('M00048')}</button> <!-- 검색 -->
			</div>
		</div>
		<!-- 버튼 끝-->


		<div class="count_title mt20">
			<h5>전체 : ${op:numberFormat(pageContent.totalElements)}건</h5>
			<span>
				${op:message('M00052')} :
				<form:select path="size" title="${op:message('M00239')}"> <!-- 화면출력 -->
					<form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
					<form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
					<form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
				</form:select>
			</span>
		</div>
	</form:form>

	<form id="listForm">
		<div class="board_write">
			<table class="board_list_table" summary="${op:message('M00273')}"> <!-- 주문내역 리스트 -->
				<caption>${op:message('M00273')}</caption>
				<colgroup>
					<col style="width:30px;" />
					<col style="width:60px;" />
					<col style="width:130px;" />
					<col />
					<col style="width:160px;" />
					<col style="width:60px;" />
					<%-- <col style="width:100px;" /> --%>
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><input type="checkbox" id="check_all" title="${op:message('M00169')}" /></th> <!-- 체크박스 -->
						<th scope="col">순번</th>
						<th scope="col">질문유형</th>
						<th scope="col">제목</th>
						<th scope="col">작성일</th>
						<th scope="col">조회수</th>
						<!-- <th scope="col">관리</th> -->
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageContent.content}" var="list" varStatus="i">
						<tr>
							<td><input type="checkbox" name="id" id="check" value="${fn:escapeXml(list.id)}" title="${op:message('M00169')}" /></td>
							<td>${op:numbering(pageContent, i.index)}</td>
							<td>${fn:escapeXml(list.faqType.title)}</td>
							<td class="text-left">
								<a href="javascript:Link.view('/opmanager/faq/edit/${fn:escapeXml(list.id)}')">${fn:escapeXml(list.title)}</a>
							<td>${fn:escapeXml(list.createdDateTime)}</td>
							<td>${fn:escapeXml(list.hit)}</td>
<%-- 							<td>
                                <a href="javascript:Link.view('/opmanager/faq/edit/${list.id}')" class="btn btn-gradient btn-xs">수정</a>
                                <a href="#" class="btn btn-gradient btn-xs op-delete-data">삭제</a>
                            </td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<c:if test="${empty pageContent.content}">
			<div class="no_content">
				${op:message('M00473')} <!-- 데이터가 없습니다. -->
			</div>
		</c:if>

		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a href="<c:url value="/opmanager/faq/create" />" class="btn btn-dark-gray btn-mini">${op:message('M00088')}</a> <!-- 등록 -->
				<a id="op-delete-list-data" href="#" class="btn btn-dark-gray btn-mini"><span>${op:message('M00074')}</span></a>
				<%-- <a href="javascript:deleteCheckDataboard(${databoard.dataId})" class="btn btn-dark-gray btn-mini">${op:message('M00074')}</a> --%>
			</div>
		</div>
		<div class="pagination-wrap">
			<p class="pagination op-pagination">
				<page:pagination-jpa />
			</p>
		</div>



<%-- 		<div class="btn_all">
			<div class="btn_left mb0">
				<a id="op-delete-list-data" href="#" class="btn btn-default btn-sm"><span>선택 삭제</span></a>
			</div>
			<div class="btn_right mb0">
				<a href="/opmanager/faq/create" class="btn btn-active btn-sm"><span class="glyphicon glyphicon-plus"></span> ${op:message('M00088')}</a> <!-- 등록 -->
			</div>
		</div>

        <page:pagination-jpa /> --%>
	</form>



<script type="text/javascript">
$(function() {

	// 데이터 출력 수 설정.
	$('#size').on("change", function(){
	    $('#page').val("1");
		$('#faqDto').submit();
	});

	// 목록데이터 - 삭제처리
	$('#op-delete-list-data').on('click', function() {
		Common.updateListData("/opmanager/faq/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
	});

    $('.op-delete-data').on('click', function(e) {
        e.preventDefault();
        $('#check_all').prop('checked', false);
        $(this).closest('table').find('input[name=id]').prop('checked', false);
        $(this).closest('tr').find('input[name=id]').prop('checked', true);
        $('#op-delete-list-data').click();
    });

});
</script>
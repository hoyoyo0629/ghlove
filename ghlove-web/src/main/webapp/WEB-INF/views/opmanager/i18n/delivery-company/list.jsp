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


			<form:form modelAttribute="deliveryCompanyParam" method="post" enctype="multipart/form-data">
				<h3><span><c:out value="${op:message('M01211')}"/></span></h3>
				<div class="board_write">
					<table class="board_write_table" summary="${op:message('M01211')}">
						<caption><c:out value="${op:message('M01211')}"/></caption> <!-- 배송업체 -->
						<colgroup>
							<col style="width:220px;" />
							<col style="width:auto;" />
						</colgroup>
						<tbody>
							 <tr>
							 	<td class="label"><c:out value="${op:message('M00011')}"/></td> <!-- 검색구분 -->
							 	<td>
							 		<div class="flex_box gap-08">
							 			<form:select path="where" class="wd-150">
											<form:option value="DELIVERY_COMPANY_NAME" label="${op:message('M00665')}" /> <!-- 배송업체명 -->
											<form:option value="TEL_NUMBER" label="${op:message('M00154')}" />	 <!-- 연락처 -->
										</form:select>
										<form:input type="text" path="query" name="deliveryCompanyName" class="full" title="${op:message('M00665')}" />
							 		</div>
							 	</td>
							 </tr>
						</tbody>
					</table>
				</div> <!-- // board_write -->

				<!-- 버튼시작 -->
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/delivery-company/list';"><c:out value="${op:message('M00047')}"/></button>
						<button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
					</div>
				</div>
				<!-- 버튼 끝-->

			<div class="count_title mt-40">
				<h5>
					<c:out value="${op:message('M00045')}"/>  <c:out value='${op:numberFormat(pagination.totalItems)}'/> <c:out value="${op:message('M00272')}"/>
				</h5>	 <!-- 전체 -->   <!-- 건 조회 -->
				<span>
					<form:select path="itemsPerPage" title="출력수 선택" onchange="$('#deliveryCompanyParam').submit();">
						<form:option value="10" label="10개 출력" />
						<form:option value="20" label="20개 출력" />
						<form:option value="30" label="30개 출력" />
						<form:option value="50" label="50개 출력" />
						<form:option value="100" label="100개 출력" />
						<form:option value="500" label="500개 출력" />
						<form:option value="1000" label="1000개 출력" />
					</form:select>
				</span>				
			</div>
			</form:form>
			
			<form id="listForm">
			<div class="board_write">
				<table class="board_list_table" summary="${op:message('M00273')}"> <!-- 주문내역 리스트 -->
					<caption><c:out value="${op:message('M00273')}"/></caption>
					<colgroup>
	                    <col style="width:50px;">
	                    <col style="width:200px;">
	                    <col style="width:200px;">
	                    <col style="width:500px;">
	                    <col style="width:150px;">
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" name="tempId2" id="check_all" title="${op:message('M00169')}" /></th> <!-- 체크박스 -->
							<th scope="col"><c:out value="${op:message('M00665')}"/></th> <!-- 배송업체명 -->
							<th scope="col"><c:out value="${op:message('M00666')}"/></th> <!-- 대표연락처 -->
							<th scope="col"><c:out value="${op:message('M00671')}"/></th> <!-- 배송 조회 URL -->
							<th scope="col"><c:out value="${op:message('M00669')}"/></th> <!-- 사용유무 -->
						</tr>

					</thead>
					<tbody>
					<c:forEach items="${deliveryCompanyList}" var="companyList">
						<tr style="background:#fff;">
							<td>
								<div>
									<input type="checkbox" name="id" id="check" value="${fn:escapeXml(companyList.deliveryCompanyId)}" title="${op:message('M00169')}" />
								</div>
							</td>	
							<td>
								<div>
									<a href="/opmanager/delivery-company/edit/${fn:escapeXml(companyList.deliveryCompanyId)}"><c:out value="${companyList.deliveryCompanyName}"/></a>
								</div>
							</td>
							<td>
								<div>
									<c:out value='${companyList.telNumber}'/>
								</div>
							</td>	
							<td style="text-align:left;">
								<div>
									<c:out value='${companyList.deliveryCompanyUrl}'/>
								</div>
							</td>	
							<td>
								<div>
									<c:out value="${companyList.useFlag == 'Y' ? op:message('M00083') : op:message('M00089')}"/> <!-- 사용 --> <!-- 사용안함 -->
								</div>	
							</td>	
						</tr>
					</c:forEach>
					</tbody>
				</table>

				<c:if test="${empty deliveryCompanyList}">
					<div class="no_content">
						<c:out value="${op:message('M00473')}"/><!-- 데이터가 없습니다. -->
					</div>
				</c:if>

			</div><!--// board_write E-->

			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<a href="/opmanager/delivery-company/create" class="btn btn-default btn-mini"> <c:out value="${op:message('M00088')}"/></a> <!-- 등록 -->
					<a id="delete_list_data" href="#" class="btn btn-dark-gray btn-mini"><span><c:out value="${op:message('M00074')}"/></span></a> <!-- 삭제 -->
				</div>
			</div>
			</form>

			<div class="pagination-wrap">
				<page:pagination-manager /><br/>
			</div>



<script type="text/javascript">
$(function() {

	//목록데이터 - 삭제처리
	$('#delete_list_data').on('click', function() {
		Common.updateListData("/opmanager/delivery-company/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
	});

});
</script>
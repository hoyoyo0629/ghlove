<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<style>
	.popup_contents02 {
		width:1500px !important;
		padding-right:7px;
	}
</style>

<div class="popup_contents02">
	<form method="post" id="listForm">
		<div class="count_title">
			<h5>
				<c:out value="${op:message('M00045')}"/> <c:out value="${op:numberFormat(pagination.totalItems)}"/> <c:out value="${op:message('M00272')}"/>
			</h5>
			<%-- <span>
				<select id="pageCount" onchange="pageChange();" title="출력수 선택">
					<option value="10" label="10개 출력" />
					<option value="20" label="20개 출력" />
					<option value="30" label="30개 출력" />
					<option value="50" label="50개 출력" />
					<option value="100" label="100개 출력" />
					<option value="500" label="500개 출력" />
					<option value="1000" label="1000개 출력" />
				</select>
			</span> --%>
		</div>		
		<div class="board_write">
			<table class="board_list_table">
				<colgroup>
	                <col style="width:50px;">
	                <col style="width:400px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
				</colgroup>
				<thead>
					<tr>
						<!-- <th scope="col">No</th> -->
						<th scope="col">로그 아이디</th>
						<th scope="col">특정사업 기부 사업명</th>
						<th scope="col">승인 전 상태</th>
						<th scope="col">승인 후 상태</th>
	 					<th scope="col">승인자</th>
						<th scope="col">승인일시</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="log" varStatus="i">
						<tr>
							<%-- <td>
								<c:out value="${pagination.totalItems - log.logNo + 1}"/>
							</td> --%>
							<td>
								<c:out value="${log.dsgncntrConfirmId}"/>
							</td>
							<td>
								<c:out value="${log.prjSubject}"/>
							</td>
							<td>
								<c:out value="${log.bfStatusStr}"/>
							</td>
							<td>
								<c:out value="${log.tbStatusStr}"/>
							</td>
							<td>
								<c:out value="${log.registerName}"/>
							</td>
							<td>
								<c:out value="${log.registPnttmStr}"/>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty list}">
						<tr class="no_content">
							<td colspan="13">승인 이력이 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>				 
		</div> <!-- // board_write -->
	</form>
	<div class="pagination-wrap">
		<page:pagination-manager />
	</div>
    <p class="popup_btns">
        <button type="button" class="btn btn-active" onclick="javascript:self.close();">확인</button>
    </p>
</div>


<script type="text/javascript">
    function pageChange() {
        location.href="/opmanager/item/popup/log/${itemId}";
    }
    
    $(function(){
        $('.popup_title').text('승인이력조회');	
    });
    
</script>
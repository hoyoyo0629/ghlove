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
		<input type="hidden" name="userId" value="<c:out value='${userId}'/>" />
		<div class="count_title">
			<h5>
					<c:out value='${op:message("M00045")}'/> <c:out value='${op:numberFormat(pagination.totalItems)}'/> <c:out value='${op:message("M00272")}'/>
			</h5>
			<span>
				<select id="pageCount" onchange="pageChange();" title="출력수 선택">
					<option value="10" label="10개 출력" />
					<option value="20" label="20개 출력" />
					<option value="30" label="30개 출력" />
					<option value="50" label="50개 출력" />
					<option value="100" label="100개 출력" />
					<option value="500" label="500개 출력" />
					<option value="1000" label="1000개 출력" />
				</select>
			</span>				
		</div>		
		<div class="board_write">
			<table class="board_list_table">
				<colgroup>
	                <col style="width:50px;">
	                <col style="width:400px;">
	                <col style="width:100px;">
	                <col style="width:150px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	                <col style="width:150px;">
	                <col style="width:150px;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">No</th>
						<th scope="col">상품명(상품코드)</th>
						<!-- <th scope="col">판매자</th> -->
						<th scope="col">공개유무</th>
						<th scope="col">품절여부</th>
						<!-- <th scope="col">정가</th> -->
						<th scope="col">판매가</th>
						<!-- <th scope="col">공급가 설정</th> -->
	 					<!-- <th scope="col">등록/수정위치</th> -->
	 					<th scope="col">등록/수정자</th>
	 					<th scope="col">처리 구분</th>
	 					<th scope="col">등록/수정일</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="<c:out value='${list}'/>" var="item" varStatus="i">
						<tr>
							<td>
								<c:out value='${pagination.itemNumber - i.count}'/>
							</td>
							<td class="text-left">
								<c:out value='${item.itemName}'/>
								<br/>
								(<c:out value='${item.itemUserCode}'/>)
							</td>
							<td class="hidden">
								<c:out value='${item.seller.sellerName}'/>
							</td>
							<td>
								<c:choose>
									<c:when test="${item.displayFlag == 'Y'">공개</c:when>
									<c:when test="${item.displayFlag == 'N'">비공개</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${item.soldOut == '0'}">판매가능</c:when>
									<c:when test="${item.soldOut == '1'}">재고품절</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
							<td class="hidden">
								<c:choose>
									<c:when test="<c:out value='${empty item.itemPrice}'/>">
										-
									</c:when>
									<c:otherwise>
										<c:out value='${op:numberFormat(item.itemPrice)}'/>원
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:out value='${op:numberFormat(item.salePrice)}'/>
							</td>
							<td class="hidden">
								<c:choose>
									<c:when test="${item.commissionType == '1'}">입점업체 수수료 설정</c:when>
									<c:when test="${item.commissionType == '2'}">상품별 수수료 설정</c:when>
									<c:when test="${item.commissionType == '3'}">공급가 설정</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
							<td class="hidden">
								<c:choose>
									<c:when test="${item.processPage == 'manager'}">관리자</c:when>
									<c:when test="${item.processPage == 'seller'}">판매자</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${item.processPage == 'manager'}"><c:out value='${item.managerLoginId}'/> (<c:out value='${item.userName}'/>)</c:when>
									<c:when test="${item.processPage == 'seller'}"><c:out value='${item.seller.loginId}'/> (<c:out value='${item.seller.sellerName}'/>)</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
							</td>
							<td>
							
								<c:choose>
									<c:when test="${item.actionType == 'insert'}">등록</c:when>
									<c:when test="${item.actionType == 'update'}">수정</c:when>
									<c:when test="${item.actionType == 'approval'}">승인</c:when>
									<c:when test="${item.actionType == 'insert-by-excel'}">엑셀등록</c:when>
									<c:when test="${item.actionType == 'apply-reg-by-excel'}">엑셀등록신청</c:when>
									<c:when test="${item.actionType == 'update-by-excel'}">엑셀수정</c:when>
									<c:when test="${item.actionType == 'apply-mod-by-excel'}">엑셀수정신청</c:when>
									<c:otherwise><c:out value='${item.actionType}'/></c:otherwise>
								</c:choose>
							</td>
							<td><c:out value='${op:datetime(item.createdDate)}'/></td>
						</tr>
					</c:forEach>
					<c:if test="<c:out value='${empty list}'/>">
						<tr class="no_content">
							<td colspan="13">변경사항 내역이 없습니다.</td>
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
        location.href="/seller/item/popup/log/<c:out value='${itemId}'/>";
    }
</script>
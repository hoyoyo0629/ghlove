<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<!-- <div class="admin_wrap"> -->
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<div class="item_list">
		<h3><span>대표상품관리</span></h3>

		<form:form modelAttribute="itemParam" method="post">
			<form:hidden path="categoryId" />
			<form:hidden path="query" />

            <div class="btn_all btn_right">
                <div class="flex_box gap-08">
                    <button type="button" class="btn btn-default btn-mini" onclick="insertMainItem()">등록</button>
                    <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteMainItem()">삭제</button>
                </div>
            </div>

			<div class="count_title mt-40 hidden">
				<h5>
						${op:message('M00045')}  ${op:numberFormat(pagination.totalItems)} ${op:message('M00272')}
				</h5>	 <!-- 전체 -->   <!-- 건 조회 -->
				<span>
								<form:select path="orderBy" title="등록일선택">
									<form:option value="ITEM_ID" label="${op:message('M00202')}" /> <!-- 등록일 -->

									<c:if test="${!empty itemParam.categoryId}">
										<form:option value="ORDERING" label="${op:message('M00790')}" /> <!-- 정렬 -->
									</c:if>

									<form:option value="SALE_PRICE" label="${op:message('M00786')}" /> <!-- 판매가격 -->
									<form:option value="HITS" label="${op:message('M00685')}" /> <!-- 조회수 -->
								</form:select>
								<form:select path="sort" title="검색방법 선택" style="display:none">
									<form:option value="DESC" label="${op:message('M00689')}" />    <!-- 내림차순 -->
									<form:option value="ASC" label="${op:message('M00690')}" />    <!-- 오름차순 -->
								</form:select>
								<form:select path="itemsPerPage" title="출력수 선택">
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

		<div class="board_list">
			<form id="listForm">
				<table class="board_list_table" summary="전체상품리스트">
					<caption>전체상품리스트</caption>
					<colgroup>
                        <col style="width:500px;">
                        <col style="width:200px;">
                        <col style="width:150px;">
                        <col style="width:100px;">
                        <col style="width:100px;">
                        <col style="width:100px;">
                        <col style="width:200px;">
					</colgroup>
					<thead>
					<tr>
						<th>${op:message('M00018')} <!-- 상품명 --></th>
						<th>${op:message('M00786')} <!-- 판매가격 --></th>
						<th>${op:message('M00104')} <!-- 상호명 --></th>
						<th>${op:message('M01630')} <!-- 판매자 --></th>
						<th>${op:message('M01476')} <!-- 상태 --></th>
						<th>${op:message('M00191')} <!-- 공개유무 --></th>
						<th>${op:message('M00202')} <!-- 등록일 --></th>
					</tr>
					</thead>
					<tbody class="sortable">

					<c:forEach items="${list}" var="item" varStatus="i">
						<c:set var="displayFlagText">${op:message('M00096')}</c:set> <!-- 공개 -->
						<c:if test="${item.displayFlag == 'N'}">
							<c:set var="displayFlagText"><span style="color:#e84700">${op:message('M00097')}</span></c:set> <!-- 비공개 -->
						</c:if>

						<!-- 인덱스 시킴 -->
						<c:set var="noindexYn"><span style="color: #25A5DC">Y</span></c:set>	<!-- 인덱스 시킴. -->
						<c:if test="${item.seo.indexFlag == 'N'}">
							<c:set var="noindexYn" value="N" /> <!-- 인덱스 시키지 않음. -->
						</c:if>

						<c:choose>
							<c:when test="${item.dataStatusCode == '20' || item.dataStatusCode == '21' || item.dataStatusCode == '30' || item.dataStatusCode == '31' || item.dataStatusCode == '40' || item.dataStatusCode == '41'}">
								<c:set var="itemSaleStatusText">등록대기</c:set>
							</c:when>
							<c:when test="${item.dataStatusCode == '90'}">
								<c:set var="itemSaleStatusText">판매종료</c:set>
							</c:when>
							<c:when test="${item.itemSoldOutFlag == 'Y'}">
								<c:set var="itemSaleStatusText"><span style="color:#e84700">${op:message('M00693')}</span></c:set>	 <!-- 품절 -->
							</c:when>
							<c:otherwise>
								<c:set var="itemSaleStatusText">${op:message('M00694')}</c:set> <!-- 판매중 -->
							</c:otherwise>
						</c:choose>

						<c:set var="param" value="/opmanager/item/${item.itemType == '3' ? 'set-edit' : 'edit'}/${item.itemUserCode}" />
						<tr>
							<td class="left break-word">
								<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(item.itemUserCode)}" target="_blank" class="break-word">
									<div class="flex_box item-center gap-08">
										<input type="hidden" id="itemId" value="${fn:escapeXml(item.itemId)}" />
										<img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image" alt="상품이미지" />
										<div>[${fn:escapeXml(item.itemUserCode)}]${fn:escapeXml(item.itemName)}
										<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(item.itemUserCode)}" target="_blank" class="break-word" style="color:#517BAB">[새탭]</a>
										<c:if test="${item.itemLabel == '2'}">
											<img src="/content/opmanager/images/icon/icon_new2.gif" alt="new" />
										</c:if>
									</div>
								</a>

							</td>
							<td class="text-center">
								${op:numberFormat(item.salePrice)}
							</td>
							<td>
								${fn:escapeXml(item.seller.companyName)}
							</td>
							<td>${fn:escapeXml(item.seller.sellerName)}</td>
							<td>${fn:escapeXml(itemSaleStatusText)}</td>
							<td>${fn:escapeXml(displayFlagText)}</td>
							<td>${op:date(item.createdDate)}</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</form>

			<c:if test="${empty list}">
				<div class="no_content">
						등록된 상품이 없습니다.
				</div>
			</c:if>

			<div class="pagination-wrap hidden">
				<page:pagination-manager />
			</div>

		</div> <!-- // board_list -->
	</div>
<!-- </div> -->

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>
<script type="text/javascript">
    $(function() {

        $('#orderBy, #sort, #itemsPerPage').on("change", function(){
            $('#sort option').eq(0).prop("disabled", false);
            if ($(this).val() == 'ORDERING') {
                $('#sort').val("ASC").find('option').eq(0).prop("disabled", true);
            }

            $('#itemParam').submit();
        });

    });

    // 대표상품등록
    function insertMainItem() {
    	// 지자체 관리자만 등록가능
    	if('${fn:escapeXml(adminRole)}' == 'SYS') {
    		alert("대표상품 등록권한이 없습니다.");
    		return false;
    	}

    	Common.popup("/opmanager/item/edit/find-item", 'find-item', 1000, 1000, 1);

    }

    // 대표상품삭제
    function deleteMainItem() {

    	// 지자체 관리자만 삭제가능
    	if('${fn:escapeXml(adminRole)}' == 'SYS') {
    		alert("대표상품 삭제권한이 없습니다.");
    		return false;
    	}

		var message = '등록된 대표상품을 삭제 하시겠습니까?';
		if (!confirm(message)) {
			return;
		}

		var param = {'itemId': $('#itemId').val()};

		$.post('${fn:escapeXml(requestContext.managerUri)}/item/edit/representative-item/delete', param, function(response){
			Common.responseHandler(response, function(){
				alert(Message.get("M00205"));	// 삭제 되었습니다.
				location.reload();
			});
		});

    }

</script>


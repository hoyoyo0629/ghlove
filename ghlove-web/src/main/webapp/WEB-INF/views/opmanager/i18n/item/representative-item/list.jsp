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
		<h3><span>상품리스트</span></h3>
	
		<form:form modelAttribute="itemParam" method="get">
			<form:hidden path="categoryId" />
	
			<div class="board_write">
				<table class="board_write_table" summary="상품리스트">
					<caption>상품리스트</caption>
					<colgroup>
						<col style="width: 220px" />
						<col />
					</colgroup>
					<tbody>
                    <tr>
                        <td class="label">지자체</td>
                        <td>
                            <div class="flex_box gap-08 item-center">
	                            <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
				                    <form:option value="">-시,도 선택-</form:option>
				                    <c:forEach items="${wdr}" var="wdr">
				                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
				                    </c:forEach>
				                </form:select>
	                            <form:select path="shLocgovCode" class="wd-150">
				                    <option value="">-시,군,구-</option>
				                </form:select>
                            </div>
                        </td>
                    </tr>	
					<tr>
						<td class="label">${op:message('M00270')} <!-- 카테고리 --></td>
						<td>
							<div class="flex_box gap-08">
								<form:select path="categoryGroupId" class="category wd-150">
									<option value="0">= ${op:message('M00039')} =</option> <!-- 전체 -->
									<c:forEach items="${categoryTeamGroupList}" var="categoriesTeam">
										<c:if test="${categoriesTeam.categoryTeamFlag == 'Y'}">
											<optgroup label="${fn:escapeXml(categoriesTeam.name)}">
												<c:forEach items="${categoriesTeam.categoriesGroupList}" var="categoriesGroup">
													<c:if test="${categoriesGroup.categoryGroupFlag == 'Y'}">
														<form:option value="${fn:escapeXml(categoriesGroup.categoryGroupId)}" label="${fn:escapeXml(categoriesGroup.groupName)}" />
													</c:if>
												</c:forEach>
											</optgroup>
										</c:if>
									</c:forEach>
	
								</form:select>
	
								<form:select path="categoryClass1" class="category wd-150">
								</form:select>
	
								<form:select path="categoryClass2" class="category wd-150">
								</form:select>
	
								<form:select path="categoryClass3" class="category" style="display:none">
								</form:select>
	
								<form:select path="categoryClass4" class="category" style="display:none">
								</form:select>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">${op:message('M00011')}</td>    <!-- 검색구분 -->
						<td>
							<div class="flex_box gap-08">
								<form:select path="where" title="상세검색 선택" class="wd-150">
									<form:option value="">${op:message('M00039')} <!-- 전체 --></form:option>
									<form:option value="ITEM_NAME">${op:message('M00018')} <!-- 상품명 --></form:option>
									<form:option value="SELLER_NAME">${op:message('M00104')} <!-- 상호명 --> </form:option>
								</form:select>
								<form:input path="query" class="input_txt required _filter wd-310" title="상세검색 입력" />
							</div>
						</td>
					</tr>   	
					</tbody>
				</table>
	
			</div> <!-- // board_write -->
	
			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/item/representative-item/list';"> ${op:message('M00047')}</button> <!-- 초기화 -->
					<button type="submit" class="btn btn-dark-gray btn-mini"> ${op:message('M00048')}</button> <!-- 검색 -->
				</div>
			</div>
	
			<div class="count_title mt-40">
				<h5>
						${op:message('M00045')}  ${op:numberFormat(pagination.totalItems)} ${op:message('M00272')}
				</h5>	 <!-- 전체 -->   <!-- 건 조회 -->
				<span>
								<form:select path="orderBy" title="등록일선택" style="display:none">
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
                        <col style="width:50px;">
                        <col style="width:50px;">
                        <col style="width:200px;">
                        <col style="width:500px;">
                        <col style="width:200px;">
                        <col style="width:150px;">
                        <col style="width:100px;">
                        <col style="width:100px;">
                        <col style="width:200px;">
					</colgroup>
					<thead>
					<tr>
						<th><input type="checkbox" id="check_all" title="체크박스" /></th>
						<th>No</th> <!-- 순번 -->
						<th>지자체</th>
						<th>${op:message('M00018')} <!-- 상품명 --></th>
						<th>${op:message('M00786')} <!-- 판매가격 --></th>
						<th>
							<c:choose>
								<c:when test="${itemParam.orderBy == 'HITS'}">
									${op:message('M00104')} <!-- 상호명 -->
								</c:when>
								<c:otherwise>
									${op:message('M00104')} <!-- 상호명 -->
								</c:otherwise>
							</c:choose>
						</th>
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
							<td><input type="checkbox" name="id" value="${fn:escapeXml(item.itemId)}" class="${fn:escapeXml(item.itemUserCode)}" title="" /></td>
							<td>
								<c:choose>
									<c:when test="${itemParam.orderBy == 'ORDERING' && itemParam.sort == 'ASC'}">
										${pagination.number + i.count}
									</c:when>
									<c:otherwise>
										${pagination.itemNumber - i.count}
									</c:otherwise>
								</c:choose>
								<p style="padding-top: 5px;" class="hidden">
									<c:choose>
										<c:when test='${op:property("saleson.view.type") eq "api"}'>
											<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(item.itemUserCode)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
										</c:when>
										<c:otherwise>
											<a href="/products/preview/${fn:escapeXml(item.itemUserCode)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
											<a href="/m/products/preview/${fn:escapeXml(item.itemUserCode)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_mobile.gif" alt="" /></a>
										</c:otherwise>
									</c:choose>
								</p>
							</td>
							<td>
								${fn:escapeXml(item.locgovNm)}
							</td>
							<td class="left break-word">
								<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(item.itemUserCode)}" target="_blank" class="break-word">
									<div class="flex_box item-center gap-08">
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
								<a href="/opmanager/seller/edit/${fn:escapeXml(item.sellerId)}" style="padding-top: 5px;font-size: 11px; color: #000" target="_blank"><span class="glyphicon glyphicon-user"></span>${fn:escapeXml(item.seller.companyName)}</a>
							</td>
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
						${op:message('M00473')} <!-- 데이터가 없습니다. -->
				</div>
			</c:if>
	
	
	
			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
	                <button type="button" id="delete_list_data" class="btn btn-dark-gray btn-mini">${op:message('M00074')}</button> <!-- 삭제 -->
				</div>
			</div>
	
			<div class="pagination-wrap">
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

        // 목록데이터 - 삭제처리
        $('#delete_list_data').on('click', function() {
            var $form = $('#listForm');
            if ($form.find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }
        
            Common.updateListData("/opmanager/item/representative-item/list/delete", Message.get("M00196"));	// 삭제하시겠습니까?
        });

        // 팀/그룹 ~ 4차 카테고리 이벤트
        ShopEventHandler.categorySelectboxChagneEvent();
        Shop.activeCategoryClass('${fn:escapeXml(itemParam.categoryGroupId)}', '${fn:escapeXml(itemParam.categoryClass1)}', '${fn:escapeXml(itemParam.categoryClass2)}', '${fn:escapeXml(itemParam.categoryClass3)}', '${fn:escapeXml(itemParam.categoryClass4)}');

    });

	$(function() {
	    wdrChange($("#shWdr").val());
	});
	
	var changeYn = "N";
	
	// 지차체 변경여부 체크
	$("select[name='shWdr']").on('focus', function () {
	    
	}).change(function() {
		changeYn = "Y";
	});	
	
	// 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();
		$("#shLocgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				
				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#shLocgovCode').append(options);
		        }
		        
				// 조회 된 값 유지
				if ("${fn:escapeXml(itemParam.shLocgovCode)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(itemParam.shLocgovCode)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}
				
		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}    
	
</script>


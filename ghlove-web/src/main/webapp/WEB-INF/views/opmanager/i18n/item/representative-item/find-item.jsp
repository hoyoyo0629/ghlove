<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

		<!-- 본문 -->
		<div class="popup_wrap">

			<h1 class="popup_title">${op:message('M00773')}</h1> <!-- 상품등록 -->

			<div class="popup_contents">
				<c:if test="${itemParam.targetId == 'spot'}">
				<p class="text-info">
					* 현재 핫딜이 진행 중인 상품은 검색되지 않습니다.
				</p>
				</c:if>
				<form:form modelAttribute="itemParam" method="post">
					<form:hidden path="targetId" />
					<form:hidden path="conditionType" />

					<div class="board_write">
						<table class="board_write_table" summary="관련상품등록">
							<caption>관련상품등록 </caption>
							<colgroup>
								<col style="width: 220px" />
								<col style="" />
							</colgroup>
							<tbody>
								<tr>
									<td class="label">${op:message('M00270')}</td> <!-- 카테고리 -->
									<td>
										<div class="flex_box gap-08">
											<form:select path="categoryGroupId" class="category">
												<option value="0">${op:message('M00039')}</option> <!-- 전체 -->
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

											<form:select path="categoryClass1" class="category">
											</form:select>

											<form:select path="categoryClass2" class="category">
											</form:select>

											<form:select path="categoryClass3" class="category hidden">
											</form:select>

											<form:select path="categoryClass4" class="category hidden">
											</form:select>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00011')}</td>    <!-- 검색구분 -->
									<td>
										<div class="flex_box gap-08">
											<form:select path="where" title="상세검색 선택" class="wd-150">
												<form:option value="ITEM_NAME">${op:message('M00018')}</form:option> <!-- 상품명 -->
												<form:option value="ITEM_USER_CODE">${op:message('M00783')}</form:option> <!-- 상품코드 -->
											</form:select>
											<form:input path="query" class="wd-310" title="${op:message('M00022')}" /> <!-- 검색어 -->
										</div>
									</td>
								</tr>
								<tr ${requestContext.sellerPage || itemParam.targetId == 'set' ? 'style="display:none"' : ''} class="hidden">
									<td class="label">판매자</td>
									<td>
										<div>
											<c:choose>
												<c:when test="${requestContext.opmanagerPage}">

													<select name="sellerId" title="${op:message('M01630')}"> <!-- 판매자선택 -->
														<option value="0">${op:message('M00039')}</option> <!-- 전체 -->
														<c:forEach items="${sellerList}" var="list" varStatus="i">
														<c:choose>
															<c:when test="${itemParam.sellerId == list.sellerId}">
																<c:set var='selected' value='selected'/>
															</c:when>
															<c:otherwise>
																<c:set var='selected' value=''/>
															</c:otherwise>
														</c:choose>
														<option value="${fn:escapeXml(list.sellerId)}" ${fn:escapeXml(selected)}>${fn:escapeXml(list.sellerName)}</option>
														</c:forEach>
													</select>

												</c:when>
												<c:when test="${requestContext.opmanagerPage}">
													<form:input path="sellerId" />

												</c:when>
											</c:choose>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

					</div> <!--// board_write -->

					<div class="btn_all btn_right">
						<div class="flex_box gap-08">
							<a href="/opmanager/item/edit/find-item" class="btn btn-dark-gray btn-mini">${op:message('M00047')}</a> <!-- 초기화 -->
							<button type="submit" class="btn btn-dark-gray btn-mini">${op:message('M00048')}</button> <!-- 검색 -->
						</div>
					</div> <!-- // btn_all -->
				</form:form>

				<div class="board_list" style="clear:both">
					<table class="board_list_table" summary="상품리스트">
						<caption>상품리스트</caption>
						<colgroup>
	                        <col style="width:50px;">
	                        <col style="width:500px;">
	                        <col style="width:200px;">
	                        <col style="width:200px;">
	                        <col style="width:150px;">
	                        <col style="width:150px;">
						</colgroup>
						<thead>
							<tr>
								<th></th>
								<th>${op:message('M00018')}</th> <!-- 상품명 -->
								<th>${op:message('M00783')}</th> <!-- 상품코드 -->
								<th>${op:message('M00786')}</th> <!-- 판매가 -->
								<th>${op:message('M00104')}</th> <!-- 상호명 -->
								<th>${op:message('M01630')}</th> <!-- 판매자 -->
							</tr>
						</thead>
						<tbody class="sortable">
							<c:forEach items="${list}" var="item">
							<tr>
								<td>
									<input type="radio" name="itemId" value="${fn:escapeXml(item.itemId)}" <c:if test="${ fn:escapeXml(item.representativeItemYn) == 'Y' }">checked="checked"</c:if>>

									<span class="item_info" style="display:none;">
										<span class="item_names">${fn:escapeXml(item.itemName)}</span>
										<span class="item_user_code">${fn:escapeXml(item.itemUserCode)}</span>
										<span class="seller_name">${fn:escapeXml(item.seller.sellerName)}</span>
										<span class="item_sale_price">${fn:escapeXml(item.salePrice)}</span>
										<span class="brand">${fn:escapeXml(item.brand)}</span>
										<span class="item_price">${fn:escapeXml(item.itemPrice)}</span>
										<span class="minus_spot_discount">${fn:escapeXml(item.minusSpotDiscount)}</span>
										<span class="except_spot_discount">${fn:escapeXml(item.exceptSpotDiscount)}</span>
										<span class="present_price">${fn:escapeXml(item.presentPrice)}</span>
                                        <span class="item_commission_rate">${fn:escapeXml(item.displayCommissionRate)}</span>
										<span class="display_flag">${fn:escapeXml(item.displayFlag)}</span>
										<span class="data_status_code">${fn:escapeXml(item.dataStatusCode)}</span>
										<span class="order_min_quantity">${fn:escapeXml(item.orderMinQuantity)}</span>
										<span class="order_max_quantity">${fn:escapeXml(item.orderMaxQuantity)}</span>
										<span class="item_option_flag">${fn:escapeXml(item.itemOptionFlag)}</span>
										<span class="stock_flag">${fn:escapeXml(item.stockFlag)}</span>
										<span class="stock_quantity">${fn:escapeXml(item.stockQuantity)}</span>
										<span class="item_sold_out_flag">${fn:escapeXml(item.itemSoldOutFlag)}</span>
										<span class="sold_out">${fn:escapeXml(item.soldOut)}</span>
									</span>

									<span class="item_options" style="display:none;">

										<c:forEach items="${item.itemOptionGroups}" var="itemOptionGroup">
											<span class="itemOptionGroup">
												<span class="groupItemId">${fn:escapeXml(itemOptionGroup.itemId)}</span>
												<span class="groupOptionType">${fn:escapeXml(itemOptionGroup.optionType)}</span>
												<span class="optionTitle">${fn:escapeXml(itemOptionGroup.optionTitle)}</span>
												<span class="itemOptions">
													<c:forEach items="${itemOptionGroup.itemOptions}" var="itemOption" varStatus="i">
														<span class="itemOption">
															<span class="itemOptionId">${fn:escapeXml(itemOption.itemOptionId)}</span>
															<span class="itemId">${fn:escapeXml(itemOption.itemId)}</span>
															<span class="optionType">${fn:escapeXml(itemOption.optionType)}</span>
															<span class="optionName1">${fn:escapeXml(itemOption.optionName1)}</span>
															<span class="optionName2">${fn:escapeXml(itemOption.optionName2)}</span>
															<span class="optionName3">${fn:escapeXml(itemOption.optionName3)}</span>
															<span class="optionCode">${fn:escapeXml(itemOption.optionStockCode)}</span>
															<span class="price">${fn:escapeXml(itemOption.price)}</span>
															<span class="priceNonmember">${fn:escapeXml(itemOption.optionPriceNonmember)}</span>
															<span class="stockQuantity">${fn:escapeXml(itemOption.optionStockQuantity)}</span>
															<span class="stockScheduleText">${fn:escapeXml(itemOption.optionStockScheduleText)}</span>
															<span class="stockScheduleDate">${fn:escapeXml(itemOption.optionStockScheduleDate)}</span>
															<span class="displayFlag">${fn:escapeXml(itemOption.optionDisplayFlag)}</span>
														</span>

													</c:forEach>
												</span>
											</span>
										</c:forEach>

									</span>
								</td>
								<td class="left break-word">
                                    <div class="flex_box item-center gap-08">
                                        <img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image" alt="상품이미지" />
                                        <div>
											<c:choose>
												<c:when test="${item.dataStatusCode == '1'}">
													<c:if test="${item.itemSoldOutFlag == 'Y'}"><strong style="color:red">[품절]</strong></c:if>
													<c:if test="${item.displayFlag == 'N'}"><strong style="color:red">[비공개]</strong></c:if>
												</c:when>
												<c:otherwise>
													<a href="javascript:Manager.itemLog('${fn:escapeXml(item.itemId)}')"><strong style="color:red">[비공개]</strong></a>
												</c:otherwise>
											</c:choose>
											${fn:escapeXml(item.itemName)}
                                        </div>
                                    </div>
								</td>
								<td>${fn:escapeXml(item.itemUserCode)}</td>
								<td>
									<c:if test="${not empty item.itemPrice && item.itemPrice != item.salePrice}">
										<span style="text-decoration:line-through;">${op:numberFormat(item.itemPrice)}원</span>
									</c:if>
									<p>${op:numberFormat(item.salePrice)}</p>
								</td>
								<td>${fn:escapeXml(item.seller.companyName)}</td>
								<td>${fn:escapeXml(item.seller.sellerName)}</td>
							</tr>
							</c:forEach>

						</tbody>
					</table>

					<c:if test="${empty list}">
					<div class="no_content">
						${op:message('M00473')} <!-- 데이터가 없습니다. -->
					</div>
					</c:if>

					<div class="pagination-wrap">
						<page:pagination-manager />
					</div>
				</div> <!--// board_write -->

				<p class="popup_btns">
					<a href="javascript:saveMainItem()" class="btn btn-active">${op:message('M00101')}</a> <!-- 저장 -->
					<a href="javascript:self.close();" class="btn btn-default">닫기</a>
				</p>

			</div> <!-- // popup_contents -->

			<a href="#" class="popup_close">창 닫기</a>
		</div>

<script type="text/javascript">

$(function() {
	// 팀/그룹 ~ 4차 카테고리 이벤트
	ShopEventHandler.categorySelectboxChagneEvent();

	Shop.activeCategoryClass('${fn:escapeXml(itemParam.categoryGroupId)}', '${fn:escapeXml(itemParam.categoryClass1)}', '${fn:escapeXml(itemParam.categoryClass2)}', '${fn:escapeXml(itemParam.categoryClass3)}', '${fn:escapeXml(itemParam.categoryClass4)}');
});

// 저장
function saveMainItem() {

	var $itemId =  $('input[name=itemId]');

	if (!$itemId.is(':checked')) {
		alert('대표상품으로 등록할 상품을 선택하세요.');
		return false;
	}

	var message = '선택한 상품을 대표상품으로 등록 하시겠습니까?';
	if (!confirm(message)) {
		return;
	}

	var param = {'itemId': $('input[name=itemId]:checked').val()};

	$.post('${fn:escapeXml(requestContext.managerUri)}/item/edit/representative-item/register', param, function(response){
		Common.responseHandler(response, function(){
			alert(Message.get("M00288"));	// 등록되었습니다.
			opener.location.reload();
			self.close();
		});
	});

}

</script>
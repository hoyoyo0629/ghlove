<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>


	<!-- 개발 영역 -->
    <!-- 답례품 등록 popup -->
    <div class="popup_wrap">
        <div id="pop_header">
            <h1 class="popup_title">${op:message('M01185')}</h1> <!-- 답례품검색 -->
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

        <div class="popup_contents">
			<form:form modelAttribute="itemParam" method="post">
				<!-- form:hidden으로 하면 targetId가 페이지 이동할 때마다 prod,prod,prod,...로 붙음 -->
				<input type="hidden" id="targetId" value="${itemParam.targetId}" />
				<form:hidden path="conditionType" />

	            <div class="board_write">
	                <table class="board_write_table" summary="관련답례품등록">
	                    <colgroup>
	                        <col style="width:220px;">
	                    </colgroup>
	                    <tbody>
	                        <tr>
	                            <td class="label">${op:message('M00270')}</td> <!-- 카테고리 -->
	                            <td>
	                                <div class="flex_box gap-08">
	                                	<form:select path="categoryGroupId" class="category" title="1차 카테고리">
	                                		<option value="0">${op:message('= 1차 카테고리 =')}</option>		<!-- 선택 -->
	                                		<c:forEach items="${categoryTeamGroupList}" var="categoriesTeam">
												<c:if test="${categoriesTeam.categoryTeamFlag == 'Y'}">
													<%-- <optgroup label="${categoriesTeam.name}"> --%>
													<c:forEach items="${categoriesTeam.categoriesGroupList}" var="categoriesGroup">
														<c:if test="${categoriesGroup.categoryGroupFlag == 'Y'}">
															<form:option value="${fn:escapeXml(categoriesGroup.categoryGroupId)}" label="${fn:escapeXml(categoriesGroup.groupName)}" />
														</c:if>
													</c:forEach>
													</optgroup>
												</c:if>
											</c:forEach>
	                                	</form:select>

	                                	<form:select path="categoryClass1" class="category" title="2차 카테고리">
										</form:select>

										<form:select path="categoryClass2" class="category" title="3차 카테고리">
										</form:select>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="label">${op:message('M00011')}</td>    <!-- 검색구분 -->
	                            <td>
	                                <div class="flex_box gap-08">
										<form:select path="where" title="${op:message('M00011')}" class="wd-150">    <!-- 검색구분 -->
											<form:option value="ITEM_NAME">${op:message('M00018')}</form:option> <!-- 답례품명 -->
											<form:option value="ITEM_USER_CODE">${op:message('M00019')}</form:option> <!-- 답례품번호 -->
										</form:select>
										<form:input path="query" class="input_txt required _filter full" title="${op:message('M00022')}" /> <!-- 검색어 -->
	                                </div>
	                            </td>
	                        </tr>
	                        <tr ${requestContext.sellerPage || itemParam.targetId == 'set' ? 'style="display:none"' : ''}>
								<td class="label">판매자</td>
								<td>
									<div>
										<c:choose>
											<c:when test="${requestContext.opmanagerPage}">

												<select name="sellerId" title="${op:message('M01630')}" class="wd-150"> <!-- 판매자선택 -->
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
	            </div>

	            <div class="btn_all btn_right">
	                <div class="flex_box gap-08">
	                    <button type="button" class="btn btn-dark-gray btn-mini"
	                        onclick="location.href='${fn:escapeXml(requestContext.managerUri)}/item/find-item?targetId=${fn:escapeXml(itemParam.targetId)}';">${op:message('M00047')}</button>		<!-- 초기화 -->
	                    <button type="submit" class="btn btn-dark-gray btn-mini">${op:message('M00048')}</button>		<!-- 검색 -->
	                </div>
	            </div>
			</form:form>

            <!-- 조회 결과 테이블 -->
            <div class="board_list">
                <table class="board_list_table" summary="답례품 등록">
                    <caption>답례품 등록</caption>
                    <colgroup>
                    <c:if test="${param.targetId ne 'favProd'}">
                        <col style="width:50px;">
					</c:if>
                        <col style="width:500px;">
                        <col style="width:200px;">
                        <col style="width:200px;">
                        <col style="width:150px;">
                        <col style="width:150px;">
                    </colgroup>
                    <thead>
                        <tr>
                        	<c:if test="${param.targetId ne 'favProd'}">
                            	<th scope="col"><input type="checkbox" id="check_all"></th>
                            </c:if>
                            <th>${op:message('M00018')}</th>	<!-- 답례품명 -->
                            <th>${op:message('M00783')}</th>	<!-- 답례품코드 -->
                            <th>${op:message('M00786')}</th>	<!-- 판매자 -->
                            <th>${op:message('M00104')}</th>	<!-- 상호명 -->
                            <th>${op:message('M01630')}</th>	<!-- 판매자명 -->
                        </tr>
                    </thead>
                    <tbody class="sortable">
                    	<c:forEach items="${list}" var="item">
	                        <tr id="item_${fn:escapeXml(item.itemId)}">
	                            <!-- 체크박스 -->
	                            <c:if test="${param.targetId ne 'favProd'}">
		                            <td>
		                                <div>
		                                    <input type="checkbox" name="id" value="${fn:escapeXml(item.itemId)}" />

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
		                                </div>
		                            </td>
	                            </c:if>
	                            <!-- // 체크박스 -->
	                            <!-- 답례품명 -->
	                            <td class="left break-word">
	                                <%-- <a href="javascript:Link.view('/opmanager/item/seller/edit/${item.itemUserCode}')" class="break-word"> --%>
	                                    <div class="flex_box item-center gap-08">
	                                        <img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image" alt="답례품이미지" />
	                                        <div>
	                                        	<span style="color:#e84700" class="mr5">
													<c:choose>
		                                        		<c:when test="${item.dataStatusCode == '1'}">
			                                        		<c:if test="${item.itemSoldOutFlag == 'Y'}">[품절]</c:if>
															<c:if test="${item.displayFlag == 'N'}">[비공개]</c:if>
														</c:when>
														<c:otherwise>
															<a href="javascript:Manager.itemLog('${fn:escapeXml(item.itemId)}')">[비공개]</a>
														</c:otherwise>
													</c:choose>
	                                        	</span>
	                                        	<c:if test="${param.targetId eq 'favProd'}">
	                                        		<a href="javascript:addSelectItem('${item.itemId}');">
														<c:out value="${item.itemName}"/>
													</a>
													<span class="item_info" style="display:none;">
														<span class="item_names">${item.itemName}</span>
														<span class="item_user_code">${item.itemUserCode}</span>
														<span class="seller_name">${item.seller.sellerName}</span>
														<span class="item_sale_price">${item.salePrice}</span>
														<span class="brand">${item.brand}</span>
														<span class="item_price">${item.itemPrice}</span>
														<span class="minus_spot_discount">${item.minusSpotDiscount}</span>
														<span class="except_spot_discount">${item.exceptSpotDiscount}</span>
														<span class="present_price">${item.presentPrice}</span>
				                                        <span class="item_commission_rate">${item.displayCommissionRate}</span>
														<span class="display_flag">${item.displayFlag}</span>
														<span class="data_status_code">${item.dataStatusCode}</span>
														<span class="order_min_quantity">${item.orderMinQuantity}</span>
														<span class="order_max_quantity">${item.orderMaxQuantity}</span>
														<span class="item_option_flag">${item.itemOptionFlag}</span>
														<span class="stock_flag">${item.stockFlag}</span>
														<span class="stock_quantity">${item.stockQuantity}</span>
														<span class="item_sold_out_flag">${item.itemSoldOutFlag}</span>
														<span class="sold_out">${item.soldOut}</span>
													</span>
	                                        	</c:if>
	                                        	<c:if test="${param.targetId ne 'favProd'}">
	                                        		${fn:escapeXml(item.itemName)}
	                                        	</c:if>
                                        	</div>
	                                    </div>
	                                <!-- </a> -->
	                            </td>
	                            <!-- // 답례품명 -->
	                            <!-- 답례품코드 -->
	                            <td>
	                                <div>${fn:escapeXml(item.itemUserCode)}</div>
	                            </td>
	                            <!-- // 답례품코드 -->
	                            <!-- 판매가 -->
	                            <td>
	                                <div>${op:numberFormat(item.salePrice)}원</div>
	                            </td>
	                            <!-- // 판매가 -->
	                            <!-- 상호명 -->
	                            <td>
	                                <div>${fn:escapeXml(item.seller.companyName)}</div>
	                            </td>
	                            <!-- // 상호명 -->
	                            <!-- 판매자 -->
	                            <td>
	                                <div>${fn:escapeXml(item.seller.sellerName)}</div>
	                            </td>
	                            <!-- // 판매자 -->
	                        </tr>
                        </c:forEach>
                    </tbody>
                </table>

				<c:if test="${empty list}">
					<div class="no_content">
						${op:message('M00473')} <!-- 데이터가 없습니다. -->
					</div>
				</c:if>
                <!-- 페이지네이션 -->
                <div class="pagination-wrap">
                    <page:pagination-manager />
                </div>
                <!-- // 페이지네이션 -->
            </div> <!-- // board_list -->
            <!-- // 조회 결과 테이블 -->

            <p class="popup_btns">
            	<c:if test="${param.targetId ne 'favProd'}">
                	<button type="button" class="btn btn-active" onclick="javascript:addCheckedRelationItem();">${op:message('M00192')}</button> <!-- 추가 -->
                </c:if>
                <button type="button" class="btn btn-default" onclick="javascript:self.close();">${op:message('M00569')}</button>	<!-- 닫기 -->
            </p>
        </div>
    </div>
    <!-- // 답례품 등록 popup -->
    <!-- // 개발 영역 -->


<script type="text/javascript">
$(function() {
	// 팀/그룹 ~ 4차 카테고리 이벤트
	ShopEventHandler.categorySelectboxChagneEvent();

	Shop.activeCategoryClass('${fn:escapeXml(itemParam.categoryGroupId)}', '${fn:escapeXml(itemParam.categoryClass1)}', '${fn:escapeXml(itemParam.categoryClass2)}', '${fn:escapeXml(itemParam.categoryClass3)}', '${fn:escapeXml(itemParam.categoryClass4)}');
});

function addRelationItem(itemId, messageDisplay) {
	var display = true;
	if (messageDisplay == false) {
		display = false;
	}

	var targetId = $('#targetId').val();

	// 추천 테마 상품 갯수 확인
	if (targetId === 'mainTheme') {
		if(!opener.Shop.isAvilableRelationItemCount(targetId, 4)){
			Message.danger("한 테마에 상품은 최대 4개까지 등록 가능합니다.");
			return;
		}
	}

	if (targetId === 'related') {
		if(!opener.Shop.isAvilableRelationItemCount(targetId, 20)){
			Message.danger("관련상품은 최대 20개까지 등록 가능합니다.");
			return;
		}
	}

	if (targetId === 'set') {
		if(!opener.Shop.isAvilableRelationItemCount(targetId, 10)){
			Message.danger("세트상품은 최대 10개까지 등록 가능합니다.");
			return;
		}
	}

	if (targetId === 'favProd') {
		if(!opener.Shop.isAvilableRelationItemCount(targetId, 1)){
			Message.danger("상품은 최대 1개까지 등록 가능합니다.");
			return;
		}
	}

	if (opener.Shop.isAddedRelationItem(targetId, itemId)) {
		if (display) {
			var message = Message.get("M01215");	// 이미 추가한 상품입니다.
			Message.danger(message);
		}
		return;
	}

	var $item = $('#item_' + itemId);

	var item = {
		'itemId' : itemId,
		'itemName' : $item.find('.item_names').text(),
		'itemUserCode' : $item.find('.item_user_code').html(),
		'itemImage' : $item.find('.item_image').attr('src'),
		'itemSalePrice' : $item.find('.item_sale_price').text(),
		'brand' : $item.find('.brand').text(),
		'itemPrice' : $item.find('.item_price').text(),
		'presentPrice' : $item.find('.present_price').text(),
		'sellerName' : $item.find('.seller_name').text(),
		'minusSpotDiscount' : $item.find('.minus_spot_discount').text(),
		'exceptSpotDiscount' : $item.find('.except_spot_discount').text(),
		'itemOptions' : [],
        'commissionRate' : $item.find('.item_commission_rate').text(),
		'displayFlag'	 : $item.find('.display_flag').text(),
		'dataStatusCode' : $item.find('.data_status_code').text(),
		'orderMinQuantity' : $item.find('.order_min_quantity').text(),
		'orderMaxQuantity' : $item.find('.order_max_quantity').text(),
		'itemOptionFlag' : $item.find('.item_option_flag').text(),
		'stockFlag' : $item.find('.stock_flag').text(),
		'stockQuantity' : $item.find('.stock_quantity').text(),
		'itemSoldOutFlag' : $item.find('.item_sold_out_flag').text(),
		'soldOut' : $item.find('.sold_out').text(),
		'targerId' : targetId
	};

	// 상품 옵션 정보
	var itemOptionGroups = [];

	$item.find('span.itemOptionGroup').each(function() {
		var itemOptionGroup = {};
		itemOptionGroup.itemId = $(this).find('span.groupItemId').text();
		itemOptionGroup.optionType = $(this).find('span.groupOptionType').text();
		itemOptionGroup.optionTitle = $(this).find('span.optionTitle').text();

		var itemOptions = [];
		$(this).find('span.itemOption').each(function() {

			var itemOption = {};
			$(this).find('span').each(function() {

				itemOption[$(this).attr('class')] = $(this).text();
			});
			itemOptions.push(itemOption);

		});

		itemOptionGroup.itemOptions = itemOptions;
		itemOptionGroups.push(itemOptionGroup);

	});

	item.itemOptionGroups = itemOptionGroups;

	opener.Shop.addRelationItem(targetId, item);

	if (display) {
		Message.success(Message.get("M01217"));	// 추가하였습니다.
	}
}

// 체크한 상품 일괄 추가
function addCheckedRelationItem() {
	var targetId = $('#targetId').val();
	if ($('input[name=id]:checked').size() == 0) {
		var message = Message.get("M01216");	// 추가할 상품을 선택해 주세요.
		alert(message);
		$('#check_all').focus();
		return;
	}

	// 추천 테마 상품 갯수 확인
	if (targetId == 'mainTheme') {
		if($('input[name=id]:checked').size() > 4) {
			Message.danger("한 테마에 상품은 최대 4개까지 등록 가능합니다.");
			return;
		}
	}

	if (targetId == 'related') {
		if($('input[name=id]:checked').size() > 20) {
			Message.danger("관련상품등록은 최대 20개까지 등록 가능합니다.");
			return;
		}
	}

	$('input[name=id]:checked').each(function() {
		var itemId = $(this).val();
		addRelationItem(itemId, false);
	});

	// Message.success(Message.get("M01217"));	// 추가하였습니다.
	alert(Message.get("상품이 추가되었습니다."));
}

//체크한 상품 추가
function addSelectItem(itemId) {
	addRelationItem(itemId, false);
	alert(Message.get("상품이 추가되었습니다."));
	self.close();
}

</script>
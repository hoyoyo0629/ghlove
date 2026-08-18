<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<!-- <div class="admin_wrap"> -->
<div>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<div class="item_list">
		<h3><span>상품리스트</span></h3>

		<form:form modelAttribute="itemParam" method="post">
			<form:hidden path="categoryId" />

			<div class="board_write">
				<table class="board_write_table" summary="상품리스트">
					<caption>상품리스트</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
					<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
                    <tr>
                        <td class="label">지자체</td>
                        <td colspan="3">
                            <div class="flex_box gap-08">
	                            <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
				                    <form:option value="">-${op:message('M00039')}-</form:option>
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
                    </c:if>
					<tr>
						<td class="label">${op:message('M00011')}</td>    <!-- 검색구분 -->
						<td colspan="3">
							<div class="flex_box gap-08">
								<form:select path="where" title="상세검색 선택" class="wd-150">
									<%-- <form:option value="">${op:message('M01223')} <!-- 구분 --></form:option> --%>
									<form:option value="ITEM_NAME">${op:message('M00018')} <!-- 상품명 --></form:option>
									<form:option value="ITEM_USER_CODE">${op:message('M00783')} <!-- 상품코드 --></form:option>
									<form:option value="COMPANY_NAME">${op:message('M00104')} <!-- 상호명 --> </form:option>
									<%-- <form:option value="ITEM_SELLER_CODE" label="고유코드" /> --%>
								</form:select>
								<form:input path="query" class="full" title="상세검색 입력" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">검색일자</td>
						<td colspan="3">
							<div>
								<form:select path="srchDateType" title="검색일자구분 선택" class="wd-150">
									<form:option value="create">${op:message('M00202')} <!-- 등록일 --></form:option>
									<form:option value="update">수정일</form:option>
								</form:select>
								<span class="datepicker"><form:input path="searchStartDate" maxlength="8" class="datepicker" title="${op:message('M00024')}" /><!-- 주문일자 시작일 --></span>
								<span class="wave">~</span>
								<span class="datepicker mr10"><form:input path="searchEndDate" maxlength="8" class="datepicker" title="${op:message('M00025')}" /><!-- 주문일자 종료일 --></span>
								<span class="day_btns mt3" style="margin-left:0px;">
									<!-- <a href="javascript:;" class="btn_date clear">전체</a> -->
									<a href="javascript:;" class="btn_date btn_created_date today">${op:message('M00026')}</a><!-- 오늘 -->
									<a href="javascript:;" class="btn_date btn_created_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
									<a href="javascript:;" class="btn_date btn_created_date month-1">${op:message('M00029')}</a><!-- 한달 -->
									<a href="javascript:;" class="btn_date btn_created_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
									<a href="javascript:;" class="btn_date btn_created_date year-1">${op:message('M00031')}</a><!-- 1년 -->
                                    <c:choose>
                                        <c:when test="${op:hasRole('ROLE_ADMIN_1') or op:hasRole('ROLE_ADMIN_5')}">
                                            <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
                                        </c:when>
                                    </c:choose>
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">${op:message('M00270')} <!-- 카테고리 --></td>
						<td colspan="3">
							<div>
								<form:select path="categoryGroupId" class="category">
									<option value="0">= 1차 카테고리 =</option>
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

								<form:select path="categoryClass3" class="category" style="display:none">
								</form:select>

								<form:select path="categoryClass4" class="category" style="display:none">
								</form:select>
							</div>
						</td>
					</tr>

					<tr>
						<td class="label">${op:message('M00787')} <!-- 판매상태 --></td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<form:radiobutton path="saleStatus" value="" label="${op:message('M00039')}" /> <!-- 전체 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="saleStatus" value="sale" label="${op:message('M00694')}" /> <!-- 판매중 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="saleStatus" value="soldOut" label="${op:message('M00693')}" /> <!--  품절 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="saleStatus" value="saleEnd" label="${op:message('M00695')}" /> <!-- 판매종료 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="saleStatus" value="mig" label="임시등록" /> <!-- 임시등록 -->
								</div>
							</div>
						</td>
						<td class="label">${op:message('M00191')} <!-- 공개유무 --></td>
						<td>
							<div class="flex_box gap-12">
								<div class="input-form">
									<form:radiobutton path="displayFlag" value="" label="${op:message('M00039')}" /> <!-- 전체 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="displayFlag" value="Y" label="${op:message('M00096')}" /> <!-- 공개 -->
								</div>
								<div class="input-form">
									<form:radiobutton path="displayFlag" value="N" label="${op:message('M00097')}" /> <!-- 비공개 -->
								</div>
							</div>
						</td>
					</tr>
					<tr class="hidden">
						<td class="label">판매자</td>
						<td colspan="3">
							<div class="flex_box gap-08">
								<select name="sellerId" id="sellerId" title="${op:message('M01630')}" class="wd-150"> <!-- 판매자선택 -->
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
								<a href="javascript:Common.popup('/opmanager/seller/find', 'find_seller', 800, 500, 1)" class="btn btn-gradient btn-sm">검색</a>
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
		        </ul>
		  	</div>
			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/item/list';"> ${op:message('M00047')}</button> <!-- 초기화 -->
					<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> ${op:message('M00048')}</button> <!-- 검색 --> --%>
					<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
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

								<c:if test="${!empty itemParam.categoryId}">
									<!-- <button type="button" id="change_ordering2" class="btn ctrl_btn " style="position:fixed;right:0; bottom:260px; z-index: 1000;">${op:message('M00791')}</button> <!-- 정렬순서변경 -->
								</c:if>
							</span>
			</div>
		</form:form>

		<div class="board_guide" style="border:1px solid #d5d5d5; padding: 15px; margin-top:15px; display: none">
			<p class="tip">
				<a href="javascript:;" class="btn_write gray_small" onclick="javascript:shoppingHow()"><span>쇼핑하우 txt파일 생성</span> </a>
			</p>
			<p class="tip">
				<br/>쇼핑하우 상품정보 파일을 생성합니다.
			</p>
		</div>

		<div class="board_list">
			<form id="listForm">
				<table class="board_list_table" summary="전체상품리스트">
					<caption>전체상품리스트</caption>
					<colgroup>
                        <col style="width: 40px" />
                        <col style="width: 50px;" />
                        <c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
                        <col style="width: 150px;" />
                        </c:if>
                        <col style="width: 100px;" />
                        <col style="width: 150px;">
                        <col style="width: 300px;">
                        <col style="width: 200px;">
                        <col style="width: 40px;">
                        <col style="width: 100px;">
                        <col style="width: 150px;">
                        <col style="width: 100px;">
                        <col style="width: 200px;">
                        <col style="width: 100px;">
                        <col style="width: 120px;">
                        <col style="width: 120px;">
					</colgroup>
					<thead>
					<tr>
						<th><input type="checkbox" id="check_all" title="체크박스" /></th>
						<th>No</th> <!-- 순번 -->
						<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
						<th>지자체</th>
						</c:if>
						<th>${op:message('M00787')} <!-- 판매상태 --></th>
						<th>${op:message('M00752')}</th> <!-- 이미지 -->
						<th>${op:message('M00001')} <!-- 상품 --></th>
						<th>${op:message('M00786')} <!-- 판매가격 --></th>
						<th>옵션</th>
						<th>필수추가정보</th>
						<th>${op:message('M01462')} <!-- 재고수 --></th>
						<th>${op:message('M00191')} <!-- 공개유무 --></th>
						<th>
							<c:choose>
								<c:when test="${itemParam.orderBy == 'HITS'}">
									${op:message('M00685')}/${op:message('M00685')} <!-- 상호/조회수 -->
								</c:when>
								<c:otherwise>
									${op:message('M01635')}/${op:message('M00202')} <!-- 상호/등록일 -->
								</c:otherwise>
							</c:choose>
						</th>
						<th>수정일</th>
						<th>변경사항</th>
						<th>${op:message('M00590')}</th>	 <!-- 관리 -->
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
							<c:when test="${item.dataStatusCode == '0'}">	<!-- 1차 DB 이관 답례품 -->
								<c:set var="itemSaleStatusText">임시등록</c:set>
							</c:when>
							<c:when test="${item.itemSoldOutFlag == 'Y'}">
								<c:set var="itemSaleStatusText"><span style="color:#e84700">${op:message('M00693')}</span></c:set>	 <!-- 품절 -->
							</c:when>
							<c:otherwise>
								<c:set var="itemSaleStatusText">${op:message('M00694')}</c:set> <!-- 판매중 -->
							</c:otherwise>
						</c:choose>

						<c:set var="param" value="/opmanager/item/${item.itemType == '3' ? 'set-edit' : 'edit'}/${item.itemUserCode}" />

						<tr id="item_${item.itemId}">
							<td><input type="checkbox" name="id" value="${fn:escapeXml(item.itemId)}" class="${fn:escapeXml(item.itemUserCode)}" title="" />
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
									<span class="item_text_option_flag">${fn:escapeXml(item.itemTextOptionFlag)}</span><!-- 20260325 필수 추가정보 isb 수정 -->
									<span class="stock_flag">${fn:escapeXml(item.stockFlag)}</span>
									<span class="stock_quantity">${fn:escapeXml(item.stockQuantity)}</span>
									<span class="item_sold_out_flag">${fn:escapeXml(item.itemSoldOutFlag)}</span>
									<span class="sold_out">${fn:escapeXml(item.soldOut)}</span>
								</span>
							</td>
							<td>
								<c:choose>
									<c:when test="${itemParam.orderBy == 'ORDERING' && itemParam.sort == 'ASC'}">
										${fn:escapeXml(pagination.number + i.count)}
									</c:when>
									<c:otherwise>
										${fn:escapeXml(pagination.itemNumber - i.count)}
									</c:otherwise>
								</c:choose>
								<c:if test="${item.displayFlag == 'Y' && item.dataStatusCode == '1'}">
									<p style="padding-top: 5px;">
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
								</c:if>
							</td>
							<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
								<td>
									${fn:escapeXml(item.locgovNm)}
								</td>
							</c:if>
							<td>${op:removeIframe(itemSaleStatusText)}</td>
							<td>
								<div>
									<a href="javascript:Link.view('/opmanager/item/edit/${fn:escapeXml(item.itemUserCode)}')"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image" alt="상품이미지" /></a>
								</div>
							</td>
							<td class="left break-word">
								<a href="javascript:Link.view('/opmanager/item/edit/${fn:escapeXml(item.itemUserCode)}')" class="break-word">
									[${fn:escapeXml(item.itemUserCode)}]<br/>
									<c:if test="${item.itemSellerCode != '' && item.itemSellerCode != null}">
										(${fn:escapeXml(item.itemSellerCode)})<br>
									</c:if>
									${op:removeIframe(item.itemName)}
								</a>
								<a href="javascript:Link.view('/opmanager/item/edit/${fn:escapeXml(item.itemUserCode)}', 1)" class="break-word" style="color:#517BAB">[새탭]</a>
								<c:if test="${item.itemLabel == '2'}">
									<img src="/content/opmanager/images/icon/icon_new2.gif" alt="new" />
								</c:if>
							</td>

							<td class="text-center">
								${op:numberFormat(item.salePrice)}P
							</td>
							<td>
								${fn:escapeXml(item.itemOptionFlag)}
							</td>
							<td>
								${fn:escapeXml(item.itemTextOptionFlag)}<!-- 20260325 필수 추가정보 isb 수정 -->
							</td>
							<td>
								<c:choose>
									<c:when test="${item.stockFlag == 'Y'}">
										<c:choose>
											<c:when test="${item.itemType == '3'}">
												-
											</c:when>
											<c:when test="${item.stockQuantity == -1}">
												${op:message('M01497')} <!-- 무제한 -->
											</c:when>
											<c:otherwise>
												${op:numberFormat(item.stockQuantity)}개
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										${op:message('M01497')} <!-- 무제한 -->
									</c:otherwise>
								</c:choose>
							</td>

							<td>${op:removeIframe(displayFlagText)}</td>

							<td>
								<a href="/opmanager/seller/edit/${fn:escapeXml(item.sellerId)}" style="padding-top: 5px;font-size: 11px; color: #000" target="_blank"><span class="glyphicon glyphicon-user"></span>${fn:escapeXml(item.seller.companyName)}</a>
								<c:choose>
									<c:when test="${itemParam.orderBy == 'HITS'}">
										<br />${op:numberFormat(item.hits)}
									</c:when>
									<c:otherwise>
										<br />${op:date(item.createdDate)}
									</c:otherwise>
								</c:choose>
							</td>
							<td>${op:date(item.updatedDate)}</td>
							<td>
								<div class="flex_box juc-center">
									<button type="button" class="btn btn-gradient btn-xs" onclick="javascript:Manager.itemLog('${fn:escapeXml(item.itemId)}')">보기</button>
								</div>
							</td>
							<td>
								<div class="flex_box juc-center gap-08">
<%-- 									<a href="/opmanager/item/copy/${fn:escapeXml(item.itemId)}" class="btn btn-gradient btn-xs">${op:message('M00788')}</a> <!-- 복사 --> --%>
									<a href="#" class="delete_item btn btn-gradient btn-xs" style="margin-top: 1px;">${op:message('M00074')}</a> <!-- 삭제 -->
								</div>
							</td>
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



			<div class="flex_box juc-sbt">
				<div class="btn_all">
					<!-- <a href="/opmanager/item/create" class="btn btn-active btn-sm"><span class="glyphicon glyphicon-plus"></span> ${op:message('M00773')}</a> <!-- 상품등록 -->
					<a href="javascript:downloadExcel()" class="btn btn-dark-gray btn-mini">엑셀</a> <!-- 엑셀 다운로드 -->
					<!-- <a href="javascript:uploadExcel()" class="btn btn-success btn-sm"><span class="glyphicon glyphicon-open" aria-hidden="true"></span> ${op:message('M00793')}</a> <!-- 엑셀 업로드 -->

					<!-- a href="javascript:openApilits()" class="btn_write gray_small"><span>APILITS</span> </a -->
					<!--
	                <a href="javascript:downloadCsv('item')">다운로드 CSV</a>
	                <a href="javascript:Common.popup('/opmanager/item/upload-csv', 'upload-csv', 400, 500, 0)" class="btn_write gray_small"><img src="/content/opmanager/images/icon/icon_excel.png" alt=""><span>CSV 업로드</span> </a>
	                -->
				</div>
				<div class="btn_all">
					<div class="flex_box gap-08">
						<%--
		                <button type="button" id="update_list_data" class="btn btn-default btn-sm">${op:message('M00792')}</button> <!-- 일괄수정 -->
		                --%>
		                <c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
		                	<button type="button" id="update_list_data_sold_out" onclick="locgovItem()" class="btn btn-default btn-mini">대표답례품추가</button>
		                </c:if>
		                <c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
		                	<button type="button" id="update_list_data_sold_out" onclick="locgovItemOff()" class="btn btn-default btn-mini">오프라인답례품추가</button>
		                </c:if>
						<button type="button" id="update_list_data_sold_out" onclick="updateListDataLabel('1')" class="btn btn-default btn-mini">품절</button>
						<button type="button" id="update_list_data_sold_out" onclick="updateListDataLabel('0')" class="btn btn-default btn-mini">품절해제</button>
						<button type="button" id="update_list_data_display" onclick="updateListDataDisplay('Y')" class="btn btn-default btn-mini">공개</button>
						<button type="button" id="update_list_data_display" onclick="updateListDataDisplay('N')" class="btn btn-default btn-mini">비공개</button>
						<button type="button" onclick="location.href='/opmanager/item/create'" class="btn btn-default btn-mini">${op:message('M00088')}</button>
						<!-- <button type="button" id="update_list_data_sale_off" onclick="updateListDataLabel('90')" class="btn btn-default btn-mini">판매종료처리</button>


		                <button type="button" id="update_list_data_resale" class="btn btn-dark-gray btn-sm">품절해제</button>

						<button type="button" id="update_list_data_add_category" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-plus"></span> ${op:message('M00789')}</button> <!-- 카테고리추가 -->

						<c:if test="${!empty itemParam.categoryId}">
							<!-- <button type="button" id="change_ordering" class="btn btn-dark-gray btn-sm">${op:message('M00791')}</button> <!-- 정렬순서변경 -->
						</c:if>
					</div>
				</div>
			</div>

			<div class="pagination-wrap">
				<page:pagination-manager />
			</div>

		</div> <!-- // board_list -->



		<div class="board_guide ml10" style="display:none">
			<p class="tip">Tip</p>
			<p class="tip">${op:message('M01414')}</p> <!-- 상품순서 변경은 1차카테고리까지 선택 후 검색한 뒤 -> 기본값 정렬옵션이 정렬.오름차순 -> 순서변경 클릭 -> 상품 드래그 -> 순서변경 -->
			<p class="tip">품절해지, 판매종료처리 해지는 상세페이지에서 가능합니다.</p>
		</div>

		<c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || (itemParam.shLocgovCode != null && !itemParam.shLocgovCode.isEmpty())}">
	        <div id="relationItem">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
	                    <col />
	                </colgroup>
	                <tbody>
						<tr>
							<td class="label"><c:out value="${op:message('대표 답례품 관리')}"/><br><c:out value="${op:message('(최대 4개)')}"/></td><!-- 선택 답례품 -->
							<td>
								<div class="flex_box gap-08 mb10">
									<c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
										<button type="button" class="btn btn-default btn-sm" onclick="javascript:saveRelationItem();" ><span><c:out value="현재상태 저장"/> <!-- 저장 --> </span></button>
										<button type="button" class="btn btn-default btn-sm" onclick="javascript:deleteAllItem();" ><span><c:out value="${op:message('M00411')}"/> <!-- 전체삭제 --> </span></button>
									</c:if>
								</div>

								<div id="product">
									<ul id="rprsProd" class="sortable_item_relation">
										<c:forEach items="${lclgvRprsGdsList}" var="item" varStatus="i">
											<c:if test="${!empty item.itemId}">
												<li id="rprsProd_item_${item.itemId}">
													<input type="hidden" name="rprsProdItemIds" value="${item.itemId}" />
													<p class="image"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image size-100 none" alt="${op:message('M00659')}" /></p><!-- 답례품이미지 -->
													<p class="title">
														<c:choose>
															<c:when test="${item.dataStatusCode == '1'}">
																<c:if test="${item.itemSoldOutFlag == 'Y'}"><strong style="color:red">[<c:out value="${op:message('M00693')}"/>]</strong></c:if><%-- 품절 --%>
																<c:if test="${item.displayFlag == 'N'}"><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></c:if><%-- 비공개 --%>
															</c:when>
															<c:otherwise>
																<%-- <a href="javascript:Manager.itemLog('${item.itemId}')"> --%><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></a>	<%-- 비공개 --%>
															</c:otherwise>
														</c:choose>
														[<c:out value="${item.itemUserCode}"/>] <label style="color:red">[<c:out value="${op:numberFormat(item.salePrice)}"/>]</label><br /><c:out value="${item.itemName}"/>
													</p>
													<span class="ordering"><%-- <c:out value="${i.count}"/> --%></span>

													<a href="javascript:Shop.deleteRelationItem('rprsProd_item_${item.itemId}');" style="position: absolute;top: 5px; right: 5px;"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
												</li>
											</c:if>
										</c:forEach>
									</ul>
								</div>
							</td>
			            </tr>
	                </tbody>
	            </table>
			</div>

			<br>
			<!-- 오프라인 대표 답례품 관리 -->
			<div id="relationItemOff">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
	                    <col />
	                </colgroup>
	                <tbody>
						<tr>
							<td class="label"><c:out value="${op:message('오프라인 대표 답례품 관리')}"/><br><c:out value="${op:message('(최대 2개)')}"/></td><!-- 선택 답례품 -->
							<td>
								<div class="flex_box gap-08 mb10">
									<c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
										<button type="button" class="btn btn-default btn-sm" onclick="javascript:saveRelationItemOff()" ><span><c:out value="현재상태 저장"/> <!-- 저장 --> </span></button>
										<button type="button" class="btn btn-default btn-sm" onclick="javascript:deleteAllItemOff()" ><span><c:out value="${op:message('M00411')}"/> <!-- 전체삭제 --> </span></button>
									</c:if>
								</div>

								<div id="product">
									<ul id="rprsProdOff" class="sortable_item_relation">
										<c:forEach items="${lclgvOffRprsGdsList}" var="item" varStatus="i">
											<c:if test="${!empty item.itemId}">
												<li id="rprsProdOff_item_${item.itemId}">
													<input type="hidden" name="rprsProdOffItemIds" value="${item.itemId}" />
													<input type="hidden" id="default_price_${item.itemId }" value="${item.salePrice }">
													<input type="hidden" id="option_price_${item.itemId }" value="">
													<label style="color:red;">
														<span id="price_area_${item.itemId }">
															[<c:out value="${op:numberFormat(item.salePrice)}P 답례품"/>]
														</span>
													</label>
													<p class="image"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image size-100 none" alt="${op:message('M00659')}" /></p><!-- 답례품이미지 -->
													<p class="off_prod">
														<c:choose>
															<c:when test="${item.dataStatusCode == '1'}">
																<c:if test="${item.itemSoldOutFlag == 'Y'}"><strong style="color:red">[<c:out value="${op:message('M00693')}"/>]</strong></c:if><%-- 품절 --%>
																<c:if test="${item.displayFlag == 'N'}"><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></c:if><%-- 비공개 --%>
															</c:when>
															<c:otherwise>
																<strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></a>	<%-- 비공개 --%>
															</c:otherwise>
														</c:choose>
														<%-- <c:out value="${item.itemName}"/> --%>
														<span id="itemName_${item.itemId}"><c:out value="${item.itemName}"/></span>
													</p>
													<a href="javascript:Shop.deleteRelationItemOff('rprsProdOff_item_${item.itemId}');" style="position: absolute;top: 5px; right: 5px;"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
												</li>
											</c:if>
										</c:forEach>
									</ul>
								</div>
							</td>
			            </tr>
	                </tbody>
	            </table>
			</div>
		<br>
		<div>
		    <ul class="list-bullet point">
		        <li>
		            <strong>오프라인 대표 답례품</strong>은 <strong>최대 2개</strong>까지만 등록이 가능합니다.
		        </li>
		        <li>
		        	<strong>오프라인 대표 답례품</strong>은 <strong>옵션 가격을 포함</strong>하여 <strong>30,000P와 15,000P 각 1개씩만</strong> 등록이 가능합니다.
		        </li>
		        <li>
		            <strong>오프라인 기탁서 등록</strong>시 재고가 <strong>10개 </strong>이상인 경우에 노출됩니다.
		        </li>
		    </ul>
		</div>
		</c:if>
	</div>
</div>



<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>
<script type="text/javascript">
    $(function() {
        if (isChangingOrdering()) {
            // 관련상품 drag sortable
            $(".sortable").sortable({
                placeholder: "sortable-placeholder"
            });
            $(".sortable").disableSelection();

            $('#change_ordering, #change_ordering2').css({'background-color': '#25a5dc', 'border': '1px solid #25a5dc'});
        } else {
            $('#change_ordering, #change_ordering2').css({'background-color': '#c34e00', 'border': '1px solid #c34e00'});
        }

        $('#orderBy, #sort, #itemsPerPage').on("change", function(){
            $('#sort option').eq(0).prop("disabled", false);
            if ($(this).val() == 'ORDERING') {
                $('#sort').val("ASC").find('option').eq(0).prop("disabled", true);
            }

            $('#itemParam').submit();
        });

        // 상품삭제
        $('.delete_item').on('click', function(e) {
            e.preventDefault();
            $('#check_all').prop("checked", false);
            $(this).closest("table").find('input[name=id]:enabled').prop("checked", false);
            $(this).closest("tr").find('input[name=id]').prop("checked", true);

            Common.updateListData("/opmanager/item/list/delete", Message.get("M00196"));	// 삭제하시겠습니까?
        });

        // 목록데이터 - 삭제처리
        $('#delete_list_data').on('click', function() {
            Common.updateListData("/opmanager/item/list/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
        });

        // 목록데이터 - 수정처리
        $('#update_list_data').on('click', function() {

            var $form = $('#listForm');
            if ($form.find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }

            var errors = 0;
            $form.find('input[name=id]:checked').each(function() {
                var $salePrice = $(this).closest('tr').find('input[name=salePrice]');
                if ($.trim($salePrice.val()) == '') {
                    alert($.validator.messages['text'].format($salePrice.attr('title')));
                    $salePrice.focus();
                    errors++;
                    return;
                }
            });
            if (errors == 0) {
                Common.updateListData("/opmanager/item/list/update", Message.get("M00307"));	// 선택된 데이터를 수정하시겠습니까?
            }
        });

        // 목록데이터 - 품절처리
        $('#update_list_data_soldout').on('click', function() {
            Common.updateListData("/opmanager/item/list/update-sold-out", "선택한 상품을 품절로 처리하시겠습니까?");	// 선택된 데이터를 수정하시겠습니까?
        });

        // 목록데이터 - 품절해제(재판매)
        $('#update_list_data_soldout').on('click', function() {
            Common.updateListData("/opmanager/item/list/update-resale", "선택한 상품을 품절해제로 처리하시겠습니까?");	// 선택된 데이터를 수정하시겠습니까?
        });

        // 목록데이터 - 품절해제(재판매)
        $('#update_list_data_add_category').on('click', function() {
            var $form = $('#listForm');
            if ($form.find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }

            Common.popup('/opmanager/item/add-items-to-category', 'add-items-to-category', 970, 420);
        });

        // 목록데이터 - 순서변경 버튼 처리
        $('#change_ordering, #change_ordering2').on('click', function() {
            if (!isChangingOrdering()) {
                if (confirm(Message.get("M00298") + '\n' + Message.get("M00299"))) { // '현재 검색 조건으로는 순서 정렬이 불가능합니다. 순서 변경이 가능한 조건으로 다시 검색하시겠습니까?'
                    var orderBy = '${fn:escapeXml(itemParam.orderBy)}';
                    var totalItems = '${fn:escapeXml(pagination.totalItems)}';
                    var itemsPerPage = '${fn:escapeXml(itemParam.itemsPerPage)}';

                    // 순서 변경이 가능한 조건인지 체크.
                    var $query = $('#query');
                    var $soldOutFlag = $('#soldOutFlag1');
                    var $displayFlag = $('#displayFlag2');
                    var $rankingFlag = $('#rankingFlag1');
                    var $priceRange = $('#priceRange');
                    var $searchStartDate = $('#searchStartDate');
                    var $searchEndDate = $('#searchEndDate');
                    var $orderBy = $('#orderBy');
                    var $sort = $('#sort');
                    var $itemsPerPage = $('#itemsPerPage');

                    $query.val('');
                    $soldOutFlag.prop('checked', true);
                    $displayFlag.prop('checked', true);
                    $rankingFlag.prop('checked', true);
                    $priceRange.val('');
                    $searchStartDate.val('');
                    $searchEndDate.val('');
                    $orderBy.val('ORDERING');
                    $sort.val('ASC');
                    $itemsPerPage.val('1000');

                    $('#itemParam').submit();

                }
            } else {
                Common.changeListOrdering('/opmanager/item/list/change-ordering');
            }

        });

        // 팀/그룹 ~ 4차 카테고리 이벤트
        ShopEventHandler.categorySelectboxChagneEvent();
        Shop.activeCategoryClass('${fn:escapeXml(itemParam.categoryGroupId)}', '${fn:escapeXml(itemParam.categoryClass1)}', '${fn:escapeXml(itemParam.categoryClass2)}', '${fn:escapeXml(itemParam.categoryClass3)}', '${fn:escapeXml(itemParam.categoryClass4)}');

        //Common.DateButtonEvent.set('.day_btns > a.btn_date.btn_created_date', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
        //Common.DateButtonEvent.set('.day_btns > a.btn_date.btn_update_date', '', 'input[name="searchStartUpdateDate"]' , 'input[name="searchEndUpdateDate"]');
        Common.DateButtonEvent.set('.day_btns > a.btn_date', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();

		// 관련답례품 드레그
	    $(".sortable_item_relation").sortable({
	        placeholder: "sortable_item_relation_placeholder"
	    });

		// 오프라인 대표 답례품 itemId를 jQuery 추출
	 	let items = $('input[name="rprsProdOffItemIds"]');

	 	items.each(function(idx, item){
	 		let itemId = $(item).val();
	 		// itemId로 해당 답례품의 옵션 조회 AJAX
	 		$.post(url('/opmanager/item/getOptionList'), {'itemId' : itemId}, function(res) {
				if(res.length != 0) {
					// <select> 태그 생성 + <option> 태그 생성 + change 이벤트 추가 함수 호출
					appendItemOption('off_prod', res);
				}

				// 오프라인 답례품 테이블의 options 컬럼을 가지고 옴
				$.post(url('/opmanager/item/getSelectOption'), {'itemId' : itemId}, function(res){
		 		 	let option = res.split('||');
		 		 	// 선택형 상품 - S||실크 스카프 선택||도트패턴(롱)_하늘||30000
	 		 		if(option[0] == 'S'){
		 		 		$('#select_box1_' + itemId).val(option[2]).trigger('change');
	 		 		}
	 		 		// 조합형 상품 - S3||상품구분||A||색상||빨강||사이즈||M||-2000
	 		 		else {
	 					$('#select_box1_' + itemId).val(option[2]).trigger('change');
	 					$('#select_box2_' + itemId).val(option[4]).trigger('change');
	 					$('#select_box3_' + itemId).val(option[6]).trigger('change');
	 		 		}
		 		});
			});
	 	})

		const offgiveItemList = ${empty lclgvOffRprsGdsListJSON ? [] : lclgvOffRprsGdsListJSON};	// 오프라인 답례품 리스트
	 	if (offgiveItemList) {
	 		if (offgiveItemList.length > 0) {
		 		let alertComment = "오프라인 답례품 수정 필요 내역";
		 		let printAlert = false;
		 		for (let i = 0; i < offgiveItemList.length; i++) {
	 				if (offgiveItemList[i].dataStatusCode != "1") {
			 			alertComment += "\n제품명 : " + offgiveItemList[i].itemName;
	 					alertComment += "\n※ 제품이 등록되어있지 않습니다. 제품의 승인 상태 및 삭제 여부를 확인해주세요.";
		 				alertComment += "\n※ [비공개]상태인 오프라인 답례품을 저장할 경우, 해당 답례품이 오프라인 답례품 관리 목록에서 삭제처리 됩니다.";
		 				printAlert = true;
	 				} else {
		 				if (offgiveItemList[i].itemOptionFlag == "Y") {
		 		 			if (offgiveItemList[i].changedOption == "Y") {
				 				alertComment += "\n제품명 : " + offgiveItemList[i].itemName;
		 		 				alertComment += "\n※ 제품의 옵션값이 수정되었습니다. 옵션을 새로 선택해주세요.";
		 		 				printAlert = true;
		 		 			} else if (
	 		 					offgiveItemList[i].optionStockQuantity[0] < 10
	 		 					&& offgiveItemList[i].optionStockQuantity[0] != -1
		 					) {
		 		 				alertComment += "\n제품명 : " + offgiveItemList[i].itemName;
		 		 				alertComment += "\n※ 제품 수량이 10개 미만(현재 " + offgiveItemList[i].optionStockQuantity + " 개)입니다.";
		 		 				printAlert = true;
		 		 			}
	 		 			} else {
	 			 			if (
			 					offgiveItemList[i].stockQuantity < 10
			 					&& offgiveItemList[i].stockQuantity != -1
							) {
				 				alertComment += "\n제품명 : " + offgiveItemList[i].itemName;
		 		 				alertComment += "\n※ 제품 수량이 10개 미만(" + offgiveItemList[i].stockQuantity + " 개)입니다.";
		 		 				printAlert = true;
				 			}
			 			}
		 			}
	 			}
		 		if (printAlert) {
		 			alert(alertComment);
		 		}
	 		}
	 	}
    });

    function shoppingHow() {
        Common.popup("/opmanager/item/make-shopping-how", 'makeShoppingHow', 500, 300, 1);
    }

    // 순서변경 가능여부 체크
    function isChangingOrdering() {
        var orderBy = '${fn:escapeXml(itemParam.orderBy)}';
        var totalItems = '${fn:escapeXml(pagination.totalItems)}';
        var itemsPerPage = '${fn:escapeXml(itemParam.itemsPerPage)}';

        // 순서 변경이 가능한 조건인지 체크.
        var $query = $('#query');
        var $soldOutFlag = $('#soldOutFlag1');
        var $displayFlag = $('#displayFlag2');
        var $rankingFlag = $('#rankingFlag1');
        var $priceRange = $('#priceRange');
        var $searchStartDate = $('#searchStartDate');
        var $searchEndDate = $('#searchEndDate');
        var $orderBy = $('#orderBy');
        var $sort = $('#sort');
        var $itemsPerPage = $('#itemsPerPage');

        if ($soldOutFlag.prop('checked') == false
            || $displayFlag.prop('checked') == false
            || $rankingFlag.prop('checked') == false
            || $query.val() != ''
            || $priceRange.val() != ''
            || $searchStartDate.val() != ''
            || $searchEndDate.val() != ''
            || $orderBy.val() != 'ORDERING'
            || $sort.val() != 'ASC'
            || Number(totalItems) > Number(itemsPerPage)
        ) {

            return false;
        }
        return true;
    }

    function deferredResult() {
        $.post("/opmanager/item/deferred-result", {}, function(text) {
            alert(text);
        }, 'text');
    }


    // CSV 다운로드 - 사용안함.
    function downloadCsv(type) {
        var $form = $('#itemParam');
        $form.attr('action', '/opmanager/item/download-' + type + '-csv');
        $form.submit();
        $form.attr('action', '/opmanager/item/list');
    }

    // 엑셀 다운로드 팝업
    function downloadExcel() {

		if (${fn:escapeXml(pagination.totalItems)} == 0) {
			alert(Message.get("M00297"));	// 조회된 데이터가 없습니다.
			return;
		}

		//Common.popup("/opmanager/item/download-excel?" + $("#itemParam").serialize(), "download-excel", 600, 550, 0);
		Common.popup("/opmanager/item/download-excel" + location.search, "download-excel", 600, 550, 0);
    }

    // 엑셀 업로드
    function uploadExcel() {
        Common.popup('/opmanager/item/upload-excel', 'upload-excel', 600, 550, 0);
    }

    // 아피리츠.
    function openApilits() {
        Common.popup('/opmanager/item/apilits', 'apilits', 390, 280, 0);
    }

    function updateListDataDisplay(flag) {

        if ($('#listForm').find('input[name=id]:checked').size() == 0) {
            alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
            return;
        } else {
            Common.updateListData("/opmanager/item/list/update-display/" + flag, "정보를 "+"${op:message('M00365')}");
        }
    }

    function updateListDataLabel(flag) {
        var message = "정보를 "+"${op:message('M00365')}";
        if ($('#listForm').find('input[name=id]:checked').size() == 0) {
            alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
            return;
        } else {
            // 판매종료 처리시 설명글 추가 2017-05-08 yulsun.yoo
            if (flag == 90) {
                message = "판매종료 처리 된 상품은 상품상태 변경이 불가능합니다.\n선택된 데이터를 수정하시겠습니까?";
            }
            Common.updateListData("/opmanager/item/list/update-label/" + flag, message);
        }
    }

    function sellerSeller(sellerId) {
        $('#sellerId').val(sellerId);
    }

    function findMd(targetId) {
        Common.popup('/opmanager/seller/find-md?targetId=' + targetId, 'find_md', 720, 800, 1);
    }

    function clearMd(targetId) {
        var $target = $('#' + targetId);
        $target.val('');
        $target.closest('td').find('#mdName').val('');
    }

    //MD 검색 콜백
    function handleFindMdCallback(response) {
        var $target = $('#' + response.targetId);
        $target.val(response.userId);
        $target.closest('td').find('#mdName').val(response.userName);

    }

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
			$.post(url("/opmanager/item/options-by-locgovCode"), {'code' : value}, function(response) {

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

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		var strStartDate = $("#searchStartDate").val();
		var strEndDate = $("#searchEndDate").val();
		var strStartUpdateDate = $("#searchStartUpdateDate").val();
		var strEndUpdateDate = $("#searchEndUpdateDate").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("등록일 종료일이 시작일보다 빠릅니다.");
				$("#searchEndDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		if(strStartUpdateDate && strStartUpdateDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startUpdateDate = new Date(strStartUpdateDate.substr(0, 4), strStartUpdateDate.substr(4, 2), strStartUpdateDate.substr(6, 2));
			var endUpdateDate = new Date(strEndUpdateDate.substr(0, 4), strEndUpdateDate.substr(4, 2), strEndUpdateDate.substr(6, 2));

			if(startUpdateDate > endUpdateDate) {
				alert("수정일 종료일이 시작일보다 빠릅니다.");
				$("#searchEndUpdateDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startUpdateDate, endUpdateDate);
			if(!searchChk) return false;
		}

		$("#itemParam").submit();
	}


	<c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">

	/**
	 * 대표답례품 추가
	 */
	function locgovItem() {
		/*$('input[name=id]:checked').each(function() {
			var itemId = $(this).val();
			addRelationItem(itemId);
		});*/

		let selectedInfos = $('input[name=id]:checked');
		let length = selectedInfos.length;
		if (length == 0) {
			alert('선택된 답례품이 없습니다.');
			return;
		} else {
			let targetId = 'rprsProd';

			for(let i = 0 ; i < length ; i++) {			// 등록 여부 체크
				let itemId = selectedInfos[i].value;
				if (Shop.isAddedRelationItem(targetId, itemId)) {
					alert("이미 추가한 답례품이 존재합니다.");
					return;
				}
			}

			if(!Shop.isAvilableRelationItemCount(targetId, 4, length)){
				alert("대표 답례품은 최대 4개까지 등록 가능합니다.");
				return;
			}

			for(let i = 0 ; i < length ; i++) {
				let itemId = selectedInfos[i].value;
				addRelationItem(itemId);
			}

			window.scrollTo({top: $("#relationItem")[0].offsetTop, behavior: 'smooth'});
		}
	}

	function addRelationItem(itemId) {
		// 체크
		let targetId = 'rprsProd';
		/*if (Shop.isAddedRelationItem(targetId, itemId)) {
			let message = Message.get("M01215");	// 이미 추가한 상품입니다.
			Message.danger(message);
			return;
		}*/

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

		/*
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
		*/

		Shop.addRelationItem(targetId, item);
	}

	/**
	 * 대표답례품 화면 내용 저장
	 */
	function saveRelationItem() {
		let itemIdElements = $("#rprsProd").find('input[name=rprsProdItemIds]');
		let length = itemIdElements.length;
		let itemIds = [];
		for(let i = 0 ; i < length ; i++) {
			itemIds.push(itemIdElements[i].value);
		}

		$.post(url("/opmanager/item/saveLclgvRprsGds"), {'itemIds' : itemIds}, function(response) {
			try {
				if (response.isSuccess) {
					alert('저장되었습니다.');
	                //location.reload();
				} else if (response.errorMessage)  {
					alert(response.errorMessage);
				} else  {
					alert('문제가 발생했습니다.');
				}
			} catch (e) {
				alert('문제가 발생했습니다.');
			}
	    });
	}

	function deleteAllItem() {
		if(confirm('서버에 저장된 항목을 전체삭제하시겠습니까?')){
			Shop.deleteRelationItemAll('rprsProd');
			saveRelationItem();
		}
	}


	////////////////////////////////////////////////////// 오프라인 대표 답례품 Function
	/*
		오프라인 대표 답례품 추가 버큰 클릭 이벤트
	*/
	function locgovItemOff() {
		let selectedInfos = $('input[name=id]:checked');
		let length = selectedInfos.length;
		// Validation Check
		if(length == 0){
			alert('선택된 답례품이 없습니다.');
			return
		} else {
			let targetId = 'rprsProdOff';
			for(let i = 0; i < length ; i ++) {
				let itemId = selectedInfos[i].value;
				if(Shop.isAddedRelationItem(targetId, itemId)) {
					alert("이미 추가한 답례품이 존재합니다.");
					return;
				}
			}

			if(!Shop.isAvilableRelationItemCount(targetId, 2, length)) {
				alert("오프라인 대표 답례품은 최대 2개까지 등록 가능합니다.");
				return;
			}

			for(let i = 0; i < length; i++){
				let itemId = selectedInfos[i].value;
				// 오프라인 대표 답례품 추가
				addRelationItemOff(itemId);
			}

			window.scrollTo({top: $("#relationItemOff")[0].offsetTop, behavior: 'smooth'});
		}
	}

	//오프라인 답례품 관리 영역에 아이템 추가
	function addRelationItemOff(itemId) {

		// 오프라인 대표 답례품 DIV
		let targetId = 'rprsProdOff';

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
			'itemTextOptionFlag' : $item.find('.item_text_option_flag').text(),
			'stockFlag' : $item.find('.stock_flag').text(),
			'stockQuantity' : $item.find('.stock_quantity').text(),
			'itemSoldOutFlag' : $item.find('.item_sold_out_flag').text(),
			'soldOut' : $item.find('.sold_out').text(),
			'targerId' : targetId
		};

		// Validation Check - 최소 주문 수량
		const omq = parseInt(item['orderMinQuantity']);
		if(omq > 1) {
			alert('\'' + item['itemName'] + '\' 상품은 최소 주문 수량이 ' + omq + '개 입니다.\n오프라인 대표 답례품은 최소 주문 수량이 없는 상품만 등록이 가능합니다.');
			return;
		}

		/*
		  Validation Check - 답례품 상태 확인
		  답례품 관리는 현재 0, 1, 90번 코드가 조회 됨
		  하지만, 실제 판매가 되는 1번(정상) 데이터만 등록할 수 있도록 해야 함
		*/
		if(parseInt(item['dataStatusCode']) != 1) {
			alert('\'' + item['itemName'] + '\' 답례품은 대표 답례품으로 등록할 수 없는 상태입니다.\n다른 답례품을 선택바랍니다.');
			return;
		}
		/*
		  Validation Check - 답례품 상태 확인
		  답례품 중 옵션이 붙어있는 답례품은 오프라인대표답례품으로 등록 할 수 없다.
		  itemOptionFlag : item_option_flag
		  itemTextOptionFlag : item_text_option_flag
		*/

		/*
		if(item['itemOptionFlag'] !== 'N' || item['itemTextOptionFlag'] !== 'N') { <!-- 20260325 필수 추가정보 isb 수정 -->
			alert('\'' + item['itemName'] + '\' 옵션값이 있는경우 오프라인답례품으로 등록할 수 없습니다.\n다른 답례품을 선택바랍니다.');
			return;
		}
		*/


		// targetId = rprsProdOff
		Shop.addRelationItemOff(targetId, item);

		// 선택한 Item의 옵션 정보를 response에 저장
		let response;
		$.post(url('/opmanager/item/getOptionList'), {'itemId' : item.itemId}, function(res) {
			response = res;
		});

		// 아이템 옵션이 있을 경우 옵션 추가
		if(response.length != 0) {
			appendItemOption('off_prod', response);
		}
	}

	// 오프라인 답례품 아이템 Select, Option 태그 추가
	function appendItemOption(target, response) {

		const itemId = response[0].itemId;
		const optionType = response[0].itemOptionType;
		// 옵션 타입이 선택형(S)인 경우
 		if(optionType === 'S') {
			const selectBox = createSelectBox(itemId, 1);
			// 선택형(S) 옵션의 optionName1은 모두 동일
			const optionTitle = response[0].itemOptionGroups[0].itemOptions[0].optionName1
			// 해당 ID의 li에 Select 태그 추가
			$('#rprsProdOff_item_' + itemId + ' p.' + target).append(selectBox.tag);
			// 선택형(S)의 옵션 이름 추가
			$('#' + selectBox.id).append(createOption(true, optionTitle));
			// 선택형(S)은 단일 옵션으로 아이템 옵션 전체를 <option> 태그로 생성
			response[0].itemOptionGroups[0].itemOptions.forEach(function(option){
				$('#' + selectBox.id).append(createOption(false, option.optionName2, option.optionPrice));
			});

			// 선택형의 옵션(1개)을 선택하는 순간 change 이벤트 발생
			$('#' + selectBox.id).on('change', function(){
				addSelectEventListener(selectBox.id, null, 'S');
			});
		// 옵션 타입이 조합형(S3)인 경우
		}else if(optionType === 'S3'){
			//selectBox 생성
			const selectBox1 = createSelectBox(itemId, 1, optionType);
			const selectBox2 = createSelectBox(itemId, 2, optionType);
			const selectBox3 = createSelectBox(itemId, 3, optionType);

			//아이템 뒤에 append
			$('#rprsProdOff_item_' + itemId + ' p.' + target).append(selectBox1.tag);
			$('#rprsProdOff_item_' + itemId + ' p.' + target).append(selectBox2.tag);
			$('#rprsProdOff_item_' + itemId + ' p.' + target).append(selectBox3.tag);

			const itemOptionTitle1 = response[0].itemOptionTitle1;
			const itemOptionTitle2 = response[0].itemOptionTitle2;
			const itemOptionTitle3 = response[0].itemOptionTitle3;

			$('#' + selectBox1.id).append(createOption(true, itemOptionTitle1));
			$('#' + selectBox2.id).append(createOption(true, itemOptionTitle2));
			$('#' + selectBox3.id).append(createOption(true, itemOptionTitle3));

			let optionMap = {
				itemId : '' + itemId,
				optionType : 'S3'
			};

			// 조합형 옵션 생성 - 1번 <select> 태그만 옵션 생성
			createComboOption(optionMap, 'init');

			// 옵션 변경 시 이벤트 추가
			changeComboBox();
		}
	}

	// 오프라인 답례품 select 생성
	function createSelectBox(itemId, idx, optionType) {

		const boxId = 'select_box' + idx + '_' + itemId;
		let selectBox = {
				id : boxId,
				tag : $('<select></select>')
					.addClass('form-select')
					.addClass('select_box')
					.attr('id', boxId)
		};

		// 조합형의 경우 EventListener를 적용하기 위한 클래스 추가
		if(optionType === 'S3') selectBox.tag.addClass('combo_box');
		return selectBox;
	}

	// option 태그 생성
	function createOption(defaultFlag, value, price) {

		// 옵션의 대표 문구(색상, 사이즈 등)은 value 없이 생성
		if(defaultFlag === true) return $('<option></option>').val("").text(value);
		let text = value;
		// 가격이 양수이면 '상품명+N', 가격이 음수이면 '상품명-N'
		if(price > 0) text = text + '+' + price;
		else if(price < 0) text = text + ' ' +price;
		return $('<option></option>').val(value).text(text);
	}

	// 조합형 옵션 생성
	function createComboOption(optionMap, selectBoxId, selectBoxIdList) {

		$.ajax({
			url : '/opmanager/item/getComboOptionList',
			type : 'POST',
			data : JSON.stringify(optionMap),
			contentType : 'application/json',
			success: function(res){
				// type이 string이기 때문에 price는 number로 캐스팅
				res = res.map(option => ({
					...option,
					PRICE : parseInt(option.PRICE)
				}));
				// 가격 오름차순 정렬
				res = res.sort((a, b) => a.PRICE - b.PRICE);

				res.forEach(function(option){
					let id;
					// 최초 생성 시 select_box1_itemId에 append
					if(selectBoxId === 'init') id = 'select_box1_' + optionMap['itemId'];
					// selectBoxId가 select_box1이면, select_box2에 append
					// selectBoxId가 select_box2이면, select_box3에 append
					else id = selectBoxId == selectBoxIdList['box1'] ? selectBoxIdList['box2'] : selectBoxIdList['box3'];

					// select_box3의 경우 <option> 태그의 text에 option_price가 추가되어야 함
					if(selectBoxIdList !== undefined && id === selectBoxIdList['box3']) {
						$('#' + id).append(createOption(false, option['OPTION_NAME'], option['PRICE']));
						return;
					}
					$('#' + id).append(createOption(false, option['OPTION_NAME']));

				})
			}
		});
	}

	// 옵션 변경 시 이벤트 추가
	function changeComboBox() {
		// 오프라인 답례품 조합형 Event
		$('.combo_box').on('change', function(){
			// 선택된 옵션의 <select> 태그 id 값
			const selectBoxId = $(this).attr('id');
			// 선택된 옵션의 값
			const selectedValue = $(this).val();
			const itemId = selectBoxId.substring(selectBoxId.lastIndexOf('_') + 1);
			let optionMap = {
					itemId : '' + itemId,
					optionType : 'S3'
			};
			// <select> 태그 id
			const selectBoxIdList = {
				'box1' : 'select_box1_' + itemId,
				'box2' : 'select_box2_' + itemId,
				'box3' : 'select_box3_' + itemId
			}

			// select 태그 Event 추가
			addSelectEventListener(selectBoxId, selectBoxIdList, 'S3');

			// 선택된 옵션의 값이 존재할 경우
			if(selectedValue !== "") {
				// select_box1 or select_box2라면
				if(selectBoxId != selectBoxIdList['box3']) {
					// select_box1_itemId의 값(선택된 1번 옵션)을 optionMap에 추가
					const optionName1 = $('#' + selectBoxIdList['box1']).val();
					optionMap['optionName1'] = optionName1;
					if(selectBoxId == selectBoxIdList['box2']) {
						//select_box2_itemId의 값(선택된 2번 옵션)을 optionMap에 추가
						const optionName2 = $('#' + selectBoxIdList['box2']).val();
						optionMap['optionName2'] = optionName2;
					}
					// <option> 태그 생성
					createComboOption(optionMap, selectBoxId, selectBoxIdList);
				}
			}
		});
	}

	// select 태그의 값이 변경될 때마다 실행
	function addSelectEventListener(selectBoxId, selectBoxIdList, optionType) {

		let itemId = selectBoxId.substring(selectBoxId.lastIndexOf('_') + 1);
		// input hidden의 답례품 기본 가격 값 추출
		const defaultPrice = parseInt($('#default_price_' + itemId).val().replace(',', ''));
		// 금앨을 표시하는 span의 id
		const priceId = 'price_area_' + itemId;
		// 어떤 select의 value가 변경되든 기본 가격으로 초기화
		changeSpanPrice(priceId, defaultPrice);
		// 선택형이고 선택된 옵션이 있거나 조합형이고 3번째 옵션일 경우 → 기본값 + 옵션값으로 금액 변경
		if((optionType === 'S' && $('#' + selectBoxId).val() != '') || (optionType === 'S3' && selectBoxId === selectBoxIdList['box3'])) {
			// AJAX로 전송할 데이터 생성
			let optionMap = {
				itemId : '' + itemId
			}
			if(optionType === 'S') {
				optionMap['optionType'] = 'S';
				// 선택형의 경우 optionName2가 실제 데이터
				optionMap['optionName2'] = $('#' + selectBoxId).val();
			}else {
				optionMap['optionType'] = 'S3';
				Object.entries(selectBoxIdList).forEach(([key, id], idx) => {
					// key : box1, value : select_box1_itemId
					// key : box2, value : select_box2_itemId
					// key : box3, value : select_box3_itemId
					optionMap['optionName' + ++idx] = $('#' + id).val();
				});
			}
			// AJAX 결과(option_price) + default_price_itemId(기본값)을 <span id ="price_area_itemId">의 HTML에 셋팅
			$.ajax({
				url : '/opmanager/item/getComboOptionList',
				type : 'POST',
				data : JSON.stringify(optionMap),
				contentType : 'application/json',
				success : function(res) {
					if (res === null || res.length === 0) {
						return;
					} else {
						const optionPrice = parseInt(res[0]['PRICE']);
						// input hidden의 옵션 가격에 값 셋팅
						$('#option_price_' + itemId).val(optionPrice);
						const totalPrice = defaultPrice + optionPrice;
						// 금액 변경 함수 호출
						changeSpanPrice(priceId, totalPrice);
					}
				}
			})

			return;
		}

		// 조합형의 경우 select 태그 선택 시 옵션들 삭제
		if(optionType === 'S3') {
			// 3번째 옵션 삭제
			$('#' + selectBoxIdList['box3'] + ' option').filter(function() {
				// 옵션의 value가 비어있으면 삭제하지 않음
				return $(this).val() !== '';
			}).remove();

			// 1번째 <select> 태그 선택 시 2번 옵션도 삭제
			if(selectBoxId == selectBoxIdList['box1']) {
				$('#' + selectBoxIdList['box2'] + ' option').filter(function() {
					// 옵션의 value가 비어있으면 삭제하지 않음
					return $(this).val() !== '';
				}).remove();
			}
		}
	}

	// 오프라인 답례품 가격 변경
	function changeSpanPrice(priceId, price) {
		$('#' + priceId).html('[' + Common.numberFormat(price) + 'P 답례품]');
	}

	/*
		오프라인 대표 답례품 - 현재 상태 저장
	*/
	function saveRelationItemOff() {
		// name=rprsProdOffItemIds인 input의 value값(itemId)을 리스트로 담음
		let itemIds = $("#rprsProdOff").find('input[name=rprsProdOffItemIds]').map(function(){
			return $(this).val();
		}).get();

		// 옵션이 모두 선택 되었는 지 체크
		let optionValidationCheck = optionValidation(itemIds);
		if(!optionValidationCheck) return;

		// 옵션 가격이 각각 30,000P, 15,000P인지 체크
		let priceValidationCheck = priceValidation(itemIds);
		if(!priceValidationCheck) return;

		// 서버에 보낼 데이터 리스트
		let optionList = [];

		itemIds.forEach((itemId) => {
			// 아이디가 rprsProdOff_item_itemId인 태그의 클래스가 select_box인 Element 추출
			let selectBox = $('#rprsProdOff_item_' + itemId + ' .select_box');
			let map = {};
			map['itemId'] = itemId;
			// options에 들어갈 데이터 생성
			// select_box라는 클래스의 Element가 없으면 옵션이 없는 답례품
			if(selectBox.length == 0){
				map['option'] = '';
			}
			// select_box라는 클래스의 Element가 1개면 선택형(S)
			else if(selectBox.length == 1) {
				// S||분류(?)||옵션명||옵션가격
				let selectBoxId = '#select_box1_' + itemId
				map['option'] = 'S||' + selectBox[0][0].innerHTML + '||' + $(selectBoxId).val()
									+ '||' + $('#option_price_' + itemId).val();
			}
			// select_box라는 클래스의 Element가 3개면 조합형(S3)
			else {
				// S3||분류1||옵션명1||분류2||옵션명2||분류3||옵션명3||옵션가격
				let option = 'S3';
				selectBox.each(function(i, box){
					option += '||' + $(box)[0][0].innerHTML + '||' + $(box).val();
				});
				option += '||' + $('#option_price_' + itemId).val();
				map['option'] = option;
			}

			optionList.push(map);
		});

		// 오프라인 대표 답례품 현재 상태 저장 AJAX
		$.ajax({
			url : '/opmanager/item/saveLclgvOffRprsGds',
			type : 'POST',
			data : JSON.stringify(optionList),
			contentType : 'application/json',
			success : function(res) {
				try {
					if (res.isSuccess) {
						alert('저장되었습니다.');
					} else if (res.errorMessage)  {
						alert(res.errorMessage);
					} else  {
						alert('문제가 발생했습니다.');
					}
				} catch (e) {
					alert('문제가 발생했습니다.');
				}
			}
		});
	}

	/*
		오프라인 대표 답례품 등록 시 Option Validation
	*/
	function optionValidation(itemIds) {

		let itemName;
		let flag = true;
		all : for(let i = 0; i < itemIds.length; i++) {
			let itemId = itemIds[i];
			itemName = $('#itemName_' + itemId).html();
			let selectBox = $('#rprsProdOff_item_' + itemId + ' .select_box');
			// 선택형의 경우
			if(selectBox.length == 1) {
				let value = selectBox[0].value;
				if(value === undefined || value === '') {
 					flag = false;
					break;
				}
			}
			// 조합형의 경우
			else if(selectBox.length == 3) {
				for(let j = 0; j < selectBox.length; j++) {
					let value = selectBox[j].value;
					if(value === undefined || value === '') {
						flag = false;
						// label이 all인 반복문을 break
						break all;
					}
				}
			}
		}

		// flag가 false일 경우 옵션이 모두 선택되지 않음
		if(!flag) alert('\'' + itemName + '\' 답례품의 옵션이 선택되지 않았습니다.\n옵션 확인바랍니다.');
		return flag;
	}

	/*
		오프라인 대표 답례품 등록 시 Price Validation
	*/
	function priceValidation(itemIds) {

		let checkFlag = true;
		let cnt15000 = 0;
		let cnt30000 = 0;

		for(var i = 0; i < itemIds.length; i++) {

			// 답례품 기본 가격 저장
			let defaultPrice = $('#default_price_' + itemIds[i]).val();
			// 답례품 옵션 가격 저장
			let optionPrice = $('#option_price_' + itemIds[i]).val();
			// 답례품 옵션이 비어있을 경우(옵션이 없는 상품) 0원으로 셋팅
			if(optionPrice === undefined || optionPrice === '') optionPrice = 0;
			let totalPrice = parseInt(defaultPrice) + parseInt(optionPrice);

			// 총 가격이 15,000원이면
			if(totalPrice == 15000) cnt15000++;
			// 총 가격이 30,000원이면
			else if(totalPrice == 30000) cnt30000++;
			// 그 외 가격이라면 잘못 등록된 상품
			else {
				checkFlag = false;
				break;
			}
		}

		// itemId가 1개라면
		if(checkFlag && itemIds.length == 1) {
			// 15,000원 상품 or 30,000원 상품이 아니라면
			if(!(cnt15000 || cnt30000)) {
				checkFlag = false;
			}
		}
		// itemId가 2개라면
		else if(checkFlag && itemIds.length == 2) {
			// 15000원과 30000원 둘 다 1이 아니라면
			if(!(cnt15000 && cnt30000)) {
				checkFlag = false;
			}
		}

		// checkFlag가 false라면, 30,000원 or 15,000원 상품이 아니거나, 각각 1개씩을 등록하지 않음
		if(!checkFlag) {
			alert('오프라인 답례품은 30,000P와 15,000P인 상품 1건씩만 등록이 가능합니다.');
		}

		return checkFlag;

	}


	/*
		오프라인 대표 답례품 - 전체 삭제 → 현재 상태 저장을 씀
	*/
	function deleteAllItemOff() {
		if(confirm('서버에 저장된 항목을 전체삭제하시겠습니까?')){
			// front에 있는 Element 삭제
			Shop.deleteRelationItemAll('rprsProdOff');
			// 현재 상태 저장 함수 호출
			saveRelationItemOff();
		}
	}


	/*
		오프라인 대표 답례품 등록 Validation Check - 최소 주문 수량
		추후 다른 Validation Check가 있을 수 있기 때문에, Map으로 받음
	*/

	</c:if>
</script>


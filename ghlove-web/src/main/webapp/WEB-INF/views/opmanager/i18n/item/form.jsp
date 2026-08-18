<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style>
span.require {color: #e84700; margin-left: 5px;}
</style>

		<div class="location">
			<a href="#">상품관리</a> &gt;  <a href="#">상품정보</a> &gt; <a href="#" class="on">상품등록</a>
		</div>

		<form:form modelAttribute="item" id="${requestContext.sellerPage ? 'item': 'impossible-form'}" method="post" enctype="multipart/form-data" onsubmit="${requestContext.sellerPage ? '': 'return false'}">
			<input type="hidden" name="listPage" value="${fn:escapeXml(listPage)}"/>
			<input type="hidden" id="useItemUserCode" value="${fn:escapeXml(useItemUserCode)}" />
			<form:hidden path="otherFlag" value="1" /> <%-- 자사상품/업체배송 : 추후 삭제 예정  --%>
			<form:hidden path="opentime" />
			<form:hidden path="itemType" /> <%-- 상품구분: 1.일반상품, 2.사업자상품, 3.세트상품 --%>
			<form:hidden path="itemCode" />
			<form:hidden path="itemDataType" />	<%-- 상품데이터 형태: 1.일반상품, 2.추가구성상품 --%>
			<form:hidden path="brandId" />	<%-- 브랜드 ID: ???? --%>
			<form:hidden path="priceCriteria" />	<%-- 가격 입력 기준 (1: 공급가기준-수수료자동입력, 2: 수수료기준-공급가 자동입력) --%>
			<form:hidden path="dataStatusMessage" />
			<form:hidden path="naverPayFlag" value="N" />

			<c:set var="isSet" value="${item.itemType == '3'}" />

			<div class="item_list">

				<h3 class="custom"><span>${item.itemId == 0 ? op:message('M00773') : op:message('M00907')}</span></h3>	<!-- 상품등록 --><!-- 상품기본정보 -->

				<%--<p class="tip">*카테고리 2개 이상 추가시 드래그로 카테고리 순서를 변경할 수 있습니다.</p>-->
				<%-- 등록된 상품 카테고리 세팅 --%>
				<ul id="item_categories" class="sortable_item_category category_box ui-sortable">
					<c:forEach items="${item.breadcrumbs}" var="breadcrumb" varStatus="i">
						<li id="item_category_${fn:escapeXml(breadcrumb.categoryClass)}">

							${fn:escapeXml(breadcrumb.teamName)} > ${fn:escapeXml(breadcrumb.groupName)}

							<c:forEach items="${breadcrumb.breadcrumbCategories}" var="subBreadcrumb">
								 > ${fn:escapeXml(subBreadcrumb.categoryName)}
							</c:forEach>
							<a href="javascript:deleteItemCategory(${fn:escapeXml(breadcrumb.categoryClass)})" class="delete">[${op:message('M00074')}]</a>
							<input type="hidden" name="categoryIds" value="${fn:escapeXml(breadcrumb.categoryClass)}" />
						</li>
					</c:forEach>
					<%-- 등록된 상품 카테고리가 없을 경우 --%>
					<c:if test="${empty item.breadcrumbs}">
						<li class="nothing">${op:message("M00077")}</li>	<!-- 등록된 상품 카테고리가 없습니다. -->
					</c:if>
				</ul>


				<div class="category_wrap mb20 flex_box gap-12 item-center">
					<select id="categoryGroupId" class="category multiple" size="12">
						<option value="0">= 1${op:message('M00075')} =</option> <!-- 팀/그룹 -->
						<c:forEach items="${categoryTeamGroupList}" var="categoriesTeam">
							<c:if test="${categoriesTeam.categoryTeamFlag == 'Y'}">
								<optgroup label="${fn:escapeXml(categoriesTeam.name)}">
								<c:forEach items="${categoriesTeam.categoriesGroupList}" var="categoriesGroup">
									<c:if test="${categoriesGroup.categoryGroupFlag == 'Y'}">
										<option value="${fn:escapeXml(categoriesGroup.categoryGroupId)}">${fn:escapeXml(categoriesGroup.groupName)}</option>
									</c:if>
								</c:forEach>
								</optgroup>
							</c:if>
						</c:forEach>

					</select> <!-- // category_step -->

					<select id="categoryClass1" class="category multiple"  size="12">
						<option value="">= 2${op:message('M00075')} =</option> <!-- {}차 카테고리 -->
					</select> <!-- // category_step -->

					<select id="categoryClass2" class="category multiple"  size="12">
						<option value="">= 3${op:message('M00075')} =</option> <!-- {}차 카테고리 -->
					</select> <!-- // category_step -->

					<select id="categoryClass3" class="category multiple"  size="12" style="display:none">
						<option value="">= 4${op:message('M00075')} =</option> <!-- {}차 카테고리 -->
					</select> <!-- // category_step -->

					<select id="categoryClass4" class="category multiple"  size="12" style="display:none">
						<option value="">= 5${op:message('M00075')} =</option> <!-- {}차 카테고리 -->
					</select> <!-- // category_step -->

					<div>
						<button type="button" class="btn btn-success btn-lg" onclick="addItemCategory()" <c:if test="${ !requestContext.sellerPage }">disabled="disabled"</c:if>><span class="glyphicon glyphicon-plus"></span> ${op:message('M00789')}</button> <!-- 카테고리추가 -->
					</div>
				</div> <!-- // category_wrap -->
			</div>

			<div class="item_info_wrap">
				<div class="item_list mt70">
					<h3>기본정보</h3>
					<div class="board_write">
						<table class="board_write_table" summary="상품기본정보">
							<caption>상품기본정보</caption>
							<colgroup>
								<col style="width: 220px;" />
								<col />
								<col style="width: 220px;" />
								<col />
							</colgroup>
							<tbody>
								<c:if test="${item.itemId != 0}">
									<tr>
										<td class="label">${op:message('M00019')}</td> <!-- 상품번호 -->
										<td colspan="3">
											<div>
												<c:choose>
													<c:when test='${op:property("saleson.view.type") eq "api"}'>
														<a href="${op:property('saleson.url.frontend')}/items/details.html?code=${fn:escapeXml(item.itemUserCode)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
													</c:when>
													<c:otherwise>
														<a href="/products/preview/${item.itemUserCode}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
														<a href="/m/products/preview/${item.itemUserCode}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_mobile.gif" alt="" /></a>
													</c:otherwise>
												</c:choose>

												<span style="font-weight:bold; font-size: 14px; color: #000;">
													${fn:escapeXml(item.itemCode)}
												</span>
											</div>
										</td>
										<td class="label hidden">재입고 알림</td>
										<td class="hidden">
											<div>
												<span class="restock-button hide">${fn:escapeXml(restockNoticeCount)}명에게 <button type="button" onclick="sendRestockNotice();" class="btn btn-gradient btn-sm"> 메시지 전송</button></span>
												<span class="restock-text hide">신청한 회원이 없습니다.</span>
											</div>
										</td>
									</tr>
								</c:if>

								<%--
								<tr>
									<td class="label">${op:message('M00960')}</td> <!-- 상품구분 -->
									<td>
										<div>
											<p>
												<form:radiobutton path="itemType" value="1" label="일반상품" />
												<form:radiobutton path="itemType" value="2" label="사업자상품" />
											</p>
										</div>
									</td>
								</tr>
								--%>

                               <tr>
                                   <td class="label">답례품 분류</td> <!-- 답례품 분류 -->
                                   <td>
                                       <div class="checkbox">
                                            <form:checkbox path="mobileItemYn" value="Y" label="모바일 발송전용" /> <!-- 모바일 발송전용 -->
                                        </div>
                                   </td>
                                   <td class="label">구매 제한</td>
                                   <td>
                                       <div class="checkbox">
                                            <form:checkbox path="adultItemYn" value="Y" label="성인" disabled="${seller.adultItemYn == 'Y' ? 'false' : 'true'}"/> <!-- 성인 -->
                                        </div>
                                   </td>
                               </tr>

								<tr>
									<td class="label"><span class="required_mark">*</span>답례품명</td> <!-- 답례품명 -->
									<td>
										<div class="flex_box gap-12 item-center">
											<form:input path="itemName" maxlength="50" class="input_txt required _filter wd-500" title="${op:message('M00018')}" onkeyup="return text_maxlength(this)"/>
											<span id="itemlength">[0/50]</span>
										</div>
									</td>

                                    <td class="label">
                                    	원산지
                                    </td>
                                    <td>
                                    	<div class="red" style="padding-bottom: 0px;">
                                    	* 농축산물 원산지 표시 방법: 국산일 경우 국내산 또는 시ㆍ군ㆍ구 등으로 표시<br>
                                    	* 가공품 원산지 표시 방법: 원료 함량이 높은 순위에 따라 국가명 표시<br>
                                    	* 처벌조항: 거짓표시(7년 이하의 징역, 1억원 미만 벌금), 미표시(1천만원 이하 과태료)<br>
                                    	* 원산지 표시 방법 문의: 국립농산물품질관리원 1588-8112<br>
                                    	</div>
                                        <div>
                                            <form:input path="originCountry" maxlength="30" class="input_txt _filter wd-500" title="원산지" />
                                        </div>
                                    </td>
                                    <td class="label hidden">${op:message('M01627')} <%-- 무게 --%></td>
                                    <td class="hidden">
                                        <div>
                                            <form:input path="weight" maxlength="5" class="amount _number_comma" /> g
                                        </div>
                                    </td>



									<td class="label hidden">브랜드</td>
									<td class="hidden">
										<div>
											<form:select path="brand" class="wd-500">
												<form:option value="" data-id="0" label="선택" />
												<c:forEach items="${brandList}" var="brand">
													<c:if test="${brand.displayFlag == 'Y'}">
														<option value="${fn:escapeXml(brand.brandName)}" data-id="${fn:escapeXml(brand.brandId)}" ${op:selected(item.brandId, brand.brandId)}>${fn:escapeXml(brand.brandName)}</option>
													</c:if>
												</c:forEach>
											</form:select>
										</div>
									</td>
								</tr>

								<%-- 1.일반 --%>
								<c:if test="${op:property('saleson.mall.type') == '1'}">
									<tr>
										<td class="label">${op:message('M00019')}</td> <!-- 상품번호 -->
										<td>
											<div>
												<c:choose>
													<c:when test="${mode == 'edit'}">
														<form:input path="itemUserCode" maxlength="20" class="required" title="${op:message('M00019')}" /> <a href="#" id="btn_check_duplicate" class="table_btn">${op:message('M00148')}</a> <!-- 중복검사 -->
													</c:when>
													<c:otherwise>
														<form:input path="itemUserCode" maxlength="20" class="required" title="${op:message('M00019')}" /> <a href="#" id="btn_check_duplicate" class="table_btn">${op:message('M00148')}</a> <!-- 중복검사 -->
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${mode == 'copy'}">
														<form:input path="itemUserCode" maxlength="20" class="required" title="${op:message('M00019')}" /> <a href="#" id="btn_check_duplicate" class="table_btn">${op:message('M00148')}</a> <!-- 중복검사 -->
													</c:when>
													<c:otherwise>
														<form:input path="itemUserCode" maxlength="20" class="required" title="${op:message('M00019')}" /> <a href="#" id="btn_check_duplicate" class="table_btn">${op:message('M00148')}</a> <!-- 중복검사 -->
													</c:otherwise>
												</c:choose>
												<p class="text-info text-sm"> ※ 주의 : 아래 문자는 사용하시면 안됩니다. <br/>1. [ / (슬러시)] 상품 상세페이지가 정상적으로 연결되지 않습니다 <br/>2. [ , (콤마) ][ _ (언더바) ][ : (콜론) ][ . (${op:message('M00246')}) ] 오픈 마켓에 상품구분으로 사용하고 있습니다</p>
											</div>
										</td>
									</tr>
								</c:if>

								<%-- 2.몰인몰 --%>
								<c:if test="${op:property('saleson.mall.type') == '2'}">
									<input type="hidden" name="itemUserCode" value="${fn:escapeXml(itemCode)}" />
								</c:if>

								<tr class="hidden">
									<td class="label">제조사</td>
									<td>
										<div>
											<form:input path="manufacturer" maxlength="30" class="form-block" title="제조사" />
										</div>
									</td>
									<td class="label">공급사</td>
									<td>
										<div>
											<%--
											<form:hidden path="sellerId" />
											 --%>

											<c:if test="${!isSellerPage}">
												<%-- 본사는 본사 상품만 등록하는 경우
												<input type="hidden" name="sellerId" id="sellerId" data-is-hq-seller="Y" value="${shopContext.hqSellerId}"/>
												<c:forEach items="${sellerList}" var="seller" varStatus="i">
													<c:if test="${seller.sellerId == shopContext.hqSellerId}">
														${seller.sellerName}
													</c:if>
												</c:forEach>
												--%>

												<%-- 본사가 판매자를 지정하여 상품을 등록할 수 있는 경우 --%>
												<select id="sellerId" name="sellerId" class="required" title="공급사">
													<option value="">선택</option>

													<c:forEach items="${sellerList}" var="seller" varStatus="i">
														<c:set var="isHqSeller" value="N" />
														<c:if test="${seller.sellerId == shopContext.hqSellerId}">
															<c:set var="isHqSeller" value="Y" />
														</c:if>

														<c:choose>
															<c:when test="${item.sellerId == seller.sellerId}">
																<option value="${fn:escapeXml(seller.sellerId)}" data-is-hq-seller="${fn:escapeXml(isHqSeller)}" selected="selected">${fn:escapeXml(seller.sellerName)}</option>
															</c:when>
															<c:otherwise>
																<c:if test="${!isSellerPage}">
																	<option value="${fn:escapeXml(seller.sellerId)}" data-is-hq-seller="${fn:escapeXml(isHqSeller)}">${fn:escapeXml(seller.sellerName)}</option>
																</c:if>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>

											</c:if>

											<c:if test="${isSellerPage}">
												<input type="hidden" name="sellerId" id="sellerId" data-is-hq-seller="N" value="${fn:escapeXml(sellerContext.seller.sellerId)}"/>
												${fn:escapeXml(sellerContext.seller.sellerName)} (${fn:escapeXml(sellerContext.seller.loginId)})
											</c:if>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>과세구분</td> <!-- 과세구분 -->
									<td colspan="3">
										<div class="flex_box gap-12">
                                            <div class="input-form">
                                                <form:radiobutton path="taxType" value="1" label="과세" /> <!-- 과세 -->
                                            </div>
                                            <div class="input-form">
                                                <form:radiobutton path="taxType" value="2" label="면세" /> <!-- 면세 -->
                                            </div>
                                        </div>
									</td>
									<%-- <td class="label">고유코드</td>
									<td>
										<div class="flex_box gap-12 item-center">
											<form:input path="itemSellerCode" maxlength="30" class="input_txt required _filter wd-500" title="고유코드" onkeyup="return text_maxlength(this)"/>
										</div>
									</td> --%>
								</tr>
								<tr class="hidden">
									<td class="label">${op:message('M01624')} <%-- 사은품 정보 --%></td>
									<td colspan="3">
										<div>
											<form:radiobutton path="freeGiftFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
											<form:radiobutton path="freeGiftFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->

											<div class="hide_content">
												<form:input path="freeGiftName" placeholder="사은품 정보를 입력해 주세요." maxlength="80" class="required-free-gift form-block mb10" title="사은품 정보" />

												<div>
													<p class="mb10">
														<button type="button" class="table_btn" onclick="findGiftItem()"><span>${op:message('M00582')} <!-- 상품 추가 --> </span></button>
														<button type="button" class="table_btn" onclick="Shop.deleteRelationItemAll('freeGift')"><span>${op:message('M00411')} <!-- 전체삭제 --> </span></button>
													</p>

													<ul id="freeGift" class="sortable_item_relation">
														<li style="display: none;"></li>
														<c:forEach items="${item.freeGiftItemList}" var="giftItem" varStatus="i">
															<c:if test="${not empty giftItem}">
																<li id="freeGift_item_${fn:escapeXml(fn:escapeXml(giftItem.id))}">
																	<input type="hidden" name="freeGiftItemIds" value="${fn:escapeXml(giftItem.id)}" />
																	<p class="image"><img src="${shop:loadImageBySrc(giftItem.imageSrc, 'XS')}" class="item_image size-100 none" alt="상품이미지" /></p>
																	<p class="title">
																		[${fn:escapeXml(giftItem.code)}] ${fn:escapeXml(giftItem.name)}
																	</p>

																	<c:choose>
																		<c:when test="${not empty giftItem.notProcessLabel}">
																			<span class="error">[${fn:escapeXml(giftItem.notProcessLabel)}]</span>
																		</c:when>
																		<c:otherwise>
																			<span class="ordering">${fn:escapeXml(i.count)}</span>
																		</c:otherwise>
																	</c:choose>
																	<a href="javascript:Shop.deleteRelationItem('freeGift_item_${fn:escapeXml(giftItem.id)}');" class="delete_item_image"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
																</li>
															</c:if>
														</c:forEach>
													</ul>
												</div>

											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">답례품설명</td><!-- 답례품설명 -->
									<td colspan="3">
										<div>
											<form:textarea path="itemSummary" class="full" style="width:95%" title="${op:message('M01441')}" maxlength="100" onkeyup="return text_maxlength2(this)"/>
											 <span id="itemSummarylength">[0/100]</span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">검색어</td>
									<td colspan="3">
										<div>
											<div class="tip mb5">
												* 설정한 검색어로 검색시 상품이 검색결과에 노출됩니다. (구분값은 쉼표입니다.)
											</div>
											<form:textarea path="itemKeyword" maxlength="100" class="full" title="${op:message('M00022')}" />
										</div>
									</td>
								</tr>
								<%--
								<!-- 추천상품 -->
								<tr>
									<td class="label">${op:message('M00581')}</td> <!-- 추천상품 -->
									<td>
										<div>
											<p>
												<form:radiobutton path="recommendFlag" value="Y" label="${op:message('M00958')}" /> <!-- 포함  -->
												<form:radiobutton path="recommendFlag" value="N" label="${op:message('M00959')}" /> <!-- 포함하지 않음  -->
											</p>
										</div>
									</td>
								</tr>
								--%>
								<%--
								<tr>
									<td class="label">${op:message('M00246')}</td> <!-- 포인트 -->
									<td>
										<div>
											<p class="mb5">
												<button type="button" class="table_btn add_item_point" onclick="addItemPoint()"><span>${op:message('M00501')}</span></button> <!-- 기간별 포인트 추가 -->
											</p>

												<!-- 포인트 추가시 -->
												<table id="item_point_config" class="board_list_table" summary="${op:message('M00246')} 추가">
													<caption>${op:message('M00246')} 추가 리스트</caption>
													<colgroup>
														<col style="width: 160px;">
														<col style="">
														<!--
														<col style="width: 100px;">
														-->
														<col style="width: 70px;">
													</colgroup>
													<thead>
														<tr>
															<th>${op:message('M00246')}</th> <!-- 포인트 -->
															<th>${op:message('M00505')}</th> <!-- 포인트 적용 기간 -->
															<!-- <th>${op:message('M00506')}</th> 특정일 적용 -->
															<th>${op:message('M00074')}</th> <!-- 삭제 -->
														</tr>
													</thead>
													<tbody id="item_point_area">

														<c:forEach items="${pointConfigList}" var="pointConfig">
															<tr>
																<td>
																	<input type="text" name="point" maxlength="4" value="${pointConfig.point}"  class="three required _number" title="${op:message('M00246')}" /> <!-- 포인트 -->
																	<input type="hidden" name="pointType" value="${pointConfig.pointType}" />

																	<input type="radio" name="R${pointConfig.pointConfigId}" id="R${pointConfig.pointConfigId}1" ${op:checked('1', pointConfig.pointType)}> <label for="R${pointConfig.pointConfigId}1">%</label>
																	<input type="radio" name="R${pointConfig.pointConfigId}" id="R${pointConfig.pointConfigId}2" ${op:checked('2', pointConfig.pointType)}> <label for="R${pointConfig.pointConfigId}2">P</label>
																</td>
																<td>
																	<span class="datepicker"><input type="text" name="pointStartDate" class="term  _date" maxlength="8" value="${pointConfig.startDate}" title="${op:message('M00507')}"></span> <!-- 시작일 -->
																	<select name="pointStartTime" title="시간 선택">
																		<c:forEach items="${hours}" var="code">
																			<option value="${code.value}" ${op:selected(code.value, pointConfig.startTime)}>${code.label}</option>
																		</c:forEach>
																	</select>시 ~
																	<span class="datepicker"><input type="text" name="pointEndDate" class="term required _date" maxlength="8" value="${pointConfig.endDate}" title="${op:message('M00509')}"></span> <!-- 종료일 -->
																	<select name="pointEndTime" title="시간 선택">
																		<c:forEach items="${hours}" var="code">
																			<option value="${code.value}" ${op:selected(code.value, pointConfig.endTime)}>${code.label}</option>
																		</c:forEach>
																	</select>시 59분


																	<input type="hidden" name="pointRepeatDay" value="" />
																</td>
																<!--
																<td>
																	<select name="pointRepeatDay">
																		<option value="">${op:message('M00039')}</option> <!-- 전체 ->
																		<c:forEach begin="1" end="31" step="1" var="i">
																			<option value="${ i }"
																				<c:if test="${pointConfig.repeatDay eq i}">selected="selected"</c:if>>${ i }</option>
																		</c:forEach>
																	</select> ${op:message('M00511')} <!-- 일 ->
																</td>
																 -->
																<td><a href="#" class="fix_btn delete_item_point">${op:message('M00074')}</a></td> <!-- 삭제 -->
															</tr>

														</c:forEach>
													</tbody>
												</table>

										</div>
									</td>
								</tr>
								 --%>
							</tbody>
						</table>
					</div>
				</div>

				<c:if test="${isSet}">
					<div class="item_list mt30">
						<h3>
							<span>세트상품 구성</span>
						</h3>
						<div class="board_write">
							<table class="board_write_table">
								<caption>세트상품 구성</caption>
								<colgroup>
									<col style="width: 160px;" />
									<col style="width: 400px;" />
									<col style="width: 160px;" />
									<col style="" />
								</colgroup>
								<tbody>
								<tr>
									<td class="label">세트상품</td>
									<td colspan="3">
										<div>
											<p class="text-info text-sm">* 텍스트 옵션 상품은 세트상품으로 등록이 불가능합니다.</p>
											<button type="button" id="button_add_set_item" class="btn btn-gradient btn-sm" onclick="findItemPopup('set','${fn:escapeXml(item.itemUserCode)}')">+ 세트상품 추가</button>
											<button type="button" class="btn btn-gradient btn-sm" onclick="deleteItemSet('all', '')">x ${op:message('M00411')}</button> <!-- 전체삭제 -->
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="4">
										<table class="inner-table th-center" style="border-top:2px solid #666;">
											<caption>세트상품 리스트</caption>
											<colgroup>
												<col style="width: 50px;" />
												<col style="width: 70px;" />
												<col style="" />
												<col style="width: 60px;" />
												<col style="width: 160px;" />
												<col style="width: 70px;" />
												<col style="width: 70px;" />
												<col style="width: 120px;" />
												<col style="width: 60px;" />
											</colgroup>
											<thead>
											<tr>
												<th>${op:message('M00200')}</th> <!-- 순번 -->
												<th>${op:message('M00752')}</th> <!-- 이미지 -->
												<th>상품명</th>
												<th>옵션</th>
												<th>${op:message('M00786')}</th> <!-- 판매가격 -->
												<th>${op:message('M00787')}</th> <!-- 판매상태 -->
												<th>${op:message('M00191')}</th> <!-- 공개유무 -->
												<th>구매수량</th>
												<th>관리</th>
											</tr>
											</thead>
											<tbody id="set" class="sortable_item_set">
											<c:forEach items="${item.itemSets}" var="itemSet" varStatus="i">
												<c:set var="displayFlagText">${op:message('M00096')}</c:set> <!-- 공개 -->
												<c:if test="${itemSet.item.displayFlag == 'N'}">
													<c:set var="displayFlagText"><span style="color:#e84700">${op:message('M00097')}</span></c:set> <!-- 비공개 -->
												</c:if>
												<c:choose>
													<c:when test="${itemSet.item.dataStatusCode == '20' || itemSet.item.dataStatusCode == '31' || itemSet.item.dataStatusCode == '40' || itemSet.item.dataStatusCode == '41'}">
														<c:set var="itemSaleStatusText">등록대기</c:set>
													</c:when>
													<c:when test="${itemSet.item.dataStatusCode == '21'}">
														<c:set var="itemSaleStatusText"><a href="#" class="show-reject-message">등록보류</a></c:set>
													</c:when>
													<c:when test="${itemSet.item.dataStatusCode == '30'}">
														<c:set var="itemSaleStatusText">재등록신청</c:set>
													</c:when>
													<c:when test="${itemSet.item.dataStatusCode == '90'}">
														<c:set var="itemSaleStatusText">판매종료</c:set>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${itemSet.item.itemSoldOutFlag == 'Y'}">
																<c:set var="itemSaleStatusText"><span style="color:#e84700">품절</span></c:set>
															</c:when>
															<c:otherwise><c:set var="itemSaleStatusText">${op:message('M00694')}</c:set> <!-- 판매중 --></c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>

												<tr id="set_item_${fn:escapeXml(itemSet.itemId)}" class="set_items"
													data-name="${fn:escapeXml(itemSet.item.itemName)}" data-price="${fn:escapeXml(itemSet.item.salePrice)}"
													data-min-quantity="${fn:escapeXml(itemSet.item.orderMinQuantity)}" data-max-quantity="${fn:escapeXml(itemSet.item.orderMaxQuantity)}">
													<td class="text-center">
														<input type="hidden" name="setItemIds" value="${fn:escapeXml(itemSet.itemId)}" />
														<span class="ordering">${fn:escapeXml(i.count)}</span>
													</td>
													<td>
														<div>
															<img src="${shop:loadImage(itemSet.item.itemUserCode, itemSet.item.itemImage, 'XS')}" class="item_image" alt="상품이미지" />
														</div>
													</td>
													<td class="left break-word">
														[${fn:escapeXml(itemSet.item.itemUserCode)}]<br/>${fn:escapeXml(itemSet.item.itemName)}
													</td>
													<td class="text-center">${fn:escapeXml(itemSet.item.itemOptionFlag)}</td>
													<td class="text-right">
														${op:numberFormat(itemSet.item.salePrice)}원
													</td>
													<td class="text-center">
														${fn:escapeXml(itemSaleStatusText)}
													</td>
													<td class="text-center">
														${fn:escapeXml(displayFlagText)}
													</td>
													<td class="text-center">
														<div>
															<input type="text" name="setQuantities" class="form-half _number" maxlength="3" value="${fn:escapeXml(itemSet.quantity)}" /> 개
														</div>
													</td>
													<td class="text-center">
														<a href="javascript:deleteItemSet('select', 'set_item_${fn:escapeXml(itemSet.itemId)}');" class="delete_item_image btn btn-gradient btn-xs">삭제</a>
													</td>
												</tr>
											</c:forEach>
											<c:if test="${empty item.itemSets}">
												<tr>
													<td colspan="9" class="no_content">상품 내역이 없습니다.</td>
												</tr>
											</c:if>
											</tbody>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="4">
										<table class="inner-table th-center" style="border-top:2px solid #666">
											<caption>세트상품 판매 설정</caption>
											<colgroup>
												<col style="width: 33%;" />
												<col style="width: 33%;" />
												<col style="width: 33%;" />
											</colgroup>
											<thead>
											<tr>
												<th>세트상품 판매가</th>
												<th>상품 합계금액</th>
												<th>세트 할인</th>
											</tr>
											</thead>
											<tbody>
											<tr>
												<td class="text-right bg-color">
													<span id="set-total-amount">0</span>원
												</td>
												<td class="text-right">
													<span id="set-sale-amount">0</span>원
												</td>
												<td class="text-right">
													<form:input path="setDiscountAmount" maxlength="6" class="required-set-discount-amount _min_10 _number_comma amount" title="세트 할인 금액" /> ${op:space()}
													<form:select path="setDiscountType">
														<form:option value="1">원</form:option>
														<form:option value="2">%</form:option>
													</form:select>
												</td>
											</tr>
											</tbody>
										</table>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
					</div>
				</c:if>

				<div class="item_list mt70">
					<h3>
						<span>기본설정</span>
					</h3>
					<div class="board_write">
						<table class="board_write_table">
							<caption>상품기본정보</caption>
							<colgroup>
								<col style="width: 220px;" />
								<col />
								<col style="width: 220px;" />
								<col />
							</colgroup>
							<tbody>
								<tr ${isSet ? 'style="display:none"' : ''}>
									<td class="label"><span class="required_mark">*</span>판매가</td> <!-- 판매가 -->
									<td colspan="3">
										<div class="flex_box gap-08 item-center">
											<p class="text-info text-sm hidden">
												* 실제 판매할 가격을 입력해 주십시오.<br/>
												* 입력한 판매가격에서 할인(즉시할인, 스팟할인)이 적용되며 배송비 조건 <br/>
												&nbsp;&nbsp;&nbsp;기준이 됩니다. <br/>
												* 입점몰로 운영되는 경우 입점판매자 정산금액의 기준가격이 됩니다.
											</p>
											<input type="text" name="salePrice" id="salePrice" maxlength="8" value="${op:negativeNumberToEmpty(item.salePrice)}" class="amount required _min_10 _number_comma wd-200" title="${op:message('M00786')}" /> <span>p</span> <%-- <span>(${shopContext.config.taxDisplayTypeText})</span> (세금포함)--%>
											<input type="hidden" id="sellerCommissionRate" value="${fn:escapeXml(seller.commissionRate)}" />
										</div>
									</td>
									<td class="label hidden">${op:message('M00785')}</td> <!-- 정가 -->
									<td class="hidden">
										<div>
											<p class="text-info text-sm">
												* 정가는 필수 입력항목이 아니며 단순 표기용으로&nbsp;사용됩니다.
											</p>
											<form:input path="itemPrice" maxlength="8" class="amount optional _number_comma" title="${op:message('M00785')}" /> 원 <%-- <span>(${shopContext.config.taxDisplayTypeText})</span> (세금포함)--%>
										</div>
									</td>
								</tr>
								<tr ${isSet ? 'style="display:none"' : 'style="display:none"'}>
									<td class="label">공급가 설정</td>
									<td>
										<div>
											<c:choose>
												<c:when test="${isSellerPage}">
													<form:hidden path="commissionType" />
													<c:if test="${item.commissionType == '1'}">
														${op:message('M01610')} (${op:numberFormat(seller.commissionRate)}%)	<%-- 입점업체 수수료로 설정 --%>
													</c:if>
													<c:if test="${item.commissionType == '2'}">
														${op:message('M01611')} (${op:numberFormat(item.commissionRate)}%) <%-- 상품별 수수료로 설정 --%>
													</c:if>
													<c:if test="${item.commissionType == '3'}">
														공급가 설정 (${op:numberFormat(item.supplyPrice)}원)
													</c:if>
												</c:when>
												<c:otherwise>
													<form:radiobutton path="commissionType" value="1" label="${op:message('M01610')}" /> <%-- 입점업체 수수료로 설정 --%>
													<form:radiobutton path="commissionType" value="2" label="${op:message('M01611')}" /> <%-- 상품별 수수료로 설정 --%>
													<form:radiobutton path="commissionType" value="3" label="공급가 설정" />
												</c:otherwise>
											</c:choose>
										</div>
									</td>
									<td class="label" style="display:none;">공급가<span class="require">*</span></td>
									<td style="display:none;">
										<div>
											<form:input path="supplyPrice" maxlength="8" title="공급가" class="required-supply-price amount optional _number_comma" readonly="true" /> 원
										</div>
									</td>
									<td class="label" style="display:none;">수수료율<span class="require">*</span></td>
									<td style="display:none;">
										<div>
											<form:input path="commissionRate" maxlength="4" title="수수료율" class="required-commission-rate _number_float form-sm" readonly="true" /> %
										</div>
									</td>
									<%-- <td class="label">원가</td>
									<td>
										<div>
											<p class="text-info text-sm">
												※ 주의 : 옵션을 구성하실 경우 옵션별로 원가를 입력하시기 바랍니다. <br />
												단, 상품 원가에 금액을 입력하시고 옵션별 원가를 입력하지 않으셨을 때에는 상품별 원가를 사용합니다.
											</p>
											<input type="text" name="costPrice" id="costPrice" maxlength="8" value="${op:negativeNumberToEmpty(item.costPrice)}" class="amount required _number_comma" title="원가" /> 원 <span>(${shopContext.config.taxDisplayTypeText})</span> (세금포함)
										</div>
									</td> --%>
								</tr>

								<tr style="display:none">
									<td class="label">비회원 판매가격 설정</td>
									<td>
										<div>
											<form:radiobutton path="salePriceNonmemberFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
											<form:radiobutton path="salePriceNonmemberFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->
											<div class="hide_content">
												<input type="text" name="salePriceNonmember" id="salePriceNonmember" maxlength="8" value="${op:negativeNumberToEmpty(item.salePriceNonmember)}" class="amount required-nonmember-price _min_10 _number_comma" title="비회원 가격" /> 원 <%-- <span>※ VAT별도</span>--%>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label" ${isSet ? 'style="display:none"' : ''}>재고연동</td>
									<td ${isSet ? 'style="display:none"' : ''}>
										<div class="flex_box gap-12">
											<p class="text-info text-sm hidden">* 옵션이 등록된 경우 옵션재고 수량으로 판단됩니다.</p>
											<div class="input-form">
												<form:radiobutton path="stockFlag" value="N" label="연동안함 (무제한)" />
											</div>
											<div class="input-form">
												<form:radiobutton path="stockFlag" value="Y" label="연동함" />
											</div>
										</div>
									</td>
									<td class="label">품절유무 <%-- 품절유무 --%></td>
									<td ${isSet ? 'colspan="3"' : ''}>
										<div class="checkbox">
											<p class="text-info text-sm hidden">* 체크 시 상품/옵션 재고여부와 상관없이 무조건 품절로 표시됩니다.</p>

											<form:checkbox path="soldOut" value="1" label="${op:message('M00693')}" /> <%-- 품절 --%>
											<input type="hidden" name="!soldOut" value="0" />
										</div>
									</td>
								</tr>

								<tr id="trStockQuantity" ${(item.stockFlag == 'Y' && !isSet) ? '' : 'style="display: none"'}>
									<td class="label">재고</td> <!-- 재고 -->
									<td colspan="3">
										<div class="flex_box gap-08 item-center">
											<input type="text" name="stockQuantity" id="stockQuantity" maxlength="5" value="${op:negativeNumberToEmpty(item.stockQuantity)}" class="amount required-stock-quantity _min_0 _number_comma wd-200" title="${op:message('M00930')}" /> 개
										</div>
									</td>
									<td class="label hidden">${op:message('M01626')} <%-- 관리코드 --%></td>
									<td class="hidden">
										<div>
											<form:input path="stockCode" maxlength="20" />
										</div>
									</td>
								</tr>

								<%--
								<tr>
									<td class="label">${op:message('M00910')} (${op:message('M00787')})</td> <!-- 상품라벨 --> <!-- 판매상태 -->
									<td>
										<div>
											<p class="mb10">
												${op:message('M00931')} <!-- 상품 재고가 0이 되면 자동으로 설정된 상품라벨이 표기됩니다. -->
											</p>
											<span>
												<form:radiobutton path="soldOut" value="1" label="${op:message('M00693')}" /> <!-- 입하대기 -->
											</span>
											<span>
												<form:radiobutton path="soldOut" value="2" label="${op:message('M00692')}" /> <!-- 판매종료 -->
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00933')}</td> <!-- 재입고 예정일 -->
									<td>
										<div>
											<p>
												${op:message('M00934')} <!-- 상품 재고가 0이되면 자동으로 설정된 값이 출력됩니다. -->
											</p>

											<p class="mt10">
												<form:radiobutton path="stockScheduleType" value="" label="${op:message('M00801')}" /><br /> <!-- 없음 -->
											</p>
											<p class="mt5">
												<span style="display:inline-block; width: 100px">
													<form:radiobutton path="stockScheduleType" value="date" label="${op:message('M00935')}" /> <!-- 입하 예정일 -->
												</span>
												<form:input path="stockScheduleDate" maxlength="10" title="${op:message('M00935')}"/><br />
											</p>
											<p class="mt5">
												<span style="display:inline-block; width: 100px">
													<form:radiobutton path="stockScheduleType" value="text" label="${op:message('M00936')}" /> <!-- 기타 텍스트 -->
												</span>
												<form:input path="stockScheduleText" maxlength="30" title="${op:message('M00936')}"/>
											</p>
										</div>
									</td>
								</tr>
								--%>
								<c:if test="${!isSet}">
									<tr>
										<td class="label">${op:message('M00953')}</td> <!-- 최소주문수량 -->
										<td>
											<div class="flex_box gap-08 item-center">
												<input type="text" name="orderMinQuantity" id="orderMinQuantity" maxlength="5" value="${op:negativeNumberToEmpty(item.orderMinQuantity)}" class="amount optional _min_1 _number_comma wd-200" title="${op:message('M00953')}" />
												<div class="tip">*입력값이 없는 경우 제한없음 <!-- 입력 값이 없는 경우는 제한이 없습니다. --></div>
										</div></td>
										<td class="label">${op:message('M00954')}</td> <!-- 최대주문수량 -->
										<td>
											<div class="flex_box gap-08 item-center">
												<input type="text" name="orderMaxQuantity" id="orderMaxQuantity" maxlength="5" value="${op:negativeNumberToEmpty(item.orderMaxQuantity)}" class="amount optional _min_1 _number_comma wd-200" title="${op:message('M00954')}" />
												<div class="tip">*입력값이 없는 경우 제한없음 <!-- 입력 값이 없는 경우는 제한이 없습니다. --></div>
											</div>
										</td>
									</tr>
								</c:if>
								<tr class="hidden">
									<td class="label">쿠폰 사용 가능 여부</td>
									<td colspan="3">
										<div>
											<form:radiobutton path="couponUseFlag" label="사용 가능" value="Y" />
											<form:radiobutton path="couponUseFlag" label="사용 불가능" value="N" />
										</div>
									</td>
								</tr>
								<tr class="hidden">
									<td class="label">네이버 쇼핑 사용 여부</td>
									<td>
										<div>
											<p class="text-info text-sm">
												* 네이버 쇼핑에 상품정보 제공 여부 (EP 파일에 포함)
											</p>
											<form:radiobutton path="naverShoppingFlag" label="활성" value="Y" />
											<form:radiobutton path="naverShoppingFlag" label="비활성" value="N" />
										</div>
									</td>
									<td class="label">네이버 쇼핑 상품명</td>
									<td>
										<div>
											<p class="text-info text-sm">
												* 네이버 쇼핑에 상품정보 제공시 상품명을 변경하는 경우
											</p>
											<form:input path="naverShoppingItemName" maxlength="100" class="form-block" disabled="${item.naverShoppingFlag == 'Y' ? 'false' : 'true'}" />
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">답례품유형</td> <!-- 답례품유형 -->
									<td colspan="3">
										<div class="checkbox">
											<!-- <span> -->
												<form:checkbox path="itemNewFlag" value="Y" label="신규 답례품" checked="checked" disabled="true" />
												<input type="hidden" name="!itemNewFlag" value="N" />
											<!-- </span> -->
											<span class="hidden">
												<form:checkbox path="itemType1" value="1" label="인기상품" />
												<input type="hidden" name="!itemType1" value="0" />
											</span>
											<span class="hidden">
												<form:checkbox path="itemType2" value="1" label="BEST상품 " />
												<input type="hidden" name="!itemType2" value="0" />
											</span>
											<span class="hidden">
												<form:checkbox path="itemType4" value="1" label="추천상품" />
												<input type="hidden" name="!itemType4" value="0" />
											</span>
											<span class="hidden">
												<form:checkbox path="itemType5" value="1" label="기본상품" />
												<input type="hidden" name="!itemType5" value="0" />
											</span>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00191')}</td> <!-- 공개유무 -->
									<td>
										<div class="flex_box gap-12">
											<div class="input-form">
												<form:radiobutton path="displayFlag" value="Y" label="${op:message('M00096')}" /> <!-- 공개 -->
											</div>
											<div class="input-form">
												<form:radiobutton path="displayFlag" value="N" label="${op:message('M00097')}" /> <!-- 비공개 -->
											</div>
										</div>
									</td>
									<td class="label">마감일자</td> <!-- 마감일자 -->
									<td>
										 <div>
					                      <span class="datepicker mr10">
					                            <form:input path="itemCloseDt" cssClass="datepicker optional " title="마감일자" />
					                        </span>
				                        </div>
									</td>
								</tr>
                                <tr>
                                    <td class="label">제철 월 선택<br>(최대 3개)</td>
                                    <td colspan="3" style="padding-left: 15px;">
                                        <c:forEach var="i" begin="1" end="12" step="1">
                                            <input type="checkbox" id="seasonFoodMonthList_${i}" name="seasonFoodMonthList" value="${i}" onclick="return seasonFoodMonthListOnclick(this)"
	                                            <c:forEach var="seasonFoodMonthList" items="${seasonFoodMonthList}">
										            <c:if test="${i eq seasonFoodMonthList}">
										                checked
										            </c:if>
										        </c:forEach>
                                            ><label for="seasonFoodMonthList_${i}">${i}월</label>
                                        </c:forEach>
                                    </td>
                                </tr>
							</tbody>
						</table>
						<div class="tip" style="margin-top:15px; margin-left:45px">
                            * 제철 식품관은 농축산물, 수산물 카테고리에 등록되는 답례품만 노출이 가능합니다.
                        </div>
					</div>
				</div>

				<div class="item_list mt70 hidden">
					<h3>
						<span>할인/${op:message('M00246')} 설정</span> <!-- 상품 이미지 등록 -->
					</h3>

					<div class="board_write">

						<table class="board_write_table">
							<colgroup>
								<col style="width: 160px;">
								<col style="">
							</colgroup>
							<tbody>
								<tr>
									<td class="label">즉시할인</td>
									<td>
										<div>
											<form:radiobutton path="sellerDiscountFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
											<form:radiobutton path="sellerDiscountFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->

											<div class="hide_content">
												판매가에서
												<form:input path="sellerDiscountAmount" maxlength="6" class="required-seller-discount-amount _min_10 _number_comma amount" title="즉시할인 금액" /> ${op:space()}

												<form:select path="sellerDiscountType">
													<form:option value="1">원</form:option>
													<%-- <form:option value="2">%</form:option> --%>
												</form:select>
												할인
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">스팟할인</td>
									<td>
										<form:hidden path="spotStartDate" />
										<form:hidden path="spotEndDate" />
										<form:hidden path="spotStartTime" />
										<form:hidden path="spotEndTime" />
										<c:set var="impossible" value="false" />

										<c:set var="disabled" value="false" />
										<c:if test="${item.spotFlag == 'Y' && ((item.spotType == '1' && requestContext.sellerPage) || (item.spotType == '2' && !requestContext.sellerPage))}">
											<c:set var="impossible" value="true" />
										</c:if>

										<form:hidden path="spotType" />
										<div id="spot-area">
											<form:radiobutton path="spotFlag" value="N" label="${op:message('M00089')}" class="spotFlag" disabled="${fn:escapeXml(impossible)}" />  <!-- 사용안함 -->
											<form:radiobutton path="spotFlag" value="Y" label="${op:message('M00083')}" class="spotFlag" disabled="${fn:escapeXml(impossible)}" />  <!-- 사용 -->

											<div class="hide_content">
												<c:if test="${item.spotFlag == 'Y'}">
													<p class="text-info">
														(${item.spotType == '1' ? '운영자' : '판매자'}에 의해 스팟할인이 진행 중 입니다.)
													</p>
												</c:if>
												<table class="inner-table">
													<colgroup>
														<col style="width: 150px;" />
														<col style="width: auto;" />
													</colgroup>
													<tbody>
														<tr>
															<td class="label">
																스팟 기간 구분
															</td>
															<td>
																<c:choose>
																	<c:when test="${impossible}">
																		${item.spotDateType == '1' ? '시점' : '기간'}
																	</c:when>
																	<c:otherwise>
																		<form:radiobutton path="spotDateType" value="1" label="시점" checked="true" />
																		<form:radiobutton path="spotDateType" value="2" label="기간" />
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
														<tr class="spotDateTypeOne">
															<td class="label">기간</td>
															<td>
																<c:choose>
																	<c:when test="${impossible}">
																		<span>${op:date(item.spotStartDate)}</span>
																		<span class="wave">~</span>
																		<span>${op:date(item.spotEndDate)}</span>
																	</c:when>
																	<c:otherwise>
																		<span class="datepicker"><input id="spotStartDateOne" type="text" value="${fn:escapeXml(item.spotStartDate)}" maxlength="8" class="_date datepicker" title="세일시작일" /></span>
																		<span class="wave">~</span>
																		<span class="datepicker"><input id="spotEndDateOne" type="text" value="${fn:escapeXml(item.spotEndDate)}" maxlength="8" class="_date datepicker" title="세일종료일" /></span>
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
                                                        <tr class="spotDateTypeOne">
															<td class="label">시간대</td>
															<td>
																<c:choose>
																	<c:when test="${impossible}">
																		<span>${fn:escapeXml(spotStartHour)}:${fn:escapeXml(spotStartMinute)}</span>
																		<span class="wave">~</span>
																		<span>${fn:escapeXml(spotEndHour)}:${fn:escapeXml(spotEndMinute)}</span>
																	</c:when>
																	<c:otherwise>
																		<select id="spotStartHourOne">
																			<c:forEach begin="0" end="23" step="1" var="hour" varStatus="i">
																				<option value="${hour < 10 ? '0' : ''}${hour}" ${spotStartHour eq hour ? 'selected' : ''}>${hour < 10 ? '0' : ''}${hour}</option>
																			</c:forEach>
																		</select> :
																		<select id="spotStartMinuteOne">
																			<c:forEach begin="0" end="59" step="1" var="minute" varStatus="i">
																				<option value="${minute < 10 ? '0' : ''}${minute}" ${spotStartMinute eq minute ? 'selected' : ''}>${minute < 10 ? '0' : ''}${minute}</option>
																			</c:forEach>
																		</select>
																		<span class="wave">~</span>
																		<select id="spotEndHourOne">
																			<c:forEach begin="0" end="23" step="1" var="hour" varStatus="i">
																				<option value="${hour < 10 ? '0' : ''}${hour}" ${spotEndHour eq hour ? 'selected' : ''}>${hour < 10 ? '0' : ''}${hour}</option>
																			</c:forEach>
																		</select> :
																		<select id="spotEndMinuteOne">
																			<c:forEach begin="0" end="59" step="1" var="minute" varStatus="i">
																				<option value="${minute < 10 ? '0' : ''}${minute}" ${spotEndMinute eq minute ? 'selected' : ''}>${minute < 10 ? '0' : ''}${minute}</option>
																			</c:forEach>
																		</select>
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
														<tr class="spotDateTypeTwo">
                                                            <td class="label">기간</td>
                                                            <td>
                                                                <c:choose>
																	<c:when test="${impossible}">
																		<p>
																			<span>${op:date(item.spotStartDate)}</span>
																			<span class="wave">~</span>
																			<span>${op:date(item.spotEndDate)}</span>
																		</p>
																		<p>
																			<span>${fn:escapeXml(spotStartHour)}:${fn:escapeXml(spotStartMinute)}</span>
																			<span class="wave">~</span>
																			<span>${fn:escapeXml(spotEndHour)}:${fn:escapeXml(spotEndMinute)}</span>
																		</p>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="datepicker"><input id="spotStartDateTwo" type="text" value="${fn:escapeXml(item.spotStartDate)}" maxlength="8" class="_date datepicker" title="세일시작일"/></span>
                                                                        <select id="spotStartHourTwo">
                                                                            <c:forEach begin="0" end="23" step="1" var="hour" varStatus="i">
                                                                                <option value="${hour < 10 ? '0' : ''}${hour}" ${spotStartHour eq hour ? 'selected' : ''}>${hour < 10 ? '0' : ''}${hour}</option>
                                                                            </c:forEach>
                                                                        </select> :
                                                                        <select id="spotStartMinuteTwo">
                                                                            <c:forEach begin="0" end="59" step="1" var="minute" varStatus="i">
                                                                                <option value="${minute < 10 ? '0' : ''}${minute}" ${spotStartMinute eq minute ? 'selected' : ''}>${minute < 10 ? '0' : ''}${minute}</option>
                                                                            </c:forEach>
                                                                        </select>
                                                                        <span class="wave">~</span>
                                                                        <span class="datepicker"><input id="spotEndDateTwo" type="text" value="${fn:escapeXml(item.spotEndDate)}" maxlength="8" class="_date datepicker" title="세일종료일" /></span>
                                                                        <select id="spotEndHourTwo">
                                                                            <c:forEach begin="0" end="23" step="1" var="hour"  varStatus="i">
                                                                                <option value="${hour < 10 ? '0' : ''}${hour}" ${spotEndHour eq hour ? 'selected' : ''}>${hour < 10 ? '0' : ''}${hour}</option>
                                                                            </c:forEach>
                                                                        </select> :
                                                                        <select id="spotEndMinuteTwo">
                                                                            <c:forEach begin="0" end="59" step="1" var="minute" varStatus="i">
                                                                                <option value="${minute < 10 ? '0' : ''}${minute}" ${spotEndMinute eq minute ? 'selected' : ''}>${minute < 10 ? '0' : ''}${minute}</option>
                                                                            </c:forEach>
                                                                        </select>

                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
														</tr>
														<tr>
															<td class="label">요일</td>
															<td>
																<c:choose>
																	<c:when test="${impossible}">
																		<c:forEach items="${item.spotWeekDayList}" var="code" varStatus="i">
																			${fn:escapeXml(code.label)}<c:if test="${!i.last}">,</c:if>
																		</c:forEach>
																	</c:when>
																	<c:otherwise>
																		<form:hidden path="spotWeekDay" />
																		<c:forEach items="${item.spotWeekDayList}" var="code">
																			<label style="margin-right: 10px;"><input type="checkbox" name="day_of_week" value="${fn:escapeXml(code.value)}" ${code.detail == '1' ? 'checked="checked"' : '' } /> ${fn:escapeXml(code.label)}</label>
																		</c:forEach>
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
														<tr>
															<td class="label">할인금액</td>
															<td>
																<c:choose>
																	<c:when test="${impossible}">
																		${fn:escapeXml(item.spotDiscountAmount)}원
																	</c:when>
																	<c:otherwise>
																		<form:input path="spotDiscountAmount" title="할인금액" class="_min_10 _number_comma amount" /> 원
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00246')} 지급</td>
									<td>
										<div>
											<p class="text-info text-sm">
												* 사용 설정 시 상품을 구매하는 경우 판매자 부담 ${op:message('M00246')}가 지급됩니다.
											</p>
											<form:radiobutton path="sellerPointFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
											<form:radiobutton path="sellerPointFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->

											<div class="hide_content">
												<c:forEach items="${pointConfigList}" var="pointConfig" varStatus="i">
													<c:if test="${i.index == 0}">

														<c:set var="pointConfig" value="${pointConfig}" scope="request" />
														<jsp:include page="include-item-point.jsp" />

													</c:if>

												</c:forEach>

												<c:if test="${empty pointConfigList}">
													<jsp:include page="include-item-point.jsp" />
												</c:if>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="item_list mt70 hidden"  ${isSellerPage ? 'style="display:none"' : ''}>
					<h3>
						<span>관리항목</span>
					</h3>
					<div class="board_write">
						<table class="board_write_table">
							<caption>상품기본정보</caption>
							<colgroup>
								<col style="width: 160px;" />
								<col style="" />
								<col style="width: 160px;" />
								<col style="" />
							</colgroup>
							<tbody>
								<tr>


									<td class="label">전용상품</td>
									<td>
										<div>
											<p>
												<form:select path="privateType">
													<form:options items="${op:nullToEmptyArr(privateTypes)}" itemLabel="label" itemValue="value" />
												</form:select>
											</p>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00910')}</td> <!-- 상품라벨 -->
									<td>
										<div>
											<span><form:radiobutton path="itemLabel" value="0" label="${op:message('M00801')}" /></span> <!-- 없음 -->
											<span><form:radiobutton path="itemLabel" value="2" label="NEW" checked="checked" /></span>
											<span><form:radiobutton path="itemLabel" value="3" label="SALE" /></span>

										</div>
									</td>
									<td class="label">담당MD</td> <!-- 담당MD -->
									<td>
										<div>
											<input type="hidden" name="currentMdId" value="${fn:escapeXml(seller.mdId)}" />
											<input type="hidden" id="mdId" name="mdId" value="${fn:escapeXml(seller.mdId)}" />
											<form:input path="mdName" title="담당MD" readonly="true" />

											<button type="button" onclick="findMd('mdId')" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> MD검색</button>
											<button type="button" onclick="clearMd('mdId')" class="btn btn-gradient btn-sm"><span class="glyphicon glyphicon-remove"></span> 초기화</button>
										</div>
									</td>
								</tr>


								<tr>
									<td class="label">${op:message('M00909')}</td>
									<td colspan="3">
										<div>
											<p>
												<form:radiobutton path="nonmemberOrderType" value="1" label="${op:message('M00920')}" /> <!-- 비회원 구매가능 -->
												<form:radiobutton path="nonmemberOrderType" value="2" label="${op:message('M00921')}" /> <!-- 회원가격 비표시 -->
												<form:radiobutton path="nonmemberOrderType" value="3" label="${op:message('M00922')}" /> <!-- 상세페이지 접속 불가 -->
											</p>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>


				<div class="item_list mt70">
					<h3>
						<span>${op:message('M01003')}</span> <!-- 상품 이미지 등록 -->
					</h3>

					<div class="board_write">

						<table class="board_write_table item_image_info">
							<colgroup>
								<col style="width: 220px;">
								<col />
							</colgroup>
							<tbody>
								<tr>
									<td class="label"><p><span class="required_mark">*</span>${op:message('M00659')}</p><p>(600px * 600px)</p></td> <!-- 상세이미지 -->
									<td>
										<div class="flex_box item-center">

											<button type="button" id="add_detail_image_file" style="display:none" class="table_btn"><span>+ ${op:message('M00984')}</span></button> <!-- 이미지추가 -->
											<p class="text-info text-sm hidden">
												* 상품 이미지는 제한 없이 등록이 가능합니다.<br />
												* [파일선택] 버튼을 선택한 후 파일 선택 창에서 상품 이미지를 복수로 선택하거나 드레그 하여 선택 후 등록해 주십시오.<br /><br />
											</p>

												<input type="file" name="detailImageFiles[]" multiple="multiple" accept="image/png, image/jpeg, image/gif" class="full input_file" />


											<div id="multiple_files">

											</div>

											<ul id="item_details_images" class="sortable_item_image clear">
												<c:forEach items="${item.itemImages}" var="itemImage" varStatus="i">
													<c:if test="${itemImage.itemImageId != 0}">
														<li id="item_image_id_${fn:escapeXml(itemImage.itemImageId)}">
															<img src="${shop:loadImage(itemImage.itemUserCode, itemImage.imageName, 'XS')}" class="item_image size-100" alt="" />
															<span class="ordering">${fn:escapeXml(i.count)}</span>
															<a href="javascript:deleteItemImage('details', ${fn:escapeXml(itemImage.itemImageId)});" class="delete_item_image"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>

															<c:choose>
																<c:when test="${mode == 'copy'}"><input type="hidden" name="copyItemImageIds" value="${fn:escapeXml(itemImage.itemImageId)}" /></c:when>
																<c:otherwise><input type="hidden" name="itemImageIds" value="${itemImage.itemImageId}" /></c:otherwise>
															</c:choose>
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
				</div>

				<c:if test="${!isSet}">
					<div class="item_list mt70">

						<h3><span>옵션등록</span></h3> <!-- 옵션등록 -->

						<div class="board_write item_option_board">

							<table class="board_write_table" summary="옵션등록">
								<caption>옵션등록</caption>
								<colgroup>
									<col style="width: 220px;" />
									<col />
								</colgroup>
								<tbody>
									<tr>
										<td class="label">옵션 사용여부</td> <!-- 옵션 사용여부 -->
										<td>
											<div class="flex_box gap-12">
												<div class="input-form">
													<form:radiobutton path="itemOptionFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
												</div>
												<div class="input-form">
													<form:radiobutton path="itemOptionFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->
													<input type="hidden" name="itemOptionFlagValue" value="${fn:escapeXml(item.itemOptionFlag)}" />
												</div>
											</div>
										</td>
									</tr>
									<tr class="option-step step1">
										<td class="label">${op:message('M01629')} <%-- 상품옵션 형태 --%></td>
										<td>
											<div class="flex_box gap-12">
												<div class="input-form">
													<form:radiobutton path="itemOptionType" value="S" label="선택형" />
												</div>
												<div class="input-form hidden">
													<form:radiobutton path="itemOptionType" value="S2" label="2조합형" />
												</div>
												<div class="input-form">
													<form:radiobutton path="itemOptionType" value="S3" label="3조합형" />
												</div>
												<div class="input-form hidden">
													<form:radiobutton path="itemOptionType" value="T" label="텍스트형" />
													<input type="hidden" name="itemOptionTypeValue" value="${fn:escapeXml(item.itemOptionType)}" />
												</div>
											</div>
										</td>
									</tr>

								</tbody>
							</table>

							<div class="option-wrapper option-step step2">
								<div class="option-type type-S">
									<h4>■ 선택형</h4>
									<p class="text-info text-sm">* 선택해야하는 옵션이 한 종류인 경우</p>

									<%-- <button type="button" class="btn btn-gradient btn-sm add-option-input">+ 항목추가</button> --%>
									<table class="inner-table tbl-option">
										<col style="" />
										<col style="" />
										<col style="width: 100px" />
										<thead>
											<tr>
												<th>옵션 제목(예: 색상)</th>
												<th>옵션 내용(예: 빨강,노랑,파랑)</th>
												<th>삭제</th>
											</tr>
										</thead>
										<tbody id="s-option-input-wrap">
											<tr>
												<td><input type="text" title="옵션명" placeholder="예) 색상" /></td>
												<td><input type="text" title="옵션값" placeholder="예) 빨강,노랑,파랑" /></td>
												<td><a href="#" class="btn btn-dark-gray btn-sm delete_option_input">삭제</a></td>
											</tr>
										</tbody>
									</table>
								</div>


								<div class="option-type type-S2" style="display:none">
									<h4>■ 2개 조합형</h4>
									<table class="inner-table tbl-option">
										<col style="" />
										<col style="" />
										<col style="" />
										<thead>
											<tr>
												<th>옵션명</th>
												<th>옵션값</th>
											</tr>
										</thead>
										<tbody id="s2-option-input-wrap">
											<tr>
												<td><input type="text" title="옵션명1" placeholder="예) 색상" /></td>
												<td><input type="text" title="옵션값1" placeholder="예) 빨강,노랑,파랑." /></td>
											</tr>
											<tr>
												<td><input type="text" title="옵션명2" placeholder="예) 사이즈" /></td>
												<td><input type="text" title="옵션값2" placeholder="예) S,M,L,XL" /></td>
											</tr>
										</tbody>
									</table>
								</div>


								<div class="option-type type-S3" style="display:none">
									<h4>■ 3조합형</h4>
									<table class="inner-table tbl-option">
										<col style="" />
										<col style="" />
										<col style="" />
										<thead>
											<tr>
												<th>옵션 제목(예: 색상)</th>
												<th>옵션 내용(예: 빨강,노랑,파랑)</th>
											</tr>
										</thead>
										<tbody id="s3-option-input-wrap">
											<tr>
												<td><input type="text" title="옵션명1" placeholder="예) 상품구분" /></td>
												<td><input type="text" title="옵션값1" placeholder="예) 야구모자,스냅백" /></td>
											</tr>
											<tr>
												<td><input type="text" title="옵션명2" placeholder="예) 색상" /></td>
												<td><input type="text" title="옵션값2" placeholder="예) 빨강,노랑,파랑." /></td>
											</tr>
											<tr>
												<td><input type="text" title="옵션명3" placeholder="예) 사이즈" /></td>
												<td><input type="text" title="옵션값3" placeholder="예) S,M,L,XL" /></td>
											</tr>
										</tbody>
									</table>
								</div>

								<div class="item-option-table">
									<div class="btn_all btn_center">
										<div class="flex_box">
											<a href="#" class="btn btn-warning btn-sm make-item-option">적용</a>
										</div>
									</div>

									<div>
										<table class="inner-table tbl-option active" id="sActiveOption">
											<colgroup>
												<col style="" />
												<col style="" />
												<col style="" />
												<col class="hidden" style="" />
												<col style="" />
												<col style="width: 100px" />
												<col style="" />
												<col style="" />
												<col class="hidden" style="width: 150px" />
												<col style="width: 70px" />
											</colgroup>
											<thead>
												<tr>
													<th><form:input path="itemOptionTitle1" /></th>
													<th><form:input path="itemOptionTitle2" /></th>
													<th class="option-S3"><form:input path="itemOptionTitle3" /></th>
													<th>추가금액</th>
													<th class="hidden">원가</th>
													<th>재고연동</th>
													<th>재고수량</th>
													<th>판매상태</th>
													<th>노출여부</th>
													<th class="hidden">관리코드</th>
													<th>삭제</th>
												</tr>
											</thead>
											<tbody id="item-options">

												<c:set var="optionCount" value="0" />
												<c:forEach items="${item.itemOptions}" var="itemOption" varStatus="i">
													<c:if test="${itemOption.optionType != 'T'}">
														<c:set var="optionCount">${optionCount + 1}</c:set>
														<c:set var="itemOption" value="${itemOption}" scope="request"></c:set>

														<jsp:include page="include-item-option.jsp" />

													</c:if>
												</c:forEach>

												<c:if test="${optionCount == 0}">
													<jsp:include page="include-item-option.jsp" />
												</c:if>
											</tbody>
										</table>
										<%-- <div class="item-options-info">
											<div class="btn_all btn_right">
												<div class="flex_box">
													<button type="button" class="btn btn-gradient btn-sm add-item-option"><span>+ 옵션추가</span></button>
												</div>
											</div>
										</div> --%>
									</div>
								</div>

								<div class="option-type type-T" style="display:none;">
									<h4>■ 텍스트형</h4>
									<table class="inner-table tbl-option active">
										<colgroup>
											<col style="" />
											<col style="width: 70px" />
											<col style="width: 70px" />
										</colgroup>
										<thead>
											<tr>
												<th>옵션명</th>
												<th>노출여부</th>
												<th>삭제</th>
											</tr>
										</thead>
										<tbody id="item-text-options">
											<c:set var="textOptionCount" value="0" />
											<c:forEach items="${item.itemOptions}" var="itemTextOption" varStatus="i">
												<c:if test="${itemTextOption.optionType == 'T'}">
													<c:set var="textOptionCount">${textOptionCount + 1}</c:set>
													<c:set var="itemTextOption" value="${itemTextOption}" scope="request" />

													<jsp:include page="include-item-text-option.jsp" />
												</c:if>
											</c:forEach>

											<c:if test="${textOptionCount == 0}">
												<jsp:include page="include-item-text-option.jsp" />
											</c:if>
										</tbody>
									</table>
									<div class="item-options-info">
										<div class="btn_all btn_right">
											<div class="flex_box">
												<button type="button" class="btn btn-gradient btn-sm add-item-text-option"><span>+ 옵션추가</span></button>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div> <!-- // board_write -->
					</div>

					<div class="item_list mt70">
						<h3><span>필수 추가정보 등록</span></h3> <!-- 상품 필수 입력 정보란 -->

						<div class="board_write item_text_option_board">

							<!--  사용 여부 체크 -->
							<table class="board_write_table" summary="필수 추가정보 등록">
								<caption>필수 추가정보 등록</caption>
								<colgroup>
									<col style="width: 220px;" />
									<col />
								</colgroup>
								<tbody>
									<tr>
										<td class="label">필수 추가정보 사용여부</td> <!-- 옵션 사용여부 -->
										<td>
											<div class="flex_box gap-12">
												<div class="input-form">
													<form:radiobutton path="itemTextOptionFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
												</div>
												<div class="input-form">
													<form:radiobutton path="itemTextOptionFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->
													<input type="hidden" name="itemTextOptionFlagValue" value="${fn:escapeXml(item.itemTextOptionFlag)}" />
												</div>
											</div>
										</td>
									</tr>
								</tbody>
							</table>  <!--  사용 여부 체크 끝 -->

							<!--  필수 입력정보 입력란 -->
							<div class="option-wrapper option-step step2">

								<!--  필수 입력정보 입력란 -->
								<div class="item_text_option_input_box" style="display:none;">
									<h4>■ 텍스트형 필수 추가입력 정보</h4>
									<table class="inner-table tbl-option">
										<col style="" />
										<thead>
											<tr>
												<th>필수 추가정보 제목(예: 티켓을 받으실 아이디를 입력해주세요)</th>
											</tr>
										</thead>
										<tbody id="text-option-input-wrap">
											<tr>
												<td><input id="text-option-input" type="text" title="필수 추가정보 제목" placeholder="예) 티켓을 받으실 아이디를 입력해주세요" /></td>
											</tr>
										</tbody>
									</table>
									<div class="tip" style="margin-top:15px; margin-left:45px">
									    * 구매자에게 입력받을 텍스트 형식의 옵션을 추가할 수 있습니다.</br>
			                            * 각 필수 추가정보 제목은 최대 30글자까지 입력 가능합니다. 구분값은 쉼표입니다. 콜론, 수직선기호, 홑화살괄호(:, |, <, >)는 입력하실 수 없습니다.
			                        </div>
								</div><!--  필수 추가정보 입력란 끝 -->

								<!-- 필수 추가정보 입력 목록 -->
								<div class="item_text_option_list_box">
									<div class="btn_all btn_center">
										<div class="flex_box">
											<a href="#" class="btn btn-warning btn-sm make-item-option">적용</a>
										</div>
									</div>

									<div>
										<input type="hidden" name="itemTextOptionTitle1" id="itemTextOptionTitle1" value="${fn:escapeXml(item.itemTextOptionTitle1)}">
										<input type="hidden" name="itemTextOptionTitle2" id="itemTextOptionTitle2" value="${fn:escapeXml(item.itemTextOptionTitle2)}">
										<input type="hidden" name="itemTextOptionTitle3" id="itemTextOptionTitle3" value="${fn:escapeXml(item.itemTextOptionTitle3)}">

										<table class="inner-table tbl-option item_text_option_tbl active">
											<colgroup>
												<col style="" />
												<col style="width: 70px" />
											</colgroup>
											<thead>
												<tr>
													<th>필수 추가정보 제목</th>
													<th>삭제</th>
												</tr>
											</thead>
											<tbody id="item-text-options-two">
												<c:set var="itemTextOptionCount" value="0"/>
												<c:forEach var="titleNum" begin="1" end="3">		<%-- 3개까지만 등록가능 --%>
													<c:set var="optionKey" value="itemTextOptionTitle${titleNum}"/>
													<c:set var="optionText" value="${item[optionKey]}"/>
													<c:if test="${(item[optionKey] != null) && (item[optionKey] != '') }">
														<c:set var="itemTextOptionCount">${itemTextOptionCount + 1}</c:set>
														<tr style="<c:if test='${isVisible}'>display:none</c:if>">
															<td>
																<input type="text" class="required-item-text-option" name="itemTextOptionTitle" value="${fn:escapeXml(item[optionKey])}" title="필수 추가정보 제목" readonly="readonly"/>
															</td>
															<td><a href="#" class="btn btn-dark-gray btn-sm delete-item-text-option" data-num="">삭제</a></td>
														</tr>
													</c:if>
												</c:forEach>
												<c:if test="${item.itemTextOptionFlag != 'Y' || itemTextOptionCount == 0}">
													<tr>
														<td>
															<input type="text" class="required-item-text-option" name="itemTextOptionTitle" value="" title="필수 추가정보 제목" readonly="readonly" />
														</td>
														<td><a href="#" class="btn btn-dark-gray btn-sm delete-item-text-option">삭제</a></td>
													</tr>
												</c:if>

											</tbody>
										</table>
									</div>
								</div><!-- 필수 추가정보 입력 목록 끝 -->
							</div>

						</div> <!-- // board_write -->
					</div>
				</c:if>

				<%--
				<div class="item_list mt30">
					<h3><span>${op:message('M01619')}</span></h3>   추가구성상품
					<div class="board_write">
						<table class="board_write_table" summary="추가구성상품">
							<caption>추가구성상품</caption>
							<colgroup>
								<col style="width: 160px;" />
								<col style="" />
							</colgroup>
							<tbody>
								<tr>
									<td class="label">${op:message('M01620')}</td> <%-- 추가구성 사용여부
									<td>
										<div>
											<p>
												<form:radiobutton path="itemAdditionFlag" value="N" label="${op:message('M00089')}" />  <!-- 사용안함 -->
												<form:radiobutton path="itemAdditionFlag" value="Y" label="${op:message('M00083')}" />  <!-- 사용 -->
											</p>
										</div>
									</td>
								</tr>
							</tbody>
						</table>



						<div  class="option-wrapper item-addition-wrap">
							<h4>■ ${op:message('M01625')} <%-- 추가구성 상품정보 </h4>
							<p class="text-info text-sm">
								* [+항목추가] 버튼을 클릭하시면, 추가구성 상품을 여러 개 등록이 가능합니다.<br />
								* 추가구성 상품은 쿠폰 등의 고객혜택이 적용되지 않으므로, ‘상품옵션’과 구분하여 등록하여 주시기 바랍니다.<br />
								* 면세상품 선택 시, 세무/법률적 책임은 판매자에게 있습니다.
							</p>

							<button type="button" class="btn btn-gradient btn-sm add-item-addition">+ 항목추가</button>
							<table class="inner-table tbl-option">
								<col style="" />
								<col style="" />
								<col style="" />
								<thead>
									<tr>
										<th>추가상품명 (최대40자)</th>
										<th>추가상품가격</th>
										<th>원가</th>
										<th>재고연동여부</th>
										<th>재고수량</th>
										<th>판매상태</th>
										<th>부가세</th>
										<th>노출여부</th>
										<th>무게(g)</th>
										<th>재고관리코드</th>
										<th>삭제</th>
									</tr>
								</thead>
								<tbody id="item-additions">
									<c:forEach items="${item.itemAdditions}" var="itemAddition" varStatus="i">
										<c:set var="itemAddition" value="${itemAddition}" scope="request" />
										<jsp:include page="include-item-addition.jsp" />
									</c:forEach>

									<c:if test="${empty item.itemAdditions}">
										<jsp:include page="include-item-addition.jsp" />
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				--%>
				<div class="item_list mt70" id="deliveryDiv">
					<h3><span>${op:message('M01612')}</span></h3> <%-- 배송정보 설정 --%>

					<div class="board_write">
						<form:hidden path="shipmentGroupCode" />
						<table class="board_write_table" summary="배송비설정">
							<colgroup>
								<col style="width: 220px;" />
								<col style="" />
								<col style="width: 220px;" />
								<col style="" />
							</colgroup>
							<tbody>
								<tr>
									<td class="label hidden">배송구분</td>
									<td class="hidden">
										<div>
											<form:select path="deliveryType" class="required" title="배송구분">
												<option value="">선택</option>
												<form:option value="1">본사배송</form:option>
												<form:option value="2">업체배송</form:option>
											</form:select>
										</div>
									</td>

									<td class="label"><span class="required_mark">*</span>택배사</td>
									<td>
										<div>
											<form:hidden path="deliveryCompanyName" />
											<form:select path="deliveryCompanyId" class="required wd-200" title="택배사">
												<option value="0">택배사 선택</option>
												<form:options items="${op:nullToEmptyArr(deliveryCompanyList)}" itemValue="deliveryCompanyId" itemLabel="deliveryCompanyName" />
											</form:select>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M01613')}</td> <%-- 출고지 주소 --%>
									<td colspan="3">
										<div class="flex_box gap-08">
											<form:hidden path="shipmentId" />
											<form:input path="shipmentAddress" readonly="true" class="required wd-500" title="출고지 주소" />
											<button type="button" class="btn btn-default btn-mini change-shipment-address" <c:if test="${ !requestContext.sellerPage }">disabled="disabled"</c:if>><span>주소변경</span></button>
										</div>
									</td>
								</tr>
								<tr class="hidden">
									<td class="label">${op:message('M00643')} <span class="require">*</span></td> <%-- 배송비 설정 --%>
									<td colspan="3">
										<div>
											<h4>■ 배송비 설정 </h4>

											<form:hidden path="shippingGroupCode" />
											<form:hidden path="shipping" />
											<form:hidden path="shippingFreeAmount" />

											<table class="inner-table th-center shipping-option">
												<col style="width: 150px;" />
												<col style="width: 170px;" />
												<col style="" />
												<tr>
													<th>배송비 종류</th>
													<th>배송비</th>
													<th>기준 (구매금액:판매가+옵션가+추가구성상품금액)</th>
												</tr>
												<tr class="shipping-type-1">
													<td><form:radiobutton path="shippingType" value="1" class="required" title="배송비" label="${op:message('M00928')}" /> <%-- 무료배송 --%></td>
													<td class="text-right">
														무료
														<input type="hidden" class="opt-shipping" value="0" />
														<input type="hidden" class="opt-shipping-free-amount" value="0" />
													</td>
													<td>수량/주문금액에 상관없이 무료배송</td>
												</tr>
												<tr class="shipping-type-2">
													<td><form:radiobutton path="shippingType" value="2" class="required" title="배송비" label="${op:message('M01614')}" /> <%-- 판매자조건부 --%></td>
													<td class="text-right">
														<span class="seller-empty" ${!empty seller ? 'style="display:none"' : ''}>
															-
														</span>

														<span class="seller-info" ${empty seller ? 'style="display:none"' : ''}>
															<span class="opt-shipping-text">${op:numberFormat(seller.shipping)}</span> 원
														</span>

														<input type="hidden" class="opt-shipping" value="${fn:escapeXml(seller.shipping)}" />
														<input type="hidden" class="opt-shipping-free-amount" value="${fn:escapeXml(seller.shippingFreeAmount)}" />
														<input type="hidden" class="opt-shipping-extra-charge1" value="${fn:escapeXml(seller.shippingExtraCharge1)}" />
														<input type="hidden" class="opt-shipping-extra-charge2" value="${fn:escapeXml(seller.shippingExtraCharge2)}" />
													</td>
													<td>
														<span class="seller-empty" ${!empty seller ? 'style="display:none"' : ''}>
															판매자 조건부 배송비를 설정해 주세요.
														</span>

														<span class="seller-info" ${empty seller ? 'style="display:none"' : ''}>
															<span class="opt-shipping-text">${op:numberFormat(seller.shipping)}</span>원
															(<span class="opt-shipping-free-amount-text">${op:numberFormat(seller.shippingFreeAmount)}</span>원 이상 구매시 무료)
														</span>
													</td>
												</tr>
												<tr class="shipping-type-3">
													<td><form:radiobutton path="shippingType" value="3" class="required" title="배송비" label="${op:message('M01615')}" /> <%-- 출고지조건부 --%></td>
													<td class="text-right">
														<span class="shipment-empty" ${!empty shipment ? 'style="display:none"' : ''}>
															-
														</span>

														<span class="shipment-info" ${empty shipment ? 'style="display:none"' : ''}>
															<span class="opt-shipping-text">${op:numberFormat(shipment.shipping)}</span> 원
														</span>

														<input type="hidden" class="opt-shipping" value="${fn:escapeXml(shipment.shipping)}" />
														<input type="hidden" class="opt-shipping-free-amount" value="${fn:escapeXml(shipment.shippingFreeAmount)}" />
														<input type="hidden" class="opt-shipping-extra-charge1" value="${fn:escapeXml(shipment.shippingExtraCharge1)}" />
														<input type="hidden" class="opt-shipping-extra-charge2" value="${fn:escapeXml(shipment.shippingExtraCharge2)}" />
													</td>
													<td>
														<span class="shipment-empty" ${!empty shipment ? 'style="display:none"' : ''}>
															출고지 조건부 배송비를 설정해 주세요.
														</span>

														<span class="shipment-info" ${empty shipment ? 'style="display:none"' : ''}>
															<span class="opt-shipping-text">${op:numberFormat(shipment.shipping)}</span>원
															(<span class="opt-shipping-free-amount-text">${op:numberFormat(shipment.shippingFreeAmount)}</span>원 이상 구매시 무료)
														</span>
														<!-- <a href="#" class="btn btn-dark-gray btn-xs change-shipment-address">기준설정</a> -->
													</td>
												</tr>
												<tr class="shipping-type-4">
													<td><form:radiobutton path="shippingType" value="4" class="required" title="배송비" label="${op:message('M01616')}" /> <%-- 상품조건부 --%></td>
													<td class="text-right">
														<input type="text" maxlength="6" value="${fn:escapeXml(item.shipping)}" readonly="readonly" class="required-shipping-4 _min_10 _number opt-shipping amount" title="배송비" /> 원
													</td>
													<td><input type="text" maxlength="6" value="${fn:escapeXml(item.shippingFreeAmount)}" readonly="readonly" class="required-shipping-4 _min_10 _number opt-shipping-free-amount amount" title="무료배송 조건 금액" /> 원 이상 구매 시 무료</td>
												</tr>
												<tr class="shipping-type-5">
													<td><form:radiobutton path="shippingType" value="5" class="required" title="배송비" label="${op:message('M01617')}" /> <%-- 개당배송비 --%></td>
													<td class="text-right"><input type="text" maxlength="6" value="${fn:escapeXml(item.shipping)}" readonly="readonly" class="required-shipping-5 _min_10 _number opt-shipping amount" title="배송비" /> 원</td>
													<td>
														<input type="hidden" class="opt-shipping-free-amount" value="0" />
														수량 <form:input path="shippingItemCount" maxlength="2" readonly="readonly" class="required-shipping-5 _min_1 _number form-xs" title="개당배송비 부과 기준 상품 수" />개마다 배송비 추가
													</td>
												</tr>
												<tr class="shipping-type-6">
													<td><form:radiobutton path="shippingType" value="6" class="required" title="배송비" label="${op:message('M01618')}" /> <%-- 고정배송 --%></td>
													<td class="text-right"><input type="text" maxlength="6" value="${fn:escapeXml(item.shipping)}" readonly="readonly" class="required-shipping-6 _min_10 _number opt-shipping amount" title="배송비" /> 원</td>
													<td><input type="hidden" class="opt-shipping-free-amount" value="0" /> 수량/주문금액과 상관없이 고정 배송비</td>
												</tr>
											</table>


											<h4 style="margin-top: 20px;">■ 제주/도서산간 추가 배송비 설정</h4>

											<p class="text-info text-sm" style="position: relative;">
												* 제주/도서산간 지역 배송지의 경우 구매자에게 추가 운송비를 부담하게 설정합니다.<br />
												<a href="javascript:Common.popup('/island/island-popup?mode=1', 'islandPopup', 600, 580)" class="btn btn-dark-gray btn-xs" style="position: absolute; right: 0; bottom: 3px;">제주/도서산간지역 보기</a>
											</p>

											<table class="inner-table th-center shipping-option">
												<col style="width: 150px;" />
												<col style="" />
												<col style="width: 150px;" />
												<col style="" />
												<tr>
													<th>제주</th>
													<td><form:input path="shippingExtraCharge1" readonly="true" class="amount required _number_comma" title="제주도 추가 배송비" /> 원</td>
													<th>도서산간</th>
													<td><form:input path="shippingExtraCharge2" readonly="true" class="amount required _number_comma" title="도서산간 추가 배송비" /> 원</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>반품/교환 신청 가능 여부</td>
									<td colspan="3">
										<div class="flex_box gap-12">
											<div class="input-form">
												<form:radiobutton path="itemReturnFlag" value="Y" label="가능" />
											</div>
											<div class="input-form">
												<form:radiobutton path="itemReturnFlag" value="N" label="불가능" />
											</div>
										</div>
									</td>
								</tr>
								<tr class="hidden">
									<td class="label">반품/교환 구분</td>
									<td>
										<div>
											<form:select path="shipmentReturnType" class="required" title="반송구분">
												<option value="">선택</option>
												<form:option value="1">본사반품</form:option>
												<form:option value="2">업체반품</form:option>
											</form:select>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>반품/교환 주소</td>
									<td colspan="3">
										<div class="flex_box gap-08">
											<form:hidden path="shipmentReturnId" />
											<form:input path="shipmentReturnAddress" readonly="true" class="required wd-500" title="반송지 주소" />

											<button type="button" class="btn btn-default btn-mini change-shipment-return-address" <c:if test="${ !requestContext.sellerPage }">disabled="disabled"</c:if>>주소변경</button>
										</div>
									</td>
								</tr>

								<tr class="hidden">
									<td class="label">반품/교환 배송비</td>
									<td colspan="3">
										<div>
											<p class="text-info text-sm">
												* 반품/교환 시 배송비를 고객/판매자에게 안내됩니다. (정산에 반영되지 않습니다.)<br />
												* 편도 기준 금액으로 입력해 주세요. <br />
												* 교환의 경우 입력한 금액 * 2 한 금액으로 안내됩니다. (2,500원 * 2 = 5,000원)<br />
												* 단순 변심, 상품 파손 등의 반품/교환 사유를 확인하고 필요 시 구매자로 부터 반품/교환비를 오프라인으로 결제한 후에 반품/교환 절차를 진행해 주십시오.
											</p>
											<form:input path="shippingReturn" class="amount required _number_comma" title="반품/교환 배송비" /> 원 (편도 기준 금액)
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="item_list item_info mt70">
					<h3><span>${op:message('M01621')}</span></h3> <%-- 상품정보고시 --%>

					<%--
					<button type="button" class="btn btn-gradient btn-sm add_item_info" onclick="addItemInfo()"><span>${op:message('M01002')}</span></button>	 <!-- 항목추가 -->
					--%>
					<div class="board_write">
						<table class="board_write_table">
							<caption>상품정보등록</caption>
							<colgroup>
								<col style="width: 160px;" />
								<col style="" />
							</colgroup>

							<tbody id="item_info_area">
								<tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M01628')} <%-- 상품의 상품군 --%></td>
									<td>
										<div>
											<form:select path="itemNoticeCode" class="required wd-500" title="${op:message('M01628')}"> <%-- 상품의 상품군 --%>
												<option value="">선택하세요</option>
												<form:options items="${op:nullToEmptyArr(itemNoticeCodes)}"  itemValue="itemNoticeCode" itemLabel="itemNoticeTitle" />
											</form:select>

											<label class="check-all-item-notice-label"><input type="checkbox" class="check-all-item-notice" /> ${op:message('M01622')} <%-- ‘상세정보 별도표기’ 모두 선택 --%></label>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div> <!-- // board_write -->

					<div id="item-notice-data" style="display:none">
						<c:forEach items="${item.itemInfos}" var="itemInfo">
							<p>
								<span class="title">${fn:escapeXml(itemInfo.title)}</span>
								<span class="desc">${fn:escapeXml(itemInfo.description)}</span>
							</p>
						</c:forEach>
					</div>

					<div class="check-all-item-notice-label">
						* 하기의 정보는 <strong>전자상거래법(제13조2항) 및 공정위 상품정보제공고시</strong>에 의거하여 판매회원이 스스로의 책임으로 입력하여야 하는 사항입니다.<br />
						* 여러 상품(주된 상품에 부수되는 상품은 제외)을 추가선택으로 판매하는 경우에는 각 상품에 해당하는 정보를 모두 입력하여야 합니다.<br />
						* 제공되는 입력란의 공간이 부족한 경우에는 광고화면에 직접 입력하셔도 무방합니다.<br>
					</div>
				</div>


				<div class="item_list mt70">
					<h3><span>상세 설명</span></h3> <!-- 상세 설명 -->
					<button type="button" class="btn btn-gradient btn-sm add_item_point" onclick="clearContent()"><span>&nbsp;⬇︎ CLEAR &nbsp;︎</span></button>
					<form:textarea path="detailContent" cols="30" rows="20" class="editor-content" maxlength="10000" title="${op:message('M00990')}"/>
				</div>

				<div class="copy_button hidden">
					<button type="button" class="btn btn-gradient btn-sm add_item_point" onclick="copyContent('detailContent')"><span>&nbsp;⬇︎ COPY &nbsp;︎</span></button>
				</div>

				<div class="item_list mt70 hidden">
					<h3><span>${op:message('M00990')} (${op:message('M00236')})</span></h3> <!-- 상품 상세 설명 --> <!-- 모바일 -->

					<form:textarea path="detailContentMobile" cols="30" rows="6" class="editor-content" style="width: 1085px" title="${op:message('M00990')} (${op:message('M00236')})" />
				</div>
				<div class="item_list mt70 hidden">
					<h3><span>카테고리 필터설정</span></h3> <!-- 카테고리 필터 -->
					<div class="board_write">
						<table class="board_write_table" id="categoryFilterSetting">
							<colgroup>
								<col style="width: 160px;" />
								<col style="" />
							</colgroup>
							<tbody>
								<tr>
									<td colspan="2">
										<div>
											<h4>필터가 등록된 카테고리를 선택한 경우 필터 설정 항목이 노출됩니다.</h4>
										</div>
									</td>
								</tr>
							</tbody>
						</table>

					</div>
				</div>
				<div id="categoryInput"></div>
				<div class="item_list mt70 hidden">
					<h3><span>${op:message('M00985')}</span></h3> <!-- 관련상품등록 -->

					<div class="board_write">

						<table class="board_write_table" summary="관련상품등록">
							<caption>관련상품등록</caption>
							<colgroup>
								<col style="width: 160px;" />
								<col style="" />
							</colgroup>
							<tbody>
								<tr>
									<td class="label">${op:message('M00986')}</td> <!-- 관련상품 출력 방법 -->
									<td>
										<div>
											<p>
												<form:radiobutton path="relationItemDisplayType" value="1" label="${op:message('M00987')}" />  <!-- 관련상품을 해당카테고리에서 임의로 출력합니다. -->
												<form:radiobutton path="relationItemDisplayType" value="2" label="${op:message('M00988')}" />  <!-- 관련상품을 선택합니다. -->
											</p>
										</div>
									</td>
								</tr>

								<!-- 관련상품을 선택시 노출 -->
								<c:set var="relationItemDisplay" value="display:none;" />
								<c:if test="${item.relationItemDisplayType == '2' || !empty item.itemRelations}">
									<c:set var="relationItemDisplay" value="" />
								</c:if>
								<tr id="relation_item_area" style="${fn:escapeXml(relationItemDisplay)}">
									<td class="label">${op:message('M01213')}</td> <!-- 관련상품 -->
									<td>
										<div>
											<p class="mb10">
												<%-- <button type="button" id="button_add_relation_item" class="btn btn-gradient btn-sm" onclick="Shop.findItem('related')">+ ${op:message('M00989')}</button> --%> <!-- 관련상품 추가 -->
												<button type="button" id="button_add_relation_item" class="btn btn-gradient btn-sm" onclick="findItemPopup('related','${fn:escapeXml(item.itemUserCode)}')">+ ${op:message('M00989')}</button> <!-- 관련상품 추가 -->
												<button type="button" class="btn btn-gradient btn-sm" onclick="Shop.deleteRelationItemAll('related')">x ${op:message('M00411')}</button> <!-- 전체삭제 -->
											</p>

											<ul id="related" class="sortable_item_relation">
												<li style="display: none;"></li>

												<c:forEach items="${item.itemRelations}" var="itemRelation" varStatus="i">
													<c:if test="${!empty itemRelation.item.itemId}">
														<li id="related_item_${fn:escapeXml(itemRelation.item.itemId)}">
															<input type="hidden" name="relatedItemIds" value="${fn:escapeXml(itemRelation.item.itemId)}" />
															<p class="image"><img src="${shop:loadImageBySrc(itemRelation.item.imageSrc, 'XS')}" class="item_image size-100 none" alt="상품이미지" /></p>
															<p class="title">[${fn:escapeXml(itemRelation.item.itemUserCode)}]<br />${fn:escapeXml(itemRelation.item.itemName)}</p>

															<span class="ordering">${fn:escapeXml(i.count)}</span>
															<a href="javascript:Shop.deleteRelationItem('related_item_${fn:escapeXml(itemRelation.item.itemId)}');" class="delete_item_image"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
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
				</div>


				<div class="item_list mt70 hidden">
					<h3>
						<span>${op:message('M00995')}</span> <!-- SEO 설정 -->
						<span class="f12"></span>
					</h3>

					<div class="board_write">

						<table class="board_write_table">
							<colgroup>
								<col style="width: 160px;">
								<col style="">
							</colgroup>
							<tbody>
								<tr>
									<td class="label">검색 엔진에 공개</td>
									<td>
										<div>
											<p class="text-info text-sm">
												* 검색 엔진에 상품 페이지 공개 여부를 설정합니다.
											</p>
											<p>
												<form:radiobutton path="seo.indexFlag" value="Y" label="${op:message('M01418')}" /> <!-- index 시킴 -->
												<form:radiobutton path="seo.indexFlag" value="N" label="${op:message('M01419')}" /> <!-- index 시키지 않음 -->
											</p>
										</div>
									</td>
								</tr>

								<tr>
									<td class="label">${op:message('M00090')}</td> <!-- 브라우저 타이틀 -->
									<td>
										<div>
											<p class="text-info text-sm">
												* 제목은 브라우저의 상단과 검색 엔진에 페이지 제목으로 나타납니다. <br />
												* 제목은 최대 55-60자 까지 가능합니다.  <br />
												* 2-5 단어로 페이지를 설명할 수 있는 제목을 선택하세요.<br />
												* (예: 상품명, 페이지명 등)<br>
												* (예: 상품페이지의 경우 상품명을 입력해 주시면 됩니다.)
											</p>
											<form:input path="seo.title" maxlength="100" class="eight" title="${op:message('M00090')}" />

										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00997')}</td> <!-- Meta 키워드 -->
									<td>
										<div>
											<p class="text-info text-sm">
												* 최대 10 키워드로 페이지를 홍보할 수 있습니다. <br />
												* 키워드는 웹사이트 콘텐츠를 설명하는 단어나 짧은 구문이며 검색 사이트에서 사용자가 해당 페이지를 검색할 때 사용할 만한 단어를 포함해야 합니다.<br />
												* 태그란에 키워드를 입력할 때는 쉼표로 키워드들을 구분합니다.
											</p>
											<form:input path="seo.keywords" maxlength="100" class="eight" title="${op:message('M00997')}" />

										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M00998')}</td> <!-- Meta용 기술서 -->
									<td>
										<div>
											<p class="text-info text-sm">
												* 해당 문구는 검색 엔진 목록의 사이트 제목 아래에 표시됩니다. <br />
												* 페이지 설명은 최대 250자 까지 사용 가능합니다. (또는 약  15-20 단어) <br />
												* (예: 페이지 제목이 Google 지도인 경우, 사이트 설명은 "웹에서 지도와 운전 경로를 보고 지역 정보를 검색하세요!"가 될 수 있습니다.)
											</p>
											<form:textarea path="seo.description" maxlength="200" class="full" rows="5" title="${op:message('M00998')}" />
										</div>
									</td>
								</tr>

								<tr>
									<td class="label">${op:message('M00999')}</td> <!-- H1태그 -->
									<td>
										<div>
											<p class="text-info text-sm">
											※ ${op:message('M01040')} <!-- 페이지 상단의 <H1> 태그에 배포됩니다. -->
											</p>
											<form:input path="seo.headerContents1" maxlength="100" title="${op:message('M00999')}" class="eight" />
										</div>
									</td>
								</tr>
								<%--
								<tr>
									<td class="label">${op:message('M01000')}</td> <!-- 테마워드용 타이틀 -->
									<td>
										<div>
											<form:input path="seo.themawordTitle" title="${op:message('M01000')}" class="eight" />
											<button type="button" class="copy_item_name btn btn-gradient btn-sm"><span>${op:message('M01023')}</span></button> <!-- 상품명 복사 -->
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M01001')}</td> <!-- 테마워드용 기술서 -->
									<td>
										<div>
											<form:textarea path="seo.themawordDescription" cols="30" rows="3" title="${op:message('M01001')}" />
										</div>
									</td>
								</tr>
								 --%>
							</tbody>
						</table>

					</div> <!-- // board_write -->


				</div> <!-- // item_list02 -->

				<!-- 이미지 설명 이용자 선택 버전 -->

				<div class="item-option-table2">
					<div>
					<p class="text-info text-sm">* 시각 장애인을 위한 이미지 설명을 입력하여 주시길 부탁드립니다. *</p>
						<table class="inner-table tbl-option active" id="sActiveOption2">
							<colgroup>
								<col style="width: 100px;"/>
								<col/>
								<col style="width: 50px;"/>
							</colgroup>
							<thead>
								<tr>
									<th style="text-align:center;">순번</th>
									<th style="text-align:center;">이미지 설명</th>
									<th>삭제</th>
								</tr>
							</thead>
							<tbody id="item-options2">
								<c:forEach items="${item.itemImageExplain}" var="imagesExplain" varStatus="loopStatus">

									<c:if test="${not empty imagesExplain.imagesValues}">
										<c:set var="imagesIndexes" value="${imagesExplain.imagesIndexes}" scope="request"></c:set>
										<c:set var="imagesValue" value="${imagesExplain.imagesValues}" scope="request"></c:set>
									<tr>
										<td>
										    <input type="text" id="imageExplainIndex" name="itemImageExplain[${fn:escapeXml(loopStatus.index)}].imagesIndexes" maxlength="200" class="이미지 설명 순서" title="${op:message('M00977')}" value="${fn:escapeXml(imagesIndexes)}"  readonly/>
										</td>
										<td>
										    <%-- <input type="text" id="imageExplainValue" name="itemImageExplain[${loopStatus.index}].imagesValues" maxlength="200" title="이미지 설명" value="${imagesValue}" /> --%>
										    <textarea id="imageExplainValue" name="itemImageExplain[${fn:escapeXml(loopStatus.index)}].imagesValues">${fn:escapeXml(imagesValue)}</textarea>
										</td>
										<td><a href="#" class="btn btn-dark-gray btn-sm delete-item-option2">삭제</a></td>
									</tr>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
							<div class="item-options-info">
								<div class="btn_all btn_right">
									<div class="flex_box">
										<button type="button" class="btn btn-gradient btn-sm add-item-option2"><span>+ 추가</span></button>
									</div>
								</div>
							</div>
					</div>
			</div>



			<%-- <div class="board_write">
			    <table class="board_write_table">
			        <caption>상품정보등록</caption>
			        <colgroup>
			            <col style="width: 120px;">
			            <col style="">
			        </colgroup>
					<table id="webAccessibility" class="inner-table tbl-option">
						<p class="text-info text-sm">* 시각 장애인을 위한 이미지 설명을 입력하여 주시길 부탁드립니다. *</p>
						<colgroup><col style="">
						<col style="">
						<col style="width: 80px">
						</colgroup>
						<thead>
							<tr>
								<th style="width: 10%;">이미지 순서</th>
								<th>이미지 설명</th>
								<!-- <th>삭제</th> -->
							</tr>
						</thead>
						<tbody id="s-option-input-wrap">
							<tr id="accessibilityTr">
								<td><input type="text" id="imagesIndexes" title="옵션명" placeholder="" readonly="Y" data-siid="si_input_23"></td>
								<td><input type="text" id="imagesValues" title="옵션값" placeholder="예) 서울의 특산물 수라상에 올라가던 과일 '수라배'를 설명하는 이미지" data-siid="si_input_24"></td>
								<!-- <td><a href="#" class="btn btn-dark-gray btn-sm delete_option_input">삭제</a></td> -->
							</tr>
						</tbody>
					</table>

			<!-- 	<table class="inner-table2 tbl-option">
			        <tbody id="item_info_area2">
			            <tr>
			                <td class="label"><span class="required_mark">*</span>이미지 설명

			                <td>
		                        <input type="text" name="itemInfoDescriptions" maxlength="100" class="required" title="소비자상담 전화번호" readonly="" data-siid="si_input_63">
			                </td>
			                <td>
			                	<input type="text" name="itemInfoDescriptions" maxlength="100" class="required" title="소비자상담 전화번호" readonly="" data-siid="si_input_64">
			                </td>
			            </tr>
			        </tbody>
			    </table> -->
			    </table>
			</div> --%>

				<div id="buttons" class="btn_all btn_right">
					<div class="flex_box gap-08">
						<c:if test="${pendingApproval == '1'}">
						<button type="button" class="btn btn-warning btn-approval"><span class="glyphicon glyphicon-ok"></span> 승인처리</button>
						</c:if>
						<button type="submit" class="btn btn-dark-gray btn-mini">${item.itemId == 0 ? op:message('M00088') : op:message('M00087')}</button>
						<a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/item/list')" class="btn btn-default">${op:message('M00480')}</a>	<!-- 목록 -->
					</div>
				</div>
			</div>
		</form:form>




<table id="item_point_template" style="display:none">
	<tbody>
		<tr>
			<td>
				<input type="text" name="point" maxlength="4" class="three _number" title="${op:message('M00246')}" />
				<input type="hidden" name="pointType" />
				{INPUT_RADIO}
			</td>
			<td>
				<span class="datepicker"><input type="text" name="pointStartDate" class="term _date" maxlength="8" title="시작일"></span>
				<select name="pointStartTime" title="시간 선택">
					<c:forEach items="${hours}" var="code">
						<option value="${fn:escapeXml(code.value)}">${fn:escapeXml(code.label)}</option>
					</c:forEach>
				</select>시 ~
				<span class="datepicker"><input type="text" name="pointEndDate" class="term _date" maxlength="8" title="종료일"></span>
				<select name="pointEndTime" title="시간 선택">
					<c:forEach items="${hours}" var="code">
						<option value="${fn:escapeXml(code.value)}">${fn:escapeXml(code.label)}</option>
					</c:forEach>
				</select>시 59분

				<input type="hidden" name="pointRepeatDay" value="" />
			</td>
			<%--
			<td>
				<select name="pointRepeatDay">
					<option value="">전체</option>
					<c:forEach begin="1" end="31" step="1" var="i">
						<option value="${ i }">${ i }</option>
					</c:forEach>
				</select> ${op:message('M00511')} <!-- 일 -->
			</td>
			--%>
			<td><a href="#" class="fix_btn delete_item_point">${op:message('M00074')}</a></td> <!-- 삭제 -->
		</tr>
	</tbody>
</table>

<module:smarteditorInit />
<module:smarteditor id="detailContent"/>
<module:smarteditor id="detailContentMobile"/>

<script type="text/javascript">
window.onload = () => {
	// 스마트에디터 사진 용량제한
	picAppLoad();
}

// Editor 내용 순수 텍스트 추출
const splitHtml = (content) => {
	let result = '';
	if(content) {
		result = content.replace(/(<[^>]+)>/gi, '')  // 태그 제거
						.replace(/&nbsp;/gi, '')     // 줄바꿈 제거
						.replace(/&[^;]+;/gi, '');   // 엔티티 제거
	} else {
		result = '';
	}

	return result;

}


$(function() {
	// 권한체크
	$('#impossible-form').submit(function() {
		alert('답례품제공자만 등록/수정이 가능합니다.');
		Common.loading.hide();
		return false;
	});

	// 필수 입력항목 마커.
	Common.displayRequireMark();

	// 상품명 글자수 체크
	Common.checkedMaxStringLength($('#itemName'), null, 100);

	// 상품명 길이
	$("#itemlength").text("["+$('#itemName').val().length+"/50]");

	// 상품명 길이
	$("#itemSummarylength").text("["+$('#itemSummary').val().length+"/100]");

	// 상세설명 - 추가/삭제 이벤트 핸들러
	// itemInfoEventHandler();

	$('#brand').on('change', function() {
		$("#brandId").val($("#brand option:selected").attr("data-id")); // 2021.06.18
	});

	$("input[name='spotDateType']").on('change', function() {
        selectSpotDateType($(this).val());
    });

	// 스팟 기간 구분
    selectSpotDateType('${fn:escapeXml(item.spotDateType)}');

	//팀/그룹 ~ 4차 카테고리 이벤트
	ShopEventHandler.categorySelectboxChagneEvent();

	// 버튼 스크롤
	scrollButtons();

	// 비회원 판매가격 이벤트 핸들러.
	initNonmemberPriceEvent();

	// 판매자 선택 이벤트 핸들러.
	initSellerIdEvent();

	// 재고 연동 여부에 따른 수량입력 show/hide 이벤트
	initStockFlagEvent();

	// 사은품 이벤트 핸들러.
	initFreeGiftEvent();

	// 상품 수수료 설정 이벤트 핸들러
	initCommissionEvent();

	// 할인 / 포인트 설정 이벤트 핸들러
	initDiscountAndPointEvent();

	// 상품 옵션 이벤트 핸들러.
	initItemOptionEvent();

	// 20260325 필수 추가정보 _상품 필수 추가정보 이벤트 핸들러.
	initItemTextOptionEvent();

	// 추가 상품 이벤트 핸들러.
	initItemAdditionEvent();

	// 배송 정보 설정 이벤트 핸들러.
	initShippingEvent();

	// 상품정보고시 관련 이벤트 핸들러.
	initItemNoticeEvent();

	// 상품 승인 처리 이벤트
	initItemApprovalEvent();

	// 세트상품 처리 이벤트
	initItemSetEvent();

	// Validation 조건 설정
	setValidatorCondition();

	// 모바일 상품일 경우 Validation 조건 설정
	setMobileValidatorCondition();

	// 카테고리 필터 조회(수정 시)
	if(${fn:escapeXml(mode) == 'edit'}){
		//getItemCategoryFilter(${fn:escapeXml(categoryId)})
	}

	// 카테고리 필터 조회(복사 시)
	if(${fn:escapeXml(mode) == 'copy'}){
		//getItemCategoryFilter(${fn:escapeXml(categoryId)})
	}

	// 숫자 컴마.
	Common.addNumberComma();

	// multiple file is not support
	if (!File.isSupportMultiple) {
		// 파일 추가
		$('#add_detail_image_file').css('margin-bottom', '10px').show().on("click", function() {
			var html = '<p><input type="file" name="detailImageFiles[]" multiple="multiple" accept="image/png, image/jpeg, image/gif" /> <a href="#" class="delete_detail_image_file">[' + Message.get("M00074") + ']</a></p>';  // 삭제
			$('#multiple_files').append(html);
		});

		// 추가항목 삭제
		$('#multiple_files').on('click', '.delete_detail_image_file', function(e) {
			e.preventDefault();
			$(this).parent().remove();
		});
	}

	// 상품 카테고리 드레그
	$('.sortable_item_category').sortable({
		placeholder: "sortable_item_category_placeholder",
		stop : function(event, ui){
			var cId = $('#item_categories>li:first').find('input').val();
			//getItemCategoryFilter(cId);
		}
	});


	// 상세이미지 드레그
	$('.sortable_item_image').sortable({
		placeholder: "sortable_item_image_placeholder"
	});

	// 관련상품 드레그
    $(".sortable_item_relation").sortable({
        placeholder: "sortable_item_relation_placeholder"
    });

	// 세트상품 드레그
	$(".sortable_item_set").sortable({
		placeholder: "sortable_item_set_placeholder"
	});

    $(".sortable_item_image, .sortable_item_relation, .sortable_item_set").disableSelection();


	$('#itemUserCode').on("change", function() {
		$('#useItemUserCode').val("N");
	});



	// 포인트 설정 정보 노출 여부 체크
	displayItemPointConfig();


	// 포인트 설정 삭제
	$('#item_point_area').on('click', '.delete_item_point', function(e){
		e.preventDefault();

		$(this).closest("tr").remove();
		displayItemPointConfig();
	});

	// 관련상품 선택방법
	setRelationItemDisplay();
	$('input[name=relationItemDisplayType]').on('click', function() {
		setRelationItemDisplay();
	});


	// 상품코드 중복체크
	$('#btn_check_duplicate').on("click", function(e){
		e.preventDefault();

		var $itemUserCode = $('#itemUserCode');
		if (!$.validator.required($itemUserCode)) {
			return false;
		}


		$.post("${fn:escapeXml(requestContext.managerUri)}/item/check-for-duplicate-item-user-code", {'itemUserCode': $itemUserCode.val()}, function(response) {
			Common.responseHandler(response, function(response) {
				$('#useItemUserCode').val("Y");
				alert(Message.get("M00885"));	// 사용가능

			}, function(response){
				$('#useItemUserCode').val("N");
				alert(Message.get("M01088"));	// 사용불가
				$('#itemUserCode').val("").focus();

			});

		}, 'json');

	});

	// 상품명 복사
	$('.copy_item_name').on('click', function() {
		$(this).prev().val($('#itemName').val());
	});

	// 네이버 쇼핑 사용 여부
	$('input[name="naverShoppingFlag"]').on('click', function () {
		if ($(this).val() == 'Y') {
			$('#naverShoppingItemName').attr('disabled', false);
		} else {
			$('#naverShoppingItemName').val('');
			$('#naverShoppingItemName').attr('disabled', true);
		}
	});

	// 세트상품 이벤트
	$(document).on('change', '#setDiscountAmount, #setDiscountType, input[name=setQuantities]', function() {
		initItemSetEvent();
	});

	// 폼체크
	$("#item").validator({
		'submitHandler' : function() {

			//접근성 이미지 설명 입력란에 공백이 들어 올 시 해당 로우 제거하여 not null 데이터가 null로 들어가지 않도록 방지
			let rows = $('#item-options2 tr');
		    for(let i = 0; i < rows.length; i++) {
		        let inputValue = $(rows[i]).find('textarea[id="imageExplainValue"]').val();
		        if (!inputValue || inputValue.trim() === "") {
		            $(rows[i]).remove();
		        }
		    }
		    rows = $('#item-options2 tr');
		    let size = rows.size();
			let index = 0;
			for(i = 0; i < size; i++) {
				index = i+1;
				$('#item-options2 tr:eq(' + i + ')').find('textarea[id="imageExplainValue"]').attr('name', 'itemImageExplain[' + (index - 1) + '].imagesValues');
				$('#item-options2 tr:eq(' + i + ')').find('input[id="imageExplainIndex"]').attr('name', 'itemImageExplain[' + (index - 1) + '].imagesIndexes');
				$('#item-options2 tr:eq(' + i + ')').find('input[id="imageExplainIndex"]').val(index);
			}
			//접근성 이미지 설명 이미지 alt에 입력
			let values = [...document.querySelectorAll("#item-options2 input[id='imageExplainValue']")].map(input => input.value);
            let outerIframe = document.querySelectorAll("#editor_frame")[0];
            let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
            let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");
            let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;

            let detailSize = new Blob([innerIframeDoc.body.innerHTML]).size;
            if(detailSize>5000000){ //상세 설명란 5MB제한 트래픽 이슈로인해 상세설명의 용량은 5mb로 제한합니다.
//             	alert('작성하신 상세설명 내용이 2MB를 넘었습니다. \n현재까지 작성하신 내용 크기는 ' + Math.round((detailSize/1000000)*100)/100 + 'MB 입니다. \nCLEAR 후 다시 작성해 주세요.');
            	let quot = detailSize/1000000;
            	let result = quot - Math.floor(5000000/1000000);
            	alert('트래픽 이슈로인해 상세설명의 용량은 5MB로 제한합니다. \n현재까지 초과된 내용 크기는 ' + Math.round(result*10000)/10000 + 'MB 입니다. \nCLEAR 후 다시 작성해 주세요.');
            	return false;
            }

            let images = innerIframeDoc.querySelectorAll("img");
            for (let i = 0; i < images.length; i++) {
                let img = images[i];
                if (values[i]) {
                    img.setAttribute("alt", values[i]);
                }
            }

	        /* 접근성 이미지 설명 이미지와 갯수 매칭 유효성 검사 */
	        if (size != images.length) {
	        	alert('상세설명에 올리신 사진의 갯수와 \n이미지 설명의 갯수를 동일하게 맞추어 주셔야 합니다. \n현재 작성하신 이미지 설명 갯수: '+size+' , 상세설명에 올리신 이미지 갯수: '+images.length+' ');
	        	return false;
	        }


			var selectFileCount = 0;
			$.each($(':file[class*="input_file"]'), function(){
				if ($(this).val() != '') {
					selectFileCount++;
				}
			});

			if (selectFileCount == 0  && $("#item_details_images li").length == 0) {
				alert('최소 1개의 이미지를 등록하셔야 합니다.');
				$('input[name="detailImageFiles[]"]').focus();
				return false;
			}

			if ($('input[name="detailImageFiles[]"]')[0].files.length > 10) {
				alert('이미지 파일은 최대 10까지 등록할 수 있습니다.');
				$('input[name="detailImageFiles[]"]').focus();
				return false;
			}

			var maxSize = 500 * 1024;	//500KB

			for(var i=0; i < $('input[name="detailImageFiles[]"]')[0].files.length; i++) {
			    var ext = $('input[name="detailImageFiles[]"]')[0].files[i].name.split('.').pop().toLowerCase();
			  	if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	   alert('등록할 수 없는 파일확장자가 포함되어 있습니다.');
			  	   $('input[name="detailImageFiles[]"]').focus();
			  	   return false;
			 	}

				if($('input[name="detailImageFiles[]"]')[0].files[i].size > maxSize) {
					alert("이미지 용량은 파일당 500KB 이하로 등록 가능합니다.\n첨부된 파일의 용량을 확인해주세요!");
					$('input[name="detailImageFiles[]"]').focus();
					return false;
				}
			}

			if(!$('input[name=mobileItemYn]').prop('checked') && $('#deliveryCompanyId').val() == "0") {
				alert('택배사 항목을 선택해 주세요.');
				$('#deliveryCompanyId').focus();
				return false;
			}

			var itemType = "${fn:escapeXml(item.itemType)}";
			if (itemType == '3' && $(".set_items").length == 0) {
				alert("세트상품을 추가해주세요.");
				$("#button_add_set_item").focus();
				return false;
			}

			var impossible = ${item.spotFlag == 'Y' && ((item.spotType == '1' && requestContext.sellerPage) || (item.spotType == '2' && !requestContext.sellerPage))};
            var spotDateType = $("input[name='spotDateType']:checked").val();

			if (!impossible) {
				if (spotDateType == '2') {
					$("#spotStartDate").val($("#spotStartDateTwo").val());
					$("#spotEndDate").val($("#spotEndDateTwo").val());
					$("#spotStartTime").val($("#spotStartHourTwo").val() + "" + $("#spotStartMinuteTwo").val() + "00");
					$("#spotEndTime").val($("#spotEndHourTwo").val() + "" + $("#spotEndMinuteTwo").val() + "59");
				} else {
					$("#spotStartDate").val($("#spotStartDateOne").val());
					$("#spotEndDate").val($("#spotEndDateOne").val());
					$("#spotStartTime").val($("#spotStartHourOne").val() + "" + $("#spotStartMinuteOne").val() + "00");
					$("#spotEndTime").val($("#spotEndHourOne").val() + "" + $("#spotEndMinuteOne").val() + "59");
				}
			}

            if ($("input[name='spotFlag']:checked").val() == 'Y') {
                if ($("#spotStartDate").val().length != 8 || $("#spotEndDate").val().length != 8) {
                    alert("스팟 기간을 정확히 입력해주세요.");
                    return false;
				}
			}

			if ($('input[name=categoryIds]').size() == 0) {
				alert(Message.get("M00078"));	// 상품 카테고리를 선택해 주세요.
				$('#categoryGroupId').focus();
				return false;
			}

			if ($('#itemUserCode').size() > 0 && $('#useItemUserCode').val() != "Y") {
				alert(Message.get("M00379"));		// 상품코드 중복 여부가 체크되지 않았습니다.
				$('#itemUserCode').focus();
				return false;
			}

			/* 비회원 가격
			var $salePriceNonmember = $('#salePriceNonmember');
			if ($('input[name=nonmemberOrderType]:checked').val() == '1' &&
					($.trim($salePriceNonmember.val()) == '' || $salePriceNonmember.val() == '0')) {
				$.validator.validatorAlert($.validator.messages['text'].format($salePriceNonmember.attr('title')), $salePriceNonmember);
				$salePriceNonmember.focus();
				return false;
			}*/

			// 할인 가능 금액 체크
			// 할인 가능금액 100 - 수수료율 - 30 까지만 가능.
			Common.removeNumberComma();

			var supplyPrice = Number($('#supplyPrice').val());
			var salePrice = Number($('#salePrice').val());

			if (supplyPrice > salePrice) {
				alert('공급가는 판매금액보다 높게 설정할 수 없습니다.\n판매금액 및 공급가를 확인해 주세요.');
				$('#supplyPrice').focus();
				return false;
			}

			var discountLimitRate = 70;	// 100-30 (30%는 임의 수치, 판매자 즉시할인+스팟할인+수수료로 계산되도록 개선 필요)

			var sellerDiscountAmount = 0;
			if ($('input[name=sellerDiscountFlag]:checked').val() == 'Y') {
				sellerDiscountAmount = Number($('#sellerDiscountAmount').val());
			}

			var spotDiscountAmount = 0;
			if ($('input[name=spotFlag]:checked').val() == 'Y') {
				spotDiscountAmount = Number($('#spotDiscountAmount').val());

				var spotWeekDay = '';
				$('input[name=day_of_week]:checked').each(function() {
					spotWeekDay += '' + $(this).val();
				});
				$('#spotWeekDay').val(spotWeekDay);

				/*var $spotStartTime = $('#spotStartTime');
				var $spotEndTime = $('#spotEndTime');*/

				// 기본값 설정
				/*var spotStartTime = $('#spotStartHour').val() + "" + $('#spotStartMinute').val() + "00";
				var spotEndTime = $('#spotEndHour').val() + "" + $('#spotEndMinute').val() + "59";
				$spotStartTime.val(spotStartTime);
				$spotEndTime.val(spotEndTime);*/
			}

			var discountRate = Math.round((sellerDiscountAmount + spotDiscountAmount) * 100 / salePrice);
			if (discountRate > discountLimitRate) {
				alert('할인 가능 금액은 판매금액의 ' + discountLimitRate + '% 까지만 설정이 가능합니다.\n판매금액 및 할인금액을 확인해 주세요.');
				Common.addNumberComma();
				$('#spotDiscountAmount').focus();
				return false;
			}

			// 최소/최대 구매수량
			var orderMinQuantity = $.trim($('#orderMinQuantity').val());
			var orderMaxQuantity = $.trim($('#orderMaxQuantity').val());
			if (orderMinQuantity != '' && orderMaxQuantity != ''
				&& Number(orderMinQuantity) > 0 && Number(orderMaxQuantity) > 0) {

				if (Number(orderMinQuantity) > Number(orderMaxQuantity)) {
					alert(Message.get("M00699"));	// 최대 구매 수량을 최소 구매 수량 보다 큰 값으로 입력해 주세요.
					$('#orderMaxQuantity').focus();
					return false;
				}
			}

			// 포인트 처리
			$('#item_point_area tr').each(function(){
				var pointType = $(this).find('input[type=radio]').eq(0).prop('checked') == true ? "1" : "2";
				$(this).find('input[name=pointType]').val(pointType);
			});

			if ($('input[name=sellerPointFlag]:checked').val() == 'Y') {
				if($("select[name=pointType]").val() == '1'){
					if($('input[name=point]').val() > 100){
						alert("${op:message('M00246')} 지급시 100%이상으로는 지급할 수 없습니다. 다시 입력하여 주십시요.");
						$('input[name=point]').focus();
						return false;
					}
				}
			}

			let itemOptionName1List = $('#item-options').find('input[name=optionName1]');
			let length = itemOptionName1List.length;
			if (length > 250) {
				alert("답례품 옵션 등록은 250개 까지 가능합니다.");
				return false;
			}

			// 상품 옵션인 경우 본 상품 재고 관리는 무제한으로 변경.
			var itemOptionFlag = $('input[name=itemOptionFlag]:checked').val();
			var itemOptionType = $('input[name=itemOptionType]:checked').val();
			if (itemOptionFlag == 'Y' && itemOptionType != 'T') {
				$('input[name=stockFlag]').eq(0).prop('checked', true);
				$('#trStockQuantity').hide();
				$('#stockQuantity').val('');
				setValidatorCondition();
			}

			$("#item-options #option-type-table").val(itemOptionType);
			$("#item-text-options #option-text-table").val('T');


			// 20260325 필수 추가정보
			// 필수 추가정보 옵션
			var itemTextOptionFlag = $('input[name=itemTextOptionFlag]:checked').val();
			if (itemTextOptionFlag == 'Y') {
				setValidatorCondition();
				var $itemOptions = $('#item-text-options-two tr');
				for (var i = 0; i < 3; i++) {
					var $tr = $itemOptions.eq(i);
					var dd = $tr.find('input[name=itemTextOptionTitle]').val();
					if(dd != '' && dd != undefined){
						if(dd.length > 30){
							alert("상품 필수 입력정보는 30글자 이내로만 작성할 수 있습니다. 다시 입력하여 주십시요.");
							$tr.find('input[name=itemTextOptionTitle]').focus();
							return false;
						}

						$('input[name=itemTextOptionTitle'+(i+1)+']').val(dd);
					}else{
						$('input[name=itemTextOptionTitle'+(i+1)+']').remove();
					}
				}
			}else{ // 필수 추가정보 옵션 사용 안함 시 빈값 들어가게 처리
				for (var i = 0; i < 3; i++) {
					$('input[name=itemTextOptionTitle'+(i+1)+']').remove();
				}
			}





			// 배송비 설정
			var $shippingType = $('input[name=shippingType]:checked');
			var $shippingInfo = $shippingType.closest('tr');
			var shipping = $shippingInfo.find('.opt-shipping').val();
			var shippingFreeAmount = $shippingInfo.find('.opt-shipping-free-amount').val();

			$('#shipping').val(shipping);
			$('#shippingFreeAmount').val(shippingFreeAmount);

			// 관련상품
			var relationItemDisplayType = $('input[name=relationItemDisplayType]:checked').val();
			if (relationItemDisplayType == 2) {
				if ($('input[name=relatedItemIds]').size() == 0) {
					alert(Message.get("M00080"));	// 관련상품을 추가해 주세요.
					$('#button_add_relation_item').focus();
					return false;
				}
			}

			if (!confirm('상품 정보를 저장하시겠습니까?')) {
				return false;
			}

			// 에디터 내용 검증 (내용 입력 여부 / 필터) - 필수 아님?
			//if ($.validator.validateEditor(editors, "detailContent") == false) return false;

			// 에디터 내용 설정.
			//Common.getEditorContent("detailContentTop");
			//Common.getEditorContent("detailContentTopMobile");
			Common.getEditorContent("detailContent");
			Common.getEditorContent("detailContentMobile");
			//Common.getEditorContent("useManual");
			//Common.getEditorContent("makeManual");

			//if ($.trim($('#useManual').val()).toLowerCase() == '<p>&nbsp;</p>') {
			//	$('#useManual').val('');
			//}

			//if ($.trim($('#makeManual').val()).toLowerCase() == '<p>&nbsp;</p>') {
			//	$('#makeManual').val('');
			//}

			Common.removeNumberComma();

			$(".spotFlag").removeAttr("disabled");

			// optionName1 중복시 배열수 고정을 위해 사용하지 않는 옵션상태테이블 삭제
			var itemOptionType = $('input[name=itemOptionType]:checked').val();
			if (itemOptionType == 'T') {
				// 테스트형
				$('.item-option-table').empty();
				$('.option-type.type-T input[name="optionType"]').eq(0).val('T');
			} else {
				// 그 외
				$('.option-type.type-T').empty();
			}

		}// formcheck end
	});

	var restockNoticeCount = '${fn:escapeXml(restockNoticeCount)}';
	if (restockNoticeCount > 0) {
	    $('.restock-button').removeClass('hide');
    } else {
        $('.restock-text').removeClass('hide');
    }

	$( window ).scroll(function() {

		//setHeight();
	});

	// 옵션명 수정 불가하도록 수정
	let itemOptionName1List = $('#item-options').find('input[name=optionName1]');
	let length1 = itemOptionName1List.length;
	for (let i = 0 ; i < length1 ; i++) {
		itemOptionName1List[i].setAttribute('readonly', true);
	}
	let itemOptionName2List = $('#item-options').find('input[name=optionName2]');
	let length2 = itemOptionName2List.length;
	for (let i = 0 ; i < length2 ; i++) {
		itemOptionName2List[i].setAttribute('readonly', true);
	}
	let itemOptionName3List = $('#item-options').find('input[name=optionName3]');
	let length3 = itemOptionName3List.length;
	for (let i = 0 ; i < length3 ; i++) {
		itemOptionName3List[i].setAttribute('readonly', true);
	}
	try {
		$('#itemOptionTitle1').attr('readonly', true);
	} catch(e) {
		console.log(e);
	}
	try {
		$('#itemOptionTitle2').attr('readonly', true);
	} catch(e) {
		console.log(e);
	}
	try {
		$('#itemOptionTitle3').attr('readonly', true);
	} catch(e) {
		console.log(e);
	}

	// Initially trigger the function
	updateImgAlt();



}); //function()end


// 스마트에디터 사진 용량제한
function picAppLoad() {

	setTimeout(async () => {
		let outerIframe = await document.querySelectorAll("#editor_frame")[0];
		let outerIframeDoc = await outerIframe.contentDocument || outerIframe.contentWindow.document;
		let innerIframe = await outerIframeDoc.querySelector("#smart_editor2_content iframe");
		let innerIframeDoc = await innerIframe.contentDocument || innerIframe.contentWindow.document;
		const watcher = await innerIframeDoc.body;

		if(!watcher) {
			return;
		}

		console.log(watcher);
		watcher.addEventListener('paste', function(){
			console.log('붙여넣기 발생~~');

			//클립보드에서 데이터 가져오기
			const clipboardData = event.clipboardData || window.clipboardData;

			// 가져온 데이터가 이미지인지 확인
// 			const isImage = clipboardData && clipboardData.items && clipboardData.items.length > 0 && clipboardData.items[0].type.includes('image');

			const isImage = Array.from(clipboardData.items).some(item => item.type.startsWith("image/"));

			const htmlData = clipboardData.getData("text/html");
			const hasImgTag = htmlData && htmlData.includes("<img");

			if (isImage || hasImgTag) {
				// 이미지 붙여넣기 방지
				event.preventDefault();
				alert('이미지는 복사 붙여넣기를 할수 없습니다.');

				// 이미지의 용량체크 추후에 용량 체크 필요시 사용
				/* const imageFile = clipboardData.items[0].getAsFile(); // 이미지 파일 확인
				const imageSizeInKB = imageFile.size / ( 1024 ); // 파일 크기 MB 환산
				console.log('image Size is :: ' + imageSizeInKB);
				const maxImageSizeInKB = 200;
				if(imageSizeInKB > maxImageSizeInKB) {
					event.preventDefault();
					alert('이미지 붙여넣기 크기는 ' + maxImageSizeInKB + 'KB를 초과할수 없습니다.');
					return;
				} */
			}

		});
	}, 500);

}


// 비회원 판매가격 이벤트 핸들러.
function initNonmemberPriceEvent() {
	showHideContent($('input[name=salePriceNonmemberFlag]:checked'));
	$('input[name=salePriceNonmemberFlag]').on('click', function() {
		showHideContent($(this));
		setValidatorCondition();
	});
}

// 사은품 이벤트 핸들러.
function initFreeGiftEvent() {
	showHideContent($('input[name=freeGiftFlag]:checked'));
	$('input[name=freeGiftFlag]').on('click', function() {
		showHideContent($(this));
		setValidatorCondition();
	});
}


// 상품 수수료 설정 이벤트 핸들러.
function initCommissionEvent() {
	setCommissionRate();

	$('input[name=commissionType]').on('click', function() {
		setCommissionRate();
		setValidatorCondition();
	});

	function setCommissionRate() {
		var commissionType = $('input[name=commissionType]:checked').val();
		var $commissionRate = $('#commissionRate');
        var $supplyPrice = $('#supplyPrice');

		if (commissionType == '1') {			// 입점업체 수수료
			$commissionRate.val($('#sellerCommissionRate').val());
			$commissionRate.prop('readonly', true);
			$commissionRate.closest("td").prev().show();
			$commissionRate.closest("td").show();

			$supplyPrice.val(0);
            $supplyPrice.prop('readonly', true);
			$supplyPrice.closest("td").prev().hide();
			$supplyPrice.closest("td").hide();
		} else if (commissionType == '2') {		// 상품별 수수료
			$commissionRate.val('${fn:escapeXml(item.commissionRate)}');
			$commissionRate.prop('readonly', false);
			$commissionRate.closest("td").prev().show();
			$commissionRate.closest("td").show();

			$supplyPrice.val(0);
            $supplyPrice.prop('readonly', true);
			$supplyPrice.closest("td").prev().hide();
			$supplyPrice.closest("td").hide();
		} else if (commissionType == '3') {     // 공급가 기준
            $commissionRate.val(0);
            $commissionRate.prop('readonly', true);
			$commissionRate.closest("td").prev().hide();
			$commissionRate.closest("td").hide();

			$supplyPrice.prop('readonly', false);
            if (${fn:escapeXml(requestContext.sellerPage)} == 'true') {
                $supplyPrice.prop('readonly', true);
			}
			$supplyPrice.closest("td").prev().show();
			$supplyPrice.closest("td").show();
        }
	}
}


// 상품 옵션 이벤트 핸들러.
function initItemOptionEvent() {

	// 상품옵션
	showHideItemOption();


	$('input[name=itemOptionFlag]').on('click', function() {
		if ($(this).val() == 'N') {
			if (!confirm('상품옵션을 사용안함으로 설정하는 경우 현재 설정된 옵션 설정이 삭제됩니다. 변경하시겠습니까?')) {
				return false;
			}
			clearItemOptions();
		}
		showHideItemOption();
		setValidatorCondition();
        clearItemOptions();
	});



	// 옵션 타입 선택 이벤트.
	$('input[name=itemOptionType]').on('click', function() {
		if (!confirm('옵션 형태를 변경하는 경우 현재 설정된 옵션 설정이 삭제됩니다. 옵션타입을 변경하시겠습니까?')) {
			return false;
		}
		//$('.option-step.step2').hide();
		$('.option-type').hide();

		clearItemOptions();

		showHideItemOption();
		setValidatorCondition();
	});

	// S옵션 입력 도우미 - 항목추가.
	$('.add-option-input').on('click', function() {
		var addHtml = $('#s-option-input-wrap').find('tr').eq(0).parentHtml();
		$('#s-option-input-wrap').append(addHtml);
	});

	// S옵션 입력 도우미 - 항목삭제.
	$('#s-option-input-wrap').on('click', ' .delete_option_input', function(e) {
		e.preventDefault();
		if ($('#s-option-input-wrap tr').size() > 1) {
			$(this).closest('tr').remove();
		} else {
			$(this).closest('tr').find('input').val('');
		}
	});


	// 옵션 적용 (옵션 생성)
	$('.item_option_board .make-item-option').on('click', function(e) {
		e.preventDefault();

		var optionType = $('input[name=itemOptionType]:checked').val();

		if (optionType == 'S') {
			// 1. 입력 검증.
			var $target = $('#s-option-input-wrap');
			var errorCount = validOptionInput($target);

			if (errorCount > 0) {
				return;
			}

			// 2. 옵션 값 생성.
			var options = [];
			var optionIndex = 0;
			$target.find('tr').each(function() {
				var option1 = $.trim($(this).find('input').eq(0).val());
				var option2 = $.trim($(this).find('input').eq(1).val());

				var optArray = option2.split(',');

				for (var i = 0; i < optArray.length; i++) {
					var opt2 = $.trim(optArray[i]);
					if (opt2 != '') {
						options[optionIndex] = {'optionType': 'S', 'optionName1': option1, 'optionName2': opt2, 'optionName3': ''};
						optionIndex++;
					}
				}
			});

			$('#itemOptionTitle1').val('옵션 제목');
			$('#itemOptionTitle2').val('옵션 내용');

			// 3. 옵션 적용.
			makeItemOptions(options);

		} else if (optionType == 'S2') {
			// 1. 입력 검증.
			var $target = $('#s2-option-input-wrap');
			var errorCount = validOptionInput($target);

			if (errorCount > 0) {
				return;
			}

			// 2. 옵션 값 생성.
			var options = [];
			var optionIndex = 0;

			var optionTitle1 = $.trim($target.find('tr').eq(0).find('input').eq(0).val());
			var optionTitle2 = $.trim($target.find('tr').eq(1).find('input').eq(0).val());

			var option1 = $.trim($target.find('tr').eq(0).find('input').eq(1).val());
			var option2 = $.trim($target.find('tr').eq(1).find('input').eq(1).val());

			var opt1Array = option1.split(',');
			var opt2Array = option2.split(',');

			for (var i = 0; i < opt1Array.length; i++) {
				for (var j = 0; j < opt2Array.length; j++) {
					var opt1 = $.trim(opt1Array[i]);
					var opt2 = $.trim(opt2Array[j]);

					if (opt1 != '' && opt2 != '') {
						options[optionIndex] = {'optionType': 'S2', 'optionName1': opt1, 'optionName2': opt2, 'optionName3': ''};
						optionIndex++;
					}
				}
			}

			$('#itemOptionTitle1').val(optionTitle1);
			$('#itemOptionTitle2').val(optionTitle2);

			// 3. 옵션 적용.
			makeItemOptions(options);

		} else if (optionType == 'S3') {
			// 1. 입력 검증.
			var $target = $('#s3-option-input-wrap');
			var errorCount = validOptionInput($target);

			if (errorCount > 0) {
				return;
			}

			// 2. 옵션 값 생성.
			var options = [];
			var optionIndex = 0;

			var optionTitle1 = $.trim($target.find('tr').eq(0).find('input').eq(0).val());
			var optionTitle2 = $.trim($target.find('tr').eq(1).find('input').eq(0).val());
			var optionTitle3 = $.trim($target.find('tr').eq(2).find('input').eq(0).val());

			var option1 = $.trim($target.find('tr').eq(0).find('input').eq(1).val());
			var option2 = $.trim($target.find('tr').eq(1).find('input').eq(1).val());
			var option3 = $.trim($target.find('tr').eq(2).find('input').eq(1).val());

			var opt1Array = option1.split(',');
			var opt2Array = option2.split(',');
			var opt3Array = option3.split(',');

			for (var i = 0; i < opt1Array.length; i++) {
				for (var j = 0; j < opt2Array.length; j++) {
					for (var k = 0; k < opt3Array.length; k++) {
						var opt1 = $.trim(opt1Array[i]);
						var opt2 = $.trim(opt2Array[j]);
						var opt3 = $.trim(opt3Array[k]);

						if (opt1 != '' && opt2 != '' && opt3 != '') {
							options[optionIndex] = {'optionType': 'S3', 'optionName1': opt1, 'optionName2': opt2, 'optionName3': opt3};
							optionIndex++;
						}
					}
				}
			}

			$('#itemOptionTitle1').val(optionTitle1);
			$('#itemOptionTitle2').val(optionTitle2);
			$('#itemOptionTitle3').val(optionTitle3);

			// 3. 옵션 적용.
			makeItemOptions(options);
		}

		$("#sActiveOption").show();

	});

	// 상품옵션 - 재고 연동 여부 변경 시
	$('#item-options').on('change', 'select[name=optionStockFlag]', function() {
		var optionStockFlag = $(this).val();
		var $optionStockQuantity = $(this).closest('tr').find('input[name=optionStockQuantity]');
		if (optionStockFlag == 'Y') {
			$optionStockQuantity.prop('readonly', false).addClass('required-item-option');

		} else {
			$optionStockQuantity.val('').prop('readonly', true).removeClass('required-item-option');

		}
	});

	// 상품옵션 - 옵션추가
	$('.add-item-option').on('click', function() {
		var $templateOption = $('#item-options tr').eq(0).find('input[name=optionStockQuantity]');
		var isReadonly = $templateOption.prop('readonly');
		$templateOption.prop('readonly', true);

		var optionHtml = $('#item-options tr').eq(0).parentHtml();
		$('#item-options').append(optionHtml);

		$templateOption.prop('readonly', isReadonly);

		var $newItem = $('#item-options tr:last-child');
		resetItemOption($newItem)
	});



	// 이미지 설명 로우 추가
	$('.add-item-option2').on('click', function() {
		let size = $('#item-options2 tr').size();
	    var newHtml = '<tr>' +
	    '<td>' +
	    '<input type="text" id="imageExplainIndex" name="itemImageExplain[' + size + '].imagesIndexes" maxlength="200" class="" title="이미지 설명 순서" value="'+ size +'" readonly/>' +
	    '</td>' +
	    '<td>' +
	    '<input type="text" id="imageExplainValue" name="itemImageExplain[' + size + '].imagesValues" maxlength="200"  title="이미지 설명" value="" />' +
	    '</td>' +
	    '<td><a href="javascript:void(0);" class="btn btn-dark-gray btn-sm delete-item-option2" data-index="' + size + '">삭제</a></td>'+
	    '</tr>';

	    var newHtml2 = '<tr>' +
	    '<td>' +
	    '<input type="text" id="imageExplainIndex" name="itemImageExplain[' + size + '].imagesIndexes" class="" title="이미지 설명 순서" value="'+ size +'" readonly/>' +
	    '</td>' +
	    '<td>' +
	    /* '<input type="text" id="imageExplainValue" name="itemImageExplain[' + size + '].imagesValues" maxlength="200"  title="이미지 설명" value="" />' + */
	    '<textarea id="imageExplainValue" name="itemImageExplain[' + size + '].imagesValues"   title="이미지 설명" value="" ></textarea>' +
	    '</td>' +
	    '<td><a href="javascript:void(0);" class="btn btn-dark-gray btn-sm delete-item-option2" data-index="' + size + '">삭제</a></td>'+
	    '</tr>';
        $('#item-options2').append(newHtml2);
		//var $newItem = $('#item-options2 tr:last-child');
		//resetItemImageExplain($newItem)
		updateRowIndex();
	});


	//접근성 이미지 설명 로우 추가 혹은 삭제
	function updateRowIndex() {
		let size = $('#item-options2 tr').size();
		let index = 0;
		for(i = 0; i < size; i++) {
			index = i+1;
			$('#item-options2 tr:eq(' + i + ')').find('textarea[id="imageExplainValue"]').attr('name', 'itemImageExplain[' + (index - 1) + '].imagesValues');
			$('#item-options2 tr:eq(' + i + ')').find('input[id="imageExplainIndex"]').attr('name', 'itemImageExplain[' + (index - 1) + '].imagesIndexes');
			$('#item-options2 tr:eq(' + i + ')').find('input[id="imageExplainIndex"]').val(index);
		}

		/* $('#item-options2 tr').each(function(index) {
	        $(this).find('input[name^="itemImageExplain["]').attr('name', 'itemImageExplain[' + index + '].imagesIndexes');
	        $(this).find('input[id="imageExplainIndex"]').val(index);
	    }); */

	}


	// 상품옵션 - 옵션삭제
	$('#item-options').on('click', '.delete-item-option', function(e) {
		e.preventDefault();

		if ($('#item-options tr').size() > 1) {
			$(this).closest('tr').remove();
		} else {
			resetItemOption($(this).closest('tr'));
			$("#sActiveOption").hide();
		}
	});

	// 이미지 설명 로우 삭제
	$('#item-options2').on('click', '.delete-item-option2', function(e) {
		e.preventDefault();

		if ($('#item-options2 tr').size() > 0) {
			$(this).closest('tr').remove();
		    // index 값 조정
		    //currentIndex = Math.max(currentIndex - 1, 0);
		} else {
			//resetItemOption($(this).closest('tr'));
			//$("#sActiveOption").hide();
		}
		 updateRowIndex();
	});

	// 상품 텍스트 옵션 - 옵션추가
	$('.add-item-text-option').on('click', function() {
		var optionHtml = $('#item-text-options tr').eq(0).parentHtml();
		$('#item-text-options').append(optionHtml);

		var $newItem = $('#item-text-options tr:last-child');
		resetTextOption($newItem, 'add');
	});

	// 상품 텍스트 옵션 - 옵션삭제
	$('#item-text-options').on('click', '.delete-item-text-option', function(e) {
		e.preventDefault();
		resetTextOption($(this));
	});

	function showHideItemOption() {
		var itemOptionFlag = $('input[name=itemOptionFlag]:checked').val();
		var itemOptionType = $('input[name=itemOptionType]:checked').val();
		$('.item_option_board .option-step.step1').hide();
		$('.item_option_board .option-step.step2').hide();
		$('.item_option_board .option-type').hide();
		$('.item_option_board .option-S3').hide();
		$('.item_option_board .option-S3').find('input[name=optionName3]').removeClass('required_item_option');

		if (itemOptionFlag == 'N' || itemOptionType === undefined) {
			return;
		}
		$('.option-step.step1').show();
		if (itemOptionType == 'S') {
			$('#itemOptionTitle1').val('옵션 제목');
			$('#itemOptionTitle2').val('옵션 내용');
			$('.item-option-table').show();

		} else if (itemOptionType == 'S2') {

			$('.item-option-table').show();

		} else if (itemOptionType == 'S3') {
			$('.item-option-table').show();
			$('.option-S3').show();
			$('.option-S3').find('input[name=optionName3]').addClass('required_item_option');


		} else if (itemOptionType == 'T') {
			$('.item_option_board .option-step.step2').show();
			$('.item_option_board .item-option-table').hide();
			$('.item_option_board .option-type.type-T').show();

		}

		if (itemOptionType != 'T') {
			$('.item_option_board .option-step.step2').show();
			$('.item_option_board .option-type.type-' + itemOptionType).show();
		}

		// 옵션 상품 재고 수량 / readonly
		$('select[name=optionStockFlag]').each(function() {
			var optionStockFlag = $(this).val();
			var $optionStockQuantity = $(this).closest('tr').find('input[name=optionStockQuantity]');
			if (optionStockFlag == 'Y') {
				$optionStockQuantity.prop('readonly', false).addClass('required-item-option');
			} else {
				$optionStockQuantity.val('').prop('readonly', true).removeClass('required-item-option');

			}
		});
	}
	// 옵션 입력 도우미 - 입력 검증.
	function validOptionInput($target) {
		var errorCount = 0;

		$target.find('input').each(function(i) {
			var optionValue = $.trim($(this).val());
			var optionTitle = $(this).attr('title');

			if (optionValue == '') {
				errorCount++;
				alert(optionTitle + '을 입력해 주세요.');
				$(this).focus();
				return false;
			}
		});

		return errorCount;
	}

	// 상품 옵션 clear {
	function clearItemOptions() {
		// S, S2, S3
		var optionHtml = $('#item-options tr').eq(0).parentHtml();
		var $itemOptions = $('#item-options');

		$itemOptions.find('tr').remove();
		$itemOptions.append(optionHtml);
		$('#itemOptionTitle1, #itemOptionTitle2, #itemOptionTitle3').val('');

		resetItemOption($itemOptions);

		// 옵션 명 수정하지 못하도록 수정
		let itemOptionName1List = $('#item-options').find('input[name=optionName1]');
		let length1 = itemOptionName1List.length;
		for (let i = 0 ; i < length1 ; i++) {
			itemOptionName1List[i].setAttribute('readonly', true);
		}
		let itemOptionName2List = $('#item-options').find('input[name=optionName2]');
		let length2 = itemOptionName2List.length;
		for (let i = 0 ; i < length2 ; i++) {
			itemOptionName2List[i].setAttribute('readonly', true);
		}
		let itemOptionName3List = $('#item-options').find('input[name=optionName3]');
		let length3 = itemOptionName3List.length;
		for (let i = 0 ; i < length3 ; i++) {
			itemOptionName3List[i].setAttribute('readonly', true);
		}
		$("#sActiveOption").hide();


		// T
		$('#item-text-options tr').each(function(){
			resetTextOption($(this));
		});
	}


	// 옵션 클리어
	function resetItemOption($target) {
        $target.find('input').val('');
		$target.find('input[name="optionStockQuantity"]').prop('readonly', true).removeClass('required-item-option');
		$target.find('select[name=optionStockFlag]').val('N');
		$target.find('select[name=optionSoldOutFlag]').val('N');
		$target.find('select[name=optionDisplayFlag]').val('Y');

		$target.find('input[name="optionType"]').val($('input[name=itemOptionType]:checked').val());
		$target.find('input[name="optionDisplayType"]').val('select');
	}

	//이미지 설명 내용 클리어
/* 	function resetItemImageExplain($target) {
        $target.find('input[id="imageExplainValue"]').val('');
	} */

	// 텍스트 옵션 클리어
	function resetTextOption($selector, type) {
		if ($('#item-text-options tr').size() > 1 && type != 'add') {
			$selector.closest('tr').remove();
		} else {
			$selector.closest('tr').find('input').val('');
			$selector.closest('tr').find('select').each(function() {
				$selector.find('option:eq(0)').prop('selected', true);
			});
			$selector.closest('tr').find('input[name="optionType"]').val('T');
			$selector.closest('tr').find('input[name="optionDisplayType"]').val('text');
		}
	}

	// 상품 옵션 생성.
	function makeItemOptions(options) {
		//$('#item-options tr').eq(0).find('input[name=optionStockQuantity]').prop('readonly', true);
		var optionHtml = $('#item-options tr').eq(0).parentHtml();

		var makeHtml = '';

		for (var i = 0; i < options.length; i++) {
			makeHtml += optionHtml;
		}


		var $itemOptions = $('#item-options');
		$itemOptions.find('tr').remove();
		$itemOptions.append(makeHtml);


		for (var i = 0; i < options.length; i++) {
			var $tr = $itemOptions.find('tr').eq(i);
			$tr.find('input[type=text]').val('');

			resetItemOption($tr);

			$tr.find('input[name=optionType]').val(options[i].optionType);
			$tr.find('input[name=optionName1]').val(options[i].optionName1);
			$tr.find('input[name=optionName2]').val(options[i].optionName2);
			$tr.find('input[name=optionName3]').val(options[i].optionName3);

		}

	}

}


// 상품 필수 추가정보 옵션 이벤트 핸들러.  // 20260325 필수 추가정보
function initItemTextOptionEvent() {

	// 상품옵션
	showHideItemTextOption();


	$('input[name=itemTextOptionFlag]').on('click', function() {
		if ($(this).val() == 'N') {
			if (!confirm('필수 추가정보를 사용안함으로 설정하는 경우 현재 설정된 필수 추가정보 설정이 삭제됩니다. 변경하시겠습니까?')) {
				return false;
			}
			clearItemTextOptions();
		}
		showHideItemTextOption();
		setValidatorCondition();
        clearItemTextOptions();
	});


	$('#text-option-input').on('input', function(e) {
//		$(this).val($(this).val().replace(/\|\||:/g,''));			// ||랑 : 막기
		$(this).val($(this).val().replace(/[|:<>]/g,''));				// |랑 : 막기

	});

	// 옵션 적용 (옵션 생성)
	$('.item_text_option_board .make-item-option').on('click', function(e) {

		e.preventDefault();
		// 1. 입력 검증.
		var $target = $('#text-option-input-wrap');
		var errorCount = validTextOptionInput($target);
		if (errorCount > 0) {
			return;
		}

		var options = '';						// 사용자가 입력한 필수 입력정보
		var optArray = [];						// ,로 분리한 필수 입력정보 목록
		var useOptArray = [];					//  값 체크 완료되어 실제 화면에 넣을값 목록
		var useOptCnt = 0;						//  값 체크 완료되어 실제 화면에 넣을값 목록

		// 2. 옵션 값 생성.
		options = $target.find('tr').find('input').val();
		optArray = options.split(',');
		for (var i = 0; i < optArray.length; i++) {
			if(i > 2){
				$target.find('tr').find('input').val(useOptArray.join(', '));
				alert('필수 추가정보는 3건까지 등록 가능합니다.');
				break;
			}

			var opt = (optArray[i]).trim();
			if (opt != '') {
				useOptArray[useOptCnt] = opt;
				useOptCnt++;
			}
		}

		if(useOptCnt < 1){					// 제대로 값이 들어있는 옵션이 없을 경우 입력란 비우고 alert 처리
			$target.find('input').val('');
			validTextOptionInput($target);
			return;
		}

		makeItemTextOptions(useOptArray);
	});

	// 상품옵션 - 옵션삭제
	$('#item-options').on('click', '.delete-item-option', function(e) {
		e.preventDefault();

		if ($('#item-options tr').size() > 1) {
			$(this).closest('tr').remove();
		} else {
			resetItemOption($(this).closest('tr'));
			$("#sActiveOption").hide();
		}
	});

	// 상품 텍스트 옵션 - 옵션삭제
	$('#item-text-options-two').on('click', '.delete-item-text-option', function(e) {
		e.preventDefault();
		resetTextOption($(this));
	});

	function showHideItemTextOption() {
		var itemTextOptionFlag = $('input[name=itemTextOptionFlag]:checked').val();

		$('.item_text_option_board .option-step.step2').hide();
		$('.item_text_option_board .item_text_option_input_box').hide();
		if (itemTextOptionFlag == 'N') {
			return;
		}

		$('.item_text_option_board .option-step.step2').show();
		//$('.item_text_option_board .item_text_option_list_box').hide();
		$('.item_text_option_board .item_text_option_input_box').show();

		$('.item_text_option_board .item_text_option_tbl').removeClass("hidden")
		$(".item_text_option_board .item_text_option_tbl").show();
	}

	// 옵션 입력 도우미 - 입력 검증.
	function validTextOptionInput($target) {
		var errorCount = 0;

		$target.find('input').each(function(i) {
			var optionValue = $.trim($(this).val());
			var optionTitle = $(this).attr('title');

			if (optionValue == '') {
				errorCount++;
				alert(optionTitle + '을 입력해 주세요.');
				$(this).focus();
				return false;
			}
		});

		return errorCount;
	}

	// 상품 텍스트 옵션 clear {
	function clearItemTextOptions() {
		$('#item-text-options-two tr').each(function(){
			resetTextOption($(this));
		});
		$(".item_text_option_board .item_text_option_tbl ").hide();
	}


	// 옵션 클리어
	function resetItemOption($target) {
        $target.find('input').val('');
	}

	// 텍스트 옵션 클리어
	function resetTextOption($selector) {
		if ($('#item-text-options-two tr').size() > 1) {
			$selector.closest('tr').remove();
		} else {
			$selector.closest('tr').find('input').val('');
			$selector.closest('tr').find('select').each(function() {
				$selector.find('option:eq(0)').prop('selected', true);
			});

		}
	}

	// 상품 옵션 생성.
	function makeItemTextOptions(options) {
		var $itemOptions = $('#item-text-options-two');
		var optionHtml = $('#item-text-options-two tr').eq(0).parentHtml();
		var makeHtml = '';

		for (var i = 0; i < options.length; i++) {
			makeHtml += optionHtml;
		}

		$itemOptions.find('tr').remove();
		$itemOptions.append(makeHtml);

		for (var i = 0; i < options.length; i++) {
			var $tr = $itemOptions.find('tr').eq(i);
			$tr.find('input[name=itemTextOptionTitle]').val(options[i]);
		}

		$('.item_text_option_board .item_text_option_tbl').removeClass("hidden")
		$(".item_text_option_board .item_text_option_tbl").show();
	}

}


// 추가 상품 이벤트 핸들러.
function initItemAdditionEvent() {

	setItemAddition();

	// 추가 구성 사용 여부 클릭 이벤트.
	$('input[name=itemAdditionFlag]').on('click', function() {
		setItemAddition();
	});

	// 추가구성 상품 - 상품추가
	$('.add-item-addition').on('click', function() {
		var $itemAdditions = $('#item-additions');

		var optionHtml = $itemAdditions.find('tr').eq(0).parentHtml();
		$itemAdditions.append(optionHtml);


		var $newItem = $itemAdditions.find('tr:last-child');
		$newItem.find('input').val('');
		$newItem.find('input[name=additionStockQuantity]').prop('readonly', true).removeClass('required-item-addition');
		$newItem.find('select').each(function() {
			$(this).find('option:eq(0)').prop('selected', true);
		});

	});

	// 추가구성 상품 - 상품삭제
	$('#item-additions').on('click', '.delete-item-addition', function(e) {
		e.preventDefault();

		if ($('#item-additions tr').size() > 1) {
			$(this).closest('tr').remove();
		} else {
			$(this).closest('tr').find('input').val('');
			$(this).closest('tr').find('select').each(function() {
				$(this).find('option:eq(0)').prop('selected', true);
			});
		}
	});

	// 재고 연동여부 readonly
	$('#item-additions').on('change', 'select[name=additionStockFlag]', function(e) {
		handleAdditionStockFlagEvent($(this));
	});


	// 재고 연동 여부에 따른 재고수량 / readonly
	function handleAdditionStockFlagEvent($target) {
		var stockFlag = $target.val();
		var $stockQuantity = $target.closest('tr').find('input[name=additionStockQuantity]');
		if (stockFlag == 'Y') {
			$stockQuantity.prop('readonly', false).addClass('required-item-addition');

		} else {
			$stockQuantity.val('').prop('readonly', true).removeClass('required-item-addition');

		}
	}

	// 추가 구성 상품 init
	function setItemAddition() {
		var itemAdditionFlag = $('input[name=itemAdditionFlag]:checked').val();
		var $itemAdditionWrap = $('.item-addition-wrap');

		if (itemAdditionFlag == 'Y') {
			$itemAdditionWrap.show();
		} else {
			$itemAdditionWrap.hide();
		}

		// 옵션 상품 재고 수량 / readonly
		$('select[name=additionStockFlag]').each(function() {
			handleAdditionStockFlagEvent($(this));
		});

		setValidatorCondition();
	}
}

// 배송 정보 설정 이벤트 핸들러.
function initShippingEvent() {

	// 배송구분 설정
	setDeliveryType();

	// 배송비 종류 선택 설정.
	setShippingType();

	// 배송비 종류 노출 설정.
	showHideShippingType();

	// 공급사 선택 이벤트.
	$('#sellerId').on('change', function() {
		// 배송구분 설정
		setDeliveryType();
		if (!isHqSeller()) {
			$('#deliveryType').val('2'); // 업체배송
		}

		// 판매자 정보 / 출고지 / 반송지 정보 조회
		setShipmentInformation();

		// 반품/교환 종류 초기화
		clearShipmentReturnInfo();
	});

	// 배송구분 선택 이벤트
	$('#deliveryType').on('change', function() {
		// 배송비 종류 초기화
		setShipmentInformation();
	});

	// 배송구분 선택 이벤트
	$('#shipmentReturnType').on('change', function() {
		// 반품/교환 종류 초기화
		clearShipmentReturnInfo();
	});

	// 배송구분 설정
	function setDeliveryType() {
		if (isHqSeller()) {
			$('#deliveryType option').eq(2).prop('disabled', true);		// 업체 배송 disabled
			$('#shipmentReturnType option').eq(1).prop('disabled', false);		// 본사반품 O
			$('#shipmentReturnType option').eq(2).prop('disabled', true);		// 업체 배송 disabled
			$('#deliveryType').val('1'); // 본사배송
			$('#shipmentReturnType').val('1'); // 업체반품
		} else {
			$('#deliveryType option').eq(2).prop('disabled', false);
			$('#shipmentReturnType option').eq(1).prop('disabled', true);		// 본사반품 X
			$('#shipmentReturnType option').eq(2).prop('disabled', false);
			$('#shipmentReturnType').val('2'); // 업체반품
		}
	}

	function isHqSeller() {
		var isHqSeller = false;
		if ($('#sellerId').data('isHqSeller') == undefined) {
			isHqSeller = $('#sellerId option:selected').data('isHqSeller') == 'Y' ? true : false;
		} else {
			isHqSeller = $('#sellerId').data('isHqSeller') == 'Y' ? true : false;
		}
		return isHqSeller;
	}


	// 배송정보 설정.
	function setShipmentInformation() {

		var deliveryType = $('#deliveryType').val();
		var sellerId = $('#sellerId').val();


		// 1. 배송비 종류 노출 설정.
		showHideShippingType();

		// 2. 배송비 설정 정보 초기화
		$('.shipping-option input[name=shippingType]').prop('checked', false);
		$('#shipmentGroupCode').val('');

		clearShipmentInfo();
		clearShipmentReturnInfo();

		if (sellerId == '' || (sellerId == '' && deliveryType == '2')) { // 업체배송인데 공급사가 없는 경우
			clearSellerInfo();
			return;
		}

		if (deliveryType == '1') {
			sellerId = '${fn:escapeXml(shopContext.hqSellerId)}';
		}

		var url = '${isSellerPage?"/seller":"/opmanager"}/item/seller-info/' + sellerId;
		$.post(url, {}, function(response) {
			Common.responseHandler(response, function(response) {
				var shipment = response.data.shipment;
				var shipmentReturn = response.data.shipmentReturn;
				var seller = response.data.seller;

				if (seller != null) {
					//$('#vnCode').val(seller.vnCode);

					var shipping = Common.numberFormat(seller.shipping);
					var $shippingType2 = $('.shipping-type-2');
					$shippingType2.find('.opt-shipping-text').text(shipping);
					$shippingType2.find('.opt-shipping').val(seller.shipping);
					$shippingType2.find('.opt-shipping-free-amount').val(seller.shippingFreeAmount);
					$shippingType2.find('.opt-shipping-extra-charge1').val(seller.shippingExtraCharge1);
					$shippingType2.find('.opt-shipping-extra-charge2').val(seller.shippingExtraCharge2);
					$shippingType2.find('.opt-shipping-free-amount-text').text(Common.numberFormat(seller.shippingFreeAmount));
					$('.seller-empty').hide();
					$('.seller-info').show();

					// 수수료
					$('#sellerCommissionRate').val(seller.commissionRate);
					// if ($('input[name=commissionType]').eq(0).prop('checked')) {
                    var commissionType = $('input[name=commissionType]:checked').val();
					if (commissionType == '1') {     // 입점업체 수수료인 경우
						$('#commissionRate').val(seller.commissionRate);
					}

					// mdid 자동
					<c:if test="${isSellerPage && item.itemId == 0}"> <%-- 관리자 상품 등록인 경우에만 --%>
						$('#mdId').val(seller.mdId);
					</c:if>

				} else {
					clearSellerInfo();
				}
				if (shipment != null) {
					var shipping = Common.numberFormat(shipment.shipping);
					var $shippingType3 = $('.shipping-type-3');
					$shippingType3.find('.opt-shipping-text').text(shipping);
					$shippingType3.find('.opt-shipping').val(shipment.shipping);
					$shippingType3.find('.opt-shipping-free-amount').val(shipment.shippingFreeAmount);
					$shippingType3.find('.opt-shipping-extra-charge1').val(shipment.shippingExtraCharge1);
					$shippingType3.find('.opt-shipping-extra-charge2').val(shipment.shippingExtraCharge2);
					$shippingType3.find('.opt-shipping-free-amount-text').text(Common.numberFormat(shipment.shippingFreeAmount));


					$('#shipmentId').val(shipment.shipmentId);
					$('#shipmentAddress').val(shipment.fullAddress);
					$('#shipmentGroupCode').val(shipment.shipmentGroupCode);
					$('.shipment-empty').hide();
					$('.shipment-info').show();

				} else {
					clearShipmentInfo();
				}

				if (shipmentReturn != null) {

					$('#shipmentReturnId').val(shipmentReturn.shipmentReturnId);
					$('#shipmentReturnAddress').val(shipmentReturn.shipmentReturnAddress);

				} else {
					clearShipmentReturnInfo();
				}
			});
		});

	}



	// 택배사 선택
	$('#deliveryCompanyId').on('change', function() {
		$('#deliveryCompanyName').val($(this).find('option:selected').text());
	});

	// 배송비 설정.
	$('input[name=shippingType]').on('click', function() {
		setShippingType();
		setValidatorCondition();
	});

	// 출고지/배송비 변경 팝업.
	$('.change-shipment-address').on('click', function(e) {
		e.preventDefault();
		var popupUrl = '${fn:escapeXml(requestContext.managerUri)}/shipment/list-popup';


		var $deliveryType = $('#deliveryType');
		if ($deliveryType.val() == '') {
			alert('배송구분을 선택해 주세요.');
			$deliveryType.focus();
			return;
		} else if ($deliveryType.val() == '2') {
			var $sellerId = $('#sellerId');
			if ($sellerId.val() == '') {
				alert('판매자(공급사)를 선택해 주세요.');
				return;
			}

			popupUrl = '${fn:escapeXml(requestContext.managerUri)}/shipment/list-popup/' + $sellerId.val();
		}

		Common.popup(popupUrl, 'shipment_popup', 980, 750, 1);
	});

	// 교환반품 주소 변경 팝업.
	$('.change-shipment-return-address').on('click', function(e) {
		e.preventDefault();
		var popupUrl = '${fn:escapeXml(requestContext.managerUri)}/shipment-return/list-popup';


		var $shipmentReturnType = $('#shipmentReturnType');
		if ($shipmentReturnType.val() == '') {
			alert('반품/교환구분을 선택해 주세요.');
			$shipmentReturnType.focus();
			return;
		} else if ($shipmentReturnType.val() == '2') {
			var $sellerId = $('#sellerId');
			if ($sellerId.val() == '') {
				alert('판매자(공급사)를 선택해 주세요.');
				return;
			}

			popupUrl = '${fn:escapeXml(requestContext.managerUri)}/shipment-return/list-popup/' + $sellerId.val();
		}
		Common.popup(popupUrl, 'shipment_return', 980, 750, 1);
	});

	// 배송비 종류 노출 설정.
	function showHideShippingType() {

		if ($('#deliveryType').val() == '2') {
			$('.shipping-type-2').show();
		} else {
			$('.shipping-type-2').hide();
		}
	}


	// 배송비 종류 선택 처리.
	function setShippingType() {

		var dbShippingType = '${fn:escapeXml(item.shippingType)}';
		var $shippingType = $('input[name=shippingType]:checked');
		var shippingType = $shippingType.val();

		if (shippingType == '1') {

		} else if (shippingType == '3') {
			if ($('#shipmentId').val() == 0) {
				alert('출고지를 선택해 주십시오.');
				$('.change-shipment-address').eq(0).focus();
				return false;
			}
		}

		$('.shipping-option tr:gt(3)').find('.opt-shipping').val('').prop('readonly', true);
		$('.shipping-option tr:eq(4)').find('.opt-shipping-free-amount').val('').prop('readonly', true);

		if (Number(shippingType) == 5) {
			$('#shippingItemCount').prop('readonly', false);
		} else {
			$('#shippingItemCount').prop('readonly', true);
		}


		if (Number(shippingType) >= 4) {
			$shippingType.closest('tr').find('.opt-shipping, .opt-shipping-free-amount').prop('readonly', false);



			if (dbShippingType == shippingType) {
				$shippingType.closest('tr').find('.opt-shipping').val($('#shipping').val());

				if (shippingType == 4) {
					$shippingType.closest('tr').find('.opt-shipping-free-amount').val($('#shippingFreeAmount').val());
				}
			}
		}

		// 제주/도서산간 추가 배송비
		if (shippingType == '2' || shippingType == '3') {
			$('#shippingExtraCharge1, #shippingExtraCharge2').prop('readonly', true);
			var shippingExtraCharge1 = $shippingType.closest('tr').find('.opt-shipping-extra-charge1').val();
			var shippingExtraCharge2 = $shippingType.closest('tr').find('.opt-shipping-extra-charge2').val();

			$('#shippingExtraCharge1').val(Common.numberFormat(shippingExtraCharge1));
			$('#shippingExtraCharge2').val(Common.numberFormat(shippingExtraCharge2));

		} else {
			$('#shippingExtraCharge1, #shippingExtraCharge2').prop('readonly', false);
		}
	}
}

// 상품정보고시 관련 이벤트 핸들러.
function initItemNoticeEvent() {
	var DEFAULT_MESSAGE = '상세정보 별도표기';

	setItemNotice();

	// 상품유형 선택 시.
	$('#itemNoticeCode').on('change', function() {
		setItemNotice();
	});

	$('#item_info_area').on('click', '.check-all-item-notice', function() {
		var $itemInfoDescriptions = $(this).closest('table').find('input[name=itemInfoDescriptions]');
		var $itemNoticeCheckboxies = $(this).closest('table').find('.check-item-notice');

		if ($(this).prop('checked')) {
			$itemInfoDescriptions.prop('readonly', true).val(DEFAULT_MESSAGE);
			$itemNoticeCheckboxies.prop('checked', true);
		} else {
			$itemInfoDescriptions.prop('readonly', false).val('');
			$itemNoticeCheckboxies.prop('checked', false);
		}
	});

	$('#item_info_area').on('click', '.check-item-notice', function() {
		var $itemInfoDescription = $(this).closest('tr').find('input[name=itemInfoDescriptions]');
		if ($(this).prop('checked')) {
			$itemInfoDescription.prop('readonly', true).val(DEFAULT_MESSAGE);
		} else {
			$itemInfoDescription.prop('readonly', false).val('');
		}
	});

	function setItemNotice() {
		var itemNoticeCode = $('#itemNoticeCode').val();

		if (itemNoticeCode == '') {
			$('#item_info_area tr:gt(0)').remove();
			$('.check-all-item-notice-label').hide();

		} else {
			$('.check-all-item-notice-label').show();

			$.post('${fn:escapeXml(requestContext.managerUri)}/item/item-notice-list', {'itemNoticeCode': itemNoticeCode}, function(response) {
				Common.responseHandler(response, function() {

					var html = '';
					for (var i = 0; i < response.data.length; i++) {
						var itemNotice = response.data[i];

						html += '	<tr>';
						html += '		<td class="label"><span class="required_mark">*</span>' + itemNotice.noticeTitle + '<input type="hidden" name="itemInfoTitles" value="' + itemNotice.noticeTitle + '" /></td>';
						html += '		<td>';
						html += '			<div>';

						if (itemNotice.noticeTitle != itemNotice.noticeDescription) {
							html += '			<p class="text-info text-sm">* ' + itemNotice.noticeDescription + '</p>';

						}
						html += '				<input type="text" name="itemInfoDescriptions" maxlength="100" class="required" title="' + itemNotice.noticeTitle + '" />';
						html += '				<label><input type="checkbox" class="check-item-notice" /> 상세정보 별도표기</label>';
						html += '			</div>';
						html += '		</td>';
						html += '	</tr>';

					}

					var $itemInfoArea = $('#item_info_area');
					$itemInfoArea.find('tr:gt(0)').remove();
					$itemInfoArea.append(html);


					// 모두 선택이 체크된 경우.
					if ($('.check-all-item-notice').prop('checked')) {

					}


					// 등록된 데이터가 있는 경우.
					var $itemNoticeData = $('#item-notice-data p');
					var selectedItemNoticeCode = '${fn:escapeXml(item.itemNoticeCode)}';

					if ($itemNoticeData.size() > 0 && selectedItemNoticeCode == itemNoticeCode) {

						$itemNoticeData.each(function(i) {
							var tit = $(this).find('span.title').text();
							var desc = $(this).find('span.desc').text();

							$itemInfoArea.find('tr').each(function() {
								var $target = $(this);
								var $itemInfoTitles = $(this).find('input[name=itemInfoTitles]');


								if ($itemInfoTitles.val() == tit) {
									$target.find('input[name=itemInfoDescriptions]').val(desc);

									if (desc == DEFAULT_MESSAGE) {
										$target.find('input[name=itemInfoDescriptions]').prop('readonly', true);
										$target.find('input.check-item-notice').prop('checked', true);
									}
								}
							});
						});
					}
				});
			});

		}
	}

}

// 상품 승인 처리 이벤트
function initItemApprovalEvent() {
	var $btnApproval = $('.btn-approval');

	if ($btnApproval.size() == 0) {
		return;
	}

	$btnApproval.on('click', function() {
		if (!confirm('승인 처리를 하시겠습니까?\n(상품 정보는 수정되지 않습니다.)')) {
			return;
		}

		$.post('/opmanager/item/edit/seller-item-approval/${fn:escapeXml(item.itemId)}', null, function(response){

			Common.responseHandler(response, function(response) {

				alert('승인이 완료 되었습니다.');
				location.href = '/opmanager/item/seller/list';

			}, function(response){

				alert(response.errorMessage);

			});

		}, 'json');

	});
}


// 할인 / 포인트 설정 이벤트 핸들러
function initDiscountAndPointEvent() {
	init();

	$('input[name=sellerDiscountFlag]').on('click', function() {
		showHideContent($(this));
		setValidatorCondition();
	});

	$('input[name=spotFlag]').on('click', function() {
		showHideContent($(this));
		setValidatorCondition();
	});

	$('input[name=sellerPointFlag]').on('click', function() {
		showHideContent($(this));
		setValidatorCondition();
	});

	$('select[name=pointType]').on('change', function(){
		$('input[name=point]').val('0');
		setNumberClass($('input[name=point]'), $(this).val());
	});

	function init() {
		showHideContent($('input[name=sellerDiscountFlag]:checked'));
		showHideContent($('input[name=spotFlag]:checked'));
		showHideContent($('input[name=sellerPointFlag]:checked'));

		// 스팟 (운영자 스팟할인 진행시 판매자 수정불가)
		var spotType = $('#spotType').val();
		var isSellerLogin = '${fn:escapeXml(sellerContext.login)}';
		if (spotType == '1' && isSellerLogin == 'true') {
			$('#spot-area').find('input, select').prop('disabled', true);
			$('#spot-area').find('button').hide();
		}
		setNumberClass($('input[name=point]'), $('select[name=pointType]').val());
	}

	function setNumberClass(obj, pointType) {

		if (obj.length > 0) {
			obj.removeClass('_number_comma');
			obj.removeClass('_number_float');

			if ('1' == pointType) {
				obj.addClass('_number_float');
			} else {
				obj.addClass('_number_comma');
			}
		}
	}
}

// .hide_content Show / Hide
function showHideContent($selector) {
	var flag = $selector.val();
	var $content = $selector.closest('div').find('.hide_content');

	if (flag == 'Y') {
		$content.show();
	} else {
		$content.hide();
	}
}

function getItemSetCategoryFilter(itemId){
	var returnValue = false;
	$.ajaxSetup({'async': false});
	$.get("${fn:escapeXml(requestContext.managerUri)}/categories-filter/getItemFilterList?itemId="+itemId, '', function(response){
		// 성공
		if(response.isSuccess){
			itemCategoryCheck(response.data);
		}
	}, 'json').error(function(e){
		alert(e.message);
	});
}

/* function getItemCategoryFilter(categoryId){
	var returnValue = false;
	$.ajaxSetup({'async': false});
	if (categoryId != null) {
		$.get("${fn:escapeXml(requestContext.managerUri)}/categories-filter/filterGroupList?categoryId=" + categoryId, '', function(response) {
			// 성공
			if (response.isSuccess) {
				addItemCategoryFilter(response.data);
			}
		}, 'json').error(function(e) {
			alert(e.responseText);
		});
	}
} */

function addItemCategoryFilter(data){
	$("#categoryFilterSetting tbody").empty();
	var group = '';
	if(data.length > 0){
		$.each(data, function(i){
			var f_id = data[i].id;
			var f_name = data[i].label;
			var f_desc = data[i].description;
			var vals = data[i].codeList;
			group += "<tr fId='"+f_id+"'><td class='label'>"+f_name+"("+f_desc+")</td><td><div>";
			$.each(vals, function(j){
				group += "<span><label id='filter_"+vals[j].id+"'><input type='checkbox' id='filter_"+vals[j].id+"' name='filterCodeId"+j+"' onclick='categoryFilterCheck()' value='"+vals[j].id+"'> "+vals[j].label+"</label></span>";
			});
			group += "</div></td></tr>";
		});
	} else {
		group = "<tr><td colspan=\"2\"><div><h4>카테고리 선택 시 등록 된 필터가 노출됩니다.</h4></div></td></tr>";
	}
	$("#categoryFilterSetting tbody").append(group);

	// 상세인 경우
	if(${fn:escapeXml(mode) == 'edit'}){
		getItemSetCategoryFilter(${fn:escapeXml(item.itemId)});
	}
}

function itemCategoryCheck(data){
	var fL = $("#categoryFilterSetting tr");
	for(var i = 0; i < fL.length; i++){
		var fId = $(fL[i]).attr("fId");
		// 1차 filterGroupId 체크
		for(var j = 0; j < data.length; j++){
			if(fId == data[j].filterGroupId){
				var input = $(fL[i]).find("input");
				// 일치 할 시 CODE 체크
				for(var z = 0; z < input.length; z++){
					if(input[z].value == data[j].filterCodeId){
						input[z].checked = true;
					}
				}
			}
		}
	}
}

function categoryFilterCheck(){
	$("#categoryInput").empty();
	var l = $("#categoryFilterSetting tbody >"); // 항목 갯수
	var filterSetList = new Array();
	$.each(l, function(i){
		var filterMap = {};
		var fid = $(l[i]).attr("fid");	// 항목 ID
		// 체크항목
		var cvL = $(l[i]).find("input[type='checkbox']:checked");
		var cvList = new Array();
		var filterGroups = "<input type='hidden' name='filterGroups["+i+"]' value='"+fid+"'>";
		$("#categoryInput").append(filterGroups);
		$.each(cvL, function(j){
			var filterCodes = "<input type='hidden' name='filterCodes["+fid+"]["+j+"]' value='"+cvL[j].value+"'>";
			$("#categoryInput").append(filterCodes);
		});
	});
}

// 상품 카테고리 추가
function addItemCategory() {
	var breadcrumb = '';
	var categoryId = '';

	$('select.category').each(function(index) {

		var $selectedOption = $(this).find('option:selected');
		if ($selectedOption.size() > 0) {

			// 팀/그룹
			if (index == 0) {
				breadcrumb = $selectedOption.parent().attr('label');
			}

			categoryId = $selectedOption.attr('rel');

			breadcrumb += ' > ' + $selectedOption.text();
		}

	});


	if (categoryId == undefined || categoryId == '') {
		alert(Message.get("M00078"));	// 상품 카테고리를 선택해 주세요.
		$('#category_team_group').focus();
		return;
	}


	//alert(categoryId + '  : ' + breadcrumb);


	// 중복 체크
	var itemDuplicationCount = 0;
	$('input[name=categoryIds]').each(function() {
		if ($(this).val() == categoryId) {
			itemDuplicationCount++;
		}
	});

	if (itemDuplicationCount > 0) {
		alert(Message.get("M00079"));	// 이미 등록된 카테고리입니다.
		return;
	}

	// 카테고리 1개 이상 등록 제한
	var categoryCount = $('#item_categories>li').not( '.nothing' ).length;
	if(categoryCount > 0){
		alert("1개의 카테고리만 등록이 가능합니다.")
		return;
	}

	var html = '';
	html += '	<li id="item_category_' + categoryId + '">' + breadcrumb;
	html += '		<a href="javascript:deleteItemCategory(' + categoryId + ')" class="delete">[' + Message.get("M00074") + ']</a>'; // 삭제
	html += '		<input type="hidden" name="categoryIds" value="' + categoryId + '" />';
	html += '	</li>';


	$('#item_categories').find('li.nothing').remove();
	$('#item_categories').append(html);

	// 카테고리 추가 시 카테고리 필터 조회
	var categoryLiLength = $('#item_categories>li').not( '.nothing' ).length;
	if(categoryLiLength == 1 ){
		/* 첫번재 카테고리[대표 카테고리] 추가 시(length:1)에만 카테고리 필터 조회
             (대표 카테고리 : 처음 추가된 카테고리, 최상단에 있을 것) */
		//getItemCategoryFilter(categoryId);


	}
}

function deleteItemCategory(categoryId) {

	var cId_origin = $('#item_categories>li:first').find('input').val(); // 대표 카테고리(삭제전)

	$('#item_category_' + categoryId).remove();

	var $itemCategories = $('#item_categories');
	if ($itemCategories.find('li').size() <= 0) {
		$itemCategories.append('<li class="nothing">' + Message.get("M00077") + '</li>'); // Jun-Eu Son 2017.4.24 카테고리 삭제시 메시지 처리 수정
		addItemCategoryFilter("");
	}else{

		if(cId_origin == categoryId){
			/* 새로운 카테고리 필터 조회
                 : 삭제한 카테고리가 첫번재 카테고리[대표 카테고리]이고, 다른 카테고리ID가 존재하는 경우 */
			var cId = $('#item_categories>li:first').find('input').val(); // 대표 카테고리(삭제후)
			//getItemCategoryFilter(cId);
		}
	}
}

// 포인트 설정 정보 노출여부
function displayItemPointConfig() {
	if ($('#item_point_area tr').size() == 0) {
		$('#item_point_config').hide();
	} else {
		$('#item_point_config').show();
	}

}

// 포인트 설정 정보 추가
function addItemPoint() {
	var optionTemplate = $('#item_point_template tbody').html();


	var randomKey = Math.floor(Math.random() * 10000) + 1;

	var radioButtons = '';
	radioButtons += '<input type="radio" name="R' + randomKey + '" id="R' + randomKey + '1" checked="checked"> <label for="R' + randomKey + '1">%</label>';
	radioButtons += '<input type="radio" name="R' + randomKey + '" id="R' + randomKey + '2"> <label for="R' + randomKey + '2">P</label>';


	$('#item_point_area').append(optionTemplate.replace('{INPUT_RADIO}', radioButtons));
	displayItemPointConfig();

	var $item = $('#item');
	$item.find('.ui-datepicker-trigger').remove();
	$item.find("input.term").each(function() {
		$(this).removeClass('hasDatepicker').datepicker();
	});
}

// 옵션추가
function addItemOption() {
	var optionTemplate = $('#item_option_template tbody').html();
	var randomKey = Math.floor(Math.random() * 10000) + 1;

	var radioButtons = '';

	radioButtons += '<label><input type="radio" name="optionDisplyTypeRadio' + randomKey + '" value="radio" class="option_display_type" checked="checked" /> 라디오버튼</label>';
	radioButtons += '<label><input type="radio" name="optionDisplyTypeRadio' + randomKey + '" value="select" class="option_display_type" /> 셀렉트박스</label>';


	$('#option_data').hide();
	$('#item_option_area tbody').append(optionTemplate.replace('{INPUT_RADIO}', radioButtons));
}




// validator 체크 조건 설정.
function setValidatorCondition() {
	var currentClasses = "PREFIX";
	var targetFlag = '';

	// 비회원 판매가격
	targetFlag = $('input[name=salePriceNonmemberFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-nonmember-price';
	}

	// 상품재고
	targetFlag = $('input[name=stockFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-stock-quantity';
	}

	// 사은품 정보
	targetFlag = $('input[name=freeGiftFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-free-gift';
	}

	// 즉시할인
	targetFlag = $('input[name=sellerDiscountFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-seller-discount-amount';
	}


	// 스팟할인
	/*targetFlag = $('input[name=spotFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-spot';
	}*/

	// 포인트 지급
	targetFlag = $('input[name=sellerPointFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-seller-point';
	}

	// 옵션
	targetFlag = $('input[name=itemOptionFlag]:checked').val();

	if (targetFlag == 'Y') {

		// 3조합
		targetFlag = $('input[name=itemOptionType]:checked').val();
		if (targetFlag == 'S3') {
			currentClasses += ',required-item-option';
			currentClasses += ',required-item-option-s3';

		} else if (targetFlag == 'T') {
			currentClasses += ',required-item-option-text';

		} else {
			currentClasses += ',required-item-option';

		}
	}



	// 20260325 필수 추가정보
	targetFlag = $('input[name=itemTextOptionFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-item-text-option';
	}



	// 추가구성 상품.
	targetFlag = $('input[name=itemAdditionFlag]:checked').val();
	if (targetFlag == 'Y') {
		currentClasses += ',required-item-addition';
	}

	// 배송비 설정
	targetFlag = $('input[name=shippingType]:checked').val();
	if (targetFlag == '4') {
		currentClasses += ',required-shipping-4';
	}
	if (targetFlag == '5') {
		currentClasses += ',required-shipping-5';
	}
	if (targetFlag == '6') {
		currentClasses += ',required-shipping-6';
	}

	// 공급가 설정
	targetFlag = $('input[name=commissionType]:checked').val();
	if (targetFlag == '1') {
		currentClasses += ',required-commission-rate';
	}
	if (targetFlag == '2') {
		currentClasses += ',required-commission-rate';
	}
	if (targetFlag == '3') {
		currentClasses += ',required-supply-price';
	}

	currentClasses = currentClasses.replace('PREFIX,', '');
	currentClasses = currentClasses.replace('PREFIX', '');

	$.validator.currentClass = currentClasses;

}


function setRelationItemDisplay() {
	var relationItemDisplayType = $('input[name=relationItemDisplayType]:checked').val();
	if (relationItemDisplayType == '1') {
		$('#relation_item_area').hide();

	} else {
		$('#relation_item_area').show();

	}

}

// 상품이미지 삭제
function deleteItemImage(type, id) {
	var mode = "${fn:escapeXml(mode)}";
	if (mode == "copy") {
		var message = '이미지를 삭제하시겠습니까?';
		if (!confirm(message)) {
			return;
		}

		$('#item_image_id_' + id).remove();
	} else {
		var message = '이미지가 실제로 삭제됩니다.(복구불가)\n삭제하시겠습니까?';
		if (!confirm(message)) {
			return;
		}

		if (type == "main") {
			var param = {'itemId': id};
			$.post('${fn:escapeXml(requestContext.managerUri)}/item/delete-item-image', param, function(response){
				Common.responseHandler(response);
				$('.item_image_main').remove();
			});
		} else if (type == "details") {
			var param = {'itemImageId': id};
			$.post('${fn:escapeXml(requestContext.managerUri)}/item/delete-item-details-image', param, function(response){
				Common.responseHandler(response);
				$('#item_image_id_' + param.itemImageId).remove();
			});
		}
	}
}


// 상세설명 - 추가/삭제 이벤트 핸들러
/*
function itemInfoEventHandler() {
	// 기본으로 2개 보임.
	if ($('#item_info_area tr').size() == 0) {
		for (var i = 0; i < 1; i++) {
			addItemInfo();
		}
	}

	if ($('#item_info_mobile_area tr').size() == 0) {
		for (var i = 0; i < 1; i++) {
			addItemInfoMobile();
		}
	}


	// textarea resize - load
	$('#item_info_area textarea, #item_info_mobile_area textarea').each(function() {
		$(this).css('height','auto');
		$(this).height(this.scrollHeight < 30 ? 20 : this.scrollHeight);
	});

	// textarea resize - event
	$(document).on('keyup', '#item_info_area textarea, #item_info_mobile_area textarea' ,function(){
		$(this).css('height','auto');
		$(this).height(this.scrollHeight < 30 ? 20 : this.scrollHeight);
    });

	// 항목 삭제 이벤트.
	$(document).on('click', '#item_info_area .delete_item_info' ,function(e) {
		e.preventDefault();

		if ($('#item_info_area tr').size() > 1) {
			$(this).closest('tr').remove();
		} else {
			$(this).closest('tr').find('input, textarea').val('');
			$(this).closest('tr').find('textarea').height(20);
		}
    });

	// 항목 삭제 이벤트.
	$(document).on('click', '#item_info_mobile_area .delete_item_info' ,function(e) {
		e.preventDefault();

		if ($('#item_info_mobile_area tr').size() > 1) {
			$(this).closest('tr').remove();
		} else {
			$(this).closest('tr').find('input, textarea').val('');
			$(this).closest('tr').find('textarea').height(20);
		}
    });
}
*/
// 상세설명 - 항목추가
function addItemInfo() {
	var html = '';
	html += '	<tr>';
	html += '		<td class="label"><input type="text" name="itemInfoTitles" maxlength="50" /></td>';
	html += '		<td>';
	html += '			<div>';
	html += '				<textarea name="itemInfoDescriptions" maxlength="500" rows="1"></textarea>';
	html += '			</div>';
	html += '		</td>';
	html += '		<td class="middle"><a href="#" class="fix_btn delete_item_info">' + Message.get("M00074") + '</a></td>';	// 삭제
	html += '	</tr>';

	$('#item_info_area').append(html);
}

//상세설명 - 항목추가
function addItemInfoMobile() {
	var html = '';
	html += '	<tr>';
	html += '		<td class="label"><input type="text" name="itemInfoMobileTitles" maxlength="50" /></td>';
	html += '		<td>';
	html += '			<div>';
	html += '				<textarea name="itemInfoMobileDescriptions" maxlength="500" rows="1"></textarea>';
	html += '			</div>';
	html += '		</td>';
	html += '		<td class="middle"><a href="#" class="fix_btn delete_item_info">' + Message.get("M00074") + '</a></td>';	// 삭제
	html += '	</tr>';

	$('#item_info_mobile_area').append(html);
}

function copyContent(id) {

	if (id == 'itemInfo') {
		var source = $('#item_info_area').html();
		source = source.replace(/itemInfoTitles/g, 'itemInfoMobileTitles');
		source = source.replace(/itemInfoDescriptions/g, 'itemInfoMobileDescriptions');

		$('#item_info_mobile_area').empty().append(source);

		$('input[name=itemInfoTitles]').each(function(i) {
			var sourceTitle = $('input[name=itemInfoTitles]').eq(i).val();
			var sourceDescription = $('#item_info_area textarea').eq(i).val();

			$('input[name=itemInfoMobileTitles]').eq(i).val(sourceTitle);
			$('#item_info_mobile_area textarea').eq(i).val(sourceDescription);
		});
		return;
	}

	var sourceHtml = editors.getById[id].getIR();
	editors.getById[id + "Mobile"].exec("SET_CONTENTS", ['']);
	editors.getById[id + "Mobile"].exec("PASTE_HTML", [sourceHtml]);

	//oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD", []);	// 에디터의 내용이 textarea에 적용됩니다.
}


function scrollButtons() {
	$(window).scroll(function () {
		var st = $(window).scrollTop();
		var scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();

		if (scrollBottom < 170) {
			$('#buttons').removeClass('fixed_button');
		} else {
			$('#buttons').addClass('fixed_button');
		}
	});
}

// 재고 연동 여부에 따른 수량입력 show/hide 이벤트
function initStockFlagEvent() {

	$('input[name=stockFlag]').on('click', function() {
		var stockFlag = $('input[name=stockFlag]:checked').val();
		var $trStockQuantity = $('#trStockQuantity');

		if (stockFlag == 'Y') {
			$trStockQuantity.show();
		} else {
			$trStockQuantity.hide();
			$trStockQuantity.val('');
		}

		setValidatorCondition();
	});

}

// 세트상품 이벤트 핸들러
function initItemSetEvent() {
	var isSet = ${item.itemType == '3'};
	if (!isSet) {
		return false;
	}

	// 상품 합계금액, 세트상품 판매가
	var setSaleAmount = 0;
	var setTotalAmount = 0;

	// 설정한 할인타입, 할인금액(율)
	var discountType = $('#setDiscountType').val();
	var discountAmount = intNvl($('#setDiscountAmount').val().replace(',', ''), 0);

	// 추가한 상품의 판매가 계산
	$('#set tr').each(function() {
		var itemName = $(this).data('name');
		var salePrice = intNvl($(this).data('price'), 0);
		var quantity = intNvl($(this).find('input[name=setQuantities]').val(), 1);
		// var minQuantity = intNvl($(this).data('min-quantity'), -1);
		// var maxQuantity = intNvl($(this).data('max-quantity'), -1);

		if (quantity <= 0) {
			quantity = 1;
		}

		/*if (quantity < minQuantity) {
			alert(itemName + ' 상품의 최소주문수량이 ' + minQuantity + '개 입니다.');
			quantity = minQuantity;
		}

		if (maxQuantity > 0 && quantity > maxQuantity) {
			alert(itemName + ' 상품의 최대주문수량이 ' + maxQuantity + '개 입니다.');
			quantity = maxQuantity;
		}*/

		setSaleAmount += salePrice * quantity;
		$(this).find('input[name=setQuantities]').val(quantity);
	});

	// 할인구분 (1:금액, 2:비율)
	if (discountType === '2') {
		if (discountAmount > 100) {				// 100% 초과시
			discountAmount = 0;
		}

		var discountPrice = setSaleAmount * (discountAmount / 100);
		setTotalAmount = setSaleAmount - (Math.floor(discountPrice / 10) * 10);  // 원 단위 절사
	} else {
		if (discountAmount > setSaleAmount) {	// 조건 불일치
			discountAmount = 0;
		}

		setTotalAmount = setSaleAmount - discountAmount;
	}

	$('#setDiscountAmount').val(Common.numberFormat(discountAmount));
	$('#set-total-amount').text(Common.numberFormat(setTotalAmount));
	$('#set-sale-amount').text(Common.numberFormat(setSaleAmount));

	$('input[name="salePrice"]').val(setSaleAmount);
	$('input[name="salePrice"]').removeClass('required');
}

function intNvl($select, value) {
	if ($select === '' || $select == null) {
		return value;
	}

	return parseInt($select);
}

// 세트상품 제거
function deleteItemSet(type, id) {
	if (type === 'all') {
		Shop.deleteRelationItemAll('set');
	} else {
		Shop.deleteRelationItem(id)
	}

	initItemSetEvent();
}

// 출고지 정보 팝업 콜백 핸들러
function handleShipmentPopupCallback(shipment) {
	//alert(shipment.shipmentGroupCode);
	$('#shipmentId').val(shipment.shipmentId);
	$('#shipmentAddress').val('[' + shipment.zipcode + '] ' + shipment.address + ' ' + shipment.addressDetail);
	$('#shipmentGroupCode').val(shipment.shipmentGroupCode);

	var $shippingType3 = $('.shipping-type-3');
	$shippingType3.find('.opt-shipping').val(shipment.shipping);
	$shippingType3.find('.opt-shipping-text').text(Common.numberFormat(shipment.shipping));
	$shippingType3.find('.opt-shipping-free-amount').val(shipment.shippingFreeAmount);
	$shippingType3.find('.opt-shipping-free-amount-text').text(Common.numberFormat(shipment.shippingFreeAmount));
	$shippingType3.find('.opt-shipping-extra-charge1').val(shipment.shippingExtraCharge1);
	$shippingType3.find('.opt-shipping-extra-charge2').val(shipment.shippingExtraCharge2);

	$('.shipment-empty').hide();
	$('.shipment-info').show();
}

function handleShipmentReturnPopupCallback(shipmentReturn) {
	$('#shipmentReturnId').val(shipmentReturn.shipmentReturnId);
	$('#shipmentReturnAddress').val('[' + shipmentReturn.zipcode + '] ' + shipmentReturn.address + ' ' + shipmentReturn.addressDetail);


}


// 판매자 선택 이벤트 핸들러.
function initSellerIdEvent() {

}


// 판매자조건부 정보 초기화
function clearSellerInfo() {
	var $shippingType2 = $('.shipping-type-3');
	$shippingType2.find('.opt-shipping-text').text("0");
	$shippingType2.find('.opt-shipping').val("0");
	$shippingType2.find('.opt-shipping-free-amount').val("0");
	$shippingType2.find('.opt-shipping-extra-charge1').val("0");
	$shippingType2.find('.opt-shipping-extra-charge2').val("0");

	$('.seller-empty').show();
	$('.seller-info').hide();
}


// 출고지 정보 초기화
function clearShipmentInfo() {
	var $shippingType3 = $('.shipping-type-3');
	$shippingType3.find('.opt-shipping-text').text("0");
	$shippingType3.find('.opt-shipping').val("0");
	$shippingType3.find('.opt-shipping-free-amount').val("0");
	$shippingType3.find('.opt-shipping-free-amount-text').text("0");
	$shippingType3.find('.opt-shipping-extra-charge1').val("0");
	$shippingType3.find('.opt-shipping-extra-charge2').val("0");

	$('#shipmentId').val(0);
	$('#shipmentAddress').val('');

	$('#shippingExtraCharge1').val('0');
	$('#shippingExtraCharge2').val('0');

	$('.shipment-empty').show();
	$('.shipment-info').hide();
}
// 반송지 정보 초기화
function clearShipmentReturnInfo() {
	$('#shipmentReturnId').val(0);
	$('#shipmentReturnAddress').val("");
}

function findMd(targetId) {
	Common.popup('/opmanager/seller/find-md?targetId=' + targetId, 'find_md', 720, 800, 1);
}

function clearMd(targetId) {
	var $target = $('#' + targetId);
	$target.val('');
	$target.closest('td').find('#mdName').val('');
}

// MD 검색 콜백
function handleFindMdCallback(response) {
	var $target = $('#' + response.targetId);
	$target.val(response.userId);
	$target.closest('td').find('#mdName').val(response.userName);

}

function findItemPopup(targetId, itemUserCode){
	<c:choose>
		<c:when test="${isSellerPage}">
			Common.popup("/seller/item/find-item?targetId=" + targetId + "&itemUserCode=" + itemUserCode, 'find-item', 1000, 600, 1);
		</c:when>
		<c:otherwise>
		Common.popup("/opmanager/item/find-item?targetId=" + targetId + "&itemUserCode=" + itemUserCode, 'find-item', 1000, 600, 1);
		</c:otherwise>
	</c:choose>
}

function sendRestockNotice() {
    var param = {
        'itemId' : '${fn:escapeXml(item.itemId)}'
	};

    if (confirm('재입고 메시지를 전송하시겠습니까?')) {
		$.post('/opmanager/item/restock-notice/message', param, function(response) {
			Common.responseHandler(response, function () {
                $('.restock-button').addClass('hide');
                $('.restock-text').removeClass('hide');
				alert('메시지가 전송되었습니다.')
			});
		}, 'json');
    }
}

function findGiftItem(){

	var targetId = 'freeGift';

	<c:choose>
		<c:when test="${isSellerPage}">
			Common.popup("/seller/gift-item/find-item?processType=progress&targetId=" + targetId, 'find-gift-item', 1000, 600, 1);
		</c:when>
		<c:otherwise>
			Common.popup("/opmanager/gift-item/find-item?processType=progress&targetId=" + targetId + "&sellerId="+$('#sellerId').val(), 'find-gift-item', 1000, 600, 1);
		</c:otherwise>
	</c:choose>

}

function selectSpotDateType(type) {

    if (type == '2') {
        $('.spotDateTypeOne').css('display', 'none');
        $('.spotDateTypeTwo').css('display', '');

    } else {
        $('.spotDateTypeOne').css('display', '');
        $('.spotDateTypeTwo').css('display', 'none');

    }

}

function text_maxlength(obj) {
	$("#itemlength").text("["+obj.value.length+"/50]");
}

function text_maxlength2(obj) {
	$("#itemSummarylength").text("["+obj.value.length+"/100]");
}

function seasonFoodMonthListOnclick(obj) {
	if ($("input[name=seasonFoodMonthList]:checked").length > 3) {
		alert("제철 월 선택은 3개까지 가능합니다.");
		return false;
	}
}

function setMobileValidatorCondition() {

	// 모바일 답례품일 경우 수정을 위해 화면진입시 배송정보 설정영역 숨김
	if('${fn:escapeXml(item.mobileItemYn)}' == 'Y') {
		$('#deliveryDiv').addClass("hidden");	// 배송정보 설정 영역 숨기기
		$('#deliveryCompanyId').removeClass("required");	// 택배사 필수선택 제거
		$('#shipmentAddress').removeClass("required");		// 출고지 주소 필수선택 제거
		$('#shipmentAddress').val('');	// 출고지 주소 초기화
		$('input:radio[name="itemReturnFlag"]:radio[value="N"]').prop('checked',true);	// 반품/교환 신청 가능 여부 불가능 선택
		$('#shipmentReturnAddress').removeClass("required");	// 반품/교환 주소 필수선택 제거
		$('#shipmentReturnAddress').val('');	// 반품/교환 주소 주소 초기화
	}

	// 모바일 답례품으로 신규등록시 모바일 체크여부에 따른 배송정보 필수입력 설정 (활성화/비활성화)
	$('input[name=mobileItemYn]').on('click', function() {
		if(this.checked) {
			$('#deliveryDiv').addClass("hidden");	// 배송정보 설정 영역 숨기기
			$('#deliveryCompanyId').removeClass("required");	// 택배사 필수선택 제거
			$('#deliveryCompanyId').val("0");	// 택배사 초기화
			$('#deliveryCompanyName').val("");	// 택배사명 초기화
			$('#shipmentAddress').removeClass("required");		// 출고지 주소 필수선택 제거
			$('#shipmentAddress').val('');	// 출고지 주소 초기화
			$('#shipmentId').val('0');	// 출고지ID 초기화
			$('input:radio[name="itemReturnFlag"]:radio[value="N"]').prop('checked',true);	// 반품/교환 신청 가능 여부 불가능 선택
			$('#shipmentReturnAddress').removeClass("required");	// 반품/교환 주소 필수선택 제거
			$('#shipmentReturnAddress').val('');	// 반품/교환 주소 주소 초기화
			$('#shipmentReturnId').val('0');	// 반송지ID 초기화
		} else {
			$('#deliveryDiv').removeClass("hidden")	// 배송정보 설정 영역 보이기
			$('#deliveryCompanyId').addClass("required");	// 택배사 필수선택 추가
			$('#shipmentAddress').addClass("required");		// 출고지 주소 필수선택 추가
			$('input:radio[name="itemReturnFlag"]:radio[value="Y"]').prop('checked',true);	// 반품/교환 신청 가능 여부 가능 선택
			$('#shipmentReturnAddress').addClass("required");	// 반품/교환 주소 필수선택 추가
		}
	});

}

//네이버editor내의 이미지 세기
function countImagesInIframe(iframe) {

    let iframeDoc = iframe.contentDocument || iframe.contentWindow.document;

    let images = iframeDoc.querySelectorAll("img");
    let imageCount = images.length;

    let nestedIframes = iframeDoc.querySelectorAll("iframe");

    nestedIframes.forEach(function(nestedIframe) {
        imageCount += countImagesInIframe(nestedIframe);
    });

    return imageCount;
}

//이미지 alt update
function updateImgAlt() {
	let values = [...document.querySelectorAll("#item-options2 input[id='imageExplainValue']")].map(input => input.value);
	let outerIframe = document.getElementById("editor_frame");
    if (!outerIframe) {
        setTimeout(updateImgAlt, 500);
        return;
    }

    let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
    let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");

    if (!innerIframe) {
        setTimeout(updateImgAlt, 500);
        return;
    }

    let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
    let images = innerIframeDoc.querySelectorAll("img");

    if (images.length === 0) {
        setTimeout(updateImgAlt, 500);
        return;
    }

    images.forEach((img, index) => {
        if (values[index]) {
            img.setAttribute("alt", values[index]);
        }
        });
    }

function clearContent() {

	if (confirm('작성하신 모든 내용이 지워집니다. \n내용을 모두 삭제 하시겠습니까?')) {

	 	let outerIframe = document.getElementById("editor_frame");
	    if (!outerIframe) {
	        setTimeout(clearContent, 500);
	        return;
	    }

	    let outerIframeDoc = outerIframe.contentDocument || outerIframe.contentWindow.document;
	    let innerIframe = outerIframeDoc.querySelector("#smart_editor2_content iframe");

	    if (!innerIframe) {
	        setTimeout(clearContent, 500);
	        return;
	    }
	    let innerIframeDoc = innerIframe.contentDocument || innerIframe.contentWindow.document;
	    innerIframeDoc.body.innerHTML="";
	    $(".se2_input_htmlsrc").val("");
	    $(".se2_input_text").val("");

	}




}
</script>
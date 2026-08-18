<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<c:if test="${categories.categoryLevel != '1'}">
	<input type="hidden" name="categoryGroupId" value="${fn:escapeXml(categories.categoryGroupId)}" />
</c:if>

    <!-- 2차 카테고리 수정 -->
	<h5 style="margin-top: 0; border-left: 4px solid #000; padding-left: 8px;"> ${fn:escapeXml(categories.categoryLevel+1)}${op:message('M01292')} <!-- 차 카테고리 수정 --> </h5>
	<table class="board_write_table bt2">
		<caption> ${op:message('M00545')} <!-- 카테고리 만들기 --></caption>
		<colgroup>
			<col style="width: 150px;" />
			<col style="width: auto;" />
		</colgroup>
		<tbody>
			<tr>
				<td class="label">URL</td>
				<td>
					<div style="font-size: 13px;">
						<c:choose>
							<c:when test='${op:property("saleson.view.type") eq "api"}'>
								<a href="${op:property('saleson.url.frontend')}/goods/searchGoods.html?category=${fn:escapeXml(categories.categoryUrl)}&type=C" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
								/goods/searchGoods.html?category=
							</c:when>
							<c:otherwise>
								<a href="/m/categories/index/${fn:escapeXml(categories.categoryUrl)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_mobile.gif" alt="" /></a>
								<a href="/categories/index/${fn:escapeXml(categories.categoryUrl)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
								/categories/index/
							</c:otherwise>
						</c:choose>

						<input type="text" id="categoryUrl" name="categoryUrl" style="width: 150px; font-size: 12px; color: #000" value="${fn:escapeXml(categories.categoryUrl)}" />&type=C
				 	</div>
				</td>
			</tr>
		 	 <tr>
				<td class="label">${op:message('M01139')} <!-- 카테고리명 --></td>
				<td>
					<div>
						<input name="categoryName" type="text" class="required full" title="${op:message('M01139')}" value="${fn:escapeXml(categories.categoryName)}">
						<input name="categoryId" type="hidden" value="${fn:escapeXml(categories.categoryId)}" />
						<input name="categoryCode" type="hidden" value="${fn:escapeXml(categories.categoryCode)}" />
						<input name="categoryClass1" type="hidden" value="${fn:escapeXml(categories.categoryClass1)}" />
						<input name="categoryClass2" type="hidden" value="${fn:escapeXml(categories.categoryClass2)}" />
						<input name="categoryClass3" type="hidden" value="${fn:escapeXml(categories.categoryClass3)}" />
						<input name="categoryClass4" type="hidden" value="${fn:escapeXml(categories.categoryClass4)}" />
						<input name="categoryLevel" type="hidden" value="${fn:escapeXml(categories.categoryLevel)}" />
						<input name="currentCategoryUrl" type="hidden" value="${fn:escapeXml(categories.categoryUrl)}" />
						<input name="categoryUrlCheck" type="hidden" value="1" />
				 	</div>
				</td>
			</tr>

			<c:if test="${categories.categoryLevel == '1'}">
			<tr>
				<td class="label">1차 카테고리</td>
				<td>
					<div>
						<p>
							<select name="categoryGroupId" class="category">
								<option value="0">= 카테고리 그룹 =</option> <!-- 팀/그룹 -->
								<c:forEach items="${categoryTeamGroupList}" var="categoriesTeam">
									<c:if test="${categoriesTeam.categoryTeamFlag == 'Y'}">
										<optgroup label="${fn:escapeXml(categoriesTeam.name)}">
										<c:forEach items="${categoriesTeam.categoriesGroupList}" var="categoriesGroup">
											<c:if test="${categoriesGroup.categoryGroupFlag == 'Y'}">
												<option value="${fn:escapeXml(categoriesGroup.categoryGroupId)}" label="${fn:escapeXml(categoriesGroup.groupName)}" ${op:selected(categories.categoryGroupId, categoriesGroup.categoryGroupId) }>${fn:escapeXml(categoriesGroup.groupName)}</option>
											</c:if>
										</c:forEach>
										</optgroup>
									</c:if>
								</c:forEach>
							</select>
						</p>
				 	</div>
				</td>
			</tr>
			</c:if>

			<tr>
				<td class="label">${op:message('M00191')}</td> <!-- 공개유무 --></td>
				<td>
                    <div class="flex_box gap-12">
                        <div class="input-form">
                            <input type="radio" name="categoryFlag" value="Y" id="out_all" ${op:checked('Y',categories.categoryFlag) } />
                            <label for="out_all">${op:message('M00096')} <!-- 공개 --></label>
                        </div>
                        <div class="input-form">
                            <input type="radio" name="categoryFlag" value="N" id="out" ${op:checked('N',categories.categoryFlag) } />
                            <label for="out">${op:message('M00097')} <!-- 비공개 --></label>
                        </div>
                     </div>
				</td>
			</tr>
			<tr style="display: none;">
				<td class="label">${op:message('M01604')} <!-- 접속권한 --></td>
				<td>
					<div>
						<form:radiobutton path="categories.accessType" value="1" checked="checked" label="${op:message('M00497')}" /> <!-- 제한없음 -->
						<form:radiobutton path="categories.accessType" value="2" label="${op:message('M01679')}" />  <!-- 성인만 -->
					</div>
				</td>
			</tr>
		</tbody>
	</table>

    <div class="btn_all btn_center">
        <div class="flex_box gap-08">
            <button type="submit" class="btn btn-small btn-dark-gray">수정</button>
            <button type="button" class="btn btn-small btn-default op-delete-category" onclick="deleteCategory();">${op:message('M00074')}</button> <!-- 삭제 -->
        </div>
    </div>
    <!-- // 2차 카테고리 수정 -->

	<!-- 3차 카테고리 등록 -->
	<c:if test="${categories.categoryLevel != '2' && categories.categoryType == '1' }">
		<h5 style="margin-top: 20px;border-left: 4px solid #000; padding-left: 8px;"> ${fn:escapeXml(categories.categoryLevel+2) }${op:message('M01294')} <!-- 차 --> 카테고리 등록</h5>
		<table class="board_write_table bt2">
			<caption> ${op:message('M00545')} <!-- 카테고리 만들기 --></caption>
			<colgroup>
				<col style="width: 150px;" />
				<col style="width: auto;" />
			</colgroup>
			<tbody>
				<tr>
					<td class="label">${op:message('M01139')} <!-- 카테고리명 --></td>
					<td>
						<div>
							 <input name="categoryLowName" type="text" class="full" style="width: 50%;" title="${op:message('M01139')}" />
					 	</div>
					</td>
				</tr>
				<tr>
					<td class="label">URL</td>
					<td>
						<div class="flex_box gap-08">
							 <input name="categoryLowUrl" type="text" class="full" style="width: 50%;" title="URL" />
							 <input name="categoryLowUrlCheck" type="hidden" value="0" />
							 <a href="javascript:;" id="categroyLowUrlSearch" class="btn btn-gradient btn-sm">${op:message('M01488')}</a>
					 	</div>
					</td>
				</tr>

				<tr>
					<td class="label">${op:message('M00191')}</td> <!-- 공개유무 --></td>
					<td>
                        <div class="flex_box gap-12">
                            <div class="input-form">
                                <input type="radio" name="categoryLowFlag" value="Y" id="out_all-01" checked="checked"} />
                                <label for="out_all-01">${op:message('M00096')} <!-- 공개 --></label>
                            </div>
                            <div class="input-form">
                                <input type="radio" name="categoryLowFlag" value="N" id="out-01"} />
                                <label for="out-01">${op:message('M00097')} <!-- 비공개 --></label>
                            </div>
                         </div>
					</td>
				</tr>
			</tbody>
		</table>

        <div class="btn_all btn_center">
            <div class="flex_box gap-08">
                <a href="javascript:;" id="categroyLowAdd" class="btn btn-small btn-dark-gray">${categories.categoryLevel + 2}차 카테고리 등록</a>
            </div>
        </div>
	</c:if>
    <!-- // 3차 카테고리 등록 -->
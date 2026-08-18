<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<tr>
	<td>
		<input type="hidden" name="optionId" value="${fn:escapeXml(itemOption.itemOptionId)}" />
		<input type="hidden" name="optionType" value="${fn:escapeXml(itemOption.optionType)}" />
		<input type="text" name="optionName1" maxlength="200" class="required-item-option" title="${op:message('M00977')}" value="${fn:escapeXml(itemOption.optionName1)}" />	 <%-- 옵션명 --%>
	</td>
	<td>
		<input type="text" name="optionName2" maxlength="200" class="required-item-option" title="${op:message('M00977')}"  value="${fn:escapeXml(itemOption.optionName2)}" />	 <%-- 옵션명 --%>
	</td>
	<td class="option-S3">
		<input type="text" name="optionName3" maxlength="200" class="required-item-option-s3" title="${op:message('M00977')}"  value="${fn:escapeXml(itemOption.optionName3)}" />	 <%-- 옵션명 --%>
	</td>
	<td><input type="text" name="optionPrice" value="${fn:escapeXml(itemOption.optionPrice)}" class="_number_comma" /></td>
	<td class="hidden"><input type="text" name="optionCostPrice" value="${fn:escapeXml(itemOption.optionCostPrice)}" class="_number_comma" /></td>
	<td>
		<select name="optionStockFlag">
			<option value="N" ${op:selected('N', itemOption.optionStockFlag)}>무제한</option>
			<option value="Y" ${op:selected('Y', itemOption.optionStockFlag)}>재고연동</option>
		</select>
	</td>
	<td><input type="text" name="optionStockQuantity" maxlength="5" value="${fn:escapeXml(itemOption.optionStockQuantity)}" readonly="realonly" class="_number_comma" title="옵션 재고수량" /></td>
	<td>
		<select name="optionSoldOutFlag">
			<option value="N" ${op:selected('N', itemOption.optionSoldOutFlag)}>판매가능</option>
			<option value="Y" ${op:selected('Y', itemOption.optionSoldOutFlag)}>품절</option>
		</select>
	</td>
	<td>
		<select name="optionDisplayFlag">
			<option value="Y" ${op:selected('Y', itemOption.optionDisplayFlag)}>노출</option>
			<option value="N" ${op:selected('N', itemOption.optionDisplayFlag)}>숨김</option>
		</select>
	</td>
	<td class="hidden"><input type="text" name="optionStockCode" value="${fn:escapeXml(itemOption.optionStockCode)}" /></td>
	<td><a href="#" class="btn btn-dark-gray btn-sm delete-item-option">삭제</a></td>
</tr>
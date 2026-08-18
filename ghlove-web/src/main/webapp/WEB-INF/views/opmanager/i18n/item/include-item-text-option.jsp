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
														<input type="hidden" name="optionId" value="${fn:escapeXml(itemTextOption.itemOptionId)}" />
														<input type="hidden" name="optionType" value="T" />
														<input type="text" name="optionName1" value="${fn:escapeXml(itemTextOption.optionName1)}" title="옵션명" maxlength="200" />
														<%-- <input type="text" name="optionName1" value="${itemTextOption.optionName1}" class="required-item-option-text" title="옵션명" /> --%>
														<input type="hidden" name="optionName2" value="" />
														<input type="hidden" name="optionName3" value="" />
														<input type="hidden" name="optionPrice" value="" />
														<input type="hidden" name="optionCostPrice" value="" />
														<input type="hidden" name="optionStockFlag" value="N" />
														<input type="hidden" name="optionStockQuantity" value="" />
														<input type="hidden" name="optionSoldOutFlag" value="N" />
														<input type="hidden" name="optionStockCode" value="" />
													</td>
													<td>
														<select name="optionDisplayFlag">
															<option value="Y" ${op:selected("Y", itemTextOption.optionDisplayFlag)}>노출</option>
															<option value="N" ${op:selected("N", itemTextOption.optionDisplayFlag)}>숨김</option>
														</select>
													</td>
													<td><a href="#" class="btn btn-dark-gray btn-sm delete-item-text-option">삭제</a></td>
												</tr>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="all_menu">
	<a href="javascript:Shop.showAllCategoriesLayer();" class="btn_menu" id="op-all-menu-button">전체 카테고리</a>
    <div class="gnbs" id="op-all-menus">
        <c:forEach items="${shopContext.shopCategoryGroups}" var="group" varStatus="i">
            <c:if test="${i.index % 4 == 0}">
                <ul class="depth">
            </c:if>
                    <li class="menu">
                            <h2><c:out value="${group.name}"/></h2>
                            <ul>
                                <c:forEach items="${group.categories}" var="category1">
                                    <li>
                                        <a class="all_2depth" href="/categories/index/${fn:escapeXml(category1.url)}"><c:out value="${fn:escapeXml(category1.name)}"/></a>
                                        <ul>
                                            <c:forEach items="${category1.childCategories}" var="category2">
                                                <li>
                                                    <a href="/categories/index/${fn:escapeXml(category2.url)}"><c:out value="${fn:escapeXml(category2.name)}"/></a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </li>
                                </c:forEach>
                            </ul>
                    </li>
            <c:if test="${i.index % 4 == 3 || i.last}">
                </ul>
            </c:if>
        </c:forEach>
    </div>
</div>
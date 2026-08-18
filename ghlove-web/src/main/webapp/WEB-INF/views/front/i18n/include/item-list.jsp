<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
				<c:forEach items="${items}" var="item" varStatus="i">
                <c:choose>
                    <c:when test="${item.itemLabel == '2' || item.itemType1 == '1' || item.itemType2 == '1' || item.itemType4 == '1' || item.itemType5 == '1'}">
                        <li>
                    </c:when>
                    <c:otherwise>
                        <li class="-nolabel">
                    </c:otherwise>
                </c:choose>
						<a href="javascript:redirectItem('${fn:escapeXml(item.itemUserCode)}','${fn:escapeXml(item.link)}')" ${fn:escapeXml(item.noFollow)}>
							<div id="item-${fn:escapeXml(item.itemUserCode)}" style="display: none" class="item_data"
							     data-item-user-code="${fn:escapeXml(item.itemUserCode)}"
							     data-item-name="${fn:escapeXml(item.itemName)}"
							     data-brand="${fn:escapeXml(item.brand)}"
							     data-present-price="${fn:escapeXml(item.exceptUserDiscountPresentPrice)}"
							></div>
							<div class="thumbnail_wrap">
								<div class="item-label-01">
									<p>
										<c:choose>
											<c:when test="${item.itemLabel == '2'}">
												<span class="new"><img src="/content/images/icon/label_max_new.png" alt="new"></span>
											</c:when>
											<c:when test="${item.itemLabel == '3'}">
												<span class="sale"><img src="/content/images/icon/label_max_sale.png" alt="sale"></span>
											</c:when>
										</c:choose>
									</p>
								</div>
								<c:choose>
									<c:when test="${(item.itemSoldOutFlag == 'Y')}">
										<span class="thumbnail soldOut"><!-- 품절 -->
											<img src="/content/images/common/sold_out.jpg" alt="thumbnail" class="thumbnail_photo">
										</span>
									</c:when>
									<c:otherwise>
										<c:set var="imageSize">&size=223</c:set>
										<span class="thumbnail">
											<img src="${ shop:loadImage(item.itemUserCode, item.itemImage, 'S') }"  alt="${fn:escapeXml(item.itemName)}" class="thumbnail_photo">
											<%--<img src="/content/images/common/thumbnail.jpg">--%>
										</span>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="item-info">
								<p class="name"><c:out value="${item.itemName}"/></p>
								<div class="price-zone">
									<c:if test="${item.discountRate > 0}">
										<p class="sale">
										<c:out value="${item.discountRate }"/><span>%</span>
										</p>
									</c:if>
									<p class="price <c:if test="${item.discountRate == 0}">-nobp</c:if>">
										<c:if test="${item.totalDiscountAmount > 0 && item.discountRate > 0}">
										<span class="before_price"><c:out value="${op:numberFormat(item.listPrice)}"/><span>원</span></span>
										</c:if>
										<span class="sale_price"><c:out value="${op:numberFormat(item.exceptUserDiscountPresentPrice)}"/><span>원</span></span>
									</p>
								</div>
							</div>  
						</a>
						<%--
						<div class="review">리뷰<a href="#">(${item.reviewCount})</a></div>
						
						<div class="item-label-02">
							<c:if test="${item.itemLabel == '2'}">
							<span><img src="/content/image/icon/icon_new.gif" alt="신상품"></span>
							</c:if> 
						</div>
						--%>
						<c:if test="${item.itemLabel == '2' || item.itemType1 == '1' || item.itemType2 == '1' || item.itemType4 == '1' || item.itemType5 == '1'}">
							<div class="item-label-02">
								<c:if test="${item.itemLabel == '2'}">
									<span><img src="/content/images/common/label_new.gif" alt="신상품"></span>
								</c:if>
								<c:if test="${item.itemType1 == '1'}">
									<span><img src="/content/images/common/label_popular.gif" alt="인기상품"></span>
								</c:if>
								<c:if test="${item.itemType2 == '1'}">
									<span><img src="/content/images/common/label_best.gif" alt="BEST상품"></span>
								</c:if>
								<c:if test="${item.itemType4 == '1'}">
									<span><img src="/content/images/common/label_push.gif" alt="추천상품"></span>
								</c:if>
								<c:if test="${item.itemType5 == '1'}">
									<span><img src="/content/images/common/label_basic.gif" alt="기본상품"></span>
								</c:if>
							</div>
						</c:if>
						<c:if test="${(item.stockFlag == 'N' || (item.stockFlag == 'Y' and item.stockQuantity > 0)) && item.itemSoldOutFlag == 'N'}">
							<div class="item-btns">
								<c:choose>
									<c:when test="${(item.stockFlag == 'N' || (item.stockFlag == 'Y' and item.stockQuantity > 0)) && item.itemSoldOutFlag == 'N'}">
										<c:if test="${item.itemOptionFlag != 'Y' && item.itemType != '3'}">
											<button type="button" class="btn btn-s btn-submit" title="장바구니" onclick="Shop.addToCart('${fn:escapeXml(item.itemId)}', '${fn:escapeXml(item.orderMinQuantity)}', '${fn:escapeXml(item.nonmemberOrderType)}', '${fn:escapeXml(requestContext.userLogin)}', '${fn:escapeXml(requestContext.requestUri)}','${fn:escapeXml(item.itemType)}')"><strong>장바구니</strong></button>
										</c:if>
										<button type="button" class="btn btn-s btn-wish ${item.wishlistFlag?'on':''}" title="찜하기" onclick="Shop.addToWishList('${fn:escapeXml(item.itemId)}', '${fn:escapeXml(item.orderMinQuantity)}', '${fn:escapeXml(requestContext.userLogin)}', '${fn:escapeXml(requestContext.requestUri)}','0','${fn:escapeXml(item.itemType)}')"><strong>찜하기</strong></button>
									</c:when>
									<c:when test="${item.stockQuantity == 0 && item.soldOut == '1' && item.itemOptionFlag != 'Y'}">
										<%--
                                        <button type="button" class="btn btn-skyblue btn-min" onclick="Shop.applyForArrival('${item.itemId}', '${requestContext.userLogin}', '${requestContext.requestUri}')"><img src="/content/images/btn/btn_icon_mail.png" alt=""> 入荷通知申し込み</button>
                                         --%>
									</c:when>
									<c:otherwise>
										<%--  상세화면으로
                                        <button type="button" class="btn btn_detail btn-min" onclick="Shop.goItemDetails('${item.itemUserCode}', '${item.nonmemberOrderType}', '${requestContext.userLogin}')"><img src="/content/images/btn/btn_icon_view.png" alt=""> ${op:message('M01044')}</button>
                                         --%>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>
					</li>
				</c:forEach>
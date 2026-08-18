<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="inner">

	<div class="location_area">
		<jsp:include page="/WEB-INF/views/layouts/front/saleson_all_categories.jsp" />
		<div class="breadcrumbs">
			<a href="/" class="home"><span class="hide">home</span></a>
			<c:forEach items="${breadcrumbsForSelectbox}" var="codes" varStatus="i">

				<c:choose>
					<c:when test="${i.first}">

					</c:when>
					<c:otherwise>
						<span>
							<select title="">
								<c:forEach items="${codes}" var="code">
									<c:if test="${code.detail == '1'}">
										<option value="${code.value}" ${code.detail == '1' ? 'selected="selected"' : ''}>
												${code.label}
										</option>
									</c:if>
								</c:forEach>
								<optgroup label="--------------------">
									<c:forEach items="${codes}" var="code">
										<option value="${code.value}">
											${code.label}
										</option>
									</c:forEach>
								</optgroup>
							</select>
						</span>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</div>
	</div><!-- // location_area E -->
</div>

<c:if test="${not empty rankingList}">
	<div class="best_rank">
		<div class="inner">
			<h2>베스트 랭킹</h2>
			<div class="best_rank_inner">
				<div class="best-rank">
					<c:forEach var="item" items="${rankingList}" varStatus="i">
						<div class="slide">
							<a href="${fn:escapeXml(item.link)}" class="first redirect-item">
								<div style="display: none" class="item_data"
								     data-item-user-code="${fn:escapeXml(item.itemUserCode)}"
								     data-item-name="${fn:escapeXml(item.itemName)}"
								     data-brand="${fn:escapeXml(item.brand)}"
								     data-present-price="${fn:escapeXml(item.exceptUserDiscountPresentPrice)}"
								></div>
								<c:set var="rank" value="${i.index + 1 }"/>
								<span class="rank ${rank < 5? "rank_top": ""}">${fn:escapeXml(rank)}</span>
								<span class="thumbnail"><img src="${ shop:loadImage(item.itemUserCode, item.itemImage, 'S') }" alt="item"></span>
								<div class="cont">
									<p class="name">${fn:escapeXml(item.itemName)}</p>
									<c:if test="${item.totalDiscountAmount > 0 && item.discountRate > 0}">
										<span class="sale">${fn:escapeXml(item.discountRate)}<span>%</span></span>
									</c:if>
									<p class="price">
										<c:if test="${item.totalDiscountAmount > 0 && item.discountRate > 0}">
											<span class="before_price">${op:numberFormat(item.listPrice)}원</span>
										</c:if>
										<span class="sale_price">${op:numberFormat(item.exceptUserDiscountPresentPrice)}원</span>
									</p>
								</div><!-- // cont E -->
							</a>
						</div> <!-- // slide -->
					</c:forEach>
				</div> <!-- // mainRank E -->
			</div><!--// best_rank_inner E-->
		</div>	<!-- // inner E -->
	</div><!-- // best_rank E -->
</c:if>

<div class="inner">
	<form:form action="/categories/index/${fn:escapeXml(category.categoryUrl)}" modelAttribute="itemParam" method="get">
		<form:hidden path="orderBy" />
		<form:hidden path="sort" />
		<form:hidden path="listType" />
		<form:hidden path="fcIds" />
		<form:hidden path="maxPrice" />
		<form:hidden path="minPrice" />

		<div class="sort_area">
	 		<div class="sort_total">카테고리 내에  <span>${op:numberFormat(pagination.totalItems)}</span>개의 상품이 있습니다.</div>
			<button type="button" class="btn_filter">
				<p><span>FILTER</span></p>
			</button>
			<div class="category_filter">
				<dl>
					<dt>가격</dt>
					<dd id="priceFilter">
						<c:forEach items="${priceAreaList}" var="data" varStatus="i">
							<div class="sorting radio ${itemParam.minPrice == data.beginSalePrice && itemParam.maxPrice == data.endSalePrice ? 'op_on' : ''}">
								<input type="radio" id="price${i.index}" value="${fn:escapeXml(data.beginSalePrice)}_${fn:escapeXml(data.endSalePrice)}" class="custom"
									${itemParam.minPrice == data.beginSalePrice && itemParam.maxPrice == data.endSalePrice ? 'checked="checked"' : ''}>
								<label for="price${fn:escapeXml(i.index)}">
									<c:if test="${data.beginSalePrice == 0}">
										${op:numberFormat(data.endSalePrice)}원 이하
									</c:if>
									<c:if test="${data.beginSalePrice != 0 && data.endSalePrice != 0}">
										${op:numberFormat(data.beginSalePrice)}원 ~ ${op:numberFormat(data.endSalePrice)}원
									</c:if>
									<c:if test="${data.endSalePrice == 0}">
										${op:numberFormat(data.beginSalePrice)}원 이상
									</c:if>
								</label>
							</div>
						</c:forEach>
					</dd>
				</dl>

				<c:forEach items="${categoryFilterList}" var="group">
					<c:if test="${group.filterType == 'COLOR'}">
						<dl class="color">
							<dt>${fn:escapeXml(group.label)}</dt>
							<dd>
								<c:forEach items="${group.codeList}" var="codes">
									<div class="sorting chk ${codes.labelCode == '#ffffff' ? 'white' : ''}">
										<a href="javascript:void(0)" id="${fn:escapeXml(group.id)}_${fn:escapeXml(codes.id)}" class="custom ${fn:indexOf(itemParam.fcIds, codes.id) > -1 ? 'on' : ''}"
											   onClick="javascript:filterLink('${fn:escapeXml(group.id)}', '${fn:escapeXml(codes.id)}')">
											<span class="bullet" style="background:${fn:escapeXml(codes.labelCode)}"></span>
											<span class="txt">${fn:escapeXml(codes.label)}</span>
										</a>
									</div>
								</c:forEach>
							</dd>
						</dl>
					</c:if>
					<c:if test="${group.filterType == 'IMAGE'}">
						<dl class="kind">
							<dt>${fn:escapeXml(group.label)}</dt>
							<dd>
								<c:forEach items="${group.codeList}" var="codes">
									<div class="sorting">
										<a href="javascript:void(0)" class="${fn:indexOf(itemParam.fcIds, codes.id) > -1 ? 'on' : ''}"
										   onClick="javascript:filterLink('${fn:escapeXml(group.id)}', '${fn:escapeXml(codes.id)}')">
											<img src="${fn:escapeXml(codes.imageSrc)}" alt="${fn:escapeXml(codes.label)}">
											<span class="txt">${fn:escapeXml(codes.label)}</span>
										</a>
									</div>
								</c:forEach>
							</dd>
						</dl>
					</c:if>
					<c:if test="${group.filterType != 'COLOR' && group.filterType != 'IMAGE'}">
						<dl class="kind">
							<dt>${fn:escapeXml(group.label)}</dt>
							<dd>
								<c:forEach items="${group.codeList}" var="codes">
									<div class="sorting">
										<a href="javascript:void(0)" class="${fn:indexOf(itemParam.fcIds, codes.id) > -1 ? 'on' : ''}"
										   onClick="javascript:filterLink('${fn:escapeXml(group.id)}', '${fn:escapeXml(codes.id)}')">
											<span class="txt">${fn:escapeXml(codes.label)}</span>
										</a>
									</div>
								</c:forEach>
							</dd>
						</dl>
					</c:if>
				</c:forEach>

				<div class="filter_result">

					<c:forEach items="${priceAreaList}" var="data" varStatus="i">
						<c:if test="${itemParam.minPrice == data.beginSalePrice && itemParam.maxPrice == data.endSalePrice}">
							<div class="result">
								<span>
									<c:if test="${data.beginSalePrice == 0}">
										${op:numberFormat(data.endSalePrice)}원 이하
									</c:if>
									<c:if test="${data.beginSalePrice != 0 && data.endSalePrice != 0}">
										${op:numberFormat(data.beginSalePrice)}원 ~ ${op:numberFormat(data.endSalePrice)}원
									</c:if>
									<c:if test="${data.endSalePrice == 0}">
										${op:numberFormat(data.beginSalePrice)}원 이상
									</c:if>
								</span>
								<a href="javascript:filterReset();" class="close">
									<span class="hide">닫기 버튼</span>
								</a>
							</div>
						</c:if>
					</c:forEach>

					<c:forEach items="${categoryFilterList}" var="group">
						<c:forEach items="${group.codeList}" var="codes">
							<c:if test="${fn:indexOf(itemParam.fcIds, codes.id) > -1}">
								<div class="result">
									<span>${fn:escapeXml(codes.label)}</span>
									<a href="javascript:filterLink('${fn:escapeXml(group.id)}', '${fn:escapeXml(codes.id)}');" class="close">
										<span class="hide">닫기 버튼</span>
									</a>
								</div>
							</c:if>
						</c:forEach>
					</c:forEach>

					<div class="result_btn">
						<button type="button" class="btn_reset" onClick="javascript:filterReset('all')">초기화</button>
					</div>
				</div>
			</div>

			<c:if test="${!empty category.childCategories}">
				<div class="category_tab">
					<ul>
						<c:forEach items="${category.childCategories}" var="childCategory">
							<li><a href="${fn:escapeXml(childCategory.url)}">${fn:escapeXml(childCategory.name)}</a></li>
						</c:forEach>
					</ul>
				</div><!--//item_tab E-->
			</c:if>

			<div class="category_sort">
				<ul>
					<li><a href="javascript:itemSort('ORDERING', 'ASC')" id="ORDERING_ASC" rel="nofollow">${op:message('M00845')}</a></li> <!-- 신상품순 -->
					<li><a href="javascript:itemSort('HITS', 'DESC')" id="HITS_DESC" rel="nofollow">${op:message('M00846')}</a></li> <!-- 인기순 -->
					<li><a href="javascript:itemSort('SALE_PRICE', 'DESC')" id="SALE_PRICE_DESC" rel="nofollow">${op:message('M00847')}</a></li> <!-- 높은 가격순 -->
					<li><a href="javascript:itemSort('SALE_PRICE', 'ASC')" id="SALE_PRICE_ASC" rel="nofollow">${op:message('M00848')}</a></li> <!-- 낮은 가격순 -->
				</ul>
				<div class="sort_right">
					<form:select path="itemsPerPage">

						<c:forEach var="i" begin="20"  end="100" step="20" >
							<form:option value="${i }" label="${i }${op:message('M00844')}" /> <!-- 개 보기 -->
						</c:forEach>

					</form:select>
					<p class="type">
						<span><a href="javascript:itemListType('a')" id="list_type_a" rel="nofollow"><img src="/content/images/icon/sort_icon_view.gif" alt=""></a></span>
						<span><a href="javascript:itemListType('c')" id="list_type_c" rel="nofollow"><img src="/content/images/icon/sort_icon_blog.gif" alt=""></a></span>
					</p>
				</div>
			</div><!--//category_sort E-->

		 	 <c:set var="itemListTypeCss" value="thumb"/>
		 	 <c:if test='${itemParam.listType == "c" }'>
		 	 	<c:set var="itemListTypeCss" value="basic"/>
		 	 </c:if>

		 	 <c:if test="${not empty items}">
			 	 <div class="item-list ${itemListTypeCss }">
			 		<ul class="list-inner">
						<jsp:include page="../../include/item-list.jsp" />
			 		</ul>
			 	</div>
		 	</c:if>

		 	<c:if test="${empty items}">
		 		<div class="no_content">${op:message('M00778')} <!-- 상품이 없습니다. --></div>
		 	</c:if>

		 	<c:if test="${!empty items}">
			<page:pagination-seo />
			</c:if>

	 	</div>
	</form:form>
</div>

<jsp:include page="../../include/layer-cart.jsp" />
<jsp:include page="../../include/layer-wishlist.jsp" />

<page:javascript>
<script type="text/javascript">

var CATEGORY_NAME = '${category.categoryName}';
var LIST_NAME = '${categoryPath}';

$(function() {

	// 상품 UL 높이 맞춤.
	Shop.setHeightOfItemList('.item_list');

    // [SKC_20180712] 페이지 POST 전송
    Shop.handlePagination('#itemParam');




    // 검색조건 활성화
	itemSearchOptionActive();

	if ($('#color').val() != '') {
		$('#color_box').css('background', $('#color').val()).show();
	} else {
		$('#color_box').hide();
	}

	$('#color, #priceRange, #itemsPerPage').on('change', function() {
		$('#itemParam').submit();
	});

	$('.child-categories li').each(function() {
		if ($(this).height() > 15) {
			$(this).parent().addClass("col-3");
			return;
		}
	});

	// 상품 마우스 오버시 다른 이미지 보이기.
	$('.item_list a').on("mouseenter", function() {
		//$(this).find('.photo_area').animate({'opacity': '0.4'}, 'fast');
		//$(this).find('.item_extra_info').show('fast');
	}).on("mouseleave", function(){
		//$(this).find('.photo_area').animate({'opacity': '1'}, 'fast');
		//$(this).find('.item_extra_info').hide('fast');
	});

	// 상품평 좌우 이동 이벤트
	setReviewItemEvent();

	// google analytics
	googleAnalyticsEvent();
});


// 상품평 좌.우 이동.
function setReviewItemEvent() {
	var $reviewItem = $('.latest_list ul');
	var itemCount = $reviewItem.find('li').size();
	var currentPage = 1;
	var itemsPerPage = 3;
	var itemWidth = 295;
    var totalPages = Math.ceil(itemCount / itemsPerPage);

    $reviewItem.css('position', 'relative').width(itemWidth * itemsPerPage * totalPages);

    $('.arrow-prev').on('click', function(e) {
    	e.preventDefault();

    	if (totalPages > 1) {
    		if (currentPage == 1) {
    			//currentPage = totalPages;
    		} else {
    			currentPage--;

    			var topSize = (currentPage - 1) * itemWidth * itemsPerPage;
        		$reviewItem.animate({'left': '-' + topSize + 'px'}, 'fast');
    		}
    	}
    });

    $('.arrow-next').on('click', function(e) {
    	e.preventDefault();

    	if (totalPages > 1) {
    		if (currentPage == totalPages) {
    			//currentPage = 1;
    		} else {
    			currentPage++;

    			var topSize = (currentPage - 1) * itemWidth * itemsPerPage;
        		$reviewItem.animate({'left': '-' + topSize + 'px'}, 'fast');
    		}
    	}
    });

	$('#priceFilter > div > input[type=radio]').on('click', function(i) {
		if ($(i.target.parentElement).hasClass('op_on')) {
			$('#priceFilter > div').removeClass('op_on');
		} else {
			$('#priceFilter > div').removeClass('op_on');
			$(i.target.parentElement).toggleClass('op_on');
		}

		var startPrice = new Array();
		var endPrice = new Array();
		if ($('#priceFilter > div').hasClass('op_on')) {
			$.each($('#priceFilter > div'), function(i) {
				if ($('#priceFilter > div:eq('+i+')').hasClass('op_on')) {
					startPrice.push($('#priceFilter > div:eq('+i+') >').val().split('_')[0]);
					endPrice.push($('#priceFilter > div:eq('+i+') >').val().split('_')[1]);
				}
			});

			$('#minPrice').val(Math.min.apply(null, startPrice));
			$('#maxPrice').val(Math.max.apply(null, endPrice));

		}

		$('#itemParam').submit();

	});

}

// 검색조건 활성화
function itemSearchOptionActive() {
	var orderBy = '${itemParam.orderBy}';
	var sort = '${itemParam.sort}';
	var listType = 'list_type_${itemParam.listType}';

	$('#' + orderBy + '_' + sort).addClass('on');
	var $listTypeImage = $('#' + listType).find('img');
	var src = $listTypeImage.attr('src').replace(".gif", "_on.gif");
	$listTypeImage.attr('src', src);

}

// 상품 정렬.
function itemSort(orderBy, sortName) {
	var $form = $('#itemParam');
	$form.find('#orderBy').val(orderBy);
	$form.find('#sort').val(sortName);
	$form.submit();
}

// 리스트 타입
function itemListType(listType) {
	var $form = $('#itemParam');
	$form.find('#listType').val(listType);
	$form.submit();
}

function filterLink(groupId, filterCode) {
	var fcIds = $('#fcIds').val();
	if (fcIds.indexOf(filterCode) > -1) {
		var tempArray = fcIds.split(filterCode + "N");
		$('#fcIds').val(tempArray.join(''));
	} else {
		$('#fcIds').val(fcIds + filterCode + "N");
	}

	$('#itemParam').submit();
}

function filterReset(type) {
	if (type == 'all') {
		$('#fcIds').val('');
	}

	$('#minPrice').val(0);
	$('#maxPrice').val(0);

	$('#itemParam').submit();
}

function redirectItem(itemUserCode, link) {

	var selectorId = '#item-'+itemUserCode;
	var $selector = $(selectorId);

	if ($selector.size() > 0) {
		try {
			Ga.frontSelectBySelector($selector, LIST_NAME, CATEGORY_NAME);
		} catch (e) {console.log = () => {};}
	}

	location.href=link;
}

function googleAnalyticsEvent() {

	var $listSelector = $('.item-list');

	if ($listSelector.size() > 0) {
		try {
			Ga.frontImpressionBySelector($listSelector, LIST_NAME, CATEGORY_NAME);
		} catch (e) {console.log = () => {};}
	}
}

</script>
</page:javascript>

<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>

<div id="sub_contents_min">
		
		<div class="main_banner sub_banner">
			${fn:escapeXml(categoryEdit.header)}
		</div><!--//main_banner E-->
		
		<c:set var="groupName"></c:set>
		<!-- 중그룹 리스트 시작-->
		<div class="breadcrumbs">
			<c:forEach items="${shopContext.gnbCategories}" var="team">
				<c:forEach items="${team.groups}" var="group">
					<c:if test="${group.url == categoryCode}">
						<a href="/categories/index/${fn:escapeXml(team.url)}" class="home">${fn:escapeXml(team.name)}用品通販HOME</a> &gt; 
						<span>${fn:escapeXml(group.name)}</span>
						<c:set var="groupName">${fn:escapeXml(group.name)}</c:set>
					</c:if>
				</c:forEach>
			</c:forEach>
		</div>  <!-- // sub_location E-->
		
		<div class="middle_group">
			<c:forEach items="${shopContext.gnbCategories}" var="team">
				<c:forEach items="${team.groups}" var="group">
					<c:if test="${group.url == categoryCode}">
						<c:if test="${!empty group.categories}">
			
							<c:forEach items="${group.categories}" var="category">
								<div class="code_base_${fn:escapeXml(team.url)} code_${fn:escapeXml(category.url)}">
									<p class="i_massage"><a href="/categories/index/${fn:escapeXml(category.url)}">${fn:escapeXml(category.name)}</a></p>
									<%-- <p class="i_down_tit"><img src="/content/images/category_group/${category.url}_tit.png" alt="" /></p> --%>
									<c:if test="${!empty category.childCategories}">
										<ul>
											<c:forEach items="${category.childCategories}" var="childCategories">
											
												<li><a href="/categories/index/${fn:escapeXml(childCategories.url)}">${fn:escapeXml(childCategories.name)}</a>
											</c:forEach>
										</ul>
									</c:if>
								</div>
								
							</c:forEach>
						</c:if>
						
					</c:if>
				</c:forEach>
			</c:forEach>		
		</div> 
		
		<c:if test="${!empty categoryEdit.recommend}">
			<div class="free-space-content">
				${fn:escapeXml(categoryEdit.recommend)}
			</div>
		</c:if>
		
	<!--// 최근본 상품 시작-->
	<c:if test="${!empty shopContext.todayItems}">
	 	<h4>${op:message('M00854')}</h4>
		<div class="selling_rating">
			<ul>
				<c:forEach items="${shopContext.todayItems}" var="list" varStatus="i">
					<c:if test="${i.count <= 5}">
				    	<li>
				        	<a href="${fn:escapeXml(list.link)}" ${fn:escapeXml(list.noFollow)}><img src="${fn:escapeXml(list.imageSrc)}" class="item_list_image_min" alt="${fn:escapeXml(list.itemName)}" /></a>	
				         	<p><a href="${fn:escapeXml(list.link)}" ${fn:escapeXml(list.noFollow)}>${fn:escapeXml(list.itemName)}</a></p>
				     	</li>
			     	</c:if>
				</c:forEach>
			 </ul>
		</div><!--//selling_rating E-->
	</c:if>
 	<!--// 최근본 상품 끝-->
	
 	<!-- 히트상품랭킹1 시작 -->
 	<h4> ${fn:escapeXml(groupName)}売れ筋ランキング<span><a href="/ranking/${fn:escapeXml(categoryGroupCode)}_${fn:escapeXml(categoryTeamCode)}">${fn:escapeXml(groupName)}の売れ筋ランキング一覧</a></span></h4>
 	<div class="selling_ranking"> 
		<div class="ranking_content">
			<ul>
				<c:forEach items="${rankingList}" var="list" varStatus="i">
					<li>
						<span><img src="/content/images/main/hit_icon_${fn:escapeXml(i.count)}.png" alt=""></span>
						<a href="${fn:escapeXml(list.link)}" ${fn:escapeXml(list.noFollow)}><img src="${fn:escapeXml(list.imageSrc)}" class="item_list_image_min" alt="${fn:escapeXml(list.itemName)}" /></a>
						<p><a href="${fn:escapeXml(list.link)}" ${fn:escapeXml(list.noFollow)}>${fn:escapeXml(list.itemName)}</a></p>
					</li>
				</c:forEach>
			</ul>
		</div>	
	</div> <!--//selling_ranking E-->
	<!--// 히트상품랭킹1 끝 -->	
	
	<h4>${fn:escapeXml(groupName)}新着レビュー<span><a href="/product_reviews/recent">${fn:escapeXml(groupName)}レビュー一覧</a></span></h4>
	<div class="selling_rating">
		<ul style="position: relative;">
			<c:forEach items="${reviewList}" var="itemReview" varStatus="i">
		    	<li>
		        	<a href="${fn:escapeXml(itemReview.item.link)}" ${fn:escapeXml(itemReview.item.noFollow)}><img src="${fn:escapeXml(itemReview.item.imageSrc)}" class="item_list_image_min" alt="${fn:escapeXml(itemReview.item.itemName)}" /></a>
		        	<div class="star_rating">
		        		<span style="width:${fn:escapeXml(itemReview.score * 20)}%"></span><span class="point">${fn:escapeXml(itemReview.score)}</span>
			 			<!-- <span style="width:70%"></span><span class="point">5</span> -->
			 		</div>					                		
		         	<p><a href="${fn:escapeXml(itemReview.item.link)}" ${fn:escapeXml(itemReview.item.noFollow)}>${fn:escapeXml(itemReview.item.itemName)}</a></p>					         	
		     	</li>
			</c:forEach> 
		 </ul>
	</div><!--//selling_rating E-->
</div>		

<page:javascript>
<script type="text/javascript">
	$(function(){
		
		Gnb.active('${fn:escapeXml(categoryTeamCode)}');
		
		setReviewItemEvent();
		setGroupBg();
		
	});
	
	// 그룹 배경이미지 
	function setGroupBg() {
		var $groupDiv = $('.middle_group > div');
		
		var $thisSelector = [];
		var imgSrc = [];
		var image = [];
		$groupDiv.each(function(index) {
			var cls = $(this).attr('class').split(' ')[1].replace('code_', '');
			$thisSelector[index] = $(this);
			
			imgSrc[index] = '/content/images/category_group/' + cls + '.png';

			image[index] = new Image();
			image[index].onload = function () {
				$thisSelector[index].css('background-image', 'url(' + imgSrc[index] + ')');
			};
			image[index].src = imgSrc[index];
		});
		
	}
	
	function setReviewItemEvent() {
		
		var $newItem = $('.selling_rating .items ul');
		var itemCount = $newItem.find('li').size();
		var currentPage = 1;
		var itemsPerPage = 4;
		var itemWidth = 164;
	    var totalPages = Math.ceil(itemCount / itemsPerPage);
	    
	    $newItem.width(itemWidth * itemsPerPage * totalPages);
	    
	    $('.arrow-prev').on('click', function(e) {
	    	
	    	e.preventDefault();
	    	
	    	if (totalPages > 1) {
	    		if (currentPage == 1) {
	    			//currentPage = totalPages;
	    		} else {
	    			currentPage--;
	    			
	    			var topSize = (currentPage - 1) * itemWidth * totalPages;
	        		$newItem.animate({'left': '-' + topSize + 'px'}, 'fast');
	    		}
	    	} 
	    });
	    
	    $('.arrow-next').on('click', function(e) {
	    	e.preventDefault();
	    	
	    	if (totalPages > 1) {
	    		if (currentPage == itemsPerPage) {
	    			//currentPage = 1;
	    			
	    		} else {
	    			currentPage++;
	    			
	    			var topSize = (currentPage - 1) * itemWidth * totalPages;
	        		$newItem.animate({'left': '-' + topSize + 'px'}, 'fast');
	    		}
	    	} 
	    });
	}

</script>
</page:javascript>

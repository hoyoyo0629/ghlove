<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style>
	#reject-message-wrap {
		border: 1px solid #ccc;
		overflow-y: auto;
		height: 150px;

	}
	.reject-message-layer {
		position: fixed;
		width: 550px;
		margin-left: -210px;
		margin-top: -150px;
		left: 50%;
		top: 50%;
		border: 3px solid #434755;
		display: none;
		background: #fff;
	}
	.reject-message-layer h1 {
		border-top: 0;
	}
	.reject-message-layer p {
		padding-bottom: 10px;
	}
	.reject-message-layer .popup_contents {
		padding: 30px 20px !important;
	}
	.data-status-message {
		display: none;
	}
	.show-reject-message {
		text-decoration: underline;
	}
	.show-reject-message:hover {
		color: #067aac;
	}
</style>

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
					<col style="width: 220px" />
					<col style="width: auto;" />
					<col style="width: 220px" />
					<col style="width: auto;" />
				</colgroup>
				<tbody>
				<tr>
					<td class="label">${op:message('M00011')}</td>    <!-- 검색구분 -->
					<td colspan="3">
						<div>
							<form:select path="where" title="상세검색 선택" class="wd-150">
								<form:option value="">${op:message('M00039')} <!-- 전체 --></form:option>
								<form:option value="ITEM_NAME">${op:message('M00018')} <!-- 상품명 --></form:option>
								<form:option value="ITEM_USER_CODE">${op:message('M00783')} <!-- 상품코드 --></form:option>
								<form:option value="COMPANY_NAME">${op:message('M00104')} <!-- 상호명 --> </form:option>
								<%-- <form:option value="ITEM_SELLER_CODE">고유코드</form:option> --%>
							</form:select>
							<form:input path="query" class="wd-500" title="상세검색 입력" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">${op:message('M00202')} <!-- 등록일 --></td>
					<td colspan="3">
						<div>
							<span class="datepicker"><form:input path="searchStartDate" maxlength="8" class="datepicker" title="${op:message('M00024')}" /><!-- 주문일자 시작일 --></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="searchEndDate" maxlength="8" class="datepicker" title="${op:message('M00025')}" /><!-- 주문일자 종료일 --></span>
							<span class="day_btns">
								<a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
								<a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
								<a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
								<a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
								<a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
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

							<form:select path="categoryClass3" class="category hidden">
							</form:select>

							<form:select path="categoryClass4" class="category hidden">
							</form:select>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">${op:message('M00787')} <!-- 판매상태 --></td>
					<td colspan="3">
						<div class="flex_box gap-12">
							<div class="input-form">
								<form:radiobutton path="dataStatusCode" value="" label="${op:message('M00039')}" checked="checked" /> <!-- 전체 -->
							</div>
							<div class="input-form">
								<form:radiobutton path="dataStatusCode" value="20" label="등록대기" />
							</div>
							<div class="input-form">
								<form:radiobutton path="dataStatusCode" value="30" label="재등록신청" />
							</div>
							<div class="input-form">
								<form:radiobutton path="dataStatusCode" value="21" label="등록보류" />
							</div>
							<div class="input-form">
								<form:radiobutton path="dataStatusCode" value="40" label="삭제대기" />
							</div>
						</div>
					</td>
				</tr>
				<%-- 1.일반 --%>
				<c:if test="${op:property('saleson.mall.type') == '1'}">
					<tr class="hidden">
						<td class="label">상품구분</td>
						<td colspan="3">
							<div>
								<p>
									<form:radiobutton path="itemType" value="" checked="checked" label="${op:message('M00039')}" /> <!-- 전체 -->
									<form:radiobutton path="itemType" value="1" label="일반상품" />
									<form:radiobutton path="itemType" value="2" label="사업자상품" />
								</p>
							</div>
						</td>
					</tr>
				</c:if>

				<tr class="hidden">
					<td class="label">담당MD명</td>
					<td>
						<div>
							<input type="hidden" name="currentMdId" value="${fn:escapeXml(seller.mdId)}" />
							<input type="hidden" id="mdId" name="mdId" value="${fn:escapeXml(seller.mdId)}" />
							<form:input path="mdName" title="담당MD" readonly="true" />

							<button type="button" onclick="findMd('mdId')" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> MD검색</button>
							<button type="button" onclick="clearMd('mdId')" class="btn btn-gradient btn-sm"><span class="glyphicon glyphicon-remove"></span> 초기화</button>
						</div>
					</td>
					<td class="label">${op:message('M00784')} <!-- 가격 --></td>
					<td >
						<div>
							<form:select path="priceRange" title="가격대 선택">
								<option value="">${op:message('M00039')} <!-- 전체 --></option>
								<form:option value="0|10000" label="0～10,000" />
								<form:option value="10000|50000" label="10,000～50,000" />
								<form:option value="50000|100000" label="50,000～100,000" />
								<form:option value="100000|1000000" label="100,000～1,000,000" />
								<form:option value="1000000|90000000" label="1,000,000～" />
							</form:select>
						</div>
					</td>
				</tr>
				<tr class="hidden">
					<td class="label">${op:message('M00191')} <!-- 공개유무 --></td>
					<td>
						<div>
							<p>
								<form:radiobutton path="displayFlag" value="" label="${op:message('M00039')}" /> <!-- 전체 -->
								<form:radiobutton path="displayFlag" value="Y" label="${op:message('M00096')}" /> <!-- 공개 -->
								<form:radiobutton path="displayFlag" value="N" label="${op:message('M00097')}" /> <!-- 비공개 -->
							</p>
						</div>
					</td>
					<td class="label">배송구분</td>
					<td>
						<div>
							<form:radiobutton path="deliveryType" value="" label="${op:message('M00039')}" checked="checked" /> <!-- 전체 -->
							<form:radiobutton path="deliveryType" value="1" label="본사배송" /> <!-- 인덱스 시킨다. -->
							<form:radiobutton path="deliveryType" value="2" label="업체배송" /> <!-- 인덱스 시키지 않는다. -->
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/seller/item/pending/list';"> ${op:message('M00047')}</button> <!-- 초기화 -->
				<button type="submit" class="btn btn-dark-gray btn-mini"> ${op:message('M00048')}</button> <!-- 검색 -->
			</div>
		</div>

		<div class="count_title mt40">
			<h5>
				${op:message('M00045')}  ${op:numberFormat(pagination.totalItems)} ${op:message('M00272')}
			</h5>	 <!-- 전체 -->   <!-- 건 조회 -->
			<span>
				<form:select path="orderBy" title="등록일선택" class="hidden">
					<form:option value="ITEM_ID" label="${op:message('M00202')}" /> <!-- 등록일 -->

					<c:if test="${!empty itemParam.categoryId}">
						<form:option value="ORDERING" label="${op:message('M00790')}" /> <!-- 정렬 -->
					</c:if>

					<form:option value="SALE_PRICE" label="${op:message('M00786')}" /> <!-- 판매가격 -->
					<form:option value="HITS" label="${op:message('M00685')}" /> <!-- 조회수 -->
				</form:select>
				<form:select path="sort" title="검색방법 선택" class="hidden">
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
					<button type="button" id="change_ordering2" class="btn ctrl_btn hidden" style="position:fixed;right:0; bottom:260px; z-index: 1000;">${op:message('M00791')}</button> <!-- 정렬순서변경 -->
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
	                <col style="width:50px;">
	                <col style="width:50px;">
	                <col style="width:100px;">
	                <col style="width:150px;">
	                <col style="width:400px;">
	                <col style="width:200px;">
	                <col style="width:50px;">
	                <col style="width:50px;"><!-- 20260325 필수 추가정보_ isb 수정 참고 -->
	                <col style="width:100px;">
	                <col style="width:100px;">
	                <col style="width:200px;">
	                <col style="width:100px;" class="hidden">
	                <col style="width:150px;">
				</colgroup>
				<thead>
					<tr>
						<th><input type="checkbox" id="check_all" title="체크박스" /></th>
						<th>No</th> <!-- 순번 -->
						<th>승인상태 <!-- 승인상태 --></th>
						<th>${op:message('M00752')}</th> <!-- 이미지 -->
						<th>${op:message('M00018')}</th> <!-- 상품명 -->
						<th>${op:message('M00786')} <!-- 판매가격 --></th>
						<th>옵션</th>
						<th>필수추가정보</th><!-- 20260325 필수 추가정보_ isb 수정 참고 -->
						<th>${op:message('M01462')} <!-- 재고수 --></th>
						<th>${op:message('M00191')} <!-- 공개유무 --></th>
						<th>
							<c:choose>
								<c:when test="${itemParam.orderBy == 'HITS'}">
									${op:message('M00685')} <!-- 조회수 -->
								</c:when>
								<c:otherwise>
									${op:message('M00202')} <!-- 등록일 -->
								</c:otherwise>
							</c:choose>
						</th>
						<th class="hidden">변경사항</th>
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
							<c:when test="${item.dataStatusCode == '20' || item.dataStatusCode == '31' || item.dataStatusCode == '41'}">
								<c:set var="itemSaleStatusText">등록대기</c:set>
							</c:when>
							<c:when test="${item.dataStatusCode == '21'}">
								<c:set var="itemSaleStatusText"><a href="#" class="show-reject-message txt_line">등록보류</a></c:set>
							</c:when>
							<c:when test="${item.dataStatusCode == '30'}">
								<c:set var="itemSaleStatusText">재등록신청</c:set>
							</c:when>
							<c:when test="${item.dataStatusCode == '40'}">
								<c:set var="itemSaleStatusText">삭제대기</c:set>
							</c:when>
							<c:when test="${item.dataStatusCode == '90'}">
								<c:set var="itemSaleStatusText">판매종료</c:set>
							</c:when>
							<c:otherwise>
								<c:set var="itemSaleStatusText">${op:message('M00694')}</c:set> <!-- 판매중 -->
							</c:otherwise>
						</c:choose>

						<tr>
							<td><input type="checkbox" name="id" value="${fn:escapeXml(item.itemId)}" class="${fn:escapeXml(item.itemUserCode)}" title="" /></td>
							<td>
								<c:choose>
									<c:when test="${itemParam.orderBy == 'ORDERING' && itemParam.sort == 'ASC'}">
										${pagination.number + i.count}
									</c:when>
									<c:otherwise>
										${pagination.itemNumber - i.count}
									</c:otherwise>
								</c:choose>
								<%-- <p style="padding-top: 5px;">
									<c:choose>
										<c:when test='${op:property("saleson.view.type") eq "api"}'>
											<a href="${op:property("saleson.url.frontend")}/items/details.html?code=${item.itemUserCode}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
										</c:when>
										<c:otherwise>
											<a href="/products/preview/${item.itemUserCode}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
											<a href="/m/products/preview/${item.itemUserCode}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_mobile.gif" alt="" /></a>
										</c:otherwise>
									</c:choose>
								</p> --%>
							</td>
							<td>
								${op:nl2br(itemSaleStatusText)}
								<div class="data-status-message">
									${op:nl2br(item.dataStatusMessage)}
								</div>
							</td>
							<td>
								<div>
									<a href="javascript:Link.view('/seller/item/edit/${fn:escapeXml(item.itemUserCode)}')"><img src="${ shop:loadImage(item.itemUserCode, item.itemImage, 'XS') }" class="item_image" alt="상품이미지" /></a>
								</div>
							</td>
							<td class="left break-word">
								<a href="javascript:Link.view('/seller/item/edit/${fn:escapeXml(item.itemUserCode)}')" class="break-word">
									[${fn:escapeXml(item.itemUserCode)}]<br/>
									<c:if test="${item.itemSellerCode != '' && item.itemSellerCode != null}">
										(${fn:escapeXml(item.itemSellerCode)})<br>
									</c:if>
									${fn:escapeXml(item.itemName)}
								</a>
								<a href="javascript:Link.view('/seller/item/edit/${fn:escapeXml(item.itemUserCode)}', 1)" class="break-word" style="color:#517BAB">[새탭]</a>
								<c:if test="${item.itemLabel == '2'}">
									<img src="/content/opmanager/images/icon/icon_new2.gif" alt="new" />
								</c:if>
							</td>
							<td>
								${op:numberFormat(item.salePrice)}
							</td>
							<td>${fn:escapeXml(item.itemOptionFlag)}</td>
							<td>${fn:escapeXml(item.itemTextOptionFlag)}</td><!-- 20260325 필수 추가정보_ isb 수정 -->
							<td>
								<c:choose>
									<c:when test="${item.stockQuantity == -1}">
										${op:message('M01497')} <!-- 무제한 -->
									</c:when>
									<c:otherwise>
										${op:numberFormat(item.stockQuantity)}개
									</c:otherwise>
								</c:choose>
							</td>
							<td>${op:nl2br(displayFlagText)}</td>
							<td>
								<c:choose>
									<c:when test="${itemParam.orderBy == 'HITS'}">
										${op:numberFormat(item.hits)}
									</c:when>
									<c:otherwise>
										${op:date(item.createdDate)}
									</c:otherwise>
								</c:choose>
							</td>
							<td class="hidden">
								<div class="flex_box juc-center">
									<button type="button" class="btn btn-default btn-sm" onclick="javascript:Manager.sellerItemLog('${fn:escapeXml(item.itemId)}')">보기</button>
								</div>
							</td>
							<td>
								<c:if test="${itemSaleStatusText == '판매중'}">
									<a href="javascript:Link.view('/seller/item/sale/edit/${fn:escapeXml(item.itemId)}')" class="btn btn-gradient btn-xs hidden">가격변경</a><br />
								</c:if>
<%-- 								<a href="/seller/item/copy/${fn:escapeXml(item.itemId)}" class="btn btn-default btn-xs">${op:message('M00788')}</a><br /> <!-- 복사 --> --%>
								<a href="#" class="delete_item btn btn-default btn-xs">${op:message('M00074')}</a> <!-- 삭제 -->
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



		<div class="flex_box juc-sbt mt15">
			<div>
				<div class="flex_box gap-08">

				</div>
			</div>
			<div>
				<div class="flex_box gap-08">
					<a href="/seller/item/create" class="btn btn-dark-gray btn-mini"> ${op:message('M00773')}</a> <!-- 상품등록 -->
				</div>
			</div>
		</div>

		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>

	</div> <!-- // board_list -->

	<div class="reject-message-layer popup_wrap">
		<div id="pop_header">
			<h1 class="popup_title text-center">등록보류 처리</h1>
			<a href="javascript:self.close();" class="btn_close cancel-reject"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

		<div class="popup_contents">
            <table class="board_write_table" summary="등록보류 처리">
                <caption>등록보류 처리</caption>
                <colgroup>
                    <col style="width: 100px" />
                    <col />
                </colgroup>
                <tbody>
                    <tr>
                        <td class="label">사유</td>
                        <td>
                            <div>
                                <span class="placeholder_wrap">
                                    <textarea id="reject-message-wrap" name="reject-message-wrap" readonly></textarea>
                                </span>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
			<div class="btn_all btn_center">
				<div class="flex_box gap-08">
					<button type="button" class="close-reject-message btn btn-active">확인</button>
				</div>
			</div>
		</div>
		<a href="#" class="popup_close close-reject-message">창 닫기</a>
	</div>
</div>

<style>
	td {background: #fff;}
	.sortable-placeholder td{height: 100px; background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
		opacity: .5;}



</style>


<page:javascript>
	<script type="text/javascript">
        $(function() {
            $('.show-reject-message').on('click', function(e) {
                e.preventDefault();
                var rejectMessage = $(this).closest('tr').find('.data-status-message').html();
	            var sortRejectMessage = "";

				let substrings = rejectMessage.split('<br>');

				for(var i=substrings.length-2; i>=0; i--) {
					sortRejectMessage = sortRejectMessage + substrings[i];

				}

                $('#reject-message-wrap').val(sortRejectMessage.replace('\n',''));
                $('.reject-message-layer').show();
            });

            $('.close-reject-message').on('click', function() {
                $('#reject-message-wrap').empty();
                $('.reject-message-layer').hide();
            });

            if (isChangingOrdering()) {
                // 관련상품 drag sortable
                $( ".sortable" ).sortable({
                    placeholder: "sortable-placeholder"
                });
                $( ".sortable" ).disableSelection();

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

                Common.updateListData("/seller/item/list/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
            });

            // 목록데이터 - 삭제처리
            $('#delete_list_data').on('click', function() {
                Common.updateListData("/seller/item/list/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
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
                    Common.updateListData("/seller/item/list/update", Message.get("M00307"));	// 선택된 데이터를 수정하시겠습니까?
                }
            });

            // 목록데이터 - 품절처리
            $('#update_list_data_soldout').on('click', function() {
                Common.updateListData("/seller/item/list/update-sold-out", "선택한 상품을 품절로 처리하시겠습니까?");	// 선택된 데이터를 수정하시겠습니까?
            });

            // 목록데이터 - 품절해제(재판매)
            $('#update_list_data_soldout').on('click', function() {
                Common.updateListData("/seller/item/list/update-resale", "선택한 상품을 품절해제로 처리하시겠습니까?");	// 선택된 데이터를 수정하시겠습니까?
            });

            // 목록데이터 - 품절해제(재판매)
            $('#update_list_data_add_category').on('click', function() {
                var $form = $('#listForm');
                if ($form.find('input[name=id]:checked').size() == 0) {
                    alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                    return;
                }

                Common.popup('/seller/item/add-items-to-category', 'add-items-to-category', 970, 420);
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
                    Common.changeListOrdering('/seller/item/list/change-ordering');
                }

            });

            // 팀/그룹 ~ 4차 카테고리 이벤트
            ShopEventHandler.categorySelectboxChagneEvent();
            Shop.activeCategoryClass('${fn:escapeXml(itemParam.categoryGroupId)}', '${fn:escapeXml(itemParam.categoryClass1)}', '${fn:escapeXml(itemParam.categoryClass2)}', '${fn:escapeXml(itemParam.categoryClass3)}', '${fn:escapeXml(itemParam.categoryClass4)}');

            Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
			EventHandler.calendarStartDateAndEndDateVaild();


        });

        function shoppingHow() {
            Common.popup("/seller/item/make-shopping-how", 'makeShoppingHow', 500, 300, 1);
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
            $.post("/seller/item/deferred-result", {}, function(text) {
                alert(text);
            }, 'text');
        }

        //엑셀 다운로드 팝업.
        function downloadExcel() {
            var deliveryType = '${fn:escapeXml(itemParam.deliveryType)}';

            if (deliveryType == '') {
                alert('배송구분을 검색후 다운로드하세요.');
                return false;
            }

			Common.popup("/opmanager/item/download-excel?" + $("#itemParam").serialize(), "download-excel", 600, 550, 0);
        }

        // 엑셀 업로드
        function uploadExcel() {
            Common.popup('/opmanager/item/upload-excel', 'upload-excel', 600, 550, 0);
        }

        function updateListDataDisplay(flag) {

            if ($('#listForm').find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            } else {
                Common.updateListData("/seller/item/list/update-display/" + flag, "선택된 데이터를 수정하시겠습니까?");
            }
        }

        function updateListDataLabel(flag) {

            if ($('#listForm').find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            } else {
                Common.updateListData("/seller/item/list/update-label/" + flag, "선택된 데이터를 수정하시겠습니까?");
            }
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
	</script>
</page:javascript>

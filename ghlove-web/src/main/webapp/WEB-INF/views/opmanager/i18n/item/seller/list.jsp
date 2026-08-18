<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<style>
	.layer_wrap{
		position: relative;
	}
	.reject-message {
		position: absolute;
		bottom: 0;
		right: 0;
		width:500px;
		border: 3px solid #434755;
		display: none;
		background: #fff;
	}
	.reject-message h1 {
		border-top: 0;
	}
	.reject-message p {
		padding-bottom: 10px;
	}
	.reject-message .popup_contents {
		padding: 30px 20px !important;
	}



	#reject-message-wrap {
		border: 1px solid #ccc;
		overflow-y: auto;
		height: 100px;

	}
	.reject-message-layer {
		position: fixed;
		width: 420px;
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

	<form:form modelAttribute="itemParam" method="get">
		<form:hidden path="categoryId" />

		<div class="board_write">
			<table class="board_write_table" summary="상품리스트">
				<caption>상품리스트</caption>
				<colgroup>
					<col style="width: 150px" />
					<col style="" />
					<col style="width: 150px" />
					<col style="width: 400px" />

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
						<div>
							<form:select path="where" title="상세검색 선택" class="wd-150">
								<form:option value="">${op:message('M00039')} <!-- 전체 --></form:option>
								<form:option value="ITEM_NAME">${op:message('M00018')} <!-- 상품명 --></form:option>
								<form:option value="ITEM_USER_CODE">${op:message('M00783')} <!-- 상품코드 --></form:option>
								<form:option value="COMPANY_NAME">${op:message('M00104')} <!-- 상호명 --> </form:option>
								<%-- <form:option value="ITEM_SELLER_CODE" label="고유코드" /> --%>
							</form:select>
							<form:input path="query" class="wd-500" title="상세검색 입력" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">수정일</td>
					<td colspan="3">
						<div>
							<span class="datepicker"><form:input path="searchStartDate" maxlength="8" class="datepicker" title="${op:message('M00024')}" /><!-- 주문일자 시작일 --></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="searchEndDate" maxlength="8" class="datepicker" title="${op:message('M00025')}" /><!-- 주문일자 종료일 --></span>
							<span class="day_btns">
												<!-- <a href="javascript:;" class="btn_date clear">전체</a> -->
												<a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
												<a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
												<a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
												<a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
												<a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
                                                <c:choose>
                                                    <c:when test="${op:hasRole('ROLE_ADMIN_CALL')}">
                                                        <a href="javascript:;" class="btn_date all-1"><c:out value="${op:message('M00039')}"/></a><!-- 전체 -->
                                                    </c:when>
                                                </c:choose>
											</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">상태</td>
					<td>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/item/seller/list';"> ${op:message('M00047')}</button> <!-- 초기화 -->
				<%-- <button type="submit" class="btn btn-dark-gray btn-mini"> ${op:message('M00048')}</button> <!-- 검색 --> --%>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>

		<div class="count_title mt-40">
			<h5>
					${op:message('M00045')} ${op:numberFormat(pagination.totalItems)} ${op:message('M00272')}
			</h5>	 <!-- 전체 -->   <!-- 건 조회 -->
			<span>

							<form:select path="itemsPerPage" title="출력수 선택">
								<form:option value="10" label="10개 출력" />
								<form:option value="20" label="20개 출력" />
								<form:option value="30" label="30개 출력" />
								<form:option value="50" label="50개 출력" />
								<form:option value="100" label="100개 출력" />
								<form:option value="500" label="500개 출력" />
								<form:option value="1000" label="1000개 출력" />
							</form:select>


						</span>
		</div>
	</form:form>

	<div class="board_list">
		<form id="listForm">
			<input type="hidden" id="processType" name="processType" />
			<input type="hidden" id="approvalMessage" name="approvalMessage" />
			<table class="board_list_table" summary="전체상품리스트">
				<caption>전체상품리스트</caption>
				<colgroup>
                    <col style="width:50px;">
                    <col style="width:50px;">
                    <c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
                    <col style="width:200px;">
                    </c:if>
                    <col style="width:150px;">
                    <col style="width:100px;">
                    <col style="width:500px;">
                    <col style="width:200px;">
                    <col style="width:100px;">
                    <col style="width:100px;">
                    <col style="width:100px;">
                    <col style="width:200px;">
                    <col style="width:100px;">
				</colgroup>
				<thead>
				<tr>
					<th><input type="checkbox" id="check_all" title="체크박스" /></th>
					<th>No</th> <!-- 순번 -->
					<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
					<th>지자체</th>
					</c:if>
					<th>상태</th>
					<th>${op:message('M00752')}</th> <!-- 이미지 -->
					<th>상품</th>
					<th>${op:message('M00786')} <!-- 판매가격 --></th>
					<th>옵션</th>
					<th>${op:message('M01462')} <!-- 재고수 --></th>
					<th>${op:message('M00191')} <!-- 공개유무 --></th>
					<th>
						<c:choose>
							<c:when test="${itemParam.orderBy == 'HITS'}">
								${op:message('M01635')}/${op:message('M00685')} <!-- 상호/조회수 -->
							</c:when>
							<c:otherwise>
								${op:message('M01635')}/${op:message('M00202')} <!-- 상호/등록일 -->
							</c:otherwise>
						</c:choose>
					</th>
					<th>미리보기</th>
					<th>변경사항</th>
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
							<c:set var="itemSaleStatusText">등록대기<input type="hidden" id="itemSaleStatusText_${fn:escapeXml(item.itemUserCode) }" value="20" /></c:set>
						</c:when>
						<c:when test="${item.dataStatusCode == '21'}">
							<c:set var="itemSaleStatusText"><a href="#" class="show-reject-message txt_line">등록보류</a><input type="hidden" id="itemSaleStatusText_${fn:escapeXml(item.itemUserCode) }" value="21" /></c:set>
						</c:when>
						<c:when test="${item.dataStatusCode == '30'}">
							<c:set var="itemSaleStatusText">재등록신청<input type="hidden" id="itemSaleStatusText_${fn:escapeXml(item.itemUserCode) }" value="30" /></c:set>
						</c:when>
						<c:when test="${item.dataStatusCode == '40'}">
							<c:set var="itemSaleStatusText">삭제대기<input type="hidden" id="itemSaleStatusText_${fn:escapeXml(item.itemUserCode) }" value="40" /></c:set>
						</c:when>
						<c:when test="${item.dataStatusCode == '90'}">
							<c:set var="itemSaleStatusText">판매종료<input type="hidden" id="itemSaleStatusText_${fn:escapeXml(item.itemUserCode) }" value="90" /></c:set>
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
						<c:if test="${requestContext.sellerPage == false && adminRole == 'SYS'}">
						<td>
							${fn:escapeXml(item.locgovNm)}
						</td>
						</c:if>
						<td>
							${op:nl2br(itemSaleStatusText)}
							<div class="data-status-message">
									${op:nl2br(item.dataStatusMessage)}
							</div>
						</td>
						<td>
							<div>
							<!--
								<c:choose>
									<c:when test='${op:property("saleson.view.type") eq "api"}'>
										<a href="${op:property("saleson.url.frontend")}/items/details.html?code=${fn:escapeXml(item.itemUserCode)}" target="_blank"><img src="/content/opmanager/images/icon/icon_preview_pc.gif" alt="" /></a>
									</c:when>
									<c:otherwise>
										<a href="/products/preview/${fn:escapeXml(item.itemUserCode)}" target="_blank"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image" alt="상품이미지" /></a>
									</c:otherwise>
								</c:choose>
							-->
								<a href="javascript:Link.view('/opmanager/item/seller/edit/${fn:escapeXml(item.itemUserCode)}')"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image" alt="상품이미지" /></a>
							</div>
						</td>
						<td class="left break-word">

							<a href="javascript:Link.view('/opmanager/item/seller/edit/${fn:escapeXml(item.itemUserCode)}')" class="break-word">
								[${fn:escapeXml(item.itemUserCode)}]<br/>
								<c:if test="${item.itemSellerCode != '' && item.itemSellerCode != null}">
									(${fn:escapeXml(item.itemSellerCode)})<br>
								</c:if>
								${fn:escapeXml(item.itemName)}</a>
							<a href="javascript:Link.view('/opmanager/item/seller/edit/${fn:escapeXml(item.itemUserCode)}', 1)" class="break-word" style="color:#517BAB">[새탭]</a>
						</td>

						<td class="text-center">
							${op:numberFormat(item.salePrice)}P
						</td>
						<td>${fn:escapeXml(item.itemOptionFlag)}</td>
						<td>
							<c:choose>
								<c:when test="${item.stockFlag == 'Y'}">
									<c:choose>
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

						<td>${op:nl2br(displayFlagText)}</td>

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

						<td>
							<div class="flex_box juc-center">
								<button type="button" class="btn btn-default btn-sm" onclick="javascript:openPreview('${fn:escapeXml(item.itemUserCode)}')">미리보기</button>
							</div>
						</td>

						<td>
							<div class="flex_box juc-center">
								<button type="button" class="btn btn-default btn-sm" onclick="javascript:Manager.itemLog('${fn:escapeXml(item.itemId)}')">보기</button>
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

		<div class="layer_wrap">
			<div class="btn_all btn_right">
				<div class="flex_box gap-08">
					<button type="button" id="update_list_data_reject" class="btn btn-default btn-mini">등록보류</button>
					<button type="button" id="update_list_data" class="btn btn-dark-gray btn-mini">승인처리</button>
				</div>
			</div>
			<div class="reject-message popup_wrap">
				<div id="pop_header">
					<h1 class="popup_title">등록보류 처리</h1>
					<a href="javascript:self.close();" class="btn_close cancel-reject"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
				</div>

				<div class="popup_contents">
		            <table class="board_write_table" summary="등록보류 처리">
		                <caption>등록보류 처리</caption>
		                <colgroup>
		                    <col style="width: 150px" />
		                    <col />
		                </colgroup>
		                <tbody>
		                    <tr>
		                        <td class="label">사유</td>
		                        <td>
		                            <div>
		                                <span class="placeholder_wrap">
		                                    <textarea id="rejectMessage" name="rejectMessage"></textarea>
		                                </span>
		                            </div>
		                        </td>
		                    </tr>
		                </tbody>
		            </table>
					<div class="btn_all btn_center">
						<div class="flex_box gap-08">
							<button type="button" id="update_list_data_reject_process" class="btn btn-active">등록보류처리</button>
							<button type="button" class="cancel-reject btn btn-default">취소</button>
						</div>
					</div>
				</div>

			</div>
		</div>


		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>

	</div> <!-- // board_list -->

	<div class="reject-message-layer popup_wrap">
		<div id="pop_header">
			<h1 class="popup_title">등록보류</h1>
			<a href="javascript:self.close();" class="btn_close cancel-reject"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

		<div class="popup_contents">
			<div class="text_box">
				<div id="reject-message-wrap"></div>
			</div>

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
<script type="text/javascript">
    $(function() {
        $('.show-reject-message').on('click', function(e) {
            e.preventDefault();
            var rejectMessage = $(this).closest('tr').find('.data-status-message').html().trim();
            var sortRejectMessage = "";

			let substrings = rejectMessage.split('<br>');

			for(var i=substrings.length-2; i>=0; i--) {
				sortRejectMessage = sortRejectMessage + substrings[i] + '<br>';

			}

            $('#reject-message-wrap').html(sortRejectMessage);
            $('.reject-message-layer').show();
            $('.reject-message').hide();
        });

        $('.close-reject-message').on('click', function() {
            $('#reject-message-wrap').empty();
            $('.reject-message-layer').hide();
        });


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
            $('#processType').val('');

            $('#check_all').prop("checked", false);
            $(this).closest("table").find('input[name=id]:enabled').prop("checked", false);
            $(this).closest("tr").find('input[name=id]').prop("checked", true);

            Common.updateListData("/opmanager/item/list/delete", Message.get("M00306"), function(response) {
            	console.log(response);
            });	// 선택된 데이터를 삭제하시겠습니까?
        });


        // 목록데이터 - 승인처리
        $('#update_list_data').on('click', function() {

            var $form = $('#listForm');
            if ($form.find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }

            $('#processType').val('approval');
            Common.updateListData("/opmanager/item/list/update", "판매자가 등록(수정)한 상품 정보를 정확히 확인된 경우에만 승인처리를 하시기 바랍니다.\n\n선택한 상품을 승인처리 하시겠습니까?");

        });

        // 목록데이터 - 등록보류
        $('#update_list_data_reject').on('click', function() {

            var $form = $('#listForm');
            let checkedList = $form.find('input[name=id]:checked');
            if (checkedList.size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }

            let length = checkedList.length;
            for(let i = 0 ; i < length ; i++) {
            	var itemUserCode = checkedList[i].getAttribute("class");
            	var itemSaleStatusText = $('#itemSaleStatusText_'+itemUserCode).val();
            	 if(itemSaleStatusText == "40") {
                     alert('선택한 답례품의 상태가 등록대기 혹은 재등록신청 상태가 아닙니다.');
                     return;
                 }
            }

            $('.reject-message').show();
            $('.reject-message-layer').hide();
            $('#approvalMessage').val("");
            $('#rejectMessage').val("");
            $('#processType').val('reject');

        });

        // 목록데이터 - 등록보류 전송
        $('#update_list_data_reject_process').on('click', function() {

            var $form = $('#listForm');
            if ($form.find('input[name=id]:checked').size() == 0) {
                alert(Message.get("M00308"));	// 처리할 항목을 선택해 주세요.
                return;
            }

            if ($.trim($('#rejectMessage').val()) == '') {
                alert("등록보류 사유를 입력해 주세요.");
                $('#rejectMessage').focus();
                return;
            }

            $('#processType').val('reject');
            $('#approvalMessage').val($('#rejectMessage').val());
            Common.updateListData("/opmanager/item/list/update", "선택한 상품을 등록보류로 처리하시겠습니까?");
            $('.reject-message').hide();
        });


        $('.cancel-reject').on('click', function() {
            $('#approvalMessage').val("");
            $('#rejectMessage').val("");
            $('#processType').val('');
            $('.reject-message').hide();
        });





        // 팀/그룹 ~ 4차 카테고리 이벤트
        //ShopEventHandler.categorySelectboxChagneEvent();
        //Shop.activeCategoryClass('${fn:escapeXml(itemParam.categoryGroupId)}', '${fn:escapeXml(itemParam.categoryClass1)}', '${fn:escapeXml(itemParam.categoryClass2)}', '${fn:escapeXml(itemParam.categoryClass3)}', '${fn:escapeXml(itemParam.categoryClass4)}');

        Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
		EventHandler.calendarStartDateAndEndDateVaild();


    });
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
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {

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

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				$("#searchEndDate").focus();
				return false;
			}
			var searchChk = Common.searchDateMonth(startDate, endDate);
			if(!searchChk) return false;
		}

		$("#itemParam").submit();
	}

	function openPreview(itemUserCode){
		Common.popup('/opmanager/item/seller/preview?itemUserCode='+itemUserCode, "preview", 1600, 800, 0, 0, 0)
	}

</script>


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
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<!-- <style>
p.input_info {
	display: inline;
	padding-top: 3px;
	color: #e84700;
	font-size: 11px;
	margin-left: 10px;
}

.groupDiv {
	border-radius: 25px;
	border: 2px solid #73AD21;
	padding: 20px;
	margin: 10px;
}

.delete_item {
	position: absolute;
	top: 5px;
	right: 5px;
}

.groupPlaceHolder {
	width: 100%;
	height: 300px;
	background: #d6eafd	url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
	opacity: .5;
}

.text-info {
	padding-top: 8px;
}

p.item_image_main a.delete_item_image {
	right: 225px;
}
p.item_image_main a.delete_item_image.mobile {
	right: 10px;
}
</style> -->

	<div class="location">
		<a href=""></a> &gt;  <a href=""></a> &gt; <a href="" class="on"></a>
	</div>

	<div class="item_list">
		<h3><span><!-- 이벤트 관리 --></span></h3>

		<form:form modelAttribute="featured" method="post" enctype="multipart/form-data">
			<form:hidden path="featuredId" />
			<form:hidden path="featuredType"/>
			<input type="hidden" name="prodString" id="prodString" />
			<%-- <input type="hidden" name="featuredCheck" value="${featuredCheck}" /> --%>
			<input type="hidden" name="featuredCheck" value="1" />

		    <div class="board_write">
		        <table class="board_write_table" summary="">
		            <colgroup>
		                <col style="width:220px;">
		                <col>
		                <col style="width:220px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <tr>
		                    <td class="label"><span class="required_mark">*</span><c:out value="${op:message('이벤트명')}"/></td><!-- 이벤트명 -->
		                    <td>
		                        <div class="flex_box gap-08">
		                            <form:input path="featuredName" class="input_txt required _filter full" type="text" title="${fn:escapeXml(mainPart)}${op:message('M01220')}"/><!-- 명 -->
		                        </div>
		                    </td>
		                    <%-- <td class="label"><span class="required_mark">*</span>${op:message('M00413')}</td><!-- URL -->
		                    <td>
		                        <div class="flex_box gap-08 item-center">
		                            <span>/pages/</span>
		                            <form:input path="featuredUrl" title="URL" class="input_txt required _filter wd-200" type="text" />
		                            <button id="featuredUrlSearch" type="button" class="btn btn-default btn-sm"><span>${op:message('M00148')}</span></button><!-- 중복검사 -->
		                        </div>
		                    </td> --%>
		                </tr>
		                <%-- <tr>
		                    <td class="label">설명</td><!-- 설명 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-08">
		                        	<form:input path="featuredSimpleContent" title="설명" class="input_txt required _filter full" type="text"></form:input><!-- 설명 -->
		                        </div>
		                    </td>
		                </tr> --%>
		                <tr>
		                    <td class="label"><span class="required_mark">*</span>진행 기간</td><!-- 진행 기간 -->
		                    <td colspan="3">
		                        <div>
		                            <span class="datepicker">
		                                <input id=startDate name="startDate" title="${op:message('M00507')}자" class="datepicker" type="text" value="${featured.startDate != '99999999' ? featured.startDate : '' }" maxlength="8" autocomplete="off"><!-- 시작일자 -->
		                                <!-- <button type="button" class="ui-datepicker-trigger">
		                                    <span class="icon_calendar">날짜 선택</span>
		                                </button> -->
		                            </span>
		                            <form:select path="startTime" title="${op:message('M00508')}"> <!-- 시간 선택 -->
										<c:forEach items="${hours}" var="code">
											<form:option value="${fn:escapeXml(code.value)}" label="${fn:escapeXml(code.label)}"/>
										</c:forEach>
									</form:select>
		                            <span class="wave">~</span>
		                            <span class="datepicker">
		                                <input id="endDate" name="endDate" title="${op:message('M00509')}자" class="datepicker" type="text" value="${featured.endDate != '99999999' ? featured.endDate : '' }" maxlength="8" autocomplete="off"><!-- 종료일자 -->
		                                <!-- <button type="button" class="ui-datepicker-trigger">
		                                    <span class="icon_calendar">날짜 선택</span>
		                                </button> -->
		                            </span>
		                            <form:select path="endTime" title="${op:message('M00508')}"> <!-- 시간 선택 -->
										<c:forEach items="${hours}" var="code">
											<form:option value="${fn:escapeXml(code.value)}" label="${fn:escapeXml(code.label)}"/>
										</c:forEach>
									</form:select>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label"><c:out value="${op:message('M00666')}"/></td><!-- 대표연락처 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-08 item-center">
		                            <form:select path="featuredPhoneNo1" title="${op:message('M00666')}" class="wd-150"><!-- 대표연락처 -->
		                                <option value="">선택</option><!-- 선택 -->
		                                <c:forEach items="${phone}" var="data" varStatus="i" >
											<option value="${fn:escapeXml(data.value)}" ${featured.featuredPhoneNo1 == data.value ? 'selected' : ''}>${fn:escapeXml(data.label)}</option>
		                                </c:forEach>
		                            </form:select>
		                            <span class="wave">-</span>
		                            <form:input path="featuredPhoneNo2" class="input_txt _filter wd-150" type="text" maxlength="4" title="${op:message('M00666')}2" oninput="javascript:inputNumber(this);" /><!-- 대표연락처2 -->
		                            <span class="wave">-</span>
		                            <form:input path="featuredPhoneNo3" class="input_txt _filter wd-150" type="text" maxlength="4" title="${op:message('M00666')}3" oninput="javascript:inputNumber(this);" /><!-- 대표연락처3 -->
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label">주최/주관</td><!-- 주최/주관 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-08">
		                            <form:input path="featuredHost" title="주최/주관" class="input_txt _filter full" value="${fn:escapeXml(featured.featuredHost)}" /><!-- 주최/주관 -->
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label"><c:out value="${op:message('M00779')}"/> 주소</td><!-- 홈페이지 주소 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-08">
		                            <form:input path="link" title="${op:message('M00779')} 주소" class="input_txt _filter full" type="text" maxlength="100" /><!-- 홈페이지 주소 -->
		                        </div>
		                    </td>
		                </tr>
		            </tbody>
		        </table>
		        <table class="board_write_table" summary="">
		            <colgroup>
		                <col style="width:220px;">
		                <col style="width:180px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <tr>
		                    <td class="label">목록 <c:out value="${op:message('M00752')}"/></td><!-- 목록 이미지 -->
		                    <td class="line-right tcenter">
		                        <div>
		                            <p class="mt10 point">권장 사이즈는<br>300px X 300px 입니다.</p>
		                        </div>
		                    </td>
		                    <td colspan="2">
		                        <div class="flex_box item-center">
		                            <c:if test="${empty featured.featuredListImage}">
										<input name="featuredListFile" type="file" title="${op:message('M00752')}" class="full input_file" accept="image/png, image/jpeg, image/gif" onchange="javascript:checkSize(this, 5);"/>
									</c:if>
									<c:if test="${!empty featured.featuredListImage}">
										<p class="item_image_main" style="width:100px;">
											<input type="hidden" class="fileType" value="5"/>
											<img src="${fn:escapeXml(featured.featuredListImageSrc)}" class="item_image size-100" alt=""/>
											<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
												<a href="javascript:;" class="delete_item_image"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
											</c:if>
										</p>
									</c:if>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label" rowspan="2">상단 <c:out value="${op:message('M00752')}"/></td><!-- 상단 이미지 -->
		                    <td class="line-right tcenter">
		                        <div>
		                            <p>PC</p>
		                            <p class="mt10 point">권장 사이즈는<br>1120px X 1120px 입니다.</p>
		                        </div>
		                    </td>
		                    <td colspan="2">
		                        <div class="flex_box item-center">
		                            <c:if test="${empty featured.featuredImage}">
										<input name="featuredFile" type="file" class="full input_file" title="${op:message('M00752')}" accept="image/png, image/jpeg, image/gif" onchange="javascript:checkSize(this, 20);"/><!-- 이미지 -->
									</c:if>
									<c:if test="${!empty featured.featuredImage}">
										<p class="item_image_main" style="width:100px;">
											<input type="hidden" class="fileType" value="2"/>
											<img src="${fn:escapeXml(featured.featuredImageSrc)}" class="item_image size-100" alt=""/>
											<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
												<a href="javascript:;" class="delete_item_image"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
											</c:if>
										</p>
									</c:if>
		                        </div>
		                        <!-- <div class="file_camera">
		                            <div class="image-box">
		                                <img src="../../content/images/common/item_223-2.jpg" class="item_image size-100 none" alt="상품이미지">
		                                <a href=""><img src="../../content/images/btn/file_close.gif" alt="close"></a>
		                            </div>
		                        </div> -->
		                    </td>
		                </tr>
		                <tr>
		                    <td class="line-right tcenter">
		                        <div>
		                            <p>mobile</p><!-- mobile -->
		                            <p class="mt10 point">권장사이즈는<br>350px X 350px 입니다.</p>
		                        </div>
		                    </td>
		                    <td colspan="2">
		                        <div class="flex_box item-center">
		                            <c:if test="${empty featured.featuredImageMobile}">
										<input name="featuredFileMobile" type="file" class="full input_file" title="${op:message('M00752')}" accept="image/png, image/jpeg, image/gif" onchange="javascript:checkSize(this, 20);"/><!-- 이미지 -->
									</c:if>
									<c:if test="${!empty featured.featuredImageMobile}">
										<p class="item_image_main" style="width:100px;">
											<input type="hidden" class="fileType" value="3"/>
											<img src="${fn:escapeXml(featured.featuredImageMobileSrc)}" class="item_image size-100" alt="" />
											<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
												<a href="javascript:;" class="delete_item_image mobile"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
											</c:if>
										</p>
									</c:if>
		                        </div>
		                      <!--   <div class="file_camera">
		                            <div class="image-box">
		                                <img src="../../content/images/common/item_223-2.jpg" class="item_image size-100 none" alt="상품이미지">
		                                <a href=""><img src="../../content/images/btn/file_close.gif" alt="close"></a>
		                            </div>
		                        </div> -->
		                    </td>
		                </tr>
		            </tbody>
		        </table>
		        <table class="board_write_table" summary="">
		            <colgroup>
		                <col style="width:220px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <tr>
		                    <td class="label"><span class="required_mark">*</span>상세내용</td><!-- 상세내용 -->
		                    <td colspan="3">
		                        <!-- smart_editor2-wrap -->
		                        <!-- <div style="width: 100%; height:300px; background:#eee;">스마트 에디터 영역 입니다.</div> -->
		                        <!-- 실 사용 소스 -->
		                        <div class="smart_editor2-wrap">
		                            <form:textarea path="featuredContent" cols="30" rows="20" class="editor-content" title="message('M00006')" />
		                        </div>
		                        <!-- // 실 사용 소스 -->
		                        <!-- smart_editor2-wrap -->
		                    </td>
		                </tr>
		                <tr style="display: none;">
		                    <td class="label"><c:out value="${op:message('M00100')}"/> 형태</td><!-- 리스트 형태 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-12">
	                                <form:radiobutton path="prodState" value="1" label="${op:message('M01580')}${op:message('M00431')}" checked="checked" title="제품그룹 선택" /><!-- 기본선택 --><!-- 제품그룹 선택 -->
		                        </div>
		                    </td>
		                </tr>
		                <tr id="prodTr1">
		                    <td class="label"><c:out value="${op:message('M00431')}"/> <c:out value="${op:message('M00006')}"/></td><!-- 선택 상품 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-12" id="activeArea">
		                        	<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
			                            <button type="button" class="btn btn-default btn-sm" id="button_add_relation_item" onclick="findItem('prod${status.index}')"><c:out value="${op:message('M00582')}"/></button><!-- 상품 추가 -->
			                            <button type="button" class="btn btn-default btn-sm" onclick="if(confirm('모든 상품을 삭제하시겠습니까?')){Shop.deleteRelationItemAll('prod${status.index}');}"><c:out value="${op:message('M00411')}"/></button><!-- 모든 상품을 삭제하시겠습니까? --><!-- 전체삭제 -->
		                            </c:if>
		                        </div>
		                        <div>
			                        <ul id="prod${status.index}" class="sortable_item_relation" onclick="javascript:onItemClick(event);">
										<!-- <li style="display: none;"></li> -->
										<c:if test="${featured.prodState eq '2' || featured.prodState eq '1'}">
											<c:forEach items="${list}" var="item" varStatus="i">
												<c:if test="${!empty item.itemId}">
													<li id="prod${fn:escapeXml(status.index)}_item_${fn:escapeXml(item.itemId)}">
														<input type="hidden" name="prod${fn:escapeXml(status.index)}ItemIds" value="${fn:escapeXml(item.itemId)}" />
														<p class="image"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image size-100 none" alt="${op:message('M00659')}" /></p><!-- 상품이미지 -->
														<p class="title">[<c:out value="${item.itemUserCode}"/>] <label style="color:red">[<c:out value="${op:numberFormat(item.salePrice)}"/>]</label><br /><c:out value="${item.itemName}"/></p>

														<span class="ordering"><c:out value="${i.count}"/></span>
														<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
															<a href="javascript:javascript:deleteItem();" class="delete_item" style="position: absolute;top: 5px; right: 5px;"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
														</c:if>
													</li>
												</c:if>
											</c:forEach>
										</c:if>
									</ul>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label"><c:out value="${op:message('M00191')}"/></td><!-- 공개유무 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-12">
		                            <form:radiobutton path="displayListFlag" value="Y" checked="checked" label="${op:message('M00096')}" title="${op:message('M00096')}" /> <!-- 공개 -->
									<form:radiobutton path="displayListFlag" value="N" label="${op:message('M00097')}" title="${op:message('M00097')}" />     <!-- 비공개 -->
		                        </div>
		                    </td>
		                </tr>

		                <!-- 사용유무 필수값, 화면에 없음 -->
						<tr class="hidden">
							<td class="label"><c:out value="${op:message('M00669')}"/></td><!-- 사용유무 -->
							<td>
								<div>
									<form:radiobutton path="featuredFlag" value="Y" checked="checked" label="${op:message('M00083')}" class="requirement" title="${op:message('M00669')}" /><!-- 사용유무 -->
									<form:radiobutton path="featuredFlag" value="N" label="${op:message('M00089')}" class="requirement" title="${op:message('M00669')}" /><!-- 사용유무 -->
								</div>
							</td>
						</tr>
		            </tbody>
		        </table>
		    </div>

		    <div class="btn_all btn_center">
		        <div class="flex_box gap-08">
		        	<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || op:hasRole('ROLE_ADMIN_7') || op:hasRole('ROLE_ADMIN_8'))}">
			        	<c:choose>
			        		<c:when test="${featured.featuredId > 0}">
			            		<button type="submit" class="btn btn-dark-gray btn-small"><c:out value="${op:message('M00087')}"/></button><!-- 수정 -->
			        		</c:when>
			        		<c:otherwise>
			            		<button type="submit" class="btn btn-dark-gray btn-small"><c:out value="${op:message('M00088')}"/></button><!-- 등록 -->
			        		</c:otherwise>
			        	</c:choose>
			            <button type="button" class="btn btn-default btn-small" onclick="document.location.href='/opmanager/featured/list';" class="btn btn-default;'"><c:out value="${op:message('M00037')}"/></button><!-- 취소 -->
		            </c:if>
		        </div>
		    </div>
		</form:form>
	</div>










<module:smarteditorInit />
<module:smarteditor id="featuredContent" />

<script type="text/javascript">

var patterns = /^[a-zA-Z0-9_-]+$/;
var featuredUrl = '${fn:escapeXml(featured.featuredUrl)}';
var mainPart = '${fn:escapeXml(mainPart)}';


	$(function(){
		// 기획전 상품 그룹 펼치기
		showItemGroups();
		// 기획전 상품 그룹 접기
		hideItemGroups();

		$('input[name=featuredClass]').on('click', function() {
			if ($(this).val() == 1) {
				$('.mainPart').text('기획전');/* 기획전 */
				$('input[name=featuredName]').attr('title', Message.get("M01083"));/* 기획전명 */
				mainPart = '기획전';/* 기획전 */
				showAndHideImages();
			} else {
				$('.mainPart').text(Message.get("이벤트"));/* 이벤트 */
				$('input[name=featuredName]').attr('title', '이벤트명');/* 이벤트명 */
				mainPart = Message.get("이벤트");/* 이벤트 */
				showAndHideImages();

			}
		});

		$("#activeArea").sortable({
			placeholder : "groupPlaceHolder"
		});

		$(".delete_item").click(function(){
			if (confirm(Message.get("M00196"))) {/* 삭제하시겠습니까? */
				$(this).parent().remove();
			}
		});

		/* $("input[name='featuredCodeChecked']").on("click",function(){
			if($(this).val() == 'all'){
				if($(this).prop("checked")){
					$("input[name='featuredCodeChecked']").prop("checked",false);
					$("input[name='featuredCodeChecked']").attr("disabled", true);
					$(this).attr("disabled", false);
					$(this).prop("checked",true);
				} else {
					$("input[name='featuredCodeChecked']").attr("disabled", false);
				}
			}
		}); */

		$("input[name='displayListFlag']").on("change",function(){
			if($(this).val()== "N") {
				$(".requirement").removeClass("required");
			}
			else {
				$(".requirement").addClass("required");
			}
		});

		$("input[name='displayListFlag']").trigger("change");

		$("#featured").validator(function() {
			if($("input[name='displayListFlag']:checked").val()== "Y") {
				var value = $("input[name='featuredUrl']").val();

				if(!patterns.test(value)){
					alert(Message.get("M01332")); // 영문, 숫자, 하이픈, 언더바 만 입력가능합니다.
					$("#featuredUrl").focus();
					return false;
				}
			<c:if test="${isEvent ne 'Y'}">
				var outSideLink = $("input[name='link']").val();
				if ( !checkedOutSideLink(outSideLink) ){
					alert("※ 외부 사이트 링크인 경우 반드시 http:// (또는 https://)로 시작하도록 작성하셔야 합니다.");/* ※ 외부 사이트 링크인 경우 반드시 http:// (또는 https://)로 시작하도록 작성하셔야 합니다. */
					return false;
				}
			</c:if>


				var $startDate = $('#startDate').val();
				var $endDate = $('#endDate').val();



				if (!$startDate || !$endDate) {
					alert("진행 기간을 입력해주세요.");/* 진행 기간을 입력해주세요. */
					if (!$startDate) {
						$('#startDate').focus();
					} else {
						$('#endDate').focus();
					}
					return false;
				}

				if(($startDate != "" && $startDate != null) && ($endDate != "" && $endDate != null)) {
					if ( checkedDate( $startDate, $('#startTime').val(), $endDate, $('#endTime').val() ) ) {
						alert('사용기간의 종료기간이 시작기간보다 빠를 수 없습니다.');/* 사용기간의 종료기간이 시작기간보다 빠를 수 없습니다. */
						$('#startDate').focus();
						return false;
					}
				}

				if(($startDate == "" || $startDate == null) && ($endDate != "" && $endDate != null)) {
					alert('사용기간의 종료기간은 시작기간 선택없이 설정할 수 없습니다.');/* 사용기간의 종료기간은 시작기간 선택없이 설정할 수 없습니다. */
					$('#startDate').focus();
					return false;
				}

				/* if (!($('#featured input:checkbox[name=featuredCodeChecked]').is(":checked"))) {
					alert("구분이 선택되지 않았습니다.");
					$('#featured input:checkbox[name=featuredCodeChecked]').eq(0).focus();
					return false;
				} */



				/* if ($('#featured #featuredContent').val() == '') {
					alert("내용 작성해주세요.");
					$('#featured #featuredContent').focus();
					return false;
				} */

			}

			Common.getEditorContent("featuredContent");

			if ($("input[name='featuredCheck']").val() != '1'
//					|| (featuredUrl != null && featuredUrl != '' && $("input[name='featuredUrl']").val() != featuredUrl)
					) {

				alert(Message.get("M01087"));/* URL의 중복 여부가 체크되지 않았습니다. */
				$("#featuredUrl").focus();

				return false;
			}

			//이상우 [2017-03-16 수정] 사용자그룹 정렬 방식 일때만 그룹명 체크
			var prodState = $("input[name=prodState]:checked").val();
			if (prodState == '3') {
				var groupChk = 0;
				$("input[name=userGroup]").each(function(){
					if ($(this).val()=="") {
						alert("그룹명을 입력해주세요.");/* 그룹명을 입력해주세요. */
						groupChk = 1;
						$(this).focus();
						return false;
					}
				});

				if (groupChk == 1) {
					return false;
				}
			}
			var stateFlag = $("input[name=featuredClass]:checked").val();
			if (stateFlag == '1' && prodState == '3') {
				if ($("#prodTr2").find(".groupDiv").length<1) {
					alert("현재 기획전에 추가된 그룹명이 없습니다. 그룹을 추가해주세요.");/* 현재 기획전에 추가된 그룹명이 없습니다. 그룹을 추가해주세요. */
					return false;
				}

				if ($("#prodTr2").find("li").length<1) {
					alert("현재 기획전에 추가된 상품이 없습니다. 상품을 추가하시거나\n 제품그룹선택 항목의 '기본선택'을 선택해주세요.");/* 현재 기획전에 추가된 상품이 없습니다. 상품을 추가하시거나\n 제품그룹선택 항목의 '기본선택'을 선택해주세요. */
					return false;
				}

				var itemChk = 0;
				var groupName=[];
				$(".groupDiv").each(function(index){
					var groupEl = $(this).find(".userGroup");
					groupEl.val($.trim(groupEl.val()));
					groupName[index] = groupEl.val();
					if ($(this).find("li").length == 0) {
						alert("\""+groupEl.val()+"\"그룹에 등록된 제품이 없습니다.");/* 그룹에 등록된 제품이 없습니다. */
						itemChk = 1;
						groupEl.focus();
						return false;
					}
					$(this).find("ul").attr("id","prod"+index);
					$(this).find("ul li input").attr("name","prod"+index+"ItemIds");
				});

				if (itemChk == 0) {
					for (var i=0; i<groupName.length; i++) {
						for (var j=i; j<groupName.length; j++) {
							if (i != j && groupName[i].toLowerCase() === groupName[j].toLowerCase()) {
								alert("그룹명이 중복됩니다. 변경 해주세요.");/* 그룹명이 중복됩니다. 변경 해주세요. */
								$(".userGroup").eq(j).focus();
								itemChk = 1;
								return false;
							}
						}
						if (itemChk == 1) {return false;}
					}
				}


				if (itemChk == 1) {
					return false;
				}
			}
			else {
				/* if ($("#prodTr1").find("li").length<1 && $("input[name='displayListFlag']:checked").val()== "Y") {
					alert("현재 기획전에 추가된 상품이 없습니다. 상품을 추가해주십시오.");
					return false;
				} */
			}
			makeProdString();

			// if (!confirm(mainPart + ' 정보를 저장하시겠습니까?')) {/* 정보를 저장하시겠습니까? */
			if (!confirm(Message.get("M00001") + ' 정보를 저장하시겠습니까?')) {/* 상품 정보를 저장하시겠습니까? */
				return false;
			}
		});

		$("#featuredUrlSearch").on("click",function(){

			var value = $("input[name='featuredUrl']").val();

			if(value == '') {
				alert("URL을 입력하세요.")/* URL을 입력하세요. */
				$("#featuredUrl").focus();
				return false;
			}

			if(!patterns.test(value)){
				alert(Message.get("M01332")); // 영문, 숫자, 하이픈, 언더바 만 입력가능합니다.
				$("#featuredUrl").focus();
				return false;
			}

			var originalValue = $("#originalFeaturedUrl").val();
			if (value == originalValue) {
				alert("사용 가능한 페이지명입니다.");	// 사용가능/* 사용 가능한 페이지명입니다. */
				$("input[name='featuredCheck']").val("1");
				return false;
			}

			var param = {
				"featuredUrl" : $("input[name='featuredUrl']").val()
				, "notFeaturedId" : $('#featuredId').val()
			};

			$.post("/opmanager/featured/url-search",param,function(resp){
				Common.responseHandler(resp, function(){
					alert("사용 가능한 페이지명입니다.");	// 사용가능/* 사용 가능한 페이지명입니다. */
					$("input[name='featuredCheck']").val("1");
					featuredUrl = value;
				},function(){
					alert("해당 URL이 존재 합니다.");	// 사용불가/* 해당 URL이 존재 합니다. */
					$("input[name='featuredCheck']").val("0");
					featuredUrl = '';
				});
			});

		});

		$(".delete_item_image").on("click",function(){
			var fileType=$(this).parent().find(".fileType").val();
			var fileAdd = $(this).parent().parent();
			var desc = "";
			var label = "";
			var message = Message.get("M00196");	// 삭제하시겠습니까?

			if($('input[name="featuredClass"]:checked').val() == '1') {
				label = "기획전";/* 기획전 */
			} else {
				label = Message.get("이벤트");/* 이벤트 */
			}


			Common.confirm(message, function(){
				$.post("/opmanager/featured/delete-image", "featuredId="+$("input[name='featuredId']").val()+"&fileType="+fileType , function(resp){
					Common.responseHandler(resp, function(){
						//$(".item_image_main").remove();
						$(this).parent().remove();
						 var message = "";
						var nm = "";
						message=Message.get("M00752");/* 이미지 */
						if(fileType=="1") {
							//message=Message.get("M01085"); // 섬네일이미지
							nm="thumbnailFile";
							//desc="<span class=\"f11\">※ <span class=\"mainPart\">" + label +" </span>목록 배너이미지로 권장크기는 1,100px * 150px 입니다.</span>";
						}
						else if(fileType=="2") {
							//message=Message.get("M00982"); // 대표이미지
							nm="featuredFile";
							//desc="<span class=\"f11\">※  메인페이지에 노출되는 <span class=\"mainPart\">" + label + "</span>대표이미지로 권장크기는 258px * 220px 입니다.</span>";
						}
						else if(fileType=="3") {
							//message="모바일 대표이미지"; // 모바일 대표이미지
							nm="featuredFileMobile";
							//desc="<span class=\"f11\">※ 모바일 메인페이지 <span class=\"mainPart\">" + label +"</span>대표이미지로 기기에 따라 자동 조정됩니다. 권장크기는 480px * 250px 입니다.</span>";
						}
						else if(fileType=="4") {
							//message="모바일 대표이미지"; // 모바일 섬네일이미지
							nm="thumbnailFileMobile";
							//desc="<span class=\"f11\">※ 모바일 <span class=\"mainPart\">" + label +"</span> 목록 기기에 따라 자동 조정됩니다. 권장크기는 400px * 85px 입니다.</span>";
						}
						else if(fileType=="5") {
							nm="featuredListFile";// 목록용 이미지
						}
						else if(fileType=="6") {
							nm="thumbnailListFile";		// 목록용 썸네일 이미지
						}
						fileAdd.html('<input name="'+ nm +'" type="file" title="' + message + '" accept="image/png, image/jpeg, image/gif" />'+desc);
					});
				});
			});
		});

		// 관련상품 드레그
	    $(".sortable_item_relation").sortable({
	        placeholder: "sortable_item_relation_placeholder"
	    });

		$("input[name=prodState]").change(function(){
			var stateFlag = $("input[name=prodState]:checked").val();
			if(stateFlag == '1' || stateFlag == "2") {
				$("#prodTr1").show();
				$("#prodTr2").hide();
			}
			else {
				$("#prodTr2").show();
				$("#prodTr1").hide();
			}
		});

		$("input[name=prodState]").trigger("change");

		showAndHideImages();
	});

	function checkedOutSideLink (link){

		var pattern1 = new RegExp("http://");
		var pattern2 = new RegExp("https://");

		if (link == "") {
			return true;
		}

		if( (pattern1.test(link)) ){
			return  true;
		}

		if( (pattern2.test(link)) ){
			return  true;
		}

		return false;

	}

	function findItem(dest) {

		<%-- var featuredType = '${featured.featuredType}'; --%>
		var conditionType = "";
		/*if (featuredType == '8' || featuredType == '9') {
			conditionType = "FIND_ITEM_POPUP_FOR_PLANNER";
		}*/

		Shop.findItem(dest, conditionType);
	}

	function prodDrag()	{
		$(".sortable_item_relation").sortable({
			placeholder : "sortable_item_relation_placeholder"
		});
		$("#activeArea").sortable({
			placeholder : "groupPlaceHolder"
		});

	}

	var itemNo;
	if ("${fn:escapeXml(featured.prodState)}"=="2") {
		itemNo = 0;
	}
	else {
		itemNo = "${fn:length(userGroupList)}";
	}

	function addItem() {
		$("#activeArea")
				.prepend(
						'<div class="groupDiv"><p class="mb10"><input type="text" class="userGroup" name="userGroup" class="full required requirement" title="사용자 그룹명" />&nbsp;<button type="button" id="button_add_relation_item" class="table_btn" onclick="findItem(\'prod'
								+ itemNo
								+ '\')"><span>상품 추가</span></button>&nbsp;<button type="button" class="table_btn" onclick="Shop.deleteRelationItemAll(\'prod'
								+ itemNo
								+ '\')"><span>전체삭제</span></button></p><ul id="prod'+itemNo+'" class="sortable_item_relation"></li></ul></div>');
		itemNo++;
		prodDrag();
	}

	function makeProdString() {
		var stateFlag = $("input[name=prodState]:checked").val();
		if (stateFlag == '1' || stateFlag == '2') {
			$("#prodTr2").remove();
		}
		else {
			$("#prodTr1").remove();
		}

		var prod=[""];
		if (stateFlag == '1' || stateFlag == '2') {
			prod[0]="";
			$("input[name=prod${fn:escapeXml(dest)}ItemIds]").each(function(index) {
				if(index!=0){prod[0]+="~";}
				//alert($(this).val());
				prod[0]=prod[0]+$(this).val();
			});
		}
		else {
			var j = 0;
			for(var i=0; i<itemNo; i++)	{
				prod[j]="";
				if ($("input[name=prod"+i+"ItemIds]").length>0) {
					$("input[name=prod"+i+"ItemIds]").each(function(index) {
						if(index!=0){prod[j]+="~";}
						prod[j]+=$(this).val();
					});
					j++;
				}
			}
		}

		$("#prodString").val(prod);
		return false;
	}

	function  checkedDate(startDate,startTime, endDate, endTime){

		return (startDate+startTime+'00') > (endDate+endTime+'59' );

	}

	/* function unfoldItemGroup() {
		$('.add-group-item-area').removeClass('fold');
		$('.add-group-item-area .groupDiv').removeClass('fold');
	}
	function foldItemGroup() {
		$('.add-group-item-area').addClass('fold');
		$('.add-group-item-area .groupDiv').addClass('fold');
	} */

	function showAndHideImages() {
		/*
	    if($("input[name=featuredClass]:checked").val() == '1') {
			$('.imageArea-1').show();
			$('.imageArea-3').show();
// 			$('.select-category').show();
		} else {
			$('.imageArea-1').hide();
			$('.imageArea-3').hide();
// 			$('.select-category').hide();
		}
		*/
	}

	function showItemGroups() {
		$('.fold-item-groups').on('click', function() {
			$('.groupDiv').find('ul').hide();
		});
	}

	function hideItemGroups() {
		$('.show-item-groups').on('click', function() {
			$('.groupDiv').find('ul').show();
		});
	}

	function fnReplyManagement() {
	    Common.popup('/opmanager/featured/manage-event-reply?featuredId=' + $('#featuredId').val(), 'create', 1024, 800, 1);
	}

	function updateEventCode() {
		var id = $('#featuredId').val();
		$.post('/opmanager/featured/update-event-code/'+id, {}, function(resp){
			Common.responseHandler(resp, function(){
				location.reload();
			});
		});
	}

	// 숫자만 입력
	function inputNumber(inputElement) {
		let beforePosition = inputElement.selectionStart - 1;		// 키 입력 후 위치 - 1 => 입력 전 위치
		let beforeLength = inputElement.value.length;			// 입력 후 길이
		inputElement.value = inputElement.value.replace(/[^0-9.]/g, '');
		let afterLength = inputElement.value.length;			// 입력 전 길이
		if (beforeLength > afterLength) {		// 다르면 숫자 입력 값이 아님
			inputElement.setSelectionRange(beforePosition, beforePosition);		// 기존 위치로 커서 이동
		}
	}

	// 선택상품 삭제시 번호 다시 매기기
	function onItemClick(e){
		/* try {
			if ((e.path[1].getAttribute("class") == "delete_item"
						|| e.path[1].getAttribute("class") == "delete_item_image")
					&& e.path[2].getAttribute("id").includes("prod_item_")) {
				let id = e.path[2].getAttribute("id");

				let number = 1;

				let itemList = $("#prod").children();
				let length = itemList.length;
				for (let i = 0 ; i < length ; i++) {
					let itemId = itemList[i].id;

					if (itemId != id) {
						itemList[i].children[3].innerHTML = number;
						number++;
					}
				}
			}
		} catch (e) {
			console.log(e);
		} */
	}

	// 첨부파일 용량 체크
	function checkSize(input, maxSize) {
		try {
		    if (input.files && input.files[0].size > (maxSize * 1024 * 1024)) {
		        alert("파일 사이즈가 " + maxSize + "mb 를 넘습니다.");
		        input.value = null;
		    }
		} catch (e) {
			console.log(e);
	        alert("문제가 발생했습니다.");
	        input.value = null;
		}
	}

</script>
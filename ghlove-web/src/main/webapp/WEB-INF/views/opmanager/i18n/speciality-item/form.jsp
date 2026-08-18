<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

		<div class="location">
            <a href=""></a> &gt; <a href=""></a> &gt; <a href=""></a>
        </div>
        <h3><span><c:out value="${op:message('특산물관 관리')}"/></span></h3><!-- 특산물관 관리 -->
        <!-- // 상단 타이틀 영역 -->

		<form:form modelAttribute="specialityItemManage" method="POST">
			<form:input type="hidden" path="specialityItemManageId"/>
			<input type="hidden" name="prodString" id="prodString" />
	        <!-- 조회 영역 -->
	        <div class="board_write">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
	                    <col />
	                </colgroup>
	                <tbody>
	                    <tr>
	                        <td class="label"><c:out value="${op:message('키워드')}"/></td><!-- 키워드 -->
	                        <td>
	                            <div class="flex_box gap-08">
	                            	<c:choose>
	                            		<c:when test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
	                                		<form:input path="keywords" title="${op:message('키워드')}" class="input_txt required _filter full" type="text" value="${fn:escapeXml(specialityItemManage.keywords)}" readOnly="readOnly" /><!-- 키워드 -->
	                            		</c:when>
	                            		<c:otherwise>
	                                		<form:input path="keywords" title="${op:message('키워드')}" class="input_txt required _filter full" type="text" value="${fn:escapeXml(specialityItemManage.keywords)}" oninput="javascript:inputCheck(this);" /><!-- 키워드 -->
	                            		</c:otherwise>
	                            	</c:choose>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label"><c:out value="${op:message('특산물 소개')}"/></td><!-- 특산물 소개 -->
	                        <td>
	                            <div class="flex_box col item-fend">
	                            	<c:choose>
	                            		<c:when test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
	                                		<textarea id="specialityItemInfo" name="specialityItemInfo" title="${op:message('특산물 소개')}" maxlength="200" class="full" readOnly="readOnly" ><c:out value="${specialityItemManage.specialityItemInfo}"/></textarea><!-- 특산물 소개 -->
	                            		</c:when>
	                            		<c:otherwise>
	                                		<textarea id="specialityItemInfo" name="specialityItemInfo" title="${op:message('특산물 소개')}" maxlength="200" class="full" ><c:out value="${specialityItemManage.specialityItemInfo}"/></textarea><!-- 특산물 소개 -->
	                            		</c:otherwise>
	                            	</c:choose>
	                                <span id="infoLength">[0/200]</span>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="label"><c:out value="${op:message('선택 답례품')}"/></td><!-- 선택 답례품 -->
	                        <td>
								<c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
		                            <div class="flex_box gap-08 mb10">
	                                    <button type="button" id="button_add_relation_item" class="btn btn-default btn-sm" onclick="findItem('prod')"><span><c:out value="${op:message('M00582')}"/> <!-- 답례품 추가 --> </span></button>
	                                    <button type="button" class="btn btn-default btn-sm" onclick="if(confirm('모든 답례품을 삭제하시겠습니까?')){Shop.deleteRelationItemAll('prod');}" ><span><c:out value="${op:message('M00411')}"/> <!-- 전체삭제 --> </span></button>
	                                </div>
                                </c:if>

		                        <div id="product">
			                        <ul id="prod" class="sortable_item_relation" onclick="javascript:onItemClick(event);">
										<!-- <li style="display: none;"></li> -->
										<%-- <c:if test="${featured.prodState eq '2' || featured.prodState eq '1'}"> --%>
											<c:forEach items="${specialityItemList}" var="item" varStatus="i">
												<c:if test="${!empty item.itemId}">
													<li id="prod_item_${fn:escapeXml(item.itemId)}">
														<input type="hidden" name="prodItemIds" value="${fn:escapeXml(item.itemId)}" />
														<p class="image"><img src="${shop:loadImage(item.itemCode, item.itemImage, 'XS')}" class="item_image size-100 none" alt="${op:message('M00659')}" /></p><!-- 답례품이미지 -->
														<p class="title">
															<c:choose>
		                                                        <c:when test="${item.dataStatusCode == '1'}">
		                                                            <c:if test="${item.itemSoldOutFlag == 'Y'}"><strong style="color:red">[<c:out value="${op:message('M00693')}"/>]</strong></c:if><%-- 품절 --%>
		                                                            <c:if test="${item.displayFlag == 'N'}"><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></c:if><%-- 비공개 --%>
		                                                        </c:when>
		                                                        <c:otherwise>
		                                                            <a href="javascript:Manager.itemLog('${fn:escapeXml(item.itemId)}')"><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></a>	<%-- 비공개 --%>
		                                                        </c:otherwise>
		                                                    </c:choose>
															[<c:out value="${item.itemUserCode}"/>] <label style="color:red">[<c:out value="${op:numberFormat(item.salePrice)}"/>]</label><br /><c:out value="${item.itemName}"/>
														</p>
														<span class="ordering"><c:out value="${i.count}"/></span>

														<c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
															<a href="javascript:javascript:deleteItem();" class="delete_item" style="position: absolute;top: 5px; right: 5px;"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
														</c:if>
													</li>
												</c:if>
											</c:forEach>
										<%-- </c:if> --%>
									</ul>
		                        </div>
	                        </td>
	                    </tr>

	                </tbody>
	            </table>
	            <div class="btn_all btn_center">
	                <div class="flex_box gap-08">
			            <c:choose>
		               		<c:when test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
		                   		<button type="button" class="btn btn-dark-gray btn-small" onclick="javascript:document.location.href='/opmanager/speciality-item/list';"><c:out value="${op:message('M00480')}"/></button><!-- 목록 -->
		               		</c:when>
		               		<c:otherwise>
			                    <button type="submit" class="btn btn-dark-gray btn-small" onclick="javascript:validate(event);"><c:out value="${op:message('M00101')}"/></button><!-- 저장 -->
		               		</c:otherwise>
		               	</c:choose>
	                </div>
	            </div>
	        </div>
	        <!-- // 조회 영역 -->
		</form:form>


<page:javascript>
<script type="text/javascript">



	$(document).ready(function() {

		$("#infoLength").empty();
		$("#infoLength").html("[" + $('#specialityItemInfo').val().length + " / " + $("#specialityItemInfo")[0].maxLength + "]");

<c:choose>
	<c:when test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">


	});

	</c:when>
	<c:otherwise>


		$('#specialityItemInfo').on('keyup', function(event) {
	        $('#infoLength').html("[" + $(this).val().length + " / " + this.maxLength + "]");
	        if($(this).val().length > this.maxLength) {
	            $(this).val($(this).val().substring(0, this.maxLength));
	            $('#infoLength').html("[" + this.maxLength + " / " + this.maxLength + "]");
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

		// 관련답례품 드레그
	    $(".sortable_item_relation").sortable({
	        placeholder: "sortable_item_relation_placeholder"
	    });

	});

	//입력 제한
	function inputCheck(inputElement) {
		let beforePosition = inputElement.selectionStart - 1;		// 키 입력 후 위치 - 1 => 입력 전 위치
		let beforeLength = inputElement.value.length;			// 입력 후 길이
//		inputElement.value = inputElement.value.replace(/[^0-9.]/g, '');		// 숫자만
		inputElement.value = inputElement.value.replace(/[^a-zA-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|0-9|,|]/g, '');		// 알파벳,한글,숫자, ',' 만 입력 가능
		let afterLength = inputElement.value.length;			// 입력 전 길이
		if (beforeLength > afterLength) {		// 다르면 가능한 입력 값이 아님
			inputElement.setSelectionRange(beforePosition, beforePosition);		// 기존 위치로 커서 이동
		}
	}

	// 선택답례품 삭제시 번호 다시 매기기
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

	function findItem(dest) {

		<%-- var featuredType = '${featured.featuredType}';
		var conditionType = "";
		if (featuredType == '8' || featuredType == '9') {
			conditionType = "FIND_ITEM_POPUP_FOR_PLANNER";
		}

		Shop.findItem(dest, conditionType); --%>
		Shop.findItem(dest, "");
	}

	function validate(event) {
		if (confirm("정보를 저장하시겠습니까?")) {
			makeProdString();
		} else {
			event.preventDefault();
		}
	}


	function makeProdString() {
		let prod = "";

		$("#prod").children('li').each(function(index) {
			if(index!=0){
				prod += "~";
			}
			prod += $(this).children('input[name="prodItemIds"]').val();
		});

		$("#prodString").val(prod);
	}


	</c:otherwise>
</c:choose>

</script>
</page:javascript>
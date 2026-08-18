<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<form:form modelAttribute="locgovFavItemMng" id="prj" method="post" enctype="multipart/form-data" onsubmit=''>
    <h3 class="custom">인기답례품관리 설정</h3>
    <div class="item_info_wrap">
        <div class="item_list mt70">
            <div class="board_write">
                <table class="board_write_table" summary="인기답례품관리 설정">
                    <colgroup>
                        <col style="width: 120px" />
                        <col style="width: auto;" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="label"><span class="required_mark">*</span>지자체</td>
							<td>
                                <c:choose>
			                   		<%-- <c:when test="${(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}"> --%>
			                   		<c:when test="${(locgovFavItemMng.locgovCode eq null || locgovFavItemMng.locgovCode eq '') && (locgovCode eq null || locgovCode eq '')}">
			                       		<div class="flex_box gap-08">
				                            <form:select path="upperLocgovCode" class="wd-150">
							                    <form:option value="">-${op:message('M00039')}-</form:option>
							                    <c:forEach items="${wdr}" var="wdr">
							                        <form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
							                    </c:forEach>
							                </form:select>
				                            <form:select path="locgovCode" class="wd-150">
							                    <option value="">-시,군,구-</option>
							                </form:select>
			                            </div>
			                   		</c:when>
		                   			<c:otherwise>
			                   			<div>
			                            	<c:out value="${locgovNm}"/>
			                            	<form:input path="locgovCode" type="hidden" value="${fn:escapeXml(locgovCode)}"></form:input>
			                            </div>
		                   			</c:otherwise>
		                   		</c:choose>
							</td>
                        </tr>

                        <tr>
							<td class="label">공개여부</td>
							<td>
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="deleteYn" value="N" label="공개" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="deleteYn" value="Y" label="비공개"/>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">발간호</td>
							<td>
								<div>
									<c:choose>
				                   		<c:when test="${(locgovFavItemMng.catalogYear eq null || locgovFavItemMng.catalogYear eq '')}">
											<form:select path="catalogYear" title="구분" class="wd-150">
												<form:option value="">전체</form:option>
												<c:forEach items="${yyyy}" var="yyyy">
												    <form:option value="${fn:escapeXml(yyyy.catalogYear)}" selected="${fn:escapeXml(locgovFavItemMng.catalogYear) eq fn:escapeXml(yyyy.catalogYear) ? 'selected' : ''}" label="${fn:escapeXml(yyyy.catalogYear)}" />
												</c:forEach>
								            </form:select>년
								            <form:select path="catalogNo" title="구분" class="wd-150">
												<form:option value="">선택</form:option>
								            </form:select>호
							            </c:when>
			                   			<c:otherwise>
			                   				<c:out value="${locgovFavItemMng.catalogYear}"/>-<c:out value="${locgovFavItemMng.catalogNo}"/>
			                   			</c:otherwise>
		                   			</c:choose>
								</div>
							</td>
						</tr>
						<tr>
                            <td class="label">
                            	<p><span class="required_mark">*</span>답례품 선택</p>
                            </td> <!-- 상세이미지 -->
	                        <td>
								<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6'))}">
		                            <div class="flex_box gap-08 mb10">
	                                    <button type="button" id="button_add_relation_item" class="btn btn-default btn-sm" onclick="findItem('favProd')"><span><c:out value="${op:message('M00582')}"/> <!-- 답례품 추가 --> </span></button>
	                                </div>
                                </c:if>
                                <form:input path="itemId" type="hidden" value=""></form:input>
		                        <div id="product">
			                        <ul id="favProd" class="sortable_item_relation">
										<c:if test="${locgovFavItemMng.itemId != 0}">
											<li id="favProd_item_${fn:escapeXml(locgovFavItemMng.itemId)}">
												<form:input path="itemId" type="hidden" value="${fn:escapeXml(locgovFavItemMng.itemId)}"></form:input>
												<p class="image"><img src="${shop:loadImage(locgovFavItemMng.itemCode, locgovFavItemMng.itemImage, 'XS')}" class="item_image size-100 none" alt="${op:message('M00659')}" /></p><!-- 답례품이미지 -->
												<p class="title">
													<c:choose>
                                                        <c:when test="${locgovFavItemMng.dataStatusCode == '1'}">
														<%--	<c:if test="${item.itemSoldOutFlag == 'Y'}"><strong style="color:red">[<c:out value="${op:message('M00693')}"/>]</strong></c:if>품절 --%>
                                                            <c:if test="${locgovFavItemMng.displayFlag == 'N'}"><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></c:if><%-- 비공개 --%>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="javascript:Manager.itemLog('${fn:escapeXml(locgovFavItemMng.itemId)}')"><strong style="color:red">[<c:out value="${op:message('M00097')}"/>]</strong></a>	<%-- 비공개 --%>
                                                        </c:otherwise>
                                                    </c:choose>
													[<c:out value="${locgovFavItemMng.itemUserCode}"/>] <label style="color:red">[<c:out value="${op:numberFormat(locgovFavItemMng.salePrice)}"/>]</label><br /><c:out value="${locgovFavItemMng.itemName}"/>
												</p>
												<span class="ordering"><c:out value="${i.count}"/></span>

												<c:if test="${!(op:hasRole('ROLE_ADMIN_1') || op:hasRole('ROLE_ADMIN_2') || op:hasRole('ROLE_ADMIN_3') || op:hasRole('ROLE_ADMIN_4'))}">
													<a href="javascript:deleteRelationItem('favProd_item_${fn:escapeXml(locgovFavItemMng.itemId)}');" class="delete_item" style="position: absolute;top: 5px; right: 5px;"><img src="/content/opmanager/images/icon/icon_x.gif" alt="" /></a>
												</c:if>
											</li>
										</c:if>
									</ul>
		                        </div>
	                        </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
		<c:if test="${(op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6'))}">
	        <div class="btn_all btn_center">
				<div class="flex_box gap-08">
					<button type="submit" id="insertBtn" class="btn btn-active">
						<c:choose>
	                   		<c:when test="${(locgovFavItemMng.catalogYear eq null || locgovFavItemMng.catalogYear eq '')}">
								${op:message('M00088')} <!-- 등록 -->
							</c:when>
							<c:otherwise>
								${op:message('M00087')} <!-- 수정 -->
                   			</c:otherwise>
               			</c:choose>
					</button>
					<button type="button" class="btn btn-default" onclick="cancle();">${op:message('M00037')} <!-- 취소 --> </button>
			    </div>
			</div>
		</c:if>
    </div>
</form:form>
<module:smarteditorInit />
<%-- <module:smarteditor id="prjCn" /> --%>

<script type="text/javascript">

    // 지차체 변경여부 체크
    var changeYn = "N";
    // 숫자 컴마.
	Common.addNumberComma();

$(function() {

	window.onpageshow = function(event) {
	    if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
	        // 에디트 내용을 decode하여 표시
	        //$('#prjCn')[0].value = decodeURIComponent($('#prjCn')[0].value);
	    }
	}

    scrollButtons();

	$("select[name='upperLocgovCode']").change(function() {
        searchSigungu(document.getElementById("upperLocgovCode"));
	});

	$("#insertBtn").click(function() {
		if(!validator()){
			return false;
		};
	});

	$("#catalogYear").change(function() {
		searchCatalogMngNoList($("#catalogYear").val());
	});

	// 발간년도 발간호 수정 시 disable 처리
	if("${fn:escapeXml(locgovFavItemMng.catalogYear)}" == ""){
		$("#catalogYear").attr("disabled",false);
		$("#catalogNo").attr("disabled",false);
	} else {
		$("#catalogYear").attr("disabled",true);
		$("#catalogNo").attr("disabled",true);
	}

}); //function() end

	//관련상품 삭제
	function deleteRelationItem(key) {
		$("input[name='itemId']").val(0);
	    $('#' + key).remove();
	};

	/**
	 *	함 수 명 : validator
	 *	기	능  : 유효성검사
	 */
	function validator() {
		if($("#catalogYear").val() == "") {
			alert("발간년도를 선택해 주세요.");
			$("#catalogYear").focus();
			return false;
		}

		if($("#catalogNo").val() == "") {
			alert("발간호를 선택해 주세요.");
			$("#catalogNo").focus();
			return false;
		}
		return true;
	}

	//시군구 목록 조회
	function searchSigungu(sidoElement) {
		let sidoValue = sidoElement.value;
		$("#locgovCode option").remove();
	    $('#locgovCode').append('<option value="">-시,군,구-</option>');
	    Common.loading.show();
	    if (sidoValue) {
	        $.post(
	        		url("/common/getLocgovCode")
	        		, {'code' : sidoValue}
	        		, function(response) {
	        			Common.responseHandler(response, function(){
	        				let data = response.data;
				            for (var i = 0; i < data.length; i++) {
				                var options = '<option value="' + data[i].LOCGOV_CODE + '">' + data[i].LOCGOV_NM + '</option>';
				                $('#locgovCode').append(options);
				            }

				         	// 조회 된 값 유지
				            if ("${fn:escapeXml(locgovFavItemMngParam.locgovCode)}" != "") {
				            	$("#locgovCode").val("${fn:escapeXml(locgovFavItemMngParam.locgovCode)}").prop("selected", true);

				            	if (!$("#locgovCode").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
				            		$("#locgovCode option:eq(0)").prop("selected", true);
				            	}
				            }
	        			});
		            }
	        )
	        .always(function () {
	        	Common.loading.hide();
	        });
	    } else {
	    	Common.loading.hide();
	    }
	}

	//발간호 조회
	function searchCatalogMngNoList(year) {
		$("#catalogNo option").remove();
	    $('#catalogNo').append('<option value="">-호 선택-</option>');
	    Common.loading.show();
	    if (year) {
	        $.post(
	        		url("/opmanager/catalog/locgovFavItem/catalogMngNoList")
	        		, {'code' : year}
	        		, function(response) {
	        			Common.responseHandler(response, function(){
	        				let data = response.data;
				            for (var i = 0; i < data.length; i++) {
				                var options = '<option value="' + data[i].CATALOG_NO + '">' + data[i].CATALOG_NO + '</option>';
				                $('#catalogNo').append(options);
				            }

				         	// 조회 된 값 유지
// 				            if ("${fn:escapeXml(locgovFavItemMngParam.locgovCode)}" != "") {
// 				            	$("#locgovCode").val("${fn:escapeXml(locgovFavItemMngParam.locgovCode)}").prop("selected", true);

// 				            	if (!$("#locgovCode").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
// 				            		$("#locgovCode option:eq(0)").prop("selected", true);
// 				            	}
// 				            }
	        			});
		            }
	        )
	        .always(function () {
	        	Common.loading.hide();
	        });
	    } else {
	    	Common.loading.hide();
	    }
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

   	//날짜 정합성 체크
    function dateValidate(vDate) {
    	var vValue = vDate;
    	var vValue_Num = vValue.replace(/[^0-9]/g, "");

    	var rxDatePattern = /^(\d{4})(\d{1,2})(\d{1,2})$/;
    	var dtArray = vValue_Num.match(rxDatePattern);

    	if (dtArray == null) {
    		return false;
    	}

    	dtYear = dtArray[1];
    	dtMonth = dtArray[2];
    	dtDay = dtArray[3];

    	if (dtMonth < 1 || dtMonth > 12) {
    		alert("유효하지 않은 날짜입니다. 다시 한 번 확인해주세요");
    		return false;
    	}
    	else if (dtDay < 1 || dtDay > 31) {
    		alert("유효하지 않은 날짜입니다. 다시 한 번 확인해주세요");
    		return false;
    	}
    	else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31) {
    		alert("유효하지 않은 날짜입니다. 다시 한 번 확인해주세요");
    		return false;
    	}
    	else if (dtMonth == 2) {
    		var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
    		if (dtDay > 29 || (dtDay == 29 && !isleap)) {
				alert("유효하지 않은 날짜입니다. 다시 한번 확인해주세요");
    			return false;
    		}
    	}

    	return true;
    }

    //버튼 스크롤에 따라 반응형으로 작동
    function scrollButtons() {
        $(window).scroll(function () {
            var st = $(window).scrollTop();
            var scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();

            if (scrollBottom < 170) {
                $('#buttons').removeClass('fixed_button');
            } else {
                $('#buttons').addClass('fixed_button');
            }
        });
    }

    function getCurrentKoreanDate() {
        const options = { timeZone: 'Asia/Seoul' };
        const koreanDate = new Date().toLocaleString('en-US', options);
        const [datePart, timePart] = koreanDate.split(', ');

        if (datePart && timePart) {
            const [month, day, year] = datePart.split('/');
            return `${fn:escapeXml(year)}${fn:escapeXml(month)}${fn:escapeXml(day)}`;
        }

        return null; // 오류 처리
    }

    function cancle() {
		location.replace('/opmanager/catalog/locgovFavItem/list');
	}
</script>
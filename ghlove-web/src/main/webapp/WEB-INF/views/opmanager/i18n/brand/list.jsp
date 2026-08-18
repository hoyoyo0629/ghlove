<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
		
    <!-- 개발 영역 -->
    <!-- <div class="admin_wrap"> -->
        <!-- 내용 -->
        <!-- <div id="container">
            <div class="contents" style="margin-left:0px;">
                <div class="contents_inner"> -->
                    <!-- 상단 타이틀 영역 -->
                    <div class="location">
                        <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
                    </div>
                    <h3><span><%-- ${op:message('MENU_16700')} --%></span></h3><!-- 브랜드 관리 -->
                    <!-- // 상단 타이틀 영역 -->

                    <!-- 조회 영역 -->
                    <form:form modelAttribute="brandParam" method="post" enctype="multipart/form-data">
	                    <div class="board_write">
	                        <table class="board_write_table" summary="">
	                            <colgroup>
	                                <col style="width:220px;">
	                                <col>
	                            </colgroup>
	                            <tbody>
	                                <tr>
	                                    <td class="label">${op:message('지자체')}</td><!-- 지자체 -->
	                                    <td>
	                                        <div class="flex_box gap-08">
	                                            <form:select id="selectSidoCode" name="" title="${op:message('지자체')}" class="wd-150" onchange="javascript:searchSigungu(this);" path="upperLocgovCode"><!-- 지자체 -->
	                                                <option value="">-시,도-</option><!-- -시,도- -->
					                                <c:forEach items="${sido}" var="data" varStatus="i" >
					                                	<option value="${fn:escapeXml(data.value)}">${fn:escapeXml(data.label)}</option>
					                                </c:forEach>
	                                            </form:select>
	                                            <form:select id="selectSigungu" name="" title="${op:message('지자체')}" class="wd-150" path="locgovCode"><!-- 지자체 -->
	                                                <option value="">-시,군,구-</option><!-- -시,군,구- -->
	                                            </form:select>
	                                        </div>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">브랜드명</td><!-- 브랜드명 -->
	                                    <td>
	                                        <div class="flex_box gap-08">
	                                            <form:input id="" path="brandName" title="브랜드명" class="input_txt required _filter full" type="text" value="" style="margin-left:0px;" /><!-- 브랜드명 -->
	                                        </div>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">${op:message('M00191')}</td><!-- 공개유무 -->
	                                    <td>
	                                        <div class="flex_box gap-12">
	                                            <!-- <div class="input-form">
	                                                <input id="rdo1-1" name="rdo1" type="radio" value="" checked="checked">
	                                                <label for="rdo1-1">전체</label>
	                                            </div>
	                                            <div class="input-form">
	                                                <input id="rdo1-2" name="rdo1" type="radio" value="">
	                                                <label for="rdo1-2">공개</label>
	                                            </div>
	                                            <div class="input-form">
	                                                <input id="rdo1-3" name="rdo1" type="radio" value="">
	                                                <label for="rdo1-3">비공개</label>
	                                            </div> -->
												<form:radiobutton path="displayFlag" label="${op:message('M00039')}" value="" checked="checked" /><!-- 전체 -->
												<form:radiobutton path="displayFlag" label="${op:message('M00096')}" value="Y" /><!-- 공개 -->
												<form:radiobutton path="displayFlag" label="${op:message('M00097')}" value="N" /><!-- 비공개 -->
	                                        </div>
	                                    </td>
	                                </tr>
	                            </tbody>
	                        </table>
	
	                        <div class="btn_all btn_right">
	                            <div class="flex_box gap-08">
	                                <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/brand/list';">${op:message('M00047')}</button><!-- 초기화 -->
	                                <button type="submit" class="btn btn-dark-gray btn-mini" id="searchBtn" onclick="javascript:pageLoad();">${op:message('M00048')}</button><!-- 검색 -->
	                            </div>
	                        </div>
	                    </div>
                    <!-- // 조회 영역 -->
                    </form:form>

                    <!-- 결과 영역 -->
                    <div class="count_title mt-40">
                        <h5>총 ${fn:escapeXml(brandCount)} ${op:message('M00272')}</h5><!-- 디자인: 총 00,000건 -->
                        <span>
                            <select id="pageCnt" name="" title="${op:message('M01387')}" onchange="javascript:setPageCnt(this);"><!-- 출력수 선택 --><!-- 디자인: 화면출력수 -->
                                <option value="10" ${itemsPageCnt == '10' ? 'selected="selected"' : ''}>${op:message('M00240')}</option><!-- 10개 출력 -->
                                <option value="20" ${itemsPageCnt == '20' ? 'selected="selected"' : ''}>${op:message('M00241')}</option><!-- 20개 출력 -->
                                <option value="50" ${itemsPageCnt == '50' ? 'selected="selected"' : ''}>${op:message('M00242')}</option><!-- 50개 출력 -->
                                <option value="100" ${itemsPageCnt == '100' ? 'selected="selected"' : ''}>${op:message('M00243')}</option><!-- 100개 출력 -->
                                <%-- <option value="1" ${itemsPageCnt == '1' ? 'selected="selected"' : ''}>1개 출력</option> --%>
                            </select>
                        </span>
                    </div>
					<form id="listForm">
	                    <div class="board_list">
	                        <table class="board_list_table">
	                            <colgroup>
	                                <col style="width:50px;">
	                                <col style="width:50px;">
	                                <col style="width:200px;">
	                                <col style="width:500px;">
	                                <col style="width:100px;">
	                            </colgroup>
	                            <thead>
	                                <tr>
	                                    <th scope="col"><input type="checkbox" id="check_all"></th>
	                                    <th scope="col">No.</th><!-- No. -->
	                                    <th scope="col">${op:message('지자체')}</th><!-- 지자체 -->
	                                    <th scope="col">브랜드명</th><!-- 브랜드명 -->
	                                    <th scope="col">${op:message('M01431')}</th><!-- 공개여부 -->
	                                </tr>
	                            </thead>
	                            <tbody>
	                                <c:forEach items="${brandList}" var="list" varStatus="i" >
										<tr style="background:#fff;">
											<td><input type="checkbox" name="id" id="check" value="${fn:escapeXml(list.brandId)}" title=${op:message('M00169')} /></td>
											<td>
		                                        <div><%-- ${list.brandId} --%>${fn:escapeXml(pagination.itemNumber - i.count)}</div>
		                                    </td>
		                                    <td>
		                                        <div>${fn:escapeXml(list.locgovName)}</div>
		                                    </td>
											<td>
		                                        <div>
		                                            <a href="/opmanager/brand/edit/${fn:escapeXml(list.brandId)}">${fn:escapeXml(list.brandName)}</a>
		                                        </div>
		                                    </td>
											<td><%-- ${list.displayFlag == 'Y' ? '공개' : '비공개'} --%>
												<c:choose>
													<c:when test="${list.displayFlag == 'Y'}">
												        ${op:message('M00096')}
												    </c:when>
												    <c:otherwise>
												        ${op:message('M00097')}
												    </c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
	                            </tbody>
	                        </table>
							<c:if test="${empty brandList}">
								<div class="no_content">
									${op:message('M00473')} <!-- 데이터가 없습니다. -->
								</div>
							</c:if>
	
	                        <!-- 하단 버튼 -->
	                        <div class="btn_all btn_right">
	                            <div class="flex_box gap-08">
	                                <button type="button" class="btn btn-default btn-mini" onclick="javascript:createBrand();">${op:message('M00088')}</button><!-- 등록 -->
	                                <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:deleteBrand();">${op:message('M00074')}</button><!-- 삭제 -->
	                            </div>
	                        </div>
	                        
	                        <!-- // 하단 버튼 -->
	
	                        <div class="pagination-wrap">
	                            <page:pagination-manager />
	                        </div>
	                    </div>
                    </form>
                    <!-- // 결과 영역 -->
                <!-- </div>
            </div>
        </div>
    </div> -->
    <!-- // 개발 영역 -->	

<script type="text/javascript">
	
	// 화면 로딩 후 시도, 시군구 정보 세팅
	$(document).ready(function (e) {
        if ("${fn:escapeXml(param.upperLocgovCode)}" != "") {		// 세팅된 시도 값 있을 경우
        	$("#selectSidoCode").val("${fn:escapeXml(param.upperLocgovCode)}").prop("selected", true);		// 시도 값 세팅
            searchSigungu(document.getElementById("selectSidoCode"));		// 시군구 목록 조회
        }
        pageLoad();
	});
	
	// 검색 전 한 화면당 페이지 출력 수 세팅
	function pageLoad() {
		setPageCnt(document.getElementById("pageCnt"));
	}

	// 등록 버튼 클릭시
	function createBrand() {
		document.location.href="/opmanager/brand/create";
	}
	
	// 삭제 버튼 클릭시
	function deleteBrand() {
		Common.updateListData("/opmanager/brand/delete-list", "${op:message('M00196')}");	// 삭제하시겠습니까?
	}
	
	// 한 화면당 페이지 출력 수 세팅
	function setPageCnt(pageElement) {
		let form = document.getElementById("brandParam");
		let itemCntPerPage = document.getElementById("perPageCntStr");
		if (itemCntPerPage) {
			itemCntPerPage.value = pageElement.value;
		} else {
			itemCntPerPage = document.createElement("input");
			itemCntPerPage.setAttribute("id", "perPageCntStr");
			itemCntPerPage.setAttribute("type", "hidden");
			itemCntPerPage.setAttribute("name", "perPageCntStr");
			itemCntPerPage.setAttribute("value", pageElement.value);
		}
		
		form.appendChild(itemCntPerPage);
	}

	// 시군구 목록 조회
	function searchSigungu(sidoElement) {
		let sidoValue = sidoElement.value;
		$("#selectSigungu option").remove();
        $('#selectSigungu').append('<option value="">-시,군,구-</option>');
	    if (sidoValue) {
	        $.post(
	        		url("/common/getLocgovCode")
	        		, {'code' : sidoValue}
	        		, function(response) {
	        			Common.responseHandler(response, function(){
	        				let data = response.data;
				            for (var i = 0; i < data.length; i++) {
				                var options = '<option value="' + data[i].LOCGOV_CODE + '">' + data[i].LOCGOV_NM + '</option>';
				                $('#selectSigungu').append(options);
				            }
			
				            // 조회 된 값 유지
				            if ("${fn:escapeXml(param.locgovCode)}" != "") {
				            	$("#selectSigungu").val("${fn:escapeXml(param.locgovCode)}").prop("selected", true);
				            	
				            	if (!$("#selectSigungu").val()) {		// 선택된 값이 없으면 제일 첫 번째 값 기본 세팅
				            		$("#selectSigungu option:eq(0)").prop("selected", true);
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
	
	

</script>
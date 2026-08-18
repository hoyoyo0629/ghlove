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


	<!-- 상단 타이틀 영역 -->
    <!-- 20221114 수정 -->
    <div class="location">
        <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
    </div>
    <h3><span><!-- 이벤트 관리 --></span></h3>
    <!-- // 20221114 수정 -->
    <!-- // 상단 타이틀 영역 -->

    <!-- 조회 영역 -->
   	<form:form modelAttribute="featuredParam" method="post">
    	<div class="item_list">
		    <div class="board_write">
		        <table class="board_write_table" summary="이벤트 등록">
		            <colgroup>
		                <col style="width:220px;">
		                <col>
		                <col style="width:220px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <!-- 20221114 수정 -->
		                <c:if test="${op:hasRole('ROLE_ADMIN_1')
		                			 || op:hasRole('ROLE_ADMIN_2')
		                			 || op:hasRole('ROLE_ADMIN_3')
		                			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
		                <tr>
		                    <td class="label">지자체</td>
		                    <td colspan="3">
		                        <div class="flex_box gap-08">
		                            <form:select id="selectSidoCode" name="" title="${op:message('지자체')}" class="wd-150" onchange="javascript:searchSigungu(this);" path="upperLocgovCode"><!-- 지자체 -->
		                                <option value="">-시,도-</option><!-- -시,도- -->
		                                <c:forEach items="${sido}" var="data" varStatus="i" >
		                                	<option value="${fn:escapeXml(data.value)}"><c:out value="${data.label}"/></option>
		                                </c:forEach>
		                            </form:select>
		                            <form:select id="selectSigungu" name="" title="${op:message('지자체')}" class="wd-150" path="locgovCode"><!-- 지자체 -->
		                                <option value="">-시,군,구-</option>
		                            </form:select>
		                        </div>
		                    </td>
		                </tr>
		                </c:if>
		                <!-- // 20221114 수정 -->
		                <tr>
		                    <td class="label"><c:out value="${op:message('M00011')}"/></td><!-- 검색구분 -->
		                    <td colspan="3">
		                        <div class="flex_box gap-08">
		                            <%-- <select id="" name="" title="검색구분" class="wd-150">
		                                <option value="">제목</option>
		                            </select>
		                            <input id="" name="" title="검색구분" class="input_txt required _filter full" type="text" value=""> --%>
									<form:select path="where" title="${op:message('M01186')}" class="wd-150"><!-- 상세검색 선택 -->
										<form:option value="FEATURED_NAME"><c:out value="${op:message('이벤트명')}"/></form:option><!-- 제목 --><!-- 이벤트명 -->
										<%-- <form:option value="FEATURED_URL"><c:out value="${op:message('M00413')}"/></form:option> --%><!-- URL -->
										<!--
										<form:option value="FEATURED_SIMPLE_CONTENT"><c:out value="${op:message('M01084')}"/></form:option> <!-- 특집페이지 설명
										-->
									</form:select>
									<form:input path="query" class="input_txt required _filter full" title="${op:message('M00022')}" /> <!-- 검색어 -->
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label"><c:out value="${op:message('M00032')}"/></td><!-- 진행상태 -->
		                    <td>
		                        <div class="flex_box gap-12">
		                           <!--  <div class="input-form">
		                                <input id="rdo1-1" name="rdo1" type="radio" value="" checked="checked">
		                                <label for="rdo1-1">전체</label>
		                            </div>
		                            <div class="input-form">
		                                <input id="rdo1-2" name="rdo1" type="radio" value="">
		                                <label for="rdo1-2">진행중</label>
		                            </div>
		                            <div class="input-form">
		                                <input id="rdo1-3" name="rdo1" type="radio" value="">
		                                <label for="rdo1-3">완료</label>
		                            </div> -->
									<form:radiobutton path="progression" value="" label="${op:message('M00039')}" checked="checked"/><!-- 전체 -->
									<%-- <form:radiobutton path="progression" value="1" label="미진행"  /><!-- 미진행 --> --%>
									<form:radiobutton path="progression" value="2" label="진행중"  /><!-- 진행중 -->
									<form:radiobutton path="progression" value="3" label="진행완료" /><!-- 진행완료 -->
		                        </div>
		                    </td>
		                    <td class="label"><c:out value="${op:message('M00191')}"/></td><!-- 공개유무 -->
		                    <td>
		                        <div class="flex_box gap-12">
		                            <!-- <div class="input-form">
		                                <input id="rdo2-1" name="rdo2" type="radio" value="" checked="checked">
		                                <label for="rdo2-1">전체</label>
		                            </div>
		                            <div class="input-form">
		                                <input id="rdo2-2" name="rdo2" type="radio" value="">
		                                <label for="rdo2-2">공개</label>
		                            </div>
		                            <div class="input-form">
		                                <input id="rdo2-3" name="rdo2" type="radio" value="">
		                                <label for="rdo2-3">비공개</label>
		                            </div> -->
									<form:radiobutton path="displayListFlag" value="" checked="checked" label="${op:message('M00039')}" /> <!-- 전체 -->
									<form:radiobutton path="displayListFlag" value="Y" label="${op:message('M00096')}" /> <!-- 공개 -->
									<form:radiobutton path="displayListFlag" value="N" label="${op:message('M00097')}" /> <!-- 비공개 -->
		                        </div>
		                    </td>
		                </tr>
		            </tbody>
		        </table>
				<div class="btn_all btn_left">
					<ul class="list-bullet point">
						<li>
					    	검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
					    </li>
				</div>
		        <div class="btn_all btn_right">
		            <div class="flex_box gap-08">
		                <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/${fn:escapeXml(featuredTypeUri)}/list';"><c:out value="${op:message('M00047')}"/></button><!-- 초기화 -->
		                <button type="submit" class="btn btn-dark-gray btn-mini" ><c:out value="${op:message('M00048')}"/></button><!-- 검색 -->
		            </div>
		        </div>
		    </div>
	    </div>
	    <!-- // 조회 영역 -->

	    <!-- 결과 영역 -->
	    <div class="count_title mt-40">
	        <h5>총 <c:out value="${featuredCount}"/><c:out value="${op:message('M00272')}"/></h5>	<!-- 총 n,nnn,nnn건 -->
	        <span>
	            <form:select path="itemsPerPage" title="화면출력수">
	                <option value="10" ${itemsPerPage == '10' ? 'selected="selected"' : ''}><c:out value="${op:message('M00240')}"/></option><!-- 10개 출력 -->
	                <option value="20" ${itemsPerPage == '20' ? 'selected="selected"' : ''}><c:out value="${op:message('M00241')}"/></option><!-- 20개 출력 -->
	                <option value="50" ${itemsPerPage == '50' ? 'selected="selected"' : ''}><c:out value="${op:message('M00242')}"/></option><!-- 50개 출력 -->
	                <option value="100" ${itemsPerPage == '100' ? 'selected="selected"' : ''}><c:out value="${op:message('M00243')}"/></option><!-- 100개 출력 -->
	                <%-- <option value="1" ${itemsPerPage == '1' ? 'selected="selected"' : ''}>1개 출력</option> --%>
	            </form:select>
	        </span>
	    </div>
    </form:form>

	<form id="listForm" method="post" action="/opmanager/${fn:escapeXml(featuredTypeUri)}/checked-delete">
		<input type="hidden" name="featuredCode" value="${fn:escapeXml(featuredParam.featuredCodeChecked)}" />
	    <div class="board_list">
	        <table class="board_list_table">
	            <colgroup>
	            	<c:if test="${op:hasRole('ROLE_ADMIN_5')
                			 || op:hasRole('ROLE_ADMIN_6')}">
	                <col style="width:50px;">
	                </c:if>
	                <col style="width:50px;">

			        <c:if test="${op:hasRole('ROLE_ADMIN_1')
			        			 || op:hasRole('ROLE_ADMIN_2')
			        			 || op:hasRole('ROLE_ADMIN_3')
			        			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
	                <col style="width:200px;">
	                </c:if>
	                <col style="width:500px;">
	                <col style="width:150px;">
	                <col style="width:150px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	            </colgroup>
	            <thead>
	                <tr>
	                	<c:if test="${op:hasRole('ROLE_ADMIN_5')
                			 || op:hasRole('ROLE_ADMIN_6')}">
	                    <th scope="col"><input type="checkbox" id="check_all"></th>
	                    </c:if>
	                    <th scope="col">No.</th><!-- No. -->

				        <c:if test="${op:hasRole('ROLE_ADMIN_1')
				        			 || op:hasRole('ROLE_ADMIN_2')
				        			 || op:hasRole('ROLE_ADMIN_3')
				        			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
	                    <th scope="col"><c:out value="${op:message('지자체')}"/></th><!-- 지자체 -->
	                    </c:if>
	                    <th scope="col"><c:out value="${op:message('이벤트명')}"/></th><!-- 이벤트명 -->
	                    <th scope="col"><c:out value="${op:message('M00032')}"/></th><!-- 진행상태 -->
	                    <th scope="col"><c:out value="${op:message('M01431')}"/></th><!-- 공개여부 -->
	                    <th scope="col">진행기간</th><!-- 진행기간 -->
	                    <th scope="col"><c:out value="${op:message('M00202')}"/></th><!-- 등록일 -->
	                </tr>
	            </thead>
	            <tbody>
	            	<c:forEach items="${featuredList}" var="list" varStatus="i">
		                <tr style="background:#fff;">
		                	<c:if test="${op:hasRole('ROLE_ADMIN_5')
                			 	|| op:hasRole('ROLE_ADMIN_6')}">
		                    <td>
		                       <div>
		                           <input type="checkbox" name="featuredIds" id="check" value="${fn:escapeXml(list.featuredId)}">
		                       </div>
		                    </td>
		                    </c:if>
		                    <td>
		                        <div><%-- ${list.featuredId} --%><c:out value="${pagination.itemNumber - i.count}"/></div>
		                    </td>

					        <c:if test="${op:hasRole('ROLE_ADMIN_1')
					        			 || op:hasRole('ROLE_ADMIN_2')
					        			 || op:hasRole('ROLE_ADMIN_3')
					        			 || op:hasRole('ROLE_ADMIN_4')}"><%-- 시스템/행안부 관리자만 보임 --%>
		                    <td>
		                        <div><c:out value="${list.locgovName}"/></div>
		                    </td>
		                    </c:if>
		                    <td>
		                        <div>
		                            <a href="/opmanager/${fn:escapeXml(featuredTypeUri)}/edit/${fn:escapeXml(list.featuredId)}"><c:out value="${list.featuredName}"/></a>
		                        </div>
		                    </td>
		                    <td>
		                        <div>
		                        	<c:choose>
										<c:when test="${list.progression eq '1' }">미진행</c:when><%-- 미진행 --%>
										<c:when test="${list.progression eq '2' }">진행중</c:when><%-- 진행중 --%>
										<c:when test="${list.progression eq '3' }">진행완료</c:when><%-- 진행완료 --%>
									</c:choose>
								</div>
		                    </td>
		                    <td>
		                        <div><c:out value="${fn:replace(fn:replace(list.displayListFlag,'Y',op:message('M00096')),'N',op:message('M00097'))}"/></div><!-- 공개 --><!-- 비공개 -->
		                    </td>
		                    <td>
		                        <div>
		                        	<c:choose>
										<c:when test="${list.startDate ne '99999999' and list.endDate eq '99999999'}"><c:out value="${op:date(list.startDate)}"/> ~ </c:when>
										<c:when test="${list.startDate eq '99999999' and list.endDate eq '99999999'}">상시 게시</c:when><%-- 상시 게시 --%>
										<c:when test="${list.startDate != '' and list.startDate ne '' }"><c:out value="${op:date(list.startDate)}"/> ~ <c:out value="${op:date(list.endDate)}"/></c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose>
	                        	</div>
		                    </td>
		                    <td>
		                        <div><c:out value="${op:date(list.createdDate)}"/></div>
		                    </td>
		                </tr>
	                </c:forEach>
	            </tbody>
	        </table>
			<c:if test="${empty featuredList}">
				<div class="no_content">
					<c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

			<c:if test="${op:hasRole('ROLE_ADMIN_5')
                			 || op:hasRole('ROLE_ADMIN_6')}">
	        <!-- 하단 버튼 -->
	        <div class="btn_all btn_right">
	            <div class="flex_box gap-08">
	                <button type="button" class="btn btn-dark-gray btn-mini" onclick="document.location.href='/opmanager/${fn:escapeXml(featuredTypeUri)}/create';"><c:out value="${op:message('M00088')}"/></button><!-- 등록 -->
	                <button id="deleteBtn" type="button" class="btn btn-default btn-mini"><c:out value="${op:message('M00074')}"/></button><!-- 삭제 -->
	            </div>
	        </div>
	        <!-- // 하단 버튼 -->
	        </c:if>

	        <div class="pagination-wrap">
	        	<page:pagination-manager />
	        </div>
	    </div>
    </form>
    <!-- // 결과 영역 -->


<!-- <style>
td {background: #fff;}
.sortable-placeholder td {
	height: 70px;
	background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
	opacity: 0.5;
}
#change_ordering, #change_ordering2 {
	display: none;
}
#change_ordering2 {
	position:fixed; right:0; bottom: 166px; z-index: 1000;
}
</style> -->


<script type="text/javascript">
$(function(){
	$("#deleteBtn").on("click",function(){
		//if($("input[name='id']:checked").size() > 0){
			Common.confirm(Message.get("M00196"),function(){	// 삭제하시겠습니까?
				if($("input[name='featuredIds']:checked").size() > 0){
					$("#listForm").submit();
				} else {
	                alert(Message.get("M00308"));
	            }
	        });
	});


	// 화면 로딩 후 시도, 시군구 정보 세팅
	if ("${fn:escapeXml(param.upperLocgovCode)}" != "") {		// 세팅된 시도 값 있을 경우
    	$("#selectSidoCode").val("${fn:escapeXml(param.upperLocgovCode)}").prop("selected", true);		// 시도 값 세팅
        searchSigungu(document.getElementById("selectSidoCode"));		// 시군구 목록 조회
    }
});


// 페이지 당 조회수 파라미터 세팅
function setItemPerPage() {
	let form = document.getElementById("featuredParam");
	let itemCntPerPage = document.getElementById("perPageCntStr");
	let pageElement = document.getElementById("selectItemPerPage");
	if (itemCntPerPage) {
		itemCntPerPage.value = pageElement.value;
	} else {
		itemCntPerPage = document.createElement("input");
		itemCntPerPage.setAttribute("id", "perPageCntStr");
		itemCntPerPage.setAttribute("type", "hidden");
		itemCntPerPage.setAttribute("name", "itemsPerPage");
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
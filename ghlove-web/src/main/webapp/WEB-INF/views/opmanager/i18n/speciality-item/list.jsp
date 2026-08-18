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
			<h3><span>${op:message('특산물관 관리')}</span></h3><!-- 특산물관 관리 -->
			<!-- // 상단 타이틀 영역 -->

			<!-- 조회 영역 -->
			<form:form modelAttribute="searchParams" method="post">
				<div class="board_write">
				    <table class="board_write_table" summary="">
				        <colgroup>
				            <col style="width:220px;">
				            <col />
				        </colgroup>
				        <tbody>
				            <tr>
				                <td class="label">${op:message('지자체')}</td><!-- 지자체 -->
				                <td>
				                    <div class="flex_box gap-12">
				                        <form:select path="upperLocgovCode" title="${op:message('-시,도-')}" class="wd-150" onchange="javascript:searchSigungu(this);"><%-- 시,도 --%>
				                            <option value="">${op:message('-시,도-')}</option>
				                            <c:forEach items="${sido}" var="item">
				                            	<option value="${fn:escapeXml(item.value)}">${fn:escapeXml(item.label)}</option>
				                            </c:forEach>
				                        </form:select>
				                        <form:select path="locgovCode" title="${op:message('-시,군,구-')}" class="wd-150"><%-- 시,군,구 --%>
				                            <option value="">${op:message('-시,군,구-')}</option>
				                        </form:select>
				                    </div>
				                </td>
				            </tr>
				            <tr>
				                <td class="label">${op:message('키워드')}</td><!-- 키워드 -->
				                <td>
				                    <div class="flex_box gap-08">
				                        <form:input path="keywords" title="${op:message('키워드')}" class="input_txt required _filter full" type="text" value="" /><!-- 키워드 -->
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
				            <button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:document.location.href='/opmanager/speciality-item/list'">${op:message('M00047')}</button><!-- 초기화 -->
				            <button type="submit" class="btn btn-dark-gray btn-mini">${op:message('M00048')}</button><!-- 검색 -->
				        </div>
				    </div>
				</div>
				<!-- // 조회 영역 -->

				<!-- 결과 영역 -->
				<div class="count_title mt-40">
				    <h5>총 ${fn:escapeXml(specialityItemManageCount)}${op:message('건')}</h5>
				    <span>
				        <form:select path="itemsPerPage" title="${op:message('화면출력수')}"><!-- 화면출력수 -->
			                <option value="10" ${searchParams.itemsPerPage == '10' ? 'selected="selected"' : ''}>${op:message('M00240')}</option><!-- 10개 출력 -->
			                <option value="20" ${searchParams.itemsPerPage == '20' ? 'selected="selected"' : ''}>${op:message('M00241')}</option><!-- 20개 출력 -->
			                <option value="50" ${searchParams.itemsPerPage == '50' ? 'selected="selected"' : ''}>${op:message('M00242')}</option><!-- 50개 출력 -->
			                <option value="100" ${searchParams.itemsPerPage == '100' ? 'selected="selected"' : ''}>${op:message('M00243')}</option><!-- 100개 출력 -->
			                <%-- <option value="1" ${searchParams.itemsPerPage == '1' ? 'selected="selected"' : ''}>1개 출력</option> --%>
				        </form:select>
				    </span>
				</div>
			</form:form>

			<div class="board_list">
			    <table class="board_list_table">
			        <colgroup>
			            <col style="width:50px;">
			            <col style="width:100px;">
			            <col style="width:500px;">
			            <col style="width:200px;">
			        </colgroup>
			        <thead>
			            <tr>
			                <th scope="col">${op:message('No.')}</th><!-- No. -->
			                <th scope="col">${op:message('지자체')}</th><!-- 지자체 -->
			                <th scope="col">${op:message('소개')}</th><!-- 소개 -->
			                <th scope="col">${op:message('키워드')}</th><!-- 키워드 -->
			            </tr>
			        </thead>
			        <tbody>
			        	<c:forEach items="${specialityItemManageList}" var="item">
				            <tr style="background:#fff;">
				                <td>
				                    <%-- <div>${item.specialityItemManageId}</div> --%>
				                    <div>${specialityItemManageCount - item.rownum + 1}</div>
				                </td>
				                <td>
				                    <div>
				                        <a href="/opmanager/speciality-item/form/${fn:escapeXml(item.specialityItemManageId)}">${fn:escapeXml(item.locgovNm)}</a>
				                    </div>
				                </td>
				                <td>
				                    <div class="text-left">${fn:escapeXml(item.specialityItemInfo)}</div>
				                </td>
				                <td>
				                    <div>${fn:escapeXml(item.keywords)}</div>
				                </td>
				            </tr>
			        	</c:forEach>
			        </tbody>
			    </table>

				<c:if test="${empty specialityItemManageList}">
					<div class="no_content">
						${op:message('M00473')} <!-- 데이터가 없습니다. -->
					</div>
				</c:if>

		        <div class="pagination-wrap">
		        	<page:pagination-manager />
		        </div>
			</div>
			<!-- // 결과 영역 -->

<script type="text/javascript">
	$(function(){
		// 화면 로딩 후 시도, 시군구 정보 세팅
		if ("${fn:escapeXml(param.upperLocgovCode)}" != "") {		// 세팅된 시도 값 있을 경우
	    	$("#upperLocgovCode").val("${fn:escapeXml(param.upperLocgovCode)}").prop("selected", true);		// 시도 값 세팅
	        searchSigungu(document.getElementById("upperLocgovCode"));		// 시군구 목록 조회
	    }
	})

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
				            if ("${fn:escapeXml(param.locgovCode)}" != "") {
				            	$("#locgovCode").val("${fn:escapeXml(param.locgovCode)}").prop("selected", true);

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

</script>
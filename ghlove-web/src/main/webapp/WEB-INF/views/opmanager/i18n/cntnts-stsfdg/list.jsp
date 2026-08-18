<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<h3><span>콘텐츠 만족도</span></h3>
	<form:form modelAttribute="searchParam" method="post">

	<div class="board_write">
	    <table class="board_write_table" summary="콘텐츠 만족도">
	        <colgroup>
	            <col style="width:220px;">
	        </colgroup>
	        <tbody>
	            <tr>
	                <td class="label">URL</td>
	                <td>
	                    <div>
	                        <form:input path="menuUrl" title="URL" class="input_txt required _filter half"
	                            type="text" value="" />
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
	            <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/cntnts-stsfdg/list';">초기화</button>
	            <button type="submit" class="btn btn-dark-gray btn-mini">검색</button>
	        </div>
	    </div>
	</div>

	<div class="board_list mt-40">
	    <div class="count_title mt-40">
	        <h5>총 ${fn:escapeXml(count)}건</h5>
	        <span>
	            <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();"> <!-- 화면출력 -->
		        <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
		        <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
		        <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
		        <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
		   </form:select>
	        </span>
	    </div>
</form:form>

	    <table class="board_list_table" summary="콘텐츠 만족도">
	        <caption>콘텐츠 만족도</caption>
	        <colgroup>
	            <col style="width:50px;">
	            <col style="width:500px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	            <col style="width:100px;">
	        </colgroup>
	        <thead>
	            <tr>
	                <th scope="col">No.</th>
	                <th scope="col">URL</th>
	                <th scope="col">매우 만족</th>
	                <th scope="col">만족</th>
	                <th scope="col">불만족</th>
	                <th scope="col">매우 불만족</th>
	                <th scope="col"></th>
	            </tr>
	        </thead>
	        <tbody>
	        	<c:forEach items="${list}" var="list" varStatus="i">
	            <c:if test="${list.menuUrl ne '/donation/guide4.html' && list.menuUrl ne '/donation/map-select.html'}">
		            <tr style="background:#fff;">
		                <td>
		                    <div>
		                        ${fn:escapeXml(pagination.itemNumber - i.count)}
		                    </div>
		                </td>
		                <td>
		                    <div>
		                        <a href="${op:property('saleson.url.frontend')}${fn:escapeXml(list.menuUrl )}" target="_black">${fn:escapeXml(list.menuUrl )} (${fn:escapeXml(list.menuName)})</a>
		                    </div>
		                </td>
		                <td>
		                    <div>
		                        ${fn:escapeXml(list.stsfdg4 )}
		                    </div>
		                </td>
		                <td>
		                    <div>
								${fn:escapeXml(list.stsfdg3 )}
		                    </div>
		                </td>
		                <td>
		                    <div>
								${fn:escapeXml(list.stsfdg2 )}
		                    </div>
		                </td>
		                <td>
		                    <div>
		                    	${fn:escapeXml(list.stsfdg1 )}
		                    </div>
		                </td>
		                <td>
		                    <div>
		                        <a href="javascript:detail('${fn:escapeXml(list.menuUrl )}')">상세보기</a>
		                    </div>
		                </td>
		            </tr>
	            </c:if>
	            </c:forEach>
	        </tbody>
	    </table>
	    <c:if test="${empty list}">
		    <div class="no_content">
		        ${op:message('M00473')}
		    </div>
	    </c:if>
	    <div class="pagination-wrap">
	        <page:pagination-manager />
	    </div>
	</div>

	<div style="display: none;">
		<span id="today">${fn:escapeXml(today)}</span>
		<span id="week">${fn:escapeXml(week)}</span>
		<span id="month1">${fn:escapeXml(month1)}</span>
		<span id="month2">${fn:escapeXml(month2)}</span>
	</div>

<script type="text/javascript">
$(function(){

})

function detail(url) {
	location.href = '/opmanager/cntnts-stsfdg/detail?menuUrl='+url;
}


</script>
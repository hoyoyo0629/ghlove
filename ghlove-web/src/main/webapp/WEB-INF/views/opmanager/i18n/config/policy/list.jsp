<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>

<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>


<form:form modelAttribute="policyParam" method="get">
    <h3><span><c:out value="${op:message('M00756')}"/></span></h3>

    <div class="board_write">
        <table class="board_write_table" summary="${op:message('M00459')}">
            <caption><c:out value="${op:message('M00459')}"/></caption>
            <colgroup>
                <col style="width:220px;">
            </colgroup>
            <tbody>
            <tr>
                <td class="label"><c:out value="${op:message('M00011')}"/></td> <!-- 검색구분 -->
                <td>
                    <div class="flex_box gap-08">

                        <form:select path="where" title="${op:message('M00011')}" class="wd-150">
<%--                             <form:option value="">전체</form:option> --%>
                            <form:option value="TITLE">제목</form:option>
                            <form:option value="CONTENT">내용</form:option>
                        </form:select>
                        <form:input type="text" path="query" class="input_txt required _filter full" title="${op:message('M00021')}" /> <!-- 검색어 입력 -->
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label">정책구분</td>
                <td>
                    <div class="flex_box gap-12">
                    	<div class="input-form">
                        	<form:radiobutton path="policyType" value="" label="전체" checked="checked" />
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="policyType" value="0" label="약관" />   <!-- 공개 -->
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="policyType" value="1" label="개인정보처리방침" /> <!-- 비공개 -->
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="policyType" value="4" label="개인정보수집 제3자 이용 동의" />
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="policyType" value="5" label="저작권정책" />
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="policyType" value="6" label="개인정보 수집·이용 동의" />
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label">전시 여부</td>
                <td>
                    <div class="flex_box gap-12">
                    	<div class="input-form">
                        	<form:radiobutton path="exhibitionStatus" value="" label="전체" checked="checked" />
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="exhibitionStatus" value="Y" label="공개" />   <!-- 공개 -->
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="exhibitionStatus" value="N" label="비공개" /> <!-- 비공개 -->
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div> <!-- // board_write -->

    <!-- 버튼시작 -->
				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/config/policy/list'"><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
						<button type="submit" class="btn btn-default btn-mini"></span> <c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
					</div>
				</div>


<%--     <div class="btn_all">
        <div class="btn_left">

            <button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/config/policy/list';"><span>${op:message('M00047')}</span></button> <!-- 초기화 -->
        </div>
        <div class="btn_right">
            <button type="submit" class="btn btn-dark-gray btn-sm"><span>${op:message('M00048')}</span></button> <!-- 검색 -->
        </div>
    </div> --%>
    <!-- 버튼 끝-->

    <div class="count_title mt-40">
        <h5>
                총 <c:out value='${totalCount}'/>건
        </h5>	 <!-- 전체 -->   <!-- 건 조회 -->
        <span>
            <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#policyParam').submit();"> <!-- 화면출력 -->
                <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
                <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
                <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
                <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
            </form:select>
        </span>
    </div>
</form:form>

<form id="listForm">
    <div class="board_write">
        <table class="board_list_table" summary="${op:message('M00273')}"> <!-- 주문내역 리스트 -->
            <caption><c:out value="${op:message('M00273')}"/></caption>
            <colgroup>
                <col style="width:50px;">
                <col style="width:50px;">
                <col style="width:150px;">
                <col style="width:500px;">
                <col style="width:100px;">
                <col style="width:100px;">
                <col style="width:150px;">
            </colgroup>
            <thead>
            <tr>
                <th scope="col"><input type="checkbox" id="check_all" title="${op:message('M00169')}" /></th>
                <th scope="col">No.</th> <!-- 순번 -->
                <th scope="col">정책구분</th> <!-- 정책구분 -->
                <th scope="col">제목</th> <!-- 제목 -->
                <th scope="col">전시 여부</th> <!-- 전시 여부 -->
                <th scope="col">작성자</th> <!-- 수정자 -->
                <th scope="col">작성일</th> <!-- 수정일 -->
            </tr>
            </thead>
            <tbody>
            <c:forEach var="policys" items="${policyList}" varStatus="i">
                <tr>
                    <td><input type="checkbox" name="id" id="check" value="${fn:escapeXml(policys.policyId)}" title="체크박스" /></td>
                    <td><c:out value="${pagination.itemNumber - i.count}"/></td>

                    <td><c:out value="${policys.policyTypeLabel}"/></td>

                    <td>
                        <div>
                            <a href="/opmanager/config/policy/detail/${fn:escapeXml(policys.policyId)}"><c:out value="${policys.title}"/></a>
                        </div>
                    </td>

                    <td><c:out value='${policys.exhibitionStatus == "Y" ? "공개" : "비공개"}'/></td>

                    <td>
                        <c:choose>
                            <c:when test="${empty policys.updatedLoginId}">
                                -
                            </c:when>
                            <c:otherwise>
                                <c:out value="${policys.updatedLoginId}"/>
                            </c:otherwise>

                        </c:choose>

                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${empty policys.createdDate}">
                                -
                            </c:when>
                            <c:otherwise>
                                <c:out value="${op:date(policys.createdDate)}"/>
                            </c:otherwise>

                        </c:choose>

                    </td>

<!--                     <td> -->
<%--                         <a href="javascript:deleteCheck('${policys.policyId}');" class="btn btn-gradient btn-xs">${op:message('M00074')}</a> --%>
<!--                     </td> -->
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div><!--// board_write E-->

    <c:if test="${empty policyList}">
        <div class="no_content">
                <c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
        </div>
    </c:if>

<%--     <div class="btn_all">
        <div class="btn_left mb0">
            <a id="delete_list_data" href="#" class="btn btn-default btn-sm">${op:message('M00576')}</a> <!-- 선택삭제 -->
        </div>

        <div class="btn_right">
            <a href="/opmanager/config/policy/create" class="btn btn-active btn-sm">${op:message('M00088')} </a> <!-- 등록 -->
        </div>
    </div> --%>

		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a id="delete_list_data" href="#"><button type="button" class="btn btn-dark-gray btn-mini">삭제</button></a> <!-- 선택삭제 -->
				<a href="<c:url value="/opmanager/config/policy/create" />"><button type="button" class="btn btn-dark-default btn-mini">등록</button></a> <!-- 등록 -->

				<%-- <a href="javascript:deleteCheckNotice(${notice.noticeId})" class="btn btn-dark-gray btn-mini">${op:message('M00074')}</a> --%>
			</div>
		</div>

		<div class="pagination-wrap">
	      	<page:pagination-manager />
	  	</div>

</form>

<script type="text/javascript">
    $(function() {
        Common.DateButtonEvent.set('.day_btns > a[class^=table_btn]', '', 'input[name="exhibitionStartDate"]' , 'input[name="exhibitionEndDate"]');

        //목록데이터 - 삭제처리
        $('#delete_list_data').on('click', function() {
            Common.updateListData("/opmanager/config/policy/delete-list", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?
        });
    });

    function deleteCheck(policyId) {
        if (confirm("해당 약관을 " + Message.get("M00196"))) {		// 해당 약관을 삭제하시겠습니까?
            location.replace("/opmanager/config/policy/delete/" + policyId);
        } else {
            return;
        }
    }

</script>
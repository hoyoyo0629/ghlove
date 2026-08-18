<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>


<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>약관관리</span></h3>

<form:form modelAttribute="policy" method="post" enctype="multipart/form-data">

<c:if test="${not empty policy.title}">
	<div class="btn_all btn_right mb15">
	    <div class="flex_box gap-08">
	        <button type="submit" class="btn btn-dark-gray btn-mini">수정</button>
	        <button type="button" class="btn btn-dark-gray btn-mini" onclick="deleteCheck('${fn:escapeXml(policy.policyId)}')">삭제</button>
	        <button type="button" class="btn btn-dark-default btn-mini" onClick="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/config/policy/list')">목록</button>
	    </div>
	</div>
</c:if>


<div class="board_write mt20">

        <table class="board_write_table" cellpadding="0" cellspacing="0" summary="${op:message('M00206')}">
            <caption><c:out value="${op:message('M00206')}"/></caption>
            <colgroup>
                <col style="width:220px;">
            </colgroup>
            <tbody>
            <tr>
                <td class="label">정책 타입</td>
                <td>
                    <div class="flex_box gap-12">
                    	<div class="input-form">
                        	<form:radiobutton path="policyType" value="0" label="약관" checked="checked" />
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="policyType" value="1" label="개인정보처리방침" />
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
                <td class="label"><span class="required_mark">*</span>제목</td>
                <td>
                    <div class="flex_box gap-12">
                        <form:input path="title" title="제목"  class="input_txt required _filter half" maxlength="70"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="label"><span class="required_mark">*</span>전시 여부</td>
                <td>
                    <div class="flex_box gap-12">
                    	<div class="input-form">
                        	<form:radiobutton path="exhibitionStatus" value="Y" label="공개" checked="checked" />
                        </div>
                        <div class="input-form">
                        	<form:radiobutton path="exhibitionStatus" value="N" label="비공개" />
                        </div>
                    </div>
                </td>
            </tr>

            </tbody>
        </table>
        <table class="board_write_table mt30" summary="">
	        <colgroup>
	            <col style="width:220px;">
	        </colgroup>
	        <tbody>

	            <tr>
	                <td class="label"><span class="required_mark">*</span>내용</td>
	                <td>
	                    <!-- smart_editor2 area -->
		                <div>
		                    <form:textarea path="content" cols="100" rows="20" class="editor-content" title="${op:message('M00006')}" />
		                </div>
		                <!-- smart_editor2 area -->
	                </td>
	            </tr>
	        </tbody>
	    </table>

	    <c:if test="${empty policy.title}">
		    <div class="btn_all btn_center">
		        <div class="flex_box gap-08">
		            <button type="submit" class="btn btn-dark-gray btn-small">등록</button>
		            <a href="javascript:Link.list('${fn:escapeXml(requestContext.managerUri)}/config/policy/list')"><button type="button" class="btn btn-dark-default btn-small">취소</button></a>
		        </div>
		    </div>
	    </c:if>



        <%-- <br/>
        <tr style="width: auto;">
            <td>
                <div>
                    <form:textarea path="content" cols="100" rows="20" class="editor-content editor-lg" style="width:100%;" title="내용" />
                </div>
            </td>
        </tr>

        <p class="btn_center">
            <button type="submit" class="btn btn-active">${op:message('M00101')}</button>
            <a href="javascript:Link.list('${requestContext.managerUri}/config/policy/list')" class="btn btn-default">${op:message('M00480')}</a>	<!-- 목록 -->
        </p> --%>

</div><!--//board_write E-->

</form:form>

<module:smarteditorInit />
<module:smarteditor id="content" />

<script type="text/javascript">
    $(function() {

        /*Common.DateButtonEvent.set('.day_btns > a[class^=table_btn]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');*/

        // validator
        $('#policy').validator(
            function() {

                return Common.getEditorContent("content", true);

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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>

<style type="text/css">
    .board_write_table .label p {
        margin-left: 20px;
    }
</style>

<h3><span>관리자 접속IP관리</span></h3>

<form:form modelAttribute="access" method="post">
    <form:hidden path="allowIpId"/>

<div class="board_write">
	<div class="text2">
		&nbsp;
	</div>
	<table class="board_write_table">
		<colgroup>
			<col style="width: 220px;">
		</colgroup>
		<tbody>
			 
            <tr>
                <td class="label"><span class="required_mark">*</span>접근 타입</td>
                <td>
                    <div class="flex_box gap-08 item-center">
                        <div class="input-form">
                            <form:radiobutton path="accessType" value="1"  id="rdo1-1" cssClass="full required " title="접근 타입"/>
                            <label for="rdo1-1">관리자</label>
                        </div>
                        <div class="input-form">
                            <form:radiobutton path="accessType" value="2" id="rdo1-2" cssClass="full required " title="접근 타입"/>
                            <label for="rdo1-2">판매자</label>
                        </div>
                    </div>
                </td>
            </tr>

            <tr>
                <td class="label"><span class="required_mark">*</span>IP 주소</td>
                <td>
                    <div class="flex_box gap-08 item-center">
                        <form:input path="remoteAddr" cssClass="required wd-200" title="IP 주소"/>
                        <span class="tip">IP 입력시 111.111.111.111 이면 한 개의 IP 111.111.111.111 만 접근 // 111.111.111.* 이면 111.111.111.* 의 IP 대역 접근</span>
                    </div>
                </td>
            </tr>
        </tbody>
	</table>	
	
	<div class="btn_all btn_right">
        <div class="flex_box gap-08">
            <button type="submit" class="btn btn-dark-gray btn-mini">등록</button>
            <a href="/opmanager/access/list" class="btn btn-default btn-mini">목록</a>
        </div>
	</div>
</div>
</form:form>

<script type="text/javascript">
$(function(){
	// validator
	$('#access').validator(function() {
		
	});
});
function fn_delete(){
	 if(confirm("접속IP를 삭제하시겠습니까?")){
		 $("#deleteForm").submit();
	 }
}
</script>
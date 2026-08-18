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


<div class="popup_wrap">
    <div id="pop_header">
        <h1 class="popup_title">소식지 등록</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
    <div class="popup_contents">
		<form:form id="listForm" modelAttribute="catalogMngParam" method="post">
			<form:hidden path="rowNumber" value="${fn:escapeXml(catalog.rowNumber）}" />
			<form:hidden path="duplicate" value="${fn:escapeXml(duplicate）}" />

			<div class="board_write">
				<table class="board_write_table">
					<caption>소식지 등록</caption>
					<colgroup>
						<col style="width:220px;">
						<col style="width:auto;">
					</colgroup>
					<tbody>
						<tr>
							<td class="label"><span class="required_mark">*</span>발간호</td>
							<td>
								<div class="flex_box gap-08">
				                    <c:choose>
									    <c:when test="${not empty rowNumber}">
									        <form:input path="catalogYearStr" maxlength="4" readonly="true" />
									    </c:when>
									    <c:otherwise>
									        <form:input path="catalogYearStr" maxlength="4" value="" />
									    </c:otherwise>
									</c:choose> 년
									<c:choose>
									    <c:when test="${not empty rowNumber}">
									        <form:input path="catalogNoStr" maxlength="4" readonly="true" />
									    </c:when>
									    <c:otherwise>
									        <form:input path="catalogNoStr" maxlength="4" value="" />
									    </c:otherwise>
									</c:choose> 호

				                </div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>표시여부</td>
							<td>
								<div class="admin_wrap flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="displayYn" value="Y" label="표시" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="displayYn" value="N" label="미표시" />
									</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
       		 </div>

			<!-- 버튼시작 -->
			<div class="popup_btns">
				<button type="button" id="saveButton" class="btn btn-active"><c:out value="${op:message('M00101')}"/></button><!-- 저장 -->
				<a href="JavaScript:self.close();" class="btn btn-default"><span><c:out value="${op:message('M00569')}"/></span></a> <!-- 닫기 -->
			</div>
			<!-- 버튼 끝-->
		</form:form>
    </div>
</div>


<script type="text/javascript">

	$(document).ready(function() {
	    // URL에서 파라미터 추출
	    var param = new URLSearchParams(window.location.search);
	    var rowNumber = param.get('rowNumber');
	    var catalogYearStr = param.get('catalogYear');
	    var catalogNoStr = param.get('catalogNo');
	    var $form = $('#listForm');
	    var msg = '${op:message("M00406")}'; // 등록되었습니다.

       	var url = rowNumber ? "/opmanager/catalog/catalogMng/update" : "/opmanager/catalog/catalogMng/create";

	 	// rowNumber가 존재하면 update
	    if (rowNumber) {
	        // 파라미터를 사용하여 URL 생성
	        $.get('/opmanager/catalog/catalogMng/update?rowNumber=' + rowNumber, function(data) {
	            $("#catalogYearStr").val(catalogYearStr);
	            $("#catalogNoStr").val(catalogNoStr);
	            $("input[name='displayYn'][value='" + data.displayYn + "']").prop("checked", true);
	        });
	        $('#saveButton').on('click', function() {
		        $.post(url, $form.serialize(), function(response) {
		            alert(msg);
		            opener.location.reload(); // 부모 창 새로 고침
		            window.close(); // 팝업 닫기
		        });
	        });
	    }

	    if (!rowNumber) {
		    // 등록 버튼
		    $('#saveButton').on('click', function() {
		        // 공백체크
		        if (!$("#catalogYearStr").val() || $("#catalogYearStr").val() === '0') {
		            alert("연도를 입력해 주세요.");
		            return;
		        }
		        if (!$("#catalogNoStr").val() || $("#catalogNoStr").val() === '0') {
		            alert("호수를 입력해 주세요.");
		            return;
		        }

		        // json 타입으로 데이터 전송
		        var formData = {
	        	    "catalogYearStr": $("#catalogYearStr").val(),
	        	    "catalogNoStr": $("#catalogNoStr").val(),
	        	    "displayYn": $("input[name='displayYn']:checked").val(),
	        	    "rowNumber": param.get('rowNumber')
	        	};

		     	// 중복체크
		        $.ajax({
	// 	            url: "/opmanager/catalog/catalogMng/create",
		            url: url,
		            type: "POST",
				    data: JSON.stringify(formData),
				    contentType: "application/json", // 데이터 형식을 JSON으로 지정
		            success: function(response) {
		                if ( response.data === 'failure' ) {
		                    alert("중복된 소식지입니다. 다른 값을 입력해주세요.");
		                } else {
		                    alert("등록되었습니다.");
		                    opener.location.reload(); // 부모 창 새로 고침
		                    window.close(); // 팝업 닫기
		                }
		            },
		            error: function(xhr, status, error) {
		                alert("등록 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
		            }
		        });
		    });
	    }

	});


</script>

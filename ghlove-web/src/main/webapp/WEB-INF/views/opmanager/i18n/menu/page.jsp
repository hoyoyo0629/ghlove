<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

		<div class="location">
            <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
        </div>
        <h3><span><!-- 상세 --></span></h3>
		<form:form modelAttribute="menu" method="post" enctype="multipart/form-data">

	        <div class="board_write">
	            <table class="board_write_table" summary="">
	                <colgroup>
	                    <col style="width:220px;">
	                    <col>
	                </colgroup>
	                <tbody>
	                    <tr>
	                        <td class="label">메뉴명</td>
	                        <td>
	                            <div>
	                                <c:out value="${menu.menuName}"/>
	                            </div>
	                        </td>
	                    </tr>	             
	                    <tr>
	                        <td class="label">페이지 URL</td>
	                        <td>
	                            <div>
	                               <c:out value="${menu.menuUrl}"/>
	                            </div>
	                        </td>
	                    </tr>	  	                       
	                    <tr>
	                        <td class="label"><c:out value="${op:message('M00006')}"/></td>
	                        <td>
	                            <!-- smart_editor2-wrap -->
	                            <!-- <div style="width: 100%; height:300px; background:#eee;">스마트 에디터 영역 입니다.</div> -->
	                            <!-- 실 사용 소스 -->
	                            <div class="smart_editor2-wrap">
	                                <form:textarea path="menuPageContent" cols="30" rows="20" class="editor-content" title="${op:message('M00006')}" />
	                            </div>
	                            <!-- // 실 사용 소스 -->
	                            <!-- smart_editor2-wrap -->
	                        </td>
	                    </tr>
	                </tbody>
	            </table>
	        </div>
	        <div class="point mt10">※ 내용을 변경하면 이전으로 복구가 되지 않으니, 내용 수정 시 유의하시기 바랍니다. (내용을 다른 곳에 저장해 놓으신 후 변경하길 권장합니다.)</div>
            <div class="btn_all btn_center mt20">
                <div class="flex_box gap-08">
                    <button type="submit" class="btn btn-dark-gray btn-small"><c:out value="${op:message('M00101')}"/></button> <!-- 저장 -->
                    <button type="button" class="btn btn-default btn-small" onclick="document.location.href='/opmanager/menu/list';"><c:out value="${op:message('M00480')}"/></button> <!-- 목록 -->
                </div>
            </div>	        
		</form:form>


		
<module:smarteditorInit />
<module:smarteditor id="menuPageContent" />
<script type="text/javascript">

	$(function(){
	
		$('#menu').validator(function() {
			Common.getEditorContent("menuPageContent");
			
			if (!confirm("정보를 저장하시겠습니까?")) {
            	return false;
        	}	
		});
	
	});

</script>
		
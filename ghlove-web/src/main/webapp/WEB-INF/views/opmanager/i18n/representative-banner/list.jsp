<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>대표배너관리</span></h3>

<form id="listForm">
	<div class="board_list">
	    <table class="board_list_table" summary="대표배너관리">
	        <caption>대표배너관리</caption>
	        <colgroup>
	            <col style="width:500px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	            <col style="width:150px;">
	        </colgroup>
	        <thead>
	            <tr>
	                <th scope="col">배너명</th>
	                <th scope="col">사용여부</th>
	                <th scope="col">관리</th>
	                <th scope="col">순서</th>
	            </tr>
	        </thead>
	        <tbody>
	        	<c:forEach items="${bannerList}" var="banner" varStatus="i">
	        		<tr style="background:#fff;">
	        			<td>
	        				<input type="hidden" name="id" value="${fn:escapeXml(banner.representativeBannerId)}"/>
	        				<div>${fn:escapeXml(banner.title)}</div>
	        			</td>
	                    <td>
	                        <div>${fn:escapeXml(banner.useYn)}</div>
	                    </td>   
	                    <td>
	                        <div class="flex_box juc-center gap-08">
	                            <button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/representative-banner/form/${fn:escapeXml(banner.representativeBannerId)}'">수정</button>
	                        </div>
	                    </td>	 
	                    <td>
	                        <div class="flex_box juc-center">	                    
								<select id="ordering" name="ordering" title="순서" class="wd-80" value="${fn:escapeXml(banner.displayOrder)}">
									<option value="">-선택-</option>
									<c:forEach begin="1" end="15" step="1" var="index">
										<option value="${index}" label="${index}" ${op:selected(banner.displayOrder, index)}>
									</c:forEach>
								</select>	
						     </div>
	                	</td>                    
	        		</tr>
	        	</c:forEach>
	        	<c:set var="insertRow" value="${15-fn:length(bannerList)}" />
	        	<c:forEach begin="1" end="${insertRow}" step="1">
	                <tr style="background:#fff;">
	                    <td>
	                        <div></div>
	                    </td>
	                    <td>
	                        <div></div>
	                    </td>
	                    <td>
	                        <div class="flex_box juc-center gap-08">
	                            <button type="button" class="btn btn-default btn-sm" onclick="location.href='/opmanager/representative-banner/form/0'">등록</button>
	                        </div>
	                    </td>
	                    <td>
	                        <div class="flex_box juc-center">
	                            <select id="ordering" name="ordering" title="순서" class="wd-80">
									<option value="">-선택-</option>
									<c:forEach begin="1" end="15" step="1" var="index">
										<option value="${index}" label="${index}">
									</c:forEach>
	                            </select>
	                        </div>
	                    </td>
	                </tr>   
	            </c:forEach>                                                                            
	        </tbody>
	    </table>
	</div>
	
	<div class="btn_all btn_center mt40">
	    <div class="flex_box gap-08">
	        <button type="button" class="btn btn-dark-gray btn-small" onclick="changeOrdering()">저장</button>
	    </div>
	</div>
</form>
    
<script type="text/javascript">

	$(function(){
				
	});
	
	// 노출순서 번경
	function changeOrdering() {
	
    	// 지자체 관리자만 권한있음
    	if('${fn:escapeXml(adminRole)}' != 'SYS') {
    		alert("시스템 관리자만 저장이 가능합니다.");
    		return false;
    	}	
	
        var $form = $('#listForm');
        var sort = document.getElementsByName("ordering");
        var bannerCnt = ${fn:length(bannerList)};

        for(j=0; j<sort.length; j++){
            sort1 = $form.find('select[name=ordering]')[j].value;
            if("" == sort1 && (j+1) <= bannerCnt){
            	alert("배너순서를 선택하세요."); 
            	$form.find('select[name=ordering]')[j].focus();
            	return false;
            }
       
            if(sort.length > j+1){
                for(i=(j+1); i<bannerCnt; i++){
                    sort2 = $form.find('select[name=ordering]')[i].value;
                    if(sort1 == sort2){
                        alert('중복된 순서가 있어 저장되지 않았습니다. 확인해 주세요.');
                        $form.find('select[name=ordering]')[i].focus();
                        return false;
                    }
                }
            }           
        }		
	
		$.post("/opmanager/representative-banner/change-ordering", $form.serialize(), function(response) {
			Common.responseHandler(response, function(response){
				alert(Message.get("M00406"));	// 저장되었습니다.
				location.reload();
			});
		});
			
	}	

</script>
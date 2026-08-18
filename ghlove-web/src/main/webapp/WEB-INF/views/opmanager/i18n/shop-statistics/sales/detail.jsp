<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

<!-- 개발 영역 -->
<div class="popup_wrap">
    <div id="pop_header">
        <h1 class="popup_title">상세 내역</h1>
        <a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
    </div>

    <div class="popup_contents">
        <h3 class="fs24"><span><c:out value='${itemName}'/></span></h3>
        <table class="board_list_table">
		      <caption></caption>
		   <colgroup>
		       <col style="width:50px;">
		       <col style="width:100px;">
		       <col style="width:100px;">
		       <col style="width:100px;">
		       <col style="width:100px;">
		       <col style="width:100px;">
		   </colgroup>
		   <thead>
		       <tr>
		           <th scope="col" class="col-md-1" rowspan="2">No.</th>
		           <th scope="col" class="col-md-2" rowspan="2">회원명</th>
		           <th scope="col" class="col-md-3" colspan="2">주문</th>
		           <th scope="col" class="col-md-3" colspan="2">반품/환불</th>
		       </tr>
		       <tr>
		           <th scope="col" class="col-md-1">건수</th>
		           <th scope="col" class="col-md-1">금액(p)</th>
		           <th scope="col" class="col-md-1">건수</th>
		           <th scope="col" class="col-md-1">금액(p)</th>
		       </tr>
		   </thead>
		   <tbody>
		   <c:forEach items="${dateList}" var="list" varStatus="i">
		   <c:forEach items="${ list.groupStats }" var="item" varStatus="groupIndex">
		       <tr style="background:#fff;">
		           <td>
		               <div><c:out value='${pagination.itemNumber - i.count}'/></div>
		           </td>
		           <td>
		               <div>
		                   <c:out value='${list.groupObject}'/>
		               </div>
		           </td>
		           <td>
		               <div><c:out value='${op:numberFormat(item.saleCount)}'/></div>
		           </td>
		           <td>
		               <div><c:out value='${op:numberFormat(item.saleAmount)}'/></div>
		           </td>
		           <td>
		               <div><c:out value='${op:numberFormat(item.cancelCount)}'/></div>
		           </td>
		           <td>
		               <div><c:out value='${op:numberFormat(item.cancelAmount)}'/></div>
		           </td>
		       </tr>
		       </c:forEach>
		       </c:forEach>
		   </tbody>
	   </table>
	   <c:if test="${empty dateList}">
	       <div class="no_content">
	           <c:out value="${op:message('M00473')}"/>
	       </div>
	   </c:if>

	   <div class="pagination-wrap">
	       <page:pagination-manager />
	   </div>
   </div>
</div>
<!-- // 개발 영역 -->



<script type="text/javascript">

</script>

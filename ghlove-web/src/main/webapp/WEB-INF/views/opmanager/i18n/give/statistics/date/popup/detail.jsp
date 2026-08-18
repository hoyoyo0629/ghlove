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

<!-- 개발 영역 -->
    <!-- 상세 내역 popup -->
    <div class="popup_wrap">
        <div id="pop_header">
            <h1 class="popup_title">상세 내역</h1>
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

        <div class="popup_contents">
            <div>
                <div class="count_title">
                    <h5>총 <c:out value="${list.size()}" />건</h5>
                </div>
                <div class="board_list">
                    <table class="board_list_table">
                        <colgroup>
                            <col style="width:200px;">
                            <col style="width:200px;">
                            <col style="width:200px;">
                            <col style="width:300px;">
                        </colgroup>
                        <thead>
                            <tr>
                                <th scope="col">전자납부번호</th>
                                <th scope="col">기부금액</th>
                                <th scope="col">발생포인트</th>
                                <th scope="col">형태</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${list}" var="list" varStatus="i">
                                <tr style="background:#fff;">
                                    <td>
                                        <div><c:out value="${list.elctrnPayNo}" /></div>
                                    </td>
                                    <td>
                                        <div><c:out value="${op:numberFormat(list.cntrAmt)}" /></div>
                                    </td>
                                    <td>
                                        <div><c:out value="${op:numberFormat(list.cntrPoint)}" /></div>
                                    </td>
                                    <td>
                                        <div><c:out value="${list.cntrPathName}" /></div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- // 상세 내역 popup -->
    <!-- // 개발 영역 -->

<script type="text/javascript">
$(function() {
	
});

</script>


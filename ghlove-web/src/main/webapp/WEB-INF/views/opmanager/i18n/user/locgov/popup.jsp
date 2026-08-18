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
            <h1 class="popup_title">부서코드 변경이력</h1>
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
		</div>

        <div class="popup_contents">
            <div>
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
                                <th scope="col">No</th>
                                <th scope="col">변경일</th>
                                <th scope="col">부서코드</th>
                                <th scope="col">등록자</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${list.size() > 0}">
                                    <c:forEach items="${list}" var="item" varStatus="i">
                                        <tr style="background:#fff;">
                                            <td>
                                                <div><c:out value="${item.deptHistNo}" /></div>
                                            </td>
                                            <td>
                                                <div><c:out value="${item.lastUpdtPnttm}" /></div>
                                            </td>
                                            <td>
                                                <div><c:out value="${item.processDeptCode}" /></div>
                                            </td>
                                            <td>
                                                <div><c:out value="${item.userName}" />(<c:out value="${item.loginId}" />)</div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="4">부서코드 변경이력 데이터가 존재하지 않습니다.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            
                        </tbody>
                    </table>
                    <c:if test="${list.size() > 0}">
                        <div class="pagination-wrap">
                            <page:pagination-manager />
                        </div>
                    </c:if>
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


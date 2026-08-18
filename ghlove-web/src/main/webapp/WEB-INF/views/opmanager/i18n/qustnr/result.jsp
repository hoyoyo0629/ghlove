<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop" %>
    
    
  <!-- 개발 영역 -->
<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>설문 결과</span></h3>

<div class="btn_all btn_right mb15">
	<div class="flex_box gap-08">
		<button type="button" class="btn btn-dark-default btn-mini" onclick="location.href='/opmanager/qustnr/list'">목록</button>
	</div>
</div>

<div class="board_write">
	<table class="board_write_table" summary="기부모금액">
		<colgroup>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">설문 참여</td>
				<td>
					<div>총 <c:out value="${op:numberFormat(total)}" />건</span></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<div class="board_write mt-40 question_board">
	<table id="qusten" class="board_write_table" summary="질문 테이블">
		<colgroup>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<c:if test="${qestnar != null && not empty qestnar.qustnrQesitm}">
				<c:forEach items="${qestnar.qustnrQesitm}" var="item" varStatus="state">
					<tr>
						<td class="label">질문</td>
						<td>
							<div class="flex_box gap-08">
								<span><c:out value="${item.qestnCn}" /></span>
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">답변</td>
						<td class="answer">
							<c:set var="answerList" value="${item.qustnrIem}"></c:set>
							<c:forEach items="${answerList}" var="answer" varStatus="i">
								<div class="flex_box gap-08 answer-box">
									<span><c:out value="${answer.iemCn} (${op:numberFormat(answer.userCnt)})" /></span>
								</div>
							</c:forEach>
						</td>
					 </tr>
					 <c:if test="${!state.last}">
						<tr style="height : 20px"></tr>
					 </c:if>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>

<script type="text/javascript">
$(function() {
	$(".contents_inner").find("div.location a").removeClass("on");
	$(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">결과</a>');
	$(".contents_inner").find('h3:first > span').html("설문 결과");

});


</script>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>회원등록 불가능ID</span></h3>
<form:form modelAttribute="config" cssClass="opmanager-search-form clear" action="/opmanager/config/deny/edit" method="post">
	<fieldset>
		<legend class="hidden">회원등록 불가능ID</legend>
		<form:hidden path="shopConfigId" />
		<div class="board_write">
			<table class="board_write_table">
				<colgroup>
					<col style="width: 150px;" />
				</colgroup>
				<tbody>
				<tr>
					<td class="label"><c:out value="${op:message('M00011')}"/></td>
					<td >
						<div>
							<select>
								<option><c:out value="${op:message('M00081')}"/></option>
							</select>
							<input type="text" id="deniedId" name="deniedId" class="optional seven _filter" title="${op:message('M00211')}" value="${fn:escapeXml(config.deniedId)}" />
						</div>
					</td>
				</tr>
				</tbody>
			</table>
		</div> <!--// board_write E-->

		<div class="btn_all">
			<div class="btn_left">
				<button type="button" class="btn btn-dark-gray btn-sm" onclick="location.href='/opmanager/config/deny/edit'"><span class="glyphicon glyphicon-repeat"></span><c:out value="${op:message('M00047')}"/></button> <!-- 초기화 -->
			</div>
			<div class="btn_right">
				<button type="submit" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span><c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
			</div>
		</div>	<!--// btn_all E-->
	</fieldset>

	<div class="count_title mt20">
		<h5>
			전체 : <c:out value="${op:numberFormat(pagination.totalItems)}"/>건 조회
		</h5>
	</div>
</form:form>

<div class="board_write">
	<form id="listForm">
		<table class="board_list_table">
			<colgroup>
				<col style="width:60px;" />
				<col style="width:auto;" />
				<col style="width:70px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="col"><c:out value="${op:message('M00200')}"/></th>
					<th scope="col"><c:out value="${op:message('M00081')}"/></th> <!-- 아이디 -->
					<th scope="col"><c:out value="${op:message('M00074')}"/></th> <!-- 수정 --> <!-- 삭제 -->
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="userId" varStatus="i">
					<tr>
						<td><c:out value="${pagination.itemNumber - i.count}"/></td>
						<td><c:out value="${userId}"/></td>
						<td>
							<button type="button" class="btn btn-gradient btn-xs" onclick="deleteDeniedId('${fn:escapeXml(userId)}');"><c:out value="${op:message('M00074')}"/></button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>

	<c:if test="${empty list}">
		<div class="no_content">
			데이터가 없습니다.
		</div>
	</c:if>

</div><!--//board_write E-->

<div class="btn_all">
	<div class="btn_right mb0">
		<a href="/opmanager/config/deny/form" class="btn btn-active btn-sm"><span class="glyphicon glyphicon-plus"></span> 등록</a>
	</div>
</div>

<page:pagination-manager />

<script type="text/javascript">

function deleteDeniedId(id) {
	if (confirm("삭제하시겠습니까?")) {
		var params = {
			deniedIds : id,
			deniedKey : "1"
		};

		$.post('/opmanager/config/deny/edit', params, function(response) {
			Common.responseHandler(response, function(response) {
				alert(response.data);
				location.reload();
			});
		});
	}
}
$(function() {
	$("#deny").click(function(){
		$("#addId").val($("#deny option:selected").val());
	});

	$("#delbtn").click(function(){
		if($("#addId").val() != ""){
			$("#deny option:selected").remove();
			$("#addId").val("");
		}
	});

	$("#modifybtn").click(function(){
		if($("#addId").val() != ""){
			var value = $("#addId").val();
			$("#deny option:selected").text(value);
			$("#deny option:selected").val(value);
		}
	});

	$("#addbtn").click(function(){
		if($("#addId").val() != ""){
			var size = $("#deny option").size();
			var value = $("#addId").val();
			$("#deny").append("<option value='" + value + "'>" + value + "</option>");
			$("#deny option:eq(" + size + ")").attr("selected", "selected");
		}
	});
});



</script>

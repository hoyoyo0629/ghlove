<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>


<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3>
	<span>FAQ</span>
</h3>

<form:form modelAttribute="searchParam" enctype="multipart/form-data" method="post">
	<form:hidden path="page" />
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage" />

	<div class="board_write">
		<table class="board_write_table">
			<caption>${op:message('M00675')}</caption>
			<colgroup>
				<col style="width: 150px;" />
				<col />
			</colgroup>
			<tbody>
				<tr>
					<td class="label">${op:message('M00275')}</td>
					<td>
						<div class="flex_box gap-08">
							<form:hidden path="where" value="SUBJECT" />
							<form:input path="query"
								cssClass="input_txt required _filter half"
								title="${op:message('M00021')}" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">질문 유형</td>
					<td>
						<div class="row">
							<div class="col-xs-2 pr-0">
								<form:select path="faqType" class="form-block" title="질문 유형">
									<form:option value="">전체 </form:option>
									<c:forEach var="faqType" items="${faqTypes}">
										<form:option value="${fn:escapeXml(faqType.code)}">${fn:escapeXml(faqType.title)}</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- // board_write -->

	<!-- 버튼시작 -->
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-sm"
				onclick="location.href='/opmanager/community/locv-faq/list';">
				<span class="glyphicon glyphicon-repeat"></span>
				${op:message('M00047')}
			</button>
			<!-- 초기화 -->
			<button type="submit" class="btn btn-dark-gray btn-sm">
				<span class="glyphicon glyphicon-search"></span>
				${op:message('M00048')}
			</button>
			<!-- 검색 -->
		</div>
	</div>
	<!-- 버튼 끝-->


	<div class="count_title mt20">
		<h5>전체 : ${op:numberFormat(pagination.totalItems)}건</h5>
		<span> <select name="displayCount" id="displayCount"
			title="${op:message('M00239')}">
				<option value="10"><c:out value="${op:message('M00240')}" /></option>
				<option value="20"><c:out value="${op:message('M00241')}" /></option>
				<option value="50"><c:out value="${op:message('M00242')}" /></option>
				<option value="100"><c:out value="${op:message('M00243')}" /></option>
		</select>
		</span>
	</div>
</form:form>

<form id="listForm">
	<div class="board_write">
		<table class="board_list_table" summary="${op:message('M00273')}">
			<!-- 주문내역 리스트 -->
			<caption>${op:message('M00273')}</caption>
			<colgroup>
				<col style="width: 30px;" />
				<col style="width: 60px;" />
				<col style="width: 130px;" />
				<col style="width: 700px;" />
				<col style="width: 100px;" />
				<col style="width: 100px;" />
			</colgroup>
			<thead>
				<tr>
					<th scope="col"><input type="checkbox" id="check_all"
						title="${op:message('M00169')}" /></th>
					<!-- 체크박스 -->
					<th scope="col">순번</th>
					<th scope="col">질문유형</th>
					<th scope="col">제목</th>
					<th scope="col">작성일</th>
					<th scope="col">조회수</th>
					<!-- <th scope="col">관리</th> -->
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="community" varStatus="i">
					<tr>
						<td><input type="checkbox" name="id"
							id="check_${fn:escapeXml(community.id)}" value="${fn:escapeXml(community.id)}"
							title="${op:message('M00169')}" /></td>
						<td><c:out value="${pagination.itemNumber - i.count}" /></td>
						<td>${fn:escapeXml(community.faqType.title)}</td>
						<td class="text"><a
							href="javascript:communityDetail('${fn:escapeXml(community.id)}')"
							style="cursor: pointer;">${fn:escapeXml(community.subject)}</a></td>
						<c:set var="formattedDate">
							<fmt:formatDate value="${fn:escapeXml(community.createdDate)}"
								pattern="yyyy-MM-dd" />
						</c:set>
						<td><c:out value="${formattedDate}" /></td>
						<td><c:out value="${community.hits}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<c:if test="${empty list}">
		<div class="no_content">
			${op:message('M00473')}
			<!-- 데이터가 없습니다. -->
		</div>
	</c:if>
	<c:if test="${role eq 'mois'}">
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<a href="<c:url value="/opmanager/community/locv-faq/create" />"
					class="btn btn-dark-gray btn-mini">${op:message('M00088')}</a>
				<!-- 등록 -->
				<a id="op-delete-list-data" href="javascript:deleteCheckDataboard()"
					class="btn btn-dark-gray btn-mini"><span>${op:message('M00074')}</span></a>
			</div>
		</div>
	</c:if>
	<div class="pagination-wrap">
		<p class="pagination op-pagination">
			<page:pagination-manager />
		</p>
	</div>


</form>



<script type="text/javascript">
	$(function() {

		// 데이터 출력 수 설정.
		$('#size').on("change", function() {
			$('#page').val("1");
			$('#faqDto').submit();
		});

	});

	function deleteCheckDataboard() {
		var databoardList = new Array;

		$("input[id^='check_']:checked").not("[id='check_all']").map(
				function(index, item) {
					databoardList.push($(item).val());
				});

		if (databoardList.length == 0) {
			alert("삭제할 게시물을 선택해주세요.");
			return false;
		}

		if (confirm("삭제하시겠습니까?")) {
			$.post('/opmanager/community/locv-faq/deleteDataboard', {
				"databoardList" : databoardList
			}, function(response) {
				if (response.isSuccess && response.data) {
					if (response.data == "SUCC") {
						alert("삭제되었습니다.");
						location.reload();
					} else {
						alert("오류가 발생했습니다.");
					}
				}
			});
		}
	}

	function checkedEventSet() {
		var childObjs = $("input[id^='check_']").not("[id='check_all']");

		// 체크박스 '전체' 체크&해지
		$("#check_all").click(function() {
			$(childObjs).prop("checked", $(this).is(":checked"));
		});

		// '개별' 체크박스에 따른 '전체' 체크박스 체크&해지
		$(childObjs).click(
				function() {
					if ($(childObjs).length == $("input[id^='check_']:checked")
							.not("[id='check_all']").length) {
						$("#check_all").prop("checked", true);
					} else {
						$("#check_all").prop("checked", false);
					}
				});
	}

	function communityDetail(id) {

		location.href = '/opmanager/community/locv-faq/list/' + id;
	}
</script>
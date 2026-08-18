<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>


	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

<div class="item_list">

	<h3><span>${op:message('M00584')} <!-- 팀별 그룹관리 --> </span></h3>

	<div class="board_list pt15">
		<form:form modelAttribute="searchParam" id="searchForm" method="post" style="display:none;">
			<div class="count_title">
				<h5>
					<form:select path="categoryType">
						<form:option value="">${op:message('M00039')} <!-- 전체 --> </form:option>
						<form:option value="team">대그룹</form:option>
					</form:select>
				</h5>
				<%-- <button class="table_btn" id="ordering" style="float: right;" title="순서 정렬">${op:message('M00587')} <!-- 순서 정렬 --> </button>  --%>
			</div>
		</form:form>
	
		<form id="listForm">
		<table class="board_list_table group" style="width: 100%">
			<colgroup>
				<col style="width: 150px;">
                <col style="width: 500px;">
				<col style="width: 150px;">
				<col style="width: 100px;">
				<col style="width: 150px;">
				<col style="width: 100px;">
				<col style="width: 150px;">
			</colgroup>
			<thead>
				<tr>
					<th>${op:message('M00588')} <!-- 레벨 --> </th>
					<th>${op:message('M00394')} <!-- 타이틀 --> </th>
					<th>${op:message('M01374')} <!-- 코드 --> </th>
					<th>${op:message('M00191')} <!-- 공개유무 --> </th>
					<th>${op:message('M01604')} <!-- 접속권한 --> </th>
					<th>${op:message('M00190')} <!-- 순서 --> </th>
					<th>${op:message('M00590')} <!-- 관리 --> </th>
				</tr>
			</thead>
				<c:if test="${searchParam.categoryType == 'team'}">
					<tbody class="sortable">
					<c:forEach items="${categoriesTeamGroupList}" var="list" varStatus="i">
						<tr class="back_color">
							<td>
								대그룹
								<input type="hidden" name="id" value="${fn:escapeXml(list.categoryTeamId)}"/>
								<input type="hidden" name="ordering" />
							</td>
							<td class="tex_l">${fn:escapeXml(list.name)}</td>
							<td>${fn:escapeXml(list.code)}</td>
							<td>
								<c:if test="${list.categoryTeamFlag == 'Y'}">
									${op:message('M00096')}
								</c:if>
								<c:if test="${list.categoryTeamFlag == 'N'}">
									${op:message('M00097')}
								</c:if>
							</td>
							<td></td>
							<td>${fn:escapeXml(list.ordering)}</td>
							<td>
                                <div class="flex_box juc-center gap-08">
                                    <%-- <a href="javascript:fn_categoriesTeamUpdate('${list.categoryTeamId}');" class="btn btn-default btn-sm" title="수정">${op:message('M00087')}</a> <!-- 수정 --> --%>
                                </div>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</c:if>
				<c:if test="${searchParam.categoryType == 'group'}">
					<c:forEach items="${categoriesTeamGroupList}" var="list" varStatus="i">
                        <tr>
                            <td>1차 <!-- 1차 --> </td>
                            <td class="tex_l">┗ <a href="/opmanager/categories/list?categoryGroupId=${fn:escapeXml(list.categoryGroupId)}">${fn:escapeXml(list.name)}</a></td>
                            <td>${fn:escapeXml(list.code)}</td>
							<td>
								<c:if test="${list.categoryGroupFlag == 'Y'}">
									${op:message('M00096')}
								</c:if>
								<c:if test="${list.categoryGroupFlag == 'N'}">
									${op:message('M00097')}
								</c:if>							
							</td>
							<td>
								<c:if test="${list.accessType == 1}">
									${op:message('M00497')}
								</c:if>
								<c:if test="${list.accessType == 2}">
									${op:message('M01679')}
								</c:if>
							</td>
							<td>${fn:escapeXml(list.ordering)}</td>
							<td>
                                <div class="flex_box juc-center gap-08">
                                    <a href="javascript:fn_categoriesGroupUpdate('${fn:escapeXml(list.categoryGroupId)}');" class="btn btn-default btn-sm" title="수정">${op:message('M00087')}</a> <!-- 수정 -->
                                </div>
							</td>
                        </tr>
                    </c:forEach>
				</c:if>
				<c:if test="${searchParam.categoryType == ''}">
					<c:forEach items="${categoriesTeamGroupList}" var="list" varStatus="i">
						<tr class="back_color">
							<td>대그룹</td>
							<td class="tex_l">${fn:escapeXml(list.name)}</td>
							<td >${fn:escapeXml(list.code)}</td>
							<td>
								<c:if test="${list.categoryTeamFlag == 'Y'}">
									${op:message('M00096')}
								</c:if>
								<c:if test="${list.categoryTeamFlag == 'N'}">
									${op:message('M00097')}
								</c:if>
							</td>
							<td></td>
							<td>${fn:escapeXml(list.ordering)}</td>
							<td>
                                <div class="flex_box juc-center gap-08">
                                   <%-- <a href="javascript:fn_categoriesTeamUpdate('${list.categoryTeamId}');" class="btn btn-default btn-sm" title="수정">${op:message('M00087')}</a> <!-- 수정 --> --%>
                                </div>
							</td>
						</tr>
						<tbody id="${fn:escapeXml(list.code)}_groups" class="sortable">
						<c:forEach items="${list.categoriesGroupList}" var="list2" varStatus="j">
							<c:if test="${list2.groupName != null && list2.groupName != '' }">
								<tr>
									<td>1차 <!-- 1차 -->
										<input type="hidden" name="id" value="${fn:escapeXml(list2.categoryGroupId)}"/>
										<input type="hidden" name="ordering" />
									</td>
									<td class="tex_l">┗ <a href="/opmanager/categories/list?categoryGroupId=${fn:escapeXml(list2.categoryGroupId)}">${fn:escapeXml(list2.groupName)}</a></td>
									<td>${fn:escapeXml(list2.code)}</td>
									<td>
										<c:if test="${list2.categoryGroupFlag == 'Y'}">
											${op:message('M00096')}
										</c:if>
										<c:if test="${list2.categoryGroupFlag == 'N'}">
											${op:message('M00097')}
										</c:if>							
									</td>
									<td>
										<c:if test="${list2.accessType == 1}">
											${op:message('M00497')}
										</c:if>
										<c:if test="${list2.accessType == 2}">
											${op:message('M01679')}
										</c:if>
									</td>
                                    <td>${fn:escapeXml(list2.groupOrdering)}</td>
                                    <td>
                                        <div class="flex_box juc-center gap-08">
                                            <a href="javascript:fn_categoriesGroupUpdate('${fn:escapeXml(list2.categoryGroupId)}');" class="btn btn-default btn-sm" title="수정">${op:message('M00087')}</a> <!-- 수정 -->
                                        </div>
                                    </td>
								</tr>
							</c:if>
						</c:forEach>
						</tbody>
					</c:forEach>
				</c:if>
		</table>

		<c:if test="${empty categoriesTeamGroupList}">
			<div class="no_content">
				<p>${op:message('M00591')} <!-- 등록된 데이터가 없습니다. --> </p>
			</div>
		</c:if>

		<div class="btn_all btn_right">
            <div class="flex_box gap-08">
				<button type="button" onclick="changeOrdering('${fn:escapeXml(searchParam.categoryType)}')" class="btn btn-dark-gray btn-mini">${op:message('M00791')}</button>
				<button type="button" onclick="categoryCreate()" class="btn btn-dark-gray btn-mini">${op:message('M00544')}</button>
			</div>
		</div>
	</div> <!-- // board_list -->

</div> <!-- // item_list01 -->


<style>
td {background: #fff;}
.sortable-placeholder td {
	height: 50px;
	background: #d6eafd url("/content/styles/ui-lightness/images/ui-bg_diagonals-thick_20_666666_40x40.png") 50% 50% repeat;
	opacity: 0.5;
}
.back_color td {background:#ebe3d0}
</style>

<script type="text/javascript">
$(function(){
	$('.sortable').sortable({
        placeholder: "sortable-placeholder"
    });
    $('.sortable').disableSelection();

	categoryChange();
	teamGroupOrdering();

});


// 드레그 후 순서 변경.
function changeOrdering(type) {

	// 시스템/행안부 관리자만 권한있음
	if('${fn:escapeXml(adminRole)}' != 'SYS') {
		alert("변경 권한이 없습니다.");
		return false;
	}
    	
	var $form = $('#listForm');

	$('tbody').each(function() {
		$(this).find('input[name=ordering]').each(function(index) {
			$(this).val(index + 1);
		});
	});

	if (type == '') {
		type = 'group';
	}

	if (confirm("정렬 순서를 변경하시겠습니까?")) {		//  변경하시겠습니까?
		$.post("/opmanager/categories-team-group/change-ordering/" + type, $form.serialize(), function(response) {
			Common.responseHandler(response, function(response){
				location.reload();
			});
		});
	}
}

function categoryCreate() {

	// 시스템/행안부 관리자만 권한있음
	if('${fn:escapeXml(adminRole)}' != 'SYS') {
		alert("등록 권한이 없습니다.");
		return false;
	}
	
	location.href='/opmanager/categories-team-group/group/create';

}


function categoryChange(){
	$("#categoryType").on("change",function(){
		$("#searchForm").submit();
	});
}

function fn_categoriesTeamUpdate(teamId){
	location.href="/opmanager/categories-team-group/team/edit/"+teamId;
}

function fn_categoriesTeamDelete(teamId){
	Common.confirm(Message.get("M00594"), function() {
		location.href="/opmanager/categories-team-group/team/delete/"+teamId;
	});
}

function fn_categoriesGroupUpdate(groupId){

	// 시스템/행안부 관리자만 권한있음
	if('${fn:escapeXml(adminRole)}' != 'SYS') {
		alert("수정 권한이 없습니다.");
		return false;
	}
	
	location.href="/opmanager/categories-team-group/group/edit/"+groupId;
}

function fn_categoriesGroupDelete(groupId){
	Common.confirm(Message.get("M00594"), function() {
		location.href="/opmanager/categories-team-group/group/delete/"+groupId;
	});
}

function teamGroupOrdering(){
	$(".team_ordering, .group_ordering").on("keydown",function(e){

		var obj = $(this);

		if (!(e.which && (e.which > 47 && e.which < 58) || (e.which > 95 && e.which < 106) || e.which ==8 || e.which == 13 || e.which == 37 || e.which == 39 || e.which == 46 || e.which ==9|| e.which ==0 || (e.ctrlKey && e.which ==86) ) ) {
		 	e.preventDefault();
	   	}



	   	/* var pattern = /^[0-9]+/g;
	   	var matchValue = obj.val().match(pattern);

	   	if (!pattern.test(obj.val())) {
			obj.val('');
		}

		if (obj.val() != matchValue) {
			  obj.val(matchValue);
		} */

	});

	var orgTeamOrdering;
	$(".team_ordering").on("focusin", function(){
		orgTeamOrdering = $(this).val()
	}).on("focusout", function() {
		if ($.isNumeric($(this).val()) == false) {
			$(this).val(orgTeamOrdering);
			return false;
		}

		var changeOrdering = $(this).val();
		var isSave = true;
		$.each($('.team_ordering').not(this), function() {
			if ($(this).val() == changeOrdering) {
				isSave = false;
			}
		});

		if (changeOrdering == orgTeamOrdering) {
			return false;
		}

		if (isSave == false) {
			$(this).val(orgTeamOrdering);
		} else {

			var param  = {
				"ordering" : $(this).val(),
				"categoryTeamId" : $(this).attr("rel")
			};

			$.post("/opmanager/categories-team-group/ordering-edit/team", param, function(response) {
				Common.responseHandler(response, function(){
					location.reload();
				});
			});

		}
	});

	var orgGroupOrdering;
	$('.group_ordering').on("focusin", function(){
		orgGroupOrdering = $(this).val();
	}).on("focusout", function(){

		if ($.isNumeric($(this).val()) == false) {
			$(this).val(orgGroupOrdering);
			return false;
		}

		var changeOrdering = $(this).val();
		var isSave = true;
		$.each($('.' + $(this).attr('id')).not(this), function(){
			if ($(this).val() == changeOrdering) {
				isSave = false;
			}
		});

		if (changeOrdering == orgGroupOrdering) {
			return false;
		}

		if (isSave == false) {
			$(this).val(orgGroupOrdering);
		} else {
			var param  = {
					"ordering" : $(this).val(),
					"categoryGroupId" : $(this).attr("rel")
			};

			$.post("/opmanager/categories-team-group/ordering-edit/group", param, function(response) {
				Common.responseHandler(response, function(){
					location.reload();
				});
			});
		}

	})

	/* $("#ordering").on("click",function(){
		location.reload();
	}); */
}

// 순서변경
</script>

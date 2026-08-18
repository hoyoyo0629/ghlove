<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="module"	tagdir="/WEB-INF/tags/modules"%>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>메인 배너관리</span></h3>

<div class="board_list">
	<table class="board_list_table" summary="메인 배너관리">
		<caption>메인 배너관리</caption>
		<colgroup>
			<col style="width:100px;">
			<col style="width:300px;">
			<col style="width:150px;">
			<col style="width:150px;">
			<col style="width:150px;">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">배너명</th>
				<th scope="col">내용</th>
				<th scope="col">사용여부</th>
				<th scope="col">관리</th>
				<th scope="col">순서</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="i">
				<tr style="background:#fff;">
					<td>
						<div><c:out value="${item.title}"/></div>
					</td>
					<td>
						<div><c:out value="${item.contents}"/></div>
					</td>
					<td>
						<div><c:out value="${item.displayFlag eq 'Y' ? '사용' : '중지'}"/></div>
					</td>
					<td>
						<div class="flex_box juc-center gap-08">
							<button type="button" class="btn btn-dark-gray btn-sm" onclick="bannerEdit('${fn:escapeXml(item.bannerId)}')">수정</button>
						</div>
					</td>
					<td>
						<div class="flex_box juc-center">
							<select name="displayOrder" data-id="${fn:escapeXml(item.bannerId)}" title="순서" class="wd-80">
								<c:forEach begin="1" end="${fn:length(list)}" step="1" varStatus="ordSts">
									<option value="${ordSts.count}" <c:if test="${item.displayOrder eq ordSts.count}">selected</c:if>>
										<c:out value="${ordSts.count}"/>
									</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
			</c:forEach>
			<c:forEach begin="${fn:length(list)+1}" end="10" step="1" varStatus="banSts">
				<tr style="background:#fff;">
					<td>
						<div></div>
					</td>
					<td>
						<div></div>
					</td>
					<td>
						<div class="flex_box juc-center gap-08">
							<button type="button" class="btn btn-default btn-sm" onclick="bannerCreate();">등록</button>
						</div>
					</td>
					<td>
						<div></div>
					</td>
					<td>
						<div></div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="btn_all btn_center mt40">
	<div class="flex_box gap-08">
		<button type="button" class="btn btn-dark-gray btn-small" onclick="displayOrderSave();">저장</button>
	</div>
</div>

<script type="text/javascript">
	/**
	 *	함 수 명 : bannerCreate
	 *	기	능  : 배너 등록
	 */
	function bannerCreate() {
		location.href = "/opmanager/user-login-banner/create";
	}

	/**
	 *	함 수 명 : bannerEdit
	 *	기	능  : 배너 수정
	 */
	function bannerEdit(bannerId) {
		 location.href = "/opmanager/user-login-banner/edit/"+bannerId;
	}

	/**
	 *	함 수 명 : displayOrderSave
	 *	기	능  : 배열 순서 저장
	 */
	function displayOrderSave() {
		if(!validator() || !confirm("저장하시겠습니까?")) return false;

		var arrDisplayOrder = [];
		$("select[name='displayOrder']").each(function(idx, item) {
			if($(item).val() && $(item).attr("data-id")) {
				arrDisplayOrder.push($(item).attr("data-id") + "|" + $(item).val());
			}
		});

		$.post("/opmanager/user-login-banner/change-display-order", {"displayOrderList": arrDisplayOrder}, function(response) {
			if(response.isSuccess && response.data) {
				alert("저장되었습니다.");
				location.reload();
			}
		});
	}

	/**
	 *	함 수 명 : validator
	 *	기	능  : 유효성 검사
	 */
	function validator() {
		var arrDisplayOrder = $("select[name='displayOrder']");
		var arrTempDisplayOrder = [];
		var isDup = false;

		if(!arrDisplayOrder && !arrDisplayOrder.length) {
			alert("변경할 메인 배너 정보가 없습니다.");
			return false;
		}

		$(arrDisplayOrder).each(function(idx, item) {
			if(arrTempDisplayOrder.indexOf($(item).val()) > -1) {
				isDup = true;
				return false;
			} else {
				arrTempDisplayOrder.push($(item).val());
			}
		});

		if(isDup) {
			alert("중복된 순서가 있어 저장되지 않았습니다. 확인해 주세요.");
			return false;
		}

		return true;
	}
</script>

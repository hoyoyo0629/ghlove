<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.onlinepowers.framework.util.DateUtils" %>
<%@ page import="saleson.common.Const" %>

<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span><c:out value="${op:message('MENU_1406')}"/></span></h3>

<!-- DataSet : 로그인 사용자의 접근 권한 ('ROLE_ADMIN_6' : 지자체, 'ROLE_ADMIN_8' : 오프라인, 'ALL' : 시스템/행안부 -->
<c:set var="loginUserAuth" value="${searchParam.reqstSeCode}"/>
<!--// DataSet  -->

<!-- Search -->
<form:form modelAttribute="searchParam" cssClass="opmanager-search-form clear" method="get" id="searchForm">
	<form:hidden path="sort" />
	<form:hidden path="orderBy" />
	<form:hidden path="itemsPerPage"/>

	<div class="board_write">
		<table class="board_write_table" summary="">
			<colgroup>
				<col style="width:220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label">검색구분</td>
					<td>
						<div class="flex_box gap-08">
							<form:select path="srchKey" title="구분" class="wd-150">
								<form:option value="LOGIN_ID">아이디</form:option>
								<form:option value="USER_NAME">이름</form:option>
								<form:option value="EMAIL">이메일</form:option>
							</form:select>
							<form:input path="srchValue" title="검색구분" class="input_txt required _filter half" type="text"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">등록일</td>
					<td>
						<div class="search-date">
							<span class="datepicker"><form:input path="srchStartCreated" cssClass="datepicker optional " title="${op:message('M00507')}" /></span>
							<span class="wave">~</span>
							<span class="datepicker"><form:input path="srchEndCreated" cssClass="datepicker optional " title="${op:message('M00509')}" /></span>
							<span class="day_btns">
								<a href="javascript:void(0);" class="btn_date today"><c:out value="${op:message('M00026')}"/></a>
								<a href="javascript:void(0);" class="btn_date week-1"><c:out value="${op:message('M00027')}"/></a>
								<a href="javascript:void(0);" class="btn_date month-1"><c:out value="${op:message('M00029')}"/></a>
								<a href="javascript:void(0);" class="btn_date month-3"><c:out value="${op:message('M00030')}"/></a>
								<a href="javascript:void(0);" class="btn_date year-1"><c:out value="${op:message('M00031')}"/></a>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label">상태</td>
					<td>
						<div class="flex_box gap-08">
							<div class="input-form">
								<form:radiobutton path="srchConfmSttusCode" value="" label="전체" checked="checked" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchConfmSttusCode" value="200" label="대기" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchConfmSttusCode" value="100" label="승인" />
							</div>
							<div class="input-form">
								<form:radiobutton path="srchConfmSttusCode" value="300" label="거절" />
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/welfareCenter/list';">초기화</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search();">검색</button>
			</div>
		</div>
	</div>
</form:form>
<!--// Search -->

<!-- List Header -->
<div class="count_title mt-40">
	<h5>총 <fmt:formatNumber value="${searchParam.pagination.totalItems}" pattern="#,###"/>건</h5>
	<span>
		<select name="displayCount" id="displayCount" title="${op:message('M00239')} ">
			<option value="10"><c:out value="${op:message('M00240')}"/></option>
			<option value="20"><c:out value="${op:message('M00241')}"/></option>
			<option value="50"><c:out value="${op:message('M00242')}"/></option>
			<option value="100"><c:out value="${op:message('M00243')}"/></option>
		</select>
	</span>
</div>
<!--// List Header -->

<!-- List -->
<div class="board_list">
	<table class="board_list_table" summary="관리자 권한 승인관리">
		<caption>관리자 권한 승인관리</caption>
			<colgroup>
				<col style="width:5%;">
			</colgroup>
			<thead>
				<tr>
					<th scope="col">No.</th>
					<c:if test="${loginUserAuth ne 'ROLE_ADMIN_6'}"><th scope="col">지자체</th></c:if>
					<th scope="col">행정복지센터명</th>
					<th scope="col">아이디</th>
					<th scope="col">이름</th>
					<th scope="col">이메일</th>
					<th scope="col">직위</th>
					<th scope="col">상태</th>
					<th scope="col">요청일</th>
					<th scope="col">이력</th>
					<th scope="col">삭제여부</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="i">
							<tr style="background:#fff;">
								<td><c:out value="${searchParam.pagination.totalItems - (searchParam.itemsPerPage * (searchParam.pagination.currentPage - 1)) - i.count + 1}"/></td>
								<td>
									<c:out value="${item.upperlocgovNm}"/>&nbsp;<c:out value="${item.locgovNm}"/>
								</td>
								<td><c:out value="${item.pbadmsWlfrCntrNm}"/></td>
								<td><c:out value="${item.loginId}"/></td>
								<td><c:out value="${item.userName}"/></td>
								<td><c:out value="${item.email}"/></td>
								<td><c:out value="${item.ofcpsNm}"/></td>
								<td>
									<a href="javascript:detailsPopup('${fn:escapeXml(item.userId)}', '${fn:escapeXml(item.reqstSn)}', '${fn:escapeXml(item.locgovCode)}', '${fn:escapeXml(item.active)}')"><c:out value="${item.confmSttusNm}"/></a>
								</td>
								<td><c:out value="${item.frstRegistPnttm}"/></td>
								<td><a href="javascript:histroyPopup('${fn:escapeXml(item.userId)}')">보기</a></td>
								<td>
									<c:choose>
										<c:when test="${item.active == 'Y'}">
											<div class="flex_box juc-center gap-08">
												<a href="javascript:deleteManager('${fn:escapeXml(item.userId)}');" class="btn btn-gradient btn-xs">삭제하기</a>
											</div>
										</c:when>
										<c:otherwise>
											<c:out value="${item.active}"/>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="11">관리자 권한 승인 신청한 정보가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
	</table>
	<c:if test="${not empty list}">
		<div class="pagination-wrap">
			<page:pagination-manager />
		</div>
	</c:if>
</div>

<script type="text/javascript">
	$(function() {

		// 한 페이지 출력 갯수 변경 이벤트
		displayChange();

		// 선택된 페이지 출력 갯수 셋팅
		displaySelected();

		// 날짜 변경 이벤트
		datepickerChange();

		// 검색 enter key
		searchEnterKey();

		Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="srchStartCreated"]' , 'input[name="srchEndCreated"]');
		EventHandler.calendarStartDateAndEndDateVaild();
	});

	/**
	 *	함 수 명 : displayChange
	 *	기	능  : 한 페이지 출력 갯수 변경 이벤트
	 */
	function displayChange() {
		$("#displayCount").on('change', function(){
			$("#itemsPerPage").val($(this).val());
			search();
		});
	}

	/**
	 *	함 수 명 : displaySelected
	 *	기	능  : 선택된 페이지 출력 갯수 셋팅
	 */
	function displaySelected(){
		$("#displayCount").val($("#itemsPerPage").val());
	}

	/**
	 *	함 수 명 : detailsPopup
	 *	기	능  : 관리자 권한 승인관리 상세 조회
	 *	파라미터  : userId - 사용자ID , reqstSn - 신청 일련번호
	 */
	function detailsPopup(userId, reqstSn, lclgvCd, active) {
		var url = '/opmanager/welfareCenter/popup/details/'+userId+'/'+reqstSn+'/'+lclgvCd+'/'+active;
		var popupName = '/opmanager/welfareCenter/popup/details';

		// 상세 팝업 활성화
		Common.popup(url, popupName, 600, 650 ,1, 0, 0);
	}

	/**
	 *	함 수 명 : datepickerChange
	 *	기	능  : 날짜 변경 이벤트
	 */
	function datepickerChange() {
		if($("input.datepicker").length) {
			$("input.datepicker").keyup(function(e) {
				if(e && e.target && e.target.value) {
					var value = e.target.value.replace(/[^0-9]/g, "");
					if(value.length > 8) { value = value.slice(0, 8); }
					e.target.value = value;
				}
			});
		}
	}

	/**
	 *	함 수 명 : search
	 *	기	능  : 검색
	 */
	function search() {
		var strStartDate = $("#srchStartCreated").val();
		var strEndDate = $("#srchEndCreated").val();

		if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
			var startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));
			var endDate = new Date(strEndDate.substr(0, 4), strEndDate.substr(4, 2), strEndDate.substr(6, 2));

			if(startDate > endDate) {
				alert("종료일이 시작일보다 빠릅니다.");
				var value = $("#srchEndCreated").val();
				$("#srchEndCreated").val("");
				$("#srchEndCreated").focus();
				$("#srchEndCreated").val(value);
				return false;
			}
		}

		$("#searchForm").submit();
	}

	/**
	 *	함 수 명 : searchEnterKey
	 *	기	능  : 검색 엔터키 이벤트
	 */
	function searchEnterKey() {
		$("#srchValue, #srchStartCreated, #srchEndCreated").on('keydown', function(e){
			if (e.keyCode == '13') {
				search();
			}
		});
	}

	function deleteManager(userId) {
		if(confirm("해당 사용자의 권한을 삭제하시겠습니까?")) {
			$.post('/opmanager/user/locgov-charger/delete', {"userIdList": [userId]}, function(response) {
				if(response.isSuccess && response.data) {
					if(response.data.code == "SUCC") {
						alert("삭제되었습니다.");
						location.reload();
					} else {
						alert("문제가 발생했습니다.");
					}
				}
			});
		}
	}

	function histroyPopup(userId) {
		var url = '/opmanager/welfareCenter/popup/history/'+userId;
		var popupName = '/opmanager/welfareCenter/popup/history';

		// 상세 팝업 활성화
		Common.popup(url, popupName, 800, 650 ,1, 0, 0);
	}



</script>

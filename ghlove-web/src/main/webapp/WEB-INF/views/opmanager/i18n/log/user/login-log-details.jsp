<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>
<%@ page import="com.onlinepowers.framework.util.DateUtils" %>
<%@ page import="saleson.common.Const" %>

<div class="location">
	<a href="#"></a>&gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>사용자 로그관리</span></h3>

<div class="board_write">
	<table class="board_write_table" summary="사용자 로그관리">
		<colgroup>
			<col style="width:220px;">
		</colgroup>
		<tbody>
			<tr>
				<td class="label">사용자 ID</td>
				<td>
					<div><c:out value="${details.loginId}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label">접속 IP</td>
				<td>
					<div><c:out value="${details.remoteAddr}"/></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<!-- List -->
<div class="board_list mt-40" id="actionLogList"></div>
<!--// List -->

<!-- 목록 검색 조건 -->
<input type="hidden" id="listSearchParam" />
<!--// 목록 검색 조건  -->

<script type="text/javascript">
	$(function() {

		// 페이지 유효성 확인
		if("${fn:escapeXml(details.loginId)}" == "") {
			alert("잘못된 접근입니다.");
			moveListPage();
		}

		// 네비게이션 수정
		$(".contents .contents_inner").find("div.location a").removeClass("on");
		$(".contents .contents_inner").find("div.location").append('> <a href="'+location.pathname+'" class="on">상세</a>');

		// 목록 검색 조건 저장
		listSearchParamSave();

		// 메뉴사용이력 조회
		getActionLogList(1, 20);
	});

	/**
	 *	함 수 명 : getActionLogList
	 *	기	능  : 메뉴사용이력 조회
	 *	파라미터  : page - 현재 페이지, itemPerPage - 목록수 (한 페이지에서 노출한 row)
	 */
	function getActionLogList(page, itemPerPage) {
		var searchValue = "page="+page;
		searchValue += "&srchStartCreated=${fn:escapeXml(details.loginDate)}";
		searchValue += "&srchEndCreated=${fn:escapeXml(details.nextLoginDate)}";

		if(itemPerPage) {
			searchValue += "&itemsPerPage="+itemPerPage;
		} else {
			searchValue += "&itemsPerPage="+10;
		}

		$.get("/opmanager/log/user/login-log/details/${fn:escapeXml(loginLogId)}/action-log-list?"+searchValue, {}, function(result) {
			$("#actionLogList").html(result);
		}, "html");
	}

	/**
	 *	함 수 명 : listSearchParamSave
	 *	기	능  : 목록 검색 조건 저장
	 */
	function listSearchParamSave() {

		// 목록 검색 조건 저장
		$("#listSearchParam").val(location.search);

		// 검색 조건 삭제
		history.replaceState({}, null, location.pathname);
	}

	/**
	 *	함 수 명 : moveListPage
	 *	기	능  : 목록 페이지 이동
	 */
	function moveListPage() {
		location.href = "/opmanager/log/user/login-log"+$("#listSearchParam").val();
	}
</script>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span><c:out value="${op:message('M00210')}"/></span></h3>

<!-- 회원상세정보 -->
<div class="board_write">
	<table class="board_write_table" summary="회원상세정보">
		<colgroup>
			<col style="width:220px;">
			<col>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">회원구분</td>
				<td colspan="3">
					<div>일반회원</div>
				</td>
			</tr>
			<tr>
				<td class="label">이름</td>
				<td>
					<div><c:out value="${details.userName}"/></div>
				</td>
				<td class="label">아이디</td>
				<td>
					<div><c:out value="${details.loginId}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label">휴대폰</td>
				<td>
					<div><c:out value="${details.phoneNumber}"/></div>
				</td>
				<td class="label">이메일 주소</td>
				<td>
					<div><c:out value="${details.email}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label" rowspan="2">주소</td>
				<td colspan="3">
					<div><c:out value="${details.post}"/></div>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<div><c:out value="${details.address}"/> <c:out value="${details.addressDetail}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label">EMAIL 수신동의</td>
				<td>
					<div><c:out value="${details.receiveEmail eq '0' ? '동의' : '비동의'}"/></div>
				</td>
				<td class="label">국민비서·SMS 수신동의</td>
				<td>
					<div><c:out value="${details.receiveSms eq '0' ? '동의' : '비동의'}"/></div>
				</td>
			</tr>
			<tr>
				<td class="label">가입경로</td>
				<td colspan="3">
					<div><c:out value="${details.sbscrbSeNm}"/></div>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="btn_all btn_right">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="deliveryPopup();">배송지 관리</button>
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="passwordConfirmPopup();">개인정보 열람</button>
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="customerSecede();">회원탈퇴</button>
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="moveListPage();">목록</button>
		</div>
	</div>
</div>
<!--// 회원상세정보 -->

<!-- 누적합계 -->
<h3 class="mt50 fs24"><span>누적합계</span></h3>
<div class="board_write mb30">
	<table class="board_write_table" summary="회원상세정보">
		<colgroup>
			<col>
			<col>
			<col>
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">기부금액</td>
				<td>
					<div><fmt:formatNumber value="${fn:escapeXml(cumclativeTotal.totalCntrAmt)}" pattern="#,###.##"/></div>
				</td>
				<td class="label">발생포인트</td>
				<td>
					<div><fmt:formatNumber value="${fn:escapeXml(cumclativeTotal.totalCntrPoint)}" pattern="#,###.##"/></div>
				</td>
				<td class="label">사용포인트</td>
				<td>
					<div><fmt:formatNumber value="${fn:escapeXml(cumclativeTotal.totalCntrUsePoint)}" pattern="#,###.##"/></div>
				</td>
				<td class="label">포인트잔액</td>
				<td>
					<div><fmt:formatNumber value="${fn:escapeXml(cumclativeTotal.totalCntrBlcePoint)}" pattern="#,###.##"/></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!--// 누적합계 -->

<!-- 기부내역 목록 -->
<h3 class="mt-70"><span>기부내역</span></h3>
<div class="board_list mb30" id="cntrList"></div>
<!--// 기부내역 목록  -->

<!-- 포인트 내역 목록 -->
<h3><span>포인트 내역</span></h3>
<div class="board_list" id="pointList"></div>
<!--// 포인트 내역 목록  -->

<!-- 목록 검색 조건 -->
<input type="hidden" id="listSearchParam" />
<!--// 목록 검색 조건  -->

<script type="text/javascript">
	$(function(){

		// 페이지 유효성 확인
		if("${fn:escapeXml(details.userId)}" == "") {
			alert("잘못된 접근입니다.");
			moveListPage();
		}

		// 네비게이션 수정
		$(".contents .contents_inner").find("div.location a").removeClass("on");
		$(".contents .contents_inner").find("div.location").append('> <a href="'+location.pathname+'" class="on">수정</a>');

		// 목록 검색 조건 저장
		listSearchParamSave();

		// 기부내역 목록 조회
		getCntrList(1);

		// 포인트 내역 목록 조회
		getPointList(1);
	});

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
		location.href = "/opmanager/user/customer/list"+$("#listSearchParam").val();
	}

	/**
	 *	함 수 명 : passwordConfirmPopup
	 *	기	능  : 개인정보 열람 > 비밀번호 확인 팝업 활성화
	 */
	function passwordConfirmPopup() {
		window.open("/opmanager/user/customer/popup/password/${fn:escapeXml(details.userId)}", "customerAccessPopup", "width=450, height=250");
	}

	/**
	 *	함 수 명 : getCntrList
	 *	기	능  : 기부내역 목록 조회
	 *	파라미터  : page - 현재 페이지
	 */
	function getCntrList(page) {
		$.get("/opmanager/user/customer/details/${fn:escapeXml(details.userId)}/cntr-list?page="+page, {}, function(result) {
			$("#cntrList").html(result);
		}, "html");
	}

	/**
	 *	함 수 명 : getPointList
	 *	기	능  : 포인트 내역 목록 조회
	 *	파라미터  : page - 현재 페이지
	 */
	function getPointList(page) {
		$.get("/opmanager/user/customer/details/${fn:escapeXml(details.userId)}/point-list?page="+page, {}, function(result) {
			$("#pointList").html(result);
		}, "html");
	}

	/**
	 *	함 수 명 : customerSecede
	 *	기	능  : 회원탈퇴 팝업 활성화
	 */
	function customerSecede() {
		if("${fn:escapeXml(details.userKey)}" != "") {
			alert("디지털원패스 회원은 탈퇴 처리 불가능합니다.");
			return false;
		}

		var url = "/opmanager/user/customer/popup/secede/${fn:escapeXml(details.userId)}";
		var popupName = "/opmanager/user/customer/popup/secede";
		Common.popup(url, popupName, 600, 330, 1, 0, 0);
	}

	/**
	 *	함 수 명 : customerSecedeCallBack
	 *	기	능  : 회원탈퇴 콜백 함수
	 *	파라미터  :
	 */
	function customerSecedeCallBack(isLogout) {
		if(isLogout && isLogout == "Y") {
			location.href = "/op_security_logout?target=/opmanager";
		} else {
			$("#listSearchParam").val("");
			moveListPage();
		}
	}

	/**
	 *	함 수 명 : deliveryPopup
	 *	기	능  : 배송지 관리 팝업
	 *	파라미터  :
	 */
	function deliveryPopup() {
		var url = "/opmanager/user/customer/delivery/${fn:escapeXml(details.userId)}";
		Common.popup(url, '배송지관리', 800, 480, 1, 0, 0);
	}
</script>
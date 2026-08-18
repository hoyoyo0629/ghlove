<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"		uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>메인 배너관리</span></h3>

<!-- 수정버튼영역 -->
<c:if test="${pageValidFlag eq 'Y'}">
	<div class="btn_all btn_right mb15">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-mini" onclick="bannerEdit();">수정</button>
			<button type="button" class="btn btn-default btn-mini" onclick="moveListPage();">목록</button>
		</div>
	</div>
</c:if>
<!--// 수정버튼영역 -->

<!-- 상세정보 -->
<div class="board_write">
	<table class="board_write_table mt30" summary="">
		<colgroup>
			<col style="width:220px;">
			<col style="width:180px;">
		</colgroup>
		<tbody>
			<tr>
				<td class="label"><span class="required_mark">*</span>배너명</td>
				<td colspan="3">
					<div class="flex_box gap-08">
						<input id="title" title="배너명" class="input_txt required _filter half" type="text" value="${fn:escapeXml(details.title)}">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"">내용<br><span id="contentsCount">(0/1500)</span></td>
				<td colspan="3">
					<div class="flex_box gap-08">
						<input id="contents" title="내용" class="input_txt required _filter half" type="text" maxlength="1500" value="${details.contents}">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label" rowspan="2"><span class="required_mark">*</span>이미지</td>
				<td class="line-right tcenter">
					<div>
						<p>PC</p>
						<p class="mt10 point">권장사이즈는<br>1840px X 760px 입니다.</p>
					</div>
				</td>
				<td colspan="2">
					<div class="flex_box item-center">
						<input type="file" id="pcFile" class="full input_file" title="이미지" accept="image/png, image/jpeg, image/gif, image/jpg"/>
					</div>
					<c:if test="${not empty details.pcFileName}">
						<div class="file_camera" id="pcFile_${fn:escapeXml(details.bannerId)}">
							<div class="image-box">
								<img src="/opmanager/user-login-banner/mainBanner/pc/${fn:escapeXml(details.bannerId)}" class="item_image size-100 none" alt="배너 이미지(Pc)">
								<a href="javascript:deleteFile('${fn:escapeXml(details.bannerId)}', 'PC')"><img src="/content/images/btn/file_close.gif" alt="close"></a>
							</div>
						</div>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="line-right tcenter">
					<div>
						<p>mobile</p>
						<p class="mt10 point">권장사이즈는<br>1074px X 780px 입니다.</p>
					</div>
				</td>
				<td colspan="2">
					<div class="flex_box item-center">
						<input type="file" id="mFile" class="full input_file" title="이미지" accept="image/png, image/jpeg, image/gif, image/jpg"/>
					</div>
					<c:if test="${not empty details.mFileName}">
						<div class="file_camera" id="mFile_${fn:escapeXml(details.bannerId)}">
							<div class="image-box">
								<img src="/opmanager/user-login-banner/mainBanner/m/${fn:escapeXml(details.bannerId)}" class="item_image size-100 none" alt="배너 이미지(Mobile)">
								<a href="javascript:deleteFile('${fn:escapeXml(details.bannerId)}', 'M')"><img src="/content/images/btn/file_close.gif" alt="close"></a>
							</div>
						</div>
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="label">이미지 링크</td>
				<td colspan="3">
					<div class="flex_box gap-08">
						<input id="linkUrl" title="이미지링크" class="input_txt required _filter half" type="text" value="${fn:escapeXml(details.linkUrl)}">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>사용여부</td>
				<td colspan="3">
					<div class="flex_box gap-12">
						<div class="input-form">
							<input id="displayFlagY" name="displayFalg" type="radio" value="Y" <c:if test="${details.displayFlag eq 'Y'}">checked</c:if>>
							<label for="displayFlagY">사용</label>
						</div>
						<div class="input-form">
							<input id="displayFlagN" name="displayFalg" type="radio" value="N" <c:if test="${details.displayFlag eq 'N'}">checked</c:if>>
							<label for="displayFlagN">중지</label>
						</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!--// 상세정보 -->

<!-- 등록버튼영역 -->
<c:if test="${pageValidFlag ne 'Y'}">
	<div class="btn_all btn_center mt30">
		<div class="flex_box gap-08">
			<button type="button" class="btn btn-dark-gray btn-small" onclick="bannerCreate();">등록</button>
			<button type="button" class="btn btn-default btn-small" onclick="moveListPage();">취소</button>
		</div>
	</div>
</c:if>
<!--// 등록버튼영역 -->

<script type="text/javascript">
	$(function() {
		// 페이지 유효성 체크
		if("${fn:escapeXml(pageValidFlag)}" == "N") {
			alert("잘못된 접근 입니다.");
			moveListPage();
		}

		// 네비게이션 수정
		var navText = ("${fn:escapeXml(pageValidFlag)}" == "Y") ? "수정" : "등록";
		$(".contents .contents_inner").find("div.location a").removeClass("on");
		$(".contents .contents_inner").find("div.location").append('> <a href="'+location.pathname+'" class="on">'+navText+'</a>');

		// 파일 선택 이벤트
		fileSelect();

		// 글자 수 출력 이벤트
		updateCount();
	});

	/**
	 *	함 수 명 : updateCount
	 *	기	능  : 글자 수 출력 이벤트
	 */
	 function updateCount() {
		const $contents 		= $("#contents");
		const $contentsCount 	= $("#contentsCount");
		const max 				= Number($contents.attr('maxlength')) || 1500;

		function render() {
			const len			= $contents.val().length;
			$contentsCount.text("(" + len + "/" + max + ")");
		}

		// render 초기화
		render();

		$contents.off("input.updateCount").on("input.updateCount", render);
	 }

	/**
	 *	함 수 명 : fileSelect
	 *	기	능  : 파일 선택 이벤트
	 */
	function fileSelect() {
		$("#pcFile, #mFile").change(function(e) {
			if(e.target.files[0]) {
				var size = 20;
				var extensions = ['jpg', 'jpeg','gif','png'];
				var file = e.target.files[0];
				var fileExt = file.name;

				if (fileExt != "") {
					if (file.size > (size * 1024 * 1024)) {
						alert("파일크기는 " + size + "MB 이내로 등록 가능합니다.");
						e.target.value = "";
						return;
					}
				}

				fileExt = fileExt.slice(fileExt.lastIndexOf(".") + 1).toLowerCase();
				if (!extensions.includes(fileExt)) {
					alert("이미지파일만 등록 가능합니다.");
					e.target.value = "";
					return;
				}
			}
		});
	}

	/**
	 *	함 수 명 : validator
	 *	기	능  : 유효성검사
	 */
	function validator() {
		if($.trim($("#title").val()) == "") {
			alert("배너명을 입력해 주세요.");
			$("#title").focus();
			return false;
		}

		if(!($.trim($("#pcFile").val()) != "" || $("#pcFile")[0].files[0] || $("#pcFile_${fn:escapeXml(details.bannerId)}").length)) {
			alert("PC 이미지를 선택해 주세요.");
			$("#pcFile").focus();
			return false;
		}

		if(!($.trim($("#mFile").val()) != "" || $("#mFile")[0].files[0] || $("#mFile_${fn:escapeXml(details.bannerId)}").length)) {
			alert("mobile 이미지를 선택해 주세요.");
			$("#mFile").focus();
			return false;
		}

		if($("input[name='displayFalg']:checked").length == 0) {
			alert("사용여부를 선택해 주세요.");
			$("#displayFalgY").focus();
			return false;
		}

		return true;
	}

	/**
	 *	함 수 명 : moveListPage
	 *	기	능  : 목록 이동
	 */
	function moveListPage() {
		location.href = "/opmanager/user-login-banner/index";
	}


	/* *******************************************************************************************************
	 *
	 *											등록 function
	 *
	 * *******************************************************************************************************/

	/**
	 *	함 수 명 : bannerCreate
	 *	기	능  : 배너 등록
	 */
	function bannerCreate() {
		if(!validator() || !confirm("정보를 등록 하시겠습니까?")) return false;

		var formData = new FormData();
		formData.append("title"			, $("#title").val());
		formData.append("contents"		, $("#contents").val());
		formData.append("pcFile"		, $("#pcFile")[0].files[0]);
		formData.append("mFile"			, $("#mFile")[0].files[0]);
		formData.append("linkUrl"		, $("#linkUrl").val());
		formData.append("displayFlag"	, $("input[name='displayFalg']:checked").val());

		$.ajax({
			url : "/opmanager/user-login-banner/create",
			type : "POST",
			async : false,
			data : formData,
			processData: false,
			contentType: false,
			cache: false,
			success : function (response) {
				alert("등록되었습니다.");
				moveListPage();
			},
			error : function (response) {
				alert("오류가 발생했습니다.");
			}
		});
	}


	/* *******************************************************************************************************
	 *
	 *											수정 function
	 *
	 * *******************************************************************************************************/

	/**
	 *	함 수 명 : deleteFile
	 *	기	능  : 파일 삭제 (영역만 삭제)
	 *	파라미터  : bannerId - 배너 ID, fileType - M/PC (모바일/PC)
	 */
	function deleteFile(bannerId, fileType) {
		if(fileType == "PC")  $("div#pcFile_"+bannerId).remove();
		else $("div#mFile_"+bannerId).remove();
	}

	/**
	 *	함 수 명 : bannerEdit
	 *	기	능  : 메인 배너 수정
	 */
	function bannerEdit() {
		if(!validator() || !confirm("정보를 수정 하시겠습니까?")) return false;

		var formData = new FormData();
		formData.append("bannerId"		, "${fn:escapeXml(details.bannerId)}");
		formData.append("title"			, $("#title").val());
		formData.append("contents"		, $("#contents").val());
		formData.append("linkUrl"		, $("#linkUrl").val());
		formData.append("displayFlag"	, $("input[name='displayFalg']:checked").val());

		if($("#pcFile")[0].files[0]) {
			formData.append("pcFile", $("#pcFile")[0].files[0]);
		}

		if($("#mFile")[0].files[0]) {
			formData.append("mFile", $("#mFile")[0].files[0]);
		}

		$.ajax({
			url : "/opmanager/user-login-banner/edit",
			type : "POST",
			async : false,
			data : formData,
			processData: false,
			contentType: false,
			cache: false,
			success : function (response) {
				alert("수정되었습니다.");
				moveListPage();
			},
			error : function (response) {
				alert("오류가 발생했습니다.");
			}
		});
	}
</script>
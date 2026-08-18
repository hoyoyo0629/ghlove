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


<div class="popup_wrap">
    <div id="pop_header">
        <h1 class="popup_title">메뉴관리</h1>
		<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="닫기"></a>
	</div>
    <div class="popup_contents">

		<form:form modelAttribute="menu" method="post" enctype="multipart/form-data">
			<form:hidden path="menuType" value="1" />
			<form:hidden path="menuParentId" value="0" />

			<div class="board_write">

				<table class="board_write_table">
					<caption>메뉴관리</caption> <!-- 메뉴관리 -->
					<colgroup>
						<col style="width:220px;">
						<col style="width:auto;">
					</colgroup>
					<tbody>
						<tr>
							<td class="label"><span class="required_mark">*</span>메뉴ID</td>
							<td>
								<div>
									<form:input type="text" path="menuId" title="메뉴ID" class="nine" readonly="true" maxlength="19"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>카테고리</td>
							<td>
								<div class="flex_box gap-08 item-center">
									<form:select path="menuGubun" title="${op:message('M00054')}" disabled="true"> <!-- 화면 선택 -->
										<form:option value="OPMANAGER">관리자</form:option> <!-- 관리자 -->
										<form:option value="SELLER">답례품관리자</form:option> <!-- 답례품관리자 -->
									</form:select>
									<select id="firstMenu" onchange="getSecondMenuList()">
										<option value="">1차</option>
										<c:forEach items="${firstMenuList}" var="menu" varStatus="status">
											<option value="${fn:escapeXml(menu.menuId)}"><c:out value="${fn:escapeXml(menu.menuName)}"/></option>
										</c:forEach>
									</select>
									<select id="secondMenu" onchange="getThirdMenuId()">
										<option value="">2차</option>
									</select>
									<span class="wave" id="menuPath">${op:nl2br(param.menuPath)}</span>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>메뉴명</td>
							<td>
								<div>
									<form:input type="text" path="menuName" title="메뉴명" class="nine" maxlength="50"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">메뉴코드</td>
							<td>
								<div>
									<form:input type="text" path="menuCode" title="메뉴코드" class="nine" maxlength="100"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">페이지 URL</td>
							<td>
								<div>
									<form:input type="text" path="menuUrl" title="페이지 URL" class="nine" maxlength="100"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label"><span class="required_mark">*</span>메뉴순서</td>
							<td>
								<div>
									<form:input type="text" path="menuSeq" title="메뉴순서" class="nine _number" maxlength="19"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">노출여부</td>
							<td>
								<div class="admin_wrap flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="displayFlag" value="Y" label="Y" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="displayFlag" value="N" label="N" />
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td class="label">메뉴상태</td>
							<td>
								<div class="admin_wrap flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="statusCode" value="1" label="사용" checked="true"/>
									</div>
									<div class="input-form">
										<form:radiobutton path="statusCode" value="2" label="사용안함" />
									</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
       		 </div> <!--// board_write E-->

			<!-- 버튼시작 -->
			<div class="popup_btns">
				<button type="submit" class="btn btn-active"><span><c:out value="${op:message('M00101')}"/></span></button> <!-- 저장 -->
				<a href="JavaScript:self.close();" class="btn btn-default"><span><c:out value="${op:message('M00569')}"/></span></a> <!-- 닫기 -->
			</div>
			<!-- 버튼 끝-->
		</form:form>
    </div>
</div>


<script type="text/javascript">
	var mode = location.href.includes("edit");	// 편집상태 여부

	$(document).ready(function(){
	  	$("#menuGubun").val(opener.$('select[name=where]').val());	// 메뉴구분 자동설정
	  	if(mode) {
	  		$("#firstMenu").hide();
	  		$("#secondMenu").hide();
	  	}
	});

	$(function() {

		// validator
		try{
			$('#menu').validator(function() {

				var menuId = $("#menuId").val();
				var menuType = $("#menuType").val();
				var firstMenu = $("#firstMenu option:selected").text();

				if(menuId % 1000 == 0 && menuType == "2") {
					alert(firstMenu+"에 생성가능한 2레벨 허용범위를 초과하였습니다.");
					return false;
				}

				var menuName = $("#menuName").val();
				var menuSeq = $("#menuSeq").val();
				var menuCode = $("#menuCode").val();
				var menuUrl = $("#menuUrl").val();

				if (menuName.length==0) {
					alert("메뉴명은 필수 입력항목입니다.");
					$("#menuName").focus();
					return false;
				}

				if (menuType == "3" && menuCode.length==0) {
					alert("3레벨일 경우 메뉴코드는 필수 입력항목입니다.");
					$("#menuCode").focus();
					return false;
				}

				if (menuType == "3" && menuUrl.length==0) {
					alert("3레벨일 경우 페이지 URL은 필수 입력항목입니다.");
					$("#menuUrl").focus();
					return false;
				}

				if (menuSeq.length==0) {
					alert("메뉴순서는 필수 입력항목입니다.");
					$("#menuSeq").focus();
					return false;
				}

				if (!mode && !confirm('입력하신 정보로 메뉴를 생성 하시겠습니까?')) {
					return false;
				} else if (mode && !confirm('정보를 저장하시겠습니까?')) {
					return false;
				}

			});
		} catch(e) {
			alert(e.message);
		}

	});

	/**
	 *	함 수 명 : getSecondMenuList
	 *	기	능  : 2레벨 메뉴 조회
	 */
	function getSecondMenuList() {

		var firstMenu = $("#firstMenu").val();
		$("#secondMenu").html('<option value="">2차</option>');

		if(firstMenu != '') {
			$("#menuType").val('2');
		} else {
			$("#menuType").val('1');
		}

		if(firstMenu) {
			$.post('/opmanager/menu/secondMenuList', {"menuParentId": firstMenu, "menuGubun": opener.$('select[name=where]').val()}, function(response) {
				if(response.isSuccess && response.data) {
					$.each(response.data, function(index, item) {
						$("#secondMenu").append('<option value="'+item.menuId+'">'+item.menuName+'</option>');
					});
				}
			});
		}

		$.post('/opmanager/menu/menuId', {"menuParentId": firstMenu, "menuGubun": opener.$('select[name=where]').val(), "menuType": $("#menuType").val()}, function(response) {
			if(response.isSuccess && response.data) {
				$("#menuId").val(response.data);
				$("#menuParentId").val(firstMenu == "" ? 0 : firstMenu);
			}
		});

	}

	/**
	 *	함 수 명 : getThirdMenuId
	 *	기	능 : 메뉴ID 채번
	 */
	function getThirdMenuId() {

		var firstMenu = $("#firstMenu").val();
		var secondMenu = $("#secondMenu").val();

		if(secondMenu != '') {
			$("#menuType").val('3');
		} else {
			$("#menuType").val('2');
		}

		var menuParentId = ($("#secondMenu").val() == '') ? firstMenu : secondMenu;
		$("#menuParentId").val(menuParentId);

		$.post('/opmanager/menu/menuId', {"menuParentId": menuParentId, "menuGubun": opener.$('select[name=where]').val(), "menuType": $("#menuType").val()}, function(response) {
			if(response.isSuccess && response.data) {
				$("#menuId").val(response.data);
			}
		});

	}

</script>
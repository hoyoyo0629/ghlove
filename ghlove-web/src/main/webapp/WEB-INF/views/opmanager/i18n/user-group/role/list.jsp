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

<style>
  #blog-landing {
  margin-top: 10px;
  position: relative;
  max-width: 100%;
  width: 100%;
  }
  #blog-landing > div {
    position: absolute;
  }
  .section {
    border: 1px solid #ccc;

  }
  .section li {
    padding-bottom: 15px;
  }
  .menu1 {
    font-weight: bold;
    font-size: 14px;
    padding: 10px;
    background: #f4f4f4;
    color: #000;
  }
  .menu2 {
    display: block;
    font-weight: bold;
    font-size: 13px;
    padding: 10px 10px 5px 10px;

    color: #000;
    border-top: 1px solid #eee;

  }
  .menu3 {
    display: block;
    font-size: 12px;
    padding: 2px 10px 2px 15px;
    color: #555;
  }
</style>

	<!-- 네비게이션 -->
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

  <!-- 상단 타이틀 -->
	<h3><span>${op:message('MENU_1404')}</span></h3> <!-- 사용자 권한 관리 -->

  <form:form modelAttribute="userGroupParam" method="get" enctype="multipart/form-data">
  <input type="hidden" name="userAuthority" id="userAuthority" value="${fn:escapeXml(authority)}" />
  </form:form>
  <form:form modelAttribute="role" method="post" enctype="multipart/form-data">
  <input type="hidden" name="authority" id="authority" value="${fn:escapeXml(authority)}" />
  <input type="hidden" name="roleName" id="roleName" value="${fn:escapeXml(roleName)}" />
  <input type="hidden" name="roleDesc" id="roleDesc" value="${fn:escapeXml(roleDesc)}" />
  <div class="flex_box gap-20">
    <div class="board_list">
      <table class="board_list_table" summary="사용자 권한관리">
        <caption>${op:message('MENU_1404')}</caption>
        <colgroup>
          <col style="width:50px;">
          <col style="width:300px;">
        </colgroup>
        <thead>
          <tr>
            <th scope="col">No</th>
            <th scope="col">${op:message('M00586')}</th>
          </tr>
        </thead>
        <tbody id="targetTb">

        	<!-- 수정부분입니다. -->
        <c:set var="rowNum" value="1" />
          <c:forEach items="${list}" var="role" varStatus="i">
          	<c:if test="${role.groupName != '지정기부사업자'}">
				<tr class="dyTr" onclick="fnGroupSearch($(this))">
              		<input type="hidden" value="${fn:escapeXml(role.authority)}" />
							<td>${fn:escapeXml(rowNum)}</td>
							<td>${fn:escapeXml(role.groupName)}</td>
				</tr>
				<c:set var="rowNum" value="${rowNum + 1 }" />
			</c:if>
		  </c:forEach>
			<!-- 수정부분입니다. -->

        </tbody>
      </table>
      <div class="btn_all btn_center">
        <div class="flex_box gap-08 juc-center">
          <button type="submit" class="btn btn-dark-gray btn-mini">${op:message('M00101')} <!-- 저장 --></button>
        </div>
      </div>
    </div>


    <div class="board_write half mt10">
      <table class="board_write_table" summary="">
        <colgroup>
          <col style="width:150px;">
          <col style="">
        </colgroup>
        <tbody>
          <tr>
            <td class="label" valign="top" style="padding-top: 15px; font-size: 13px">메뉴권한</td>
            <td>
              <div style="padding: 15px 10 15px 15px">
                <p><label><input type="checkbox" class="check-all" /> 전체선택</label></p>

                <div id="blog-landing" class="input_wrap col-w-7" style="height: 100vh;">

                  <c:forEach items="${menuList}" var="menu1">
                    <div class="section">
                      <div  class="menu1">
                        <label><input type="checkbox" class="check-all" /> ${fn:escapeXml(menu1.menuName)}</label>
                      </div>
                      <ul>
                        <c:forEach items="${menu1.childMenu}" var="menu2">
                          <li>
                            <span class="menu2">
                              <label><input type="checkbox" class="check-all" /> ${fn:escapeXml(menu2.menuName)}</label>
                            </span>

                            <c:forEach items="${menu2.childMenu}" var="menu3">
                              <c:set var="hasMenuRight" value="N" />

                              <c:forEach items="${menuRightList}" var="menuRight">
                                <c:if test='${hasMenuRight == "N"}'>
                                  <c:if test="${menu3.menuId == menuRight.menuId}">

                                    <c:set var="hasMenuRight" value="Y" />

                                  </c:if>
                                </c:if>
                              </c:forEach>

                              <span class="menu3">
                                <label><input type="checkbox" name="menuIds" value="${menu3.menuId}" ${hasMenuRight == 'Y' ? 'checked="checked"' : ''} /> ${menu3.menuName}</label>
                              </span>
                            </c:forEach>
                          </li>
                        </c:forEach>
                      </ul>
                    </div>
                  </c:forEach>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  </form:form>

<script type="text/javascript" src="/content/modules/pinterest_grid.js"></script>
<script type="text/javascript">

$(function(){
  $('#blog-landing').pinterest_grid({
      no_columns: 4,
      padding_x: 10,
      padding_y: 10,
      margin_bottom: 50,
      single_column_breakpoint: 700
  });

  $('.check-all').on('click', function(e) {
		var isChecked = $(this).prop('checked');
		var $target = $(this).parent().parent().parent().find('input[type=checkbox]');
		$target.prop('checked', isChecked);
	});

  /* $(document.body).delegate('.dyTr', 'click', function() {
    var str = ""
		var tdArr = new Array();

		var tr = $(this);
		var td = tr.children();

    $("#targetTb tr").css({"background-color":"#ffffff","color":"#000000"});

    $(this).css({"background-color":"#E4F7BA","color":"#000"});

    console.log("ddddd : "+$(this).children('input').val());
    $('#authority').val($(this).children('input').val());
		//$('#userGroupParam').submit();

  }); */

  // init 조회
  if($('#userAuthority').val() == null ||  $('#userAuthority').val() == ""){
    fnGroupSearch();
  }

  // init 하이라이트
  fnLineColor();

});

// 사용자 권한 선택시 조회
function fnGroupSearch(target){
  if(target == null){
    var tr = $("#targetTb tr:first");
    $('#authority').val(tr.children('input').val());
    $('#userAuthority').val(tr.children('input').val());
  }else{
    $('#authority').val(target.children('input').val());
    $('#userAuthority').val(target.children('input').val());
  }

  $('#userGroupParam').submit();
}

// 사용자 권한 테이블 하이라이트
function fnLineColor(){
  $("#targetTb tr").css({"background-color":"#ffffff","color":"#000000"});
  var target = $('#userAuthority').val();
  $("#targetTb tr").each(function(){
    if($(this).find('input').val() == target){
      $(this).css({"background-color":"#E4F7BA","color":"#000000"});
    }
  });
}
</script>
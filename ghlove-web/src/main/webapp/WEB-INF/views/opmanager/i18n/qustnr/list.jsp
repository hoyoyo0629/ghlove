<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"    tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"      uri="/WEB-INF/tlds/functions" %>

  <!-- 개발 영역 -->
<div class="location">
    <a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>설문 관리</span></h3>

<form:form modelAttribute="searchParam" method="post">
  <div class="board_write">
    <table class="board_write_table" summary="설문 조회">
      <caption>설문 조회</caption>
      <colgroup>
        <col style="width:220px;" />
        <col style="width:auto;" />
      </colgroup>
      <tbody>
        <tr>
          <td class="label">설문명</td> <!-- 검색구분 -->
          <td>
            <div class="flex_box gap-08">
              <form:input type="text" path="searchTxt" class="full"  />
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div> <!-- // board_write -->

  <!-- 버튼시작 -->
  <div class="btn_all btn_right">
    <div class="flex_box gap-08">
      <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/qustnr/list';"><c:out value="${op:message('M00047')}"/></button>
      <button type="submit" class="btn btn-dark-gray btn-mini"> <c:out value="${op:message('M00048')}"/></button> <!-- 검색 -->
    </div>
  </div>
  <!-- 버튼 끝-->

  <div class="count_title mt-40">
    <h5>
      <c:out value="${op:message('M00045')}"/>  <c:out value="${count}" /> <c:out value="${op:message('M00272')}"/>
    </h5>	 <!-- 전체 -->   <!-- 건 조회 -->
    <span>
      <form:select path="itemsPerPage" title="${op:message('M00239')}" onchange="$('form#searchParam').submit();" > <!-- 화면출력 -->
        <form:option value="10" label="${op:message('M00240')}" />  <!-- 10개 출력 -->
        <form:option value="20" label="${op:message('M00241')}" />  <!-- 20개 출력 -->
        <form:option value="50" label="${op:message('M00242')}" />  <!-- 50개 출력 -->
        <form:option value="100" label="${op:message('M00243')}" /> <!-- 100개 출력 -->
      </form:select>
    </span>
  </div>
</form:form>

<form id="listForm">
  <div class="board_write">
    <table class="board_list_table" summary="${op:message('M00273')}">
      <caption><c:out value="${op:message('M00273')}"/></caption>
      <colgroup>
                  <col style="width:50px;">
                  <col style="width:50px;">
                  <col style="width:100px;">
                  <col style="width:120px;">
                  <col style="width:400px;">
                  <col style="width:300px;">
                  <col style="width:100px;">
                  <col style="width:100px;">
      </colgroup>
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" name="tempId2" id="check_all" title="${op:message('M00169')}" /></th> <!-- 체크박스 -->
          <th scope="col">No</th>
          <th scope="col">등록일</th>
          <th scope="col">대상</th>
          <th scope="col">설문명</th>
          <th scope="col">기간</th>
          <th scope="col">미리보기</th>
          <th scope="col">결과보기</th>
        </tr>

      </thead>
      <tbody>
        <c:forEach items="${list}" var="list" varStatus="i">
          <tr>
            <td>
              <div><input type="checkbox" name="id" id="check" value="${fn:escapeXml(list.qustnrSn)}" title="체크" /></div>
            </td>
            <td>
              <div><c:out value='${pagination.itemNumber - i.count}'/></div>
            </td>
            <td>
              <div><c:out value="${list.frstRegistPnttm}" /></div>
            </td>
            <td>
              <div>
	              <c:choose>
	              	<c:when test="${list.srvyTrgt eq 'M'}">
	              		관리자
	              	</c:when>
	              	<c:when test="${list.srvyTrgt eq 'U'}">
	              		대민
	              	</c:when>
	              	<c:when test="${list.srvyTrgt eq 'S'}">
	              		답례품제공자
	              	</c:when>
	              </c:choose>
              </div>
            </td>
            <td>
              <div><a href="javascript:detail('${fn:escapeXml(list.qustnrSn)}')"><c:out value="${list.qustnrSj}" /></a></div>
            </td>
            <td>
              <div><c:out value="${list.qustnrBgnDe}" /> ~ <c:out value="${list.qustnrEndDe}" /></div>
            </td>
            <td>
              <div><a href="javascript:preview('${fn:escapeXml(list.qustnrSn)}');">보기</a></div>
            </td>
            <td>
              <div><a href="/opmanager/qustnr/${fn:escapeXml(list.qustnrSn)}/result">보기</a></div>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <c:if test="${empty list}">
      <div class="no_content">
        <c:out value="${op:message('M00473')}"/> <!-- 데이터가 없습니다. -->
      </div>
    </c:if>

  </div><!--// board_write E-->

  <div class="btn_all btn_right">
    <div class="flex_box gap-08">
      <a href="/opmanager/qustnr/create" class="btn btn-default btn-mini"> <c:out value="${op:message('M00088')}"/></a> <!-- 등록 -->
      <a id="delete_list_data" href="javascript:deleteQustnr();" class="btn btn-dark-gray btn-mini"><span><c:out value="${op:message('M00074')}"/></span></a> <!-- 삭제 -->
    </div>
  </div>
  </form>

  <div class="pagination-wrap">
    <page:pagination-manager /><br/>
  </div>




<!-- // 시스템/운영자 화면 case -->

<script type="text/javascript">
  $(function() {
  });

  function detail (qustnrSn) {
    location.href = '/opmanager/qustnr/' + qustnrSn;
  }

  function deleteQustnr() {

    Common.updateListData("/opmanager/qustnr/delete", Message.get("M00306"));	// 선택된 데이터를 삭제하시겠습니까?

  }

  function preview(qustnrSn) {
    window.open('${op:property("saleson.url.frontend")}/qustnr/detail_srvy.html?qustnrSn=' + qustnrSn, '_blank');
  }

</script>

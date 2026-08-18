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

	<!-- 상단 타이틀 영역 -->
    <div class="location">
        <a href=""></a> &gt; <a href=""></a> &gt; <a href="" class="on"></a>
    </div>
    <h3><span></span></h3>
    <!-- // 상단 타이틀 영역 -->

    <!-- 조회 영역 -->
    <form:form modelAttribute="qna" method="post" enctype="multipart/form-data">
	    <div class="board_write">
	        <table class="board_write_table" summary="">
	            <colgroup>
	                <col style="width:220px;">
	                <col>
	                <col style="width:220px;">
	                <col>
	            </colgroup>
	            <tbody>
	                <tr>
	                    <td class="label">${op:message('M00460')}</td><!-- 문의유형 -->
	                    <td colspan="3">
	                        <div class="flex_box gap-08">
	                            <form:select path="qnaGroup" title="${op:message('M00460')}" class="wd-150"><!-- 문의유형 -->
	                                <form:option value="">${op:message('M00039')}</form:option><!-- 전체 -->
	                                <c:forEach items="${qnaGroups}" var="group">
										<form:option value="${fn:escapeXml(group.id)}" label="${fn:escapeXml(group.label)}" />
									</c:forEach>
	                            </form:select>
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="label">${op:message('M00011')}</td><!-- 검색구분 -->
	                    <td colspan="3">
	                        <div class="flex_box gap-08">
	                            <form:select path="where" title="${op:message('M00011')}" class="wd-150"><!-- 검색구분 -->
	                                <form:option value="">${op:message('M00039')}</form:option><!-- 전체 -->
	                                <form:option value="ITEM_USER_CODE" label="${op:message('M00783')}" /> <!-- 상품코드 -->
	                                <%-- <form:option value="ITEM_NAME" label="${op:message('상품명')}" /> <!-- 상품명 --> --%>
									<form:option value="LOGIN_ID" label="${op:message('M00081')}" /> <!-- 아이디 -->
									<form:option value="USER_NAME" label="${op:message('M00005')}" /> <!-- 이름 -->
									<form:option value="SUBJECT" label="${op:message('M00275')}" /> <!-- 제목 -->
									<form:option value="QUESTION" label="${op:message('M00006')}" /> <!-- 내용 -->
									<form:option value="COMPANY_NAME" label="${op:message('M00104')}" /> <!-- 상호명 -->
	                            </form:select>
	                            <form:input path="query" title="${op:message('M00011')}" class="input_txt required _filter full" type="text" value="" /><!-- 검색구분 -->
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="label">${op:message('M00276')}</td><!-- 작성일 -->
	                    <td colspan="3">
	                        <div>
	                            <span class="datepicker">
	                                <form:input path="searchStartDate" title="${op:message('날짜 선택')}"
	                                    class="datepicker" type="text" value="" maxlength="8"
	                                    autocomplete="off" />
	                                <%-- <button type="button" class="ui-datepicker-trigger">
	                                    <span class="icon_calendar">${op:message('날짜 선택')}</span><!-- 날짜 선택 -->
	                                </button> --%>
	                            </span>
	                            <span class="wave">~</span>
	                            <span class="datepicker">
	                                <form:input path="searchEndDate" title="${op:message('날짜 선택')}"
	                                    class="datepicker" type="text" value="" maxlength="8"
	                                    autocomplete="off" />
	                                <%-- <button type="button" class="ui-datepicker-trigger">
	                                    <span class="icon_calendar">${op:message('날짜 선택')}</span><!-- 날짜 선택 -->
	                                </button> --%>
	                            </span>
	                            <span class="day_btns">
	                                <a href="javascript:;" class="btn_date today">${op:message('M00026')}</a><!-- 오늘 -->
	                                <a href="javascript:;" class="btn_date week-1">${op:message('M00027')}</a><!-- 1주일 -->
	                                <a href="javascript:;" class="btn_date month-1">${op:message('M00029')}</a><!-- 한달 -->
	                                <a href="javascript:;" class="btn_date month-3">${op:message('M00030')}</a><!-- 3개월 -->
	                                <a href="javascript:;" class="btn_date year-1">${op:message('M00031')}</a><!-- 1년 -->
	                            </span>
	                        </div>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="label">${op:message('M00462')}</td><!-- 답변상태 -->
	                    <td colspan="3">
	                        <div class="flex_box gap-12">
	                        	<form:radiobutton path="answerCount" value="0" checked="checked" label="${op:message('M00039')}" /><!-- 전체 -->
								<form:radiobutton path="answerCount" value="1" label="${op:message('M00463')}" /><!-- 답변완료 -->
								<form:radiobutton path="answerCount" value="2" label="${op:message('M00464')}" /><!-- 답변대기 -->
	                        </div>
	                    </td>
	                </tr>
	            </tbody>
	        </table>
			<div class="btn_all btn_left">
				<ul class="list-bullet point">
			        <li>
			            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
			        </li>
		        </ul>
		  	</div>
	        <div class="btn_all btn_right">
	            <div class="flex_box gap-08">
	                <button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/seller/qna-item/list';">${op:message('M00047')}</button><!-- 초기화 -->
	                <button type="submit" class="btn btn-dark-gray btn-mini">${op:message('M00048')}</button><!-- 검색 -->
	            </div>
	        </div>
	    </div>
	    <!-- // 조회 영역 -->

	    <!-- 결과 영역 -->
	    <div class="count_title mt-40">
	        <h5>${op:message('M00045')} ${fn:escapeXml(itemListCount)}${op:message('M00272')}</h5>
	        <span>
	            <form:select path="itemsPerPage" title="${op:message('화면출력수')}"><!-- 화면출력수 -->
	                <form:option value="10" selected="selected">${op:message('M00240')}</form:option><!-- 10개 출력 -->
	                <form:option value="20">${op:message('M00241')}</form:option><!-- 20개 출력 -->
	                <form:option value="50">${op:message('M00242')}</form:option><!-- 50개 출력 -->
	                <form:option value="100">${op:message('M00243')}</form:option><!-- 100개 출력 -->
	            </form:select>
	        </span>
	    </div>

    </form:form>

	<form action="/seller/qna-item/list" method="post" id="listForm">
	    <div class="board_list">
	        <table class="board_list_table">
	            <colgroup>
	                <col style="width:50px;">
	                <col style="width:50px;">
	                <col style="width:200px;">
	                <col style="width:150px;">
	                <col style="width:400px;">
	                <col style="width:150px;">
	                <col style="width:200px;">
	                <col style="width:150px;">
	                <col style="width:200px;">
	                <col style="width:200px;">
	            </colgroup>
	            <thead>
	                <tr>
	                    <th scope="col"><input type="checkbox" id="check_all"></th>
	                    <th scope="col">${op:message('No.')}</th><!-- No. -->
	                    <th scope="col">${op:message('지자체')}</th><!-- 지자체 -->
	                    <th scope="col">${op:message('M00460')}</th><!-- 문의유형 -->
	                    <%-- <th scope="col">${op:message('M00006')}</th> --%><!-- 내용 -->
	                    <th scope="col">${op:message('제목')}</th><!-- 제목 -->
	                    <th scope="col">${op:message('M00472')}</th><!-- 작성자 -->
	                    <th scope="col">${op:message('M00104')}</th><!-- 상호명 -->
	                    <th scope="col">${op:message('M00462')}</th><!-- 답변상태 -->
	                    <th scope="col">${op:message('M00276')}</th><!-- 작성일 -->
	                    <th scope="col">${op:message('답변일')}</th><!-- 답변일 -->
	                </tr>
	            </thead>
	            <tbody>
	            	<c:forEach items="${itemQnaLists}" var="item" varStatus="i">
		                <tr style="background:#fff;">
		                	<td>
		                       <div>
		                           <input type="checkbox" name="id" value="${fn:escapeXml(item.qnaId)}" title=${op:message('M00169')} />
		                       </div>
		                    </td>
		                    <td>
		                        <div>${pagination.itemNumber - i.count}</div>
		                    </td>
		                    <td>
		                        <div>
		                            ${fn:escapeXml(item.locgovName)}
		                        </div>
		                    </td>
		                    <td>
		                        <div>
		                            ${fn:escapeXml(item.qnaGroup)}
		                        </div>
		                    </td>
		                    <td>
		                        <div>
		                        	<c:if test="${!empty item.qnaAnswer && item.qnaAnswer.qnaAnswerId != 0}">
			                        	<a href="javascript:qnaDetail('${fn:escapeXml(item.qnaId)}', '${fn:escapeXml(item.qnaAnswer.qnaAnswerId)}');">
			                            	<c:if test="${!empty item.itemUserCode}">
												[${fn:escapeXml(item.itemUserCode)}]
											</c:if>
											${fn:escapeXml(item.subject)}
			                            </a>
		                        	</c:if>
		                            <c:if test="${empty item.qnaAnswer || item.qnaAnswer.qnaAnswerId == 0}">
		                            <a href="javascript:qnaDetail('${fn:escapeXml(item.qnaId)}');">
		                            	<c:if test="${!empty item.itemUserCode}">
											[${fn:escapeXml(item.itemUserCode)}]
										</c:if>
										${fn:escapeXml(item.subject)}
		                            </a>
		                            </c:if>
		                        </div>
		                    </td>
		                    <td>
		                    	<c:choose>
									<c:when test="${item.userId > 0 && !empty item.loginId}">
										<%-- <a href="javascript:Common.popup('/opmanager/user/popup/details/${item.userId}', '/user/popup/details', 1100, 800 ,1, 0, 0)"> --%>
										<div>${fn:escapeXml(item.userName)}</div>
										<div>(${fn:escapeXml(item.loginId)})</div>
										<%-- </a> --%>
									</c:when>
									<c:when test="${!empty item.userName}">
										<div>${fn:escapeXml(item.userName)}</div>
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
		                    </td>
		                    <td>
		                        <div>${fn:escapeXml(item.sellerCompanyName)}</div>
		                    </td>
		                    <td>
		                        <div>${item.qnaAnswer != null && item.qnaAnswer.qnaAnswerId > 0 ? op:message('M00463') : op:message('M00464')}</div>		<!-- 답변완료 --> <!-- 답변대기 -->
		                    </td>
		                    <td>
		                        <div>${op:date(item.createdDate)}</div>
		                    </td>
		                    <td>
		                    	<c:if test="${item.qnaAnswer != null}"><div>${op:date(item.qnaAnswer.answerDate)}</div></c:if>
		                    </td>
		                </tr>
	                </c:forEach>
	            </tbody>
	        </table>

	        <c:if test="${empty itemQnaLists}">
				<div class="no_content">
						${op:message('M00473')} <!-- 데이터가 없습니다. -->
				</div>
			</c:if>

	        <!-- 하단 버튼 -->
	        <div class="btn_all btn_right">
	            <div class="flex_box gap-08">
	                <button id="delete_list_data" type="button" class="btn btn-dark-gray btn-mini">${op:message('M00074')}</button><!-- 삭제 -->
	            </div>
	        </div>
	        <!-- // 하단 버튼 -->

	        <div class="pagination-wrap">
	            <page:pagination-manager /><br/>
	        </div>
	    </div>
    </form>
    <!-- // 결과 영역 -->

	<%-- <div style="display: none;">
		<span id="today">${today}</span>
		<span id="week">${week}</span>
		<span id="month1">${month1}</span>
		<span id="month3">${month3}</span>
	</div> --%>

<script type="text/javascript">
    var $qnaTypeOptions;
    $(function() {
        $qnaTypeOptions = $('<select />').append($('#qnaType option').clone());
        //목록데이터 - 삭제처리
        $('#delete_list_data').on('click', function() {
            Common.updateListData("/seller/qna-item/delete", "${op:message('M00196')}");// 삭제하시겠습니까?

        });

        /* $('#itemsPerPage').on("change", function(){
            $('#qna').submit();
        }); */

        $(".btn_date").on('click',function(){

            var $id = $(this).attr('class').replace('btn_date ','');		// id[0] : type, id[1] : value

            if ($id == 'all') {

                $("input[type=text]",$(this).parent().parent()).val('');

            } else {

                var today = $("#today").text();

                var date1 = '';
                var date2 = '';

                if ($id == 'today') {
                    date1 = today;
                    date2 = today;
                } else {
                    date1 = $("#"+$id).text();
                    date2 = today;
                }

                $("input[type=text]", $(this).parent().parent()).eq(0).val(date1);
                $("input[type=text]", $(this).parent().parent()).eq(1).val(date2);

            }
        });

        $('#qnaGroup').on('change', function() {
            var type = $(this).val();
            $('#qnaType').find('option').not(':first-child').remove();
            $option = $qnaTypeOptions.find('.qna_type_' + type).show().clone();
            $('#qnaType').append($option).val("0");
        });

        if ("${fn:escapeXml(param.sido)}" != "") {		// 세팅된 시도 값 있을 경우
        	// $("#sido").val("${fn:escapeXml(param.sido)}").prop("selected", true);		// 시도 값 세팅
            searchSigungu(document.getElementById("sido"));		// 시군구 목록 조회
        }
    });
<%--
    //엑셀 다운로드 팝업.
    function downloadExcel() {
        if (confirm("엑셀파일로 다운로드 받으시겠습니까?")) {
            $('#listForm').submit();

            // 다운로드 체크.
            $.cookie('DOWNLOAD_STATUS', 'in progress', {path:'/'});
            checkDownloadStatus();
        } else {
            return false;
        }
    }

    //다운로드 체크
    function checkDownloadStatus() {
        if ($.cookie('DOWNLOAD_STATUS') == 'complete') {
            Common.loading.hide();
            return;
        } else {
            setTimeout("checkDownloadStatus()", 1000);
        }
    }

    //판매자명 검색기능 추가 2017-03-10 yulsun.yoo
    function sellerSeller(sellerId) {
        $('#sellerId').val(sellerId);
    }
--%>
    function qnaDetail(qnaId, answerId) {
    	var detailUrl = "/seller/qna-item/answer/"+qnaId;
    	if(answerId > 0 && answerId !== undefined){
    		detailUrl = "/seller/qna-item/answer/"+qnaId + "?answerId="+answerId;
    	}
        //$("#qna").attr("action", "/opmanager/qna-item/answer/"+qnaId).submit();
        location.href= detailUrl;
    }

</script>

<script type="text/javascript">
    $(function() {
        Common.DateButtonEvent.set('.day_btns > a[class^=btn_date]', '', 'input[name="searchStartDate"]' , 'input[name="searchEndDate"]');
        EventHandler.calendarStartDateAndEndDateVaild();
    });
</script>
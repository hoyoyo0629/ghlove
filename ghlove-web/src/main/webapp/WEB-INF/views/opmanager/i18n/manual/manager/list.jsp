<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<h3><span>메뉴얼 관리</span></h3>
	<form:form modelAttribute="searchParam" method="post">

	<!-- 20221124 수정 -->
	<div>
	    <div class="flex_box gap-20 item-center">
	        <!-- <div class="checkbox">
	            <input type="checkbox" id="chk" class="check-all">
	            <label for="chk">전체선택</label>
	        </div>
	        <button type="button" class="btn btn-dark-gray btn-micro" onClick="downloadCheckManual()">선택 다운로드</button> -->
	    </div>
	    <!-- <div class="grid_wrap"> -->
	    <div id="blog-landing" class="input_wrap" style="height: 100vh;">
	    	<c:forEach items="${menuList}" var="menu2">
	        <div class="board_list">
	            <table class="board_list_table" summary="">
	                <caption></caption>
	                <colgroup>
	                    <!-- <col style="width:50px;"> -->
	                    <col style="width:200px;">
	                    <col style="width:100px;">
	                </colgroup>
	                <thead>
	                    <tr>
	                        <!-- <th scope="col">
	                            <input type="checkbox" name="" value="" id="check_all" class="check-all">
	                        </th> -->
	                        <th scope="col"><c:out value="${menu2.menu_name }"/></th>
	                        <th scope="col">메뉴얼</th>
	                    </tr>
	                </thead>
	                <tbody>
	                	<c:forEach items="${menu2.detail}" var="menu3">
	                    <tr style="background:#fff;">
	                        <!-- <td>
	                            <input type="checkbox" name="" value="${menu3.file_nm != null ? menu3.menu_id : ''}" id="check_${fn:escapeXml(menu3.menu_id)}">
	                        </td> -->
	                        <td>
	                        	<c:if test="${role == 'SYSTEM'}">
	                            	<a href="javascript:fileUploadOpen('${fn:escapeXml(menu3.menu_id) }', '${fn:escapeXml(menu3.menu_name) }')"><c:out value="${menu3.menu_name }"/></a>
	                            </c:if>
	                            <c:if test="${role != 'SYSTEM'}">
	                            	<c:out value="${menu3.menu_name }"/>
	                            </c:if>
	                        </td>
	                        <td>
	                        	<c:if test="${menu3.file_nm != null && menu3.file_nm != ''}">
	                        		<a href="javascript:downloadItemImage('${fn:escapeXml(menu3.menu_id) }')">다운로드</a>
	                        	</c:if>
	                        	<c:if test="${menu3.file_nm == null || menu3.file_nm == ''}">
	                        		-
	                        	</c:if>

	                        </td>
	                    </tr>
	                    </c:forEach>
	                </tbody>
	            </table>
	        </div>
	    	</c:forEach>
	    </div>
	</div>
	<!-- // 20221124 수정 -->
	</form:form>

<!-- 20221124 수정 -->
<script type="text/javascript" src="/content/modules/pinterest_grid.js"></script>
<script type="text/javascript">

    $(function () {

        $('#blog-landing').pinterest_grid({
            no_columns: 4,
            padding_x: 10,
            padding_y: 10,
            margin_bottom: 50,
            single_column_breakpoint: 700
        });
    });

	$('.check-all').on('click', function(e) {
		var isChecked = $(this).prop('checked');
		var $target = $(this).parent().parent().parent().parent().find('input[type=checkbox]');
		$target.prop('checked', isChecked);
	});

	function fileUploadOpen(id, name) {
		var popupWidth = 500;
		var popupHeight = 300;
		var popupY = (window.screen.height / 2) - (popupHeight / 2);
		var popupX = (window.screen.width / 2) - (popupWidth / 2);
		popupX += window.screenLeft;

		window.open('/opmanager/manual/manager/create?menuId='+id+'&menuName='+name,'pop','toolbar=no, menubar=no, location=no, resizable=no, scrollbars=no, status=no, titlebar=no, width=500,height=300, left='+ popupX + ', top='+ popupY);
	}

	function downloadItemImage(id) {

		/*$.ajax({
			url : '/opmanager/manual/manager/file-download/'+id,
			type : 'get',
			success: function (data, status, xhr) {

				var disposition = xhr.getResponseHeader("content-disposition");
				var blob = new Blob([data], { type: xhr.getResponseHeader('content-type') });
				disposition = decodeURI(disposition);
				var dispositionArr = disposition.split('filename=');
				var fileName = dispositionArr[dispositionArr.length > 1 ? 1 : 0];

				// IE 10일 경우 생성한 blob을 fileName의 이름으로 다운로드 수행
				if (window.navigator.msSaveOrOpenBlob) { // IE 10+
					window.navigator.msSaveOrOpenBlob(blob, fileName);
				}

				var link = document.createElement('a');
				var url = window.URL.createObjectURL(blob);
				link.href = url;
				link.target = '_self';

				link.download = fileName;
				// 문서에 설정한 a 태그 생성
                document.body.append(link);

                // a 태그 클릭 이벤트 (다운로드 실행)
                link.click();

                // a 태그 삭제
                link.remove();

                // 생성한 blob 파일의 경로 제거 (다시 요청해도 다운로드 불가)
                window.URL.revokeObjectURL(url);

			},
			error: function (err) {
				alert("파일이 존재하지 않습니다.");
			}
		});*/

		location.href = '/opmanager/manual/manager/file-download/'+id;
	}

	function downloadCheckManual() {
		var manualList = new Array;

		var iFrameCnt = 0;
		$("input[id^='check_']:checked").not("[id='check_all']").map(function(index, item) {
			if ($(item).val() != '') {
				manualList.push($(item).val());

				url = '/opmanager/manual/manager/file-download/'+$(item).val();

				fnCreateIframe(iFrameCnt); // 보이지 않는 iframe 생성, name는 숫자로

	            $("iframe[name=" + iFrameCnt + "]").attr("src", url);

	            iFrameCnt++;

	            fnSleep(1000); //각 파일별 시간 텀을 준다
			}

		});

		if(manualList.length == 0) {
			alert("다운로드할 게시물을 선택해주세요.");
			return false;
		}

// 		if(confirm("다운로드 하시겠습니까?")) {


// 			url = '/opmanager/manual/manager/file-download/checkList/'+manualList;

// 			fnCreateIframe(iFrameCnt); // 보이지 않는 iframe 생성, name는 숫자로

//             $("iframe[name=" + iFrameCnt + "]").attr("src", url);

//             iFrameCnt++;

//             fnSleep(1000); //각 파일별 시간 텀을 준다

// // 			$.post('/opmanager/manual/manager/file-download/checkList', {"manualList": manualList}, function(response) {

// // 				console.log("response", response);
// // // 				if(response.isSuccess && response.data) {
// // // 					if(response.data == "SUCC") {
// // // 						alert("삭제되었습니다.");
// // // 					} else {
// // // 						alert("오류가 발생했습니다.");
// // // 					}
// // // 				}
// // 			});
// 		}
	}

	function fnSleep (delay){

        var start = new Date().getTime();
        while (start + delay > new Date().getTime());

    };

    function fnCreateIframe (name){

        var frm = $('<iframe name="' + name + '" style="display: none;"></iframe>');
        frm.appendTo("body");

    }
</script>
<!-- // 20221124 수정 -->
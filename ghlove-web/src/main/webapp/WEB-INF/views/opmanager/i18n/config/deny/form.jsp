<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

	<h3><span>회원등록 불가능ID</span></h3>

	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>

	<div class="board_write">
		<table class="board_write_table">
			<colgroup>
				<col style="width:150px;" />
				<col style="width: auto;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="label">아이디</th>
					<td>
						<div>
							<input type="text" name="deniedIds" class="form-half required" title="아이디" />
							<button type="button" name="inputAdd" class="btn btn-dark-gray btn-sm">추가</button>
							<button type="button" name="inputDel" class="btn btn-dark-gray btn-sm">제거</button>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_center">
		<button type="button" onclick="editDeniedId();" class="btn btn-active"><c:out value="${op:message('M00101')}"/></button>
		<a href="javascript:history.back();" class="btn btn-default"><c:out value="${op:message('M00037')}"/></a>
	</div>

	<div class="board_guide ml10">
		<p class="tip">* ID는 중복 등록이 불가능합니다. 중복 ID를 입력하는 경우 1건만 저장됩니다.</p>
	</div>

<script type="text/javascript">
    var tr = createInput();

    function createInput(){
        var tr = document.createElement("tr");
        var th = document.createElement("th");
        th.setAttribute("class", "label");
        th.innerHTML = "아이디";
        var td = document.createElement("td");
        var div = document.createElement("div");
        var input = document.createElement("input");
		input.setAttribute("type", "text");
		input.setAttribute("title", "아이디");
        input.setAttribute("class", "required form-half");
        div.appendChild(input);
        td.appendChild(div);
        tr.appendChild(th);
        tr.appendChild(td);
        return tr;
    }

    $("button[name='inputAdd']").click(function(){
        var size = $("table tbody tr").length;
        if (size <= 4) {
            $("table tbody").append($(tr).clone());
        } else if (size == 5) {
            alert("한번에 최대 5개까지 등록이 가능합니다.");
        }
    });

    $("button[name='inputDel']").click(function() {
        var size = $("table tbody tr").length-1;
        if (size != 0) {
            $("table tbody tr:eq("+size+")").remove();
        }
    });

    function inputAddIdData(){
        var size = $("table tbody tr").length;
        var id = "";
        for (var i = 0; i < size; i++) {
        	var $input = $("table tbody tr:eq("+i+") input");
        	if ($input.val().trim() == "") {
				alert("아이디를 입력해주세요. (No. " + (i + 1) + ")");
				$input.focus();
				return null;
	        }
			if (!$.validator.patterns._id.test($input.val().trim())) {
				alert($.validator.messages._id + " (No. " + (i + 1) + ")");
				$input.focus();
				return null;
			}
			id += $input.val().trim() + ",";
        }

        id = id.substr(0, id.length -1);
        return id;
    }

    function editDeniedId() {
    	var id = inputAddIdData();
	    var params = {
			deniedIds : id,
		    deniedKey : "2"
	    };

		if (id != "" && id != null) {
		    $.post("/opmanager/config/deny/edit", params, function(response) {
				Common.responseHandler(response, function(response) {
					alert(response.data);
					location.href = "/opmanager/config/deny/edit";
				});
		    });
	    }

    }

</script>

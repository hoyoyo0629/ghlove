<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>



<form:form modelAttribute="item" method="post">
	<form:hidden path="id" />
	<input type="hidden" name="searchParam" id="item" />

	<div class="board_write">
		<table class="board_write_table" summary="${op:message('M00269')}">
			<colgroup>
				<col style="width: 220px;">
			</colgroup>
			<tbody>
				<tr>
					<td class="label"><span class="required_mark">*</span>
					<c:out value="${op:message('M00275')}" /></td>
					<td>
						<div>
							<form:input path="subject" title="${op:message('M00275')}"
								cssClass="input_txt required _filter half subject"
								maxlength="255" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span>
					<c:out value="${op:message('M00006')}" /></td>
					<td>
						<!-- smart_editor2 area -->
						<div>
							<form:textarea path="content" cols="30" rows="20"
								class="w90 editor-content" name="test"
								title="${op:message('M00006')}" />
						</div> <!-- smart_editor2 area -->
					</td>
				</tr>

			</tbody>
		</table>
		<div class="btn_all btn_center">
			<div class="flex_box gap-08">
				<button type="submit" class="btn btn-dark-gray btn-small">
					<span>등록</span>
				</button>
				<button type="button" class="btn btn-defualt btn-small"
					onclick="location.href='/opmanager/community/list'">
					<c:out value="${op:message('M00037')}" />
				</button>
			</div>
		</div>

	</div>
</form:form>


<module:smarteditorInit />
<module:smarteditor id="content" />

<script type="text/javascript">
$(function() {	
	try{
		$('#item').validator(function() {
			Common.getEditorContent("content");
	
			if ($('#content').val().toLowerCase() == '<p>&nbsp;</p>' || $('#content').val() == '') {
				alert($.validator.messages['text'].format("${op:message('M00006')}"));
				return false;
			}
		})
	} catch(e) {
		alert(e.message);
	}
});

function UpdateItem(itemId) {
	var subject = $(".subject").val();
	var content = $("textarea[name='test']").val();
	var url = "/opmanager/community/locv-faq/edit/" + itemId
	Common.confirm("수정 하시겠습니까?", function() {
		$.post(url, {subject,content}, function(response) {			
			Common.responseHandler(response, function() {
				alert("${op:message('M01673')}");
				location.href = "/opmanager/community/locv-faq/list";

			});
		});
	});

	
}

</script>
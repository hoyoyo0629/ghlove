<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="module" tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>


<h3><span></span></h3>
<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>
<form:form modelAttribute="searchParam" method="post">
	<form:hidden path="bbsId" />
	<input type="hidden" name="searchParam" id="searchParam" />

	<div class="board_write">
		<table class="board_write_table" summary="${op:message('M00269')}">
			<colgroup>
				<col style="width: 220px;">
			</colgroup>
			<tbody>
				<c:if test="${role eq 'ROLE_ADMIN_1' || role eq 'ROLE_ADMIN_2' || role eq 'ROLE_ADMIN_3' || role eq 'ROLE_ADMIN_4'}">
					<tr>
						<td class="label">상단공지</td>
						<td>
							<div class="flex_box gap-08 item-center">
								<div class="checkbox">
									<!-- checkbox id="noticeYn1" -->
									<form:checkbox path="noticeYn" label="${op:message('M00083')}" value="N" />
								</div>
							</div>
						</td>
					</tr>
				</c:if>
				<tr>
					<td class="label">비밀글</td>
					<td>
						<div class="flex_box gap-08 item-center">
							<div class="checkbox">
								<form:checkbox path="isSecret" label="${op:message('M00083')}" value="N" />
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span> <c:out value="${op:message('M00275')}" /></td>
					<td>
						<div>
							<form:input path="bbsTtl" title="${op:message('M00275')}" cssClass="input_txt required _filter half" maxlength="255" />
						</div>
					</td>
				</tr>
				<tr>
					<td class="label"><span class="required_mark">*</span> <c:out value="${op:message('M00006')}" /></td><td>
						<!-- smart_editor2 area -->
						<div>
							<form:textarea path="bbsCn" cols="30" rows="20" maxlength="300" class="w90 editor-content" title="${op:message('M00006')}" /></div> <!-- smart_editor2 area -->
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_all btn_center">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-small add" ><span>등록</span></button>
				<button type="button" class="btn btn-defualt btn-small cancel"><c:out value="${op:message('M00037')}" /></button>
			</div>
		</div>
	</div>
</form:form>


<module:smarteditorInit />
<module:smarteditor id="bbsCn" />
<script type="text/javascript" src="/content/modules/cmnty/bbs.js?<spring:eval expression="@environment.getProperty('jsVersion')" />"></script>
<script type="text/javascript">
	$(function() {
		try {
			$('#searchParam')
					.validator(
							function() {
								Common.getEditorContent("bbsCn");

								if ($('#bbsCn').val().toLowerCase() == '<p>&nbsp;</p>'
										|| $('#bbsCn').val() == '') {
									alert($.validator.messages['text']
											.format("${op:message('M00006')}"));
									return false;
								}
							})
		} catch (e) {
			alert(e.message);
		}

		bbs.create.init();
});

</script>
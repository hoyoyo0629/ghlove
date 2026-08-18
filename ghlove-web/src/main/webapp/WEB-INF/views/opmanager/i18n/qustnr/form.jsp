<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" 	uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop" %>


<!-- 개발 영역 -->
<!-- 설문관리 주관식질문 추가, 연계질문 기능 추가 - 조형원 -->
<div class="location">
	<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span>설문 관리</span></h3>

<c:if test="${not empty qestnar}">
	<div class="btn_all btn_right mb15">
		<div class="flex_box gap-08">
			<button type="submit" class="btn btn-dark-gray btn-mini" onclick="modifyQustnr('${fn:escapeXml(qestnar.qustnrSn)}')">수정</button>
			<button type="button" class="btn btn-dark-default btn-mini" onclick="location.href='/opmanager/qustnr/list'">목록</button>
		</div>
	</div>
</c:if>

<div class="board_write">
	<table class="board_write_table" summary="설문 정보">
		<colgroup>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<td class="label">설문명</td>
				<td>
					<div class="flex_box gap-08">
						<input name="qustnrSj" title="설문명" class="input_txt required _filter full" type="text" value="${fn:escapeXml(qestnar.qustnrSj)}" />
					</div>
				</td>
			</tr>
			<tr>
			   <td class="label">설문 기간</td>
			   <td>
				   <div>
					   <span class="datepicker">
							<input type="text" name="qustnrBgnDe" title="설문 시작일" class="datepicker optional" title="${op:message('M00507')}" value="${fn:escapeXml(qestnar.qustnrBgnDe)}" />
					   </span>
					   <span class="wave">~</span>
					   <span class="datepicker">
							<input type="text" name="qustnrEndDe" title="설문 종료일" class="datepicker optional" title="${op:message('M00509')}" value="${fn:escapeXml(qestnar.qustnrEndDe)}" />
					   </span>
				   </div>
			   </td>
			</tr>
			<tr>
			   <td class="label">설문 대상</td>
			   <td>
				   <div class="flex_box gap-12">
                        <div class="input-form">
                        	<input type="radio" name="srvyTrgt" id= "srvyTrgt_U" value="U" ${op:checked('U', qestnar.srvyTrgt)} /><label for="srvyTrgt_U" style="margin-left:3px;">대민</label>
                        </div>
                        <div class="input-form">
                            <input type="radio" name="srvyTrgt" id= "srvyTrgt_M" value="M" ${op:checked('M', qestnar.srvyTrgt)} /><label for="srvyTrgt_M" style="margin-left:3px;">관리자</label>
                        </div>
                        <div class="input-form">
                            <input type="radio" name="srvyTrgt" id= "srvyTrgt_S" value="S" ${op:checked('S', qestnar.srvyTrgt)} /><label for="srvyTrgt_S" style="margin-left:3px;">답례품제공자</label>
                        </div>
                    </div>
			   </td>
			</tr>
		</tbody>
	</table>
</div>

<div class="count_title mt-40">
	<h5></h5>
	<div style="display: flex;justify-content: flex-end;">
		<button style="margin-right: 1rem;" type="button" class="btn btn-dark-gray btn-mini" onclick="subjectiveAddQuestion()">주관식 질문 추가</button><!-- 주관식 질문 추가 250520 조형원  -->
		<button type="button" class="btn btn-dark-gray btn-mini" onclick="addQuestion()">질문 추가</button>
	</div>
</div>
<input type="hidden" id="qustenCnt" value="${not empty qestnar ? qestnar.maxQesitm : 0}" />
<div class="board_write mt-40 question_board">
	<table id="qusten" class="board_write_table" summary="질문 테이블">
		<colgroup>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody id="mainBody">
			<c:if test="${qestnar != null && not empty qestnar.qustnrQesitm}">
				<c:forEach items="${qestnar.qustnrQesitm}" var="item" varStatus="i">
					<tr class="qusten_${fn:escapeXml(item.qustnrQesitmSn)}">
						<td class="label">질문</td>
						<td>
							<div class="flex_box gap-08" style="${item.parentSn > 0 ? 'background-color: blue;' : ''}">
								<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(item.qustnrQesitmSn)}" />
								<input name="qestnTyCode" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(item.qestnTyCode)}" />
								<input name="qestnCn" title="질문" class="input_txt required _filter full question" type="text" value="${fn:escapeXml(item.qestnCn)}" code="Q" />
								<input name="parentSn" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(item.parentSn)}" />
							</div>
						</td>
					</tr>
					<c:if test="${item.qestnTyCode == 'rtype'}">
						<tr class="qusten_${fn:escapeXml(item.qustnrQesitmSn)}">
							<td class="label">답변</td>
							<td class="answer">
								<c:set var="answerList" value="${item.qustnrIem}"></c:set>
								<c:forEach items="${answerList}" var="answer" varStatus="i">
									<div class="flex_box gap-08 answer-box">
										<input type="radio" title="답변" class="input_txt required _filter" type="text" value="" onclick="return false;" />
										<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(answer.qustnrQesitmSn)}" />
										<input name="qustnrIemSn" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(answer.qustnrIemSn)}" />
										<input name="answer" title="답변" class="input_txt required _filter ml15 full" type="text" value="${fn:escapeXml(answer.iemCn)}" code="A" />
										<button type="button" class="btn btn-gray btn-mini" style="<c:if test='${answerList.size() <= 1}'>display: none;</c:if>" onclick="removeAnswer(this)">삭제</button>
									</div>
								</c:forEach>
							</td>
						 </tr>
						 <tr class="qusten_${fn:escapeXml(item.qustnrQesitmSn)}">
							<td colspan="2">
								<div class="flex_box gap-08" style="float : right">
									<c:if test="${item.parentSn <= 0}">
										<button type="button" class="btn btn-mini" onclick="linkQuestion(this)">연계질문 추가</button><!-- 연계 질문 추가 250520 조형원  -->
									</c:if>
									<button type="button" class="btn btn-orange btn-mini" onclick="addAnswer(this)">답변 추가</button>
									<button type="button" class="btn btn-orange btn-mini" onclick="removeQusten(this)">질문 삭제</button>
								</div>
							</td>
						</tr>
					</c:if>
					<c:if test="${item.qestnTyCode == 'stype'}">
						<tr class="qusten_${fn:escapeXml(item.qustnrQesitmSn)}">
							<td class="label">답변</td>
							<td class="answer">
								<c:set var="answerList" value="${item.qustnrIem}"></c:set>
								<c:forEach items="${answerList}" var="answer" varStatus="i">
								   <div class="flex_box gap-08 answer-box">
										<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(answer.qustnrQesitmSn)}" />
										<input name="qustnrIemSn" type="hidden" class="input_txt required _filter" value="${fn:escapeXml(answer.qustnrIemSn)}" />
										<input name="answer" title="주관식 답변" style="margin:0;" class="input_txt required _filter ml15 full" type="text" value="" code="A" placeholder="100자 이내 주관식 답변입니다." readOnly/>
										<button type="button" class="btn btn-gray btn-mini" style="<c:if test='${answerList.size() <= 1}'>display: none;</c:if>" onclick="removeAnswer(this)">삭제</button>
								   </div>
								</c:forEach>
							</td>
						 </tr>
						 <tr class="qusten_${fn:escapeXml(item.qustnrQesitmSn)}">
							<td colspan="2">
								<div class="flex_box gap-08" style="float : right">
									<button type="button" class="btn btn-orange btn-mini" onclick="removeQusten(this)">질문 삭제</button>
								</div>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>
<c:if test="${empty qestnar}">
	<div class="btn_all btn_center">
		<div class="flex_box gap-08">
			<button type="submit" class="btn btn-dark-gray btn-small" onclick="insertQustnr()"><span>등록</span></button>
			<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/qustnr/list'">취소</button>
		</div>
	</div>
</c:if>


<table id="qustenDummy" style="display: none;">
	<tr>
		<td class="label">질문</td>
		<td>
			<div class="flex_box gap-08">
				<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="0" />
				<input name="qestnTyCode" type="hidden" class="input_txt required _filter" value="rtype" />
				<input name="qestnCn" title="질문" class="input_txt required _filter full question" type="text" value="" code="Q" />
				<input name="parentSn" type="hidden" class="input_txt required _filter" value="0" />
			</div>
		</td>
	</tr>
	<tr>
	   <td class="label">답변</td>
	   <td class="answer">
		   <div class="flex_box gap-08 answer-box">
				<input type="radio" title="답변" class="input_txt required _filter" type="text" value="" onclick="return false;" />
				<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="0" />
				<input name="answer" title="답변" class="input_txt required _filter ml15 full" type="text" value="" code="A" />
				<button type="button" class="btn btn-gray btn-mini" style="display: none;" onclick="removeAnswer(this)">삭제</button>
		   </div>
	   </td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="flex_box gap-08" style="float : right">
				<button type="button" class="btn btn-mini" onclick="linkQuestion(this)">연계질문 추가</button><!-- 연계 질문 추가 250520 조형원  -->
				<button type="button" class="btn btn-orange btn-mini" onclick="addAnswer(this)">답변 추가</button>
				<button type="button" class="btn btn-orange btn-mini" onclick="removeQusten(this)">질문 삭제</button>
			</div>
		</td>
	</tr>
</table>

<table id="subjectiveQustenDummy" style="display: none;">
	<tr>
		<td class="label">질문</td>
		<td>
			<div class="flex_box gap-08">
				<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="0" />
				<input name="qestnTyCode" type="hidden" class="input_txt required _filter" value="stype" />
				<input name="qestnCn" title="질문" class="input_txt required _filter full question" type="text" value="" code="Q" />
				<input name="parentSn" type="hidden" class="input_txt required _filter" value="0" />
			</div>
		</td>
	</tr>
	<tr>
	   <td class="label">답변</td>
	   <td class="answer">
		   <div class="flex_box gap-08 answer-box">
				<input name="qustnrQesitmSn" type="hidden" class="input_txt required _filter" value="0" />
				<input name="answer" title="주관식 답변" style="margin:0;" class="input_txt required _filter ml15 full" type="text" value="" code="A" placeholder="100자 이내 주관식 답변입니다." readOnly/>
				<button type="button" class="btn btn-gray btn-mini" style="display: none;" onclick="removeAnswer(this)">삭제</button>
		   </div>
	   </td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="flex_box gap-08" style="float : right">
				<button type="button" class="btn btn-orange btn-mini" onclick="removeQusten(this)">질문 삭제</button>
			</div>
		</td>
	</tr>
</table>

<script type="text/javascript">
$(function() {
	$(".contents_inner").find("div.location a").removeClass("on");
	$(".contents_inner").find("div.location").append('> <a href="javascript:;" class="on">상세</a>');

	var qestnar = '${fn:escapeXml(qestnar)}';

	if (!qestnar) {
		addQuestion();
	}

});

/* 주관식 질문 추가 250519 조형원 */
const subjectiveAddQuestion = () => {
	var cnt = Number($("#qustenCnt").val()) + 1;
	const clone = $("#subjectiveQustenDummy tbody tr").clone();

	clone.appendTo("#qusten tbody");

	$("#qustenCnt").val(cnt);
	reClassQuestionSet();
}

/* 질문 엘리먼트 class 재정렬 250523 조형원*/
const reClassQuestionSet = () => {
	const allTr = $('#mainBody tr');

	let index = 1;

	for(let i = 0; i < allTr.length; i+= 3) {
		allTr.eq(i).attr('class', 'qusten_'+ index);
		allTr.eq(i + 1).attr('class', 'qusten_'+ index);
		allTr.eq(i + 2).attr('class', 'qusten_'+ index);

		allTr.eq(i).find('input').first().val(index);
		index++;
	}
}

/* 하위 연계질문 추가 250519 조형원 */
const linkQuestion = (el) => {
	const cnt = Number($("#qustenCnt").val()) + 1;

	const $currentControlRow = $(el).closest('tr');
	const $parent = $currentControlRow.prev().prev();
	const parentId = $parent.find('input').first().val()
	const clone = $("#qustenDummy tbody tr").clone();

	clone.eq(0).find('input').eq(3).val(parentId);
	clone.eq(0).css({backgroundColor: 'blue'});
	clone.closest('tr').eq(2).find('td div button').first().remove();
	clone.eq(2).insertAfter($currentControlRow);
	clone.eq(1).insertAfter($currentControlRow);
	clone.eq(0).insertAfter($currentControlRow);

	$("#qustenCnt").val(cnt);
	reClassQuestionSet();

}

// 질문 추가
function addQuestion () {
	var cnt = Number($("#qustenCnt").val()) + 1;
	var clone = $("#qustenDummy tbody tr").clone();

	clone.appendTo("#qusten tbody");

	reClassQuestionSet();
	$("#qustenCnt").val(cnt);

}

// 답변 추가
function addAnswer (that) {
	var parentTd = $(that).closest('tr').prev().find('.answer');

	/* 20241105 답변 제한 해제
	if (parentTd.find('.answer-box').length >= 5) {
		alert("질문 하나에 답변은 최대 5개까지 가능합니다.");
		return false;
	};
	*/

	var asd = $("#qustenDummy .answer > div:first").clone();
	$(that).closest('tr').prev().find('.answer').append(asd);

	var box = parentTd.find('div');

	if (box.length > 1) {
		box.find('button').show();
	} else {
		box.find('button').hide();
	}

}

// 연계질문 삭제 기능로직 추가 250526 조형원
function removeQusten (that) {
	const $controlTr = $(that).closest('tr');
	const cn = $controlTr.attr('class');

	const $questionTr = $controlTr.prev().prev();
	const parentId = $questionTr.find('input').eq(0).val();

	$('tr.' + cn).remove();
	if(parentId > 0) {
		$('tr[class^="qusten_"]').each(function(i, tr) {
			if(i % 3 !== 0) return;
			const $tr = $(tr);
			const pid = $tr.find('input').eq(3).val();
			const childClass = $tr.attr('class');

			if(('qusten_' + pid) === cn) {
				$('tr.' + childClass).remove();
			}
		})
	}

	reClassQuestionSet();

	$("#qustenCnt").val($('#mainBody tr').length / 3);
}

function removeAnswer (that) {
	var div = $(that).parent();
	var td = div.parent();

	div.remove();

	if (td.find('.answer-box').length > 1) {
		td.find('button').show();
	} else {
		td.find('button').hide();
	}
}

// 설문 등록
function insertQustnr () {

	if (!validation() || !confirm('정보를 저장 하시겠습니까?')) return false;

	var params = {
		qustnrSj : $("input[name=qustnrSj]").val(),
		qustnrBgnDe : $("input[name=qustnrBgnDe]").val(),
		qustnrEndDe : $("input[name=qustnrEndDe]").val(),
		qustnrQesitm : [],
		srvyTrgt : $("input[name=srvyTrgt]:checked").val()

	}

	$('.question_board input[code=Q]').each(function (idx, el) {
		var arr = [];

		$(el).closest('tr').next().find('.answer').find('input[name=answer]').each(function (idx2, el2) {
			arr.push({
				qustnrIemSn : 0,
				iemCn : $(el2).val(),
			})
		});

		params.qustnrQesitm.push({
			qestnCn : $(el).val(),
			qestnTyCode : $(el).prev().val(),
			parentSn: $(el).next().val(),
			qustnrQesitmSn : $(el).prev().prev().val(),
			qustnrIem : arr
		});
	});

	$.ajax({
		url: "/opmanager/qustnr/create",
		data: JSON.stringify(params),
		type: 'POST',
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		error: function (error) {
			alert('저장에 실패하였습니다.');
		},
		success: function (data) {
			if (data.isSuccess) {
				alert('저장되었습니다.');
				location.href = '/opmanager/qustnr/list';
			} else {
				alert(data.errorMessage ? data.errorMessage : '저장에 실패하였습니다.');
			}
		}
	});


}

// 설문 수정
function modifyQustnr(qustnrSn) {
	if (!validation() || !confirm('정보를 수정 하시겠습니까?')) return false;

	var params = {
		qustnrSj : $("input[name=qustnrSj]").val(),
		qustnrBgnDe : $("input[name=qustnrBgnDe]").val(),
		qustnrEndDe : $("input[name=qustnrEndDe]").val(),
		qustnrQesitm : [],
		srvyTrgt : $("input[name=srvyTrgt]:checked").val()
	}

	$('.question_board input[code=Q]').each(function (idx, el) {

		var arr = [];
		var qustnrQesitmSn = $(el).prev().prev().val();

		$(el).closest('tr').next().find('.answer').find('input[name=answer]').each(function (idx2, el2) {
			arr.push({
				qustnrIemSn : $(el2).prev().val(),
				iemCn : $(el2).val(),
				qustnrSn : qustnrSn,
				qustnrQesitmSn : qustnrQesitmSn
			})
		});

		params.qustnrQesitm.push({
			qestnCn : $(el).val(),
			qestnTyCode : $(el).prev().val(),
			qustnrQesitmSn : qustnrQesitmSn,
			parentSn: $(el).next().val(),
			qustnrSn : qustnrSn,
			qustnrIem : arr
		});
	});

	$.ajax({
		url: "/opmanager/qustnr/" + qustnrSn,
		data: JSON.stringify(params),
		type: 'POST',
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		error: function (error) {
			alert('저장에 실패하였습니다.');
		},
		success: function (data) {
			if (data.isSuccess) {
				alert('저장되었습니다.');
				location.href = '/opmanager/qustnr/list';
			} else {
				alert(data.errorMessage ? data.errorMessage : '저장에 실패하였습니다.');
			}
		}
	});
}

// 설문 등록, 설문 수정시 파라미터 유효성 체크
const validation = () => {

	for (var i = 0; i < $(".board_write input[type=text]").length; i++) {
		var el = $(".board_write input[type=text]").eq(i);

		if ($(el).val() == '' && el.attr('title') != '주관식 답변') {
			alert(el.attr('title') + '을 입력해주세요.');
			el.focus();
			return false;
		}
	}

	var strStartDate = $("input[name=qustnrBgnDe]").val();
	var strEndDate = $("input[name=qustnrEndDe]").val();

	if (!Common.validateDate(strStartDate) || strStartDate.length != 8) {
		alert("올바른 날짜 형식이 아닙니다.");
		$("input[name=qustnrBgnDe]").focus();
		return false;
	}

	if (!Common.validateDate(strEndDate)  || strEndDate.length != 8) {
		alert("올바른 날짜 형식이 아닙니다.");
		$("input[name=qustnrEndDe]").focus();
		return false;
	}

	if(strStartDate && strStartDate.length == 8 && strEndDate && strEndDate.length == 8) {
		if(strStartDate > strEndDate) {
			alert("종료일이 시작일보다 빠릅니다.");
			$("input[name=qustnrEndDe]").focus();
			return false;
		}
	}

	if(!$("input[name=srvyTrgt]:checked").val()){
		alert("설문대상을 선택해 주세요.");
		$("input[name=srvyTrgt]").focus();
		return false;
	}

	return true;
}



</script>
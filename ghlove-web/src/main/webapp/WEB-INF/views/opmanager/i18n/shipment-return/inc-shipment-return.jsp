<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>


      		
      		<p class="form-group hidden">
	      		* 주소를 수정 하시면 해당 주소로 등록되어 있는 상품에 즉시 적용됩니다. <br />
	      		* 주소를 변경하시려면 주소를 추가하여 사용하시기 바랍니다.<br />
	      		* 주소를 추가하시려면 주소록에 내용을 기입하시고 등록버튼을 클릭하시면 됩니다.<br />
	      	</p>
	      	<div id="sm-display-area1">
	      		<table class="inner-table th-center">
	      			<colgroup>
                        <col style="width:50px;">
                        <col style="width:200px;">
                        <col style="width:150px;">
                        <col style="width:200px;">
                        <col style="width:150px;">
	      			</colgroup>
					<tr>
						<th>선택</th>    		
						<th>주소명</th>    		
						<th>이름</th>    		
						<th>주소</th>    		
						<th>연락처</th>    		
					</tr>
					<c:forEach items="${list}" var="shipment">
	      				<tr class="listCount">
	      					<td class="text-center">
	      						<input type="radio" name="shipmentReturnId" value="${fn:escapeXml(shipment.shipmentReturnId)}" />
	      						<span class="defaultAddressFlag hide"><c:out value="${shipment.defaultAddressFlag}"/></span>
	      						<span class="sellerId hide"><c:out value="${shipment.sellerId}"/></span>		
	      					</td>
	      					<td class="text-center">
	      						<span class="addressName"><c:out value="${shipment.addressName}"/></span>
	      						<%-- <c:out value="${shipment.defaultAddressFlag == 'Y' ? ' <span class=\"text-info\">(기본)</span>' : ''}	"/> --%>
	      						<span class="text-info ${fn:escapeXml(shipment.defaultAddressFlag == 'Y' ? '' : 'hide')}">(기본)</span>
	      					</td>
	      					<td class="text-center"><span class="name"><c:out value="${shipment.name}"/></span></td>
	      					<td>
	      						<span class="zipcode" style="display:none"><c:out value="${shipment.zipcode}"/></span>
	      						<span class="zipcode1" style="display:none"><c:out value="${shipment.zipcode}"/></span>
	      						<span class="zipcode2" style="display:none"><c:out value="${shipment.zipcode}"/></span>
	      						<span class="address"><c:out value="${shipment.address}"/></span>
	      						<span class="addressDetail"><c:out value="${shipment.addressDetail}"/></span>
	      					</td>
	      					<td class="text-center">
	      						<span class="telephoneNumber"><c:out value="${shipment.telephoneNumber}"/></span>
	      					</td>
	      					
		      		</c:forEach>
	      		</table>
	      		
	      		<c:if test="${empty list}">
	      			<div class="no_content" style="padding: 30px;">
	      				주소를 등록해 주세요.
	      			</div>
	      		</c:if>
	      		
	      		<div class="popup_btns">
	      			<div class="btn_all flex_box juc-sbt">
	      				<div class="btn_right gap-08">
			      			<c:if test="${isPopup}">
			      				<button type="button" class="btn btn-active select-shipment">적용</button>
			      			</c:if>
			      		</div>
			      		
		      			<c:if test="${hasModifyPermission}">
			      			<div class="btn_right gap-08">
			      				<button type="button" class="btn btn-active btn-create" id="btnCreate">신규<c:out value="${op:message('M00088')}"/></button> <!-- 등록 -->
								<button type="button" class="btn btn-active btn-edit hide"><c:out value="${op:message('M00087')}"/></button> <!-- 수정 -->
								<a href="javascript:shipmentDelete();" class="btn btn-default btn-delete hide"><c:out value="${op:message('M00074')}"/></a> <!-- 삭제 -->
			      			</div>
		      			</c:if>
			      	</div>
	      		</div>
	      		
	      	</div>
	      	
	      	<c:if test="${hasModifyPermission}">
		      	<div id="sm-display-area2" style="display: none">
		      		<form id="shipment" action="/opmanager/shipment-return/create" method="post">
		      			<input type="hidden" name="shipmentReturnId" value="0" />
		      			<input type="hidden" name="telephoneNumber" id="telephoneNumber" />
		      			<!-- <input type="hidden" name="zipcode" id="zipcode" /> -->
		      			<input type="hidden" name="sellerId" value="${fn:escapeXml(shipmentReturn.sellerId)}"/>
		      			<input type="hidden" id="existingAddressFlag" value="0"/>
		      			
			      		<table class="board_write_table">
			      			<col style="width: 220px;" />
			      			<col style="" />
			      			
			      			<tr>
			      				<td class="label">주소명</td>
			      				<td>
			      					<div class="flex_box gap-12 item-center">
				      					<input type="text" name="addressName" class="required" title="주소명" />
				      					<div class="checkbox">
					      					<input type="checkbox" name="defaultAddressFlag" value="Y" /> <label>기본주소로 설정</label>
					      					<input type="hidden" name="!defaultAddressFlag" value="N" />	
					      				</div>
				      				</div>
			      				</td>
			      			</tr>
			      			<tr>
			      				<td class="label">상호명</td>
			      				<td>
			      					<div>
			      						<input type="text" name="name" class="required" title="상호명" />
			      					</div>
			      				</td>
			      			</tr>
			      			<tr>
			      				<td class="label">연락처</td>
			      				<td>
			      					<div class="flex_box gap-08 item-center">
				      					<input type="text" name="telephoneNumber1" maxlength="4" class="form-sm text-center required _number" title="연락처 국번" /> -
				      					<input type="text" name="telephoneNumber2" maxlength="4" class="form-sm text-center required _number" title="연락처 가운데 번호" /> -
				      					<input type="text" name="telephoneNumber3" maxlength="4" class="form-sm text-center required _number" title="연락처 마지막 번호" />
				      				</div>
			      				</td>
			      			</tr>
			      			<tr>
			      				<td class="label">주소</td>
			      				<td>
			      					<div class="flex_box gap-08">
			      						<input type="text" id="zipcode" name="zipcode" maxlength="7" class="form-sm text-center required wd-150" readonly="readonly" style="width: 70px;" title="주소"/>
				      					<!-- <input type="text" name="zipcode1" maxlength="3" class="form-sm text-center required _number" title="우편번호 앞자리" /> -
				      					<input type="text" name="zipcode2" maxlength="3" class="form-sm text-center required _number" title="우편번호 뒷자리" />  -->
				      					<a href="#" class="btn btn-default btn-mini" onclick="openDaumAddress()">주소찾기</a>
			      					</div>
			      					<div class="flex_box gap-08">
			      						<input type="text" name="address" class="form-block required" title="주소" readonly="readonly"/>
			      						<input type="text" name="addressDetail" class="form-block required" title="상세주소" />
			      					</div>
			      				</td>
			      			</tr>
			      			
			      		</table>
			      		
						<p class="popup_btns">
							<div class="btn_all btn_center">
								<div class="btn_left gap-08">
									<button type="submit" class="btn btn-active btn-create btn-create1"><c:out value="${op:message('M00088')}"/></button> <!-- 등록 -->
									<button type="submit" class="btn btn-active btn-edit1 hide"><c:out value="${op:message('M00087')}"/></button> <!-- 수정 -->
									<a href="javascript:shipmentDelete();" class="btn btn-active btn-delete hide" id="btnDelete"><c:out value="${op:message('M00074')}"/></a> <!-- 삭제 -->
									<c:if test="${isPopup}">
										<button type="button" class="btn btn-default btn-cancel"><c:out value="${op:message('M00037')}"/></button> <!-- 취소 -->
									</c:if>
								</div>
							</div>
						</p>
			
					</form>
				</div>
			</c:if>

<!-- 다음 주소검색 -->
<daum:address />

<script>
var message = '저장하시겠습니까?';
var shipment = {};
var saveType = "";

$(function() {
	shipmentEvent();
	
	initValidator();
	
	$('input[name=shipmentReturnId]:radio[value='+opener.shipmentReturnId.value+']').click();

	$('#btnCreate').click(function() {
		$('#btnDelete').hide();
	});

});


function initValidator() {
	$('#shipment').validator(function() {
		var telephoneNumber = $('input[name=telephoneNumber1]').val() + '-' + $('input[name=telephoneNumber2]').val() + '-' + $('input[name=telephoneNumber3]').val();
		/* var zipcode = $('input[name=zipcode1]').val() + '-' + $('input[name=zipcode2]').val();
		
		$('#zipcode').val(zipcode); */
		$('#telephoneNumber').val(telephoneNumber);
		
		if (saveType == "edit" && $('#existingAddressFlag').val() == 'Y' && $('input:checkbox[name=defaultAddressFlag]').is(':checked') == false) {
			alert("기본 배송정보는 최소 한 개이상 등록해야합니다.")
			return false;
		}
		if (!confirm(message)) {
			return false;
		}
	});
}


function shipmentEvent() {

	// 출고지 주소 checkbox 클릭 시 
	$('input[name=shipmentReturnId]').on('click', function() {
		$('.btn-edit, .btn-edit1').removeClass('hide');
		$('.btn-delete').removeClass('hide');

		changeShipmentReturnId($(this));

	});
	
	// 선택 버튼 클릭 이벤트.
	$('.select-shipment').on('click', function() {
		var $shipmentReturnId =  $('input[name=shipmentReturnId]');
		
		if (!$shipmentReturnId.is(':checked')) {
			alert('교환/반품 주소를 선택해 주세요.');
			$shipmentReturnId.eq(0).focus();
			return false;
		}
		
		opener.handleShipmentReturnPopupCallback(shipment);
		self.close();
	});
	
	// 등록 버튼.
	$('.btn-create').on('click', function() {
		if ($('#sm-display-area2').css('display') == 'none') {
			$('#sm-display-area1').hide();
			$('#sm-display-area2').show();
			$('.btn-edit1').hide();
			$('#shipment input[type=text]').val('');
			$('#shipment input[type=checkbox]').prop('checked',false);
			saveType = "create";
			return;
		}
		
		message = "저장하시겠습니까?";
		$('#shipment input[name=shipmentReturnId]').val('0');
		$('#shipment').attr('action', '${fn:escapeXml(requestContext.managerUri)}/shipment-return/create');
	});
	
	// 수정 버튼1
	$('.btn-edit').on('click', function() {

		var $radidElement = $('input[name=shipmentReturnId]:checked');
		changeShipmentReturnId($radidElement);

		if ($('#sm-display-area2').css('display') == 'none') {
			$('#sm-display-area1').hide();
			$('#sm-display-area2').show();
			$('.btn-create1').hide();
			$('.btn-delete').show();
			saveType = "edit";
			return;
		}
	});


	// 수정 버튼2
	$('.btn-edit1').on('click', function() {
		message = "교환/반품 정보를 변경하면 해당 교환/반품으로 설정되어있는 상품에 즉시 적용됩니다.";
		$('#shipment input[name=shipmentReturnId]').val(shipment.shipmentReturnId);
		$('#shipment').attr('action', '${fn:escapeXml(requestContext.managerUri)}/shipment-return/edit');
	});

	// 취소버튼 
	$('.btn-cancel').on('click', function() {
		$('#sm-display-area2').hide();
		$('#sm-display-area1').show();
		$('.btn-create1').show();
		$('.btn-edit1').show();
	});
	
}

function shipmentDelete() {
	if ($('#existingAddressFlag').val() == 'Y') {
		alert('기본정보로 설정한 배송정보는 지울수 없습니다.');
		return;
	}
	if ($('.listCount').length == 1) {
		alert('배송정보는 최소 한 개 이상 등록해야합니다.');
		return;
	}
	if (confirm("배송정보를 삭제하시겠습니까?")) {
		$('#shipment').attr('action', '${fn:escapeXml(requestContext.managerUri)}/shipment-return/delete');
		$('#shipment').submit();
	}
}

function closePopup() {
	self.close();
}

// 반품/교환 주소 라디오 버튼 클릭시 주소 값 세팅
function changeShipmentReturnId($radidElement){

	var $shipment = $radidElement.closest('tr');
	var data = ['addressName', 'name', 'telephoneNumber', 'zipcode', 'address', 'addressDetail', 'defaultAddressFlag', 'sellerId'];

	shipment = {};
	shipment.shipmentReturnId = $radidElement.val();

	for (var i = 0; i < data.length; i++) {
		var value = $shipment.find('span.' + data[i]).text();
		shipment[data[i]] = value;
	}

	/* shipment.zipcode1 = shipment.zipcode.split('-')[0];
    shipment.zipcode2 = shipment.zipcode.split('-')[1]; */

	shipment.telephoneNumber1 = shipment.telephoneNumber.split('-')[0];
	shipment.telephoneNumber2 = shipment.telephoneNumber.split('-')[1];
	shipment.telephoneNumber3 = shipment.telephoneNumber.split('-')[2];

	// 기본주소.
	if (shipment.defaultAddressFlag == 'Y') {
		$('#shipment input[name=defaultAddressFlag]').prop('checked', true);
	} else {
		$('#shipment input[name=defaultAddressFlag]').prop('checked', false);
	}

	for(var key in shipment) {
		if (shipment.hasOwnProperty(key)) {
			if (key == 'defaultAddressFlag') {
				$('#shipment #existingAddressFlag').val(shipment[key]);
			} else {
				$('#shipment input[name=' + key +']').val(shipment[key]);
			}
		}
	}
}
</script>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>

    <div class="popup_wrap" id="dataArea">
        <div id="pop_header">
            <h1 class="popup_title"><c:out value="${op:message('정산 확인')}"/></h1><!-- 키워드 수정 -->
			<a href="javascript:self.close();" class="btn_close"><img src="/content/opmanager/images/btn/btn_close.png" alt="${op:message('M00569')}"></a><!-- 닫기 -->
		</div>
		<form:form action="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/upload-file" method="post" enctype="multipart/form-data" id="formData">
        <div class="popup_contents" style="padding-bottom:30px;">
            <div class="board_write">

                <c:if test="${requestContext.sellerPage}">
	                <table class="board_write_table" summary="" id="uploadArea">
	                    <colgroup>
	                        <col style="width:150px;">
	                        <col />
	                    </colgroup>
	                    <tbody>
	                    	<tr>
	                    		<td>
			                    	<input type="hidden" name="remittanceId" id="remittanceId" value="">
			                    	<input type="hidden" name="id" id="id" value=""></td>
	                    		<td></td>
	                    	</tr>
	                        <tr id="uploadArea">
	                            <td class="label"><c:out value="${op:message('정산자료 첨부하기')}"/></td><!-- 기간 -->
	                            <td>
	                            	<div style="display: none;">
				                    	<input id="addFile" type="file" name="addFile" class="full input_file" title="${op:message('M01699')}"
				                    		multiple="multiple" onchange="javascript:changeAddFile(this);">
				                    </div>
	                            	<div class="flex_box item-center">
				                    	<button type="button" onclick="javascript:addFileClick();">파일 선택</button>
				                    </div>
	                                <div id="displayAddFile">

	                                </div>
	                                <div id="addFileNone">
	                                    업로드할 파일이 없습니다.
	                                </div>
	                            </td>
	                        </tr>
	                        <tr id="downloadArea">
	                            <td class="label"><c:out value="${op:message('정산첨부자료')}"/></td><!-- 키워드 -->
	                            <td>
	                                <div id="displayUploadedFile">

	                                </div>
	                                <div id="uploadedFileNone">
	                                    업로드된 파일이 없습니다.
	                                </div>
	                            </td>
	                        </tr>
	                    	<tr>
	                    		<td></td>
	                    		<td></td>
	                    	</tr>
	                    </tbody>
	                </table>
                </c:if>

                <c:if test="${!requestContext.sellerPage}">
	                <table class="board_write_table" summary="" id="downloadArea">
	                    <colgroup>
	                        <col style="width:150px;">
	                        <col />
	                        <col style="width:150px;">
	                    </colgroup>
	                    <tbody id="downloadDisplay">
	                    	<tr>
	                    		<td>
			                    	<input type="hidden" name="remittanceId" id="remittanceId" value="">
			                    	<input type="hidden" name="id" id="id" value=""></td>
	                    		<td></td>
	                    	</tr>
	                    	<tr>
	                    		<td class="label">등록일</td>
	                    		<td class="label">첨부파일</td>
	                    		<td class="label">일괄 다운로드</td>
	                    	</tr>
	                    </tbody>
	                </table>
                </c:if>
            </div>

            <div class="board_write">
            	<ul class="list-bullet point" style="padding-top:10px;">
		            <li>
		                세금계산서(과세/비과세), 배송비 정산 내역서 등을 첨부해주세요.
		            </li>
		            <!-- <li>
		                배송비 정산 내역서
		            </li>
		            <li>
		                기타 정산에 필요한 파일을 첨부해주세요.
		            </li> -->
		        </ul>
            </div>

            <p class="popup_btns">
            	<c:if test="${requestContext.sellerPage}">
                	<button type="button" class="btn btn-active" onclick="javascript:save(event);" id="submit">${op:message('정산확인')}</button><!-- 저장 -->
                </c:if>
            	<%-- <c:if test="${!requestContext.sellerPage}">
                	<button type="button" class="btn btn-active" onclick="javascript:allDownload();">${op:message('일괄다운로드')}</button><!-- 저장 -->
                </c:if> --%>
                <%-- <button type="button" class="btn btn-default" onclick="javascript:self.close();">${op:message('M00569')}</button> --%><!-- 닫기 -->
            </p>
        </div>
        </form:form>
    </div>

<script type="text/javascript">

let confirmRemittanceId;

/* document.getElementById("dataArea").load = function() {
	$("#shadow").remove();
	$("#displayAddFile").hide();
	$("#displayUploadedFile").hide();
	$("#uploadedFileNone").hide();

	try {
		window.opener.getRemittanceId();		// 화면 오픈 후 부모화면 함수 호출
	} catch (e) {
		console.log(e);
		alert('${op:message("정산확정 내역 화면에서 진행해주세요.")}');		// 정산확정 내역 상세화면에서 진행해주세요.
		// self.close();
	}
} */

$(function() {
	$("#shadow").remove();
	$("#displayAddFile").hide();
	$("#displayUploadedFile").hide();
	$("#uploadedFileNone").hide();

	try {
		window.opener.getRemittanceId();		// 화면 오픈 후 부모화면 함수 호출
	} catch (e) {
		alert('${op:message("정산확정 내역 화면에서 진행해주세요.")}');		// 정산확정 내역 상세화면에서 진행해주세요.
		// self.close();
	}
});

// 저장버튼 클릭시
function save(event) {
	if (!document.getElementById("remittanceId").value) {
		event.preventDefault();
		alert(Message.get("업로드가 불가능합니다."));
		return;
	}
	let files = document.getElementById("addFile").files;
	if (!files || files.length == 0) {
		event.preventDefault();
		//alert(Message.get("업로드할 파일이 없습니다."));
		if (confirm(Message.get("업로드할 파일이 없습니다. 정산을 진행하시겠습니까?"))) {
			remittanceConfirm();
		}
	} else if (confirm(Message.get("첨부하신 정산자료로 정산확인 처리를 하시겠습니까?"))) {
		uploadFile();
	}
}

// 부모화면에서 넘겨준 데이터 요소에 세팅
function sendPopupData(remittanceId) {		// 부모화면에서 호출하여 데이터 세팅
	//confirmRemittanceId = remittanceId;
	document.getElementById("remittanceId").value = remittanceId;
	document.getElementById("id").value = remittanceId;
	confirmRemittanceId = remittanceId;
	// 화면 호출하자마자 통신할 경우 로딩화면 해제되어 0.1초 후 통신 수행
	setTimeout(function() {
		$.ajax ({
			url	: "${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/file-list",
			type	: "POST",
			//timeout : 3000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
			data  : JSON.stringify({"remittanceId" : remittanceId}), // 요청 시 포함되어질 데이터
			processData : true,
			contentType : "application/json",
			dataType    : "json",
			success : function(resp, status, xhr) {
				Common.responseHandler(resp, function(response){
					<%--<c:if test="${!requestContext.sellerPage}">--%>
					if (response.data) {
						if (response.data.remittanceId == remittanceId) {
		    				let data = resp.data.remittanceFileList;

							setDisplay(remittanceId, data, response.data.remittanceStatusCode);
							return;
						}
					}
					alert(Message.get("실패했습니다."));		// 실패했습니다.
					<%--</c:if>--%>
				});
			},
			error	: function(xhr, status, error) {
				console.log(error);
				alert(Message.get("실패했습니다."));		// 실패했습니다.
			}
		});
	}, 100);

}

// 입력 제한
function inputCheck(inputElement) {
	let beforePosition = inputElement.selectionStart - 1;		// 키 입력 후 위치 - 1 => 입력 전 위치
	let beforeLength = inputElement.value.length;			// 입력 후 길이
//	inputElement.value = inputElement.value.replace(/[^0-9.]/g, '');		// 숫자만
	inputElement.value = inputElement.value.replace(/[^a-zA-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|0-9|,|]/g, '');		// 알파벳 한글 숫자 , 만 입력 가능
	let afterLength = inputElement.value.length;			// 입력 전 길이
	if (beforeLength > afterLength) {		// 다르면 가능한 입력 값이 아님
		inputElement.setSelectionRange(beforePosition, beforePosition);		// 기존 위치로 커서 이동
	}
}


function changeAddFile(inputElement) {
	let files = inputElement.files;
	let displayAddFile = document.getElementById('displayAddFile');		// 첨부파일 목록 영역
	displayAddFile.replaceChildren();		// 하위 태그 초기화
	displayAddFile.removeAttribute("style");
	if (files && files.length > 0) {
		let fileSize = 0;
		let fileCnt = files.length;
		for (let idx = 0 ; idx < fileCnt ; idx++) {
			let file = files[idx];
			let imgElement = document.createElement('img');		// img 요소 추가
			let divElement = document.createElement('span');		// div 요소 추가
			displayAddFile.appendChild(divElement);					// displayAddFile 요소 하위에 div 요소 할당
			divElement.innerHTML = file.name;
			divElement.appendChild(imgElement);
			imgElement.setAttribute('id', 'file' + idx);	// img 요소에 아이디 부여
			imgElement.setAttribute('alt', "close");
			imgElement.setAttribute('onclick', 'javascript:removeFileList("' + idx + '")');
			imgElement.setAttribute('src', "/content/images/btn/file_close.gif");
			imgElement.setAttribute('style', "margin-left:2px;cursor:pointer;");

			divElement.setAttribute("style", "margin-right:10px;")
		}
		$("#addFileNone").hide();
	} else {
		displayAddFile.setAttribute("style", "display:none;");
		$("#addFileNone").show();
	}
}

// input file 숨겨서 별도 버튼으로 동작하도록 함
function addFileClick() {
	document.getElementById("addFile").click();
}

//첨부파일 삭제
function removeFileList(i) {
	let filesElement = document.getElementById('addFile');
	let files = filesElement.files;
	if (files && files.length > 0) {
		let fileCnt = files.length;
		const dataTransfer = new DataTransfer();		// 폼 객체 내 파일 정보 수정시 처리용 객체
		for (let idx = 0 ; idx < fileCnt ; idx++) {
			if (i != idx) {
				dataTransfer.items.add(files[idx]);
			}
		}
		filesElement.files = dataTransfer.files;
		//files = dataTransfer.files;
		changeAddFile(filesElement);		// 목록 다시그리기
	} else {
		let displayAddFile = document.getElementById('displayAddFile');		// 첨부파일 목록 영역
		displayAddFile.replaceChildren();		// 하위 태그 초기화
		displayAddFile.setAttribute("style", "display:none;");
	}
}

function uploadFile() {
	let form = $('#formData')[0];
    let formData = new FormData(form);

	$.ajax ({
		url	: "${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/upload-file",
		type	: "POST",
        enctype: 'multipart/form-data',
		//timeout : 10000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
		data  : formData, // 요청 시 포함되어질 데이터
		processData : false,
		contentType : false,
		cache: false,
		success : function(resp, status, xhr) {
			Common.responseHandler(resp, function(response){
				alert(Message.get("저장되었습니다."));		// 실패했습니다.
				//self.close();

				form.reset();

				form["remittanceId"].value = confirmRemittanceId;
				form["id"].value = confirmRemittanceId;

				let data = resp.data.remittanceFileList;
				setDisplay(resp.data.remittanceId, data, response.data.remittanceStatusCode);
				return;

			});
		},
		error	: function(xhr, status, error) {
			console.log(error);
			alert(Message.get("실패했습니다."));		// 실패했습니다.
		}
	});
}


function removeUploadedFile(remittanceId, fileSeq) {
	if (confirm(Message.get("삭제하시겠습니까?"))) {
		$.ajax ({
			url	: "${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/delete-file/" + remittanceId + "/" + fileSeq,
			type	: "GET",
			//timeout : 3000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
			processData : true,
			contentType : "application/json",
			dataType    : "json",
			success : function(resp, status, xhr) {
				Common.responseHandler(resp, function(response){
					if (response.data) {
						if (response.data.remittanceId == remittanceId) {
		    				let data = resp.data.remittanceFileList;
							setDisplay(remittanceId, data, response.data.remittanceStatusCode);
							return;
						}
					}
					alert(Message.get("실패했습니다."));		// 실패했습니다.
				});
			},
			error	: function(xhr, status, error) {
				console.log(error);
				alert(Message.get("실패했습니다."));		// 실패했습니다.
			}
		});
	}
}

function setDisplay(remittanceId, data, remittanceStatusCode) {
	$("#displayAddFile").empty();
	$("#displayAddFile").hide();
	$("#addFileNone").show();
	$("#displayUploadedFile").empty();
	$('#uploadedFileNone').hide();

	$('#downloadDisplay').empty();
	let downloadDisplayChild = '';
	downloadDisplayChild += '<tr>';
	downloadDisplayChild += '	<td class="label">등록일</td>';
	downloadDisplayChild += '	<td class="label">첨부파일</td>';
	downloadDisplayChild += '	<td class="label">일괄 다운로드</td>';
	downloadDisplayChild += '</tr>';

	if (!${fn:escapeXml(requestContext.sellerPage)}) {
		$("#uploadArea").hide();
	}

	if (data && data.length > 0) {
		$("#displayUploadedFile").show();

		let dataLength = data.length;

        let uploadDateList = [];
        let uploadDateLength = 0;
        for (let i = 0; i < dataLength; i++) {
            let div = "";
            //div += '<div class="file_camera">';
            div += '	<a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/file/'
					+ remittanceId + '/' + data[i].fileSeq + '">' + data[i].orgFileName + '</a>';

			<c:if test="${requestContext.sellerPage}">
			div += '	<img src="/content/images/btn/file_close.gif" onclick="javascript:removeUploadedFile(\'' + remittanceId + '\', \'' + data[i].fileSeq + '\')"'
        			+ ' style="cursor:pointer;" />';
        	</c:if>

            //div += '</div>';
            div += '&nbsp&nbsp';
            $('#displayUploadedFile').append(div);

            uploadDateLength = uploadDateList.length;
            let exist = false;
            dateCheck: for(let j = 0 ; j < uploadDateLength ; j++) {
            	if (data[i].uploadDate == uploadDateList[j]) {
            		exist = true;
            		break dateCheck;
            	}
            }

            if (!exist) {
            	uploadDateList.push(data[i].uploadDate);
            }
        }

		uploadDateLength = uploadDateList.length;
        if (uploadDateLength > 0) {
        	for(let i = 0 ; i < uploadDateLength ; i++) {
                downloadDisplayChild += '<tr>';

                downloadDisplayChild += '	<th>' + dateFormat(uploadDateList[i], '-') + '</th>';
                downloadDisplayChild += '	<th style="height:40px;">';
				for(let j = 0 ; j < dataLength ; j++) {
					if (data[j].uploadDate == uploadDateList[i]) {
						//downloadDisplayChild += '<div>';
						downloadDisplayChild += '<a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/file/'
								+ remittanceId + '/' + data[j].fileSeq + '">' + data[j].orgFileName + '</a> &nbsp';
						//downloadDisplayChild += '</div>';
					}
				}
                downloadDisplayChild += '	</th>';
                downloadDisplayChild += '	<th><a href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/allFileDownload/' + confirmRemittanceId + '/' + uploadDateList[i] + '">일괄 다운로드</a></th>';

                downloadDisplayChild += '</tr>';
        	}
        }

		if (${fn:escapeXml(requestContext.sellerPage)}) {
			$("#downloadArea").show();
		}
	} else {
		$("#displayUploadedFile").hide();
		$('#uploadedFileNone').show();
		if (${fn:escapeXml(requestContext.sellerPage)} && remittanceStatusCode == '3') {
			$("#downloadArea").hide();
		} else {
			downloadDisplayChild += '<tr>';
			downloadDisplayChild += '	<th colspan="3" style="height: 50px;"> 첨부파일이 없습니다.</th>';
			downloadDisplayChild += '</tr>';
		}
	}
	$('#downloadDisplay').append(downloadDisplayChild);

	<c:if test="${requestContext.sellerPage}">
	$('#downloadDisplay').hide();
	if (remittanceStatusCode == "3" || remittanceStatusCode == "4") {
		<%--if (remittanceStatusCode == "3") {
				document.getElementById("uploadArea").removeAttribute("style");
				document.getElementById("submit").removeAttribute("disabled");
				document.getElementById("submit").removeAttribute("style");
			} else {
				document.getElementById("uploadArea").setAttribute("style", "display:none;");
				document.getElementById("submit").setAttribute("disabled", "disabled");
				document.getElementById("submit").setAttribute("style", "display:none;");
			}--%>
			let btnText = "";
			if (remittanceStatusCode == "3") {
				btnText = Message.get("정산확인");
			} else {
				btnText = Message.get("보내기");
			}
			document.getElementById("submit").innerHTML = btnText;
	} else {
			document.getElementById("uploadArea").setAttribute("style", "display:none;");
			document.getElementById("submit").setAttribute("disabled", "disabled");
			document.getElementById("submit").setAttribute("style", "display:none;");
	}
	</c:if>

	window.opener.postMessage({fnName: 'btnChg'}, '*');
}

function dateFormat(dateStr, prefix) {
	try {
		let year = dateStr.substring(0, 4);
		let month = dateStr.substring(4, 6);
		let day = dateStr.substring(6, 8);
		return year + prefix + month + prefix + day;
	} catch (e) {
		console.log(e);
		return dateStr;
	}
}


function allDownload() {
	location.href="${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/allFileDownload/" + confirmRemittanceId + "/";
}

function remittanceConfirm() {
	let form = $('#formData')[0];
    let formData = {remittanceId : form["remittanceId"].value, id : [form["id"].value]};

	$.ajax ({
		url	: "${requestContext.sellerPage ? '/seller' : '/opmanager'}/remittance/confirm/list/check",
		type	: "POST",
		//timeout : 10000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
		data  : JSON.stringify(formData), // 요청 시 포함되어질 데이터
		processData : true,
		contentType : "application/json",
		dataType    : "json",
		success : function(resp, status, xhr) {
			Common.responseHandler(resp, function(response){
				alert(Message.get("저장되었습니다."));
				//self.close();

				/*form.reset();

				form["remittanceId"].value = confirmRemittanceId;
				form["id"].value = confirmRemittanceId;

				let data = resp.data.remittanceFileList;
				setDisplay(resp.data.remittanceId, data, response.data.remittanceStatusCode);
				return;*/
				window.opener.postMessage({fnName: 'btnChg'}, '*');
			});
		},
		error	: function(xhr, status, error) {
			console.log(error);
			alert(Message.get("실패했습니다."));		// 실패했습니다.
		}
	});
}


</script>

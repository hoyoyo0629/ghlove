<%@ page language="java"	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="page"	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op"		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#"></a> &gt; <a href="#"></a> &gt; <a href="#" class="on"></a>
</div>

<h3><span></span></h3>

<div class="board_write">
	<table class="board_write_table" summary="">
		<colgroup>
			<col style="width:220px;">
			<col>
			<col style="width:220px;">
			<col>
		</colgroup>
		<tbody>
			<c:choose>
				<c:when test="${adminRole eq 'SYS'}">
					<tr>
						<td class="label">지자체코드</td>
						<td colspan="3">
							<div id="locgovCode"></div>
						</td>
					</tr>
					<tr>
						<td class="label"><span class="required_mark">*</span>지자체명</td>
						<td colspan="3">
							<div class="flex_box gap-08">
								<select id="upperLocgov" onchange="changeUpperLocgov()">
									<option value="">- 시도 선택 -</option>
									<c:forEach items="${upperLocgovCodeList}" var="code" varStatus="status">
										<option value="${fn:escapeXml(code.id)}"><c:out value="${fn:escapeXml(code.label)}"/></option>
									</c:forEach>
								</select>
								<select id="locgov" onchange="changeLocgov()">
									<option value="">- 시군구 -</option>
								</select>
							</div>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="label">지자체코드</td>
						<td colspan="3">
							<div>
								<c:out value="${userLocgovCodeDetails.id}"/>
								<input type="hidden" id="upperLocgov" value="${fn:substring(userLocgovCodeDetails.id, 0, 2)}000" />
								<input type="hidden" id="locgov" value="${fn:escapeXml(userLocgovCodeDetails.id)}" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">지자체명</td>
						<td colspan="3">
							<div><c:out value="${fn:split(userLocgovCodeDetails.label, ',')[0]}"/>&nbsp;<c:out value="${fn:split(userLocgovCodeDetails.label, ',')[1]}"/></div>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			<tr>
				<td class="label"><span class="required_mark">*</span>사업자 등록번호</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<input id="bizrno1" title="" class="input_txt required _filter wd-100 _number" type="text" maxlength="3">
						<span class="wave">-</span>
						<input id="bizrno2" title="" class="input_txt required _filter wd-100 _number" type="text" maxlength="2">
						<span class="wave">-</span>
						<input id="bizrno3" title="" class="input_txt required _filter wd-100 _number" type="text" maxlength="5">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>담당자명</td>
				<td colspan="3">
					<div>
						<input id="chargerNm" title="담당자명" class="input_txt required _filter wd-150" type="text">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>담당자 연락처</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<select id="chargerCttpc1" title="-선택-" class="wd-100">
							<option value="">-선택-</option>
							<c:forEach items="${phoneCodeList}" var="code" varStatus="status">
								<option value="${fn:escapeXml(code.id)}"><c:out value="${fn:escapeXml(code.label)}"/></option>
							</c:forEach>
							<c:forEach items="${telCodeList}" var="code" varStatus="status">
								<option value="${fn:escapeXml(code.id)}"><c:out value="${fn:escapeXml(code.label)}"/></option>
							</c:forEach>
						</select>
						<span class="wave">-</span>
						<input id="chargerCttpc2" title="" class="input_txt required _filter wd-100 _number" type="text" maxlength="4">
						<span class="wave">-</span>
						<input id="chargerCttpc3" title="" class="input_txt required _filter wd-100 _number" type="text" maxlength="4">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>담당자 이메일</td>
				<td colspan="3">
					<div>
						<input id="chargerEmail" title="담당자 이메일" class="input_txt required _filter half" type="text">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>지자체 홈페이지</td>
				<td colspan="3">
					<div>
						<input id="locgovHmpg" title="지자체 홈페이지" class="input_txt required _filter half" type="text">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>지자체 소개</td>
				<td colspan="3">
					<div>
						<span class="placeholder_wrap">
							<span class="placeholder"></span>
							<textarea id="locgovIntrcnCn" cols="30" rows="10" class="required _filter" title="지자체 소개"></textarea>
						</span>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label" rowspan="2"><span class="required_mark">*</span>지자체 주소</td>
				<td colspan="3">
					<div class="flex_box">
						<input id="locgovZip" name="" title="지번" class="input_txt required _filter wd-150" type="text" readonly>
						<button type="button" class="btn btn-default btn-mini" onclick="jusoPopup();">주소찾기</button>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="flex_box gap-08">
						<input id="bassAdres" title="" class="input_txt required _filter half" type="text" readonly>
						<input id="dtlAdres" title="" class="input_txt required _filter half" type="text">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>예산</td>
				<td colspan="3">
					<div>
						<input id="locgovBudgetAmt" title="예산" class="input_txt required _filter wd-150 _number_comma form-amount" type="text" value=""> 백만원
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>인구수</td>
				<td colspan="3">
					<div>
						<input id="locgovPopltnCo" title="인구수" class="input_txt required _filter wd-150 _number_comma form-amount" type="text" value=""> 명
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>포인트 지급률</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<input id="pointRate" title="" class="input_txt required _filter wd-150 _number_float form-amount" type="text">
						<span class="wave">%</span>
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>부서명</td>
				<td>
					<div class="flex_box gap-08 item-center">
						<input id="chargerPsitnDept" title="" class="input_txt required _filter wd-150" type="text">
					</div>
				</td>
				<td class="label"><span class="required_mark">*</span>부서코드</td>
				<td>
					<div class="flex_box gap-08 item-center">
						<input id="processDeptCode" title="" class="input_txt required _filter wd-150" type="text">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>행정표준기관코드</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<input id="administInsttCode" title="" class="input_txt required _filter wd-150 _number" type="text" maxlength="7">
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>회계구분</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<select id="fisSp" name="fisSp" title="회계구분" class="wd-150">
							<option value="">- 선택 -</option>
							<c:forEach items="${locFisSpList}" var="item" varStatus="i">
								<option value="${fn:escapeXml(item.id)}" <c:if test="${item.id eq details.fisSp}">selected</c:if> ><c:out value="${item.label}"/></option>
							</c:forEach>
						</select>
					</div>
				</td>
			</tr>
			<!--<c:forEach items="${honorCodeList}" var="item" varStatus="i">
				<c:set var="amt" value="stdr${i.count}levelAmt" />
				<c:choose>
					<c:when test="${i.index == 0}">
						<tr>
							<td class="label" rowspan="${honorCodeList.size()}"><span class="required_mark"></span>명예회원선정기준</td>
							<td colspan="3">
								<div class="flex_box gap-08 item-center">
									<span> <c:out value="${item.label}" /></span>
									<input type="hidden" id="${fn:escapeXml(amt)}Code" value="${fn:escapeXml(item.id)}" />
									<input id="${fn:escapeXml(amt)}" title="" class="input_txt required _filter wd-150 _number_comma form-amount honor-amt" type="text"  /> 원 이상
								</div>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="3">
								<div class="flex_box gap-08 item-center">
									<span> <c:out value="${item.label}" /></span>
									<input type="hidden" id="${fn:escapeXml(amt)}Code" value="${fn:escapeXml(item.id)}" />
									<input id="${fn:escapeXml(amt)}" title="" class="input_txt required _filter wd-150 _number_comma form-amount honor-amt" type="text" /> 원 이상
								</div>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</c:forEach>-->
			<tr>
				<td class="label"><span class="required_mark">*</span>직인명</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<input id="offcsNm" title="지자체 홈페이지" class="input_txt required _filter half" type="text" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="label"><span class="required_mark">*</span>파일업로드</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<input id="offcsFile" title="" class="input_txt required _filter wd-150 _number" type="file" accept="image/*" onchange="previewOffcs(this)" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="label">직인이미지</td>
				<td colspan="3">
					<div class="flex_box gap-08 item-center">
						<img id="preview" src="/content/images/common/no-image-gray.gif" style="max-width : 300px" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="label" rowspan="2">답례품 배경이미지</td>
				<td colspan="3">
					<div style="display: flex; align-items: center;">
						<div style="min-width:20%; float: left;text-align: center;">
							PC<br>
							권장 사이즈 : 1920 X 220<br>
							<input id="addPcFile" type="file" name="addPcFile" class="full input_file" title="${op:message('M01699')}"
				                    		onchange="javascript:changePcImgFile(event);" style="display: none;" accept="image/*">
							<button onclick="javascript:addPcImgClick();">파일 선택</button>
						</div>
						<div>
							<div class="flex_box gap-08 item-center" style="width:60%; float: left;margin-bottom: 10px;">
								<div style="min-width: 100px;">업로드할 이미지</div>
								<img id="uploadPcImg" src="/content/images/common/no-image-gray.gif" style="width : 960px; height: 110px;" />
							</div>
							<div class="flex_box gap-08 item-center" style="width:60%; float: left;">
								<div style="min-width: 100px;">업로드된 이미지</div>
								<img id="uploadedPcImg" src="${fn:escapeXml(details.pcFilePath)}" style="width : 960px; height: 110px;"
									onerror="this.src='/content/images/common/no-image-gray.gif';" />
							</div>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<div style="display: flex; align-items: center;">
						<div style="min-width:20%; float: left;text-align: center;">
							MOBILE<br>
							권장 사이즈 : 768 X 180<br>
							<input id="addMbFile" type="file" name="addMbFile" class="full input_file" title="${op:message('M01699')}"
				                    		onchange="javascript:changeMbImgFile(event);" style="display: none;" accept="image/*">
							<button onclick="javascript:addMbImgClick();">파일 선택</button>
						</div>
						<div>
							<div class="flex_box gap-08 item-center" style="width:60%; float: left;margin-bottom: 10px;">
								<div style="min-width: 100px;">업로드할 이미지</div>
								<img id="uploadMbImg" src="/content/images/common/no-image-gray.gif" style="width : 384px; height: 90px;" />
							</div>
							<div class="flex_box gap-08 item-center" style="width:60%; float: left;">
								<div style="min-width: 100px;">업로드된 이미지</div>
								<img id="uploadedMbImg" src="${fn:escapeXml(details.mbFilePath)}" style="width : 384px; height: 90px;"
									onerror="this.src='/content/images/common/no-image-gray.gif';"/>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="btn_all btn_center">
	<div class="flex_box gap-08">
		<button type="button" class="btn btn-dark-gray btn-small" onclick="locgovCreate();">등록</button>
		<c:choose>
			<c:when test="${adminRole eq 'SYS'}">
				<button type="button" class="btn btn-defualt btn-small" onclick="moveListPage();">취소</button>
			</c:when>
			<c:otherwise>
				<button type="button" class="btn btn-defualt btn-small" onclick="clearLocgov();">초기화</button>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<!-- 목록 검색 조건 -->
<input type="hidden" id="listSearchParam" />
<!--// 목록 검색 조건  -->

<script>
	$(function() {

		// 관리자&행안부인 경우 타이틀, 네비게이션 수정
		if("${fn:escapeXml(adminRole)}" == "SYS") {
			$(".contents .contents_inner").find("h3 span").text("지자체등록");
			$(".contents .contents_inner").find("div.location a").removeClass("on");
			$(".contents .contents_inner").find("div.location").append('> <a href="/opmanager/user/locgov/create" class="on">등록</a>');
		}

		// 목록 검색 조건 저장
		listSearchParamSave();

	});

	function previewOffcs(e) {
		var imageReg = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
		var maxSize = 500 * 1024;	//500kb
		var isSucc = true;

		if (e.files && e.files[0]) {

			if (isSucc && !e.files[0].name.match(imageReg)) {
				alert("이미지 파일만 등록 가능합니다.");
				$(e).focus();
				isSucc = false;
			}

			if (isSucc && e.files[0].size > maxSize) {
				alert("이미지 파일은 500kb이하로 등록 가능합니다.");
				$(e).focus();
				isSucc = false;
			}

			if (!isSucc) {
				if (Browser.getName() == 'ie') {
					$("#offcsFile").replaceWith($("#offcsFile").clone(true));
				} else {
					$("#offcsFile").val("");
				}
				document.getElementById('preview').src = "/content/images/common/no-image-gray.gif";
				return isSucc;
			}


			var reader = new FileReader();
			reader.onload = function(e) {
			document.getElementById('preview').src = e.target.result;
			};
			reader.readAsDataURL(e.files[0]);
		} else {
			document.getElementById('preview').src = "/content/images/common/no-image-gray.gif";
		}
	}

	/**
	 *	함 수 명 : listSearchParamSave
	 *	기	능  : 목록 검색 조건 저장
	 */
	function listSearchParamSave() {

		// 목록 검색 조건 저장
		$("#listSearchParam").val(location.search);

		// 검색 조건 삭제
		history.replaceState({}, null, location.pathname);
	}

	/**
	 *	함 수 명 : jusoPopup
	 *	기	능  : 주소팝업 호출
	 */
	function jusoPopup() {
		Common.popup("/opmanager/juso-popup", "/opmanager/juso-popup", 600, 420 ,1, 0, 0);
	}

	/**
	 *	함 수 명 : jusoCallBack
	 *	기	능  : 주소팝업 콜백 함수
	 */
	function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2,engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn
			, detBdNmList, bdNm, bdKdcd, siNm, sggNm, emdNm, liNm, rn, udrtYn, buldMnnm, buldSlno, mtYn, lnbrMnnm, lnbrSlno, emdNo){
		$("#locgovZip").val(zipNo);
		$("#bassAdres").val(roadAddrPart1+" "+roadAddrPart2);
		$("#dtlAdres").val(addrDetail);
	}

	/**
	 *	함 수 명 : locgovCreate
	 *	기	능  : 지자체 등록
	 */
	function locgovCreate() {
		if(!validator() || !confirm("정보를 등록 하시겠습니까?")) return false;

		$.ajax({
			url : '/opmanager/user/locgov/create',
			type : 'post',
			type : 'post',
			data: setParam(),
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			cache: false,
			success : function(response) {
			if(response.isSuccess && response.data) {
				if(response.data == "SUCC") {
					alert("등록되었습니다.");

					// 시스템&행안부인 경우 목록
					if("${fn:escapeXml(adminRole)}" == "SYS") {
						$("#listSearchParam").val("");
						moveListPage();

					// 지자체인 경우 수정 페이지로 이동
					} else {
						location.href = "/opmanager/user/locgov/edit/"+$("#locgov").val();
					}
				} else if(response.data == "DUP") {
					alert("이미 등록된 지자체 정보가 존재합니다.");
				} else {
					alert("오류가 발생했습니다.");
				}
			}
		}
		})

	}


	/**
	 *	함 수 명 : validator
	 *	기	능  : 유효성 체크
	 */
	function validator() {
		if($("#upperLocgov").val() == "") {
			alert("지자체(시도)를 선택해 주세요.");
			$("#upperLocgov").focus();
			return false;
		}

		if($("#locgov").val() == "") {
			alert("지자체(시군구)를 선택해 주세요.");
			$("#locgov").focus();
			return false;
		}

		if($.trim($("#bizrno1").val()) == "") {
			alert("사업자 등록번호를 입력해 주세요.");
			$("#bizrno1").focus();
			return false;
		}

		if($.trim($("#bizrno2").val()) == "") {
			alert("사업자 등록번호를 입력해 주세요.");
			$("#bizrno2").focus();
			return false;
		}

		if($.trim($("#bizrno3").val()) == "") {
			alert("사업자 등록번호를 입력해 주세요.");
			$("#bizrno3").focus();
			return false;
		}

		if($.trim($("#chargerNm").val()) == "") {
			alert("담당자명을 입력해 주세요.");
			$("#chargerNm").focus();
			return false;
		}

		if($.trim($("#chargerEmail").val()) == "") {
			alert("담당자 이메일을 입력해 주세요.");
			$("#chargerEmail").focus();
			return false;
		}

		if($("#chargerCttpc1").val() == "") {
			alert("담당자 연락처를 선택해 주세요.");
			$("#chargerCttpc1").focus();
			return false;
		}

		if($("#chargerCttpc2").val() == "") {
			alert("담당자 연락처를 선택해 주세요.");
			$("#chargerCttpc2").focus();
			return false;
		}

		if($("#chargerCttpc3").val() == "") {
			alert("담당자 연락처를 선택해 주세요.");
			$("#chargerCttpc3").focus();
			return false;
		}

		if($.trim($("#locgovHmpg").val()) == "") {
			alert("지자체 홈페이지를 입력해 주세요.");
			$("#locgovHmpg").focus();
			return false;
		}

		if($.trim($("#locgovIntrcnCn").val()) == "") {
			alert("지자체 소개를 입력해 주세요.");
			$("#locgovIntrcnCn").focus();
			return false;
		}

		if($.trim($("#locgovZip").val()) == "") {
			alert("지자체 주소(우편번호)를 입력해 주세요.");
			$("#locgovZip").focus();
			return false;
		}

		if($.trim($("#bassAdres").val()) == "") {
			alert("지자체 주소를 입력해 주세요.");
			$("#bassAdres").focus();
			return false;
		}

		if($.trim($("#locgovBudgetAmt").val()) == "") {
			alert("예산을 입력해 주세요.");
			$("#locgovBudgetAmt").focus();
			return false;
		}

		if($.trim($("#locgovPopltnCo").val()) == "") {
			alert("인구수를 입력해 주세요.");
			$("#locgovPopltnCo").focus();
			return false;
		}

		if($.trim($("#pointRate").val()) == "") {
			alert("포인트 지급률을 입력해 주세요.");
			$("#pointRate").focus();
			return false;
		}

		if(isNaN(parseInt($("#pointRate").val()))) {
			alert("포인트 지급률을 정상적으로 입력해 주세요.");
			$("#pointRate").focus();
			return false;
		}

		if($.trim($("#chargerPsitnDept").val()) == "") {
			alert("부서명을 입력해 주세요.");
			$("#chargerPsitnDept").focus();
			return false;
		}

		if($.trim($("#processDeptCode").val()) == "") {
			alert("부서코드를 입력해 주세요.");
			$("#processDeptCode").focus();
			return false;
		}

		if($("#processDeptCode").val().length != 11) {
			alert("부서코드는 11자리로 입력해 주세요.");
			$("#processDeptCode").focus();
			return false;
		}

		if($.trim($("#administInsttCode").val()) == "") {
			alert("행정표준기관코드를 입력해 주세요.");
			$("#administInsttCode").focus();
			return false;
		}

		if($.trim($("#fisSp").val()) == "") {
			alert("회계구분을 입력해 주세요.");
			$("#fisSp").focus();
			return false;
		}

		var isZero = true;
		var flag = false;
		var overAmt = false;
		var ele = null;
		var nextEleTxt = null;



		$("input.honor-amt").each(function (index, item) {
			var amt = removeComma($(item).val());

			if (amt != '' && Number(amt) != 0) {
				isZero = false;
				return false;
			}
		})


		if (!isZero) {
			$("input.honor-amt").each(function (index, item) {
				var amt = removeComma($(item).val());

				if (amt < 100 || amt > ${limitAmt}) {
					flag = true;
					ele = item;
					return false;
				}

				var nextEle = $('#stdr' + (index + 2) + 'levelAmt');
				if (nextEle.length > 0 && amt >= Number(removeComma(nextEle.val()))) {
					overAmt = true;
					ele = item;
					nextEleTxt = nextEle.parent().find('span').html().trim();
					return false;
				}

			});
		}

		if (flag) {
			alert("기부액은 100원 ~ ${fn:escapeXml(limitAmtString)}원 까지 설정 가능합니다.");
			$(ele).focus();
			return false;
		}

		if (overAmt) {
			var nowTxt = $(ele).parent().find('span').html().trim();
			alert("[" + nowTxt + "] 금액을 [" + nextEleTxt + "] 금액보다 낮게 설정해 주세요.");
			$(ele).focus();
			return false;
		}

		if ($.trim($("#offcsNm").val()) == "") {
			alert("직인명을 입력해 주세요.");
			$("#offcsNm").focus();
			return false;
		}

		if ($("#offcsFile").val() == '') {
			alert("직인 이미지를 등록해 주세요.");
			$("#offcsFile").focus();
			return false;
		}



		return true;
	}

	function removeComma(val) {
		return val.replace(/,/g, "");
	}

	/**
	 *	함 수 명 : setParam
	 *	기	능  : 지자체 등록값 셋팅
	 */
	function setParam() {

		var bizrno = $("#bizrno1").val() + $("#bizrno2").val() + $("#bizrno3").val();
		var chargerCttpc = $("#chargerCttpc1").val() +"-"+ $("#chargerCttpc2").val() +"-"+ $("#chargerCttpc3").val();

		var formData = new FormData();
		formData.append("locgovCode", $("#locgov").val());							// 지자체코드
		formData.append("upperLocgovCode", $("#upperLocgov").val());			// 상위지자체코드
		formData.append("bizrno", bizrno);											// 사업자 등록번호
		formData.append("chargerNm", $("#chargerNm").val());						// 담당자명
		formData.append("chargerEmail", $("#chargerEmail").val());						// 담당자명
		formData.append("chargerCttpc", chargerCttpc);								// 담당자 연락처
		formData.append("locgovHmpg", $("#locgovHmpg").val());						// 지자체 홈페이지
		formData.append("locgovIntrcnCn", $("#locgovIntrcnCn").val());				// 지자체 소개
		formData.append("locgovZip", $("#locgovZip").val());						// 지자체 우편번호
		formData.append("bassAdres", $("#bassAdres").val());						// 기본 주소
		formData.append("dtlAdres", $("#dtlAdres").val());							// 상세 주소
		formData.append("locgovBudgetAmt", removeComma($("#locgovBudgetAmt").val()));			// 예산
		formData.append("locgovPopltnCo", removeComma($("#locgovPopltnCo").val()));				// 인구수
		formData.append("pointRate", $("#pointRate").val());						// 포인트 지급률
		formData.append("stdrYear", new Date().getFullYear());						// 기준 년도
		formData.append("chargerPsitnDept", $("#chargerPsitnDept").val());			// 부서명
		formData.append("processDeptCode", $("#processDeptCode").val());			// 부서코드
		formData.append("administInsttCode", $("#administInsttCode").val());		// 행정표준기관코드
		formData.append("fisSp", $("#fisSp").val());								// 회계구분

		$("input.honor-amt").each(function (index, item) {
			var id = $(item).attr('id');
			formData.append(id, $(item).val() == '' ? 0 : removeComma($(item).val()));				// 기준 금액
			formData.append(id + 'Code', $('#' + id + 'Code').val());									// 기준 코드
		});
		formData.append("offcsNm", $("#offcsNm").val());							// 직인명
		formData.append("offcsFile", $("#offcsFile")[0].files[0]);					// 직인파일


		// 답례품 배경 이미지 추가
		if ($("#addPcFile")[0].files.length > 0) {
			formData.append("addPcFile", $("#addPcFile")[0].files[0]);
		}
		if ($("#addMbFile")[0].files.length > 0) {
			formData.append("addMbFile", $("#addMbFile")[0].files[0]);
		}



		/* var param = new Object();
		param.locgovCode = $("#locgov").val();						// 지자체코드
		param.upperLocgovCode = $("#upperLocgov").val();			// 상위지자체코드
		param.bizrno = bizrno;										// 사업자 등록번호
		param.chargerNm = $("#chargerNm").val();					// 담당자명
		param.chargerCttpc = chargerCttpc;							// 담당자 연락처
		param.locgovHmpg = $("#locgovHmpg").val();					// 지자체 홈페이지
		param.locgovIntrcnCn = $("#locgovIntrcnCn").val();			// 지자체 소개
		param.locgovZip = $("#locgovZip").val();					// 지자체 우편번호
		param.bassAdres = $("#bassAdres").val();					// 기본 주소
		param.dtlAdres = $("#dtlAdres").val();						// 상세 주소
		param.locgovBudgetAmt = $("#locgovBudgetAmt").val();		// 예산
		param.locgovPopltnCo = $("#locgovPopltnCo").val();			// 인구수
		param.pointRate = $("#pointRate").val();					// 포인트 지급률
		param.stdrYear = new Date().getFullYear();					// 기준 년도
		param.chargerPsitnDept = $("#chargerPsitnDept").val();		// 부서명
		param.processDeptCode = $("#processDeptCode").val();		// 부서코드
		param.administInsttCode = $("#administInsttCode").val();	// 행정표준기관코드

		param.stdr1levelAmt = $("#stdr1levelAmt").val();			// 기준 1레벨 금액
		param.stdr2levelAmt = $("#stdr2levelAmt").val();			// 기준 2레벨 금액
		param.stdr3levelAmt = $("#stdr3levelAmt").val();			// 기준 3레벨 금액
 */



		return formData;
	}



	/* *******************************************************************************************************
	 *
	 *											시스템/행안부 function
	 *
	 * *******************************************************************************************************/

	/**
	 *	함 수 명 : moveListPage
	 *	기	능  : 목록 페이지 이동
	 */
	function moveListPage() {
		location.href = "/opmanager/user/locgov/list"+$("#listSearchParam").val();
	}

	/**
	 *	함 수 명 : changeUpperLocgov
	 *	기	능  : 지자체(시도) 변경 이벤트
	 */
	function changeUpperLocgov() {
		var upperLocgovCode = $("#upperLocgov").val();
		$("#locgov").html('<option value="">- 시군구 -</option>');
		$("#locgovCode").html("");

		if(upperLocgovCode) {
			$.post('/opmanager/manager-request/code-child/list', {"codeType": "LOCGOV_CODE"}, function(response) {
				if(response.isSuccess && response.data) {
					$.each(response.data, function(index, item) {
						if(item.id && upperLocgovCode && (item.id.substring(0, 2) == upperLocgovCode.substring(0, 2))) {
							$("#locgov").append('<option value="'+item.id+'">'+item.label+'</option>');
						}
					});
				}
			});
		}
	}

	/**
	 *	함 수 명 : changeLocgov
	 *	기	능  : 지자체(시군구) 변경 이벤트
	 */
	function changeLocgov() {
		let locgovCode = $("#locgov").val();
		$("#locgovCode").html(locgovCode);
		
		let options = $("#fisSp")[0].options;
		let length = options.length;
		for(let i = length ; i > 1 ; i--) {
			options[i-1].remove();
		}
		
		if (locgovCode) {
			if (locgovCode.substring(2, 5) == '000') {
				let opt = document.createElement("option");
				opt.value = "31";
				opt.text = "일반회계";
				$("#fisSp")[0].appendChild(opt);
				opt = document.createElement("option");
				opt.value = "51";
				opt.text = "특별회계";
				$("#fisSp")[0].appendChild(opt);
			} else {
				let opt = document.createElement("option");
				opt.value = "41";
				opt.text = "일반회계";
				$("#fisSp")[0].appendChild(opt);
				opt = document.createElement("option");
				opt.value = "61";
				opt.text = "특별회계";
				$("#fisSp")[0].appendChild(opt);
			}
		}
	}



	/* *******************************************************************************************************
	 *
	 *											지자체 function
	 *
	 * *******************************************************************************************************/

	 /**
	 *	함 수 명 : clearLocgov
	 *	기	능  : 지자체 정보 초기화
	 */
	function clearLocgov() {
		$("#locgovIntrcnCn").val("");
		$("#chargerCttpc1").val("");
		$("#chargerCttpc2").val("");
		$("#chargerCttpc3").val("");
		$("#chargerNm").val("");
		$("#chargerEmail").val("");
		$("#locgovHmpg").val("");
		$("#locgovPopltnCo").val("");
		$("#locgovBudgetAmt").val("");
		$("#bizrno1").val("");
		$("#bizrno2").val("");
		$("#bizrno3").val("");
		$("#locgovZip").val("");
		$("#bassAdres").val("");
		$("#dtlAdres").val("");
		$("#pointRate").val("");
		$("#chargerPsitnDept").val("");
		$("#processDeptCode").val("");
		$("#administInsttCode").val("");

		$("#stdr1levelAmt").val("");
		$("#stdr2levelAmt").val("");
		$("#stdr3levelAmt").val("");

		$("#offcsNm").val();

		if (Browser.getName() == 'ie') {
			$("#offcsFile").replaceWith($("#offcsFile").clone(true));
		} else {
			$("#offcsFile").val("");
		}


	}

	function selectPcImgFile(inputElement) {
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
	function addPcImgClick() {
		document.getElementById("addFile").click();
	}








	//답례품 배경 이미지 관련
	function changePcImgFile(event) {
		let inputElement = document.getElementById('addPcFile');
		let files = inputElement.files;
		let displayAddFile = document.getElementById('uploadPcImg');		// 첨부파일 목록 영역

		displayAddFile.setAttribute("src", '/content/images/common/no-image-gray.gif');

		if (files && files.length > 0) {
			let file = files[0];
			if (file.type && file.type.indexOf('image/') > -1) {
				setThumbnail(event, displayAddFile);
			} else {
				const dataTransfer = new DataTransfer();		// 폼 객체 내 파일 정보 수정시 처리용 객체
				inputElement.files = dataTransfer.files;
				alert("이미지 파일만 업로드 가능합니다.");
			}
		}
	}

	// input file 숨겨서 별도 버튼으로 동작하도록 함
	function addPcImgClick() {
		document.getElementById("addPcFile").click();
	}


	//답례품 배경 이미지 관련
	function changeMbImgFile(event) {
		let inputElement = document.getElementById('addMbFile');
		let files = inputElement.files;
		let displayAddFile = document.getElementById('uploadMbImg');		// 첨부파일 목록 영역

		displayAddFile.setAttribute("src", '/content/images/common/no-image-gray.gif');

		if (files && files.length > 0) {
			let file = files[0];
			if (file.type && file.type.indexOf('image/') > -1) {
				setThumbnail(event, displayAddFile);
			} else {
				const dataTransfer = new DataTransfer();		// 폼 객체 내 파일 정보 수정시 처리용 객체
				inputElement.files = dataTransfer.files;
				alert("이미지 파일만 업로드 가능합니다.");
			}
		}
	}


	function addMbImgClick() {
		document.getElementById("addMbFile").click();
	}

	function setThumbnail(event, imgElement) {
		let reader = new FileReader();
		reader.onload = function(event) {
			imgElement.setAttribute("src", event.target.result);
		};
		reader.readAsDataURL(event.target.files[0]);
	}
</script>
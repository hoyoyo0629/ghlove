<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules"%>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<style>
span.require {color: #e84700; margin-left: 5px;}

</style>

			<div class="location">
				<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
			</div>


			<form:form modelAttribute="seller" id="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || requestContext.sellerPage ? 'seller': 'impossible-form'}" method="post" enctype="multipart/form-data" onsubmit="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6') || requestContext.sellerPage ? '': 'return false'}">
				<input type="hidden" name="idCheck" id="idCheck" value="0"/>
				<input type="hidden" name="mode" id="mode" value="${fn:escapeXml(mode)}"/>
				<div class="item_list">
					<!--입점업체 등록/수정 시작-->
					<h3><span>${op:message('M00746')}</span></h3> <!-- 입점업체 등록/수정 -->

					<c:if test="${mode == 'edit'}">
	                    <div class="btn_all btn_right mb15">
	                        <div class="flex_box gap-08">
	                            <button type="submit" class="btn btn-dark-gray btn-mini"><span>${op:message('M00087')}</span></button> <!-- 수정 -->
	                            <!-- <a href="javascript:;" onClick="fn_delete()" class="btn btn-dark-gray btn-mini">${op:message('M00074')}</a> -->
	                            <button type="button" class="btn btn-defualt btn-mini" onclick="location.href='/opmanager/seller/list'"><span>${op:message('M00480')}</span></a> <!-- 목록 --> </button>
	                        </div>
	                    </div>
                    </c:if>

					<div class="board_write">
						<table class="board_write_table">
							<caption>${op:message('M00746')}</caption>
							<colgroup>
								<col style="width:220px;" />
								<col style="width:auto;" />
								<col style="width:220px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
                                <tr>
                                    <td class="label">지자체</td>
                                    <td colspan="3">
                                    	<c:choose>
                                    		<c:when test="${mode=='create'}">
                                    			<div>${fn:escapeXml(userLocgovCodeDetails.label)}</div>
                                    		</c:when>
	                                    	<c:otherwise>
	                                    		<div>${fn:escapeXml(seller.locgovNm)}</div>
	                                    	</c:otherwise>
                                    	</c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label"><c:if test="${mode == 'create'}"><span class="required_mark">*</span></c:if>아이디</td>
									<c:choose>
										<c:when test="${mode=='create'}">
		                                    <td colspan="3">
		                                        <div class="flex_box gap-08">
		                                            <form:input type="text" path="loginId" title="${op:message('M00081')}" class="input_txt required _filter wd-150" readonly="true"/>
		                                            <button type="button" id="makeloginId" class="btn btn-default btn-mini" onclick="makeSellerId()">아이디 생성</button>
		                                        </div>
		                                    </td>
										</c:when>
										<c:otherwise>
		                                    <td>
		                                        <div class="flex_box gap-08">
													${seller.loginId}
													<input type="hidden" name="loginId" id="loginId" value="${seller.loginId}"/>
		                                        </div>
		                                    </td>
										</c:otherwise>
									</c:choose>
									<c:if test="${mode == 'edit'}">
										<td class="label">비밀번호</td>
										<td>
											<div class="input_wrap" style="display:flex;align-items: center;">
												<button type="button" class="btn btn-dark-gray btn-sm btn-password">비밀번호 초기화</button>
												<p style="white-space:pre;">   초기화된 비밀번호는 1111 입니다.</p>
											</div>
										</td>
									</c:if>
                                </tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M00005')}</td> <!-- 이름 -->
									<td colspan="3">
										<div>
											<form:input type="text" path="sellerName" title="${op:message('M00005')}" class="input_txt required _filter wd-150" maxlength="20"/>
									    </div>
									</td>
								</tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>휴대폰</td> <!-- 담당자 휴대폰번호 -->
									<td colspan="3">
										<div class="flex_box gap-08 item-center">
											<c:set var="phone_arr" value="${fn:split(seller.phoneNumber, '-') }"/>
											<select name="phoneNumber1" class="choice3 wd-150">
												<c:forEach items="${phoneCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${code.value == phone_arr[0] ? 'selected="selected"' : '' }>${fn:escapeXml(code.label)}</option>
												</c:forEach>
											</select>
											<span class="wave">-</span>
											<input type="text" name="phoneNumber2" value="${phone_arr[1]}" maxlength="4" class="required form-sm _number wd-150" title="휴대폰번호 가운데자리"/>
											<span class="wave">-</span>
											<input type="text" name="phoneNumber3" value="${phone_arr[2]}" maxlength="4" class="required form-sm _number wd-150" title="휴대폰번호 마지막자리"/>
										</div>
									</td>
								</tr>
								<tr>
							        <td class="label">${op:message('M01111')}</td> <!-- 이메일주소 -->
							        <td colspan="3">
										<div class="flex_box gap-08 item-center">
											<input id="emailAddr" title="${op:message('M01111')}" class="input_txt _filter wd-150 _first_email" type="text" value="${fn:escapeXml(seller.emailBefore)}">
											<span class="wave">@</span>
											<select id="email_choice" title="이메일 선택" class="wd-150" onchange="setEmail()">
												<option value="">-선택-</option>
												<c:forEach items="${emailCodes}" var="code" varStatus="status">
													<option value="${fn:escapeXml(code.id)}" <c:if test="${seller.emailAfter == code.id}">selected='selected'</c:if>>${fn:escapeXml(code.label)}</option>
												</c:forEach>
											</select>
											<input id="directInput" title="직접입력" class="input_txt _filter wd-150 _first_email" type="text" value="${fn:escapeXml(seller.emailAfter)}" disabled>
											<form:input path="email" class="input_txt _filter wd-500 _email hidden" title="담당자 이메일"/>
										</div>
							        </td>
								</tr>
								<c:if test="${mode=='create'}">
									<tr class="hidden">
										<td class="label">
											${op:message('M00150')}	<!-- 패스워드 -->
											<c:set var="pwd_required" value=""/>
											<c:if test="${mode=='create'}">
												<span class="require">*</span>
												<c:set var="pwd_required" value="required"/>
											</c:if>
										</td>
										<td colspan="3">
											<div>
												<input type="password" name="password" id="password" title="${op:message('M00150')}" class="required"  value="1111"/>
										    </div>
										</td>
									</tr>
								</c:if>
                                <tr>
                                    <td class="label" colspan="4">사업자 정보</td>
                                </tr>
								<tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M01635')}</td> <!-- 상호 -->
									<td>
										<div>
											<form:input type="text" path="companyName" title="${op:message('M01635')}" class="input_txt required _filter wd-150" />
										</div>
									</td>
						        	<td class="label"><span class="required_mark">*</span>${op:message('M01648')}</td> <!-- 대표자명 -->
									<td>
										<div>
											<form:input type="text" path="representativeName" title="${op:message('M01648')}" class="input_txt required _filter wd-150" />
										</div>
									</td>
						        </tr>
                                <tr>
                                    <td class="label">사업자등록증</td>
                                    <td>
                                        <div class="flex_box item-center">
                                            <input type="file" name="uploadFile1" class="full input_file" title="사업자등록증" accept="image/png, image/jpeg, image/gif, .pdf" />
                                        </div>
                                        <c:if test="${mode=='edit' && seller.fileNameCertificate1 != null}">
	                                        <div class="file_camera" id="attachedFile1">
	                                        	<input type="hidden" name="fileNameCertificate1" value="${fn:escapeXml(seller.fileNameCertificate1)}" />
	                                            <div>
	                                            	${fn:escapeXml(seller.fileNameCertificate1)}
	                                            	<a href="javascript:deleteFile('${fn:escapeXml(seller.fileNameCertificate1)}', '${fn:escapeXml(seller.sellerId)}', 'file1');"><img src="/content/images/btn/file_close.gif" alt="close"></a>
	                                            </div>
	                                        </div>
                                        </c:if>
                                    </td>
                                    <td class="label">통신판매신고증</td>
                                    <td>
                                        <div class="flex_box item-center">
                                            <input type="file" name="uploadFile2" class="full input_file" title="통신판매신고증" accept="image/png, image/jpeg, image/gif, .pdf" />
                                        </div>
                                        <c:if test="${mode=='edit' && seller.fileNameCertificate2 != null}">
	                                        <div class="file_camera" id="attachedFile2">
	                                        	<input type="hidden" name="fileNameCertificate2" value="${fn:escapeXml(seller.fileNameCertificate2)}" />
	                                            <div>
	                                            	${fn:escapeXml(seller.fileNameCertificate2)}</a>
	                                            	<a href="javascript:deleteFile('${fn:escapeXml(seller.fileNameCertificate2)}', '${fn:escapeXml(seller.sellerId)}', 'file2');"><img src="/content/images/btn/file_close.gif" alt="close"></a>
	                                            </div>
	                                        </div>
                                        </c:if>
                                    </td>
                                </tr>
						        <tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M00106')}</td> <!-- 사업자등록번호 -->
									<td>
										<div class="flex_box gap-08 item-center">
											<c:set var="businessNumber_arr" value="${fn:split(seller.businessNumber, '-') }"/>
											<input type="text" name="businessNumber1" id="businessNumber1" value="${businessNumber_arr[0]}" title="${op:message('M01649')}" class=" _number form-sm required wd-150" maxlength="3"/>
											<span class="wave">-</span>
											<input type="text" name="businessNumber2" id="businessNumber2" value="${businessNumber_arr[1]}" title="${op:message('M01649')}" class=" _number form-sm required wd-150" maxlength="2"/>
											<span class="wave">-</span>
											<input type="text" name="businessNumber3" id="businessNumber3" value="${businessNumber_arr[2]}" title="${op:message('M01649')}" class=" _number form-sm required wd-150" maxlength="5"/>
										</div>
									</td>
                                    <%-- <td class="label"><span id="statMark1" class="required_mark <c:if test="${seller.mailOrderNumber == ''}">hidden</c:if>">*</span>통신판매신고번호</td>
                                    <td>
                                        <div class="flex_box gap-12">
                                        	<c:choose>
                                        		<c:when test="${seller.mailOrderNumber != ''}">
                                            		<form:input type="text" path="mailOrderNumber" title="통신판매신고번호" class="input_txt required _filter wd-200" />
                                            	</c:when>
                                            	<c:otherwise>
                                            		<form:input type="text" path="mailOrderNumber" title="통신판매신고번호" class="input_txt _filter wd-200" readonly="true"/>
                                            	</c:otherwise>
                                            </c:choose>
                                            <div class="checkbox">
                                            	<input type="checkbox" id="localLove" name="localLove" <c:if test="${seller.mailOrderNumber == ''}">checked='checked'</c:if>/> <label for="localLove"> 지역사랑상품권 제공전용</label>
                                            </div>
                                        </div>
                                    </td> --%>
									<%-- <c:choose>
										<c:when test="${seller.mailOrderNumber != null && seller.mailOrderNumber != ''}">
											<td class="label"><span id="statMark1" class="required_mark">*</span>통신판매신고번호</td>
											<td>
												<div class="flex_box gap-12">
													<form:input type="text" path="mailOrderNumber" title="통신판매신고번호" class="input_txt required _filter wd-200" />
													<div class="checkbox">
														<input type="checkbox" id="localLove" name="localLove" checked="checked"/><label for="localLove">전통주류 판매 가능</label>
													</div>
												</div>
											</td>
										</c:when>
										<c:otherwise>
                                            <td class="label"><span id="statMark1" class="required_mark hidden">*</span>통신판매신고번호</td>
											<td>
												<div class="flex_box gap-12">
													<form:input type="text" path="mailOrderNumber" title="통신판매신고번호" class="input_txt _filter wd-200" readonly="true"/>
													<div class="checkbox">
														<input type="checkbox" id="localLove" name="localLove" /> <label for="localLove">전통주류 판매 가능</label>
													</div>
												</div>
											</td>
										</c:otherwise>
									</c:choose> --%>
									<td class="label">통신판매신고번호</td>
									<td>
										<div class="flex_box gap-12">
											<form:input type="text" path="mailOrderNumber" title="통신판매신고번호" class="input_txt _filter wd-200"/>
										</div>
									</td>
								</tr>
                                <tr>
                                    <td class="label">구매안전이용확인증</td>
                                    <td>
                                        <div class="flex_box item-center">
                                            <input type="file" name="uploadFile3" class="full input_file" title="구매안전이용확인증" accept="image/png, image/jpeg, image/gif, .pdf" />
                                        </div>
                                        <c:if test="${mode=='edit' && seller.fileNameCertificate3 != null}">
	                                        <div class="file_camera" id="attachedFile3">
	                                        	<input type="hidden" name="fileNameCertificate3" value="${fn:escapeXml(seller.fileNameCertificate3)}" />
	                                            <div>
	                                            	${fn:escapeXml(seller.fileNameCertificate3)}
	                                            	<a href="javascript:deleteFile('${fn:escapeXml(seller.fileNameCertificate3)}', '${fn:escapeXml(seller.sellerId)}', 'file3');"><img src="/content/images/btn/file_close.gif" alt="close"></a>
	                                            </div>
	                                        </div>
                                        </c:if>
                                    </td>
                                    <td class="label">구매안전 이용확인번호</td>
                                    <td>
                                        <div>
                                        	<%-- <c:choose>
                                        		<c:when test="${seller.mailOrderNumber != ''}">
                                            		<form:input type="text" path="buySafetyUseConfirmNumber" title="구매안전 이용확인번호" class="input_txt _filter wd-200" />
                                            </c:when>
                                            	<c:otherwise>
                                            		<form:input type="text" path="buySafetyUseConfirmNumber" title="구매안전 이용확인번호" class="input_txt _filter wd-200" readonly="true"/>
                                            	</c:otherwise>
                                            </c:choose> --%>
                                            <form:input type="text" path="buySafetyUseConfirmNumber" title="구매안전 이용확인번호" class="input_txt _filter wd-200" />
                                        </div>
                                    </td>
                                </tr>
						        <tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M01651')}</td> <!-- 업태 -->
									<td>
										<div>
											<form:input type="text" path="businessType" title="${op:message('M01651')}" class=" required wd-150" />
										</div>
									</td>
									<td class="label"><span class="required_mark">*</span>업종</td> <!-- 업종 -->
									<td>
										<div class="flex_box gap-12">
											<div>
												<form:input type="text" path="businessItems" title="업종" class=" required wd-200" />
											</div>
	                                        <div class="checkbox">
												<form:checkbox path="adultItemYn" value="Y" label="성인답례품 등록권한" />
												<input type="hidden" name="!adultItemYn" value="N" />
	                                        </div>
	                                    </div>
									</td>
						        </tr>
								<tr>
									<td class="label" rowspan="2"><span class="required_mark">*</span>대표번호</td> <!-- 대표번호 -->
									<td rowspan="2">
										<div class="flex_box gap-08 item-center">
											<c:set var="tel_arr" value="${fn:split(seller.telephoneNumber, '-') }"/>
											<c:set var="first_tel" value="${fn:split(tel_arr[0], ',') }"/>
											<input type="text" id="iptTelNum1" value="${first_tel[0]}" maxlength="4" class="form-sm _number wd-150 telephoneNumber ${seller.manualNumberType ? '' : 'hidden'}" title="대표번호 첫번째자리" />
											<select name="telephoneNumber1" id="selTelNum1" class="choice3 wd-150 telephoneNumber ${seller.manualNumberType ? 'hidden':''}">
												<!-- 국번이 없는 경우를 위해 추가 -->
												<option value="미선택" ${code.value == null || code.value == '' ? 'selected="selected"' : '' }>미선택</option>
												<c:forEach items="${telCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${code.value == first_tel[0] ? 'selected="selected"' : '' }>${fn:escapeXml(code.label)}</option>
												</c:forEach>
											</select>
											<span class="wave">-</span>
											<input type="text" name="telephoneNumber2" value="${tel_arr[1]}" maxlength="4" class="required form-sm _number wd-150" title="대표번호 가운데자리"/>
											<span class="wave">-</span>
											<input type="text" name="telephoneNumber3" value="${tel_arr[2]}" maxlength="4" class="required form-sm _number wd-150" title="대표번호 마지막자리"/>
										</div>

										<div class="checkbox">
											<form:checkbox path="manualNumberType" value="true" label="직접입력" />
										</div>
									</td>
                                    <td class="label"><span class="required_mark">*</span>과세구분</td>
                                    <td>
                                        <div class="flex_box gap-12">
                                            <div class="input-form">
												<form:radiobutton path="taxType" title="과세" class="required" value="1" label="과세" checked="checked"/>
                                            </div>
                                            <div class="input-form">
												<form:radiobutton path="taxType" title="비과세" class="required" value="2" label="비과세"/>
                                            </div>
                                        </div>
                                    </td>
								</tr>
								<tr>
									<td class="label"> 마을기업 여부</td>
									<td>
										<div class="input-form">
											<form:checkbox path="communityBusinessYn" value="Y" label="마을기업 여부" /> <!-- 마을기업 여부 -->
											<input type="hidden" name="!communityBusinessYn" value="N" />
										</div>
									</td>
								</tr>
						        <tr>
									<td class="label">${op:message('M01650')}<span class="require">*</span></td> <!-- 사업장 소재지 -->
									<td colspan="3">
	                                    <div class="flex_box gap-08">
											<input type="hidden" name="newPost" value="">
											<input type="text" name="post" id="post" value="${fn:escapeXml(seller.post)}" class="required wd-150" title="${op:message('M01650')}" maxlength="5" class="one" required="required" readonly="readonly">
											<a href="javascript:;" onclick="openDaumPostcode()" class="btn btn-default btn-mini"> 주소찾기</a>

	                                    </div>
	                                    <div class="flex_box gap-08">
											<form:input type="text" path="businessLocation" title="${op:message('M01646')}" class="input_txt required _filter wd-500" readonly="true" /><br/>
											<form:input type="text" path="addressDetail" title="${op:message('M01647')}" class="input_txt required _filter wd-500" maxlength="100"/>
	                                    </div>
									</td>
						        </tr>
						        <tr>
									<td class="label">${op:message('M01476')}</td> <!-- 영업상태 -->
									<td colspan="3">
										<div class="flex_box gap-12">
											<div class="input-form">
												<form:radiobutton path="statusCode" title="${op:message('M00083')}" class="required" value="2" label="사용" checked="checked"/>
                                        	</div>
                                        	<div class="input-form">
                                        		<form:radiobutton path="statusCode" title="${op:message('M01639')}" class="required" value="3" label="중지" />
											</div>
                                        	<div class="input-form">
                                        		<form:radiobutton path="statusCode" title="승인대기" class="required" value="5" label="승인대기" />
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">상품승인설정</td>
									<td colspan="3">
										<div class="flex_box gap-12">
											<div class="input-form">
												<form:radiobutton path="itemApprovalType" class="required" value="1" label="관리자 승인" checked="checked"/>
											</div>
											<div class="input-form">
												<form:radiobutton path="itemApprovalType" class="required" value="2" label="자동승인"/>
											</div>
										</div>
									</td>
						        </tr>
								<tr>
									<td class="label">SMS 수신동의</td>
									<td colspan="3">
										<div class="flex_box gap-12">
											<div class="input-form">
												<form:radiobutton path="receiveSms" class="required" value="0" label="동의" checked="checked"/>
											</div>
											<div class="input-form">
												<form:radiobutton path="receiveSms" class="required" value="1" label="동의안함"/>
											</div>
										</div>
									</td>
						        </tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="item_list mt30 hidden">
					<h3><span>담당자정보</span></h3>
					<div class="board_write">
						<table class="board_write_table">
							<caption>${op:message('M00746')}</caption>
							<colgroup>
								<col style="width:170px;" />
								<col style="width:auto;" />
								<col style="width:170px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
								</tr>
								<tr>
									<td class="label">${op:message('M01632')}<span class="require">*</span></td> <!-- 담당자명 -->
									<td colspan="3">
										<div>
											<form:input type="text" path="userName" title="${op:message('M01632')}" class=" " />
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M01643')}<span class="require">*</span></td> <!-- 팩스번호 -->
									<td colspan="3">
										<div>
											<c:set var="fax_arr" value="${fn:split(seller.faxNumber, '-') }"/>
											<select name="faxNumber1" class="choice3">
												<c:forEach items="${telCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${fax_arr[0] == code.value ? 'selected="true"' : '' }>${fn:escapeXml(code.label)}</option>
												</c:forEach>
											</select> -
											<input type="text" name="faxNumber2" value="${fax_arr[1]}" maxlength="4" class=" form-sm _number" title="팩스 가운데자리"/> -
											<input type="text" name="faxNumber3" value="${fax_arr[2]}" maxlength="4" class=" form-sm _number" title="팩스 마지막자리"/>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div> <!-- // board_write -->
				</div>

				<div class="item_list mt30 hidden">
					<h3><span>주문 담당자</span>
						<span class="text-info">
							* <strong>주문안내 SMS발송</strong> 시간을 설정한 경우 설정된 시간에 주문 담당자에게 주문안내 SMS발송합니다.
						</span>
					</h3>
					<div class="board_write">
						<table class="board_write_table">
							<caption>${op:message('M00746')}</caption>
							<colgroup>
								<col style="width:170px;" />
								<col style="width:auto;" />
								<col style="width:170px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
								</tr>
								<tr>
									<td class="label">${op:message('M01632')}</td> <!-- 담당자명 -->
									<td colspan="3">
										<div>
											<form:input type="text" path="secondUserName" title="${op:message('M01632')}" class="" />
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M01633')}</td> <!-- 담당자 전화번호 -->
									<td>
										<div>
											<c:set var="tel_arr" value="${fn:split(seller.secondTelephoneNumber, '-') }"/>
											<select name="secondTelephoneNumber1" class="choice3">
												<c:forEach items="${telCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${code.value == tel_arr[0] ? 'selected="selected"' : '' }>${fn:escapeXml(code.label)}</option>
												</c:forEach>
											</select> -
											<input type="text" name="secondTelephoneNumber2" value="${tel_arr[1]}" maxlength="4" class="form-sm _number" title="전화번호 가운데자리"/> -
											<input type="text" name="secondTelephoneNumber3" value="${tel_arr[2]}" maxlength="4" class="form-sm _number" title="전화번호 마지막자리"/>
										</div>
									</td>
									<td class="label">${op:message('M01634')}</td> <!-- 담당자 휴대폰번호 -->
									<td>
										<div>
											<c:set var="phone_arr" value="${fn:split(seller.secondPhoneNumber, '-') }"/>
											<select name="secondPhoneNumber1" class="choice3">
												<c:forEach items="${phoneCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${code.value == phone_arr[0] ? 'selected="selected"' : '' }>${fn:escapeXml(code.label)}</option>
												</c:forEach>
											</select> -
											<input type="text" name="secondPhoneNumber2" value="${phone_arr[1]}" maxlength="4" class="form-sm _number_masking" title="휴대폰번호 가운데자리"/> -
											<input type="text" name="secondPhoneNumber3" value="${phone_arr[2]}" maxlength="4" class="form-sm _number" title="휴대폰번호 마지막자리"/>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label">${op:message('M01644')}</td> <!-- 담당자이메일 -->
							        <td>
										<div>
											<form:input path="secondEmail" class="form-half optional _email" title="담당자 이메일"/>
										</div>
							        </td>
							        <td class="label">주문안내 SMS 발송</td>
							        <td>
										<div>
											<select name="smsSendTime">
												<option value="">미설정</option>

												<c:set var="timeSeperate" value="오전" />

												<c:forEach begin="1" end="23" var="i">
													<c:set var="time" value="${i}" />
													<c:set var="timeValue" value="${i}" />
													<c:if test="${i >= 12}">
														<c:set var="timeSeperate" value="오후" />
													</c:if>
													<c:if test="${i > 12}">
														<c:set var="time" value="${i - 12}" />
													</c:if>

													<c:if test="${i < 10}">
														<c:set var="i">0${i}</c:set>
													</c:if>

													<option value="${timeValue}" ${seller.smsSendTime == timeValue ? 'selected="selected"' : '' }>${timeSeperate}&nbsp;${time}시</option>

												</c:forEach>
											</select>
										</div>
							        </td>
								</tr>

							</tbody>
						</table>
					</div> <!-- // board_write -->
				</div>



				<div class="item_list mt30">
					<h3 class="mt50 fs24"><span>정산정보</span></h3> <!-- 정산정보 -->
					<div class="board_write">
						<table class="board_write_table">
							<caption>정산정보</caption>
							<colgroup>
								<col style="width:220px;" />
								<col style="width:auto;" />
								<col style="width:220px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
								<tr class="hidden">
									<td class="label">${op:message('M01653')}<span class="require">*</span></td> <!-- 수수료율 -->
									<td colspan="3">
										<div>
											<form:input type="text" path="commissionRate" title="${op:message('M01653')}" class=" amount _number_float _percent _min_0" maxlength="4"/>%
										</div>
									</td>
						        </tr>
						        <tr class="hidden">
									<td class="label">${op:message('M01654')}<span class="require">*</span></td> <!-- 정산주기 -->
									<td>
										<div>

											<form:select path="remittanceType" class="choice3" style="width:100px;">
												<form:option value="">선택하세요</form:option>
												<c:forEach items="${remittanceTypeCodes}" var="code">
													<form:option value="${fn:escapeXml(code.value)}">${fn:escapeXml(code.label)}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</td>

									<c:if test="${seller.remittanceType != 4}">
										<c:set var="tdHide" value="hide"/>
									</c:if>

									<td class="label remittanceDayTd ${fn:escapeXml(tdHide)}">정산일</td> <!-- 정산일 -->
									<td class="remittanceDayTd ${fn:escapeXml(tdHide)}">
										<div>
											<form:select path="remittanceDay">
												<form:option value="">선택하세요</form:option>
												<form:option value="1">1일</form:option>
												<form:option value="5">5일</form:option>
												<form:option value="10">10일</form:option>
												<form:option value="15">15일</form:option>
												<form:option value="20">20일</form:option>
												<form:option value="25">25일</form:option>
											</form:select>
										</div>
									</td>
						        </tr>
						        <tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M01655')}</td> <!-- 정산- 입급은행명 -->
									<td>
										<div>
											<form:select path="bankName" title="${op:message('M01655')}" class="choice3 required wd-150">
												<form:option value="">-선택-</form:option>
												<c:forEach items="${bankCodes}" var="code">
													<form:option value="${fn:escapeXml(code.label)}">${fn:escapeXml(code.label)}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</td>
									<td class="label"><span class="required_mark">*</span>${op:message('M00167')}명</td> <!-- 정산- 예금주명 -->
									<td>
										<div>
											<form:input type="text" path="bankInName" title="${op:message('M00167')}명" class=" required wd-150" maxlength="10"/>
										</div>
									</td>
						        </tr>
						        <tr>
									<td class="label"><span class="required_mark">*</span>${op:message('M01656')}</td> <!-- 정산- 입급계좌번호 -->
									<td colspan="3">
										<div>
											<form:input type="text" path="bankAccountNumber" title="${op:message('M01656')}" class=" _number required wd-500" maxlength="20"/>
											<span class="tip">‘-’ 를 제외하고 입력해 주세요.</span>
										</div>
									</td>
						        </tr>
							</tbody>
						</table>
						<div class="check-all-item-notice-label hidden" style="display: block;">
							* 정산주기 상세설명<br>
							1. 일정산 : 구매확정일의 다음날 정산됩니다.<br>
							2. 주정산 : 구매확정일 기준 차주 월요일에 정산됩니다. (일 ~ 토요일까지의 구매확정주문들이 차주 월요일에 정산)<br>
							3. 15일주기 : 1 ~ 14일 구매확정주문 = 15일에 정산, 15 ~ 말일 구매확정주문 = 다음달 1일에 정산됩니다.<br>
							4. 월정산 : 월정산의 정산일날 구매확정된 주문은 다음달에 정산됩니다. (ex. 설정한 정산일이 매달1일, 주문의 구매확정이 1월 1일인경우 2월1일에 정산.)<br>
							* <strong>정산일이 휴일인 경우 다음날 정산됩니다.</strong>
						</div>
					</div>
				</div>

				<div class="item_list mt30 hidden">
					<h3><span>판매자 배송비 설정</span></h3> <!-- 조건부배송비 -->
					<div class="board_write">
						<table class="board_write_table">
							<colgroup>
								<col style="width:170px;" />
								<col style="width:auto;" />
								<col style="width:170px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
								<tr>
									<td class="label">설정 여부</td> <!-- 판매자 조건부 배송비 설정여부 -->
									<td colspan="3">
										<div>
											<form:radiobutton path="shippingFlag" title="${op:message('M01658')}" class="required" value="N" label="미설정"/>
											<form:radiobutton path="shippingFlag" title="${op:message('M01658')}" class="required" value="Y" label="설정"/>

										</div>
									</td>
						        </tr>

						        <c:set var="shipping_hide" value="hide"/>
						        <c:if test="${seller.shippingFlag=='Y' || seller.shippingFlag==''}">
									<c:set var="shipping_hide" value=""/>
									<c:set var="shipping_required" value="required"/>
						        </c:if>

						        <tr class="shipping_tr ${fn:escapeXml(shipping_hide)}">
									<td class="label">배송비<span class="require">*</span></td> <!-- 판매자 조건부 배송비 -->
									<td>
										<div>
											<form:input type="text" path="shipping" title="배송비" class=" ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원
										</div>
									</td>
									<td class="label">무료배송 조건별 금액<span class="require">*</span></td> <!-- 판매자 조건부 배송비 무료배송 금액 -->
									<td>
										<div>
											<form:input type="text" path="shippingFreeAmount" title="조건별 무료배송 금액" class=" ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원 이상 무료
										</div>
									</td>
						        </tr>
						        <tr class="shipping_tr ${fn:escapeXml(shipping_hide)}">
									<td class="label">${op:message('M01660')}<span class="require">*</span></td> <!-- 추가배송비 - 제주도 -->
									<td>
										<div>
											<form:input type="text" path="shippingExtraCharge1" title="${op:message('M01658')}" class=" ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원
										</div>
									</td>
									<td class="label">${op:message('M01661')}<span class="require">*</span></td> <!-- 추가배송비 - 도서산간 -->
									<td>
										<div>
											<form:input type="text" path="shippingExtraCharge2" title="${op:message('M01659')}" class=" ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원
										</div>
									</td>
						        </tr>
							</tbody>
						</table>
					</div>

					<div class="text-info" style="margin-top: 10px">
						* 판매자 배송비를 설정한 경우 설정 값이 상품 등록 시 배송시 설정 항목 중 <strong>판매자조건부</strong>에 설정됩니다. <br />
						* <strong>판매자조건부</strong>로 설정된 상품을 주문하는 경우 해당 설정 금액 기준으로 묶음 배송비가 부과됩니다.

					</div>
				</div>

				<div class="item_list mt30 hidden">
					<h3><span>관리항목</span></h3>
					<div class="board_write">
						<table class="board_write_table">
							<colgroup>
								<col style="width:170px;" />
								<col style="width:auto;" />
								<col style="width:170px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
						        <tr>
									<td class="label">담당MD<span class="require">*</span></td> <!-- 담당MD -->
									<td colspan="3">
										<div>
											<input type="hidden" name="currentMdId" value="${fn:escapeXml(seller.mdId)}" />
											<input type="hidden" id="mdId" name="mdId" value="${fn:escapeXml(seller.mdId)}" />
											<form:input path="mdName" title="담당MD" readonly="true" />

											<button type="button" onclick="findMd('mdId')" class="btn btn-dark-gray btn-sm"><span class="glyphicon glyphicon-search"></span> MD검색</button>
											<button type="button" onclick="clearMd('mdId')" class="btn btn-gradient btn-sm"><span class="glyphicon glyphicon-remove"></span> 초기화</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div> <!-- // board_write -->
				</div>
				<div class="btn_all btn_center">
					<div class="flex_box gap-08">
						<c:if test="${mode == 'create'}">
							<button type="submit" class="btn btn-dark-gray btn-small"><span>${op:message('M00088')}</span></button> <!-- 등록 -->
							<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/opmanager/seller/list'"><span>${op:message('M00037')}</span></a> <!-- 취소 --> </button>
						</c:if>
					</div>
				</div>
			</form:form>

<form id="deleteForm" method="post" action="/opmanager/seller/delete">
	<input type="hidden" name="sellerId" value="${fn:escapeXml(seller.sellerId)}" />
	<input type="hidden" name="userId" value="${fn:escapeXml(seller.userId)}" />
</form>

<!-- 다음 주소검색 -->
<daum:address />

<script type="text/javascript">

$(function() {

	// 수정일때 이메일 설정
	if($('#mode').val() == 'edit'){

		if($("#email_choice").val() != "") {
			$("#directInput").hide();	// 직접입력이 아닌 경우 이메일 도메인값 숨기기
		} else {
			if($("#directInput").val() != "") {
				$("#email_choice").val('0');	// 직접입력 선택
				directInput.disabled = false;
			}
		}
	} else {
		$("#directInput").hide();
	}

	Common.addNumberComma();

	$('#impossible-form').submit(function() {
		alert('수정 권한이 없습니다. (지자체 권한 필요)');
		Common.loading.hide();
		return false;
	});

	//조회한 내용이 콤보에 없으면
	if ($("#selTelNum1 option:checked").val() == "미선택") {
		//input에 값이 있으면
		if ($.trim($("#iptTelNum1").val()).length > 0) {
			$("#manualNumberType1").prop("checked", true).trigger("change");
			$("#selTelNum1").prop("selectedIndex", 0);
		}else{
			$("#manualNumberType1").prop("checked", false).trigger("change");
			$("#iptTelNum1").val("");
		}
	}else{
		$("#manualNumberType1").prop("checked", false).trigger("change");
		$("#iptTelNum1").val("");
	}


	// validator
	try {

		$('#seller').validator(function() {
			var loginId = $("#loginId").val();
			 var params = {
				'loginId' : loginId
			};

			//if($('#mode').val() == 'create'){

				if($("#emailAddr").val() != "") {
					if (!$.validator.patterns._first_email.test($("#emailAddr").val())) {
						alert($.validator.messages._first_email);
						$("#emailAddr").focus();
						return false;
					}

					if ($("#email_choice").val() == "" && $("#directInput").val() == "") {
						alert("이메일 도메인을 선택하세요.");
						$("#email_choice").focus();
						return false;
					}

					if ($("#email_choice").val() == "0" && $("#directInput").val() == "") {
						alert("이메일 도메인을 입력하세요.");
						$("#directInput").focus();
						return false;
					}
				}

				if($("#directInput").val() != "") {
					if (!$.validator.patterns._last_email.test($("#directInput").val())) {
						alert($.validator.messages._last_email);
						$("#directInput").focus();
						return false;
					}
				}

				// 사업자등록증 업로드파일 확장자 체크
				if( $('input[name=uploadFile1]').val() != "" ){
			      var ext = $('input[name=uploadFile1]').val().split('.').pop().toLowerCase();
			  	  if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	     alert('등록할 수 없는 파일확장자입니다.');
			  	     $('input[name=uploadFile1]').val(""); // input file 파일명을 다시 지워준다.
			  	     return false;
			 	  }
			    }

			    // 통신판매신고증 업로드파일 확장자 체크
				if( $('input[name=uploadFile2]').val() != "" ){
			      var ext = $('input[name=uploadFile2]').val().split('.').pop().toLowerCase();
			  	  if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	     alert('등록할 수 없는 파일확장자입니다.');
			  	     $('input[name=uploadFile2]').val(""); // input file 파일명을 다시 지워준다.
			  	     return false;
			 	  }
			    }

			    // 구매안전이용확인증 업로드파일 확장자 체크
				if( $('input[name=uploadFile3]').val() != "" ){
			      var ext = $('input[name=uploadFile3]').val().split('.').pop().toLowerCase();
			  	  if($.inArray(ext, ['jpg', 'gif', 'png', 'pdf', 'jpeg']) == -1) {
			  	     alert('등록할 수 없는 파일확장자입니다.');
			  	     $('input[name=uploadFile3]').val(""); // input file 파일명을 다시 지워준다.
			  	     return false;
			 	  }
			    }

				// 사업자등록증 업로드파일 용량체크
				if( $('input[name=uploadFile1]').val() != "" ){
				    var fileSize = $('input[name=uploadFile1]')[0].files[0].size;
				    var maxSize = 5 * 1024 * 1024;	// 5MB

				    if(fileSize > maxSize){
				       alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다. ");
				        $('input[name=uploadFile1]').val("");
				        return false;
				     }
				}

				// 통신판매신고증 업로드파일 용량체크
				if( $('input[name=uploadFile2]').val() != "" ){
				    var fileSize = $('input[name=uploadFile2]')[0].files[0].size;
				    var maxSize = 5 * 1024 * 1024;	// 5MB

				    if(fileSize > maxSize){
				       alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다. ");
				        $('input[name=uploadFile2]').val("");
				        return false;
				     }
				}

				// 구매안전이용확인증 업로드파일 용량체크
				if( $('input[name=uploadFile3]').val() != "" ){
				    var fileSize = $('input[name=uploadFile3]')[0].files[0].size;
				    var maxSize = 5 * 1024 * 1024;	// 5MB

				    if(fileSize > maxSize){
				       alert("첨부파일 사이즈는 5MB 이내로 등록 가능합니다. ");
				        $('input[name=uploadFile3]').val("");
				        return false;
				     }
				}

				if('${fn:escapeXml(mode)}' == 'create') {
					if (!confirm("정보를 ${op:message('M00159')}")) {
		            	return false;
	            	}
	            } else if('${fn:escapeXml(mode)}' == 'edit') {
					if (!confirm("정보를 ${op:message('M00365')}")) {
		            	return false;
	            	}
	            }

			//}


			Common.removeNumberComma();
		});
	} catch(e) {
		alert(e.message);
	}

	$('.emailSel').on("change", function(){
		$('input[name=email2]').val($(this).val());
	});

	$('#remittanceType').on('change', function(){
		if($(this).val()==4){
			$('.remittanceDayTd').removeClass('hide');
			$('#remittanceDay').attr('name', 'remittanceDay');
		}else{
			$('.remittanceDayTd').addClass('hide');
			$('#remittanceDay').attr('name', '');
		}

	});

	$('input[name=shippingFlag]').on('change', function(){
		if($(this).val()=='Y'){
			$('.shipping_tr').removeClass('hide');
			$('.shipping').addClass('required');
		}else{
			$('.shipping_tr').addClass('hide');
			$('.shipping').removeClass('required');
		}


	});

	// 비밀번호 팝업
	$('.btn-password').on('click', function(e) {
		e.preventDefault();
		//Common.popup('/opmanager/seller/password-change-popup?sellerId=${fn:escapeXml(seller.sellerId)}', 'password_change_popup', 520, 330, 1);

    	// 지자체 관리자만 비밀번호 초기화 가능
    	if('${fn:escapeXml(adminRole)}' == 'SYS') {
    		alert("지자체관리자만 비밀번호 변경이 가능합니다.");
    		return false;
    	}

		if (confirm("초기화된 아이디는 비밀번호 재설정을 통해 정상 이용이 가능합니다. 비밀번호를 초기화 하시겠습니까?")) {
			let formData = new FormData();
			formData.append("sellerId", "${fn:escapeXml(seller.sellerId)}");
			formData.append("loginId", "${fn:escapeXml(seller.loginId)}");
			formData.append("password", "1111");
			$.ajax ({
				url	: "/opmanager/seller/init-seller-password",
				type	: "POST",
				//timeout : 3000, // 요청 제한 시간 안에 완료되지 않으면 요청을 취소하거나 error 콜백을 호출.(단위: ms)
				data  : formData, // 요청 시 포함되어질 데이터
				processData : false,
				contentType : false,
				success : function(resp, status, xhr) {
					if ("S" == resp) {
						alert(Message.get("초기화되었습니다."));
					} else {
						alert(Message.get("실패했습니다."));
					}
				},
				error	: function(xhr, status, error) {
					try {
						popupWindow.openMsg(Message.get("실패했습니다."));		// 실패했습니다.
					} catch (e) {
						alert(Message.get("실패했습니다."));		// 실패했습니다.
					}
				},
				complete : function(xhr, status) {
					Common.loading.hide();
				}
			});
		}
	});

});

	function openDaumPostcode() {

		var tagNames = {
			'newZipcode'			: 'post',
			/* 'zipcode' 				: 'post', */
			'zipcode1' 				: 'post1',
			'zipcode2' 				: 'post2',
		}

	    openDaumAddress(tagNames, function(data){
			$('#businessLocation').val(data.address);	// 사업장소재지
			//$('#addressDetail').prop("readonly", false);	// 상세주소 활성화
	    });

	}

	function fn_delete(){
		 if(document.forms.hasOwnProperty('impossible-form')) {
			alert('삭제 권한이 없습니다. (지자체 권한 필요)');
			Common.loading.hide();
			return false;
		 }

		 if(confirm(Message.get("M00196"))){	// 삭제하시겠습니까?
			 $("#deleteForm").submit();
		 }
	}

	function findMd(targetId) {
		Common.popup('/opmanager/seller/find-md?targetId=' + targetId, 'find_md', 720, 800, 1);
	}

	function clearMd(targetId) {
		var $target = $('#' + targetId);
		$target.val('');
		$target.closest('td').find('#mdName').val('');
	}

	// MD 검색 콜백
	function handleFindMdCallback(response) {
		var $target = $('#' + response.targetId);
		$target.val(response.userId);
		$target.closest('td').find('#mdName').val(response.userName);

	}

	// 아이디 생성
	function makeSellerId() {

		var locgovCode = "";	// 지자체 코드

		if(${userLocgovCodeDetails.id != null}){
			locgovCode = '${fn:escapeXml(userLocgovCodeDetails.id)}';
		}

		$.post('/opmanager/seller/makeSellerId', {"locgovCode": locgovCode}, function(response) {
			if(response.isSuccess && response.data) {
				$("#loginId").val(response.data);
			}
		});

	}

	// 이메일 생성
	function setEmail() {

		var emailChoice = $("#email_choice").val();

		// 이메일 도메인 직접입력
		if(emailChoice == '0') {
			$("#directInput").show();
			directInput.disabled =  false;
			$("#directInput").val('');
			$("#email").val($("#emailAddr").val()+'@');
		}
		// 이메일 초기화
		else if(emailChoice == '') {
			$("#directInput").hide();
			directInput.disabled =  true;
			$("#email").val('');
			$("#directInput").val('');
		}
		// 이메일 도메인 선택
		else {
			$("#directInput").hide();
			directInput.disabled =  true;
			$("#directInput").val('');

			// 이메일주소 + 이메일도메인 최종결합
			$("#email").val($("#emailAddr").val()+'@'+$("#email_choice").val());

		}

	}

	// 이메일 도메인 직접입력시 이메일 주소 최종완성
	$('#emailAddr').on('keyup', function(){
		if($("#email_choice").val() == '0') {
			$("#email").val($("#emailAddr").val()+'@'+$("#directInput").val());
		} else {
			$("#email").val($("#emailAddr").val()+'@'+$("#email_choice").val());
		}

		// 이메일 주소 지우면 초기화
		if($("#emailAddr").val() == '') {
			$("#email").val('');
			$("#directInput").val('');
		}
	});

	// 이메일 도메인 직접입력시 이메일 주소 최종완성
	$('#directInput').on('keyup', function(){
		$("#email").val($("#emailAddr").val()+'@'+$("#directInput").val());
	});

	// 파일 삭제
	function deleteFile(fileName, sellerId, fileType) {

		var message = Message.get("M00196");

		var param = {
			"fileName" : fileName,
			"sellerId" : sellerId,
			"fileType" : fileType
		};

		if (confirm(message)) {
			$.post("/opmanager/seller/deleteFile", param, function(resp){
				Common.responseHandler(resp, function(){

					alert(Message.get("M00205"));	// 삭제 되었습니다.

					location.reload();
				});
			});
		}

	}
	// 새로운 첨부파일을 선택할 경우 기존 파일 삭제처리 (화면에서 숨기고 수정시 삭제)
	$('input[name=uploadFile1]').change(function(){
	    $("#attachedFile1").hide();
	 })

	$('input[name=uploadFile2]').change(function(){
	    $("#attachedFile2").hide();
	 })

	$('input[name=uploadFile3]').change(function(){
	    $("#attachedFile3").hide();
	 })
	// 대표번호 직접입력으로 인한 추가기능
 	$('#manualNumberType1').change(function(){
		if(this.checked){
			$("#iptTelNum1").attr("name","telephoneNumber1");
			$("#iptTelNum1").removeClass("hidden").addClass("required").prop("disabled" , false);

			$("#selTelNum1").removeAttr("name").removeClass("required");
			$("#selTelNum1").addClass("hidden").prop("disabled" , true);
		}else{
			$("#selTelNum1").attr("name","telephoneNumber1");
			$("#selTelNum1").removeClass("hidden").addClass("required").prop("disabled" , false);

			$("#iptTelNum1").removeAttr("name").removeClass("required");
			$("#iptTelNum1").addClass("hidden").prop("disabled" , true);
		}
	})


	// 지역사랑상품권 제공전용 체크시 통신판매신고번호, 구매안전 이용확인번호 필수입력 체크설정 변경
//	$('input[name=localLove]').on('click', function() {
//		if(this.checked) {
//			/* $('#mailOrderNumber').removeClass("required");	// 통신판매신고번호 필수선택 제거
//			$("#statMark1").addClass("hidden");	// 필수입력 표시(*) 숨기기
//			$('#mailOrderNumber').val('');	// 통신판매신고번호 입력값 초기화
//			$('#mailOrderNumber').prop("readonly", true);	// 통신판매신고번호 비활성화
//			$('#buySafetyUseConfirmNumber').val('');	// 구매안전 이용확인번호 입력값 초기화
//			$('#buySafetyUseConfirmNumber').prop("readonly", true);	// 구매안전 이용확인번호 비활성화	 */
//
//			$('#mailOrderNumber').addClass("required");	// 통신판매신고번호 필수선택 추가
//			$("#statMark1").removeClass("hidden");	// 필수입력 표시(*) 보이기
//			$('#mailOrderNumber').prop("readonly", false);	// 통신판매신고번호 활성화
//		} else {
//			/* $('#mailOrderNumber').addClass("required");	// 통신판매신고번호 필수선택 추가
//			$("#statMark1").removeClass("hidden");	// 필수입력 표시(*) 보이기
//			$('#mailOrderNumber').prop("readonly", false);	// 통신판매신고번호 활성화
//			$('#buySafetyUseConfirmNumber').prop("readonly", false);	// 구매안전 이용확인번호 활성화 */
//
//			$('#mailOrderNumber').removeClass("required");	// 통신판매신고번호 필수선택 제거
//			$("#statMark1").addClass("hidden");	// 필수입력 표시(*) 숨기기
//			$('#mailOrderNumber').val('');	// 통신판매신고번호 입력값 초기화
//			$('#mailOrderNumber').prop("readonly", true);	// 통신판매신고번호 비활성화
//		}
//	});


</script>

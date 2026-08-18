<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="daum"	tagdir="/WEB-INF/tags/daum" %>
<%@ taglib prefix="module" 	tagdir="/WEB-INF/tags/modules" %>
<style>
.phone_number {width: 45px;}

</style>
			<h3>판매자정보</h3>

			<div class="location">
				<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
			</div>

			<form:form modelAttribute="seller" method="post">
				<input type="hidden" name="sellerId" value="${fn:escapeXml(seller.sellerId)}" />
				<input type="hidden" name="commissionRate" value="${fn:escapeXml(seller.commissionRate)}" />
				<input type="hidden" name="loginId" value="${fn:escapeXml(seller.loginId)}" />
				<input type="hidden" name="statusCode" value="${fn:escapeXml(seller.statusCode)}" />
				<div class="board_write">
					<table class="board_write_table">
						<colgroup>
							<col style="width:220px;" />
							<col style="" />
							<col style="width:220px;" />
							<col style="" />
						</colgroup>
						<tbody>
                            <tr>
                                <td class="label">지자체</td>
                                <td colspan="3">
                                    <div>
                                    	<c:out value="${seller.locgovNm}" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">아이디</td>
                                <c:choose>
	                                <c:when test="${isShadowSellerLogin}">
		                                <td colspan="3">
		                                    <div><c:out value="${seller.loginId}" /></div>
		                                </td>
	                                </c:when>
	                                <c:otherwise>
		                                <td>
		                                    <div><c:out value="${seller.loginId}" /></div>
		                                </td>
		                                <td class="label">비밀번호</td>
		                                <td>
		                                    <div><button type="button" class="btn btn-default btn-mini" id="btnOpenPwdPop" onClick="javascript:openChangePwd();">비밀번호 변경</button></div>
		                                </td>
	                                </c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <td class="label">휴대폰</td>
                                <td>
                                    <div><c:out value="${seller.phoneNumber}" /></div>
                                </td>
                                <td class="label">실명인증여부<br>(알림톡/문자 전송시 필요)</td>
                                <td>
                                    <div>
                                    	<span id="mberCi">${seller.mberCi == 'Y' ? '인증' : '미인증'}</span>
	                                    <c:if test="${!isShadowSellerLogin}">
	                                    	<button id="btnshowSciArea" type="button" onclick="javascript:showSciArea();" class="btn btn-default btn-mini" style="display:unset;margin-left:10px;">본인인증 등록/변경</a>
	                                    	<button type="button" onclick="javascript:delMberCi();" class="btn btn-default btn-mini" style="display:unset;margin-left:10px;">본인인증 삭제</a>
	                                    </c:if>
                                    </div>
                                </td>
                            </tr>
							<c:if test="${!isShadowSellerLogin}">
								<tr id="sciArea" style="display:none;">
									<td class="label">실명인증 할<br>이름</td>
									<td>
										<div class="flex_box gap-08 item-center">
											<input id="sciUserName" name="sciUserName" title="이름" class="input_txt _filter" type="text" autocomplete="off" value="" maxlength="50">
										</div>
									</td>
									<td class="label">실명인증 할<br>주민번호</td>
									<td>
										<div class="flex_box gap-08 item-center">
											<input id="sciJumin1" name="sciJumin1" title="주민등록번호 앞자리" class="input_txt _filter wd-150 _number" type="text" autocomplete="off" value="" maxlength="6" >
											<span class="wave">-</span>
											<input id="sciJumin2" name="sciJumin2" title="주민등록번호 뒷자리" class="input_txt _filter wd-150 _number" type="password" autocomplete="new-password" value="" maxlength="7" >
											<button type="button" class="btn btn-default btn-mini" id="btnSciCall" onClick="javascript:sciCallClick();"><span>실명인증</span></button>
										</div>
										<div style="padding-top:0px;">
											<span class="wave tip">* 실명 등록 이 필요한 경우 아래 URL 을 이용하시기 바랍니다.<br></span>
											&nbsp;&nbsp;&nbsp;바로가기 ▶ <a href="https://www.siren24.com/mysiren/customer/sir_g0201_01.jsp" target="_blank">https://www.siren24.com/mysiren/customer/sir_g0201_01.jsp</a>
										</div>
									</td>
								</tr>
							</c:if>
                            <tr>
                                <td class="label">이메일주소</td>
                                <td colspan="3">
                                    <div class="flex_box gap-08 item-center">
                                    	<!--
                                        <input id="" name="" title="" class="input_txt required _filter wd-150" type="text" value="korea" placeholder="">
                                        <span class="wave">@</span>
                                        <input id="" name="" title="" class="input_txt required _filter wd-150" type="text" value="naver.com" placeholder="">
                                        <select id="" name="" class="wd-150">
                                            <option value="">선택</option>
                                        </select>
                                        -->
                                        <form:input path="email" class="quater _email" title="담당자 이메일"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label" colspan="4">사업자 정보</td>
                            </tr>
							<tr>
			 					<td class="label">상호</td>
			 					<td>
			 						<div class="bold">
			 							<c:out value="${seller.companyName}" />
			 						</div>
			 					</td>
			 					<td class="label">대표자명</td>
			 					<td>
			 						<div>
			 							<c:out value="${seller.representativeName}" />
			 						</div>
			 					</td>
		 					</tr>
                            <tr>
                                <td class="label">사업자등록증</td>
                                <td>
                                    <div>
										<c:choose>
											<c:when test="${seller.fileNameCertificate1 != null && seller.fileNameCertificate1 != ''}">
												제출
											</c:when>
											<c:otherwise>
												미제출
											</c:otherwise>
										</c:choose>
                                    </div>
                                </td>
                                <td class="label">통신판매신고증</td>
                                <td>
                                    <div>
										<c:choose>
											<c:when test="${seller.fileNameCertificate2 != null && seller.fileNameCertificate2 != ''}">
												제출
											</c:when>
											<c:otherwise>
												미제출
											</c:otherwise>
										</c:choose>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">사업자등록번호</td>
                                <td>
                                    <div>
                                        <c:out value="${seller.businessNumber}" />
                                    </div>
                                </td>
                                <td class="label">통신판매신고번호</td>
                                <td>
                                    <div>
                                        <span>
                                            <c:out value="${seller.mailOrderNumber}" />
											<c:choose>
												<c:when test="${seller.adultItemYn == 'Y'}">
													( <span class="tip">성인</span> 답례품 판매가능 )
												</c:when>
												<c:otherwise>

												</c:otherwise>
											</c:choose>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">구매안전이용확인증</td>
                                <td>
                                    <div class="flex_box item-center">
										<c:choose>
											<c:when test="${seller.fileNameCertificate3 != null && seller.fileNameCertificate3 != ''}">
												제출
											</c:when>
											<c:otherwise>
												미제출
											</c:otherwise>
										</c:choose>
                                    </div>
                                </td>
                                <td class="label">구매안전 이용확인번호</td>
                                <td>
                                    <div>
                                        <c:out value="${seller.buySafetyUseConfirmNumber}" />
                                    </div>
                                </td>
                            </tr>
		 					<tr>
			 					<td class="label">업태</td>
			 					<td>
			 						<div class="bold">
			 							<c:out value="${seller.businessType}" />
			 						</div>
			 					</td>
			 					<td class="label">업종</td>
			 					<td>
			 						<div>
			 							<c:out value="${seller.businessItems}" />
			 						</div>
			 					</td>
		 					</tr>
                            <tr>
                                <td class="label"><span class="required_mark">*</span>대표번호</td>
                                <td>
                                    <div class="flex_box gap-08 item-center">
		 								<c:set var="tel_arr" value="${fn:split(seller.telephoneNumber, '-') }"/>
											<c:set var="first_tel" value="${fn:split(tel_arr[0], ',') }"/>
		 								<input type="text" id="iptTelNum1" value="${first_tel[0]}" maxlength="4" class="form-sm _number wd-150 telephoneNumber ${seller.manualNumberType ? '' : 'hidden'}" title="대표번호 첫번째자리" />
										<select name="telephoneNumber1" id="selTelNum1" class="wd-150 telephoneNumber ${seller.manualNumberType ? 'hidden':''}">
											<!-- 국번이 없는 경우를 위해 추가 -->
											<option value="미선택" ${code.value == null || code.value == '' ? 'selected="selected"' : '' }>미선택</option>
											<c:forEach items="${telCodes}" var="code">
												<option value="${fn:escapeXml(code.value)}" ${fn:escapeXml(code.value) == first_tel[0] ? 'selected="selected"' : '' }><c:out value="${fn:escapeXml(code.label)}" /></option>
											</c:forEach>
										</select>
										<span class="wave">-</span>
										<input type="text" name="telephoneNumber2" value="${tel_arr[1]}" maxlength="4" class="required form-sm _number wd-150" title="대표전화번호 가운데자리"/>
										<span class="wave">-</span>
										<input type="text" name="telephoneNumber3" value="${tel_arr[2]}" maxlength="4" class="required form-sm _number wd-150" title="대표전화번호 마지막자리"/>
                                    </div>

                                    <div class="checkbox">
										<form:checkbox path="manualNumberType" value="true" label="직접입력" />
									</div>
                                </td>
                                <td class="label">과세구분</td>
                                <td>
                                    <div>
										<c:choose>
											<c:when test="${seller.taxType == '1'}">
												과세
											</c:when>
											<c:when test="${seller.taxType == '2'}">
												비과세
											</c:when>
											<c:otherwise>

											</c:otherwise>
										</c:choose>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label"><span class="required_mark">*</span>사업장 소재지</td>
                                <td colspan="3">
                                    <div class="flex_box gap-08">
										<input type="hidden" name="newPost" value="">
										<input type="text" name="post" id="post" value="${fn:escapeXml(seller.post)}" class="required wd-150" title="${op:message('M00115')}  ${op:message('M00107')}" maxlength="5" class="one" required="required" readonly="readonly">
										<a href="javascript:;" onclick="openDaumPostcode()" class="btn btn-default btn-mini"> 주소찾기</a>

                                    </div>
                                    <div class="flex_box gap-08">
										<form:input type="text" path="businessLocation" title="${op:message('M01646')}" class="input_txt required _filter wd-500" readonly="true" /><br/>
										<form:input type="text" path="addressDetail" title="${op:message('M01647')}" class="input_txt required _filter wd-500" maxlength="100"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">상태</td>
                                <td colspan="3">
                                    <div>
                                        <c:out value="${seller.statusCode == '2' ?  '사용' : '중지'}" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">상품승인설정</td>
                                <td colspan="3">
                                    <div>
                                        <c:out value="${seller.itemApprovalType == '1' ? '관리자 승인' : '자동승인'}" />
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


					<h3 class="mt20 hidden">담당자정보</h3>
					<table class="board_write_table hidden">
						<colgroup>
							<col style="width:170px;" />
							<col style="" />
							<col style="width:170px;" />
							<col style="" />
						</colgroup>
						<tbody>
							<tr>
			 					<td class="label">담당자명<span class="require">*</span></td>
			 					<td>
			 						<div class="input_wrap col-w-7">
			 							<form:input path="userName" class="" title="담당자명" />
			 						</div>
			 					</td>
		 					</tr>
		 					<tr>
			 					<td class="label">담당자 휴대폰번호<span class="require">*</span></td>
			 					<td>
			 						<div class="input_wrap">
			 							<c:set var="phone_arr" value="${fn:split(seller.phoneNumber, '-') }"/>
										<select name="phoneNumber1" class="choice3" style="width:100px;">
											<c:forEach items="${phoneCodes}" var="code">
												<option value="${fn:escapeXml(code.value)}" ${fn:escapeXml(code.value) == phone_arr[0] ? 'selected="selected"' : '' }><c:out value="${fn:escapeXml(code.label)}" /></option>
											</c:forEach>
										</select> -
										<input type="text" name="phoneNumber2" value="${phone_arr[1]}" maxlength="4" class=" form-sm _number" title="휴대폰번호 가운데자리"/> -
										<input type="text" name="phoneNumber3" value="${phone_arr[2]}" maxlength="4" class=" form-sm _number" title="휴대폰번호 마지막자리"/>

			 						</div>
			 					</td>
			 					<td class="label">팩스번호<span class="require">*</span></td>
			 					<td>
			 						<div class="input_wrap col-w-7">
			 							<c:set var="fax_arr" value="${fn:split(seller.faxNumber, '-') }"/>
										<select name="faxNumber1" class="choice3" style="width:100px;">
											<c:forEach items="${telCodes}" var="code">
												<option value="${fn:escapeXml(code.value)}" ${fn:escapeXml(code.value) == fax_arr[0] ? 'selected="selected"' : '' }><c:out value="${fn:escapeXml(code.label)}" /></option>
											</c:forEach>
										</select> -
										<input type="text" name="faxNumber2" value="${fax_arr[1]}" maxlength="4" class=" form-sm _number" title="팩스 가운데자리"/> -
										<input type="text" name="faxNumber3" value="${fax_arr[2]}" maxlength="4" class=" form-sm _number" title="팩스 마지막자리"/>
			 						</div>
			 					</td>
		 					</tr>
		 				</tbody>
		 			</table>

				</div> <!--// board_write E-->

				<div class="item_list mt30 hidden">
					<h3><span>주문 담당자</span>
						<span class="text-info">
							* <strong>주문안내 SMS발송</strong> 시간을 설정한 경우 설정된 시간에 주문 담당자에게 주문안내 SMS발송합니다.
						</span>
					</h3>
					<div class="board_write">
						<table class="board_write_table">
							<caption><c:out value="${op:message('M00746')}" /></caption>
							<colgroup>
								<col style="width:170px;" />
								<col style="width:auto;" />
								<col style="width:170px;" />
								<col style="width:auto;" />
							</colgroup>
							<tbody>
								</tr>
								<tr>
									<td class="label"><c:out value="${op:message('M01632')}" /></td> <!-- 담당자명 -->
									<td colspan="3">
										<div>
											<form:input type="text" path="secondUserName" title="${op:message('M01632')}" class="full" />
										</div>
									</td>
								</tr>
								<tr>
									<td class="label"><c:out value="${op:message('M01633')}" /></td> <!-- 담당자 전화번호 -->
									<td>
										<div>
											<c:set var="tel_arr" value="${fn:split(seller.secondTelephoneNumber, '-') }"/>
											<select name="secondTelephoneNumber1" class="choice3">
												<c:forEach items="${telCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${fn:escapeXml(code.value) == tel_arr[0] ? 'selected="selected"' : '' }><c:out value="${fn:escapeXml(code.label)}" /></option>
												</c:forEach>
											</select> -
											<input type="text" name="secondTelephoneNumber2" value="${tel_arr[1]}" maxlength="4" class="form-sm _number" title="전화번호 가운데자리"/> -
											<input type="text" name="secondTelephoneNumber3" value="${tel_arr[2]}" maxlength="4" class="form-sm _number" title="전화번호 마지막자리"/>
										</div>
									</td>
									<td class="label"><c:out value="${op:message('M01634')}" /></td> <!-- 담당자 휴대폰번호 -->
									<td>
										<div>
											<c:set var="phone_arr" value="${fn:split(seller.secondPhoneNumber, '-') }"/>
											<select name="secondPhoneNumber1" class="choice3">
												<c:forEach items="${phoneCodes}" var="code">
													<option value="${fn:escapeXml(code.value)}" ${fn:escapeXml(code.value) == phone_arr[0] ? 'selected="selected"' : '' }><c:out value="${fn:escapeXml(code.label)}" /></option>
												</c:forEach>
											</select> -
											<input type="text" name="secondPhoneNumber2" value="${phone_arr[1]}" maxlength="4" class="form-sm _number" title="휴대폰번호 가운데자리"/> -
											<input type="text" name="secondPhoneNumber3" value="${phone_arr[2]}" maxlength="4" class="form-sm _number" title="휴대폰번호 마지막자리"/>
										</div>
									</td>
								</tr>
								<tr>
									<td class="label"><c:out value="${op:message('M01644')}" /></td> <!-- 담당자이메일 -->
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
														<c:set var="i">0<c:out value="${i}" /></c:set>
													</c:if>

													<option value="${timeValue}" ${seller.smsSendTime == timeValue ? 'selected="selected"' : '' }><c:out value="${timeSeperate}" />&nbsp;<c:out value="${time}" />시</option>

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
					<h3 class="mt50 fs24"><span>정산정보</span></h3>
					<div class="board_write">
						<table class="board_write_table">
							<caption>정산정보</caption>
							<colgroup>
								<col style="width:220px;" />
								<col />
								<col style="width:220px;" />
								<col />
							</colgroup>
							<tbody>
						        <tr>
									<td class="label">${op:message('M01655')}</td> <!-- 정산- 입급은행명 -->
									<td>
										<div>
											<!-- <form:input type="text" path="bankName" title="${op:message('M01655')}" class="full required" maxlength="20"/> -->
											<c:out value="${seller.bankName}" />
										</div>
									</td>
									<td class="label">${op:message('M00167')}명</td> <!-- 정산- 예금주명 -->
									<td>
										<div>
											<!-- <form:input type="text" path="bankInName" title="${op:message('M01663')}" class="full required" maxlength="10"/> -->
											<c:out value="${seller.bankInName}" />
										</div>
									</td>
						        </tr>
								<tr class="hidden">
									<td class="label">${op:message('M01653')}</td> <!-- 수수료율 -->
									<td colspan="3">
										<div>
											<c:out value="${seller.commissionRate}" /> %
										</div>
									</td>
						        </tr>
						        <tr class="hidden">
									<td class="label">${op:message('M01654')}</td> <!-- 정산주기 -->
									<td>
										<div>
											<c:choose>
												<c:when test="${seller.remittanceType == 1}">
													일정산
												</c:when>
												<c:when test="${seller.remittanceType == 2}">
													주정산
												</c:when>
												<c:when test="${seller.remittanceType == 3}">
													15일정산
												</c:when>
												<c:when test="${seller.remittanceType == 4}">
													월정산
												</c:when>
											</c:choose>
										</div>
									</td>

									<c:if test="${seller.remittanceType != 4}">
										<c:set var="tdHide" value="hide"/>
									</c:if>

									<td class="label remittanceDayTd ${fn:escapeXml(tdHide)}">정산일</td> <!-- 정산일 -->
									<td class="remittanceDayTd ${fn:escapeXml(tdHide)}">
										<div>
											<c:out value="${seller.remittanceDay}" />일
										</div>
									</td>
						        </tr>
						        <tr>
									<td class="label"><c:out value="${op:message('M01656')}" /></td> <!-- 정산- 입급계좌번호 -->
									<td colspan="3">
										<div>
											<p class="text-info text-sm hidden">* '-' 을 제외한 계좌번호를 입력해주세요.</p>
											<!-- <form:input type="text" path="bankAccountNumber" title="${op:message('M01656')}" class="full _number required" maxlength="20"/> -->
											<c:out value="${seller.bankAccountNumber}" />
										</div>
									</td>
						        </tr>
							</tbody>
						</table>
						<br>
						<div>
						    <ul class="list-bullet point">
						        <li>
						            이메일/대표번호/사업장 소재지 외 사업자 정보 및 정산정보의 수정이 필요하시면 관리자 문의 게시판을 통해 지자체 관리자에게 문의하세요.
						        </li>
						    </ul>
						</div>
						<div class="check-all-item-notice-label" style="display: none;">
							* 정산주기 상세설명<br>
							1. 일정산 : 구매확정일의 다음날 정산됩니다.<br>
							2. 주정산 : 구매확정일 기준 차주 월요일에 정산됩니다. (일 ~ 토요일까지의 구매확정주문들이 차주 월요일에 정산)<br>
							3. 15일주기 : 1 ~ 14일 구매확정주문 = 15일에 정산, 15 ~ 말일 구매확정주문 = 다음달 1일에 정산됩니다.<br>
							4. 월정산 : 월정산의 정산일날 구매확정된 주문은 다음달에 정산됩니다. (ex. 설정한 정산일이 매달1일, 주문의 구매확정이 1월 1일인경우 2월1일에 정산.)<br>
							* <strong>정산일이 휴일인 경우 다음날 정산됩니다.</strong>
						</div>
					</div>
				</div>


				<div class="board_write mt30 hidden">
					<h3><span>조건부배송비</span></h3> <!-- 조건부배송비 -->
					<div class="board_write">
						<table class="board_write_table">
							<caption>조건부배송비</caption>
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
											<form:input type="text" path="shipping" title="${op:message('M01658')}" class="full ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원
										</div>
									</td>
									<td class="label">무료배송 조건별 금액<span class="require">*</span></td> <!-- 판매자 조건부 배송비 무료배송 금액 -->
									<td>
										<div>
											<form:input type="text" path="shippingFreeAmount" title="${op:message('M01659')}" class="full ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원 이상 무료
										</div>
									</td>
						        </tr>
						        <tr class="shipping_tr ${fn:escapeXml(shipping_hide)}">
									<td class="label">${op:message('M01660')}<span class="require">*</span></td> <!-- 추가배송비 - 제주도 -->
									<td>
										<div>
											<form:input type="text" path="shippingExtraCharge1" title="${op:message('M01658')}" class="full ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원
										</div>
									</td>
									<td class="label">${op:message('M01661')}<span class="require">*</span></td> <!-- 추가배송비 - 도서산간 -->
									<td>
										<div>
											<form:input type="text" path="shippingExtraCharge2" title="${op:message('M01659')}" class="full ${fn:escapeXml(shipping_required)} amount _number_comma shipping _min_0" /> 원
										</div>
									</td>
						        </tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="board_write mt30 hidden">
					<h3><span>미니몰 상단</span></h3>
					<div>
						<form:textarea path="headerContent" cols="30" rows="20" style="width: 1085px" class="editor-content" title="미니몰 상단 내용" />
					</div>
				</div>
				<div class="btn_all btn_center">
					<div class="flex_box gap-08">
						<button id="btnSave" type="submit" class="btn btn-dark-gray btn-small"><c:out value="${op:message('M00101')}" /> <!-- 저장 --></button>
						<button type="button" class="btn btn-defualt btn-small" onclick="location.href='/seller/'">취소</button>
					</div>
				</div>
			</form:form>

<c:if test="${!isShadowSellerLogin}">
	<form id="sciCall" name="sciCall" method="post" action="/seller/user/sci-call" target="sciCallIframe">
		<input type="hidden" id="sci_jumin1" name="sci_jumin1" value="" />
		<input type="hidden" id="sci_jumin2" name="sci_jumin2" value="" />
		<input type="hidden" id="sci_name" name="sci_name" value="" />
	</form>

	<iframe name="sciCallIframe" style="display:none;" width="500px" height="500px"></iframe>

	<form name="AKCFrm" id="AKCFrm" method="post">
	    <input type="hidden" name="confmKey" value="U01TX0FVVEgyMDIyMTEyNTE0MTUzMTExMzI1OTc=" />
	    <input type="hidden" name="encodingType"   value=""   />
	    <input type="hidden" name="cssUrl" value="" />
	    <input type="hidden" name="resultType" value="4" />
	    <input type="hidden" name="currentPage" id="currentPage" value="1" />
	    <input type="hidden" name="countPerPage" value="1" />
	    <input type="hidden" name="keyword" id="keyword" value="" />
	</form>
</c:if>

<daum:address />
<module:smarteditorInit />
<module:smarteditor id="headerContent" />

<page:javascript>
<script type="text/javascript" src="/content/modules/jsencrypt.min.js"></script>
<script type="text/javascript">

var loginCheckId = '';
let crypt;

$(function(){
	Common.addNumberComma();
	$('#seller').validator(function() {
		Common.removeNumberComma();
		Common.getEditorContent("headerContent");
	});

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

	$('input[name=shippingFlag]').on('change', function(){
		if($(this).val()=='Y'){
			$('.shipping_tr').removeClass('hide');
			$('.shipping').addClass('required');
		}else{
			$('.shipping_tr').addClass('hide');
			$('.shipping').removeClass('required');
		}


	});


	// 비밀번호 변경팝업
	$('.btn-password').on('click', function(e) {
		e.preventDefault();
		Common.popup('/seller/edit/password-change-popup', 'password_change_popup', 370, 330, 1);
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
<c:if test="${!isShadowSellerLogin}">

	let popupType = "toolbar=no,width=700,height=405,top=150px,left=250px,directories=no,menubar=no,scrollbars=yes,location=no";
	let popup;

	const receiveMsg = async (e) => {
		if (e.data.hasOwnProperty('fnName')) {
			if (e.data.fnName == 'popupClose') {
				if (popup) {
					popup.close();
				}
			}
		}
	}

	$(function() {
		window.addEventListener("unload", (event) => {		// 화면 닫을 때 팝업 같이 닫기(화면 이동시 팝업만 남아있는 상황 방지)
			if (popup) {
				popup.close();
			}
		});

		window.addEventListener("message", receiveMsg, false);			// 팝업 통신용
	});

	$("#btnSave").on("click",function(){
		let mberCiTxt = '';
		try {
			mberCiTxt = $("#mberCi")[0].innerHTML;
		} catch (e) {
			mberCiTxt = '미인증';
		}

		if(mberCiTxt == '미인증') {
			alert("실명인증을 먼저 실행해 주세요.");
			$("#btnshowSciArea").focus();
			return false;
		}
	});

	function sciCallClick() {

		if(validation("sciUserName", "이름")) return false;
		if(validation("sciJumin1", "주민등록번호 앞자리")) return false;
		if(validation("sciJumin2", "주민등록번호 뒷자리")) return false;

		if (($("#sciJumin1").val().length + $("#sciJumin2").val().length) != 13) {
			alert("주민번호는 13자리로 입력하셔야 합니다.")
			return;
		}

		Common.loading.show();
		$.post("/seller/user/sci-enckey"
			, ''
			, function(response){
				Common.responseHandler(response, function(){
				    let publicKeyStr = response.data.publicKey;
					crypt = new JSEncrypt();
					crypt.setPrivateKey(publicKeyStr);

					$("#sciCall #sci_jumin1").val($("#sciJumin1").val());
					$("#sciCall #sci_jumin2").val($("#sciJumin2").val());
					$("#sciCall #sci_name").val($("#sciUserName").val());

					Common.loading.show();
					$("#sciCall").submit();
				});
			}
			, "json"
		)
		.always(function () {
			Common.loading.hide();		// 로딩 화면 제거
	    });
	}

	//실명인증 callbak
	function sciResponse(sciUser) {
		Common.loading.hide();
		if (sciUser.result == 1) {	// 성공
			if (confirm('실명인증 되었습니다. 인증 내용을 등록하시겠습니까?')) {
				// 판매자 실명인증 정보 수정
				Common.loading.show();
				$.post(
					"/seller/user/modSellerCi"
					, {'mberCi' : sciUser.ci
						, 'loginId' : '${fn:escapeXml(seller.loginId)}'}
					, function(response) {
						Common.responseHandler(response, function(){
							alert('인증되었습니다.');
						    $("#mberCi").empty();
						    $("#mberCi").append("인증");
						});
			    	}
				)
				.always(function () {
					Common.loading.hide();		// 로딩 화면 제거
			    });
				return;
			}
		} else if (sciUser.result == 3) {	// 없음
			alert("실명이 확인되지 않았습니다.\n실명 등록이 필요 합니다. \n실명등록 후 이용하시기 바랍니다.");
			//window.open("https://www.siren24.com/mysiren/customer/sir_g0201_01.jsp", "siren24");
		} else {
			alert("실명인증에 실패하셨습니다. [" + sciUser.message + "]");
		}

		// 로딩바 display none
		$("#loading-dimmed").css("display","none");
		$("#loading").css("display","none");
	}

	function validation(id, text) {
		if(!$("#"+id).val()) {
			alert(text+" 정보를 확인해 주세요.");
			$("#"+id).focus();
			return true;
		}
		return false;
	}

	//실명인증 삭제
	function delMberCi() {
		if (confirm('실명인증 정보를 삭제하시겠습니까?')) {
			// 판매자 실명인증 정보 수정
			Common.loading.show();
			$.post(
				"/seller/user/delSellerCi"
				, ''
				, function(response) {
					Common.responseHandler(response, function(){
					    alert('삭제되었습니다.');
					    $("#mberCi").empty();
					    $("#mberCi").append("미인증");
					});
		    	}
			)
			.always(function () {
				Common.loading.hide();		// 로딩 화면 제거
		    });
			return;
		}
	}

	//실명인증 영역 보이기
	function showSciArea() {
		$("#sciArea").removeAttr("style");
	}


	function openChangePwd() {
		popup = window.open('/seller/edit/change-password', 'sellerChgPwd', popupType);
	}

</c:if>
</script>
</page:javascript>

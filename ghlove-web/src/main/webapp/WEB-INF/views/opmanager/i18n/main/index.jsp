<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" uri="/WEB-INF/tlds/functions"%>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop"%>

<c:set var="prefixMonth" value="최근 " />

<!-- 관리자 권한 셋팅 -->
<c:choose>
	<c:when
		test="${(adminRole eq 'ROLE_ADMIN_7') or (adminRole eq 'ROLE_ADMIN_8') or (adminRole eq 'ROLE_ADMIN_11')}">
		<c:set var="adminRoleType" value="OFF" />
	</c:when>
	<c:when
		test="${(adminRole eq 'ROLE_ADMIN_5') or (adminRole eq 'ROLE_ADMIN_6') or (adminRole eq 'ROLE_ADMIN_10')}">
		<c:set var="adminRoleType" value="LOC" />
	</c:when>
	<c:when
		test="${(adminRole eq 'ROLE_ADMIN_1') or (adminRole eq 'ROLE_ADMIN_2')
					or (adminRole eq 'ROLE_ADMIN_3') or (adminRole eq 'ROLE_ADMIN_4')}">
		<c:set var="adminRoleType" value="SYS" />
	</c:when>
</c:choose>
<!-- 관리자 권한 셋팅 -->

<!-- 내용 -->
<div class="mainArea">
	<!-- 초기 렌더링 버튼 추가 -->
	<div class="cont-inner mt40" style="margin-top: 2rem;">
		<div style="display: flex;flex-direction: column;align-items: center;">
			<button type="button" class="btn btn-dark-gray btn-mini" id="callButton" onclick="mountedInit()">메인화면 데이터 조회</button>
			<p style="color:red;font-size: 14px;">※메인페이지 진입시 서버 부하를 줄이기 위해 클릭하여 가져오는것으로 변경되었습니다.</p>
		</div>
	</div>
	<!-- 본문 -->
	<div class="cont-inner mt40">
		<c:if test="${adminRoleType eq 'SYS'}">
			<ul class="member">
				<li class="member_box type01">
					<div class="title">
						<i class="icon-person01"></i> <span class="tit">전체회원</span>
					</div>
					<div class="count">
						<span class="num" id="all-subscribers-count">0</span>
					</div>
				</li>
				<li class="member_box type02">
					<div class="title">
						<i class="icon-person02"></i> <span class="tit">신규회원</span>
					</div>
					<div class="count">
						<span class="total">TODAY <span class="num"
							id="today-subscribers-count">0</span>
						</span>
					</div>
				</li>
			</ul>
		</c:if>
		<c:if test="${adminRoleType eq 'SYS'}">
			<div class="tit">* 알림서비스 동의 여부</div>
			<ul class="member" style="margin: 10px 0 25px;">

				<li class="member_box type02">
					<div class="title">
						<i class="icon-person-sms"></i> <span class="tit">국민비서 동의 회원</span>
					</div>
					<div class="count">
						<span class="total"> <span class="num"
							id="sms-count">0</span>
						</span>
					</div>
				</li>
				<li class="member_box type01">
					<div class="title">
						<i class="icon-person-share"></i> <span class="tit">SMS 동의 회원</span>
					</div>
					<div class="count">
						<span class="num" id="pbanc-count">0</span>
					</div>
				</li>
			</ul>
		</c:if>
		<c:if test="${adminRoleType eq 'SYS'}">
			<ul class="member" style="margin: 30px 0 25px;">
				<li class="member_box type01">
					<div class="title">
						<i class="icon-person-email"></i> <span class="tit">Email 동의 회원</span>
					</div>
					<div class="count">
						<span class="num" id="email-count">0</span>
					</div>
				</li>
				<li class="member_box type02">
					<div class="title">
						<i class="icon-person-kakao"></i> <span class="tit">카카오톡 알림톡 동의 회원</span>
					</div>
					<div class="count">
						<span class="total"> <span class="num"
							id="kakao-count">0</span>
						</span>
					</div>
				</li>
			</ul>
		</c:if>
		<c:if test="${adminRoleType eq 'OFF'}">
			<div class="list_box">
				<ul class="list type01">
					<li style="height: 730px; display: flex;"><a href="javascript:;"> <i class="icon-pencil"></i>
							<div class="info">
								<p class="tit">오프라인 접수현황</p>
								<p class="txt">
									TODAY<span class="num" id="offline-receipt-count"
										onclick='location.href="<c:url value='/opmanager/offgive/list'/>"'>0</span>
								</p>
							</div>
					</a></li>

				</ul>
			</div>
		</c:if>
		<c:if test="${adminRoleType eq 'SYS'}">
			<div class="list_box">
				<ul class="list type01">
					<c:if test="${adminRoleType eq 'SYS'}">
						<li><a href="javascript:;"> <i class="icon-setting"></i>
								<div class="info">
									<p class="tit">관리자 권한요청</p>
									<p class="txt">
										WAIT <span class="num" id="request-ready-count"
											onclick='location.href="<c:url value='/opmanager/manager-request/list'/>"'>0</span>
									</p>
								</div>
						</a></li>
					</c:if>
					<li><a href="javascript:;"> <i class="icon-pencil"></i>
							<div class="info">
								<p class="tit">오프라인 접수현황</p>
								<p class="txt">
									TODAY<span class="num" id="offline-receipt-count"
										onclick='location.href="<c:url value='/opmanager/offgive/list'/>"'>0</span>
								</p>
							</div>
					</a></li>

				</ul>
			</div>
		</c:if>
		<c:if test="${adminRoleType eq 'SYS' || (adminRoleType eq 'LOC')}">
			<div class="list_box">
				<ul class="list type02">
					<li class="bg01"><a href="javascript:;">
							<p class="tit">올해 기부현황 &#40;전체 현황&#41;</p>
							<c:if test="${adminRoleType eq 'LOC'}">
								<p class="tit">
									<span id="total-cntramt-locgov"></span>
								</p>
							</c:if>
							<p class="txt">
								<span class="num" id="this-year-cntramt-count">0</span>&nbsp;&#40;원&#41;
								&#40;<span class="num2" id="total-cntramt-count">0</span>&nbsp;&#40;백만원&#41;&#41;
							</p>
							<p class="txt">
								<span class="num" id="this-year-cntr-count">0</span>&nbsp;&#40;건&#41;
								&#40;<span class="num2" id="total-cntr-count">0</span>&nbsp;&#40;건&#41;&#41;
							</p>

					</a></li>

					<li class="bg02"><a href="javascript:;">
							<p class="tit">올해 답례품 현황 &#40;전체 현황&#41;</p>
							<c:if test="${adminRoleType eq 'LOC'}">
								<p class="tit">
									<span id="total-presentamt-locgov"></span>
								</p>
							</c:if>
							<p class="txt">
								<span class="num" id="this-year-presentamt-count">0</span>&nbsp;&#40;원&#41;
								&#40;<span class="num2" id="total-presentamt-count">0</span>&nbsp;&#40;백만원&#41;&#41;
							</p>
							<p class="txt">
								<span class="num" id="this-year-present-count">0</span>&nbsp;&#40;건&#41;
								&#40;<span class="num2" id="total-present-count">0</span>&nbsp;&#40;건&#41;&#41;
							</p>
					</a> </a></li>
				</ul>
			</div>
		</c:if>
	</div>
	<c:if test="${adminRoleType eq 'SYS' || (adminRoleType eq 'LOC')}">
		<c:if test="${adminRoleType eq 'SYS'}">
			<div class="bg_box">
				<div class="cont-inner">
					<div class="list_box">
						<ul class="list type03">
							<li><a href="javascript:;">
									<p class="title">QnA</p>
									<div class="info">
										<p class="tit">
											TOTAL <span class="num" id="total-qnaopen-count">0</span>
										</p>
										<p class="txt">
											답변대기 <span class="num" id="reply-qnaopen-count"
												onclick='location.href="<c:url value='/opmanager/qna-open/list'/>"'>0</span>건
										</p>
									</div>
							</a></li>
							<li><a href="javascript:;">
									<p class="title">1:1 문의</p>
									<div class="info">
										<p class="tit">
											TOTAL <span class="num" id="total-qna-count">0</span>
										</p>
										<p class="txt">
											답변대기 <span class="num" id="reply-qna-count"
												onclick='location.href="<c:url value='/opmanager/qna/list'/>"'>0</span>건
										</p>
									</div>
							</a></li>
							<!-- <li>
							<a href="javascript:;">
								<p class="title">민원신고</p>
								<div class="info">
									<p class="tit">TOTLAL 5</p>
								</div>
							</a>
						</li> -->
						</ul>
					</div>
				</div>
			</div>
		</c:if>
		<div class="cont-inner">
			<div class="list_box pb-85 mt40">
				<ul class="list type04">
					<li class="bd01">
						<p class="tit">기부금액</p>
						<div class="graph">
							<canvas id="amountChart"></canvas>
						</div>
						<div class="info-box">
							<dl>
								<dt>평균</dt>
								<dd>
									<span class="num" id="avg-amt-info">0</span> 원
								</dd>
								<dt>총계</dt>
								<dd>
									<span class="num" id="total-amt-info">0</span> 원
								</dd>
							</dl>
						</div>
					</li>
					<li class="bd02">
						<p class="tit">기부건수</p>
						<div class="graph">
							<canvas id="countChart"></canvas>
						</div>
						<div class="info-box">
							<dl>
								<dt>평균</dt>
								<dd>
									<span class="num" id="avg-count-info">0</span> 건
								</dd>
								<dt>총계</dt>
								<dd>
									<span class="num" id="total-count-info">0</span> 건
								</dd>
							</dl>
						</div>
					</li>
				</ul>
			</div>
		</div>
		<!--// currentState E-->
		<%-- <div class="cont-inner">
			<div class="list_box pb-85 mt40">
				<ul class="list type04">
					<li class="bd01">
						<p class="tit">기부금액</p>
						<div class="graph">
							<canvas id="amountChart"></canvas>
						</div>
						<div class="info-box">
							<dl>
								<dt>평균</dt>
								<dd>
									<span class="num" id="avg-amt-info">0</span> 원
								</dd>
								<dt>총계</dt>
								<dd>
									<span class="num" id="total-amt-info">0</span> 원
								</dd>
							</dl>
						</div>
					</li>
					<li class="bd02">
						<p class="tit">기부건수</p>
						<div class="graph">
							<canvas id="countChart"></canvas>
						</div>
						<div class="info-box">
							<dl>
								<dt>평균</dt>
								<dd>
									<span class="num" id="avg-count-info">0</span> 건
								</dd>
								<dt>총계</dt>
								<dd>
									<span class="num" id="total-count-info">0</span> 건
								</dd>
							</dl>
						</div>
					</li>
				</ul>
			</div>
		</div> --%>
	</c:if>
<!-- 2023-02-21 추가 -->
<c:if test="${adminRoleType eq 'SYS'}">
<!-- searchForm -->
<%-- <form:form modelAttribute="searchParam" method="post" id="searchForm"> --%>
	<div class="board_write">
		<table class="board_write_table" summary="">
			<colgroup>
				<col style="width:220px;">
			</colgroup>
			<tbody>

	    <%--  <tr>
	        <td class="label">기부 지자체</td>
	        <td colspan="3">
	          <div class="flex_box gap-08">
	             <form:select path="shWdr" class="wd-150" onChange="wdrChange(this.value)">
	                 <form:option value="">-시·도 선택-</form:option>
	                 <c:forEach items="${wdr}" var="wdr">
	                   <form:option value="${wdr.id}" label="${wdr.label}" />
	                 </c:forEach>
	             </form:select>

	             <form:select path="shLocgovCode" class="wd-150">
	                 <option value="">-시·군·구-</option>
	             </form:select>

	          </div>
	        </td>
	      </tr> --%>

				<tr>
					<td class="label">기부일자</td>
					<td colspan="2">
						<div>
							<%-- <span class="datepicker">
								<form:input path="shCntrDeStart" cssClass="datepicker optional " title="${op:message('M00507')}" />

	                   <!--  </span>
	                    <span class="wave">~</span>
	                    <span class="datepicker">
	                        <form:input path="shCntrDeEnd" cssClass="datepicker optional " title="${op:message('M00509')}" />
	                    </span>--> --%>

							<span class="datepicker">
								<input id="shCntrDeStart" name="shCntrDeStart" class="datepicker optional" title="시작일" type="text">
							</span>
						</div>
					</td>

					<td class="label">콜수 등록</td>
					<td colspan="2">
						<div>
							<button type="button" class="btn btn-dark-gray btn-mini" onclick="insertCall()">콜수등록</button>
						</div>
					</td>

				</tr>

			</tbody>
		</table>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="searchClear()">초기화</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="search()">검색</button>
	           <!--  <button type="submit" class="btn btn-dark-gray btn-mini">검색</button> -->
			</div>
		</div>
	</div>
<%-- </form:form> --%>
<!-- //searchForm -->
	<div style="display: flex;">
			<div class="board_list" style="display: flex; flex-flow: column;">
				<table class="board_list_table" summary=""
					style="width: 650px; float: left; margin-right: 50px; border-radius: 10%;">
					<thead>
						<tr>
							<th colspan="2">기부 현황</th>
						</tr>
					</thead>
					<colgroup>
         				<col style="width:50%;">
          				<col style="width:50%;">
          			</colgroup>
					<tbody>
						<tr>
							<td>총 기부 건수</td>
							<td ><span class="num" id="total-cntr-info">0</span>건</td>
						</tr>
						<tr>
							<td >온라인 일반기부 건수</td>
							<td ><span class="num" id="online-cntr-info">0</span>건</td>
						</tr>
						<tr>
							<td >온라인 지정기부 건수</td>
							<td ><span class="num" id="online-prj-cntr-info">0</span>건</td>
						</tr>
						<tr>
							<td >오프라인 일반기부 건수</td>
							<td ><span class="num" id="offline-cntr-info">0</span>건</td>
						</tr>
						<tr>
							<td >오프라인 지정기부 건수</td>
							<td ><span class="num" id="offline-prj-cntr-info">0</span>건</td>
						</tr>
						<tr>
							<td >총 기부 금액</td>
							<td ><span class="num" id="total-cntramt-info">0</span>원</td>
						</tr>
						<tr>
							<td >온라인 일반기부 금액</td>
							<td ><span class="num" id="online-cntramt-info">0</span>원</td>
						</tr>
						<tr>
							<td >온라인 지정기부 금액</td>
							<td ><span class="num" id="online-prj-cntramt-info">0</span>원</td>
						</tr>
						<tr>
							<td >오프라인 일반기부 금액</td>
							<td ><span class="num" id="offline-cntramt-info">0</span>원</td>
						</tr>
						<tr>
							<td >오프라인 지정기부 금액</td>
							<td ><span class="num" id="offline-prj-cntramt-info">0</span>원</td>
						</tr>
					</tbody>
				</table>
				<table class="board_list_table" summary=""
					style="width: 650px; float: left; margin-right: 50px; border-radius: 10%;">
					<thead>
						<tr>
							<th colspan="2">답례품 현황</th>
						</tr>
					</thead>
					<colgroup>
         				<col style="width:50%;">
          				<col style="width:50%;">
          			</colgroup>
					<tbody>

						<tr>
							<td>등록된 답례품 건 수</td>
							<td><span class="num"id="present">0</span>건</td>
						</tr>
						<tr>
							<td>답례품 신청 건 수</td>
							<td><span class="num"id="total-present-info">0</span>건</td>
						</tr>
						<tr>
							<td>답례품 신청 금액</td>
							<td><span class="num"id="total-presentamt-info">0</span>원</td>
						</tr>
					</tbody>
				</table>
				<table class="board_list_table" summary=""
					style="width: 650px; float: left; margin-right: 50px; border-radius: 10%;">
					<thead>
						<tr>
							<th colspan="2">콜 수 현황</th>
						</tr>
					</thead>
					<colgroup>
         				<col style="width:50%;">
          				<col style="width:50%;">
          			</colgroup>
					<tbody>
						 <tr>
							<td style="width:360.38px;">대국민 건 수</td>
							<td><span  class="call_kookmin"id="call_kookmin">0</span>건</td>
						</tr>
						<tr>
							<td>지자체 건 수</td>
							<td><span class="call_lov">0</span>건</td>
						</tr>
						<tr>
							<td>답례품 건 수</td>
							<td><span class="call_giver">0</span>건</td>
						</tr>
						<tr>
							<td>농협 건 수</td>
							<td><span class="call_nhbank">0</span>건</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
			<div style="display: flex; margin-top:10px">
				<div id="gibuCopy" style="display: flex; flex-direction: column; border: 1px solid; height: 100% ; padding: 10px; margin-right: 100px; line-height: 2em;">
					<span style="display: flex; padding:7px; border-top: 1px solid black; border-bottom: 1px solid black;" class="date"></span>
					<span style="line-height: 10px;">&#12288;</span>
					<span>&nbsp;&#128230;&nbsp;답례품 등록: <!--<span id="numGift"></span>지자체&nbsp;&#40;미등록<span id="unGift"></span>개&#41&nbsp;--><span id=present1></span>개</span>
			<!--  	<span>- 미등록 지자체:&nbsp;<span id="giftData"></span></span>	-->
					<span>&nbsp;&#128101;&nbsp;회원가입:&nbsp;<span class="num" id="all-user-count"></span></span>
					<span style="line-height: 15px;">&#12288;</span>
					<div>
						<span>&nbsp;&#128200;&nbsp;기부건수: &nbsp;<span class="num" id="total-cntr">0</span>건</span>
						<ul style="margin-left:3%;">
							<li>
								<span>&nbsp;&middot;&nbsp;시스템 일반기부:&nbsp;<span class="num" id="online-cntr">0</span>건</span>
							</li>
							<li>
								<span>&nbsp;&middot;&nbsp;시스템 지정기부:&nbsp;<span class="num" id="online-prj-cntr">0</span>건</span>
							</li>
							<li style="line-height: 10px;">&#12288;</li>
							<li>
								<span>&nbsp;&middot;&nbsp;오프라인 일반기부:&nbsp;<span class="num" id="offline-cntr">0</span>건</span>
							</li>
							<li>
								<span>&nbsp;&middot;&nbsp;오프라인 지정기부:&nbsp;<span class="num" id="offline-prj-cntr">0</span>건</span>
							</li>
						</ul>
					</div>
					<span style="line-height: 15px;">&#12288;</span>
					<div>
						<span>&nbsp;&#128176;&nbsp;기부금:&nbsp;<span class="num" id="total-cntramt">0</span>백만원</span>
						<ul style="margin-left:3%;">
							<li>
								<span>&nbsp;&middot;&nbsp;시스템 일반기부금:&nbsp;<span class="num" id="online-cntramt">0</span>백만원</span>
							</li>
							<li>
								<span>&nbsp;&middot;&nbsp;시스템 지정기부금:&nbsp;<span class="num" id="online-prj-cntramt">0</span>백만원</span>
							</li>
							<li style="line-height: 10px;">&#12288;</li>
							<li>
								<span>&nbsp;&middot;&nbsp;오프라인 일반기부금:&nbsp;<span class="num" id="offline-cntramt">0</span>백만원</span>
							</li>
							<li>
								<span>&nbsp;&middot;&nbsp;오프라인 지정기부금:&nbsp;<span class="num" id="offline-prj-cntramt">0</span>백만원</span>
							</li>
						</ul>
					</div>
					<span style="line-height: 15px;">&#12288;</span>
					<span>&nbsp;&#127873;&nbsp;답례품 신청:&nbsp;<span class="num"id="total-present">0</span>건</span>
					<span>&nbsp;&#128184;&nbsp;답례품 신청 금액:&nbsp;<span class="num"id="total-presentamt">0</span>백만원</span>
					<span style="line-height: 15px;">&#12288;</span>
					<span>&nbsp;&#128316;&nbsp;기부 증감액:&nbsp;<span class="num"id="totalCntrCal">0</span>건&nbsp;<span class="num"id="totalCntrAmtCal">0</span>백만원</span>
				</div>

				<div id="callCopy" style="display: flex;  flex-direction: column; border: 1px solid; height: 200px; padding: 10px; line-height: 1.8em;" id="callResult">
					<span style="display: flex">일일 콜 현황</span>
					<span>일일 총 콜수:<span class="call_total">0</span>건</span>
					<span>대국민  콜수:<span class="call_kookmin">0</span>건</span>
					<span>지자체 콜수:<span class="call_lov">0</span>건</span>
					<span>답례품 제공자 콜수:<span class="call_giver">0</span>건</span>
					<span>농협 관리자 콜수:<span class="call_nhbank">0</span>건</span>
				</div>


			</div>
					<button id="copyGibu" onclick="copyGibu()" style="margin-right: 100px; width:auto; margin-top:10px">기부현황 복사</button>
					<button id="copyCall" onclick="copyCall()" style="width: auto; margin-left:155px">일일콜수 현황 복사</button>
			</div>



		</div>

	</c:if>
</div>


<input type="hidden" id="serverIp" value="${fn:escapeXml(serverIp)}"/>

<!--// mainArea E-->
<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script> -->
<script type="text/javascript" src="/content/modules/chart.min.js"></script>
<script type="text/javascript" src="/content/modules/op.main.js"></script>
<script type="text/javascript" src="/content/popup/popup.js?v=260803"></script>
<script type="text/javascript">
	var adminRole = "${fn:escapeXml(adminRole)}";
	var adminRoleType = "${fn:escapeXml(adminRoleType)}";

	if (adminRoleType == "SYS" || adminRoleType == "LOC") {
		var context = document.getElementById('amountChart').getContext('2d');
		var context2 = document.getElementById('countChart').getContext('2d');
	}

	$(function() {
		// 2026-06-05 인천행정체제 개편으로 인한 중구동구서구 관리자 메시지 표시
		//var showIncheonPopup = "${fn:escapeXml(showIncheonPopup)}";
		//if (showIncheonPopup != null && showIncheonPopup == "Y") {
			//// const today = new Date();
			//// const target = new Date(today.getFullYear(), 5, 13); // 월은 0부터 시작 → 5 = 6월 / 06월 12일 23시 59분 59초까지(06.12 00:00:00)
			//// if(today <= target) {
			//	var d = {
			//			width : 450,
			//			height : 440,
			//			subject : '기관관리자 안내사항',
			//			// content : '2026년 7월 1일 인천시 행정체제 개편에 따라 해당 기관 답례품 주문이 6월 30일까지 배송이 완료되도록 수시로 모니터링 바라며, 답례품 제공자가 신속하게 처리할 수 있도록 독려 바랍니다.',
			//			content : '<img style="margin-top:10px; max-width: 100%; height: auto" src="/content/images/popup/area_change_manager.png" alt="2026년 7월 1일 인천시 행정체제 개편에 따라 해당 기관 답례품 주문이 6월 30일까지 배송이 완료되도록 수시로 모니터링 바라며, 답례품 제공자가 신속하게 처리할 수 있도록 독려 바랍니다."/>',
			//			managerNoticeId : 9999
			//	};

			//	pop.openInfoPopup(d, 1, 10);
			// }
		//}

		// 설문조사 팝업 표시
		if (adminRoleType == "SYS" || adminRoleType == "LOC") {
			var showPopup = "${fn:escapeXml(showPopup)}";
			if(showPopup != null && showPopup == "Y") { //

				var d = {
						width : 390,
						height : 550,
						subject : '고향사랑e음 설문조사',
						//content : '내용',
						content : '<a href="https://www.ilovegohyang.go.kr/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607300918074101.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
						//content : '<a href="http://localhost:3000/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607281714265325.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
						//content : '<a href="http://152.99.104.8/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607281714265325.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
						managerNoticeId : 9990
				};

				pop.openQustnrPopup(d, 1, 10);
			}
		}

		// 엑셀암호화 팝업 표시
		if (adminRoleType == "SYS" || adminRoleType == "LOC") {
			var showPopup2 = "${fn:escapeXml(showPopup2)}";
			if(showPopup2 != null && showPopup2 == "Y") { //

				var d = {
						width : 550,
						height : 570,
						subject : '고향사랑e음 안내사항',
						//content : '내용',
						content : '<img src="/upload/popup/264/202608031449447664.png" style="width:100%;height:440px;padding:0px;object-fit:contain;" alt="보안강화 안내">', //운영
						//content : '<img src="/upload/popup/264/202608031437075475.png" style="width:100%;height:440px;padding:0px;object-fit:contain;" alt="보안강화 안내">', // 로컬
						//content : '<a href="http://localhost:3000/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607281714265325.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
						//content : '<a href="http://152.99.104.8/qustnr/detail_srvy.html?qustnrSn=7" target="_blank"><img src="/upload/popup/262/202607281714265325.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
						managerNoticeId : 9991
				};

				pop.openInfoPopup2(d, 10, 11);
			}
		}

//	setMainInfo();
//	setBoardInfo();
	//setMainTableInfo();			// 세팅된 초기 기부일자가 없어서 페이지 오픈시 주석처리
//	if (adminRoleType == "SYS" || adminRoleType == "LOC") {
//		getAmountChart();
//		getCountChart();
//		setMainChart();
//		pop.openNotice('O');
//		dateFormat();
//		mainGift();

//			const today = new Date();
//			const target = new Date(today.getFullYear(), 5, 13); // 월은 0부터 시작 → 5 = 6월 / 06월 12일 23시 59분 59초까지(06.12 00:00:00)
//			if(today <= target) {
//				var d = {
//						width : 340,
//						height : 545,
//						subject : '고향사랑e음 설문조사',
//						//content : '내용',
//						content : '<a href="https://www.ilovegohyang.go.kr/qustnr/detail_srvy.html?qustnrSn=5" target="_blank"><img src="/upload/popup/185/202505291646271086.png" style="width:100%;height:440px;padding:0px;object-fit:contain;"></a>',
//						managerNoticeId : 999
//				};

//				pop.openQustnrPopup(d, 1, 10);
//			}
//		}
	});

	// 초기 렌더링시 호출했던 함수 현재는 버튼으로 변경하여 서버 부하를 줄임
	// 원복시 바로 위 jquery 함수 주석 해체
	function mountedInit () {
		const btn = document.getElementById('callButton');

		btn.disabled = true;
		try {
			setMainInfo();
			setBoardInfo();
			if (adminRoleType == "SYS" || adminRoleType == "LOC") {
				getAmountChart();
				getCountChart();
				setMainChart();
				pop.openNotice('O');
				dateFormat();
				mainGift();
				getNotificationAgreeMemberCount();
			}
		} catch (error) {
			// console.error('데이터는 가져오는 도중 문제가 발생했습니다.');
		}
	}

	// 광고성 정보 수신 동의 회원 / SMS 동의 회원 / Email 동의 회원 / 카카오톡 알림톡 동의 회원 수
	function getNotificationAgreeMemberCount () {
		$.post('/common/opmanager/main-notification-agree-member', null, function(resp) {
			console.table(resp);
			if(resp.data == undefined) {
				return;
			}

			if(resp.isSuccess) {
				$('#pbanc-count').html(Common.numberFormat(resp.data.pbanc_count));
				$('#sms-count').html(Common.numberFormat(resp.data.sms_count));
				$('#email-count').html(Common.numberFormat(resp.data.email_count));
				$('#kakao-count').html(Common.numberFormat(resp.data.kakao_count));
			} else {
				return;
			}


		}, 'json');
	}

	// 전체회원/금일가입회원/관리자권한요청(대기)/오프라인접수현황/총기부현황/총답례품현황 카운트
	function setMainInfo() {
		$.post('/common/opmanager/main-info', null, function(resp) {

			if (resp.data == undefined) {
				return;
			}

			$.each(resp.data, function(i, state) {
				$object = $('span#' + state.id + "-count");

				if ($object.size() > 0) {
					$object.html(Common.numberFormat(state.count));
				}

				$object2 = $('span#' + state.id + "-locgov");

				if ($object2.size() > 0) {
					$object2.html(state.label);
				}

			});

		}, 'json');
	}

	// 게시판 현황
	function setBoardInfo() {
		$.post('/common/opmanager/main-boardInfo', null, function(resp) {

			if (resp.data == undefined) {
				return;
			}

			$.each(resp.data, function(i, state) {
				$object = $('span#' + state.id + "-count");

				if ($object.size() > 0) {
					$object.html(Common.numberFormat(state.count));
				}

			});

		}, 'json');
	}

	// 기부금액 chart
	function getAmountChart() {
		var amountLabel = [];
		var amountList = [];
		$.post('/common/opmanager/main-amountChart', null, function(resp) {

			if (resp.data == undefined) {
				return;
			}

			$.each(resp.data, function(i, state) {
				//    $object = $('span#' + state.id + "-count");

				amountLabel.push(state.cntrDay);
				amountList.push(state.cntrAmt);

			});

			var amountChart = new Chart(context, {
				type : 'line',
				data : {
					labels : amountLabel,
					datasets : [ {
						lineTension : 0,
						data : amountList,
						borderColor : "#004CCE",
						fill : false
					} ]
				},
				options : {
					plugins : {
						legend : {
							display : false
						},
						title : {
							display : true,
							text : '기부금액(단위 천원)'
						}
					},
					scales : {
						y : {
							suggestedMin : 1000,
						// suggestedMax: 100000,
						}
					}
				}
			});

		}, 'json');
	}

	// 기부건수 chart
	function getCountChart() {
		var amountLabel = [];
		var amountList = [];
		$.post('/common/opmanager/main-countChart', null, function(resp) {

			if (resp.data == undefined) {
				return;
			}

			$.each(resp.data, function(i, state) {
				//    $object = $('span#' + state.id + "-count");

				amountLabel.push(state.cntrDay);
				amountList.push(state.cntrAmt);

			});

			var countChart = new Chart(context2, {
				type : 'line',
				data : {
					labels : amountLabel,
					datasets : [ {
						lineTension : 0,
						data : amountList,
						borderColor : "#09C2C7",
						fill : false
					} ]
				},
				options : {
					plugins : {
						legend : {
							display : false
						},
						title : {
							display : true,
							text : '기부건수(단위 건)'
						}
					},
					scales : {
						y : {
							suggestedMin : 0,
						// suggestedMax: 100000,
						}
					}
				}
			});

		}, 'json');
	}

	// 기부금액/기부건수 일주일간 총계 및 평균
	function setMainChart() {
		$.post('/common/opmanager/main-chartInfo', null, function(resp) {

			if (resp.data == undefined) {
				return;
			}

			$.each(resp.data, function(i, state) {
				$object = $('span#' + state.id + "-info");

				if ($object.size() > 0) {
					$object.html(Common.numberFormat(state.count));
				}

			});

		}, 'json');
	}

	//2023-02-21 대시보드 관련 fn 추가

	//set 하단 테이블 정보
	function setMainTableInfo() {

		$.post('/common/opmanager/main-search', null, function(resp) {

			if (resp.data == undefined) {
				return;
			}

			$.each(resp.data, function(i, state) {
				$object = $('span#' + state.id + "-info");

				if ($object.size() > 0) {

					$object.html(Common.numberFormat(state.count));

				}

			});

		}, 'json');
	}

	// 관리자 하단 테이블 정보 검색
	function search() {

		var strStartDate = $("#shCntrDeStart").val();

		let startDate;
		if(strStartDate && strStartDate.length == 8 ) {
			startDate = new Date(strStartDate.substr(0, 4), strStartDate.substr(4, 2), strStartDate.substr(6, 2));

		}

		if (strStartDate && startDate) {		// 날짜에 값이 있을때에만

			$.post('/common/opmanager/main-search',{
				shCntrDeStart : strStartDate,
			}, function(resp) {

				if (resp.data == undefined) {
					return;
				}

				$.each(resp.data, function(i, state) {
					$object = $('span#' + state.id + "-info");


					if ( $object.size() > 0) {
					$object.html(Common.numberFormat(state.count));
					}

				});


			}, 'json');

			$.post('/common/opmanager/get-user',{
				shCntrDeStart : strStartDate,
			}, function(resp) {
				$("#all-user-count").html(Common.numberFormat(resp.data.userAmt));
				$("#total-cntr").html(Common.numberFormat(resp.data.totalcntr));
				$("#online-cntr").html(Common.numberFormat(resp.data.onlinecntr));
				$("#online-prj-cntr").html(Common.numberFormat(resp.data.onlineprjcntr));
				$("#offline-cntr").html(Common.numberFormat(resp.data.offlinecntr));
				$("#offline-prj-cntr").html(Common.numberFormat(resp.data.offlineprjcntr));
				$("#total-cntramt").html(Math.round(parseInt(resp.data.totalcntramt)/1000000).toLocaleString());
				$("#online-cntramt").html(Math.round(parseInt(resp.data.onlinecntramt)/1000000).toLocaleString());
				$("#online-prj-cntramt").html(Math.round(parseInt(resp.data.onlineprjcntramt)/1000000).toLocaleString());
				$("#offline-cntramt").html(Math.round(parseInt(resp.data.offlinecntramt)/1000000).toLocaleString());
				$("#offline-prj-cntramt").html(Math.round(parseInt(resp.data.offlineprjcntramt)/1000000).toLocaleString());
				$("#total-present").html(Common.numberFormat(resp.data.totalpresent));
				$("#total-presentamt").html(Math.round(parseInt(resp.data.totalpresentamt)/1000000).toLocaleString());
				$("#totalCntrAmtCal").html(Math.round(parseInt(resp.data.totalCntrAmtCal)/1000000).toLocaleString());
				$("#totalCntrCal").html(Common.numberFormat(resp.data.totalCntrCal));
			}, 'json');

			$.post('/common/opmanager/call-search',{shCntrDeStart : strStartDate,}, function(resp){
				$(".call_kookmin").html(resp.data.callKookmin)
				$(".call_lov").html(resp.data.callLov)
				$(".call_giver").html(resp.data.callGiver)
				$(".call_nhbank").html(resp.data.callNhbank)
				$(".call_total").html(resp.data.callTotal)


			},'json');

			$.get('/common/opmanager/gift-amount',{shCntrDeStart : strStartDate,},function(resp){
				//console.log(resp);
				$("#present").html(Common.numberFormat(resp.data));
				$("#present1").html(Common.numberFormat(resp.data));
			});
		}

	}


	// 날짜 포맷
	function dateFormat(){
		let day =["일","월","화","수","목","금","토"]
		let today = new Date();
		let dateFormat =" [ 고향사랑 기부 현황 ] " + (today.getMonth()+1) + "." + today.getDate() + " " + day[today.getDay()]  +" 18:00시 기준"
		$(".date").html(dateFormat);

	}

	//초기화
	function searchClear() {
	    $("#shWdr").val("");
	    $("#shCntrDeStart").val("");


	    search();
	}

	function insertCall(){
		var url = '/opmanager/popup/insertCall';
		var popupName ="콜 수 등록"
		Common.popup(url, popupName, 1200, 200, 1,0, 0);
	}

	function copyGibu(){
		const valOfDIV = document.getElementById("gibuCopy").innerText;
		const textArea = document.createElement('textarea');
		document.body.appendChild(textArea);
		textArea.value = valOfDIV;
		textArea.select();
		document.execCommand('copy');
		document.body.removeChild(textArea);
		this.alert('복사 완료');
	}

	//카피 콜 수
	function copyCall( ){
		const valOfDIV = document.getElementById("callCopy").innerText;
		const textArea = document.createElement('textarea');
		document.body.appendChild(textArea);
		textArea.value = valOfDIV;
		textArea.select()
		document.execCommand('copy');
		document.body.removeChild(textArea);
		this.alert('복사 완료');
	}
	// 답례품 지자체
	function mainGift(){
		$.get("common/opmanager/gift-search",function(resp){
			let str =[resp.data]
			let arr = String(str).split(" ");
			let len = resp.data.length
			let gift = 243 - len
			$("#giftData").html(arr);
			$("#numGift").html(gift);
			$("#unGift").html(len);
		})
	}






</script>
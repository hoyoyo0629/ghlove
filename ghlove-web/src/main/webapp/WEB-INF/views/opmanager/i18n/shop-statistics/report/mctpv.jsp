<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">지자체별 현황</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span>지자체별 현황</span></h3>
	<form:form modelAttribute="statisticsParam" method="post" >
		<form:input type="hidden" path="tabId" value="1"/>

		<div class="board_write mt10">
			<table class="board_write_table" summary="지자체별 현황">
		            <colgroup>
		                <col style="width:220px;">
		                <col>
		                <col style="width:220px;">
		                <col>
		            </colgroup>
		            <tbody>
		                <tr>
		                    <td class="label">기간</td>
		                    <td>
		                        <div class="flex_box gap-08" style="align-items: center;">
		                            <form:select path="shYear" title="${op:message('년도')}" class="wd-150" onChange="monthChange(this.value, ${statisticsParam.shMonth})">
		                                <c:forEach items="${crtrYear}" var="data" varStatus="i">
											<option value="${data.crtrYr}">${data.crtrYr}</option>
										</c:forEach>
		                            </form:select>
		                            년
		                            <form:select path="shMonth" title="${op:message('월')}" class="wd-150">
		                                	<c:forEach var="i" begin="1" end="12" step="1">
		                                		<c:if test="${i < nowMonth}">
													<option value="${i}">${i}</option>
												</c:if>
											</c:forEach>
		                            </form:select>
		                            월
		                        </div>
		                    </td>
		                </tr>
		            </tbody>
		        </table>

		</div> <!-- // board_write -->
		<div class="btn_all btn_left">
			<ul class="list-bullet point">
		        <li>
		            검색 버튼을 클릭하면 조건에 맞는 검색결과를 확인할 수 있습니다.
		        </li>
	        </ul>
	  	</div>
		<%-- 검색버튼 --%>
		<div class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>현재 날짜 기준 집계 완료된 통계자료(직전월)까지만 조회 가능합니다.</caption>
				<button type="submit" class="btn btn-dark-gray btn-mini" onclick="javascript:search()"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/report/mctpv'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
			</div>
		</div>
	</form:form>

	<div class="board_list">
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<button type="button" id="btn_tab1" class="btn btn-dark-gray btn-mini">지자체별 기부금 현황(누계)</button>
				<button type="button" id="btn_tab2"  class="btn btn-dark-gray btn-mini">지자체별 기부금 현황(2000만원, 누계)</button>
				<button type="button" id="btn_tab3"  class="btn btn-dark-gray btn-mini">지자체별 답례품 제공현황</button>
			</div>
		</div>
		<div id="divCaptionForMctpvDntn" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 백만원, %)</caption>
			</div>
		</div>
		<div id="tbMctpvDntnStats" style="overflow-x:auto;">
			<table class="board_list_table" style="width:max-content;min-width:100%" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth*3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th class="stickyColumn" scope="col">시/도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th id="th_dts_now_yr_${i}" scope="col">${i}월</th>
								<th id="th_dts_prvyr_${i}" scope="col">전년도 ${i}월</th>
								<th id="th_dts_rt_${i}" scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${mctpvDntnStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td class="stickyColumn">
							<c:out value="${list.mctpvNm}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMJan)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMJan)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJan != '0'}">
								<c:if test="${list.prvyrNowYrRtJan == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJan != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJan))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJan == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 1}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMFeb)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMFeb)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtFeb != '0'}">
								<c:if test="${list.prvyrNowYrRtFeb == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtFeb != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtFeb))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtFeb == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 2}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMMar)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMMar)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMar != '0'}">
								<c:if test="${list.prvyrNowYrRtMar == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMar != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtMar))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMar == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 3}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMApr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMApr)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtApr != '0'}">
								<c:if test="${list.prvyrNowYrRtApr == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtApr != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtApr))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtApr == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 4}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMMay)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMMay)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMay != '0'}">
								<c:if test="${list.prvyrNowYrRtMay == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMay != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtMay))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMay == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 5}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMJun)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMJun)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJun != '0'}">
								<c:if test="${list.prvyrNowYrRtJun == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJun != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJun))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJun == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 6}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMJul)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMJul)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJul != '0'}">
								<c:if test="${list.prvyrNowYrRtJul == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJul != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJul))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJul == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 7}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMAug)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMAug)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtAug != '0'}">
								<c:if test="${list.prvyrNowYrRtAug == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtAug != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtAug))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtAug == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 8}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMSep)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMSep)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtSep != '0'}">
								<c:if test="${list.prvyrNowYrRtSep == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtSep != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtSep))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtSep == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 9}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMOct)}"/>
						</td>
						<td >
							<c:out value="${op:numberFormat(list.prvyrMOct)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtOct != '0'}">
								<c:if test="${list.prvyrNowYrRtOct == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtOct != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtOct))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtOct == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 10}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMNov)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMNov)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtNov != '0'}">
								<c:if test="${list.prvyrNowYrRtNov == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtNov != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtNov))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtNov == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 11}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMDec)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMDec)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtDec != '0'}">
								<c:if test="${list.prvyrNowYrRtDec == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtDec != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtDec))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtDec == '0'}">
								0
							</c:if>
						</td>
						</c:if>
					</tr>
				  </c:forEach>

				</tbody>
			</table>
		</div>
		<div id="divCaptionForMctpvDntnMax" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 백만원, %)</caption>
			</div>
		</div>
		<div id="tbMctpvDntnMaxStats" style="overflow-x:auto;">
			<table class="board_list_table" style="width:max-content;min-width:100%" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth*3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th class="stickyColumn" scope="col">시/도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th id="th_dts_now_yr_${i}" scope="col">${i}월</th>
								<th id="th_dts_prvyr_${i}" scope="col">전년도 ${i}월</th>
								<th id="th_dts_rt_${i}" scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${mctpvDntnMaxAmtStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td class="stickyColumn">
							<c:out value="${list.mctpvNm}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:if test="${list.nowYrMJan == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMJan != 0}">
								<c:out value="${op:numberFormat(list.nowYrMJan)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMJan == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMJan != 0}">
								<c:out value="${op:numberFormat(list.prvyrMJan)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJan != '0'}">
								<c:if test="${list.prvyrNowYrRtJan == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJan != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJan))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJan == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 1}">
						<td>
							<c:if test="${list.nowYrMFeb == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMFeb != 0}">
								<c:out value="${op:numberFormat(list.nowYrMFeb)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMFeb == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMFeb != 0}">
								<c:out value="${op:numberFormat(list.prvyrMFeb)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtFeb != '0'}">
								<c:if test="${list.prvyrNowYrRtFeb == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtFeb != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtFeb))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtFeb == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 2}">
						<td>
							<c:if test="${list.nowYrMMar == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMMar != 0}">
								<c:out value="${op:numberFormat(list.nowYrMMar)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMMar == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMMar != 0}">
								<c:out value="${op:numberFormat(list.prvyrMMar)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMar != '0'}">
								<c:if test="${list.prvyrNowYrRtMar == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMar != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtMar))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMar == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 3}">
						<td>
							<c:if test="${list.nowYrMApr == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMApr != 0}">
								<c:out value="${op:numberFormat(list.nowYrMApr)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMApr == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMApr != 0}">
								<c:out value="${op:numberFormat(list.prvyrMApr)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtApr != '0'}">
								<c:if test="${list.prvyrNowYrRtApr == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtApr != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtApr))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtApr == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 4}">
						<td>
							<c:if test="${list.nowYrMMay == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMMay != 0}">
								<c:out value="${op:numberFormat(list.nowYrMMay)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMMay == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMMay != 0}">
								<c:out value="${op:numberFormat(list.prvyrMMay)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMay != '0'}">
								<c:if test="${list.prvyrNowYrRtMay == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMay != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtMay))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMay == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 5}">
						<td>
							<c:if test="${list.nowYrMJun == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMJun != 0}">
								<c:out value="${op:numberFormat(list.nowYrMJun)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMJun == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMJun != 0}">
								<c:out value="${op:numberFormat(list.prvyrMJun)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJun != '0'}">
								<c:if test="${list.prvyrNowYrRtJun == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJun != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJun))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJun == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 6}">
						<td>
							<c:if test="${list.nowYrMJul == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMJul != 0}">
								<c:out value="${op:numberFormat(list.nowYrMJul)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMJul == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMJul != 0}">
								<c:out value="${op:numberFormat(list.prvyrMJul)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJul != '0'}">
								<c:if test="${list.prvyrNowYrRtJul == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJul != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJul))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJul == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 7}">
						<td>
							<c:if test="${list.nowYrMAug == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMAug != 0}">
								<c:out value="${op:numberFormat(list.nowYrMAug)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMAug == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMAug != 0}">
								<c:out value="${op:numberFormat(list.prvyrMAug)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtAug != '0'}">
								<c:if test="${list.prvyrNowYrRtAug == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtAug != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtAug))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtAug == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 8}">
						<td>
							<c:if test="${list.nowYrMSep == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMSep != 0}">
								<c:out value="${op:numberFormat(list.nowYrMSep)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMSep == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMSep != 0}">
								<c:out value="${op:numberFormat(list.prvyrMSep)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtSep != '0'}">
								<c:if test="${list.prvyrNowYrRtSep == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtSep != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtSep))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtSep == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 9}">
						<td>
							<c:if test="${list.nowYrMOct == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMOct != 0}">
								<c:out value="${op:numberFormat(list.nowYrMOct)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMOct == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMOct != 0}">
								<c:out value="${op:numberFormat(list.prvyrMOct)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtOct != '0'}">
								<c:if test="${list.prvyrNowYrRtOct == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtOct != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtOct))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtOct == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 10}">
						<td>
							<c:if test="${list.nowYrMNov == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMNov != 0}">
								<c:out value="${op:numberFormat(list.nowYrMNov)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMNov == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMNov != 0}">
								<c:out value="${op:numberFormat(list.prvyrMNov)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtNov != '0'}">
								<c:if test="${list.prvyrNowYrRtNov == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtNov != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtNov))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtNov == '0'}">
								-
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 11}">
						<td>
							<c:if test="${list.nowYrMDec == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrMDec != 0}">
								<c:out value="${op:numberFormat(list.nowYrMDec)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrMDec == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrMDec != 0}">
								<c:out value="${op:numberFormat(list.prvyrMDec)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtDec != '0'}">
								<c:if test="${list.prvyrNowYrRtDec == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtDec != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtDec))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtDec == '0'}">
								-
							</c:if>
						</td>
						</c:if>
					</tr>
				  </c:forEach>

				</tbody>
			</table>
		</div>
		<div id="divCaptionForMctpvGds" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 백만원, %)</caption>
			</div>
		</div>
		<div id="tbMctpvGdsStats" style="overflow-x:auto;">
			<table class="board_list_table" style="width:max-content;min-width:100%" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth*3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th class="stickyColumn" scope="col">시/도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th id="th_dts_now_yr_${i}" scope="col">${i}월</th>
								<th id="th_dts_prvyr_${i}" scope="col">전년도 ${i}월</th>
								<th id="th_dts_rt_${i}" scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${mctpvGdsStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td class="stickyColumn">
							<c:out value="${list.mctpvNm}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMJan)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMJan)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJan != '0'}">
								<c:if test="${list.prvyrNowYrRtJan == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJan != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJan))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJan == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 1}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMFeb)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMFeb)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtFeb != '0'}">
								<c:if test="${list.prvyrNowYrRtFeb == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtFeb != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtFeb))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtFeb == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 2}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMMar)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMMar)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMar != '0'}">
								<c:if test="${list.prvyrNowYrRtMar == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMar != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtMar))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMar == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 3}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMApr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMApr)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtApr != '0'}">
								<c:if test="${list.prvyrNowYrRtApr == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtApr != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtApr))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtApr == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 4}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMMay)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMMay)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMay != '0'}">
								<c:if test="${list.prvyrNowYrRtMay == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMay != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtMay))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMay == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 5}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMJun)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMJun)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJun != '0'}">
								<c:if test="${list.prvyrNowYrRtJun == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJun != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJun))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJun == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 6}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMJul)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMJul)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJul != '0'}">
								<c:if test="${list.prvyrNowYrRtJul == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJul != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtJul))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJul == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 7}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMAug)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMAug)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtAug != '0'}">
								<c:if test="${list.prvyrNowYrRtAug == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtAug != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtAug))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtAug == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 8}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMSep)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMSep)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtSep != '0'}">
								<c:if test="${list.prvyrNowYrRtSep == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtSep != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtSep))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtSep == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 9}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMOct)}"/>
						</td>
						<td >
							<c:out value="${op:numberFormat(list.prvyrMOct)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtOct != '0'}">
								<c:if test="${list.prvyrNowYrRtOct == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtOct != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtOct))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtOct == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 10}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMNov)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMNov)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtNov != '0'}">
								<c:if test="${list.prvyrNowYrRtNov == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtNov != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtNov))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtNov == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 11}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMDec)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMDec)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtDec != '0'}">
								<c:if test="${list.prvyrNowYrRtDec == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtDec != '-1'}">
									<c:out value="${op:numberFormat(100 * Float.parseFloat(list.prvyrNowYrRtDec))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtDec == '0'}">
								0
							</c:if>
						</td>
						</c:if>
					</tr>
				  </c:forEach>

				</tbody>
			</table>
		</div>

		<%-- 엑셀버튼 --%>
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:excelDownload();">엑셀</button>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
	function excelDownload() {
		Shop.downloadExcelOrder("/opmanager/shop-statistics/report/lclgv/excel-download", $('#statisticsParam').serialize(), false);
	}

	// 통계 데이터가 없으면 탭 핸들러 안의 forEach 가 비어 crtrYr 가 선언되지 않아 ReferenceError 가 나므로 기본값을 둔다.
	var crtrYr = '${empty statisticsParam.shYear ? nowYear : statisticsParam.shYear}';

	$("#btn_tab1").click(function() {
		document.getElementById("tbMctpvDntnStats").style.display = "";
		document.getElementById("tbMctpvDntnMaxStats").style.display = "none";
		document.getElementById("tbMctpvGdsStats").style.display = "none";
		document.getElementById("divCaptionForMctpvDntn").style.display = "";
		document.getElementById("divCaptionForMctpvDntnMax").style.display = "none";
		document.getElementById("divCaptionForMctpvGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${mctpvDntnStats}" var="mds" varStatus="i">
			var crtrYr = '${mds.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "1";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	$("#btn_tab2").click(function() {
		document.getElementById("tbMctpvDntnStats").style.display = "none";
		document.getElementById("tbMctpvDntnMaxStats").style.display = "";
		document.getElementById("tbMctpvGdsStats").style.display = "none";
		document.getElementById("divCaptionForMctpvDntn").style.display = "none";
		document.getElementById("divCaptionForMctpvDntnMax").style.display = "";
		document.getElementById("divCaptionForMctpvGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${mctpvDntnStats}" var="mds" varStatus="i">
			var crtrYr = '${mds.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "2";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	$("#btn_tab3").click(function() {
		document.getElementById("tbMctpvDntnStats").style.display = "none";
		document.getElementById("tbMctpvDntnMaxStats").style.display = "none";
		document.getElementById("tbMctpvGdsStats").style.display = "";
		document.getElementById("divCaptionForMctpvDntn").style.display = "none";
		document.getElementById("divCaptionForMctpvDntnMax").style.display = "none";
		document.getElementById("divCaptionForMctpvGds").style.display = "";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#00215A");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${mctpvDntnStats}" var="mds" varStatus="i">
			var crtrYr = '${mds.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "3";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	document.addEventListener("DOMContentLoaded", function(){
		var tabVal = '${tabId}';
		if(tabVal == "1") {
			$("#btn_tab1").trigger("click");
		} else if(tabVal == "2") {
			$("#btn_tab2").trigger("click");
		} else {
			$("#btn_tab3").trigger("click");
		}
	});

	function monthChange(year, month) {
		$("#shYear").val(year).prop("selected", true);
		var nowYear = '${nowYear}';
		var nowMonth = '${nowMonth}';
		var searchYear = '${statisticsParam.shYear}';
		var searchMonth = '${statisticsParam.shMonth}';
		Common.loading.display = false;

		//console.log("year : " + year + ", nowYear : " + nowYear + ", searchYear : " + searchYear + ", nowMonth : " + nowMonth + ", searchMonth : " + searchMonth);

		$("#shMonth option").remove();
		if ($("#shYear").val() != "") {
			if(year == nowYear) {
				for (var i = 1; i < parseInt(nowMonth); i++) {
		            var options = '<option value="' + i + '">' + i + '</option>';
		            $('#shMonth').append(options);
		        }
				if(year == searchYear) {
					$("#shMonth").val(month).prop("selected", true);
				} else {
					$("#shMonth").val(parseInt(nowMonth)-1).prop("selected", true);
				}
			} else {
				for (var i = 1; i <= 12; i++) {
		            var options = '<option value="' + i + '">' + i + '</option>';
		            $('#shMonth').append(options);
		        }
				if(year == searchYear) {
					$("#shMonth").val(month).prop("selected", true);
				} else {
					$("#shMonth").val(12).prop("selected", true);
				}
			}
		} else {
	        $('#shMonth').append('<option value="">검색월</option>');
		}
	}
</script>
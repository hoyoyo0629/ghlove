<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">총괄 현황</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span>총괄 현황</span></h3>
	<form:form modelAttribute="statisticsParam" method="post" >
		<form:input type="hidden" path="tabId" value="1"/>

		<div class="board_write mt10">
			<table class="board_write_table" summary="총괄 현황">
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
		                            <form:select path="shMonth" title="${op:message('월')}" class="wd-150" style="display:none">
		                                	<c:forEach var="i" begin="1" end="12" step="1">
		                                		<c:if test="${i < nowMonth}">
													<option value="${i}">${i}</option>
												</c:if>
											</c:forEach>
		                            </form:select>
		                            <!-- 월 -->
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/report/general'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
			</div>
		</div>
	</form:form>

	<div class="board_list">
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<button type="button" id="btn_tab1" class="btn btn-dark-gray btn-mini">회원 수 현황</button>
				<button type="button" id="btn_tab2"  class="btn btn-dark-gray btn-mini">기부건수 현황</button>
				<button type="button" id="btn_tab3"  class="btn btn-dark-gray btn-mini">기부건수(2000만원) 현황</button>
				<button type="button" id="btn_tab4"  class="btn btn-dark-gray btn-mini">답례품건수 현황</button>
			</div>
		</div>

		<div id="divCaptionForMbr" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 명)</caption>
			</div>
		</div>
		<div id="tbMbrTnocsStats">
			<table class="board_list_table" summary="총 회원수 현황">
				<thead>
					<tr>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
							<th scope="col" id="th_mts_${i}">${i}월</th>
						</c:forEach>
						<th scope="col">합계</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${mbrTnocsStats}" var="list" varStatus="i">
					<tr style="background:#fff;">
					<c:if test="${statisticsParam.shMonth > 0}">
						<td id="td_mts_1">
							<c:out value="${op:numberFormat(list.mbrCntJan)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 1}">
						<td id="td_mts_2">
							<c:out value="${op:numberFormat(list.mbrCntFeb)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 2}">
						<td id="td_mts_3">
							<c:out value="${op:numberFormat(list.mbrCntMar)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 3}">
						<td id="td_mts_4">
							<c:out value="${op:numberFormat(list.mbrCntApr)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 4}">
						<td id="td_mts_5">
							<c:out value="${op:numberFormat(list.mbrCntMay)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 5}">
						<td id="td_mts_6">
							<c:out value="${op:numberFormat(list.mbrCntJun)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 6}">
						<td id="td_mts_7">
							<c:out value="${op:numberFormat(list.mbrCntJul)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 7}">
						<td id="td_mts_8">
							<c:out value="${op:numberFormat(list.mbrCntAug)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 8}">
						<td id="td_mts_9">
							<c:out value="${op:numberFormat(list.mbrCntSep)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 9}">
						<td id="td_mts_10">
							<c:out value="${op:numberFormat(list.mbrCntOct)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 10}">
						<td id="td_mts_11">
							<c:out value="${op:numberFormat(list.mbrCntNov)}"/>
						</td>
					</c:if>
					<c:if test="${statisticsParam.shMonth > 11}">
						<td id="td_mts_12">
							<c:out value="${op:numberFormat(list.mbrCntDec)}"/>
						</td>
					</c:if>
						<td>
							<c:out value="${op:numberFormat(list.mbrTnocs)}"/>
						</td>
					</tr>
				  </c:forEach>

				</tbody>
			</table>
		</div>

		<div id="divCaptionForDntn" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 건, %)</caption>
			</div>
		</div>
		<div id="tbDntnTnocsStats" style="overflow-x:auto;">
			<table class="board_list_table" style="width:max-content;min-width:100%" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<col width="100px" />
					<col width="100px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth *3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th class="stickyColumn" scope="col">시/도</th>
						<th scope="col">올해 총계</th>
						<th scope="col">전년도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th id="th_dts_now_yr_${i}" scope="col">${i}월</th>
								<th id="th_dts_prvyr_${i}" scope="col">전년도 ${i}월</th>
								<th id="th_dts_rt_${i}" scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${dntnTnocsStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td class="stickyColumn">
							<c:out value="${list.mctpvNm}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.nowYr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyr)}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJan)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJan)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJan != '0'}">
								<c:if test="${list.prvyrNowYrRtJan == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJan != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJan))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJan == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 1}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrFeb)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrFeb)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtFeb != '0'}">
								<c:if test="${list.prvyrNowYrRtFeb == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtFeb != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtFeb))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtFeb == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 2}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMar)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMar)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMar != '0'}">
								<c:if test="${list.prvyrNowYrRtMar == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMar != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtMar))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMar == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 3}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrApr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrApr)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtApr != '0'}">
								<c:if test="${list.prvyrNowYrRtApr == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtApr != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtApr))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtApr == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 4}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMay)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMay)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMay != '0'}">
								<c:if test="${list.prvyrNowYrRtMay == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMay != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtMay))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMay == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 5}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJun)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJun)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJun != '0'}">
								<c:if test="${list.prvyrNowYrRtJun == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJun != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJun))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJun == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 6}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJul)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJul)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJul != '0'}">
								<c:if test="${list.prvyrNowYrRtJul == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJul != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJul))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJul == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 7}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrAug)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrAug)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtAug != '0'}">
								<c:if test="${list.prvyrNowYrRtAug == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtAug != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtAug))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtAug == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 8}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrSep)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrSep)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtSep != '0'}">
								<c:if test="${list.prvyrNowYrRtSep == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtSep != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtSep))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtSep == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 9}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrOct)}"/>
						</td>
						<td >
							<c:out value="${op:numberFormat(list.prvyrOct)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtOct != '0'}">
								<c:if test="${list.prvyrNowYrRtOct == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtOct != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtOct))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtOct == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 10}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrNov)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrNov)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtNov != '0'}">
								<c:if test="${list.prvyrNowYrRtNov == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtNov != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtNov))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtNov == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 11}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrDec)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrDec)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtDec != '0'}">
								<c:if test="${list.prvyrNowYrRtDec == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtDec != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtDec))}" />%
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

		<div id="divCaptionForGds" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 건, %)</caption>
			</div>
		</div>
		<div id="tbGdsTnocsStats" style="overflow-x:auto;">
			<table class="board_list_table" style="width:max-content;min-width:100%" summary="총 답례품건수 현황">
				<colgroup>
					<col width="200px" />
					<col width="100px" />
					<col width="100px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth *3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th class="stickyColumn" scope="col">시/도</th>
						<th scope="col">올해 총계</th>
						<th scope="col">전년도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th id="th_dts_now_yr_${i}" scope="col">${i}월</th>
								<th id="th_dts_prvyr_${i}" scope="col">전년도 ${i}월</th>
								<th id="th_dts_rt_${i}" scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${gdsTnocsStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td class="stickyColumn">
							<c:out value="${list.mctpvNm}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.nowYr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyr)}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJan)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJan)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJan != '0'}">
								<c:if test="${list.prvyrNowYrRtJan == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJan != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJan))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJan == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 1}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrFeb)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrFeb)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtFeb != '0'}">
								<c:if test="${list.prvyrNowYrRtFeb == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtFeb != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtFeb))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtFeb == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 2}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMar)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMar)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMar != '0'}">
								<c:if test="${list.prvyrNowYrRtMar == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMar != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtMar))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMar == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 3}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrApr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrApr)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtApr != '0'}">
								<c:if test="${list.prvyrNowYrRtApr == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtApr != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtApr))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtApr == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 4}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMay)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMay)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMay != '0'}">
								<c:if test="${list.prvyrNowYrRtMay == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMay != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtMay))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMay == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 5}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJun)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJun)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJun != '0'}">
								<c:if test="${list.prvyrNowYrRtJun == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJun != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJun))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJun == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 6}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJul)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJul)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJul != '0'}">
								<c:if test="${list.prvyrNowYrRtJul == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJul != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJul))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJul == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 7}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrAug)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrAug)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtAug != '0'}">
								<c:if test="${list.prvyrNowYrRtAug == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtAug != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtAug))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtAug == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 8}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrSep)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrSep)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtSep != '0'}">
								<c:if test="${list.prvyrNowYrRtSep == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtSep != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtSep))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtSep == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 9}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrOct)}"/>
						</td>
						<td >
							<c:out value="${op:numberFormat(list.prvyrOct)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtOct != '0'}">
								<c:if test="${list.prvyrNowYrRtOct == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtOct != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtOct))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtOct == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 10}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrNov)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrNov)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtNov != '0'}">
								<c:if test="${list.prvyrNowYrRtNov == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtNov != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtNov))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtNov == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 11}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrDec)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrDec)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtDec != '0'}">
								<c:if test="${list.prvyrNowYrRtDec == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtDec != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtDec))}" />%
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

		<div id="divCaptionForDntnMaxAmt" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 건, %)</caption>
			</div>
		</div>
		<div id="tbDntnTnocsMaxAmtStats" style="overflow-x:auto;">
			<table class="board_list_table" style="width:max-content;min-width:100%" summary="총 기부건수(2000만원) 현황">
				<colgroup>
					<col width="200px" />
					<col width="100px" />
					<col width="100px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth *3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th class="stickyColumn" scope="col">시/도</th>
						<th scope="col">올해 총계</th>
						<th scope="col">전년도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th id="th_dts_now_yr_${i}" scope="col">${i}월</th>
								<th id="th_dts_prvyr_${i}" scope="col">전년도 ${i}월</th>
								<th id="th_dts_rt_${i}" scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${dntnTnocsMaxAmtStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td class="stickyColumn">
							<c:out value="${list.mctpvNm}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.nowYr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyr)}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJan)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJan)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJan != '0'}">
								<c:if test="${list.prvyrNowYrRtJan == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJan != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJan))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJan == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 1}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrFeb)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrFeb)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtFeb != '0'}">
								<c:if test="${list.prvyrNowYrRtFeb == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtFeb != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtFeb))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtFeb == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 2}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMar)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMar)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMar != '0'}">
								<c:if test="${list.prvyrNowYrRtMar == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMar != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtMar))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMar == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 3}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrApr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrApr)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtApr != '0'}">
								<c:if test="${list.prvyrNowYrRtApr == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtApr != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtApr))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtApr == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 4}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrMay)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrMay)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtMay != '0'}">
								<c:if test="${list.prvyrNowYrRtMay == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtMay != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtMay))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtMay == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 5}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJun)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJun)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJun != '0'}">
								<c:if test="${list.prvyrNowYrRtJun == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJun != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJun))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJun == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 6}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrJul)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrJul)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtJul != '0'}">
								<c:if test="${list.prvyrNowYrRtJul == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtJul != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtJul))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtJul == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 7}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrAug)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrAug)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtAug != '0'}">
								<c:if test="${list.prvyrNowYrRtAug == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtAug != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtAug))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtAug == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 8}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrSep)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrSep)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtSep != '0'}">
								<c:if test="${list.prvyrNowYrRtSep == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtSep != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtSep))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtSep == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 9}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrOct)}"/>
						</td>
						<td >
							<c:out value="${op:numberFormat(list.prvyrOct)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtOct != '0'}">
								<c:if test="${list.prvyrNowYrRtOct == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtOct != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtOct))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtOct == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 10}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrNov)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrNov)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtNov != '0'}">
								<c:if test="${list.prvyrNowYrRtNov == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtNov != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtNov))}" />%
								</c:if>
							</c:if>
							<c:if test="${list.prvyrNowYrRtNov == '0'}">
								0
							</c:if>
						</td>
						</c:if>
						<c:if test="${statisticsParam.shMonth > 11}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrDec)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrDec)}"/>
						</td>
						<td>
							<c:if test="${list.prvyrNowYrRtDec != '0'}">
								<c:if test="${list.prvyrNowYrRtDec == '-1'}">
									-
								</c:if>
								<c:if test="${list.prvyrNowYrRtDec != '-1'}">
									<c:out value="${op:numberFormat(100*Float.parseFloat(list.prvyrNowYrRtDec))}" />%
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
				<!-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="birthdayBatch();">연령배치</button>
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="statisticsBatch();">통계배치</button>-->
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="excelDownload();">엑셀</button>
				<!-- <button type="button" class="btn btn-dark-gray btn-mini" onclick="excelLastYearDownload();">연통계</button> -->
			</div>
		</div>
	</div>
</div>


<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">
	function excelDownload() {
		Shop.downloadExcelOrder("/opmanager/shop-statistics/report/lclgv/excel-download", $('#statisticsParam').serialize(), false);
	}

	function excelLastYearDownload() {
		var selYear = $("#shYear").val();
		var nowYear = '${nowYear}';

		if(selYear == nowYear){
			alert(nowYear + "년도에서 과거년도 통계를 제공합니다.");
			$("#shYear").val(nowYear-1).prop("selected", true);
			return;
		}
		Shop.downloadExcelOrder("/opmanager/shop-statistics/report/lclgv/excel-pastYear-download", $('#statisticsParam').serialize(), false);
	}

	function birthdayBatch() {
		uri = "/opmanager/shop-statistics/report/general/birthdayBatch";
		location.href = uri;
	}

	function statisticsBatch() {
		uri = "/opmanager/shop-statistics/report/general/statisticsBatch";
		location.href = uri;
	}

	$("#btn_tab1").click(function() {
		document.getElementById("tbMbrTnocsStats").style.display = "";
		document.getElementById("tbDntnTnocsStats").style.display = "none";
		document.getElementById("tbDntnTnocsMaxAmtStats").style.display = "none";
		document.getElementById("tbGdsTnocsStats").style.display = "none";
		document.getElementById("divCaptionForMbr").style.display = "";
		document.getElementById("divCaptionForDntn").style.display = "none";
		document.getElementById("divCaptionForDntnMaxAmt").style.display = "none";
		document.getElementById("divCaptionForGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${mbrTnocsStats}" var="mts" varStatus="i">
			var crtrYr = '${mts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "1";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})

	$("#btn_tab2").click(function() {
		document.getElementById("tbMbrTnocsStats").style.display = "none";
		document.getElementById("tbDntnTnocsStats").style.display = "";
		document.getElementById("tbDntnTnocsMaxAmtStats").style.display = "none";
		document.getElementById("tbGdsTnocsStats").style.display = "none";
		document.getElementById("divCaptionForMbr").style.display = "none";
		document.getElementById("divCaptionForDntn").style.display = "";
		document.getElementById("divCaptionForDntnMaxAmt").style.display = "none";
		document.getElementById("divCaptionForGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${mbrTnocsStats}" var="mts" varStatus="i">
			var crtrYr = '${mts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "2";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})


	$("#btn_tab3").click(function() {
		document.getElementById("tbMbrTnocsStats").style.display = "none";
		document.getElementById("tbDntnTnocsStats").style.display = "none";
		document.getElementById("tbDntnTnocsMaxAmtStats").style.display = "";
		document.getElementById("tbGdsTnocsStats").style.display = "none";
		document.getElementById("divCaptionForMbr").style.display = "none";
		document.getElementById("divCaptionForDntn").style.display = "none";
		document.getElementById("divCaptionForDntnMaxAmt").style.display = "";
		document.getElementById("divCaptionForGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#004CCE");


		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${dntnTnocsMaxAmtStats}" var="dt2s" varStatus="i">
			var crtrYr = '${dt2s.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		var tabId = "3";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})


	$("#btn_tab4").click(function() {
		document.getElementById("tbMbrTnocsStats").style.display = "none";
		document.getElementById("tbDntnTnocsStats").style.display = "none";
		document.getElementById("tbDntnTnocsMaxAmtStats").style.display = "none";
		document.getElementById("tbGdsTnocsStats").style.display = "";
		document.getElementById("divCaptionForMbr").style.display = "none";
		document.getElementById("divCaptionForDntn").style.display = "none";
		document.getElementById("divCaptionForDntnMaxAmt").style.display = "none";
		document.getElementById("divCaptionForGds").style.display = "";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab4").setAttribute("style", "background-color:#00215A");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${gdsTnocsStats}" var="gts" varStatus="i">
			var crtrYr = '${gts.crtrYr}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';

		console.log("crtrYr : " + crtrYr + " ,crtrMm : " + crtrMm);

		var tabId = "4";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
	})



	document.addEventListener("DOMContentLoaded", function(){
		var tabVal = '${tabId}';
		if(tabVal == "1") {
			$("#btn_tab1").trigger("click");
		} else if(tabVal == "2") {
			$("#btn_tab2").trigger("click");
		} else if(tabVal == "3") {
			$("#btn_tab3").trigger("click");
		} else {
			$("#btn_tab4").trigger("click");
		}
	});

	function monthChange(year, month) {
		$("#shYear").val(year).prop("selected", true);
		var nowYear = '${nowYear}';
		var nowMonth = '${nowMonth}';
		var searchYear = '${statisticsParam.shYear}';
		var searchMonth = '${statisticsParam.shMonth}';
		Common.loading.display = false;

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
	monthChange($("#shYear").val(), "");//안보이는 달 셋팅해줘야함
</script>
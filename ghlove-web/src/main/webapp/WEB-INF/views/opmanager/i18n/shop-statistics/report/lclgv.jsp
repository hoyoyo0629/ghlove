<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="sec" 	uri="http://www.springframework.org/security/tags"%>

<div class="location">
	<a href="#">통계</a> &gt;  <a href="#">보고서</a> &gt; <a href="#" class="on">지자체 현황</a>
</div>
<!-- 본문 -->
<div class="statistics_web">
	<h3><span>지자체 현황</span></h3>
	<form:form modelAttribute="statisticsParam" method="post" >
		<form:input type="hidden" path="tabId" value="1"/>

		<div class="board_write mt10">
			<table class="board_write_table" summary="지자체(243개) 현황">
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
		                    <td class="label">지자체</td>
		                    <td>
		                        <div class="flex_box gap-08" style="align-items: center;">
		                        	<c:if test="${!op:hasRole('ROLE_ADMIN_5') && !op:hasRole('ROLE_ADMIN_6')}">
			                            <form:select path="shMctpv" title="${op:message('광역지자체')}" class="wd-150" onChange="searchSigungu(this.value)">
			                                <option value="">시,도</option>
			                                <c:forEach items="${mctpv}" var="data" varStatus="i">
												<option value="${data.mctpvCd}"}">${data.mctpvNm}</option>
											</c:forEach>
			                            </form:select>
			                            <form:select path="shLclgv" title="${op:message('지자체')}" class="wd-150">
			                                <option value="">시,군,구</option>
			                            </form:select>
			                    	</c:if>
			                    	<c:if test="${op:hasRole('ROLE_ADMIN_5') || op:hasRole('ROLE_ADMIN_6')}">
			                            <c:if test="${lclgvCd.length() > 0}">
			                            	<c:if test="${lclgvCd == mctpvCd}">
			                            		<c:forEach items="${mctpv}" var="data" varStatus="i">
			                            			<c:if test="${data.mctpvCd == mctpvCd}">
			                            				${data.mctpvNm }
			                            			</c:if>
			                            		</c:forEach>
			                            	</c:if>
			                            	<c:if test="${lclgvCd != mctpvCd}">
			                            		<c:forEach items="${mctpv}" var="data" varStatus="i">
			                            			<c:if test="${data.mctpvCd == mctpvCd}">
			                            				${data.mctpvNm }
			                            			</c:if>
			                            		</c:forEach>
			                            		<c:forEach items="${lclgv}" var="data" varStatus="i">
			                            			<c:if test="${data.lclgvCd == lclgvCd}">
			                            				${data.lclgvNm }
			                            			</c:if>
			                            		</c:forEach>
			                            	</c:if>
			                            </c:if>
			                    	</c:if>
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
				<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/shop-statistics/report/lclgv'"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
			</div>
		</div>
	</form:form>

	<div class="board_list">
		<div class="btn_all btn_left">
			<div class="flex_box gap-08">
				<button type="button" id="btn_tab1" class="btn btn-dark-gray btn-mini">지자체별 기부금 현황</button>
				<button type="button" id="btn_tab2"  class="btn btn-dark-gray btn-mini">지자체별 기부금 현황(2000만원)</button>
				<button type="button" id="btn_tab3"  class="btn btn-dark-gray btn-mini">지자체별 답례품 제공현황</button>
			</div>
		</div>
		<div id="divCaptionForLclgvAod" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 천원, %)</caption>
			</div>
		</div>
		<div id="tbLclgvAodStats" style="overflow-x:auto; overflow:scroll; height:700px">
			<table class="board_list_table" style="width:max-content;min-width:100%;" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<col width="100px" />
					<col width="100px" />
					<col width="100px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth*3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th style="position:sticky;left:0;" scope="col">시/도</th>
						<th style="position:sticky;left:200px;" scope="col">지자체</th>
						<th scope="col">올해 총계</th>
						<th scope="col">전년도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th scope="col">${i}월</th>
								<th scope="col">전년도 ${i}월</th>
								<th scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lclgvAodStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td style="position:sticky;left:0;background-color:#fff;" >
							<c:out value="${list.mctpvNm}"/>
						</td>
						<td style="position:sticky;left:200px;background-color:#fff;">
							<c:out value="${list.lclgvNm}"/>
						</td>
						<td>
							<c:if test="${list.nowYrK == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrK != 0}">
								<c:out value="${op:numberFormat(list.nowYrK)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrK == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrK != 0}">
								<c:out value="${op:numberFormat(list.prvyrK)}"/>
							</c:if>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:if test="${list.nowYrKJan == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKJan != 0}">
								<c:out value="${op:numberFormat(list.nowYrKJan)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKJan == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKJan != 0}">
								<c:out value="${op:numberFormat(list.prvyrKJan)}"/>
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
							<c:if test="${list.nowYrKFeb == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKFeb != 0}">
								<c:out value="${op:numberFormat(list.nowYrKFeb)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKFeb == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKFeb != 0}">
								<c:out value="${op:numberFormat(list.prvyrKFeb)}"/>
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
							<c:if test="${list.nowYrKMar == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKMar != 0}">
								<c:out value="${op:numberFormat(list.nowYrKMar)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKMar == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKMar != 0}">
								<c:out value="${op:numberFormat(list.prvyrKMar)}"/>
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
							<c:if test="${list.nowYrKApr == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKApr != 0}">
								<c:out value="${op:numberFormat(list.nowYrKApr)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKApr == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKApr != 0}">
								<c:out value="${op:numberFormat(list.prvyrKApr)}"/>
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
							<c:if test="${list.nowYrKMay == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKMay != 0}">
								<c:out value="${op:numberFormat(list.nowYrKMay)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKMay == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKMay != 0}">
								<c:out value="${op:numberFormat(list.prvyrKMay)}"/>
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
							<c:if test="${list.nowYrKJun == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKJun != 0}">
								<c:out value="${op:numberFormat(list.nowYrKJun)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKJun == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKJun != 0}">
								<c:out value="${op:numberFormat(list.prvyrKJun)}"/>
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
							<c:if test="${list.nowYrKJul == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKJul != 0}">
								<c:out value="${op:numberFormat(list.nowYrKJul)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKJul == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKJul != 0}">
								<c:out value="${op:numberFormat(list.prvyrKJul)}"/>
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
							<c:if test="${list.nowYrKAug == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKAug != 0}">
								<c:out value="${op:numberFormat(list.nowYrKAug)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKAug == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKAug != 0}">
								<c:out value="${op:numberFormat(list.prvyrKAug)}"/>
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
							<c:if test="${list.nowYrKSep == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKSep != 0}">
								<c:out value="${op:numberFormat(list.nowYrKSep)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKSep == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKSep != 0}">
								<c:out value="${op:numberFormat(list.prvyrKSep)}"/>
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
							<c:if test="${list.nowYrKOct == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKOct != 0}">
								<c:out value="${op:numberFormat(list.nowYrKOct)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKOct == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKOct != 0}">
								<c:out value="${op:numberFormat(list.prvyrKOct)}"/>
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
							<c:if test="${list.nowYrKNov == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKNov != 0}">
								<c:out value="${op:numberFormat(list.nowYrKNov)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKNov == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKNov != 0}">
								<c:out value="${op:numberFormat(list.prvyrKNov)}"/>
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
							<c:if test="${list.nowYrKDec == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKDec != 0}">
								<c:out value="${op:numberFormat(list.nowYrKDec)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKDec == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKDec != 0}">
								<c:out value="${op:numberFormat(list.prvyrKDec)}"/>
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
		<div id="divCaptionForLclgvAodMax" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 천원, %)</caption>
			</div>
		</div>
		<div id="tbLclgvAodMaxStats" style="overflow-x:auto; overflow:scroll; height:700px">
			<table class="board_list_table" style="width:max-content;min-width:100%;" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<col width="100px" />
					<col width="100px" />
					<col width="100px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth*3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th style="position:sticky;left:0;" scope="col">시/도</th>
						<th style="position:sticky;left:200px;" scope="col">지자체</th>
						<th scope="col">올해 총계</th>
						<th scope="col">전년도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th scope="col">${i}월</th>
								<th scope="col">전년도 ${i}월</th>
								<th scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lclgvAodMaxAmtStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td style="position:sticky;left:0;background-color:#fff;" >
							<c:out value="${list.mctpvNm}"/>
						</td>
						<td style="position:sticky;left:200px;background-color:#fff;">
							<c:out value="${list.lclgvNm}"/>
						</td>
						<td>
							<c:if test="${list.nowYrK == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrK != 0}">
								<c:out value="${op:numberFormat(list.nowYrK)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrK == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrK != 0}">
								<c:out value="${op:numberFormat(list.prvyrK)}"/>
							</c:if>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:if test="${list.nowYrKJan == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKJan != 0}">
								<c:out value="${op:numberFormat(list.nowYrKJan)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKJan == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKJan != 0}">
								<c:out value="${op:numberFormat(list.prvyrKJan)}"/>
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
							<c:if test="${list.nowYrKFeb == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKFeb != 0}">
								<c:out value="${op:numberFormat(list.nowYrKFeb)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKFeb == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKFeb != 0}">
								<c:out value="${op:numberFormat(list.prvyrKFeb)}"/>
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
							<c:if test="${list.nowYrKMar == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKMar != 0}">
								<c:out value="${op:numberFormat(list.nowYrKMar)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKMar == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKMar != 0}">
								<c:out value="${op:numberFormat(list.prvyrKMar)}"/>
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
							<c:if test="${list.nowYrKApr == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKApr != 0}">
								<c:out value="${op:numberFormat(list.nowYrKApr)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKApr == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKApr != 0}">
								<c:out value="${op:numberFormat(list.prvyrKApr)}"/>
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
							<c:if test="${list.nowYrKMay == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKMay != 0}">
								<c:out value="${op:numberFormat(list.nowYrKMay)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKMay == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKMay != 0}">
								<c:out value="${op:numberFormat(list.prvyrKMay)}"/>
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
							<c:if test="${list.nowYrKJun == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKJun != 0}">
								<c:out value="${op:numberFormat(list.nowYrKJun)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKJun == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKJun != 0}">
								<c:out value="${op:numberFormat(list.prvyrKJun)}"/>
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
							<c:if test="${list.nowYrKJul == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKJul != 0}">
								<c:out value="${op:numberFormat(list.nowYrKJul)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKJul == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKJul != 0}">
								<c:out value="${op:numberFormat(list.prvyrKJul)}"/>
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
							<c:if test="${list.nowYrKAug == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKAug != 0}">
								<c:out value="${op:numberFormat(list.nowYrKAug)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKAug == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKAug != 0}">
								<c:out value="${op:numberFormat(list.prvyrKAug)}"/>
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
							<c:if test="${list.nowYrKSep == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKSep != 0}">
								<c:out value="${op:numberFormat(list.nowYrKSep)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKSep == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKSep != 0}">
								<c:out value="${op:numberFormat(list.prvyrKSep)}"/>
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
							<c:if test="${list.nowYrKOct == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKOct != 0}">
								<c:out value="${op:numberFormat(list.nowYrKOct)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKOct == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKOct != 0}">
								<c:out value="${op:numberFormat(list.prvyrKOct)}"/>
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
							<c:if test="${list.nowYrKNov == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKNov != 0}">
								<c:out value="${op:numberFormat(list.nowYrKNov)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKNov == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKNov != 0}">
								<c:out value="${op:numberFormat(list.prvyrKNov)}"/>
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
							<c:if test="${list.nowYrKDec == 0}">
								-
							</c:if>
							<c:if test="${list.nowYrKDec != 0}">
								<c:out value="${op:numberFormat(list.nowYrKDec)}"/>
							</c:if>
						</td>
						<td>
							<c:if test="${list.prvyrKDec == 0}">
								-
							</c:if>
							<c:if test="${list.prvyrKDec != 0}">
								<c:out value="${op:numberFormat(list.prvyrKDec)}"/>
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
		<div id="divCaptionForLclgvGds" class="btn_all btn_right">
			<div class="flex_box gap-08">
				<caption>(단위: 천원, %)</caption>
			</div>
		</div>
		<div id="tbLclgvGdsStats" style="overflow-x:auto;overflow:scroll;height:700px">
			<table class="board_list_table" style="width:max-content;min-width:100%;" summary="총 기부건수 현황">
				<colgroup>
					<col width="200px" />
					<col width="100px" />
					<col width="100px" />
					<col width="100px" />
					<c:forEach var="i" begin="1" end="${statisticsParam.shMonth*3}" step="1">
						<col width="100px" />
					</c:forEach>
				</colgroup>
				<thead>
					<tr style="position:relative;">
						<th style="position:sticky;left:0;" scope="col">시/도</th>
						<th style="position:sticky;left:200px;" scope="col">지자체</th>
						<th scope="col">올해 총계</th>
						<th scope="col">전년도</th>
						<c:forEach var="i" begin="1" end="${statisticsParam.shMonth}" step="1">
								<th scope="col">${i}월</th>
								<th scope="col">전년도 ${i}월</th>
								<th scope="col">전년대비</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lclgvGdsStats}" var="list" varStatus="i">
					<tr style="background:#fff; position:relative;">
						<td style="position:sticky;left:0;background-color:#fff;" >
							<c:out value="${list.mctpvNm}"/>
						</td>
						<td style="position:sticky;left:200px;background-color:#fff;">
							<c:out value="${list.lclgvNm}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.nowYrK)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrK)}"/>
						</td>
						<c:if test="${statisticsParam.shMonth > 0}">
						<td>
							<c:out value="${op:numberFormat(list.nowYrKJan)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKJan)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKFeb)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKFeb)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKMar)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKMar)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKApr)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKApr)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKMay)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKMay)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKJun)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKJun)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKJul)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKJul)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKAug)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKAug)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKSep)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKSep)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKOct)}"/>
						</td>
						<td >
							<c:out value="${op:numberFormat(list.prvyrKOct)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKNov)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKNov)}"/>
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
							<c:out value="${op:numberFormat(list.nowYrKDec)}"/>
						</td>
						<td>
							<c:out value="${op:numberFormat(list.prvyrKDec)}"/>
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

	// 통계 데이터가 없으면 탭 핸들러 안의 forEach 가 비어 crtrYr/mctpv/lclgv 가 선언되지 않아 ReferenceError 가 나므로 기본값을 둔다.
	var crtrYr = '${empty statisticsParam.shYear ? nowYear : statisticsParam.shYear}';
	var mctpv = '${statisticsParam.shMctpv}';
	var lclgv = '${statisticsParam.shLclgv}';

	$("#btn_tab1").click(function() {
		document.getElementById("tbLclgvAodStats").style.display = "";
		document.getElementById("tbLclgvAodMaxStats").style.display = "none";
		document.getElementById("tbLclgvGdsStats").style.display = "none";
		document.getElementById("divCaptionForLclgvAod").style.display = "";
		document.getElementById("divCaptionForLclgvAodMax").style.display = "none";
		document.getElementById("divCaptionForLclgvGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${lclgvAodStats}" var="las" varStatus="i">
			var crtrYr = '${las.crtrYr}';
			var mctpv = '${las.mctpvCd}';
			var lclgv = '${las.lclgvCd}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';
		var shMctpv = '${statisticsParam.shMctpv}';
		var shLclgv = '${statisticsParam.shLclgv}';

		if(shMctpv == "") {
			$("#shMctpv").val("").prop("selected", true)
		} else {
			$("#shMctpv").val(mctpv).prop("selected", true)
		}

		if(shLclgv == "") {
			$("#shMctpv").val("").prop("selected", true)
		} else {
			$("#shLclgv").val(mctpv).prop("selected", true)
		}


		var tabId = "1";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
		searchSigungu(shMctpv, shLclgv, tabId);
	})

	$("#btn_tab2").click(function() {
		document.getElementById("tbLclgvAodStats").style.display = "none";
		document.getElementById("tbLclgvAodMaxStats").style.display = "";
		document.getElementById("tbLclgvGdsStats").style.display = "none";
		document.getElementById("divCaptionForLclgvAod").style.display = "none";
		document.getElementById("divCaptionForLclgvAodMax").style.display = "";
		document.getElementById("divCaptionForLclgvGds").style.display = "none";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#00215A");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#004CCE");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${lclgvGdsStats}" var="lgs" varStatus="i">
			var crtrYr = '${lgs.crtrYr}';
			var mctpv = '${lgs.mctpvCd}';
			var lclgv = '${lgs.lclgvCd}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';
		var shMctpv = '${statisticsParam.shMctpv}';
		var shLclgv = '${statisticsParam.shLclgv}';

		if(shMctpv == "") {
			$("#shMctpv").val("").prop("selected", true)
		} else {
			$("#shMctpv").val(mctpv).prop("selected", true)
		}

		if(shLclgv == "") {
			$("#shMctpv").val("").prop("selected", true)
		} else {
			$("#shLclgv").val(mctpv).prop("selected", true)
		}


		var tabId = "2";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
		searchSigungu(shMctpv, shLclgv, tabId);
	})

	$("#btn_tab3").click(function() {
		document.getElementById("tbLclgvAodStats").style.display = "none";
		document.getElementById("tbLclgvAodMaxStats").style.display = "none";
		document.getElementById("tbLclgvGdsStats").style.display = "";
		document.getElementById("divCaptionForLclgvAod").style.display = "none";
		document.getElementById("divCaptionForLclgvAodMax").style.display = "none";
		document.getElementById("divCaptionForLclgvGds").style.display = "";
		document.getElementById("btn_tab1").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab2").setAttribute("style", "background-color:#004CCE");
		document.getElementById("btn_tab3").setAttribute("style", "background-color:#00215A");

		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<c:forEach items="${lclgvAodStats}" var="las" varStatus="i">
			var crtrYr = '${las.crtrYr}';
			var mctpv = '${las.mctpvCd}';
			var lclgv = '${las.lclgvCd}';
		</c:forEach>

		var crtrMm = '${statisticsParam.shMonth}';
		var shMctpv = '${statisticsParam.shMctpv}';
		var shLclgv = '${statisticsParam.shLclgv}';

		if(shMctpv == "") {
			$("#shMctpv").val("").prop("selected", true)
		} else {
			$("#shMctpv").val(mctpv).prop("selected", true)
		}
		if(shLclgv == "") {
			$("#shLclgv").val("").prop("selected", true)
		} else {
			$("#shLclgv").val(lclgv).prop("selected", true)
		}

		var lclgvCd = '${lclgv}';
		var mctpvCd = '${mctpv}';

		var tabId = "3";
		$('#tabId').val(tabId);

		monthChange(crtrYr, crtrMm);
		searchSigungu(shMctpv, shLclgv, tabId);
	})

	document.addEventListener("DOMContentLoaded", function(){
		var tabVal = '${tabId}';
		if(tabVal == "1") {
			$("#btn_tab1").trigger("click");
		} else if(tabVal == "2") {
			$("#btn_tab2").trigger("click");
		}  else {
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

	function searchSigungu(mctpv, lclgv, tabId) {
		$("#shMctpv").val(mctpv).prop("selected", true);
		$("#shLclgv option").remove();
		if ($("#shMctpv").val() != "") {
			$.post(url("/opmanager/shop-statistics/report/lclgv/options-by-lclgv"), {'mctpv' : mctpv, 'tabId' : tabId}, function(response) {
				$('#shLclgv').append('<option value="">시,군,구</option>');
				for (var i = 0; i < response.length; i++) {
		            var options = '<option value="' + response[i].lclgvCd + '">' + response[i].lclgvNm + '</option>';
		            $('#shLclgv').append(options);
		        }

				$("#shLclgv").val(lclgv).prop("selected", true);

		    });
		} else {
	        $('#shLclgv').append('<option value="">시,군,구</option>');
		}
	}
</script>
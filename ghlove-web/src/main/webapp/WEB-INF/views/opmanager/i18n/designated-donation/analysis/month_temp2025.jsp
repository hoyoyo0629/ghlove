<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" 	tagdir="/WEB-INF/tags/page"%>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>
<%@ taglib prefix="shop"	uri="/WEB-INF/tlds/shop" %>
<!-- <div class="admin_wrap"> -->
<div>
	<div class="location">
		<a href="#"></a> &gt;  <a href="#"></a> &gt; <a href="#" class="on"></a>
	</div>


	<div class="item_list">
		<h3><span>월별 통계</span></h3>

		<form:form modelAttribute="locgovSearchParam" method="post">

			<div class="board_write">
				<table class="board_write_table" summary="특정사업에 기부하기 목록">
					<caption>특정사업에 기부하기 목록</caption>
					<colgroup>
						<col style="width: 150px" />
						<col style="width: auto;" />
						<col style="width: 150px" />
						<col style="width: auto;" />
					</colgroup>
					<tbody>
						<c:if test="${op:hasRole('ROLE_ADMIN_1')
									|| op:hasRole('ROLE_ADMIN_2')
									|| op:hasRole('ROLE_ADMIN_3')
									|| op:hasRole('ROLE_ADMIN_4')}">
							<tr>
		                    	<td class="label">지자체</td>
				              	<td>
				                  	<div class="flex_box gap-08">
				                    	<form:select path="shWdr" title="지자체" class="wd-150" onChange="wdrChange(this.value)">
		                            		<form:option value="">-전체-</form:option>
			                            	<c:forEach items="${wdr}" var="wdr">
			                              		<form:option value="${fn:escapeXml(wdr.id)}" label="${fn:escapeXml(wdr.label)}" />
			                            	</c:forEach>
		                          		</form:select>
		                          		<form:select path="shLocgovCode" title="지자체" class="wd-150">
		                            		<option value="">-시,군,구-</option>
		                          		</form:select>
				                  	</div>
				              	</td>
							</tr>
						</c:if>
						<tr>
							<td class="label">조회년도</td>
							<td>
								<div>
				                	<form:select path="selYear" title="년도" class="wd-150">
				                    	<c:forEach items="${yyyy}" var="yyyy">
				                        	<form:option value="${fn:escapeXml(yyyy.id)}" label="${fn:escapeXml(yyyy.label)}"  />
				                    	</c:forEach>
				                    </form:select>
				                </div>
							</td>
							<td class="label">상태</td>
							<td>
								<div class="flex_box gap-12">
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="0" label="전체" checked="checked" />
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="2" label="진행" />
									</div>
									<div class="input-form">
										<form:radiobutton path="prjStatus" value="9" label="종료" />
									</div>
								</div>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="btn_all btn_right">
					<div class="flex_box gap-08">
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="location.href='/opmanager/designated-donation/analysis/month';"> <c:out value="${op:message('M00047')}"/><!-- 초기화 --></button>
						<button type="button" class="btn btn-dark-gray btn-mini" onclick="javascript:fnSearch();"> <c:out value="${op:message('M00048')}"/><!-- 검색 --></button>
					</div>
				</div>
			</div>
		</form:form>
		<div class="mainArea loc_details mb30" style="margin-top: 15px;">
			<div class="graph_wrap all">
		        <div class="bd_gray mb15" style="float: left; width: 49%;">
		            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">사업구분별 사업 진행건 비율</p>
		            <div class="graph mb15" style="width:300px;margin: auto;">
		                <canvas id="designatedLocgovMonthCampaignChart"></canvas>
		            </div>

			        <table class="board_write_table" style="margin-top: 15px;">
			        	<colgroup>
			                <col>
			                <col>
			                <col>
			                <col>
			                <col>
			                <col>
			            </colgroup>
			            <tbody>
			                <tr>
			                    <td class="label">사업구분</td>
								<td class="label">취약계층</td>
			                    <td class="label">문화/예술</td>
			                    <td class="label">자원봉사</td>
			                    <td class="label">복리증진</td>
			                    <td class="label">합계</td>
			                </tr>
			                <tr>
			                    <td id="tdCampaign1_1" class="label"></td>
								<td id="tdCampaign1_2" style="text-align:center;"></td>
			                    <td id="tdCampaign1_3" style="text-align:center;"></td>
			                    <td id="tdCampaign1_4" style="text-align:center;"></td>
			                    <td id="tdCampaign1_5" style="text-align:center;"></td>
			                    <td id="tdCampaign1_6" style="text-align:center;"></td>
			                </tr>
			                <tr>
			                    <td id="tdCampaign2_1" class="label"></td>
								<td id="tdCampaign2_2" style="text-align:center;" class="number"></td>
			                    <td id="tdCampaign2_3" style="text-align:center;" class="number"></td>
			                    <td id="tdCampaign2_4" style="text-align:center;" class="number"></td>
			                    <td id="tdCampaign2_5" style="text-align:center;" class="number"></td>
			                    <td id="tdCampaign2_6" style="text-align:center;" class="number"></td>
			                </tr>
			            </tbody>
			        </table>
				</div>
		        <div class="bd_gray mb15" style="float: left; width: 49%;">
		            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">모금액 비율</p>
		            <div class="graph mb15" style="width:300px;margin: auto;">
		            	<canvas id="designatedLocgovMonthAmountRaisedChart"></canvas>
		            </div>
			        <table class="board_write_table" style="margin-top: 15px;">
			        	<colgroup>
			                <col>
			                <col>
			                <col>
			                <col>
			                <col>
			                <col>
			            </colgroup>
			            <tbody>
			                <tr>
			                    <td class="label">사업구분</td>
								<td class="label">취약계층</td>
			                    <td class="label">문화/예술</td>
			                    <td class="label">자원봉사</td>
			                    <td class="label">복리증진</td>
			                    <td class="label">합계</td>
			                </tr>
			                <tr>
			                    <td id="tdAmountRaised1_1" class="label"></td>
								<td id="tdAmountRaised1_2" style="text-align:center;"></td>
			                    <td id="tdAmountRaised1_3" style="text-align:center;"></td>
			                    <td id="tdAmountRaised1_4" style="text-align:center;"></td>
			                    <td id="tdAmountRaised1_5" style="text-align:center;"></td>
			                    <td id="tdAmountRaised1_6" style="text-align:center;"></td>
			                </tr>
			                <tr>
			                    <td id="tdAmountRaised2_1" class="label"></td>
								<td id="tdAmountRaised2_2" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised2_3" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised2_4" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised2_5" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised2_6" style="text-align:center;" class="number"></td>
			                </tr>
			                <tr>
			                    <td id="tdAmountRaised3_1" class="label"></td>
								<td id="tdAmountRaised3_2" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised3_3" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised3_4" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised3_5" style="text-align:center;" class="number"></td>
			                    <td id="tdAmountRaised3_6" style="text-align:center;" class="number"></td>
			                </tr>
			            </tbody>
			        </table>
				</div>
				<div class="bd_gray mb15">
		            <p class="tit" style="line-height: 36px; margin-bottom: 20px; font-weight: bold;">월별 모금액 추이</p>
		            <div class="graph mb15">
		            	<canvas id="designatedLocgovMonthAmountChart" style="width:100%; height: 400px;"></canvas>
		            </div>
			        <table class="board_write_table">
			        	<colgroup>
			                <col style="width:130px;">
			                <col>
			                <col>
			                <col>
			                <col>
			                <col>
			            </colgroup>
			            <tbody>
			                <tr>
			                    <td class="label">월</td>
								<td class="label">1월</td>
			                    <td class="label">2월</td>
			                    <td class="label">3월</td>
			                    <td class="label">4월</td>
			                    <td class="label">5월</td>
			                    <td class="label">6월</td>
			                    <td class="label">7월</td>
			                    <td class="label">8월</td>
			                    <td class="label">9월</td>
			                    <td class="label">10월</td>
			                    <td class="label">11월</td>
			                    <td class="label">12월</td>
			                    <td class="label">합계</td>
			                </tr>
			                <tr>
			                    <td id="tdAmount1_1" class="label"></td>
								<td id="tdAmount1_2" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_3" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_4" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_5" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_6" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_7" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_8" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_9" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_10" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_11" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_12" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_13" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount1_14" style="text-align:center;" class="number"></td>
			                </tr>
			                <tr>
			                    <td id="tdAmount2_1" class="label"></td>
								<td id="tdAmount2_2" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_3" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_4" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_5" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_6" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_7" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_8" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_9" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_10" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_11" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_12" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_13" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount2_14" style="text-align:center;" class="number"></td>
			                </tr>
			                <tr>
			                    <td id="tdAmount3_1" class="label"></td>
								<td id="tdAmount3_2" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_3" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_4" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_5" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_6" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_7" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_8" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_9" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_10" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_11" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_12" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_13" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount3_14" style="text-align:center;" class="number"></td>
			                </tr>
			                <tr>
			                    <td id="tdAmount4_1" class="label"></td>
								<td id="tdAmount4_2" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_3" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_4" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_5" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_6" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_7" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_8" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_9" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_10" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_11" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_12" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_13" style="text-align:center;" class="number"></td>
			                    <td id="tdAmount4_14" style="text-align:center;" class="number"></td>
			                </tr>
			            </tbody>
			        </table>
				</div>
		    </div>
		</div>
	</div>
</div>

<style>
	td {background: #fff;}

</style>
<!-- <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script> -->
<script type="text/javascript" src="/content/modules/op.chart.js"></script>
<script type="text/javascript">


	let changeYn = "N";

	let locgovChart;

	let chartLoadingYn = true;

	$(function () {
		$("select[name='shWdr']").on('focus', function () {

		}).change(function() {
			changeYn = "Y";
		});

		fnSearch();
	});


	// 지자체 변경
	function wdrChange(value) {
		Common.loading.hide();
		$("#shLocgovCode option").remove();
		if ($("#shWdr").val() != "") {
			$.post(url("/opmanager/give/give-state/options-by-locgovCode"), {'code' : value}, function(response) {
				$('#shLocgovCode').append('<option value="">-전체-</option>');
				for (var i = 0; i < response.length; i++) {
		            const options = '<option value="' + response[i].LOCGOV_CODE + '">' + response[i].LOCGOV_NM + '</option>';
		            $('#shLocgovCode').append(options);
		        }

				// 조회 된 값 유지
				if ("${fn:escapeXml(locgovSearchParam.shWdr)}" != "" && changeYn == 'N') {
				    $("#shLocgovCode").val('${fn:escapeXml(locgovSearchParam.shWdr)}').prop("selected", true);
				} else {
					// 지차체 변경후 시,군,구 index 제일 처음으로 설정
					//$("#shLocgovCode option:eq(0)").attr("selected", "selected");
				}
				let locgovCode = '<c:out value="${locgovSearchParam.shLocgovCode}"/>';

				let optionLists = $("#shLocgovCode")[0];
				for(let option of optionLists) {
					if (option.value == locgovCode) {
						option.setAttribute("selected", "selected");
						break;
					}
				}
		    });
		} else {
	        $('#shLocgovCode').append('<option value="">-시,군,구-</option>');
		}
	}


	function fnSearch() {
		designatedLocgovMonthCampaign();
		designatedLocgovMonthAmountRaised();
		designatedLocgovMonthAmount();
	}

	// 사업구분별 사업 진행건 비율
	function designatedLocgovMonthCampaign() {
        const labelArr = [];
        const dataArr = [];
        const backgroundColorArr = [];
		$.post('/opmanager/designated-donation/analysis/month/campaign', {"shWdr" : $('#shWdr').val(),  "shLocgovCode" : $('#shLocgovCode').val(), "selYear" : $("#selYear").val(), "prjStatus" : $("input[name='prjStatus']:checked").val()}, function(response) {
			if (response.isSuccess) {
				let label = '';
				let prjBsns = '';
				let backgroundColor = '';
				label = '취약계층';
				prjBsns = response.data[0].prjBsns100;
				//prjBsns = '34';
				backgroundColor = 'rgb(255, 99, 132)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				label = '문화/예술';
				prjBsns = response.data[0].prjBsns200;
				//prjBsns = '34';
				backgroundColor = 'rgb(54, 162, 235)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				label = '자원봉사';
				prjBsns = response.data[0].prjBsns300;
				//prjBsns = '17';
				backgroundColor = 'rgb(255, 205, 86)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				label = '복리증진';
				prjBsns = response.data[0].prjBsns400;
				//prjBsns = '14';
				backgroundColor = 'rgb(32, 169, 59)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				$('#tdCampaign1_1').text(response.data[0].columnTpDesc);
				$('#tdCampaign1_2').text(response.data[0].prjBsns100 + '%');
				$('#tdCampaign1_3').text(response.data[0].prjBsns200 + '%');
				$('#tdCampaign1_4').text(response.data[0].prjBsns300 + '%');
				$('#tdCampaign1_5').text(response.data[0].prjBsns400 + '%');
				$('#tdCampaign1_6').text(response.data[0].prjBsnsTot + '%');

				$('#tdCampaign2_1').text(response.data[1].columnTpDesc);
				$('#tdCampaign2_2').text(response.data[1].prjBsns100 + '건');
				$('#tdCampaign2_3').text(response.data[1].prjBsns200 + '건');
				$('#tdCampaign2_4').text(response.data[1].prjBsns300 + '건');
				$('#tdCampaign2_5').text(response.data[1].prjBsns400 + '건');
				$('#tdCampaign2_6').text(response.data[1].prjBsnsTot + '건');


                let p = $('#designatedLocgovMonthCampaignChart').parent('div');
                const ele = $('#designatedLocgovMonthCampaignChart').clone();
                p.empty();
                p.append(ele);
            	const context = document.getElementById('designatedLocgovMonthCampaignChart').getContext('2d');
                const chartData = {
	           		  labels: labelArr,
	           		  datasets: [{
	           		    label: '',
	           		    data: dataArr,
	           		    backgroundColor: backgroundColorArr,
	           		    hoverOffset: 4
	           		  }]
           		};

                try {
                    const c = new Chart(context, {
                        type : 'pie',
                        data : chartData
                        //options : defualtOptions,
                        //plugins : [ChartCommon.customTitle]
                    });
                } catch (e) {
                	$.log(e);
                	checkChartLib();
                }


			} else {
                alert(response.errorMessage);
            }
		});
	}


	function designatedLocgovMonthAmountRaised() {
        const labelArr = [];
        const dataArr = [];
        const backgroundColorArr = [];
		$.post('/opmanager/designated-donation/analysis/month/amountraised', {"shWdr" : $('#shWdr').val(),  "shLocgovCode" : $('#shLocgovCode').val(), "selYear" : $("#selYear").val(), "prjStatus" : $("input[name='prjStatus']:checked").val()}, function(response) {
			if (response.isSuccess) {
				let label = '';
				let prjBsns = '';
				let backgroundColor = '';

				label = '취약계층';
				prjBsns = response.data[0].prjBsns100;
				backgroundColor = 'rgb(255, 99, 132)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				label = '문화/예술';
				prjBsns = response.data[0].prjBsns200;
				backgroundColor = 'rgb(54, 162, 235)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				label = '자원봉사';
				prjBsns = response.data[0].prjBsns300;
				backgroundColor = 'rgb(255, 205, 86)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				label = '복리증진';
				prjBsns = response.data[0].prjBsns400;
				backgroundColor = 'rgb(32, 169, 59)';
				labelArr.push(label);
				dataArr.push(prjBsns);
				backgroundColorArr.push(backgroundColor);

				$('#tdAmountRaised1_1').text(response.data[0].columnTpDesc);
				$('#tdAmountRaised1_2').text(response.data[0].prjBsns100 + '%');
				$('#tdAmountRaised1_3').text(response.data[0].prjBsns200 + '%');
				$('#tdAmountRaised1_4').text(response.data[0].prjBsns300 + '%');
				$('#tdAmountRaised1_5').text(response.data[0].prjBsns400 + '%');
				$('#tdAmountRaised1_6').text(response.data[0].prjBsnsTot + '%');

				$('#tdAmountRaised2_1').text(response.data[1].columnTpDesc);
				$('#tdAmountRaised2_2').text(Common.numberFormat(response.data[1].prjBsns100) + '원');
				$('#tdAmountRaised2_3').text(Common.numberFormat(response.data[1].prjBsns200) + '원');
				$('#tdAmountRaised2_4').text(Common.numberFormat(response.data[1].prjBsns300) + '원');
				$('#tdAmountRaised2_5').text(Common.numberFormat(response.data[1].prjBsns400) + '원');
				$('#tdAmountRaised2_6').text(Common.numberFormat(response.data[1].prjBsnsTot) + '원');

				$('#tdAmountRaised3_1').text(response.data[2].columnTpDesc);
				$('#tdAmountRaised3_2').text(Common.numberFormat(response.data[2].prjBsns100) + '원');
				$('#tdAmountRaised3_3').text(Common.numberFormat(response.data[2].prjBsns200) + '원');
				$('#tdAmountRaised3_4').text(Common.numberFormat(response.data[2].prjBsns300) + '원');
				$('#tdAmountRaised3_5').text(Common.numberFormat(response.data[2].prjBsns400) + '원');
				$('#tdAmountRaised3_6').text(Common.numberFormat(response.data[2].prjBsnsTot) + '원');



                let p = $('#designatedLocgovMonthAmountRaisedChart').parent('div');
                const ele = $('#designatedLocgovMonthAmountRaisedChart').clone();
                p.empty();
                p.append(ele);
            	const context = document.getElementById('designatedLocgovMonthAmountRaisedChart').getContext('2d');
                const chartData = {
	           		  labels: labelArr,
	           		  datasets: [{
	           		    label: '',
	           		    data: dataArr,
	           		    backgroundColor: backgroundColorArr,
	           		    hoverOffset: 4
	           		  }]
           		};

                try {
                    const c = new Chart(context, {
                        type : 'pie',
                        data : chartData
                        //options : defualtOptions,
                        //plugins : [ChartCommon.customTitle]
                    });
                } catch (e) {
                	$.log(e);
                	checkChartLib();
                }
			} else {
                alert(response.errorMessage);
            }
		});
	}

	function designatedLocgovMonthAmount() {
		const monthArr = ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월', '12월'];
		const monthColumnArr = ['m01','m02','m03','m04','m05','m06','m07','m08','m09','m10','m11', 'm12'];
        const amountArr = [
        					{label : '목표금액(원)', type : 'bar', data : [], backgroundColor:'#00215A', borderColor:'#00215A', minBarLength: 0, yAxisID: 'y'}
        					, {label : '모금액(원)', type : 'bar', data : [], backgroundColor:'#09C2C7', borderColor:'#09C2C7', minBarLength: 0, yAxisID: 'y'}
        					//, {label : '테스트1(포인트)', type : 'line', data : [], backgroundColor:'#30815A', borderColor:'#30815A', minBarLength: 0, yAxisID: 'y1'}
        					//, {label : '테스트2(포인트)', type : 'line', data : [], backgroundColor:'#59C207', borderColor:'#59C207', minBarLength: 0, yAxisID: 'y1'}
       					];
		$.post(
			'/opmanager/designated-donation/analysis/month/amount'
			, {"shWdr" : $('#shWdr').val()
				, "shLocgovCode" : $('#shLocgovCode').val()
				, "selYear" : $("#selYear").val()
				, "prjStatus" : $("input[name='prjStatus']:checked").val()
			}
			, function(response) {
				if (response.isSuccess) {
					let monthLength = monthColumnArr.length;
					for(let i = 0 ; i < monthLength ; i++) {
						amountArr[0].data.push(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[0][monthColumnArr[i]] : 0);
			           	amountArr[1].data.push(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[1][monthColumnArr[i]] : 0);

						//amountArr[2].data.push(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[0][monthColumnArr[i]] + 10000000 : 0);
						//amountArr[3].data.push(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[0][monthColumnArr[i]] + 20000000 : 0);
					}


					$('#tdAmount1_1').text(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[0].columnTpDesc : 0);
					$('#tdAmount1_2').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m01) : 0);
					$('#tdAmount1_3').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m02) : 0);
					$('#tdAmount1_4').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m03) : 0);
					$('#tdAmount1_5').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m04) : 0);
					$('#tdAmount1_6').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m05) : 0);
					$('#tdAmount1_7').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m06) : 0);
					$('#tdAmount1_8').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m07) : 0);
					$('#tdAmount1_9').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m08) : 0);
					$('#tdAmount1_10').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m09) : 0);
					$('#tdAmount1_11').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m10) : 0);
					$('#tdAmount1_12').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m11) : 0);
					$('#tdAmount1_13').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].m12) : 0);
					$('#tdAmount1_14').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[0].total) : 0);

					$('#tdAmount2_1').text(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[1].columnTpDesc : 0);
					$('#tdAmount2_2').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m01) : 0);
					$('#tdAmount2_3').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m02) : 0);
					$('#tdAmount2_4').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m03) : 0);
					$('#tdAmount2_5').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m04) : 0);
					$('#tdAmount2_6').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m05) : 0);
					$('#tdAmount2_7').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m06) : 0);
					$('#tdAmount2_8').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m07) : 0);
					$('#tdAmount2_9').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m08) : 0);
					$('#tdAmount2_10').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m09) : 0);
					$('#tdAmount2_11').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m10) : 0);
					$('#tdAmount2_12').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m11) : 0);
					$('#tdAmount2_13').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].m12) : 0);
					$('#tdAmount2_14').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[1].total) : 0);

					$('#tdAmount3_1').text(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[2].columnTpDesc : 0);
					$('#tdAmount3_2').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m01) : 0);
					$('#tdAmount3_3').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m02) : 0);
					$('#tdAmount3_4').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m03) : 0);
					$('#tdAmount3_5').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m04) : 0);
					$('#tdAmount3_6').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m05) : 0);
					$('#tdAmount3_7').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m06) : 0);
					$('#tdAmount3_8').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m07) : 0);
					$('#tdAmount3_9').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m08) : 0);
					$('#tdAmount3_10').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m09) : 0);
					$('#tdAmount3_11').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m10) : 0);
					$('#tdAmount3_12').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m11) : 0);
					$('#tdAmount3_13').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].m12) : 0);
					$('#tdAmount3_14').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[2].total) : 0);

					$('#tdAmount4_1').text(!Common.isUndefined(response.data) && response.data.length > 0  ? response.data[3].columnTpDesc : 0);
					$('#tdAmount4_2').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m01) : 0);
					$('#tdAmount4_3').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m02) : 0);
					$('#tdAmount4_4').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m03) : 0);
					$('#tdAmount4_5').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m04) : 0);
					$('#tdAmount4_6').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m05) : 0);
					$('#tdAmount4_7').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m06) : 0);
					$('#tdAmount4_8').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m07) : 0);
					$('#tdAmount4_9').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m08) : 0);
					$('#tdAmount4_10').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m09) : 0);
					$('#tdAmount4_11').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m10) : 0);
					$('#tdAmount4_12').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m11) : 0);
					$('#tdAmount4_13').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].m12) : 0);
					$('#tdAmount4_14').text(!Common.isUndefined(response.data) && response.data.length > 0  ? Common.numberFormat(response.data[3].total) : 0);

					/*
					const amountChart = document.getElementById('designatedLocgovMonthAmountChart');
					if (Chart.getChart(amountChart)) {
						Chart.getChart(amountChart)?.destroy();
					}
					*/

				    if (locgovChart) {
			    		let d = {
				                lineTension: 0,
				                backgroundColor: "#92278F",
				                borderColor: "#92278F",
				                fill: false
			            };

			    		try {
			    			removeChartData(locgovChart);
			    		} catch (e) {
			    			$.log(e);
			    		}

			    		try {
			    			addChartData(locgovChart, monthColumnArr, amountArr);
			    		} catch (e) {
			    			$.log(e);
			    		}
				    } else {
				    	try {
					    	locgovChart = ChartCommon.drawChartMultiYaxis(
									'designatedLocgovMonthAmountChart'
									, 'bar'
									, {
										scales:{
							                x:{ //x축값 누적
							                    stacked:false
							                },
							                y:{ //y축값 누적
							                    stacked:false
							                }
							            }
							        }
									, monthArr
									, amountArr
									, {
										x : '월'
										, y : '원'
										//, y1 : '포인트'
									}
									, "FIXED"
								);
				    	} catch (e) {
				    		$.log(e);
				    		checkChartLib();
				    	}
				    }

				} else {
	                alert(response.errorMessage);
	            }
			}
		);
	}

	function addChartData(chart, label, data) {
	    let length = data.length;
	    for (let i = 0 ; i < length ; i++) {
	    	chart.data.datasets.push(data[i]);
	    }
	    chart.update();
	}

	function removeChartData(chart) {
	    let length = chart.data.datasets.length;
	    for (let i = 0 ; i < length ; i++) {
	    	chart.data.datasets.pop();
	    }
	    chart.update();
	}

	function checkChartLib() {
		if (chartLoadingYn) {
			chartLoadingYn = false;
			alert("그래프 라이브러리를 읽어오는데 실패했습니다. 새로고침 해주세요.");
		}
	}


</script>


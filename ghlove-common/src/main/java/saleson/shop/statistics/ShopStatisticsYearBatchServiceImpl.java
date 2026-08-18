package saleson.shop.statistics;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import saleson.shop.analysis.AnalysisMapper;
import saleson.shop.give.givestate.GiveStateMapper;
import saleson.shop.slave.SlaveShopStatisticsMapper;

@Slf4j
@Service("ShopStatisticsYearBatchService")
public class ShopStatisticsYearBatchServiceImpl implements ShopStatisticsYearBatchService {

	@Autowired
//	ShopStatisticsMapper shopStatisticsMapper;
	private SlaveShopStatisticsMapper shopStatisticsMapper;

	@Autowired
	private AnalysisMapper analysisMapper;

//	@Autowired
//	private YearsMapper yearsMapper;

	@Autowired
	GiveStateMapper giveStateMapper;

//	@Autowired
//	ShopPsintDatabaseConfig dsConfig;

	/**
	 * 연간통계 지자체243 작년총건수총금액
	 */
	@Override
	public void setStatisticsYearReport() {
		LocalDate date = LocalDate.now();
		LocalDate lastYear = date.minusYears(1);
		log.info(">>>> setStatisticsYearReport() >>>> " + lastYear.getYear());
		String setLastYear = String.valueOf(lastYear.getYear());//작년데이터 통계집계
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", setLastYear);

		try {
			//해당년도 데이터 있는지 확인(데이터 있으면 실행안함, 없으면 실행하여 데이터 생성)
			int cntData = analysisMapper.getListForSumAmtTnocsStatsCnt(hashMap);
			if(cntData < 1) {
				//연간통계 지자체243 작년총건수총금액
				List<HashMap<String, Object>> listAt = giveStateMapper.getListForSumAmtTnocsStats(hashMap);//총 건수, 총 금액
				if(listAt.size()>0) {
					for(HashMap<String, Object> hash : listAt) {
						hash.put("crtrYr", setLastYear);
						hash.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));
						hash.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));
						hash.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
						hash.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));
						hash.put("sumTnocs", hash.get("SUM_TNOCS"));
						hash.put("sumAmt", hash.get("SUM_AMT"));

						analysisMapper.insertListForYearSumAmtTnocs(hash);
					}
				}
			}
			cntData = 0;//초기화

			//연간통계 지자체243 기부경로방법별 현황
			List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프

			//해당년도 데이터 있는지 확인(데이터 있으면 실행안함, 없으면 실행하여 데이터 생성)
			cntData = analysisMapper.getListForPathAmtTnocsStatsCnt(hashMap);
			if(cntData < 1) {
				//연간통계 지자체243 기부경로방법별 현황
				if(locgovList.size() > 0) {
					for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.

						HashMap<String, Object> hashParam = new HashMap<>();
						hashParam.put("crtrYr", setLastYear);
						hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));
						//각 지자체의 경로방법별 통계가져옴
						List<HashMap<String, Object>> listPath = giveStateMapper.getListForPathAmtTnocsStats(hashParam);//지자체별 기부경로 리스트
						if(listPath.size() > 0) {//가져온 데이터에 상위코드와 상위이름 넣기
							for(HashMap<String, Object> hashUpCd : listPath) {
								HashMap<String, Object> hashInParam = new HashMap<>();

								hashInParam.put("crtrYr", setLastYear);
								hashInParam.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));//상위 지자체
								hashInParam.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));//상위 지자체
								hashInParam.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
								hashInParam.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));
								hashInParam.put("dntnPath", String.valueOf(hashUpCd.get("DNTN_PATH")));
								hashInParam.put("dntnPathCd", String.valueOf(hashUpCd.get("DNTN_PATH_CODE")));
								hashInParam.put("gramt", String.valueOf(hashUpCd.get("GRAMT")));
								hashInParam.put("tnocs", String.valueOf(hashUpCd.get("TNOCS")));
								hashInParam.put("gramtrt", String.valueOf(hashUpCd.get("GRAMTRT")));
								hashInParam.put("rt", String.valueOf(hashUpCd.get("RT")));
								hashInParam.put("od", String.valueOf(hashUpCd.get("OD")));

								analysisMapper.insertListForYearPathAmtTnocs(hashInParam);
							}
						}

					}

				}
			}

			cntData = 0;//초기화
			//해당년도 데이터 있는지 확인(데이터 있으면 실행안함, 없으면 실행하여 데이터 생성)
			cntData = analysisMapper.getListPastYearForDntnAmtTnocsStatsCnt(hashMap);
			if(cntData < 1) {
				//연간통계 지자체243 금액별 현황(건수,금액)
				if(locgovList.size() > 0) {
					for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
						HashMap<String, Object> hashParam = new HashMap<>();
						hashParam.put("crtrYr", setLastYear);
						hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));
						//각 지자체의 금액별 통계가져옴
						List<HashMap<String, Object>> listPrice = giveStateMapper.getListPastYearForDntnAmtTnocsStats(hashParam);
						if(listPrice.size() > 0) {
							for(HashMap<String, Object> hashUpCd : listPrice) {
								HashMap<String, Object> hashInParam = new HashMap<>();

								hashInParam.put("crtrYr", setLastYear);
								hashInParam.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));//상위 지자체
								hashInParam.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));//상위 지자체
								hashInParam.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
								hashInParam.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));

								hashInParam.put("gramt", String.valueOf(hashUpCd.get("GRAMT")));
								hashInParam.put("tnocs", String.valueOf(hashUpCd.get("TNOCS")));
								hashInParam.put("atm", String.valueOf(hashUpCd.get("ATM")));
								hashInParam.put("atmhund", String.valueOf(hashUpCd.get("ATMHUND")));

								analysisMapper.insertListForYearPriceAmtTnocs(hashInParam);

							}
						}
					}
				}
			}

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 1!",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 2!",e);
		}
	}


	/**
	 * 연간통계 지자체243 작년총건수총금액 - 연령별 and 월별금액건수
	 */
	@Override
	public void statisticsYearReportAgeMonthBatch() {
		LocalDate date = LocalDate.now();
		LocalDate lastYear = date.minusYears(1);
		log.info(">>>> setStatisticsYearReport() >>>> " + lastYear.getYear());
		String setLastYear = String.valueOf(lastYear.getYear());//작년데이터 통계집계
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", setLastYear);

		try {
			//연간통계 지자체243
			List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프

			//해당년도 데이터 있는지 확인(데이터 있으면 실행안함, 없으면 실행하여 데이터 생성)
			int cntData = analysisMapper.getListPastYearForAgeDntnAmtTnocsStatsCnt(hashMap);

			if(cntData < 1) {
				//연간통계 지자체243 연령별 현황(건수,금액)
				if(locgovList.size() > 0) {
					for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
						HashMap<String, Object> hashParam = new HashMap<>();
						hashParam.put("crtrYr", setLastYear);
						hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));
						//각 지자체의 연령별 통계가져옴
						List<HashMap<String, Object>> listAge = giveStateMapper.getListPastYearForAgeDntnAmtTnocsStats(hashParam);
						if(listAge.size() > 0) {
							for(HashMap<String, Object> hashUpCd : listAge) {
								HashMap<String, Object> hashInParam = new HashMap<>();

								hashInParam.put("crtrYr", setLastYear);
								hashInParam.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));//상위 지자체
								hashInParam.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));//상위 지자체
								hashInParam.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
								hashInParam.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));

								hashInParam.put("ages", String.valueOf(hashUpCd.get("AGES")));
								hashInParam.put("tnocs", String.valueOf(hashUpCd.get("TNOCS")));
								hashInParam.put("rt", String.valueOf(hashUpCd.get("RT")));
								hashInParam.put("asum", String.valueOf(hashUpCd.get("ASUM")));

								analysisMapper.insertListForYearAgesAmtTnocs(hashInParam);

							}
						}
					}
				}
			}
			cntData = 0;
			//연간통계 지자체243 월별 현황(건수,금액)
			cntData = analysisMapper.getListForMonthAmtTnocsStatsCnt(hashMap);
			if(cntData < 1) {
				if(locgovList.size() > 0) {
					for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
						HashMap<String, Object> hashParam = new HashMap<>();
						hashParam.put("crtrYr", setLastYear);
						hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));
						//각 지자체의 월별 통계가져옴
						List<HashMap<String, Object>> listAge = giveStateMapper.getListForMonthAmtTnocsStats(hashParam);
						if(listAge.size() > 0) {
							for(HashMap<String, Object> hashUpCd : listAge) {
								HashMap<String, Object> hashInParam = new HashMap<>();

								hashInParam.put("crtrYr", setLastYear);
								hashInParam.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));//상위 지자체
								hashInParam.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));//상위 지자체
								hashInParam.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
								hashInParam.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));

								hashInParam.put("cateMonth", String.valueOf(hashUpCd.get("CATE_MONTH")));
								hashInParam.put("snCnt", String.valueOf(hashUpCd.get("SN_CNT")));
								hashInParam.put("snCntRt", String.valueOf(hashUpCd.get("SN_CNT_RT")));
								hashInParam.put("amtSum", String.valueOf(hashUpCd.get("AMT_SUM")));
								hashInParam.put("amtSumRt", String.valueOf(hashUpCd.get("AMT_SUM_RT")));
								hashInParam.put("uniqId", String.valueOf(hashUpCd.get("UNIQ_ID")));

								analysisMapper.insertListForYearMonthAmtTnocs(hashInParam);

							}
						}
					}
				}
			}

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 1!",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 2!",e);
		}
	}

	/**
	 * 연간통계 지자체243 기부자 거주지별
	 */
	@Override
	public void statisticsYearReportPsintBatch() {
		LocalDate date = LocalDate.now();
		LocalDate lastYear = date.minusYears(1);
		log.info(">>>> statisticsYearReportPsintBatch() >>>> " + lastYear.getYear());
		String setLastYear = String.valueOf(lastYear.getYear());//작년데이터 통계집계
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", setLastYear);

		try {
			List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프
			//해당년도 데이터 있는지 확인(데이터 있으면 실행안함, 없으면 실행하여 데이터 생성)
			int cntData = analysisMapper.getListForPsintAmtTnocsStatsCnt(hashMap);

			if(cntData < 1) {
				if(locgovList.size() > 0) {
					for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
						HashMap<String, Object> hashParam = new HashMap<>();
						hashParam.put("crtrYr", setLastYear);
						hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));
						//각 지자체의 거주지별 통계가져옴
						List<HashMap<String, Object>> listPsint = giveStateMapper.getListForPsintAmtTnocsStats(hashParam);
						if(listPsint.size() > 0) {
							for(HashMap<String, Object> hashUpCd : listPsint) {
								HashMap<String, Object> hashInParam = new HashMap<>();

								hashInParam.put("crtrYr", setLastYear);
								hashInParam.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));//상위 지자체
								hashInParam.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));//상위 지자체
								hashInParam.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
								hashInParam.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));

								hashInParam.put("psintMctpvNm", String.valueOf(hashUpCd.get("GIVE_UPPER_LOCGOV_NM")));
								hashInParam.put("psintMctpvCd", String.valueOf(hashUpCd.get("GIVE_UPPER_LOCGOV_CODE")));
								hashInParam.put("psintLclgvNm", String.valueOf(hashUpCd.get("GIVE_LOCGOV_NM")));
								hashInParam.put("psintLclgvCd", String.valueOf(hashUpCd.get("GIVE_LOCGOV_CODE")));

								hashInParam.put("tnocs", String.valueOf(hashUpCd.get("TNOCS")));
								hashInParam.put("asum", String.valueOf(hashUpCd.get("SUM_AMT")));
								hashInParam.put("tnocsRt", String.valueOf(hashUpCd.get("TNOCS_RT")));
								hashInParam.put("asumRt", String.valueOf(hashUpCd.get("AMT_RT")));

								analysisMapper.insertListForYearPsintAmtTnocs(hashInParam);

							}
						}
					}
				}
			}

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 1!",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 2!",e);
		}

	}

	/**
	 * 연간통계 지자체243 인기 답례품 현황
	 */
	@Override
	public void statisticsYearReportGoodsBatch() {
		LocalDate date = LocalDate.now();
		LocalDate lastYear = date.minusYears(1);
		log.info(">>>> statisticsYearReportGoodsBatch() >>>> " + lastYear.getYear());
		String setLastYear = String.valueOf(lastYear.getYear());//작년데이터 통계집계
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", setLastYear);

		try {
			List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프
			//해당년도 데이터 있는지 확인(데이터 있으면 실행안함, 없으면 실행하여 데이터 생성)
			int cntData = analysisMapper.getListForPubGoodsPastYearStatsCnt(hashMap);
//			int cntData = yearsMapper.getListForPubGoodsPastYearStatsCntN(hashMap);

			if(cntData < 1) {
				if(locgovList.size() > 0) {
					for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
						HashMap<String, Object> hashParam = new HashMap<>();
						hashParam.put("crtrYr", setLastYear);
						hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));
						//답례품 인기순
//						List<HashMap<String, Object>> listGoods = analysisMapper.getListForPubGoodsPastYearStats(hashParam);
						List<HashMap<String, Object>> listGoods = giveStateMapper.getListForPubGoodsPastYearStats(hashParam);
//						List<HashMap<String, Object>> listGoods = yearsMapper.getListForPubGoodsPastYearStatsN(hashParam);

						if(listGoods.size() > 0) {
							for(HashMap<String, Object> hashUpCd : listGoods) {
								HashMap<String, Object> hashInParam = new HashMap<>();

								hashInParam.put("crtrYr", setLastYear);
								hashInParam.put("mctpvNm", String.valueOf(hash.get("UPPER_LOCGOV_NM")));//상위 지자체
								hashInParam.put("mctpvCd", String.valueOf(hash.get("UPPER_LOCGOV_CODE")));//상위 지자체
								hashInParam.put("lclgvNm", String.valueOf(hash.get("LOCGOV_NM")));
								hashInParam.put("lclgvCd", String.valueOf(hash.get("LOCGOV_CODE")));

								hashInParam.put("itemCode", String.valueOf(hashUpCd.get("ITEM_CODE")));
								hashInParam.put("itemName", String.valueOf(hashUpCd.get("ITEM_NAME")));
								hashInParam.put("price", String.valueOf(hashUpCd.get("PRICE")));
								hashInParam.put("sumPrice", String.valueOf(hashUpCd.get("SUM_PRICE")));
								hashInParam.put("quantitySum", String.valueOf(hashUpCd.get("SUM_QUANTITY")));

								analysisMapper.insertListForYearGoodsAmtTnocs(hashInParam);
//								yearsMapper.insertListForYearGoodsAmtTnocsN(hashInParam);

							}
						}
					}
				}
			}

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 1!",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 2!",e);
		}


	}

}

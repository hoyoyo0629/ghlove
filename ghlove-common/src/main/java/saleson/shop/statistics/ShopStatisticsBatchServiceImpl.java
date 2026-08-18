package saleson.shop.statistics;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import saleson.shop.analysis.AnalysisMapper;
import saleson.shop.give.givestate.GiveStateMapper;
import saleson.shop.slave.SlaveGiveStateMapper;
import saleson.shop.slave.SlaveShopStatisticsMapper;

@Slf4j
@Service("ShopStatisticsBatchService")
public class ShopStatisticsBatchServiceImpl implements ShopStatisticsBatchService {

	@Autowired
//	ShopStatisticsMapper shopStatisticsMapper;
	private SlaveShopStatisticsMapper shopStatisticsMapper;

	@Autowired
	private AnalysisMapper analysisMapper;

	@Autowired
	GiveStateMapper giveStateMapper;

	@Override
	public void setStatisticsReport() {
		List<HashMap<String, Object>> paramDateList = new ArrayList<>();
		List<HashMap<String, Object>> resultList = new ArrayList<>();

		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		// 1. 총괄현황 회원수
		try {
			String paramYr = analysisMapper.selectMaxYrMmForMbrTnocsStats(); //통계DB 최근 인입연월(YYYYMM) 조회
			paramDateList = shopStatisticsMapper.getParamDateListForMbrTnocsStats(paramYr); // 조회할 기준연월(YYYYMM) 리스트 조회
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString());
					hashMap.put("sysYear", sysYear);
					resultList = shopStatisticsMapper.getListForMbrTnocsStats(hashMap);
					analysisMapper.deleteListForMbrTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString());
						analysisMapper.insertListForMbrTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 2. 총괄현황 기부건수
		try {
			String paramYr = analysisMapper.selectMaxYrMmForDntnTnocsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForDntnTnocsStats(paramYr);
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForDntnTnocsStats(hashMap);
					resultList = shopStatisticsMapper.getListForDntnTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForDntnTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 3-2. 총괄현황 한도금액 기부건수 - 2000만원
		try {
			String paramYr = analysisMapper.selectMaxYrMmForDntnTnocsMaxAmtStats();
			paramDateList = shopStatisticsMapper.getParamDateListForDntnTnocsStats(paramYr);
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForDntnTnocsMaxAmtStats(hashMap);
					resultList = shopStatisticsMapper.getListForDntnTnocsMaxAmtStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForDntnTnocsMaxAmtStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 4. 총괄현황 답례품건수
		try {
			String paramYr = analysisMapper.selectMaxYrMmForGdsTnocsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForGdsTnocsStats(paramYr);
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString());
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForGdsTnocsStats(hashMap);
					resultList = shopStatisticsMapper.getListForGdsTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString());
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForGdsTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 5. 지자체별 기부현황(누계)
		try {
			String paramYr = analysisMapper.selectMaxYrMmForMctpvDntnStats();
			paramDateList = shopStatisticsMapper.getParamDateListForDntnTnocsStats(paramYr);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForMctpvDntnStats(hashMap);
					resultList = shopStatisticsMapper.getListForMctpvDntnStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForMctpvDntnStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}


		// 6-2. 지자체별 한도금액 기부현황(누계)-2000만원
		try {
			String paramYr = analysisMapper.selectMaxYrMmForMctpvDntnMaxAmtStats();
			paramDateList = shopStatisticsMapper.getParamDateListForDntnTnocsStats(paramYr);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForMctpvDntnMaxAmtStats(hashMap);
					resultList = shopStatisticsMapper.getListForMctpvDntnMaxAmtStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForMctpvDntnMaxAmtStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 7. 243개 지자체별 기부금현황
		try {
			String paramYr = analysisMapper.selectMaxYrMmForLclgvAodStats();
			paramDateList = shopStatisticsMapper.getParamDateListForDntnTnocsStats(paramYr);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForLclgvAodStats(hashMap);
					resultList = shopStatisticsMapper.getListForLclgvAodStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForLclgvAodStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}


		// 8-2. 243개 지자체별 한도금액 기부금현황-2000만원
		try {
			String paramYr = analysisMapper.selectMaxYrMmForLclgvAodMaxAmtStats();
			paramDateList = shopStatisticsMapper.getParamDateListForDntnTnocsStats(paramYr);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForLclgvAodMaxAmtStats(hashMap);
					resultList = shopStatisticsMapper.getListForLclgvAodMaxAmtStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString().substring(0,4));
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForLclgvAodMaxAmtStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 9. 지자체별 답례품제공현황
		try {
			String paramYr = analysisMapper.selectMaxYrMmForMctpvGdsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForGdsTnocsStats(paramYr);	// 답례품현황 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString());
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForMctpvGdsStats(hashMap);
					resultList = shopStatisticsMapper.getListForMctpvGdsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString());
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("UPPER_LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForMctpvGdsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 10. 243개 지자체별 답례품제공현황
		try {
			String paramYr = analysisMapper.selectMaxYrMmForLclgvGdsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForGdsTnocsStats(paramYr);	// 답례품현황 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYR").toString());
					hashMap.put("paramPrvYr", hashMap.get("PRVYR").toString().substring(0,4));
					hashMap.put("sysYear", sysYear);
					analysisMapper.deleteListForLclgvGdsStats(hashMap);
					resultList = shopStatisticsMapper.getListForLclgvGdsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYR").toString());
						if(hash.get("PRVYR_JAN").toString().equals("0")) { // 1월
							hash.put("PRVYR_NOW_YR_RT_JAN", "-1");
						} else {
							if(hash.get("NOW_YR_JAN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JAN", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jan = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JAN").toString()) / Float.parseFloat(hash.get("PRVYR_JAN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JAN", rt_jan);
								}
							}
						}
						if(hash.get("PRVYR_FEB").toString().equals("0")) { // 2월
							hash.put("PRVYR_NOW_YR_RT_FEB", "-1");
						} else {
							if(hash.get("NOW_YR_FEB").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_FEB", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_feb = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_FEB").toString()) / Float.parseFloat(hash.get("PRVYR_FEB").toString()));
									hash.put("PRVYR_NOW_YR_RT_FEB", rt_feb);
								}
							}
						}
						if(hash.get("PRVYR_MAR").toString().equals("0")) { // 3월
							hash.put("PRVYR_NOW_YR_RT_MAR", "-1");
						} else {
							if(hash.get("NOW_YR_MAR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAR", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_mar = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAR").toString()) / Float.parseFloat(hash.get("PRVYR_MAR").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAR", rt_mar);
								}
							}
						}
						if(hash.get("PRVYR_APR").toString().equals("0")) { // 4월
							hash.put("PRVYR_NOW_YR_RT_APR", "-1");
						} else {
							if(hash.get("NOW_YR_APR").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_APR", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_apr = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_APR").toString()) / Float.parseFloat(hash.get("PRVYR_APR").toString()));
									hash.put("PRVYR_NOW_YR_RT_APR", rt_apr);
								}
							}
						}
						if(hash.get("PRVYR_MAY").toString().equals("0")) { // 5월
							hash.put("PRVYR_NOW_YR_RT_MAY", "-1");
						} else {
							if(hash.get("NOW_YR_MAY").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_MAY", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_may = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_MAY").toString()) / Float.parseFloat(hash.get("PRVYR_MAY").toString()));
									hash.put("PRVYR_NOW_YR_RT_MAY", rt_may);
								}
							}
						}
						if(hash.get("PRVYR_JUN").toString().equals("0")) { // 6월
							hash.put("PRVYR_NOW_YR_RT_JUN", "-1");
						} else {
							if(hash.get("NOW_YR_JUN").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUN", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jun = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUN").toString()) / Float.parseFloat(hash.get("PRVYR_JUN").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUN", rt_jun);
								}
							}
						}
						if(hash.get("PRVYR_JUL").toString().equals("0")) { // 7월
							hash.put("PRVYR_NOW_YR_RT_JUL", "-1");
						} else {
							if(hash.get("NOW_YR_JUL").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_JUL", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_jul = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_JUL").toString()) / Float.parseFloat(hash.get("PRVYR_JUL").toString()));
									hash.put("PRVYR_NOW_YR_RT_JUL", rt_jul);
								}
							}
						}
						if(hash.get("PRVYR_AUG").toString().equals("0")) { // 8월
							hash.put("PRVYR_NOW_YR_RT_AUG", "-1");
						} else {
							if(hash.get("NOW_YR_AUG").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_AUG", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_aug = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_AUG").toString()) / Float.parseFloat(hash.get("PRVYR_AUG").toString()));
									hash.put("PRVYR_NOW_YR_RT_AUG", rt_aug);
								}
							}
						}
						if(hash.get("PRVYR_SEP").toString().equals("0")) { // 9월
							hash.put("PRVYR_NOW_YR_RT_SEP", "-1");
						} else {
							if(hash.get("NOW_YR_SEP").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_SEP", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_sep = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_SEP").toString()) / Float.parseFloat(hash.get("PRVYR_SEP").toString()));
									hash.put("PRVYR_NOW_YR_RT_SEP", rt_sep);
								}
							}
						}
						if(hash.get("PRVYR_OCT").toString().equals("0")) { // 10월
							hash.put("PRVYR_NOW_YR_RT_OCT", "-1");
						} else {
							if(hash.get("NOW_YR_OCT").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_OCT", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_oct = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_OCT").toString()) / Float.parseFloat(hash.get("PRVYR_OCT").toString()));
									hash.put("PRVYR_NOW_YR_RT_OCT", rt_oct);
								}
							}
						}
						if(hash.get("PRVYR_NOV").toString().equals("0")) { // 11월
							hash.put("PRVYR_NOW_YR_RT_NOV", "-1");
						} else {
							if(hash.get("NOW_YR_NOV").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_NOV", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_nov = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_NOV").toString()) / Float.parseFloat(hash.get("PRVYR_NOV").toString()));
									hash.put("PRVYR_NOW_YR_RT_NOV", rt_nov);
								}
							}
						}
						if(hash.get("PRVYR_DEC").toString().equals("0")) { // 12월
							hash.put("PRVYR_NOW_YR_RT_DEC", "-1");
						} else {
							if(hash.get("NOW_YR_DEC").toString().equals("0")) {
								hash.put("PRVYR_NOW_YR_RT_DEC", "0");
							} else {
								if(hash.get("LOCGOV_CODE").toString().equals("00000")) {
									String rt_dec = String.format("%.4f",Float.parseFloat(hash.get("NOW_YR_DEC").toString()) / Float.parseFloat(hash.get("PRVYR_DEC").toString()));
									hash.put("PRVYR_NOW_YR_RT_DEC", rt_dec);
								}
							}
						}
						analysisMapper.insertListForLclgvGdsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 11. 총괄누계현황 - 기부방법별 건수
		try {
			String paramYrMm = analysisMapper.selectMaxYrMmForDntnPathTnocsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForTotalDntnTnocsStats(paramYrMm);	// 기부건수 ParamDate와 같음 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
					hashMap.put("crtrMm", hashMap.get("CRTRYRMM").toString().substring(4,6));
//					analysisMapper.deleteListForDntnPathTnocsStats(hashMap);
					resultList = shopStatisticsMapper.getListForDntnPathTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
						hash.put("crtrMm", Integer.parseInt(hashMap.get("CRTRYRMM").toString().substring(4,6)));
						analysisMapper.insertListForDntnPathTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 12. 총괄누계현황 - 기부금액별 건수
		try {
			String paramYrMm = analysisMapper.selectMaxYrMmForDntnAmtTnocsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForTotalDntnTnocsStats(paramYrMm);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
					hashMap.put("crtrMm", hashMap.get("CRTRYRMM").toString().substring(4,6));
//					analysisMapper.deleteListForDntnAmtTnocsStats(hashMap);
					resultList = shopStatisticsMapper.getListForDntnAmtTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
						hash.put("crtrMm", Integer.parseInt(hashMap.get("CRTRYRMM").toString().substring(4,6)));
						if("10만원 미만".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "10");
						} else if ("10만원".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "20");
						} else if ("10~100만원 미만".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "30");
						} else if ("100~500만원 미만".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "40");
						} else if ("500만원".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "50");
						} else if ("500~2000만원 미만".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "60");
						} else if ("2000만원".equals(hash.get("GRAMT").toString())) {
							hash.put("AMTCD", "70");
						}  else {
							hash.put("AMTCD", "00");
						}
						analysisMapper.insertListForDntnAmtTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 13. 총괄누계현황 - 연령별 건수
		try {
			String paramYrMm = analysisMapper.selectMaxYrMmForDntnAgeTnocsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForTotalDntnTnocsStats(paramYrMm);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
					hashMap.put("crtrMm", hashMap.get("CRTRYRMM").toString().substring(4,6));
//					analysisMapper.deleteListForDntnAgeTnocsStats(hashMap);
					resultList = shopStatisticsMapper.getListForDntnAgeTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
						hash.put("crtrMm", Integer.parseInt(hashMap.get("CRTRYRMM").toString().substring(4,6)));
						if("20대 미만".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "10");
						} else if ("20대".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "20");
						} else if ("30대".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "30");
						} else if ("40대".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "50");
						} else if ("50대".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "50");
						} else if ("60대".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "60");
						} else if ("70대".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "70");
						} else if ("80대 이상".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "80");
						} else if ("생년월일 없음".equals(hash.get("AGES").toString())) {
							hash.put("AGECD", "90");
						} else {
							hash.put("AGECD", "00");
						}
						analysisMapper.insertListForDntnAgeTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}

		// 14. 총괄누계현황 - 거주지역 및 기부지역 별 건수
		try {
			String paramYrMm = analysisMapper.selectMaxYrMmForHabDntnMctpvTnocsStats();
			paramDateList = shopStatisticsMapper.getParamDateListForTotalDntnTnocsStats(paramYrMm);	// 기부건수 ParamDate와 같음
			if(paramDateList.size()>0) {
				for(HashMap<String, Object> hashMap : paramDateList) {
					hashMap.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
					hashMap.put("crtrMm", hashMap.get("CRTRYRMM").toString().substring(4,6));
//					analysisMapper.deleteListForHabDntnMctpvTnocsStats(hashMap);
					resultList = shopStatisticsMapper.getListForHabDntnMctpvTnocsStats(hashMap);
					for(HashMap<String, Object> hash : resultList) {
						hash.put("crtrYr", hashMap.get("CRTRYRMM").toString().substring(0,4));
						hash.put("crtrMm", Integer.parseInt(hashMap.get("CRTRYRMM").toString().substring(4,6)));
						analysisMapper.insertListForHabDntnMctpvTnocsStats(hash);
					}
				}
			}
		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch Error !",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch Error !",e);
		}
	}



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

			//연간통계 지자체243 기부경로방법별 현황
			List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프
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

							analysisMapper.insertListForYearPathAmtTnocs(hashInParam);
						}
					}

				}

			}

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


		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 1!",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 2!",e);
		}
	}


	/**
	 * 연간통계 지자체243 작년총건수총금액 - 연령별 and 월별금액건수 답례품순위
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

			//연간통계 지자체243 월별 현황(건수,금액)
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

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 1!",e);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch setStatisticsYearReport Error 2!",e);
		}
	}
}

package saleson.batch.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinepowers.framework.util.DateUtils;

import lombok.RequiredArgsConstructor;
import saleson.batch.BatchExecutionMapper;
import saleson.batch.configuration.JobPsintDatabaseConfig;
import saleson.common.Const;
import saleson.common.scheduling.domain.BatchExecution;
import saleson.shop.give.givestate.GiveStateMapper;
import saleson.shop.slave.SlaveShopStatisticsMapper;

@Service("jobPsintService")
@RequiredArgsConstructor
public class JobPsintServiceImpl extends EgovAbstractServiceImpl implements JobPsintService {
	private final BatchExecutionMapper batchExecutionMapper;

	@Autowired
	GiveStateMapper giveStateMapper;

	@Autowired
	private SlaveShopStatisticsMapper shopStatisticsMapper;

	@Autowired
	JobPsintDatabaseConfig dsConfig;

	private static final Logger log = LoggerFactory.getLogger(JobPsintServiceImpl.class);

	/**
	 *
	 *
	 *
	 * 여기는
	 * 배치 실행시간이 길때는 배치 실행로그를 따로따로 넣을수 있는 공간임
	 *
	 *
	 */



	// 1 . 거주지별 기부건수 기부금액
	@Transactional
	public void statisticsYearReportPsintBatch() {

		String executionDate = DateUtils.getToday(Const.DATE_FORMAT);
		BatchExecution batchExecution = null;
		batchExecution = new BatchExecution("JobPsintServiceImpl.statisticsYearReportPsintBatch()", executionDate);


		List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프
		LocalDate date = LocalDate.now();
		LocalDate lastYear = date.minusYears(1);
		log.info(">>>> statisticsYearReportPsintBatch() >>>> " + lastYear.getYear());
		String setLastYear = String.valueOf(lastYear.getYear());//작년데이터 통계집계
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", setLastYear);

		try {
		//배치 실행시간이 길때는 배치 실행로그를 따로따로 넣는다.
			if(locgovList.size() > 0) {
				DataSource dataSource = dsConfig.dataSource();
				int queryTimeout = 600;//10분
				Connection conn = dataSource.getConnection();

				for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
					HashMap<String, Object> hashParam = new HashMap<>();
					hashParam.put("crtrYr", setLastYear);
					hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));

					if(conn != null) {
						conn.setAutoCommit(false);
						String sql = "INSERT INTO YEAR_PSINT_AMT_TNOCS ( CRTR_YR, UPPER_LOCGOV_NM, UPPER_LOCGOV_CODE, LOCGOV_NM, LOCGOV_CODE, PSINT_UPPER_LOCGOV_NM, PSINT_UPPER_LOCGOV_CODE, PSINT_LOCGOV_NM, PSINT_LOCGOV_CODE, TNOCS, ASUM, TNOCS_RT, ASUM_RT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setQueryTimeout(queryTimeout);
						List<HashMap<String, Object>> listAge = giveStateMapper.getListForPsintAmtTnocsStats(hashParam);
						if(listAge.size() > 0) {

							for(HashMap<String, Object> hashUpCd : listAge) {
								pstmt.setString(1, setLastYear);
								pstmt.setString(2, String.valueOf(hash.get("UPPER_LOCGOV_NM")));
								pstmt.setString(3, String.valueOf(hash.get("UPPER_LOCGOV_CODE")));
								pstmt.setString(4, String.valueOf(hash.get("LOCGOV_NM")));
								pstmt.setString(5, String.valueOf(hash.get("LOCGOV_CODE")));
								pstmt.setString(6, String.valueOf(hashUpCd.get("GIVE_UPPER_LOCGOV_NM")));
								pstmt.setString(7, String.valueOf(hashUpCd.get("GIVE_UPPER_LOCGOV_CODE")));
								pstmt.setString(8, String.valueOf(hashUpCd.get("GIVE_LOCGOV_NM")));
								pstmt.setString(9, String.valueOf(hashUpCd.get("GIVE_LOCGOV_CODE")));
								pstmt.setString(10, String.valueOf(hashUpCd.get("TNOCS")));
								pstmt.setString(11, String.valueOf(hashUpCd.get("SUM_AMT")));
								pstmt.setString(12, String.valueOf(hashUpCd.get("TNOCS_RT")));
								pstmt.setString(13, String.valueOf(hashUpCd.get("AMT_RT")));

								pstmt.addBatch();

							}
							pstmt.executeBatch();
							conn.commit();
						}
					}


				}
			}

			batchExecution.setResult("1");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			batchExecutionMapper.mergeBatchExecution(batchExecution);

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch statisticsYearReportPsintBatch Error 1!",e);
			batchExecution.setResult("2");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			batchExecutionMapper.mergeBatchExecution(batchExecution);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch statisticsYearReportPsintBatch Error 2!",e);
			batchExecution.setResult("2");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			batchExecutionMapper.mergeBatchExecution(batchExecution);
		}

	}

	// 2 . 답례품 인기순위
	@Transactional
	public void statisticsYearReportGoodsBatch() {
		String executionDate = DateUtils.getToday(Const.DATE_FORMAT);
		BatchExecution batchExecution = null;
		batchExecution = new BatchExecution("JobPsintServiceImpl.statisticsYearReportGoodsBatch()", executionDate);


		List<HashMap<String, Object>> locgovList = giveStateMapper.getUppLocgovCodeList();//모든 지자체243만큼 루프
		LocalDate date = LocalDate.now();
		LocalDate lastYear = date.minusYears(1);
		log.info(">>>> statisticsYearReportGoodsBatch() >>>> " + lastYear.getYear());
		String setLastYear = String.valueOf(lastYear.getYear());//작년데이터 통계집계
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", setLastYear);

		try {
		//배치 실행시간이 길때는 배치 실행로그를 따로따로 넣는다.
			if(locgovList.size() > 0) {
				DataSource dataSource = dsConfig.dataSource();
				int queryTimeout = 900;//15분
				Connection conn = dataSource.getConnection();

				for(HashMap<String, Object> hash : locgovList) {//243번 루프돈다.
					HashMap<String, Object> hashParam = new HashMap<>();
					hashParam.put("crtrYr", setLastYear);
					hashParam.put("locgovCode", String.valueOf(hash.get("LOCGOV_CODE")));

					if(conn != null) {
						conn.setAutoCommit(false);
						String sql = "INSERT INTO YEAR_GOODS_AMT_TNOCS ( CRTR_YR, UPPER_LOCGOV_NM, UPPER_LOCGOV_CODE, LOCGOV_NM, LOCGOV_CODE, ITEM_CODE, ITEM_NAME, PRICE, SUM_PRICE, SUM_QUANTITY) VALUES (?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setQueryTimeout(queryTimeout);
						List<HashMap<String, Object>> listAge = giveStateMapper.getListForPubGoodsPastYearStats(hashParam);
						if(listAge.size() > 0) {

							for(HashMap<String, Object> hashUpCd : listAge) {
								pstmt.setString(1, setLastYear);
								pstmt.setString(2, String.valueOf(hash.get("UPPER_LOCGOV_NM")));
								pstmt.setString(3, String.valueOf(hash.get("UPPER_LOCGOV_CODE")));
								pstmt.setString(4, String.valueOf(hash.get("LOCGOV_NM")));
								pstmt.setString(5, String.valueOf(hash.get("LOCGOV_CODE")));
								pstmt.setString(6, String.valueOf(hashUpCd.get("ITEM_CODE")));
								pstmt.setString(7, String.valueOf(hashUpCd.get("ITEM_NAME")));
								pstmt.setString(8, String.valueOf(hashUpCd.get("PRICE")));
								pstmt.setLong(9, Long.parseLong(String.valueOf(hashUpCd.get("SUM_PRICE"))));
								pstmt.setLong(10, Long.parseLong(String.valueOf(hashUpCd.get("SUM_QUANTITY"))));

								pstmt.addBatch();

							}
							pstmt.executeBatch();
							conn.commit();
						}
					}


				}
			}

			batchExecution.setResult("1");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			batchExecutionMapper.mergeBatchExecution(batchExecution);

		} catch (RuntimeException e) {
			log.error("ShopStatisticsBatch statisticsYearReportGoodsBatch Error 1!",e);
			batchExecution.setResult("2");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			batchExecutionMapper.mergeBatchExecution(batchExecution);
		} catch (Exception e) {
			log.error("ShopStatisticsBatch statisticsYearReportGoodsBatch Error 2!",e);
			batchExecution.setResult("2");
			batchExecution.setEndTime(DateUtils.getToday("HH:mm:ss"));
			batchExecutionMapper.mergeBatchExecution(batchExecution);
		}

	}

}

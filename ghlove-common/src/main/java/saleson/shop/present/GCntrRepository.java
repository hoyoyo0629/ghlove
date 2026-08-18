package saleson.shop.present;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import saleson.model.GCntr;

public interface GCntrRepository extends JpaRepository<GCntr, String>, JpaSpecificationExecutor<GCntr> {

	// 유저의 잔액 포인트가 남아 있는 리스트
	List<GCntr> findByUserIdAndDeleteAtAndCntrBlcePointGreaterThanOrderByCntrSnAsc(Long userId, String deleteAt, Integer cntrBlcePoint);

	// 유저의 해당 지자체의 잔액이 남아 있는 리스트 (오래된순)
	List<GCntr> findByUserIdAndCntrBlcePointGreaterThanAndCntrLocgovCodeOrderByPayValidDeAscCntrSnAsc(Long userId, Integer cntrBlcePoint, String cntr_locgov_code);
	List<GCntr> findByUserIdAndCntrBlcePointGreaterThanAndCntrLocgovCodeOrderByFrstRegistPnttmAscCntrSnDesc(Long userId, Integer cntrBlcePoint, String cntr_locgov_code);	// 기존껀데 이상해서 변경함.

	@Query(value = "select "
			+ "	IFNULL(to_number(max(cntr_sn)) + 1, concat(:cntr_de, :cntr_locgov_code, '000001')) as cntr_sn "
			+ " from g_cntr "
			+ " where cntr_de = :cntr_de "
			+ " and cntr_locgov_code =  :cntr_locgov_code", nativeQuery = true)
	String findMaxByCntrDeAndCntrLocgovCode(@Param(value = "cntr_de") String cntr_de, @Param(value = "cntr_locgov_code") String cntr_locgov_code);

	@Query(value = "select "
			+ "	IFNULL(sum(cntr_blce_point), 0) as sum_cntr_blce_point "
			+ " from g_cntr "
			+ " where delete_at = 'N' "
			+ " and user_id = :user_id "
			+ " and cntr_locgov_code = :cntr_locgov_code", nativeQuery = true)
	int findSumByUserIdeAndCntrLocgovCode(@Param(value = "user_id") String user_id, @Param(value = "cntr_locgov_code") String cntr_locgov_code);

}
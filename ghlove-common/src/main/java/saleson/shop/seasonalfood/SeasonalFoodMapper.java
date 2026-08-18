package saleson.shop.seasonalfood;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.seasonalfood.domain.SeasonalFood;

@Mapper("seasonalFoodMapper")
public interface SeasonalFoodMapper {
	
	/**
	 * 제철식품관 관리 정보 조회
	 * @param 
	 * @return List<SeasonalFood>
	 */
	List<SeasonalFood> getSeasonalFood(SeasonalFood seasonalFood);
	
	/**
	 * 제철식품관 관리 정보 조회
	 * @param 
	 * @return List<SeasonalFood>
	 */
	List<SeasonalFood> getSeasonalFoodDisplay();
	
	/**
	 * 제철식품관 관리 정보 저장
	 * @param seasonalFood
	 * @return int
	 */
//	int saveSeasonalFood(SeasonalFood seasonalFood);
	
	/**
	 * 제철식품관 관리 정보 추가
	 * @param seasonalFood
	 * @return int
	 */
	int insertSeasonalFood(SeasonalFood seasonalFood);
	
	/**
	 * 제철식품관 관리 정보 삭제
	 * @param seasonalFood
	 * @return int
	 */
	int deleteSeasonalFood(SeasonalFood seasonalFood);

}

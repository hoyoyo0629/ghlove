package saleson.shop.seasonalfood;

import java.util.List;

import saleson.shop.item.domain.Item;
import saleson.shop.seasonalfood.domain.SeasonalFood;

public interface SeasonalFoodService {

	/**
	 * 제철식품관 관리 정보 조회
	 * @param
	 * @return List<SeasonalFood>
	 */
	List<SeasonalFood> getSeasonalFood();

	/**
	 * 제철식품관 관리 정보 조회
	 * @param
	 * @return List<SeasonalFood>
	 */
	List<SeasonalFood> getFrontSeasonFood();

	/**
	 * 제철식품관 관리 정보 저장
	 * @param seasonalFoods
	 * @return List<SeasonalFood>
	 */
	List<SeasonalFood> saveSeasonalFood(List<SeasonalFood> seasonalFoods);

	/**
	 * 제철식품관 상품 조회
	 * @param seasonalFoods
	 * @return List<SeasonalFood>
	 */
	List<Item> getSeasonalFoodKeywordItem(SeasonalFood seasonalFood);

	/**
	 * 제철식품관 상품 조회 카운트
	 * @param seasonalFoods
	 * @return int
	 */
	int getSeasonalFoodKeywordItemCnt(SeasonalFood seasonalFood);



}

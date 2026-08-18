package saleson.shop.seasonalfood;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import saleson.common.utils.UserUtils;
import saleson.shop.item.ItemMapper;
import saleson.shop.item.domain.Item;
import saleson.shop.seasonalfood.domain.SeasonalFood;

@Service("seasonalFoodService")
public class SeasonalFoodServiceImpl implements SeasonalFoodService {

	@Autowired
	private SeasonalFoodMapper seasonalFoodMapper;

	@Autowired
	private ItemMapper itemMapper;

	/**
	 * 제철식품관 관리 정보 조회
	 * @param
	 * @return List<SeasonalFood>
	 */
	@Override
	public List<SeasonalFood> getSeasonalFood() {
		List<SeasonalFood> result = seasonalFoodMapper.getSeasonalFoodDisplay();

		// 테스트용
//		SeasonalFood seasonalFood = new SeasonalFood();
//		seasonalFood.setSeasonalFoodMonth(5);
//		List<Map<String, String>> searchItems = seasonalFoodMapper.getSeasonalFoodKeywordItem(seasonalFood);
		// 테스트용

		return makeFullSeasonalKeyword(result);
	}

	/**
	 * 제철식품관 관리 정보 조회
	 * @param
	 * @return List<SeasonalFood>
	 */
	@Override
	public List<SeasonalFood> getFrontSeasonFood() {
		return seasonalFoodMapper.getSeasonalFoodDisplay();
	}


	/**
	 * 제철식품관 관리 정보 저장
	 * @param seasonalFoods
	 * @return List<SeasonalFood>
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public List<SeasonalFood> saveSeasonalFood(List<SeasonalFood> seasonalFoods) throws RuntimeException {
		long userId = UserUtils.getUserId();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		for (SeasonalFood seasonalFood : seasonalFoods) {
//			seasonalFoodMapper.saveSeasonalFood(seasonalFood);

			// db 구조 변경으로 로직 수정
			seasonalFoodMapper.deleteSeasonalFood(seasonalFood);
			String[] keywords = seasonalFood.getSeasonalFoodKeyword().split(",");
			if (keywords != null) {
				int i = 0;
				for (String keyword : keywords) {
					if (keyword != null && !keyword.trim().isEmpty()) {
						i++;
						SeasonalFood insertSeasonalFood = new SeasonalFood();
						insertSeasonalFood.setSeasonalFoodMonth(seasonalFood.getSeasonalFoodMonth());
						insertSeasonalFood.setSeasonalFoodKeyword(keyword.trim());
						insertSeasonalFood.setRegSeq(i);
						insertSeasonalFood.setFrstRegisterId(userId);
						insertSeasonalFood.setFrstRegistPnttm(now);
						insertSeasonalFood.setLastUpdusrId(userId);
						insertSeasonalFood.setLastUpdtPnttm(now);

						seasonalFoodMapper.insertSeasonalFood(insertSeasonalFood);
					}
				}
			}


		}
		return getSeasonalFood();
	}



	/**
	 * 키워드로 상품 검색(테스트)
	 * @param
	 * @return int
	 */
//	private int getSeasonalItemByKeyword(String[] keywords) {
////		if (keywords != null && keywords.length > 0) {
//			return seasonalFoodMapper.getSeasonalItemByKeyword(keywords);
////		}
////		return 0;
//	}

	/**
	 * 1~12월 제철식품 데이터 생성
	 * @param List<SeasonalFood>
	 * @return List<SeasonalFood>
	 */
	private List<SeasonalFood> makeFullSeasonalKeyword(List<SeasonalFood> list) {
		if (list == null) {
			list = new ArrayList<>();
		}
		List<SeasonalFood> data = new ArrayList<>();
		final int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		int[] notExistsMonths = new int[12];
		int inputCnt = 0;
		for (int month : months) {
			boolean isExist = false;
			for (SeasonalFood seasonalFood : list) {
				if (month == seasonalFood.getSeasonalFoodMonth()) {
					data.add(seasonalFood);
					isExist = true;
				}
			}
			if (!isExist) {
				notExistsMonths[inputCnt++] = month;
			}
		}

		for (int i : notExistsMonths) {
			if (i > 0) {
				SeasonalFood seasonalFood = new SeasonalFood();
				seasonalFood.setSeasonalFoodMonth(i);
				data.add(seasonalFood);
			}
		}

		Collections.sort(data, new Comparator<SeasonalFood>() {
			@Override
			public int compare(SeasonalFood o1, SeasonalFood o2) {
				return o1.getSeasonalFoodMonth() - o2.getSeasonalFoodMonth();
			}
		});

		return data;
	}


	@Override
	public List<Item> getSeasonalFoodKeywordItem(SeasonalFood seasonalFood) {
		return itemMapper.getSeasonalFoodKeywordItem(seasonalFood);
	}


	@Override
	public int getSeasonalFoodKeywordItemCnt(SeasonalFood seasonalFood) {
		return itemMapper.getSeasonalFoodKeywordItemCnt(seasonalFood);
	}
}

package saleson.shop.seasonalfood.support;

import java.util.List;

import saleson.shop.seasonalfood.domain.SeasonalFood;

public class SeasonalFoodParam {
	
	private List<SeasonalFood> seasonalFoods;

	public SeasonalFoodParam() {
		
	}

	public List<SeasonalFood> getSeasonalFoods() {
		return seasonalFoods;
	}

	public void setSeasonalFoods(List<SeasonalFood> seasonalFoods) {
		this.seasonalFoods = seasonalFoods;
	}
	
	
}

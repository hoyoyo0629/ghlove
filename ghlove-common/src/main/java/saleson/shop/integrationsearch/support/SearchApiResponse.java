package saleson.shop.integrationsearch.support;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import saleson.shop.integrationsearch.domain.ItemFlat;

@Data
// 정의되지 않은 Key값은 무시
// sortKey, location 같은 Key값들 오토매핑할 때 무시
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchApiResponse<T> {
	private Result<T> result;

	@Data
	// Snake Case(Response) ↔ Camel Case(변수명) 매핑 어노테이션
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Result<T> {
		private int totalCount;

		private List<Row<T>> rows;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Row<T> {
		private T fields;

	}


}

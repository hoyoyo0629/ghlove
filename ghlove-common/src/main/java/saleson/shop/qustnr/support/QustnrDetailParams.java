package saleson.shop.qustnr.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QustnrDetailParams {
	private long userId;
	private long qustnrSn;
}

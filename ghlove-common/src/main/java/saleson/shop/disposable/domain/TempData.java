package saleson.shop.disposable.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TempData {
	
	// 데이터 임시 키
	private String dataId;
	
	// 암호화 데이터
	private String encData;
	
	// 결과 메시지
	private Timestamp regDt;
	
}

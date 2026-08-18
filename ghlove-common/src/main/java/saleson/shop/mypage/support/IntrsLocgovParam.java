package saleson.shop.mypage.support;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrsLocgovParam {
	
	private long userId;
	private List<String> locgovArr;
	
}

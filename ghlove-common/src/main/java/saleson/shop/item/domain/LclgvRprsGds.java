package saleson.shop.item.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LclgvRprsGds extends Item {
	
	/**
	 * 정렬 순서
	 */
	int sortSeq;
		
	/**
	 * 상위 지자체 코드	(지자체 코드, 지자체 명은 ItemBase 객체에 있어서 제외함)
	 */
	String upperLocgovCode;
	
	/**
	 * 상위 지자체 명
	 */
	String upperLocgovNm;
	
}

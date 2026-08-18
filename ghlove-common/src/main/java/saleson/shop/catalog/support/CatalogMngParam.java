package saleson.shop.catalog.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CatalogMngParam extends SearchParam {


	private static final long serialVersionUID = -1438890165647176646L;
	
	
	// 소식지 발간년도
	private int catalogYear;
	private String catalogYearStr;
	
	// 소식지 발간호수
	private int catalogNo;
	private String catalogNoStr;
	
	// 소식지 카운트
	private int rowNumber;
	
	// 소식지 중복체크
	private boolean isDuplicate;
	private int dupCnt;
	
	// 대민화면 표시 여부
	private String displayYn;
	
	// 로그인된 아이디
	private long userId;
	
	// 상위 지자체 코드
	private String upperLocgovCode;
	
	// 지자체 코드
	private String locgovCode;
	
	// 아이템 코드 리스트(더보기)
	private String itemUserCodeList;
	
	public String[] getItemUserCodeListArr() {
		if(itemUserCodeList == null) {
			return null;
		} else {
			String[] itemList = itemUserCodeList.split(",");
			if(itemList.length == 0) {
				return null;
			} else {
				return itemList;
			}
		}
	}
}

package saleson.shop.catalog.support;

import org.springframework.util.StringUtils;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LocgovFavItemMngParam  extends SearchParam {

	private static final long serialVersionUID = 1087890625762237813L;

	// 지자체 상위코드
	private String upperLocgovCode;
	
	// 지자체코드
	private String locgovCode;
	
	// 지자체 명
	private String locgovNm;
	
	// 검색 시작일
	private String searchStartDate;
	
	// 검색 종료일
	private String searchEndDate;
	
	// 소식지 공개여부
	private String deleteYn;
	
	// 소식지 발간년도-호수 키
	private String catalogYearNo;
	
	// 아이템 코드 리스트(더보기)
	private String itemUserCodeList;
	
	/**
	 * 발간년도-호수 키 값으로 발간년도 추출
	 * @return
	 */
	public int getCatalogYear() {
		if (StringUtils.hasLength(catalogYearNo)) {
			String[] splitData = catalogYearNo.split("-");
			int length = splitData.length;
			if (length == 2) {
				try {
					return Integer.valueOf(splitData[0]);	
				} catch (NumberFormatException e) {
					return 0;
				}
			}
		}
		return 0;
	}
	
	/**
	 * 발간년도-호수 키 값으로 발간호수 추출
	 * @return
	 */
	public int getCatalogNo() {
		if (StringUtils.hasLength(catalogYearNo)) {
			String[] splitData = catalogYearNo.split("-");
			int length = splitData.length;
			if (length == 2) {
				try {
					return Integer.valueOf(splitData[1]);	
				} catch (NumberFormatException e) {
					return 0;
				}
			}
		}
		return 0;
	}
	
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

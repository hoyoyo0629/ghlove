package saleson.shop.catalog.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class LocgovFavItemMng extends ListParam{

	// 지자체 상위코드
	private String upperLocgovCode;
	
	// 지자체 코드
	private String locgovCode;
	
	// 소식지 버전 아이디
	private long catalogMngId;
	
	// 삭제 여부(Y, N)
	private String deleteYn;
	
	// 인기답례품 답례품아이디
	private long itemId;
	
	// 최초 등록자 
	private long frstRegisterId;
	
	// 최초 등록일시
	private Timestamp frstRegistPnttm;
	
	// 최종 수정자
	private long lastUpdusrId;
	
	// 최종 수정일시
	private Timestamp lastUpdtPnttm;
	
	// 지자체 명
	private String locgovNm;
	
	// 표시여부(소식지 버전 표시 여부) - 진행상태
	private String displayYn;
	
	// 답례품 코드
	private String itemUserCode;

	// 답례품 가격
	private long salePrice;
	
	// 답례품 이름
	private String itemName;
	
	// 발간년도
	private Integer catalogYear;
		
	// 발간 호
	private Integer catalogNo;
	
	// 유저아이디
	private long userId;
	 
	// 아이템이미지
	private String itemImage;
	             
	// 아이템코드
	private String itemCode;
	             
	// 아이템 데이터 상태코드
	private String dataStatusCode;
	             
	// 아이템 품절여부
	private String itemSoldOutFlag;
	             
	// 공개여부
	private String displayFlag;
	
	// 판매자 상호명(대민)
	private String companyName;
	
	/**
	 * 최초 등록일시 yyyy-MM-dd HH:mm:ss 형티로 리턴
	 * @return String
	 */
	public String getRegDateStr() {
		return timestampToStr(frstRegistPnttm);
	}
	
	/**
	 * 최종 수정일시 yyyy-MM-dd HH:mm:ss 형티로 리턴
	 * @return String
	 */
	public String getUpdtDateStr() {
		return timestampToStr(lastUpdtPnttm);
	}
	
	private String timestampToStr(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		LocalDateTime localDateTime = timestamp.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(formatter);
	}
	
}

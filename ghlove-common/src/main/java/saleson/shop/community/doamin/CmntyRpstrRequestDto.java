package saleson.shop.community.doamin;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CmntyRpstrRequestDto extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long rpstrId;
	private String locgovCode;	//지자체
	private String startDt;	//시작일시
	private String endDt;	//종료일시
	private String searchRole;	//소속
	private String query;
	private String where;
	private String shWdr;

}
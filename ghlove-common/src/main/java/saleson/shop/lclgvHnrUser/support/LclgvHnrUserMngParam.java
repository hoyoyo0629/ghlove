package saleson.shop.lclgvHnrUser.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saleson.common.Const;
import saleson.shop.designateddonation.domain.PrjImage;
import saleson.shop.designateddonation.domain.PrjImageExplain;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LclgvHnrUserMngParam extends SearchParam {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// 지자체 코드
	private String lclgvCd;

	// 상위 지자체 코드
	private String upperLocgovCode;

	// 아이디
	private long prjId;

	// 이미지 순서
	private int imgSeq;

	// 이미지 설명
	private String imgExpln;

	// 상품 이미지 설명 접근성
	private List<PrjImageExplain> prjImageExplain = new ArrayList<>();

	private List<PrjImage> prjImages = new ArrayList<>();

	// 명예사용자 설정 제목
	private String hnrUserStngTtl;

	//사용자명
	private String userName;

	private String startDate;
	private String endDate;


}

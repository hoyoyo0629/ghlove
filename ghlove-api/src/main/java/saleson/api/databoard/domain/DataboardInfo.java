package saleson.api.databoard.domain;

import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.util.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.shop.databoard.domain.Databoard;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataboardInfo {

	private Integer dataId;
	private String noticeFlag;
	private String subject;
	private String content;
	private String createdDate;
	private String link;
	private String targetOption;
	private String relOption;
	private String fileTy;
	private Integer hits;
	private String userName;
	
	public DataboardInfo(Databoard databoard) {
		if(databoard != null) {
			setDataId(CommonUtils.intNvl(databoard.getDataId()));
			setSubject(CommonUtils.dataNvl(databoard.getSubject()));
			setContent(CommonUtils.dataNvl(databoard.getContent()));
			
			String createdDate = CommonUtils.dataNvl(databoard.getCreatedDate());
			
			if (!ObjectUtils.isEmpty(createdDate)) {
				createdDate = DateUtils.date(createdDate);
			}

			setCreatedDate(createdDate);

			String url = CommonUtils.dataNvl(databoard.getUrl());
			String urlType = CommonUtils.dataNvl(databoard.getUrlType());

			if ("0".equals(urlType)) {
				url = SalesonProperty.getSalesonUrlShoppingmall() + url;
			}

			setLink(url);
			setTargetOption(CommonUtils.dataNvl(databoard.getTargetOption()));
			setRelOption(CommonUtils.dataNvl(databoard.getRelOption()));
			setNoticeFlag(CommonUtils.dataNvl(databoard.getNoticeFlag()));
			setFileTy(CommonUtils.dataNvl(databoard.getFileTy()));
			setHits(CommonUtils.intNvl(databoard.getHits()));
			setUserName(CommonUtils.dataNvl(databoard.getUserName()));
		}
	}
}

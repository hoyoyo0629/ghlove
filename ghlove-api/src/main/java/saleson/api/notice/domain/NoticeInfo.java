package saleson.api.notice.domain;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.shop.notice.domain.Notice;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeInfo {

    private int noticeId;
    private String noticeFlag;
    private String subject;
    private String content;
    private String createdDate;
    private String link;
    private String targetOption;
    private String relOption;
    private String userName;
    private int hits;

    public NoticeInfo(Notice notice) {

        if (notice != null) {
            setNoticeId(CommonUtils.intNvl(notice.getNoticeId()));
            setSubject(CommonUtils.dataNvl(notice.getSubject()));
            setContent(CommonUtils.dataNvl(notice.getContent()));

            String createdDate = CommonUtils.dataNvl(notice.getCreatedDate());

            if (!ObjectUtils.isEmpty(createdDate)) {
                createdDate = DateUtils.date(createdDate);
            }

            setCreatedDate(createdDate);

            String url = CommonUtils.dataNvl(notice.getUrl());
            String urlType = CommonUtils.dataNvl(notice.getUrlType());

            if ("0".equals(urlType)) {
                url = SalesonProperty.getSalesonUrlShoppingmall() + url;
            }

            setLink(url);
            setTargetOption(CommonUtils.dataNvl(notice.getTargetOption()));
            setRelOption(CommonUtils.dataNvl(notice.getRelOption()));

            setNoticeFlag(CommonUtils.dataNvl(notice.getNoticeFlag()));
            setUserName(CommonUtils.dataNvl(notice.getUserName()));
            
            if (!ObjectUtils.isEmpty(notice.getHits())) {
            	setHits(notice.getHits());
            }
            
        }
    }
}

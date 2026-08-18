package saleson.api.representativebanner.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import saleson.api.representativebanner.domain.RepresentativeBannerInfo;
import saleson.shop.representativebanner.domain.RepresentativeBanner;

@Component
public class RepresentativeBannerDataSupport {
	public List<RepresentativeBannerInfo> representativeBannerDataSet(List<RepresentativeBanner> list) {
        List<RepresentativeBannerInfo> resultList = new ArrayList<>();

        if (list != null && !list.isEmpty()) {
            for (RepresentativeBanner representativeBanner : list) {
                resultList.add(new RepresentativeBannerInfo(representativeBanner));
            }
        }

        return resultList;
    }
}

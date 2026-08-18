package saleson.api.mypage.domain;

import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import saleson.common.configuration.SalesonProperty;
import saleson.shop.userlevel.domain.UserLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLevelInfo {

    private int levelId;
    private String groupCode;
    private int depth;
    private String levelName;
    private String fileSrc;
    private int priceStart;
    private int priceEnd;

    private float discountRate;
    private float pointRate;
    private int shippingCouponCount;

    private int retentionPeriod;
    private int referencePeriod;

    public UserLevelInfo(UserLevel userLevel) {

        if (userLevel != null) {

            setLevelId(userLevel.getLevelId());
            setGroupCode(userLevel.getGroupCode());
            setDepth(userLevel.getDepth());
            setLevelName(userLevel.getLevelName());

            String fileSrc  = "";
            String fileName = userLevel.getFileName();
            if (!ObjectUtils.isEmpty(fileName)) {

                StringBuilder sb = new StringBuilder();
                sb.append(SalesonProperty.getSalesonUrlCdn());
                sb.append(SalesonProperty.getUploadBaseFolder());
                sb.append("/");
                sb.append("user_level");
                sb.append("/");
                sb.append(userLevel.getLevelId());
                sb.append("/");
                sb.append(fileName);

                fileSrc = sb.toString();
            }

            setFileSrc(fileSrc);
            setPriceStart(userLevel.getPriceStart());
            setPriceEnd(userLevel.getPriceEnd());
            setDiscountRate(userLevel.getDiscountRate());
            setPointRate(userLevel.getPointRate());
            setShippingCouponCount(userLevel.getShippingCouponCount());
            setRetentionPeriod(userLevel.getRetentionPeriod());
            setReferencePeriod(userLevel.getReferencePeriod());

        }

    }
}

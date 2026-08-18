package saleson.shop.item.domain;

import com.onlinepowers.framework.util.ValidationUtils;
import lombok.Getter;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

@Getter
@Setter
public class ItemReviewImage {
    private long itemReviewImageId;
    private int itemReviewId;
    private String reviewImage;
    private int ordering;
    private String createdDate;

    public StringBuilder getDefaultSrc() {
        StringBuilder sb = new StringBuilder();

        sb.append(SalesonProperty.getSalesonUrlCdn());
        sb.append(SalesonProperty.getUploadBaseFolder());
        if (reviewImage != null && reviewImage.contains("/")) {			// 답례품 후기 이관 데이터
//        	sb.append("/rc_upload_file");
        } else {
            sb.append("/item-review/");
            sb.append(this.itemReviewId);
        }
        return sb;
    }

    public String getImageSrc() {
        if (ValidationUtils.isEmpty(this.reviewImage)) {
            return ShopUtils.getNoImagePath();
        }

        StringBuilder sb = getDefaultSrc();
        sb.append("/");
        sb.append(this.reviewImage);

        return sb.toString();
    }
}

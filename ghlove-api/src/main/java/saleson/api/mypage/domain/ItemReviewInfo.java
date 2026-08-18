package saleson.api.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.api.order.OrderController;
import saleson.common.utils.CommonUtils;
import saleson.model.review.ItemReviewFilter;
import saleson.shop.item.domain.ItemBase;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.item.domain.ItemReviewImage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemReviewInfo {

    private static Logger log = LoggerFactory.getLogger(ItemReviewInfo.class);

    private int itemReviewId;

    private int itemId;
    private String itemUserCode;
    private String itemName;
    private String itemImageSrc;

    private String subject;
    private String content;
    private int score;
    List<Boolean> starFlags;

    private List<ItemReviewImage> itemReviewImages;
    private String thumbnailSrc;
    private boolean isPhotoReview;

    private String createdDate;
    private String maskUsername;

    private boolean displayOptionsFlag;
    private String options;
    private String adminComment;
    private int likeCount = 0;
    private String shLocgovName;
    private String displayflag;

    private List<Map<String, Object>> filters;
    
    private long rownum;
	private String answerDate;
	private String answerAdminName;
	
	private String sellerName;
	private String sellerCompanyName;  

    public ItemReviewInfo(ItemReview itemReview) {

        if (itemReview != null) {

            try {

                this.itemReviewId = itemReview.getItemReviewId();
                this.subject = itemReview.getSubject();
                this.content = itemReview.getContent();
                this.score = itemReview.getScore();
                this.starFlags = getStarFlags(itemReview.getScore());

                this.maskUsername = itemReview.getMaskUsername();
                this.createdDate = itemReview.getCreatedDate();
                this.shLocgovName = itemReview.getShLocgovName();
                this.displayflag = itemReview.getDisplayFlag();
                ItemBase itemBase = itemReview.getItem();

                this.thumbnailSrc = itemReview.getThumbnailSrc();
                this.itemReviewImages = itemReview.getItemReviewImages();

                if (this.itemReviewImages != null && !this.itemReviewImages.isEmpty()) {
                    for (ItemReviewImage itemReviewImage : this.itemReviewImages) {
                        if (itemReviewImage.getItemReviewImageId() > 0) {
                            this.isPhotoReview = true;
                            break;
                        }
                    }
                }

                if (itemBase != null) {
                    this.itemId = itemBase.getItemId();
                    this.itemUserCode = itemBase.getItemUserCode();
                    this.itemName = itemBase.getItemName();
                    this.itemImageSrc = itemBase.getImageSrc();
                }

                setAdminComment(CommonUtils.dataNvl(itemReview.getAdminComment()));
                setDisplayOptionsFlag("Y".equals(itemReview.getDisplayOptionsFlag()));
                setOptions(itemReview.getOptions());
                setLikeCount(itemReview.getLikeCount());

                setFilters(getFilters(itemReview.getItemReviewFilters()));
                
                setRownum(itemReview.getRownum());
                setAnswerDate(itemReview.getAnswerDate());
                setAnswerAdminName(itemReview.getAnswerAdminName());
                
                setSellerName(itemReview.getSellerName());
                setSellerCompanyName(itemReview.getSellerCompanyName());
            } catch (RuntimeException ignore) {	
            	log.error(getClass().getName() + " constructor error", ignore);
            }
        }
    }

    public List<Boolean> getStarFlags(int score) {

        List<Boolean> list = new ArrayList<>();

        int maxScore = 5;
        int subScore = maxScore - score;

        for (int i=0; i<score; i++) {
            list.add(true);
        }

        if (subScore > 0) {
            for (int i=0; i<subScore; i++) {
                list.add(false);
            }
        }

        return list;
    }

    private List<Map<String, Object>> getFilters(List<ItemReviewFilter> filters) {
        List<Map<String, Object>> list = new ArrayList<>();

        if (filters != null && !filters.isEmpty()) {

            filters.forEach(f->{

                long group = CommonUtils.longNvl(f.getFilterGroupId());
                long code = CommonUtils.longNvl(f.getFilterCodeId());

                if (group > 0 && code > 0) {
                    Map<String, Object> map = new LinkedHashMap<>();

                    map.put("group", group);
                    map.put("code", code);

                    list.add(map);
                }

            });

        }

        return list;
    }
}

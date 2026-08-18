package saleson.api.item.domain;

import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.item.domain.ItemBase;
import saleson.shop.item.domain.ItemRelation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationItem {

    public RelationItem(ItemRelation itemRelation) {

        if (itemRelation != null && itemRelation.getItem() != null) {
            ItemBase item  = itemRelation.getItem();

            setRelationId(itemRelation.getItemRelationId());
            setOrdering(itemRelation.getOrdering());

            setItemUserCode(item.getItemUserCode());
            setItemName(item.getItemName());

            setBrandName("");

            if (!ObjectUtils.isEmpty(item.getItemImage())) {
                item.setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "M"));
            }

            setItemImage(item.getItemImage());
            setImageSrc(item.getImageSrc());

            setDiscountRate(item.getDiscountRate());
            setSalePrice(item.getSalePrice());
            setPresentPrice(item.getPresentPrice());
        }
    }

    private int relationId;
    private int ordering;

    private String itemUserCode;
    private String itemName;

    private String brandName;

    private String itemImage;
    private String imageSrc;

    private int discountRate;
    private int salePrice;
    private int presentPrice;

}

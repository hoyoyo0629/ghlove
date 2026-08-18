package saleson.shop.google.analytics.domain.measuring;

import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.cart.domain.Cart;
import saleson.shop.item.domain.Item;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.ItemPrice;
import saleson.shop.order.domain.OrderItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Product {

    private String id; // Product ID. Either ID or name must be set.
    private String nm; // Product name. Either ID or name must be set.
    private String ca; // Product category.
    private String br; // Product brand.
    private String va; // Product variant.
    private String pr; // Product Price.
    private String qt; // Product quantity.
    private String ps; // Product position.

    public Product(Item item) {
        if (item != null) {

            if (item != null) {
                setId(item.getItemUserCode());
                setNm(item.getItemName());
                setBr(item.getBrand());
                setPr(String.valueOf(item.getPresentPrice()));
            }
        }
    }

    public Product(Cart cart) {

        if (cart != null) {

            Item item = cart.getItem();

            if (item != null) {
                setId(item.getItemUserCode());
                setNm(item.getItemName());
                //setCa("");
                setBr(item.getBrand());
                setVa(ShopUtils.viewItemOptions(cart.getSetItemFlag(), cart.getOptions()));
                setPr(String.valueOf(item.getPresentPrice()));
                setQt(String.valueOf(cart.getQuantity()));
            }
        }

    }

    public Product(BuyItem buyItem) {

        if (buyItem != null) {

            ItemPrice itemPrice = buyItem.getItemPrice();

            setId(buyItem.getItemUserCode());
            setNm(buyItem.getItemName());
            //setCa("");
            setBr(buyItem.getBrand());
            setVa(ShopUtils.viewItemOptions(buyItem.getSetItemFlag(), buyItem.getOptions()));
            if(itemPrice != null) {
	            setPr(String.valueOf(itemPrice.getItemSalePrice() + itemPrice.getOptionPrice()));
	            setQt(String.valueOf(itemPrice.getQuantity()));
            }
        }

    }

    public Product(OrderItem orderItem, String category) {

        if (orderItem != null) {

            setId(orderItem.getItemUserCode());

            setNm(orderItem.getItemName());
            setCa(category);
            setBr(orderItem.getBrand());

            if (!ObjectUtils.isEmpty(orderItem.getOptions())) {
                setVa(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
            }

            setPr(String.valueOf(orderItem.getSalePrice() + orderItem.getOptionPrice()));
            setQt(String.valueOf(orderItem.getQuantity()));
        }

    }

    public Product(String id, String qt) {
        setId(id);
        setQt(qt);
    }
}

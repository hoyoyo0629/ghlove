package saleson.shop.openmarket.domain;

import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.categories.domain.BreadcrumbCategory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncNaverItem {

    private int itemId;
    private String itemUserCode;
    private String itemName;
    private String itemSummary;
    private String itemImage;
    private int salePrice;
    private int stockQuantity;
    private String stockFlag;
    private int shipmentReturnId;
    private String returnZipcode;
    private String returnAddress;
    private String returnAddressDetail;
    private String returnName;
    private String returnTelephoneNumber;
    private String itemOptionFlag;
    private String itemOptionType;
    private String itemOptionTitle1;
    private String itemOptionTitle2;
    private String itemOptionTitle3;
    private List<SyncNaverItemOption> options;
    private List<SyncNaverCategory> categories;


    private List<Breadcrumb> breadcrumbs;
    public String getXml() {
        StringBuilder sb = new StringBuilder();
        setItem(sb);
        return sb.toString();
    }

    private void setItem(StringBuilder sb) {

        String url = SalesonProperty.getSalesonUrlShoppingmall()
                + ShopUtils.getMobilePrefixByPage()
                + "/products/view/" + getItemUserCode();

        if ("api".equals(SalesonProperty.getSalesonViewType())) {
            url = SalesonProperty.getSalesonUrlFrontend()
                    + "/items/details.html?code="
                    + getItemUserCode();
        }

        String image = getImageSrc(getItemImage(), getItemUserCode());
        image = StringUtils.hasText(image) ? image : "";
        int quantity = (!"Y".equals(getStockFlag()) || getStockQuantity() == -1)
                ? 9999 : getStockQuantity();

        sb.append("<item id=\""+getItemId()+"\">");
        sb.append(getCdataElement("name",getItemName()));
        sb.append(getCdataElement("url",url));
        sb.append(getCdataElement("description",getItemSummary()));
        sb.append(getCdataElement("image",image));
        sb.append(getCdataElement("thumb",image));
        sb.append(getElement("price",getSalePrice()));
        sb.append(getElement("quantity",quantity));
        setOption(sb);
        setCategory(sb);
        setReturnInfo(sb);
        sb.append("</item>");
    }
    private void setOption(StringBuilder sb) {

        List<SyncNaverItemOption> options = getOptions();

        if ("Y".equals(getItemOptionFlag())
                && options != null && !options.isEmpty()) {

            Set<String> option1Set = new HashSet<>();
            Set<String> option2Set = new HashSet<>();
            Set<String> option3Set = new HashSet<>();

            options.forEach(o-> {
                o.setName(option1Set, option2Set, option3Set);
            });

            sb.append("<options>");

            setOptionElement(sb, getItemOptionTitle1(), option1Set);
            setOptionElement(sb, getItemOptionTitle2(), option2Set);
            setOptionElement(sb, getItemOptionTitle3(), option3Set);

            sb.append("</options>");

        }
    }

    private void setOptionElement(StringBuilder sb, String title, Set<String> nameSet) {
        if (!nameSet.isEmpty()) {
            sb.append("<option name=\""+title+"\">");
            nameSet.forEach(s-> {
                sb.append(getCdataElement("select", s));
            });
            sb.append("</option>");
        }
    }

    private void setReturnInfo(StringBuilder sb) {

        if (getShipmentReturnId() > 0) {
            sb.append("<returnInfo>");
            sb.append(getCdataElement("zipcode", getReturnZipcode()));
            sb.append(getCdataElement("address1", getReturnAddress()));
            sb.append(getCdataElement("address2", getReturnAddressDetail()));
            sb.append(getCdataElement("sellername", getReturnName()));
            sb.append(getCdataElement("contact1", getReturnTelephoneNumber()));
            sb.append("</returnInfo>");
        }
    }

    private void setCategory(StringBuilder sb) {

        if (!ObjectUtils.isEmpty(getBreadcrumbs())) {

            SyncNaverItemCategory category = getSyncNaverItemCategory();
            if (category != null) {
                sb.append("<category>");

                if(StringUtils.hasText(category.getFirst()))
                    sb.append(getCdataElement("first", category.getFirst()));
                if(StringUtils.hasText(category.getSecond()))
                    sb.append(getCdataElement("second", category.getSecond()));
                if(StringUtils.hasText(category.getThird()))
                    sb.append(getCdataElement("third", category.getThird()));
                if(StringUtils.hasText(category.getFourth()))
                    sb.append(getCdataElement("fourth", category.getFourth()));

                sb.append("</category>");
            }

        }
    }
    private String getElement(String tag, Object value) {
        return new StringBuilder().append("<"+tag+">")
                .append(value)
                .append("</"+tag+">")
                .toString();
    }

    private String getCdataElement(String tag, Object value) {
        return new StringBuilder().append("<"+tag+">")
                .append("<![CDATA[")
                .append(value)
                .append("]]>")
                .append("</"+tag+">")
                .toString();
    }

    private SyncNaverItemCategory getSyncNaverItemCategory() {

        List<Breadcrumb> breadcrumbs = getBreadcrumbs();

        if (!ObjectUtils.isEmpty(breadcrumbs)) {
            SyncNaverItemCategory syncNaverItemCategory = new SyncNaverItemCategory();

            Breadcrumb breadcrumb = breadcrumbs.get(0);

            syncNaverItemCategory.setFirst(breadcrumb.getGroupName());

            List<BreadcrumbCategory> categories = breadcrumb.getBreadcrumbCategories();
            if (categories != null && !categories.isEmpty()) {
                int index = 0;
                for (BreadcrumbCategory category : categories) {
                    String name = category.getCategoryName();
                    switch (index) {
                        case 0: syncNaverItemCategory.setSecond(name); break;
                        case 1: syncNaverItemCategory.setThird(name);break;
                        case 2: syncNaverItemCategory.setFourth(name);break;
                    }
                    index++;
                }
            }

            return syncNaverItemCategory;
        }

        return null;
    }

    private String getImageSrc(String image, String itemUserCode) {

        if (StringUtils.hasText(image)) {

            if (image.contains("http://") || image.contains("https://") ) {
                return image;
            }

            return SalesonProperty.getSalesonUrlShoppingmall()
                    + ShopUtils.loadImage(itemUserCode, image, "L");
        }

        return "";

    }

    public int getFirstCategoryId() {

        if (!ObjectUtils.isEmpty(getCategories())) {
            return getCategories().get(0).getCategoryId();
        }

        return 0;
    }
}

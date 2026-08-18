package saleson.api.item.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import saleson.api.item.domain.ItemList;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.seller.main.domain.Seller;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemImage;
import saleson.shop.item.domain.ItemInfo;
import saleson.shop.seo.domain.Seo;

@Component
public class ItemDataSupport {

    public List<ItemList> resultItemListInfo(List<Item> list) {
        List<ItemList> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {

            for (Item item : list) {
                resultList.add(new ItemList(item));
            }
        }
        return resultList;
    }

    public void setItemDataNvl(Item item) {

        item.setManagerLoginId(CommonUtils.dataNvl(item.getManagerLoginId()));
        item.setUserName(CommonUtils.dataNvl(item.getUserName()));
        item.setActionType(CommonUtils.dataNvl(item.getActionType()));
        item.setProcessPage(CommonUtils.dataNvl(item.getProcessPage()));

        Seller seller = item.getSeller();
        if (seller != null) {
            seller.setSellerName(CommonUtils.dataNvl(seller.getSellerName()));
            seller.setLoginId(CommonUtils.dataNvl(seller.getLoginId()));
        }

        item.setErpExceptionType(CommonUtils.dataNvl(item.getErpExceptionType()));
        item.setSupplyType(CommonUtils.dataNvl(item.getSupplyType()));
        item.setItemType3(CommonUtils.dataNvl(item.getItemType3()));
        item.setStockScheduleAutoFlag(CommonUtils.dataNvl(item.getStockScheduleAutoFlag()));
        item.setShipmentAddress(CommonUtils.dataNvl(item.getShipmentAddress()));
        item.setShipmentReturnAddress(CommonUtils.dataNvl(item.getShipmentReturnAddress()));

        Seo seo = item.getSeo();

        seo.setSeoUrl(CommonUtils.dataNvl(seo.getSeoUrl()));
        seo.setHeaderContents2(CommonUtils.dataNvl(seo.getHeaderContents2()));
        seo.setHeaderContents3(CommonUtils.dataNvl(seo.getHeaderContents3()));
        seo.setThemawordTitle(CommonUtils.dataNvl(seo.getThemawordTitle()));
        seo.setThemawordTopTitle(CommonUtils.dataNvl(seo.getThemawordTopTitle()));
        seo.setThemawordDescription(CommonUtils.dataNvl(seo.getThemawordDescription()));
        seo.setTitle(CommonUtils.dataNvl(seo.getTitle()));
        seo.setCreatedDate(CommonUtils.dataNvl(seo.getCreatedDate()));

        if (item.getBreadcrumbs().size() > 0) {
            item.getBreadcrumbs().get(0).getBreadcrumbCategories().get(0).getGroupUrl();
        }

        List<ItemInfo> itemInfos = new ArrayList<>();
        if (item.getItemInfos().size() > 0) {
            for (int i = 0; i < item.getItemInfos().size(); i++) {
                ItemInfo ii = new ItemInfo();
                ii.setItemInfoId(item.getItemInfos().get(i).getItemInfoId());
                ii.setItemId(item.getItemInfos().get(i).getItemId());
                ii.setItemNoticeCode(CommonUtils.dataNvl(item.getItemInfos().get(i).getItemNoticeCode()));
                ii.setInfoCode(CommonUtils.dataNvl(item.getItemInfos().get(i).getInfoCode()));
                ii.setTitle(CommonUtils.dataNvl(item.getItemInfos().get(i).getTitle()));
                ii.setDescription(CommonUtils.dataNvl(item.getItemInfos().get(i).getDescription()));
                ii.setCreatedDate(CommonUtils.dataNvl(item.getItemInfos().get(i).getCreatedDate()));
                ii.setItemUserCode(CommonUtils.dataNvl(item.getItemInfos().get(i).getItemUserCode()));
                ii.setItemName(CommonUtils.dataNvl(item.getItemInfos().get(i).getItemName()));
                ii.setDetailContentTop(CommonUtils.dataNvl(item.getItemInfos().get(i).getDetailContentTop()));
                ii.setDetailContent(CommonUtils.dataNvl(item.getItemInfos().get(i).getDetailContent()));
                itemInfos.add(ii);
            }
            item.setItemInfos(itemInfos);
        }

        if (item.getOptionGroups().size() > 0) {
            item.getItemOptionGroups().get(0).getOptionType();
            item.getItemOptionGroups().get(0).getOptionTitle();
            item.getItemOptionGroups().get(0).getOptionDisplayType();
            item.getItemOptionGroups().get(0).getOptionHideFlag();

            if (item.getItemOptionGroups().get(0).getItemOptions().size() > 0) {
                item.getItemOptionGroups().get(0).getItemOptions().get(0).getOptionStockFlag();
                item.getItemOptionGroups().get(0).getItemOptions().get(0).getOptionSoldOutFlag();
                item.getItemOptionGroups().get(0).getItemOptions().get(0).getCreatedDate();
            }
        }

        item.getCategoryIds(); // int[]
        item.getRelatedItemIds(); // int[]

        item.setOptionId(CommonUtils.dataAryNvl(item.getOptionId()));
        item.setOptionType(CommonUtils.dataAryNvl(item.getOptionType()));
        item.setOptionName1(CommonUtils.dataAryNvl(item.getOptionName1()));
        item.setOptionName2(CommonUtils.dataAryNvl(item.getOptionName2()));
        item.setOptionName3(CommonUtils.dataAryNvl(item.getOptionName3()));
        item.setOptionPrice(CommonUtils.dataAryNvl(item.getOptionPrice()));
        item.setOptionCostPrice(CommonUtils.dataAryNvl(item.getOptionCostPrice()));
        item.setOptionStockFlag(CommonUtils.dataAryNvl(item.getOptionStockFlag()));

        item.setOptionStockQuantity(CommonUtils.dataAryNvl(item.getOptionStockQuantity()));

        item.setOptionSoldOutFlag(CommonUtils.dataAryNvl(item.getOptionSoldOutFlag()));
        item.setOptionDisplayFlag(CommonUtils.dataAryNvl(item.getOptionDisplayFlag()));
        item.setOptionStockCode(CommonUtils.dataAryNvl(item.getOptionStockCode()));
        item.setAdditionItemId(CommonUtils.dataAryNvl(item.getAdditionItemId()));
        item.setAdditionItemName(CommonUtils.dataAryNvl(item.getAdditionItemName()));
        item.setAdditionSalePrice(CommonUtils.dataAryNvl(item.getAdditionSalePrice()));
        item.setAdditionCostPrice(CommonUtils.dataAryNvl(item.getAdditionCostPrice()));
        item.setAdditionStockFlag(CommonUtils.dataAryNvl(item.getAdditionStockFlag()));
        item.setAdditionStockQuantity(CommonUtils.dataAryNvl(item.getAdditionStockQuantity()));
        item.setAdditionStockCode(CommonUtils.dataAryNvl(item.getAdditionStockCode()));
        item.setAdditionSoldOut(CommonUtils.dataAryNvl(item.getAdditionSoldOut()));
        item.setAdditionTaxType(CommonUtils.dataAryNvl(item.getAdditionTaxType()));
        item.setAdditionDisplayFlag(CommonUtils.dataAryNvl(item.getAdditionDisplayFlag()));
        item.setAdditionWeight(CommonUtils.dataAryNvl(item.getAdditionWeight()));
        item.setPointType(CommonUtils.dataAryNvl(item.getPointType()));

        item.setPointStartDate(CommonUtils.dataAryNvl(item.getPointStartDate()));

        item.setPointStartTime(CommonUtils.dataAryNvl(item.getPointStartTime()));
        item.setPointEndDate(CommonUtils.dataAryNvl(item.getPointEndDate()));
        item.setPointEndTime(CommonUtils.dataAryNvl(item.getPointEndTime()));
        item.setPointRepeatDay(CommonUtils.dataAryNvl(item.getPointRepeatDay()));
        item.setItemInfoTitles(CommonUtils.dataAryNvl(item.getItemInfoTitles()));
        item.setItemInfoDescriptions(CommonUtils.dataAryNvl(item.getItemInfoDescriptions()));
        item.setItemInfoMobileTitles(CommonUtils.dataAryNvl(item.getItemInfoMobileTitles()));
        item.setItemInfoMobileDescriptions(CommonUtils.dataAryNvl(item.getItemInfoMobileDescriptions()));

        item.setSimpleContent(CommonUtils.dataNvl(item.getSimpleContent()));
        item.setTempId(CommonUtils.dataNvl(item.getTempId()));
        item.setTempControl(CommonUtils.dataNvl(item.getTempControl()));

        item.setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "L"));

        if (item.getItemImages().size() > 0) {
            for (ItemImage itemImage : item.getItemImages()) {
                if (!ObjectUtils.isEmpty(itemImage.getImageName())) {
                    itemImage.setImageName(ShopUtils.loadImage(item.getItemUserCode(), itemImage.getImageName(), "L"));
                }
            }
        }

        // 사은품
        item.setFreeGiftFlag(CommonUtils.dataNvl(item.getFreeGiftFlag()));
        item.setFreeGiftName(CommonUtils.dataNvl(item.getFreeGiftName()));
        if (item.getFreeGiftItemList() == null) {
            item.setFreeGiftItemList(new ArrayList<>());
        }

        // 세트상품
        if (item.getItemSets() == null) {
            item.setItemSets(new ArrayList<>());
        }
        item.setLocgovCode(CommonUtils.dataNvl(item.getLocgovCode()));
        item.setLocgovNm(CommonUtils.dataNvl(item.getLocgovNm()));
        
        item.setAdultItemYn(CommonUtils.dataNvl(item.getAdultItemYn()));
        item.setMobileItemYn(CommonUtils.dataNvl(item.getMobileItemYn()));
    }

}

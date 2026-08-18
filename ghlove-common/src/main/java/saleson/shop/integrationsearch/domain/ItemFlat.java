package saleson.shop.integrationsearch.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import saleson.seller.main.domain.Seller;
import saleson.shop.item.domain.Item;

@Data
public class ItemFlat {
	@JsonProperty("item_id")
	private int itemId;

	@JsonProperty("seller_id")
	private int sellerId;

	@JsonProperty("item_code")
	private String itemCode;

	@JsonProperty("item_user_code")
	private String itemUserCode;

	@JsonProperty("item_name")
	private String itemName;

	@JsonProperty("item_summary")
	private String itemSummary;

	@JsonProperty("item_data_type")
	private String itemDataType;

	@JsonProperty("item_type")
	private String itemType;

	@JsonProperty("item_new_flag")
	private String itemNewFlag;

	@JsonProperty("item_label")
	private String itemLabel;

	@JsonProperty("item_type1")
	private String itemType1;

	@JsonProperty("item_type2")
	private String itemType2;

	@JsonProperty("item_type3")
	private String itemType3;

	@JsonProperty("item_type4")
	private String itemType4;

	@JsonProperty("item_type5")
	private String itemType5;

	@JsonProperty("item_type6")
	private String itemType6;

	@JsonProperty("item_type7")
	private String itemType7;

	@JsonProperty("item_type8")
	private String itemType8;

	@JsonProperty("item_type9")
	private String itemType9;

	@JsonProperty("item_type10")
	private String itemType10;

	@JsonProperty("sold_out")
	private String soldOut;

	@JsonProperty("stock_flag")
	private String stockFlag;

	@JsonProperty("stock_quantity")
	private int stockQuantity;

	@JsonProperty("tax_type")
	private String taxType;

	@JsonProperty("item_price")
	private String itemPrice;

	@JsonProperty("cost_price")
	private int costPrice;

	@JsonProperty("supply_price")
	private int supplyPrice;

	@JsonProperty("sale_price")
	private int salePrice;

	@JsonProperty("adult_item_yn")
	private String adultItemYn;

	@JsonProperty("mobile_item_yn")
	private String mobileItemYn;

	@JsonProperty("item_image")
	private String itemImage;

	@JsonProperty("item_option_flag")
	private String itemOptionFlag;

	@JsonProperty("item_option_type")
	private String itemOptionType;

	@JsonProperty("item_option_title1")
	private String itemOptionTitle1;

	@JsonProperty("item_option_title2")
	private String itemOptionTitle2;

	@JsonProperty("item_option_title3")
	private String itemOptionTitle3;

	@JsonProperty("data_status_code")
	private String dataStatusCode;

	@JsonProperty("display_flag")
	private String displayFlag;

	@JsonProperty("stock_code")
	private String stockCode;

	@JsonProperty("stock_schedule_auto_flag")
	private String stockScheduleAutoFlag;

	@JsonProperty("stock_schedule_type")
	private String stockScheduleType;

	@JsonProperty("stock_schedule_date")
	private String stockScheduleDate;

	@JsonProperty("stock_schedule_text")
	private String stockScheduleText;

	@JsonProperty("order_min_quantity")
	private int orderMinQuantity;

	@JsonProperty("order_max_quantity")
	private int orderMaxQuantity;

	@JsonProperty("sale_quantity")
	private int saleQuantity;

	@JsonProperty("created_date")
	private String createdDate;

	@JsonProperty("seller_name")
	private String sellerName;

	@JsonProperty("seller_login_id")
	private String loginId;

	@JsonProperty("seller_company_name")
	private String companyName;

	@JsonProperty("seller_commission_rate")
	private float commissionRate;

	@JsonProperty("locgov_code")
	private String locgovCode;

	@JsonProperty("locgov_nm")
	private String locgovNm;

	@JsonProperty("code")
	private String code;

	@JsonProperty("category_url")
	private String categoryUrl;

	public Item toItem() {
		Seller seller = new Seller();
		seller.setSellerName(sellerName);
		seller.setLoginId(loginId);
		seller.setCompanyName(companyName);
		seller.setCommissionRate(commissionRate);
		seller.setLocgovCode(locgovCode);

		Item item = new Item().builder()
				.itemId(itemId)
				.sellerId(sellerId)
				.itemCode(itemCode)
				.itemUserCode(itemUserCode)
				.itemName(itemName)
				.itemSummary(itemSummary)
				.itemDataType(itemDataType)
				.itemType(itemType)
				.itemNewFlag(itemNewFlag)
				.itemLabel(itemLabel)
				.itemType(itemType)
				.itemType1(itemType1)
				.itemType2(itemType2)
				.itemType3(itemType3)
				.itemType4(itemType4)
				.itemType5(itemType5)
				.soldOut(soldOut)
				.stockFlag(stockFlag)
				.stockQuantity(stockQuantity)
				.taxType(taxType)
				.itemPrice(itemPrice)
				.costPrice(costPrice)
				.supplyPrice(supplyPrice)
				.salePrice(salePrice)
				.adultItemYn(adultItemYn)
				.mobileItemYn(mobileItemYn)
				.itemImage(itemImage)
				.itemOptionFlag(itemOptionFlag)
				.itemOptionType(itemOptionType)
				.itemOptionTitle1(itemOptionTitle1)
				.itemOptionTitle2(itemOptionTitle2)
				.itemOptionTitle3(itemOptionTitle3)
				.dataStatusCode(dataStatusCode)
				.displayFlag(displayFlag)
				.stockCode(stockCode)
				.stockScheduleAutoFlag(stockScheduleAutoFlag)
				.stockScheduleType(stockScheduleType)
				.stockScheduleDate(stockScheduleDate)
				.stockScheduleText(stockScheduleText)
				.orderMinQuantity(orderMinQuantity)
				.orderMaxQuantity(orderMaxQuantity)
				.saleQuantity(saleQuantity)
				.createdDate(createdDate)
				.locgovNm(locgovNm)
				.locgovCode(locgovCode)
				.seller(seller)
				.build();

		return item;
	}
}

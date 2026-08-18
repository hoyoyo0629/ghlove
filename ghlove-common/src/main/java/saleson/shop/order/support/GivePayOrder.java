package saleson.shop.order.support;

import java.util.ArrayList;
import java.util.List;

import saleson.shop.order.domain.BuyItem;

public class GivePayOrder {

	private String locgovCode;

	private List<BuyItem> buyItemList = new ArrayList<>();

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public List<BuyItem> getBuyItemList() {
		List<BuyItem> list = new ArrayList<>();
		for (BuyItem buyItem : buyItemList) {
			try {
				list.add((BuyItem) buyItem.clone());
			} catch(CloneNotSupportedException e) {
				continue;
			}
		}
		return list;
	}

	public void addBuyItemList(BuyItem buyItem) {
		try {
			buyItemList.add((BuyItem) buyItem.clone());
		} catch(CloneNotSupportedException e) {
			return;
		}
	}

	public int getSumPrice() {
		int result = 0;
		for (BuyItem buyItem : buyItemList) {
			if(buyItem != null && buyItem.getItemPrice() != null) {
				result += buyItem.getItemPrice().getItemSaleAmount();
			}
		}
		return result;
	}

	public int getTaxFreeSumPrice() {
		int result = 0;
		for (BuyItem buyItem : buyItemList) {
			if ("Y".equalsIgnoreCase(buyItem.getTaxFreeYn())) {
				if(buyItem != null) {
					result += buyItem.getItemPrice().getItemSaleAmount();
				}
			}
		}
		return result;
	}


}

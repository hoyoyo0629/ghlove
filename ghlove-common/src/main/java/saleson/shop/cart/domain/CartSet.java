package saleson.shop.cart.domain;

public class CartSet {

    private int itemId;
    private int quantity;

    // 세트상품용
    private String[] arrayItemSets;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String[] getArrayItemSets() {
    	if (arrayItemSets == null) {
    		return null;
    	} else {
    		int length = arrayItemSets.length;
    		if (length == 0) {
    			return new String[0];
    		}
    		String[] array = new String[length];
    		for (int i = 0 ; i < length ; i++) {
    			array[i] = new String(arrayItemSets[i]);
			}
            return array;
    	}
    }

    public void setArrayItemSets(String[] arrayItemSets) {
    	if (arrayItemSets == null) {
    		this.arrayItemSets = null;
    	} else {
    		int length = arrayItemSets.length;
    		if (length == 0) {
    			this.arrayItemSets = new String[0];
    			return;
    		}
    		this.arrayItemSets = new String[length];
    		for (int i = 0 ; i < length ; i++) {
    			this.arrayItemSets[i] = new String(arrayItemSets[i]);
			}
    	}
    }
}

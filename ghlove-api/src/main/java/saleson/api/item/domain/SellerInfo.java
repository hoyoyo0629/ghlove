package saleson.api.item.domain;

import saleson.seller.main.domain.Seller;

public class SellerInfo {

    private long sellerId;
    private String sellerName;
    private String userName;
    private String telephoneNumber;
    private String phoneNumber;
    private String email;
    private String companyName;
    private String address;
    private String addressDetail;
    private String representativeName;
    private String businessNumber;
    private String businessLocation;
    private String businessType;
    private String businessItems;
    
    

    public SellerInfo() {
		
	}

    public SellerInfo(Seller seller) {
		setSellerId(seller.getSellerId());
		setSellerName(seller.getSellerName());
		setUserName(seller.getUserName());
		setTelephoneNumber(seller.getTelephoneNumber());
		setPhoneNumber(seller.getPhoneNumber());
		setEmail(seller.getEmail());
		setCompanyName(seller.getCompanyName());
		setAddress(seller.getAddress());
		setAddressDetail(seller.getAddressDetail());
		setRepresentativeName(seller.getRepresentativeName());
		setBusinessNumber(seller.getBusinessNumber());
		setBusinessLocation(seller.getBusinessLocation());
		setBusinessType(seller.getBusinessType());
		setBusinessItems(seller.getBusinessItems());
	}

	public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getBusinessLocation() {
		return businessLocation;
	}

	public void setBusinessLocation(String businessLocation) {
		this.businessLocation = businessLocation;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessItems() {
		return businessItems;
	}

	public void setBusinessItems(String businessItems) {
		this.businessItems = businessItems;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

}

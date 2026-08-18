package saleson.shop.item.domain;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import saleson.shop.remittance.support.RemittanceFileSupport;

public class ItemImageExplain {
//	private final Logger log = LoggerFactory.getLogger(RemittanceFileSupport.class);
	
	private Long imagesIndexes;
	private String imagesValues;

	public Long getImagesIndexes() {
		return imagesIndexes;
	}

	public void setImagesIndexes(Long imagesIndexes) {
		this.imagesIndexes = imagesIndexes;
	}

	public String getImagesValues() {
		return imagesValues;
	}

	public void setImagesValues(String imagesValues) {
		this.imagesValues = imagesValues;
	}

	public ItemImageExplain() {};
}

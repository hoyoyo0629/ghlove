package saleson.shop.openmarket;

import java.util.List;

public interface OpenmarketService {

    String getSyncNaverItemXml(List<String> itemUserCodes);
}

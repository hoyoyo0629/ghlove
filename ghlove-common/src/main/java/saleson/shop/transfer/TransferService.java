package saleson.shop.transfer;

import java.util.HashMap;
import java.util.List;

public interface TransferService {

	public HashMap<String, Object> transferItemImage(String count, List<String> item_id_list);
	public HashMap<String, Object> transferItemImageTempTest(String count, List<String> item_id_list);	// TODO : 삭제예정. test 용도


}

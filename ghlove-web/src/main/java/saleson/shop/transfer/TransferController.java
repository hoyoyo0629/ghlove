package saleson.shop.transfer;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.common.sms.domain.ReceiverInfoSeller;
import saleson.shop.remittance.RemittanceService;

//@RestController("ApiTransferController")
//@RequestMapping("/opmanager/transfer")
public class TransferController {

//	private Logger log = LoggerFactory.getLogger(TransferController.class);

	@Autowired
    private TransferService transferService;
	
//	@Autowired
//	private RemittanceService remittanceService;

    /**
     * 아이템 이미지 마이그레이션
     * ex) http://localhost:8080/opmanager/transfer/item/image?count=10&item_id_list=39200, 39201
     *
     * @param count 실행개수
     * @param item_id_list 실행할 아이템 ID List
     * @return
     * @throws Exception
     */
//    @GetMapping("/item/image")
//    public JsonView transferItemImage(@RequestParam(name="count", required=false) String count, @RequestParam(name="item_id_list", required=false) List<String> item_id_list) throws Exception {
//		// 로컬에서만 사용하도록 수정
//		// 서버 오른쪽 클릭 -> Open Config -> Arguments 탭 선택 -> VM arguments 에 -Dspring.profiles.active=local 입력 후 apply 버튼 클릭, 서버 실행하면 사용 가능
//		String profile = System.getProperty("spring.profiles.active");
//		if (!"local".equals(profile)) {
//			throw new PageNotFoundException();
//		}
//    	
//    	HashMap<String, Object> result = transferService.transferItemImage(count, item_id_list);
////    	HashMap<String, Object> result = transferService.transferItemImageTempTest(count, item_id_list);	// TODO : 삭제예정. test 용도
//
//		return JsonViewUtils.success(result);
//    }
    
    
    // 국민비서 해쉬 테스트
//    @GetMapping("/test1")
//    public JsonView test() throws Exception {
//		// 로컬에서만 사용하도록 수정
//		// 서버 오른쪽 클릭 -> Open Config -> Arguments 탭 선택 -> VM arguments 에 -Dspring.profiles.active=local 입력 후 apply 버튼 클릭, 서버 실행하면 사용 가능
//		String profile = System.getProperty("spring.profiles.active");
//		if (!"local".equals(profile)) {
//			throw new PageNotFoundException();
//		}
//		
//		ReceiverInfoSeller seller = new ReceiverInfoSeller();
//		seller.setSellerName("국민비서");
//		seller.setSellerMobileNo("구삐");
//		String result1 = seller.getPrvcIdntfcInfo();
//		seller.setSellerName("행정");
//		seller.setSellerMobileNo("안전부");
//		String result2 = seller.getPrvcIdntfcInfo();
//		
//		return JsonViewUtils.success(result1 + ";" + result2);
//    }
    
    
    // 정산 예정 문자 테스트
//    @GetMapping("/test2")
//    public JsonView test() throws Exception {
//		// 로컬에서만 사용하도록 수정
//		// 서버 오른쪽 클릭 -> Open Config -> Arguments 탭 선택 -> VM arguments 에 -Dspring.profiles.active=local 입력 후 apply 버튼 클릭, 서버 실행하면 사용 가능
//		String profile = System.getProperty("spring.profiles.active");
//		if (!"local".equals(profile)) {
//			throw new PageNotFoundException();
//		}
//		
//		remittanceService.sendRemittanceExpectedMsg();
//		
//		return JsonViewUtils.success("success");
//    }

}

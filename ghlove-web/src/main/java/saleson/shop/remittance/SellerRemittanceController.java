package saleson.shop.remittance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

@Controller
@RequestMapping("/seller/remittance")
@RequestProperty(title="정산", layout="default", template="seller")
public class SellerRemittanceController extends RemittanceManagerController {

}

package saleson.shop.tempprocess;

import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import saleson.common.enumeration.IdType;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.shop.log.ExceldownloadLogService;
import saleson.shop.order.support.OrderParam;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.support.RemittanceExcelView2;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.user.LocgovService;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.*;

@Controller
@RequestMapping("/seller/temp-process")
@RequestProperty(template="seller", layout="default")
public class TempProcessManagerSellerController extends TempProcessManagerController {
	
	
}

package saleson.api.banner;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.SearchParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.databoard.DataboardController;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.banner.MainBannerService;
import saleson.shop.banner.domain.MainBannerManager;
import saleson.shop.banner.support.MainBannerManagerSearchParam;
import saleson.shop.cart.CartService;
import saleson.shop.cart.domain.Cart;
import saleson.shop.cart.domain.CartSet;
import saleson.shop.cart.support.CartParam;
import saleson.shop.config.ConfigService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.Receiver;
import saleson.shop.order.domain.Shipping;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.order.support.OrderException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController("ApiBannerController")
@RequestMapping("/api/banner")
public class BannerController {
	private static final Logger log = LoggerFactory.getLogger(BannerController.class);

	@Autowired
	private MainBannerService mainBannerService;

    /**
     * 메인 배너 목록 api
     *
     * @param searchParam
     * @return
     */
    @GetMapping
    public ResponseEntity list(HttpServletRequest request, MainBannerManagerSearchParam searchParam){

    	searchParam.setDisplayFlag("Y");

        ResponseEntity result = null;

        try {
        	List<MainBannerManager> list = mainBannerService.getMainBannerList(searchParam);
        	result = ApiResponseEntity.data().list(list).ok();
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ list Exception {}",e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("DataboardController Detail",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

        return result;
    }

    @GetMapping("server")
	@RequestProperty(layout = "base")
	public ResponseEntity index(HttpServletRequest request) {

    	ResponseEntity result = null;
    	Map<String, Object> serverMap =  new HashMap<String, Object>();
    	serverMap.put("serverIp", getServerIp());
    	serverMap.put("remote", request.getRemoteAddr());
    	serverMap.put("local", request.getLocalAddr());
    	serverMap.put("x_forwarded_for", request.getHeader("X_FORWARDED_FOR"));
    	//serverMap.put("request", request);

		result = ApiResponseEntity.data().map(serverMap).ok();
		return result;
	}

    private String getServerIp() {

		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		}
		catch ( UnknownHostException e ) {
			log.error("■■■ERROR■■■ Exception {}",e.getStackTrace()[0]);
		}

		if( local == null ) {
			return "";
		}
		else {
			String ip = local.getHostAddress();
			return ip;
		}

	}

}

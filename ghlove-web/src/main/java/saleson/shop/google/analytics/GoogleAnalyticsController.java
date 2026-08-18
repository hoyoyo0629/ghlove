package saleson.shop.google.analytics;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.cart.CartService;
import saleson.shop.cart.domain.Cart;
import saleson.shop.google.analytics.domain.measuring.Product;
import saleson.shop.google.analytics.support.GoogleAnalyticsParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.Order;
import saleson.shop.order.support.OrderParam;
import saleson.shop.user.domain.UserDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/google-analytics")
public class GoogleAnalyticsController {

    private static final Logger log = LoggerFactory.getLogger(GoogleAnalyticsController.class);

    @Autowired
    private GoogleAnalyticsService googleAnalyticsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    @PostMapping("/purchase")
    public JsonView purchase(@RequestBody GoogleAnalyticsParam param,
                                   HttpServletRequest request) {
        try {

            Order order = getOrder(param.getOrderCode(), param.getOrderSequence());

            if (order != null) {
                googleAnalyticsService.purchase(request, param.getCid(), order, param.getPage());
            }

        } catch (RuntimeException ignore) {
//            log.error("Google Analytics Purchase error [{}]", ignore.getMessage(), ignore);
            log.error("Google Analytics Purchase error [{}]", getClass().getName() + " :: purchase RuntimeException ============");
        }

        return JsonViewUtils.success();
    }

    @PostMapping("/add-to-cart")
    public JsonView addToCart(@RequestBody GoogleAnalyticsParam param,
                                    HttpServletRequest request) {
        try {

            List<String> cartArrayRequiredItems = param.getCartArrayRequiredItems();
            Cart cart = new Cart();
            cart.setArrayRequiredItems(cartArrayRequiredItems.toArray(new String[cartArrayRequiredItems.size()]));
            List<Cart> cartList = cartService.makeCartListByCart(cart);

            List<Product> products = new ArrayList<>();

            if (cartList != null && !cartList.isEmpty()) {
                cartList.forEach(c->{
                    products.add(new Product(c));
                });
            }

            googleAnalyticsService.addToCart(request, param.getCid(), products);
        } catch (RuntimeException ignore) {
//            log.error("Google Analytics Add To Cart error [{}]", ignore.getMessage(), ignore);
            log.error("Google Analytics Add To Cart error [{}]", getClass().getName() + " :: addToCart RuntimeException ============");
        }

        return JsonViewUtils.success();
    }

    @PostMapping("/remove-from-cart")
    public JsonView removeFromCart(@RequestBody GoogleAnalyticsParam param,
                                         HttpServletRequest request) {
        try {

            googleAnalyticsService.removeFromCart(request, param.getCid(), param.getProducts());
        } catch (RuntimeException ignore) {
//            log.error("Google Analytics Remove From Cart error [{}]", ignore.getMessage(), ignore);
            log.error("Google Analytics Remove From Cart error [{}]", getClass().getName() + " :: removeFromCart RuntimeException ============");
        }

        return JsonViewUtils.success();
    }

    @PostMapping("/change-from-cart-quantity")
    public JsonView changeFromCartQuantity(@RequestBody GoogleAnalyticsParam param,
                                                 HttpServletRequest request) {
        try {

            if (param.isAddQuantityFlag()) {
                googleAnalyticsService.addToCart(request, param.getCid(), param.getProducts());
            } else {
                googleAnalyticsService.removeFromCart(request, param.getCid(), param.getProducts());
            }

        } catch (RuntimeException ignore) {
//            log.error("Google Analytics Change From Cart Quantity error [{}]", ignore.getMessage(), ignore);
            log.error("Google Analytics Change From Cart Quantity error [{}]", getClass().getName() + " :: changeFromCartQuantity RuntimeException ============");
        }

        return JsonViewUtils.success();
    }

    @PostMapping("/detail")
    public JsonView detail(@RequestBody GoogleAnalyticsParam param,
                                 HttpServletRequest request) {
        try {

            Item item = itemService.getItemByItemUserCode(param.getItemUserCode());

            if (item != null) {
                List<Product> products = new ArrayList<>();
                Product product = new Product(item);

                try {
                    product.setCa(item.getBreadcrumbs().get(0).getBreadcrumbCategories().get(0).getCategoryName());
                } catch (RuntimeException ignore) {
//                    log.error("Google Analytics Detail Category Name error [{}]", ignore.getMessage(), ignore);
                    log.error("Google Analytics Detail Category Name error [{}]", getClass().getName() + " :: detail RuntimeException1 ============");
                }
                products.add(product);

                googleAnalyticsService.detail(request, param.getCid(), products);
            }


        } catch (RuntimeException ignore) {
//            log.error("Google Analytics Detail error [{}]", ignore.getMessage(), ignore);
            log.error("Google Analytics Detail error [{}]", getClass().getName() + " :: detail RuntimeException2 ============");
        }

        return JsonViewUtils.success();
    }

    @PostMapping("/checkout")
    public JsonView checkout(@RequestBody GoogleAnalyticsParam param,
                             HttpSession session,
                             HttpServletRequest request) {
        try {

            OrderParam orderParam = new OrderParam();

            long userId = 0;
            if (UserUtils.isUserLogin()) {
                userId = UserUtils.getUserId();
            }

            orderParam.setSessionId(session.getId());
            orderParam.setUserId(userId);

            Buy buy = orderService.getBuyForStep1(orderParam, UserUtils.getUser());

            googleAnalyticsService.checkout(request, param.getCid(), buy, param.getPage());

        } catch (RuntimeException ignore) {
//            log.error("Google Analytics Detail error [{}]", ignore.getMessage(), ignore);
            log.error("Google Analytics Detail error [{}]", getClass().getName() + " :: checkout RuntimeException ============");
        }

        return JsonViewUtils.success();
    }


    private Order getOrder(String orderCode, int orderSequence) {
        OrderParam orderParam = new OrderParam();
        // 회원, 비회원 로그인 구분
        if (UserUtils.isUserLogin()) {
            orderParam.setUserId(UserUtils.getUserId());
        } else if (UserUtils.isGuestLogin()) {
            User user = UserUtils.getGuestLogin();

            if (user != null) {
            	 orderParam.setGuestUserName(user.getUserName());
            }

            if (user.getUserDetail() != null) {
            	UserDetail userDetail = (UserDetail) user.getUserDetail();
            	orderParam.setGuestPhoneNumber(userDetail.getPhoneNumber());
            }
        }

        orderParam.setOrderCode(orderCode);
        orderParam.setOrderSequence(orderSequence);

        return orderService.getOrderByParam(orderParam);
    }
}

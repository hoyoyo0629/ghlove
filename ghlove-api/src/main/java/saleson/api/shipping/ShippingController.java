package saleson.api.shipping;

import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.utils.UserUtils;
import saleson.shop.userdelivery.UserDeliveryService;
import saleson.shop.userdelivery.domain.UserDelivery;
import saleson.shop.userdelivery.support.UserDeliveryParam;

import javax.validation.Valid;
import java.util.List;


@RestController("ApiShippingController")
@RequestMapping("/api/shipping")
public class ShippingController {

    private Logger log = LoggerFactory.getLogger(ShippingController.class);

    public static final String STATUS = "status";

    @Autowired
    private UserDeliveryService userDeliveryService;

    /**
     * 배송지 목록 조회
     * @param param
     * @return
     */
    @GetMapping("")
    public ResponseEntity list(UserDeliveryParam param){
        ResponseEntity result = null;
        List<UserDelivery> list = null;
        Pagination pagination = null;
        int deliveryCount = 0;
        try {
            deliveryCount = userDeliveryService.getDeliveryCount(UserUtils.getUserId());
            pagination = Pagination.getInstance(deliveryCount, param.getItemsPerPage());

            param.setUserId(UserUtils.getUserId());
            param.setPagination(pagination);
            list = userDeliveryService.getUserDeliveryListByParam(param);

            // 핸드폰번호 앞자리 리스트 (010, 011, 012...)
            List<CodeInfo> phoneCodes = CodeUtils.getCodeInfoList("PHONE");
            List<CodeInfo> telCodes = CodeUtils.getCodeInfoList("TEL");

            result = ApiResponseEntity.data().list(list)
            								.put("phoneCodes", phoneCodes)
            								.put("telCodes", telCodes)
            								.pagination(pagination).ok();
        } catch(RuntimeException e){
            //log.error("[get: /api/shipping] ERROR : {}", e.getMessage(), e);
        	log.error("ERROR-50: 배송지 목록 조회 return 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 배송지 추가&수정
     * @param userDelivery
     * @param bindingResult
     * @return
     */
    @PostMapping("")
    public ResponseEntity save(@RequestBody @Valid UserDelivery userDelivery, BindingResult bindingResult){
        ResponseEntity result = null;
        try {
            userDelivery.setUserId(UserUtils.getUserId());
            userDelivery.processHyphen();

            if (bindingResult.hasErrors()) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_SHIPPING);
            }

            if (userDelivery.getUserDeliveryId() > 0) {
                userDeliveryService.updateUserDelivery(userDelivery);
            } else {
                userDeliveryService.insertUserDelivery(userDelivery);
            }

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch(RuntimeException e){
            //log.error("[post: /api/shipping] ERROR : {}", e.getMessage(), e);
            log.error("ERROR-51: 배송지 추가&수정 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 기본 배송지 설정
     * @param
     * @return
     */
    @PostMapping("/base-shipping")
    public ResponseEntity baseShipping(@RequestBody UserDeliveryParam userDeliveryParam) {
        userDeliveryParam.setMode("mod");
        return listAction(userDeliveryParam);
    }

    /**
     * 배송지 삭제
     * @param
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody UserDeliveryParam userDeliveryParam) {
        userDeliveryParam.setMode("del");
        return listAction(userDeliveryParam);
    }

    private ResponseEntity listAction(UserDeliveryParam userDeliveryParam) {
        ResponseEntity result = null;
        try {
            userDeliveryParam.setUserId(UserUtils.getUserId());
            userDeliveryService.listAction(userDeliveryParam);
            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch(RuntimeException e){
            String mode = "mod".equals(userDeliveryParam.getMode()) ? "base-shipping" : "delete";
            //log.error("[/api/shipping/{}] ERROR : {}", mode, e.getMessage(), e);
            log.error("ERROR-52: 배송지 삭제 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }
}

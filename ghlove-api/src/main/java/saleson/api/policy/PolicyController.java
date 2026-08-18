package saleson.api.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.policy.PolicyService;
import saleson.shop.policy.domain.Policy;


@RestController("ApiPolicyController")
@RequestMapping("/api/policy")
public class PolicyController {

    private static final Logger log = LoggerFactory.getLogger(PolicyController.class);

    @Autowired
    private PolicyService policyService;


    /**
     * 이용약관
     * @return
     */
    @GetMapping("/clause")
    public ResponseEntity etc_clause() {

        ResponseEntity result = null;

        try {
            Policy policy = policyService.getCurrentPolicyByType(Policy.POLICY_TYPE_AGREEMENT); // 약관
            result = ApiResponseEntity.data().put("clause", policy).ok();
        } catch (RuntimeException e) {
            log.error("policy error",e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 개인정보보호방침
     * @return
     */
    @GetMapping("/protect")
    public ResponseEntity etc_protect() {

        ResponseEntity result = null;

        try {
            Policy policy = policyService.getCurrentPolicyByType(Policy.POLICY_TYPE_PROTECT_POLICY); // 개인정보취급방침
            result = ApiResponseEntity.data().put("protect", policy).ok();
        } catch (RuntimeException e) {
            log.error("policy error",e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 마케팅 이용약관
     * @return
     */
    @GetMapping("/marketing")
    public ResponseEntity etc_() {

        ResponseEntity result = null;

        try {
            Policy policy = policyService.getCurrentPolicyByType(Policy.POLICY_TYPE_MARKETING_AGREEMENT); // 마케팅이용약관
            result = ApiResponseEntity.data().put("marketing", policy).ok();
        } catch (RuntimeException e) {
            log.error("policy error",e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 저작권정책
     * @return
     */
    @GetMapping("/copyright")
    public ResponseEntity etc_copyright() {

        ResponseEntity result = null;

        try {
            Policy policy = policyService.getCurrentPolicyByType(Policy.POLICY_TYPE_COPYRIGHT); // 약관
            result = ApiResponseEntity.data().put("copyright", policy).ok();
        } catch (RuntimeException e) {
            log.error("policy error",e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

}

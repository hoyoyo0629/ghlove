package saleson.shop.policy;

import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import saleson.common.utils.UserUtils;
import saleson.shop.policy.domain.Policy;
import saleson.shop.policy.support.PolicyParam;
import saleson.shop.user.domain.Customer;
import saleson.shop.user.support.AgreeDto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service("policyService")
public class PolicyServiceImpl extends EgovAbstractServiceImpl implements PolicyService {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private PolicyMapper policyMapper;

    @Override
    public void insertPolicy(Policy policy) {

        policy.setPolicyId(sequenceService.getId("OP_POLICY"));

        policyMapper.insertPolicy(policy);
    }

    @Override
    public List<Policy> getPolicyListByParam(PolicyParam policyParam) {
        return policyMapper.getPolicyListByParam(policyParam);
    }

    @Override
    public Policy getPolicyByParam(int policyId, String policyType) {
        PolicyParam policyParam = new PolicyParam();

        policyParam.setPolicyId(policyId);
        policyParam.setPolicyType(policyType);

        return policyMapper.getPolicyByParam(policyParam);
    }

    @Override
    public Policy getCurrentPolicyByType(String policyType) {
        return policyMapper.getCurrentPolicyByType(policyType);
    }

    @Override
    public Policy getPolicyByPolicyId(int policyId) {
        return policyMapper.getPolicyByPolicyId(policyId);
    }

    @Override
    public void updatePolicy(Policy policy) {
        policy.setUpdatedLoginId(UserUtils.getLoginId());

        policyMapper.updatePolicy(policy);
    }

    @Override
    public void createPolicy(Policy policy) {
        policy.setPolicyId(sequenceService.getId("OP_POLICY"));
        policy.setCreatedUserId(UserUtils.getManagerId());
        policy.setUpdatedLoginId(UserUtils.getLoginId());

        policyMapper.createPolicy(policy);
    }

    @Override
    public int getCountPolicyListByParam(PolicyParam policyParam) {
        return policyMapper.getCountPolicyListByParam(policyParam);
    }

    @Override
    public List<Policy> getPeriodListByParam(PolicyParam policyParam) {
        return policyMapper.getPeriodListByParam(policyParam);
    }
    @Override
    public String getPolicyType(String type) {

        String agreementType = "";

        if (StringUtils.hasText(type)){
            switch (type) {
                case "agreement":
                case "terms":
                    return Policy.POLICY_TYPE_AGREEMENT;
                case "protect-policy":
                case "privacy":
                    return Policy.POLICY_TYPE_PROTECT_POLICY;
                case "trader-raw":
                    return Policy.POLICY_TYPE_TRADER_RAW;
                case "marketing":
                    return Policy.POLICY_TYPE_MARKETING_AGREEMENT;
            }
        }
        return agreementType;
    }

    @Override
    public void deletePolicyData(ListParam listparam) {

        if (listparam.getId() != null) {

            for (String policyId : listparam.getId()) {
                policyMapper.deletePolicy(Integer.parseInt(policyId));
            }
        }
    }

    @Override
    public void deletePolicy(int policyId) {
        policyMapper.deletePolicy(policyId);
    }
}

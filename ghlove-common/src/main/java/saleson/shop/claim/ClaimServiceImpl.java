package saleson.shop.claim;

import com.onlinepowers.framework.sequence.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.claim.domain.ClaimCriteriaEncryptor;
import saleson.shop.claim.domain.ClaimMemo;
import saleson.shop.claim.domain.ClaimMemoEncryptor;
import saleson.shop.claim.support.ClaimMemoParam;

import java.util.List;

@RequiredArgsConstructor
@Service("claimMemoService")
public class ClaimServiceImpl extends EgovAbstractServiceImpl implements ClaimService {

	private final SequenceService sequenceService;
	private final ClaimMapper claimMapper;
	private final ClaimCriteriaEncryptor claimCriteriaEncryptor;
	private final ClaimMemoEncryptor claimMemoEncryptor;

	@Override
	public int getClaimMemoCount(ClaimMemoParam param) {
		param.encrypt(claimCriteriaEncryptor);
		int count = claimMapper.getClaimMemoCount(param);
		param.decrypt(claimCriteriaEncryptor);

		return count;
	}

	@Override
	public List<ClaimMemo> getClaimMemoList(ClaimMemoParam param) {
		param.encrypt(claimCriteriaEncryptor);
		List<ClaimMemo> list = claimMapper.getClaimMemoList(param);
		param.decrypt(claimCriteriaEncryptor);


		list.forEach(c -> c.decrypt(claimMemoEncryptor, ShopUtils.needMasking()));

		return list;
	}

	@Override
	public ClaimMemo getClaimMemoById(int claimMemoId) {
		ClaimMemo claimMemo = claimMapper.getClaimMemoById(claimMemoId);
		claimMemo.decrypt(claimMemoEncryptor, ShopUtils.needMasking());

		return claimMemo;
	}

	@Override
	public void insertClaimMemo(ClaimMemo claimMemo) {
		
		claimMemo.setClaimMemoId(sequenceService.getId("OP_CLAIM_MEMO"));
		claimMemo.setManagerUserId(UserUtils.getUser().getUserId());
		claimMemo.setManagerLoginId(UserUtils.getUser().getLoginId());
		claimMemo.setDataStatusCode("1");

		claimMemo.encrypt(claimMemoEncryptor);
		claimMapper.insertClaimMemo(claimMemo);
	}

	@Override
	public void updateClaimMemo(ClaimMemo claimMemo) {
		claimMemo.setManagerUserId(UserUtils.getUser().getUserId());
		claimMemo.setManagerLoginId(UserUtils.getUser().getLoginId());
		claimMapper.updateClaimMemo(claimMemo);
	}

	@Override
	public void deleteClaimMemoById(int claimMemoId) {
		claimMapper.deleteClaimMemoById(claimMemoId);
	}
}

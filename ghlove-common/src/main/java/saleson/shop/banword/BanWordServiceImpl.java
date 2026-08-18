package saleson.shop.banword;

import java.util.List;

import com.onlinepowers.framework.util.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.ObjectUtils;
import saleson.shop.banword.domain.BanWord;
import saleson.shop.categories.CategoriesServiceImpl;

import com.onlinepowers.framework.exception.BusinessException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.web.domain.SearchParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;

@Service("banWordService")
public class BanWordServiceImpl extends EgovAbstractServiceImpl implements BanWordService {
	@Autowired
	private BanWordMapper banWordMapper;
	
	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private ConfigService configService;
	
	private final Logger log = LoggerFactory.getLogger(BanWordServiceImpl.class);

	@Override
	public int getBanWordCount(SearchParam searchParam) {
		return banWordMapper.getBanWordCount(searchParam);
	}

	@Override
	public List<BanWord> getBanWordList(SearchParam searchParam) {
		return banWordMapper.getBanWordList(searchParam);
	}
	
	@Override
	public List<BanWord> getBanWordListAll() {
		return banWordMapper.getBanWordListAll();
	}

	@Override
	public String checkBanWord(String text) {
		List<BanWord> banWords = banWordMapper.getBanWordListAll();
		String bannedWord = null;
		
		for (BanWord banWord : banWords) {
			if (text.indexOf(banWord.getBanWord()) > -1) {
				bannedWord = banWord.getBanWord();
				break;
			}
		}
		
		return bannedWord;
	}

	@Override
	public void insertBanWord(BanWord banWord) {
		banWord.setBanWordId(sequenceService.getId("OP_BAN_WORD"));
		banWord.setUserId(SecurityUtils.getCurrentUserId());
		banWord.setUserName(SecurityUtils.getCurrentUser().getUserName());
		
		// 중복조회
		SearchParam searchParam = new SearchParam();
		searchParam.setWhere("BAN_WORD");
		searchParam.setQuery(banWord.getBanWord());
		
		int banWordCount = banWordMapper.getBanWordCount(searchParam);
		
		if (banWordCount > 0) {
			throw new BusinessException("'" + banWord.getBanWord() + "'는 이미 등록된 금칙어 입니다.");
		}
		
		banWordMapper.insertBanWord(banWord);
	}

	@Override
	public BanWord getBanWordByBanWordId(int banWordId) {
		return banWordMapper.getBanWordByBanWordId(banWordId);
	}

	@Override
	public void deleteBanWordByBanWordId(int banWordId) {
		banWordMapper.deleteBanWordByBanWordId(banWordId);
	}


	@Override
	public String getCheckedBanWord(String value) {

		if (ObjectUtils.isEmpty(value)) {
			return "";
		}

		Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);
		String storeBanword = config.getBanWord();

		if (!ObjectUtils.isEmpty(storeBanword)) {
			try {

				String[] array = StringUtils.delimitedListToStringArray(storeBanword,",");

				if (!ObjectUtils.isEmpty(array)) {
					for (String banword : array) {
						if (value.contains(banword)) {
							return banword;
						}
					}
				}

			} catch (OpRuntimeException e) {
				log.error(getClass().getName() + " getCheckedBanWord error", e);
			}
		}

		return "";
	}

	@Override
	public boolean includeBanWord(String value) {

		String banword = getCheckedBanWord(value);
		if (!ObjectUtils.isEmpty(banword)) {
			return true;
		}
		return false;
	}
}

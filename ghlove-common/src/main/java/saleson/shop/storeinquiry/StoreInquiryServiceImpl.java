package saleson.shop.storeinquiry;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.common.utils.ShopUtils;
import saleson.shop.order.domain.OrderCount;
import saleson.shop.storeinquiry.domain.StoreInquiry;
import saleson.shop.storeinquiry.domain.StoreInquiryCriteriaEncryptor;
import saleson.shop.storeinquiry.domain.StoreInquiryEncryptor;
import saleson.shop.storeinquiry.support.StoreInquiryParam;


@Service("storeInquiryService")
public class StoreInquiryServiceImpl extends EgovAbstractServiceImpl implements StoreInquiryService {
	private static final Logger log = LoggerFactory.getLogger(StoreInquiryServiceImpl.class);

	@Autowired
	private StoreInquiryMapper storeInquiryMapper;

	@Autowired
	private StoreInquiryEncryptor storeInquiryEncryptor;

	@Autowired
	private StoreInquiryCriteriaEncryptor storeInquiryCriteriaEncryptor;


	/**
	 * 입점문의 insert
	 * @param storeInquiry
	 */
	public void insertStoreInquiry(StoreInquiry storeInquiry){
		storeInquiry.encrypt(storeInquiryEncryptor);
		storeInquiryMapper.insertStoreInquiry(storeInquiry);
	};
	
	/**
	 * 입점문의 리스트
	 * @param storeInquiryParam
	 * @return
	 */
	public List<StoreInquiry> getStoreInquiryList(StoreInquiryParam storeInquiryParam){;
		storeInquiryParam.encrypt(storeInquiryCriteriaEncryptor);

		List<StoreInquiry> list = storeInquiryMapper.getStoreInquiryList(storeInquiryParam);
		list.forEach(s -> decryptData(s));

		storeInquiryParam.decrypt(storeInquiryCriteriaEncryptor);

		return list;
	}
	
	/**
	 * 입점문의 상태 업데이트
	 * @param storeInquiry
	 */
	public void updateStoreInquiryStatus(StoreInquiry storeInquiry){
		storeInquiry.encrypt(storeInquiryEncryptor);
		storeInquiryMapper.updateStoreInquiryStatus(storeInquiry);
	}

	@Override
	public int getStoreInquiryCount(StoreInquiryParam storeInquiryParam) {
		storeInquiryParam.encrypt(storeInquiryCriteriaEncryptor);
		int count = storeInquiryMapper.getStoreInquiryCount(storeInquiryParam);
		storeInquiryParam.decrypt(storeInquiryCriteriaEncryptor);
		return count;
	}

	@Override
	public StoreInquiry getStoreInquiry(int storeInquiryId) {
		StoreInquiry storeInquiry = storeInquiryMapper.getStoreInquiry(storeInquiryId);
		decryptData(storeInquiry);
		return storeInquiry;
	};
	
	@Override
	public StoreInquiry getStoreInquiryByFileName(int storeInquiryId) {
		StoreInquiry storeInquiry = storeInquiryMapper.getStoreInquiryByFileName(storeInquiryId);
		decryptData(storeInquiry);
		return storeInquiry;
	}
	
	@Override
	public List<OrderCount> getOpmanagerStoreCountAll() {
		return storeInquiryMapper.getOpmanagerStoreCountAll();
	}

	private void decryptData(StoreInquiry storeInquiry) {
		// 복호화
		if (storeInquiry != null) {
			storeInquiry.decrypt(storeInquiryEncryptor, ShopUtils.needMasking());
		}
	}

}

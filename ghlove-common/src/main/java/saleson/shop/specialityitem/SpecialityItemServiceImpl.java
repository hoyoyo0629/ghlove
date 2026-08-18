package saleson.shop.specialityitem;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.dreamsecurity.magice2e.util.Log;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.StringUtils;

import saleson.common.utils.UserUtils;
import saleson.shop.specialityitem.domain.SpecialityFrontDomain;
import saleson.shop.specialityitem.domain.SpecialityItem;
import saleson.shop.specialityitem.domain.SpecialityItemManage;
import saleson.shop.specialityitem.support.SpecialityItemParam;

@Service("specialityItemService")
public class SpecialityItemServiceImpl implements SpecialityItemService {

	@Autowired
	private SpecialityItemMapper specialityItemMapper;

	@Autowired
	private SequenceService sequenceService;

	@Override
	public List<SpecialityItemManage> getSpecialityItemManageList(SpecialityItemParam params) {
		return specialityItemMapper.getSpecialityItemManageList(params);
	}

	@Override
	public SpecialityItemManage insertSpecialityItemManage(SpecialityItemParam specialityItemParam) {
		long userId = UserUtils.getUserId();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());

		SpecialityItemManage insertData = new SpecialityItemManage();
		insertData.setLocgovCode(specialityItemParam.getLocgovCode());
		insertData.setSpecialityItemManageId(sequenceService.getLong("G_SPCL_ITEM_MNG"));
		insertData.setFrstRegisterId(userId);
		insertData.setFrstRegistPnttm(now);
		insertData.setLastUpdusrId(userId);
		insertData.setLastUpdtPnttm(now);

		specialityItemMapper.insertSpecialityItemManage(insertData);

		addSpecialityItemManageKeyword(insertData);

		return insertData;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public int updateSpecialityItemManage(SpecialityItemManage specialityItemManage) throws RuntimeException {
		long userId = UserUtils.getUserId();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());

		if (specialityItemManage.getSpecialityItemManageId() < 1) {
			SpecialityItemParam specialityItemParam = new SpecialityItemParam();
			specialityItemParam.setLocgovCode(specialityItemManage.getLocgovCode());
			SpecialityItemManage insertData = insertSpecialityItemManage(specialityItemParam);
			specialityItemManage.setSpecialityItemManageId(insertData.getSpecialityItemManageId());
		}
		
		specialityItemManage.setLastUpdusrId(userId);
		specialityItemManage.setLastUpdtPnttm(now);

		specialityItemMapper.updateSpecialityItemManage(specialityItemManage);
		addSpecialityItemManageKeyword(specialityItemManage);

		specialityItemMapper.deleteSpecialityItem(specialityItemManage);

		int order = 0;
		for (String itemId : specialityItemManage.getProdString().split("~")) {
			SpecialityItem item = new SpecialityItem();
			try {
				item.setSpecialityItemManageId(specialityItemManage.getSpecialityItemManageId());
//				item.setItemId(Long.valueOf(itemId));
				item.setItemId(Integer.valueOf(itemId));
				item.setDisplayOrder(++order);

				item.setFrstRegisterId(userId);
				item.setFrstRegistPnttm(now);
				item.setLastUpdusrId(userId);
				item.setLastUpdtPnttm(now);

				specialityItemMapper.insertSpecialityItem(item);
			} catch (NumberFormatException e) {
				Log.error(getClass().getName() + " :: updateSpecialityItemManage NumberFormatException ===============");
			}
		}

		return 0;
	}

	@Override
	public long getSpecialityItemManageCount() {
		try {
			return specialityItemMapper.getSpecialityItemManageCount();
		} catch (BindingException e) {
			Log.error(getClass().getName() + " :: getSpecialityItemManageCount BindingException ===============");
			return 0l;
		}
	}

	@Override
	public List<SpecialityItem> getSpecialityItemListByParam(SpecialityItemParam specialityItemParam) {
		return specialityItemMapper.getSpecialityItemListByParam(specialityItemParam);
	}

	private void addSpecialityItemManageKeyword(SpecialityItemManage specialityItemManage) {
		// 키워드 삭제
		specialityItemMapper.deleteSpecialityItemKeyword(specialityItemManage);
		// 키워드 추가
		String keywordsStr = specialityItemManage.getKeywords();
		if (specialityItemManage.getSpecialityItemManageId() > 0 && keywordsStr != null) {
			String[] keywords = keywordsStr.split(",");
			
			// 통합검색 요청으로 , 구분자로 한개 로우만 인서트
//			int i = 0;
//			for (String keyword : keywords) {
//				i++;
//				if (!keyword.trim().isEmpty()) {
//					SpecialityItemParam param = new SpecialityItemParam();
//					param.setSpecialityItemManageId(specialityItemManage.getSpecialityItemManageId());
//					param.setKeywords(keyword);
//					param.setRegSeq(i);
//					param.setFrstRegisterId(specialityItemManage.getLastUpdusrId());
//					param.setFrstRegistPnttm(specialityItemManage.getLastUpdtPnttm());
//					param.setLastUpdusrId(specialityItemManage.getLastUpdusrId());
//					param.setLastUpdtPnttm(specialityItemManage.getLastUpdtPnttm());
//
//					specialityItemMapper.insertSpecialityItemKeyword(param);
//				}
//			}
			// 통합검색 요청으로 , 구분자로 한개 로우만 인서트

			// 중복 제거 추가
			List<String> keywordList = new ArrayList<>();
			for (String keyword : keywords) {
				int dupCnt = 0;
				if (!StringUtils.isEmpty(keyword)) {
					keyword = keyword.trim();
					for (String keyword2 : keywordList) {
						if (keyword.equals(keyword2)) {
							dupCnt++;
						}
					}
					if (dupCnt == 0) {
						keywordList.add(keyword);
					}
				}
			}
			
			StringBuffer buf = new StringBuffer();
			for (String keyword : keywordList) {
				if (buf.length() > 0) {
					buf.append(",");
				}
				buf.append(keyword);
			}
			// 중복 제거 추가
			
			SpecialityItemParam param = new SpecialityItemParam();
			param.setSpecialityItemManageId(specialityItemManage.getSpecialityItemManageId());
			param.setKeywords(buf.toString());
			param.setRegSeq(1);
			param.setFrstRegisterId(specialityItemManage.getLastUpdusrId());
			param.setFrstRegistPnttm(specialityItemManage.getLastUpdtPnttm());
			param.setLastUpdusrId(specialityItemManage.getLastUpdusrId());
			param.setLastUpdtPnttm(specialityItemManage.getLastUpdtPnttm());
			
			specialityItemMapper.insertSpecialityItemKeyword(param);
		}
	}

	@Override
	public int getSpecialityFrontListCount(SpecialityItemParam params) {
		return specialityItemMapper.getSpecialityFrontListCount(params);
	}

	@Override
	public List<SpecialityFrontDomain> getSpecialityFrontList(SpecialityItemParam params) {
		return specialityItemMapper.getSpecialityFrontList(params);
	}


}

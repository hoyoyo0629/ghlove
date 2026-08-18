package saleson.shop.give.giveoperation;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.give.giveoperation.domain.CtbnyOpratn;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnFile;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnSearchParam;
import saleson.shop.give.giveoperation.domain.GiveOperation;
import saleson.shop.give.giveoperation.domain.GiveOperationSearchParam;

@Mapper("giveOperationMapper")
public interface GiveOperationMapper {
	
	GiveOperation getGiveOperationTotalCnt(GiveOperationSearchParam searchParam);
	
	int getGiveOperationListTotalCnt(GiveOperationSearchParam searchParam);
	
	List<GiveOperation> getGiveOperationList(GiveOperationSearchParam searchParam);
	
	int getCtbnyOpratnListTotalCnt(GiveOperationSearchParam searchParam);
	
	List<CtbnyOpratn> getCtbnyOpratnList(GiveOperationSearchParam searchParam);
	
	int getCtbnyOpratnListAmt(GiveOperationSearchParam searchParam);
	
	int insertCtbnyOpratn(CtbnyOpratn ctbnyOpratn);
	
	int insertCtbnyOpratnFile(CtbnyOpratnFile ctbnyOpratnFile);
	
	CtbnyOpratn getCtbnyOpratnDetail(Long registSn);
	
	CtbnyOpratnFile getCtbnyOpratnFile(Long registFileId);
	
	int deleteCtbnyOpratnFile(CtbnyOpratnFile ctbnyOpratnFile);
	
	int updateCtbnyOpratn(CtbnyOpratn ctbnyOpratn);
	
	List<CtbnyOpratnFile> getCtbnyOpratnFileList(Long registSn);
	
	int deleteCtbnyOpratn(Long registSn);
	
	
}

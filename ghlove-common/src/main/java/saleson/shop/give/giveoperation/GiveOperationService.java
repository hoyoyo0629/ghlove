package saleson.shop.give.giveoperation;

import java.util.List;

import com.onlinepowers.framework.exception.UserException;

import saleson.shop.give.giveoperation.domain.LocManagerCheck;
import saleson.shop.give.giveoperation.domain.CtbnyOpratn;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnFile;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnSearchParam;
import saleson.shop.give.giveoperation.domain.GiveOperation;
import saleson.shop.give.giveoperation.domain.GiveOperationSearchParam;

public interface GiveOperationService {
	
	public GiveOperation getGiveOperationTotalCnt(GiveOperationSearchParam searchParam);
	
	public int getGiveOperationListTotalCnt(GiveOperationSearchParam searchParam);
	
	public List<GiveOperation> getGiveOperationList(GiveOperationSearchParam searchParam);
	
	public int getCtbnyOpratnListTotalCnt(GiveOperationSearchParam searchParam);
	
	public List<CtbnyOpratn> getCtbnyOpratnList(GiveOperationSearchParam searchParam);
	
	public int getCtbnyOpratnListAmt(GiveOperationSearchParam searchParam);
	
	public String insertCtbnyOpratn(CtbnyOpratn ctbnyOpratn) throws UserException; 
	
	CtbnyOpratn getCtbnyOpratnDetail(Long registSn);
	
	public String deleteCtbnyOpratnFile(CtbnyOpratnFile ctbnyOpratnFile) throws Exception;
	
	public String updateCtbnyOpratn(CtbnyOpratn ctbnyOpratn) throws Exception;
	
	public boolean checkBalanceAmt(String locgovCode, Long amt, Long registSn);
	
	public LocManagerCheck findAdminRoleAndLocgov(String locgovCode, String returnUrl);
	
	public CtbnyOpratnFile getCtbnyOpratnFile(Long registFileId);
	
	public String deleteCtbnyOpratn(Long registSn) throws Exception;
	
	public List<CtbnyOpratnFile> getCtbnyOpratnFileList(Long registSn);
	
	
}

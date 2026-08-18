package saleson.shop.give.giveoperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.ViewUtils;

import saleson.api.common.enumerated.UserAdminRole;
import saleson.common.utils.UserUtils;
import saleson.shop.code.domain.Code;
import saleson.shop.give.giveoperation.domain.LocManagerCheck;
import saleson.shop.give.giveoperation.domain.CtbnyOpratn;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnFile;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnSearchParam;
import saleson.shop.give.giveoperation.domain.GiveOperation;
import saleson.shop.give.giveoperation.domain.GiveOperationSearchParam;
import saleson.shop.user.LocgovService;

@Service("giveOperationService")
public class GiveOperationServiceImpl implements GiveOperationService {

	private final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "hwp", "doc", "docx", "pdf", "zip", "ppt", "pptx"};

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private GiveOperationMapper giveOperationMapper;

	@Override
	public GiveOperation getGiveOperationTotalCnt(GiveOperationSearchParam searchParam) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getGiveOperationTotalCnt(searchParam);
	}

	@Override
	public int getGiveOperationListTotalCnt(GiveOperationSearchParam searchParam) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getGiveOperationListTotalCnt(searchParam);
	}

	@Override
	public List<GiveOperation> getGiveOperationList(GiveOperationSearchParam searchParam) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getGiveOperationList(searchParam);
	}

	@Override
	public int getCtbnyOpratnListTotalCnt(GiveOperationSearchParam searchParam) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getCtbnyOpratnListTotalCnt(searchParam);
	}

	@Override
	public List<CtbnyOpratn> getCtbnyOpratnList(GiveOperationSearchParam searchParam) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getCtbnyOpratnList(searchParam);
	}

	@Override
	public int getCtbnyOpratnListAmt(GiveOperationSearchParam searchParam) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getCtbnyOpratnListAmt(searchParam);
	}

	@Override
	@Transactional
	public String insertCtbnyOpratn(CtbnyOpratn ctbnyOpratn) {

		if (!this.checkBalanceAmt(ctbnyOpratn.getLocgovCode(), Long.parseLong(ctbnyOpratn.getExpndtrAmt()), Long.parseLong("0"))) return "잔액이 부족합니다.";


		int result = giveOperationMapper.insertCtbnyOpratn(ctbnyOpratn);

		// 파일이 있는 경우
		if (ctbnyOpratn.getOperationFiles().length > 0) {

			for (MultipartFile m : ctbnyOpratn.getOperationFiles()) {

				if (m.isEmpty()) continue;

				String newFileName = locgovService.saveFile(m, ctbnyOpratn.getUploadPath(), AVAILABLE_EXTENSION, 1, false);

				CtbnyOpratnFile cof = CtbnyOpratnFile.builder()
													 .registSn(ctbnyOpratn.getRegistSn())
													 .fileNm(newFileName)
													 .orginlFileNm(m.getOriginalFilename())
													 .fileTy(FileUtils.getExtension(m.getOriginalFilename()))
													 .frstRegisterId(ctbnyOpratn.getFrstRegisterId())
													 .lastUpdusrId(ctbnyOpratn.getFrstRegisterId())
													 .build();

				giveOperationMapper.insertCtbnyOpratnFile(cof);

			}

		}



		return "SUCC";
	}

	@Override
	public CtbnyOpratn getCtbnyOpratnDetail(Long registSn) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getCtbnyOpratnDetail(registSn);
	}

	@Override
	public String deleteCtbnyOpratnFile(CtbnyOpratnFile ctbnyOpratnFile) throws Exception {

		CtbnyOpratnFile cof = giveOperationMapper.getCtbnyOpratnFile(ctbnyOpratnFile.getRegistFileId());

		if (cof == null) throw new UserException("파일이 DB에 저장 되어있지 않습니다.");

		CtbnyOpratn co = new CtbnyOpratn();

		Path p = Paths.get(co.getUploadPath() + "/" + cof.getFileNm());

		if (Files.deleteIfExists(p)) {
			giveOperationMapper.deleteCtbnyOpratnFile(ctbnyOpratnFile);
		} else {
			throw new IOException("파일이 존재하지 않습니다.");
		}

		return "SUCC";

	}

	@Override
	public String updateCtbnyOpratn(CtbnyOpratn ctbnyOpratn) throws Exception {

		if (!this.checkBalanceAmt(ctbnyOpratn.getLocgovCode(), Long.parseLong(ctbnyOpratn.getExpndtrAmt()), ctbnyOpratn.getRegistSn())) return "잔액이 부족합니다.";

		giveOperationMapper.updateCtbnyOpratn(ctbnyOpratn);

		// 파일이 있는 경우
		if (ctbnyOpratn.getOperationFiles().length > 0) {

			for (MultipartFile m : ctbnyOpratn.getOperationFiles()) {

				if (m.isEmpty()) continue;

				String newFileName = locgovService.saveFile(m, ctbnyOpratn.getUploadPath(), AVAILABLE_EXTENSION, 1, false);

				CtbnyOpratnFile cof = CtbnyOpratnFile.builder()
													 .registSn(ctbnyOpratn.getRegistSn())
													 .fileNm(newFileName)
													 .orginlFileNm(m.getOriginalFilename())
													 .fileTy(FileUtils.getExtension(m.getOriginalFilename()))
													 .frstRegisterId(ctbnyOpratn.getFrstRegisterId())
													 .lastUpdusrId(ctbnyOpratn.getFrstRegisterId())
													 .build();

				giveOperationMapper.insertCtbnyOpratnFile(cof);

			}

		}

		return "SUCC";
	}

	@Override
	public boolean checkBalanceAmt(String locgovCode, Long amt, Long registSn) {
		GiveOperationSearchParam gosp = new GiveOperationSearchParam();
		gosp.setShLocgovCode(locgovCode);
		gosp.setRegistSn(registSn);
		GiveOperation go = giveOperationMapper.getGiveOperationTotalCnt(gosp);

		if (Long.parseLong(go.getBalanceAmt()) - amt < 0) {
			return false;
		}

		return true;
	}

	@Override
	public LocManagerCheck findAdminRoleAndLocgov(String locgovCode, String returnUrl) {
		UserAdminRole role = UserAdminRole.findByUserRole();

		LocManagerCheck ac = LocManagerCheck.builder().adminRole(role).build();
		Code locgovCodeDetails = null;
		if (UserAdminRole.LOC.equals(role)) {
			locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());

			if (locgovCodeDetails != null) {
				ac.setLocgovCode(locgovCodeDetails.getId());
				ac.setLocgovNm(locgovCodeDetails.getLabel());

				if (locgovCode != null && !"".equals(locgovCode)) {
					if (!locgovCode.equals(locgovCodeDetails.getId())) {
						String url = returnUrl != null && !"".equals(returnUrl) ? returnUrl.replaceAll("\\{locgovCode\\}", locgovCodeDetails.getId()) : null;

						ac.setMessage("등록된 지자체만 이용할 수 있습니다.");
						ac.setReturnUrl(url);
						ac.setPass(false);
					}
				}



			} else {
				ac.setPass(false);
			}
		}


		return ac;
	}

	@Override
	public CtbnyOpratnFile getCtbnyOpratnFile(Long registFileId) {
		// TODO Auto-generated method stub
		return giveOperationMapper.getCtbnyOpratnFile(registFileId);
	}

	@Override
	public String deleteCtbnyOpratn(Long registSn) throws Exception {

		List<CtbnyOpratnFile> cofList = giveOperationMapper.getCtbnyOpratnFileList(registSn);
		CtbnyOpratn co = new CtbnyOpratn();

		for (CtbnyOpratnFile cof : cofList) {
			Path p = Paths.get(co.getUploadPath() + "/" + cof.getFileNm());
				if (!Files.deleteIfExists(p)) {
					throw new IOException("파일이 존재하지 않습니다.");
				}
		}

		CtbnyOpratnFile cof = CtbnyOpratnFile.builder().registSn(registSn).build();
		giveOperationMapper.deleteCtbnyOpratnFile(cof);

		giveOperationMapper.deleteCtbnyOpratn(registSn);

		return "SUCC";
	}

	@Override
	public List<CtbnyOpratnFile> getCtbnyOpratnFileList(Long registSn) {
		return giveOperationMapper.getCtbnyOpratnFileList(registSn);
	}


}

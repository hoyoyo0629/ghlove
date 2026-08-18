package saleson.shop.user;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.domain.Code;
import saleson.shop.donation.DonationVerification;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.domain.HonorCntr;
import saleson.shop.user.domain.ContributionSetup;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.LocgovDeptHist;
import saleson.shop.user.domain.LocgovItemImage;
import saleson.shop.user.support.ContributionSetupSearchParam;
import saleson.shop.user.support.LocgovDeptSearchParam;
import saleson.shop.user.support.LocgovSearchParam;

@RequiredArgsConstructor
@Service("locgovService")
public class LocgovServiceImpl extends EgovAbstractServiceImpl implements LocgovService {
	private static final Logger log = LoggerFactory.getLogger(LocgovServiceImpl.class);

	private final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png"};

	private final LocgovMapper locgovMapper;

	@Autowired
	private final LocgovImageMapper locgovImageMapper;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private DonationVerification donationVerification;

	@Autowired
	private PlatformTransactionManager transactionManager;

	/**
	 * 지자체관리 목록 총 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getLocgovCountByParam(LocgovSearchParam searchParam) {
		return locgovMapper.getLocgovCountByParam(searchParam);
	}

	/**
	 * 지자체관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<Locgov> getLocgovListByParam(LocgovSearchParam searchParam) {
		return locgovMapper.getLocgovListByParam(searchParam);
	}

	/**
	 * 지자체관리 목록 > 포인트 목록 총 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getLocgovPointCountBySearchParam(ContributionSetupSearchParam searchParam) {
		return locgovMapper.getLocgovPointCountBySearchParam(searchParam);
	}

	/**
	 * 지자체관리 목록 > 포인트 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<ContributionSetup> getLocgovPointListBySearchParam(ContributionSetupSearchParam searchParam) {
		return locgovMapper.getLocgovPointListBySearchParam(searchParam);
	}

	/**
	 * 지자체관리 목록 > 지자체 삭제
	 * @param locgov
	 * @return
	 */
	@Override
	public String deleteLocgov(Locgov locgov) throws RuntimeException {
		String code = "FAIL";

		if(locgov != null && locgov.getLocgovCodeList() != null && locgov.getLocgovCodeList().size() > 0) {

			// 삭제하려는 지자체로 기부된 이력이 존재하는지 확인
			if(locgovMapper.getContributionCountByLocgovCode(locgov) == 0) {
				int nResult = 0;

				// 지자체 & 기부금 설정 정보 삭제
				for(String locgovCode : locgov.getLocgovCodeList()) {

					// 기부금 설정 삭제
					ContributionSetup contributionSetup = new ContributionSetup();
					contributionSetup.setLocgovCode(locgovCode);
					locgovMapper.deleteContributionSetup(contributionSetup);

					// 지자체 삭제
					locgov.setLocgovCode(locgovCode);
					nResult += locgovMapper.deleteLocgov(locgov);

					deleteLocgovItemImgInfo(locgov);
				}

				if(nResult == locgov.getLocgovCodeList().size()) {
					code = "SUCC";
				} else {
					throw new RuntimeException();
				}

			} else {
				code = "NOT_DEL";
			}
		}

		return code;
	}

	/**
	 * 지자체 등록
	 * @param locgov
	 * @return
	 */
	@Override
	public String insertLocgov(Locgov locgov) throws RuntimeException {
		String code = "FAIL";

		// 0. 유효성 검사 (이미 등록된 지자체 정보인지 확인)
		LocgovSearchParam searchParam = new LocgovSearchParam();
		searchParam.setLocgovCode(locgov.getLocgovCode());
		if(locgovMapper.getLocgovDetails(searchParam) != null) {
			code = "DUP";
			return code;
		}


		if (locgov.getOffcsFiles() != null) {
			String newFileName = this.saveFile(locgov.getOffcsFiles(), locgov.getUploadPath(), AVAILABLE_EXTENSION, 5, true);

			locgov.setOrginlFileNm(locgov.getOffcsFiles().getOriginalFilename());
			locgov.setOffcsFileNm(newFileName);
		}

		// 1. 지자체 등록
		int nResult = locgovMapper.insertLocgov(locgov);

		// 2. 부서 이력관리 등록
		/*
		LocgovDeptHist ldh = new LocgovDeptHist();
		ldh.setFrstRegisterId(locgov.getLastUpdusrId());
		ldh.setLocgovCode(locgov.getLocgovCode());
		ldh.setProcessDeptCode(locgov.getProcessDeptCode());

		locgovMapper.insertLocgovDeptHist(ldh);
		*/

		// 2. 포인트 등록
		if(nResult > 0) {
			ContributionSetup contributionSetup = new ContributionSetup();
			contributionSetup.setStdrYear(locgov.getStdrYear());
			contributionSetup.setLocgovCode(locgov.getLocgovCode());
			contributionSetup.setLmtAmt(Integer.parseInt(donationVerification.donationLimitAmt().getLabel()));

			int year = LocalDate.now().getYear();
			CodeInfo codeInfo = CodeUtils.getCodeInfo("DONATION_LIMIT_AMT", String.valueOf(year));

			contributionSetup.setLmtAmt(Integer.parseInt(codeInfo.getLabel()));
			contributionSetup.setPointRate(locgov.getPointRate());
			contributionSetup.setPointValidPd(5);
			contributionSetup.setFrstRegisterId(locgov.getFrstRegisterId());
			contributionSetup.setLastUpdusrId(locgov.getLastUpdusrId());

			if(locgovMapper.insertContributionSetup(contributionSetup) > 0) {
				code = "SUCC";
				// 답례품 배경 이미지 저장 추가
				saveLocgovItemImage(locgov);
			} else {
				throw new RuntimeException();
			}
		}

		return code;
	}

	/**
	 * 로그인 사용자 지자체 코드 상세 정보
	 * @param userId
	 * @return
	 */
	@Override
	public Code getLoginUserLocgovCodeDetails(long userId) {
		return locgovMapper.getLoginUserLocgovCodeDetails(userId);
	}

	/**
	 * 지자체 상세 정보 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public Locgov getLocgovDetails(LocgovSearchParam searchParam) {
		Locgov locgov = locgovMapper.getLocgovDetails(searchParam);

		if (locgov != null) {
			LocgovItemImage locgovItemImage = locgovImageMapper.getLocgovImageInfo(locgov.getLocgovCode());
			if (locgovItemImage != null) {
				if (!StringUtils.isEmpty(locgovItemImage.getPcFileName())) {
					locgov.setPcFilePath("/upload/locgovItem/" + locgov.getLocgovCode() + File.separator + locgovItemImage.getPcFileName());
				}
				if (!StringUtils.isEmpty(locgovItemImage.getMobileFileName())) {
					locgov.setMbFilePath("/upload/locgovItem/" + locgov.getLocgovCode() + File.separator + locgovItemImage.getMobileFileName());
				}
			}
		}

		return locgov;
	}

	/**
	 * 기부금 설정 상세 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public ContributionSetup getContributionSetupDetails(ContributionSetupSearchParam searchParam) {
		return locgovMapper.getContributionSetupDetails(searchParam);
	}

	/**
	 * 기부금 설정 등록
	 * @param contributionSetup
	 * @return
	 */
	@Override
	public int insertContributionSetup(ContributionSetup contributionSetup) throws RuntimeException {

		contributionSetup.setLmtAmt(Integer.parseInt(donationVerification.donationLimitAmt().getLabel()));

		int year = LocalDate.now().getYear();
		CodeInfo codeInfo = CodeUtils.getCodeInfo("DONATION_LIMIT_AMT", String.valueOf(year));

		contributionSetup.setLmtAmt(Integer.parseInt(codeInfo.getLabel()));
		contributionSetup.setPointValidPd(1);
		return locgovMapper.insertContributionSetup(contributionSetup);
	}

	/**
	 * 기부금 설정 수정
	 * @param contributionSetup
	 * @return
	 */
	@Override
	public int updateContributionSetup(ContributionSetup contributionSetup) throws RuntimeException {
		return locgovMapper.updateContributionSetup(contributionSetup);
	}

	/**
	 * 지자체 수정
	 * @param locgov
	 * @return
	 * @throws Exception
	 */
	@Override
	public String updateLocgov(Locgov locgov) throws RuntimeException, IOException {
		String code = "FAIL";

		Locgov beforeLocgov = locgovMapper.getProcessDeptCodeAndHonorAmt(locgov.getLocgovCode());

		if (locgov.getOffcsFiles() != null) {
			String newFileName = this.saveFile(locgov.getOffcsFiles(), locgov.getUploadPath(), AVAILABLE_EXTENSION, 5, true);

			if (newFileName == null) {
				throw new IOException("파일 등록에 실패하였습니다.");
			}

			locgov.setOrginlFileNm(locgov.getOffcsFiles().getOriginalFilename());
			locgov.setOffcsFileNm(newFileName);
		}

		if(locgovMapper.updateLocgov(locgov) > 0) {

			String beforeDeptCode = beforeLocgov.getProcessDeptCode();

			// 부서코드 이력 등록
			if (!beforeDeptCode.equals(locgov.getProcessDeptCode())) {
				LocgovDeptHist ldh = new LocgovDeptHist();
				ldh.setFrstRegisterId(locgov.getLastUpdusrId());
				ldh.setLocgovCode(locgov.getLocgovCode());
				ldh.setProcessDeptCode(beforeDeptCode);

				locgovMapper.insertLocgovDeptHist(ldh);
			}

			int level1Amt = beforeLocgov.getStdr1levelAmt();
			int level2Amt = beforeLocgov.getStdr2levelAmt();
			int level3Amt = beforeLocgov.getStdr3levelAmt();

			// 명예회원 재설정
			if (level1Amt != locgov.getStdr1levelAmt() || level2Amt != locgov.getStdr2levelAmt() || level3Amt != locgov.getStdr3levelAmt()) {
				locgovMapper.deleteHonorCntrUser(locgov);

				if (locgov.getStdr1levelAmt() >= 100 || locgov.getStdr2levelAmt() >= 100 || locgov.getStdr3levelAmt() >= 100) {
					locgovMapper.insertHonorCntrUser(locgov);
				}

				List<HonorCntr> honorList = locgovMapper.getHonorUserList(locgov.getLocgovCode());

				List<GiveUserSmsInfo> smsList = honorList.stream().map(honor -> honor.getSmsInfo()).collect(Collectors.toList());
				smsIpsService.giveSendSms(smsList, SmsType.HONOR_DONATION);

			}


			// 기부 제한 날짜 등록
			int lmttCnt = locgovMapper.getCntrLmttCount(locgov.getLocgovCode());
			boolean isDel = ("".equals(locgov.getLmttBgnDe()) || locgov.getLmttBgnDe() == null) && ("".equals(locgov.getLmttEndDe()) || locgov.getLmttEndDe() == null );

			if (lmttCnt > 0 && isDel) {
				locgovMapper.deleteCntrLmmt(locgov);
			} else if (lmttCnt > 0) {
				locgovMapper.updateCntrLmmt(locgov);
			} else if (lmttCnt <= 0 && !isDel) {
				locgovMapper.insertCntrLmtt(locgov);
			}


			code = "SUCC";
			// 답례품 배경 이미지 저장 추가
			saveLocgovItemImage(locgov);
		}

		return code;
	}

	/**
	 * 로그인 사용자 관리자 권한 확인
	 * @return
	 */
	@Override
	public String getLoginUserAdminRoleCheck() {
		String adminRole = "SYS";

		// 로그인 사용자 권한 목록 조회
		List<UserRole> userRoleList = UserUtils.getUser().getUserRoles();

		// 로그인 사용자 관리자 권한 체크 (시스템/행안부/지자체/오프라인)
		if(userRoleList != null) {
			for(UserRole userRole : userRoleList) {
				String auth = userRole.getAuthority();
				if(auth != null && auth.indexOf("ROLE_ADMIN_") > -1) {
					switch(auth) {
						case "ROLE_ADMIN_5" :
							adminRole = "LOC";
							break;
						case "ROLE_ADMIN_6" :
							adminRole = "LOC";
							break;
						case "ROLE_ADMIN_7" :
							adminRole = "OFF";
							break;
						case "ROLE_ADMIN_8" :
							adminRole = "OFF";
							break;
						case "ROLE_ADMIN_11" :
							adminRole = "WCM";
							break;
						default :
							break;
					};

					break;
				};
			};
		};

		return adminRole;
	}

	@Override
	public int getLocgovDeptHistListCount(LocgovDeptSearchParam params) {
		return locgovMapper.getLocgovDeptHistListCount(params);
	}

	@Override
	public List<LocgovDeptHist> getLocgovDeptHistList(LocgovDeptSearchParam param) {
		if(param.getPage() == 0) {
			param.setPage(1);
    	}
		return locgovMapper.getLocgovDeptHistList(param);
	}


	@Override
	public boolean deleteOffcsFile(Locgov locgov) {

		Locgov result = locgovMapper.getLocgovOffcsInfo(locgov.getLocgovCode());

		if (result == null) return false;

		Path filePath = Paths.get(result.getFullFilePath());

		try {
			boolean isFile = Files.deleteIfExists(filePath);
			/*if (isFile) {
				locgovMapper.updateLocgovOffcsInfo(locgov.getLocgovCode());
			}
			return isFile;
			*/
			locgovMapper.updateLocgovOffcsInfo(locgov.getLocgovCode());
			return true;
		} catch (IOException e) {
//			e.printStackTrace();

			return false;
		} catch (Exception e) {
//			e.printStackTrace();

			return false;
		}

	}

	@Override
	public String saveFile(MultipartFile file, String filePath, String[] ableExt, int maxSize, boolean isEncrypt) {
		int size = maxSize * 1024 * 1024;
		boolean extenstion_check = false;

		// 파일 확장자 체크
		String extension = FileUtils.getExtension(file.getOriginalFilename());

		for (int i = 0; i < ableExt.length; i++) {
			if (extension.equals(ableExt[i])) {
				extenstion_check = true;
				break;
			}
		}

		if (!extenstion_check) throw new UserException("유효하지 않은 파일입니다.");
		if (size < file.getSize()) throw new UserException("업로드 가능 최대 용량 : " + maxSize + "MB 입니다.");

		String saveFileName = RandomStringUtils.randomNewFileName(extension);

	    InputStream is = null;

	    try {
	    	Path path = Paths.get(filePath).toAbsolutePath().normalize();

	    	// 디렉토리 생성
	    	Files.createDirectories(path);

	    	if (isEncrypt) {
	    		// 파일을 정형화된 hex로 만들어서 암호화 후 저장
	    		String hexFile = new String(Hex.encode(file.getBytes()));
	    		String encHex = pCrypto.Encrypt("normal", hexFile, "");

	    		is = new ByteArrayInputStream(encHex.getBytes());
	    	} else {
	    		is = file.getInputStream();
	    	}


	    	Path target = Paths.get(filePath + File.separator + saveFileName);
	    	Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);

			return saveFileName;

		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Locgov getLocgovOffcsInfo(String locgov) {
		return locgovMapper.getLocgovOffcsInfo(locgov);
	}

	@Override
	public String getLocgovCodeByOpId(long userId, IdType idType) {
		String locgovCode = "";
		switch (idType) {
		case MANAGER:
			locgovCode = locgovMapper.getLocgovCodeByManagerId(userId);
			break;
		case SELLER:
			locgovCode = locgovMapper.getLocgovCodeBySellerId(userId);
			break;
		case SELLER_USER:
			locgovCode = locgovMapper.getLocgovCodeBySellerUserId(userId);
			break;
		default:
			locgovCode = "00000";
			break;
		}
		return locgovCode;
	}

	@Override
	public String getUpperLocgovCode(String locgov) {
		return locgovMapper.getUpperLocgovCode(locgov);
	}

	// 답례품 이미지 저장
	private String getItemImgUploadPath(String locgov) {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("locgovItem")
				.append(File.separator)
				.append(locgov)
				.toString();
	}


	private String saveItemImgFile(MultipartFile file, String filePath, String[] ableExt, int maxSize, boolean isEncrypt) {
		int size = maxSize * 1024 * 1024;
		boolean extenstion_check = false;

		// 파일 확장자 체크
		String extension = FileUtils.getExtension(file.getOriginalFilename());

		for (int i = 0; i < ableExt.length; i++) {
			if (extension.equals(ableExt[i])) {
				extenstion_check = true;
				break;
			}
		}

		if (!extenstion_check) throw new UserException("유효하지 않은 파일입니다.");
		if (size < file.getSize()) throw new UserException("업로드 가능 최대 용량 : " + maxSize + "MB 입니다.");

		String saveFileName = RandomStringUtils.randomNewFileName(extension);

	    InputStream is = null;

	    try {
	    	Path path = Paths.get(filePath).toAbsolutePath().normalize();

	    	// 디렉토리 생성
	    	Files.createDirectories(path);

	    	if (isEncrypt) {
	    		// 파일을 정형화된 hex로 만들어서 암호화 후 저장
	    		String hexFile = new String(Hex.encode(file.getBytes()));
	    		String encHex = pCrypto.Encrypt("normal", hexFile, "");

	    		is = new ByteArrayInputStream(encHex.getBytes());
	    	} else {
	    		is = file.getInputStream();
	    	}


	    	Path target = Paths.get(filePath + File.separator + saveFileName);
	    	Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);

			return saveFileName;

		} catch (IOException e) {
			log.error(getClass().getName() + " :: saveItemImgFile error1", e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(getClass().getName() + " :: saveItemImgFile error2", e);
				}
			}
		}
	}

	private void saveLocgovItemImage(Locgov locgov) {
		try {
			LocalDateTime now = LocalDateTime.now();
			Timestamp timestamp = Timestamp.valueOf(now);

			boolean isInsert = false;
			boolean fileExist = false;

			LocgovItemImage locgovItemImage = getLocgovItemImage(locgov.getLocgovCode());

			if (locgovItemImage == null) {
				locgovItemImage = new LocgovItemImage();
				locgovItemImage.setLocgovCode(locgov.getLocgovCode());
				locgovItemImage.setFrstRegisterId(UserUtils.getUserId());
				locgovItemImage.setFrstRegistPnttm(timestamp);
				isInsert = true;
			}

			locgovItemImage.setLastUpdusrId(UserUtils.getUserId());
			locgovItemImage.setLastUpdtPnttm(timestamp);

			if (locgov.getAddPcFile() != null) {
				String pcFileName = saveItemImgFile(locgov.getAddPcFile(), getItemImgUploadPath(locgov.getLocgovCode()), AVAILABLE_EXTENSION, 10, false);
				String beforeFileName = locgovItemImage.getPcFileName();
				if (pcFileName != null) {
					locgovItemImage.setPcFileName(pcFileName);
					fileExist = true;

					if (!StringUtils.isEmpty(beforeFileName)) {		// 기존 파일 있으면 삭제
						locgovItemImgFileDelete(locgov.getLocgovCode(), beforeFileName);
					}
				}
			}
			if (locgov.getAddMbFile() != null) {
				String mbFileName = saveItemImgFile(locgov.getAddMbFile(), getItemImgUploadPath(locgov.getLocgovCode()), AVAILABLE_EXTENSION, 10, false);
				String beforeFileName = locgovItemImage.getMobileFileName();
				if (mbFileName != null) {
					locgovItemImage.setMobileFileName(mbFileName);
					fileExist = true;

					if (!StringUtils.isEmpty(beforeFileName)) {		// 기존 파일 있으면 삭제
						locgovItemImgFileDelete(locgov.getLocgovCode(), beforeFileName);
					}
				}
			}

			if (fileExist) {
				if (isInsert) {
					locgovImageMapper.insertLocgovImageInfo(locgovItemImage);
				} else {
					locgovImageMapper.updateLocgovImageInfo(locgovItemImage);
				}
			}
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " :: saveLocgovItemImage error" , e);
		}
	}

	// 지자체 삭제시 지자체 답례품 배경 이미지 삭제
	private void deleteLocgovItemImgInfo(Locgov locgov) {
		LocgovItemImage locgovItemImage = locgovImageMapper.getLocgovImageInfo(locgov.getLocgovCode());

		if (locgovItemImage != null) {
			if (!StringUtils.isEmpty(locgovItemImage.getPcFileName())) {
				locgovItemImgFileDelete(locgov.getLocgovCode(), locgovItemImage.getPcFileName());
			}
			if (!StringUtils.isEmpty(locgovItemImage.getMobileFileName())) {
				locgovItemImgFileDelete(locgov.getLocgovCode(), locgovItemImage.getMobileFileName());
			}

			locgovImageMapper.deleteLocgovImageInfo(locgov.getLocgovCode());
		}
	}

	// 지자체 답례품 배경 파일 삭제
	private boolean locgovItemImgFileDelete(String locgovCode, String fileName) {
		try {
			boolean isFile = Files.deleteIfExists(Paths.get(getItemImgUploadPath(locgovCode) + File.separator + fileName));
			return isFile;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public LocgovItemImage getLocgovItemImage(String locgov) {
//		return locgovImageMapper.getLocgovImageInfo(locgov);
		LocgovItemImage locgovItemImage = locgovImageMapper.getLocgovImageInfo(locgov);

		transactionManager.commit(transactionManager.getTransaction(new DefaultTransactionDefinition()));

		return locgovItemImage;
	}

	@Override
	public boolean deleteLocgovItemImage(String locgovCode, String type) {
		LocgovItemImage locgovItemImage = getLocgovItemImage(locgovCode);
		if (locgovItemImage != null) {
			if ("PC".equalsIgnoreCase(type)) {
				String fileName = locgovItemImage.getPcFileName();
				if (!StringUtils.isEmpty(fileName)) {
					locgovItemImgFileDelete(locgovCode, fileName);
					locgovItemImage.setPcFileName("");
				}
			} else if ("MB".equalsIgnoreCase(type)) {
				String fileName = locgovItemImage.getMobileFileName();
				if (!StringUtils.isEmpty(fileName)) {
					locgovItemImgFileDelete(locgovCode, fileName);
					locgovItemImage.setMobileFileName("");
				}
			}
			locgovImageMapper.updateLocgovImageInfo(locgovItemImage);
		}
		return true;
	}

	// 답례품 이미지 저장
}

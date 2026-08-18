package saleson.shop.banner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.FileUtils;

import lombok.RequiredArgsConstructor;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.banner.domain.MainBannerManager;
import saleson.shop.banner.support.MainBannerManagerSearchParam;

@RequiredArgsConstructor
@Service("mainBannerServiceImpl")
public class MainBannerServiceImpl extends EgovAbstractServiceImpl implements MainBannerService {
	private static final Logger log = LoggerFactory.getLogger(MainBannerServiceImpl.class);

	private final MainBannerMapper mainBannerMapper;
	private final SequenceService sequenceService;
	private final FileService fileService;
	private final FileStorage fileStorage;

	/**
	 * 메인 배너 등록
	 * @param mainBannerManager
	 * @return
	 */
	@Override
	public int insertMainBanner(MainBannerManager mainBannerManager) throws OpRuntimeException {

		// 0. sequenceKey
		mainBannerManager.setBannerId(sequenceService.getId("OP_MAIN_BANNER"));


		/*************************************************************************
		 *  1. PC 배너 이미지 저장
		 *************************************************************************/
		if(mainBannerManager.getPcFile() != null && mainBannerManager.getPcFile().getSize() > 0) {
			// 1-1. PC 파일 정보 조회
			MultipartFile pcMultipartFile = mainBannerManager.getPcFile();
			String pcFileExtension = FileUtils.getExtension(pcMultipartFile.getOriginalFilename());
			//String pcDefaultFileName = mainBannerManager.getBannerId()+"_pc"+ "." + pcFileExtension;
			String pcDefaultFileName = RandomStringUtils.randomNewFileName(pcFileExtension);

			// 1-2. 업로드 경로설정
			String pcUploadPath = mainBannerManager.getUploadPath();
			fileService.makeUploadPath(pcUploadPath);

			// 1-3. 파일명 중복파일 삭제
			//fileStorage.delete(pcUploadPath, ShopUtils.unescapeHtml(pcDefaultFileName));

			// 1-4. 새로운 파일명
			pcDefaultFileName = FileUtils.getNewFileName(pcUploadPath, pcDefaultFileName);

			// 1-5. 저장될 파일
			File pcSaveFile = new File(pcUploadPath + File.separator + pcDefaultFileName);

			// 1-6. 파일저장 (물리적)
			try {
				fileStorage.upload(pcMultipartFile.getBytes(), pcSaveFile);
			} catch (IOException e) {
				log.error("ERROR: {}", getClass().getName() + " :: insertMainBanner Exception ===========");
			}

			// 1-7. 파일정보 저장
			mainBannerManager.setPcFileName(pcDefaultFileName);
			mainBannerManager.setPcOrgFileName(pcMultipartFile.getOriginalFilename());
		}


		/*************************************************************************
		 *  2. 모바일 배너 이미지 저장
		 *************************************************************************/
		if(mainBannerManager.getmFile() != null && mainBannerManager.getmFile().getSize() > 0) {
			// 2-1. PC 파일 정보 조회
			MultipartFile mMultipartFile = mainBannerManager.getmFile();
			String mFileExtension = FileUtils.getExtension(mMultipartFile.getOriginalFilename());
			//String mDefaultFileName = mainBannerManager.getBannerId()+"_m"+ "." + mFileExtension;
			String mDefaultFileName = RandomStringUtils.randomNewFileName(mFileExtension);

			// 2-2. 업로드 경로설정
			String mUploadPath = mainBannerManager.getUploadPath();
			fileService.makeUploadPath(mUploadPath);

			// 2-3. 파일명 중복파일 삭제
			//fileStorage.delete(mUploadPath, ShopUtils.unescapeHtml(mDefaultFileName));

			// 2-4. 새로운 파일명
			mDefaultFileName = FileUtils.getNewFileName(mUploadPath, mDefaultFileName);

			// 2-5. 저장될 파일
			File mSaveFile = new File(mUploadPath + File.separator + mDefaultFileName);

			// 2-6. 파일저장 (물리적)
			try {
				fileStorage.upload(mMultipartFile.getBytes(), mSaveFile);
			} catch (IOException e) {
				log.error("ERROR: {}", getClass().getName() + " :: insertMainBanner Exception ===========");
			}

			// 2-7. 파일정보 저장
			mainBannerManager.setmFileName(mDefaultFileName);
			mainBannerManager.setmOrgFileName(mMultipartFile.getOriginalFilename());
		}


		/*************************************************************************
		 *  3. 메인 배너 등록
		 *************************************************************************/
		// 3. 메인 배너 등록
		int nResult = mainBannerMapper.insertMainBanner(mainBannerManager);


		return nResult;
	}

	/**
	 * 메인 배너 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<MainBannerManager> getMainBannerList(MainBannerManagerSearchParam searchParam) {
		List<MainBannerManager> mainBannerList = mainBannerMapper.getMainBannerList(searchParam);
		for (MainBannerManager mainBanner : mainBannerList) {
			// mainBanner 경로 설정
			mainBanner.setPcFileSrc();
			mainBanner.setMFileSrc();
		}

		return mainBannerList;
	}

	/**
	 * 메인 배너 순서 변경
	 * @param mainBannerManager
	 * @return
	 */
	@Override
	public int updateDisplayOrder(MainBannerManager mainBannerManager) throws OpRuntimeException {
		int nResult = 0;

		// 파라미터
		MainBannerManager param = null;

		// 메인 배너 순서 변경
		List<String> displayOrderList = mainBannerManager.getDisplayOrderList();
		if(displayOrderList != null && displayOrderList.size() > 0) {
			for(String displayOrderInfo : displayOrderList) {
				String[] arrDisplayOrderInfo = displayOrderInfo.split("\\|");
				if(arrDisplayOrderInfo != null && arrDisplayOrderInfo.length == 2) {
					param = new MainBannerManager();
					param.setBannerId(Integer.valueOf(arrDisplayOrderInfo[0]));
					param.setDisplayOrder(Integer.valueOf(arrDisplayOrderInfo[1]));
					nResult += mainBannerMapper.updateDisplayOrder(param);
				}
			}
		}

		return nResult;
	}

	/**
	 * 메인 배너 상세 조회
	 * @param bannerId
	 * @return
	 */
	@Override
	public MainBannerManager getMainBannerDetails(Integer bannerId) {
		return mainBannerMapper.getMainBannerDetails(bannerId);
	}

	/**
	 * 메인 배너 파일 삭제
	 * @param mainBannerManager
	 * @return
	 */
	@Override
	public int deleteMainBannerFile(MainBannerManager mainBannerManager) throws OpRuntimeException {
		int nResult = 0;

		// 1. 상세 정보 조회
		MainBannerManager details = mainBannerMapper.getMainBannerDetails(mainBannerManager.getBannerId());
		if(details == null) {
			return nResult;
		}

		// 2. 파일 논리적 삭제
		nResult = mainBannerMapper.updateMainBannerFileDel(mainBannerManager);

		// 3. 파일 물리적 삭제
		if(nResult > 0) {
			String fileName = "";

			if("PC".equals(CommonUtils.dataNvl(mainBannerManager.getFileType()))
					&& !"".equals(CommonUtils.dataNvl(details.getPcFileName()))) {
				fileName = CommonUtils.dataNvl(details.getPcFileName());

			} else if("M".equals(CommonUtils.dataNvl(mainBannerManager.getFileType()))
					&& !"".equals(CommonUtils.dataNvl(details.getmFileName()))) {
				fileName = CommonUtils.dataNvl(details.getmFileName());
			}

			if(!"".equals(fileName)) {
				fileStorage.delete(details.getUploadPath(), ShopUtils.unescapeHtml(fileName));
			}
		}

		return nResult;
	}

	/**
	 * 메인 배너 수정
	 * @param mainBannerManager
	 * @return
	 */
	@Override
	public int updateMainBanner(MainBannerManager mainBannerManager) throws OpRuntimeException {
		/*************************************************************************
		 *  1. PC 배너 이미지 저장
		 *************************************************************************/

		MainBannerManager details = mainBannerMapper.getMainBannerDetails(mainBannerManager.getBannerId());

		if(mainBannerManager.getPcFile() != null && mainBannerManager.getPcFile().getSize() > 0) {
			// 1-1. PC 파일 정보 조회
			MultipartFile pcMultipartFile = mainBannerManager.getPcFile();
			String pcFileExtension = FileUtils.getExtension(pcMultipartFile.getOriginalFilename());
			// 수정 시 이미지 이름이 같은 경우 이미지 캐시 때문에 리로딩 안되는 문제로 고정 이름 > 랜덤으로 변경
			//String pcDefaultFileName = mainBannerManager.getBannerId()+"_pc"+ "." + pcFileExtension;
			String pcDefaultFileName = RandomStringUtils.randomNewFileName(pcFileExtension);

			// 1-2. 업로드 경로설정
			String pcUploadPath = mainBannerManager.getUploadPath();
			fileService.makeUploadPath(pcUploadPath);

			// 1-3. 파일명 중복파일 삭제
			fileStorage.delete(pcUploadPath, ShopUtils.unescapeHtml(details.getPcFileName()));

			// 1-4. 새로운 파일명
			pcDefaultFileName = FileUtils.getNewFileName(pcUploadPath, pcDefaultFileName);

			// 1-5. 저장될 파일
			File pcSaveFile = new File(pcUploadPath + File.separator + pcDefaultFileName);

			// 1-6. 파일저장 (물리적)
			try {
				fileStorage.upload(pcMultipartFile.getBytes(), pcSaveFile);
			} catch (IOException e) {
				log.error("ERROR: {}", getClass().getName() + " :: updateMainBanner Exception ===========");
			}

			// 1-7. 파일정보 저장
			mainBannerManager.setPcFileName(pcDefaultFileName);
			mainBannerManager.setPcOrgFileName(pcMultipartFile.getOriginalFilename());
		}


		/*************************************************************************
		 *  2. 모바일 배너 이미지 저장
		 *************************************************************************/
		if(mainBannerManager.getmFile() != null && mainBannerManager.getmFile().getSize() > 0) {
			// 2-1. PC 파일 정보 조회
			MultipartFile mMultipartFile = mainBannerManager.getmFile();
			String mFileExtension = FileUtils.getExtension(mMultipartFile.getOriginalFilename());
			// 수정 시 이미지 이름이 같은 경우 이미지 캐시 때문에 리로딩 안되는 문제로 고정 이름 > 랜덤으로 변경
			//String mDefaultFileName = mainBannerManager.getBannerId()+"_m"+ "." + mFileExtension;
			String mDefaultFileName = RandomStringUtils.randomNewFileName(mFileExtension);

			// 2-2. 업로드 경로설정
			String mUploadPath = mainBannerManager.getUploadPath();
			fileService.makeUploadPath(mUploadPath);

			// 2-3. 파일명 중복파일 삭제
			fileStorage.delete(mUploadPath, ShopUtils.unescapeHtml(details.getmFileName()));

			// 2-4. 새로운 파일명
			mDefaultFileName = FileUtils.getNewFileName(mUploadPath, mDefaultFileName);

			// 2-5. 저장될 파일
			File mSaveFile = new File(mUploadPath + File.separator + mDefaultFileName);

			// 2-6. 파일저장 (물리적)
			try {
				fileStorage.upload(mMultipartFile.getBytes(), mSaveFile);
			} catch (IOException e) {
				log.error("ERROR: {}", getClass().getName() + " :: updateMainBanner Exception ===========");
			}

			// 2-7. 파일정보 저장
			mainBannerManager.setmFileName(mDefaultFileName);
			mainBannerManager.setmOrgFileName(mMultipartFile.getOriginalFilename());
		}


		/*************************************************************************
		 *  3. 메인 배너 등록
		 *************************************************************************/
		// 3. 메인 배너 등록
		int nResult = mainBannerMapper.updateMainBanner(mainBannerManager);


		return nResult;
	}
}

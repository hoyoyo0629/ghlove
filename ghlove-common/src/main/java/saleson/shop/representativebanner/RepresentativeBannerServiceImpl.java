package saleson.shop.representativebanner;

import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ThumbnailUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import saleson.common.Const;
import saleson.common.file.infra.FileStorage;
import saleson.common.file.support.ThumbUtils;
import saleson.shop.groupbanner.support.GroupBannerManagerException;
import saleson.shop.representativebanner.domain.RepresentativeBanner;
import saleson.shop.representativebanner.domain.RepresentativeBannerListParam;
import saleson.shop.representativebanner.support.RepresentativeBannerManagerException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service("representativeBannerService")
public class RepresentativeBannerServiceImpl extends EgovAbstractServiceImpl implements RepresentativeBannerService {

	@Autowired
	private RepresentativeBannerMapper representativeBannerMapper;
	
	@Autowired
	private SequenceService sequenceServices;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private FileStorage fileStorage;
	
	@Override
	public List<RepresentativeBanner> getRepresentativeBannerList(RepresentativeBannerListParam listParam) {
		return representativeBannerMapper.getRepresentativeBannerList(listParam);
	}
	
	@Override
	public void updateRepresentativeBanneOrdering(RepresentativeBannerListParam listParam) {
		int i = 0;
		
		for (String id : listParam.getId()) {
			RepresentativeBanner representativeBanner = new RepresentativeBanner();
			
			representativeBanner.setRepresentativeBannerId(Integer.parseInt(id));
			representativeBanner.setDisplayOrder(Integer.parseInt(listParam.getOrdering()[i]));

			representativeBannerMapper.updateRepresentativeBanneOrdering(representativeBanner);
			
			i++;
		}
	}

	@Override
	public RepresentativeBanner getRepresentativeBannerInfo(int representativeBannerId) {
		return representativeBannerMapper.getRepresentativeBannerInfo(representativeBannerId);
	}
	
	@Override
	public void insertRepresentativeBanner(RepresentativeBanner representativeBanner) {
		representativeBannerMapper.insertRepresentativeBanner(representativeBanner);
	}

	@Override
	public void updateRepresentativeBanner(RepresentativeBanner representativeBanner) {
		representativeBannerMapper.updateRepresentativeBanner(representativeBanner);
	}

	@Override
	public void deleteRepresentativeBanner(RepresentativeBanner representativeBanner) {
		representativeBannerMapper.deleteRepresentativeBanner(representativeBanner);
	}

	@Override
	public void editRepresentativeBanner(RepresentativeBanner representativeBanner) {
		
		String redirectUrl = "/opmanager/representative/form/" + representativeBanner.getRepresentativeBannerId();
		
		// 신규 등록
		if(representativeBanner.getRepresentativeBannerId() == 0) {
			
			representativeBanner.setRepresentativeBannerId(sequenceServices.getId("OP_REPRESENTATIVE_BANNER"));
			
			// PC 이미지
			if (StringUtils.isNotEmpty(representativeBanner.getUploadFilePc().getOriginalFilename())) {
				String fileName = this.uploadFile(representativeBanner, "pc");
				if (ObjectUtils.isEmpty(fileName)) {
					throw new RepresentativeBannerManagerException("파일 업로드에 실패 하였습니다.", redirectUrl);
				}
				representativeBanner.setFileNamePc(fileName);
			} else {
				representativeBanner.setFileNamePc("");
			}
			// 모바일 이미지
			if (StringUtils.isNotEmpty(representativeBanner.getUploadFileMobile().getOriginalFilename())) {
				String fileName = this.uploadFile(representativeBanner, "mobile");
				if (ObjectUtils.isEmpty(fileName)) {
					throw new RepresentativeBannerManagerException("파일 업로드에 실패 하였습니다.", redirectUrl);
				}
				representativeBanner.setFileNameMobile(fileName);
			} else {
				representativeBanner.setFileNameMobile("");
			}
			
			this.insertRepresentativeBanner(representativeBanner);
			
		} 
		// 파일 수정인 경우
		else {			// TODO :: 기존 파일 삭제 추가 필요
			
			// PC 이미지
			if (StringUtils.isNotEmpty(representativeBanner.getUploadFilePc().getOriginalFilename())) {
				String fileName = this.uploadFile(representativeBanner, "pc");
				if (ObjectUtils.isEmpty(fileName)) {
					throw new RepresentativeBannerManagerException("파일 업로드에 실패 하였습니다.", redirectUrl);
				}
				representativeBanner.setFileNamePc(fileName);
			} else {
				representativeBanner.setFileNamePc("");
			}
			// 모바일 이미지
			if (StringUtils.isNotEmpty(representativeBanner.getUploadFileMobile().getOriginalFilename())) {
				String fileName = this.uploadFile(representativeBanner, "mobile");
				if (ObjectUtils.isEmpty(fileName)) {
					throw new RepresentativeBannerManagerException("파일 업로드에 실패 하였습니다.", redirectUrl);
				}
				representativeBanner.setFileNameMobile(fileName);
			} else {
				representativeBanner.setFileNameMobile("");
			}
			
			this.updateRepresentativeBanner(representativeBanner);
			
		}
		
	}

	private String uploadFile(RepresentativeBanner banner, String imageType) {
		String fileName = "";
		
		if(imageType.equals("pc") && banner.getUploadFilePc() == null) {
			return "";
		}
		
		if(imageType.equals("pc") && banner.getUploadFilePc().getSize() == 0) {
			return "";
		}
		
		if(imageType.equals("mobile") && banner.getUploadFileMobile() == null) {
			return "";
		}
		
		if(imageType.equals("mobile") && banner.getUploadFileMobile().getSize() == 0) {
			return "";
		}
		
		String fileExtension = "";	// 확장자
		
		if(imageType.equals("pc")) {
			fileExtension = FileUtils.getExtension(banner.getUploadFilePc().getOriginalFilename());
		} else if(imageType.equals("mobile")) {
			fileExtension = FileUtils.getExtension(banner.getUploadFileMobile().getOriginalFilename());
		}
		
		String defaultFileName = DateUtils.getToday(Const.DATETIME_FORMAT) + "_" + imageType + "." + fileExtension;	// 파일명
				
		// 1. 업로드 경로설정
		String uploadPath = banner.getDefaultFilePath();
		fileService.makeUploadPath(uploadPath);
		
		// 2. 새로운 파일명.
		defaultFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

		// 3. 저장될 파일 
		File saveFile = new File(uploadPath + File.separator + defaultFileName);

		// 생성.
		try {
			if(imageType.equals("pc")) {
				ThumbUtils.create(banner.getUploadFilePc(), saveFile, banner.getThumbnailWidthPc(), banner.getThumbnailHeightPc(), fileStorage);
			} else if(imageType.equals("mobile")) {
				ThumbUtils.create(banner.getUploadFileMobile(), saveFile, banner.getThumbnailWidthMobile(), banner.getThumbnailHeightMobile(), fileStorage);
			}
			fileName = defaultFileName;
		} catch (IOException e) {
			throw new RepresentativeBannerManagerException("파일 업로드에 실패 하였습니다.", e);
		}
		
		return fileName;
	}

	@Override
	public List<RepresentativeBanner> getRepresentativeBannerListFront(RepresentativeBannerListParam listParam) {
		return representativeBannerMapper.getRepresentativeBannerListFront(listParam);
	}
}

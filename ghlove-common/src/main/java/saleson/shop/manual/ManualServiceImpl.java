package saleson.shop.manual;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.common.Const;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.manual.domain.Manual;
import saleson.shop.manual.support.ManualParam;
import saleson.shop.offgive.domain.Manager;


@Service("manualService")
public class ManualServiceImpl extends EgovAbstractServiceImpl implements ManualService{
	private static final Logger log = LoggerFactory.getLogger(ManualServiceImpl.class);

	@Autowired
	ManualMapper manualMapper;

	@Autowired SequenceService sequenceService;

	@Autowired
    private FileService fileService;

	@Autowired
    private FileStorage fileStorage;

	@Override
	public void insertManual(Manual item) {

		// 메뉴구분코드로 중복 확인
		Manual manual = manualMapper.getManualByMenuSeCode(item.getMenuSeCode());
		if (manual != null) {
			throw new UserException("이미 등록 되어 있습니다.");
		}

		int mnlSn = sequenceService.getId("G_MNL");
		item.setMnlSn(mnlSn);
		item.setFrstRegisterId(UserUtils.getUserId());
		manualMapper.insertManual(item);

		if (item.getItemDetailImageFiles() != null) {

			for (MultipartFile multipartFile : item.getItemDetailImageFiles()) {
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "zip"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

					if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

							// 1. 업로드 경로설정
				            StringBuilder uploadPath = new StringBuilder();

				            uploadPath
				                    .append(item.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

	//						String defaultFileName = sequenceId + "." + extension;
	//						storeInquiry.setFileName(fileName);

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리
							if(fileName.endsWith(".jpg")
									|| fileName.endsWith(".jpeg")
									|| fileName.endsWith(".gif")
									|| fileName.endsWith(".bmp")
									|| fileName.endsWith(".png")
									|| fileName.endsWith(".doc")
									|| fileName.endsWith(".docx")
									|| fileName.endsWith(".xls")
									|| fileName.endsWith(".xlsx")
									|| fileName.endsWith(".ppt")
									|| fileName.endsWith(".pptx")
									|| fileName.endsWith(".pdf")
									|| fileName.endsWith(".tif")
									|| fileName.endsWith(".tiff")
									|| fileName.endsWith(".hwp")
									|| fileName.endsWith(".zip")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}", e.getMessage(), e);
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
	//						message = "업로드 가능한 최대 용량 : 5MB 입니다";
		//					return ViewUtils.getView("/store-inquiry/inquiry", message);
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
	//					message = "유효하지 않은 파일입니다.";
		//				return ViewUtils.getView("/store-inquiry/inquiry", message);
					}

//					// 파일이 있으면
					item.setFileNm(saveFileName.toString());
					item.setFileTy(extension);
					item.setOrginlFileNm(fileName);
					item.setLastUpdusrId(UserUtils.getUserId());
					manualMapper.updateManual(item);

				}

				// 파일이 없으면
				item.setLastUpdusrId(UserUtils.getUserId());
				manualMapper.updateManual(item);
			}
        }
	}

	@Override
	public void insertManagerManual(Manual item) {

		// 메뉴구분코드로 중복 확인
//		Manual manual = manualMapper.getManualByMenuSeCode(item.getMenuSeCode());
//		if (manual != null) {
//			throw new UserException("이미 등록 되어 있습니다.");
//		}

//		int mnlSn = sequenceService.getId("G_MNL");
//		item.setMnlSn(mnlSn);
//		item.setFrstRegisterId(UserUtils.getUserId());
//		manualMapper.insertManual(item);

		if (item.getItemDetailImageFiles() != null) {

			for (MultipartFile multipartFile : item.getItemDetailImageFiles()) {
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "zip"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

					if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

							// 1. 업로드 경로설정
				            StringBuilder uploadPath = new StringBuilder();

				            uploadPath
				                    .append(item.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

	//						String defaultFileName = sequenceId + "." + extension;
	//						storeInquiry.setFileName(fileName);

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리
							if(fileName.endsWith(".jpg")
									|| fileName.endsWith(".jpeg")
									|| fileName.endsWith(".gif")
									|| fileName.endsWith(".bmp")
									|| fileName.endsWith(".png")
									|| fileName.endsWith(".doc")
									|| fileName.endsWith(".docx")
									|| fileName.endsWith(".xls")
									|| fileName.endsWith(".xlsx")
									|| fileName.endsWith(".ppt")
									|| fileName.endsWith(".pptx")
									|| fileName.endsWith(".pdf")
									|| fileName.endsWith(".tif")
									|| fileName.endsWith(".tiff")
									|| fileName.endsWith(".hwp")
									|| fileName.endsWith(".zip")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}", e.getMessage(), e);
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
	//						message = "업로드 가능한 최대 용량 : 5MB 입니다";
		//					return ViewUtils.getView("/store-inquiry/inquiry", message);
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
	//					message = "유효하지 않은 파일입니다.";
		//				return ViewUtils.getView("/store-inquiry/inquiry", message);
					}

//					// 파일이 있으면
					item.setFileNm(saveFileName.toString());
					item.setOrginlFileNm(fileName);
//					item.setLastUpdusrId(UserUtils.getUserId());
					manualMapper.updateManagerManual(item);

				}

				// 파일이 없으면 아무것도 안함.

			}
        }
	}

	@Override
	public int getManualCount(ManualParam manualParam) {

		return manualMapper.getManualCount(manualParam);
	}

	@Override
	public List<Manual> getManualList(ManualParam manualParam) {

		return manualMapper.getManualList(manualParam);
	}

	@Override
	public Manual getManual(int mnlSn) {
		Manual manual = manualMapper.getManual(mnlSn);
		return manual;
	}

	@Override
	public void updateManual(Manual item) {

		if (item.getItemDetailImageFiles() != null) {

			for (MultipartFile multipartFile : item.getItemDetailImageFiles()) {

				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					String extension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String fileName = multipartFile.getOriginalFilename();
					int maxSize = 50 * 1024 * 1024; // 업로드 가능한 최대 용량 : 50MB

					final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "zip"};

					boolean extenstion_check = false;

					for (int i = 0; i < AVAILABLE_EXTENSION.length; i++) {
						if (extension.equals(AVAILABLE_EXTENSION[i])) {
							extenstion_check = true;
						}
					}

					StringBuilder saveFileName = new StringBuilder();
					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
					saveFileName.setLength(0);
				    saveFileName.append(dateNanoTime);
				    saveFileName.append("_").append(multipartFile.getOriginalFilename());

					if (extenstion_check) {
						if (maxSize > multipartFile.getSize()) {

							// 1. 업로드 경로설정
				            StringBuilder uploadPath = new StringBuilder();

				            uploadPath
				                    .append(item.getUploadPath())
				                    .append(File.separator);

							fileService.makeUploadPath(uploadPath.toString());

	//						String defaultFileName = sequenceId + "." + extension;
	//						storeInquiry.setFileName(fileName);

							// 2. 저장될 파일
							// 시큐어코딩 CWE-434 처리
							if(fileName.endsWith(".jpg")
									|| fileName.endsWith(".jpeg")
									|| fileName.endsWith(".gif")
									|| fileName.endsWith(".bmp")
									|| fileName.endsWith(".png")
									|| fileName.endsWith(".doc")
									|| fileName.endsWith(".docx")
									|| fileName.endsWith(".xls")
									|| fileName.endsWith(".xlsx")
									|| fileName.endsWith(".ppt")
									|| fileName.endsWith(".pptx")
									|| fileName.endsWith(".pdf")
									|| fileName.endsWith(".tif")
									|| fileName.endsWith(".tiff")
									|| fileName.endsWith(".hwp")
									|| fileName.endsWith(".zip")
									) {

								File saveFile = new File(uploadPath + File.separator + saveFileName);

								try {
									fileStorage.upload(multipartFile.getBytes(), saveFile);
								} catch (IOException e) {
									log.error("ERROR: {}");
								}
							} else {
								throw new UserException("유효하지 않은 파일입니다.");
							}

						} else {
	                        throw new UserException("업로드 가능한 최대 용량 : 50MB 입니다");
	//						message = "업로드 가능한 최대 용량 : 5MB 입니다";
		//					return ViewUtils.getView("/store-inquiry/inquiry", message);
						}
					} else {
						throw new UserException("유효하지 않은 파일입니다.");
	//					message = "유효하지 않은 파일입니다.";
		//				return ViewUtils.getView("/store-inquiry/inquiry", message);
					}

					// 파일이 있으면
					item.setFileNm(saveFileName.toString());
					item.setFileTy(extension);
					item.setOrginlFileNm(fileName);
					item.setLastUpdusrId(UserUtils.getUserId());
					manualMapper.updateManual(item);

				}

				// 파일이 없으면
				item.setLastUpdusrId(UserUtils.getUserId());
				manualMapper.updateManual(item);


			}
		}

	}

	@Override
	public void deleteManual(int mnlSn) {
		manualMapper.deleteManual(mnlSn);
	}

	/**
	 * 체크박스 선택 삭제
	 * @param manualParam
	 * @return
	 */
	@Override
	public String manualListDelete(ManualParam manualParam) {
		String code = "FAIL";

		if(manualParam.getManualList() != null && manualParam.getManualList().size() > 0) {
			int nResult = 0;


			for (String manualListId : manualParam.getManualList()) {
				int dataId = Integer.parseInt(manualListId);
				manualMapper.deleteManual(dataId);
				nResult ++;

			}

			if (nResult == manualParam.getManualList().size()) {
				code = "SUCC";
			} else {
				throw new RuntimeException();
			}
		}
		return code;
	}

	@Override
    public void deleteItemImageByItemId(Integer mnlSn) {
		Manual manualFile = manualMapper.getManual(mnlSn);

		Manual item = new Manual();

        String listImage = item.getUploadPath() + "/" + ShopUtils.unescapeHtml(manualFile.getFileNm());
        log.debug(">>> listImage : {}", listImage);

        fileStorage.delete(listImage);

        // 데이터 삭제
        manualMapper.deleteManualFile(mnlSn);
    }

	@Override
	public void managerDeleteItemImageByItemId(String menuId) {
		Manual manualFile = manualMapper.getOpMenu(menuId);

		Manual item = new Manual();

		String listImage = item.getUploadPath() + "/" + ShopUtils.unescapeHtml(manualFile.getFileNm());
		log.debug(">>> listImage : {}", listImage);

		fileStorage.delete(listImage);

		// 데이터 삭제
		manualMapper.managerDeleteManualFile(menuId);
	}

	@Override
	public List<HashMap<String, Object>> getAllMenuList() {

		String authority = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_5".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_7".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_8".equals(userRole.getAuthority())
				) {
				authority = userRole.getAuthority();
				break;
			} else {
				authority = "";
				break;
			}
		}

		List<HashMap<String, Object>> list =  manualMapper.getAllMenuListDept2(authority);
		for (int i = 0; i < list.size(); i++) {
			String menuId = list.get(i).get("menu_id").toString();

			HashMap<String, Object> map = new HashMap<>();
			map.put("authority", authority);
			map.put("menuId", menuId);
			List<HashMap<String, Object>> list3 = manualMapper.getAllMenuListDept3(map);

			list.get(i).put("detail", list3);
		}
		return list;
	}

	@Override
    public Manual getOpMenu(String menuId) {
		return manualMapper.getOpMenu(menuId);
    }

}

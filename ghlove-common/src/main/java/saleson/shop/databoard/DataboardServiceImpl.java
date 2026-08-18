package saleson.shop.databoard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.imageio.ImageIO;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.common.Const;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.ShopUtils;
import saleson.shop.databoard.domain.Databoard;
import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.databoard.support.DataboardParam;
import saleson.shop.item.ItemServiceImpl;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemImage;


@Service("databoardService")
public class DataboardServiceImpl extends EgovAbstractServiceImpl implements DataboardService{
	private static final Logger log = LoggerFactory.getLogger(DataboardServiceImpl.class);

	@Autowired
	DataboardMapper databoardMapper;

	@Autowired SequenceService sequenceService;

	@Autowired
    private FileService fileService;

	@Autowired
    private FileStorage fileStorage;

	@Override
	public void insertDataboard(Databoard item) {

		int dataId = sequenceService.getId("OP_DATA_BOARD");
		item.setDataId(dataId);
		databoardMapper.insertDataboard(item);


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

					DataboardFile itemImage = new DataboardFile();
	                itemImage.setDataFileId(String.valueOf(sequenceService.getId("OP_DATA_BOARD_FILE")));
	                itemImage.setDataId(item.getDataId());
	                itemImage.setFileName(saveFileName.toString());
	                itemImage.setFileTy(extension);
	                itemImage.setOrgFileName(multipartFile.getOriginalFilename());

	                databoardMapper.insertDataboardFile(itemImage);
				}
			}









//            StringBuilder uploadPath = new StringBuilder();
//
//            uploadPath
//                    .append(item.getUploadPath())
//                    .append(File.separator);
//
//            fileService.makeUploadPath(uploadPath.toString());
//
//            for (MultipartFile multipartFile : item.getItemDetailImageFiles()) {
//                if (multipartFile.getSize() > 0) {
//
//                    StringBuilder saveFileName = new StringBuilder();
//                    BufferedImage bufferedImage = null;
//                    String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
//
//                    try {
//                        try {
//                            bufferedImage = ImageIO.read(multipartFile.getInputStream());
//                            if (bufferedImage == null) {
//                                throw new IOException();
//                            }
//                        } catch (IOException e1) {
//                            throw new UserException("파일 읽어오기 실패");
//                        }
//
//                        // 2. 파일명
//                        saveFileName.setLength(0);
//                        saveFileName.append(dateNanoTime);
//                        saveFileName.append("_").append(multipartFile.getOriginalFilename());
//
//                        // 3. 저장될 파일
//                        File saveFile = new File(new StringBuilder(uploadPath).append(File.separator).append(saveFileName).toString());
//
//                        ByteArrayOutputStream output = new ByteArrayOutputStream();
//                        output.flush();
//                        ImageIO.write(bufferedImage, FileUtils.getExtension(multipartFile.getOriginalFilename()), output);
//                        output.close();
//                        fileStorage.upload(output.toByteArray(), saveFile);
//                    } catch (Exception e) {
//                        log.debug("파일 생성중 오류 : {}", e.getMessage());
//                        throw new UserException("파일 생성중 오류");
//                    }
//
//                    DataboardFile itemImage = new DataboardFile();
//                    itemImage.setDataFileId(sequenceService.getId("OP_DATA_BOARD_FILE"));
//                    itemImage.setDataId(item.getDataId());
//                    itemImage.setFileName(saveFileName.toString());
//                    itemImage.setFileTy(multipartFile.getContentType());
//                    itemImage.setOrgFileName(multipartFile.getOriginalFilename());
//
//                    databoardMapper.insertDataboardFile(itemImage);
//
//                    // 첫 번째 이미지가 목록에 출력
////                    if (!hasItemImage && uploadFileCount == 0) {
////                        item.setItemImage(saveFileName.toString());
////                    }
//
////                    uploadFileCount++;
////                    ordering++;
//                }
//            }
        }

		// TODO : JOO 첨부파일 등록
//		databoardMapper.insertDataboardFile(databoard);


	}

	@Override
	public int getDataboardCount(DataboardParam databoardParam) {

		return databoardMapper.getDataboardCount(databoardParam);
	}

	@Override
	public List<Databoard> getDataboardList(DataboardParam databoardParam) {

		return databoardMapper.getDataboardList(databoardParam);
	}

	@Override
	public Databoard getDataboard(int dataId) {
		Databoard databoard = databoardMapper.getDataboard(dataId);
		databoard.setDataboardFiles(databoardMapper.getFrontDataboardFileList(dataId));

		return databoard;
	}

	@Override
	public void updateDataboard(Databoard item) {

		//databoard.setCategoryTeam(databoard.getCategoryTeam().replaceAll(",", "|"));
		databoardMapper.updateDataboard(item);

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

					DataboardFile itemImage = new DataboardFile();
	                itemImage.setDataFileId(String.valueOf(sequenceService.getId("OP_DATA_BOARD_FILE")));
	                itemImage.setDataId(item.getDataId());
	                itemImage.setFileName(saveFileName.toString());
	                itemImage.setFileTy(extension);
	                itemImage.setOrgFileName(multipartFile.getOriginalFilename());

	                databoardMapper.insertDataboardFile(itemImage);
				}
			}
		}

	}

	@Override
	public void deleteDataboard(int dataId) {

		databoardMapper.deleteDataboard(dataId);

	}

	@Override
	public List<Databoard> getFrontDataboardList(DataboardParam databoardParam) {

		return databoardMapper.getFrontDataboardList(databoardParam);
	}

	@Override
	public List<Databoard> getFrontDataboardListByTeamCodes(DataboardParam databoardParam) {
		return databoardMapper.getFrontDataboardListByTeamCodes(databoardParam);
	}

	@Override
	public int getFrontDataboardListCount(DataboardParam databoardParam) {
		return databoardMapper.getFrontDataboardListCount(databoardParam);
	}

	@Override
	public void addHitCount(int dataId) {

		databoardMapper.addHitCount(dataId);
	}

	/**
	 * 체크박스 선택 삭제
	 * @param databoardParam
	 * @return
	 */
	@Override
	public String databoardListDelete(DataboardParam databoardParam){
		String code = "FAIL";

		if(databoardParam.getDataboardList() != null && databoardParam.getDataboardList().size() > 0) {
			int nResult = 0;


			for (String databoardListId : databoardParam.getDataboardList()) {
				int dataId = Integer.parseInt(databoardListId);
				nResult += databoardMapper.databoardListDelete(dataId);

			}

			if (nResult == databoardParam.getDataboardList().size()) {
				code = "SUCC";
			}
		}
		return code;
	}

	@Override
	public void deleteDataboardSeller(int databoardSellerId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 자료실 상세 (사용자)
	 * @param dataId
	 * @return
	 */
	@Override
	public Databoard getFrontDataboardDetail(Integer dataId) {
		databoardMapper.addHitCount(dataId);
		return databoardMapper.getDataboard(dataId);
	}

	/**
	 * 자료실 파일 목록 조회 (사용자)
	 * @param dataId
	 * @return
	 */
	@Override
	public List<DataboardFile> getFrontDataboardFileList(Integer dataId) {
		return databoardMapper.getFrontDataboardFileList(dataId);
	}

	/**
	 * 자료실 파일 조회
	 * @param dataFileId
	 * @return
	 */
	@Override
	public DataboardFile getFrontDataboardFileDetail(String dataFileId) {
		return databoardMapper.getFrontDataboardFileDetail(dataFileId);
	}

	@Override
    public void deleteItemImageByItemId(String dataFileId) {
		try {
			dataFileId = URLDecoder.decode(dataFileId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(">>> deleteItemImageByItemId 인코딩 에러 ", e.toString());
		}

		DataboardFile databoardFile = databoardMapper.getFrontDataboardFileDetail(dataFileId);

		Databoard item = new Databoard();

        String listImage = item.getUploadPath() + "/" + ShopUtils.unescapeHtml(databoardFile.getFileName());
        log.debug(">>> listImage : {}", listImage);

        fileStorage.delete(listImage);

        // 데이터 삭제
        databoardMapper.deleteDataboardFile(dataFileId);
    }

}

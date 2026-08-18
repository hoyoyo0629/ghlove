package saleson.shop.transfer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;

import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.ShopUtils;
import saleson.shop.item.domain.ItemImage;

@Service("transferService")
public class TransferServiceImpl implements TransferService {

	private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);


    @Autowired
    TransferMapper transferMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorage fileStorage;

    @Autowired
    private SequenceService sequenceService;

    @Value("${upload.root}")
	String uploadRoot;


    /**
     * 상품 이지미 마이그레이션
     */
    public HashMap<String, Object> transferItemImage(String count, List<String> item_id_list) {
//    	/*
//    	 * <list> -> temp 테이블을 읽는다. group by item_id(정렬 item_id asc)
//    	 *
//    	 * for () -> Item List
//    	 *
//    	 * 		ㄴ<list> -> 이미지 리스트들을 list 로 조회한다.(order asc)
//    	 *
//    	 * 		image_nama 을 해당 폴더에서 찾는다.
//    	 *
//    	 *		for () -> image List
//    	 *
//    	 * 			for () -> (config 에 설정되어 있는 사이즈별로 4개)
//    	 *
//    	 * 				이미지를 사이즈 별로 만들어서 업로드한다.
//    	 *
//    	 * 				검증 : temp_item_image_mig_tables 에 컬럼을 추가 해서 해당 이미지 파일명을 저장한다.
//    	 *
//    	 * 				insertItemImage
//    	 *
//    	 * 				if (list 의 ordering = 1 && 마지막 순번 XS 파일 일때)
//    	 * 					op_item 에 없데이트
//    	 */
//
//
//    	// <list> -> temp 테이블을 읽는다. group by item_id(정렬 item_id asc)
//    	HashMap<String, Object> param = new HashMap<>();
//    	param.put("count", count);
//    	param.put("item_id_list", item_id_list);
////    	List<HashMap<String, Object>> itemIdList = transferMapper.getTempItemImageMigTablesGroupByItemIdList(param);
//    	
//    	List<HashMap<String, Object>> imageList = transferMapper.getTempItemImageMigTablesByItemIdList(param);
////    	int itemImageCount = 0;
//    	int itemImageCreateCount = 0;
////    	for (HashMap<String, Object> map : itemIdList ) {
//		for (HashMap<String, Object> imageMap : imageList ) {
//    		// ㄴ<list> -> 이미지 리스트들을 list 로 조회한다.(order asc)
////    		HashMap<String, Object> paramMap = new HashMap<>();
////    		paramMap.put("item_id", map.get("ITEM_ID"));
////    		List<HashMap<String, Object>> imageList = transferMapper.getTempItemImageMigTablesByItemIdList(paramMap);
////    		itemImageCount += imageList.size();
//
//    		// image_name 을 해당 폴더에서 찾는다.
//        	// TODO : upload_file/ABS_goods/~ 에 넣을 예정
////        	String path = uploadRoot + File.separator + "migration" + File.separator;
////        	String path = "C://ghlove/workspace/ghlove/ghlove-web/storage/migration/";	// 로컬 예제 경로
//        	String path = uploadRoot + File.separator + "upload" + File.separator + "rc_upload_file" + File.separator + "ABS_goods" + File.separator;
//        	
////        	String savePath = File.separator + transferMapper.getItemCode(map.get("ITEM_ID").toString());
////        	String savePath = File.separator + map.get("ITEM_CODE").toString();
//        	String savePath = File.separator + imageMap.get("ITEM_CODE").toString();
//        	if (savePath != null && !(File.separator + "null").equalsIgnoreCase(savePath)) {
//            	StringBuilder uploadPath = new StringBuilder();
////                uploadPath
////                        .append(getUploadPath())
////                        .append(File.separator)
////                        .append(transferMapper.getItemCode(map.get("ITEM_ID").toString()));
//            	uploadPath.append(getUploadPath()).append(savePath);
//
//                fileService.makeUploadPath(uploadPath.toString());
//                String imageName = imageMap.get("IMAGE_NAME").toString();
////        		for (HashMap<String, Object> imageMap : imageList ) {
//        			InputStream input = null;	
//    				try {
//
//    					File file = null;
//    					if (imageMap.get("IMAGE_NAME").toString().contains("http")) {
//    						URL url = new URL(imageMap.get("IMAGE_NAME").toString());
//    					    file = readFileFromUrl( url );
//    					} else {
//    						file = new File(path + imageMap.get("IMAGE_NAME"));
//    					}
//
//    					DiskFileItem fileItem;
//    					fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length() , file.getParentFile());
//
//    					StringBuilder saveFileName = new StringBuilder();
//    	    			input = new FileInputStream(file);
//    	    			OutputStream os = fileItem.getOutputStream();
//    	    			IOUtils.copy(input, os);
//
//    	    			MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//
//
//    	    			if (file.isFile() && multipartFile.getSize() > 0) {
//
//
//    	                    BufferedImage bufferedImage = null;
//    	                    String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
//
//    	                    // 이미지를 사이즈별로 저장[2017-05-31]minae.yun
//    	                    try {
//    	                    	// for () -> 이미지를 사이즈 별로 만들어서 업로드한다.(config 에 설정되어 있는 사이즈별로 4개)
//    	                    	int thumbnailCount = 1;
//    	                        for (String sizeName : ShopUtils.getThumbnailType()) {
//    	                        	if (multipartFile != null) {
//	    	                            try (InputStream is = multipartFile.getInputStream();) {
//	    	                                bufferedImage = ImageIO.read(is);
//	    	                                if (bufferedImage == null) {
//	    	                                    throw new IOException();
//	    	                                }
//	    	                            } catch (IOException e1) {
//	    	                                log.debug("이미지 읽어오기 실패 : {}", getClass().getName() + " :: saveItem IOException1 ========== :: imageName :: " + imageName, e1);
//	    	                                throw new UserException("이미지 읽어오기 실패");
//	    	                            }
//    	                        	}
//    	                        	itemImageCreateCount++;
//
//    	                            // 2. 파일명
//    	                            saveFileName.setLength(0);
//    	                            saveFileName.append(dateNanoTime);
//    	                            saveFileName.append("_").append(sizeName).append(".").append(FileUtils.getExtension(multipartFile.getOriginalFilename()));
//
//    	                            // 3. 저장될 파일
//    	                            File saveFile = new File(new StringBuilder(uploadPath).append(File.separator).append(saveFileName).toString());
//    	                            int imageSize = ShopUtils.getImageSize(sizeName);
//
//    	                            // 이상우 [2017-03-31 수정] 이미지의 가로 세로 중 작은 쪽이 imageSize보다 크면 true, imageSize+10하면 사이즈 비율에 맞게 조절
//    	                            if (ShopUtils.checkImageSize2(bufferedImage.getWidth(), bufferedImage.getHeight(), imageSize)) {
//    	                                try {
//    	                                    bufferedImage = ShopUtils.getThumbnailImage(bufferedImage, imageSize);
//    	                                } catch (IOException e) {
//    	                                    log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException2 ========== :: imageName :: \" + imageName", e);
//    	                                    throw new UserException("썸네일 생성중 오류");
//    	                                }
//    	                            }
//    	                            try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
//    		                            output.flush();
//    		                            ImageIO.write(bufferedImage, FileUtils.getExtension(multipartFile.getOriginalFilename()), output);
//    	//	                            output.close();
//    		                            fileStorage.upload(output.toByteArray(), saveFile);
//
//    		                            // 검증 : temp_item_image_mig_tables 에 컬럼을 추가 해서 해당 이미지 파일명을 저장한다.
//    		                            HashMap<String, Object> updateMigTableParam = new HashMap<>();
//    			                        updateMigTableParam.put("image_no", Integer.toString(thumbnailCount));
//    			                        updateMigTableParam.put("image_path", saveFile.toString());
////    			                        updateMigTableParam.put("image_path", uploadPath + File.separator + saveFile.getName());
//    			                        updateMigTableParam.put("item_image_id", imageMap.get("ITEM_IMAGE_ID"));
//    			                        updateMigTableParam.put("item_id", imageMap.get("ITEM_ID"));
//    			                        updateMigTableParam.put("ordering", imageMap.get("ORDERING"));
//    			                        transferMapper.updateTempItemImageMigTables(updateMigTableParam);
//
//    			                        thumbnailCount++;
//
//    	                            } catch (IOException e) {
//    	                                log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException3 ========== :: imageName :: " + imageName, e);
//    	                            	throw new IOException(e);
//    	                            }
//    	                        }
//
//
//
//
//    	                        // insertItemImage
////    	                        ItemImage itemImage = new ItemImage();
//    	                        HashMap<String, Object> itemImageMap = new HashMap<>();
//    	                        itemImageMap.put("item_image_id", sequenceService.getId("OP_ITEM_IMAGE"));
//    	                        itemImageMap.put("item_id", imageMap.get("ITEM_ID"));
//    	                        itemImageMap.put("image_name", saveFileName.toString());
//    	                        itemImageMap.put("ordering", imageMap.get("ORDERING").toString());
//    	                        itemImageMap.put("serial_num", imageMap.get("SERIAL_NUM").toString());
//    	                        transferMapper.insertOpItemImage(itemImageMap);
//
//    	                        // if (list 의 ordering = 1 && 마지막 순번 XS 파일 일때) op_item 에 업데이트
////    	                        if ("1".equals(imageMap.get("ORDERING").toString())) {
////    	                        	HashMap<String, Object> opItemParamMap = new HashMap<>();
////    	                        	opItemParamMap.put("item_image", saveFileName.toString());
////    	                        	opItemParamMap.put("item_id", imageMap.get("ITEM_ID"));
////    	                        	transferMapper.updateOpItem(opItemParamMap);
////    	                        }
//
//    	                    } catch (IOException e) {
//    	                        log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: transferItemImage IOException4 ========== :: imageName :: " + imageName, e);
//    	                        throw new UserException("썸네일 생성중 오류");
//    		    			}
//
//
//
//
//    	    			} else {
//    	    				log.debug("파일이 존재하지 않습니다");
//    	    			}
//    				} catch (IOException e2) {
//    					log.debug("transferItemImage1 Error :: imageName :: " + imageName, e2);	
//    				} catch (Exception e) {
//    					log.debug("transferItemImage2 Error :: imageName :: " + imageName, e);	
//        			} finally {
//        				if(input != null) {
//        					try {
//        						input.close();
//        					} catch (IOException e) {
//        						log.debug("InputStream Close Error3 :: imageName :: " + imageName, e);	
//        					}
//        				}
//        			}
//    				
////        		}
//        		
//        		// 비어있는 폴더일 경우 삭제 처리
//        		File imageFilePath = new File(uploadPath.toString());
//        		if (imageFilePath.exists()) {
//            		File[] fileList = imageFilePath.listFiles();
//            		if (fileList == null || fileList.length == 0) {
//            			imageFilePath.delete();
//            		}
//        		}
//        	}
//
//
//    	}
//
//    	HashMap<String, Object> resultMap = new HashMap<>();
////    	resultMap.put("item_count", itemIdList.size());
//    	resultMap.put("item_count", imageList.size());
////    	resultMap.put("item_image_count", itemImageCount);
//    	resultMap.put("item_image_create_count", itemImageCreateCount);
//    	return resultMap;
//
    	return null;
    }



















    /**
     * TEMP 테이블 테스트 용도
     * TODO : 삭제예정
     *
     * @param count
     * @return
     */
//    public HashMap<String, Object> transferItemImageTempTest(String count, List<String> item_id_list) {
//    	/*
//    	 * <list> -> temp 테이블을 읽는다. group by item_id(정렬 item_id asc)
//    	 *
//    	 * for () -> Item List
//    	 *
//    	 * 		ㄴ<list> -> 이미지 리스트들을 list 로 조회한다.(order asc)
//    	 *
//    	 * 		image_nama 을 해당 폴더에서 찾는다.
//    	 *
//    	 *		for () -> image List
//    	 *
//    	 * 			for () -> (config 에 설정되어 있는 사이즈별로 4개)
//    	 *
//    	 * 				이미지를 사이즈 별로 만들어서 업로드한다.
//    	 *
//    	 * 				검증 : temp_item_image_mig_tables 에 컬럼을 추가 해서 해당 이미지 파일명을 저장한다.
//    	 *
//    	 * 				insertItemImage
//    	 *
//    	 * 				if (list 의 ordering = 1 && 마지막 순번 XS 파일 일때)
//    	 * 					op_item 에 없데이트
//    	 */
//
//    	// <list> -> temp 테이블을 읽는다. group by item_id(정렬 item_id asc)
//    	HashMap<String, Object> param = new HashMap<>();
//    	param.put("count", count);
//    	param.put("item_id_list", item_id_list);
//    	List<HashMap<String, Object>> itemIdList = transferMapper.getTempItemImageMigTablesGroupByItemIdList(param);
//
//    	int itemImageCount = 0;
//    	int itemImageCreateCount = 0;
//    	for (HashMap<String, Object> map : itemIdList ) {
//
//    		// ㄴ<list> -> 이미지 리스트들을 list 로 조회한다.(order asc)
//    		HashMap<String, Object> paramMap = new HashMap<>();
//    		paramMap.put("item_id", map.get("item_id"));
//    		List<HashMap<String, Object>> imageList = transferMapper.getTempItemImageMigTablesByItemIdList(paramMap);
//    		itemImageCount += imageList.size();	// result data
//
//    		String path = uploadRoot + File.separator + "migration" + File.separator;
////        	String path = "C://ghlove/workspace/ghlove/ghlove-web/storage/migration/";
//    		StringBuilder uploadPath = new StringBuilder();
//    		uploadPath
//    		.append(getUploadPath())
//    		.append(File.separator)
//    		.append(transferMapper.getTempItemCode(map.get("item_id").toString()));
//
//    		fileService.makeUploadPath(uploadPath.toString());
//
//    		for (HashMap<String, Object> imageMap : imageList ) {
//    			InputStream input = null;	
//    			try {
//
//    				File file = null;
//    				// image_nama 을 해당 폴더에서 찾는다.
//    				if (imageMap.get("image_name").toString().contains("http")) {
//    					URL url = new URL(imageMap.get("image_name").toString());
//    					file = readFileFromUrl( url );
//    				} else {
//    					file = new File(path + imageMap.get("image_name"));
//    				}
//
//    				DiskFileItem fileItem;
//    				fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length() , file.getParentFile());
//
//    				StringBuilder saveFileName = new StringBuilder();
//    				input = new FileInputStream(file);
//    				OutputStream os = fileItem.getOutputStream();
//    				IOUtils.copy(input, os);
//
//    				MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//
//
//    				if (file.isFile() && multipartFile.getSize() > 0) {
//
//
//    					BufferedImage bufferedImage = null;
//    					String dateNanoTime = DateUtils.getToday(Const.DATENANO_FORMAT);
//
//    					// 이미지를 사이즈별로 저장[2017-05-31]minae.yun
//    					try {
//    						// for () -> 이미지를 사이즈 별로 만들어서 업로드한다.(config 에 설정되어 있는 사이즈별로 4개)
//    						int thumbnailCount = 1;
//    						for (String sizeName : ShopUtils.getThumbnailType()) {
//    							itemImageCreateCount++;
//
//    							try (InputStream is = multipartFile.getInputStream();) {
//    								bufferedImage = ImageIO.read(is);
//    								if (bufferedImage == null) {
//    									throw new IOException();
//    								}
//    							} catch (IOException e1) {
//    								log.debug("이미지 읽어오기 실패 : {}", getClass().getName() + " :: saveItem IOException1 ==========");
//    								throw new UserException("이미지 읽어오기 실패");
//    							}
//
//    							// 2. 파일명
//    							saveFileName.setLength(0);
//    							saveFileName.append(dateNanoTime);
//    							saveFileName.append("_").append(sizeName).append(".").append(FileUtils.getExtension(multipartFile.getOriginalFilename()));
//
//    							// 3. 저장될 파일
//    							File saveFile = new File(new StringBuilder(uploadPath).append(File.separator).append(saveFileName).toString());
//    							int imageSize = ShopUtils.getImageSize(sizeName);
//
//    							// 이상우 [2017-03-31 수정] 이미지의 가로 세로 중 작은 쪽이 imageSize보다 크면 true, imageSize+10하면 사이즈 비율에 맞게 조절
//    							if (ShopUtils.checkImageSize2(bufferedImage.getWidth(), bufferedImage.getHeight(), imageSize)) {
//    								try {
//    									bufferedImage = ShopUtils.getThumbnailImage(bufferedImage, imageSize);
//    								} catch (IOException e) {
//    									log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException2 ==========");
//    									throw new UserException("썸네일 생성중 오류");
//    								}
//    							}
//    							try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
//    								output.flush();
//    								ImageIO.write(bufferedImage, FileUtils.getExtension(multipartFile.getOriginalFilename()), output);
//    								//	                            output.close();
//    								fileStorage.upload(output.toByteArray(), saveFile);
//
//    								// 검증 : temp_item_image_mig_tables 에 컬럼을 추가 해서 해당 이미지 파일명을 저장한다.
//    								HashMap<String, Object> updateMigTableParam = new HashMap<>();
//    								updateMigTableParam.put("image_no", Integer.toString(thumbnailCount));
//    								updateMigTableParam.put("image_path", saveFile.toString());
//    								updateMigTableParam.put("item_image_id", imageMap.get("item_image_id"));
//    								updateMigTableParam.put("item_id", imageMap.get("item_id"));
//    								updateMigTableParam.put("ordering", imageMap.get("ordering"));
//    								transferMapper.updateTempItemImageMigTables(updateMigTableParam);
//
//    								thumbnailCount++;
//
//    							} catch (IOException e) {
//    								log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: saveItem IOException3 ==========");
//    								throw new IOException(e);
//    							}
//    						}
//
//    						// insertItemImage
//    						ItemImage itemImage = new ItemImage();
//    						HashMap<String, Object> itemImageMap = new HashMap<>();
//    						itemImageMap.put("item_id", imageMap.get("item_id"));
//    						itemImageMap.put("image_name", saveFileName.toString());
//    						itemImageMap.put("ordering", imageMap.get("ordering").toString());
//    						transferMapper.insertTempOpItemImage(itemImageMap);
//
//    						// if (list 의 ordering = 1 && 마지막 순번 XS 파일 일때) op_item 에 없데이트
//    						if ("1".equals(imageMap.get("ordering").toString())) {
//    							HashMap<String, Object> opItemParamMap = new HashMap<>();
//    							opItemParamMap.put("item_image", saveFileName.toString());
//    							opItemParamMap.put("item_id", imageMap.get("item_id"));
//    							transferMapper.updateTempOpItem(opItemParamMap);
//    						}
//
//    					} catch (IOException e) {
//    						log.debug("썸네일 생성중 오류 : {}", getClass().getName() + " :: transferItemImage IOException4 ==========");
//    						throw new UserException("썸네일 생성중 오류");
//    					}
//
//
//
//
//    				} else {
//    					log.debug("파일이 존재하지 않습니다");
//    				}
//    			} catch (IOException e2) {
//    				log.debug("transferItemImageTempTest Error");
//    			} catch (Exception e) {
//    				log.debug("transferItemImageTempTest Error");
//    			} finally {
//    				if(input != null) {
//    					try {
//    						input.close();
//    					} catch (IOException e) {
//    						log.debug("InputStream Close Error");	
//    					}
//    				}
//    			}
//
//    		}
//
//
//    	}
//
//    	HashMap<String, Object> resultMap = new HashMap<>();
//    	resultMap.put("item_count", itemIdList.size());
//    	resultMap.put("item_image_count", itemImageCount);
//    	resultMap.put("item_image_create_count", itemImageCreateCount);
//    	return resultMap;
//
//    }


    public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("item")
				.toString();
	}


	/**
	 * read image file from url.
	 *
	 * @param imageUrl
	 * @return
	 * @throws IOException
	 */
	public static File readFileFromUrl(URL url) throws Exception {
//		InputStream inputStream = null;
//		try {
//			String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
//			URLConnection con = url.openConnection();
//			con.setRequestProperty("User-Agent", USER_AGENT);
//			inputStream = con.getInputStream();
//			File temp = File.createTempFile(UUID.randomUUID().toString(), url.toString().substring(url.toString().lastIndexOf(".")));
//			org.apache.commons.io.FileUtils.copyToFile(inputStream, temp);
//			return temp;
//		} finally {
//			IOUtils.closeQuietly(inputStream);
//		}
		return null;
	}



















	@Override
	public HashMap<String, Object> transferItemImageTempTest(String count, List<String> item_id_list) {
		// TODO Auto-generated method stub
		return null;
	}



}

/*
 * Copyright(c) 2009-2011 Onlinepowers Development Team
 * http://www.onlinepowers.com
 *
 * @file com.onlinepowers.framework.file.service.impl.FileServiceImpl.java
 * @author skc
 * @date 2011. 4. 29.
 */
package saleson.common.file.service;

import com.onlinepowers.framework.file.domain.FileValidator;
import com.onlinepowers.framework.file.domain.TempFiles;
import com.onlinepowers.framework.file.domain.UploadFile;
import com.onlinepowers.framework.file.handler.FileHandler;
import com.onlinepowers.framework.file.mapper.FileMapper;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.file.service.strategy.UploadTypeFactory;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.sequence.support.SequenceKey;
import com.onlinepowers.framework.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;
import saleson.common.file.infra.FileStorage;
import saleson.common.file.support.ThumbUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 파일 서비스 FileService 인터페이스르 구현한 클래스
 *
 * <pre>
 * OP_FILE 테이블과 연동되며  모든 파일 관련 정보를 관리한다.
 * - refCode : 모듈 코드  (예 : 게시판인 경우 게시판 코드, Popup 관련이면 popup 이 키가됨)
 * - refId : 모듈별 UniqueId
 * </pre>
 *
 * @author skc
 * @since 2011. 5. 2.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AzureFileService implements FileService {
	@Autowired
	private FileMapper fileMapper;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private FileHandler fileHandler;

	@Autowired
	private FileValidator fileValidator;

	@Autowired
	private FileStorage fileStorage;

	@Override
	public UploadFile getUploadFileById(int fileId) {
		return fileMapper.getById(fileId);
	}

	@Override
	public List<UploadFile> getUploadFileList(UploadFile uploadFile) {
		return fileMapper.getUploadFileList(uploadFile);
	}


	@Override
	public List<UploadFile> getUploadFileList(String refCode, int refId) {
		UploadFile uploadFile = new UploadFile();
		uploadFile.setRefCode(refCode);
		uploadFile.setRefId(refId);
		return fileMapper.getUploadFileList(uploadFile);
	}

	public void add(UploadFile uploadFile) {
		fileMapper.add(uploadFile);
	}

	public void deleteById(int fileId) {
		UploadFile uploadFile = fileMapper.getById(fileId);

		String uploadPath = getUploadPath(uploadFile);
		File file = new File(uploadPath, uploadFile.getFileName());
		file.delete();


		// thumbnail 삭제
		List<File> files = FileUtils.getFilesInDirectory(uploadFile.getRefCode() + File.separator + uploadFile.getRefId() + File.separator + "thumb");

		for (File thumbFile : files) {
			if (thumbFile.getName().indexOf(uploadFile.getFileName()) > -1){
				thumbFile.delete();
			}
		}

		fileMapper.deleteById(fileId);

	}

	@Override
	public void delete(UploadFile uploadFile) {
		fileMapper.deleteByUploadFile(uploadFile);
			fileStorage.delete("/" + uploadFile.getRefCode() + "/" + uploadFile.getRefId());
	}

	@Override
	public void delete(String refCode, int refId) {
		UploadFile uploadFile = new UploadFile();
		uploadFile.setRefCode(refCode);
		uploadFile.setRefId(refId);

		delete(uploadFile);
	}


	@Override
	public int upload(MultipartFile[] multipartFiles, String refCode, int refId) {
		return uploadProcess(multipartFiles, refCode, refId, null, "");
	}


	@Override
	public int upload(MultipartFile[] multipartFiles, String refCode, int refId, String useEncrypt) {
		return uploadProcess(multipartFiles, refCode, refId, null, useEncrypt);
	}


	@Override
	public int uploadWithThumbnail(MultipartFile[] multipartFiles, String refCode, int refId, String thumbnailSize, String useEncrypt) {
		return uploadProcess(multipartFiles, refCode, refId, thumbnailSize, useEncrypt);
	}


	@Override
	public int upload(int[] tempFileIds, String refCode, int refId) {
		return upload(tempFileIds, refCode, refId, "false");
	}


	@Override
	public int upload(int[] tempFileIds, String refCode, int refId, String useEncrypt) {
		return uploadProcessByTempFileIds(tempFileIds, refCode, refId, null, useEncrypt);
	}



	@Override
	public int uploadWithThumbnail(int[] tempFileIds, String refCode,
			int refId, String thumbnailSize) {
		return uploadWithThumbnail(tempFileIds, refCode, refId, thumbnailSize, "false");
	}



	@Override
	public int uploadWithThumbnail(int[] tempFileIds, String refCode,
			int refId, String thumbnailSize, String useEncrypt) {
		return uploadProcessByTempFileIds(tempFileIds, refCode, refId, thumbnailSize, useEncrypt);
	}




	@Override
	public void uploadTempSWF(MultipartFile multipartFile, UploadFile uploadFile) {
		// 업로드가 가능한 파일인지 확인 (확장자)
		fileValidator.validate(multipartFile);

		uploadFile.setFileSize((int) multipartFile.getSize());
		uploadFile.setFileType(FileUtils.getExtension(multipartFile));


		String uploadPath = FileUtils.getDefaultUploadPath() + File.separator + "temp"
							+ File.separator + DateUtils.getToday()+ File.separator
							+ uploadFile.getRefCode() ;
		makeUploadPath(uploadPath);


    	//String newFileName = FileUtils.getNewFileName(uploadPath, multipartFile);
		String newFileName = FileUtils.getNewFileName(uploadPath, multipartFile.getOriginalFilename());
    	uploadFile.setFileName(newFileName);

    	File saveFile = new File(uploadPath + File.separator + newFileName);

		try {
			//UploadTypeFactory.INSTANCE.getType(uploadFile.getUseEncrypt()).getHandler().upload(multipartFile.getInputStream(), new FileOutputStream(saveFile));
            //fileStorage.upload(multipartFile.getBytes(), saveFile);

			fileStorage.upload(multipartFile.getBytes(), saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

		fileMapper.addFileTemp(uploadFile);
	}



	@Override
	public void uploadTempEditor(MultipartFile multipartFile,
			UploadFile uploadFile) {

		// 업로드가 가능한 파일인지 확인 (확장자)
		fileValidator.validate(multipartFile);

		String fileExtension = FileUtils.getExtension(multipartFile);
		uploadFile.setFileSize((int) multipartFile.getSize());
		uploadFile.setFileType(fileExtension);

		// Temp 폴더에 업로드 (게시물 저장용)
		String uploadPath = FileUtils.getDefaultUploadPath() + File.separator + "temp"
							+ File.separator + DateUtils.getToday()+ File.separator
							+ uploadFile.getRefCode() ;
		makeUploadPath(uploadPath);

		// Editor 폴더에 업로드 (에디터 링크용)
		String editorUploadPath = FileUtils.getDefaultUploadPath() + File.separator + "editor"
							+ File.separator + DateUtils.getToday()+ File.separator
							+ uploadFile.getRefCode() ;
		makeUploadPath(editorUploadPath);


    	String newFileName = FileUtils.getNewFileName(uploadPath, "editor." + fileExtension, "_");
    	uploadFile.setFileName(newFileName);

    	File saveFile = new File(uploadPath + File.separator + newFileName);
    	File editorSaveFile = new File(editorUploadPath + File.separator + newFileName);
    	if(saveFile != null) {
    		try (FileOutputStream fos = new FileOutputStream(saveFile);
    				InputStream is = multipartFile.getInputStream();
    				InputStream is2 = multipartFile.getInputStream();
    				FileOutputStream editorFos = new FileOutputStream(editorSaveFile);) {
    			UploadTypeFactory.INSTANCE.getType(uploadFile.getUseEncrypt()).getHandler().upload(is, fos);
    			//fileStorage.upload(multipartFile.getBytes(), saveFile);


    			// Editor 폴더에 업로드 (에디터 링크용)
    			UploadTypeFactory.INSTANCE.getType(uploadFile.getUseEncrypt()).getHandler().upload(is2, editorFos);
    		} catch (IOException e) {
    			throw new RuntimeException(e);
    		}
    	}

		fileMapper.addFileTemp(uploadFile);
	}


	/**
	 * 업로드된 MultipartFile 파일을 해당 위치로 복사하고 섬네일을 생성한다.
	 * @param multipartFiles 업로드파일
	 * @param refCode 참조코드
	 * @param refId 참조ID
	 * @param thumbnailSize 섬네일 사이즈 "200x100;300x"
	 * @return
	 */
	private int uploadProcess(MultipartFile[] multipartFiles, String refCode, int refId, String thumbnailSize, String useEncrypt) {
		// 업로드가 가능한 파일인지 확인 (확장자)
		fileValidator.validate(multipartFiles);


		// 0. max sequence
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("refCode", refCode);
		map.put("refId", refId);

		int seq = fileMapper.getMaxSeq(map);


		for (MultipartFile multipartFile : multipartFiles) {
			if (multipartFile != null && multipartFile.getSize() > 0) {
				UploadFile uploadFile = new UploadFile();
				uploadFile.setRefCode(refCode);
				uploadFile.setRefId(refId);
				uploadFile.setOrdering(seq);
				uploadFile.setStatusCode("1");
				uploadFile.setUseEncrypt(useEncrypt);

				uploadFile.setFileId(sequenceService.getId(SequenceKey.FILE));
				uploadFile.setFileSize((int) multipartFile.getSize());
				uploadFile.setFileType(FileUtils.getExtension(multipartFile.getOriginalFilename()));

				String uploadPath = FileUtils.getDefaultUploadPath() + File.separator + uploadFile.getRefCode() + File.separator + uploadFile.getRefId();
				makeUploadPath(uploadPath);


				String newFileName = "";
				// 업로드한 파일명 그대로 저장
				if (FileUtils.getFilenameSaveOption().equals("1")) {
			    	newFileName = FileUtils.getNewFileName(uploadPath, multipartFile);
			    	uploadFile.setFileName(newFileName);
			    	File saveFile = new File(uploadPath + File.separator + newFileName);
				} else {  // {FileId}.확장자 형태로 저장
					newFileName = uploadFile.getFileId() + "." + uploadFile.getFileType();
					uploadFile.setFileName(multipartFile.getOriginalFilename());
				}

				File saveFile = new File(uploadPath + File.separator + newFileName);

				if(multipartFile != null) {
					try (InputStream is = multipartFile.getInputStream();
							FileOutputStream fos = new FileOutputStream(saveFile);) {
			            //fileStorage.upload(multipartFile.getBytes(), saveFile);
						UploadTypeFactory.INSTANCE.getType(uploadFile.getUseEncrypt()).getHandler().upload(is, fos);

			            // thumbnail 생성
			            if (!ValidationUtils.isNull(thumbnailSize)) {
			            	createThumbnail(saveFile, uploadPath, newFileName, thumbnailSize, useEncrypt);
			            }

			        } catch (IOException e) {
			            throw new RuntimeException(e);
			        }
				}


				fileMapper.add(uploadFile);

				seq++;
			}
		}

		// 5. 파일카운트
		return fileMapper.getFileCount(map);
	}


	/**
	 * SWFUpload, Editor를 이용한 파일 업로드인 경우의 처리
	 * <pre>
	 * 1. SWFUpload, Editor를 이용하여 업로드 한 경우 OP_FILE_TEMP에 파일 정보가 저장되며
	 *    업로드 기본 폴더 / temp / 오늘날짜(yyyymmdd) / token / 폴더에 저장된다.
	 * 2. OP_FILE 테이블에 파일정보 저장
	 * 3. 저장된 임시파일을 글 등록시 글의 위치로 임시파일을 복사한다.
	 * 4. thumbnailSize 가  null이 아닌 경우 thumbnail을 생성한다.
	 * 5. 임시 파일을 삭제한다.
	 * </pre>
	 * @param tempFileIds OP_FILE_TEMP에 FILE_ID 값
	 * @param refCode 참조코드 (모듈 ID)
	 * @param refId 참조ID (모듈별 UNIQUE ID)
	 * @param thumbnailSize 섬네일 생성 사이즈 - "200x100, 300x"
	 * @return OP_FILE 에 refCode, refId에 해당하는 총 파일 수를 반환한다.
	 */
	private int uploadProcessByTempFileIds(int[] tempFileIds, String refCode, int refId, String thumbnailSize, String useEncrypt){
		// 0. max sequence
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("refCode", refCode);
		map.put("refId", refId);

		int seq = fileMapper.getMaxSeq(map);

		String token = "";
		String creationDate = "";

		if (ValidationUtils.isNotNull(tempFileIds)) {
			for (int tempFileId : tempFileIds) {
				if (tempFileId != 0) {
					UploadFile uploadFile = fileMapper.getByTempId(tempFileId);

					token = uploadFile.getRefCode();

					uploadFile.setRefCode(refCode);
					uploadFile.setRefId(refId);
					uploadFile.setFileId(sequenceService.getId(SequenceKey.FILE));
					uploadFile.setStatusCode("1");
					uploadFile.setOrdering(seq);


					String uploadPath = getUploadPath(uploadFile);
					makeUploadPath(uploadPath);


			    	creationDate = uploadFile.getCreatedDate().substring(0, 8);


					// 1. 파일 카피
					File source = new File(FileUtils.getDefaultUploadPath() + File.separatorChar + "temp" + File.separatorChar + creationDate + File.separatorChar + token, uploadFile.getFileName());

					File target = null;
					if (FileUtils.getFilenameSaveOption().equals("1")) {
						target = new File(uploadPath, uploadFile.getFileName());
					} else {
						target = new File(uploadPath, uploadFile.getFileId() + "." + uploadFile.getFileType());
					}


					try {
						fileStorage.upload(source, target);

						// thumbnail 생성
			            if (!ValidationUtils.isNull(thumbnailSize)) {
			            	if (FileUtils.getFilenameSaveOption().equals("1")) {
			            		createThumbnail(source, uploadPath, uploadFile.getFileName(), thumbnailSize, useEncrypt);
			            	} else {
			            		createThumbnail(source, uploadPath, uploadFile.getFileId() + "." + uploadFile.getFileType(), thumbnailSize, useEncrypt);
			            	}
			            }

					} catch (IOException e) {
						//throw new OpRuntimeException(null, null);
						log.error(getClass().getName() + " :: uploadProcessByTempFileIds IOException =============");
					}

					// 2. db update
					fileMapper.add(uploadFile);

					seq++;
				}
			}
		}

		// 3. temp db 삭제
		fileMapper.deleteByToken(token);


		// 4. temp 파일 삭제
		fileStorage.delete("/temp/" + creationDate + "/" + token);


		// 5. 파일카운트
		return fileMapper.getFileCount(map);

	}

	@Override
	public int upload(int tempFileId, String refCode, int refId) {
		return upload(new int[]{tempFileId}, refCode, refId, "false");
	}

	@Override
	public int upload(TempFiles tempFiles) {
		// 0. max sequence
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("refCode", tempFiles.getRefCode());
		map.put("refId", tempFiles.getRefId());

		int seq = fileMapper.getMaxSeq(map);

		String token = "";
		String creationDate = "";
		List<UploadFile> uploadFiles = new ArrayList<UploadFile>();

		int processingCount = 0;
		if (ValidationUtils.isNotNull(tempFiles.getTempFileIds())) {
			int[] tempFileIds = tempFiles.getTempFileIds();
			String[] fileDescriptions = tempFiles.getTempFileDescriptions();

			for (int i = 0; i < tempFileIds.length; i++) {
				int tempFileId = tempFileIds[i];

				if (tempFileId != 0) {
					String fileDescription = fileDescriptions[i];

					UploadFile uploadFile = fileMapper.getByTempId(tempFileId);
					log.debug(" uploadFile !!!!!!!!!!!!!! : {}" ,uploadFile);
					token = uploadFile.getRefCode();

					uploadFile.setRefCode(tempFiles.getRefCode());
					uploadFile.setRefId(tempFiles.getRefId());
					uploadFile.setFileId(sequenceService.getId(SequenceKey.FILE));
					uploadFile.setFileDescription(fileDescription);
					uploadFile.setOrdering(seq);
					uploadFile.setStatusCode("1");

					String uploadPath = getUploadPath(uploadFile);
					makeUploadPath(uploadPath);

			    	creationDate = uploadFile.getCreatedDate().substring(0, 8);

					// 1. 파일 카피
					File source = new File(FileUtils.getDefaultUploadPath() + File.separatorChar + "temp" + File.separatorChar + creationDate + File.separatorChar + token, uploadFile.getFileName());

					File target = null;
					if (FileUtils.getFilenameSaveOption().equals("1")) {
						target = new File(uploadPath, uploadFile.getFileName());
					} else {
						target = new File(uploadPath, uploadFile.getFileId() + "." + uploadFile.getFileType());
					}

					uploadFile.setFile(target);

					try (FileInputStream fis = new FileInputStream(source);) {
						//fileStorage.upload(source, target);
						fileStorage.upload(fis, target);

						// thumbnail 생성
			            if (!ValidationUtils.isNull(tempFiles.getThumbnailSize())) {
			            	String useEncrypt = tempFiles.isEncrypt() ? "true" : "false";

			            	if (FileUtils.getFilenameSaveOption().equals("1")) {
			            		createThumbnail(source, uploadPath, uploadFile.getFileName(), tempFiles.getThumbnailSize(), useEncrypt);
			            	} else {
			            		createThumbnail(source, uploadPath, uploadFile.getFileId() + "." + uploadFile.getFileType(), tempFiles.getThumbnailSize(), useEncrypt);
			            	}
			            }

					} catch (IOException ignore) {
//						log.error("파일 복사 에러 : {} " , ignore.getMessage());
						log.error("파일 복사 에러 : {} " , getClass().getName() + " :: upload IOException ============");
					}

					// 2. db update
					fileMapper.add(uploadFile);

					uploadFiles.add(uploadFile);

					seq++;
					processingCount++;
				}
			}
		}

		// 3. temp db 삭제
		fileMapper.deleteByToken(token);


		// 4. temp 파일 삭제
		//fileStorage.delete("/temp/" + creationDate + "/" + token);
		fileStorage.delete(SalesonProperty.getUploadBaseFolder() + File.separator + "temp" + File.separator + creationDate + File.separator + token);
		// System.out.println(SalesonProperty.getUploadBaseFolder() + File.separator + "temp" + File.separator + creationDate + File.separator + token);


		// 5.파일 핸들러 실행. (임시 실행 중지)
		/*
		if (fileHandler != null && processingCount > 0) {
			fileHandler.postHandler(uploadFiles);
		}
		*/

		// 5. 파일카운트
		return fileMapper.getFileCount(map);



	}

	@Override
	public void createThumbnail(File source, String uploadFolder, String fileName, String thumbnailSize, String useEncrypt){

    	String thumbnailUploadPath = uploadFolder + File.separator + "thumb";
    	makeUploadPath(thumbnailUploadPath);

    	String ext = FileUtils.getExtension(fileName);

        if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg") || ext.toLowerCase().equals("gif") || ext.toLowerCase().equals("bmp") || ext.toLowerCase().equals("png")) {

        	String[] thumbnails = StringUtils.tokenizeToStringArray(thumbnailSize, ";");
        	for (String size : thumbnails) {
        		size = size.trim();

        		String[] imageSize = StringUtils.delimitedListToStringArray(size, "x");
        		int width = "".equals(imageSize[0]) || "0".equals(imageSize[0]) ? -1 : Integer.parseInt(imageSize[0]);
        		int height = "".equals(imageSize[1]) || "0".equals(imageSize[1]) ? -1 : Integer.parseInt(imageSize[1]);

        		//System.out.println(width + ", " + height);
				File thumbFile = new File(thumbnailUploadPath, size + "_"+ fileName);
				try {
					ThumbUtils.create(source, thumbFile, width, height, useEncrypt, fileStorage);

				} catch (IOException e) {
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail IOException ============");
				}			// 썸네일 생성
				catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail InvalidKeyException ============");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail NoSuchAlgorithmException ============");
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail IllegalBlockSizeException ============");
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail NoSuchPaddingException ============");
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail BadPaddingException ============");
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail InvalidKeySpecException ============");
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					log.error("Error : {} " , getClass().getName() + " :: createThumbnail InvalidAlgorithmParameterException ============");
				}
        	}
		}
	}





	/**
	 * UploadFile 정보를 기준으로 실제 업로드 Path를 반환한다.
	 * @param uploadFile UploadFile
	 * @return 업로드 path
	 */
	private String getUploadPath(UploadFile uploadFile) {
		return FileUtils.getDefaultUploadPath() + File.separator + uploadFile.getRefCode() + File.separator + uploadFile.getRefId();
		//return SalesonProperty.getSalesonUrlCdn() + SalesonProperty.getUploadBaseFolder() + "/" + uploadFile.getRefCode() + "/" + uploadFile.getRefId();
	}

	@Override
	public void makeUploadPath(String path) {
		File savePath = new File(path);
		if(!savePath.exists())
			savePath.mkdirs();
	}

	@Override
	public UploadFile getByTempId(int fileTempId) {
		return fileMapper.getByTempId(fileTempId);
	}

	@Override
	public List<String> getCreationDateList(String token) {
		return fileMapper.getCreationDateList(token);
	}

	@Override
	public int getFileCountByUploadFile(UploadFile uploadFile) {
		return fileMapper.getFileCountByUploadFile(uploadFile);
	}

	@Override
	public int uploadWithThumbnail(MultipartFile multipartFile, String refCode,
			int submainContentId, String thumbnailSize, String useEncrypt) {
		MultipartFile[] multipartFiles = new MultipartFile[1];
		multipartFiles[0] = multipartFile;

		return uploadWithThumbnail(multipartFiles, refCode, submainContentId, thumbnailSize, useEncrypt);
	}

	@Override
	public int upload(MultipartFile multipartFile, String refCode, int refId,
			String useEncrypt) {
		MultipartFile[] multipartFiles = new MultipartFile[1];
		multipartFiles[0] = multipartFile;

		return upload(multipartFiles, refCode, refId);
	}

	@Override
	public void updateUploadFile(UploadFile uploadFile) {
		fileMapper.updateUploadFile(uploadFile);
	}
}


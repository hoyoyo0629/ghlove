/*
 * Copyright(c) 2009-2011 Onlinepowers Development Team
 * http://www.onlinepowers.com
 *
 * @file com.onlinepowers.framework.util.ThumbnailUtils.java
 * @date 2011. 5. 2.
 */
package saleson.common.file.support;

import com.onlinepowers.framework.file.domain.UploadFile;
import com.onlinepowers.framework.file.security.service.CipherService;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.file.infra.FileStorage;
import saleson.common.multipart.MultipartImage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
public class ThumbUtils {


	/**
	 * 썸네일 생성
	 * @param orgName 원본 이미지 파일 경로
	 * @param destName 썸에릴로 저장될 이미지 파일 경로
	 * @param width 줄일 가로 길이
	 * @param height 줄일 세로 길이
	 * @return 썸네일 파일 이름
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeySpecException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public static void create(MultipartFile multipartFile, File destFile,
							  int width, int height, FileStorage fileStorage) throws IOException {
		if(destFile.getParentFile() != null) {
			if(!destFile.getParentFile().exists()){
				destFile.getParentFile().mkdir();
			}
		}

		Image orgImage = null;
		String fileExtension = FileUtils.getExtension(multipartFile.getOriginalFilename());


		if (fileExtension.equals("bmp") || fileExtension.equals("png") || fileExtension.equals("gif")) {
			orgImage = ImageIO.read(multipartFile.getInputStream());
		} else {
			orgImage = new ImageIcon(multipartFile.getBytes()).getImage();
		}

		if (orgImage != null) {
			int orgWidth = orgImage.getWidth(null);
			int orgHeight = orgImage.getHeight(null);
	
			// 섬네일 크기 계산.
			int destWidth = -1, destHeight = -1;
	
			// 0x300, 300x0 형태의 섬네일 생성 시 원본 이미지가 지정한 섬네일 사이즈 보다 작을 경우는 섬네일을 생성하지 않고 원본이미지를 복사한다.
			boolean isFileCopy = false;
	
			if (width > 0 && height > 0) {
				destWidth = width;
				destHeight = height;
			} else if (width > 0 && height < 0) {	// width 비율에 맞게 height 자동 계산.
				if (orgWidth <= width) {
					isFileCopy = true;
				}
				destWidth = width;
				destHeight = (orgHeight * width) / orgWidth;
	
			} else if (width < 0 && height > 0) {	// height 비율에 맞게 width 자동 계산.
				if (orgHeight <= height) {
					isFileCopy = true;
				}
				destWidth = (orgWidth * height) / orgHeight;
				destHeight = height;
			}
			
			
	
			// 섬네일 생성.
			if (isFileCopy) {
	
	//			FileCopyUtils.copy(multipartFile.getBytes(), destFile);
				fileStorage.upload(multipartFile.getBytes(), destFile);
	
			} else {
	
				Image targetImage = orgImage.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
				int pixels[] = new int[destWidth * destHeight];
				PixelGrabber pg = new PixelGrabber(targetImage, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
	
				try {
					pg.grabPixels();
				} catch (InterruptedException e) {
	//				throw new IOException(e.getMessage());
					throw new IOException(e);
				} catch (Exception e) {
	//				e.printStackTrace();
					log.error("ThumbUtils :: create InterruptedException =============");
				}
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
					BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
					destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
					
					ImageIO.write(destImg, fileExtension, baos);
					baos.flush();
	
					MultipartFile multipartFileNew = new MultipartImage(baos.toByteArray());
					fileStorage.upload(multipartFileNew.getBytes(), destFile);
				} catch (IOException e) {
					throw new IOException(e);
				}
			}
		}

	}

	public static String create(File orgFile, File destFile, int width, int height, String useEncrypt, FileStorage fileStorage) throws IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		if (destFile != null) {
			if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdir();
			}
		} else {
			throw new IOException("파일이 존재하지 않습니다.");
		}

		Object orgImage;
		if (useEncrypt.equals(UploadFile.USE_ENCRYPT_KEY)) {
			String parent = orgFile.getParent();
			orgImage = new File(parent + File.separator + "tempFile." + FileUtils.getExtension(orgFile.getName()));
			try (
					InputStream in = new FileInputStream(orgFile);
					FileOutputStream out = new FileOutputStream((File)orgImage);
					) {
				CipherService service = (CipherService) SpringUtils.getBean("cipherService");
				service.decrypt(in, out);
				orgFile = (File)orgImage;
			} catch (IOException e) {
				throw new IOException(e);
			}
		}

		orgImage = null;
		String suffix = orgFile.getName().substring(orgFile.getName().lastIndexOf(46) + 1).toLowerCase();
		log.debug("orgFile : {}", orgFile);
		log.debug("destFile : {}", destFile);
		log.debug("suffix : {}", suffix);

		if (!suffix.equals("bmp") && !suffix.equals("png") && !suffix.equals("gif")) {
			orgImage = (new ImageIcon(orgFile.toString())).getImage();
		} else {
			orgImage = ImageIO.read(orgFile);
		}

		log.debug("orgImage : {}", orgImage);
		int orgWidth = ((Image)orgImage).getWidth((ImageObserver)null);
		int orgHeight = ((Image)orgImage).getHeight((ImageObserver)null);
		int destWidth = -1;
		int destHeight = -1;
		boolean isFileCopy = false;
		if (width > 0 && height > 0) {
			destWidth = width;
			destHeight = height;
		} else if (width > 0 && height < 0) {
			if (orgWidth <= width) {
				isFileCopy = true;
			}

			destWidth = width;
			destHeight = orgHeight * width / orgWidth;
		} else if (width < 0 && height > 0) {
			if (orgHeight <= height) {
				isFileCopy = true;
			}

			destWidth = orgWidth * height / orgHeight;
			destHeight = height;
		}

		if (isFileCopy) {
			fileStorage.upload(orgFile, destFile);
		} else {
			Image targetImage = ((Image)orgImage).getScaledInstance(destWidth, destHeight, 4);
			int[] pixels = new int[destWidth * destHeight];
			PixelGrabber pg = new PixelGrabber(targetImage, 0, 0, destWidth, destHeight, pixels, 0, destWidth);

			try {
				pg.grabPixels();
			} catch (InterruptedException var16) {
//				throw new IOException(var16.getMessage());
				throw new IOException(var16);
			} catch (Exception var17) {
//				var17.printStackTrace();
				log.error("ThumbUtils :: create Exception ============");
			}

			BufferedImage destImg = new BufferedImage(destWidth, destHeight, 1);
			destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
			ImageIO.write(destImg, suffix, destFile);
			if (useEncrypt.equals(UploadFile.USE_ENCRYPT_KEY)) {
				fileStorage.delete(orgFile.getPath());
			}
		}

		return destFile.getName();
	}
}


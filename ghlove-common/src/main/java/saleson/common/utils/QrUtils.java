package saleson.common.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrUtils {
	
	/**
	 *  qr 이미지 bytes 로 가져오기
	 * @param qr에 넣을 데이터(String)
	 * @return byte[]
	 */
	public static byte[] makeQrCode(String data) {		
		if (data == null) {
			return null;
		}
		
		if (data.length() > 1500) {
			return null;
		}
		
		Map<EncodeHintType, Object> hintMap = new HashMap<>();
		hintMap.put(EncodeHintType.MARGIN, 0);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		int qrSize = 600;
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		byte[] qrCodeBytes = null;
		
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
			BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, qrSize, qrSize, hintMap);
			
			BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
	        
	        ImageIO.write(qrCodeImage,"png", byteArrayOutputStream);
	        byteArrayOutputStream.flush();
	        qrCodeBytes = byteArrayOutputStream.toByteArray();
		} catch (WriterException | IOException e) {
			return null;
		}
		
		return qrCodeBytes;
	}
	
	/**
	 *  qr 코드를 base64 이미지스트링으로 가져오기
	 * @param qr에 넣을 데이터(String)
	 * @return String
	 */
	public static String getQrCodeBase64String(String data) {
		byte[] qrImg = makeQrCode(data);
		if (qrImg != null  && qrImg.length > 0) {
			return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrImg);
		} else {
			return "";
		}
	}
	
}

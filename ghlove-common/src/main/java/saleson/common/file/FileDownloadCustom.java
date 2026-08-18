package saleson.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import com.onlinepowers.framework.exception.PageNotFoundException;

import saleson.common.utils.CommonUtils;

public class FileDownloadCustom {

	// 파일 다운로드 LocalFileStorage.download 기능 참고함, 파일명 지정 불가하여 별도 구현
	// fileFullPath : 파일 전체 경로(파일명 포함), setFileName : 다운로드시 지정할 파일 명
	public static ResponseEntity<byte[]> fileDownloadCustom(String fileFullPath, String setFileName) throws IOException {
		File file = new File(fileFullPath);
		if (file == null || !file.exists()) {
			throw new PageNotFoundException("파일이 없습니다.");
		}
		byte[] bytes = Files.readAllBytes(file.toPath());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		setFileName = URLEncoder.encode(setFileName, "UTF-8");		// 한글 파일명 조치
		httpHeaders.setContentDispositionFormData("attachment", setFileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	public static ResponseEntity<ByteArrayResource> fileDownload(String reqFileName, File file) throws FileNotFoundException, IOException {
		String fileName  = URLEncoder.encode(reqFileName, StandardCharsets.UTF_8);
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] bytes = FileCopyUtils.copyToByteArray(fis);
			ByteArrayResource resource = new ByteArrayResource(bytes);

			return ResponseEntity.ok()
					.headers(header)
					.contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		}
	}
}

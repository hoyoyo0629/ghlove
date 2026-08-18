package saleson.common.file.infra;

import com.onlinepowers.framework.file.domain.UploadFile;
import com.onlinepowers.framework.util.FileUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.file.domain.Path;
import saleson.common.utils.LocalDateUtils;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor
public class LocalFileStorage implements FileStorage {

	public static final String LOG_PREFIX = "[LocalFileStorage] ";

	@Value("${upload.root}")
	private String uploadRoot;

	@Value("${upload.save.folder}")
	private String defaultUploadPath;

	@Override
	public String getNewFileName(String fileName) {
		if (StringUtils.hasText(fileName)) {

			String newName = LocalDateUtils.localDateTimeToString(LocalDateTime.now(), Const.DATENANO_FORMAT);

			return new StringBuilder(newName)
					.append(".")
					.append(FileUtils.getExtension(fileName)).toString();
		}

		return fileName;
	}

	@Override
	public void upload(byte[] in, File out) throws IOException {
		try (ByteArrayInputStream inStream = new ByteArrayInputStream(in);
				InputStream is = (InputStream)inStream;) {
			upload(is, getUploadPath(out.getPath(), out.getName()));
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void upload(MultipartFile file, String uploadPath) throws IOException {

		if(file != null && file.getSize() > 0) {
			try (InputStream is = file.getInputStream();) {
				upload(is, getUploadPath(uploadPath, file.getName()));
			} catch (IOException e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void upload(InputStream in, String uploadPath) throws IOException {
		try {
			upload(in, new File(uploadPath));
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new IOException(e);
				}
			}
		}
	}

	@Override
	public void upload(InputStream in, File file) throws IOException {
		FileCopyUtils.copy(in.readAllBytes(), file);
	}

	@Override
	public void upload(File file, String uploadPath) throws IOException {
		try (InputStream is = new FileInputStream(file);) {
			upload(is, getUploadPath(uploadPath, file.getName()));
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void upload(File file, File saveFile) throws IOException {
		try (InputStream is = new FileInputStream(file);) {
			upload(is, saveFile.getPath());
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String uploadEditor(MultipartFile file, UploadFile uploadFile) {
		if (file == null || uploadFile == null) {
			return "";
		}
		try (InputStream is = file.getInputStream();) {

			StringBuilder sb = new StringBuilder()
					.append("/editor/")
					.append(uploadFile.getCreatedDate().substring(0, 8))
					.append("/")
					.append(uploadFile.getRefCode())
					.append("/")
					.append(uploadFile.getFileName());

			String uploadPath = new StringBuilder()
					.append(SalesonProperty.getUploadSaveFolder())
					.append(sb).toString();

			upload(is, uploadPath);

			return new StringBuilder()
					.append(SalesonProperty.getUploadBaseFolder())
					.append(sb).toString();
		} catch (IOException e) {
//			log.error(LOG_PREFIX + " uploadEditor error: {}", e.getMessage(), e);
			log.error(LOG_PREFIX + " uploadEditor error: {}", getClass().getName() + " :: uploadEditor IOException ============");
		}

		return "";
	}

	@Override
	public ResponseEntity<byte[]> download(String filePath) throws IOException {

		File file = new File(filePath);
		byte[] bytes = Files.readAllBytes(file.toPath());
		String fileName = file.getName();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	@Override
	public void delete(String filePath) {

		if (StringUtils.hasText(filePath)) {

			try {
				if (!filePath.startsWith(defaultUploadPath)) {
					filePath = defaultUploadPath +File.separator+ filePath;
				}

				File file = new File(filePath);
				if (file != null && file.isFile() && file.exists()) {
					file.delete();
				}
			} catch (RuntimeException e) {
//				log.warn(LOG_PREFIX + " delete error [{}]", filePath, e);
				log.error(LOG_PREFIX + " delete error [{}]", filePath, getClass().getName() + " :: delete RuntimeException ============");
			}

		}
	}
	@Override
	public void delete(String uploadPath, String fileName) {
		delete(uploadPath + File.separator + fileName);
	}


	private String getUploadPath(String uploadPath, String fileName) {
		Path path = new Path(uploadRoot, uploadPath);

		if (!StringUtils.hasText(path.getFilename())) {
			path.setFilename(fileName);
		}

		return new StringBuilder()
				.append(uploadRoot)
				.append(path.getPath())
				.append("/")
				.append(path.getFilename())
				.toString();
	}
}

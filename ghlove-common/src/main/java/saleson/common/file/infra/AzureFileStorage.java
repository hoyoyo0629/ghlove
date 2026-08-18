package saleson.common.file.infra;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.batch.BlobBatchClient;
import com.azure.storage.blob.batch.BlobBatchClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.onlinepowers.framework.file.domain.FileType;
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

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Slf4j
@NoArgsConstructor
public class AzureFileStorage implements FileStorage {
	public static final String LOG_PREFIX = "[AzureFileStorage] ";

	private BlobServiceClient blobServiceClient;
	private BlobBatchClient blobBatchClient;

	@Value("${file.storage.azure.accountName}")
	private String accountName;

	@Value("${file.storage.azure.accountKey}")
	private String accountKey;


	@Value("${upload.root}")
	private String uploadRoot;


	private String endPoint;


	public AzureFileStorage(String accountName, String accountKey, String uploadRoot) {
		this.accountName = accountName;
		this.accountKey = accountKey;
		this.uploadRoot = uploadRoot;

		initialize();
	}

	@PostConstruct
	private void initialize() {
		StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

		/*
		 * From the Azure portal, get your Storage account blob service URL endpoint.
		 * The URL typically looks like this:
		 */
		endPoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName);

		/*
		 * Create a BlobServiceClient object that wraps the service endpoint, credential and a request pipeline.
		 * Now you can use the blobServiceClient to perform various container and blob operations.
		 */
		this.blobServiceClient = new BlobServiceClientBuilder().endpoint(endPoint).credential(credential).buildClient();

		this.blobBatchClient = new BlobBatchClientBuilder(blobServiceClient).buildClient();
	}

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
		upload(new ByteArrayInputStream(in), out);
	}

	@Override
	public void upload(MultipartFile file, String uploadPath) throws IOException {
		BlobClient blob = getBlobClient(uploadPath, file.getOriginalFilename());
		BlobRequestConditions blobRequestConditions = new BlobRequestConditions();
//		if (!overwrite) {
//			blobRequestConditions.setIfNoneMatch(Constants.HeaderConstants.ETAG_WILDCARD);
//		}
		blob.uploadWithResponse(file.getInputStream(), file.getSize(), null, getBlobHttpHeaders(file.getOriginalFilename()), null, null, blobRequestConditions, null, Context.NONE);
	}


	@Override
	public void upload(InputStream in, String uploadPath) throws IOException {
		BlobClient blob = getBlobClient(uploadPath);

		File file = new File("./" + getPath(uploadPath).getFilename());
		FileCopyUtils.copy(in.readAllBytes(), file);

		blob.uploadFromFile(file.getPath(), null, getBlobHttpHeaders(file.getPath()), null, null, null, null);

		if (file != null) file.delete();
	}

	@Override
	public void upload(InputStream in, File file) throws IOException {
		BlobClient blob = getBlobClient(file.getAbsolutePath());

		FileCopyUtils.copy(in.readAllBytes(), file);
		//blob.uploadFromFile(file.getPath(), true);

		blob.uploadFromFile(file.getPath(), null, getBlobHttpHeaders(file.getPath()), null, null, null, null);

		if( !(file.getPath().contains("temp") || file.getPath().contains("notice") || file.getPath().contains("press") || file.getPath().contains("review")
				|| file.getPath().contains("words") || file.getPath().contains("guide") || file.getPath().contains("private")) ) {
			if (file != null) file.delete(); //로컬 템프파일 삭제?
		}
	}

	@Override
	public void upload(File file, String uploadPath) throws IOException {
		BlobClient blob = getBlobClient(uploadPath);
		blob.uploadFromFile(file.getPath(), null, getBlobHttpHeaders(file.getPath()), null, null, null, null);
	}

	@Override
	public String uploadEditor(MultipartFile file, UploadFile uploadFile) {

		try {
			String filePath = new StringBuilder()
					.append(SalesonProperty.getUploadBaseFolder())
					.append("/editor/")
					.append(uploadFile.getCreatedDate().substring(0, 8))
					.append("/")
					.append(uploadFile.getRefCode())
					.append("/")
					.append(uploadFile.getFileName())
					.toString();

			// 3. 저장될 파일
			File saveFile = new File( filePath );
			upload(file.getBytes(), saveFile);

			return filePath;
		} catch (IOException e) {
//			log.error(LOG_PREFIX + " uploadEditor error: {}", e.getMessage(), e);
			log.error(LOG_PREFIX + " uploadEditor error: {}", getClass().getName() + " :: uploadEditor IOException ==================");
		}

		return "";
	}

	@Override
	public ResponseEntity<byte[]> download(String filePath) throws IOException {
		Path path = getPath(filePath);

		BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(path.getPath());
		BlockBlobClient blobClient = blobContainer.getBlobClient(path.getFilename()).getBlockBlobClient();

		/*
		 * Download the blob's content to output stream.
		 */
		byte[] bytes;
		int dataSize = (int) blobClient.getProperties().getBlobSize();
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(dataSize);) {
			blobClient.downloadStream(outputStream);

			bytes = outputStream.toByteArray();
		} catch (RuntimeException e) {
			throw new IOException(e);
		}

		String fileName = URLEncoder.encode(path.getFilename(), "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	@Override
	public void upload(File file, File saveFile) throws IOException {

	}

	public void delete(String filePath) {

		Path path = getPath(filePath);

		if (!"".equals(path.getFilename())) {
			BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(path.getPath());
			BlockBlobClient blobClient = blobContainer.getBlobClient(path.getFilename()).getBlockBlobClient();
			blobClient.delete();

		} else {
			BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(path.getContainer());


			ListBlobsOptions options=new ListBlobsOptions();
			options.setPrefix(path.getPrefix());
			Duration fromDays = Duration.of(10, ChronoUnit.DAYS);

			blobContainer.listBlobs(options, fromDays)
					.forEach(blobItem -> {
						blobContainer.getBlobClient(blobItem.getName()).getBlockBlobClient().delete();
					});
		}
	}

	@Override
	public void delete(String uploadPath, String fileName) {
		delete(uploadPath + "/" + fileName);
	}

	private String getBlobUrl(String filePath) {
		if (!filePath.startsWith("https")) {
			filePath = filePath.startsWith("/") ? this.endPoint + filePath : this.endPoint + "/" + filePath;
		}

		if (!filePath.contains(endPoint)) {
			log.warn(LOG_PREFIX + "blobUrl이 포함되어 있지 않습니다. ");
		}

		return filePath;
	}


	private BlobHttpHeaders getBlobHttpHeaders(String filePath) {
		String ext = FileUtils.getExtension(filePath);

		// 확장자로 contentType 지정하기
		String mimeType = ((String) FileType.stream().filter((d) -> {
			return ext.equalsIgnoreCase(d.getExtension());
		}).map((d) -> {
			return d.getMimeType();
		}).findFirst().orElse("application/octet-stream")).toString();

		return new BlobHttpHeaders().setContentType(mimeType);
	}

	private BlobClient getBlobClient(String uploadPath, String originalFilename) {
		Path path = getPath(uploadPath);
		BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(path.getPath());
		return blobContainer.getBlobClient(originalFilename);
	}

	private BlobClient getBlobClient(String uploadPath) {
		Path path = getPath(uploadPath);
		BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(path.getPath());
		return blobContainer.getBlobClient(path.getFilename());
	}

	private Path getPath(String uploadPath) {
		return new Path(uploadRoot, uploadPath);
	}
}

package saleson.common.file.infra;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.onlinepowers.framework.file.domain.FileType;
import com.onlinepowers.framework.file.domain.UploadFile;
import com.onlinepowers.framework.util.FileUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.file.domain.Path;
import saleson.common.utils.LocalDateUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@NoArgsConstructor
public class AmazonS3Storage implements FileStorage {

	public static final String LOG_PREFIX = "[AmazonS3Storage] ";
	private AmazonS3 amazonS3;

	@Value("${file.storage.aws.accessKey}")
	private String accessKey;

	@Value("${file.storage.aws.secretKey}")
	private String secretKey;

	@Value("${file.storage.aws.bucketName}")
	private String bucketName;


	@Value("${upload.root}")
	private String uploadRoot;

	@PostConstruct
	private void initializeAmazon() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		amazonS3 =  AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(Regions.AP_NORTHEAST_2)
				.build();
	}

	public AmazonS3Storage(String accessKey, String secretKey, String bucketPrefix, String uploadRoot) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.bucketName = bucketPrefix;
		this.uploadRoot = uploadRoot;
		initializeAmazon();
	}


	/**
	 * filePath로 부터 key를 가져옴.
	 * @param uploadPath
	 * @return String
	 */
	private String getKey(String uploadPath) {
		uploadPath = uploadPath.replace("\\", "/")
				.replace(uploadRoot + "/", "");

		if (uploadPath.startsWith("/")) uploadPath = uploadPath.substring(1);
		return uploadPath;
	}


	/**
	 * 파일 확장자에 맞는 ObjectMetadata를 가져옴.
	 * @param filePath
	 * @return
	 */
	private ObjectMetadata getObjectMetadata(String filePath) {
		// 확장자
		String extension = FileUtils.getExtension(filePath);

		FileType contentType = Arrays.stream(FileType.values())
				.filter(c -> extension.equalsIgnoreCase(c.getExtension()))
				.findFirst().orElse(FileType.PSD);			// application/octet-stream

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType.getMimeType());

		return objectMetadata;
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

	/**
	 * S3에 업로드 (ByteArray)
	 * @param in
	 * @param filePath
	 * @return
	 */
	public void upload(byte[] in, String filePath) {
		try (ByteArrayInputStream inStream = new ByteArrayInputStream(in);
				InputStream is = (InputStream) inStream;) {
			upload(is, filePath);
		} catch (IOException e) {
			log.error(getClass().getName() + " :: upload IOException1 ==============");
		}
	}


	/**
	 * S3에 업로드 (MultipartFile)
	 * @param multipartFile
	 * @param filePath
	 * @return
	 */
	@Override
	public void upload(MultipartFile multipartFile, String filePath) {
		if(multipartFile != null) {
			try (InputStream is = multipartFile.getInputStream();) {
				upload(is, filePath);
			} catch (IOException e) {
				log.error(getClass().getName() + " :: upload IOException2 ==============");
			}
		}
	}


	/**
	 * S3에 업로드 (InputStream)
	 * @param inputStream
	 * @param uploadPath
	 * @return
	 */
	@Override
	public void upload(InputStream inputStream, String uploadPath) {
		String key = getKey(uploadPath);

		// Access (All Users, Permission.READ)
		//AccessControlList acl = new AccessControlList();
		//acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

		// File Object
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, getObjectMetadata(uploadPath));
		//putObjectRequest.setAccessControlList(acl);
		PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);


		log.debug("[S3] putObjectResult: {}", putObjectResult);

		IOUtils.closeQuietly(inputStream);
	}


	/**
	 * S3에 업로드
	 * @param in
	 * @param file
	 * @throws IOException
	 */
	@Override
	public void upload(InputStream in, File file) throws IOException {
		upload(in, file.getPath());
	}

	/**
	 * S3에 업로드
	 * @param file
	 * @param uploadPath
	 * @throws IOException
	 */
	@Override
	public void upload(File file, String uploadPath) throws IOException {
		try (InputStream is = new FileInputStream(file);) {
			upload(is, uploadPath);
		} catch (IOException e) {
			throw new IOException(e);
		}

		// 다른방법
		//amazonS3.putObject(	new PutObjectRequest(bucketPrefix, uploadPath, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	/**
	 * S3에 업로드
	 * @param file
	 * @param saveFile
	 * @throws IOException
	 */
	@Override
	public void upload(File file, File saveFile) throws IOException {
		try (InputStream is = new FileInputStream(file);) {
			upload(is, saveFile.getPath());
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	/**
	 * S3 파일 다운로드
	 * @param filePath "/bucketName/경로/파일명.jpg"
	 * @return
	 * @throws IOException
	 */
	@Override
	public ResponseEntity<byte[]> download(String filePath) throws IOException {
		S3ObjectInputStream objectInputStream = get(filePath);
		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		String fileName = URLEncoder.encode(getKey(filePath), "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	@Override
	public void upload(byte[] in, File out) throws IOException {
		try (ByteArrayInputStream inStream = new ByteArrayInputStream(in);
				InputStream is = (InputStream)inStream;) {
			upload(is, out.getPath());
		} catch (IOException e) {
			throw new IOException(e);
		}
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
			log.error(LOG_PREFIX + " uploadEditor error: {}", getClass().getName() + " :: uploadEditor IOException =============");
		}

		return "";
	}


	/**
	 * S3의 파일삭제
	 * @param filePath "경로/../파일명.jpg"
	 */
	@Override
	public void delete(String filePath) {
		String key = getKey(filePath);

		amazonS3.deleteObject(bucketName, key);
	}

	@Override
	public void delete(String uploadPath, String fileName) {
		delete(uploadPath + "/" + fileName);
	}

	/**
	 * S3 버킷의 파일 목록 조회
	 * @param bucketName prefix를 제외한 bucketName.
	 * @return
	 */
/*	public List<S3ObjectSummary> list(String bucketName) {
		String bucket = this.bucketName + bucketName;
		ObjectListing objectListing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(bucket));
		List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();
		return s3ObjectSummaries;

	}*/

	/**
	 * S3 파일 조회
	 * @param filePath "/bucketName/경로/파일명.jpg"
	 * @return
	 * @throws IOException
	 */
	private S3ObjectInputStream get(String filePath) throws IOException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, getKey(filePath));
		S3Object s3Object = amazonS3.getObject(getObjectRequest);
		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
		return objectInputStream;
	}

}

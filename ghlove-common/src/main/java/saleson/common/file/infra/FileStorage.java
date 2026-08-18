package saleson.common.file.infra;

import com.onlinepowers.framework.file.domain.UploadFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileStorage {

	String getNewFileName(String fileName);

	void upload(byte[] in, File out) throws IOException;

	void upload(MultipartFile file, String uploadPath) throws IOException;

	void upload(InputStream in, String uploadPath) throws IOException;


	void upload(InputStream in, File file) throws IOException;

	void upload(File file, String uploadPath) throws IOException;

	void upload(File file, File saveFile) throws IOException;

	String uploadEditor(MultipartFile file, UploadFile uploadFile);

	ResponseEntity<byte[]> download(String filePath) throws IOException;

    void delete(String filePath);

	void delete(String uploadPath, String fileName);
}

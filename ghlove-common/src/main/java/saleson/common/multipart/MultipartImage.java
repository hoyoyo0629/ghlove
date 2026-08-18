package saleson.common.multipart;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MultipartImage implements MultipartFile {


    private byte[] bytes;
    String name;
    String originalFilename;
    String contentType;
    boolean isEmpty;
    long size;

    public MultipartImage(byte[] bytes) {
    	if (bytes == null) {
    		this.bytes = null;
    	} else {
    		this.bytes = bytes.clone();
    	}
    	// 사용하는 클래스인가?
//        this.name = name;
//        this.originalFilename = originalFilename;
//        this.contentType = contentType;
//        this.size = size;
//        this.isEmpty = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() throws IOException {
    	if (bytes == null) {
    		return null;
    	} else {
    		return bytes.clone();
    	}
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        
    }
}
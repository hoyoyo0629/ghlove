package saleson.shop.brand;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.file.infra.FileStorage;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.domain.Seller;
import saleson.shop.brand.domain.Brand;
import saleson.shop.brand.support.BrandParam;

import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import saleson.shop.item.ItemMapper;

@Service("brandService")
public class BrandServiceImpl extends EgovAbstractServiceImpl implements BrandService {
	private static final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);
	
	@Autowired
	private SequenceService sequenceService;
	
	@Autowired
	private BrandMapper brandMapper;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private FileStorage fileStorage;

	@Autowired
	private ItemMapper itemMapper;

	@Override
	public int getBrandCount(BrandParam param) {
		return brandMapper.getBrandCount(param);
	}

	@Override
	public List<Brand> getBrandList(BrandParam param) {
		return brandMapper.getBrandList(param);
	}

	@Override
	public Brand getBrand(BrandParam param) {
		return brandMapper.getBrand(param);
	}

	@Override
	public Brand getBrandById(int brandId) {
		return brandMapper.getBrandById(brandId);
	}

	@Override
	public void insertBrand(Brand brand) {
		brand.setBrandId(sequenceService.getId("OP_BRAND"));
		processUploadedFile(brand);
		brandMapper.insertBrand(brand);
	}

	@Override
	public void updateBrand(Brand brand) {
		
		// 삭제 체크 시 처리.
		if ("Y".equals(brand.getBrandImageDeleteFlag())) {
			brand.setBrandImage("");
		}
		processUploadedFile(brand);
		brandMapper.updateBrand(brand);
		itemMapper.updateItemBrandName(brand);
	}

	/**
	 * 업로드 파일 처리.
	 * @param brand
	 */
	private void processUploadedFile(Brand brand) {
		if (brand.getFile() != null && brand.getFile().getSize() > 0) {
			MultipartFile multipartFile = brand.getFile();
			
			String SAVE_FOLDER = "brand";
			String defaultFileName = fileStorage.getNewFileName(multipartFile.getOriginalFilename());
				
			// 1. 업로드 경로설정
			String uploadPath = brand.getUploadPath();
			fileService.makeUploadPath(uploadPath);
	    	
			
			// 2. 파일명 중복파일 삭제
			fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(defaultFileName));
			
			// 2-1. 새로운 파일명.
			defaultFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

			// 3. 저장될 파일 
			File saveFile = new File(uploadPath + File.separator + defaultFileName);
			

			// 생성.
			try {
				fileStorage.upload(multipartFile.getBytes(), saveFile);
			} catch (IOException e) {
				log.error("fileStorage.upload(multipartFile.getBytes(), saveFile); : {}", e.getMessage(), e);
			}


			// 대표이미지 파일명
			brand.setBrandImage(defaultFileName);
		}
	}

	@Override
	public void deleteBrandById(int brandId) {
		brandMapper.deleteBrandById(brandId);
	}

	@Override
	public void deleteBrandData(ListParam listParam) {
		
		if (listParam.getId() != null) {

			for (String brandId : listParam.getId()) {
				brandMapper.deleteBrandById(Integer.parseInt(brandId));
			}	
		}
	}

	/**
	 * 저장된 파일 삭제
	 * @param Brand
	 */
	@Override
	public void deleteBrandImg(Brand brand) {
		fileStorage.delete(brand.getBrandImageSrc().replaceFirst("/upload", ""));
		brand.setBrandImage("");
		brandMapper.updateBrand(brand);
	}	
}

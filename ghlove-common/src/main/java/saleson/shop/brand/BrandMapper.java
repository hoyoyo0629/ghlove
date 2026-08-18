package saleson.shop.brand;

import java.util.List;

import saleson.shop.brand.domain.Brand;
import saleson.shop.brand.support.BrandParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("brandMapper")
public interface BrandMapper {
	
	/**
	 * 조건에 해당하는 데이터 수
	 * @param param
	 * @return
	 */
	int getBrandCount(BrandParam param);

	/**
	 * 조건에 해당하는 데이터 목록
	 * @param param
	 * @return
	 */
	List<Brand> getBrandList(BrandParam param);
	
	/**
	 * 조건에 해당하는 데이터 조회
	 * @param param
	 * @return
	 */
	Brand getBrand(BrandParam param);
	
	/**
	 * ID로 데이터 조회
	 * @param brandId
	 * @return
	 */
	Brand getBrandById(int brandId);
	
	/**
	 * 데이터 등록
	 * @param brand
	 */
	void insertBrand(Brand brand);
	
	/**
	 * 데이터 수정
	 * @param brand
	 */
	void updateBrand(Brand brand);
	
	/**
	 * ID로 데이터 삭제
	 * @param brandId
	 */
	void deleteBrandById(int brandId);
	
	/**
	 * (임시) 판매자 아이디로 지자체 코드 조회(관리자 지자체 코드 조회해오는 부분이 없다...)
	 * @param userId
	 */
	String getOpUserLocgovCode(BrandParam param);
	
	/**
	 * (임시) 관리자 아이디로 지자체 코드 조회(관리자 지자체 코드 조회해오는 부분이 없다...)
	 * @param userId
	 */
	String getOpManagerLocgovCode(BrandParam param);
}

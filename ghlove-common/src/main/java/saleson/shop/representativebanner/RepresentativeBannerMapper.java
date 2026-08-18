package saleson.shop.representativebanner;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;

import saleson.shop.representativebanner.domain.RepresentativeBanner;
import saleson.shop.representativebanner.domain.RepresentativeBannerListParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("representativeBannerMapper")
public interface RepresentativeBannerMapper {

	/**
	 * 대표 배너 목록
	 * @param RepresentativeBanner
	 * @return
	 */
	List<RepresentativeBanner> getRepresentativeBannerList(RepresentativeBannerListParam listParam);
	
	/**
	 *  대표 배너 순서 변경
	 */
//	@CacheEvict(value="frontCategories", allEntries=true)
	void updateRepresentativeBanneOrdering(RepresentativeBanner representativeBanner);	
	
	/**
	 * 대표 배너 정보
	 * @param representativeBannerId
	 * @return
	 */
	RepresentativeBanner getRepresentativeBannerInfo(int representativeBannerId);	
	
	/**
	 * 대표 배너 등록
	 * @param RepresentativeBanner
	 */
	void insertRepresentativeBanner(RepresentativeBanner groupBanner);
	
	/**
	 * 대표 배너 수정
	 * @param RepresentativeBanner
	 */
	void updateRepresentativeBanner(RepresentativeBanner groupBanner);
	
	/**
	 * 대표 배너 삭제
	 * @param representativeBannerId
	 */
	void deleteRepresentativeBanner(RepresentativeBanner representativeBanner);

	/**
	 * 대표 배너 목록(답례품 화면 표시용)
	 * @param RepresentativeBanner
	 * @return
	 */
	List<RepresentativeBanner> getRepresentativeBannerListFront(RepresentativeBannerListParam listParam);
	
	
	
}

package saleson.shop.representativebanner;

import java.util.List;

import saleson.shop.representativebanner.domain.RepresentativeBanner;
import saleson.shop.representativebanner.domain.RepresentativeBannerListParam;

public interface RepresentativeBannerService {

	/**
	 * 대표 배너를 등록하거나 수정한다.
	 * @param RepresentativeBanner
	 */
	public void editRepresentativeBanner(RepresentativeBanner representativeBanner);
	
	/**
	 * 대표 배너 목록
	 * @param RepresentativeBanner
	 * @return
	 */
	public List<RepresentativeBanner> getRepresentativeBannerList(RepresentativeBannerListParam listParam);	
	
	/**
	 * 대표 배너 순서 변경
	 * @param listParam
	 * @return
	 */	
	public void updateRepresentativeBanneOrdering(RepresentativeBannerListParam listParam);
	
	/**
	 * 대표 배너 정보
	 * @param representativeBannerId
	 * @return
	 */
	public RepresentativeBanner getRepresentativeBannerInfo(int representativeBannerId);
	
	/**
	 * 대표 배너 등록
	 * @param RepresentativeBanner
	 */
	public void insertRepresentativeBanner(RepresentativeBanner representativeBanner);
	
	/**
	 * 대표 배너 수정
	 * @param RepresentativeBanner
	 */
	public void updateRepresentativeBanner(RepresentativeBanner representativeBanner);
	
	/**
	 * 대표 배너 삭제
	 * @param representativeBannerId
	 */
	public void deleteRepresentativeBanner(RepresentativeBanner representativeBanner);
	
	/**
	 * 대표 배너 목록(답례품 표시용)
	 * @param RepresentativeBanner
	 * @return
	 */
	public List<RepresentativeBanner> getRepresentativeBannerListFront(RepresentativeBannerListParam listParam);	
	
}

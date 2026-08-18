package saleson.shop.banner;

import java.util.List;

import com.onlinepowers.framework.exception.OpRuntimeException;

import saleson.shop.banner.domain.MainBannerManager;
import saleson.shop.banner.support.MainBannerManagerSearchParam;

public interface MainBannerService {

	/**
	 * 메인 배너 등록
	 * @param mainBannerManager
	 * @return
	 */
	int insertMainBanner(MainBannerManager mainBannerManager) throws OpRuntimeException;

	/**
	 * 메인 배너 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<MainBannerManager> getMainBannerList(MainBannerManagerSearchParam searchParam);

	/**
	 * 메인 배너 순서 변경
	 * @param mainBannerManager
	 * @return
	 */
	int updateDisplayOrder(MainBannerManager mainBannerManager) throws OpRuntimeException;

	/**
	 * 메인 배너 상세 조회
	 * @param bannerId
	 * @return
	 */
	MainBannerManager getMainBannerDetails(Integer bannerId);

	/**
	 * 메인 배너 파일 삭제
	 * @param mainBannerManager
	 * @return
	 */
	int deleteMainBannerFile(MainBannerManager mainBannerManager) throws OpRuntimeException;

	/**
	 * 메인 배너 수정
	 * @param mainBannerManager
	 * @return
	 */
	int updateMainBanner(MainBannerManager mainBannerManager) throws OpRuntimeException;
}
